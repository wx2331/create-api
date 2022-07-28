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
/*    */ public class BooleanNot
/*    */   extends UnaryExpr
/*    */ {
/*    */   protected BooleanNot(Expression paramExpression) {
/* 47 */     super("!", paramExpression);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object evaluate() throws EvaluationException {
/*    */     try {
/*    */       Boolean bool;
/* 54 */       Object object = operand().evaluate();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 60 */       if (object instanceof Number) {
/*    */         
/* 62 */         if (object instanceof BigInteger) {
/* 63 */           bool = new Boolean((((BigInteger)object).compareTo(zero) != 0));
/*    */         } else {
/* 65 */           bool = new Boolean((((Number)object).longValue() != 0L));
/*    */         } 
/*    */       } else {
/* 68 */         bool = (Boolean)object;
/*    */       } 
/* 70 */       value(new Boolean(!bool.booleanValue()));
/*    */     }
/* 72 */     catch (ClassCastException classCastException) {
/*    */       
/* 74 */       String[] arrayOfString = { Util.getMessage("EvaluationException.booleanNot"), operand().value().getClass().getName() };
/* 75 */       throw new EvaluationException(Util.getMessage("EvaluationException.2", arrayOfString));
/*    */     } 
/* 77 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\BooleanNot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */