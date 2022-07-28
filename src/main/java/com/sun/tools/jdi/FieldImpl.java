/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.Field;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldImpl
/*     */   extends TypeComponentImpl
/*     */   implements Field, ValueContainer
/*     */ {
/*     */   FieldImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt) {
/*  38 */     super(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  43 */     if (paramObject != null && paramObject instanceof FieldImpl) {
/*  44 */       FieldImpl fieldImpl = (FieldImpl)paramObject;
/*  45 */       return (declaringType().equals(fieldImpl.declaringType()) && 
/*  46 */         ref() == fieldImpl.ref() && super
/*  47 */         .equals(paramObject));
/*     */     } 
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  54 */     return (int)ref();
/*     */   }
/*     */   
/*     */   public int compareTo(Field paramField) {
/*  58 */     ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)declaringType();
/*  59 */     int i = referenceTypeImpl.compareTo(paramField.declaringType());
/*  60 */     if (i == 0)
/*     */     {
/*  62 */       i = referenceTypeImpl.indexOf(this) - referenceTypeImpl.indexOf(paramField);
/*     */     }
/*  64 */     return i;
/*     */   }
/*     */   
/*     */   public Type type() throws ClassNotLoadedException {
/*  68 */     return findType(signature());
/*     */   }
/*     */   
/*     */   public Type findType(String paramString) throws ClassNotLoadedException {
/*  72 */     ReferenceTypeImpl referenceTypeImpl = (ReferenceTypeImpl)declaringType();
/*  73 */     return referenceTypeImpl.findType(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String typeName() {
/*  81 */     JNITypeParser jNITypeParser = new JNITypeParser(signature());
/*  82 */     return jNITypeParser.typeName();
/*     */   }
/*     */   
/*     */   public boolean isTransient() {
/*  86 */     return isModifierSet(128);
/*     */   }
/*     */   
/*     */   public boolean isVolatile() {
/*  90 */     return isModifierSet(64);
/*     */   }
/*     */   
/*     */   public boolean isEnumConstant() {
/*  94 */     return isModifierSet(16384);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  98 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/* 100 */     stringBuffer.append(declaringType().name());
/* 101 */     stringBuffer.append('.');
/* 102 */     stringBuffer.append(name());
/*     */     
/* 104 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\FieldImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */