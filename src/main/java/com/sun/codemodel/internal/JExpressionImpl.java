/*     */ package com.sun.codemodel.internal;
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
/*     */ public abstract class JExpressionImpl
/*     */   implements JExpression
/*     */ {
/*     */   public final JExpression minus() {
/*  39 */     return JOp.minus(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JExpression not() {
/*  46 */     return JOp.not(this);
/*     */   }
/*     */   
/*     */   public final JExpression complement() {
/*  50 */     return JOp.complement(this);
/*     */   }
/*     */   
/*     */   public final JExpression incr() {
/*  54 */     return JOp.incr(this);
/*     */   }
/*     */   
/*     */   public final JExpression decr() {
/*  58 */     return JOp.decr(this);
/*     */   }
/*     */   
/*     */   public final JExpression plus(JExpression right) {
/*  62 */     return JOp.plus(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression minus(JExpression right) {
/*  66 */     return JOp.minus(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression mul(JExpression right) {
/*  70 */     return JOp.mul(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression div(JExpression right) {
/*  74 */     return JOp.div(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression mod(JExpression right) {
/*  78 */     return JOp.mod(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shl(JExpression right) {
/*  82 */     return JOp.shl(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shr(JExpression right) {
/*  86 */     return JOp.shr(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression shrz(JExpression right) {
/*  90 */     return JOp.shrz(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression band(JExpression right) {
/*  94 */     return JOp.band(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression bor(JExpression right) {
/*  98 */     return JOp.bor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression cand(JExpression right) {
/* 102 */     return JOp.cand(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression cor(JExpression right) {
/* 106 */     return JOp.cor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression xor(JExpression right) {
/* 110 */     return JOp.xor(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression lt(JExpression right) {
/* 114 */     return JOp.lt(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression lte(JExpression right) {
/* 118 */     return JOp.lte(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression gt(JExpression right) {
/* 122 */     return JOp.gt(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression gte(JExpression right) {
/* 126 */     return JOp.gte(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression eq(JExpression right) {
/* 130 */     return JOp.eq(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression ne(JExpression right) {
/* 134 */     return JOp.ne(this, right);
/*     */   }
/*     */   
/*     */   public final JExpression _instanceof(JType right) {
/* 138 */     return JOp._instanceof(this, right);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JInvocation invoke(JMethod method) {
/* 147 */     return JExpr.invoke(this, method);
/*     */   }
/*     */   
/*     */   public final JInvocation invoke(String method) {
/* 151 */     return JExpr.invoke(this, method);
/*     */   }
/*     */   
/*     */   public final JFieldRef ref(JVar field) {
/* 155 */     return JExpr.ref(this, field);
/*     */   }
/*     */   
/*     */   public final JFieldRef ref(String field) {
/* 159 */     return JExpr.ref(this, field);
/*     */   }
/*     */   
/*     */   public final JArrayCompRef component(JExpression index) {
/* 163 */     return JExpr.component(this, index);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JExpressionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */