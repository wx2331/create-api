/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeScanner
/*     */   extends JCTree.Visitor
/*     */ {
/*     */   public void scan(JCTree paramJCTree) {
/*  49 */     if (paramJCTree != null) paramJCTree.accept(this);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void scan(List<? extends JCTree> paramList) {
/*  55 */     if (paramList != null) {
/*  56 */       for (List<? extends JCTree> list = paramList; list.nonEmpty(); list = list.tail) {
/*  57 */         scan((JCTree)list.head);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/*  66 */     scan((List)paramJCCompilationUnit.packageAnnotations);
/*  67 */     scan(paramJCCompilationUnit.pid);
/*  68 */     scan(paramJCCompilationUnit.defs);
/*     */   }
/*     */   
/*     */   public void visitImport(JCTree.JCImport paramJCImport) {
/*  72 */     scan(paramJCImport.qualid);
/*     */   }
/*     */   
/*     */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/*  76 */     scan(paramJCClassDecl.mods);
/*  77 */     scan((List)paramJCClassDecl.typarams);
/*  78 */     scan(paramJCClassDecl.extending);
/*  79 */     scan((List)paramJCClassDecl.implementing);
/*  80 */     scan(paramJCClassDecl.defs);
/*     */   }
/*     */   
/*     */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*  84 */     scan(paramJCMethodDecl.mods);
/*  85 */     scan(paramJCMethodDecl.restype);
/*  86 */     scan((List)paramJCMethodDecl.typarams);
/*  87 */     scan(paramJCMethodDecl.recvparam);
/*  88 */     scan((List)paramJCMethodDecl.params);
/*  89 */     scan((List)paramJCMethodDecl.thrown);
/*  90 */     scan(paramJCMethodDecl.defaultValue);
/*  91 */     scan(paramJCMethodDecl.body);
/*     */   }
/*     */   
/*     */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*  95 */     scan(paramJCVariableDecl.mods);
/*  96 */     scan(paramJCVariableDecl.vartype);
/*  97 */     scan(paramJCVariableDecl.nameexpr);
/*  98 */     scan(paramJCVariableDecl.init);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitSkip(JCTree.JCSkip paramJCSkip) {}
/*     */   
/*     */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 105 */     scan((List)paramJCBlock.stats);
/*     */   }
/*     */   
/*     */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 109 */     scan(paramJCDoWhileLoop.body);
/* 110 */     scan(paramJCDoWhileLoop.cond);
/*     */   }
/*     */   
/*     */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 114 */     scan(paramJCWhileLoop.cond);
/* 115 */     scan(paramJCWhileLoop.body);
/*     */   }
/*     */   
/*     */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 119 */     scan((List)paramJCForLoop.init);
/* 120 */     scan(paramJCForLoop.cond);
/* 121 */     scan((List)paramJCForLoop.step);
/* 122 */     scan(paramJCForLoop.body);
/*     */   }
/*     */   
/*     */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 126 */     scan(paramJCEnhancedForLoop.var);
/* 127 */     scan(paramJCEnhancedForLoop.expr);
/* 128 */     scan(paramJCEnhancedForLoop.body);
/*     */   }
/*     */   
/*     */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 132 */     scan(paramJCLabeledStatement.body);
/*     */   }
/*     */   
/*     */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 136 */     scan(paramJCSwitch.selector);
/* 137 */     scan((List)paramJCSwitch.cases);
/*     */   }
/*     */   
/*     */   public void visitCase(JCTree.JCCase paramJCCase) {
/* 141 */     scan(paramJCCase.pat);
/* 142 */     scan((List)paramJCCase.stats);
/*     */   }
/*     */   
/*     */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/* 146 */     scan(paramJCSynchronized.lock);
/* 147 */     scan(paramJCSynchronized.body);
/*     */   }
/*     */   
/*     */   public void visitTry(JCTree.JCTry paramJCTry) {
/* 151 */     scan(paramJCTry.resources);
/* 152 */     scan(paramJCTry.body);
/* 153 */     scan((List)paramJCTry.catchers);
/* 154 */     scan(paramJCTry.finalizer);
/*     */   }
/*     */   
/*     */   public void visitCatch(JCTree.JCCatch paramJCCatch) {
/* 158 */     scan(paramJCCatch.param);
/* 159 */     scan(paramJCCatch.body);
/*     */   }
/*     */   
/*     */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 163 */     scan(paramJCConditional.cond);
/* 164 */     scan(paramJCConditional.truepart);
/* 165 */     scan(paramJCConditional.falsepart);
/*     */   }
/*     */   
/*     */   public void visitIf(JCTree.JCIf paramJCIf) {
/* 169 */     scan(paramJCIf.cond);
/* 170 */     scan(paramJCIf.thenpart);
/* 171 */     scan(paramJCIf.elsepart);
/*     */   }
/*     */   
/*     */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/* 175 */     scan(paramJCExpressionStatement.expr);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitBreak(JCTree.JCBreak paramJCBreak) {}
/*     */ 
/*     */   
/*     */   public void visitContinue(JCTree.JCContinue paramJCContinue) {}
/*     */   
/*     */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 185 */     scan(paramJCReturn.expr);
/*     */   }
/*     */   
/*     */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 189 */     scan(paramJCThrow.expr);
/*     */   }
/*     */   
/*     */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/* 193 */     scan(paramJCAssert.cond);
/* 194 */     scan(paramJCAssert.detail);
/*     */   }
/*     */   
/*     */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 198 */     scan((List)paramJCMethodInvocation.typeargs);
/* 199 */     scan(paramJCMethodInvocation.meth);
/* 200 */     scan((List)paramJCMethodInvocation.args);
/*     */   }
/*     */   
/*     */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 204 */     scan(paramJCNewClass.encl);
/* 205 */     scan((List)paramJCNewClass.typeargs);
/* 206 */     scan(paramJCNewClass.clazz);
/* 207 */     scan((List)paramJCNewClass.args);
/* 208 */     scan(paramJCNewClass.def);
/*     */   }
/*     */   
/*     */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 212 */     scan((List)paramJCNewArray.annotations);
/* 213 */     scan(paramJCNewArray.elemtype);
/* 214 */     scan((List)paramJCNewArray.dims);
/* 215 */     for (List<? extends JCTree> list : paramJCNewArray.dimAnnotations)
/* 216 */       scan(list); 
/* 217 */     scan((List)paramJCNewArray.elems);
/*     */   }
/*     */   
/*     */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/* 221 */     scan(paramJCLambda.body);
/* 222 */     scan((List)paramJCLambda.params);
/*     */   }
/*     */   
/*     */   public void visitParens(JCTree.JCParens paramJCParens) {
/* 226 */     scan(paramJCParens.expr);
/*     */   }
/*     */   
/*     */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 230 */     scan(paramJCAssign.lhs);
/* 231 */     scan(paramJCAssign.rhs);
/*     */   }
/*     */   
/*     */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/* 235 */     scan(paramJCAssignOp.lhs);
/* 236 */     scan(paramJCAssignOp.rhs);
/*     */   }
/*     */   
/*     */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 240 */     scan(paramJCUnary.arg);
/*     */   }
/*     */   
/*     */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 244 */     scan(paramJCBinary.lhs);
/* 245 */     scan(paramJCBinary.rhs);
/*     */   }
/*     */   
/*     */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 249 */     scan(paramJCTypeCast.clazz);
/* 250 */     scan(paramJCTypeCast.expr);
/*     */   }
/*     */   
/*     */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 254 */     scan(paramJCInstanceOf.expr);
/* 255 */     scan(paramJCInstanceOf.clazz);
/*     */   }
/*     */   
/*     */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 259 */     scan(paramJCArrayAccess.indexed);
/* 260 */     scan(paramJCArrayAccess.index);
/*     */   }
/*     */   
/*     */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 264 */     scan(paramJCFieldAccess.selected);
/*     */   }
/*     */   
/*     */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/* 268 */     scan(paramJCMemberReference.expr);
/* 269 */     scan((List)paramJCMemberReference.typeargs);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIdent(JCTree.JCIdent paramJCIdent) {}
/*     */ 
/*     */   
/*     */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {}
/*     */ 
/*     */   
/*     */   public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) {}
/*     */   
/*     */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/* 282 */     scan(paramJCArrayTypeTree.elemtype);
/*     */   }
/*     */   
/*     */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply) {
/* 286 */     scan(paramJCTypeApply.clazz);
/* 287 */     scan((List)paramJCTypeApply.arguments);
/*     */   }
/*     */   
/*     */   public void visitTypeUnion(JCTree.JCTypeUnion paramJCTypeUnion) {
/* 291 */     scan((List)paramJCTypeUnion.alternatives);
/*     */   }
/*     */   
/*     */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/* 295 */     scan((List)paramJCTypeIntersection.bounds);
/*     */   }
/*     */   
/*     */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/* 299 */     scan((List)paramJCTypeParameter.annotations);
/* 300 */     scan((List)paramJCTypeParameter.bounds);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard) {
/* 305 */     scan(paramJCWildcard.kind);
/* 306 */     if (paramJCWildcard.inner != null) {
/* 307 */       scan(paramJCWildcard.inner);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeBoundKind(JCTree.TypeBoundKind paramTypeBoundKind) {}
/*     */   
/*     */   public void visitModifiers(JCTree.JCModifiers paramJCModifiers) {
/* 315 */     scan((List)paramJCModifiers.annotations);
/*     */   }
/*     */   
/*     */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 319 */     scan(paramJCAnnotation.annotationType);
/* 320 */     scan((List)paramJCAnnotation.args);
/*     */   }
/*     */   
/*     */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/* 324 */     scan((List)paramJCAnnotatedType.annotations);
/* 325 */     scan(paramJCAnnotatedType.underlyingType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {}
/*     */   
/*     */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) {
/* 332 */     scan((List)paramLetExpr.defs);
/* 333 */     scan(paramLetExpr.expr);
/*     */   }
/*     */   
/*     */   public void visitTree(JCTree paramJCTree) {
/* 337 */     Assert.error();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\TreeScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */