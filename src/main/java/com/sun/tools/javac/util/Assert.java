/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Assert
/*     */ {
/*     */   public static void check(boolean paramBoolean) {
/*  44 */     if (!paramBoolean) {
/*  45 */       error();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkNull(Object paramObject) {
/*  52 */     if (paramObject != null) {
/*  53 */       error();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T checkNonNull(T paramT) {
/*  60 */     if (paramT == null)
/*  61 */       error(); 
/*  62 */     return paramT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void check(boolean paramBoolean, int paramInt) {
/*  69 */     if (!paramBoolean) {
/*  70 */       error(String.valueOf(paramInt));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void check(boolean paramBoolean, long paramLong) {
/*  77 */     if (!paramBoolean) {
/*  78 */       error(String.valueOf(paramLong));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void check(boolean paramBoolean, Object paramObject) {
/*  85 */     if (!paramBoolean) {
/*  86 */       error(String.valueOf(paramObject));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void check(boolean paramBoolean, String paramString) {
/*  93 */     if (!paramBoolean) {
/*  94 */       error(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkNull(Object paramObject1, Object paramObject2) {
/* 101 */     if (paramObject1 != null) {
/* 102 */       error(String.valueOf(paramObject2));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkNull(Object paramObject, String paramString) {
/* 109 */     if (paramObject != null) {
/* 110 */       error(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T checkNonNull(T paramT, String paramString) {
/* 117 */     if (paramT == null)
/* 118 */       error(paramString); 
/* 119 */     return paramT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void error() {
/* 126 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void error(String paramString) {
/* 133 */     throw new AssertionError(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Assert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */