/*    */ package com.sun.tools.javap;
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
/*    */   private static final long serialVersionUID = 8114054446416187030L;
/*    */   public final Object[] args;
/*    */   
/*    */   InternalError(Throwable paramThrowable, Object... paramVarArgs) {
/* 37 */     super("Internal error", paramThrowable);
/* 38 */     this.args = paramVarArgs;
/*    */   }
/*    */   
/*    */   InternalError(Object... paramVarArgs) {
/* 42 */     super("Internal error");
/* 43 */     this.args = paramVarArgs;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\InternalError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */