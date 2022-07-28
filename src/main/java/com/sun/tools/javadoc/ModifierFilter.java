/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModifierFilter
/*     */ {
/*     */   public static final long PACKAGE = -9223372036854775808L;
/*     */   public static final long ALL_ACCESS = -9223372036854775801L;
/*     */   private long oneOf;
/*     */   private long must;
/*     */   private long cannot;
/*     */   private static final int ACCESS_BITS = 7;
/*     */   
/*     */   public ModifierFilter(long paramLong) {
/*  79 */     this(paramLong, 0L, 0L);
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
/*     */   public ModifierFilter(long paramLong1, long paramLong2, long paramLong3) {
/* 103 */     this.oneOf = paramLong1;
/* 104 */     this.must = paramLong2;
/* 105 */     this.cannot = paramLong3;
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
/*     */   public boolean checkModifier(int paramInt) {
/* 117 */     long l = ((paramInt & 0x7) == 0) ? (paramInt | Long.MIN_VALUE) : paramInt;
/*     */ 
/*     */     
/* 120 */     return ((this.oneOf == 0L || (this.oneOf & l) != 0L) && (this.must & l) == this.must && (this.cannot & l) == 0L);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ModifierFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */