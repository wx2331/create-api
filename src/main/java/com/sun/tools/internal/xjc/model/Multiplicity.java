/*     */ package com.sun.tools.internal.xjc.model;
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
/*     */ public final class Multiplicity
/*     */ {
/*     */   public final BigInteger min;
/*     */   public final BigInteger max;
/*     */   
/*     */   public static Multiplicity create(BigInteger min, BigInteger max) {
/*  50 */     if (BigInteger.ZERO.equals(min) && max == null) return STAR; 
/*  51 */     if (BigInteger.ONE.equals(min) && max == null) return PLUS; 
/*  52 */     if (max != null) {
/*  53 */       if (BigInteger.ZERO.equals(min) && BigInteger.ZERO.equals(max)) return ZERO; 
/*  54 */       if (BigInteger.ZERO.equals(min) && BigInteger.ONE.equals(max)) return OPTIONAL; 
/*  55 */       if (BigInteger.ONE.equals(min) && BigInteger.ONE.equals(max)) return ONE; 
/*     */     } 
/*  57 */     return new Multiplicity(min, max);
/*     */   }
/*     */   
/*     */   public static Multiplicity create(int min, Integer max) {
/*  61 */     return create(BigInteger.valueOf(min), BigInteger.valueOf(max.intValue()));
/*     */   }
/*     */   
/*     */   private Multiplicity(BigInteger min, BigInteger max) {
/*  65 */     this.min = min;
/*  66 */     this.max = max;
/*     */   }
/*     */   
/*     */   private Multiplicity(int min, int max) {
/*  70 */     this(BigInteger.valueOf(min), BigInteger.valueOf(max));
/*     */   }
/*     */   
/*     */   private Multiplicity(int min, Integer max) {
/*  74 */     this(BigInteger.valueOf(min), (max == null) ? null : BigInteger.valueOf(max.intValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  79 */     if (!(o instanceof Multiplicity)) return false;
/*     */     
/*  81 */     Multiplicity that = (Multiplicity)o;
/*     */     
/*  83 */     if (!this.min.equals(that.min)) return false; 
/*  84 */     if ((this.max != null) ? !this.max.equals(that.max) : (that.max != null)) return false;
/*     */     
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     return this.min.add(this.max).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnique() {
/*  96 */     if (this.max == null) return false; 
/*  97 */     return (BigInteger.ONE.equals(this.min) && BigInteger.ONE.equals(this.max));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOptional() {
/* 102 */     if (this.max == null) return false; 
/* 103 */     return (BigInteger.ZERO.equals(this.min) && BigInteger.ONE.equals(this.max));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAtMostOnce() {
/* 108 */     if (this.max == null) return false; 
/* 109 */     return (this.max.compareTo(BigInteger.ONE) <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZero() {
/* 114 */     if (this.max == null) return false; 
/* 115 */     return BigInteger.ZERO.equals(this.max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean includes(Multiplicity rhs) {
/* 125 */     if (rhs.min.compareTo(this.min) == -1) return false; 
/* 126 */     if (this.max == null) return true; 
/* 127 */     if (rhs.max == null) return false; 
/* 128 */     return (rhs.max.compareTo(this.max) <= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMaxString() {
/* 136 */     if (this.max == null) return "unbounded"; 
/* 137 */     return this.max.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     return "(" + this.min + ',' + getMaxString() + ')';
/*     */   }
/*     */ 
/*     */   
/* 149 */   public static final Multiplicity ZERO = new Multiplicity(0, 0);
/*     */ 
/*     */   
/* 152 */   public static final Multiplicity ONE = new Multiplicity(1, 1);
/*     */ 
/*     */   
/* 155 */   public static final Multiplicity OPTIONAL = new Multiplicity(0, 1);
/*     */ 
/*     */   
/* 158 */   public static final Multiplicity STAR = new Multiplicity(0, null);
/*     */ 
/*     */   
/* 161 */   public static final Multiplicity PLUS = new Multiplicity(1, null);
/*     */ 
/*     */   
/*     */   public static Multiplicity choice(Multiplicity lhs, Multiplicity rhs) {
/* 165 */     return create(lhs.min
/* 166 */         .min(rhs.min), (lhs.max == null || rhs.max == null) ? null : lhs.max
/* 167 */         .max(rhs.max));
/*     */   }
/*     */   public static Multiplicity group(Multiplicity lhs, Multiplicity rhs) {
/* 170 */     return create(lhs.min
/* 171 */         .add(rhs.min), (lhs.max == null || rhs.max == null) ? null : lhs.max
/* 172 */         .add(rhs.max));
/*     */   }
/*     */   public static Multiplicity multiply(Multiplicity lhs, Multiplicity rhs) {
/* 175 */     BigInteger max, min = lhs.min.multiply(rhs.min);
/*     */     
/* 177 */     if (isZero(lhs.max) || isZero(rhs.max)) {
/* 178 */       max = BigInteger.ZERO;
/*     */     }
/* 180 */     else if (lhs.max == null || rhs.max == null) {
/* 181 */       max = null;
/*     */     } else {
/* 183 */       max = lhs.max.multiply(rhs.max);
/* 184 */     }  return create(min, max);
/*     */   }
/*     */   
/*     */   private static boolean isZero(BigInteger i) {
/* 188 */     return (i != null && BigInteger.ZERO.equals(i));
/*     */   }
/*     */   
/*     */   public static Multiplicity oneOrMore(Multiplicity c) {
/* 192 */     if (c.max == null) return c; 
/* 193 */     if (BigInteger.ZERO.equals(c.max)) return c; 
/* 194 */     return create(c.min, (BigInteger)null);
/*     */   }
/*     */   
/*     */   public Multiplicity makeOptional() {
/* 198 */     if (BigInteger.ZERO.equals(this.min)) return this; 
/* 199 */     return create(BigInteger.ZERO, this.max);
/*     */   }
/*     */   
/*     */   public Multiplicity makeRepeated() {
/* 203 */     if (this.max == null || BigInteger.ZERO.equals(this.max)) return this; 
/* 204 */     return create(this.min, (BigInteger)null);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\Multiplicity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */