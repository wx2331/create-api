/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JVar
/*     */   extends JExpressionImpl
/*     */   implements JDeclaration, JAssignmentTarget, JAnnotatable
/*     */ {
/*     */   private JMods mods;
/*     */   private JType type;
/*     */   private String name;
/*     */   private JExpression init;
/*  64 */   private List<JAnnotationUse> annotations = null;
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
/*     */   JVar(JMods mods, JType type, String name, JExpression init) {
/*  81 */     this.mods = mods;
/*  82 */     this.type = type;
/*  83 */     this.name = name;
/*  84 */     this.init = init;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar init(JExpression init) {
/*  95 */     this.init = init;
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 105 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void name(String name) {
/* 112 */     if (!JJavaName.isJavaIdentifier(name))
/* 113 */       throw new IllegalArgumentException(); 
/* 114 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType type() {
/* 123 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods mods() {
/* 132 */     return this.mods;
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
/*     */   public JType type(JType newType) {
/* 145 */     JType r = this.type;
/* 146 */     if (newType == null)
/* 147 */       throw new IllegalArgumentException(); 
/* 148 */     this.type = newType;
/* 149 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 159 */     if (this.annotations == null)
/* 160 */       this.annotations = new ArrayList<>(); 
/* 161 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 162 */     this.annotations.add(a);
/* 163 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 173 */     return annotate(this.type.owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 177 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */   
/*     */   public Collection<JAnnotationUse> annotations() {
/* 181 */     if (this.annotations == null)
/* 182 */       this.annotations = new ArrayList<>(); 
/* 183 */     return Collections.unmodifiableList(this.annotations);
/*     */   }
/*     */   
/*     */   protected boolean isAnnotated() {
/* 187 */     return (this.annotations != null);
/*     */   }
/*     */   
/*     */   public void bind(JFormatter f) {
/* 191 */     if (this.annotations != null)
/* 192 */       for (int i = 0; i < this.annotations.size(); i++) {
/* 193 */         f.g(this.annotations.get(i)).nl();
/*     */       } 
/* 195 */     f.g(this.mods).g(this.type).id(this.name);
/* 196 */     if (this.init != null)
/* 197 */       f.p('=').g(this.init); 
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 201 */     f.b(this).p(';').nl();
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 205 */     f.id(this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression assign(JExpression rhs) {
/* 210 */     return JExpr.assign(this, rhs);
/*     */   }
/*     */   public JExpression assignPlus(JExpression rhs) {
/* 213 */     return JExpr.assignPlus(this, rhs);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JVar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */