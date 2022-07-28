/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.NativeMethodException;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.request.AccessWatchpointRequest;
/*     */ import com.sun.jdi.request.BreakpointRequest;
/*     */ import com.sun.jdi.request.ClassPrepareRequest;
/*     */ import com.sun.jdi.request.ClassUnloadRequest;
/*     */ import com.sun.jdi.request.DuplicateRequestException;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import com.sun.jdi.request.ExceptionRequest;
/*     */ import com.sun.jdi.request.InvalidRequestStateException;
/*     */ import com.sun.jdi.request.MethodEntryRequest;
/*     */ import com.sun.jdi.request.MethodExitRequest;
/*     */ import com.sun.jdi.request.ModificationWatchpointRequest;
/*     */ import com.sun.jdi.request.MonitorContendedEnterRequest;
/*     */ import com.sun.jdi.request.MonitorContendedEnteredRequest;
/*     */ import com.sun.jdi.request.MonitorWaitRequest;
/*     */ import com.sun.jdi.request.MonitorWaitedRequest;
/*     */ import com.sun.jdi.request.StepRequest;
/*     */ import com.sun.jdi.request.ThreadDeathRequest;
/*     */ import com.sun.jdi.request.ThreadStartRequest;
/*     */ import com.sun.jdi.request.VMDeathRequest;
/*     */ import com.sun.jdi.request.WatchpointRequest;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ class EventRequestManagerImpl
/*     */   extends MirrorImpl
/*     */   implements EventRequestManager
/*     */ {
/*     */   List<? extends EventRequest>[] requestLists;
/*  47 */   private static int methodExitEventCmd = 0;
/*     */   
/*     */   static int JDWPtoJDISuspendPolicy(byte paramByte) {
/*  50 */     switch (paramByte) {
/*     */       case 2:
/*  52 */         return 2;
/*     */       case 1:
/*  54 */         return 1;
/*     */       case 0:
/*  56 */         return 0;
/*     */     } 
/*  58 */     throw new IllegalArgumentException("Illegal policy constant: " + paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   static byte JDItoJDWPSuspendPolicy(int paramInt) {
/*  63 */     switch (paramInt) {
/*     */       case 2:
/*  65 */         return 2;
/*     */       case 1:
/*  67 */         return 1;
/*     */       case 0:
/*  69 */         return 0;
/*     */     } 
/*  71 */     throw new IllegalArgumentException("Illegal policy constant: " + paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  79 */     return (this == paramObject);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  83 */     return System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract class EventRequestImpl
/*     */     extends MirrorImpl
/*     */     implements EventRequest
/*     */   {
/*     */     int id;
/*     */     
/*  94 */     List<Object> filters = new ArrayList();
/*     */     
/*     */     boolean isEnabled = false;
/*     */     boolean deleted = false;
/*  98 */     byte suspendPolicy = 2;
/*  99 */     private Map<Object, Object> clientProperties = null;
/*     */     
/*     */     EventRequestImpl() {
/* 102 */       super((VirtualMachine)EventRequestManagerImpl.this.vm);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 110 */       return (this == param1Object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 114 */       return System.identityHashCode(this);
/*     */     }
/*     */     
/*     */     abstract int eventCmd();
/*     */     
/*     */     InvalidRequestStateException invalidState() {
/* 120 */       return new InvalidRequestStateException(toString());
/*     */     }
/*     */     
/*     */     String state() {
/* 124 */       return this.deleted ? " (deleted)" : (
/* 125 */         isEnabled() ? " (enabled)" : " (disabled)");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     List requestList() {
/* 132 */       return EventRequestManagerImpl.this.requestList(eventCmd());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void delete() {
/* 139 */       if (!this.deleted) {
/* 140 */         requestList().remove(this);
/* 141 */         disable();
/* 142 */         this.deleted = true;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/* 147 */       return this.isEnabled;
/*     */     }
/*     */     
/*     */     public void enable() {
/* 151 */       setEnabled(true);
/*     */     }
/*     */     
/*     */     public void disable() {
/* 155 */       setEnabled(false);
/*     */     }
/*     */     
/*     */     public synchronized void setEnabled(boolean param1Boolean) {
/* 159 */       if (this.deleted) {
/* 160 */         throw invalidState();
/*     */       }
/* 162 */       if (param1Boolean != this.isEnabled) {
/* 163 */         if (this.isEnabled) {
/* 164 */           clear();
/*     */         } else {
/* 166 */           set();
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void addCountFilter(int param1Int) {
/* 173 */       if (isEnabled() || this.deleted) {
/* 174 */         throw invalidState();
/*     */       }
/* 176 */       if (param1Int < 1) {
/* 177 */         throw new IllegalArgumentException("count is less than one");
/*     */       }
/* 179 */       this.filters.add(JDWP.EventRequest.Set.Modifier.Count.create(param1Int));
/*     */     }
/*     */     
/*     */     public void setSuspendPolicy(int param1Int) {
/* 183 */       if (isEnabled() || this.deleted) {
/* 184 */         throw invalidState();
/*     */       }
/* 186 */       this.suspendPolicy = EventRequestManagerImpl.JDItoJDWPSuspendPolicy(param1Int);
/*     */     }
/*     */     
/*     */     public int suspendPolicy() {
/* 190 */       return EventRequestManagerImpl.JDWPtoJDISuspendPolicy(this.suspendPolicy);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     synchronized void set() {
/* 198 */       JDWP.EventRequest.Set.Modifier[] arrayOfModifier = this.filters.<JDWP.EventRequest.Set.Modifier>toArray(
/* 199 */           new JDWP.EventRequest.Set.Modifier[this.filters.size()]);
/*     */       try {
/* 201 */         this.id = (JDWP.EventRequest.Set.process(this.vm, (byte)eventCmd(), this.suspendPolicy, arrayOfModifier)).requestID;
/*     */       }
/* 203 */       catch (JDWPException jDWPException) {
/* 204 */         throw jDWPException.toJDIException();
/*     */       } 
/* 206 */       this.isEnabled = true;
/*     */     }
/*     */     
/*     */     synchronized void clear() {
/*     */       try {
/* 211 */         JDWP.EventRequest.Clear.process(this.vm, (byte)eventCmd(), this.id);
/* 212 */       } catch (JDWPException jDWPException) {
/* 213 */         throw jDWPException.toJDIException();
/*     */       } 
/* 215 */       this.isEnabled = false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Map<Object, Object> getProperties() {
/* 224 */       if (this.clientProperties == null) {
/* 225 */         this.clientProperties = new HashMap<>(2);
/*     */       }
/* 227 */       return this.clientProperties;
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
/*     */     public final Object getProperty(Object param1Object) {
/* 239 */       if (this.clientProperties == null) {
/* 240 */         return null;
/*     */       }
/* 242 */       return getProperties().get(param1Object);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void putProperty(Object param1Object1, Object param1Object2) {
/* 252 */       if (param1Object2 != null) {
/* 253 */         getProperties().put(param1Object1, param1Object2);
/*     */       } else {
/* 255 */         getProperties().remove(param1Object1);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   abstract class ThreadVisibleEventRequestImpl extends EventRequestImpl {
/*     */     public synchronized void addThreadFilter(ThreadReference param1ThreadReference) {
/* 262 */       validateMirror((Mirror)param1ThreadReference);
/* 263 */       if (isEnabled() || this.deleted) {
/* 264 */         throw invalidState();
/*     */       }
/* 266 */       this.filters.add(
/* 267 */           JDWP.EventRequest.Set.Modifier.ThreadOnly.create((ThreadReferenceImpl)param1ThreadReference));
/*     */     }
/*     */   }
/*     */   
/*     */   abstract class ClassVisibleEventRequestImpl
/*     */     extends ThreadVisibleEventRequestImpl {
/*     */     public synchronized void addClassFilter(ReferenceType param1ReferenceType) {
/* 274 */       validateMirror((Mirror)param1ReferenceType);
/* 275 */       if (isEnabled() || this.deleted) {
/* 276 */         throw invalidState();
/*     */       }
/* 278 */       this.filters.add(
/* 279 */           JDWP.EventRequest.Set.Modifier.ClassOnly.create((ReferenceTypeImpl)param1ReferenceType));
/*     */     }
/*     */     
/*     */     public synchronized void addClassFilter(String param1String) {
/* 283 */       if (isEnabled() || this.deleted) {
/* 284 */         throw invalidState();
/*     */       }
/* 286 */       if (param1String == null) {
/* 287 */         throw new NullPointerException();
/*     */       }
/* 289 */       this.filters.add(
/* 290 */           JDWP.EventRequest.Set.Modifier.ClassMatch.create(param1String));
/*     */     }
/*     */     
/*     */     public synchronized void addClassExclusionFilter(String param1String) {
/* 294 */       if (isEnabled() || this.deleted) {
/* 295 */         throw invalidState();
/*     */       }
/* 297 */       if (param1String == null) {
/* 298 */         throw new NullPointerException();
/*     */       }
/* 300 */       this.filters.add(
/* 301 */           JDWP.EventRequest.Set.Modifier.ClassExclude.create(param1String));
/*     */     }
/*     */     
/*     */     public synchronized void addInstanceFilter(ObjectReference param1ObjectReference) {
/* 305 */       validateMirror((Mirror)param1ObjectReference);
/* 306 */       if (isEnabled() || this.deleted) {
/* 307 */         throw invalidState();
/*     */       }
/* 309 */       if (!this.vm.canUseInstanceFilters()) {
/* 310 */         throw new UnsupportedOperationException("target does not support instance filters");
/*     */       }
/*     */       
/* 313 */       this.filters.add(
/* 314 */           JDWP.EventRequest.Set.Modifier.InstanceOnly.create((ObjectReferenceImpl)param1ObjectReference));
/*     */     }
/*     */   }
/*     */   
/*     */   class BreakpointRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements BreakpointRequest {
/*     */     private final Location location;
/*     */     
/*     */     BreakpointRequestImpl(Location param1Location) {
/* 323 */       this.location = param1Location;
/* 324 */       this.filters.add(0, 
/* 325 */           JDWP.EventRequest.Set.Modifier.LocationOnly.create(param1Location));
/* 326 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     public Location location() {
/* 330 */       return this.location;
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 334 */       return 2;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 338 */       return "breakpoint request " + location() + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class ClassPrepareRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements ClassPrepareRequest {
/*     */     ClassPrepareRequestImpl() {
/* 345 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 349 */       return 8;
/*     */     }
/*     */     
/*     */     public synchronized void addSourceNameFilter(String param1String) {
/* 353 */       if (isEnabled() || this.deleted) {
/* 354 */         throw invalidState();
/*     */       }
/* 356 */       if (!this.vm.canUseSourceNameFilters()) {
/* 357 */         throw new UnsupportedOperationException("target does not support source name filters");
/*     */       }
/*     */       
/* 360 */       if (param1String == null) {
/* 361 */         throw new NullPointerException();
/*     */       }
/*     */       
/* 364 */       this.filters.add(
/* 365 */           JDWP.EventRequest.Set.Modifier.SourceNameMatch.create(param1String));
/*     */     }
/*     */     
/*     */     public String toString() {
/* 369 */       return "class prepare request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class ClassUnloadRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements ClassUnloadRequest {
/*     */     ClassUnloadRequestImpl() {
/* 376 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 380 */       return 9;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 384 */       return "class unload request " + state();
/*     */     } }
/*     */   class ExceptionRequestImpl extends ClassVisibleEventRequestImpl implements ExceptionRequest { ReferenceType exception;
/*     */     
/*     */     ExceptionRequestImpl(ReferenceType param1ReferenceType, boolean param1Boolean1, boolean param1Boolean2) {
/*     */       ReferenceTypeImpl referenceTypeImpl;
/* 390 */       this.exception = null;
/* 391 */       this.caught = true;
/* 392 */       this.uncaught = true;
/*     */ 
/*     */ 
/*     */       
/* 396 */       this.exception = param1ReferenceType;
/* 397 */       this.caught = param1Boolean1;
/* 398 */       this.uncaught = param1Boolean2;
/*     */ 
/*     */       
/* 401 */       if (this.exception == null) {
/* 402 */         referenceTypeImpl = new ClassTypeImpl((VirtualMachine)this.vm, 0L);
/*     */       } else {
/* 404 */         referenceTypeImpl = (ReferenceTypeImpl)this.exception;
/*     */       } 
/* 406 */       this.filters.add(
/* 407 */           JDWP.EventRequest.Set.Modifier.ExceptionOnly.create(referenceTypeImpl, this.caught, this.uncaught));
/*     */       
/* 409 */       requestList().add(this);
/*     */     }
/*     */     boolean caught; boolean uncaught;
/*     */     public ReferenceType exception() {
/* 413 */       return this.exception;
/*     */     }
/*     */     
/*     */     public boolean notifyCaught() {
/* 417 */       return this.caught;
/*     */     }
/*     */     
/*     */     public boolean notifyUncaught() {
/* 421 */       return this.uncaught;
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 425 */       return 4;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 429 */       return "exception request " + exception() + state();
/*     */     } }
/*     */ 
/*     */   
/*     */   class MethodEntryRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements MethodEntryRequest {
/*     */     MethodEntryRequestImpl() {
/* 436 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 440 */       return 40;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 444 */       return "method entry request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class MethodExitRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements MethodExitRequest {
/*     */     MethodExitRequestImpl() {
/* 451 */       if (EventRequestManagerImpl.methodExitEventCmd == 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 462 */         if (this.vm.canGetMethodReturnValues()) {
/* 463 */           EventRequestManagerImpl.methodExitEventCmd = 42;
/*     */         } else {
/* 465 */           EventRequestManagerImpl.methodExitEventCmd = 41;
/*     */         } 
/*     */       }
/* 468 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 472 */       return EventRequestManagerImpl.methodExitEventCmd;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 476 */       return "method exit request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorContendedEnterRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements MonitorContendedEnterRequest {
/*     */     MonitorContendedEnterRequestImpl() {
/* 483 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 487 */       return 43;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 491 */       return "monitor contended enter request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorContendedEnteredRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements MonitorContendedEnteredRequest {
/*     */     MonitorContendedEnteredRequestImpl() {
/* 498 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 502 */       return 44;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 506 */       return "monitor contended entered request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorWaitRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements MonitorWaitRequest {
/*     */     MonitorWaitRequestImpl() {
/* 513 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 517 */       return 45;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 521 */       return "monitor wait request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorWaitedRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements MonitorWaitedRequest {
/*     */     MonitorWaitedRequestImpl() {
/* 528 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 532 */       return 46;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 536 */       return "monitor waited request " + state();
/*     */     } }
/*     */   
/*     */   class StepRequestImpl extends ClassVisibleEventRequestImpl implements StepRequest {
/*     */     ThreadReferenceImpl thread;
/*     */     int size;
/*     */     int depth;
/*     */     
/*     */     StepRequestImpl(ThreadReference param1ThreadReference, int param1Int1, int param1Int2) {
/*     */       boolean bool;
/*     */       byte b;
/* 547 */       this.thread = (ThreadReferenceImpl)param1ThreadReference;
/* 548 */       this.size = param1Int1;
/* 549 */       this.depth = param1Int2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 555 */       switch (param1Int1) {
/*     */         case -1:
/* 557 */           bool = false;
/*     */           break;
/*     */         case -2:
/* 560 */           bool = true;
/*     */           break;
/*     */         default:
/* 563 */           throw new IllegalArgumentException("Invalid step size");
/*     */       } 
/*     */ 
/*     */       
/* 567 */       switch (param1Int2) {
/*     */         case 1:
/* 569 */           b = 0;
/*     */           break;
/*     */         case 2:
/* 572 */           b = 1;
/*     */           break;
/*     */         case 3:
/* 575 */           b = 2;
/*     */           break;
/*     */         default:
/* 578 */           throw new IllegalArgumentException("Invalid step depth");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 584 */       List<StepRequest> list = EventRequestManagerImpl.this.stepRequests();
/* 585 */       Iterator<StepRequest> iterator = list.iterator();
/* 586 */       while (iterator.hasNext()) {
/* 587 */         StepRequest stepRequest = iterator.next();
/* 588 */         if (stepRequest != this && stepRequest
/* 589 */           .isEnabled() && stepRequest
/* 590 */           .thread().equals(param1ThreadReference)) {
/* 591 */           throw new DuplicateRequestException("Only one step request allowed per thread");
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 596 */       this.filters.add(
/* 597 */           JDWP.EventRequest.Set.Modifier.Step.create(this.thread, bool, b));
/* 598 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     public int depth() {
/* 602 */       return this.depth;
/*     */     }
/*     */     
/*     */     public int size() {
/* 606 */       return this.size;
/*     */     }
/*     */     
/*     */     public ThreadReference thread() {
/* 610 */       return this.thread;
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 614 */       return 1;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 618 */       return "step request " + thread() + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class ThreadDeathRequestImpl
/*     */     extends ThreadVisibleEventRequestImpl implements ThreadDeathRequest {
/*     */     ThreadDeathRequestImpl() {
/* 625 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 629 */       return 7;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 633 */       return "thread death request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class ThreadStartRequestImpl
/*     */     extends ThreadVisibleEventRequestImpl implements ThreadStartRequest {
/*     */     ThreadStartRequestImpl() {
/* 640 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 644 */       return 6;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 648 */       return "thread start request " + state();
/*     */     }
/*     */   }
/*     */   
/*     */   abstract class WatchpointRequestImpl
/*     */     extends ClassVisibleEventRequestImpl implements WatchpointRequest {
/*     */     final Field field;
/*     */     
/*     */     WatchpointRequestImpl(Field param1Field) {
/* 657 */       this.field = param1Field;
/* 658 */       this.filters.add(0, 
/* 659 */           JDWP.EventRequest.Set.Modifier.FieldOnly.create((ReferenceTypeImpl)param1Field
/* 660 */             .declaringType(), ((FieldImpl)param1Field)
/* 661 */             .ref()));
/*     */     }
/*     */     
/*     */     public Field field() {
/* 665 */       return this.field;
/*     */     }
/*     */   }
/*     */   
/*     */   class AccessWatchpointRequestImpl
/*     */     extends WatchpointRequestImpl implements AccessWatchpointRequest {
/*     */     AccessWatchpointRequestImpl(Field param1Field) {
/* 672 */       super(param1Field);
/* 673 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 677 */       return 20;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 681 */       return "access watchpoint request " + this.field + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class ModificationWatchpointRequestImpl
/*     */     extends WatchpointRequestImpl implements ModificationWatchpointRequest {
/*     */     ModificationWatchpointRequestImpl(Field param1Field) {
/* 688 */       super(param1Field);
/* 689 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 693 */       return 21;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 697 */       return "modification watchpoint request " + this.field + state();
/*     */     }
/*     */   }
/*     */   
/*     */   class VMDeathRequestImpl
/*     */     extends EventRequestImpl implements VMDeathRequest {
/*     */     VMDeathRequestImpl() {
/* 704 */       requestList().add(this);
/*     */     }
/*     */     
/*     */     int eventCmd() {
/* 708 */       return 99;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 712 */       return "VM death request " + state();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventRequestManagerImpl(VirtualMachine paramVirtualMachine) {
/* 720 */     super(paramVirtualMachine);
/*     */     
/* 722 */     Field[] arrayOfField = JDWP.EventKind.class.getDeclaredFields();
/* 723 */     int i = 0; byte b;
/* 724 */     for (b = 0; b < arrayOfField.length; b++) {
/*     */       int j;
/*     */       try {
/* 727 */         j = arrayOfField[b].getInt(null);
/* 728 */       } catch (IllegalAccessException illegalAccessException) {
/* 729 */         throw new RuntimeException("Got: " + illegalAccessException);
/*     */       } 
/* 731 */       if (j > i) {
/* 732 */         i = j;
/*     */       }
/*     */     } 
/* 735 */     this.requestLists = (List<? extends EventRequest>[])new List[i + 1];
/* 736 */     for (b = 0; b <= i; b++) {
/* 737 */       this.requestLists[b] = new ArrayList<>();
/*     */     }
/*     */   }
/*     */   
/*     */   public ClassPrepareRequest createClassPrepareRequest() {
/* 742 */     return new ClassPrepareRequestImpl();
/*     */   }
/*     */   
/*     */   public ClassUnloadRequest createClassUnloadRequest() {
/* 746 */     return new ClassUnloadRequestImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionRequest createExceptionRequest(ReferenceType paramReferenceType, boolean paramBoolean1, boolean paramBoolean2) {
/* 752 */     validateMirrorOrNull((Mirror)paramReferenceType);
/* 753 */     return new ExceptionRequestImpl(paramReferenceType, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   
/*     */   public StepRequest createStepRequest(ThreadReference paramThreadReference, int paramInt1, int paramInt2) {
/* 758 */     validateMirror((Mirror)paramThreadReference);
/* 759 */     return new StepRequestImpl(paramThreadReference, paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public ThreadDeathRequest createThreadDeathRequest() {
/* 763 */     return new ThreadDeathRequestImpl();
/*     */   }
/*     */   
/*     */   public ThreadStartRequest createThreadStartRequest() {
/* 767 */     return new ThreadStartRequestImpl();
/*     */   }
/*     */   
/*     */   public MethodEntryRequest createMethodEntryRequest() {
/* 771 */     return new MethodEntryRequestImpl();
/*     */   }
/*     */   
/*     */   public MethodExitRequest createMethodExitRequest() {
/* 775 */     return new MethodExitRequestImpl();
/*     */   }
/*     */   
/*     */   public MonitorContendedEnterRequest createMonitorContendedEnterRequest() {
/* 779 */     if (!this.vm.canRequestMonitorEvents()) {
/* 780 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */     
/* 783 */     return new MonitorContendedEnterRequestImpl();
/*     */   }
/*     */   
/*     */   public MonitorContendedEnteredRequest createMonitorContendedEnteredRequest() {
/* 787 */     if (!this.vm.canRequestMonitorEvents()) {
/* 788 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */     
/* 791 */     return new MonitorContendedEnteredRequestImpl();
/*     */   }
/*     */   
/*     */   public MonitorWaitRequest createMonitorWaitRequest() {
/* 795 */     if (!this.vm.canRequestMonitorEvents()) {
/* 796 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */     
/* 799 */     return new MonitorWaitRequestImpl();
/*     */   }
/*     */   
/*     */   public MonitorWaitedRequest createMonitorWaitedRequest() {
/* 803 */     if (!this.vm.canRequestMonitorEvents()) {
/* 804 */       throw new UnsupportedOperationException("target VM does not support requesting Monitor events");
/*     */     }
/*     */     
/* 807 */     return new MonitorWaitedRequestImpl();
/*     */   }
/*     */   
/*     */   public BreakpointRequest createBreakpointRequest(Location paramLocation) {
/* 811 */     validateMirror((Mirror)paramLocation);
/* 812 */     if (paramLocation.codeIndex() == -1L) {
/* 813 */       throw new NativeMethodException("Cannot set breakpoints on native methods");
/*     */     }
/* 815 */     return new BreakpointRequestImpl(paramLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public AccessWatchpointRequest createAccessWatchpointRequest(Field paramField) {
/* 820 */     validateMirror((Mirror)paramField);
/* 821 */     if (!this.vm.canWatchFieldAccess()) {
/* 822 */       throw new UnsupportedOperationException("target VM does not support access watchpoints");
/*     */     }
/*     */     
/* 825 */     return new AccessWatchpointRequestImpl(paramField);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModificationWatchpointRequest createModificationWatchpointRequest(Field paramField) {
/* 830 */     validateMirror((Mirror)paramField);
/* 831 */     if (!this.vm.canWatchFieldModification()) {
/* 832 */       throw new UnsupportedOperationException("target VM does not support modification watchpoints");
/*     */     }
/*     */     
/* 835 */     return new ModificationWatchpointRequestImpl(paramField);
/*     */   }
/*     */   
/*     */   public VMDeathRequest createVMDeathRequest() {
/* 839 */     if (!this.vm.canRequestVMDeathEvent()) {
/* 840 */       throw new UnsupportedOperationException("target VM does not support requesting VM death events");
/*     */     }
/*     */     
/* 843 */     return new VMDeathRequestImpl();
/*     */   }
/*     */   
/*     */   public void deleteEventRequest(EventRequest paramEventRequest) {
/* 847 */     validateMirror((Mirror)paramEventRequest);
/* 848 */     ((EventRequestImpl)paramEventRequest).delete();
/*     */   }
/*     */   
/*     */   public void deleteEventRequests(List<? extends EventRequest> paramList) {
/* 852 */     validateMirrors((Collection)paramList);
/*     */     
/* 854 */     Iterator<?> iterator = (new ArrayList(paramList)).iterator();
/* 855 */     while (iterator.hasNext()) {
/* 856 */       ((EventRequestImpl)iterator.next()).delete();
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteAllBreakpoints() {
/* 861 */     requestList(2).clear();
/*     */     
/*     */     try {
/* 864 */       JDWP.EventRequest.ClearAllBreakpoints.process(this.vm);
/* 865 */     } catch (JDWPException jDWPException) {
/* 866 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<StepRequest> stepRequests() {
/* 871 */     return (List)unmodifiableRequestList(1);
/*     */   }
/*     */   
/*     */   public List<ClassPrepareRequest> classPrepareRequests() {
/* 875 */     return (List)unmodifiableRequestList(8);
/*     */   }
/*     */   
/*     */   public List<ClassUnloadRequest> classUnloadRequests() {
/* 879 */     return (List)unmodifiableRequestList(9);
/*     */   }
/*     */   
/*     */   public List<ThreadStartRequest> threadStartRequests() {
/* 883 */     return (List)unmodifiableRequestList(6);
/*     */   }
/*     */   
/*     */   public List<ThreadDeathRequest> threadDeathRequests() {
/* 887 */     return (List)unmodifiableRequestList(7);
/*     */   }
/*     */   
/*     */   public List<ExceptionRequest> exceptionRequests() {
/* 891 */     return (List)unmodifiableRequestList(4);
/*     */   }
/*     */   
/*     */   public List<BreakpointRequest> breakpointRequests() {
/* 895 */     return (List)unmodifiableRequestList(2);
/*     */   }
/*     */   
/*     */   public List<AccessWatchpointRequest> accessWatchpointRequests() {
/* 899 */     return (List)unmodifiableRequestList(20);
/*     */   }
/*     */   
/*     */   public List<ModificationWatchpointRequest> modificationWatchpointRequests() {
/* 903 */     return (List)unmodifiableRequestList(21);
/*     */   }
/*     */   
/*     */   public List<MethodEntryRequest> methodEntryRequests() {
/* 907 */     return (List)unmodifiableRequestList(40);
/*     */   }
/*     */   
/*     */   public List<MethodExitRequest> methodExitRequests() {
/* 911 */     return (List)unmodifiableRequestList(methodExitEventCmd);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MonitorContendedEnterRequest> monitorContendedEnterRequests() {
/* 916 */     return (List)unmodifiableRequestList(43);
/*     */   }
/*     */   
/*     */   public List<MonitorContendedEnteredRequest> monitorContendedEnteredRequests() {
/* 920 */     return (List)unmodifiableRequestList(44);
/*     */   }
/*     */   
/*     */   public List<MonitorWaitRequest> monitorWaitRequests() {
/* 924 */     return (List)unmodifiableRequestList(45);
/*     */   }
/*     */   
/*     */   public List<MonitorWaitedRequest> monitorWaitedRequests() {
/* 928 */     return (List)unmodifiableRequestList(46);
/*     */   }
/*     */   
/*     */   public List<VMDeathRequest> vmDeathRequests() {
/* 932 */     return (List)unmodifiableRequestList(99);
/*     */   }
/*     */   
/*     */   List<? extends EventRequest> unmodifiableRequestList(int paramInt) {
/* 936 */     return Collections.unmodifiableList(requestList(paramInt));
/*     */   }
/*     */   
/*     */   EventRequest request(int paramInt1, int paramInt2) {
/* 940 */     List<? extends EventRequest> list = requestList(paramInt1);
/* 941 */     for (int i = list.size() - 1; i >= 0; i--) {
/* 942 */       EventRequestImpl eventRequestImpl = (EventRequestImpl)list.get(i);
/* 943 */       if (eventRequestImpl.id == paramInt2) {
/* 944 */         return eventRequestImpl;
/*     */       }
/*     */     } 
/* 947 */     return null;
/*     */   }
/*     */   
/*     */   List<? extends EventRequest> requestList(int paramInt) {
/* 951 */     return this.requestLists[paramInt];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\EventRequestManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */