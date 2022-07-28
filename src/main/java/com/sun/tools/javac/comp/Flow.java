/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.source.tree.LambdaExpressionTree;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.ArrayUtils;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Bits;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.util.HashMap;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class Flow
/*      */ {
/*  184 */   protected static final Context.Key<Flow> flowKey = new Context.Key();
/*      */
/*      */   private final Names names;
/*      */
/*      */   private final Log log;
/*      */   private final Symtab syms;
/*      */   private final Types types;
/*      */   private final Check chk;
/*      */   private TreeMaker make;
/*      */   private final Resolve rs;
/*      */   private final JCDiagnostic.Factory diags;
/*      */   private Env<AttrContext> attrEnv;
/*      */   private Lint lint;
/*      */   private final boolean allowImprovedRethrowAnalysis;
/*      */   private final boolean allowImprovedCatchAnalysis;
/*      */   private final boolean allowEffectivelyFinalInInnerClasses;
/*      */   private final boolean enforceThisDotInit;
/*      */
/*      */   public static Flow instance(Context paramContext) {
/*  203 */     Flow flow = (Flow)paramContext.get(flowKey);
/*  204 */     if (flow == null)
/*  205 */       flow = new Flow(paramContext);
/*  206 */     return flow;
/*      */   }
/*      */
/*      */   public void analyzeTree(Env<AttrContext> paramEnv, TreeMaker paramTreeMaker) {
/*  210 */     (new AliveAnalyzer()).analyzeTree(paramEnv, paramTreeMaker);
/*  211 */     (new AssignAnalyzer()).analyzeTree(paramEnv);
/*  212 */     (new FlowAnalyzer()).analyzeTree(paramEnv, paramTreeMaker);
/*  213 */     (new CaptureAnalyzer()).analyzeTree(paramEnv, paramTreeMaker);
/*      */   }
/*      */
/*      */   public void analyzeLambda(Env<AttrContext> paramEnv, JCTree.JCLambda paramJCLambda, TreeMaker paramTreeMaker, boolean paramBoolean) {
/*  217 */     Log.DiscardDiagnosticHandler discardDiagnosticHandler = null;
/*      */
/*      */
/*      */
/*      */
/*      */
/*  223 */     if (!paramBoolean) {
/*  224 */       discardDiagnosticHandler = new Log.DiscardDiagnosticHandler(this.log);
/*      */     }
/*      */     try {
/*  227 */       (new AliveAnalyzer()).analyzeTree(paramEnv, (JCTree)paramJCLambda, paramTreeMaker);
/*      */     } finally {
/*  229 */       if (!paramBoolean) {
/*  230 */         this.log.popDiagnosticHandler((Log.DiagnosticHandler)discardDiagnosticHandler);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public List<Type> analyzeLambdaThrownTypes(final Env<AttrContext> env, JCTree.JCLambda paramJCLambda, TreeMaker paramTreeMaker) {
/*  242 */     Log.DiscardDiagnosticHandler discardDiagnosticHandler = new Log.DiscardDiagnosticHandler(this.log);
/*      */     try {
/*  244 */       (new AssignAnalyzer() {
/*  245 */           Scope enclosedSymbols = new Scope((Symbol)env.enclClass.sym);
/*      */
/*      */           public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/*  248 */             this.enclosedSymbols.enter((Symbol)param1JCVariableDecl.sym);
/*  249 */             super.visitVarDef(param1JCVariableDecl);
/*      */           }
/*      */
/*      */           protected boolean trackable(Symbol.VarSymbol param1VarSymbol) {
/*  253 */             return (this.enclosedSymbols.includes((Symbol)param1VarSymbol) && param1VarSymbol.owner.kind == 16);
/*      */           }
/*  256 */         }).analyzeTree(env, (JCTree)paramJCLambda);
/*  257 */       LambdaFlowAnalyzer lambdaFlowAnalyzer = new LambdaFlowAnalyzer();
/*  258 */       lambdaFlowAnalyzer.analyzeTree(env, (JCTree)paramJCLambda, paramTreeMaker);
/*  259 */       return lambdaFlowAnalyzer.inferredThrownTypes;
/*      */     } finally {
/*  261 */       this.log.popDiagnosticHandler((Log.DiagnosticHandler)discardDiagnosticHandler);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   enum FlowKind
/*      */   {
/*  272 */     NORMAL("var.might.already.be.assigned", false),
/*      */
/*      */
/*      */
/*      */
/*  277 */     SPECULATIVE_LOOP("var.might.be.assigned.in.loop", true);
/*      */
/*      */     final String errKey;
/*      */     final boolean isFinal;
/*      */
/*      */     FlowKind(String param1String1, boolean param1Boolean) {
/*  283 */       this.errKey = param1String1;
/*  284 */       this.isFinal = param1Boolean;
/*      */     }
/*      */
/*      */     boolean isFinal() {
/*  288 */       return this.isFinal;
/*      */     }
/*      */   }
/*      */
/*      */   protected Flow(Context paramContext) {
/*  293 */     paramContext.put(flowKey, this);
/*  294 */     this.names = Names.instance(paramContext);
/*  295 */     this.log = Log.instance(paramContext);
/*  296 */     this.syms = Symtab.instance(paramContext);
/*  297 */     this.types = Types.instance(paramContext);
/*  298 */     this.chk = Check.instance(paramContext);
/*  299 */     this.lint = Lint.instance(paramContext);
/*  300 */     this.rs = Resolve.instance(paramContext);
/*  301 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  302 */     Source source = Source.instance(paramContext);
/*  303 */     this.allowImprovedRethrowAnalysis = source.allowImprovedRethrowAnalysis();
/*  304 */     this.allowImprovedCatchAnalysis = source.allowImprovedCatchAnalysis();
/*  305 */     this.allowEffectivelyFinalInInnerClasses = source.allowEffectivelyFinalInInnerClasses();
/*  306 */     this.enforceThisDotInit = source.enforceThisDotInit();
/*      */   }
/*      */
/*      */   static abstract class BaseAnalyzer<P extends BaseAnalyzer.PendingExit>
/*      */     extends TreeScanner {
/*      */     ListBuffer<P> pendingExits;
/*      */
/*      */     abstract void markDead();
/*      */
/*      */     enum JumpKind {
/*  316 */       BREAK((String)JCTree.Tag.BREAK)
/*      */       {
/*      */         JCTree getTarget(JCTree param3JCTree) {
/*  319 */           return ((JCTree.JCBreak)param3JCTree).target;
/*      */         }
/*      */       },
/*  322 */       CONTINUE((String)JCTree.Tag.CONTINUE)
/*      */       {
/*      */         JCTree getTarget(JCTree param3JCTree) {
/*  325 */           return ((JCTree.JCContinue)param3JCTree).target;
/*      */         }
/*      */       };
/*      */
/*      */       final JCTree.Tag treeTag;
/*      */
/*      */       JumpKind(JCTree.Tag param2Tag) {
/*  332 */         this.treeTag = param2Tag;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       abstract JCTree getTarget(JCTree param2JCTree);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class PendingExit
/*      */     {
/*      */       JCTree tree;
/*      */
/*      */
/*      */
/*      */
/*      */       PendingExit(JCTree param2JCTree) {
/*  353 */         this.tree = param2JCTree;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       void resolveJump() {}
/*      */     }
/*      */
/*      */
/*      */
/*      */     void recordExit(P param1P) {
/*  365 */       this.pendingExits.append(param1P);
/*  366 */       markDead();
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     private boolean resolveJump(JCTree param1JCTree, ListBuffer<P> param1ListBuffer, JumpKind param1JumpKind) {
/*  373 */       boolean bool = false;
/*  374 */       List list = this.pendingExits.toList();
/*  375 */       this.pendingExits = param1ListBuffer;
/*  376 */       for (; list.nonEmpty(); list = list.tail) {
/*  377 */         PendingExit pendingExit = (PendingExit)list.head;
/*  378 */         if (pendingExit.tree.hasTag(param1JumpKind.treeTag) && param1JumpKind
/*  379 */           .getTarget(pendingExit.tree) == param1JCTree) {
/*  380 */           pendingExit.resolveJump();
/*  381 */           bool = true;
/*      */         } else {
/*  383 */           this.pendingExits.append(pendingExit);
/*      */         }
/*      */       }
/*  386 */       return bool;
/*      */     }
/*      */
/*      */
/*      */     boolean resolveContinues(JCTree param1JCTree) {
/*  391 */       return resolveJump(param1JCTree, new ListBuffer(), JumpKind.CONTINUE);
/*      */     }
/*      */
/*      */
/*      */     boolean resolveBreaks(JCTree param1JCTree, ListBuffer<P> param1ListBuffer) {
/*  396 */       return resolveJump(param1JCTree, param1ListBuffer, JumpKind.BREAK);
/*      */     }
/*      */
/*      */
/*      */     public void scan(JCTree param1JCTree) {
/*  401 */       if (param1JCTree != null && (param1JCTree.type == null || param1JCTree.type != Type.stuckType))
/*      */       {
/*      */
/*  404 */         super.scan(param1JCTree);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   class AliveAnalyzer
/*      */     extends BaseAnalyzer<BaseAnalyzer.PendingExit>
/*      */   {
/*      */     private boolean alive;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void markDead() {
/*  424 */       this.alive = false;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void scanDef(JCTree param1JCTree) {
/*  434 */       scanStat(param1JCTree);
/*  435 */       if (param1JCTree != null && param1JCTree.hasTag(JCTree.Tag.BLOCK) && !this.alive) {
/*  436 */         Flow.this.log.error(param1JCTree.pos(), "initializer.must.be.able.to.complete.normally", new Object[0]);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     void scanStat(JCTree param1JCTree) {
/*  444 */       if (!this.alive && param1JCTree != null) {
/*  445 */         Flow.this.log.error(param1JCTree.pos(), "unreachable.stmt", new Object[0]);
/*  446 */         if (!param1JCTree.hasTag(JCTree.Tag.SKIP)) this.alive = true;
/*      */       }
/*  448 */       scan(param1JCTree);
/*      */     }
/*      */
/*      */
/*      */
/*      */     void scanStats(List<? extends JCTree.JCStatement> param1List) {
/*  454 */       if (param1List != null) {
/*  455 */         for (List<? extends JCTree.JCStatement> list = param1List; list.nonEmpty(); list = list.tail) {
/*  456 */           scanStat((JCTree)list.head);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/*  462 */       if (param1JCClassDecl.sym == null)
/*  463 */         return;  boolean bool = this.alive;
/*  464 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  465 */       Lint lint = Flow.this.lint;
/*      */
/*  467 */       this.pendingExits = new ListBuffer();
/*  468 */       Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCClassDecl.sym);
/*      */
/*      */       try {
/*      */         List list;
/*  472 */         for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/*  473 */           if (!((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF) && (
/*  474 */             TreeInfo.flags((JCTree)list.head) & 0x8L) != 0L) {
/*  475 */             scanDef((JCTree)list.head);
/*      */           }
/*      */         }
/*      */
/*      */
/*  480 */         for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/*  481 */           if (!((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF) && (
/*  482 */             TreeInfo.flags((JCTree)list.head) & 0x8L) == 0L) {
/*  483 */             scanDef((JCTree)list.head);
/*      */           }
/*      */         }
/*      */
/*      */
/*  488 */         for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/*  489 */           if (((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF)) {
/*  490 */             scan((JCTree)list.head);
/*      */           }
/*      */         }
/*      */       } finally {
/*  494 */         this.pendingExits = listBuffer;
/*  495 */         this.alive = bool;
/*  496 */         Flow.this.lint = lint;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/*  501 */       if (param1JCMethodDecl.body == null)
/*  502 */         return;  Lint lint = Flow.this.lint;
/*      */
/*  504 */       Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCMethodDecl.sym);
/*      */
/*  506 */       Assert.check(this.pendingExits.isEmpty());
/*      */
/*      */       try {
/*  509 */         this.alive = true;
/*  510 */         scanStat((JCTree)param1JCMethodDecl.body);
/*      */
/*  512 */         if (this.alive && !param1JCMethodDecl.sym.type.getReturnType().hasTag(TypeTag.VOID)) {
/*  513 */           Flow.this.log.error(TreeInfo.diagEndPos((JCTree)param1JCMethodDecl.body), "missing.ret.stmt", new Object[0]);
/*      */         }
/*  515 */         List list = this.pendingExits.toList();
/*  516 */         this.pendingExits = new ListBuffer();
/*  517 */         while (list.nonEmpty()) {
/*  518 */           PendingExit pendingExit = (PendingExit)list.head;
/*  519 */           list = list.tail;
/*  520 */           Assert.check(pendingExit.tree.hasTag(JCTree.Tag.RETURN));
/*      */         }
/*      */       } finally {
/*  523 */         Flow.this.lint = lint;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/*  528 */       if (param1JCVariableDecl.init != null) {
/*  529 */         Lint lint = Flow.this.lint;
/*  530 */         Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCVariableDecl.sym);
/*      */         try {
/*  532 */           scan((JCTree)param1JCVariableDecl.init);
/*      */         } finally {
/*  534 */           Flow.this.lint = lint;
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     public void visitBlock(JCTree.JCBlock param1JCBlock) {
/*  540 */       scanStats(param1JCBlock.stats);
/*      */     }
/*      */
/*      */     public void visitDoLoop(JCTree.JCDoWhileLoop param1JCDoWhileLoop) {
/*  544 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  545 */       this.pendingExits = new ListBuffer();
/*  546 */       scanStat((JCTree)param1JCDoWhileLoop.body);
/*  547 */       this.alive |= resolveContinues((JCTree)param1JCDoWhileLoop);
/*  548 */       scan((JCTree)param1JCDoWhileLoop.cond);
/*  549 */       this.alive = (this.alive && !param1JCDoWhileLoop.cond.type.isTrue());
/*  550 */       this.alive |= resolveBreaks((JCTree)param1JCDoWhileLoop, listBuffer);
/*      */     }
/*      */
/*      */     public void visitWhileLoop(JCTree.JCWhileLoop param1JCWhileLoop) {
/*  554 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  555 */       this.pendingExits = new ListBuffer();
/*  556 */       scan((JCTree)param1JCWhileLoop.cond);
/*  557 */       this.alive = !param1JCWhileLoop.cond.type.isFalse();
/*  558 */       scanStat((JCTree)param1JCWhileLoop.body);
/*  559 */       this.alive |= resolveContinues((JCTree)param1JCWhileLoop);
/*  560 */       this
/*  561 */         .alive = (resolveBreaks((JCTree)param1JCWhileLoop, listBuffer) || !param1JCWhileLoop.cond.type.isTrue());
/*      */     }
/*      */
/*      */     public void visitForLoop(JCTree.JCForLoop param1JCForLoop) {
/*  565 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  566 */       scanStats(param1JCForLoop.init);
/*  567 */       this.pendingExits = new ListBuffer();
/*  568 */       if (param1JCForLoop.cond != null) {
/*  569 */         scan((JCTree)param1JCForLoop.cond);
/*  570 */         this.alive = !param1JCForLoop.cond.type.isFalse();
/*      */       } else {
/*  572 */         this.alive = true;
/*      */       }
/*  574 */       scanStat((JCTree)param1JCForLoop.body);
/*  575 */       this.alive |= resolveContinues((JCTree)param1JCForLoop);
/*  576 */       scan(param1JCForLoop.step);
/*  577 */       this
/*  578 */         .alive = (resolveBreaks((JCTree)param1JCForLoop, listBuffer) || (param1JCForLoop.cond != null && !param1JCForLoop.cond.type.isTrue()));
/*      */     }
/*      */
/*      */     public void visitForeachLoop(JCTree.JCEnhancedForLoop param1JCEnhancedForLoop) {
/*  582 */       visitVarDef(param1JCEnhancedForLoop.var);
/*  583 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  584 */       scan((JCTree)param1JCEnhancedForLoop.expr);
/*  585 */       this.pendingExits = new ListBuffer();
/*  586 */       scanStat((JCTree)param1JCEnhancedForLoop.body);
/*  587 */       this.alive |= resolveContinues((JCTree)param1JCEnhancedForLoop);
/*  588 */       resolveBreaks((JCTree)param1JCEnhancedForLoop, listBuffer);
/*  589 */       this.alive = true;
/*      */     }
/*      */
/*      */     public void visitLabelled(JCTree.JCLabeledStatement param1JCLabeledStatement) {
/*  593 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  594 */       this.pendingExits = new ListBuffer();
/*  595 */       scanStat((JCTree)param1JCLabeledStatement.body);
/*  596 */       this.alive |= resolveBreaks((JCTree)param1JCLabeledStatement, listBuffer);
/*      */     }
/*      */
/*      */     public void visitSwitch(JCTree.JCSwitch param1JCSwitch) {
/*  600 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  601 */       this.pendingExits = new ListBuffer();
/*  602 */       scan((JCTree)param1JCSwitch.selector);
/*  603 */       boolean bool = false;
/*  604 */       for (List list = param1JCSwitch.cases; list.nonEmpty(); list = list.tail) {
/*  605 */         this.alive = true;
/*  606 */         JCTree.JCCase jCCase = (JCTree.JCCase)list.head;
/*  607 */         if (jCCase.pat == null) {
/*  608 */           bool = true;
/*      */         } else {
/*  610 */           scan((JCTree)jCCase.pat);
/*  611 */         }  scanStats(jCCase.stats);
/*      */
/*  613 */         if (this.alive && Flow.this
/*  614 */           .lint.isEnabled(Lint.LintCategory.FALLTHROUGH) && jCCase.stats
/*  615 */           .nonEmpty() && list.tail.nonEmpty()) {
/*  616 */           Flow.this.log.warning(Lint.LintCategory.FALLTHROUGH, ((JCTree.JCCase)list.tail.head)
/*  617 */               .pos(), "possible.fall-through.into.case", new Object[0]);
/*      */         }
/*      */       }
/*  620 */       if (!bool) {
/*  621 */         this.alive = true;
/*      */       }
/*  623 */       this.alive |= resolveBreaks((JCTree)param1JCSwitch, listBuffer);
/*      */     }
/*      */
/*      */     public void visitTry(JCTree.JCTry param1JCTry) {
/*  627 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  628 */       this.pendingExits = new ListBuffer();
/*  629 */       for (JCTree jCTree : param1JCTry.resources) {
/*  630 */         if (jCTree instanceof JCTree.JCVariableDecl) {
/*  631 */           JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)jCTree;
/*  632 */           visitVarDef(jCVariableDecl); continue;
/*  633 */         }  if (jCTree instanceof JCTree.JCExpression) {
/*  634 */           scan(jCTree); continue;
/*      */         }
/*  636 */         throw new AssertionError(param1JCTry);
/*      */       }
/*      */
/*      */
/*  640 */       scanStat((JCTree)param1JCTry.body);
/*  641 */       boolean bool = this.alive;
/*      */
/*  643 */       for (List list = param1JCTry.catchers; list.nonEmpty(); list = list.tail) {
/*  644 */         this.alive = true;
/*  645 */         JCTree.JCVariableDecl jCVariableDecl = ((JCTree.JCCatch)list.head).param;
/*  646 */         scan((JCTree)jCVariableDecl);
/*  647 */         scanStat((JCTree)((JCTree.JCCatch)list.head).body);
/*  648 */         bool |= this.alive;
/*      */       }
/*  650 */       if (param1JCTry.finalizer != null) {
/*  651 */         ListBuffer<PendingExit> listBuffer1 = this.pendingExits;
/*  652 */         this.pendingExits = listBuffer;
/*  653 */         this.alive = true;
/*  654 */         scanStat((JCTree)param1JCTry.finalizer);
/*  655 */         param1JCTry.finallyCanCompleteNormally = this.alive;
/*  656 */         if (!this.alive) {
/*  657 */           if (Flow.this.lint.isEnabled(Lint.LintCategory.FINALLY)) {
/*  658 */             Flow.this.log.warning(Lint.LintCategory.FINALLY,
/*  659 */                 TreeInfo.diagEndPos((JCTree)param1JCTry.finalizer), "finally.cannot.complete", new Object[0]);
/*      */           }
/*      */         } else {
/*      */
/*  663 */           while (listBuffer1.nonEmpty()) {
/*  664 */             this.pendingExits.append(listBuffer1.next());
/*      */           }
/*  666 */           this.alive = bool;
/*      */         }
/*      */       } else {
/*  669 */         this.alive = bool;
/*  670 */         ListBuffer<PendingExit> listBuffer1 = this.pendingExits;
/*  671 */         this.pendingExits = listBuffer;
/*  672 */         for (; listBuffer1.nonEmpty(); this.pendingExits.append(listBuffer1.next()));
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitIf(JCTree.JCIf param1JCIf) {
/*  678 */       scan((JCTree)param1JCIf.cond);
/*  679 */       scanStat((JCTree)param1JCIf.thenpart);
/*  680 */       if (param1JCIf.elsepart != null) {
/*  681 */         boolean bool = this.alive;
/*  682 */         this.alive = true;
/*  683 */         scanStat((JCTree)param1JCIf.elsepart);
/*  684 */         this.alive |= bool;
/*      */       } else {
/*  686 */         this.alive = true;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitBreak(JCTree.JCBreak param1JCBreak) {
/*  691 */       recordExit(new PendingExit((JCTree)param1JCBreak));
/*      */     }
/*      */
/*      */     public void visitContinue(JCTree.JCContinue param1JCContinue) {
/*  695 */       recordExit(new PendingExit((JCTree)param1JCContinue));
/*      */     }
/*      */
/*      */     public void visitReturn(JCTree.JCReturn param1JCReturn) {
/*  699 */       scan((JCTree)param1JCReturn.expr);
/*  700 */       recordExit(new PendingExit((JCTree)param1JCReturn));
/*      */     }
/*      */
/*      */     public void visitThrow(JCTree.JCThrow param1JCThrow) {
/*  704 */       scan((JCTree)param1JCThrow.expr);
/*  705 */       markDead();
/*      */     }
/*      */
/*      */     public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/*  709 */       scan((JCTree)param1JCMethodInvocation.meth);
/*  710 */       scan(param1JCMethodInvocation.args);
/*      */     }
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/*  714 */       scan((JCTree)param1JCNewClass.encl);
/*  715 */       scan(param1JCNewClass.args);
/*  716 */       if (param1JCNewClass.def != null) {
/*  717 */         scan((JCTree)param1JCNewClass.def);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/*  723 */       if (param1JCLambda.type != null && param1JCLambda.type
/*  724 */         .isErroneous()) {
/*      */         return;
/*      */       }
/*      */
/*  728 */       ListBuffer<PendingExit> listBuffer = this.pendingExits;
/*  729 */       boolean bool = this.alive;
/*      */       try {
/*  731 */         this.pendingExits = new ListBuffer();
/*  732 */         this.alive = true;
/*  733 */         scanStat(param1JCLambda.body);
/*  734 */         param1JCLambda.canCompleteNormally = this.alive;
/*      */       } finally {
/*      */
/*  737 */         this.pendingExits = listBuffer;
/*  738 */         this.alive = bool;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit param1JCCompilationUnit) {}
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void analyzeTree(Env<AttrContext> param1Env, TreeMaker param1TreeMaker) {
/*  753 */       analyzeTree(param1Env, param1Env.tree, param1TreeMaker);
/*      */     }
/*      */     public void analyzeTree(Env<AttrContext> param1Env, JCTree param1JCTree, TreeMaker param1TreeMaker) {
/*      */       try {
/*  757 */         Flow.this.attrEnv = param1Env;
/*  758 */         Flow.this.make = param1TreeMaker;
/*  759 */         this.pendingExits = new ListBuffer();
/*  760 */         this.alive = true;
/*  761 */         scan(param1JCTree);
/*      */       } finally {
/*  763 */         this.pendingExits = null;
/*  764 */         Flow.this.make = null;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   class FlowAnalyzer
/*      */     extends BaseAnalyzer<FlowAnalyzer.FlowPendingExit>
/*      */   {
/*      */     HashMap<Symbol, List<Type>> preciseRethrowTypes;
/*      */
/*      */
/*      */
/*      */     JCTree.JCClassDecl classDef;
/*      */
/*      */
/*      */
/*      */     List<Type> thrown;
/*      */
/*      */
/*      */
/*      */     List<Type> caught;
/*      */
/*      */
/*      */
/*      */
/*      */     class FlowPendingExit
/*      */       extends PendingExit
/*      */     {
/*      */       Type thrown;
/*      */
/*      */
/*      */
/*      */       FlowPendingExit(JCTree param2JCTree, Type param2Type) {
/*  800 */         super(param2JCTree);
/*  801 */         this.thrown = param2Type;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void markDead() {}
/*      */
/*      */
/*      */
/*      */
/*      */     void errorUncaught() {
/*  815 */       FlowPendingExit flowPendingExit = (FlowPendingExit)this.pendingExits.next();
/*  816 */       for (; flowPendingExit != null;
/*  817 */         flowPendingExit = (FlowPendingExit)this.pendingExits.next()) {
/*  818 */         if (this.classDef != null && this.classDef.pos == flowPendingExit.tree.pos) {
/*      */
/*  820 */           Flow.this.log.error(flowPendingExit.tree.pos(), "unreported.exception.default.constructor", new Object[] { flowPendingExit.thrown });
/*      */
/*      */         }
/*  823 */         else if (flowPendingExit.tree.hasTag(JCTree.Tag.VARDEF) && ((JCTree.JCVariableDecl)flowPendingExit.tree).sym
/*  824 */           .isResourceVariable()) {
/*  825 */           Flow.this.log.error(flowPendingExit.tree.pos(), "unreported.exception.implicit.close", new Object[] { flowPendingExit.thrown, ((JCTree.JCVariableDecl)flowPendingExit.tree).sym.name });
/*      */
/*      */         }
/*      */         else {
/*      */
/*  830 */           Flow.this.log.error(flowPendingExit.tree.pos(), "unreported.exception.need.to.catch.or.throw", new Object[] { flowPendingExit.thrown });
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void markThrown(JCTree param1JCTree, Type param1Type) {
/*  841 */       if (!Flow.this.chk.isUnchecked(param1JCTree.pos(), param1Type)) {
/*  842 */         if (!Flow.this.chk.isHandled(param1Type, this.caught)) {
/*  843 */           this.pendingExits.append(new FlowPendingExit(param1JCTree, param1Type));
/*      */         }
/*  845 */         this.thrown = Flow.this.chk.incl(param1Type, this.thrown);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/*  856 */       if (param1JCClassDecl.sym == null)
/*      */         return;
/*  858 */       JCTree.JCClassDecl jCClassDecl = this.classDef;
/*  859 */       List<Type> list1 = this.thrown;
/*  860 */       List<Type> list2 = this.caught;
/*  861 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/*  862 */       Lint lint = Flow.this.lint;
/*      */
/*  864 */       this.pendingExits = new ListBuffer();
/*  865 */       if (param1JCClassDecl.name != Flow.this.names.empty) {
/*  866 */         this.caught = List.nil();
/*      */       }
/*  868 */       this.classDef = param1JCClassDecl;
/*  869 */       this.thrown = List.nil();
/*  870 */       Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCClassDecl.sym);
/*      */
/*      */       try {
/*      */         List list;
/*  874 */         for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/*  875 */           if (!((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF) && (
/*  876 */             TreeInfo.flags((JCTree)list.head) & 0x8L) != 0L) {
/*  877 */             scan((JCTree)list.head);
/*  878 */             errorUncaught();
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*  884 */         if (param1JCClassDecl.name != Flow.this.names.empty) {
/*  885 */           boolean bool = true;
/*  886 */           for (List list3 = param1JCClassDecl.defs; list3.nonEmpty(); list3 = list3.tail) {
/*  887 */             if (TreeInfo.isInitialConstructor((JCTree)list3.head)) {
/*      */
/*  889 */               List<Type> list4 = ((JCTree.JCMethodDecl)list3.head).sym.type.getThrownTypes();
/*  890 */               if (bool) {
/*  891 */                 this.caught = list4;
/*  892 */                 bool = false;
/*      */               } else {
/*  894 */                 this.caught = Flow.this.chk.intersect(list4, this.caught);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*  901 */         for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/*  902 */           if (!((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF) && (
/*  903 */             TreeInfo.flags((JCTree)list.head) & 0x8L) == 0L) {
/*  904 */             scan((JCTree)list.head);
/*  905 */             errorUncaught();
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  915 */         if (param1JCClassDecl.name == Flow.this.names.empty) {
/*  916 */           for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/*  917 */             if (TreeInfo.isInitialConstructor((JCTree)list.head)) {
/*  918 */               JCTree.JCMethodDecl jCMethodDecl = (JCTree.JCMethodDecl)list.head;
/*  919 */               jCMethodDecl.thrown = Flow.this.make.Types(this.thrown);
/*  920 */               jCMethodDecl.sym.type = Flow.this.types.createMethodTypeWithThrown(jCMethodDecl.sym.type, this.thrown);
/*      */             }
/*      */           }
/*  923 */           list1 = Flow.this.chk.union(this.thrown, list1);
/*      */         }
/*      */
/*      */
/*  927 */         for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/*  928 */           if (((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF)) {
/*  929 */             scan((JCTree)list.head);
/*  930 */             errorUncaught();
/*      */           }
/*      */         }
/*      */
/*  934 */         this.thrown = list1;
/*      */       } finally {
/*  936 */         this.pendingExits = listBuffer;
/*  937 */         this.caught = list2;
/*  938 */         this.classDef = jCClassDecl;
/*  939 */         Flow.this.lint = lint;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/*  944 */       if (param1JCMethodDecl.body == null)
/*      */         return;
/*  946 */       List<Type> list1 = this.caught;
/*  947 */       List<Type> list2 = param1JCMethodDecl.sym.type.getThrownTypes();
/*  948 */       Lint lint = Flow.this.lint;
/*      */
/*  950 */       Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCMethodDecl.sym);
/*      */
/*  952 */       Assert.check(this.pendingExits.isEmpty());
/*      */       try {
/*      */         List list;
/*  955 */         for (list = param1JCMethodDecl.params; list.nonEmpty(); list = list.tail) {
/*  956 */           JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list.head;
/*  957 */           scan((JCTree)jCVariableDecl);
/*      */         }
/*  959 */         if (TreeInfo.isInitialConstructor((JCTree)param1JCMethodDecl)) {
/*  960 */           this.caught = Flow.this.chk.union(this.caught, list2);
/*  961 */         } else if ((param1JCMethodDecl.sym.flags() & 0x100008L) != 1048576L) {
/*  962 */           this.caught = list2;
/*      */         }
/*      */
/*      */
/*  966 */         scan((JCTree)param1JCMethodDecl.body);
/*      */
/*  968 */         list = this.pendingExits.toList();
/*  969 */         this.pendingExits = new ListBuffer();
/*  970 */         while (list.nonEmpty()) {
/*  971 */           FlowPendingExit flowPendingExit = (FlowPendingExit)list.head;
/*  972 */           list = list.tail;
/*  973 */           if (flowPendingExit.thrown == null) {
/*  974 */             Assert.check(flowPendingExit.tree.hasTag(JCTree.Tag.RETURN));
/*      */             continue;
/*      */           }
/*  977 */           this.pendingExits.append(flowPendingExit);
/*      */         }
/*      */       } finally {
/*      */
/*  981 */         this.caught = list1;
/*  982 */         Flow.this.lint = lint;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/*  987 */       if (param1JCVariableDecl.init != null) {
/*  988 */         Lint lint = Flow.this.lint;
/*  989 */         Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCVariableDecl.sym);
/*      */         try {
/*  991 */           scan((JCTree)param1JCVariableDecl.init);
/*      */         } finally {
/*  993 */           Flow.this.lint = lint;
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     public void visitBlock(JCTree.JCBlock param1JCBlock) {
/*  999 */       scan(param1JCBlock.stats);
/*      */     }
/*      */
/*      */     public void visitDoLoop(JCTree.JCDoWhileLoop param1JCDoWhileLoop) {
/* 1003 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1004 */       this.pendingExits = new ListBuffer();
/* 1005 */       scan((JCTree)param1JCDoWhileLoop.body);
/* 1006 */       resolveContinues((JCTree)param1JCDoWhileLoop);
/* 1007 */       scan((JCTree)param1JCDoWhileLoop.cond);
/* 1008 */       resolveBreaks((JCTree)param1JCDoWhileLoop, listBuffer);
/*      */     }
/*      */
/*      */     public void visitWhileLoop(JCTree.JCWhileLoop param1JCWhileLoop) {
/* 1012 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1013 */       this.pendingExits = new ListBuffer();
/* 1014 */       scan((JCTree)param1JCWhileLoop.cond);
/* 1015 */       scan((JCTree)param1JCWhileLoop.body);
/* 1016 */       resolveContinues((JCTree)param1JCWhileLoop);
/* 1017 */       resolveBreaks((JCTree)param1JCWhileLoop, listBuffer);
/*      */     }
/*      */
/*      */     public void visitForLoop(JCTree.JCForLoop param1JCForLoop) {
/* 1021 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1022 */       scan(param1JCForLoop.init);
/* 1023 */       this.pendingExits = new ListBuffer();
/* 1024 */       if (param1JCForLoop.cond != null) {
/* 1025 */         scan((JCTree)param1JCForLoop.cond);
/*      */       }
/* 1027 */       scan((JCTree)param1JCForLoop.body);
/* 1028 */       resolveContinues((JCTree)param1JCForLoop);
/* 1029 */       scan(param1JCForLoop.step);
/* 1030 */       resolveBreaks((JCTree)param1JCForLoop, listBuffer);
/*      */     }
/*      */
/*      */     public void visitForeachLoop(JCTree.JCEnhancedForLoop param1JCEnhancedForLoop) {
/* 1034 */       visitVarDef(param1JCEnhancedForLoop.var);
/* 1035 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1036 */       scan((JCTree)param1JCEnhancedForLoop.expr);
/* 1037 */       this.pendingExits = new ListBuffer();
/* 1038 */       scan((JCTree)param1JCEnhancedForLoop.body);
/* 1039 */       resolveContinues((JCTree)param1JCEnhancedForLoop);
/* 1040 */       resolveBreaks((JCTree)param1JCEnhancedForLoop, listBuffer);
/*      */     }
/*      */
/*      */     public void visitLabelled(JCTree.JCLabeledStatement param1JCLabeledStatement) {
/* 1044 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1045 */       this.pendingExits = new ListBuffer();
/* 1046 */       scan((JCTree)param1JCLabeledStatement.body);
/* 1047 */       resolveBreaks((JCTree)param1JCLabeledStatement, listBuffer);
/*      */     }
/*      */
/*      */     public void visitSwitch(JCTree.JCSwitch param1JCSwitch) {
/* 1051 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1052 */       this.pendingExits = new ListBuffer();
/* 1053 */       scan((JCTree)param1JCSwitch.selector);
/* 1054 */       for (List list = param1JCSwitch.cases; list.nonEmpty(); list = list.tail) {
/* 1055 */         JCTree.JCCase jCCase = (JCTree.JCCase)list.head;
/* 1056 */         if (jCCase.pat != null) {
/* 1057 */           scan((JCTree)jCCase.pat);
/*      */         }
/* 1059 */         scan(jCCase.stats);
/*      */       }
/* 1061 */       resolveBreaks((JCTree)param1JCSwitch, listBuffer);
/*      */     }
/*      */
/*      */     public void visitTry(JCTree.JCTry param1JCTry) {
/* 1065 */       List<Type> list1 = this.caught;
/* 1066 */       List<Type> list2 = this.thrown;
/* 1067 */       this.thrown = List.nil();
/* 1068 */       for (List list = param1JCTry.catchers; list.nonEmpty(); list = list.tail) {
/*      */
/*      */
/* 1071 */         List list6 = TreeInfo.isMultiCatch((JCTree.JCCatch)list.head) ? ((JCTree.JCTypeUnion)((JCTree.JCCatch)list.head).param.vartype).alternatives : List.of(((JCTree.JCCatch)list.head).param.vartype);
/* 1072 */         for (JCTree.JCExpression jCExpression : list6) {
/* 1073 */           this.caught = Flow.this.chk.incl(jCExpression.type, this.caught);
/*      */         }
/*      */       }
/*      */
/* 1077 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1078 */       this.pendingExits = new ListBuffer();
/* 1079 */       for (JCTree jCTree : param1JCTry.resources) {
/* 1080 */         if (jCTree instanceof JCTree.JCVariableDecl) {
/* 1081 */           JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)jCTree;
/* 1082 */           visitVarDef(jCVariableDecl); continue;
/* 1083 */         }  if (jCTree instanceof JCTree.JCExpression) {
/* 1084 */           scan(jCTree); continue;
/*      */         }
/* 1086 */         throw new AssertionError(param1JCTry);
/*      */       }
/*      */
/* 1089 */       for (JCTree jCTree : param1JCTry.resources) {
/*      */
/*      */
/* 1092 */         List list6 = jCTree.type.isCompound() ? Flow.this.types.interfaces(jCTree.type).prepend(Flow.this.types.supertype(jCTree.type)) : List.of(jCTree.type);
/* 1093 */         for (Type type : list6) {
/* 1094 */           if (Flow.this.types.asSuper(type, (Symbol)Flow.this.syms.autoCloseableType.tsym) != null) {
/* 1095 */             Symbol symbol = Flow.this.rs.resolveQualifiedMethod((JCDiagnostic.DiagnosticPosition)param1JCTry, Flow.this
/* 1096 */                 .attrEnv, type,
/*      */
/* 1098 */                 Flow.this.names.close,
/* 1099 */                 List.nil(),
/* 1100 */                 List.nil());
/* 1101 */             Type type1 = Flow.this.types.memberType(jCTree.type, symbol);
/* 1102 */             if (symbol.kind == 16) {
/* 1103 */               for (Type type2 : type1.getThrownTypes()) {
/* 1104 */                 markThrown(jCTree, type2);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1110 */       scan((JCTree)param1JCTry.body);
/*      */
/* 1112 */       List<Type> list3 = Flow.this.allowImprovedCatchAnalysis ? Flow.this.chk.union(this.thrown, List.of(Flow.this.syms.runtimeExceptionType, Flow.this.syms.errorType)) : this.thrown;
/*      */
/* 1114 */       this.thrown = list2;
/* 1115 */       this.caught = list1;
/*      */
/* 1117 */       List<Type> list4 = List.nil(); List<Type> list5;
/* 1118 */       for (list5 = param1JCTry.catchers; list5.nonEmpty(); list5 = list5.tail) {
/* 1119 */         JCTree.JCVariableDecl jCVariableDecl = ((JCTree.JCCatch)list5.head).param;
/*      */
/*      */
/* 1122 */         List list6 = TreeInfo.isMultiCatch((JCTree.JCCatch)list5.head) ? ((JCTree.JCTypeUnion)((JCTree.JCCatch)list5.head).param.vartype).alternatives : List.of(((JCTree.JCCatch)list5.head).param.vartype);
/* 1123 */         List<Type> list7 = List.nil();
/* 1124 */         List<Type> list8 = Flow.this.chk.diff(list3, list4);
/* 1125 */         for (JCTree.JCExpression jCExpression : list6) {
/* 1126 */           Type type = jCExpression.type;
/* 1127 */           if (type != Flow.this.syms.unknownType) {
/* 1128 */             list7 = list7.append(type);
/* 1129 */             if (Flow.this.types.isSameType(type, Flow.this.syms.objectType))
/*      */               continue;
/* 1131 */             checkCaughtType(((JCTree.JCCatch)list5.head).pos(), type, list3, list4);
/* 1132 */             list4 = Flow.this.chk.incl(type, list4);
/*      */           }
/*      */         }
/* 1135 */         scan((JCTree)jCVariableDecl);
/* 1136 */         this.preciseRethrowTypes.put(jCVariableDecl.sym, Flow.this.chk.intersect(list7, list8));
/* 1137 */         scan((JCTree)((JCTree.JCCatch)list5.head).body);
/* 1138 */         this.preciseRethrowTypes.remove(jCVariableDecl.sym);
/*      */       }
/* 1140 */       if (param1JCTry.finalizer != null) {
/* 1141 */         list5 = this.thrown;
/* 1142 */         this.thrown = List.nil();
/* 1143 */         ListBuffer<FlowPendingExit> listBuffer1 = this.pendingExits;
/* 1144 */         this.pendingExits = listBuffer;
/* 1145 */         scan((JCTree)param1JCTry.finalizer);
/* 1146 */         if (!param1JCTry.finallyCanCompleteNormally) {
/*      */
/* 1148 */           this.thrown = Flow.this.chk.union(this.thrown, list2);
/*      */         } else {
/* 1150 */           this.thrown = Flow.this.chk.union(this.thrown, Flow.this.chk.diff(list3, list4));
/* 1151 */           this.thrown = Flow.this.chk.union(this.thrown, list5);
/*      */
/*      */
/* 1154 */           while (listBuffer1.nonEmpty()) {
/* 1155 */             this.pendingExits.append(listBuffer1.next());
/*      */           }
/*      */         }
/*      */       } else {
/* 1159 */         this.thrown = Flow.this.chk.union(this.thrown, Flow.this.chk.diff(list3, list4));
/* 1160 */         ListBuffer<FlowPendingExit> listBuffer1 = this.pendingExits;
/* 1161 */         this.pendingExits = listBuffer;
/* 1162 */         for (; listBuffer1.nonEmpty(); this.pendingExits.append(listBuffer1.next()));
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitIf(JCTree.JCIf param1JCIf) {
/* 1168 */       scan((JCTree)param1JCIf.cond);
/* 1169 */       scan((JCTree)param1JCIf.thenpart);
/* 1170 */       if (param1JCIf.elsepart != null) {
/* 1171 */         scan((JCTree)param1JCIf.elsepart);
/*      */       }
/*      */     }
/*      */
/*      */     void checkCaughtType(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Type param1Type, List<Type> param1List1, List<Type> param1List2) {
/* 1176 */       if (Flow.this.chk.subset(param1Type, param1List2)) {
/* 1177 */         Flow.this.log.error(param1DiagnosticPosition, "except.already.caught", new Object[] { param1Type });
/* 1178 */       } else if (!Flow.this.chk.isUnchecked(param1DiagnosticPosition, param1Type) &&
/* 1179 */         !isExceptionOrThrowable(param1Type) &&
/* 1180 */         !Flow.this.chk.intersects(param1Type, param1List1)) {
/* 1181 */         Flow.this.log.error(param1DiagnosticPosition, "except.never.thrown.in.try", new Object[] { param1Type });
/* 1182 */       } else if (Flow.this.allowImprovedCatchAnalysis) {
/* 1183 */         List<Type> list = Flow.this.chk.intersect(List.of(param1Type), param1List1);
/*      */
/*      */
/*      */
/*      */
/* 1188 */         if (Flow.this.chk.diff(list, param1List2).isEmpty() &&
/* 1189 */           !isExceptionOrThrowable(param1Type)) {
/* 1190 */           String str = (list.length() == 1) ? "unreachable.catch" : "unreachable.catch.1";
/*      */
/*      */
/* 1193 */           Flow.this.log.warning(param1DiagnosticPosition, str, new Object[] { list });
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     private boolean isExceptionOrThrowable(Type param1Type) {
/* 1199 */       return (param1Type.tsym == Flow.this.syms.throwableType.tsym || param1Type.tsym ==
/* 1200 */         Flow.this.syms.exceptionType.tsym);
/*      */     }
/*      */
/*      */     public void visitBreak(JCTree.JCBreak param1JCBreak) {
/* 1204 */       recordExit(new FlowPendingExit((JCTree)param1JCBreak, null));
/*      */     }
/*      */
/*      */     public void visitContinue(JCTree.JCContinue param1JCContinue) {
/* 1208 */       recordExit(new FlowPendingExit((JCTree)param1JCContinue, null));
/*      */     }
/*      */
/*      */     public void visitReturn(JCTree.JCReturn param1JCReturn) {
/* 1212 */       scan((JCTree)param1JCReturn.expr);
/* 1213 */       recordExit(new FlowPendingExit((JCTree)param1JCReturn, null));
/*      */     }
/*      */
/*      */     public void visitThrow(JCTree.JCThrow param1JCThrow) {
/* 1217 */       scan((JCTree)param1JCThrow.expr);
/* 1218 */       Symbol symbol = TreeInfo.symbol((JCTree)param1JCThrow.expr);
/* 1219 */       if (symbol != null && symbol.kind == 4 && (symbol
/*      */
/* 1221 */         .flags() & 0x20000000010L) != 0L && this.preciseRethrowTypes
/* 1222 */         .get(symbol) != null && Flow.this
/* 1223 */         .allowImprovedRethrowAnalysis) {
/* 1224 */         for (Type type : this.preciseRethrowTypes.get(symbol)) {
/* 1225 */           markThrown((JCTree)param1JCThrow, type);
/*      */         }
/*      */       } else {
/*      */
/* 1229 */         markThrown((JCTree)param1JCThrow, param1JCThrow.expr.type);
/*      */       }
/* 1231 */       markDead();
/*      */     }
/*      */
/*      */     public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/* 1235 */       scan((JCTree)param1JCMethodInvocation.meth);
/* 1236 */       scan(param1JCMethodInvocation.args);
/* 1237 */       for (List list = param1JCMethodInvocation.meth.type.getThrownTypes(); list.nonEmpty(); list = list.tail)
/* 1238 */         markThrown((JCTree)param1JCMethodInvocation, (Type)list.head);
/*      */     }
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 1242 */       scan((JCTree)param1JCNewClass.encl);
/* 1243 */       scan(param1JCNewClass.args);
/*      */
/* 1245 */       List<Type> list = param1JCNewClass.constructorType.getThrownTypes();
/* 1246 */       for (; list.nonEmpty();
/* 1247 */         list = list.tail) {
/* 1248 */         markThrown((JCTree)param1JCNewClass, (Type)list.head);
/*      */       }
/* 1250 */       list = this.caught;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       try {
/* 1260 */         if (param1JCNewClass.def != null) {
/* 1261 */           List list1 = param1JCNewClass.constructor.type.getThrownTypes();
/* 1262 */           for (; list1.nonEmpty();
/* 1263 */             list1 = list1.tail)
/* 1264 */             this.caught = Flow.this.chk.incl((Type)list1.head, this.caught);
/*      */         }
/* 1266 */         scan((JCTree)param1JCNewClass.def);
/*      */       } finally {
/*      */
/* 1269 */         this.caught = list;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/* 1275 */       if (param1JCLambda.type != null && param1JCLambda.type
/* 1276 */         .isErroneous()) {
/*      */         return;
/*      */       }
/* 1279 */       List<Type> list1 = this.caught;
/* 1280 */       List<Type> list2 = this.thrown;
/* 1281 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/*      */       try {
/* 1283 */         this.pendingExits = new ListBuffer();
/* 1284 */         this.caught = param1JCLambda.getDescriptorType(Flow.this.types).getThrownTypes();
/* 1285 */         this.thrown = List.nil();
/* 1286 */         scan(param1JCLambda.body);
/* 1287 */         List list = this.pendingExits.toList();
/* 1288 */         this.pendingExits = new ListBuffer();
/* 1289 */         while (list.nonEmpty()) {
/* 1290 */           FlowPendingExit flowPendingExit = (FlowPendingExit)list.head;
/* 1291 */           list = list.tail;
/* 1292 */           if (flowPendingExit.thrown == null) {
/* 1293 */             Assert.check(flowPendingExit.tree.hasTag(JCTree.Tag.RETURN));
/*      */             continue;
/*      */           }
/* 1296 */           this.pendingExits.append(flowPendingExit);
/*      */         }
/*      */
/*      */
/* 1300 */         errorUncaught();
/*      */       } finally {
/* 1302 */         this.pendingExits = listBuffer;
/* 1303 */         this.caught = list1;
/* 1304 */         this.thrown = list2;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit param1JCCompilationUnit) {}
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void analyzeTree(Env<AttrContext> param1Env, TreeMaker param1TreeMaker) {
/* 1319 */       analyzeTree(param1Env, param1Env.tree, param1TreeMaker);
/*      */     }
/*      */     public void analyzeTree(Env<AttrContext> param1Env, JCTree param1JCTree, TreeMaker param1TreeMaker) {
/*      */       try {
/* 1323 */         Flow.this.attrEnv = param1Env;
/* 1324 */         Flow.this.make = param1TreeMaker;
/* 1325 */         this.pendingExits = new ListBuffer();
/* 1326 */         this.preciseRethrowTypes = new HashMap<>();
/* 1327 */         this.thrown = this.caught = null;
/* 1328 */         this.classDef = null;
/* 1329 */         scan(param1JCTree);
/*      */       } finally {
/* 1331 */         this.pendingExits = null;
/* 1332 */         Flow.this.make = null;
/* 1333 */         this.thrown = this.caught = null;
/* 1334 */         this.classDef = null;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   class LambdaFlowAnalyzer
/*      */     extends FlowAnalyzer
/*      */   {
/*      */     List<Type> inferredThrownTypes;
/*      */     boolean inLambda;
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/* 1347 */       if ((param1JCLambda.type != null && param1JCLambda.type
/* 1348 */         .isErroneous()) || this.inLambda) {
/*      */         return;
/*      */       }
/* 1351 */       List<Type> list1 = this.caught;
/* 1352 */       List<Type> list2 = this.thrown;
/* 1353 */       ListBuffer<FlowPendingExit> listBuffer = this.pendingExits;
/* 1354 */       this.inLambda = true;
/*      */       try {
/* 1356 */         this.pendingExits = new ListBuffer();
/* 1357 */         this.caught = List.of(Flow.this.syms.throwableType);
/* 1358 */         this.thrown = List.nil();
/* 1359 */         scan(param1JCLambda.body);
/* 1360 */         this.inferredThrownTypes = this.thrown;
/*      */       } finally {
/* 1362 */         this.pendingExits = listBuffer;
/* 1363 */         this.caught = list1;
/* 1364 */         this.thrown = list2;
/* 1365 */         this.inLambda = false;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {}
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public class AssignAnalyzer
/*      */     extends BaseAnalyzer<AssignAnalyzer.AssignPendingExit>
/*      */   {
/*      */     final Bits inits;
/*      */
/*      */
/*      */
/*      */     final Bits uninits;
/*      */
/*      */
/*      */
/*      */     final Bits uninitsTry;
/*      */
/*      */
/*      */
/*      */     final Bits initsWhenTrue;
/*      */
/*      */
/*      */
/*      */     final Bits initsWhenFalse;
/*      */
/*      */
/*      */
/*      */     final Bits uninitsWhenTrue;
/*      */
/*      */
/*      */
/*      */     final Bits uninitsWhenFalse;
/*      */
/*      */
/*      */
/*      */     protected JCTree.JCVariableDecl[] vardecls;
/*      */
/*      */
/*      */
/*      */     JCTree.JCClassDecl classDef;
/*      */
/*      */
/*      */
/*      */     int firstadr;
/*      */
/*      */
/*      */
/*      */     protected int nextadr;
/*      */
/*      */
/*      */
/*      */     protected int returnadr;
/*      */
/*      */
/*      */
/*      */     Scope unrefdResources;
/*      */
/*      */
/*      */
/* 1434 */     FlowKind flowKind = FlowKind.NORMAL;
/*      */     int startPos;
/*      */     private boolean isInitialConstructor;
/*      */
/*      */     public class AssignPendingExit
/*      */       extends PendingExit
/*      */     {
/*      */       final Bits inits;
/*      */       final Bits uninits;
/* 1443 */       final Bits exit_inits = new Bits(true);
/* 1444 */       final Bits exit_uninits = new Bits(true);
/*      */
/*      */       public AssignPendingExit(JCTree param2JCTree, Bits param2Bits1, Bits param2Bits2) {
/* 1447 */         super(param2JCTree);
/* 1448 */         this.inits = param2Bits1;
/* 1449 */         this.uninits = param2Bits2;
/* 1450 */         this.exit_inits.assign(param2Bits1);
/* 1451 */         this.exit_uninits.assign(param2Bits2);
/*      */       }
/*      */
/*      */
/*      */       void resolveJump() {
/* 1456 */         this.inits.andSet(this.exit_inits);
/* 1457 */         this.uninits.andSet(this.exit_uninits);
/*      */       }
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
/*      */     public AssignAnalyzer() {
/* 1471 */       this.isInitialConstructor = false; this.inits = new Bits(); this.uninits = new Bits(); this.uninitsTry = new Bits();
/*      */       this.initsWhenTrue = new Bits(true);
/*      */       this.initsWhenFalse = new Bits(true);
/*      */       this.uninitsWhenTrue = new Bits(true);
/* 1475 */       this.uninitsWhenFalse = new Bits(true); } void markDead() { if (!this.isInitialConstructor) {
/* 1476 */         this.inits.inclRange(this.returnadr, this.nextadr);
/*      */       } else {
/* 1478 */         for (int i = this.returnadr; i < this.nextadr; i++) {
/* 1479 */           if (!isFinalUninitializedStaticField((this.vardecls[i]).sym)) {
/* 1480 */             this.inits.incl(i);
/*      */           }
/*      */         }
/*      */       }
/* 1484 */       this.uninits.inclRange(this.returnadr, this.nextadr); }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     protected boolean trackable(Symbol.VarSymbol param1VarSymbol) {
/* 1493 */       return (param1VarSymbol.pos >= this.startPos && (param1VarSymbol.owner.kind == 16 ||
/*      */
/*      */
/* 1496 */         isFinalUninitializedField(param1VarSymbol)));
/*      */     }
/*      */
/*      */     boolean isFinalUninitializedField(Symbol.VarSymbol param1VarSymbol) {
/* 1500 */       return (param1VarSymbol.owner.kind == 2 && (param1VarSymbol
/* 1501 */         .flags() & 0x200040010L) == 16L && this.classDef.sym
/* 1502 */         .isEnclosedBy((Symbol.ClassSymbol)param1VarSymbol.owner));
/*      */     }
/*      */
/*      */     boolean isFinalUninitializedStaticField(Symbol.VarSymbol param1VarSymbol) {
/* 1506 */       return (isFinalUninitializedField(param1VarSymbol) && param1VarSymbol.isStatic());
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void newVar(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 1514 */       Symbol.VarSymbol varSymbol = param1JCVariableDecl.sym;
/* 1515 */       this.vardecls = (JCTree.JCVariableDecl[])ArrayUtils.ensureCapacity((Object[])this.vardecls, this.nextadr);
/* 1516 */       if ((varSymbol.flags() & 0x10L) == 0L) {
/* 1517 */         varSymbol.flags_field |= 0x20000000000L;
/*      */       }
/* 1519 */       varSymbol.adr = this.nextadr;
/* 1520 */       this.vardecls[this.nextadr] = param1JCVariableDecl;
/* 1521 */       this.inits.excl(this.nextadr);
/* 1522 */       this.uninits.incl(this.nextadr);
/* 1523 */       this.nextadr++;
/*      */     }
/*      */
/*      */
/*      */
/*      */     void letInit(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol.VarSymbol param1VarSymbol) {
/* 1529 */       if (param1VarSymbol.adr >= this.firstadr && trackable(param1VarSymbol)) {
/* 1530 */         if ((param1VarSymbol.flags() & 0x20000000000L) != 0L) {
/* 1531 */           if (!this.uninits.isMember(param1VarSymbol.adr)) {
/*      */
/*      */
/*      */
/* 1535 */             param1VarSymbol.flags_field &= 0xFFFFFDFFFFFFFFFFL;
/*      */           } else {
/* 1537 */             uninit(param1VarSymbol);
/*      */           }
/* 1539 */         } else if ((param1VarSymbol.flags() & 0x10L) != 0L) {
/* 1540 */           if ((param1VarSymbol.flags() & 0x200000000L) != 0L) {
/* 1541 */             if ((param1VarSymbol.flags() & 0x8000000000L) != 0L) {
/* 1542 */               Flow.this.log.error(param1DiagnosticPosition, "multicatch.parameter.may.not.be.assigned", new Object[] { param1VarSymbol });
/*      */             } else {
/* 1544 */               Flow.this.log.error(param1DiagnosticPosition, "final.parameter.may.not.be.assigned", new Object[] { param1VarSymbol });
/*      */             }
/*      */
/* 1547 */           } else if (!this.uninits.isMember(param1VarSymbol.adr)) {
/* 1548 */             Flow.this.log.error(param1DiagnosticPosition, this.flowKind.errKey, new Object[] { param1VarSymbol });
/*      */           } else {
/* 1550 */             uninit(param1VarSymbol);
/*      */           }
/*      */         }
/* 1553 */         this.inits.incl(param1VarSymbol.adr);
/* 1554 */       } else if ((param1VarSymbol.flags() & 0x10L) != 0L) {
/* 1555 */         Flow.this.log.error(param1DiagnosticPosition, "var.might.already.be.assigned", new Object[] { param1VarSymbol });
/*      */       }
/*      */     }
/*      */
/*      */     void uninit(Symbol.VarSymbol param1VarSymbol) {
/* 1560 */       if (!this.inits.isMember(param1VarSymbol.adr)) {
/*      */
/* 1562 */         this.uninits.excl(param1VarSymbol.adr);
/* 1563 */         this.uninitsTry.excl(param1VarSymbol.adr);
/*      */       } else {
/*      */
/* 1566 */         this.uninits.excl(param1VarSymbol.adr);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void letInit(JCTree param1JCTree) {
/* 1575 */       param1JCTree = TreeInfo.skipParens(param1JCTree);
/* 1576 */       if (param1JCTree.hasTag(JCTree.Tag.IDENT) || param1JCTree.hasTag(JCTree.Tag.SELECT)) {
/* 1577 */         Symbol symbol = TreeInfo.symbol(param1JCTree);
/* 1578 */         if (symbol.kind == 4) {
/* 1579 */           letInit(param1JCTree.pos(), (Symbol.VarSymbol)symbol);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     void checkInit(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol.VarSymbol param1VarSymbol) {
/* 1587 */       checkInit(param1DiagnosticPosition, param1VarSymbol, "var.might.not.have.been.initialized");
/*      */     }
/*      */
/*      */     void checkInit(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol.VarSymbol param1VarSymbol, String param1String) {
/* 1591 */       if ((param1VarSymbol.adr >= this.firstadr || param1VarSymbol.owner.kind != 2) &&
/* 1592 */         trackable(param1VarSymbol) &&
/* 1593 */         !this.inits.isMember(param1VarSymbol.adr)) {
/* 1594 */         Flow.this.log.error(param1DiagnosticPosition, param1String, new Object[] { param1VarSymbol });
/* 1595 */         this.inits.incl(param1VarSymbol.adr);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     private void resetBits(Bits... param1VarArgs) {
/* 1602 */       for (Bits bits : param1VarArgs) {
/* 1603 */         bits.reset();
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     void split(boolean param1Boolean) {
/* 1610 */       this.initsWhenFalse.assign(this.inits);
/* 1611 */       this.uninitsWhenFalse.assign(this.uninits);
/* 1612 */       this.initsWhenTrue.assign(this.inits);
/* 1613 */       this.uninitsWhenTrue.assign(this.uninits);
/* 1614 */       if (param1Boolean) {
/* 1615 */         resetBits(new Bits[] { this.inits, this.uninits });
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     protected void merge() {
/* 1622 */       this.inits.assign(this.initsWhenFalse.andSet(this.initsWhenTrue));
/* 1623 */       this.uninits.assign(this.uninitsWhenFalse.andSet(this.uninitsWhenTrue));
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void scanExpr(JCTree param1JCTree) {
/* 1634 */       if (param1JCTree != null) {
/* 1635 */         scan(param1JCTree);
/* 1636 */         if (this.inits.isReset()) {
/* 1637 */           merge();
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     void scanExprs(List<? extends JCTree.JCExpression> param1List) {
/* 1645 */       if (param1List != null) {
/* 1646 */         for (List<? extends JCTree.JCExpression> list = param1List; list.nonEmpty(); list = list.tail) {
/* 1647 */           scanExpr((JCTree)list.head);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     void scanCond(JCTree param1JCTree) {
/* 1654 */       if (param1JCTree.type.isFalse()) {
/* 1655 */         if (this.inits.isReset()) merge();
/* 1656 */         this.initsWhenTrue.assign(this.inits);
/* 1657 */         this.initsWhenTrue.inclRange(this.firstadr, this.nextadr);
/* 1658 */         this.uninitsWhenTrue.assign(this.uninits);
/* 1659 */         this.uninitsWhenTrue.inclRange(this.firstadr, this.nextadr);
/* 1660 */         this.initsWhenFalse.assign(this.inits);
/* 1661 */         this.uninitsWhenFalse.assign(this.uninits);
/* 1662 */       } else if (param1JCTree.type.isTrue()) {
/* 1663 */         if (this.inits.isReset()) merge();
/* 1664 */         this.initsWhenFalse.assign(this.inits);
/* 1665 */         this.initsWhenFalse.inclRange(this.firstadr, this.nextadr);
/* 1666 */         this.uninitsWhenFalse.assign(this.uninits);
/* 1667 */         this.uninitsWhenFalse.inclRange(this.firstadr, this.nextadr);
/* 1668 */         this.initsWhenTrue.assign(this.inits);
/* 1669 */         this.uninitsWhenTrue.assign(this.uninits);
/*      */       } else {
/* 1671 */         scan(param1JCTree);
/* 1672 */         if (!this.inits.isReset())
/* 1673 */           split((param1JCTree.type != Flow.this.syms.unknownType));
/*      */       }
/* 1675 */       if (param1JCTree.type != Flow.this.syms.unknownType) {
/* 1676 */         resetBits(new Bits[] { this.inits, this.uninits });
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/* 1683 */       if (param1JCClassDecl.sym == null) {
/*      */         return;
/*      */       }
/*      */
/* 1687 */       Lint lint = Flow.this.lint;
/* 1688 */       Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCClassDecl.sym);
/*      */       try {
/* 1690 */         if (param1JCClassDecl.sym == null) {
/*      */           return;
/*      */         }
/*      */
/* 1694 */         JCTree.JCClassDecl jCClassDecl = this.classDef;
/* 1695 */         int i = this.firstadr;
/* 1696 */         int j = this.nextadr;
/* 1697 */         ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/*      */
/* 1699 */         this.pendingExits = new ListBuffer();
/* 1700 */         if (param1JCClassDecl.name != Flow.this.names.empty) {
/* 1701 */           this.firstadr = this.nextadr;
/*      */         }
/* 1703 */         this.classDef = param1JCClassDecl;
/*      */         try {
/*      */           List list;
/* 1706 */           for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 1707 */             if (((JCTree)list.head).hasTag(JCTree.Tag.VARDEF)) {
/* 1708 */               JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list.head;
/* 1709 */               if ((jCVariableDecl.mods.flags & 0x8L) != 0L) {
/* 1710 */                 Symbol.VarSymbol varSymbol = jCVariableDecl.sym;
/* 1711 */                 if (trackable(varSymbol)) {
/* 1712 */                   newVar(jCVariableDecl);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */
/*      */
/* 1719 */           for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 1720 */             if (!((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF) && (
/* 1721 */               TreeInfo.flags((JCTree)list.head) & 0x8L) != 0L) {
/* 1722 */               scan((JCTree)list.head);
/*      */             }
/*      */           }
/*      */
/*      */
/* 1727 */           for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 1728 */             if (((JCTree)list.head).hasTag(JCTree.Tag.VARDEF)) {
/* 1729 */               JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list.head;
/* 1730 */               if ((jCVariableDecl.mods.flags & 0x8L) == 0L) {
/* 1731 */                 Symbol.VarSymbol varSymbol = jCVariableDecl.sym;
/* 1732 */                 if (trackable(varSymbol)) {
/* 1733 */                   newVar(jCVariableDecl);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */
/* 1739 */           for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 1740 */             if (!((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF) && (
/* 1741 */               TreeInfo.flags((JCTree)list.head) & 0x8L) == 0L) {
/* 1742 */               scan((JCTree)list.head);
/*      */             }
/*      */           }
/*      */
/*      */
/* 1747 */           for (list = param1JCClassDecl.defs; list.nonEmpty(); list = list.tail) {
/* 1748 */             if (((JCTree)list.head).hasTag(JCTree.Tag.METHODDEF)) {
/* 1749 */               scan((JCTree)list.head);
/*      */             }
/*      */           }
/*      */         } finally {
/* 1753 */           this.pendingExits = listBuffer;
/* 1754 */           this.nextadr = j;
/* 1755 */           this.firstadr = i;
/* 1756 */           this.classDef = jCClassDecl;
/*      */         }
/*      */       } finally {
/* 1759 */         Flow.this.lint = lint;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/* 1764 */       if (param1JCMethodDecl.body == null) {
/*      */         return;
/*      */       }
/*      */
/*      */
/*      */
/* 1770 */       if ((param1JCMethodDecl.sym.flags() & 0x1000L) != 0L) {
/*      */         return;
/*      */       }
/*      */
/* 1774 */       Lint lint = Flow.this.lint;
/* 1775 */       Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCMethodDecl.sym);
/*      */       try {
/* 1777 */         if (param1JCMethodDecl.body == null) {
/*      */           return;
/*      */         }
/*      */
/*      */
/* 1782 */         if ((param1JCMethodDecl.sym.flags() & 0x2000000001000L) == 4096L) {
/*      */           return;
/*      */         }
/*      */
/* 1786 */         Bits bits1 = new Bits(this.inits);
/* 1787 */         Bits bits2 = new Bits(this.uninits);
/* 1788 */         int i = this.nextadr;
/* 1789 */         int j = this.firstadr;
/* 1790 */         int k = this.returnadr;
/*      */
/* 1792 */         Assert.check(this.pendingExits.isEmpty());
/* 1793 */         boolean bool = this.isInitialConstructor;
/*      */         try {
/* 1795 */           this.isInitialConstructor = TreeInfo.isInitialConstructor((JCTree)param1JCMethodDecl);
/*      */
/* 1797 */           if (!this.isInitialConstructor)
/* 1798 */             this.firstadr = this.nextadr;
/*      */           List list;
/* 1800 */           for (list = param1JCMethodDecl.params; list.nonEmpty(); list = list.tail) {
/* 1801 */             JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list.head;
/* 1802 */             scan((JCTree)jCVariableDecl);
/* 1803 */             Assert.check(((jCVariableDecl.sym.flags() & 0x200000000L) != 0L), "Method parameter without PARAMETER flag");
/*      */
/*      */
/*      */
/* 1807 */             initParam(jCVariableDecl);
/*      */           }
/*      */
/*      */
/* 1811 */           scan((JCTree)param1JCMethodDecl.body);
/*      */
/* 1813 */           if (this.isInitialConstructor) {
/* 1814 */             boolean bool1 = ((param1JCMethodDecl.sym.flags() & 0x1000000000L) != 0L) ? true : false;
/*      */
/* 1816 */             for (int m = this.firstadr; m < this.nextadr; m++) {
/* 1817 */               JCTree.JCVariableDecl jCVariableDecl = this.vardecls[m];
/* 1818 */               Symbol.VarSymbol varSymbol = jCVariableDecl.sym;
/* 1819 */               if (varSymbol.owner == this.classDef.sym)
/*      */               {
/*      */
/* 1822 */                 if (bool1) {
/* 1823 */                   checkInit(TreeInfo.diagnosticPositionFor((Symbol)varSymbol, (JCTree)jCVariableDecl), varSymbol, "var.not.initialized.in.default.constructor");
/*      */                 } else {
/*      */
/* 1826 */                   checkInit(TreeInfo.diagEndPos((JCTree)param1JCMethodDecl.body), varSymbol);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/* 1831 */           list = this.pendingExits.toList();
/* 1832 */           this.pendingExits = new ListBuffer();
/* 1833 */           while (list.nonEmpty()) {
/* 1834 */             AssignPendingExit assignPendingExit = (AssignPendingExit)list.head;
/* 1835 */             list = list.tail;
/* 1836 */             Assert.check(assignPendingExit.tree.hasTag(JCTree.Tag.RETURN), assignPendingExit.tree);
/* 1837 */             if (this.isInitialConstructor) {
/* 1838 */               this.inits.assign(assignPendingExit.exit_inits);
/* 1839 */               for (int m = this.firstadr; m < this.nextadr; m++) {
/* 1840 */                 checkInit(assignPendingExit.tree.pos(), (this.vardecls[m]).sym);
/*      */               }
/*      */             }
/*      */           }
/*      */         } finally {
/* 1845 */           this.inits.assign(bits1);
/* 1846 */           this.uninits.assign(bits2);
/* 1847 */           this.nextadr = i;
/* 1848 */           this.firstadr = j;
/* 1849 */           this.returnadr = k;
/* 1850 */           this.isInitialConstructor = bool;
/*      */         }
/*      */       } finally {
/* 1853 */         Flow.this.lint = lint;
/*      */       }
/*      */     }
/*      */
/*      */     protected void initParam(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 1858 */       this.inits.incl(param1JCVariableDecl.sym.adr);
/* 1859 */       this.uninits.excl(param1JCVariableDecl.sym.adr);
/*      */     }
/*      */
/*      */     public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 1863 */       Lint lint = Flow.this.lint;
/* 1864 */       Flow.this.lint = Flow.this.lint.augment((Symbol)param1JCVariableDecl.sym);
/*      */       try {
/* 1866 */         boolean bool = trackable(param1JCVariableDecl.sym);
/* 1867 */         if (bool && param1JCVariableDecl.sym.owner.kind == 16) {
/* 1868 */           newVar(param1JCVariableDecl);
/*      */         }
/* 1870 */         if (param1JCVariableDecl.init != null) {
/* 1871 */           scanExpr((JCTree)param1JCVariableDecl.init);
/* 1872 */           if (bool) {
/* 1873 */             letInit(param1JCVariableDecl.pos(), param1JCVariableDecl.sym);
/*      */           }
/*      */         }
/*      */       } finally {
/* 1877 */         Flow.this.lint = lint;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitBlock(JCTree.JCBlock param1JCBlock) {
/* 1882 */       int i = this.nextadr;
/* 1883 */       scan(param1JCBlock.stats);
/* 1884 */       this.nextadr = i;
/*      */     }
/*      */
/*      */     public void visitDoLoop(JCTree.JCDoWhileLoop param1JCDoWhileLoop) {
/* 1888 */       ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/* 1889 */       FlowKind flowKind = this.flowKind;
/* 1890 */       this.flowKind = FlowKind.NORMAL;
/* 1891 */       Bits bits1 = new Bits(true);
/* 1892 */       Bits bits2 = new Bits(true);
/* 1893 */       this.pendingExits = new ListBuffer();
/* 1894 */       int i = Flow.this.log.nerrors;
/*      */       while (true) {
/* 1896 */         Bits bits = new Bits(this.uninits);
/* 1897 */         bits.excludeFrom(this.nextadr);
/* 1898 */         scan((JCTree)param1JCDoWhileLoop.body);
/* 1899 */         resolveContinues((JCTree)param1JCDoWhileLoop);
/* 1900 */         scanCond((JCTree)param1JCDoWhileLoop.cond);
/* 1901 */         if (!this.flowKind.isFinal()) {
/* 1902 */           bits1.assign(this.initsWhenFalse);
/* 1903 */           bits2.assign(this.uninitsWhenFalse);
/*      */         }
/* 1905 */         if (Flow.this.log.nerrors != i || this.flowKind
/* 1906 */           .isFinal() || (new Bits(bits))
/* 1907 */           .diffSet(this.uninitsWhenTrue).nextBit(this.firstadr) == -1)
/*      */           break;
/* 1909 */         this.inits.assign(this.initsWhenTrue);
/* 1910 */         this.uninits.assign(bits.andSet(this.uninitsWhenTrue));
/* 1911 */         this.flowKind = FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 1913 */       this.flowKind = flowKind;
/* 1914 */       this.inits.assign(bits1);
/* 1915 */       this.uninits.assign(bits2);
/* 1916 */       resolveBreaks((JCTree)param1JCDoWhileLoop, listBuffer);
/*      */     }
/*      */
/*      */     public void visitWhileLoop(JCTree.JCWhileLoop param1JCWhileLoop) {
/* 1920 */       ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/* 1921 */       FlowKind flowKind = this.flowKind;
/* 1922 */       this.flowKind = FlowKind.NORMAL;
/* 1923 */       Bits bits1 = new Bits(true);
/* 1924 */       Bits bits2 = new Bits(true);
/* 1925 */       this.pendingExits = new ListBuffer();
/* 1926 */       int i = Flow.this.log.nerrors;
/* 1927 */       Bits bits3 = new Bits(this.uninits);
/* 1928 */       bits3.excludeFrom(this.nextadr);
/*      */       while (true) {
/* 1930 */         scanCond((JCTree)param1JCWhileLoop.cond);
/* 1931 */         if (!this.flowKind.isFinal()) {
/* 1932 */           bits1.assign(this.initsWhenFalse);
/* 1933 */           bits2.assign(this.uninitsWhenFalse);
/*      */         }
/* 1935 */         this.inits.assign(this.initsWhenTrue);
/* 1936 */         this.uninits.assign(this.uninitsWhenTrue);
/* 1937 */         scan((JCTree)param1JCWhileLoop.body);
/* 1938 */         resolveContinues((JCTree)param1JCWhileLoop);
/* 1939 */         if (Flow.this.log.nerrors != i || this.flowKind
/* 1940 */           .isFinal() || (new Bits(bits3))
/* 1941 */           .diffSet(this.uninits).nextBit(this.firstadr) == -1) {
/*      */           break;
/*      */         }
/* 1944 */         this.uninits.assign(bits3.andSet(this.uninits));
/* 1945 */         this.flowKind = FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 1947 */       this.flowKind = flowKind;
/*      */
/*      */
/* 1950 */       this.inits.assign(bits1);
/* 1951 */       this.uninits.assign(bits2);
/* 1952 */       resolveBreaks((JCTree)param1JCWhileLoop, listBuffer);
/*      */     }
/*      */
/*      */     public void visitForLoop(JCTree.JCForLoop param1JCForLoop) {
/* 1956 */       ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/* 1957 */       FlowKind flowKind = this.flowKind;
/* 1958 */       this.flowKind = FlowKind.NORMAL;
/* 1959 */       int i = this.nextadr;
/* 1960 */       scan(param1JCForLoop.init);
/* 1961 */       Bits bits1 = new Bits(true);
/* 1962 */       Bits bits2 = new Bits(true);
/* 1963 */       this.pendingExits = new ListBuffer();
/* 1964 */       int j = Flow.this.log.nerrors;
/*      */       while (true) {
/* 1966 */         Bits bits = new Bits(this.uninits);
/* 1967 */         bits.excludeFrom(this.nextadr);
/* 1968 */         if (param1JCForLoop.cond != null) {
/* 1969 */           scanCond((JCTree)param1JCForLoop.cond);
/* 1970 */           if (!this.flowKind.isFinal()) {
/* 1971 */             bits1.assign(this.initsWhenFalse);
/* 1972 */             bits2.assign(this.uninitsWhenFalse);
/*      */           }
/* 1974 */           this.inits.assign(this.initsWhenTrue);
/* 1975 */           this.uninits.assign(this.uninitsWhenTrue);
/* 1976 */         } else if (!this.flowKind.isFinal()) {
/* 1977 */           bits1.assign(this.inits);
/* 1978 */           bits1.inclRange(this.firstadr, this.nextadr);
/* 1979 */           bits2.assign(this.uninits);
/* 1980 */           bits2.inclRange(this.firstadr, this.nextadr);
/*      */         }
/* 1982 */         scan((JCTree)param1JCForLoop.body);
/* 1983 */         resolveContinues((JCTree)param1JCForLoop);
/* 1984 */         scan(param1JCForLoop.step);
/* 1985 */         if (Flow.this.log.nerrors != j || this.flowKind
/* 1986 */           .isFinal() || (new Bits(bits))
/* 1987 */           .diffSet(this.uninits).nextBit(this.firstadr) == -1)
/*      */           break;
/* 1989 */         this.uninits.assign(bits.andSet(this.uninits));
/* 1990 */         this.flowKind = FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 1992 */       this.flowKind = flowKind;
/*      */
/*      */
/* 1995 */       this.inits.assign(bits1);
/* 1996 */       this.uninits.assign(bits2);
/* 1997 */       resolveBreaks((JCTree)param1JCForLoop, listBuffer);
/* 1998 */       this.nextadr = i;
/*      */     }
/*      */
/*      */     public void visitForeachLoop(JCTree.JCEnhancedForLoop param1JCEnhancedForLoop) {
/* 2002 */       visitVarDef(param1JCEnhancedForLoop.var);
/*      */
/* 2004 */       ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/* 2005 */       FlowKind flowKind = this.flowKind;
/* 2006 */       this.flowKind = FlowKind.NORMAL;
/* 2007 */       int i = this.nextadr;
/* 2008 */       scan((JCTree)param1JCEnhancedForLoop.expr);
/* 2009 */       Bits bits1 = new Bits(this.inits);
/* 2010 */       Bits bits2 = new Bits(this.uninits);
/*      */
/* 2012 */       letInit(param1JCEnhancedForLoop.pos(), param1JCEnhancedForLoop.var.sym);
/* 2013 */       this.pendingExits = new ListBuffer();
/* 2014 */       int j = Flow.this.log.nerrors;
/*      */       while (true) {
/* 2016 */         Bits bits = new Bits(this.uninits);
/* 2017 */         bits.excludeFrom(this.nextadr);
/* 2018 */         scan((JCTree)param1JCEnhancedForLoop.body);
/* 2019 */         resolveContinues((JCTree)param1JCEnhancedForLoop);
/* 2020 */         if (Flow.this.log.nerrors != j || this.flowKind
/* 2021 */           .isFinal() || (new Bits(bits))
/* 2022 */           .diffSet(this.uninits).nextBit(this.firstadr) == -1)
/*      */           break;
/* 2024 */         this.uninits.assign(bits.andSet(this.uninits));
/* 2025 */         this.flowKind = FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 2027 */       this.flowKind = flowKind;
/* 2028 */       this.inits.assign(bits1);
/* 2029 */       this.uninits.assign(bits2.andSet(this.uninits));
/* 2030 */       resolveBreaks((JCTree)param1JCEnhancedForLoop, listBuffer);
/* 2031 */       this.nextadr = i;
/*      */     }
/*      */
/*      */     public void visitLabelled(JCTree.JCLabeledStatement param1JCLabeledStatement) {
/* 2035 */       ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/* 2036 */       this.pendingExits = new ListBuffer();
/* 2037 */       scan((JCTree)param1JCLabeledStatement.body);
/* 2038 */       resolveBreaks((JCTree)param1JCLabeledStatement, listBuffer);
/*      */     }
/*      */
/*      */     public void visitSwitch(JCTree.JCSwitch param1JCSwitch) {
/* 2042 */       ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/* 2043 */       this.pendingExits = new ListBuffer();
/* 2044 */       int i = this.nextadr;
/* 2045 */       scanExpr((JCTree)param1JCSwitch.selector);
/* 2046 */       Bits bits1 = new Bits(this.inits);
/* 2047 */       Bits bits2 = new Bits(this.uninits);
/* 2048 */       boolean bool = false;
/* 2049 */       for (List list = param1JCSwitch.cases; list.nonEmpty(); list = list.tail) {
/* 2050 */         this.inits.assign(bits1);
/* 2051 */         this.uninits.assign(this.uninits.andSet(bits2));
/* 2052 */         JCTree.JCCase jCCase = (JCTree.JCCase)list.head;
/* 2053 */         if (jCCase.pat == null) {
/* 2054 */           bool = true;
/*      */         } else {
/* 2056 */           scanExpr((JCTree)jCCase.pat);
/*      */         }
/* 2058 */         if (bool) {
/* 2059 */           this.inits.assign(bits1);
/* 2060 */           this.uninits.assign(this.uninits.andSet(bits2));
/*      */         }
/* 2062 */         scan(jCCase.stats);
/* 2063 */         addVars(jCCase.stats, bits1, bits2);
/* 2064 */         if (!bool) {
/* 2065 */           this.inits.assign(bits1);
/* 2066 */           this.uninits.assign(this.uninits.andSet(bits2));
/*      */         }
/*      */       }
/*      */
/* 2070 */       if (!bool) {
/* 2071 */         this.inits.andSet(bits1);
/*      */       }
/* 2073 */       resolveBreaks((JCTree)param1JCSwitch, listBuffer);
/* 2074 */       this.nextadr = i;
/*      */     }
/*      */
/*      */
/*      */
/*      */     private void addVars(List<JCTree.JCStatement> param1List, Bits param1Bits1, Bits param1Bits2) {
/* 2080 */       for (; param1List.nonEmpty(); param1List = param1List.tail) {
/* 2081 */         JCTree jCTree = (JCTree)param1List.head;
/* 2082 */         if (jCTree.hasTag(JCTree.Tag.VARDEF)) {
/* 2083 */           int i = ((JCTree.JCVariableDecl)jCTree).sym.adr;
/* 2084 */           param1Bits1.excl(i);
/* 2085 */           param1Bits2.incl(i);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     public void visitTry(JCTree.JCTry param1JCTry) {
/* 2091 */       ListBuffer listBuffer = new ListBuffer();
/* 2092 */       Bits bits1 = new Bits(this.uninitsTry);
/* 2093 */       ListBuffer<AssignPendingExit> listBuffer1 = this.pendingExits;
/* 2094 */       this.pendingExits = new ListBuffer();
/* 2095 */       Bits bits2 = new Bits(this.inits);
/* 2096 */       this.uninitsTry.assign(this.uninits);
/* 2097 */       for (JCTree jCTree : param1JCTry.resources) {
/* 2098 */         if (jCTree instanceof JCTree.JCVariableDecl) {
/* 2099 */           JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)jCTree;
/* 2100 */           visitVarDef(jCVariableDecl);
/* 2101 */           this.unrefdResources.enter((Symbol)jCVariableDecl.sym);
/* 2102 */           listBuffer.append(jCVariableDecl); continue;
/* 2103 */         }  if (jCTree instanceof JCTree.JCExpression) {
/* 2104 */           scanExpr(jCTree); continue;
/*      */         }
/* 2106 */         throw new AssertionError(param1JCTry);
/*      */       }
/*      */
/* 2109 */       scan((JCTree)param1JCTry.body);
/* 2110 */       this.uninitsTry.andSet(this.uninits);
/* 2111 */       Bits bits3 = new Bits(this.inits);
/* 2112 */       Bits bits4 = new Bits(this.uninits);
/* 2113 */       int i = this.nextadr;
/*      */
/* 2115 */       if (!listBuffer.isEmpty() && Flow.this
/* 2116 */         .lint.isEnabled(Lint.LintCategory.TRY)) {
/* 2117 */         for (JCTree.JCVariableDecl jCVariableDecl : listBuffer) {
/* 2118 */           if (this.unrefdResources.includes((Symbol)jCVariableDecl.sym)) {
/* 2119 */             Flow.this.log.warning(Lint.LintCategory.TRY, jCVariableDecl.pos(), "try.resource.not.referenced", new Object[] { jCVariableDecl.sym });
/*      */
/* 2121 */             this.unrefdResources.remove((Symbol)jCVariableDecl.sym);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2130 */       Bits bits5 = new Bits(bits2);
/* 2131 */       Bits bits6 = new Bits(this.uninitsTry);
/*      */
/* 2133 */       for (List list = param1JCTry.catchers; list.nonEmpty(); list = list.tail) {
/* 2134 */         JCTree.JCVariableDecl jCVariableDecl = ((JCTree.JCCatch)list.head).param;
/* 2135 */         this.inits.assign(bits5);
/* 2136 */         this.uninits.assign(bits6);
/* 2137 */         scan((JCTree)jCVariableDecl);
/*      */
/*      */
/*      */
/* 2141 */         initParam(jCVariableDecl);
/* 2142 */         scan((JCTree)((JCTree.JCCatch)list.head).body);
/* 2143 */         bits3.andSet(this.inits);
/* 2144 */         bits4.andSet(this.uninits);
/* 2145 */         this.nextadr = i;
/*      */       }
/* 2147 */       if (param1JCTry.finalizer != null) {
/* 2148 */         this.inits.assign(bits2);
/* 2149 */         this.uninits.assign(this.uninitsTry);
/* 2150 */         ListBuffer<AssignPendingExit> listBuffer2 = this.pendingExits;
/* 2151 */         this.pendingExits = listBuffer1;
/* 2152 */         scan((JCTree)param1JCTry.finalizer);
/* 2153 */         if (param1JCTry.finallyCanCompleteNormally) {
/*      */
/*      */
/* 2156 */           this.uninits.andSet(bits4);
/*      */
/*      */
/* 2159 */           while (listBuffer2.nonEmpty()) {
/* 2160 */             AssignPendingExit assignPendingExit = (AssignPendingExit)listBuffer2.next();
/* 2161 */             if (assignPendingExit.exit_inits != null) {
/* 2162 */               assignPendingExit.exit_inits.orSet(this.inits);
/* 2163 */               assignPendingExit.exit_uninits.andSet(this.uninits);
/*      */             }
/* 2165 */             this.pendingExits.append(assignPendingExit);
/*      */           }
/* 2167 */           this.inits.orSet(bits3);
/*      */         }
/*      */       } else {
/* 2170 */         this.inits.assign(bits3);
/* 2171 */         this.uninits.assign(bits4);
/* 2172 */         ListBuffer<AssignPendingExit> listBuffer2 = this.pendingExits;
/* 2173 */         this.pendingExits = listBuffer1;
/* 2174 */         for (; listBuffer2.nonEmpty(); this.pendingExits.append(listBuffer2.next()));
/*      */       }
/* 2176 */       this.uninitsTry.andSet(bits1).andSet(this.uninits);
/*      */     }
/*      */
/*      */     public void visitConditional(JCTree.JCConditional param1JCConditional) {
/* 2180 */       scanCond((JCTree)param1JCConditional.cond);
/* 2181 */       Bits bits1 = new Bits(this.initsWhenFalse);
/* 2182 */       Bits bits2 = new Bits(this.uninitsWhenFalse);
/* 2183 */       this.inits.assign(this.initsWhenTrue);
/* 2184 */       this.uninits.assign(this.uninitsWhenTrue);
/* 2185 */       if (param1JCConditional.truepart.type.hasTag(TypeTag.BOOLEAN) && param1JCConditional.falsepart.type
/* 2186 */         .hasTag(TypeTag.BOOLEAN)) {
/*      */
/*      */
/*      */
/*      */
/* 2191 */         scanCond((JCTree)param1JCConditional.truepart);
/* 2192 */         Bits bits3 = new Bits(this.initsWhenTrue);
/* 2193 */         Bits bits4 = new Bits(this.initsWhenFalse);
/* 2194 */         Bits bits5 = new Bits(this.uninitsWhenTrue);
/* 2195 */         Bits bits6 = new Bits(this.uninitsWhenFalse);
/* 2196 */         this.inits.assign(bits1);
/* 2197 */         this.uninits.assign(bits2);
/* 2198 */         scanCond((JCTree)param1JCConditional.falsepart);
/* 2199 */         this.initsWhenTrue.andSet(bits3);
/* 2200 */         this.initsWhenFalse.andSet(bits4);
/* 2201 */         this.uninitsWhenTrue.andSet(bits5);
/* 2202 */         this.uninitsWhenFalse.andSet(bits6);
/*      */       } else {
/* 2204 */         scanExpr((JCTree)param1JCConditional.truepart);
/* 2205 */         Bits bits3 = new Bits(this.inits);
/* 2206 */         Bits bits4 = new Bits(this.uninits);
/* 2207 */         this.inits.assign(bits1);
/* 2208 */         this.uninits.assign(bits2);
/* 2209 */         scanExpr((JCTree)param1JCConditional.falsepart);
/* 2210 */         this.inits.andSet(bits3);
/* 2211 */         this.uninits.andSet(bits4);
/*      */       }
/*      */     }
/*      */
/*      */     public void visitIf(JCTree.JCIf param1JCIf) {
/* 2216 */       scanCond((JCTree)param1JCIf.cond);
/* 2217 */       Bits bits1 = new Bits(this.initsWhenFalse);
/* 2218 */       Bits bits2 = new Bits(this.uninitsWhenFalse);
/* 2219 */       this.inits.assign(this.initsWhenTrue);
/* 2220 */       this.uninits.assign(this.uninitsWhenTrue);
/* 2221 */       scan((JCTree)param1JCIf.thenpart);
/* 2222 */       if (param1JCIf.elsepart != null) {
/* 2223 */         Bits bits3 = new Bits(this.inits);
/* 2224 */         Bits bits4 = new Bits(this.uninits);
/* 2225 */         this.inits.assign(bits1);
/* 2226 */         this.uninits.assign(bits2);
/* 2227 */         scan((JCTree)param1JCIf.elsepart);
/* 2228 */         this.inits.andSet(bits3);
/* 2229 */         this.uninits.andSet(bits4);
/*      */       } else {
/* 2231 */         this.inits.andSet(bits1);
/* 2232 */         this.uninits.andSet(bits2);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitBreak(JCTree.JCBreak param1JCBreak) {
/* 2238 */       recordExit(new AssignPendingExit((JCTree)param1JCBreak, this.inits, this.uninits));
/*      */     }
/*      */
/*      */
/*      */     public void visitContinue(JCTree.JCContinue param1JCContinue) {
/* 2243 */       recordExit(new AssignPendingExit((JCTree)param1JCContinue, this.inits, this.uninits));
/*      */     }
/*      */
/*      */
/*      */     public void visitReturn(JCTree.JCReturn param1JCReturn) {
/* 2248 */       scanExpr((JCTree)param1JCReturn.expr);
/* 2249 */       recordExit(new AssignPendingExit((JCTree)param1JCReturn, this.inits, this.uninits));
/*      */     }
/*      */
/*      */     public void visitThrow(JCTree.JCThrow param1JCThrow) {
/* 2253 */       scanExpr((JCTree)param1JCThrow.expr);
/* 2254 */       markDead();
/*      */     }
/*      */
/*      */     public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/* 2258 */       scanExpr((JCTree)param1JCMethodInvocation.meth);
/* 2259 */       scanExprs(param1JCMethodInvocation.args);
/*      */     }
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 2263 */       scanExpr((JCTree)param1JCNewClass.encl);
/* 2264 */       scanExprs(param1JCNewClass.args);
/* 2265 */       scan((JCTree)param1JCNewClass.def);
/*      */     }
/*      */
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/* 2270 */       Bits bits1 = new Bits(this.uninits);
/* 2271 */       Bits bits2 = new Bits(this.inits);
/* 2272 */       int i = this.returnadr;
/* 2273 */       ListBuffer<AssignPendingExit> listBuffer = this.pendingExits;
/*      */       try {
/* 2275 */         this.returnadr = this.nextadr;
/* 2276 */         this.pendingExits = new ListBuffer();
/* 2277 */         for (List list = param1JCLambda.params; list.nonEmpty(); list = list.tail) {
/* 2278 */           JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list.head;
/* 2279 */           scan((JCTree)jCVariableDecl);
/* 2280 */           this.inits.incl(jCVariableDecl.sym.adr);
/* 2281 */           this.uninits.excl(jCVariableDecl.sym.adr);
/*      */         }
/* 2283 */         if (param1JCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION) {
/* 2284 */           scanExpr(param1JCLambda.body);
/*      */         } else {
/* 2286 */           scan(param1JCLambda.body);
/*      */         }
/*      */       } finally {
/*      */
/* 2290 */         this.returnadr = i;
/* 2291 */         this.uninits.assign(bits1);
/* 2292 */         this.inits.assign(bits2);
/* 2293 */         this.pendingExits = listBuffer;
/*      */       }
/*      */     }
/*      */
/*      */     public void visitNewArray(JCTree.JCNewArray param1JCNewArray) {
/* 2298 */       scanExprs(param1JCNewArray.dims);
/* 2299 */       scanExprs(param1JCNewArray.elems);
/*      */     }
/*      */
/*      */     public void visitAssert(JCTree.JCAssert param1JCAssert) {
/* 2303 */       Bits bits1 = new Bits(this.inits);
/* 2304 */       Bits bits2 = new Bits(this.uninits);
/* 2305 */       scanCond((JCTree)param1JCAssert.cond);
/* 2306 */       bits2.andSet(this.uninitsWhenTrue);
/* 2307 */       if (param1JCAssert.detail != null) {
/* 2308 */         this.inits.assign(this.initsWhenFalse);
/* 2309 */         this.uninits.assign(this.uninitsWhenFalse);
/* 2310 */         scanExpr((JCTree)param1JCAssert.detail);
/*      */       }
/* 2312 */       this.inits.assign(bits1);
/* 2313 */       this.uninits.assign(bits2);
/*      */     }
/*      */
/*      */     public void visitAssign(JCTree.JCAssign param1JCAssign) {
/* 2317 */       JCTree.JCExpression jCExpression = TreeInfo.skipParens(param1JCAssign.lhs);
/* 2318 */       if (!isIdentOrThisDotIdent((JCTree)jCExpression))
/* 2319 */         scanExpr((JCTree)jCExpression);
/* 2320 */       scanExpr((JCTree)param1JCAssign.rhs);
/* 2321 */       letInit((JCTree)jCExpression);
/*      */     }
/*      */     private boolean isIdentOrThisDotIdent(JCTree param1JCTree) {
/* 2324 */       if (param1JCTree.hasTag(JCTree.Tag.IDENT))
/* 2325 */         return true;
/* 2326 */       if (!param1JCTree.hasTag(JCTree.Tag.SELECT)) {
/* 2327 */         return false;
/*      */       }
/* 2329 */       JCTree.JCFieldAccess jCFieldAccess = (JCTree.JCFieldAccess)param1JCTree;
/* 2330 */       return (jCFieldAccess.selected.hasTag(JCTree.Tag.IDENT) && ((JCTree.JCIdent)jCFieldAccess.selected).name ==
/* 2331 */         Flow.this.names._this);
/*      */     }
/*      */
/*      */
/*      */
/*      */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 2337 */       super.visitSelect(param1JCFieldAccess);
/* 2338 */       if (Flow.this.enforceThisDotInit && param1JCFieldAccess.selected
/* 2339 */         .hasTag(JCTree.Tag.IDENT) && ((JCTree.JCIdent)param1JCFieldAccess.selected).name ==
/* 2340 */         Flow.this.names._this && param1JCFieldAccess.sym.kind == 4)
/*      */       {
/*      */
/* 2343 */         checkInit(param1JCFieldAccess.pos(), (Symbol.VarSymbol)param1JCFieldAccess.sym);
/*      */       }
/*      */     }
/*      */
/*      */     public void visitAssignop(JCTree.JCAssignOp param1JCAssignOp) {
/* 2348 */       scanExpr((JCTree)param1JCAssignOp.lhs);
/* 2349 */       scanExpr((JCTree)param1JCAssignOp.rhs);
/* 2350 */       letInit((JCTree)param1JCAssignOp.lhs);
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
/*      */
/*      */     public void visitUnary(JCTree.JCUnary param1JCUnary) {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/comp/Flow$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   3: aload_1
/*      */       //   4: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 137, 1 -> 44, 2 -> 118, 3 -> 118, 4 -> 118, 5 -> 118
/*      */       //   44: aload_0
/*      */       //   45: aload_1
/*      */       //   46: getfield arg : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   49: invokevirtual scanCond : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   52: new com/sun/tools/javac/util/Bits
/*      */       //   55: dup
/*      */       //   56: aload_0
/*      */       //   57: getfield initsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   60: invokespecial <init> : (Lcom/sun/tools/javac/util/Bits;)V
/*      */       //   63: astore_2
/*      */       //   64: aload_0
/*      */       //   65: getfield initsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   68: aload_0
/*      */       //   69: getfield initsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   72: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   75: pop
/*      */       //   76: aload_0
/*      */       //   77: getfield initsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   80: aload_2
/*      */       //   81: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   84: pop
/*      */       //   85: aload_2
/*      */       //   86: aload_0
/*      */       //   87: getfield uninitsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   90: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   93: pop
/*      */       //   94: aload_0
/*      */       //   95: getfield uninitsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   98: aload_0
/*      */       //   99: getfield uninitsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   102: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   105: pop
/*      */       //   106: aload_0
/*      */       //   107: getfield uninitsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   110: aload_2
/*      */       //   111: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   114: pop
/*      */       //   115: goto -> 145
/*      */       //   118: aload_0
/*      */       //   119: aload_1
/*      */       //   120: getfield arg : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   123: invokevirtual scanExpr : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   126: aload_0
/*      */       //   127: aload_1
/*      */       //   128: getfield arg : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   131: invokevirtual letInit : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   134: goto -> 145
/*      */       //   137: aload_0
/*      */       //   138: aload_1
/*      */       //   139: getfield arg : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   142: invokevirtual scanExpr : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   145: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2354	-> 0
/*      */       //   #2356	-> 44
/*      */       //   #2357	-> 52
/*      */       //   #2358	-> 64
/*      */       //   #2359	-> 76
/*      */       //   #2360	-> 85
/*      */       //   #2361	-> 94
/*      */       //   #2362	-> 106
/*      */       //   #2363	-> 115
/*      */       //   #2366	-> 118
/*      */       //   #2367	-> 126
/*      */       //   #2368	-> 134
/*      */       //   #2370	-> 137
/*      */       //   #2372	-> 145
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
/*      */
/*      */     public void visitBinary(JCTree.JCBinary param1JCBinary) {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/comp/Flow$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   3: aload_1
/*      */       //   4: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: lookupswitch default -> 210, 6 -> 36, 7 -> 121
/*      */       //   36: aload_0
/*      */       //   37: aload_1
/*      */       //   38: getfield lhs : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   41: invokevirtual scanCond : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   44: new com/sun/tools/javac/util/Bits
/*      */       //   47: dup
/*      */       //   48: aload_0
/*      */       //   49: getfield initsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   52: invokespecial <init> : (Lcom/sun/tools/javac/util/Bits;)V
/*      */       //   55: astore_2
/*      */       //   56: new com/sun/tools/javac/util/Bits
/*      */       //   59: dup
/*      */       //   60: aload_0
/*      */       //   61: getfield uninitsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   64: invokespecial <init> : (Lcom/sun/tools/javac/util/Bits;)V
/*      */       //   67: astore_3
/*      */       //   68: aload_0
/*      */       //   69: getfield inits : Lcom/sun/tools/javac/util/Bits;
/*      */       //   72: aload_0
/*      */       //   73: getfield initsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   76: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   79: pop
/*      */       //   80: aload_0
/*      */       //   81: getfield uninits : Lcom/sun/tools/javac/util/Bits;
/*      */       //   84: aload_0
/*      */       //   85: getfield uninitsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   88: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   91: pop
/*      */       //   92: aload_0
/*      */       //   93: aload_1
/*      */       //   94: getfield rhs : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   97: invokevirtual scanCond : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   100: aload_0
/*      */       //   101: getfield initsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   104: aload_2
/*      */       //   105: invokevirtual andSet : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   108: pop
/*      */       //   109: aload_0
/*      */       //   110: getfield uninitsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   113: aload_3
/*      */       //   114: invokevirtual andSet : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   117: pop
/*      */       //   118: goto -> 226
/*      */       //   121: aload_0
/*      */       //   122: aload_1
/*      */       //   123: getfield lhs : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   126: invokevirtual scanCond : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   129: new com/sun/tools/javac/util/Bits
/*      */       //   132: dup
/*      */       //   133: aload_0
/*      */       //   134: getfield initsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   137: invokespecial <init> : (Lcom/sun/tools/javac/util/Bits;)V
/*      */       //   140: astore #4
/*      */       //   142: new com/sun/tools/javac/util/Bits
/*      */       //   145: dup
/*      */       //   146: aload_0
/*      */       //   147: getfield uninitsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   150: invokespecial <init> : (Lcom/sun/tools/javac/util/Bits;)V
/*      */       //   153: astore #5
/*      */       //   155: aload_0
/*      */       //   156: getfield inits : Lcom/sun/tools/javac/util/Bits;
/*      */       //   159: aload_0
/*      */       //   160: getfield initsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   163: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   166: pop
/*      */       //   167: aload_0
/*      */       //   168: getfield uninits : Lcom/sun/tools/javac/util/Bits;
/*      */       //   171: aload_0
/*      */       //   172: getfield uninitsWhenFalse : Lcom/sun/tools/javac/util/Bits;
/*      */       //   175: invokevirtual assign : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   178: pop
/*      */       //   179: aload_0
/*      */       //   180: aload_1
/*      */       //   181: getfield rhs : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   184: invokevirtual scanCond : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   187: aload_0
/*      */       //   188: getfield initsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   191: aload #4
/*      */       //   193: invokevirtual andSet : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   196: pop
/*      */       //   197: aload_0
/*      */       //   198: getfield uninitsWhenTrue : Lcom/sun/tools/javac/util/Bits;
/*      */       //   201: aload #5
/*      */       //   203: invokevirtual andSet : (Lcom/sun/tools/javac/util/Bits;)Lcom/sun/tools/javac/util/Bits;
/*      */       //   206: pop
/*      */       //   207: goto -> 226
/*      */       //   210: aload_0
/*      */       //   211: aload_1
/*      */       //   212: getfield lhs : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   215: invokevirtual scanExpr : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   218: aload_0
/*      */       //   219: aload_1
/*      */       //   220: getfield rhs : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   223: invokevirtual scanExpr : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   226: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2375	-> 0
/*      */       //   #2377	-> 36
/*      */       //   #2378	-> 44
/*      */       //   #2379	-> 56
/*      */       //   #2380	-> 68
/*      */       //   #2381	-> 80
/*      */       //   #2382	-> 92
/*      */       //   #2383	-> 100
/*      */       //   #2384	-> 109
/*      */       //   #2385	-> 118
/*      */       //   #2387	-> 121
/*      */       //   #2388	-> 129
/*      */       //   #2389	-> 142
/*      */       //   #2390	-> 155
/*      */       //   #2391	-> 167
/*      */       //   #2392	-> 179
/*      */       //   #2393	-> 187
/*      */       //   #2394	-> 197
/*      */       //   #2395	-> 207
/*      */       //   #2397	-> 210
/*      */       //   #2398	-> 218
/*      */       //   #2400	-> 226
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
/*      */     public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 2403 */       if (param1JCIdent.sym.kind == 4) {
/* 2404 */         checkInit(param1JCIdent.pos(), (Symbol.VarSymbol)param1JCIdent.sym);
/* 2405 */         referenced(param1JCIdent.sym);
/*      */       }
/*      */     }
/*      */
/*      */     void referenced(Symbol param1Symbol) {
/* 2410 */       this.unrefdResources.remove(param1Symbol);
/*      */     }
/*      */
/*      */
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType param1JCAnnotatedType) {
/* 2415 */       param1JCAnnotatedType.underlyingType.accept((JCTree.Visitor)this);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit param1JCCompilationUnit) {}
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void analyzeTree(Env<?> param1Env) {
/* 2429 */       analyzeTree(param1Env, param1Env.tree);
/*      */     }
/*      */
/*      */     public void analyzeTree(Env<?> param1Env, JCTree param1JCTree) {
/*      */       try {
/* 2434 */         this.startPos = param1JCTree.pos().getStartPosition();
/*      */
/* 2436 */         if (this.vardecls == null) {
/* 2437 */           this.vardecls = new JCTree.JCVariableDecl[32];
/*      */         } else {
/* 2439 */           for (byte b = 0; b < this.vardecls.length; b++)
/* 2440 */             this.vardecls[b] = null;
/* 2441 */         }  this.firstadr = 0;
/* 2442 */         this.nextadr = 0;
/* 2443 */         this.pendingExits = new ListBuffer();
/* 2444 */         this.classDef = null;
/* 2445 */         this.unrefdResources = new Scope((Symbol)param1Env.enclClass.sym);
/* 2446 */         scan(param1JCTree);
/*      */       } finally {
/*      */
/* 2449 */         this.startPos = -1;
/* 2450 */         resetBits(new Bits[] { this.inits, this.uninits, this.uninitsTry, this.initsWhenTrue, this.initsWhenFalse, this.uninitsWhenTrue, this.uninitsWhenFalse });
/*      */
/* 2452 */         if (this.vardecls != null)
/* 2453 */           for (byte b = 0; b < this.vardecls.length; b++) {
/* 2454 */             this.vardecls[b] = null;
/*      */           }
/* 2456 */         this.firstadr = 0;
/* 2457 */         this.nextadr = 0;
/* 2458 */         this.pendingExits = null;
/* 2459 */         this.classDef = null;
/* 2460 */         this.unrefdResources = null;
/*      */       }
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
/*      */   class CaptureAnalyzer
/*      */     extends BaseAnalyzer<BaseAnalyzer.PendingExit>
/*      */   {
/*      */     JCTree currentTree;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void markDead() {}
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     void checkEffectivelyFinal(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol.VarSymbol param1VarSymbol) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield currentTree : Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   4: ifnull -> 123
/*      */       //   7: aload_2
/*      */       //   8: getfield owner : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   11: getfield kind : I
/*      */       //   14: bipush #16
/*      */       //   16: if_icmpne -> 123
/*      */       //   19: aload_2
/*      */       //   20: getfield pos : I
/*      */       //   23: aload_0
/*      */       //   24: getfield currentTree : Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   27: invokevirtual getStartPosition : ()I
/*      */       //   30: if_icmpge -> 123
/*      */       //   33: getstatic com/sun/tools/javac/comp/Flow$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   36: aload_0
/*      */       //   37: getfield currentTree : Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   40: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   43: invokevirtual ordinal : ()I
/*      */       //   46: iaload
/*      */       //   47: lookupswitch default -> 123, 8 -> 72, 9 -> 104
/*      */       //   72: aload_0
/*      */       //   73: getfield this$0 : Lcom/sun/tools/javac/comp/Flow;
/*      */       //   76: invokestatic access$1300 : (Lcom/sun/tools/javac/comp/Flow;)Z
/*      */       //   79: ifne -> 104
/*      */       //   82: aload_2
/*      */       //   83: invokevirtual flags : ()J
/*      */       //   86: ldc2_w 16
/*      */       //   89: land
/*      */       //   90: lconst_0
/*      */       //   91: lcmp
/*      */       //   92: ifne -> 123
/*      */       //   95: aload_0
/*      */       //   96: aload_1
/*      */       //   97: aload_2
/*      */       //   98: invokevirtual reportInnerClsNeedsFinalError : (Lcom/sun/tools/javac/util/JCDiagnostic$DiagnosticPosition;Lcom/sun/tools/javac/code/Symbol;)V
/*      */       //   101: goto -> 123
/*      */       //   104: aload_2
/*      */       //   105: invokevirtual flags : ()J
/*      */       //   108: ldc2_w 2199023255568
/*      */       //   111: land
/*      */       //   112: lconst_0
/*      */       //   113: lcmp
/*      */       //   114: ifne -> 123
/*      */       //   117: aload_0
/*      */       //   118: aload_1
/*      */       //   119: aload_2
/*      */       //   120: invokevirtual reportEffectivelyFinalError : (Lcom/sun/tools/javac/util/JCDiagnostic$DiagnosticPosition;Lcom/sun/tools/javac/code/Symbol;)V
/*      */       //   123: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2483	-> 0
/*      */       //   #2485	-> 27
/*      */       //   #2486	-> 33
/*      */       //   #2488	-> 72
/*      */       //   #2489	-> 82
/*      */       //   #2490	-> 95
/*      */       //   #2495	-> 104
/*      */       //   #2496	-> 117
/*      */       //   #2500	-> 123
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
/*      */     void letInit(JCTree param1JCTree) {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: invokestatic skipParens : (Lcom/sun/tools/javac/tree/JCTree;)Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   4: astore_1
/*      */       //   5: aload_1
/*      */       //   6: getstatic com/sun/tools/javac/tree/JCTree$Tag.IDENT : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   9: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   12: ifne -> 25
/*      */       //   15: aload_1
/*      */       //   16: getstatic com/sun/tools/javac/tree/JCTree$Tag.SELECT : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   19: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   22: ifeq -> 141
/*      */       //   25: aload_1
/*      */       //   26: invokestatic symbol : (Lcom/sun/tools/javac/tree/JCTree;)Lcom/sun/tools/javac/code/Symbol;
/*      */       //   29: astore_2
/*      */       //   30: aload_0
/*      */       //   31: getfield currentTree : Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   34: ifnull -> 141
/*      */       //   37: aload_2
/*      */       //   38: getfield kind : I
/*      */       //   41: iconst_4
/*      */       //   42: if_icmpne -> 141
/*      */       //   45: aload_2
/*      */       //   46: getfield owner : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   49: getfield kind : I
/*      */       //   52: bipush #16
/*      */       //   54: if_icmpne -> 141
/*      */       //   57: aload_2
/*      */       //   58: checkcast com/sun/tools/javac/code/Symbol$VarSymbol
/*      */       //   61: getfield pos : I
/*      */       //   64: aload_0
/*      */       //   65: getfield currentTree : Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   68: invokevirtual getStartPosition : ()I
/*      */       //   71: if_icmpge -> 141
/*      */       //   74: getstatic com/sun/tools/javac/comp/Flow$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   77: aload_0
/*      */       //   78: getfield currentTree : Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   81: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   84: invokevirtual ordinal : ()I
/*      */       //   87: iaload
/*      */       //   88: lookupswitch default -> 141, 8 -> 116, 9 -> 135
/*      */       //   116: aload_0
/*      */       //   117: getfield this$0 : Lcom/sun/tools/javac/comp/Flow;
/*      */       //   120: invokestatic access$1300 : (Lcom/sun/tools/javac/comp/Flow;)Z
/*      */       //   123: ifne -> 135
/*      */       //   126: aload_0
/*      */       //   127: aload_1
/*      */       //   128: aload_2
/*      */       //   129: invokevirtual reportInnerClsNeedsFinalError : (Lcom/sun/tools/javac/util/JCDiagnostic$DiagnosticPosition;Lcom/sun/tools/javac/code/Symbol;)V
/*      */       //   132: goto -> 141
/*      */       //   135: aload_0
/*      */       //   136: aload_1
/*      */       //   137: aload_2
/*      */       //   138: invokevirtual reportEffectivelyFinalError : (Lcom/sun/tools/javac/util/JCDiagnostic$DiagnosticPosition;Lcom/sun/tools/javac/code/Symbol;)V
/*      */       //   141: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2504	-> 0
/*      */       //   #2505	-> 5
/*      */       //   #2506	-> 25
/*      */       //   #2507	-> 30
/*      */       //   #2510	-> 68
/*      */       //   #2511	-> 74
/*      */       //   #2513	-> 116
/*      */       //   #2514	-> 126
/*      */       //   #2515	-> 132
/*      */       //   #2518	-> 135
/*      */       //   #2522	-> 141
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
/*      */     void reportEffectivelyFinalError(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol) {
/* 2525 */       String str = this.currentTree.hasTag(JCTree.Tag.LAMBDA) ? "lambda" : "inner.cls";
/*      */
/* 2527 */       Flow.this.log.error(param1DiagnosticPosition, "cant.ref.non.effectively.final.var", new Object[] { param1Symbol, Flow.access$1400(this.this$0).fragment(str, new Object[0]) });
/*      */     }
/*      */
/*      */     void reportInnerClsNeedsFinalError(JCDiagnostic.DiagnosticPosition param1DiagnosticPosition, Symbol param1Symbol) {
/* 2531 */       Flow.this.log.error(param1DiagnosticPosition, "local.var.accessed.from.icls.needs.final", new Object[] { param1Symbol });
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
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/* 2543 */       JCTree jCTree = this.currentTree;
/*      */       try {
/* 2545 */         this.currentTree = param1JCClassDecl.sym.isLocal() ? (JCTree)param1JCClassDecl : null;
/* 2546 */         super.visitClassDef(param1JCClassDecl);
/*      */       } finally {
/* 2548 */         this.currentTree = jCTree;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/* 2554 */       JCTree jCTree = this.currentTree;
/*      */       try {
/* 2556 */         this.currentTree = (JCTree)param1JCLambda;
/* 2557 */         super.visitLambda(param1JCLambda);
/*      */       } finally {
/* 2559 */         this.currentTree = jCTree;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 2565 */       if (param1JCIdent.sym.kind == 4) {
/* 2566 */         checkEffectivelyFinal((JCDiagnostic.DiagnosticPosition)param1JCIdent, (Symbol.VarSymbol)param1JCIdent.sym);
/*      */       }
/*      */     }
/*      */
/*      */     public void visitAssign(JCTree.JCAssign param1JCAssign) {
/* 2571 */       JCTree.JCExpression jCExpression = TreeInfo.skipParens(param1JCAssign.lhs);
/* 2572 */       if (!(jCExpression instanceof JCTree.JCIdent)) {
/* 2573 */         scan((JCTree)jCExpression);
/*      */       }
/* 2575 */       scan((JCTree)param1JCAssign.rhs);
/* 2576 */       letInit((JCTree)jCExpression);
/*      */     }
/*      */
/*      */     public void visitAssignop(JCTree.JCAssignOp param1JCAssignOp) {
/* 2580 */       scan((JCTree)param1JCAssignOp.lhs);
/* 2581 */       scan((JCTree)param1JCAssignOp.rhs);
/* 2582 */       letInit((JCTree)param1JCAssignOp.lhs);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitUnary(JCTree.JCUnary param1JCUnary) {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/comp/Flow$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */       //   3: aload_1
/*      */       //   4: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 59, 2 -> 40, 3 -> 40, 4 -> 40, 5 -> 40
/*      */       //   40: aload_0
/*      */       //   41: aload_1
/*      */       //   42: getfield arg : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   45: invokevirtual scan : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   48: aload_0
/*      */       //   49: aload_1
/*      */       //   50: getfield arg : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   53: invokevirtual letInit : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   56: goto -> 67
/*      */       //   59: aload_0
/*      */       //   60: aload_1
/*      */       //   61: getfield arg : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   64: invokevirtual scan : (Lcom/sun/tools/javac/tree/JCTree;)V
/*      */       //   67: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2586	-> 0
/*      */       //   #2589	-> 40
/*      */       //   #2590	-> 48
/*      */       //   #2591	-> 56
/*      */       //   #2593	-> 59
/*      */       //   #2595	-> 67
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit param1JCCompilationUnit) {}
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void analyzeTree(Env<AttrContext> param1Env, TreeMaker param1TreeMaker) {
/* 2608 */       analyzeTree(param1Env, param1Env.tree, param1TreeMaker);
/*      */     }
/*      */     public void analyzeTree(Env<AttrContext> param1Env, JCTree param1JCTree, TreeMaker param1TreeMaker) {
/*      */       try {
/* 2612 */         Flow.this.attrEnv = param1Env;
/* 2613 */         Flow.this.make = param1TreeMaker;
/* 2614 */         this.pendingExits = new ListBuffer();
/* 2615 */         scan(param1JCTree);
/*      */       } finally {
/* 2617 */         this.pendingExits = null;
/* 2618 */         Flow.this.make = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Flow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
