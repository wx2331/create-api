/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventQueueImpl
/*     */   extends MirrorImpl
/*     */   implements EventQueue
/*     */ {
/*  40 */   LinkedList<EventSet> eventSets = new LinkedList<>();
/*     */   
/*     */   TargetVM target;
/*     */   boolean closed = false;
/*     */   
/*     */   EventQueueImpl(VirtualMachine paramVirtualMachine, TargetVM paramTargetVM) {
/*  46 */     super(paramVirtualMachine);
/*  47 */     this.target = paramTargetVM;
/*  48 */     paramTargetVM.addEventQueue(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  55 */     return (this == paramObject);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  59 */     return System.identityHashCode(this);
/*     */   }
/*     */   
/*     */   synchronized void enqueue(EventSet paramEventSet) {
/*  63 */     this.eventSets.add(paramEventSet);
/*  64 */     notifyAll();
/*     */   }
/*     */   
/*     */   synchronized int size() {
/*  68 */     return this.eventSets.size();
/*     */   }
/*     */   
/*     */   synchronized void close() {
/*  72 */     if (!this.closed) {
/*  73 */       this.closed = true;
/*     */ 
/*     */       
/*  76 */       enqueue(new EventSetImpl((VirtualMachine)this.vm, (byte)100));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EventSet remove() throws InterruptedException {
/*  82 */     return remove(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventSet remove(long paramLong) throws InterruptedException {
/*     */     EventSet eventSet;
/*  90 */     if (paramLong < 0L) {
/*  91 */       throw new IllegalArgumentException("Timeout cannot be negative");
/*     */     }
/*     */ 
/*     */     
/*     */     do {
/*  96 */       EventSetImpl eventSetImpl = removeUnfiltered(paramLong);
/*  97 */       if (eventSetImpl == null) {
/*  98 */         Object object = null;
/*     */ 
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 107 */       eventSet = eventSetImpl.userFilter();
/* 108 */     } while (eventSet.isEmpty());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     if (eventSet != null && eventSet.suspendPolicy() == 2) {
/* 114 */       this.vm.notifySuspend();
/*     */     }
/*     */     
/* 117 */     return eventSet;
/*     */   }
/*     */ 
/*     */   
/*     */   EventSet removeInternal() throws InterruptedException {
/*     */     EventSet eventSet;
/*     */     do {
/* 124 */       eventSet = removeUnfiltered(0L).internalFilter();
/* 125 */     } while (eventSet == null || eventSet.isEmpty());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     return eventSet;
/*     */   }
/*     */   
/*     */   private TimerThread startTimerThread(long paramLong) {
/* 138 */     TimerThread timerThread = new TimerThread(paramLong);
/* 139 */     timerThread.setDaemon(true);
/* 140 */     timerThread.start();
/* 141 */     return timerThread;
/*     */   }
/*     */   
/*     */   private boolean shouldWait(TimerThread paramTimerThread) {
/* 145 */     return (!this.closed && this.eventSets.isEmpty() && (paramTimerThread == null || 
/* 146 */       !paramTimerThread.timedOut()));
/*     */   }
/*     */ 
/*     */   
/*     */   private EventSetImpl removeUnfiltered(long paramLong) throws InterruptedException {
/* 151 */     EventSetImpl eventSetImpl = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     this.vm.waitInitCompletion();
/*     */     
/* 159 */     synchronized (this) {
/* 160 */       if (!this.eventSets.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 165 */         eventSetImpl = (EventSetImpl)this.eventSets.removeFirst();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 182 */         TimerThread timerThread = null;
/*     */         try {
/* 184 */           if (paramLong > 0L) {
/* 185 */             timerThread = startTimerThread(paramLong);
/*     */           }
/*     */           
/* 188 */           while (shouldWait(timerThread)) {
/* 189 */             wait();
/*     */           }
/*     */         } finally {
/* 192 */           if (timerThread != null && !timerThread.timedOut()) {
/* 193 */             timerThread.interrupt();
/*     */           }
/*     */         } 
/*     */         
/* 197 */         if (this.eventSets.isEmpty()) {
/* 198 */           if (this.closed) {
/* 199 */             throw new VMDisconnectedException();
/*     */           }
/*     */         } else {
/* 202 */           eventSetImpl = (EventSetImpl)this.eventSets.removeFirst();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 209 */     if (eventSetImpl != null) {
/* 210 */       this.target.notifyDequeueEventSet();
/* 211 */       eventSetImpl.build();
/*     */     } 
/* 213 */     return eventSetImpl;
/*     */   }
/*     */   
/*     */   private class TimerThread extends Thread {
/*     */     private boolean timedOut = false;
/*     */     private long timeout;
/*     */     
/*     */     TimerThread(long param1Long) {
/* 221 */       super(EventQueueImpl.this.vm.threadGroupForJDI(), "JDI Event Queue Timer");
/* 222 */       this.timeout = param1Long;
/*     */     }
/*     */     
/*     */     boolean timedOut() {
/* 226 */       return this.timedOut;
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       try {
/* 231 */         Thread.sleep(this.timeout);
/* 232 */         EventQueueImpl eventQueueImpl = EventQueueImpl.this;
/* 233 */         synchronized (eventQueueImpl) {
/* 234 */           this.timedOut = true;
/* 235 */           eventQueueImpl.notifyAll();
/*     */         } 
/* 237 */       } catch (InterruptedException interruptedException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\EventQueueImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */