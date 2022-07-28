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
/*    */ @Exported
/*    */ public class VMMismatchException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 289169358790459564L;
/*    */   
/*    */   public VMMismatchException() {}
/*    */   
/*    */   public VMMismatchException(String paramString) {
/* 44 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\VMMismatchException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */