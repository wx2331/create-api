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
/*    */ @Exported
/*    */ public class InvocationException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 6066780907971918568L;
/*    */   ObjectReference exception;
/*    */   
/*    */   public InvocationException(ObjectReference paramObjectReference) {
/* 41 */     super("Exception occurred in target VM");
/* 42 */     this.exception = paramObjectReference;
/*    */   }
/*    */   
/*    */   public ObjectReference exception() {
/* 46 */     return this.exception;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\InvocationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */