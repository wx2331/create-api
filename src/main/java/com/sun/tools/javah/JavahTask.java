/*     */ package com.sun.tools.javah;
/*     */
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.main.CommandLine;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVisitor;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.SimpleTypeVisitor8;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ import javax.tools.ToolProvider;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class JavahTask
/*     */   implements NativeHeaderTool.NativeHeaderTask
/*     */ {
/*     */   public class BadArgs
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 1479361270874789045L;
/*     */     final String key;
/*     */     final Object[] args;
/*     */     boolean showUsage;
/*     */
/*     */     BadArgs(String param1String, Object... param1VarArgs) {
/*  96 */       super(JavahTask.this.getMessage(param1String, param1VarArgs));
/*  97 */       this.key = param1String;
/*  98 */       this.args = param1VarArgs;
/*     */     }
/*     */
/*     */     BadArgs showUsage(boolean param1Boolean) {
/* 102 */       this.showUsage = param1Boolean;
/* 103 */       return this;
/*     */     }
/*     */   }
/*     */
/*     */   static abstract class Option
/*     */   {
/*     */     final boolean hasArg;
/*     */     final String[] aliases;
/*     */
/*     */     Option(boolean param1Boolean, String... param1VarArgs) {
/* 113 */       this.hasArg = param1Boolean;
/* 114 */       this.aliases = param1VarArgs;
/*     */     }
/*     */
/*     */     boolean isHidden() {
/* 118 */       return false;
/*     */     }
/*     */
/*     */     boolean matches(String param1String) {
/* 122 */       for (String str : this.aliases) {
/* 123 */         if (str.equals(param1String))
/* 124 */           return true;
/*     */       }
/* 126 */       return false;
/*     */     }
/*     */
/*     */     boolean ignoreRest() {
/* 130 */       return false;
/*     */     }
/*     */
/*     */
/*     */     abstract void process(JavahTask param1JavahTask, String param1String1, String param1String2) throws BadArgs;
/*     */   }
/*     */
/*     */   static abstract class HiddenOption
/*     */     extends Option
/*     */   {
/*     */     HiddenOption(boolean param1Boolean, String... param1VarArgs) {
/* 141 */       super(param1Boolean, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */     boolean isHidden() {
/* 146 */       return true;
/*     */     }
/*     */   }
/*     */
/* 150 */   static final Option[] recognizedOptions = new Option[] { new Option(true, new String[] { "-o" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2) {
/* 153 */           param1JavahTask.ofile = new File(param1String2);
/*     */         }
/*     */       }, new Option(true, new String[] { "-d" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 159 */           param1JavahTask.odir = new File(param1String2);
/*     */         }
/*     */       }, new HiddenOption(true, new String[] { "-td" })
/*     */       {
/*     */
/*     */
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2) {}
/*     */       }, new HiddenOption(false, new String[] { "-stubs" })
/*     */       {
/*     */
/*     */
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2) {}
/*     */       }, new Option(false, new String[] { "-v", "-verbose" })
/*     */       {
/*     */
/*     */
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 177 */           param1JavahTask.verbose = true;
/*     */         }
/*     */       }, new Option(false, new String[] { "-h", "-help", "--help", "-?" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 183 */           param1JavahTask.help = true;
/*     */         }
/*     */       }, new HiddenOption(false, new String[] { "-trace" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 189 */           param1JavahTask.trace = true;
/*     */         }
/*     */       }, new Option(false, new String[] { "-version" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 195 */           param1JavahTask.version = true;
/*     */         }
/*     */       }, new HiddenOption(false, new String[] { "-fullversion" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 201 */           param1JavahTask.fullVersion = true;
/*     */         }
/*     */       }, new Option(false, new String[] { "-jni" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 207 */           param1JavahTask.jni = true;
/*     */         }
/*     */       },
/*     */       new Option(false, new String[] { "-force" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2) {
/* 213 */           param1JavahTask.force = true;
/*     */         }
/*     */       }, new HiddenOption(false, new String[] { "-Xnew" })
/*     */       {
/*     */
/*     */
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2) {}
/*     */       }, new HiddenOption(false, new String[] { "-llni", "-Xllni" })
/*     */       {
/*     */
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 225 */           param1JavahTask.llni = true;
/*     */         }
/*     */       }, new HiddenOption(false, new String[] { "-llnidouble" })
/*     */       {
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2)
/*     */         {
/* 231 */           param1JavahTask.llni = true;
/* 232 */           param1JavahTask.doubleAlign = true;
/*     */         }
/*     */       }, new HiddenOption(false, new String[0])
/*     */       {
/*     */         boolean matches(String param1String)
/*     */         {
/* 238 */           return param1String.startsWith("-XD");
/*     */         }
/*     */         void process(JavahTask param1JavahTask, String param1String1, String param1String2) {
/* 241 */           param1JavahTask.javac_extras.add(param1String1);
/*     */         }
/*     */       } };
/*     */   private static final String versionRBName = "com.sun.tools.javah.resources.version";
/*     */   private static ResourceBundle versionRB;
/*     */   File ofile;
/*     */   File odir;
/*     */   String bootcp;
/*     */   String usercp;
/*     */   List<String> classes;
/*     */   boolean verbose;
/*     */
/*     */   JavahTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2) {
/* 254 */     this();
/* 255 */     this.log = getPrintWriterForWriter(paramWriter);
/* 256 */     this.fileManager = paramJavaFileManager;
/* 257 */     this.diagnosticListener = paramDiagnosticListener;
/*     */
/*     */     try {
/* 260 */       handleOptions(paramIterable1, false);
/* 261 */     } catch (BadArgs badArgs) {
/* 262 */       throw new IllegalArgumentException(badArgs.getMessage());
/*     */     }
/*     */
/* 265 */     this.classes = new ArrayList<>();
/* 266 */     if (paramIterable2 != null)
/* 267 */       for (String str : paramIterable2) {
/* 268 */         str.getClass();
/* 269 */         this.classes.add(str);
/*     */       }
/*     */   }
/*     */   boolean noArgs; boolean help; boolean trace; boolean version; boolean fullVersion; boolean jni; boolean llni; boolean doubleAlign; boolean force;
/*     */
/*     */   public void setLocale(Locale paramLocale) {
/* 275 */     if (paramLocale == null)
/* 276 */       paramLocale = Locale.getDefault();
/* 277 */     this.task_locale = paramLocale;
/*     */   }
/*     */
/*     */   public void setLog(PrintWriter paramPrintWriter) {
/* 281 */     this.log = paramPrintWriter;
/*     */   }
/*     */
/*     */   public void setLog(OutputStream paramOutputStream) {
/* 285 */     setLog(getPrintWriterForStream(paramOutputStream));
/*     */   }
/*     */
/*     */   static PrintWriter getPrintWriterForStream(OutputStream paramOutputStream) {
/* 289 */     return new PrintWriter(paramOutputStream, true);
/*     */   }
/*     */
/*     */   static PrintWriter getPrintWriterForWriter(Writer paramWriter) {
/* 293 */     if (paramWriter == null)
/* 294 */       return getPrintWriterForStream(null);
/* 295 */     if (paramWriter instanceof PrintWriter) {
/* 296 */       return (PrintWriter)paramWriter;
/*     */     }
/* 298 */     return new PrintWriter(paramWriter, true);
/*     */   }
/*     */
/*     */   public void setDiagnosticListener(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener) {
/* 302 */     this.diagnosticListener = paramDiagnosticListener;
/*     */   }
/*     */
/*     */   public void setDiagnosticListener(OutputStream paramOutputStream) {
/* 306 */     setDiagnosticListener(getDiagnosticListenerForStream(paramOutputStream));
/*     */   }
/*     */
/*     */   private DiagnosticListener<JavaFileObject> getDiagnosticListenerForStream(OutputStream paramOutputStream) {
/* 310 */     return getDiagnosticListenerForWriter(getPrintWriterForStream(paramOutputStream));
/*     */   }
/*     */
/*     */   private DiagnosticListener<JavaFileObject> getDiagnosticListenerForWriter(Writer paramWriter) {
/* 314 */     final PrintWriter pw = getPrintWriterForWriter(paramWriter);
/* 315 */     return new DiagnosticListener<JavaFileObject>() {
/*     */         public void report(Diagnostic<? extends JavaFileObject> param1Diagnostic) {
/* 317 */           if (param1Diagnostic.getKind() == Diagnostic.Kind.ERROR) {
/* 318 */             pw.print(JavahTask.this.getMessage("err.prefix", new Object[0]));
/* 319 */             pw.print(" ");
/*     */           }
/* 321 */           pw.println(param1Diagnostic.getMessage(null));
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */   int run(String[] paramArrayOfString) {
/*     */     try {
/* 328 */       handleOptions(paramArrayOfString);
/* 329 */       boolean bool = run();
/* 330 */       return bool ? 0 : 1;
/* 331 */     } catch (BadArgs badArgs) {
/* 332 */       this.diagnosticListener.report(createDiagnostic(badArgs.key, badArgs.args));
/* 333 */       return 1;
/* 334 */     } catch (InternalError internalError) {
/* 335 */       this.diagnosticListener.report(createDiagnostic("err.internal.error", new Object[] { internalError.getMessage() }));
/* 336 */       return 1;
/* 337 */     } catch (Exit exit) {
/* 338 */       return exit.exitValue;
/*     */     } finally {
/* 340 */       this.log.flush();
/*     */     }
/*     */   }
/*     */
/*     */   public void handleOptions(String[] paramArrayOfString) throws BadArgs {
/* 345 */     handleOptions(Arrays.asList(paramArrayOfString), true);
/*     */   }
/*     */
/*     */   private void handleOptions(Iterable<String> paramIterable, boolean paramBoolean) throws BadArgs {
/* 349 */     if (this.log == null) {
/* 350 */       this.log = getPrintWriterForStream(System.out);
/* 351 */       if (this.diagnosticListener == null) {
/* 352 */         this.diagnosticListener = getDiagnosticListenerForStream(System.err);
/*     */       }
/* 354 */     } else if (this.diagnosticListener == null) {
/* 355 */       this.diagnosticListener = getDiagnosticListenerForWriter(this.log);
/*     */     }
/*     */
/* 358 */     if (this.fileManager == null) {
/* 359 */       this.fileManager = getDefaultFileManager(this.diagnosticListener, this.log);
/*     */     }
/* 361 */     Iterator<String> iterator = expandAtArgs(paramIterable).iterator();
/* 362 */     this.noArgs = !iterator.hasNext();
/*     */
/* 364 */     while (iterator.hasNext()) {
/* 365 */       String str = iterator.next();
/* 366 */       if (str.startsWith("-")) {
/* 367 */         handleOption(str, iterator); continue;
/* 368 */       }  if (paramBoolean) {
/* 369 */         if (this.classes == null)
/* 370 */           this.classes = new ArrayList<>();
/* 371 */         this.classes.add(str);
/* 372 */         while (iterator.hasNext())
/* 373 */           this.classes.add(iterator.next());  continue;
/*     */       }
/* 375 */       throw (new BadArgs("err.unknown.option", new Object[] { str })).showUsage(true);
/*     */     }
/*     */
/* 378 */     if ((this.classes == null || this.classes.size() == 0) && !this.noArgs && !this.help && !this.version && !this.fullVersion)
/*     */     {
/* 380 */       throw new BadArgs("err.no.classes.specified", new Object[0]);
/*     */     }
/*     */
/* 383 */     if (this.jni && this.llni) {
/* 384 */       throw new BadArgs("jni.llni.mixed", new Object[0]);
/*     */     }
/* 386 */     if (this.odir != null && this.ofile != null)
/* 387 */       throw new BadArgs("dir.file.mixed", new Object[0]);
/*     */   }
/*     */
/*     */   private void handleOption(String paramString, Iterator<String> paramIterator) throws BadArgs {
/* 391 */     for (Option option : recognizedOptions) {
/* 392 */       if (option.matches(paramString)) {
/* 393 */         if (option.hasArg)
/* 394 */         { if (paramIterator.hasNext()) {
/* 395 */             option.process(this, paramString, paramIterator.next());
/*     */           } else {
/* 397 */             throw (new BadArgs("err.missing.arg", new Object[] { paramString })).showUsage(true);
/*     */           }  }
/* 399 */         else { option.process(this, paramString, null); }
/*     */
/* 401 */         if (option.ignoreRest()) {
/* 402 */           while (paramIterator.hasNext()) {
/* 403 */             paramIterator.next();
/*     */           }
/*     */         }
/*     */         return;
/*     */       }
/*     */     }
/* 409 */     if (this.fileManager.handleOption(paramString, paramIterator)) {
/*     */       return;
/*     */     }
/* 412 */     throw (new BadArgs("err.unknown.option", new Object[] { paramString })).showUsage(true);
/*     */   }
/*     */
/*     */   private Iterable<String> expandAtArgs(Iterable<String> paramIterable) throws BadArgs {
/*     */     try {
/* 417 */       ArrayList<String> arrayList = new ArrayList();
/* 418 */       for (String str : paramIterable) arrayList.add(str);
/* 419 */       return Arrays.asList(CommandLine.parse(arrayList.<String>toArray(new String[arrayList.size()])));
/* 420 */     } catch (FileNotFoundException fileNotFoundException) {
/* 421 */       throw new BadArgs("at.args.file.not.found", new Object[] { fileNotFoundException.getLocalizedMessage() });
/* 422 */     } catch (IOException iOException) {
/* 423 */       throw new BadArgs("at.args.io.exception", new Object[] { iOException.getLocalizedMessage() });
/*     */     }
/*     */   }
/*     */
/*     */   public Boolean call() {
/* 428 */     return Boolean.valueOf(run());
/*     */   }
/*     */
/*     */   public boolean run() throws Util.Exit {
/*     */     JNI jNI;
/* 433 */     Util util = new Util(this.log, this.diagnosticListener);
/*     */
/* 435 */     if (this.noArgs || this.help) {
/* 436 */       showHelp();
/* 437 */       return this.help;
/*     */     }
/*     */
/* 440 */     if (this.version || this.fullVersion) {
/* 441 */       showVersion(this.fullVersion);
/* 442 */       return true;
/*     */     }
/*     */
/* 445 */     util.verbose = this.verbose;
/*     */
/*     */
/*     */
/* 449 */     if (this.llni) {
/* 450 */       LLNI lLNI = new LLNI(this.doubleAlign, util);
/*     */     }
/*     */     else {
/*     */
/* 454 */       jNI = new JNI(util);
/*     */     }
/*     */
/* 457 */     if (this.ofile != null) {
/* 458 */       if (!(this.fileManager instanceof StandardJavaFileManager)) {
/* 459 */         this.diagnosticListener.report(createDiagnostic("err.cant.use.option.for.fm", new Object[] { "-o" }));
/* 460 */         return false;
/*     */       }
/*     */
/* 463 */       Iterable<? extends JavaFileObject> iterable = ((StandardJavaFileManager)this.fileManager).getJavaFileObjectsFromFiles(Collections.singleton(this.ofile));
/* 464 */       JavaFileObject javaFileObject = iterable.iterator().next();
/* 465 */       jNI.setOutFile(javaFileObject);
/*     */     } else {
/* 467 */       if (this.odir != null) {
/* 468 */         if (!(this.fileManager instanceof StandardJavaFileManager)) {
/* 469 */           this.diagnosticListener.report(createDiagnostic("err.cant.use.option.for.fm", new Object[] { "-d" }));
/* 470 */           return false;
/*     */         }
/*     */
/* 473 */         if (!this.odir.exists() &&
/* 474 */           !this.odir.mkdirs())
/* 475 */           util.error("cant.create.dir", new Object[] { this.odir.toString() });
/*     */         try {
/* 477 */           ((StandardJavaFileManager)this.fileManager).setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(this.odir));
/* 478 */         } catch (IOException iOException1) {
/* 479 */           IOException iOException2; String str = iOException1.getLocalizedMessage();
/* 480 */           if (str == null) {
/* 481 */             iOException2 = iOException1;
/*     */           }
/* 483 */           this.diagnosticListener.report(createDiagnostic("err.ioerror", new Object[] { this.odir, iOException2 }));
/* 484 */           return false;
/*     */         }
/*     */       }
/* 487 */       jNI.setFileManager(this.fileManager);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 494 */     jNI.setForce(this.force);
/*     */
/* 496 */     if (this.fileManager instanceof JavahFileManager) {
/* 497 */       ((JavahFileManager)this.fileManager).setSymbolFileEnabled(false);
/*     */     }
/* 499 */     JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
/* 500 */     ArrayList<String> arrayList = new ArrayList();
/* 501 */     arrayList.add("-proc:only");
/* 502 */     arrayList.addAll(this.javac_extras);
/* 503 */     JavaCompiler.CompilationTask compilationTask = javaCompiler.getTask(this.log, this.fileManager, this.diagnosticListener, arrayList, this.classes, null);
/* 504 */     JavahProcessor javahProcessor = new JavahProcessor(jNI);
/* 505 */     compilationTask.setProcessors(Collections.singleton(javahProcessor));
/*     */
/* 507 */     boolean bool = compilationTask.call().booleanValue();
/* 508 */     if (javahProcessor.exit != null)
/* 509 */       throw new Util.Exit(javahProcessor.exit);
/* 510 */     return bool;
/*     */   }
/*     */
/*     */   private List<File> pathToFiles(String paramString) {
/* 514 */     ArrayList<File> arrayList = new ArrayList();
/* 515 */     for (String str : paramString.split(File.pathSeparator)) {
/* 516 */       if (str.length() > 0)
/* 517 */         arrayList.add(new File(str));
/*     */     }
/* 519 */     return arrayList;
/*     */   }
/*     */
/*     */   static StandardJavaFileManager getDefaultFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, PrintWriter paramPrintWriter) {
/* 523 */     return (StandardJavaFileManager)JavahFileManager.create(paramDiagnosticListener, paramPrintWriter);
/*     */   }
/*     */
/*     */   private void showHelp() {
/* 527 */     this.log.println(getMessage("main.usage", new Object[] { "javah" }));
/* 528 */     for (Option option : recognizedOptions) {
/* 529 */       if (!option.isHidden()) {
/*     */
/* 531 */         String str = option.aliases[0].substring(1);
/* 532 */         this.log.println(getMessage("main.opt." + str, new Object[0]));
/*     */       }
/* 534 */     }  String[] arrayOfString = { "-classpath", "-cp", "-bootclasspath" };
/* 535 */     for (String str : arrayOfString) {
/* 536 */       if (this.fileManager.isSupportedOption(str) != -1) {
/*     */
/* 538 */         String str1 = str.substring(1);
/* 539 */         this.log.println(getMessage("main.opt." + str1, new Object[0]));
/*     */       }
/* 541 */     }  this.log.println(getMessage("main.usage.foot", new Object[0]));
/*     */   }
/*     */
/*     */   private void showVersion(boolean paramBoolean) {
/* 545 */     this.log.println(version(paramBoolean));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private String version(boolean paramBoolean) {
/* 552 */     String str1 = paramBoolean ? "javah.fullVersion" : "javah.version";
/* 553 */     String str2 = paramBoolean ? "full" : "release";
/*     */
/*     */
/* 556 */     if (versionRB == null) {
/*     */       try {
/* 558 */         versionRB = ResourceBundle.getBundle("com.sun.tools.javah.resources.version");
/* 559 */       } catch (MissingResourceException missingResourceException) {
/* 560 */         return getMessage("version.resource.missing", new Object[] { System.getProperty("java.version") });
/*     */       }
/*     */     }
/*     */     try {
/* 564 */       return getMessage(str1, new Object[] { "javah", versionRB.getString(str2) });
/*     */     }
/* 566 */     catch (MissingResourceException missingResourceException) {
/* 567 */       return getMessage("version.unknown", new Object[] { System.getProperty("java.version") });
/*     */     }
/*     */   }
/*     */
/*     */   private Diagnostic<JavaFileObject> createDiagnostic(final String key, Object... args) {
/* 572 */     return new Diagnostic<JavaFileObject>() {
/*     */         public Kind getKind() {
/* 574 */           return Kind.ERROR;
/*     */         }
/*     */
/*     */         public JavaFileObject getSource() {
/* 578 */           return null;
/*     */         }
/*     */
/*     */         public long getPosition() {
/* 582 */           return -1L;
/*     */         }
/*     */
/*     */         public long getStartPosition() {
/* 586 */           return -1L;
/*     */         }
/*     */
/*     */         public long getEndPosition() {
/* 590 */           return -1L;
/*     */         }
/*     */
/*     */         public long getLineNumber() {
/* 594 */           return -1L;
/*     */         }
/*     */
/*     */         public long getColumnNumber() {
/* 598 */           return -1L;
/*     */         }
/*     */
/*     */         public String getCode() {
/* 602 */           return key;
/*     */         }
/*     */
/*     */         public String getMessage(Locale param1Locale) {
/* 606 */           return JavahTask.this.getMessage(param1Locale, key, args);
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */
/*     */   private String getMessage(String paramString, Object... paramVarArgs) {
/* 613 */     return getMessage(this.task_locale, paramString, paramVarArgs);
/*     */   }
/*     */
/*     */   private String getMessage(Locale paramLocale, String paramString, Object... paramVarArgs) {
/* 617 */     if (this.bundles == null)
/*     */     {
/*     */
/*     */
/* 621 */       this.bundles = new HashMap<>();
/*     */     }
/*     */
/* 624 */     if (paramLocale == null) {
/* 625 */       paramLocale = Locale.getDefault();
/*     */     }
/* 627 */     ResourceBundle resourceBundle = this.bundles.get(paramLocale);
/* 628 */     if (resourceBundle == null) {
/*     */       try {
/* 630 */         resourceBundle = ResourceBundle.getBundle("com.sun.tools.javah.resources.l10n", paramLocale);
/* 631 */         this.bundles.put(paramLocale, resourceBundle);
/* 632 */       } catch (MissingResourceException missingResourceException) {
/* 633 */         throw new InternalError("Cannot find javah resource bundle for locale " + paramLocale, missingResourceException);
/*     */       }
/*     */     }
/*     */
/*     */     try {
/* 638 */       return MessageFormat.format(resourceBundle.getString(paramString), paramVarArgs);
/* 639 */     } catch (MissingResourceException missingResourceException) {
/* 640 */       return paramString;
/*     */     }
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
/* 660 */   Set<String> javac_extras = new LinkedHashSet<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   PrintWriter log;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   JavaFileManager fileManager;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   DiagnosticListener<? super JavaFileObject> diagnosticListener;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Locale task_locale;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Map<Locale, ResourceBundle> bundles;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static final String progname = "javah";
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   JavahTask() {}
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @SupportedAnnotationTypes({"*"})
/*     */   class JavahProcessor
/*     */     extends AbstractProcessor
/*     */   {
/*     */     private Messager messager;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private TypeVisitor<Void, Types> checkMethodParametersVisitor;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private Gen g;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private Util.Exit exit;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     JavahProcessor(Gen param1Gen) {
/* 740 */       this.checkMethodParametersVisitor = new SimpleTypeVisitor8<Void, Types>()
/*     */         {
/*     */           public Void visitArray(ArrayType param2ArrayType, Types param2Types)
/*     */           {
/* 744 */             visit(param2ArrayType.getComponentType(), param2Types);
/* 745 */             return null;
/*     */           }
/*     */
/*     */           public Void visitDeclared(DeclaredType param2DeclaredType, Types param2Types) {
/* 749 */             param2DeclaredType.asElement().getKind();
/* 750 */             for (TypeMirror typeMirror : param2Types.directSupertypes(param2DeclaredType))
/* 751 */               visit(typeMirror, param2Types);
/* 752 */             return null;
/*     */           }
/*     */         };
/*     */       this.g = param1Gen;
/*     */     }
/*     */
/*     */     public SourceVersion getSupportedSourceVersion() {
/*     */       return SourceVersion.latest();
/*     */     }
/*     */
/*     */     public void init(ProcessingEnvironment param1ProcessingEnvironment) {
/*     */       super.init(param1ProcessingEnvironment);
/*     */       this.messager = this.processingEnv.getMessager();
/*     */     }
/*     */
/*     */     public boolean process(Set<? extends TypeElement> param1Set, RoundEnvironment param1RoundEnvironment) {
/*     */       try {
/*     */         Set<TypeElement> set = getAllClasses(ElementFilter.typesIn(param1RoundEnvironment.getRootElements()));
/*     */         if (set.size() > 0) {
/*     */           checkMethodParameters(set);
/*     */           this.g.setProcessingEnvironment(this.processingEnv);
/*     */           this.g.setClasses(set);
/*     */           this.g.run();
/*     */         }
/*     */       } catch (Symbol.CompletionFailure completionFailure) {
/*     */         this.messager.printMessage(Diagnostic.Kind.ERROR, JavahTask.this.getMessage("class.not.found", new Object[] { completionFailure.sym.getQualifiedName().toString() }));
/*     */       } catch (ClassNotFoundException classNotFoundException) {
/*     */         this.messager.printMessage(Diagnostic.Kind.ERROR, JavahTask.this.getMessage("class.not.found", new Object[] { classNotFoundException.getMessage() }));
/*     */       } catch (IOException iOException) {
/*     */         this.messager.printMessage(Diagnostic.Kind.ERROR, JavahTask.this.getMessage("io.exception", new Object[] { iOException.getMessage() }));
/*     */       } catch (Exit exit) {
/*     */         this.exit = exit;
/*     */       }
/*     */       return true;
/*     */     }
/*     */
/*     */     private Set<TypeElement> getAllClasses(Set<? extends TypeElement> param1Set) {
/*     */       LinkedHashSet<TypeElement> linkedHashSet = new LinkedHashSet();
/*     */       getAllClasses0(param1Set, linkedHashSet);
/*     */       return linkedHashSet;
/*     */     }
/*     */
/*     */     private void getAllClasses0(Iterable<? extends TypeElement> param1Iterable, Set<TypeElement> param1Set) {
/*     */       for (TypeElement typeElement : param1Iterable) {
/*     */         param1Set.add(typeElement);
/*     */         getAllClasses0(ElementFilter.typesIn(typeElement.getEnclosedElements()), param1Set);
/*     */       }
/*     */     }
/*     */
/*     */     private void checkMethodParameters(Set<TypeElement> param1Set) {
/*     */       Types types = this.processingEnv.getTypeUtils();
/*     */       for (TypeElement typeElement : param1Set) {
/*     */         for (ExecutableElement executableElement : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
/*     */           for (VariableElement variableElement : executableElement.getParameters()) {
/*     */             TypeMirror typeMirror = variableElement.asType();
/*     */             this.checkMethodParametersVisitor.visit(typeMirror, types);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\JavahTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
