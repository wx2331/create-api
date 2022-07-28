/*     */ package com.sun.tools.internal.ws.wscompile;
/*     */
/*     */ import com.sun.codemodel.internal.CodeWriter;
/*     */ import com.sun.codemodel.internal.writer.ProgressCodeWriter;
/*     */ import com.sun.istack.internal.tools.DefaultAuthenticator;
/*     */ import com.sun.istack.internal.tools.ParallelWorldClassLoader;
/*     */ import com.sun.tools.internal.ws.ToolVersion;
/*     */ import com.sun.tools.internal.ws.api.TJavaGeneratorExtension;
/*     */ import com.sun.tools.internal.ws.processor.generator.CustomExceptionGenerator;
/*     */ import com.sun.tools.internal.ws.processor.generator.GeneratorBase;
/*     */ import com.sun.tools.internal.ws.processor.generator.JwsImplGenerator;
/*     */ import com.sun.tools.internal.ws.processor.generator.SeiGenerator;
/*     */ import com.sun.tools.internal.ws.processor.generator.ServiceGenerator;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.modeler.wsdl.ConsoleErrorReporter;
/*     */ import com.sun.tools.internal.ws.processor.modeler.wsdl.WSDLModeler;
/*     */ import com.sun.tools.internal.ws.processor.util.DirectoryUtil;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.util.WSDLFetcher;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.InternalizationLogic;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.WSDLInternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.util.NullStream;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import javax.xml.bind.JAXBPermission;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.EndpointContext;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class WsimportTool
/*     */ {
/*     */   private static final String WSIMPORT = "wsimport";
/*     */   private final PrintStream out;
/*     */   private final Container container;
/*  76 */   protected WsimportOptions options = new WsimportOptions();
/*     */
/*     */   public WsimportTool(OutputStream out) {
/*  79 */     this(out, null);
/*     */   }
/*     */
/*     */   public WsimportTool(OutputStream logStream, Container container) {
/*  83 */     this.out = (logStream instanceof PrintStream) ? (PrintStream)logStream : new PrintStream(logStream);
/*  84 */     this.container = container;
/*     */   }
/*     */
/*     */   protected class Listener extends WsimportListener {
/*  88 */     ConsoleErrorReporter cer = new ConsoleErrorReporter((WsimportTool.this.out == null) ? new PrintStream((OutputStream)new NullStream()) : WsimportTool.this.out);
/*     */
/*     */
/*     */     public void generatedFile(String fileName) {
/*  92 */       message(fileName);
/*     */     }
/*     */
/*     */
/*     */     public void message(String msg) {
/*  97 */       WsimportTool.this.out.println(msg);
/*     */     }
/*     */
/*     */
/*     */     public void error(SAXParseException exception) {
/* 102 */       this.cer.error(exception);
/*     */     }
/*     */
/*     */
/*     */     public void fatalError(SAXParseException exception) {
/* 107 */       this.cer.fatalError(exception);
/*     */     }
/*     */
/*     */
/*     */     public void warning(SAXParseException exception) {
/* 112 */       this.cer.warning(exception);
/*     */     }
/*     */
/*     */
/*     */     public void debug(SAXParseException exception) {
/* 117 */       this.cer.debug(exception);
/*     */     }
/*     */
/*     */
/*     */     public void info(SAXParseException exception) {
/* 122 */       this.cer.info(exception);
/*     */     }
/*     */
/*     */     public void enableDebugging() {
/* 126 */       this.cer.enableDebugging();
/*     */     }
/*     */   }
/*     */
/*     */   protected class Receiver
/*     */     extends ErrorReceiverFilter {
/*     */     private Listener listener;
/*     */
/*     */     public Receiver(Listener listener) {
/* 135 */       super(listener);
/* 136 */       this.listener = listener;
/*     */     }
/*     */
/*     */
/*     */     public void info(SAXParseException exception) {
/* 141 */       if (WsimportTool.this.options.verbose) {
/* 142 */         super.info(exception);
/*     */       }
/*     */     }
/*     */
/*     */     public void warning(SAXParseException exception) {
/* 147 */       if (!WsimportTool.this.options.quiet) {
/* 148 */         super.warning(exception);
/*     */       }
/*     */     }
/*     */
/*     */     public void pollAbort() throws AbortException {
/* 153 */       if (this.listener.isCanceled()) {
/* 154 */         throw new AbortException();
/*     */       }
/*     */     }
/*     */
/*     */     public void debug(SAXParseException exception) {
/* 159 */       if (WsimportTool.this.options.debugMode) {
/* 160 */         this.listener.debug(exception);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   public boolean run(String[] args) {
/* 166 */     Listener listener = new Listener();
/* 167 */     Receiver receiver = new Receiver(listener);
/* 168 */     return run(args, listener, receiver);
/*     */   }
/*     */
/*     */
/*     */   protected boolean run(String[] args, Listener listener, Receiver receiver) {
/* 173 */     for (String arg : args) {
/* 174 */       if (arg.equals("-version")) {
/* 175 */         listener.message(
/* 176 */             WscompileMessages.WSIMPORT_VERSION(ToolVersion.VERSION.MAJOR_VERSION));
/* 177 */         return true;
/*     */       }
/* 179 */       if (arg.equals("-fullversion")) {
/* 180 */         listener.message(
/* 181 */             WscompileMessages.WSIMPORT_FULLVERSION(ToolVersion.VERSION.toString()));
/* 182 */         return true;
/*     */       }
/*     */     }
/*     */
/*     */     try {
/* 187 */       parseArguments(args, listener, receiver);
/*     */
/*     */       try {
/* 190 */         Model wsdlModel = buildWsdlModel(listener, receiver);
/* 191 */         if (wsdlModel == null) {
/* 192 */           return false;
/*     */         }
/* 194 */         if (!generateCode(listener, receiver, wsdlModel, true)) {
/* 195 */           return false;
/*     */
/*     */
/*     */         }
/*     */
/*     */
/*     */       }
/* 202 */       catch (IOException e) {
/* 203 */         receiver.error(e);
/* 204 */         return false;
/* 205 */       } catch (XMLStreamException e) {
/* 206 */         receiver.error(e);
/* 207 */         return false;
/*     */       }
/* 209 */       if (!this.options.nocompile &&
/* 210 */         !compileGeneratedClasses(receiver, listener)) {
/* 211 */         listener.message(WscompileMessages.WSCOMPILE_COMPILATION_FAILED());
/* 212 */         return false;
/*     */       }
/*     */
/*     */       try {
/* 216 */         if (this.options.clientjar != null)
/*     */         {
/* 218 */           addClassesToGeneratedFiles();
/* 219 */           jarArtifacts(listener);
/*     */         }
/*     */
/* 222 */       } catch (IOException e) {
/* 223 */         receiver.error(e);
/* 224 */         return false;
/*     */       }
/*     */
/* 227 */     } catch (WeAreDone done) {
/* 228 */       usage(done.getOptions());
/* 229 */     } catch (BadCommandLineException e) {
/* 230 */       if (e.getMessage() != null) {
/* 231 */         System.out.println(e.getMessage());
/* 232 */         System.out.println();
/*     */       }
/* 234 */       usage(e.getOptions());
/* 235 */       return false;
/*     */     } finally {
/* 237 */       deleteGeneratedFiles();
/* 238 */       if (!this.options.disableAuthenticator) {
/* 239 */         DefaultAuthenticator.reset();
/*     */       }
/*     */     }
/* 242 */     if (receiver.hadError()) {
/* 243 */       return false;
/*     */     }
/* 245 */     return true;
/*     */   }
/*     */
/*     */   private void deleteGeneratedFiles() {
/* 249 */     Set<File> trackedRootPackages = new HashSet<>();
/*     */
/* 251 */     if (this.options.clientjar != null) {
/*     */
/* 253 */       Iterable<File> generatedFiles = this.options.getGeneratedFiles();
/* 254 */       synchronized (generatedFiles) {
/* 255 */         for (File file : generatedFiles) {
/* 256 */           if (!file.getName().endsWith(".java")) {
/* 257 */             boolean deleted = file.delete();
/* 258 */             if (this.options.verbose && !deleted) {
/* 259 */               System.out.println(MessageFormat.format("{0} could not be deleted.", new Object[] { file }));
/*     */             }
/* 261 */             trackedRootPackages.add(file.getParentFile());
/*     */           }
/*     */         }
/*     */       }
/*     */
/* 266 */       for (File pkg : trackedRootPackages) {
/*     */
/* 268 */         while (pkg.list() != null && (pkg.list()).length == 0 && !pkg.equals(this.options.destDir)) {
/* 269 */           File parentPkg = pkg.getParentFile();
/* 270 */           boolean deleted = pkg.delete();
/* 271 */           if (this.options.verbose && !deleted) {
/* 272 */             System.out.println(MessageFormat.format("{0} could not be deleted.", new Object[] { pkg }));
/*     */           }
/* 274 */           pkg = parentPkg;
/*     */         }
/*     */       }
/*     */     }
/* 278 */     if (!this.options.keep) {
/* 279 */       this.options.removeGeneratedFiles();
/*     */     }
/*     */   }
/*     */
/*     */   private void addClassesToGeneratedFiles() throws IOException {
/* 284 */     Iterable<File> generatedFiles = this.options.getGeneratedFiles();
/* 285 */     final List<File> trackedClassFiles = new ArrayList<>();
/* 286 */     for (File f : generatedFiles) {
/* 287 */       if (f.getName().endsWith(".java")) {
/* 288 */         String relativeDir = DirectoryUtil.getRelativePathfromCommonBase(f.getParentFile(), this.options.sourceDir);
/* 289 */         final String className = f.getName().substring(0, f.getName().indexOf(".java"));
/* 290 */         File classDir = new File(this.options.destDir, relativeDir);
/* 291 */         if (classDir.exists()) {
/* 292 */           classDir.listFiles(new FilenameFilter()
/*     */               {
/*     */                 public boolean accept(File dir, String name) {
/* 295 */                   if (name.equals(className + ".class") || (name.startsWith(className + "$") && name.endsWith(".class"))) {
/* 296 */                     trackedClassFiles.add(new File(dir, name));
/* 297 */                     return true;
/*     */                   }
/* 299 */                   return false;
/*     */                 }
/*     */               });
/*     */         }
/*     */       }
/*     */     }
/* 305 */     for (File f : trackedClassFiles) {
/* 306 */       this.options.addGeneratedFile(f);
/*     */     }
/*     */   }
/*     */
/*     */   private void jarArtifacts(WsimportListener listener) throws IOException {
/* 311 */     File zipFile = new File(this.options.clientjar);
/* 312 */     if (!zipFile.isAbsolute()) {
/* 313 */       zipFile = new File(this.options.destDir, this.options.clientjar);
/*     */     }
/*     */
/*     */
/* 317 */     if (!this.options.quiet) {
/* 318 */       listener.message(WscompileMessages.WSIMPORT_ARCHIVING_ARTIFACTS(zipFile));
/*     */     }
/*     */
/* 321 */     BufferedInputStream bis = null;
/* 322 */     FileInputStream fi = null;
/* 323 */     FileOutputStream fos = new FileOutputStream(zipFile);
/* 324 */     JarOutputStream jos = new JarOutputStream(fos);
/*     */     try {
/* 326 */       String base = this.options.destDir.getCanonicalPath();
/* 327 */       for (File f : this.options.getGeneratedFiles()) {
/*     */
/* 329 */         if (f.getName().endsWith(".java")) {
/*     */           continue;
/*     */         }
/* 332 */         if (this.options.verbose) {
/* 333 */           listener.message(WscompileMessages.WSIMPORT_ARCHIVE_ARTIFACT(f, this.options.clientjar));
/*     */         }
/* 335 */         String entry = f.getCanonicalPath().substring(base.length() + 1).replace(File.separatorChar, '/');
/* 336 */         fi = new FileInputStream(f);
/* 337 */         bis = new BufferedInputStream(fi);
/* 338 */         JarEntry jarEntry = new JarEntry(entry);
/* 339 */         jos.putNextEntry(jarEntry);
/*     */
/* 341 */         byte[] buffer = new byte[1024]; int bytesRead;
/* 342 */         while ((bytesRead = bis.read(buffer)) != -1) {
/* 343 */           jos.write(buffer, 0, bytesRead);
/*     */         }
/*     */       }
/*     */     } finally {
/*     */       try {
/* 348 */         if (bis != null) {
/* 349 */           bis.close();
/*     */         }
/*     */       } finally {
/* 352 */         if (jos != null) {
/* 353 */           jos.close();
/*     */         }
/* 355 */         if (fi != null) {
/* 356 */           fi.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   protected void parseArguments(String[] args, Listener listener, Receiver receiver) throws BadCommandLineException {
/* 364 */     this.options.parseArguments(args);
/* 365 */     this.options.validate();
/* 366 */     if (this.options.debugMode)
/* 367 */       listener.enableDebugging();
/* 368 */     this.options.parseBindings(receiver);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected Model buildWsdlModel(Listener listener, final Receiver receiver) throws BadCommandLineException, XMLStreamException, IOException {
/* 375 */     if (!this.options.disableAuthenticator)
/*     */
/*     */     {
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 408 */       DefaultAuthenticator da = DefaultAuthenticator.getAuthenticator();
/* 409 */       if (this.options.proxyAuth != null) {
/* 410 */         da.setProxyAuth(this.options.proxyAuth);
/*     */       }
/* 412 */       if (this.options.authFile != null)
/* 413 */       { da.setAuth(this.options.authFile, new AuthListener(true)); }
/*     */       else
/* 415 */       { da.setAuth(new File(WsimportOptions.defaultAuthfile), new AuthListener(this, false, receiver)); }  }  class AuthListener implements DefaultAuthenticator.Receiver {
/*     */       private final boolean isFatal;
/*     */       AuthListener(boolean isFatal) { this.isFatal = isFatal; }
/*     */       public void onParsingError(String text, Locator loc) { error(new SAXParseException(WscompileMessages.WSIMPORT_ILLEGAL_AUTH_INFO(text), loc)); }
/* 419 */       public void onError(Exception e, Locator loc) { if (e instanceof java.io.FileNotFoundException) { error(new SAXParseException(WscompileMessages.WSIMPORT_AUTH_FILE_NOT_FOUND(loc.getSystemId(), WsimportOptions.defaultAuthfile), null)); } else { error(new SAXParseException(WscompileMessages.WSIMPORT_FAILED_TO_PARSE(loc.getSystemId(), e.getMessage()), loc)); }  } private void error(SAXParseException e) { if (this.isFatal) { receiver.error(e); } else { receiver.debug(e); }  } }; if (!this.options.quiet) {
/* 420 */       listener.message(WscompileMessages.WSIMPORT_PARSING_WSDL());
/*     */     }
/*     */
/* 423 */     MetadataFinder forest = new MetadataFinder((InternalizationLogic)new WSDLInternalizationLogic(), this.options, receiver);
/* 424 */     forest.parseWSDL();
/* 425 */     if (forest.isMexMetadata) {
/* 426 */       receiver.reset();
/*     */     }
/* 428 */     WSDLModeler wsdlModeler = new WSDLModeler(this.options, receiver, forest);
/* 429 */     Model wsdlModel = wsdlModeler.buildModel();
/* 430 */     if (wsdlModel == null) {
/* 431 */       listener.message(WsdlMessages.PARSING_PARSE_FAILED());
/*     */     }
/*     */
/* 434 */     if (this.options.clientjar != null) {
/* 435 */       if (!this.options.quiet)
/* 436 */         listener.message(WscompileMessages.WSIMPORT_FETCHING_METADATA());
/* 437 */       this.options.wsdlLocation = (new WSDLFetcher(this.options, listener)).fetchWsdls(forest);
/*     */     }
/*     */
/* 440 */     return wsdlModel;
/*     */   }
/*     */
/*     */
/*     */   protected boolean generateCode(Listener listener, Receiver receiver, Model wsdlModel, boolean generateService) throws IOException {
/*     */     WSCodeWriter wSCodeWriter;
/*     */     ProgressCodeWriter progressCodeWriter;
/* 447 */     if (!this.options.quiet) {
/* 448 */       listener.message(WscompileMessages.WSIMPORT_GENERATING_CODE());
/*     */     }
/* 450 */     TJavaGeneratorExtension[] genExtn = ServiceFinder.<TJavaGeneratorExtension>find(TJavaGeneratorExtension.class).toArray();
/* 451 */     CustomExceptionGenerator.generate(wsdlModel, this.options, receiver);
/* 452 */     SeiGenerator.generate(wsdlModel, this.options, receiver, genExtn);
/* 453 */     if (receiver.hadError()) {
/* 454 */       throw new AbortException();
/*     */     }
/* 456 */     if (generateService)
/*     */     {
/* 458 */       ServiceGenerator.generate(wsdlModel, this.options, receiver);
/*     */     }
/* 460 */     for (GeneratorBase g : ServiceFinder.<GeneratorBase>find(GeneratorBase.class)) {
/* 461 */       g.init(wsdlModel, this.options, receiver);
/* 462 */       g.doGeneration();
/*     */     }
/*     */
/* 465 */     List<String> implFiles = null;
/* 466 */     if (this.options.isGenerateJWS) {
/* 467 */       implFiles = JwsImplGenerator.generate(wsdlModel, this.options, receiver);
/*     */     }
/*     */
/* 470 */     for (Plugin plugin : this.options.activePlugins) {
/*     */       try {
/* 472 */         plugin.run(wsdlModel, this.options, receiver);
/* 473 */       } catch (SAXException sex) {
/*     */
/* 475 */         return false;
/*     */       }
/*     */     }
/*     */
/*     */
/* 480 */     if (this.options.filer != null) {
/* 481 */       wSCodeWriter = new FilerCodeWriter(this.options.sourceDir, this.options);
/*     */     } else {
/* 483 */       wSCodeWriter = new WSCodeWriter(this.options.sourceDir, this.options);
/*     */     }
/*     */
/* 486 */     if (this.options.verbose)
/* 487 */       progressCodeWriter = new ProgressCodeWriter((CodeWriter)wSCodeWriter, this.out);
/* 488 */     this.options.getCodeModel().build((CodeWriter)progressCodeWriter);
/*     */
/* 490 */     if (this.options.isGenerateJWS)
/*     */     {
/* 492 */       return JwsImplGenerator.moveToImplDestDir(implFiles, this.options, receiver);
/*     */     }
/*     */
/* 495 */     return true;
/*     */   }
/*     */
/*     */   public void setEntityResolver(EntityResolver resolver) {
/* 499 */     this.options.entityResolver = resolver;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private static boolean useBootClasspath(Class clazz) {
/*     */     try {
/* 507 */       ParallelWorldClassLoader.toJarUrl(clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class"));
/* 508 */       return true;
/* 509 */     } catch (Exception e) {
/* 510 */       return false;
/*     */     }
/*     */   }
/*     */
/*     */   protected boolean compileGeneratedClasses(ErrorReceiver receiver, WsimportListener listener) {
/* 515 */     List<String> sourceFiles = new ArrayList<>();
/*     */
/* 517 */     for (File f : this.options.getGeneratedFiles()) {
/* 518 */       if (f.exists() && f.getName().endsWith(".java")) {
/* 519 */         sourceFiles.add(f.getAbsolutePath());
/*     */       }
/*     */     }
/*     */
/* 523 */     if (sourceFiles.size() > 0) {
/* 524 */       String classDir = this.options.destDir.getAbsolutePath();
/* 525 */       String classpathString = createClasspathString();
/* 526 */       boolean bootCP = (useBootClasspath(EndpointContext.class) || useBootClasspath(JAXBPermission.class));
/* 527 */       List<String> args = new ArrayList<>();
/* 528 */       args.add("-d");
/* 529 */       args.add(classDir);
/* 530 */       args.add("-classpath");
/* 531 */       args.add(classpathString);
/*     */
/* 533 */       if (bootCP) {
/* 534 */         args.add("-Xbootclasspath/p:" +
/* 535 */             JavaCompilerHelper.getJarFile(EndpointContext.class) + File.pathSeparator +
/*     */
/* 537 */             JavaCompilerHelper.getJarFile(JAXBPermission.class));
/*     */       }
/*     */
/* 540 */       if (this.options.debug) {
/* 541 */         args.add("-g");
/*     */       }
/*     */
/* 544 */       if (this.options.encoding != null) {
/* 545 */         args.add("-encoding");
/* 546 */         args.add(this.options.encoding);
/*     */       }
/*     */
/* 549 */       if (this.options.javacOptions != null) {
/* 550 */         args.addAll(this.options.getJavacOptions(args, listener));
/*     */       }
/*     */
/* 553 */       for (int i = 0; i < sourceFiles.size(); i++) {
/* 554 */         args.add(sourceFiles.get(i));
/*     */       }
/*     */
/* 557 */       listener.message(WscompileMessages.WSIMPORT_COMPILING_CODE());
/* 558 */       if (this.options.verbose) {
/* 559 */         StringBuilder argstr = new StringBuilder();
/* 560 */         for (String arg : args) {
/* 561 */           argstr.append(arg).append(" ");
/*     */         }
/* 563 */         listener.message("javac " + argstr.toString());
/*     */       }
/*     */
/* 566 */       return JavaCompilerHelper.compile(args.<String>toArray(new String[args.size()]), this.out, receiver);
/*     */     }
/*     */
/* 569 */     return true;
/*     */   }
/*     */
/*     */   private String createClasspathString() {
/* 573 */     StringBuilder classpathStr = new StringBuilder(System.getProperty("java.class.path"));
/* 574 */     for (String s : this.options.cmdlineJars) {
/* 575 */       classpathStr.append(File.pathSeparator);
/* 576 */       classpathStr.append((new File(s)).toString());
/*     */     }
/* 578 */     return classpathStr.toString();
/*     */   }
/*     */
/*     */   protected void usage(Options options) {
/* 582 */     System.out.println(WscompileMessages.WSIMPORT_HELP("wsimport"));
/* 583 */     System.out.println(WscompileMessages.WSIMPORT_USAGE_EXTENSIONS());
/* 584 */     System.out.println(WscompileMessages.WSIMPORT_USAGE_EXAMPLES());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\WsimportTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
