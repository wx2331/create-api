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
/*    */ public class InvalidTypeException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 2256667231949650806L;
/*    */   
/*    */   public InvalidTypeException() {}
/*    */   
/*    */   public InvalidTypeException(String paramString) {
/* 44 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\InvalidTypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */