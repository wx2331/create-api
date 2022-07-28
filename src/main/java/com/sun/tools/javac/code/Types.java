/*      */ package com.sun.tools.javac.code;
/*      */
/*      */ import com.sun.tools.javac.api.Messages;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Check;
/*      */ import com.sun.tools.javac.comp.Enter;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.jvm.ClassFile;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JavacMessages;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.tools.JavaFileObject;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class Types
/*      */ {
/*   80 */   protected static final Context.Key<Types> typesKey = new Context.Key();
/*      */
/*      */   final Symtab syms;
/*      */
/*      */   final JavacMessages messages;
/*      */   final Names names;
/*      */   final boolean allowBoxing;
/*      */   final boolean allowCovariantReturns;
/*      */   final boolean allowObjectToPrimitiveCast;
/*      */   final boolean allowDefaultMethods;
/*      */   final ClassReader reader;
/*      */   final Check chk;
/*      */   final Enter enter;
/*      */   JCDiagnostic.Factory diags;
/*   94 */   List<Warner> warnStack = List.nil(); final Name capturedName; private final FunctionDescriptorLookupError functionDescriptorLookupError; public final Warner noWarnings; private final UnaryVisitor<Boolean> isUnbounded; private final SimpleVisitor<Type, Symbol> asSub; private DescriptorCache descCache; private Filter<Symbol> bridgeFilter; private TypeRelation isSubtype; TypeRelation isSameTypeLoose; TypeRelation isSameTypeStrict; TypeRelation isSameAnnotatedType; private TypeRelation containsType; private TypeRelation isCastable; private TypeRelation disjointType; private final Type.Mapping cvarLowerBoundMapping; private UnaryVisitor<Boolean> isReifiable; private Type.Mapping elemTypeFun; private SimpleVisitor<Type, Symbol> asSuper; private SimpleVisitor<Type, Symbol> memberType; private SimpleVisitor<Type, Boolean> erasure; private Type.Mapping erasureFun; private Type.Mapping erasureRecFun; private UnaryVisitor<Type> supertype; private UnaryVisitor<List<Type>> interfaces; private final UnaryVisitor<List<Type>> directSupertypes; Map<Type, Boolean> isDerivedRawCache;
/*      */   private UnaryVisitor<Type> classBound;
/*      */   private ImplementationCache implCache;
/*      */   private MembersClosureCache membersCache;
/*      */   TypeRelation hasSameArgs_strict;
/*      */   TypeRelation hasSameArgs_nonstrict;
/*      */
/*      */   public static Types instance(Context paramContext) {
/*  102 */     Types types = (Types)paramContext.get(typesKey);
/*  103 */     if (types == null)
/*  104 */       types = new Types(paramContext);
/*  105 */     return types;
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type wildUpperBound(Type paramType) {
/*  134 */     if (paramType.hasTag(TypeTag.WILDCARD)) {
/*  135 */       Type.WildcardType wildcardType = (Type.WildcardType)paramType.unannotatedType();
/*  136 */       if (wildcardType.isSuperBound()) {
/*  137 */         return (wildcardType.bound == null) ? this.syms.objectType : wildcardType.bound.bound;
/*      */       }
/*  139 */       return wildUpperBound(wildcardType.type);
/*      */     }
/*  141 */     return paramType.unannotatedType();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type cvarUpperBound(Type paramType) {
/*  149 */     if (paramType.hasTag(TypeTag.TYPEVAR)) {
/*  150 */       Type.TypeVar typeVar = (Type.TypeVar)paramType.unannotatedType();
/*  151 */       return typeVar.isCaptured() ? cvarUpperBound(typeVar.bound) : typeVar;
/*      */     }
/*  153 */     return paramType.unannotatedType();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type wildLowerBound(Type paramType) {
/*  161 */     if (paramType.hasTag(TypeTag.WILDCARD)) {
/*  162 */       Type.WildcardType wildcardType = (Type.WildcardType)paramType.unannotatedType();
/*  163 */       return wildcardType.isExtendsBound() ? this.syms.botType : wildLowerBound(wildcardType.type);
/*      */     }
/*  165 */     return paramType.unannotatedType();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type cvarLowerBound(Type paramType) {
/*  173 */     if (paramType.hasTag(TypeTag.TYPEVAR)) {
/*  174 */       Type.TypeVar typeVar = (Type.TypeVar)paramType.unannotatedType();
/*  175 */       return typeVar.isCaptured() ? cvarLowerBound(typeVar.getLowerBound()) : typeVar;
/*      */     }
/*  177 */     return paramType.unannotatedType();
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
/*      */
/*      */
/*      */   public boolean isUnbounded(Type paramType) {
/*  191 */     return ((Boolean)this.isUnbounded.visit(paramType)).booleanValue();
/*      */   }
/*      */
/*  194 */   protected Types(Context paramContext) { this.isUnbounded = new UnaryVisitor<Boolean>()
/*      */       {
/*      */         public Boolean visitType(Type param1Type, Void param1Void) {
/*  197 */           return Boolean.valueOf(true);
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/*  202 */           List<Type> list1 = param1ClassType.tsym.type.allparams();
/*  203 */           List<Type> list2 = param1ClassType.allparams();
/*  204 */           while (list1.nonEmpty()) {
/*      */
/*      */
/*      */
/*  208 */             Type.WildcardType wildcardType = new Type.WildcardType(Types.this.syms.objectType, BoundKind.UNBOUND, Types.this.syms.boundClass, (Type.TypeVar)((Type)list1.head).unannotatedType());
/*  209 */             if (!Types.this.containsType((Type)list2.head, wildcardType))
/*  210 */               return Boolean.valueOf(false);
/*  211 */             list1 = list1.tail;
/*  212 */             list2 = list2.tail;
/*      */           }
/*  214 */           return Boolean.valueOf(true);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  235 */     this.asSub = new SimpleVisitor<Type, Symbol>()
/*      */       {
/*      */         public Type visitType(Type param1Type, Symbol param1Symbol) {
/*  238 */           return null;
/*      */         }
/*      */
/*      */
/*      */         public Type visitClassType(Type.ClassType param1ClassType, Symbol param1Symbol) {
/*  243 */           if (param1ClassType.tsym == param1Symbol)
/*  244 */             return param1ClassType;
/*  245 */           Type type1 = Types.this.asSuper(param1Symbol.type, param1ClassType.tsym);
/*  246 */           if (type1 == null)
/*  247 */             return null;
/*  248 */           ListBuffer<Type> listBuffer1 = new ListBuffer();
/*  249 */           ListBuffer<Type> listBuffer2 = new ListBuffer();
/*      */           try {
/*  251 */             Types.this.adapt(type1, param1ClassType, listBuffer1, listBuffer2);
/*  252 */           } catch (AdaptFailure adaptFailure) {
/*  253 */             return null;
/*      */           }
/*  255 */           Type type2 = Types.this.subst(param1Symbol.type, listBuffer1.toList(), listBuffer2.toList());
/*  256 */           if (!Types.this.isSubtype(type2, param1ClassType))
/*  257 */             return null;
/*  258 */           ListBuffer listBuffer = new ListBuffer();
/*  259 */           List<Type> list = param1Symbol.type.allparams();
/*  260 */           for (; list.nonEmpty(); list = list.tail) {
/*  261 */             if (type2.contains((Type)list.head) && !param1ClassType.contains((Type)list.head))
/*  262 */               listBuffer.append(list.head);
/*  263 */           }  if (listBuffer.nonEmpty()) {
/*  264 */             if (param1ClassType.isRaw()) {
/*      */
/*  266 */               type2 = Types.this.erasure(type2);
/*      */             } else {
/*      */
/*  269 */               list = listBuffer.toList();
/*  270 */               ListBuffer listBuffer3 = new ListBuffer();
/*  271 */               for (List<Type> list1 = list; list1.nonEmpty(); list1 = list1.tail) {
/*  272 */                 listBuffer3.append(new Type.WildcardType(Types.this.syms.objectType, BoundKind.UNBOUND, Types.this.syms.boundClass, (Type.TypeVar)((Type)list1.head).unannotatedType()));
/*      */               }
/*  274 */               type2 = Types.this.subst(type2, list, listBuffer3.toList());
/*      */             }
/*      */           }
/*  277 */           return type2;
/*      */         }
/*      */
/*      */
/*      */         public Type visitErrorType(Type.ErrorType param1ErrorType, Symbol param1Symbol) {
/*  282 */           return param1ErrorType;
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  553 */     this.descCache = new DescriptorCache();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  680 */     this.bridgeFilter = new Filter<Symbol>() {
/*      */         public boolean accepts(Symbol param1Symbol) {
/*  682 */           return (param1Symbol.kind == 16 && param1Symbol.name != Types.this.names.init && param1Symbol.name != Types.this.names.clinit && (param1Symbol
/*      */
/*      */
/*  685 */             .flags() & 0x1000L) == 0L);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  843 */     this.isSubtype = new TypeRelation()
/*      */       {
/*      */         public Boolean visitType(Type param1Type1, Type param1Type2)
/*      */         {
/*  847 */           switch (param1Type1.getTag()) {
/*      */             case BYTE:
/*  849 */               return Boolean.valueOf((!param1Type2.hasTag(TypeTag.CHAR) && param1Type1.getTag().isSubRangeOf(param1Type2.getTag())));
/*      */             case CHAR:
/*  851 */               return Boolean.valueOf((!param1Type2.hasTag(TypeTag.SHORT) && param1Type1.getTag().isSubRangeOf(param1Type2.getTag())));
/*      */             case SHORT: case INT: case LONG: case FLOAT:
/*      */             case DOUBLE:
/*  854 */               return Boolean.valueOf(param1Type1.getTag().isSubRangeOf(param1Type2.getTag()));
/*      */             case BOOLEAN: case VOID:
/*  856 */               return Boolean.valueOf(param1Type1.hasTag(param1Type2.getTag()));
/*      */             case TYPEVAR:
/*  858 */               return Boolean.valueOf(Types.this.isSubtypeNoCapture(param1Type1.getUpperBound(), param1Type2));
/*      */             case BOT:
/*  860 */               return
/*  861 */                 Boolean.valueOf((param1Type2.hasTag(TypeTag.BOT) || param1Type2.hasTag(TypeTag.CLASS) || param1Type2
/*  862 */                   .hasTag(TypeTag.ARRAY) || param1Type2.hasTag(TypeTag.TYPEVAR)));
/*      */             case WILDCARD:
/*      */             case NONE:
/*  865 */               return Boolean.valueOf(false);
/*      */           }
/*  867 */           throw new AssertionError("isSubtype " + param1Type1.getTag());
/*      */         }
/*      */
/*      */
/*  871 */         private Set<TypePair> cache = new HashSet<>();
/*      */
/*      */         private boolean containsTypeRecursive(Type param1Type1, Type param1Type2) {
/*  874 */           TypePair typePair = new TypePair(param1Type1, param1Type2);
/*  875 */           if (this.cache.add(typePair)) {
/*      */             try {
/*  877 */               return Types.this.containsType(param1Type1.getTypeArguments(), param1Type2
/*  878 */                   .getTypeArguments());
/*      */             } finally {
/*  880 */               this.cache.remove(typePair);
/*      */             }
/*      */           }
/*  883 */           return Types.this.containsType(param1Type1.getTypeArguments(),
/*  884 */               rewriteSupers(param1Type2).getTypeArguments());
/*      */         }
/*      */
/*      */
/*      */         private Type rewriteSupers(Type param1Type) {
/*  889 */           if (!param1Type.isParameterized())
/*  890 */             return param1Type;
/*  891 */           ListBuffer listBuffer1 = new ListBuffer();
/*  892 */           ListBuffer listBuffer2 = new ListBuffer();
/*  893 */           Types.this.adaptSelf(param1Type, listBuffer1, listBuffer2);
/*  894 */           if (listBuffer1.isEmpty())
/*  895 */             return param1Type;
/*  896 */           ListBuffer listBuffer3 = new ListBuffer();
/*  897 */           boolean bool = false;
/*  898 */           for (Type type1 : listBuffer2.toList()) {
/*  899 */             Type type2 = rewriteSupers(type1);
/*  900 */             if (type2.isSuperBound() && !type2.isExtendsBound()) {
/*  901 */               type2 = new Type.WildcardType(Types.this.syms.objectType, BoundKind.UNBOUND, Types.this.syms.boundClass);
/*      */
/*      */
/*  904 */               bool = true;
/*  905 */             } else if (type2 != type1) {
/*  906 */               type2 = new Type.WildcardType(Types.this.wildUpperBound(type2), BoundKind.EXTENDS, Types.this.syms.boundClass);
/*      */
/*      */
/*  909 */               bool = true;
/*      */             }
/*  911 */             listBuffer3.append(type2);
/*      */           }
/*  913 */           if (bool) {
/*  914 */             return Types.this.subst(param1Type.tsym.type, listBuffer1.toList(), listBuffer3.toList());
/*      */           }
/*  916 */           return param1Type;
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitClassType(Type.ClassType param1ClassType, Type param1Type) {
/*  921 */           Type type = Types.this.asSuper(param1ClassType, param1Type.tsym);
/*  922 */           if (type == null) return Boolean.valueOf(false);
/*      */
/*  924 */           if (!type.hasTag(TypeTag.CLASS)) return Boolean.valueOf(Types.this.isSubtypeNoCapture(type, param1Type));
/*  925 */           return Boolean.valueOf((type.tsym == param1Type.tsym && (
/*      */
/*  927 */               !param1Type.isParameterized() || containsTypeRecursive(param1Type, type)) && Types.this
/*  928 */               .isSubtypeNoCapture(type.getEnclosingType(), param1Type
/*  929 */                 .getEnclosingType())));
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitArrayType(Type.ArrayType param1ArrayType, Type param1Type) {
/*  934 */           if (param1Type.hasTag(TypeTag.ARRAY)) {
/*  935 */             if (param1ArrayType.elemtype.isPrimitive()) {
/*  936 */               return Boolean.valueOf(Types.this.isSameType(param1ArrayType.elemtype, Types.this.elemtype(param1Type)));
/*      */             }
/*  938 */             return Boolean.valueOf(Types.this.isSubtypeNoCapture(param1ArrayType.elemtype, Types.this.elemtype(param1Type)));
/*      */           }
/*      */
/*  941 */           if (param1Type.hasTag(TypeTag.CLASS)) {
/*  942 */             Name name = param1Type.tsym.getQualifiedName();
/*  943 */             return Boolean.valueOf((name == Types.this.names.java_lang_Object || name == Types.this.names.java_lang_Cloneable || name == Types.this.names.java_io_Serializable));
/*      */           }
/*      */
/*      */
/*      */
/*  948 */           return Boolean.valueOf(false);
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitUndetVar(Type.UndetVar param1UndetVar, Type param1Type) {
/*  954 */           if (param1UndetVar == param1Type || param1UndetVar.qtype == param1Type || param1Type.hasTag(TypeTag.ERROR) || param1Type.hasTag(TypeTag.UNKNOWN))
/*  955 */             return Boolean.valueOf(true);
/*  956 */           if (param1Type.hasTag(TypeTag.BOT))
/*      */           {
/*      */
/*  959 */             return Boolean.valueOf(false);
/*      */           }
/*      */
/*  962 */           param1UndetVar.addBound(Type.UndetVar.InferenceBound.UPPER, param1Type, Types.this);
/*  963 */           return Boolean.valueOf(true);
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitErrorType(Type.ErrorType param1ErrorType, Type param1Type) {
/*  968 */           return Boolean.valueOf(true);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1225 */     this.isSameTypeLoose = new LooseSameTypeVisitor();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1263 */     this.isSameTypeStrict = new SameTypeVisitor()
/*      */       {
/*      */         boolean sameTypeVars(Type.TypeVar param1TypeVar1, Type.TypeVar param1TypeVar2) {
/* 1266 */           return (param1TypeVar1 == param1TypeVar2);
/*      */         }
/*      */
/*      */         protected boolean containsTypes(List<Type> param1List1, List<Type> param1List2) {
/* 1270 */           return Types.this.isSameTypes(param1List1, param1List2, true);
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitWildcardType(Type.WildcardType param1WildcardType, Type param1Type) {
/* 1275 */           if (!param1Type.hasTag(TypeTag.WILDCARD)) {
/* 1276 */             return Boolean.valueOf(false);
/*      */           }
/* 1278 */           Type.WildcardType wildcardType = (Type.WildcardType)param1Type.unannotatedType();
/* 1279 */           return Boolean.valueOf((param1WildcardType.kind == wildcardType.kind && Types.this
/* 1280 */               .isSameType(param1WildcardType.type, wildcardType.type, true)));
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1289 */     this.isSameAnnotatedType = new LooseSameTypeVisitor()
/*      */       {
/*      */         public Boolean visitAnnotatedType(Type.AnnotatedType param1AnnotatedType, Type param1Type) {
/* 1292 */           if (!param1Type.isAnnotated())
/* 1293 */             return Boolean.valueOf(false);
/* 1294 */           if (!param1AnnotatedType.getAnnotationMirrors().containsAll((Collection)param1Type.getAnnotationMirrors()))
/* 1295 */             return Boolean.valueOf(false);
/* 1296 */           if (!param1Type.getAnnotationMirrors().containsAll((Collection)param1AnnotatedType.getAnnotationMirrors()))
/* 1297 */             return Boolean.valueOf(false);
/* 1298 */           return visit(param1AnnotatedType.unannotatedType(), param1Type);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1373 */     this.containsType = new TypeRelation()
/*      */       {
/*      */         public Boolean visitType(Type param1Type1, Type param1Type2) {
/* 1376 */           if (param1Type2.isPartial()) {
/* 1377 */             return Boolean.valueOf(Types.this.containedBy(param1Type2, param1Type1));
/*      */           }
/* 1379 */           return Boolean.valueOf(Types.this.isSameType(param1Type1, param1Type2));
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         public Boolean visitWildcardType(Type.WildcardType param1WildcardType, Type param1Type) {
/* 1398 */           if (param1Type.isPartial()) {
/* 1399 */             return Boolean.valueOf(Types.this.containedBy(param1Type, param1WildcardType));
/*      */           }
/*      */
/* 1402 */           return Boolean.valueOf((Types.this.isSameWildcard(param1WildcardType, param1Type) || param1WildcardType.type == param1Type || Types.this
/*      */
/* 1404 */               .isCaptureOf(param1Type, param1WildcardType) || ((param1WildcardType
/* 1405 */               .isExtendsBound() || Types.this.isSubtypeNoCapture(Types.this.wildLowerBound(param1WildcardType), Types.this.cvarLowerBound(Types.this.wildLowerBound(param1Type)))) && (param1WildcardType
/*      */
/* 1407 */               .isSuperBound() || Types.this.isSubtypeNoCapture(Types.this.cvarUpperBound(Types.this.wildUpperBound(param1Type)), Types.this.wildUpperBound(param1WildcardType))))));
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitUndetVar(Type.UndetVar param1UndetVar, Type param1Type) {
/* 1413 */           if (!param1Type.hasTag(TypeTag.WILDCARD)) {
/* 1414 */             return Boolean.valueOf(Types.this.isSameType(param1UndetVar, param1Type));
/*      */           }
/* 1416 */           return Boolean.valueOf(false);
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitErrorType(Type.ErrorType param1ErrorType, Type param1Type) {
/* 1422 */           return Boolean.valueOf(true);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1502 */     this.isCastable = new TypeRelation()
/*      */       {
/*      */         public Boolean visitType(Type param1Type1, Type param1Type2) {
/* 1505 */           if (param1Type2.hasTag(TypeTag.ERROR)) {
/* 1506 */             return Boolean.valueOf(true);
/*      */           }
/* 1508 */           switch (param1Type1.getTag()) { case BYTE: case CHAR: case SHORT: case INT: case LONG:
/*      */             case FLOAT:
/*      */             case DOUBLE:
/* 1511 */               return Boolean.valueOf(param1Type2.isNumeric());
/*      */             case BOOLEAN:
/* 1513 */               return Boolean.valueOf(param1Type2.hasTag(TypeTag.BOOLEAN));
/*      */             case VOID:
/* 1515 */               return Boolean.valueOf(false);
/*      */             case BOT:
/* 1517 */               return Boolean.valueOf(Types.this.isSubtype(param1Type1, param1Type2)); }
/*      */
/* 1519 */           throw new AssertionError();
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitWildcardType(Type.WildcardType param1WildcardType, Type param1Type) {
/* 1525 */           return Boolean.valueOf(Types.this.isCastable(Types.this.wildUpperBound(param1WildcardType), param1Type, (Warner)Types.this.warnStack.head));
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitClassType(Type.ClassType param1ClassType, Type param1Type) {
/* 1530 */           if (param1Type.hasTag(TypeTag.ERROR) || param1Type.hasTag(TypeTag.BOT)) {
/* 1531 */             return Boolean.valueOf(true);
/*      */           }
/* 1533 */           if (param1Type.hasTag(TypeTag.TYPEVAR)) {
/* 1534 */             if (Types.this.isCastable(param1ClassType, param1Type.getUpperBound(), Types.this.noWarnings)) {
/* 1535 */               ((Warner)Types.this.warnStack.head).warn(Lint.LintCategory.UNCHECKED);
/* 1536 */               return Boolean.valueOf(true);
/*      */             }
/* 1538 */             return Boolean.valueOf(false);
/*      */           }
/*      */
/*      */
/* 1542 */           if (param1ClassType.isIntersection() || param1Type.isIntersection()) {
/* 1543 */             return Boolean.valueOf(!param1ClassType.isIntersection() ?
/* 1544 */                 visitIntersectionType((Type.IntersectionClassType)param1Type.unannotatedType(), param1ClassType, true) :
/* 1545 */                 visitIntersectionType((Type.IntersectionClassType)param1ClassType.unannotatedType(), param1Type, false));
/*      */           }
/*      */
/* 1548 */           if (param1Type.hasTag(TypeTag.CLASS) || param1Type.hasTag(TypeTag.ARRAY)) {
/*      */             boolean bool;
/* 1550 */             if ((bool = Types.this.isSubtype(Types.this.erasure(param1ClassType), Types.this.erasure(param1Type))) || Types.this
/* 1551 */               .isSubtype(Types.this.erasure(param1Type), Types.this.erasure(param1ClassType))) {
/* 1552 */               if (!bool && param1Type.hasTag(TypeTag.ARRAY)) {
/* 1553 */                 if (!Types.this.isReifiable(param1Type))
/* 1554 */                   ((Warner)Types.this.warnStack.head).warn(Lint.LintCategory.UNCHECKED);
/* 1555 */                 return Boolean.valueOf(true);
/* 1556 */               }  if (param1Type.isRaw())
/* 1557 */                 return Boolean.valueOf(true);
/* 1558 */               if (param1ClassType.isRaw()) {
/* 1559 */                 if (!Types.this.isUnbounded(param1Type))
/* 1560 */                   ((Warner)Types.this.warnStack.head).warn(Lint.LintCategory.UNCHECKED);
/* 1561 */                 return Boolean.valueOf(true);
/*      */               }
/*      */
/* 1564 */               Type type1 = bool ? param1ClassType : param1Type;
/* 1565 */               Type type2 = bool ? param1Type : param1ClassType;
/*      */
/*      */
/*      */
/* 1569 */               Type type3 = Types.this.rewriteQuantifiers(type1, true, false);
/* 1570 */               Type type4 = Types.this.rewriteQuantifiers(type1, false, false);
/* 1571 */               Type type5 = Types.this.rewriteQuantifiers(type2, true, false);
/* 1572 */               Type type6 = Types.this.rewriteQuantifiers(type2, false, false);
/* 1573 */               Type type7 = Types.this.asSub(type6, type4.tsym);
/* 1574 */               Type type8 = (type7 == null) ? null : Types.this.asSub(type5, type3.tsym);
/* 1575 */               if (type8 == null) {
/*      */
/* 1577 */                 type3 = Types.this.rewriteQuantifiers(type1, true, true);
/* 1578 */                 type4 = Types.this.rewriteQuantifiers(type1, false, true);
/* 1579 */                 type5 = Types.this.rewriteQuantifiers(type2, true, true);
/* 1580 */                 type6 = Types.this.rewriteQuantifiers(type2, false, true);
/* 1581 */                 type7 = Types.this.asSub(type6, type4.tsym);
/* 1582 */                 type8 = (type7 == null) ? null : Types.this.asSub(type5, type3.tsym);
/*      */               }
/* 1584 */               if (type8 != null) {
/* 1585 */                 if (type1.tsym != type8.tsym || type1.tsym != type7.tsym) {
/* 1586 */                   Assert.error(type1.tsym + " != " + type8.tsym + " != " + type7.tsym);
/*      */                 }
/* 1588 */                 if (!Types.this.disjointTypes(type3.allparams(), type8.allparams()) &&
/* 1589 */                   !Types.this.disjointTypes(type3.allparams(), type7.allparams()) &&
/* 1590 */                   !Types.this.disjointTypes(type4.allparams(), type8.allparams()) &&
/* 1591 */                   !Types.this.disjointTypes(type4.allparams(), type7.allparams())) {
/* 1592 */                   if (bool ? Types.this.giveWarning(type1, type2) : Types.this
/* 1593 */                     .giveWarning(type2, type1))
/* 1594 */                     ((Warner)Types.this.warnStack.head).warn(Lint.LintCategory.UNCHECKED);
/* 1595 */                   return Boolean.valueOf(true);
/*      */                 }
/*      */               }
/* 1598 */               if (Types.this.isReifiable(param1Type)) {
/* 1599 */                 return Boolean.valueOf(Types.this.isSubtypeUnchecked(type1, type2));
/*      */               }
/* 1601 */               return Boolean.valueOf(Types.this.isSubtypeUnchecked(type1, type2, (Warner)Types.this.warnStack.head));
/*      */             }
/*      */
/*      */
/* 1605 */             if (param1Type.hasTag(TypeTag.CLASS)) {
/* 1606 */               if ((param1Type.tsym.flags() & 0x200L) != 0L)
/* 1607 */                 return Boolean.valueOf(((param1ClassType.tsym.flags() & 0x10L) == 0L) ? Types.this
/* 1608 */                     .sideCast(param1ClassType, param1Type, (Warner)Types.this.warnStack.head) : Types.this
/* 1609 */                     .sideCastFinal(param1ClassType, param1Type, (Warner)Types.this.warnStack.head));
/* 1610 */               if ((param1ClassType.tsym.flags() & 0x200L) != 0L) {
/* 1611 */                 return Boolean.valueOf(((param1Type.tsym.flags() & 0x10L) == 0L) ? Types.this
/* 1612 */                     .sideCast(param1ClassType, param1Type, (Warner)Types.this.warnStack.head) : Types.this
/* 1613 */                     .sideCastFinal(param1ClassType, param1Type, (Warner)Types.this.warnStack.head));
/*      */               }
/*      */
/* 1616 */               return Boolean.valueOf(false);
/*      */             }
/*      */           }
/*      */
/* 1620 */           return Boolean.valueOf(false);
/*      */         }
/*      */
/*      */         boolean visitIntersectionType(Type.IntersectionClassType param1IntersectionClassType, Type param1Type, boolean param1Boolean) {
/* 1624 */           Warner warner = Types.this.noWarnings;
/* 1625 */           for (Type type : param1IntersectionClassType.getComponents()) {
/* 1626 */             warner.clear();
/* 1627 */             if (param1Boolean ? !Types.this.isCastable(param1Type, type, warner) : !Types.this.isCastable(type, param1Type, warner))
/* 1628 */               return false;
/*      */           }
/* 1630 */           if (warner.hasLint(Lint.LintCategory.UNCHECKED))
/* 1631 */             ((Warner)Types.this.warnStack.head).warn(Lint.LintCategory.UNCHECKED);
/* 1632 */           return true;
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitArrayType(Type.ArrayType param1ArrayType, Type param1Type) {
/* 1637 */           switch (param1Type.getTag()) {
/*      */             case BOT:
/*      */             case ERROR:
/* 1640 */               return Boolean.valueOf(true);
/*      */             case TYPEVAR:
/* 1642 */               if (Types.this.isCastable(param1Type, param1ArrayType, Types.this.noWarnings)) {
/* 1643 */                 ((Warner)Types.this.warnStack.head).warn(Lint.LintCategory.UNCHECKED);
/* 1644 */                 return Boolean.valueOf(true);
/*      */               }
/* 1646 */               return Boolean.valueOf(false);
/*      */
/*      */             case CLASS:
/* 1649 */               return Boolean.valueOf(Types.this.isSubtype(param1ArrayType, param1Type));
/*      */             case ARRAY:
/* 1651 */               if (Types.this.elemtype(param1ArrayType).isPrimitive() || Types.this.elemtype(param1Type).isPrimitive()) {
/* 1652 */                 return Boolean.valueOf(Types.this.elemtype(param1ArrayType).hasTag(Types.this.elemtype(param1Type).getTag()));
/*      */               }
/* 1654 */               return visit(Types.this.elemtype(param1ArrayType), Types.this.elemtype(param1Type));
/*      */           }
/*      */
/* 1657 */           return Boolean.valueOf(false);
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitTypeVar(Type.TypeVar param1TypeVar, Type param1Type) {
/* 1663 */           switch (param1Type.getTag()) {
/*      */             case BOT:
/*      */             case ERROR:
/* 1666 */               return Boolean.valueOf(true);
/*      */             case TYPEVAR:
/* 1668 */               if (Types.this.isSubtype(param1TypeVar, param1Type))
/* 1669 */                 return Boolean.valueOf(true);
/* 1670 */               if (Types.this.isCastable(param1TypeVar.bound, param1Type, Types.this.noWarnings)) {
/* 1671 */                 ((Warner)Types.this.warnStack.head).warn(Lint.LintCategory.UNCHECKED);
/* 1672 */                 return Boolean.valueOf(true);
/*      */               }
/* 1674 */               return Boolean.valueOf(false);
/*      */           }
/*      */
/* 1677 */           return Boolean.valueOf(Types.this.isCastable(param1TypeVar.bound, param1Type, (Warner)Types.this.warnStack.head));
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitErrorType(Type.ErrorType param1ErrorType, Type param1Type) {
/* 1683 */           return Boolean.valueOf(true);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1711 */     this.disjointType = new TypeRelation()
/*      */       {
/* 1713 */         private Set<TypePair> cache = new HashSet<>();
/*      */
/*      */
/*      */         public Boolean visitType(Type param1Type1, Type param1Type2) {
/* 1717 */           if (param1Type2.hasTag(TypeTag.WILDCARD)) {
/* 1718 */             return visit(param1Type2, param1Type1);
/*      */           }
/* 1720 */           return Boolean.valueOf((notSoftSubtypeRecursive(param1Type1, param1Type2) || notSoftSubtypeRecursive(param1Type2, param1Type1)));
/*      */         }
/*      */
/*      */         private boolean isCastableRecursive(Type param1Type1, Type param1Type2) {
/* 1724 */           TypePair typePair = new TypePair(param1Type1, param1Type2);
/* 1725 */           if (this.cache.add(typePair)) {
/*      */             try {
/* 1727 */               return Types.this.isCastable(param1Type1, param1Type2);
/*      */             } finally {
/* 1729 */               this.cache.remove(typePair);
/*      */             }
/*      */           }
/* 1732 */           return true;
/*      */         }
/*      */
/*      */
/*      */         private boolean notSoftSubtypeRecursive(Type param1Type1, Type param1Type2) {
/* 1737 */           TypePair typePair = new TypePair(param1Type1, param1Type2);
/* 1738 */           if (this.cache.add(typePair)) {
/*      */             try {
/* 1740 */               return Types.this.notSoftSubtype(param1Type1, param1Type2);
/*      */             } finally {
/* 1742 */               this.cache.remove(typePair);
/*      */             }
/*      */           }
/* 1745 */           return false;
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitWildcardType(Type.WildcardType param1WildcardType, Type param1Type) {
/* 1751 */           if (param1WildcardType.isUnbound()) {
/* 1752 */             return Boolean.valueOf(false);
/*      */           }
/* 1754 */           if (!param1Type.hasTag(TypeTag.WILDCARD)) {
/* 1755 */             if (param1WildcardType.isExtendsBound()) {
/* 1756 */               return Boolean.valueOf(notSoftSubtypeRecursive(param1Type, param1WildcardType.type));
/*      */             }
/* 1758 */             return Boolean.valueOf(notSoftSubtypeRecursive(param1WildcardType.type, param1Type));
/*      */           }
/*      */
/* 1761 */           if (param1Type.isUnbound()) {
/* 1762 */             return Boolean.valueOf(false);
/*      */           }
/* 1764 */           if (param1WildcardType.isExtendsBound()) {
/* 1765 */             if (param1Type.isExtendsBound())
/* 1766 */               return Boolean.valueOf(!isCastableRecursive(param1WildcardType.type, Types.this.wildUpperBound(param1Type)));
/* 1767 */             if (param1Type.isSuperBound())
/* 1768 */               return Boolean.valueOf(notSoftSubtypeRecursive(Types.this.wildLowerBound(param1Type), param1WildcardType.type));
/* 1769 */           } else if (param1WildcardType.isSuperBound() &&
/* 1770 */             param1Type.isExtendsBound()) {
/* 1771 */             return Boolean.valueOf(notSoftSubtypeRecursive(param1WildcardType.type, Types.this.wildUpperBound(param1Type)));
/*      */           }
/* 1773 */           return Boolean.valueOf(false);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1782 */     this.cvarLowerBoundMapping = new Type.Mapping("cvarLowerBound") {
/*      */         public Type apply(Type param1Type) {
/* 1784 */           return Types.this.cvarLowerBound(param1Type);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1827 */     this.isReifiable = new UnaryVisitor<Boolean>()
/*      */       {
/*      */         public Boolean visitType(Type param1Type, Void param1Void) {
/* 1830 */           return Boolean.valueOf(true);
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/* 1835 */           if (param1ClassType.isCompound()) {
/* 1836 */             return Boolean.valueOf(false);
/*      */           }
/* 1838 */           if (!param1ClassType.isParameterized()) {
/* 1839 */             return Boolean.valueOf(true);
/*      */           }
/* 1841 */           for (Type type : param1ClassType.allparams()) {
/* 1842 */             if (!type.isUnbound())
/* 1843 */               return Boolean.valueOf(false);
/*      */           }
/* 1845 */           return Boolean.valueOf(true);
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Boolean visitArrayType(Type.ArrayType param1ArrayType, Void param1Void) {
/* 1851 */           return visit(param1ArrayType.elemtype);
/*      */         }
/*      */
/*      */
/*      */         public Boolean visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 1856 */           return Boolean.valueOf(false);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1897 */     this.elemTypeFun = new Type.Mapping("elemTypeFun") {
/*      */         public Type apply(Type param1Type) {
/* 1899 */           while (param1Type.hasTag(TypeTag.TYPEVAR)) {
/* 1900 */             param1Type = param1Type.getUpperBound();
/*      */           }
/* 1902 */           return Types.this.elemtype(param1Type);
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1955 */     this.asSuper = new SimpleVisitor<Type, Symbol>()
/*      */       {
/*      */         public Type visitType(Type param1Type, Symbol param1Symbol) {
/* 1958 */           return null;
/*      */         }
/*      */
/*      */
/*      */         public Type visitClassType(Type.ClassType param1ClassType, Symbol param1Symbol) {
/* 1963 */           if (param1ClassType.tsym == param1Symbol) {
/* 1964 */             return param1ClassType;
/*      */           }
/* 1966 */           Type type = Types.this.supertype(param1ClassType);
/* 1967 */           if (type.hasTag(TypeTag.CLASS) || type.hasTag(TypeTag.TYPEVAR)) {
/* 1968 */             Type type1 = Types.this.asSuper(type, param1Symbol);
/* 1969 */             if (type1 != null)
/* 1970 */               return type1;
/*      */           }
/* 1972 */           if ((param1Symbol.flags() & 0x200L) != 0L)
/* 1973 */             for (List<Type> list = Types.this.interfaces(param1ClassType); list.nonEmpty(); list = list.tail) {
/* 1974 */               if (!((Type)list.head).hasTag(TypeTag.ERROR)) {
/* 1975 */                 Type type1 = Types.this.asSuper((Type)list.head, param1Symbol);
/* 1976 */                 if (type1 != null) {
/* 1977 */                   return type1;
/*      */                 }
/*      */               }
/*      */             }
/* 1981 */           return null;
/*      */         }
/*      */
/*      */
/*      */         public Type visitArrayType(Type.ArrayType param1ArrayType, Symbol param1Symbol) {
/* 1986 */           return Types.this.isSubtype(param1ArrayType, param1Symbol.type) ? param1Symbol.type : null;
/*      */         }
/*      */
/*      */
/*      */         public Type visitTypeVar(Type.TypeVar param1TypeVar, Symbol param1Symbol) {
/* 1991 */           if (param1TypeVar.tsym == param1Symbol) {
/* 1992 */             return param1TypeVar;
/*      */           }
/* 1994 */           return Types.this.asSuper(param1TypeVar.bound, param1Symbol);
/*      */         }
/*      */
/*      */
/*      */         public Type visitErrorType(Type.ErrorType param1ErrorType, Symbol param1Symbol) {
/* 1999 */           return param1ErrorType;
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2074 */     this.memberType = new SimpleVisitor<Type, Symbol>()
/*      */       {
/*      */         public Type visitType(Type param1Type, Symbol param1Symbol) {
/* 2077 */           return param1Symbol.type;
/*      */         }
/*      */
/*      */
/*      */         public Type visitWildcardType(Type.WildcardType param1WildcardType, Symbol param1Symbol) {
/* 2082 */           return Types.this.memberType(Types.this.wildUpperBound(param1WildcardType), param1Symbol);
/*      */         }
/*      */
/*      */
/*      */         public Type visitClassType(Type.ClassType param1ClassType, Symbol param1Symbol) {
/* 2087 */           Symbol symbol = param1Symbol.owner;
/* 2088 */           long l = param1Symbol.flags();
/* 2089 */           if ((l & 0x8L) == 0L && symbol.type.isParameterized()) {
/* 2090 */             Type type = Types.this.asOuterSuper(param1ClassType, symbol);
/*      */
/*      */
/*      */
/* 2094 */             type = param1ClassType.isCompound() ? Types.this.capture(type) : type;
/* 2095 */             if (type != null) {
/* 2096 */               List<Type> list1 = symbol.type.allparams();
/* 2097 */               List<Type> list2 = type.allparams();
/* 2098 */               if (list1.nonEmpty()) {
/* 2099 */                 if (list2.isEmpty())
/*      */                 {
/* 2101 */                   return Types.this.erasure(param1Symbol.type);
/*      */                 }
/* 2103 */                 return Types.this.subst(param1Symbol.type, list1, list2);
/*      */               }
/*      */             }
/*      */           }
/*      */
/* 2108 */           return param1Symbol.type;
/*      */         }
/*      */
/*      */
/*      */         public Type visitTypeVar(Type.TypeVar param1TypeVar, Symbol param1Symbol) {
/* 2113 */           return Types.this.memberType(param1TypeVar.bound, param1Symbol);
/*      */         }
/*      */
/*      */
/*      */         public Type visitErrorType(Type.ErrorType param1ErrorType, Symbol param1Symbol) {
/* 2118 */           return param1ErrorType;
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2191 */     this.erasure = new SimpleVisitor<Type, Boolean>() {
/*      */         public Type visitType(Type param1Type, Boolean param1Boolean) {
/* 2193 */           if (param1Type.isPrimitive()) {
/* 2194 */             return param1Type;
/*      */           }
/* 2196 */           return param1Type.map(param1Boolean.booleanValue() ? Types.this.erasureRecFun : Types.this.erasureFun);
/*      */         }
/*      */
/*      */
/*      */         public Type visitWildcardType(Type.WildcardType param1WildcardType, Boolean param1Boolean) {
/* 2201 */           return Types.this.erasure(Types.this.wildUpperBound(param1WildcardType), param1Boolean.booleanValue());
/*      */         }
/*      */
/*      */
/*      */         public Type visitClassType(Type.ClassType param1ClassType, Boolean param1Boolean) {
/* 2206 */           Type type = param1ClassType.tsym.erasure(Types.this);
/* 2207 */           if (param1Boolean.booleanValue()) {
/* 2208 */             type = new Type.ErasedClassType(type.getEnclosingType(), type.tsym);
/*      */           }
/* 2210 */           return type;
/*      */         }
/*      */
/*      */
/*      */         public Type visitTypeVar(Type.TypeVar param1TypeVar, Boolean param1Boolean) {
/* 2215 */           return Types.this.erasure(param1TypeVar.bound, param1Boolean.booleanValue());
/*      */         }
/*      */
/*      */
/*      */         public Type visitErrorType(Type.ErrorType param1ErrorType, Boolean param1Boolean) {
/* 2220 */           return param1ErrorType;
/*      */         }
/*      */
/*      */
/*      */         public Type visitAnnotatedType(Type.AnnotatedType param1AnnotatedType, Boolean param1Boolean) {
/* 2225 */           Type type = Types.this.erasure(param1AnnotatedType.unannotatedType(), param1Boolean.booleanValue());
/* 2226 */           if (type.isAnnotated())
/*      */           {
/*      */
/*      */
/*      */
/* 2231 */             type = ((Type.AnnotatedType)type).unannotatedType();
/*      */           }
/* 2233 */           return type.annotatedType(param1AnnotatedType.getAnnotationMirrors());
/*      */         }
/*      */       };
/*      */
/* 2237 */     this.erasureFun = new Type.Mapping("erasure") { public Type apply(Type param1Type) {
/* 2238 */           return Types.this.erasure(param1Type);
/*      */         } }
/*      */       ;
/* 2241 */     this.erasureRecFun = new Type.Mapping("erasureRecursive") { public Type apply(Type param1Type) {
/* 2242 */           return Types.this.erasureRecursive(param1Type);
/*      */         } }
/*      */       ;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2318 */     this.supertype = new UnaryVisitor<Type>()
/*      */       {
/*      */
/*      */         public Type visitType(Type param1Type, Void param1Void)
/*      */         {
/* 2323 */           return Type.noType;
/*      */         }
/*      */
/*      */
/*      */         public Type visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/* 2328 */           if (param1ClassType.supertype_field == null) {
/* 2329 */             Type type = ((Symbol.ClassSymbol)param1ClassType.tsym).getSuperclass();
/*      */
/* 2331 */             if (param1ClassType.isInterface())
/* 2332 */               type = ((Type.ClassType)param1ClassType.tsym.type).supertype_field;
/* 2333 */             if (param1ClassType.supertype_field == null) {
/* 2334 */               List<Type> list1 = Types.this.classBound(param1ClassType).allparams();
/* 2335 */               List<Type> list2 = param1ClassType.tsym.type.allparams();
/* 2336 */               if (param1ClassType.hasErasedSupertypes()) {
/* 2337 */                 param1ClassType.supertype_field = Types.this.erasureRecursive(type);
/* 2338 */               } else if (list2.nonEmpty()) {
/* 2339 */                 param1ClassType.supertype_field = Types.this.subst(type, list2, list1);
/*      */               } else {
/*      */
/* 2342 */                 param1ClassType.supertype_field = type;
/*      */               }
/*      */             }
/*      */           }
/* 2346 */           return param1ClassType.supertype_field;
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         public Type visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 2357 */           if (param1TypeVar.bound.hasTag(TypeTag.TYPEVAR) || (
/* 2358 */             !param1TypeVar.bound.isCompound() && !param1TypeVar.bound.isInterface())) {
/* 2359 */             return param1TypeVar.bound;
/*      */           }
/* 2361 */           return Types.this.supertype(param1TypeVar.bound);
/*      */         }
/*      */
/*      */
/*      */
/*      */         public Type visitArrayType(Type.ArrayType param1ArrayType, Void param1Void) {
/* 2367 */           if (param1ArrayType.elemtype.isPrimitive() || Types.this.isSameType(param1ArrayType.elemtype, Types.this.syms.objectType)) {
/* 2368 */             return Types.this.arraySuperType();
/*      */           }
/* 2370 */           return new Type.ArrayType(Types.this.supertype(param1ArrayType.elemtype), param1ArrayType.tsym);
/*      */         }
/*      */
/*      */
/*      */         public Type visitErrorType(Type.ErrorType param1ErrorType, Void param1Void) {
/* 2375 */           return Type.noType;
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2388 */     this.interfaces = new UnaryVisitor<List<Type>>()
/*      */       {
/*      */         public List<Type> visitType(Type param1Type, Void param1Void) {
/* 2391 */           return List.nil();
/*      */         }
/*      */
/*      */
/*      */         public List<Type> visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/* 2396 */           if (param1ClassType.interfaces_field == null) {
/* 2397 */             List<Type> list = ((Symbol.ClassSymbol)param1ClassType.tsym).getInterfaces();
/* 2398 */             if (param1ClassType.interfaces_field == null) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2414 */               Assert.check((param1ClassType != param1ClassType.tsym.type), param1ClassType);
/* 2415 */               List<Type> list1 = param1ClassType.allparams();
/* 2416 */               List<Type> list2 = param1ClassType.tsym.type.allparams();
/* 2417 */               if (param1ClassType.hasErasedSupertypes()) {
/* 2418 */                 param1ClassType.interfaces_field = Types.this.erasureRecursive(list);
/* 2419 */               } else if (list2.nonEmpty()) {
/* 2420 */                 param1ClassType.interfaces_field = Types.this.subst(list, list2, list1);
/*      */               } else {
/*      */
/* 2423 */                 param1ClassType.interfaces_field = list;
/*      */               }
/*      */             }
/*      */           }
/* 2427 */           return param1ClassType.interfaces_field;
/*      */         }
/*      */
/*      */
/*      */         public List<Type> visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 2432 */           if (param1TypeVar.bound.isCompound()) {
/* 2433 */             return Types.this.interfaces(param1TypeVar.bound);
/*      */           }
/* 2435 */           if (param1TypeVar.bound.isInterface()) {
/* 2436 */             return List.of(param1TypeVar.bound);
/*      */           }
/* 2438 */           return List.nil();
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2446 */     this.directSupertypes = new UnaryVisitor<List<Type>>()
/*      */       {
/*      */         public List<Type> visitType(Type param1Type, Void param1Void) {
/* 2449 */           if (!param1Type.isIntersection()) {
/* 2450 */             Type type = Types.this.supertype(param1Type);
/* 2451 */             return (type == Type.noType || type == param1Type || type == null) ? Types.this
/* 2452 */               .interfaces(param1Type) : Types.this
/* 2453 */               .interfaces(param1Type).prepend(type);
/*      */           }
/* 2455 */           return visitIntersectionType((Type.IntersectionClassType)param1Type);
/*      */         }
/*      */
/*      */
/*      */         private List<Type> visitIntersectionType(Type.IntersectionClassType param1IntersectionClassType) {
/* 2460 */           return param1IntersectionClassType.getExplicitComponents();
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2474 */     this.isDerivedRawCache = new HashMap<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2561 */     this.classBound = new UnaryVisitor<Type>()
/*      */       {
/*      */         public Type visitType(Type param1Type, Void param1Void) {
/* 2564 */           return param1Type;
/*      */         }
/*      */
/*      */
/*      */         public Type visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/* 2569 */           Type type = Types.this.classBound(param1ClassType.getEnclosingType());
/* 2570 */           if (type != param1ClassType.getEnclosingType()) {
/* 2571 */             return new Type.ClassType(type, param1ClassType.getTypeArguments(), param1ClassType.tsym);
/*      */           }
/* 2573 */           return param1ClassType;
/*      */         }
/*      */
/*      */
/*      */         public Type visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 2578 */           return Types.this.classBound(Types.this.supertype(param1TypeVar));
/*      */         }
/*      */
/*      */
/*      */         public Type visitErrorType(Type.ErrorType param1ErrorType, Void param1Void) {
/* 2583 */           return param1ErrorType;
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2701 */     this.implCache = new ImplementationCache();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2794 */     this.membersCache = new MembersClosureCache();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2965 */     this.hasSameArgs_strict = new HasSameArgs(true);
/* 2966 */     this.hasSameArgs_nonstrict = new HasSameArgs(false);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3226 */     this.methodWithParameters = new MapVisitor<List<Type>>() {
/*      */         public Type visitType(Type param1Type, List<Type> param1List) {
/* 3228 */           throw new IllegalArgumentException("Not a method type: " + param1Type);
/*      */         }
/*      */         public Type visitMethodType(Type.MethodType param1MethodType, List<Type> param1List) {
/* 3231 */           return new Type.MethodType(param1List, param1MethodType.restype, param1MethodType.thrown, param1MethodType.tsym);
/*      */         }
/*      */         public Type visitForAll(Type.ForAll param1ForAll, List<Type> param1List) {
/* 3234 */           return new Type.ForAll(param1ForAll.tvars, param1ForAll.qtype.<Type, List<Type>>accept(this, param1List));
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3242 */     this.methodWithThrown = new MapVisitor<List<Type>>() {
/*      */         public Type visitType(Type param1Type, List<Type> param1List) {
/* 3244 */           throw new IllegalArgumentException("Not a method type: " + param1Type);
/*      */         }
/*      */         public Type visitMethodType(Type.MethodType param1MethodType, List<Type> param1List) {
/* 3247 */           return new Type.MethodType(param1MethodType.argtypes, param1MethodType.restype, param1List, param1MethodType.tsym);
/*      */         }
/*      */         public Type visitForAll(Type.ForAll param1ForAll, List<Type> param1List) {
/* 3250 */           return new Type.ForAll(param1ForAll.tvars, param1ForAll.qtype.<Type, List<Type>>accept(this, param1List));
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3258 */     this.methodWithReturn = new MapVisitor<Type>() {
/*      */         public Type visitType(Type param1Type1, Type param1Type2) {
/* 3260 */           throw new IllegalArgumentException("Not a method type: " + param1Type1);
/*      */         }
/*      */         public Type visitMethodType(Type.MethodType param1MethodType, Type param1Type) {
/* 3263 */           return new Type.MethodType(param1MethodType.argtypes, param1Type, param1MethodType.thrown, param1MethodType.tsym);
/*      */         }
/*      */         public Type visitForAll(Type.ForAll param1ForAll, Type param1Type) {
/* 3266 */           return new Type.ForAll(param1ForAll.tvars, param1ForAll.qtype.<Type, Type>accept(this, param1Type));
/*      */         }
/*      */       };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3415 */     this.closureCache = new HashMap<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3531 */     this.mergeCache = new HashSet<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 3752 */     this.arraySuperType = null; paramContext.put(typesKey, this); this.syms = Symtab.instance(paramContext); this.names = Names.instance(paramContext); Source source = Source.instance(paramContext); this.allowBoxing = source.allowBoxing(); this.allowCovariantReturns = source.allowCovariantReturns(); this.allowObjectToPrimitiveCast = source.allowObjectToPrimitiveCast(); this.allowDefaultMethods = source.allowDefaultMethods(); this.reader = ClassReader.instance(paramContext); this.chk = Check.instance(paramContext); this.enter = Enter.instance(paramContext); this.capturedName = this.names.fromString("<captured wildcard>"); this.messages = JavacMessages.instance(paramContext); this.diags = JCDiagnostic.Factory.instance(paramContext); this.functionDescriptorLookupError = new FunctionDescriptorLookupError(); this.noWarnings = new Warner(null); } public Type asSub(Type paramType, Symbol paramSymbol) { return this.asSub.visit(paramType, paramSymbol); } public boolean isConvertible(Type paramType1, Type paramType2, Warner paramWarner) { if (paramType1.hasTag(TypeTag.ERROR)) return true;  boolean bool1 = paramType1.isPrimitive(); boolean bool2 = paramType2.isPrimitive(); if (bool1 == bool2) return isSubtypeUnchecked(paramType1, paramType2, paramWarner);  if (!this.allowBoxing) return false;  return bool1 ? isSubtype((boxedClass(paramType1)).type, paramType2) : isSubtype(unboxedType(paramType1), paramType2); } public boolean isConvertible(Type paramType1, Type paramType2) { return isConvertible(paramType1, paramType2, this.noWarnings); } public static class FunctionDescriptorLookupError extends RuntimeException {
/*      */     private static final long serialVersionUID = 0L; JCDiagnostic diagnostic = null; FunctionDescriptorLookupError setMessage(JCDiagnostic param1JCDiagnostic) { this.diagnostic = param1JCDiagnostic; return this; } public JCDiagnostic getDiagnostic() { return this.diagnostic; } } class DescriptorCache { private WeakHashMap<Symbol.TypeSymbol, Entry> _map = new WeakHashMap<>(); class FunctionDescriptor {
/*      */       Symbol descSym; FunctionDescriptor(Symbol param2Symbol) { this.descSym = param2Symbol; } public Symbol getSymbol() { return this.descSym; } public Type getType(Type param2Type) { param2Type = Types.this.removeWildcards(param2Type); if (!Types.this.chk.checkValidGenericType(param2Type)) throw DescriptorCache.this.failure(Types.this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { param2Type }));  return Types.this.memberType(param2Type, this.descSym); } } class Entry {
/* 3755 */       final FunctionDescriptor cachedDescRes; final int prevMark; public Entry(FunctionDescriptor param2FunctionDescriptor, int param2Int) { this.cachedDescRes = param2FunctionDescriptor; this.prevMark = param2Int; } boolean matches(int param2Int) { return (this.prevMark == param2Int); } } FunctionDescriptor get(Symbol.TypeSymbol param1TypeSymbol) throws FunctionDescriptorLookupError { Entry entry = this._map.get(param1TypeSymbol); Scope.CompoundScope compoundScope = Types.this.membersClosure(param1TypeSymbol.type, false); if (entry == null || !entry.matches(compoundScope.getMark())) { FunctionDescriptor functionDescriptor = findDescriptorInternal(param1TypeSymbol, compoundScope); this._map.put(param1TypeSymbol, new Entry(functionDescriptor, compoundScope.getMark())); return functionDescriptor; }  return entry.cachedDescRes; } public FunctionDescriptor findDescriptorInternal(Symbol.TypeSymbol param1TypeSymbol, Scope.CompoundScope param1CompoundScope) throws FunctionDescriptorLookupError { if (!param1TypeSymbol.isInterface() || (param1TypeSymbol.flags() & 0x2000L) != 0L) throw failure("not.a.functional.intf", new Object[] { param1TypeSymbol });  ListBuffer listBuffer = new ListBuffer(); for (Symbol symbol : param1CompoundScope.getElements(new DescriptorFilter(param1TypeSymbol))) { Type type = Types.this.memberType(param1TypeSymbol.type, symbol); if (listBuffer.isEmpty() || (symbol.name == ((Symbol)listBuffer.first()).name && Types.this.overrideEquivalent(type, Types.this.memberType(param1TypeSymbol.type, (Symbol)listBuffer.first())))) { listBuffer.append(symbol); continue; }  throw failure("not.a.functional.intf.1", new Object[] { param1TypeSymbol, this.this$0.diags.fragment("incompatible.abstracts", new Object[] { Kinds.kindName(param1TypeSymbol), param1TypeSymbol }) }); }  if (listBuffer.isEmpty()) throw failure("not.a.functional.intf.1", new Object[] { param1TypeSymbol, this.this$0.diags.fragment("no.abstracts", new Object[] { Kinds.kindName(param1TypeSymbol), param1TypeSymbol }) });  if (listBuffer.size() == 1) return new FunctionDescriptor((Symbol)listBuffer.first());  FunctionDescriptor functionDescriptor = mergeDescriptors(param1TypeSymbol, listBuffer.toList()); if (functionDescriptor == null) { ListBuffer listBuffer1 = new ListBuffer(); for (Symbol symbol : listBuffer) { String str = symbol.type.getThrownTypes().nonEmpty() ? "descriptor.throws" : "descriptor"; listBuffer1.append(Types.this.diags.fragment(str, new Object[] { symbol.name, symbol.type.getParameterTypes(), symbol.type.getReturnType(), symbol.type.getThrownTypes() })); }  JCDiagnostic.MultilineDiagnostic multilineDiagnostic = new JCDiagnostic.MultilineDiagnostic(Types.this.diags.fragment("incompatible.descs.in.functional.intf", new Object[] { Kinds.kindName(param1TypeSymbol), param1TypeSymbol }), listBuffer1.toList()); throw failure(multilineDiagnostic); }  return functionDescriptor; } private FunctionDescriptor mergeDescriptors(Symbol.TypeSymbol param1TypeSymbol, List<Symbol> param1List) { List list1 = List.nil(); label55: for (Symbol symbol1 : param1List) { Type type1 = Types.this.memberType(param1TypeSymbol.type, symbol1); for (Symbol symbol2 : param1List) { Type type2 = Types.this.memberType(param1TypeSymbol.type, symbol2); if (!Types.this.isSubSignature(type1, type2)) continue label55;  }  list1 = list1.prepend(symbol1); }  if (list1.isEmpty()) return null;  boolean bool1 = false; Symbol symbol = null; while (symbol == null) { label56: for (Symbol symbol1 : list1) { Type type1 = Types.this.memberType(param1TypeSymbol.type, symbol1); for (Symbol symbol2 : param1List) { Type type2 = Types.this.memberType(param1TypeSymbol.type, symbol2); if (bool1) { if (!Types.this.returnTypeSubstitutable(type1, type2)) continue label56;  continue; }  if (!isSubtypeInternal(type1.getReturnType(), type2.getReturnType())) continue label56;  }  symbol = symbol1; }  if (bool1) break;  bool1 = true; }  if (symbol == null) return null;  boolean bool2 = !symbol.type.hasTag(TypeTag.FORALL) ? true : false; List list2 = null; Type type = Types.this.memberType(param1TypeSymbol.type, symbol); for (Symbol symbol1 : param1List) { Type type1 = Types.this.memberType(param1TypeSymbol.type, symbol1); List<Type> list = type1.getThrownTypes(); if (bool2) { list = Types.this.erasure(list); } else { Type.ForAll forAll1 = (Type.ForAll)type; Type.ForAll forAll2 = (Type.ForAll)type1; list = Types.this.subst(list, forAll2.tvars, forAll1.tvars); }  list2 = (list2 == null) ? list : Types.this.chk.intersect(list, list2); }  final List thrown1 = list2; return new FunctionDescriptor(symbol) { public Type getType(Type param2Type) { Type type = Types.this.memberType(param2Type, getSymbol()); return Types.this.createMethodTypeWithThrown(type, thrown1); } }; } boolean isSubtypeInternal(Type param1Type1, Type param1Type2) { return (param1Type1.isPrimitive() && param1Type2.isPrimitive()) ? Types.this.isSameType(param1Type2, param1Type1) : Types.this.isSubtype(param1Type1, param1Type2); } FunctionDescriptorLookupError failure(String param1String, Object... param1VarArgs) { return failure(Types.this.diags.fragment(param1String, param1VarArgs)); } FunctionDescriptorLookupError failure(JCDiagnostic param1JCDiagnostic) { return Types.this.functionDescriptorLookupError.setMessage(param1JCDiagnostic); } } class FunctionDescriptor { Symbol descSym; FunctionDescriptor(Symbol param1Symbol) { this.descSym = param1Symbol; } public Symbol getSymbol() { return this.descSym; } public Type getType(Type param1Type) { param1Type = Types.this.removeWildcards(param1Type); if (!Types.this.chk.checkValidGenericType(param1Type)) throw this.this$1.failure(Types.this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { param1Type }));  return Types.this.memberType(param1Type, this.descSym); } } public Symbol findDescriptorSymbol(Symbol.TypeSymbol paramTypeSymbol) throws FunctionDescriptorLookupError { return this.descCache.get(paramTypeSymbol).getSymbol(); } public Type findDescriptorType(Type paramType) throws FunctionDescriptorLookupError { return this.descCache.get(paramType.tsym).getType(paramType); } public boolean isFunctionalInterface(Symbol.TypeSymbol paramTypeSymbol) { try { findDescriptorSymbol(paramTypeSymbol); return true; } catch (FunctionDescriptorLookupError functionDescriptorLookupError) { return false; }  } public boolean isFunctionalInterface(Type paramType) { try { findDescriptorType(paramType); return true; } catch (FunctionDescriptorLookupError functionDescriptorLookupError) { return false; }  } public Type removeWildcards(Type paramType) { Type type = capture(paramType); if (type != paramType) { Type type1 = paramType.tsym.type; ListBuffer listBuffer = new ListBuffer(); List<Type> list1 = paramType.getTypeArguments(); List<Type> list2 = type.getTypeArguments(); for (Type type2 : type1.getTypeArguments()) { if (((Type)list1.head).hasTag(TypeTag.WILDCARD)) { Type type3; Type.CapturedType capturedType; Type.WildcardType wildcardType = (Type.WildcardType)((Type)list1.head).unannotatedType(); switch (wildcardType.kind) { case ARRAY: case CLASS: capturedType = (Type.CapturedType)((Type)list2.head).unannotatedType(); type3 = capturedType.bound.containsAny(type.getTypeArguments()) ? wildcardType.type : capturedType.bound; break;default: type3 = wildcardType.type; break; }  listBuffer.append(type3); } else { listBuffer.append(list1.head); }  list1 = list1.tail; list2 = list2.tail; }  return subst(type1, type1.getTypeArguments(), listBuffer.toList()); }  return paramType; } public Symbol.ClassSymbol makeFunctionalInterfaceClass(Env<AttrContext> paramEnv, Name paramName, List<Type> paramList, long paramLong) { if (paramList.isEmpty()) return null;  Symbol symbol = findDescriptorSymbol(((Type)paramList.head).tsym); Type type = findDescriptorType((Type)paramList.head); Symbol.ClassSymbol classSymbol = new Symbol.ClassSymbol(paramLong, paramName, paramEnv.enclClass.sym.outermostClass()); classSymbol.completer = null; classSymbol.members_field = new Scope(classSymbol); Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(symbol.flags(), symbol.name, type, classSymbol); classSymbol.members_field.enter(methodSymbol); Type.ClassType classType = new Type.ClassType(Type.noType, List.nil(), classSymbol); classType.supertype_field = this.syms.objectType; classType.interfaces_field = paramList; classSymbol.type = classType; classSymbol.sourcefile = ((Symbol.ClassSymbol)classSymbol.owner).sourcefile; return classSymbol; } public List<Symbol> functionalInterfaceBridges(Symbol.TypeSymbol paramTypeSymbol) { Assert.check(isFunctionalInterface(paramTypeSymbol)); Symbol symbol = findDescriptorSymbol(paramTypeSymbol); Scope.CompoundScope compoundScope = membersClosure(paramTypeSymbol.type, false); ListBuffer listBuffer = new ListBuffer(); label23: for (Symbol symbol1 : compoundScope.getElementsByName(symbol.name, this.bridgeFilter)) { if (symbol1 != symbol && symbol.overrides(symbol1, paramTypeSymbol, this, false)) { for (Symbol symbol2 : listBuffer) { if (!isSameType(symbol2.erasure(this), symbol1.erasure(this))) { if (symbol2.overrides(symbol1, paramTypeSymbol, this, false)) { if (!pendingBridges((Symbol.ClassSymbol)paramTypeSymbol, symbol2.enclClass())) { if (((Symbol.MethodSymbol)symbol1).binaryImplementation((Symbol.ClassSymbol)symbol2.owner, this) != null) continue label23;  continue; }  continue label23; }  continue; }  continue label23; }  listBuffer.add(symbol1); }  }  return listBuffer.toList(); } private boolean pendingBridges(Symbol.ClassSymbol paramClassSymbol, Symbol.TypeSymbol paramTypeSymbol) { if (paramClassSymbol.classfile != null && paramClassSymbol.classfile.getKind() == JavaFileObject.Kind.CLASS && this.enter.getEnv(paramClassSymbol) == null) return false;  if (paramClassSymbol == paramTypeSymbol) return true;  for (Type type : interfaces(paramClassSymbol.type)) { if (pendingBridges((Symbol.ClassSymbol)type.tsym, paramTypeSymbol)) return true;  }  return false; } class DescriptorFilter implements Filter<Symbol> { Symbol.TypeSymbol origin; DescriptorFilter(Symbol.TypeSymbol param1TypeSymbol) { this.origin = param1TypeSymbol; } public boolean accepts(Symbol param1Symbol) { return (param1Symbol.kind == 16 && (param1Symbol.flags() & 0x80000000400L) == 1024L && !Types.this.overridesObjectMethod(this.origin, param1Symbol) && (((Symbol.MethodSymbol)(Types.this.interfaceCandidates(this.origin.type, (Symbol.MethodSymbol)param1Symbol)).head).flags() & 0x80000000000L) == 0L); } } public boolean isSubtypeUnchecked(Type paramType1, Type paramType2) { return isSubtypeUnchecked(paramType1, paramType2, this.noWarnings); } public boolean isSubtypeUnchecked(Type paramType1, Type paramType2, Warner paramWarner) { boolean bool = isSubtypeUncheckedInternal(paramType1, paramType2, paramWarner); if (bool) checkUnsafeVarargsConversion(paramType1, paramType2, paramWarner);  return bool; } private boolean isSubtypeUncheckedInternal(Type paramType1, Type paramType2, Warner paramWarner) { if (paramType1.hasTag(TypeTag.ARRAY) && paramType2.hasTag(TypeTag.ARRAY)) { paramType1 = paramType1.unannotatedType(); paramType2 = paramType2.unannotatedType(); if (((Type.ArrayType)paramType1).elemtype.isPrimitive()) return isSameType(elemtype(paramType1), elemtype(paramType2));  return isSubtypeUnchecked(elemtype(paramType1), elemtype(paramType2), paramWarner); }  if (isSubtype(paramType1, paramType2)) return true;  if (paramType1.hasTag(TypeTag.TYPEVAR)) return isSubtypeUnchecked(paramType1.getUpperBound(), paramType2, paramWarner);  if (!paramType2.isRaw()) { Type type = asSuper(paramType1, paramType2.tsym); if (type != null && type.isRaw()) { if (isReifiable(paramType2)) { paramWarner.silentWarn(Lint.LintCategory.UNCHECKED); } else { paramWarner.warn(Lint.LintCategory.UNCHECKED); }  return true; }  }  return false; } private void checkUnsafeVarargsConversion(Type paramType1, Type paramType2, Warner paramWarner) { Type.ArrayType arrayType2; if (!paramType1.hasTag(TypeTag.ARRAY) || isReifiable(paramType1)) return;  paramType1 = paramType1.unannotatedType(); paramType2 = paramType2.unannotatedType(); Type.ArrayType arrayType1 = (Type.ArrayType)paramType1; boolean bool = false; switch (paramType2.getTag()) { case ARRAY: arrayType2 = (Type.ArrayType)paramType2; bool = (arrayType1.isVarargs() && !arrayType2.isVarargs() && !isReifiable(arrayType1)); break;case CLASS: bool = arrayType1.isVarargs(); break; }  if (bool) paramWarner.warn(Lint.LintCategory.VARARGS);  } public final boolean isSubtype(Type paramType1, Type paramType2) { return isSubtype(paramType1, paramType2, true); } public final boolean isSubtypeNoCapture(Type paramType1, Type paramType2) { return isSubtype(paramType1, paramType2, false); } public boolean isSubtype(Type paramType1, Type paramType2, boolean paramBoolean) { if (paramType1 == paramType2) return true;  paramType1 = paramType1.unannotatedType(); paramType2 = paramType2.unannotatedType(); if (paramType1 == paramType2) return true;  if (paramType2.isPartial()) return isSuperType(paramType2, paramType1);  if (paramType2.isCompound()) { for (Type type : interfaces(paramType2).prepend(supertype(paramType2))) { if (!isSubtype(paramType1, type, paramBoolean)) return false;  }  return true; }  if (!paramType1.hasTag(TypeTag.UNDETVAR) && !paramType1.isCompound()) { Type type = cvarLowerBound(wildLowerBound(paramType2)); if (paramType2 != type) return isSubtype(paramBoolean ? capture(paramType1) : paramType1, type, false);  }  return this.isSubtype.visit(paramBoolean ? capture(paramType1) : paramType1, paramType2).booleanValue(); } public boolean isSubtypeUnchecked(Type paramType, List<Type> paramList, Warner paramWarner) { for (List<Type> list = paramList; list.nonEmpty(); list = list.tail) { if (!isSubtypeUnchecked(paramType, (Type)list.head, paramWarner)) return false;  }  return true; } public boolean isSubtypes(List<Type> paramList1, List<Type> paramList2) { while (paramList1.tail != null && paramList2.tail != null && isSubtype((Type)paramList1.head, (Type)paramList2.head)) { paramList1 = paramList1.tail; paramList2 = paramList2.tail; }  return (paramList1.tail == null && paramList2.tail == null); } public boolean isSubtypesUnchecked(List<Type> paramList1, List<Type> paramList2, Warner paramWarner) { while (paramList1.tail != null && paramList2.tail != null && isSubtypeUnchecked((Type)paramList1.head, (Type)paramList2.head, paramWarner)) { paramList1 = paramList1.tail; paramList2 = paramList2.tail; }  return (paramList1.tail == null && paramList2.tail == null); } public boolean isSuperType(Type paramType1, Type paramType2) { Type.UndetVar undetVar; switch (paramType1.getTag()) { case ERROR: return true;case UNDETVAR: undetVar = (Type.UndetVar)paramType1; if (paramType1 == paramType2 || undetVar.qtype == paramType2 || paramType2.hasTag(TypeTag.ERROR) || paramType2.hasTag(TypeTag.BOT)) return true;  undetVar.addBound(Type.UndetVar.InferenceBound.LOWER, paramType2, this); return true; }  return isSubtype(paramType2, paramType1); } public boolean isSameTypes(List<Type> paramList1, List<Type> paramList2) { return isSameTypes(paramList1, paramList2, false); } public boolean isSameTypes(List<Type> paramList1, List<Type> paramList2, boolean paramBoolean) { while (paramList1.tail != null && paramList2.tail != null && isSameType((Type)paramList1.head, (Type)paramList2.head, paramBoolean)) { paramList1 = paramList1.tail; paramList2 = paramList2.tail; }  return (paramList1.tail == null && paramList2.tail == null); } public boolean isSignaturePolymorphic(Symbol.MethodSymbol paramMethodSymbol) { List<Type> list = paramMethodSymbol.type.getParameterTypes(); return ((paramMethodSymbol.flags_field & 0x100L) != 0L && paramMethodSymbol.owner == this.syms.methodHandleType.tsym && list.length() == 1 && ((Type)list.head).hasTag(TypeTag.ARRAY) && (paramMethodSymbol.type.getReturnType()).tsym == this.syms.objectType.tsym && ((Type.ArrayType)list.head).elemtype.tsym == this.syms.objectType.tsym); } public boolean isSameType(Type paramType1, Type paramType2) { return isSameType(paramType1, paramType2, false); } public boolean isSameType(Type paramType1, Type paramType2, boolean paramBoolean) { return paramBoolean ? this.isSameTypeStrict.visit(paramType1, paramType2).booleanValue() : this.isSameTypeLoose.visit(paramType1, paramType2).booleanValue(); } public boolean isSameAnnotatedType(Type paramType1, Type paramType2) { return this.isSameAnnotatedType.visit(paramType1, paramType2).booleanValue(); } abstract class SameTypeVisitor extends TypeRelation { public Boolean visitType(Type param1Type1, Type param1Type2) { if (param1Type1 == param1Type2) return Boolean.valueOf(true);  if (param1Type2.isPartial()) return visit(param1Type2, param1Type1);  switch (param1Type1.getTag()) { case BYTE: case CHAR: case SHORT: case INT: case LONG: case FLOAT: case DOUBLE: case BOOLEAN: case VOID: case BOT: case NONE: return Boolean.valueOf(param1Type1.hasTag(param1Type2.getTag()));case TYPEVAR: if (param1Type2.hasTag(TypeTag.TYPEVAR)) return Boolean.valueOf(sameTypeVars((Type.TypeVar)param1Type1.unannotatedType(), (Type.TypeVar)param1Type2.unannotatedType()));  return Boolean.valueOf((param1Type2.isSuperBound() && !param1Type2.isExtendsBound() && visit(param1Type1, Types.this.wildUpperBound(param1Type2)).booleanValue())); }  throw new AssertionError("isSameType " + param1Type1.getTag()); } public Boolean visitWildcardType(Type.WildcardType param1WildcardType, Type param1Type) { if (param1Type.isPartial()) return visit(param1Type, param1WildcardType);  return Boolean.valueOf(false); } public Boolean visitClassType(Type.ClassType param1ClassType, Type param1Type) { if (param1ClassType == param1Type) return Boolean.valueOf(true);  if (param1Type.isPartial()) return visit(param1Type, param1ClassType);  if (param1Type.isSuperBound() && !param1Type.isExtendsBound()) return Boolean.valueOf((visit(param1ClassType, Types.this.wildUpperBound(param1Type)).booleanValue() && visit(param1ClassType, Types.this.wildLowerBound(param1Type)).booleanValue()));  if (param1ClassType.isCompound() && param1Type.isCompound()) { if (!visit(Types.this.supertype(param1ClassType), Types.this.supertype(param1Type)).booleanValue()) return Boolean.valueOf(false);  HashSet<UniqueType> hashSet = new HashSet(); for (Type type : Types.this.interfaces(param1ClassType)) hashSet.add(new UniqueType(type.unannotatedType(), Types.this));  for (Type type : Types.this.interfaces(param1Type)) { if (!hashSet.remove(new UniqueType(type.unannotatedType(), Types.this))) return Boolean.valueOf(false);  }  return Boolean.valueOf(hashSet.isEmpty()); }  return Boolean.valueOf((param1ClassType.tsym == param1Type.tsym && visit(param1ClassType.getEnclosingType(), param1Type.getEnclosingType()).booleanValue() && containsTypes(param1ClassType.getTypeArguments(), param1Type.getTypeArguments()))); } public Boolean visitArrayType(Type.ArrayType param1ArrayType, Type param1Type) { if (param1ArrayType == param1Type) return Boolean.valueOf(true);  if (param1Type.isPartial()) return visit(param1Type, param1ArrayType);  return Boolean.valueOf((param1Type.hasTag(TypeTag.ARRAY) && Types.this.containsTypeEquivalent(param1ArrayType.elemtype, Types.this.elemtype(param1Type)))); } public Boolean visitMethodType(Type.MethodType param1MethodType, Type param1Type) { return Boolean.valueOf((Types.this.hasSameArgs(param1MethodType, param1Type) && visit(param1MethodType.getReturnType(), param1Type.getReturnType()).booleanValue())); } public Boolean visitPackageType(Type.PackageType param1PackageType, Type param1Type) { return Boolean.valueOf((param1PackageType == param1Type)); } public Boolean visitForAll(Type.ForAll param1ForAll, Type param1Type) { if (!param1Type.hasTag(TypeTag.FORALL)) return Boolean.valueOf(false);  Type.ForAll forAll = (Type.ForAll)param1Type; return Boolean.valueOf((Types.this.hasSameBounds(param1ForAll, forAll) && visit(param1ForAll.qtype, Types.this.subst(forAll.qtype, forAll.tvars, param1ForAll.tvars)).booleanValue())); } public Boolean visitUndetVar(Type.UndetVar param1UndetVar, Type param1Type) { if (param1Type.hasTag(TypeTag.WILDCARD)) return Boolean.valueOf(false);  if (param1UndetVar == param1Type || param1UndetVar.qtype == param1Type || param1Type.hasTag(TypeTag.ERROR) || param1Type.hasTag(TypeTag.UNKNOWN)) return Boolean.valueOf(true);  param1UndetVar.addBound(Type.UndetVar.InferenceBound.EQ, param1Type, Types.this); return Boolean.valueOf(true); } public Boolean visitErrorType(Type.ErrorType param1ErrorType, Type param1Type) { return Boolean.valueOf(true); } abstract boolean sameTypeVars(Type.TypeVar param1TypeVar1, Type.TypeVar param1TypeVar2); protected abstract boolean containsTypes(List<Type> param1List1, List<Type> param1List2); } private class LooseSameTypeVisitor extends SameTypeVisitor { private Set<TypePair> cache; private LooseSameTypeVisitor() { this.cache = new HashSet<>(); } boolean sameTypeVars(Type.TypeVar param1TypeVar1, Type.TypeVar param1TypeVar2) { return (param1TypeVar1.tsym == param1TypeVar2.tsym && checkSameBounds(param1TypeVar1, param1TypeVar2)); } protected boolean containsTypes(List<Type> param1List1, List<Type> param1List2) { return Types.this.containsTypeEquivalent(param1List1, param1List2); } private boolean checkSameBounds(Type.TypeVar param1TypeVar1, Type.TypeVar param1TypeVar2) { TypePair typePair = new TypePair(param1TypeVar1, param1TypeVar2, true); if (this.cache.add(typePair)) try { return visit(param1TypeVar1.getUpperBound(), param1TypeVar2.getUpperBound()).booleanValue(); } finally { this.cache.remove(typePair); }   return false; } } public boolean containedBy(Type paramType1, Type paramType2) { switch (paramType1.getTag()) { case UNDETVAR: if (paramType2.hasTag(TypeTag.WILDCARD)) { Type type; Type.UndetVar undetVar = (Type.UndetVar)paramType1; Type.WildcardType wildcardType = (Type.WildcardType)paramType2.unannotatedType(); switch (wildcardType.kind) { case ARRAY: type = wildUpperBound(paramType2); undetVar.addBound(Type.UndetVar.InferenceBound.UPPER, type, this); break;case BYTE: type = wildLowerBound(paramType2); undetVar.addBound(Type.UndetVar.InferenceBound.LOWER, type, this); break; }  return true; }  return isSameType(paramType1, paramType2);case ERROR: return true; }  return containsType(paramType2, paramType1); } boolean containsType(List<Type> paramList1, List<Type> paramList2) { while (paramList1.nonEmpty() && paramList2.nonEmpty() && containsType((Type)paramList1.head, (Type)paramList2.head)) { paramList1 = paramList1.tail; paramList2 = paramList2.tail; }  return (paramList1.isEmpty() && paramList2.isEmpty()); } public boolean containsType(Type paramType1, Type paramType2) { return this.containsType.visit(paramType1, paramType2).booleanValue(); } public boolean isCaptureOf(Type paramType, Type.WildcardType paramWildcardType) { if (!paramType.hasTag(TypeTag.TYPEVAR) || !((Type.TypeVar)paramType.unannotatedType()).isCaptured()) return false;  return isSameWildcard(paramWildcardType, ((Type.CapturedType)paramType.unannotatedType()).wildcard); } public boolean isSameWildcard(Type.WildcardType paramWildcardType, Type paramType) { if (!paramType.hasTag(TypeTag.WILDCARD)) return false;  Type.WildcardType wildcardType = (Type.WildcardType)paramType.unannotatedType(); return (wildcardType.kind == paramWildcardType.kind && wildcardType.type == paramWildcardType.type); } public boolean containsTypeEquivalent(List<Type> paramList1, List<Type> paramList2) { while (paramList1.nonEmpty() && paramList2.nonEmpty() && containsTypeEquivalent((Type)paramList1.head, (Type)paramList2.head)) { paramList1 = paramList1.tail; paramList2 = paramList2.tail; }  return (paramList1.isEmpty() && paramList2.isEmpty()); } public boolean isEqualityComparable(Type paramType1, Type paramType2, Warner paramWarner) { if (paramType2.isNumeric() && paramType1.isNumeric()) return true;  boolean bool1 = paramType2.isPrimitive(); boolean bool2 = paramType1.isPrimitive(); if (!bool1 && !bool2) return (isCastable(paramType1, paramType2, paramWarner) || isCastable(paramType2, paramType1, paramWarner));  return false; } public boolean isCastable(Type paramType1, Type paramType2) { return isCastable(paramType1, paramType2, this.noWarnings); } public boolean isCastable(Type paramType1, Type paramType2, Warner paramWarner) { if (paramType1 == paramType2) return true;  if (paramType1.isPrimitive() != paramType2.isPrimitive()) return (this.allowBoxing && (isConvertible(paramType1, paramType2, paramWarner) || (this.allowObjectToPrimitiveCast && paramType2.isPrimitive() && isSubtype((boxedClass(paramType2)).type, paramType1))));  if (paramWarner != this.warnStack.head) try { this.warnStack = this.warnStack.prepend(paramWarner); checkUnsafeVarargsConversion(paramType1, paramType2, paramWarner); return this.isCastable.visit(paramType1, paramType2).booleanValue(); } finally { this.warnStack = this.warnStack.tail; }   return this.isCastable.visit(paramType1, paramType2).booleanValue(); } public boolean disjointTypes(List<Type> paramList1, List<Type> paramList2) { while (paramList1.tail != null && paramList2.tail != null) { if (disjointType((Type)paramList1.head, (Type)paramList2.head)) return true;  paramList1 = paramList1.tail; paramList2 = paramList2.tail; }  return false; } public boolean disjointType(Type paramType1, Type paramType2) { return this.disjointType.visit(paramType1, paramType2).booleanValue(); } public List<Type> cvarLowerBounds(List<Type> paramList) { return Type.map(paramList, this.cvarLowerBoundMapping); } public boolean notSoftSubtype(Type paramType1, Type paramType2) { if (paramType1 == paramType2) return false;  if (paramType1.hasTag(TypeTag.TYPEVAR)) { Type.TypeVar typeVar = (Type.TypeVar)paramType1; return !isCastable(typeVar.bound, relaxBound(paramType2), this.noWarnings); }  if (!paramType2.hasTag(TypeTag.WILDCARD)) paramType2 = cvarUpperBound(paramType2);  return !isSubtype(paramType1, relaxBound(paramType2)); } private Type relaxBound(Type paramType) { if (paramType.hasTag(TypeTag.TYPEVAR)) { while (paramType.hasTag(TypeTag.TYPEVAR)) paramType = paramType.getUpperBound();  paramType = rewriteQuantifiers(paramType, true, true); }  return paramType; } public boolean isReifiable(Type paramType) { return ((Boolean)this.isReifiable.visit(paramType)).booleanValue(); } public boolean isArray(Type paramType) { while (paramType.hasTag(TypeTag.WILDCARD)) paramType = wildUpperBound(paramType);  return paramType.hasTag(TypeTag.ARRAY); } public Type elemtype(Type paramType) { switch (paramType.getTag()) { case WILDCARD: return elemtype(wildUpperBound(paramType));case ARRAY: paramType = paramType.unannotatedType(); return ((Type.ArrayType)paramType).elemtype;case FORALL: return elemtype(((Type.ForAll)paramType).qtype);case ERROR: return paramType; }  return null; } public Type elemtypeOrType(Type paramType) { Type type = elemtype(paramType); return (type != null) ? type : paramType; } public int dimensions(Type paramType) { byte b = 0; while (paramType.hasTag(TypeTag.ARRAY)) { b++; paramType = elemtype(paramType); }  return b; } public Type.ArrayType makeArrayType(Type paramType) { if (paramType.hasTag(TypeTag.VOID) || paramType.hasTag(TypeTag.PACKAGE)) Assert.error("Type t must not be a VOID or PACKAGE type, " + paramType.toString());  return new Type.ArrayType(paramType, this.syms.arrayClass); } public Type asSuper(Type paramType, Symbol paramSymbol) { if (paramSymbol.type == this.syms.objectType) return this.syms.objectType;  return this.asSuper.visit(paramType, paramSymbol); } public Type asOuterSuper(Type paramType, Symbol paramSymbol) { switch (paramType.getTag()) { case CLASS: while (true) { Type type = asSuper(paramType, paramSymbol); if (type != null) return type;  paramType = paramType.getEnclosingType(); if (!paramType.hasTag(TypeTag.CLASS)) return null;  } case ARRAY: return isSubtype(paramType, paramSymbol.type) ? paramSymbol.type : null;case TYPEVAR: return asSuper(paramType, paramSymbol);case ERROR: return paramType; }  return null; } public Type asEnclosingSuper(Type paramType, Symbol paramSymbol) { switch (paramType.getTag()) { case CLASS: while (true) { Type type1 = asSuper(paramType, paramSymbol); if (type1 != null) return type1;  Type type2 = paramType.getEnclosingType(); paramType = type2.hasTag(TypeTag.CLASS) ? type2 : ((paramType.tsym.owner.enclClass() != null) ? (paramType.tsym.owner.enclClass()).type : Type.noType); if (!paramType.hasTag(TypeTag.CLASS)) return null;  } case ARRAY: return isSubtype(paramType, paramSymbol.type) ? paramSymbol.type : null;case TYPEVAR: return asSuper(paramType, paramSymbol);case ERROR: return paramType; }  return null; } public Type memberType(Type paramType, Symbol paramSymbol) { return ((paramSymbol.flags() & 0x8L) != 0L) ? paramSymbol.type : this.memberType.visit(paramType, paramSymbol); } public boolean isAssignable(Type paramType1, Type paramType2) { return isAssignable(paramType1, paramType2, this.noWarnings); } public boolean isAssignable(Type paramType1, Type paramType2, Warner paramWarner) { if (paramType1.hasTag(TypeTag.ERROR)) return true;  if (paramType1.getTag().isSubRangeOf(TypeTag.INT) && paramType1.constValue() != null) { int i = ((Number)paramType1.constValue()).intValue(); switch (paramType2.getTag()) { case BYTE: if (-128 <= i && i <= 127) return true;  break;case CHAR: if (0 <= i && i <= 65535) return true;  break;case SHORT: if (-32768 <= i && i <= 32767) return true;  break;case INT: return true;case CLASS: switch (unboxedType(paramType2).getTag()) { case BYTE: case CHAR: case SHORT: return isAssignable(paramType1, unboxedType(paramType2), paramWarner); }  break; }  }  return isConvertible(paramType1, paramType2, paramWarner); } public Type erasure(Type paramType) { return eraseNotNeeded(paramType) ? paramType : erasure(paramType, false); } private boolean eraseNotNeeded(Type paramType) { return (paramType.isPrimitive() || this.syms.stringType.tsym == paramType.tsym); } private Type erasure(Type paramType, boolean paramBoolean) { if (paramType.isPrimitive()) return paramType;  return this.erasure.visit(paramType, Boolean.valueOf(paramBoolean)); } public List<Type> erasure(List<Type> paramList) { return Type.map(paramList, this.erasureFun); } public Type erasureRecursive(Type paramType) { return erasure(paramType, true); } public List<Type> erasureRecursive(List<Type> paramList) { return Type.map(paramList, this.erasureRecFun); } public Type.IntersectionClassType makeIntersectionType(List<Type> paramList) { return makeIntersectionType(paramList, ((Type)paramList.head).tsym.isInterface()); } private Type arraySuperType() { if (this.arraySuperType == null) {
/* 3756 */       synchronized (this) {
/* 3757 */         if (this.arraySuperType == null)
/*      */         {
/* 3759 */           this.arraySuperType = makeIntersectionType(List.of(this.syms.serializableType, this.syms.cloneableType), true);
/*      */         }
/*      */       }
/*      */     }
/*      */
/* 3764 */     return this.arraySuperType; } public Type.IntersectionClassType makeIntersectionType(List<Type> paramList, boolean paramBoolean) { Assert.check(paramList.nonEmpty()); Type type = (Type)paramList.head; if (paramBoolean) paramList = paramList.prepend(this.syms.objectType);  Symbol.ClassSymbol classSymbol = new Symbol.ClassSymbol(1090524161L, Type.moreInfo ? this.names.fromString(paramList.toString()) : this.names.empty, null, this.syms.noSymbol); Type.IntersectionClassType intersectionClassType = new Type.IntersectionClassType(paramList, classSymbol, paramBoolean); classSymbol.type = intersectionClassType; classSymbol.erasure_field = ((Type)paramList.head).hasTag(TypeTag.TYPEVAR) ? this.syms.objectType : erasure(type); classSymbol.members_field = new Scope(classSymbol); return intersectionClassType; } public Type makeIntersectionType(Type paramType1, Type paramType2) { return makeIntersectionType(List.of(paramType1, paramType2)); } public Type supertype(Type paramType) { return this.supertype.visit(paramType); } public List<Type> interfaces(Type paramType) { return this.interfaces.visit(paramType); } public List<Type> directSupertypes(Type paramType) { return this.directSupertypes.visit(paramType); } public boolean isDirectSuperInterface(Symbol.TypeSymbol paramTypeSymbol1, Symbol.TypeSymbol paramTypeSymbol2) { for (Type type : interfaces(paramTypeSymbol2.type)) { if (paramTypeSymbol1 == type.tsym) return true;  }  return false; } public boolean isDerivedRaw(Type paramType) { Boolean bool = this.isDerivedRawCache.get(paramType); if (bool == null) { bool = Boolean.valueOf(isDerivedRawInternal(paramType)); this.isDerivedRawCache.put(paramType, bool); }  return bool.booleanValue(); } public boolean isDerivedRawInternal(Type paramType) { if (paramType.isErroneous()) return false;  return (paramType.isRaw() || (supertype(paramType) != Type.noType && isDerivedRaw(supertype(paramType))) || isDerivedRaw(interfaces(paramType))); } public boolean isDerivedRaw(List<Type> paramList) { List<Type> list = paramList; for (; list.nonEmpty() && !isDerivedRaw((Type)list.head); list = list.tail); return list.nonEmpty(); } public void setBounds(Type.TypeVar paramTypeVar, List<Type> paramList) { setBounds(paramTypeVar, paramList, ((Type)paramList.head).tsym.isInterface()); } public void setBounds(Type.TypeVar paramTypeVar, List<Type> paramList, boolean paramBoolean) { paramTypeVar.bound = paramList.tail.isEmpty() ? (Type)paramList.head : makeIntersectionType(paramList, paramBoolean); paramTypeVar.rank_field = -1; } public List<Type> getBounds(Type.TypeVar paramTypeVar) { if (paramTypeVar.bound.hasTag(TypeTag.NONE)) return List.nil();  if (paramTypeVar.bound.isErroneous() || !paramTypeVar.bound.isCompound()) return List.of(paramTypeVar.bound);  if (((erasure(paramTypeVar)).tsym.flags() & 0x200L) == 0L) return interfaces(paramTypeVar).prepend(supertype(paramTypeVar));  return interfaces(paramTypeVar); } public Type classBound(Type paramType) { return this.classBound.visit(paramType); } public boolean isSubSignature(Type paramType1, Type paramType2) { return isSubSignature(paramType1, paramType2, true); } public boolean isSubSignature(Type paramType1, Type paramType2, boolean paramBoolean) { return (hasSameArgs(paramType1, paramType2, paramBoolean) || hasSameArgs(paramType1, erasure(paramType2), paramBoolean)); } public boolean overrideEquivalent(Type paramType1, Type paramType2) { return (hasSameArgs(paramType1, paramType2) || hasSameArgs(paramType1, erasure(paramType2)) || hasSameArgs(erasure(paramType1), paramType2)); } public boolean overridesObjectMethod(Symbol.TypeSymbol paramTypeSymbol, Symbol paramSymbol) { for (Scope.Entry entry = this.syms.objectType.tsym.members().lookup(paramSymbol.name); entry.scope != null; entry = entry.next()) { if (paramSymbol.overrides(entry.sym, paramTypeSymbol, this, true)) return true;  }  return false; } class ImplementationCache {
/*      */     private WeakHashMap<Symbol.MethodSymbol, SoftReference<Map<Symbol.TypeSymbol, Entry>>> _map = new WeakHashMap<>(); class Entry {
/*      */       final Symbol.MethodSymbol cachedImpl; final Filter<Symbol> implFilter; final boolean checkResult; final int prevMark; public Entry(Symbol.MethodSymbol param2MethodSymbol, Filter<Symbol> param2Filter, boolean param2Boolean, int param2Int) { this.cachedImpl = param2MethodSymbol; this.implFilter = param2Filter; this.checkResult = param2Boolean; this.prevMark = param2Int; } boolean matches(Filter<Symbol> param2Filter, boolean param2Boolean, int param2Int) { return (this.implFilter == param2Filter && this.checkResult == param2Boolean && this.prevMark == param2Int); } } Symbol.MethodSymbol get(Symbol.MethodSymbol param1MethodSymbol, Symbol.TypeSymbol param1TypeSymbol, boolean param1Boolean, Filter<Symbol> param1Filter) { SoftReference<Map> softReference = (SoftReference)this._map.get(param1MethodSymbol); Map<Object, Object> map = (softReference != null) ? softReference.get() : null; if (map == null) { map = new HashMap<>(); this._map.put(param1MethodSymbol, new SoftReference(map)); }  Entry entry = (Entry)map.get(param1TypeSymbol); Scope.CompoundScope compoundScope = Types.this.membersClosure(param1TypeSymbol.type, true); if (entry == null || !entry.matches(param1Filter, param1Boolean, compoundScope.getMark())) { Symbol.MethodSymbol methodSymbol = implementationInternal(param1MethodSymbol, param1TypeSymbol, param1Boolean, param1Filter); map.put(param1TypeSymbol, new Entry(methodSymbol, param1Filter, param1Boolean, compoundScope.getMark())); return methodSymbol; }  return entry.cachedImpl; } private Symbol.MethodSymbol implementationInternal(Symbol.MethodSymbol param1MethodSymbol, Symbol.TypeSymbol param1TypeSymbol, boolean param1Boolean, Filter<Symbol> param1Filter) { for (Type type = param1TypeSymbol.type; type.hasTag(TypeTag.CLASS) || type.hasTag(TypeTag.TYPEVAR); type = Types.this.supertype(type)) { while (type.hasTag(TypeTag.TYPEVAR)) type = type.getUpperBound();  Symbol.TypeSymbol typeSymbol = type.tsym; Scope.Entry entry = typeSymbol.members().lookup(param1MethodSymbol.name, param1Filter); for (; entry.scope != null; entry = entry.next(param1Filter)) { if (entry.sym != null && entry.sym.overrides(param1MethodSymbol, param1TypeSymbol, Types.this, param1Boolean)) return (Symbol.MethodSymbol)entry.sym;  }  }  return null; } } public Symbol.MethodSymbol implementation(Symbol.MethodSymbol paramMethodSymbol, Symbol.TypeSymbol paramTypeSymbol, boolean paramBoolean, Filter<Symbol> paramFilter) { return this.implCache.get(paramMethodSymbol, paramTypeSymbol, paramBoolean, paramFilter); } class MembersClosureCache extends SimpleVisitor<Scope.CompoundScope, Void> {
/*      */     private Map<Symbol.TypeSymbol, Scope.CompoundScope> _map = new HashMap<>(); Set<Symbol.TypeSymbol> seenTypes = new HashSet<>(); Scope.CompoundScope nilScope; class MembersScope extends Scope.CompoundScope {
/*      */       CompoundScope scope; public MembersScope(CompoundScope param2CompoundScope) { super(param2CompoundScope.owner); this.scope = param2CompoundScope; } Filter<Symbol> combine(final Filter<Symbol> sf) { return new Filter<Symbol>() { public boolean accepts(Symbol param3Symbol) { return (!param3Symbol.owner.isInterface() && (sf == null || sf.accepts(param3Symbol))); } }; } public Iterable<Symbol> getElements(Filter<Symbol> param2Filter) { return this.scope.getElements(combine(param2Filter)); } public Iterable<Symbol> getElementsByName(Name param2Name, Filter<Symbol> param2Filter) { return this.scope.getElementsByName(param2Name, combine(param2Filter)); } public int getMark() { return this.scope.getMark(); } } public Scope.CompoundScope visitType(Type param1Type, Void param1Void) { if (this.nilScope == null) this.nilScope = new Scope.CompoundScope(Types.this.syms.noSymbol);  return this.nilScope; } public Scope.CompoundScope visitClassType(Type.ClassType param1ClassType, Void param1Void) { if (!this.seenTypes.add(param1ClassType.tsym)) return new Scope.CompoundScope(param1ClassType.tsym);  try { this.seenTypes.add(param1ClassType.tsym); Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)param1ClassType.tsym; Scope.CompoundScope compoundScope = this._map.get(classSymbol); if (compoundScope == null) { compoundScope = new Scope.CompoundScope(classSymbol); for (Type type : Types.this.interfaces(param1ClassType)) compoundScope.addSubScope(visit(type, null));  compoundScope.addSubScope(visit(Types.this.supertype(param1ClassType), null)); compoundScope.addSubScope(classSymbol.members()); this._map.put(classSymbol, compoundScope); }  return compoundScope; } finally { this.seenTypes.remove(param1ClassType.tsym); }  } public Scope.CompoundScope visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) { return visit(param1TypeVar.getUpperBound(), null); } } class MembersScope extends Scope.CompoundScope {
/*      */     CompoundScope scope; public MembersScope(CompoundScope param1CompoundScope) { super(param1CompoundScope.owner); this.scope = param1CompoundScope; } Filter<Symbol> combine(final Filter<Symbol> sf) { return new Filter<Symbol>() { public boolean accepts(Symbol param3Symbol) { return (!param3Symbol.owner.isInterface() && (sf == null || sf.accepts(param3Symbol))); } }; } public Iterable<Symbol> getElements(Filter<Symbol> param1Filter) { return this.scope.getElements(combine(param1Filter)); } public Iterable<Symbol> getElementsByName(Name param1Name, Filter<Symbol> param1Filter) { return this.scope.getElementsByName(param1Name, combine(param1Filter)); } public int getMark() { return this.scope.getMark(); } } public Scope.CompoundScope membersClosure(Type paramType, boolean paramBoolean) { Scope.CompoundScope compoundScope = this.membersCache.visit(paramType, null); if (compoundScope == null) Assert.error("type " + paramType);  this.membersCache.getClass(); return paramBoolean ? new MembersClosureCache.MembersScope(compoundScope) : compoundScope; } public Symbol.MethodSymbol firstUnimplementedAbstract(Symbol.ClassSymbol paramClassSymbol) { try { return firstUnimplementedAbstractImpl(paramClassSymbol, paramClassSymbol); } catch (CompletionFailure completionFailure) { this.chk.completionError((this.enter.getEnv(paramClassSymbol)).tree.pos(), completionFailure); return null; }  } private Symbol.MethodSymbol firstUnimplementedAbstractImpl(Symbol.ClassSymbol paramClassSymbol1, Symbol.ClassSymbol paramClassSymbol2) { Symbol.MethodSymbol methodSymbol = null; if (paramClassSymbol2 == paramClassSymbol1 || (paramClassSymbol2.flags() & 0x600L) != 0L) { Scope scope = paramClassSymbol2.members(); Scope.Entry entry = scope.elems; for (; methodSymbol == null && entry != null; entry = entry.sibling) { if (entry.sym.kind == 16 && (entry.sym.flags() & 0x80000200400L) == 1024L) { Symbol.MethodSymbol methodSymbol1 = (Symbol.MethodSymbol)entry.sym; Symbol.MethodSymbol methodSymbol2 = methodSymbol1.implementation(paramClassSymbol1, this, true); if (methodSymbol2 == null || methodSymbol2 == methodSymbol1) if (this.allowDefaultMethods) { Symbol.MethodSymbol methodSymbol3 = (Symbol.MethodSymbol)(interfaceCandidates(paramClassSymbol1.type, methodSymbol1)).head; if (methodSymbol3 != null && methodSymbol3.overrides(methodSymbol1, paramClassSymbol1, this, true)) methodSymbol2 = methodSymbol3;  }   if (methodSymbol2 == null || methodSymbol2 == methodSymbol1) methodSymbol = methodSymbol1;  }  }  if (methodSymbol == null) { Type type = supertype(paramClassSymbol2.type); if (type.hasTag(TypeTag.CLASS)) methodSymbol = firstUnimplementedAbstractImpl(paramClassSymbol1, (Symbol.ClassSymbol)type.tsym);  }  List<Type> list = interfaces(paramClassSymbol2.type); for (; methodSymbol == null && list.nonEmpty(); list = list.tail) methodSymbol = firstUnimplementedAbstractImpl(paramClassSymbol1, (Symbol.ClassSymbol)((Type)list.head).tsym);  }  return methodSymbol; } public List<Symbol.MethodSymbol> interfaceCandidates(Type paramType, Symbol.MethodSymbol paramMethodSymbol) { MethodFilter methodFilter = new MethodFilter(paramMethodSymbol, paramType); List<Symbol.MethodSymbol> list = List.nil(); for (Symbol symbol : membersClosure(paramType, false).getElements(methodFilter)) { if (!paramType.tsym.isInterface() && !symbol.owner.isInterface()) return List.of(symbol);  if (!list.contains(symbol)) list = list.prepend(symbol);  }  return prune(list); } public List<Symbol.MethodSymbol> prune(List<Symbol.MethodSymbol> paramList) { ListBuffer listBuffer = new ListBuffer(); for (Symbol.MethodSymbol methodSymbol : paramList) { boolean bool = true; for (Symbol.MethodSymbol methodSymbol1 : paramList) { if (methodSymbol != methodSymbol1 && methodSymbol1.owner != methodSymbol.owner && asSuper(methodSymbol1.owner.type, methodSymbol.owner) != null) { bool = false; break; }  }  if (bool) listBuffer.append(methodSymbol);  }  return listBuffer.toList(); } private class MethodFilter implements Filter<Symbol> {
/* 3770 */     Symbol msym; Type site; MethodFilter(Symbol param1Symbol, Type param1Type) { this.msym = param1Symbol; this.site = param1Type; } public boolean accepts(Symbol param1Symbol) { return (param1Symbol.kind == 16 && param1Symbol.name == this.msym.name && (param1Symbol.flags() & 0x1000L) == 0L && param1Symbol.isInheritedIn(this.site.tsym, Types.this) && Types.this.overrideEquivalent(Types.this.memberType(this.site, param1Symbol), Types.this.memberType(this.site, this.msym))); } } public boolean hasSameArgs(Type paramType1, Type paramType2) { return hasSameArgs(paramType1, paramType2, true); } public boolean hasSameArgs(Type paramType1, Type paramType2, boolean paramBoolean) { return hasSameArgs(paramType1, paramType2, paramBoolean ? this.hasSameArgs_strict : this.hasSameArgs_nonstrict); } private boolean hasSameArgs(Type paramType1, Type paramType2, TypeRelation paramTypeRelation) { return paramTypeRelation.visit(paramType1, paramType2).booleanValue(); } private class HasSameArgs extends TypeRelation { boolean strict; public HasSameArgs(boolean param1Boolean) { this.strict = param1Boolean; } public Boolean visitType(Type param1Type1, Type param1Type2) { throw new AssertionError(); } public Boolean visitMethodType(Type.MethodType param1MethodType, Type param1Type) { return Boolean.valueOf((param1Type.hasTag(TypeTag.METHOD) && Types.this.containsTypeEquivalent(param1MethodType.argtypes, param1Type.getParameterTypes()))); } public Boolean visitForAll(Type.ForAll param1ForAll, Type param1Type) { if (!param1Type.hasTag(TypeTag.FORALL)) return Boolean.valueOf(this.strict ? false : visitMethodType(param1ForAll.asMethodType(), param1Type).booleanValue());  Type.ForAll forAll = (Type.ForAll)param1Type; return Boolean.valueOf((Types.this.hasSameBounds(param1ForAll, forAll) && visit(param1ForAll.qtype, Types.this.subst(forAll.qtype, forAll.tvars, param1ForAll.tvars)).booleanValue())); } public Boolean visitErrorType(Type.ErrorType param1ErrorType, Type param1Type) { return Boolean.valueOf(false); } } public List<Type> subst(List<Type> paramList1, List<Type> paramList2, List<Type> paramList3) { return (new Subst(paramList2, paramList3)).subst(paramList1); } public Type subst(Type paramType, List<Type> paramList1, List<Type> paramList2) { return (new Subst(paramList1, paramList2)).subst(paramType); } private class Subst extends UnaryVisitor<Type> { List<Type> from; List<Type> to; public Subst(List<Type> param1List1, List<Type> param1List2) { int i = param1List1.length(); int j = param1List2.length(); while (i > j) { i--; param1List1 = param1List1.tail; }  while (i < j) { j--; param1List2 = param1List2.tail; }  this.from = param1List1; this.to = param1List2; } Type subst(Type param1Type) { if (this.from.tail == null) return param1Type;  return visit(param1Type); } List<Type> subst(List<Type> param1List) { if (this.from.tail == null) return param1List;  boolean bool = false; if (param1List.nonEmpty() && this.from.nonEmpty()) { Type type = subst((Type)param1List.head); List<Type> list = subst(param1List.tail); if (type != param1List.head || list != param1List.tail) return list.prepend(type);  }  return param1List; } public Type visitType(Type param1Type, Void param1Void) { return param1Type; } public Type visitMethodType(Type.MethodType param1MethodType, Void param1Void) { List<Type> list1 = subst(param1MethodType.argtypes); Type type = subst(param1MethodType.restype); List<Type> list2 = subst(param1MethodType.thrown); if (list1 == param1MethodType.argtypes && type == param1MethodType.restype && list2 == param1MethodType.thrown) return param1MethodType;  return new Type.MethodType(list1, type, list2, param1MethodType.tsym); } public Type visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) { List<Type> list1 = this.from, list2 = this.to; for (; list1.nonEmpty(); list1 = list1.tail, list2 = list2.tail) { if (param1TypeVar == list1.head) return ((Type)list2.head).withTypeVar(param1TypeVar);  }  return param1TypeVar; } public Type visitUndetVar(Type.UndetVar param1UndetVar, Void param1Void) { return param1UndetVar; } public Type visitClassType(Type.ClassType param1ClassType, Void param1Void) { if (!param1ClassType.isCompound()) { List<Type> list1 = param1ClassType.getTypeArguments(); List<Type> list2 = subst(list1); Type type1 = param1ClassType.getEnclosingType(); Type type2 = subst(type1); if (list2 == list1 && type2 == type1) return param1ClassType;  return new Type.ClassType(type2, list2, param1ClassType.tsym); }  Type type = subst(Types.this.supertype(param1ClassType)); List<Type> list = subst(Types.this.interfaces(param1ClassType)); if (type == Types.this.supertype(param1ClassType) && list == Types.this.interfaces(param1ClassType)) return param1ClassType;  return Types.this.makeIntersectionType(list.prepend(type)); } public Type visitWildcardType(Type.WildcardType param1WildcardType, Void param1Void) { Type type = param1WildcardType.type; if (param1WildcardType.kind != BoundKind.UNBOUND) type = subst(type);  if (type == param1WildcardType.type) return param1WildcardType;  if (param1WildcardType.isExtendsBound() && type.isExtendsBound()) type = Types.this.wildUpperBound(type);  return new Type.WildcardType(type, param1WildcardType.kind, Types.this.syms.boundClass, param1WildcardType.bound); } public Type visitArrayType(Type.ArrayType param1ArrayType, Void param1Void) { Type type = subst(param1ArrayType.elemtype); if (type == param1ArrayType.elemtype) return param1ArrayType;  return new Type.ArrayType(type, param1ArrayType.tsym); } public Type visitForAll(Type.ForAll param1ForAll, Void param1Void) { if (Type.containsAny(this.to, param1ForAll.tvars)) { List<Type> list1 = Types.this.newInstances(param1ForAll.tvars); param1ForAll = new Type.ForAll(list1, Types.this.subst(param1ForAll.qtype, param1ForAll.tvars, list1)); }  List<Type> list = Types.this.substBounds(param1ForAll.tvars, this.from, this.to); Type type = subst(param1ForAll.qtype); if (list == param1ForAll.tvars && type == param1ForAll.qtype) return param1ForAll;  if (list == param1ForAll.tvars) return new Type.ForAll(list, type);  return new Type.ForAll(list, Types.this.subst(type, param1ForAll.tvars, list)); } public Type visitErrorType(Type.ErrorType param1ErrorType, Void param1Void) { return param1ErrorType; } } public Type glb(List<Type> paramList) { Type type = (Type)paramList.head;
/* 3771 */     for (Type type1 : paramList.tail) {
/* 3772 */       if (type.isErroneous())
/* 3773 */         return type;
/* 3774 */       type = glb(type, type1);
/*      */     }
/* 3776 */     return type; }
/*      */   public List<Type> substBounds(List<Type> paramList1, List<Type> paramList2, List<Type> paramList3) { if (paramList1.isEmpty()) return paramList1;  ListBuffer listBuffer1 = new ListBuffer(); boolean bool = false; for (Type type1 : paramList1) { Type.TypeVar typeVar = (Type.TypeVar)type1; Type type2 = subst(typeVar.bound, paramList2, paramList3); if (type2 != typeVar.bound) bool = true;  listBuffer1.append(type2); }  if (!bool) return paramList1;  ListBuffer listBuffer2 = new ListBuffer(); for (Type type : paramList1) listBuffer2.append(new Type.TypeVar(type.tsym, null, this.syms.botType));  List list = listBuffer1.toList(); paramList2 = paramList1; paramList3 = listBuffer2.toList(); for (; !list.isEmpty(); list = list.tail) list.head = subst((Type)list.head, paramList2, paramList3);  list = listBuffer1.toList(); for (Type type : listBuffer2.toList()) { Type.TypeVar typeVar = (Type.TypeVar)type; typeVar.bound = (Type)list.head; list = list.tail; }  return listBuffer2.toList(); } public Type.TypeVar substBound(Type.TypeVar paramTypeVar, List<Type> paramList1, List<Type> paramList2) { Type type = subst(paramTypeVar.bound, paramList1, paramList2); if (type == paramTypeVar.bound) return paramTypeVar;  Type.TypeVar typeVar = new Type.TypeVar(paramTypeVar.tsym, null, this.syms.botType); typeVar.bound = subst(type, List.of(paramTypeVar), List.of(typeVar)); return typeVar; } public boolean hasSameBounds(Type.ForAll paramForAll1, Type.ForAll paramForAll2) { List<Type> list1 = paramForAll1.tvars; List<Type> list2 = paramForAll2.tvars; while (list1.nonEmpty() && list2.nonEmpty() && isSameType(((Type)list1.head).getUpperBound(), subst(((Type)list2.head).getUpperBound(), paramForAll2.tvars, paramForAll1.tvars))) { list1 = list1.tail; list2 = list2.tail; }  return (list1.isEmpty() && list2.isEmpty()); } public List<Type> newInstances(List<Type> paramList) { List<Type> list1 = Type.map(paramList, newInstanceFun); for (List<Type> list2 = list1; list2.nonEmpty(); list2 = list2.tail) { Type.TypeVar typeVar = (Type.TypeVar)list2.head; typeVar.bound = subst(typeVar.bound, paramList, list1); }  return list1; } private static final Type.Mapping newInstanceFun = new Type.Mapping("newInstanceFun") { public Type apply(Type param1Type) { return new Type.TypeVar(param1Type.tsym, param1Type.getUpperBound(), param1Type.getLowerBound()); } }
/*      */   ; private final MapVisitor<List<Type>> methodWithParameters; private final MapVisitor<List<Type>> methodWithThrown; private final MapVisitor<Type> methodWithReturn; private Map<Type, List<Type>> closureCache; Set<TypePair> mergeCache; private Type arraySuperType; public Type createMethodTypeWithParameters(Type paramType, List<Type> paramList) { return paramType.<Type, List<Type>>accept(this.methodWithParameters, paramList); } public Type createMethodTypeWithThrown(Type paramType, List<Type> paramList) { return paramType.<Type, List<Type>>accept(this.methodWithThrown, paramList); } public Type createMethodTypeWithReturn(Type paramType1, Type paramType2) { return paramType1.<Type, Type>accept(this.methodWithReturn, paramType2); } public Type createErrorType(Type paramType) { return new Type.ErrorType(paramType, this.syms.errSymbol); } public Type createErrorType(Symbol.ClassSymbol paramClassSymbol, Type paramType) { return new Type.ErrorType(paramClassSymbol, paramType); } public Type createErrorType(Name paramName, Symbol.TypeSymbol paramTypeSymbol, Type paramType) { return new Type.ErrorType(paramName, paramTypeSymbol, paramType); } public int rank(Type paramType) { Type.ClassType classType; Type.TypeVar typeVar; paramType = paramType.unannotatedType(); switch (paramType.getTag()) { case CLASS: classType = (Type.ClassType)paramType; if (classType.rank_field < 0) { Name name = classType.tsym.getQualifiedName(); if (name == this.names.java_lang_Object) { classType.rank_field = 0; } else { int i = rank(supertype(classType)); List<Type> list = interfaces(classType); for (; list.nonEmpty(); list = list.tail) { if (rank((Type)list.head) > i) i = rank((Type)list.head);  }  classType.rank_field = i + 1; }  }  return classType.rank_field;case TYPEVAR: typeVar = (Type.TypeVar)paramType; if (typeVar.rank_field < 0) { int i = rank(supertype(typeVar)); List<Type> list = interfaces(typeVar); for (; list.nonEmpty(); list = list.tail) { if (rank((Type)list.head) > i) i = rank((Type)list.head);  }  typeVar.rank_field = i + 1; }  return typeVar.rank_field;case NONE: case ERROR: return 0; }  throw new AssertionError(); } public String toString(Type paramType, Locale paramLocale) { return Printer.createStandardPrinter((Messages)this.messages).visit(paramType, paramLocale); } public String toString(Symbol paramSymbol, Locale paramLocale) { return Printer.createStandardPrinter((Messages)this.messages).visit(paramSymbol, paramLocale); } @Deprecated public String toString(Type paramType) { if (paramType.hasTag(TypeTag.FORALL)) { Type.ForAll forAll = (Type.ForAll)paramType; return typaramsString(forAll.tvars) + forAll.qtype; }  return "" + paramType; } private String typaramsString(List<Type> paramList) { StringBuilder stringBuilder = new StringBuilder(); stringBuilder.append('<'); boolean bool = true; for (Type type : paramList) { if (!bool) stringBuilder.append(", ");  bool = false; appendTyparamString((Type.TypeVar)type.unannotatedType(), stringBuilder); }  stringBuilder.append('>'); return stringBuilder.toString(); } private void appendTyparamString(Type.TypeVar paramTypeVar, StringBuilder paramStringBuilder) { paramStringBuilder.append(paramTypeVar); if (paramTypeVar.bound == null || paramTypeVar.bound.tsym.getQualifiedName() == this.names.java_lang_Object) return;  paramStringBuilder.append(" extends "); Type type = paramTypeVar.bound; if (!type.isCompound()) { paramStringBuilder.append(type); } else if (((erasure(paramTypeVar)).tsym.flags() & 0x200L) == 0L) { paramStringBuilder.append(supertype(paramTypeVar)); for (Type type1 : interfaces(paramTypeVar)) { paramStringBuilder.append('&'); paramStringBuilder.append(type1); }  } else { boolean bool = true; for (Type type1 : interfaces(paramTypeVar)) { if (!bool) paramStringBuilder.append('&');  bool = false; paramStringBuilder.append(type1); }  }  } public List<Type> closure(Type paramType) { List<Type> list = this.closureCache.get(paramType); if (list == null) { Type type = supertype(paramType); if (!paramType.isCompound()) { if (type.hasTag(TypeTag.CLASS)) { list = insert(closure(type), paramType); } else if (type.hasTag(TypeTag.TYPEVAR)) { list = closure(type).prepend(paramType); } else { list = List.of(paramType); }  } else { list = closure(supertype(paramType)); }  for (List<Type> list1 = interfaces(paramType); list1.nonEmpty(); list1 = list1.tail) list = union(list, closure((Type)list1.head));  this.closureCache.put(paramType, list); }  return list; } public List<Type> insert(List<Type> paramList, Type paramType) { if (paramList.isEmpty()) return paramList.prepend(paramType);  if (paramType.tsym == ((Type)paramList.head).tsym) return paramList;  if (paramType.tsym.precedes(((Type)paramList.head).tsym, this)) return paramList.prepend(paramType);  return insert(paramList.tail, paramType).prepend(paramList.head); } public List<Type> union(List<Type> paramList1, List<Type> paramList2) { if (paramList1.isEmpty()) return paramList2;  if (paramList2.isEmpty()) return paramList1;  if (((Type)paramList1.head).tsym == ((Type)paramList2.head).tsym) return union(paramList1.tail, paramList2.tail).prepend(paramList1.head);  if (((Type)paramList1.head).tsym.precedes(((Type)paramList2.head).tsym, this)) return union(paramList1.tail, paramList2).prepend(paramList1.head);  if (((Type)paramList2.head).tsym.precedes(((Type)paramList1.head).tsym, this)) return union(paramList1, paramList2.tail).prepend(paramList2.head);  return union(paramList1.tail, paramList2).prepend(paramList1.head); } public List<Type> intersect(List<Type> paramList1, List<Type> paramList2) { if (paramList1 == paramList2) return paramList1;  if (paramList1.isEmpty() || paramList2.isEmpty()) return List.nil();  if (((Type)paramList1.head).tsym.precedes(((Type)paramList2.head).tsym, this)) return intersect(paramList1.tail, paramList2);  if (((Type)paramList2.head).tsym.precedes(((Type)paramList1.head).tsym, this)) return intersect(paramList1, paramList2.tail);  if (isSameType((Type)paramList1.head, (Type)paramList2.head)) return intersect(paramList1.tail, paramList2.tail).prepend(paramList1.head);  if (((Type)paramList1.head).tsym == ((Type)paramList2.head).tsym && ((Type)paramList1.head).hasTag(TypeTag.CLASS) && ((Type)paramList2.head).hasTag(TypeTag.CLASS)) { if (((Type)paramList1.head).isParameterized() && ((Type)paramList2.head).isParameterized()) { Type type = merge((Type)paramList1.head, (Type)paramList2.head); return intersect(paramList1.tail, paramList2.tail).prepend(type); }  if (((Type)paramList1.head).isRaw() || ((Type)paramList2.head).isRaw()) return intersect(paramList1.tail, paramList2.tail).prepend(erasure((Type)paramList1.head));  }  return intersect(paramList1.tail, paramList2.tail); } class TypePair {
/*      */     final Type t1; final Type t2; boolean strict; TypePair(Type param1Type1, Type param1Type2) { this(param1Type1, param1Type2, false); } TypePair(Type param1Type1, Type param1Type2, boolean param1Boolean) { this.t1 = param1Type1; this.t2 = param1Type2; this.strict = param1Boolean; } public int hashCode() { return 127 * Types.this.hashCode(this.t1) + Types.this.hashCode(this.t2); } public boolean equals(Object param1Object) { if (!(param1Object instanceof TypePair)) return false;  TypePair typePair = (TypePair)param1Object; return (Types.this.isSameType(this.t1, typePair.t1, this.strict) && Types.this.isSameType(this.t2, typePair.t2, this.strict)); }
/* 3780 */   } private Type merge(Type paramType1, Type paramType2) { Type.ClassType classType1 = (Type.ClassType)paramType1; List<Type> list1 = classType1.getTypeArguments(); Type.ClassType classType2 = (Type.ClassType)paramType2; List<Type> list2 = classType2.getTypeArguments(); ListBuffer listBuffer = new ListBuffer(); List<Type> list3 = classType1.tsym.type.getTypeArguments(); while (list1.nonEmpty() && list2.nonEmpty() && list3.nonEmpty()) { if (containsType((Type)list1.head, (Type)list2.head)) { listBuffer.append(list1.head); } else if (containsType((Type)list2.head, (Type)list1.head)) { listBuffer.append(list2.head); } else { Type.WildcardType wildcardType; TypePair typePair = new TypePair(paramType1, paramType2); if (this.mergeCache.add(typePair)) { wildcardType = new Type.WildcardType(lub(new Type[] { wildUpperBound((Type)list1.head), wildUpperBound((Type)list2.head) }, ), BoundKind.EXTENDS, this.syms.boundClass); this.mergeCache.remove(typePair); } else { wildcardType = new Type.WildcardType(this.syms.objectType, BoundKind.UNBOUND, this.syms.boundClass); }  listBuffer.append(wildcardType.withTypeVar((Type)list3.head)); }  list1 = list1.tail; list2 = list2.tail; list3 = list3.tail; }  Assert.check((list1.isEmpty() && list2.isEmpty() && list3.isEmpty())); return new Type.ClassType(classType1.getEnclosingType(), listBuffer.toList(), classType1.tsym); } private Type compoundMin(List<Type> paramList) { if (paramList.isEmpty()) return this.syms.objectType;  List<Type> list = closureMin(paramList); if (list.isEmpty()) return null;  if (list.tail.isEmpty()) return (Type)list.head;  return makeIntersectionType(list); } private List<Type> closureMin(List<Type> paramList) { ListBuffer listBuffer1 = new ListBuffer(); ListBuffer listBuffer2 = new ListBuffer(); HashSet<Type> hashSet = new HashSet(); while (!paramList.isEmpty()) { Type type = (Type)paramList.head; boolean bool = !hashSet.contains(type) ? true : false; if (bool && type.hasTag(TypeTag.TYPEVAR)) for (Type type1 : paramList.tail) { if (isSubtypeNoCapture(type1, type)) { bool = false; break; }  }   if (bool) { if (type.isInterface()) { listBuffer2.append(type); } else { listBuffer1.append(type); }  for (Type type1 : paramList.tail) { if (isSubtypeNoCapture(type, type1)) hashSet.add(type1);  }  }  paramList = paramList.tail; }  return listBuffer1.appendList(listBuffer2).toList(); } public Type lub(List<Type> paramList) { return lub((Type[])paramList.toArray((Object[])new Type[paramList.length()])); } public Type lub(Type... paramVarArgs) { Type[] arrayOfType; byte b2, b3; List<Type> list1; int j; List<Type> list2, list3; int[] arrayOfInt = new int[paramVarArgs.length]; int i = 0; byte b1 = 0; while (true) { if (b1 < paramVarArgs.length) { Type type = paramVarArgs[b1]; switch (type.getTag()) { case CLASS: arrayOfInt[b1] = 2; i |= 0x2; break;case ARRAY: arrayOfInt[b1] = 1; i |= 0x1; break;case TYPEVAR: while (true) { type = type.getUpperBound(); if (!type.hasTag(TypeTag.TYPEVAR)) { if (type.hasTag(TypeTag.ARRAY)) { arrayOfInt[b1] = 1; i |= 0x1; break; }  arrayOfInt[b1] = 2; i |= 0x2; b1++; }  }  break;default: arrayOfInt[b1] = 0; if (type.isPrimitive()) return this.syms.errType;  break; }  } else { break; }  b1++; }  switch (i) { case 0: return this.syms.botType;case 1: arrayOfType = new Type[paramVarArgs.length]; for (b2 = 0; b2 < paramVarArgs.length; b2++) { Type type = arrayOfType[b2] = this.elemTypeFun.apply(paramVarArgs[b2]); if (type.isPrimitive()) { Type type1 = paramVarArgs[0]; for (byte b = 1; b < paramVarArgs.length; b++) { if (!isSameType(type1, paramVarArgs[b])) return arraySuperType();  }  return type1; }  }  return new Type.ArrayType(lub(arrayOfType), this.syms.arrayClass);case 2: b2 = 0; for (b3 = 0; b3 < paramVarArgs.length; b3++) { Type type = paramVarArgs[b3]; if (type.hasTag(TypeTag.CLASS) || type.hasTag(TypeTag.TYPEVAR)) break;  b2++; }  Assert.check((b2 < paramVarArgs.length)); list1 = erasedSupertypes(paramVarArgs[b2]); for (j = b2 + 1; j < paramVarArgs.length; j++) { Type type = paramVarArgs[j]; if (type.hasTag(TypeTag.CLASS) || type.hasTag(TypeTag.TYPEVAR)) list1 = intersect(list1, erasedSupertypes(type));  }  list2 = closureMin(list1); list3 = List.nil(); for (Type type : list2) { List<Type> list = List.of(asSuper(paramVarArgs[b2], type.tsym)); for (int k = b2 + 1; k < paramVarArgs.length; k++) { Type type1 = asSuper(paramVarArgs[k], type.tsym); list = intersect(list, (type1 != null) ? List.of(type1) : List.nil()); }  list3 = list3.appendList(list); }  return compoundMin(list3); }  List<Type> list4 = List.of(arraySuperType()); for (byte b4 = 0; b4 < paramVarArgs.length; b4++) { if (arrayOfInt[b4] != 1) list4 = list4.prepend(paramVarArgs[b4]);  }  return lub(list4); } List<Type> erasedSupertypes(Type paramType) { ListBuffer listBuffer = new ListBuffer(); for (Type type : closure(paramType)) { if (type.hasTag(TypeTag.TYPEVAR)) { listBuffer.append(type); continue; }  listBuffer.append(erasure(type)); }  return listBuffer.toList(); } public Type glb(Type paramType1, Type paramType2) { if (paramType2 == null)
/* 3781 */       return paramType1;
/* 3782 */     if (paramType1.isPrimitive() || paramType2.isPrimitive())
/* 3783 */       return this.syms.errType;
/* 3784 */     if (isSubtypeNoCapture(paramType1, paramType2))
/* 3785 */       return paramType1;
/* 3786 */     if (isSubtypeNoCapture(paramType2, paramType1)) {
/* 3787 */       return paramType2;
/*      */     }
/* 3789 */     List<Type> list = union(closure(paramType1), closure(paramType2));
/* 3790 */     return glbFlattened(list, paramType1); }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private Type glbFlattened(List<Type> paramList, Type paramType) {
/* 3802 */     List<Type> list1 = closureMin(paramList);
/*      */
/* 3804 */     if (list1.isEmpty())
/* 3805 */       return this.syms.objectType;
/* 3806 */     if (list1.tail.isEmpty()) {
/* 3807 */       return (Type)list1.head;
/*      */     }
/* 3809 */     byte b = 0;
/* 3810 */     List<Type> list2 = List.nil();
/* 3811 */     for (Type type : list1) {
/* 3812 */       if (!type.isInterface()) {
/* 3813 */         b++;
/* 3814 */         Type type1 = cvarLowerBound(type);
/* 3815 */         if (type != type1 && !type1.hasTag(TypeTag.BOT))
/* 3816 */           list2 = insert(list2, type1);
/*      */       }
/*      */     }
/* 3819 */     if (b > 1) {
/* 3820 */       if (list2.isEmpty()) {
/* 3821 */         return createErrorType(paramType);
/*      */       }
/* 3823 */       return glbFlattened(union(list1, list2), paramType);
/*      */     }
/*      */
/* 3826 */     return makeIntersectionType(list1);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public int hashCode(Type paramType) {
/* 3835 */     return ((Integer)hashCode.visit(paramType)).intValue();
/*      */   }
/*      */
/* 3838 */   private static final UnaryVisitor<Integer> hashCode = new UnaryVisitor<Integer>()
/*      */     {
/*      */       public Integer visitType(Type param1Type, Void param1Void) {
/* 3841 */         return Integer.valueOf(param1Type.getTag().ordinal());
/*      */       }
/*      */
/*      */
/*      */       public Integer visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/* 3846 */         int i = visit(param1ClassType.getEnclosingType()).intValue();
/* 3847 */         i *= 127;
/* 3848 */         i += param1ClassType.tsym.flatName().hashCode();
/* 3849 */         for (Type type : param1ClassType.getTypeArguments()) {
/* 3850 */           i *= 127;
/* 3851 */           i += visit(type).intValue();
/*      */         }
/* 3853 */         return Integer.valueOf(i);
/*      */       }
/*      */
/*      */
/*      */       public Integer visitMethodType(Type.MethodType param1MethodType, Void param1Void) {
/* 3858 */         int i = TypeTag.METHOD.ordinal();
/* 3859 */         List<Type> list = param1MethodType.argtypes;
/* 3860 */         for (; list.tail != null;
/* 3861 */           list = list.tail)
/* 3862 */           i = (i << 5) + visit((Type)list.head).intValue();
/* 3863 */         return Integer.valueOf((i << 5) + visit(param1MethodType.restype).intValue());
/*      */       }
/*      */
/*      */
/*      */       public Integer visitWildcardType(Type.WildcardType param1WildcardType, Void param1Void) {
/* 3868 */         int i = param1WildcardType.kind.hashCode();
/* 3869 */         if (param1WildcardType.type != null) {
/* 3870 */           i *= 127;
/* 3871 */           i += visit(param1WildcardType.type).intValue();
/*      */         }
/* 3873 */         return Integer.valueOf(i);
/*      */       }
/*      */
/*      */
/*      */       public Integer visitArrayType(Type.ArrayType param1ArrayType, Void param1Void) {
/* 3878 */         return Integer.valueOf(visit(param1ArrayType.elemtype).intValue() + 12);
/*      */       }
/*      */
/*      */
/*      */       public Integer visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 3883 */         return Integer.valueOf(System.identityHashCode(param1TypeVar.tsym));
/*      */       }
/*      */
/*      */
/*      */       public Integer visitUndetVar(Type.UndetVar param1UndetVar, Void param1Void) {
/* 3888 */         return Integer.valueOf(System.identityHashCode(param1UndetVar));
/*      */       }
/*      */
/*      */
/*      */       public Integer visitErrorType(Type.ErrorType param1ErrorType, Void param1Void) {
/* 3893 */         return Integer.valueOf(0);
/*      */       }
/*      */     };
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean resultSubtype(Type paramType1, Type paramType2, Warner paramWarner) {
/* 3908 */     List<Type> list1 = paramType1.getTypeArguments();
/* 3909 */     List<Type> list2 = paramType2.getTypeArguments();
/* 3910 */     Type type1 = paramType1.getReturnType();
/* 3911 */     Type type2 = subst(paramType2.getReturnType(), list2, list1);
/* 3912 */     return covariantReturnType(type1, type2, paramWarner);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean returnTypeSubstitutable(Type paramType1, Type paramType2) {
/* 3920 */     if (hasSameArgs(paramType1, paramType2)) {
/* 3921 */       return resultSubtype(paramType1, paramType2, this.noWarnings);
/*      */     }
/* 3923 */     return covariantReturnType(paramType1.getReturnType(),
/* 3924 */         erasure(paramType2.getReturnType()), this.noWarnings);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean returnTypeSubstitutable(Type paramType1, Type paramType2, Type paramType3, Warner paramWarner) {
/* 3931 */     if (isSameType(paramType1.getReturnType(), paramType3))
/* 3932 */       return true;
/* 3933 */     if (paramType1.getReturnType().isPrimitive() || paramType3.isPrimitive()) {
/* 3934 */       return false;
/*      */     }
/* 3936 */     if (hasSameArgs(paramType1, paramType2))
/* 3937 */       return covariantReturnType(paramType1.getReturnType(), paramType3, paramWarner);
/* 3938 */     if (!this.allowCovariantReturns)
/* 3939 */       return false;
/* 3940 */     if (isSubtypeUnchecked(paramType1.getReturnType(), paramType3, paramWarner))
/* 3941 */       return true;
/* 3942 */     if (!isSubtype(paramType1.getReturnType(), erasure(paramType3)))
/* 3943 */       return false;
/* 3944 */     paramWarner.warn(Lint.LintCategory.UNCHECKED);
/* 3945 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean covariantReturnType(Type paramType1, Type paramType2, Warner paramWarner) {
/* 3953 */     return (
/* 3954 */       isSameType(paramType1, paramType2) || (this.allowCovariantReturns &&
/*      */
/* 3956 */       !paramType1.isPrimitive() &&
/* 3957 */       !paramType2.isPrimitive() &&
/* 3958 */       isAssignable(paramType1, paramType2, paramWarner)));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Symbol.ClassSymbol boxedClass(Type paramType) {
/* 3967 */     return this.reader.enterClass(this.syms.boxedName[paramType.getTag().ordinal()]);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public Type boxedTypeOrType(Type paramType) {
/* 3974 */     return paramType.isPrimitive() ?
/* 3975 */       (boxedClass(paramType)).type : paramType;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type unboxedType(Type paramType) {
/* 3983 */     if (this.allowBoxing)
/* 3984 */       for (byte b = 0; b < this.syms.boxedName.length; b++) {
/* 3985 */         Name name = this.syms.boxedName[b];
/* 3986 */         if (name != null &&
/* 3987 */           asSuper(paramType, this.reader.enterClass(name)) != null) {
/* 3988 */           return this.syms.typeOfTag[b];
/*      */         }
/*      */       }
/* 3991 */     return Type.noType;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public Type unboxedTypeOrType(Type paramType) {
/* 3998 */     Type type = unboxedType(paramType);
/* 3999 */     return type.hasTag(TypeTag.NONE) ? paramType : type;
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public List<Type> capture(List<Type> paramList) {
/* 4042 */     List list = List.nil();
/* 4043 */     for (Type type : paramList) {
/* 4044 */       list = list.prepend(capture(type));
/*      */     }
/* 4046 */     return list.reverse();
/*      */   }
/*      */
/*      */   public Type capture(Type paramType) {
/* 4050 */     if (!paramType.hasTag(TypeTag.CLASS)) {
/* 4051 */       return paramType;
/*      */     }
/* 4053 */     if (paramType.getEnclosingType() != Type.noType) {
/* 4054 */       Type type = capture(paramType.getEnclosingType());
/* 4055 */       if (type != paramType.getEnclosingType()) {
/* 4056 */         Type type1 = memberType(type, paramType.tsym);
/* 4057 */         paramType = subst(type1, paramType.tsym.type.getTypeArguments(), paramType.getTypeArguments());
/*      */       }
/*      */     }
/* 4060 */     paramType = paramType.unannotatedType();
/* 4061 */     Type.ClassType classType1 = (Type.ClassType)paramType;
/* 4062 */     if (classType1.isRaw() || !classType1.isParameterized()) {
/* 4063 */       return classType1;
/*      */     }
/* 4065 */     Type.ClassType classType2 = (Type.ClassType)classType1.asElement().asType();
/* 4066 */     List<Type> list1 = classType2.getTypeArguments();
/* 4067 */     List<Type> list2 = classType1.getTypeArguments();
/* 4068 */     List<Type> list3 = freshTypeVariables(list2);
/*      */
/* 4070 */     List<Type> list4 = list1;
/* 4071 */     List<Type> list5 = list2;
/* 4072 */     List<Type> list6 = list3;
/* 4073 */     boolean bool = false;
/* 4074 */     while (!list4.isEmpty() &&
/* 4075 */       !list5.isEmpty() &&
/* 4076 */       !list6.isEmpty()) {
/* 4077 */       if (list6.head != list5.head) {
/* 4078 */         bool = true;
/* 4079 */         Type.WildcardType wildcardType = (Type.WildcardType)((Type)list5.head).unannotatedType();
/* 4080 */         Type type1 = ((Type)list4.head).getUpperBound();
/* 4081 */         Type.CapturedType capturedType = (Type.CapturedType)((Type)list6.head).unannotatedType();
/* 4082 */         if (type1 == null)
/* 4083 */           type1 = this.syms.objectType;
/* 4084 */         switch (wildcardType.kind) {
/*      */           case CLASS:
/* 4086 */             capturedType.bound = subst(type1, list1, list3);
/* 4087 */             capturedType.lower = this.syms.botType;
/*      */             break;
/*      */           case ARRAY:
/* 4090 */             capturedType.bound = glb(wildcardType.getExtendsBound(), subst(type1, list1, list3));
/* 4091 */             capturedType.lower = this.syms.botType;
/*      */             break;
/*      */           case BYTE:
/* 4094 */             capturedType.bound = subst(type1, list1, list3);
/* 4095 */             capturedType.lower = wildcardType.getSuperBound();
/*      */             break;
/*      */         }
/* 4098 */         Type type2 = capturedType.bound.hasTag(TypeTag.UNDETVAR) ? ((Type.UndetVar)capturedType.bound).qtype : capturedType.bound;
/* 4099 */         Type type3 = capturedType.lower.hasTag(TypeTag.UNDETVAR) ? ((Type.UndetVar)capturedType.lower).qtype : capturedType.lower;
/* 4100 */         if (!capturedType.bound.hasTag(TypeTag.ERROR) &&
/* 4101 */           !capturedType.lower.hasTag(TypeTag.ERROR) &&
/* 4102 */           isSameType(type2, type3, false)) {
/* 4103 */           list6.head = capturedType.bound;
/*      */         }
/*      */       }
/* 4106 */       list4 = list4.tail;
/* 4107 */       list5 = list5.tail;
/* 4108 */       list6 = list6.tail;
/*      */     }
/* 4110 */     if (!list4.isEmpty() || !list5.isEmpty() || !list6.isEmpty()) {
/* 4111 */       return erasure(paramType);
/*      */     }
/* 4113 */     if (bool) {
/* 4114 */       return new Type.ClassType(classType1.getEnclosingType(), list3, classType1.tsym);
/*      */     }
/* 4116 */     return paramType;
/*      */   }
/*      */
/*      */   public List<Type> freshTypeVariables(List<Type> paramList) {
/* 4120 */     ListBuffer listBuffer = new ListBuffer();
/* 4121 */     for (Type type : paramList) {
/* 4122 */       if (type.hasTag(TypeTag.WILDCARD)) {
/* 4123 */         type = type.unannotatedType();
/* 4124 */         Type type1 = ((Type.WildcardType)type).getExtendsBound();
/* 4125 */         if (type1 == null)
/* 4126 */           type1 = this.syms.objectType;
/* 4127 */         listBuffer.append(new Type.CapturedType(this.capturedName, this.syms.noSymbol, type1, this.syms.botType, (Type.WildcardType)type));
/*      */
/*      */
/*      */         continue;
/*      */       }
/*      */
/* 4133 */       listBuffer.append(type);
/*      */     }
/*      */
/* 4136 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean sideCast(Type paramType1, Type paramType2, Warner paramWarner) {
/* 4146 */     boolean bool = false;
/* 4147 */     Type type = paramType2;
/* 4148 */     if ((paramType2.tsym.flags() & 0x200L) == 0L) {
/* 4149 */       Assert.check(((paramType1.tsym.flags() & 0x200L) != 0L));
/* 4150 */       bool = true;
/* 4151 */       paramType2 = paramType1;
/* 4152 */       paramType1 = type;
/*      */     }
/* 4154 */     List<Type> list = superClosure(paramType2, erasure(paramType1));
/* 4155 */     boolean bool1 = list.isEmpty();
/*      */
/*      */
/* 4158 */     while (list.nonEmpty()) {
/* 4159 */       Type type1 = asSuper(paramType1, ((Type)list.head).tsym);
/* 4160 */       Type type2 = (Type)list.head;
/* 4161 */       if (disjointTypes(type1.getTypeArguments(), type2.getTypeArguments()))
/* 4162 */         return false;
/* 4163 */       bool1 = (bool1 || (bool ? giveWarning(type2, type1) : giveWarning(type1, type2)));
/* 4164 */       list = list.tail;
/*      */     }
/* 4166 */     if (bool1 && !isReifiable(bool ? paramType1 : paramType2))
/* 4167 */       paramWarner.warn(Lint.LintCategory.UNCHECKED);
/* 4168 */     if (!this.allowCovariantReturns)
/*      */     {
/*      */
/* 4171 */       this.chk.checkCompatibleAbstracts(paramWarner.pos(), paramType1, paramType2); }
/* 4172 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean sideCastFinal(Type paramType1, Type paramType2, Warner paramWarner) {
/* 4181 */     boolean bool = false;
/* 4182 */     Type type1 = paramType2;
/* 4183 */     if ((paramType2.tsym.flags() & 0x200L) == 0L) {
/* 4184 */       Assert.check(((paramType1.tsym.flags() & 0x200L) != 0L));
/* 4185 */       bool = true;
/* 4186 */       paramType2 = paramType1;
/* 4187 */       paramType1 = type1;
/*      */     }
/* 4189 */     Assert.check(((paramType1.tsym.flags() & 0x10L) != 0L));
/* 4190 */     Type type2 = asSuper(paramType1, paramType2.tsym);
/* 4191 */     if (type2 == null) return false;
/* 4192 */     Type type3 = paramType2;
/* 4193 */     if (disjointTypes(type2.getTypeArguments(), type3.getTypeArguments()))
/* 4194 */       return false;
/* 4195 */     if (!this.allowCovariantReturns)
/*      */     {
/*      */
/* 4198 */       this.chk.checkCompatibleAbstracts(paramWarner.pos(), paramType1, paramType2); }
/* 4199 */     if (!isReifiable(type1) && (bool ?
/* 4200 */       giveWarning(type3, type2) : giveWarning(type2, type3)))
/* 4201 */       paramWarner.warn(Lint.LintCategory.UNCHECKED);
/* 4202 */     return true;
/*      */   }
/*      */
/*      */
/*      */   private boolean giveWarning(Type paramType1, Type paramType2) {
/* 4207 */     List list = paramType2.isCompound() ? ((Type.IntersectionClassType)paramType2.unannotatedType()).getComponents() : List.of(paramType2);
/* 4208 */     for (Type type1 : list) {
/* 4209 */       Type type2 = asSub(paramType1, type1.tsym);
/* 4210 */       if (type1.isParameterized() &&
/* 4211 */         !isUnbounded(type1) &&
/* 4212 */         !isSubtype(paramType1, type1) && (type2 == null ||
/* 4213 */         !containsType(type1.allparams(), type2.allparams()))) {
/* 4214 */         return true;
/*      */       }
/*      */     }
/* 4217 */     return false;
/*      */   }
/*      */
/*      */   private List<Type> superClosure(Type paramType1, Type paramType2) {
/* 4221 */     List<Type> list1 = List.nil();
/* 4222 */     for (List<Type> list2 = interfaces(paramType1); list2.nonEmpty(); list2 = list2.tail) {
/* 4223 */       if (isSubtype(paramType2, erasure((Type)list2.head))) {
/* 4224 */         list1 = insert(list1, (Type)list2.head);
/*      */       } else {
/* 4226 */         list1 = union(list1, superClosure((Type)list2.head, paramType2));
/*      */       }
/*      */     }
/* 4229 */     return list1;
/*      */   }
/*      */
/*      */   private boolean containsTypeEquivalent(Type paramType1, Type paramType2) {
/* 4233 */     return (
/* 4234 */       isSameType(paramType1, paramType2) || (
/* 4235 */       containsType(paramType1, paramType2) && containsType(paramType2, paramType1)));
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void adapt(Type paramType1, Type paramType2, ListBuffer<Type> paramListBuffer1, ListBuffer<Type> paramListBuffer2) throws AdaptFailure {
/* 4252 */     (new Adapter(paramListBuffer1, paramListBuffer2)).adapt(paramType1, paramType2);
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   class Adapter
/*      */     extends SimpleVisitor<Void, Type>
/*      */   {
/*      */     ListBuffer<Type> from;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     ListBuffer<Type> to;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     Map<Symbol, Type> mapping;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private Set<TypePair> cache;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     Adapter(ListBuffer<Type> param1ListBuffer1, ListBuffer<Type> param1ListBuffer2) {
/* 4333 */       this.cache = new HashSet<>(); this.from = param1ListBuffer1; this.to = param1ListBuffer2; this.mapping = new HashMap<>();
/*      */     } public void adapt(Type param1Type1, Type param1Type2) throws AdaptFailure { visit(param1Type1, param1Type2); List list1 = this.from.toList(); List list2 = this.to.toList(); while (!list1.isEmpty()) { Type type = this.mapping.get(((Type)list1.head).tsym); if (list2.head != type) list2.head = type;  list1 = list1.tail; list2 = list2.tail; }  } public Void visitClassType(Type.ClassType param1ClassType, Type param1Type) throws AdaptFailure { if (param1Type.hasTag(TypeTag.CLASS)) adaptRecursive(param1ClassType.allparams(), param1Type.allparams());  return null; } public Void visitArrayType(Type.ArrayType param1ArrayType, Type param1Type) throws AdaptFailure { if (param1Type.hasTag(TypeTag.ARRAY)) adaptRecursive(Types.this.elemtype(param1ArrayType), Types.this.elemtype(param1Type));  return null; } public Void visitWildcardType(Type.WildcardType param1WildcardType, Type param1Type) throws AdaptFailure { if (param1WildcardType.isExtendsBound()) { adaptRecursive(Types.this.wildUpperBound(param1WildcardType), Types.this.wildUpperBound(param1Type)); } else if (param1WildcardType.isSuperBound()) { adaptRecursive(Types.this.wildLowerBound(param1WildcardType), Types.this.wildLowerBound(param1Type)); }  return null; } public Void visitTypeVar(Type.TypeVar param1TypeVar, Type param1Type) throws AdaptFailure { Type type = this.mapping.get(param1TypeVar.tsym); if (type != null) { if (type.isSuperBound() && param1Type.isSuperBound()) { type = Types.this.isSubtype(Types.this.wildLowerBound(type), Types.this.wildLowerBound(param1Type)) ? param1Type : type; } else if (type.isExtendsBound() && param1Type.isExtendsBound()) { type = Types.this.isSubtype(Types.this.wildUpperBound(type), Types.this.wildUpperBound(param1Type)) ? type : param1Type; } else if (!Types.this.isSameType(type, param1Type)) { throw new AdaptFailure(); }  } else { type = param1Type; this.from.append(param1TypeVar); this.to.append(param1Type); }  this.mapping.put(param1TypeVar.tsym, type); return null; }
/*      */     public Void visitType(Type param1Type1, Type param1Type2) { return null; }
/* 4336 */     private void adaptRecursive(Type param1Type1, Type param1Type2) { TypePair typePair = new TypePair(param1Type1, param1Type2);
/* 4337 */       if (this.cache.add(typePair)) {
/*      */         try {
/* 4339 */           visit(param1Type1, param1Type2);
/*      */         } finally {
/* 4341 */           this.cache.remove(typePair);
/*      */         }
/*      */       } }
/*      */
/*      */
/*      */     private void adaptRecursive(List<Type> param1List1, List<Type> param1List2) {
/* 4347 */       if (param1List1.length() == param1List2.length()) {
/* 4348 */         while (param1List1.nonEmpty()) {
/* 4349 */           adaptRecursive((Type)param1List1.head, (Type)param1List2.head);
/* 4350 */           param1List1 = param1List1.tail;
/* 4351 */           param1List2 = param1List2.tail;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class AdaptFailure
/*      */     extends RuntimeException
/*      */   {
/*      */     static final long serialVersionUID = -7490231548272701566L;
/*      */   }
/*      */
/*      */   private void adaptSelf(Type paramType, ListBuffer<Type> paramListBuffer1, ListBuffer<Type> paramListBuffer2) {
/*      */     try {
/* 4366 */       adapt(paramType.tsym.type, paramType, paramListBuffer1, paramListBuffer2);
/* 4367 */     } catch (AdaptFailure adaptFailure) {
/*      */
/*      */
/* 4370 */       throw new AssertionError(adaptFailure);
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private Type rewriteQuantifiers(Type paramType, boolean paramBoolean1, boolean paramBoolean2) {
/* 4392 */     return (new Rewriter(paramBoolean1, paramBoolean2)).visit(paramType);
/*      */   }
/*      */
/*      */   class Rewriter
/*      */     extends UnaryVisitor<Type> {
/*      */     boolean high;
/*      */     boolean rewriteTypeVars;
/*      */
/*      */     Rewriter(boolean param1Boolean1, boolean param1Boolean2) {
/* 4401 */       this.high = param1Boolean1;
/* 4402 */       this.rewriteTypeVars = param1Boolean2;
/*      */     }
/*      */
/*      */
/*      */     public Type visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/* 4407 */       ListBuffer listBuffer = new ListBuffer();
/* 4408 */       boolean bool = false;
/* 4409 */       for (Type type1 : param1ClassType.allparams()) {
/* 4410 */         Type type2 = visit(type1);
/* 4411 */         if (type1 != type2) {
/* 4412 */           bool = true;
/*      */         }
/* 4414 */         listBuffer.append(type2);
/*      */       }
/* 4416 */       if (bool) {
/* 4417 */         return Types.this.subst(param1ClassType.tsym.type, param1ClassType.tsym.type
/* 4418 */             .allparams(), listBuffer
/* 4419 */             .toList());
/*      */       }
/* 4421 */       return param1ClassType;
/*      */     }
/*      */
/*      */     public Type visitType(Type param1Type, Void param1Void) {
/* 4425 */       return param1Type;
/*      */     }
/*      */
/*      */
/*      */     public Type visitCapturedType(Type.CapturedType param1CapturedType, Void param1Void) {
/* 4430 */       Type type1 = param1CapturedType.wildcard.type;
/*      */
/*      */
/* 4433 */       Type type2 = type1.contains(param1CapturedType) ? Types.this.erasure(type1) : visit(type1);
/* 4434 */       return rewriteAsWildcardType(visit(type2), param1CapturedType.wildcard.bound, param1CapturedType.wildcard.kind);
/*      */     }
/*      */
/*      */
/*      */     public Type visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 4439 */       if (this.rewriteTypeVars) {
/*      */
/*      */
/* 4442 */         Type type = param1TypeVar.bound.contains(param1TypeVar) ? Types.this.erasure(param1TypeVar.bound) : visit(param1TypeVar.bound);
/* 4443 */         return rewriteAsWildcardType(type, param1TypeVar, BoundKind.EXTENDS);
/*      */       }
/* 4445 */       return param1TypeVar;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public Type visitWildcardType(Type.WildcardType param1WildcardType, Void param1Void) {
/* 4451 */       Type type = visit(param1WildcardType.type);
/* 4452 */       return (param1WildcardType.type == type) ? param1WildcardType : rewriteAsWildcardType(type, param1WildcardType.bound, param1WildcardType.kind);
/*      */     }
/*      */
/*      */     private Type rewriteAsWildcardType(Type param1Type, Type.TypeVar param1TypeVar, BoundKind param1BoundKind) {
/* 4456 */       switch (param1BoundKind) { case ARRAY:
/* 4457 */           return this.high ? Types.this
/* 4458 */             .makeExtendsWildcard(B(param1Type), param1TypeVar) : Types.this
/* 4459 */             .makeExtendsWildcard(Types.this.syms.objectType, param1TypeVar);
/* 4460 */         case BYTE: return this.high ? Types.this
/* 4461 */             .makeSuperWildcard(Types.this.syms.botType, param1TypeVar) : Types.this
/* 4462 */             .makeSuperWildcard(B(param1Type), param1TypeVar);
/* 4463 */         case CLASS: return Types.this.makeExtendsWildcard(Types.this.syms.objectType, param1TypeVar); }
/*      */
/* 4465 */       Assert.error("Invalid bound kind " + param1BoundKind);
/* 4466 */       return null;
/*      */     }
/*      */
/*      */
/*      */     Type B(Type param1Type) {
/* 4471 */       while (param1Type.hasTag(TypeTag.WILDCARD)) {
/* 4472 */         Type.WildcardType wildcardType = (Type.WildcardType)param1Type.unannotatedType();
/*      */
/*      */
/* 4475 */         param1Type = this.high ? wildcardType.getExtendsBound() : wildcardType.getSuperBound();
/* 4476 */         if (param1Type == null) {
/* 4477 */           param1Type = this.high ? Types.this.syms.objectType : Types.this.syms.botType;
/*      */         }
/*      */       }
/* 4480 */       return param1Type;
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
/*      */
/*      */   private Type.WildcardType makeExtendsWildcard(Type paramType, Type.TypeVar paramTypeVar) {
/* 4494 */     if (paramType == this.syms.objectType) {
/* 4495 */       return new Type.WildcardType(this.syms.objectType, BoundKind.UNBOUND, this.syms.boundClass, paramTypeVar);
/*      */     }
/*      */
/*      */
/*      */
/* 4500 */     return new Type.WildcardType(paramType, BoundKind.EXTENDS, this.syms.boundClass, paramTypeVar);
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
/*      */
/*      */
/*      */
/*      */
/*      */   private Type.WildcardType makeSuperWildcard(Type paramType, Type.TypeVar paramTypeVar) {
/* 4516 */     if (paramType.hasTag(TypeTag.BOT)) {
/* 4517 */       return new Type.WildcardType(this.syms.objectType, BoundKind.UNBOUND, this.syms.boundClass, paramTypeVar);
/*      */     }
/*      */
/*      */
/*      */
/* 4522 */     return new Type.WildcardType(paramType, BoundKind.SUPER, this.syms.boundClass, paramTypeVar);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static class UniqueType
/*      */   {
/*      */     public final Type type;
/*      */
/*      */
/*      */     final Types types;
/*      */
/*      */
/*      */
/*      */     public UniqueType(Type param1Type, Types param1Types) {
/* 4537 */       this.type = param1Type;
/* 4538 */       this.types = param1Types;
/*      */     }
/*      */
/*      */     public int hashCode() {
/* 4542 */       return this.types.hashCode(this.type);
/*      */     }
/*      */
/*      */     public boolean equals(Object param1Object) {
/* 4546 */       return (param1Object instanceof UniqueType && this.types
/* 4547 */         .isSameAnnotatedType(this.type, ((UniqueType)param1Object).type));
/*      */     }
/*      */
/*      */     public String toString() {
/* 4551 */       return this.type.toString();
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
/*      */
/*      */
/*      */
/*      */
/*      */   public static abstract class DefaultTypeVisitor<R, S>
/*      */     implements Type.Visitor<R, S>
/*      */   {
/*      */     public final R visit(Type param1Type, S param1S) {
/* 4571 */       return param1Type.accept(this, param1S);
/* 4572 */     } public R visitClassType(Type.ClassType param1ClassType, S param1S) { return visitType(param1ClassType, param1S); }
/* 4573 */     public R visitWildcardType(Type.WildcardType param1WildcardType, S param1S) { return visitType(param1WildcardType, param1S); }
/* 4574 */     public R visitArrayType(Type.ArrayType param1ArrayType, S param1S) { return visitType(param1ArrayType, param1S); }
/* 4575 */     public R visitMethodType(Type.MethodType param1MethodType, S param1S) { return visitType(param1MethodType, param1S); }
/* 4576 */     public R visitPackageType(Type.PackageType param1PackageType, S param1S) { return visitType(param1PackageType, param1S); }
/* 4577 */     public R visitTypeVar(Type.TypeVar param1TypeVar, S param1S) { return visitType(param1TypeVar, param1S); }
/* 4578 */     public R visitCapturedType(Type.CapturedType param1CapturedType, S param1S) { return visitType(param1CapturedType, param1S); }
/* 4579 */     public R visitForAll(Type.ForAll param1ForAll, S param1S) { return visitType(param1ForAll, param1S); }
/* 4580 */     public R visitUndetVar(Type.UndetVar param1UndetVar, S param1S) { return visitType(param1UndetVar, param1S); } public R visitErrorType(Type.ErrorType param1ErrorType, S param1S) {
/* 4581 */       return visitType(param1ErrorType, param1S);
/*      */     } public R visitAnnotatedType(Type.AnnotatedType param1AnnotatedType, S param1S) {
/* 4583 */       return visit(param1AnnotatedType.unannotatedType(), param1S);
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
/*      */   public static abstract class DefaultSymbolVisitor<R, S>
/*      */     implements Symbol.Visitor<R, S>
/*      */   {
/*      */     public final R visit(Symbol param1Symbol, S param1S) {
/* 4599 */       return param1Symbol.accept(this, param1S);
/* 4600 */     } public R visitClassSymbol(Symbol.ClassSymbol param1ClassSymbol, S param1S) { return visitSymbol(param1ClassSymbol, param1S); }
/* 4601 */     public R visitMethodSymbol(Symbol.MethodSymbol param1MethodSymbol, S param1S) { return visitSymbol(param1MethodSymbol, param1S); }
/* 4602 */     public R visitOperatorSymbol(Symbol.OperatorSymbol param1OperatorSymbol, S param1S) { return visitSymbol(param1OperatorSymbol, param1S); }
/* 4603 */     public R visitPackageSymbol(Symbol.PackageSymbol param1PackageSymbol, S param1S) { return visitSymbol(param1PackageSymbol, param1S); }
/* 4604 */     public R visitTypeSymbol(Symbol.TypeSymbol param1TypeSymbol, S param1S) { return visitSymbol(param1TypeSymbol, param1S); } public R visitVarSymbol(Symbol.VarSymbol param1VarSymbol, S param1S) {
/* 4605 */       return visitSymbol(param1VarSymbol, param1S);
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
/*      */
/*      */
/*      */
/*      */   public static abstract class SimpleVisitor<R, S>
/*      */     extends DefaultTypeVisitor<R, S>
/*      */   {
/*      */     public R visitCapturedType(Type.CapturedType param1CapturedType, S param1S) {
/* 4624 */       return visitTypeVar(param1CapturedType, param1S);
/*      */     }
/*      */
/*      */     public R visitForAll(Type.ForAll param1ForAll, S param1S) {
/* 4628 */       return visit(param1ForAll.qtype, param1S);
/*      */     }
/*      */
/*      */     public R visitUndetVar(Type.UndetVar param1UndetVar, S param1S) {
/* 4632 */       return visit(param1UndetVar.qtype, param1S);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static abstract class TypeRelation
/*      */     extends SimpleVisitor<Boolean, Type> {}
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static abstract class UnaryVisitor<R>
/*      */     extends SimpleVisitor<R, Void>
/*      */   {
/*      */     public final R visit(Type param1Type) {
/* 4652 */       return param1Type.accept(this, (Object)null);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static class MapVisitor<S>
/*      */     extends DefaultTypeVisitor<Type, S>
/*      */   {
/*      */     public final Type visit(Type param1Type)
/*      */     {
/* 4666 */       return param1Type.accept(this, (Object)null); } public Type visitType(Type param1Type, S param1S) {
/* 4667 */       return param1Type;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public Attribute.RetentionPolicy getRetention(Attribute.Compound paramCompound) {
/* 4675 */     return getRetention(paramCompound.type.tsym);
/*      */   }
/*      */
/*      */   public Attribute.RetentionPolicy getRetention(Symbol paramSymbol) {
/* 4679 */     Attribute.RetentionPolicy retentionPolicy = Attribute.RetentionPolicy.CLASS;
/* 4680 */     Attribute.Compound compound = paramSymbol.attribute(this.syms.retentionType.tsym);
/* 4681 */     if (compound != null) {
/* 4682 */       Attribute attribute = compound.member(this.names.value);
/* 4683 */       if (attribute != null && attribute instanceof Attribute.Enum) {
/* 4684 */         Name name = ((Attribute.Enum)attribute).value.name;
/* 4685 */         if (name == this.names.SOURCE) { retentionPolicy = Attribute.RetentionPolicy.SOURCE; }
/* 4686 */         else if (name == this.names.CLASS) { retentionPolicy = Attribute.RetentionPolicy.CLASS; }
/* 4687 */         else if (name == this.names.RUNTIME) { retentionPolicy = Attribute.RetentionPolicy.RUNTIME; }
/*      */
/*      */       }
/*      */     }
/* 4691 */     return retentionPolicy;
/*      */   }
/*      */
/*      */   public static abstract class SignatureGenerator
/*      */   {
/*      */     private final Types types;
/*      */
/*      */     protected abstract void append(char param1Char);
/*      */
/*      */     protected abstract void append(byte[] param1ArrayOfbyte);
/*      */
/*      */     protected abstract void append(Name param1Name);
/*      */
/*      */     protected void classReference(Symbol.ClassSymbol param1ClassSymbol) {}
/*      */
/*      */     protected SignatureGenerator(Types param1Types) {
/* 4707 */       this.types = param1Types;
/*      */     }
/*      */     public void assembleSig(Type param1Type) {
/*      */       Type.ArrayType arrayType;
/*      */       Type.MethodType methodType;
/*      */       Type.WildcardType wildcardType;
/*      */       Type.ForAll forAll;
/* 4714 */       param1Type = param1Type.unannotatedType();
/* 4715 */       switch (param1Type.getTag()) {
/*      */         case BYTE:
/* 4717 */           append('B');
/*      */           return;
/*      */         case SHORT:
/* 4720 */           append('S');
/*      */           return;
/*      */         case CHAR:
/* 4723 */           append('C');
/*      */           return;
/*      */         case INT:
/* 4726 */           append('I');
/*      */           return;
/*      */         case LONG:
/* 4729 */           append('J');
/*      */           return;
/*      */         case FLOAT:
/* 4732 */           append('F');
/*      */           return;
/*      */         case DOUBLE:
/* 4735 */           append('D');
/*      */           return;
/*      */         case BOOLEAN:
/* 4738 */           append('Z');
/*      */           return;
/*      */         case VOID:
/* 4741 */           append('V');
/*      */           return;
/*      */         case CLASS:
/* 4744 */           append('L');
/* 4745 */           assembleClassSig(param1Type);
/* 4746 */           append(';');
/*      */           return;
/*      */         case ARRAY:
/* 4749 */           arrayType = (Type.ArrayType)param1Type;
/* 4750 */           append('[');
/* 4751 */           assembleSig(arrayType.elemtype);
/*      */           return;
/*      */         case METHOD:
/* 4754 */           methodType = (Type.MethodType)param1Type;
/* 4755 */           append('(');
/* 4756 */           assembleSig(methodType.argtypes);
/* 4757 */           append(')');
/* 4758 */           assembleSig(methodType.restype);
/* 4759 */           if (hasTypeVar(methodType.thrown)) {
/* 4760 */             for (List<Type> list = methodType.thrown; list.nonEmpty(); list = list.tail) {
/* 4761 */               append('^');
/* 4762 */               assembleSig((Type)list.head);
/*      */             }
/*      */           }
/*      */           return;
/*      */         case WILDCARD:
/* 4767 */           wildcardType = (Type.WildcardType)param1Type;
/* 4768 */           switch (wildcardType.kind) {
/*      */             case BYTE:
/* 4770 */               append('-');
/* 4771 */               assembleSig(wildcardType.type);
/*      */               return;
/*      */             case ARRAY:
/* 4774 */               append('+');
/* 4775 */               assembleSig(wildcardType.type);
/*      */               return;
/*      */             case CLASS:
/* 4778 */               append('*');
/*      */               return;
/*      */           }
/* 4781 */           throw new AssertionError(wildcardType.kind);
/*      */
/*      */
/*      */
/*      */         case TYPEVAR:
/* 4786 */           append('T');
/* 4787 */           append(param1Type.tsym.name);
/* 4788 */           append(';');
/*      */           return;
/*      */         case FORALL:
/* 4791 */           forAll = (Type.ForAll)param1Type;
/* 4792 */           assembleParamsSig(forAll.tvars);
/* 4793 */           assembleSig(forAll.qtype);
/*      */           return;
/*      */       }
/* 4796 */       throw new AssertionError("typeSig " + param1Type.getTag());
/*      */     }
/*      */
/*      */
/*      */     public boolean hasTypeVar(List<Type> param1List) {
/* 4801 */       while (param1List.nonEmpty()) {
/* 4802 */         if (((Type)param1List.head).hasTag(TypeTag.TYPEVAR)) {
/* 4803 */           return true;
/*      */         }
/* 4805 */         param1List = param1List.tail;
/*      */       }
/* 4807 */       return false;
/*      */     }
/*      */
/*      */     public void assembleClassSig(Type param1Type) {
/* 4811 */       param1Type = param1Type.unannotatedType();
/* 4812 */       Type.ClassType classType = (Type.ClassType)param1Type;
/* 4813 */       Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)classType.tsym;
/* 4814 */       classReference(classSymbol);
/* 4815 */       Type type = classType.getEnclosingType();
/* 4816 */       if (type.allparams().nonEmpty()) {
/* 4817 */         boolean bool = (classSymbol.owner.kind == 16 || classSymbol.name == this.types.names.empty) ? true : false;
/*      */
/*      */
/* 4820 */         assembleClassSig(bool ? this.types
/* 4821 */             .erasure(type) : type);
/*      */
/* 4823 */         append(bool ? 36 : 46);
/* 4824 */         Assert.check(classSymbol.flatname.startsWith((classSymbol.owner.enclClass()).flatname));
/* 4825 */         append(bool ? classSymbol.flatname
/* 4826 */             .subName((classSymbol.owner.enclClass()).flatname.getByteLength() + 1, classSymbol.flatname.getByteLength()) : classSymbol.name);
/*      */       } else {
/*      */
/* 4829 */         append(ClassFile.externalize(classSymbol.flatname));
/*      */       }
/* 4831 */       if (classType.getTypeArguments().nonEmpty()) {
/* 4832 */         append('<');
/* 4833 */         assembleSig(classType.getTypeArguments());
/* 4834 */         append('>');
/*      */       }
/*      */     }
/*      */
/*      */     public void assembleParamsSig(List<Type> param1List) {
/* 4839 */       append('<');
/* 4840 */       for (List<Type> list = param1List; list.nonEmpty(); list = list.tail) {
/* 4841 */         Type.TypeVar typeVar = (Type.TypeVar)list.head;
/* 4842 */         append(typeVar.tsym.name);
/* 4843 */         List<Type> list1 = this.types.getBounds(typeVar);
/* 4844 */         if ((((Type)list1.head).tsym.flags() & 0x200L) != 0L) {
/* 4845 */           append(':');
/*      */         }
/* 4847 */         for (List<Type> list2 = list1; list2.nonEmpty(); list2 = list2.tail) {
/* 4848 */           append(':');
/* 4849 */           assembleSig((Type)list2.head);
/*      */         }
/*      */       }
/* 4852 */       append('>');
/*      */     }
/*      */
/*      */     private void assembleSig(List<Type> param1List) {
/* 4856 */       for (List<Type> list = param1List; list.nonEmpty(); list = list.tail)
/* 4857 */         assembleSig((Type)list.head);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Types.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
