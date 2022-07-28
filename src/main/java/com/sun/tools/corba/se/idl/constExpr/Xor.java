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
/*    */ public class Xor
/*    */   extends BinaryExpr
/*    */ {
/*    */   protected Xor(Expression paramExpression1, Expression paramExpression2) {
/* 47 */     super("^", paramExpression1, paramExpression2);
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
/* 59 */         String[] arrayOfString = { Util.getMessage("EvaluationException.xor"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 69 */       BigInteger bigInteger1 = (BigInteger)coerceToTarget(number1);
/* 70 */       BigInteger bigInteger2 = (BigInteger)coerceToTarget(number2);
/* 71 */       value(bigInteger1.xor(bigInteger2));
/*    */     
/*    */     }
/* 74 */     catch (ClassCastException classCastException) {
/*    */       
/* 76 */       String[] arrayOfString = { Util.getMessage("EvaluationException.xor"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 77 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */     } 
/* 79 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Xor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */