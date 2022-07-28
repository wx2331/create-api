/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.source.doctree.DocTreeVisitor;
/*     */ import com.sun.source.tree.CatchTree;
/*     */ import com.sun.source.tree.ClassTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.MethodTree;
/*     */ import com.sun.source.tree.Scope;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.util.DocSourcePositions;
/*     */ import com.sun.source.util.DocTreePath;
/*     */ import com.sun.source.util.DocTreeScanner;
/*     */ import com.sun.source.util.DocTrees;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.SourcePositions;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.comp.Attr;
/*     */ import com.sun.tools.javac.comp.AttrContext;
/*     */ import com.sun.tools.javac.comp.Enter;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.comp.MemberEnter;
/*     */ import com.sun.tools.javac.comp.Resolve;
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.tree.DCTree;
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.TreeCopier;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeMaker;
/*     */ import com.sun.tools.javac.util.Abort;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.ErrorType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaFileObject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavacTrees
/*     */   extends DocTrees
/*     */ {
/*     */   private Resolve resolve;
/*     */   private Enter enter;
/*     */   private Log log;
/*     */   private MemberEnter memberEnter;
/*     */   private Attr attr;
/*     */   private TreeMaker treeMaker;
/*     */   private JavacElements elements;
/*     */   private JavacTaskImpl javacTaskImpl;
/*     */   private Names names;
/*     */   private Types types;
/*     */   Types.TypeRelation fuzzyMatcher;
/*     */   
/*     */   public static JavacTrees instance(JavaCompiler.CompilationTask paramCompilationTask) {
/* 135 */     if (!(paramCompilationTask instanceof BasicJavacTask))
/* 136 */       throw new IllegalArgumentException(); 
/* 137 */     return instance(((BasicJavacTask)paramCompilationTask).getContext());
/*     */   }
/*     */ 
/*     */   
/*     */   public static JavacTrees instance(ProcessingEnvironment paramProcessingEnvironment) {
/* 142 */     if (!(paramProcessingEnvironment instanceof JavacProcessingEnvironment))
/* 143 */       throw new IllegalArgumentException(); 
/* 144 */     return instance(((JavacProcessingEnvironment)paramProcessingEnvironment).getContext());
/*     */   }
/*     */   
/*     */   public static JavacTrees instance(Context paramContext) {
/* 148 */     JavacTrees javacTrees = (JavacTrees)paramContext.get(JavacTrees.class);
/* 149 */     if (javacTrees == null)
/* 150 */       javacTrees = new JavacTrees(paramContext); 
/* 151 */     return javacTrees;
/*     */   }
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
/*     */   protected JavacTrees(Context paramContext) {
/* 646 */     this.fuzzyMatcher = new Types.TypeRelation()
/*     */       {
/*     */         public Boolean visitType(Type param1Type1, Type param1Type2) {
/*     */           // Byte code:
/*     */           //   0: aload_1
/*     */           //   1: aload_2
/*     */           //   2: if_acmpne -> 10
/*     */           //   5: iconst_1
/*     */           //   6: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */           //   9: areturn
/*     */           //   10: aload_2
/*     */           //   11: invokevirtual isPartial : ()Z
/*     */           //   14: ifeq -> 27
/*     */           //   17: aload_0
/*     */           //   18: aload_2
/*     */           //   19: aload_1
/*     */           //   20: invokevirtual visit : (Lcom/sun/tools/javac/code/Type;Ljava/lang/Object;)Ljava/lang/Object;
/*     */           //   23: checkcast java/lang/Boolean
/*     */           //   26: areturn
/*     */           //   27: getstatic com/sun/tools/javac/api/JavacTrees$4.$SwitchMap$com$sun$tools$javac$code$TypeTag : [I
/*     */           //   30: aload_1
/*     */           //   31: invokevirtual getTag : ()Lcom/sun/tools/javac/code/TypeTag;
/*     */           //   34: invokevirtual ordinal : ()I
/*     */           //   37: iaload
/*     */           //   38: tableswitch default -> 108, 1 -> 96, 2 -> 96, 3 -> 96, 4 -> 96, 5 -> 96, 6 -> 96, 7 -> 96, 8 -> 96, 9 -> 96, 10 -> 96, 11 -> 96
/*     */           //   96: aload_1
/*     */           //   97: aload_2
/*     */           //   98: invokevirtual getTag : ()Lcom/sun/tools/javac/code/TypeTag;
/*     */           //   101: invokevirtual hasTag : (Lcom/sun/tools/javac/code/TypeTag;)Z
/*     */           //   104: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */           //   107: areturn
/*     */           //   108: new java/lang/AssertionError
/*     */           //   111: dup
/*     */           //   112: new java/lang/StringBuilder
/*     */           //   115: dup
/*     */           //   116: invokespecial <init> : ()V
/*     */           //   119: ldc 'fuzzyMatcher '
/*     */           //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */           //   124: aload_1
/*     */           //   125: invokevirtual getTag : ()Lcom/sun/tools/javac/code/TypeTag;
/*     */           //   128: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */           //   131: invokevirtual toString : ()Ljava/lang/String;
/*     */           //   134: invokespecial <init> : (Ljava/lang/Object;)V
/*     */           //   137: athrow
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #649	-> 0
/*     */           //   #650	-> 5
/*     */           //   #652	-> 10
/*     */           //   #653	-> 17
/*     */           //   #655	-> 27
/*     */           //   #658	-> 96
/*     */           //   #660	-> 108
/*     */         }
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
/*     */         public Boolean visitArrayType(Type.ArrayType param1ArrayType, Type param1Type) {
/* 666 */           if (param1ArrayType == param1Type) {
/* 667 */             return Boolean.valueOf(true);
/*     */           }
/* 669 */           if (param1Type.isPartial()) {
/* 670 */             return (Boolean)visit(param1Type, param1ArrayType);
/*     */           }
/* 672 */           return Boolean.valueOf((param1Type.hasTag(TypeTag.ARRAY) && ((Boolean)
/* 673 */               visit(param1ArrayType.elemtype, JavacTrees.this.types.elemtype(param1Type))).booleanValue()));
/*     */         }
/*     */ 
/*     */         
/*     */         public Boolean visitClassType(Type.ClassType param1ClassType, Type param1Type) {
/* 678 */           if (param1ClassType == param1Type) {
/* 679 */             return Boolean.valueOf(true);
/*     */           }
/* 681 */           if (param1Type.isPartial()) {
/* 682 */             return (Boolean)visit(param1Type, param1ClassType);
/*     */           }
/* 684 */           return Boolean.valueOf((param1ClassType.tsym == param1Type.tsym));
/*     */         }
/*     */         
/*     */         public Boolean visitErrorType(Type.ErrorType param1ErrorType, Type param1Type)
/*     */         {
/* 689 */           return Boolean.valueOf((param1Type.hasTag(TypeTag.CLASS) && param1ErrorType.tsym.name == ((Type.ClassType)param1Type).tsym.name)); }
/*     */       }; paramContext.put(JavacTrees.class, this); init(paramContext);
/*     */   } public void updateContext(Context paramContext) { init(paramContext); } private void init(Context paramContext) { this.attr = Attr.instance(paramContext); this.enter = Enter.instance(paramContext); this.elements = JavacElements.instance(paramContext); this.log = Log.instance(paramContext); this.resolve = Resolve.instance(paramContext); this.treeMaker = TreeMaker.instance(paramContext); this.memberEnter = MemberEnter.instance(paramContext); this.names = Names.instance(paramContext); this.types = Types.instance(paramContext); JavacTask javacTask = (JavacTask)paramContext.get(JavacTask.class); if (javacTask instanceof JavacTaskImpl) this.javacTaskImpl = (JavacTaskImpl)javacTask;  } public DocSourcePositions getSourcePositions() { return new DocSourcePositions() {
/*     */         public long getStartPosition(CompilationUnitTree param1CompilationUnitTree, Tree param1Tree) { return TreeInfo.getStartPos((JCTree)param1Tree); } public long getEndPosition(CompilationUnitTree param1CompilationUnitTree, Tree param1Tree) { EndPosTable endPosTable = ((JCTree.JCCompilationUnit)param1CompilationUnitTree).endPositions; return TreeInfo.getEndPos((JCTree)param1Tree, endPosTable); } public long getStartPosition(CompilationUnitTree param1CompilationUnitTree, DocCommentTree param1DocCommentTree, DocTree param1DocTree) { return ((DCTree)param1DocTree).getSourcePosition((DCTree.DCDocComment)param1DocCommentTree); } public long getEndPosition(CompilationUnitTree param1CompilationUnitTree, DocCommentTree param1DocCommentTree, DocTree param1DocTree) { // Byte code:
/*     */           //   0: aload_2
/*     */           //   1: checkcast com/sun/tools/javac/tree/DCTree$DCDocComment
/*     */           //   4: astore #4
/*     */           //   6: aload_3
/*     */           //   7: instanceof com/sun/tools/javac/tree/DCTree$DCEndPosTree
/*     */           //   10: ifeq -> 34
/*     */           //   13: aload_3
/*     */           //   14: checkcast com/sun/tools/javac/tree/DCTree$DCEndPosTree
/*     */           //   17: aload #4
/*     */           //   19: invokevirtual getEndPos : (Lcom/sun/tools/javac/tree/DCTree$DCDocComment;)I
/*     */           //   22: istore #5
/*     */           //   24: iload #5
/*     */           //   26: iconst_m1
/*     */           //   27: if_icmpeq -> 34
/*     */           //   30: iload #5
/*     */           //   32: i2l
/*     */           //   33: lreturn
/*     */           //   34: iconst_0
/*     */           //   35: istore #5
/*     */           //   37: getstatic com/sun/tools/javac/api/JavacTrees$4.$SwitchMap$com$sun$source$doctree$DocTree$Kind : [I
/*     */           //   40: aload_3
/*     */           //   41: invokeinterface getKind : ()Lcom/sun/source/doctree/DocTree$Kind;
/*     */           //   46: invokevirtual ordinal : ()I
/*     */           //   49: iaload
/*     */           //   50: tableswitch default -> 332, 1 -> 124, 2 -> 156, 3 -> 188, 4 -> 242, 5 -> 270, 6 -> 270, 7 -> 270, 8 -> 270, 9 -> 270, 10 -> 270, 11 -> 270, 12 -> 270, 13 -> 270, 14 -> 270, 15 -> 270
/*     */           //   124: aload_3
/*     */           //   125: checkcast com/sun/tools/javac/tree/DCTree$DCText
/*     */           //   128: astore #6
/*     */           //   130: aload #4
/*     */           //   132: getfield comment : Lcom/sun/tools/javac/parser/Tokens$Comment;
/*     */           //   135: aload #6
/*     */           //   137: getfield pos : I
/*     */           //   140: aload #6
/*     */           //   142: getfield text : Ljava/lang/String;
/*     */           //   145: invokevirtual length : ()I
/*     */           //   148: iadd
/*     */           //   149: invokeinterface getSourcePos : (I)I
/*     */           //   154: i2l
/*     */           //   155: lreturn
/*     */           //   156: aload_3
/*     */           //   157: checkcast com/sun/tools/javac/tree/DCTree$DCErroneous
/*     */           //   160: astore #7
/*     */           //   162: aload #4
/*     */           //   164: getfield comment : Lcom/sun/tools/javac/parser/Tokens$Comment;
/*     */           //   167: aload #7
/*     */           //   169: getfield pos : I
/*     */           //   172: aload #7
/*     */           //   174: getfield body : Ljava/lang/String;
/*     */           //   177: invokevirtual length : ()I
/*     */           //   180: iadd
/*     */           //   181: invokeinterface getSourcePos : (I)I
/*     */           //   186: i2l
/*     */           //   187: lreturn
/*     */           //   188: aload_3
/*     */           //   189: checkcast com/sun/tools/javac/tree/DCTree$DCIdentifier
/*     */           //   192: astore #8
/*     */           //   194: aload #4
/*     */           //   196: getfield comment : Lcom/sun/tools/javac/parser/Tokens$Comment;
/*     */           //   199: aload #8
/*     */           //   201: getfield pos : I
/*     */           //   204: aload #8
/*     */           //   206: getfield name : Lcom/sun/tools/javac/util/Name;
/*     */           //   209: aload_0
/*     */           //   210: getfield this$0 : Lcom/sun/tools/javac/api/JavacTrees;
/*     */           //   213: invokestatic access$000 : (Lcom/sun/tools/javac/api/JavacTrees;)Lcom/sun/tools/javac/util/Names;
/*     */           //   216: getfield error : Lcom/sun/tools/javac/util/Name;
/*     */           //   219: if_acmpeq -> 233
/*     */           //   222: aload #8
/*     */           //   224: getfield name : Lcom/sun/tools/javac/util/Name;
/*     */           //   227: invokevirtual length : ()I
/*     */           //   230: goto -> 234
/*     */           //   233: iconst_0
/*     */           //   234: iadd
/*     */           //   235: invokeinterface getSourcePos : (I)I
/*     */           //   240: i2l
/*     */           //   241: lreturn
/*     */           //   242: aload_3
/*     */           //   243: checkcast com/sun/tools/javac/tree/DCTree$DCParam
/*     */           //   246: astore #9
/*     */           //   248: aload #9
/*     */           //   250: getfield isTypeParameter : Z
/*     */           //   253: ifeq -> 270
/*     */           //   256: aload #9
/*     */           //   258: invokevirtual getDescription : ()Lcom/sun/tools/javac/util/List;
/*     */           //   261: invokevirtual isEmpty : ()Z
/*     */           //   264: ifeq -> 270
/*     */           //   267: iconst_1
/*     */           //   268: istore #5
/*     */           //   270: aload_0
/*     */           //   271: getfield this$0 : Lcom/sun/tools/javac/api/JavacTrees;
/*     */           //   274: aload_3
/*     */           //   275: invokestatic access$100 : (Lcom/sun/tools/javac/api/JavacTrees;Lcom/sun/source/doctree/DocTree;)Lcom/sun/source/doctree/DocTree;
/*     */           //   278: astore #10
/*     */           //   280: aload #10
/*     */           //   282: ifnull -> 298
/*     */           //   285: aload_0
/*     */           //   286: aload_1
/*     */           //   287: aload_2
/*     */           //   288: aload #10
/*     */           //   290: invokevirtual getEndPosition : (Lcom/sun/source/tree/CompilationUnitTree;Lcom/sun/source/doctree/DocCommentTree;Lcom/sun/source/doctree/DocTree;)J
/*     */           //   293: iload #5
/*     */           //   295: i2l
/*     */           //   296: ladd
/*     */           //   297: lreturn
/*     */           //   298: aload_3
/*     */           //   299: checkcast com/sun/tools/javac/tree/DCTree$DCBlockTag
/*     */           //   302: astore #11
/*     */           //   304: aload #4
/*     */           //   306: getfield comment : Lcom/sun/tools/javac/parser/Tokens$Comment;
/*     */           //   309: aload #11
/*     */           //   311: getfield pos : I
/*     */           //   314: aload #11
/*     */           //   316: invokevirtual getTagName : ()Ljava/lang/String;
/*     */           //   319: invokevirtual length : ()I
/*     */           //   322: iadd
/*     */           //   323: iconst_1
/*     */           //   324: iadd
/*     */           //   325: invokeinterface getSourcePos : (I)I
/*     */           //   330: i2l
/*     */           //   331: lreturn
/*     */           //   332: aload_0
/*     */           //   333: getfield this$0 : Lcom/sun/tools/javac/api/JavacTrees;
/*     */           //   336: aload_3
/*     */           //   337: invokestatic access$100 : (Lcom/sun/tools/javac/api/JavacTrees;Lcom/sun/source/doctree/DocTree;)Lcom/sun/source/doctree/DocTree;
/*     */           //   340: astore #10
/*     */           //   342: aload #10
/*     */           //   344: ifnull -> 356
/*     */           //   347: aload_0
/*     */           //   348: aload_1
/*     */           //   349: aload_2
/*     */           //   350: aload #10
/*     */           //   352: invokevirtual getEndPosition : (Lcom/sun/source/tree/CompilationUnitTree;Lcom/sun/source/doctree/DocCommentTree;Lcom/sun/source/doctree/DocTree;)J
/*     */           //   355: lreturn
/*     */           //   356: ldc2_w -1
/*     */           //   359: lreturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #195	-> 0
/*     */           //   #196	-> 6
/*     */           //   #197	-> 13
/*     */           //   #199	-> 24
/*     */           //   #200	-> 30
/*     */           //   #203	-> 34
/*     */           //   #204	-> 37
/*     */           //   #206	-> 124
/*     */           //   #208	-> 130
/*     */           //   #210	-> 156
/*     */           //   #212	-> 162
/*     */           //   #214	-> 188
/*     */           //   #216	-> 194
/*     */           //   #218	-> 242
/*     */           //   #220	-> 248
/*     */           //   #221	-> 267
/*     */           //   #226	-> 270
/*     */           //   #228	-> 280
/*     */           //   #229	-> 285
/*     */           //   #232	-> 298
/*     */           //   #234	-> 304
/*     */           //   #237	-> 332
/*     */           //   #239	-> 342
/*     */           //   #240	-> 347
/*     */           //   #245	-> 356 }
/*     */       }; } private DocTree getLastChild(DocTree paramDocTree) { final DocTree[] last = { null }; paramDocTree.accept((DocTreeVisitor)new DocTreeScanner<Void, Void>() {
/*     */           public Void scan(DocTree param1DocTree, Void param1Void) { if (param1DocTree != null) last[0] = param1DocTree;  return null; }
/* 695 */         },  null); return arrayOfDocTree[0]; } public JCTree.JCClassDecl getTree(TypeElement paramTypeElement) { return (JCTree.JCClassDecl)getTree(paramTypeElement); } public JCTree.JCMethodDecl getTree(ExecutableElement paramExecutableElement) { return (JCTree.JCMethodDecl)getTree(paramExecutableElement); } public JCTree getTree(Element paramElement) { Symbol symbol = (Symbol)paramElement; Symbol.ClassSymbol classSymbol = symbol.enclClass(); Env env = this.enter.getEnv((Symbol.TypeSymbol)classSymbol); if (env == null) return null;  JCTree.JCClassDecl jCClassDecl = env.enclClass; if (jCClassDecl != null) { if (TreeInfo.symbolFor((JCTree)jCClassDecl) == paramElement) return (JCTree)jCClassDecl;  for (JCTree jCTree : jCClassDecl.getMembers()) { if (TreeInfo.symbolFor(jCTree) == paramElement) return jCTree;  }  }  return null; } public JCTree getTree(Element paramElement, AnnotationMirror paramAnnotationMirror) { return getTree(paramElement, paramAnnotationMirror, (AnnotationValue)null); } public JCTree getTree(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) { Pair pair = this.elements.getTreeAndTopLevel(paramElement, paramAnnotationMirror, paramAnnotationValue); if (pair == null) return null;  return (JCTree)pair.fst; } public TreePath getPath(CompilationUnitTree paramCompilationUnitTree, Tree paramTree) { return TreePath.getPath(paramCompilationUnitTree, paramTree); } public TreePath getPath(Element paramElement) { return getPath(paramElement, null, null); } public TreePath getPath(Element paramElement, AnnotationMirror paramAnnotationMirror) { return getPath(paramElement, paramAnnotationMirror, null); } public TypeMirror getTypeMirror(TreePath paramTreePath) { Tree tree = paramTreePath.getLeaf();
/* 696 */     return (TypeMirror)((JCTree)tree).type; }
/*     */   public TreePath getPath(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) { Pair pair = this.elements.getTreeAndTopLevel(paramElement, paramAnnotationMirror, paramAnnotationValue); if (pair == null) return null;  return TreePath.getPath((CompilationUnitTree)pair.snd, (Tree)pair.fst); }
/*     */   public Symbol getElement(TreePath paramTreePath) { JCTree jCTree = (JCTree)paramTreePath.getLeaf(); Symbol symbol = TreeInfo.symbolFor(jCTree); if (symbol == null && TreeInfo.isDeclaration(jCTree)) for (TreePath treePath = paramTreePath; treePath != null; treePath = treePath.getParentPath()) { JCTree jCTree1 = (JCTree)treePath.getLeaf(); if (jCTree1.hasTag(JCTree.Tag.CLASSDEF)) { JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)jCTree1; if (jCClassDecl.sym != null) { if ((jCClassDecl.sym.flags_field & 0x10000000L) != 0L) { this.attr.attribClass(jCClassDecl.pos(), jCClassDecl.sym); symbol = TreeInfo.symbolFor(jCTree); }  break; }  }  }   return symbol; }
/*     */   public Element getElement(DocTreePath paramDocTreePath) { DocTree docTree = paramDocTreePath.getLeaf(); if (docTree instanceof DCTree.DCReference) return (Element)attributeDocReference(paramDocTreePath.getTreePath(), (DCTree.DCReference)docTree);  if (docTree instanceof DCTree.DCIdentifier && paramDocTreePath.getParentPath().getLeaf() instanceof DCTree.DCParam) return (Element)attributeParamIdentifier(paramDocTreePath.getTreePath(), (DCTree.DCParam)paramDocTreePath.getParentPath().getLeaf());  return null; }
/* 700 */   private Symbol attributeDocReference(TreePath paramTreePath, DCTree.DCReference paramDCReference) { Env<AttrContext> env = getAttrContext(paramTreePath); Log.DeferredDiagnosticHandler deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log); try { Symbol.TypeSymbol typeSymbol; Name name; List<Type> list; if (paramDCReference.qualifierExpression == null) { Symbol.ClassSymbol classSymbol1 = env.enclClass.sym; name = paramDCReference.memberName; } else { Type type = this.attr.attribType(paramDCReference.qualifierExpression, env); if (type.isErroneous()) { if (paramDCReference.memberName == null) { Symbol.PackageSymbol packageSymbol = this.elements.getPackageElement(paramDCReference.qualifierExpression.toString()); if (packageSymbol != null) return (Symbol)packageSymbol;  if (paramDCReference.qualifierExpression.hasTag(JCTree.Tag.IDENT)) { Symbol.ClassSymbol classSymbol1 = env.enclClass.sym; name = ((JCTree.JCIdent)paramDCReference.qualifierExpression).name; } else { return null; }  } else { return null; }  } else { typeSymbol = type.tsym; name = paramDCReference.memberName; }  }  if (name == null) return (Symbol)typeSymbol;  if (paramDCReference.paramTypes == null) { list = null; } else { ListBuffer listBuffer = new ListBuffer(); for (List list1 = paramDCReference.paramTypes; list1.nonEmpty(); list1 = list1.tail) { JCTree jCTree = (JCTree)list1.head; Type type = this.attr.attribType(jCTree, env); listBuffer.add(type); }  list = listBuffer.toList(); }  Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)(this.types.cvarUpperBound(typeSymbol.type)).tsym; Symbol.MethodSymbol methodSymbol = (name == classSymbol.name) ? findConstructor(classSymbol, list) : findMethod(classSymbol, name, list); if (list != null) return (Symbol)methodSymbol;  Symbol.VarSymbol varSymbol = (paramDCReference.paramTypes != null) ? null : findField(classSymbol, name); if (varSymbol != null && (methodSymbol == null || this.types.isSubtypeUnchecked(varSymbol.enclClass().asType(), methodSymbol.enclClass().asType()))) return (Symbol)varSymbol;  return (Symbol)methodSymbol; } catch (Abort abort) { return null; } finally { this.log.popDiagnosticHandler((Log.DiagnosticHandler)deferredDiagnosticHandler); }  } private Symbol attributeParamIdentifier(TreePath paramTreePath, DCTree.DCParam paramDCParam) { Symbol symbol = getElement(paramTreePath); if (symbol == null) return null;  ElementKind elementKind = symbol.getKind(); List list = List.nil(); if (elementKind == ElementKind.METHOD || elementKind == ElementKind.CONSTRUCTOR) { Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)symbol; list = paramDCParam.isTypeParameter() ? methodSymbol.getTypeParameters() : methodSymbol.getParameters(); } else if (elementKind.isClass() || elementKind.isInterface()) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)symbol; list = classSymbol.getTypeParameters(); }  for (Symbol symbol1 : list) { if (symbol1.getSimpleName() == paramDCParam.getName().getName()) return symbol1;  }  return null; } private Symbol.VarSymbol findField(Symbol.ClassSymbol paramClassSymbol, Name paramName) { return searchField(paramClassSymbol, paramName, new HashSet<>()); } private Symbol.VarSymbol searchField(Symbol.ClassSymbol paramClassSymbol, Name paramName, Set<Symbol.ClassSymbol> paramSet) { if (paramSet.contains(paramClassSymbol)) return null;  paramSet.add(paramClassSymbol); Scope.Entry entry = paramClassSymbol.members().lookup(paramName); for (; entry.scope != null; entry = entry.next()) { if (entry.sym.kind == 4) return (Symbol.VarSymbol)entry.sym;  }  Symbol.ClassSymbol classSymbol = paramClassSymbol.owner.enclClass(); if (classSymbol != null) { Symbol.VarSymbol varSymbol = searchField(classSymbol, paramName, paramSet); if (varSymbol != null) return varSymbol;  }  Type type = paramClassSymbol.getSuperclass(); if (type.tsym != null) { Symbol.VarSymbol varSymbol = searchField((Symbol.ClassSymbol)type.tsym, paramName, paramSet); if (varSymbol != null) return varSymbol;  }  List list1 = paramClassSymbol.getInterfaces(); for (List list2 = list1; list2.nonEmpty(); list2 = list2.tail) { Type type1 = (Type)list2.head; if (!type1.isErroneous()) { Symbol.VarSymbol varSymbol = searchField((Symbol.ClassSymbol)type1.tsym, paramName, paramSet); if (varSymbol != null) return varSymbol;  }  }  return null; } Symbol.MethodSymbol findConstructor(Symbol.ClassSymbol paramClassSymbol, List<Type> paramList) { Scope.Entry entry = paramClassSymbol.members().lookup(this.names.init); for (; entry.scope != null; entry = entry.next()) { if (entry.sym.kind == 16 && hasParameterTypes((Symbol.MethodSymbol)entry.sym, paramList)) return (Symbol.MethodSymbol)entry.sym;  }  return null; } private Symbol.MethodSymbol findMethod(Symbol.ClassSymbol paramClassSymbol, Name paramName, List<Type> paramList) { return searchMethod(paramClassSymbol, paramName, paramList, new HashSet<>()); } private Symbol.MethodSymbol searchMethod(Symbol.ClassSymbol paramClassSymbol, Name paramName, List<Type> paramList, Set<Symbol.ClassSymbol> paramSet) { if (paramName == this.names.init) return null;  if (paramSet.contains(paramClassSymbol)) return null;  paramSet.add(paramClassSymbol); Scope.Entry entry = paramClassSymbol.members().lookup(paramName); if (paramList == null) { Symbol.MethodSymbol methodSymbol = null; for (; entry.scope != null; entry = entry.next()) { if (entry.sym.kind == 16 && entry.sym.name == paramName) methodSymbol = (Symbol.MethodSymbol)entry.sym;  }  if (methodSymbol != null) return methodSymbol;  } else { for (; entry.scope != null; entry = entry.next()) { if (entry.sym != null && entry.sym.kind == 16) if (hasParameterTypes((Symbol.MethodSymbol)entry.sym, paramList)) return (Symbol.MethodSymbol)entry.sym;   }  }  Type type = paramClassSymbol.getSuperclass(); if (type.tsym != null) { Symbol.MethodSymbol methodSymbol = searchMethod((Symbol.ClassSymbol)type.tsym, paramName, paramList, paramSet); if (methodSymbol != null) return methodSymbol;  }  List list1 = paramClassSymbol.getInterfaces(); for (List list2 = list1; list2.nonEmpty(); list2 = list2.tail) { Type type1 = (Type)list2.head; if (!type1.isErroneous()) { Symbol.MethodSymbol methodSymbol = searchMethod((Symbol.ClassSymbol)type1.tsym, paramName, paramList, paramSet); if (methodSymbol != null) return methodSymbol;  }  }  Symbol.ClassSymbol classSymbol = paramClassSymbol.owner.enclClass(); if (classSymbol != null) { Symbol.MethodSymbol methodSymbol = searchMethod(classSymbol, paramName, paramList, paramSet); if (methodSymbol != null) return methodSymbol;  }  return null; } private boolean hasParameterTypes(Symbol.MethodSymbol paramMethodSymbol, List<Type> paramList) { if (paramList == null) return true;  if (paramMethodSymbol.params().size() != paramList.size()) return false;  List<Type> list = this.types.erasureRecursive(paramMethodSymbol.asType()).getParameterTypes(); return Type.isErroneous(paramList) ? fuzzyMatch(paramList, list) : this.types.isSameTypes(paramList, list); } boolean fuzzyMatch(List<Type> paramList1, List<Type> paramList2) { List<Type> list1 = paramList1; List<Type> list2 = paramList2; while (list1.nonEmpty()) { if (!fuzzyMatch((Type)list1.head, (Type)list2.head)) return false;  list1 = list1.tail; list2 = list2.tail; }  return true; } boolean fuzzyMatch(Type paramType1, Type paramType2) { Boolean bool = (Boolean)this.fuzzyMatcher.visit(paramType1, paramType2); return (bool == Boolean.TRUE); } public JavacScope getScope(TreePath paramTreePath) { return new JavacScope(getAttrContext(paramTreePath)); }
/*     */ 
/*     */   
/*     */   public String getDocComment(TreePath paramTreePath) {
/* 704 */     CompilationUnitTree compilationUnitTree = paramTreePath.getCompilationUnit();
/* 705 */     Tree tree = paramTreePath.getLeaf();
/* 706 */     if (compilationUnitTree instanceof JCTree.JCCompilationUnit && tree instanceof JCTree) {
/* 707 */       JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)compilationUnitTree;
/* 708 */       if (jCCompilationUnit.docComments != null) {
/* 709 */         return jCCompilationUnit.docComments.getCommentText((JCTree)tree);
/*     */       }
/*     */     } 
/* 712 */     return null;
/*     */   }
/*     */   
/*     */   public DocCommentTree getDocCommentTree(TreePath paramTreePath) {
/* 716 */     CompilationUnitTree compilationUnitTree = paramTreePath.getCompilationUnit();
/* 717 */     Tree tree = paramTreePath.getLeaf();
/* 718 */     if (compilationUnitTree instanceof JCTree.JCCompilationUnit && tree instanceof JCTree) {
/* 719 */       JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)compilationUnitTree;
/* 720 */       if (jCCompilationUnit.docComments != null) {
/* 721 */         return (DocCommentTree)jCCompilationUnit.docComments.getCommentTree((JCTree)tree);
/*     */       }
/*     */     } 
/* 724 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isAccessible(Scope paramScope, TypeElement paramTypeElement) {
/* 728 */     if (paramScope instanceof JavacScope && paramTypeElement instanceof Symbol.ClassSymbol) {
/* 729 */       Env<AttrContext> env = ((JavacScope)paramScope).env;
/* 730 */       return this.resolve.isAccessible(env, (Symbol.TypeSymbol)paramTypeElement, true);
/*     */     } 
/* 732 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAccessible(Scope paramScope, Element paramElement, DeclaredType paramDeclaredType) {
/* 736 */     if (paramScope instanceof JavacScope && paramElement instanceof Symbol && paramDeclaredType instanceof Type) {
/*     */ 
/*     */       
/* 739 */       Env<AttrContext> env = ((JavacScope)paramScope).env;
/* 740 */       return this.resolve.isAccessible(env, (Type)paramDeclaredType, (Symbol)paramElement, true);
/*     */     } 
/* 742 */     return false;
/*     */   }
/*     */   
/*     */   private Env<AttrContext> getAttrContext(TreePath paramTreePath) {
/* 746 */     if (!(paramTreePath.getLeaf() instanceof JCTree)) {
/* 747 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 753 */     if (this.javacTaskImpl != null) {
/*     */       try {
/* 755 */         this.javacTaskImpl.enter(null);
/* 756 */       } catch (IOException iOException) {
/* 757 */         throw new Error("unexpected error while entering symbols: " + iOException);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 762 */     JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit();
/* 763 */     Copier copier = createCopier(this.treeMaker.forToplevel(jCCompilationUnit));
/*     */     
/* 765 */     Env<AttrContext> env = null;
/* 766 */     JCTree.JCMethodDecl jCMethodDecl = null;
/* 767 */     JCTree.JCVariableDecl jCVariableDecl = null;
/*     */     
/* 769 */     List list = List.nil();
/* 770 */     TreePath treePath = paramTreePath;
/* 771 */     while (treePath != null) {
/* 772 */       list = list.prepend(treePath.getLeaf());
/* 773 */       treePath = treePath.getParentPath();
/*     */     } 
/*     */     
/* 776 */     for (; list.nonEmpty(); list = list.tail) {
/* 777 */       Tree tree = (Tree)list.head;
/* 778 */       switch (tree.getKind()) {
/*     */         
/*     */         case ERROR:
/* 781 */           env = this.enter.getTopLevelEnv((JCTree.JCCompilationUnit)tree);
/*     */           break;
/*     */         
/*     */         case WARNING:
/*     */         case MANDATORY_WARNING:
/*     */         case null:
/*     */         case null:
/* 788 */           env = this.enter.getClassEnv((Symbol.TypeSymbol)((JCTree.JCClassDecl)tree).sym);
/*     */           break;
/*     */         
/*     */         case null:
/* 792 */           jCMethodDecl = (JCTree.JCMethodDecl)tree;
/* 793 */           env = this.memberEnter.getMethodEnv(jCMethodDecl, env);
/*     */           break;
/*     */         
/*     */         case null:
/* 797 */           jCVariableDecl = (JCTree.JCVariableDecl)tree;
/*     */           break;
/*     */         
/*     */         case null:
/* 801 */           if (jCMethodDecl != null) {
/*     */             try {
/* 803 */               Assert.check((jCMethodDecl.body == tree));
/* 804 */               jCMethodDecl.body = copier.<JCTree.JCBlock>copy((JCTree.JCBlock)tree, (JCTree)paramTreePath.getLeaf());
/* 805 */               env = attribStatToTree((JCTree)jCMethodDecl.body, env, copier.leafCopy);
/*     */             } finally {
/* 807 */               jCMethodDecl.body = (JCTree.JCBlock)tree;
/*     */             } 
/*     */           } else {
/* 810 */             JCTree.JCBlock jCBlock = copier.<JCTree.JCBlock>copy((JCTree.JCBlock)tree, (JCTree)paramTreePath.getLeaf());
/* 811 */             env = attribStatToTree((JCTree)jCBlock, env, copier.leafCopy);
/*     */           } 
/* 813 */           return env;
/*     */ 
/*     */         
/*     */         default:
/* 817 */           if (jCVariableDecl != null && jCVariableDecl.getInitializer() == tree) {
/* 818 */             env = this.memberEnter.getInitEnv(jCVariableDecl, env);
/* 819 */             JCTree.JCExpression jCExpression = copier.<JCTree.JCExpression>copy((JCTree.JCExpression)tree, (JCTree)paramTreePath.getLeaf());
/* 820 */             env = attribExprToTree(jCExpression, env, copier.leafCopy);
/* 821 */             return env;
/*     */           }  break;
/*     */       } 
/*     */     } 
/* 825 */     return (jCVariableDecl != null) ? this.memberEnter.getInitEnv(jCVariableDecl, env) : env;
/*     */   }
/*     */   
/*     */   private Env<AttrContext> attribStatToTree(JCTree paramJCTree1, Env<AttrContext> paramEnv, JCTree paramJCTree2) {
/* 829 */     JavaFileObject javaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*     */     try {
/* 831 */       return this.attr.attribStatToTree(paramJCTree1, paramEnv, paramJCTree2);
/*     */     } finally {
/* 833 */       this.log.useSource(javaFileObject);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Env<AttrContext> attribExprToTree(JCTree.JCExpression paramJCExpression, Env<AttrContext> paramEnv, JCTree paramJCTree) {
/* 838 */     JavaFileObject javaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*     */     try {
/* 840 */       return this.attr.attribExprToTree((JCTree)paramJCExpression, paramEnv, paramJCTree);
/*     */     } finally {
/* 842 */       this.log.useSource(javaFileObject);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class Copier
/*     */     extends TreeCopier<JCTree>
/*     */   {
/* 850 */     JCTree leafCopy = null;
/*     */     
/*     */     protected Copier(TreeMaker param1TreeMaker) {
/* 853 */       super(param1TreeMaker);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends JCTree> T copy(T param1T, JCTree param1JCTree) {
/* 858 */       JCTree jCTree = super.copy((JCTree)param1T, param1JCTree);
/* 859 */       if (param1T == param1JCTree)
/* 860 */         this.leafCopy = jCTree; 
/* 861 */       return (T)jCTree;
/*     */     }
/*     */   }
/*     */   
/*     */   protected Copier createCopier(TreeMaker paramTreeMaker) {
/* 866 */     return new Copier(paramTreeMaker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeMirror getOriginalType(ErrorType paramErrorType) {
/* 876 */     if (paramErrorType instanceof Type.ErrorType) {
/* 877 */       return (TypeMirror)((Type.ErrorType)paramErrorType).getOriginalType();
/*     */     }
/*     */     
/* 880 */     return (TypeMirror)Type.noType;
/*     */   }
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
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Tree paramTree, CompilationUnitTree paramCompilationUnitTree) {
/* 895 */     printMessage(paramKind, paramCharSequence, ((JCTree)paramTree).pos(), paramCompilationUnitTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, DocTree paramDocTree, DocCommentTree paramDocCommentTree, CompilationUnitTree paramCompilationUnitTree) {
/* 902 */     printMessage(paramKind, paramCharSequence, ((DCTree)paramDocTree).pos((DCTree.DCDocComment)paramDocCommentTree), paramCompilationUnitTree);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, CompilationUnitTree paramCompilationUnitTree) {
/* 908 */     JavaFileObject javaFileObject1 = null;
/* 909 */     JavaFileObject javaFileObject2 = null;
/*     */     
/* 911 */     javaFileObject2 = paramCompilationUnitTree.getSourceFile();
/* 912 */     if (javaFileObject2 == null) {
/* 913 */       paramDiagnosticPosition = null;
/*     */     } else {
/* 915 */       javaFileObject1 = this.log.useSource(javaFileObject2);
/*     */     } 
/*     */     try {
/*     */       boolean bool;
/* 919 */       switch (paramKind) {
/*     */         case ERROR:
/* 921 */           bool = this.log.multipleErrors;
/*     */           try {
/* 923 */             this.log.error(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */           } finally {
/* 925 */             this.log.multipleErrors = bool;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case WARNING:
/* 930 */           this.log.warning(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */           break;
/*     */         
/*     */         case MANDATORY_WARNING:
/* 934 */           this.log.mandatoryWarning(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */           break;
/*     */         
/*     */         default:
/* 938 */           this.log.note(paramDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() }); break;
/*     */       } 
/*     */     } finally {
/* 941 */       if (javaFileObject1 != null) {
/* 942 */         this.log.useSource(javaFileObject1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public TypeMirror getLub(CatchTree paramCatchTree) {
/* 948 */     JCTree.JCCatch jCCatch = (JCTree.JCCatch)paramCatchTree;
/* 949 */     JCTree.JCVariableDecl jCVariableDecl = jCCatch.param;
/* 950 */     if (jCVariableDecl.type != null && jCVariableDecl.type.getKind() == TypeKind.UNION) {
/* 951 */       Type.UnionClassType unionClassType = (Type.UnionClassType)jCVariableDecl.type;
/* 952 */       return (TypeMirror)unionClassType.getLub();
/*     */     } 
/* 954 */     return (TypeMirror)jCVariableDecl.type;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\JavacTrees.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */