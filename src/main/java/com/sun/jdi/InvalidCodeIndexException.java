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
/*    */ @Deprecated
/*    */ public class InvalidCodeIndexException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 7416010225133747805L;
/*    */   
/*    */   public InvalidCodeIndexException() {}
/*    */   
/*    */   public InvalidCodeIndexException(String paramString) {
/* 46 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\InvalidCodeIndexException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */