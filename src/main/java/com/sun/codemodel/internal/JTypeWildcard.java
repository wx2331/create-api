/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ final class JTypeWildcard
/*     */   extends JClass
/*     */ {
/*     */   private final JClass bound;
/*     */   
/*     */   JTypeWildcard(JClass bound) {
/*  51 */     super(bound.owner());
/*  52 */     this.bound = bound;
/*     */   }
/*     */   
/*     */   public String name() {
/*  56 */     return "? extends " + this.bound.name();
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  60 */     return "? extends " + this.bound.fullName();
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass _extends() {
/*  74 */     if (this.bound != null) {
/*  75 */       return this.bound;
/*     */     }
/*  77 */     return owner().ref(Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JClass> _implements() {
/*  84 */     return this.bound._implements();
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/*  96 */     JClass nb = this.bound.substituteParams(variables, bindings);
/*  97 */     if (nb == this.bound) {
/*  98 */       return this;
/*     */     }
/* 100 */     return new JTypeWildcard(nb);
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 104 */     if (this.bound._extends() == null) {
/* 105 */       f.p("?");
/*     */     } else {
/* 107 */       f.p("? extends").g(this.bound);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JTypeWildcard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */