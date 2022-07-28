/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ParameterImpl
/*     */   implements Parameter
/*     */ {
/*     */   private final DocEnv env;
/*     */   private final Symbol.VarSymbol sym;
/*     */   private final Type type;
/*     */   
/*     */   ParameterImpl(DocEnv paramDocEnv, Symbol.VarSymbol paramVarSymbol) {
/*  56 */     this.env = paramDocEnv;
/*  57 */     this.sym = paramVarSymbol;
/*  58 */     this.type = TypeMaker.getType(paramDocEnv, paramVarSymbol.type, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type type() {
/*  65 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  73 */     return this.sym.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String typeName() {
/*  81 */     return (this.type instanceof com.sun.javadoc.ClassDoc || this.type instanceof com.sun.javadoc.TypeVariable) ? this.type
/*  82 */       .typeName() : this.type
/*  83 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  94 */     return typeName() + " " + this.sym;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationDesc[] annotations() {
/* 102 */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[this.sym.getRawAttributes().length()];
/* 103 */     byte b = 0;
/* 104 */     for (Attribute.Compound compound : this.sym.getRawAttributes()) {
/* 105 */       arrayOfAnnotationDesc[b++] = new AnnotationDescImpl(this.env, compound);
/*     */     }
/* 107 */     return arrayOfAnnotationDesc;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ParameterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */