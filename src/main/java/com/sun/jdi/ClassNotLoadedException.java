/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
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
/*    */ @Exported
/*    */ public class ClassNotLoadedException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -6242978768444298722L;
/*    */   private String className;
/*    */   
/*    */   public ClassNotLoadedException(String paramString) {
/* 78 */     this.className = paramString;
/*    */   }
/*    */   
/*    */   public ClassNotLoadedException(String paramString1, String paramString2) {
/* 82 */     super(paramString2);
/* 83 */     this.className = paramString1;
/*    */   }
/*    */   
/*    */   public String className() {
/* 87 */     return this.className;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ClassNotLoadedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */