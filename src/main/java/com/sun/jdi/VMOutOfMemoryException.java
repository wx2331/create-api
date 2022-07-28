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
/*    */ @Exported
/*    */ public class VMOutOfMemoryException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 71504228548910686L;
/*    */   
/*    */   public VMOutOfMemoryException() {}
/*    */   
/*    */   public VMOutOfMemoryException(String paramString) {
/* 43 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\VMOutOfMemoryException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */