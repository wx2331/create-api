/*     */ package com.sun.tools.javac.jvm;
/*     */
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.ByteBuffer;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class CRTable
/*     */   implements CRTFlags
/*     */ {
/*     */   private final boolean crtDebug = false;
/*  52 */   private ListBuffer<CRTEntry> entries = new ListBuffer();
/*     */
/*     */
/*     */
/*  56 */   private Map<Object, SourceRange> positions = new HashMap<>();
/*     */
/*     */
/*     */
/*     */   private EndPosTable endPosTable;
/*     */
/*     */
/*     */
/*     */   JCTree.JCMethodDecl methodTree;
/*     */
/*     */
/*     */
/*     */
/*     */   public CRTable(JCTree.JCMethodDecl paramJCMethodDecl, EndPosTable paramEndPosTable) {
/*  70 */     this.methodTree = paramJCMethodDecl;
/*  71 */     this.endPosTable = paramEndPosTable;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void put(Object paramObject, int paramInt1, int paramInt2, int paramInt3) {
/*  82 */     this.entries.append(new CRTEntry(paramObject, paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public int writeCRT(ByteBuffer paramByteBuffer, Position.LineMap paramLineMap, Log paramLog) {
/*  90 */     byte b = 0;
/*     */
/*     */
/*  93 */     (new SourceComputer()).csp((JCTree)this.methodTree);
/*     */
/*  95 */     for (List list = this.entries.toList(); list.nonEmpty(); list = list.tail) {
/*     */
/*  97 */       CRTEntry cRTEntry = (CRTEntry)list.head;
/*     */
/*     */
/*     */
/* 101 */       if (cRTEntry.startPc != cRTEntry.endPc) {
/*     */
/*     */
/* 104 */         SourceRange sourceRange = this.positions.get(cRTEntry.tree);
/* 105 */         Assert.checkNonNull(sourceRange, "CRT: tree source positions are undefined");
/* 106 */         if (sourceRange.startPos != -1 && sourceRange.endPos != -1) {
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 115 */           int i = encodePosition(sourceRange.startPos, paramLineMap, paramLog);
/* 116 */           if (i != -1)
/*     */
/*     */           {
/*     */
/*     */
/*     */
/*     */
/*     */
/* 124 */             int j = encodePosition(sourceRange.endPos, paramLineMap, paramLog);
/* 125 */             if (j != -1)
/*     */
/*     */             {
/*     */
/* 129 */               paramByteBuffer.appendChar(cRTEntry.startPc);
/*     */
/* 131 */               paramByteBuffer.appendChar(cRTEntry.endPc - 1);
/* 132 */               paramByteBuffer.appendInt(i);
/* 133 */               paramByteBuffer.appendInt(j);
/* 134 */               paramByteBuffer.appendChar(cRTEntry.flags);
/*     */
/* 136 */               b++; }  }
/*     */         }
/*     */       }
/* 139 */     }  return b;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public int length() {
/* 145 */     return this.entries.length();
/*     */   }
/*     */
/*     */
/*     */
/*     */   private String getTypes(int paramInt) {
/* 151 */     String str = "";
/* 152 */     if ((paramInt & 0x1) != 0) str = str + " CRT_STATEMENT";
/* 153 */     if ((paramInt & 0x2) != 0) str = str + " CRT_BLOCK";
/* 154 */     if ((paramInt & 0x4) != 0) str = str + " CRT_ASSIGNMENT";
/* 155 */     if ((paramInt & 0x8) != 0) str = str + " CRT_FLOW_CONTROLLER";
/* 156 */     if ((paramInt & 0x10) != 0) str = str + " CRT_FLOW_TARGET";
/* 157 */     if ((paramInt & 0x20) != 0) str = str + " CRT_INVOKE";
/* 158 */     if ((paramInt & 0x40) != 0) str = str + " CRT_CREATE";
/* 159 */     if ((paramInt & 0x80) != 0) str = str + " CRT_BRANCH_TRUE";
/* 160 */     if ((paramInt & 0x100) != 0) str = str + " CRT_BRANCH_FALSE";
/* 161 */     return str;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private int encodePosition(int paramInt, Position.LineMap paramLineMap, Log paramLog) {
/* 168 */     int i = paramLineMap.getLineNumber(paramInt);
/* 169 */     int j = paramLineMap.getColumnNumber(paramInt);
/* 170 */     int k = Position.encodePosition(i, j);
/*     */
/*     */
/*     */
/*     */
/* 175 */     if (k == -1) {
/* 176 */       paramLog.warning(paramInt, "position.overflow", new Object[] { Integer.valueOf(i) });
/*     */     }
/* 178 */     return k;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   class SourceComputer
/*     */     extends JCTree.Visitor
/*     */   {
/*     */     SourceRange result;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public SourceRange csp(JCTree param1JCTree) {
/* 198 */       if (param1JCTree == null) return null;
/* 199 */       param1JCTree.accept(this);
/* 200 */       if (this.result != null) {
/* 201 */         CRTable.this.positions.put(param1JCTree, this.result);
/*     */       }
/* 203 */       return this.result;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public SourceRange csp(List<? extends JCTree> param1List) {
/* 209 */       if (param1List == null || !param1List.nonEmpty()) return null;
/* 210 */       SourceRange sourceRange = new SourceRange();
/* 211 */       for (List<? extends JCTree> list = param1List; list.nonEmpty(); list = list.tail) {
/* 212 */         sourceRange.mergeWith(csp((JCTree)list.head));
/*     */       }
/* 214 */       CRTable.this.positions.put(param1List, sourceRange);
/* 215 */       return sourceRange;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public SourceRange cspCases(List<JCTree.JCCase> param1List) {
/* 222 */       if (param1List == null || !param1List.nonEmpty()) return null;
/* 223 */       SourceRange sourceRange = new SourceRange();
/* 224 */       for (List<JCTree.JCCase> list = param1List; list.nonEmpty(); list = list.tail) {
/* 225 */         sourceRange.mergeWith(csp((JCTree)list.head));
/*     */       }
/* 227 */       CRTable.this.positions.put(param1List, sourceRange);
/* 228 */       return sourceRange;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public SourceRange cspCatchers(List<JCTree.JCCatch> param1List) {
/* 235 */       if (param1List == null || !param1List.nonEmpty()) return null;
/* 236 */       SourceRange sourceRange = new SourceRange();
/* 237 */       for (List<JCTree.JCCatch> list = param1List; list.nonEmpty(); list = list.tail) {
/* 238 */         sourceRange.mergeWith(csp((JCTree)list.head));
/*     */       }
/* 240 */       CRTable.this.positions.put(param1List, sourceRange);
/* 241 */       return sourceRange;
/*     */     }
/*     */
/*     */     public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/* 245 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCMethodDecl), endPos((JCTree)param1JCMethodDecl));
/* 246 */       sourceRange.mergeWith(csp((JCTree)param1JCMethodDecl.body));
/* 247 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 251 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCVariableDecl), endPos((JCTree)param1JCVariableDecl));
/* 252 */       csp((JCTree)param1JCVariableDecl.vartype);
/* 253 */       sourceRange.mergeWith(csp((JCTree)param1JCVariableDecl.init));
/* 254 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */
/*     */     public void visitSkip(JCTree.JCSkip param1JCSkip) {
/* 259 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCSkip), startPos((JCTree)param1JCSkip));
/* 260 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitBlock(JCTree.JCBlock param1JCBlock) {
/* 264 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCBlock), endPos((JCTree)param1JCBlock));
/* 265 */       csp(param1JCBlock.stats);
/* 266 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitDoLoop(JCTree.JCDoWhileLoop param1JCDoWhileLoop) {
/* 270 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCDoWhileLoop), endPos((JCTree)param1JCDoWhileLoop));
/* 271 */       sourceRange.mergeWith(csp((JCTree)param1JCDoWhileLoop.body));
/* 272 */       sourceRange.mergeWith(csp((JCTree)param1JCDoWhileLoop.cond));
/* 273 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitWhileLoop(JCTree.JCWhileLoop param1JCWhileLoop) {
/* 277 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCWhileLoop), endPos((JCTree)param1JCWhileLoop));
/* 278 */       sourceRange.mergeWith(csp((JCTree)param1JCWhileLoop.cond));
/* 279 */       sourceRange.mergeWith(csp((JCTree)param1JCWhileLoop.body));
/* 280 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitForLoop(JCTree.JCForLoop param1JCForLoop) {
/* 284 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCForLoop), endPos((JCTree)param1JCForLoop));
/* 285 */       sourceRange.mergeWith(csp(param1JCForLoop.init));
/* 286 */       sourceRange.mergeWith(csp((JCTree)param1JCForLoop.cond));
/* 287 */       sourceRange.mergeWith(csp(param1JCForLoop.step));
/* 288 */       sourceRange.mergeWith(csp((JCTree)param1JCForLoop.body));
/* 289 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitForeachLoop(JCTree.JCEnhancedForLoop param1JCEnhancedForLoop) {
/* 293 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCEnhancedForLoop), endPos((JCTree)param1JCEnhancedForLoop));
/* 294 */       sourceRange.mergeWith(csp((JCTree)param1JCEnhancedForLoop.var));
/* 295 */       sourceRange.mergeWith(csp((JCTree)param1JCEnhancedForLoop.expr));
/* 296 */       sourceRange.mergeWith(csp((JCTree)param1JCEnhancedForLoop.body));
/* 297 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitLabelled(JCTree.JCLabeledStatement param1JCLabeledStatement) {
/* 301 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCLabeledStatement), endPos((JCTree)param1JCLabeledStatement));
/* 302 */       sourceRange.mergeWith(csp((JCTree)param1JCLabeledStatement.body));
/* 303 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitSwitch(JCTree.JCSwitch param1JCSwitch) {
/* 307 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCSwitch), endPos((JCTree)param1JCSwitch));
/* 308 */       sourceRange.mergeWith(csp((JCTree)param1JCSwitch.selector));
/* 309 */       sourceRange.mergeWith(cspCases(param1JCSwitch.cases));
/* 310 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitCase(JCTree.JCCase param1JCCase) {
/* 314 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCCase), endPos((JCTree)param1JCCase));
/* 315 */       sourceRange.mergeWith(csp((JCTree)param1JCCase.pat));
/* 316 */       sourceRange.mergeWith(csp(param1JCCase.stats));
/* 317 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitSynchronized(JCTree.JCSynchronized param1JCSynchronized) {
/* 321 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCSynchronized), endPos((JCTree)param1JCSynchronized));
/* 322 */       sourceRange.mergeWith(csp((JCTree)param1JCSynchronized.lock));
/* 323 */       sourceRange.mergeWith(csp((JCTree)param1JCSynchronized.body));
/* 324 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitTry(JCTree.JCTry param1JCTry) {
/* 328 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCTry), endPos((JCTree)param1JCTry));
/* 329 */       sourceRange.mergeWith(csp(param1JCTry.resources));
/* 330 */       sourceRange.mergeWith(csp((JCTree)param1JCTry.body));
/* 331 */       sourceRange.mergeWith(cspCatchers(param1JCTry.catchers));
/* 332 */       sourceRange.mergeWith(csp((JCTree)param1JCTry.finalizer));
/* 333 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitCatch(JCTree.JCCatch param1JCCatch) {
/* 337 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCCatch), endPos((JCTree)param1JCCatch));
/* 338 */       sourceRange.mergeWith(csp((JCTree)param1JCCatch.param));
/* 339 */       sourceRange.mergeWith(csp((JCTree)param1JCCatch.body));
/* 340 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitConditional(JCTree.JCConditional param1JCConditional) {
/* 344 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCConditional), endPos((JCTree)param1JCConditional));
/* 345 */       sourceRange.mergeWith(csp((JCTree)param1JCConditional.cond));
/* 346 */       sourceRange.mergeWith(csp((JCTree)param1JCConditional.truepart));
/* 347 */       sourceRange.mergeWith(csp((JCTree)param1JCConditional.falsepart));
/* 348 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitIf(JCTree.JCIf param1JCIf) {
/* 352 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCIf), endPos((JCTree)param1JCIf));
/* 353 */       sourceRange.mergeWith(csp((JCTree)param1JCIf.cond));
/* 354 */       sourceRange.mergeWith(csp((JCTree)param1JCIf.thenpart));
/* 355 */       sourceRange.mergeWith(csp((JCTree)param1JCIf.elsepart));
/* 356 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitExec(JCTree.JCExpressionStatement param1JCExpressionStatement) {
/* 360 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCExpressionStatement), endPos((JCTree)param1JCExpressionStatement));
/* 361 */       sourceRange.mergeWith(csp((JCTree)param1JCExpressionStatement.expr));
/* 362 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitBreak(JCTree.JCBreak param1JCBreak) {
/* 366 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCBreak), endPos((JCTree)param1JCBreak));
/* 367 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitContinue(JCTree.JCContinue param1JCContinue) {
/* 371 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCContinue), endPos((JCTree)param1JCContinue));
/* 372 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitReturn(JCTree.JCReturn param1JCReturn) {
/* 376 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCReturn), endPos((JCTree)param1JCReturn));
/* 377 */       sourceRange.mergeWith(csp((JCTree)param1JCReturn.expr));
/* 378 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitThrow(JCTree.JCThrow param1JCThrow) {
/* 382 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCThrow), endPos((JCTree)param1JCThrow));
/* 383 */       sourceRange.mergeWith(csp((JCTree)param1JCThrow.expr));
/* 384 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitAssert(JCTree.JCAssert param1JCAssert) {
/* 388 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCAssert), endPos((JCTree)param1JCAssert));
/* 389 */       sourceRange.mergeWith(csp((JCTree)param1JCAssert.cond));
/* 390 */       sourceRange.mergeWith(csp((JCTree)param1JCAssert.detail));
/* 391 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/* 395 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCMethodInvocation), endPos((JCTree)param1JCMethodInvocation));
/* 396 */       sourceRange.mergeWith(csp((JCTree)param1JCMethodInvocation.meth));
/* 397 */       sourceRange.mergeWith(csp(param1JCMethodInvocation.args));
/* 398 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 402 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCNewClass), endPos((JCTree)param1JCNewClass));
/* 403 */       sourceRange.mergeWith(csp((JCTree)param1JCNewClass.encl));
/* 404 */       sourceRange.mergeWith(csp((JCTree)param1JCNewClass.clazz));
/* 405 */       sourceRange.mergeWith(csp(param1JCNewClass.args));
/* 406 */       sourceRange.mergeWith(csp((JCTree)param1JCNewClass.def));
/* 407 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitNewArray(JCTree.JCNewArray param1JCNewArray) {
/* 411 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCNewArray), endPos((JCTree)param1JCNewArray));
/* 412 */       sourceRange.mergeWith(csp((JCTree)param1JCNewArray.elemtype));
/* 413 */       sourceRange.mergeWith(csp(param1JCNewArray.dims));
/* 414 */       sourceRange.mergeWith(csp(param1JCNewArray.elems));
/* 415 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitParens(JCTree.JCParens param1JCParens) {
/* 419 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCParens), endPos((JCTree)param1JCParens));
/* 420 */       sourceRange.mergeWith(csp((JCTree)param1JCParens.expr));
/* 421 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitAssign(JCTree.JCAssign param1JCAssign) {
/* 425 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCAssign), endPos((JCTree)param1JCAssign));
/* 426 */       sourceRange.mergeWith(csp((JCTree)param1JCAssign.lhs));
/* 427 */       sourceRange.mergeWith(csp((JCTree)param1JCAssign.rhs));
/* 428 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitAssignop(JCTree.JCAssignOp param1JCAssignOp) {
/* 432 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCAssignOp), endPos((JCTree)param1JCAssignOp));
/* 433 */       sourceRange.mergeWith(csp((JCTree)param1JCAssignOp.lhs));
/* 434 */       sourceRange.mergeWith(csp((JCTree)param1JCAssignOp.rhs));
/* 435 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitUnary(JCTree.JCUnary param1JCUnary) {
/* 439 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCUnary), endPos((JCTree)param1JCUnary));
/* 440 */       sourceRange.mergeWith(csp((JCTree)param1JCUnary.arg));
/* 441 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitBinary(JCTree.JCBinary param1JCBinary) {
/* 445 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCBinary), endPos((JCTree)param1JCBinary));
/* 446 */       sourceRange.mergeWith(csp((JCTree)param1JCBinary.lhs));
/* 447 */       sourceRange.mergeWith(csp((JCTree)param1JCBinary.rhs));
/* 448 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitTypeCast(JCTree.JCTypeCast param1JCTypeCast) {
/* 452 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCTypeCast), endPos((JCTree)param1JCTypeCast));
/* 453 */       sourceRange.mergeWith(csp(param1JCTypeCast.clazz));
/* 454 */       sourceRange.mergeWith(csp((JCTree)param1JCTypeCast.expr));
/* 455 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitTypeTest(JCTree.JCInstanceOf param1JCInstanceOf) {
/* 459 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCInstanceOf), endPos((JCTree)param1JCInstanceOf));
/* 460 */       sourceRange.mergeWith(csp((JCTree)param1JCInstanceOf.expr));
/* 461 */       sourceRange.mergeWith(csp(param1JCInstanceOf.clazz));
/* 462 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitIndexed(JCTree.JCArrayAccess param1JCArrayAccess) {
/* 466 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCArrayAccess), endPos((JCTree)param1JCArrayAccess));
/* 467 */       sourceRange.mergeWith(csp((JCTree)param1JCArrayAccess.indexed));
/* 468 */       sourceRange.mergeWith(csp((JCTree)param1JCArrayAccess.index));
/* 469 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 473 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCFieldAccess), endPos((JCTree)param1JCFieldAccess));
/* 474 */       sourceRange.mergeWith(csp((JCTree)param1JCFieldAccess.selected));
/* 475 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 479 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCIdent), endPos((JCTree)param1JCIdent));
/* 480 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitLiteral(JCTree.JCLiteral param1JCLiteral) {
/* 484 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCLiteral), endPos((JCTree)param1JCLiteral));
/* 485 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitTypeIdent(JCTree.JCPrimitiveTypeTree param1JCPrimitiveTypeTree) {
/* 489 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCPrimitiveTypeTree), endPos((JCTree)param1JCPrimitiveTypeTree));
/* 490 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitTypeArray(JCTree.JCArrayTypeTree param1JCArrayTypeTree) {
/* 494 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCArrayTypeTree), endPos((JCTree)param1JCArrayTypeTree));
/* 495 */       sourceRange.mergeWith(csp((JCTree)param1JCArrayTypeTree.elemtype));
/* 496 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitTypeApply(JCTree.JCTypeApply param1JCTypeApply) {
/* 500 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCTypeApply), endPos((JCTree)param1JCTypeApply));
/* 501 */       sourceRange.mergeWith(csp((JCTree)param1JCTypeApply.clazz));
/* 502 */       sourceRange.mergeWith(csp(param1JCTypeApply.arguments));
/* 503 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */
/*     */     public void visitLetExpr(JCTree.LetExpr param1LetExpr) {
/* 508 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1LetExpr), endPos((JCTree)param1LetExpr));
/* 509 */       sourceRange.mergeWith(csp(param1LetExpr.defs));
/* 510 */       sourceRange.mergeWith(csp(param1LetExpr.expr));
/* 511 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitTypeParameter(JCTree.JCTypeParameter param1JCTypeParameter) {
/* 515 */       SourceRange sourceRange = new SourceRange(startPos((JCTree)param1JCTypeParameter), endPos((JCTree)param1JCTypeParameter));
/* 516 */       sourceRange.mergeWith(csp(param1JCTypeParameter.bounds));
/* 517 */       this.result = sourceRange;
/*     */     }
/*     */
/*     */     public void visitWildcard(JCTree.JCWildcard param1JCWildcard) {
/* 521 */       this.result = null;
/*     */     }
/*     */
/*     */     public void visitErroneous(JCTree.JCErroneous param1JCErroneous) {
/* 525 */       this.result = null;
/*     */     }
/*     */
/*     */     public void visitTree(JCTree param1JCTree) {
/* 529 */       Assert.error();
/*     */     }
/*     */
/*     */
/*     */
/*     */     public int startPos(JCTree param1JCTree) {
/* 535 */       if (param1JCTree == null) return -1;
/* 536 */       return TreeInfo.getStartPos(param1JCTree);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public int endPos(JCTree param1JCTree) {
/* 543 */       if (param1JCTree == null) return -1;
/* 544 */       return TreeInfo.getEndPos(param1JCTree, CRTable.this.endPosTable);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   static class CRTEntry
/*     */   {
/*     */     Object tree;
/*     */
/*     */
/*     */
/*     */     int flags;
/*     */
/*     */
/*     */
/*     */     int startPc;
/*     */
/*     */
/*     */
/*     */     int endPc;
/*     */
/*     */
/*     */
/*     */     CRTEntry(Object param1Object, int param1Int1, int param1Int2, int param1Int3) {
/* 570 */       this.tree = param1Object;
/* 571 */       this.flags = param1Int1;
/* 572 */       this.startPc = param1Int2;
/* 573 */       this.endPc = param1Int3;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static class SourceRange
/*     */   {
/*     */     int startPos;
/*     */
/*     */
/*     */
/*     */     int endPos;
/*     */
/*     */
/*     */
/*     */
/*     */     SourceRange() {
/* 593 */       this.startPos = -1;
/* 594 */       this.endPos = -1;
/*     */     }
/*     */
/*     */
/*     */     SourceRange(int param1Int1, int param1Int2) {
/* 599 */       this.startPos = param1Int1;
/* 600 */       this.endPos = param1Int2;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     SourceRange mergeWith(SourceRange param1SourceRange) {
/* 608 */       if (param1SourceRange == null) return this;
/* 609 */       if (this.startPos == -1) {
/* 610 */         this.startPos = param1SourceRange.startPos;
/* 611 */       } else if (param1SourceRange.startPos != -1) {
/* 612 */         this.startPos = (this.startPos < param1SourceRange.startPos) ? this.startPos : param1SourceRange.startPos;
/* 613 */       }  if (this.endPos == -1) {
/* 614 */         this.endPos = param1SourceRange.endPos;
/* 615 */       } else if (param1SourceRange.endPos != -1) {
/* 616 */         this.endPos = (this.endPos > param1SourceRange.endPos) ? this.endPos : param1SourceRange.endPos;
/* 617 */       }  return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\CRTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
