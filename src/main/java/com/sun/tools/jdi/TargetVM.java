/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class TargetVM
/*     */   implements Runnable
/*     */ {
/*  37 */   private Map<String, Packet> waitingQueue = new HashMap<>(32, 0.75F);
/*     */   private volatile boolean shouldListen = true;
/*  39 */   private List<EventQueue> eventQueues = Collections.synchronizedList(new ArrayList<>(2));
/*     */   private VirtualMachineImpl vm;
/*     */   private Connection connection;
/*     */   private Thread readerThread;
/*  43 */   private EventController eventController = null;
/*     */ 
/*     */   
/*     */   private boolean eventsHeld = false;
/*     */   
/*     */   private static final int OVERLOADED_QUEUE = 2000;
/*     */   
/*     */   private static final int UNDERLOADED_QUEUE = 100;
/*     */ 
/*     */   
/*     */   TargetVM(VirtualMachineImpl paramVirtualMachineImpl, Connection paramConnection) {
/*  54 */     this.vm = paramVirtualMachineImpl;
/*  55 */     this.connection = paramConnection;
/*  56 */     this.readerThread = new Thread(paramVirtualMachineImpl.threadGroupForJDI(), this, "JDI Target VM Interface");
/*     */     
/*  58 */     this.readerThread.setDaemon(true);
/*     */   }
/*     */   
/*     */   void start() {
/*  62 */     this.readerThread.start();
/*     */   }
/*     */   
/*     */   private void dumpPacket(Packet paramPacket, boolean paramBoolean) {
/*  66 */     String str = paramBoolean ? "Sending" : "Receiving";
/*  67 */     if (paramBoolean) {
/*  68 */       this.vm.printTrace(str + " Command. id=" + paramPacket.id + ", length=" + paramPacket.data.length + ", commandSet=" + paramPacket.cmdSet + ", command=" + paramPacket.cmd + ", flags=" + paramPacket.flags);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  74 */       String str1 = ((paramPacket.flags & 0x80) != 0) ? "Reply" : "Event";
/*     */       
/*  76 */       this.vm.printTrace(str + " " + str1 + ". id=" + paramPacket.id + ", length=" + paramPacket.data.length + ", errorCode=" + paramPacket.errorCode + ", flags=" + paramPacket.flags);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  81 */     StringBuffer stringBuffer = new StringBuffer(80);
/*  82 */     stringBuffer.append("0000: ");
/*  83 */     for (byte b = 0; b < paramPacket.data.length; b++) {
/*  84 */       if (b > 0 && b % 16 == 0) {
/*  85 */         this.vm.printTrace(stringBuffer.toString());
/*  86 */         stringBuffer.setLength(0);
/*  87 */         stringBuffer.append(String.valueOf(b));
/*  88 */         stringBuffer.append(": ");
/*  89 */         int j = stringBuffer.length();
/*  90 */         for (byte b1 = 0; b1 < 6 - j; b1++) {
/*  91 */           stringBuffer.insert(0, '0');
/*     */         }
/*     */       } 
/*  94 */       int i = 0xFF & paramPacket.data[b];
/*  95 */       String str1 = Integer.toHexString(i);
/*  96 */       if (str1.length() == 1) {
/*  97 */         stringBuffer.append('0');
/*     */       }
/*  99 */       stringBuffer.append(str1);
/* 100 */       stringBuffer.append(' ');
/*     */     } 
/* 102 */     if (stringBuffer.length() > 6) {
/* 103 */       this.vm.printTrace(stringBuffer.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/* 108 */     if ((this.vm.traceFlags & 0x1) != 0) {
/* 109 */       this.vm.printTrace("Target VM interface thread running");
/*     */     }
/* 111 */     Packet packet = null;
/*     */ 
/*     */     
/* 114 */     while (this.shouldListen) {
/*     */       Packet packet1;
/* 116 */       boolean bool = false;
/*     */       try {
/* 118 */         byte[] arrayOfByte = this.connection.readPacket();
/* 119 */         if (arrayOfByte.length == 0) {
/* 120 */           bool = true;
/*     */         }
/* 122 */         packet = Packet.fromByteArray(arrayOfByte);
/* 123 */       } catch (IOException iOException) {
/* 124 */         bool = true;
/*     */       } 
/*     */       
/* 127 */       if (bool) {
/* 128 */         this.shouldListen = false;
/*     */         try {
/* 130 */           this.connection.close();
/* 131 */         } catch (IOException iOException) {}
/*     */         
/*     */         break;
/*     */       } 
/* 135 */       if ((this.vm.traceFlags & VirtualMachineImpl.TRACE_RAW_RECEIVES) != 0) {
/* 136 */         dumpPacket(packet, false);
/*     */       }
/*     */       
/* 139 */       if ((packet.flags & 0x80) == 0) {
/*     */         
/* 141 */         handleVMCommand(packet);
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 147 */       this.vm.state().notifyCommandComplete(packet.id);
/* 148 */       String str = String.valueOf(packet.id);
/*     */       
/* 150 */       synchronized (this.waitingQueue) {
/* 151 */         packet1 = this.waitingQueue.get(str);
/*     */         
/* 153 */         if (packet1 != null) {
/* 154 */           this.waitingQueue.remove(str);
/*     */         }
/*     */       } 
/* 157 */       if (packet1 == null) {
/*     */ 
/*     */ 
/*     */         
/* 161 */         System.err.println("Recieved reply with no sender!");
/*     */         continue;
/*     */       } 
/* 164 */       packet1.errorCode = packet.errorCode;
/* 165 */       packet1.data = packet.data;
/* 166 */       packet1.replied = true;
/*     */       
/* 168 */       synchronized (packet1) {
/* 169 */         packet1.notify();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 175 */     this.vm.vmManager.disposeVirtualMachine((VirtualMachine)this.vm);
/* 176 */     if (this.eventController != null) {
/* 177 */       this.eventController.release();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     synchronized (this.eventQueues) {
/* 184 */       Iterator<EventQueue> iterator = this.eventQueues.iterator();
/* 185 */       while (iterator.hasNext()) {
/* 186 */         ((EventQueueImpl)iterator.next()).close();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 192 */     synchronized (this.waitingQueue) {
/* 193 */       Iterator<Packet> iterator = this.waitingQueue.values().iterator();
/* 194 */       while (iterator.hasNext()) {
/* 195 */         Packet packet1 = iterator.next();
/* 196 */         synchronized (packet1) {
/* 197 */           packet1.notify();
/*     */         } 
/*     */       } 
/* 200 */       this.waitingQueue.clear();
/*     */     } 
/*     */     
/* 203 */     if ((this.vm.traceFlags & 0x1) != 0) {
/* 204 */       this.vm.printTrace("Target VM interface thread exiting");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void handleVMCommand(Packet paramPacket) {
/* 209 */     switch (paramPacket.cmdSet) {
/*     */       case 64:
/* 211 */         handleEventCmdSet(paramPacket);
/*     */         return;
/*     */     } 
/*     */     
/* 215 */     System.err.println("Ignoring cmd " + paramPacket.id + "/" + paramPacket.cmdSet + "/" + paramPacket.cmd + " from the VM");
/*     */   }
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
/*     */   protected void handleEventCmdSet(Packet paramPacket) {
/* 231 */     EventSetImpl eventSetImpl = new EventSetImpl((VirtualMachine)this.vm, paramPacket);
/*     */     
/* 233 */     if (eventSetImpl != null) {
/* 234 */       queueEventSet(eventSetImpl);
/*     */     }
/*     */   }
/*     */   
/*     */   private EventController eventController() {
/* 239 */     if (this.eventController == null) {
/* 240 */       this.eventController = new EventController();
/*     */     }
/* 242 */     return this.eventController;
/*     */   }
/*     */   
/*     */   private synchronized void controlEventFlow(int paramInt) {
/* 246 */     if (!this.eventsHeld && paramInt > 2000) {
/* 247 */       eventController().hold();
/* 248 */       this.eventsHeld = true;
/* 249 */     } else if (this.eventsHeld && paramInt < 100) {
/* 250 */       eventController().release();
/* 251 */       this.eventsHeld = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   void notifyDequeueEventSet() {
/* 256 */     int i = 0;
/* 257 */     synchronized (this.eventQueues) {
/* 258 */       Iterator<EventQueue> iterator = this.eventQueues.iterator();
/* 259 */       while (iterator.hasNext()) {
/* 260 */         EventQueueImpl eventQueueImpl = (EventQueueImpl)iterator.next();
/* 261 */         i = Math.max(i, eventQueueImpl.size());
/*     */       } 
/*     */     } 
/* 264 */     controlEventFlow(i);
/*     */   }
/*     */   
/*     */   private void queueEventSet(EventSet paramEventSet) {
/* 268 */     int i = 0;
/*     */     
/* 270 */     synchronized (this.eventQueues) {
/* 271 */       Iterator<EventQueue> iterator = this.eventQueues.iterator();
/* 272 */       while (iterator.hasNext()) {
/* 273 */         EventQueueImpl eventQueueImpl = (EventQueueImpl)iterator.next();
/* 274 */         eventQueueImpl.enqueue(paramEventSet);
/* 275 */         i = Math.max(i, eventQueueImpl.size());
/*     */       } 
/*     */     } 
/*     */     
/* 279 */     controlEventFlow(i);
/*     */   }
/*     */   
/*     */   void send(Packet paramPacket) {
/* 283 */     String str = String.valueOf(paramPacket.id);
/*     */     
/* 285 */     synchronized (this.waitingQueue) {
/* 286 */       this.waitingQueue.put(str, paramPacket);
/*     */     } 
/*     */     
/* 289 */     if ((this.vm.traceFlags & VirtualMachineImpl.TRACE_RAW_SENDS) != 0) {
/* 290 */       dumpPacket(paramPacket, true);
/*     */     }
/*     */     
/*     */     try {
/* 294 */       this.connection.writePacket(paramPacket.toByteArray());
/* 295 */     } catch (IOException iOException) {
/* 296 */       throw new VMDisconnectedException(iOException.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   void waitForReply(Packet paramPacket) {
/* 301 */     synchronized (paramPacket) {
/* 302 */       while (!paramPacket.replied && this.shouldListen) { 
/* 303 */         try { paramPacket.wait(); } catch (InterruptedException interruptedException) {} }
/*     */ 
/*     */       
/* 306 */       if (!paramPacket.replied) {
/* 307 */         throw new VMDisconnectedException();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   void addEventQueue(EventQueueImpl paramEventQueueImpl) {
/* 313 */     if ((this.vm.traceFlags & 0x4) != 0) {
/* 314 */       this.vm.printTrace("New event queue added");
/*     */     }
/* 316 */     this.eventQueues.add(paramEventQueueImpl);
/*     */   }
/*     */   
/*     */   void stopListening() {
/* 320 */     if ((this.vm.traceFlags & 0x4) != 0) {
/* 321 */       this.vm.printTrace("Target VM i/f closing event queues");
/*     */     }
/* 323 */     this.shouldListen = false;
/*     */     try {
/* 325 */       this.connection.close();
/* 326 */     } catch (IOException iOException) {}
/*     */   }
/*     */   
/*     */   private class EventController extends Thread {
/* 330 */     int controlRequest = 0;
/*     */     
/*     */     EventController() {
/* 333 */       super(TargetVM.this.vm.threadGroupForJDI(), "JDI Event Control Thread");
/* 334 */       setDaemon(true);
/* 335 */       setPriority(7);
/* 336 */       start();
/*     */     }
/*     */     
/*     */     synchronized void hold() {
/* 340 */       this.controlRequest++;
/* 341 */       notifyAll();
/*     */     }
/*     */     
/*     */     synchronized void release() {
/* 345 */       this.controlRequest--;
/* 346 */       notifyAll();
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       while (true) {
/*     */         int i;
/* 352 */         synchronized (this) {
/* 353 */           while (this.controlRequest == 0) { 
/* 354 */             try { wait(); } catch (InterruptedException interruptedException) {}
/* 355 */             if (!TargetVM.this.shouldListen) {
/*     */               return;
/*     */             } }
/*     */           
/* 359 */           i = this.controlRequest;
/* 360 */           this.controlRequest = 0;
/*     */         } 
/*     */         try {
/* 363 */           if (i > 0) {
/* 364 */             JDWP.VirtualMachine.HoldEvents.process(TargetVM.this.vm); continue;
/*     */           } 
/* 366 */           JDWP.VirtualMachine.ReleaseEvents.process(TargetVM.this.vm);
/*     */         }
/* 368 */         catch (JDWPException jDWPException) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 373 */           jDWPException.toJDIException().printStackTrace(System.err);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\TargetVM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */