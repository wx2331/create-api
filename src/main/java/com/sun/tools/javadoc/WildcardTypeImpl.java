/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WildcardTypeImpl
/*     */   extends AbstractTypeImpl
/*     */   implements WildcardType
/*     */ {
/*     */   WildcardTypeImpl(DocEnv paramDocEnv, Type.WildcardType paramWildcardType) {
/*  50 */     super(paramDocEnv, (Type)paramWildcardType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] extendsBounds() {
/*  59 */     return TypeMaker.getTypes(this.env, getExtendsBounds((Type.WildcardType)this.type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] superBounds() {
/*  68 */     return TypeMaker.getTypes(this.env, getSuperBounds((Type.WildcardType)this.type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc asClassDoc() {
/*  76 */     return this.env.getClassDoc((Symbol.ClassSymbol)(this.env.types.erasure(this.type)).tsym);
/*     */   }
/*     */ 
/*     */   
/*     */   public WildcardType asWildcardType() {
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public String typeName() {
/*  85 */     return "?";
/*     */   } public String qualifiedTypeName() {
/*  87 */     return "?";
/*     */   } public String simpleTypeName() {
/*  89 */     return "?";
/*     */   }
/*     */   
/*     */   public String toString() {
/*  93 */     return wildcardTypeToString(this.env, (Type.WildcardType)this.type, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String wildcardTypeToString(DocEnv paramDocEnv, Type.WildcardType paramWildcardType, boolean paramBoolean) {
/* 104 */     if (paramDocEnv.legacyDoclet) {
/* 105 */       return TypeMaker.getTypeName(paramDocEnv.types.erasure((Type)paramWildcardType), paramBoolean);
/*     */     }
/* 107 */     StringBuilder stringBuilder = new StringBuilder("?");
/* 108 */     List<Type> list = getExtendsBounds(paramWildcardType);
/* 109 */     if (list.nonEmpty()) {
/* 110 */       stringBuilder.append(" extends ");
/*     */     } else {
/* 112 */       list = getSuperBounds(paramWildcardType);
/* 113 */       if (list.nonEmpty()) {
/* 114 */         stringBuilder.append(" super ");
/*     */       }
/*     */     } 
/* 117 */     boolean bool = true;
/* 118 */     for (Type type : list) {
/* 119 */       if (!bool) {
/* 120 */         stringBuilder.append(" & ");
/*     */       }
/* 122 */       stringBuilder.append(TypeMaker.getTypeString(paramDocEnv, type, paramBoolean));
/* 123 */       bool = false;
/*     */     } 
/* 125 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   private static List<Type> getExtendsBounds(Type.WildcardType paramWildcardType) {
/* 129 */     return paramWildcardType.isSuperBound() ? 
/* 130 */       List.nil() : 
/* 131 */       List.of(paramWildcardType.type);
/*     */   }
/*     */   
/*     */   private static List<Type> getSuperBounds(Type.WildcardType paramWildcardType) {
/* 135 */     return paramWildcardType.isExtendsBound() ? 
/* 136 */       List.nil() : 
/* 137 */       List.of(paramWildcardType.type);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\WildcardTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */