/*     */ package com.sun.tools.javac.main;
/*     */ 
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.Plugin;
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.api.BasicJavacTask;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.file.CacheFSInfo;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.jvm.Target;
/*     */ import com.sun.tools.javac.processing.AnnotationProcessingError;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.FatalError;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.PropagatedException;
/*     */ import com.sun.tools.javac.util.ServiceLoader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.Processor;
/*     */ import javax.tools.JavaFileManager;
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
/*     */ public class Main
/*     */ {
/*     */   String ownName;
/*     */   PrintWriter out;
/*     */   public Log log;
/*     */   boolean apiMode;
/*     */   
/*     */   public enum Result
/*     */   {
/*  92 */     OK(0),
/*  93 */     ERROR(1),
/*  94 */     CMDERR(2),
/*  95 */     SYSERR(3),
/*  96 */     ABNORMAL(4); public final int exitCode;
/*     */     
/*     */     Result(int param1Int1) {
/*  99 */       this.exitCode = param1Int1;
/*     */     }
/*     */     
/*     */     public boolean isOK() {
/* 103 */       return (this.exitCode == 0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   private Option[] recognizedOptions = Option.getJavaCompilerOptions().<Option>toArray(new Option[0]);
/*     */   
/* 112 */   private OptionHelper optionHelper = new OptionHelper()
/*     */     {
/*     */       public String get(Option param1Option) {
/* 115 */         return Main.this.options.get(param1Option);
/*     */       }
/*     */ 
/*     */       
/*     */       public void put(String param1String1, String param1String2) {
/* 120 */         Main.this.options.put(param1String1, param1String2);
/*     */       }
/*     */ 
/*     */       
/*     */       public void remove(String param1String) {
/* 125 */         Main.this.options.remove(param1String);
/*     */       }
/*     */ 
/*     */       
/*     */       public Log getLog() {
/* 130 */         return Main.this.log;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getOwnName() {
/* 135 */         return Main.this.ownName;
/*     */       }
/*     */ 
/*     */       
/*     */       public void error(String param1String, Object... param1VarArgs) {
/* 140 */         Main.this.error(param1String, param1VarArgs);
/*     */       }
/*     */ 
/*     */       
/*     */       public void addFile(File param1File) {
/* 145 */         Main.this.filenames.add(param1File);
/*     */       }
/*     */ 
/*     */       
/*     */       public void addClassName(String param1String) {
/* 150 */         Main.this.classnames.append(param1String);
/*     */       }
/*     */     };
/*     */   private Options options; public Set<File> filenames;
/*     */   public ListBuffer<String> classnames;
/*     */   private JavaFileManager fileManager;
/*     */   public static final String javacBundleName = "com.sun.tools.javac.resources.javac";
/*     */   
/*     */   public Main(String paramString) {
/* 159 */     this(paramString, new PrintWriter(System.err, true));
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
/*     */   public Main(String paramString, PrintWriter paramPrintWriter) {
/* 171 */     this.options = null;
/*     */ 
/*     */ 
/*     */     
/* 175 */     this.filenames = null;
/*     */ 
/*     */ 
/*     */     
/* 179 */     this.classnames = null;
/*     */     this.ownName = paramString;
/*     */     this.out = paramPrintWriter;
/*     */   }
/*     */   void error(String paramString, Object... paramVarArgs) {
/* 184 */     if (this.apiMode) {
/* 185 */       String str = this.log.localize(Log.PrefixKind.JAVAC, paramString, paramVarArgs);
/* 186 */       throw new PropagatedException(new IllegalStateException(str));
/*     */     } 
/* 188 */     warning(paramString, paramVarArgs);
/* 189 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.usage", new Object[] { this.ownName });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void warning(String paramString, Object... paramVarArgs) {
/* 195 */     this.log.printRawLines(this.ownName + ": " + this.log.localize(Log.PrefixKind.JAVAC, paramString, paramVarArgs));
/*     */   }
/*     */   
/*     */   public Option getOption(String paramString) {
/* 199 */     for (Option option : this.recognizedOptions) {
/* 200 */       if (option.matches(paramString))
/* 201 */         return option; 
/*     */     } 
/* 203 */     return null;
/*     */   }
/*     */   
/*     */   public void setOptions(Options paramOptions) {
/* 207 */     if (paramOptions == null)
/* 208 */       throw new NullPointerException(); 
/* 209 */     this.options = paramOptions;
/*     */   }
/*     */   
/*     */   public void setAPIMode(boolean paramBoolean) {
/* 213 */     this.apiMode = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<File> processArgs(String[] paramArrayOfString) {
/* 221 */     return processArgs(paramArrayOfString, null);
/*     */   }
/*     */   
/*     */   public Collection<File> processArgs(String[] paramArrayOfString1, String[] paramArrayOfString2) {
/* 225 */     byte b = 0;
/* 226 */     while (b < paramArrayOfString1.length) {
/* 227 */       String str = paramArrayOfString1[b];
/* 228 */       b++;
/*     */       
/* 230 */       Option option = null;
/*     */       
/* 232 */       if (str.length() > 0) {
/*     */ 
/*     */ 
/*     */         
/* 236 */         byte b1 = (str.charAt(0) == '-') ? 0 : (this.recognizedOptions.length - 1);
/* 237 */         for (byte b2 = b1; b2 < this.recognizedOptions.length; b2++) {
/* 238 */           if (this.recognizedOptions[b2].matches(str)) {
/* 239 */             option = this.recognizedOptions[b2];
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 245 */       if (option == null) {
/* 246 */         error("err.invalid.flag", new Object[] { str });
/* 247 */         return null;
/*     */       } 
/*     */       
/* 250 */       if (option.hasArg()) {
/* 251 */         if (b == paramArrayOfString1.length) {
/* 252 */           error("err.req.arg", new Object[] { str });
/* 253 */           return null;
/*     */         } 
/* 255 */         String str5 = paramArrayOfString1[b];
/* 256 */         b++;
/* 257 */         if (option.process(this.optionHelper, str, str5))
/* 258 */           return null;  continue;
/*     */       } 
/* 260 */       if (option.process(this.optionHelper, str)) {
/* 261 */         return null;
/*     */       }
/*     */     } 
/*     */     
/* 265 */     if (this.options.get(Option.PROFILE) != null && this.options.get(Option.BOOTCLASSPATH) != null) {
/* 266 */       error("err.profile.bootclasspath.conflict", new Object[0]);
/* 267 */       return null;
/*     */     } 
/*     */     
/* 270 */     if (this.classnames != null && paramArrayOfString2 != null) {
/* 271 */       this.classnames.addAll(Arrays.asList(paramArrayOfString2));
/*     */     }
/*     */     
/* 274 */     if (!checkDirectory(Option.D))
/* 275 */       return null; 
/* 276 */     if (!checkDirectory(Option.S)) {
/* 277 */       return null;
/*     */     }
/* 279 */     String str1 = this.options.get(Option.SOURCE);
/*     */     
/* 281 */     Source source = (str1 != null) ? Source.lookup(str1) : Source.DEFAULT;
/*     */     
/* 283 */     String str2 = this.options.get(Option.TARGET);
/*     */     
/* 285 */     Target target = (str2 != null) ? Target.lookup(str2) : Target.DEFAULT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     if (Character.isDigit(target.name.charAt(0))) {
/* 293 */       if (target.compareTo((Enum)source.requiredTarget()) < 0) {
/* 294 */         if (str2 != null) {
/* 295 */           if (str1 == null) {
/* 296 */             warning("warn.target.default.source.conflict", new Object[] { str2, 
/*     */                   
/* 298 */                   (source.requiredTarget()).name });
/*     */           } else {
/* 300 */             warning("warn.source.target.conflict", new Object[] { str1, 
/*     */                   
/* 302 */                   (source.requiredTarget()).name });
/*     */           } 
/* 304 */           return null;
/*     */         } 
/* 306 */         target = source.requiredTarget();
/* 307 */         this.options.put("-target", target.name);
/*     */       
/*     */       }
/* 310 */       else if (str2 == null && !source.allowGenerics()) {
/* 311 */         target = Target.JDK1_4;
/* 312 */         this.options.put("-target", target.name);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 317 */     String str3 = this.options.get(Option.PROFILE);
/* 318 */     if (str3 != null) {
/* 319 */       Profile profile = Profile.lookup(str3);
/* 320 */       if (!profile.isValid(target)) {
/* 321 */         warning("warn.profile.target.conflict", new Object[] { str3, target.name });
/* 322 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 327 */     String str4 = this.options.get("showClass");
/* 328 */     if (str4 != null) {
/* 329 */       if (str4.equals("showClass"))
/* 330 */         str4 = "com.sun.tools.javac.Main"; 
/* 331 */       showClass(str4);
/*     */     } 
/*     */     
/* 334 */     this.options.notifyListeners();
/*     */     
/* 336 */     return this.filenames;
/*     */   }
/*     */   
/*     */   private boolean checkDirectory(Option paramOption) {
/* 340 */     String str = this.options.get(paramOption);
/* 341 */     if (str == null)
/* 342 */       return true; 
/* 343 */     File file = new File(str);
/* 344 */     if (!file.exists()) {
/* 345 */       error("err.dir.not.found", new Object[] { str });
/* 346 */       return false;
/*     */     } 
/* 348 */     if (!file.isDirectory()) {
/* 349 */       error("err.file.not.directory", new Object[] { str });
/* 350 */       return false;
/*     */     } 
/* 352 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Result compile(String[] paramArrayOfString) {
/* 359 */     Context context = new Context();
/* 360 */     JavacFileManager.preRegister(context);
/* 361 */     Result result = compile(paramArrayOfString, context);
/* 362 */     if (this.fileManager instanceof JavacFileManager)
/*     */     {
/* 364 */       ((JavacFileManager)this.fileManager).close();
/*     */     }
/* 366 */     return result;
/*     */   }
/*     */   
/*     */   public Result compile(String[] paramArrayOfString, Context paramContext) {
/* 370 */     return compile(paramArrayOfString, paramContext, List.nil(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Result compile(String[] paramArrayOfString, Context paramContext, List<JavaFileObject> paramList, Iterable<? extends Processor> paramIterable) {
/* 381 */     return compile(paramArrayOfString, null, paramContext, paramList, paramIterable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Result compile(String[] paramArrayOfString1, String[] paramArrayOfString2, Context paramContext, List<JavaFileObject> paramList, Iterable<? extends Processor> paramIterable) {
/* 390 */     paramContext.put(Log.outKey, this.out);
/* 391 */     this.log = Log.instance(paramContext);
/*     */     
/* 393 */     if (this.options == null) {
/* 394 */       this.options = Options.instance(paramContext);
/*     */     }
/* 396 */     this.filenames = new LinkedHashSet<>();
/* 397 */     this.classnames = new ListBuffer();
/* 398 */     JavaCompiler javaCompiler = null;
/*     */ 
/*     */     
/*     */     try {
/*     */       Collection<File> collection;
/*     */ 
/*     */       
/* 405 */       if (paramArrayOfString1.length == 0 && (paramArrayOfString2 == null || paramArrayOfString2.length == 0) && paramList
/*     */         
/* 407 */         .isEmpty()) {
/* 408 */         Option.HELP.process(this.optionHelper, "-help");
/* 409 */         return Result.CMDERR;
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 414 */         collection = processArgs(CommandLine.parse(paramArrayOfString1), paramArrayOfString2);
/* 415 */         if (collection == null)
/*     */         {
/* 417 */           return Result.CMDERR; } 
/* 418 */         if (collection.isEmpty() && paramList.isEmpty() && this.classnames.isEmpty()) {
/*     */           
/* 420 */           if (this.options.isSet(Option.HELP) || this.options
/* 421 */             .isSet(Option.X) || this.options
/* 422 */             .isSet(Option.VERSION) || this.options
/* 423 */             .isSet(Option.FULLVERSION))
/* 424 */             return Result.OK; 
/* 425 */           if (JavaCompiler.explicitAnnotationProcessingRequested(this.options)) {
/* 426 */             error("err.no.source.files.classes", new Object[0]);
/*     */           } else {
/* 428 */             error("err.no.source.files", new Object[0]);
/*     */           } 
/* 430 */           return Result.CMDERR;
/*     */         } 
/* 432 */       } catch (FileNotFoundException fileNotFoundException) {
/* 433 */         warning("err.file.not.found", new Object[] { fileNotFoundException.getMessage() });
/* 434 */         return Result.SYSERR;
/*     */       } 
/*     */       
/* 437 */       boolean bool = this.options.isSet("stdout");
/* 438 */       if (bool) {
/* 439 */         this.log.flush();
/* 440 */         this.log.setWriters(new PrintWriter(System.out, true));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 445 */       boolean bool1 = (this.options.isUnset("nonBatchMode") && System.getProperty("nonBatchMode") == null) ? true : false;
/* 446 */       if (bool1) {
/* 447 */         CacheFSInfo.preRegister(paramContext);
/*     */       }
/*     */ 
/*     */       
/* 451 */       String str1 = this.options.get(Option.PLUGIN);
/* 452 */       if (str1 != null) {
/* 453 */         JavacProcessingEnvironment javacProcessingEnvironment = JavacProcessingEnvironment.instance(paramContext);
/* 454 */         ClassLoader classLoader = javacProcessingEnvironment.getProcessorClassLoader();
/* 455 */         ServiceLoader serviceLoader = ServiceLoader.load(Plugin.class, classLoader);
/* 456 */         LinkedHashSet<List> linkedHashSet = new LinkedHashSet();
/* 457 */         for (String str : str1.split("\\x00")) {
/* 458 */           linkedHashSet.add(List.from((Object[])str.split("\\s+")));
/*     */         }
/* 460 */         JavacTask javacTask = null;
/* 461 */         Iterator<Plugin> iterator = serviceLoader.iterator();
/* 462 */         while (iterator.hasNext()) {
/* 463 */           Plugin plugin = iterator.next();
/* 464 */           for (List list : linkedHashSet) {
/* 465 */             if (plugin.getName().equals(list.head)) {
/* 466 */               linkedHashSet.remove(list);
/*     */               try {
/* 468 */                 if (javacTask == null)
/* 469 */                   javacTask = JavacTask.instance((ProcessingEnvironment)javacProcessingEnvironment); 
/* 470 */                 plugin.init(javacTask, (String[])list.tail.toArray((Object[])new String[list.tail.size()]));
/* 471 */               } catch (Throwable throwable) {
/* 472 */                 if (this.apiMode)
/* 473 */                   throw new RuntimeException(throwable); 
/* 474 */                 pluginMessage(throwable);
/* 475 */                 return Result.SYSERR;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 480 */         for (List list : linkedHashSet) {
/* 481 */           this.log.printLines(Log.PrefixKind.JAVAC, "msg.plugin.not.found", new Object[] { list.head });
/*     */         } 
/*     */       } 
/*     */       
/* 485 */       javaCompiler = JavaCompiler.instance(paramContext);
/*     */ 
/*     */       
/* 488 */       String str2 = this.options.get(Option.XDOCLINT);
/* 489 */       String str3 = this.options.get(Option.XDOCLINT_CUSTOM);
/* 490 */       if (str2 != null || str3 != null) {
/* 491 */         LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
/* 492 */         if (str2 != null)
/* 493 */           linkedHashSet.add("-Xmsgs"); 
/* 494 */         if (str3 != null)
/* 495 */           for (String str : str3.split("\\s+")) {
/* 496 */             if (!str.isEmpty())
/*     */             {
/* 498 */               linkedHashSet.add(str.replace(Option.XDOCLINT_CUSTOM.text, "-Xmsgs:"));
/*     */             }
/*     */           }  
/* 501 */         if (linkedHashSet.size() != 1 || 
/* 502 */           !((String)linkedHashSet.iterator().next()).equals("-Xmsgs:none")) {
/* 503 */           JavacTask javacTask = BasicJavacTask.instance(paramContext);
/*     */           
/* 505 */           linkedHashSet.add("-XimplicitHeaders:2");
/* 506 */           (new DocLint()).init(javacTask, linkedHashSet.<String>toArray(new String[linkedHashSet.size()]));
/* 507 */           javaCompiler.keepComments = true;
/*     */         } 
/*     */       } 
/*     */       
/* 511 */       this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class);
/*     */       
/* 513 */       if (!collection.isEmpty()) {
/*     */         
/* 515 */         javaCompiler = JavaCompiler.instance(paramContext);
/* 516 */         List list = List.nil();
/* 517 */         JavacFileManager javacFileManager = (JavacFileManager)this.fileManager;
/* 518 */         for (JavaFileObject javaFileObject : javacFileManager.getJavaFileObjectsFromFiles(collection))
/* 519 */           list = list.prepend(javaFileObject); 
/* 520 */         for (JavaFileObject javaFileObject : list)
/* 521 */           paramList = paramList.prepend(javaFileObject); 
/*     */       } 
/* 523 */       javaCompiler.compile(paramList, this.classnames
/* 524 */           .toList(), paramIterable);
/*     */ 
/*     */       
/* 527 */       if (this.log.expectDiagKeys != null) {
/* 528 */         if (this.log.expectDiagKeys.isEmpty()) {
/* 529 */           this.log.printRawLines("all expected diagnostics found");
/* 530 */           return Result.OK;
/*     */         } 
/* 532 */         this.log.printRawLines("expected diagnostic keys not found: " + this.log.expectDiagKeys);
/* 533 */         return Result.ERROR;
/*     */       } 
/*     */ 
/*     */       
/* 537 */       if (javaCompiler.errorCount() != 0)
/* 538 */         return Result.ERROR; 
/* 539 */     } catch (IOException iOException) {
/* 540 */       ioMessage(iOException);
/* 541 */       return Result.SYSERR;
/* 542 */     } catch (OutOfMemoryError outOfMemoryError) {
/* 543 */       resourceMessage(outOfMemoryError);
/* 544 */       return Result.SYSERR;
/* 545 */     } catch (StackOverflowError stackOverflowError) {
/* 546 */       resourceMessage(stackOverflowError);
/* 547 */       return Result.SYSERR;
/* 548 */     } catch (FatalError fatalError) {
/* 549 */       feMessage((Throwable)fatalError);
/* 550 */       return Result.SYSERR;
/* 551 */     } catch (AnnotationProcessingError annotationProcessingError) {
/* 552 */       if (this.apiMode)
/* 553 */         throw new RuntimeException(annotationProcessingError.getCause()); 
/* 554 */       apMessage(annotationProcessingError);
/* 555 */       return Result.SYSERR;
/* 556 */     } catch (ClientCodeException clientCodeException) {
/*     */ 
/*     */       
/* 559 */       throw new RuntimeException(clientCodeException.getCause());
/* 560 */     } catch (PropagatedException propagatedException) {
/* 561 */       throw propagatedException.getCause();
/* 562 */     } catch (Throwable throwable) {
/*     */ 
/*     */ 
/*     */       
/* 566 */       if (javaCompiler == null || javaCompiler.errorCount() == 0 || this.options == null || this.options
/* 567 */         .isSet("dev"))
/* 568 */         bugMessage(throwable); 
/* 569 */       return Result.ABNORMAL;
/*     */     } finally {
/* 571 */       if (javaCompiler != null) {
/*     */         try {
/* 573 */           javaCompiler.close();
/* 574 */         } catch (ClientCodeException clientCodeException) {
/* 575 */           throw new RuntimeException(clientCodeException.getCause());
/*     */         } 
/*     */       }
/* 578 */       this.filenames = null;
/* 579 */       this.options = null;
/*     */     } 
/* 581 */     return Result.OK;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void bugMessage(Throwable paramThrowable) {
/* 587 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.bug", new Object[] { JavaCompiler.version() });
/* 588 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void feMessage(Throwable paramThrowable) {
/* 594 */     this.log.printRawLines(paramThrowable.getMessage());
/* 595 */     if (paramThrowable.getCause() != null && this.options.isSet("dev")) {
/* 596 */       paramThrowable.getCause().printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void ioMessage(Throwable paramThrowable) {
/* 603 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.io", new Object[0]);
/* 604 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void resourceMessage(Throwable paramThrowable) {
/* 610 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.resource", new Object[0]);
/* 611 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void apMessage(AnnotationProcessingError paramAnnotationProcessingError) {
/* 618 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.proc.annotation.uncaught.exception", new Object[0]);
/* 619 */     paramAnnotationProcessingError.getCause().printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void pluginMessage(Throwable paramThrowable) {
/* 626 */     this.log.printLines(Log.PrefixKind.JAVAC, "msg.plugin.uncaught.exception", new Object[0]);
/* 627 */     paramThrowable.printStackTrace(this.log.getWriter(Log.WriterKind.NOTICE));
/*     */   }
/*     */ 
/*     */   
/*     */   void showClass(String paramString) {
/* 632 */     PrintWriter printWriter = this.log.getWriter(Log.WriterKind.NOTICE);
/* 633 */     printWriter.println("javac: show class: " + paramString);
/* 634 */     URL uRL = getClass().getResource('/' + paramString.replace('.', '/') + ".class");
/* 635 */     if (uRL == null) {
/* 636 */       printWriter.println("  class not found");
/*     */     } else {
/* 638 */       printWriter.println("  " + uRL);
/*     */ 
/*     */       
/*     */       try {
/* 642 */         MessageDigest messageDigest = MessageDigest.getInstance("MD5");
/* 643 */         DigestInputStream digestInputStream = new DigestInputStream(uRL.openStream(), messageDigest);
/*     */         
/* 645 */         try { byte[] arrayOfByte = new byte[8192];
/*     */           while (true)
/* 647 */           { int i = digestInputStream.read(arrayOfByte); if (i <= 0)
/* 648 */             { byte[] arrayOfByte1 = messageDigest.digest();
/*     */               
/* 650 */               digestInputStream.close();
/*     */               
/* 652 */               StringBuilder stringBuilder = new StringBuilder();
/* 653 */               for (byte b : arrayOfByte1)
/* 654 */               { stringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(b) })); }  return; }  }  } finally { digestInputStream.close(); }
/*     */       
/* 656 */       } catch (Exception exception) {
/* 657 */         printWriter.println("  cannot compute digest: " + exception);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\main\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */