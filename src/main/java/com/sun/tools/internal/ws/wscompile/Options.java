/*     */ package com.sun.tools.internal.ws.wscompile;
/*     */ 
/*     */ import com.sun.tools.internal.ws.Invoker;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.annotation.processing.Filer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Options
/*     */ {
/*     */   public boolean verbose;
/*     */   public boolean quiet;
/*     */   public boolean keep;
/*  70 */   public File destDir = new File(".");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File sourceDir;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filer filer;
/*     */ 
/*     */ 
/*     */   
/*     */   public String encoding;
/*     */ 
/*     */ 
/*     */   
/*  88 */   public String classpath = System.getProperty("java.class.path");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> javacOptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nocompile;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean disableXmlSecurity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Target
/*     */   {
/* 113 */     V2_0, V2_1, V2_2;
/*     */     
/*     */     private static final Target LOADED_API_VERSION;
/*     */ 
/*     */     
/*     */     public boolean isLaterThan(Target t) {
/* 119 */       return (ordinal() >= t.ordinal());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Target parse(String token) {
/* 128 */       if (token.equals("2.0"))
/* 129 */         return V2_0; 
/* 130 */       if (token.equals("2.1"))
/* 131 */         return V2_1; 
/* 132 */       if (token.equals("2.2"))
/* 133 */         return V2_2; 
/* 134 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getVersion() {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/internal/ws/wscompile/Options$1.$SwitchMap$com$sun$tools$internal$ws$wscompile$Options$Target : [I
/*     */       //   3: aload_0
/*     */       //   4: invokevirtual ordinal : ()I
/*     */       //   7: iaload
/*     */       //   8: tableswitch default -> 45, 1 -> 36, 2 -> 39, 3 -> 42
/*     */       //   36: ldc '2.0'
/*     */       //   38: areturn
/*     */       //   39: ldc '2.1'
/*     */       //   41: areturn
/*     */       //   42: ldc '2.2'
/*     */       //   44: areturn
/*     */       //   45: aconst_null
/*     */       //   46: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #141	-> 0
/*     */       //   #143	-> 36
/*     */       //   #145	-> 39
/*     */       //   #147	-> 42
/*     */       //   #149	-> 45
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	47	0	this	Lcom/sun/tools/internal/ws/wscompile/Options$Target;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Target getDefault() {
/* 154 */       return V2_2;
/*     */     }
/*     */     
/*     */     public static Target getLoadedAPIVersion() {
/* 158 */       return LOADED_API_VERSION;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 165 */       if (Invoker.checkIfLoading22API()) {
/* 166 */         LOADED_API_VERSION = V2_2;
/*     */       }
/* 168 */       else if (Invoker.checkIfLoading21API()) {
/* 169 */         LOADED_API_VERSION = V2_1;
/*     */       } else {
/* 171 */         LOADED_API_VERSION = V2_0;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/* 176 */   public Target target = Target.V2_2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int STRICT = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int EXTENSION = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public int compatibilityMode = 1;
/*     */   
/*     */   public boolean isExtensionMode() {
/* 197 */     return (this.compatibilityMode == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean debug = false;
/*     */ 
/*     */   
/*     */   public boolean debugMode = false;
/*     */ 
/*     */   
/* 208 */   private final List<File> generatedFiles = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGeneratedFile(File file) {
/* 217 */     this.generatedFiles.add(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeGeneratedFiles() {
/* 224 */     for (File file : this.generatedFiles) {
/* 225 */       if (file.getName().endsWith(".java")) {
/* 226 */         boolean deleted = file.delete();
/* 227 */         if (this.verbose && !deleted) {
/* 228 */           System.out.println(MessageFormat.format("{0} could not be deleted.", new Object[] { file }));
/*     */         }
/*     */       } 
/*     */     } 
/* 232 */     this.generatedFiles.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<File> getGeneratedFiles() {
/* 239 */     return this.generatedFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteGeneratedFiles() {
/* 248 */     synchronized (this.generatedFiles) {
/* 249 */       for (File file : this.generatedFiles) {
/* 250 */         if (file.getName().endsWith(".java")) {
/* 251 */           boolean deleted = file.delete();
/* 252 */           if (this.verbose && !deleted) {
/* 253 */             System.out.println(MessageFormat.format("{0} could not be deleted.", new Object[] { file }));
/*     */           }
/*     */         } 
/*     */       } 
/* 257 */       this.generatedFiles.clear();
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
/*     */   public void parseArguments(String[] args) throws BadCommandLineException {
/* 269 */     for (int i = 0; i < args.length; i++) {
/* 270 */       if (args[i].length() == 0)
/* 271 */         throw new BadCommandLineException(); 
/* 272 */       if (args[i].charAt(0) == '-') {
/* 273 */         int j = parseArguments(args, i);
/* 274 */         if (j == 0)
/* 275 */           throw new BadCommandLineException(WscompileMessages.WSCOMPILE_INVALID_OPTION(args[i])); 
/* 276 */         i += j - 1;
/*     */       } else {
/* 278 */         addFile(args[i]);
/*     */       } 
/*     */     } 
/* 281 */     if (this.destDir == null)
/* 282 */       this.destDir = new File("."); 
/* 283 */     if (this.sourceDir == null) {
/* 284 */       this.sourceDir = this.destDir;
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
/*     */   protected void addFile(String arg) throws BadCommandLineException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int parseArguments(String[] args, int i) throws BadCommandLineException {
/* 306 */     if (args[i].equals("-g")) {
/* 307 */       this.debug = true;
/* 308 */       return 1;
/* 309 */     }  if (args[i].equals("-Xdebug")) {
/* 310 */       this.debugMode = true;
/* 311 */       return 1;
/* 312 */     }  if (args[i].equals("-Xendorsed"))
/*     */     {
/* 314 */       return 1; } 
/* 315 */     if (args[i].equals("-verbose")) {
/* 316 */       this.verbose = true;
/* 317 */       return 1;
/* 318 */     }  if (args[i].equals("-quiet")) {
/* 319 */       this.quiet = true;
/* 320 */       return 1;
/* 321 */     }  if (args[i].equals("-keep")) {
/* 322 */       this.keep = true;
/* 323 */       return 1;
/* 324 */     }  if (args[i].equals("-target")) {
/* 325 */       String token = requireArgument("-target", args, ++i);
/* 326 */       this.target = Target.parse(token);
/* 327 */       if (this.target == null)
/* 328 */         throw new BadCommandLineException(WscompileMessages.WSIMPORT_ILLEGAL_TARGET_VERSION(token)); 
/* 329 */       return 2;
/* 330 */     }  if (args[i].equals("-classpath") || args[i].equals("-cp")) {
/* 331 */       this.classpath = requireArgument("-classpath", args, ++i) + File.pathSeparator + System.getProperty("java.class.path");
/* 332 */       return 2;
/* 333 */     }  if (args[i].equals("-d")) {
/* 334 */       this.destDir = new File(requireArgument("-d", args, ++i));
/* 335 */       if (!this.destDir.exists())
/* 336 */         throw new BadCommandLineException(WscompileMessages.WSCOMPILE_NO_SUCH_DIRECTORY(this.destDir.getPath())); 
/* 337 */       return 2;
/* 338 */     }  if (args[i].equals("-s")) {
/* 339 */       this.sourceDir = new File(requireArgument("-s", args, ++i));
/* 340 */       this.keep = true;
/* 341 */       if (!this.sourceDir.exists()) {
/* 342 */         throw new BadCommandLineException(WscompileMessages.WSCOMPILE_NO_SUCH_DIRECTORY(this.sourceDir.getPath()));
/*     */       }
/* 344 */       return 2;
/* 345 */     }  if (args[i].equals("-extension")) {
/* 346 */       this.compatibilityMode = 2;
/* 347 */       return 1;
/* 348 */     }  if (args[i].startsWith("-help")) {
/* 349 */       WeAreDone done = new WeAreDone();
/* 350 */       done.initOptions(this);
/* 351 */       throw done;
/* 352 */     }  if (args[i].equals("-Xnocompile")) {
/*     */       
/* 354 */       this.nocompile = true;
/* 355 */       this.keep = true;
/* 356 */       return 1;
/* 357 */     }  if (args[i].equals("-encoding")) {
/* 358 */       this.encoding = requireArgument("-encoding", args, ++i);
/*     */       try {
/* 360 */         if (!Charset.isSupported(this.encoding)) {
/* 361 */           throw new BadCommandLineException(WscompileMessages.WSCOMPILE_UNSUPPORTED_ENCODING(this.encoding));
/*     */         }
/* 363 */       } catch (IllegalCharsetNameException icne) {
/* 364 */         throw new BadCommandLineException(WscompileMessages.WSCOMPILE_UNSUPPORTED_ENCODING(this.encoding));
/*     */       } 
/* 366 */       return 2;
/* 367 */     }  if (args[i].equals("-disableXmlSecurity")) {
/* 368 */       disableXmlSecurity();
/* 369 */       return 1;
/* 370 */     }  if (args[i].startsWith("-J")) {
/* 371 */       if (this.javacOptions == null) {
/* 372 */         this.javacOptions = new ArrayList<>();
/*     */       }
/* 374 */       this.javacOptions.add(args[i].substring(2));
/* 375 */       return 1;
/*     */     } 
/* 377 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void disableXmlSecurity() {
/* 382 */     this.disableXmlSecurity = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String requireArgument(String optionName, String[] args, int i) throws BadCommandLineException {
/* 390 */     if (args[i].startsWith("-")) {
/* 391 */       throw new BadCommandLineException(WscompileMessages.WSCOMPILE_MISSING_OPTION_ARGUMENT(optionName));
/*     */     }
/* 393 */     return args[i];
/*     */   }
/*     */   
/*     */   List<String> getJavacOptions(List<String> existingOptions, WsimportListener listener) {
/* 397 */     List<String> result = new ArrayList<>();
/* 398 */     for (String o : this.javacOptions) {
/* 399 */       if (o.contains("=") && !o.startsWith("A")) {
/* 400 */         int i = o.indexOf('=');
/* 401 */         String key = o.substring(0, i);
/* 402 */         if (existingOptions.contains(key)) {
/* 403 */           listener.message(WscompileMessages.WSCOMPILE_EXISTING_OPTION(key)); continue;
/*     */         } 
/* 405 */         result.add(key);
/* 406 */         result.add(o.substring(i + 1));
/*     */         continue;
/*     */       } 
/* 409 */       if (existingOptions.contains(o)) {
/* 410 */         listener.message(WscompileMessages.WSCOMPILE_EXISTING_OPTION(o)); continue;
/*     */       } 
/* 412 */       result.add(o);
/*     */     } 
/*     */ 
/*     */     
/* 416 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class WeAreDone
/*     */     extends BadCommandLineException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 428 */     if (this.classLoader == null) {
/* 429 */       this
/*     */         
/* 431 */         .classLoader = new URLClassLoader(pathToURLs(this.classpath), getClass().getClassLoader());
/*     */     }
/* 433 */     return this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL[] pathToURLs(String path) {
/* 444 */     StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
/* 445 */     URL[] urls = new URL[st.countTokens()];
/* 446 */     int count = 0;
/* 447 */     while (st.hasMoreTokens()) {
/* 448 */       URL url = fileToURL(new File(st.nextToken()));
/* 449 */       if (url != null) {
/* 450 */         urls[count++] = url;
/*     */       }
/*     */     } 
/* 453 */     if (urls.length != count) {
/* 454 */       URL[] tmp = new URL[count];
/* 455 */       System.arraycopy(urls, 0, tmp, 0, count);
/* 456 */       urls = tmp;
/*     */     } 
/* 458 */     return urls;
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
/*     */   public static URL fileToURL(File file) {
/*     */     try {
/* 471 */       name = file.getCanonicalPath();
/* 472 */     } catch (IOException e) {
/* 473 */       name = file.getAbsolutePath();
/*     */     } 
/* 475 */     String name = name.replace(File.separatorChar, '/');
/* 476 */     if (!name.startsWith("/")) {
/* 477 */       name = "/" + name;
/*     */     }
/*     */ 
/*     */     
/* 481 */     if (!file.isFile()) {
/* 482 */       name = name + "/";
/*     */     }
/*     */     try {
/* 485 */       return new URL("file", "", name);
/* 486 */     } catch (MalformedURLException e) {
/* 487 */       throw new IllegalArgumentException("file");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\Options.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */