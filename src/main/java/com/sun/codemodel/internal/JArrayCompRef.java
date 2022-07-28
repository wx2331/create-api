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
/*    */ final class JArrayCompRef
/*    */   extends JExpressionImpl
/*    */   implements JAssignmentTarget
/*    */ {
/*    */   private final JExpression array;
/*    */   private final JExpression index;
/*    */   
/*    */   JArrayCompRef(JExpression array, JExpression index) {
/* 54 */     if (array == null || index == null) {
/* 55 */       throw new NullPointerException();
/*    */     }
/* 57 */     this.array = array;
/* 58 */     this.index = index;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 62 */     f.g(this.array).p('[').g(this.index).p(']');
/*    */   }
/*    */   
/*    */   public JExpression assign(JExpression rhs) {
/* 66 */     return JExpr.assign(this, rhs);
/*    */   }
/*    */   public JExpression assignPlus(JExpression rhs) {
/* 69 */     return JExpr.assignPlus(this, rhs);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JArrayCompRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */