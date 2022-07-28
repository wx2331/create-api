/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.DoubleValue;
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
/*     */ public class DoubleValueImpl
/*     */   extends PrimitiveValueImpl
/*     */   implements DoubleValue
/*     */ {
/*     */   private double value;
/*     */   
/*     */   DoubleValueImpl(VirtualMachine paramVirtualMachine, double paramDouble) {
/*  35 */     super(paramVirtualMachine);
/*     */     
/*  37 */     this.value = paramDouble;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if (paramObject != null && paramObject instanceof DoubleValue) {
/*  42 */       return (this.value == ((DoubleValue)paramObject).value() && super
/*  43 */         .equals(paramObject));
/*     */     }
/*  45 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(DoubleValue paramDoubleValue) {
/*  50 */     double d = paramDoubleValue.value();
/*  51 */     if (value() < d)
/*  52 */       return -1; 
/*  53 */     if (value() == d) {
/*  54 */       return 0;
/*     */     }
/*  56 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  64 */     return intValue();
/*     */   }
/*     */   
/*     */   public Type type() {
/*  68 */     return (Type)this.vm.theDoubleType();
/*     */   }
/*     */   
/*     */   public double value() {
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
/* 100 */     return (float)this.value;
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 104 */     return this.value;
/*     */   }
/*     */   
/*     */   byte checkedByteValue() throws InvalidTypeException {
/* 108 */     if (this.value > 127.0D || this.value < -128.0D) {
/* 109 */       throw new InvalidTypeException("Can't convert " + this.value + " to byte");
/*     */     }
/* 111 */     return super.checkedByteValue();
/*     */   }
/*     */ 
/*     */   
/*     */   char checkedCharValue() throws InvalidTypeException {
/* 116 */     if (this.value > 65535.0D || this.value < 0.0D) {
/* 117 */       throw new InvalidTypeException("Can't convert " + this.value + " to char");
/*     */     }
/* 119 */     return super.checkedCharValue();
/*     */   }
/*     */ 
/*     */   
/*     */   short checkedShortValue() throws InvalidTypeException {
/* 124 */     if (this.value > 32767.0D || this.value < -32768.0D) {
/* 125 */       throw new InvalidTypeException("Can't convert " + this.value + " to short");
/*     */     }
/* 127 */     return super.checkedShortValue();
/*     */   }
/*     */ 
/*     */   
/*     */   int checkedIntValue() throws InvalidTypeException {
/* 132 */     if (this.value > 2.147483647E9D || this.value < -2.147483648E9D) {
/* 133 */       throw new InvalidTypeException("Can't convert " + this.value + " to int");
/*     */     }
/* 135 */     return super.checkedIntValue();
/*     */   }
/*     */ 
/*     */   
/*     */   long checkedLongValue() throws InvalidTypeException {
/* 140 */     long l = (long)this.value;
/* 141 */     if (l != this.value) {
/* 142 */       throw new InvalidTypeException("Can't convert " + this.value + " to long");
/*     */     }
/* 144 */     return super.checkedLongValue();
/*     */   }
/*     */ 
/*     */   
/*     */   float checkedFloatValue() throws InvalidTypeException {
/* 149 */     float f = (float)this.value;
/* 150 */     if (f != this.value) {
/* 151 */       throw new InvalidTypeException("Can't convert " + this.value + " to float");
/*     */     }
/* 153 */     return super.checkedFloatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return "" + this.value;
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 162 */     return 68;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\DoubleValueImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */