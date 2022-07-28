/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.ClassType;
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.InvocationException;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectReferenceImpl
/*     */   extends ValueImpl
/*     */   implements ObjectReference, VMListener
/*     */ {
/*     */   protected long ref;
/*  37 */   private ReferenceType type = null;
/*  38 */   private int gcDisableCount = 0;
/*     */   boolean addedListener = false;
/*     */   
/*     */   protected static class Cache
/*     */   {
/*  43 */     JDWP.ObjectReference.MonitorInfo monitorInfo = null;
/*     */   }
/*     */   
/*  46 */   private static final Cache noInitCache = new Cache();
/*  47 */   private static final Cache markerCache = new Cache();
/*  48 */   private Cache cache = noInitCache;
/*     */   
/*     */   private void disableCache() {
/*  51 */     synchronized (this.vm.state()) {
/*  52 */       this.cache = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enableCache() {
/*  57 */     synchronized (this.vm.state()) {
/*  58 */       this.cache = markerCache;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Cache newCache() {
/*  64 */     return new Cache();
/*     */   }
/*     */   
/*     */   protected Cache getCache() {
/*  68 */     synchronized (this.vm.state()) {
/*  69 */       if (this.cache == noInitCache) {
/*  70 */         if (this.vm.state().isSuspended()) {
/*     */ 
/*     */           
/*  73 */           enableCache();
/*     */         } else {
/*  75 */           disableCache();
/*     */         } 
/*     */       }
/*  78 */       if (this.cache == markerCache) {
/*  79 */         this.cache = newCache();
/*     */       }
/*  81 */       return this.cache;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassTypeImpl invokableReferenceType(Method paramMethod) {
/*  89 */     return (ClassTypeImpl)referenceType();
/*     */   }
/*     */   
/*     */   ObjectReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  93 */     super(paramVirtualMachine);
/*     */     
/*  95 */     this.ref = paramLong;
/*     */   }
/*     */   
/*     */   protected String description() {
/*  99 */     return "ObjectReference " + uniqueID();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean vmSuspended(VMAction paramVMAction) {
/* 106 */     enableCache();
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean vmNotSuspended(VMAction paramVMAction) {
/* 112 */     synchronized (this.vm.state()) {
/* 113 */       if (this.cache != null && (this.vm.traceFlags & 0x10) != 0) {
/* 114 */         this.vm.printTrace("Clearing temporary cache for " + description());
/*     */       }
/* 116 */       disableCache();
/* 117 */       if (this.addedListener) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 123 */         this.addedListener = false;
/* 124 */         return false;
/*     */       } 
/* 126 */       return true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 132 */     if (paramObject != null && paramObject instanceof ObjectReferenceImpl) {
/* 133 */       ObjectReferenceImpl objectReferenceImpl = (ObjectReferenceImpl)paramObject;
/* 134 */       return (ref() == objectReferenceImpl.ref() && super
/* 135 */         .equals(paramObject));
/*     */     } 
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     return (int)ref();
/*     */   }
/*     */   
/*     */   public Type type() {
/* 146 */     return (Type)referenceType();
/*     */   }
/*     */   
/*     */   public ReferenceType referenceType() {
/* 150 */     if (this.type == null) {
/*     */       
/*     */       try {
/* 153 */         JDWP.ObjectReference.ReferenceType referenceType = JDWP.ObjectReference.ReferenceType.process(this.vm, this);
/* 154 */         this.type = this.vm.referenceType(referenceType.typeID, referenceType.refTypeTag);
/*     */       }
/* 156 */       catch (JDWPException jDWPException) {
/* 157 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     }
/* 160 */     return this.type;
/*     */   }
/*     */   
/*     */   public Value getValue(Field paramField) {
/* 164 */     ArrayList<Field> arrayList = new ArrayList(1);
/* 165 */     arrayList.add(paramField);
/* 166 */     Map<Field, Value> map = getValues(arrayList);
/* 167 */     return map.get(paramField);
/*     */   } public Map<Field, Value> getValues(List<? extends Field> paramList) {
/*     */     Map<FieldImpl, ValueImpl> map;
/*     */     ValueImpl[] arrayOfValueImpl;
/* 171 */     validateMirrors((Collection)paramList);
/*     */     
/* 173 */     ArrayList<Field> arrayList1 = new ArrayList(0);
/* 174 */     int i = paramList.size();
/* 175 */     ArrayList<Field> arrayList2 = new ArrayList(i);
/*     */     
/* 177 */     for (byte b1 = 0; b1 < i; b1++) {
/* 178 */       Field field = paramList.get(b1);
/*     */ 
/*     */       
/* 181 */       ((ReferenceTypeImpl)referenceType()).validateFieldAccess(field);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       if (field.isStatic()) {
/* 187 */         arrayList1.add(field);
/*     */       } else {
/* 189 */         arrayList2.add(field);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 194 */     if (arrayList1.size() > 0) {
/* 195 */       map = referenceType().getValues(arrayList1);
/*     */     } else {
/* 197 */       map = (Map)new HashMap<>(i);
/*     */     } 
/*     */     
/* 200 */     i = arrayList2.size();
/*     */     
/* 202 */     JDWP.ObjectReference.GetValues.Field[] arrayOfField = new JDWP.ObjectReference.GetValues.Field[i];
/*     */     
/* 204 */     for (byte b2 = 0; b2 < i; b2++) {
/* 205 */       FieldImpl fieldImpl = (FieldImpl)arrayList2.get(b2);
/* 206 */       arrayOfField[b2] = new JDWP.ObjectReference.GetValues.Field(fieldImpl
/* 207 */           .ref());
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 212 */       arrayOfValueImpl = (JDWP.ObjectReference.GetValues.process(this.vm, this, arrayOfField)).values;
/* 213 */     } catch (JDWPException jDWPException) {
/* 214 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 217 */     if (i != arrayOfValueImpl.length) {
/* 218 */       throw new InternalException("Wrong number of values returned from target VM");
/*     */     }
/*     */     
/* 221 */     for (byte b3 = 0; b3 < i; b3++) {
/* 222 */       FieldImpl fieldImpl = (FieldImpl)arrayList2.get(b3);
/* 223 */       map.put(fieldImpl, arrayOfValueImpl[b3]);
/*     */     } 
/*     */     
/* 226 */     return (Map)map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Field paramField, Value paramValue) throws InvalidTypeException, ClassNotLoadedException {
/* 232 */     validateMirror((Mirror)paramField);
/* 233 */     validateMirrorOrNull((Mirror)paramValue);
/*     */ 
/*     */     
/* 236 */     ((ReferenceTypeImpl)referenceType()).validateFieldSet(paramField);
/*     */     
/* 238 */     if (paramField.isStatic()) {
/* 239 */       ReferenceType referenceType = referenceType();
/* 240 */       if (referenceType instanceof ClassType) {
/* 241 */         ((ClassType)referenceType).setValue(paramField, paramValue);
/*     */         return;
/*     */       } 
/* 244 */       throw new IllegalArgumentException("Invalid type for static field set");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 250 */       JDWP.ObjectReference.SetValues.FieldValue[] arrayOfFieldValue = new JDWP.ObjectReference.SetValues.FieldValue[1];
/*     */       
/* 252 */       arrayOfFieldValue[0] = new JDWP.ObjectReference.SetValues.FieldValue(((FieldImpl)paramField)
/* 253 */           .ref(), 
/*     */           
/* 255 */           ValueImpl.prepareForAssignment(paramValue, (FieldImpl)paramField));
/*     */       
/*     */       try {
/* 258 */         JDWP.ObjectReference.SetValues.process(this.vm, this, arrayOfFieldValue);
/* 259 */       } catch (JDWPException jDWPException) {
/* 260 */         throw jDWPException.toJDIException();
/*     */       } 
/* 262 */     } catch (ClassNotLoadedException classNotLoadedException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 271 */       if (paramValue != null) {
/* 272 */         throw classNotLoadedException;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateMethodInvocation(Method paramMethod, int paramInt) throws InvalidTypeException, InvocationException {
/* 284 */     ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)paramMethod.declaringType();
/* 285 */     if (!referenceTypeImpl.isAssignableFrom(this)) {
/* 286 */       throw new IllegalArgumentException("Invalid method");
/*     */     }
/*     */     
/* 289 */     if (referenceTypeImpl instanceof ClassTypeImpl) {
/* 290 */       validateClassMethodInvocation(paramMethod, paramInt);
/* 291 */     } else if (referenceTypeImpl instanceof InterfaceTypeImpl) {
/* 292 */       validateIfaceMethodInvocation(paramMethod, paramInt);
/*     */     } else {
/* 294 */       throw new InvalidTypeException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateClassMethodInvocation(Method paramMethod, int paramInt) throws InvalidTypeException, InvocationException {
/* 302 */     ClassTypeImpl classTypeImpl = invokableReferenceType(paramMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     if (paramMethod.isConstructor()) {
/* 308 */       throw new IllegalArgumentException("Cannot invoke constructor");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 314 */     if (isNonVirtual(paramInt) && 
/* 315 */       paramMethod.isAbstract()) {
/* 316 */       throw new IllegalArgumentException("Abstract method");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 326 */     if (isNonVirtual(paramInt)) {
/*     */       
/* 328 */       ClassTypeImpl classTypeImpl1 = classTypeImpl;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 335 */       Method method = classTypeImpl.concreteMethodByName(paramMethod.name(), paramMethod
/* 336 */           .signature());
/*     */       
/* 338 */       ClassTypeImpl classTypeImpl1 = (ClassTypeImpl)method.declaringType();
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
/*     */   void validateIfaceMethodInvocation(Method paramMethod, int paramInt) throws InvalidTypeException, InvocationException {
/* 351 */     if (isNonVirtual(paramInt) && !paramMethod.isDefault()) {
/* 352 */       throw new IllegalArgumentException("Not a default method");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PacketStream sendInvokeCommand(final ThreadReferenceImpl thread, final ClassTypeImpl refType, final MethodImpl method, final ValueImpl[] args, final int options) {
/*     */     PacketStream packetStream;
/* 361 */     CommandSender commandSender = new CommandSender()
/*     */       {
/*     */         public PacketStream send() {
/* 364 */           return JDWP.ObjectReference.InvokeMethod.enqueueCommand(ObjectReferenceImpl.this.vm, ObjectReferenceImpl.this, thread, refType, method
/*     */ 
/*     */               
/* 367 */               .ref(), args, options);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 372 */     if ((options & 0x1) != 0) {
/* 373 */       packetStream = thread.sendResumingCommand(commandSender);
/*     */     } else {
/* 375 */       packetStream = this.vm.sendResumingCommand(commandSender);
/*     */     } 
/* 377 */     return packetStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt) throws InvalidTypeException, IncompatibleThreadStateException, InvocationException, ClassNotLoadedException {
/*     */     JDWP.ObjectReference.InvokeMethod invokeMethod;
/* 386 */     validateMirror((Mirror)paramThreadReference);
/* 387 */     validateMirror((Mirror)paramMethod);
/* 388 */     validateMirrorsOrNulls((Collection)paramList);
/*     */     
/* 390 */     MethodImpl methodImpl = (MethodImpl)paramMethod;
/* 391 */     ThreadReferenceImpl threadReferenceImpl = (ThreadReferenceImpl)paramThreadReference;
/*     */     
/* 393 */     if (methodImpl.isStatic()) {
/* 394 */       if (referenceType() instanceof InterfaceType) {
/* 395 */         InterfaceType interfaceType = (InterfaceType)referenceType();
/* 396 */         return interfaceType.invokeMethod(threadReferenceImpl, methodImpl, paramList, paramInt);
/* 397 */       }  if (referenceType() instanceof ClassType) {
/* 398 */         ClassType classType = (ClassType)referenceType();
/* 399 */         return classType.invokeMethod(threadReferenceImpl, methodImpl, paramList, paramInt);
/*     */       } 
/* 401 */       throw new IllegalArgumentException("Invalid type for static method invocation");
/*     */     } 
/*     */ 
/*     */     
/* 405 */     validateMethodInvocation(methodImpl, paramInt);
/*     */     
/* 407 */     List<Value> list = methodImpl.validateAndPrepareArgumentsForInvoke(paramList);
/*     */ 
/*     */     
/* 410 */     ValueImpl[] arrayOfValueImpl = list.<ValueImpl>toArray(new ValueImpl[0]);
/*     */ 
/*     */     
/*     */     try {
/* 414 */       PacketStream packetStream = sendInvokeCommand(threadReferenceImpl, invokableReferenceType(methodImpl), methodImpl, arrayOfValueImpl, paramInt);
/*     */       
/* 416 */       invokeMethod = JDWP.ObjectReference.InvokeMethod.waitForReply(this.vm, packetStream);
/* 417 */     } catch (JDWPException jDWPException) {
/* 418 */       if (jDWPException.errorCode() == 10) {
/* 419 */         throw new IncompatibleThreadStateException();
/*     */       }
/* 421 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 429 */     if ((paramInt & 0x1) == 0) {
/* 430 */       this.vm.notifySuspend();
/*     */     }
/*     */     
/* 433 */     if (invokeMethod.exception != null) {
/* 434 */       throw new InvocationException(invokeMethod.exception);
/*     */     }
/* 436 */     return invokeMethod.returnValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void disableCollection() {
/* 442 */     if (this.gcDisableCount == 0) {
/*     */       try {
/* 444 */         JDWP.ObjectReference.DisableCollection.process(this.vm, this);
/* 445 */       } catch (JDWPException jDWPException) {
/* 446 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     }
/* 449 */     this.gcDisableCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void enableCollection() {
/* 454 */     this.gcDisableCount--;
/*     */     
/* 456 */     if (this.gcDisableCount == 0) {
/*     */       try {
/* 458 */         JDWP.ObjectReference.EnableCollection.process(this.vm, this);
/* 459 */       } catch (JDWPException jDWPException) {
/*     */         
/* 461 */         if (jDWPException.errorCode() != 20) {
/* 462 */           throw jDWPException.toJDIException();
/*     */         }
/*     */         return;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isCollected() {
/*     */     try {
/* 471 */       return (JDWP.ObjectReference.IsCollected.process(this.vm, this)).isCollected;
/*     */     }
/* 473 */     catch (JDWPException jDWPException) {
/* 474 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public long uniqueID() {
/* 479 */     return ref();
/*     */   }
/*     */ 
/*     */   
/*     */   JDWP.ObjectReference.MonitorInfo jdwpMonitorInfo() throws IncompatibleThreadStateException {
/* 484 */     JDWP.ObjectReference.MonitorInfo monitorInfo = null;
/*     */ 
/*     */     
/*     */     try {
/*     */       Cache cache;
/*     */       
/* 490 */       synchronized (this.vm.state()) {
/* 491 */         cache = getCache();
/*     */         
/* 493 */         if (cache != null) {
/* 494 */           monitorInfo = cache.monitorInfo;
/*     */ 
/*     */ 
/*     */           
/* 498 */           if (monitorInfo == null && !this.vm.state().hasListener(this)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 505 */             this.vm.state().addListener(this);
/* 506 */             this.addedListener = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 510 */       if (monitorInfo == null) {
/* 511 */         monitorInfo = JDWP.ObjectReference.MonitorInfo.process(this.vm, this);
/* 512 */         if (cache != null) {
/* 513 */           cache.monitorInfo = monitorInfo;
/* 514 */           if ((this.vm.traceFlags & 0x10) != 0) {
/* 515 */             this.vm.printTrace("ObjectReference " + uniqueID() + " temporarily caching monitor info");
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 520 */     } catch (JDWPException jDWPException) {
/* 521 */       if (jDWPException.errorCode() == 13) {
/* 522 */         throw new IncompatibleThreadStateException();
/*     */       }
/* 524 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 527 */     return monitorInfo;
/*     */   }
/*     */   
/*     */   public List<ThreadReference> waitingThreads() throws IncompatibleThreadStateException {
/* 531 */     return Arrays.asList((ThreadReference[])(jdwpMonitorInfo()).waiters);
/*     */   }
/*     */   
/*     */   public ThreadReference owningThread() throws IncompatibleThreadStateException {
/* 535 */     return (jdwpMonitorInfo()).owner;
/*     */   }
/*     */   
/*     */   public int entryCount() throws IncompatibleThreadStateException {
/* 539 */     return (jdwpMonitorInfo()).entryCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ObjectReference> referringObjects(long paramLong) {
/* 544 */     if (!this.vm.canGetInstanceInfo()) {
/* 545 */       throw new UnsupportedOperationException("target does not support getting referring objects");
/*     */     }
/*     */ 
/*     */     
/* 549 */     if (paramLong < 0L) {
/* 550 */       throw new IllegalArgumentException("maxReferrers is less than zero: " + paramLong);
/*     */     }
/*     */ 
/*     */     
/* 554 */     int i = (paramLong > 2147483647L) ? Integer.MAX_VALUE : (int)paramLong;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 559 */       return Arrays.asList(
/* 560 */           (ObjectReference[])(JDWP.ObjectReference.ReferringObjects.process(this.vm, this, i)).referringObjects);
/* 561 */     } catch (JDWPException jDWPException) {
/* 562 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */   
/*     */   long ref() {
/* 567 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isClassObject() {
/* 574 */     return referenceType().name().equals("java.lang.Class");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ValueImpl prepareForAssignmentTo(ValueContainer paramValueContainer) throws InvalidTypeException, ClassNotLoadedException {
/* 581 */     validateAssignment(paramValueContainer);
/* 582 */     return this;
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
/*     */   void validateAssignment(ValueContainer paramValueContainer) throws InvalidTypeException, ClassNotLoadedException {
/* 596 */     if (paramValueContainer.signature().length() == 1) {
/* 597 */       throw new InvalidTypeException("Can't assign object value to primitive");
/*     */     }
/* 599 */     if (paramValueContainer.signature().charAt(0) == '[' && 
/* 600 */       type().signature().charAt(0) != '[') {
/* 601 */       throw new InvalidTypeException("Can't assign non-array value to an array");
/*     */     }
/* 603 */     if ("void".equals(paramValueContainer.typeName())) {
/* 604 */       throw new InvalidTypeException("Can't assign object value to a void");
/*     */     }
/*     */ 
/*     */     
/* 608 */     ReferenceTypeImpl referenceTypeImpl1 = (ReferenceTypeImpl)paramValueContainer.type();
/* 609 */     ReferenceTypeImpl referenceTypeImpl2 = (ReferenceTypeImpl)referenceType();
/* 610 */     if (!referenceTypeImpl2.isAssignableTo(referenceTypeImpl1)) {
/* 611 */       JNITypeParser jNITypeParser = new JNITypeParser(referenceTypeImpl1.signature());
/* 612 */       String str = jNITypeParser.typeName();
/* 613 */       throw new InvalidTypeException("Can't assign " + 
/* 614 */           type().name() + " to " + str);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 621 */     return "instance of " + referenceType().name() + "(id=" + uniqueID() + ")";
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 625 */     return 76;
/*     */   }
/*     */   
/*     */   private static boolean isNonVirtual(int paramInt) {
/* 629 */     return ((paramInt & 0x2) != 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ObjectReferenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */