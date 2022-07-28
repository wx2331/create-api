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
/*    */ public class BooleanAnd
/*    */   extends BinaryExpr
/*    */ {
/*    */   protected BooleanAnd(Expression paramExpression1, Expression paramExpression2) {
/* 47 */     super("&&", paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object evaluate() throws EvaluationException {
/*    */     try {
/*    */       Boolean bool1, bool2;
/* 54 */       Object object1 = left().evaluate();
/* 55 */       Object object2 = right().evaluate();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 63 */       if (object1 instanceof Number) {
/*    */         
/* 65 */         if (object1 instanceof BigInteger) {
/* 66 */           bool1 = new Boolean((((BigInteger)object1).compareTo(BigInteger.valueOf(0L)) != 0));
/*    */         } else {
/* 68 */           bool1 = new Boolean((((Number)object1).longValue() != 0L));
/*    */         } 
/*    */       } else {
/* 71 */         bool1 = (Boolean)object1;
/*    */       } 
/*    */ 
/*    */ 
/*    */       
/* 76 */       if (object2 instanceof Number) {
/*    */         
/* 78 */         if (object2 instanceof BigInteger) {
/* 79 */           bool2 = new Boolean((((BigInteger)object2).compareTo(zero) != 0));
/*    */         } else {
/* 81 */           bool2 = new Boolean((((Number)object2).longValue() != 0L));
/*    */         } 
/*    */       } else {
/* 84 */         bool2 = (Boolean)object2;
/*    */       } 
/* 86 */       value(new Boolean((bool1.booleanValue() && bool2.booleanValue())));
/*    */     }
/* 88 */     catch (ClassCastException classCastException) {
/*    */       
/* 90 */       String[] arrayOfString = { Util.getMessage("EvaluationException.booleanAnd"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 91 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*    */     } 
/* 93 */     return value();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\BooleanAnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */