/*     */ package com.sun.tools.javac.comp;
/*     */
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Kinds;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeMaker;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.util.Map;
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
/*     */ public class Annotate
/*     */ {
/*     */   final Attr attr;
/*     */   final TreeMaker make;
/*     */   final Log log;
/*     */   final Symtab syms;
/*     */   final Names names;
/*     */   final Resolve rs;
/*     */   final Types types;
/*     */   final ConstFold cfolder;
/*  52 */   protected static final Context.Key<Annotate> annotateKey = new Context.Key(); final Check chk; private int enterCount; ListBuffer<Worker> q; ListBuffer<Worker> typesQ; ListBuffer<Worker> repeatedQ; ListBuffer<Worker> afterRepeatedQ;
/*     */   ListBuffer<Worker> validateQ;
/*     */
/*     */   public static Annotate instance(Context paramContext) {
/*  56 */     Annotate annotate = (Annotate)paramContext.get(annotateKey);
/*  57 */     if (annotate == null)
/*  58 */       annotate = new Annotate(paramContext);
/*  59 */     return annotate;
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
/*     */
/*     */
/*     */   protected Annotate(Context paramContext)
/*     */   {
/*  89 */     this.enterCount = 0;
/*     */
/*  91 */     this.q = new ListBuffer();
/*  92 */     this.typesQ = new ListBuffer();
/*  93 */     this.repeatedQ = new ListBuffer();
/*  94 */     this.afterRepeatedQ = new ListBuffer();
/*  95 */     this.validateQ = new ListBuffer(); paramContext.put(annotateKey, this); this.attr = Attr.instance(paramContext); this.make = TreeMaker.instance(paramContext); this.log = Log.instance(paramContext); this.syms = Symtab.instance(paramContext); this.names = Names.instance(paramContext); this.rs = Resolve.instance(paramContext);
/*     */     this.types = Types.instance(paramContext);
/*     */     this.cfolder = ConstFold.instance(paramContext);
/*  98 */     this.chk = Check.instance(paramContext); } public void earlier(Worker paramWorker) { this.q.prepend(paramWorker); }
/*     */
/*     */
/*     */   public void normal(Worker paramWorker) {
/* 102 */     this.q.append(paramWorker);
/*     */   }
/*     */
/*     */   public void typeAnnotation(Worker paramWorker) {
/* 106 */     this.typesQ.append(paramWorker);
/*     */   }
/*     */
/*     */   public void repeated(Worker paramWorker) {
/* 110 */     this.repeatedQ.append(paramWorker);
/*     */   }
/*     */
/*     */   public void afterRepeated(Worker paramWorker) {
/* 114 */     this.afterRepeatedQ.append(paramWorker);
/*     */   }
/*     */
/*     */   public void validate(Worker paramWorker) {
/* 118 */     this.validateQ.append(paramWorker);
/*     */   }
/*     */
/*     */
/*     */   public void enterStart() {
/* 123 */     this.enterCount++;
/*     */   }
/*     */
/*     */
/*     */   public void enterDone() {
/* 128 */     this.enterCount--;
/* 129 */     flush();
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void enterDoneWithoutFlush() {
/* 135 */     this.enterCount--;
/*     */   }
/*     */
/*     */   public void flush() {
/* 139 */     if (this.enterCount != 0)
/* 140 */       return;  this.enterCount++;
/*     */     try {
/* 142 */       while (this.q.nonEmpty()) {
/* 143 */         ((Worker)this.q.next()).run();
/*     */       }
/* 145 */       while (this.typesQ.nonEmpty()) {
/* 146 */         ((Worker)this.typesQ.next()).run();
/*     */       }
/* 148 */       while (this.repeatedQ.nonEmpty()) {
/* 149 */         ((Worker)this.repeatedQ.next()).run();
/*     */       }
/* 151 */       while (this.afterRepeatedQ.nonEmpty()) {
/* 152 */         ((Worker)this.afterRepeatedQ.next()).run();
/*     */       }
/* 154 */       while (this.validateQ.nonEmpty()) {
/* 155 */         ((Worker)this.validateQ.next()).run();
/*     */       }
/*     */     } finally {
/* 158 */       this.enterCount--;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public class AnnotateRepeatedContext<T extends Attribute.Compound>
/*     */   {
/*     */     public final Env<AttrContext> env;
/*     */
/*     */
/*     */
/*     */     public final Map<Symbol.TypeSymbol, ListBuffer<T>> annotated;
/*     */
/*     */
/*     */
/*     */     public final Map<T, JCDiagnostic.DiagnosticPosition> pos;
/*     */
/*     */
/*     */
/*     */     public final Log log;
/*     */
/*     */
/*     */
/*     */     public final boolean isTypeCompound;
/*     */
/*     */
/*     */
/*     */
/*     */     public AnnotateRepeatedContext(Env<AttrContext> param1Env, Map<Symbol.TypeSymbol, ListBuffer<T>> param1Map, Map<T, JCDiagnostic.DiagnosticPosition> param1Map1, Log param1Log, boolean param1Boolean) {
/* 190 */       Assert.checkNonNull(param1Env);
/* 191 */       Assert.checkNonNull(param1Map);
/* 192 */       Assert.checkNonNull(param1Map1);
/* 193 */       Assert.checkNonNull(param1Log);
/*     */
/* 195 */       this.env = param1Env;
/* 196 */       this.annotated = param1Map;
/* 197 */       this.pos = param1Map1;
/* 198 */       this.log = param1Log;
/* 199 */       this.isTypeCompound = param1Boolean;
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
/*     */     public T processRepeatedAnnotations(List<T> param1List, Symbol param1Symbol) {
/* 211 */       return Annotate.this.processRepeatedAnnotations(param1List, this, param1Symbol);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void annotateRepeated(Worker param1Worker) {
/* 221 */       Annotate.this.repeated(param1Worker);
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
/*     */   Attribute.Compound enterAnnotation(JCTree.JCAnnotation paramJCAnnotation, Type paramType, Env<AttrContext> paramEnv) {
/* 236 */     return enterAnnotation(paramJCAnnotation, paramType, paramEnv, false);
/*     */   }
/*     */
/*     */
/*     */
/*     */   Attribute.TypeCompound enterTypeAnnotation(JCTree.JCAnnotation paramJCAnnotation, Type paramType, Env<AttrContext> paramEnv) {
/* 242 */     return (Attribute.TypeCompound)enterAnnotation(paramJCAnnotation, paramType, paramEnv, true);
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
/*     */   Attribute.Compound enterAnnotation(JCTree.JCAnnotation paramJCAnnotation, Type paramType, Env<AttrContext> paramEnv, boolean paramBoolean) {
/* 255 */     Type type = (paramJCAnnotation.annotationType.type != null) ? paramJCAnnotation.annotationType.type : this.attr.attribType(paramJCAnnotation.annotationType, paramEnv);
/* 256 */     paramJCAnnotation.type = this.chk.checkType(paramJCAnnotation.annotationType.pos(), type, paramType);
/* 257 */     if (paramJCAnnotation.type.isErroneous()) {
/*     */
/* 259 */       this.attr.postAttr((JCTree)paramJCAnnotation);
/*     */
/* 261 */       if (paramBoolean) {
/* 262 */         return (Attribute.Compound)new Attribute.TypeCompound(paramJCAnnotation.type, List.nil(), new TypeAnnotationPosition());
/*     */       }
/*     */
/* 265 */       return new Attribute.Compound(paramJCAnnotation.type, List.nil());
/*     */     }
/*     */
/* 268 */     if ((paramJCAnnotation.type.tsym.flags() & 0x2000L) == 0L) {
/* 269 */       this.log.error(paramJCAnnotation.annotationType.pos(), "not.annotation.type", new Object[] { paramJCAnnotation.type
/* 270 */             .toString() });
/*     */
/*     */
/* 273 */       this.attr.postAttr((JCTree)paramJCAnnotation);
/*     */
/* 275 */       if (paramBoolean) {
/* 276 */         return (Attribute.Compound)new Attribute.TypeCompound(paramJCAnnotation.type, List.nil(), null);
/*     */       }
/* 278 */       return new Attribute.Compound(paramJCAnnotation.type, List.nil());
/*     */     }
/*     */
/* 281 */     List list1 = paramJCAnnotation.args;
/* 282 */     if (list1.length() == 1 && !((JCTree.JCExpression)list1.head).hasTag(JCTree.Tag.ASSIGN))
/*     */     {
/* 284 */       list1
/* 285 */         .head = this.make.at(((JCTree.JCExpression)list1.head).pos).Assign((JCTree.JCExpression)this.make.Ident(this.names.value), (JCTree.JCExpression)list1.head);
/*     */     }
/* 287 */     ListBuffer listBuffer = new ListBuffer();
/*     */
/* 289 */     for (List list2 = list1; list2.nonEmpty(); list2 = list2.tail) {
/* 290 */       JCTree.JCExpression jCExpression = (JCTree.JCExpression)list2.head;
/* 291 */       if (!jCExpression.hasTag(JCTree.Tag.ASSIGN)) {
/* 292 */         this.log.error(jCExpression.pos(), "annotation.value.must.be.name.value", new Object[0]);
/*     */       } else {
/*     */
/* 295 */         JCTree.JCAssign jCAssign = (JCTree.JCAssign)jCExpression;
/* 296 */         if (!jCAssign.lhs.hasTag(JCTree.Tag.IDENT))
/* 297 */         { this.log.error(jCExpression.pos(), "annotation.value.must.be.name.value", new Object[0]); }
/*     */         else
/*     */
/* 300 */         { JCTree.JCIdent jCIdent = (JCTree.JCIdent)jCAssign.lhs;
/* 301 */           Symbol symbol = this.rs.resolveQualifiedMethod(jCAssign.rhs.pos(), paramEnv, paramJCAnnotation.type, jCIdent.name,
/*     */
/*     */
/*     */
/* 305 */               List.nil(), null);
/*     */
/* 307 */           jCIdent.sym = symbol;
/* 308 */           jCIdent.type = symbol.type;
/* 309 */           if (symbol.owner != paramJCAnnotation.type.tsym)
/* 310 */             this.log.error(jCIdent.pos(), "no.annotation.member", new Object[] { jCIdent.name, paramJCAnnotation.type });
/* 311 */           Type type1 = symbol.type.getReturnType();
/* 312 */           Attribute attribute = enterAttributeValue(type1, jCAssign.rhs, paramEnv);
/* 313 */           if (!symbol.type.isErroneous())
/* 314 */             listBuffer.append(new Pair(symbol, attribute));
/* 315 */           jCExpression.type = type1; }
/*     */       }
/* 317 */     }  if (paramBoolean) {
/* 318 */       if (paramJCAnnotation.attribute == null || !(paramJCAnnotation.attribute instanceof Attribute.TypeCompound)) {
/*     */
/* 320 */         Attribute.TypeCompound typeCompound = new Attribute.TypeCompound(paramJCAnnotation.type, listBuffer.toList(), new TypeAnnotationPosition());
/* 321 */         paramJCAnnotation.attribute = (Attribute.Compound)typeCompound;
/* 322 */         return (Attribute.Compound)typeCompound;
/*     */       }
/*     */
/* 325 */       return paramJCAnnotation.attribute;
/*     */     }
/*     */
/* 328 */     Attribute.Compound compound = new Attribute.Compound(paramJCAnnotation.type, listBuffer.toList());
/* 329 */     paramJCAnnotation.attribute = compound;
/* 330 */     return compound;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Attribute enterAttributeValue(Type paramType, JCTree.JCExpression paramJCExpression, Env<AttrContext> paramEnv) {
/*     */     JCTree.JCNewArray jCNewArray;
/*     */     try {
/* 341 */       paramType.tsym.complete();
/* 342 */     } catch (Symbol.CompletionFailure completionFailure) {
/* 343 */       this.log.error(paramJCExpression.pos(), "cant.resolve", new Object[] { Kinds.kindName(completionFailure.sym), completionFailure.sym });
/* 344 */       paramType = this.syms.errType;
/*     */     }
/* 346 */     if (paramType.hasTag(TypeTag.ARRAY)) {
/* 347 */       if (!paramJCExpression.hasTag(JCTree.Tag.NEWARRAY))
/*     */       {
/* 349 */         jCNewArray = this.make.at(paramJCExpression.pos).NewArray(null, List.nil(), List.of(paramJCExpression));
/*     */       }
/* 351 */       JCTree.JCNewArray jCNewArray1 = jCNewArray;
/* 352 */       if (jCNewArray1.elemtype != null) {
/* 353 */         this.log.error(jCNewArray1.elemtype.pos(), "new.not.allowed.in.annotation", new Object[0]);
/*     */       }
/* 355 */       ListBuffer listBuffer = new ListBuffer();
/* 356 */       for (List list = jCNewArray1.elems; list.nonEmpty(); list = list.tail) {
/* 357 */         listBuffer.append(enterAttributeValue(this.types.elemtype(paramType), (JCTree.JCExpression)list.head, paramEnv));
/*     */       }
/*     */
/*     */
/* 361 */       jCNewArray1.type = paramType;
/* 362 */       return (Attribute)new Attribute.Array(paramType, (Attribute[])listBuffer
/* 363 */           .toArray((Object[])new Attribute[listBuffer.length()]));
/*     */     }
/* 365 */     if (jCNewArray.hasTag(JCTree.Tag.NEWARRAY)) {
/* 366 */       if (!paramType.isErroneous())
/* 367 */         this.log.error(jCNewArray.pos(), "annotation.value.not.allowable.type", new Object[0]);
/* 368 */       JCTree.JCNewArray jCNewArray1 = jCNewArray;
/* 369 */       if (jCNewArray1.elemtype != null) {
/* 370 */         this.log.error(jCNewArray1.elemtype.pos(), "new.not.allowed.in.annotation", new Object[0]);
/*     */       }
/* 372 */       for (List list = jCNewArray1.elems; list.nonEmpty(); list = list.tail) {
/* 373 */         enterAttributeValue(this.syms.errType, (JCTree.JCExpression)list.head, paramEnv);
/*     */       }
/*     */
/*     */
/* 377 */       return (Attribute)new Attribute.Error(this.syms.errType);
/*     */     }
/* 379 */     if ((paramType.tsym.flags() & 0x2000L) != 0L) {
/* 380 */       if (jCNewArray.hasTag(JCTree.Tag.ANNOTATION)) {
/* 381 */         return (Attribute)enterAnnotation((JCTree.JCAnnotation)jCNewArray, paramType, paramEnv);
/*     */       }
/* 383 */       this.log.error(jCNewArray.pos(), "annotation.value.must.be.annotation", new Object[0]);
/* 384 */       paramType = this.syms.errType;
/*     */     }
/*     */
/* 387 */     if (jCNewArray.hasTag(JCTree.Tag.ANNOTATION)) {
/* 388 */       if (!paramType.isErroneous())
/* 389 */         this.log.error(jCNewArray.pos(), "annotation.not.valid.for.type", new Object[] { paramType });
/* 390 */       enterAnnotation((JCTree.JCAnnotation)jCNewArray, this.syms.errType, paramEnv);
/* 391 */       return (Attribute)new Attribute.Error(((JCTree.JCAnnotation)jCNewArray).annotationType.type);
/*     */     }
/* 393 */     if (paramType.isPrimitive() || this.types.isSameType(paramType, this.syms.stringType)) {
/* 394 */       Type type = this.attr.attribExpr((JCTree)jCNewArray, paramEnv, paramType);
/* 395 */       if (type.isErroneous())
/* 396 */         return (Attribute)new Attribute.Error(type.getOriginalType());
/* 397 */       if (type.constValue() == null) {
/* 398 */         this.log.error(jCNewArray.pos(), "attribute.value.must.be.constant", new Object[0]);
/* 399 */         return (Attribute)new Attribute.Error(paramType);
/*     */       }
/* 401 */       type = this.cfolder.coerce(type, paramType);
/* 402 */       return (Attribute)new Attribute.Constant(paramType, type.constValue());
/*     */     }
/* 404 */     if (paramType.tsym == this.syms.classType.tsym) {
/* 405 */       Type type = this.attr.attribExpr((JCTree)jCNewArray, paramEnv, paramType);
/* 406 */       if (type.isErroneous()) {
/*     */
/* 408 */         if (TreeInfo.name((JCTree)jCNewArray) == this.names._class && ((JCTree.JCFieldAccess)jCNewArray).selected.type
/* 409 */           .isErroneous()) {
/* 410 */           Name name = ((JCTree.JCFieldAccess)jCNewArray).selected.type.tsym.flatName();
/* 411 */           return (Attribute)new Attribute.UnresolvedClass(paramType, this.types
/* 412 */               .createErrorType(name, (Symbol.TypeSymbol)this.syms.unknownSymbol, this.syms.classType));
/*     */         }
/*     */
/* 415 */         return (Attribute)new Attribute.Error(type.getOriginalType());
/*     */       }
/*     */
/*     */
/*     */
/*     */
/* 421 */       if (TreeInfo.name((JCTree)jCNewArray) != this.names._class) {
/* 422 */         this.log.error(jCNewArray.pos(), "annotation.value.must.be.class.literal", new Object[0]);
/* 423 */         return (Attribute)new Attribute.Error(this.syms.errType);
/*     */       }
/* 425 */       return (Attribute)new Attribute.Class(this.types, ((JCTree.JCFieldAccess)jCNewArray).selected.type);
/*     */     }
/*     */
/* 428 */     if (paramType.hasTag(TypeTag.CLASS) && (paramType.tsym
/* 429 */       .flags() & 0x4000L) != 0L) {
/* 430 */       Type type = this.attr.attribExpr((JCTree)jCNewArray, paramEnv, paramType);
/* 431 */       Symbol symbol = TreeInfo.symbol((JCTree)jCNewArray);
/* 432 */       if (symbol == null ||
/* 433 */         TreeInfo.nonstaticSelect((JCTree)jCNewArray) || symbol.kind != 4 || (symbol
/*     */
/* 435 */         .flags() & 0x4000L) == 0L) {
/* 436 */         this.log.error(jCNewArray.pos(), "enum.annotation.must.be.enum.constant", new Object[0]);
/* 437 */         return (Attribute)new Attribute.Error(type.getOriginalType());
/*     */       }
/* 439 */       Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)symbol;
/* 440 */       return (Attribute)new Attribute.Enum(paramType, varSymbol);
/*     */     }
/*     */
/* 443 */     if (!paramType.isErroneous())
/* 444 */       this.log.error(jCNewArray.pos(), "annotation.value.not.allowable.type", new Object[0]);
/* 445 */     return (Attribute)new Attribute.Error(this.attr.attribExpr((JCTree)jCNewArray, paramEnv, paramType));
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
/*     */   private <T extends Attribute.Compound> T processRepeatedAnnotations(List<T> paramList, AnnotateRepeatedContext<T> paramAnnotateRepeatedContext, Symbol paramSymbol) {
/* 459 */     Attribute.Compound compound = (Attribute.Compound)paramList.head;
/* 460 */     List list = List.nil();
/* 461 */     Type type1 = null;
/* 462 */     Type.ArrayType arrayType = null;
/* 463 */     Type type2 = null;
/* 464 */     Symbol.MethodSymbol methodSymbol = null;
/*     */
/* 466 */     Assert.check((!paramList.isEmpty() &&
/* 467 */         !paramList.tail.isEmpty()));
/*     */
/* 469 */     byte b = 0;
/* 470 */     List<T> list1 = paramList;
/* 471 */     for (; !list1.isEmpty();
/* 472 */       list1 = list1.tail) {
/*     */
/* 474 */       b++;
/*     */
/*     */
/* 477 */       Assert.check((b > 1 || !list1.tail.isEmpty()));
/*     */
/* 479 */       Attribute.Compound compound1 = (Attribute.Compound)list1.head;
/*     */
/* 481 */       type1 = compound1.type;
/* 482 */       if (arrayType == null) {
/* 483 */         arrayType = this.types.makeArrayType(type1);
/*     */       }
/*     */
/*     */
/* 487 */       boolean bool = (b > 1) ? true : false;
/* 488 */       Type type = getContainingType(compound1, paramAnnotateRepeatedContext.pos.get(compound1), bool);
/* 489 */       if (type != null) {
/*     */
/*     */
/*     */
/*     */
/*     */
/* 495 */         Assert.check((type2 == null || type == type2));
/* 496 */         type2 = type;
/*     */
/* 498 */         methodSymbol = validateContainer(type2, type1, paramAnnotateRepeatedContext.pos.get(compound1));
/*     */
/* 500 */         if (methodSymbol != null)
/*     */         {
/*     */
/*     */
/*     */
/* 505 */           list = list.prepend(compound1); }
/*     */       }
/*     */     }
/* 508 */     if (!list.isEmpty()) {
/* 509 */       list = list.reverse();
/* 510 */       TreeMaker treeMaker = this.make.at(paramAnnotateRepeatedContext.pos.get(compound));
/* 511 */       Pair pair = new Pair(methodSymbol, new Attribute.Array((Type)arrayType, list));
/*     */
/*     */
/* 514 */       if (paramAnnotateRepeatedContext.isTypeCompound) {
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 523 */         Attribute.TypeCompound typeCompound = new Attribute.TypeCompound(type2, List.of(pair), ((Attribute.TypeCompound)paramList.head).position);
/*     */
/*     */
/*     */
/*     */
/* 528 */         typeCompound.setSynthesized(true);
/*     */
/*     */
/* 531 */         return (T)typeCompound;
/*     */       }
/*     */
/* 534 */       Attribute.Compound compound1 = new Attribute.Compound(type2, List.of(pair));
/* 535 */       JCTree.JCAnnotation jCAnnotation = treeMaker.Annotation((Attribute)compound1);
/*     */
/* 537 */       if (!this.chk.annotationApplicable(jCAnnotation, paramSymbol)) {
/* 538 */         this.log.error(jCAnnotation.pos(), "invalid.repeatable.annotation.incompatible.target", new Object[] { type2, type1 });
/*     */       }
/* 540 */       if (!this.chk.validateAnnotationDeferErrors(jCAnnotation)) {
/* 541 */         this.log.error(jCAnnotation.pos(), "duplicate.annotation.invalid.repeated", new Object[] { type1 });
/*     */       }
/* 543 */       compound1 = enterAnnotation(jCAnnotation, type2, paramAnnotateRepeatedContext.env);
/* 544 */       compound1.setSynthesized(true);
/*     */
/*     */
/* 547 */       return (T)compound1;
/*     */     }
/*     */
/*     */
/* 551 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Type getContainingType(Attribute.Compound paramCompound, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, boolean paramBoolean) {
/* 560 */     Type type = paramCompound.type;
/* 561 */     Symbol.TypeSymbol typeSymbol = type.tsym;
/*     */
/*     */
/*     */
/* 565 */     Attribute.Compound compound = typeSymbol.attribute((Symbol)this.syms.repeatableType.tsym);
/* 566 */     if (compound == null) {
/* 567 */       if (paramBoolean)
/* 568 */         this.log.error(paramDiagnosticPosition, "duplicate.annotation.missing.container", new Object[] { type, this.syms.repeatableType });
/* 569 */       return null;
/*     */     }
/*     */
/* 572 */     return filterSame(extractContainingType(compound, paramDiagnosticPosition, typeSymbol), type);
/*     */   }
/*     */
/*     */
/*     */
/*     */   private Type filterSame(Type paramType1, Type paramType2) {
/* 578 */     if (paramType1 == null || paramType2 == null) {
/* 579 */       return paramType1;
/*     */     }
/*     */
/* 582 */     return this.types.isSameType(paramType1, paramType2) ? null : paramType1;
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
/*     */   private Type extractContainingType(Attribute.Compound paramCompound, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol) {
/* 595 */     if (paramCompound.values.isEmpty()) {
/* 596 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation", new Object[] { paramTypeSymbol });
/* 597 */       return null;
/*     */     }
/* 599 */     Pair pair = (Pair)paramCompound.values.head;
/* 600 */     Name name = ((Symbol.MethodSymbol)pair.fst).name;
/* 601 */     if (name != this.names.value) {
/* 602 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation", new Object[] { paramTypeSymbol });
/* 603 */       return null;
/*     */     }
/* 605 */     if (!(pair.snd instanceof Attribute.Class)) {
/* 606 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation", new Object[] { paramTypeSymbol });
/* 607 */       return null;
/*     */     }
/*     */
/* 610 */     return ((Attribute.Class)pair.snd).getValue();
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
/*     */   private Symbol.MethodSymbol validateContainer(Type paramType1, Type paramType2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 622 */     Symbol.MethodSymbol methodSymbol = null;
/* 623 */     boolean bool1 = false;
/*     */
/*     */
/* 626 */     Scope scope = paramType1.tsym.members();
/* 627 */     byte b = 0;
/* 628 */     boolean bool2 = false;
/* 629 */     for (Symbol symbol : scope.getElementsByName(this.names.value)) {
/* 630 */       b++;
/*     */
/* 632 */       if (b == 1 && symbol.kind == 16) {
/*     */
/* 634 */         methodSymbol = (Symbol.MethodSymbol)symbol; continue;
/*     */       }
/* 636 */       bool2 = true;
/*     */     }
/*     */
/* 639 */     if (bool2) {
/* 640 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.multiple.values", new Object[] { paramType1,
/*     */
/*     */
/* 643 */             Integer.valueOf(b) });
/* 644 */       return null;
/* 645 */     }  if (b == 0) {
/* 646 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.no.value", new Object[] { paramType1 });
/*     */
/*     */
/* 649 */       return null;
/*     */     }
/*     */
/*     */
/*     */
/* 654 */     if (methodSymbol.kind != 16) {
/* 655 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.invalid.value", new Object[] { paramType1 });
/*     */
/*     */
/* 658 */       bool1 = true;
/*     */     }
/*     */
/*     */
/*     */
/* 663 */     Type type = methodSymbol.type.getReturnType();
/* 664 */     Type.ArrayType arrayType = this.types.makeArrayType(paramType2);
/* 665 */     if (!this.types.isArray(type) ||
/* 666 */       !this.types.isSameType((Type)arrayType, type)) {
/* 667 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.value.return", new Object[] { paramType1, type, arrayType });
/*     */
/*     */
/*     */
/*     */
/* 672 */       bool1 = true;
/*     */     }
/* 674 */     if (bool2) {
/* 675 */       bool1 = true;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/* 681 */     return bool1 ? null : methodSymbol;
/*     */   }
/*     */
/*     */   public static interface Worker {
/*     */     void run();
/*     */
/*     */     String toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Annotate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
