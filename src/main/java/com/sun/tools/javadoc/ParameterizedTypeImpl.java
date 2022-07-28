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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterizedTypeImpl
/*     */   extends AbstractTypeImpl
/*     */   implements ParameterizedType
/*     */ {
/*     */   ParameterizedTypeImpl(DocEnv paramDocEnv, Type paramType) {
/*  53 */     super(paramDocEnv, paramType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc asClassDoc() {
/*  61 */     return this.env.getClassDoc((Symbol.ClassSymbol)this.type.tsym);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] typeArguments() {
/*  68 */     return TypeMaker.getTypes(this.env, this.type.getTypeArguments());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type superclassType() {
/*  76 */     if (asClassDoc().isInterface()) {
/*  77 */       return null;
/*     */     }
/*  79 */     Type type = this.env.types.supertype(this.type);
/*  80 */     return TypeMaker.getType(this.env, (type != this.type) ? type : this.env.syms.objectType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] interfaceTypes() {
/*  90 */     return TypeMaker.getTypes(this.env, this.env.types.interfaces(this.type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type containingType() {
/*  98 */     if (this.type.getEnclosingType().hasTag(TypeTag.CLASS))
/*     */     {
/* 100 */       return TypeMaker.getType(this.env, this.type.getEnclosingType());
/*     */     }
/* 102 */     Symbol.ClassSymbol classSymbol = this.type.tsym.owner.enclClass();
/* 103 */     if (classSymbol != null)
/*     */     {
/*     */ 
/*     */       
/* 107 */       return (Type)this.env.getClassDoc(classSymbol);
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String typeName() {
/* 118 */     return TypeMaker.getTypeName(this.type, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParameterizedType asParameterizedType() {
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return parameterizedTypeToString(this.env, (Type.ClassType)this.type, true);
/*     */   }
/*     */ 
/*     */   
/*     */   static String parameterizedTypeToString(DocEnv paramDocEnv, Type.ClassType paramClassType, boolean paramBoolean) {
/* 133 */     if (paramDocEnv.legacyDoclet) {
/* 134 */       return TypeMaker.getTypeName((Type)paramClassType, paramBoolean);
/*     */     }
/* 136 */     StringBuilder stringBuilder = new StringBuilder();
/* 137 */     if (!paramClassType.getEnclosingType().hasTag(TypeTag.CLASS)) {
/* 138 */       stringBuilder.append(TypeMaker.getTypeName((Type)paramClassType, paramBoolean));
/*     */     } else {
/* 140 */       Type.ClassType classType = (Type.ClassType)paramClassType.getEnclosingType();
/* 141 */       stringBuilder.append(parameterizedTypeToString(paramDocEnv, classType, paramBoolean))
/* 142 */         .append('.')
/* 143 */         .append(paramClassType.tsym.name.toString());
/*     */     } 
/* 145 */     stringBuilder.append(TypeMaker.typeArgumentsString(paramDocEnv, paramClassType, paramBoolean));
/* 146 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ParameterizedTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */