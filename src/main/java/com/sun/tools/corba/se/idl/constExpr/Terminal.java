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
/*     */ public class Terminal
/*     */   extends Expression
/*     */ {
/*     */   protected Terminal(String paramString, Character paramCharacter, boolean paramBoolean) {
/*  61 */     rep(paramString);
/*  62 */     value(paramCharacter);
/*  63 */     if (paramBoolean) {
/*  64 */       type("wchar");
/*     */     } else {
/*  66 */       type("char");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Terminal(String paramString, Boolean paramBoolean) {
/*  71 */     rep(paramString);
/*  72 */     value(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Terminal(String paramString, BigInteger paramBigInteger) {
/*  78 */     rep(paramString);
/*  79 */     value(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Terminal(String paramString, Long paramLong) {
/*  84 */     long l = paramLong.longValue();
/*  85 */     rep(paramString);
/*  86 */     if (l > 2147483647L || l < -2147483648L) {
/*  87 */       value(paramLong);
/*     */     } else {
/*  89 */       value(new Integer(paramLong.intValue()));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Terminal(String paramString, Double paramDouble) {
/*  94 */     rep(paramString);
/*  95 */     value(paramDouble);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Terminal(String paramString, boolean paramBoolean) {
/* 100 */     rep(paramString);
/* 101 */     value(paramString);
/* 102 */     if (paramBoolean) {
/* 103 */       type("wstring");
/*     */     } else {
/* 105 */       type("string");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Terminal(ConstEntry paramConstEntry) {
/* 110 */     rep(paramConstEntry.fullName());
/* 111 */     value(paramConstEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object evaluate() throws EvaluationException {
/* 117 */     if (value() instanceof ConstEntry) {
/* 118 */       return ((ConstEntry)value()).value().evaluate();
/*     */     }
/* 120 */     return value();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\Terminal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */