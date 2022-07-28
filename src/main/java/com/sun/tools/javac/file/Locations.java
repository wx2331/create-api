/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.zip.ZipFile;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Locations
/*     */ {
/*     */   private Log log;
/*     */   private Options options;
/*     */   private Lint lint;
/*     */   private FSInfo fsInfo;
/*     */   private boolean warn;
/*     */   private boolean inited = false;
/*     */   Map<JavaFileManager.Location, LocationHandler> handlersForLocation;
/*     */   Map<Option, LocationHandler> handlersForOption;
/*     */
/*     */   public Locations() {
/*  92 */     initHandlers();
/*     */   }
/*     */
/*     */   public void update(Log paramLog, Options paramOptions, Lint paramLint, FSInfo paramFSInfo) {
/*  96 */     this.log = paramLog;
/*  97 */     this.options = paramOptions;
/*  98 */     this.lint = paramLint;
/*  99 */     this.fsInfo = paramFSInfo;
/*     */   }
/*     */
/*     */   public Collection<File> bootClassPath() {
/* 103 */     return getLocation(StandardLocation.PLATFORM_CLASS_PATH);
/*     */   }
/*     */
/*     */
/*     */   public boolean isDefaultBootClassPath() {
/* 108 */     BootClassPathLocationHandler bootClassPathLocationHandler = (BootClassPathLocationHandler)getHandler(StandardLocation.PLATFORM_CLASS_PATH);
/* 109 */     return bootClassPathLocationHandler.isDefault();
/*     */   }
/*     */
/*     */
/*     */   boolean isDefaultBootClassPathRtJar(File paramFile) {
/* 114 */     BootClassPathLocationHandler bootClassPathLocationHandler = (BootClassPathLocationHandler)getHandler(StandardLocation.PLATFORM_CLASS_PATH);
/* 115 */     return bootClassPathLocationHandler.isDefaultRtJar(paramFile);
/*     */   }
/*     */
/*     */   public Collection<File> userClassPath() {
/* 119 */     return getLocation(StandardLocation.CLASS_PATH);
/*     */   }
/*     */
/*     */   public Collection<File> sourcePath() {
/* 123 */     Collection<File> collection = getLocation(StandardLocation.SOURCE_PATH);
/*     */
/* 125 */     return (collection == null || collection.isEmpty()) ? null : collection;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static Iterable<File> getPathEntries(String paramString) {
/* 134 */     return getPathEntries(paramString, null);
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
/*     */   private static Iterable<File> getPathEntries(String paramString, File paramFile) {
/* 147 */     ListBuffer listBuffer = new ListBuffer();
/* 148 */     int i = 0;
/* 149 */     while (i <= paramString.length()) {
/* 150 */       int j = paramString.indexOf(File.pathSeparatorChar, i);
/* 151 */       if (j == -1)
/* 152 */         j = paramString.length();
/* 153 */       if (i < j) {
/* 154 */         listBuffer.add(new File(paramString.substring(i, j)));
/* 155 */       } else if (paramFile != null) {
/* 156 */         listBuffer.add(paramFile);
/* 157 */       }  i = j + 1;
/*     */     }
/* 159 */     return (Iterable<File>)listBuffer;
/*     */   }
/*     */
/*     */
/*     */   private class Path
/*     */     extends LinkedHashSet<File>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */
/*     */     private boolean expandJarClassPaths = false;
/*     */
/* 170 */     private Set<File> canonicalValues = new HashSet<>();
/*     */
/*     */     public Path expandJarClassPaths(boolean param1Boolean) {
/* 173 */       this.expandJarClassPaths = param1Boolean;
/* 174 */       return this;
/*     */     }
/*     */
/*     */
/* 178 */     private File emptyPathDefault = null;
/*     */
/*     */     public Path emptyPathDefault(File param1File) {
/* 181 */       this.emptyPathDefault = param1File;
/* 182 */       return this;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public Path addDirectories(String param1String, boolean param1Boolean) {
/* 188 */       boolean bool = this.expandJarClassPaths;
/* 189 */       this.expandJarClassPaths = true;
/*     */       try {
/* 191 */         if (param1String != null)
/* 192 */           for (File file : Locations.getPathEntries(param1String))
/* 193 */             addDirectory(file, param1Boolean);
/* 194 */         return this;
/*     */       } finally {
/* 196 */         this.expandJarClassPaths = bool;
/*     */       }
/*     */     }
/*     */
/*     */     public Path addDirectories(String param1String) {
/* 201 */       return addDirectories(param1String, Locations.this.warn);
/*     */     }
/*     */
/*     */     private void addDirectory(File param1File, boolean param1Boolean) {
/* 205 */       if (!param1File.isDirectory()) {
/* 206 */         if (param1Boolean) {
/* 207 */           Locations.this.log.warning(Lint.LintCategory.PATH, "dir.path.element.not.found", new Object[] { param1File });
/*     */         }
/*     */
/*     */         return;
/*     */       }
/* 212 */       File[] arrayOfFile = param1File.listFiles();
/* 213 */       if (arrayOfFile == null) {
/*     */         return;
/*     */       }
/* 216 */       for (File file : arrayOfFile) {
/* 217 */         if (Locations.this.isArchive(file))
/* 218 */           addFile(file, param1Boolean);
/*     */       }
/*     */     }
/*     */
/*     */     public Path addFiles(String param1String, boolean param1Boolean) {
/* 223 */       if (param1String != null) {
/* 224 */         addFiles(Locations.getPathEntries(param1String, this.emptyPathDefault), param1Boolean);
/*     */       }
/* 226 */       return this;
/*     */     }
/*     */
/*     */     public Path addFiles(String param1String) {
/* 230 */       return addFiles(param1String, Locations.this.warn);
/*     */     }
/*     */
/*     */     public Path addFiles(Iterable<? extends File> param1Iterable, boolean param1Boolean) {
/* 234 */       if (param1Iterable != null)
/* 235 */         for (File file : param1Iterable) {
/* 236 */           addFile(file, param1Boolean);
/*     */         }
/* 238 */       return this;
/*     */     }
/*     */
/*     */     public Path addFiles(Iterable<? extends File> param1Iterable) {
/* 242 */       return addFiles(param1Iterable, Locations.this.warn);
/*     */     }
/*     */
/*     */     public void addFile(File param1File, boolean param1Boolean) {
/* 246 */       if (contains(param1File)) {
/*     */         return;
/*     */       }
/*     */
/*     */
/* 251 */       if (!Locations.this.fsInfo.exists(param1File)) {
/*     */
/* 253 */         if (param1Boolean) {
/* 254 */           Locations.this.log.warning(Lint.LintCategory.PATH, "path.element.not.found", new Object[] { param1File });
/*     */         }
/*     */
/* 257 */         add((E)param1File);
/*     */
/*     */         return;
/*     */       }
/* 261 */       File file = Locations.this.fsInfo.getCanonicalFile(param1File);
/* 262 */       if (this.canonicalValues.contains(file)) {
/*     */         return;
/*     */       }
/*     */
/*     */
/* 267 */       if (Locations.this.fsInfo.isFile(param1File))
/*     */       {
/* 269 */         if (!Locations.this.isArchive(param1File)) {
/*     */
/*     */           try {
/*     */
/* 273 */             ZipFile zipFile = new ZipFile(param1File);
/* 274 */             zipFile.close();
/* 275 */             if (param1Boolean) {
/* 276 */               Locations.this.log.warning(Lint.LintCategory.PATH, "unexpected.archive.file", new Object[] { param1File });
/*     */             }
/*     */           }
/* 279 */           catch (IOException iOException) {
/*     */
/* 281 */             if (param1Boolean) {
/* 282 */               Locations.this.log.warning(Lint.LintCategory.PATH, "invalid.archive.file", new Object[] { param1File });
/*     */             }
/*     */
/*     */
/*     */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */
/*     */
/* 292 */       add((E)param1File);
/* 293 */       this.canonicalValues.add(file);
/*     */
/* 295 */       if (this.expandJarClassPaths && Locations.this.fsInfo.isFile(param1File)) {
/* 296 */         addJarClassPath(param1File, param1Boolean);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     private void addJarClassPath(File param1File, boolean param1Boolean) {
/*     */       try {
/* 305 */         for (File file : Locations.this.fsInfo.getJarClassPath(param1File)) {
/* 306 */           addFile(file, param1Boolean);
/*     */         }
/* 308 */       } catch (IOException iOException) {
/* 309 */         Locations.this.log.error("error.reading.file", new Object[] { param1File, JavacFileManager.getMessage(iOException) });
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected abstract class LocationHandler
/*     */   {
/*     */     final JavaFileManager.Location location;
/*     */
/*     */
/*     */
/*     */
/*     */     final Set<Option> options;
/*     */
/*     */
/*     */
/*     */
/*     */     protected LocationHandler(JavaFileManager.Location param1Location, Option... param1VarArgs) {
/* 331 */       this.location = param1Location;
/* 332 */       this
/*     */
/* 334 */         .options = (param1VarArgs.length == 0) ? EnumSet.<Option>noneOf(Option.class) : EnumSet.<Option>copyOf(Arrays.asList(param1VarArgs));
/*     */     }
/*     */
/*     */
/*     */     void update(Options param1Options) {
/* 339 */       for (Option option : this.options) {
/* 340 */         String str = param1Options.get(option);
/* 341 */         if (str != null) {
/* 342 */           handleOption(option, str);
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     abstract boolean handleOption(Option param1Option, String param1String);
/*     */
/*     */
/*     */     abstract Collection<File> getLocation();
/*     */
/*     */
/*     */     abstract void setLocation(Iterable<? extends File> param1Iterable) throws IOException;
/*     */   }
/*     */
/*     */
/*     */   private class OutputLocationHandler
/*     */     extends LocationHandler
/*     */   {
/*     */     private File outputDir;
/*     */
/*     */
/*     */     OutputLocationHandler(JavaFileManager.Location param1Location, Option... param1VarArgs) {
/* 365 */       super(param1Location, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */     boolean handleOption(Option param1Option, String param1String) {
/* 370 */       if (!this.options.contains(param1Option)) {
/* 371 */         return false;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/* 377 */       this.outputDir = new File(param1String);
/* 378 */       return true;
/*     */     }
/*     */
/*     */
/*     */     Collection<File> getLocation() {
/* 383 */       return (this.outputDir == null) ? null : Collections.<File>singleton(this.outputDir);
/*     */     }
/*     */
/*     */
/*     */     void setLocation(Iterable<? extends File> param1Iterable) throws IOException {
/* 388 */       if (param1Iterable == null) {
/* 389 */         this.outputDir = null;
/*     */       } else {
/* 391 */         Iterator<? extends File> iterator = param1Iterable.iterator();
/* 392 */         if (!iterator.hasNext())
/* 393 */           throw new IllegalArgumentException("empty path for directory");
/* 394 */         File file = iterator.next();
/* 395 */         if (iterator.hasNext())
/* 396 */           throw new IllegalArgumentException("path too long for directory");
/* 397 */         if (!file.exists())
/* 398 */           throw new FileNotFoundException(file + ": does not exist");
/* 399 */         if (!file.isDirectory())
/* 400 */           throw new IOException(file + ": not a directory");
/* 401 */         this.outputDir = file;
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private class SimpleLocationHandler
/*     */     extends LocationHandler
/*     */   {
/*     */     protected Collection<File> searchPath;
/*     */
/*     */
/*     */
/*     */     SimpleLocationHandler(JavaFileManager.Location param1Location, Option... param1VarArgs) {
/* 416 */       super(param1Location, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */     boolean handleOption(Option param1Option, String param1String) {
/* 421 */       if (!this.options.contains(param1Option))
/* 422 */         return false;
/* 423 */       this
/* 424 */         .searchPath = (param1String == null) ? null : Collections.<File>unmodifiableCollection(createPath().addFiles(param1String));
/* 425 */       return true;
/*     */     }
/*     */
/*     */
/*     */     Collection<File> getLocation() {
/* 430 */       return this.searchPath;
/*     */     }
/*     */
/*     */
/*     */     void setLocation(Iterable<? extends File> param1Iterable) {
/*     */       Path path;
/* 436 */       if (param1Iterable == null) {
/* 437 */         path = computePath(null);
/*     */       } else {
/* 439 */         path = createPath().addFiles(param1Iterable);
/*     */       }
/* 441 */       this.searchPath = Collections.unmodifiableCollection(path);
/*     */     }
/*     */
/*     */     protected Path computePath(String param1String) {
/* 445 */       return createPath().addFiles(param1String);
/*     */     }
/*     */
/*     */     protected Path createPath() {
/* 449 */       return new Path();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private class ClassPathLocationHandler
/*     */     extends SimpleLocationHandler
/*     */   {
/*     */     ClassPathLocationHandler() {
/* 460 */       super(StandardLocation.CLASS_PATH, new Option[] { Option.CLASSPATH, Option.CP });
/*     */     }
/*     */
/*     */
/*     */
/*     */     Collection<File> getLocation() {
/* 466 */       lazy();
/* 467 */       return this.searchPath;
/*     */     }
/*     */
/*     */
/*     */     protected Path computePath(String param1String) {
/* 472 */       String str = param1String;
/*     */
/*     */
/* 475 */       if (str == null) str = System.getProperty("env.class.path");
/*     */
/*     */
/*     */
/* 479 */       if (str == null && System.getProperty("application.home") == null) {
/* 480 */         str = System.getProperty("java.class.path");
/*     */       }
/*     */
/* 483 */       if (str == null) str = ".";
/*     */
/* 485 */       return createPath().addFiles(str);
/*     */     }
/*     */
/*     */
/*     */     protected Path createPath() {
/* 490 */       return (new Path())
/* 491 */         .expandJarClassPaths(true)
/* 492 */         .emptyPathDefault(new File("."));
/*     */     }
/*     */
/*     */     private void lazy() {
/* 496 */       if (this.searchPath == null) {
/* 497 */         setLocation((Iterable<? extends File>)null);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private class BootClassPathLocationHandler
/*     */     extends LocationHandler
/*     */   {
/*     */     private Collection<File> searchPath;
/*     */
/*     */
/*     */
/* 513 */     final Map<Option, String> optionValues = new EnumMap<>(Option.class);
/*     */
/*     */
/*     */
/*     */
/*     */
/* 519 */     private File defaultBootClassPathRtJar = null;
/*     */
/*     */
/*     */     private boolean isDefaultBootClassPath;
/*     */
/*     */
/*     */
/*     */     BootClassPathLocationHandler() {
/* 527 */       super(StandardLocation.PLATFORM_CLASS_PATH, new Option[] { Option.BOOTCLASSPATH, Option.XBOOTCLASSPATH, Option.XBOOTCLASSPATH_PREPEND, Option.XBOOTCLASSPATH_APPEND, Option.ENDORSEDDIRS, Option.DJAVA_ENDORSED_DIRS, Option.EXTDIRS, Option.DJAVA_EXT_DIRS });
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     boolean isDefault() {
/* 536 */       lazy();
/* 537 */       return this.isDefaultBootClassPath;
/*     */     }
/*     */
/*     */     boolean isDefaultRtJar(File param1File) {
/* 541 */       lazy();
/* 542 */       return param1File.equals(this.defaultBootClassPathRtJar);
/*     */     }
/*     */
/*     */
/*     */     boolean handleOption(Option param1Option, String param1String) {
/* 547 */       if (!this.options.contains(param1Option)) {
/* 548 */         return false;
/*     */       }
/* 550 */       param1Option = canonicalize(param1Option);
/* 551 */       this.optionValues.put(param1Option, param1String);
/* 552 */       if (param1Option == Option.BOOTCLASSPATH) {
/* 553 */         this.optionValues.remove(Option.XBOOTCLASSPATH_PREPEND);
/* 554 */         this.optionValues.remove(Option.XBOOTCLASSPATH_APPEND);
/*     */       }
/* 556 */       this.searchPath = null;
/* 557 */       return true;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private Option canonicalize(Option param1Option) {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/javac/file/Locations$1.$SwitchMap$com$sun$tools$javac$main$Option : [I
/*     */       //   3: aload_1
/*     */       //   4: invokevirtual ordinal : ()I
/*     */       //   7: iaload
/*     */       //   8: tableswitch default -> 48, 1 -> 36, 2 -> 40, 3 -> 44
/*     */       //   36: getstatic com/sun/tools/javac/main/Option.BOOTCLASSPATH : Lcom/sun/tools/javac/main/Option;
/*     */       //   39: areturn
/*     */       //   40: getstatic com/sun/tools/javac/main/Option.ENDORSEDDIRS : Lcom/sun/tools/javac/main/Option;
/*     */       //   43: areturn
/*     */       //   44: getstatic com/sun/tools/javac/main/Option.EXTDIRS : Lcom/sun/tools/javac/main/Option;
/*     */       //   47: areturn
/*     */       //   48: aload_1
/*     */       //   49: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #563	-> 0
/*     */       //   #565	-> 36
/*     */       //   #567	-> 40
/*     */       //   #569	-> 44
/*     */       //   #571	-> 48
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     Collection<File> getLocation() {
/* 577 */       lazy();
/* 578 */       return this.searchPath;
/*     */     }
/*     */
/*     */
/*     */     void setLocation(Iterable<? extends File> param1Iterable) {
/* 583 */       if (param1Iterable == null) {
/* 584 */         this.searchPath = null;
/*     */       } else {
/* 586 */         this.defaultBootClassPathRtJar = null;
/* 587 */         this.isDefaultBootClassPath = false;
/* 588 */         Path path = (new Path()).addFiles(param1Iterable, false);
/* 589 */         this.searchPath = Collections.unmodifiableCollection(path);
/* 590 */         this.optionValues.clear();
/*     */       }
/*     */     }
/*     */
/*     */     Path computePath() {
/* 595 */       this.defaultBootClassPathRtJar = null;
/* 596 */       Path path = new Path();
/*     */
/* 598 */       String str1 = this.optionValues.get(Option.BOOTCLASSPATH);
/* 599 */       String str2 = this.optionValues.get(Option.ENDORSEDDIRS);
/* 600 */       String str3 = this.optionValues.get(Option.EXTDIRS);
/* 601 */       String str4 = this.optionValues.get(Option.XBOOTCLASSPATH_PREPEND);
/* 602 */       String str5 = this.optionValues.get(Option.XBOOTCLASSPATH_APPEND);
/* 603 */       path.addFiles(str4);
/*     */
/* 605 */       if (str2 != null) {
/* 606 */         path.addDirectories(str2);
/*     */       } else {
/* 608 */         path.addDirectories(System.getProperty("java.endorsed.dirs"), false);
/*     */       }
/* 610 */       if (str1 != null) {
/* 611 */         path.addFiles(str1);
/*     */       } else {
/*     */
/* 614 */         String str = System.getProperty("sun.boot.class.path");
/* 615 */         path.addFiles(str, false);
/* 616 */         File file = new File("rt.jar");
/* 617 */         for (File file1 : Locations.getPathEntries(str)) {
/* 618 */           if ((new File(file1.getName())).equals(file)) {
/* 619 */             this.defaultBootClassPathRtJar = file1;
/*     */           }
/*     */         }
/*     */       }
/* 623 */       path.addFiles(str5);
/*     */
/*     */
/*     */
/*     */
/* 628 */       if (str3 != null) {
/* 629 */         path.addDirectories(str3);
/*     */       } else {
/* 631 */         path.addDirectories(System.getProperty("java.ext.dirs"), false);
/*     */       }
/* 633 */       this.isDefaultBootClassPath = (str4 == null && str1 == null && str5 == null);
/*     */
/*     */
/*     */
/*     */
/* 638 */       return path;
/*     */     }
/*     */
/*     */     private void lazy() {
/* 642 */       if (this.searchPath == null) {
/* 643 */         this.searchPath = Collections.unmodifiableCollection(computePath());
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   void initHandlers() {
/* 651 */     this.handlersForLocation = new HashMap<>();
/* 652 */     this.handlersForOption = new EnumMap<>(Option.class);
/*     */
/* 654 */     LocationHandler[] arrayOfLocationHandler = { new BootClassPathLocationHandler(), new ClassPathLocationHandler(), new SimpleLocationHandler(StandardLocation.SOURCE_PATH, new Option[] { Option.SOURCEPATH }), new SimpleLocationHandler(StandardLocation.ANNOTATION_PROCESSOR_PATH, new Option[] { Option.PROCESSORPATH }), new OutputLocationHandler(StandardLocation.CLASS_OUTPUT, new Option[] { Option.D }), new OutputLocationHandler(StandardLocation.SOURCE_OUTPUT, new Option[] { Option.S }), new OutputLocationHandler(StandardLocation.NATIVE_HEADER_OUTPUT, new Option[] { Option.H }) };
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 664 */     for (LocationHandler locationHandler : arrayOfLocationHandler) {
/* 665 */       this.handlersForLocation.put(locationHandler.location, locationHandler);
/* 666 */       for (Option option : locationHandler.options)
/* 667 */         this.handlersForOption.put(option, locationHandler);
/*     */     }
/*     */   }
/*     */
/*     */   boolean handleOption(Option paramOption, String paramString) {
/* 672 */     LocationHandler locationHandler = this.handlersForOption.get(paramOption);
/* 673 */     return (locationHandler == null) ? false : locationHandler.handleOption(paramOption, paramString);
/*     */   }
/*     */
/*     */   Collection<File> getLocation(JavaFileManager.Location paramLocation) {
/* 677 */     LocationHandler locationHandler = getHandler(paramLocation);
/* 678 */     return (locationHandler == null) ? null : locationHandler.getLocation();
/*     */   }
/*     */
/*     */   File getOutputLocation(JavaFileManager.Location paramLocation) {
/* 682 */     if (!paramLocation.isOutputLocation())
/* 683 */       throw new IllegalArgumentException();
/* 684 */     LocationHandler locationHandler = getHandler(paramLocation);
/* 685 */     return ((OutputLocationHandler)locationHandler).outputDir;
/*     */   }
/*     */
/*     */   void setLocation(JavaFileManager.Location paramLocation, Iterable<? extends File> paramIterable) throws IOException {
/* 689 */     LocationHandler locationHandler = getHandler(paramLocation);
/* 690 */     if (locationHandler == null) {
/* 691 */       if (paramLocation.isOutputLocation()) {
/* 692 */         locationHandler = new OutputLocationHandler(paramLocation, new Option[0]);
/*     */       } else {
/* 694 */         locationHandler = new SimpleLocationHandler(paramLocation, new Option[0]);
/* 695 */       }  this.handlersForLocation.put(paramLocation, locationHandler);
/*     */     }
/* 697 */     locationHandler.setLocation(paramIterable);
/*     */   }
/*     */
/*     */   protected LocationHandler getHandler(JavaFileManager.Location paramLocation) {
/* 701 */     paramLocation.getClass();
/* 702 */     lazy();
/* 703 */     return this.handlersForLocation.get(paramLocation);
/*     */   }
/*     */
/*     */
/*     */   protected void lazy() {
/* 708 */     if (!this.inited) {
/* 709 */       this.warn = this.lint.isEnabled(Lint.LintCategory.PATH);
/*     */
/* 711 */       for (LocationHandler locationHandler : this.handlersForLocation.values()) {
/* 712 */         locationHandler.update(this.options);
/*     */       }
/*     */
/* 715 */       this.inited = true;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   private boolean isArchive(File paramFile) {
/* 721 */     String str = StringUtils.toLowerCase(paramFile.getName());
/* 722 */     return (this.fsInfo.isFile(paramFile) && (str
/* 723 */       .endsWith(".jar") || str.endsWith(".zip")));
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
/*     */   public static URL[] pathToURLs(String paramString) {
/* 736 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
/* 737 */     URL[] arrayOfURL = new URL[stringTokenizer.countTokens()];
/* 738 */     byte b = 0;
/* 739 */     while (stringTokenizer.hasMoreTokens()) {
/* 740 */       URL uRL = fileToURL(new File(stringTokenizer.nextToken()));
/* 741 */       if (uRL != null) {
/* 742 */         arrayOfURL[b++] = uRL;
/*     */       }
/*     */     }
/* 745 */     arrayOfURL = Arrays.<URL>copyOf(arrayOfURL, b);
/* 746 */     return arrayOfURL;
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
/*     */   private static URL fileToURL(File paramFile) {
/*     */     try {
/* 759 */       str = paramFile.getCanonicalPath();
/* 760 */     } catch (IOException iOException) {
/* 761 */       str = paramFile.getAbsolutePath();
/*     */     }
/* 763 */     String str = str.replace(File.separatorChar, '/');
/* 764 */     if (!str.startsWith("/")) {
/* 765 */       str = "/" + str;
/*     */     }
/*     */
/* 768 */     if (!paramFile.isFile()) {
/* 769 */       str = str + "/";
/*     */     }
/*     */     try {
/* 772 */       return new URL("file", "", str);
/* 773 */     } catch (MalformedURLException malformedURLException) {
/* 774 */       throw new IllegalArgumentException(paramFile.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\Locations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
