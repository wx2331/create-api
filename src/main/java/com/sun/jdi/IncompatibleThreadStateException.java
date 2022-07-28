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
/*    */ public class IncompatibleThreadStateException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 6199174323414551389L;
/*    */   
/*    */   public IncompatibleThreadStateException() {}
/*    */   
/*    */   public IncompatibleThreadStateException(String paramString) {
/* 44 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\IncompatibleThreadStateException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */