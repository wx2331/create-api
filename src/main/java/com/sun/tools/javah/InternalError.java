/*    */ package com.sun.tools.javah;
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
/*    */ public class InternalError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 8411861562497165022L;
/*    */   
/*    */   InternalError(String paramString, Throwable paramThrowable) {
/* 37 */     super("Internal error: " + paramString);
/* 38 */     initCause(paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\InternalError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */