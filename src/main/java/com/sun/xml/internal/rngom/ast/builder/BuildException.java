/*    */ package com.sun.xml.internal.rngom.ast.builder;
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
/*    */ public class BuildException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final Throwable cause;
/*    */   
/*    */   public BuildException(Throwable cause) {
/* 58 */     if (cause == null)
/* 59 */       throw new NullPointerException("null cause"); 
/* 60 */     this.cause = cause;
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 64 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\ast\builder\BuildException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */