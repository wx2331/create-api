/*      */ package com.sun.tools.javac.tree;
/*      */ import com.sun.source.tree.AnnotationTree;
/*      */ import com.sun.source.tree.ArrayAccessTree;
/*      */ import com.sun.source.tree.ArrayTypeTree;
/*      */ import com.sun.source.tree.AssertTree;
/*      */ import com.sun.source.tree.AssignmentTree;
/*      */ import com.sun.source.tree.BinaryTree;
/*      */ import com.sun.source.tree.BlockTree;
/*      */ import com.sun.source.tree.BreakTree;
/*      */ import com.sun.source.tree.CaseTree;
/*      */ import com.sun.source.tree.CatchTree;
/*      */ import com.sun.source.tree.ClassTree;
/*      */ import com.sun.source.tree.CompilationUnitTree;
/*      */ import com.sun.source.tree.CompoundAssignmentTree;
/*      */ import com.sun.source.tree.ConditionalExpressionTree;
/*      */ import com.sun.source.tree.DoWhileLoopTree;
/*      */ import com.sun.source.tree.EmptyStatementTree;
/*      */ import com.sun.source.tree.EnhancedForLoopTree;
/*      */ import com.sun.source.tree.ErroneousTree;
/*      */ import com.sun.source.tree.ExpressionStatementTree;
/*      */ import com.sun.source.tree.ExpressionTree;
/*      */ import com.sun.source.tree.ForLoopTree;
/*      */ import com.sun.source.tree.IdentifierTree;
/*      */ import com.sun.source.tree.IfTree;
/*      */ import com.sun.source.tree.ImportTree;
/*      */ import com.sun.source.tree.InstanceOfTree;
/*      */ import com.sun.source.tree.IntersectionTypeTree;
/*      */ import com.sun.source.tree.LabeledStatementTree;
/*      */ import com.sun.source.tree.LambdaExpressionTree;
/*      */ import com.sun.source.tree.LineMap;
/*      */ import com.sun.source.tree.LiteralTree;
/*      */ import com.sun.source.tree.MemberReferenceTree;
/*      */ import com.sun.source.tree.MemberSelectTree;
/*      */ import com.sun.source.tree.MethodInvocationTree;
/*      */ import com.sun.source.tree.MethodTree;
/*      */ import com.sun.source.tree.ModifiersTree;
/*      */ import com.sun.source.tree.NewArrayTree;
/*      */ import com.sun.source.tree.NewClassTree;
/*      */ import com.sun.source.tree.ParameterizedTypeTree;
/*      */ import com.sun.source.tree.ParenthesizedTree;
/*      */ import com.sun.source.tree.PrimitiveTypeTree;
/*      */ import com.sun.source.tree.ReturnTree;
/*      */ import com.sun.source.tree.StatementTree;
/*      */ import com.sun.source.tree.SwitchTree;
/*      */ import com.sun.source.tree.SynchronizedTree;
/*      */ import com.sun.source.tree.ThrowTree;
/*      */ import com.sun.source.tree.Tree;
/*      */ import com.sun.source.tree.TreeVisitor;
/*      */ import com.sun.source.tree.TryTree;
/*      */ import com.sun.source.tree.TypeCastTree;
/*      */ import com.sun.source.tree.TypeParameterTree;
/*      */ import com.sun.source.tree.UnaryTree;
/*      */ import com.sun.source.tree.UnionTypeTree;
/*      */ import com.sun.source.tree.VariableTree;
/*      */ import com.sun.source.tree.WhileLoopTree;
/*      */ import com.sun.source.tree.WildcardTree;
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Position;
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.element.Name;
/*      */ import javax.lang.model.type.TypeKind;
/*      */ import javax.tools.JavaFileObject;
/*      */
/*      */ public abstract class JCTree implements Tree, Cloneable, JCDiagnostic.DiagnosticPosition {
/*      */   public int pos;
/*      */   public Type type;
/*      */
/*      */   public abstract Tag getTag();
/*      */
/*      */   public enum Tag {
/*   86 */     NO_TAG,
/*      */
/*      */
/*      */
/*   90 */     TOPLEVEL,
/*      */
/*      */
/*      */
/*   94 */     IMPORT,
/*      */
/*      */
/*      */
/*   98 */     CLASSDEF,
/*      */
/*      */
/*      */
/*  102 */     METHODDEF,
/*      */
/*      */
/*      */
/*  106 */     VARDEF,
/*      */
/*      */
/*      */
/*  110 */     SKIP,
/*      */
/*      */
/*      */
/*  114 */     BLOCK,
/*      */
/*      */
/*      */
/*  118 */     DOLOOP,
/*      */
/*      */
/*      */
/*  122 */     WHILELOOP,
/*      */
/*      */
/*      */
/*  126 */     FORLOOP,
/*      */
/*      */
/*      */
/*  130 */     FOREACHLOOP,
/*      */
/*      */
/*      */
/*  134 */     LABELLED,
/*      */
/*      */
/*      */
/*  138 */     SWITCH,
/*      */
/*      */
/*      */
/*  142 */     CASE,
/*      */
/*      */
/*      */
/*  146 */     SYNCHRONIZED,
/*      */
/*      */
/*      */
/*  150 */     TRY,
/*      */
/*      */
/*      */
/*  154 */     CATCH,
/*      */
/*      */
/*      */
/*  158 */     CONDEXPR,
/*      */
/*      */
/*      */
/*  162 */     IF,
/*      */
/*      */
/*      */
/*  166 */     EXEC,
/*      */
/*      */
/*      */
/*  170 */     BREAK,
/*      */
/*      */
/*      */
/*  174 */     CONTINUE,
/*      */
/*      */
/*      */
/*  178 */     RETURN,
/*      */
/*      */
/*      */
/*  182 */     THROW,
/*      */
/*      */
/*      */
/*  186 */     ASSERT,
/*      */
/*      */
/*      */
/*  190 */     APPLY,
/*      */
/*      */
/*      */
/*  194 */     NEWCLASS,
/*      */
/*      */
/*      */
/*  198 */     NEWARRAY,
/*      */
/*      */
/*      */
/*  202 */     LAMBDA,
/*      */
/*      */
/*      */
/*  206 */     PARENS,
/*      */
/*      */
/*      */
/*  210 */     ASSIGN,
/*      */
/*      */
/*      */
/*  214 */     TYPECAST,
/*      */
/*      */
/*      */
/*  218 */     TYPETEST,
/*      */
/*      */
/*      */
/*  222 */     INDEXED,
/*      */
/*      */
/*      */
/*  226 */     SELECT,
/*      */
/*      */
/*      */
/*  230 */     REFERENCE,
/*      */
/*      */
/*      */
/*  234 */     IDENT,
/*      */
/*      */
/*      */
/*  238 */     LITERAL,
/*      */
/*      */
/*      */
/*  242 */     TYPEIDENT,
/*      */
/*      */
/*      */
/*  246 */     TYPEARRAY,
/*      */
/*      */
/*      */
/*  250 */     TYPEAPPLY,
/*      */
/*      */
/*      */
/*  254 */     TYPEUNION,
/*      */
/*      */
/*      */
/*  258 */     TYPEINTERSECTION,
/*      */
/*      */
/*      */
/*  262 */     TYPEPARAMETER,
/*      */
/*      */
/*      */
/*  266 */     WILDCARD,
/*      */
/*      */
/*      */
/*  270 */     TYPEBOUNDKIND,
/*      */
/*      */
/*      */
/*  274 */     ANNOTATION,
/*      */
/*      */
/*      */
/*  278 */     TYPE_ANNOTATION,
/*      */
/*      */
/*      */
/*  282 */     MODIFIERS,
/*      */
/*      */
/*      */
/*  286 */     ANNOTATED_TYPE,
/*      */
/*      */
/*      */
/*  290 */     ERRONEOUS,
/*      */
/*      */
/*      */
/*  294 */     POS,
/*  295 */     NEG,
/*  296 */     NOT,
/*  297 */     COMPL,
/*  298 */     PREINC,
/*  299 */     PREDEC,
/*  300 */     POSTINC,
/*  301 */     POSTDEC,
/*      */
/*      */
/*      */
/*  305 */     NULLCHK,
/*      */
/*      */
/*      */
/*  309 */     OR,
/*  310 */     AND,
/*  311 */     BITOR,
/*  312 */     BITXOR,
/*  313 */     BITAND,
/*  314 */     EQ,
/*  315 */     NE,
/*  316 */     LT,
/*  317 */     GT,
/*  318 */     LE,
/*  319 */     GE,
/*  320 */     SL,
/*  321 */     SR,
/*  322 */     USR,
/*  323 */     PLUS,
/*  324 */     MINUS,
/*  325 */     MUL,
/*  326 */     DIV,
/*  327 */     MOD,
/*      */
/*      */
/*      */
/*  331 */     BITOR_ASG((String)BITOR),
/*  332 */     BITXOR_ASG((String)BITXOR),
/*  333 */     BITAND_ASG((String)BITAND),
/*      */
/*  335 */     SL_ASG((String)SL),
/*  336 */     SR_ASG((String)SR),
/*  337 */     USR_ASG((String)USR),
/*  338 */     PLUS_ASG((String)PLUS),
/*  339 */     MINUS_ASG((String)MINUS),
/*  340 */     MUL_ASG((String)MUL),
/*  341 */     DIV_ASG((String)DIV),
/*  342 */     MOD_ASG((String)MOD),
/*      */
/*      */
/*      */
/*  346 */     LETEXPR;
/*      */
/*      */     private final Tag noAssignTag;
/*      */
/*  350 */     private static final int numberOfOperators = MOD.ordinal() - POS.ordinal() + 1; static {
/*      */
/*      */     } Tag(Tag param1Tag) {
/*  353 */       this.noAssignTag = param1Tag;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public static int getNumberOfOperators() {
/*  361 */       return numberOfOperators;
/*      */     }
/*      */
/*      */     public Tag noAssignOp() {
/*  365 */       if (this.noAssignTag != null)
/*  366 */         return this.noAssignTag;
/*  367 */       throw new AssertionError("noAssignOp() method is not available for non assignment tags");
/*      */     }
/*      */
/*      */     public boolean isPostUnaryOp() {
/*  371 */       return (this == POSTINC || this == POSTDEC);
/*      */     }
/*      */
/*      */     public boolean isIncOrDecUnaryOp() {
/*  375 */       return (this == PREINC || this == PREDEC || this == POSTINC || this == POSTDEC);
/*      */     }
/*      */
/*      */     public boolean isAssignop() {
/*  379 */       return (this.noAssignTag != null);
/*      */     }
/*      */
/*      */     public int operatorIndex() {
/*  383 */       return ordinal() - POS.ordinal();
/*      */     }
/*      */   }
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
/*      */   public boolean hasTag(Tag paramTag) {
/*  402 */     return (paramTag == getTag());
/*      */   }
/*      */
/*      */
/*      */
/*      */   public String toString() {
/*  408 */     StringWriter stringWriter = new StringWriter();
/*      */     try {
/*  410 */       (new Pretty(stringWriter, false)).printExpr(this);
/*      */     }
/*  412 */     catch (IOException iOException) {
/*      */
/*      */
/*  415 */       throw new AssertionError(iOException);
/*      */     }
/*  417 */     return stringWriter.toString();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public JCTree setPos(int paramInt) {
/*  423 */     this.pos = paramInt;
/*  424 */     return this;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public JCTree setType(Type paramType) {
/*  430 */     this.type = paramType;
/*  431 */     return this;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public abstract void accept(Visitor paramVisitor);
/*      */
/*      */
/*      */
/*      */   public abstract <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD);
/*      */
/*      */
/*      */   public Object clone() {
/*      */     try {
/*  445 */       return super.clone();
/*  446 */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/*  447 */       throw new RuntimeException(cloneNotSupportedException);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public JCDiagnostic.DiagnosticPosition pos() {
/*  454 */     return this;
/*      */   }
/*      */
/*      */
/*      */   public JCTree getTree() {
/*  459 */     return this;
/*      */   }
/*      */
/*      */
/*      */   public int getStartPosition() {
/*  464 */     return TreeInfo.getStartPos(this);
/*      */   }
/*      */
/*      */
/*      */   public int getPreferredPosition() {
/*  469 */     return this.pos;
/*      */   }
/*      */
/*      */
/*      */   public int getEndPosition(EndPosTable paramEndPosTable) {
/*  474 */     return TreeInfo.getEndPos(this, paramEndPosTable);
/*      */   }
/*      */
/*      */
/*      */   public static class JCCompilationUnit
/*      */     extends JCTree
/*      */     implements CompilationUnitTree
/*      */   {
/*      */     public List<JCAnnotation> packageAnnotations;
/*      */
/*      */     public JCExpression pid;
/*      */
/*      */     public List<JCTree> defs;
/*      */
/*      */     public JavaFileObject sourcefile;
/*      */
/*      */     public Symbol.PackageSymbol packge;
/*      */
/*      */     public Scope.ImportScope namedImportScope;
/*      */
/*      */     public Scope.StarImportScope starImportScope;
/*  495 */     public Position.LineMap lineMap = null;
/*      */
/*      */
/*  498 */     public DocCommentTable docComments = null;
/*      */
/*      */
/*  501 */     public EndPosTable endPositions = null;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     protected JCCompilationUnit(List<JCAnnotation> param1List, JCExpression param1JCExpression, List<JCTree> param1List1, JavaFileObject param1JavaFileObject, Symbol.PackageSymbol param1PackageSymbol, Scope.ImportScope param1ImportScope, Scope.StarImportScope param1StarImportScope) {
/*  509 */       this.packageAnnotations = param1List;
/*  510 */       this.pid = param1JCExpression;
/*  511 */       this.defs = param1List1;
/*  512 */       this.sourcefile = param1JavaFileObject;
/*  513 */       this.packge = param1PackageSymbol;
/*  514 */       this.namedImportScope = param1ImportScope;
/*  515 */       this.starImportScope = param1StarImportScope;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/*  518 */       param1Visitor.visitTopLevel(this);
/*      */     } public Kind getKind() {
/*  520 */       return Kind.COMPILATION_UNIT;
/*      */     } public List<JCAnnotation> getPackageAnnotations() {
/*  522 */       return this.packageAnnotations;
/*      */     }
/*      */     public List<JCImport> getImports() {
/*  525 */       ListBuffer listBuffer = new ListBuffer();
/*  526 */       for (JCTree jCTree : this.defs) {
/*  527 */         if (jCTree.hasTag(Tag.IMPORT)) {
/*  528 */           listBuffer.append(jCTree); continue;
/*  529 */         }  if (!jCTree.hasTag(Tag.SKIP))
/*      */           break;
/*      */       }
/*  532 */       return listBuffer.toList();
/*      */     } public JCExpression getPackageName() {
/*  534 */       return this.pid;
/*      */     } public JavaFileObject getSourceFile() {
/*  536 */       return this.sourcefile;
/*      */     }
/*      */     public Position.LineMap getLineMap() {
/*  539 */       return this.lineMap;
/*      */     }
/*      */     public List<JCTree> getTypeDecls() {
/*      */       List<JCTree> list;
/*  543 */       for (list = this.defs; !list.isEmpty() && (
/*  544 */         (JCTree)list.head).hasTag(Tag.IMPORT); list = list.tail);
/*      */
/*  546 */       return list;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  550 */       return (R)param1TreeVisitor.visitCompilationUnit(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  555 */       return Tag.TOPLEVEL;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCImport
/*      */     extends JCTree
/*      */     implements ImportTree
/*      */   {
/*      */     public boolean staticImport;
/*      */     public JCTree qualid;
/*      */
/*      */     protected JCImport(JCTree param1JCTree, boolean param1Boolean) {
/*  567 */       this.qualid = param1JCTree;
/*  568 */       this.staticImport = param1Boolean;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/*  571 */       param1Visitor.visitImport(this);
/*      */     }
/*  573 */     public boolean isStatic() { return this.staticImport; } public JCTree getQualifiedIdentifier() {
/*  574 */       return this.qualid;
/*      */     } public Kind getKind() {
/*  576 */       return Kind.IMPORT;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  579 */       return (R)param1TreeVisitor.visitImport(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  584 */       return Tag.IMPORT;
/*      */     }
/*      */   }
/*      */
/*      */   public static abstract class JCStatement
/*      */     extends JCTree implements StatementTree {
/*      */     public JCStatement setType(Type param1Type) {
/*  591 */       super.setType(param1Type);
/*  592 */       return this;
/*      */     }
/*      */
/*      */     public JCStatement setPos(int param1Int) {
/*  596 */       super.setPos(param1Int);
/*  597 */       return this;
/*      */     }
/*      */   }
/*      */
/*      */   public static abstract class JCExpression
/*      */     extends JCTree implements ExpressionTree {
/*      */     public JCExpression setType(Type param1Type) {
/*  604 */       super.setType(param1Type);
/*  605 */       return this;
/*      */     }
/*      */
/*      */     public JCExpression setPos(int param1Int) {
/*  609 */       super.setPos(param1Int);
/*  610 */       return this;
/*      */     }
/*      */
/*  613 */     public boolean isPoly() { return false; } public boolean isStandalone() {
/*  614 */       return true;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static abstract class JCPolyExpression
/*      */     extends JCExpression
/*      */   {
/*      */     public PolyKind polyKind;
/*      */
/*      */
/*      */     public enum PolyKind
/*      */     {
/*  628 */       STANDALONE,
/*      */
/*  630 */       POLY;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public boolean isPoly() {
/*  636 */       return (this.polyKind == PolyKind.POLY); } public boolean isStandalone() {
/*  637 */       return (this.polyKind == PolyKind.STANDALONE);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static abstract class JCFunctionalExpression
/*      */     extends JCPolyExpression
/*      */   {
/*      */     public List<Type> targets;
/*      */
/*      */
/*      */
/*      */
/*      */     public Type getDescriptorType(Types param1Types) {
/*  654 */       return this.targets.nonEmpty() ? param1Types.findDescriptorType((Type)this.targets.head) : param1Types.createErrorType(null);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static class JCClassDecl
/*      */     extends JCStatement
/*      */     implements ClassTree
/*      */   {
/*      */     public JCModifiers mods;
/*      */
/*      */
/*      */     public Name name;
/*      */
/*      */
/*      */     public List<JCTypeParameter> typarams;
/*      */
/*      */
/*      */     public JCExpression extending;
/*      */
/*      */
/*      */     public List<JCExpression> implementing;
/*      */
/*      */     public List<JCTree> defs;
/*      */
/*      */     public Symbol.ClassSymbol sym;
/*      */
/*      */
/*      */     protected JCClassDecl(JCModifiers param1JCModifiers, Name param1Name, List<JCTypeParameter> param1List, JCExpression param1JCExpression, List<JCExpression> param1List1, List<JCTree> param1List2, Symbol.ClassSymbol param1ClassSymbol) {
/*  684 */       this.mods = param1JCModifiers;
/*  685 */       this.name = param1Name;
/*  686 */       this.typarams = param1List;
/*  687 */       this.extending = param1JCExpression;
/*  688 */       this.implementing = param1List1;
/*  689 */       this.defs = param1List2;
/*  690 */       this.sym = param1ClassSymbol;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/*  693 */       param1Visitor.visitClassDef(this);
/*      */     }
/*      */     public Kind getKind() {
/*  696 */       if ((this.mods.flags & 0x2000L) != 0L)
/*  697 */         return Kind.ANNOTATION_TYPE;
/*  698 */       if ((this.mods.flags & 0x200L) != 0L)
/*  699 */         return Kind.INTERFACE;
/*  700 */       if ((this.mods.flags & 0x4000L) != 0L) {
/*  701 */         return Kind.ENUM;
/*      */       }
/*  703 */       return Kind.CLASS;
/*      */     }
/*      */
/*  706 */     public JCModifiers getModifiers() { return this.mods; } public Name getSimpleName() {
/*  707 */       return this.name;
/*      */     } public List<JCTypeParameter> getTypeParameters() {
/*  709 */       return this.typarams;
/*      */     } public JCExpression getExtendsClause() {
/*  711 */       return this.extending;
/*      */     } public List<JCExpression> getImplementsClause() {
/*  713 */       return this.implementing;
/*      */     }
/*      */     public List<JCTree> getMembers() {
/*  716 */       return this.defs;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  720 */       return (R)param1TreeVisitor.visitClass(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  725 */       return Tag.CLASSDEF;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static class JCMethodDecl
/*      */     extends JCTree
/*      */     implements MethodTree
/*      */   {
/*      */     public JCModifiers mods;
/*      */
/*      */
/*      */     public Name name;
/*      */
/*      */
/*      */     public JCExpression restype;
/*      */
/*      */
/*      */     public List<JCTypeParameter> typarams;
/*      */
/*      */
/*      */     public JCVariableDecl recvparam;
/*      */
/*      */
/*      */     public List<JCVariableDecl> params;
/*      */
/*      */
/*      */     public List<JCExpression> thrown;
/*      */
/*      */
/*      */     public JCBlock body;
/*      */
/*      */     public JCExpression defaultValue;
/*      */
/*      */     public Symbol.MethodSymbol sym;
/*      */
/*      */
/*      */     protected JCMethodDecl(JCModifiers param1JCModifiers, Name param1Name, JCExpression param1JCExpression1, List<JCTypeParameter> param1List, JCVariableDecl param1JCVariableDecl, List<JCVariableDecl> param1List1, List<JCExpression> param1List2, JCBlock param1JCBlock, JCExpression param1JCExpression2, Symbol.MethodSymbol param1MethodSymbol) {
/*  764 */       this.mods = param1JCModifiers;
/*  765 */       this.name = param1Name;
/*  766 */       this.restype = param1JCExpression1;
/*  767 */       this.typarams = param1List;
/*  768 */       this.params = param1List1;
/*  769 */       this.recvparam = param1JCVariableDecl;
/*      */
/*      */
/*  772 */       this.thrown = param1List2;
/*  773 */       this.body = param1JCBlock;
/*  774 */       this.defaultValue = param1JCExpression2;
/*  775 */       this.sym = param1MethodSymbol;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/*  778 */       param1Visitor.visitMethodDef(this);
/*      */     }
/*  780 */     public Kind getKind() { return Kind.METHOD; }
/*  781 */     public JCModifiers getModifiers() { return this.mods; }
/*  782 */     public Name getName() { return this.name; } public JCTree getReturnType() {
/*  783 */       return this.restype;
/*      */     } public List<JCTypeParameter> getTypeParameters() {
/*  785 */       return this.typarams;
/*      */     }
/*      */     public List<JCVariableDecl> getParameters() {
/*  788 */       return this.params;
/*      */     } public JCVariableDecl getReceiverParameter() {
/*  790 */       return this.recvparam;
/*      */     } public List<JCExpression> getThrows() {
/*  792 */       return this.thrown;
/*      */     } public JCBlock getBody() {
/*  794 */       return this.body;
/*      */     } public JCTree getDefaultValue() {
/*  796 */       return this.defaultValue;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  800 */       return (R)param1TreeVisitor.visitMethod(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  805 */       return Tag.METHODDEF;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static class JCVariableDecl
/*      */     extends JCStatement
/*      */     implements VariableTree
/*      */   {
/*      */     public JCModifiers mods;
/*      */
/*      */
/*      */     public Name name;
/*      */
/*      */
/*      */     public JCExpression nameexpr;
/*      */
/*      */     public JCExpression vartype;
/*      */
/*      */     public JCExpression init;
/*      */
/*      */     public Symbol.VarSymbol sym;
/*      */
/*      */
/*      */     protected JCVariableDecl(JCModifiers param1JCModifiers, Name param1Name, JCExpression param1JCExpression1, JCExpression param1JCExpression2, Symbol.VarSymbol param1VarSymbol) {
/*  831 */       this.mods = param1JCModifiers;
/*  832 */       this.name = param1Name;
/*  833 */       this.vartype = param1JCExpression1;
/*  834 */       this.init = param1JCExpression2;
/*  835 */       this.sym = param1VarSymbol;
/*      */     }
/*      */
/*      */
/*      */
/*      */     protected JCVariableDecl(JCModifiers param1JCModifiers, JCExpression param1JCExpression1, JCExpression param1JCExpression2) {
/*  841 */       this(param1JCModifiers, null, param1JCExpression2, null, null);
/*  842 */       this.nameexpr = param1JCExpression1;
/*  843 */       if (param1JCExpression1.hasTag(Tag.IDENT)) {
/*  844 */         this.name = ((JCIdent)param1JCExpression1).name;
/*      */       } else {
/*      */
/*  847 */         this.name = ((JCFieldAccess)param1JCExpression1).name;
/*      */       }
/*      */     }
/*      */
/*      */     public void accept(Visitor param1Visitor) {
/*  852 */       param1Visitor.visitVarDef(this);
/*      */     }
/*  854 */     public Kind getKind() { return Kind.VARIABLE; }
/*  855 */     public JCModifiers getModifiers() { return this.mods; }
/*  856 */     public Name getName() { return this.name; }
/*  857 */     public JCExpression getNameExpression() { return this.nameexpr; } public JCTree getType() {
/*  858 */       return this.vartype;
/*      */     } public JCExpression getInitializer() {
/*  860 */       return this.init;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  864 */       return (R)param1TreeVisitor.visitVariable(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  869 */       return Tag.VARDEF;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static class JCSkip
/*      */     extends JCStatement
/*      */     implements EmptyStatementTree
/*      */   {
/*      */     public void accept(Visitor param1Visitor) {
/*  880 */       param1Visitor.visitSkip(this);
/*      */     } public Kind getKind() {
/*  882 */       return Kind.EMPTY_STATEMENT;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  885 */       return (R)param1TreeVisitor.visitEmptyStatement(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  890 */       return Tag.SKIP;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCBlock
/*      */     extends JCStatement
/*      */     implements BlockTree
/*      */   {
/*      */     public long flags;
/*      */
/*      */     public List<JCStatement> stats;
/*      */
/*  903 */     public int endpos = -1;
/*      */     protected JCBlock(long param1Long, List<JCStatement> param1List) {
/*  905 */       this.stats = param1List;
/*  906 */       this.flags = param1Long;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/*  909 */       param1Visitor.visitBlock(this);
/*      */     } public Kind getKind() {
/*  911 */       return Kind.BLOCK;
/*      */     } public List<JCStatement> getStatements() {
/*  913 */       return this.stats;
/*      */     } public boolean isStatic() {
/*  915 */       return ((this.flags & 0x8L) != 0L);
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  918 */       return (R)param1TreeVisitor.visitBlock(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  923 */       return Tag.BLOCK;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCDoWhileLoop
/*      */     extends JCStatement
/*      */     implements DoWhileLoopTree {
/*      */     public JCStatement body;
/*      */     public JCExpression cond;
/*      */
/*      */     protected JCDoWhileLoop(JCStatement param1JCStatement, JCExpression param1JCExpression) {
/*  934 */       this.body = param1JCStatement;
/*  935 */       this.cond = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/*  938 */       param1Visitor.visitDoLoop(this);
/*      */     }
/*  940 */     public Kind getKind() { return Kind.DO_WHILE_LOOP; }
/*  941 */     public JCExpression getCondition() { return this.cond; } public JCStatement getStatement() {
/*  942 */       return this.body;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  945 */       return (R)param1TreeVisitor.visitDoWhileLoop(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  950 */       return Tag.DOLOOP;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCWhileLoop
/*      */     extends JCStatement
/*      */     implements WhileLoopTree {
/*      */     public JCExpression cond;
/*      */     public JCStatement body;
/*      */
/*      */     protected JCWhileLoop(JCExpression param1JCExpression, JCStatement param1JCStatement) {
/*  961 */       this.cond = param1JCExpression;
/*  962 */       this.body = param1JCStatement;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/*  965 */       param1Visitor.visitWhileLoop(this);
/*      */     }
/*  967 */     public Kind getKind() { return Kind.WHILE_LOOP; }
/*  968 */     public JCExpression getCondition() { return this.cond; } public JCStatement getStatement() {
/*  969 */       return this.body;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/*  972 */       return (R)param1TreeVisitor.visitWhileLoop(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/*  977 */       return Tag.WHILELOOP;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCForLoop
/*      */     extends JCStatement
/*      */     implements ForLoopTree
/*      */   {
/*      */     public List<JCStatement> init;
/*      */
/*      */     public JCExpression cond;
/*      */
/*      */     public List<JCExpressionStatement> step;
/*      */     public JCStatement body;
/*      */
/*      */     protected JCForLoop(List<JCStatement> param1List, JCExpression param1JCExpression, List<JCExpressionStatement> param1List1, JCStatement param1JCStatement) {
/*  994 */       this.init = param1List;
/*  995 */       this.cond = param1JCExpression;
/*  996 */       this.step = param1List1;
/*  997 */       this.body = param1JCStatement;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1000 */       param1Visitor.visitForLoop(this);
/*      */     }
/* 1002 */     public Kind getKind() { return Kind.FOR_LOOP; }
/* 1003 */     public JCExpression getCondition() { return this.cond; } public JCStatement getStatement() {
/* 1004 */       return this.body;
/*      */     } public List<JCStatement> getInitializer() {
/* 1006 */       return this.init;
/*      */     }
/*      */     public List<JCExpressionStatement> getUpdate() {
/* 1009 */       return this.step;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1013 */       return (R)param1TreeVisitor.visitForLoop(this, param1D);
/*      */     }
/*      */
/*      */
/*      */     public Tag getTag() {
/* 1018 */       return Tag.FORLOOP;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCEnhancedForLoop
/*      */     extends JCStatement
/*      */     implements EnhancedForLoopTree {
/*      */     public JCVariableDecl var;
/*      */     public JCExpression expr;
/*      */     public JCStatement body;
/*      */
/*      */     protected JCEnhancedForLoop(JCVariableDecl param1JCVariableDecl, JCExpression param1JCExpression, JCStatement param1JCStatement) {
/* 1030 */       this.var = param1JCVariableDecl;
/* 1031 */       this.expr = param1JCExpression;
/* 1032 */       this.body = param1JCStatement;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1035 */       param1Visitor.visitForeachLoop(this);
/*      */     }
/* 1037 */     public Kind getKind() { return Kind.ENHANCED_FOR_LOOP; }
/* 1038 */     public JCVariableDecl getVariable() { return this.var; }
/* 1039 */     public JCExpression getExpression() { return this.expr; } public JCStatement getStatement() {
/* 1040 */       return this.body;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1043 */       return (R)param1TreeVisitor.visitEnhancedForLoop(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1047 */       return Tag.FOREACHLOOP;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCLabeledStatement
/*      */     extends JCStatement
/*      */     implements LabeledStatementTree {
/*      */     public Name label;
/*      */     public JCStatement body;
/*      */
/*      */     protected JCLabeledStatement(Name param1Name, JCStatement param1JCStatement) {
/* 1058 */       this.label = param1Name;
/* 1059 */       this.body = param1JCStatement;
/*      */     }
/*      */
/* 1062 */     public void accept(Visitor param1Visitor) { param1Visitor.visitLabelled(this); }
/* 1063 */     public Kind getKind() { return Kind.LABELED_STATEMENT; }
/* 1064 */     public Name getLabel() { return this.label; } public JCStatement getStatement() {
/* 1065 */       return this.body;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1068 */       return (R)param1TreeVisitor.visitLabeledStatement(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1072 */       return Tag.LABELLED;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCSwitch
/*      */     extends JCStatement
/*      */     implements SwitchTree {
/*      */     public JCExpression selector;
/*      */     public List<JCCase> cases;
/*      */
/*      */     protected JCSwitch(JCExpression param1JCExpression, List<JCCase> param1List) {
/* 1083 */       this.selector = param1JCExpression;
/* 1084 */       this.cases = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1087 */       param1Visitor.visitSwitch(this);
/*      */     }
/* 1089 */     public Kind getKind() { return Kind.SWITCH; }
/* 1090 */     public JCExpression getExpression() { return this.selector; } public List<JCCase> getCases() {
/* 1091 */       return this.cases;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1094 */       return (R)param1TreeVisitor.visitSwitch(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1098 */       return Tag.SWITCH;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCCase
/*      */     extends JCStatement
/*      */     implements CaseTree {
/*      */     public JCExpression pat;
/*      */     public List<JCStatement> stats;
/*      */
/*      */     protected JCCase(JCExpression param1JCExpression, List<JCStatement> param1List) {
/* 1109 */       this.pat = param1JCExpression;
/* 1110 */       this.stats = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1113 */       param1Visitor.visitCase(this);
/*      */     }
/* 1115 */     public Kind getKind() { return Kind.CASE; }
/* 1116 */     public JCExpression getExpression() { return this.pat; } public List<JCStatement> getStatements() {
/* 1117 */       return this.stats;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1120 */       return (R)param1TreeVisitor.visitCase(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1124 */       return Tag.CASE;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCSynchronized
/*      */     extends JCStatement
/*      */     implements SynchronizedTree {
/*      */     public JCExpression lock;
/*      */     public JCBlock body;
/*      */
/*      */     protected JCSynchronized(JCExpression param1JCExpression, JCBlock param1JCBlock) {
/* 1135 */       this.lock = param1JCExpression;
/* 1136 */       this.body = param1JCBlock;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1139 */       param1Visitor.visitSynchronized(this);
/*      */     }
/* 1141 */     public Kind getKind() { return Kind.SYNCHRONIZED; }
/* 1142 */     public JCExpression getExpression() { return this.lock; } public JCBlock getBlock() {
/* 1143 */       return this.body;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1146 */       return (R)param1TreeVisitor.visitSynchronized(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1150 */       return Tag.SYNCHRONIZED;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCTry
/*      */     extends JCStatement
/*      */     implements TryTree
/*      */   {
/*      */     public JCBlock body;
/*      */
/*      */     public List<JCCatch> catchers;
/*      */     public JCBlock finalizer;
/*      */     public List<JCTree> resources;
/*      */     public boolean finallyCanCompleteNormally;
/*      */
/*      */     protected JCTry(List<JCTree> param1List, JCBlock param1JCBlock1, List<JCCatch> param1List1, JCBlock param1JCBlock2) {
/* 1167 */       this.body = param1JCBlock1;
/* 1168 */       this.catchers = param1List1;
/* 1169 */       this.finalizer = param1JCBlock2;
/* 1170 */       this.resources = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1173 */       param1Visitor.visitTry(this);
/*      */     }
/* 1175 */     public Kind getKind() { return Kind.TRY; } public JCBlock getBlock() {
/* 1176 */       return this.body;
/*      */     } public List<JCCatch> getCatches() {
/* 1178 */       return this.catchers;
/*      */     } public JCBlock getFinallyBlock() {
/* 1180 */       return this.finalizer;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1183 */       return (R)param1TreeVisitor.visitTry(this, param1D);
/*      */     }
/*      */
/*      */     public List<JCTree> getResources() {
/* 1187 */       return this.resources;
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1191 */       return Tag.TRY;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCCatch
/*      */     extends JCTree
/*      */     implements CatchTree {
/*      */     public JCVariableDecl param;
/*      */     public JCBlock body;
/*      */
/*      */     protected JCCatch(JCVariableDecl param1JCVariableDecl, JCBlock param1JCBlock) {
/* 1202 */       this.param = param1JCVariableDecl;
/* 1203 */       this.body = param1JCBlock;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1206 */       param1Visitor.visitCatch(this);
/*      */     }
/* 1208 */     public Kind getKind() { return Kind.CATCH; }
/* 1209 */     public JCVariableDecl getParameter() { return this.param; } public JCBlock getBlock() {
/* 1210 */       return this.body;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1213 */       return (R)param1TreeVisitor.visitCatch(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1217 */       return Tag.CATCH;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCConditional
/*      */     extends JCPolyExpression
/*      */     implements ConditionalExpressionTree
/*      */   {
/*      */     public JCExpression cond;
/*      */
/*      */     public JCExpression truepart;
/*      */     public JCExpression falsepart;
/*      */
/*      */     protected JCConditional(JCExpression param1JCExpression1, JCExpression param1JCExpression2, JCExpression param1JCExpression3) {
/* 1232 */       this.cond = param1JCExpression1;
/* 1233 */       this.truepart = param1JCExpression2;
/* 1234 */       this.falsepart = param1JCExpression3;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1237 */       param1Visitor.visitConditional(this);
/*      */     }
/* 1239 */     public Kind getKind() { return Kind.CONDITIONAL_EXPRESSION; }
/* 1240 */     public JCExpression getCondition() { return this.cond; }
/* 1241 */     public JCExpression getTrueExpression() { return this.truepart; } public JCExpression getFalseExpression() {
/* 1242 */       return this.falsepart;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1245 */       return (R)param1TreeVisitor.visitConditionalExpression(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1249 */       return Tag.CONDEXPR;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCIf
/*      */     extends JCStatement
/*      */     implements IfTree
/*      */   {
/*      */     public JCExpression cond;
/*      */
/*      */     public JCStatement thenpart;
/*      */     public JCStatement elsepart;
/*      */
/*      */     protected JCIf(JCExpression param1JCExpression, JCStatement param1JCStatement1, JCStatement param1JCStatement2) {
/* 1264 */       this.cond = param1JCExpression;
/* 1265 */       this.thenpart = param1JCStatement1;
/* 1266 */       this.elsepart = param1JCStatement2;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1269 */       param1Visitor.visitIf(this);
/*      */     }
/* 1271 */     public Kind getKind() { return Kind.IF; }
/* 1272 */     public JCExpression getCondition() { return this.cond; }
/* 1273 */     public JCStatement getThenStatement() { return this.thenpart; } public JCStatement getElseStatement() {
/* 1274 */       return this.elsepart;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1277 */       return (R)param1TreeVisitor.visitIf(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1281 */       return Tag.IF;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCExpressionStatement
/*      */     extends JCStatement
/*      */     implements ExpressionStatementTree
/*      */   {
/*      */     public JCExpression expr;
/*      */
/*      */     protected JCExpressionStatement(JCExpression param1JCExpression) {
/* 1293 */       this.expr = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1296 */       param1Visitor.visitExec(this);
/*      */     }
/* 1298 */     public Kind getKind() { return Kind.EXPRESSION_STATEMENT; } public JCExpression getExpression() {
/* 1299 */       return this.expr;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1302 */       return (R)param1TreeVisitor.visitExpressionStatement(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1306 */       return Tag.EXEC;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public String toString() {
/* 1312 */       StringWriter stringWriter = new StringWriter();
/*      */       try {
/* 1314 */         (new Pretty(stringWriter, false)).printStat(this);
/*      */       }
/* 1316 */       catch (IOException iOException) {
/*      */
/*      */
/* 1319 */         throw new AssertionError(iOException);
/*      */       }
/* 1321 */       return stringWriter.toString();
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCBreak
/*      */     extends JCStatement
/*      */     implements BreakTree {
/*      */     public Name label;
/*      */     public JCTree target;
/*      */
/*      */     protected JCBreak(Name param1Name, JCTree param1JCTree) {
/* 1332 */       this.label = param1Name;
/* 1333 */       this.target = param1JCTree;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1336 */       param1Visitor.visitBreak(this);
/*      */     }
/* 1338 */     public Kind getKind() { return Kind.BREAK; } public Name getLabel() {
/* 1339 */       return this.label;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1342 */       return (R)param1TreeVisitor.visitBreak(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1346 */       return Tag.BREAK;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCContinue
/*      */     extends JCStatement
/*      */     implements ContinueTree {
/*      */     public Name label;
/*      */     public JCTree target;
/*      */
/*      */     protected JCContinue(Name param1Name, JCTree param1JCTree) {
/* 1357 */       this.label = param1Name;
/* 1358 */       this.target = param1JCTree;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1361 */       param1Visitor.visitContinue(this);
/*      */     }
/* 1363 */     public Kind getKind() { return Kind.CONTINUE; } public Name getLabel() {
/* 1364 */       return this.label;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1367 */       return (R)param1TreeVisitor.visitContinue(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1371 */       return Tag.CONTINUE;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCReturn
/*      */     extends JCStatement
/*      */     implements ReturnTree {
/*      */     public JCExpression expr;
/*      */
/*      */     protected JCReturn(JCExpression param1JCExpression) {
/* 1381 */       this.expr = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1384 */       param1Visitor.visitReturn(this);
/*      */     }
/* 1386 */     public Kind getKind() { return Kind.RETURN; } public JCExpression getExpression() {
/* 1387 */       return this.expr;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1390 */       return (R)param1TreeVisitor.visitReturn(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1394 */       return Tag.RETURN;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCThrow
/*      */     extends JCStatement
/*      */     implements ThrowTree {
/*      */     public JCExpression expr;
/*      */
/*      */     protected JCThrow(JCExpression param1JCExpression) {
/* 1404 */       this.expr = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1407 */       param1Visitor.visitThrow(this);
/*      */     }
/* 1409 */     public Kind getKind() { return Kind.THROW; } public JCExpression getExpression() {
/* 1410 */       return this.expr;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1413 */       return (R)param1TreeVisitor.visitThrow(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1417 */       return Tag.THROW;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCAssert
/*      */     extends JCStatement
/*      */     implements AssertTree {
/*      */     public JCExpression cond;
/*      */     public JCExpression detail;
/*      */
/*      */     protected JCAssert(JCExpression param1JCExpression1, JCExpression param1JCExpression2) {
/* 1428 */       this.cond = param1JCExpression1;
/* 1429 */       this.detail = param1JCExpression2;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1432 */       param1Visitor.visitAssert(this);
/*      */     }
/* 1434 */     public Kind getKind() { return Kind.ASSERT; }
/* 1435 */     public JCExpression getCondition() { return this.cond; } public JCExpression getDetail() {
/* 1436 */       return this.detail;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1439 */       return (R)param1TreeVisitor.visitAssert(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1443 */       return Tag.ASSERT;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCMethodInvocation
/*      */     extends JCPolyExpression
/*      */     implements MethodInvocationTree
/*      */   {
/*      */     public List<JCExpression> typeargs;
/*      */
/*      */     public JCExpression meth;
/*      */     public List<JCExpression> args;
/*      */     public Type varargsElement;
/*      */
/*      */     protected JCMethodInvocation(List<JCExpression> param1List1, JCExpression param1JCExpression, List<JCExpression> param1List2) {
/* 1459 */       this.typeargs = (param1List1 == null) ? List.nil() : param1List1;
/*      */
/* 1461 */       this.meth = param1JCExpression;
/* 1462 */       this.args = param1List2;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1465 */       param1Visitor.visitApply(this);
/*      */     } public Kind getKind() {
/* 1467 */       return Kind.METHOD_INVOCATION;
/*      */     } public List<JCExpression> getTypeArguments() {
/* 1469 */       return this.typeargs;
/*      */     } public JCExpression getMethodSelect() {
/* 1471 */       return this.meth;
/*      */     } public List<JCExpression> getArguments() {
/* 1473 */       return this.args;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1477 */       return (R)param1TreeVisitor.visitMethodInvocation(this, param1D);
/*      */     }
/*      */
/*      */     public JCMethodInvocation setType(Type param1Type) {
/* 1481 */       super.setType(param1Type);
/* 1482 */       return this;
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1486 */       return Tag.APPLY;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCNewClass
/*      */     extends JCPolyExpression
/*      */     implements NewClassTree
/*      */   {
/*      */     public JCExpression encl;
/*      */
/*      */     public List<JCExpression> typeargs;
/*      */
/*      */     public JCExpression clazz;
/*      */
/*      */     public List<JCExpression> args;
/*      */     public JCClassDecl def;
/*      */     public Symbol constructor;
/*      */     public Type varargsElement;
/*      */     public Type constructorType;
/*      */
/*      */     protected JCNewClass(JCExpression param1JCExpression1, List<JCExpression> param1List1, JCExpression param1JCExpression2, List<JCExpression> param1List2, JCClassDecl param1JCClassDecl) {
/* 1508 */       this.encl = param1JCExpression1;
/* 1509 */       this.typeargs = (param1List1 == null) ? List.nil() : param1List1;
/*      */
/* 1511 */       this.clazz = param1JCExpression2;
/* 1512 */       this.args = param1List2;
/* 1513 */       this.def = param1JCClassDecl;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1516 */       param1Visitor.visitNewClass(this);
/*      */     } public Kind getKind() {
/* 1518 */       return Kind.NEW_CLASS;
/*      */     } public JCExpression getEnclosingExpression() {
/* 1520 */       return this.encl;
/*      */     }
/*      */     public List<JCExpression> getTypeArguments() {
/* 1523 */       return this.typeargs;
/*      */     } public JCExpression getIdentifier() {
/* 1525 */       return this.clazz;
/*      */     } public List<JCExpression> getArguments() {
/* 1527 */       return this.args;
/*      */     } public JCClassDecl getClassBody() {
/* 1529 */       return this.def;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1532 */       return (R)param1TreeVisitor.visitNewClass(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1536 */       return Tag.NEWCLASS;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCNewArray
/*      */     extends JCExpression
/*      */     implements NewArrayTree
/*      */   {
/*      */     public JCExpression elemtype;
/*      */
/*      */     public List<JCExpression> dims;
/*      */
/*      */     public List<JCAnnotation> annotations;
/*      */
/*      */     public List<List<JCAnnotation>> dimAnnotations;
/*      */     public List<JCExpression> elems;
/*      */
/*      */     protected JCNewArray(JCExpression param1JCExpression, List<JCExpression> param1List1, List<JCExpression> param1List2) {
/* 1555 */       this.elemtype = param1JCExpression;
/* 1556 */       this.dims = param1List1;
/* 1557 */       this.annotations = List.nil();
/* 1558 */       this.dimAnnotations = List.nil();
/* 1559 */       this.elems = param1List2;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1562 */       param1Visitor.visitNewArray(this);
/*      */     }
/* 1564 */     public Kind getKind() { return Kind.NEW_ARRAY; } public JCExpression getType() {
/* 1565 */       return this.elemtype;
/*      */     } public List<JCExpression> getDimensions() {
/* 1567 */       return this.dims;
/*      */     }
/*      */     public List<JCExpression> getInitializers() {
/* 1570 */       return this.elems;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1574 */       return (R)param1TreeVisitor.visitNewArray(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1578 */       return Tag.NEWARRAY;
/*      */     }
/*      */
/*      */
/*      */     public List<JCAnnotation> getAnnotations() {
/* 1583 */       return this.annotations;
/*      */     }
/*      */
/*      */
/*      */     public List<List<JCAnnotation>> getDimAnnotations() {
/* 1588 */       return this.dimAnnotations;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCLambda
/*      */     extends JCFunctionalExpression implements LambdaExpressionTree {
/*      */     public List<JCVariableDecl> params;
/*      */     public JCTree body;
/*      */
/*      */     public enum ParameterKind {
/* 1598 */       IMPLICIT,
/* 1599 */       EXPLICIT;
/*      */     }
/*      */
/*      */
/*      */     public boolean canCompleteNormally = true;
/*      */
/*      */     public ParameterKind paramKind;
/*      */
/*      */
/*      */     public JCLambda(List<JCVariableDecl> param1List, JCTree param1JCTree) {
/* 1609 */       this.params = param1List;
/* 1610 */       this.body = param1JCTree;
/* 1611 */       if (param1List.isEmpty() || ((JCVariableDecl)param1List.head).vartype != null) {
/*      */
/* 1613 */         this.paramKind = ParameterKind.EXPLICIT;
/*      */       } else {
/* 1615 */         this.paramKind = ParameterKind.IMPLICIT;
/*      */       }
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1620 */       return Tag.LAMBDA;
/*      */     }
/*      */
/*      */     public void accept(Visitor param1Visitor) {
/* 1624 */       param1Visitor.visitLambda(this);
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1628 */       return (R)param1TreeVisitor.visitLambdaExpression(this, param1D);
/*      */     }
/*      */     public Kind getKind() {
/* 1631 */       return Kind.LAMBDA_EXPRESSION;
/*      */     }
/*      */     public JCTree getBody() {
/* 1634 */       return this.body;
/*      */     }
/*      */     public List<? extends VariableTree> getParameters() {
/* 1637 */       return (List)this.params;
/*      */     }
/*      */
/*      */     public JCLambda setType(Type param1Type) {
/* 1641 */       super.setType(param1Type);
/* 1642 */       return this;
/*      */     }
/*      */
/*      */     public BodyKind getBodyKind() {
/* 1646 */       return this.body.hasTag(Tag.BLOCK) ? BodyKind.STATEMENT : BodyKind.EXPRESSION;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCParens
/*      */     extends JCExpression
/*      */     implements ParenthesizedTree
/*      */   {
/*      */     public JCExpression expr;
/*      */
/*      */     protected JCParens(JCExpression param1JCExpression) {
/* 1658 */       this.expr = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1661 */       param1Visitor.visitParens(this);
/*      */     }
/* 1663 */     public Kind getKind() { return Kind.PARENTHESIZED; } public JCExpression getExpression() {
/* 1664 */       return this.expr;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1667 */       return (R)param1TreeVisitor.visitParenthesized(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1671 */       return Tag.PARENS;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCAssign
/*      */     extends JCExpression
/*      */     implements AssignmentTree {
/*      */     public JCExpression lhs;
/*      */     public JCExpression rhs;
/*      */
/*      */     protected JCAssign(JCExpression param1JCExpression1, JCExpression param1JCExpression2) {
/* 1682 */       this.lhs = param1JCExpression1;
/* 1683 */       this.rhs = param1JCExpression2;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1686 */       param1Visitor.visitAssign(this);
/*      */     }
/* 1688 */     public Kind getKind() { return Kind.ASSIGNMENT; }
/* 1689 */     public JCExpression getVariable() { return this.lhs; } public JCExpression getExpression() {
/* 1690 */       return this.rhs;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1693 */       return (R)param1TreeVisitor.visitAssignment(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1697 */       return Tag.ASSIGN;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCAssignOp
/*      */     extends JCExpression
/*      */     implements CompoundAssignmentTree {
/*      */     private Tag opcode;
/*      */     public JCExpression lhs;
/*      */     public JCExpression rhs;
/*      */     public Symbol operator;
/*      */
/*      */     protected JCAssignOp(Tag param1Tag, JCTree param1JCTree1, JCTree param1JCTree2, Symbol param1Symbol) {
/* 1710 */       this.opcode = param1Tag;
/* 1711 */       this.lhs = (JCExpression)param1JCTree1;
/* 1712 */       this.rhs = (JCExpression)param1JCTree2;
/* 1713 */       this.operator = param1Symbol;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1716 */       param1Visitor.visitAssignop(this);
/*      */     }
/* 1718 */     public Kind getKind() { return TreeInfo.tagToKind(getTag()); }
/* 1719 */     public JCExpression getVariable() { return this.lhs; } public JCExpression getExpression() {
/* 1720 */       return this.rhs;
/*      */     } public Symbol getOperator() {
/* 1722 */       return this.operator;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1726 */       return (R)param1TreeVisitor.visitCompoundAssignment(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1730 */       return this.opcode;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCUnary
/*      */     extends JCExpression
/*      */     implements UnaryTree {
/*      */     private Tag opcode;
/*      */     public JCExpression arg;
/*      */     public Symbol operator;
/*      */
/*      */     protected JCUnary(Tag param1Tag, JCExpression param1JCExpression) {
/* 1742 */       this.opcode = param1Tag;
/* 1743 */       this.arg = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1746 */       param1Visitor.visitUnary(this);
/*      */     }
/* 1748 */     public Kind getKind() { return TreeInfo.tagToKind(getTag()); } public JCExpression getExpression() {
/* 1749 */       return this.arg;
/*      */     } public Symbol getOperator() {
/* 1751 */       return this.operator;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1755 */       return (R)param1TreeVisitor.visitUnary(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1759 */       return this.opcode;
/*      */     }
/*      */
/*      */     public void setTag(Tag param1Tag) {
/* 1763 */       this.opcode = param1Tag;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCBinary
/*      */     extends JCExpression
/*      */     implements BinaryTree
/*      */   {
/*      */     private Tag opcode;
/*      */
/*      */     public JCExpression lhs;
/*      */     public JCExpression rhs;
/*      */     public Symbol operator;
/*      */
/*      */     protected JCBinary(Tag param1Tag, JCExpression param1JCExpression1, JCExpression param1JCExpression2, Symbol param1Symbol) {
/* 1779 */       this.opcode = param1Tag;
/* 1780 */       this.lhs = param1JCExpression1;
/* 1781 */       this.rhs = param1JCExpression2;
/* 1782 */       this.operator = param1Symbol;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1785 */       param1Visitor.visitBinary(this);
/*      */     }
/* 1787 */     public Kind getKind() { return TreeInfo.tagToKind(getTag()); }
/* 1788 */     public JCExpression getLeftOperand() { return this.lhs; } public JCExpression getRightOperand() {
/* 1789 */       return this.rhs;
/*      */     } public Symbol getOperator() {
/* 1791 */       return this.operator;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1795 */       return (R)param1TreeVisitor.visitBinary(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1799 */       return this.opcode;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCTypeCast
/*      */     extends JCExpression
/*      */     implements TypeCastTree {
/*      */     public JCTree clazz;
/*      */     public JCExpression expr;
/*      */
/*      */     protected JCTypeCast(JCTree param1JCTree, JCExpression param1JCExpression) {
/* 1810 */       this.clazz = param1JCTree;
/* 1811 */       this.expr = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1814 */       param1Visitor.visitTypeCast(this);
/*      */     }
/* 1816 */     public Kind getKind() { return Kind.TYPE_CAST; }
/* 1817 */     public JCTree getType() { return this.clazz; } public JCExpression getExpression() {
/* 1818 */       return this.expr;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1821 */       return (R)param1TreeVisitor.visitTypeCast(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1825 */       return Tag.TYPECAST;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCInstanceOf
/*      */     extends JCExpression
/*      */     implements InstanceOfTree {
/*      */     public JCExpression expr;
/*      */     public JCTree clazz;
/*      */
/*      */     protected JCInstanceOf(JCExpression param1JCExpression, JCTree param1JCTree) {
/* 1836 */       this.expr = param1JCExpression;
/* 1837 */       this.clazz = param1JCTree;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1840 */       param1Visitor.visitTypeTest(this);
/*      */     }
/* 1842 */     public Kind getKind() { return Kind.INSTANCE_OF; }
/* 1843 */     public JCTree getType() { return this.clazz; } public JCExpression getExpression() {
/* 1844 */       return this.expr;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1847 */       return (R)param1TreeVisitor.visitInstanceOf(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1851 */       return Tag.TYPETEST;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCArrayAccess
/*      */     extends JCExpression
/*      */     implements ArrayAccessTree {
/*      */     public JCExpression indexed;
/*      */     public JCExpression index;
/*      */
/*      */     protected JCArrayAccess(JCExpression param1JCExpression1, JCExpression param1JCExpression2) {
/* 1862 */       this.indexed = param1JCExpression1;
/* 1863 */       this.index = param1JCExpression2;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1866 */       param1Visitor.visitIndexed(this);
/*      */     }
/* 1868 */     public Kind getKind() { return Kind.ARRAY_ACCESS; }
/* 1869 */     public JCExpression getExpression() { return this.indexed; } public JCExpression getIndex() {
/* 1870 */       return this.index;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1873 */       return (R)param1TreeVisitor.visitArrayAccess(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1877 */       return Tag.INDEXED;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCFieldAccess
/*      */     extends JCExpression
/*      */     implements MemberSelectTree
/*      */   {
/*      */     public JCExpression selected;
/*      */
/*      */     public Name name;
/*      */     public Symbol sym;
/*      */
/*      */     protected JCFieldAccess(JCExpression param1JCExpression, Name param1Name, Symbol param1Symbol) {
/* 1892 */       this.selected = param1JCExpression;
/* 1893 */       this.name = param1Name;
/* 1894 */       this.sym = param1Symbol;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1897 */       param1Visitor.visitSelect(this);
/*      */     }
/* 1899 */     public Kind getKind() { return Kind.MEMBER_SELECT; } public JCExpression getExpression() {
/* 1900 */       return this.selected;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1903 */       return (R)param1TreeVisitor.visitMemberSelect(this, param1D);
/*      */     } public Name getIdentifier() {
/* 1905 */       return this.name;
/*      */     }
/*      */     public Tag getTag() {
/* 1908 */       return Tag.SELECT;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCMemberReference
/*      */     extends JCFunctionalExpression
/*      */     implements MemberReferenceTree
/*      */   {
/*      */     public ReferenceMode mode;
/*      */     public ReferenceKind kind;
/*      */     public Name name;
/*      */     public JCExpression expr;
/*      */     public List<JCExpression> typeargs;
/*      */     public Symbol sym;
/*      */     public Type varargsElement;
/*      */     public PolyKind refPolyKind;
/*      */     public boolean ownerAccessible;
/*      */     public OverloadKind overloadKind;
/*      */
/*      */     public enum OverloadKind
/*      */     {
/* 1929 */       OVERLOADED,
/* 1930 */       UNOVERLOADED;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public enum ReferenceKind
/*      */     {
/* 1939 */       SUPER((String) ReferenceMode.INVOKE, false),
/*      */
/* 1941 */       UNBOUND((String) ReferenceMode.INVOKE, true),
/*      */
/* 1943 */       STATIC((String) ReferenceMode.INVOKE, false),
/*      */
/* 1945 */       BOUND((String) ReferenceMode.INVOKE, false),
/*      */
/* 1947 */       IMPLICIT_INNER((String) ReferenceMode.NEW, false),
/*      */
/* 1949 */       TOPLEVEL((String) ReferenceMode.NEW, false),
/*      */
/* 1951 */       ARRAY_CTOR((String) ReferenceMode.NEW, false);
/*      */
/*      */       final ReferenceMode mode;
/*      */       final boolean unbound;
/*      */
/*      */       ReferenceKind(ReferenceMode param2ReferenceMode, boolean param2Boolean) {
/* 1957 */         this.mode = param2ReferenceMode;
/* 1958 */         this.unbound = param2Boolean;
/*      */       }
/*      */
/*      */       public boolean isUnbound() {
/* 1962 */         return this.unbound;
/*      */       }
/*      */     }
/*      */
/*      */     protected JCMemberReference(ReferenceMode param1ReferenceMode, Name param1Name, JCExpression param1JCExpression, List<JCExpression> param1List) {
/* 1967 */       this.mode = param1ReferenceMode;
/* 1968 */       this.name = param1Name;
/* 1969 */       this.expr = param1JCExpression;
/* 1970 */       this.typeargs = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 1973 */       param1Visitor.visitReference(this);
/*      */     } public Kind getKind() {
/* 1975 */       return Kind.MEMBER_REFERENCE;
/*      */     } public ReferenceMode getMode() {
/* 1977 */       return this.mode;
/*      */     } public JCExpression getQualifierExpression() {
/* 1979 */       return this.expr;
/*      */     } public Name getName() {
/* 1981 */       return this.name;
/*      */     } public List<JCExpression> getTypeArguments() {
/* 1983 */       return this.typeargs;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 1987 */       return (R)param1TreeVisitor.visitMemberReference(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 1991 */       return Tag.REFERENCE;
/*      */     }
/*      */     public boolean hasKind(ReferenceKind param1ReferenceKind) {
/* 1994 */       return (this.kind == param1ReferenceKind);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCIdent
/*      */     extends JCExpression
/*      */     implements IdentifierTree
/*      */   {
/*      */     public Name name;
/*      */     public Symbol sym;
/*      */
/*      */     protected JCIdent(Name param1Name, Symbol param1Symbol) {
/* 2007 */       this.name = param1Name;
/* 2008 */       this.sym = param1Symbol;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2011 */       param1Visitor.visitIdent(this);
/*      */     }
/* 2013 */     public Kind getKind() { return Kind.IDENTIFIER; } public Name getName() {
/* 2014 */       return this.name;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2017 */       return (R)param1TreeVisitor.visitIdentifier(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2021 */       return Tag.IDENT;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCLiteral
/*      */     extends JCExpression
/*      */     implements LiteralTree
/*      */   {
/*      */     public TypeTag typetag;
/*      */     public Object value;
/*      */
/*      */     protected JCLiteral(TypeTag param1TypeTag, Object param1Object) {
/* 2033 */       this.typetag = param1TypeTag;
/* 2034 */       this.value = param1Object;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2037 */       param1Visitor.visitLiteral(this);
/*      */     }
/*      */     public Kind getKind() {
/* 2040 */       return this.typetag.getKindLiteral();
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public Object getValue() {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/tree/JCTree$1.$SwitchMap$com$sun$tools$javac$code$TypeTag : [I
/*      */       //   3: aload_0
/*      */       //   4: getfield typetag : Lcom/sun/tools/javac/code/TypeTag;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: lookupswitch default -> 94, 1 -> 36, 2 -> 60
/*      */       //   36: aload_0
/*      */       //   37: getfield value : Ljava/lang/Object;
/*      */       //   40: checkcast java/lang/Integer
/*      */       //   43: invokevirtual intValue : ()I
/*      */       //   46: istore_1
/*      */       //   47: iload_1
/*      */       //   48: ifeq -> 55
/*      */       //   51: iconst_1
/*      */       //   52: goto -> 56
/*      */       //   55: iconst_0
/*      */       //   56: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*      */       //   59: areturn
/*      */       //   60: aload_0
/*      */       //   61: getfield value : Ljava/lang/Object;
/*      */       //   64: checkcast java/lang/Integer
/*      */       //   67: invokevirtual intValue : ()I
/*      */       //   70: istore_2
/*      */       //   71: iload_2
/*      */       //   72: i2c
/*      */       //   73: istore_3
/*      */       //   74: iload_3
/*      */       //   75: iload_2
/*      */       //   76: if_icmpeq -> 89
/*      */       //   79: new java/lang/AssertionError
/*      */       //   82: dup
/*      */       //   83: ldc 'bad value for char literal'
/*      */       //   85: invokespecial <init> : (Ljava/lang/Object;)V
/*      */       //   88: athrow
/*      */       //   89: iload_3
/*      */       //   90: invokestatic valueOf : (C)Ljava/lang/Character;
/*      */       //   93: areturn
/*      */       //   94: aload_0
/*      */       //   95: getfield value : Ljava/lang/Object;
/*      */       //   98: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2044	-> 0
/*      */       //   #2046	-> 36
/*      */       //   #2047	-> 47
/*      */       //   #2049	-> 60
/*      */       //   #2050	-> 71
/*      */       //   #2051	-> 74
/*      */       //   #2052	-> 79
/*      */       //   #2053	-> 89
/*      */       //   #2055	-> 94
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2060 */       return (R)param1TreeVisitor.visitLiteral(this, param1D);
/*      */     }
/*      */
/*      */     public JCLiteral setType(Type param1Type) {
/* 2064 */       super.setType(param1Type);
/* 2065 */       return this;
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2069 */       return Tag.LITERAL;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCPrimitiveTypeTree
/*      */     extends JCExpression
/*      */     implements PrimitiveTypeTree
/*      */   {
/*      */     public TypeTag typetag;
/*      */
/*      */     protected JCPrimitiveTypeTree(TypeTag param1TypeTag) {
/* 2081 */       this.typetag = param1TypeTag;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2084 */       param1Visitor.visitTypeIdent(this);
/*      */     } public Kind getKind() {
/* 2086 */       return Kind.PRIMITIVE_TYPE;
/*      */     } public TypeKind getPrimitiveTypeKind() {
/* 2088 */       return this.typetag.getPrimitiveTypeKind();
/*      */     }
/*      */
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2093 */       return (R)param1TreeVisitor.visitPrimitiveType(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2097 */       return Tag.TYPEIDENT;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCArrayTypeTree
/*      */     extends JCExpression
/*      */     implements ArrayTypeTree {
/*      */     public JCExpression elemtype;
/*      */
/*      */     protected JCArrayTypeTree(JCExpression param1JCExpression) {
/* 2107 */       this.elemtype = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2110 */       param1Visitor.visitTypeArray(this);
/*      */     }
/* 2112 */     public Kind getKind() { return Kind.ARRAY_TYPE; } public JCTree getType() {
/* 2113 */       return this.elemtype;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2116 */       return (R)param1TreeVisitor.visitArrayType(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2120 */       return Tag.TYPEARRAY;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCTypeApply
/*      */     extends JCExpression
/*      */     implements ParameterizedTypeTree {
/*      */     public JCExpression clazz;
/*      */     public List<JCExpression> arguments;
/*      */
/*      */     protected JCTypeApply(JCExpression param1JCExpression, List<JCExpression> param1List) {
/* 2131 */       this.clazz = param1JCExpression;
/* 2132 */       this.arguments = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2135 */       param1Visitor.visitTypeApply(this);
/*      */     }
/* 2137 */     public Kind getKind() { return Kind.PARAMETERIZED_TYPE; } public JCTree getType() {
/* 2138 */       return this.clazz;
/*      */     } public List<JCExpression> getTypeArguments() {
/* 2140 */       return this.arguments;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2144 */       return (R)param1TreeVisitor.visitParameterizedType(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2148 */       return Tag.TYPEAPPLY;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCTypeUnion
/*      */     extends JCExpression
/*      */     implements UnionTypeTree
/*      */   {
/*      */     public List<JCExpression> alternatives;
/*      */
/*      */     protected JCTypeUnion(List<JCExpression> param1List) {
/* 2160 */       this.alternatives = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2163 */       param1Visitor.visitTypeUnion(this);
/*      */     } public Kind getKind() {
/* 2165 */       return Kind.UNION_TYPE;
/*      */     }
/*      */     public List<JCExpression> getTypeAlternatives() {
/* 2168 */       return this.alternatives;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2172 */       return (R)param1TreeVisitor.visitUnionType(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2176 */       return Tag.TYPEUNION;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCTypeIntersection
/*      */     extends JCExpression
/*      */     implements IntersectionTypeTree
/*      */   {
/*      */     public List<JCExpression> bounds;
/*      */
/*      */     protected JCTypeIntersection(List<JCExpression> param1List) {
/* 2188 */       this.bounds = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2191 */       param1Visitor.visitTypeIntersection(this);
/*      */     } public Kind getKind() {
/* 2193 */       return Kind.INTERSECTION_TYPE;
/*      */     }
/*      */     public List<JCExpression> getBounds() {
/* 2196 */       return this.bounds;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2200 */       return (R)param1TreeVisitor.visitIntersectionType(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2204 */       return Tag.TYPEINTERSECTION;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCTypeParameter
/*      */     extends JCTree
/*      */     implements TypeParameterTree
/*      */   {
/*      */     public Name name;
/*      */
/*      */     public List<JCExpression> bounds;
/*      */     public List<JCAnnotation> annotations;
/*      */
/*      */     protected JCTypeParameter(Name param1Name, List<JCExpression> param1List, List<JCAnnotation> param1List1) {
/* 2219 */       this.name = param1Name;
/* 2220 */       this.bounds = param1List;
/* 2221 */       this.annotations = param1List1;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2224 */       param1Visitor.visitTypeParameter(this);
/*      */     }
/* 2226 */     public Kind getKind() { return Kind.TYPE_PARAMETER; } public Name getName() {
/* 2227 */       return this.name;
/*      */     } public List<JCExpression> getBounds() {
/* 2229 */       return this.bounds;
/*      */     }
/*      */     public List<JCAnnotation> getAnnotations() {
/* 2232 */       return this.annotations;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2236 */       return (R)param1TreeVisitor.visitTypeParameter(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2240 */       return Tag.TYPEPARAMETER;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCWildcard extends JCExpression implements WildcardTree { public TypeBoundKind kind;
/*      */     public JCTree inner;
/*      */
/*      */     protected JCWildcard(TypeBoundKind param1TypeBoundKind, JCTree param1JCTree) {
/* 2248 */       param1TypeBoundKind.getClass();
/* 2249 */       this.kind = param1TypeBoundKind;
/* 2250 */       this.inner = param1JCTree;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2253 */       param1Visitor.visitWildcard(this);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public Kind getKind() {
/*      */       // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/tree/JCTree$1.$SwitchMap$com$sun$tools$javac$code$BoundKind : [I
/*      */       //   3: aload_0
/*      */       //   4: getfield kind : Lcom/sun/tools/javac/tree/JCTree$TypeBoundKind;
/*      */       //   7: getfield kind : Lcom/sun/tools/javac/code/BoundKind;
/*      */       //   10: invokevirtual ordinal : ()I
/*      */       //   13: iaload
/*      */       //   14: tableswitch default -> 52, 1 -> 40, 2 -> 44, 3 -> 48
/*      */       //   40: getstatic com/sun/source/tree/Tree$Kind.UNBOUNDED_WILDCARD : Lcom/sun/source/tree/Tree$Kind;
/*      */       //   43: areturn
/*      */       //   44: getstatic com/sun/source/tree/Tree$Kind.EXTENDS_WILDCARD : Lcom/sun/source/tree/Tree$Kind;
/*      */       //   47: areturn
/*      */       //   48: getstatic com/sun/source/tree/Tree$Kind.SUPER_WILDCARD : Lcom/sun/source/tree/Tree$Kind;
/*      */       //   51: areturn
/*      */       //   52: new java/lang/AssertionError
/*      */       //   55: dup
/*      */       //   56: new java/lang/StringBuilder
/*      */       //   59: dup
/*      */       //   60: invokespecial <init> : ()V
/*      */       //   63: ldc 'Unknown wildcard bound '
/*      */       //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   68: aload_0
/*      */       //   69: getfield kind : Lcom/sun/tools/javac/tree/JCTree$TypeBoundKind;
/*      */       //   72: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   75: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   78: invokespecial <init> : (Ljava/lang/Object;)V
/*      */       //   81: athrow
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2256	-> 0
/*      */       //   #2258	-> 40
/*      */       //   #2260	-> 44
/*      */       //   #2262	-> 48
/*      */       //   #2264	-> 52
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public JCTree getBound() {
/* 2267 */       return this.inner;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2270 */       return (R)param1TreeVisitor.visitWildcard(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2274 */       return Tag.WILDCARD;
/*      */     } }
/*      */
/*      */   public static class TypeBoundKind extends JCTree {
/*      */     public BoundKind kind;
/*      */
/*      */     protected TypeBoundKind(BoundKind param1BoundKind) {
/* 2281 */       this.kind = param1BoundKind;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2284 */       param1Visitor.visitTypeBoundKind(this);
/*      */     }
/*      */     public Kind getKind() {
/* 2287 */       throw new AssertionError("TypeBoundKind is not part of a public API");
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2291 */       throw new AssertionError("TypeBoundKind is not part of a public API");
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2295 */       return Tag.TYPEBOUNDKIND;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public static class JCAnnotation
/*      */     extends JCExpression
/*      */     implements AnnotationTree
/*      */   {
/*      */     private Tag tag;
/*      */
/*      */     public JCTree annotationType;
/*      */     public List<JCExpression> args;
/*      */     public Attribute.Compound attribute;
/*      */
/*      */     protected JCAnnotation(Tag param1Tag, JCTree param1JCTree, List<JCExpression> param1List) {
/* 2311 */       this.tag = param1Tag;
/* 2312 */       this.annotationType = param1JCTree;
/* 2313 */       this.args = param1List;
/*      */     }
/*      */
/*      */     public void accept(Visitor param1Visitor) {
/* 2317 */       param1Visitor.visitAnnotation(this);
/*      */     } public Kind getKind() {
/* 2319 */       return TreeInfo.tagToKind(getTag());
/*      */     } public JCTree getAnnotationType() {
/* 2321 */       return this.annotationType;
/*      */     } public List<JCExpression> getArguments() {
/* 2323 */       return this.args;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2327 */       return (R)param1TreeVisitor.visitAnnotation(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2331 */       return this.tag;
/*      */     } }
/*      */
/*      */   public static class JCModifiers extends JCTree implements ModifiersTree {
/*      */     public long flags;
/*      */     public List<JCAnnotation> annotations;
/*      */
/*      */     protected JCModifiers(long param1Long, List<JCAnnotation> param1List) {
/* 2339 */       this.flags = param1Long;
/* 2340 */       this.annotations = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2343 */       param1Visitor.visitModifiers(this);
/*      */     } public Kind getKind() {
/* 2345 */       return Kind.MODIFIERS;
/*      */     } public Set<Modifier> getFlags() {
/* 2347 */       return Flags.asModifierSet(this.flags);
/*      */     }
/*      */     public List<JCAnnotation> getAnnotations() {
/* 2350 */       return this.annotations;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2354 */       return (R)param1TreeVisitor.visitModifiers(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2358 */       return Tag.MODIFIERS;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCAnnotatedType
/*      */     extends JCExpression implements AnnotatedTypeTree {
/*      */     public List<JCAnnotation> annotations;
/*      */     public JCExpression underlyingType;
/*      */
/*      */     protected JCAnnotatedType(List<JCAnnotation> param1List, JCExpression param1JCExpression) {
/* 2368 */       Assert.check((param1List != null && param1List.nonEmpty()));
/* 2369 */       this.annotations = param1List;
/* 2370 */       this.underlyingType = param1JCExpression;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2373 */       param1Visitor.visitAnnotatedType(this);
/*      */     } public Kind getKind() {
/* 2375 */       return Kind.ANNOTATED_TYPE;
/*      */     } public List<JCAnnotation> getAnnotations() {
/* 2377 */       return this.annotations;
/*      */     }
/*      */     public JCExpression getUnderlyingType() {
/* 2380 */       return this.underlyingType;
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2384 */       return (R)param1TreeVisitor.visitAnnotatedType(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2388 */       return Tag.ANNOTATED_TYPE;
/*      */     }
/*      */   }
/*      */
/*      */   public static class JCErroneous extends JCExpression implements ErroneousTree {
/*      */     public List<? extends JCTree> errs;
/*      */
/*      */     protected JCErroneous(List<? extends JCTree> param1List) {
/* 2396 */       this.errs = param1List;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2399 */       param1Visitor.visitErroneous(this);
/*      */     } public Kind getKind() {
/* 2401 */       return Kind.ERRONEOUS;
/*      */     }
/*      */     public List<? extends JCTree> getErrorTrees() {
/* 2404 */       return this.errs;
/*      */     }
/*      */
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2409 */       return (R)param1TreeVisitor.visitErroneous(this, param1D);
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2413 */       return Tag.ERRONEOUS;
/*      */     }
/*      */   }
/*      */
/*      */   public static class LetExpr extends JCExpression {
/*      */     public List<JCVariableDecl> defs;
/*      */     public JCTree expr;
/*      */
/*      */     protected LetExpr(List<JCVariableDecl> param1List, JCTree param1JCTree) {
/* 2422 */       this.defs = param1List;
/* 2423 */       this.expr = param1JCTree;
/*      */     }
/*      */     public void accept(Visitor param1Visitor) {
/* 2426 */       param1Visitor.visitLetExpr(this);
/*      */     }
/*      */     public Kind getKind() {
/* 2429 */       throw new AssertionError("LetExpr is not part of a public API");
/*      */     }
/*      */
/*      */     public <R, D> R accept(TreeVisitor<R, D> param1TreeVisitor, D param1D) {
/* 2433 */       throw new AssertionError("LetExpr is not part of a public API");
/*      */     }
/*      */
/*      */     public Tag getTag() {
/* 2437 */       return Tag.LETEXPR;
/*      */     }
/*      */   }
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
/*      */
/*      */
/*      */
/*      */
/*      */   public static abstract class Visitor
/*      */   {
/*      */     public void visitTopLevel(JCCompilationUnit param1JCCompilationUnit) {
/* 2533 */       visitTree(param1JCCompilationUnit);
/* 2534 */     } public void visitImport(JCImport param1JCImport) { visitTree(param1JCImport); }
/* 2535 */     public void visitClassDef(JCClassDecl param1JCClassDecl) { visitTree(param1JCClassDecl); }
/* 2536 */     public void visitMethodDef(JCMethodDecl param1JCMethodDecl) { visitTree(param1JCMethodDecl); }
/* 2537 */     public void visitVarDef(JCVariableDecl param1JCVariableDecl) { visitTree(param1JCVariableDecl); }
/* 2538 */     public void visitSkip(JCSkip param1JCSkip) { visitTree(param1JCSkip); }
/* 2539 */     public void visitBlock(JCBlock param1JCBlock) { visitTree(param1JCBlock); }
/* 2540 */     public void visitDoLoop(JCDoWhileLoop param1JCDoWhileLoop) { visitTree(param1JCDoWhileLoop); }
/* 2541 */     public void visitWhileLoop(JCWhileLoop param1JCWhileLoop) { visitTree(param1JCWhileLoop); }
/* 2542 */     public void visitForLoop(JCForLoop param1JCForLoop) { visitTree(param1JCForLoop); }
/* 2543 */     public void visitForeachLoop(JCEnhancedForLoop param1JCEnhancedForLoop) { visitTree(param1JCEnhancedForLoop); }
/* 2544 */     public void visitLabelled(JCLabeledStatement param1JCLabeledStatement) { visitTree(param1JCLabeledStatement); }
/* 2545 */     public void visitSwitch(JCSwitch param1JCSwitch) { visitTree(param1JCSwitch); }
/* 2546 */     public void visitCase(JCCase param1JCCase) { visitTree(param1JCCase); }
/* 2547 */     public void visitSynchronized(JCSynchronized param1JCSynchronized) { visitTree(param1JCSynchronized); }
/* 2548 */     public void visitTry(JCTry param1JCTry) { visitTree(param1JCTry); }
/* 2549 */     public void visitCatch(JCCatch param1JCCatch) { visitTree(param1JCCatch); }
/* 2550 */     public void visitConditional(JCConditional param1JCConditional) { visitTree(param1JCConditional); }
/* 2551 */     public void visitIf(JCIf param1JCIf) { visitTree(param1JCIf); }
/* 2552 */     public void visitExec(JCExpressionStatement param1JCExpressionStatement) { visitTree(param1JCExpressionStatement); }
/* 2553 */     public void visitBreak(JCBreak param1JCBreak) { visitTree(param1JCBreak); }
/* 2554 */     public void visitContinue(JCContinue param1JCContinue) { visitTree(param1JCContinue); }
/* 2555 */     public void visitReturn(JCReturn param1JCReturn) { visitTree(param1JCReturn); }
/* 2556 */     public void visitThrow(JCThrow param1JCThrow) { visitTree(param1JCThrow); }
/* 2557 */     public void visitAssert(JCAssert param1JCAssert) { visitTree(param1JCAssert); }
/* 2558 */     public void visitApply(JCMethodInvocation param1JCMethodInvocation) { visitTree(param1JCMethodInvocation); }
/* 2559 */     public void visitNewClass(JCNewClass param1JCNewClass) { visitTree(param1JCNewClass); }
/* 2560 */     public void visitNewArray(JCNewArray param1JCNewArray) { visitTree(param1JCNewArray); }
/* 2561 */     public void visitLambda(JCLambda param1JCLambda) { visitTree(param1JCLambda); }
/* 2562 */     public void visitParens(JCParens param1JCParens) { visitTree(param1JCParens); }
/* 2563 */     public void visitAssign(JCAssign param1JCAssign) { visitTree(param1JCAssign); }
/* 2564 */     public void visitAssignop(JCAssignOp param1JCAssignOp) { visitTree(param1JCAssignOp); }
/* 2565 */     public void visitUnary(JCUnary param1JCUnary) { visitTree(param1JCUnary); }
/* 2566 */     public void visitBinary(JCBinary param1JCBinary) { visitTree(param1JCBinary); }
/* 2567 */     public void visitTypeCast(JCTypeCast param1JCTypeCast) { visitTree(param1JCTypeCast); }
/* 2568 */     public void visitTypeTest(JCInstanceOf param1JCInstanceOf) { visitTree(param1JCInstanceOf); }
/* 2569 */     public void visitIndexed(JCArrayAccess param1JCArrayAccess) { visitTree(param1JCArrayAccess); }
/* 2570 */     public void visitSelect(JCFieldAccess param1JCFieldAccess) { visitTree(param1JCFieldAccess); }
/* 2571 */     public void visitReference(JCMemberReference param1JCMemberReference) { visitTree(param1JCMemberReference); }
/* 2572 */     public void visitIdent(JCIdent param1JCIdent) { visitTree(param1JCIdent); }
/* 2573 */     public void visitLiteral(JCLiteral param1JCLiteral) { visitTree(param1JCLiteral); }
/* 2574 */     public void visitTypeIdent(JCPrimitiveTypeTree param1JCPrimitiveTypeTree) { visitTree(param1JCPrimitiveTypeTree); }
/* 2575 */     public void visitTypeArray(JCArrayTypeTree param1JCArrayTypeTree) { visitTree(param1JCArrayTypeTree); }
/* 2576 */     public void visitTypeApply(JCTypeApply param1JCTypeApply) { visitTree(param1JCTypeApply); }
/* 2577 */     public void visitTypeUnion(JCTypeUnion param1JCTypeUnion) { visitTree(param1JCTypeUnion); }
/* 2578 */     public void visitTypeIntersection(JCTypeIntersection param1JCTypeIntersection) { visitTree(param1JCTypeIntersection); }
/* 2579 */     public void visitTypeParameter(JCTypeParameter param1JCTypeParameter) { visitTree(param1JCTypeParameter); }
/* 2580 */     public void visitWildcard(JCWildcard param1JCWildcard) { visitTree(param1JCWildcard); }
/* 2581 */     public void visitTypeBoundKind(TypeBoundKind param1TypeBoundKind) { visitTree(param1TypeBoundKind); }
/* 2582 */     public void visitAnnotation(JCAnnotation param1JCAnnotation) { visitTree(param1JCAnnotation); }
/* 2583 */     public void visitModifiers(JCModifiers param1JCModifiers) { visitTree(param1JCModifiers); }
/* 2584 */     public void visitAnnotatedType(JCAnnotatedType param1JCAnnotatedType) { visitTree(param1JCAnnotatedType); }
/* 2585 */     public void visitErroneous(JCErroneous param1JCErroneous) { visitTree(param1JCErroneous); } public void visitLetExpr(LetExpr param1LetExpr) {
/* 2586 */       visitTree(param1LetExpr);
/*      */     } public void visitTree(JCTree param1JCTree) {
/* 2588 */       Assert.error();
/*      */     }
/*      */   }
/*      */
/*      */   public static interface Factory {
/*      */     JCCompilationUnit TopLevel(List<JCAnnotation> param1List, JCExpression param1JCExpression, List<JCTree> param1List1);
/*      */
/*      */     JCImport Import(JCTree param1JCTree, boolean param1Boolean);
/*      */
/*      */     JCClassDecl ClassDef(JCModifiers param1JCModifiers, Name param1Name, List<JCTypeParameter> param1List, JCExpression param1JCExpression, List<JCExpression> param1List1, List<JCTree> param1List2);
/*      */
/*      */     JCMethodDecl MethodDef(JCModifiers param1JCModifiers, Name param1Name, JCExpression param1JCExpression1, List<JCTypeParameter> param1List, JCVariableDecl param1JCVariableDecl, List<JCVariableDecl> param1List1, List<JCExpression> param1List2, JCBlock param1JCBlock, JCExpression param1JCExpression2);
/*      */
/*      */     JCVariableDecl VarDef(JCModifiers param1JCModifiers, Name param1Name, JCExpression param1JCExpression1, JCExpression param1JCExpression2);
/*      */
/*      */     JCSkip Skip();
/*      */
/*      */     JCBlock Block(long param1Long, List<JCStatement> param1List);
/*      */
/*      */     JCDoWhileLoop DoLoop(JCStatement param1JCStatement, JCExpression param1JCExpression);
/*      */
/*      */     JCWhileLoop WhileLoop(JCExpression param1JCExpression, JCStatement param1JCStatement);
/*      */
/*      */     JCForLoop ForLoop(List<JCStatement> param1List, JCExpression param1JCExpression, List<JCExpressionStatement> param1List1, JCStatement param1JCStatement);
/*      */
/*      */     JCEnhancedForLoop ForeachLoop(JCVariableDecl param1JCVariableDecl, JCExpression param1JCExpression, JCStatement param1JCStatement);
/*      */
/*      */     JCLabeledStatement Labelled(Name param1Name, JCStatement param1JCStatement);
/*      */
/*      */     JCSwitch Switch(JCExpression param1JCExpression, List<JCCase> param1List);
/*      */
/*      */     JCCase Case(JCExpression param1JCExpression, List<JCStatement> param1List);
/*      */
/*      */     JCSynchronized Synchronized(JCExpression param1JCExpression, JCBlock param1JCBlock);
/*      */
/*      */     JCTry Try(JCBlock param1JCBlock1, List<JCCatch> param1List, JCBlock param1JCBlock2);
/*      */
/*      */     JCTry Try(List<JCTree> param1List, JCBlock param1JCBlock1, List<JCCatch> param1List1, JCBlock param1JCBlock2);
/*      */
/*      */     JCCatch Catch(JCVariableDecl param1JCVariableDecl, JCBlock param1JCBlock);
/*      */
/*      */     JCConditional Conditional(JCExpression param1JCExpression1, JCExpression param1JCExpression2, JCExpression param1JCExpression3);
/*      */
/*      */     JCIf If(JCExpression param1JCExpression, JCStatement param1JCStatement1, JCStatement param1JCStatement2);
/*      */
/*      */     JCExpressionStatement Exec(JCExpression param1JCExpression);
/*      */
/*      */     JCBreak Break(Name param1Name);
/*      */
/*      */     JCContinue Continue(Name param1Name);
/*      */
/*      */     JCReturn Return(JCExpression param1JCExpression);
/*      */
/*      */     JCThrow Throw(JCExpression param1JCExpression);
/*      */
/*      */     JCAssert Assert(JCExpression param1JCExpression1, JCExpression param1JCExpression2);
/*      */
/*      */     JCMethodInvocation Apply(List<JCExpression> param1List1, JCExpression param1JCExpression, List<JCExpression> param1List2);
/*      */
/*      */     JCNewClass NewClass(JCExpression param1JCExpression1, List<JCExpression> param1List1, JCExpression param1JCExpression2, List<JCExpression> param1List2, JCClassDecl param1JCClassDecl);
/*      */
/*      */     JCNewArray NewArray(JCExpression param1JCExpression, List<JCExpression> param1List1, List<JCExpression> param1List2);
/*      */
/*      */     JCParens Parens(JCExpression param1JCExpression);
/*      */
/*      */     JCAssign Assign(JCExpression param1JCExpression1, JCExpression param1JCExpression2);
/*      */
/*      */     JCAssignOp Assignop(Tag param1Tag, JCTree param1JCTree1, JCTree param1JCTree2);
/*      */
/*      */     JCUnary Unary(Tag param1Tag, JCExpression param1JCExpression);
/*      */
/*      */     JCBinary Binary(Tag param1Tag, JCExpression param1JCExpression1, JCExpression param1JCExpression2);
/*      */
/*      */     JCTypeCast TypeCast(JCTree param1JCTree, JCExpression param1JCExpression);
/*      */
/*      */     JCInstanceOf TypeTest(JCExpression param1JCExpression, JCTree param1JCTree);
/*      */
/*      */     JCArrayAccess Indexed(JCExpression param1JCExpression1, JCExpression param1JCExpression2);
/*      */
/*      */     JCFieldAccess Select(JCExpression param1JCExpression, Name param1Name);
/*      */
/*      */     JCIdent Ident(Name param1Name);
/*      */
/*      */     JCLiteral Literal(TypeTag param1TypeTag, Object param1Object);
/*      */
/*      */     JCPrimitiveTypeTree TypeIdent(TypeTag param1TypeTag);
/*      */
/*      */     JCArrayTypeTree TypeArray(JCExpression param1JCExpression);
/*      */
/*      */     JCTypeApply TypeApply(JCExpression param1JCExpression, List<JCExpression> param1List);
/*      */
/*      */     JCTypeParameter TypeParameter(Name param1Name, List<JCExpression> param1List);
/*      */
/*      */     JCWildcard Wildcard(TypeBoundKind param1TypeBoundKind, JCTree param1JCTree);
/*      */
/*      */     TypeBoundKind TypeBoundKind(BoundKind param1BoundKind);
/*      */
/*      */     JCAnnotation Annotation(JCTree param1JCTree, List<JCExpression> param1List);
/*      */
/*      */     JCModifiers Modifiers(long param1Long, List<JCAnnotation> param1List);
/*      */
/*      */     JCErroneous Erroneous(List<? extends JCTree> param1List);
/*      */
/*      */     LetExpr LetExpr(List<JCVariableDecl> param1List, JCTree param1JCTree);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\JCTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
