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
/*    */ public class ShiftRight
/*    */   extends BinaryExpr
/*    */ {
/*    */   protected ShiftRight(Expression paramExpression1, Expression paramExpression2) {
/* 47 */     super(">>", paramExpression1, paramExpression2);
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
/* 59 */         String[] arrayOfString = { Util.getMessage("EvaluationException.right"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 60 */         throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 66 */       BigInteger bigInteger1 = (BigInteger)coerceToTarget(number1);
/* 67 */       BigInteger bigInteger2 = (BigInteger)number2;
/*    */ 
/*    */       
/* 70 */       if (bigInteger1.signum() == -1)
/* 71 */         if (type().equals("short")) {
/* 72 */           bigInteger1 = bigInteger1.add(twoPow16);
/* 73 */         } else if (type().equals("long")) {
/* 74 */           bigInteger1 = bigInteger1.add(twoPow32);
/* 75 */         } else if (type().equals("long long")) {
/* 76 */           bigInteger1 = bigInteger1.add(twoPow64);
/*    */         }  
/* 78 */       value(bigInteger1.shiftRight(bigInteger2.intValue()));
/*    */     
/*    */     }
/* 81 */     catch (ClassCastException classCastException) {
/*    */       
/* 83 */       String[] arrayOfString = { Util.getMessage("EvaluationException.right"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 84 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */     } 
/* 86 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\ShiftRight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */