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
/*    */ public class JAssignment
/*    */   extends JExpressionImpl
/*    */   implements JStatement
/*    */ {
/*    */   JAssignmentTarget lhs;
/*    */   JExpression rhs;
/* 36 */   String op = "";
/*    */   
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs) {
/* 39 */     this.lhs = lhs;
/* 40 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   JAssignment(JAssignmentTarget lhs, JExpression rhs, String op) {
/* 44 */     this.lhs = lhs;
/* 45 */     this.rhs = rhs;
/* 46 */     this.op = op;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 50 */     f.g(this.lhs).p(this.op + '=').g(this.rhs);
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 54 */     f.g(this).p(';').nl();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAssignment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */