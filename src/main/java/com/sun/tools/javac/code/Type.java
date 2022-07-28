/*      */ package com.sun.tools.javac.code;
/*      */
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.element.Element;
/*      */ import javax.lang.model.type.DeclaredType;
/*      */ import javax.lang.model.type.ExecutableType;
/*      */ import javax.lang.model.type.IntersectionType;
/*      */ import javax.lang.model.type.NoType;
/*      */ import javax.lang.model.type.NullType;
/*      */ import javax.lang.model.type.PrimitiveType;
/*      */ import javax.lang.model.type.TypeKind;
/*      */ import javax.lang.model.type.TypeMirror;
/*      */ import javax.lang.model.type.TypeVariable;
/*      */ import javax.lang.model.type.TypeVisitor;
/*      */ import javax.lang.model.type.UnionType;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public abstract class Type
/*      */   extends AnnoConstruct
/*      */   implements TypeMirror
/*      */ {
/*   75 */   public static final JCNoType noType = new JCNoType()
/*      */     {
/*      */       public String toString() {
/*   78 */         return "none";
/*      */       }
/*      */     };
/*      */
/*      */
/*   83 */   public static final JCNoType recoveryType = new JCNoType()
/*      */     {
/*      */       public String toString() {
/*   86 */         return "recovery";
/*      */       }
/*      */     };
/*      */
/*      */
/*   91 */   public static final JCNoType stuckType = new JCNoType()
/*      */     {
/*      */       public String toString() {
/*   94 */         return "stuck";
/*      */       }
/*      */     };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static boolean moreInfo = false;
/*      */
/*      */
/*      */
/*      */   public Symbol.TypeSymbol tsym;
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean hasTag(TypeTag paramTypeTag) {
/*  112 */     return (paramTypeTag == getTag());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isNumeric() {
/*  122 */     return false;
/*      */   }
/*      */
/*      */   public boolean isPrimitive() {
/*  126 */     return false;
/*      */   }
/*      */
/*      */   public boolean isPrimitiveOrVoid() {
/*  130 */     return false;
/*      */   }
/*      */
/*      */   public boolean isReference() {
/*  134 */     return false;
/*      */   }
/*      */
/*      */   public boolean isNullOrReference() {
/*  138 */     return false;
/*      */   }
/*      */
/*      */   public boolean isPartial() {
/*  142 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Object constValue() {
/*  152 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean isFalse() {
/*  158 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean isTrue() {
/*  164 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type getModelType() {
/*  173 */     return this;
/*      */   }
/*      */
/*      */   public static List<Type> getModelTypes(List<Type> paramList) {
/*  177 */     ListBuffer listBuffer = new ListBuffer();
/*  178 */     for (Type type : paramList)
/*  179 */       listBuffer.append(type.getModelType());
/*  180 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public Type getOriginalType() {
/*  186 */     return this;
/*      */   }
/*      */   public <R, S> R accept(Visitor<R, S> paramVisitor, S paramS) {
/*  189 */     return paramVisitor.visitType(this, paramS);
/*      */   }
/*      */
/*      */
/*      */   public Type(Symbol.TypeSymbol paramTypeSymbol) {
/*  194 */     this.tsym = paramTypeSymbol;
/*      */   }
/*      */
/*      */   public static abstract class Mapping
/*      */   {
/*      */     private String name;
/*      */
/*      */     public Mapping(String param1String) {
/*  202 */       this.name = param1String;
/*      */     }
/*      */     public abstract Type apply(Type param1Type);
/*      */     public String toString() {
/*  206 */       return this.name;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public Type map(Mapping paramMapping) {
/*  213 */     return this;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static List<Type> map(List<Type> paramList, Mapping paramMapping) {
/*  219 */     if (paramList.nonEmpty()) {
/*  220 */       List<Type> list = map(paramList.tail, paramMapping);
/*  221 */       Type type = paramMapping.apply((Type)paramList.head);
/*  222 */       if (list != paramList.tail || type != paramList.head)
/*  223 */         return list.prepend(type);
/*      */     }
/*  225 */     return paramList;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public Type constType(Object paramObject) {
/*  232 */     throw new AssertionError();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type baseType() {
/*  240 */     return this;
/*      */   }
/*      */
/*      */   public Type annotatedType(List<Attribute.TypeCompound> paramList) {
/*  244 */     return new AnnotatedType(paramList, this);
/*      */   }
/*      */
/*      */   public boolean isAnnotated() {
/*  248 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type unannotatedType() {
/*  256 */     return this;
/*      */   }
/*      */
/*      */
/*      */   public List<Attribute.TypeCompound> getAnnotationMirrors() {
/*  261 */     return List.nil();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> paramClass) {
/*  267 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public <A extends java.lang.annotation.Annotation> A[] getAnnotationsByType(Class<A> paramClass) {
/*  274 */     return (A[])Array.newInstance(paramClass, 0);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static List<Type> baseTypes(List<Type> paramList) {
/*  281 */     if (paramList.nonEmpty()) {
/*  282 */       Type type = ((Type)paramList.head).baseType();
/*  283 */       List<Type> list = baseTypes(paramList.tail);
/*  284 */       if (type != paramList.head || list != paramList.tail)
/*  285 */         return list.prepend(type);
/*      */     }
/*  287 */     return paramList;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public String toString() {
/*  295 */     String str = (this.tsym == null || this.tsym.name == null) ? "<none>" : this.tsym.name.toString();
/*  296 */     if (moreInfo && hasTag(TypeTag.TYPEVAR)) {
/*  297 */       str = str + hashCode();
/*      */     }
/*  299 */     return str;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static String toString(List<Type> paramList) {
/*  308 */     if (paramList.isEmpty()) {
/*  309 */       return "";
/*      */     }
/*  311 */     StringBuilder stringBuilder = new StringBuilder();
/*  312 */     stringBuilder.append(((Type)paramList.head).toString());
/*  313 */     for (List list = paramList.tail; list.nonEmpty(); list = list.tail)
/*  314 */       stringBuilder.append(",").append(((Type)list.head).toString());
/*  315 */     return stringBuilder.toString();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public String stringValue() {
/*  323 */     Object object = Assert.checkNonNull(constValue());
/*  324 */     return object.toString();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean equals(Object paramObject) {
/*  334 */     return super.equals(paramObject);
/*      */   }
/*      */
/*      */
/*      */   public int hashCode() {
/*  339 */     return super.hashCode();
/*      */   }
/*      */
/*      */   public String argtypes(boolean paramBoolean) {
/*  343 */     List<Type> list = getParameterTypes();
/*  344 */     if (!paramBoolean) return list.toString();
/*  345 */     StringBuilder stringBuilder = new StringBuilder();
/*  346 */     while (list.tail.nonEmpty()) {
/*  347 */       stringBuilder.append(list.head);
/*  348 */       list = list.tail;
/*  349 */       stringBuilder.append(',');
/*      */     }
/*  351 */     if (((Type)list.head).unannotatedType().hasTag(TypeTag.ARRAY)) {
/*  352 */       stringBuilder.append(((ArrayType)((Type)list.head).unannotatedType()).elemtype);
/*  353 */       if (((Type)list.head).getAnnotationMirrors().nonEmpty()) {
/*  354 */         stringBuilder.append(((Type)list.head).getAnnotationMirrors());
/*      */       }
/*  356 */       stringBuilder.append("...");
/*      */     } else {
/*  358 */       stringBuilder.append(list.head);
/*      */     }
/*  360 */     return stringBuilder.toString();
/*      */   }
/*      */
/*      */   public List<Type> getTypeArguments()
/*      */   {
/*  365 */     return List.nil();
/*  366 */   } public Type getEnclosingType() { return null; }
/*  367 */   public List<Type> getParameterTypes() { return List.nil(); }
/*  368 */   public Type getReturnType() { return null; }
/*  369 */   public Type getReceiverType() { return null; }
/*  370 */   public List<Type> getThrownTypes() { return List.nil(); }
/*  371 */   public Type getUpperBound() { return null; } public Type getLowerBound() {
/*  372 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public List<Type> allparams() {
/*  381 */     return List.nil();
/*      */   }
/*      */
/*      */
/*      */   public boolean isErroneous() {
/*  386 */     return false;
/*      */   }
/*      */
/*      */   public static boolean isErroneous(List<Type> paramList) {
/*  390 */     for (List<Type> list = paramList; list.nonEmpty(); list = list.tail) {
/*  391 */       if (((Type)list.head).isErroneous()) return true;
/*  392 */     }  return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isParameterized() {
/*  401 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isRaw() {
/*  412 */     return false;
/*      */   }
/*      */
/*      */   public boolean isCompound() {
/*  416 */     return (this.tsym.completer == null && (this.tsym
/*      */
/*      */
/*      */
/*      */
/*  421 */       .flags() & 0x1000000L) != 0L);
/*      */   }
/*      */
/*      */   public boolean isIntersection() {
/*  425 */     return false;
/*      */   }
/*      */
/*      */   public boolean isUnion() {
/*  429 */     return false;
/*      */   }
/*      */
/*      */   public boolean isInterface() {
/*  433 */     return ((this.tsym.flags() & 0x200L) != 0L);
/*      */   }
/*      */
/*      */   public boolean isFinal() {
/*  437 */     return ((this.tsym.flags() & 0x10L) != 0L);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean contains(Type paramType) {
/*  444 */     return (paramType == this);
/*      */   }
/*      */
/*      */   public static boolean contains(List<Type> paramList, Type paramType) {
/*  448 */     List<Type> list = paramList;
/*  449 */     for (; list.tail != null;
/*  450 */       list = list.tail) {
/*  451 */       if (((Type)list.head).contains(paramType)) return true;
/*  452 */     }  return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public boolean containsAny(List<Type> paramList) {
/*  458 */     for (Type type : paramList) {
/*  459 */       if (contains(type)) return true;
/*  460 */     }  return false;
/*      */   }
/*      */
/*      */   public static boolean containsAny(List<Type> paramList1, List<Type> paramList2) {
/*  464 */     for (Type type : paramList1) {
/*  465 */       if (type.containsAny(paramList2)) return true;
/*  466 */     }  return false;
/*      */   }
/*      */
/*      */   public static List<Type> filter(List<Type> paramList, Filter<Type> paramFilter) {
/*  470 */     ListBuffer listBuffer = new ListBuffer();
/*  471 */     for (Type type : paramList) {
/*  472 */       if (paramFilter.accepts(type)) {
/*  473 */         listBuffer.append(type);
/*      */       }
/*      */     }
/*  476 */     return listBuffer.toList();
/*      */   }
/*      */
/*  479 */   public boolean isSuperBound() { return false; }
/*  480 */   public boolean isExtendsBound() { return false; }
/*  481 */   public boolean isUnbound() { return false; } public Type withTypeVar(Type paramType) {
/*  482 */     return this;
/*      */   }
/*      */
/*      */   public MethodType asMethodType() {
/*  486 */     throw new AssertionError();
/*      */   }
/*      */
/*      */
/*      */   public void complete() {}
/*      */
/*      */   public Symbol.TypeSymbol asElement() {
/*  493 */     return this.tsym;
/*      */   }
/*      */
/*      */
/*      */   public TypeKind getKind() {
/*  498 */     return TypeKind.OTHER;
/*      */   }
/*      */
/*      */
/*      */   public <R, P> R accept(TypeVisitor<R, P> paramTypeVisitor, P paramP) {
/*  503 */     throw new AssertionError();
/*      */   }
/*      */
/*      */   public abstract TypeTag getTag();
/*      */
/*      */   public static class JCPrimitiveType extends Type implements PrimitiveType {
/*      */     TypeTag tag;
/*      */
/*      */     public JCPrimitiveType(TypeTag param1TypeTag, Symbol.TypeSymbol param1TypeSymbol) {
/*  512 */       super(param1TypeSymbol);
/*  513 */       this.tag = param1TypeTag;
/*  514 */       Assert.check(param1TypeTag.isPrimitive);
/*      */     }
/*      */
/*      */
/*      */     public boolean isNumeric() {
/*  519 */       return (this.tag != TypeTag.BOOLEAN);
/*      */     }
/*      */
/*      */
/*      */     public boolean isPrimitive() {
/*  524 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/*  529 */       return this.tag;
/*      */     }
/*      */
/*      */
/*      */     public boolean isPrimitiveOrVoid() {
/*  534 */       return true;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public Type constType(Object param1Object) {
/*  542 */       final Object value = param1Object;
/*  543 */       return new JCPrimitiveType(this.tag, this.tsym)
/*      */         {
/*      */           public Object constValue() {
/*  546 */             return value;
/*      */           }
/*      */
/*      */           public Type baseType() {
/*  550 */             return this.tsym.type;
/*      */           }
/*      */         };
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public String stringValue() {
/*  560 */       Object object = Assert.checkNonNull(constValue());
/*  561 */       if (this.tag == TypeTag.BOOLEAN) {
/*  562 */         return (((Integer)object).intValue() == 0) ? "false" : "true";
/*      */       }
/*  564 */       if (this.tag == TypeTag.CHAR) {
/*  565 */         return String.valueOf((char)((Integer)object).intValue());
/*      */       }
/*      */
/*  568 */       return object.toString();
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public boolean isFalse() {
/*  576 */       return (this.tag == TypeTag.BOOLEAN &&
/*      */
/*  578 */         constValue() != null && ((Integer)
/*  579 */         constValue()).intValue() == 0);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public boolean isTrue() {
/*  586 */       return (this.tag == TypeTag.BOOLEAN &&
/*      */
/*  588 */         constValue() != null && ((Integer)
/*  589 */         constValue()).intValue() != 0);
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/*  594 */       return param1TypeVisitor.visitPrimitive(this, param1P);
/*      */     }
/*      */
/*      */
/*      */
/*      */     public TypeKind getKind() {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/code/Type$4.$SwitchMap$com$sun$tools$javac$code$TypeTag : [I
/*      */       //   3: aload_0
/*      */       //   4: getfield tag : Lcom/sun/tools/javac/code/TypeTag;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 88, 1 -> 56, 2 -> 60, 3 -> 64, 4 -> 68, 5 -> 72, 6 -> 76, 7 -> 80, 8 -> 84
/*      */       //   56: getstatic javax/lang/model/type/TypeKind.BYTE : Ljavax/lang/model/type/TypeKind;
/*      */       //   59: areturn
/*      */       //   60: getstatic javax/lang/model/type/TypeKind.CHAR : Ljavax/lang/model/type/TypeKind;
/*      */       //   63: areturn
/*      */       //   64: getstatic javax/lang/model/type/TypeKind.SHORT : Ljavax/lang/model/type/TypeKind;
/*      */       //   67: areturn
/*      */       //   68: getstatic javax/lang/model/type/TypeKind.INT : Ljavax/lang/model/type/TypeKind;
/*      */       //   71: areturn
/*      */       //   72: getstatic javax/lang/model/type/TypeKind.LONG : Ljavax/lang/model/type/TypeKind;
/*      */       //   75: areturn
/*      */       //   76: getstatic javax/lang/model/type/TypeKind.FLOAT : Ljavax/lang/model/type/TypeKind;
/*      */       //   79: areturn
/*      */       //   80: getstatic javax/lang/model/type/TypeKind.DOUBLE : Ljavax/lang/model/type/TypeKind;
/*      */       //   83: areturn
/*      */       //   84: getstatic javax/lang/model/type/TypeKind.BOOLEAN : Ljavax/lang/model/type/TypeKind;
/*      */       //   87: areturn
/*      */       //   88: new java/lang/AssertionError
/*      */       //   91: dup
/*      */       //   92: invokespecial <init> : ()V
/*      */       //   95: athrow
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #599	-> 0
/*      */       //   #600	-> 56
/*      */       //   #601	-> 60
/*      */       //   #602	-> 64
/*      */       //   #603	-> 68
/*      */       //   #604	-> 72
/*      */       //   #605	-> 76
/*      */       //   #606	-> 80
/*      */       //   #607	-> 84
/*      */       //   #609	-> 88
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static class WildcardType
/*      */     extends Type
/*      */     implements javax.lang.model.type.WildcardType
/*      */   {
/*      */     public Type type;
/*      */
/*      */
/*      */     public BoundKind kind;
/*      */
/*      */
/*      */     public TypeVar bound;
/*      */
/*      */
/*      */     boolean isPrintingBound;
/*      */
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/*  623 */       return param1Visitor.visitWildcardType(this, param1S);
/*      */     }
/*      */
/*      */     public WildcardType(Type param1Type, BoundKind param1BoundKind, Symbol.TypeSymbol param1TypeSymbol) {
/*  627 */       super(param1TypeSymbol);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  681 */       this.isPrintingBound = false; this.type = (Type)Assert.checkNonNull(param1Type); this.kind = param1BoundKind;
/*      */     } public WildcardType(WildcardType param1WildcardType, TypeVar param1TypeVar) { this(param1WildcardType.type, param1WildcardType.kind, param1WildcardType.tsym, param1TypeVar); } public WildcardType(Type param1Type, BoundKind param1BoundKind, Symbol.TypeSymbol param1TypeSymbol, TypeVar param1TypeVar) { this(param1Type, param1BoundKind, param1TypeSymbol); this.bound = param1TypeVar; } public TypeTag getTag() { return TypeTag.WILDCARD; } public boolean contains(Type param1Type) { return (this.kind != BoundKind.UNBOUND && this.type.contains(param1Type)); } public boolean isSuperBound() { return (this.kind == BoundKind.SUPER || this.kind == BoundKind.UNBOUND); } public boolean isExtendsBound() { return (this.kind == BoundKind.EXTENDS || this.kind == BoundKind.UNBOUND); } public boolean isUnbound() { return (this.kind == BoundKind.UNBOUND); } public boolean isReference() { return true; } public boolean isNullOrReference() { return true; } public Type withTypeVar(Type param1Type) { if (this.bound == param1Type) return this;  this.bound = (TypeVar)param1Type; return this; }
/*  683 */     public String toString() { StringBuilder stringBuilder = new StringBuilder();
/*  684 */       stringBuilder.append(this.kind.toString());
/*  685 */       if (this.kind != BoundKind.UNBOUND)
/*  686 */         stringBuilder.append(this.type);
/*  687 */       if (moreInfo && this.bound != null && !this.isPrintingBound)
/*      */         try {
/*  689 */           this.isPrintingBound = true;
/*  690 */           stringBuilder.append("{:").append(this.bound.bound).append(":}");
/*      */         } finally {
/*  692 */           this.isPrintingBound = false;
/*      */         }
/*  694 */       return stringBuilder.toString(); }
/*      */
/*      */
/*      */
/*      */     public Type map(Mapping param1Mapping) {
/*  699 */       Type type = this.type;
/*  700 */       if (type != null)
/*  701 */         type = param1Mapping.apply(type);
/*  702 */       if (type == this.type) {
/*  703 */         return this;
/*      */       }
/*  705 */       return new WildcardType(type, this.kind, this.tsym, this.bound);
/*      */     }
/*      */
/*      */     public Type getExtendsBound() {
/*  709 */       if (this.kind == BoundKind.EXTENDS) {
/*  710 */         return this.type;
/*      */       }
/*  712 */       return null;
/*      */     }
/*      */
/*      */     public Type getSuperBound() {
/*  716 */       if (this.kind == BoundKind.SUPER) {
/*  717 */         return this.type;
/*      */       }
/*  719 */       return null;
/*      */     }
/*      */
/*      */     public TypeKind getKind() {
/*  723 */       return TypeKind.WILDCARD;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/*  727 */       return param1TypeVisitor.visitWildcard(this, param1P);
/*      */     } }
/*      */   public static class ClassType extends Type implements DeclaredType { private Type outer_field; public List<Type> typarams_field; public List<Type> allparams_field; public Type supertype_field; public List<Type> interfaces_field; public List<Type> all_interfaces_field; int rank_field;
/*      */     public TypeTag getTag() {
/*      */       return TypeTag.CLASS;
/*      */     }
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/*      */       return param1Visitor.visitClassType(this, param1S);
/*      */     }
/*      */     public Type constType(Object param1Object) {
/*      */       final Object value = param1Object;
/*      */       return new ClassType(getEnclosingType(), this.typarams_field, this.tsym) { public Object constValue() {
/*      */             return value;
/*      */           }
/*      */           public Type baseType() {
/*      */             return this.tsym.type;
/*      */           } }
/*      */         ;
/*      */     }
/*      */     public String toString() {
/*      */       StringBuilder stringBuilder = new StringBuilder();
/*      */       if (getEnclosingType().hasTag(TypeTag.CLASS) && this.tsym.owner.kind == 2) {
/*      */         stringBuilder.append(getEnclosingType().toString());
/*      */         stringBuilder.append(".");
/*      */         stringBuilder.append(className(this.tsym, false));
/*      */       } else {
/*      */         stringBuilder.append(className(this.tsym, true));
/*      */       }
/*      */       if (getTypeArguments().nonEmpty()) {
/*      */         stringBuilder.append('<');
/*      */         stringBuilder.append(getTypeArguments().toString());
/*      */         stringBuilder.append(">");
/*      */       }
/*      */       return stringBuilder.toString();
/*      */     }
/*  762 */     public ClassType(Type param1Type, List<Type> param1List, Symbol.TypeSymbol param1TypeSymbol) { super(param1TypeSymbol);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  903 */       this.rank_field = -1; this.outer_field = param1Type; this.typarams_field = param1List; this.allparams_field = null; this.supertype_field = null; this.interfaces_field = null; } private String className(Symbol param1Symbol, boolean param1Boolean) { if (param1Symbol.name.isEmpty() && (param1Symbol.flags() & 0x1000000L) != 0L) { StringBuilder stringBuilder = new StringBuilder(this.supertype_field.toString()); for (List<Type> list = this.interfaces_field; list.nonEmpty(); list = list.tail) { stringBuilder.append("&"); stringBuilder.append(((Type)list.head).toString()); }  return stringBuilder.toString(); }  if (param1Symbol.name.isEmpty()) { String str; ClassType classType = (ClassType)this.tsym.type.unannotatedType(); if (classType == null) { str = Log.getLocalizedString("anonymous.class", new Object[] { null }); } else if (classType.interfaces_field != null && classType.interfaces_field.nonEmpty()) { str = Log.getLocalizedString("anonymous.class", new Object[] { classType.interfaces_field.head }); } else { str = Log.getLocalizedString("anonymous.class", new Object[] { classType.supertype_field }); }  if (moreInfo)
/*      */           str = str + String.valueOf(param1Symbol.hashCode());  return str; }
/*      */        if (param1Boolean)
/*      */         return param1Symbol.getQualifiedName().toString();  return param1Symbol.name.toString(); }
/*      */     public List<Type> getTypeArguments() { if (this.typarams_field == null) { complete(); if (this.typarams_field == null)
/*      */           this.typarams_field = List.nil();  }
/*      */        return this.typarams_field; }
/*      */     public boolean hasErasedSupertypes() { return isRaw(); }
/*  911 */     public boolean isRaw() { return (this != this.tsym.type && this.tsym.type
/*      */
/*  913 */         .allparams().nonEmpty() &&
/*  914 */         allparams().isEmpty()); } public Type getEnclosingType() { return this.outer_field; } public void setEnclosingType(Type param1Type) { this.outer_field = param1Type; } public List<Type> allparams() { if (this.allparams_field == null) this.allparams_field = getTypeArguments().prependList(getEnclosingType().allparams());  return this.allparams_field; } public boolean isErroneous() { return (getEnclosingType().isErroneous() || isErroneous(getTypeArguments()) || (this != this.tsym.type.unannotatedType() && this.tsym.type.isErroneous())); }
/*      */     public boolean isParameterized() { return ((allparams()).tail != null); }
/*      */     public boolean isReference() { return true; }
/*      */     public boolean isNullOrReference() { return true; }
/*  918 */     public Type map(Mapping param1Mapping) { Type type1 = getEnclosingType();
/*  919 */       Type type2 = param1Mapping.apply(type1);
/*  920 */       List<Type> list1 = getTypeArguments();
/*  921 */       List<Type> list2 = map(list1, param1Mapping);
/*  922 */       if (type2 == type1 && list2 == list1) return this;
/*  923 */       return new ClassType(type2, list2, this.tsym); }
/*      */
/*      */
/*      */     public boolean contains(Type param1Type) {
/*  927 */       return (param1Type == this || (
/*      */
/*  929 */         isParameterized() && (
/*  930 */         getEnclosingType().contains(param1Type) || contains(getTypeArguments(), param1Type))) || (
/*  931 */         isCompound() && (this.supertype_field
/*  932 */         .contains(param1Type) || contains(this.interfaces_field, param1Type))));
/*      */     }
/*      */
/*      */     public void complete() {
/*  936 */       if (this.tsym.completer != null) this.tsym.complete();
/*      */     }
/*      */
/*      */     public TypeKind getKind() {
/*  940 */       return TypeKind.DECLARED;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/*  944 */       return param1TypeVisitor.visitDeclared(this, param1P);
/*      */     } }
/*      */
/*      */
/*      */   public static class ErasedClassType extends ClassType {
/*      */     public ErasedClassType(Type param1Type, Symbol.TypeSymbol param1TypeSymbol) {
/*  950 */       super(param1Type, List.nil(), param1TypeSymbol);
/*      */     }
/*      */
/*      */
/*      */     public boolean hasErasedSupertypes() {
/*  955 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */   public static class UnionClassType
/*      */     extends ClassType implements UnionType {
/*      */     final List<? extends Type> alternatives_field;
/*      */
/*      */     public UnionClassType(ClassType param1ClassType, List<? extends Type> param1List) {
/*  964 */       super(param1ClassType.outer_field, param1ClassType.typarams_field, param1ClassType.tsym);
/*  965 */       this.allparams_field = param1ClassType.allparams_field;
/*  966 */       this.supertype_field = param1ClassType.supertype_field;
/*  967 */       this.interfaces_field = param1ClassType.interfaces_field;
/*  968 */       this.all_interfaces_field = param1ClassType.interfaces_field;
/*  969 */       this.alternatives_field = param1List;
/*      */     }
/*      */
/*      */     public Type getLub() {
/*  973 */       return this.tsym.type;
/*      */     }
/*      */
/*      */     public List<? extends TypeMirror> getAlternatives() {
/*  977 */       return Collections.unmodifiableList((List)this.alternatives_field);
/*      */     }
/*      */
/*      */
/*      */     public boolean isUnion() {
/*  982 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public TypeKind getKind() {
/*  987 */       return TypeKind.UNION;
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/*  992 */       return param1TypeVisitor.visitUnion(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */   public static class IntersectionClassType
/*      */     extends ClassType
/*      */     implements IntersectionType {
/*      */     public boolean allInterfaces;
/*      */
/*      */     public IntersectionClassType(List<Type> param1List, Symbol.ClassSymbol param1ClassSymbol, boolean param1Boolean) {
/* 1002 */       super(Type.noType, List.nil(), param1ClassSymbol);
/* 1003 */       this.allInterfaces = param1Boolean;
/* 1004 */       Assert.check(((param1ClassSymbol.flags() & 0x1000000L) != 0L));
/* 1005 */       this.supertype_field = (Type)param1List.head;
/* 1006 */       this.interfaces_field = param1List.tail;
/* 1007 */       Assert.check((this.supertype_field.tsym.completer != null ||
/* 1008 */           !this.supertype_field.isInterface()), this.supertype_field);
/*      */     }
/*      */
/*      */     public List<? extends TypeMirror> getBounds() {
/* 1012 */       return Collections.unmodifiableList((List)getExplicitComponents());
/*      */     }
/*      */
/*      */     public List<Type> getComponents() {
/* 1016 */       return this.interfaces_field.prepend(this.supertype_field);
/*      */     }
/*      */
/*      */
/*      */     public boolean isIntersection() {
/* 1021 */       return true;
/*      */     }
/*      */
/*      */     public List<Type> getExplicitComponents() {
/* 1025 */       return this.allInterfaces ? this.interfaces_field :
/*      */
/* 1027 */         getComponents();
/*      */     }
/*      */
/*      */
/*      */     public TypeKind getKind() {
/* 1032 */       return TypeKind.INTERSECTION;
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1037 */       return param1TypeVisitor.visitIntersection(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */   public static class ArrayType
/*      */     extends Type
/*      */     implements javax.lang.model.type.ArrayType {
/*      */     public Type elemtype;
/*      */
/*      */     public ArrayType(Type param1Type, Symbol.TypeSymbol param1TypeSymbol) {
/* 1047 */       super(param1TypeSymbol);
/* 1048 */       this.elemtype = param1Type;
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1053 */       return TypeTag.ARRAY;
/*      */     }
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1057 */       return param1Visitor.visitArrayType(this, param1S);
/*      */     }
/*      */
/*      */     public String toString() {
/* 1061 */       return this.elemtype + "[]";
/*      */     }
/*      */
/*      */     public boolean equals(Object param1Object) {
/* 1065 */       return (this == param1Object || (param1Object instanceof ArrayType && this.elemtype
/*      */
/*      */
/* 1068 */         .equals(((ArrayType)param1Object).elemtype)));
/*      */     }
/*      */
/*      */     public int hashCode() {
/* 1072 */       return (TypeTag.ARRAY.ordinal() << 5) + this.elemtype.hashCode();
/*      */     }
/*      */
/*      */     public boolean isVarargs() {
/* 1076 */       return false;
/*      */     }
/*      */     public List<Type> allparams() {
/* 1079 */       return this.elemtype.allparams();
/*      */     }
/*      */     public boolean isErroneous() {
/* 1082 */       return this.elemtype.isErroneous();
/*      */     }
/*      */
/*      */     public boolean isParameterized() {
/* 1086 */       return this.elemtype.isParameterized();
/*      */     }
/*      */
/*      */
/*      */     public boolean isReference() {
/* 1091 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public boolean isNullOrReference() {
/* 1096 */       return true;
/*      */     }
/*      */
/*      */     public boolean isRaw() {
/* 1100 */       return this.elemtype.isRaw();
/*      */     }
/*      */
/*      */     public ArrayType makeVarargs() {
/* 1104 */       return new ArrayType(this.elemtype, this.tsym)
/*      */         {
/*      */           public boolean isVarargs() {
/* 1107 */             return true;
/*      */           }
/*      */         };
/*      */     }
/*      */
/*      */     public Type map(Mapping param1Mapping) {
/* 1113 */       Type type = param1Mapping.apply(this.elemtype);
/* 1114 */       if (type == this.elemtype) return this;
/* 1115 */       return new ArrayType(type, this.tsym);
/*      */     }
/*      */
/*      */     public boolean contains(Type param1Type) {
/* 1119 */       return (param1Type == this || this.elemtype.contains(param1Type));
/*      */     }
/*      */
/*      */     public void complete() {
/* 1123 */       this.elemtype.complete();
/*      */     }
/*      */
/*      */     public Type getComponentType() {
/* 1127 */       return this.elemtype;
/*      */     }
/*      */
/*      */     public TypeKind getKind() {
/* 1131 */       return TypeKind.ARRAY;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1135 */       return param1TypeVisitor.visitArray(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class MethodType
/*      */     extends Type
/*      */     implements ExecutableType
/*      */   {
/*      */     public List<Type> argtypes;
/*      */
/*      */     public Type restype;
/*      */
/*      */     public List<Type> thrown;
/*      */
/*      */     public Type recvtype;
/*      */
/*      */     public MethodType(List<Type> param1List1, Type param1Type, List<Type> param1List2, Symbol.TypeSymbol param1TypeSymbol) {
/* 1153 */       super(param1TypeSymbol);
/* 1154 */       this.argtypes = param1List1;
/* 1155 */       this.restype = param1Type;
/* 1156 */       this.thrown = param1List2;
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1161 */       return TypeTag.METHOD;
/*      */     }
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1165 */       return param1Visitor.visitMethodType(this, param1S);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public String toString() {
/* 1174 */       return "(" + this.argtypes + ")" + this.restype;
/*      */     }
/*      */
/* 1177 */     public List<Type> getParameterTypes() { return this.argtypes; }
/* 1178 */     public Type getReturnType() { return this.restype; }
/* 1179 */     public Type getReceiverType() { return this.recvtype; } public List<Type> getThrownTypes() {
/* 1180 */       return this.thrown;
/*      */     }
/*      */     public boolean isErroneous() {
/* 1183 */       return (
/* 1184 */         isErroneous(this.argtypes) || (this.restype != null && this.restype
/* 1185 */         .isErroneous()));
/*      */     }
/*      */
/*      */     public Type map(Mapping param1Mapping) {
/* 1189 */       List<Type> list1 = map(this.argtypes, param1Mapping);
/* 1190 */       Type type = param1Mapping.apply(this.restype);
/* 1191 */       List<Type> list2 = map(this.thrown, param1Mapping);
/* 1192 */       if (list1 == this.argtypes && type == this.restype && list2 == this.thrown)
/*      */       {
/* 1194 */         return this; }
/* 1195 */       return new MethodType(list1, type, list2, this.tsym);
/*      */     }
/*      */
/*      */     public boolean contains(Type param1Type) {
/* 1199 */       return (param1Type == this || contains(this.argtypes, param1Type) || this.restype.contains(param1Type) || contains(this.thrown, param1Type));
/*      */     }
/*      */     public MethodType asMethodType() {
/* 1202 */       return this;
/*      */     } public void complete() {
/*      */       List<Type> list;
/* 1205 */       for (list = this.argtypes; list.nonEmpty(); list = list.tail)
/* 1206 */         ((Type)list.head).complete();
/* 1207 */       this.restype.complete();
/* 1208 */       this.recvtype.complete();
/* 1209 */       for (list = this.thrown; list.nonEmpty(); list = list.tail)
/* 1210 */         ((Type)list.head).complete();
/*      */     }
/*      */
/*      */     public List<TypeVar> getTypeVariables() {
/* 1214 */       return List.nil();
/*      */     }
/*      */
/*      */     public Symbol.TypeSymbol asElement() {
/* 1218 */       return null;
/*      */     }
/*      */
/*      */     public TypeKind getKind() {
/* 1222 */       return TypeKind.EXECUTABLE;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1226 */       return param1TypeVisitor.visitExecutable(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */   public static class PackageType
/*      */     extends Type implements NoType {
/*      */     PackageType(Symbol.TypeSymbol param1TypeSymbol) {
/* 1233 */       super(param1TypeSymbol);
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1238 */       return TypeTag.PACKAGE;
/*      */     }
/*      */
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1243 */       return param1Visitor.visitPackageType(this, param1S);
/*      */     }
/*      */
/*      */     public String toString() {
/* 1247 */       return this.tsym.getQualifiedName().toString();
/*      */     }
/*      */
/*      */     public TypeKind getKind() {
/* 1251 */       return TypeKind.PACKAGE;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1255 */       return param1TypeVisitor.visitNoType(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static class TypeVar
/*      */     extends Type
/*      */     implements TypeVariable
/*      */   {
/* 1271 */     public Type bound = null;
/*      */
/*      */
/*      */     public Type lower;
/*      */
/*      */     int rank_field;
/*      */
/*      */
/*      */     public TypeVar(Name param1Name, Symbol param1Symbol, Type param1Type)
/*      */     {
/* 1281 */       super((Symbol.TypeSymbol)null);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1310 */       this.rank_field = -1; this.tsym = new Symbol.TypeVariableSymbol(0L, param1Name, this, param1Symbol); this.lower = param1Type; } public TypeVar(Symbol.TypeSymbol param1TypeSymbol, Type param1Type1, Type param1Type2) { super(param1TypeSymbol); this.rank_field = -1; this.bound = param1Type1; this.lower = param1Type2; } public TypeTag getTag() { return TypeTag.TYPEVAR; }
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) { return param1Visitor.visitTypeVar(this, param1S); }
/*      */     public Type getUpperBound() { if ((this.bound == null || this.bound.hasTag(TypeTag.NONE)) && this != this.tsym.type)
/*      */         this.bound = this.tsym.type.getUpperBound();  return this.bound; }
/* 1314 */     public Type getLowerBound() { return this.lower; }
/*      */
/*      */
/*      */     public TypeKind getKind() {
/* 1318 */       return TypeKind.TYPEVAR;
/*      */     }
/*      */
/*      */     public boolean isCaptured() {
/* 1322 */       return false;
/*      */     }
/*      */
/*      */
/*      */     public boolean isReference() {
/* 1327 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public boolean isNullOrReference() {
/* 1332 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1337 */       return param1TypeVisitor.visitTypeVariable(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static class CapturedType
/*      */     extends TypeVar
/*      */   {
/*      */     public WildcardType wildcard;
/*      */
/*      */
/*      */
/*      */
/*      */     public CapturedType(Name param1Name, Symbol param1Symbol, Type param1Type1, Type param1Type2, WildcardType param1WildcardType) {
/* 1354 */       super(param1Name, param1Symbol, param1Type2);
/* 1355 */       this.lower = (Type)Assert.checkNonNull(param1Type2);
/* 1356 */       this.bound = param1Type1;
/* 1357 */       this.wildcard = param1WildcardType;
/*      */     }
/*      */
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1362 */       return param1Visitor.visitCapturedType(this, param1S);
/*      */     }
/*      */
/*      */
/*      */     public boolean isCaptured() {
/* 1367 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public String toString() {
/* 1372 */       return "capture#" + ((
/* 1373 */         hashCode() & 0xFFFFFFFFL) % 997L) + " of " + this.wildcard;
/*      */     }
/*      */   }
/*      */
/*      */   public static abstract class DelegatedType
/*      */     extends Type {
/*      */     public Type qtype;
/*      */     public TypeTag tag;
/*      */
/*      */     public DelegatedType(TypeTag param1TypeTag, Type param1Type) {
/* 1383 */       super(param1Type.tsym);
/* 1384 */       this.tag = param1TypeTag;
/* 1385 */       this.qtype = param1Type;
/*      */     }
/* 1387 */     public TypeTag getTag() { return this.tag; }
/* 1388 */     public String toString() { return this.qtype.toString(); }
/* 1389 */     public List<Type> getTypeArguments() { return this.qtype.getTypeArguments(); }
/* 1390 */     public Type getEnclosingType() { return this.qtype.getEnclosingType(); }
/* 1391 */     public List<Type> getParameterTypes() { return this.qtype.getParameterTypes(); }
/* 1392 */     public Type getReturnType() { return this.qtype.getReturnType(); }
/* 1393 */     public Type getReceiverType() { return this.qtype.getReceiverType(); }
/* 1394 */     public List<Type> getThrownTypes() { return this.qtype.getThrownTypes(); }
/* 1395 */     public List<Type> allparams() { return this.qtype.allparams(); }
/* 1396 */     public Type getUpperBound() { return this.qtype.getUpperBound(); } public boolean isErroneous() {
/* 1397 */       return this.qtype.isErroneous();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class ForAll
/*      */     extends DelegatedType
/*      */     implements ExecutableType
/*      */   {
/*      */     public List<Type> tvars;
/*      */
/*      */     public ForAll(List<Type> param1List, Type param1Type) {
/* 1409 */       super(TypeTag.FORALL, param1Type);
/* 1410 */       this.tvars = param1List;
/*      */     }
/*      */
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1415 */       return param1Visitor.visitForAll(this, param1S);
/*      */     }
/*      */
/*      */     public String toString() {
/* 1419 */       return "<" + this.tvars + ">" + this.qtype;
/*      */     }
/*      */     public List<Type> getTypeArguments() {
/* 1422 */       return this.tvars;
/*      */     }
/*      */     public boolean isErroneous() {
/* 1425 */       return this.qtype.isErroneous();
/*      */     }
/*      */
/*      */     public Type map(Mapping param1Mapping) {
/* 1429 */       return param1Mapping.apply(this.qtype);
/*      */     }
/*      */
/*      */     public boolean contains(Type param1Type) {
/* 1433 */       return this.qtype.contains(param1Type);
/*      */     }
/*      */
/*      */     public MethodType asMethodType() {
/* 1437 */       return (MethodType)this.qtype;
/*      */     }
/*      */
/*      */     public void complete() {
/* 1441 */       for (List<Type> list = this.tvars; list.nonEmpty(); list = list.tail) {
/* 1442 */         ((TypeVar)list.head).bound.complete();
/*      */       }
/* 1444 */       this.qtype.complete();
/*      */     }
/*      */
/*      */     public List<TypeVar> getTypeVariables() {
/* 1448 */       return List.convert(TypeVar.class, getTypeArguments());
/*      */     }
/*      */
/*      */     public TypeKind getKind() {
/* 1452 */       return TypeKind.EXECUTABLE;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1456 */       return param1TypeVisitor.visitExecutable(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static class UndetVar
/*      */     extends DelegatedType
/*      */   {
/*      */     protected Map<InferenceBound, List<Type>> bounds;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public enum InferenceBound
/*      */     {
/* 1481 */       UPPER { public InferenceBound complement() {
/* 1482 */           return LOWER;
/*      */         } }
/*      */       ,
/* 1485 */       LOWER { public InferenceBound complement() {
/* 1486 */           return UPPER;
/*      */         } }
/*      */       ,
/* 1489 */       EQ { public InferenceBound complement() {
/* 1490 */           return EQ;
/*      */         } }
/*      */       ;
/*      */
/*      */
/*      */
/*      */       public abstract InferenceBound complement();
/*      */     }
/*      */
/*      */
/* 1500 */     public Type inst = null;
/*      */
/*      */
/*      */     public int declaredCount;
/*      */
/*      */
/* 1506 */     public UndetVarListener listener = null;
/*      */     Mapping toTypeVarMap;
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1510 */       return param1Visitor.visitUndetVar(this, param1S);
/*      */     }
/*      */
/*      */     public UndetVar(TypeVar param1TypeVar, Types param1Types) {
/* 1514 */       super(TypeTag.UNDETVAR, param1TypeVar);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1593 */       this.toTypeVarMap = new Mapping("toTypeVarMap")
/*      */         {
/*      */           public Type apply(Type param2Type) {
/* 1596 */             if (param2Type.hasTag(TypeTag.UNDETVAR)) {
/* 1597 */               UndetVar undetVar = (UndetVar)param2Type;
/* 1598 */               return (undetVar.inst != null) ? undetVar.inst : undetVar.qtype;
/*      */             }
/* 1600 */             return param2Type.map(this);
/*      */           }
/*      */         }; this.bounds = new EnumMap<>(InferenceBound.class); List<Type> list = param1Types.getBounds(param1TypeVar); this.declaredCount = list.length(); this.bounds.put(InferenceBound.UPPER, list); this.bounds.put(InferenceBound.LOWER, List.nil()); this.bounds.put(InferenceBound.EQ, List.nil());
/*      */     }
/*      */     public String toString() { return (this.inst == null) ? (this.qtype + "?") : this.inst.toString(); } public String debugString() { String str = "inference var = " + this.qtype + "\n"; if (this.inst != null)
/*      */         str = str + "inst = " + this.inst + '\n';  for (InferenceBound inferenceBound : InferenceBound.values()) { List list = this.bounds.get(inferenceBound); if (list.size() > 0)
/*      */           str = str + inferenceBound + " = " + list + '\n';  }
/* 1607 */        return str; } public boolean isPartial() { return true; } public Type baseType() { return (this.inst == null) ? this : this.inst.baseType(); } public void substBounds(List<Type> param1List1, List<Type> param1List2, Types param1Types) { List<Type> list = param1List1.diff(param1List2);
/*      */
/* 1609 */       if (list.isEmpty())
/* 1610 */         return;  final EnumSet<InferenceBound> boundsChanged = EnumSet.noneOf(InferenceBound.class);
/* 1611 */       UndetVarListener undetVarListener = this.listener;
/*      */
/*      */
/* 1614 */       try { this.listener = new UndetVarListener() {
/*      */             public void varChanged(UndetVar param2UndetVar, Set<InferenceBound> param2Set) {
/* 1616 */               boundsChanged.addAll(param2Set);
/*      */             }
/*      */           };
/* 1619 */         for (Map.Entry<InferenceBound, List<Type>> entry : this.bounds.entrySet()) {
/* 1620 */           InferenceBound inferenceBound = (InferenceBound)entry.getKey();
/* 1621 */           List list1 = (List)entry.getValue();
/* 1622 */           ListBuffer listBuffer1 = new ListBuffer();
/* 1623 */           ListBuffer listBuffer2 = new ListBuffer();
/*      */
/* 1625 */           for (Type type : list1) {
/* 1626 */             if (!type.containsAny(list)) {
/* 1627 */               listBuffer1.append(type); continue;
/*      */             }
/* 1629 */             listBuffer2.append(type);
/*      */           }
/*      */
/*      */
/* 1633 */           this.bounds.put(inferenceBound, listBuffer1.toList());
/*      */
/* 1635 */           for (Type type : listBuffer2) {
/* 1636 */             addBound(inferenceBound, param1Types.subst(type, param1List1, param1List2), param1Types, true);
/*      */           }
/*      */         }  }
/*      */       finally
/* 1640 */       { this.listener = undetVarListener;
/* 1641 */         if (!enumSet.isEmpty())
/* 1642 */           notifyChange(enumSet);  }  }
/*      */     public List<Type> getBounds(InferenceBound... param1VarArgs) { ListBuffer listBuffer = new ListBuffer(); for (InferenceBound inferenceBound : param1VarArgs) listBuffer.appendList(this.bounds.get(inferenceBound));  return listBuffer.toList(); }
/*      */     public List<Type> getDeclaredBounds() { ListBuffer listBuffer = new ListBuffer(); byte b = 0; for (Type type : getBounds(new InferenceBound[] { InferenceBound.UPPER })) { if (b++ == this.declaredCount) break;  listBuffer.append(type); }  return listBuffer.toList(); }
/*      */     public void setBounds(InferenceBound param1InferenceBound, List<Type> param1List) { this.bounds.put(param1InferenceBound, param1List); }
/*      */     public final void addBound(InferenceBound param1InferenceBound, Type param1Type, Types param1Types) { addBound(param1InferenceBound, param1Type, param1Types, false); }
/*      */     protected void addBound(InferenceBound param1InferenceBound, Type param1Type, Types param1Types, boolean param1Boolean) { Type type = this.toTypeVarMap.apply(param1Type).baseType(); List list = this.bounds.get(param1InferenceBound); for (Type type1 : list) { if (param1Types.isSameType(type1, type, true) || param1Type == this.qtype)
/* 1648 */           return;  }  this.bounds.put(param1InferenceBound, list.prepend(type)); notifyChange(EnumSet.of(param1InferenceBound)); } private void notifyChange(EnumSet<InferenceBound> param1EnumSet) { if (this.listener != null) {
/* 1649 */         this.listener.varChanged(this, param1EnumSet);
/*      */       } }
/*      */
/*      */
/*      */     public boolean isCaptured() {
/* 1654 */       return false;
/*      */     }
/*      */
/*      */     public static interface UndetVarListener
/*      */     {
/*      */       void varChanged(UndetVar param2UndetVar, Set<InferenceBound> param2Set);
/*      */     }
/*      */   }
/*      */
/*      */   public static class CapturedUndetVar
/*      */     extends UndetVar
/*      */   {
/*      */     public CapturedUndetVar(CapturedType param1CapturedType, Types param1Types) {
/* 1667 */       super(param1CapturedType, param1Types);
/* 1668 */       if (!param1CapturedType.lower.hasTag(TypeTag.BOT)) {
/* 1669 */         this.bounds.put(InferenceBound.LOWER, List.of(param1CapturedType.lower));
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void addBound(InferenceBound param1InferenceBound, Type param1Type, Types param1Types, boolean param1Boolean) {
/* 1675 */       if (param1Boolean)
/*      */       {
/* 1677 */         super.addBound(param1InferenceBound, param1Type, param1Types, param1Boolean);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public boolean isCaptured() {
/* 1683 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCNoType
/*      */     extends Type
/*      */     implements NoType {
/*      */     public JCNoType() {
/* 1691 */       super((Symbol.TypeSymbol)null);
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1696 */       return TypeTag.NONE;
/*      */     }
/*      */
/*      */
/*      */     public TypeKind getKind() {
/* 1701 */       return TypeKind.NONE;
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1706 */       return param1TypeVisitor.visitNoType(this, param1P);
/*      */     }
/*      */
/*      */     public boolean isCompound() {
/* 1710 */       return false;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCVoidType
/*      */     extends Type
/*      */     implements NoType {
/*      */     public JCVoidType() {
/* 1718 */       super((Symbol.TypeSymbol)null);
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1723 */       return TypeTag.VOID;
/*      */     }
/*      */
/*      */
/*      */     public TypeKind getKind() {
/* 1728 */       return TypeKind.VOID;
/*      */     }
/*      */
/*      */     public boolean isCompound() {
/* 1732 */       return false;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1736 */       return param1TypeVisitor.visitNoType(this, param1P);
/*      */     }
/*      */
/*      */
/*      */     public boolean isPrimitiveOrVoid() {
/* 1741 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */   static class BottomType extends Type implements NullType {
/*      */     public BottomType() {
/* 1747 */       super((Symbol.TypeSymbol)null);
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1752 */       return TypeTag.BOT;
/*      */     }
/*      */
/*      */
/*      */     public TypeKind getKind() {
/* 1757 */       return TypeKind.NULL;
/*      */     }
/*      */
/*      */     public boolean isCompound() {
/* 1761 */       return false;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1765 */       return param1TypeVisitor.visitNull(this, param1P);
/*      */     }
/*      */
/*      */
/*      */     public Type constType(Object param1Object) {
/* 1770 */       return this;
/*      */     }
/*      */
/*      */
/*      */     public String stringValue() {
/* 1775 */       return "null";
/*      */     }
/*      */
/*      */
/*      */     public boolean isNullOrReference() {
/* 1780 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */   public static class ErrorType
/*      */     extends ClassType
/*      */     implements javax.lang.model.type.ErrorType
/*      */   {
/* 1788 */     private Type originalType = null;
/*      */
/*      */     public ErrorType(Type param1Type, Symbol.TypeSymbol param1TypeSymbol) {
/* 1791 */       super(noType, List.nil(), (Symbol.TypeSymbol)null);
/* 1792 */       this.tsym = param1TypeSymbol;
/* 1793 */       this.originalType = (param1Type == null) ? noType : param1Type;
/*      */     }
/*      */
/*      */     public ErrorType(Symbol.ClassSymbol param1ClassSymbol, Type param1Type) {
/* 1797 */       this(param1Type, param1ClassSymbol);
/* 1798 */       param1ClassSymbol.type = this;
/* 1799 */       param1ClassSymbol.kind = 63;
/* 1800 */       param1ClassSymbol.members_field = new Scope.ErrorScope(param1ClassSymbol);
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1805 */       return TypeTag.ERROR;
/*      */     }
/*      */
/*      */
/*      */     public boolean isPartial() {
/* 1810 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public boolean isReference() {
/* 1815 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public boolean isNullOrReference() {
/* 1820 */       return true;
/*      */     }
/*      */
/*      */     public ErrorType(Name param1Name, Symbol.TypeSymbol param1TypeSymbol, Type param1Type) {
/* 1824 */       this(new Symbol.ClassSymbol(1073741833L, param1Name, null, param1TypeSymbol), param1Type);
/*      */     }
/*      */
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1829 */       return param1Visitor.visitErrorType(this, param1S);
/*      */     }
/*      */
/* 1832 */     public Type constType(Object param1Object) { return this; }
/* 1833 */     public Type getEnclosingType() { return this; }
/* 1834 */     public Type getReturnType() { return this; }
/* 1835 */     public Type asSub(Symbol param1Symbol) { return this; } public Type map(Mapping param1Mapping) {
/* 1836 */       return this;
/*      */     }
/* 1838 */     public boolean isGenType(Type param1Type) { return true; }
/* 1839 */     public boolean isErroneous() { return true; }
/* 1840 */     public boolean isCompound() { return false; } public boolean isInterface() {
/* 1841 */       return false;
/*      */     }
/* 1843 */     public List<Type> allparams() { return List.nil(); } public List<Type> getTypeArguments() {
/* 1844 */       return List.nil();
/*      */     }
/*      */     public TypeKind getKind() {
/* 1847 */       return TypeKind.ERROR;
/*      */     }
/*      */
/*      */     public Type getOriginalType() {
/* 1851 */       return this.originalType;
/*      */     }
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1855 */       return param1TypeVisitor.visitError(this, param1P);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public static class AnnotatedType
/*      */     extends Type
/*      */     implements javax.lang.model.type.ArrayType, DeclaredType, PrimitiveType, TypeVariable, javax.lang.model.type.WildcardType
/*      */   {
/*      */     private List<Attribute.TypeCompound> typeAnnotations;
/*      */
/*      */
/*      */
/*      */     private Type underlyingType;
/*      */
/*      */
/*      */
/*      */
/*      */     protected AnnotatedType(List<Attribute.TypeCompound> param1List, Type param1Type) {
/* 1876 */       super(param1Type.tsym);
/* 1877 */       this.typeAnnotations = param1List;
/* 1878 */       this.underlyingType = param1Type;
/* 1879 */       Assert.check((param1List != null && param1List.nonEmpty()), "Can't create AnnotatedType without annotations: " + param1Type);
/*      */
/* 1881 */       Assert.check(!param1Type.isAnnotated(), "Can't annotate already annotated type: " + param1Type + "; adding: " + param1List);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 1888 */       return this.underlyingType.getTag();
/*      */     }
/*      */
/*      */
/*      */     public boolean isAnnotated() {
/* 1893 */       return true;
/*      */     }
/*      */
/*      */
/*      */     public List<Attribute.TypeCompound> getAnnotationMirrors() {
/* 1898 */       return this.typeAnnotations;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public TypeKind getKind() {
/* 1904 */       return this.underlyingType.getKind();
/*      */     }
/*      */
/*      */
/*      */     public Type unannotatedType() {
/* 1909 */       return this.underlyingType;
/*      */     }
/*      */
/*      */
/*      */     public <R, S> R accept(Visitor<R, S> param1Visitor, S param1S) {
/* 1914 */       return param1Visitor.visitAnnotatedType(this, param1S);
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 1919 */       return this.underlyingType.accept(param1TypeVisitor, param1P);
/*      */     }
/*      */
/*      */
/*      */     public Type map(Mapping param1Mapping) {
/* 1924 */       this.underlyingType.map(param1Mapping);
/* 1925 */       return this;
/*      */     }
/*      */
/*      */     public Type constType(Object param1Object) {
/* 1929 */       return this.underlyingType.constType(param1Object);
/*      */     } public Type getEnclosingType() {
/* 1931 */       return this.underlyingType.getEnclosingType();
/*      */     }
/*      */     public Type getReturnType() {
/* 1934 */       return this.underlyingType.getReturnType();
/*      */     } public List<Type> getTypeArguments() {
/* 1936 */       return this.underlyingType.getTypeArguments();
/*      */     } public List<Type> getParameterTypes() {
/* 1938 */       return this.underlyingType.getParameterTypes();
/*      */     } public Type getReceiverType() {
/* 1940 */       return this.underlyingType.getReceiverType();
/*      */     } public List<Type> getThrownTypes() {
/* 1942 */       return this.underlyingType.getThrownTypes();
/*      */     } public Type getUpperBound() {
/* 1944 */       return this.underlyingType.getUpperBound();
/*      */     } public Type getLowerBound() {
/* 1946 */       return this.underlyingType.getLowerBound();
/*      */     }
/*      */     public boolean isErroneous() {
/* 1949 */       return this.underlyingType.isErroneous();
/*      */     } public boolean isCompound() {
/* 1951 */       return this.underlyingType.isCompound();
/*      */     } public boolean isInterface() {
/* 1953 */       return this.underlyingType.isInterface();
/*      */     } public List<Type> allparams() {
/* 1955 */       return this.underlyingType.allparams();
/*      */     } public boolean isPrimitive() {
/* 1957 */       return this.underlyingType.isPrimitive();
/*      */     } public boolean isPrimitiveOrVoid() {
/* 1959 */       return this.underlyingType.isPrimitiveOrVoid();
/*      */     } public boolean isNumeric() {
/* 1961 */       return this.underlyingType.isNumeric();
/*      */     } public boolean isReference() {
/* 1963 */       return this.underlyingType.isReference();
/*      */     } public boolean isNullOrReference() {
/* 1965 */       return this.underlyingType.isNullOrReference();
/*      */     } public boolean isPartial() {
/* 1967 */       return this.underlyingType.isPartial();
/*      */     } public boolean isParameterized() {
/* 1969 */       return this.underlyingType.isParameterized();
/*      */     } public boolean isRaw() {
/* 1971 */       return this.underlyingType.isRaw();
/*      */     } public boolean isFinal() {
/* 1973 */       return this.underlyingType.isFinal();
/*      */     } public boolean isSuperBound() {
/* 1975 */       return this.underlyingType.isSuperBound();
/*      */     } public boolean isExtendsBound() {
/* 1977 */       return this.underlyingType.isExtendsBound();
/*      */     } public boolean isUnbound() {
/* 1979 */       return this.underlyingType.isUnbound();
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public String toString() {
/* 1987 */       if (this.typeAnnotations != null &&
/* 1988 */         !this.typeAnnotations.isEmpty()) {
/* 1989 */         return "(" + this.typeAnnotations.toString() + " :: " + this.underlyingType.toString() + ")";
/*      */       }
/* 1991 */       return "({} :: " + this.underlyingType.toString() + ")";
/*      */     }
/*      */
/*      */
/*      */     public boolean contains(Type param1Type) {
/* 1996 */       return this.underlyingType.contains(param1Type);
/*      */     }
/*      */
/*      */
/*      */
/*      */     public Type withTypeVar(Type param1Type) {
/* 2002 */       this.underlyingType = this.underlyingType.withTypeVar(param1Type);
/* 2003 */       return this;
/*      */     }
/*      */
/*      */
/*      */     public Symbol.TypeSymbol asElement() {
/* 2008 */       return this.underlyingType.asElement();
/*      */     }
/*      */
/*      */     public MethodType asMethodType() {
/* 2012 */       return this.underlyingType.asMethodType();
/*      */     }
/*      */     public void complete() {
/* 2015 */       this.underlyingType.complete();
/*      */     }
/*      */     public TypeMirror getComponentType() {
/* 2018 */       return ((ArrayType)this.underlyingType).getComponentType();
/*      */     }
/*      */
/*      */     public Type makeVarargs() {
/* 2022 */       return ((ArrayType)this.underlyingType).makeVarargs().annotatedType(this.typeAnnotations);
/*      */     }
/*      */
/*      */     public TypeMirror getExtendsBound() {
/* 2026 */       return ((WildcardType)this.underlyingType).getExtendsBound();
/*      */     } public TypeMirror getSuperBound() {
/* 2028 */       return ((WildcardType)this.underlyingType).getSuperBound();
/*      */     }
/*      */   }
/*      */
/*      */   public static class UnknownType extends Type {
/*      */     public UnknownType() {
/* 2034 */       super((Symbol.TypeSymbol)null);
/*      */     }
/*      */
/*      */
/*      */     public TypeTag getTag() {
/* 2039 */       return TypeTag.UNKNOWN;
/*      */     }
/*      */
/*      */
/*      */     public <R, P> R accept(TypeVisitor<R, P> param1TypeVisitor, P param1P) {
/* 2044 */       return param1TypeVisitor.visitUnknown(this, param1P);
/*      */     }
/*      */
/*      */
/*      */     public boolean isPartial() {
/* 2049 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */   public static interface Visitor<R, S> {
/*      */     R visitClassType(ClassType param1ClassType, S param1S);
/*      */
/*      */     R visitWildcardType(WildcardType param1WildcardType, S param1S);
/*      */
/*      */     R visitArrayType(ArrayType param1ArrayType, S param1S);
/*      */
/*      */     R visitMethodType(MethodType param1MethodType, S param1S);
/*      */
/*      */     R visitPackageType(PackageType param1PackageType, S param1S);
/*      */
/*      */     R visitTypeVar(TypeVar param1TypeVar, S param1S);
/*      */
/*      */     R visitCapturedType(CapturedType param1CapturedType, S param1S);
/*      */
/*      */     R visitForAll(ForAll param1ForAll, S param1S);
/*      */
/*      */     R visitUndetVar(UndetVar param1UndetVar, S param1S);
/*      */
/*      */     R visitErrorType(ErrorType param1ErrorType, S param1S);
/*      */
/*      */     R visitAnnotatedType(AnnotatedType param1AnnotatedType, S param1S);
/*      */
/*      */     R visitType(Type param1Type, S param1S);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
