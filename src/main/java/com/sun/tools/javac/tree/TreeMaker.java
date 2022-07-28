/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.source.tree.MemberReferenceTree;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.BoundKind;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeMaker
/*     */   implements JCTree.Factory
/*     */ {
/*  50 */   protected static final Context.Key<TreeMaker> treeMakerKey = new Context.Key();
/*     */ 
/*     */ 
/*     */   
/*     */   public static TreeMaker instance(Context paramContext) {
/*  55 */     TreeMaker treeMaker = (TreeMaker)paramContext.get(treeMakerKey);
/*  56 */     if (treeMaker == null)
/*  57 */       treeMaker = new TreeMaker(paramContext); 
/*  58 */     return treeMaker;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public int pos = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCCompilationUnit toplevel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Names names;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Types types;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Symtab syms;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationBuilder annotationBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeMaker forToplevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 101 */     return new TreeMaker(paramJCCompilationUnit, this.names, this.types, this.syms);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeMaker at(int paramInt) {
/* 107 */     this.pos = paramInt;
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeMaker at(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 114 */     this.pos = (paramDiagnosticPosition == null) ? -1 : paramDiagnosticPosition.getStartPosition();
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCCompilationUnit TopLevel(List<JCTree.JCAnnotation> paramList, JCTree.JCExpression paramJCExpression, List<JCTree> paramList1) {
/* 125 */     Assert.checkNonNull(paramList);
/* 126 */     for (JCTree jCTree : paramList1)
/* 127 */       Assert.check((jCTree instanceof JCTree.JCClassDecl || jCTree instanceof JCTree.JCImport || jCTree instanceof JCTree.JCSkip || jCTree instanceof JCTree.JCErroneous || (jCTree instanceof JCTree.JCExpressionStatement && ((JCTree.JCExpressionStatement)jCTree).expr instanceof JCTree.JCErroneous)), jCTree
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 133 */           .getClass().getSimpleName()); 
/* 134 */     JCTree.JCCompilationUnit jCCompilationUnit = new JCTree.JCCompilationUnit(paramList, paramJCExpression, paramList1, null, null, null, null);
/*     */     
/* 136 */     jCCompilationUnit.pos = this.pos;
/* 137 */     return jCCompilationUnit;
/*     */   }
/*     */   
/*     */   public JCTree.JCImport Import(JCTree paramJCTree, boolean paramBoolean) {
/* 141 */     JCTree.JCImport jCImport = new JCTree.JCImport(paramJCTree, paramBoolean);
/* 142 */     jCImport.pos = this.pos;
/* 143 */     return jCImport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCClassDecl ClassDef(JCTree.JCModifiers paramJCModifiers, Name paramName, List<JCTree.JCTypeParameter> paramList, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList1, List<JCTree> paramList2) {
/* 153 */     JCTree.JCClassDecl jCClassDecl = new JCTree.JCClassDecl(paramJCModifiers, paramName, paramList, paramJCExpression, paramList1, paramList2, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     jCClassDecl.pos = this.pos;
/* 161 */     return jCClassDecl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCMethodDecl MethodDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, List<JCTree.JCTypeParameter> paramList, List<JCTree.JCVariableDecl> paramList1, List<JCTree.JCExpression> paramList2, JCTree.JCBlock paramJCBlock, JCTree.JCExpression paramJCExpression2) {
/* 172 */     return MethodDef(paramJCModifiers, paramName, paramJCExpression1, paramList, null, paramList1, paramList2, paramJCBlock, paramJCExpression2);
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
/*     */   public JCTree.JCMethodDecl MethodDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, List<JCTree.JCTypeParameter> paramList, JCTree.JCVariableDecl paramJCVariableDecl, List<JCTree.JCVariableDecl> paramList1, List<JCTree.JCExpression> paramList2, JCTree.JCBlock paramJCBlock, JCTree.JCExpression paramJCExpression2) {
/* 187 */     JCTree.JCMethodDecl jCMethodDecl = new JCTree.JCMethodDecl(paramJCModifiers, paramName, paramJCExpression1, paramList, paramJCVariableDecl, paramList1, paramList2, paramJCBlock, paramJCExpression2, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     jCMethodDecl.pos = this.pos;
/* 198 */     return jCMethodDecl;
/*     */   }
/*     */   
/*     */   public JCTree.JCVariableDecl VarDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 202 */     JCTree.JCVariableDecl jCVariableDecl = new JCTree.JCVariableDecl(paramJCModifiers, paramName, paramJCExpression1, paramJCExpression2, null);
/* 203 */     jCVariableDecl.pos = this.pos;
/* 204 */     return jCVariableDecl;
/*     */   }
/*     */   
/*     */   public JCTree.JCVariableDecl ReceiverVarDef(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 208 */     JCTree.JCVariableDecl jCVariableDecl = new JCTree.JCVariableDecl(paramJCModifiers, paramJCExpression1, paramJCExpression2);
/* 209 */     jCVariableDecl.pos = this.pos;
/* 210 */     return jCVariableDecl;
/*     */   }
/*     */   
/*     */   public JCTree.JCSkip Skip() {
/* 214 */     JCTree.JCSkip jCSkip = new JCTree.JCSkip();
/* 215 */     jCSkip.pos = this.pos;
/* 216 */     return jCSkip;
/*     */   }
/*     */   
/*     */   public JCTree.JCBlock Block(long paramLong, List<JCTree.JCStatement> paramList) {
/* 220 */     JCTree.JCBlock jCBlock = new JCTree.JCBlock(paramLong, paramList);
/* 221 */     jCBlock.pos = this.pos;
/* 222 */     return jCBlock;
/*     */   }
/*     */   
/*     */   public JCTree.JCDoWhileLoop DoLoop(JCTree.JCStatement paramJCStatement, JCTree.JCExpression paramJCExpression) {
/* 226 */     JCTree.JCDoWhileLoop jCDoWhileLoop = new JCTree.JCDoWhileLoop(paramJCStatement, paramJCExpression);
/* 227 */     jCDoWhileLoop.pos = this.pos;
/* 228 */     return jCDoWhileLoop;
/*     */   }
/*     */   
/*     */   public JCTree.JCWhileLoop WhileLoop(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement) {
/* 232 */     JCTree.JCWhileLoop jCWhileLoop = new JCTree.JCWhileLoop(paramJCExpression, paramJCStatement);
/* 233 */     jCWhileLoop.pos = this.pos;
/* 234 */     return jCWhileLoop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCForLoop ForLoop(List<JCTree.JCStatement> paramList, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpressionStatement> paramList1, JCTree.JCStatement paramJCStatement) {
/* 242 */     JCTree.JCForLoop jCForLoop = new JCTree.JCForLoop(paramList, paramJCExpression, paramList1, paramJCStatement);
/* 243 */     jCForLoop.pos = this.pos;
/* 244 */     return jCForLoop;
/*     */   }
/*     */   
/*     */   public JCTree.JCEnhancedForLoop ForeachLoop(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement) {
/* 248 */     JCTree.JCEnhancedForLoop jCEnhancedForLoop = new JCTree.JCEnhancedForLoop(paramJCVariableDecl, paramJCExpression, paramJCStatement);
/* 249 */     jCEnhancedForLoop.pos = this.pos;
/* 250 */     return jCEnhancedForLoop;
/*     */   }
/*     */   
/*     */   public JCTree.JCLabeledStatement Labelled(Name paramName, JCTree.JCStatement paramJCStatement) {
/* 254 */     JCTree.JCLabeledStatement jCLabeledStatement = new JCTree.JCLabeledStatement(paramName, paramJCStatement);
/* 255 */     jCLabeledStatement.pos = this.pos;
/* 256 */     return jCLabeledStatement;
/*     */   }
/*     */   
/*     */   public JCTree.JCSwitch Switch(JCTree.JCExpression paramJCExpression, List<JCTree.JCCase> paramList) {
/* 260 */     JCTree.JCSwitch jCSwitch = new JCTree.JCSwitch(paramJCExpression, paramList);
/* 261 */     jCSwitch.pos = this.pos;
/* 262 */     return jCSwitch;
/*     */   }
/*     */   
/*     */   public JCTree.JCCase Case(JCTree.JCExpression paramJCExpression, List<JCTree.JCStatement> paramList) {
/* 266 */     JCTree.JCCase jCCase = new JCTree.JCCase(paramJCExpression, paramList);
/* 267 */     jCCase.pos = this.pos;
/* 268 */     return jCCase;
/*     */   }
/*     */   
/*     */   public JCTree.JCSynchronized Synchronized(JCTree.JCExpression paramJCExpression, JCTree.JCBlock paramJCBlock) {
/* 272 */     JCTree.JCSynchronized jCSynchronized = new JCTree.JCSynchronized(paramJCExpression, paramJCBlock);
/* 273 */     jCSynchronized.pos = this.pos;
/* 274 */     return jCSynchronized;
/*     */   }
/*     */   
/*     */   public JCTree.JCTry Try(JCTree.JCBlock paramJCBlock1, List<JCTree.JCCatch> paramList, JCTree.JCBlock paramJCBlock2) {
/* 278 */     return Try(List.nil(), paramJCBlock1, paramList, paramJCBlock2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCTry Try(List<JCTree> paramList, JCTree.JCBlock paramJCBlock1, List<JCTree.JCCatch> paramList1, JCTree.JCBlock paramJCBlock2) {
/* 285 */     JCTree.JCTry jCTry = new JCTree.JCTry(paramList, paramJCBlock1, paramList1, paramJCBlock2);
/* 286 */     jCTry.pos = this.pos;
/* 287 */     return jCTry;
/*     */   }
/*     */   
/*     */   public JCTree.JCCatch Catch(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCBlock paramJCBlock) {
/* 291 */     JCTree.JCCatch jCCatch = new JCTree.JCCatch(paramJCVariableDecl, paramJCBlock);
/* 292 */     jCCatch.pos = this.pos;
/* 293 */     return jCCatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCConditional Conditional(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, JCTree.JCExpression paramJCExpression3) {
/* 300 */     JCTree.JCConditional jCConditional = new JCTree.JCConditional(paramJCExpression1, paramJCExpression2, paramJCExpression3);
/* 301 */     jCConditional.pos = this.pos;
/* 302 */     return jCConditional;
/*     */   }
/*     */   
/*     */   public JCTree.JCIf If(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement1, JCTree.JCStatement paramJCStatement2) {
/* 306 */     JCTree.JCIf jCIf = new JCTree.JCIf(paramJCExpression, paramJCStatement1, paramJCStatement2);
/* 307 */     jCIf.pos = this.pos;
/* 308 */     return jCIf;
/*     */   }
/*     */   
/*     */   public JCTree.JCExpressionStatement Exec(JCTree.JCExpression paramJCExpression) {
/* 312 */     JCTree.JCExpressionStatement jCExpressionStatement = new JCTree.JCExpressionStatement(paramJCExpression);
/* 313 */     jCExpressionStatement.pos = this.pos;
/* 314 */     return jCExpressionStatement;
/*     */   }
/*     */   
/*     */   public JCTree.JCBreak Break(Name paramName) {
/* 318 */     JCTree.JCBreak jCBreak = new JCTree.JCBreak(paramName, null);
/* 319 */     jCBreak.pos = this.pos;
/* 320 */     return jCBreak;
/*     */   }
/*     */   
/*     */   public JCTree.JCContinue Continue(Name paramName) {
/* 324 */     JCTree.JCContinue jCContinue = new JCTree.JCContinue(paramName, null);
/* 325 */     jCContinue.pos = this.pos;
/* 326 */     return jCContinue;
/*     */   }
/*     */   
/*     */   public JCTree.JCReturn Return(JCTree.JCExpression paramJCExpression) {
/* 330 */     JCTree.JCReturn jCReturn = new JCTree.JCReturn(paramJCExpression);
/* 331 */     jCReturn.pos = this.pos;
/* 332 */     return jCReturn;
/*     */   }
/*     */   
/*     */   public JCTree.JCThrow Throw(JCTree.JCExpression paramJCExpression) {
/* 336 */     JCTree.JCThrow jCThrow = new JCTree.JCThrow(paramJCExpression);
/* 337 */     jCThrow.pos = this.pos;
/* 338 */     return jCThrow;
/*     */   }
/*     */   
/*     */   public JCTree.JCAssert Assert(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 342 */     JCTree.JCAssert jCAssert = new JCTree.JCAssert(paramJCExpression1, paramJCExpression2);
/* 343 */     jCAssert.pos = this.pos;
/* 344 */     return jCAssert;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCMethodInvocation Apply(List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList2) {
/* 351 */     JCTree.JCMethodInvocation jCMethodInvocation = new JCTree.JCMethodInvocation(paramList1, paramJCExpression, paramList2);
/* 352 */     jCMethodInvocation.pos = this.pos;
/* 353 */     return jCMethodInvocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCNewClass NewClass(JCTree.JCExpression paramJCExpression1, List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression2, List<JCTree.JCExpression> paramList2, JCTree.JCClassDecl paramJCClassDecl) {
/* 362 */     JCTree.JCNewClass jCNewClass = new JCTree.JCNewClass(paramJCExpression1, paramList1, paramJCExpression2, paramList2, paramJCClassDecl);
/* 363 */     jCNewClass.pos = this.pos;
/* 364 */     return jCNewClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCNewArray NewArray(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList1, List<JCTree.JCExpression> paramList2) {
/* 371 */     JCTree.JCNewArray jCNewArray = new JCTree.JCNewArray(paramJCExpression, paramList1, paramList2);
/* 372 */     jCNewArray.pos = this.pos;
/* 373 */     return jCNewArray;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCLambda Lambda(List<JCTree.JCVariableDecl> paramList, JCTree paramJCTree) {
/* 379 */     JCTree.JCLambda jCLambda = new JCTree.JCLambda(paramList, paramJCTree);
/* 380 */     jCLambda.pos = this.pos;
/* 381 */     return jCLambda;
/*     */   }
/*     */   
/*     */   public JCTree.JCParens Parens(JCTree.JCExpression paramJCExpression) {
/* 385 */     JCTree.JCParens jCParens = new JCTree.JCParens(paramJCExpression);
/* 386 */     jCParens.pos = this.pos;
/* 387 */     return jCParens;
/*     */   }
/*     */   
/*     */   public JCTree.JCAssign Assign(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 391 */     JCTree.JCAssign jCAssign = new JCTree.JCAssign(paramJCExpression1, paramJCExpression2);
/* 392 */     jCAssign.pos = this.pos;
/* 393 */     return jCAssign;
/*     */   }
/*     */   
/*     */   public JCTree.JCAssignOp Assignop(JCTree.Tag paramTag, JCTree paramJCTree1, JCTree paramJCTree2) {
/* 397 */     JCTree.JCAssignOp jCAssignOp = new JCTree.JCAssignOp(paramTag, paramJCTree1, paramJCTree2, null);
/* 398 */     jCAssignOp.pos = this.pos;
/* 399 */     return jCAssignOp;
/*     */   }
/*     */   
/*     */   public JCTree.JCUnary Unary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression) {
/* 403 */     JCTree.JCUnary jCUnary = new JCTree.JCUnary(paramTag, paramJCExpression);
/* 404 */     jCUnary.pos = this.pos;
/* 405 */     return jCUnary;
/*     */   }
/*     */   
/*     */   public JCTree.JCBinary Binary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 409 */     JCTree.JCBinary jCBinary = new JCTree.JCBinary(paramTag, paramJCExpression1, paramJCExpression2, null);
/* 410 */     jCBinary.pos = this.pos;
/* 411 */     return jCBinary;
/*     */   }
/*     */   
/*     */   public JCTree.JCTypeCast TypeCast(JCTree paramJCTree, JCTree.JCExpression paramJCExpression) {
/* 415 */     JCTree.JCTypeCast jCTypeCast = new JCTree.JCTypeCast(paramJCTree, paramJCExpression);
/* 416 */     jCTypeCast.pos = this.pos;
/* 417 */     return jCTypeCast;
/*     */   }
/*     */   
/*     */   public JCTree.JCInstanceOf TypeTest(JCTree.JCExpression paramJCExpression, JCTree paramJCTree) {
/* 421 */     JCTree.JCInstanceOf jCInstanceOf = new JCTree.JCInstanceOf(paramJCExpression, paramJCTree);
/* 422 */     jCInstanceOf.pos = this.pos;
/* 423 */     return jCInstanceOf;
/*     */   }
/*     */   
/*     */   public JCTree.JCArrayAccess Indexed(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 427 */     JCTree.JCArrayAccess jCArrayAccess = new JCTree.JCArrayAccess(paramJCExpression1, paramJCExpression2);
/* 428 */     jCArrayAccess.pos = this.pos;
/* 429 */     return jCArrayAccess;
/*     */   }
/*     */   
/*     */   public JCTree.JCFieldAccess Select(JCTree.JCExpression paramJCExpression, Name paramName) {
/* 433 */     JCTree.JCFieldAccess jCFieldAccess = new JCTree.JCFieldAccess(paramJCExpression, paramName, null);
/* 434 */     jCFieldAccess.pos = this.pos;
/* 435 */     return jCFieldAccess;
/*     */   }
/*     */ 
/*     */   
/*     */   public JCTree.JCMemberReference Reference(MemberReferenceTree.ReferenceMode paramReferenceMode, Name paramName, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList) {
/* 440 */     JCTree.JCMemberReference jCMemberReference = new JCTree.JCMemberReference(paramReferenceMode, paramName, paramJCExpression, paramList);
/* 441 */     jCMemberReference.pos = this.pos;
/* 442 */     return jCMemberReference;
/*     */   }
/*     */   
/*     */   public JCTree.JCIdent Ident(Name paramName) {
/* 446 */     JCTree.JCIdent jCIdent = new JCTree.JCIdent(paramName, null);
/* 447 */     jCIdent.pos = this.pos;
/* 448 */     return jCIdent;
/*     */   }
/*     */   
/*     */   public JCTree.JCLiteral Literal(TypeTag paramTypeTag, Object paramObject) {
/* 452 */     JCTree.JCLiteral jCLiteral = new JCTree.JCLiteral(paramTypeTag, paramObject);
/* 453 */     jCLiteral.pos = this.pos;
/* 454 */     return jCLiteral;
/*     */   }
/*     */   
/*     */   public JCTree.JCPrimitiveTypeTree TypeIdent(TypeTag paramTypeTag) {
/* 458 */     JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTree = new JCTree.JCPrimitiveTypeTree(paramTypeTag);
/* 459 */     jCPrimitiveTypeTree.pos = this.pos;
/* 460 */     return jCPrimitiveTypeTree;
/*     */   }
/*     */   
/*     */   public JCTree.JCArrayTypeTree TypeArray(JCTree.JCExpression paramJCExpression) {
/* 464 */     JCTree.JCArrayTypeTree jCArrayTypeTree = new JCTree.JCArrayTypeTree(paramJCExpression);
/* 465 */     jCArrayTypeTree.pos = this.pos;
/* 466 */     return jCArrayTypeTree;
/*     */   }
/*     */   
/*     */   public JCTree.JCTypeApply TypeApply(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList) {
/* 470 */     JCTree.JCTypeApply jCTypeApply = new JCTree.JCTypeApply(paramJCExpression, paramList);
/* 471 */     jCTypeApply.pos = this.pos;
/* 472 */     return jCTypeApply;
/*     */   }
/*     */   
/*     */   public JCTree.JCTypeUnion TypeUnion(List<JCTree.JCExpression> paramList) {
/* 476 */     JCTree.JCTypeUnion jCTypeUnion = new JCTree.JCTypeUnion(paramList);
/* 477 */     jCTypeUnion.pos = this.pos;
/* 478 */     return jCTypeUnion;
/*     */   }
/*     */   
/*     */   public JCTree.JCTypeIntersection TypeIntersection(List<JCTree.JCExpression> paramList) {
/* 482 */     JCTree.JCTypeIntersection jCTypeIntersection = new JCTree.JCTypeIntersection(paramList);
/* 483 */     jCTypeIntersection.pos = this.pos;
/* 484 */     return jCTypeIntersection;
/*     */   }
/*     */   
/*     */   public JCTree.JCTypeParameter TypeParameter(Name paramName, List<JCTree.JCExpression> paramList) {
/* 488 */     return TypeParameter(paramName, paramList, List.nil());
/*     */   }
/*     */   
/*     */   public JCTree.JCTypeParameter TypeParameter(Name paramName, List<JCTree.JCExpression> paramList, List<JCTree.JCAnnotation> paramList1) {
/* 492 */     JCTree.JCTypeParameter jCTypeParameter = new JCTree.JCTypeParameter(paramName, paramList, paramList1);
/* 493 */     jCTypeParameter.pos = this.pos;
/* 494 */     return jCTypeParameter;
/*     */   }
/*     */   
/*     */   public JCTree.JCWildcard Wildcard(JCTree.TypeBoundKind paramTypeBoundKind, JCTree paramJCTree) {
/* 498 */     JCTree.JCWildcard jCWildcard = new JCTree.JCWildcard(paramTypeBoundKind, paramJCTree);
/* 499 */     jCWildcard.pos = this.pos;
/* 500 */     return jCWildcard;
/*     */   }
/*     */   
/*     */   public JCTree.TypeBoundKind TypeBoundKind(BoundKind paramBoundKind) {
/* 504 */     JCTree.TypeBoundKind typeBoundKind = new JCTree.TypeBoundKind(paramBoundKind);
/* 505 */     typeBoundKind.pos = this.pos;
/* 506 */     return typeBoundKind;
/*     */   }
/*     */   
/*     */   public JCTree.JCAnnotation Annotation(JCTree paramJCTree, List<JCTree.JCExpression> paramList) {
/* 510 */     JCTree.JCAnnotation jCAnnotation = new JCTree.JCAnnotation(JCTree.Tag.ANNOTATION, paramJCTree, paramList);
/* 511 */     jCAnnotation.pos = this.pos;
/* 512 */     return jCAnnotation;
/*     */   }
/*     */   
/*     */   public JCTree.JCAnnotation TypeAnnotation(JCTree paramJCTree, List<JCTree.JCExpression> paramList) {
/* 516 */     JCTree.JCAnnotation jCAnnotation = new JCTree.JCAnnotation(JCTree.Tag.TYPE_ANNOTATION, paramJCTree, paramList);
/* 517 */     jCAnnotation.pos = this.pos;
/* 518 */     return jCAnnotation;
/*     */   }
/*     */   
/*     */   public JCTree.JCModifiers Modifiers(long paramLong, List<JCTree.JCAnnotation> paramList) {
/* 522 */     JCTree.JCModifiers jCModifiers = new JCTree.JCModifiers(paramLong, paramList);
/* 523 */     boolean bool = ((paramLong & 0x80000002DFFL) == 0L) ? true : false;
/* 524 */     jCModifiers.pos = (bool && paramList.isEmpty()) ? -1 : this.pos;
/* 525 */     return jCModifiers;
/*     */   }
/*     */   
/*     */   public JCTree.JCModifiers Modifiers(long paramLong) {
/* 529 */     return Modifiers(paramLong, List.nil());
/*     */   }
/*     */   
/*     */   public JCTree.JCAnnotatedType AnnotatedType(List<JCTree.JCAnnotation> paramList, JCTree.JCExpression paramJCExpression) {
/* 533 */     JCTree.JCAnnotatedType jCAnnotatedType = new JCTree.JCAnnotatedType(paramList, paramJCExpression);
/* 534 */     jCAnnotatedType.pos = this.pos;
/* 535 */     return jCAnnotatedType;
/*     */   }
/*     */   
/*     */   public JCTree.JCErroneous Erroneous() {
/* 539 */     return Erroneous(List.nil());
/*     */   }
/*     */   
/*     */   public JCTree.JCErroneous Erroneous(List<? extends JCTree> paramList) {
/* 543 */     JCTree.JCErroneous jCErroneous = new JCTree.JCErroneous(paramList);
/* 544 */     jCErroneous.pos = this.pos;
/* 545 */     return jCErroneous;
/*     */   }
/*     */   
/*     */   public JCTree.LetExpr LetExpr(List<JCTree.JCVariableDecl> paramList, JCTree paramJCTree) {
/* 549 */     JCTree.LetExpr letExpr = new JCTree.LetExpr(paramList, paramJCTree);
/* 550 */     letExpr.pos = this.pos;
/* 551 */     return letExpr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCClassDecl AnonymousClassDef(JCTree.JCModifiers paramJCModifiers, List<JCTree> paramList) {
/* 561 */     return ClassDef(paramJCModifiers, this.names.empty, 
/*     */         
/* 563 */         List.nil(), null, 
/*     */         
/* 565 */         List.nil(), paramList);
/*     */   }
/*     */ 
/*     */   
/*     */   public JCTree.LetExpr LetExpr(JCTree.JCVariableDecl paramJCVariableDecl, JCTree paramJCTree) {
/* 570 */     JCTree.LetExpr letExpr = new JCTree.LetExpr(List.of(paramJCVariableDecl), paramJCTree);
/* 571 */     letExpr.pos = this.pos;
/* 572 */     return letExpr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCIdent Ident(Symbol paramSymbol) {
/* 578 */     return (JCTree.JCIdent)(new JCTree.JCIdent((paramSymbol.name != this.names.empty) ? paramSymbol.name : paramSymbol
/*     */         
/* 580 */         .flatName(), paramSymbol))
/* 581 */       .setPos(this.pos)
/* 582 */       .setType(paramSymbol.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression Select(JCTree.JCExpression paramJCExpression, Symbol paramSymbol) {
/* 589 */     return (new JCTree.JCFieldAccess(paramJCExpression, paramSymbol.name, paramSymbol)).setPos(this.pos).setType(paramSymbol.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression QualIdent(Symbol paramSymbol) {
/* 596 */     return isUnqualifiable(paramSymbol) ? 
/* 597 */       Ident(paramSymbol) : 
/* 598 */       Select(QualIdent(paramSymbol.owner), paramSymbol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression Ident(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 605 */     return Ident((Symbol)paramJCVariableDecl.sym);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCExpression> Idents(List<JCTree.JCVariableDecl> paramList) {
/* 612 */     ListBuffer listBuffer = new ListBuffer();
/* 613 */     for (List<JCTree.JCVariableDecl> list = paramList; list.nonEmpty(); list = list.tail)
/* 614 */       listBuffer.append(Ident((JCTree.JCVariableDecl)list.head)); 
/* 615 */     return listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression This(Type paramType) {
/* 621 */     return Ident((Symbol)new Symbol.VarSymbol(16L, this.names._this, paramType, (Symbol)paramType.tsym));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression QualThis(Type paramType) {
/* 627 */     return Select(Type(paramType), (Symbol)new Symbol.VarSymbol(16L, this.names._this, paramType, (Symbol)paramType.tsym));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression ClassLiteral(Symbol.ClassSymbol paramClassSymbol) {
/* 633 */     return ClassLiteral(paramClassSymbol.type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression ClassLiteral(Type paramType) {
/* 639 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(25L, this.names._class, paramType, (Symbol)paramType.tsym);
/*     */ 
/*     */ 
/*     */     
/* 643 */     return Select(Type(paramType), (Symbol)varSymbol);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCIdent Super(Type paramType, Symbol.TypeSymbol paramTypeSymbol) {
/* 649 */     return Ident((Symbol)new Symbol.VarSymbol(16L, this.names._super, paramType, (Symbol)paramTypeSymbol));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCMethodInvocation App(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList) {
/* 657 */     return Apply(null, paramJCExpression, paramList).setType(paramJCExpression.type.getReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCMethodInvocation App(JCTree.JCExpression paramJCExpression) {
/* 664 */     return Apply(null, paramJCExpression, List.nil()).setType(paramJCExpression.type.getReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCExpression Create(Symbol paramSymbol, List<JCTree.JCExpression> paramList) {
/* 670 */     Type type = paramSymbol.owner.erasure(this.types);
/* 671 */     JCTree.JCNewClass jCNewClass = NewClass(null, null, Type(type), paramList, null);
/* 672 */     jCNewClass.constructor = paramSymbol;
/* 673 */     jCNewClass.setType(type);
/* 674 */     return jCNewClass; } public JCTree.JCExpression Type(Type paramType) { JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTree; JCTree.JCIdent jCIdent;
/*     */     JCTree.JCWildcard jCWildcard;
/*     */     JCTree.JCExpression jCExpression1;
/*     */     Type.WildcardType wildcardType;
/*     */     Type type;
/*     */     JCTree.JCExpression jCExpression2;
/* 680 */     if (paramType == null) return null;
/*     */     
/* 682 */     switch (paramType.getTag()) { case BYTE: case CHAR: case SHORT: case INT: case LONG: case FLOAT: case DOUBLE:
/*     */       case BOOLEAN:
/*     */       case VOID:
/* 685 */         jCPrimitiveTypeTree = TypeIdent(paramType.getTag());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 713 */         return jCPrimitiveTypeTree.setType(paramType);case TYPEVAR: jCIdent = Ident((Symbol)paramType.tsym); return jCIdent.setType(paramType);case WILDCARD: wildcardType = (Type.WildcardType)paramType; jCWildcard = Wildcard(TypeBoundKind(wildcardType.kind), Type(wildcardType.type)); return jCWildcard.setType(paramType);case CLASS: type = paramType.getEnclosingType(); jCExpression2 = (type.hasTag(TypeTag.CLASS) && paramType.tsym.owner.kind == 2) ? Select(Type(type), (Symbol)paramType.tsym) : QualIdent((Symbol)paramType.tsym); jCExpression1 = paramType.getTypeArguments().isEmpty() ? jCExpression2 : TypeApply(jCExpression2, Types(paramType.getTypeArguments())); return jCExpression1.setType(paramType);case ARRAY: jCExpression1 = TypeArray(Type(this.types.elemtype(paramType))); return jCExpression1.setType(paramType);case ERROR: jCExpression1 = TypeIdent(TypeTag.ERROR); return jCExpression1.setType(paramType); }
/*     */     
/*     */     throw new AssertionError("unexpected type: " + paramType); }
/*     */ 
/*     */   
/*     */   public List<JCTree.JCExpression> Types(List<Type> paramList) {
/* 719 */     ListBuffer listBuffer = new ListBuffer();
/* 720 */     for (List<Type> list = paramList; list.nonEmpty(); list = list.tail)
/* 721 */       listBuffer.append(Type((Type)list.head)); 
/* 722 */     return listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCVariableDecl VarDef(Symbol.VarSymbol paramVarSymbol, JCTree.JCExpression paramJCExpression) {
/* 729 */     return (JCTree.JCVariableDecl)(new JCTree.JCVariableDecl(
/*     */         
/* 731 */         Modifiers(paramVarSymbol.flags(), Annotations(paramVarSymbol.getRawAttributes())), paramVarSymbol.name, 
/*     */         
/* 733 */         Type(paramVarSymbol.type), paramJCExpression, paramVarSymbol))
/*     */       
/* 735 */       .setPos(this.pos).setType(paramVarSymbol.type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCAnnotation> Annotations(List<Attribute.Compound> paramList) {
/* 741 */     if (paramList == null) return List.nil(); 
/* 742 */     ListBuffer listBuffer = new ListBuffer();
/* 743 */     for (List<Attribute.Compound> list = paramList; list.nonEmpty(); list = list.tail) {
/* 744 */       Attribute attribute = (Attribute)list.head;
/* 745 */       listBuffer.append(Annotation(attribute));
/*     */     } 
/* 747 */     return listBuffer.toList();
/*     */   }
/*     */   
/*     */   public JCTree.JCLiteral Literal(Object paramObject) {
/* 751 */     JCTree.JCLiteral jCLiteral = null;
/* 752 */     if (paramObject instanceof String) {
/*     */       
/* 754 */       jCLiteral = Literal(TypeTag.CLASS, paramObject).setType(this.syms.stringType.constType(paramObject));
/* 755 */     } else if (paramObject instanceof Integer) {
/*     */       
/* 757 */       jCLiteral = Literal(TypeTag.INT, paramObject).setType(this.syms.intType.constType(paramObject));
/* 758 */     } else if (paramObject instanceof Long) {
/*     */       
/* 760 */       jCLiteral = Literal(TypeTag.LONG, paramObject).setType(this.syms.longType.constType(paramObject));
/* 761 */     } else if (paramObject instanceof Byte) {
/*     */       
/* 763 */       jCLiteral = Literal(TypeTag.BYTE, paramObject).setType(this.syms.byteType.constType(paramObject));
/* 764 */     } else if (paramObject instanceof Character) {
/* 765 */       char c = ((Character)paramObject).toString().charAt(0);
/*     */       
/* 767 */       jCLiteral = Literal(TypeTag.CHAR, paramObject).setType(this.syms.charType.constType(Integer.valueOf(c)));
/* 768 */     } else if (paramObject instanceof Double) {
/*     */       
/* 770 */       jCLiteral = Literal(TypeTag.DOUBLE, paramObject).setType(this.syms.doubleType.constType(paramObject));
/* 771 */     } else if (paramObject instanceof Float) {
/*     */       
/* 773 */       jCLiteral = Literal(TypeTag.FLOAT, paramObject).setType(this.syms.floatType.constType(paramObject));
/* 774 */     } else if (paramObject instanceof Short) {
/*     */       
/* 776 */       jCLiteral = Literal(TypeTag.SHORT, paramObject).setType(this.syms.shortType.constType(paramObject));
/* 777 */     } else if (paramObject instanceof Boolean) {
/* 778 */       boolean bool = ((Boolean)paramObject).booleanValue() ? true : false;
/*     */       
/* 780 */       jCLiteral = Literal(TypeTag.BOOLEAN, Integer.valueOf(bool)).setType(this.syms.booleanType.constType(Integer.valueOf(bool)));
/*     */     } else {
/* 782 */       throw new AssertionError(paramObject);
/*     */     } 
/* 784 */     return jCLiteral;
/*     */   }
/*     */   
/*     */   class AnnotationBuilder implements Attribute.Visitor {
/* 788 */     JCTree.JCExpression result = null;
/*     */     public void visitConstant(Attribute.Constant param1Constant) {
/* 790 */       this.result = TreeMaker.this.Literal(param1Constant.type.getTag(), param1Constant.value);
/*     */     }
/*     */     public void visitClass(Attribute.Class param1Class) {
/* 793 */       this.result = TreeMaker.this.ClassLiteral(param1Class.classType).setType(TreeMaker.this.syms.classType);
/*     */     }
/*     */     public void visitEnum(Attribute.Enum param1Enum) {
/* 796 */       this.result = TreeMaker.this.QualIdent((Symbol)param1Enum.value);
/*     */     }
/*     */     public void visitError(Attribute.Error param1Error) {
/* 799 */       this.result = TreeMaker.this.Erroneous();
/*     */     }
/*     */     public void visitCompound(Attribute.Compound param1Compound) {
/* 802 */       if (param1Compound instanceof Attribute.TypeCompound) {
/* 803 */         this.result = visitTypeCompoundInternal((Attribute.TypeCompound)param1Compound);
/*     */       } else {
/* 805 */         this.result = visitCompoundInternal(param1Compound);
/*     */       } 
/*     */     }
/*     */     public JCTree.JCAnnotation visitCompoundInternal(Attribute.Compound param1Compound) {
/* 809 */       ListBuffer listBuffer = new ListBuffer();
/* 810 */       for (List list = param1Compound.values; list.nonEmpty(); list = list.tail) {
/* 811 */         Pair pair = (Pair)list.head;
/* 812 */         JCTree.JCExpression jCExpression = translate((Attribute)pair.snd);
/* 813 */         listBuffer.append(TreeMaker.this.Assign(TreeMaker.this.Ident((Symbol)pair.fst), jCExpression).setType(jCExpression.type));
/*     */       } 
/* 815 */       return TreeMaker.this.Annotation(TreeMaker.this.Type(param1Compound.type), listBuffer.toList());
/*     */     }
/*     */     public JCTree.JCAnnotation visitTypeCompoundInternal(Attribute.TypeCompound param1TypeCompound) {
/* 818 */       ListBuffer listBuffer = new ListBuffer();
/* 819 */       for (List list = param1TypeCompound.values; list.nonEmpty(); list = list.tail) {
/* 820 */         Pair pair = (Pair)list.head;
/* 821 */         JCTree.JCExpression jCExpression = translate((Attribute)pair.snd);
/* 822 */         listBuffer.append(TreeMaker.this.Assign(TreeMaker.this.Ident((Symbol)pair.fst), jCExpression).setType(jCExpression.type));
/*     */       } 
/* 824 */       return TreeMaker.this.TypeAnnotation(TreeMaker.this.Type(param1TypeCompound.type), listBuffer.toList());
/*     */     }
/*     */     public void visitArray(Attribute.Array param1Array) {
/* 827 */       ListBuffer listBuffer = new ListBuffer();
/* 828 */       for (byte b = 0; b < param1Array.values.length; b++)
/* 829 */         listBuffer.append(translate(param1Array.values[b])); 
/* 830 */       this.result = TreeMaker.this.NewArray(null, List.nil(), listBuffer.toList()).setType(param1Array.type);
/*     */     }
/*     */     JCTree.JCExpression translate(Attribute param1Attribute) {
/* 833 */       param1Attribute.accept(this);
/* 834 */       return this.result;
/*     */     }
/*     */     JCTree.JCAnnotation translate(Attribute.Compound param1Compound) {
/* 837 */       return visitCompoundInternal(param1Compound);
/*     */     }
/*     */     JCTree.JCAnnotation translate(Attribute.TypeCompound param1TypeCompound) {
/* 840 */       return visitTypeCompoundInternal(param1TypeCompound);
/*     */     }
/*     */   }
/*     */   
/* 844 */   protected TreeMaker(Context paramContext) { this.annotationBuilder = new AnnotationBuilder(); paramContext.put(treeMakerKey, this); this.pos = -1; this.toplevel = null; this.names = Names.instance(paramContext); this.syms = Symtab.instance(paramContext); this.types = Types.instance(paramContext); } protected TreeMaker(JCTree.JCCompilationUnit paramJCCompilationUnit, Names paramNames, Types paramTypes, Symtab paramSymtab) { this.annotationBuilder = new AnnotationBuilder();
/*     */     this.pos = 0;
/*     */     this.toplevel = paramJCCompilationUnit;
/*     */     this.names = paramNames;
/*     */     this.types = paramTypes;
/* 849 */     this.syms = paramSymtab; } public JCTree.JCAnnotation Annotation(Attribute paramAttribute) { return this.annotationBuilder.translate((Attribute.Compound)paramAttribute); }
/*     */ 
/*     */   
/*     */   public JCTree.JCAnnotation TypeAnnotation(Attribute paramAttribute) {
/* 853 */     return this.annotationBuilder.translate((Attribute.TypeCompound)paramAttribute);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCMethodDecl MethodDef(Symbol.MethodSymbol paramMethodSymbol, JCTree.JCBlock paramJCBlock) {
/* 859 */     return MethodDef(paramMethodSymbol, paramMethodSymbol.type, paramJCBlock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCMethodDecl MethodDef(Symbol.MethodSymbol paramMethodSymbol, Type paramType, JCTree.JCBlock paramJCBlock) {
/* 866 */     return (JCTree.JCMethodDecl)(new JCTree.JCMethodDecl(
/*     */         
/* 868 */         Modifiers(paramMethodSymbol.flags(), Annotations(paramMethodSymbol.getRawAttributes())), paramMethodSymbol.name, 
/*     */         
/* 870 */         Type(paramType.getReturnType()), 
/* 871 */         TypeParams(paramType.getTypeArguments()), null, 
/*     */         
/* 873 */         Params(paramType.getParameterTypes(), (Symbol)paramMethodSymbol), 
/* 874 */         Types(paramType.getThrownTypes()), paramJCBlock, null, paramMethodSymbol))
/*     */ 
/*     */       
/* 877 */       .setPos(this.pos).setType(paramType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCTypeParameter TypeParam(Name paramName, Type.TypeVar paramTypeVar) {
/* 883 */     return (JCTree.JCTypeParameter)
/* 884 */       TypeParameter(paramName, Types(this.types.getBounds(paramTypeVar))).setPos(this.pos).setType((Type)paramTypeVar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCTypeParameter> TypeParams(List<Type> paramList) {
/* 890 */     ListBuffer listBuffer = new ListBuffer();
/* 891 */     for (List<Type> list = paramList; list.nonEmpty(); list = list.tail)
/* 892 */       listBuffer.append(TypeParam(((Type)list.head).tsym.name, (Type.TypeVar)list.head)); 
/* 893 */     return listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCVariableDecl Param(Name paramName, Type paramType, Symbol paramSymbol) {
/* 899 */     return VarDef(new Symbol.VarSymbol(8589934592L, paramName, paramType, paramSymbol), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCVariableDecl> Params(List<Type> paramList, Symbol paramSymbol) {
/* 906 */     ListBuffer listBuffer = new ListBuffer();
/* 907 */     Symbol.MethodSymbol methodSymbol = (paramSymbol.kind == 16) ? (Symbol.MethodSymbol)paramSymbol : null;
/* 908 */     if (methodSymbol != null && methodSymbol.params != null && paramList.length() == methodSymbol.params.length()) {
/* 909 */       for (Symbol.VarSymbol varSymbol : ((Symbol.MethodSymbol)paramSymbol).params)
/* 910 */         listBuffer.append(VarDef(varSymbol, null)); 
/*     */     } else {
/* 912 */       byte b = 0;
/* 913 */       for (List<Type> list = paramList; list.nonEmpty(); list = list.tail)
/* 914 */         listBuffer.append(Param(paramName(b++), (Type)list.head, paramSymbol)); 
/*     */     } 
/* 916 */     return listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCStatement Call(JCTree.JCExpression paramJCExpression) {
/* 923 */     return paramJCExpression.type.hasTag(TypeTag.VOID) ? Exec(paramJCExpression) : Return(paramJCExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCStatement Assignment(Symbol paramSymbol, JCTree.JCExpression paramJCExpression) {
/* 929 */     return Exec(Assign(Ident(paramSymbol), paramJCExpression).setType(paramSymbol.type));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCArrayAccess Indexed(Symbol paramSymbol, JCTree.JCExpression paramJCExpression) {
/* 935 */     JCTree.JCArrayAccess jCArrayAccess = new JCTree.JCArrayAccess(QualIdent(paramSymbol), paramJCExpression);
/* 936 */     jCArrayAccess.type = ((Type.ArrayType)paramSymbol.type).elemtype;
/* 937 */     return jCArrayAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JCTree.JCTypeCast TypeCast(Type paramType, JCTree.JCExpression paramJCExpression) {
/* 943 */     return (JCTree.JCTypeCast)TypeCast(Type(paramType), paramJCExpression).setType(paramType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isUnqualifiable(Symbol paramSymbol) {
/* 953 */     if (paramSymbol.name == this.names.empty || paramSymbol.owner == null || paramSymbol.owner == this.syms.rootPackage || paramSymbol.owner.kind == 16 || paramSymbol.owner.kind == 4)
/*     */     {
/*     */ 
/*     */       
/* 957 */       return true; } 
/* 958 */     if (paramSymbol.kind == 2 && this.toplevel != null) {
/*     */       
/* 960 */       Scope.Entry entry = this.toplevel.namedImportScope.lookup(paramSymbol.name);
/* 961 */       if (entry.scope != null) {
/* 962 */         return (entry.sym == paramSymbol && 
/*     */           
/* 964 */           (entry.next()).scope == null);
/*     */       }
/* 966 */       entry = this.toplevel.packge.members().lookup(paramSymbol.name);
/* 967 */       if (entry.scope != null) {
/* 968 */         return (entry.sym == paramSymbol && 
/*     */           
/* 970 */           (entry.next()).scope == null);
/*     */       }
/* 972 */       entry = this.toplevel.starImportScope.lookup(paramSymbol.name);
/* 973 */       if (entry.scope != null) {
/* 974 */         return (entry.sym == paramSymbol && 
/*     */           
/* 976 */           (entry.next()).scope == null);
/*     */       }
/*     */     } 
/* 979 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Name paramName(int paramInt) {
/* 984 */     return this.names.fromString("x" + paramInt);
/*     */   }
/*     */   
/*     */   public Name typaramName(int paramInt) {
/* 988 */     return this.names.fromString("A" + paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\TreeMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */