/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.Locatable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VMDisconnectedException;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.event.AccessWatchpointEvent;
/*     */ import com.sun.jdi.event.BreakpointEvent;
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.event.ClassUnloadEvent;
/*     */ import com.sun.jdi.event.Event;
/*     */ import com.sun.jdi.event.EventIterator;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import com.sun.jdi.event.ExceptionEvent;
/*     */ import com.sun.jdi.event.MethodEntryEvent;
/*     */ import com.sun.jdi.event.MethodExitEvent;
/*     */ import com.sun.jdi.event.ModificationWatchpointEvent;
/*     */ import com.sun.jdi.event.MonitorContendedEnterEvent;
/*     */ import com.sun.jdi.event.MonitorContendedEnteredEvent;
/*     */ import com.sun.jdi.event.MonitorWaitEvent;
/*     */ import com.sun.jdi.event.MonitorWaitedEvent;
/*     */ import com.sun.jdi.event.StepEvent;
/*     */ import com.sun.jdi.event.ThreadDeathEvent;
/*     */ import com.sun.jdi.event.ThreadStartEvent;
/*     */ import com.sun.jdi.event.VMDeathEvent;
/*     */ import com.sun.jdi.event.VMDisconnectEvent;
/*     */ import com.sun.jdi.event.VMStartEvent;
/*     */ import com.sun.jdi.event.WatchpointEvent;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventSetImpl
/*     */   extends ArrayList<Event>
/*     */   implements EventSet
/*     */ {
/*     */   private static final long serialVersionUID = -4857338819787924570L;
/*     */   private VirtualMachineImpl vm;
/*     */   private Packet pkt;
/*     */   private byte suspendPolicy;
/*     */   private EventSetImpl internalEventSet;
/*     */   
/*     */   public String toString() {
/*  58 */     String str = "event set, policy:" + this.suspendPolicy + ", count:" + size() + " = {";
/*  59 */     boolean bool = true;
/*  60 */     for (Event event : this) {
/*  61 */       if (!bool) {
/*  62 */         str = str + ", ";
/*     */       }
/*  64 */       str = str + event.toString();
/*  65 */       bool = false;
/*     */     } 
/*  67 */     str = str + "}";
/*  68 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract class EventImpl
/*     */     extends MirrorImpl
/*     */     implements Event
/*     */   {
/*     */     private final byte eventCmd;
/*     */     
/*     */     private final int requestID;
/*     */     
/*     */     private final EventRequest request;
/*     */     
/*     */     protected EventImpl(JDWP.Event.Composite.Events.EventsCommon param1EventsCommon, int param1Int) {
/*  83 */       super((VirtualMachine)EventSetImpl.this.vm);
/*  84 */       this.eventCmd = param1EventsCommon.eventKind();
/*  85 */       this.requestID = param1Int;
/*     */       
/*  87 */       EventRequestManagerImpl eventRequestManagerImpl = EventSetImpl.this.vm.eventRequestManagerImpl();
/*  88 */       this.request = eventRequestManagerImpl.request(this.eventCmd, param1Int);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/*  95 */       return (this == param1Object);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/*  99 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected EventImpl(byte param1Byte) {
/* 106 */       super((VirtualMachine)EventSetImpl.this.vm);
/* 107 */       this.eventCmd = param1Byte;
/* 108 */       this.requestID = 0;
/* 109 */       this.request = null;
/*     */     }
/*     */     
/*     */     public EventRequest request() {
/* 113 */       return this.request;
/*     */     }
/*     */     
/*     */     int requestID() {
/* 117 */       return this.requestID;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     EventDestination destination() {
/* 137 */       if (this.requestID == 0)
/*     */       {
/*     */ 
/*     */         
/* 141 */         return EventDestination.CLIENT_EVENT;
/*     */       }
/*     */ 
/*     */       
/* 145 */       if (this.request == null) {
/*     */         
/* 147 */         EventRequestManagerImpl eventRequestManagerImpl = this.vm.getInternalEventRequestManager();
/* 148 */         if (eventRequestManagerImpl.request(this.eventCmd, this.requestID) != null)
/*     */         {
/* 150 */           return EventDestination.INTERNAL_EVENT;
/*     */         }
/* 152 */         return EventDestination.UNKNOWN_EVENT;
/*     */       } 
/*     */ 
/*     */       
/* 156 */       if (this.request.isEnabled()) {
/* 157 */         return EventDestination.CLIENT_EVENT;
/*     */       }
/* 159 */       return EventDestination.UNKNOWN_EVENT;
/*     */     }
/*     */     
/*     */     abstract String eventName();
/*     */     
/*     */     public String toString() {
/* 165 */       return eventName();
/*     */     }
/*     */   }
/*     */   
/*     */   abstract class ThreadedEventImpl
/*     */     extends EventImpl
/*     */   {
/*     */     private ThreadReference thread;
/*     */     
/*     */     ThreadedEventImpl(JDWP.Event.Composite.Events.EventsCommon param1EventsCommon, int param1Int, ThreadReference param1ThreadReference) {
/* 175 */       super(param1EventsCommon, param1Int);
/* 176 */       this.thread = param1ThreadReference;
/*     */     }
/*     */     
/*     */     public ThreadReference thread() {
/* 180 */       return this.thread;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 184 */       return eventName() + " in thread " + this.thread.name();
/*     */     }
/*     */   }
/*     */   
/*     */   abstract class LocatableEventImpl
/*     */     extends ThreadedEventImpl
/*     */     implements Locatable
/*     */   {
/*     */     private Location location;
/*     */     
/*     */     LocatableEventImpl(JDWP.Event.Composite.Events.EventsCommon param1EventsCommon, int param1Int, ThreadReference param1ThreadReference, Location param1Location) {
/* 195 */       super(param1EventsCommon, param1Int, param1ThreadReference);
/* 196 */       this.location = param1Location;
/*     */     }
/*     */     
/*     */     public Location location() {
/* 200 */       return this.location;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Method method() {
/* 207 */       return this.location.method();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 211 */       return eventName() + "@" + (
/* 212 */         (location() == null) ? " null" : location().toString()) + " in thread " + 
/* 213 */         thread().name();
/*     */     }
/*     */   }
/*     */   
/*     */   class BreakpointEventImpl
/*     */     extends LocatableEventImpl implements BreakpointEvent {
/*     */     BreakpointEventImpl(JDWP.Event.Composite.Events.Breakpoint param1Breakpoint) {
/* 220 */       super(param1Breakpoint, param1Breakpoint.requestID, param1Breakpoint.thread, param1Breakpoint.location);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 224 */       return "BreakpointEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class StepEventImpl extends LocatableEventImpl implements StepEvent {
/*     */     StepEventImpl(JDWP.Event.Composite.Events.SingleStep param1SingleStep) {
/* 230 */       super(param1SingleStep, param1SingleStep.requestID, param1SingleStep.thread, param1SingleStep.location);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 234 */       return "StepEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class MethodEntryEventImpl
/*     */     extends LocatableEventImpl implements MethodEntryEvent {
/*     */     MethodEntryEventImpl(JDWP.Event.Composite.Events.MethodEntry param1MethodEntry) {
/* 241 */       super(param1MethodEntry, param1MethodEntry.requestID, param1MethodEntry.thread, param1MethodEntry.location);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 245 */       return "MethodEntryEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class MethodExitEventImpl
/*     */     extends LocatableEventImpl implements MethodExitEvent {
/* 251 */     private Value returnVal = null;
/*     */     
/*     */     MethodExitEventImpl(JDWP.Event.Composite.Events.MethodExit param1MethodExit) {
/* 254 */       super(param1MethodExit, param1MethodExit.requestID, param1MethodExit.thread, param1MethodExit.location);
/*     */     }
/*     */     
/*     */     MethodExitEventImpl(JDWP.Event.Composite.Events.MethodExitWithReturnValue param1MethodExitWithReturnValue) {
/* 258 */       super(param1MethodExitWithReturnValue, param1MethodExitWithReturnValue.requestID, param1MethodExitWithReturnValue.thread, param1MethodExitWithReturnValue.location);
/* 259 */       this.returnVal = param1MethodExitWithReturnValue.value;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 263 */       return "MethodExitEvent";
/*     */     }
/*     */     
/*     */     public Value returnValue() {
/* 267 */       if (!this.vm.canGetMethodReturnValues()) {
/* 268 */         throw new UnsupportedOperationException("target does not support return values in MethodExit events");
/*     */       }
/*     */       
/* 271 */       return this.returnVal;
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorContendedEnterEventImpl
/*     */     extends LocatableEventImpl
/*     */     implements MonitorContendedEnterEvent {
/* 278 */     private ObjectReference monitor = null;
/*     */     
/*     */     MonitorContendedEnterEventImpl(JDWP.Event.Composite.Events.MonitorContendedEnter param1MonitorContendedEnter) {
/* 281 */       super(param1MonitorContendedEnter, param1MonitorContendedEnter.requestID, param1MonitorContendedEnter.thread, param1MonitorContendedEnter.location);
/* 282 */       this.monitor = param1MonitorContendedEnter.object;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 286 */       return "MonitorContendedEnter";
/*     */     }
/*     */     
/*     */     public ObjectReference monitor() {
/* 290 */       return this.monitor;
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorContendedEnteredEventImpl
/*     */     extends LocatableEventImpl
/*     */     implements MonitorContendedEnteredEvent {
/* 297 */     private ObjectReference monitor = null;
/*     */     
/*     */     MonitorContendedEnteredEventImpl(JDWP.Event.Composite.Events.MonitorContendedEntered param1MonitorContendedEntered) {
/* 300 */       super(param1MonitorContendedEntered, param1MonitorContendedEntered.requestID, param1MonitorContendedEntered.thread, param1MonitorContendedEntered.location);
/* 301 */       this.monitor = param1MonitorContendedEntered.object;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 305 */       return "MonitorContendedEntered";
/*     */     }
/*     */     
/*     */     public ObjectReference monitor() {
/* 309 */       return this.monitor;
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorWaitEventImpl
/*     */     extends LocatableEventImpl
/*     */     implements MonitorWaitEvent {
/* 316 */     private ObjectReference monitor = null;
/*     */     private long timeout;
/*     */     
/*     */     MonitorWaitEventImpl(JDWP.Event.Composite.Events.MonitorWait param1MonitorWait) {
/* 320 */       super(param1MonitorWait, param1MonitorWait.requestID, param1MonitorWait.thread, param1MonitorWait.location);
/* 321 */       this.monitor = param1MonitorWait.object;
/* 322 */       this.timeout = param1MonitorWait.timeout;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 326 */       return "MonitorWait";
/*     */     }
/*     */     
/*     */     public ObjectReference monitor() {
/* 330 */       return this.monitor;
/*     */     }
/*     */     
/*     */     public long timeout() {
/* 334 */       return this.timeout;
/*     */     }
/*     */   }
/*     */   
/*     */   class MonitorWaitedEventImpl
/*     */     extends LocatableEventImpl implements MonitorWaitedEvent {
/* 340 */     private ObjectReference monitor = null;
/*     */     private boolean timed_out;
/*     */     
/*     */     MonitorWaitedEventImpl(JDWP.Event.Composite.Events.MonitorWaited param1MonitorWaited) {
/* 344 */       super(param1MonitorWaited, param1MonitorWaited.requestID, param1MonitorWaited.thread, param1MonitorWaited.location);
/* 345 */       this.monitor = param1MonitorWaited.object;
/* 346 */       this.timed_out = param1MonitorWaited.timed_out;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 350 */       return "MonitorWaited";
/*     */     }
/*     */     
/*     */     public ObjectReference monitor() {
/* 354 */       return this.monitor;
/*     */     }
/*     */     
/*     */     public boolean timedout() {
/* 358 */       return this.timed_out;
/*     */     }
/*     */   }
/*     */   
/*     */   class ClassPrepareEventImpl
/*     */     extends ThreadedEventImpl implements ClassPrepareEvent {
/*     */     private ReferenceType referenceType;
/*     */     
/*     */     ClassPrepareEventImpl(JDWP.Event.Composite.Events.ClassPrepare param1ClassPrepare) {
/* 367 */       super(param1ClassPrepare, param1ClassPrepare.requestID, param1ClassPrepare.thread);
/* 368 */       this.referenceType = this.vm.referenceType(param1ClassPrepare.typeID, param1ClassPrepare.refTypeTag, param1ClassPrepare.signature);
/*     */       
/* 370 */       ((ReferenceTypeImpl)this.referenceType).setStatus(param1ClassPrepare.status);
/*     */     }
/*     */     
/*     */     public ReferenceType referenceType() {
/* 374 */       return this.referenceType;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 378 */       return "ClassPrepareEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class ClassUnloadEventImpl extends EventImpl implements ClassUnloadEvent {
/*     */     private String classSignature;
/*     */     
/*     */     ClassUnloadEventImpl(JDWP.Event.Composite.Events.ClassUnload param1ClassUnload) {
/* 386 */       super(param1ClassUnload, param1ClassUnload.requestID);
/* 387 */       this.classSignature = param1ClassUnload.signature;
/*     */     }
/*     */     
/*     */     public String className() {
/* 391 */       return this.classSignature.substring(1, this.classSignature.length() - 1)
/* 392 */         .replace('/', '.');
/*     */     }
/*     */     
/*     */     public String classSignature() {
/* 396 */       return this.classSignature;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 400 */       return "ClassUnloadEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class ExceptionEventImpl
/*     */     extends LocatableEventImpl implements ExceptionEvent {
/*     */     private ObjectReference exception;
/*     */     private Location catchLocation;
/*     */     
/*     */     ExceptionEventImpl(JDWP.Event.Composite.Events.Exception param1Exception) {
/* 410 */       super(param1Exception, param1Exception.requestID, param1Exception.thread, param1Exception.location);
/* 411 */       this.exception = param1Exception.exception;
/* 412 */       this.catchLocation = param1Exception.catchLocation;
/*     */     }
/*     */     
/*     */     public ObjectReference exception() {
/* 416 */       return this.exception;
/*     */     }
/*     */     
/*     */     public Location catchLocation() {
/* 420 */       return this.catchLocation;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 424 */       return "ExceptionEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class ThreadDeathEventImpl
/*     */     extends ThreadedEventImpl implements ThreadDeathEvent {
/*     */     ThreadDeathEventImpl(JDWP.Event.Composite.Events.ThreadDeath param1ThreadDeath) {
/* 431 */       super(param1ThreadDeath, param1ThreadDeath.requestID, param1ThreadDeath.thread);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 435 */       return "ThreadDeathEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class ThreadStartEventImpl
/*     */     extends ThreadedEventImpl implements ThreadStartEvent {
/*     */     ThreadStartEventImpl(JDWP.Event.Composite.Events.ThreadStart param1ThreadStart) {
/* 442 */       super(param1ThreadStart, param1ThreadStart.requestID, param1ThreadStart.thread);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 446 */       return "ThreadStartEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class VMStartEventImpl
/*     */     extends ThreadedEventImpl implements VMStartEvent {
/*     */     VMStartEventImpl(JDWP.Event.Composite.Events.VMStart param1VMStart) {
/* 453 */       super(param1VMStart, param1VMStart.requestID, param1VMStart.thread);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 457 */       return "VMStartEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class VMDeathEventImpl
/*     */     extends EventImpl implements VMDeathEvent {
/*     */     VMDeathEventImpl(JDWP.Event.Composite.Events.VMDeath param1VMDeath) {
/* 464 */       super(param1VMDeath, param1VMDeath.requestID);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 468 */       return "VMDeathEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   class VMDisconnectEventImpl
/*     */     extends EventImpl
/*     */     implements VMDisconnectEvent {
/*     */     VMDisconnectEventImpl() {
/* 476 */       super((byte)100);
/*     */     }
/*     */     
/*     */     String eventName() {
/* 480 */       return "VMDisconnectEvent";
/*     */     }
/*     */   }
/*     */   
/*     */   abstract class WatchpointEventImpl
/*     */     extends LocatableEventImpl implements WatchpointEvent {
/*     */     private final ReferenceTypeImpl refType;
/*     */     private final long fieldID;
/*     */     private final ObjectReference object;
/* 489 */     private Field field = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     WatchpointEventImpl(JDWP.Event.Composite.Events.EventsCommon param1EventsCommon, int param1Int, ThreadReference param1ThreadReference, Location param1Location, byte param1Byte, long param1Long1, long param1Long2, ObjectReference param1ObjectReference) {
/* 496 */       super(param1EventsCommon, param1Int, param1ThreadReference, param1Location);
/* 497 */       this.refType = this.vm.referenceType(param1Long1, param1Byte);
/* 498 */       this.fieldID = param1Long2;
/* 499 */       this.object = param1ObjectReference;
/*     */     }
/*     */     
/*     */     public Field field() {
/* 503 */       if (this.field == null) {
/* 504 */         this.field = this.refType.getFieldMirror(this.fieldID);
/*     */       }
/* 506 */       return this.field;
/*     */     }
/*     */     
/*     */     public ObjectReference object() {
/* 510 */       return this.object;
/*     */     }
/*     */     
/*     */     public Value valueCurrent() {
/* 514 */       if (this.object == null) {
/* 515 */         return this.refType.getValue(field());
/*     */       }
/* 517 */       return this.object.getValue(field());
/*     */     }
/*     */   }
/*     */   
/*     */   class AccessWatchpointEventImpl
/*     */     extends WatchpointEventImpl
/*     */     implements AccessWatchpointEvent
/*     */   {
/*     */     AccessWatchpointEventImpl(JDWP.Event.Composite.Events.FieldAccess param1FieldAccess) {
/* 526 */       super(param1FieldAccess, param1FieldAccess.requestID, param1FieldAccess.thread, param1FieldAccess.location, param1FieldAccess.refTypeTag, param1FieldAccess.typeID, param1FieldAccess.fieldID, param1FieldAccess.object);
/*     */     }
/*     */ 
/*     */     
/*     */     String eventName() {
/* 531 */       return "AccessWatchpoint";
/*     */     }
/*     */   }
/*     */   
/*     */   class ModificationWatchpointEventImpl
/*     */     extends WatchpointEventImpl
/*     */     implements ModificationWatchpointEvent {
/*     */     Value newValue;
/*     */     
/*     */     ModificationWatchpointEventImpl(JDWP.Event.Composite.Events.FieldModification param1FieldModification) {
/* 541 */       super(param1FieldModification, param1FieldModification.requestID, param1FieldModification.thread, param1FieldModification.location, param1FieldModification.refTypeTag, param1FieldModification.typeID, param1FieldModification.fieldID, param1FieldModification.object);
/*     */       
/* 543 */       this.newValue = param1FieldModification.valueToBe;
/*     */     }
/*     */     
/*     */     public Value valueToBe() {
/* 547 */       return this.newValue;
/*     */     }
/*     */     
/*     */     String eventName() {
/* 551 */       return "ModificationWatchpoint";
/*     */     }
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
/*     */ 
/*     */   
/*     */   EventSetImpl(VirtualMachine paramVirtualMachine, Packet paramPacket) {
/* 570 */     this.vm = (VirtualMachineImpl)paramVirtualMachine;
/*     */     
/* 572 */     this.pkt = paramPacket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventSetImpl(VirtualMachine paramVirtualMachine, byte paramByte) {
/* 579 */     this(paramVirtualMachine, (Packet)null);
/* 580 */     this.suspendPolicy = 0;
/* 581 */     switch (paramByte) {
/*     */       case 100:
/* 583 */         addEvent(new VMDisconnectEventImpl());
/*     */         return;
/*     */     } 
/*     */     
/* 587 */     throw new InternalException("Bad singleton event code");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEvent(EventImpl paramEventImpl) {
/* 594 */     super.add(paramEventImpl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void build() {
/* 604 */     if (this.pkt == null) {
/*     */       return;
/*     */     }
/* 607 */     PacketStream packetStream = new PacketStream(this.vm, this.pkt);
/* 608 */     JDWP.Event.Composite composite = new JDWP.Event.Composite(this.vm, packetStream);
/* 609 */     this.suspendPolicy = composite.suspendPolicy;
/* 610 */     if ((this.vm.traceFlags & 0x4) != 0) {
/* 611 */       switch (this.suspendPolicy) {
/*     */         case 2:
/* 613 */           this.vm.printTrace("EventSet: SUSPEND_ALL");
/*     */           break;
/*     */         
/*     */         case 1:
/* 617 */           this.vm.printTrace("EventSet: SUSPEND_EVENT_THREAD");
/*     */           break;
/*     */         
/*     */         case 0:
/* 621 */           this.vm.printTrace("EventSet: SUSPEND_NONE");
/*     */           break;
/*     */       } 
/*     */     
/*     */     }
/* 626 */     ThreadReference threadReference = null;
/* 627 */     for (byte b = 0; b < composite.events.length; b++) {
/* 628 */       EventImpl eventImpl = createEvent(composite.events[b]);
/* 629 */       if ((this.vm.traceFlags & 0x4) != 0) {
/*     */         try {
/* 631 */           this.vm.printTrace("Event: " + eventImpl);
/* 632 */         } catch (VMDisconnectedException vMDisconnectedException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 637 */       switch (eventImpl.destination()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case UNKNOWN_EVENT:
/* 643 */           if (eventImpl instanceof ThreadedEventImpl && this.suspendPolicy == 1)
/*     */           {
/* 645 */             threadReference = ((ThreadedEventImpl)eventImpl).thread();
/*     */           }
/*     */           break;
/*     */         case CLIENT_EVENT:
/* 649 */           addEvent(eventImpl);
/*     */           break;
/*     */         case INTERNAL_EVENT:
/* 652 */           if (this.internalEventSet == null) {
/* 653 */             this.internalEventSet = new EventSetImpl((VirtualMachine)this.vm, null);
/*     */           }
/* 655 */           this.internalEventSet.addEvent(eventImpl);
/*     */           break;
/*     */         default:
/* 658 */           throw new InternalException("Invalid event destination");
/*     */       } 
/*     */     } 
/* 661 */     this.pkt = null;
/*     */ 
/*     */     
/* 664 */     if (size() == 0) {
/*     */ 
/*     */       
/* 667 */       if (this.suspendPolicy == 2) {
/* 668 */         this.vm.resume();
/* 669 */       } else if (this.suspendPolicy == 1) {
/*     */         
/* 671 */         if (threadReference != null) {
/* 672 */           threadReference.resume();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 677 */       this.suspendPolicy = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventSet userFilter() {
/* 687 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventSet internalFilter() {
/* 694 */     return this.internalEventSet;
/*     */   }
/*     */   
/*     */   EventImpl createEvent(JDWP.Event.Composite.Events paramEvents) {
/* 698 */     JDWP.Event.Composite.Events.EventsCommon eventsCommon = paramEvents.aEventsCommon;
/* 699 */     switch (paramEvents.eventKind) {
/*     */       case 6:
/* 701 */         return new ThreadStartEventImpl((JDWP.Event.Composite.Events.ThreadStart)eventsCommon);
/*     */ 
/*     */       
/*     */       case 7:
/* 705 */         return new ThreadDeathEventImpl((JDWP.Event.Composite.Events.ThreadDeath)eventsCommon);
/*     */ 
/*     */       
/*     */       case 4:
/* 709 */         return new ExceptionEventImpl((JDWP.Event.Composite.Events.Exception)eventsCommon);
/*     */ 
/*     */       
/*     */       case 2:
/* 713 */         return new BreakpointEventImpl((JDWP.Event.Composite.Events.Breakpoint)eventsCommon);
/*     */ 
/*     */       
/*     */       case 40:
/* 717 */         return new MethodEntryEventImpl((JDWP.Event.Composite.Events.MethodEntry)eventsCommon);
/*     */ 
/*     */       
/*     */       case 41:
/* 721 */         return new MethodExitEventImpl((JDWP.Event.Composite.Events.MethodExit)eventsCommon);
/*     */ 
/*     */       
/*     */       case 42:
/* 725 */         return new MethodExitEventImpl((JDWP.Event.Composite.Events.MethodExitWithReturnValue)eventsCommon);
/*     */ 
/*     */       
/*     */       case 20:
/* 729 */         return new AccessWatchpointEventImpl((JDWP.Event.Composite.Events.FieldAccess)eventsCommon);
/*     */ 
/*     */       
/*     */       case 21:
/* 733 */         return new ModificationWatchpointEventImpl((JDWP.Event.Composite.Events.FieldModification)eventsCommon);
/*     */ 
/*     */       
/*     */       case 1:
/* 737 */         return new StepEventImpl((JDWP.Event.Composite.Events.SingleStep)eventsCommon);
/*     */ 
/*     */       
/*     */       case 8:
/* 741 */         return new ClassPrepareEventImpl((JDWP.Event.Composite.Events.ClassPrepare)eventsCommon);
/*     */ 
/*     */       
/*     */       case 9:
/* 745 */         return new ClassUnloadEventImpl((JDWP.Event.Composite.Events.ClassUnload)eventsCommon);
/*     */ 
/*     */       
/*     */       case 43:
/* 749 */         return new MonitorContendedEnterEventImpl((JDWP.Event.Composite.Events.MonitorContendedEnter)eventsCommon);
/*     */ 
/*     */       
/*     */       case 44:
/* 753 */         return new MonitorContendedEnteredEventImpl((JDWP.Event.Composite.Events.MonitorContendedEntered)eventsCommon);
/*     */ 
/*     */       
/*     */       case 45:
/* 757 */         return new MonitorWaitEventImpl((JDWP.Event.Composite.Events.MonitorWait)eventsCommon);
/*     */ 
/*     */       
/*     */       case 46:
/* 761 */         return new MonitorWaitedEventImpl((JDWP.Event.Composite.Events.MonitorWaited)eventsCommon);
/*     */ 
/*     */       
/*     */       case 90:
/* 765 */         return new VMStartEventImpl((JDWP.Event.Composite.Events.VMStart)eventsCommon);
/*     */ 
/*     */       
/*     */       case 99:
/* 769 */         return new VMDeathEventImpl((JDWP.Event.Composite.Events.VMDeath)eventsCommon);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 774 */     System.err.println("Ignoring event cmd " + paramEvents.eventKind + " from the VM");
/*     */     
/* 776 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public VirtualMachine virtualMachine() {
/* 781 */     return (VirtualMachine)this.vm;
/*     */   }
/*     */   
/*     */   public int suspendPolicy() {
/* 785 */     return EventRequestManagerImpl.JDWPtoJDISuspendPolicy(this.suspendPolicy);
/*     */   }
/*     */   
/*     */   private ThreadReference eventThread() {
/* 789 */     for (Event event : this) {
/* 790 */       if (event instanceof ThreadedEventImpl) {
/* 791 */         return ((ThreadedEventImpl)event).thread();
/*     */       }
/*     */     } 
/* 794 */     return null;
/*     */   }
/*     */   public void resume() {
/*     */     ThreadReference threadReference;
/* 798 */     switch (suspendPolicy()) {
/*     */       case 2:
/* 800 */         this.vm.resume();
/*     */       
/*     */       case 1:
/* 803 */         threadReference = eventThread();
/* 804 */         if (threadReference == null) {
/* 805 */           throw new InternalException("Inconsistent suspend policy");
/*     */         }
/* 807 */         threadReference.resume();
/*     */       
/*     */       case 0:
/*     */         return;
/*     */     } 
/*     */     
/* 813 */     throw new InternalException("Invalid suspend policy");
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Event> iterator() {
/* 818 */     return (Iterator<Event>)new Itr();
/*     */   }
/*     */   
/*     */   public EventIterator eventIterator() {
/* 822 */     return new Itr();
/*     */   }
/*     */   
/*     */   public class Itr implements EventIterator {
/*     */     int cursor;
/*     */     
/*     */     public Itr() {
/* 829 */       this.cursor = 0;
/*     */     }
/*     */     public boolean hasNext() {
/* 832 */       return (this.cursor != EventSetImpl.this.size());
/*     */     }
/*     */     
/*     */     public Event next() {
/*     */       try {
/* 837 */         Event event = EventSetImpl.this.get(this.cursor);
/* 838 */         this.cursor++;
/* 839 */         return event;
/* 840 */       } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
/* 841 */         throw new NoSuchElementException();
/*     */       } 
/*     */     }
/*     */     
/*     */     public Event nextEvent() {
/* 846 */       return next();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 850 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<Event> spliterator() {
/* 856 */     return Spliterators.spliterator(this, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Event paramEvent) {
/* 862 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean remove(Object paramObject) {
/* 865 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean addAll(Collection<? extends Event> paramCollection) {
/* 868 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean removeAll(Collection<?> paramCollection) {
/* 871 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public boolean retainAll(Collection<?> paramCollection) {
/* 874 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   public void clear() {
/* 877 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\EventSetImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */