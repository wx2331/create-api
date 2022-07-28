/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PrimitiveType
/*     */   implements Type
/*     */ {
/*     */   private final String name;
/*  40 */   static final PrimitiveType voidType = new PrimitiveType("void");
/*  41 */   static final PrimitiveType booleanType = new PrimitiveType("boolean");
/*  42 */   static final PrimitiveType byteType = new PrimitiveType("byte");
/*  43 */   static final PrimitiveType charType = new PrimitiveType("char");
/*  44 */   static final PrimitiveType shortType = new PrimitiveType("short");
/*  45 */   static final PrimitiveType intType = new PrimitiveType("int");
/*  46 */   static final PrimitiveType longType = new PrimitiveType("long");
/*  47 */   static final PrimitiveType floatType = new PrimitiveType("float");
/*  48 */   static final PrimitiveType doubleType = new PrimitiveType("double");
/*     */ 
/*     */   
/*  51 */   static final PrimitiveType errorType = new PrimitiveType("");
/*     */   
/*     */   PrimitiveType(String paramString) {
/*  54 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String typeName() {
/*  63 */     return this.name;
/*     */   }
/*     */   
/*     */   public Type getElementType() {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String qualifiedTypeName() {
/*  77 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String simpleTypeName() {
/*  84 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dimension() {
/*  93 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc asClassDoc() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeDoc asAnnotationTypeDoc() {
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedType asParameterizedType() {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeVariable asTypeVariable() {
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardType asWildcardType() {
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedType asAnnotatedType() {
/* 138 */     return null;
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
/*     */   public String toString() {
/* 152 */     return qualifiedTypeName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimitive() {
/* 159 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\PrimitiveType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */