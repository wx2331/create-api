/*      */ package com.sun.tools.javac.parser;
/*      */ 
/*      */ import com.sun.source.tree.MemberReferenceTree;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.tree.DocCommentTable;
/*      */ import com.sun.tools.javac.tree.EndPosTable;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.IntHashTable;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
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
/*      */ public class JavacParser
/*      */   implements Parser
/*      */ {
/*      */   private static final int infixPrecedenceLevels = 10;
/*      */   protected Lexer S;
/*      */   protected TreeMaker F;
/*      */   private Log log;
/*      */   private Source source;
/*      */   private Names names;
/*      */   private final AbstractEndPosTable endPosTable;
/*  115 */   private List<JCTree.JCAnnotation> typeAnnotationsPushedBack = List.nil(); private boolean permitTypeAnnotationsPushBack = false; boolean allowGenerics; boolean allowDiamond; boolean allowMulticatch; boolean allowVarargs; boolean allowAsserts; boolean allowEnums; boolean allowForeach; boolean allowStaticImport; boolean allowAnnotations; boolean allowTWR; boolean allowStringFolding; boolean allowLambda; boolean allowMethodReferences; boolean allowDefaultMethods; boolean allowStaticInterfaceMethods; boolean allowIntersectionTypesInCast; boolean keepDocComments; boolean keepLineMap; boolean allowTypeAnnotations; boolean allowAnnotationsAfterTypeParams; boolean allowThisIdent; JCTree.JCVariableDecl receiverParam; static final int EXPR = 1;
/*      */   static final int TYPE = 2;
/*      */   static final int NOPARAMS = 4;
/*      */   static final int TYPEARG = 8;
/*      */   static final int DIAMOND = 16;
/*      */   private int mode;
/*      */   private int lastmode;
/*      */   protected Tokens.Token token;
/*      */   private JCTree.JCErroneous errorTree;
/*      */   private int errorPos;
/*      */   private final DocCommentTable docComments;
/*      */   ArrayList<JCTree.JCExpression[]> odStackSupply;
/*      */   ArrayList<Tokens.Token[]> opStackSupply;
/*      */   Filter<Tokens.TokenKind> LAX_IDENTIFIER;
/*      */   
/*  130 */   enum BasicErrorRecoveryAction implements ErrorRecoveryAction { BLOCK_STMT { public JCTree doRecover(JavacParser param2JavacParser) { return (JCTree)param2JavacParser.parseStatementAsBlock(); } },
/*  131 */     CATCH_CLAUSE { public JCTree doRecover(JavacParser param2JavacParser) { return (JCTree)param2JavacParser.catchClause(); }
/*      */        }
/*      */     ; }
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
/*      */   protected AbstractEndPosTable newEndPosTable(boolean paramBoolean) {
/*  173 */     return paramBoolean ? new SimpleEndPosTable(this) : new EmptyEndPosTable(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected DocCommentTable newDocCommentTable(boolean paramBoolean, ParserFactory paramParserFactory) {
/*  179 */     return paramBoolean ? new LazyDocCommentTable(paramParserFactory) : null;
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
/*      */   protected JavacParser(ParserFactory paramParserFactory, Lexer paramLexer, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*  286 */     this.mode = 0;
/*      */ 
/*      */ 
/*      */     
/*  290 */     this.lastmode = 0;
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
/*  439 */     this.errorPos = -1;
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
/* 1057 */     this.odStackSupply = (ArrayList)new ArrayList<>();
/* 1058 */     this.opStackSupply = (ArrayList)new ArrayList<>();
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
/* 1733 */     this.LAX_IDENTIFIER = new Filter<Tokens.TokenKind>()
/*      */       {
/* 1735 */         public boolean accepts(Tokens.TokenKind param1TokenKind) { return (param1TokenKind == Tokens.TokenKind.IDENTIFIER || param1TokenKind == Tokens.TokenKind.UNDERSCORE || param1TokenKind == Tokens.TokenKind.ASSERT || param1TokenKind == Tokens.TokenKind.ENUM); }
/*      */       }; this.S = paramLexer; nextToken(); this.F = paramParserFactory.F; this.log = paramParserFactory.log; this.names = paramParserFactory.names; this.source = paramParserFactory.source; this.allowGenerics = this.source.allowGenerics(); this.allowVarargs = this.source.allowVarargs(); this.allowAsserts = this.source.allowAsserts(); this.allowEnums = this.source.allowEnums(); this.allowForeach = this.source.allowForeach(); this.allowStaticImport = this.source.allowStaticImport(); this.allowAnnotations = this.source.allowAnnotations(); this.allowTWR = this.source.allowTryWithResources(); this.allowDiamond = this.source.allowDiamond(); this.allowMulticatch = this.source.allowMulticatch(); this.allowStringFolding = paramParserFactory.options.getBoolean("allowStringFolding", true); this.allowLambda = this.source.allowLambda(); this.allowMethodReferences = this.source.allowMethodReferences(); this.allowDefaultMethods = this.source.allowDefaultMethods(); this.allowStaticInterfaceMethods = this.source.allowStaticInterfaceMethods(); this.allowIntersectionTypesInCast = this.source.allowIntersectionTypesInCast(); this.allowTypeAnnotations = this.source.allowTypeAnnotations(); this.allowAnnotationsAfterTypeParams = this.source.allowAnnotationsAfterTypeParams(); this.keepDocComments = paramBoolean1; this.docComments = newDocCommentTable(paramBoolean1, paramParserFactory); this.keepLineMap = paramBoolean2; this.errorTree = this.F.Erroneous(); this.endPosTable = newEndPosTable(paramBoolean3);
/*      */   }
/*      */   public Tokens.Token token() { return this.token; } public void nextToken() { this.S.nextToken(); this.token = this.S.token(); } protected boolean peekToken(Filter<Tokens.TokenKind> paramFilter) { return peekToken(0, paramFilter); } protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind> paramFilter) { return paramFilter.accepts((this.S.token(paramInt + 1)).kind); } protected boolean peekToken(Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2) { return peekToken(0, paramFilter1, paramFilter2); } protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2) { return (paramFilter1.accepts((this.S.token(paramInt + 1)).kind) && paramFilter2.accepts((this.S.token(paramInt + 2)).kind)); } protected boolean peekToken(Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2, Filter<Tokens.TokenKind> paramFilter3) { return peekToken(0, paramFilter1, paramFilter2, paramFilter3); } protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind> paramFilter1, Filter<Tokens.TokenKind> paramFilter2, Filter<Tokens.TokenKind> paramFilter3) { return (paramFilter1.accepts((this.S.token(paramInt + 1)).kind) && paramFilter2.accepts((this.S.token(paramInt + 2)).kind) && paramFilter3.accepts((this.S.token(paramInt + 3)).kind)); } protected boolean peekToken(Filter<Tokens.TokenKind>... paramVarArgs) { return peekToken(0, paramVarArgs); } protected boolean peekToken(int paramInt, Filter<Tokens.TokenKind>... paramVarArgs) { for (; paramInt < paramVarArgs.length; paramInt++) { if (!paramVarArgs[paramInt].accepts((this.S.token(paramInt + 1)).kind)) return false;  }  return true; } private void skip(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) { while (true) { switch (this.token.kind) { case REFERENCE: nextToken(); return;case SELECT: case CLASSDEF: case VARDEF: case null: case null: case null: case null: case null: return;case null: if (paramBoolean1) return;  break;case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: if (paramBoolean2) return;  break;case null: case null: if (paramBoolean3) return;  break;case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: if (paramBoolean4) return;  break; }  nextToken(); }  } private JCTree.JCErroneous syntaxError(int paramInt, String paramString, Tokens.TokenKind... paramVarArgs) { return syntaxError(paramInt, List.nil(), paramString, paramVarArgs); } private JCTree.JCErroneous syntaxError(int paramInt, List<JCTree> paramList, String paramString, Tokens.TokenKind... paramVarArgs) { setErrorEndPos(paramInt); JCTree.JCErroneous jCErroneous = this.F.at(paramInt).Erroneous(paramList); reportSyntaxError((JCDiagnostic.DiagnosticPosition)jCErroneous, paramString, (Object[])paramVarArgs); if (paramList != null) { JCTree jCTree = (JCTree)paramList.last(); if (jCTree != null) storeEnd(jCTree, paramInt);  }  return toP(jCErroneous); } private void reportSyntaxError(int paramInt, String paramString, Object... paramVarArgs) { JCDiagnostic.SimpleDiagnosticPosition simpleDiagnosticPosition = new JCDiagnostic.SimpleDiagnosticPosition(paramInt); reportSyntaxError((JCDiagnostic.DiagnosticPosition)simpleDiagnosticPosition, paramString, paramVarArgs); } private void reportSyntaxError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) { int i = paramDiagnosticPosition.getPreferredPosition(); if (i > this.S.errPos() || i == -1) if (this.token.kind == Tokens.TokenKind.EOF) { error(paramDiagnosticPosition, "premature.eof", new Object[0]); } else { error(paramDiagnosticPosition, paramString, paramVarArgs); }   this.S.errPos(i); if (this.token.pos == this.errorPos) nextToken();  this.errorPos = this.token.pos; } private JCTree.JCErroneous syntaxError(String paramString) { return syntaxError(this.token.pos, paramString, new Tokens.TokenKind[0]); } private JCTree.JCErroneous syntaxError(String paramString, Tokens.TokenKind paramTokenKind) { return syntaxError(this.token.pos, paramString, new Tokens.TokenKind[] { paramTokenKind }); } public void accept(Tokens.TokenKind paramTokenKind) { if (this.token.kind == paramTokenKind) { nextToken(); } else { setErrorEndPos(this.token.pos); reportSyntaxError((this.S.prevToken()).endPos, "expected", new Object[] { paramTokenKind }); }  } JCTree.JCExpression illegal(int paramInt) { setErrorEndPos(paramInt); if ((this.mode & 0x1) != 0) return (JCTree.JCExpression)syntaxError(paramInt, "illegal.start.of.expr", new Tokens.TokenKind[0]);  return (JCTree.JCExpression)syntaxError(paramInt, "illegal.start.of.type", new Tokens.TokenKind[0]); } JCTree.JCExpression illegal() { return illegal(this.token.pos); } void checkNoMods(long paramLong) { if (paramLong != 0L) { long l = paramLong & -paramLong; error(this.token.pos, "mod.not.allowed.here", new Object[] { Flags.asFlagSet(l) }); }  } void attach(JCTree paramJCTree, Tokens.Comment paramComment) { if (this.keepDocComments && paramComment != null) this.docComments.putComment(paramJCTree, paramComment);  } private void setErrorEndPos(int paramInt) { this.endPosTable.setErrorEndPos(paramInt); } private void storeEnd(JCTree paramJCTree, int paramInt) { this.endPosTable.storeEnd(paramJCTree, paramInt); } private <T extends JCTree> T to(T paramT) { return this.endPosTable.to(paramT); } private <T extends JCTree> T toP(T paramT) { return this.endPosTable.toP(paramT); } public int getStartPos(JCTree paramJCTree) { return TreeInfo.getStartPos(paramJCTree); } public int getEndPos(JCTree paramJCTree) { return this.endPosTable.getEndPos(paramJCTree); } public Name ident() { if (this.token.kind == Tokens.TokenKind.IDENTIFIER) { Name name = this.token.name(); nextToken(); return name; }  if (this.token.kind == Tokens.TokenKind.ASSERT) { if (this.allowAsserts) { error(this.token.pos, "assert.as.identifier", new Object[0]); nextToken(); return this.names.error; }  warning(this.token.pos, "assert.as.identifier", new Object[0]); Name name = this.token.name(); nextToken(); return name; }  if (this.token.kind == Tokens.TokenKind.ENUM) { if (this.allowEnums) { error(this.token.pos, "enum.as.identifier", new Object[0]); nextToken(); return this.names.error; }  warning(this.token.pos, "enum.as.identifier", new Object[0]); Name name = this.token.name(); nextToken(); return name; }  if (this.token.kind == Tokens.TokenKind.THIS) { if (this.allowThisIdent) { checkTypeAnnotations(); Name name = this.token.name(); nextToken(); return name; }  error(this.token.pos, "this.as.identifier", new Object[0]); nextToken(); return this.names.error; }  if (this.token.kind == Tokens.TokenKind.UNDERSCORE) { warning(this.token.pos, "underscore.as.identifier", new Object[0]); Name name = this.token.name(); nextToken(); return name; }  accept(Tokens.TokenKind.IDENTIFIER); return this.names.error; } public JCTree.JCExpression qualident(boolean paramBoolean) { JCTree.JCExpression jCExpression = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident())); while (this.token.kind == Tokens.TokenKind.DOT) { int i = this.token.pos; nextToken(); List<JCTree.JCAnnotation> list = null; if (paramBoolean) list = typeAnnotationsOpt();  jCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select(jCExpression, ident())); if (list != null && list.nonEmpty()) jCExpression = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)list.head).pos).AnnotatedType(list, jCExpression));  }  return jCExpression; } JCTree.JCExpression literal(Name paramName) { return literal(paramName, this.token.pos); } JCTree.JCExpression literal(Name paramName, int paramInt) { JCTree.JCLiteral jCLiteral; JCTree.JCErroneous jCErroneous1; String str; Float float_; Double double_; JCTree.JCErroneous jCErroneous2 = this.errorTree; switch (this.token.kind) { case null: try { JCTree.JCLiteral jCLiteral1 = this.F.at(paramInt).Literal(TypeTag.INT, Integer.valueOf(Convert.string2int(strval(paramName), this.token.radix()))); } catch (NumberFormatException numberFormatException) { error(this.token.pos, "int.number.too.large", new Object[] { strval(paramName) }); }  break;case null: try { JCTree.JCLiteral jCLiteral1 = this.F.at(paramInt).Literal(TypeTag.LONG, new Long(Convert.string2long(strval(paramName), this.token.radix()))); } catch (NumberFormatException numberFormatException) { error(this.token.pos, "int.number.too.large", new Object[] { strval(paramName) }); }  break;case null: str = (this.token.radix() == 16) ? ("0x" + this.token.stringVal()) : this.token.stringVal(); try { float_ = Float.valueOf(str); } catch (NumberFormatException numberFormatException) { float_ = Float.valueOf(Float.NaN); }  if (float_.floatValue() == 0.0F && !isZero(str)) { error(this.token.pos, "fp.number.too.small", new Object[0]); break; }  if (float_.floatValue() == Float.POSITIVE_INFINITY) { error(this.token.pos, "fp.number.too.large", new Object[0]); break; }  jCLiteral = this.F.at(paramInt).Literal(TypeTag.FLOAT, float_); break;case null: str = (this.token.radix() == 16) ? ("0x" + this.token.stringVal()) : this.token.stringVal(); try { double_ = Double.valueOf(str); } catch (NumberFormatException numberFormatException) { double_ = Double.valueOf(Double.NaN); }  if (double_.doubleValue() == 0.0D && !isZero(str)) { error(this.token.pos, "fp.number.too.small", new Object[0]); break; }  if (double_.doubleValue() == Double.POSITIVE_INFINITY) { error(this.token.pos, "fp.number.too.large", new Object[0]); break; }  jCLiteral = this.F.at(paramInt).Literal(TypeTag.DOUBLE, double_); break;case null: jCLiteral = this.F.at(paramInt).Literal(TypeTag.CHAR, Integer.valueOf(this.token.stringVal().charAt(0) + 0)); break;case null: jCLiteral = this.F.at(paramInt).Literal(TypeTag.CLASS, this.token.stringVal()); break;case null: case null: jCLiteral = this.F.at(paramInt).Literal(TypeTag.BOOLEAN, Integer.valueOf((this.token.kind == Tokens.TokenKind.TRUE) ? 1 : 0)); break;case null: jCLiteral = this.F.at(paramInt).Literal(TypeTag.BOT, null); break;default: Assert.error(); break; }  if (jCLiteral == this.errorTree) jCErroneous1 = this.F.at(paramInt).Erroneous();  storeEnd((JCTree)jCErroneous1, this.token.endPos); nextToken(); return (JCTree.JCExpression)jCErroneous1; } boolean isZero(String paramString) { char[] arrayOfChar = paramString.toCharArray(); byte b1 = (arrayOfChar.length > 1 && Character.toLowerCase(arrayOfChar[1]) == 'x') ? 16 : 10; byte b2 = (b1 == 16) ? 2 : 0; for (; b2 < arrayOfChar.length && (arrayOfChar[b2] == '0' || arrayOfChar[b2] == '.'); b2++); return (b2 >= arrayOfChar.length || Character.digit(arrayOfChar[b2], b1) <= 0); } String strval(Name paramName) { String str = this.token.stringVal(); return paramName.isEmpty() ? str : (paramName + str); } public JCTree.JCExpression parseExpression() { return term(1); } public JCTree.JCExpression parseType() { List<JCTree.JCAnnotation> list = typeAnnotationsOpt(); return parseType(list); } public JCTree.JCExpression parseType(List<JCTree.JCAnnotation> paramList) { JCTree.JCExpression jCExpression = unannotatedType(); if (paramList.nonEmpty()) jCExpression = insertAnnotationsToMostInner(jCExpression, paramList, false);  return jCExpression; } public JCTree.JCExpression unannotatedType() { return term(2); } JCTree.JCExpression term(int paramInt) { int i = this.mode; this.mode = paramInt; JCTree.JCExpression jCExpression = term(); this.lastmode = this.mode; this.mode = i; return jCExpression; } JCTree.JCExpression term() { JCTree.JCExpression jCExpression = term1(); if (((this.mode & 0x1) != 0 && this.token.kind == Tokens.TokenKind.EQ) || (Tokens.TokenKind.PLUSEQ.compareTo(this.token.kind) <= 0 && this.token.kind.compareTo(Tokens.TokenKind.GTGTGTEQ) <= 0)) return termRest(jCExpression);  return jCExpression; } JCTree.JCExpression termRest(JCTree.JCExpression paramJCExpression) { int i; JCTree.JCExpression jCExpression1; Tokens.TokenKind tokenKind; JCTree.JCExpression jCExpression2; switch (this.token.kind) { case null: i = this.token.pos; nextToken(); this.mode = 1; jCExpression1 = term(); return (JCTree.JCExpression)toP(this.F.at(i).Assign(paramJCExpression, jCExpression1));case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: i = this.token.pos; tokenKind = this.token.kind; nextToken(); this.mode = 1; jCExpression2 = term(); return (JCTree.JCExpression)this.F.at(i).Assignop(optag(tokenKind), (JCTree)paramJCExpression, (JCTree)jCExpression2); }  return paramJCExpression; } JCTree.JCExpression term1() { JCTree.JCExpression jCExpression = term2(); if ((this.mode & 0x1) != 0 && this.token.kind == Tokens.TokenKind.QUES) { this.mode = 1; return term1Rest(jCExpression); }  return jCExpression; } JCTree.JCExpression term1Rest(JCTree.JCExpression paramJCExpression) { if (this.token.kind == Tokens.TokenKind.QUES) { int i = this.token.pos; nextToken(); JCTree.JCExpression jCExpression1 = term(); accept(Tokens.TokenKind.COLON); JCTree.JCExpression jCExpression2 = term1(); return (JCTree.JCExpression)this.F.at(i).Conditional(paramJCExpression, jCExpression1, jCExpression2); }  return paramJCExpression; } JCTree.JCExpression term2() { JCTree.JCExpression jCExpression = term3(); if ((this.mode & 0x1) != 0 && prec(this.token.kind) >= 4) { this.mode = 1; return term2Rest(jCExpression, 4); }  return jCExpression; } JCTree.JCExpression term2Rest(JCTree.JCExpression paramJCExpression, int paramInt) { JCTree.JCExpression[] arrayOfJCExpression = newOdStack(); Tokens.Token[] arrayOfToken = newOpStack(); byte b = 0; arrayOfJCExpression[0] = paramJCExpression; int i = this.token.pos; Tokens.Token token = Tokens.DUMMY; while (prec(this.token.kind) >= paramInt) { arrayOfToken[b] = token; b++; token = this.token; nextToken(); arrayOfJCExpression[b] = (token.kind == Tokens.TokenKind.INSTANCEOF) ? parseType() : term3(); while (b > 0 && prec(token.kind) >= prec(this.token.kind)) { arrayOfJCExpression[b - 1] = makeOp(token.pos, token.kind, arrayOfJCExpression[b - 1], arrayOfJCExpression[b]); b--; token = arrayOfToken[b]; }  }  Assert.check((b == 0)); paramJCExpression = arrayOfJCExpression[0]; if (paramJCExpression.hasTag(JCTree.Tag.PLUS)) paramJCExpression = foldStrings(paramJCExpression);  this.odStackSupply.add(arrayOfJCExpression); this.opStackSupply.add(arrayOfToken); return paramJCExpression; } private JCTree.JCExpression makeOp(int paramInt, Tokens.TokenKind paramTokenKind, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) { if (paramTokenKind == Tokens.TokenKind.INSTANCEOF) return (JCTree.JCExpression)this.F.at(paramInt).TypeTest(paramJCExpression1, (JCTree)paramJCExpression2);  return (JCTree.JCExpression)this.F.at(paramInt).Binary(optag(paramTokenKind), paramJCExpression1, paramJCExpression2); } protected JCTree.JCExpression foldStrings(JCTree.JCExpression paramJCExpression) { if (!this.allowStringFolding) return paramJCExpression;  ListBuffer<JCTree.JCExpression> listBuffer = new ListBuffer(); ListBuffer<JCTree.JCLiteral> listBuffer1 = new ListBuffer(); boolean bool = false; JCTree.JCExpression jCExpression = paramJCExpression; while (jCExpression.hasTag(JCTree.Tag.PLUS)) { JCTree.JCBinary jCBinary = (JCTree.JCBinary)jCExpression; bool |= foldIfNeeded(jCBinary.rhs, listBuffer1, listBuffer, false); jCExpression = jCBinary.lhs; }  bool |= foldIfNeeded(jCExpression, listBuffer1, listBuffer, true); if (bool) { JCTree.JCBinary jCBinary; List list = listBuffer.toList(); JCTree.JCExpression jCExpression1 = (JCTree.JCExpression)list.head; for (JCTree.JCExpression jCExpression2 : list.tail) { jCBinary = this.F.at(jCExpression2.getStartPosition()).Binary(optag(Tokens.TokenKind.PLUS), jCExpression1, jCExpression2); storeEnd((JCTree)jCBinary, getEndPos((JCTree)jCExpression2)); }  return (JCTree.JCExpression)jCBinary; }  return paramJCExpression; } private boolean foldIfNeeded(JCTree.JCExpression paramJCExpression, ListBuffer<JCTree.JCLiteral> paramListBuffer, ListBuffer<JCTree.JCExpression> paramListBuffer1, boolean paramBoolean) { JCTree.JCLiteral jCLiteral = stringLiteral((JCTree)paramJCExpression); if (jCLiteral != null) { paramListBuffer.prepend(jCLiteral); return (paramBoolean && merge(paramListBuffer, paramListBuffer1)); }  boolean bool = merge(paramListBuffer, paramListBuffer1); paramListBuffer.clear(); paramListBuffer1.prepend(paramJCExpression); return bool; } boolean merge(ListBuffer<JCTree.JCLiteral> paramListBuffer, ListBuffer<JCTree.JCExpression> paramListBuffer1) { if (paramListBuffer.isEmpty()) return false;  if (paramListBuffer.size() == 1) { paramListBuffer1.prepend(paramListBuffer.first()); return false; }  StringBuilder stringBuilder = new StringBuilder(); for (JCTree.JCLiteral jCLiteral1 : paramListBuffer) stringBuilder.append(jCLiteral1.getValue());  JCTree.JCLiteral jCLiteral = this.F.at(((JCTree.JCLiteral)paramListBuffer.first()).getStartPosition()).Literal(TypeTag.CLASS, stringBuilder.toString()); storeEnd((JCTree)jCLiteral, ((JCTree.JCLiteral)paramListBuffer.last()).getEndPosition(this.endPosTable)); paramListBuffer1.prepend(jCLiteral); return true; } private JCTree.JCLiteral stringLiteral(JCTree paramJCTree) { if (paramJCTree.hasTag(JCTree.Tag.LITERAL)) { JCTree.JCLiteral jCLiteral = (JCTree.JCLiteral)paramJCTree; if (jCLiteral.typetag == TypeTag.CLASS) return jCLiteral;  }  return null; } private JCTree.JCExpression[] newOdStack() { if (this.odStackSupply.isEmpty()) return new JCTree.JCExpression[11];  return this.odStackSupply.remove(this.odStackSupply.size() - 1); } private Tokens.Token[] newOpStack() { if (this.opStackSupply.isEmpty()) return new Tokens.Token[11];  return this.opStackSupply.remove(this.opStackSupply.size() - 1); } protected JCTree.JCExpression term3() { JCTree.JCExpression jCExpression2; JCTree.JCMethodInvocation jCMethodInvocation; JCTree.JCExpression jCExpression1; List<JCTree.JCAnnotation> list1; JCTree.JCExpression jCExpression3; int i = this.token.pos; List<JCTree.JCExpression> list = typeArgumentsOpt(1); switch (this.token.kind) { case null: if ((this.mode & 0x2) != 0 && (this.mode & 0xC) == 8) { this.mode = 2; return typeArgument(); }  return illegal();case null: case null: case null: case null: case null: case null: if (list == null && (this.mode & 0x1) != 0) { Tokens.TokenKind tokenKind = this.token.kind; nextToken(); this.mode = 1; if (tokenKind == Tokens.TokenKind.SUB && (this.token.kind == Tokens.TokenKind.INTLITERAL || this.token.kind == Tokens.TokenKind.LONGLITERAL) && this.token.radix() == 10) { this.mode = 1; jCExpression2 = literal(this.names.hyphen, i); } else { jCExpression2 = term3(); return (JCTree.JCExpression)this.F.at(i).Unary(unoptag(tokenKind), jCExpression2); }  } else { return illegal(); }  return term3Rest(jCExpression2, list);case null: if (list == null && (this.mode & 0x1) != 0) { int j; List list2; JCTree.JCExpression jCExpression; ParensResult parensResult = analyzeParens(); switch (parensResult) { case REFERENCE: accept(Tokens.TokenKind.LPAREN); this.mode = 2; j = i; list2 = List.of(jCExpression2 = term3()); while (this.token.kind == Tokens.TokenKind.AMP) { checkIntersectionTypesInCast(); accept(Tokens.TokenKind.AMP); list2 = list2.prepend(term3()); }  if (list2.length() > 1) jCExpression2 = (JCTree.JCExpression)toP(this.F.at(j).TypeIntersection(list2.reverse()));  accept(Tokens.TokenKind.RPAREN); this.mode = 1; jCExpression = term3(); return (JCTree.JCExpression)this.F.at(i).TypeCast((JCTree)jCExpression2, jCExpression);case SELECT: case CLASSDEF: jCExpression2 = lambdaExpressionOrStatement(true, (parensResult == ParensResult.EXPLICIT_LAMBDA), i); return term3Rest(jCExpression2, list); }  accept(Tokens.TokenKind.LPAREN); this.mode = 1; jCExpression2 = termRest(term1Rest(term2Rest(term3(), 4))); accept(Tokens.TokenKind.RPAREN); jCExpression2 = (JCTree.JCExpression)toP(this.F.at(i).Parens(jCExpression2)); } else { return illegal(); }  return term3Rest(jCExpression2, list);case null: if ((this.mode & 0x1) != 0) { this.mode = 1; jCExpression2 = (JCTree.JCExpression)to(this.F.at(i).Ident(this.names._this)); nextToken(); if (list == null) { jCExpression2 = argumentsOpt(null, jCExpression2); } else { jCMethodInvocation = arguments(list, jCExpression2); }  list = null; } else { return illegal(); }  return term3Rest((JCTree.JCExpression)jCMethodInvocation, list);case null: if ((this.mode & 0x1) != 0) { this.mode = 1; jCExpression1 = (JCTree.JCExpression)to(this.F.at(i).Ident(this.names._super)); jCExpression1 = superSuffix(list, jCExpression1); list = null; } else { return illegal(); }  return term3Rest(jCExpression1, list);case null: case null: case null: case null: case null: case null: case null: case null: case null: if (list == null && (this.mode & 0x1) != 0) { this.mode = 1; jCExpression1 = literal(this.names.empty); } else { return illegal(); }  return term3Rest(jCExpression1, list);case null: if (list != null) return illegal();  if ((this.mode & 0x1) != 0) { this.mode = 1; nextToken(); if (this.token.kind == Tokens.TokenKind.LT) list = typeArguments(false);  jCExpression1 = creator(i, list); list = null; } else { return illegal(); }  return term3Rest(jCExpression1, list);case null: list1 = typeAnnotationsOpt(); if (list1.isEmpty()) throw new AssertionError("Expected type annotations, but found none!");  jCExpression3 = term3(); if ((this.mode & 0x2) == 0) { JCTree.JCMemberReference jCMemberReference1; JCTree.JCMemberReference jCMemberReference2; JCTree.JCFieldAccess jCFieldAccess; switch (jCExpression3.getTag()) { case REFERENCE: jCMemberReference2 = (JCTree.JCMemberReference)jCExpression3; jCMemberReference2.expr = (JCTree.JCExpression)toP(this.F.at(i).AnnotatedType(list1, jCMemberReference2.expr)); jCMemberReference1 = jCMemberReference2; return term3Rest((JCTree.JCExpression)jCMemberReference1, list);case SELECT: jCFieldAccess = (JCTree.JCFieldAccess)jCExpression3; if (jCFieldAccess.name != this.names._class) return illegal();  this.log.error(this.token.pos, "no.annotations.on.dot.class", new Object[0]); return jCExpression3; }  return illegal(((JCTree.JCAnnotation)list1.head).pos); }  jCExpression1 = insertAnnotationsToMostInner(jCExpression3, list1, false); return term3Rest(jCExpression1, list);case null: case null: case null: case null: if (list != null) return illegal();  if ((this.mode & 0x1) != 0 && peekToken(Tokens.TokenKind.ARROW)) { jCExpression1 = lambdaExpressionOrStatement(false, false, i); } else { jCExpression1 = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident())); while (true) { int j; List<JCTree.JCAnnotation> list3; i = this.token.pos; List<JCTree.JCAnnotation> list2 = typeAnnotationsOpt(); if (!list2.isEmpty() && this.token.kind != Tokens.TokenKind.LBRACKET && this.token.kind != Tokens.TokenKind.ELLIPSIS) return illegal(((JCTree.JCAnnotation)list2.head).pos);  switch (this.token.kind) { case null: nextToken(); if (this.token.kind == Tokens.TokenKind.RBRACKET) { nextToken(); jCExpression1 = bracketsOpt(jCExpression1); jCExpression1 = (JCTree.JCExpression)toP(this.F.at(i).TypeArray(jCExpression1)); if (list2.nonEmpty()) jCExpression1 = (JCTree.JCExpression)toP(this.F.at(i).AnnotatedType(list2, jCExpression1));  JCTree.JCExpression jCExpression = bracketsSuffix(jCExpression1); if (jCExpression != jCExpression1 && (list2.nonEmpty() || TreeInfo.containsTypeAnnotation((JCTree)jCExpression1))) syntaxError("no.annotations.on.dot.class");  jCExpression1 = jCExpression; break; }  if ((this.mode & 0x1) != 0) { this.mode = 1; JCTree.JCExpression jCExpression = term(); if (!list2.isEmpty()) jCExpression1 = illegal(((JCTree.JCAnnotation)list2.head).pos);  jCExpression1 = (JCTree.JCExpression)to(this.F.at(i).Indexed(jCExpression1, jCExpression)); }  accept(Tokens.TokenKind.RBRACKET); break;case null: if ((this.mode & 0x1) != 0) { this.mode = 1; JCTree.JCMethodInvocation jCMethodInvocation1 = arguments(list, jCExpression1); if (!list2.isEmpty()) jCExpression1 = illegal(((JCTree.JCAnnotation)list2.head).pos);  list = null; }  break;case null: nextToken(); j = this.mode; this.mode &= 0xFFFFFFFB; list = typeArgumentsOpt(1); this.mode = j; if ((this.mode & 0x1) != 0) { int k; switch (this.token.kind) { case null: if (list != null) return illegal();  this.mode = 1; jCExpression1 = (JCTree.JCExpression)to(this.F.at(i).Select(jCExpression1, this.names._class)); nextToken(); break;case null: if (list != null) return illegal();  this.mode = 1; jCExpression1 = (JCTree.JCExpression)to(this.F.at(i).Select(jCExpression1, this.names._this)); nextToken(); break;case null: this.mode = 1; jCExpression1 = (JCTree.JCExpression)to(this.F.at(i).Select(jCExpression1, this.names._super)); jCExpression1 = superSuffix(list, jCExpression1); list = null; break;case null: if (list != null) return illegal();  this.mode = 1; k = this.token.pos; nextToken(); if (this.token.kind == Tokens.TokenKind.LT) list = typeArguments(false);  jCExpression1 = innerCreator(k, list, jCExpression1); list = null; break; }  }  list3 = null; if ((this.mode & 0x2) != 0 && this.token.kind == Tokens.TokenKind.MONKEYS_AT) list3 = typeAnnotationsOpt();  jCExpression1 = (JCTree.JCExpression)toP(this.F.at(i).Select(jCExpression1, ident())); if (list3 != null && list3.nonEmpty()) jCExpression1 = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)list3.head).pos).AnnotatedType(list3, jCExpression1));  continue;case null: if (this.permitTypeAnnotationsPushBack) { this.typeAnnotationsPushedBack = list2; break; }  if (list2.nonEmpty()) illegal(((JCTree.JCAnnotation)list2.head).pos);  break;case null: if ((this.mode & 0x2) == 0 && isUnboundMemberRef()) { int k = this.token.pos; accept(Tokens.TokenKind.LT); ListBuffer listBuffer = new ListBuffer(); listBuffer.append(typeArgument()); while (this.token.kind == Tokens.TokenKind.COMMA) { nextToken(); listBuffer.append(typeArgument()); }  accept(Tokens.TokenKind.GT); jCExpression1 = (JCTree.JCExpression)toP(this.F.at(k).TypeApply(jCExpression1, listBuffer.toList())); checkGenerics(); while (this.token.kind == Tokens.TokenKind.DOT) { nextToken(); this.mode = 2; jCExpression1 = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Select(jCExpression1, ident())); jCExpression1 = typeArgumentsOpt(jCExpression1); }  jCExpression1 = bracketsOpt(jCExpression1); if (this.token.kind != Tokens.TokenKind.COLCOL) jCExpression1 = illegal();  this.mode = 1; return term3Rest(jCExpression1, list); }  break;default: break; }  if (list != null) illegal();  jCExpression1 = typeArgumentsOpt(jCExpression1); return term3Rest(jCExpression1, list); }  }  if (list != null) illegal();  jCExpression1 = typeArgumentsOpt(jCExpression1); return term3Rest(jCExpression1, list);case null: case null: case null: case null: case null: case null: case null: case null: if (list != null) illegal();  jCExpression1 = bracketsSuffix(bracketsOpt((JCTree.JCExpression)basicType())); return term3Rest(jCExpression1, list);case null: if (list != null) illegal();  if ((this.mode & 0x1) != 0) { nextToken(); if (this.token.kind == Tokens.TokenKind.DOT) { JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTree = toP(this.F.at(i).TypeIdent(TypeTag.VOID)); jCExpression1 = bracketsSuffix((JCTree.JCExpression)jCPrimitiveTypeTree); } else { return illegal(i); }  } else { JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTree = to(this.F.at(i).TypeIdent(TypeTag.VOID)); nextToken(); return (JCTree.JCExpression)jCPrimitiveTypeTree; }  return term3Rest(jCExpression1, list); }  return illegal(); } JCTree.JCExpression term3Rest(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList) { JCTree.JCExpression jCExpression; List<JCTree.JCAnnotation> list; if (paramList != null) illegal();  while (true) { int i = this.token.pos; list = typeAnnotationsOpt(); if (this.token.kind == Tokens.TokenKind.LBRACKET) { nextToken(); if ((this.mode & 0x2) != 0) { int j = this.mode; this.mode = 2; if (this.token.kind == Tokens.TokenKind.RBRACKET) { nextToken(); paramJCExpression = bracketsOpt(paramJCExpression); paramJCExpression = (JCTree.JCExpression)toP(this.F.at(i).TypeArray(paramJCExpression)); if (this.token.kind == Tokens.TokenKind.COLCOL) { this.mode = 1; continue; }  if (list.nonEmpty()) paramJCExpression = (JCTree.JCExpression)toP(this.F.at(i).AnnotatedType(list, paramJCExpression));  return paramJCExpression; }  this.mode = j; }  if ((this.mode & 0x1) != 0) { this.mode = 1; JCTree.JCExpression jCExpression1 = term(); paramJCExpression = (JCTree.JCExpression)to(this.F.at(i).Indexed(paramJCExpression, jCExpression1)); }  accept(Tokens.TokenKind.RBRACKET); continue; }  if (this.token.kind == Tokens.TokenKind.DOT) { JCTree.JCMethodInvocation jCMethodInvocation; nextToken(); paramList = typeArgumentsOpt(1); if (this.token.kind == Tokens.TokenKind.SUPER && (this.mode & 0x1) != 0) { this.mode = 1; paramJCExpression = (JCTree.JCExpression)to(this.F.at(i).Select(paramJCExpression, this.names._super)); nextToken(); jCMethodInvocation = arguments(paramList, paramJCExpression); paramList = null; continue; }  if (this.token.kind == Tokens.TokenKind.NEW && (this.mode & 0x1) != 0) { if (paramList != null) return illegal();  this.mode = 1; int j = this.token.pos; nextToken(); if (this.token.kind == Tokens.TokenKind.LT) paramList = typeArguments(false);  jCExpression = innerCreator(j, paramList, (JCTree.JCExpression)jCMethodInvocation); paramList = null; continue; }  List<JCTree.JCAnnotation> list1 = null; if ((this.mode & 0x2) != 0 && this.token.kind == Tokens.TokenKind.MONKEYS_AT) list1 = typeAnnotationsOpt();  jCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select(jCExpression, ident())); if (list1 != null && list1.nonEmpty()) jCExpression = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)list1.head).pos).AnnotatedType(list1, jCExpression));  jCExpression = argumentsOpt(paramList, typeArgumentsOpt(jCExpression)); paramList = null; continue; }  if ((this.mode & 0x1) != 0 && this.token.kind == Tokens.TokenKind.COLCOL) { this.mode = 1; if (paramList != null) return illegal();  accept(Tokens.TokenKind.COLCOL); jCExpression = memberReferenceSuffix(i, jCExpression); continue; }  break; }  if (!list.isEmpty()) if (this.permitTypeAnnotationsPushBack) { this.typeAnnotationsPushedBack = list; } else { return illegal(((JCTree.JCAnnotation)list.head).pos); }   while ((this.token.kind == Tokens.TokenKind.PLUSPLUS || this.token.kind == Tokens.TokenKind.SUBSUB) && (this.mode & 0x1) != 0) { this.mode = 1; jCExpression = (JCTree.JCExpression)to(this.F.at(this.token.pos).Unary((this.token.kind == Tokens.TokenKind.PLUSPLUS) ? JCTree.Tag.POSTINC : JCTree.Tag.POSTDEC, jCExpression)); nextToken(); }  return toP(jCExpression); } boolean isUnboundMemberRef() { byte b1 = 0, b2 = 0; for (Tokens.Token token = this.S.token(b1);; token = this.S.token(++b1)) { byte b; switch (token.kind) { case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: break;case null: b = 0; for (;; b1++) { Tokens.TokenKind tokenKind = (this.S.token(b1)).kind; switch (tokenKind) { case null: return false;case null: b++; break;case null: b--; if (b == 0) break;  break; }  }  break;case null: b2++; break;case null: b2--;case null: b2--;case null: b2--; if (b2 == 0) { Tokens.TokenKind tokenKind = (this.S.token(b1 + 1)).kind; return (tokenKind == Tokens.TokenKind.DOT || tokenKind == Tokens.TokenKind.LBRACKET || tokenKind == Tokens.TokenKind.COLCOL); }  break;default: return false; }  }  } ParensResult analyzeParens() { byte b1 = 0; boolean bool = false; for (byte b2 = 0;; b2++) { Tokens.TokenKind tokenKind = (this.S.token(b2)).kind; switch (tokenKind) { case null: bool = true; break;case null: case null: case null: case null: break;case null: if (peekToken(b2, Tokens.TokenKind.EXTENDS) || peekToken(b2, Tokens.TokenKind.SUPER)) bool = true;  break;case null: case null: case null: case null: case null: case null: case null: case null: if (peekToken(b2, Tokens.TokenKind.RPAREN)) return ParensResult.CAST;  if (peekToken(b2, this.LAX_IDENTIFIER)) return ParensResult.EXPLICIT_LAMBDA;  break;case null: if (b2) return ParensResult.PARENS;  if (peekToken(b2, Tokens.TokenKind.RPAREN)) return ParensResult.EXPLICIT_LAMBDA;  break;case null: if (bool) return ParensResult.CAST;  switch ((this.S.token(b2 + 1)).kind) { case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: return ParensResult.CAST; }  return ParensResult.PARENS;case null: case null: case null: case null: if (peekToken(b2, this.LAX_IDENTIFIER)) return ParensResult.EXPLICIT_LAMBDA;  if (peekToken(b2, Tokens.TokenKind.RPAREN, Tokens.TokenKind.ARROW)) return ParensResult.IMPLICIT_LAMBDA;  bool = false; break;case CLASSDEF: case null: return ParensResult.EXPLICIT_LAMBDA;case null: bool = true; b2++; while (peekToken(b2, Tokens.TokenKind.DOT)) b2 += 2;  if (peekToken(b2, Tokens.TokenKind.LPAREN)) { b2++; byte b = 0; for (;; b2++) { Tokens.TokenKind tokenKind1 = (this.S.token(b2)).kind; switch (tokenKind1) { case null: return ParensResult.PARENS;case null: b++; break;case null: b--; if (b == 0) break;  break; }  }  }  break;case null: if (peekToken(b2, Tokens.TokenKind.RBRACKET, this.LAX_IDENTIFIER)) return ParensResult.EXPLICIT_LAMBDA;  if (peekToken(b2, Tokens.TokenKind.RBRACKET, Tokens.TokenKind.RPAREN) || peekToken(b2, Tokens.TokenKind.RBRACKET, Tokens.TokenKind.AMP)) return ParensResult.CAST;  if (peekToken(b2, Tokens.TokenKind.RBRACKET)) { bool = true; b2++; break; }  return ParensResult.PARENS;case null: b1++; break;case null: b1--;case null: b1--;case null: b1--; if (b1 == 0) { if (peekToken(b2, Tokens.TokenKind.RPAREN) || peekToken(b2, Tokens.TokenKind.AMP)) return ParensResult.CAST;  if (peekToken(b2, this.LAX_IDENTIFIER, Tokens.TokenKind.COMMA) || peekToken(b2, this.LAX_IDENTIFIER, Tokens.TokenKind.RPAREN, Tokens.TokenKind.ARROW) || peekToken(b2, Tokens.TokenKind.ELLIPSIS)) return ParensResult.EXPLICIT_LAMBDA;  bool = true; break; }  if (b1 < 0) return ParensResult.PARENS;  break;default: return ParensResult.PARENS; }  }  } enum ParensResult
/*      */   {
/* 1740 */     CAST,
/* 1741 */     EXPLICIT_LAMBDA,
/* 1742 */     IMPLICIT_LAMBDA,
/* 1743 */     PARENS;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression lambdaExpressionOrStatement(boolean paramBoolean1, boolean paramBoolean2, int paramInt) {
/* 1749 */     List<JCTree.JCVariableDecl> list = paramBoolean2 ? formalParameters(true) : implicitParameters(paramBoolean1);
/*      */     
/* 1751 */     return lambdaExpressionOrStatementRest(list, paramInt);
/*      */   }
/*      */   
/*      */   JCTree.JCExpression lambdaExpressionOrStatementRest(List<JCTree.JCVariableDecl> paramList, int paramInt) {
/* 1755 */     checkLambda();
/* 1756 */     accept(Tokens.TokenKind.ARROW);
/*      */     
/* 1758 */     return (this.token.kind == Tokens.TokenKind.LBRACE) ? 
/* 1759 */       lambdaStatement(paramList, paramInt, paramInt) : 
/* 1760 */       lambdaExpression(paramList, paramInt);
/*      */   }
/*      */   
/*      */   JCTree.JCExpression lambdaStatement(List<JCTree.JCVariableDecl> paramList, int paramInt1, int paramInt2) {
/* 1764 */     JCTree.JCBlock jCBlock = block(paramInt2, 0L);
/* 1765 */     return (JCTree.JCExpression)toP(this.F.at(paramInt1).Lambda(paramList, (JCTree)jCBlock));
/*      */   }
/*      */   
/*      */   JCTree.JCExpression lambdaExpression(List<JCTree.JCVariableDecl> paramList, int paramInt) {
/* 1769 */     JCTree.JCExpression jCExpression = parseExpression();
/* 1770 */     return (JCTree.JCExpression)toP(this.F.at(paramInt).Lambda(paramList, (JCTree)jCExpression));
/*      */   }
/*      */   
/*      */   JCTree.JCExpression superSuffix(List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression) {
/*      */     JCTree.JCMethodInvocation jCMethodInvocation;
/*      */     JCTree.JCExpression jCExpression;
/* 1776 */     nextToken();
/* 1777 */     if (this.token.kind == Tokens.TokenKind.LPAREN || paramList != null) {
/* 1778 */       jCMethodInvocation = arguments(paramList, paramJCExpression);
/* 1779 */     } else if (this.token.kind == Tokens.TokenKind.COLCOL) {
/* 1780 */       if (paramList != null) return illegal(); 
/* 1781 */       jCExpression = memberReferenceSuffix((JCTree.JCExpression)jCMethodInvocation);
/*      */     } else {
/* 1783 */       int i = this.token.pos;
/* 1784 */       accept(Tokens.TokenKind.DOT);
/* 1785 */       paramList = (this.token.kind == Tokens.TokenKind.LT) ? typeArguments(false) : null;
/* 1786 */       jCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select(jCExpression, ident()));
/* 1787 */       jCExpression = argumentsOpt(paramList, jCExpression);
/*      */     } 
/* 1789 */     return jCExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCPrimitiveTypeTree basicType() {
/* 1795 */     JCTree.JCPrimitiveTypeTree jCPrimitiveTypeTree = to(this.F.at(this.token.pos).TypeIdent(typetag(this.token.kind)));
/* 1796 */     nextToken();
/* 1797 */     return jCPrimitiveTypeTree;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression argumentsOpt(List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression) {
/* 1803 */     if (((this.mode & 0x1) != 0 && this.token.kind == Tokens.TokenKind.LPAREN) || paramList != null) {
/* 1804 */       this.mode = 1;
/* 1805 */       return (JCTree.JCExpression)arguments(paramList, paramJCExpression);
/*      */     } 
/* 1807 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCExpression> arguments() {
/* 1814 */     ListBuffer listBuffer = new ListBuffer();
/* 1815 */     if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 1816 */       nextToken();
/* 1817 */       if (this.token.kind != Tokens.TokenKind.RPAREN) {
/* 1818 */         listBuffer.append(parseExpression());
/* 1819 */         while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 1820 */           nextToken();
/* 1821 */           listBuffer.append(parseExpression());
/*      */         } 
/*      */       } 
/* 1824 */       accept(Tokens.TokenKind.RPAREN);
/*      */     } else {
/* 1826 */       syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.LPAREN });
/*      */     } 
/* 1828 */     return listBuffer.toList();
/*      */   }
/*      */   
/*      */   JCTree.JCMethodInvocation arguments(List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression) {
/* 1832 */     int i = this.token.pos;
/* 1833 */     List<JCTree.JCExpression> list = arguments();
/* 1834 */     return toP(this.F.at(i).Apply(paramList, paramJCExpression, list));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression typeArgumentsOpt(JCTree.JCExpression paramJCExpression) {
/* 1840 */     if (this.token.kind == Tokens.TokenKind.LT && (this.mode & 0x2) != 0 && (this.mode & 0x4) == 0) {
/*      */ 
/*      */       
/* 1843 */       this.mode = 2;
/* 1844 */       checkGenerics();
/* 1845 */       return (JCTree.JCExpression)typeArguments(paramJCExpression, false);
/*      */     } 
/* 1847 */     return paramJCExpression;
/*      */   }
/*      */   
/*      */   List<JCTree.JCExpression> typeArgumentsOpt() {
/* 1851 */     return typeArgumentsOpt(2);
/*      */   }
/*      */   
/*      */   List<JCTree.JCExpression> typeArgumentsOpt(int paramInt) {
/* 1855 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 1856 */       checkGenerics();
/* 1857 */       if ((this.mode & paramInt) == 0 || (this.mode & 0x4) != 0)
/*      */       {
/* 1859 */         illegal();
/*      */       }
/* 1861 */       this.mode = paramInt;
/* 1862 */       return typeArguments(false);
/*      */     } 
/* 1864 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCExpression> typeArguments(boolean paramBoolean) {
/* 1873 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 1874 */       nextToken();
/* 1875 */       if (this.token.kind == Tokens.TokenKind.GT && paramBoolean) {
/* 1876 */         checkDiamond();
/* 1877 */         this.mode |= 0x10;
/* 1878 */         nextToken();
/* 1879 */         return List.nil();
/*      */       } 
/* 1881 */       ListBuffer listBuffer = new ListBuffer();
/* 1882 */       listBuffer.append(((this.mode & 0x1) == 0) ? typeArgument() : parseType());
/* 1883 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 1884 */         nextToken();
/* 1885 */         listBuffer.append(((this.mode & 0x1) == 0) ? typeArgument() : parseType());
/*      */       } 
/* 1887 */       switch (this.token.kind) { case null: case null:
/*      */         case null:
/*      */         case null:
/*      */         case null:
/* 1891 */           this.token = this.S.split();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1900 */           return listBuffer.toList();case null: nextToken(); return listBuffer.toList(); }  listBuffer.append(syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.GT })); return listBuffer.toList();
/*      */     } 
/*      */     
/* 1903 */     return List.of(syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.LT }));
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
/*      */   JCTree.JCExpression typeArgument() {
/*      */     JCTree.JCExpression jCExpression;
/* 1916 */     List<JCTree.JCAnnotation> list = typeAnnotationsOpt();
/* 1917 */     if (this.token.kind != Tokens.TokenKind.QUES) return parseType(list); 
/* 1918 */     int i = this.token.pos;
/* 1919 */     nextToken();
/*      */     
/* 1921 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 1922 */       JCTree.TypeBoundKind typeBoundKind = to(this.F.at(i).TypeBoundKind(BoundKind.EXTENDS));
/* 1923 */       nextToken();
/* 1924 */       JCTree.JCExpression jCExpression1 = parseType();
/* 1925 */       JCTree.JCWildcard jCWildcard = this.F.at(i).Wildcard(typeBoundKind, (JCTree)jCExpression1);
/* 1926 */     } else if (this.token.kind == Tokens.TokenKind.SUPER) {
/* 1927 */       JCTree.TypeBoundKind typeBoundKind = to(this.F.at(i).TypeBoundKind(BoundKind.SUPER));
/* 1928 */       nextToken();
/* 1929 */       JCTree.JCExpression jCExpression1 = parseType();
/* 1930 */       JCTree.JCWildcard jCWildcard = this.F.at(i).Wildcard(typeBoundKind, (JCTree)jCExpression1);
/* 1931 */     } else if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/*      */       
/* 1933 */       JCTree.TypeBoundKind typeBoundKind = this.F.at(-1).TypeBoundKind(BoundKind.UNBOUND);
/* 1934 */       JCTree.JCExpression jCExpression1 = (JCTree.JCExpression)toP(this.F.at(i).Wildcard(typeBoundKind, null));
/* 1935 */       JCTree.JCIdent jCIdent = toP(this.F.at(this.token.pos).Ident(ident()));
/* 1936 */       JCTree.JCErroneous jCErroneous2 = this.F.at(i).Erroneous(List.of(jCExpression1, jCIdent));
/* 1937 */       reportSyntaxError((JCDiagnostic.DiagnosticPosition)jCErroneous2, "expected3", new Object[] { Tokens.TokenKind.GT, Tokens.TokenKind.EXTENDS, Tokens.TokenKind.SUPER });
/* 1938 */       JCTree.JCErroneous jCErroneous1 = jCErroneous2;
/*      */     } else {
/* 1940 */       JCTree.TypeBoundKind typeBoundKind = toP(this.F.at(i).TypeBoundKind(BoundKind.UNBOUND));
/* 1941 */       jCExpression = (JCTree.JCExpression)toP(this.F.at(i).Wildcard(typeBoundKind, null));
/*      */     } 
/* 1943 */     if (!list.isEmpty()) {
/* 1944 */       jCExpression = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)list.head).pos).AnnotatedType(list, jCExpression));
/*      */     }
/* 1946 */     return jCExpression;
/*      */   }
/*      */   
/*      */   JCTree.JCTypeApply typeArguments(JCTree.JCExpression paramJCExpression, boolean paramBoolean) {
/* 1950 */     int i = this.token.pos;
/* 1951 */     List<JCTree.JCExpression> list = typeArguments(paramBoolean);
/* 1952 */     return toP(this.F.at(i).TypeApply(paramJCExpression, list));
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
/*      */   private JCTree.JCExpression bracketsOpt(JCTree.JCExpression paramJCExpression, List<JCTree.JCAnnotation> paramList) {
/* 1965 */     List<JCTree.JCAnnotation> list = typeAnnotationsOpt();
/*      */     
/* 1967 */     if (this.token.kind == Tokens.TokenKind.LBRACKET) {
/* 1968 */       int i = this.token.pos;
/* 1969 */       nextToken();
/* 1970 */       paramJCExpression = bracketsOptCont(paramJCExpression, i, list);
/* 1971 */     } else if (!list.isEmpty()) {
/* 1972 */       if (this.permitTypeAnnotationsPushBack) {
/* 1973 */         this.typeAnnotationsPushedBack = list;
/*      */       } else {
/* 1975 */         return illegal(((JCTree.JCAnnotation)list.head).pos);
/*      */       } 
/*      */     } 
/*      */     
/* 1979 */     if (!paramList.isEmpty()) {
/* 1980 */       paramJCExpression = (JCTree.JCExpression)toP(this.F.at(this.token.pos).AnnotatedType(paramList, paramJCExpression));
/*      */     }
/* 1982 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private JCTree.JCExpression bracketsOpt(JCTree.JCExpression paramJCExpression) {
/* 1988 */     return bracketsOpt(paramJCExpression, List.nil());
/*      */   }
/*      */ 
/*      */   
/*      */   private JCTree.JCExpression bracketsOptCont(JCTree.JCExpression paramJCExpression, int paramInt, List<JCTree.JCAnnotation> paramList) {
/* 1993 */     accept(Tokens.TokenKind.RBRACKET);
/* 1994 */     paramJCExpression = bracketsOpt(paramJCExpression);
/* 1995 */     paramJCExpression = (JCTree.JCExpression)toP(this.F.at(paramInt).TypeArray(paramJCExpression));
/* 1996 */     if (paramList.nonEmpty()) {
/* 1997 */       paramJCExpression = (JCTree.JCExpression)toP(this.F.at(paramInt).AnnotatedType(paramList, paramJCExpression));
/*      */     }
/* 1999 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression bracketsSuffix(JCTree.JCExpression paramJCExpression) {
/*      */     JCTree.JCExpression jCExpression;
/* 2006 */     if ((this.mode & 0x1) != 0 && this.token.kind == Tokens.TokenKind.DOT) {
/* 2007 */       JCTree.JCErroneous jCErroneous; this.mode = 1;
/* 2008 */       int i = this.token.pos;
/* 2009 */       nextToken();
/* 2010 */       accept(Tokens.TokenKind.CLASS);
/* 2011 */       if (this.token.pos == this.endPosTable.errorEndPos) {
/*      */         Name name;
/*      */         
/* 2014 */         if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 2015 */           name = this.token.name();
/* 2016 */           nextToken();
/*      */         } else {
/* 2018 */           name = this.names.error;
/*      */         } 
/* 2020 */         jCErroneous = this.F.at(i).Erroneous(List.of(toP(this.F.at(i).Select(paramJCExpression, name))));
/*      */       } else {
/* 2022 */         jCExpression = (JCTree.JCExpression)toP(this.F.at(i).Select((JCTree.JCExpression)jCErroneous, this.names._class));
/*      */       } 
/* 2024 */     } else if ((this.mode & 0x2) != 0) {
/* 2025 */       if (this.token.kind != Tokens.TokenKind.COLCOL) {
/* 2026 */         this.mode = 2;
/*      */       }
/* 2028 */     } else if (this.token.kind != Tokens.TokenKind.COLCOL) {
/* 2029 */       syntaxError(this.token.pos, "dot.class.expected", new Tokens.TokenKind[0]);
/*      */     } 
/* 2031 */     return jCExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression memberReferenceSuffix(JCTree.JCExpression paramJCExpression) {
/* 2039 */     int i = this.token.pos;
/* 2040 */     accept(Tokens.TokenKind.COLCOL);
/* 2041 */     return memberReferenceSuffix(i, paramJCExpression);
/*      */   } JCTree.JCExpression memberReferenceSuffix(int paramInt, JCTree.JCExpression paramJCExpression) {
/*      */     Name name;
/*      */     MemberReferenceTree.ReferenceMode referenceMode;
/* 2045 */     checkMethodReferences();
/* 2046 */     this.mode = 1;
/* 2047 */     List<JCTree.JCExpression> list = null;
/* 2048 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 2049 */       list = typeArguments(false);
/*      */     }
/*      */ 
/*      */     
/* 2053 */     if (this.token.kind == Tokens.TokenKind.NEW) {
/* 2054 */       referenceMode = MemberReferenceTree.ReferenceMode.NEW;
/* 2055 */       name = this.names.init;
/* 2056 */       nextToken();
/*      */     } else {
/* 2058 */       referenceMode = MemberReferenceTree.ReferenceMode.INVOKE;
/* 2059 */       name = ident();
/*      */     } 
/* 2061 */     return (JCTree.JCExpression)toP(this.F.at(paramJCExpression.getStartPosition()).Reference(referenceMode, name, paramJCExpression, list));
/*      */   }
/*      */ 
/*      */   
/*      */   JCTree.JCExpression creator(int paramInt, List<JCTree.JCExpression> paramList) {
/*      */     JCTree.JCTypeApply jCTypeApply;
/* 2067 */     List<JCTree.JCAnnotation> list = typeAnnotationsOpt();
/*      */     
/* 2069 */     switch (this.token.kind) { case null: case null: case null: case null: case null: case null:
/*      */       case null:
/*      */       case null:
/* 2072 */         if (paramList == null) {
/* 2073 */           if (list.isEmpty()) {
/* 2074 */             return arrayCreatorRest(paramInt, (JCTree.JCExpression)basicType());
/*      */           }
/* 2076 */           return arrayCreatorRest(paramInt, (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)list.head).pos).AnnotatedType(list, (JCTree.JCExpression)basicType())));
/*      */         } 
/*      */         break; }
/*      */ 
/*      */ 
/*      */     
/* 2082 */     JCTree.JCExpression jCExpression2 = qualident(true);
/*      */     
/* 2084 */     int i = this.mode;
/* 2085 */     this.mode = 2;
/* 2086 */     boolean bool = false;
/* 2087 */     int j = -1;
/* 2088 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 2089 */       checkGenerics();
/* 2090 */       j = this.token.pos;
/* 2091 */       jCTypeApply = typeArguments(jCExpression2, true);
/* 2092 */       bool = ((this.mode & 0x10) != 0) ? true : false;
/*      */     } 
/* 2094 */     while (this.token.kind == Tokens.TokenKind.DOT) {
/* 2095 */       if (bool)
/*      */       {
/* 2097 */         illegal();
/*      */       }
/* 2099 */       int k = this.token.pos;
/* 2100 */       nextToken();
/* 2101 */       List<JCTree.JCAnnotation> list1 = typeAnnotationsOpt();
/* 2102 */       jCExpression1 = (JCTree.JCExpression)toP(this.F.at(k).Select((JCTree.JCExpression)jCTypeApply, ident()));
/*      */       
/* 2104 */       if (list1 != null && list1.nonEmpty()) {
/* 2105 */         jCExpression1 = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)list1.head).pos).AnnotatedType(list1, jCExpression1));
/*      */       }
/*      */       
/* 2108 */       if (this.token.kind == Tokens.TokenKind.LT) {
/* 2109 */         j = this.token.pos;
/* 2110 */         checkGenerics();
/* 2111 */         jCTypeApply = typeArguments(jCExpression1, true);
/* 2112 */         bool = ((this.mode & 0x10) != 0) ? true : false;
/*      */       } 
/*      */     } 
/* 2115 */     this.mode = i;
/* 2116 */     if (this.token.kind == Tokens.TokenKind.LBRACKET || this.token.kind == Tokens.TokenKind.MONKEYS_AT) {
/*      */       
/* 2118 */       if (list.nonEmpty()) {
/* 2119 */         jCExpression1 = insertAnnotationsToMostInner((JCTree.JCExpression)jCTypeApply, list, false);
/*      */       }
/*      */       
/* 2122 */       JCTree.JCExpression jCExpression = arrayCreatorRest(paramInt, jCExpression1);
/* 2123 */       if (bool) {
/* 2124 */         reportSyntaxError(j, "cannot.create.array.with.diamond", new Object[0]);
/* 2125 */         return (JCTree.JCExpression)toP(this.F.at(paramInt).Erroneous(List.of(jCExpression)));
/*      */       } 
/* 2127 */       if (paramList != null) {
/* 2128 */         int k = paramInt;
/* 2129 */         if (!paramList.isEmpty() && ((JCTree.JCExpression)paramList.head).pos != -1)
/*      */         {
/*      */ 
/*      */           
/* 2133 */           k = ((JCTree.JCExpression)paramList.head).pos;
/*      */         }
/* 2135 */         setErrorEndPos((this.S.prevToken()).endPos);
/* 2136 */         JCTree.JCErroneous jCErroneous = this.F.at(k).Erroneous(paramList.prepend(jCExpression));
/* 2137 */         reportSyntaxError((JCDiagnostic.DiagnosticPosition)jCErroneous, "cannot.create.array.with.type.arguments", new Object[0]);
/* 2138 */         return (JCTree.JCExpression)toP(jCErroneous);
/*      */       } 
/* 2140 */       return jCExpression;
/* 2141 */     }  if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 2142 */       JCTree.JCNewClass jCNewClass = classCreatorRest(paramInt, null, paramList, jCExpression1);
/* 2143 */       if (jCNewClass.def != null) {
/* 2144 */         assert jCNewClass.def.mods.annotations.isEmpty();
/* 2145 */         if (list.nonEmpty())
/*      */         {
/*      */ 
/*      */ 
/*      */           
/* 2150 */           jCNewClass.def.mods.pos = earlier(jCNewClass.def.mods.pos, ((JCTree.JCAnnotation)list.head).pos);
/* 2151 */           jCNewClass.def.mods.annotations = list;
/*      */         }
/*      */       
/*      */       }
/* 2155 */       else if (list.nonEmpty()) {
/* 2156 */         jCExpression1 = insertAnnotationsToMostInner(jCExpression1, list, false);
/* 2157 */         jCNewClass.clazz = jCExpression1;
/*      */       } 
/*      */       
/* 2160 */       return (JCTree.JCExpression)jCNewClass;
/*      */     } 
/* 2162 */     setErrorEndPos(this.token.pos);
/* 2163 */     reportSyntaxError(this.token.pos, "expected2", new Object[] { Tokens.TokenKind.LPAREN, Tokens.TokenKind.LBRACKET });
/* 2164 */     JCTree.JCExpression jCExpression1 = (JCTree.JCExpression)toP(this.F.at(paramInt).NewClass(null, paramList, jCExpression1, List.nil(), null));
/* 2165 */     return (JCTree.JCExpression)toP(this.F.at(paramInt).Erroneous(List.of(jCExpression1)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression innerCreator(int paramInt, List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression) {
/*      */     JCTree.JCTypeApply jCTypeApply;
/* 2172 */     List<JCTree.JCAnnotation> list = typeAnnotationsOpt();
/*      */     
/* 2174 */     JCTree.JCExpression jCExpression = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident()));
/*      */     
/* 2176 */     if (list.nonEmpty()) {
/* 2177 */       jCExpression = (JCTree.JCExpression)toP(this.F.at(((JCTree.JCAnnotation)list.head).pos).AnnotatedType(list, jCExpression));
/*      */     }
/*      */     
/* 2180 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 2181 */       int i = this.mode;
/* 2182 */       checkGenerics();
/* 2183 */       jCTypeApply = typeArguments(jCExpression, true);
/* 2184 */       this.mode = i;
/*      */     } 
/* 2186 */     return (JCTree.JCExpression)classCreatorRest(paramInt, paramJCExpression, paramList, (JCTree.JCExpression)jCTypeApply);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression arrayCreatorRest(int paramInt, JCTree.JCExpression paramJCExpression) {
/* 2193 */     List<JCTree.JCAnnotation> list = typeAnnotationsOpt();
/*      */     
/* 2195 */     accept(Tokens.TokenKind.LBRACKET);
/* 2196 */     if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 2197 */       accept(Tokens.TokenKind.RBRACKET);
/* 2198 */       paramJCExpression = bracketsOpt(paramJCExpression, list);
/* 2199 */       if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 2200 */         JCTree.JCNewArray jCNewArray1 = (JCTree.JCNewArray)arrayInitializer(paramInt, paramJCExpression);
/* 2201 */         if (list.nonEmpty()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2208 */           JCTree.JCAnnotatedType jCAnnotatedType = (JCTree.JCAnnotatedType)paramJCExpression;
/* 2209 */           assert jCAnnotatedType.annotations == list;
/* 2210 */           jCNewArray1.annotations = jCAnnotatedType.annotations;
/* 2211 */           jCNewArray1.elemtype = jCAnnotatedType.underlyingType;
/*      */         } 
/* 2213 */         return (JCTree.JCExpression)jCNewArray1;
/*      */       } 
/* 2215 */       JCTree.JCExpression jCExpression = (JCTree.JCExpression)toP(this.F.at(paramInt).NewArray(paramJCExpression, List.nil(), null));
/* 2216 */       return (JCTree.JCExpression)syntaxError(this.token.pos, List.of(jCExpression), "array.dimension.missing", new Tokens.TokenKind[0]);
/*      */     } 
/*      */     
/* 2219 */     ListBuffer listBuffer1 = new ListBuffer();
/*      */ 
/*      */     
/* 2222 */     ListBuffer listBuffer2 = new ListBuffer();
/* 2223 */     listBuffer2.append(list);
/*      */     
/* 2225 */     listBuffer1.append(parseExpression());
/* 2226 */     accept(Tokens.TokenKind.RBRACKET);
/* 2227 */     while (this.token.kind == Tokens.TokenKind.LBRACKET || this.token.kind == Tokens.TokenKind.MONKEYS_AT) {
/*      */       
/* 2229 */       List<JCTree.JCAnnotation> list1 = typeAnnotationsOpt();
/* 2230 */       int i = this.token.pos;
/* 2231 */       nextToken();
/* 2232 */       if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 2233 */         paramJCExpression = bracketsOptCont(paramJCExpression, i, list1); continue;
/*      */       } 
/* 2235 */       if (this.token.kind == Tokens.TokenKind.RBRACKET) {
/* 2236 */         paramJCExpression = bracketsOptCont(paramJCExpression, i, list1); continue;
/*      */       } 
/* 2238 */       listBuffer2.append(list1);
/* 2239 */       listBuffer1.append(parseExpression());
/* 2240 */       accept(Tokens.TokenKind.RBRACKET);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2245 */     JCTree.JCNewArray jCNewArray = toP(this.F.at(paramInt).NewArray(paramJCExpression, listBuffer1.toList(), null));
/* 2246 */     jCNewArray.dimAnnotations = listBuffer2.toList();
/* 2247 */     return (JCTree.JCExpression)jCNewArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCNewClass classCreatorRest(int paramInt, JCTree.JCExpression paramJCExpression1, List<JCTree.JCExpression> paramList, JCTree.JCExpression paramJCExpression2) {
/* 2258 */     List<JCTree.JCExpression> list = arguments();
/* 2259 */     JCTree.JCClassDecl jCClassDecl = null;
/* 2260 */     if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 2261 */       int i = this.token.pos;
/* 2262 */       List<JCTree> list1 = classOrInterfaceBody(this.names.empty, false);
/* 2263 */       JCTree.JCModifiers jCModifiers = this.F.at(-1).Modifiers(0L);
/* 2264 */       jCClassDecl = toP(this.F.at(i).AnonymousClassDef(jCModifiers, list1));
/*      */     } 
/* 2266 */     return toP(this.F.at(paramInt).NewClass(paramJCExpression1, paramList, paramJCExpression2, list, jCClassDecl));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression arrayInitializer(int paramInt, JCTree.JCExpression paramJCExpression) {
/* 2272 */     accept(Tokens.TokenKind.LBRACE);
/* 2273 */     ListBuffer listBuffer = new ListBuffer();
/* 2274 */     if (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2275 */       nextToken();
/* 2276 */     } else if (this.token.kind != Tokens.TokenKind.RBRACE) {
/* 2277 */       listBuffer.append(variableInitializer());
/* 2278 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2279 */         nextToken();
/* 2280 */         if (this.token.kind == Tokens.TokenKind.RBRACE)
/* 2281 */           break;  listBuffer.append(variableInitializer());
/*      */       } 
/*      */     } 
/* 2284 */     accept(Tokens.TokenKind.RBRACE);
/* 2285 */     return (JCTree.JCExpression)toP(this.F.at(paramInt).NewArray(paramJCExpression, List.nil(), listBuffer.toList()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JCTree.JCExpression variableInitializer() {
/* 2291 */     return (this.token.kind == Tokens.TokenKind.LBRACE) ? arrayInitializer(this.token.pos, null) : parseExpression();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression parExpression() {
/* 2297 */     int i = this.token.pos;
/* 2298 */     accept(Tokens.TokenKind.LPAREN);
/* 2299 */     JCTree.JCExpression jCExpression = parseExpression();
/* 2300 */     accept(Tokens.TokenKind.RPAREN);
/* 2301 */     return (JCTree.JCExpression)toP(this.F.at(i).Parens(jCExpression));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCBlock block(int paramInt, long paramLong) {
/* 2307 */     accept(Tokens.TokenKind.LBRACE);
/* 2308 */     List<JCTree.JCStatement> list = blockStatements();
/* 2309 */     JCTree.JCBlock jCBlock = this.F.at(paramInt).Block(paramLong, list);
/* 2310 */     while (this.token.kind == Tokens.TokenKind.CASE || this.token.kind == Tokens.TokenKind.DEFAULT) {
/* 2311 */       syntaxError("orphaned", this.token.kind);
/* 2312 */       switchBlockStatementGroups();
/*      */     } 
/*      */ 
/*      */     
/* 2316 */     jCBlock.endpos = this.token.pos;
/* 2317 */     accept(Tokens.TokenKind.RBRACE);
/* 2318 */     return toP(jCBlock);
/*      */   }
/*      */   
/*      */   public JCTree.JCBlock block() {
/* 2322 */     return block(this.token.pos, 0L);
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
/*      */   List<JCTree.JCStatement> blockStatements() {
/* 2335 */     ListBuffer listBuffer = new ListBuffer();
/*      */     while (true) {
/* 2337 */       List<JCTree.JCStatement> list = blockStatement();
/* 2338 */       if (list.isEmpty()) {
/* 2339 */         return listBuffer.toList();
/*      */       }
/* 2341 */       if (this.token.pos <= this.endPosTable.errorEndPos) {
/* 2342 */         skip(false, true, true, true);
/*      */       }
/* 2344 */       listBuffer.addAll((Collection)list);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCStatement parseStatementAsBlock() {
/* 2355 */     int i = this.token.pos;
/* 2356 */     List<JCTree.JCStatement> list = blockStatement();
/* 2357 */     if (list.isEmpty()) {
/* 2358 */       JCTree.JCErroneous jCErroneous = this.F.at(i).Erroneous();
/* 2359 */       error((JCDiagnostic.DiagnosticPosition)jCErroneous, "illegal.start.of.stmt", new Object[0]);
/* 2360 */       return (JCTree.JCStatement)this.F.at(i).Exec((JCTree.JCExpression)jCErroneous);
/*      */     } 
/* 2362 */     JCTree.JCStatement jCStatement = (JCTree.JCStatement)list.head;
/* 2363 */     String str = null;
/* 2364 */     switch (jCStatement.getTag()) {
/*      */       case CLASSDEF:
/* 2366 */         str = "class.not.allowed";
/*      */         break;
/*      */       case VARDEF:
/* 2369 */         str = "variable.not.allowed";
/*      */         break;
/*      */     } 
/* 2372 */     if (str != null) {
/* 2373 */       error((JCDiagnostic.DiagnosticPosition)jCStatement, str, new Object[0]);
/* 2374 */       List list1 = List.of(this.F.at(jCStatement.pos).Block(0L, list));
/* 2375 */       return (JCTree.JCStatement)toP(this.F.at(i).Exec((JCTree.JCExpression)this.F.at(jCStatement.pos).Erroneous(list1)));
/*      */     } 
/* 2377 */     return jCStatement;
/*      */   }
/*      */   
/*      */   List<JCTree.JCStatement> blockStatement() {
/*      */     Tokens.Comment comment;
/*      */     JCTree.JCModifiers jCModifiers;
/*      */     ListBuffer listBuffer;
/* 2384 */     int i = this.token.pos;
/* 2385 */     switch (this.token.kind) { case null: case null: case null:
/*      */       case null:
/* 2387 */         return List.nil();
/*      */       case REFERENCE: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null: case null:
/*      */       case null:
/*      */       case null:
/* 2391 */         return List.of(parseStatement());
/*      */       case CLASSDEF:
/*      */       case null:
/* 2394 */         comment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2395 */         jCModifiers = modifiersOpt();
/* 2396 */         if (this.token.kind == Tokens.TokenKind.INTERFACE || this.token.kind == Tokens.TokenKind.CLASS || (this.allowEnums && this.token.kind == Tokens.TokenKind.ENUM))
/*      */         {
/*      */           
/* 2399 */           return List.of(classOrInterfaceOrEnumDeclaration(jCModifiers, comment));
/*      */         }
/* 2401 */         jCExpression = parseType();
/*      */         
/* 2403 */         listBuffer = variableDeclarators(jCModifiers, jCExpression, new ListBuffer());
/*      */         
/* 2405 */         storeEnd((JCTree)listBuffer.last(), this.token.endPos);
/* 2406 */         accept(Tokens.TokenKind.SEMI);
/* 2407 */         return listBuffer.toList();
/*      */       
/*      */       case VARDEF:
/*      */       case null:
/* 2411 */         comment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2412 */         jCModifiers = modifiersOpt();
/* 2413 */         return List.of(classOrInterfaceOrEnumDeclaration(jCModifiers, comment));
/*      */       
/*      */       case null:
/*      */       case null:
/* 2417 */         comment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2418 */         return List.of(classOrInterfaceOrEnumDeclaration(modifiersOpt(), comment));
/*      */       case null:
/*      */       case null:
/* 2421 */         if (this.allowEnums && this.token.kind == Tokens.TokenKind.ENUM) {
/* 2422 */           error(this.token.pos, "local.enum", new Object[0]);
/* 2423 */           comment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 2424 */           return List.of(classOrInterfaceOrEnumDeclaration(modifiersOpt(), comment));
/* 2425 */         }  if (this.allowAsserts && this.token.kind == Tokens.TokenKind.ASSERT) {
/* 2426 */           return List.of(parseStatement());
/*      */         }
/*      */         break; }
/*      */     
/* 2430 */     Tokens.Token token = this.token;
/* 2431 */     JCTree.JCExpression jCExpression = term(3);
/* 2432 */     if (this.token.kind == Tokens.TokenKind.COLON && jCExpression.hasTag(JCTree.Tag.IDENT)) {
/* 2433 */       nextToken();
/* 2434 */       JCTree.JCStatement jCStatement = parseStatement();
/* 2435 */       return List.of(this.F.at(i).Labelled(token.name(), jCStatement));
/* 2436 */     }  if ((this.lastmode & 0x2) != 0 && this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 2437 */       i = this.token.pos;
/* 2438 */       JCTree.JCModifiers jCModifiers1 = this.F.at(-1).Modifiers(0L);
/* 2439 */       this.F.at(i);
/*      */       
/* 2441 */       ListBuffer listBuffer1 = (ListBuffer)variableDeclarators(jCModifiers1, jCExpression, new ListBuffer());
/*      */       
/* 2443 */       storeEnd((JCTree)listBuffer1.last(), this.token.endPos);
/* 2444 */       accept(Tokens.TokenKind.SEMI);
/* 2445 */       return listBuffer1.toList();
/*      */     } 
/*      */     
/* 2448 */     JCTree.JCExpressionStatement jCExpressionStatement = to(this.F.at(i).Exec(checkExprStat(jCExpression)));
/* 2449 */     accept(Tokens.TokenKind.SEMI);
/* 2450 */     return List.of(jCExpressionStatement); } public JCTree.JCStatement parseStatement() { JCTree.JCExpression jCExpression3;
/*      */     List list1;
/*      */     JCTree.JCExpression jCExpression2;
/*      */     JCTree.JCStatement jCStatement1;
/*      */     List<JCTree> list;
/*      */     JCTree.JCExpression jCExpression1;
/*      */     Name name;
/*      */     int j;
/*      */     JCTree.JCStatement jCStatement3;
/*      */     JCTree.JCExpression jCExpression5;
/*      */     JCTree.JCStatement jCStatement2;
/*      */     JCTree.JCExpression jCExpression4;
/*      */     JCTree.JCBlock jCBlock2;
/*      */     List<JCTree.JCCase> list2;
/*      */     JCTree.JCBlock jCBlock1;
/*      */     JCTree.JCReturn jCReturn;
/*      */     JCTree.JCThrow jCThrow;
/*      */     JCTree.JCBreak jCBreak;
/*      */     JCTree.JCContinue jCContinue;
/*      */     int k;
/*      */     JCTree.JCStatement jCStatement4;
/*      */     List list3;
/*      */     JCTree.JCDoWhileLoop jCDoWhileLoop;
/*      */     ListBuffer listBuffer;
/*      */     JCTree.JCSwitch jCSwitch;
/*      */     JCTree.JCStatement jCStatement5;
/*      */     JCTree.JCBlock jCBlock3;
/* 2477 */     int i = this.token.pos;
/* 2478 */     switch (this.token.kind) {
/*      */       case null:
/* 2480 */         return (JCTree.JCStatement)block();
/*      */       case null:
/* 2482 */         nextToken();
/* 2483 */         jCExpression3 = parExpression();
/* 2484 */         jCStatement3 = parseStatementAsBlock();
/* 2485 */         jCStatement4 = null;
/* 2486 */         if (this.token.kind == Tokens.TokenKind.ELSE) {
/* 2487 */           nextToken();
/* 2488 */           jCStatement4 = parseStatementAsBlock();
/*      */         } 
/* 2490 */         return (JCTree.JCStatement)this.F.at(i).If(jCExpression3, jCStatement3, jCStatement4);
/*      */       
/*      */       case null:
/* 2493 */         nextToken();
/* 2494 */         accept(Tokens.TokenKind.LPAREN);
/* 2495 */         list1 = (this.token.kind == Tokens.TokenKind.SEMI) ? List.nil() : forInit();
/* 2496 */         if (list1.length() == 1 && ((JCTree.JCStatement)list1.head)
/* 2497 */           .hasTag(JCTree.Tag.VARDEF) && ((JCTree.JCVariableDecl)list1.head).init == null && this.token.kind == Tokens.TokenKind.COLON) {
/*      */ 
/*      */           
/* 2500 */           checkForeach();
/* 2501 */           JCTree.JCVariableDecl jCVariableDecl = (JCTree.JCVariableDecl)list1.head;
/* 2502 */           accept(Tokens.TokenKind.COLON);
/* 2503 */           JCTree.JCExpression jCExpression = parseExpression();
/* 2504 */           accept(Tokens.TokenKind.RPAREN);
/* 2505 */           JCTree.JCStatement jCStatement = parseStatementAsBlock();
/* 2506 */           return (JCTree.JCStatement)this.F.at(i).ForeachLoop(jCVariableDecl, jCExpression, jCStatement);
/*      */         } 
/* 2508 */         accept(Tokens.TokenKind.SEMI);
/* 2509 */         jCExpression5 = (this.token.kind == Tokens.TokenKind.SEMI) ? null : parseExpression();
/* 2510 */         accept(Tokens.TokenKind.SEMI);
/* 2511 */         list3 = (this.token.kind == Tokens.TokenKind.RPAREN) ? List.nil() : forUpdate();
/* 2512 */         accept(Tokens.TokenKind.RPAREN);
/* 2513 */         jCStatement5 = parseStatementAsBlock();
/* 2514 */         return (JCTree.JCStatement)this.F.at(i).ForLoop(list1, jCExpression5, list3, jCStatement5);
/*      */ 
/*      */       
/*      */       case null:
/* 2518 */         nextToken();
/* 2519 */         jCExpression2 = parExpression();
/* 2520 */         jCStatement2 = parseStatementAsBlock();
/* 2521 */         return (JCTree.JCStatement)this.F.at(i).WhileLoop(jCExpression2, jCStatement2);
/*      */       
/*      */       case null:
/* 2524 */         nextToken();
/* 2525 */         jCStatement1 = parseStatementAsBlock();
/* 2526 */         accept(Tokens.TokenKind.WHILE);
/* 2527 */         jCExpression4 = parExpression();
/* 2528 */         jCDoWhileLoop = to(this.F.at(i).DoLoop(jCStatement1, jCExpression4));
/* 2529 */         accept(Tokens.TokenKind.SEMI);
/* 2530 */         return (JCTree.JCStatement)jCDoWhileLoop;
/*      */       
/*      */       case null:
/* 2533 */         nextToken();
/* 2534 */         list = List.nil();
/* 2535 */         if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 2536 */           checkTryWithResources();
/* 2537 */           nextToken();
/* 2538 */           list = resources();
/* 2539 */           accept(Tokens.TokenKind.RPAREN);
/*      */         } 
/* 2541 */         jCBlock2 = block();
/* 2542 */         listBuffer = new ListBuffer();
/* 2543 */         jCStatement5 = null;
/* 2544 */         if (this.token.kind == Tokens.TokenKind.CATCH || this.token.kind == Tokens.TokenKind.FINALLY) {
/* 2545 */           for (; this.token.kind == Tokens.TokenKind.CATCH; listBuffer.append(catchClause()));
/* 2546 */           if (this.token.kind == Tokens.TokenKind.FINALLY) {
/* 2547 */             nextToken();
/* 2548 */             jCBlock3 = block();
/*      */           }
/*      */         
/* 2551 */         } else if (this.allowTWR) {
/* 2552 */           if (list.isEmpty())
/* 2553 */             error(i, "try.without.catch.finally.or.resource.decls", new Object[0]); 
/*      */         } else {
/* 2555 */           error(i, "try.without.catch.or.finally", new Object[0]);
/*      */         } 
/* 2557 */         return (JCTree.JCStatement)this.F.at(i).Try(list, jCBlock2, listBuffer.toList(), jCBlock3);
/*      */       
/*      */       case null:
/* 2560 */         nextToken();
/* 2561 */         jCExpression1 = parExpression();
/* 2562 */         accept(Tokens.TokenKind.LBRACE);
/* 2563 */         list2 = switchBlockStatementGroups();
/* 2564 */         jCSwitch = to(this.F.at(i).Switch(jCExpression1, list2));
/* 2565 */         accept(Tokens.TokenKind.RBRACE);
/* 2566 */         return (JCTree.JCStatement)jCSwitch;
/*      */       
/*      */       case null:
/* 2569 */         nextToken();
/* 2570 */         jCExpression1 = parExpression();
/* 2571 */         jCBlock1 = block();
/* 2572 */         return (JCTree.JCStatement)this.F.at(i).Synchronized(jCExpression1, jCBlock1);
/*      */       
/*      */       case null:
/* 2575 */         nextToken();
/* 2576 */         jCExpression1 = (this.token.kind == Tokens.TokenKind.SEMI) ? null : parseExpression();
/* 2577 */         jCReturn = to(this.F.at(i).Return(jCExpression1));
/* 2578 */         accept(Tokens.TokenKind.SEMI);
/* 2579 */         return (JCTree.JCStatement)jCReturn;
/*      */       
/*      */       case null:
/* 2582 */         nextToken();
/* 2583 */         jCExpression1 = parseExpression();
/* 2584 */         jCThrow = to(this.F.at(i).Throw(jCExpression1));
/* 2585 */         accept(Tokens.TokenKind.SEMI);
/* 2586 */         return (JCTree.JCStatement)jCThrow;
/*      */       
/*      */       case null:
/* 2589 */         nextToken();
/* 2590 */         name = this.LAX_IDENTIFIER.accepts(this.token.kind) ? ident() : null;
/* 2591 */         jCBreak = to(this.F.at(i).Break(name));
/* 2592 */         accept(Tokens.TokenKind.SEMI);
/* 2593 */         return (JCTree.JCStatement)jCBreak;
/*      */       
/*      */       case null:
/* 2596 */         nextToken();
/* 2597 */         name = this.LAX_IDENTIFIER.accepts(this.token.kind) ? ident() : null;
/* 2598 */         jCContinue = to(this.F.at(i).Continue(name));
/* 2599 */         accept(Tokens.TokenKind.SEMI);
/* 2600 */         return (JCTree.JCStatement)jCContinue;
/*      */       
/*      */       case REFERENCE:
/* 2603 */         nextToken();
/* 2604 */         return (JCTree.JCStatement)toP(this.F.at(i).Skip());
/*      */       case null:
/* 2606 */         j = this.token.pos;
/* 2607 */         nextToken();
/* 2608 */         return doRecover(j, BasicErrorRecoveryAction.BLOCK_STMT, "else.without.if");
/*      */       case null:
/* 2610 */         k = this.token.pos;
/* 2611 */         nextToken();
/* 2612 */         return doRecover(k, BasicErrorRecoveryAction.BLOCK_STMT, "finally.without.try");
/*      */       case null:
/* 2614 */         return doRecover(this.token.pos, BasicErrorRecoveryAction.CATCH_CLAUSE, "catch.without.try");
/*      */       case null:
/* 2616 */         if (this.allowAsserts && this.token.kind == Tokens.TokenKind.ASSERT) {
/* 2617 */           JCTree.JCExpression jCExpression8; nextToken();
/* 2618 */           JCTree.JCExpression jCExpression7 = parseExpression();
/* 2619 */           jCBlock3 = null;
/* 2620 */           if (this.token.kind == Tokens.TokenKind.COLON) {
/* 2621 */             nextToken();
/* 2622 */             jCExpression8 = parseExpression();
/*      */           } 
/* 2624 */           JCTree.JCAssert jCAssert = to(this.F.at(i).Assert(jCExpression7, jCExpression8));
/* 2625 */           accept(Tokens.TokenKind.SEMI);
/* 2626 */           return (JCTree.JCStatement)jCAssert;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 2632 */     Tokens.Token token = this.token;
/* 2633 */     JCTree.JCExpression jCExpression6 = parseExpression();
/* 2634 */     if (this.token.kind == Tokens.TokenKind.COLON && jCExpression6.hasTag(JCTree.Tag.IDENT)) {
/* 2635 */       nextToken();
/* 2636 */       JCTree.JCStatement jCStatement = parseStatement();
/* 2637 */       return (JCTree.JCStatement)this.F.at(i).Labelled(token.name(), jCStatement);
/*      */     } 
/*      */     
/* 2640 */     JCTree.JCExpressionStatement jCExpressionStatement = to(this.F.at(i).Exec(checkExprStat(jCExpression6)));
/* 2641 */     accept(Tokens.TokenKind.SEMI);
/* 2642 */     return (JCTree.JCStatement)jCExpressionStatement; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private JCTree.JCStatement doRecover(int paramInt, ErrorRecoveryAction paramErrorRecoveryAction, String paramString) {
/* 2648 */     int i = this.S.errPos();
/* 2649 */     JCTree jCTree = paramErrorRecoveryAction.doRecover(this);
/* 2650 */     this.S.errPos(i);
/* 2651 */     return (JCTree.JCStatement)toP(this.F.Exec((JCTree.JCExpression)syntaxError(paramInt, List.of(jCTree), paramString, new Tokens.TokenKind[0])));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JCTree.JCCatch catchClause() {
/* 2658 */     int i = this.token.pos;
/* 2659 */     accept(Tokens.TokenKind.CATCH);
/* 2660 */     accept(Tokens.TokenKind.LPAREN);
/* 2661 */     JCTree.JCModifiers jCModifiers = optFinal(8589934592L);
/* 2662 */     List<JCTree.JCExpression> list = catchTypes();
/*      */     
/* 2664 */     JCTree.JCExpression jCExpression = (list.size() > 1) ? (JCTree.JCExpression)toP(this.F.at(((JCTree.JCExpression)list.head).getStartPosition()).TypeUnion(list)) : (JCTree.JCExpression)list.head;
/*      */     
/* 2666 */     JCTree.JCVariableDecl jCVariableDecl = variableDeclaratorId(jCModifiers, jCExpression);
/* 2667 */     accept(Tokens.TokenKind.RPAREN);
/* 2668 */     JCTree.JCBlock jCBlock = block();
/* 2669 */     return this.F.at(i).Catch(jCVariableDecl, jCBlock);
/*      */   }
/*      */   
/*      */   List<JCTree.JCExpression> catchTypes() {
/* 2673 */     ListBuffer listBuffer = new ListBuffer();
/* 2674 */     listBuffer.add(parseType());
/* 2675 */     while (this.token.kind == Tokens.TokenKind.BAR) {
/* 2676 */       checkMulticatch();
/* 2677 */       nextToken();
/*      */ 
/*      */       
/* 2680 */       listBuffer.add(parseType());
/*      */     } 
/* 2682 */     return listBuffer.toList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCCase> switchBlockStatementGroups() {
/* 2690 */     ListBuffer listBuffer = new ListBuffer();
/*      */     while (true) {
/* 2692 */       int i = this.token.pos;
/* 2693 */       switch (this.token.kind) {
/*      */         case null:
/*      */         case null:
/* 2696 */           listBuffer.append(switchBlockStatementGroup()); continue;
/*      */         case null:
/*      */         case null:
/* 2699 */           return listBuffer.toList();
/*      */       } 
/* 2701 */       nextToken();
/* 2702 */       syntaxError(i, "expected3", new Tokens.TokenKind[] { Tokens.TokenKind.CASE, Tokens.TokenKind.DEFAULT, Tokens.TokenKind.RBRACE });
/*      */     } 
/*      */   }
/*      */   protected JCTree.JCCase switchBlockStatementGroup() {
/*      */     List<JCTree.JCStatement> list;
/*      */     JCTree.JCCase jCCase;
/*      */     JCTree.JCExpression jCExpression;
/* 2709 */     int i = this.token.pos;
/*      */ 
/*      */     
/* 2712 */     switch (this.token.kind) {
/*      */       case null:
/* 2714 */         nextToken();
/* 2715 */         jCExpression = parseExpression();
/* 2716 */         accept(Tokens.TokenKind.COLON);
/* 2717 */         list = blockStatements();
/* 2718 */         jCCase = this.F.at(i).Case(jCExpression, list);
/* 2719 */         if (list.isEmpty())
/* 2720 */           storeEnd((JCTree)jCCase, (this.S.prevToken()).endPos); 
/* 2721 */         return jCCase;
/*      */       case null:
/* 2723 */         nextToken();
/* 2724 */         accept(Tokens.TokenKind.COLON);
/* 2725 */         list = blockStatements();
/* 2726 */         jCCase = this.F.at(i).Case(null, list);
/* 2727 */         if (list.isEmpty())
/* 2728 */           storeEnd((JCTree)jCCase, (this.S.prevToken()).endPos); 
/* 2729 */         return jCCase;
/*      */     } 
/* 2731 */     throw new AssertionError("should not reach here");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   <T extends ListBuffer<? super JCTree.JCExpressionStatement>> T moreStatementExpressions(int paramInt, JCTree.JCExpression paramJCExpression, T paramT) {
/* 2740 */     paramT.append(toP(this.F.at(paramInt).Exec(checkExprStat(paramJCExpression))));
/* 2741 */     while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2742 */       nextToken();
/* 2743 */       paramInt = this.token.pos;
/* 2744 */       JCTree.JCExpression jCExpression = parseExpression();
/*      */       
/* 2746 */       paramT.append(toP(this.F.at(paramInt).Exec(checkExprStat(jCExpression))));
/*      */     } 
/* 2748 */     return paramT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCStatement> forInit() {
/* 2755 */     ListBuffer listBuffer = new ListBuffer();
/* 2756 */     int i = this.token.pos;
/* 2757 */     if (this.token.kind == Tokens.TokenKind.FINAL || this.token.kind == Tokens.TokenKind.MONKEYS_AT) {
/* 2758 */       return variableDeclarators(optFinal(0L), parseType(), listBuffer).toList();
/*      */     }
/* 2760 */     JCTree.JCExpression jCExpression = term(3);
/* 2761 */     if ((this.lastmode & 0x2) != 0 && this.LAX_IDENTIFIER.accepts(this.token.kind))
/* 2762 */       return variableDeclarators(mods(i, 0L, List.nil()), jCExpression, listBuffer).toList(); 
/* 2763 */     if ((this.lastmode & 0x2) != 0 && this.token.kind == Tokens.TokenKind.COLON) {
/* 2764 */       error(i, "bad.initializer", new Object[] { "for-loop" });
/* 2765 */       return List.of(this.F.at(i).VarDef(null, null, jCExpression, null));
/*      */     } 
/* 2767 */     return moreStatementExpressions(i, jCExpression, listBuffer).toList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCExpressionStatement> forUpdate() {
/* 2775 */     return moreStatementExpressions(this.token.pos, 
/* 2776 */         parseExpression(), new ListBuffer())
/* 2777 */       .toList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCAnnotation> annotationsOpt(JCTree.Tag paramTag) {
/* 2785 */     if (this.token.kind != Tokens.TokenKind.MONKEYS_AT) return List.nil(); 
/* 2786 */     ListBuffer listBuffer = new ListBuffer();
/* 2787 */     int i = this.mode;
/* 2788 */     while (this.token.kind == Tokens.TokenKind.MONKEYS_AT) {
/* 2789 */       int j = this.token.pos;
/* 2790 */       nextToken();
/* 2791 */       listBuffer.append(annotation(j, paramTag));
/*      */     } 
/* 2793 */     this.lastmode = this.mode;
/* 2794 */     this.mode = i;
/* 2795 */     return listBuffer.toList();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCAnnotation> typeAnnotationsOpt() {
/* 2801 */     return annotationsOpt(JCTree.Tag.TYPE_ANNOTATION);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCModifiers modifiersOpt() {
/* 2811 */     return modifiersOpt(null);
/*      */   } protected JCTree.JCModifiers modifiersOpt(JCTree.JCModifiers paramJCModifiers) {
/*      */     long l;
/*      */     int i;
/* 2815 */     ListBuffer listBuffer = new ListBuffer();
/*      */     
/* 2817 */     if (paramJCModifiers == null) {
/* 2818 */       l = 0L;
/* 2819 */       i = this.token.pos;
/*      */     } else {
/* 2821 */       l = paramJCModifiers.flags;
/* 2822 */       listBuffer.appendList(paramJCModifiers.annotations);
/* 2823 */       i = paramJCModifiers.pos;
/*      */     } 
/* 2825 */     if (this.token.deprecatedFlag()) {
/* 2826 */       l |= 0x20000L;
/*      */     }
/*      */     
/*      */     while (true) {
/*      */       long l1;
/*      */       
/* 2832 */       switch (this.token.kind) { case null:
/* 2833 */           l1 = 2L; break;
/* 2834 */         case null: l1 = 4L; break;
/* 2835 */         case SELECT: l1 = 1L; break;
/* 2836 */         case null: l1 = 8L; break;
/* 2837 */         case null: l1 = 128L; break;
/* 2838 */         case CLASSDEF: l1 = 16L; break;
/* 2839 */         case VARDEF: l1 = 1024L; break;
/* 2840 */         case null: l1 = 256L; break;
/* 2841 */         case null: l1 = 64L; break;
/* 2842 */         case null: l1 = 32L; break;
/* 2843 */         case null: l1 = 2048L; break;
/* 2844 */         case null: l1 = 8192L; break;
/* 2845 */         case null: checkDefaultMethods(); l1 = 8796093022208L; break;
/* 2846 */         case null: l1 = 0L; nextToken(); break;
/*      */         default:
/*      */           break; }
/* 2849 */        if ((l & l1) != 0L) error(this.token.pos, "repeated.modifier", new Object[0]); 
/* 2850 */       int j = this.token.pos;
/* 2851 */       nextToken();
/* 2852 */       if (l1 == 8192L) {
/* 2853 */         checkAnnotations();
/* 2854 */         if (this.token.kind != Tokens.TokenKind.INTERFACE) {
/* 2855 */           JCTree.JCAnnotation jCAnnotation = annotation(j, JCTree.Tag.ANNOTATION);
/*      */           
/* 2857 */           if (l == 0L && listBuffer.isEmpty())
/* 2858 */             i = jCAnnotation.pos; 
/* 2859 */           listBuffer.append(jCAnnotation);
/* 2860 */           l1 = 0L;
/*      */         } 
/*      */       } 
/* 2863 */       l |= l1;
/*      */     } 
/* 2865 */     switch (this.token.kind) { case null:
/* 2866 */         l |= 0x4000L; break;
/* 2867 */       case null: l |= 0x200L;
/*      */         break; }
/*      */ 
/*      */     
/* 2871 */     return mods(i, l, listBuffer.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCModifiers mods(int paramInt, long paramLong, List<JCTree.JCAnnotation> paramList) {
/* 2877 */     if ((paramLong & 0x80000002DFFL) == 0L && paramList.isEmpty()) {
/* 2878 */       paramInt = -1;
/*      */     }
/* 2880 */     JCTree.JCModifiers jCModifiers = this.F.at(paramInt).Modifiers(paramLong, paramList);
/* 2881 */     if (paramInt != -1)
/* 2882 */       storeEnd((JCTree)jCModifiers, (this.S.prevToken()).endPos); 
/* 2883 */     return jCModifiers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCAnnotation annotation(int paramInt, JCTree.Tag paramTag) {
/*      */     JCTree.JCAnnotation jCAnnotation;
/* 2893 */     checkAnnotations();
/* 2894 */     if (paramTag == JCTree.Tag.TYPE_ANNOTATION) {
/* 2895 */       checkTypeAnnotations();
/*      */     }
/* 2897 */     JCTree.JCExpression jCExpression = qualident(false);
/* 2898 */     List<JCTree.JCExpression> list = annotationFieldValuesOpt();
/*      */     
/* 2900 */     if (paramTag == JCTree.Tag.ANNOTATION) {
/* 2901 */       jCAnnotation = this.F.at(paramInt).Annotation((JCTree)jCExpression, list);
/* 2902 */     } else if (paramTag == JCTree.Tag.TYPE_ANNOTATION) {
/* 2903 */       jCAnnotation = this.F.at(paramInt).TypeAnnotation((JCTree)jCExpression, list);
/*      */     } else {
/* 2905 */       throw new AssertionError("Unhandled annotation kind: " + paramTag);
/*      */     } 
/*      */     
/* 2908 */     storeEnd((JCTree)jCAnnotation, (this.S.prevToken()).endPos);
/* 2909 */     return jCAnnotation;
/*      */   }
/*      */   
/*      */   List<JCTree.JCExpression> annotationFieldValuesOpt() {
/* 2913 */     return (this.token.kind == Tokens.TokenKind.LPAREN) ? annotationFieldValues() : List.nil();
/*      */   }
/*      */ 
/*      */   
/*      */   List<JCTree.JCExpression> annotationFieldValues() {
/* 2918 */     accept(Tokens.TokenKind.LPAREN);
/* 2919 */     ListBuffer listBuffer = new ListBuffer();
/* 2920 */     if (this.token.kind != Tokens.TokenKind.RPAREN) {
/* 2921 */       listBuffer.append(annotationFieldValue());
/* 2922 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2923 */         nextToken();
/* 2924 */         listBuffer.append(annotationFieldValue());
/*      */       } 
/*      */     } 
/* 2927 */     accept(Tokens.TokenKind.RPAREN);
/* 2928 */     return listBuffer.toList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression annotationFieldValue() {
/* 2935 */     if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 2936 */       this.mode = 1;
/* 2937 */       JCTree.JCExpression jCExpression = term1();
/* 2938 */       if (jCExpression.hasTag(JCTree.Tag.IDENT) && this.token.kind == Tokens.TokenKind.EQ) {
/* 2939 */         int i = this.token.pos;
/* 2940 */         accept(Tokens.TokenKind.EQ);
/* 2941 */         JCTree.JCExpression jCExpression1 = annotationValue();
/* 2942 */         return (JCTree.JCExpression)toP(this.F.at(i).Assign(jCExpression, jCExpression1));
/*      */       } 
/* 2944 */       return jCExpression;
/*      */     } 
/*      */     
/* 2947 */     return annotationValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCExpression annotationValue() {
/*      */     int i;
/*      */     ListBuffer listBuffer;
/* 2956 */     switch (this.token.kind) {
/*      */       case null:
/* 2958 */         i = this.token.pos;
/* 2959 */         nextToken();
/* 2960 */         return (JCTree.JCExpression)annotation(i, JCTree.Tag.ANNOTATION);
/*      */       case null:
/* 2962 */         i = this.token.pos;
/* 2963 */         accept(Tokens.TokenKind.LBRACE);
/* 2964 */         listBuffer = new ListBuffer();
/* 2965 */         if (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2966 */           nextToken();
/* 2967 */         } else if (this.token.kind != Tokens.TokenKind.RBRACE) {
/* 2968 */           listBuffer.append(annotationValue());
/* 2969 */           while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 2970 */             nextToken();
/* 2971 */             if (this.token.kind == Tokens.TokenKind.RBRACE)
/* 2972 */               break;  listBuffer.append(annotationValue());
/*      */           } 
/*      */         } 
/* 2975 */         accept(Tokens.TokenKind.RBRACE);
/* 2976 */         return (JCTree.JCExpression)toP(this.F.at(i).NewArray(null, List.nil(), listBuffer.toList()));
/*      */     } 
/* 2978 */     this.mode = 1;
/* 2979 */     return term1();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends ListBuffer<? super JCTree.JCVariableDecl>> T variableDeclarators(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, T paramT) {
/* 2989 */     return variableDeclaratorsRest(this.token.pos, paramJCModifiers, paramJCExpression, ident(), false, null, paramT);
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
/*      */   <T extends ListBuffer<? super JCTree.JCVariableDecl>> T variableDeclaratorsRest(int paramInt, JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, Name paramName, boolean paramBoolean, Tokens.Comment paramComment, T paramT) {
/* 3006 */     paramT.append(variableDeclaratorRest(paramInt, paramJCModifiers, paramJCExpression, paramName, paramBoolean, paramComment));
/* 3007 */     while (this.token.kind == Tokens.TokenKind.COMMA) {
/*      */       
/* 3009 */       storeEnd((JCTree)paramT.last(), this.token.endPos);
/* 3010 */       nextToken();
/* 3011 */       paramT.append(variableDeclarator(paramJCModifiers, paramJCExpression, paramBoolean, paramComment));
/*      */     } 
/* 3013 */     return paramT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCVariableDecl variableDeclarator(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, boolean paramBoolean, Tokens.Comment paramComment) {
/* 3020 */     return variableDeclaratorRest(this.token.pos, paramJCModifiers, paramJCExpression, ident(), paramBoolean, paramComment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCVariableDecl variableDeclaratorRest(int paramInt, JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, Name paramName, boolean paramBoolean, Tokens.Comment paramComment) {
/* 3031 */     paramJCExpression = bracketsOpt(paramJCExpression);
/* 3032 */     JCTree.JCExpression jCExpression = null;
/* 3033 */     if (this.token.kind == Tokens.TokenKind.EQ)
/* 3034 */     { nextToken();
/* 3035 */       jCExpression = variableInitializer(); }
/*      */     
/* 3037 */     else if (paramBoolean) { syntaxError(this.token.pos, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.EQ }); }
/*      */     
/* 3039 */     JCTree.JCVariableDecl jCVariableDecl = toP(this.F.at(paramInt).VarDef(paramJCModifiers, paramName, paramJCExpression, jCExpression));
/* 3040 */     attach((JCTree)jCVariableDecl, paramComment);
/* 3041 */     return jCVariableDecl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCVariableDecl variableDeclaratorId(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression) {
/* 3047 */     return variableDeclaratorId(paramJCModifiers, paramJCExpression, false);
/*      */   }
/*      */   JCTree.JCVariableDecl variableDeclaratorId(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, boolean paramBoolean) {
/*      */     Name name;
/* 3051 */     int i = this.token.pos;
/*      */     
/* 3053 */     if (paramBoolean && this.token.kind == Tokens.TokenKind.UNDERSCORE) {
/* 3054 */       this.log.error(i, "underscore.as.identifier.in.lambda", new Object[0]);
/* 3055 */       name = this.token.name();
/* 3056 */       nextToken();
/*      */     }
/* 3058 */     else if (this.allowThisIdent) {
/* 3059 */       JCTree.JCExpression jCExpression = qualident(false);
/* 3060 */       if (jCExpression.hasTag(JCTree.Tag.IDENT) && ((JCTree.JCIdent)jCExpression).name != this.names._this) {
/* 3061 */         name = ((JCTree.JCIdent)jCExpression).name;
/*      */       } else {
/* 3063 */         if ((paramJCModifiers.flags & 0x400000000L) != 0L) {
/* 3064 */           this.log.error(this.token.pos, "varargs.and.receiver", new Object[0]);
/*      */         }
/* 3066 */         if (this.token.kind == Tokens.TokenKind.LBRACKET) {
/* 3067 */           this.log.error(this.token.pos, "array.and.receiver", new Object[0]);
/*      */         }
/* 3069 */         return toP(this.F.at(i).ReceiverVarDef(paramJCModifiers, jCExpression, paramJCExpression));
/*      */       } 
/*      */     } else {
/* 3072 */       name = ident();
/*      */     } 
/*      */     
/* 3075 */     if ((paramJCModifiers.flags & 0x400000000L) != 0L && this.token.kind == Tokens.TokenKind.LBRACKET)
/*      */     {
/* 3077 */       this.log.error(this.token.pos, "varargs.and.old.array.syntax", new Object[0]);
/*      */     }
/* 3079 */     paramJCExpression = bracketsOpt(paramJCExpression);
/* 3080 */     return toP(this.F.at(i).VarDef(paramJCModifiers, name, paramJCExpression, null));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree> resources() {
/* 3086 */     ListBuffer listBuffer = new ListBuffer();
/* 3087 */     listBuffer.append(resource());
/* 3088 */     while (this.token.kind == Tokens.TokenKind.SEMI) {
/*      */       
/* 3090 */       storeEnd((JCTree)listBuffer.last(), this.token.endPos);
/* 3091 */       int i = this.token.pos;
/* 3092 */       nextToken();
/* 3093 */       if (this.token.kind == Tokens.TokenKind.RPAREN) {
/*      */         break;
/*      */       }
/*      */       
/* 3097 */       listBuffer.append(resource());
/*      */     } 
/* 3099 */     return listBuffer.toList();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected JCTree resource() {
/* 3105 */     JCTree.JCModifiers jCModifiers = optFinal(16L);
/* 3106 */     JCTree.JCExpression jCExpression = parseType();
/* 3107 */     int i = this.token.pos;
/* 3108 */     Name name = ident();
/* 3109 */     return (JCTree)variableDeclaratorRest(i, jCModifiers, jCExpression, name, true, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JCTree.JCCompilationUnit parseCompilationUnit() {
/* 3115 */     Tokens.Token token = this.token;
/* 3116 */     JCTree.JCExpression jCExpression = null;
/* 3117 */     JCTree.JCModifiers jCModifiers = null;
/* 3118 */     boolean bool1 = false;
/* 3119 */     boolean bool2 = false;
/* 3120 */     boolean bool3 = false;
/* 3121 */     List list = List.nil();
/* 3122 */     if (this.token.kind == Tokens.TokenKind.MONKEYS_AT) {
/* 3123 */       jCModifiers = modifiersOpt();
/*      */     }
/* 3125 */     if (this.token.kind == Tokens.TokenKind.PACKAGE) {
/* 3126 */       bool3 = true;
/* 3127 */       if (jCModifiers != null) {
/* 3128 */         checkNoMods(jCModifiers.flags);
/* 3129 */         list = jCModifiers.annotations;
/* 3130 */         jCModifiers = null;
/*      */       } 
/* 3132 */       nextToken();
/* 3133 */       jCExpression = qualident(false);
/* 3134 */       accept(Tokens.TokenKind.SEMI);
/*      */     } 
/* 3136 */     ListBuffer listBuffer = new ListBuffer();
/* 3137 */     boolean bool4 = true;
/* 3138 */     boolean bool5 = true;
/* 3139 */     while (this.token.kind != Tokens.TokenKind.EOF) {
/* 3140 */       JCTree.JCExpression jCExpression1; if (this.token.pos > 0 && this.token.pos <= this.endPosTable.errorEndPos) {
/*      */         
/* 3142 */         skip(bool4, false, false, false);
/* 3143 */         if (this.token.kind == Tokens.TokenKind.EOF)
/*      */           break; 
/*      */       } 
/* 3146 */       if (bool4 && jCModifiers == null && this.token.kind == Tokens.TokenKind.IMPORT) {
/* 3147 */         bool2 = true;
/* 3148 */         listBuffer.append(importDeclaration()); continue;
/*      */       } 
/* 3150 */       Tokens.Comment comment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3151 */       if (bool5 && !bool2 && !bool3) {
/* 3152 */         comment = token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3153 */         bool1 = true;
/*      */       } 
/* 3155 */       JCTree jCTree = typeDeclaration(jCModifiers, comment);
/* 3156 */       if (jCTree instanceof JCTree.JCExpressionStatement)
/* 3157 */         jCExpression1 = ((JCTree.JCExpressionStatement)jCTree).expr; 
/* 3158 */       listBuffer.append(jCExpression1);
/* 3159 */       if (jCExpression1 instanceof JCTree.JCClassDecl)
/* 3160 */         bool4 = false; 
/* 3161 */       jCModifiers = null;
/* 3162 */       bool5 = false;
/*      */     } 
/*      */     
/* 3165 */     JCTree.JCCompilationUnit jCCompilationUnit = this.F.at(token.pos).TopLevel(list, jCExpression, listBuffer.toList());
/* 3166 */     if (!bool1)
/* 3167 */       attach((JCTree)jCCompilationUnit, token.comment(Tokens.Comment.CommentStyle.JAVADOC)); 
/* 3168 */     if (listBuffer.isEmpty())
/* 3169 */       storeEnd((JCTree)jCCompilationUnit, (this.S.prevToken()).endPos); 
/* 3170 */     if (this.keepDocComments)
/* 3171 */       jCCompilationUnit.docComments = this.docComments; 
/* 3172 */     if (this.keepLineMap)
/* 3173 */       jCCompilationUnit.lineMap = this.S.getLineMap(); 
/* 3174 */     this.endPosTable.setParser((JavacParser)null);
/* 3175 */     jCCompilationUnit.endPositions = this.endPosTable;
/* 3176 */     return jCCompilationUnit;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree importDeclaration() {
/* 3182 */     int i = this.token.pos;
/* 3183 */     nextToken();
/* 3184 */     boolean bool = false;
/* 3185 */     if (this.token.kind == Tokens.TokenKind.STATIC) {
/* 3186 */       checkStaticImports();
/* 3187 */       bool = true;
/* 3188 */       nextToken();
/*      */     } 
/* 3190 */     JCTree.JCExpression jCExpression = (JCTree.JCExpression)toP(this.F.at(this.token.pos).Ident(ident()));
/*      */     do {
/* 3192 */       int j = this.token.pos;
/* 3193 */       accept(Tokens.TokenKind.DOT);
/* 3194 */       if (this.token.kind == Tokens.TokenKind.STAR) {
/* 3195 */         jCExpression = (JCTree.JCExpression)to(this.F.at(j).Select(jCExpression, this.names.asterisk));
/* 3196 */         nextToken();
/*      */         break;
/*      */       } 
/* 3199 */       jCExpression = (JCTree.JCExpression)toP(this.F.at(j).Select(jCExpression, ident()));
/*      */     }
/* 3201 */     while (this.token.kind == Tokens.TokenKind.DOT);
/* 3202 */     accept(Tokens.TokenKind.SEMI);
/* 3203 */     return (JCTree)toP(this.F.at(i).Import((JCTree)jCExpression, bool));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree typeDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment) {
/* 3210 */     int i = this.token.pos;
/* 3211 */     if (paramJCModifiers == null && this.token.kind == Tokens.TokenKind.SEMI) {
/* 3212 */       nextToken();
/* 3213 */       return (JCTree)toP(this.F.at(i).Skip());
/*      */     } 
/* 3215 */     return (JCTree)classOrInterfaceOrEnumDeclaration(modifiersOpt(paramJCModifiers), paramComment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree.JCStatement classOrInterfaceOrEnumDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment) {
/*      */     List<JCTree> list;
/* 3225 */     if (this.token.kind == Tokens.TokenKind.CLASS)
/* 3226 */       return (JCTree.JCStatement)classDeclaration(paramJCModifiers, paramComment); 
/* 3227 */     if (this.token.kind == Tokens.TokenKind.INTERFACE)
/* 3228 */       return (JCTree.JCStatement)interfaceDeclaration(paramJCModifiers, paramComment); 
/* 3229 */     if (this.allowEnums) {
/* 3230 */       if (this.token.kind == Tokens.TokenKind.ENUM) {
/* 3231 */         return (JCTree.JCStatement)enumDeclaration(paramJCModifiers, paramComment);
/*      */       }
/* 3233 */       int j = this.token.pos;
/*      */       
/* 3235 */       if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 3236 */         list = List.of(paramJCModifiers, toP(this.F.at(j).Ident(ident())));
/* 3237 */         setErrorEndPos(this.token.pos);
/*      */       } else {
/* 3239 */         list = List.of(paramJCModifiers);
/*      */       } 
/* 3241 */       return (JCTree.JCStatement)toP(this.F.Exec((JCTree.JCExpression)syntaxError(j, list, "expected3", new Tokens.TokenKind[] { Tokens.TokenKind.CLASS, Tokens.TokenKind.INTERFACE, Tokens.TokenKind.ENUM })));
/*      */     } 
/*      */ 
/*      */     
/* 3245 */     if (this.token.kind == Tokens.TokenKind.ENUM) {
/* 3246 */       error(this.token.pos, "enums.not.supported.in.source", new Object[] { this.source.name });
/* 3247 */       this.allowEnums = true;
/* 3248 */       return (JCTree.JCStatement)enumDeclaration(paramJCModifiers, paramComment);
/*      */     } 
/* 3250 */     int i = this.token.pos;
/*      */     
/* 3252 */     if (this.LAX_IDENTIFIER.accepts(this.token.kind)) {
/* 3253 */       list = List.of(paramJCModifiers, toP(this.F.at(i).Ident(ident())));
/* 3254 */       setErrorEndPos(this.token.pos);
/*      */     } else {
/* 3256 */       list = List.of(paramJCModifiers);
/*      */     } 
/* 3258 */     return (JCTree.JCStatement)toP(this.F.Exec((JCTree.JCExpression)syntaxError(i, list, "expected2", new Tokens.TokenKind[] { Tokens.TokenKind.CLASS, Tokens.TokenKind.INTERFACE })));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JCTree.JCClassDecl classDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment) {
/* 3269 */     int i = this.token.pos;
/* 3270 */     accept(Tokens.TokenKind.CLASS);
/* 3271 */     Name name = ident();
/*      */     
/* 3273 */     List<JCTree.JCTypeParameter> list = typeParametersOpt();
/*      */     
/* 3275 */     JCTree.JCExpression jCExpression = null;
/* 3276 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 3277 */       nextToken();
/* 3278 */       jCExpression = parseType();
/*      */     } 
/* 3280 */     List<JCTree.JCExpression> list1 = List.nil();
/* 3281 */     if (this.token.kind == Tokens.TokenKind.IMPLEMENTS) {
/* 3282 */       nextToken();
/* 3283 */       list1 = typeList();
/*      */     } 
/* 3285 */     List<JCTree> list2 = classOrInterfaceBody(name, false);
/* 3286 */     JCTree.JCClassDecl jCClassDecl = toP(this.F.at(i).ClassDef(paramJCModifiers, name, list, jCExpression, list1, list2));
/*      */     
/* 3288 */     attach((JCTree)jCClassDecl, paramComment);
/* 3289 */     return jCClassDecl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JCTree.JCClassDecl interfaceDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment) {
/* 3298 */     int i = this.token.pos;
/* 3299 */     accept(Tokens.TokenKind.INTERFACE);
/* 3300 */     Name name = ident();
/*      */     
/* 3302 */     List<JCTree.JCTypeParameter> list = typeParametersOpt();
/*      */     
/* 3304 */     List<JCTree.JCExpression> list1 = List.nil();
/* 3305 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 3306 */       nextToken();
/* 3307 */       list1 = typeList();
/*      */     } 
/* 3309 */     List<JCTree> list2 = classOrInterfaceBody(name, true);
/* 3310 */     JCTree.JCClassDecl jCClassDecl = toP(this.F.at(i).ClassDef(paramJCModifiers, name, list, null, list1, list2));
/*      */     
/* 3312 */     attach((JCTree)jCClassDecl, paramComment);
/* 3313 */     return jCClassDecl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JCTree.JCClassDecl enumDeclaration(JCTree.JCModifiers paramJCModifiers, Tokens.Comment paramComment) {
/* 3321 */     int i = this.token.pos;
/* 3322 */     accept(Tokens.TokenKind.ENUM);
/* 3323 */     Name name = ident();
/*      */     
/* 3325 */     List<JCTree.JCExpression> list = List.nil();
/* 3326 */     if (this.token.kind == Tokens.TokenKind.IMPLEMENTS) {
/* 3327 */       nextToken();
/* 3328 */       list = typeList();
/*      */     } 
/*      */     
/* 3331 */     List<JCTree> list1 = enumBody(name);
/* 3332 */     paramJCModifiers.flags |= 0x4000L;
/* 3333 */     JCTree.JCClassDecl jCClassDecl = toP(this.F.at(i)
/* 3334 */         .ClassDef(paramJCModifiers, name, List.nil(), null, list, list1));
/*      */     
/* 3336 */     attach((JCTree)jCClassDecl, paramComment);
/* 3337 */     return jCClassDecl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree> enumBody(Name paramName) {
/* 3344 */     accept(Tokens.TokenKind.LBRACE);
/* 3345 */     ListBuffer listBuffer = new ListBuffer();
/* 3346 */     if (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3347 */       nextToken();
/* 3348 */     } else if (this.token.kind != Tokens.TokenKind.RBRACE && this.token.kind != Tokens.TokenKind.SEMI) {
/* 3349 */       listBuffer.append(enumeratorDeclaration(paramName));
/* 3350 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3351 */         nextToken();
/* 3352 */         if (this.token.kind == Tokens.TokenKind.RBRACE || this.token.kind == Tokens.TokenKind.SEMI)
/* 3353 */           break;  listBuffer.append(enumeratorDeclaration(paramName));
/*      */       } 
/* 3355 */       if (this.token.kind != Tokens.TokenKind.SEMI && this.token.kind != Tokens.TokenKind.RBRACE) {
/* 3356 */         listBuffer.append(syntaxError(this.token.pos, "expected3", new Tokens.TokenKind[] { Tokens.TokenKind.COMMA, Tokens.TokenKind.RBRACE, Tokens.TokenKind.SEMI }));
/*      */         
/* 3358 */         nextToken();
/*      */       } 
/*      */     } 
/* 3361 */     if (this.token.kind == Tokens.TokenKind.SEMI) {
/* 3362 */       nextToken();
/* 3363 */       while (this.token.kind != Tokens.TokenKind.RBRACE && this.token.kind != Tokens.TokenKind.EOF) {
/* 3364 */         listBuffer.appendList(classOrInterfaceBodyDeclaration(paramName, false));
/*      */         
/* 3366 */         if (this.token.pos <= this.endPosTable.errorEndPos)
/*      */         {
/* 3368 */           skip(false, true, true, false);
/*      */         }
/*      */       } 
/*      */     } 
/* 3372 */     accept(Tokens.TokenKind.RBRACE);
/* 3373 */     return listBuffer.toList();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   JCTree enumeratorDeclaration(Name paramName) {
/* 3379 */     Tokens.Comment comment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3380 */     int i = 16409;
/* 3381 */     if (this.token.deprecatedFlag()) {
/* 3382 */       i |= 0x20000;
/*      */     }
/* 3384 */     int j = this.token.pos;
/* 3385 */     List<JCTree.JCAnnotation> list = annotationsOpt(JCTree.Tag.ANNOTATION);
/* 3386 */     JCTree.JCModifiers jCModifiers = this.F.at(list.isEmpty() ? -1 : j).Modifiers(i, list);
/* 3387 */     List<JCTree.JCExpression> list1 = typeArgumentsOpt();
/* 3388 */     int k = this.token.pos;
/* 3389 */     Name name = ident();
/* 3390 */     int m = this.token.pos;
/*      */     
/* 3392 */     List list2 = (this.token.kind == Tokens.TokenKind.LPAREN) ? arguments() : List.nil();
/* 3393 */     JCTree.JCClassDecl jCClassDecl = null;
/* 3394 */     if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 3395 */       JCTree.JCModifiers jCModifiers1 = this.F.at(-1).Modifiers(16392L);
/* 3396 */       List<JCTree> list3 = classOrInterfaceBody(this.names.empty, false);
/* 3397 */       jCClassDecl = toP(this.F.at(k).AnonymousClassDef(jCModifiers1, list3));
/*      */     } 
/* 3399 */     if (list2.isEmpty() && jCClassDecl == null)
/* 3400 */       m = k; 
/* 3401 */     JCTree.JCIdent jCIdent = this.F.at(k).Ident(paramName);
/* 3402 */     JCTree.JCNewClass jCNewClass = this.F.at(m).NewClass(null, list1, (JCTree.JCExpression)jCIdent, list2, jCClassDecl);
/* 3403 */     if (m != k)
/* 3404 */       storeEnd((JCTree)jCNewClass, (this.S.prevToken()).endPos); 
/* 3405 */     jCIdent = this.F.at(k).Ident(paramName);
/* 3406 */     JCTree jCTree = (JCTree)toP(this.F.at(j).VarDef(jCModifiers, name, (JCTree.JCExpression)jCIdent, (JCTree.JCExpression)jCNewClass));
/* 3407 */     attach(jCTree, comment);
/* 3408 */     return jCTree;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCExpression> typeList() {
/* 3414 */     ListBuffer listBuffer = new ListBuffer();
/* 3415 */     listBuffer.append(parseType());
/* 3416 */     while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3417 */       nextToken();
/* 3418 */       listBuffer.append(parseType());
/*      */     } 
/* 3420 */     return listBuffer.toList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree> classOrInterfaceBody(Name paramName, boolean paramBoolean) {
/* 3427 */     accept(Tokens.TokenKind.LBRACE);
/* 3428 */     if (this.token.pos <= this.endPosTable.errorEndPos) {
/*      */       
/* 3430 */       skip(false, true, false, false);
/* 3431 */       if (this.token.kind == Tokens.TokenKind.LBRACE)
/* 3432 */         nextToken(); 
/*      */     } 
/* 3434 */     ListBuffer listBuffer = new ListBuffer();
/* 3435 */     while (this.token.kind != Tokens.TokenKind.RBRACE && this.token.kind != Tokens.TokenKind.EOF) {
/* 3436 */       listBuffer.appendList(classOrInterfaceBodyDeclaration(paramName, paramBoolean));
/* 3437 */       if (this.token.pos <= this.endPosTable.errorEndPos)
/*      */       {
/* 3439 */         skip(false, true, true, false);
/*      */       }
/*      */     } 
/* 3442 */     accept(Tokens.TokenKind.RBRACE);
/* 3443 */     return listBuffer.toList();
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
/*      */   protected List<JCTree> classOrInterfaceBodyDeclaration(Name paramName, boolean paramBoolean) {
/*      */     JCTree.JCExpression jCExpression;
/* 3476 */     if (this.token.kind == Tokens.TokenKind.SEMI) {
/* 3477 */       nextToken();
/* 3478 */       return List.nil();
/*      */     } 
/* 3480 */     Tokens.Comment comment = this.token.comment(Tokens.Comment.CommentStyle.JAVADOC);
/* 3481 */     int i = this.token.pos;
/* 3482 */     JCTree.JCModifiers jCModifiers = modifiersOpt();
/* 3483 */     if (this.token.kind == Tokens.TokenKind.CLASS || this.token.kind == Tokens.TokenKind.INTERFACE || (this.allowEnums && this.token.kind == Tokens.TokenKind.ENUM))
/*      */     {
/*      */       
/* 3486 */       return List.of(classOrInterfaceOrEnumDeclaration(jCModifiers, comment)); } 
/* 3487 */     if (this.token.kind == Tokens.TokenKind.LBRACE && !paramBoolean && (jCModifiers.flags & 0xFFFL & 0xFFFFFFFFFFFFFFF7L) == 0L && jCModifiers.annotations
/*      */       
/* 3489 */       .isEmpty()) {
/* 3490 */       return List.of(block(i, jCModifiers.flags));
/*      */     }
/* 3492 */     i = this.token.pos;
/* 3493 */     List<JCTree.JCTypeParameter> list = typeParametersOpt();
/*      */ 
/*      */     
/* 3496 */     if (list.nonEmpty() && jCModifiers.pos == -1) {
/* 3497 */       jCModifiers.pos = i;
/* 3498 */       storeEnd((JCTree)jCModifiers, i);
/*      */     } 
/* 3500 */     List<JCTree.JCAnnotation> list1 = annotationsOpt(JCTree.Tag.ANNOTATION);
/*      */     
/* 3502 */     if (list1.nonEmpty()) {
/* 3503 */       checkAnnotationsAfterTypeParams(((JCTree.JCAnnotation)list1.head).pos);
/* 3504 */       jCModifiers.annotations = jCModifiers.annotations.appendList(list1);
/* 3505 */       if (jCModifiers.pos == -1) {
/* 3506 */         jCModifiers.pos = ((JCTree.JCAnnotation)jCModifiers.annotations.head).pos;
/*      */       }
/*      */     } 
/* 3509 */     Tokens.Token token = this.token;
/* 3510 */     i = this.token.pos;
/*      */     
/* 3512 */     boolean bool = (this.token.kind == Tokens.TokenKind.VOID) ? true : false;
/* 3513 */     if (bool) {
/* 3514 */       jCExpression = (JCTree.JCExpression)to(this.F.at(i).TypeIdent(TypeTag.VOID));
/* 3515 */       nextToken();
/*      */     } else {
/*      */       
/* 3518 */       jCExpression = unannotatedType();
/*      */     } 
/* 3520 */     if (this.token.kind == Tokens.TokenKind.LPAREN && !paramBoolean && jCExpression.hasTag(JCTree.Tag.IDENT)) {
/* 3521 */       if (paramBoolean || token.name() != paramName) {
/* 3522 */         error(i, "invalid.meth.decl.ret.type.req", new Object[0]);
/* 3523 */       } else if (list1.nonEmpty()) {
/* 3524 */         illegal(((JCTree.JCAnnotation)list1.head).pos);
/* 3525 */       }  return List.of(methodDeclaratorRest(i, jCModifiers, null, this.names.init, list, paramBoolean, true, comment));
/*      */     } 
/*      */ 
/*      */     
/* 3529 */     i = this.token.pos;
/* 3530 */     Name name = ident();
/* 3531 */     if (this.token.kind == Tokens.TokenKind.LPAREN) {
/* 3532 */       return List.of(methodDeclaratorRest(i, jCModifiers, jCExpression, name, list, paramBoolean, bool, comment));
/*      */     }
/*      */     
/* 3535 */     if (!bool && list.isEmpty()) {
/*      */ 
/*      */       
/* 3538 */       List<JCTree> list3 = variableDeclaratorsRest(i, jCModifiers, jCExpression, name, paramBoolean, comment, new ListBuffer()).toList();
/* 3539 */       storeEnd((JCTree)list3.last(), this.token.endPos);
/* 3540 */       accept(Tokens.TokenKind.SEMI);
/* 3541 */       return list3;
/*      */     } 
/* 3543 */     i = this.token.pos;
/*      */     
/* 3545 */     List<JCTree> list2 = bool ? List.of(toP(this.F.at(i).MethodDef(jCModifiers, name, jCExpression, list, 
/* 3546 */             List.nil(), List.nil(), null, null))) : null;
/*      */     
/* 3548 */     return List.of(syntaxError(this.token.pos, list2, "expected", new Tokens.TokenKind[] { Tokens.TokenKind.LPAREN }));
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
/*      */   protected JCTree methodDeclaratorRest(int paramInt, JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression, Name paramName, List<JCTree.JCTypeParameter> paramList, boolean paramBoolean1, boolean paramBoolean2, Tokens.Comment paramComment) {
/* 3569 */     if (paramBoolean1 && (paramJCModifiers.flags & 0x8L) != 0L) {
/* 3570 */       checkStaticInterfaceMethods();
/*      */     }
/* 3572 */     JCTree.JCVariableDecl jCVariableDecl = this.receiverParam; try {
/*      */       JCTree.JCExpression jCExpression;
/* 3574 */       this.receiverParam = null;
/*      */       
/* 3576 */       List<JCTree.JCVariableDecl> list = formalParameters();
/* 3577 */       if (!paramBoolean2) paramJCExpression = bracketsOpt(paramJCExpression); 
/* 3578 */       List<JCTree.JCExpression> list1 = List.nil();
/* 3579 */       if (this.token.kind == Tokens.TokenKind.THROWS) {
/* 3580 */         nextToken();
/* 3581 */         list1 = qualidentList();
/*      */       } 
/* 3583 */       JCTree.JCBlock jCBlock = null;
/*      */       
/* 3585 */       if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 3586 */         jCBlock = block();
/* 3587 */         jCExpression = null;
/*      */       } else {
/* 3589 */         if (this.token.kind == Tokens.TokenKind.DEFAULT) {
/* 3590 */           accept(Tokens.TokenKind.DEFAULT);
/* 3591 */           jCExpression = annotationValue();
/*      */         } else {
/* 3593 */           jCExpression = null;
/*      */         } 
/* 3595 */         accept(Tokens.TokenKind.SEMI);
/* 3596 */         if (this.token.pos <= this.endPosTable.errorEndPos) {
/*      */           
/* 3598 */           skip(false, true, false, false);
/* 3599 */           if (this.token.kind == Tokens.TokenKind.LBRACE) {
/* 3600 */             jCBlock = block();
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 3606 */       JCTree.JCMethodDecl jCMethodDecl = toP(this.F.at(paramInt).MethodDef(paramJCModifiers, paramName, paramJCExpression, paramList, this.receiverParam, list, list1, jCBlock, jCExpression));
/*      */ 
/*      */       
/* 3609 */       attach((JCTree)jCMethodDecl, paramComment);
/* 3610 */       return (JCTree)jCMethodDecl;
/*      */     } finally {
/* 3612 */       this.receiverParam = jCVariableDecl;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCExpression> qualidentList() {
/* 3619 */     ListBuffer listBuffer = new ListBuffer();
/*      */     
/* 3621 */     List<JCTree.JCAnnotation> list = typeAnnotationsOpt();
/* 3622 */     JCTree.JCExpression jCExpression = qualident(true);
/* 3623 */     if (!list.isEmpty()) {
/* 3624 */       JCTree.JCExpression jCExpression1 = insertAnnotationsToMostInner(jCExpression, list, false);
/* 3625 */       listBuffer.append(jCExpression1);
/*      */     } else {
/* 3627 */       listBuffer.append(jCExpression);
/*      */     } 
/* 3629 */     while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3630 */       nextToken();
/*      */       
/* 3632 */       list = typeAnnotationsOpt();
/* 3633 */       jCExpression = qualident(true);
/* 3634 */       if (!list.isEmpty()) {
/* 3635 */         JCTree.JCExpression jCExpression1 = insertAnnotationsToMostInner(jCExpression, list, false);
/* 3636 */         listBuffer.append(jCExpression1); continue;
/*      */       } 
/* 3638 */       listBuffer.append(jCExpression);
/*      */     } 
/*      */     
/* 3641 */     return listBuffer.toList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCTypeParameter> typeParametersOpt() {
/* 3650 */     if (this.token.kind == Tokens.TokenKind.LT) {
/* 3651 */       checkGenerics();
/* 3652 */       ListBuffer listBuffer = new ListBuffer();
/* 3653 */       nextToken();
/* 3654 */       listBuffer.append(typeParameter());
/* 3655 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3656 */         nextToken();
/* 3657 */         listBuffer.append(typeParameter());
/*      */       } 
/* 3659 */       accept(Tokens.TokenKind.GT);
/* 3660 */       return listBuffer.toList();
/*      */     } 
/* 3662 */     return List.nil();
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
/*      */   JCTree.JCTypeParameter typeParameter() {
/* 3674 */     int i = this.token.pos;
/* 3675 */     List<JCTree.JCAnnotation> list = typeAnnotationsOpt();
/* 3676 */     Name name = ident();
/* 3677 */     ListBuffer listBuffer = new ListBuffer();
/* 3678 */     if (this.token.kind == Tokens.TokenKind.EXTENDS) {
/* 3679 */       nextToken();
/* 3680 */       listBuffer.append(parseType());
/* 3681 */       while (this.token.kind == Tokens.TokenKind.AMP) {
/* 3682 */         nextToken();
/* 3683 */         listBuffer.append(parseType());
/*      */       } 
/*      */     } 
/* 3686 */     return toP(this.F.at(i).TypeParameter(name, listBuffer.toList(), list));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<JCTree.JCVariableDecl> formalParameters() {
/* 3694 */     return formalParameters(false);
/*      */   }
/*      */   List<JCTree.JCVariableDecl> formalParameters(boolean paramBoolean) {
/* 3697 */     ListBuffer listBuffer = new ListBuffer();
/*      */     
/* 3699 */     accept(Tokens.TokenKind.LPAREN);
/* 3700 */     if (this.token.kind != Tokens.TokenKind.RPAREN) {
/* 3701 */       this.allowThisIdent = true;
/* 3702 */       JCTree.JCVariableDecl jCVariableDecl = formalParameter(paramBoolean);
/* 3703 */       if (jCVariableDecl.nameexpr != null) {
/* 3704 */         this.receiverParam = jCVariableDecl;
/*      */       } else {
/* 3706 */         listBuffer.append(jCVariableDecl);
/*      */       } 
/* 3708 */       this.allowThisIdent = false;
/* 3709 */       while ((jCVariableDecl.mods.flags & 0x400000000L) == 0L && this.token.kind == Tokens.TokenKind.COMMA) {
/* 3710 */         nextToken();
/* 3711 */         listBuffer.append(jCVariableDecl = formalParameter(paramBoolean));
/*      */       } 
/*      */     } 
/* 3714 */     accept(Tokens.TokenKind.RPAREN);
/* 3715 */     return listBuffer.toList();
/*      */   }
/*      */   
/*      */   List<JCTree.JCVariableDecl> implicitParameters(boolean paramBoolean) {
/* 3719 */     if (paramBoolean) {
/* 3720 */       accept(Tokens.TokenKind.LPAREN);
/*      */     }
/* 3722 */     ListBuffer listBuffer = new ListBuffer();
/* 3723 */     if (this.token.kind != Tokens.TokenKind.RPAREN && this.token.kind != Tokens.TokenKind.ARROW) {
/* 3724 */       listBuffer.append(implicitParameter());
/* 3725 */       while (this.token.kind == Tokens.TokenKind.COMMA) {
/* 3726 */         nextToken();
/* 3727 */         listBuffer.append(implicitParameter());
/*      */       } 
/*      */     } 
/* 3730 */     if (paramBoolean) {
/* 3731 */       accept(Tokens.TokenKind.RPAREN);
/*      */     }
/* 3733 */     return listBuffer.toList();
/*      */   }
/*      */   
/*      */   JCTree.JCModifiers optFinal(long paramLong) {
/* 3737 */     JCTree.JCModifiers jCModifiers = modifiersOpt();
/* 3738 */     checkNoMods(jCModifiers.flags & 0xFFFFFFFFFFFDFFEFL);
/* 3739 */     jCModifiers.flags |= paramLong;
/* 3740 */     return jCModifiers;
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
/*      */   private JCTree.JCExpression insertAnnotationsToMostInner(JCTree.JCExpression paramJCExpression, List<JCTree.JCAnnotation> paramList, boolean paramBoolean) {
/*      */     JCTree.JCAnnotatedType jCAnnotatedType;
/* 3765 */     int i = getEndPos((JCTree)paramJCExpression);
/* 3766 */     JCTree.JCExpression jCExpression1 = paramJCExpression;
/* 3767 */     JCTree.JCArrayTypeTree jCArrayTypeTree = null;
/* 3768 */     while (TreeInfo.typeIn(jCExpression1).hasTag(JCTree.Tag.TYPEARRAY)) {
/* 3769 */       jCArrayTypeTree = (JCTree.JCArrayTypeTree)TreeInfo.typeIn(jCExpression1);
/* 3770 */       jCExpression1 = jCArrayTypeTree.elemtype;
/*      */     } 
/*      */     
/* 3773 */     if (paramBoolean) {
/* 3774 */       jCExpression1 = (JCTree.JCExpression)to(this.F.at(this.token.pos).TypeArray(jCExpression1));
/*      */     }
/*      */     
/* 3777 */     JCTree.JCExpression jCExpression2 = jCExpression1;
/* 3778 */     if (paramList.nonEmpty()) {
/* 3779 */       JCTree.JCExpression jCExpression = jCExpression1;
/*      */       
/* 3781 */       while (TreeInfo.typeIn(jCExpression1).hasTag(JCTree.Tag.SELECT) || 
/* 3782 */         TreeInfo.typeIn(jCExpression1).hasTag(JCTree.Tag.TYPEAPPLY)) {
/* 3783 */         while (TreeInfo.typeIn(jCExpression1).hasTag(JCTree.Tag.SELECT)) {
/* 3784 */           jCExpression = jCExpression1;
/* 3785 */           jCExpression1 = ((JCTree.JCFieldAccess)TreeInfo.typeIn(jCExpression1)).getExpression();
/*      */         } 
/* 3787 */         while (TreeInfo.typeIn(jCExpression1).hasTag(JCTree.Tag.TYPEAPPLY)) {
/* 3788 */           jCExpression = jCExpression1;
/* 3789 */           jCExpression1 = ((JCTree.JCTypeApply)TreeInfo.typeIn(jCExpression1)).clazz;
/*      */         } 
/*      */       } 
/*      */       
/* 3793 */       JCTree.JCAnnotatedType jCAnnotatedType1 = this.F.at(((JCTree.JCAnnotation)paramList.head).pos).AnnotatedType(paramList, jCExpression1);
/*      */       
/* 3795 */       if (TreeInfo.typeIn(jCExpression).hasTag(JCTree.Tag.TYPEAPPLY)) {
/* 3796 */         ((JCTree.JCTypeApply)TreeInfo.typeIn(jCExpression)).clazz = (JCTree.JCExpression)jCAnnotatedType1;
/* 3797 */       } else if (TreeInfo.typeIn(jCExpression).hasTag(JCTree.Tag.SELECT)) {
/* 3798 */         ((JCTree.JCFieldAccess)TreeInfo.typeIn(jCExpression)).selected = (JCTree.JCExpression)jCAnnotatedType1;
/*      */       } else {
/*      */         
/* 3801 */         jCAnnotatedType = jCAnnotatedType1;
/*      */       } 
/*      */     } 
/*      */     
/* 3805 */     if (jCArrayTypeTree == null) {
/* 3806 */       return (JCTree.JCExpression)jCAnnotatedType;
/*      */     }
/* 3808 */     jCArrayTypeTree.elemtype = (JCTree.JCExpression)jCAnnotatedType;
/* 3809 */     storeEnd((JCTree)paramJCExpression, i);
/* 3810 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JCTree.JCVariableDecl formalParameter() {
/* 3818 */     return formalParameter(false);
/*      */   }
/*      */   protected JCTree.JCVariableDecl formalParameter(boolean paramBoolean) {
/* 3821 */     JCTree.JCModifiers jCModifiers = optFinal(8589934592L);
/*      */ 
/*      */     
/* 3824 */     this.permitTypeAnnotationsPushBack = true;
/* 3825 */     JCTree.JCExpression jCExpression = parseType();
/* 3826 */     this.permitTypeAnnotationsPushBack = false;
/*      */     
/* 3828 */     if (this.token.kind == Tokens.TokenKind.ELLIPSIS) {
/* 3829 */       List<JCTree.JCAnnotation> list = this.typeAnnotationsPushedBack;
/* 3830 */       this.typeAnnotationsPushedBack = List.nil();
/* 3831 */       checkVarargs();
/* 3832 */       jCModifiers.flags |= 0x400000000L;
/*      */       
/* 3834 */       jCExpression = insertAnnotationsToMostInner(jCExpression, list, true);
/* 3835 */       nextToken();
/*      */     } else {
/*      */       
/* 3838 */       if (this.typeAnnotationsPushedBack.nonEmpty()) {
/* 3839 */         reportSyntaxError(((JCTree.JCAnnotation)this.typeAnnotationsPushedBack.head).pos, "illegal.start.of.type", new Object[0]);
/*      */       }
/*      */       
/* 3842 */       this.typeAnnotationsPushedBack = List.nil();
/*      */     } 
/* 3844 */     return variableDeclaratorId(jCModifiers, jCExpression, paramBoolean);
/*      */   }
/*      */   
/*      */   protected JCTree.JCVariableDecl implicitParameter() {
/* 3848 */     JCTree.JCModifiers jCModifiers = this.F.at(this.token.pos).Modifiers(8589934592L);
/* 3849 */     return variableDeclaratorId(jCModifiers, null, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void error(int paramInt, String paramString, Object... paramVarArgs) {
/* 3855 */     this.log.error(JCDiagnostic.DiagnosticFlag.SYNTAX, paramInt, paramString, paramVarArgs);
/*      */   }
/*      */   
/*      */   void error(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 3859 */     this.log.error(JCDiagnostic.DiagnosticFlag.SYNTAX, paramDiagnosticPosition, paramString, paramVarArgs);
/*      */   }
/*      */   
/*      */   void warning(int paramInt, String paramString, Object... paramVarArgs) {
/* 3863 */     this.log.warning(paramInt, paramString, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected JCTree.JCExpression checkExprStat(JCTree.JCExpression paramJCExpression) {
/* 3869 */     if (!TreeInfo.isExpressionStatement(paramJCExpression)) {
/* 3870 */       JCTree.JCErroneous jCErroneous = this.F.at(paramJCExpression.pos).Erroneous(List.of(paramJCExpression));
/* 3871 */       error((JCDiagnostic.DiagnosticPosition)jCErroneous, "not.stmt", new Object[0]);
/* 3872 */       return (JCTree.JCExpression)jCErroneous;
/*      */     } 
/* 3874 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int prec(Tokens.TokenKind paramTokenKind) {
/* 3882 */     JCTree.Tag tag = optag(paramTokenKind);
/* 3883 */     return (tag != JCTree.Tag.NO_TAG) ? TreeInfo.opPrec(tag) : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int earlier(int paramInt1, int paramInt2) {
/* 3891 */     if (paramInt1 == -1)
/* 3892 */       return paramInt2; 
/* 3893 */     if (paramInt2 == -1)
/* 3894 */       return paramInt1; 
/* 3895 */     return (paramInt1 < paramInt2) ? paramInt1 : paramInt2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static JCTree.Tag optag(Tokens.TokenKind paramTokenKind) {
/* 3902 */     switch (paramTokenKind) {
/*      */       case null:
/* 3904 */         return JCTree.Tag.OR;
/*      */       case null:
/* 3906 */         return JCTree.Tag.AND;
/*      */       case null:
/* 3908 */         return JCTree.Tag.BITOR;
/*      */       case null:
/* 3910 */         return JCTree.Tag.BITOR_ASG;
/*      */       case null:
/* 3912 */         return JCTree.Tag.BITXOR;
/*      */       case null:
/* 3914 */         return JCTree.Tag.BITXOR_ASG;
/*      */       case null:
/* 3916 */         return JCTree.Tag.BITAND;
/*      */       case null:
/* 3918 */         return JCTree.Tag.BITAND_ASG;
/*      */       case null:
/* 3920 */         return JCTree.Tag.EQ;
/*      */       case null:
/* 3922 */         return JCTree.Tag.NE;
/*      */       case null:
/* 3924 */         return JCTree.Tag.LT;
/*      */       case null:
/* 3926 */         return JCTree.Tag.GT;
/*      */       case null:
/* 3928 */         return JCTree.Tag.LE;
/*      */       case null:
/* 3930 */         return JCTree.Tag.GE;
/*      */       case null:
/* 3932 */         return JCTree.Tag.SL;
/*      */       case null:
/* 3934 */         return JCTree.Tag.SL_ASG;
/*      */       case null:
/* 3936 */         return JCTree.Tag.SR;
/*      */       case null:
/* 3938 */         return JCTree.Tag.SR_ASG;
/*      */       case null:
/* 3940 */         return JCTree.Tag.USR;
/*      */       case null:
/* 3942 */         return JCTree.Tag.USR_ASG;
/*      */       case null:
/* 3944 */         return JCTree.Tag.PLUS;
/*      */       case null:
/* 3946 */         return JCTree.Tag.PLUS_ASG;
/*      */       case null:
/* 3948 */         return JCTree.Tag.MINUS;
/*      */       case null:
/* 3950 */         return JCTree.Tag.MINUS_ASG;
/*      */       case null:
/* 3952 */         return JCTree.Tag.MUL;
/*      */       case null:
/* 3954 */         return JCTree.Tag.MUL_ASG;
/*      */       case null:
/* 3956 */         return JCTree.Tag.DIV;
/*      */       case null:
/* 3958 */         return JCTree.Tag.DIV_ASG;
/*      */       case null:
/* 3960 */         return JCTree.Tag.MOD;
/*      */       case null:
/* 3962 */         return JCTree.Tag.MOD_ASG;
/*      */       case null:
/* 3964 */         return JCTree.Tag.TYPETEST;
/*      */     } 
/* 3966 */     return JCTree.Tag.NO_TAG;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static JCTree.Tag unoptag(Tokens.TokenKind paramTokenKind) {
/* 3974 */     switch (paramTokenKind) {
/*      */       case null:
/* 3976 */         return JCTree.Tag.POS;
/*      */       case null:
/* 3978 */         return JCTree.Tag.NEG;
/*      */       case null:
/* 3980 */         return JCTree.Tag.NOT;
/*      */       case null:
/* 3982 */         return JCTree.Tag.COMPL;
/*      */       case null:
/* 3984 */         return JCTree.Tag.PREINC;
/*      */       case null:
/* 3986 */         return JCTree.Tag.PREDEC;
/*      */     } 
/* 3988 */     return JCTree.Tag.NO_TAG;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static TypeTag typetag(Tokens.TokenKind paramTokenKind) {
/* 3996 */     switch (paramTokenKind) {
/*      */       case null:
/* 3998 */         return TypeTag.BYTE;
/*      */       case null:
/* 4000 */         return TypeTag.CHAR;
/*      */       case null:
/* 4002 */         return TypeTag.SHORT;
/*      */       case null:
/* 4004 */         return TypeTag.INT;
/*      */       case null:
/* 4006 */         return TypeTag.LONG;
/*      */       case null:
/* 4008 */         return TypeTag.FLOAT;
/*      */       case null:
/* 4010 */         return TypeTag.DOUBLE;
/*      */       case null:
/* 4012 */         return TypeTag.BOOLEAN;
/*      */     } 
/* 4014 */     return TypeTag.NONE;
/*      */   }
/*      */ 
/*      */   
/*      */   void checkGenerics() {
/* 4019 */     if (!this.allowGenerics) {
/* 4020 */       error(this.token.pos, "generics.not.supported.in.source", new Object[] { this.source.name });
/* 4021 */       this.allowGenerics = true;
/*      */     } 
/*      */   }
/*      */   void checkVarargs() {
/* 4025 */     if (!this.allowVarargs) {
/* 4026 */       error(this.token.pos, "varargs.not.supported.in.source", new Object[] { this.source.name });
/* 4027 */       this.allowVarargs = true;
/*      */     } 
/*      */   }
/*      */   void checkForeach() {
/* 4031 */     if (!this.allowForeach) {
/* 4032 */       error(this.token.pos, "foreach.not.supported.in.source", new Object[] { this.source.name });
/* 4033 */       this.allowForeach = true;
/*      */     } 
/*      */   }
/*      */   void checkStaticImports() {
/* 4037 */     if (!this.allowStaticImport) {
/* 4038 */       error(this.token.pos, "static.import.not.supported.in.source", new Object[] { this.source.name });
/* 4039 */       this.allowStaticImport = true;
/*      */     } 
/*      */   }
/*      */   void checkAnnotations() {
/* 4043 */     if (!this.allowAnnotations) {
/* 4044 */       error(this.token.pos, "annotations.not.supported.in.source", new Object[] { this.source.name });
/* 4045 */       this.allowAnnotations = true;
/*      */     } 
/*      */   }
/*      */   void checkDiamond() {
/* 4049 */     if (!this.allowDiamond) {
/* 4050 */       error(this.token.pos, "diamond.not.supported.in.source", new Object[] { this.source.name });
/* 4051 */       this.allowDiamond = true;
/*      */     } 
/*      */   }
/*      */   void checkMulticatch() {
/* 4055 */     if (!this.allowMulticatch) {
/* 4056 */       error(this.token.pos, "multicatch.not.supported.in.source", new Object[] { this.source.name });
/* 4057 */       this.allowMulticatch = true;
/*      */     } 
/*      */   }
/*      */   void checkTryWithResources() {
/* 4061 */     if (!this.allowTWR) {
/* 4062 */       error(this.token.pos, "try.with.resources.not.supported.in.source", new Object[] { this.source.name });
/* 4063 */       this.allowTWR = true;
/*      */     } 
/*      */   }
/*      */   void checkLambda() {
/* 4067 */     if (!this.allowLambda) {
/* 4068 */       this.log.error(this.token.pos, "lambda.not.supported.in.source", new Object[] { this.source.name });
/* 4069 */       this.allowLambda = true;
/*      */     } 
/*      */   }
/*      */   void checkMethodReferences() {
/* 4073 */     if (!this.allowMethodReferences) {
/* 4074 */       this.log.error(this.token.pos, "method.references.not.supported.in.source", new Object[] { this.source.name });
/* 4075 */       this.allowMethodReferences = true;
/*      */     } 
/*      */   }
/*      */   void checkDefaultMethods() {
/* 4079 */     if (!this.allowDefaultMethods) {
/* 4080 */       this.log.error(this.token.pos, "default.methods.not.supported.in.source", new Object[] { this.source.name });
/* 4081 */       this.allowDefaultMethods = true;
/*      */     } 
/*      */   }
/*      */   void checkIntersectionTypesInCast() {
/* 4085 */     if (!this.allowIntersectionTypesInCast) {
/* 4086 */       this.log.error(this.token.pos, "intersection.types.in.cast.not.supported.in.source", new Object[] { this.source.name });
/* 4087 */       this.allowIntersectionTypesInCast = true;
/*      */     } 
/*      */   }
/*      */   void checkStaticInterfaceMethods() {
/* 4091 */     if (!this.allowStaticInterfaceMethods) {
/* 4092 */       this.log.error(this.token.pos, "static.intf.methods.not.supported.in.source", new Object[] { this.source.name });
/* 4093 */       this.allowStaticInterfaceMethods = true;
/*      */     } 
/*      */   }
/*      */   void checkTypeAnnotations() {
/* 4097 */     if (!this.allowTypeAnnotations) {
/* 4098 */       this.log.error(this.token.pos, "type.annotations.not.supported.in.source", new Object[] { this.source.name });
/* 4099 */       this.allowTypeAnnotations = true;
/*      */     } 
/*      */   }
/*      */   void checkAnnotationsAfterTypeParams(int paramInt) {
/* 4103 */     if (!this.allowAnnotationsAfterTypeParams) {
/* 4104 */       this.log.error(paramInt, "annotations.after.type.params.not.supported.in.source", new Object[] { this.source.name });
/* 4105 */       this.allowAnnotationsAfterTypeParams = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected static class SimpleEndPosTable
/*      */     extends AbstractEndPosTable
/*      */   {
/*      */     private final IntHashTable endPosMap;
/*      */ 
/*      */     
/*      */     SimpleEndPosTable(JavacParser param1JavacParser) {
/* 4117 */       super(param1JavacParser);
/* 4118 */       this.endPosMap = new IntHashTable();
/*      */     }
/*      */     
/*      */     public void storeEnd(JCTree param1JCTree, int param1Int) {
/* 4122 */       this.endPosMap.putAtIndex(param1JCTree, (this.errorEndPos > param1Int) ? this.errorEndPos : param1Int, this.endPosMap
/* 4123 */           .lookup(param1JCTree));
/*      */     }
/*      */     
/*      */     protected <T extends JCTree> T to(T param1T) {
/* 4127 */       storeEnd((JCTree)param1T, this.parser.token.endPos);
/* 4128 */       return param1T;
/*      */     }
/*      */     
/*      */     protected <T extends JCTree> T toP(T param1T) {
/* 4132 */       storeEnd((JCTree)param1T, (this.parser.S.prevToken()).endPos);
/* 4133 */       return param1T;
/*      */     }
/*      */     
/*      */     public int getEndPos(JCTree param1JCTree) {
/* 4137 */       int i = this.endPosMap.getFromIndex(this.endPosMap.lookup(param1JCTree));
/*      */       
/* 4139 */       return (i == -1) ? -1 : i;
/*      */     }
/*      */     
/*      */     public int replaceTree(JCTree param1JCTree1, JCTree param1JCTree2) {
/* 4143 */       int i = this.endPosMap.remove(param1JCTree1);
/* 4144 */       if (i != -1) {
/* 4145 */         storeEnd(param1JCTree2, i);
/* 4146 */         return i;
/*      */       } 
/* 4148 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class EmptyEndPosTable
/*      */     extends AbstractEndPosTable
/*      */   {
/*      */     EmptyEndPosTable(JavacParser param1JavacParser) {
/* 4158 */       super(param1JavacParser);
/*      */     }
/*      */     
/*      */     public void storeEnd(JCTree param1JCTree, int param1Int) {}
/*      */     
/*      */     protected <T extends JCTree> T to(T param1T) {
/* 4164 */       return param1T;
/*      */     }
/*      */     
/*      */     protected <T extends JCTree> T toP(T param1T) {
/* 4168 */       return param1T;
/*      */     }
/*      */     
/*      */     public int getEndPos(JCTree param1JCTree) {
/* 4172 */       return -1;
/*      */     }
/*      */     
/*      */     public int replaceTree(JCTree param1JCTree1, JCTree param1JCTree2) {
/* 4176 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static abstract class AbstractEndPosTable
/*      */     implements EndPosTable
/*      */   {
/*      */     protected JavacParser parser;
/*      */ 
/*      */     
/*      */     protected int errorEndPos;
/*      */ 
/*      */ 
/*      */     
/*      */     public AbstractEndPosTable(JavacParser param1JavacParser) {
/* 4193 */       this.parser = param1JavacParser;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected abstract <T extends JCTree> T to(T param1T);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected abstract <T extends JCTree> T toP(T param1T);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void setErrorEndPos(int param1Int) {
/* 4218 */       if (param1Int > this.errorEndPos) {
/* 4219 */         this.errorEndPos = param1Int;
/*      */       }
/*      */     }
/*      */     
/*      */     protected void setParser(JavacParser param1JavacParser) {
/* 4224 */       this.parser = param1JavacParser;
/*      */     }
/*      */   }
/*      */   
/*      */   static interface ErrorRecoveryAction {
/*      */     JCTree doRecover(JavacParser param1JavacParser);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\JavacParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */