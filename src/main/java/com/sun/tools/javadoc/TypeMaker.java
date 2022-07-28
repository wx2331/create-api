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
/*     */ import com.sun.tools.javac.code.TypeTag;
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
/*     */ public class TypeMaker
/*     */ {
/*     */   public static Type getType(DocEnv paramDocEnv, Type paramType) {
/*  48 */     return getType(paramDocEnv, paramType, true);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static Type getType(DocEnv paramDocEnv, Type paramType, boolean paramBoolean) {
/*  57 */     return getType(paramDocEnv, paramType, paramBoolean, true);
/*     */   }
/*     */
/*     */
/*     */   public static Type getType(DocEnv paramDocEnv, Type paramType, boolean paramBoolean1, boolean paramBoolean2) {
/*     */     try {
/*  63 */       return getTypeImpl(paramDocEnv, paramType, paramBoolean1, paramBoolean2);
/*  64 */     } catch (Symbol.CompletionFailure completionFailure) {
/*     */
/*     */
/*     */
/*     */
/*  69 */       return getType(paramDocEnv, paramType, paramBoolean1, paramBoolean2);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   private static Type getTypeImpl(DocEnv paramDocEnv, Type paramType, boolean paramBoolean1, boolean paramBoolean2) {
/*     */     Type.WildcardType wildcardType;
/*  76 */     if (paramDocEnv.legacyDoclet) {
/*  77 */       paramType = paramDocEnv.types.erasure(paramType);
/*     */     }
/*     */
/*  80 */     if (paramBoolean2 && paramType.isAnnotated()) {
/*  81 */       return new AnnotatedTypeImpl(paramDocEnv, paramType);
/*     */     }
/*     */
/*  84 */     switch (paramType.getTag()) {
/*     */       case CLASS:
/*  86 */         if (ClassDocImpl.isGeneric((Symbol.ClassSymbol)paramType.tsym)) {
/*  87 */           return paramDocEnv.getParameterizedType((Type.ClassType)paramType);
/*     */         }
/*  89 */         return (Type)paramDocEnv.getClassDoc((Symbol.ClassSymbol)paramType.tsym);
/*     */
/*     */       case WILDCARD:
/*  92 */         wildcardType = (Type.WildcardType)paramType;
/*  93 */         return new WildcardTypeImpl(paramDocEnv, wildcardType);
/*  94 */       case TYPEVAR: return new TypeVariableImpl(paramDocEnv, (Type.TypeVar)paramType);
/*  95 */       case ARRAY: return new ArrayTypeImpl(paramDocEnv, paramType);
/*  96 */       case BYTE: return PrimitiveType.byteType;
/*  97 */       case CHAR: return PrimitiveType.charType;
/*  98 */       case SHORT: return PrimitiveType.shortType;
/*  99 */       case INT: return PrimitiveType.intType;
/* 100 */       case LONG: return PrimitiveType.longType;
/* 101 */       case FLOAT: return PrimitiveType.floatType;
/* 102 */       case DOUBLE: return PrimitiveType.doubleType;
/* 103 */       case BOOLEAN: return PrimitiveType.booleanType;
/* 104 */       case VOID: return PrimitiveType.voidType;
/*     */       case ERROR:
/* 106 */         if (paramBoolean1)
/* 107 */           return (Type)paramDocEnv.getClassDoc((Symbol.ClassSymbol)paramType.tsym);
/*     */         break;
/*     */     }
/* 110 */     return new PrimitiveType(paramType.tsym.getQualifiedName().toString());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static Type[] getTypes(DocEnv paramDocEnv, List<Type> paramList) {
/* 118 */     return getTypes(paramDocEnv, paramList, new Type[paramList.length()]);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static Type[] getTypes(DocEnv paramDocEnv, List<Type> paramList, Type[] paramArrayOfType) {
/* 126 */     byte b = 0;
/* 127 */     for (Type type : paramList) {
/* 128 */       paramArrayOfType[b++] = getType(paramDocEnv, type);
/*     */     }
/* 130 */     return paramArrayOfType;
/*     */   }
/*     */   public static String getTypeName(Type paramType, boolean paramBoolean) {
/*     */     StringBuilder stringBuilder;
/* 134 */     switch (paramType.getTag()) {
/*     */       case ARRAY:
/* 136 */         stringBuilder = new StringBuilder();
/* 137 */         while (paramType.hasTag(TypeTag.ARRAY)) {
/* 138 */           stringBuilder.append("[]");
/* 139 */           paramType = ((Type.ArrayType)paramType).elemtype;
/*     */         }
/* 141 */         stringBuilder.insert(0, getTypeName(paramType, paramBoolean));
/* 142 */         return stringBuilder.toString();
/*     */       case CLASS:
/* 144 */         return ClassDocImpl.getClassName((Symbol.ClassSymbol)paramType.tsym, paramBoolean);
/*     */     }
/* 146 */     return paramType.tsym.getQualifiedName().toString();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static String getTypeString(DocEnv paramDocEnv, Type paramType, boolean paramBoolean) {
/*     */     StringBuilder stringBuilder;
/*     */     Type.WildcardType wildcardType;
/* 157 */     if (paramType.isAnnotated()) {
/* 158 */       paramType = paramType.unannotatedType();
/*     */     }
/* 160 */     switch (paramType.getTag()) {
/*     */       case ARRAY:
/* 162 */         stringBuilder = new StringBuilder();
/* 163 */         while (paramType.hasTag(TypeTag.ARRAY)) {
/* 164 */           stringBuilder.append("[]");
/* 165 */           paramType = paramDocEnv.types.elemtype(paramType);
/*     */         }
/* 167 */         stringBuilder.insert(0, getTypeString(paramDocEnv, paramType, paramBoolean));
/* 168 */         return stringBuilder.toString();
/*     */       case CLASS:
/* 170 */         return
/* 171 */           ParameterizedTypeImpl.parameterizedTypeToString(paramDocEnv, (Type.ClassType)paramType, paramBoolean);
/*     */       case WILDCARD:
/* 173 */         wildcardType = (Type.WildcardType)paramType;
/* 174 */         return WildcardTypeImpl.wildcardTypeToString(paramDocEnv, wildcardType, paramBoolean);
/*     */     }
/* 176 */     return paramType.tsym.getQualifiedName().toString();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static String typeParametersString(DocEnv paramDocEnv, Symbol paramSymbol, boolean paramBoolean) {
/* 187 */     if (paramDocEnv.legacyDoclet || paramSymbol.type.getTypeArguments().isEmpty()) {
/* 188 */       return "";
/*     */     }
/* 190 */     StringBuilder stringBuilder = new StringBuilder();
/* 191 */     for (Type type : paramSymbol.type.getTypeArguments()) {
/* 192 */       stringBuilder.append((stringBuilder.length() == 0) ? "<" : ", ");
/* 193 */       stringBuilder.append(TypeVariableImpl.typeVarToString(paramDocEnv, (Type.TypeVar)type, paramBoolean));
/*     */     }
/* 195 */     stringBuilder.append(">");
/* 196 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static String typeArgumentsString(DocEnv paramDocEnv, Type.ClassType paramClassType, boolean paramBoolean) {
/* 205 */     if (paramDocEnv.legacyDoclet || paramClassType.getTypeArguments().isEmpty()) {
/* 206 */       return "";
/*     */     }
/* 208 */     StringBuilder stringBuilder = new StringBuilder();
/* 209 */     for (Type type : paramClassType.getTypeArguments()) {
/* 210 */       stringBuilder.append((stringBuilder.length() == 0) ? "<" : ", ");
/* 211 */       stringBuilder.append(getTypeString(paramDocEnv, type, paramBoolean));
/*     */     }
/* 213 */     stringBuilder.append(">");
/* 214 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */   private static class ArrayTypeImpl
/*     */     implements Type
/*     */   {
/*     */     Type arrayType;
/*     */
/*     */     DocEnv env;
/*     */
/*     */     private Type skipArraysCache;
/*     */
/*     */
/*     */     ArrayTypeImpl(DocEnv param1DocEnv, Type param1Type) {
/* 229 */       this.skipArraysCache = null;
/*     */       this.env = param1DocEnv;
/*     */       this.arrayType = param1Type; } public Type getElementType() {
/* 232 */       return TypeMaker.getType(this.env, this.env.types.elemtype(this.arrayType));
/*     */     }
/*     */
/*     */     private Type skipArrays() {
/* 236 */       if (this.skipArraysCache == null) {
/*     */         Type type;
/* 238 */         for (type = this.arrayType; type.hasTag(TypeTag.ARRAY); type = this.env.types.elemtype(type));
/* 239 */         this.skipArraysCache = TypeMaker.getType(this.env, type);
/*     */       }
/* 241 */       return this.skipArraysCache;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String dimension() {
/* 250 */       StringBuilder stringBuilder = new StringBuilder();
/* 251 */       for (Type type = this.arrayType; type.hasTag(TypeTag.ARRAY); type = this.env.types.elemtype(type)) {
/* 252 */         stringBuilder.append("[]");
/*     */       }
/* 254 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String typeName() {
/* 263 */       return skipArrays().typeName();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String qualifiedTypeName() {
/* 273 */       return skipArrays().qualifiedTypeName();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public String simpleTypeName() {
/* 280 */       return skipArrays().simpleTypeName();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public ClassDoc asClassDoc() {
/* 290 */       return skipArrays().asClassDoc();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public ParameterizedType asParameterizedType() {
/* 298 */       return skipArrays().asParameterizedType();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public TypeVariable asTypeVariable() {
/* 306 */       return skipArrays().asTypeVariable();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public WildcardType asWildcardType() {
/* 313 */       return null;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public AnnotatedType asAnnotatedType() {
/* 320 */       return null;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public AnnotationTypeDoc asAnnotationTypeDoc() {
/* 328 */       return skipArrays().asAnnotationTypeDoc();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isPrimitive() {
/* 335 */       return skipArrays().isPrimitive();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String toString() {
/* 350 */       return qualifiedTypeName() + dimension();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\TypeMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
