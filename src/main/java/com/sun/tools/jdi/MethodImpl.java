/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.ArrayReference;
/*     */ import com.sun.jdi.ArrayType;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MethodImpl
/*     */   extends TypeComponentImpl
/*     */   implements Method
/*     */ {
/*     */   private JNITypeParser signatureParser;
/*     */   ReturnContainer retValContainer;
/*     */   
/*     */   MethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt) {
/*  53 */     super(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     this.retValContainer = null; this.signatureParser = new JNITypeParser(paramString2);
/*     */   } static MethodImpl createMethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt) { if ((paramInt & 0x500) != 0) return new NonConcreteMethodImpl(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);  return new ConcreteMethodImpl(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt); } public boolean equals(Object paramObject) { if (paramObject != null && paramObject instanceof MethodImpl) { MethodImpl methodImpl = (MethodImpl)paramObject; return (declaringType().equals(methodImpl.declaringType()) && ref() == methodImpl.ref() && super.equals(paramObject)); }  return false; } public int hashCode() { return (int)ref(); } public final List<Location> allLineLocations() throws AbsentInformationException { return allLineLocations(this.vm.getDefaultStratum(), (String)null); } public List<Location> allLineLocations(String paramString1, String paramString2) throws AbsentInformationException { return allLineLocations(this.declaringType.stratum(paramString1), paramString2); } public final List<Location> locationsOfLine(int paramInt) throws AbsentInformationException { return locationsOfLine(this.vm.getDefaultStratum(), (String)null, paramInt); } public List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt) throws AbsentInformationException { return locationsOfLine(this.declaringType.stratum(paramString1), paramString2, paramInt); } LineInfo codeIndexToLineInfo(SDE.Stratum paramStratum, long paramLong) { if (paramStratum.isJava()) return new BaseLineInfo(-1, this.declaringType);  return new StratumLineInfo(paramStratum.id(), -1, null, null); } public String returnTypeName() { return this.signatureParser.typeName(); } private String returnSignature() { return this.signatureParser.signature(); } public Type returnType() throws ClassNotLoadedException { return findType(returnSignature()); } public Type findType(String paramString) throws ClassNotLoadedException { ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)declaringType(); return referenceTypeImpl.findType(paramString); } public List<String> argumentTypeNames() { return this.signatureParser.argumentTypeNames(); }
/* 253 */   ReturnContainer getReturnValueContainer() { if (this.retValContainer == null) {
/* 254 */       this.retValContainer = new ReturnContainer();
/*     */     }
/* 256 */     return this.retValContainer; }
/*     */   public List<String> argumentSignatures() { return this.signatureParser.argumentSignatures(); }
/*     */   Type argumentType(int paramInt) throws ClassNotLoadedException { ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)declaringType(); String str = argumentSignatures().get(paramInt); return referenceTypeImpl.findType(str); }
/*     */   public List<Type> argumentTypes() throws ClassNotLoadedException { int i = argumentSignatures().size(); ArrayList<Type> arrayList = new ArrayList(i); for (byte b = 0; b < i; b++) { Type type = argumentType(b); arrayList.add(type); }  return arrayList; }
/*     */   public int compareTo(Method paramMethod) { ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)declaringType(); int i = referenceTypeImpl.compareTo(paramMethod.declaringType()); if (i == 0) i = referenceTypeImpl.indexOf(this) - referenceTypeImpl.indexOf(paramMethod);  return i; }
/*     */   public boolean isAbstract() { return isModifierSet(1024); }
/*     */   public boolean isDefault() { return (!isModifierSet(1024) && !isModifierSet(8) && !isModifierSet(2) && declaringType() instanceof com.sun.jdi.InterfaceType); }
/*     */   public boolean isSynchronized() { return isModifierSet(32); }
/*     */   public boolean isNative() { return isModifierSet(256); } public boolean isVarArgs() { return isModifierSet(128); } public boolean isBridge() { return isModifierSet(64); } public boolean isConstructor() { return name().equals("<init>"); } public boolean isStaticInitializer() { return name().equals("<clinit>"); } public boolean isObsolete() { try { return (JDWP.Method.IsObsolete.process(this.vm, this.declaringType, this.ref)).isObsolete; } catch (JDWPException jDWPException) { throw jDWPException.toJDIException(); }  } class ReturnContainer implements ValueContainer {
/*     */     public Type type() throws ClassNotLoadedException { return MethodImpl.this.returnType(); } public String typeName() { return MethodImpl.this.returnTypeName(); } public String signature() { return MethodImpl.this.returnSignature(); } public Type findType(String param1String) throws ClassNotLoadedException { return MethodImpl.this.findType(param1String); }
/*     */   } class ArgumentContainer implements ValueContainer {
/* 267 */     ArgumentContainer(int param1Int) { this.index = param1Int; }
/*     */      int index;
/*     */     public Type type() throws ClassNotLoadedException {
/* 270 */       return MethodImpl.this.argumentType(this.index);
/*     */     }
/*     */     public String typeName() {
/* 273 */       return MethodImpl.this.argumentTypeNames().get(this.index);
/*     */     }
/*     */     public String signature() {
/* 276 */       return MethodImpl.this.argumentSignatures().get(this.index);
/*     */     }
/*     */     public Type findType(String param1String) throws ClassNotLoadedException {
/* 279 */       return MethodImpl.this.findType(param1String);
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
/*     */ 
/*     */ 
/*     */   
/*     */   void handleVarArgs(List<Value> paramList) throws ClassNotLoadedException, InvalidTypeException {
/* 301 */     List<Type> list = argumentTypes();
/* 302 */     ArrayType arrayType = (ArrayType)list.get(list.size() - 1);
/* 303 */     Type type1 = arrayType.componentType();
/* 304 */     int i = paramList.size();
/* 305 */     int j = list.size();
/* 306 */     if (i < j - 1) {
/*     */       return;
/*     */     }
/*     */     
/* 310 */     if (i == j - 1) {
/*     */ 
/*     */       
/* 313 */       ArrayReference arrayReference1 = arrayType.newInstance(0);
/* 314 */       paramList.add(arrayReference1);
/*     */       return;
/*     */     } 
/* 317 */     Value value = paramList.get(j - 1);
/* 318 */     if (value == null && i == j) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     Type type2 = (value == null) ? null : value.type();
/* 326 */     if (type2 instanceof ArrayTypeImpl && 
/* 327 */       i == j && ((ArrayTypeImpl)type2)
/* 328 */       .isAssignableTo((ReferenceType)arrayType)) {
/*     */       return;
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
/* 342 */     int k = i - j + 1;
/* 343 */     ArrayReference arrayReference = arrayType.newInstance(k);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 350 */     arrayReference.setValues(0, paramList, j - 1, k);
/* 351 */     paramList.set(j - 1, arrayReference);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     for (int m = j; m < i; m++) {
/* 357 */       paramList.remove(j);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Value> validateAndPrepareArgumentsForInvoke(List<? extends Value> paramList) throws ClassNotLoadedException, InvalidTypeException {
/* 368 */     ArrayList<Value> arrayList = new ArrayList<>(paramList);
/* 369 */     if (isVarArgs()) {
/* 370 */       handleVarArgs(arrayList);
/*     */     }
/*     */     
/* 373 */     int i = arrayList.size();
/*     */     
/* 375 */     JNITypeParser jNITypeParser = new JNITypeParser(signature());
/* 376 */     List<String> list = jNITypeParser.argumentSignatures();
/*     */     
/* 378 */     if (list.size() != i) {
/* 379 */       throw new IllegalArgumentException("Invalid argument count: expected " + list
/* 380 */           .size() + ", received " + arrayList
/* 381 */           .size());
/*     */     }
/*     */     
/* 384 */     for (byte b = 0; b < i; b++) {
/* 385 */       Value value = arrayList.get(b);
/* 386 */       value = ValueImpl.prepareForAssignment(value, new ArgumentContainer(b));
/*     */       
/* 388 */       arrayList.set(b, value);
/*     */     } 
/* 390 */     return arrayList;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 394 */     StringBuffer stringBuffer = new StringBuffer();
/* 395 */     stringBuffer.append(declaringType().name());
/* 396 */     stringBuffer.append(".");
/* 397 */     stringBuffer.append(name());
/* 398 */     stringBuffer.append("(");
/* 399 */     boolean bool = true;
/* 400 */     for (String str : argumentTypeNames()) {
/* 401 */       if (!bool) {
/* 402 */         stringBuffer.append(", ");
/*     */       }
/* 404 */       stringBuffer.append(str);
/* 405 */       bool = false;
/*     */     } 
/* 407 */     stringBuffer.append(")");
/* 408 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   abstract int argSlotCount() throws AbsentInformationException;
/*     */   
/*     */   abstract List<Location> allLineLocations(SDE.Stratum paramStratum, String paramString) throws AbsentInformationException;
/*     */   
/*     */   abstract List<Location> locationsOfLine(SDE.Stratum paramStratum, String paramString, int paramInt) throws AbsentInformationException;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\MethodImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */