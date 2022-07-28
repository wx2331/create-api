/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractTypeImpl
/*     */   implements Type
/*     */ {
/*     */   protected final DocEnv env;
/*     */   protected final Type type;
/*     */   
/*     */   protected AbstractTypeImpl(DocEnv paramDocEnv, Type paramType) {
/*  52 */     this.env = paramDocEnv;
/*  53 */     this.type = paramType;
/*     */   }
/*     */   
/*     */   public String typeName() {
/*  57 */     return this.type.tsym.name.toString();
/*     */   }
/*     */   
/*     */   public String qualifiedTypeName() {
/*  61 */     return this.type.tsym.getQualifiedName().toString();
/*     */   }
/*     */   
/*     */   public Type getElementType() {
/*  65 */     return null;
/*     */   }
/*     */   
/*     */   public String simpleTypeName() {
/*  69 */     return this.type.tsym.name.toString();
/*     */   }
/*     */   
/*     */   public String name() {
/*  73 */     return typeName();
/*     */   }
/*     */   
/*     */   public String qualifiedName() {
/*  77 */     return qualifiedTypeName();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  81 */     return qualifiedTypeName();
/*     */   }
/*     */   
/*     */   public String dimension() {
/*  85 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isPrimitive() {
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public ClassDoc asClassDoc() {
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public TypeVariable asTypeVariable() {
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public WildcardType asWildcardType() {
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public ParameterizedType asParameterizedType() {
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotationTypeDoc asAnnotationTypeDoc() {
/* 109 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotatedType asAnnotatedType() {
/* 113 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\AbstractTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */