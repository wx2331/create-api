/*     */ package com.sun.tools.corba.se.idl.constExpr;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Util;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Divide
/*     */   extends BinaryExpr
/*     */ {
/*     */   protected Divide(Expression paramExpression1, Expression paramExpression2) {
/*  57 */     super("/", paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object evaluate() throws EvaluationException {
/*     */     try {
/*  67 */       Number number1 = (Number)left().evaluate();
/*  68 */       Number number2 = (Number)right().evaluate();
/*     */       
/*  70 */       boolean bool1 = (number1 instanceof Float || number1 instanceof Double) ? true : false;
/*  71 */       boolean bool2 = (number2 instanceof Float || number2 instanceof Double) ? true : false;
/*     */       
/*  73 */       if (bool1 && bool2)
/*  74 */       { value(new Double(number1.doubleValue() / number2.doubleValue())); }
/*  75 */       else { if (bool1 || bool2) {
/*     */ 
/*     */ 
/*     */           
/*  79 */           String[] arrayOfString = { Util.getMessage("EvaluationException.divide"), left().value().getClass().getName(), right().value().getClass().getName() };
/*  80 */           throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*     */         } 
/*     */ 
/*     */         
/*  84 */         BigInteger bigInteger1 = (BigInteger)number1, bigInteger2 = (BigInteger)number2;
/*  85 */         value(bigInteger1.divide(bigInteger2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */          }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 125 */     catch (ClassCastException classCastException) {
/*     */       
/* 127 */       String[] arrayOfString = { Util.getMessage("EvaluationException.divide"), left().value().getClass().getName(), right().value().getClass().getName() };
/* 128 */       throw new EvaluationException(Util.getMessage("EvaluationException.1", arrayOfString));
/*     */     } 
/* 130 */     return value();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Divide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */