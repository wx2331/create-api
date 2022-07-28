/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public abstract class Type
/*     */ {
/*     */   public boolean isObject() {
/*  46 */     return false;
/*     */   }
/*     */
/*     */   public abstract <R, D> R accept(Visitor<R, D> paramVisitor, D paramD);
/*     */
/*     */   protected static void append(StringBuilder paramStringBuilder, String paramString1, List<? extends Type> paramList, String paramString2) {
/*  52 */     paramStringBuilder.append(paramString1);
/*  53 */     String str = "";
/*  54 */     for (Type type : paramList) {
/*  55 */       paramStringBuilder.append(str);
/*  56 */       paramStringBuilder.append(type);
/*  57 */       str = ", ";
/*     */     }
/*  59 */     paramStringBuilder.append(paramString2);
/*     */   }
/*     */
/*     */   protected static void appendIfNotEmpty(StringBuilder paramStringBuilder, String paramString1, List<? extends Type> paramList, String paramString2) {
/*  63 */     if (paramList != null && paramList.size() > 0) {
/*  64 */       append(paramStringBuilder, paramString1, paramList, paramString2);
/*     */     }
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
/*     */   public static class SimpleType
/*     */     extends Type
/*     */   {
/*     */     public SimpleType(String param1String) {
/*  95 */       this.name = param1String;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/*  99 */       return param1Visitor.visitSimpleType(this, param1D);
/*     */     }
/*     */
/*     */     public boolean isPrimitiveType() {
/* 103 */       return primitiveTypes.contains(this.name);
/*     */     }
/*     */
/* 106 */     private static final Set<String> primitiveTypes = new HashSet<>(Arrays.asList(new String[] { "boolean", "byte", "char", "double", "float", "int", "long", "short", "void" }));
/*     */
/*     */     public final String name;
/*     */
/*     */     public String toString() {
/* 111 */       return this.name;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public static class ArrayType
/*     */     extends Type
/*     */   {
/*     */     public final Type elemType;
/*     */
/*     */
/*     */
/*     */
/*     */     public ArrayType(Type param1Type) {
/* 127 */       this.elemType = param1Type;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 131 */       return param1Visitor.visitArrayType(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 136 */       return this.elemType + "[]";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public static class MethodType
/*     */     extends Type
/*     */   {
/*     */     public final List<? extends TypeParamType> typeParamTypes;
/*     */
/*     */     public final List<? extends Type> paramTypes;
/*     */
/*     */     public final Type returnType;
/*     */
/*     */     public final List<? extends Type> throwsTypes;
/*     */
/*     */     public MethodType(List<? extends Type> param1List, Type param1Type) {
/* 153 */       this((List<? extends TypeParamType>)null, param1List, param1Type, (List<? extends Type>)null);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public MethodType(List<? extends TypeParamType> param1List, List<? extends Type> param1List1, Type param1Type, List<? extends Type> param1List2) {
/* 160 */       this.typeParamTypes = param1List;
/* 161 */       this.paramTypes = param1List1;
/* 162 */       this.returnType = param1Type;
/* 163 */       this.throwsTypes = param1List2;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 167 */       return param1Visitor.visitMethodType(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 172 */       StringBuilder stringBuilder = new StringBuilder();
/* 173 */       appendIfNotEmpty(stringBuilder, "<", (List)this.typeParamTypes, "> ");
/* 174 */       stringBuilder.append(this.returnType);
/* 175 */       append(stringBuilder, " (", this.paramTypes, ")");
/* 176 */       appendIfNotEmpty(stringBuilder, " throws ", this.throwsTypes, "");
/* 177 */       return stringBuilder.toString();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public static class ClassSigType
/*     */     extends Type
/*     */   {
/*     */     public final List<TypeParamType> typeParamTypes;
/*     */
/*     */
/*     */     public final Type superclassType;
/*     */
/*     */
/*     */     public final List<Type> superinterfaceTypes;
/*     */
/*     */
/*     */
/*     */     public ClassSigType(List<TypeParamType> param1List, Type param1Type, List<Type> param1List1) {
/* 198 */       this.typeParamTypes = param1List;
/* 199 */       this.superclassType = param1Type;
/* 200 */       this.superinterfaceTypes = param1List1;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 204 */       return param1Visitor.visitClassSigType(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 209 */       StringBuilder stringBuilder = new StringBuilder();
/* 210 */       appendIfNotEmpty(stringBuilder, "<", (List)this.typeParamTypes, ">");
/* 211 */       if (this.superclassType != null) {
/* 212 */         stringBuilder.append(" extends ");
/* 213 */         stringBuilder.append(this.superclassType);
/*     */       }
/* 215 */       appendIfNotEmpty(stringBuilder, " implements ", this.superinterfaceTypes, "");
/* 216 */       return stringBuilder.toString();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class ClassType
/*     */     extends Type
/*     */   {
/*     */     public final ClassType outerType;
/*     */
/*     */
/*     */
/*     */
/*     */     public final String name;
/*     */
/*     */
/*     */
/*     */     public final List<Type> typeArgs;
/*     */
/*     */
/*     */
/*     */
/*     */     public ClassType(ClassType param1ClassType, String param1String, List<Type> param1List) {
/* 242 */       this.outerType = param1ClassType;
/* 243 */       this.name = param1String;
/* 244 */       this.typeArgs = param1List;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 248 */       return param1Visitor.visitClassType(this, param1D);
/*     */     }
/*     */
/*     */     public String getBinaryName() {
/* 252 */       if (this.outerType == null) {
/* 253 */         return this.name;
/*     */       }
/* 255 */       return this.outerType.getBinaryName() + "$" + this.name;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 260 */       StringBuilder stringBuilder = new StringBuilder();
/* 261 */       if (this.outerType != null) {
/* 262 */         stringBuilder.append(this.outerType);
/* 263 */         stringBuilder.append(".");
/*     */       }
/* 265 */       stringBuilder.append(this.name);
/* 266 */       appendIfNotEmpty(stringBuilder, "<", this.typeArgs, ">");
/* 267 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */
/*     */     public boolean isObject() {
/* 272 */       return (this.outerType == null && this.name
/* 273 */         .equals("java/lang/Object") && (this.typeArgs == null || this.typeArgs
/* 274 */         .isEmpty()));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class TypeParamType
/*     */     extends Type
/*     */   {
/*     */     public final String name;
/*     */
/*     */
/*     */
/*     */     public final Type classBound;
/*     */
/*     */
/*     */
/*     */     public final List<Type> interfaceBounds;
/*     */
/*     */
/*     */
/*     */
/*     */     public TypeParamType(String param1String, Type param1Type, List<Type> param1List) {
/* 299 */       this.name = param1String;
/* 300 */       this.classBound = param1Type;
/* 301 */       this.interfaceBounds = param1List;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 305 */       return param1Visitor.visitTypeParamType(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 310 */       StringBuilder stringBuilder = new StringBuilder();
/* 311 */       stringBuilder.append(this.name);
/* 312 */       String str = " extends ";
/* 313 */       if (this.classBound != null) {
/* 314 */         stringBuilder.append(str);
/* 315 */         stringBuilder.append(this.classBound);
/* 316 */         str = " & ";
/*     */       }
/* 318 */       if (this.interfaceBounds != null) {
/* 319 */         for (Type type : this.interfaceBounds) {
/* 320 */           stringBuilder.append(str);
/* 321 */           stringBuilder.append(type);
/* 322 */           str = " & ";
/*     */         }
/*     */       }
/* 325 */       return stringBuilder.toString();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class WildcardType
/*     */     extends Type
/*     */   {
/*     */     public final Kind kind;
/*     */
/*     */
/*     */
/*     */     public final Type boundType;
/*     */
/*     */
/*     */
/*     */
/*     */     public enum Kind
/*     */     {
/* 347 */       UNBOUNDED, EXTENDS, SUPER; }
/*     */     public WildcardType() {
/* 349 */       this(Kind.UNBOUNDED, null);
/*     */     }
/*     */     public WildcardType(Kind param1Kind, Type param1Type) {
/* 352 */       this.kind = param1Kind;
/* 353 */       this.boundType = param1Type;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 357 */       return param1Visitor.visitWildcardType(this, param1D);
/*     */     }
/*     */
/*     */     public String toString() {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/classfile/Type$1.$SwitchMap$com$sun$tools$classfile$Type$WildcardType$Kind : [I
/*     */       //   3: aload_0
/*     */       //   4: getfield kind : Lcom/sun/tools/classfile/Type$WildcardType$Kind;
/*     */       //   7: invokevirtual ordinal : ()I
/*     */       //   10: iaload
/*     */       //   11: tableswitch default -> 85, 1 -> 36, 2 -> 39, 3 -> 62
/*     */       //   36: ldc '?'
/*     */       //   38: areturn
/*     */       //   39: new java/lang/StringBuilder
/*     */       //   42: dup
/*     */       //   43: invokespecial <init> : ()V
/*     */       //   46: ldc '? extends '
/*     */       //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   51: aload_0
/*     */       //   52: getfield boundType : Lcom/sun/tools/classfile/Type;
/*     */       //   55: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   58: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   61: areturn
/*     */       //   62: new java/lang/StringBuilder
/*     */       //   65: dup
/*     */       //   66: invokespecial <init> : ()V
/*     */       //   69: ldc '? super '
/*     */       //   71: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   74: aload_0
/*     */       //   75: getfield boundType : Lcom/sun/tools/classfile/Type;
/*     */       //   78: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   81: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   84: areturn
/*     */       //   85: new java/lang/AssertionError
/*     */       //   88: dup
/*     */       //   89: invokespecial <init> : ()V
/*     */       //   92: athrow
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #362	-> 0
/*     */       //   #364	-> 36
/*     */       //   #366	-> 39
/*     */       //   #368	-> 62
/*     */       //   #370	-> 85
/*     */     }
/*     */   }
/*     */
/*     */   public static interface Visitor<R, P> {
/*     */     R visitSimpleType(SimpleType param1SimpleType, P param1P);
/*     */
/*     */     R visitArrayType(ArrayType param1ArrayType, P param1P);
/*     */
/*     */     R visitMethodType(MethodType param1MethodType, P param1P);
/*     */
/*     */     R visitClassSigType(ClassSigType param1ClassSigType, P param1P);
/*     */
/*     */     R visitClassType(ClassType param1ClassType, P param1P);
/*     */
/*     */     R visitTypeParamType(TypeParamType param1TypeParamType, P param1P);
/*     */
/*     */     R visitWildcardType(WildcardType param1WildcardType, P param1P);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
