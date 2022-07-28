/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.source.tree.LambdaExpressionTree;
/*      */ import com.sun.source.tree.MemberReferenceTree;
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.jvm.Pool;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.DiagnosticSource;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.util.EnumMap;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.type.TypeKind;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class LambdaToMethod
/*      */   extends TreeTranslator
/*      */ {
/*      */   private Attr attr;
/*      */   private JCDiagnostic.Factory diags;
/*      */   private Log log;
/*      */   private Lower lower;
/*      */   private Names names;
/*      */   private Symtab syms;
/*      */   private Resolve rs;
/*      */   private TreeMaker make;
/*      */   private Types types;
/*      */   private TransTypes transTypes;
/*      */   private Env<AttrContext> attrEnv;
/*      */   private LambdaAnalyzerPreprocessor analyzer;
/*      */   private Map<JCTree, LambdaAnalyzerPreprocessor.TranslationContext<?>> contextMap;
/*      */   private LambdaAnalyzerPreprocessor.TranslationContext<?> context;
/*      */   private KlassInfo kInfo;
/*      */   private boolean dumpLambdaToMethodStats;
/*      */   private final boolean forceSerializable;
/*      */   public static final int FLAG_SERIALIZABLE = 1;
/*      */   public static final int FLAG_MARKERS = 2;
/*      */   public static final int FLAG_BRIDGES = 4;
/*  117 */   protected static final Context.Key<LambdaToMethod> unlambdaKey = new Context.Key();
/*      */
/*      */
/*      */   public static LambdaToMethod instance(Context paramContext) {
/*  121 */     LambdaToMethod lambdaToMethod = (LambdaToMethod)paramContext.get(unlambdaKey);
/*  122 */     if (lambdaToMethod == null) {
/*  123 */       lambdaToMethod = new LambdaToMethod(paramContext);
/*      */     }
/*  125 */     return lambdaToMethod;
/*      */   }
/*      */   private LambdaToMethod(Context paramContext) {
/*  128 */     paramContext.put(unlambdaKey, this);
/*  129 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  130 */     this.log = Log.instance(paramContext);
/*  131 */     this.lower = Lower.instance(paramContext);
/*  132 */     this.names = Names.instance(paramContext);
/*  133 */     this.syms = Symtab.instance(paramContext);
/*  134 */     this.rs = Resolve.instance(paramContext);
/*  135 */     this.make = TreeMaker.instance(paramContext);
/*  136 */     this.types = Types.instance(paramContext);
/*  137 */     this.transTypes = TransTypes.instance(paramContext);
/*  138 */     this.analyzer = new LambdaAnalyzerPreprocessor();
/*  139 */     Options options = Options.instance(paramContext);
/*  140 */     this.dumpLambdaToMethodStats = options.isSet("dumpLambdaToMethodStats");
/*  141 */     this.attr = Attr.instance(paramContext);
/*  142 */     this.forceSerializable = options.isSet("forceSerializable");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private class KlassInfo
/*      */   {
/*      */     private ListBuffer<JCTree> appendedMethodList;
/*      */
/*      */
/*      */
/*      */     private final Map<String, ListBuffer<JCTree.JCStatement>> deserializeCases;
/*      */
/*      */
/*      */
/*      */     private final Symbol.MethodSymbol deserMethodSym;
/*      */
/*      */
/*      */
/*      */     private final Symbol.VarSymbol deserParamSym;
/*      */
/*      */
/*      */
/*      */     private final JCTree.JCClassDecl clazz;
/*      */
/*      */
/*      */
/*      */     private KlassInfo(JCTree.JCClassDecl param1JCClassDecl) {
/*  171 */       this.clazz = param1JCClassDecl;
/*  172 */       this.appendedMethodList = new ListBuffer();
/*  173 */       this.deserializeCases = new HashMap<>();
/*      */
/*  175 */       Type.MethodType methodType = new Type.MethodType(List.of(LambdaToMethod.this.syms.serializedLambdaType), LambdaToMethod.this.syms.objectType, List.nil(), (Symbol.TypeSymbol)LambdaToMethod.this.syms.methodClass);
/*  176 */       this.deserMethodSym = LambdaToMethod.this.makePrivateSyntheticMethod(8L, LambdaToMethod.this.names.deserializeLambda, (Type)methodType, (Symbol)param1JCClassDecl.sym);
/*  177 */       this
/*  178 */         .deserParamSym = new Symbol.VarSymbol(16L, LambdaToMethod.this.names.fromString("lambda"), LambdaToMethod.this.syms.serializedLambdaType, (Symbol)this.deserMethodSym);
/*      */     }
/*      */
/*      */     private void addMethod(JCTree param1JCTree) {
/*  182 */       this.appendedMethodList = this.appendedMethodList.prepend(param1JCTree);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public <T extends JCTree> T translate(T paramT) {
/*  189 */     LambdaAnalyzerPreprocessor.TranslationContext<?> translationContext = this.contextMap.get(paramT);
/*  190 */     return translate(paramT, (translationContext != null) ? translationContext : this.context);
/*      */   }
/*      */
/*      */   <T extends JCTree> T translate(T paramT, LambdaAnalyzerPreprocessor.TranslationContext<?> paramTranslationContext) {
/*  194 */     LambdaAnalyzerPreprocessor.TranslationContext<?> translationContext = this.context;
/*      */     try {
/*  196 */       this.context = paramTranslationContext;
/*  197 */       return (T)super.translate((JCTree)paramT);
/*      */     } finally {
/*      */
/*  200 */       this.context = translationContext;
/*      */     }
/*      */   }
/*      */
/*      */   <T extends JCTree> List<T> translate(List<T> paramList, LambdaAnalyzerPreprocessor.TranslationContext<?> paramTranslationContext) {
/*  205 */     ListBuffer listBuffer = new ListBuffer();
/*  206 */     for (JCTree jCTree : paramList) {
/*  207 */       listBuffer.append(translate(jCTree, paramTranslationContext));
/*      */     }
/*  209 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */   public JCTree translateTopLevelClass(Env<AttrContext> paramEnv, JCTree paramJCTree, TreeMaker paramTreeMaker) {
/*  213 */     this.make = paramTreeMaker;
/*  214 */     this.attrEnv = paramEnv;
/*  215 */     this.context = null;
/*  216 */     this.contextMap = new HashMap<>();
/*  217 */     return translate(paramJCTree);
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
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/*  230 */     if (paramJCClassDecl.sym.owner.kind == 1)
/*      */     {
/*  232 */       paramJCClassDecl = this.analyzer.analyzeAndPreprocessClass(paramJCClassDecl);
/*      */     }
/*  234 */     KlassInfo klassInfo = this.kInfo;
/*      */     try {
/*  236 */       this.kInfo = new KlassInfo(paramJCClassDecl);
/*  237 */       super.visitClassDef(paramJCClassDecl);
/*  238 */       if (!this.kInfo.deserializeCases.isEmpty()) {
/*  239 */         int i = this.make.pos;
/*      */         try {
/*  241 */           this.make.at((JCDiagnostic.DiagnosticPosition)paramJCClassDecl);
/*  242 */           this.kInfo.addMethod((JCTree)makeDeserializeMethod((Symbol)paramJCClassDecl.sym));
/*      */         } finally {
/*  244 */           this.make.at(i);
/*      */         }
/*      */       }
/*      */
/*  248 */       List list = this.kInfo.appendedMethodList.toList();
/*  249 */       paramJCClassDecl.defs = paramJCClassDecl.defs.appendList(list);
/*  250 */       for (JCTree jCTree : list) {
/*  251 */         paramJCClassDecl.sym.members().enter((Symbol)((JCTree.JCMethodDecl)jCTree).sym);
/*      */       }
/*  253 */       this.result = (JCTree)paramJCClassDecl;
/*      */     } finally {
/*  255 */       this.kInfo = klassInfo;
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
/*      */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/*  267 */     LambdaAnalyzerPreprocessor.LambdaTranslationContext lambdaTranslationContext = (LambdaAnalyzerPreprocessor.LambdaTranslationContext)this.context;
/*  268 */     Symbol.MethodSymbol methodSymbol = lambdaTranslationContext.translatedSym;
/*  269 */     Type.MethodType methodType = (Type.MethodType)methodSymbol.type;
/*      */
/*      */
/*  272 */     Symbol symbol = lambdaTranslationContext.owner;
/*  273 */     ListBuffer listBuffer1 = new ListBuffer();
/*  274 */     ListBuffer listBuffer2 = new ListBuffer();
/*      */
/*  276 */     for (Attribute.TypeCompound typeCompound : symbol.getRawTypeAttributes()) {
/*  277 */       if (typeCompound.position.onLambda == paramJCLambda) {
/*  278 */         listBuffer2.append(typeCompound); continue;
/*      */       }
/*  280 */       listBuffer1.append(typeCompound);
/*      */     }
/*      */
/*  283 */     if (listBuffer2.nonEmpty()) {
/*  284 */       symbol.setTypeAttributes(listBuffer1.toList());
/*  285 */       methodSymbol.setTypeAttributes(listBuffer2.toList());
/*      */     }
/*      */
/*      */
/*      */
/*  290 */     JCTree.JCMethodDecl jCMethodDecl = this.make.MethodDef(this.make.Modifiers(methodSymbol.flags_field), methodSymbol.name, this.make
/*      */
/*  292 */         .QualIdent((Symbol)(methodType.getReturnType()).tsym),
/*  293 */         List.nil(), lambdaTranslationContext.syntheticParams,
/*      */
/*  295 */         (methodType.getThrownTypes() == null) ?
/*  296 */         List.nil() : this.make
/*  297 */         .Types(methodType.getThrownTypes()), null, null);
/*      */
/*      */
/*  300 */     jCMethodDecl.sym = methodSymbol;
/*  301 */     jCMethodDecl.type = (Type)methodType;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  308 */     jCMethodDecl.body = translate(makeLambdaBody(paramJCLambda, jCMethodDecl));
/*      */
/*      */
/*  311 */     this.kInfo.addMethod((JCTree)jCMethodDecl);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  324 */     listBuffer1 = new ListBuffer();
/*      */
/*  326 */     if (lambdaTranslationContext.methodReferenceReceiver != null) {
/*  327 */       listBuffer1.append(lambdaTranslationContext.methodReferenceReceiver);
/*  328 */     } else if (!methodSymbol.isStatic()) {
/*  329 */       listBuffer1.append(makeThis(methodSymbol.owner
/*  330 */             .enclClass().asType(), (Symbol)lambdaTranslationContext.owner
/*  331 */             .enclClass()));
/*      */     }
/*      */
/*      */
/*  335 */     for (Symbol symbol1 : lambdaTranslationContext.getSymbolMap(LambdaSymbolKind.CAPTURED_VAR).keySet()) {
/*  336 */       if (symbol1 != lambdaTranslationContext.self) {
/*  337 */         JCTree.JCExpression jCExpression = this.make.Ident(symbol1).setType(symbol1.type);
/*  338 */         listBuffer1.append(jCExpression);
/*      */       }
/*      */     }
/*      */
/*  342 */     for (Symbol symbol1 : lambdaTranslationContext.getSymbolMap(LambdaSymbolKind.CAPTURED_OUTER_THIS).keySet()) {
/*  343 */       JCTree.JCExpression jCExpression = this.make.QualThis(symbol1.type);
/*  344 */       listBuffer1.append(jCExpression);
/*      */     }
/*      */
/*      */
/*  348 */     List<JCTree> list = translate(listBuffer1.toList(), lambdaTranslationContext.prev);
/*      */
/*      */
/*  351 */     int i = referenceKind((Symbol)methodSymbol);
/*      */
/*      */
/*  354 */     this.result = (JCTree)makeMetafactoryIndyCall(this.context, i, (Symbol)methodSymbol, (List)list);
/*      */   }
/*      */
/*      */   private JCTree.JCIdent makeThis(Type paramType, Symbol paramSymbol) {
/*  358 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(8589938704L, this.names._this, paramType, paramSymbol);
/*      */
/*      */
/*      */
/*  362 */     return this.make.Ident((Symbol)varSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/*      */     JCTree.JCIdent jCIdent;
/*      */     JCTree.JCExpression jCExpression;
/*  372 */     LambdaAnalyzerPreprocessor.ReferenceTranslationContext referenceTranslationContext = (LambdaAnalyzerPreprocessor.ReferenceTranslationContext)this.context;
/*      */
/*      */
/*      */
/*  376 */     Symbol symbol = referenceTranslationContext.isSignaturePolymorphic() ? referenceTranslationContext.sigPolySym : paramJCMemberReference.sym;
/*      */
/*      */
/*      */
/*      */
/*      */
/*  382 */     switch (paramJCMemberReference.kind) {
/*      */
/*      */       case CAPTURED_THIS:
/*      */       case TYPE_VAR:
/*  386 */         jCIdent = makeThis(referenceTranslationContext.owner
/*  387 */             .enclClass().asType(), (Symbol)referenceTranslationContext.owner
/*  388 */             .enclClass());
/*      */         break;
/*      */
/*      */       case CAPTURED_VAR:
/*  392 */         jCExpression = paramJCMemberReference.getQualifierExpression();
/*  393 */         jCExpression = this.attr.makeNullCheck(jCExpression);
/*      */         break;
/*      */
/*      */       case CAPTURED_OUTER_THIS:
/*      */       case LOCAL_VAR:
/*      */       case PARAM:
/*      */       case null:
/*  400 */         jCExpression = null;
/*      */         break;
/*      */
/*      */       default:
/*  404 */         throw new InternalError("Should not have an invalid kind");
/*      */     }
/*      */
/*  407 */     List<JCTree.JCExpression> list = (jCExpression == null) ? List.nil() : translate(List.of(jCExpression), referenceTranslationContext.prev);
/*      */
/*      */
/*      */
/*  411 */     this.result = (JCTree)makeMetafactoryIndyCall(referenceTranslationContext, referenceTranslationContext.referenceKind(), symbol, list);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/*  420 */     if (this.context == null || !this.analyzer.lambdaIdentSymbolFilter(paramJCIdent.sym)) {
/*  421 */       super.visitIdent(paramJCIdent);
/*      */     } else {
/*  423 */       int i = this.make.pos;
/*      */       try {
/*  425 */         this.make.at((JCDiagnostic.DiagnosticPosition)paramJCIdent);
/*      */
/*  427 */         LambdaAnalyzerPreprocessor.LambdaTranslationContext lambdaTranslationContext = (LambdaAnalyzerPreprocessor.LambdaTranslationContext)this.context;
/*  428 */         JCTree jCTree = lambdaTranslationContext.translate(paramJCIdent);
/*  429 */         if (jCTree != null) {
/*  430 */           this.result = jCTree;
/*      */         }
/*      */         else {
/*      */
/*  434 */           super.visitIdent(paramJCIdent);
/*      */         }
/*      */       } finally {
/*  437 */         this.make.at(i);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/*  448 */     if (this.context == null || !this.analyzer.lambdaFieldAccessFilter(paramJCFieldAccess)) {
/*  449 */       super.visitSelect(paramJCFieldAccess);
/*      */     } else {
/*  451 */       int i = this.make.pos;
/*      */       try {
/*  453 */         this.make.at((JCDiagnostic.DiagnosticPosition)paramJCFieldAccess);
/*      */
/*  455 */         LambdaAnalyzerPreprocessor.LambdaTranslationContext lambdaTranslationContext = (LambdaAnalyzerPreprocessor.LambdaTranslationContext)this.context;
/*  456 */         JCTree jCTree = lambdaTranslationContext.translate(paramJCFieldAccess);
/*  457 */         if (jCTree != null) {
/*  458 */           this.result = jCTree;
/*      */         } else {
/*  460 */           super.visitSelect(paramJCFieldAccess);
/*      */         }
/*      */       } finally {
/*  463 */         this.make.at(i);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*  470 */     LambdaAnalyzerPreprocessor.LambdaTranslationContext lambdaTranslationContext = (LambdaAnalyzerPreprocessor.LambdaTranslationContext)this.context;
/*  471 */     if (this.context != null && lambdaTranslationContext.getSymbolMap(LambdaSymbolKind.LOCAL_VAR).containsKey(paramJCVariableDecl.sym)) {
/*  472 */       paramJCVariableDecl.init = translate(paramJCVariableDecl.init);
/*  473 */       paramJCVariableDecl.sym = (Symbol.VarSymbol)lambdaTranslationContext.getSymbolMap(LambdaSymbolKind.LOCAL_VAR).get(paramJCVariableDecl.sym);
/*  474 */       this.result = (JCTree)paramJCVariableDecl;
/*  475 */     } else if (this.context != null && lambdaTranslationContext.getSymbolMap(LambdaSymbolKind.TYPE_VAR).containsKey(paramJCVariableDecl.sym)) {
/*  476 */       JCTree.JCExpression jCExpression = translate(paramJCVariableDecl.init);
/*  477 */       Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)lambdaTranslationContext.getSymbolMap(LambdaSymbolKind.TYPE_VAR).get(paramJCVariableDecl.sym);
/*  478 */       int i = this.make.pos;
/*      */       try {
/*  480 */         this.result = (JCTree)this.make.at((JCDiagnostic.DiagnosticPosition)paramJCVariableDecl).VarDef(varSymbol, jCExpression);
/*      */       } finally {
/*  482 */         this.make.at(i);
/*      */       }
/*      */
/*  485 */       Scope scope = paramJCVariableDecl.sym.owner.members();
/*  486 */       if (scope != null) {
/*  487 */         scope.remove((Symbol)paramJCVariableDecl.sym);
/*  488 */         scope.enter((Symbol)varSymbol);
/*      */       }
/*      */     } else {
/*  491 */       super.visitVarDef(paramJCVariableDecl);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private JCTree.JCBlock makeLambdaBody(JCTree.JCLambda paramJCLambda, JCTree.JCMethodDecl paramJCMethodDecl) {
/*  500 */     return (paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION) ?
/*  501 */       makeLambdaExpressionBody((JCTree.JCExpression)paramJCLambda.body, paramJCMethodDecl) :
/*  502 */       makeLambdaStatementBody((JCTree.JCBlock)paramJCLambda.body, paramJCMethodDecl, paramJCLambda.canCompleteNormally);
/*      */   }
/*      */
/*      */   private JCTree.JCBlock makeLambdaExpressionBody(JCTree.JCExpression paramJCExpression, JCTree.JCMethodDecl paramJCMethodDecl) {
/*  506 */     Type type = paramJCMethodDecl.type.getReturnType();
/*  507 */     boolean bool1 = paramJCExpression.type.hasTag(TypeTag.VOID);
/*  508 */     boolean bool2 = type.hasTag(TypeTag.VOID);
/*  509 */     boolean bool3 = this.types.isSameType(type, (this.types.boxedClass((Type)this.syms.voidType)).type);
/*  510 */     int i = this.make.pos;
/*      */     try {
/*  512 */       if (bool2) {
/*      */
/*      */
/*  515 */         JCTree.JCExpressionStatement jCExpressionStatement = this.make.at((JCDiagnostic.DiagnosticPosition)paramJCExpression).Exec(paramJCExpression);
/*  516 */         return this.make.Block(0L, List.of(jCExpressionStatement));
/*  517 */       }  if (bool1 && bool3) {
/*      */
/*      */
/*  520 */         ListBuffer listBuffer = new ListBuffer();
/*  521 */         listBuffer.append(this.make.at((JCDiagnostic.DiagnosticPosition)paramJCExpression).Exec(paramJCExpression));
/*  522 */         listBuffer.append(this.make.Return((JCTree.JCExpression)this.make.Literal(TypeTag.BOT, null).setType(this.syms.botType)));
/*  523 */         return this.make.Block(0L, listBuffer.toList());
/*      */       }
/*      */
/*      */
/*  527 */       JCTree.JCExpression jCExpression = this.transTypes.coerce(this.attrEnv, paramJCExpression, type);
/*  528 */       return this.make.at((JCDiagnostic.DiagnosticPosition)jCExpression).Block(0L, List.of(this.make.Return(jCExpression)));
/*      */     } finally {
/*      */
/*  531 */       this.make.at(i);
/*      */     }
/*      */   }
/*      */
/*      */   private JCTree.JCBlock makeLambdaStatementBody(JCTree.JCBlock paramJCBlock, final JCTree.JCMethodDecl lambdaMethodDecl, boolean paramBoolean) {
/*  536 */     final Type restype = lambdaMethodDecl.type.getReturnType();
/*  537 */     final boolean isTarget_void = type.hasTag(TypeTag.VOID);
/*  538 */     boolean bool2 = this.types.isSameType(type, (this.types.boxedClass((Type)this.syms.voidType)).type);
/*      */
/*      */     class LambdaBodyTranslator
/*      */       extends TreeTranslator
/*      */     {
/*      */       public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl)
/*      */       {
/*  545 */         this.result = (JCTree)param1JCClassDecl;
/*      */       }
/*      */
/*      */
/*      */
/*      */       public void visitLambda(JCTree.JCLambda param1JCLambda) {
/*  551 */         this.result = (JCTree)param1JCLambda;
/*      */       }
/*      */
/*      */
/*      */       public void visitReturn(JCTree.JCReturn param1JCReturn) {
/*  556 */         boolean bool = (param1JCReturn.expr == null) ? true : false;
/*  557 */         if (isTarget_void && !bool) {
/*      */
/*      */
/*  560 */           Symbol.VarSymbol varSymbol = LambdaToMethod.this.makeSyntheticVar(0L, LambdaToMethod.this.names.fromString("$loc"), param1JCReturn.expr.type, (Symbol)lambdaMethodDecl.sym);
/*  561 */           JCTree.JCVariableDecl jCVariableDecl = LambdaToMethod.this.make.VarDef(varSymbol, param1JCReturn.expr);
/*  562 */           this.result = (JCTree)LambdaToMethod.this.make.Block(0L, List.of(jCVariableDecl, LambdaToMethod.this.make.Return(null)));
/*  563 */         } else if (!isTarget_void || !bool) {
/*      */
/*      */
/*  566 */           param1JCReturn.expr = LambdaToMethod.this.transTypes.coerce(LambdaToMethod.this.attrEnv, param1JCReturn.expr, restype);
/*  567 */           this.result = (JCTree)param1JCReturn;
/*      */         } else {
/*  569 */           this.result = (JCTree)param1JCReturn;
/*      */         }
/*      */       }
/*      */     };
/*      */
/*      */
/*  575 */     JCTree.JCBlock jCBlock = (JCTree.JCBlock)(new LambdaBodyTranslator()).translate((JCTree)paramJCBlock);
/*  576 */     if (paramBoolean && bool2)
/*      */     {
/*      */
/*  579 */       jCBlock.stats = jCBlock.stats.append(this.make.Return((JCTree.JCExpression)this.make.Literal(TypeTag.BOT, null).setType(this.syms.botType)));
/*      */     }
/*  581 */     return jCBlock;
/*      */   }
/*      */
/*      */   private JCTree.JCMethodDecl makeDeserializeMethod(Symbol paramSymbol) {
/*  585 */     ListBuffer listBuffer1 = new ListBuffer();
/*  586 */     ListBuffer listBuffer2 = new ListBuffer();
/*  587 */     for (Map.Entry entry : this.kInfo.deserializeCases.entrySet()) {
/*  588 */       JCTree.JCBreak jCBreak = this.make.Break(null);
/*  589 */       listBuffer2.add(jCBreak);
/*  590 */       List list = ((ListBuffer)entry.getValue()).append(jCBreak).toList();
/*  591 */       listBuffer1.add(this.make.Case((JCTree.JCExpression)this.make.Literal(entry.getKey()), list));
/*      */     }
/*  593 */     JCTree.JCSwitch jCSwitch = this.make.Switch(deserGetter("getImplMethodName", this.syms.stringType), listBuffer1.toList());
/*  594 */     for (JCTree.JCBreak jCBreak : listBuffer2) {
/*  595 */       jCBreak.target = (JCTree)jCSwitch;
/*      */     }
/*  597 */     JCTree.JCBlock jCBlock = this.make.Block(0L, List.of(jCSwitch, this.make
/*      */
/*  599 */           .Throw((JCTree.JCExpression)makeNewClass(this.syms.illegalArgumentExceptionType,
/*      */
/*  601 */               List.of(this.make.Literal("Invalid lambda deserialization"))))));
/*  602 */     JCTree.JCMethodDecl jCMethodDecl = this.make.MethodDef(this.make.Modifiers(this.kInfo.deserMethodSym.flags()), this.names.deserializeLambda, this.make
/*      */
/*  604 */         .QualIdent((Symbol)(this.kInfo.deserMethodSym.getReturnType()).tsym),
/*  605 */         List.nil(),
/*  606 */         List.of(this.make.VarDef(this.kInfo.deserParamSym, null)),
/*  607 */         List.nil(), jCBlock, null);
/*      */
/*      */
/*  610 */     jCMethodDecl.sym = this.kInfo.deserMethodSym;
/*  611 */     jCMethodDecl.type = this.kInfo.deserMethodSym.type;
/*      */
/*  613 */     return jCMethodDecl;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCNewClass makeNewClass(Type paramType, List<JCTree.JCExpression> paramList, Symbol paramSymbol) {
/*  622 */     JCTree.JCNewClass jCNewClass = this.make.NewClass(null, null, this.make
/*  623 */         .QualIdent((Symbol)paramType.tsym), paramList, null);
/*  624 */     jCNewClass.constructor = paramSymbol;
/*  625 */     jCNewClass.type = paramType;
/*  626 */     return jCNewClass;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree.JCNewClass makeNewClass(Type paramType, List<JCTree.JCExpression> paramList) {
/*  634 */     return makeNewClass(paramType, paramList, this.rs
/*  635 */         .resolveConstructor(null, this.attrEnv, paramType, TreeInfo.types(paramList), List.nil()));
/*      */   }
/*      */
/*      */
/*      */   private void addDeserializationCase(int paramInt, Symbol paramSymbol, Type paramType, Symbol.MethodSymbol paramMethodSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, List<Object> paramList, Type.MethodType paramMethodType) {
/*  640 */     String str1 = classSig(paramType);
/*  641 */     String str2 = paramMethodSymbol.getSimpleName().toString();
/*  642 */     String str3 = typeSig(this.types.erasure(paramMethodSymbol.type));
/*  643 */     String str4 = classSig(this.types.erasure(paramSymbol.owner.type));
/*  644 */     String str5 = paramSymbol.getQualifiedName().toString();
/*  645 */     String str6 = typeSig(this.types.erasure(paramSymbol.type));
/*      */
/*  647 */     JCTree.JCExpression jCExpression = eqTest((Type)this.syms.intType, deserGetter("getImplMethodKind", (Type)this.syms.intType), (JCTree.JCExpression)this.make.Literal(Integer.valueOf(paramInt)));
/*  648 */     ListBuffer listBuffer1 = new ListBuffer();
/*  649 */     byte b = 0;
/*  650 */     for (Type type : paramMethodType.getParameterTypes()) {
/*  651 */       List<JCTree.JCExpression> list = (new ListBuffer()).append(this.make.Literal(Integer.valueOf(b))).toList();
/*  652 */       List<Type> list1 = (new ListBuffer()).append(this.syms.intType).toList();
/*  653 */       listBuffer1.add(this.make.TypeCast(this.types.erasure(type), deserGetter("getCapturedArg", this.syms.objectType, list1, list)));
/*  654 */       b++;
/*      */     }
/*  656 */     JCTree.JCIf jCIf = this.make.If(
/*  657 */         deserTest(deserTest(deserTest(deserTest(deserTest(jCExpression, "getFunctionalInterfaceClass", str1), "getFunctionalInterfaceMethodName", str2), "getFunctionalInterfaceMethodSignature", str3), "getImplClass", str4), "getImplMethodSignature", str6), (JCTree.JCStatement)this.make
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  664 */         .Return(makeIndyCall(paramDiagnosticPosition, this.syms.lambdaMetafactory, this.names.altMetafactory, paramList, paramMethodType, listBuffer1
/*      */
/*      */
/*      */
/*  668 */             .toList(), paramMethodSymbol.name)), null);
/*      */
/*  670 */     ListBuffer listBuffer2 = (ListBuffer)this.kInfo.deserializeCases.get(str5);
/*  671 */     if (listBuffer2 == null) {
/*  672 */       listBuffer2 = new ListBuffer();
/*  673 */       this.kInfo.deserializeCases.put(str5, listBuffer2);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  685 */     listBuffer2.append(jCIf);
/*      */   }
/*      */
/*      */   private JCTree.JCExpression eqTest(Type paramType, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/*  689 */     JCTree.JCBinary jCBinary = this.make.Binary(JCTree.Tag.EQ, paramJCExpression1, paramJCExpression2);
/*  690 */     jCBinary.operator = this.rs.resolveBinaryOperator(null, JCTree.Tag.EQ, this.attrEnv, paramType, paramType);
/*  691 */     jCBinary.setType((Type)this.syms.booleanType);
/*  692 */     return (JCTree.JCExpression)jCBinary;
/*      */   }
/*      */
/*      */   private JCTree.JCExpression deserTest(JCTree.JCExpression paramJCExpression, String paramString1, String paramString2) {
/*  696 */     Type.MethodType methodType = new Type.MethodType(List.of(this.syms.objectType), (Type)this.syms.booleanType, List.nil(), (Symbol.TypeSymbol)this.syms.methodClass);
/*  697 */     Symbol symbol = this.rs.resolveQualifiedMethod(null, this.attrEnv, this.syms.objectType, this.names.equals, List.of(this.syms.objectType), List.nil());
/*  698 */     JCTree.JCMethodInvocation jCMethodInvocation = this.make.Apply(
/*  699 */         List.nil(), this.make
/*  700 */         .Select(deserGetter(paramString1, this.syms.stringType), symbol).setType((Type)methodType),
/*  701 */         List.of(this.make.Literal(paramString2)));
/*  702 */     jCMethodInvocation.setType((Type)this.syms.booleanType);
/*  703 */     JCTree.JCBinary jCBinary = this.make.Binary(JCTree.Tag.AND, paramJCExpression, (JCTree.JCExpression)jCMethodInvocation);
/*  704 */     jCBinary.operator = this.rs.resolveBinaryOperator(null, JCTree.Tag.AND, this.attrEnv, (Type)this.syms.booleanType, (Type)this.syms.booleanType);
/*  705 */     jCBinary.setType((Type)this.syms.booleanType);
/*  706 */     return (JCTree.JCExpression)jCBinary;
/*      */   }
/*      */
/*      */   private JCTree.JCExpression deserGetter(String paramString, Type paramType) {
/*  710 */     return deserGetter(paramString, paramType, List.nil(), List.nil());
/*      */   }
/*      */
/*      */   private JCTree.JCExpression deserGetter(String paramString, Type paramType, List<Type> paramList, List<JCTree.JCExpression> paramList1) {
/*  714 */     Type.MethodType methodType = new Type.MethodType(paramList, paramType, List.nil(), (Symbol.TypeSymbol)this.syms.methodClass);
/*  715 */     Symbol symbol = this.rs.resolveQualifiedMethod(null, this.attrEnv, this.syms.serializedLambdaType, this.names.fromString(paramString), paramList, List.nil());
/*  716 */     return (JCTree.JCExpression)this.make.Apply(
/*  717 */         List.nil(), this.make
/*  718 */         .Select(this.make.Ident((Symbol)this.kInfo.deserParamSym).setType(this.syms.serializedLambdaType), symbol).setType((Type)methodType), paramList1)
/*  719 */       .setType(paramType);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private Symbol.MethodSymbol makePrivateSyntheticMethod(long paramLong, Name paramName, Type paramType, Symbol paramSymbol) {
/*  726 */     return new Symbol.MethodSymbol(paramLong | 0x1000L | 0x2L, paramName, paramType, paramSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private Symbol.VarSymbol makeSyntheticVar(long paramLong, String paramString, Type paramType, Symbol paramSymbol) {
/*  733 */     return makeSyntheticVar(paramLong, this.names.fromString(paramString), paramType, paramSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private Symbol.VarSymbol makeSyntheticVar(long paramLong, Name paramName, Type paramType, Symbol paramSymbol) {
/*  740 */     return new Symbol.VarSymbol(paramLong | 0x1000L, paramName, paramType, paramSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void setVarargsIfNeeded(JCTree paramJCTree, Type paramType) {
/*  748 */     if (paramType != null) {
/*  749 */       switch (paramJCTree.getTag()) { case CAPTURED_THIS:
/*  750 */           ((JCTree.JCMethodInvocation)paramJCTree).varargsElement = paramType; return;
/*  751 */         case TYPE_VAR: ((JCTree.JCNewClass)paramJCTree).varargsElement = paramType; return; }
/*  752 */        throw new AssertionError();
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
/*      */   private List<JCTree.JCExpression> convertArgs(Symbol paramSymbol, List<JCTree.JCExpression> paramList, Type paramType) {
/*  764 */     Assert.check((paramSymbol.kind == 16));
/*  765 */     List<Type> list = this.types.erasure(paramSymbol.type).getParameterTypes();
/*  766 */     if (paramType != null) {
/*  767 */       Assert.check(((paramSymbol.flags() & 0x400000000L) != 0L));
/*      */     }
/*  769 */     return this.transTypes.translateArgs(paramList, list, paramType, this.attrEnv);
/*      */   }
/*      */
/*      */
/*      */
/*      */   private class MemberReferenceToLambda
/*      */   {
/*      */     private final JCTree.JCMemberReference tree;
/*      */
/*      */     private final LambdaAnalyzerPreprocessor.ReferenceTranslationContext localContext;
/*      */
/*      */     private final Symbol owner;
/*      */
/*  782 */     private final ListBuffer<JCTree.JCExpression> args = new ListBuffer();
/*  783 */     private final ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer();
/*      */
/*  785 */     private JCTree.JCExpression receiverExpression = null;
/*      */
/*      */     MemberReferenceToLambda(JCTree.JCMemberReference param1JCMemberReference, LambdaAnalyzerPreprocessor.ReferenceTranslationContext param1ReferenceTranslationContext, Symbol param1Symbol) {
/*  788 */       this.tree = param1JCMemberReference;
/*  789 */       this.localContext = param1ReferenceTranslationContext;
/*  790 */       this.owner = param1Symbol;
/*      */     }
/*      */
/*      */     JCTree.JCLambda lambda() {
/*  794 */       int i = LambdaToMethod.this.make.pos;
/*      */       try {
/*  796 */         LambdaToMethod.this.make.at((JCDiagnostic.DiagnosticPosition)this.tree);
/*      */
/*      */
/*      */
/*  800 */         Symbol.VarSymbol varSymbol = addParametersReturnReceiver();
/*      */
/*      */
/*  803 */         JCTree.JCExpression jCExpression = (this.tree.getMode() == MemberReferenceTree.ReferenceMode.INVOKE) ? expressionInvoke(varSymbol) : expressionNew();
/*      */
/*  805 */         JCTree.JCLambda jCLambda = LambdaToMethod.this.make.Lambda(this.params.toList(), (JCTree)jCExpression);
/*  806 */         jCLambda.targets = this.tree.targets;
/*  807 */         jCLambda.type = this.tree.type;
/*  808 */         jCLambda.pos = this.tree.pos;
/*  809 */         return jCLambda;
/*      */       } finally {
/*  811 */         LambdaToMethod.this.make.at(i);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     Symbol.VarSymbol addParametersReturnReceiver() {
/*      */       Symbol.VarSymbol varSymbol;
/*  821 */       Type type = this.localContext.bridgedRefSig();
/*  822 */       List list1 = type.getParameterTypes();
/*  823 */       List list2 = this.tree.getDescriptorType(LambdaToMethod.this.types).getParameterTypes();
/*      */
/*      */
/*      */
/*  827 */       switch (this.tree.kind) {
/*      */
/*      */         case CAPTURED_VAR:
/*  830 */           varSymbol = addParameter("rec$", (this.tree.getQualifierExpression()).type, false);
/*  831 */           this.receiverExpression = LambdaToMethod.this.attr.makeNullCheck(this.tree.getQualifierExpression());
/*      */           break;
/*      */
/*      */
/*      */         case CAPTURED_OUTER_THIS:
/*  836 */           varSymbol = addParameter("rec$", (Type)(type.getParameterTypes()).head, false);
/*  837 */           list1 = list1.tail;
/*  838 */           list2 = list2.tail;
/*      */           break;
/*      */         default:
/*  841 */           varSymbol = null;
/*      */           break;
/*      */       }
/*  844 */       List list3 = this.tree.sym.type.getParameterTypes();
/*  845 */       int i = list3.size();
/*  846 */       int j = list1.size();
/*      */
/*  848 */       int k = this.localContext.needsVarArgsConversion() ? (i - 1) : i;
/*      */
/*      */
/*  851 */       boolean bool = (this.tree.varargsElement != null || i == list2.size()) ? true : false;
/*      */
/*      */
/*      */
/*      */
/*      */       int m;
/*      */
/*      */
/*      */
/*  860 */       for (m = 0; list3.nonEmpty() && m < k; m++) {
/*      */
/*  862 */         Type type1 = (Type)list3.head;
/*      */
/*      */
/*      */
/*  866 */         if (bool && ((Type)list2.head).getKind() == TypeKind.TYPEVAR) {
/*  867 */           Type.TypeVar typeVar = (Type.TypeVar)list2.head;
/*  868 */           if (typeVar.bound.getKind() == TypeKind.INTERSECTION) {
/*  869 */             type1 = (Type)list1.head;
/*      */           }
/*      */         }
/*  872 */         addParameter("x$" + m, type1, true);
/*      */
/*      */
/*  875 */         list3 = list3.tail;
/*  876 */         list1 = list1.tail;
/*  877 */         list2 = list2.tail;
/*      */       }
/*      */
/*  880 */       for (m = k; m < j; m++) {
/*  881 */         addParameter("xva$" + m, this.tree.varargsElement, true);
/*      */       }
/*      */
/*  884 */       return varSymbol;
/*      */     }
/*      */
/*      */     JCTree.JCExpression getReceiverExpression() {
/*  888 */       return this.receiverExpression;
/*      */     }
/*      */     private JCTree.JCExpression makeReceiver(Symbol.VarSymbol param1VarSymbol) {
/*      */       JCTree.JCExpression jCExpression;
/*  892 */       if (param1VarSymbol == null) return null;
/*  893 */       JCTree.JCIdent jCIdent = LambdaToMethod.this.make.Ident((Symbol)param1VarSymbol);
/*  894 */       Type type = this.tree.ownerAccessible ? (this.tree.sym.enclClass()).type : this.tree.expr.type;
/*  895 */       if (type == LambdaToMethod.this.syms.arrayClass.type)
/*      */       {
/*  897 */         type = (this.tree.getQualifierExpression()).type;
/*      */       }
/*  899 */       if (!param1VarSymbol.type.tsym.isSubClass((Symbol)type.tsym, LambdaToMethod.this.types)) {
/*  900 */         jCExpression = LambdaToMethod.this.make.TypeCast((JCTree)LambdaToMethod.this.make.Type(type), (JCTree.JCExpression)jCIdent).setType(type);
/*      */       }
/*  902 */       return jCExpression;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private JCTree.JCExpression expressionInvoke(Symbol.VarSymbol param1VarSymbol) {
/*  913 */       JCTree.JCExpression jCExpression1 = (param1VarSymbol != null) ? makeReceiver(param1VarSymbol) : this.tree.getQualifierExpression();
/*      */
/*      */
/*  916 */       JCTree.JCFieldAccess jCFieldAccess = LambdaToMethod.this.make.Select(jCExpression1, this.tree.sym.name);
/*  917 */       jCFieldAccess.sym = this.tree.sym;
/*  918 */       jCFieldAccess.type = this.tree.sym.erasure(LambdaToMethod.this.types);
/*      */
/*      */
/*      */
/*      */
/*  923 */       JCTree.JCMethodInvocation jCMethodInvocation = LambdaToMethod.this.make.Apply(List.nil(), (JCTree.JCExpression)jCFieldAccess, LambdaToMethod.this.convertArgs(this.tree.sym, this.args.toList(), this.tree.varargsElement)).setType(this.tree.sym.erasure(LambdaToMethod.this.types).getReturnType());
/*      */
/*  925 */       JCTree.JCExpression jCExpression2 = LambdaToMethod.this.transTypes.coerce((JCTree.JCExpression)jCMethodInvocation, this.localContext.generatedRefSig().getReturnType());
/*  926 */       LambdaToMethod.this.setVarargsIfNeeded((JCTree)jCExpression2, this.tree.varargsElement);
/*  927 */       return jCExpression2;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     private JCTree.JCExpression expressionNew() {
/*  934 */       if (this.tree.kind == JCTree.JCMemberReference.ReferenceKind.ARRAY_CTOR) {
/*      */
/*  936 */         JCTree.JCNewArray jCNewArray = LambdaToMethod.this.make.NewArray(LambdaToMethod.this
/*  937 */             .make.Type(LambdaToMethod.this.types.elemtype((this.tree.getQualifierExpression()).type)),
/*  938 */             List.of(LambdaToMethod.this.make.Ident((JCTree.JCVariableDecl)this.params.first())), null);
/*      */
/*  940 */         jCNewArray.type = (this.tree.getQualifierExpression()).type;
/*  941 */         return (JCTree.JCExpression)jCNewArray;
/*      */       }
/*      */
/*      */
/*      */
/*  946 */       JCTree.JCNewClass jCNewClass = LambdaToMethod.this.make.NewClass(null,
/*  947 */           List.nil(), LambdaToMethod.this
/*  948 */           .make.Type((this.tree.getQualifierExpression()).type), LambdaToMethod.this
/*  949 */           .convertArgs(this.tree.sym, this.args.toList(), this.tree.varargsElement), null);
/*      */
/*  951 */       jCNewClass.constructor = this.tree.sym;
/*  952 */       jCNewClass.constructorType = this.tree.sym.erasure(LambdaToMethod.this.types);
/*  953 */       jCNewClass.type = (this.tree.getQualifierExpression()).type;
/*  954 */       LambdaToMethod.this.setVarargsIfNeeded((JCTree)jCNewClass, this.tree.varargsElement);
/*  955 */       return (JCTree.JCExpression)jCNewClass;
/*      */     }
/*      */
/*      */
/*      */     private Symbol.VarSymbol addParameter(String param1String, Type param1Type, boolean param1Boolean) {
/*  960 */       Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(8589938688L, LambdaToMethod.this.names.fromString(param1String), param1Type, this.owner);
/*  961 */       varSymbol.pos = this.tree.pos;
/*  962 */       this.params.append(LambdaToMethod.this.make.VarDef(varSymbol, null));
/*  963 */       if (param1Boolean) {
/*  964 */         this.args.append(LambdaToMethod.this.make.Ident((Symbol)varSymbol));
/*      */       }
/*  966 */       return varSymbol;
/*      */     }
/*      */   }
/*      */
/*      */   private Type.MethodType typeToMethodType(Type paramType) {
/*  971 */     Type type = this.types.erasure(paramType);
/*  972 */     return new Type.MethodType(type.getParameterTypes(), type
/*  973 */         .getReturnType(), type
/*  974 */         .getThrownTypes(), (Symbol.TypeSymbol)this.syms.methodClass);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private JCTree.JCExpression makeMetafactoryIndyCall(LambdaAnalyzerPreprocessor.TranslationContext<?> paramTranslationContext, int paramInt, Symbol paramSymbol, List<JCTree.JCExpression> paramList) {
/*  983 */     T t = paramTranslationContext.tree;
/*      */
/*  985 */     Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)this.types.findDescriptorSymbol(((JCTree.JCFunctionalExpression)t).type.tsym);
/*  986 */     List<Object> list = List.of(
/*  987 */         typeToMethodType(methodSymbol.type), new Pool.MethodHandle(paramInt, paramSymbol, this.types),
/*      */
/*  989 */         typeToMethodType(t.getDescriptorType(this.types)));
/*      */
/*      */
/*  992 */     ListBuffer listBuffer = new ListBuffer();
/*  993 */     for (JCTree.JCExpression jCExpression : paramList) {
/*  994 */       listBuffer.append(jCExpression.type);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 1000 */     Type.MethodType methodType = new Type.MethodType(listBuffer.toList(), ((JCTree.JCFunctionalExpression)t).type, List.nil(), (Symbol.TypeSymbol)this.syms.methodClass);
/*      */
/*      */
/* 1003 */     Name name = paramTranslationContext.needsAltMetafactory() ? this.names.altMetafactory : this.names.metafactory;
/*      */
/*      */
/* 1006 */     if (paramTranslationContext.needsAltMetafactory()) {
/* 1007 */       ListBuffer listBuffer1 = new ListBuffer();
/* 1008 */       for (Type type : ((JCTree.JCFunctionalExpression)t).targets.tail) {
/* 1009 */         if (type.tsym != this.syms.serializableType.tsym) {
/* 1010 */           listBuffer1.append(type.tsym);
/*      */         }
/*      */       }
/* 1013 */       int i = paramTranslationContext.isSerializable() ? 1 : 0;
/* 1014 */       boolean bool1 = listBuffer1.nonEmpty();
/* 1015 */       boolean bool2 = paramTranslationContext.bridges.nonEmpty();
/* 1016 */       if (bool1) {
/* 1017 */         i |= 0x2;
/*      */       }
/* 1019 */       if (bool2) {
/* 1020 */         i |= 0x4;
/*      */       }
/* 1022 */       list = list.append(Integer.valueOf(i));
/* 1023 */       if (bool1) {
/* 1024 */         list = list.append(Integer.valueOf(listBuffer1.length()));
/* 1025 */         list = list.appendList(listBuffer1.toList());
/*      */       }
/* 1027 */       if (bool2) {
/* 1028 */         list = list.append(Integer.valueOf(paramTranslationContext.bridges.length() - 1));
/* 1029 */         for (Symbol symbol : paramTranslationContext.bridges) {
/* 1030 */           Type type = symbol.erasure(this.types);
/* 1031 */           if (!this.types.isSameType(type, methodSymbol.erasure(this.types))) {
/* 1032 */             list = list.append(symbol.erasure(this.types));
/*      */           }
/*      */         }
/*      */       }
/* 1036 */       if (paramTranslationContext.isSerializable()) {
/* 1037 */         int j = this.make.pos;
/*      */         try {
/* 1039 */           this.make.at((JCDiagnostic.DiagnosticPosition)this.kInfo.clazz);
/* 1040 */           addDeserializationCase(paramInt, paramSymbol, ((JCTree.JCFunctionalExpression)t).type, methodSymbol, (JCDiagnostic.DiagnosticPosition)t, list, methodType);
/*      */         } finally {
/*      */
/* 1043 */           this.make.at(j);
/*      */         }
/*      */       }
/*      */     }
/*      */
/* 1048 */     return makeIndyCall((JCDiagnostic.DiagnosticPosition)t, this.syms.lambdaMetafactory, name, list, methodType, paramList, methodSymbol.name);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private JCTree.JCExpression makeIndyCall(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName1, List<Object> paramList, Type.MethodType paramMethodType, List<JCTree.JCExpression> paramList1, Name paramName2) {
/* 1058 */     int i = this.make.pos;
/*      */     try {
/* 1060 */       this.make.at(paramDiagnosticPosition);
/*      */
/*      */
/* 1063 */       List<Type> list = List.of(this.syms.methodHandleLookupType, this.syms.stringType, this.syms.methodTypeType).appendList(bsmStaticArgToTypes(paramList));
/*      */
/* 1065 */       Symbol.MethodSymbol methodSymbol = this.rs.resolveInternalMethod(paramDiagnosticPosition, this.attrEnv, paramType, paramName1, list,
/* 1066 */           List.nil());
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1076 */       Symbol.DynamicMethodSymbol dynamicMethodSymbol = new Symbol.DynamicMethodSymbol(paramName2, (Symbol)this.syms.noSymbol, methodSymbol.isStatic() ? 6 : 5, methodSymbol, (Type)paramMethodType, paramList.toArray());
/*      */
/* 1078 */       JCTree.JCFieldAccess jCFieldAccess = this.make.Select(this.make.QualIdent((Symbol)paramType.tsym), paramName1);
/* 1079 */       jCFieldAccess.sym = (Symbol)dynamicMethodSymbol;
/* 1080 */       jCFieldAccess.type = paramMethodType.getReturnType();
/*      */
/* 1082 */       JCTree.JCMethodInvocation jCMethodInvocation = this.make.Apply(List.nil(), (JCTree.JCExpression)jCFieldAccess, paramList1);
/* 1083 */       jCMethodInvocation.type = paramMethodType.getReturnType();
/* 1084 */       return (JCTree.JCExpression)jCMethodInvocation;
/*      */     } finally {
/* 1086 */       this.make.at(i);
/*      */     }
/*      */   }
/*      */
/*      */   private List<Type> bsmStaticArgToTypes(List<Object> paramList) {
/* 1091 */     ListBuffer listBuffer = new ListBuffer();
/* 1092 */     for (Object object : paramList) {
/* 1093 */       listBuffer.append(bsmStaticArgToType(object));
/*      */     }
/* 1095 */     return listBuffer.toList();
/*      */   }
/*      */
/*      */   private Type bsmStaticArgToType(Object paramObject) {
/* 1099 */     Assert.checkNonNull(paramObject);
/* 1100 */     if (paramObject instanceof Symbol.ClassSymbol)
/* 1101 */       return this.syms.classType;
/* 1102 */     if (paramObject instanceof Integer)
/* 1103 */       return (Type)this.syms.intType;
/* 1104 */     if (paramObject instanceof Long)
/* 1105 */       return (Type)this.syms.longType;
/* 1106 */     if (paramObject instanceof Float)
/* 1107 */       return (Type)this.syms.floatType;
/* 1108 */     if (paramObject instanceof Double)
/* 1109 */       return (Type)this.syms.doubleType;
/* 1110 */     if (paramObject instanceof String)
/* 1111 */       return this.syms.stringType;
/* 1112 */     if (paramObject instanceof Pool.MethodHandle)
/* 1113 */       return this.syms.methodHandleType;
/* 1114 */     if (paramObject instanceof Type.MethodType) {
/* 1115 */       return this.syms.methodTypeType;
/*      */     }
/* 1117 */     Assert.error("bad static arg " + paramObject.getClass());
/* 1118 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private int referenceKind(Symbol paramSymbol) {
/* 1126 */     if (paramSymbol.isConstructor()) {
/* 1127 */       return 8;
/*      */     }
/* 1129 */     if (paramSymbol.isStatic())
/* 1130 */       return 6;
/* 1131 */     if ((paramSymbol.flags() & 0x2L) != 0L)
/* 1132 */       return 7;
/* 1133 */     if (paramSymbol.enclClass().isInterface()) {
/* 1134 */       return 9;
/*      */     }
/* 1136 */     return 5;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   class LambdaAnalyzerPreprocessor
/*      */     extends TreeTranslator
/*      */   {
/*      */     private List<Frame> frameStack;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1157 */     private int lambdaCount = 0;
/*      */
/*      */
/*      */
/*      */
/*      */     private List<Symbol.ClassSymbol> typesUnderConstruction;
/*      */
/*      */
/*      */
/*      */
/*      */     private class SyntheticMethodNameCounter
/*      */     {
/* 1169 */       private Map<String, Integer> map = new HashMap<>();
/*      */       int getIndex(StringBuilder param2StringBuilder) {
/* 1171 */         String str = param2StringBuilder.toString();
/* 1172 */         Integer integer = this.map.get(str);
/* 1173 */         if (integer == null) {
/* 1174 */           integer = Integer.valueOf(0);
/*      */         }
/* 1176 */         integer = Integer.valueOf(integer.intValue() + 1);
/* 1177 */         this.map.put(str, integer);
/* 1178 */         return integer.intValue();
/*      */       }
/*      */       private SyntheticMethodNameCounter() {} }
/* 1181 */     private SyntheticMethodNameCounter syntheticMethodNameCounts = new SyntheticMethodNameCounter();
/*      */
/*      */
/*      */
/*      */
/*      */     private Map<Symbol, JCTree.JCClassDecl> localClassDefs;
/*      */
/*      */
/*      */
/* 1190 */     private Map<Symbol.ClassSymbol, Symbol> clinits = new HashMap<>();
/*      */
/*      */
/*      */     private JCTree.JCClassDecl analyzeAndPreprocessClass(JCTree.JCClassDecl param1JCClassDecl) {
/* 1194 */       this.frameStack = List.nil();
/* 1195 */       this.typesUnderConstruction = List.nil();
/* 1196 */       this.localClassDefs = new HashMap<>();
/* 1197 */       return (JCTree.JCClassDecl)translate((JCTree)param1JCClassDecl);
/*      */     }
/*      */
/*      */
/*      */     public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/* 1202 */       List<Symbol.ClassSymbol> list = this.typesUnderConstruction;
/*      */       try {
/* 1204 */         Name name = TreeInfo.name((JCTree)param1JCMethodInvocation.meth);
/* 1205 */         if (name == LambdaToMethod.this.names._this || name == LambdaToMethod.this.names._super) {
/* 1206 */           this.typesUnderConstruction = this.typesUnderConstruction.prepend(currentClass());
/*      */         }
/* 1208 */         super.visitApply(param1JCMethodInvocation);
/*      */       } finally {
/* 1210 */         this.typesUnderConstruction = list;
/*      */       }
/*      */     }
/*      */
/*      */     private Symbol.ClassSymbol currentClass() {
/* 1215 */       for (Frame frame : this.frameStack) {
/* 1216 */         if (frame.tree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 1217 */           JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)frame.tree;
/* 1218 */           return jCClassDecl.sym;
/*      */         }
/*      */       }
/* 1221 */       return null;
/*      */     }
/*      */
/*      */
/*      */     public void visitBlock(JCTree.JCBlock param1JCBlock) {
/* 1226 */       List<Frame> list = this.frameStack;
/*      */       try {
/* 1228 */         if (this.frameStack.nonEmpty() && ((Frame)this.frameStack.head).tree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 1229 */           this.frameStack = this.frameStack.prepend(new Frame((JCTree)param1JCBlock));
/*      */         }
/* 1231 */         super.visitBlock(param1JCBlock);
/*      */       } finally {
/*      */
/* 1234 */         this.frameStack = list;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/* 1240 */       List<Frame> list = this.frameStack;
/* 1241 */       int i = this.lambdaCount;
/* 1242 */       SyntheticMethodNameCounter syntheticMethodNameCounter = this.syntheticMethodNameCounts;
/*      */
/* 1244 */       Map<Symbol.ClassSymbol, Symbol> map = this.clinits;
/* 1245 */       DiagnosticSource diagnosticSource = LambdaToMethod.this.log.currentSource();
/*      */       try {
/* 1247 */         LambdaToMethod.this.log.useSource(param1JCClassDecl.sym.sourcefile);
/* 1248 */         this.lambdaCount = 0;
/* 1249 */         this.syntheticMethodNameCounts = new SyntheticMethodNameCounter();
/* 1250 */         map = new HashMap<>();
/* 1251 */         if (param1JCClassDecl.sym.owner.kind == 16) {
/* 1252 */           this.localClassDefs.put(param1JCClassDecl.sym, param1JCClassDecl);
/*      */         }
/* 1254 */         if (directlyEnclosingLambda() != null) {
/* 1255 */           param1JCClassDecl.sym.owner = owner();
/* 1256 */           if (param1JCClassDecl.sym.hasOuterInstance()) {
/*      */
/*      */
/* 1259 */             TranslationContext<?> translationContext = context();
/* 1260 */             while (translationContext != null) {
/* 1261 */               if (translationContext.tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1262 */                 ((LambdaTranslationContext)translationContext)
/* 1263 */                   .addSymbol((Symbol)(param1JCClassDecl.sym.type.getEnclosingType()).tsym, LambdaSymbolKind.CAPTURED_THIS);
/*      */               }
/* 1265 */               translationContext = translationContext.prev;
/*      */             }
/*      */           }
/*      */         }
/* 1269 */         this.frameStack = this.frameStack.prepend(new Frame((JCTree)param1JCClassDecl));
/* 1270 */         super.visitClassDef(param1JCClassDecl);
/*      */       } finally {
/*      */
/* 1273 */         LambdaToMethod.this.log.useSource(diagnosticSource.getFile());
/* 1274 */         this.frameStack = list;
/* 1275 */         this.lambdaCount = i;
/* 1276 */         this.syntheticMethodNameCounts = syntheticMethodNameCounter;
/* 1277 */         this.clinits = map;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 1283 */       if (context() != null && lambdaIdentSymbolFilter(param1JCIdent.sym)) {
/* 1284 */         if (param1JCIdent.sym.kind == 4 && param1JCIdent.sym.owner.kind == 16 && param1JCIdent.type
/*      */
/* 1286 */           .constValue() == null) {
/* 1287 */           TranslationContext<?> translationContext = context();
/* 1288 */           while (translationContext != null) {
/* 1289 */             if (translationContext.tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1290 */               JCTree jCTree = capturedDecl(translationContext.depth, param1JCIdent.sym);
/* 1291 */               if (jCTree == null)
/* 1292 */                 break;  ((LambdaTranslationContext)translationContext)
/* 1293 */                 .addSymbol(param1JCIdent.sym, LambdaSymbolKind.CAPTURED_VAR);
/*      */             }
/* 1295 */             translationContext = translationContext.prev;
/*      */           }
/* 1297 */         } else if (param1JCIdent.sym.owner.kind == 2) {
/* 1298 */           TranslationContext<?> translationContext = context();
/* 1299 */           while (translationContext != null) {
/* 1300 */             if (translationContext.tree.hasTag(JCTree.Tag.LAMBDA)) {
/* 1301 */               JCTree.JCClassDecl jCClassDecl; JCTree jCTree = capturedDecl(translationContext.depth, param1JCIdent.sym);
/* 1302 */               if (jCTree == null)
/* 1303 */                 break;  switch (jCTree.getTag()) {
/*      */                 case CAPTURED_VAR:
/* 1305 */                   jCClassDecl = (JCTree.JCClassDecl)jCTree;
/* 1306 */                   ((LambdaTranslationContext)translationContext)
/* 1307 */                     .addSymbol((Symbol)jCClassDecl.sym, LambdaSymbolKind.CAPTURED_THIS);
/*      */                   break;
/*      */                 default:
/* 1310 */                   Assert.error("bad block kind"); break;
/*      */               }
/*      */             }
/* 1313 */             translationContext = translationContext.prev;
/*      */           }
/*      */         }
/*      */       }
/* 1317 */       super.visitIdent(param1JCIdent);
/*      */     }
/*      */
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/* 1322 */       analyzeLambda(param1JCLambda, "lambda.stat");
/*      */     }
/*      */
/*      */
/*      */     private void analyzeLambda(JCTree.JCLambda param1JCLambda, JCTree.JCExpression param1JCExpression) {
/* 1327 */       JCTree.JCExpression jCExpression = (JCTree.JCExpression)translate((JCTree)param1JCExpression);
/* 1328 */       LambdaTranslationContext lambdaTranslationContext = analyzeLambda(param1JCLambda, "mref.stat.1");
/* 1329 */       if (jCExpression != null) {
/* 1330 */         lambdaTranslationContext.methodReferenceReceiver = jCExpression;
/*      */       }
/*      */     }
/*      */
/*      */     private LambdaTranslationContext analyzeLambda(JCTree.JCLambda param1JCLambda, String param1String) {
/* 1335 */       List<Frame> list = this.frameStack;
/*      */       try {
/* 1337 */         LambdaTranslationContext lambdaTranslationContext = new LambdaTranslationContext(param1JCLambda);
/* 1338 */         if (LambdaToMethod.this.dumpLambdaToMethodStats) {
/* 1339 */           LambdaToMethod.this.log.note((JCDiagnostic.DiagnosticPosition)param1JCLambda, param1String, new Object[] { Boolean.valueOf(lambdaTranslationContext.needsAltMetafactory()), lambdaTranslationContext.translatedSym });
/*      */         }
/* 1341 */         this.frameStack = this.frameStack.prepend(new Frame((JCTree)param1JCLambda));
/* 1342 */         for (JCTree.JCVariableDecl jCVariableDecl : param1JCLambda.params) {
/* 1343 */           lambdaTranslationContext.addSymbol((Symbol)jCVariableDecl.sym, LambdaSymbolKind.PARAM);
/* 1344 */           ((Frame)this.frameStack.head).addLocal((Symbol)jCVariableDecl.sym);
/*      */         }
/* 1346 */         LambdaToMethod.this.contextMap.put(param1JCLambda, lambdaTranslationContext);
/* 1347 */         super.visitLambda(param1JCLambda);
/* 1348 */         lambdaTranslationContext.complete();
/* 1349 */         return lambdaTranslationContext;
/*      */       } finally {
/*      */
/* 1352 */         this.frameStack = list;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/* 1358 */       List<Frame> list = this.frameStack;
/*      */       try {
/* 1360 */         this.frameStack = this.frameStack.prepend(new Frame((JCTree)param1JCMethodDecl));
/* 1361 */         super.visitMethodDef(param1JCMethodDecl);
/*      */       } finally {
/*      */
/* 1364 */         this.frameStack = list;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 1370 */       Symbol.TypeSymbol typeSymbol = param1JCNewClass.type.tsym;
/* 1371 */       boolean bool1 = currentlyInClass((Symbol)typeSymbol);
/* 1372 */       boolean bool2 = typeSymbol.isLocal();
/* 1373 */       if ((bool1 && bool2) || lambdaNewClassFilter(context(), param1JCNewClass)) {
/* 1374 */         TranslationContext<?> translationContext = context();
/* 1375 */         while (translationContext != null) {
/* 1376 */           if (translationContext.tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1377 */             ((LambdaTranslationContext)translationContext)
/* 1378 */               .addSymbol((Symbol)(param1JCNewClass.type.getEnclosingType()).tsym, LambdaSymbolKind.CAPTURED_THIS);
/*      */           }
/* 1380 */           translationContext = translationContext.prev;
/*      */         }
/*      */       }
/* 1383 */       if (context() != null && !bool1 && bool2) {
/* 1384 */         LambdaTranslationContext lambdaTranslationContext = (LambdaTranslationContext)context();
/* 1385 */         captureLocalClassDefs((Symbol)typeSymbol, lambdaTranslationContext);
/*      */       }
/* 1387 */       super.visitNewClass(param1JCNewClass);
/*      */     }
/*      */
/*      */     void captureLocalClassDefs(Symbol param1Symbol, final LambdaTranslationContext lambdaContext) {
/* 1391 */       JCTree.JCClassDecl jCClassDecl = this.localClassDefs.get(param1Symbol);
/* 1392 */       if (jCClassDecl != null && lambdaContext.freeVarProcessedLocalClasses.add(param1Symbol)) {
/* 1393 */         LambdaToMethod.this.lower.getClass(); Lower.BasicFreeVarCollector basicFreeVarCollector = new Lower.BasicFreeVarCollector(LambdaToMethod.this.lower)
/*      */           {
/*      */             void addFreeVars(Symbol.ClassSymbol param2ClassSymbol) {
/* 1396 */               LambdaAnalyzerPreprocessor.this.captureLocalClassDefs((Symbol)param2ClassSymbol, lambdaContext);
/*      */             }
/*      */
/*      */             void visitSymbol(Symbol param2Symbol) {
/* 1400 */               if (param2Symbol.kind == 4 && param2Symbol.owner.kind == 16 && ((Symbol.VarSymbol)param2Symbol)
/*      */
/* 1402 */                 .getConstValue() == null) {
/* 1403 */                 LambdaAnalyzerPreprocessor.TranslationContext<?> translationContext = LambdaAnalyzerPreprocessor.this.context();
/* 1404 */                 while (translationContext != null) {
/* 1405 */                   if (translationContext.tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1406 */                     JCTree jCTree = LambdaAnalyzerPreprocessor.this.capturedDecl(translationContext.depth, param2Symbol);
/* 1407 */                     if (jCTree == null)
/* 1408 */                       break;  ((LambdaAnalyzerPreprocessor.LambdaTranslationContext)translationContext).addSymbol(param2Symbol, LambdaSymbolKind.CAPTURED_VAR);
/*      */                   }
/* 1410 */                   translationContext = translationContext.prev;
/*      */                 }
/*      */               }
/*      */             }
/*      */           };
/* 1415 */         basicFreeVarCollector.scan((JCTree)jCClassDecl);
/*      */       }
/*      */     }
/*      */
/*      */     boolean currentlyInClass(Symbol param1Symbol) {
/* 1420 */       for (Frame frame : this.frameStack) {
/* 1421 */         if (frame.tree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 1422 */           JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)frame.tree;
/* 1423 */           if (jCClassDecl.sym == param1Symbol) {
/* 1424 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 1428 */       return false;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitReference(JCTree.JCMemberReference param1JCMemberReference) {
/* 1445 */       ReferenceTranslationContext referenceTranslationContext = new ReferenceTranslationContext(param1JCMemberReference);
/* 1446 */       LambdaToMethod.this.contextMap.put(param1JCMemberReference, referenceTranslationContext);
/* 1447 */       if (referenceTranslationContext.needsConversionToLambda()) {
/*      */
/* 1449 */         MemberReferenceToLambda memberReferenceToLambda = new MemberReferenceToLambda(param1JCMemberReference, referenceTranslationContext, owner());
/* 1450 */         analyzeLambda(memberReferenceToLambda.lambda(), memberReferenceToLambda.getReceiverExpression());
/*      */       } else {
/* 1452 */         super.visitReference(param1JCMemberReference);
/* 1453 */         if (LambdaToMethod.this.dumpLambdaToMethodStats) {
/* 1454 */           LambdaToMethod.this.log.note((JCDiagnostic.DiagnosticPosition)param1JCMemberReference, "mref.stat", new Object[] { Boolean.valueOf(referenceTranslationContext.needsAltMetafactory()), null });
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 1461 */       if (context() != null && param1JCFieldAccess.sym.kind == 4 && (param1JCFieldAccess.sym.name ==
/* 1462 */         LambdaToMethod.this.names._this || param1JCFieldAccess.sym.name ==
/* 1463 */         LambdaToMethod.this.names._super)) {
/*      */
/*      */
/* 1466 */         TranslationContext<?> translationContext = context();
/* 1467 */         while (translationContext != null) {
/* 1468 */           if (translationContext.tree.hasTag(JCTree.Tag.LAMBDA)) {
/* 1469 */             JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)capturedDecl(translationContext.depth, param1JCFieldAccess.sym);
/* 1470 */             if (jCClassDecl == null)
/* 1471 */               break;  ((LambdaTranslationContext)translationContext).addSymbol((Symbol)jCClassDecl.sym, LambdaSymbolKind.CAPTURED_THIS);
/*      */           }
/* 1473 */           translationContext = translationContext.prev;
/*      */         }
/*      */       }
/* 1476 */       super.visitSelect(param1JCFieldAccess);
/*      */     }
/*      */
/*      */
/*      */     public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 1481 */       TranslationContext<?> translationContext = context();
/* 1482 */       LambdaTranslationContext lambdaTranslationContext = (translationContext != null && translationContext instanceof LambdaTranslationContext) ? (LambdaTranslationContext)translationContext : null;
/*      */
/*      */
/* 1485 */       if (lambdaTranslationContext != null) {
/* 1486 */         if (((Frame)this.frameStack.head).tree.hasTag(JCTree.Tag.LAMBDA)) {
/* 1487 */           lambdaTranslationContext.addSymbol((Symbol)param1JCVariableDecl.sym, LambdaSymbolKind.LOCAL_VAR);
/*      */         }
/*      */
/*      */
/* 1491 */         Type type = param1JCVariableDecl.sym.asType();
/* 1492 */         if (inClassWithinLambda() && !LambdaToMethod.this.types.isSameType(LambdaToMethod.this.types.erasure(type), type)) {
/* 1493 */           lambdaTranslationContext.addSymbol((Symbol)param1JCVariableDecl.sym, LambdaSymbolKind.TYPE_VAR);
/*      */         }
/*      */       }
/*      */
/* 1497 */       List<Frame> list = this.frameStack;
/*      */       try {
/* 1499 */         if (param1JCVariableDecl.sym.owner.kind == 16) {
/* 1500 */           ((Frame)this.frameStack.head).addLocal((Symbol)param1JCVariableDecl.sym);
/*      */         }
/* 1502 */         this.frameStack = this.frameStack.prepend(new Frame((JCTree)param1JCVariableDecl));
/* 1503 */         super.visitVarDef(param1JCVariableDecl);
/*      */       } finally {
/*      */
/* 1506 */         this.frameStack = list;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private Symbol owner() {
/* 1515 */       return owner(false);
/*      */     }
/*      */
/*      */
/*      */     private Symbol owner(boolean param1Boolean) {
/* 1520 */       List<Frame> list = this.frameStack;
/* 1521 */       while (list.nonEmpty()) {
/* 1522 */         JCTree.JCClassDecl jCClassDecl1, jCClassDecl2; switch (((Frame)list.head).tree.getTag()) {
/*      */           case CAPTURED_OUTER_THIS:
/* 1524 */             if (((JCTree.JCVariableDecl)((Frame)list.head).tree).sym.isLocal()) {
/* 1525 */               list = list.tail;
/*      */               continue;
/*      */             }
/* 1528 */             jCClassDecl1 = (JCTree.JCClassDecl)((Frame)list.tail.head).tree;
/* 1529 */             return initSym(jCClassDecl1.sym, ((JCTree.JCVariableDecl)((Frame)list.head).tree).sym
/* 1530 */                 .flags() & 0x8L);
/*      */           case LOCAL_VAR:
/* 1532 */             jCClassDecl2 = (JCTree.JCClassDecl)((Frame)list.tail.head).tree;
/* 1533 */             return initSym(jCClassDecl2.sym, ((JCTree.JCBlock)((Frame)list.head).tree).flags & 0x8L);
/*      */
/*      */           case CAPTURED_VAR:
/* 1536 */             return (Symbol)((JCTree.JCClassDecl)((Frame)list.head).tree).sym;
/*      */           case PARAM:
/* 1538 */             return (Symbol)((JCTree.JCMethodDecl)((Frame)list.head).tree).sym;
/*      */           case null:
/* 1540 */             if (!param1Boolean)
/* 1541 */               return
/* 1542 */                 (Symbol)((LambdaTranslationContext)LambdaToMethod.this.contextMap.get(((Frame)list.head).tree)).translatedSym;  break;
/*      */         }
/* 1544 */         list = list.tail;
/*      */       }
/*      */
/* 1547 */       Assert.error();
/* 1548 */       return null;
/*      */     }
/*      */
/*      */     private Symbol initSym(Symbol.ClassSymbol param1ClassSymbol, long param1Long) {
/* 1552 */       boolean bool = ((param1Long & 0x8L) != 0L) ? true : false;
/* 1553 */       if (bool) {
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1559 */         Symbol.MethodSymbol methodSymbol = LambdaToMethod.this.attr.removeClinit(param1ClassSymbol);
/* 1560 */         if (methodSymbol != null) {
/* 1561 */           this.clinits.put(param1ClassSymbol, methodSymbol);
/* 1562 */           return (Symbol)methodSymbol;
/*      */         }
/*      */
/*      */
/*      */
/* 1567 */         methodSymbol = (Symbol.MethodSymbol)this.clinits.get(param1ClassSymbol);
/* 1568 */         if (methodSymbol == null) {
/*      */
/*      */
/* 1571 */           methodSymbol = LambdaToMethod.this.makePrivateSyntheticMethod(8L,
/* 1572 */               LambdaToMethod.this.names.clinit, (Type)new Type.MethodType(
/* 1573 */                 List.nil(), (Type)LambdaToMethod.this.syms.voidType,
/* 1574 */                 List.nil(), (Symbol.TypeSymbol)LambdaToMethod.this.syms.methodClass), (Symbol)param1ClassSymbol);
/*      */
/* 1576 */           this.clinits.put(param1ClassSymbol, methodSymbol);
/*      */         }
/* 1578 */         return (Symbol)methodSymbol;
/*      */       }
/*      */
/* 1581 */       Iterator<Symbol> iterator = param1ClassSymbol.members_field.getElementsByName(LambdaToMethod.this.names.init).iterator(); if (iterator.hasNext()) return iterator.next();
/*      */
/*      */
/*      */
/* 1585 */       Assert.error("init not found");
/* 1586 */       return null;
/*      */     }
/*      */
/*      */     private JCTree directlyEnclosingLambda() {
/* 1590 */       if (this.frameStack.isEmpty()) {
/* 1591 */         return null;
/*      */       }
/* 1593 */       List<Frame> list = this.frameStack;
/* 1594 */       while (list.nonEmpty()) {
/* 1595 */         switch (((Frame)list.head).tree.getTag()) {
/*      */           case CAPTURED_VAR:
/*      */           case PARAM:
/* 1598 */             return null;
/*      */           case null:
/* 1600 */             return ((Frame)list.head).tree;
/*      */         }
/* 1602 */         list = list.tail;
/*      */       }
/*      */
/* 1605 */       Assert.error();
/* 1606 */       return null;
/*      */     }
/*      */
/*      */     private boolean inClassWithinLambda() {
/* 1610 */       if (this.frameStack.isEmpty()) {
/* 1611 */         return false;
/*      */       }
/* 1613 */       List<Frame> list = this.frameStack;
/* 1614 */       boolean bool = false;
/* 1615 */       while (list.nonEmpty()) {
/* 1616 */         switch (((Frame)list.head).tree.getTag()) {
/*      */           case null:
/* 1618 */             return bool;
/*      */           case CAPTURED_VAR:
/* 1620 */             bool = true;
/* 1621 */             list = list.tail;
/*      */             continue;
/*      */         }
/* 1624 */         list = list.tail;
/*      */       }
/*      */
/*      */
/* 1628 */       return false;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private JCTree capturedDecl(int param1Int, Symbol param1Symbol) {
/* 1637 */       int i = this.frameStack.size() - 1;
/* 1638 */       for (Frame frame : this.frameStack) {
/* 1639 */         Symbol.ClassSymbol classSymbol; switch (frame.tree.getTag()) {
/*      */           case CAPTURED_VAR:
/* 1641 */             classSymbol = ((JCTree.JCClassDecl)frame.tree).sym;
/* 1642 */             if (param1Symbol.isMemberOf((Symbol.TypeSymbol)classSymbol, LambdaToMethod.this.types)) {
/* 1643 */               return (i > param1Int) ? null : frame.tree;
/*      */             }
/*      */             break;
/*      */           case CAPTURED_OUTER_THIS:
/* 1647 */             if (((JCTree.JCVariableDecl)frame.tree).sym == param1Symbol && param1Symbol.owner.kind == 16)
/*      */             {
/* 1649 */               return (i > param1Int) ? null : frame.tree;
/*      */             }
/*      */             break;
/*      */           case LOCAL_VAR:
/*      */           case PARAM:
/*      */           case null:
/* 1655 */             if (frame.locals != null && frame.locals.contains(param1Symbol)) {
/* 1656 */               return (i > param1Int) ? null : frame.tree;
/*      */             }
/*      */             break;
/*      */           default:
/* 1660 */             Assert.error("bad decl kind " + frame.tree.getTag()); break;
/*      */         }
/* 1662 */         i--;
/*      */       }
/* 1664 */       return null;
/*      */     }
/*      */
/*      */     private TranslationContext<?> context() {
/* 1668 */       for (Frame frame : this.frameStack) {
/* 1669 */         TranslationContext<?> translationContext = (TranslationContext)LambdaToMethod.this.contextMap.get(frame.tree);
/* 1670 */         if (translationContext != null) {
/* 1671 */           return translationContext;
/*      */         }
/*      */       }
/* 1674 */       return null;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private boolean lambdaIdentSymbolFilter(Symbol param1Symbol) {
/* 1682 */       return ((param1Symbol.kind == 4 || param1Symbol.kind == 16) &&
/* 1683 */         !param1Symbol.isStatic() && param1Symbol.name !=
/* 1684 */         LambdaToMethod.this.names.init);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private boolean lambdaFieldAccessFilter(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 1695 */       LambdaTranslationContext lambdaTranslationContext = (LambdaToMethod.this.context instanceof LambdaTranslationContext) ? (LambdaTranslationContext)LambdaToMethod.this.context : null;
/* 1696 */       return (lambdaTranslationContext != null &&
/* 1697 */         !param1JCFieldAccess.sym.isStatic() && param1JCFieldAccess.name ==
/* 1698 */         LambdaToMethod.this.names._this && param1JCFieldAccess.sym.owner.kind == 2 &&
/*      */
/* 1700 */         !((Map)lambdaTranslationContext.translatedSymbols.get(LambdaSymbolKind.CAPTURED_OUTER_THIS)).isEmpty());
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private boolean lambdaNewClassFilter(TranslationContext<?> param1TranslationContext, JCTree.JCNewClass param1JCNewClass) {
/* 1708 */       if (param1TranslationContext != null && param1JCNewClass.encl == null && param1JCNewClass.def == null &&
/*      */
/*      */
/* 1711 */         !param1JCNewClass.type.getEnclosingType().hasTag(TypeTag.NONE)) {
/* 1712 */         Type type1 = param1JCNewClass.type.getEnclosingType();
/* 1713 */         Type type2 = (param1TranslationContext.owner.enclClass()).type;
/* 1714 */         while (!type2.hasTag(TypeTag.NONE)) {
/* 1715 */           if (type2.tsym.isSubClass((Symbol)type1.tsym, LambdaToMethod.this.types)) {
/* 1716 */             return true;
/*      */           }
/* 1718 */           type2 = type2.getEnclosingType();
/*      */         }
/* 1720 */         return false;
/*      */       }
/* 1722 */       return false;
/*      */     }
/*      */
/*      */     private class Frame
/*      */     {
/*      */       final JCTree tree;
/*      */       List<Symbol> locals;
/*      */
/*      */       public Frame(JCTree param2JCTree) {
/* 1731 */         this.tree = param2JCTree;
/*      */       }
/*      */
/*      */       void addLocal(Symbol param2Symbol) {
/* 1735 */         if (this.locals == null) {
/* 1736 */           this.locals = List.nil();
/*      */         }
/* 1738 */         this.locals = this.locals.prepend(param2Symbol);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     private abstract class TranslationContext<T extends JCTree.JCFunctionalExpression>
/*      */     {
/*      */       final T tree;
/*      */
/*      */
/*      */       final Symbol owner;
/*      */
/*      */
/*      */       final int depth;
/*      */
/*      */
/*      */       final TranslationContext<?> prev;
/*      */
/*      */
/*      */       final List<Symbol> bridges;
/*      */
/*      */
/*      */
/*      */       TranslationContext(T param2T) {
/* 1764 */         this.tree = param2T;
/* 1765 */         this.owner = LambdaAnalyzerPreprocessor.this.owner();
/* 1766 */         this.depth = LambdaAnalyzerPreprocessor.this.frameStack.size() - 1;
/* 1767 */         this.prev = LambdaAnalyzerPreprocessor.this.context();
/*      */
/* 1769 */         Symbol.ClassSymbol classSymbol = LambdaToMethod.this.types.makeFunctionalInterfaceClass(LambdaToMethod.this.attrEnv, LambdaToMethod.this.names.empty, ((JCTree.JCFunctionalExpression)param2T).targets, 1536L);
/* 1770 */         this.bridges = LambdaToMethod.this.types.functionalInterfaceBridges((Symbol.TypeSymbol)classSymbol);
/*      */       }
/*      */
/*      */
/*      */       boolean needsAltMetafactory() {
/* 1775 */         return (((JCTree.JCFunctionalExpression)this.tree).targets.length() > 1 ||
/* 1776 */           isSerializable() || this.bridges
/* 1777 */           .length() > 1);
/*      */       }
/*      */
/*      */
/*      */       boolean isSerializable() {
/* 1782 */         if (LambdaToMethod.this.forceSerializable) {
/* 1783 */           return true;
/*      */         }
/* 1785 */         for (Type type : ((JCTree.JCFunctionalExpression)this.tree).targets) {
/* 1786 */           if (LambdaToMethod.this.types.asSuper(type, (Symbol)LambdaToMethod.this.syms.serializableType.tsym) != null) {
/* 1787 */             return true;
/*      */           }
/*      */         }
/* 1790 */         return false;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       String enclosingMethodName() {
/* 1798 */         return syntheticMethodNameComponent(this.owner.name);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       String syntheticMethodNameComponent(Name param2Name) {
/* 1806 */         if (param2Name == null) {
/* 1807 */           return "null";
/*      */         }
/* 1809 */         String str = param2Name.toString();
/* 1810 */         if (str.equals("<clinit>")) {
/* 1811 */           str = "static";
/* 1812 */         } else if (str.equals("<init>")) {
/* 1813 */           str = "new";
/*      */         }
/* 1815 */         return str;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     private class LambdaTranslationContext
/*      */       extends TranslationContext<JCTree.JCLambda>
/*      */     {
/*      */       final Symbol self;
/*      */
/*      */
/*      */
/*      */       final Symbol assignedTo;
/*      */
/*      */
/*      */
/*      */       Map<LambdaSymbolKind, Map<Symbol, Symbol>> translatedSymbols;
/*      */
/*      */
/*      */
/*      */       Symbol.MethodSymbol translatedSym;
/*      */
/*      */
/*      */
/*      */       List<JCTree.JCVariableDecl> syntheticParams;
/*      */
/*      */
/*      */
/*      */       final Set<Symbol> freeVarProcessedLocalClasses;
/*      */
/*      */
/*      */       JCTree.JCExpression methodReferenceReceiver;
/*      */
/*      */
/*      */
/*      */       LambdaTranslationContext(JCTree.JCLambda param2JCLambda) {
/* 1853 */         super(param2JCLambda);
/* 1854 */         Frame frame = (Frame) LambdaAnalyzerPreprocessor.this.frameStack.head;
/* 1855 */         switch (frame.tree.getTag()) {
/*      */           case CAPTURED_OUTER_THIS:
/* 1857 */             this.assignedTo = this.self = (Symbol)((JCTree.JCVariableDecl)frame.tree).sym;
/*      */             break;
/*      */           case null:
/* 1860 */             this.self = null;
/* 1861 */             this.assignedTo = TreeInfo.symbol((JCTree)((JCTree.JCAssign)frame.tree).getVariable());
/*      */             break;
/*      */           default:
/* 1864 */             this.assignedTo = this.self = null;
/*      */             break;
/*      */         }
/*      */
/*      */
/* 1869 */         this.translatedSym = LambdaToMethod.this.makePrivateSyntheticMethod(0L, null, null, (Symbol)this.owner.enclClass());
/*      */
/* 1871 */         this.translatedSymbols = new EnumMap<>(LambdaSymbolKind.class);
/*      */
/* 1873 */         this.translatedSymbols.put(LambdaSymbolKind.PARAM, new LinkedHashMap<>());
/* 1874 */         this.translatedSymbols.put(LambdaSymbolKind.LOCAL_VAR, new LinkedHashMap<>());
/* 1875 */         this.translatedSymbols.put(LambdaSymbolKind.CAPTURED_VAR, new LinkedHashMap<>());
/* 1876 */         this.translatedSymbols.put(LambdaSymbolKind.CAPTURED_THIS, new LinkedHashMap<>());
/* 1877 */         this.translatedSymbols.put(LambdaSymbolKind.CAPTURED_OUTER_THIS, new LinkedHashMap<>());
/* 1878 */         this.translatedSymbols.put(LambdaSymbolKind.TYPE_VAR, new LinkedHashMap<>());
/*      */
/* 1880 */         this.freeVarProcessedLocalClasses = new HashSet<>();
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private String serializedLambdaDisambiguation() {
/* 1890 */         StringBuilder stringBuilder = new StringBuilder();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1897 */         Assert.check((this.owner.type != null || LambdaAnalyzerPreprocessor.this
/*      */
/* 1899 */             .directlyEnclosingLambda() != null));
/* 1900 */         if (this.owner.type != null) {
/* 1901 */           stringBuilder.append(LambdaToMethod.this.typeSig(this.owner.type));
/* 1902 */           stringBuilder.append(":");
/*      */         }
/*      */
/*      */
/* 1906 */         stringBuilder.append((CharSequence)(LambdaToMethod.this.types.findDescriptorSymbol(this.tree.type.tsym)).owner.flatName());
/* 1907 */         stringBuilder.append(" ");
/*      */
/*      */
/* 1910 */         if (this.assignedTo != null) {
/* 1911 */           stringBuilder.append((CharSequence)this.assignedTo.flatName());
/* 1912 */           stringBuilder.append("=");
/*      */         }
/*      */
/* 1915 */         for (Symbol symbol : getSymbolMap(LambdaSymbolKind.CAPTURED_VAR).keySet()) {
/* 1916 */           if (symbol != this.self) {
/* 1917 */             stringBuilder.append(LambdaToMethod.this.typeSig(symbol.type));
/* 1918 */             stringBuilder.append(" ");
/* 1919 */             stringBuilder.append((CharSequence)symbol.flatName());
/* 1920 */             stringBuilder.append(",");
/*      */           }
/*      */         }
/*      */
/* 1924 */         return stringBuilder.toString();
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Name lambdaName() {
/* 1933 */         return LambdaToMethod.this.names.lambda.append(LambdaToMethod.this.names.fromString(enclosingMethodName() + "$" + LambdaAnalyzerPreprocessor.this.lambdaCount++));
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Name serializedLambdaName() {
/* 1943 */         StringBuilder stringBuilder = new StringBuilder();
/* 1944 */         stringBuilder.append((CharSequence)LambdaToMethod.this.names.lambda);
/*      */
/* 1946 */         stringBuilder.append(enclosingMethodName());
/* 1947 */         stringBuilder.append('$');
/*      */
/*      */
/* 1950 */         String str1 = serializedLambdaDisambiguation();
/* 1951 */         stringBuilder.append(Integer.toHexString(str1.hashCode()));
/* 1952 */         stringBuilder.append('$');
/*      */
/*      */
/* 1955 */         stringBuilder.append(LambdaAnalyzerPreprocessor.this.syntheticMethodNameCounts.getIndex(stringBuilder));
/* 1956 */         String str2 = stringBuilder.toString();
/*      */
/* 1958 */         return LambdaToMethod.this.names.fromString(str2);
/*      */       }
/*      */
/*      */
/*      */
/*      */       Symbol translate(final Symbol sym, LambdaSymbolKind param2LambdaSymbolKind) {
/*      */         Symbol symbol;
/*      */         Symbol.VarSymbol varSymbol;
/*      */         Name name;
/* 1967 */         switch (param2LambdaSymbolKind) {
/*      */           case CAPTURED_THIS:
/* 1969 */             symbol = sym;
/*      */             break;
/*      */
/*      */
/*      */           case TYPE_VAR:
/* 1974 */             varSymbol = new Symbol.VarSymbol(sym.flags(), sym.name, LambdaToMethod.this.types.erasure(sym.type), sym.owner);
/*      */
/*      */
/*      */
/*      */
/* 1979 */             varSymbol.pos = ((Symbol.VarSymbol)sym).pos;
/*      */             break;
/*      */           case CAPTURED_VAR:
/* 1982 */             varSymbol = new Symbol.VarSymbol(8589938704L, sym.name, LambdaToMethod.this.types.erasure(sym.type), (Symbol)this.translatedSym)
/*      */               {
/*      */                 public Symbol baseSymbol()
/*      */                 {
/* 1986 */                   return sym;
/*      */                 }
/*      */               };
/*      */             break;
/*      */           case CAPTURED_OUTER_THIS:
/* 1991 */             name = LambdaToMethod.this.names.fromString(new String(sym.flatName().toString() + LambdaToMethod.this.names.dollarThis));
/* 1992 */             varSymbol = new Symbol.VarSymbol(8589938704L, name, LambdaToMethod.this.types.erasure(sym.type), (Symbol)this.translatedSym)
/*      */               {
/*      */                 public Symbol baseSymbol()
/*      */                 {
/* 1996 */                   return sym;
/*      */                 }
/*      */               };
/*      */             break;
/*      */           case LOCAL_VAR:
/* 2001 */             varSymbol = new Symbol.VarSymbol(sym.flags() & 0x10L, sym.name, sym.type, (Symbol)this.translatedSym);
/* 2002 */             varSymbol.pos = ((Symbol.VarSymbol)sym).pos;
/*      */             break;
/*      */           case PARAM:
/* 2005 */             varSymbol = new Symbol.VarSymbol(sym.flags() & 0x10L | 0x200000000L, sym.name, LambdaToMethod.this.types.erasure(sym.type), (Symbol)this.translatedSym);
/* 2006 */             varSymbol.pos = ((Symbol.VarSymbol)sym).pos;
/*      */             break;
/*      */           default:
/* 2009 */             Assert.error(param2LambdaSymbolKind.name());
/* 2010 */             throw new AssertionError();
/*      */         }
/* 2012 */         if (varSymbol != sym && param2LambdaSymbolKind.propagateAnnotations()) {
/* 2013 */           varSymbol.setDeclarationAttributes(sym.getRawAttributes());
/* 2014 */           varSymbol.setTypeAttributes(sym.getRawTypeAttributes());
/*      */         }
/* 2016 */         return (Symbol)varSymbol;
/*      */       }
/*      */
/*      */       void addSymbol(Symbol param2Symbol, LambdaSymbolKind param2LambdaSymbolKind) {
/* 2020 */         if (param2LambdaSymbolKind == LambdaSymbolKind.CAPTURED_THIS && param2Symbol != null && param2Symbol.kind == 2 && !LambdaAnalyzerPreprocessor.this.typesUnderConstruction.isEmpty()) {
/* 2021 */           Symbol.ClassSymbol classSymbol = LambdaAnalyzerPreprocessor.this.currentClass();
/* 2022 */           if (classSymbol != null && LambdaAnalyzerPreprocessor.this.typesUnderConstruction.contains(classSymbol)) {
/*      */
/* 2024 */             Assert.check((param2Symbol != classSymbol));
/* 2025 */             param2LambdaSymbolKind = LambdaSymbolKind.CAPTURED_OUTER_THIS;
/*      */           }
/*      */         }
/* 2028 */         Map<Symbol, Symbol> map = getSymbolMap(param2LambdaSymbolKind);
/* 2029 */         if (!map.containsKey(param2Symbol)) {
/* 2030 */           map.put(param2Symbol, translate(param2Symbol, param2LambdaSymbolKind));
/*      */         }
/*      */       }
/*      */
/*      */       Map<Symbol, Symbol> getSymbolMap(LambdaSymbolKind param2LambdaSymbolKind) {
/* 2035 */         Map<Symbol, Symbol> map = this.translatedSymbols.get(param2LambdaSymbolKind);
/* 2036 */         Assert.checkNonNull(map);
/* 2037 */         return map;
/*      */       }
/*      */
/*      */       JCTree translate(JCTree.JCIdent param2JCIdent) {
/* 2041 */         for (LambdaSymbolKind lambdaSymbolKind : LambdaSymbolKind.values()) {
/* 2042 */           Map<Symbol, Symbol> map = getSymbolMap(lambdaSymbolKind);
/* 2043 */           switch (lambdaSymbolKind) {
/*      */             default:
/* 2045 */               if (map.containsKey(param2JCIdent.sym)) {
/* 2046 */                 Symbol symbol = map.get(param2JCIdent.sym);
/* 2047 */                 return (JCTree)LambdaToMethod.this.make.Ident(symbol).setType(param2JCIdent.type);
/*      */               }
/*      */               break;
/*      */
/*      */             case CAPTURED_OUTER_THIS:
/* 2052 */               if (param2JCIdent.sym.owner.kind == 2 && map.containsKey(param2JCIdent.sym.owner)) {
/*      */
/* 2054 */                 Symbol symbol = map.get(param2JCIdent.sym.owner);
/* 2055 */                 JCTree.JCExpression jCExpression = LambdaToMethod.this.make.Ident(symbol).setType(param2JCIdent.sym.owner.type);
/* 2056 */                 JCTree.JCFieldAccess jCFieldAccess = LambdaToMethod.this.make.Select(jCExpression, param2JCIdent.name);
/* 2057 */                 jCFieldAccess.setType(param2JCIdent.type);
/* 2058 */                 TreeInfo.setSymbol((JCTree)jCFieldAccess, param2JCIdent.sym);
/* 2059 */                 return (JCTree)jCFieldAccess;
/*      */               }
/*      */               break;
/*      */           }
/*      */         }
/* 2064 */         return null;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       public JCTree translate(JCTree.JCFieldAccess param2JCFieldAccess) {
/* 2071 */         Assert.check((param2JCFieldAccess.name == LambdaToMethod.this.names._this));
/* 2072 */         Map map = this.translatedSymbols.get(LambdaSymbolKind.CAPTURED_OUTER_THIS);
/* 2073 */         if (map.containsKey(param2JCFieldAccess.sym.owner)) {
/* 2074 */           Symbol symbol = (Symbol)map.get(param2JCFieldAccess.sym.owner);
/* 2075 */           return (JCTree)LambdaToMethod.this.make.Ident(symbol).setType(param2JCFieldAccess.sym.owner.type);
/*      */         }
/*      */
/* 2078 */         return null;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       void complete() {
/* 2088 */         if (this.syntheticParams != null) {
/*      */           return;
/*      */         }
/* 2091 */         boolean bool = this.translatedSym.owner.isInterface();
/* 2092 */         boolean bool1 = !getSymbolMap(LambdaSymbolKind.CAPTURED_THIS).isEmpty() ? true : false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2099 */         this.translatedSym.flags_field = 0x2000000001000L | this.owner.flags_field & 0x800L | this.owner.owner.flags_field & 0x800L | 0x2L | (bool1 ? (bool ? 8796093022208L : 0L) : 8L);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2106 */         ListBuffer listBuffer1 = new ListBuffer();
/* 2107 */         ListBuffer listBuffer2 = new ListBuffer();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2114 */         for (Symbol symbol : getSymbolMap(LambdaSymbolKind.CAPTURED_VAR).values()) {
/* 2115 */           listBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)symbol, null));
/* 2116 */           listBuffer2.append(symbol);
/*      */         }
/* 2118 */         for (Symbol symbol : getSymbolMap(LambdaSymbolKind.CAPTURED_OUTER_THIS).values()) {
/* 2119 */           listBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)symbol, null));
/* 2120 */           listBuffer2.append(symbol);
/*      */         }
/* 2122 */         for (Symbol symbol : getSymbolMap(LambdaSymbolKind.PARAM).values()) {
/* 2123 */           listBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)symbol, null));
/* 2124 */           listBuffer2.append(symbol);
/*      */         }
/* 2126 */         this.syntheticParams = listBuffer1.toList();
/*      */
/* 2128 */         this.translatedSym.params = listBuffer2.toList();
/*      */
/*      */
/* 2131 */         this.translatedSym
/*      */
/* 2133 */           .name = isSerializable() ? serializedLambdaName() : lambdaName();
/*      */
/*      */
/* 2136 */         this.translatedSym.type = LambdaToMethod.this.types.createMethodTypeWithParameters(
/* 2137 */             generatedLambdaSig(),
/* 2138 */             TreeInfo.types(this.syntheticParams));
/*      */       }
/*      */
/*      */       Type generatedLambdaSig() {
/* 2142 */         return LambdaToMethod.this.types.erasure(this.tree.getDescriptorType(LambdaToMethod.this.types));
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     private final class ReferenceTranslationContext
/*      */       extends TranslationContext<JCTree.JCMemberReference>
/*      */     {
/*      */       final boolean isSuper;
/*      */
/*      */
/*      */       final Symbol sigPolySym;
/*      */
/*      */
/*      */       ReferenceTranslationContext(JCTree.JCMemberReference param2JCMemberReference) {
/* 2158 */         super(param2JCMemberReference);
/* 2159 */         this.isSuper = param2JCMemberReference.hasKind(JCTree.JCMemberReference.ReferenceKind.SUPER);
/* 2160 */         this
/* 2161 */           .sigPolySym = isSignaturePolymorphic() ? (Symbol)LambdaToMethod.this.makePrivateSyntheticMethod(param2JCMemberReference.sym.flags(), param2JCMemberReference.sym.name,
/*      */
/* 2163 */             bridgedRefSig(), (Symbol)param2JCMemberReference.sym
/* 2164 */             .enclClass()) : null;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       int referenceKind() {
/* 2172 */         return LambdaToMethod.this.referenceKind(this.tree.sym);
/*      */       }
/*      */
/*      */       boolean needsVarArgsConversion() {
/* 2176 */         return (this.tree.varargsElement != null);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       boolean isArrayOp() {
/* 2183 */         return (this.tree.sym.owner == LambdaToMethod.this.syms.arrayClass);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       boolean receiverAccessible() {
/* 2190 */         return this.tree.ownerAccessible;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       boolean isPrivateInOtherClass() {
/* 2198 */         return ((this.tree.sym.flags() & 0x2L) != 0L &&
/* 2199 */           !LambdaToMethod.this.types.isSameType(LambdaToMethod.this
/* 2200 */             .types.erasure(this.tree.sym.enclClass().asType()), LambdaToMethod.this
/* 2201 */             .types.erasure(this.owner.enclClass().asType())));
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       final boolean isSignaturePolymorphic() {
/* 2209 */         return (this.tree.sym.kind == 16 && LambdaToMethod.this
/* 2210 */           .types.isSignaturePolymorphic((Symbol.MethodSymbol)this.tree.sym));
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       boolean interfaceParameterIsIntersectionType() {
/* 2218 */         List list = this.tree.getDescriptorType(LambdaToMethod.this.types).getParameterTypes();
/* 2219 */         if (this.tree.kind == JCTree.JCMemberReference.ReferenceKind.UNBOUND) {
/* 2220 */           list = list.tail;
/*      */         }
/* 2222 */         for (; list.nonEmpty(); list = list.tail) {
/* 2223 */           Type type = (Type)list.head;
/* 2224 */           if (type.getKind() == TypeKind.TYPEVAR) {
/* 2225 */             Type.TypeVar typeVar = (Type.TypeVar)type;
/* 2226 */             if (typeVar.bound.getKind() == TypeKind.INTERSECTION) {
/* 2227 */               return true;
/*      */             }
/*      */           }
/*      */         }
/* 2231 */         return false;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       final boolean needsConversionToLambda() {
/* 2239 */         return (interfaceParameterIsIntersectionType() || this.isSuper ||
/*      */
/* 2241 */           needsVarArgsConversion() ||
/* 2242 */           isArrayOp() ||
/* 2243 */           isPrivateInOtherClass() ||
/* 2244 */           !receiverAccessible() || (this.tree
/* 2245 */           .getMode() == MemberReferenceTree.ReferenceMode.NEW && this.tree.kind != JCTree.JCMemberReference.ReferenceKind.ARRAY_CTOR && (this.tree.sym.owner
/*      */
/* 2247 */           .isLocal() || this.tree.sym.owner.isInner())));
/*      */       }
/*      */
/*      */       Type generatedRefSig() {
/* 2251 */         return LambdaToMethod.this.types.erasure(this.tree.sym.type);
/*      */       }
/*      */
/*      */       Type bridgedRefSig() {
/* 2255 */         return LambdaToMethod.this.types.erasure((LambdaToMethod.this.types.findDescriptorSymbol(((Type)this.tree.targets.head).tsym)).type); } } } private abstract class TranslationContext<T extends JCTree.JCFunctionalExpression> { final T tree; final Symbol owner; final int depth; final TranslationContext<?> prev; final List<Symbol> bridges; TranslationContext(T param1T) { this.tree = param1T; this.owner = LambdaToMethod.this.owner(); this.depth = ((LambdaAnalyzerPreprocessor)LambdaToMethod.this).frameStack.size() - 1; this.prev = LambdaToMethod.this.context(); Symbol.ClassSymbol classSymbol = LambdaToMethod.this.types.makeFunctionalInterfaceClass(LambdaToMethod.this.attrEnv, LambdaToMethod.this.names.empty, ((JCTree.JCFunctionalExpression)param1T).targets, 1536L); this.bridges = LambdaToMethod.this.types.functionalInterfaceBridges((Symbol.TypeSymbol)classSymbol); } boolean needsAltMetafactory() { return (((JCTree.JCFunctionalExpression)this.tree).targets.length() > 1 || isSerializable() || this.bridges.length() > 1); } boolean isSerializable() { if (LambdaToMethod.this.forceSerializable) return true;  for (Type type : ((JCTree.JCFunctionalExpression)this.tree).targets) { if (LambdaToMethod.this.types.asSuper(type, (Symbol)LambdaToMethod.this.syms.serializableType.tsym) != null) return true;  }  return false; } String enclosingMethodName() { return syntheticMethodNameComponent(this.owner.name); } String syntheticMethodNameComponent(Name param1Name) { if (param1Name == null) return "null";  String str = param1Name.toString(); if (str.equals("<clinit>")) { str = "static"; } else if (str.equals("<init>")) { str = "new"; }  return str; } } private class LambdaTranslationContext extends LambdaAnalyzerPreprocessor.TranslationContext<JCTree.JCLambda> { final Symbol self; final Symbol assignedTo; Map<LambdaSymbolKind, Map<Symbol, Symbol>> translatedSymbols; Symbol.MethodSymbol translatedSym; List<JCTree.JCVariableDecl> syntheticParams; final Set<Symbol> freeVarProcessedLocalClasses; JCTree.JCExpression methodReferenceReceiver; LambdaTranslationContext(JCTree.JCLambda param1JCLambda) { super((LambdaAnalyzerPreprocessor)LambdaToMethod.this, param1JCLambda); LambdaAnalyzerPreprocessor.Frame frame = (LambdaAnalyzerPreprocessor.Frame)((LambdaAnalyzerPreprocessor)LambdaToMethod.this).frameStack.head; switch (frame.tree.getTag()) { case CAPTURED_OUTER_THIS: this.assignedTo = this.self = (Symbol)((JCTree.JCVariableDecl)frame.tree).sym; break;case null: this.self = null; this.assignedTo = TreeInfo.symbol((JCTree)((JCTree.JCAssign)frame.tree).getVariable()); break;default: this.assignedTo = this.self = null; break; }  this.translatedSym = LambdaToMethod.this.makePrivateSyntheticMethod(0L, null, null, (Symbol)this.owner.enclClass()); this.translatedSymbols = new EnumMap<>(LambdaSymbolKind.class); this.translatedSymbols.put(LambdaSymbolKind.PARAM, new LinkedHashMap<>()); this.translatedSymbols.put(LambdaSymbolKind.LOCAL_VAR, new LinkedHashMap<>()); this.translatedSymbols.put(LambdaSymbolKind.CAPTURED_VAR, new LinkedHashMap<>()); this.translatedSymbols.put(LambdaSymbolKind.CAPTURED_THIS, new LinkedHashMap<>()); this.translatedSymbols.put(LambdaSymbolKind.CAPTURED_OUTER_THIS, new LinkedHashMap<>()); this.translatedSymbols.put(LambdaSymbolKind.TYPE_VAR, new LinkedHashMap<>()); this.freeVarProcessedLocalClasses = new HashSet<>(); } private String serializedLambdaDisambiguation() { StringBuilder stringBuilder = new StringBuilder(); Assert.check((this.owner.type != null || this.this$1.directlyEnclosingLambda() != null)); if (this.owner.type != null) { stringBuilder.append(LambdaToMethod.this.typeSig(this.owner.type)); stringBuilder.append(":"); }  stringBuilder.append((CharSequence)(LambdaToMethod.this.types.findDescriptorSymbol(this.tree.type.tsym)).owner.flatName()); stringBuilder.append(" "); if (this.assignedTo != null) { stringBuilder.append((CharSequence)this.assignedTo.flatName()); stringBuilder.append("="); }  for (Symbol symbol : getSymbolMap(LambdaSymbolKind.CAPTURED_VAR).keySet()) { if (symbol != this.self) { stringBuilder.append(LambdaToMethod.this.typeSig(symbol.type)); stringBuilder.append(" "); stringBuilder.append((CharSequence)symbol.flatName()); stringBuilder.append(","); }  }  return stringBuilder.toString(); } private Name lambdaName() { return LambdaToMethod.this.names.lambda.append(LambdaToMethod.this.names.fromString(enclosingMethodName() + "$" + this.this$1.lambdaCount++)); } private Name serializedLambdaName() { StringBuilder stringBuilder = new StringBuilder(); stringBuilder.append((CharSequence)LambdaToMethod.this.names.lambda); stringBuilder.append(enclosingMethodName()); stringBuilder.append('$'); String str1 = serializedLambdaDisambiguation(); stringBuilder.append(Integer.toHexString(str1.hashCode())); stringBuilder.append('$'); stringBuilder.append(this.this$1.syntheticMethodNameCounts.getIndex(stringBuilder)); String str2 = stringBuilder.toString(); return LambdaToMethod.this.names.fromString(str2); } Symbol translate(final Symbol sym, LambdaSymbolKind param1LambdaSymbolKind) { Symbol symbol; Symbol.VarSymbol varSymbol; Name name; switch (param1LambdaSymbolKind) { case CAPTURED_THIS: symbol = sym; break;case TYPE_VAR: varSymbol = new Symbol.VarSymbol(sym.flags(), sym.name, LambdaToMethod.this.types.erasure(sym.type), sym.owner); varSymbol.pos = ((Symbol.VarSymbol)sym).pos; break;case CAPTURED_VAR: varSymbol = new Symbol.VarSymbol(8589938704L, sym.name, LambdaToMethod.this.types.erasure(sym.type), (Symbol)this.translatedSym) { public Symbol baseSymbol() { return sym; } }; break;case CAPTURED_OUTER_THIS: name = LambdaToMethod.this.names.fromString(new String(sym.flatName().toString() + LambdaToMethod.this.names.dollarThis)); varSymbol = new Symbol.VarSymbol(8589938704L, name, LambdaToMethod.this.types.erasure(sym.type), (Symbol)this.translatedSym) { public Symbol baseSymbol() { return sym; } }; break;case LOCAL_VAR: varSymbol = new Symbol.VarSymbol(sym.flags() & 0x10L, sym.name, sym.type, (Symbol)this.translatedSym); varSymbol.pos = ((Symbol.VarSymbol)sym).pos; break;case PARAM: varSymbol = new Symbol.VarSymbol(sym.flags() & 0x10L | 0x200000000L, sym.name, LambdaToMethod.this.types.erasure(sym.type), (Symbol)this.translatedSym); varSymbol.pos = ((Symbol.VarSymbol)sym).pos; break;default: Assert.error(param1LambdaSymbolKind.name()); throw new AssertionError(); }  if (varSymbol != sym && param1LambdaSymbolKind.propagateAnnotations()) { varSymbol.setDeclarationAttributes(sym.getRawAttributes()); varSymbol.setTypeAttributes(sym.getRawTypeAttributes()); }  return (Symbol)varSymbol; } void addSymbol(Symbol param1Symbol, LambdaSymbolKind param1LambdaSymbolKind) { if (param1LambdaSymbolKind == LambdaSymbolKind.CAPTURED_THIS && param1Symbol != null && param1Symbol.kind == 2 && !this.this$1.typesUnderConstruction.isEmpty()) { Symbol.ClassSymbol classSymbol = this.this$1.currentClass(); if (classSymbol != null && this.this$1.typesUnderConstruction.contains(classSymbol)) { Assert.check((param1Symbol != classSymbol)); param1LambdaSymbolKind = LambdaSymbolKind.CAPTURED_OUTER_THIS; }  }  Map<Symbol, Symbol> map = getSymbolMap(param1LambdaSymbolKind); if (!map.containsKey(param1Symbol)) map.put(param1Symbol, translate(param1Symbol, param1LambdaSymbolKind));  } Map<Symbol, Symbol> getSymbolMap(LambdaSymbolKind param1LambdaSymbolKind) { Map<Symbol, Symbol> map = this.translatedSymbols.get(param1LambdaSymbolKind); Assert.checkNonNull(map); return map; } JCTree translate(JCTree.JCIdent param1JCIdent) { for (LambdaSymbolKind lambdaSymbolKind : LambdaSymbolKind.values()) { Map<Symbol, Symbol> map = getSymbolMap(lambdaSymbolKind); switch (lambdaSymbolKind) { default: if (map.containsKey(param1JCIdent.sym)) { Symbol symbol = map.get(param1JCIdent.sym); return (JCTree)LambdaToMethod.this.make.Ident(symbol).setType(param1JCIdent.type); }  break;case CAPTURED_OUTER_THIS: if (param1JCIdent.sym.owner.kind == 2 && map.containsKey(param1JCIdent.sym.owner)) { Symbol symbol = map.get(param1JCIdent.sym.owner); JCTree.JCExpression jCExpression = LambdaToMethod.this.make.Ident(symbol).setType(param1JCIdent.sym.owner.type); JCTree.JCFieldAccess jCFieldAccess = LambdaToMethod.this.make.Select(jCExpression, param1JCIdent.name); jCFieldAccess.setType(param1JCIdent.type); TreeInfo.setSymbol((JCTree)jCFieldAccess, param1JCIdent.sym); return (JCTree)jCFieldAccess; }  break; }  }  return null; } public JCTree translate(JCTree.JCFieldAccess param1JCFieldAccess) { Assert.check((param1JCFieldAccess.name == LambdaToMethod.this.names._this)); Map map = this.translatedSymbols.get(LambdaSymbolKind.CAPTURED_OUTER_THIS); if (map.containsKey(param1JCFieldAccess.sym.owner)) { Symbol symbol = (Symbol)map.get(param1JCFieldAccess.sym.owner); return (JCTree)LambdaToMethod.this.make.Ident(symbol).setType(param1JCFieldAccess.sym.owner.type); }  return null; } void complete() { if (this.syntheticParams != null) return;  boolean bool = this.translatedSym.owner.isInterface(); boolean bool1 = !getSymbolMap(LambdaSymbolKind.CAPTURED_THIS).isEmpty() ? true : false; this.translatedSym.flags_field = 0x2000000001000L | this.owner.flags_field & 0x800L | this.owner.owner.flags_field & 0x800L | 0x2L | (bool1 ? (bool ? 8796093022208L : 0L) : 8L); ListBuffer listBuffer1 = new ListBuffer(); ListBuffer listBuffer2 = new ListBuffer(); for (Symbol symbol : getSymbolMap(LambdaSymbolKind.CAPTURED_VAR).values()) { listBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)symbol, null)); listBuffer2.append(symbol); }  for (Symbol symbol : getSymbolMap(LambdaSymbolKind.CAPTURED_OUTER_THIS).values()) { listBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)symbol, null)); listBuffer2.append(symbol); }  for (Symbol symbol : getSymbolMap(LambdaSymbolKind.PARAM).values()) { listBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)symbol, null)); listBuffer2.append(symbol); }  this.syntheticParams = listBuffer1.toList(); this.translatedSym.params = listBuffer2.toList(); this.translatedSym.name = isSerializable() ? serializedLambdaName() : lambdaName(); this.translatedSym.type = LambdaToMethod.this.types.createMethodTypeWithParameters(generatedLambdaSig(), TreeInfo.types(this.syntheticParams)); } Type generatedLambdaSig() { return LambdaToMethod.this.types.erasure(this.tree.getDescriptorType(LambdaToMethod.this.types)); } } private final class ReferenceTranslationContext extends LambdaAnalyzerPreprocessor.TranslationContext<JCTree.JCMemberReference> { final boolean isSuper; final Symbol sigPolySym; ReferenceTranslationContext(JCTree.JCMemberReference param1JCMemberReference) { super((LambdaAnalyzerPreprocessor)LambdaToMethod.this, param1JCMemberReference); this.isSuper = param1JCMemberReference.hasKind(JCTree.JCMemberReference.ReferenceKind.SUPER); this.sigPolySym = isSignaturePolymorphic() ? (Symbol)LambdaToMethod.this.makePrivateSyntheticMethod(param1JCMemberReference.sym.flags(), param1JCMemberReference.sym.name, bridgedRefSig(), (Symbol)param1JCMemberReference.sym.enclClass()) : null; } Type bridgedRefSig() { return LambdaToMethod.this.types.erasure((LambdaToMethod.this.types.findDescriptorSymbol(((Type)this.tree.targets.head).tsym)).type); }
/*      */     int referenceKind() { return LambdaToMethod.this.referenceKind(this.tree.sym); }
/*      */     boolean needsVarArgsConversion() { return (this.tree.varargsElement != null); }
/*      */     boolean isArrayOp() { return (this.tree.sym.owner == LambdaToMethod.this.syms.arrayClass); }
/*      */     boolean receiverAccessible() { return this.tree.ownerAccessible; }
/*      */     boolean isPrivateInOtherClass() { return ((this.tree.sym.flags() & 0x2L) != 0L && !LambdaToMethod.this.types.isSameType(LambdaToMethod.this.types.erasure(this.tree.sym.enclClass().asType()), LambdaToMethod.this.types.erasure(this.owner.enclClass().asType()))); }
/*      */     final boolean isSignaturePolymorphic() { return (this.tree.sym.kind == 16 && LambdaToMethod.this.types.isSignaturePolymorphic((Symbol.MethodSymbol)this.tree.sym)); } boolean interfaceParameterIsIntersectionType() { List list = this.tree.getDescriptorType(LambdaToMethod.this.types).getParameterTypes(); if (this.tree.kind == JCTree.JCMemberReference.ReferenceKind.UNBOUND)
/*      */         list = list.tail;  for (; list.nonEmpty(); list = list.tail) { Type type = (Type)list.head; if (type.getKind() == TypeKind.TYPEVAR) { Type.TypeVar typeVar = (Type.TypeVar)type; if (typeVar.bound.getKind() == TypeKind.INTERSECTION)
/*      */             return true;  }  }  return false; } final boolean needsConversionToLambda() { return (interfaceParameterIsIntersectionType() || this.isSuper || needsVarArgsConversion() || isArrayOp() || isPrivateInOtherClass() || !receiverAccessible() || (this.tree.getMode() == MemberReferenceTree.ReferenceMode.NEW && this.tree.kind != JCTree.JCMemberReference.ReferenceKind.ARRAY_CTOR && (this.tree.sym.owner.isLocal() || this.tree.sym.owner.isInner()))); } Type generatedRefSig() { return LambdaToMethod.this.types.erasure(this.tree.sym.type); } }
/*      */    enum LambdaSymbolKind
/*      */   {
/* 2266 */     PARAM,
/* 2267 */     LOCAL_VAR,
/* 2268 */     CAPTURED_VAR,
/* 2269 */     CAPTURED_THIS,
/* 2270 */     CAPTURED_OUTER_THIS,
/* 2271 */     TYPE_VAR;
/*      */
/*      */     boolean propagateAnnotations() {
/* 2274 */       switch (this) {
/*      */         case CAPTURED_THIS:
/*      */         case CAPTURED_VAR:
/*      */         case CAPTURED_OUTER_THIS:
/* 2278 */           return false;
/*      */       }
/* 2280 */       return true;
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
/*      */   private String typeSig(Type paramType) {
/* 2292 */     L2MSignatureGenerator l2MSignatureGenerator = new L2MSignatureGenerator();
/* 2293 */     l2MSignatureGenerator.assembleSig(paramType);
/* 2294 */     return l2MSignatureGenerator.toString();
/*      */   }
/*      */
/*      */   private String classSig(Type paramType) {
/* 2298 */     L2MSignatureGenerator l2MSignatureGenerator = new L2MSignatureGenerator();
/* 2299 */     l2MSignatureGenerator.assembleClassSig(paramType);
/* 2300 */     return l2MSignatureGenerator.toString();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private class L2MSignatureGenerator
/*      */     extends Types.SignatureGenerator
/*      */   {
/* 2311 */     StringBuilder sb = new StringBuilder();
/*      */
/*      */     L2MSignatureGenerator() {
/* 2314 */       super(LambdaToMethod.this.types);
/*      */     }
/*      */
/*      */
/*      */     protected void append(char param1Char) {
/* 2319 */       this.sb.append(param1Char);
/*      */     }
/*      */
/*      */
/*      */     protected void append(byte[] param1ArrayOfbyte) {
/* 2324 */       this.sb.append(new String(param1ArrayOfbyte));
/*      */     }
/*      */
/*      */
/*      */     protected void append(Name param1Name) {
/* 2329 */       this.sb.append(param1Name.toString());
/*      */     }
/*      */
/*      */
/*      */     public String toString() {
/* 2334 */       return this.sb.toString();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\LambdaToMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
