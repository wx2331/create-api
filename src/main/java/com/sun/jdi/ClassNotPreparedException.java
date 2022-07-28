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
/*    */ @Exported
/*    */ public class ClassNotPreparedException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -6120698967144079642L;
/*    */   
/*    */   public ClassNotPreparedException() {}
/*    */   
/*    */   public ClassNotPreparedException(String paramString) {
/* 45 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ClassNotPreparedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */