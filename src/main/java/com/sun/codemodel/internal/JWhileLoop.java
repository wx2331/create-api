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
/*    */ public class JWhileLoop
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression test;
/* 43 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JWhileLoop(JExpression test) {
/* 49 */     this.test = test;
/*    */   }
/*    */   
/*    */   public JExpression test() {
/* 53 */     return this.test;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 57 */     if (this.body == null) this.body = new JBlock(); 
/* 58 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 62 */     if (JOp.hasTopOp(this.test)) {
/* 63 */       f.p("while ").g(this.test);
/*    */     } else {
/* 65 */       f.p("while (").g(this.test).p(')');
/*    */     } 
/* 67 */     if (this.body != null) {
/* 68 */       f.s(this.body);
/*    */     } else {
/* 70 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JWhileLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */