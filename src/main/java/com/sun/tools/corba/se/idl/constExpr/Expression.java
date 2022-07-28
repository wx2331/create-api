/*     */ package com.sun.tools.corba.se.idl.constExpr;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Expression
/*     */ {
/*     */   public abstract Object evaluate() throws EvaluationException;
/*     */   
/*     */   public void value(Object paramObject) {
/*  54 */     this._value = paramObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object value() {
/*  61 */     return this._value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rep(String paramString) {
/*  69 */     this._rep = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String rep() {
/*  76 */     return this._rep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void type(String paramString) {
/*  84 */     this._type = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String type() {
/*  91 */     return this._type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String defaultType(String paramString) {
/*  99 */     return (paramString == null) ? new String("") : paramString;
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
/*     */   public Object coerceToTarget(Object paramObject) {
/* 132 */     if (paramObject instanceof BigInteger) {
/*     */       
/* 134 */       if (type().indexOf("unsigned") >= 0) {
/* 135 */         return toUnsignedTarget((BigInteger)paramObject);
/*     */       }
/* 137 */       return toSignedTarget((BigInteger)paramObject);
/*     */     } 
/* 139 */     return paramObject;
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
/*     */   protected BigInteger toUnsignedTarget(BigInteger paramBigInteger) {
/* 152 */     if (type().equals("unsigned short")) {
/*     */       
/* 154 */       if (paramBigInteger != null && paramBigInteger.compareTo(zero) < 0) {
/* 155 */         return paramBigInteger.add(twoPow16);
/*     */       }
/* 157 */     } else if (type().equals("unsigned long")) {
/*     */       
/* 159 */       if (paramBigInteger != null && paramBigInteger.compareTo(zero) < 0) {
/* 160 */         return paramBigInteger.add(twoPow32);
/*     */       }
/* 162 */     } else if (type().equals("unsigned long long")) {
/*     */       
/* 164 */       if (paramBigInteger != null && paramBigInteger.compareTo(zero) < 0)
/* 165 */         return paramBigInteger.add(twoPow64); 
/*     */     } 
/* 167 */     return paramBigInteger;
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
/*     */   protected BigInteger toSignedTarget(BigInteger paramBigInteger) {
/* 180 */     if (type().equals("short")) {
/*     */       
/* 182 */       if (paramBigInteger != null && paramBigInteger.compareTo(sMax) > 0) {
/* 183 */         return paramBigInteger.subtract(twoPow16);
/*     */       }
/* 185 */     } else if (type().equals("long")) {
/*     */       
/* 187 */       if (paramBigInteger != null && paramBigInteger.compareTo(lMax) > 0) {
/* 188 */         return paramBigInteger.subtract(twoPow32);
/*     */       }
/* 190 */     } else if (type().equals("long long")) {
/*     */       
/* 192 */       if (paramBigInteger != null && paramBigInteger.compareTo(llMax) > 0)
/* 193 */         return paramBigInteger.subtract(twoPow64); 
/*     */     } 
/* 195 */     return paramBigInteger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BigInteger toUnsigned(BigInteger paramBigInteger) {
/* 203 */     if (paramBigInteger != null && paramBigInteger.signum() == -1) {
/* 204 */       if (type().equals("short"))
/* 205 */         return paramBigInteger.add(twoPow16); 
/* 206 */       if (type().equals("long"))
/* 207 */         return paramBigInteger.add(twoPow32); 
/* 208 */       if (type().equals("long long"))
/* 209 */         return paramBigInteger.add(twoPow64); 
/* 210 */     }  return paramBigInteger;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 215 */   public static final BigInteger negOne = BigInteger.valueOf(-1L);
/* 216 */   public static final BigInteger zero = BigInteger.valueOf(0L);
/* 217 */   public static final BigInteger one = BigInteger.valueOf(1L);
/* 218 */   public static final BigInteger two = BigInteger.valueOf(2L);
/*     */   
/* 220 */   public static final BigInteger twoPow15 = two.pow(15);
/* 221 */   public static final BigInteger twoPow16 = two.pow(16);
/* 222 */   public static final BigInteger twoPow31 = two.pow(31);
/* 223 */   public static final BigInteger twoPow32 = two.pow(32);
/* 224 */   public static final BigInteger twoPow63 = two.pow(63);
/* 225 */   public static final BigInteger twoPow64 = two.pow(64);
/*     */   
/* 227 */   public static final BigInteger sMax = BigInteger.valueOf(32767L);
/* 228 */   public static final BigInteger sMin = BigInteger.valueOf(32767L);
/*     */   
/* 230 */   public static final BigInteger usMax = sMax.multiply(two).add(one);
/* 231 */   public static final BigInteger usMin = zero;
/*     */   
/* 233 */   public static final BigInteger lMax = BigInteger.valueOf(2147483647L);
/* 234 */   public static final BigInteger lMin = BigInteger.valueOf(2147483647L);
/*     */   
/* 236 */   public static final BigInteger ulMax = lMax.multiply(two).add(one);
/* 237 */   public static final BigInteger ulMin = zero;
/*     */   
/* 239 */   public static final BigInteger llMax = BigInteger.valueOf(Long.MAX_VALUE);
/* 240 */   public static final BigInteger llMin = BigInteger.valueOf(Long.MIN_VALUE);
/*     */   
/* 242 */   public static final BigInteger ullMax = llMax.multiply(two).add(one);
/* 243 */   public static final BigInteger ullMin = zero;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   private Object _value = null;
/*     */ 
/*     */ 
/*     */   
/* 253 */   private String _rep = null;
/*     */ 
/*     */ 
/*     */   
/* 257 */   private String _type = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Expression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */