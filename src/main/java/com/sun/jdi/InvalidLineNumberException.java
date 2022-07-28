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
/*    */ public class InvalidLineNumberException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 4048709912372692875L;
/*    */   
/*    */   public InvalidLineNumberException() {}
/*    */   
/*    */   public InvalidLineNumberException(String paramString) {
/* 46 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\InvalidLineNumberException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */