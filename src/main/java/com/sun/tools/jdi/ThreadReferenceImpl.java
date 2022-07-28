/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidStackFrameException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.MonitorInfo;
/*     */ import com.sun.jdi.NativeMethodException;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.BreakpointRequest;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadReferenceImpl
/*     */   extends ObjectReferenceImpl
/*     */   implements ThreadReference, VMListener
/*     */ {
/*     */   static final int SUSPEND_STATUS_SUSPENDED = 1;
/*     */   static final int SUSPEND_STATUS_BREAK = 2;
/*  38 */   private int suspendedZombieCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadGroupReference threadGroup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LocalCache localCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LocalCache
/*     */   {
/*     */     private LocalCache() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     JDWP.ThreadReference.Status status = null;
/*  68 */     List<StackFrame> frames = null;
/*  69 */     int framesStart = -1;
/*  70 */     int framesLength = 0;
/*  71 */     int frameCount = -1;
/*  72 */     List<ObjectReference> ownedMonitors = null;
/*  73 */     List<MonitorInfo> ownedMonitorsInfo = null;
/*  74 */     ObjectReference contendedMonitor = null;
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
/*     */     boolean triedCurrentContended = false;
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
/*     */   private void resetLocalCache() {
/* 103 */     this.localCache = new LocalCache();
/*     */   }
/*     */   
/*     */   private static class Cache extends ObjectReferenceImpl.Cache {
/*     */     private Cache() {}
/*     */     
/* 109 */     String name = null; }
/*     */   
/*     */   protected ObjectReferenceImpl.Cache newCache() {
/* 112 */     return new Cache();
/*     */   }
/*     */ 
/*     */   
/* 116 */   private List<WeakReference<ThreadListener>> listeners = new ArrayList<>();
/*     */ 
/*     */   
/*     */   ThreadReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/* 120 */     super(paramVirtualMachine, paramLong);
/* 121 */     resetLocalCache();
/* 122 */     this.vm.state().addListener(this);
/*     */   }
/*     */   
/*     */   protected String description() {
/* 126 */     return "ThreadReference " + uniqueID();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean vmNotSuspended(VMAction paramVMAction) {
/* 133 */     if (paramVMAction.resumingThread() == null)
/*     */     {
/* 135 */       synchronized (this.vm.state()) {
/* 136 */         processThreadAction(new ThreadAction(this, 2));
/*     */       } 
/*     */     }
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
/* 151 */     return super.vmNotSuspended(paramVMAction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 160 */     String str = null;
/*     */     try {
/* 162 */       Cache cache = (Cache)getCache();
/*     */       
/* 164 */       if (cache != null) {
/* 165 */         str = cache.name;
/*     */       }
/* 167 */       if (str == null) {
/* 168 */         str = (JDWP.ThreadReference.Name.process(this.vm, this)).threadName;
/*     */         
/* 170 */         if (cache != null) {
/* 171 */           cache.name = str;
/*     */         }
/*     */       } 
/* 174 */     } catch (JDWPException jDWPException) {
/* 175 */       throw jDWPException.toJDIException();
/*     */     } 
/* 177 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PacketStream sendResumingCommand(CommandSender paramCommandSender) {
/* 185 */     synchronized (this.vm.state()) {
/* 186 */       processThreadAction(new ThreadAction(this, 2));
/*     */       
/* 188 */       return paramCommandSender.send();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void suspend() {
/*     */     try {
/* 194 */       JDWP.ThreadReference.Suspend.process(this.vm, this);
/* 195 */     } catch (JDWPException jDWPException) {
/* 196 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resume() {
/*     */     PacketStream packetStream;
/* 207 */     if (this.suspendedZombieCount > 0) {
/* 208 */       this.suspendedZombieCount--;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 213 */     synchronized (this.vm.state()) {
/* 214 */       processThreadAction(new ThreadAction(this, 2));
/*     */       
/* 216 */       packetStream = JDWP.ThreadReference.Resume.enqueueCommand(this.vm, this);
/*     */     } 
/*     */     try {
/* 219 */       JDWP.ThreadReference.Resume.waitForReply(this.vm, packetStream);
/* 220 */     } catch (JDWPException jDWPException) {
/* 221 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int suspendCount() {
/* 229 */     if (this.suspendedZombieCount > 0) {
/* 230 */       return this.suspendedZombieCount;
/*     */     }
/*     */     
/*     */     try {
/* 234 */       return (JDWP.ThreadReference.SuspendCount.process(this.vm, this)).suspendCount;
/* 235 */     } catch (JDWPException jDWPException) {
/* 236 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop(ObjectReference paramObjectReference) throws InvalidTypeException {
/* 241 */     validateMirrorOrNull((Mirror)paramObjectReference);
/*     */     
/* 243 */     List<ReferenceType> list = this.vm.classesByName("java.lang.Throwable");
/* 244 */     ClassTypeImpl classTypeImpl = (ClassTypeImpl)list.get(0);
/* 245 */     if (paramObjectReference == null || 
/* 246 */       !classTypeImpl.isAssignableFrom(paramObjectReference)) {
/* 247 */       throw new InvalidTypeException("Not an instance of Throwable");
/*     */     }
/*     */     
/*     */     try {
/* 251 */       JDWP.ThreadReference.Stop.process(this.vm, this, (ObjectReferenceImpl)paramObjectReference);
/*     */     }
/* 253 */     catch (JDWPException jDWPException) {
/* 254 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void interrupt() {
/*     */     try {
/* 260 */       JDWP.ThreadReference.Interrupt.process(this.vm, this);
/* 261 */     } catch (JDWPException jDWPException) {
/* 262 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private JDWP.ThreadReference.Status jdwpStatus() {
/* 267 */     LocalCache localCache = this.localCache;
/* 268 */     JDWP.ThreadReference.Status status = localCache.status;
/*     */     try {
/* 270 */       if (status == null) {
/* 271 */         status = JDWP.ThreadReference.Status.process(this.vm, this);
/* 272 */         if ((status.suspendStatus & 0x1) != 0)
/*     */         {
/* 274 */           localCache.status = status;
/*     */         }
/*     */       } 
/* 277 */     } catch (JDWPException jDWPException) {
/* 278 */       throw jDWPException.toJDIException();
/*     */     } 
/* 280 */     return status;
/*     */   }
/*     */   
/*     */   public int status() {
/* 284 */     return (jdwpStatus()).threadStatus;
/*     */   }
/*     */   
/*     */   public boolean isSuspended() {
/* 288 */     return (this.suspendedZombieCount > 0 || (
/* 289 */       (jdwpStatus()).suspendStatus & 0x1) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAtBreakpoint() {
/*     */     try {
/* 297 */       StackFrame stackFrame = frame(0);
/* 298 */       Location location = stackFrame.location();
/* 299 */       List list = this.vm.eventRequestManager().breakpointRequests();
/* 300 */       Iterator<BreakpointRequest> iterator = list.iterator();
/* 301 */       while (iterator.hasNext()) {
/* 302 */         BreakpointRequest breakpointRequest = iterator.next();
/* 303 */         if (location.equals(breakpointRequest.location())) {
/* 304 */           return true;
/*     */         }
/*     */       } 
/* 307 */       return false;
/* 308 */     } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
/* 309 */       return false;
/* 310 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*     */       
/* 312 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadGroupReference threadGroup() {
/* 320 */     if (this.threadGroup == null) {
/*     */       try {
/* 322 */         this
/* 323 */           .threadGroup = (JDWP.ThreadReference.ThreadGroup.process(this.vm, this)).group;
/* 324 */       } catch (JDWPException jDWPException) {
/* 325 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     }
/* 328 */     return this.threadGroup;
/*     */   }
/*     */   
/*     */   public int frameCount() throws IncompatibleThreadStateException {
/* 332 */     LocalCache localCache = this.localCache;
/*     */     try {
/* 334 */       if (localCache.frameCount == -1) {
/* 335 */         localCache
/* 336 */           .frameCount = (JDWP.ThreadReference.FrameCount.process(this.vm, this)).frameCount;
/*     */       }
/* 338 */     } catch (JDWPException jDWPException) {
/* 339 */       switch (jDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/* 342 */           throw new IncompatibleThreadStateException();
/*     */       } 
/* 344 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 347 */     return localCache.frameCount;
/*     */   }
/*     */   
/*     */   public List<StackFrame> frames() throws IncompatibleThreadStateException {
/* 351 */     return privateFrames(0, -1);
/*     */   }
/*     */   
/*     */   public StackFrame frame(int paramInt) throws IncompatibleThreadStateException {
/* 355 */     List<StackFrame> list = privateFrames(paramInt, 1);
/* 356 */     return list.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSubrange(LocalCache paramLocalCache, int paramInt1, int paramInt2) {
/* 366 */     if (paramInt1 < paramLocalCache.framesStart) {
/* 367 */       return false;
/*     */     }
/* 369 */     if (paramInt2 == -1) {
/* 370 */       return (paramLocalCache.framesLength == -1);
/*     */     }
/* 372 */     if (paramLocalCache.framesLength == -1) {
/* 373 */       if (paramInt1 + paramInt2 > paramLocalCache.framesStart + paramLocalCache.frames
/* 374 */         .size()) {
/* 375 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 377 */       return true;
/*     */     } 
/* 379 */     return (paramInt1 + paramInt2 <= paramLocalCache.framesStart + paramLocalCache.framesLength);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StackFrame> frames(int paramInt1, int paramInt2) throws IncompatibleThreadStateException {
/* 384 */     if (paramInt2 < 0) {
/* 385 */       throw new IndexOutOfBoundsException("length must be greater than or equal to zero");
/*     */     }
/*     */     
/* 388 */     return privateFrames(paramInt1, paramInt2);
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
/*     */   private synchronized List<StackFrame> privateFrames(int paramInt1, int paramInt2) throws IncompatibleThreadStateException {
/* 400 */     LocalCache localCache = this.localCache; try {
/*     */       int j;
/* 402 */       if (localCache.frames == null || !isSubrange(localCache, paramInt1, paramInt2)) {
/*     */ 
/*     */         
/* 405 */         JDWP.ThreadReference.Frames.Frame[] arrayOfFrame = (JDWP.ThreadReference.Frames.process(this.vm, this, paramInt1, paramInt2)).frames;
/* 406 */         j = arrayOfFrame.length;
/* 407 */         localCache.frames = new ArrayList<>(j);
/*     */         
/* 409 */         for (byte b = 0; b < j; b++) {
/* 410 */           if ((arrayOfFrame[b]).location == null) {
/* 411 */             throw new InternalException("Invalid frame location");
/*     */           }
/* 413 */           StackFrameImpl stackFrameImpl = new StackFrameImpl((VirtualMachine)this.vm, this, (arrayOfFrame[b]).frameID, (arrayOfFrame[b]).location);
/*     */ 
/*     */ 
/*     */           
/* 417 */           localCache.frames.add(stackFrameImpl);
/*     */         } 
/* 419 */         localCache.framesStart = paramInt1;
/* 420 */         localCache.framesLength = paramInt2;
/* 421 */         return Collections.unmodifiableList(localCache.frames);
/*     */       } 
/* 423 */       int i = paramInt1 - localCache.framesStart;
/*     */       
/* 425 */       if (paramInt2 == -1) {
/* 426 */         j = localCache.frames.size() - i;
/*     */       } else {
/* 428 */         j = i + paramInt2;
/*     */       } 
/* 430 */       return Collections.unmodifiableList(localCache.frames.subList(i, j));
/*     */     }
/* 432 */     catch (JDWPException jDWPException) {
/* 433 */       switch (jDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/* 436 */           throw new IncompatibleThreadStateException();
/*     */       } 
/* 438 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectReference> ownedMonitors() throws IncompatibleThreadStateException {
/* 444 */     LocalCache localCache = this.localCache;
/*     */     try {
/* 446 */       if (localCache.ownedMonitors == null) {
/* 447 */         localCache.ownedMonitors = Arrays.asList(
/*     */             
/* 449 */             (ObjectReference[])(JDWP.ThreadReference.OwnedMonitors.process(this.vm, this)).owned);
/* 450 */         if ((this.vm.traceFlags & 0x10) != 0) {
/* 451 */           this.vm.printTrace(description() + " temporarily caching owned monitors (count = " + localCache.ownedMonitors
/*     */               
/* 453 */               .size() + ")");
/*     */         }
/*     */       } 
/* 456 */     } catch (JDWPException jDWPException) {
/* 457 */       switch (jDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/* 460 */           throw new IncompatibleThreadStateException();
/*     */       } 
/* 462 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 465 */     return localCache.ownedMonitors;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectReference currentContendedMonitor() throws IncompatibleThreadStateException {
/* 470 */     LocalCache localCache = this.localCache;
/*     */     try {
/* 472 */       if (localCache.contendedMonitor == null && !localCache.triedCurrentContended) {
/*     */         
/* 474 */         localCache
/* 475 */           .contendedMonitor = (JDWP.ThreadReference.CurrentContendedMonitor.process(this.vm, this)).monitor;
/* 476 */         localCache.triedCurrentContended = true;
/* 477 */         if (localCache.contendedMonitor != null && (this.vm.traceFlags & 0x10) != 0)
/*     */         {
/* 479 */           this.vm.printTrace(description() + " temporarily caching contended monitor (id = " + localCache.contendedMonitor
/*     */               
/* 481 */               .uniqueID() + ")");
/*     */         }
/*     */       } 
/* 484 */     } catch (JDWPException jDWPException) {
/* 485 */       switch (jDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/* 488 */           throw new IncompatibleThreadStateException();
/*     */       } 
/* 490 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 493 */     return localCache.contendedMonitor;
/*     */   }
/*     */   
/*     */   public List<MonitorInfo> ownedMonitorsAndFrames() throws IncompatibleThreadStateException {
/* 497 */     LocalCache localCache = this.localCache;
/*     */     try {
/* 499 */       if (localCache.ownedMonitorsInfo == null)
/*     */       {
/* 501 */         JDWP.ThreadReference.OwnedMonitorsStackDepthInfo.monitor[] arrayOfMonitor = (JDWP.ThreadReference.OwnedMonitorsStackDepthInfo.process(this.vm, this)).owned;
/*     */         
/* 503 */         localCache.ownedMonitorsInfo = new ArrayList<>(arrayOfMonitor.length);
/*     */         
/* 505 */         for (byte b = 0; b < arrayOfMonitor.length; b++) {
/* 506 */           JDWP.ThreadReference.OwnedMonitorsStackDepthInfo.monitor monitor = arrayOfMonitor[b];
/*     */           
/* 508 */           MonitorInfoImpl monitorInfoImpl = new MonitorInfoImpl((VirtualMachine)this.vm, (arrayOfMonitor[b]).monitor, this, (arrayOfMonitor[b]).stack_depth);
/* 509 */           localCache.ownedMonitorsInfo.add(monitorInfoImpl);
/*     */         } 
/*     */         
/* 512 */         if ((this.vm.traceFlags & 0x10) != 0) {
/* 513 */           this.vm.printTrace(description() + " temporarily caching owned monitors (count = " + localCache.ownedMonitorsInfo
/*     */               
/* 515 */               .size() + ")");
/*     */         }
/*     */       }
/*     */     
/* 519 */     } catch (JDWPException jDWPException) {
/* 520 */       switch (jDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/* 523 */           throw new IncompatibleThreadStateException();
/*     */       } 
/* 525 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 528 */     return localCache.ownedMonitorsInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void popFrames(StackFrame paramStackFrame) throws IncompatibleThreadStateException {
/* 535 */     if (!paramStackFrame.thread().equals(this)) {
/* 536 */       throw new IllegalArgumentException("frame does not belong to this thread");
/*     */     }
/* 538 */     if (!this.vm.canPopFrames()) {
/* 539 */       throw new UnsupportedOperationException("target does not support popping frames");
/*     */     }
/*     */     
/* 542 */     ((StackFrameImpl)paramStackFrame).pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forceEarlyReturn(Value paramValue) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException {
/*     */     StackFrameImpl stackFrameImpl;
/* 548 */     if (!this.vm.canForceEarlyReturn()) {
/* 549 */       throw new UnsupportedOperationException("target does not support the forcing of a method to return early");
/*     */     }
/*     */ 
/*     */     
/* 553 */     validateMirrorOrNull((Mirror)paramValue);
/*     */ 
/*     */     
/*     */     try {
/* 557 */       stackFrameImpl = (StackFrameImpl)frame(0);
/* 558 */     } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
/* 559 */       throw new InvalidStackFrameException("No more frames on the stack");
/*     */     } 
/* 561 */     stackFrameImpl.validateStackFrame();
/* 562 */     MethodImpl methodImpl = (MethodImpl)stackFrameImpl.location().method();
/* 563 */     ValueImpl valueImpl = ValueImpl.prepareForAssignment(paramValue, methodImpl
/* 564 */         .getReturnValueContainer());
/*     */     
/*     */     try {
/* 567 */       JDWP.ThreadReference.ForceEarlyReturn.process(this.vm, this, valueImpl);
/* 568 */     } catch (JDWPException jDWPException) {
/* 569 */       switch (jDWPException.errorCode()) {
/*     */         case 32:
/* 571 */           throw new NativeMethodException();
/*     */         case 13:
/* 573 */           throw new IncompatibleThreadStateException("Thread not suspended");
/*     */         
/*     */         case 15:
/* 576 */           throw new IncompatibleThreadStateException("Thread has not started or has finished");
/*     */         
/*     */         case 31:
/* 579 */           throw new InvalidStackFrameException("No more frames on the stack");
/*     */       } 
/*     */       
/* 582 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 588 */     return "instance of " + referenceType().name() + "(name='" + 
/* 589 */       name() + "', id=" + uniqueID() + ")";
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 593 */     return 116;
/*     */   }
/*     */   
/*     */   void addListener(ThreadListener paramThreadListener) {
/* 597 */     synchronized (this.vm.state()) {
/* 598 */       this.listeners.add(new WeakReference<>(paramThreadListener));
/*     */     } 
/*     */   }
/*     */   
/*     */   void removeListener(ThreadListener paramThreadListener) {
/* 603 */     synchronized (this.vm.state()) {
/* 604 */       Iterator<WeakReference<ThreadListener>> iterator = this.listeners.iterator();
/* 605 */       while (iterator.hasNext()) {
/* 606 */         WeakReference weakReference = iterator.next();
/* 607 */         if (paramThreadListener.equals(weakReference.get())) {
/* 608 */           iterator.remove();
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processThreadAction(ThreadAction paramThreadAction) {
/* 621 */     synchronized (this.vm.state()) {
/* 622 */       Iterator<WeakReference<ThreadListener>> iterator = this.listeners.iterator();
/* 623 */       while (iterator.hasNext()) {
/* 624 */         WeakReference<ThreadListener> weakReference = iterator.next();
/* 625 */         ThreadListener threadListener = weakReference.get();
/* 626 */         if (threadListener != null) {
/* 627 */           switch (paramThreadAction.id()) {
/*     */             case 2:
/* 629 */               if (!threadListener.threadResumable(paramThreadAction)) {
/* 630 */                 iterator.remove();
/*     */               }
/*     */               continue;
/*     */           } 
/*     */           continue;
/*     */         } 
/* 636 */         iterator.remove();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 641 */       resetLocalCache();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ThreadReferenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */