/*    */ package com.sun.tools.corba.se.idl.constExpr;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.Util;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ShiftLeft
/*    */   extends BinaryExpr
/*    */ {
/*    */   protected ShiftLeft(Expression paramExpression1, Expression paramExpression2) {
/* 47 */     super("<<", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object evaluate() throws EvaluationException {
/*    */     try {
/* 54 */       Number number1 = (Number)left().evaluate();
/* 55 */       Number number2 = (Number)right().evaluate();
/*    */       
/* 57 */       if (number1 instanceof Float || number1 instanceof Double || number2 instanceof Float || number2 instanceof Double) {
/*    */         
/* 59 */         String[] arrayOfString = { Util.getMessage("EvaluationException.left"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 66 */       BigInteger bigInteger1 = (BigInteger)coerceToTarget(number1);
/* 67 */       BigInteger bigInteger2 = (BigInteger)number2;
/*    */       
/* 69 */       BigInteger bigInteger3 = bigInteger1.shiftLeft(bigInteger2.intValue());
/*    */       
/* 71 */       if (type().indexOf("short") >= 0) {
/* 72 */         bigInteger3 = bigInteger3.mod(twoPow16);
/* 73 */       } else if (type().indexOf("long") >= 0) {
/* 74 */         bigInteger3 = bigInteger3.mod(twoPow32);
/* 75 */       } else if (type().indexOf("long long") >= 0) {
/* 76 */         bigInteger3 = bigInteger3.mod(twoPow64);
/*    */       } 
/* 78 */       value(coerceToTarget(bigInteger3));
/*    */     
/*    */     }
/* 81 */     catch (ClassCastException classCastException) {
/*    */       
/* 83 */       String[] arrayOfString = { Util.getMessage("EvaluationException.left"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 84 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */     } 
/* 86 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\ShiftLeft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */