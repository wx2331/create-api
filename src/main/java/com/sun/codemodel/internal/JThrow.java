/*    */ package com.sun.codemodel.internal;
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
/*    */ class JThrow
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression expr;
/*    */   
/*    */   JThrow(JExpression expr) {
/* 47 */     this.expr = expr;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 51 */     f.p("throw");
/* 52 */     f.g(this.expr);
/* 53 */     f.p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JThrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */