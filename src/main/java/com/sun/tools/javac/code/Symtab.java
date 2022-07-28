/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.jvm.ClassReader;
/*     */ import com.sun.tools.javac.jvm.Target;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JavacMessages;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.ElementVisitor;
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
/*     */ 
/*     */ public class Symtab
/*     */ {
/*  54 */   protected static final Context.Key<Symtab> symtabKey = new Context.Key();
/*     */ 
/*     */ 
/*     */   
/*     */   public static Symtab instance(Context paramContext) {
/*  59 */     Symtab symtab = (Symtab)paramContext.get(symtabKey);
/*  60 */     if (symtab == null)
/*  61 */       symtab = new Symtab(paramContext); 
/*  62 */     return symtab;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public final Type.JCPrimitiveType byteType = new Type.JCPrimitiveType(TypeTag.BYTE, null);
/*  68 */   public final Type.JCPrimitiveType charType = new Type.JCPrimitiveType(TypeTag.CHAR, null);
/*  69 */   public final Type.JCPrimitiveType shortType = new Type.JCPrimitiveType(TypeTag.SHORT, null);
/*  70 */   public final Type.JCPrimitiveType intType = new Type.JCPrimitiveType(TypeTag.INT, null);
/*  71 */   public final Type.JCPrimitiveType longType = new Type.JCPrimitiveType(TypeTag.LONG, null);
/*  72 */   public final Type.JCPrimitiveType floatType = new Type.JCPrimitiveType(TypeTag.FLOAT, null);
/*  73 */   public final Type.JCPrimitiveType doubleType = new Type.JCPrimitiveType(TypeTag.DOUBLE, null);
/*  74 */   public final Type.JCPrimitiveType booleanType = new Type.JCPrimitiveType(TypeTag.BOOLEAN, null);
/*  75 */   public final Type botType = new Type.BottomType();
/*  76 */   public final Type.JCVoidType voidType = new Type.JCVoidType();
/*     */   
/*     */   private final Names names;
/*     */   
/*     */   private final ClassReader reader;
/*     */   
/*     */   private final Target target;
/*     */   
/*     */   public final Symbol.PackageSymbol rootPackage;
/*     */   
/*     */   public final Symbol.PackageSymbol unnamedPackage;
/*     */   
/*     */   public final Symbol.TypeSymbol noSymbol;
/*     */   
/*     */   public final Symbol.ClassSymbol errSymbol;
/*     */   
/*     */   public final Symbol.ClassSymbol unknownSymbol;
/*     */   
/*     */   public final Type errType;
/*     */   
/*     */   public final Type unknownType;
/*     */   
/*     */   public final Symbol.ClassSymbol arrayClass;
/*     */   
/*     */   public final Symbol.MethodSymbol arrayCloneMethod;
/*     */   
/*     */   public final Symbol.ClassSymbol boundClass;
/*     */   
/*     */   public final Symbol.ClassSymbol methodClass;
/*     */   
/*     */   public final Type objectType;
/*     */   
/*     */   public final Type classType;
/*     */   
/*     */   public final Type classLoaderType;
/*     */   
/*     */   public final Type stringType;
/*     */   
/*     */   public final Type stringBufferType;
/*     */   
/*     */   public final Type stringBuilderType;
/*     */   
/*     */   public final Type cloneableType;
/*     */   
/*     */   public final Type serializableType;
/*     */   
/*     */   public final Type serializedLambdaType;
/*     */   
/*     */   public final Type methodHandleType;
/*     */   
/*     */   public final Type methodHandleLookupType;
/*     */   
/*     */   public final Type methodTypeType;
/*     */   
/*     */   public final Type nativeHeaderType;
/*     */   
/*     */   public final Type throwableType;
/*     */   
/*     */   public final Type errorType;
/*     */   
/*     */   public final Type interruptedExceptionType;
/*     */   
/*     */   public final Type illegalArgumentExceptionType;
/*     */   
/*     */   public final Type exceptionType;
/*     */   
/*     */   public final Type runtimeExceptionType;
/*     */   
/*     */   public final Type classNotFoundExceptionType;
/*     */   
/*     */   public final Type noClassDefFoundErrorType;
/*     */   
/*     */   public final Type noSuchFieldErrorType;
/*     */   
/*     */   public final Type assertionErrorType;
/*     */   
/*     */   public final Type cloneNotSupportedExceptionType;
/*     */   
/*     */   public final Type annotationType;
/*     */   
/*     */   public final Symbol.TypeSymbol enumSym;
/*     */   
/*     */   public final Type listType;
/*     */   public final Type collectionsType;
/*     */   public final Type comparableType;
/*     */   public final Type comparatorType;
/*     */   public final Type arraysType;
/*     */   public final Type iterableType;
/*     */   public final Type iteratorType;
/*     */   public final Type annotationTargetType;
/*     */   public final Type overrideType;
/*     */   public final Type retentionType;
/*     */   public final Type deprecatedType;
/*     */   public final Type suppressWarningsType;
/*     */   public final Type inheritedType;
/*     */   public final Type profileType;
/*     */   public final Type proprietaryType;
/*     */   public final Type systemType;
/*     */   public final Type autoCloseableType;
/*     */   public final Type trustMeType;
/*     */   public final Type lambdaMetafactory;
/*     */   public final Type repeatableType;
/*     */   public final Type documentedType;
/*     */   public final Type elementTypeType;
/*     */   public final Type functionalInterfaceType;
/*     */   public final Symbol.VarSymbol lengthVar;
/*     */   public final Symbol.OperatorSymbol nullcheck;
/*     */   public final Symbol.MethodSymbol enumFinalFinalize;
/*     */   public final Symbol.MethodSymbol autoCloseableClose;
/* 185 */   public final Type[] typeOfTag = new Type[TypeTag.getTypeTagCount()];
/*     */ 
/*     */ 
/*     */   
/* 189 */   public final Name[] boxedName = new Name[TypeTag.getTypeTagCount()];
/*     */ 
/*     */ 
/*     */   
/* 193 */   public final Set<Name> operatorNames = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public final Map<Name, Symbol.ClassSymbol> classes = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   public final Map<Name, Symbol.PackageSymbol> packages = new HashMap<>();
/*     */   
/*     */   public void initType(Type paramType, Symbol.ClassSymbol paramClassSymbol) {
/* 209 */     paramType.tsym = paramClassSymbol;
/* 210 */     this.typeOfTag[paramType.getTag().ordinal()] = paramType;
/*     */   }
/*     */   public final Symbol.ClassSymbol predefClass;
/*     */   public void initType(Type paramType, String paramString) {
/* 214 */     initType(paramType, new Symbol.ClassSymbol(1L, this.names
/*     */ 
/*     */           
/* 217 */           .fromString(paramString), paramType, this.rootPackage));
/*     */   }
/*     */   
/*     */   public void initType(Type paramType, String paramString1, String paramString2) {
/* 221 */     initType(paramType, paramString1);
/* 222 */     this.boxedName[paramType.getTag().ordinal()] = this.names.fromString("java.lang." + paramString2);
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
/*     */   private Symbol.VarSymbol enterConstant(String paramString, Type paramType) {
/* 236 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(25L, this.names.fromString(paramString), paramType, this.predefClass);
/*     */ 
/*     */     
/* 239 */     varSymbol.setData(paramType.constValue());
/* 240 */     this.predefClass.members().enter(varSymbol);
/* 241 */     return varSymbol;
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
/*     */   private void enterBinop(String paramString, Type paramType1, Type paramType2, Type paramType3, int paramInt) {
/* 254 */     this.predefClass.members().enter(new Symbol.OperatorSymbol(
/*     */           
/* 256 */           makeOperatorName(paramString), new Type.MethodType(
/* 257 */             List.of(paramType1, paramType2), paramType3, 
/* 258 */             List.nil(), this.methodClass), paramInt, this.predefClass));
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
/*     */   private void enterBinop(String paramString, Type paramType1, Type paramType2, Type paramType3, int paramInt1, int paramInt2) {
/* 272 */     enterBinop(paramString, paramType1, paramType2, paramType3, paramInt1 << 9 | paramInt2);
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
/*     */   private Symbol.OperatorSymbol enterUnop(String paramString, Type paramType1, Type paramType2, int paramInt) {
/* 290 */     Symbol.OperatorSymbol operatorSymbol = new Symbol.OperatorSymbol(makeOperatorName(paramString), new Type.MethodType(List.of(paramType1), paramType2, List.nil(), this.methodClass), paramInt, this.predefClass);
/*     */ 
/*     */ 
/*     */     
/* 294 */     this.predefClass.members().enter(operatorSymbol);
/* 295 */     return operatorSymbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Name makeOperatorName(String paramString) {
/* 303 */     Name name = this.names.fromString(paramString);
/* 304 */     this.operatorNames.add(name);
/* 305 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Type enterClass(String paramString) {
/* 312 */     return (this.reader.enterClass(this.names.fromString(paramString))).type;
/*     */   }
/*     */   
/*     */   public void synthesizeEmptyInterfaceIfMissing(Type paramType) {
/* 316 */     final Symbol.Completer completer = paramType.tsym.completer;
/* 317 */     if (completer != null) {
/* 318 */       paramType.tsym.completer = new Symbol.Completer() {
/*     */           public void complete(Symbol param1Symbol) throws Symbol.CompletionFailure {
/*     */             try {
/* 321 */               completer.complete(param1Symbol);
/* 322 */             } catch (CompletionFailure completionFailure) {
/* 323 */               param1Symbol.flags_field |= 0x201L;
/* 324 */               ((Type.ClassType)param1Symbol.type).supertype_field = Symtab.this.objectType;
/*     */             } 
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   public void synthesizeBoxTypeIfMissing(final Type type) {
/* 332 */     Symbol.ClassSymbol classSymbol = this.reader.enterClass(this.boxedName[type.getTag().ordinal()]);
/* 333 */     final Symbol.Completer completer = classSymbol.completer;
/* 334 */     if (completer != null) {
/* 335 */       classSymbol.completer = new Symbol.Completer() {
/*     */           public void complete(Symbol param1Symbol) throws Symbol.CompletionFailure {
/*     */             try {
/* 338 */               completer.complete(param1Symbol);
/* 339 */             } catch (CompletionFailure completionFailure) {
/* 340 */               param1Symbol.flags_field |= 0x1L;
/* 341 */               ((Type.ClassType)param1Symbol.type).supertype_field = Symtab.this.objectType;
/* 342 */               Name name = Symtab.this.target.boxWithConstructors() ? Symtab.this.names.init : Symtab.this.names.valueOf;
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 347 */               Symbol.MethodSymbol methodSymbol1 = new Symbol.MethodSymbol(9L, name, new Type.MethodType(List.of(type), param1Symbol.type, List.nil(), Symtab.this.methodClass), param1Symbol);
/*     */               
/* 349 */               param1Symbol.members().enter(methodSymbol1);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 354 */               Symbol.MethodSymbol methodSymbol2 = new Symbol.MethodSymbol(1L, type.tsym.name.append(Symtab.this.names.Value), new Type.MethodType(List.nil(), type, List.nil(), Symtab.this.methodClass), param1Symbol);
/*     */               
/* 356 */               param1Symbol.members().enter(methodSymbol2);
/*     */             } 
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Type enterSyntheticAnnotation(String paramString) {
/* 367 */     Type.ClassType classType = (Type.ClassType)enterClass(paramString);
/* 368 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)classType.tsym;
/* 369 */     classSymbol.completer = null;
/* 370 */     classSymbol.flags_field = 1073750529L;
/* 371 */     classSymbol.erasure_field = classType;
/* 372 */     classSymbol.members_field = new Scope(classSymbol);
/* 373 */     classType.typarams_field = List.nil();
/* 374 */     classType.allparams_field = List.nil();
/* 375 */     classType.supertype_field = this.annotationType;
/* 376 */     classType.interfaces_field = List.nil();
/* 377 */     return classType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Symtab(Context paramContext) throws Symbol.CompletionFailure {
/* 384 */     paramContext.put(symtabKey, this);
/*     */     
/* 386 */     this.names = Names.instance(paramContext);
/* 387 */     this.target = Target.instance(paramContext);
/*     */ 
/*     */     
/* 390 */     this.unknownType = new Type.UnknownType();
/*     */ 
/*     */     
/* 393 */     this.rootPackage = new Symbol.PackageSymbol(this.names.empty, null);
/* 394 */     final JavacMessages messages = JavacMessages.instance(paramContext);
/* 395 */     this.unnamedPackage = new Symbol.PackageSymbol(this.names.empty, this.rootPackage) {
/*     */         public String toString() {
/* 397 */           return messages.getLocalizedString("compiler.misc.unnamed.package", new Object[0]);
/*     */         }
/*     */       };
/* 400 */     this.noSymbol = new Symbol.TypeSymbol(0, 0L, this.names.empty, Type.noType, this.rootPackage) {
/*     */         public <R, P> R accept(ElementVisitor<R, P> param1ElementVisitor, P param1P) {
/* 402 */           return param1ElementVisitor.visitUnknown(this, param1P);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 407 */     this.errSymbol = new Symbol.ClassSymbol(1073741833L, this.names.any, null, this.rootPackage);
/* 408 */     this.errType = new Type.ErrorType(this.errSymbol, Type.noType);
/*     */     
/* 410 */     this.unknownSymbol = new Symbol.ClassSymbol(1073741833L, this.names.fromString("<any?>"), null, this.rootPackage);
/* 411 */     this.unknownSymbol.members_field = new Scope.ErrorScope(this.unknownSymbol);
/* 412 */     this.unknownSymbol.type = this.unknownType;
/*     */ 
/*     */     
/* 415 */     initType(this.byteType, "byte", "Byte");
/* 416 */     initType(this.shortType, "short", "Short");
/* 417 */     initType(this.charType, "char", "Character");
/* 418 */     initType(this.intType, "int", "Integer");
/* 419 */     initType(this.longType, "long", "Long");
/* 420 */     initType(this.floatType, "float", "Float");
/* 421 */     initType(this.doubleType, "double", "Double");
/* 422 */     initType(this.booleanType, "boolean", "Boolean");
/* 423 */     initType(this.voidType, "void", "Void");
/* 424 */     initType(this.botType, "<nulltype>");
/* 425 */     initType(this.errType, this.errSymbol);
/* 426 */     initType(this.unknownType, this.unknownSymbol);
/*     */ 
/*     */     
/* 429 */     this.arrayClass = new Symbol.ClassSymbol(1073741825L, this.names.Array, this.noSymbol);
/*     */ 
/*     */     
/* 432 */     this.boundClass = new Symbol.ClassSymbol(1073741825L, this.names.Bound, this.noSymbol);
/* 433 */     this.boundClass.members_field = new Scope.ErrorScope(this.boundClass);
/*     */ 
/*     */     
/* 436 */     this.methodClass = new Symbol.ClassSymbol(1073741825L, this.names.Method, this.noSymbol);
/* 437 */     this.methodClass.members_field = new Scope.ErrorScope(this.boundClass);
/*     */ 
/*     */     
/* 440 */     this.predefClass = new Symbol.ClassSymbol(1073741825L, this.names.empty, this.rootPackage);
/* 441 */     Scope scope = new Scope(this.predefClass);
/* 442 */     this.predefClass.members_field = scope;
/*     */ 
/*     */     
/* 445 */     scope.enter(this.byteType.tsym);
/* 446 */     scope.enter(this.shortType.tsym);
/* 447 */     scope.enter(this.charType.tsym);
/* 448 */     scope.enter(this.intType.tsym);
/* 449 */     scope.enter(this.longType.tsym);
/* 450 */     scope.enter(this.floatType.tsym);
/* 451 */     scope.enter(this.doubleType.tsym);
/* 452 */     scope.enter(this.booleanType.tsym);
/* 453 */     scope.enter(this.errType.tsym);
/*     */ 
/*     */     
/* 456 */     scope.enter(this.errSymbol);
/*     */     
/* 458 */     this.classes.put(this.predefClass.fullname, this.predefClass);
/*     */     
/* 460 */     this.reader = ClassReader.instance(paramContext);
/* 461 */     this.reader.init(this);
/*     */ 
/*     */     
/* 464 */     this.objectType = enterClass("java.lang.Object");
/* 465 */     this.classType = enterClass("java.lang.Class");
/* 466 */     this.stringType = enterClass("java.lang.String");
/* 467 */     this.stringBufferType = enterClass("java.lang.StringBuffer");
/* 468 */     this.stringBuilderType = enterClass("java.lang.StringBuilder");
/* 469 */     this.cloneableType = enterClass("java.lang.Cloneable");
/* 470 */     this.throwableType = enterClass("java.lang.Throwable");
/* 471 */     this.serializableType = enterClass("java.io.Serializable");
/* 472 */     this.serializedLambdaType = enterClass("java.lang.invoke.SerializedLambda");
/* 473 */     this.methodHandleType = enterClass("java.lang.invoke.MethodHandle");
/* 474 */     this.methodHandleLookupType = enterClass("java.lang.invoke.MethodHandles$Lookup");
/* 475 */     this.methodTypeType = enterClass("java.lang.invoke.MethodType");
/* 476 */     this.errorType = enterClass("java.lang.Error");
/* 477 */     this.illegalArgumentExceptionType = enterClass("java.lang.IllegalArgumentException");
/* 478 */     this.interruptedExceptionType = enterClass("java.lang.InterruptedException");
/* 479 */     this.exceptionType = enterClass("java.lang.Exception");
/* 480 */     this.runtimeExceptionType = enterClass("java.lang.RuntimeException");
/* 481 */     this.classNotFoundExceptionType = enterClass("java.lang.ClassNotFoundException");
/* 482 */     this.noClassDefFoundErrorType = enterClass("java.lang.NoClassDefFoundError");
/* 483 */     this.noSuchFieldErrorType = enterClass("java.lang.NoSuchFieldError");
/* 484 */     this.assertionErrorType = enterClass("java.lang.AssertionError");
/* 485 */     this.cloneNotSupportedExceptionType = enterClass("java.lang.CloneNotSupportedException");
/* 486 */     this.annotationType = enterClass("java.lang.annotation.Annotation");
/* 487 */     this.classLoaderType = enterClass("java.lang.ClassLoader");
/* 488 */     this.enumSym = this.reader.enterClass(this.names.java_lang_Enum);
/* 489 */     this
/*     */ 
/*     */ 
/*     */       
/* 493 */       .enumFinalFinalize = new Symbol.MethodSymbol(137438953492L, this.names.finalize, new Type.MethodType(List.nil(), this.voidType, List.nil(), this.methodClass), this.enumSym);
/*     */     
/* 495 */     this.listType = enterClass("java.util.List");
/* 496 */     this.collectionsType = enterClass("java.util.Collections");
/* 497 */     this.comparableType = enterClass("java.lang.Comparable");
/* 498 */     this.comparatorType = enterClass("java.util.Comparator");
/* 499 */     this.arraysType = enterClass("java.util.Arrays");
/* 500 */     this
/*     */       
/* 502 */       .iterableType = this.target.hasIterable() ? enterClass("java.lang.Iterable") : enterClass("java.util.Collection");
/* 503 */     this.iteratorType = enterClass("java.util.Iterator");
/* 504 */     this.annotationTargetType = enterClass("java.lang.annotation.Target");
/* 505 */     this.overrideType = enterClass("java.lang.Override");
/* 506 */     this.retentionType = enterClass("java.lang.annotation.Retention");
/* 507 */     this.deprecatedType = enterClass("java.lang.Deprecated");
/* 508 */     this.suppressWarningsType = enterClass("java.lang.SuppressWarnings");
/* 509 */     this.inheritedType = enterClass("java.lang.annotation.Inherited");
/* 510 */     this.repeatableType = enterClass("java.lang.annotation.Repeatable");
/* 511 */     this.documentedType = enterClass("java.lang.annotation.Documented");
/* 512 */     this.elementTypeType = enterClass("java.lang.annotation.ElementType");
/* 513 */     this.systemType = enterClass("java.lang.System");
/* 514 */     this.autoCloseableType = enterClass("java.lang.AutoCloseable");
/* 515 */     this
/*     */ 
/*     */       
/* 518 */       .autoCloseableClose = new Symbol.MethodSymbol(1L, this.names.close, new Type.MethodType(List.nil(), this.voidType, List.of(this.exceptionType), this.methodClass), this.autoCloseableType.tsym);
/*     */     
/* 520 */     this.trustMeType = enterClass("java.lang.SafeVarargs");
/* 521 */     this.nativeHeaderType = enterClass("java.lang.annotation.Native");
/* 522 */     this.lambdaMetafactory = enterClass("java.lang.invoke.LambdaMetafactory");
/* 523 */     this.functionalInterfaceType = enterClass("java.lang.FunctionalInterface");
/*     */     
/* 525 */     synthesizeEmptyInterfaceIfMissing(this.autoCloseableType);
/* 526 */     synthesizeEmptyInterfaceIfMissing(this.cloneableType);
/* 527 */     synthesizeEmptyInterfaceIfMissing(this.serializableType);
/* 528 */     synthesizeEmptyInterfaceIfMissing(this.lambdaMetafactory);
/* 529 */     synthesizeEmptyInterfaceIfMissing(this.serializedLambdaType);
/* 530 */     synthesizeBoxTypeIfMissing(this.doubleType);
/* 531 */     synthesizeBoxTypeIfMissing(this.floatType);
/* 532 */     synthesizeBoxTypeIfMissing(this.voidType);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 537 */     this.proprietaryType = enterSyntheticAnnotation("sun.Proprietary+Annotation");
/*     */ 
/*     */ 
/*     */     
/* 541 */     this.profileType = enterSyntheticAnnotation("jdk.Profile+Annotation");
/* 542 */     Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(1025L, this.names.value, this.intType, this.profileType.tsym);
/* 543 */     this.profileType.tsym.members().enter(methodSymbol);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 548 */     Type.ClassType classType = (Type.ClassType)this.arrayClass.type;
/* 549 */     classType.supertype_field = this.objectType;
/* 550 */     classType.interfaces_field = List.of(this.cloneableType, this.serializableType);
/* 551 */     this.arrayClass.members_field = new Scope(this.arrayClass);
/* 552 */     this.lengthVar = new Symbol.VarSymbol(17L, this.names.length, this.intType, this.arrayClass);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 557 */     this.arrayClass.members().enter(this.lengthVar);
/* 558 */     this
/*     */ 
/*     */ 
/*     */       
/* 562 */       .arrayCloneMethod = new Symbol.MethodSymbol(1L, this.names.clone, new Type.MethodType(List.nil(), this.objectType, List.nil(), this.methodClass), this.arrayClass);
/*     */     
/* 564 */     this.arrayClass.members().enter(this.arrayCloneMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 570 */     enterUnop("+++", this.doubleType, this.doubleType, 0);
/* 571 */     enterUnop("+++", this.floatType, this.floatType, 0);
/* 572 */     enterUnop("+++", this.longType, this.longType, 0);
/* 573 */     enterUnop("+++", this.intType, this.intType, 0);
/*     */     
/* 575 */     enterUnop("---", this.doubleType, this.doubleType, 119);
/* 576 */     enterUnop("---", this.floatType, this.floatType, 118);
/* 577 */     enterUnop("---", this.longType, this.longType, 117);
/* 578 */     enterUnop("---", this.intType, this.intType, 116);
/*     */     
/* 580 */     enterUnop("~", this.longType, this.longType, 131);
/* 581 */     enterUnop("~", this.intType, this.intType, 130);
/*     */     
/* 583 */     enterUnop("++", this.doubleType, this.doubleType, 99);
/* 584 */     enterUnop("++", this.floatType, this.floatType, 98);
/* 585 */     enterUnop("++", this.longType, this.longType, 97);
/* 586 */     enterUnop("++", this.intType, this.intType, 96);
/* 587 */     enterUnop("++", this.charType, this.charType, 96);
/* 588 */     enterUnop("++", this.shortType, this.shortType, 96);
/* 589 */     enterUnop("++", this.byteType, this.byteType, 96);
/*     */     
/* 591 */     enterUnop("--", this.doubleType, this.doubleType, 103);
/* 592 */     enterUnop("--", this.floatType, this.floatType, 102);
/* 593 */     enterUnop("--", this.longType, this.longType, 101);
/* 594 */     enterUnop("--", this.intType, this.intType, 100);
/* 595 */     enterUnop("--", this.charType, this.charType, 100);
/* 596 */     enterUnop("--", this.shortType, this.shortType, 100);
/* 597 */     enterUnop("--", this.byteType, this.byteType, 100);
/*     */     
/* 599 */     enterUnop("!", this.booleanType, this.booleanType, 257);
/* 600 */     this.nullcheck = enterUnop("<*nullchk*>", this.objectType, this.objectType, 276);
/*     */ 
/*     */     
/* 603 */     enterBinop("+", this.stringType, this.objectType, this.stringType, 256);
/* 604 */     enterBinop("+", this.objectType, this.stringType, this.stringType, 256);
/* 605 */     enterBinop("+", this.stringType, this.stringType, this.stringType, 256);
/* 606 */     enterBinop("+", this.stringType, this.intType, this.stringType, 256);
/* 607 */     enterBinop("+", this.stringType, this.longType, this.stringType, 256);
/* 608 */     enterBinop("+", this.stringType, this.floatType, this.stringType, 256);
/* 609 */     enterBinop("+", this.stringType, this.doubleType, this.stringType, 256);
/* 610 */     enterBinop("+", this.stringType, this.booleanType, this.stringType, 256);
/* 611 */     enterBinop("+", this.stringType, this.botType, this.stringType, 256);
/* 612 */     enterBinop("+", this.intType, this.stringType, this.stringType, 256);
/* 613 */     enterBinop("+", this.longType, this.stringType, this.stringType, 256);
/* 614 */     enterBinop("+", this.floatType, this.stringType, this.stringType, 256);
/* 615 */     enterBinop("+", this.doubleType, this.stringType, this.stringType, 256);
/* 616 */     enterBinop("+", this.booleanType, this.stringType, this.stringType, 256);
/* 617 */     enterBinop("+", this.botType, this.stringType, this.stringType, 256);
/*     */ 
/*     */     
/* 620 */     enterBinop("+", this.botType, this.botType, this.botType, 277);
/* 621 */     enterBinop("+", this.botType, this.intType, this.botType, 277);
/* 622 */     enterBinop("+", this.botType, this.longType, this.botType, 277);
/* 623 */     enterBinop("+", this.botType, this.floatType, this.botType, 277);
/* 624 */     enterBinop("+", this.botType, this.doubleType, this.botType, 277);
/* 625 */     enterBinop("+", this.botType, this.booleanType, this.botType, 277);
/* 626 */     enterBinop("+", this.botType, this.objectType, this.botType, 277);
/* 627 */     enterBinop("+", this.intType, this.botType, this.botType, 277);
/* 628 */     enterBinop("+", this.longType, this.botType, this.botType, 277);
/* 629 */     enterBinop("+", this.floatType, this.botType, this.botType, 277);
/* 630 */     enterBinop("+", this.doubleType, this.botType, this.botType, 277);
/* 631 */     enterBinop("+", this.booleanType, this.botType, this.botType, 277);
/* 632 */     enterBinop("+", this.objectType, this.botType, this.botType, 277);
/*     */     
/* 634 */     enterBinop("+", this.doubleType, this.doubleType, this.doubleType, 99);
/* 635 */     enterBinop("+", this.floatType, this.floatType, this.floatType, 98);
/* 636 */     enterBinop("+", this.longType, this.longType, this.longType, 97);
/* 637 */     enterBinop("+", this.intType, this.intType, this.intType, 96);
/*     */     
/* 639 */     enterBinop("-", this.doubleType, this.doubleType, this.doubleType, 103);
/* 640 */     enterBinop("-", this.floatType, this.floatType, this.floatType, 102);
/* 641 */     enterBinop("-", this.longType, this.longType, this.longType, 101);
/* 642 */     enterBinop("-", this.intType, this.intType, this.intType, 100);
/*     */     
/* 644 */     enterBinop("*", this.doubleType, this.doubleType, this.doubleType, 107);
/* 645 */     enterBinop("*", this.floatType, this.floatType, this.floatType, 106);
/* 646 */     enterBinop("*", this.longType, this.longType, this.longType, 105);
/* 647 */     enterBinop("*", this.intType, this.intType, this.intType, 104);
/*     */     
/* 649 */     enterBinop("/", this.doubleType, this.doubleType, this.doubleType, 111);
/* 650 */     enterBinop("/", this.floatType, this.floatType, this.floatType, 110);
/* 651 */     enterBinop("/", this.longType, this.longType, this.longType, 109);
/* 652 */     enterBinop("/", this.intType, this.intType, this.intType, 108);
/*     */     
/* 654 */     enterBinop("%", this.doubleType, this.doubleType, this.doubleType, 115);
/* 655 */     enterBinop("%", this.floatType, this.floatType, this.floatType, 114);
/* 656 */     enterBinop("%", this.longType, this.longType, this.longType, 113);
/* 657 */     enterBinop("%", this.intType, this.intType, this.intType, 112);
/*     */     
/* 659 */     enterBinop("&", this.booleanType, this.booleanType, this.booleanType, 126);
/* 660 */     enterBinop("&", this.longType, this.longType, this.longType, 127);
/* 661 */     enterBinop("&", this.intType, this.intType, this.intType, 126);
/*     */     
/* 663 */     enterBinop("|", this.booleanType, this.booleanType, this.booleanType, 128);
/* 664 */     enterBinop("|", this.longType, this.longType, this.longType, 129);
/* 665 */     enterBinop("|", this.intType, this.intType, this.intType, 128);
/*     */     
/* 667 */     enterBinop("^", this.booleanType, this.booleanType, this.booleanType, 130);
/* 668 */     enterBinop("^", this.longType, this.longType, this.longType, 131);
/* 669 */     enterBinop("^", this.intType, this.intType, this.intType, 130);
/*     */     
/* 671 */     enterBinop("<<", this.longType, this.longType, this.longType, 271);
/* 672 */     enterBinop("<<", this.intType, this.longType, this.intType, 270);
/* 673 */     enterBinop("<<", this.longType, this.intType, this.longType, 121);
/* 674 */     enterBinop("<<", this.intType, this.intType, this.intType, 120);
/*     */     
/* 676 */     enterBinop(">>", this.longType, this.longType, this.longType, 273);
/* 677 */     enterBinop(">>", this.intType, this.longType, this.intType, 272);
/* 678 */     enterBinop(">>", this.longType, this.intType, this.longType, 123);
/* 679 */     enterBinop(">>", this.intType, this.intType, this.intType, 122);
/*     */     
/* 681 */     enterBinop(">>>", this.longType, this.longType, this.longType, 275);
/* 682 */     enterBinop(">>>", this.intType, this.longType, this.intType, 274);
/* 683 */     enterBinop(">>>", this.longType, this.intType, this.longType, 125);
/* 684 */     enterBinop(">>>", this.intType, this.intType, this.intType, 124);
/*     */     
/* 686 */     enterBinop("<", this.doubleType, this.doubleType, this.booleanType, 152, 155);
/* 687 */     enterBinop("<", this.floatType, this.floatType, this.booleanType, 150, 155);
/* 688 */     enterBinop("<", this.longType, this.longType, this.booleanType, 148, 155);
/* 689 */     enterBinop("<", this.intType, this.intType, this.booleanType, 161);
/*     */     
/* 691 */     enterBinop(">", this.doubleType, this.doubleType, this.booleanType, 151, 157);
/* 692 */     enterBinop(">", this.floatType, this.floatType, this.booleanType, 149, 157);
/* 693 */     enterBinop(">", this.longType, this.longType, this.booleanType, 148, 157);
/* 694 */     enterBinop(">", this.intType, this.intType, this.booleanType, 163);
/*     */     
/* 696 */     enterBinop("<=", this.doubleType, this.doubleType, this.booleanType, 152, 158);
/* 697 */     enterBinop("<=", this.floatType, this.floatType, this.booleanType, 150, 158);
/* 698 */     enterBinop("<=", this.longType, this.longType, this.booleanType, 148, 158);
/* 699 */     enterBinop("<=", this.intType, this.intType, this.booleanType, 164);
/*     */     
/* 701 */     enterBinop(">=", this.doubleType, this.doubleType, this.booleanType, 151, 156);
/* 702 */     enterBinop(">=", this.floatType, this.floatType, this.booleanType, 149, 156);
/* 703 */     enterBinop(">=", this.longType, this.longType, this.booleanType, 148, 156);
/* 704 */     enterBinop(">=", this.intType, this.intType, this.booleanType, 162);
/*     */     
/* 706 */     enterBinop("==", this.objectType, this.objectType, this.booleanType, 165);
/* 707 */     enterBinop("==", this.booleanType, this.booleanType, this.booleanType, 159);
/* 708 */     enterBinop("==", this.doubleType, this.doubleType, this.booleanType, 151, 153);
/* 709 */     enterBinop("==", this.floatType, this.floatType, this.booleanType, 149, 153);
/* 710 */     enterBinop("==", this.longType, this.longType, this.booleanType, 148, 153);
/* 711 */     enterBinop("==", this.intType, this.intType, this.booleanType, 159);
/*     */     
/* 713 */     enterBinop("!=", this.objectType, this.objectType, this.booleanType, 166);
/* 714 */     enterBinop("!=", this.booleanType, this.booleanType, this.booleanType, 160);
/* 715 */     enterBinop("!=", this.doubleType, this.doubleType, this.booleanType, 151, 154);
/* 716 */     enterBinop("!=", this.floatType, this.floatType, this.booleanType, 149, 154);
/* 717 */     enterBinop("!=", this.longType, this.longType, this.booleanType, 148, 154);
/* 718 */     enterBinop("!=", this.intType, this.intType, this.booleanType, 160);
/*     */     
/* 720 */     enterBinop("&&", this.booleanType, this.booleanType, this.booleanType, 258);
/* 721 */     enterBinop("||", this.booleanType, this.booleanType, this.booleanType, 259);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Symtab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */