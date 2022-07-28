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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BinaryExpr
/*    */   extends Expression
/*    */ {
/*    */   private String _op;
/*    */   private Expression _left;
/*    */   private Expression _right;
/*    */   
/*    */   public BinaryExpr(String paramString, Expression paramExpression1, Expression paramExpression2) {
/* 61 */     this._op = "";
/* 62 */     this._left = null;
/* 63 */     this._right = null;
/*    */     this._op = paramString;
/*    */     this._left = paramExpression1;
/*    */     this._right = paramExpression2;
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
/*    */   public void left(Expression paramExpression) {
/*    */     this._left = paramExpression;
/*    */   }
/*    */   
/*    */   public Expression left() {
/*    */     return this._left;
/*    */   }
/*    */   
/*    */   public void right(Expression paramExpression) {
/*    */     this._right = paramExpression;
/*    */   }
/*    */   
/*    */   public Expression right() {
/*    */     return this._right;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\BinaryExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */