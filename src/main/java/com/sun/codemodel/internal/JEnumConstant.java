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
/*     */ public final class JEnumConstant
/*     */   extends JExpressionImpl
/*     */   implements JDeclaration, JAnnotatable, JDocCommentable
/*     */ {
/*     */   private final String name;
/*     */   private final JDefinedClass type;
/*  55 */   private JDocComment jdoc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private List<JExpression> args = null;
/*     */   
/*     */   JEnumConstant(JDefinedClass type, String name) {
/*  70 */     this.name = name;
/*  71 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JEnumConstant arg(JExpression arg) {
/*  81 */     if (arg == null) throw new IllegalArgumentException(); 
/*  82 */     if (this.args == null)
/*  83 */       this.args = new ArrayList<>(); 
/*  84 */     this.args.add(arg);
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  94 */     return this.type.fullName().concat(".").concat(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 103 */     if (this.jdoc == null)
/* 104 */       this.jdoc = new JDocComment(this.type.owner()); 
/* 105 */     return this.jdoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 114 */     if (this.annotations == null)
/* 115 */       this.annotations = new ArrayList<>(); 
/* 116 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 117 */     this.annotations.add(a);
/* 118 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 128 */     return annotate(this.type.owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 132 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<JAnnotationUse> annotations() {
/* 139 */     if (this.annotations == null)
/* 140 */       this.annotations = new ArrayList<>(); 
/* 141 */     return Collections.unmodifiableList(this.annotations);
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 145 */     if (this.jdoc != null)
/* 146 */       f.nl().g(this.jdoc); 
/* 147 */     if (this.annotations != null)
/* 148 */       for (int i = 0; i < this.annotations.size(); i++) {
/* 149 */         f.g(this.annotations.get(i)).nl();
/*     */       } 
/* 151 */     f.id(this.name);
/* 152 */     if (this.args != null) {
/* 153 */       f.p('(').g((Collection)this.args).p(')');
/*     */     }
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 158 */     f.t(this.type).p('.').p(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JEnumConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */