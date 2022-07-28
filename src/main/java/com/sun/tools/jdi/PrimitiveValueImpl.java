/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.PrimitiveValue;
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
/*     */ public abstract class PrimitiveValueImpl
/*     */   extends ValueImpl
/*     */   implements PrimitiveValue
/*     */ {
/*     */   PrimitiveValueImpl(VirtualMachine paramVirtualMachine) {
/*  34 */     super(paramVirtualMachine);
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
/*     */   byte checkedByteValue() throws InvalidTypeException {
/*  56 */     return byteValue();
/*     */   }
/*     */   char checkedCharValue() throws InvalidTypeException {
/*  59 */     return charValue();
/*     */   }
/*     */   short checkedShortValue() throws InvalidTypeException {
/*  62 */     return shortValue();
/*     */   }
/*     */   int checkedIntValue() throws InvalidTypeException {
/*  65 */     return intValue();
/*     */   }
/*     */   long checkedLongValue() throws InvalidTypeException {
/*  68 */     return longValue();
/*     */   }
/*     */   float checkedFloatValue() throws InvalidTypeException {
/*  71 */     return floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean checkedBooleanValue() throws InvalidTypeException {
/*  79 */     if (this instanceof com.sun.jdi.BooleanValue) {
/*  80 */       return booleanValue();
/*     */     }
/*  82 */     throw new InvalidTypeException("Can't convert non-boolean value to boolean");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final double checkedDoubleValue() throws InvalidTypeException {
/*  91 */     return doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ValueImpl prepareForAssignmentTo(ValueContainer paramValueContainer) throws InvalidTypeException {
/*  97 */     return convertForAssignmentTo(paramValueContainer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ValueImpl convertForAssignmentTo(ValueContainer paramValueContainer) throws InvalidTypeException {
/* 106 */     if (paramValueContainer.signature().length() > 1) {
/* 107 */       throw new InvalidTypeException("Can't assign primitive value to object");
/*     */     }
/*     */     
/* 110 */     if (paramValueContainer.signature().charAt(0) == 'Z' && 
/* 111 */       type().signature().charAt(0) != 'Z') {
/* 112 */       throw new InvalidTypeException("Can't assign non-boolean value to a boolean");
/*     */     }
/*     */     
/* 115 */     if (paramValueContainer.signature().charAt(0) != 'Z' && 
/* 116 */       type().signature().charAt(0) == 'Z') {
/* 117 */       throw new InvalidTypeException("Can't assign boolean value to an non-boolean");
/*     */     }
/*     */     
/* 120 */     if ("void".equals(paramValueContainer.typeName())) {
/* 121 */       throw new InvalidTypeException("Can't assign primitive value to a void");
/*     */     }
/*     */     
/*     */     try {
/* 125 */       PrimitiveTypeImpl primitiveTypeImpl = (PrimitiveTypeImpl)paramValueContainer.type();
/* 126 */       return (ValueImpl)primitiveTypeImpl.convert(this);
/* 127 */     } catch (ClassNotLoadedException classNotLoadedException) {
/* 128 */       throw new InternalException("Signature and type inconsistent for: " + paramValueContainer
/* 129 */           .typeName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract boolean booleanValue();
/*     */   
/*     */   public abstract byte byteValue();
/*     */   
/*     */   public abstract char charValue();
/*     */   
/*     */   public abstract short shortValue();
/*     */   
/*     */   public abstract int intValue();
/*     */   
/*     */   public abstract long longValue();
/*     */   
/*     */   public abstract float floatValue();
/*     */   
/*     */   public abstract double doubleValue();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\PrimitiveValueImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */