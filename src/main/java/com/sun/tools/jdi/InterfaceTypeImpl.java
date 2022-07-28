/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.ClassType;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public final class InterfaceTypeImpl
/*     */   extends InvokableTypeImpl
/*     */   implements InterfaceType
/*     */ {
/*     */   private static class IResult
/*     */     implements InvocationResult
/*     */   {
/*     */     private final JDWP.InterfaceType.InvokeMethod rslt;
/*     */
/*     */     public IResult(JDWP.InterfaceType.InvokeMethod param1InvokeMethod) {
/*  43 */       this.rslt = param1InvokeMethod;
/*     */     }
/*     */
/*     */
/*     */     public ObjectReferenceImpl getException() {
/*  48 */       return this.rslt.exception;
/*     */     }
/*     */
/*     */
/*     */     public ValueImpl getResult() {
/*  53 */       return this.rslt.returnValue;
/*     */     }
/*     */   }
/*     */
/*     */
/*  58 */   private SoftReference<List<InterfaceType>> superinterfacesRef = null;
/*     */
/*     */   protected InterfaceTypeImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  61 */     super(paramVirtualMachine, paramLong);
/*     */   }
/*     */
/*     */
/*     */   public List<InterfaceType> superinterfaces() {
/*  66 */     List<InterfaceType> list = (this.superinterfacesRef == null) ? null : this.superinterfacesRef.get();
/*  67 */     if (list == null) {
/*  68 */       list = getInterfaces();
/*  69 */       list = Collections.unmodifiableList(list);
/*  70 */       this.superinterfacesRef = new SoftReference<>(list);
/*     */     }
/*  72 */     return list;
/*     */   }
/*     */
/*     */   public List<InterfaceType> subinterfaces() {
/*  76 */     ArrayList<InterfaceType> arrayList = new ArrayList();
/*  77 */     for (ReferenceType referenceType : this.vm.allClasses()) {
/*  78 */       if (referenceType instanceof InterfaceType) {
/*  79 */         InterfaceType interfaceType = (InterfaceType)referenceType;
/*  80 */         if (interfaceType.isPrepared() && interfaceType.superinterfaces().contains(this)) {
/*  81 */           arrayList.add(interfaceType);
/*     */         }
/*     */       }
/*     */     }
/*  85 */     return arrayList;
/*     */   }
/*     */
/*     */   public List<ClassType> implementors() {
/*  89 */     ArrayList<ClassType> arrayList = new ArrayList();
/*  90 */     for (ReferenceType referenceType : this.vm.allClasses()) {
/*  91 */       if (referenceType instanceof ClassType) {
/*  92 */         ClassType classType = (ClassType)referenceType;
/*  93 */         if (classType.isPrepared() && classType.interfaces().contains(this)) {
/*  94 */           arrayList.add(classType);
/*     */         }
/*     */       }
/*     */     }
/*  98 */     return arrayList;
/*     */   }
/*     */
/*     */   public boolean isInitialized() {
/* 102 */     return isPrepared();
/*     */   }
/*     */
/*     */   public String toString() {
/* 106 */     return "interface " + name() + " (" + loaderString() + ")";
/*     */   }
/*     */
/*     */
/*     */   InvocationResult waitForReply(PacketStream paramPacketStream) throws JDWPException {
/* 111 */     return new IResult(JDWP.InterfaceType.InvokeMethod.waitForReply(this.vm, paramPacketStream));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   CommandSender getInvokeMethodSender(ThreadReferenceImpl paramThreadReferenceImpl, MethodImpl paramMethodImpl, ValueImpl[] paramArrayOfValueImpl, int paramInt) {
/* 119 */     return () -> JDWP.InterfaceType.InvokeMethod.enqueueCommand(this.vm, this, paramThreadReferenceImpl, paramMethodImpl.ref(), paramArrayOfValueImpl, paramInt);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   ClassType superclass() {
/* 130 */     return null;
/*     */   }
/*     */
/*     */
/*     */   boolean isAssignableTo(ReferenceType paramReferenceType) {
/* 135 */     if (paramReferenceType.name().equals("java.lang.Object"))
/*     */     {
/* 137 */       return true;
/*     */     }
/* 139 */     return super.isAssignableTo(paramReferenceType);
/*     */   }
/*     */
/*     */
/*     */   List<InterfaceType> interfaces() {
/* 144 */     return superinterfaces();
/*     */   }
/*     */
/*     */
/*     */
/*     */   boolean canInvoke(Method paramMethod) {
/* 150 */     return equals(paramMethod.declaringType());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\InterfaceTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
