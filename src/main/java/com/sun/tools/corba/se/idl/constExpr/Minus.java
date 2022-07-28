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
/*    */ public class Minus
/*    */   extends BinaryExpr
/*    */ {
/*    */   protected Minus(Expression paramExpression1, Expression paramExpression2) {
/* 47 */     super("-", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object evaluate() throws EvaluationException {
/*    */     try {
/* 54 */       Number number1 = (Number)left().evaluate();
/* 55 */       Number number2 = (Number)right().evaluate();
/*    */       
/* 57 */       boolean bool1 = (number1 instanceof Float || number1 instanceof Double) ? true : false;
/* 58 */       boolean bool2 = (number2 instanceof Float || number2 instanceof Double) ? true : false;
/*    */       
/* 60 */       if (bool1 && bool2)
/* 61 */       { value(new Double(number1.doubleValue() - number2.doubleValue())); }
/* 62 */       else { if (bool1 || bool2) {
/*    */           
/* 64 */           String[] arrayOfString = { Util.getMessage("EvaluationException.minus"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 65 */           throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */         } 
/*    */ 
/*    */ 
/*    */         
/* 70 */         BigInteger bigInteger1 = (BigInteger)number1, bigInteger2 = (BigInteger)number2;
/* 71 */         value(bigInteger1.subtract(bigInteger2)); }
/*    */ 
/*    */     
/*    */     }
/* 75 */     catch (ClassCastException classCastException) {
/*    */       
/* 77 */       String[] arrayOfString = { Util.getMessage("EvaluationException.minus"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 78 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */     } 
/* 80 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Minus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */