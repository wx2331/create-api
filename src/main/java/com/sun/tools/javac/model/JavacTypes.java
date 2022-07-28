/*     */ package com.sun.tools.javac.model;
/*     */ 
/*     */ import com.sun.tools.javac.code.BoundKind;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.ExecutableType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.NullType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.WildcardType;
/*     */ import javax.lang.model.util.Types;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavacTypes
/*     */   implements Types
/*     */ {
/*     */   private Symtab syms;
/*     */   private Types types;
/*     */   
/*     */   public static JavacTypes instance(Context paramContext) {
/*  55 */     JavacTypes javacTypes = (JavacTypes)paramContext.get(JavacTypes.class);
/*  56 */     if (javacTypes == null)
/*  57 */       javacTypes = new JavacTypes(paramContext); 
/*  58 */     return javacTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavacTypes(Context paramContext) {
/*  65 */     setContext(paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContext(Context paramContext) {
/*  73 */     paramContext.put(JavacTypes.class, this);
/*  74 */     this.syms = Symtab.instance(paramContext);
/*  75 */     this.types = Types.instance(paramContext);
/*     */   }
/*     */   public Element asElement(TypeMirror paramTypeMirror) {
/*     */     Type type;
/*  79 */     switch (paramTypeMirror.getKind()) {
/*     */       case DECLARED:
/*     */       case INTERSECTION:
/*     */       case ERROR:
/*     */       case TYPEVAR:
/*  84 */         type = cast(Type.class, paramTypeMirror);
/*  85 */         return (Element)type.asElement();
/*     */     } 
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSameType(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/*  92 */     return this.types.isSameType((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */   
/*     */   public boolean isSubtype(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/*  96 */     validateTypeNotIn(paramTypeMirror1, EXEC_OR_PKG);
/*  97 */     validateTypeNotIn(paramTypeMirror2, EXEC_OR_PKG);
/*  98 */     return this.types.isSubtype((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */   
/*     */   public boolean isAssignable(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 102 */     validateTypeNotIn(paramTypeMirror1, EXEC_OR_PKG);
/* 103 */     validateTypeNotIn(paramTypeMirror2, EXEC_OR_PKG);
/* 104 */     return this.types.isAssignable((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */   
/*     */   public boolean contains(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 108 */     validateTypeNotIn(paramTypeMirror1, EXEC_OR_PKG);
/* 109 */     validateTypeNotIn(paramTypeMirror2, EXEC_OR_PKG);
/* 110 */     return this.types.containsType((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */   
/*     */   public boolean isSubsignature(ExecutableType paramExecutableType1, ExecutableType paramExecutableType2) {
/* 114 */     return this.types.isSubSignature((Type)paramExecutableType1, (Type)paramExecutableType2);
/*     */   }
/*     */   
/*     */   public List<Type> directSupertypes(TypeMirror paramTypeMirror) {
/* 118 */     validateTypeNotIn(paramTypeMirror, EXEC_OR_PKG);
/* 119 */     return (List<Type>)this.types.directSupertypes((Type)paramTypeMirror);
/*     */   }
/*     */   
/*     */   public TypeMirror erasure(TypeMirror paramTypeMirror) {
/* 123 */     if (paramTypeMirror.getKind() == TypeKind.PACKAGE)
/* 124 */       throw new IllegalArgumentException(paramTypeMirror.toString()); 
/* 125 */     return (TypeMirror)this.types.erasure((Type)paramTypeMirror);
/*     */   }
/*     */   
/*     */   public TypeElement boxedClass(PrimitiveType paramPrimitiveType) {
/* 129 */     return (TypeElement)this.types.boxedClass((Type)paramPrimitiveType);
/*     */   }
/*     */   
/*     */   public PrimitiveType unboxedType(TypeMirror paramTypeMirror) {
/* 133 */     if (paramTypeMirror.getKind() != TypeKind.DECLARED)
/* 134 */       throw new IllegalArgumentException(paramTypeMirror.toString()); 
/* 135 */     Type type = this.types.unboxedType((Type)paramTypeMirror);
/* 136 */     if (!type.isPrimitive())
/* 137 */       throw new IllegalArgumentException(paramTypeMirror.toString()); 
/* 138 */     return (PrimitiveType)type;
/*     */   }
/*     */   
/*     */   public TypeMirror capture(TypeMirror paramTypeMirror) {
/* 142 */     validateTypeNotIn(paramTypeMirror, EXEC_OR_PKG);
/* 143 */     return (TypeMirror)this.types.capture((Type)paramTypeMirror);
/*     */   }
/*     */   
/*     */   public PrimitiveType getPrimitiveType(TypeKind paramTypeKind) {
/* 147 */     switch (paramTypeKind) { case BOOLEAN:
/* 148 */         return (PrimitiveType)this.syms.booleanType;
/* 149 */       case BYTE: return (PrimitiveType)this.syms.byteType;
/* 150 */       case SHORT: return (PrimitiveType)this.syms.shortType;
/* 151 */       case INT: return (PrimitiveType)this.syms.intType;
/* 152 */       case LONG: return (PrimitiveType)this.syms.longType;
/* 153 */       case CHAR: return (PrimitiveType)this.syms.charType;
/* 154 */       case FLOAT: return (PrimitiveType)this.syms.floatType;
/* 155 */       case DOUBLE: return (PrimitiveType)this.syms.doubleType; }
/*     */     
/* 157 */     throw new IllegalArgumentException("Not a primitive type: " + paramTypeKind);
/*     */   }
/*     */ 
/*     */   
/*     */   public NullType getNullType() {
/* 162 */     return (NullType)this.syms.botType;
/*     */   }
/*     */   
/*     */   public NoType getNoType(TypeKind paramTypeKind) {
/* 166 */     switch (paramTypeKind) { case VOID:
/* 167 */         return (NoType)this.syms.voidType;
/* 168 */       case NONE: return (NoType)Type.noType; }
/*     */     
/* 170 */     throw new IllegalArgumentException(paramTypeKind.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayType getArrayType(TypeMirror paramTypeMirror) {
/* 175 */     switch (paramTypeMirror.getKind()) {
/*     */       case VOID:
/*     */       case EXECUTABLE:
/*     */       case WILDCARD:
/*     */       case PACKAGE:
/* 180 */         throw new IllegalArgumentException(paramTypeMirror.toString());
/*     */     } 
/* 182 */     return (ArrayType)new Type.ArrayType((Type)paramTypeMirror, (Symbol.TypeSymbol)this.syms.arrayClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public WildcardType getWildcardType(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/*     */     BoundKind boundKind;
/*     */     Type type;
/* 189 */     if (paramTypeMirror1 == null && paramTypeMirror2 == null) {
/* 190 */       boundKind = BoundKind.UNBOUND;
/* 191 */       type = this.syms.objectType;
/* 192 */     } else if (paramTypeMirror2 == null) {
/* 193 */       boundKind = BoundKind.EXTENDS;
/* 194 */       type = (Type)paramTypeMirror1;
/* 195 */     } else if (paramTypeMirror1 == null) {
/* 196 */       boundKind = BoundKind.SUPER;
/* 197 */       type = (Type)paramTypeMirror2;
/*     */     } else {
/* 199 */       throw new IllegalArgumentException("Extends and super bounds cannot both be provided");
/*     */     } 
/*     */     
/* 202 */     switch (type.getKind()) {
/*     */       case DECLARED:
/*     */       case ERROR:
/*     */       case TYPEVAR:
/*     */       case ARRAY:
/* 207 */         return (WildcardType)new Type.WildcardType(type, boundKind, (Symbol.TypeSymbol)this.syms.boundClass);
/*     */     } 
/* 209 */     throw new IllegalArgumentException(type.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DeclaredType getDeclaredType(TypeElement paramTypeElement, TypeMirror... paramVarArgs) {
/* 215 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)paramTypeElement;
/*     */     
/* 217 */     if (paramVarArgs.length == 0)
/* 218 */       return (DeclaredType)classSymbol.erasure(this.types); 
/* 219 */     if (classSymbol.type.getEnclosingType().isParameterized()) {
/* 220 */       throw new IllegalArgumentException(classSymbol.toString());
/*     */     }
/* 222 */     return getDeclaredType0(classSymbol.type.getEnclosingType(), classSymbol, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DeclaredType getDeclaredType(DeclaredType paramDeclaredType, TypeElement paramTypeElement, TypeMirror... paramVarArgs) {
/* 228 */     if (paramDeclaredType == null) {
/* 229 */       return getDeclaredType(paramTypeElement, paramVarArgs);
/*     */     }
/* 231 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)paramTypeElement;
/* 232 */     Type type = (Type)paramDeclaredType;
/*     */     
/* 234 */     if (type.tsym != classSymbol.owner.enclClass())
/* 235 */       throw new IllegalArgumentException(paramDeclaredType.toString()); 
/* 236 */     if (!type.isParameterized()) {
/* 237 */       return getDeclaredType(paramTypeElement, paramVarArgs);
/*     */     }
/* 239 */     return getDeclaredType0(type, classSymbol, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DeclaredType getDeclaredType0(Type paramType, Symbol.ClassSymbol paramClassSymbol, TypeMirror... paramVarArgs) {
/* 245 */     if (paramVarArgs.length != paramClassSymbol.type.getTypeArguments().length()) {
/* 246 */       throw new IllegalArgumentException("Incorrect number of type arguments");
/*     */     }
/*     */     
/* 249 */     ListBuffer listBuffer = new ListBuffer();
/* 250 */     for (TypeMirror typeMirror : paramVarArgs) {
/* 251 */       if (!(typeMirror instanceof javax.lang.model.type.ReferenceType) && !(typeMirror instanceof WildcardType))
/* 252 */         throw new IllegalArgumentException(typeMirror.toString()); 
/* 253 */       listBuffer.append(typeMirror);
/*     */     } 
/*     */ 
/*     */     
/* 257 */     return (DeclaredType)new Type.ClassType(paramType, listBuffer.toList(), (Symbol.TypeSymbol)paramClassSymbol);
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
/*     */   public TypeMirror asMemberOf(DeclaredType paramDeclaredType, Element paramElement) {
/* 275 */     Type type = (Type)paramDeclaredType;
/* 276 */     Symbol symbol = (Symbol)paramElement;
/* 277 */     if (this.types.asSuper(type, symbol.getEnclosingElement()) == null)
/* 278 */       throw new IllegalArgumentException(symbol + "@" + type); 
/* 279 */     return (TypeMirror)this.types.memberType(type, symbol);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 284 */   private static final Set<TypeKind> EXEC_OR_PKG = EnumSet.of(TypeKind.EXECUTABLE, TypeKind.PACKAGE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateTypeNotIn(TypeMirror paramTypeMirror, Set<TypeKind> paramSet) {
/* 290 */     if (paramSet.contains(paramTypeMirror.getKind())) {
/* 291 */       throw new IllegalArgumentException(paramTypeMirror.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T cast(Class<T> paramClass, Object paramObject) {
/* 300 */     if (!paramClass.isInstance(paramObject))
/* 301 */       throw new IllegalArgumentException(paramObject.toString()); 
/* 302 */     return paramClass.cast(paramObject);
/*     */   }
/*     */   
/*     */   public Set<Symbol.MethodSymbol> getOverriddenMethods(Element paramElement) {
/* 306 */     if (paramElement.getKind() != ElementKind.METHOD || paramElement
/* 307 */       .getModifiers().contains(Modifier.STATIC) || paramElement
/* 308 */       .getModifiers().contains(Modifier.PRIVATE)) {
/* 309 */       return Collections.emptySet();
/*     */     }
/* 311 */     if (!(paramElement instanceof Symbol.MethodSymbol)) {
/* 312 */       throw new IllegalArgumentException();
/*     */     }
/* 314 */     Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)paramElement;
/* 315 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)methodSymbol.owner;
/*     */     
/* 317 */     LinkedHashSet<Symbol.MethodSymbol> linkedHashSet = new LinkedHashSet();
/* 318 */     for (Type type : this.types.closure(classSymbol.type)) {
/* 319 */       if (type != classSymbol.type) {
/* 320 */         Symbol.ClassSymbol classSymbol1 = (Symbol.ClassSymbol)type.tsym;
/* 321 */         for (Scope.Entry entry = classSymbol1.members().lookup(methodSymbol.name); entry.scope != null; entry = entry.next()) {
/* 322 */           if (entry.sym.kind == 16 && methodSymbol.overrides(entry.sym, (Symbol.TypeSymbol)classSymbol, this.types, true)) {
/* 323 */             linkedHashSet.add((Symbol.MethodSymbol)entry.sym);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 329 */     return linkedHashSet;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\model\JavacTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */