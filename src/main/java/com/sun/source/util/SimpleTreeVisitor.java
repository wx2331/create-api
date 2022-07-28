/*     */ package com.sun.source.util;
/*     */ import com.sun.source.tree.ArrayAccessTree;
/*     */ import com.sun.source.tree.ArrayTypeTree;
/*     */ import com.sun.source.tree.AssertTree;
/*     */ import com.sun.source.tree.AssignmentTree;
/*     */ import com.sun.source.tree.BreakTree;
/*     */ import com.sun.source.tree.ClassTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.CompoundAssignmentTree;
/*     */ import com.sun.source.tree.DoWhileLoopTree;
/*     */ import com.sun.source.tree.EmptyStatementTree;
/*     */ import com.sun.source.tree.EnhancedForLoopTree;
/*     */ import com.sun.source.tree.ErroneousTree;
/*     */ import com.sun.source.tree.ForLoopTree;
/*     */ import com.sun.source.tree.IdentifierTree;
/*     */ import com.sun.source.tree.ImportTree;
/*     */ import com.sun.source.tree.InstanceOfTree;
/*     */ import com.sun.source.tree.IntersectionTypeTree;
/*     */ import com.sun.source.tree.LabeledStatementTree;
/*     */ import com.sun.source.tree.LambdaExpressionTree;
/*     */ import com.sun.source.tree.MemberReferenceTree;
/*     */ import com.sun.source.tree.MemberSelectTree;
/*     */ import com.sun.source.tree.MethodInvocationTree;
/*     */ import com.sun.source.tree.MethodTree;
/*     */ import com.sun.source.tree.NewArrayTree;
/*     */ import com.sun.source.tree.ParameterizedTypeTree;
/*     */ import com.sun.source.tree.ParenthesizedTree;
/*     */ import com.sun.source.tree.PrimitiveTypeTree;
/*     */ import com.sun.source.tree.ReturnTree;
/*     */ import com.sun.source.tree.SynchronizedTree;
/*     */ import com.sun.source.tree.ThrowTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.tree.TryTree;
/*     */ import com.sun.source.tree.TypeCastTree;
/*     */ import com.sun.source.tree.TypeParameterTree;
/*     */ import com.sun.source.tree.WildcardTree;
/*     */ 
/*     */ @Exported
/*     */ public class SimpleTreeVisitor<R, P> implements TreeVisitor<R, P> {
/*     */   protected SimpleTreeVisitor() {
/*  41 */     this.DEFAULT_VALUE = null;
/*     */   }
/*     */   protected final R DEFAULT_VALUE;
/*     */   protected SimpleTreeVisitor(R paramR) {
/*  45 */     this.DEFAULT_VALUE = paramR;
/*     */   }
/*     */   
/*     */   protected R defaultAction(Tree paramTree, P paramP) {
/*  49 */     return this.DEFAULT_VALUE;
/*     */   }
/*     */   
/*     */   public final R visit(Tree paramTree, P paramP) {
/*  53 */     return (paramTree == null) ? null : (R)paramTree.accept(this, paramP);
/*     */   }
/*     */   
/*     */   public final R visit(Iterable<? extends Tree> paramIterable, P paramP) {
/*  57 */     R r = null;
/*  58 */     if (paramIterable != null)
/*  59 */       for (Tree tree : paramIterable)
/*  60 */         r = visit(tree, paramP);  
/*  61 */     return r;
/*     */   }
/*     */   
/*     */   public R visitCompilationUnit(CompilationUnitTree paramCompilationUnitTree, P paramP) {
/*  65 */     return defaultAction((Tree)paramCompilationUnitTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitImport(ImportTree paramImportTree, P paramP) {
/*  69 */     return defaultAction((Tree)paramImportTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitClass(ClassTree paramClassTree, P paramP) {
/*  73 */     return defaultAction((Tree)paramClassTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitMethod(MethodTree paramMethodTree, P paramP) {
/*  77 */     return defaultAction((Tree)paramMethodTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitVariable(VariableTree paramVariableTree, P paramP) {
/*  81 */     return defaultAction((Tree)paramVariableTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitEmptyStatement(EmptyStatementTree paramEmptyStatementTree, P paramP) {
/*  85 */     return defaultAction((Tree)paramEmptyStatementTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitBlock(BlockTree paramBlockTree, P paramP) {
/*  89 */     return defaultAction((Tree)paramBlockTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitDoWhileLoop(DoWhileLoopTree paramDoWhileLoopTree, P paramP) {
/*  93 */     return defaultAction((Tree)paramDoWhileLoopTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitWhileLoop(WhileLoopTree paramWhileLoopTree, P paramP) {
/*  97 */     return defaultAction((Tree)paramWhileLoopTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitForLoop(ForLoopTree paramForLoopTree, P paramP) {
/* 101 */     return defaultAction((Tree)paramForLoopTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitEnhancedForLoop(EnhancedForLoopTree paramEnhancedForLoopTree, P paramP) {
/* 105 */     return defaultAction((Tree)paramEnhancedForLoopTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitLabeledStatement(LabeledStatementTree paramLabeledStatementTree, P paramP) {
/* 109 */     return defaultAction((Tree)paramLabeledStatementTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitSwitch(SwitchTree paramSwitchTree, P paramP) {
/* 113 */     return defaultAction((Tree)paramSwitchTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitCase(CaseTree paramCaseTree, P paramP) {
/* 117 */     return defaultAction((Tree)paramCaseTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitSynchronized(SynchronizedTree paramSynchronizedTree, P paramP) {
/* 121 */     return defaultAction((Tree)paramSynchronizedTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitTry(TryTree paramTryTree, P paramP) {
/* 125 */     return defaultAction((Tree)paramTryTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitCatch(CatchTree paramCatchTree, P paramP) {
/* 129 */     return defaultAction((Tree)paramCatchTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitConditionalExpression(ConditionalExpressionTree paramConditionalExpressionTree, P paramP) {
/* 133 */     return defaultAction((Tree)paramConditionalExpressionTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitIf(IfTree paramIfTree, P paramP) {
/* 137 */     return defaultAction((Tree)paramIfTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitExpressionStatement(ExpressionStatementTree paramExpressionStatementTree, P paramP) {
/* 141 */     return defaultAction((Tree)paramExpressionStatementTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitBreak(BreakTree paramBreakTree, P paramP) {
/* 145 */     return defaultAction((Tree)paramBreakTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitContinue(ContinueTree paramContinueTree, P paramP) {
/* 149 */     return defaultAction((Tree)paramContinueTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitReturn(ReturnTree paramReturnTree, P paramP) {
/* 153 */     return defaultAction((Tree)paramReturnTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitThrow(ThrowTree paramThrowTree, P paramP) {
/* 157 */     return defaultAction((Tree)paramThrowTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitAssert(AssertTree paramAssertTree, P paramP) {
/* 161 */     return defaultAction((Tree)paramAssertTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitMethodInvocation(MethodInvocationTree paramMethodInvocationTree, P paramP) {
/* 165 */     return defaultAction((Tree)paramMethodInvocationTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitNewClass(NewClassTree paramNewClassTree, P paramP) {
/* 169 */     return defaultAction((Tree)paramNewClassTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitNewArray(NewArrayTree paramNewArrayTree, P paramP) {
/* 173 */     return defaultAction((Tree)paramNewArrayTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitLambdaExpression(LambdaExpressionTree paramLambdaExpressionTree, P paramP) {
/* 177 */     return defaultAction((Tree)paramLambdaExpressionTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitParenthesized(ParenthesizedTree paramParenthesizedTree, P paramP) {
/* 181 */     return defaultAction((Tree)paramParenthesizedTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitAssignment(AssignmentTree paramAssignmentTree, P paramP) {
/* 185 */     return defaultAction((Tree)paramAssignmentTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitCompoundAssignment(CompoundAssignmentTree paramCompoundAssignmentTree, P paramP) {
/* 189 */     return defaultAction((Tree)paramCompoundAssignmentTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitUnary(UnaryTree paramUnaryTree, P paramP) {
/* 193 */     return defaultAction((Tree)paramUnaryTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitBinary(BinaryTree paramBinaryTree, P paramP) {
/* 197 */     return defaultAction((Tree)paramBinaryTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitTypeCast(TypeCastTree paramTypeCastTree, P paramP) {
/* 201 */     return defaultAction((Tree)paramTypeCastTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitInstanceOf(InstanceOfTree paramInstanceOfTree, P paramP) {
/* 205 */     return defaultAction((Tree)paramInstanceOfTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitArrayAccess(ArrayAccessTree paramArrayAccessTree, P paramP) {
/* 209 */     return defaultAction((Tree)paramArrayAccessTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitMemberSelect(MemberSelectTree paramMemberSelectTree, P paramP) {
/* 213 */     return defaultAction((Tree)paramMemberSelectTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitMemberReference(MemberReferenceTree paramMemberReferenceTree, P paramP) {
/* 217 */     return defaultAction((Tree)paramMemberReferenceTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitIdentifier(IdentifierTree paramIdentifierTree, P paramP) {
/* 221 */     return defaultAction((Tree)paramIdentifierTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitLiteral(LiteralTree paramLiteralTree, P paramP) {
/* 225 */     return defaultAction((Tree)paramLiteralTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitPrimitiveType(PrimitiveTypeTree paramPrimitiveTypeTree, P paramP) {
/* 229 */     return defaultAction((Tree)paramPrimitiveTypeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitArrayType(ArrayTypeTree paramArrayTypeTree, P paramP) {
/* 233 */     return defaultAction((Tree)paramArrayTypeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitParameterizedType(ParameterizedTypeTree paramParameterizedTypeTree, P paramP) {
/* 237 */     return defaultAction((Tree)paramParameterizedTypeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitUnionType(UnionTypeTree paramUnionTypeTree, P paramP) {
/* 241 */     return defaultAction((Tree)paramUnionTypeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitIntersectionType(IntersectionTypeTree paramIntersectionTypeTree, P paramP) {
/* 245 */     return defaultAction((Tree)paramIntersectionTypeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitTypeParameter(TypeParameterTree paramTypeParameterTree, P paramP) {
/* 249 */     return defaultAction((Tree)paramTypeParameterTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitWildcard(WildcardTree paramWildcardTree, P paramP) {
/* 253 */     return defaultAction((Tree)paramWildcardTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitModifiers(ModifiersTree paramModifiersTree, P paramP) {
/* 257 */     return defaultAction((Tree)paramModifiersTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitAnnotation(AnnotationTree paramAnnotationTree, P paramP) {
/* 261 */     return defaultAction((Tree)paramAnnotationTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitAnnotatedType(AnnotatedTypeTree paramAnnotatedTypeTree, P paramP) {
/* 265 */     return defaultAction((Tree)paramAnnotatedTypeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitErroneous(ErroneousTree paramErroneousTree, P paramP) {
/* 269 */     return defaultAction((Tree)paramErroneousTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitOther(Tree paramTree, P paramP) {
/* 273 */     return defaultAction(paramTree, paramP);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\SimpleTreeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */