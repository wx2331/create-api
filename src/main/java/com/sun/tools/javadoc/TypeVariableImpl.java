/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeVariableImpl
/*     */   extends AbstractTypeImpl
/*     */   implements TypeVariable
/*     */ {
/*     */   TypeVariableImpl(DocEnv paramDocEnv, Type.TypeVar paramTypeVar) {
/*  57 */     super(paramDocEnv, (Type)paramTypeVar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] bounds() {
/*  64 */     return TypeMaker.getTypes(this.env, getBounds((Type.TypeVar)this.type, this.env));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProgramElementDoc owner() {
/*  72 */     Symbol symbol = this.type.tsym.owner;
/*  73 */     if ((symbol.kind & 0x2) != 0) {
/*  74 */       return this.env.getClassDoc((Symbol.ClassSymbol)symbol);
/*     */     }
/*  76 */     Names names = symbol.name.table.names;
/*  77 */     if (symbol.name == names.init) {
/*  78 */       return this.env.getConstructorDoc((Symbol.MethodSymbol)symbol);
/*     */     }
/*  80 */     return this.env.getMethodDoc((Symbol.MethodSymbol)symbol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc asClassDoc() {
/*  89 */     return this.env.getClassDoc((Symbol.ClassSymbol)(this.env.types.erasure(this.type)).tsym);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeVariable asTypeVariable() {
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return typeVarToString(this.env, (Type.TypeVar)this.type, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String typeVarToString(DocEnv paramDocEnv, Type.TypeVar paramTypeVar, boolean paramBoolean) {
/* 108 */     StringBuilder stringBuilder = new StringBuilder(paramTypeVar.toString());
/* 109 */     List<Type> list = getBounds(paramTypeVar, paramDocEnv);
/* 110 */     if (list.nonEmpty()) {
/* 111 */       boolean bool = true;
/* 112 */       for (Type type : list) {
/* 113 */         stringBuilder.append(bool ? " extends " : " & ");
/* 114 */         stringBuilder.append(TypeMaker.getTypeString(paramDocEnv, type, paramBoolean));
/* 115 */         bool = false;
/*     */       } 
/*     */     } 
/* 118 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Type> getBounds(Type.TypeVar paramTypeVar, DocEnv paramDocEnv) {
/* 125 */     Type type = paramTypeVar.getUpperBound();
/* 126 */     Name name = type.tsym.getQualifiedName();
/* 127 */     if (name == name.table.names.java_lang_Object && 
/* 128 */       !type.isAnnotated()) {
/* 129 */       return List.nil();
/*     */     }
/* 131 */     return paramDocEnv.types.getBounds(paramTypeVar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationDesc[] annotations() {
/* 140 */     if (!this.type.isAnnotated()) {
/* 141 */       return new AnnotationDesc[0];
/*     */     }
/* 143 */     List list = this.type.getAnnotationMirrors();
/* 144 */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[list.length()];
/* 145 */     byte b = 0;
/* 146 */     for (Attribute.Compound compound : list) {
/* 147 */       arrayOfAnnotationDesc[b++] = new AnnotationDescImpl(this.env, compound);
/*     */     }
/* 149 */     return arrayOfAnnotationDesc;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\TypeVariableImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */