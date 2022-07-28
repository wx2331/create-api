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
/*    */ public class InvalidStackFrameException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -1919378296505827922L;
/*    */   
/*    */   public InvalidStackFrameException() {}
/*    */   
/*    */   public InvalidStackFrameException(String paramString) {
/* 43 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\InvalidStackFrameException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */