/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ public final class JInvocation
/*     */   extends JExpressionImpl
/*     */   implements JStatement
/*     */ {
/*     */   private JGenerable object;
/*     */   private String name;
/*     */   private JMethod method;
/*     */   private boolean isConstructor = false;
/*  58 */   private List<JExpression> args = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private JType type = null;
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
/*     */   JInvocation(JExpression object, String name) {
/*  77 */     this(object, name);
/*     */   }
/*     */   
/*     */   JInvocation(JExpression object, JMethod method) {
/*  81 */     this(object, method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JInvocation(JClass type, String name) {
/*  88 */     this(type, name);
/*     */   }
/*     */   
/*     */   JInvocation(JClass type, JMethod method) {
/*  92 */     this(type, method);
/*     */   }
/*     */   
/*     */   private JInvocation(JGenerable object, String name) {
/*  96 */     this.object = object;
/*  97 */     if (name.indexOf('.') >= 0)
/*  98 */       throw new IllegalArgumentException("method name contains '.': " + name); 
/*  99 */     this.name = name;
/*     */   }
/*     */   
/*     */   private JInvocation(JGenerable object, JMethod method) {
/* 103 */     this.object = object;
/* 104 */     this.method = method;
/*     */   }
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
/*     */   JInvocation(JType c) {
/* 118 */     this.isConstructor = true;
/* 119 */     this.type = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation arg(JExpression arg) {
/* 129 */     if (arg == null) throw new IllegalArgumentException(); 
/* 130 */     this.args.add(arg);
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation arg(String v) {
/* 140 */     return arg(JExpr.lit(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression[] listArgs() {
/* 149 */     return this.args.<JExpression>toArray(new JExpression[this.args.size()]);
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 153 */     if (this.isConstructor && this.type.isArray()) {
/*     */       
/* 155 */       f.p("new").g(this.type).p('{');
/*     */     }
/* 157 */     else if (this.isConstructor) {
/* 158 */       f.p("new").g(this.type).p('(');
/*     */     } else {
/* 160 */       String name = this.name;
/* 161 */       if (name == null) name = this.method.name();
/*     */       
/* 163 */       if (this.object != null) {
/* 164 */         f.g(this.object).p('.').p(name).p('(');
/*     */       } else {
/* 166 */         f.id(name).p('(');
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     f.g((Collection)this.args);
/*     */     
/* 172 */     if (this.isConstructor && this.type.isArray()) {
/* 173 */       f.p('}');
/*     */     } else {
/* 175 */       f.p(')');
/*     */     } 
/* 177 */     if (this.type instanceof JDefinedClass && ((JDefinedClass)this.type).isAnonymous()) {
/* 178 */       ((JAnonymousClass)this.type).declareBody(f);
/*     */     }
/*     */   }
/*     */   
/*     */   public void state(JFormatter f) {
/* 183 */     f.g(this).p(';').nl();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JInvocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */