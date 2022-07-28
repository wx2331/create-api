/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.LanguageVersion;
/*     */ import com.sun.tools.javac.main.CommandLine;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Start
/*     */   extends ToolOption.Helper
/*     */ {
/*     */   private final Context context;
/*     */   private final String defaultDocletClassName;
/*     */   private final ClassLoader docletParentClassLoader;
/*     */   private static final String javadocName = "javadoc";
/*     */   private static final String standardDocletClassName = "com.sun.tools.doclets.standard.Standard";
/*  74 */   private long defaultFilter = 5L;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Messager messager;
/*     */ 
/*     */ 
/*     */   
/*     */   private DocletInvoker docletInvoker;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean apiMode;
/*     */ 
/*     */ 
/*     */   
/*     */   Start(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2) {
/*  91 */     this(paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3, paramString2, (ClassLoader)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Start(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2, ClassLoader paramClassLoader) {
/* 100 */     this.context = new Context();
/* 101 */     this.messager = new Messager(this.context, paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3);
/* 102 */     this.defaultDocletClassName = paramString2;
/* 103 */     this.docletParentClassLoader = paramClassLoader;
/*     */   }
/*     */   
/*     */   Start(String paramString1, String paramString2) {
/* 107 */     this(paramString1, paramString2, (ClassLoader)null);
/*     */   }
/*     */ 
/*     */   
/*     */   Start(String paramString1, String paramString2, ClassLoader paramClassLoader) {
/* 112 */     this.context = new Context();
/* 113 */     this.messager = new Messager(this.context, paramString1);
/* 114 */     this.defaultDocletClassName = paramString2;
/* 115 */     this.docletParentClassLoader = paramClassLoader;
/*     */   }
/*     */   
/*     */   Start(String paramString, ClassLoader paramClassLoader) {
/* 119 */     this(paramString, "com.sun.tools.doclets.standard.Standard", paramClassLoader);
/*     */   }
/*     */   
/*     */   Start(String paramString) {
/* 123 */     this(paramString, "com.sun.tools.doclets.standard.Standard");
/*     */   }
/*     */   
/*     */   Start(ClassLoader paramClassLoader) {
/* 127 */     this("javadoc", paramClassLoader);
/*     */   }
/*     */   
/*     */   Start() {
/* 131 */     this("javadoc");
/*     */   }
/*     */   
/*     */   public Start(Context paramContext) {
/* 135 */     paramContext.getClass();
/* 136 */     this.context = paramContext;
/* 137 */     this.apiMode = true;
/* 138 */     this.defaultDocletClassName = "com.sun.tools.doclets.standard.Standard";
/* 139 */     this.docletParentClassLoader = null;
/*     */     
/* 141 */     Log log = (Log)paramContext.get(Log.logKey);
/* 142 */     if (log instanceof Messager) {
/* 143 */       this.messager = (Messager)log;
/*     */     } else {
/* 145 */       PrintWriter printWriter = (PrintWriter)paramContext.get(Log.outKey);
/* 146 */       this.messager = (printWriter == null) ? new Messager(paramContext, "javadoc") : new Messager(paramContext, "javadoc", printWriter, printWriter, printWriter);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void usage() {
/* 156 */     usage(true);
/*     */   }
/*     */   
/*     */   void usage(boolean paramBoolean) {
/* 160 */     usage("main.usage", "-help", (String)null, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   void Xusage() {
/* 165 */     Xusage(true);
/*     */   }
/*     */   
/*     */   void Xusage(boolean paramBoolean) {
/* 169 */     usage("main.Xusage", "-X", "main.Xusage.foot", paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void usage(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
/* 175 */     this.messager.notice(paramString1, new Object[0]);
/*     */ 
/*     */     
/* 178 */     if (this.docletInvoker != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 184 */       this.docletInvoker.optionLength(paramString2);
/*     */     }
/*     */     
/* 187 */     if (paramString3 != null) {
/* 188 */       this.messager.notice(paramString3, new Object[0]);
/*     */     }
/* 190 */     if (paramBoolean) exit();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void exit() {
/* 197 */     this.messager.exit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int begin(String... paramVarArgs) {
/* 205 */     boolean bool = begin((Class<?>)null, paramVarArgs, Collections.emptySet());
/* 206 */     return bool ? 0 : 1;
/*     */   }
/*     */   
/*     */   public boolean begin(Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1) {
/* 210 */     ArrayList<String> arrayList = new ArrayList();
/* 211 */     for (String str : paramIterable) arrayList.add(str); 
/* 212 */     return begin(paramClass, arrayList.<String>toArray(new String[arrayList.size()]), paramIterable1);
/*     */   }
/*     */   
/*     */   private boolean begin(Class<?> paramClass, String[] paramArrayOfString, Iterable<? extends JavaFileObject> paramIterable) {
/* 216 */     int i = 0;
/*     */     
/*     */     try {
/* 219 */       i = !parseAndExecute(paramClass, paramArrayOfString, paramIterable) ? 1 : 0;
/* 220 */     } catch (ExitJavadoc exitJavadoc) {
/*     */     
/* 222 */     } catch (OutOfMemoryError outOfMemoryError) {
/* 223 */       this.messager.error(Messager.NOPOS, "main.out.of.memory", new Object[0]);
/* 224 */       i = 1;
/* 225 */     } catch (ClientCodeException clientCodeException) {
/*     */       
/* 227 */       throw clientCodeException;
/* 228 */     } catch (Error error) {
/* 229 */       error.printStackTrace(System.err);
/* 230 */       this.messager.error(Messager.NOPOS, "main.fatal.error", new Object[0]);
/* 231 */       i = 1;
/* 232 */     } catch (Exception exception) {
/* 233 */       exception.printStackTrace(System.err);
/* 234 */       this.messager.error(Messager.NOPOS, "main.fatal.exception", new Object[0]);
/* 235 */       i = 1;
/*     */     } finally {
/* 237 */       this.messager.exitNotice();
/* 238 */       this.messager.flush();
/*     */     } 
/* 240 */     i |= (this.messager.nerrors() > 0) ? 1 : 0;
/* 241 */     i |= (this.rejectWarnings && this.messager.nwarnings() > 0) ? 1 : 0;
/* 242 */     return (i == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean parseAndExecute(Class<?> paramClass, String[] paramArrayOfString, Iterable<? extends JavaFileObject> paramIterable) throws IOException {
/* 252 */     long l = System.currentTimeMillis();
/*     */     
/* 254 */     ListBuffer listBuffer = new ListBuffer();
/*     */ 
/*     */     
/*     */     try {
/* 258 */       paramArrayOfString = CommandLine.parse(paramArrayOfString);
/* 259 */     } catch (FileNotFoundException fileNotFoundException) {
/* 260 */       this.messager.error(Messager.NOPOS, "main.cant.read", new Object[] { fileNotFoundException.getMessage() });
/* 261 */       exit();
/* 262 */     } catch (IOException iOException) {
/* 263 */       iOException.printStackTrace(System.err);
/* 264 */       exit();
/*     */     } 
/*     */ 
/*     */     
/* 268 */     JavaFileManager javaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/* 269 */     setDocletInvoker(paramClass, javaFileManager, paramArrayOfString);
/*     */     
/* 271 */     this.compOpts = Options.instance(this.context);
/*     */     
/* 273 */     this.compOpts.put("-Xlint:-options", "-Xlint:-options");
/*     */ 
/*     */     
/* 276 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 277 */       String str = paramArrayOfString[b];
/*     */       
/* 279 */       ToolOption toolOption = ToolOption.get(str);
/* 280 */       if (toolOption != null) {
/*     */         
/* 282 */         if (toolOption == ToolOption.LOCALE && b > 0) {
/* 283 */           usageError("main.locale_first", new Object[0]);
/*     */         }
/* 285 */         if (toolOption.hasArg) {
/* 286 */           oneArg(paramArrayOfString, b++);
/* 287 */           toolOption.process(this, paramArrayOfString[b]);
/*     */         } else {
/* 289 */           setOption(str);
/* 290 */           toolOption.process(this);
/*     */         }
/*     */       
/* 293 */       } else if (str.startsWith("-XD")) {
/*     */         
/* 295 */         String str1 = str.substring("-XD".length());
/* 296 */         int i = str1.indexOf('=');
/* 297 */         String str2 = (i < 0) ? str1 : str1.substring(0, i);
/* 298 */         String str3 = (i < 0) ? str1 : str1.substring(i + 1);
/* 299 */         this.compOpts.put(str2, str3);
/*     */ 
/*     */       
/*     */       }
/* 303 */       else if (str.startsWith("-")) {
/*     */         
/* 305 */         int i = this.docletInvoker.optionLength(str);
/* 306 */         if (i < 0) {
/*     */           
/* 308 */           exit();
/* 309 */         } else if (i == 0) {
/*     */           
/* 311 */           usageError("main.invalid_flag", new Object[] { str });
/*     */         } else {
/*     */           
/* 314 */           if (b + i > paramArrayOfString.length) {
/* 315 */             usageError("main.requires_argument", new Object[] { str });
/*     */           }
/* 317 */           ListBuffer listBuffer1 = new ListBuffer();
/* 318 */           for (byte b1 = 0; b1 < i - 1; b1++) {
/* 319 */             listBuffer1.append(paramArrayOfString[++b]);
/*     */           }
/* 321 */           setOption(str, listBuffer1.toList());
/*     */         } 
/*     */       } else {
/* 324 */         listBuffer.append(str);
/*     */       } 
/*     */     } 
/* 327 */     this.compOpts.notifyListeners();
/*     */     
/* 329 */     if (listBuffer.isEmpty() && this.subPackages.isEmpty() && isEmpty(paramIterable)) {
/* 330 */       usageError("main.No_packages_or_classes_specified", new Object[0]);
/*     */     }
/*     */     
/* 333 */     if (!this.docletInvoker.validOptions(this.options.toList()))
/*     */     {
/* 335 */       exit();
/*     */     }
/*     */     
/* 338 */     JavadocTool javadocTool = JavadocTool.make0(this.context);
/* 339 */     if (javadocTool == null) return false;
/*     */     
/* 341 */     if (this.showAccess == null) {
/* 342 */       setFilter(this.defaultFilter);
/*     */     }
/*     */     
/* 345 */     LanguageVersion languageVersion = this.docletInvoker.languageVersion();
/* 346 */     RootDocImpl rootDocImpl = javadocTool.getRootDocImpl(this.docLocale, this.encoding, this.showAccess, listBuffer
/*     */ 
/*     */ 
/*     */         
/* 350 */         .toList(), this.options
/* 351 */         .toList(), paramIterable, this.breakiterator, this.subPackages
/*     */ 
/*     */         
/* 354 */         .toList(), this.excludedPackages
/* 355 */         .toList(), this.docClasses, (languageVersion == null || languageVersion == LanguageVersion.JAVA_1_1), this.quiet);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 362 */     javadocTool = null;
/*     */ 
/*     */     
/* 365 */     boolean bool = (rootDocImpl != null);
/* 366 */     if (bool) bool = this.docletInvoker.start(rootDocImpl);
/*     */ 
/*     */     
/* 369 */     if (this.compOpts.get("-verbose") != null) {
/* 370 */       l = System.currentTimeMillis() - l;
/* 371 */       this.messager.notice("main.done_in", new Object[] { Long.toString(l) });
/*     */     } 
/*     */     
/* 374 */     return bool;
/*     */   }
/*     */   
/*     */   private <T> boolean isEmpty(Iterable<T> paramIterable) {
/* 378 */     return !paramIterable.iterator().hasNext();
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
/*     */   private void setDocletInvoker(Class<?> paramClass, JavaFileManager paramJavaFileManager, String[] paramArrayOfString) {
/* 394 */     if (paramClass != null) {
/* 395 */       this.docletInvoker = new DocletInvoker(this.messager, paramClass, this.apiMode);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 400 */     String str1 = null;
/* 401 */     String str2 = null;
/*     */ 
/*     */     
/* 404 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 405 */       String str = paramArrayOfString[b];
/* 406 */       if (str.equals(ToolOption.DOCLET.opt)) {
/* 407 */         oneArg(paramArrayOfString, b++);
/* 408 */         if (str1 != null) {
/* 409 */           usageError("main.more_than_one_doclet_specified_0_and_1", new Object[] { str1, paramArrayOfString[b] });
/*     */         }
/*     */         
/* 412 */         str1 = paramArrayOfString[b];
/* 413 */       } else if (str.equals(ToolOption.DOCLETPATH.opt)) {
/* 414 */         oneArg(paramArrayOfString, b++);
/* 415 */         if (str2 == null) {
/* 416 */           str2 = paramArrayOfString[b];
/*     */         } else {
/* 418 */           str2 = str2 + File.pathSeparator + paramArrayOfString[b];
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 423 */     if (str1 == null) {
/* 424 */       str1 = this.defaultDocletClassName;
/*     */     }
/*     */ 
/*     */     
/* 428 */     this.docletInvoker = new DocletInvoker(this.messager, paramJavaFileManager, str1, str2, this.docletParentClassLoader, this.apiMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void oneArg(String[] paramArrayOfString, int paramInt) {
/* 439 */     if (paramInt + 1 < paramArrayOfString.length) {
/* 440 */       setOption(paramArrayOfString[paramInt], paramArrayOfString[paramInt + 1]);
/*     */     } else {
/* 442 */       usageError("main.requires_argument", new Object[] { paramArrayOfString[paramInt] });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void usageError(String paramString, Object... paramVarArgs) {
/* 448 */     this.messager.error(Messager.NOPOS, paramString, paramVarArgs);
/* 449 */     usage(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setOption(String paramString) {
/* 456 */     String[] arrayOfString = { paramString };
/* 457 */     this.options.append(arrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setOption(String paramString1, String paramString2) {
/* 464 */     String[] arrayOfString = { paramString1, paramString2 };
/* 465 */     this.options.append(arrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setOption(String paramString, List<String> paramList) {
/* 472 */     String[] arrayOfString = new String[paramList.length() + 1];
/* 473 */     byte b = 0;
/* 474 */     arrayOfString[b++] = paramString;
/* 475 */     for (List<String> list = paramList; list.nonEmpty(); list = list.tail) {
/* 476 */       arrayOfString[b++] = (String)list.head;
/*     */     }
/* 478 */     this.options.append(arrayOfString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\Start.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */