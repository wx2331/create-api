/*    */ package com.sun.xml.internal.rngom.util;
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
/*    */ 
/*    */ public abstract class Utf16
/*    */ {
/*    */   public static boolean isSurrogate(char c) {
/* 51 */     return ((c & 0xF800) == 55296);
/*    */   }
/*    */   public static boolean isSurrogate1(char c) {
/* 54 */     return ((c & 0xFC00) == 55296);
/*    */   }
/*    */   public static boolean isSurrogate2(char c) {
/* 57 */     return ((c & 0xFC00) == 56320);
/*    */   }
/*    */   public static int scalarValue(char c1, char c2) {
/* 60 */     return ((c1 & 0x3FF) << 10 | c2 & 0x3FF) + 65536;
/*    */   }
/*    */   public static char surrogate1(int c) {
/* 63 */     return (char)(c - 65536 >> 10 | 0xD800);
/*    */   }
/*    */   public static char surrogate2(int c) {
/* 66 */     return (char)(c - 65536 & 0x3FF | 0xDC00);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngo\\util\Utf16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */