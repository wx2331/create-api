/*      */ package com.sun.tools.javac.main;
/*      */
/*      */ import com.sun.source.tree.CompilationUnitTree;
/*      */ import com.sun.source.util.TaskEvent;
/*      */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*      */ import com.sun.tools.javac.api.MultiTaskListener;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.comp.Annotate;
/*      */ import com.sun.tools.javac.comp.Attr;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Check;
/*      */ import com.sun.tools.javac.comp.CompileStates;
/*      */ import com.sun.tools.javac.comp.Enter;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.comp.Flow;
/*      */ import com.sun.tools.javac.comp.LambdaToMethod;
/*      */ import com.sun.tools.javac.comp.Lower;
/*      */ import com.sun.tools.javac.comp.Todo;
/*      */ import com.sun.tools.javac.comp.TransTypes;
/*      */ import com.sun.tools.javac.file.JavacFileManager;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.ClassWriter;
/*      */ import com.sun.tools.javac.jvm.Gen;
/*      */ import com.sun.tools.javac.jvm.JNIWriter;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.parser.JavacParser;
/*      */ import com.sun.tools.javac.parser.ParserFactory;
/*      */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.Pretty;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Abort;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.BaseFileManager;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.FatalError;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.RichDiagnosticFormatter;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Queue;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import javax.annotation.processing.Processor;
/*      */ import javax.lang.model.SourceVersion;
/*      */ import javax.lang.model.element.TypeElement;
/*      */ import javax.tools.Diagnostic;
/*      */ import javax.tools.DiagnosticListener;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.StandardLocation;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class JavaCompiler
/*      */ {
/*   81 */   protected static final Context.Key<JavaCompiler> compilerKey = new Context.Key();
/*      */   private static final String versionRBName = "com.sun.tools.javac.resources.version";
/*      */   private static ResourceBundle versionRB;
/*      */
/*      */   public static JavaCompiler instance(Context paramContext) {
/*   86 */     JavaCompiler javaCompiler = (JavaCompiler)paramContext.get(compilerKey);
/*   87 */     if (javaCompiler == null)
/*   88 */       javaCompiler = new JavaCompiler(paramContext);
/*   89 */     return javaCompiler;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static String version() {
/*   95 */     return version("release");
/*      */   }
/*      */
/*      */
/*      */
/*      */   public static String fullVersion() {
/*  101 */     return version("full");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private static String version(String paramString) {
/*  108 */     if (versionRB == null) {
/*      */       try {
/*  110 */         versionRB = ResourceBundle.getBundle("com.sun.tools.javac.resources.version");
/*  111 */       } catch (MissingResourceException missingResourceException) {
/*  112 */         return Log.getLocalizedString("version.not.available", new Object[0]);
/*      */       }
/*      */     }
/*      */     try {
/*  116 */       return versionRB.getString(paramString);
/*      */     }
/*  118 */     catch (MissingResourceException missingResourceException) {
/*  119 */       return Log.getLocalizedString("version.not.available", new Object[0]);
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
/*      */   protected enum CompilePolicy
/*      */   {
/*  139 */     ATTR_ONLY,
/*      */
/*      */
/*      */
/*      */
/*      */
/*  145 */     CHECK_ONLY,
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  153 */     SIMPLE,
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  161 */     BY_FILE,
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  169 */     BY_TODO;
/*      */
/*      */     static CompilePolicy decode(String param1String) {
/*  172 */       if (param1String == null)
/*  173 */         return JavaCompiler.DEFAULT_COMPILE_POLICY;
/*  174 */       if (param1String.equals("attr"))
/*  175 */         return ATTR_ONLY;
/*  176 */       if (param1String.equals("check"))
/*  177 */         return CHECK_ONLY;
/*  178 */       if (param1String.equals("simple"))
/*  179 */         return SIMPLE;
/*  180 */       if (param1String.equals("byfile"))
/*  181 */         return BY_FILE;
/*  182 */       if (param1String.equals("bytodo")) {
/*  183 */         return BY_TODO;
/*      */       }
/*  185 */       return JavaCompiler.DEFAULT_COMPILE_POLICY;
/*      */     }
/*      */   }
/*      */
/*  189 */   private static final CompilePolicy DEFAULT_COMPILE_POLICY = CompilePolicy.BY_TODO; public Log log; JCDiagnostic.Factory diagFactory; protected TreeMaker make; protected ClassReader reader; protected ClassWriter writer; protected JNIWriter jniWriter; protected Enter enter; protected Symtab syms; protected Source source; protected Gen gen; protected Names names; protected Attr attr; protected Check chk; protected Flow flow; protected TransTypes transTypes; protected Lower lower; protected Annotate annotate; protected final Name completionFailureName; protected Types types; protected JavaFileManager fileManager; protected ParserFactory parserFactory;
/*      */   protected MultiTaskListener taskListener;
/*      */   protected JavaCompiler delegateCompiler;
/*      */
/*  193 */   protected enum ImplicitSourcePolicy { NONE,
/*      */
/*  195 */     CLASS,
/*      */
/*  197 */     UNSET;
/*      */
/*      */     static ImplicitSourcePolicy decode(String param1String) {
/*  200 */       if (param1String == null)
/*  201 */         return UNSET;
/*  202 */       if (param1String.equals("none"))
/*  203 */         return NONE;
/*  204 */       if (param1String.equals("class")) {
/*  205 */         return CLASS;
/*      */       }
/*  207 */       return UNSET;
/*      */     } }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  308 */   protected final ClassReader.SourceCompleter thisCompleter = new ClassReader.SourceCompleter()
/*      */     {
/*      */       public void complete(Symbol.ClassSymbol param1ClassSymbol) throws Symbol.CompletionFailure
/*      */       {
/*  312 */         JavaCompiler.this.complete(param1ClassSymbol);
/*      */       }
/*      */     };
/*      */
/*      */
/*      */
/*      */
/*      */   protected Options options;
/*      */
/*      */
/*      */
/*      */   protected Context context;
/*      */
/*      */
/*      */
/*      */   protected boolean annotationProcessingOccurred;
/*      */
/*      */
/*      */
/*      */   protected boolean implicitSourceFilesRead;
/*      */
/*      */
/*      */
/*      */   protected CompileStates compileStates;
/*      */
/*      */
/*      */
/*      */   public boolean verbose;
/*      */
/*      */
/*      */
/*      */   public boolean sourceOutput;
/*      */
/*      */
/*      */
/*      */   public boolean stubOutput;
/*      */
/*      */
/*      */
/*      */   public boolean attrParseOnly;
/*      */
/*      */
/*      */
/*      */   boolean relax;
/*      */
/*      */
/*      */
/*      */   public boolean printFlat;
/*      */
/*      */
/*      */
/*      */   public String encoding;
/*      */
/*      */
/*      */
/*      */   public boolean lineDebugInfo;
/*      */
/*      */
/*      */
/*      */   public boolean genEndPos;
/*      */
/*      */
/*      */   protected boolean devVerbose;
/*      */
/*      */
/*      */   protected boolean processPcks;
/*      */
/*      */
/*      */   protected boolean werror;
/*      */
/*      */
/*      */   protected boolean explicitAnnotationProcessingRequested;
/*      */
/*      */
/*      */   protected CompilePolicy compilePolicy;
/*      */
/*      */
/*      */   protected ImplicitSourcePolicy implicitSourcePolicy;
/*      */
/*      */
/*      */   public boolean verboseCompilePolicy;
/*      */
/*      */
/*      */   public CompileStates.CompileState shouldStopPolicyIfError;
/*      */
/*      */
/*      */   public CompileStates.CompileState shouldStopPolicyIfNoError;
/*      */
/*      */
/*      */   public Todo todo;
/*      */
/*      */
/*      */   public List<Closeable> closeables;
/*      */
/*      */
/*      */   protected Set<JavaFileObject> inputFiles;
/*      */
/*      */
/*      */   public boolean keepComments;
/*      */
/*      */
/*      */   private boolean hasBeenUsed;
/*      */
/*      */
/*      */   private long start_msec;
/*      */
/*      */
/*      */   public long elapsed_msec;
/*      */
/*      */
/*      */   protected boolean needRootClasses;
/*      */
/*      */
/*      */   private List<JCTree.JCClassDecl> rootClasses;
/*      */
/*      */
/*      */   boolean processAnnotations;
/*      */
/*      */
/*      */   Log.DeferredDiagnosticHandler deferredDiagnosticHandler;
/*      */
/*      */
/*      */   private JavacProcessingEnvironment procEnvImpl;
/*      */
/*      */
/*      */   HashMap<Env<AttrContext>, Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>>> desugaredEnvs;
/*      */
/*      */
/*      */
/*      */   private void checkForObsoleteOptions(Target paramTarget) {
/*  442 */     boolean bool = false;
/*  443 */     if (this.options.isUnset(Option.XLINT_CUSTOM, "-" + Lint.LintCategory.OPTIONS.option)) {
/*  444 */       if (this.source.compareTo((Enum)Source.JDK1_5) <= 0) {
/*  445 */         this.log.warning(Lint.LintCategory.OPTIONS, "option.obsolete.source", new Object[] { this.source.name });
/*  446 */         bool = true;
/*      */       }
/*      */
/*  449 */       if (paramTarget.compareTo((Enum)Target.JDK1_5) <= 0) {
/*  450 */         this.log.warning(Lint.LintCategory.OPTIONS, "option.obsolete.target", new Object[] { paramTarget.name });
/*  451 */         bool = true;
/*      */       }
/*      */
/*  454 */       if (bool) {
/*  455 */         this.log.warning(Lint.LintCategory.OPTIONS, "option.obsolete.suppression", new Object[0]);
/*      */       }
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
/*      */   public JavaCompiler(Context paramContext) {
/*  513 */     this.explicitAnnotationProcessingRequested = false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  550 */     this.closeables = List.nil();
/*      */
/*      */
/*      */
/*      */
/*      */
/*  556 */     this.inputFiles = new HashSet<>();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  644 */     this.keepComments = false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  815 */     this.hasBeenUsed = false;
/*  816 */     this.start_msec = 0L;
/*  817 */     this.elapsed_msec = 0L;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  929 */     this.needRootClasses = false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1024 */     this.processAnnotations = false;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1031 */     this.procEnvImpl = null;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1360 */     this.desugaredEnvs = new HashMap<>(); this.context = paramContext; paramContext.put(compilerKey, this); if (paramContext.get(JavaFileManager.class) == null) JavacFileManager.preRegister(paramContext);  this.names = Names.instance(paramContext); this.log = Log.instance(paramContext); this.diagFactory = JCDiagnostic.Factory.instance(paramContext); this.reader = ClassReader.instance(paramContext); this.make = TreeMaker.instance(paramContext); this.writer = ClassWriter.instance(paramContext); this.jniWriter = JNIWriter.instance(paramContext); this.enter = Enter.instance(paramContext); this.todo = Todo.instance(paramContext); this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class); this.parserFactory = ParserFactory.instance(paramContext); this.compileStates = CompileStates.instance(paramContext); try { this.syms = Symtab.instance(paramContext); } catch (Symbol.CompletionFailure completionFailure) { this.log.error("cant.access", new Object[] { completionFailure.sym, completionFailure.getDetailValue() }); if (completionFailure instanceof ClassReader.BadClassFile) throw new Abort();  }  this.source = Source.instance(paramContext); Target target = Target.instance(paramContext); this.attr = Attr.instance(paramContext); this.chk = Check.instance(paramContext); this.gen = Gen.instance(paramContext); this.flow = Flow.instance(paramContext); this.transTypes = TransTypes.instance(paramContext); this.lower = Lower.instance(paramContext); this.annotate = Annotate.instance(paramContext); this.types = Types.instance(paramContext); this.taskListener = MultiTaskListener.instance(paramContext); this.reader.sourceCompleter = this.thisCompleter; this.options = Options.instance(paramContext); this.verbose = this.options.isSet(Option.VERBOSE); this.sourceOutput = this.options.isSet(Option.PRINTSOURCE); this.stubOutput = this.options.isSet("-stubs"); this.relax = this.options.isSet("-relax"); this.printFlat = this.options.isSet("-printflat"); this.attrParseOnly = this.options.isSet("-attrparseonly"); this.encoding = this.options.get(Option.ENCODING); this.lineDebugInfo = (this.options.isUnset(Option.G_CUSTOM) || this.options.isSet(Option.G_CUSTOM, "lines")); this.genEndPos = (this.options.isSet(Option.XJCOV) || paramContext.get(DiagnosticListener.class) != null); this.devVerbose = this.options.isSet("dev"); this.processPcks = this.options.isSet("process.packages"); this.werror = this.options.isSet(Option.WERROR); if (this.source.compareTo((Enum)Source.DEFAULT) < 0 && this.options.isUnset(Option.XLINT_CUSTOM, "-" + Lint.LintCategory.OPTIONS.option) && this.fileManager instanceof BaseFileManager && ((BaseFileManager)this.fileManager).isDefaultBootClassPath()) this.log.warning(Lint.LintCategory.OPTIONS, "source.no.bootclasspath", new Object[] { this.source.name });  checkForObsoleteOptions(target); this.verboseCompilePolicy = this.options.isSet("verboseCompilePolicy"); if (this.attrParseOnly) { this.compilePolicy = CompilePolicy.ATTR_ONLY; } else { this.compilePolicy = CompilePolicy.decode(this.options.get("compilePolicy")); }  this.implicitSourcePolicy = ImplicitSourcePolicy.decode(this.options.get("-implicit")); this.completionFailureName = this.options.isSet("failcomplete") ? this.names.fromString(this.options.get("failcomplete")) : null; this.shouldStopPolicyIfError = this.options.isSet("shouldStopPolicy") ? CompileStates.CompileState.valueOf(this.options.get("shouldStopPolicy")) : (this.options.isSet("shouldStopPolicyIfError") ? CompileStates.CompileState.valueOf(this.options.get("shouldStopPolicyIfError")) : CompileStates.CompileState.INIT); this.shouldStopPolicyIfNoError = this.options.isSet("shouldStopPolicyIfNoError") ? CompileStates.CompileState.valueOf(this.options.get("shouldStopPolicyIfNoError")) : CompileStates.CompileState.GENERATE; if (this.options.isUnset("oldDiags")) this.log.setDiagnosticFormatter((DiagnosticFormatter)RichDiagnosticFormatter.instance(paramContext));
/*      */   } protected boolean shouldStop(CompileStates.CompileState paramCompileState) { CompileStates.CompileState compileState = (errorCount() > 0 || unrecoverableError()) ? this.shouldStopPolicyIfError : this.shouldStopPolicyIfNoError; return paramCompileState.isAfter(compileState); } public int errorCount() { if (this.delegateCompiler != null && this.delegateCompiler != this) return this.delegateCompiler.errorCount();  if (this.werror && this.log.nerrors == 0 && this.log.nwarnings > 0) this.log.error("warnings.and.werror", new Object[0]);  return this.log.nerrors; } protected final <T> Queue<T> stopIfError(CompileStates.CompileState paramCompileState, Queue<T> paramQueue) { return shouldStop(paramCompileState) ? (Queue<T>)new ListBuffer() : paramQueue; } protected final <T> List<T> stopIfError(CompileStates.CompileState paramCompileState, List<T> paramList) { return shouldStop(paramCompileState) ? List.nil() : paramList; } public int warningCount() { if (this.delegateCompiler != null && this.delegateCompiler != this) return this.delegateCompiler.warningCount();  return this.log.nwarnings; } public CharSequence readSource(JavaFileObject paramJavaFileObject) { try { this.inputFiles.add(paramJavaFileObject); return paramJavaFileObject.getCharContent(false); } catch (IOException iOException) { this.log.error("error.reading.file", new Object[] { paramJavaFileObject, JavacFileManager.getMessage(iOException) }); return null; }  } protected JCTree.JCCompilationUnit parse(JavaFileObject paramJavaFileObject, CharSequence paramCharSequence) { long l = now(); JCTree.JCCompilationUnit jCCompilationUnit = this.make.TopLevel(List.nil(), null, List.nil()); if (paramCharSequence != null) { if (this.verbose) this.log.printVerbose("parsing.started", new Object[] { paramJavaFileObject });  if (!this.taskListener.isEmpty()) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.PARSE, paramJavaFileObject); this.taskListener.started(taskEvent); this.keepComments = true; this.genEndPos = true; }  JavacParser javacParser = this.parserFactory.newParser(paramCharSequence, keepComments(), this.genEndPos, this.lineDebugInfo); jCCompilationUnit = javacParser.parseCompilationUnit(); if (this.verbose) this.log.printVerbose("parsing.done", new Object[] { Long.toString(elapsed(l)) });  }  jCCompilationUnit.sourcefile = paramJavaFileObject; if (paramCharSequence != null && !this.taskListener.isEmpty()) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.PARSE, (CompilationUnitTree)jCCompilationUnit); this.taskListener.finished(taskEvent); }  return jCCompilationUnit; } protected boolean keepComments() { return (this.keepComments || this.sourceOutput || this.stubOutput); } @Deprecated public JCTree.JCCompilationUnit parse(String paramString) { JavacFileManager javacFileManager = (JavacFileManager)this.fileManager; return parse(javacFileManager.getJavaFileObjectsFromStrings((Iterable)List.of(paramString)).iterator().next()); }
/*      */   public JCTree.JCCompilationUnit parse(JavaFileObject paramJavaFileObject) { JavaFileObject javaFileObject = this.log.useSource(paramJavaFileObject); try { JCTree.JCCompilationUnit jCCompilationUnit = parse(paramJavaFileObject, readSource(paramJavaFileObject)); if (jCCompilationUnit.endPositions != null) this.log.setEndPosTable(paramJavaFileObject, jCCompilationUnit.endPositions);  return jCCompilationUnit; } finally { this.log.useSource(javaFileObject); }  }
/*      */   public Symbol resolveBinaryNameOrIdent(String paramString) { try { Name name = this.names.fromString(paramString.replace("/", ".")); return (Symbol)this.reader.loadClass(name); } catch (Symbol.CompletionFailure completionFailure) { return resolveIdent(paramString); }  }
/*      */   public Symbol resolveIdent(String paramString) { if (paramString.equals("")) return (Symbol)this.syms.errSymbol;  JavaFileObject javaFileObject = this.log.useSource(null); try { JCTree.JCExpression jCExpression = null; for (String str : paramString.split("\\.", -1)) { if (!SourceVersion.isIdentifier(str)) return (Symbol)this.syms.errSymbol;  jCExpression = (JCTree.JCExpression)((jCExpression == null) ? this.make.Ident(this.names.fromString(str)) : this.make.Select(jCExpression, this.names.fromString(str))); }  JCTree.JCCompilationUnit jCCompilationUnit = this.make.TopLevel(List.nil(), null, List.nil()); jCCompilationUnit.packge = this.syms.unnamedPackage; return this.attr.attribIdent((JCTree)jCExpression, jCCompilationUnit); } finally { this.log.useSource(javaFileObject); }  }
/*      */   JavaFileObject printSource(Env<AttrContext> paramEnv, JCTree.JCClassDecl paramJCClassDecl) throws IOException { JavaFileObject javaFileObject = this.fileManager.getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, paramJCClassDecl.sym.flatname.toString(), JavaFileObject.Kind.SOURCE, null); if (this.inputFiles.contains(javaFileObject)) { this.log.error(paramJCClassDecl.pos(), "source.cant.overwrite.input.file", new Object[] { javaFileObject }); return null; }  BufferedWriter bufferedWriter = new BufferedWriter(javaFileObject.openWriter()); try { (new Pretty(bufferedWriter, true)).printUnit(paramEnv.toplevel, paramJCClassDecl); if (this.verbose) this.log.printVerbose("wrote.file", new Object[] { javaFileObject });  } finally { bufferedWriter.close(); }  return javaFileObject; }
/*      */   JavaFileObject genCode(Env<AttrContext> paramEnv, JCTree.JCClassDecl paramJCClassDecl) throws IOException { try { if (this.gen.genClass(paramEnv, paramJCClassDecl) && errorCount() == 0) return this.writer.writeClass(paramJCClassDecl.sym);  } catch (ClassWriter.PoolOverflow poolOverflow) { this.log.error(paramJCClassDecl.pos(), "limit.pool", new Object[0]); } catch (ClassWriter.StringOverflow stringOverflow) { this.log.error(paramJCClassDecl.pos(), "limit.string.overflow", new Object[] { stringOverflow.value.substring(0, 20) }); } catch (Symbol.CompletionFailure completionFailure) { this.chk.completionError(paramJCClassDecl.pos(), completionFailure); }  return null; }
/*      */   public void complete(Symbol.ClassSymbol paramClassSymbol) throws Symbol.CompletionFailure { JCTree.JCCompilationUnit jCCompilationUnit; if (this.completionFailureName == paramClassSymbol.fullname) throw new Symbol.CompletionFailure(paramClassSymbol, "user-selected completion failure by class name");  JavaFileObject javaFileObject1 = paramClassSymbol.classfile; JavaFileObject javaFileObject2 = this.log.useSource(javaFileObject1); try { jCCompilationUnit = parse(javaFileObject1, javaFileObject1.getCharContent(false)); } catch (IOException iOException) { this.log.error("error.reading.file", new Object[] { javaFileObject1, JavacFileManager.getMessage(iOException) }); jCCompilationUnit = this.make.TopLevel(List.nil(), null, List.nil()); } finally { this.log.useSource(javaFileObject2); }  if (!this.taskListener.isEmpty()) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.ENTER, (CompilationUnitTree)jCCompilationUnit); this.taskListener.started(taskEvent); }  this.enter.complete(List.of(jCCompilationUnit), paramClassSymbol); if (!this.taskListener.isEmpty()) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.ENTER, (CompilationUnitTree)jCCompilationUnit); this.taskListener.finished(taskEvent); }  if (this.enter.getEnv((Symbol.TypeSymbol)paramClassSymbol) == null) { boolean bool = jCCompilationUnit.sourcefile.isNameCompatible("package-info", JavaFileObject.Kind.SOURCE); if (bool) { if (this.enter.getEnv((Symbol.TypeSymbol)jCCompilationUnit.packge) == null) { JCDiagnostic jCDiagnostic = this.diagFactory.fragment("file.does.not.contain.package", new Object[] { paramClassSymbol.location() }); this.reader.getClass(); throw new ClassReader.BadClassFile(this.reader, paramClassSymbol, javaFileObject1, jCDiagnostic); }  } else { JCDiagnostic jCDiagnostic = this.diagFactory.fragment("file.doesnt.contain.class", new Object[] { paramClassSymbol.getQualifiedName() }); this.reader.getClass(); throw new ClassReader.BadClassFile(this.reader, paramClassSymbol, javaFileObject1, jCDiagnostic); }  }  this.implicitSourceFilesRead = true; }
/*      */   public void compile(List<JavaFileObject> paramList) throws Throwable { compile(paramList, List.nil(), null); }
/*      */   public void compile(List<JavaFileObject> paramList, List<String> paramList1, Iterable<? extends Processor> paramIterable) { if (paramIterable != null && paramIterable.iterator().hasNext()) this.explicitAnnotationProcessingRequested = true;  if (this.hasBeenUsed) throw new AssertionError("attempt to reuse JavaCompiler");  this.hasBeenUsed = true; this.options.put(Option.XLINT_CUSTOM.text + "-" + Lint.LintCategory.OPTIONS.option, "true"); this.options.remove(Option.XLINT_CUSTOM.text + Lint.LintCategory.OPTIONS.option); this.start_msec = now(); try { initProcessAnnotations(paramIterable); this.delegateCompiler = processAnnotations(enterTrees(stopIfError(CompileStates.CompileState.PARSE, parseFiles((Iterable<JavaFileObject>)paramList))), paramList1); this.delegateCompiler.compile2(); this.delegateCompiler.close(); this.elapsed_msec = this.delegateCompiler.elapsed_msec; } catch (Abort abort) { if (this.devVerbose) abort.printStackTrace(System.err);  } finally { if (this.procEnvImpl != null) this.procEnvImpl.close();  }  }
/* 1370 */   protected void desugar(final Env<AttrContext> env, Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> paramQueue) { if (shouldStop(CompileStates.CompileState.TRANSTYPES)) {
/*      */       return;
/*      */     }
/* 1373 */     if (this.implicitSourcePolicy == ImplicitSourcePolicy.NONE &&
/* 1374 */       !this.inputFiles.contains(env.toplevel.sourcefile)) {
/*      */       return;
/*      */     }
/*      */
/* 1378 */     if (this.compileStates.isDone(env, CompileStates.CompileState.LOWER)) {
/* 1379 */       paramQueue.addAll(this.desugaredEnvs.get(env));
/*      */
/*      */
/*      */
/*      */       return;
/*      */     }
/*      */
/*      */
/*      */
/*      */     class ScanNested
/*      */       extends TreeScanner
/*      */     {
/* 1391 */       Set<Env<AttrContext>> dependencies = new LinkedHashSet<>();
/*      */       protected boolean hasLambdas;
/*      */
/*      */       public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/* 1395 */         Type type = JavaCompiler.this.types.supertype(param1JCClassDecl.sym.type);
/* 1396 */         boolean bool = false;
/* 1397 */         while (!bool && type.hasTag(TypeTag.CLASS)) {
/* 1398 */           Symbol.ClassSymbol classSymbol = type.tsym.outermostClass();
/* 1399 */           Env<AttrContext> env = JavaCompiler.this.enter.getEnv((Symbol.TypeSymbol)classSymbol);
/* 1400 */           if (env != null && env != env) {
/* 1401 */             if (this.dependencies.add(env)) {
/* 1402 */               boolean bool1 = this.hasLambdas;
/*      */               try {
/* 1404 */                 scan(env.tree);
/*      */
/*      */
/*      */               }
/*      */               finally {
/*      */
/*      */
/*      */
/* 1412 */                 this.hasLambdas = bool1;
/*      */               }
/*      */             }
/* 1415 */             bool = true;
/*      */           }
/* 1417 */           type = JavaCompiler.this.types.supertype(type);
/*      */         }
/* 1419 */         super.visitClassDef(param1JCClassDecl);
/*      */       }
/*      */
/*      */       public void visitLambda(JCTree.JCLambda param1JCLambda) {
/* 1423 */         this.hasLambdas = true;
/* 1424 */         super.visitLambda(param1JCLambda);
/*      */       }
/*      */
/*      */       public void visitReference(JCTree.JCMemberReference param1JCMemberReference) {
/* 1428 */         this.hasLambdas = true;
/* 1429 */         super.visitReference(param1JCMemberReference);
/*      */       }
/*      */     };
/* 1432 */     ScanNested scanNested = new ScanNested();
/* 1433 */     scanNested.scan(env.tree);
/* 1434 */     for (Env<AttrContext> env : scanNested.dependencies) {
/* 1435 */       if (!this.compileStates.isDone(env, CompileStates.CompileState.FLOW)) {
/* 1436 */         this.desugaredEnvs.put(env, desugar(flow(attribute(env))));
/*      */       }
/*      */     }
/*      */
/*      */
/* 1441 */     if (shouldStop(CompileStates.CompileState.TRANSTYPES)) {
/*      */       return;
/*      */     }
/* 1444 */     if (this.verboseCompilePolicy) {
/* 1445 */       printNote("[desugar " + env.enclClass.sym + "]");
/*      */     }
/* 1447 */     JavaFileObject javaFileObject = this.log.useSource((env.enclClass.sym.sourcefile != null) ? env.enclClass.sym.sourcefile : env.toplevel.sourcefile);
/*      */
/*      */
/*      */
/*      */
/* 1452 */     try { JCTree jCTree = env.tree;
/*      */
/* 1454 */       this.make.at(0);
/* 1455 */       TreeMaker treeMaker = this.make.forToplevel(env.toplevel);
/*      */
/* 1457 */       if (env.tree instanceof JCTree.JCCompilationUnit) {
/* 1458 */         if (!this.stubOutput && !this.sourceOutput && !this.printFlat) {
/* 1459 */           if (shouldStop(CompileStates.CompileState.LOWER))
/*      */             return;
/* 1461 */           List list = this.lower.translateTopLevelClass(env, env.tree, treeMaker);
/* 1462 */           if (list.head != null) {
/* 1463 */             Assert.check(list.tail.isEmpty());
/* 1464 */             paramQueue.add(new Pair(env, list.head));
/*      */           }
/*      */         }
/*      */
/*      */         return;
/*      */       }
/* 1470 */       if (this.stubOutput) {
/*      */
/*      */
/* 1473 */         JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)env.tree;
/* 1474 */         if (jCTree instanceof JCTree.JCClassDecl && this.rootClasses
/* 1475 */           .contains(jCTree) && ((jCClassDecl.mods.flags & 0x5L) != 0L || jCClassDecl.sym
/*      */
/* 1477 */           .packge().getQualifiedName() == this.names.java_lang)) {
/* 1478 */           paramQueue.add(new Pair(env, removeMethodBodies(jCClassDecl)));
/*      */         }
/*      */
/*      */         return;
/*      */       }
/* 1483 */       if (shouldStop(CompileStates.CompileState.TRANSTYPES)) {
/*      */         return;
/*      */       }
/* 1486 */       env.tree = this.transTypes.translateTopLevelClass(env.tree, treeMaker);
/* 1487 */       this.compileStates.put(env, CompileStates.CompileState.TRANSTYPES);
/*      */
/* 1489 */       if (this.source.allowLambda() && scanNested.hasLambdas) {
/* 1490 */         if (shouldStop(CompileStates.CompileState.UNLAMBDA)) {
/*      */           return;
/*      */         }
/* 1493 */         env.tree = LambdaToMethod.instance(this.context).translateTopLevelClass(env, env.tree, treeMaker);
/* 1494 */         this.compileStates.put(env, CompileStates.CompileState.UNLAMBDA);
/*      */       }
/*      */
/* 1497 */       if (shouldStop(CompileStates.CompileState.LOWER)) {
/*      */         return;
/*      */       }
/* 1500 */       if (this.sourceOutput) {
/*      */
/*      */
/* 1503 */         JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)env.tree;
/* 1504 */         if (jCTree instanceof JCTree.JCClassDecl && this.rootClasses
/* 1505 */           .contains(jCTree)) {
/* 1506 */           paramQueue.add(new Pair(env, jCClassDecl));
/*      */         }
/*      */
/*      */         return;
/*      */       }
/*      */
/* 1512 */       List list1 = this.lower.translateTopLevelClass(env, env.tree, treeMaker);
/* 1513 */       this.compileStates.put(env, CompileStates.CompileState.LOWER);
/*      */
/* 1515 */       if (shouldStop(CompileStates.CompileState.LOWER)) {
/*      */         return;
/*      */       }
/*      */
/* 1519 */       for (List list2 = list1; list2.nonEmpty(); list2 = list2.tail) {
/* 1520 */         JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)list2.head;
/* 1521 */         paramQueue.add(new Pair(env, jCClassDecl));
/*      */       }  }
/*      */     finally
/*      */
/* 1525 */     { this.log.useSource(javaFileObject); }  }
/*      */   private void compile2() { try { Queue<Queue<Env<AttrContext>>> queue; switch (this.compilePolicy) { case CLASSDEF: attribute((Queue<Env<AttrContext>>)this.todo); break;case METHODDEF: flow(attribute((Queue<Env<AttrContext>>)this.todo)); break;case VARDEF: generate(desugar(flow(attribute((Queue<Env<AttrContext>>)this.todo)))); break;case null: queue = this.todo.groupByFile(); while (!queue.isEmpty() && !shouldStop(CompileStates.CompileState.ATTR)) generate(desugar(flow(attribute(queue.remove()))));  break;case null: while (!this.todo.isEmpty()) generate(desugar(flow(attribute((Env<AttrContext>)this.todo.remove()))));  break;default: Assert.error("unknown compile policy"); break; }  } catch (Abort abort) { if (this.devVerbose) abort.printStackTrace(System.err);  }  if (this.verbose) { this.elapsed_msec = elapsed(this.start_msec); this.log.printVerbose("total", new Object[] { Long.toString(this.elapsed_msec) }); }  reportDeferredDiagnostics(); if (!this.log.hasDiagnosticListener()) { printCount("error", errorCount()); printCount("warn", warningCount()); }  }
/*      */   public List<JCTree.JCCompilationUnit> parseFiles(Iterable<JavaFileObject> paramIterable) { if (shouldStop(CompileStates.CompileState.PARSE)) return List.nil();  ListBuffer listBuffer = new ListBuffer(); HashSet<JavaFileObject> hashSet = new HashSet(); for (JavaFileObject javaFileObject : paramIterable) { if (!hashSet.contains(javaFileObject)) { hashSet.add(javaFileObject); listBuffer.append(parse(javaFileObject)); }  }  return listBuffer.toList(); }
/*      */   public List<JCTree.JCCompilationUnit> enterTreesIfNeeded(List<JCTree.JCCompilationUnit> paramList) { if (shouldStop(CompileStates.CompileState.ATTR)) return List.nil();  return enterTrees(paramList); }
/*      */   public List<JCTree.JCCompilationUnit> enterTrees(List<JCTree.JCCompilationUnit> paramList) { if (!this.taskListener.isEmpty()) for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.ENTER, (CompilationUnitTree)jCCompilationUnit); this.taskListener.started(taskEvent); }   this.enter.main(paramList); if (!this.taskListener.isEmpty()) for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.ENTER, (CompilationUnitTree)jCCompilationUnit); this.taskListener.finished(taskEvent); }   if (this.needRootClasses || this.sourceOutput || this.stubOutput) { ListBuffer listBuffer = new ListBuffer(); for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) { List list = jCCompilationUnit.defs; for (; list.nonEmpty(); list = list.tail) { if (list.head instanceof JCTree.JCClassDecl) listBuffer.append(list.head);  }  }  this.rootClasses = listBuffer.toList(); }  for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) this.inputFiles.add(jCCompilationUnit.sourcefile);  return paramList; }
/*      */   public void initProcessAnnotations(Iterable<? extends Processor> paramIterable) { if (this.options.isSet(Option.PROC, "none")) { this.processAnnotations = false; } else if (this.procEnvImpl == null) { this.procEnvImpl = JavacProcessingEnvironment.instance(this.context); this.procEnvImpl.setProcessors(paramIterable); this.processAnnotations = this.procEnvImpl.atLeastOneProcessor(); if (this.processAnnotations) { this.options.put("save-parameter-names", "save-parameter-names"); this.reader.saveParameterNames = true; this.keepComments = true; this.genEndPos = true; if (!this.taskListener.isEmpty()) this.taskListener.started(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING));  this.deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log); } else { this.procEnvImpl.close(); }  }  }
/*      */   public JavaCompiler processAnnotations(List<JCTree.JCCompilationUnit> paramList) { return processAnnotations(paramList, List.nil()); }
/*      */   public JavaCompiler processAnnotations(List<JCTree.JCCompilationUnit> paramList, List<String> paramList1) { if (shouldStop(CompileStates.CompileState.PROCESS)) if (unrecoverableError()) { this.deferredDiagnosticHandler.reportDeferredDiagnostics(); this.log.popDiagnosticHandler((Log.DiagnosticHandler)this.deferredDiagnosticHandler); return this; }   if (!this.processAnnotations) { if (this.options.isSet(Option.PROC, "only")) { this.log.warning("proc.proc-only.requested.no.procs", new Object[0]); this.todo.clear(); }  if (!paramList1.isEmpty()) this.log.error("proc.no.explicit.annotation.processing.requested", new Object[] { paramList1 });  Assert.checkNull(this.deferredDiagnosticHandler); return this; }  Assert.checkNonNull(this.deferredDiagnosticHandler); try { List list1 = List.nil(); List list2 = List.nil(); if (!paramList1.isEmpty()) { if (!explicitAnnotationProcessingRequested()) { this.log.error("proc.no.explicit.annotation.processing.requested", new Object[] { paramList1 }); this.deferredDiagnosticHandler.reportDeferredDiagnostics(); this.log.popDiagnosticHandler((Log.DiagnosticHandler)this.deferredDiagnosticHandler); return this; }  boolean bool = false; for (String str : paramList1) { Symbol symbol = resolveBinaryNameOrIdent(str); if (symbol == null || (symbol.kind == 1 && !this.processPcks) || symbol.kind == 137) { this.log.error("proc.cant.find.class", new Object[] { str }); bool = true; continue; }  try { if (symbol.kind == 1) symbol.complete();  if (symbol.exists()) { if (symbol.kind == 1) { list2 = list2.prepend(symbol); continue; }  list1 = list1.prepend(symbol); continue; }  Assert.check((symbol.kind == 1)); this.log.warning("proc.package.does.not.exist", new Object[] { str }); list2 = list2.prepend(symbol); } catch (Symbol.CompletionFailure completionFailure) { this.log.error("proc.cant.find.class", new Object[] { str }); bool = true; }  }  if (bool) { this.deferredDiagnosticHandler.reportDeferredDiagnostics(); this.log.popDiagnosticHandler((Log.DiagnosticHandler)this.deferredDiagnosticHandler); return this; }  }  try { JavaCompiler javaCompiler = this.procEnvImpl.doProcessing(this.context, paramList, list1, (Iterable)list2, this.deferredDiagnosticHandler); if (javaCompiler != this)
/*      */           javaCompiler.annotationProcessingOccurred = true;  return javaCompiler; } finally { this.procEnvImpl.close(); }  } catch (Symbol.CompletionFailure completionFailure) { this.log.error("cant.access", new Object[] { completionFailure.sym, completionFailure.getDetailValue() }); this.deferredDiagnosticHandler.reportDeferredDiagnostics(); this.log.popDiagnosticHandler((Log.DiagnosticHandler)this.deferredDiagnosticHandler); return this; }  }
/*      */   private boolean unrecoverableError() { if (this.deferredDiagnosticHandler != null)
/*      */       for (JCDiagnostic jCDiagnostic : this.deferredDiagnosticHandler.getDiagnostics()) { if (jCDiagnostic.getKind() == Diagnostic.Kind.ERROR && !jCDiagnostic.isFlagSet(JCDiagnostic.DiagnosticFlag.RECOVERABLE))
/* 1536 */           return true;  }   return false; } public void generate(Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> paramQueue) { generate(paramQueue, null); }
/*      */   boolean explicitAnnotationProcessingRequested() { return (this.explicitAnnotationProcessingRequested || explicitAnnotationProcessingRequested(this.options)); }
/*      */   static boolean explicitAnnotationProcessingRequested(Options paramOptions) { return (paramOptions.isSet(Option.PROCESSOR) || paramOptions.isSet(Option.PROCESSORPATH) || paramOptions.isSet(Option.PROC, "only") || paramOptions.isSet(Option.XPRINT)); }
/*      */   public Queue<Env<AttrContext>> attribute(Queue<Env<AttrContext>> paramQueue) { ListBuffer listBuffer = new ListBuffer(); while (!paramQueue.isEmpty()) listBuffer.append(attribute(paramQueue.remove()));  return stopIfError(CompileStates.CompileState.ATTR, (Queue<Env<AttrContext>>)listBuffer); }
/* 1540 */   public Env<AttrContext> attribute(Env<AttrContext> paramEnv) { if (this.compileStates.isDone(paramEnv, CompileStates.CompileState.ATTR)) return paramEnv;  if (this.verboseCompilePolicy) printNote("[attribute " + paramEnv.enclClass.sym + "]");  if (this.verbose) this.log.printVerbose("checking.attribution", new Object[] { paramEnv.enclClass.sym });  if (!this.taskListener.isEmpty()) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.ANALYZE, (CompilationUnitTree)paramEnv.toplevel, (TypeElement)paramEnv.enclClass.sym); this.taskListener.started(taskEvent); }  JavaFileObject javaFileObject = this.log.useSource((paramEnv.enclClass.sym.sourcefile != null) ? paramEnv.enclClass.sym.sourcefile : paramEnv.toplevel.sourcefile); try { this.attr.attrib(paramEnv); if (errorCount() > 0 && !shouldStop(CompileStates.CompileState.ATTR)) this.attr.postAttr(paramEnv.tree);  this.compileStates.put(paramEnv, CompileStates.CompileState.ATTR); if (this.rootClasses != null && this.rootClasses.contains(paramEnv.enclClass)) reportPublicApi(paramEnv.enclClass.sym);  } finally { this.log.useSource(javaFileObject); }  return paramEnv; } public void reportPublicApi(Symbol.ClassSymbol paramClassSymbol) {} public Queue<Env<AttrContext>> flow(Queue<Env<AttrContext>> paramQueue) { ListBuffer listBuffer = new ListBuffer(); for (Env<AttrContext> env : paramQueue) flow(env, (Queue<Env<AttrContext>>)listBuffer);  return stopIfError(CompileStates.CompileState.FLOW, (Queue<Env<AttrContext>>)listBuffer); } public Queue<Env<AttrContext>> flow(Env<AttrContext> paramEnv) { ListBuffer listBuffer = new ListBuffer(); flow(paramEnv, (Queue<Env<AttrContext>>)listBuffer); return stopIfError(CompileStates.CompileState.FLOW, (Queue<Env<AttrContext>>)listBuffer); } protected void flow(Env<AttrContext> paramEnv, Queue<Env<AttrContext>> paramQueue) { if (this.compileStates.isDone(paramEnv, CompileStates.CompileState.FLOW)) { paramQueue.add(paramEnv); return; }  try { if (shouldStop(CompileStates.CompileState.FLOW)) return;  if (this.relax) { paramQueue.add(paramEnv); return; }  if (this.verboseCompilePolicy) printNote("[flow " + paramEnv.enclClass.sym + "]");  JavaFileObject javaFileObject = this.log.useSource((paramEnv.enclClass.sym.sourcefile != null) ? paramEnv.enclClass.sym.sourcefile : paramEnv.toplevel.sourcefile); try { this.make.at(0); TreeMaker treeMaker = this.make.forToplevel(paramEnv.toplevel); this.flow.analyzeTree(paramEnv, treeMaker); this.compileStates.put(paramEnv, CompileStates.CompileState.FLOW); if (shouldStop(CompileStates.CompileState.FLOW)) return;  paramQueue.add(paramEnv); } finally { this.log.useSource(javaFileObject); }  } finally { if (!this.taskListener.isEmpty()) { TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.ANALYZE, (CompilationUnitTree)paramEnv.toplevel, (TypeElement)paramEnv.enclClass.sym); this.taskListener.finished(taskEvent); }  }  } public Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> desugar(Queue<Env<AttrContext>> paramQueue) { ListBuffer listBuffer = new ListBuffer(); for (Env<AttrContext> env : paramQueue) desugar(env, (Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>>)listBuffer);  return stopIfError(CompileStates.CompileState.FLOW, (Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>>)listBuffer); } public void generate(Queue<Pair<Env<AttrContext>, JCTree.JCClassDecl>> paramQueue, Queue<JavaFileObject> paramQueue1) { if (shouldStop(CompileStates.CompileState.GENERATE)) {
/*      */       return;
/*      */     }
/* 1543 */     boolean bool = (this.stubOutput || this.sourceOutput || this.printFlat) ? true : false;
/*      */
/* 1545 */     for (Pair<Env<AttrContext>, JCTree.JCClassDecl> pair : paramQueue) {
/* 1546 */       Env<AttrContext> env = (Env)pair.fst;
/* 1547 */       JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)pair.snd;
/*      */
/* 1549 */       if (this.verboseCompilePolicy) {
/* 1550 */         printNote("[generate " + (bool ? " source" : "code") + " " + jCClassDecl.sym + "]");
/*      */       }
/*      */
/*      */
/*      */
/* 1555 */       if (!this.taskListener.isEmpty()) {
/* 1556 */         TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.GENERATE, (CompilationUnitTree)env.toplevel, (TypeElement)jCClassDecl.sym);
/* 1557 */         this.taskListener.started(taskEvent);
/*      */       }
/*      */
/* 1560 */       JavaFileObject javaFileObject = this.log.useSource((env.enclClass.sym.sourcefile != null) ? env.enclClass.sym.sourcefile : env.toplevel.sourcefile);
/*      */
/*      */       try {
/*      */         JavaFileObject javaFileObject1;
/*      */
/* 1565 */         if (bool) {
/* 1566 */           javaFileObject1 = printSource(env, jCClassDecl);
/*      */         } else {
/* 1568 */           if (this.fileManager.hasLocation(StandardLocation.NATIVE_HEADER_OUTPUT) && this.jniWriter
/* 1569 */             .needsHeader(jCClassDecl.sym)) {
/* 1570 */             this.jniWriter.write(jCClassDecl.sym);
/*      */           }
/* 1572 */           javaFileObject1 = genCode(env, jCClassDecl);
/*      */         }
/* 1574 */         if (paramQueue1 != null && javaFileObject1 != null)
/* 1575 */           paramQueue1.add(javaFileObject1);
/* 1576 */       } catch (IOException iOException) {
/* 1577 */         this.log.error(jCClassDecl.pos(), "class.cant.write", new Object[] { jCClassDecl.sym, iOException
/* 1578 */               .getMessage() });
/*      */         return;
/*      */       } finally {
/* 1581 */         this.log.useSource(javaFileObject);
/*      */       }
/*      */
/* 1584 */       if (!this.taskListener.isEmpty()) {
/* 1585 */         TaskEvent taskEvent = new TaskEvent(TaskEvent.Kind.GENERATE, (CompilationUnitTree)env.toplevel, (TypeElement)jCClassDecl.sym);
/* 1586 */         this.taskListener.finished(taskEvent);
/*      */       }
/*      */     }  }
/*      */
/*      */
/*      */
/*      */
/*      */   Map<JCTree.JCCompilationUnit, Queue<Env<AttrContext>>> groupByFile(Queue<Env<AttrContext>> paramQueue) {
/* 1594 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/* 1595 */     for (Env<AttrContext> env : paramQueue) {
/* 1596 */       ListBuffer<Env> listBuffer; Queue queue = (Queue)linkedHashMap.get(env.toplevel);
/* 1597 */       if (queue == null) {
/* 1598 */         listBuffer = new ListBuffer();
/* 1599 */         linkedHashMap.put(env.toplevel, listBuffer);
/*      */       }
/* 1601 */       listBuffer.add(env);
/*      */     }
/* 1603 */     return (Map)linkedHashMap;
/*      */   }
/*      */
/*      */   JCTree.JCClassDecl removeMethodBodies(JCTree.JCClassDecl paramJCClassDecl) {
/* 1607 */     final boolean isInterface = ((paramJCClassDecl.mods.flags & 0x200L) != 0L) ? true : false;
/*      */     class MethodBodyRemover
/*      */       extends TreeTranslator {
/*      */       public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/* 1611 */         param1JCMethodDecl.mods.flags &= 0xFFFFFFFFFFFFFFDFL;
/* 1612 */         for (JCTree.JCVariableDecl jCVariableDecl : param1JCMethodDecl.params)
/* 1613 */           jCVariableDecl.mods.flags &= 0xFFFFFFFFFFFFFFEFL;
/* 1614 */         param1JCMethodDecl.body = null;
/* 1615 */         super.visitMethodDef(param1JCMethodDecl);
/*      */       }
/*      */
/*      */       public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 1619 */         if (param1JCVariableDecl.init != null && param1JCVariableDecl.init.type.constValue() == null)
/* 1620 */           param1JCVariableDecl.init = null;
/* 1621 */         super.visitVarDef(param1JCVariableDecl);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/*      */         // Byte code:
/*      */         //   0: new com/sun/tools/javac/util/ListBuffer
/*      */         //   3: dup
/*      */         //   4: invokespecial <init> : ()V
/*      */         //   7: astore_2
/*      */         //   8: aload_1
/*      */         //   9: getfield defs : Lcom/sun/tools/javac/util/List;
/*      */         //   12: astore_3
/*      */         //   13: aload_3
/*      */         //   14: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */         //   17: ifnull -> 352
/*      */         //   20: aload_3
/*      */         //   21: getfield head : Ljava/lang/Object;
/*      */         //   24: checkcast com/sun/tools/javac/tree/JCTree
/*      */         //   27: astore #4
/*      */         //   29: getstatic com/sun/tools/javac/main/JavaCompiler$2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag : [I
/*      */         //   32: aload #4
/*      */         //   34: invokevirtual getTag : ()Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */         //   37: invokevirtual ordinal : ()I
/*      */         //   40: iaload
/*      */         //   41: tableswitch default -> 344, 1 -> 68, 2 -> 152, 3 -> 260
/*      */         //   68: aload_0
/*      */         //   69: getfield val$isInterface : Z
/*      */         //   72: ifne -> 142
/*      */         //   75: aload #4
/*      */         //   77: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */         //   80: getfield mods : Lcom/sun/tools/javac/tree/JCTree$JCModifiers;
/*      */         //   83: getfield flags : J
/*      */         //   86: ldc2_w 5
/*      */         //   89: land
/*      */         //   90: lconst_0
/*      */         //   91: lcmp
/*      */         //   92: ifne -> 142
/*      */         //   95: aload #4
/*      */         //   97: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */         //   100: getfield mods : Lcom/sun/tools/javac/tree/JCTree$JCModifiers;
/*      */         //   103: getfield flags : J
/*      */         //   106: ldc2_w 2
/*      */         //   109: land
/*      */         //   110: lconst_0
/*      */         //   111: lcmp
/*      */         //   112: ifne -> 344
/*      */         //   115: aload #4
/*      */         //   117: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */         //   120: getfield sym : Lcom/sun/tools/javac/code/Symbol$ClassSymbol;
/*      */         //   123: invokevirtual packge : ()Lcom/sun/tools/javac/code/Symbol$PackageSymbol;
/*      */         //   126: invokevirtual getQualifiedName : ()Lcom/sun/tools/javac/util/Name;
/*      */         //   129: aload_0
/*      */         //   130: getfield this$0 : Lcom/sun/tools/javac/main/JavaCompiler;
/*      */         //   133: getfield names : Lcom/sun/tools/javac/util/Names;
/*      */         //   136: getfield java_lang : Lcom/sun/tools/javac/util/Name;
/*      */         //   139: if_acmpne -> 344
/*      */         //   142: aload_2
/*      */         //   143: aload #4
/*      */         //   145: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */         //   148: pop
/*      */         //   149: goto -> 344
/*      */         //   152: aload_0
/*      */         //   153: getfield val$isInterface : Z
/*      */         //   156: ifne -> 250
/*      */         //   159: aload #4
/*      */         //   161: checkcast com/sun/tools/javac/tree/JCTree$JCMethodDecl
/*      */         //   164: getfield mods : Lcom/sun/tools/javac/tree/JCTree$JCModifiers;
/*      */         //   167: getfield flags : J
/*      */         //   170: ldc2_w 5
/*      */         //   173: land
/*      */         //   174: lconst_0
/*      */         //   175: lcmp
/*      */         //   176: ifne -> 250
/*      */         //   179: aload #4
/*      */         //   181: checkcast com/sun/tools/javac/tree/JCTree$JCMethodDecl
/*      */         //   184: getfield sym : Lcom/sun/tools/javac/code/Symbol$MethodSymbol;
/*      */         //   187: getfield name : Lcom/sun/tools/javac/util/Name;
/*      */         //   190: aload_0
/*      */         //   191: getfield this$0 : Lcom/sun/tools/javac/main/JavaCompiler;
/*      */         //   194: getfield names : Lcom/sun/tools/javac/util/Names;
/*      */         //   197: getfield init : Lcom/sun/tools/javac/util/Name;
/*      */         //   200: if_acmpeq -> 250
/*      */         //   203: aload #4
/*      */         //   205: checkcast com/sun/tools/javac/tree/JCTree$JCMethodDecl
/*      */         //   208: getfield mods : Lcom/sun/tools/javac/tree/JCTree$JCModifiers;
/*      */         //   211: getfield flags : J
/*      */         //   214: ldc2_w 2
/*      */         //   217: land
/*      */         //   218: lconst_0
/*      */         //   219: lcmp
/*      */         //   220: ifne -> 344
/*      */         //   223: aload #4
/*      */         //   225: checkcast com/sun/tools/javac/tree/JCTree$JCMethodDecl
/*      */         //   228: getfield sym : Lcom/sun/tools/javac/code/Symbol$MethodSymbol;
/*      */         //   231: invokevirtual packge : ()Lcom/sun/tools/javac/code/Symbol$PackageSymbol;
/*      */         //   234: invokevirtual getQualifiedName : ()Lcom/sun/tools/javac/util/Name;
/*      */         //   237: aload_0
/*      */         //   238: getfield this$0 : Lcom/sun/tools/javac/main/JavaCompiler;
/*      */         //   241: getfield names : Lcom/sun/tools/javac/util/Names;
/*      */         //   244: getfield java_lang : Lcom/sun/tools/javac/util/Name;
/*      */         //   247: if_acmpne -> 344
/*      */         //   250: aload_2
/*      */         //   251: aload #4
/*      */         //   253: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */         //   256: pop
/*      */         //   257: goto -> 344
/*      */         //   260: aload_0
/*      */         //   261: getfield val$isInterface : Z
/*      */         //   264: ifne -> 334
/*      */         //   267: aload #4
/*      */         //   269: checkcast com/sun/tools/javac/tree/JCTree$JCVariableDecl
/*      */         //   272: getfield mods : Lcom/sun/tools/javac/tree/JCTree$JCModifiers;
/*      */         //   275: getfield flags : J
/*      */         //   278: ldc2_w 5
/*      */         //   281: land
/*      */         //   282: lconst_0
/*      */         //   283: lcmp
/*      */         //   284: ifne -> 334
/*      */         //   287: aload #4
/*      */         //   289: checkcast com/sun/tools/javac/tree/JCTree$JCVariableDecl
/*      */         //   292: getfield mods : Lcom/sun/tools/javac/tree/JCTree$JCModifiers;
/*      */         //   295: getfield flags : J
/*      */         //   298: ldc2_w 2
/*      */         //   301: land
/*      */         //   302: lconst_0
/*      */         //   303: lcmp
/*      */         //   304: ifne -> 344
/*      */         //   307: aload #4
/*      */         //   309: checkcast com/sun/tools/javac/tree/JCTree$JCVariableDecl
/*      */         //   312: getfield sym : Lcom/sun/tools/javac/code/Symbol$VarSymbol;
/*      */         //   315: invokevirtual packge : ()Lcom/sun/tools/javac/code/Symbol$PackageSymbol;
/*      */         //   318: invokevirtual getQualifiedName : ()Lcom/sun/tools/javac/util/Name;
/*      */         //   321: aload_0
/*      */         //   322: getfield this$0 : Lcom/sun/tools/javac/main/JavaCompiler;
/*      */         //   325: getfield names : Lcom/sun/tools/javac/util/Names;
/*      */         //   328: getfield java_lang : Lcom/sun/tools/javac/util/Name;
/*      */         //   331: if_acmpne -> 344
/*      */         //   334: aload_2
/*      */         //   335: aload #4
/*      */         //   337: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */         //   340: pop
/*      */         //   341: goto -> 344
/*      */         //   344: aload_3
/*      */         //   345: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */         //   348: astore_3
/*      */         //   349: goto -> 13
/*      */         //   352: aload_1
/*      */         //   353: aload_2
/*      */         //   354: invokevirtual toList : ()Lcom/sun/tools/javac/util/List;
/*      */         //   357: putfield defs : Lcom/sun/tools/javac/util/List;
/*      */         //   360: aload_0
/*      */         //   361: aload_1
/*      */         //   362: invokespecial visitClassDef : (Lcom/sun/tools/javac/tree/JCTree$JCClassDecl;)V
/*      */         //   365: return
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1625	-> 0
/*      */         //   #1626	-> 8
/*      */         //   #1627	-> 20
/*      */         //   #1628	-> 29
/*      */         //   #1630	-> 68
/*      */         //   #1632	-> 123
/*      */         //   #1633	-> 142
/*      */         //   #1636	-> 152
/*      */         //   #1639	-> 231
/*      */         //   #1640	-> 250
/*      */         //   #1643	-> 260
/*      */         //   #1644	-> 315
/*      */         //   #1645	-> 334
/*      */         //   #1626	-> 344
/*      */         //   #1651	-> 352
/*      */         //   #1652	-> 360
/*      */         //   #1653	-> 365
/*      */       }
/*      */     };
/* 1655 */     MethodBodyRemover methodBodyRemover = new MethodBodyRemover();
/* 1656 */     return (JCTree.JCClassDecl)methodBodyRemover.translate((JCTree)paramJCClassDecl);
/*      */   }
/*      */
/*      */   public void reportDeferredDiagnostics() {
/* 1660 */     if (errorCount() == 0 && this.annotationProcessingOccurred && this.implicitSourceFilesRead && this.implicitSourcePolicy == ImplicitSourcePolicy.UNSET)
/*      */     {
/*      */
/*      */
/* 1664 */       if (explicitAnnotationProcessingRequested()) {
/* 1665 */         this.log.warning("proc.use.implicit", new Object[0]);
/*      */       } else {
/* 1667 */         this.log.warning("proc.use.proc.or.implicit", new Object[0]);
/*      */       }  }
/* 1669 */     this.chk.reportDeferredDiagnostics();
/* 1670 */     if (this.log.compressedOutput) {
/* 1671 */       this.log.mandatoryNote(null, "compressed.diags", new Object[0]);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void close() {
/* 1678 */     close(true);
/*      */   }
/*      */
/*      */   public void close(boolean paramBoolean) {
/* 1682 */     this.rootClasses = null;
/* 1683 */     this.reader = null;
/* 1684 */     this.make = null;
/* 1685 */     this.writer = null;
/* 1686 */     this.enter = null;
/* 1687 */     if (this.todo != null)
/* 1688 */       this.todo.clear();
/* 1689 */     this.todo = null;
/* 1690 */     this.parserFactory = null;
/* 1691 */     this.syms = null;
/* 1692 */     this.source = null;
/* 1693 */     this.attr = null;
/* 1694 */     this.chk = null;
/* 1695 */     this.gen = null;
/* 1696 */     this.flow = null;
/* 1697 */     this.transTypes = null;
/* 1698 */     this.lower = null;
/* 1699 */     this.annotate = null;
/* 1700 */     this.types = null;
/*      */
/* 1702 */     this.log.flush();
/*      */     try {
/* 1704 */       this.fileManager.flush();
/* 1705 */     } catch (IOException iOException) {
/* 1706 */       throw new Abort(iOException);
/*      */     } finally {
/* 1708 */       if (this.names != null && paramBoolean)
/* 1709 */         this.names.dispose();
/* 1710 */       this.names = null;
/*      */
/* 1712 */       for (Closeable closeable : this.closeables) {
/*      */         try {
/* 1714 */           closeable.close();
/* 1715 */         } catch (IOException iOException) {
/*      */
/*      */
/*      */
/*      */
/* 1720 */           JCDiagnostic jCDiagnostic = this.diagFactory.fragment("fatal.err.cant.close", new Object[0]);
/* 1721 */           throw new FatalError(jCDiagnostic, iOException);
/*      */         }
/*      */       }
/* 1724 */       this.closeables = List.nil();
/*      */     }
/*      */   }
/*      */
/*      */   protected void printNote(String paramString) {
/* 1729 */     this.log.printRawLines(Log.WriterKind.NOTICE, paramString);
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void printCount(String paramString, int paramInt) {
/* 1735 */     if (paramInt != 0) {
/*      */       String str;
/* 1737 */       if (paramInt == 1) {
/* 1738 */         str = "count." + paramString;
/*      */       } else {
/* 1740 */         str = "count." + paramString + ".plural";
/* 1741 */       }  this.log.printLines(Log.WriterKind.ERROR, str, new Object[] { String.valueOf(paramInt) });
/* 1742 */       this.log.flush(Log.WriterKind.ERROR);
/*      */     }
/*      */   }
/*      */
/*      */   private static long now() {
/* 1747 */     return System.currentTimeMillis();
/*      */   }
/*      */
/*      */   private static long elapsed(long paramLong) {
/* 1751 */     return now() - paramLong;
/*      */   }
/*      */
/*      */   public void initRound(JavaCompiler paramJavaCompiler) {
/* 1755 */     this.genEndPos = paramJavaCompiler.genEndPos;
/* 1756 */     this.keepComments = paramJavaCompiler.keepComments;
/* 1757 */     this.start_msec = paramJavaCompiler.start_msec;
/* 1758 */     this.hasBeenUsed = true;
/* 1759 */     this.closeables = paramJavaCompiler.closeables;
/* 1760 */     paramJavaCompiler.closeables = List.nil();
/* 1761 */     this.shouldStopPolicyIfError = paramJavaCompiler.shouldStopPolicyIfError;
/* 1762 */     this.shouldStopPolicyIfNoError = paramJavaCompiler.shouldStopPolicyIfNoError;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\main\JavaCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
