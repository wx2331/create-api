/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.event.BreakpointEvent;
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.event.ClassUnloadEvent;
/*     */ import com.sun.jdi.event.Event;
/*     */ import com.sun.jdi.event.EventIterator;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import com.sun.jdi.event.ExceptionEvent;
/*     */ import com.sun.jdi.event.LocatableEvent;
/*     */ import com.sun.jdi.event.MethodEntryEvent;
/*     */ import com.sun.jdi.event.MethodExitEvent;
/*     */ import com.sun.jdi.event.StepEvent;
/*     */ import com.sun.jdi.event.ThreadDeathEvent;
/*     */ import com.sun.jdi.event.ThreadStartEvent;
/*     */ import com.sun.jdi.event.VMDeathEvent;
/*     */ import com.sun.jdi.event.VMDisconnectEvent;
/*     */ import com.sun.jdi.event.VMStartEvent;
/*     */ import com.sun.jdi.event.WatchpointEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventHandler
/*     */   implements Runnable
/*     */ {
/*     */   EventNotifier notifier;
/*     */   Thread thread;
/*     */   volatile boolean connected = true;
/*     */   boolean completed = false;
/*     */   String shutdownMessageKey;
/*     */   boolean stopOnVMStart;
/*     */   private boolean vmDied;
/*     */   
/*     */   synchronized void shutdown() {
/*  58 */     this.connected = false;
/*  59 */     this.thread.interrupt();
/*  60 */     while (!this.completed) { 
/*  61 */       try { wait(); } catch (InterruptedException interruptedException) {} }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  67 */     EventQueue eventQueue = Env.vm().eventQueue();
/*  68 */     while (this.connected) {
/*     */       try {
/*  70 */         EventSet eventSet = eventQueue.remove();
/*  71 */         int i = 0;
/*  72 */         EventIterator eventIterator = eventSet.eventIterator();
/*  73 */         while (eventIterator.hasNext()) {
/*  74 */           i |= !handleEvent(eventIterator.nextEvent()) ? 1 : 0;
/*     */         }
/*     */         
/*  77 */         if (i != 0) {
/*  78 */           eventSet.resume(); continue;
/*  79 */         }  if (eventSet.suspendPolicy() == 2) {
/*  80 */           setCurrentThread(eventSet);
/*  81 */           this.notifier.vmInterrupted();
/*     */         } 
/*  83 */       } catch (InterruptedException interruptedException) {
/*     */       
/*  85 */       } catch (VMDisconnectedException vMDisconnectedException) {
/*  86 */         handleDisconnectedException();
/*     */         break;
/*     */       } 
/*     */     } 
/*  90 */     synchronized (this) {
/*  91 */       this.completed = true;
/*  92 */       notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean handleEvent(Event paramEvent) {
/*  97 */     this.notifier.receivedEvent(paramEvent);
/*     */     
/*  99 */     if (paramEvent instanceof ExceptionEvent)
/* 100 */       return exceptionEvent(paramEvent); 
/* 101 */     if (paramEvent instanceof BreakpointEvent)
/* 102 */       return breakpointEvent(paramEvent); 
/* 103 */     if (paramEvent instanceof WatchpointEvent)
/* 104 */       return fieldWatchEvent(paramEvent); 
/* 105 */     if (paramEvent instanceof StepEvent)
/* 106 */       return stepEvent(paramEvent); 
/* 107 */     if (paramEvent instanceof MethodEntryEvent)
/* 108 */       return methodEntryEvent(paramEvent); 
/* 109 */     if (paramEvent instanceof MethodExitEvent)
/* 110 */       return methodExitEvent(paramEvent); 
/* 111 */     if (paramEvent instanceof ClassPrepareEvent)
/* 112 */       return classPrepareEvent(paramEvent); 
/* 113 */     if (paramEvent instanceof ClassUnloadEvent)
/* 114 */       return classUnloadEvent(paramEvent); 
/* 115 */     if (paramEvent instanceof ThreadStartEvent)
/* 116 */       return threadStartEvent(paramEvent); 
/* 117 */     if (paramEvent instanceof ThreadDeathEvent)
/* 118 */       return threadDeathEvent(paramEvent); 
/* 119 */     if (paramEvent instanceof VMStartEvent) {
/* 120 */       return vmStartEvent(paramEvent);
/*     */     }
/* 122 */     return handleExitEvent(paramEvent);
/*     */   }
/*     */   
/*     */   EventHandler(EventNotifier paramEventNotifier, boolean paramBoolean) {
/* 126 */     this.vmDied = false; this.notifier = paramEventNotifier; this.stopOnVMStart = paramBoolean;
/*     */     this.thread = new Thread(this, "event-handler");
/* 128 */     this.thread.start(); } private boolean handleExitEvent(Event paramEvent) { if (paramEvent instanceof VMDeathEvent) {
/* 129 */       this.vmDied = true;
/* 130 */       return vmDeathEvent(paramEvent);
/* 131 */     }  if (paramEvent instanceof VMDisconnectEvent) {
/* 132 */       this.connected = false;
/* 133 */       if (!this.vmDied) {
/* 134 */         vmDisconnectEvent(paramEvent);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 139 */       ((TTY)this.notifier).setShuttingDown(true);
/* 140 */       Env.shutdown(this.shutdownMessageKey);
/* 141 */       return false;
/*     */     } 
/* 143 */     throw new InternalError(MessageOutput.format("Unexpected event type", new Object[] { paramEvent
/* 144 */             .getClass() })); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void handleDisconnectedException() {
/* 155 */     EventQueue eventQueue = Env.vm().eventQueue();
/* 156 */     while (this.connected) {
/*     */       try {
/* 158 */         EventSet eventSet = eventQueue.remove();
/* 159 */         EventIterator eventIterator = eventSet.eventIterator();
/* 160 */         while (eventIterator.hasNext()) {
/* 161 */           handleExitEvent((Event)eventIterator.next());
/*     */         }
/* 163 */       } catch (InterruptedException interruptedException) {
/*     */       
/* 165 */       } catch (InternalError internalError) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadReference eventThread(Event paramEvent) {
/* 172 */     if (paramEvent instanceof ClassPrepareEvent)
/* 173 */       return ((ClassPrepareEvent)paramEvent).thread(); 
/* 174 */     if (paramEvent instanceof LocatableEvent)
/* 175 */       return ((LocatableEvent)paramEvent).thread(); 
/* 176 */     if (paramEvent instanceof ThreadStartEvent)
/* 177 */       return ((ThreadStartEvent)paramEvent).thread(); 
/* 178 */     if (paramEvent instanceof ThreadDeathEvent)
/* 179 */       return ((ThreadDeathEvent)paramEvent).thread(); 
/* 180 */     if (paramEvent instanceof VMStartEvent) {
/* 181 */       return ((VMStartEvent)paramEvent).thread();
/*     */     }
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setCurrentThread(EventSet paramEventSet) {
/*     */     ThreadReference threadReference;
/* 189 */     if (paramEventSet.size() > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 194 */       Event event = paramEventSet.iterator().next();
/* 195 */       threadReference = eventThread(event);
/*     */     } else {
/* 197 */       threadReference = null;
/*     */     } 
/* 199 */     setCurrentThread(threadReference);
/*     */   }
/*     */   
/*     */   private void setCurrentThread(ThreadReference paramThreadReference) {
/* 203 */     ThreadInfo.invalidateAll();
/* 204 */     ThreadInfo.setCurrentThread(paramThreadReference);
/*     */   }
/*     */   
/*     */   private boolean vmStartEvent(Event paramEvent) {
/* 208 */     VMStartEvent vMStartEvent = (VMStartEvent)paramEvent;
/* 209 */     this.notifier.vmStartEvent(vMStartEvent);
/* 210 */     return this.stopOnVMStart;
/*     */   }
/*     */   
/*     */   private boolean breakpointEvent(Event paramEvent) {
/* 214 */     BreakpointEvent breakpointEvent = (BreakpointEvent)paramEvent;
/* 215 */     this.notifier.breakpointEvent(breakpointEvent);
/* 216 */     return true;
/*     */   }
/*     */   
/*     */   private boolean methodEntryEvent(Event paramEvent) {
/* 220 */     MethodEntryEvent methodEntryEvent = (MethodEntryEvent)paramEvent;
/* 221 */     this.notifier.methodEntryEvent(methodEntryEvent);
/* 222 */     return true;
/*     */   }
/*     */   
/*     */   private boolean methodExitEvent(Event paramEvent) {
/* 226 */     MethodExitEvent methodExitEvent = (MethodExitEvent)paramEvent;
/* 227 */     return this.notifier.methodExitEvent(methodExitEvent);
/*     */   }
/*     */   
/*     */   private boolean fieldWatchEvent(Event paramEvent) {
/* 231 */     WatchpointEvent watchpointEvent = (WatchpointEvent)paramEvent;
/* 232 */     this.notifier.fieldWatchEvent(watchpointEvent);
/* 233 */     return true;
/*     */   }
/*     */   
/*     */   private boolean stepEvent(Event paramEvent) {
/* 237 */     StepEvent stepEvent = (StepEvent)paramEvent;
/* 238 */     this.notifier.stepEvent(stepEvent);
/* 239 */     return true;
/*     */   }
/*     */   
/*     */   private boolean classPrepareEvent(Event paramEvent) {
/* 243 */     ClassPrepareEvent classPrepareEvent = (ClassPrepareEvent)paramEvent;
/* 244 */     this.notifier.classPrepareEvent(classPrepareEvent);
/*     */     
/* 246 */     if (!Env.specList.resolve(classPrepareEvent)) {
/* 247 */       MessageOutput.lnprint("Stopping due to deferred breakpoint errors.");
/* 248 */       return true;
/*     */     } 
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean classUnloadEvent(Event paramEvent) {
/* 255 */     ClassUnloadEvent classUnloadEvent = (ClassUnloadEvent)paramEvent;
/* 256 */     this.notifier.classUnloadEvent(classUnloadEvent);
/* 257 */     return false;
/*     */   }
/*     */   
/*     */   private boolean exceptionEvent(Event paramEvent) {
/* 261 */     ExceptionEvent exceptionEvent = (ExceptionEvent)paramEvent;
/* 262 */     this.notifier.exceptionEvent(exceptionEvent);
/* 263 */     return true;
/*     */   }
/*     */   
/*     */   private boolean threadDeathEvent(Event paramEvent) {
/* 267 */     ThreadDeathEvent threadDeathEvent = (ThreadDeathEvent)paramEvent;
/* 268 */     ThreadInfo.removeThread(threadDeathEvent.thread());
/* 269 */     return false;
/*     */   }
/*     */   
/*     */   private boolean threadStartEvent(Event paramEvent) {
/* 273 */     ThreadStartEvent threadStartEvent = (ThreadStartEvent)paramEvent;
/* 274 */     ThreadInfo.addThread(threadStartEvent.thread());
/* 275 */     this.notifier.threadStartEvent(threadStartEvent);
/* 276 */     return false;
/*     */   }
/*     */   
/*     */   public boolean vmDeathEvent(Event paramEvent) {
/* 280 */     this.shutdownMessageKey = "The application exited";
/* 281 */     this.notifier.vmDeathEvent((VMDeathEvent)paramEvent);
/* 282 */     return false;
/*     */   }
/*     */   
/*     */   public boolean vmDisconnectEvent(Event paramEvent) {
/* 286 */     this.shutdownMessageKey = "The application has been disconnected";
/* 287 */     this.notifier.vmDisconnectEvent((VMDisconnectEvent)paramEvent);
/* 288 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\EventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */