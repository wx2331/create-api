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
/*    */ public class Or
/*    */   extends BinaryExpr
/*    */ {
/*    */   protected Or(Expression paramExpression1, Expression paramExpression2) {
/* 47 */     super("|", paramExpression1, paramExpression2);
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
/* 59 */         String[] arrayOfString = { Util.getMessage("EvaluationException.or"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 66 */       BigInteger bigInteger1 = toUnsigned((BigInteger)number1);
/* 67 */       BigInteger bigInteger2 = toUnsigned((BigInteger)number2);
/* 68 */       value(coerceToTarget(bigInteger1.or(bigInteger2)));
/*    */     
/*    */     }
/* 71 */     catch (ClassCastException classCastException) {
/*    */       
/* 73 */       String[] arrayOfString = { Util.getMessage("EvaluationException.or"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 74 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */     } 
/* 76 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Or.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */