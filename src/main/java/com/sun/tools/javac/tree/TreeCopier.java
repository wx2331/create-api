/*     */ package com.sun.tools.javac.tree;
/*     */ import com.sun.source.tree.ArrayAccessTree;
/*     */ import com.sun.source.tree.ArrayTypeTree;
/*     */ import com.sun.source.tree.AssertTree;
/*     */ import com.sun.source.tree.AssignmentTree;
/*     */ import com.sun.source.tree.BlockTree;
/*     */ import com.sun.source.tree.BreakTree;
/*     */ import com.sun.source.tree.ClassTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.CompoundAssignmentTree;
/*     */ import com.sun.source.tree.ConditionalExpressionTree;
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
/*     */ import com.sun.source.tree.NewClassTree;
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
/*     */ import com.sun.source.tree.UnaryTree;
/*     */ import com.sun.source.tree.UnionTypeTree;
/*     */ import com.sun.source.tree.VariableTree;
/*     */ import com.sun.source.tree.WildcardTree;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ public class TreeCopier<P> implements TreeVisitor<JCTree, P> {
/*     */   public TreeCopier(TreeMaker paramTreeMaker) {
/*  47 */     this.M = paramTreeMaker;
/*     */   }
/*     */   private TreeMaker M;
/*     */   public <T extends JCTree> T copy(T paramT) {
/*  51 */     return copy(paramT, (Object)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends JCTree> T copy(T paramT, P paramP) {
/*  56 */     if (paramT == null)
/*  57 */       return null; 
/*  58 */     return (T)paramT.<JCTree, P>accept(this, paramP);
/*     */   }
/*     */   
/*     */   public <T extends JCTree> List<T> copy(List<T> paramList) {
/*  62 */     return copy(paramList, (Object)null);
/*     */   }
/*     */   
/*     */   public <T extends JCTree> List<T> copy(List<T> paramList, P paramP) {
/*  66 */     if (paramList == null)
/*  67 */       return null; 
/*  68 */     ListBuffer listBuffer = new ListBuffer();
/*  69 */     for (JCTree jCTree : paramList)
/*  70 */       listBuffer.append(copy(jCTree, paramP)); 
/*  71 */     return listBuffer.toList();
/*     */   }
/*     */   
/*     */   public JCTree visitAnnotatedType(AnnotatedTypeTree paramAnnotatedTypeTree, P paramP) {
/*  75 */     JCTree.JCAnnotatedType jCAnnotatedType = (JCTree.JCAnnotatedType)paramAnnotatedTypeTree;
/*  76 */     List<JCTree.JCAnnotation> list = copy(jCAnnotatedType.annotations, paramP);
/*  77 */     JCTree.JCExpression jCExpression = copy(jCAnnotatedType.underlyingType, paramP);
/*  78 */     return this.M.at(jCAnnotatedType.pos).AnnotatedType(list, jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitAnnotation(AnnotationTree paramAnnotationTree, P paramP) {
/*  82 */     JCTree.JCAnnotation jCAnnotation1 = (JCTree.JCAnnotation)paramAnnotationTree;
/*  83 */     JCTree jCTree = (JCTree)copy(jCAnnotation1.annotationType, paramP);
/*  84 */     List<JCTree.JCExpression> list = copy(jCAnnotation1.args, paramP);
/*  85 */     if (jCAnnotation1.getKind() == Tree.Kind.TYPE_ANNOTATION) {
/*  86 */       JCTree.JCAnnotation jCAnnotation = this.M.at(jCAnnotation1.pos).TypeAnnotation(jCTree, list);
/*  87 */       jCAnnotation.attribute = jCAnnotation1.attribute;
/*  88 */       return jCAnnotation;
/*     */     } 
/*  90 */     JCTree.JCAnnotation jCAnnotation2 = this.M.at(jCAnnotation1.pos).Annotation(jCTree, list);
/*  91 */     jCAnnotation2.attribute = jCAnnotation1.attribute;
/*  92 */     return jCAnnotation2;
/*     */   }
/*     */ 
/*     */   
/*     */   public JCTree visitAssert(AssertTree paramAssertTree, P paramP) {
/*  97 */     JCTree.JCAssert jCAssert = (JCTree.JCAssert)paramAssertTree;
/*  98 */     JCTree.JCExpression jCExpression1 = copy(jCAssert.cond, paramP);
/*  99 */     JCTree.JCExpression jCExpression2 = copy(jCAssert.detail, paramP);
/* 100 */     return this.M.at(jCAssert.pos).Assert(jCExpression1, jCExpression2);
/*     */   }
/*     */   
/*     */   public JCTree visitAssignment(AssignmentTree paramAssignmentTree, P paramP) {
/* 104 */     JCTree.JCAssign jCAssign = (JCTree.JCAssign)paramAssignmentTree;
/* 105 */     JCTree.JCExpression jCExpression1 = copy(jCAssign.lhs, paramP);
/* 106 */     JCTree.JCExpression jCExpression2 = copy(jCAssign.rhs, paramP);
/* 107 */     return this.M.at(jCAssign.pos).Assign(jCExpression1, jCExpression2);
/*     */   }
/*     */   
/*     */   public JCTree visitCompoundAssignment(CompoundAssignmentTree paramCompoundAssignmentTree, P paramP) {
/* 111 */     JCTree.JCAssignOp jCAssignOp = (JCTree.JCAssignOp)paramCompoundAssignmentTree;
/* 112 */     JCTree jCTree1 = (JCTree)copy(jCAssignOp.lhs, paramP);
/* 113 */     JCTree jCTree2 = (JCTree)copy(jCAssignOp.rhs, paramP);
/* 114 */     return this.M.at(jCAssignOp.pos).Assignop(jCAssignOp.getTag(), jCTree1, jCTree2);
/*     */   }
/*     */   
/*     */   public JCTree visitBinary(BinaryTree paramBinaryTree, P paramP) {
/* 118 */     JCTree.JCBinary jCBinary = (JCTree.JCBinary)paramBinaryTree;
/* 119 */     JCTree.JCExpression jCExpression1 = copy(jCBinary.lhs, paramP);
/* 120 */     JCTree.JCExpression jCExpression2 = copy(jCBinary.rhs, paramP);
/* 121 */     return this.M.at(jCBinary.pos).Binary(jCBinary.getTag(), jCExpression1, jCExpression2);
/*     */   }
/*     */   
/*     */   public JCTree visitBlock(BlockTree paramBlockTree, P paramP) {
/* 125 */     JCTree.JCBlock jCBlock = (JCTree.JCBlock)paramBlockTree;
/* 126 */     List<JCTree.JCStatement> list = copy(jCBlock.stats, paramP);
/* 127 */     return this.M.at(jCBlock.pos).Block(jCBlock.flags, list);
/*     */   }
/*     */   
/*     */   public JCTree visitBreak(BreakTree paramBreakTree, P paramP) {
/* 131 */     JCTree.JCBreak jCBreak = (JCTree.JCBreak)paramBreakTree;
/* 132 */     return this.M.at(jCBreak.pos).Break(jCBreak.label);
/*     */   }
/*     */   
/*     */   public JCTree visitCase(CaseTree paramCaseTree, P paramP) {
/* 136 */     JCTree.JCCase jCCase = (JCTree.JCCase)paramCaseTree;
/* 137 */     JCTree.JCExpression jCExpression = copy(jCCase.pat, paramP);
/* 138 */     List<JCTree.JCStatement> list = copy(jCCase.stats, paramP);
/* 139 */     return this.M.at(jCCase.pos).Case(jCExpression, list);
/*     */   }
/*     */   
/*     */   public JCTree visitCatch(CatchTree paramCatchTree, P paramP) {
/* 143 */     JCTree.JCCatch jCCatch = (JCTree.JCCatch)paramCatchTree;
/* 144 */     JCTree.JCVariableDecl jCVariableDecl = copy(jCCatch.param, paramP);
/* 145 */     JCTree.JCBlock jCBlock = copy(jCCatch.body, paramP);
/* 146 */     return this.M.at(jCCatch.pos).Catch(jCVariableDecl, jCBlock);
/*     */   }
/*     */   
/*     */   public JCTree visitClass(ClassTree paramClassTree, P paramP) {
/* 150 */     JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)paramClassTree;
/* 151 */     JCTree.JCModifiers jCModifiers = copy(jCClassDecl.mods, paramP);
/* 152 */     List<JCTree.JCTypeParameter> list = copy(jCClassDecl.typarams, paramP);
/* 153 */     JCTree.JCExpression jCExpression = copy(jCClassDecl.extending, paramP);
/* 154 */     List<JCTree.JCExpression> list1 = copy(jCClassDecl.implementing, paramP);
/* 155 */     List<JCTree> list2 = copy(jCClassDecl.defs, paramP);
/* 156 */     return this.M.at(jCClassDecl.pos).ClassDef(jCModifiers, jCClassDecl.name, list, jCExpression, list1, list2);
/*     */   }
/*     */   
/*     */   public JCTree visitConditionalExpression(ConditionalExpressionTree paramConditionalExpressionTree, P paramP) {
/* 160 */     JCTree.JCConditional jCConditional = (JCTree.JCConditional)paramConditionalExpressionTree;
/* 161 */     JCTree.JCExpression jCExpression1 = copy(jCConditional.cond, paramP);
/* 162 */     JCTree.JCExpression jCExpression2 = copy(jCConditional.truepart, paramP);
/* 163 */     JCTree.JCExpression jCExpression3 = copy(jCConditional.falsepart, paramP);
/* 164 */     return this.M.at(jCConditional.pos).Conditional(jCExpression1, jCExpression2, jCExpression3);
/*     */   }
/*     */   
/*     */   public JCTree visitContinue(ContinueTree paramContinueTree, P paramP) {
/* 168 */     JCTree.JCContinue jCContinue = (JCTree.JCContinue)paramContinueTree;
/* 169 */     return this.M.at(jCContinue.pos).Continue(jCContinue.label);
/*     */   }
/*     */   
/*     */   public JCTree visitDoWhileLoop(DoWhileLoopTree paramDoWhileLoopTree, P paramP) {
/* 173 */     JCTree.JCDoWhileLoop jCDoWhileLoop = (JCTree.JCDoWhileLoop)paramDoWhileLoopTree;
/* 174 */     JCTree.JCStatement jCStatement = copy(jCDoWhileLoop.body, paramP);
/* 175 */     JCTree.JCExpression jCExpression = copy(jCDoWhileLoop.cond, paramP);
/* 176 */     return this.M.at(jCDoWhileLoop.pos).DoLoop(jCStatement, jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitErroneous(ErroneousTree paramErroneousTree, P paramP) {
/* 180 */     JCTree.JCErroneous jCErroneous = (JCTree.JCErroneous)paramErroneousTree;
/* 181 */     List<? extends JCTree> list = copy(jCErroneous.errs, paramP);
/* 182 */     return this.M.at(jCErroneous.pos).Erroneous(list);
/*     */   }
/*     */   
/*     */   public JCTree visitExpressionStatement(ExpressionStatementTree paramExpressionStatementTree, P paramP) {
/* 186 */     JCTree.JCExpressionStatement jCExpressionStatement = (JCTree.JCExpressionStatement)paramExpressionStatementTree;
/* 187 */     JCTree.JCExpression jCExpression = copy(jCExpressionStatement.expr, paramP);
/* 188 */     return this.M.at(jCExpressionStatement.pos).Exec(jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitEnhancedForLoop(EnhancedForLoopTree paramEnhancedForLoopTree, P paramP) {
/* 192 */     JCTree.JCEnhancedForLoop jCEnhancedForLoop = (JCTree.JCEnhancedForLoop)paramEnhancedForLoopTree;
/* 193 */     JCTree.JCVariableDecl jCVariableDecl = copy(jCEnhancedForLoop.var, paramP);
/* 194 */     JCTree.JCExpression jCExpression = copy(jCEnhancedForLoop.expr, paramP);
/* 195 */     JCTree.JCStatement jCStatement = copy(jCEnhancedForLoop.body, paramP);
/* 196 */     return this.M.at(jCEnhancedForLoop.pos).ForeachLoop(jCVariableDecl, jCExpression, jCStatement);
/*     */   }
/*     */   
/*     */   public JCTree visitForLoop(ForLoopTree paramForLoopTree, P paramP) {
/* 200 */     JCTree.JCForLoop jCForLoop = (JCTree.JCForLoop)paramForLoopTree;
/* 201 */     List<JCTree.JCStatement> list = copy(jCForLoop.init, paramP);
/* 202 */     JCTree.JCExpression jCExpression = copy(jCForLoop.cond, paramP);
/* 203 */     List<JCTree.JCExpressionStatement> list1 = copy(jCForLoop.step, paramP);
/* 204 */     JCTree.JCStatement jCStatement = copy(jCForLoop.body, paramP);
/* 205 */     return this.M.at(jCForLoop.pos).ForLoop(list, jCExpression, list1, jCStatement);
/*     */   }
/*     */   
/*     */   public JCTree visitIdentifier(IdentifierTree paramIdentifierTree, P paramP) {
/* 209 */     JCTree.JCIdent jCIdent = (JCTree.JCIdent)paramIdentifierTree;
/* 210 */     return this.M.at(jCIdent.pos).Ident(jCIdent.name);
/*     */   }
/*     */   
/*     */   public JCTree visitIf(IfTree paramIfTree, P paramP) {
/* 214 */     JCTree.JCIf jCIf = (JCTree.JCIf)paramIfTree;
/* 215 */     JCTree.JCExpression jCExpression = copy(jCIf.cond, paramP);
/* 216 */     JCTree.JCStatement jCStatement1 = copy(jCIf.thenpart, paramP);
/* 217 */     JCTree.JCStatement jCStatement2 = copy(jCIf.elsepart, paramP);
/* 218 */     return this.M.at(jCIf.pos).If(jCExpression, jCStatement1, jCStatement2);
/*     */   }
/*     */   
/*     */   public JCTree visitImport(ImportTree paramImportTree, P paramP) {
/* 222 */     JCTree.JCImport jCImport = (JCTree.JCImport)paramImportTree;
/* 223 */     JCTree jCTree = (JCTree)copy(jCImport.qualid, paramP);
/* 224 */     return this.M.at(jCImport.pos).Import(jCTree, jCImport.staticImport);
/*     */   }
/*     */   
/*     */   public JCTree visitArrayAccess(ArrayAccessTree paramArrayAccessTree, P paramP) {
/* 228 */     JCTree.JCArrayAccess jCArrayAccess = (JCTree.JCArrayAccess)paramArrayAccessTree;
/* 229 */     JCTree.JCExpression jCExpression1 = copy(jCArrayAccess.indexed, paramP);
/* 230 */     JCTree.JCExpression jCExpression2 = copy(jCArrayAccess.index, paramP);
/* 231 */     return this.M.at(jCArrayAccess.pos).Indexed(jCExpression1, jCExpression2);
/*     */   }
/*     */   
/*     */   public JCTree visitLabeledStatement(LabeledStatementTree paramLabeledStatementTree, P paramP) {
/* 235 */     JCTree.JCLabeledStatement jCLabeledStatement = (JCTree.JCLabeledStatement)paramLabeledStatementTree;
/* 236 */     JCTree.JCStatement jCStatement = copy(jCLabeledStatement.body, paramP);
/* 237 */     return this.M.at(jCLabeledStatement.pos).Labelled(jCLabeledStatement.label, jCStatement);
/*     */   }
/*     */   
/*     */   public JCTree visitLiteral(LiteralTree paramLiteralTree, P paramP) {
/* 241 */     JCTree.JCLiteral jCLiteral = (JCTree.JCLiteral)paramLiteralTree;
/* 242 */     return this.M.at(jCLiteral.pos).Literal(jCLiteral.typetag, jCLiteral.value);
/*     */   }
/*     */   
/*     */   public JCTree visitMethod(MethodTree paramMethodTree, P paramP) {
/* 246 */     JCTree.JCMethodDecl jCMethodDecl = (JCTree.JCMethodDecl)paramMethodTree;
/* 247 */     JCTree.JCModifiers jCModifiers = copy(jCMethodDecl.mods, paramP);
/* 248 */     JCTree.JCExpression jCExpression1 = copy(jCMethodDecl.restype, paramP);
/* 249 */     List<JCTree.JCTypeParameter> list = copy(jCMethodDecl.typarams, paramP);
/* 250 */     List<JCTree.JCVariableDecl> list1 = copy(jCMethodDecl.params, paramP);
/* 251 */     JCTree.JCVariableDecl jCVariableDecl = copy(jCMethodDecl.recvparam, paramP);
/* 252 */     List<JCTree.JCExpression> list2 = copy(jCMethodDecl.thrown, paramP);
/* 253 */     JCTree.JCBlock jCBlock = copy(jCMethodDecl.body, paramP);
/* 254 */     JCTree.JCExpression jCExpression2 = copy(jCMethodDecl.defaultValue, paramP);
/* 255 */     return this.M.at(jCMethodDecl.pos).MethodDef(jCModifiers, jCMethodDecl.name, jCExpression1, list, jCVariableDecl, list1, list2, jCBlock, jCExpression2);
/*     */   }
/*     */   
/*     */   public JCTree visitMethodInvocation(MethodInvocationTree paramMethodInvocationTree, P paramP) {
/* 259 */     JCTree.JCMethodInvocation jCMethodInvocation = (JCTree.JCMethodInvocation)paramMethodInvocationTree;
/* 260 */     List<JCTree.JCExpression> list1 = copy(jCMethodInvocation.typeargs, paramP);
/* 261 */     JCTree.JCExpression jCExpression = copy(jCMethodInvocation.meth, paramP);
/* 262 */     List<JCTree.JCExpression> list2 = copy(jCMethodInvocation.args, paramP);
/* 263 */     return this.M.at(jCMethodInvocation.pos).Apply(list1, jCExpression, list2);
/*     */   }
/*     */   
/*     */   public JCTree visitModifiers(ModifiersTree paramModifiersTree, P paramP) {
/* 267 */     JCTree.JCModifiers jCModifiers = (JCTree.JCModifiers)paramModifiersTree;
/* 268 */     List<JCTree.JCAnnotation> list = copy(jCModifiers.annotations, paramP);
/* 269 */     return this.M.at(jCModifiers.pos).Modifiers(jCModifiers.flags, list);
/*     */   }
/*     */   
/*     */   public JCTree visitNewArray(NewArrayTree paramNewArrayTree, P paramP) {
/* 273 */     JCTree.JCNewArray jCNewArray = (JCTree.JCNewArray)paramNewArrayTree;
/* 274 */     JCTree.JCExpression jCExpression = copy(jCNewArray.elemtype, paramP);
/* 275 */     List<JCTree.JCExpression> list1 = copy(jCNewArray.dims, paramP);
/* 276 */     List<JCTree.JCExpression> list2 = copy(jCNewArray.elems, paramP);
/* 277 */     return this.M.at(jCNewArray.pos).NewArray(jCExpression, list1, list2);
/*     */   }
/*     */   
/*     */   public JCTree visitNewClass(NewClassTree paramNewClassTree, P paramP) {
/* 281 */     JCTree.JCNewClass jCNewClass = (JCTree.JCNewClass)paramNewClassTree;
/* 282 */     JCTree.JCExpression jCExpression1 = copy(jCNewClass.encl, paramP);
/* 283 */     List<JCTree.JCExpression> list1 = copy(jCNewClass.typeargs, paramP);
/* 284 */     JCTree.JCExpression jCExpression2 = copy(jCNewClass.clazz, paramP);
/* 285 */     List<JCTree.JCExpression> list2 = copy(jCNewClass.args, paramP);
/* 286 */     JCTree.JCClassDecl jCClassDecl = copy(jCNewClass.def, paramP);
/* 287 */     return this.M.at(jCNewClass.pos).NewClass(jCExpression1, list1, jCExpression2, list2, jCClassDecl);
/*     */   }
/*     */   
/*     */   public JCTree visitLambdaExpression(LambdaExpressionTree paramLambdaExpressionTree, P paramP) {
/* 291 */     JCTree.JCLambda jCLambda = (JCTree.JCLambda)paramLambdaExpressionTree;
/* 292 */     List<JCTree.JCVariableDecl> list = copy(jCLambda.params, paramP);
/* 293 */     JCTree jCTree = (JCTree)copy(jCLambda.body, paramP);
/* 294 */     return this.M.at(jCLambda.pos).Lambda(list, jCTree);
/*     */   }
/*     */   
/*     */   public JCTree visitParenthesized(ParenthesizedTree paramParenthesizedTree, P paramP) {
/* 298 */     JCTree.JCParens jCParens = (JCTree.JCParens)paramParenthesizedTree;
/* 299 */     JCTree.JCExpression jCExpression = copy(jCParens.expr, paramP);
/* 300 */     return this.M.at(jCParens.pos).Parens(jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitReturn(ReturnTree paramReturnTree, P paramP) {
/* 304 */     JCTree.JCReturn jCReturn = (JCTree.JCReturn)paramReturnTree;
/* 305 */     JCTree.JCExpression jCExpression = copy(jCReturn.expr, paramP);
/* 306 */     return this.M.at(jCReturn.pos).Return(jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitMemberSelect(MemberSelectTree paramMemberSelectTree, P paramP) {
/* 310 */     JCTree.JCFieldAccess jCFieldAccess = (JCTree.JCFieldAccess)paramMemberSelectTree;
/* 311 */     JCTree.JCExpression jCExpression = copy(jCFieldAccess.selected, paramP);
/* 312 */     return this.M.at(jCFieldAccess.pos).Select(jCExpression, jCFieldAccess.name);
/*     */   }
/*     */   
/*     */   public JCTree visitMemberReference(MemberReferenceTree paramMemberReferenceTree, P paramP) {
/* 316 */     JCTree.JCMemberReference jCMemberReference = (JCTree.JCMemberReference)paramMemberReferenceTree;
/* 317 */     JCTree.JCExpression jCExpression = copy(jCMemberReference.expr, paramP);
/* 318 */     List<JCTree.JCExpression> list = copy(jCMemberReference.typeargs, paramP);
/* 319 */     return this.M.at(jCMemberReference.pos).Reference(jCMemberReference.mode, jCMemberReference.name, jCExpression, list);
/*     */   }
/*     */   
/*     */   public JCTree visitEmptyStatement(EmptyStatementTree paramEmptyStatementTree, P paramP) {
/* 323 */     JCTree.JCSkip jCSkip = (JCTree.JCSkip)paramEmptyStatementTree;
/* 324 */     return this.M.at(jCSkip.pos).Skip();
/*     */   }
/*     */   
/*     */   public JCTree visitSwitch(SwitchTree paramSwitchTree, P paramP) {
/* 328 */     JCTree.JCSwitch jCSwitch = (JCTree.JCSwitch)paramSwitchTree;
/* 329 */     JCTree.JCExpression jCExpression = copy(jCSwitch.selector, paramP);
/* 330 */     List<JCTree.JCCase> list = copy(jCSwitch.cases, paramP);
/* 331 */     return this.M.at(jCSwitch.pos).Switch(jCExpression, list);
/*     */   }
/*     */   
/*     */   public JCTree visitSynchronized(SynchronizedTree paramSynchronizedTree, P paramP) {
/* 335 */     JCTree.JCSynchronized jCSynchronized = (JCTree.JCSynchronized)paramSynchronizedTree;
/* 336 */     JCTree.JCExpression jCExpression = copy(jCSynchronized.lock, paramP);
/* 337 */     JCTree.JCBlock jCBlock = copy(jCSynchronized.body, paramP);
/* 338 */     return this.M.at(jCSynchronized.pos).Synchronized(jCExpression, jCBlock);
/*     */   }
/*     */   
/*     */   public JCTree visitThrow(ThrowTree paramThrowTree, P paramP) {
/* 342 */     JCTree.JCThrow jCThrow = (JCTree.JCThrow)paramThrowTree;
/* 343 */     JCTree.JCExpression jCExpression = copy(jCThrow.expr, paramP);
/* 344 */     return this.M.at(jCThrow.pos).Throw(jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitCompilationUnit(CompilationUnitTree paramCompilationUnitTree, P paramP) {
/* 348 */     JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)paramCompilationUnitTree;
/* 349 */     List<JCTree.JCAnnotation> list = copy(jCCompilationUnit.packageAnnotations, paramP);
/* 350 */     JCTree.JCExpression jCExpression = copy(jCCompilationUnit.pid, paramP);
/* 351 */     List<JCTree> list1 = copy(jCCompilationUnit.defs, paramP);
/* 352 */     return this.M.at(jCCompilationUnit.pos).TopLevel(list, jCExpression, list1);
/*     */   }
/*     */   
/*     */   public JCTree visitTry(TryTree paramTryTree, P paramP) {
/* 356 */     JCTree.JCTry jCTry = (JCTree.JCTry)paramTryTree;
/* 357 */     List<JCTree> list = copy(jCTry.resources, paramP);
/* 358 */     JCTree.JCBlock jCBlock1 = copy(jCTry.body, paramP);
/* 359 */     List<JCTree.JCCatch> list1 = copy(jCTry.catchers, paramP);
/* 360 */     JCTree.JCBlock jCBlock2 = copy(jCTry.finalizer, paramP);
/* 361 */     return this.M.at(jCTry.pos).Try(list, jCBlock1, list1, jCBlock2);
/*     */   }
/*     */   
/*     */   public JCTree visitParameterizedType(ParameterizedTypeTree paramParameterizedTypeTree, P paramP) {
/* 365 */     JCTree.JCTypeApply jCTypeApply = (JCTree.JCTypeApply)paramParameterizedTypeTree;
/* 366 */     JCTree.JCExpression jCExpression = copy(jCTypeApply.clazz, paramP);
/* 367 */     List<JCTree.JCExpression> list = copy(jCTypeApply.arguments, paramP);
/* 368 */     return this.M.at(jCTypeApply.pos).TypeApply(jCExpression, list);
/*     */   }
/*     */   
/*     */   public JCTree visitUnionType(UnionTypeTree paramUnionTypeTree, P paramP) {
/* 372 */     JCTree.JCTypeUnion jCTypeUnion = (JCTree.JCTypeUnion)paramUnionTypeTree;
/* 373 */     List<JCTree.JCExpression> list = copy(jCTypeUnion.alternatives, paramP);
/* 374 */     return this.M.at(jCTypeUnion.pos).TypeUnion(list);
/*     */   }
/*     */   
/*     */   public JCTree visitIntersectionType(IntersectionTypeTree paramIntersectionTypeTree, P paramP) {
/* 378 */     JCTree.JCTypeIntersection jCTypeIntersection = (JCTree.JCTypeIntersection)paramIntersectionTypeTree;
/* 379 */     List<JCTree.JCExpression> list = copy(jCTypeIntersection.bounds, paramP);
/* 380 */     return this.M.at(jCTypeIntersection.pos).TypeIntersection(list);
/*     */   }
/*     */   
/*     */   public JCTree visitArrayType(ArrayTypeTree paramArrayTypeTree, P paramP) {
/* 384 */     JCTree.JCArrayTypeTree jCArrayTypeTree = (JCTree.JCArrayTypeTree)paramArrayTypeTree;
/* 385 */     JCTree.JCExpression jCExpression = copy(jCArrayTypeTree.elemtype, paramP);
/* 386 */     return this.M.at(jCArrayTypeTree.pos).TypeArray(jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitTypeCast(TypeCastTree paramTypeCastTree, P paramP) {
/* 390 */     JCTree.JCTypeCast jCTypeCast = (JCTree.JCTypeCast)paramTypeCastTree;
/* 391 */     JCTree jCTree = (JCTree)copy(jCTypeCast.clazz, paramP);
/* 392 */     JCTree.JCExpression jCExpression = copy(jCTypeCast.expr, paramP);
/* 393 */     return this.M.at(jCTypeCast.pos).TypeCast(jCTree, jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitPrimitiveType(PrimitiveTypeTree paramPrimitiveTypeTree, P paramP) {
/* 397 */     JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTree = (JCTree.JCPrimitiveTypeTree)paramPrimitiveTypeTree;
/* 398 */     return this.M.at(jCPrimitiveTypeTree.pos).TypeIdent(jCPrimitiveTypeTree.typetag);
/*     */   }
/*     */   
/*     */   public JCTree visitTypeParameter(TypeParameterTree paramTypeParameterTree, P paramP) {
/* 402 */     JCTree.JCTypeParameter jCTypeParameter = (JCTree.JCTypeParameter)paramTypeParameterTree;
/* 403 */     List<JCTree.JCAnnotation> list = copy(jCTypeParameter.annotations, paramP);
/* 404 */     List<JCTree.JCExpression> list1 = copy(jCTypeParameter.bounds, paramP);
/* 405 */     return this.M.at(jCTypeParameter.pos).TypeParameter(jCTypeParameter.name, list1, list);
/*     */   }
/*     */   
/*     */   public JCTree visitInstanceOf(InstanceOfTree paramInstanceOfTree, P paramP) {
/* 409 */     JCTree.JCInstanceOf jCInstanceOf = (JCTree.JCInstanceOf)paramInstanceOfTree;
/* 410 */     JCTree.JCExpression jCExpression = copy(jCInstanceOf.expr, paramP);
/* 411 */     JCTree jCTree = (JCTree)copy(jCInstanceOf.clazz, paramP);
/* 412 */     return this.M.at(jCInstanceOf.pos).TypeTest(jCExpression, jCTree);
/*     */   }
/*     */   
/*     */   public JCTree visitUnary(UnaryTree paramUnaryTree, P paramP) {
/* 416 */     JCTree.JCUnary jCUnary = (JCTree.JCUnary)paramUnaryTree;
/* 417 */     JCTree.JCExpression jCExpression = copy(jCUnary.arg, paramP);
/* 418 */     return this.M.at(jCUnary.pos).Unary(jCUnary.getTag(), jCExpression);
/*     */   }
/*     */   
/*     */   public JCTree visitVariable(VariableTree paramVariableTree, P paramP) {
/* 422 */     JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)paramVariableTree;
/* 423 */     JCTree.JCModifiers jCModifiers = copy(jCVariableDecl.mods, paramP);
/* 424 */     JCTree.JCExpression jCExpression1 = copy(jCVariableDecl.vartype, paramP);
/* 425 */     if (jCVariableDecl.nameexpr == null) {
/* 426 */       JCTree.JCExpression jCExpression = copy(jCVariableDecl.init, paramP);
/* 427 */       return this.M.at(jCVariableDecl.pos).VarDef(jCModifiers, jCVariableDecl.name, jCExpression1, jCExpression);
/*     */     } 
/* 429 */     JCTree.JCExpression jCExpression2 = copy(jCVariableDecl.nameexpr, paramP);
/* 430 */     return this.M.at(jCVariableDecl.pos).ReceiverVarDef(jCModifiers, jCExpression2, jCExpression1);
/*     */   }
/*     */ 
/*     */   
/*     */   public JCTree visitWhileLoop(WhileLoopTree paramWhileLoopTree, P paramP) {
/* 435 */     JCTree.JCWhileLoop jCWhileLoop = (JCTree.JCWhileLoop)paramWhileLoopTree;
/* 436 */     JCTree.JCStatement jCStatement = copy(jCWhileLoop.body, paramP);
/* 437 */     JCTree.JCExpression jCExpression = copy(jCWhileLoop.cond, paramP);
/* 438 */     return this.M.at(jCWhileLoop.pos).WhileLoop(jCExpression, jCStatement);
/*     */   }
/*     */   
/*     */   public JCTree visitWildcard(WildcardTree paramWildcardTree, P paramP) {
/* 442 */     JCTree.JCWildcard jCWildcard = (JCTree.JCWildcard)paramWildcardTree;
/* 443 */     JCTree.TypeBoundKind typeBoundKind = this.M.at(jCWildcard.kind.pos).TypeBoundKind(jCWildcard.kind.kind);
/* 444 */     JCTree jCTree = (JCTree)copy(jCWildcard.inner, paramP);
/* 445 */     return this.M.at(jCWildcard.pos).Wildcard(typeBoundKind, jCTree);
/*     */   } public JCTree visitOther(Tree paramTree, P paramP) {
/*     */     JCTree.LetExpr letExpr;
/*     */     List<JCTree.JCVariableDecl> list;
/* 449 */     JCTree jCTree2, jCTree1 = (JCTree)paramTree;
/* 450 */     switch (jCTree1.getTag()) {
/*     */       case LETEXPR:
/* 452 */         letExpr = (JCTree.LetExpr)paramTree;
/* 453 */         list = copy(letExpr.defs, paramP);
/* 454 */         jCTree2 = copy(letExpr.expr, paramP);
/* 455 */         return this.M.at(letExpr.pos).LetExpr(list, jCTree2);
/*     */     } 
/*     */     
/* 458 */     throw new AssertionError("unknown tree tag: " + jCTree1.getTag());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\TreeCopier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */