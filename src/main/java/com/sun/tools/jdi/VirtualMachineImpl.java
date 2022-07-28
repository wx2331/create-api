/*      */ package com.sun.tools.jdi;
/*      */ import com.sun.jdi.BooleanType;
/*      */ import com.sun.jdi.BooleanValue;
/*      */ import com.sun.jdi.ByteType;
/*      */ import com.sun.jdi.ByteValue;
/*      */ import com.sun.jdi.CharType;
/*      */ import com.sun.jdi.CharValue;
/*      */ import com.sun.jdi.ClassNotLoadedException;
/*      */ import com.sun.jdi.DoubleType;
/*      */ import com.sun.jdi.DoubleValue;
/*      */ import com.sun.jdi.FloatType;
/*      */ import com.sun.jdi.FloatValue;
/*      */ import com.sun.jdi.IntegerType;
/*      */ import com.sun.jdi.IntegerValue;
/*      */ import com.sun.jdi.InternalException;
/*      */ import com.sun.jdi.LongType;
/*      */ import com.sun.jdi.LongValue;
/*      */ import com.sun.jdi.Mirror;
/*      */ import com.sun.jdi.PathSearchingVirtualMachine;
/*      */ import com.sun.jdi.PrimitiveType;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.ShortType;
/*      */ import com.sun.jdi.ShortValue;
/*      */ import com.sun.jdi.StringReference;
/*      */ import com.sun.jdi.ThreadGroupReference;
/*      */ import com.sun.jdi.ThreadReference;
/*      */ import com.sun.jdi.Type;
/*      */ import com.sun.jdi.VMDisconnectedException;
/*      */ import com.sun.jdi.VirtualMachine;
/*      */ import com.sun.jdi.VirtualMachineManager;
/*      */ import com.sun.jdi.VoidType;
/*      */ import com.sun.jdi.VoidValue;
/*      */ import com.sun.jdi.connect.spi.Connection;
/*      */ import com.sun.jdi.event.EventQueue;
/*      */ import com.sun.jdi.request.BreakpointRequest;
/*      */ import com.sun.jdi.request.ClassPrepareRequest;
/*      */ import com.sun.jdi.request.ClassUnloadRequest;
/*      */ import com.sun.jdi.request.EventRequestManager;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ class VirtualMachineImpl extends MirrorImpl implements PathSearchingVirtualMachine, ThreadListener {
/*      */   public final int sizeofFieldRef;
/*      */   public final int sizeofMethodRef;
/*      */   public final int sizeofObjectRef;
/*      */   public final int sizeofClassRef;
/*      */   public final int sizeofFrameRef;
/*      */   final int sequenceNumber;
/*      */   private final TargetVM target;
/*      */   private final EventQueueImpl eventQueue;
/*      */   private final EventRequestManagerImpl internalEventRequestManager;
/*      */   private final EventRequestManagerImpl eventRequestManager;
/*      */   final VirtualMachineManagerImpl vmManager;
/*      */   private final ThreadGroup threadGroupForJDI;
/*      */   int traceFlags;
/*   65 */   static int TRACE_RAW_SENDS = 16777216;
/*   66 */   static int TRACE_RAW_RECEIVES = 33554432;
/*      */   
/*      */   boolean traceReceives;
/*      */   
/*      */   private Map<Long, ReferenceType> typesByID;
/*      */   
/*      */   private TreeSet<ReferenceType> typesBySignature;
/*      */   
/*      */   private boolean retrievedAllTypes;
/*      */   
/*      */   private String defaultStratum;
/*      */   
/*      */   private final Map<Long, SoftObjectReference> objectsByID;
/*      */   
/*      */   private final ReferenceQueue<ObjectReferenceImpl> referenceQueue;
/*      */   
/*      */   private static final int DISPOSE_THRESHOLD = 50;
/*      */   
/*      */   private final List<SoftObjectReference> batchedDisposeRequests;
/*      */   
/*      */   private JDWP.VirtualMachine.Version versionInfo;
/*      */   
/*      */   private JDWP.VirtualMachine.ClassPaths pathInfo;
/*      */   
/*      */   private JDWP.VirtualMachine.Capabilities capabilities;
/*      */   
/*      */   private JDWP.VirtualMachine.CapabilitiesNew capabilitiesNew;
/*      */   
/*      */   private BooleanType theBooleanType;
/*      */   
/*      */   private ByteType theByteType;
/*      */   
/*      */   private CharType theCharType;
/*      */   
/*      */   private ShortType theShortType;
/*      */   
/*      */   private IntegerType theIntegerType;
/*      */   
/*      */   private LongType theLongType;
/*      */   
/*      */   private FloatType theFloatType;
/*      */   
/*      */   private DoubleType theDoubleType;
/*      */   
/*      */   private VoidType theVoidType;
/*      */   
/*      */   private VoidValue voidVal;
/*      */   
/*      */   private Process process;
/*      */   private VMState state;
/*      */   private Object initMonitor;
/*      */   private boolean initComplete;
/*      */   private boolean shutdown;
/*      */   
/*      */   private void notifyInitCompletion() {
/*  121 */     synchronized (this.initMonitor) {
/*  122 */       this.initComplete = true;
/*  123 */       this.initMonitor.notifyAll();
/*      */     } 
/*      */   }
/*      */   
/*      */   void waitInitCompletion() {
/*  128 */     synchronized (this.initMonitor) {
/*  129 */       while (!this.initComplete) {
/*      */         try {
/*  131 */           this.initMonitor.wait();
/*  132 */         } catch (InterruptedException interruptedException) {}
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   VMState state() {
/*  140 */     return this.state;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean threadResumable(ThreadAction paramThreadAction) {
/*  151 */     this.state.thaw(paramThreadAction.thread());
/*  152 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   VirtualMachineImpl(VirtualMachineManager paramVirtualMachineManager, Connection paramConnection, Process paramProcess, int paramInt) {
/*  158 */     super(null); JDWP.VirtualMachine.IDSizes iDSizes; this.traceFlags = 0; this.traceReceives = false; this.retrievedAllTypes = false; this.defaultStratum = null; this.objectsByID = new HashMap<>(); this.referenceQueue = new ReferenceQueue<>(); this.batchedDisposeRequests = Collections.synchronizedList(new ArrayList<>(60)); this.capabilities = null; this.capabilitiesNew = null; this.state = new VMState(this); this.initMonitor = new Object(); this.initComplete = false; this.shutdown = false;
/*  159 */     this.vm = this;
/*      */     
/*  161 */     this.vmManager = (VirtualMachineManagerImpl)paramVirtualMachineManager;
/*  162 */     this.process = paramProcess;
/*  163 */     this.sequenceNumber = paramInt;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  168 */     this
/*      */       
/*  170 */       .threadGroupForJDI = new ThreadGroup(this.vmManager.mainGroupForJDI(), "JDI [" + hashCode() + "]");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  176 */     this.target = new TargetVM(this, paramConnection);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  182 */     EventQueueImpl eventQueueImpl = new EventQueueImpl((VirtualMachine)this, this.target);
/*  183 */     new InternalEventHandler(this, eventQueueImpl);
/*      */ 
/*      */ 
/*      */     
/*  187 */     this.eventQueue = new EventQueueImpl((VirtualMachine)this, this.target);
/*  188 */     this.eventRequestManager = new EventRequestManagerImpl((VirtualMachine)this);
/*      */     
/*  190 */     this.target.start();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  198 */       iDSizes = JDWP.VirtualMachine.IDSizes.process(this.vm);
/*  199 */     } catch (JDWPException jDWPException) {
/*  200 */       throw jDWPException.toJDIException();
/*      */     } 
/*  202 */     this.sizeofFieldRef = iDSizes.fieldIDSize;
/*  203 */     this.sizeofMethodRef = iDSizes.methodIDSize;
/*  204 */     this.sizeofObjectRef = iDSizes.objectIDSize;
/*  205 */     this.sizeofClassRef = iDSizes.referenceTypeIDSize;
/*  206 */     this.sizeofFrameRef = iDSizes.frameIDSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  220 */     this.internalEventRequestManager = new EventRequestManagerImpl((VirtualMachine)this);
/*  221 */     ClassPrepareRequest classPrepareRequest = this.internalEventRequestManager.createClassPrepareRequest();
/*  222 */     classPrepareRequest.setSuspendPolicy(0);
/*  223 */     classPrepareRequest.enable();
/*  224 */     ClassUnloadRequest classUnloadRequest = this.internalEventRequestManager.createClassUnloadRequest();
/*  225 */     classUnloadRequest.setSuspendPolicy(0);
/*  226 */     classUnloadRequest.enable();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  232 */     notifyInitCompletion();
/*      */   }
/*      */   
/*      */   EventRequestManagerImpl getInternalEventRequestManager() {
/*  236 */     return this.internalEventRequestManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void validateVM() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object paramObject) {
/*  261 */     return (this == paramObject);
/*      */   }
/*      */   
/*      */   public int hashCode() {
/*  265 */     return System.identityHashCode(this);
/*      */   }
/*      */   public List<ReferenceType> classesByName(String paramString) {
/*      */     List<ReferenceType> list;
/*  269 */     validateVM();
/*  270 */     String str = JNITypeParser.typeNameToSignature(paramString);
/*      */     
/*  272 */     if (this.retrievedAllTypes) {
/*  273 */       list = findReferenceTypes(str);
/*      */     } else {
/*  275 */       list = retrieveClassesBySignature(str);
/*      */     } 
/*  277 */     return Collections.unmodifiableList(list);
/*      */   }
/*      */   public List<ReferenceType> allClasses() {
/*      */     ArrayList<ReferenceType> arrayList;
/*  281 */     validateVM();
/*      */     
/*  283 */     if (!this.retrievedAllTypes) {
/*  284 */       retrieveAllClasses();
/*      */     }
/*      */     
/*  287 */     synchronized (this) {
/*  288 */       arrayList = new ArrayList<>(this.typesBySignature);
/*      */     } 
/*  290 */     return Collections.unmodifiableList(arrayList);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void redefineClasses(Map<? extends ReferenceType, byte[]> paramMap) {
/*  296 */     int i = paramMap.size();
/*  297 */     JDWP.VirtualMachine.RedefineClasses.ClassDef[] arrayOfClassDef = new JDWP.VirtualMachine.RedefineClasses.ClassDef[i];
/*      */     
/*  299 */     validateVM();
/*  300 */     if (!canRedefineClasses()) {
/*  301 */       throw new UnsupportedOperationException();
/*      */     }
/*  303 */     Iterator<Map.Entry> iterator = paramMap.entrySet().iterator();
/*  304 */     for (byte b = 0; iterator.hasNext(); b++) {
/*  305 */       Map.Entry entry = iterator.next();
/*  306 */       ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)entry.getKey();
/*  307 */       validateMirror(referenceTypeImpl);
/*  308 */       arrayOfClassDef[b] = new JDWP.VirtualMachine.RedefineClasses.ClassDef(referenceTypeImpl, (byte[])entry
/*  309 */           .getValue());
/*      */     } 
/*      */ 
/*      */     
/*  313 */     this.vm.state().thaw();
/*      */ 
/*      */     
/*      */     try {
/*  317 */       JDWP.VirtualMachine.RedefineClasses.process(this.vm, arrayOfClassDef);
/*  318 */     } catch (JDWPException jDWPException) {
/*  319 */       switch (jDWPException.errorCode()) {
/*      */         case 60:
/*  321 */           throw new ClassFormatError("class not in class file format");
/*      */         
/*      */         case 61:
/*  324 */           throw new ClassCircularityError("circularity has been detected while initializing a class");
/*      */         
/*      */         case 62:
/*  327 */           throw new VerifyError("verifier detected internal inconsistency or security problem");
/*      */         
/*      */         case 68:
/*  330 */           throw new UnsupportedClassVersionError("version numbers of class are not supported");
/*      */         
/*      */         case 63:
/*  333 */           throw new UnsupportedOperationException("add method not implemented");
/*      */         
/*      */         case 64:
/*  336 */           throw new UnsupportedOperationException("schema change not implemented");
/*      */         
/*      */         case 66:
/*  339 */           throw new UnsupportedOperationException("hierarchy change not implemented");
/*      */         
/*      */         case 67:
/*  342 */           throw new UnsupportedOperationException("delete method not implemented");
/*      */         
/*      */         case 70:
/*  345 */           throw new UnsupportedOperationException("changes to class modifiers not implemented");
/*      */         
/*      */         case 71:
/*  348 */           throw new UnsupportedOperationException("changes to method modifiers not implemented");
/*      */         
/*      */         case 69:
/*  351 */           throw new NoClassDefFoundError("class names do not match");
/*      */       } 
/*      */       
/*  354 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  359 */     ArrayList<BreakpointRequest> arrayList = new ArrayList();
/*  360 */     EventRequestManager eventRequestManager = eventRequestManager();
/*  361 */     iterator = eventRequestManager.breakpointRequests().iterator();
/*  362 */     while (iterator.hasNext()) {
/*  363 */       BreakpointRequest breakpointRequest = (BreakpointRequest)iterator.next();
/*  364 */       if (paramMap.containsKey(breakpointRequest.location().declaringType())) {
/*  365 */         arrayList.add(breakpointRequest);
/*      */       }
/*      */     } 
/*  368 */     eventRequestManager.deleteEventRequests(arrayList);
/*      */ 
/*      */     
/*  371 */     iterator = paramMap.keySet().iterator();
/*  372 */     while (iterator.hasNext()) {
/*  373 */       ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)iterator.next();
/*  374 */       referenceTypeImpl.noticeRedefineClass();
/*      */     } 
/*      */   }
/*      */   
/*      */   public List<ThreadReference> allThreads() {
/*  379 */     validateVM();
/*  380 */     return this.state.allThreads();
/*      */   }
/*      */   
/*      */   public List<ThreadGroupReference> topLevelThreadGroups() {
/*  384 */     validateVM();
/*  385 */     return this.state.topLevelThreadGroups();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   PacketStream sendResumingCommand(CommandSender paramCommandSender) {
/*  394 */     return this.state.thawCommand(paramCommandSender);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void notifySuspend() {
/*  402 */     this.state.freeze();
/*      */   }
/*      */   
/*      */   public void suspend() {
/*  406 */     validateVM();
/*      */     try {
/*  408 */       JDWP.VirtualMachine.Suspend.process(this.vm);
/*  409 */     } catch (JDWPException jDWPException) {
/*  410 */       throw jDWPException.toJDIException();
/*      */     } 
/*  412 */     notifySuspend();
/*      */   }
/*      */   
/*      */   public void resume() {
/*  416 */     validateVM();
/*  417 */     CommandSender commandSender = new CommandSender()
/*      */       {
/*      */         public PacketStream send() {
/*  420 */           return JDWP.VirtualMachine.Resume.enqueueCommand(VirtualMachineImpl.this.vm);
/*      */         }
/*      */       };
/*      */     try {
/*  424 */       PacketStream packetStream = this.state.thawCommand(commandSender);
/*  425 */       JDWP.VirtualMachine.Resume.waitForReply(this.vm, packetStream);
/*  426 */     } catch (VMDisconnectedException vMDisconnectedException) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  444 */     catch (JDWPException jDWPException) {
/*  445 */       switch (jDWPException.errorCode()) {
/*      */         case 112:
/*      */           return;
/*      */       } 
/*  449 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EventQueue eventQueue() {
/*  460 */     return this.eventQueue;
/*      */   }
/*      */   
/*      */   public EventRequestManager eventRequestManager() {
/*  464 */     validateVM();
/*  465 */     return this.eventRequestManager;
/*      */   }
/*      */   
/*      */   EventRequestManagerImpl eventRequestManagerImpl() {
/*  469 */     return this.eventRequestManager;
/*      */   }
/*      */   
/*      */   public BooleanValue mirrorOf(boolean paramBoolean) {
/*  473 */     validateVM();
/*  474 */     return new BooleanValueImpl((VirtualMachine)this, paramBoolean);
/*      */   }
/*      */   
/*      */   public ByteValue mirrorOf(byte paramByte) {
/*  478 */     validateVM();
/*  479 */     return new ByteValueImpl((VirtualMachine)this, paramByte);
/*      */   }
/*      */   
/*      */   public CharValue mirrorOf(char paramChar) {
/*  483 */     validateVM();
/*  484 */     return new CharValueImpl((VirtualMachine)this, paramChar);
/*      */   }
/*      */   
/*      */   public ShortValue mirrorOf(short paramShort) {
/*  488 */     validateVM();
/*  489 */     return new ShortValueImpl((VirtualMachine)this, paramShort);
/*      */   }
/*      */   
/*      */   public IntegerValue mirrorOf(int paramInt) {
/*  493 */     validateVM();
/*  494 */     return new IntegerValueImpl((VirtualMachine)this, paramInt);
/*      */   }
/*      */   
/*      */   public LongValue mirrorOf(long paramLong) {
/*  498 */     validateVM();
/*  499 */     return new LongValueImpl((VirtualMachine)this, paramLong);
/*      */   }
/*      */   
/*      */   public FloatValue mirrorOf(float paramFloat) {
/*  503 */     validateVM();
/*  504 */     return new FloatValueImpl((VirtualMachine)this, paramFloat);
/*      */   }
/*      */   
/*      */   public DoubleValue mirrorOf(double paramDouble) {
/*  508 */     validateVM();
/*  509 */     return new DoubleValueImpl((VirtualMachine)this, paramDouble);
/*      */   }
/*      */   
/*      */   public StringReference mirrorOf(String paramString) {
/*  513 */     validateVM();
/*      */     try {
/*  515 */       return 
/*  516 */         (JDWP.VirtualMachine.CreateString.process(this.vm, paramString)).stringObject;
/*  517 */     } catch (JDWPException jDWPException) {
/*  518 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */   }
/*      */   
/*      */   public VoidValue mirrorOfVoid() {
/*  523 */     if (this.voidVal == null) {
/*  524 */       this.voidVal = new VoidValueImpl((VirtualMachine)this);
/*      */     }
/*  526 */     return this.voidVal;
/*      */   }
/*      */   public long[] instanceCounts(List<? extends ReferenceType> paramList) {
/*      */     long[] arrayOfLong;
/*  530 */     if (!canGetInstanceInfo()) {
/*  531 */       throw new UnsupportedOperationException("target does not support getting instances");
/*      */     }
/*      */ 
/*      */     
/*  535 */     ReferenceTypeImpl[] arrayOfReferenceTypeImpl = new ReferenceTypeImpl[paramList.size()];
/*  536 */     byte b = 0;
/*  537 */     for (ReferenceType referenceType : paramList) {
/*  538 */       validateMirror((Mirror)referenceType);
/*  539 */       arrayOfReferenceTypeImpl[b++] = (ReferenceTypeImpl)referenceType;
/*      */     } 
/*      */     
/*      */     try {
/*  543 */       arrayOfLong = (JDWP.VirtualMachine.InstanceCounts.process(this.vm, arrayOfReferenceTypeImpl)).counts;
/*  544 */     } catch (JDWPException jDWPException) {
/*  545 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */     
/*  548 */     return arrayOfLong;
/*      */   }
/*      */   
/*      */   public void dispose() {
/*  552 */     validateVM();
/*  553 */     this.shutdown = true;
/*      */     try {
/*  555 */       JDWP.VirtualMachine.Dispose.process(this.vm);
/*  556 */     } catch (JDWPException jDWPException) {
/*  557 */       throw jDWPException.toJDIException();
/*      */     } 
/*  559 */     this.target.stopListening();
/*      */   }
/*      */   
/*      */   public void exit(int paramInt) {
/*  563 */     validateVM();
/*  564 */     this.shutdown = true;
/*      */     try {
/*  566 */       JDWP.VirtualMachine.Exit.process(this.vm, paramInt);
/*  567 */     } catch (JDWPException jDWPException) {
/*  568 */       throw jDWPException.toJDIException();
/*      */     } 
/*  570 */     this.target.stopListening();
/*      */   }
/*      */   
/*      */   public Process process() {
/*  574 */     validateVM();
/*  575 */     return this.process;
/*      */   }
/*      */   
/*      */   private JDWP.VirtualMachine.Version versionInfo() {
/*      */     try {
/*  580 */       if (this.versionInfo == null)
/*      */       {
/*  582 */         this.versionInfo = JDWP.VirtualMachine.Version.process(this.vm);
/*      */       }
/*  584 */       return this.versionInfo;
/*  585 */     } catch (JDWPException jDWPException) {
/*  586 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */   }
/*      */   public String description() {
/*  590 */     validateVM();
/*      */     
/*  592 */     return MessageFormat.format(this.vmManager.getString("version_format"), new Object[] { "" + this.vmManager
/*  593 */           .majorInterfaceVersion(), "" + this.vmManager
/*  594 */           .minorInterfaceVersion(), 
/*  595 */           (versionInfo()).description });
/*      */   }
/*      */   
/*      */   public String version() {
/*  599 */     validateVM();
/*  600 */     return (versionInfo()).vmVersion;
/*      */   }
/*      */   
/*      */   public String name() {
/*  604 */     validateVM();
/*  605 */     return (versionInfo()).vmName;
/*      */   }
/*      */   
/*      */   public boolean canWatchFieldModification() {
/*  609 */     validateVM();
/*  610 */     return (capabilities()).canWatchFieldModification;
/*      */   }
/*      */   public boolean canWatchFieldAccess() {
/*  613 */     validateVM();
/*  614 */     return (capabilities()).canWatchFieldAccess;
/*      */   }
/*      */   public boolean canGetBytecodes() {
/*  617 */     validateVM();
/*  618 */     return (capabilities()).canGetBytecodes;
/*      */   }
/*      */   public boolean canGetSyntheticAttribute() {
/*  621 */     validateVM();
/*  622 */     return (capabilities()).canGetSyntheticAttribute;
/*      */   }
/*      */   public boolean canGetOwnedMonitorInfo() {
/*  625 */     validateVM();
/*  626 */     return (capabilities()).canGetOwnedMonitorInfo;
/*      */   }
/*      */   public boolean canGetCurrentContendedMonitor() {
/*  629 */     validateVM();
/*  630 */     return (capabilities()).canGetCurrentContendedMonitor;
/*      */   }
/*      */   public boolean canGetMonitorInfo() {
/*  633 */     validateVM();
/*  634 */     return (capabilities()).canGetMonitorInfo;
/*      */   }
/*      */   
/*      */   private boolean hasNewCapabilities() {
/*  638 */     return ((versionInfo()).jdwpMajor > 1 || 
/*  639 */       (versionInfo()).jdwpMinor >= 4);
/*      */   }
/*      */   
/*      */   boolean canGet1_5LanguageFeatures() {
/*  643 */     return ((versionInfo()).jdwpMajor > 1 || 
/*  644 */       (versionInfo()).jdwpMinor >= 5);
/*      */   }
/*      */   
/*      */   public boolean canUseInstanceFilters() {
/*  648 */     validateVM();
/*  649 */     return (hasNewCapabilities() && 
/*  650 */       (capabilitiesNew()).canUseInstanceFilters);
/*      */   }
/*      */   public boolean canRedefineClasses() {
/*  653 */     validateVM();
/*  654 */     return (hasNewCapabilities() && 
/*  655 */       (capabilitiesNew()).canRedefineClasses);
/*      */   }
/*      */   public boolean canAddMethod() {
/*  658 */     validateVM();
/*  659 */     return (hasNewCapabilities() && 
/*  660 */       (capabilitiesNew()).canAddMethod);
/*      */   }
/*      */   public boolean canUnrestrictedlyRedefineClasses() {
/*  663 */     validateVM();
/*  664 */     return (hasNewCapabilities() && 
/*  665 */       (capabilitiesNew()).canUnrestrictedlyRedefineClasses);
/*      */   }
/*      */   public boolean canPopFrames() {
/*  668 */     validateVM();
/*  669 */     return (hasNewCapabilities() && 
/*  670 */       (capabilitiesNew()).canPopFrames);
/*      */   }
/*      */   public boolean canGetMethodReturnValues() {
/*  673 */     return ((versionInfo()).jdwpMajor > 1 || 
/*  674 */       (versionInfo()).jdwpMinor >= 6);
/*      */   }
/*      */   public boolean canGetInstanceInfo() {
/*  677 */     if ((versionInfo()).jdwpMajor > 1 || 
/*  678 */       (versionInfo()).jdwpMinor >= 6) {
/*  679 */       validateVM();
/*  680 */       return (hasNewCapabilities() && 
/*  681 */         (capabilitiesNew()).canGetInstanceInfo);
/*      */     } 
/*  683 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canUseSourceNameFilters() {
/*  687 */     return ((versionInfo()).jdwpMajor > 1 || 
/*  688 */       (versionInfo()).jdwpMinor >= 6);
/*      */   }
/*      */   public boolean canForceEarlyReturn() {
/*  691 */     validateVM();
/*  692 */     return (hasNewCapabilities() && 
/*  693 */       (capabilitiesNew()).canForceEarlyReturn);
/*      */   }
/*      */   public boolean canBeModified() {
/*  696 */     return true;
/*      */   }
/*      */   public boolean canGetSourceDebugExtension() {
/*  699 */     validateVM();
/*  700 */     return (hasNewCapabilities() && 
/*  701 */       (capabilitiesNew()).canGetSourceDebugExtension);
/*      */   }
/*      */   public boolean canGetClassFileVersion() {
/*  704 */     return ((versionInfo()).jdwpMajor > 1 || 
/*  705 */       (versionInfo()).jdwpMinor >= 6);
/*      */   }
/*      */   public boolean canGetConstantPool() {
/*  708 */     validateVM();
/*  709 */     return (hasNewCapabilities() && 
/*  710 */       (capabilitiesNew()).canGetConstantPool);
/*      */   }
/*      */   public boolean canRequestVMDeathEvent() {
/*  713 */     validateVM();
/*  714 */     return (hasNewCapabilities() && 
/*  715 */       (capabilitiesNew()).canRequestVMDeathEvent);
/*      */   }
/*      */   public boolean canRequestMonitorEvents() {
/*  718 */     validateVM();
/*  719 */     return (hasNewCapabilities() && 
/*  720 */       (capabilitiesNew()).canRequestMonitorEvents);
/*      */   }
/*      */   public boolean canGetMonitorFrameInfo() {
/*  723 */     validateVM();
/*  724 */     return (hasNewCapabilities() && 
/*  725 */       (capabilitiesNew()).canGetMonitorFrameInfo);
/*      */   }
/*      */   
/*      */   public void setDebugTraceMode(int paramInt) {
/*  729 */     validateVM();
/*  730 */     this.traceFlags = paramInt;
/*  731 */     this.traceReceives = ((paramInt & 0x2) != 0);
/*      */   }
/*      */   
/*      */   void printTrace(String paramString) {
/*  735 */     System.err.println("[JDI: " + paramString + "]");
/*      */   }
/*      */   
/*      */   void printReceiveTrace(int paramInt, String paramString) {
/*  739 */     StringBuffer stringBuffer = new StringBuffer("Receiving:");
/*  740 */     for (int i = paramInt; i > 0; i--) {
/*  741 */       stringBuffer.append("    ");
/*      */     }
/*  743 */     stringBuffer.append(paramString);
/*  744 */     printTrace(stringBuffer.toString());
/*      */   }
/*      */   
/*      */   private synchronized ReferenceTypeImpl addReferenceType(long paramLong, int paramInt, String paramString) {
/*      */     InterfaceTypeImpl interfaceTypeImpl;
/*      */     ArrayTypeImpl arrayTypeImpl;
/*  750 */     if (this.typesByID == null) {
/*  751 */       initReferenceTypes();
/*      */     }
/*  753 */     ClassTypeImpl classTypeImpl = null;
/*  754 */     switch (paramInt) {
/*      */       case 1:
/*  756 */         classTypeImpl = new ClassTypeImpl((VirtualMachine)this.vm, paramLong);
/*      */         break;
/*      */       case 2:
/*  759 */         interfaceTypeImpl = new InterfaceTypeImpl((VirtualMachine)this.vm, paramLong);
/*      */         break;
/*      */       case 3:
/*  762 */         arrayTypeImpl = new ArrayTypeImpl((VirtualMachine)this.vm, paramLong);
/*      */         break;
/*      */       default:
/*  765 */         throw new InternalException("Invalid reference type tag");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  774 */     if (paramString != null) {
/*  775 */       arrayTypeImpl.setSignature(paramString);
/*      */     }
/*      */     
/*  778 */     this.typesByID.put(new Long(paramLong), arrayTypeImpl);
/*  779 */     this.typesBySignature.add(arrayTypeImpl);
/*      */     
/*  781 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  782 */       this.vm.printTrace("Caching new ReferenceType, sig=" + paramString + ", id=" + paramLong);
/*      */     }
/*      */ 
/*      */     
/*  786 */     return arrayTypeImpl;
/*      */   }
/*      */   
/*      */   synchronized void removeReferenceType(String paramString) {
/*  790 */     if (this.typesByID == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  798 */     Iterator<ReferenceType> iterator = this.typesBySignature.iterator();
/*  799 */     byte b = 0;
/*  800 */     while (iterator.hasNext()) {
/*  801 */       ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)iterator.next();
/*  802 */       int i = paramString.compareTo(referenceTypeImpl.signature());
/*  803 */       if (i == 0) {
/*  804 */         b++;
/*  805 */         iterator.remove();
/*  806 */         this.typesByID.remove(new Long(referenceTypeImpl.ref()));
/*  807 */         if ((this.vm.traceFlags & 0x8) != 0) {
/*  808 */           this.vm.printTrace("Uncaching ReferenceType, sig=" + paramString + ", id=" + referenceTypeImpl
/*  809 */               .ref());
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  821 */     if (b > 1) {
/*  822 */       retrieveClassesBySignature(paramString);
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized List<ReferenceType> findReferenceTypes(String paramString) {
/*  827 */     if (this.typesByID == null) {
/*  828 */       return new ArrayList<>(0);
/*      */     }
/*  830 */     Iterator<ReferenceType> iterator = this.typesBySignature.iterator();
/*  831 */     ArrayList<ReferenceTypeImpl> arrayList = new ArrayList();
/*  832 */     while (iterator.hasNext()) {
/*  833 */       ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)iterator.next();
/*  834 */       int i = paramString.compareTo(referenceTypeImpl.signature());
/*  835 */       if (i == 0) {
/*  836 */         arrayList.add(referenceTypeImpl);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  842 */     return (List)arrayList;
/*      */   }
/*      */   
/*      */   private void initReferenceTypes() {
/*  846 */     this.typesByID = new HashMap<>(300);
/*  847 */     this.typesBySignature = new TreeSet<>();
/*      */   }
/*      */   
/*      */   ReferenceTypeImpl referenceType(long paramLong, byte paramByte) {
/*  851 */     return referenceType(paramLong, paramByte, (String)null);
/*      */   }
/*      */   
/*      */   ClassTypeImpl classType(long paramLong) {
/*  855 */     return (ClassTypeImpl)referenceType(paramLong, 1, (String)null);
/*      */   }
/*      */   
/*      */   InterfaceTypeImpl interfaceType(long paramLong) {
/*  859 */     return (InterfaceTypeImpl)referenceType(paramLong, 2, (String)null);
/*      */   }
/*      */   
/*      */   ArrayTypeImpl arrayType(long paramLong) {
/*  863 */     return (ArrayTypeImpl)referenceType(paramLong, 3, (String)null);
/*      */   }
/*      */ 
/*      */   
/*      */   ReferenceTypeImpl referenceType(long paramLong, int paramInt, String paramString) {
/*  868 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  869 */       StringBuffer stringBuffer = new StringBuffer();
/*  870 */       stringBuffer.append("Looking up ");
/*  871 */       if (paramInt == 1) {
/*  872 */         stringBuffer.append("Class");
/*  873 */       } else if (paramInt == 2) {
/*  874 */         stringBuffer.append("Interface");
/*  875 */       } else if (paramInt == 3) {
/*  876 */         stringBuffer.append("ArrayType");
/*      */       } else {
/*  878 */         stringBuffer.append("UNKNOWN TAG: " + paramInt);
/*      */       } 
/*  880 */       if (paramString != null) {
/*  881 */         stringBuffer.append(", signature='" + paramString + "'");
/*      */       }
/*  883 */       stringBuffer.append(", id=" + paramLong);
/*  884 */       this.vm.printTrace(stringBuffer.toString());
/*      */     } 
/*  886 */     if (paramLong == 0L) {
/*  887 */       return null;
/*      */     }
/*  889 */     ReferenceTypeImpl referenceTypeImpl = null;
/*  890 */     synchronized (this) {
/*  891 */       if (this.typesByID != null) {
/*  892 */         referenceTypeImpl = (ReferenceTypeImpl)this.typesByID.get(new Long(paramLong));
/*      */       }
/*  894 */       if (referenceTypeImpl == null) {
/*  895 */         referenceTypeImpl = addReferenceType(paramLong, paramInt, paramString);
/*      */       }
/*      */     } 
/*  898 */     return referenceTypeImpl;
/*      */   }
/*      */ 
/*      */   
/*      */   private JDWP.VirtualMachine.Capabilities capabilities() {
/*  903 */     if (this.capabilities == null) {
/*      */       try {
/*  905 */         this
/*  906 */           .capabilities = JDWP.VirtualMachine.Capabilities.process(this.vm);
/*  907 */       } catch (JDWPException jDWPException) {
/*  908 */         throw jDWPException.toJDIException();
/*      */       } 
/*      */     }
/*  911 */     return this.capabilities;
/*      */   }
/*      */   
/*      */   private JDWP.VirtualMachine.CapabilitiesNew capabilitiesNew() {
/*  915 */     if (this.capabilitiesNew == null) {
/*      */       try {
/*  917 */         this
/*  918 */           .capabilitiesNew = JDWP.VirtualMachine.CapabilitiesNew.process(this.vm);
/*  919 */       } catch (JDWPException jDWPException) {
/*  920 */         throw jDWPException.toJDIException();
/*      */       } 
/*      */     }
/*  923 */     return this.capabilitiesNew;
/*      */   }
/*      */   private List<ReferenceType> retrieveClassesBySignature(String paramString) {
/*      */     JDWP.VirtualMachine.ClassesBySignature.ClassInfo[] arrayOfClassInfo;
/*  927 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  928 */       this.vm.printTrace("Retrieving matching ReferenceTypes, sig=" + paramString);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  933 */       arrayOfClassInfo = (JDWP.VirtualMachine.ClassesBySignature.process(this.vm, paramString)).classes;
/*  934 */     } catch (JDWPException jDWPException) {
/*  935 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */     
/*  938 */     int i = arrayOfClassInfo.length;
/*  939 */     ArrayList<ReferenceTypeImpl> arrayList = new ArrayList(i);
/*      */ 
/*      */     
/*  942 */     synchronized (this) {
/*  943 */       for (byte b = 0; b < i; b++) {
/*  944 */         JDWP.VirtualMachine.ClassesBySignature.ClassInfo classInfo = arrayOfClassInfo[b];
/*      */         
/*  946 */         ReferenceTypeImpl referenceTypeImpl = referenceType(classInfo.typeID, classInfo.refTypeTag, paramString);
/*      */ 
/*      */         
/*  949 */         referenceTypeImpl.setStatus(classInfo.status);
/*  950 */         arrayList.add(referenceTypeImpl);
/*      */       } 
/*      */     } 
/*  953 */     return (List)arrayList;
/*      */   }
/*      */   
/*      */   private void retrieveAllClasses1_4() {
/*      */     JDWP.VirtualMachine.AllClasses.ClassInfo[] arrayOfClassInfo;
/*      */     try {
/*  959 */       arrayOfClassInfo = (JDWP.VirtualMachine.AllClasses.process(this.vm)).classes;
/*  960 */     } catch (JDWPException jDWPException) {
/*  961 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  966 */     synchronized (this) {
/*  967 */       if (!this.retrievedAllTypes) {
/*      */         
/*  969 */         int i = arrayOfClassInfo.length;
/*  970 */         for (byte b = 0; b < i; b++) {
/*  971 */           JDWP.VirtualMachine.AllClasses.ClassInfo classInfo = arrayOfClassInfo[b];
/*      */           
/*  973 */           ReferenceTypeImpl referenceTypeImpl = referenceType(classInfo.typeID, classInfo.refTypeTag, classInfo.signature);
/*      */ 
/*      */           
/*  976 */           referenceTypeImpl.setStatus(classInfo.status);
/*      */         } 
/*  978 */         this.retrievedAllTypes = true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private void retrieveAllClasses() {
/*      */     JDWP.VirtualMachine.AllClassesWithGeneric.ClassInfo[] arrayOfClassInfo;
/*  984 */     if ((this.vm.traceFlags & 0x8) != 0) {
/*  985 */       this.vm.printTrace("Retrieving all ReferenceTypes");
/*      */     }
/*      */     
/*  988 */     if (!this.vm.canGet1_5LanguageFeatures()) {
/*  989 */       retrieveAllClasses1_4();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1000 */       arrayOfClassInfo = (JDWP.VirtualMachine.AllClassesWithGeneric.process(this.vm)).classes;
/* 1001 */     } catch (JDWPException jDWPException) {
/* 1002 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1007 */     synchronized (this) {
/* 1008 */       if (!this.retrievedAllTypes) {
/*      */         
/* 1010 */         int i = arrayOfClassInfo.length;
/* 1011 */         for (byte b = 0; b < i; b++) {
/* 1012 */           JDWP.VirtualMachine.AllClassesWithGeneric.ClassInfo classInfo = arrayOfClassInfo[b];
/*      */           
/* 1014 */           ReferenceTypeImpl referenceTypeImpl = referenceType(classInfo.typeID, classInfo.refTypeTag, classInfo.signature);
/*      */ 
/*      */           
/* 1017 */           referenceTypeImpl.setGenericSignature(classInfo.genericSignature);
/* 1018 */           referenceTypeImpl.setStatus(classInfo.status);
/*      */         } 
/* 1020 */         this.retrievedAllTypes = true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   void sendToTarget(Packet paramPacket) {
/* 1026 */     this.target.send(paramPacket);
/*      */   }
/*      */   
/*      */   void waitForTargetReply(Packet paramPacket) {
/* 1030 */     this.target.waitForReply(paramPacket);
/*      */ 
/*      */ 
/*      */     
/* 1034 */     processBatchedDisposes();
/*      */   }
/*      */   
/*      */   Type findBootType(String paramString) throws ClassNotLoadedException {
/* 1038 */     List<ReferenceType> list = retrieveClassesBySignature(paramString);
/* 1039 */     Iterator<ReferenceType> iterator = list.iterator();
/* 1040 */     while (iterator.hasNext()) {
/* 1041 */       ReferenceType referenceType = iterator.next();
/* 1042 */       if (referenceType.classLoader() == null) {
/* 1043 */         return (Type)referenceType;
/*      */       }
/*      */     } 
/* 1046 */     JNITypeParser jNITypeParser = new JNITypeParser(paramString);
/* 1047 */     throw new ClassNotLoadedException(jNITypeParser.typeName(), "Type " + jNITypeParser
/* 1048 */         .typeName() + " not loaded");
/*      */   }
/*      */   
/*      */   BooleanType theBooleanType() {
/* 1052 */     if (this.theBooleanType == null) {
/* 1053 */       synchronized (this) {
/* 1054 */         if (this.theBooleanType == null) {
/* 1055 */           this.theBooleanType = new BooleanTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1059 */     return this.theBooleanType;
/*      */   }
/*      */   
/*      */   ByteType theByteType() {
/* 1063 */     if (this.theByteType == null) {
/* 1064 */       synchronized (this) {
/* 1065 */         if (this.theByteType == null) {
/* 1066 */           this.theByteType = new ByteTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1070 */     return this.theByteType;
/*      */   }
/*      */   
/*      */   CharType theCharType() {
/* 1074 */     if (this.theCharType == null) {
/* 1075 */       synchronized (this) {
/* 1076 */         if (this.theCharType == null) {
/* 1077 */           this.theCharType = new CharTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1081 */     return this.theCharType;
/*      */   }
/*      */   
/*      */   ShortType theShortType() {
/* 1085 */     if (this.theShortType == null) {
/* 1086 */       synchronized (this) {
/* 1087 */         if (this.theShortType == null) {
/* 1088 */           this.theShortType = new ShortTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1092 */     return this.theShortType;
/*      */   }
/*      */   
/*      */   IntegerType theIntegerType() {
/* 1096 */     if (this.theIntegerType == null) {
/* 1097 */       synchronized (this) {
/* 1098 */         if (this.theIntegerType == null) {
/* 1099 */           this.theIntegerType = new IntegerTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1103 */     return this.theIntegerType;
/*      */   }
/*      */   
/*      */   LongType theLongType() {
/* 1107 */     if (this.theLongType == null) {
/* 1108 */       synchronized (this) {
/* 1109 */         if (this.theLongType == null) {
/* 1110 */           this.theLongType = new LongTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1114 */     return this.theLongType;
/*      */   }
/*      */   
/*      */   FloatType theFloatType() {
/* 1118 */     if (this.theFloatType == null) {
/* 1119 */       synchronized (this) {
/* 1120 */         if (this.theFloatType == null) {
/* 1121 */           this.theFloatType = new FloatTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1125 */     return this.theFloatType;
/*      */   }
/*      */   
/*      */   DoubleType theDoubleType() {
/* 1129 */     if (this.theDoubleType == null) {
/* 1130 */       synchronized (this) {
/* 1131 */         if (this.theDoubleType == null) {
/* 1132 */           this.theDoubleType = new DoubleTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1136 */     return this.theDoubleType;
/*      */   }
/*      */   
/*      */   VoidType theVoidType() {
/* 1140 */     if (this.theVoidType == null) {
/* 1141 */       synchronized (this) {
/* 1142 */         if (this.theVoidType == null) {
/* 1143 */           this.theVoidType = new VoidTypeImpl((VirtualMachine)this);
/*      */         }
/*      */       } 
/*      */     }
/* 1147 */     return this.theVoidType;
/*      */   }
/*      */   
/*      */   PrimitiveType primitiveTypeMirror(byte paramByte) {
/* 1151 */     switch (paramByte) {
/*      */       case 90:
/* 1153 */         return (PrimitiveType)theBooleanType();
/*      */       case 66:
/* 1155 */         return (PrimitiveType)theByteType();
/*      */       case 67:
/* 1157 */         return (PrimitiveType)theCharType();
/*      */       case 83:
/* 1159 */         return (PrimitiveType)theShortType();
/*      */       case 73:
/* 1161 */         return (PrimitiveType)theIntegerType();
/*      */       case 74:
/* 1163 */         return (PrimitiveType)theLongType();
/*      */       case 70:
/* 1165 */         return (PrimitiveType)theFloatType();
/*      */       case 68:
/* 1167 */         return (PrimitiveType)theDoubleType();
/*      */     } 
/* 1169 */     throw new IllegalArgumentException("Unrecognized primitive tag " + paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   private void processBatchedDisposes() {
/* 1174 */     if (this.shutdown) {
/*      */       return;
/*      */     }
/*      */     
/* 1178 */     JDWP.VirtualMachine.DisposeObjects.Request[] arrayOfRequest = null;
/* 1179 */     synchronized (this.batchedDisposeRequests) {
/* 1180 */       int i = this.batchedDisposeRequests.size();
/* 1181 */       if (i >= 50) {
/* 1182 */         if ((this.traceFlags & 0x10) != 0) {
/* 1183 */           printTrace("Dispose threashold reached. Will dispose " + i + " object references...");
/*      */         }
/*      */         
/* 1186 */         arrayOfRequest = new JDWP.VirtualMachine.DisposeObjects.Request[i];
/* 1187 */         for (byte b = 0; b < arrayOfRequest.length; b++) {
/* 1188 */           SoftObjectReference softObjectReference = this.batchedDisposeRequests.get(b);
/* 1189 */           if ((this.traceFlags & 0x10) != 0) {
/* 1190 */             printTrace("Disposing object " + softObjectReference.key().longValue() + " (ref count = " + softObjectReference
/* 1191 */                 .count() + ")");
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1197 */           arrayOfRequest[b] = new JDWP.VirtualMachine.DisposeObjects.Request(new ObjectReferenceImpl((VirtualMachine)this, softObjectReference
/*      */                 
/* 1199 */                 .key().longValue()), softObjectReference
/* 1200 */               .count());
/*      */         } 
/* 1202 */         this.batchedDisposeRequests.clear();
/*      */       } 
/*      */     } 
/* 1205 */     if (arrayOfRequest != null) {
/*      */       try {
/* 1207 */         JDWP.VirtualMachine.DisposeObjects.process(this.vm, arrayOfRequest);
/* 1208 */       } catch (JDWPException jDWPException) {
/* 1209 */         throw jDWPException.toJDIException();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void batchForDispose(SoftObjectReference paramSoftObjectReference) {
/* 1215 */     if ((this.traceFlags & 0x10) != 0) {
/* 1216 */       printTrace("Batching object " + paramSoftObjectReference.key().longValue() + " for dispose (ref count = " + paramSoftObjectReference
/* 1217 */           .count() + ")");
/*      */     }
/* 1219 */     this.batchedDisposeRequests.add(paramSoftObjectReference);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processQueue() {
/*      */     Reference<? extends ObjectReferenceImpl> reference;
/* 1227 */     while ((reference = this.referenceQueue.poll()) != null) {
/* 1228 */       SoftObjectReference softObjectReference = (SoftObjectReference)reference;
/* 1229 */       removeObjectMirror(softObjectReference);
/* 1230 */       batchForDispose(softObjectReference);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized ObjectReferenceImpl objectMirror(long paramLong, int paramInt) {
/* 1237 */     processQueue();
/*      */     
/* 1239 */     if (paramLong == 0L) {
/* 1240 */       return null;
/*      */     }
/* 1242 */     ObjectReferenceImpl objectReferenceImpl = null;
/* 1243 */     Long long_ = new Long(paramLong);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1248 */     SoftObjectReference softObjectReference = this.objectsByID.get(long_);
/* 1249 */     if (softObjectReference != null) {
/* 1250 */       objectReferenceImpl = softObjectReference.object();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1257 */     if (objectReferenceImpl == null) {
/* 1258 */       ThreadReferenceImpl threadReferenceImpl; switch (paramInt) {
/*      */         case 76:
/* 1260 */           objectReferenceImpl = new ObjectReferenceImpl((VirtualMachine)this.vm, paramLong);
/*      */           break;
/*      */         case 115:
/* 1263 */           objectReferenceImpl = new StringReferenceImpl((VirtualMachine)this.vm, paramLong);
/*      */           break;
/*      */         case 91:
/* 1266 */           objectReferenceImpl = new ArrayReferenceImpl((VirtualMachine)this.vm, paramLong);
/*      */           break;
/*      */         case 116:
/* 1269 */           threadReferenceImpl = new ThreadReferenceImpl((VirtualMachine)this.vm, paramLong);
/*      */           
/* 1271 */           threadReferenceImpl.addListener(this);
/* 1272 */           objectReferenceImpl = threadReferenceImpl;
/*      */           break;
/*      */         case 103:
/* 1275 */           objectReferenceImpl = new ThreadGroupReferenceImpl((VirtualMachine)this.vm, paramLong);
/*      */           break;
/*      */         case 108:
/* 1278 */           objectReferenceImpl = new ClassLoaderReferenceImpl((VirtualMachine)this.vm, paramLong);
/*      */           break;
/*      */         case 99:
/* 1281 */           objectReferenceImpl = new ClassObjectReferenceImpl((VirtualMachine)this.vm, paramLong);
/*      */           break;
/*      */         default:
/* 1284 */           throw new IllegalArgumentException("Invalid object tag: " + paramInt);
/*      */       } 
/* 1286 */       softObjectReference = new SoftObjectReference(long_, objectReferenceImpl, this.referenceQueue);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1292 */       this.objectsByID.put(long_, softObjectReference);
/* 1293 */       if ((this.traceFlags & 0x10) != 0) {
/* 1294 */         printTrace("Creating new " + objectReferenceImpl
/* 1295 */             .getClass().getName() + " (id = " + paramLong + ")");
/*      */       }
/*      */     } else {
/* 1298 */       softObjectReference.incrementCount();
/*      */     } 
/*      */     
/* 1301 */     return objectReferenceImpl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void removeObjectMirror(ObjectReferenceImpl paramObjectReferenceImpl) {
/* 1307 */     processQueue();
/*      */     
/* 1309 */     SoftObjectReference softObjectReference = this.objectsByID.remove(new Long(paramObjectReferenceImpl.ref()));
/* 1310 */     if (softObjectReference != null) {
/* 1311 */       batchForDispose(softObjectReference);
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1317 */       throw new InternalException("ObjectReference " + paramObjectReferenceImpl.ref() + " not found in object cache");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void removeObjectMirror(SoftObjectReference paramSoftObjectReference) {
/* 1327 */     this.objectsByID.remove(paramSoftObjectReference.key());
/*      */   }
/*      */   
/*      */   ObjectReferenceImpl objectMirror(long paramLong) {
/* 1331 */     return objectMirror(paramLong, 76);
/*      */   }
/*      */   
/*      */   StringReferenceImpl stringMirror(long paramLong) {
/* 1335 */     return (StringReferenceImpl)objectMirror(paramLong, 115);
/*      */   }
/*      */   
/*      */   ArrayReferenceImpl arrayMirror(long paramLong) {
/* 1339 */     return (ArrayReferenceImpl)objectMirror(paramLong, 91);
/*      */   }
/*      */   
/*      */   ThreadReferenceImpl threadMirror(long paramLong) {
/* 1343 */     return (ThreadReferenceImpl)objectMirror(paramLong, 116);
/*      */   }
/*      */   
/*      */   ThreadGroupReferenceImpl threadGroupMirror(long paramLong) {
/* 1347 */     return (ThreadGroupReferenceImpl)objectMirror(paramLong, 103);
/*      */   }
/*      */ 
/*      */   
/*      */   ClassLoaderReferenceImpl classLoaderMirror(long paramLong) {
/* 1352 */     return (ClassLoaderReferenceImpl)objectMirror(paramLong, 108);
/*      */   }
/*      */ 
/*      */   
/*      */   ClassObjectReferenceImpl classObjectMirror(long paramLong) {
/* 1357 */     return (ClassObjectReferenceImpl)objectMirror(paramLong, 99);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private JDWP.VirtualMachine.ClassPaths getClasspath() {
/* 1365 */     if (this.pathInfo == null) {
/*      */       try {
/* 1367 */         this.pathInfo = JDWP.VirtualMachine.ClassPaths.process(this.vm);
/* 1368 */       } catch (JDWPException jDWPException) {
/* 1369 */         throw jDWPException.toJDIException();
/*      */       } 
/*      */     }
/* 1372 */     return this.pathInfo;
/*      */   }
/*      */   
/*      */   public List<String> classPath() {
/* 1376 */     return Arrays.asList((getClasspath()).classpaths);
/*      */   }
/*      */   
/*      */   public List<String> bootClassPath() {
/* 1380 */     return Arrays.asList((getClasspath()).bootclasspaths);
/*      */   }
/*      */   
/*      */   public String baseDirectory() {
/* 1384 */     return (getClasspath()).baseDir;
/*      */   }
/*      */   
/*      */   public void setDefaultStratum(String paramString) {
/* 1388 */     this.defaultStratum = paramString;
/* 1389 */     if (paramString == null) {
/* 1390 */       paramString = "";
/*      */     }
/*      */     try {
/* 1393 */       JDWP.VirtualMachine.SetDefaultStratum.process(this.vm, paramString);
/*      */     }
/* 1395 */     catch (JDWPException jDWPException) {
/* 1396 */       throw jDWPException.toJDIException();
/*      */     } 
/*      */   }
/*      */   
/*      */   public String getDefaultStratum() {
/* 1401 */     return this.defaultStratum;
/*      */   }
/*      */   
/*      */   ThreadGroup threadGroupForJDI() {
/* 1405 */     return this.threadGroupForJDI;
/*      */   }
/*      */   
/*      */   private static class SoftObjectReference
/*      */     extends SoftReference<ObjectReferenceImpl> {
/*      */     int count;
/*      */     Long key;
/*      */     
/*      */     SoftObjectReference(Long param1Long, ObjectReferenceImpl param1ObjectReferenceImpl, ReferenceQueue<ObjectReferenceImpl> param1ReferenceQueue) {
/* 1414 */       super(param1ObjectReferenceImpl, param1ReferenceQueue);
/* 1415 */       this.count = 1;
/* 1416 */       this.key = param1Long;
/*      */     }
/*      */     
/*      */     int count() {
/* 1420 */       return this.count;
/*      */     }
/*      */     
/*      */     void incrementCount() {
/* 1424 */       this.count++;
/*      */     }
/*      */     
/*      */     Long key() {
/* 1428 */       return this.key;
/*      */     }
/*      */     
/*      */     ObjectReferenceImpl object() {
/* 1432 */       return get();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\VirtualMachineImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */