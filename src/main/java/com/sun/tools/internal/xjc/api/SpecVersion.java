/*    */ package com.sun.tools.internal.xjc.api;
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
/*    */ public enum SpecVersion
/*    */ {
/* 34 */   V2_0, V2_1, V2_2;
/*    */   
/*    */   public static final SpecVersion LATEST;
/*    */ 
/*    */   
/*    */   public boolean isLaterThan(SpecVersion t) {
/* 40 */     return (ordinal() >= t.ordinal());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SpecVersion parse(String token) {
/* 49 */     if (token.equals("2.0"))
/* 50 */       return V2_0; 
/* 51 */     if (token.equals("2.1"))
/* 52 */       return V2_1; 
/* 53 */     if (token.equals("2.2"))
/* 54 */       return V2_2; 
/* 55 */     return null;
/*    */   }
/*    */   static {
/* 58 */     LATEST = V2_2;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\SpecVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */