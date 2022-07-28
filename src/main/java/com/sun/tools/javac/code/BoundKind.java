/*    */ package com.sun.tools.javac.code;
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
/*    */ public enum BoundKind
/*    */ {
/* 36 */   EXTENDS("? extends "),
/* 37 */   SUPER("? super "),
/* 38 */   UNBOUND("?");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   BoundKind(String paramString1) {
/* 43 */     this.name = paramString1;
/*    */   }
/*    */   public String toString() {
/* 46 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\BoundKind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */