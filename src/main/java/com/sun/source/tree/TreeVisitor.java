package com.sun.source.tree;

import jdk.Exported;

@Exported
public interface TreeVisitor<R, P> {
  R visitAnnotatedType(AnnotatedTypeTree paramAnnotatedTypeTree, P paramP);
  
  R visitAnnotation(AnnotationTree paramAnnotationTree, P paramP);
  
  R visitMethodInvocation(MethodInvocationTree paramMethodInvocationTree, P paramP);
  
  R visitAssert(AssertTree paramAssertTree, P paramP);
  
  R visitAssignment(AssignmentTree paramAssignmentTree, P paramP);
  
  R visitCompoundAssignment(CompoundAssignmentTree paramCompoundAssignmentTree, P paramP);
  
  R visitBinary(BinaryTree paramBinaryTree, P paramP);
  
  R visitBlock(BlockTree paramBlockTree, P paramP);
  
  R visitBreak(BreakTree paramBreakTree, P paramP);
  
  R visitCase(CaseTree paramCaseTree, P paramP);
  
  R visitCatch(CatchTree paramCatchTree, P paramP);
  
  R visitClass(ClassTree paramClassTree, P paramP);
  
  R visitConditionalExpression(ConditionalExpressionTree paramConditionalExpressionTree, P paramP);
  
  R visitContinue(ContinueTree paramContinueTree, P paramP);
  
  R visitDoWhileLoop(DoWhileLoopTree paramDoWhileLoopTree, P paramP);
  
  R visitErroneous(ErroneousTree paramErroneousTree, P paramP);
  
  R visitExpressionStatement(ExpressionStatementTree paramExpressionStatementTree, P paramP);
  
  R visitEnhancedForLoop(EnhancedForLoopTree paramEnhancedForLoopTree, P paramP);
  
  R visitForLoop(ForLoopTree paramForLoopTree, P paramP);
  
  R visitIdentifier(IdentifierTree paramIdentifierTree, P paramP);
  
  R visitIf(IfTree paramIfTree, P paramP);
  
  R visitImport(ImportTree paramImportTree, P paramP);
  
  R visitArrayAccess(ArrayAccessTree paramArrayAccessTree, P paramP);
  
  R visitLabeledStatement(LabeledStatementTree paramLabeledStatementTree, P paramP);
  
  R visitLiteral(LiteralTree paramLiteralTree, P paramP);
  
  R visitMethod(MethodTree paramMethodTree, P paramP);
  
  R visitModifiers(ModifiersTree paramModifiersTree, P paramP);
  
  R visitNewArray(NewArrayTree paramNewArrayTree, P paramP);
  
  R visitNewClass(NewClassTree paramNewClassTree, P paramP);
  
  R visitLambdaExpression(LambdaExpressionTree paramLambdaExpressionTree, P paramP);
  
  R visitParenthesized(ParenthesizedTree paramParenthesizedTree, P paramP);
  
  R visitReturn(ReturnTree paramReturnTree, P paramP);
  
  R visitMemberSelect(MemberSelectTree paramMemberSelectTree, P paramP);
  
  R visitMemberReference(MemberReferenceTree paramMemberReferenceTree, P paramP);
  
  R visitEmptyStatement(EmptyStatementTree paramEmptyStatementTree, P paramP);
  
  R visitSwitch(SwitchTree paramSwitchTree, P paramP);
  
  R visitSynchronized(SynchronizedTree paramSynchronizedTree, P paramP);
  
  R visitThrow(ThrowTree paramThrowTree, P paramP);
  
  R visitCompilationUnit(CompilationUnitTree paramCompilationUnitTree, P paramP);
  
  R visitTry(TryTree paramTryTree, P paramP);
  
  R visitParameterizedType(ParameterizedTypeTree paramParameterizedTypeTree, P paramP);
  
  R visitUnionType(UnionTypeTree paramUnionTypeTree, P paramP);
  
  R visitIntersectionType(IntersectionTypeTree paramIntersectionTypeTree, P paramP);
  
  R visitArrayType(ArrayTypeTree paramArrayTypeTree, P paramP);
  
  R visitTypeCast(TypeCastTree paramTypeCastTree, P paramP);
  
  R visitPrimitiveType(PrimitiveTypeTree paramPrimitiveTypeTree, P paramP);
  
  R visitTypeParameter(TypeParameterTree paramTypeParameterTree, P paramP);
  
  R visitInstanceOf(InstanceOfTree paramInstanceOfTree, P paramP);
  
  R visitUnary(UnaryTree paramUnaryTree, P paramP);
  
  R visitVariable(VariableTree paramVariableTree, P paramP);
  
  R visitWhileLoop(WhileLoopTree paramWhileLoopTree, P paramP);
  
  R visitWildcard(WildcardTree paramWildcardTree, P paramP);
  
  R visitOther(Tree paramTree, P paramP);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\TreeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */