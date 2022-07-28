/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.DiagnosticListener;
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
/*     */ public class Log
/*     */   extends AbstractLog
/*     */ {
/*  56 */   public static final Context.Key<Log> logKey = new Context.Key<>();
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final Context.Key<PrintWriter> outKey = new Context.Key<>(); protected PrintWriter errWriter; protected PrintWriter warnWriter; protected PrintWriter noticeWriter; protected int MaxErrors; protected int MaxWarnings; public boolean promptOnError; public boolean emitWarnings; public boolean suppressNotes; public boolean dumpOnError; public boolean multipleErrors; protected DiagnosticListener<? super JavaFileObject> diagListener; private DiagnosticFormatter<JCDiagnostic> diagFormatter; public Set<String> expectDiagKeys; public boolean compressedOutput; private JavacMessages messages; private DiagnosticHandler diagnosticHandler;
/*     */   public int nerrors;
/*     */   public int nwarnings;
/*     */   private Set<Pair<JavaFileObject, Integer>> recorded;
/*     */   
/*  65 */   public enum PrefixKind { JAVAC("javac."),
/*  66 */     COMPILER_MISC("compiler.misc."); final String value;
/*     */     PrefixKind(String param1String1) {
/*  68 */       this.value = param1String1;
/*     */     }
/*     */     public String key(String param1String) {
/*  71 */       return this.value + param1String;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class DiagnosticHandler
/*     */   {
/*     */     protected DiagnosticHandler prev;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void install(Log param1Log) {
/*  97 */       this.prev = param1Log.diagnosticHandler;
/*  98 */       param1Log.diagnosticHandler = this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract void report(JCDiagnostic param1JCDiagnostic);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DiscardDiagnosticHandler
/*     */     extends DiagnosticHandler
/*     */   {
/*     */     public DiscardDiagnosticHandler(Log param1Log) {
/* 112 */       install(param1Log);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void report(JCDiagnostic param1JCDiagnostic) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DeferredDiagnosticHandler
/*     */     extends DiagnosticHandler
/*     */   {
/* 126 */     private Queue<JCDiagnostic> deferred = new ListBuffer<>();
/*     */     private final Filter<JCDiagnostic> filter;
/*     */     
/*     */     public DeferredDiagnosticHandler(Log param1Log) {
/* 130 */       this(param1Log, (Filter<JCDiagnostic>)null);
/*     */     }
/*     */     
/*     */     public DeferredDiagnosticHandler(Log param1Log, Filter<JCDiagnostic> param1Filter) {
/* 134 */       this.filter = param1Filter;
/* 135 */       install(param1Log);
/*     */     }
/*     */     
/*     */     public void report(JCDiagnostic param1JCDiagnostic) {
/* 139 */       if (!param1JCDiagnostic.isFlagSet(JCDiagnostic.DiagnosticFlag.NON_DEFERRABLE) && (this.filter == null || this.filter
/* 140 */         .accepts(param1JCDiagnostic))) {
/* 141 */         this.deferred.add(param1JCDiagnostic);
/*     */       } else {
/* 143 */         this.prev.report(param1JCDiagnostic);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Queue<JCDiagnostic> getDiagnostics() {
/* 148 */       return this.deferred;
/*     */     }
/*     */ 
/*     */     
/*     */     public void reportDeferredDiagnostics() {
/* 153 */       reportDeferredDiagnostics(EnumSet.allOf(Diagnostic.Kind.class));
/*     */     }
/*     */ 
/*     */     
/*     */     public void reportDeferredDiagnostics(Set<Diagnostic.Kind> param1Set) {
/*     */       JCDiagnostic jCDiagnostic;
/* 159 */       while ((jCDiagnostic = this.deferred.poll()) != null) {
/* 160 */         if (param1Set.contains(jCDiagnostic.getKind()))
/* 161 */           this.prev.report(jCDiagnostic); 
/*     */       } 
/* 163 */       this.deferred = null;
/*     */     } }
/*     */   
/*     */   public enum WriterKind {
/* 167 */     NOTICE, WARNING, ERROR;
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
/*     */   protected Log(Context paramContext, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3) {
/* 234 */     super(JCDiagnostic.Factory.instance(paramContext));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     this.nerrors = 0;
/*     */ 
/*     */ 
/*     */     
/* 336 */     this.nwarnings = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     this.recorded = new HashSet<>(); paramContext.put(logKey, this); this.errWriter = paramPrintWriter1; this.warnWriter = paramPrintWriter2; this.noticeWriter = paramPrintWriter3; DiagnosticListener<? super JavaFileObject> diagnosticListener = paramContext.<DiagnosticListener>get(DiagnosticListener.class); this.diagListener = diagnosticListener; this.diagnosticHandler = new DefaultDiagnosticHandler(); this.messages = JavacMessages.instance(paramContext); this.messages.add("com.sun.tools.javac.resources.javac"); final Options options = Options.instance(paramContext); initOptions(options); options.addListener(new Runnable() {
/*     */           public void run() { Log.this.initOptions(options); }
/*     */         });
/* 345 */   } private void initOptions(Options paramOptions) { this.dumpOnError = paramOptions.isSet(Option.DOE); this.promptOnError = paramOptions.isSet(Option.PROMPT); this.emitWarnings = paramOptions.isUnset(Option.XLINT_CUSTOM, "none"); this.suppressNotes = paramOptions.isSet("suppressNotes"); this.MaxErrors = getIntOption(paramOptions, Option.XMAXERRS, getDefaultMaxErrors()); this.MaxWarnings = getIntOption(paramOptions, Option.XMAXWARNS, getDefaultMaxWarnings()); boolean bool = paramOptions.isSet("rawDiagnostics"); this.diagFormatter = bool ? new RawDiagnosticFormatter(paramOptions) : new BasicDiagnosticFormatter(paramOptions, this.messages); String str = paramOptions.get("expectKeys"); if (str != null) this.expectDiagKeys = new HashSet<>(Arrays.asList(str.split(", *")));  } private int getIntOption(Options paramOptions, Option paramOption, int paramInt) { String str = paramOptions.get(paramOption); try { if (str != null) { int i = Integer.parseInt(str); return (i <= 0) ? Integer.MAX_VALUE : i; }  } catch (NumberFormatException numberFormatException) {} return paramInt; } protected int getDefaultMaxErrors() { return 100; } protected int getDefaultMaxWarnings() { return 100; } public boolean hasDiagnosticListener() { return (this.diagListener != null); }
/*     */   static PrintWriter defaultWriter(Context paramContext) { PrintWriter printWriter = paramContext.<PrintWriter>get(outKey); if (printWriter == null) paramContext.put(outKey, printWriter = new PrintWriter(System.err));  return printWriter; }
/*     */   protected Log(Context paramContext) { this(paramContext, defaultWriter(paramContext)); }
/*     */   protected Log(Context paramContext, PrintWriter paramPrintWriter) { this(paramContext, paramPrintWriter, paramPrintWriter, paramPrintWriter); }
/* 349 */   public static Log instance(Context paramContext) { Log log = paramContext.<Log>get(logKey); if (log == null) log = new Log(paramContext);  return log; } public void setEndPosTable(JavaFileObject paramJavaFileObject, EndPosTable paramEndPosTable) { paramJavaFileObject.getClass();
/* 350 */     getSource(paramJavaFileObject).setEndPosTable(paramEndPosTable); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaFileObject currentSourceFile() {
/* 356 */     return (this.source == null) ? null : this.source.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DiagnosticFormatter<JCDiagnostic> getDiagnosticFormatter() {
/* 362 */     return this.diagFormatter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDiagnosticFormatter(DiagnosticFormatter<JCDiagnostic> paramDiagnosticFormatter) {
/* 368 */     this.diagFormatter = paramDiagnosticFormatter;
/*     */   }
/*     */   
/*     */   public PrintWriter getWriter(WriterKind paramWriterKind) {
/* 372 */     switch (paramWriterKind) { case FRAGMENT:
/* 373 */         return this.noticeWriter;
/* 374 */       case NOTE: return this.warnWriter;
/* 375 */       case WARNING: return this.errWriter; }
/* 376 */      throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWriter(WriterKind paramWriterKind, PrintWriter paramPrintWriter) {
/* 381 */     paramPrintWriter.getClass();
/* 382 */     switch (paramWriterKind) { case FRAGMENT:
/* 383 */         this.noticeWriter = paramPrintWriter; return;
/* 384 */       case NOTE: this.warnWriter = paramPrintWriter; return;
/* 385 */       case WARNING: this.errWriter = paramPrintWriter; return; }
/* 386 */      throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWriters(PrintWriter paramPrintWriter) {
/* 391 */     paramPrintWriter.getClass();
/* 392 */     this.noticeWriter = this.warnWriter = this.errWriter = paramPrintWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initRound(Log paramLog) {
/* 399 */     this.noticeWriter = paramLog.noticeWriter;
/* 400 */     this.warnWriter = paramLog.warnWriter;
/* 401 */     this.errWriter = paramLog.errWriter;
/* 402 */     this.sourceMap = paramLog.sourceMap;
/* 403 */     this.recorded = paramLog.recorded;
/* 404 */     this.nerrors = paramLog.nerrors;
/* 405 */     this.nwarnings = paramLog.nwarnings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void popDiagnosticHandler(DiagnosticHandler paramDiagnosticHandler) {
/* 415 */     Assert.check((this.diagnosticHandler == paramDiagnosticHandler));
/* 416 */     this.diagnosticHandler = paramDiagnosticHandler.prev;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {
/* 422 */     this.errWriter.flush();
/* 423 */     this.warnWriter.flush();
/* 424 */     this.noticeWriter.flush();
/*     */   }
/*     */   
/*     */   public void flush(WriterKind paramWriterKind) {
/* 428 */     getWriter(paramWriterKind).flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldReport(JavaFileObject paramJavaFileObject, int paramInt) {
/* 435 */     if (this.multipleErrors || paramJavaFileObject == null) {
/* 436 */       return true;
/*     */     }
/* 438 */     Pair<JavaFileObject, Integer> pair = new Pair<>(paramJavaFileObject, Integer.valueOf(paramInt));
/* 439 */     boolean bool = !this.recorded.contains(pair) ? true : false;
/* 440 */     if (bool)
/* 441 */       this.recorded.add(pair); 
/* 442 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void prompt() {
/* 448 */     if (this.promptOnError) {
/* 449 */       System.err.println(localize("resume.abort", new Object[0]));
/*     */       try {
/*     */         while (true)
/* 452 */         { switch (System.in.read()) { case 65:
/*     */             case 97:
/* 454 */               System.exit(-1); return;
/*     */             case 82: case 114:
/*     */               return;
/*     */             case 88:
/*     */             case 120:
/* 459 */               break; }  }  throw new AssertionError("user abort");
/*     */ 
/*     */       
/*     */       }
/* 463 */       catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printErrLine(int paramInt, PrintWriter paramPrintWriter) {
/* 471 */     String str = (this.source == null) ? null : this.source.getLine(paramInt);
/* 472 */     if (str == null)
/*     */       return; 
/* 474 */     int i = this.source.getColumnNumber(paramInt, false);
/*     */     
/* 476 */     printRawLines(paramPrintWriter, str);
/* 477 */     for (byte b = 0; b < i - 1; b++) {
/* 478 */       paramPrintWriter.print((str.charAt(b) == '\t') ? "\t" : " ");
/*     */     }
/* 480 */     paramPrintWriter.println("^");
/* 481 */     paramPrintWriter.flush();
/*     */   }
/*     */   
/*     */   public void printNewline() {
/* 485 */     this.noticeWriter.println();
/*     */   }
/*     */   
/*     */   public void printNewline(WriterKind paramWriterKind) {
/* 489 */     getWriter(paramWriterKind).println();
/*     */   }
/*     */   
/*     */   public void printLines(String paramString, Object... paramVarArgs) {
/* 493 */     printRawLines(this.noticeWriter, localize(paramString, paramVarArgs));
/*     */   }
/*     */   
/*     */   public void printLines(PrefixKind paramPrefixKind, String paramString, Object... paramVarArgs) {
/* 497 */     printRawLines(this.noticeWriter, localize(paramPrefixKind, paramString, paramVarArgs));
/*     */   }
/*     */   
/*     */   public void printLines(WriterKind paramWriterKind, String paramString, Object... paramVarArgs) {
/* 501 */     printRawLines(getWriter(paramWriterKind), localize(paramString, paramVarArgs));
/*     */   }
/*     */   
/*     */   public void printLines(WriterKind paramWriterKind, PrefixKind paramPrefixKind, String paramString, Object... paramVarArgs) {
/* 505 */     printRawLines(getWriter(paramWriterKind), localize(paramPrefixKind, paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printRawLines(String paramString) {
/* 512 */     printRawLines(this.noticeWriter, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printRawLines(WriterKind paramWriterKind, String paramString) {
/* 519 */     printRawLines(getWriter(paramWriterKind), paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void printRawLines(PrintWriter paramPrintWriter, String paramString) {
/*     */     int i;
/* 527 */     while ((i = paramString.indexOf('\n')) != -1) {
/* 528 */       paramPrintWriter.println(paramString.substring(0, i));
/* 529 */       paramString = paramString.substring(i + 1);
/*     */     } 
/* 531 */     if (paramString.length() != 0) paramPrintWriter.println(paramString);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printVerbose(String paramString, Object... paramVarArgs) {
/* 539 */     printRawLines(this.noticeWriter, localize("verbose." + paramString, paramVarArgs));
/*     */   }
/*     */   
/*     */   protected void directError(String paramString, Object... paramVarArgs) {
/* 543 */     printRawLines(this.errWriter, localize(paramString, paramVarArgs));
/* 544 */     this.errWriter.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void strictWarning(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object... paramVarArgs) {
/* 553 */     writeDiagnostic(this.diags.warning(this.source, paramDiagnosticPosition, paramString, paramVarArgs));
/* 554 */     this.nwarnings++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void report(JCDiagnostic paramJCDiagnostic) {
/* 562 */     this.diagnosticHandler.report(paramJCDiagnostic);
/*     */   }
/*     */ 
/*     */   
/*     */   private class DefaultDiagnosticHandler
/*     */     extends DiagnosticHandler
/*     */   {
/*     */     private DefaultDiagnosticHandler() {}
/*     */     
/*     */     public void report(JCDiagnostic param1JCDiagnostic) {
/* 572 */       if (Log.this.expectDiagKeys != null) {
/* 573 */         Log.this.expectDiagKeys.remove(param1JCDiagnostic.getCode());
/*     */       }
/* 575 */       switch (param1JCDiagnostic.getType()) {
/*     */         case FRAGMENT:
/* 577 */           throw new IllegalArgumentException();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case NOTE:
/* 583 */           if ((Log.this.emitWarnings || param1JCDiagnostic.isMandatory()) && !Log.this.suppressNotes) {
/* 584 */             Log.this.writeDiagnostic(param1JCDiagnostic);
/*     */           }
/*     */           break;
/*     */         
/*     */         case WARNING:
/* 589 */           if ((Log.this.emitWarnings || param1JCDiagnostic.isMandatory()) && 
/* 590 */             Log.this.nwarnings < Log.this.MaxWarnings) {
/* 591 */             Log.this.writeDiagnostic(param1JCDiagnostic);
/* 592 */             Log.this.nwarnings++;
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case ERROR:
/* 598 */           if (Log.this.nerrors < Log.this.MaxErrors && Log.this
/* 599 */             .shouldReport(param1JCDiagnostic.getSource(), param1JCDiagnostic.getIntPosition())) {
/* 600 */             Log.this.writeDiagnostic(param1JCDiagnostic);
/* 601 */             Log.this.nerrors++;
/*     */           } 
/*     */           break;
/*     */       } 
/* 605 */       if (param1JCDiagnostic.isFlagSet(JCDiagnostic.DiagnosticFlag.COMPRESSED)) {
/* 606 */         Log.this.compressedOutput = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeDiagnostic(JCDiagnostic paramJCDiagnostic) {
/* 615 */     if (this.diagListener != null) {
/* 616 */       this.diagListener.report(paramJCDiagnostic);
/*     */       
/*     */       return;
/*     */     } 
/* 620 */     PrintWriter printWriter = getWriterForDiagnosticType(paramJCDiagnostic.getType());
/*     */     
/* 622 */     printRawLines(printWriter, this.diagFormatter.format(paramJCDiagnostic, this.messages.getCurrentLocale()));
/*     */     
/* 624 */     if (this.promptOnError) {
/* 625 */       switch (paramJCDiagnostic.getType()) {
/*     */         case WARNING:
/*     */         case ERROR:
/* 628 */           prompt();
/*     */           break;
/*     */       } 
/*     */     }
/* 632 */     if (this.dumpOnError) {
/* 633 */       (new RuntimeException()).printStackTrace(printWriter);
/*     */     }
/* 635 */     printWriter.flush();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected PrintWriter getWriterForDiagnosticType(JCDiagnostic.DiagnosticType paramDiagnosticType) {
/* 640 */     switch (paramDiagnosticType) {
/*     */       case FRAGMENT:
/* 642 */         throw new IllegalArgumentException();
/*     */       
/*     */       case NOTE:
/* 645 */         return this.noticeWriter;
/*     */       
/*     */       case WARNING:
/* 648 */         return this.warnWriter;
/*     */       
/*     */       case ERROR:
/* 651 */         return this.errWriter;
/*     */     } 
/*     */     
/* 654 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getLocalizedString(String paramString, Object... paramVarArgs) {
/* 665 */     return JavacMessages.getDefaultLocalizedString(PrefixKind.COMPILER_MISC.key(paramString), paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String localize(String paramString, Object... paramVarArgs) {
/* 673 */     return localize(PrefixKind.COMPILER_MISC, paramString, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String localize(PrefixKind paramPrefixKind, String paramString, Object... paramVarArgs) {
/* 681 */     if (useRawMessages) {
/* 682 */       return paramPrefixKind.key(paramString);
/*     */     }
/* 684 */     return this.messages.getLocalizedString(paramPrefixKind.key(paramString), paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean useRawMessages = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printRawError(int paramInt, String paramString) {
/* 698 */     if (this.source == null || paramInt == -1) {
/* 699 */       printRawLines(this.errWriter, "error: " + paramString);
/*     */     } else {
/* 701 */       int i = this.source.getLineNumber(paramInt);
/* 702 */       JavaFileObject javaFileObject = this.source.getFile();
/* 703 */       if (javaFileObject != null) {
/* 704 */         printRawLines(this.errWriter, javaFileObject
/* 705 */             .getName() + ":" + i + ": " + paramString);
/*     */       }
/* 707 */       printErrLine(paramInt, this.errWriter);
/*     */     } 
/* 709 */     this.errWriter.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rawError(int paramInt, String paramString) {
/* 715 */     if (this.nerrors < this.MaxErrors && shouldReport(currentSourceFile(), paramInt)) {
/* 716 */       printRawError(paramInt, paramString);
/* 717 */       prompt();
/* 718 */       this.nerrors++;
/*     */     } 
/* 720 */     this.errWriter.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rawWarning(int paramInt, String paramString) {
/* 726 */     if (this.nwarnings < this.MaxWarnings && this.emitWarnings) {
/* 727 */       printRawError(paramInt, "warning: " + paramString);
/*     */     }
/* 729 */     prompt();
/* 730 */     this.nwarnings++;
/* 731 */     this.errWriter.flush();
/*     */   }
/*     */   
/*     */   public static String format(String paramString, Object... paramVarArgs) {
/* 735 */     return String.format((Locale)null, paramString, paramVarArgs);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Log.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */