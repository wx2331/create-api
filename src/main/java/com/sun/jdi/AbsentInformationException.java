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
/*    */ public class AbsentInformationException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 4988939309582416373L;
/*    */   
/*    */   public AbsentInformationException() {}
/*    */   
/*    */   public AbsentInformationException(String paramString) {
/* 45 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\AbsentInformationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */