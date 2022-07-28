/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class UnaryExpr
/*    */   extends Expression
/*    */ {
/*    */   private String _op;
/*    */   private Expression _operand;
/*    */   
/*    */   public UnaryExpr(String paramString, Expression paramExpression) {
/* 57 */     this._op = "";
/* 58 */     this._operand = null;
/*    */     this._op = paramString;
/*    */     this._operand = paramExpression;
/*    */   }
/*    */   
/*    */   public void op(String paramString) {
/*    */     this._op = (paramString == null) ? "" : paramString;
/*    */   }
/*    */   
/*    */   public String op() {
/*    */     return this._op;
/*    */   }
/*    */   
/*    */   public void operand(Expression paramExpression) {
/*    */     this._operand = paramExpression;
/*    */   }
/*    */   
/*    */   public Expression operand() {
/*    */     return this._operand;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\UnaryExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */