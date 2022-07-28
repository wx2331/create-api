/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.tree.ClassTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.MethodTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.tree.VariableTree;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.Plugin;
/*     */ import com.sun.source.util.TaskEvent;
/*     */ import com.sun.source.util.TaskListener;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.source.util.TreePathScanner;
/*     */ import com.sun.tools.javac.api.JavacTaskImpl;
/*     */ import com.sun.tools.javac.api.JavacTool;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DocLint
/*     */   implements Plugin
/*     */ {
/*     */   public static final String XMSGS_OPTION = "-Xmsgs";
/*     */   public static final String XMSGS_CUSTOM_PREFIX = "-Xmsgs:";
/*     */   private static final String STATS = "-stats";
/*     */   public static final String XIMPLICIT_HEADERS = "-XimplicitHeaders:";
/*     */   public static final String XCUSTOM_TAGS_PREFIX = "-XcustomTags:";
/*     */   public static final String TAGS_SEPARATOR = ",";
/*     */   List<File> javacBootClassPath;
/*     */   List<File> javacClassPath;
/*     */   List<File> javacSourcePath;
/*     */   List<String> javacOpts;
/*     */   List<File> javacFiles;
/*     */   
/*     */   public static void main(String... paramVarArgs) {
/*  86 */     DocLint docLint = new DocLint();
/*     */     try {
/*  88 */       docLint.run(paramVarArgs);
/*  89 */     } catch (BadArgs badArgs) {
/*  90 */       System.err.println(badArgs.getMessage());
/*  91 */       System.exit(1);
/*  92 */     } catch (IOException iOException) {
/*  93 */       System.err.println(docLint.localize("dc.main.ioerror", new Object[] { iOException.getLocalizedMessage() }));
/*  94 */       System.exit(2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public class BadArgs
/*     */     extends Exception {
/*     */     private static final long serialVersionUID = 0L;
/*     */     final String code;
/*     */     final Object[] args;
/*     */     
/*     */     BadArgs(String param1String, Object... param1VarArgs) {
/* 105 */       super(DocLint.this.localize(param1String, param1VarArgs));
/* 106 */       this.code = param1String;
/* 107 */       this.args = param1VarArgs;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(String... paramVarArgs) throws BadArgs, IOException {
/* 118 */     PrintWriter printWriter = new PrintWriter(System.out);
/*     */     try {
/* 120 */       run(printWriter, paramVarArgs);
/*     */     } finally {
/* 122 */       printWriter.flush();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void run(PrintWriter paramPrintWriter, String... paramVarArgs) throws BadArgs, IOException {
/* 127 */     this.env = new Env();
/* 128 */     processArgs(paramVarArgs);
/*     */     
/* 130 */     if (this.needHelp) {
/* 131 */       showHelp(paramPrintWriter);
/*     */     }
/* 133 */     if (this.javacFiles.isEmpty() && 
/* 134 */       !this.needHelp) {
/* 135 */       paramPrintWriter.println(localize("dc.main.no.files.given", new Object[0]));
/*     */     }
/*     */     
/* 138 */     JavacTool javacTool = JavacTool.create();
/*     */     
/* 140 */     JavacFileManager javacFileManager = new JavacFileManager(new Context(), false, null);
/* 141 */     javacFileManager.setSymbolFileEnabled(false);
/* 142 */     javacFileManager.setLocation(StandardLocation.PLATFORM_CLASS_PATH, this.javacBootClassPath);
/* 143 */     javacFileManager.setLocation(StandardLocation.CLASS_PATH, this.javacClassPath);
/* 144 */     javacFileManager.setLocation(StandardLocation.SOURCE_PATH, this.javacSourcePath);
/*     */     
/* 146 */     JavacTask javacTask = javacTool.getTask(paramPrintWriter, (JavaFileManager)javacFileManager, null, this.javacOpts, null, javacFileManager
/* 147 */         .getJavaFileObjectsFromFiles(this.javacFiles));
/* 148 */     Iterable iterable = javacTask.parse();
/* 149 */     ((JavacTaskImpl)javacTask).enter();
/*     */     
/* 151 */     this.env.init(javacTask);
/* 152 */     this.checker = new Checker(this.env);
/*     */     
/* 154 */     DeclScanner declScanner = new DeclScanner()
/*     */       {
/*     */         void visitDecl(Tree param1Tree, Name param1Name) {
/* 157 */           TreePath treePath = getCurrentPath();
/* 158 */           DocCommentTree docCommentTree = DocLint.this.env.trees.getDocCommentTree(treePath);
/*     */           
/* 160 */           DocLint.this.checker.scan(docCommentTree, treePath);
/*     */         }
/*     */       };
/*     */     
/* 164 */     declScanner.scan(iterable, null);
/*     */     
/* 166 */     reportStats(paramPrintWriter);
/*     */     
/* 168 */     Context context = ((JavacTaskImpl)javacTask).getContext();
/* 169 */     JavaCompiler javaCompiler = JavaCompiler.instance(context);
/* 170 */     javaCompiler.printCount("error", javaCompiler.errorCount());
/* 171 */     javaCompiler.printCount("warn", javaCompiler.warningCount());
/*     */   }
/*     */   
/*     */   void processArgs(String... paramVarArgs) throws BadArgs {
/* 175 */     this.javacOpts = new ArrayList<>();
/* 176 */     this.javacFiles = new ArrayList<>();
/*     */     
/* 178 */     if (paramVarArgs.length == 0) {
/* 179 */       this.needHelp = true;
/*     */     }
/* 181 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/* 182 */       String str = paramVarArgs[b];
/* 183 */       if (str.matches("-Xmax(errs|warns)") && b + 1 < paramVarArgs.length)
/* 184 */       { if (paramVarArgs[++b].matches("[0-9]+")) {
/* 185 */           this.javacOpts.add(str);
/* 186 */           this.javacOpts.add(paramVarArgs[b]);
/*     */         } else {
/* 188 */           throw new BadArgs("dc.bad.value.for.option", new Object[] { str, paramVarArgs[b] });
/*     */         }  }
/* 190 */       else if (str.equals("-stats"))
/* 191 */       { this.env.messages.setStatsEnabled(true); }
/* 192 */       else if (str.equals("-bootclasspath") && b + 1 < paramVarArgs.length)
/* 193 */       { this.javacBootClassPath = splitPath(paramVarArgs[++b]); }
/* 194 */       else if (str.equals("-classpath") && b + 1 < paramVarArgs.length)
/* 195 */       { this.javacClassPath = splitPath(paramVarArgs[++b]); }
/* 196 */       else if (str.equals("-cp") && b + 1 < paramVarArgs.length)
/* 197 */       { this.javacClassPath = splitPath(paramVarArgs[++b]); }
/* 198 */       else if (str.equals("-sourcepath") && b + 1 < paramVarArgs.length)
/* 199 */       { this.javacSourcePath = splitPath(paramVarArgs[++b]); }
/* 200 */       else if (str.equals("-Xmsgs"))
/* 201 */       { this.env.messages.setOptions(null); }
/* 202 */       else if (str.startsWith("-Xmsgs:"))
/* 203 */       { this.env.messages.setOptions(str.substring(str.indexOf(":") + 1)); }
/* 204 */       else if (str.startsWith("-XcustomTags:"))
/* 205 */       { this.env.setCustomTags(str.substring(str.indexOf(":") + 1)); }
/* 206 */       else if (str.equals("-h") || str.equals("-help") || str.equals("--help") || str
/* 207 */         .equals("-?") || str.equals("-usage"))
/* 208 */       { this.needHelp = true; }
/* 209 */       else { if (str.startsWith("-")) {
/* 210 */           throw new BadArgs("dc.bad.option", new Object[] { str });
/*     */         }
/* 212 */         while (b < paramVarArgs.length)
/* 213 */           this.javacFiles.add(new File(paramVarArgs[b++]));  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   void showHelp(PrintWriter paramPrintWriter) {
/* 219 */     String str = localize("dc.main.usage", new Object[0]);
/* 220 */     for (String str1 : str.split("\n"))
/* 221 */       paramPrintWriter.println(str1); 
/*     */   }
/*     */   
/*     */   List<File> splitPath(String paramString) {
/* 225 */     ArrayList<File> arrayList = new ArrayList();
/* 226 */     for (String str : paramString.split(File.pathSeparator)) {
/* 227 */       if (str.length() > 0)
/* 228 */         arrayList.add(new File(str)); 
/*     */     } 
/* 230 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean needHelp = false;
/*     */ 
/*     */   
/*     */   Env env;
/*     */ 
/*     */   
/*     */   Checker checker;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 246 */     return "doclint";
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(JavacTask paramJavacTask, String... paramVarArgs) {
/* 251 */     init(paramJavacTask, paramVarArgs, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(JavacTask paramJavacTask, String[] paramArrayOfString, boolean paramBoolean) {
/* 259 */     this.env = new Env();
/* 260 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 261 */       String str = paramArrayOfString[b];
/* 262 */       if (str.equals("-Xmsgs")) {
/* 263 */         this.env.messages.setOptions(null);
/* 264 */       } else if (str.startsWith("-Xmsgs:")) {
/* 265 */         this.env.messages.setOptions(str.substring(str.indexOf(":") + 1));
/* 266 */       } else if (str.matches("-XimplicitHeaders:[1-6]")) {
/* 267 */         char c = str.charAt(str.length() - 1);
/* 268 */         this.env.setImplicitHeaders(Character.digit(c, 10));
/* 269 */       } else if (str.startsWith("-XcustomTags:")) {
/* 270 */         this.env.setCustomTags(str.substring(str.indexOf(":") + 1));
/*     */       } else {
/* 272 */         throw new IllegalArgumentException(str);
/*     */       } 
/* 274 */     }  this.env.init(paramJavacTask);
/*     */     
/* 276 */     this.checker = new Checker(this.env);
/*     */     
/* 278 */     if (paramBoolean) {
/* 279 */       final DeclScanner ds = new DeclScanner()
/*     */         {
/*     */           void visitDecl(Tree param1Tree, Name param1Name) {
/* 282 */             TreePath treePath = getCurrentPath();
/* 283 */             DocCommentTree docCommentTree = DocLint.this.env.trees.getDocCommentTree(treePath);
/*     */             
/* 285 */             DocLint.this.checker.scan(docCommentTree, treePath);
/*     */           }
/*     */         };
/*     */       
/* 289 */       TaskListener taskListener = new TaskListener()
/*     */         {
/*     */           public void started(TaskEvent param1TaskEvent) {
/*     */             // Byte code:
/*     */             //   0: getstatic com/sun/tools/doclint/DocLint$4.$SwitchMap$com$sun$source$util$TaskEvent$Kind : [I
/*     */             //   3: aload_1
/*     */             //   4: invokevirtual getKind : ()Lcom/sun/source/util/TaskEvent$Kind;
/*     */             //   7: invokevirtual ordinal : ()I
/*     */             //   10: iaload
/*     */             //   11: lookupswitch default -> 58, 1 -> 28
/*     */             //   28: aload_0
/*     */             //   29: getfield todo : Ljava/util/Queue;
/*     */             //   32: invokeinterface poll : ()Ljava/lang/Object;
/*     */             //   37: checkcast com/sun/source/tree/CompilationUnitTree
/*     */             //   40: dup
/*     */             //   41: astore_2
/*     */             //   42: ifnull -> 58
/*     */             //   45: aload_0
/*     */             //   46: getfield val$ds : Lcom/sun/tools/doclint/DocLint$DeclScanner;
/*     */             //   49: aload_2
/*     */             //   50: aconst_null
/*     */             //   51: invokevirtual scan : (Lcom/sun/source/tree/Tree;Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   54: pop
/*     */             //   55: goto -> 28
/*     */             //   58: return
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #292	-> 0
/*     */             //   #295	-> 28
/*     */             //   #296	-> 45
/*     */             //   #299	-> 58
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void finished(TaskEvent param1TaskEvent) {
/*     */             // Byte code:
/*     */             //   0: getstatic com/sun/tools/doclint/DocLint$4.$SwitchMap$com$sun$source$util$TaskEvent$Kind : [I
/*     */             //   3: aload_1
/*     */             //   4: invokevirtual getKind : ()Lcom/sun/source/util/TaskEvent$Kind;
/*     */             //   7: invokevirtual ordinal : ()I
/*     */             //   10: iaload
/*     */             //   11: lookupswitch default -> 42, 2 -> 28
/*     */             //   28: aload_0
/*     */             //   29: getfield todo : Ljava/util/Queue;
/*     */             //   32: aload_1
/*     */             //   33: invokevirtual getCompilationUnit : ()Lcom/sun/source/tree/CompilationUnitTree;
/*     */             //   36: invokeinterface add : (Ljava/lang/Object;)Z
/*     */             //   41: pop
/*     */             //   42: return
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #303	-> 0
/*     */             //   #305	-> 28
/*     */             //   #308	-> 42
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 310 */           Queue<CompilationUnitTree> todo = new LinkedList<>();
/*     */         };
/*     */       
/* 313 */       paramJavacTask.addTaskListener(taskListener);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void scan(TreePath paramTreePath) {
/* 318 */     DocCommentTree docCommentTree = this.env.trees.getDocCommentTree(paramTreePath);
/* 319 */     this.checker.scan(docCommentTree, paramTreePath);
/*     */   }
/*     */   
/*     */   public void reportStats(PrintWriter paramPrintWriter) {
/* 323 */     this.env.messages.reportStats(paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidOption(String paramString) {
/* 332 */     if (paramString.equals("-Xmsgs"))
/* 333 */       return true; 
/* 334 */     if (paramString.startsWith("-Xmsgs:"))
/* 335 */       return Messages.Options.isValidOptions(paramString.substring("-Xmsgs:".length())); 
/* 336 */     return false;
/*     */   }
/*     */   
/*     */   private String localize(String paramString, Object... paramVarArgs) {
/* 340 */     Messages messages = (this.env != null) ? this.env.messages : new Messages(null);
/* 341 */     return messages.localize(paramString, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class DeclScanner
/*     */     extends TreePathScanner<Void, Void>
/*     */   {
/*     */     public Void visitCompilationUnit(CompilationUnitTree param1CompilationUnitTree, Void param1Void) {
/* 351 */       if (param1CompilationUnitTree.getPackageName() != null) {
/* 352 */         visitDecl((Tree)param1CompilationUnitTree, (Name)null);
/*     */       }
/* 354 */       return (Void)super.visitCompilationUnit(param1CompilationUnitTree, param1Void);
/*     */     }
/*     */ 
/*     */     
/*     */     public Void visitClass(ClassTree param1ClassTree, Void param1Void) {
/* 359 */       visitDecl((Tree)param1ClassTree, param1ClassTree.getSimpleName());
/* 360 */       return (Void)super.visitClass(param1ClassTree, param1Void);
/*     */     }
/*     */ 
/*     */     
/*     */     public Void visitMethod(MethodTree param1MethodTree, Void param1Void) {
/* 365 */       visitDecl((Tree)param1MethodTree, param1MethodTree.getName());
/*     */       
/* 367 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Void visitVariable(VariableTree param1VariableTree, Void param1Void) {
/* 372 */       visitDecl((Tree)param1VariableTree, param1VariableTree.getName());
/* 373 */       return (Void)super.visitVariable(param1VariableTree, param1Void);
/*     */     }
/*     */     
/*     */     abstract void visitDecl(Tree param1Tree, Name param1Name);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclint\DocLint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */