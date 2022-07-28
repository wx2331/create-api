/*     */ package com.sun.tools.javac.util;
/*     */
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.code.Kinds;
/*     */ import com.sun.tools.javac.code.Printer;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.tools.Diagnostic;
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
/*     */ public class RichDiagnosticFormatter
/*     */   extends ForwardingDiagnosticFormatter<JCDiagnostic, AbstractDiagnosticFormatter>
/*     */ {
/*     */   final Symtab syms;
/*     */   final Types types;
/*     */   final JCDiagnostic.Factory diags;
/*     */   final JavacMessages messages;
/*     */   protected ClassNameSimplifier nameSimplifier;
/*     */   private RichPrinter printer;
/*     */   Map<WhereClauseKind, Map<Type, JCDiagnostic>> whereClauses;
/*     */   protected Types.UnaryVisitor<Void> typePreprocessor;
/*     */   protected Types.DefaultSymbolVisitor<Void, Void> symbolPreprocessor;
/*     */
/*     */   public static RichDiagnosticFormatter instance(Context paramContext) {
/*  87 */     RichDiagnosticFormatter richDiagnosticFormatter = paramContext.<RichDiagnosticFormatter>get(RichDiagnosticFormatter.class);
/*  88 */     if (richDiagnosticFormatter == null)
/*  89 */       richDiagnosticFormatter = new RichDiagnosticFormatter(paramContext);
/*  90 */     return richDiagnosticFormatter;
/*     */   }
/*     */
/*     */   protected RichDiagnosticFormatter(Context paramContext) {
/*  94 */     super((AbstractDiagnosticFormatter)Log.instance(paramContext).getDiagnosticFormatter());
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
/* 452 */     this.typePreprocessor = new Types.UnaryVisitor<Void>()
/*     */       {
/*     */         public Void visit(List<Type> param1List)
/*     */         {
/* 456 */           for (Type type : param1List)
/* 457 */             visit(type);
/* 458 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitForAll(Type.ForAll param1ForAll, Void param1Void) {
/* 463 */           visit(param1ForAll.tvars);
/* 464 */           visit(param1ForAll.qtype);
/* 465 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitMethodType(Type.MethodType param1MethodType, Void param1Void) {
/* 470 */           visit(param1MethodType.argtypes);
/* 471 */           visit(param1MethodType.restype);
/* 472 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitErrorType(Type.ErrorType param1ErrorType, Void param1Void) {
/* 477 */           Type type = param1ErrorType.getOriginalType();
/* 478 */           if (type != null)
/* 479 */             visit(type);
/* 480 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitArrayType(Type.ArrayType param1ArrayType, Void param1Void) {
/* 485 */           visit(param1ArrayType.elemtype);
/* 486 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitWildcardType(Type.WildcardType param1WildcardType, Void param1Void) {
/* 491 */           visit(param1WildcardType.type);
/* 492 */           return null;
/*     */         }
/*     */
/*     */         public Void visitType(Type param1Type, Void param1Void) {
/* 496 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitCapturedType(Type.CapturedType param1CapturedType, Void param1Void) {
/* 501 */           if (RichDiagnosticFormatter.this.indexOf((Type)param1CapturedType, WhereClauseKind.CAPTURED) == -1) {
/* 502 */             String str = (param1CapturedType.lower == RichDiagnosticFormatter.this.syms.botType) ? ".1" : "";
/* 503 */             JCDiagnostic jCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.captured" + str, new Object[] { param1CapturedType, param1CapturedType.bound, param1CapturedType.lower, param1CapturedType.wildcard });
/* 504 */             ((Map<Type.CapturedType, JCDiagnostic>)RichDiagnosticFormatter.this.whereClauses.get(WhereClauseKind.CAPTURED)).put(param1CapturedType, jCDiagnostic);
/* 505 */             visit((Type)param1CapturedType.wildcard);
/* 506 */             visit(param1CapturedType.lower);
/* 507 */             visit(param1CapturedType.bound);
/*     */           }
/* 509 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitClassType(Type.ClassType param1ClassType, Void param1Void) {
/* 514 */           if (param1ClassType.isCompound()) {
/* 515 */             if (RichDiagnosticFormatter.this.indexOf((Type)param1ClassType, WhereClauseKind.INTERSECTION) == -1) {
/* 516 */               Type type = RichDiagnosticFormatter.this.types.supertype((Type)param1ClassType);
/* 517 */               List<Type> list = RichDiagnosticFormatter.this.types.interfaces((Type)param1ClassType);
/* 518 */               JCDiagnostic jCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.intersection", new Object[] { param1ClassType, list.prepend(type) });
/* 519 */               ((Map<Type.ClassType, JCDiagnostic>)RichDiagnosticFormatter.this.whereClauses.get(WhereClauseKind.INTERSECTION)).put(param1ClassType, jCDiagnostic);
/* 520 */               visit(type);
/* 521 */               visit(list);
/*     */             }
/* 523 */           } else if (param1ClassType.tsym.name.isEmpty()) {
/*     */
/* 525 */             Type.ClassType classType = (Type.ClassType)param1ClassType.tsym.type;
/* 526 */             if (classType != null) {
/* 527 */               if (classType.interfaces_field != null && classType.interfaces_field.nonEmpty()) {
/* 528 */                 visit((Type)classType.interfaces_field.head);
/*     */               } else {
/* 530 */                 visit(classType.supertype_field);
/*     */               }
/*     */             }
/*     */           }
/* 534 */           RichDiagnosticFormatter.this.nameSimplifier.addUsage((Symbol)param1ClassType.tsym);
/* 535 */           visit(param1ClassType.getTypeArguments());
/* 536 */           if (param1ClassType.getEnclosingType() != Type.noType)
/* 537 */             visit(param1ClassType.getEnclosingType());
/* 538 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitTypeVar(Type.TypeVar param1TypeVar, Void param1Void) {
/* 543 */           if (RichDiagnosticFormatter.this.indexOf((Type)param1TypeVar, WhereClauseKind.TYPEVAR) == -1) {
/*     */
/* 545 */             Type type = param1TypeVar.bound;
/* 546 */             while (type instanceof Type.ErrorType) {
/* 547 */               type = ((Type.ErrorType)type).getOriginalType();
/*     */             }
/*     */
/*     */
/*     */
/*     */
/* 553 */             List<Type> list = (type != null && (type.hasTag(TypeTag.CLASS) || type.hasTag(TypeTag.TYPEVAR))) ? RichDiagnosticFormatter.this.types.getBounds(param1TypeVar) : List.nil();
/*     */
/* 555 */             RichDiagnosticFormatter.this.nameSimplifier.addUsage((Symbol)param1TypeVar.tsym);
/*     */
/*     */
/*     */
/* 559 */             boolean bool = (list.head == null || ((Type)list.head).hasTag(TypeTag.NONE) || ((Type)list.head).hasTag(TypeTag.ERROR)) ? true : false;
/*     */
/* 561 */             if ((param1TypeVar.tsym.flags() & 0x1000L) == 0L) {
/*     */
/* 563 */               JCDiagnostic jCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.typevar" + (bool ? ".1" : ""), new Object[] { param1TypeVar, list,
/*     */
/* 565 */                     Kinds.kindName(param1TypeVar.tsym.location()), param1TypeVar.tsym.location() });
/* 566 */               ((Map<Type.TypeVar, JCDiagnostic>)RichDiagnosticFormatter.this.whereClauses.get(WhereClauseKind.TYPEVAR)).put(param1TypeVar, jCDiagnostic);
/* 567 */               RichDiagnosticFormatter.this.symbolPreprocessor.visit(param1TypeVar.tsym.location(), null);
/* 568 */               visit(list);
/*     */             } else {
/* 570 */               Assert.check(!bool);
/*     */
/* 572 */               JCDiagnostic jCDiagnostic = RichDiagnosticFormatter.this.diags.fragment("where.fresh.typevar", new Object[] { param1TypeVar, list });
/* 573 */               ((Map<Type.TypeVar, JCDiagnostic>)RichDiagnosticFormatter.this.whereClauses.get(WhereClauseKind.TYPEVAR)).put(param1TypeVar, jCDiagnostic);
/* 574 */               visit(list);
/*     */             }
/*     */           }
/*     */
/* 578 */           return null;
/*     */         }
/*     */       };
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
/* 592 */     this.symbolPreprocessor = new Types.DefaultSymbolVisitor<Void, Void>()
/*     */       {
/*     */
/*     */         public Void visitClassSymbol(Symbol.ClassSymbol param1ClassSymbol, Void param1Void)
/*     */         {
/* 597 */           if (param1ClassSymbol.type.isCompound()) {
/* 598 */             RichDiagnosticFormatter.this.typePreprocessor.visit(param1ClassSymbol.type);
/*     */           } else {
/* 600 */             RichDiagnosticFormatter.this.nameSimplifier.addUsage((Symbol)param1ClassSymbol);
/*     */           }
/* 602 */           return null;
/*     */         }
/*     */
/*     */
/*     */         public Void visitSymbol(Symbol param1Symbol, Void param1Void) {
/* 607 */           return null;
/*     */         }
/*     */
/*     */         public Void visitMethodSymbol(Symbol.MethodSymbol param1MethodSymbol, Void param1Void)
/*     */         {
/* 612 */           visit(param1MethodSymbol.owner, null);
/* 613 */           if (param1MethodSymbol.type != null)
/* 614 */             RichDiagnosticFormatter.this.typePreprocessor.visit(param1MethodSymbol.type);
/* 615 */           return null; }
/*     */       }; setRichPrinter(new RichPrinter()); this.syms = Symtab.instance(paramContext); this.diags = JCDiagnostic.Factory.instance(paramContext); this.types = Types.instance(paramContext); this.messages = JavacMessages.instance(paramContext); this.whereClauses = new EnumMap<>(WhereClauseKind.class); this.configuration = new RichConfiguration(Options.instance(paramContext), this.formatter); for (WhereClauseKind whereClauseKind : WhereClauseKind.values()) this.whereClauses.put(whereClauseKind, new LinkedHashMap<>());
/*     */   }
/*     */   public String format(JCDiagnostic paramJCDiagnostic, Locale paramLocale) { StringBuilder stringBuilder = new StringBuilder(); this.nameSimplifier = new ClassNameSimplifier(); for (WhereClauseKind whereClauseKind : WhereClauseKind.values()) ((Map)this.whereClauses.get(whereClauseKind)).clear();  preprocessDiagnostic(paramJCDiagnostic); stringBuilder.append(this.formatter.format(paramJCDiagnostic, paramLocale)); if (getConfiguration().isEnabled(RichConfiguration.RichFormatterFeature.WHERE_CLAUSES)) { List<JCDiagnostic> list = getWhereClauses(); String str = this.formatter.isRaw() ? "" : this.formatter.indentString(2); for (JCDiagnostic jCDiagnostic : list) { String str1 = this.formatter.format(jCDiagnostic, paramLocale); if (str1.length() > 0) stringBuilder.append('\n' + str + str1);  }  }  return stringBuilder.toString(); }
/*     */   public String formatMessage(JCDiagnostic paramJCDiagnostic, Locale paramLocale) { this.nameSimplifier = new ClassNameSimplifier(); preprocessDiagnostic(paramJCDiagnostic); return super.formatMessage(paramJCDiagnostic, paramLocale); }
/*     */   protected void setRichPrinter(RichPrinter paramRichPrinter) { this.printer = paramRichPrinter; this.formatter.setPrinter(paramRichPrinter); }
/*     */   protected RichPrinter getRichPrinter() { return this.printer; } protected void preprocessDiagnostic(JCDiagnostic paramJCDiagnostic) { for (Object object : paramJCDiagnostic.getArgs()) { if (object != null) preprocessArgument(object);  }  if (paramJCDiagnostic.isMultiline()) for (JCDiagnostic jCDiagnostic : paramJCDiagnostic.getSubdiagnostics()) preprocessDiagnostic(jCDiagnostic);   } protected void preprocessArgument(Object paramObject) { if (paramObject instanceof Type) { preprocessType((Type)paramObject); } else if (paramObject instanceof Symbol) { preprocessSymbol((Symbol)paramObject); } else if (paramObject instanceof JCDiagnostic) { preprocessDiagnostic((JCDiagnostic)paramObject); } else if (paramObject instanceof Iterable) { for (Object object : paramObject) preprocessArgument(object);  }  } protected List<JCDiagnostic> getWhereClauses() { List<?> list = List.nil(); for (WhereClauseKind whereClauseKind : WhereClauseKind.values()) { List<?> list1 = List.nil(); for (Map.Entry entry : ((Map)this.whereClauses.get(whereClauseKind)).entrySet()) list1 = list1.prepend(entry.getValue());  if (!list1.isEmpty()) { String str = whereClauseKind.key(); if (list1.size() > 1) str = str + ".1";  JCDiagnostic jCDiagnostic = this.diags.fragment(str, new Object[] { ((Map)this.whereClauses.get(whereClauseKind)).keySet() }); jCDiagnostic = new JCDiagnostic.MultilineDiagnostic(jCDiagnostic, (List)list1.reverse()); list = list.prepend(jCDiagnostic); }  }  return (List)list.reverse(); } private int indexOf(Type paramType, WhereClauseKind paramWhereClauseKind) { byte b = 1; for (Type type : ((Map)this.whereClauses.get(paramWhereClauseKind)).keySet()) { if (type.tsym == paramType.tsym) return b;  if (paramWhereClauseKind != WhereClauseKind.TYPEVAR || type.toString().equals(paramType.toString())) b++;  }  return -1; } private boolean unique(Type.TypeVar paramTypeVar) { byte b = 0; for (Type type : ((Map)this.whereClauses.get(WhereClauseKind.TYPEVAR)).keySet()) { if (type.toString().equals(paramTypeVar.toString())) b++;  }  if (b < 1) throw new AssertionError("Missing type variable in where clause " + paramTypeVar);  return (b == 1); } enum WhereClauseKind {
/*     */     TYPEVAR("where.description.typevar"), CAPTURED("where.description.captured"), INTERSECTION("where.description.intersection"); private final String key; WhereClauseKind(String param1String1) { this.key = param1String1; } String key() { return this.key; } } protected class ClassNameSimplifier {
/* 623 */     Map<Name, List<Symbol>> nameClashes = new HashMap<>(); protected void addUsage(Symbol param1Symbol) { Name name = param1Symbol.getSimpleName(); List<?> list = this.nameClashes.get(name); if (list == null) list = List.nil();  if (!list.contains(param1Symbol)) this.nameClashes.put(name, (List)list.append(param1Symbol));  } public String simplify(Symbol param1Symbol) { String str = param1Symbol.getQualifiedName().toString(); if (!param1Symbol.type.isCompound() && !param1Symbol.type.isPrimitive()) { List list = this.nameClashes.get(param1Symbol.getSimpleName()); if (list == null || (list.size() == 1 && list.contains(param1Symbol))) { List<?> list1 = List.nil(); Symbol symbol = param1Symbol; while (symbol.type.hasTag(TypeTag.CLASS) && symbol.type.getEnclosingType().hasTag(TypeTag.CLASS) && symbol.owner.kind == 2) { list1 = list1.prepend(symbol.getSimpleName()); symbol = symbol.owner; }  list1 = list1.prepend(symbol.getSimpleName()); StringBuilder stringBuilder = new StringBuilder(); String str1 = ""; for (Name name : list1) { stringBuilder.append(str1); stringBuilder.append(name); str1 = "."; }  str = stringBuilder.toString(); }  }  return str; } } public RichConfiguration getConfiguration() { return (RichConfiguration)this.configuration; }
/*     */   protected class RichPrinter extends Printer {
/*     */     public String localize(Locale param1Locale, String param1String, Object... param1VarArgs) { return RichDiagnosticFormatter.this.formatter.localize(param1Locale, param1String, param1VarArgs); }
/*     */     public String capturedVarId(Type.CapturedType param1CapturedType, Locale param1Locale) { return RichDiagnosticFormatter.this.indexOf((Type)param1CapturedType, WhereClauseKind.CAPTURED) + ""; }
/*     */     public String visitType(Type param1Type, Locale param1Locale) { String str = super.visitType(param1Type, param1Locale); if (param1Type == RichDiagnosticFormatter.this.syms.botType) str = localize(param1Locale, "compiler.misc.type.null", new Object[0]);  return str; }
/*     */     public String visitCapturedType(Type.CapturedType param1CapturedType, Locale param1Locale) { if (RichDiagnosticFormatter.this.getConfiguration().isEnabled(RichConfiguration.RichFormatterFeature.WHERE_CLAUSES)) return localize(param1Locale, "compiler.misc.captured.type", new Object[] { Integer.valueOf(RichDiagnosticFormatter.access$000(this.this$0, (Type)param1CapturedType, WhereClauseKind.CAPTURED)) });  return super.visitCapturedType(param1CapturedType, param1Locale); }
/*     */     public String visitClassType(Type.ClassType param1ClassType, Locale param1Locale) { if (param1ClassType.isCompound() && RichDiagnosticFormatter.this.getConfiguration().isEnabled(RichConfiguration.RichFormatterFeature.WHERE_CLAUSES)) return localize(param1Locale, "compiler.misc.intersection.type", new Object[] { Integer.valueOf(RichDiagnosticFormatter.access$000(this.this$0, (Type)param1ClassType, WhereClauseKind.INTERSECTION)) });  return super.visitClassType(param1ClassType, param1Locale); }
/*     */     protected String className(Type.ClassType param1ClassType, boolean param1Boolean, Locale param1Locale) { Symbol.TypeSymbol typeSymbol = param1ClassType.tsym; if (((Symbol)typeSymbol).name.length() == 0 || !RichDiagnosticFormatter.this.getConfiguration().isEnabled(RichConfiguration.RichFormatterFeature.SIMPLE_NAMES)) return super.className(param1ClassType, param1Boolean, param1Locale);  if (param1Boolean) return RichDiagnosticFormatter.this.nameSimplifier.simplify((Symbol)typeSymbol).toString();  return ((Symbol)typeSymbol).name.toString(); }
/*     */     public String visitTypeVar(Type.TypeVar param1TypeVar, Locale param1Locale) { if (RichDiagnosticFormatter.this.unique(param1TypeVar) || !RichDiagnosticFormatter.this.getConfiguration().isEnabled(RichConfiguration.RichFormatterFeature.UNIQUE_TYPEVAR_NAMES)) return param1TypeVar.toString();  return localize(param1Locale, "compiler.misc.type.var", new Object[] { param1TypeVar.toString(), Integer.valueOf(RichDiagnosticFormatter.access$000(this.this$0, (Type)param1TypeVar, WhereClauseKind.TYPEVAR)) }); }
/*     */     public String visitClassSymbol(Symbol.ClassSymbol param1ClassSymbol, Locale param1Locale) { if (param1ClassSymbol.type.isCompound()) return visit(param1ClassSymbol.type, param1Locale);  String str = RichDiagnosticFormatter.this.nameSimplifier.simplify((Symbol)param1ClassSymbol); if (str.length() == 0 || !RichDiagnosticFormatter.this.getConfiguration().isEnabled(RichConfiguration.RichFormatterFeature.SIMPLE_NAMES)) return super.visitClassSymbol(param1ClassSymbol, param1Locale);  return str; }
/*     */     public String visitMethodSymbol(Symbol.MethodSymbol param1MethodSymbol, Locale param1Locale) { String str1 = visit(param1MethodSymbol.owner, param1Locale); if (param1MethodSymbol.isStaticOrInstanceInit()) return str1;  String str2 = (param1MethodSymbol.name == param1MethodSymbol.name.table.names.init) ? str1 : param1MethodSymbol.name.toString(); if (param1MethodSymbol.type != null) { if (param1MethodSymbol.type.hasTag(TypeTag.FORALL))
/*     */           str2 = "<" + visitTypes(param1MethodSymbol.type.getTypeArguments(), param1Locale) + ">" + str2;  str2 = str2 + "(" + printMethodArgs(param1MethodSymbol.type.getParameterTypes(), ((param1MethodSymbol.flags() & 0x400000000L) != 0L), param1Locale) + ")"; }  return str2; }
/*     */   } protected void preprocessType(Type paramType) { this.typePreprocessor.visit(paramType); } protected void preprocessSymbol(Symbol paramSymbol) { this.symbolPreprocessor.visit(paramSymbol, null); } public static class RichConfiguration extends ForwardingConfiguration {
/* 636 */     public RichConfiguration(Options param1Options, AbstractDiagnosticFormatter param1AbstractDiagnosticFormatter) { super(param1AbstractDiagnosticFormatter.getConfiguration());
/* 637 */       this
/* 638 */         .features = param1AbstractDiagnosticFormatter.isRaw() ? EnumSet.<RichFormatterFeature>noneOf(RichFormatterFeature.class) : EnumSet.<RichFormatterFeature>of(RichFormatterFeature.SIMPLE_NAMES, RichFormatterFeature.WHERE_CLAUSES, RichFormatterFeature.UNIQUE_TYPEVAR_NAMES);
/*     */
/*     */
/* 641 */       String str = param1Options.get("diags");
/* 642 */       if (str != null) {
/* 643 */         for (String str1 : str.split(",")) {
/* 644 */           if (str1.equals("-where")) {
/* 645 */             this.features.remove(RichFormatterFeature.WHERE_CLAUSES);
/*     */           }
/* 647 */           else if (str1.equals("where")) {
/* 648 */             this.features.add(RichFormatterFeature.WHERE_CLAUSES);
/*     */           }
/* 650 */           if (str1.equals("-simpleNames")) {
/* 651 */             this.features.remove(RichFormatterFeature.SIMPLE_NAMES);
/*     */           }
/* 653 */           else if (str1.equals("simpleNames")) {
/* 654 */             this.features.add(RichFormatterFeature.SIMPLE_NAMES);
/*     */           }
/* 656 */           if (str1.equals("-disambiguateTvars")) {
/* 657 */             this.features.remove(RichFormatterFeature.UNIQUE_TYPEVAR_NAMES);
/*     */           }
/* 659 */           else if (str1.equals("disambiguateTvars")) {
/* 660 */             this.features.add(RichFormatterFeature.UNIQUE_TYPEVAR_NAMES);
/*     */           }
/*     */         }
/*     */       } }
/*     */
/*     */
/*     */
/*     */     protected EnumSet<RichFormatterFeature> features;
/*     */
/*     */
/*     */     public RichFormatterFeature[] getAvailableFeatures() {
/* 671 */       return RichFormatterFeature.values();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void enable(RichFormatterFeature param1RichFormatterFeature) {
/* 679 */       this.features.add(param1RichFormatterFeature);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void disable(RichFormatterFeature param1RichFormatterFeature) {
/* 687 */       this.features.remove(param1RichFormatterFeature);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isEnabled(RichFormatterFeature param1RichFormatterFeature) {
/* 695 */       return this.features.contains(param1RichFormatterFeature);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public enum RichFormatterFeature
/*     */     {
/* 703 */       WHERE_CLAUSES,
/*     */
/* 705 */       SIMPLE_NAMES,
/*     */
/* 707 */       UNIQUE_TYPEVAR_NAMES; } } public enum RichFormatterFeature { WHERE_CLAUSES, SIMPLE_NAMES, UNIQUE_TYPEVAR_NAMES; }
/*     */
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\RichDiagnosticFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
