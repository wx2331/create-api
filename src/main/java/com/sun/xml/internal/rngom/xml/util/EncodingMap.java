/*    */ package com.sun.xml.internal.rngom.xml.util;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class EncodingMap
/*    */ {
/* 51 */   private static final String[] aliases = new String[] { "UTF-8", "UTF8", "UTF-16", "Unicode", "UTF-16BE", "UnicodeBigUnmarked", "UTF-16LE", "UnicodeLittleUnmarked", "US-ASCII", "ASCII", "TIS-620", "TIS620" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getJavaName(String enc) {
/*    */     try {
/* 62 */       "x".getBytes(enc);
/*    */     }
/* 64 */     catch (UnsupportedEncodingException e) {
/* 65 */       for (int i = 0; i < aliases.length; i += 2) {
/* 66 */         if (enc.equalsIgnoreCase(aliases[i])) {
/*    */           try {
/* 68 */             "x".getBytes(aliases[i + 1]);
/* 69 */             return aliases[i + 1];
/*    */           }
/* 71 */           catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*    */         }
/*    */       } 
/*    */     } 
/* 75 */     return enc;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 79 */     System.err.println(getJavaName(args[0]));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\xm\\util\EncodingMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */