/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.Collections;
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
/*     */ final class JArrayClass
/*     */   extends JClass
/*     */ {
/*     */   private final JType componentType;
/*     */   
/*     */   JArrayClass(JCodeModel owner, JType component) {
/*  45 */     super(owner);
/*  46 */     this.componentType = component;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  51 */     return this.componentType.name() + "[]";
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  55 */     return this.componentType.fullName() + "[]";
/*     */   }
/*     */   
/*     */   public String binaryName() {
/*  59 */     return this.componentType.binaryName() + "[]";
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/*  63 */     f.g(this.componentType).p("[]");
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/*  67 */     return owner().rootPackage();
/*     */   }
/*     */   
/*     */   public JClass _extends() {
/*  71 */     return owner().ref(Object.class);
/*     */   }
/*     */   
/*     */   public Iterator<JClass> _implements() {
/*  75 */     return Collections.<JClass>emptyList().iterator();
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   public JType elementType() {
/*  87 */     return this.componentType;
/*     */   }
/*     */   
/*     */   public boolean isArray() {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 100 */     if (!(obj instanceof JArrayClass)) return false;
/*     */     
/* 102 */     if (this.componentType.equals(((JArrayClass)obj).componentType)) {
/* 103 */       return true;
/*     */     }
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 109 */     return this.componentType.hashCode();
/*     */   }
/*     */   
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 113 */     if (this.componentType.isPrimitive()) {
/* 114 */       return this;
/*     */     }
/* 116 */     JClass c = ((JClass)this.componentType).substituteParams(variables, bindings);
/* 117 */     if (c == this.componentType) {
/* 118 */       return this;
/*     */     }
/* 120 */     return new JArrayClass(owner(), c);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JArrayClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */