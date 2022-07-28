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
/*     */ public final class JTypeVar
/*     */   extends JClass
/*     */   implements JDeclaration
/*     */ {
/*     */   private final String name;
/*     */   private JClass bound;
/*     */   
/*     */   JTypeVar(JCodeModel owner, String _name) {
/*  45 */     super(owner);
/*  46 */     this.name = _name;
/*     */   }
/*     */   
/*     */   public String name() {
/*  50 */     return this.name;
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  54 */     return this.name;
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTypeVar bound(JClass c) {
/*  67 */     if (this.bound != null)
/*  68 */       throw new IllegalArgumentException("type variable has an existing class bound " + this.bound); 
/*  69 */     this.bound = c;
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass _extends() {
/*  80 */     if (this.bound != null) {
/*  81 */       return this.bound;
/*     */     }
/*  83 */     return owner().ref(Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JClass> _implements() {
/*  90 */     return this.bound._implements();
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void declare(JFormatter f) {
/* 105 */     f.id(this.name);
/* 106 */     if (this.bound != null) {
/* 107 */       f.p("extends").g(this.bound);
/*     */     }
/*     */   }
/*     */   
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 112 */     for (int i = 0; i < variables.length; i++) {
/* 113 */       if (variables[i] == this)
/* 114 */         return bindings.get(i); 
/* 115 */     }  return this;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 119 */     f.id(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JTypeVar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */