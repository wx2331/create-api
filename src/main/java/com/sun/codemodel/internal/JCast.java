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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class JCast
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private final JType type;
/*    */   private final JExpression object;
/*    */   
/*    */   JCast(JType type, JExpression object) {
/* 55 */     this.type = type;
/* 56 */     this.object = object;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 60 */     f.p("((").g(this.type).p(')').g(this.object).p(')');
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JCast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */