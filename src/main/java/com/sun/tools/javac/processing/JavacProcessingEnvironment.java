/*      */ package com.sun.tools.javac.processing;
/*      */
/*      */ import com.sun.source.util.JavacTask;
/*      */ import com.sun.source.util.TaskEvent;
/*      */ import com.sun.tools.javac.api.BasicJavacTask;
/*      */ import com.sun.tools.javac.api.JavacTrees;
/*      */ import com.sun.tools.javac.api.MultiTaskListener;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.comp.CompileStates;
/*      */ import com.sun.tools.javac.file.FSInfo;
/*      */ import com.sun.tools.javac.file.JavacFileManager;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.main.JavaCompiler;
/*      */ import com.sun.tools.javac.main.Option;
/*      */ import com.sun.tools.javac.model.JavacElements;
/*      */ import com.sun.tools.javac.model.JavacTypes;
/*      */ import com.sun.tools.javac.parser.Tokens;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Abort;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.ClientCodeException;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Convert;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JavacMessages;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.ServiceLoader;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.ServiceConfigurationError;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.annotation.processing.Filer;
/*      */ import javax.annotation.processing.Messager;
/*      */ import javax.annotation.processing.ProcessingEnvironment;
/*      */ import javax.annotation.processing.Processor;
/*      */ import javax.annotation.processing.RoundEnvironment;
/*      */ import javax.lang.model.SourceVersion;
/*      */ import javax.lang.model.element.AnnotationMirror;
/*      */ import javax.lang.model.element.Element;
/*      */ import javax.lang.model.element.ExecutableElement;
/*      */ import javax.lang.model.element.PackageElement;
/*      */ import javax.lang.model.element.TypeElement;
/*      */ import javax.lang.model.util.ElementScanner8;
/*      */ import javax.lang.model.util.Elements;
/*      */ import javax.lang.model.util.Types;
/*      */ import javax.tools.Diagnostic;
/*      */ import javax.tools.DiagnosticListener;
/*      */ import javax.tools.JavaFileManager;
/*      */ import javax.tools.JavaFileObject;
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
/*      */ public class JavacProcessingEnvironment
/*      */   implements ProcessingEnvironment, Closeable
/*      */ {
/*      */   private final Options options;
/*      */   private final boolean printProcessorInfo;
/*      */   private final boolean printRounds;
/*      */   private final boolean verbose;
/*      */   private final boolean lint;
/*      */   private final boolean fatalErrors;
/*      */   private final boolean werror;
/*      */   private final boolean showResolveErrors;
/*      */   private final JavacFiler filer;
/*      */   private final JavacMessager messager;
/*      */   private final JavacElements elementUtils;
/*      */   private final JavacTypes typeUtils;
/*      */   private DiscoveredProcessors discoveredProcs;
/*      */   private final Map<String, String> processorOptions;
/*      */   private final Set<String> unmatchedProcessorOptions;
/*      */   private final Set<String> platformAnnotations;
/*  130 */   private Set<Symbol.PackageSymbol> specifiedPackages = Collections.emptySet();
/*      */
/*      */
/*      */   Log log;
/*      */
/*      */
/*      */   JCDiagnostic.Factory diags;
/*      */
/*      */
/*      */   Source source;
/*      */
/*      */
/*      */   private ClassLoader processorClassLoader;
/*      */
/*      */
/*      */   private SecurityException processorClassLoaderException;
/*      */
/*      */
/*      */   private JavacMessages messages;
/*      */
/*      */
/*      */   private MultiTaskListener taskListener;
/*      */
/*      */
/*      */   private Context context;
/*      */
/*      */
/*      */
/*      */   public static JavacProcessingEnvironment instance(Context paramContext) {
/*  159 */     JavacProcessingEnvironment javacProcessingEnvironment = (JavacProcessingEnvironment)paramContext.get(JavacProcessingEnvironment.class);
/*  160 */     if (javacProcessingEnvironment == null)
/*  161 */       javacProcessingEnvironment = new JavacProcessingEnvironment(paramContext);
/*  162 */     return javacProcessingEnvironment;
/*      */   }
/*      */
/*      */   protected JavacProcessingEnvironment(Context paramContext) {
/*  166 */     this.context = paramContext;
/*  167 */     paramContext.put(JavacProcessingEnvironment.class, this);
/*  168 */     this.log = Log.instance(paramContext);
/*  169 */     this.source = Source.instance(paramContext);
/*  170 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  171 */     this.options = Options.instance(paramContext);
/*  172 */     this.printProcessorInfo = this.options.isSet(Option.XPRINTPROCESSORINFO);
/*  173 */     this.printRounds = this.options.isSet(Option.XPRINTROUNDS);
/*  174 */     this.verbose = this.options.isSet(Option.VERBOSE);
/*  175 */     this.lint = Lint.instance(paramContext).isEnabled(Lint.LintCategory.PROCESSING);
/*  176 */     if (this.options.isSet(Option.PROC, "only") || this.options.isSet(Option.XPRINT)) {
/*  177 */       JavaCompiler javaCompiler = JavaCompiler.instance(paramContext);
/*  178 */       javaCompiler.shouldStopPolicyIfNoError = CompileStates.CompileState.PROCESS;
/*      */     }
/*  180 */     this.fatalErrors = this.options.isSet("fatalEnterError");
/*  181 */     this.showResolveErrors = this.options.isSet("showResolveErrors");
/*  182 */     this.werror = this.options.isSet(Option.WERROR);
/*  183 */     this.platformAnnotations = initPlatformAnnotations();
/*      */
/*      */
/*      */
/*  187 */     this.filer = new JavacFiler(paramContext);
/*  188 */     this.messager = new JavacMessager(paramContext, this);
/*  189 */     this.elementUtils = JavacElements.instance(paramContext);
/*  190 */     this.typeUtils = JavacTypes.instance(paramContext);
/*  191 */     this.processorOptions = initProcessorOptions(paramContext);
/*  192 */     this.unmatchedProcessorOptions = initUnmatchedProcessorOptions();
/*  193 */     this.messages = JavacMessages.instance(paramContext);
/*  194 */     this.taskListener = MultiTaskListener.instance(paramContext);
/*  195 */     initProcessorClassLoader();
/*      */   }
/*      */
/*      */   public void setProcessors(Iterable<? extends Processor> paramIterable) {
/*  199 */     Assert.checkNull(this.discoveredProcs);
/*  200 */     initProcessorIterator(this.context, paramIterable);
/*      */   }
/*      */
/*      */   private Set<String> initPlatformAnnotations() {
/*  204 */     HashSet<String> hashSet = new HashSet();
/*  205 */     hashSet.add("java.lang.Deprecated");
/*  206 */     hashSet.add("java.lang.Override");
/*  207 */     hashSet.add("java.lang.SuppressWarnings");
/*  208 */     hashSet.add("java.lang.annotation.Documented");
/*  209 */     hashSet.add("java.lang.annotation.Inherited");
/*  210 */     hashSet.add("java.lang.annotation.Retention");
/*  211 */     hashSet.add("java.lang.annotation.Target");
/*  212 */     return Collections.unmodifiableSet(hashSet);
/*      */   }
/*      */
/*      */   private void initProcessorClassLoader() {
/*  216 */     JavaFileManager javaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/*      */
/*      */     try {
/*  219 */       this
/*      */
/*  221 */         .processorClassLoader = javaFileManager.hasLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) ? javaFileManager.getClassLoader(StandardLocation.ANNOTATION_PROCESSOR_PATH) : javaFileManager.getClassLoader(StandardLocation.CLASS_PATH);
/*      */
/*  223 */       if (this.processorClassLoader != null && this.processorClassLoader instanceof Closeable) {
/*  224 */         JavaCompiler javaCompiler = JavaCompiler.instance(this.context);
/*  225 */         javaCompiler.closeables = javaCompiler.closeables.prepend(this.processorClassLoader);
/*      */       }
/*  227 */     } catch (SecurityException securityException) {
/*  228 */       this.processorClassLoaderException = securityException;
/*      */     }
/*      */   }
/*      */   private void initProcessorIterator(Context paramContext, Iterable<? extends Processor> paramIterable) {
/*      */     Iterator<? extends Processor> iterator;
/*  233 */     Log log = Log.instance(paramContext);
/*      */
/*      */
/*  236 */     if (this.options.isSet(Option.XPRINT)) {
/*      */       try {
/*  238 */         Processor processor = PrintingProcessor.class.newInstance();
/*  239 */         iterator = List.of(processor).iterator();
/*  240 */       } catch (Throwable throwable) {
/*  241 */         AssertionError assertionError = new AssertionError("Problem instantiating PrintingProcessor.");
/*      */
/*  243 */         assertionError.initCause(throwable);
/*  244 */         throw assertionError;
/*      */       }
/*  246 */     } else if (paramIterable != null) {
/*  247 */       iterator = paramIterable.iterator();
/*      */     } else {
/*  249 */       String str = this.options.get(Option.PROCESSOR);
/*  250 */       if (this.processorClassLoaderException == null) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*  256 */         if (str != null) {
/*  257 */           iterator = new NameProcessIterator(str, this.processorClassLoader, log);
/*      */         } else {
/*  259 */           iterator = new ServiceIterator(this.processorClassLoader, log);
/*      */
/*      */         }
/*      */
/*      */
/*      */       }
/*      */       else {
/*      */
/*      */
/*  268 */         iterator = handleServiceLoaderUnavailability("proc.cant.create.loader", this.processorClassLoaderException);
/*      */       }
/*      */     }
/*      */
/*  272 */     this.discoveredProcs = new DiscoveredProcessors(iterator);
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
/*      */   private Iterator<Processor> handleServiceLoaderUnavailability(String paramString, Exception paramException) {
/*  287 */     JavaFileManager javaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/*      */
/*  289 */     if (javaFileManager instanceof JavacFileManager) {
/*  290 */       JavacFileManager javacFileManager = (JavacFileManager)javaFileManager;
/*      */
/*      */
/*  293 */       Iterable<? extends File> iterable = javaFileManager.hasLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) ? javacFileManager.getLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH) : javacFileManager.getLocation(StandardLocation.CLASS_PATH);
/*      */
/*  295 */       if (needClassLoader(this.options.get(Option.PROCESSOR), iterable)) {
/*  296 */         handleException(paramString, paramException);
/*      */       }
/*      */     } else {
/*  299 */       handleException(paramString, paramException);
/*      */     }
/*      */
/*  302 */     List<?> list = Collections.emptyList();
/*  303 */     return (Iterator)list.iterator();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void handleException(String paramString, Exception paramException) {
/*  311 */     if (paramException != null) {
/*  312 */       this.log.error(paramString, new Object[] { paramException.getLocalizedMessage() });
/*  313 */       throw new Abort(paramException);
/*      */     }
/*  315 */     this.log.error(paramString, new Object[0]);
/*  316 */     throw new Abort();
/*      */   }
/*      */
/*      */
/*      */   private class ServiceIterator
/*      */     implements Iterator<Processor>
/*      */   {
/*      */     private Iterator<Processor> iterator;
/*      */
/*      */     private Log log;
/*      */
/*      */     private ServiceLoader<Processor> loader;
/*      */
/*      */
/*      */     ServiceIterator(ClassLoader param1ClassLoader, Log param1Log) {
/*  331 */       this.log = param1Log;
/*      */       try {
/*      */         try {
/*  334 */           this.loader = ServiceLoader.load(Processor.class, param1ClassLoader);
/*  335 */           this.iterator = this.loader.iterator();
/*  336 */         } catch (Exception exception) {
/*      */
/*  338 */           this.iterator = JavacProcessingEnvironment.this.handleServiceLoaderUnavailability("proc.no.service", null);
/*      */         }
/*  340 */       } catch (Throwable throwable) {
/*  341 */         param1Log.error("proc.service.problem", new Object[0]);
/*  342 */         throw new Abort(throwable);
/*      */       }
/*      */     }
/*      */
/*      */     public boolean hasNext() {
/*      */       try {
/*  348 */         return this.iterator.hasNext();
/*  349 */       } catch (ServiceConfigurationError serviceConfigurationError) {
/*  350 */         this.log.error("proc.bad.config.file", new Object[] { serviceConfigurationError.getLocalizedMessage() });
/*  351 */         throw new Abort(serviceConfigurationError);
/*  352 */       } catch (Throwable throwable) {
/*  353 */         throw new Abort(throwable);
/*      */       }
/*      */     }
/*      */
/*      */     public Processor next() {
/*      */       try {
/*  359 */         return this.iterator.next();
/*  360 */       } catch (ServiceConfigurationError serviceConfigurationError) {
/*  361 */         this.log.error("proc.bad.config.file", new Object[] { serviceConfigurationError.getLocalizedMessage() });
/*  362 */         throw new Abort(serviceConfigurationError);
/*  363 */       } catch (Throwable throwable) {
/*  364 */         throw new Abort(throwable);
/*      */       }
/*      */     }
/*      */
/*      */     public void remove() {
/*  369 */       throw new UnsupportedOperationException();
/*      */     }
/*      */
/*      */     public void close() {
/*  373 */       if (this.loader != null) {
/*      */         try {
/*  375 */           this.loader.reload();
/*  376 */         } catch (Exception exception) {}
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private static class NameProcessIterator
/*      */     implements Iterator<Processor>
/*      */   {
/*  385 */     Processor nextProc = null;
/*      */     Iterator<String> names;
/*      */     ClassLoader processorCL;
/*      */     Log log;
/*      */
/*      */     NameProcessIterator(String param1String, ClassLoader param1ClassLoader, Log param1Log) {
/*  391 */       this.names = Arrays.<String>asList(param1String.split(",")).iterator();
/*  392 */       this.processorCL = param1ClassLoader;
/*  393 */       this.log = param1Log;
/*      */     }
/*      */     public boolean hasNext() {
/*      */       Processor processor;
/*  397 */       if (this.nextProc != null) {
/*  398 */         return true;
/*      */       }
/*  400 */       if (!this.names.hasNext()) {
/*  401 */         return false;
/*      */       }
/*  403 */       String str = this.names.next();
/*      */
/*      */
/*      */
/*      */       try {
/*      */         try {
/*  409 */           processor = (Processor)this.processorCL.loadClass(str).newInstance();
/*  410 */         } catch (ClassNotFoundException classNotFoundException) {
/*  411 */           this.log.error("proc.processor.not.found", new Object[] { str });
/*  412 */           return false;
/*  413 */         } catch (ClassCastException classCastException) {
/*  414 */           this.log.error("proc.processor.wrong.type", new Object[] { str });
/*  415 */           return false;
/*  416 */         } catch (Exception exception) {
/*  417 */           this.log.error("proc.processor.cant.instantiate", new Object[] { str });
/*  418 */           return false;
/*      */         }
/*  420 */       } catch (ClientCodeException clientCodeException) {
/*  421 */         throw clientCodeException;
/*  422 */       } catch (Throwable throwable) {
/*  423 */         throw new AnnotationProcessingError(throwable);
/*      */       }
/*  425 */       this.nextProc = processor;
/*  426 */       return true;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public Processor next() {
/*  433 */       if (hasNext()) {
/*  434 */         Processor processor = this.nextProc;
/*  435 */         this.nextProc = null;
/*  436 */         return processor;
/*      */       }
/*  438 */       throw new NoSuchElementException();
/*      */     }
/*      */
/*      */     public void remove() {
/*  442 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */
/*      */   public boolean atLeastOneProcessor() {
/*  447 */     return this.discoveredProcs.iterator().hasNext();
/*      */   }
/*      */
/*      */   private Map<String, String> initProcessorOptions(Context paramContext) {
/*  451 */     Options options = Options.instance(paramContext);
/*  452 */     Set set = options.keySet();
/*  453 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*      */
/*  455 */     for (String str : set) {
/*  456 */       if (str.startsWith("-A") && str.length() > 2) {
/*  457 */         int i = str.indexOf('=');
/*  458 */         String str1 = null;
/*  459 */         String str2 = null;
/*      */
/*  461 */         if (i == -1) {
/*  462 */           str1 = str.substring(2);
/*  463 */         } else if (i >= 3) {
/*  464 */           str1 = str.substring(2, i);
/*      */
/*  466 */           str2 = (i < str.length() - 1) ? str.substring(i + 1) : null;
/*      */         }
/*  468 */         linkedHashMap.put(str1, str2);
/*      */       }
/*      */     }
/*      */
/*  472 */     return (Map)Collections.unmodifiableMap(linkedHashMap);
/*      */   }
/*      */
/*      */   private Set<String> initUnmatchedProcessorOptions() {
/*  476 */     HashSet<String> hashSet = new HashSet();
/*  477 */     hashSet.addAll(this.processorOptions.keySet());
/*  478 */     return hashSet;
/*      */   }
/*      */
/*      */
/*      */
/*      */   static class ProcessorState
/*      */   {
/*      */     public Processor processor;
/*      */
/*      */
/*      */     public boolean contributed;
/*      */
/*      */     private ArrayList<Pattern> supportedAnnotationPatterns;
/*      */
/*      */     private ArrayList<String> supportedOptionNames;
/*      */
/*      */
/*      */     ProcessorState(Processor param1Processor, Log param1Log, Source param1Source, ProcessingEnvironment param1ProcessingEnvironment) {
/*  496 */       this.processor = param1Processor;
/*  497 */       this.contributed = false;
/*      */
/*      */       try {
/*  500 */         this.processor.init(param1ProcessingEnvironment);
/*      */
/*  502 */         checkSourceVersionCompatibility(param1Source, param1Log);
/*      */
/*  504 */         this.supportedAnnotationPatterns = new ArrayList<>();
/*  505 */         for (String str : this.processor.getSupportedAnnotationTypes()) {
/*  506 */           this.supportedAnnotationPatterns.add(JavacProcessingEnvironment.importStringToPattern(str, this.processor, param1Log));
/*      */         }
/*      */
/*      */
/*      */
/*  511 */         this.supportedOptionNames = new ArrayList<>();
/*  512 */         for (String str : this.processor.getSupportedOptions()) {
/*  513 */           if (checkOptionName(str, param1Log)) {
/*  514 */             this.supportedOptionNames.add(str);
/*      */           }
/*      */         }
/*  517 */       } catch (ClientCodeException clientCodeException) {
/*  518 */         throw clientCodeException;
/*  519 */       } catch (Throwable throwable) {
/*  520 */         throw new AnnotationProcessingError(throwable);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private void checkSourceVersionCompatibility(Source param1Source, Log param1Log) {
/*  531 */       SourceVersion sourceVersion = this.processor.getSupportedSourceVersion();
/*      */
/*  533 */       if (sourceVersion.compareTo(Source.toSourceVersion(param1Source)) < 0) {
/*  534 */         param1Log.warning("proc.processor.incompatible.source.version", new Object[] { sourceVersion, this.processor
/*      */
/*  536 */               .getClass().getName(), param1Source.name });
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     private boolean checkOptionName(String param1String, Log param1Log) {
/*  542 */       boolean bool = JavacProcessingEnvironment.isValidOptionName(param1String);
/*  543 */       if (!bool)
/*  544 */         param1Log.error("proc.processor.bad.option.name", new Object[] { param1String, this.processor
/*      */
/*  546 */               .getClass().getName() });
/*  547 */       return bool;
/*      */     }
/*      */
/*      */     public boolean annotationSupported(String param1String) {
/*  551 */       for (Pattern pattern : this.supportedAnnotationPatterns) {
/*  552 */         if (pattern.matcher(param1String).matches())
/*  553 */           return true;
/*      */       }
/*  555 */       return false;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public void removeSupportedOptions(Set<String> param1Set) {
/*  562 */       param1Set.removeAll(this.supportedOptionNames);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   class DiscoveredProcessors
/*      */     implements Iterable<ProcessorState>
/*      */   {
/*      */     Iterator<? extends Processor> processorIterator;
/*      */
/*      */     ArrayList<ProcessorState> procStateList;
/*      */
/*      */
/*      */     class ProcessorStateIterator
/*      */       implements Iterator<ProcessorState>
/*      */     {
/*      */       DiscoveredProcessors psi;
/*      */       Iterator<ProcessorState> innerIter;
/*      */       boolean onProcInterator;
/*      */
/*      */       ProcessorStateIterator(DiscoveredProcessors param2DiscoveredProcessors1) {
/*  583 */         this.psi = param2DiscoveredProcessors1;
/*  584 */         this.innerIter = param2DiscoveredProcessors1.procStateList.iterator();
/*  585 */         this.onProcInterator = false;
/*      */       }
/*      */
/*      */       public ProcessorState next() {
/*  589 */         if (!this.onProcInterator) {
/*  590 */           if (this.innerIter.hasNext()) {
/*  591 */             return this.innerIter.next();
/*      */           }
/*  593 */           this.onProcInterator = true;
/*      */         }
/*      */
/*  596 */         if (this.psi.processorIterator.hasNext()) {
/*  597 */           ProcessorState processorState = new ProcessorState(this.psi.processorIterator.next(), JavacProcessingEnvironment.this.log, JavacProcessingEnvironment.this.source, JavacProcessingEnvironment.this);
/*      */
/*  599 */           this.psi.procStateList.add(processorState);
/*  600 */           return processorState;
/*      */         }
/*  602 */         throw new NoSuchElementException();
/*      */       }
/*      */
/*      */       public boolean hasNext() {
/*  606 */         if (this.onProcInterator) {
/*  607 */           return this.psi.processorIterator.hasNext();
/*      */         }
/*  609 */         return (this.innerIter.hasNext() || this.psi.processorIterator.hasNext());
/*      */       }
/*      */
/*      */       public void remove() {
/*  613 */         throw new UnsupportedOperationException();
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       public void runContributingProcs(RoundEnvironment param2RoundEnvironment) {
/*  622 */         if (!this.onProcInterator) {
/*  623 */           Set<?> set = Collections.emptySet();
/*  624 */           while (this.innerIter.hasNext()) {
/*  625 */             ProcessorState processorState = this.innerIter.next();
/*  626 */             if (processorState.contributed) {
/*  627 */               JavacProcessingEnvironment.this.callProcessor(processorState.processor, (Set)set, param2RoundEnvironment);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     public ProcessorStateIterator iterator() {
/*  637 */       return new ProcessorStateIterator(this);
/*      */     }
/*      */
/*      */     DiscoveredProcessors(Iterator<? extends Processor> param1Iterator) {
/*  641 */       this.processorIterator = param1Iterator;
/*  642 */       this.procStateList = new ArrayList<>();
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public void close() {
/*  649 */       if (this.processorIterator != null && this.processorIterator instanceof ServiceIterator)
/*      */       {
/*  651 */         ((ServiceIterator)this.processorIterator).close(); }
/*      */     } }
/*      */   class ProcessorStateIterator implements Iterator<ProcessorState> {
/*      */     DiscoveredProcessors psi;
/*      */     Iterator<ProcessorState> innerIter;
/*      */     boolean onProcInterator; ProcessorStateIterator(DiscoveredProcessors param1DiscoveredProcessors1) { this.psi = param1DiscoveredProcessors1; this.innerIter = param1DiscoveredProcessors1.procStateList.iterator(); this.onProcInterator = false; } public ProcessorState next() { if (!this.onProcInterator) { if (this.innerIter.hasNext())
/*      */           return this.innerIter.next();  this.onProcInterator = true; }  if (this.psi.processorIterator.hasNext()) { ProcessorState processorState = new ProcessorState(this.psi.processorIterator.next(), JavacProcessingEnvironment.this.log, JavacProcessingEnvironment.this.source, JavacProcessingEnvironment.this); this.psi.procStateList.add(processorState); return processorState; }  throw new NoSuchElementException(); } public boolean hasNext() { if (this.onProcInterator)
/*      */         return this.psi.processorIterator.hasNext();  return (this.innerIter.hasNext() || this.psi.processorIterator.hasNext()); } public void remove() { throw new UnsupportedOperationException(); } public void runContributingProcs(RoundEnvironment param1RoundEnvironment) { if (!this.onProcInterator) { Set<?> set = Collections.emptySet(); while (this.innerIter.hasNext()) { ProcessorState processorState = this.innerIter.next(); if (processorState.contributed)
/*      */             JavacProcessingEnvironment.this.callProcessor(processorState.processor, (Set)set, param1RoundEnvironment);  }  }
/*      */        }
/*  661 */   } private void discoverAndRunProcs(Context paramContext, Set<TypeElement> paramSet, List<Symbol.ClassSymbol> paramList, List<Symbol.PackageSymbol> paramList1) { HashMap<Object, Object> hashMap = new HashMap<>(paramSet.size());
/*      */
/*  663 */     for (TypeElement typeElement : paramSet) {
/*  664 */       hashMap.put(typeElement.getQualifiedName().toString(), typeElement);
/*      */     }
/*      */
/*      */
/*      */
/*  669 */     if (hashMap.size() == 0) {
/*  670 */       hashMap.put("", null);
/*      */     }
/*  672 */     DiscoveredProcessors.ProcessorStateIterator processorStateIterator = this.discoveredProcs.iterator();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  679 */     LinkedHashSet<Symbol.ClassSymbol> linkedHashSet = new LinkedHashSet();
/*  680 */     linkedHashSet.addAll((Collection<? extends Symbol.ClassSymbol>)paramList);
/*  681 */     linkedHashSet.addAll((Collection<?>)paramList1);
/*  682 */     Set<Symbol.ClassSymbol> set = Collections.unmodifiableSet(linkedHashSet);
/*      */
/*  684 */     JavacRoundEnvironment javacRoundEnvironment = new JavacRoundEnvironment(false, false, (Set)set, this);
/*      */
/*      */
/*      */
/*      */
/*  689 */     while (hashMap.size() > 0 && processorStateIterator.hasNext()) {
/*  690 */       ProcessorState processorState = processorStateIterator.next();
/*  691 */       HashSet<String> hashSet = new HashSet();
/*  692 */       LinkedHashSet<TypeElement> linkedHashSet1 = new LinkedHashSet();
/*      */
/*  694 */       for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
/*  695 */         String str = (String)entry.getKey();
/*  696 */         if (processorState.annotationSupported(str)) {
/*  697 */           hashSet.add(str);
/*  698 */           TypeElement typeElement = (TypeElement)entry.getValue();
/*  699 */           if (typeElement != null) {
/*  700 */             linkedHashSet1.add(typeElement);
/*      */           }
/*      */         }
/*      */       }
/*  704 */       if (hashSet.size() > 0 || processorState.contributed) {
/*  705 */         boolean bool = callProcessor(processorState.processor, linkedHashSet1, javacRoundEnvironment);
/*  706 */         processorState.contributed = true;
/*  707 */         processorState.removeSupportedOptions(this.unmatchedProcessorOptions);
/*      */
/*  709 */         if (this.printProcessorInfo || this.verbose) {
/*  710 */           this.log.printLines("x.print.processor.info", new Object[] { processorState.processor
/*  711 */                 .getClass().getName(), hashSet
/*  712 */                 .toString(),
/*  713 */                 Boolean.valueOf(bool) });
/*      */         }
/*      */
/*  716 */         if (bool) {
/*  717 */           hashMap.keySet().removeAll(hashSet);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  722 */     hashMap.remove("");
/*      */
/*  724 */     if (this.lint && hashMap.size() > 0) {
/*      */
/*  726 */       hashMap.keySet().removeAll(this.platformAnnotations);
/*  727 */       if (hashMap.size() > 0) {
/*  728 */         this.log = Log.instance(paramContext);
/*  729 */         this.log.warning("proc.annotations.without.processors", new Object[] { hashMap
/*  730 */               .keySet() });
/*      */       }
/*      */     }
/*      */
/*      */
/*  735 */     processorStateIterator.runContributingProcs(javacRoundEnvironment);
/*      */
/*      */
/*  738 */     if (this.options.isSet("displayFilerState")) {
/*  739 */       this.filer.displayState();
/*      */     } }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static class ComputeAnnotationSet
/*      */     extends ElementScanner8<Set<TypeElement>, Set<TypeElement>>
/*      */   {
/*      */     public ComputeAnnotationSet(Elements param1Elements) {
/*  752 */       this.elements = param1Elements;
/*      */     }
/*      */
/*      */     final Elements elements;
/*      */
/*      */     public Set<TypeElement> visitPackage(PackageElement param1PackageElement, Set<TypeElement> param1Set) {
/*  758 */       return param1Set;
/*      */     }
/*      */
/*      */
/*      */
/*      */     public Set<TypeElement> visitType(TypeElement param1TypeElement, Set<TypeElement> param1Set) {
/*  764 */       scan((Iterable)param1TypeElement.getTypeParameters(), param1Set);
/*  765 */       return super.visitType(param1TypeElement, param1Set);
/*      */     }
/*      */
/*      */
/*      */
/*      */     public Set<TypeElement> visitExecutable(ExecutableElement param1ExecutableElement, Set<TypeElement> param1Set) {
/*  771 */       scan((Iterable)param1ExecutableElement.getTypeParameters(), param1Set);
/*  772 */       return super.visitExecutable(param1ExecutableElement, param1Set);
/*      */     }
/*      */
/*      */
/*      */     void addAnnotations(Element param1Element, Set<TypeElement> param1Set) {
/*  777 */       for (AnnotationMirror annotationMirror : this.elements.getAllAnnotationMirrors(param1Element)) {
/*  778 */         Element element = annotationMirror.getAnnotationType().asElement();
/*  779 */         param1Set.add((TypeElement)element);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public Set<TypeElement> scan(Element param1Element, Set<TypeElement> param1Set) {
/*  785 */       addAnnotations(param1Element, param1Set);
/*  786 */       return super.scan(param1Element, param1Set);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   private boolean callProcessor(Processor paramProcessor, Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment) {
/*      */     try {
/*  794 */       return paramProcessor.process(paramSet, paramRoundEnvironment);
/*  795 */     } catch (ClassReader.BadClassFile badClassFile) {
/*  796 */       this.log.error("proc.cant.access.1", new Object[] { badClassFile.sym, badClassFile.getDetailValue() });
/*  797 */       return false;
/*  798 */     } catch (Symbol.CompletionFailure completionFailure) {
/*  799 */       StringWriter stringWriter = new StringWriter();
/*  800 */       completionFailure.printStackTrace(new PrintWriter(stringWriter));
/*  801 */       this.log.error("proc.cant.access", new Object[] { completionFailure.sym, completionFailure.getDetailValue(), stringWriter.toString() });
/*  802 */       return false;
/*  803 */     } catch (ClientCodeException clientCodeException) {
/*  804 */       throw clientCodeException;
/*  805 */     } catch (Throwable throwable) {
/*  806 */       throw new AnnotationProcessingError(throwable);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   class Round
/*      */   {
/*      */     final int number;
/*      */
/*      */
/*      */     final Context context;
/*      */
/*      */
/*      */     final JavaCompiler compiler;
/*      */
/*      */
/*      */     final Log log;
/*      */
/*      */
/*      */     final Log.DeferredDiagnosticHandler deferredDiagnosticHandler;
/*      */
/*      */     List<JCTree.JCCompilationUnit> roots;
/*      */
/*      */     Map<String, JavaFileObject> genClassFiles;
/*      */
/*      */     Set<TypeElement> annotationsPresent;
/*      */
/*      */     List<Symbol.ClassSymbol> topLevelClasses;
/*      */
/*      */     List<Symbol.PackageSymbol> packageInfoFiles;
/*      */
/*      */
/*      */     private Round(Context param1Context, int param1Int1, int param1Int2, int param1Int3, Log.DeferredDiagnosticHandler param1DeferredDiagnosticHandler) {
/*  840 */       this.context = param1Context;
/*  841 */       this.number = param1Int1;
/*      */
/*  843 */       this.compiler = JavaCompiler.instance(param1Context);
/*  844 */       this.log = Log.instance(param1Context);
/*  845 */       this.log.nerrors = param1Int2;
/*  846 */       this.log.nwarnings = param1Int3;
/*  847 */       if (param1Int1 == 1) {
/*  848 */         Assert.checkNonNull(param1DeferredDiagnosticHandler);
/*  849 */         this.deferredDiagnosticHandler = param1DeferredDiagnosticHandler;
/*      */       } else {
/*  851 */         this.deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log);
/*      */       }
/*      */
/*      */
/*  855 */       JavacProcessingEnvironment.this.context = param1Context;
/*      */
/*      */
/*  858 */       this.topLevelClasses = List.nil();
/*  859 */       this.packageInfoFiles = List.nil();
/*      */     }
/*      */
/*      */
/*      */
/*      */     Round(Context param1Context, List<JCTree.JCCompilationUnit> param1List, List<Symbol.ClassSymbol> param1List1, Log.DeferredDiagnosticHandler param1DeferredDiagnosticHandler) {
/*  865 */       this(param1Context, 1, 0, 0, param1DeferredDiagnosticHandler);
/*  866 */       this.roots = param1List;
/*  867 */       this.genClassFiles = new HashMap<>();
/*      */
/*  869 */       this.compiler.todo.clear();
/*      */
/*      */
/*      */
/*      */
/*  874 */       this
/*  875 */         .topLevelClasses = JavacProcessingEnvironment.this.getTopLevelClasses(param1List).prependList(param1List1.reverse());
/*      */
/*  877 */       this.packageInfoFiles = JavacProcessingEnvironment.this.getPackageInfoFiles(param1List);
/*      */
/*  879 */       findAnnotationsPresent();
/*      */     }
/*      */
/*      */
/*      */
/*      */     private Round(Round param1Round, Set<JavaFileObject> param1Set, Map<String, JavaFileObject> param1Map) {
/*  885 */       this(param1Round.nextContext(), param1Round.number + 1, param1Round.compiler.log.nerrors, param1Round.compiler.log.nwarnings, null);
/*      */
/*      */
/*      */
/*      */
/*  890 */       this.genClassFiles = param1Round.genClassFiles;
/*      */
/*  892 */       List list = this.compiler.parseFiles(param1Set);
/*  893 */       this.roots = JavacProcessingEnvironment.cleanTrees((List)param1Round.roots).appendList(list);
/*      */
/*      */
/*  896 */       if (unrecoverableError()) {
/*      */         return;
/*      */       }
/*  899 */       enterClassFiles(this.genClassFiles);
/*  900 */       List<Symbol.ClassSymbol> list1 = enterClassFiles(param1Map);
/*  901 */       this.genClassFiles.putAll(param1Map);
/*  902 */       enterTrees(this.roots);
/*      */
/*  904 */       if (unrecoverableError()) {
/*      */         return;
/*      */       }
/*  907 */       this.topLevelClasses = (List)JavacProcessingEnvironment.join((List)JavacProcessingEnvironment.this
/*  908 */           .getTopLevelClasses(list), (List)JavacProcessingEnvironment.this
/*  909 */           .getTopLevelClassesFromClasses(list1));
/*      */
/*  911 */       this.packageInfoFiles = (List)JavacProcessingEnvironment.join((List)JavacProcessingEnvironment.this
/*  912 */           .getPackageInfoFiles(list), (List)JavacProcessingEnvironment.this
/*  913 */           .getPackageInfoFilesFromClasses(list1));
/*      */
/*  915 */       findAnnotationsPresent();
/*      */     }
/*      */
/*      */
/*      */     Round next(Set<JavaFileObject> param1Set, Map<String, JavaFileObject> param1Map) {
/*      */       try {
/*  921 */         return new Round(this, param1Set, param1Map);
/*      */       } finally {
/*  923 */         this.compiler.close(false);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     JavaCompiler finalCompiler() {
/*      */       try {
/*  930 */         Context context = nextContext();
/*  931 */         JavacProcessingEnvironment.this.context = context;
/*  932 */         JavaCompiler javaCompiler = JavaCompiler.instance(context);
/*  933 */         javaCompiler.log.initRound(this.compiler.log);
/*  934 */         return javaCompiler;
/*      */       } finally {
/*  936 */         this.compiler.close(false);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     int errorCount() {
/*  944 */       return this.compiler.errorCount();
/*      */     }
/*      */
/*      */
/*      */     int warningCount() {
/*  949 */       return this.compiler.warningCount();
/*      */     }
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
/*      */     boolean unrecoverableError() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield this$0 : Lcom/sun/tools/javac/processing/JavacProcessingEnvironment;
/*      */       //   4: invokestatic access$1000 : (Lcom/sun/tools/javac/processing/JavacProcessingEnvironment;)Lcom/sun/tools/javac/processing/JavacMessager;
/*      */       //   7: invokevirtual errorRaised : ()Z
/*      */       //   10: ifeq -> 15
/*      */       //   13: iconst_1
/*      */       //   14: ireturn
/*      */       //   15: aload_0
/*      */       //   16: getfield deferredDiagnosticHandler : Lcom/sun/tools/javac/util/Log$DeferredDiagnosticHandler;
/*      */       //   19: invokevirtual getDiagnostics : ()Ljava/util/Queue;
/*      */       //   22: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */       //   27: astore_1
/*      */       //   28: aload_1
/*      */       //   29: invokeinterface hasNext : ()Z
/*      */       //   34: ifeq -> 121
/*      */       //   37: aload_1
/*      */       //   38: invokeinterface next : ()Ljava/lang/Object;
/*      */       //   43: checkcast com/sun/tools/javac/util/JCDiagnostic
/*      */       //   46: astore_2
/*      */       //   47: getstatic com/sun/tools/javac/processing/JavacProcessingEnvironment$2.$SwitchMap$javax$tools$Diagnostic$Kind : [I
/*      */       //   50: aload_2
/*      */       //   51: invokevirtual getKind : ()Ljavax/tools/Diagnostic$Kind;
/*      */       //   54: invokevirtual ordinal : ()I
/*      */       //   57: iaload
/*      */       //   58: lookupswitch default -> 118, 1 -> 84, 2 -> 96
/*      */       //   84: aload_0
/*      */       //   85: getfield this$0 : Lcom/sun/tools/javac/processing/JavacProcessingEnvironment;
/*      */       //   88: invokestatic access$1100 : (Lcom/sun/tools/javac/processing/JavacProcessingEnvironment;)Z
/*      */       //   91: ifeq -> 118
/*      */       //   94: iconst_1
/*      */       //   95: ireturn
/*      */       //   96: aload_0
/*      */       //   97: getfield this$0 : Lcom/sun/tools/javac/processing/JavacProcessingEnvironment;
/*      */       //   100: invokestatic access$1200 : (Lcom/sun/tools/javac/processing/JavacProcessingEnvironment;)Z
/*      */       //   103: ifne -> 116
/*      */       //   106: aload_2
/*      */       //   107: getstatic com/sun/tools/javac/util/JCDiagnostic$DiagnosticFlag.RECOVERABLE : Lcom/sun/tools/javac/util/JCDiagnostic$DiagnosticFlag;
/*      */       //   110: invokevirtual isFlagSet : (Lcom/sun/tools/javac/util/JCDiagnostic$DiagnosticFlag;)Z
/*      */       //   113: ifne -> 118
/*      */       //   116: iconst_1
/*      */       //   117: ireturn
/*      */       //   118: goto -> 28
/*      */       //   121: iconst_0
/*      */       //   122: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #954	-> 0
/*      */       //   #955	-> 13
/*      */       //   #957	-> 15
/*      */       //   #958	-> 47
/*      */       //   #960	-> 84
/*      */       //   #961	-> 94
/*      */       //   #965	-> 96
/*      */       //   #966	-> 116
/*      */       //   #969	-> 118
/*      */       //   #971	-> 121
/*      */     }
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
/*      */     void findAnnotationsPresent() {
/*  977 */       ComputeAnnotationSet computeAnnotationSet = new ComputeAnnotationSet((Elements)JavacProcessingEnvironment.this.elementUtils);
/*      */
/*  979 */       this.annotationsPresent = new LinkedHashSet<>();
/*  980 */       for (Symbol.ClassSymbol classSymbol : this.topLevelClasses)
/*  981 */         computeAnnotationSet.scan((Element)classSymbol, this.annotationsPresent);
/*  982 */       for (Symbol.PackageSymbol packageSymbol : this.packageInfoFiles) {
/*  983 */         computeAnnotationSet.scan((Element)packageSymbol, this.annotationsPresent);
/*      */       }
/*      */     }
/*      */
/*      */     private List<Symbol.ClassSymbol> enterClassFiles(Map<String, JavaFileObject> param1Map) {
/*  988 */       ClassReader classReader = ClassReader.instance(this.context);
/*  989 */       Names names = Names.instance(this.context);
/*  990 */       List list = List.nil();
/*      */
/*  992 */       for (Map.Entry<String, JavaFileObject> entry : param1Map.entrySet()) {
/*  993 */         Symbol.ClassSymbol classSymbol; Name name = names.fromString((String)entry.getKey());
/*  994 */         JavaFileObject javaFileObject = (JavaFileObject)entry.getValue();
/*  995 */         if (javaFileObject.getKind() != JavaFileObject.Kind.CLASS) {
/*  996 */           throw new AssertionError(javaFileObject);
/*      */         }
/*  998 */         if (JavacProcessingEnvironment.this.isPkgInfo(javaFileObject, JavaFileObject.Kind.CLASS)) {
/*  999 */           Name name1 = Convert.packagePart(name);
/* 1000 */           Symbol.PackageSymbol packageSymbol = classReader.enterPackage(name1);
/* 1001 */           if (packageSymbol.package_info == null)
/* 1002 */             packageSymbol.package_info = classReader.enterClass(Convert.shortName(name), (Symbol.TypeSymbol)packageSymbol);
/* 1003 */           classSymbol = packageSymbol.package_info;
/* 1004 */           if (classSymbol.classfile == null)
/* 1005 */             classSymbol.classfile = javaFileObject;
/*      */         } else {
/* 1007 */           classSymbol = classReader.enterClass(name, javaFileObject);
/* 1008 */         }  list = list.prepend(classSymbol);
/*      */       }
/* 1010 */       return list.reverse();
/*      */     }
/*      */
/*      */
/*      */     private void enterTrees(List<JCTree.JCCompilationUnit> param1List) {
/* 1015 */       this.compiler.enterTrees(param1List);
/*      */     }
/*      */
/*      */
/*      */     void run(boolean param1Boolean1, boolean param1Boolean2) {
/* 1020 */       printRoundInfo(param1Boolean1);
/*      */
/* 1022 */       if (!JavacProcessingEnvironment.this.taskListener.isEmpty()) {
/* 1023 */         JavacProcessingEnvironment.this.taskListener.started(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING_ROUND));
/*      */       }
/*      */       try {
/* 1026 */         if (param1Boolean1) {
/* 1027 */           JavacProcessingEnvironment.this.filer.setLastRound(true);
/* 1028 */           Set<?> set = Collections.emptySet();
/* 1029 */           JavacRoundEnvironment javacRoundEnvironment = new JavacRoundEnvironment(true, param1Boolean2, (Set)set, JavacProcessingEnvironment.this);
/*      */
/*      */
/*      */
/* 1033 */           JavacProcessingEnvironment.this.discoveredProcs.iterator().runContributingProcs(javacRoundEnvironment);
/*      */         } else {
/* 1035 */           JavacProcessingEnvironment.this.discoverAndRunProcs(this.context, this.annotationsPresent, this.topLevelClasses, this.packageInfoFiles);
/*      */         }
/* 1037 */       } catch (Throwable throwable) {
/*      */
/*      */
/*      */
/* 1041 */         this.deferredDiagnosticHandler.reportDeferredDiagnostics();
/* 1042 */         this.log.popDiagnosticHandler((Log.DiagnosticHandler)this.deferredDiagnosticHandler);
/* 1043 */         throw throwable;
/*      */       } finally {
/* 1045 */         if (!JavacProcessingEnvironment.this.taskListener.isEmpty())
/* 1046 */           JavacProcessingEnvironment.this.taskListener.finished(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING_ROUND));
/*      */       }
/*      */     }
/*      */
/*      */     void showDiagnostics(boolean param1Boolean) {
/* 1051 */       EnumSet<Diagnostic.Kind> enumSet = EnumSet.allOf(Diagnostic.Kind.class);
/* 1052 */       if (!param1Boolean)
/*      */       {
/* 1054 */         enumSet.remove(Diagnostic.Kind.ERROR);
/*      */       }
/* 1056 */       this.deferredDiagnosticHandler.reportDeferredDiagnostics(enumSet);
/* 1057 */       this.log.popDiagnosticHandler((Log.DiagnosticHandler)this.deferredDiagnosticHandler);
/*      */     }
/*      */
/*      */
/*      */     private void printRoundInfo(boolean param1Boolean) {
/* 1062 */       if (JavacProcessingEnvironment.this.printRounds || JavacProcessingEnvironment.this.verbose) {
/* 1063 */         List list = param1Boolean ? List.nil() : this.topLevelClasses;
/* 1064 */         Object object = param1Boolean ? Collections.emptySet() : this.annotationsPresent;
/* 1065 */         this.log.printLines("x.print.rounds", new Object[] {
/* 1066 */               Integer.valueOf(this.number), "{" + list
/* 1067 */               .toString(", ") + "}", object,
/*      */
/* 1069 */               Boolean.valueOf(param1Boolean)
/*      */             });
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     private Context nextContext() {
/* 1078 */       Context context = new Context(this.context);
/*      */
/* 1080 */       Options options = Options.instance(this.context);
/* 1081 */       Assert.checkNonNull(options);
/* 1082 */       context.put(Options.optionsKey, options);
/*      */
/* 1084 */       Locale locale = (Locale)this.context.get(Locale.class);
/* 1085 */       if (locale != null) {
/* 1086 */         context.put(Locale.class, locale);
/*      */       }
/* 1088 */       Assert.checkNonNull(JavacProcessingEnvironment.this.messages);
/* 1089 */       context.put(JavacMessages.messagesKey, JavacProcessingEnvironment.this.messages);
/*      */
/*      */
/*      */
/* 1093 */       Names names1 = Names.instance(this.context);
/* 1094 */       Assert.checkNonNull(names1);
/* 1095 */       context.put(Names.namesKey, names1);
/*      */
/*      */
/* 1098 */       DiagnosticListener diagnosticListener = (DiagnosticListener)this.context.get(DiagnosticListener.class);
/* 1099 */       if (diagnosticListener != null) {
/* 1100 */         context.put(DiagnosticListener.class, diagnosticListener);
/*      */       }
/* 1102 */       MultiTaskListener multiTaskListener = (MultiTaskListener)this.context.get(MultiTaskListener.taskListenerKey);
/* 1103 */       if (multiTaskListener != null) {
/* 1104 */         context.put(MultiTaskListener.taskListenerKey, multiTaskListener);
/*      */       }
/* 1106 */       FSInfo fSInfo = (FSInfo)this.context.get(FSInfo.class);
/* 1107 */       if (fSInfo != null) {
/* 1108 */         context.put(FSInfo.class, fSInfo);
/*      */       }
/* 1110 */       JavaFileManager javaFileManager = (JavaFileManager)this.context.get(JavaFileManager.class);
/* 1111 */       Assert.checkNonNull(javaFileManager);
/* 1112 */       context.put(JavaFileManager.class, javaFileManager);
/* 1113 */       if (javaFileManager instanceof JavacFileManager) {
/* 1114 */         ((JavacFileManager)javaFileManager).setContext(context);
/*      */       }
/*      */
/* 1117 */       Names names2 = Names.instance(this.context);
/* 1118 */       Assert.checkNonNull(names2);
/* 1119 */       context.put(Names.namesKey, names2);
/*      */
/* 1121 */       Tokens tokens = Tokens.instance(this.context);
/* 1122 */       Assert.checkNonNull(tokens);
/* 1123 */       context.put(Tokens.tokensKey, tokens);
/*      */
/* 1125 */       Log log = Log.instance(context);
/* 1126 */       log.initRound(this.log);
/*      */
/* 1128 */       JavaCompiler javaCompiler1 = JavaCompiler.instance(this.context);
/* 1129 */       JavaCompiler javaCompiler2 = JavaCompiler.instance(context);
/* 1130 */       javaCompiler2.initRound(javaCompiler1);
/*      */
/* 1132 */       JavacProcessingEnvironment.this.filer.newRound(context);
/* 1133 */       JavacProcessingEnvironment.this.messager.newRound(context);
/* 1134 */       JavacProcessingEnvironment.this.elementUtils.setContext(context);
/* 1135 */       JavacProcessingEnvironment.this.typeUtils.setContext(context);
/*      */
/* 1137 */       JavacTask javacTask = (JavacTask)this.context.get(JavacTask.class);
/* 1138 */       if (javacTask != null) {
/* 1139 */         context.put(JavacTask.class, javacTask);
/* 1140 */         if (javacTask instanceof BasicJavacTask) {
/* 1141 */           ((BasicJavacTask)javacTask).updateContext(context);
/*      */         }
/*      */       }
/* 1144 */       JavacTrees javacTrees = (JavacTrees)this.context.get(JavacTrees.class);
/* 1145 */       if (javacTrees != null) {
/* 1146 */         context.put(JavacTrees.class, javacTrees);
/* 1147 */         javacTrees.updateContext(context);
/*      */       }
/*      */
/* 1150 */       this.context.clear();
/* 1151 */       return context;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public JavaCompiler doProcessing(Context paramContext, List<JCTree.JCCompilationUnit> paramList, List<Symbol.ClassSymbol> paramList1, Iterable<? extends Symbol.PackageSymbol> paramIterable, Log.DeferredDiagnosticHandler paramDeferredDiagnosticHandler) {
/*      */     boolean bool2;
/* 1163 */     this.log = Log.instance(paramContext);
/*      */
/* 1165 */     LinkedHashSet<Symbol.PackageSymbol> linkedHashSet = new LinkedHashSet();
/* 1166 */     for (Symbol.PackageSymbol packageSymbol : paramIterable)
/* 1167 */       linkedHashSet.add(packageSymbol);
/* 1168 */     this.specifiedPackages = Collections.unmodifiableSet(linkedHashSet);
/*      */
/* 1170 */     Round round = new Round(paramContext, paramList, paramList1, paramDeferredDiagnosticHandler);
/*      */
/*      */
/*      */
/*      */
/*      */     do {
/* 1176 */       round.run(false, false);
/*      */
/*      */
/*      */
/* 1180 */       bool1 = round.unrecoverableError();
/* 1181 */       bool2 = moreToDo();
/*      */
/* 1183 */       round.showDiagnostics((bool1 || this.showResolveErrors));
/*      */
/*      */
/*      */
/* 1187 */       round = round.next(new LinkedHashSet<>(this.filer
/* 1188 */             .getGeneratedSourceFileObjects()), new LinkedHashMap<>(this.filer
/* 1189 */             .getGeneratedClasses()));
/*      */
/*      */
/* 1192 */       if (!round.unrecoverableError())
/* 1193 */         continue;  bool1 = true;
/*      */     }
/* 1195 */     while (bool2 && !bool1);
/*      */
/*      */
/* 1198 */     round.run(true, bool1);
/* 1199 */     round.showDiagnostics(true);
/*      */
/* 1201 */     this.filer.warnIfUnclosedFiles();
/* 1202 */     warnIfUnmatchedOptions();
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
/* 1215 */     if (this.messager.errorRaised() || (this.werror && round
/* 1216 */       .warningCount() > 0 && round.errorCount() > 0)) {
/* 1217 */       bool1 = true;
/*      */     }
/*      */
/* 1220 */     LinkedHashSet<JavaFileObject> linkedHashSet1 = new LinkedHashSet<>(this.filer.getGeneratedSourceFileObjects());
/* 1221 */     paramList = cleanTrees(round.roots);
/*      */
/* 1223 */     JavaCompiler javaCompiler = round.finalCompiler();
/*      */
/* 1225 */     if (linkedHashSet1.size() > 0) {
/* 1226 */       paramList = paramList.appendList(javaCompiler.parseFiles(linkedHashSet1));
/*      */     }
/* 1228 */     boolean bool1 = (bool1 || javaCompiler.errorCount() > 0);
/*      */
/*      */
/* 1231 */     close();
/*      */
/* 1233 */     if (!this.taskListener.isEmpty()) {
/* 1234 */       this.taskListener.finished(new TaskEvent(TaskEvent.Kind.ANNOTATION_PROCESSING));
/*      */     }
/* 1236 */     if (bool1) {
/* 1237 */       if (javaCompiler.errorCount() == 0)
/* 1238 */         javaCompiler.log.nerrors++;
/* 1239 */       return javaCompiler;
/*      */     }
/*      */
/* 1242 */     javaCompiler.enterTreesIfNeeded(paramList);
/*      */
/* 1244 */     return javaCompiler;
/*      */   }
/*      */
/*      */   private void warnIfUnmatchedOptions() {
/* 1248 */     if (!this.unmatchedProcessorOptions.isEmpty()) {
/* 1249 */       this.log.warning("proc.unmatched.processor.options", new Object[] { this.unmatchedProcessorOptions.toString() });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void close() {
/* 1257 */     this.filer.close();
/* 1258 */     if (this.discoveredProcs != null)
/* 1259 */       this.discoveredProcs.close();
/* 1260 */     this.discoveredProcs = null;
/*      */   }
/*      */
/*      */   private List<Symbol.ClassSymbol> getTopLevelClasses(List<? extends JCTree.JCCompilationUnit> paramList) {
/* 1264 */     List list = List.nil();
/* 1265 */     for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) {
/* 1266 */       for (JCTree jCTree : jCCompilationUnit.defs) {
/* 1267 */         if (jCTree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 1268 */           Symbol.ClassSymbol classSymbol = ((JCTree.JCClassDecl)jCTree).sym;
/* 1269 */           Assert.checkNonNull(classSymbol);
/* 1270 */           list = list.prepend(classSymbol);
/*      */         }
/*      */       }
/*      */     }
/* 1274 */     return list.reverse();
/*      */   }
/*      */
/*      */   private List<Symbol.ClassSymbol> getTopLevelClassesFromClasses(List<? extends Symbol.ClassSymbol> paramList) {
/* 1278 */     List list = List.nil();
/* 1279 */     for (Symbol.ClassSymbol classSymbol : paramList) {
/* 1280 */       if (!isPkgInfo(classSymbol)) {
/* 1281 */         list = list.prepend(classSymbol);
/*      */       }
/*      */     }
/* 1284 */     return list.reverse();
/*      */   }
/*      */
/*      */   private List<Symbol.PackageSymbol> getPackageInfoFiles(List<? extends JCTree.JCCompilationUnit> paramList) {
/* 1288 */     List list = List.nil();
/* 1289 */     for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) {
/* 1290 */       if (isPkgInfo(jCCompilationUnit.sourcefile, JavaFileObject.Kind.SOURCE)) {
/* 1291 */         list = list.prepend(jCCompilationUnit.packge);
/*      */       }
/*      */     }
/* 1294 */     return list.reverse();
/*      */   }
/*      */
/*      */   private List<Symbol.PackageSymbol> getPackageInfoFilesFromClasses(List<? extends Symbol.ClassSymbol> paramList) {
/* 1298 */     List list = List.nil();
/* 1299 */     for (Symbol.ClassSymbol classSymbol : paramList) {
/* 1300 */       if (isPkgInfo(classSymbol)) {
/* 1301 */         list = list.prepend(classSymbol.owner);
/*      */       }
/*      */     }
/* 1304 */     return list.reverse();
/*      */   }
/*      */
/*      */
/*      */   private static <T> List<T> join(List<T> paramList1, List<T> paramList2) {
/* 1309 */     return paramList1.appendList(paramList2);
/*      */   }
/*      */
/*      */   private boolean isPkgInfo(JavaFileObject paramJavaFileObject, JavaFileObject.Kind paramKind) {
/* 1313 */     return paramJavaFileObject.isNameCompatible("package-info", paramKind);
/*      */   }
/*      */
/*      */   private boolean isPkgInfo(Symbol.ClassSymbol paramClassSymbol) {
/* 1317 */     return (isPkgInfo(paramClassSymbol.classfile, JavaFileObject.Kind.CLASS) && (paramClassSymbol.packge()).package_info == paramClassSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean needClassLoader(String paramString, Iterable<? extends File> paramIterable) {
/* 1325 */     if (paramString != null) {
/* 1326 */       return true;
/*      */     }
/* 1328 */     URL[] arrayOfURL = new URL[1];
/* 1329 */     for (File file : paramIterable) {
/*      */       try {
/* 1331 */         arrayOfURL[0] = file.toURI().toURL();
/* 1332 */         if (ServiceProxy.hasService(Processor.class, arrayOfURL))
/* 1333 */           return true;
/* 1334 */       } catch (MalformedURLException malformedURLException) {
/* 1335 */         throw new AssertionError(malformedURLException);
/*      */       }
/* 1337 */       catch (ServiceConfigurationError serviceConfigurationError) {
/* 1338 */         this.log.error("proc.bad.config.file", new Object[] { serviceConfigurationError.getLocalizedMessage() });
/* 1339 */         return true;
/*      */       }
/*      */     }
/*      */
/* 1343 */     return false;
/*      */   }
/*      */
/*      */   private static <T extends JCTree> List<T> cleanTrees(List<T> paramList) {
/* 1347 */     for (JCTree jCTree : paramList)
/* 1348 */       treeCleaner.scan(jCTree);
/* 1349 */     return paramList;
/*      */   }
/*      */
/* 1352 */   private static final TreeScanner treeCleaner = new TreeScanner() {
/*      */       public void scan(JCTree param1JCTree) {
/* 1354 */         super.scan(param1JCTree);
/* 1355 */         if (param1JCTree != null)
/* 1356 */           param1JCTree.type = null;
/*      */       }
/*      */       public void visitTopLevel(JCTree.JCCompilationUnit param1JCCompilationUnit) {
/* 1359 */         param1JCCompilationUnit.packge = null;
/* 1360 */         super.visitTopLevel(param1JCCompilationUnit);
/*      */       }
/*      */       public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/* 1363 */         param1JCClassDecl.sym = null;
/* 1364 */         super.visitClassDef(param1JCClassDecl);
/*      */       }
/*      */       public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/* 1367 */         param1JCMethodDecl.sym = null;
/* 1368 */         super.visitMethodDef(param1JCMethodDecl);
/*      */       }
/*      */       public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 1371 */         param1JCVariableDecl.sym = null;
/* 1372 */         super.visitVarDef(param1JCVariableDecl);
/*      */       }
/*      */       public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 1375 */         param1JCNewClass.constructor = null;
/* 1376 */         super.visitNewClass(param1JCNewClass);
/*      */       }
/*      */       public void visitAssignop(JCTree.JCAssignOp param1JCAssignOp) {
/* 1379 */         param1JCAssignOp.operator = null;
/* 1380 */         super.visitAssignop(param1JCAssignOp);
/*      */       }
/*      */       public void visitUnary(JCTree.JCUnary param1JCUnary) {
/* 1383 */         param1JCUnary.operator = null;
/* 1384 */         super.visitUnary(param1JCUnary);
/*      */       }
/*      */       public void visitBinary(JCTree.JCBinary param1JCBinary) {
/* 1387 */         param1JCBinary.operator = null;
/* 1388 */         super.visitBinary(param1JCBinary);
/*      */       }
/*      */       public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 1391 */         param1JCFieldAccess.sym = null;
/* 1392 */         super.visitSelect(param1JCFieldAccess);
/*      */       }
/*      */       public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 1395 */         param1JCIdent.sym = null;
/* 1396 */         super.visitIdent(param1JCIdent);
/*      */       }
/*      */       public void visitAnnotation(JCTree.JCAnnotation param1JCAnnotation) {
/* 1399 */         param1JCAnnotation.attribute = null;
/* 1400 */         super.visitAnnotation(param1JCAnnotation);
/*      */       }
/*      */     };
/*      */
/*      */
/*      */   private boolean moreToDo() {
/* 1406 */     return this.filer.newFiles();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Map<String, String> getOptions() {
/* 1417 */     return this.processorOptions;
/*      */   }
/*      */
/*      */   public Messager getMessager() {
/* 1421 */     return this.messager;
/*      */   }
/*      */
/*      */   public Filer getFiler() {
/* 1425 */     return this.filer;
/*      */   }
/*      */
/*      */   public JavacElements getElementUtils() {
/* 1429 */     return this.elementUtils;
/*      */   }
/*      */
/*      */   public JavacTypes getTypeUtils() {
/* 1433 */     return this.typeUtils;
/*      */   }
/*      */
/*      */   public SourceVersion getSourceVersion() {
/* 1437 */     return Source.toSourceVersion(this.source);
/*      */   }
/*      */
/*      */   public Locale getLocale() {
/* 1441 */     return this.messages.getCurrentLocale();
/*      */   }
/*      */
/*      */   public Set<Symbol.PackageSymbol> getSpecifiedPackages() {
/* 1445 */     return this.specifiedPackages;
/*      */   }
/*      */
/* 1448 */   private static final Pattern allMatches = Pattern.compile(".*");
/* 1449 */   public static final Pattern noMatches = Pattern.compile("(\\P{all})+");
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private static Pattern importStringToPattern(String paramString, Processor paramProcessor, Log paramLog) {
/* 1457 */     if (isValidImportString(paramString)) {
/* 1458 */       return validImportStringToPattern(paramString);
/*      */     }
/* 1460 */     paramLog.warning("proc.malformed.supported.string", new Object[] { paramString, paramProcessor.getClass().getName() });
/* 1461 */     return noMatches;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public static boolean isValidImportString(String paramString) {
/* 1470 */     if (paramString.equals("*")) {
/* 1471 */       return true;
/*      */     }
/* 1473 */     boolean bool = true;
/* 1474 */     String str = paramString;
/* 1475 */     int i = str.indexOf('*');
/*      */
/* 1477 */     if (i != -1)
/*      */     {
/* 1479 */       if (i == str.length() - 1) {
/*      */
/* 1481 */         if (i - 1 >= 0) {
/* 1482 */           bool = (str.charAt(i - 1) == '.');
/*      */
/* 1484 */           str = str.substring(0, str.length() - 2);
/*      */         }
/*      */       } else {
/* 1487 */         return false;
/*      */       }
/*      */     }
/*      */
/* 1491 */     if (bool) {
/* 1492 */       String[] arrayOfString = str.split("\\.", str.length() + 2);
/* 1493 */       for (String str1 : arrayOfString)
/* 1494 */         bool &= SourceVersion.isIdentifier(str1);
/*      */     }
/* 1496 */     return bool;
/*      */   }
/*      */
/*      */   public static Pattern validImportStringToPattern(String paramString) {
/* 1500 */     if (paramString.equals("*")) {
/* 1501 */       return allMatches;
/*      */     }
/* 1503 */     String str = paramString.replace(".", "\\.");
/*      */
/* 1505 */     if (str.endsWith("*")) {
/* 1506 */       str = str.substring(0, str.length() - 1) + ".+";
/*      */     }
/*      */
/* 1509 */     return Pattern.compile(str);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Context getContext() {
/* 1517 */     return this.context;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public ClassLoader getProcessorClassLoader() {
/* 1524 */     return this.processorClassLoader;
/*      */   }
/*      */
/*      */   public String toString() {
/* 1528 */     return "javac ProcessingEnvironment";
/*      */   }
/*      */
/*      */   public static boolean isValidOptionName(String paramString) {
/* 1532 */     for (String str : paramString.split("\\.", -1)) {
/* 1533 */       if (!SourceVersion.isIdentifier(str))
/* 1534 */         return false;
/*      */     }
/* 1536 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\processing\JavacProcessingEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
