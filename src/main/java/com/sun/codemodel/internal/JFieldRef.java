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
/*     */ public class JFieldRef
/*     */   extends JExpressionImpl
/*     */   implements JAssignmentTarget
/*     */ {
/*     */   private JGenerable object;
/*     */   private String name;
/*     */   private JVar var;
/*     */   private boolean explicitThis;
/*     */   
/*     */   JFieldRef(JExpression object, String name) {
/*  66 */     this(object, name, false);
/*     */   }
/*     */   
/*     */   JFieldRef(JExpression object, JVar v) {
/*  70 */     this(object, v, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JFieldRef(JType type, String name) {
/*  77 */     this(type, name, false);
/*     */   }
/*     */   
/*     */   JFieldRef(JType type, JVar v) {
/*  81 */     this(type, v, false);
/*     */   }
/*     */   
/*     */   JFieldRef(JGenerable object, String name, boolean explicitThis) {
/*  85 */     this.explicitThis = explicitThis;
/*  86 */     this.object = object;
/*  87 */     if (name.indexOf('.') >= 0)
/*  88 */       throw new IllegalArgumentException("Field name contains '.': " + name); 
/*  89 */     this.name = name;
/*     */   }
/*     */   
/*     */   JFieldRef(JGenerable object, JVar var, boolean explicitThis) {
/*  93 */     this.explicitThis = explicitThis;
/*  94 */     this.object = object;
/*  95 */     this.var = var;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/*  99 */     String name = this.name;
/* 100 */     if (name == null) name = this.var.name();
/*     */     
/* 102 */     if (this.object != null) {
/* 103 */       f.g(this.object).p('.').p(name);
/*     */     }
/* 105 */     else if (this.explicitThis) {
/* 106 */       f.p("this.").p(name);
/*     */     } else {
/* 108 */       f.id(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression assign(JExpression rhs) {
/* 114 */     return JExpr.assign(this, rhs);
/*     */   }
/*     */   public JExpression assignPlus(JExpression rhs) {
/* 117 */     return JExpr.assignPlus(this, rhs);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JFieldRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */