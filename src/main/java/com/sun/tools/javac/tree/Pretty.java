/*      */ package com.sun.tools.javac.tree;public class Pretty extends JCTree.Visitor {
/*      */   private final boolean sourceOutput;
/*      */   Writer out;
/*      */   public int width;
/*      */   int lmargin;
/*      */   Name enclClassName;
/*      */   DocCommentTable docComments;
/*      */   private static final String trimSequence = "[...]";
/*      */   private static final int PREFERRED_LENGTH = 20;
/*      */   String lineSep;
/*      */   int prec;
/*      */   
/*      */   void align() throws IOException {
/*      */     for (byte b = 0; b < this.lmargin; ) {
/*      */       this.out.write(" ");
/*      */       b++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void indent() {
/*      */     this.lmargin += this.width;
/*      */   }
/*      */   
/*      */   void undent() {
/*      */     this.lmargin -= this.width;
/*      */   }
/*      */   
/*      */   void open(int paramInt1, int paramInt2) throws IOException {
/*      */     if (paramInt2 < paramInt1)
/*      */       this.out.write("("); 
/*      */   }
/*      */   
/*      */   void close(int paramInt1, int paramInt2) throws IOException {
/*      */     if (paramInt2 < paramInt1)
/*      */       this.out.write(")"); 
/*      */   }
/*      */   
/*      */   public void print(Object paramObject) throws IOException {
/*      */     this.out.write(Convert.escapeUnicode(paramObject.toString()));
/*      */   }
/*      */   
/*      */   public void println() throws IOException {
/*      */     this.out.write(this.lineSep);
/*      */   }
/*      */   
/*      */   public static String toSimpleString(JCTree paramJCTree) {
/*      */     return toSimpleString(paramJCTree, 20);
/*      */   }
/*      */   
/*      */   public static String toSimpleString(JCTree paramJCTree, int paramInt) {
/*      */     StringWriter stringWriter = new StringWriter();
/*      */     try {
/*      */       (new Pretty(stringWriter, false)).printExpr(paramJCTree);
/*      */     } catch (IOException iOException) {
/*      */       throw new AssertionError(iOException);
/*      */     } 
/*      */     String str = stringWriter.toString().trim().replaceAll("\\s+", " ").replaceAll("/\\*missing\\*/", "");
/*      */     if (str.length() < paramInt)
/*      */       return str; 
/*      */     int i = (paramInt - "[...]".length()) * 2 / 3;
/*      */     int j = paramInt - "[...]".length() - i;
/*      */     return str.substring(0, i) + "[...]" + str.substring(str.length() - j);
/*      */   }
/*      */   
/*      */   public Pretty(Writer paramWriter, boolean paramBoolean) {
/*   66 */     this.width = 4;
/*      */ 
/*      */ 
/*      */     
/*   70 */     this.lmargin = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   79 */     this.docComments = null;
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
/*  166 */     this.lineSep = System.getProperty("line.separator");
/*      */     this.out = paramWriter;
/*      */     this.sourceOutput = paramBoolean;
/*      */   }
/*      */ 
/*      */   
/*      */   private static class UncheckedIOException
/*      */     extends Error
/*      */   {
/*      */     UncheckedIOException(IOException param1IOException) {
/*  176 */       super(param1IOException.getMessage(), param1IOException);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final long serialVersionUID = -4032692679158424751L;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void printExpr(JCTree paramJCTree, int paramInt) throws IOException {
/*  188 */     int i = this.prec;
/*      */     try {
/*  190 */       this.prec = paramInt;
/*  191 */       if (paramJCTree == null) { print("/*missing*/"); }
/*      */       else
/*  193 */       { paramJCTree.accept(this); }
/*      */     
/*  195 */     } catch (UncheckedIOException uncheckedIOException) {
/*  196 */       IOException iOException = new IOException(uncheckedIOException.getMessage());
/*  197 */       iOException.initCause(uncheckedIOException);
/*  198 */       throw iOException;
/*      */     } finally {
/*  200 */       this.prec = i;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printExpr(JCTree paramJCTree) throws IOException {
/*  208 */     printExpr(paramJCTree, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void printStat(JCTree paramJCTree) throws IOException {
/*  214 */     printExpr(paramJCTree, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends JCTree> void printExprs(List<T> paramList, String paramString) throws IOException {
/*  221 */     if (paramList.nonEmpty()) {
/*  222 */       printExpr((JCTree)paramList.head);
/*  223 */       for (List list = paramList.tail; list.nonEmpty(); list = list.tail) {
/*  224 */         print(paramString);
/*  225 */         printExpr((JCTree)list.head);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends JCTree> void printExprs(List<T> paramList) throws IOException {
/*  233 */     printExprs(paramList, ", ");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void printStats(List<? extends JCTree> paramList) throws IOException {
/*  239 */     for (List<? extends JCTree> list = paramList; list.nonEmpty(); list = list.tail) {
/*  240 */       align();
/*  241 */       printStat((JCTree)list.head);
/*  242 */       println();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void printFlags(long paramLong) throws IOException {
/*  249 */     if ((paramLong & 0x1000L) != 0L) print("/*synthetic*/ "); 
/*  250 */     print(TreeInfo.flagNames(paramLong));
/*  251 */     if ((paramLong & 0x80000000FFFL) != 0L) print(" "); 
/*  252 */     if ((paramLong & 0x2000L) != 0L) print("@"); 
/*      */   }
/*      */   
/*      */   public void printAnnotations(List<JCTree.JCAnnotation> paramList) throws IOException {
/*  256 */     for (List<JCTree.JCAnnotation> list = paramList; list.nonEmpty(); list = list.tail) {
/*  257 */       printStat((JCTree)list.head);
/*  258 */       println();
/*  259 */       align();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void printTypeAnnotations(List<JCTree.JCAnnotation> paramList) throws IOException {
/*  264 */     for (List<JCTree.JCAnnotation> list = paramList; list.nonEmpty(); list = list.tail) {
/*  265 */       printExpr((JCTree)list.head);
/*  266 */       print(" ");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printDocComment(JCTree paramJCTree) throws IOException {
/*  274 */     if (this.docComments != null) {
/*  275 */       String str = this.docComments.getCommentText(paramJCTree);
/*  276 */       if (str != null) {
/*  277 */         print("/**"); println();
/*  278 */         int i = 0;
/*  279 */         int j = lineEndPos(str, i);
/*  280 */         while (i < str.length()) {
/*  281 */           align();
/*  282 */           print(" *");
/*  283 */           if (i < str.length() && str.charAt(i) > ' ') print(" "); 
/*  284 */           print(str.substring(i, j)); println();
/*  285 */           i = j + 1;
/*  286 */           j = lineEndPos(str, i);
/*      */         } 
/*  288 */         align(); print(" */"); println();
/*  289 */         align();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   static int lineEndPos(String paramString, int paramInt) {
/*  295 */     int i = paramString.indexOf('\n', paramInt);
/*  296 */     if (i < 0) i = paramString.length(); 
/*  297 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printTypeParameters(List<JCTree.JCTypeParameter> paramList) throws IOException {
/*  304 */     if (paramList.nonEmpty()) {
/*  305 */       print("<");
/*  306 */       printExprs(paramList);
/*  307 */       print(">");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void printBlock(List<? extends JCTree> paramList) throws IOException {
/*  314 */     print("{");
/*  315 */     println();
/*  316 */     indent();
/*  317 */     printStats(paramList);
/*  318 */     undent();
/*  319 */     align();
/*  320 */     print("}");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void printEnumBody(List<JCTree> paramList) throws IOException {
/*  326 */     print("{");
/*  327 */     println();
/*  328 */     indent();
/*  329 */     boolean bool = true; List<JCTree> list;
/*  330 */     for (list = paramList; list.nonEmpty(); list = list.tail) {
/*  331 */       if (isEnumerator((JCTree)list.head)) {
/*  332 */         if (!bool) {
/*  333 */           print(",");
/*  334 */           println();
/*      */         } 
/*  336 */         align();
/*  337 */         printStat((JCTree)list.head);
/*  338 */         bool = false;
/*      */       } 
/*      */     } 
/*  341 */     print(";");
/*  342 */     println();
/*  343 */     for (list = paramList; list.nonEmpty(); list = list.tail) {
/*  344 */       if (!isEnumerator((JCTree)list.head)) {
/*  345 */         align();
/*  346 */         printStat((JCTree)list.head);
/*  347 */         println();
/*      */       } 
/*      */     } 
/*  350 */     undent();
/*  351 */     align();
/*  352 */     print("}");
/*      */   }
/*      */ 
/*      */   
/*      */   boolean isEnumerator(JCTree paramJCTree) {
/*  357 */     return (paramJCTree.hasTag(JCTree.Tag.VARDEF) && (((JCTree.JCVariableDecl)paramJCTree).mods.flags & 0x4000L) != 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printUnit(JCTree.JCCompilationUnit paramJCCompilationUnit, JCTree.JCClassDecl paramJCClassDecl) throws IOException {
/*  368 */     this.docComments = paramJCCompilationUnit.docComments;
/*  369 */     printDocComment(paramJCCompilationUnit);
/*  370 */     if (paramJCCompilationUnit.pid != null) {
/*  371 */       print("package ");
/*  372 */       printExpr(paramJCCompilationUnit.pid);
/*  373 */       print(";");
/*  374 */       println();
/*      */     } 
/*  376 */     boolean bool = true;
/*  377 */     List<JCTree> list = paramJCCompilationUnit.defs;
/*  378 */     for (; list.nonEmpty() && (paramJCClassDecl == null || ((JCTree)list.head).hasTag(JCTree.Tag.IMPORT)); 
/*  379 */       list = list.tail) {
/*  380 */       if (((JCTree)list.head).hasTag(JCTree.Tag.IMPORT)) {
/*  381 */         JCTree.JCImport jCImport = (JCTree.JCImport)list.head;
/*  382 */         Name name = TreeInfo.name(jCImport.qualid);
/*  383 */         if (name == name.table.names.asterisk || paramJCClassDecl == null || 
/*      */           
/*  385 */           isUsed(TreeInfo.symbol(jCImport.qualid), paramJCClassDecl)) {
/*  386 */           if (bool) {
/*  387 */             bool = false;
/*  388 */             println();
/*      */           } 
/*  390 */           printStat(jCImport);
/*      */         } 
/*      */       } else {
/*  393 */         printStat((JCTree)list.head);
/*      */       } 
/*      */     } 
/*  396 */     if (paramJCClassDecl != null) {
/*  397 */       printStat(paramJCClassDecl);
/*  398 */       println();
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean isUsed(final Symbol t, JCTree paramJCTree) {
/*      */     class UsedVisitor extends TreeScanner {
/*      */       public void scan(JCTree param1JCTree) {
/*  405 */         if (param1JCTree != null && !this.result) param1JCTree.accept(this); 
/*      */       }
/*      */       boolean result = false;
/*      */       public void visitIdent(JCTree.JCIdent param1JCIdent) {
/*  409 */         if (param1JCIdent.sym == t) this.result = true; 
/*      */       }
/*      */     };
/*  412 */     UsedVisitor usedVisitor = new UsedVisitor();
/*  413 */     usedVisitor.scan(paramJCTree);
/*  414 */     return usedVisitor.result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/*      */     try {
/*  423 */       printUnit(paramJCCompilationUnit, null);
/*  424 */     } catch (IOException iOException) {
/*  425 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitImport(JCTree.JCImport paramJCImport) {
/*      */     try {
/*  431 */       print("import ");
/*  432 */       if (paramJCImport.staticImport) print("static "); 
/*  433 */       printExpr(paramJCImport.qualid);
/*  434 */       print(";");
/*  435 */       println();
/*  436 */     } catch (IOException iOException) {
/*  437 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/*      */     try {
/*  443 */       println(); align();
/*  444 */       printDocComment(paramJCClassDecl);
/*  445 */       printAnnotations(paramJCClassDecl.mods.annotations);
/*  446 */       printFlags(paramJCClassDecl.mods.flags & 0xFFFFFFFFFFFFFDFFL);
/*  447 */       Name name = this.enclClassName;
/*  448 */       this.enclClassName = paramJCClassDecl.name;
/*  449 */       if ((paramJCClassDecl.mods.flags & 0x200L) != 0L) {
/*  450 */         print("interface " + paramJCClassDecl.name);
/*  451 */         printTypeParameters(paramJCClassDecl.typarams);
/*  452 */         if (paramJCClassDecl.implementing.nonEmpty()) {
/*  453 */           print(" extends ");
/*  454 */           printExprs(paramJCClassDecl.implementing);
/*      */         } 
/*      */       } else {
/*  457 */         if ((paramJCClassDecl.mods.flags & 0x4000L) != 0L) {
/*  458 */           print("enum " + paramJCClassDecl.name);
/*      */         } else {
/*  460 */           print("class " + paramJCClassDecl.name);
/*  461 */         }  printTypeParameters(paramJCClassDecl.typarams);
/*  462 */         if (paramJCClassDecl.extending != null) {
/*  463 */           print(" extends ");
/*  464 */           printExpr(paramJCClassDecl.extending);
/*      */         } 
/*  466 */         if (paramJCClassDecl.implementing.nonEmpty()) {
/*  467 */           print(" implements ");
/*  468 */           printExprs(paramJCClassDecl.implementing);
/*      */         } 
/*      */       } 
/*  471 */       print(" ");
/*  472 */       if ((paramJCClassDecl.mods.flags & 0x4000L) != 0L) {
/*  473 */         printEnumBody(paramJCClassDecl.defs);
/*      */       } else {
/*  475 */         printBlock(paramJCClassDecl.defs);
/*      */       } 
/*  477 */       this.enclClassName = name;
/*  478 */     } catch (IOException iOException) {
/*  479 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*      */     try {
/*  486 */       if (paramJCMethodDecl.name == paramJCMethodDecl.name.table.names.init && this.enclClassName == null && this.sourceOutput) {
/*      */         return;
/*      */       }
/*  489 */       println(); align();
/*  490 */       printDocComment(paramJCMethodDecl);
/*  491 */       printExpr(paramJCMethodDecl.mods);
/*  492 */       printTypeParameters(paramJCMethodDecl.typarams);
/*  493 */       if (paramJCMethodDecl.name == paramJCMethodDecl.name.table.names.init) {
/*  494 */         print((this.enclClassName != null) ? this.enclClassName : paramJCMethodDecl.name);
/*      */       } else {
/*  496 */         printExpr(paramJCMethodDecl.restype);
/*  497 */         print(" " + paramJCMethodDecl.name);
/*      */       } 
/*  499 */       print("(");
/*  500 */       if (paramJCMethodDecl.recvparam != null) {
/*  501 */         printExpr(paramJCMethodDecl.recvparam);
/*  502 */         if (paramJCMethodDecl.params.size() > 0) {
/*  503 */           print(", ");
/*      */         }
/*      */       } 
/*  506 */       printExprs(paramJCMethodDecl.params);
/*  507 */       print(")");
/*  508 */       if (paramJCMethodDecl.thrown.nonEmpty()) {
/*  509 */         print(" throws ");
/*  510 */         printExprs(paramJCMethodDecl.thrown);
/*      */       } 
/*  512 */       if (paramJCMethodDecl.defaultValue != null) {
/*  513 */         print(" default ");
/*  514 */         printExpr(paramJCMethodDecl.defaultValue);
/*      */       } 
/*  516 */       if (paramJCMethodDecl.body != null) {
/*  517 */         print(" ");
/*  518 */         printStat(paramJCMethodDecl.body);
/*      */       } else {
/*  520 */         print(";");
/*      */       } 
/*  522 */     } catch (IOException iOException) {
/*  523 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*      */     try {
/*  529 */       if (this.docComments != null && this.docComments.hasComment(paramJCVariableDecl)) {
/*  530 */         println(); align();
/*      */       } 
/*  532 */       printDocComment(paramJCVariableDecl);
/*  533 */       if ((paramJCVariableDecl.mods.flags & 0x4000L) != 0L) {
/*  534 */         print("/*public static final*/ ");
/*  535 */         print(paramJCVariableDecl.name);
/*  536 */         if (paramJCVariableDecl.init != null) {
/*  537 */           if (this.sourceOutput && paramJCVariableDecl.init.hasTag(JCTree.Tag.NEWCLASS)) {
/*  538 */             print(" /*enum*/ ");
/*  539 */             JCTree.JCNewClass jCNewClass = (JCTree.JCNewClass)paramJCVariableDecl.init;
/*  540 */             if (jCNewClass.args != null && jCNewClass.args.nonEmpty()) {
/*  541 */               print("(");
/*  542 */               print(jCNewClass.args);
/*  543 */               print(")");
/*      */             } 
/*  545 */             if (jCNewClass.def != null && jCNewClass.def.defs != null) {
/*  546 */               print(" ");
/*  547 */               printBlock(jCNewClass.def.defs);
/*      */             } 
/*      */             return;
/*      */           } 
/*  551 */           print(" /* = ");
/*  552 */           printExpr(paramJCVariableDecl.init);
/*  553 */           print(" */");
/*      */         } 
/*      */       } else {
/*  556 */         printExpr(paramJCVariableDecl.mods);
/*  557 */         if ((paramJCVariableDecl.mods.flags & 0x400000000L) != 0L) {
/*  558 */           JCTree.JCExpression jCExpression = paramJCVariableDecl.vartype;
/*  559 */           List<JCTree.JCAnnotation> list = null;
/*  560 */           if (jCExpression instanceof JCTree.JCAnnotatedType) {
/*  561 */             list = ((JCTree.JCAnnotatedType)jCExpression).annotations;
/*  562 */             jCExpression = ((JCTree.JCAnnotatedType)jCExpression).underlyingType;
/*      */           } 
/*  564 */           printExpr(((JCTree.JCArrayTypeTree)jCExpression).elemtype);
/*  565 */           if (list != null) {
/*  566 */             print(Character.valueOf(' '));
/*  567 */             printTypeAnnotations(list);
/*      */           } 
/*  569 */           print("... " + paramJCVariableDecl.name);
/*      */         } else {
/*  571 */           printExpr(paramJCVariableDecl.vartype);
/*  572 */           print(" " + paramJCVariableDecl.name);
/*      */         } 
/*  574 */         if (paramJCVariableDecl.init != null) {
/*  575 */           print(" = ");
/*  576 */           printExpr(paramJCVariableDecl.init);
/*      */         } 
/*  578 */         if (this.prec == -1) print(";"); 
/*      */       } 
/*  580 */     } catch (IOException iOException) {
/*  581 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitSkip(JCTree.JCSkip paramJCSkip) {
/*      */     try {
/*  587 */       print(";");
/*  588 */     } catch (IOException iOException) {
/*  589 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/*      */     try {
/*  595 */       printFlags(paramJCBlock.flags);
/*  596 */       printBlock((List)paramJCBlock.stats);
/*  597 */     } catch (IOException iOException) {
/*  598 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/*      */     try {
/*  604 */       print("do ");
/*  605 */       printStat(paramJCDoWhileLoop.body);
/*  606 */       align();
/*  607 */       print(" while ");
/*  608 */       if (paramJCDoWhileLoop.cond.hasTag(JCTree.Tag.PARENS)) {
/*  609 */         printExpr(paramJCDoWhileLoop.cond);
/*      */       } else {
/*  611 */         print("(");
/*  612 */         printExpr(paramJCDoWhileLoop.cond);
/*  613 */         print(")");
/*      */       } 
/*  615 */       print(";");
/*  616 */     } catch (IOException iOException) {
/*  617 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/*      */     try {
/*  623 */       print("while ");
/*  624 */       if (paramJCWhileLoop.cond.hasTag(JCTree.Tag.PARENS)) {
/*  625 */         printExpr(paramJCWhileLoop.cond);
/*      */       } else {
/*  627 */         print("(");
/*  628 */         printExpr(paramJCWhileLoop.cond);
/*  629 */         print(")");
/*      */       } 
/*  631 */       print(" ");
/*  632 */       printStat(paramJCWhileLoop.body);
/*  633 */     } catch (IOException iOException) {
/*  634 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/*      */     try {
/*  640 */       print("for (");
/*  641 */       if (paramJCForLoop.init.nonEmpty()) {
/*  642 */         if (((JCTree.JCStatement)paramJCForLoop.init.head).hasTag(JCTree.Tag.VARDEF)) {
/*  643 */           printExpr((JCTree)paramJCForLoop.init.head);
/*  644 */           for (List list = paramJCForLoop.init.tail; list.nonEmpty(); list = list.tail) {
/*  645 */             JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list.head;
/*  646 */             print(", " + jCVariableDecl.name + " = ");
/*  647 */             printExpr(jCVariableDecl.init);
/*      */           } 
/*      */         } else {
/*  650 */           printExprs(paramJCForLoop.init);
/*      */         } 
/*      */       }
/*  653 */       print("; ");
/*  654 */       if (paramJCForLoop.cond != null) printExpr(paramJCForLoop.cond); 
/*  655 */       print("; ");
/*  656 */       printExprs(paramJCForLoop.step);
/*  657 */       print(") ");
/*  658 */       printStat(paramJCForLoop.body);
/*  659 */     } catch (IOException iOException) {
/*  660 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/*      */     try {
/*  666 */       print("for (");
/*  667 */       printExpr(paramJCEnhancedForLoop.var);
/*  668 */       print(" : ");
/*  669 */       printExpr(paramJCEnhancedForLoop.expr);
/*  670 */       print(") ");
/*  671 */       printStat(paramJCEnhancedForLoop.body);
/*  672 */     } catch (IOException iOException) {
/*  673 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/*      */     try {
/*  679 */       print(paramJCLabeledStatement.label + ": ");
/*  680 */       printStat(paramJCLabeledStatement.body);
/*  681 */     } catch (IOException iOException) {
/*  682 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/*      */     try {
/*  688 */       print("switch ");
/*  689 */       if (paramJCSwitch.selector.hasTag(JCTree.Tag.PARENS)) {
/*  690 */         printExpr(paramJCSwitch.selector);
/*      */       } else {
/*  692 */         print("(");
/*  693 */         printExpr(paramJCSwitch.selector);
/*  694 */         print(")");
/*      */       } 
/*  696 */       print(" {");
/*  697 */       println();
/*  698 */       printStats((List)paramJCSwitch.cases);
/*  699 */       align();
/*  700 */       print("}");
/*  701 */     } catch (IOException iOException) {
/*  702 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitCase(JCTree.JCCase paramJCCase) {
/*      */     try {
/*  708 */       if (paramJCCase.pat == null) {
/*  709 */         print("default");
/*      */       } else {
/*  711 */         print("case ");
/*  712 */         printExpr(paramJCCase.pat);
/*      */       } 
/*  714 */       print(": ");
/*  715 */       println();
/*  716 */       indent();
/*  717 */       printStats((List)paramJCCase.stats);
/*  718 */       undent();
/*  719 */       align();
/*  720 */     } catch (IOException iOException) {
/*  721 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/*      */     try {
/*  727 */       print("synchronized ");
/*  728 */       if (paramJCSynchronized.lock.hasTag(JCTree.Tag.PARENS)) {
/*  729 */         printExpr(paramJCSynchronized.lock);
/*      */       } else {
/*  731 */         print("(");
/*  732 */         printExpr(paramJCSynchronized.lock);
/*  733 */         print(")");
/*      */       } 
/*  735 */       print(" ");
/*  736 */       printStat(paramJCSynchronized.body);
/*  737 */     } catch (IOException iOException) {
/*  738 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTry(JCTree.JCTry paramJCTry) {
/*      */     try {
/*  744 */       print("try ");
/*  745 */       if (paramJCTry.resources.nonEmpty()) {
/*  746 */         print("(");
/*  747 */         boolean bool = true;
/*  748 */         for (JCTree jCTree : paramJCTry.resources) {
/*  749 */           if (!bool) {
/*  750 */             println();
/*  751 */             indent();
/*      */           } 
/*  753 */           printStat(jCTree);
/*  754 */           bool = false;
/*      */         } 
/*  756 */         print(") ");
/*      */       } 
/*  758 */       printStat(paramJCTry.body);
/*  759 */       for (List<JCTree.JCCatch> list = paramJCTry.catchers; list.nonEmpty(); list = list.tail) {
/*  760 */         printStat((JCTree)list.head);
/*      */       }
/*  762 */       if (paramJCTry.finalizer != null) {
/*  763 */         print(" finally ");
/*  764 */         printStat(paramJCTry.finalizer);
/*      */       } 
/*  766 */     } catch (IOException iOException) {
/*  767 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitCatch(JCTree.JCCatch paramJCCatch) {
/*      */     try {
/*  773 */       print(" catch (");
/*  774 */       printExpr(paramJCCatch.param);
/*  775 */       print(") ");
/*  776 */       printStat(paramJCCatch.body);
/*  777 */     } catch (IOException iOException) {
/*  778 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/*      */     try {
/*  784 */       open(this.prec, 3);
/*  785 */       printExpr(paramJCConditional.cond, 4);
/*  786 */       print(" ? ");
/*  787 */       printExpr(paramJCConditional.truepart);
/*  788 */       print(" : ");
/*  789 */       printExpr(paramJCConditional.falsepart, 3);
/*  790 */       close(this.prec, 3);
/*  791 */     } catch (IOException iOException) {
/*  792 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitIf(JCTree.JCIf paramJCIf) {
/*      */     try {
/*  798 */       print("if ");
/*  799 */       if (paramJCIf.cond.hasTag(JCTree.Tag.PARENS)) {
/*  800 */         printExpr(paramJCIf.cond);
/*      */       } else {
/*  802 */         print("(");
/*  803 */         printExpr(paramJCIf.cond);
/*  804 */         print(")");
/*      */       } 
/*  806 */       print(" ");
/*  807 */       printStat(paramJCIf.thenpart);
/*  808 */       if (paramJCIf.elsepart != null) {
/*  809 */         print(" else ");
/*  810 */         printStat(paramJCIf.elsepart);
/*      */       } 
/*  812 */     } catch (IOException iOException) {
/*  813 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/*      */     try {
/*  819 */       printExpr(paramJCExpressionStatement.expr);
/*  820 */       if (this.prec == -1) print(";"); 
/*  821 */     } catch (IOException iOException) {
/*  822 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitBreak(JCTree.JCBreak paramJCBreak) {
/*      */     try {
/*  828 */       print("break");
/*  829 */       if (paramJCBreak.label != null) print(" " + paramJCBreak.label); 
/*  830 */       print(";");
/*  831 */     } catch (IOException iOException) {
/*  832 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitContinue(JCTree.JCContinue paramJCContinue) {
/*      */     try {
/*  838 */       print("continue");
/*  839 */       if (paramJCContinue.label != null) print(" " + paramJCContinue.label); 
/*  840 */       print(";");
/*  841 */     } catch (IOException iOException) {
/*  842 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/*      */     try {
/*  848 */       print("return");
/*  849 */       if (paramJCReturn.expr != null) {
/*  850 */         print(" ");
/*  851 */         printExpr(paramJCReturn.expr);
/*      */       } 
/*  853 */       print(";");
/*  854 */     } catch (IOException iOException) {
/*  855 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/*      */     try {
/*  861 */       print("throw ");
/*  862 */       printExpr(paramJCThrow.expr);
/*  863 */       print(";");
/*  864 */     } catch (IOException iOException) {
/*  865 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/*      */     try {
/*  871 */       print("assert ");
/*  872 */       printExpr(paramJCAssert.cond);
/*  873 */       if (paramJCAssert.detail != null) {
/*  874 */         print(" : ");
/*  875 */         printExpr(paramJCAssert.detail);
/*      */       } 
/*  877 */       print(";");
/*  878 */     } catch (IOException iOException) {
/*  879 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/*      */     try {
/*  885 */       if (!paramJCMethodInvocation.typeargs.isEmpty()) {
/*  886 */         if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT)) {
/*  887 */           JCTree.JCFieldAccess jCFieldAccess = (JCTree.JCFieldAccess)paramJCMethodInvocation.meth;
/*  888 */           printExpr(jCFieldAccess.selected);
/*  889 */           print(".<");
/*  890 */           printExprs(paramJCMethodInvocation.typeargs);
/*  891 */           print(">" + jCFieldAccess.name);
/*      */         } else {
/*  893 */           print("<");
/*  894 */           printExprs(paramJCMethodInvocation.typeargs);
/*  895 */           print(">");
/*  896 */           printExpr(paramJCMethodInvocation.meth);
/*      */         } 
/*      */       } else {
/*  899 */         printExpr(paramJCMethodInvocation.meth);
/*      */       } 
/*  901 */       print("(");
/*  902 */       printExprs(paramJCMethodInvocation.args);
/*  903 */       print(")");
/*  904 */     } catch (IOException iOException) {
/*  905 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/*      */     try {
/*  911 */       if (paramJCNewClass.encl != null) {
/*  912 */         printExpr(paramJCNewClass.encl);
/*  913 */         print(".");
/*      */       } 
/*  915 */       print("new ");
/*  916 */       if (!paramJCNewClass.typeargs.isEmpty()) {
/*  917 */         print("<");
/*  918 */         printExprs(paramJCNewClass.typeargs);
/*  919 */         print(">");
/*      */       } 
/*  921 */       if (paramJCNewClass.def != null && paramJCNewClass.def.mods.annotations.nonEmpty()) {
/*  922 */         printTypeAnnotations(paramJCNewClass.def.mods.annotations);
/*      */       }
/*  924 */       printExpr(paramJCNewClass.clazz);
/*  925 */       print("(");
/*  926 */       printExprs(paramJCNewClass.args);
/*  927 */       print(")");
/*  928 */       if (paramJCNewClass.def != null) {
/*  929 */         Name name = this.enclClassName;
/*  930 */         this.enclClassName = (paramJCNewClass.def.name != null) ? paramJCNewClass.def.name : ((paramJCNewClass.type != null && paramJCNewClass.type.tsym.name != paramJCNewClass.type.tsym.name.table.names.empty) ? paramJCNewClass.type.tsym.name : null);
/*      */ 
/*      */ 
/*      */         
/*  934 */         if ((paramJCNewClass.def.mods.flags & 0x4000L) != 0L) print("/*enum*/"); 
/*  935 */         printBlock(paramJCNewClass.def.defs);
/*  936 */         this.enclClassName = name;
/*      */       } 
/*  938 */     } catch (IOException iOException) {
/*  939 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/*      */     try {
/*  945 */       if (paramJCNewArray.elemtype != null) {
/*  946 */         print("new ");
/*  947 */         JCTree.JCExpression jCExpression = paramJCNewArray.elemtype;
/*  948 */         printBaseElementType(jCExpression);
/*      */         
/*  950 */         if (!paramJCNewArray.annotations.isEmpty()) {
/*  951 */           print(Character.valueOf(' '));
/*  952 */           printTypeAnnotations(paramJCNewArray.annotations);
/*      */         } 
/*  954 */         if (paramJCNewArray.elems != null) {
/*  955 */           print("[]");
/*      */         }
/*      */         
/*  958 */         byte b = 0;
/*  959 */         List<List<JCTree.JCAnnotation>> list = paramJCNewArray.dimAnnotations;
/*  960 */         for (List<JCTree.JCExpression> list1 = paramJCNewArray.dims; list1.nonEmpty(); list1 = list1.tail) {
/*  961 */           if (list.size() > b && !((List)list.get(b)).isEmpty()) {
/*  962 */             print(Character.valueOf(' '));
/*  963 */             printTypeAnnotations((List<JCTree.JCAnnotation>)list.get(b));
/*      */           } 
/*  965 */           print("[");
/*  966 */           b++;
/*  967 */           printExpr((JCTree)list1.head);
/*  968 */           print("]");
/*      */         } 
/*  970 */         printBrackets(jCExpression);
/*      */       } 
/*  972 */       if (paramJCNewArray.elems != null) {
/*  973 */         print("{");
/*  974 */         printExprs(paramJCNewArray.elems);
/*  975 */         print("}");
/*      */       } 
/*  977 */     } catch (IOException iOException) {
/*  978 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/*      */     try {
/*  984 */       print("(");
/*  985 */       if (paramJCLambda.paramKind == JCTree.JCLambda.ParameterKind.EXPLICIT) {
/*  986 */         printExprs(paramJCLambda.params);
/*      */       } else {
/*  988 */         String str = "";
/*  989 */         for (JCTree.JCVariableDecl jCVariableDecl : paramJCLambda.params) {
/*  990 */           print(str);
/*  991 */           print(jCVariableDecl.name);
/*  992 */           str = ",";
/*      */         } 
/*      */       } 
/*  995 */       print(")->");
/*  996 */       printExpr(paramJCLambda.body);
/*  997 */     } catch (IOException iOException) {
/*  998 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/*      */     try {
/* 1004 */       print("(");
/* 1005 */       printExpr(paramJCParens.expr);
/* 1006 */       print(")");
/* 1007 */     } catch (IOException iOException) {
/* 1008 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/*      */     try {
/* 1014 */       open(this.prec, 1);
/* 1015 */       printExpr(paramJCAssign.lhs, 2);
/* 1016 */       print(" = ");
/* 1017 */       printExpr(paramJCAssign.rhs, 1);
/* 1018 */       close(this.prec, 1);
/* 1019 */     } catch (IOException iOException) {
/* 1020 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String operatorName(JCTree.Tag paramTag) {
/* 1025 */     switch (paramTag) { case INT:
/* 1026 */         return "+";
/* 1027 */       case LONG: return "-";
/* 1028 */       case FLOAT: return "!";
/* 1029 */       case DOUBLE: return "~";
/* 1030 */       case CHAR: return "++";
/* 1031 */       case BOOLEAN: return "--";
/* 1032 */       case BOT: return "++";
/* 1033 */       case BYTE: return "--";
/* 1034 */       case SHORT: return "<*nullchk*>";
/* 1035 */       case VOID: return "||";
/* 1036 */       case null: return "&&";
/* 1037 */       case null: return "==";
/* 1038 */       case null: return "!=";
/* 1039 */       case null: return "<";
/* 1040 */       case null: return ">";
/* 1041 */       case null: return "<=";
/* 1042 */       case null: return ">=";
/* 1043 */       case null: return "|";
/* 1044 */       case null: return "^";
/* 1045 */       case null: return "&";
/* 1046 */       case null: return "<<";
/* 1047 */       case null: return ">>";
/* 1048 */       case null: return ">>>";
/* 1049 */       case null: return "+";
/* 1050 */       case null: return "-";
/* 1051 */       case null: return "*";
/* 1052 */       case null: return "/";
/* 1053 */       case null: return "%"; }
/* 1054 */      throw new Error();
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/*      */     try {
/* 1060 */       open(this.prec, 2);
/* 1061 */       printExpr(paramJCAssignOp.lhs, 3);
/* 1062 */       print(" " + operatorName(paramJCAssignOp.getTag().noAssignOp()) + "= ");
/* 1063 */       printExpr(paramJCAssignOp.rhs, 2);
/* 1064 */       close(this.prec, 2);
/* 1065 */     } catch (IOException iOException) {
/* 1066 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/*      */     try {
/* 1072 */       int i = TreeInfo.opPrec(paramJCUnary.getTag());
/* 1073 */       String str = operatorName(paramJCUnary.getTag());
/* 1074 */       open(this.prec, i);
/* 1075 */       if (!paramJCUnary.getTag().isPostUnaryOp()) {
/* 1076 */         print(str);
/* 1077 */         printExpr(paramJCUnary.arg, i);
/*      */       } else {
/* 1079 */         printExpr(paramJCUnary.arg, i);
/* 1080 */         print(str);
/*      */       } 
/* 1082 */       close(this.prec, i);
/* 1083 */     } catch (IOException iOException) {
/* 1084 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/*      */     try {
/* 1090 */       int i = TreeInfo.opPrec(paramJCBinary.getTag());
/* 1091 */       String str = operatorName(paramJCBinary.getTag());
/* 1092 */       open(this.prec, i);
/* 1093 */       printExpr(paramJCBinary.lhs, i);
/* 1094 */       print(" " + str + " ");
/* 1095 */       printExpr(paramJCBinary.rhs, i + 1);
/* 1096 */       close(this.prec, i);
/* 1097 */     } catch (IOException iOException) {
/* 1098 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/*      */     try {
/* 1104 */       open(this.prec, 14);
/* 1105 */       print("(");
/* 1106 */       printExpr(paramJCTypeCast.clazz);
/* 1107 */       print(")");
/* 1108 */       printExpr(paramJCTypeCast.expr, 14);
/* 1109 */       close(this.prec, 14);
/* 1110 */     } catch (IOException iOException) {
/* 1111 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/*      */     try {
/* 1117 */       open(this.prec, 10);
/* 1118 */       printExpr(paramJCInstanceOf.expr, 10);
/* 1119 */       print(" instanceof ");
/* 1120 */       printExpr(paramJCInstanceOf.clazz, 11);
/* 1121 */       close(this.prec, 10);
/* 1122 */     } catch (IOException iOException) {
/* 1123 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/*      */     try {
/* 1129 */       printExpr(paramJCArrayAccess.indexed, 15);
/* 1130 */       print("[");
/* 1131 */       printExpr(paramJCArrayAccess.index);
/* 1132 */       print("]");
/* 1133 */     } catch (IOException iOException) {
/* 1134 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/*      */     try {
/* 1140 */       printExpr(paramJCFieldAccess.selected, 15);
/* 1141 */       print("." + paramJCFieldAccess.name);
/* 1142 */     } catch (IOException iOException) {
/* 1143 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/*      */     try {
/* 1149 */       printExpr(paramJCMemberReference.expr);
/* 1150 */       print("::");
/* 1151 */       if (paramJCMemberReference.typeargs != null) {
/* 1152 */         print("<");
/* 1153 */         printExprs(paramJCMemberReference.typeargs);
/* 1154 */         print(">");
/*      */       } 
/* 1156 */       print((paramJCMemberReference.getMode() == MemberReferenceTree.ReferenceMode.INVOKE) ? paramJCMemberReference.name : "new");
/* 1157 */     } catch (IOException iOException) {
/* 1158 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/*      */     try {
/* 1164 */       print(paramJCIdent.name);
/* 1165 */     } catch (IOException iOException) {
/* 1166 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/*      */     try {
/* 1172 */       switch (paramJCLiteral.typetag) {
/*      */         case INT:
/* 1174 */           print(paramJCLiteral.value.toString());
/*      */           return;
/*      */         case LONG:
/* 1177 */           print(paramJCLiteral.value + "L");
/*      */           return;
/*      */         case FLOAT:
/* 1180 */           print(paramJCLiteral.value + "F");
/*      */           return;
/*      */         case DOUBLE:
/* 1183 */           print(paramJCLiteral.value.toString());
/*      */           return;
/*      */         case CHAR:
/* 1186 */           print("'" + 
/* 1187 */               Convert.quote(
/* 1188 */                 String.valueOf((char)((Number)paramJCLiteral.value).intValue())) + "'");
/*      */           return;
/*      */         
/*      */         case BOOLEAN:
/* 1192 */           print((((Number)paramJCLiteral.value).intValue() == 1) ? "true" : "false");
/*      */           return;
/*      */         case BOT:
/* 1195 */           print("null");
/*      */           return;
/*      */       } 
/* 1198 */       print("\"" + Convert.quote(paramJCLiteral.value.toString()) + "\"");
/*      */     
/*      */     }
/* 1201 */     catch (IOException iOException) {
/* 1202 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) {
/*      */     try {
/* 1208 */       switch (paramJCPrimitiveTypeTree.typetag) {
/*      */         case BYTE:
/* 1210 */           print("byte");
/*      */           return;
/*      */         case CHAR:
/* 1213 */           print("char");
/*      */           return;
/*      */         case SHORT:
/* 1216 */           print("short");
/*      */           return;
/*      */         case INT:
/* 1219 */           print("int");
/*      */           return;
/*      */         case LONG:
/* 1222 */           print("long");
/*      */           return;
/*      */         case FLOAT:
/* 1225 */           print("float");
/*      */           return;
/*      */         case DOUBLE:
/* 1228 */           print("double");
/*      */           return;
/*      */         case BOOLEAN:
/* 1231 */           print("boolean");
/*      */           return;
/*      */         case VOID:
/* 1234 */           print("void");
/*      */           return;
/*      */       } 
/* 1237 */       print("error");
/*      */     
/*      */     }
/* 1240 */     catch (IOException iOException) {
/* 1241 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/*      */     try {
/* 1247 */       printBaseElementType(paramJCArrayTypeTree);
/* 1248 */       printBrackets(paramJCArrayTypeTree);
/* 1249 */     } catch (IOException iOException) {
/* 1250 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void printBaseElementType(JCTree paramJCTree) throws IOException {
/* 1256 */     printExpr(TreeInfo.innermostType(paramJCTree));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void printBrackets(JCTree paramJCTree) throws IOException {
/* 1262 */     JCTree jCTree = paramJCTree;
/*      */     while (true) {
/* 1264 */       if (jCTree.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 1265 */         JCTree.JCAnnotatedType jCAnnotatedType = (JCTree.JCAnnotatedType)jCTree;
/* 1266 */         jCTree = jCAnnotatedType.underlyingType;
/* 1267 */         if (jCTree.hasTag(JCTree.Tag.TYPEARRAY)) {
/* 1268 */           print(Character.valueOf(' '));
/* 1269 */           printTypeAnnotations(jCAnnotatedType.annotations);
/*      */         } 
/*      */       } 
/* 1272 */       if (jCTree.hasTag(JCTree.Tag.TYPEARRAY)) {
/* 1273 */         print("[]");
/* 1274 */         jCTree = ((JCTree.JCArrayTypeTree)jCTree).elemtype;
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply) {
/*      */     try {
/* 1283 */       printExpr(paramJCTypeApply.clazz);
/* 1284 */       print("<");
/* 1285 */       printExprs(paramJCTypeApply.arguments);
/* 1286 */       print(">");
/* 1287 */     } catch (IOException iOException) {
/* 1288 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeUnion(JCTree.JCTypeUnion paramJCTypeUnion) {
/*      */     try {
/* 1294 */       printExprs(paramJCTypeUnion.alternatives, " | ");
/* 1295 */     } catch (IOException iOException) {
/* 1296 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/*      */     try {
/* 1302 */       printExprs(paramJCTypeIntersection.bounds, " & ");
/* 1303 */     } catch (IOException iOException) {
/* 1304 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/*      */     try {
/* 1310 */       if (paramJCTypeParameter.annotations.nonEmpty()) {
/* 1311 */         printTypeAnnotations(paramJCTypeParameter.annotations);
/*      */       }
/* 1313 */       print(paramJCTypeParameter.name);
/* 1314 */       if (paramJCTypeParameter.bounds.nonEmpty()) {
/* 1315 */         print(" extends ");
/* 1316 */         printExprs(paramJCTypeParameter.bounds, " & ");
/*      */       } 
/* 1318 */     } catch (IOException iOException) {
/* 1319 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard) {
/*      */     try {
/* 1326 */       print(paramJCWildcard.kind);
/* 1327 */       if (paramJCWildcard.kind.kind != BoundKind.UNBOUND)
/* 1328 */         printExpr(paramJCWildcard.inner); 
/* 1329 */     } catch (IOException iOException) {
/* 1330 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void visitTypeBoundKind(JCTree.TypeBoundKind paramTypeBoundKind) {
/*      */     try {
/* 1337 */       print(String.valueOf(paramTypeBoundKind.kind));
/* 1338 */     } catch (IOException iOException) {
/* 1339 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {
/*      */     try {
/* 1345 */       print("(ERROR)");
/* 1346 */     } catch (IOException iOException) {
/* 1347 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) {
/*      */     try {
/* 1353 */       print("(let " + paramLetExpr.defs + " in " + paramLetExpr.expr + ")");
/* 1354 */     } catch (IOException iOException) {
/* 1355 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitModifiers(JCTree.JCModifiers paramJCModifiers) {
/*      */     try {
/* 1361 */       printAnnotations(paramJCModifiers.annotations);
/* 1362 */       printFlags(paramJCModifiers.flags);
/* 1363 */     } catch (IOException iOException) {
/* 1364 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/*      */     try {
/* 1370 */       print("@");
/* 1371 */       printExpr(paramJCAnnotation.annotationType);
/* 1372 */       print("(");
/* 1373 */       printExprs(paramJCAnnotation.args);
/* 1374 */       print(")");
/* 1375 */     } catch (IOException iOException) {
/* 1376 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/*      */     try {
/* 1382 */       if (paramJCAnnotatedType.underlyingType.hasTag(JCTree.Tag.SELECT)) {
/* 1383 */         JCTree.JCFieldAccess jCFieldAccess = (JCTree.JCFieldAccess)paramJCAnnotatedType.underlyingType;
/* 1384 */         printExpr(jCFieldAccess.selected, 15);
/* 1385 */         print(".");
/* 1386 */         printTypeAnnotations(paramJCAnnotatedType.annotations);
/* 1387 */         print(jCFieldAccess.name);
/* 1388 */       } else if (paramJCAnnotatedType.underlyingType.hasTag(JCTree.Tag.TYPEARRAY)) {
/* 1389 */         printBaseElementType(paramJCAnnotatedType);
/* 1390 */         printBrackets(paramJCAnnotatedType);
/*      */       } else {
/* 1392 */         printTypeAnnotations(paramJCAnnotatedType.annotations);
/* 1393 */         printExpr(paramJCAnnotatedType.underlyingType);
/*      */       } 
/* 1395 */     } catch (IOException iOException) {
/* 1396 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void visitTree(JCTree paramJCTree) {
/*      */     try {
/* 1402 */       print("(UNKNOWN: " + paramJCTree + ")");
/* 1403 */       println();
/* 1404 */     } catch (IOException iOException) {
/* 1405 */       throw new UncheckedIOException(iOException);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\Pretty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */