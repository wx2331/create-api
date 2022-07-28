/*    */ package com.sun.tools.javac.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PropagatedException
/*    */   extends RuntimeException
/*    */ {
/*    */   static final long serialVersionUID = -6065309339888775367L;
/*    */   
/*    */   public PropagatedException(RuntimeException paramRuntimeException) {
/* 43 */     super(paramRuntimeException);
/*    */   }
/*    */ 
/*    */   
/*    */   public RuntimeException getCause() {
/* 48 */     return (RuntimeException)super.getCause();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\PropagatedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */