/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.ClassType;
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.InvocationException;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public final class ClassTypeImpl
/*     */   extends InvokableTypeImpl
/*     */   implements ClassType
/*     */ {
/*     */   private static class IResult
/*     */     implements InvocationResult
/*     */   {
/*     */     private final JDWP.ClassType.InvokeMethod rslt;
/*     */
/*     */     public IResult(JDWP.ClassType.InvokeMethod param1InvokeMethod) {
/*  39 */       this.rslt = param1InvokeMethod;
/*     */     }
/*     */
/*     */
/*     */     public ObjectReferenceImpl getException() {
/*  44 */       return this.rslt.exception;
/*     */     }
/*     */
/*     */
/*     */     public ValueImpl getResult() {
/*  49 */       return this.rslt.returnValue;
/*     */     }
/*     */   }
/*     */
/*     */   private boolean cachedSuperclass = false;
/*  54 */   private ClassType superclass = null;
/*  55 */   private int lastLine = -1;
/*  56 */   private List<InterfaceType> interfaces = null;
/*     */
/*     */   protected ClassTypeImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  59 */     super(paramVirtualMachine, paramLong);
/*     */   }
/*     */
/*     */   public ClassType superclass() {
/*  63 */     if (!this.cachedSuperclass) {
/*  64 */       ClassTypeImpl classTypeImpl = null;
/*     */
/*     */       try {
/*  67 */         classTypeImpl = (JDWP.ClassType.Superclass.process(this.vm, this)).superclass;
/*  68 */       } catch (JDWPException jDWPException) {
/*  69 */         throw jDWPException.toJDIException();
/*     */       }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  77 */       if (classTypeImpl != null) {
/*  78 */         this.superclass = classTypeImpl;
/*     */       }
/*  80 */       this.cachedSuperclass = true;
/*     */     }
/*     */
/*  83 */     return this.superclass;
/*     */   }
/*     */
/*     */
/*     */   public List<InterfaceType> interfaces() {
/*  88 */     if (this.interfaces == null) {
/*  89 */       this.interfaces = getInterfaces();
/*     */     }
/*  91 */     return this.interfaces;
/*     */   }
/*     */
/*     */
/*     */   public List<InterfaceType> allInterfaces() {
/*  96 */     return getAllInterfaces();
/*     */   }
/*     */
/*     */   public List<ClassType> subclasses() {
/* 100 */     ArrayList<ClassType> arrayList = new ArrayList();
/* 101 */     for (ReferenceType referenceType : this.vm.allClasses()) {
/* 102 */       if (referenceType instanceof ClassType) {
/* 103 */         ClassType classType1 = (ClassType)referenceType;
/* 104 */         ClassType classType2 = classType1.superclass();
/* 105 */         if (classType2 != null && classType2.equals(this)) {
/* 106 */           arrayList.add((ClassType)referenceType);
/*     */         }
/*     */       }
/*     */     }
/*     */
/* 111 */     return arrayList;
/*     */   }
/*     */
/*     */   public boolean isEnum() {
/* 115 */     ClassType classType = superclass();
/* 116 */     if (classType != null && classType
/* 117 */       .name().equals("java.lang.Enum")) {
/* 118 */       return true;
/*     */     }
/* 120 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void setValue(Field paramField, Value paramValue) throws InvalidTypeException, ClassNotLoadedException {
/* 126 */     validateMirror((Mirror)paramField);
/* 127 */     validateMirrorOrNull((Mirror)paramValue);
/* 128 */     validateFieldSet(paramField);
/*     */
/*     */
/* 131 */     if (!paramField.isStatic()) {
/* 132 */       throw new IllegalArgumentException("Must set non-static field through an instance");
/*     */     }
/*     */
/*     */
/*     */     try {
/* 137 */       JDWP.ClassType.SetValues.FieldValue[] arrayOfFieldValue = new JDWP.ClassType.SetValues.FieldValue[1];
/*     */
/* 139 */       arrayOfFieldValue[0] = new JDWP.ClassType.SetValues.FieldValue(((FieldImpl)paramField)
/* 140 */           .ref(),
/*     */
/* 142 */           ValueImpl.prepareForAssignment(paramValue, (FieldImpl)paramField));
/*     */
/*     */       try {
/* 145 */         JDWP.ClassType.SetValues.process(this.vm, this, arrayOfFieldValue);
/* 146 */       } catch (JDWPException jDWPException) {
/* 147 */         throw jDWPException.toJDIException();
/*     */       }
/* 149 */     } catch (ClassNotLoadedException classNotLoadedException) {
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 158 */       if (paramValue != null) {
/* 159 */         throw classNotLoadedException;
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   PacketStream sendNewInstanceCommand(final ThreadReferenceImpl thread, final MethodImpl method, final ValueImpl[] args, final int options) {
/*     */     PacketStream packetStream;
/* 168 */     CommandSender commandSender = new CommandSender()
/*     */       {
/*     */         public PacketStream send() {
/* 171 */           return JDWP.ClassType.NewInstance.enqueueCommand(ClassTypeImpl.this.vm, ClassTypeImpl.this, thread, method
/*     */
/* 173 */               .ref(), args, options);
/*     */         }
/*     */       };
/*     */
/*     */
/* 178 */     if ((options & 0x1) != 0) {
/* 179 */       packetStream = thread.sendResumingCommand(commandSender);
/*     */     } else {
/* 181 */       packetStream = this.vm.sendResumingCommand(commandSender);
/*     */     }
/* 183 */     return packetStream;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ObjectReference newInstance(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
/* 194 */     validateMirror((Mirror)paramThreadReference);
/* 195 */     validateMirror((Mirror)paramMethod);
/* 196 */     validateMirrorsOrNulls((Collection)paramList);
/*     */
/* 198 */     MethodImpl methodImpl = (MethodImpl)paramMethod;
/* 199 */     ThreadReferenceImpl threadReferenceImpl = (ThreadReferenceImpl)paramThreadReference;
/*     */
/* 201 */     validateConstructorInvocation(methodImpl);
/*     */
/* 203 */     List<Value> list = methodImpl.validateAndPrepareArgumentsForInvoke(paramList);
/*     */
/* 205 */     ValueImpl[] arrayOfValueImpl = list.<ValueImpl>toArray(new ValueImpl[0]);
/* 206 */     JDWP.ClassType.NewInstance newInstance = null;
/*     */
/*     */     try {
/* 209 */       PacketStream packetStream = sendNewInstanceCommand(threadReferenceImpl, methodImpl, arrayOfValueImpl, paramInt);
/* 210 */       newInstance = JDWP.ClassType.NewInstance.waitForReply(this.vm, packetStream);
/* 211 */     } catch (JDWPException jDWPException) {
/* 212 */       if (jDWPException.errorCode() == 10) {
/* 213 */         throw new IncompatibleThreadStateException();
/*     */       }
/* 215 */       throw jDWPException.toJDIException();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 223 */     if ((paramInt & 0x1) == 0) {
/* 224 */       this.vm.notifySuspend();
/*     */     }
/*     */
/* 227 */     if (newInstance.exception != null) {
/* 228 */       throw new InvocationException(newInstance.exception);
/*     */     }
/* 230 */     return newInstance.newObject;
/*     */   }
/*     */
/*     */
/*     */   public Method concreteMethodByName(String paramString1, String paramString2) {
/* 235 */     Method method = null;
/* 236 */     for (Method method1 : visibleMethods()) {
/* 237 */       if (method1.name().equals(paramString1) && method1
/* 238 */         .signature().equals(paramString2) &&
/* 239 */         !method1.isAbstract()) {
/*     */
/* 241 */         method = method1;
/*     */         break;
/*     */       }
/*     */     }
/* 245 */     return method;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void validateConstructorInvocation(Method paramMethod) throws InvalidTypeException, InvocationException {
/* 254 */     ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)paramMethod.declaringType();
/* 255 */     if (!referenceTypeImpl.equals(this)) {
/* 256 */       throw new IllegalArgumentException("Invalid constructor");
/*     */     }
/*     */
/*     */
/*     */
/*     */
/* 262 */     if (!paramMethod.isConstructor()) {
/* 263 */       throw new IllegalArgumentException("Cannot create instance with non-constructor");
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/* 269 */     return "class " + name() + " (" + loaderString() + ")";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   CommandSender getInvokeMethodSender(ThreadReferenceImpl paramThreadReferenceImpl, MethodImpl paramMethodImpl, ValueImpl[] paramArrayOfValueImpl, int paramInt) {
/* 277 */     return () -> JDWP.ClassType.InvokeMethod.enqueueCommand(this.vm, this, paramThreadReferenceImpl, paramMethodImpl.ref(), paramArrayOfValueImpl, paramInt);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   InvocationResult waitForReply(PacketStream paramPacketStream) throws JDWPException {
/* 288 */     return new IResult(JDWP.ClassType.InvokeMethod.waitForReply(this.vm, paramPacketStream));
/*     */   }
/*     */
/*     */
/*     */
/*     */   boolean canInvoke(Method paramMethod) {
/* 294 */     return ((ReferenceTypeImpl)paramMethod.declaringType()).isAssignableFrom(this);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ClassTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
