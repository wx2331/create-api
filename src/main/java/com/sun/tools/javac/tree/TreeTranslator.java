/*     */ package com.sun.tools.javac.tree;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeTranslator
/*     */   extends JCTree.Visitor
/*     */ {
/*     */   protected JCTree result;
/*     */   
/*     */   public <T extends JCTree> T translate(T paramT) {
/*  55 */     if (paramT == null) {
/*  56 */       return null;
/*     */     }
/*  58 */     paramT.accept(this);
/*  59 */     JCTree jCTree = this.result;
/*  60 */     this.result = null;
/*  61 */     return (T)jCTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends JCTree> List<T> translate(List<T> paramList) {
/*  68 */     if (paramList == null) return null; 
/*  69 */     for (List<T> list = paramList; list.nonEmpty(); list = list.tail)
/*  70 */       list.head = translate((JCTree)list.head); 
/*  71 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCVariableDecl> translateVarDefs(List<JCTree.JCVariableDecl> paramList) {
/*  77 */     for (List<JCTree.JCVariableDecl> list = paramList; list.nonEmpty(); list = list.tail)
/*  78 */       list.head = translate((JCTree)list.head); 
/*  79 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCTypeParameter> translateTypeParams(List<JCTree.JCTypeParameter> paramList) {
/*  85 */     for (List<JCTree.JCTypeParameter> list = paramList; list.nonEmpty(); list = list.tail)
/*  86 */       list.head = translate((JCTree)list.head); 
/*  87 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCCase> translateCases(List<JCTree.JCCase> paramList) {
/*  93 */     for (List<JCTree.JCCase> list = paramList; list.nonEmpty(); list = list.tail)
/*  94 */       list.head = translate((JCTree)list.head); 
/*  95 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCCatch> translateCatchers(List<JCTree.JCCatch> paramList) {
/* 101 */     for (List<JCTree.JCCatch> list = paramList; list.nonEmpty(); list = list.tail)
/* 102 */       list.head = translate((JCTree)list.head); 
/* 103 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JCTree.JCAnnotation> translateAnnotations(List<JCTree.JCAnnotation> paramList) {
/* 109 */     for (List<JCTree.JCAnnotation> list = paramList; list.nonEmpty(); list = list.tail)
/* 110 */       list.head = translate((JCTree)list.head); 
/* 111 */     return paramList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 119 */     paramJCCompilationUnit.pid = translate(paramJCCompilationUnit.pid);
/* 120 */     paramJCCompilationUnit.defs = translate(paramJCCompilationUnit.defs);
/* 121 */     this.result = paramJCCompilationUnit;
/*     */   }
/*     */   
/*     */   public void visitImport(JCTree.JCImport paramJCImport) {
/* 125 */     paramJCImport.qualid = translate(paramJCImport.qualid);
/* 126 */     this.result = paramJCImport;
/*     */   }
/*     */   
/*     */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/* 130 */     paramJCClassDecl.mods = translate(paramJCClassDecl.mods);
/* 131 */     paramJCClassDecl.typarams = translateTypeParams(paramJCClassDecl.typarams);
/* 132 */     paramJCClassDecl.extending = translate(paramJCClassDecl.extending);
/* 133 */     paramJCClassDecl.implementing = translate(paramJCClassDecl.implementing);
/* 134 */     paramJCClassDecl.defs = translate(paramJCClassDecl.defs);
/* 135 */     this.result = paramJCClassDecl;
/*     */   }
/*     */   
/*     */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 139 */     paramJCMethodDecl.mods = translate(paramJCMethodDecl.mods);
/* 140 */     paramJCMethodDecl.restype = translate(paramJCMethodDecl.restype);
/* 141 */     paramJCMethodDecl.typarams = translateTypeParams(paramJCMethodDecl.typarams);
/* 142 */     paramJCMethodDecl.recvparam = translate(paramJCMethodDecl.recvparam);
/* 143 */     paramJCMethodDecl.params = translateVarDefs(paramJCMethodDecl.params);
/* 144 */     paramJCMethodDecl.thrown = translate(paramJCMethodDecl.thrown);
/* 145 */     paramJCMethodDecl.body = translate(paramJCMethodDecl.body);
/* 146 */     this.result = paramJCMethodDecl;
/*     */   }
/*     */   
/*     */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 150 */     paramJCVariableDecl.mods = translate(paramJCVariableDecl.mods);
/* 151 */     paramJCVariableDecl.nameexpr = translate(paramJCVariableDecl.nameexpr);
/* 152 */     paramJCVariableDecl.vartype = translate(paramJCVariableDecl.vartype);
/* 153 */     paramJCVariableDecl.init = translate(paramJCVariableDecl.init);
/* 154 */     this.result = paramJCVariableDecl;
/*     */   }
/*     */   
/*     */   public void visitSkip(JCTree.JCSkip paramJCSkip) {
/* 158 */     this.result = paramJCSkip;
/*     */   }
/*     */   
/*     */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 162 */     paramJCBlock.stats = translate(paramJCBlock.stats);
/* 163 */     this.result = paramJCBlock;
/*     */   }
/*     */   
/*     */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 167 */     paramJCDoWhileLoop.body = translate(paramJCDoWhileLoop.body);
/* 168 */     paramJCDoWhileLoop.cond = translate(paramJCDoWhileLoop.cond);
/* 169 */     this.result = paramJCDoWhileLoop;
/*     */   }
/*     */   
/*     */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 173 */     paramJCWhileLoop.cond = translate(paramJCWhileLoop.cond);
/* 174 */     paramJCWhileLoop.body = translate(paramJCWhileLoop.body);
/* 175 */     this.result = paramJCWhileLoop;
/*     */   }
/*     */   
/*     */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 179 */     paramJCForLoop.init = translate(paramJCForLoop.init);
/* 180 */     paramJCForLoop.cond = translate(paramJCForLoop.cond);
/* 181 */     paramJCForLoop.step = translate(paramJCForLoop.step);
/* 182 */     paramJCForLoop.body = translate(paramJCForLoop.body);
/* 183 */     this.result = paramJCForLoop;
/*     */   }
/*     */   
/*     */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 187 */     paramJCEnhancedForLoop.var = translate(paramJCEnhancedForLoop.var);
/* 188 */     paramJCEnhancedForLoop.expr = translate(paramJCEnhancedForLoop.expr);
/* 189 */     paramJCEnhancedForLoop.body = translate(paramJCEnhancedForLoop.body);
/* 190 */     this.result = paramJCEnhancedForLoop;
/*     */   }
/*     */   
/*     */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 194 */     paramJCLabeledStatement.body = translate(paramJCLabeledStatement.body);
/* 195 */     this.result = paramJCLabeledStatement;
/*     */   }
/*     */   
/*     */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 199 */     paramJCSwitch.selector = translate(paramJCSwitch.selector);
/* 200 */     paramJCSwitch.cases = translateCases(paramJCSwitch.cases);
/* 201 */     this.result = paramJCSwitch;
/*     */   }
/*     */   
/*     */   public void visitCase(JCTree.JCCase paramJCCase) {
/* 205 */     paramJCCase.pat = translate(paramJCCase.pat);
/* 206 */     paramJCCase.stats = translate(paramJCCase.stats);
/* 207 */     this.result = paramJCCase;
/*     */   }
/*     */   
/*     */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/* 211 */     paramJCSynchronized.lock = translate(paramJCSynchronized.lock);
/* 212 */     paramJCSynchronized.body = translate(paramJCSynchronized.body);
/* 213 */     this.result = paramJCSynchronized;
/*     */   }
/*     */   
/*     */   public void visitTry(JCTree.JCTry paramJCTry) {
/* 217 */     paramJCTry.resources = translate(paramJCTry.resources);
/* 218 */     paramJCTry.body = translate(paramJCTry.body);
/* 219 */     paramJCTry.catchers = translateCatchers(paramJCTry.catchers);
/* 220 */     paramJCTry.finalizer = translate(paramJCTry.finalizer);
/* 221 */     this.result = paramJCTry;
/*     */   }
/*     */   
/*     */   public void visitCatch(JCTree.JCCatch paramJCCatch) {
/* 225 */     paramJCCatch.param = translate(paramJCCatch.param);
/* 226 */     paramJCCatch.body = translate(paramJCCatch.body);
/* 227 */     this.result = paramJCCatch;
/*     */   }
/*     */   
/*     */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 231 */     paramJCConditional.cond = translate(paramJCConditional.cond);
/* 232 */     paramJCConditional.truepart = translate(paramJCConditional.truepart);
/* 233 */     paramJCConditional.falsepart = translate(paramJCConditional.falsepart);
/* 234 */     this.result = paramJCConditional;
/*     */   }
/*     */   
/*     */   public void visitIf(JCTree.JCIf paramJCIf) {
/* 238 */     paramJCIf.cond = translate(paramJCIf.cond);
/* 239 */     paramJCIf.thenpart = translate(paramJCIf.thenpart);
/* 240 */     paramJCIf.elsepart = translate(paramJCIf.elsepart);
/* 241 */     this.result = paramJCIf;
/*     */   }
/*     */   
/*     */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/* 245 */     paramJCExpressionStatement.expr = translate(paramJCExpressionStatement.expr);
/* 246 */     this.result = paramJCExpressionStatement;
/*     */   }
/*     */   
/*     */   public void visitBreak(JCTree.JCBreak paramJCBreak) {
/* 250 */     this.result = paramJCBreak;
/*     */   }
/*     */   
/*     */   public void visitContinue(JCTree.JCContinue paramJCContinue) {
/* 254 */     this.result = paramJCContinue;
/*     */   }
/*     */   
/*     */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 258 */     paramJCReturn.expr = translate(paramJCReturn.expr);
/* 259 */     this.result = paramJCReturn;
/*     */   }
/*     */   
/*     */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 263 */     paramJCThrow.expr = translate(paramJCThrow.expr);
/* 264 */     this.result = paramJCThrow;
/*     */   }
/*     */   
/*     */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/* 268 */     paramJCAssert.cond = translate(paramJCAssert.cond);
/* 269 */     paramJCAssert.detail = translate(paramJCAssert.detail);
/* 270 */     this.result = paramJCAssert;
/*     */   }
/*     */   
/*     */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 274 */     paramJCMethodInvocation.meth = translate(paramJCMethodInvocation.meth);
/* 275 */     paramJCMethodInvocation.args = translate(paramJCMethodInvocation.args);
/* 276 */     this.result = paramJCMethodInvocation;
/*     */   }
/*     */   
/*     */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 280 */     paramJCNewClass.encl = translate(paramJCNewClass.encl);
/* 281 */     paramJCNewClass.clazz = translate(paramJCNewClass.clazz);
/* 282 */     paramJCNewClass.args = translate(paramJCNewClass.args);
/* 283 */     paramJCNewClass.def = translate(paramJCNewClass.def);
/* 284 */     this.result = paramJCNewClass;
/*     */   }
/*     */   
/*     */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/* 288 */     paramJCLambda.params = translate(paramJCLambda.params);
/* 289 */     paramJCLambda.body = translate(paramJCLambda.body);
/* 290 */     this.result = paramJCLambda;
/*     */   }
/*     */   
/*     */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 294 */     paramJCNewArray.annotations = translate(paramJCNewArray.annotations);
/* 295 */     List<List<JCTree.JCAnnotation>> list = List.nil();
/* 296 */     for (List<JCTree> list1 : paramJCNewArray.dimAnnotations)
/* 297 */       list = list.append(translate(list1)); 
/* 298 */     paramJCNewArray.dimAnnotations = list;
/* 299 */     paramJCNewArray.elemtype = translate(paramJCNewArray.elemtype);
/* 300 */     paramJCNewArray.dims = translate(paramJCNewArray.dims);
/* 301 */     paramJCNewArray.elems = translate(paramJCNewArray.elems);
/* 302 */     this.result = paramJCNewArray;
/*     */   }
/*     */   
/*     */   public void visitParens(JCTree.JCParens paramJCParens) {
/* 306 */     paramJCParens.expr = translate(paramJCParens.expr);
/* 307 */     this.result = paramJCParens;
/*     */   }
/*     */   
/*     */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 311 */     paramJCAssign.lhs = translate(paramJCAssign.lhs);
/* 312 */     paramJCAssign.rhs = translate(paramJCAssign.rhs);
/* 313 */     this.result = paramJCAssign;
/*     */   }
/*     */   
/*     */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/* 317 */     paramJCAssignOp.lhs = translate(paramJCAssignOp.lhs);
/* 318 */     paramJCAssignOp.rhs = translate(paramJCAssignOp.rhs);
/* 319 */     this.result = paramJCAssignOp;
/*     */   }
/*     */   
/*     */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 323 */     paramJCUnary.arg = translate(paramJCUnary.arg);
/* 324 */     this.result = paramJCUnary;
/*     */   }
/*     */   
/*     */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 328 */     paramJCBinary.lhs = translate(paramJCBinary.lhs);
/* 329 */     paramJCBinary.rhs = translate(paramJCBinary.rhs);
/* 330 */     this.result = paramJCBinary;
/*     */   }
/*     */   
/*     */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 334 */     paramJCTypeCast.clazz = translate(paramJCTypeCast.clazz);
/* 335 */     paramJCTypeCast.expr = translate(paramJCTypeCast.expr);
/* 336 */     this.result = paramJCTypeCast;
/*     */   }
/*     */   
/*     */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 340 */     paramJCInstanceOf.expr = translate(paramJCInstanceOf.expr);
/* 341 */     paramJCInstanceOf.clazz = translate(paramJCInstanceOf.clazz);
/* 342 */     this.result = paramJCInstanceOf;
/*     */   }
/*     */   
/*     */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 346 */     paramJCArrayAccess.indexed = translate(paramJCArrayAccess.indexed);
/* 347 */     paramJCArrayAccess.index = translate(paramJCArrayAccess.index);
/* 348 */     this.result = paramJCArrayAccess;
/*     */   }
/*     */   
/*     */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 352 */     paramJCFieldAccess.selected = translate(paramJCFieldAccess.selected);
/* 353 */     this.result = paramJCFieldAccess;
/*     */   }
/*     */   
/*     */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/* 357 */     paramJCMemberReference.expr = translate(paramJCMemberReference.expr);
/* 358 */     this.result = paramJCMemberReference;
/*     */   }
/*     */   
/*     */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 362 */     this.result = paramJCIdent;
/*     */   }
/*     */   
/*     */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/* 366 */     this.result = paramJCLiteral;
/*     */   }
/*     */   
/*     */   public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) {
/* 370 */     this.result = paramJCPrimitiveTypeTree;
/*     */   }
/*     */   
/*     */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/* 374 */     paramJCArrayTypeTree.elemtype = translate(paramJCArrayTypeTree.elemtype);
/* 375 */     this.result = paramJCArrayTypeTree;
/*     */   }
/*     */   
/*     */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply) {
/* 379 */     paramJCTypeApply.clazz = translate(paramJCTypeApply.clazz);
/* 380 */     paramJCTypeApply.arguments = translate(paramJCTypeApply.arguments);
/* 381 */     this.result = paramJCTypeApply;
/*     */   }
/*     */   
/*     */   public void visitTypeUnion(JCTree.JCTypeUnion paramJCTypeUnion) {
/* 385 */     paramJCTypeUnion.alternatives = translate(paramJCTypeUnion.alternatives);
/* 386 */     this.result = paramJCTypeUnion;
/*     */   }
/*     */   
/*     */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/* 390 */     paramJCTypeIntersection.bounds = translate(paramJCTypeIntersection.bounds);
/* 391 */     this.result = paramJCTypeIntersection;
/*     */   }
/*     */   
/*     */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/* 395 */     paramJCTypeParameter.annotations = translate(paramJCTypeParameter.annotations);
/* 396 */     paramJCTypeParameter.bounds = translate(paramJCTypeParameter.bounds);
/* 397 */     this.result = paramJCTypeParameter;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard) {
/* 402 */     paramJCWildcard.kind = translate(paramJCWildcard.kind);
/* 403 */     paramJCWildcard.inner = translate(paramJCWildcard.inner);
/* 404 */     this.result = paramJCWildcard;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitTypeBoundKind(JCTree.TypeBoundKind paramTypeBoundKind) {
/* 409 */     this.result = paramTypeBoundKind;
/*     */   }
/*     */   
/*     */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {
/* 413 */     this.result = paramJCErroneous;
/*     */   }
/*     */   
/*     */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) {
/* 417 */     paramLetExpr.defs = translateVarDefs(paramLetExpr.defs);
/* 418 */     paramLetExpr.expr = translate(paramLetExpr.expr);
/* 419 */     this.result = paramLetExpr;
/*     */   }
/*     */   
/*     */   public void visitModifiers(JCTree.JCModifiers paramJCModifiers) {
/* 423 */     paramJCModifiers.annotations = translateAnnotations(paramJCModifiers.annotations);
/* 424 */     this.result = paramJCModifiers;
/*     */   }
/*     */   
/*     */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 428 */     paramJCAnnotation.annotationType = translate(paramJCAnnotation.annotationType);
/* 429 */     paramJCAnnotation.args = translate(paramJCAnnotation.args);
/* 430 */     this.result = paramJCAnnotation;
/*     */   }
/*     */   
/*     */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/* 434 */     paramJCAnnotatedType.annotations = translate(paramJCAnnotatedType.annotations);
/* 435 */     paramJCAnnotatedType.underlyingType = translate(paramJCAnnotatedType.underlyingType);
/* 436 */     this.result = paramJCAnnotatedType;
/*     */   }
/*     */   
/*     */   public void visitTree(JCTree paramJCTree) {
/* 440 */     throw new AssertionError(paramJCTree);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\TreeTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */