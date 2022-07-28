/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.ClassType;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.InvocationException;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class InvokableTypeImpl
/*     */   extends ReferenceTypeImpl
/*     */ {
/*     */   InvokableTypeImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  60 */     super(paramVirtualMachine, paramLong);
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
/*     */   public final Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
/*     */     InvocationResult invocationResult;
/* 102 */     validateMirror((Mirror)paramThreadReference);
/* 103 */     validateMirror((Mirror)paramMethod);
/* 104 */     validateMirrorsOrNulls((Collection)paramList);
/* 105 */     MethodImpl methodImpl = (MethodImpl)paramMethod;
/* 106 */     ThreadReferenceImpl threadReferenceImpl = (ThreadReferenceImpl)paramThreadReference;
/* 107 */     validateMethodInvocation(methodImpl);
/* 108 */     List<Value> list = methodImpl.validateAndPrepareArgumentsForInvoke(paramList);
/* 109 */     ValueImpl[] arrayOfValueImpl = list.<ValueImpl>toArray(new ValueImpl[0]);
/*     */     
/*     */     try {
/* 112 */       PacketStream packetStream = sendInvokeCommand(threadReferenceImpl, methodImpl, arrayOfValueImpl, paramInt);
/* 113 */       invocationResult = waitForReply(packetStream);
/* 114 */     } catch (JDWPException jDWPException) {
/* 115 */       if (jDWPException.errorCode() == 10) {
/* 116 */         throw new IncompatibleThreadStateException();
/*     */       }
/* 118 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     if ((paramInt & 0x1) == 0) {
/* 126 */       this.vm.notifySuspend();
/*     */     }
/* 128 */     if (invocationResult.getException() != null) {
/* 129 */       throw new InvocationException(invocationResult.getException());
/*     */     }
/* 131 */     return invocationResult.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isAssignableTo(ReferenceType paramReferenceType) {
/* 137 */     ClassTypeImpl classTypeImpl = (ClassTypeImpl)superclass();
/* 138 */     if (equals(paramReferenceType))
/* 139 */       return true; 
/* 140 */     if (classTypeImpl != null && classTypeImpl.isAssignableTo(paramReferenceType)) {
/* 141 */       return true;
/*     */     }
/* 143 */     List<InterfaceType> list = interfaces();
/* 144 */     Iterator<InterfaceType> iterator = list.iterator();
/* 145 */     while (iterator.hasNext()) {
/* 146 */       InterfaceTypeImpl interfaceTypeImpl = (InterfaceTypeImpl)iterator.next();
/* 147 */       if (interfaceTypeImpl.isAssignableTo(paramReferenceType)) {
/* 148 */         return true;
/*     */       }
/*     */     } 
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void addVisibleMethods(Map<String, Method> paramMap, Set<InterfaceType> paramSet) {
/* 162 */     Iterator<InterfaceType> iterator = interfaces().iterator();
/* 163 */     while (iterator.hasNext()) {
/* 164 */       InterfaceTypeImpl interfaceTypeImpl = (InterfaceTypeImpl)iterator.next();
/* 165 */       if (!paramSet.contains(interfaceTypeImpl)) {
/* 166 */         interfaceTypeImpl.addVisibleMethods(paramMap, paramSet);
/* 167 */         paramSet.add(interfaceTypeImpl);
/*     */       } 
/*     */     } 
/* 170 */     ClassTypeImpl classTypeImpl = (ClassTypeImpl)superclass();
/* 171 */     if (classTypeImpl != null) {
/* 172 */       classTypeImpl.addVisibleMethods(paramMap, paramSet);
/*     */     }
/* 174 */     addToMethodMap(paramMap, methods());
/*     */   }
/*     */   
/*     */   final void addInterfaces(List<InterfaceType> paramList) {
/* 178 */     List<InterfaceType> list = interfaces();
/* 179 */     paramList.addAll(interfaces());
/* 180 */     Iterator<InterfaceType> iterator = list.iterator();
/* 181 */     while (iterator.hasNext()) {
/* 182 */       InterfaceTypeImpl interfaceTypeImpl = (InterfaceTypeImpl)iterator.next();
/* 183 */       interfaceTypeImpl.addInterfaces(paramList);
/*     */     } 
/* 185 */     ClassTypeImpl classTypeImpl = (ClassTypeImpl)superclass();
/* 186 */     if (classTypeImpl != null) {
/* 187 */       classTypeImpl.addInterfaces(paramList);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final List<InterfaceType> getAllInterfaces() {
/* 196 */     ArrayList<InterfaceType> arrayList = new ArrayList();
/* 197 */     addInterfaces(arrayList);
/* 198 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<Method> allMethods() {
/* 207 */     ArrayList<Method> arrayList = new ArrayList<>(methods());
/* 208 */     ClassType classType = superclass();
/* 209 */     while (classType != null) {
/* 210 */       arrayList.addAll(classType.methods());
/* 211 */       classType = classType.superclass();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     for (InterfaceType interfaceType : getAllInterfaces()) {
/* 218 */       arrayList.addAll(interfaceType.methods());
/*     */     }
/* 220 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   final List<ReferenceType> inheritedTypes() {
/* 225 */     ArrayList<ClassType> arrayList = new ArrayList();
/* 226 */     if (superclass() != null) {
/* 227 */       arrayList.add(0, superclass());
/*     */     }
/* 229 */     for (ReferenceType referenceType : interfaces()) {
/* 230 */       arrayList.add(referenceType);
/*     */     }
/* 232 */     return (List)arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PacketStream sendInvokeCommand(ThreadReferenceImpl paramThreadReferenceImpl, MethodImpl paramMethodImpl, ValueImpl[] paramArrayOfValueImpl, int paramInt) {
/*     */     PacketStream packetStream;
/* 240 */     CommandSender commandSender = getInvokeMethodSender(paramThreadReferenceImpl, paramMethodImpl, paramArrayOfValueImpl, paramInt);
/*     */     
/* 242 */     if ((paramInt & 0x1) != 0) {
/* 243 */       packetStream = paramThreadReferenceImpl.sendResumingCommand(commandSender);
/*     */     } else {
/* 245 */       packetStream = this.vm.sendResumingCommand(commandSender);
/*     */     } 
/* 247 */     return packetStream;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateMethodInvocation(Method paramMethod) throws InvalidTypeException, InvocationException {
/* 253 */     if (!canInvoke(paramMethod)) {
/* 254 */       throw new IllegalArgumentException("Invalid method");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 259 */     if (!paramMethod.isStatic())
/* 260 */       throw new IllegalArgumentException("Cannot invoke instance method on a class/interface type"); 
/* 261 */     if (paramMethod.isStaticInitializer())
/* 262 */       throw new IllegalArgumentException("Cannot invoke static initializer"); 
/*     */   }
/*     */   
/*     */   abstract CommandSender getInvokeMethodSender(ThreadReferenceImpl paramThreadReferenceImpl, MethodImpl paramMethodImpl, ValueImpl[] paramArrayOfValueImpl, int paramInt);
/*     */   
/*     */   abstract InvocationResult waitForReply(PacketStream paramPacketStream) throws JDWPException;
/*     */   
/*     */   abstract ClassType superclass();
/*     */   
/*     */   abstract List<InterfaceType> interfaces();
/*     */   
/*     */   abstract boolean canInvoke(Method paramMethod);
/*     */   
/*     */   static interface InvocationResult {
/*     */     ObjectReferenceImpl getException();
/*     */     
/*     */     ValueImpl getResult();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\InvokableTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */