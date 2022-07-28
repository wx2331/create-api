/*     */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*     */ 
/*     */ import com.sun.istack.internal.logging.Logger;
/*     */ import com.sun.tools.internal.ws.processor.generator.GeneratorUtil;
/*     */ import com.sun.tools.internal.ws.processor.modeler.ModelerException;
/*     */ import com.sun.tools.internal.ws.resources.WebserviceapMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.Options;
/*     */ import com.sun.tools.internal.ws.wscompile.WsgenOptions;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Scanner;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.annotation.processing.SupportedOptions;
/*     */ import javax.jws.WebService;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.xml.ws.Holder;
/*     */ import javax.xml.ws.WebServiceProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SupportedAnnotationTypes({"javax.jws.HandlerChain", "javax.jws.Oneway", "javax.jws.WebMethod", "javax.jws.WebParam", "javax.jws.WebResult", "javax.jws.WebService", "javax.jws.soap.InitParam", "javax.jws.soap.SOAPBinding", "javax.jws.soap.SOAPMessageHandler", "javax.jws.soap.SOAPMessageHandlers", "javax.xml.ws.BindingType", "javax.xml.ws.RequestWrapper", "javax.xml.ws.ResponseWrapper", "javax.xml.ws.ServiceMode", "javax.xml.ws.WebEndpoint", "javax.xml.ws.WebFault", "javax.xml.ws.WebServiceClient", "javax.xml.ws.WebServiceProvider", "javax.xml.ws.WebServiceRef"})
/*     */ @SupportedOptions({"doNotOverWrite", "ignoreNoWebServiceFoundWarning"})
/*     */ public class WebServiceAp
/*     */   extends AbstractProcessor
/*     */   implements ModelBuilder
/*     */ {
/*  95 */   private static final Logger LOGGER = Logger.getLogger(WebServiceAp.class);
/*     */   
/*     */   public static final String DO_NOT_OVERWRITE = "doNotOverWrite";
/*     */   
/*     */   public static final String IGNORE_NO_WEB_SERVICE_FOUND_WARNING = "ignoreNoWebServiceFoundWarning";
/*     */   private WsgenOptions options;
/*     */   protected AnnotationProcessorContext context;
/*     */   private File sourceDir;
/*     */   private boolean doNotOverWrite;
/*     */   private boolean ignoreNoWebServiceFoundWarning = false;
/*     */   private TypeElement remoteElement;
/*     */   private TypeMirror remoteExceptionElement;
/*     */   private TypeMirror exceptionElement;
/*     */   private TypeMirror runtimeExceptionElement;
/*     */   private TypeElement defHolderElement;
/*     */   private boolean isCommandLineInvocation;
/*     */   private PrintStream out;
/* 112 */   private Collection<TypeElement> processedTypeElements = new HashSet<>();
/*     */   
/*     */   public WebServiceAp() {
/* 115 */     this.context = new AnnotationProcessorContext();
/*     */   }
/*     */   
/*     */   public WebServiceAp(WsgenOptions options, PrintStream out) {
/* 119 */     this.options = options;
/* 120 */     this.sourceDir = (options != null) ? options.sourceDir : null;
/* 121 */     this.doNotOverWrite = (options != null && options.doNotOverWrite);
/* 122 */     this.context = new AnnotationProcessorContext();
/* 123 */     this.out = out;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void init(ProcessingEnvironment processingEnv) {
/* 128 */     super.init(processingEnv);
/* 129 */     this.remoteElement = processingEnv.getElementUtils().getTypeElement(Remote.class.getName());
/* 130 */     this.remoteExceptionElement = processingEnv.getElementUtils().getTypeElement(RemoteException.class.getName()).asType();
/* 131 */     this.exceptionElement = processingEnv.getElementUtils().getTypeElement(Exception.class.getName()).asType();
/* 132 */     this.runtimeExceptionElement = processingEnv.getElementUtils().getTypeElement(RuntimeException.class.getName()).asType();
/* 133 */     this.defHolderElement = processingEnv.getElementUtils().getTypeElement(Holder.class.getName());
/* 134 */     if (this.options == null) {
/* 135 */       this.options = new WsgenOptions();
/*     */       
/* 137 */       this.out = new PrintStream(new ByteArrayOutputStream());
/*     */       
/* 139 */       this.doNotOverWrite = getOption("doNotOverWrite");
/* 140 */       this.ignoreNoWebServiceFoundWarning = getOption("ignoreNoWebServiceFoundWarning");
/*     */       
/* 142 */       String classDir = parseArguments();
/* 143 */       String property = System.getProperty("java.class.path");
/* 144 */       this.options.classpath = classDir + File.pathSeparator + ((property != null) ? property : "");
/* 145 */       this.isCommandLineInvocation = true;
/*     */     } 
/* 147 */     this.options.filer = processingEnv.getFiler();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String parseArguments() {
/* 153 */     String classDir = null;
/*     */     try {
/* 155 */       ClassLoader cl = WebServiceAp.class.getClassLoader();
/* 156 */       Class<?> javacProcessingEnvironmentClass = Class.forName("com.sun.tools.javac.processing.JavacProcessingEnvironment", false, cl);
/* 157 */       if (javacProcessingEnvironmentClass.isInstance(this.processingEnv)) {
/* 158 */         Method getContextMethod = javacProcessingEnvironmentClass.getDeclaredMethod("getContext", new Class[0]);
/* 159 */         Object tmpContext = getContextMethod.invoke(this.processingEnv, new Object[0]);
/* 160 */         Class<?> optionsClass = Class.forName("com.sun.tools.javac.util.Options", false, cl);
/* 161 */         Class<?> contextClass = Class.forName("com.sun.tools.javac.util.Context", false, cl);
/* 162 */         Method instanceMethod = optionsClass.getDeclaredMethod("instance", new Class[] { contextClass });
/* 163 */         Object tmpOptions = instanceMethod.invoke(null, new Object[] { tmpContext });
/* 164 */         if (tmpOptions != null) {
/* 165 */           Method getMethod = optionsClass.getDeclaredMethod("get", new Class[] { String.class });
/* 166 */           Object result = getMethod.invoke(tmpOptions, new Object[] { "-s" });
/* 167 */           if (result != null) {
/* 168 */             classDir = (String)result;
/*     */           }
/* 170 */           this.options.verbose = (getMethod.invoke(tmpOptions, new Object[] { "-verbose" }) != null);
/*     */         } 
/*     */       } 
/* 173 */     } catch (Exception e) {
/*     */       
/* 175 */       processWarning(WebserviceapMessages.WEBSERVICEAP_PARSING_JAVAC_OPTIONS_ERROR());
/* 176 */       report(e.getMessage());
/*     */     } 
/*     */     
/* 179 */     if (classDir == null) {
/* 180 */       String property = System.getProperty("sun.java.command");
/* 181 */       if (property != null) {
/* 182 */         Scanner scanner = new Scanner(property);
/* 183 */         boolean sourceDirNext = false;
/* 184 */         while (scanner.hasNext()) {
/* 185 */           String token = scanner.next();
/* 186 */           if (sourceDirNext) {
/* 187 */             classDir = token;
/* 188 */             sourceDirNext = false; continue;
/* 189 */           }  if ("-verbose".equals(token)) {
/* 190 */             this.options.verbose = true; continue;
/* 191 */           }  if ("-s".equals(token)) {
/* 192 */             sourceDirNext = true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 197 */     if (classDir != null) {
/* 198 */       this.sourceDir = new File(classDir);
/*     */     }
/* 200 */     return classDir;
/*     */   }
/*     */   
/*     */   private boolean getOption(String key) {
/* 204 */     String value = this.processingEnv.getOptions().get(key);
/* 205 */     if (value != null) {
/* 206 */       return Boolean.valueOf(value).booleanValue();
/*     */     }
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/* 213 */     if (this.context.getRound() != 1) {
/* 214 */       return true;
/*     */     }
/* 216 */     this.context.incrementRound();
/*     */ 
/*     */     
/* 219 */     WebServiceVisitor webServiceVisitor = new WebServiceWrapperGenerator(this, this.context);
/* 220 */     boolean processedEndpoint = false;
/* 221 */     Collection<TypeElement> classes = new ArrayList<>();
/* 222 */     filterClasses(classes, roundEnv.getRootElements());
/* 223 */     for (TypeElement element : classes) {
/* 224 */       WebServiceProvider webServiceProvider = element.<WebServiceProvider>getAnnotation(WebServiceProvider.class);
/* 225 */       WebService webService = element.<WebService>getAnnotation(WebService.class);
/* 226 */       if (webServiceProvider != null) {
/* 227 */         if (webService != null) {
/* 228 */           processError(WebserviceapMessages.WEBSERVICEAP_WEBSERVICE_AND_WEBSERVICEPROVIDER(element.getQualifiedName()));
/*     */         }
/* 230 */         processedEndpoint = true;
/*     */       } 
/*     */       
/* 233 */       if (webService == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 237 */       element.accept(webServiceVisitor, null);
/* 238 */       processedEndpoint = true;
/*     */     } 
/* 240 */     if (!processedEndpoint) {
/* 241 */       if (this.isCommandLineInvocation) {
/* 242 */         if (!this.ignoreNoWebServiceFoundWarning) {
/* 243 */           processWarning(WebserviceapMessages.WEBSERVICEAP_NO_WEBSERVICE_ENDPOINT_FOUND());
/*     */         }
/*     */       } else {
/* 246 */         processError(WebserviceapMessages.WEBSERVICEAP_NO_WEBSERVICE_ENDPOINT_FOUND());
/*     */       } 
/*     */     }
/* 249 */     return true;
/*     */   }
/*     */   
/*     */   private void filterClasses(Collection<TypeElement> classes, Collection<? extends Element> elements) {
/* 253 */     for (Element element : elements) {
/* 254 */       if (element.getKind().equals(ElementKind.CLASS)) {
/* 255 */         classes.add((TypeElement)element);
/* 256 */         filterClasses(classes, (Collection)ElementFilter.typesIn(element.getEnclosedElements()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processWarning(String message) {
/* 263 */     if (this.isCommandLineInvocation) {
/* 264 */       this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message);
/*     */     } else {
/* 266 */       report(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void report(String msg) {
/* 271 */     if (this.out == null) {
/* 272 */       if (LOGGER.isLoggable(Level.FINE)) {
/* 273 */         LOGGER.log(Level.FINE, "No output set for web service annotation processor reporting.");
/*     */       }
/*     */       return;
/*     */     } 
/* 277 */     this.out.println(msg);
/* 278 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void processError(String message) {
/* 283 */     if (this.isCommandLineInvocation) {
/* 284 */       this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
/* 285 */       throw new AbortException();
/*     */     } 
/* 287 */     throw new ModelerException(message);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void processError(String message, Element element) {
/* 293 */     if (this.isCommandLineInvocation) {
/* 294 */       this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
/*     */     } else {
/* 296 */       throw new ModelerException(message);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canOverWriteClass(String className) {
/* 302 */     return (!this.doNotOverWrite || !GeneratorUtil.classExists((Options)this.options, className));
/*     */   }
/*     */ 
/*     */   
/*     */   public File getSourceDir() {
/* 307 */     return this.sourceDir;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRemote(TypeElement typeElement) {
/* 312 */     return this.processingEnv.getTypeUtils().isSubtype(typeElement.asType(), this.remoteElement.asType());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isServiceException(TypeMirror typeMirror) {
/* 317 */     return (this.processingEnv.getTypeUtils().isSubtype(typeMirror, this.exceptionElement) && 
/* 318 */       !this.processingEnv.getTypeUtils().isSubtype(typeMirror, this.runtimeExceptionElement) && 
/* 319 */       !this.processingEnv.getTypeUtils().isSubtype(typeMirror, this.remoteExceptionElement));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeMirror getHolderValueType(TypeMirror type) {
/* 324 */     return TypeModeler.getHolderValueType(type, this.defHolderElement, this.processingEnv);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkAndSetProcessed(TypeElement typeElement) {
/* 329 */     if (!this.processedTypeElements.contains(typeElement)) {
/* 330 */       this.processedTypeElements.add(typeElement);
/* 331 */       return false;
/*     */     } 
/* 333 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message) {
/* 338 */     if (this.options != null && this.options.verbose) {
/* 339 */       message = '[' + message + ']';
/* 340 */       this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public WsgenOptions getOptions() {
/* 346 */     return this.options;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProcessingEnvironment getProcessingEnvironment() {
/* 351 */     return this.processingEnv;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOperationName(Name messageName) {
/* 356 */     return (messageName != null) ? messageName.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceVersion getSupportedSourceVersion() {
/* 361 */     return SourceVersion.latest();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\WebServiceAp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */