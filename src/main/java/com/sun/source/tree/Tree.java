/*     */ package com.sun.source.tree;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public interface Tree
/*     */ {
/*     */   Kind getKind();
/*     */   
/*     */   <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD);
/*     */   
/*     */   @Exported
/*     */   public enum Kind
/*     */   {
/*  51 */     ANNOTATED_TYPE((String)AnnotatedTypeTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     ANNOTATION((String)AnnotationTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     TYPE_ANNOTATION((String)AnnotationTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     ARRAY_ACCESS((String)ArrayAccessTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     ARRAY_TYPE((String)ArrayTypeTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     ASSERT((String)AssertTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     ASSIGNMENT((String)AssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     BLOCK((String)BlockTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     BREAK((String)BreakTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     CASE((String)CaseTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     CATCH((String)CatchTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     CLASS((String)ClassTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     COMPILATION_UNIT((String)CompilationUnitTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     CONDITIONAL_EXPRESSION((String)ConditionalExpressionTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     CONTINUE((String)ContinueTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     DO_WHILE_LOOP((String)DoWhileLoopTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     ENHANCED_FOR_LOOP((String)EnhancedForLoopTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     EXPRESSION_STATEMENT((String)ExpressionStatementTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     MEMBER_SELECT((String)MemberSelectTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     MEMBER_REFERENCE((String)MemberReferenceTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     FOR_LOOP((String)ForLoopTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     IDENTIFIER((String)IdentifierTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     IF((String)IfTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     IMPORT((String)ImportTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     INSTANCE_OF((String)InstanceOfTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     LABELED_STATEMENT((String)LabeledStatementTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     METHOD((String)MethodTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     METHOD_INVOCATION((String)MethodInvocationTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     MODIFIERS((String)ModifiersTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     NEW_ARRAY((String)NewArrayTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     NEW_CLASS((String)NewClassTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     LAMBDA_EXPRESSION((String)LambdaExpressionTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     PARENTHESIZED((String)ParenthesizedTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     PRIMITIVE_TYPE((String)PrimitiveTypeTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     RETURN((String)ReturnTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     EMPTY_STATEMENT((String)EmptyStatementTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     SWITCH((String)SwitchTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     SYNCHRONIZED((String)SynchronizedTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     THROW((String)ThrowTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 248 */     TRY((String)TryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     PARAMETERIZED_TYPE((String)ParameterizedTypeTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     UNION_TYPE((String)UnionTypeTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 263 */     INTERSECTION_TYPE((String)IntersectionTypeTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     TYPE_CAST((String)TypeCastTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     TYPE_PARAMETER((String)TypeParameterTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     VARIABLE((String)VariableTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     WHILE_LOOP((String)WhileLoopTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 289 */     POSTFIX_INCREMENT((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     POSTFIX_DECREMENT((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     PREFIX_INCREMENT((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     PREFIX_DECREMENT((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     UNARY_PLUS((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 319 */     UNARY_MINUS((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     BITWISE_COMPLEMENT((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     LOGICAL_COMPLEMENT((String)UnaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     MULTIPLY((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     DIVIDE((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     REMAINDER((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     PLUS((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     MINUS((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 367 */     LEFT_SHIFT((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 373 */     RIGHT_SHIFT((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 379 */     UNSIGNED_RIGHT_SHIFT((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 385 */     LESS_THAN((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 391 */     GREATER_THAN((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     LESS_THAN_EQUAL((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 403 */     GREATER_THAN_EQUAL((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 409 */     EQUAL_TO((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 415 */     NOT_EQUAL_TO((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 421 */     AND((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 427 */     XOR((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 433 */     OR((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 439 */     CONDITIONAL_AND((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 445 */     CONDITIONAL_OR((String)BinaryTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     MULTIPLY_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 457 */     DIVIDE_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 463 */     REMAINDER_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 469 */     PLUS_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 475 */     MINUS_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 481 */     LEFT_SHIFT_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 487 */     RIGHT_SHIFT_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 493 */     UNSIGNED_RIGHT_SHIFT_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 499 */     AND_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 505 */     XOR_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 511 */     OR_ASSIGNMENT((String)CompoundAssignmentTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 517 */     INT_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 523 */     LONG_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 529 */     FLOAT_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 535 */     DOUBLE_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     BOOLEAN_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 547 */     CHAR_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 553 */     STRING_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 559 */     NULL_LITERAL((String)LiteralTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 565 */     UNBOUNDED_WILDCARD((String)WildcardTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 571 */     EXTENDS_WILDCARD((String)WildcardTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 577 */     SUPER_WILDCARD((String)WildcardTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 582 */     ERRONEOUS((String)ErroneousTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 587 */     INTERFACE((String)ClassTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 592 */     ENUM((String)ClassTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 597 */     ANNOTATION_TYPE((String)ClassTree.class),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 603 */     OTHER(null);
/*     */     private final Class<? extends Tree> associatedInterface;
/*     */     
/*     */     Kind(Class<? extends Tree> param1Class) {
/* 607 */       this.associatedInterface = param1Class;
/*     */     }
/*     */     
/*     */     public Class<? extends Tree> asInterface() {
/* 611 */       return this.associatedInterface;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\Tree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */