/*     */ package com.sun.tools.hat.internal.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Misc
/*     */ {
/*  45 */   private static char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */   
/*     */   public static final String toHex(int paramInt) {
/*  49 */     char[] arrayOfChar = new char[8];
/*  50 */     byte b1 = 0;
/*  51 */     for (byte b2 = 28; b2 >= 0; b2 -= 4) {
/*  52 */       arrayOfChar[b1++] = digits[paramInt >> b2 & 0xF];
/*     */     }
/*  54 */     return "0x" + new String(arrayOfChar);
/*     */   }
/*     */   
/*     */   public static final String toHex(long paramLong) {
/*  58 */     return "0x" + Long.toHexString(paramLong);
/*     */   }
/*     */   
/*     */   public static final long parseHex(String paramString) {
/*  62 */     long l = 0L;
/*  63 */     if (paramString.length() < 2 || paramString.charAt(0) != '0' || paramString
/*  64 */       .charAt(1) != 'x') {
/*  65 */       return -1L;
/*     */     }
/*  67 */     for (byte b = 2; b < paramString.length(); b++) {
/*  68 */       l *= 16L;
/*  69 */       char c = paramString.charAt(b);
/*  70 */       if (c >= '0' && c <= '9') {
/*  71 */         l += (c - 48);
/*  72 */       } else if (c >= 'a' && c <= 'f') {
/*  73 */         l += (c - 97 + 10);
/*  74 */       } else if (c >= 'A' && c <= 'F') {
/*  75 */         l += (c - 65 + 10);
/*     */       } else {
/*  77 */         throw new NumberFormatException("" + c + " is not a valid hex digit");
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     return l;
/*     */   }
/*     */   
/*     */   public static String encodeHtml(String paramString) {
/*  85 */     int i = paramString.length();
/*  86 */     StringBuffer stringBuffer = new StringBuffer();
/*  87 */     for (byte b = 0; b < i; b++) {
/*  88 */       char c = paramString.charAt(b);
/*  89 */       if (c == '<') {
/*  90 */         stringBuffer.append("&lt;");
/*  91 */       } else if (c == '>') {
/*  92 */         stringBuffer.append("&gt;");
/*  93 */       } else if (c == '"') {
/*  94 */         stringBuffer.append("&quot;");
/*  95 */       } else if (c == '\'') {
/*  96 */         stringBuffer.append("&#039;");
/*  97 */       } else if (c == '&') {
/*  98 */         stringBuffer.append("&amp;");
/*  99 */       } else if (c < ' ') {
/* 100 */         stringBuffer.append("&#" + Integer.toString(c) + ";");
/*     */       } else {
/* 102 */         int j = c & Character.MAX_VALUE;
/* 103 */         if (j > 127) {
/* 104 */           stringBuffer.append("&#" + Integer.toString(j) + ";");
/*     */         } else {
/* 106 */           stringBuffer.append(c);
/*     */         } 
/*     */       } 
/*     */     } 
/* 110 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\interna\\util\Misc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */