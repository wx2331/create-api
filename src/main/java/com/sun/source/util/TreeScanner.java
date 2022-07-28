/*     */ package com.sun.source.util;
/*     */ 
/*     */ import com.sun.source.tree.AnnotatedTypeTree;
/*     */ import com.sun.source.tree.AnnotationTree;
/*     */ import com.sun.source.tree.ArrayAccessTree;
/*     */ import com.sun.source.tree.ArrayTypeTree;
/*     */ import com.sun.source.tree.AssertTree;
/*     */ import com.sun.source.tree.AssignmentTree;
/*     */ import com.sun.source.tree.BinaryTree;
/*     */ import com.sun.source.tree.BlockTree;
/*     */ import com.sun.source.tree.BreakTree;
/*     */ import com.sun.source.tree.CaseTree;
/*     */ import com.sun.source.tree.CatchTree;
/*     */ import com.sun.source.tree.ClassTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.CompoundAssignmentTree;
/*     */ import com.sun.source.tree.ConditionalExpressionTree;
/*     */ import com.sun.source.tree.ContinueTree;
/*     */ import com.sun.source.tree.DoWhileLoopTree;
/*     */ import com.sun.source.tree.EmptyStatementTree;
/*     */ import com.sun.source.tree.EnhancedForLoopTree;
/*     */ import com.sun.source.tree.ErroneousTree;
/*     */ import com.sun.source.tree.ExpressionStatementTree;
/*     */ import com.sun.source.tree.ForLoopTree;
/*     */ import com.sun.source.tree.IdentifierTree;
/*     */ import com.sun.source.tree.IfTree;
/*     */ import com.sun.source.tree.ImportTree;
/*     */ import com.sun.source.tree.InstanceOfTree;
/*     */ import com.sun.source.tree.IntersectionTypeTree;
/*     */ import com.sun.source.tree.LabeledStatementTree;
/*     */ import com.sun.source.tree.LambdaExpressionTree;
/*     */ import com.sun.source.tree.LiteralTree;
/*     */ import com.sun.source.tree.MemberReferenceTree;
/*     */ import com.sun.source.tree.MemberSelectTree;
/*     */ import com.sun.source.tree.MethodInvocationTree;
/*     */ import com.sun.source.tree.MethodTree;
/*     */ import com.sun.source.tree.ModifiersTree;
/*     */ import com.sun.source.tree.NewArrayTree;
/*     */ import com.sun.source.tree.NewClassTree;
/*     */ import com.sun.source.tree.ParameterizedTypeTree;
/*     */ import com.sun.source.tree.ParenthesizedTree;
/*     */ import com.sun.source.tree.PrimitiveTypeTree;
/*     */ import com.sun.source.tree.ReturnTree;
/*     */ import com.sun.source.tree.SwitchTree;
/*     */ import com.sun.source.tree.SynchronizedTree;
/*     */ import com.sun.source.tree.ThrowTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.tree.TreeVisitor;
/*     */ import com.sun.source.tree.TryTree;
/*     */ import com.sun.source.tree.TypeCastTree;
/*     */ import com.sun.source.tree.TypeParameterTree;
/*     */ import com.sun.source.tree.UnaryTree;
/*     */ import com.sun.source.tree.UnionTypeTree;
/*     */ import com.sun.source.tree.VariableTree;
/*     */ import com.sun.source.tree.WhileLoopTree;
/*     */ import com.sun.source.tree.WildcardTree;
/*     */ import jdk.Exported;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Exported
/*     */ public class TreeScanner<R, P>
/*     */   implements TreeVisitor<R, P>
/*     */ {
/*     */   public R scan(Tree paramTree, P paramP) {
/*  77 */     return (paramTree == null) ? null : (R)paramTree.accept(this, paramP);
/*     */   }
/*     */   
/*     */   private R scanAndReduce(Tree paramTree, P paramP, R paramR) {
/*  81 */     return reduce(scan(paramTree, paramP), paramR);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public R scan(Iterable<? extends Tree> paramIterable, P paramP) {
/*  87 */     R r = null;
/*  88 */     if (paramIterable != null) {
/*  89 */       boolean bool = true;
/*  90 */       for (Tree tree : paramIterable) {
/*  91 */         r = bool ? scan(tree, paramP) : scanAndReduce(tree, paramP, r);
/*  92 */         bool = false;
/*     */       } 
/*     */     } 
/*  95 */     return r;
/*     */   }
/*     */   
/*     */   private R scanAndReduce(Iterable<? extends Tree> paramIterable, P paramP, R paramR) {
/*  99 */     return reduce(scan(paramIterable, paramP), paramR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R reduce(R paramR1, R paramR2) {
/* 108 */     return paramR1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R visitCompilationUnit(CompilationUnitTree paramCompilationUnitTree, P paramP) {
/* 117 */     R r = scan(paramCompilationUnitTree.getPackageAnnotations(), paramP);
/* 118 */     r = scanAndReduce((Tree)paramCompilationUnitTree.getPackageName(), paramP, r);
/* 119 */     r = scanAndReduce(paramCompilationUnitTree.getImports(), paramP, r);
/* 120 */     r = scanAndReduce(paramCompilationUnitTree.getTypeDecls(), paramP, r);
/* 121 */     return r;
/*     */   }
/*     */   
/*     */   public R visitImport(ImportTree paramImportTree, P paramP) {
/* 125 */     return scan(paramImportTree.getQualifiedIdentifier(), paramP);
/*     */   }
/*     */   
/*     */   public R visitClass(ClassTree paramClassTree, P paramP) {
/* 129 */     R r = scan((Tree)paramClassTree.getModifiers(), paramP);
/* 130 */     r = scanAndReduce(paramClassTree.getTypeParameters(), paramP, r);
/* 131 */     r = scanAndReduce(paramClassTree.getExtendsClause(), paramP, r);
/* 132 */     r = scanAndReduce(paramClassTree.getImplementsClause(), paramP, r);
/* 133 */     r = scanAndReduce(paramClassTree.getMembers(), paramP, r);
/* 134 */     return r;
/*     */   }
/*     */   
/*     */   public R visitMethod(MethodTree paramMethodTree, P paramP) {
/* 138 */     R r = scan((Tree)paramMethodTree.getModifiers(), paramP);
/* 139 */     r = scanAndReduce(paramMethodTree.getReturnType(), paramP, r);
/* 140 */     r = scanAndReduce(paramMethodTree.getTypeParameters(), paramP, r);
/* 141 */     r = scanAndReduce(paramMethodTree.getParameters(), paramP, r);
/* 142 */     r = scanAndReduce((Tree)paramMethodTree.getReceiverParameter(), paramP, r);
/* 143 */     r = scanAndReduce(paramMethodTree.getThrows(), paramP, r);
/* 144 */     r = scanAndReduce((Tree)paramMethodTree.getBody(), paramP, r);
/* 145 */     r = scanAndReduce(paramMethodTree.getDefaultValue(), paramP, r);
/* 146 */     return r;
/*     */   }
/*     */   
/*     */   public R visitVariable(VariableTree paramVariableTree, P paramP) {
/* 150 */     R r = scan((Tree)paramVariableTree.getModifiers(), paramP);
/* 151 */     r = scanAndReduce(paramVariableTree.getType(), paramP, r);
/* 152 */     r = scanAndReduce((Tree)paramVariableTree.getNameExpression(), paramP, r);
/* 153 */     r = scanAndReduce((Tree)paramVariableTree.getInitializer(), paramP, r);
/* 154 */     return r;
/*     */   }
/*     */   
/*     */   public R visitEmptyStatement(EmptyStatementTree paramEmptyStatementTree, P paramP) {
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   public R visitBlock(BlockTree paramBlockTree, P paramP) {
/* 162 */     return scan(paramBlockTree.getStatements(), paramP);
/*     */   }
/*     */   
/*     */   public R visitDoWhileLoop(DoWhileLoopTree paramDoWhileLoopTree, P paramP) {
/* 166 */     R r = scan((Tree)paramDoWhileLoopTree.getStatement(), paramP);
/* 167 */     r = scanAndReduce((Tree)paramDoWhileLoopTree.getCondition(), paramP, r);
/* 168 */     return r;
/*     */   }
/*     */   
/*     */   public R visitWhileLoop(WhileLoopTree paramWhileLoopTree, P paramP) {
/* 172 */     R r = scan((Tree)paramWhileLoopTree.getCondition(), paramP);
/* 173 */     r = scanAndReduce((Tree)paramWhileLoopTree.getStatement(), paramP, r);
/* 174 */     return r;
/*     */   }
/*     */   
/*     */   public R visitForLoop(ForLoopTree paramForLoopTree, P paramP) {
/* 178 */     R r = scan(paramForLoopTree.getInitializer(), paramP);
/* 179 */     r = scanAndReduce((Tree)paramForLoopTree.getCondition(), paramP, r);
/* 180 */     r = scanAndReduce(paramForLoopTree.getUpdate(), paramP, r);
/* 181 */     r = scanAndReduce((Tree)paramForLoopTree.getStatement(), paramP, r);
/* 182 */     return r;
/*     */   }
/*     */   
/*     */   public R visitEnhancedForLoop(EnhancedForLoopTree paramEnhancedForLoopTree, P paramP) {
/* 186 */     R r = scan((Tree)paramEnhancedForLoopTree.getVariable(), paramP);
/* 187 */     r = scanAndReduce((Tree)paramEnhancedForLoopTree.getExpression(), paramP, r);
/* 188 */     r = scanAndReduce((Tree)paramEnhancedForLoopTree.getStatement(), paramP, r);
/* 189 */     return r;
/*     */   }
/*     */   
/*     */   public R visitLabeledStatement(LabeledStatementTree paramLabeledStatementTree, P paramP) {
/* 193 */     return scan((Tree)paramLabeledStatementTree.getStatement(), paramP);
/*     */   }
/*     */   
/*     */   public R visitSwitch(SwitchTree paramSwitchTree, P paramP) {
/* 197 */     R r = scan((Tree)paramSwitchTree.getExpression(), paramP);
/* 198 */     r = scanAndReduce(paramSwitchTree.getCases(), paramP, r);
/* 199 */     return r;
/*     */   }
/*     */   
/*     */   public R visitCase(CaseTree paramCaseTree, P paramP) {
/* 203 */     R r = scan((Tree)paramCaseTree.getExpression(), paramP);
/* 204 */     r = scanAndReduce(paramCaseTree.getStatements(), paramP, r);
/* 205 */     return r;
/*     */   }
/*     */   
/*     */   public R visitSynchronized(SynchronizedTree paramSynchronizedTree, P paramP) {
/* 209 */     R r = scan((Tree)paramSynchronizedTree.getExpression(), paramP);
/* 210 */     r = scanAndReduce((Tree)paramSynchronizedTree.getBlock(), paramP, r);
/* 211 */     return r;
/*     */   }
/*     */   
/*     */   public R visitTry(TryTree paramTryTree, P paramP) {
/* 215 */     R r = scan(paramTryTree.getResources(), paramP);
/* 216 */     r = scanAndReduce((Tree)paramTryTree.getBlock(), paramP, r);
/* 217 */     r = scanAndReduce(paramTryTree.getCatches(), paramP, r);
/* 218 */     r = scanAndReduce((Tree)paramTryTree.getFinallyBlock(), paramP, r);
/* 219 */     return r;
/*     */   }
/*     */   
/*     */   public R visitCatch(CatchTree paramCatchTree, P paramP) {
/* 223 */     R r = scan((Tree)paramCatchTree.getParameter(), paramP);
/* 224 */     r = scanAndReduce((Tree)paramCatchTree.getBlock(), paramP, r);
/* 225 */     return r;
/*     */   }
/*     */   
/*     */   public R visitConditionalExpression(ConditionalExpressionTree paramConditionalExpressionTree, P paramP) {
/* 229 */     R r = scan((Tree)paramConditionalExpressionTree.getCondition(), paramP);
/* 230 */     r = scanAndReduce((Tree)paramConditionalExpressionTree.getTrueExpression(), paramP, r);
/* 231 */     r = scanAndReduce((Tree)paramConditionalExpressionTree.getFalseExpression(), paramP, r);
/* 232 */     return r;
/*     */   }
/*     */   
/*     */   public R visitIf(IfTree paramIfTree, P paramP) {
/* 236 */     R r = scan((Tree)paramIfTree.getCondition(), paramP);
/* 237 */     r = scanAndReduce((Tree)paramIfTree.getThenStatement(), paramP, r);
/* 238 */     r = scanAndReduce((Tree)paramIfTree.getElseStatement(), paramP, r);
/* 239 */     return r;
/*     */   }
/*     */   
/*     */   public R visitExpressionStatement(ExpressionStatementTree paramExpressionStatementTree, P paramP) {
/* 243 */     return scan((Tree)paramExpressionStatementTree.getExpression(), paramP);
/*     */   }
/*     */   
/*     */   public R visitBreak(BreakTree paramBreakTree, P paramP) {
/* 247 */     return null;
/*     */   }
/*     */   
/*     */   public R visitContinue(ContinueTree paramContinueTree, P paramP) {
/* 251 */     return null;
/*     */   }
/*     */   
/*     */   public R visitReturn(ReturnTree paramReturnTree, P paramP) {
/* 255 */     return scan((Tree)paramReturnTree.getExpression(), paramP);
/*     */   }
/*     */   
/*     */   public R visitThrow(ThrowTree paramThrowTree, P paramP) {
/* 259 */     return scan((Tree)paramThrowTree.getExpression(), paramP);
/*     */   }
/*     */   
/*     */   public R visitAssert(AssertTree paramAssertTree, P paramP) {
/* 263 */     R r = scan((Tree)paramAssertTree.getCondition(), paramP);
/* 264 */     r = scanAndReduce((Tree)paramAssertTree.getDetail(), paramP, r);
/* 265 */     return r;
/*     */   }
/*     */   
/*     */   public R visitMethodInvocation(MethodInvocationTree paramMethodInvocationTree, P paramP) {
/* 269 */     R r = scan(paramMethodInvocationTree.getTypeArguments(), paramP);
/* 270 */     r = scanAndReduce((Tree)paramMethodInvocationTree.getMethodSelect(), paramP, r);
/* 271 */     r = scanAndReduce(paramMethodInvocationTree.getArguments(), paramP, r);
/* 272 */     return r;
/*     */   }
/*     */   
/*     */   public R visitNewClass(NewClassTree paramNewClassTree, P paramP) {
/* 276 */     R r = scan((Tree)paramNewClassTree.getEnclosingExpression(), paramP);
/* 277 */     r = scanAndReduce((Tree)paramNewClassTree.getIdentifier(), paramP, r);
/* 278 */     r = scanAndReduce(paramNewClassTree.getTypeArguments(), paramP, r);
/* 279 */     r = scanAndReduce(paramNewClassTree.getArguments(), paramP, r);
/* 280 */     r = scanAndReduce((Tree)paramNewClassTree.getClassBody(), paramP, r);
/* 281 */     return r;
/*     */   }
/*     */   
/*     */   public R visitNewArray(NewArrayTree paramNewArrayTree, P paramP) {
/* 285 */     R r = scan(paramNewArrayTree.getType(), paramP);
/* 286 */     r = scanAndReduce(paramNewArrayTree.getDimensions(), paramP, r);
/* 287 */     r = scanAndReduce(paramNewArrayTree.getInitializers(), paramP, r);
/* 288 */     r = scanAndReduce(paramNewArrayTree.getAnnotations(), paramP, r);
/* 289 */     for (Iterable<? extends Tree> iterable : (Iterable<Iterable<? extends Tree>>)paramNewArrayTree.getDimAnnotations()) {
/* 290 */       r = scanAndReduce(iterable, paramP, r);
/*     */     }
/* 292 */     return r;
/*     */   }
/*     */   
/*     */   public R visitLambdaExpression(LambdaExpressionTree paramLambdaExpressionTree, P paramP) {
/* 296 */     R r = scan(paramLambdaExpressionTree.getParameters(), paramP);
/* 297 */     r = scanAndReduce(paramLambdaExpressionTree.getBody(), paramP, r);
/* 298 */     return r;
/*     */   }
/*     */   
/*     */   public R visitParenthesized(ParenthesizedTree paramParenthesizedTree, P paramP) {
/* 302 */     return scan((Tree)paramParenthesizedTree.getExpression(), paramP);
/*     */   }
/*     */   
/*     */   public R visitAssignment(AssignmentTree paramAssignmentTree, P paramP) {
/* 306 */     R r = scan((Tree)paramAssignmentTree.getVariable(), paramP);
/* 307 */     r = scanAndReduce((Tree)paramAssignmentTree.getExpression(), paramP, r);
/* 308 */     return r;
/*     */   }
/*     */   
/*     */   public R visitCompoundAssignment(CompoundAssignmentTree paramCompoundAssignmentTree, P paramP) {
/* 312 */     R r = scan((Tree)paramCompoundAssignmentTree.getVariable(), paramP);
/* 313 */     r = scanAndReduce((Tree)paramCompoundAssignmentTree.getExpression(), paramP, r);
/* 314 */     return r;
/*     */   }
/*     */   
/*     */   public R visitUnary(UnaryTree paramUnaryTree, P paramP) {
/* 318 */     return scan((Tree)paramUnaryTree.getExpression(), paramP);
/*     */   }
/*     */   
/*     */   public R visitBinary(BinaryTree paramBinaryTree, P paramP) {
/* 322 */     R r = scan((Tree)paramBinaryTree.getLeftOperand(), paramP);
/* 323 */     r = scanAndReduce((Tree)paramBinaryTree.getRightOperand(), paramP, r);
/* 324 */     return r;
/*     */   }
/*     */   
/*     */   public R visitTypeCast(TypeCastTree paramTypeCastTree, P paramP) {
/* 328 */     R r = scan(paramTypeCastTree.getType(), paramP);
/* 329 */     r = scanAndReduce((Tree)paramTypeCastTree.getExpression(), paramP, r);
/* 330 */     return r;
/*     */   }
/*     */   
/*     */   public R visitInstanceOf(InstanceOfTree paramInstanceOfTree, P paramP) {
/* 334 */     R r = scan((Tree)paramInstanceOfTree.getExpression(), paramP);
/* 335 */     r = scanAndReduce(paramInstanceOfTree.getType(), paramP, r);
/* 336 */     return r;
/*     */   }
/*     */   
/*     */   public R visitArrayAccess(ArrayAccessTree paramArrayAccessTree, P paramP) {
/* 340 */     R r = scan((Tree)paramArrayAccessTree.getExpression(), paramP);
/* 341 */     r = scanAndReduce((Tree)paramArrayAccessTree.getIndex(), paramP, r);
/* 342 */     return r;
/*     */   }
/*     */   
/*     */   public R visitMemberSelect(MemberSelectTree paramMemberSelectTree, P paramP) {
/* 346 */     return scan((Tree)paramMemberSelectTree.getExpression(), paramP);
/*     */   }
/*     */   
/*     */   public R visitMemberReference(MemberReferenceTree paramMemberReferenceTree, P paramP) {
/* 350 */     R r = scan((Tree)paramMemberReferenceTree.getQualifierExpression(), paramP);
/* 351 */     r = scanAndReduce(paramMemberReferenceTree.getTypeArguments(), paramP, r);
/* 352 */     return r;
/*     */   }
/*     */   
/*     */   public R visitIdentifier(IdentifierTree paramIdentifierTree, P paramP) {
/* 356 */     return null;
/*     */   }
/*     */   
/*     */   public R visitLiteral(LiteralTree paramLiteralTree, P paramP) {
/* 360 */     return null;
/*     */   }
/*     */   
/*     */   public R visitPrimitiveType(PrimitiveTypeTree paramPrimitiveTypeTree, P paramP) {
/* 364 */     return null;
/*     */   }
/*     */   
/*     */   public R visitArrayType(ArrayTypeTree paramArrayTypeTree, P paramP) {
/* 368 */     return scan(paramArrayTypeTree.getType(), paramP);
/*     */   }
/*     */   
/*     */   public R visitParameterizedType(ParameterizedTypeTree paramParameterizedTypeTree, P paramP) {
/* 372 */     R r = scan(paramParameterizedTypeTree.getType(), paramP);
/* 373 */     r = scanAndReduce(paramParameterizedTypeTree.getTypeArguments(), paramP, r);
/* 374 */     return r;
/*     */   }
/*     */   
/*     */   public R visitUnionType(UnionTypeTree paramUnionTypeTree, P paramP) {
/* 378 */     return scan(paramUnionTypeTree.getTypeAlternatives(), paramP);
/*     */   }
/*     */   
/*     */   public R visitIntersectionType(IntersectionTypeTree paramIntersectionTypeTree, P paramP) {
/* 382 */     return scan(paramIntersectionTypeTree.getBounds(), paramP);
/*     */   }
/*     */   
/*     */   public R visitTypeParameter(TypeParameterTree paramTypeParameterTree, P paramP) {
/* 386 */     R r = scan(paramTypeParameterTree.getAnnotations(), paramP);
/* 387 */     r = scanAndReduce(paramTypeParameterTree.getBounds(), paramP, r);
/* 388 */     return r;
/*     */   }
/*     */   
/*     */   public R visitWildcard(WildcardTree paramWildcardTree, P paramP) {
/* 392 */     return scan(paramWildcardTree.getBound(), paramP);
/*     */   }
/*     */   
/*     */   public R visitModifiers(ModifiersTree paramModifiersTree, P paramP) {
/* 396 */     return scan(paramModifiersTree.getAnnotations(), paramP);
/*     */   }
/*     */   
/*     */   public R visitAnnotation(AnnotationTree paramAnnotationTree, P paramP) {
/* 400 */     R r = scan(paramAnnotationTree.getAnnotationType(), paramP);
/* 401 */     r = scanAndReduce(paramAnnotationTree.getArguments(), paramP, r);
/* 402 */     return r;
/*     */   }
/*     */   
/*     */   public R visitAnnotatedType(AnnotatedTypeTree paramAnnotatedTypeTree, P paramP) {
/* 406 */     R r = scan(paramAnnotatedTypeTree.getAnnotations(), paramP);
/* 407 */     r = scanAndReduce((Tree)paramAnnotatedTypeTree.getUnderlyingType(), paramP, r);
/* 408 */     return r;
/*     */   }
/*     */   
/*     */   public R visitOther(Tree paramTree, P paramP) {
/* 412 */     return null;
/*     */   }
/*     */   
/*     */   public R visitErroneous(ErroneousTree paramErroneousTree, P paramP) {
/* 416 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\TreeScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */