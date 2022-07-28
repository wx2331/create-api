/*     */ package com.sun.tools.javac.code;
/*     */
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Constants;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.AnnotationValueVisitor;
/*     */ import javax.lang.model.type.DeclaredType;
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
/*     */ public abstract class Attribute
/*     */   implements AnnotationValue
/*     */ {
/*     */   public Type type;
/*     */
/*     */   public Attribute(Type paramType) {
/*  50 */     this.type = paramType;
/*     */   }
/*     */
/*     */   public abstract void accept(Visitor paramVisitor);
/*     */
/*     */   public Object getValue() {
/*  56 */     throw new UnsupportedOperationException();
/*     */   }
/*     */
/*     */   public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/*  60 */     throw new UnsupportedOperationException();
/*     */   }
/*     */
/*     */   public boolean isSynthesized() {
/*  64 */     return false;
/*     */   }
/*     */   public TypeAnnotationPosition getPosition() {
/*  67 */     return null;
/*     */   }
/*     */
/*     */   public static class Constant extends Attribute {
/*     */     public void accept(Visitor param1Visitor) {
/*  72 */       param1Visitor.visitConstant(this);
/*     */     } public final Object value; public Constant(Type param1Type, Object param1Object) {
/*  74 */       super(param1Type);
/*  75 */       this.value = param1Object;
/*     */     }
/*     */     public String toString() {
/*  78 */       return Constants.format(this.value, this.type);
/*     */     }
/*     */     public Object getValue() {
/*  81 */       return Constants.decode(this.value, this.type);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> param1AnnotationValueVisitor, P param1P) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield value : Ljava/lang/Object;
/*     */       //   4: instanceof java/lang/String
/*     */       //   7: ifeq -> 25
/*     */       //   10: aload_1
/*     */       //   11: aload_0
/*     */       //   12: getfield value : Ljava/lang/Object;
/*     */       //   15: checkcast java/lang/String
/*     */       //   18: aload_2
/*     */       //   19: invokeinterface visitString : (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   24: areturn
/*     */       //   25: aload_0
/*     */       //   26: getfield value : Ljava/lang/Object;
/*     */       //   29: instanceof java/lang/Integer
/*     */       //   32: ifeq -> 152
/*     */       //   35: aload_0
/*     */       //   36: getfield value : Ljava/lang/Object;
/*     */       //   39: checkcast java/lang/Integer
/*     */       //   42: invokevirtual intValue : ()I
/*     */       //   45: istore_3
/*     */       //   46: getstatic com/sun/tools/javac/code/Attribute$1.$SwitchMap$com$sun$tools$javac$code$TypeTag : [I
/*     */       //   49: aload_0
/*     */       //   50: getfield type : Lcom/sun/tools/javac/code/Type;
/*     */       //   53: invokevirtual getTag : ()Lcom/sun/tools/javac/code/TypeTag;
/*     */       //   56: invokevirtual ordinal : ()I
/*     */       //   59: iaload
/*     */       //   60: tableswitch default -> 152, 1 -> 96, 2 -> 113, 3 -> 123, 4 -> 133, 5 -> 143
/*     */       //   96: aload_1
/*     */       //   97: iload_3
/*     */       //   98: ifeq -> 105
/*     */       //   101: iconst_1
/*     */       //   102: goto -> 106
/*     */       //   105: iconst_0
/*     */       //   106: aload_2
/*     */       //   107: invokeinterface visitBoolean : (ZLjava/lang/Object;)Ljava/lang/Object;
/*     */       //   112: areturn
/*     */       //   113: aload_1
/*     */       //   114: iload_3
/*     */       //   115: i2c
/*     */       //   116: aload_2
/*     */       //   117: invokeinterface visitChar : (CLjava/lang/Object;)Ljava/lang/Object;
/*     */       //   122: areturn
/*     */       //   123: aload_1
/*     */       //   124: iload_3
/*     */       //   125: i2b
/*     */       //   126: aload_2
/*     */       //   127: invokeinterface visitByte : (BLjava/lang/Object;)Ljava/lang/Object;
/*     */       //   132: areturn
/*     */       //   133: aload_1
/*     */       //   134: iload_3
/*     */       //   135: i2s
/*     */       //   136: aload_2
/*     */       //   137: invokeinterface visitShort : (SLjava/lang/Object;)Ljava/lang/Object;
/*     */       //   142: areturn
/*     */       //   143: aload_1
/*     */       //   144: iload_3
/*     */       //   145: aload_2
/*     */       //   146: invokeinterface visitInt : (ILjava/lang/Object;)Ljava/lang/Object;
/*     */       //   151: areturn
/*     */       //   152: getstatic com/sun/tools/javac/code/Attribute$1.$SwitchMap$com$sun$tools$javac$code$TypeTag : [I
/*     */       //   155: aload_0
/*     */       //   156: getfield type : Lcom/sun/tools/javac/code/Type;
/*     */       //   159: invokevirtual getTag : ()Lcom/sun/tools/javac/code/TypeTag;
/*     */       //   162: invokevirtual ordinal : ()I
/*     */       //   165: iaload
/*     */       //   166: tableswitch default -> 246, 6 -> 192, 7 -> 210, 8 -> 228
/*     */       //   192: aload_1
/*     */       //   193: aload_0
/*     */       //   194: getfield value : Ljava/lang/Object;
/*     */       //   197: checkcast java/lang/Long
/*     */       //   200: invokevirtual longValue : ()J
/*     */       //   203: aload_2
/*     */       //   204: invokeinterface visitLong : (JLjava/lang/Object;)Ljava/lang/Object;
/*     */       //   209: areturn
/*     */       //   210: aload_1
/*     */       //   211: aload_0
/*     */       //   212: getfield value : Ljava/lang/Object;
/*     */       //   215: checkcast java/lang/Float
/*     */       //   218: invokevirtual floatValue : ()F
/*     */       //   221: aload_2
/*     */       //   222: invokeinterface visitFloat : (FLjava/lang/Object;)Ljava/lang/Object;
/*     */       //   227: areturn
/*     */       //   228: aload_1
/*     */       //   229: aload_0
/*     */       //   230: getfield value : Ljava/lang/Object;
/*     */       //   233: checkcast java/lang/Double
/*     */       //   236: invokevirtual doubleValue : ()D
/*     */       //   239: aload_2
/*     */       //   240: invokeinterface visitDouble : (DLjava/lang/Object;)Ljava/lang/Object;
/*     */       //   245: areturn
/*     */       //   246: new java/lang/AssertionError
/*     */       //   249: dup
/*     */       //   250: new java/lang/StringBuilder
/*     */       //   253: dup
/*     */       //   254: invokespecial <init> : ()V
/*     */       //   257: ldc 'Bad annotation element value: '
/*     */       //   259: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   262: aload_0
/*     */       //   263: getfield value : Ljava/lang/Object;
/*     */       //   266: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   269: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   272: invokespecial <init> : (Ljava/lang/Object;)V
/*     */       //   275: athrow
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       //   #85	-> 10
/*     */       //   #86	-> 25
/*     */       //   #87	-> 35
/*     */       //   #88	-> 46
/*     */       //   #89	-> 96
/*     */       //   #90	-> 113
/*     */       //   #91	-> 123
/*     */       //   #92	-> 133
/*     */       //   #93	-> 143
/*     */       //   #96	-> 152
/*     */       //   #97	-> 192
/*     */       //   #98	-> 210
/*     */       //   #99	-> 228
/*     */       //   #101	-> 246
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class Class
/*     */     extends Attribute
/*     */   {
/*     */     public final Type classType;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void accept(Visitor param1Visitor) {
/* 110 */       param1Visitor.visitClass(this);
/*     */     } public Class(Types param1Types, Type param1Type) {
/* 112 */       super(makeClassType(param1Types, param1Type));
/* 113 */       this.classType = param1Type;
/*     */     }
/*     */
/*     */
/*     */     static Type makeClassType(Types param1Types, Type param1Type) {
/* 118 */       Type type = param1Type.isPrimitive() ? (param1Types.boxedClass(param1Type)).type : param1Types.erasure(param1Type);
/* 119 */       return new Type.ClassType(param1Types.syms.classType.getEnclosingType(),
/* 120 */           List.of(type), param1Types.syms.classType.tsym);
/*     */     }
/*     */
/*     */     public String toString() {
/* 124 */       return this.classType + ".class";
/*     */     }
/*     */     public Type getValue() {
/* 127 */       return this.classType;
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> param1AnnotationValueVisitor, P param1P) {
/* 130 */       return param1AnnotationValueVisitor.visitType(this.classType, param1P);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public static class Compound
/*     */     extends Attribute
/*     */     implements AnnotationMirror
/*     */   {
/*     */     public final List<Pair<Symbol.MethodSymbol, Attribute>> values;
/*     */
/*     */
/*     */     private boolean synthesized = false;
/*     */
/*     */
/*     */
/*     */     public boolean isSynthesized() {
/* 149 */       return this.synthesized;
/*     */     }
/*     */
/*     */     public void setSynthesized(boolean param1Boolean) {
/* 153 */       this.synthesized = param1Boolean;
/*     */     }
/*     */
/*     */
/*     */     public Compound(Type param1Type, List<Pair<Symbol.MethodSymbol, Attribute>> param1List) {
/* 158 */       super(param1Type);
/* 159 */       this.values = param1List;
/*     */     } public void accept(Visitor param1Visitor) {
/* 161 */       param1Visitor.visitCompound(this);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String toString() {
/* 172 */       StringBuilder stringBuilder = new StringBuilder();
/* 173 */       stringBuilder.append("@");
/* 174 */       stringBuilder.append(this.type);
/* 175 */       int i = this.values.length();
/* 176 */       if (i > 0) {
/* 177 */         stringBuilder.append('(');
/* 178 */         boolean bool = true;
/* 179 */         for (Pair<Symbol.MethodSymbol, Attribute> pair : this.values) {
/* 180 */           if (!bool) stringBuilder.append(", ");
/* 181 */           bool = false;
/*     */
/* 183 */           Name name = ((Symbol.MethodSymbol)pair.fst).name;
/* 184 */           if (i > 1 || name != name.table.names.value) {
/* 185 */             stringBuilder.append((CharSequence)name);
/* 186 */             stringBuilder.append('=');
/*     */           }
/* 188 */           stringBuilder.append(pair.snd);
/*     */         }
/* 190 */         stringBuilder.append(')');
/*     */       }
/* 192 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */     public Attribute member(Name param1Name) {
/* 196 */       Pair<Symbol.MethodSymbol, Attribute> pair = getElemPair(param1Name);
/* 197 */       return (pair == null) ? null : (Attribute)pair.snd;
/*     */     }
/*     */
/*     */     private Pair<Symbol.MethodSymbol, Attribute> getElemPair(Name param1Name) {
/* 201 */       for (Pair<Symbol.MethodSymbol, Attribute> pair : this.values) {
/* 202 */         if (((Symbol.MethodSymbol)pair.fst).name == param1Name) return pair;
/* 203 */       }  return null;
/*     */     }
/*     */
/*     */     public Compound getValue() {
/* 207 */       return this;
/*     */     }
/*     */
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> param1AnnotationValueVisitor, P param1P) {
/* 211 */       return param1AnnotationValueVisitor.visitAnnotation(this, param1P);
/*     */     }
/*     */
/*     */     public DeclaredType getAnnotationType() {
/* 215 */       return (DeclaredType)this.type;
/*     */     }
/*     */
/*     */
/*     */     public TypeAnnotationPosition getPosition() {
/* 220 */       if (this.values.size() != 0) {
/* 221 */         Name name = ((Symbol.MethodSymbol)((Pair)this.values.head).fst).name.table.names.value;
/* 222 */         Pair<Symbol.MethodSymbol, Attribute> pair = getElemPair(name);
/* 223 */         return (pair == null) ? null : ((Attribute)pair.snd).getPosition();
/*     */       }
/* 225 */       return null;
/*     */     }
/*     */
/*     */     public Map<Symbol.MethodSymbol, Attribute> getElementValues() {
/* 229 */       LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*     */
/* 231 */       for (Pair<Symbol.MethodSymbol, Attribute> pair : this.values)
/* 232 */         linkedHashMap.put(pair.fst, pair.snd);
/* 233 */       return (Map)linkedHashMap;
/*     */     }
/*     */   }
/*     */
/*     */   public static class TypeCompound
/*     */     extends Compound {
/*     */     public TypeAnnotationPosition position;
/*     */
/*     */     public TypeCompound(Compound param1Compound, TypeAnnotationPosition param1TypeAnnotationPosition) {
/* 242 */       this(param1Compound.type, param1Compound.values, param1TypeAnnotationPosition);
/*     */     }
/*     */
/*     */
/*     */     public TypeCompound(Type param1Type, List<Pair<Symbol.MethodSymbol, Attribute>> param1List, TypeAnnotationPosition param1TypeAnnotationPosition) {
/* 247 */       super(param1Type, param1List);
/* 248 */       this.position = param1TypeAnnotationPosition;
/*     */     }
/*     */
/*     */
/*     */     public TypeAnnotationPosition getPosition() {
/* 253 */       if (hasUnknownPosition()) {
/* 254 */         this.position = super.getPosition();
/*     */       }
/* 256 */       return this.position;
/*     */     }
/*     */
/*     */     public boolean hasUnknownPosition() {
/* 260 */       return (this.position.type == TargetType.UNKNOWN);
/*     */     }
/*     */
/*     */     public boolean isContainerTypeCompound() {
/* 264 */       if (isSynthesized() && this.values.size() == 1)
/* 265 */         return (getFirstEmbeddedTC() != null);
/* 266 */       return false;
/*     */     }
/*     */
/*     */     private TypeCompound getFirstEmbeddedTC() {
/* 270 */       if (this.values.size() == 1) {
/* 271 */         Pair pair = (Pair)this.values.get(0);
/* 272 */         if (((Symbol.MethodSymbol)pair.fst).getSimpleName().contentEquals("value") && pair.snd instanceof Array) {
/*     */
/* 274 */           Array array = (Array)pair.snd;
/* 275 */           if (array.values.length != 0 && array.values[0] instanceof TypeCompound)
/*     */           {
/* 277 */             return (TypeCompound)array.values[0]; }
/*     */         }
/*     */       }
/* 280 */       return null;
/*     */     }
/*     */
/*     */     public boolean tryFixPosition() {
/* 284 */       if (!isContainerTypeCompound()) {
/* 285 */         return false;
/*     */       }
/* 287 */       TypeCompound typeCompound = getFirstEmbeddedTC();
/* 288 */       if (typeCompound != null && typeCompound.position != null && typeCompound.position.type != TargetType.UNKNOWN) {
/*     */
/* 290 */         this.position = typeCompound.position;
/* 291 */         return true;
/*     */       }
/* 293 */       return false;
/*     */     }
/*     */   }
/*     */
/*     */   public static class Array
/*     */     extends Attribute {
/*     */     public final Attribute[] values;
/*     */
/*     */     public Array(Type param1Type, Attribute[] param1ArrayOfAttribute) {
/* 302 */       super(param1Type);
/* 303 */       this.values = param1ArrayOfAttribute;
/*     */     }
/*     */
/*     */     public Array(Type param1Type, List<Attribute> param1List) {
/* 307 */       super(param1Type);
/* 308 */       this.values = (Attribute[])param1List.toArray((Object[])new Attribute[param1List.size()]);
/*     */     }
/*     */     public void accept(Visitor param1Visitor) {
/* 311 */       param1Visitor.visitArray(this);
/*     */     } public String toString() {
/* 313 */       StringBuilder stringBuilder = new StringBuilder();
/* 314 */       stringBuilder.append('{');
/* 315 */       boolean bool = true;
/* 316 */       for (Attribute attribute : this.values) {
/* 317 */         if (!bool)
/* 318 */           stringBuilder.append(", ");
/* 319 */         bool = false;
/* 320 */         stringBuilder.append(attribute);
/*     */       }
/* 322 */       stringBuilder.append('}');
/* 323 */       return stringBuilder.toString();
/*     */     }
/*     */     public List<Attribute> getValue() {
/* 326 */       return List.from((Object[])this.values);
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> param1AnnotationValueVisitor, P param1P) {
/* 329 */       return param1AnnotationValueVisitor.visitArray((List)getValue(), param1P);
/*     */     }
/*     */
/*     */
/*     */     public TypeAnnotationPosition getPosition() {
/* 334 */       if (this.values.length != 0) {
/* 335 */         return this.values[0].getPosition();
/*     */       }
/* 337 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */   public static class Enum
/*     */     extends Attribute {
/*     */     public Symbol.VarSymbol value;
/*     */
/*     */     public Enum(Type param1Type, Symbol.VarSymbol param1VarSymbol) {
/* 346 */       super(param1Type);
/* 347 */       this.value = (Symbol.VarSymbol)Assert.checkNonNull(param1VarSymbol);
/*     */     } public void accept(Visitor param1Visitor) {
/* 349 */       param1Visitor.visitEnum(this);
/*     */     } public String toString() {
/* 351 */       return this.value.enclClass() + "." + this.value;
/*     */     }
/*     */     public Symbol.VarSymbol getValue() {
/* 354 */       return this.value;
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> param1AnnotationValueVisitor, P param1P) {
/* 357 */       return param1AnnotationValueVisitor.visitEnumConstant(this.value, param1P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class Error extends Attribute {
/*     */     public Error(Type param1Type) {
/* 363 */       super(param1Type);
/*     */     } public void accept(Visitor param1Visitor) {
/* 365 */       param1Visitor.visitError(this);
/*     */     } public String toString() {
/* 367 */       return "<error>";
/*     */     }
/*     */     public String getValue() {
/* 370 */       return toString();
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> param1AnnotationValueVisitor, P param1P) {
/* 373 */       return param1AnnotationValueVisitor.visitString(toString(), param1P);
/*     */     } }
/*     */
/*     */   public static class UnresolvedClass extends Error {
/*     */     public Type classType;
/*     */
/*     */     public UnresolvedClass(Type param1Type1, Type param1Type2) {
/* 380 */       super(param1Type1);
/* 381 */       this.classType = param1Type2;
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
/*     */   public enum RetentionPolicy
/*     */   {
/* 397 */     SOURCE,
/* 398 */     CLASS,
/* 399 */     RUNTIME;
/*     */   }
/*     */
/*     */   public static interface Visitor {
/*     */     void visitConstant(Constant param1Constant);
/*     */
/*     */     void visitClass(Class param1Class);
/*     */
/*     */     void visitCompound(Compound param1Compound);
/*     */
/*     */     void visitArray(Array param1Array);
/*     */
/*     */     void visitEnum(Enum param1Enum);
/*     */
/*     */     void visitError(Error param1Error);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
