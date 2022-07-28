/*      */ package com.sun.tools.jdeps;
/*      */
/*      */ import com.sun.tools.classfile.AccessFlags;
/*      */ import com.sun.tools.classfile.ClassFile;
/*      */ import com.sun.tools.classfile.ConstantPoolException;
/*      */ import com.sun.tools.classfile.Dependencies;
/*      */ import com.sun.tools.classfile.Dependency;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.nio.file.DirectoryStream;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.regex.Pattern;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ class JdepsTask
/*      */ {
/*      */   static class BadArgs
/*      */     extends Exception
/*      */   {
/*      */     static final long serialVersionUID = 8765093759964640721L;
/*      */     final String key;
/*      */     final Object[] args;
/*      */     boolean showUsage;
/*      */
/*      */     BadArgs(String param1String, Object... param1VarArgs) {
/*   52 */       super(JdepsTask.getMessage(param1String, param1VarArgs));
/*   53 */       this.key = param1String;
/*   54 */       this.args = param1VarArgs;
/*      */     }
/*      */
/*      */     BadArgs showUsage(boolean param1Boolean) {
/*   58 */       this.showUsage = param1Boolean;
/*   59 */       return this;
/*      */     }
/*      */   }
/*      */
/*      */   static abstract class Option {
/*      */     final boolean hasArg;
/*      */     final String[] aliases;
/*      */
/*      */     Option(boolean param1Boolean, String... param1VarArgs) {
/*   68 */       this.hasArg = param1Boolean;
/*   69 */       this.aliases = param1VarArgs;
/*      */     }
/*      */
/*      */     boolean isHidden() {
/*   73 */       return false;
/*      */     }
/*      */
/*      */     boolean matches(String param1String) {
/*   77 */       for (String str : this.aliases) {
/*   78 */         if (str.equals(param1String))
/*   79 */           return true;
/*   80 */         if (this.hasArg && param1String.startsWith(str + "="))
/*   81 */           return true;
/*      */       }
/*   83 */       return false;
/*      */     }
/*      */
/*      */     boolean ignoreRest() {
/*   87 */       return false;
/*      */     }
/*      */
/*      */     abstract void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) throws BadArgs;
/*      */   }
/*      */
/*      */   static abstract class HiddenOption
/*      */     extends Option
/*      */   {
/*      */     HiddenOption(boolean param1Boolean, String... param1VarArgs) {
/*   97 */       super(param1Boolean, param1VarArgs);
/*      */     }
/*      */
/*      */     boolean isHidden() {
/*  101 */       return true;
/*      */     }
/*      */   }
/*      */
/*  105 */   static Option[] recognizedOptions = new Option[] { new Option(false, new String[] { "-h", "-?", "-help" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  108 */           param1JdepsTask.options.help = true;
/*      */         }
/*      */       }, new Option(true, new String[] { "-dotoutput" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) throws BadArgs {
/*  113 */           Path path = Paths.get(param1String2, new String[0]);
/*  114 */           if (Files.exists(path, new java.nio.file.LinkOption[0]) && (!Files.isDirectory(path, new java.nio.file.LinkOption[0]) || !Files.isWritable(path))) {
/*  115 */             throw new BadArgs("err.invalid.path", new Object[] { param1String2 });
/*      */           }
/*  117 */           param1JdepsTask.options.dotOutputDir = param1String2;
/*      */         }
/*      */       }, new Option(false, new String[] { "-s", "-summary" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  122 */           param1JdepsTask.options.showSummary = true;
/*  123 */           param1JdepsTask.options.verbose = Analyzer.Type.SUMMARY;
/*      */         }
/*      */       }, new Option(false, new String[] { "-v", "-verbose", "-verbose:package", "-verbose:class" })
/*      */       {
/*      */
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) throws BadArgs
/*      */         {
/*  130 */           switch (param1String1) {
/*      */             case "-v":
/*      */             case "-verbose":
/*  133 */               param1JdepsTask.options.verbose = Analyzer.Type.VERBOSE;
/*  134 */               param1JdepsTask.options.filterSameArchive = false;
/*  135 */               param1JdepsTask.options.filterSamePackage = false;
/*      */               return;
/*      */             case "-verbose:package":
/*  138 */               param1JdepsTask.options.verbose = Analyzer.Type.PACKAGE;
/*      */               return;
/*      */             case "-verbose:class":
/*  141 */               param1JdepsTask.options.verbose = Analyzer.Type.CLASS;
/*      */               return;
/*      */           }
/*  144 */           throw new BadArgs("err.invalid.arg.for.option", new Object[] { param1String1 });
/*      */         }
/*      */       }, new Option(true, new String[] { "-cp", "-classpath" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2)
/*      */         {
/*  150 */           param1JdepsTask.options.classpath = param1String2;
/*      */         }
/*      */       }, new Option(true, new String[] { "-p", "-package" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  155 */           param1JdepsTask.options.packageNames.add(param1String2);
/*      */         }
/*      */       }, new Option(true, new String[] { "-e", "-regex" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  160 */           param1JdepsTask.options.regex = param1String2;
/*      */         }
/*      */       }, new Option(true, new String[] { "-f", "-filter" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2)
/*      */         {
/*  166 */           param1JdepsTask.options.filterRegex = param1String2;
/*      */         }
/*      */       }, new Option(false, new String[] { "-filter:package", "-filter:archive", "-filter:none" })
/*      */       {
/*      */
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2)
/*      */         {
/*  173 */           switch (param1String1) {
/*      */             case "-filter:package":
/*  175 */               param1JdepsTask.options.filterSamePackage = true;
/*  176 */               param1JdepsTask.options.filterSameArchive = false;
/*      */               break;
/*      */             case "-filter:archive":
/*  179 */               param1JdepsTask.options.filterSameArchive = true;
/*  180 */               param1JdepsTask.options.filterSamePackage = false;
/*      */               break;
/*      */             case "-filter:none":
/*  183 */               param1JdepsTask.options.filterSameArchive = false;
/*  184 */               param1JdepsTask.options.filterSamePackage = false;
/*      */               break;
/*      */           }
/*      */         }
/*      */       }, new Option(true, new String[] { "-include" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) throws BadArgs {
/*  191 */           param1JdepsTask.options.includePattern = Pattern.compile(param1String2);
/*      */         }
/*      */       },
/*      */       new Option(false, new String[] { "-P", "-profile" }) {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) throws BadArgs {
/*  196 */           param1JdepsTask.options.showProfile = true;
/*  197 */           if (Profile.getProfileCount() == 0) {
/*  198 */             throw new BadArgs("err.option.unsupported", new Object[] { param1String1, JdepsTask.getMessage("err.profiles.msg", new Object[0]) });
/*      */           }
/*      */         }
/*      */       }, new Option(false, new String[] { "-apionly" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  204 */           param1JdepsTask.options.apiOnly = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-R", "-recursive" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  209 */           param1JdepsTask.options.depth = 0;
/*      */
/*  211 */           param1JdepsTask.options.filterSameArchive = false;
/*  212 */           param1JdepsTask.options.filterSamePackage = false;
/*      */         }
/*      */       }, new Option(false, new String[] { "-jdkinternals" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  217 */           param1JdepsTask.options.findJDKInternals = true;
/*  218 */           param1JdepsTask.options.verbose = Analyzer.Type.CLASS;
/*  219 */           if (param1JdepsTask.options.includePattern == null) {
/*  220 */             param1JdepsTask.options.includePattern = Pattern.compile(".*");
/*      */           }
/*      */         }
/*      */       }, new Option(false, new String[] { "-version" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  226 */           param1JdepsTask.options.version = true;
/*      */         }
/*      */       }, new HiddenOption(false, new String[] { "-fullversion" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  231 */           param1JdepsTask.options.fullVersion = true;
/*      */         }
/*      */       }, new HiddenOption(false, new String[] { "-showlabel" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  236 */           param1JdepsTask.options.showLabel = true;
/*      */         }
/*      */       }, new HiddenOption(false, new String[] { "-q", "-quiet" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) {
/*  241 */           param1JdepsTask.options.nowarning = true;
/*      */         }
/*      */       }, new HiddenOption(true, new String[] { "-depth" })
/*      */       {
/*      */         void process(JdepsTask param1JdepsTask, String param1String1, String param1String2) throws BadArgs {
/*      */           try {
/*  247 */             param1JdepsTask.options.depth = Integer.parseInt(param1String2);
/*  248 */           } catch (NumberFormatException numberFormatException) {
/*  249 */             throw new BadArgs("err.invalid.arg.for.option", new Object[] { param1String1 });
/*      */           }
/*      */         }
/*      */       } };
/*      */
/*      */
/*      */   private static final String PROGNAME = "jdeps";
/*  256 */   private final Options options = new Options();
/*  257 */   private final List<String> classes = new ArrayList<>();
/*      */   private PrintWriter log;
/*      */
/*      */   void setLog(PrintWriter paramPrintWriter) {
/*  261 */     this.log = paramPrintWriter;
/*      */   }
/*      */
/*      */
/*      */   static final int EXIT_OK = 0;
/*      */
/*      */   static final int EXIT_ERROR = 1;
/*      */
/*      */   static final int EXIT_CMDERR = 2;
/*      */   static final int EXIT_SYSERR = 3;
/*      */   static final int EXIT_ABNORMAL = 4;
/*      */
/*      */   int run(String[] paramArrayOfString) {
/*  274 */     if (this.log == null) {
/*  275 */       this.log = new PrintWriter(System.out);
/*      */     }
/*      */     try {
/*  278 */       handleOptions(paramArrayOfString);
/*  279 */       if (this.options.help) {
/*  280 */         showHelp();
/*      */       }
/*  282 */       if (this.options.version || this.options.fullVersion) {
/*  283 */         showVersion(this.options.fullVersion);
/*      */       }
/*  285 */       if (this.classes.isEmpty() && this.options.includePattern == null) {
/*  286 */         if (this.options.help || this.options.version || this.options.fullVersion) {
/*  287 */           return 0;
/*      */         }
/*  289 */         showHelp();
/*  290 */         return 2;
/*      */       }
/*      */
/*  293 */       if (this.options.regex != null && this.options.packageNames.size() > 0) {
/*  294 */         showHelp();
/*  295 */         return 2;
/*      */       }
/*  297 */       if (this.options.findJDKInternals && (this.options.regex != null || this.options.packageNames
/*  298 */         .size() > 0 || this.options.showSummary)) {
/*  299 */         showHelp();
/*  300 */         return 2;
/*      */       }
/*  302 */       if (this.options.showSummary && this.options.verbose != Analyzer.Type.SUMMARY) {
/*  303 */         showHelp();
/*  304 */         return 2;
/*      */       }
/*  306 */       boolean bool = run();
/*  307 */       return bool ? 0 : 1;
/*  308 */     } catch (BadArgs badArgs) {
/*  309 */       reportError(badArgs.key, badArgs.args);
/*  310 */       if (badArgs.showUsage) {
/*  311 */         this.log.println(getMessage("main.usage.summary", new Object[] { "jdeps" }));
/*      */       }
/*  313 */       return 2;
/*  314 */     } catch (IOException iOException) {
/*  315 */       return 4;
/*      */     } finally {
/*  317 */       this.log.flush();
/*      */     }
/*      */   }
/*      */
/*  321 */   private final List<Archive> sourceLocations = new ArrayList<>();
/*      */
/*      */   private boolean run() throws IOException {
/*  324 */     findDependencies();
/*      */
/*  326 */     Analyzer analyzer = new Analyzer(this.options.verbose, new Analyzer.Filter()
/*      */         {
/*      */
/*      */           public boolean accepts(Dependency.Location param1Location1, Archive param1Archive1, Dependency.Location param1Location2, Archive param1Archive2)
/*      */           {
/*  331 */             if (JdepsTask.this.options.findJDKInternals)
/*      */             {
/*  333 */               return (JdepsTask.this.isJDKArchive(param1Archive2) &&
/*  334 */                 !((PlatformClassPath.JDKArchive)param1Archive2).isExported(param1Location2.getClassName())); }
/*  335 */             if (JdepsTask.this.options.filterSameArchive)
/*      */             {
/*  337 */               return (param1Archive1 != param1Archive2);
/*      */             }
/*  339 */             return true;
/*      */           }
/*      */         });
/*      */
/*      */
/*  344 */     analyzer.run(this.sourceLocations);
/*      */
/*      */
/*  347 */     if (this.options.dotOutputDir != null) {
/*  348 */       Path path = Paths.get(this.options.dotOutputDir, new String[0]);
/*  349 */       Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
/*  350 */       generateDotFiles(path, analyzer);
/*      */     } else {
/*  352 */       printRawOutput(this.log, analyzer);
/*      */     }
/*      */
/*  355 */     if (this.options.findJDKInternals && !this.options.nowarning) {
/*  356 */       showReplacements(analyzer);
/*      */     }
/*  358 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private void generateSummaryDotFile(Path paramPath, Analyzer paramAnalyzer) throws IOException {
/*  364 */     Analyzer.Type type = (this.options.verbose == Analyzer.Type.PACKAGE || this.options.verbose == Analyzer.Type.SUMMARY) ? Analyzer.Type.SUMMARY : Analyzer.Type.PACKAGE;
/*      */
/*  366 */     Path path = paramPath.resolve("summary.dot");
/*  367 */     try(PrintWriter null = new PrintWriter(Files.newOutputStream(path, new java.nio.file.OpenOption[0]));
/*  368 */         SummaryDotFile null = new SummaryDotFile(printWriter, type)) {
/*  369 */       for (Archive archive : this.sourceLocations) {
/*  370 */         if (!archive.isEmpty()) {
/*  371 */           if ((this.options.verbose == Analyzer.Type.PACKAGE || this.options.verbose == Analyzer.Type.SUMMARY) &&
/*  372 */             this.options.showLabel)
/*      */           {
/*  374 */             paramAnalyzer.visitDependences(archive, summaryDotFile.labelBuilder(), Analyzer.Type.PACKAGE);
/*      */           }
/*      */
/*  377 */           paramAnalyzer.visitDependences(archive, summaryDotFile, type);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void generateDotFiles(Path paramPath, Analyzer paramAnalyzer) throws IOException {
/*  385 */     if (this.options.verbose != Analyzer.Type.SUMMARY) {
/*  386 */       for (Archive archive : this.sourceLocations) {
/*  387 */         if (paramAnalyzer.hasDependences(archive)) {
/*  388 */           Path path = paramPath.resolve(archive.getName() + ".dot");
/*  389 */           try(PrintWriter null = new PrintWriter(Files.newOutputStream(path, new java.nio.file.OpenOption[0]));
/*  390 */               DotFileFormatter null = new DotFileFormatter(printWriter, archive)) {
/*  391 */             paramAnalyzer.visitDependences(archive, dotFileFormatter);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  397 */     generateSummaryDotFile(paramPath, paramAnalyzer);
/*      */   }
/*      */
/*      */   private void printRawOutput(PrintWriter paramPrintWriter, Analyzer paramAnalyzer) {
/*  401 */     RawOutputFormatter rawOutputFormatter = new RawOutputFormatter(paramPrintWriter);
/*  402 */     RawSummaryFormatter rawSummaryFormatter = new RawSummaryFormatter(paramPrintWriter);
/*  403 */     for (Archive archive : this.sourceLocations) {
/*  404 */       if (!archive.isEmpty()) {
/*  405 */         paramAnalyzer.visitDependences(archive, rawSummaryFormatter, Analyzer.Type.SUMMARY);
/*  406 */         if (paramAnalyzer.hasDependences(archive) && this.options.verbose != Analyzer.Type.SUMMARY) {
/*  407 */           paramAnalyzer.visitDependences(archive, rawOutputFormatter);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   private boolean isValidClassName(String paramString) {
/*  414 */     if (!Character.isJavaIdentifierStart(paramString.charAt(0))) {
/*  415 */       return false;
/*      */     }
/*  417 */     for (byte b = 1; b < paramString.length(); b++) {
/*  418 */       char c = paramString.charAt(b);
/*  419 */       if (c != '.' && !Character.isJavaIdentifierPart(c)) {
/*  420 */         return false;
/*      */       }
/*      */     }
/*  423 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   class DependencyFilter
/*      */     implements Dependency.Filter
/*      */   {
/*      */     final Dependency.Filter filter;
/*      */
/*      */
/*      */
/*      */     final Pattern filterPattern;
/*      */
/*      */
/*      */
/*      */
/*      */     DependencyFilter() {
/*  442 */       if (JdepsTask.this.options.regex != null) {
/*  443 */         this.filter = Dependencies.getRegexFilter(Pattern.compile(JdepsTask.this.options.regex));
/*  444 */       } else if (JdepsTask.this.options.packageNames.size() > 0) {
/*  445 */         this.filter = Dependencies.getPackageFilter(JdepsTask.this.options.packageNames, false);
/*      */       } else {
/*  447 */         this.filter = null;
/*      */       }
/*      */
/*  450 */       this
/*  451 */         .filterPattern = (JdepsTask.this.options.filterRegex != null) ? Pattern.compile(JdepsTask.this.options.filterRegex) : null;
/*      */     }
/*      */
/*      */     public boolean accepts(Dependency param1Dependency) {
/*  455 */       if (param1Dependency.getOrigin().equals(param1Dependency.getTarget())) {
/*  456 */         return false;
/*      */       }
/*  458 */       String str = param1Dependency.getTarget().getPackageName();
/*  459 */       if (JdepsTask.this.options.filterSamePackage && param1Dependency.getOrigin().getPackageName().equals(str)) {
/*  460 */         return false;
/*      */       }
/*      */
/*  463 */       if (this.filterPattern != null && this.filterPattern.matcher(str).matches()) {
/*  464 */         return false;
/*      */       }
/*  466 */       return (this.filter != null) ? this.filter.accepts(param1Dependency) : true;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean matches(String paramString, AccessFlags paramAccessFlags) {
/*  475 */     if (this.options.apiOnly && !paramAccessFlags.is(1))
/*  476 */       return false;
/*  477 */     if (this.options.includePattern != null) {
/*  478 */       return this.options.includePattern.matcher(paramString.replace('/', '.')).matches();
/*      */     }
/*  480 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private void findDependencies() throws IOException {
/*  487 */     Dependency.Finder finder = this.options.apiOnly ? Dependencies.getAPIFinder(4) : Dependencies.getClassDependencyFinder();
/*  488 */     DependencyFilter dependencyFilter = new DependencyFilter();
/*      */
/*  490 */     ArrayList<Archive> arrayList1 = new ArrayList();
/*  491 */     LinkedList<String> linkedList1 = new LinkedList();
/*  492 */     ArrayList<Path> arrayList = new ArrayList();
/*  493 */     for (String str : this.classes) {
/*  494 */       Path path = Paths.get(str, new String[0]);
/*  495 */       if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  496 */         arrayList.add(path);
/*  497 */         arrayList1.add(Archive.getInstance(path)); continue;
/*      */       }
/*  499 */       if (isValidClassName(str)) {
/*  500 */         linkedList1.add(str); continue;
/*      */       }
/*  502 */       warning("warn.invalid.arg", new Object[] { str });
/*      */     }
/*      */
/*      */
/*  506 */     this.sourceLocations.addAll(arrayList1);
/*      */
/*  508 */     ArrayList<Archive> arrayList2 = new ArrayList();
/*  509 */     arrayList2.addAll(getClassPathArchives(this.options.classpath, arrayList));
/*  510 */     if (this.options.includePattern != null) {
/*  511 */       arrayList1.addAll(arrayList2);
/*      */     }
/*  513 */     arrayList2.addAll(PlatformClassPath.getArchives());
/*      */
/*      */
/*  516 */     this.sourceLocations.addAll(arrayList2);
/*      */
/*      */
/*  519 */     for (Archive archive : this.sourceLocations) {
/*  520 */       if (archive.reader().isMultiReleaseJar()) {
/*  521 */         warning("warn.mrjar.usejdk9", new Object[] { archive.getPathName() });
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*  528 */     LinkedList<String> linkedList2 = new LinkedList();
/*  529 */     HashSet<String> hashSet = new HashSet();
/*      */
/*      */
/*  532 */     for (Archive archive : arrayList1) {
/*  533 */       for (ClassFile classFile : archive.reader().getClassFiles()) {
/*      */         String str;
/*      */         try {
/*  536 */           str = classFile.getName();
/*  537 */         } catch (ConstantPoolException constantPoolException) {
/*  538 */           throw new Dependencies.ClassFileError(constantPoolException);
/*      */         }
/*      */
/*      */
/*  542 */         if (!matches(str, classFile.access_flags)) {
/*      */           continue;
/*      */         }
/*      */
/*  546 */         if (!hashSet.contains(str)) {
/*  547 */           hashSet.add(str);
/*      */         }
/*      */
/*  550 */         for (Dependency dependency : finder.findDependencies(classFile)) {
/*  551 */           if (dependencyFilter.accepts(dependency)) {
/*  552 */             String str1 = dependency.getTarget().getName();
/*  553 */             if (!hashSet.contains(str1) && !linkedList2.contains(str1)) {
/*  554 */               linkedList2.add(str1);
/*      */             }
/*  556 */             archive.addClass(dependency.getOrigin(), dependency.getTarget());
/*      */             continue;
/*      */           }
/*  559 */           archive.addClass(dependency.getOrigin());
/*      */         }
/*      */
/*  562 */         for (String str1 : archive.reader().skippedEntries()) {
/*  563 */           warning("warn.skipped.entry", new Object[] { str1, archive.getPathName() });
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*  570 */     LinkedList<String> linkedList3 = linkedList1;
/*  571 */     int i = (this.options.depth > 0) ? this.options.depth : Integer.MAX_VALUE;
/*      */     do {
/*      */       String str;
/*  574 */       while ((str = linkedList3.poll()) != null) {
/*  575 */         if (hashSet.contains(str)) {
/*      */           continue;
/*      */         }
/*  578 */         ClassFile classFile = null;
/*  579 */         for (Archive archive : arrayList2) {
/*  580 */           classFile = archive.reader().getClassFile(str);
/*  581 */           if (classFile != null) {
/*      */             String str1;
/*      */             try {
/*  584 */               str1 = classFile.getName();
/*  585 */             } catch (ConstantPoolException constantPoolException) {
/*  586 */               throw new Dependencies.ClassFileError(constantPoolException);
/*      */             }
/*  588 */             if (!hashSet.contains(str1)) {
/*      */
/*      */
/*  591 */               hashSet.add(str1);
/*      */
/*  593 */               if (isJDKArchive(archive)) {
/*  594 */                 ((PlatformClassPath.JDKArchive)archive).processJdkExported(classFile);
/*      */               }
/*  596 */               for (Dependency dependency : finder.findDependencies(classFile)) {
/*  597 */                 if (i == 0) {
/*      */
/*  599 */                   archive.addClass(dependency.getOrigin()); break;
/*      */                 }
/*  601 */                 if (dependencyFilter.accepts(dependency)) {
/*  602 */                   archive.addClass(dependency.getOrigin(), dependency.getTarget());
/*  603 */                   String str2 = dependency.getTarget().getName();
/*  604 */                   if (!hashSet.contains(str2) && !linkedList2.contains(str2)) {
/*  605 */                     linkedList2.add(str2);
/*      */                   }
/*      */                   continue;
/*      */                 }
/*  609 */                 archive.addClass(dependency.getOrigin());
/*      */               }
/*      */             }
/*      */
/*      */             break;
/*      */           }
/*      */         }
/*  616 */         if (classFile == null) {
/*  617 */           hashSet.add(str);
/*      */         }
/*      */       }
/*  620 */       linkedList3 = linkedList2;
/*  621 */       linkedList2 = new LinkedList<>();
/*  622 */     } while (!linkedList3.isEmpty() && i-- > 0);
/*      */   }
/*      */
/*      */
/*      */   public void handleOptions(String[] paramArrayOfString) throws BadArgs {
/*  627 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  628 */       if (paramArrayOfString[i].charAt(0) == '-') {
/*  629 */         String str1 = paramArrayOfString[i];
/*  630 */         Option option = getOption(str1);
/*  631 */         String str2 = null;
/*  632 */         if (option.hasArg) {
/*  633 */           if (str1.startsWith("-") && str1.indexOf('=') > 0) {
/*  634 */             str2 = str1.substring(str1.indexOf('=') + 1, str1.length());
/*  635 */           } else if (i + 1 < paramArrayOfString.length) {
/*  636 */             str2 = paramArrayOfString[++i];
/*      */           }
/*  638 */           if (str2 == null || str2.isEmpty() || str2.charAt(0) == '-') {
/*  639 */             throw (new BadArgs("err.missing.arg", new Object[] { str1 })).showUsage(true);
/*      */           }
/*      */         }
/*  642 */         option.process(this, str1, str2);
/*  643 */         if (option.ignoreRest()) {
/*  644 */           i = paramArrayOfString.length;
/*      */         }
/*      */       } else {
/*      */
/*  648 */         for (; i < paramArrayOfString.length; i++) {
/*  649 */           String str = paramArrayOfString[i];
/*  650 */           if (str.charAt(0) == '-') {
/*  651 */             throw (new BadArgs("err.option.after.class", new Object[] { str })).showUsage(true);
/*      */           }
/*  653 */           this.classes.add(str);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   private Option getOption(String paramString) throws BadArgs {
/*  660 */     for (Option option : recognizedOptions) {
/*  661 */       if (option.matches(paramString)) {
/*  662 */         return option;
/*      */       }
/*      */     }
/*  665 */     throw (new BadArgs("err.unknown.option", new Object[] { paramString })).showUsage(true);
/*      */   }
/*      */
/*      */   private void reportError(String paramString, Object... paramVarArgs) {
/*  669 */     this.log.println(getMessage("error.prefix", new Object[0]) + " " + getMessage(paramString, paramVarArgs));
/*      */   }
/*      */
/*      */   private void warning(String paramString, Object... paramVarArgs) {
/*  673 */     this.log.println(getMessage("warn.prefix", new Object[0]) + " " + getMessage(paramString, paramVarArgs));
/*      */   }
/*      */
/*      */   private void showHelp() {
/*  677 */     this.log.println(getMessage("main.usage", new Object[] { "jdeps" }));
/*  678 */     for (Option option : recognizedOptions) {
/*  679 */       String str = option.aliases[0].substring(1);
/*  680 */       str = (str.charAt(0) == '-') ? str.substring(1) : str;
/*  681 */       if (!option.isHidden() && !str.equals("h") && !str.startsWith("filter:"))
/*      */       {
/*      */
/*  684 */         this.log.println(getMessage("main.opt." + str, new Object[0])); }
/*      */     }
/*      */   }
/*      */
/*      */   private void showVersion(boolean paramBoolean) {
/*  689 */     this.log.println(version(paramBoolean ? "full" : "release"));
/*      */   }
/*      */
/*      */
/*      */
/*      */   private String version(String paramString) {
/*  695 */     if (ResourceBundleHelper.versionRB == null) {
/*  696 */       return System.getProperty("java.version");
/*      */     }
/*      */     try {
/*  699 */       return ResourceBundleHelper.versionRB.getString(paramString);
/*  700 */     } catch (MissingResourceException missingResourceException) {
/*  701 */       return getMessage("version.unknown", new Object[] { System.getProperty("java.version") });
/*      */     }
/*      */   }
/*      */
/*      */   static String getMessage(String paramString, Object... paramVarArgs) {
/*      */     try {
/*  707 */       return MessageFormat.format(ResourceBundleHelper.bundle.getString(paramString), paramVarArgs);
/*  708 */     } catch (MissingResourceException missingResourceException) {
/*  709 */       throw new InternalError("Missing message: " + paramString);
/*      */     }
/*      */   }
/*      */
/*      */   private static class Options { boolean help;
/*      */     boolean version;
/*      */     boolean fullVersion;
/*      */     boolean showProfile;
/*      */     boolean showSummary;
/*      */     boolean apiOnly;
/*      */     boolean showLabel;
/*      */     boolean findJDKInternals;
/*      */     boolean nowarning;
/*      */
/*      */     private Options() {}
/*      */
/*  725 */     Analyzer.Type verbose = Analyzer.Type.PACKAGE;
/*      */     boolean filterSamePackage = true;
/*      */     boolean filterSameArchive = false;
/*      */     String filterRegex;
/*      */     String dotOutputDir;
/*  730 */     String classpath = "";
/*  731 */     int depth = 1;
/*  732 */     Set<String> packageNames = new HashSet<>();
/*      */     String regex;
/*      */     Pattern includePattern; }
/*      */
/*      */   private static class ResourceBundleHelper {
/*      */     static final ResourceBundle versionRB;
/*      */     static final ResourceBundle bundle;
/*      */     static final ResourceBundle jdkinternals;
/*      */
/*      */     static {
/*  742 */       Locale locale = Locale.getDefault();
/*      */       try {
/*  744 */         bundle = ResourceBundle.getBundle("com.sun.tools.jdeps.resources.jdeps", locale);
/*  745 */       } catch (MissingResourceException missingResourceException) {
/*  746 */         throw new InternalError("Cannot find jdeps resource bundle for locale " + locale);
/*      */       }
/*      */       try {
/*  749 */         versionRB = ResourceBundle.getBundle("com.sun.tools.jdeps.resources.version");
/*  750 */       } catch (MissingResourceException missingResourceException) {
/*  751 */         throw new InternalError("version.resource.missing");
/*      */       }
/*      */       try {
/*  754 */         jdkinternals = ResourceBundle.getBundle("com.sun.tools.jdeps.resources.jdkinternals");
/*  755 */       } catch (MissingResourceException missingResourceException) {
/*  756 */         throw new InternalError("Cannot find jdkinternals resource bundle");
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
/*      */   private List<Archive> getClassPathArchives(String paramString, List<Path> paramList) throws IOException {
/*  768 */     ArrayList<Archive> arrayList = new ArrayList();
/*  769 */     if (paramString.isEmpty()) {
/*  770 */       return arrayList;
/*      */     }
/*      */
/*  773 */     ArrayList<Path> arrayList1 = new ArrayList();
/*  774 */     for (String str : paramString.split(File.pathSeparator)) {
/*  775 */       if (str.length() > 0) {
/*      */
/*  777 */         int i = str.lastIndexOf(".*");
/*  778 */         if (i > 0) {
/*  779 */           Path path = Paths.get(str.substring(0, i), new String[0]);
/*  780 */           try (DirectoryStream<Path> null = Files.newDirectoryStream(path, "*.jar")) {
/*  781 */             for (Path path1 : directoryStream) {
/*  782 */               arrayList1.add(path1);
/*      */             }
/*      */           }
/*      */         } else {
/*  786 */           arrayList1.add(Paths.get(str, new String[0]));
/*      */         }
/*      */       }
/*      */     }
/*  790 */     for (Path path : arrayList1) {
/*  791 */       if (Files.exists(path, new java.nio.file.LinkOption[0]) && !hasSameFile(paramList, path)) {
/*  792 */         arrayList.add(Archive.getInstance(path));
/*      */       }
/*      */     }
/*  795 */     return arrayList;
/*      */   }
/*      */
/*      */   private boolean hasSameFile(List<Path> paramList, Path paramPath) throws IOException {
/*  799 */     for (Path path : paramList) {
/*  800 */       if (Files.isSameFile(path, paramPath)) {
/*  801 */         return true;
/*      */       }
/*      */     }
/*  804 */     return false;
/*      */   }
/*      */
/*      */   class RawOutputFormatter implements Analyzer.Visitor {
/*      */     private final PrintWriter writer;
/*  809 */     private String pkg = "";
/*      */     RawOutputFormatter(PrintWriter param1PrintWriter) {
/*  811 */       this.writer = param1PrintWriter;
/*      */     }
/*      */
/*      */
/*      */     public void visitDependence(String param1String1, Archive param1Archive1, String param1String2, Archive param1Archive2) {
/*  816 */       String str = JdepsTask.this.toTag(param1String2, param1Archive2);
/*  817 */       if (JdepsTask.this.options.verbose == Analyzer.Type.VERBOSE) {
/*  818 */         this.writer.format("   %-50s -> %-50s %s%n", new Object[] { param1String1, param1String2, str });
/*      */       } else {
/*  820 */         if (!param1String1.equals(this.pkg)) {
/*  821 */           this.pkg = param1String1;
/*  822 */           this.writer.format("   %s (%s)%n", new Object[] { param1String1, param1Archive1.getName() });
/*      */         }
/*  824 */         this.writer.format("      -> %-50s %s%n", new Object[] { param1String2, str });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   class RawSummaryFormatter implements Analyzer.Visitor { private final PrintWriter writer;
/*      */
/*      */     RawSummaryFormatter(PrintWriter param1PrintWriter) {
/*  832 */       this.writer = param1PrintWriter;
/*      */     }
/*      */
/*      */
/*      */     public void visitDependence(String param1String1, Archive param1Archive1, String param1String2, Archive param1Archive2) {
/*  837 */       this.writer.format("%s -> %s", new Object[] { param1Archive1.getName(), param1Archive2.getPathName() });
/*  838 */       if (JdepsTask.this.options.showProfile && PlatformClassPath.JDKArchive.isProfileArchive(param1Archive2)) {
/*  839 */         this.writer.format(" (%s)", new Object[] { param1String2 });
/*      */       }
/*  841 */       this.writer.format("%n", new Object[0]);
/*      */     } }
/*      */
/*      */   class DotFileFormatter implements Analyzer.Visitor, AutoCloseable {
/*      */     private final PrintWriter writer;
/*      */     private final String name;
/*      */
/*      */     DotFileFormatter(PrintWriter param1PrintWriter, Archive param1Archive) {
/*  849 */       this.writer = param1PrintWriter;
/*  850 */       this.name = param1Archive.getName();
/*  851 */       param1PrintWriter.format("digraph \"%s\" {%n", new Object[] { this.name });
/*  852 */       param1PrintWriter.format("    // Path: %s%n", new Object[] { param1Archive.getPathName() });
/*      */     }
/*      */
/*      */
/*      */     public void close() {
/*  857 */       this.writer.println("}");
/*      */     }
/*      */
/*      */
/*      */
/*      */     public void visitDependence(String param1String1, Archive param1Archive1, String param1String2, Archive param1Archive2) {
/*  863 */       String str = JdepsTask.this.toTag(param1String2, param1Archive2);
/*  864 */       this.writer.format("   %-50s -> \"%s\";%n", new Object[] {
/*  865 */             String.format("\"%s\"", new Object[] { param1String1
/*  866 */               }), str.isEmpty() ? param1String2 :
/*  867 */             String.format("%s (%s)", new Object[] { param1String2, str }) });
/*      */     }
/*      */   }
/*      */
/*      */   class SummaryDotFile implements Analyzer.Visitor, AutoCloseable {
/*      */     private final PrintWriter writer;
/*      */     private final Analyzer.Type type;
/*  874 */     private final Map<Archive, Map<Archive, StringBuilder>> edges = new HashMap<>();
/*      */     SummaryDotFile(PrintWriter param1PrintWriter, Analyzer.Type param1Type) {
/*  876 */       this.writer = param1PrintWriter;
/*  877 */       this.type = param1Type;
/*  878 */       param1PrintWriter.format("digraph \"summary\" {%n", new Object[0]);
/*      */     }
/*      */
/*      */
/*      */     public void close() {
/*  883 */       this.writer.println("}");
/*      */     }
/*      */
/*      */
/*      */
/*      */     public void visitDependence(String param1String1, Archive param1Archive1, String param1String2, Archive param1Archive2) {
/*  889 */       String str1 = (this.type == Analyzer.Type.PACKAGE) ? param1String2 : param1Archive2.getName();
/*  890 */       if (this.type == Analyzer.Type.PACKAGE) {
/*  891 */         String str = JdepsTask.this.toTag(param1String2, param1Archive2, this.type);
/*  892 */         if (!str.isEmpty())
/*  893 */           str1 = str1 + " (" + str + ")";
/*  894 */       } else if (JdepsTask.this.options.showProfile && PlatformClassPath.JDKArchive.isProfileArchive(param1Archive2)) {
/*  895 */         str1 = str1 + " (" + param1String2 + ")";
/*      */       }
/*  897 */       String str2 = getLabel(param1Archive1, param1Archive2);
/*  898 */       this.writer.format("  %-50s -> \"%s\"%s;%n", new Object[] {
/*  899 */             String.format("\"%s\"", new Object[] { param1String1 }), str1, str2 });
/*      */     }
/*      */
/*      */     String getLabel(Archive param1Archive1, Archive param1Archive2) {
/*  903 */       if (this.edges.isEmpty()) {
/*  904 */         return "";
/*      */       }
/*  906 */       StringBuilder stringBuilder = (StringBuilder)((Map)this.edges.get(param1Archive1)).get(param1Archive2);
/*  907 */       return (stringBuilder == null) ? "" : String.format(" [label=\"%s\",fontsize=9]", new Object[] { stringBuilder.toString() });
/*      */     }
/*      */
/*      */
/*      */     Analyzer.Visitor labelBuilder() {
/*  912 */       return new Analyzer.Visitor()
/*      */         {
/*      */
/*      */           public void visitDependence(String param2String1, Archive param2Archive1, String param2String2, Archive param2Archive2)
/*      */           {
/*  917 */             Map<Object, Object> map = (Map) SummaryDotFile.this.edges.get(param2Archive1);
/*  918 */             if (!SummaryDotFile.this.edges.containsKey(param2Archive1)) {
/*  919 */               SummaryDotFile.this.edges.put(param2Archive1, map = new HashMap<>());
/*      */             }
/*  921 */             StringBuilder stringBuilder = (StringBuilder)map.get(param2Archive2);
/*  922 */             if (stringBuilder == null) {
/*  923 */               map.put(param2Archive2, stringBuilder = new StringBuilder());
/*      */             }
/*  925 */             String str = JdepsTask.this.toTag(param2String2, param2Archive2, Analyzer.Type.PACKAGE);
/*  926 */             addLabel(stringBuilder, param2String1, param2String2, str);
/*      */           }
/*      */
/*      */           void addLabel(StringBuilder param2StringBuilder, String param2String1, String param2String2, String param2String3) {
/*  930 */             param2StringBuilder.append(param2String1).append(" -> ").append(param2String2);
/*  931 */             if (!param2String3.isEmpty()) {
/*  932 */               param2StringBuilder.append(" (" + param2String3 + ")");
/*      */             }
/*  934 */             param2StringBuilder.append("\\n");
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean isJDKArchive(Archive paramArchive) {
/*  944 */     return PlatformClassPath.JDKArchive.class.isInstance(paramArchive);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private String toTag(String paramString, Archive paramArchive, Analyzer.Type paramType) {
/*  955 */     if (!isJDKArchive(paramArchive)) {
/*  956 */       return paramArchive.getName();
/*      */     }
/*      */
/*  959 */     PlatformClassPath.JDKArchive jDKArchive = (PlatformClassPath.JDKArchive)paramArchive;
/*  960 */     boolean bool = false;
/*  961 */     if (paramType == Analyzer.Type.CLASS || paramType == Analyzer.Type.VERBOSE) {
/*  962 */       bool = jDKArchive.isExported(paramString);
/*      */     } else {
/*  964 */       bool = jDKArchive.isExportedPackage(paramString);
/*      */     }
/*  966 */     Profile profile = getProfile(paramString, paramType);
/*  967 */     if (bool)
/*      */     {
/*  969 */       return (this.options.showProfile && profile != null) ? profile.profileName() : "";
/*      */     }
/*  971 */     return "JDK internal API (" + paramArchive.getName() + ")";
/*      */   }
/*      */
/*      */
/*      */   private String toTag(String paramString, Archive paramArchive) {
/*  976 */     return toTag(paramString, paramArchive, this.options.verbose);
/*      */   }
/*      */
/*      */   private Profile getProfile(String paramString, Analyzer.Type paramType) {
/*  980 */     String str = paramString;
/*  981 */     if (paramType == Analyzer.Type.CLASS || paramType == Analyzer.Type.VERBOSE) {
/*  982 */       int i = paramString.lastIndexOf('.');
/*  983 */       str = (i > 0) ? paramString.substring(0, i) : "";
/*      */     }
/*  985 */     return Profile.getProfile(str);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private String replacementFor(String paramString) {
/*  993 */     String str1 = paramString;
/*  994 */     String str2 = null;
/*  995 */     while (str2 == null && str1 != null) {
/*      */       try {
/*  997 */         str2 = ResourceBundleHelper.jdkinternals.getString(str1);
/*  998 */       } catch (MissingResourceException missingResourceException) {
/*      */
/* 1000 */         int i = str1.lastIndexOf('.');
/* 1001 */         str1 = (i > 0) ? str1.substring(0, i) : null;
/*      */       }
/*      */     }
/* 1004 */     return str2;
/*      */   }
/*      */
/*      */   private void showReplacements(Analyzer paramAnalyzer) {
/* 1008 */     TreeMap<Object, Object> treeMap = new TreeMap<>();
/* 1009 */     boolean bool = false;
/* 1010 */     for (Archive archive : this.sourceLocations) {
/* 1011 */       bool = (bool || paramAnalyzer.hasDependences(archive)) ? true : false;
/* 1012 */       for (String str1 : paramAnalyzer.dependences(archive)) {
/* 1013 */         String str2 = replacementFor(str1);
/* 1014 */         if (str2 != null && !treeMap.containsKey(str1)) {
/* 1015 */           treeMap.put(str1, str2);
/*      */         }
/*      */       }
/*      */     }
/* 1019 */     if (bool) {
/* 1020 */       this.log.println();
/* 1021 */       warning("warn.replace.useJDKInternals", new Object[] { getMessage("jdeps.wiki.url", new Object[0]) });
/*      */     }
/* 1023 */     if (!treeMap.isEmpty()) {
/* 1024 */       this.log.println();
/* 1025 */       this.log.format("%-40s %s%n", new Object[] { "JDK Internal API", "Suggested Replacement" });
/* 1026 */       this.log.format("%-40s %s%n", new Object[] { "----------------", "---------------------" });
/* 1027 */       for (Map.Entry<Object, Object> entry : treeMap.entrySet()) {
/* 1028 */         this.log.format("%-40s %s%n", new Object[] { entry.getKey(), entry.getValue() });
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\JdepsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
