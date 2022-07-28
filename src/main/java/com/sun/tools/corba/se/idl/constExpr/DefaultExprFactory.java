/*     */ package com.sun.tools.corba.se.idl.constExpr;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.ConstEntry;
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
/*     */ public class DefaultExprFactory
/*     */   implements ExprFactory
/*     */ {
/*     */   public And and(Expression paramExpression1, Expression paramExpression2) {
/*  47 */     return new And(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public BooleanAnd booleanAnd(Expression paramExpression1, Expression paramExpression2) {
/*  52 */     return new BooleanAnd(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public BooleanNot booleanNot(Expression paramExpression) {
/*  57 */     return new BooleanNot(paramExpression);
/*     */   }
/*     */ 
/*     */   
/*     */   public BooleanOr booleanOr(Expression paramExpression1, Expression paramExpression2) {
/*  62 */     return new BooleanOr(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Divide divide(Expression paramExpression1, Expression paramExpression2) {
/*  67 */     return new Divide(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Equal equal(Expression paramExpression1, Expression paramExpression2) {
/*  72 */     return new Equal(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public GreaterEqual greaterEqual(Expression paramExpression1, Expression paramExpression2) {
/*  77 */     return new GreaterEqual(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public GreaterThan greaterThan(Expression paramExpression1, Expression paramExpression2) {
/*  82 */     return new GreaterThan(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public LessEqual lessEqual(Expression paramExpression1, Expression paramExpression2) {
/*  87 */     return new LessEqual(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public LessThan lessThan(Expression paramExpression1, Expression paramExpression2) {
/*  92 */     return new LessThan(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Minus minus(Expression paramExpression1, Expression paramExpression2) {
/*  97 */     return new Minus(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Modulo modulo(Expression paramExpression1, Expression paramExpression2) {
/* 102 */     return new Modulo(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Negative negative(Expression paramExpression) {
/* 107 */     return new Negative(paramExpression);
/*     */   }
/*     */ 
/*     */   
/*     */   public Not not(Expression paramExpression) {
/* 112 */     return new Not(paramExpression);
/*     */   }
/*     */ 
/*     */   
/*     */   public NotEqual notEqual(Expression paramExpression1, Expression paramExpression2) {
/* 117 */     return new NotEqual(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Or or(Expression paramExpression1, Expression paramExpression2) {
/* 122 */     return new Or(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Plus plus(Expression paramExpression1, Expression paramExpression2) {
/* 127 */     return new Plus(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Positive positive(Expression paramExpression) {
/* 132 */     return new Positive(paramExpression);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShiftLeft shiftLeft(Expression paramExpression1, Expression paramExpression2) {
/* 137 */     return new ShiftLeft(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShiftRight shiftRight(Expression paramExpression1, Expression paramExpression2) {
/* 142 */     return new ShiftRight(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Terminal terminal(String paramString, Character paramCharacter, boolean paramBoolean) {
/* 148 */     return new Terminal(paramString, paramCharacter, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public Terminal terminal(String paramString, Boolean paramBoolean) {
/* 153 */     return new Terminal(paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Terminal terminal(String paramString, BigInteger paramBigInteger) {
/* 159 */     return new Terminal(paramString, paramBigInteger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Terminal terminal(String paramString, Double paramDouble) {
/* 169 */     return new Terminal(paramString, paramDouble);
/*     */   }
/*     */ 
/*     */   
/*     */   public Terminal terminal(String paramString, boolean paramBoolean) {
/* 174 */     return new Terminal(paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public Terminal terminal(ConstEntry paramConstEntry) {
/* 179 */     return new Terminal(paramConstEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public Times times(Expression paramExpression1, Expression paramExpression2) {
/* 184 */     return new Times(paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Xor xor(Expression paramExpression1, Expression paramExpression2) {
/* 189 */     return new Xor(paramExpression1, paramExpression2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\DefaultExprFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */