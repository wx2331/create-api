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
/*    */ public class Positive
/*    */   extends UnaryExpr
/*    */ {
/*    */   protected Positive(Expression paramExpression) {
/* 47 */     super("+", paramExpression);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object evaluate() throws EvaluationException {
/*    */     try {
/* 54 */       Number number = (Number)operand().evaluate();
/*    */       
/* 56 */       if (number instanceof Float || number instanceof Double) {
/* 57 */         value(new Double(number.doubleValue()));
/*    */       
/*    */       }
/*    */       else {
/*    */         
/* 62 */         value(((BigInteger)number).multiply(BigInteger.valueOf(((BigInteger)number).signum())));
/*    */       }
/*    */     
/*    */     }
/* 66 */     catch (ClassCastException classCastException) {
/*    */       
/* 68 */       String[] arrayOfString = { Util.getMessage("EvaluationException.pos"), operand().value().getClass().getName() };
/* 69 */       throw new EvaluationException(Util.getMessage("EvaluationException.2", arrayOfString));
/*    */     } 
/* 71 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Positive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */