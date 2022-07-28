/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import org.xml.sax.Locator;
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
/*    */ public final class Util
/*    */ {
/*    */   public static String getSystemProperty(String name) {
/*    */     try {
/* 47 */       return System.getProperty(name);
/* 48 */     } catch (SecurityException e) {
/* 49 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean equals(Locator lhs, Locator rhs) {
/* 57 */     return (lhs.getLineNumber() == rhs.getLineNumber() && lhs
/* 58 */       .getColumnNumber() == rhs.getColumnNumber() && 
/* 59 */       equals(lhs.getSystemId(), rhs.getSystemId()) && 
/* 60 */       equals(lhs.getPublicId(), rhs.getPublicId()));
/*    */   }
/*    */   
/*    */   private static boolean equals(String lhs, String rhs) {
/* 64 */     if (lhs == null && rhs == null) return true; 
/* 65 */     if (lhs == null || rhs == null) return false; 
/* 66 */     return lhs.equals(rhs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getSystemProperty(Class clazz, String name) {
/* 74 */     return getSystemProperty(clazz.getName() + '.' + name);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */