/*      */ package com.sun.tools.javap;
/*      */
/*      */ import com.sun.tools.classfile.Attribute;
/*      */ import com.sun.tools.classfile.Attributes;
/*      */ import com.sun.tools.classfile.ClassFile;
/*      */ import com.sun.tools.classfile.ConstantPool;
/*      */ import com.sun.tools.classfile.ConstantPoolException;
/*      */ import com.sun.tools.classfile.Field;
/*      */ import com.sun.tools.classfile.InnerClasses_attribute;
/*      */ import com.sun.tools.classfile.Method;
/*      */ import java.io.EOFException;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.security.DigestInputStream;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.lang.model.element.Modifier;
/*      */ import javax.lang.model.element.NestingKind;
/*      */ import javax.tools.Diagnostic;
/*      */ import javax.tools.DiagnosticListener;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
/*      */ import javax.tools.StandardJavaFileManager;
/*      */ import javax.tools.StandardLocation;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class JavapTask
/*      */   implements DisassemblerTool.DisassemblerTask, Messages
/*      */ {
/*      */   public class BadArgs
/*      */     extends Exception
/*      */   {
/*      */     static final long serialVersionUID = 8765093759964640721L;
/*      */     final String key;
/*      */     final Object[] args;
/*      */     boolean showUsage;
/*      */
/*      */     BadArgs(String param1String, Object... param1VarArgs) {
/*   81 */       super(JavapTask.this.getMessage(param1String, param1VarArgs));
/*   82 */       this.key = param1String;
/*   83 */       this.args = param1VarArgs;
/*      */     }
/*      */
/*      */     BadArgs showUsage(boolean param1Boolean) {
/*   87 */       this.showUsage = param1Boolean;
/*   88 */       return this;
/*      */     }
/*      */   }
/*      */
/*      */   static abstract class Option
/*      */   {
/*      */     final boolean hasArg;
/*      */     final String[] aliases;
/*      */
/*      */     Option(boolean param1Boolean, String... param1VarArgs) {
/*   98 */       this.hasArg = param1Boolean;
/*   99 */       this.aliases = param1VarArgs;
/*      */     }
/*      */
/*      */     boolean matches(String param1String) {
/*  103 */       for (String str : this.aliases) {
/*  104 */         if (str.equals(param1String))
/*  105 */           return true;
/*      */       }
/*  107 */       return false;
/*      */     }
/*      */
/*      */     boolean ignoreRest() {
/*  111 */       return false;
/*      */     }
/*      */
/*      */
/*      */
/*      */     abstract void process(JavapTask param1JavapTask, String param1String1, String param1String2) throws BadArgs;
/*      */   }
/*      */
/*      */
/*  120 */   static final Option[] recognizedOptions = new Option[] { new Option(false, new String[] { "-help", "--help", "-?" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  124 */           param1JavapTask.options.help = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-version" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  130 */           param1JavapTask.options.version = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-fullversion" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  136 */           param1JavapTask.options.fullVersion = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-v", "-verbose", "-all" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  142 */           param1JavapTask.options.verbose = true;
/*  143 */           param1JavapTask.options.showDescriptors = true;
/*  144 */           param1JavapTask.options.showFlags = true;
/*  145 */           param1JavapTask.options.showAllAttrs = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-l" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  151 */           param1JavapTask.options.showLineAndLocalVariableTables = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-public" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  157 */           param1JavapTask.options.accessOptions.add(param1String1);
/*  158 */           param1JavapTask.options.showAccess = 1;
/*      */         }
/*      */       }, new Option(false, new String[] { "-protected" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  164 */           param1JavapTask.options.accessOptions.add(param1String1);
/*  165 */           param1JavapTask.options.showAccess = 4;
/*      */         }
/*      */       }, new Option(false, new String[] { "-package" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  171 */           param1JavapTask.options.accessOptions.add(param1String1);
/*  172 */           param1JavapTask.options.showAccess = 0;
/*      */         }
/*      */       }, new Option(false, new String[] { "-p", "-private" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  178 */           if (!param1JavapTask.options.accessOptions.contains("-p") &&
/*  179 */             !param1JavapTask.options.accessOptions.contains("-private")) {
/*  180 */             param1JavapTask.options.accessOptions.add(param1String1);
/*      */           }
/*  182 */           param1JavapTask.options.showAccess = 2;
/*      */         }
/*      */       }, new Option(false, new String[] { "-c" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  188 */           param1JavapTask.options.showDisassembled = true;
/*      */         }
/*      */       },
/*      */       new Option(false, new String[] { "-s" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2) {
/*  194 */           param1JavapTask.options.showDescriptors = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-sysinfo" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  200 */           param1JavapTask.options.sysInfo = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-XDdetails" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  206 */           param1JavapTask.options.details = EnumSet.allOf(InstructionDetailWriter.Kind.class);
/*      */         }
/*      */       }, new Option(false, new String[] { "-XDdetails:" })
/*      */       {
/*      */
/*      */
/*      */         boolean matches(String param1String)
/*      */         {
/*  214 */           int i = param1String.indexOf(":");
/*  215 */           return (i != -1 && super.matches(param1String.substring(0, i + 1)));
/*      */         }
/*      */
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2) throws BadArgs {
/*  219 */           int i = param1String1.indexOf(":");
/*  220 */           for (String str : param1String1.substring(i + 1).split("[,: ]+")) {
/*  221 */             if (!handleArg(param1JavapTask, str)) {
/*  222 */               param1JavapTask.getClass(); throw new BadArgs("err.invalid.arg.for.option", new Object[] { str });
/*      */             }
/*      */           }
/*      */         }
/*      */         boolean handleArg(JavapTask param1JavapTask, String param1String) {
/*  227 */           if (param1String.length() == 0) {
/*  228 */             return true;
/*      */           }
/*  230 */           if (param1String.equals("all")) {
/*  231 */             param1JavapTask.options.details = EnumSet.allOf(InstructionDetailWriter.Kind.class);
/*  232 */             return true;
/*      */           }
/*      */
/*  235 */           boolean bool = true;
/*  236 */           if (param1String.startsWith("-")) {
/*  237 */             bool = false;
/*  238 */             param1String = param1String.substring(1);
/*      */           }
/*      */
/*  241 */           for (InstructionDetailWriter.Kind kind : InstructionDetailWriter.Kind.values()) {
/*  242 */             if (param1String.equalsIgnoreCase(kind.option)) {
/*  243 */               if (bool) {
/*  244 */                 param1JavapTask.options.details.add(kind);
/*      */               } else {
/*  246 */                 param1JavapTask.options.details.remove(kind);
/*  247 */               }  return true;
/*      */             }
/*      */           }
/*  250 */           return false;
/*      */         }
/*      */       }, new Option(false, new String[] { "-constants" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  256 */           param1JavapTask.options.showConstants = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-XDinner" })
/*      */       {
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2)
/*      */         {
/*  262 */           param1JavapTask.options.showInnerClasses = true;
/*      */         }
/*      */       }, new Option(false, new String[] { "-XDindent:" })
/*      */       {
/*      */
/*      */         boolean matches(String param1String)
/*      */         {
/*  269 */           int i = param1String.indexOf(":");
/*  270 */           return (i != -1 && super.matches(param1String.substring(0, i + 1)));
/*      */         }
/*      */
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2) throws BadArgs {
/*  274 */           int i = param1String1.indexOf(":");
/*      */           try {
/*  276 */             int j = Integer.valueOf(param1String1.substring(i + 1)).intValue();
/*  277 */             if (j > 0)
/*  278 */               param1JavapTask.options.indentWidth = j;
/*  279 */           } catch (NumberFormatException numberFormatException) {}
/*      */         }
/*      */       }, new Option(false, new String[] { "-XDtab:" })
/*      */       {
/*      */
/*      */
/*      */         boolean matches(String param1String)
/*      */         {
/*  287 */           int i = param1String.indexOf(":");
/*  288 */           return (i != -1 && super.matches(param1String.substring(0, i + 1)));
/*      */         }
/*      */
/*      */         void process(JavapTask param1JavapTask, String param1String1, String param1String2) throws BadArgs {
/*  292 */           int i = param1String1.indexOf(":");
/*      */           try {
/*  294 */             int j = Integer.valueOf(param1String1.substring(i + 1)).intValue();
/*  295 */             if (j > 0)
/*  296 */               param1JavapTask.options.tabColumn = j;
/*  297 */           } catch (NumberFormatException numberFormatException) {}
/*      */         }
/*      */       } };
/*      */   static final int EXIT_OK = 0; static final int EXIT_ERROR = 1; static final int EXIT_CMDERR = 2;
/*      */   static final int EXIT_SYSERR = 3;
/*      */   static final int EXIT_ABNORMAL = 4;
/*      */
/*      */   public JavapTask() {
/*  305 */     this.context = new Context();
/*  306 */     this.context.put(Messages.class, this);
/*  307 */     this.options = Options.instance(this.context);
/*  308 */     this.attributeFactory = new Attribute.Factory();
/*      */   }
/*      */
/*      */
/*      */
/*      */   public JavapTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener) {
/*  314 */     this();
/*  315 */     this.log = getPrintWriterForWriter(paramWriter);
/*  316 */     this.fileManager = paramJavaFileManager;
/*  317 */     this.diagnosticListener = paramDiagnosticListener;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public JavapTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2) {
/*  325 */     this(paramWriter, paramJavaFileManager, paramDiagnosticListener);
/*      */
/*  327 */     this.classes = new ArrayList<>();
/*  328 */     for (String str : paramIterable2) {
/*  329 */       str.getClass();
/*  330 */       this.classes.add(str);
/*      */     }
/*      */
/*      */     try {
/*  334 */       if (paramIterable1 != null)
/*  335 */         handleOptions(paramIterable1, false);
/*  336 */     } catch (BadArgs badArgs) {
/*  337 */       throw new IllegalArgumentException(badArgs.getMessage());
/*      */     }
/*      */   }
/*      */
/*      */   public void setLocale(Locale paramLocale) {
/*  342 */     if (paramLocale == null)
/*  343 */       paramLocale = Locale.getDefault();
/*  344 */     this.task_locale = paramLocale;
/*      */   }
/*      */
/*      */   public void setLog(Writer paramWriter) {
/*  348 */     this.log = getPrintWriterForWriter(paramWriter);
/*      */   }
/*      */
/*      */   public void setLog(OutputStream paramOutputStream) {
/*  352 */     setLog(getPrintWriterForStream(paramOutputStream));
/*      */   }
/*      */
/*      */   private static PrintWriter getPrintWriterForStream(OutputStream paramOutputStream) {
/*  356 */     return new PrintWriter((paramOutputStream == null) ? System.err : paramOutputStream, true);
/*      */   }
/*      */
/*      */   private static PrintWriter getPrintWriterForWriter(Writer paramWriter) {
/*  360 */     if (paramWriter == null)
/*  361 */       return getPrintWriterForStream(null);
/*  362 */     if (paramWriter instanceof PrintWriter) {
/*  363 */       return (PrintWriter)paramWriter;
/*      */     }
/*  365 */     return new PrintWriter(paramWriter, true);
/*      */   }
/*      */
/*      */   public void setDiagnosticListener(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener) {
/*  369 */     this.diagnosticListener = paramDiagnosticListener;
/*      */   }
/*      */
/*      */   public void setDiagnosticListener(OutputStream paramOutputStream) {
/*  373 */     setDiagnosticListener(getDiagnosticListenerForStream(paramOutputStream));
/*      */   }
/*      */
/*      */   private DiagnosticListener<JavaFileObject> getDiagnosticListenerForStream(OutputStream paramOutputStream) {
/*  377 */     return getDiagnosticListenerForWriter(getPrintWriterForStream(paramOutputStream));
/*      */   }
/*      */
/*      */   private DiagnosticListener<JavaFileObject> getDiagnosticListenerForWriter(Writer paramWriter) {
/*  381 */     final PrintWriter pw = getPrintWriterForWriter(paramWriter);
/*  382 */     return new DiagnosticListener<JavaFileObject>()
/*      */       {
/*      */         public void report(Diagnostic<? extends JavaFileObject> param1Diagnostic) {
/*      */           // Byte code:
/*      */           //   0: getstatic com/sun/tools/javap/JavapTask$22.$SwitchMap$javax$tools$Diagnostic$Kind : [I
/*      */           //   3: aload_1
/*      */           //   4: invokeinterface getKind : ()Ljavax/tools/Diagnostic$Kind;
/*      */           //   9: invokevirtual ordinal : ()I
/*      */           //   12: iaload
/*      */           //   13: tableswitch default -> 106, 1 -> 40, 2 -> 63, 3 -> 86
/*      */           //   40: aload_0
/*      */           //   41: getfield val$pw : Ljava/io/PrintWriter;
/*      */           //   44: aload_0
/*      */           //   45: getfield this$0 : Lcom/sun/tools/javap/JavapTask;
/*      */           //   48: ldc 'err.prefix'
/*      */           //   50: iconst_0
/*      */           //   51: anewarray java/lang/Object
/*      */           //   54: invokevirtual getMessage : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */           //   57: invokevirtual print : (Ljava/lang/String;)V
/*      */           //   60: goto -> 106
/*      */           //   63: aload_0
/*      */           //   64: getfield val$pw : Ljava/io/PrintWriter;
/*      */           //   67: aload_0
/*      */           //   68: getfield this$0 : Lcom/sun/tools/javap/JavapTask;
/*      */           //   71: ldc 'warn.prefix'
/*      */           //   73: iconst_0
/*      */           //   74: anewarray java/lang/Object
/*      */           //   77: invokevirtual getMessage : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */           //   80: invokevirtual print : (Ljava/lang/String;)V
/*      */           //   83: goto -> 106
/*      */           //   86: aload_0
/*      */           //   87: getfield val$pw : Ljava/io/PrintWriter;
/*      */           //   90: aload_0
/*      */           //   91: getfield this$0 : Lcom/sun/tools/javap/JavapTask;
/*      */           //   94: ldc 'note.prefix'
/*      */           //   96: iconst_0
/*      */           //   97: anewarray java/lang/Object
/*      */           //   100: invokevirtual getMessage : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*      */           //   103: invokevirtual print : (Ljava/lang/String;)V
/*      */           //   106: aload_0
/*      */           //   107: getfield val$pw : Ljava/io/PrintWriter;
/*      */           //   110: ldc ' '
/*      */           //   112: invokevirtual print : (Ljava/lang/String;)V
/*      */           //   115: aload_0
/*      */           //   116: getfield val$pw : Ljava/io/PrintWriter;
/*      */           //   119: aload_1
/*      */           //   120: aconst_null
/*      */           //   121: invokeinterface getMessage : (Ljava/util/Locale;)Ljava/lang/String;
/*      */           //   126: invokevirtual println : (Ljava/lang/String;)V
/*      */           //   129: return
/*      */           // Line number table:
/*      */           //   Java source line number -> byte code offset
/*      */           //   #384	-> 0
/*      */           //   #386	-> 40
/*      */           //   #387	-> 60
/*      */           //   #389	-> 63
/*      */           //   #390	-> 83
/*      */           //   #392	-> 86
/*      */           //   #395	-> 106
/*      */           //   #396	-> 115
/*      */           //   #397	-> 129
/*      */         }
/*      */       };
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
/*      */   int run(String[] paramArrayOfString) {
/*      */     try {
/*  412 */       handleOptions(paramArrayOfString);
/*      */
/*      */
/*  415 */       if (this.classes == null || this.classes.size() == 0) {
/*  416 */         if (this.options.help || this.options.version || this.options.fullVersion) {
/*  417 */           return 0;
/*      */         }
/*  419 */         return 2;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     }
/*  434 */     catch (BadArgs badArgs) {
/*  435 */       reportError(badArgs.key, badArgs.args);
/*  436 */       if (badArgs.showUsage) {
/*  437 */         printLines(getMessage("main.usage.summary", new Object[] { "javap" }));
/*      */       }
/*  439 */       return 2;
/*  440 */     } catch (InternalError internalError) {
/*      */       Object[] arrayOfObject;
/*  442 */       if (internalError.getCause() == null) {
/*  443 */         arrayOfObject = internalError.args;
/*      */       } else {
/*  445 */         arrayOfObject = new Object[internalError.args.length + 1];
/*  446 */         arrayOfObject[0] = internalError.getCause();
/*  447 */         System.arraycopy(internalError.args, 0, arrayOfObject, 1, internalError.args.length);
/*      */       }
/*  449 */       reportError("err.internal.error", arrayOfObject);
/*  450 */       return 4;
/*      */     } finally {
/*  452 */       this.log.flush();
/*      */     }
/*      */   }
/*      */
/*      */   public void handleOptions(String[] paramArrayOfString) throws BadArgs {
/*  457 */     handleOptions(Arrays.asList(paramArrayOfString), true);
/*      */   }
/*      */
/*      */   private void handleOptions(Iterable<String> paramIterable, boolean paramBoolean) throws BadArgs {
/*  461 */     if (this.log == null) {
/*  462 */       this.log = getPrintWriterForStream(System.out);
/*  463 */       if (this.diagnosticListener == null) {
/*  464 */         this.diagnosticListener = getDiagnosticListenerForStream(System.err);
/*      */       }
/*  466 */     } else if (this.diagnosticListener == null) {
/*  467 */       this.diagnosticListener = getDiagnosticListenerForWriter(this.log);
/*      */     }
/*      */
/*      */
/*  471 */     if (this.fileManager == null) {
/*  472 */       this.fileManager = getDefaultFileManager(this.diagnosticListener, this.log);
/*      */     }
/*  474 */     Iterator<String> iterator = paramIterable.iterator();
/*  475 */     boolean bool = !iterator.hasNext() ? true : false;
/*      */
/*  477 */     while (iterator.hasNext()) {
/*  478 */       String str = iterator.next();
/*  479 */       if (str.startsWith("-")) {
/*  480 */         handleOption(str, iterator); continue;
/*  481 */       }  if (paramBoolean) {
/*  482 */         if (this.classes == null)
/*  483 */           this.classes = new ArrayList<>();
/*  484 */         this.classes.add(str);
/*  485 */         while (iterator.hasNext())
/*  486 */           this.classes.add(iterator.next());  continue;
/*      */       }
/*  488 */       throw (new BadArgs("err.unknown.option", new Object[] { str })).showUsage(true);
/*      */     }
/*      */
/*  491 */     if (this.options.accessOptions.size() > 1) {
/*  492 */       StringBuilder stringBuilder = new StringBuilder();
/*  493 */       for (String str : this.options.accessOptions) {
/*  494 */         if (stringBuilder.length() > 0)
/*  495 */           stringBuilder.append(" ");
/*  496 */         stringBuilder.append(str);
/*      */       }
/*  498 */       throw new BadArgs("err.incompatible.options", new Object[] { stringBuilder });
/*      */     }
/*      */
/*  501 */     if ((this.classes == null || this.classes.size() == 0) && !bool && !this.options.help && !this.options.version && !this.options.fullVersion)
/*      */     {
/*  503 */       throw new BadArgs("err.no.classes.specified", new Object[0]);
/*      */     }
/*      */
/*  506 */     if (bool || this.options.help) {
/*  507 */       showHelp();
/*      */     }
/*  509 */     if (this.options.version || this.options.fullVersion)
/*  510 */       showVersion(this.options.fullVersion);
/*      */   }
/*      */
/*      */   private void handleOption(String paramString, Iterator<String> paramIterator) throws BadArgs {
/*  514 */     for (Option option : recognizedOptions) {
/*  515 */       if (option.matches(paramString)) {
/*  516 */         if (option.hasArg)
/*  517 */         { if (paramIterator.hasNext()) {
/*  518 */             option.process(this, paramString, paramIterator.next());
/*      */           } else {
/*  520 */             throw (new BadArgs("err.missing.arg", new Object[] { paramString })).showUsage(true);
/*      */           }  }
/*  522 */         else { option.process(this, paramString, null); }
/*      */
/*  524 */         if (option.ignoreRest()) {
/*  525 */           while (paramIterator.hasNext()) {
/*  526 */             paramIterator.next();
/*      */           }
/*      */         }
/*      */         return;
/*      */       }
/*      */     }
/*      */     try {
/*  533 */       if (this.fileManager.handleOption(paramString, paramIterator))
/*      */         return;
/*  535 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  536 */       throw (new BadArgs("err.invalid.use.of.option", new Object[] { paramString })).showUsage(true);
/*      */     }
/*      */
/*  539 */     throw (new BadArgs("err.unknown.option", new Object[] { paramString })).showUsage(true);
/*      */   }
/*      */
/*      */   public Boolean call() {
/*  543 */     return Boolean.valueOf((run() == 0));
/*      */   }
/*      */
/*      */   public int run() {
/*  547 */     if (this.classes == null || this.classes.isEmpty()) {
/*  548 */       return 1;
/*      */     }
/*      */
/*  551 */     this.context.put(PrintWriter.class, this.log);
/*  552 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  553 */     SourceWriter sourceWriter = SourceWriter.instance(this.context);
/*  554 */     sourceWriter.setFileManager(this.fileManager);
/*      */
/*  556 */     int i = 0;
/*      */
/*  558 */     for (String str : this.classes) {
/*      */       try {
/*  560 */         i = writeClass(classWriter, str);
/*  561 */       } catch (ConstantPoolException constantPoolException) {
/*  562 */         reportError("err.bad.constant.pool", new Object[] { str, constantPoolException.getLocalizedMessage() });
/*  563 */         i = 1;
/*  564 */       } catch (EOFException eOFException) {
/*  565 */         reportError("err.end.of.file", new Object[] { str });
/*  566 */         i = 1;
/*  567 */       } catch (FileNotFoundException fileNotFoundException) {
/*  568 */         reportError("err.file.not.found", new Object[] { fileNotFoundException.getLocalizedMessage() });
/*  569 */         i = 1;
/*  570 */       } catch (IOException iOException1) {
/*      */         IOException iOException2;
/*  572 */         String str1 = iOException1.getLocalizedMessage();
/*  573 */         if (str1 == null) {
/*  574 */           iOException2 = iOException1;
/*      */         }
/*  576 */         reportError("err.ioerror", new Object[] { str, iOException2 });
/*  577 */         i = 1;
/*  578 */       } catch (Throwable throwable) {
/*  579 */         StringWriter stringWriter = new StringWriter();
/*  580 */         PrintWriter printWriter = new PrintWriter(stringWriter);
/*  581 */         throwable.printStackTrace(printWriter);
/*  582 */         printWriter.close();
/*  583 */         reportError("err.crash", new Object[] { throwable.toString(), stringWriter.toString() });
/*  584 */         i = 4;
/*      */       }
/*      */     }
/*      */
/*  588 */     return i;
/*      */   }
/*      */
/*      */
/*      */   protected int writeClass(ClassWriter paramClassWriter, String paramString) throws IOException, ConstantPoolException {
/*  593 */     JavaFileObject javaFileObject = open(paramString);
/*  594 */     if (javaFileObject == null) {
/*  595 */       reportError("err.class.not.found", new Object[] { paramString });
/*  596 */       return 1;
/*      */     }
/*      */
/*  599 */     ClassFileInfo classFileInfo = read(javaFileObject);
/*  600 */     if (!paramString.endsWith(".class")) {
/*  601 */       String str = classFileInfo.cf.getName();
/*  602 */       if (!str.replaceAll("[/$]", ".").equals(paramString.replaceAll("[/$]", "."))) {
/*  603 */         reportWarning("warn.unexpected.class", new Object[] { paramString, str.replace('/', '.') });
/*      */       }
/*      */     }
/*  606 */     write(classFileInfo);
/*      */
/*  608 */     if (this.options.showInnerClasses) {
/*  609 */       ClassFile classFile = classFileInfo.cf;
/*  610 */       Attribute attribute = classFile.getAttribute("InnerClasses");
/*  611 */       if (attribute instanceof InnerClasses_attribute) {
/*  612 */         InnerClasses_attribute innerClasses_attribute = (InnerClasses_attribute)attribute;
/*      */         try {
/*  614 */           int i = 0;
/*  615 */           for (byte b = 0; b < innerClasses_attribute.classes.length; b++) {
/*  616 */             int j = (innerClasses_attribute.classes[b]).outer_class_info_index;
/*  617 */             ConstantPool.CONSTANT_Class_info cONSTANT_Class_info = classFile.constant_pool.getClassInfo(j);
/*  618 */             String str = cONSTANT_Class_info.getName();
/*  619 */             if (str.equals(classFile.getName())) {
/*  620 */               int k = (innerClasses_attribute.classes[b]).inner_class_info_index;
/*  621 */               ConstantPool.CONSTANT_Class_info cONSTANT_Class_info1 = classFile.constant_pool.getClassInfo(k);
/*  622 */               String str1 = cONSTANT_Class_info1.getName();
/*  623 */               paramClassWriter.println("// inner class " + str1.replaceAll("[/$]", "."));
/*  624 */               paramClassWriter.println();
/*  625 */               i = writeClass(paramClassWriter, str1);
/*  626 */               if (i != 0) return i;
/*      */             }
/*      */           }
/*  629 */           return i;
/*  630 */         } catch (ConstantPoolException constantPoolException) {
/*  631 */           reportError("err.bad.innerclasses.attribute", new Object[] { paramString });
/*  632 */           return 1;
/*      */         }
/*  634 */       }  if (attribute != null) {
/*  635 */         reportError("err.bad.innerclasses.attribute", new Object[] { paramString });
/*  636 */         return 1;
/*      */       }
/*      */     }
/*      */
/*  640 */     return 0;
/*      */   }
/*      */
/*      */
/*      */   protected JavaFileObject open(String paramString) throws IOException {
/*  645 */     JavaFileObject javaFileObject = getClassFileObject(paramString);
/*  646 */     if (javaFileObject != null) {
/*  647 */       return javaFileObject;
/*      */     }
/*      */
/*  650 */     String str = paramString;
/*      */     int i;
/*  652 */     while ((i = str.lastIndexOf(".")) != -1) {
/*  653 */       str = str.substring(0, i) + "$" + str.substring(i + 1);
/*  654 */       javaFileObject = getClassFileObject(str);
/*  655 */       if (javaFileObject != null) {
/*  656 */         return javaFileObject;
/*      */       }
/*      */     }
/*  659 */     if (!paramString.endsWith(".class")) {
/*  660 */       return null;
/*      */     }
/*  662 */     if (this.fileManager instanceof StandardJavaFileManager) {
/*  663 */       StandardJavaFileManager standardJavaFileManager = (StandardJavaFileManager)this.fileManager;
/*  664 */       javaFileObject = standardJavaFileManager.getJavaFileObjects(new String[] { paramString }).iterator().next();
/*  665 */       if (javaFileObject != null && javaFileObject.getLastModified() != 0L) {
/*  666 */         return javaFileObject;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*  672 */     if (paramString.matches("^[A-Za-z]+:.*")) {
/*      */
/*  674 */       try { final URI uri = new URI(paramString);
/*  675 */         final URL url = uRI.toURL();
/*  676 */         final URLConnection conn = uRL.openConnection();
/*  677 */         return new JavaFileObject() {
/*      */             public Kind getKind() {
/*  679 */               return Kind.CLASS;
/*      */             }
/*      */
/*      */             public boolean isNameCompatible(String param1String, Kind param1Kind) {
/*  683 */               throw new UnsupportedOperationException();
/*      */             }
/*      */
/*      */             public NestingKind getNestingKind() {
/*  687 */               throw new UnsupportedOperationException();
/*      */             }
/*      */
/*      */             public Modifier getAccessLevel() {
/*  691 */               throw new UnsupportedOperationException();
/*      */             }
/*      */
/*      */             public URI toUri() {
/*  695 */               return uri;
/*      */             }
/*      */
/*      */             public String getName() {
/*  699 */               return url.toString();
/*      */             }
/*      */
/*      */             public InputStream openInputStream() throws IOException {
/*  703 */               return conn.getInputStream();
/*      */             }
/*      */
/*      */             public OutputStream openOutputStream() throws IOException {
/*  707 */               throw new UnsupportedOperationException();
/*      */             }
/*      */
/*      */             public Reader openReader(boolean param1Boolean) throws IOException {
/*  711 */               throw new UnsupportedOperationException();
/*      */             }
/*      */
/*      */             public CharSequence getCharContent(boolean param1Boolean) throws IOException {
/*  715 */               throw new UnsupportedOperationException();
/*      */             }
/*      */
/*      */             public Writer openWriter() throws IOException {
/*  719 */               throw new UnsupportedOperationException();
/*      */             }
/*      */
/*      */             public long getLastModified() {
/*  723 */               return conn.getLastModified();
/*      */             }
/*      */
/*      */             public boolean delete() {
/*  727 */               throw new UnsupportedOperationException();
/*      */             }
/*      */           }; }
/*      */
/*  731 */       catch (URISyntaxException uRISyntaxException) {  }
/*  732 */       catch (IOException iOException) {}
/*      */     }
/*      */
/*      */
/*  736 */     return null;
/*      */   }
/*      */   public static class ClassFileInfo { public final JavaFileObject fo; public final ClassFile cf; public final byte[] digest; public final int size;
/*      */
/*      */     ClassFileInfo(JavaFileObject param1JavaFileObject, ClassFile param1ClassFile, byte[] param1ArrayOfbyte, int param1Int) {
/*  741 */       this.fo = param1JavaFileObject;
/*  742 */       this.cf = param1ClassFile;
/*  743 */       this.digest = param1ArrayOfbyte;
/*  744 */       this.size = param1Int;
/*      */     } }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public ClassFileInfo read(JavaFileObject paramJavaFileObject) throws IOException, ConstantPoolException {
/*  753 */     InputStream inputStream = paramJavaFileObject.openInputStream();
/*      */     try {
/*  755 */       SizeInputStream sizeInputStream = null;
/*  756 */       MessageDigest messageDigest = null;
/*  757 */       if (this.options.sysInfo || this.options.verbose) {
/*      */         try {
/*  759 */           messageDigest = MessageDigest.getInstance("MD5");
/*  760 */         } catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
/*      */
/*  762 */         inputStream = new DigestInputStream(inputStream, messageDigest);
/*  763 */         inputStream = sizeInputStream = new SizeInputStream(inputStream);
/*      */       }
/*      */
/*  766 */       ClassFile classFile = ClassFile.read(inputStream, this.attributeFactory);
/*  767 */       byte[] arrayOfByte = (messageDigest == null) ? null : messageDigest.digest();
/*  768 */       boolean bool = (sizeInputStream == null) ? true : sizeInputStream.size();
/*  769 */       return new ClassFileInfo(paramJavaFileObject, classFile, arrayOfByte, bool);
/*      */     } finally {
/*  771 */       inputStream.close();
/*      */     }
/*      */   }
/*      */
/*      */   public void write(ClassFileInfo paramClassFileInfo) {
/*  776 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  777 */     if (this.options.sysInfo || this.options.verbose) {
/*  778 */       classWriter.setFile(paramClassFileInfo.fo.toUri());
/*  779 */       classWriter.setLastModified(paramClassFileInfo.fo.getLastModified());
/*  780 */       classWriter.setDigest("MD5", paramClassFileInfo.digest);
/*  781 */       classWriter.setFileSize(paramClassFileInfo.size);
/*      */     }
/*      */
/*  784 */     classWriter.write(paramClassFileInfo.cf);
/*      */   }
/*      */
/*      */   protected void setClassFile(ClassFile paramClassFile) {
/*  788 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  789 */     classWriter.setClassFile(paramClassFile);
/*      */   }
/*      */
/*      */   protected void setMethod(Method paramMethod) {
/*  793 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  794 */     classWriter.setMethod(paramMethod);
/*      */   }
/*      */
/*      */   protected void write(Attribute paramAttribute) {
/*  798 */     AttributeWriter attributeWriter = AttributeWriter.instance(this.context);
/*  799 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  800 */     ClassFile classFile = classWriter.getClassFile();
/*  801 */     attributeWriter.write(classFile, paramAttribute, classFile.constant_pool);
/*      */   }
/*      */
/*      */   protected void write(Attributes paramAttributes) {
/*  805 */     AttributeWriter attributeWriter = AttributeWriter.instance(this.context);
/*  806 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  807 */     ClassFile classFile = classWriter.getClassFile();
/*  808 */     attributeWriter.write(classFile, paramAttributes, classFile.constant_pool);
/*      */   }
/*      */
/*      */   protected void write(ConstantPool paramConstantPool) {
/*  812 */     ConstantWriter constantWriter = ConstantWriter.instance(this.context);
/*  813 */     constantWriter.writeConstantPool(paramConstantPool);
/*      */   }
/*      */
/*      */   protected void write(ConstantPool paramConstantPool, int paramInt) {
/*  817 */     ConstantWriter constantWriter = ConstantWriter.instance(this.context);
/*  818 */     constantWriter.write(paramInt);
/*      */   }
/*      */
/*      */   protected void write(ConstantPool.CPInfo paramCPInfo) {
/*  822 */     ConstantWriter constantWriter = ConstantWriter.instance(this.context);
/*  823 */     constantWriter.println(paramCPInfo);
/*      */   }
/*      */
/*      */   protected void write(Field paramField) {
/*  827 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  828 */     classWriter.writeField(paramField);
/*      */   }
/*      */
/*      */   protected void write(Method paramMethod) {
/*  832 */     ClassWriter classWriter = ClassWriter.instance(this.context);
/*  833 */     classWriter.writeMethod(paramMethod);
/*      */   }
/*      */
/*      */   private JavaFileManager getDefaultFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, PrintWriter paramPrintWriter) {
/*  837 */     if (this.defaultFileManager == null)
/*  838 */       this.defaultFileManager = (JavaFileManager)JavapFileManager.create(paramDiagnosticListener, paramPrintWriter);
/*  839 */     return this.defaultFileManager;
/*      */   }
/*      */
/*      */
/*      */   private JavaFileObject getClassFileObject(String paramString) throws IOException {
/*  844 */     JavaFileObject javaFileObject = this.fileManager.getJavaFileForInput(StandardLocation.PLATFORM_CLASS_PATH, paramString, JavaFileObject.Kind.CLASS);
/*  845 */     if (javaFileObject == null)
/*  846 */       javaFileObject = this.fileManager.getJavaFileForInput(StandardLocation.CLASS_PATH, paramString, JavaFileObject.Kind.CLASS);
/*  847 */     return javaFileObject;
/*      */   }
/*      */
/*      */   private void showHelp() {
/*  851 */     printLines(getMessage("main.usage", new Object[] { "javap" }));
/*  852 */     for (Option option : recognizedOptions) {
/*  853 */       String str = option.aliases[0].substring(1);
/*  854 */       if (!str.startsWith("X") && !str.equals("fullversion") && !str.equals("h") && !str.equals("verify"))
/*      */       {
/*  856 */         printLines(getMessage("main.opt." + str, new Object[0])); }
/*      */     }
/*  858 */     String[] arrayOfString = { "-classpath", "-cp", "-bootclasspath" };
/*  859 */     for (String str : arrayOfString) {
/*  860 */       if (this.fileManager.isSupportedOption(str) != -1) {
/*      */
/*  862 */         String str1 = str.substring(1);
/*  863 */         printLines(getMessage("main.opt." + str1, new Object[0]));
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   private void showVersion(boolean paramBoolean) {
/*  869 */     printLines(version(paramBoolean ? "full" : "release"));
/*      */   }
/*      */
/*      */   private void printLines(String paramString) {
/*  873 */     this.log.println(paramString.replace("\n", nl));
/*      */   }
/*      */
/*  876 */   private static final String nl = System.getProperty("line.separator"); private static final String versionRBName = "com.sun.tools.javap.resources.version";
/*      */   private static ResourceBundle versionRB;
/*      */   protected Context context;
/*      */   JavaFileManager fileManager;
/*      */   JavaFileManager defaultFileManager;
/*      */   PrintWriter log;
/*      */
/*      */   private String version(String paramString) {
/*  884 */     if (versionRB == null) {
/*      */       try {
/*  886 */         versionRB = ResourceBundle.getBundle("com.sun.tools.javap.resources.version");
/*  887 */       } catch (MissingResourceException missingResourceException) {
/*  888 */         return getMessage("version.resource.missing", new Object[] { System.getProperty("java.version") });
/*      */       }
/*      */     }
/*      */     try {
/*  892 */       return versionRB.getString(paramString);
/*      */     }
/*  894 */     catch (MissingResourceException missingResourceException) {
/*  895 */       return getMessage("version.unknown", new Object[] { System.getProperty("java.version") });
/*      */     }
/*      */   }
/*      */   DiagnosticListener<? super JavaFileObject> diagnosticListener; List<String> classes; Options options; Locale task_locale; Map<Locale, ResourceBundle> bundles; protected Attribute.Factory attributeFactory; private static final String progname = "javap";
/*      */   private void reportError(String paramString, Object... paramVarArgs) {
/*  900 */     this.diagnosticListener.report(createDiagnostic(Diagnostic.Kind.ERROR, paramString, paramVarArgs));
/*      */   }
/*      */
/*      */   private void reportNote(String paramString, Object... paramVarArgs) {
/*  904 */     this.diagnosticListener.report(createDiagnostic(Diagnostic.Kind.NOTE, paramString, paramVarArgs));
/*      */   }
/*      */
/*      */   private void reportWarning(String paramString, Object... paramVarArgs) {
/*  908 */     this.diagnosticListener.report(createDiagnostic(Diagnostic.Kind.WARNING, paramString, paramVarArgs));
/*      */   }
/*      */
/*      */
/*      */   private Diagnostic<JavaFileObject> createDiagnostic(final Diagnostic.Kind kind, final String key, Object... args) {
/*  913 */     return new Diagnostic<JavaFileObject>() {
/*      */         public Kind getKind() {
/*  915 */           return kind;
/*      */         }
/*      */
/*      */         public JavaFileObject getSource() {
/*  919 */           return null;
/*      */         }
/*      */
/*      */         public long getPosition() {
/*  923 */           return -1L;
/*      */         }
/*      */
/*      */         public long getStartPosition() {
/*  927 */           return -1L;
/*      */         }
/*      */
/*      */         public long getEndPosition() {
/*  931 */           return -1L;
/*      */         }
/*      */
/*      */         public long getLineNumber() {
/*  935 */           return -1L;
/*      */         }
/*      */
/*      */         public long getColumnNumber() {
/*  939 */           return -1L;
/*      */         }
/*      */
/*      */         public String getCode() {
/*  943 */           return key;
/*      */         }
/*      */
/*      */         public String getMessage(Locale param1Locale) {
/*  947 */           return JavapTask.this.getMessage(param1Locale, key, args);
/*      */         }
/*      */
/*      */
/*      */         public String toString() {
/*  952 */           return getClass().getName() + "[key=" + key + ",args=" + Arrays.<Object>asList(args) + "]";
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */
/*      */
/*      */   public String getMessage(String paramString, Object... paramVarArgs) {
/*  960 */     return getMessage(this.task_locale, paramString, paramVarArgs);
/*      */   }
/*      */
/*      */   public String getMessage(Locale paramLocale, String paramString, Object... paramVarArgs) {
/*  964 */     if (this.bundles == null)
/*      */     {
/*      */
/*      */
/*  968 */       this.bundles = new HashMap<>();
/*      */     }
/*      */
/*  971 */     if (paramLocale == null) {
/*  972 */       paramLocale = Locale.getDefault();
/*      */     }
/*  974 */     ResourceBundle resourceBundle = this.bundles.get(paramLocale);
/*  975 */     if (resourceBundle == null) {
/*      */       try {
/*  977 */         resourceBundle = ResourceBundle.getBundle("com.sun.tools.javap.resources.javap", paramLocale);
/*  978 */         this.bundles.put(paramLocale, resourceBundle);
/*  979 */       } catch (MissingResourceException missingResourceException) {
/*  980 */         throw new InternalError(new Object[] { "Cannot find javap resource bundle for locale " + paramLocale });
/*      */       }
/*      */     }
/*      */
/*      */     try {
/*  985 */       return MessageFormat.format(resourceBundle.getString(paramString), paramVarArgs);
/*  986 */     } catch (MissingResourceException missingResourceException) {
/*  987 */       throw new InternalError(missingResourceException, new Object[] { paramString });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private static class SizeInputStream
/*      */     extends FilterInputStream
/*      */   {
/*      */     private int size;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     SizeInputStream(InputStream param1InputStream) {
/* 1007 */       super(param1InputStream);
/*      */     }
/*      */
/*      */     int size() {
/* 1011 */       return this.size;
/*      */     }
/*      */
/*      */
/*      */     public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
/* 1016 */       int i = super.read(param1ArrayOfbyte, param1Int1, param1Int2);
/* 1017 */       if (i > 0)
/* 1018 */         this.size += i;
/* 1019 */       return i;
/*      */     }
/*      */
/*      */
/*      */     public int read() throws IOException {
/* 1024 */       int i = super.read();
/* 1025 */       this.size++;
/* 1026 */       return i;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\JavapTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
