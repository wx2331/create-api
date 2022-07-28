/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.FloatValue;
/*     */ import com.sun.jdi.InvalidTypeException;
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
/*     */ public class FloatValueImpl
/*     */   extends PrimitiveValueImpl
/*     */   implements FloatValue
/*     */ {
/*     */   private float value;
/*     */   
/*     */   FloatValueImpl(VirtualMachine paramVirtualMachine, float paramFloat) {
/*  35 */     super(paramVirtualMachine);
/*     */     
/*  37 */     this.value = paramFloat;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if (paramObject != null && paramObject instanceof FloatValue) {
/*  42 */       return (this.value == ((FloatValue)paramObject).value() && super
/*  43 */         .equals(paramObject));
/*     */     }
/*  45 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  53 */     return intValue();
/*     */   }
/*     */   
/*     */   public int compareTo(FloatValue paramFloatValue) {
/*  57 */     float f = paramFloatValue.value();
/*  58 */     if (value() < f)
/*  59 */       return -1; 
/*  60 */     if (value() == f) {
/*  61 */       return 0;
/*     */     }
/*  63 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type type() {
/*  68 */     return (Type)this.vm.theFloatType();
/*     */   }
/*     */   
/*     */   public float value() {
/*  72 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean booleanValue() {
/*  76 */     return !(this.value == 0.0D);
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/*  80 */     return (byte)(int)this.value;
/*     */   }
/*     */   
/*     */   public char charValue() {
/*  84 */     return (char)(int)this.value;
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  88 */     return (short)(int)this.value;
/*     */   }
/*     */   
/*     */   public int intValue() {
/*  92 */     return (int)this.value;
/*     */   }
/*     */   
/*     */   public long longValue() {
/*  96 */     return (long)this.value;
/*     */   }
/*     */   
/*     */   public float floatValue() {
/* 100 */     return this.value;
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 104 */     return this.value;
/*     */   }
/*     */   
/*     */   byte checkedByteValue() throws InvalidTypeException {
/* 108 */     if (this.value > 127.0F || this.value < -128.0F) {
/* 109 */       throw new InvalidTypeException("Can't convert " + this.value + " to byte");
/*     */     }
/* 111 */     return super.checkedByteValue();
/*     */   }
/*     */ 
/*     */   
/*     */   char checkedCharValue() throws InvalidTypeException {
/* 116 */     if (this.value > 65535.0F || this.value < 0.0F) {
/* 117 */       throw new InvalidTypeException("Can't convert " + this.value + " to char");
/*     */     }
/* 119 */     return super.checkedCharValue();
/*     */   }
/*     */ 
/*     */   
/*     */   short checkedShortValue() throws InvalidTypeException {
/* 124 */     if (this.value > 32767.0F || this.value < -32768.0F) {
/* 125 */       throw new InvalidTypeException("Can't convert " + this.value + " to short");
/*     */     }
/* 127 */     return super.checkedShortValue();
/*     */   }
/*     */ 
/*     */   
/*     */   int checkedIntValue() throws InvalidTypeException {
/* 132 */     int i = (int)this.value;
/* 133 */     if (i != this.value) {
/* 134 */       throw new InvalidTypeException("Can't convert " + this.value + " to int");
/*     */     }
/* 136 */     return super.checkedIntValue();
/*     */   }
/*     */ 
/*     */   
/*     */   long checkedLongValue() throws InvalidTypeException {
/* 141 */     long l = (long)this.value;
/* 142 */     if ((float)l != this.value) {
/* 143 */       throw new InvalidTypeException("Can't convert " + this.value + " to long");
/*     */     }
/* 145 */     return super.checkedLongValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 150 */     return "" + this.value;
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 154 */     return 70;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\FloatValueImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */