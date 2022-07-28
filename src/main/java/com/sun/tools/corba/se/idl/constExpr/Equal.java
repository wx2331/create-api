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
/*    */ public class Equal
/*    */   extends BinaryExpr
/*    */ {
/*    */   protected Equal(Expression paramExpression1, Expression paramExpression2) {
/* 47 */     super("==", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object evaluate() throws EvaluationException {
/*    */     try {
/* 54 */       Object object = left().evaluate();
/* 55 */       if (object instanceof Boolean) {
/*    */         
/* 57 */         Boolean bool1 = (Boolean)object;
/* 58 */         Boolean bool2 = (Boolean)right().evaluate();
/* 59 */         value(new Boolean((bool1.booleanValue() == bool2.booleanValue())));
/*    */       }
/*    */       else {
/*    */         
/* 63 */         Number number1 = (Number)object;
/* 64 */         Number number2 = (Number)right().evaluate();
/* 65 */         if (number1 instanceof Float || number1 instanceof Double || number2 instanceof Float || number2 instanceof Double) {
/* 66 */           value(new Boolean((number1.doubleValue() == number2.doubleValue())));
/*    */         } else {
/*    */           
/* 69 */           value(new Boolean(((BigInteger)number1).equals(number2)));
/*    */         } 
/*    */       } 
/* 72 */     } catch (ClassCastException classCastException) {
/*    */       
/* 74 */       String[] arrayOfString = { Util.getMessage("EvaluationException.equal"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 75 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */     } 
/* 77 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Equal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */