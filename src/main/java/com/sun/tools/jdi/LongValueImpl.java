/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.LongValue;
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
/*     */ public class LongValueImpl
/*     */   extends PrimitiveValueImpl
/*     */   implements LongValue
/*     */ {
/*     */   private long value;
/*     */   
/*     */   LongValueImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  35 */     super(paramVirtualMachine);
/*     */     
/*  37 */     this.value = paramLong;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if (paramObject != null && paramObject instanceof LongValue) {
/*  42 */       return (this.value == ((LongValue)paramObject).value() && super
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
/*     */   public int compareTo(LongValue paramLongValue) {
/*  57 */     long l = paramLongValue.value();
/*  58 */     if (value() < l)
/*  59 */       return -1; 
/*  60 */     if (value() == l) {
/*  61 */       return 0;
/*     */     }
/*  63 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type type() {
/*  68 */     return (Type)this.vm.theLongType();
/*     */   }
/*     */   
/*     */   public long value() {
/*  72 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean booleanValue() {
/*  76 */     return !(this.value == 0L);
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
/*  96 */     return this.value;
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
/* 108 */     if (this.value > 127L || this.value < -128L) {
/* 109 */       throw new InvalidTypeException("Can't convert " + this.value + " to byte");
/*     */     }
/* 111 */     return super.checkedByteValue();
/*     */   }
/*     */ 
/*     */   
/*     */   char checkedCharValue() throws InvalidTypeException {
/* 116 */     if (this.value > 65535L || this.value < 0L) {
/* 117 */       throw new InvalidTypeException("Can't convert " + this.value + " to char");
/*     */     }
/* 119 */     return super.checkedCharValue();
/*     */   }
/*     */ 
/*     */   
/*     */   short checkedShortValue() throws InvalidTypeException {
/* 124 */     if (this.value > 32767L || this.value < -32768L) {
/* 125 */       throw new InvalidTypeException("Can't convert " + this.value + " to short");
/*     */     }
/* 127 */     return super.checkedShortValue();
/*     */   }
/*     */ 
/*     */   
/*     */   int checkedIntValue() throws InvalidTypeException {
/* 132 */     if (this.value > 2147483647L || this.value < -2147483648L) {
/* 133 */       throw new InvalidTypeException("Can't convert " + this.value + " to int");
/*     */     }
/* 135 */     return super.checkedIntValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     return "" + this.value;
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 144 */     return 74;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\LongValueImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */