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
/*    */ public class Not
/*    */   extends UnaryExpr
/*    */ {
/*    */   protected Not(Expression paramExpression) {
/* 47 */     super("~", paramExpression);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object evaluate() throws EvaluationException {
/*    */     try {
/* 54 */       Number number = (Number)operand().evaluate();
/*    */       
/* 56 */       if (number instanceof Float || number instanceof Double) {
/*    */         
/* 58 */         String[] arrayOfString = { Util.getMessage("EvaluationException.not"), operand().value().getClass().getName() };
/* 59 */         throw new EvaluationException(Util.getMessage("EvaluationException.2", arrayOfString));
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 65 */       BigInteger bigInteger = (BigInteger)coerceToTarget(number);
/*    */ 
/*    */       
/* 68 */       if (type().equals("short") || type().equals("long") || type().equals("long long")) {
/* 69 */         value(bigInteger.add(one).multiply(negOne));
/* 70 */       } else if (type().equals("unsigned short")) {
/*    */         
/* 72 */         value(twoPow16.subtract(one).subtract(bigInteger));
/* 73 */       } else if (type().equals("unsigned long")) {
/* 74 */         value(twoPow32.subtract(one).subtract(bigInteger));
/* 75 */       } else if (type().equals("unsigned long long")) {
/* 76 */         value(twoPow64.subtract(one).subtract(bigInteger));
/*    */       } else {
/* 78 */         value(bigInteger.not());
/*    */       }
/*    */     
/* 81 */     } catch (ClassCastException classCastException) {
/*    */       
/* 83 */       String[] arrayOfString = { Util.getMessage("EvaluationException.not"), operand().value().getClass().getName() };
/* 84 */       throw new EvaluationException(Util.getMessage("EvaluationException.2", arrayOfString));
/*    */     } 
/* 86 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Not.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */