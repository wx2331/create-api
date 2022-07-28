/*     */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*     */
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.resources.WebserviceapMessages;
/*     */ import com.sun.tools.internal.ws.util.ClassNameInfo;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import com.sun.xml.internal.ws.model.RuntimeModeler;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.jws.Oneway;
/*     */ import javax.jws.WebMethod;
/*     */ import javax.jws.WebParam;
/*     */ import javax.jws.WebResult;
/*     */ import javax.jws.WebService;
/*     */ import javax.jws.soap.SOAPBinding;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.SimpleElementVisitor6;
/*     */ import javax.lang.model.util.SimpleTypeVisitor6;
/*     */ import javax.lang.model.util.Types;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public abstract class WebServiceVisitor
/*     */   extends SimpleElementVisitor6<Void, Object>
/*     */ {
/*     */   protected ModelBuilder builder;
/*     */   protected String wsdlNamespace;
/*     */   protected String typeNamespace;
/*     */   protected Stack<SOAPBinding> soapBindingStack;
/*     */   protected SOAPBinding typeElementSoapBinding;
/*  75 */   protected SOAPStyle soapStyle = SOAPStyle.DOCUMENT;
/*     */
/*     */   protected boolean wrapped = true;
/*     */   protected Port port;
/*     */   protected Name serviceImplName;
/*     */   protected Name endpointInterfaceName;
/*     */   protected AnnotationProcessorContext context;
/*     */   protected AnnotationProcessorContext.SeiContext seiContext;
/*     */   protected boolean processingSei = false;
/*     */   protected String serviceName;
/*     */   protected Name packageName;
/*     */   protected String portName;
/*     */   protected boolean endpointReferencesInterface = false;
/*     */   protected boolean hasWebMethods = false;
/*     */   protected TypeElement typeElement;
/*     */   protected Set<String> processedMethods;
/*     */   protected boolean pushedSoapBinding = false;
/*  92 */   private static final NoTypeVisitor NO_TYPE_VISITOR = new NoTypeVisitor();
/*     */
/*     */   public WebServiceVisitor(ModelBuilder builder, AnnotationProcessorContext context) {
/*  95 */     this.builder = builder;
/*  96 */     this.context = context;
/*  97 */     this.soapBindingStack = new Stack<>();
/*  98 */     this.processedMethods = new HashSet<>();
/*     */   }
/*     */
/*     */   public Void visitType(TypeElement e, Object o) {
/*     */     String endpointInterfaceName;
/* 103 */     WebService webService = e.<WebService>getAnnotation(WebService.class);
/* 104 */     if (!shouldProcessWebService(webService, e))
/* 105 */       return null;
/* 106 */     if (this.builder.checkAndSetProcessed(e))
/* 107 */       return null;
/* 108 */     this.typeElement = e;
/*     */
/* 110 */     switch (e.getKind()) {
/*     */       case INTERFACE:
/* 112 */         if (this.endpointInterfaceName != null && !this.endpointInterfaceName.equals(e.getQualifiedName())) {
/* 113 */           this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ENDPOINTINTERFACES_DO_NOT_MATCH(this.endpointInterfaceName, e.getQualifiedName()), e);
/*     */         }
/* 115 */         verifySeiAnnotations(webService, e);
/* 116 */         this.endpointInterfaceName = e.getQualifiedName();
/* 117 */         this.processingSei = true;
/* 118 */         preProcessWebService(webService, e);
/* 119 */         processWebService(webService, e);
/* 120 */         postProcessWebService(webService, e);
/*     */         break;
/*     */
/*     */       case CLASS:
/* 124 */         this.typeElementSoapBinding = e.<SOAPBinding>getAnnotation(SOAPBinding.class);
/* 125 */         if (this.serviceImplName == null)
/* 126 */           this.serviceImplName = e.getQualifiedName();
/* 127 */         endpointInterfaceName = (webService != null) ? webService.endpointInterface() : null;
/* 128 */         if (endpointInterfaceName != null && endpointInterfaceName.length() > 0) {
/* 129 */           checkForInvalidImplAnnotation(e, SOAPBinding.class);
/* 130 */           if (webService.name().length() > 0)
/* 131 */             this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ENDPOINTINTEFACE_PLUS_ELEMENT("name"), e);
/* 132 */           this.endpointReferencesInterface = true;
/* 133 */           verifyImplAnnotations(e);
/* 134 */           inspectEndpointInterface(endpointInterfaceName, e);
/* 135 */           this.serviceImplName = null;
/* 136 */           return null;
/*     */         }
/* 138 */         this.processingSei = false;
/* 139 */         preProcessWebService(webService, e);
/* 140 */         processWebService(webService, e);
/* 141 */         this.serviceImplName = null;
/* 142 */         postProcessWebService(webService, e);
/* 143 */         this.serviceImplName = null;
/*     */         break;
/*     */     }
/*     */
/*     */
/*     */
/* 149 */     return null;
/*     */   }
/*     */
/*     */   protected void verifySeiAnnotations(WebService webService, TypeElement d) {
/* 153 */     if (webService.endpointInterface().length() > 0) {
/* 154 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ENDPOINTINTERFACE_ON_INTERFACE(d
/* 155 */             .getQualifiedName(), webService.endpointInterface()), d);
/*     */     }
/* 157 */     if (webService.serviceName().length() > 0) {
/* 158 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_SEI_ANNOTATION_ELEMENT("serviceName", d
/* 159 */             .getQualifiedName()), d);
/*     */     }
/* 161 */     if (webService.portName().length() > 0) {
/* 162 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_SEI_ANNOTATION_ELEMENT("portName", d
/* 163 */             .getQualifiedName()), d);
/*     */     }
/*     */   }
/*     */
/*     */   protected void verifyImplAnnotations(TypeElement d) {
/* 168 */     for (ExecutableElement method : ElementFilter.methodsIn(d.getEnclosedElements())) {
/* 169 */       checkForInvalidImplAnnotation(method, WebMethod.class);
/* 170 */       checkForInvalidImplAnnotation(method, Oneway.class);
/* 171 */       checkForInvalidImplAnnotation(method, WebResult.class);
/* 172 */       for (VariableElement param : method.getParameters()) {
/* 173 */         checkForInvalidImplAnnotation(param, WebParam.class);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   protected void checkForInvalidSeiAnnotation(TypeElement element, Class<Annotation> annotationClass) {
/* 179 */     Object annotation = element.getAnnotation(annotationClass);
/* 180 */     if (annotation != null) {
/* 181 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_SEI_ANNOTATION(annotationClass
/* 182 */             .getName(), element.getQualifiedName()), element);
/*     */     }
/*     */   }
/*     */
/*     */   protected void checkForInvalidImplAnnotation(Element element, Class<Annotation> annotationClass) {
/* 187 */     Object annotation = element.getAnnotation(annotationClass);
/* 188 */     if (annotation != null) {
/* 189 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ENDPOINTINTEFACE_PLUS_ANNOTATION(annotationClass.getName()), element);
/*     */     }
/*     */   }
/*     */
/*     */   protected void preProcessWebService(WebService webService, TypeElement element) {
/* 194 */     this.processedMethods = new HashSet<>();
/* 195 */     this.seiContext = this.context.getSeiContext(element);
/* 196 */     String targetNamespace = null;
/* 197 */     if (webService != null)
/* 198 */       targetNamespace = webService.targetNamespace();
/* 199 */     PackageElement packageElement = this.builder.getProcessingEnvironment().getElementUtils().getPackageOf(element);
/* 200 */     if (targetNamespace == null || targetNamespace.length() == 0) {
/* 201 */       String packageName = packageElement.getQualifiedName().toString();
/* 202 */       if (packageName == null || packageName.length() == 0) {
/* 203 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_NO_PACKAGE_CLASS_MUST_HAVE_TARGETNAMESPACE(element
/* 204 */               .getQualifiedName()), element);
/*     */       }
/* 206 */       targetNamespace = RuntimeModeler.getNamespace(packageName);
/*     */     }
/* 208 */     this.seiContext.setNamespaceUri(targetNamespace);
/* 209 */     if (this.serviceImplName == null)
/* 210 */       this.serviceImplName = this.seiContext.getSeiImplName();
/* 211 */     if (this.serviceImplName != null) {
/* 212 */       this.seiContext.setSeiImplName(this.serviceImplName);
/* 213 */       this.context.addSeiContext(this.serviceImplName, this.seiContext);
/*     */     }
/* 215 */     this.portName = ClassNameInfo.getName(element.getSimpleName().toString().replace('$', '_'));
/* 216 */     this.packageName = packageElement.getQualifiedName();
/* 217 */     this
/* 218 */       .portName = (webService != null && webService.name() != null && webService.name().length() > 0) ? webService.name() : this.portName;
/* 219 */     this.serviceName = ClassNameInfo.getName(element.getQualifiedName().toString()) + WebServiceConstants.SERVICE.getValue();
/* 220 */     this
/* 221 */       .serviceName = (webService != null && webService.serviceName() != null && webService.serviceName().length() > 0) ? webService.serviceName() : this.serviceName;
/* 222 */     this.wsdlNamespace = this.seiContext.getNamespaceUri();
/* 223 */     this.typeNamespace = this.wsdlNamespace;
/*     */
/* 225 */     SOAPBinding soapBinding = element.<SOAPBinding>getAnnotation(SOAPBinding.class);
/* 226 */     if (soapBinding != null) {
/* 227 */       this.pushedSoapBinding = pushSoapBinding(soapBinding, element, element);
/* 228 */     } else if (element.equals(this.typeElement)) {
/* 229 */       this.pushedSoapBinding = pushSoapBinding(new MySoapBinding(), element, element);
/*     */     }
/*     */   }
/*     */
/*     */   public static boolean sameStyle(SOAPBinding.Style style, SOAPStyle soapStyle) {
/* 234 */     return ((style.equals(SOAPBinding.Style.DOCUMENT) && soapStyle
/* 235 */       .equals(SOAPStyle.DOCUMENT)) || (style
/* 236 */       .equals(SOAPBinding.Style.RPC) && soapStyle
/* 237 */       .equals(SOAPStyle.RPC)));
/*     */   }
/*     */
/*     */   protected boolean pushSoapBinding(SOAPBinding soapBinding, Element bindingElement, TypeElement classElement) {
/* 241 */     boolean changed = false;
/* 242 */     if (!sameStyle(soapBinding.style(), this.soapStyle)) {
/* 243 */       changed = true;
/* 244 */       if (this.pushedSoapBinding)
/* 245 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_MIXED_BINDING_STYLE(classElement
/* 246 */               .getQualifiedName()), bindingElement);
/*     */     }
/* 248 */     if (soapBinding.style().equals(SOAPBinding.Style.RPC)) {
/* 249 */       this.soapStyle = SOAPStyle.RPC;
/* 250 */       this.wrapped = true;
/* 251 */       if (soapBinding.parameterStyle().equals(SOAPBinding.ParameterStyle.BARE)) {
/* 252 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_RPC_LITERAL_MUST_NOT_BE_BARE(classElement
/* 253 */               .getQualifiedName()), bindingElement);
/*     */       }
/*     */     } else {
/* 256 */       this.soapStyle = SOAPStyle.DOCUMENT;
/* 257 */       if (this.wrapped != soapBinding.parameterStyle().equals(SOAPBinding.ParameterStyle.WRAPPED)) {
/* 258 */         this.wrapped = soapBinding.parameterStyle().equals(SOAPBinding.ParameterStyle.WRAPPED);
/* 259 */         changed = true;
/*     */       }
/*     */     }
/* 262 */     if (soapBinding.use().equals(SOAPBinding.Use.ENCODED)) {
/* 263 */       String style = "rpc";
/* 264 */       if (soapBinding.style().equals(SOAPBinding.Style.DOCUMENT))
/* 265 */         style = "document";
/* 266 */       this.builder.processError(WebserviceapMessages.WEBSERVICE_ENCODED_NOT_SUPPORTED(classElement
/* 267 */             .getQualifiedName(), style), bindingElement);
/*     */     }
/* 269 */     if (changed || this.soapBindingStack.empty()) {
/* 270 */       this.soapBindingStack.push(soapBinding);
/* 271 */       this.pushedSoapBinding = true;
/*     */     }
/* 273 */     return changed;
/*     */   }
/*     */
/*     */   protected SOAPBinding popSoapBinding() {
/* 277 */     if (this.pushedSoapBinding)
/* 278 */       this.soapBindingStack.pop();
/* 279 */     SOAPBinding soapBinding = null;
/* 280 */     if (!this.soapBindingStack.empty()) {
/* 281 */       soapBinding = this.soapBindingStack.peek();
/* 282 */       if (soapBinding.style().equals(SOAPBinding.Style.RPC)) {
/* 283 */         this.soapStyle = SOAPStyle.RPC;
/* 284 */         this.wrapped = true;
/*     */       } else {
/* 286 */         this.soapStyle = SOAPStyle.DOCUMENT;
/* 287 */         this.wrapped = soapBinding.parameterStyle().equals(SOAPBinding.ParameterStyle.WRAPPED);
/*     */       }
/*     */     } else {
/* 290 */       this.pushedSoapBinding = false;
/*     */     }
/* 292 */     return soapBinding;
/*     */   }
/*     */
/*     */   protected String getNamespace(PackageElement packageElement) {
/* 296 */     return RuntimeModeler.getNamespace(packageElement.getQualifiedName().toString());
/*     */   }
/*     */   protected boolean shouldProcessWebService(WebService webService, TypeElement element) {
/*     */     SOAPBinding soapBinding;
/* 300 */     switch (element.getKind()) {
/*     */       case INTERFACE:
/* 302 */         this.hasWebMethods = false;
/* 303 */         if (webService == null) {
/* 304 */           this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ENDPOINTINTERFACE_HAS_NO_WEBSERVICE_ANNOTATION(element
/* 305 */                 .getQualifiedName()), element);
/*     */         }
/* 307 */         soapBinding = element.<SOAPBinding>getAnnotation(SOAPBinding.class);
/* 308 */         if (soapBinding != null && soapBinding
/* 309 */           .style() == SOAPBinding.Style.RPC && soapBinding
/* 310 */           .parameterStyle() == SOAPBinding.ParameterStyle.BARE) {
/* 311 */           this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_SOAPBINDING_PARAMETERSTYLE(soapBinding, element), element);
/*     */
/* 313 */           return false;
/*     */         }
/* 315 */         return isLegalSei(element);
/*     */
/*     */       case CLASS:
/* 318 */         if (webService == null)
/* 319 */           return false;
/* 320 */         this.hasWebMethods = hasWebMethods(element);
/* 321 */         soapBinding = element.<SOAPBinding>getAnnotation(SOAPBinding.class);
/* 322 */         if (soapBinding != null && soapBinding
/* 323 */           .style() == SOAPBinding.Style.RPC && soapBinding
/* 324 */           .parameterStyle() == SOAPBinding.ParameterStyle.BARE) {
/* 325 */           this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_SOAPBINDING_PARAMETERSTYLE(soapBinding, element), element);
/*     */
/* 327 */           return false;
/*     */         }
/* 329 */         return isLegalImplementation(webService, element);
/*     */     }
/*     */
/* 332 */     throw new IllegalArgumentException("Class or Interface was expecting. But element: " + element);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected void postProcessWebService(WebService webService, TypeElement element) {
/* 340 */     processMethods(element);
/* 341 */     popSoapBinding();
/*     */   }
/*     */
/*     */   protected boolean hasWebMethods(TypeElement element) {
/* 345 */     if (element.getQualifiedName().toString().equals(Object.class.getName())) {
/* 346 */       return false;
/*     */     }
/* 348 */     for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
/* 349 */       WebMethod webMethod = method.<WebMethod>getAnnotation(WebMethod.class);
/* 350 */       if (webMethod != null) {
/* 351 */         if (webMethod.exclude()) {
/* 352 */           if (webMethod.operationName().length() > 0)
/* 353 */             this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_WEBMETHOD_ELEMENT_WITH_EXCLUDE("operationName", element
/* 354 */                   .getQualifiedName(), method.toString()), method);
/* 355 */           if (webMethod.action().length() > 0)
/* 356 */             this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_WEBMETHOD_ELEMENT_WITH_EXCLUDE("action", element
/* 357 */                   .getQualifiedName(), method.toString()), method);  continue;
/*     */         }
/* 359 */         return true;
/*     */       }
/*     */     }
/*     */
/* 363 */     return false;
/*     */   }
/*     */   protected void processMethods(TypeElement element) {
/*     */     TypeMirror superclass;
/* 367 */     switch (element.getKind()) {
/*     */       case INTERFACE:
/* 369 */         this.builder.log("ProcessedMethods Interface: " + element);
/* 370 */         this.hasWebMethods = false;
/* 371 */         for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
/* 372 */           method.accept(this, null);
/*     */         }
/* 374 */         for (TypeMirror superType : element.getInterfaces()) {
/* 375 */           processMethods((TypeElement)((DeclaredType)superType).asElement());
/*     */         }
/*     */         break;
/*     */       case CLASS:
/* 379 */         this.builder.log("ProcessedMethods Class: " + element);
/* 380 */         this.hasWebMethods = hasWebMethods(element);
/* 381 */         if (element.getQualifiedName().toString().equals(Object.class.getName()))
/*     */           return;
/* 383 */         if (element.getAnnotation(WebService.class) != null)
/*     */         {
/* 385 */           for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
/* 386 */             method.accept(this, null);
/*     */           }
/*     */         }
/* 389 */         superclass = element.getSuperclass();
/* 390 */         if (!superclass.getKind().equals(TypeKind.NONE)) {
/* 391 */           processMethods((TypeElement)((DeclaredType)superclass).asElement());
/*     */         }
/*     */         break;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private TypeElement getEndpointInterfaceElement(String endpointInterfaceName, TypeElement element) {
/* 401 */     TypeElement intTypeElement = null;
/* 402 */     for (TypeMirror interfaceType : element.getInterfaces()) {
/* 403 */       if (endpointInterfaceName.equals(interfaceType.toString())) {
/* 404 */         intTypeElement = (TypeElement)((DeclaredType)interfaceType).asElement();
/* 405 */         this.seiContext = this.context.getSeiContext(intTypeElement.getQualifiedName());
/* 406 */         assert this.seiContext != null;
/* 407 */         this.seiContext.setImplementsSei(true);
/*     */         break;
/*     */       }
/*     */     }
/* 411 */     if (intTypeElement == null) {
/* 412 */       intTypeElement = this.builder.getProcessingEnvironment().getElementUtils().getTypeElement(endpointInterfaceName);
/*     */     }
/* 414 */     if (intTypeElement == null)
/* 415 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ENDPOINTINTERFACE_CLASS_NOT_FOUND(endpointInterfaceName));
/* 416 */     return intTypeElement;
/*     */   }
/*     */
/*     */   private void inspectEndpointInterface(String endpointInterfaceName, TypeElement d) {
/* 420 */     TypeElement intTypeElement = getEndpointInterfaceElement(endpointInterfaceName, d);
/* 421 */     if (intTypeElement != null) {
/* 422 */       intTypeElement.accept(this, null);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Void visitExecutable(ExecutableElement method, Object o) {
/* 428 */     if (!method.getModifiers().contains(Modifier.PUBLIC))
/* 429 */       return null;
/* 430 */     if (processedMethod(method))
/* 431 */       return null;
/* 432 */     WebMethod webMethod = method.<WebMethod>getAnnotation(WebMethod.class);
/* 433 */     if (webMethod != null && webMethod.exclude())
/* 434 */       return null;
/* 435 */     SOAPBinding soapBinding = method.<SOAPBinding>getAnnotation(SOAPBinding.class);
/* 436 */     if (soapBinding == null && !method.getEnclosingElement().equals(this.typeElement) &&
/* 437 */       method.getEnclosingElement().getKind().equals(ElementKind.CLASS)) {
/* 438 */       soapBinding = method.getEnclosingElement().<SOAPBinding>getAnnotation(SOAPBinding.class);
/* 439 */       if (soapBinding != null) {
/* 440 */         this.builder.log("using " + method.getEnclosingElement() + "'s SOAPBinding.");
/*     */       } else {
/* 442 */         soapBinding = new MySoapBinding();
/*     */       }
/*     */     }
/*     */
/* 446 */     boolean newBinding = false;
/* 447 */     if (soapBinding != null) {
/* 448 */       newBinding = pushSoapBinding(soapBinding, method, this.typeElement);
/*     */     }
/*     */     try {
/* 451 */       if (shouldProcessMethod(method, webMethod)) {
/* 452 */         processMethod(method, webMethod);
/*     */       }
/*     */     } finally {
/* 455 */       if (newBinding) {
/* 456 */         popSoapBinding();
/*     */       }
/*     */     }
/* 459 */     return null;
/*     */   }
/*     */
/*     */   protected boolean processedMethod(ExecutableElement method) {
/* 463 */     String id = method.toString();
/* 464 */     if (this.processedMethods.contains(id))
/* 465 */       return true;
/* 466 */     this.processedMethods.add(id);
/* 467 */     return false;
/*     */   }
/*     */
/*     */   protected boolean shouldProcessMethod(ExecutableElement method, WebMethod webMethod) {
/* 471 */     this.builder.log("should process method: " + method.getSimpleName() + " hasWebMethods: " + this.hasWebMethods + " ");
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 479 */     Collection<Modifier> modifiers = method.getModifiers();
/* 480 */     boolean staticFinal = (modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.FINAL));
/* 481 */     if (staticFinal) {
/* 482 */       if (webMethod != null) {
/* 483 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_WEBSERVICE_METHOD_IS_STATIC_OR_FINAL(method.getEnclosingElement(), method), method);
/*     */       }
/*     */
/* 486 */       return false;
/*     */     }
/*     */
/*     */
/* 490 */     boolean result = (this.endpointReferencesInterface || method.getEnclosingElement().equals(this.typeElement) || method.getEnclosingElement().getAnnotation(WebService.class) != null);
/* 491 */     this.builder.log("endpointReferencesInterface: " + this.endpointReferencesInterface);
/* 492 */     this.builder.log("declaring class has WebService: " + ((method.getEnclosingElement().getAnnotation(WebService.class) != null) ? 1 : 0));
/* 493 */     this.builder.log("returning: " + result);
/* 494 */     return result;
/*     */   }
/*     */
/*     */
/*     */
/*     */   protected boolean isLegalImplementation(WebService webService, TypeElement classElement) {
/* 500 */     boolean isStateful = isStateful(classElement);
/*     */
/* 502 */     Collection<Modifier> modifiers = classElement.getModifiers();
/* 503 */     if (!modifiers.contains(Modifier.PUBLIC)) {
/* 504 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_WEBSERVICE_CLASS_NOT_PUBLIC(classElement.getQualifiedName()), classElement);
/* 505 */       return false;
/*     */     }
/* 507 */     if (modifiers.contains(Modifier.FINAL) && !isStateful) {
/* 508 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_WEBSERVICE_CLASS_IS_FINAL(classElement.getQualifiedName()), classElement);
/* 509 */       return false;
/*     */     }
/* 511 */     if (modifiers.contains(Modifier.ABSTRACT) && !isStateful) {
/* 512 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_WEBSERVICE_CLASS_IS_ABSTRACT(classElement.getQualifiedName()), classElement);
/* 513 */       return false;
/*     */     }
/* 515 */     boolean hasDefaultConstructor = false;
/* 516 */     for (ExecutableElement constructor : ElementFilter.constructorsIn(classElement.getEnclosedElements())) {
/* 517 */       if (constructor.getModifiers().contains(Modifier.PUBLIC) && constructor
/* 518 */         .getParameters().isEmpty()) {
/* 519 */         hasDefaultConstructor = true;
/*     */         break;
/*     */       }
/*     */     }
/* 523 */     if (!hasDefaultConstructor && !isStateful) {
/* 524 */       if (classElement.getEnclosingElement() != null && !modifiers.contains(Modifier.STATIC)) {
/* 525 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_WEBSERVICE_CLASS_IS_INNERCLASS_NOT_STATIC(classElement
/* 526 */               .getQualifiedName()), classElement);
/* 527 */         return false;
/*     */       }
/*     */
/* 530 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_WEBSERVICE_NO_DEFAULT_CONSTRUCTOR(classElement
/* 531 */             .getQualifiedName()), classElement);
/* 532 */       return false;
/*     */     }
/* 534 */     if (webService.endpointInterface().isEmpty()) {
/* 535 */       if (!methodsAreLegal(classElement))
/* 536 */         return false;
/*     */     } else {
/* 538 */       TypeElement interfaceElement = getEndpointInterfaceElement(webService.endpointInterface(), classElement);
/* 539 */       if (!classImplementsSei(classElement, interfaceElement)) {
/* 540 */         return false;
/*     */       }
/*     */     }
/* 543 */     return true;
/*     */   }
/*     */
/*     */
/*     */   private boolean isStateful(TypeElement classElement) {
/*     */     try {
/* 549 */       return (classElement.getAnnotation(Class.forName("com.sun.xml.internal.ws.developer.Stateful")) != null);
/* 550 */     } catch (ClassNotFoundException classNotFoundException) {
/*     */
/*     */
/* 553 */       return false;
/*     */     }
/*     */   }
/*     */   protected boolean classImplementsSei(TypeElement classElement, TypeElement interfaceElement) {
/* 557 */     for (TypeMirror interfaceType : classElement.getInterfaces()) {
/* 558 */       if (((DeclaredType)interfaceType).asElement().equals(interfaceElement))
/* 559 */         return true;
/*     */     }
/* 561 */     List<ExecutableElement> classMethods = getClassMethods(classElement);
/*     */
/* 563 */     for (ExecutableElement interfaceMethod : ElementFilter.methodsIn(interfaceElement.getEnclosedElements())) {
/* 564 */       boolean implementsMethod = false;
/* 565 */       for (ExecutableElement classMethod : classMethods) {
/* 566 */         if (sameMethod(interfaceMethod, classMethod)) {
/* 567 */           implementsMethod = true;
/* 568 */           classMethods.remove(classMethod);
/*     */           break;
/*     */         }
/*     */       }
/* 572 */       if (!implementsMethod) {
/* 573 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_METHOD_NOT_IMPLEMENTED(interfaceElement.getSimpleName(), classElement.getSimpleName(), interfaceMethod), interfaceMethod);
/* 574 */         return false;
/*     */       }
/*     */     }
/* 577 */     return true;
/*     */   }
/*     */
/*     */   private static List<ExecutableElement> getClassMethods(TypeElement classElement) {
/* 581 */     if (classElement.getQualifiedName().toString().equals(Object.class.getName()))
/* 582 */       return null;
/* 583 */     TypeElement superclassElement = (TypeElement)((DeclaredType)classElement.getSuperclass()).asElement();
/* 584 */     List<ExecutableElement> superclassesMethods = getClassMethods(superclassElement);
/* 585 */     List<ExecutableElement> classMethods = ElementFilter.methodsIn(classElement.getEnclosedElements());
/* 586 */     if (superclassesMethods == null) {
/* 587 */       return classMethods;
/*     */     }
/* 589 */     superclassesMethods.addAll(classMethods);
/* 590 */     return superclassesMethods;
/*     */   }
/*     */
/*     */   protected boolean sameMethod(ExecutableElement method1, ExecutableElement method2) {
/* 594 */     if (!method1.getSimpleName().equals(method2.getSimpleName()))
/* 595 */       return false;
/* 596 */     Types typeUtils = this.builder.getProcessingEnvironment().getTypeUtils();
/* 597 */     if (!typeUtils.isSameType(method1.getReturnType(), method2.getReturnType()) &&
/* 598 */       !typeUtils.isSubtype(method2.getReturnType(), method1.getReturnType()))
/* 599 */       return false;
/* 600 */     List<? extends VariableElement> parameters1 = method1.getParameters();
/* 601 */     List<? extends VariableElement> parameters2 = method2.getParameters();
/* 602 */     if (parameters1.size() != parameters2.size())
/* 603 */       return false;
/* 604 */     for (int i = 0; i < parameters1.size(); i++) {
/* 605 */       if (!typeUtils.isSameType(((VariableElement)parameters1.get(i)).asType(), ((VariableElement)parameters2.get(i)).asType()))
/* 606 */         return false;
/*     */     }
/* 608 */     return true;
/*     */   }
/*     */
/*     */   protected boolean isLegalSei(TypeElement interfaceElement) {
/* 612 */     for (VariableElement field : ElementFilter.fieldsIn(interfaceElement.getEnclosedElements())) {
/* 613 */       if (field.getConstantValue() != null) {
/* 614 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_SEI_CANNOT_CONTAIN_CONSTANT_VALUES(interfaceElement
/* 615 */               .getQualifiedName(), field.getSimpleName()));
/* 616 */         return false;
/*     */       }
/* 618 */     }  return methodsAreLegal(interfaceElement);
/*     */   } protected boolean methodsAreLegal(TypeElement element) {
/*     */     DeclaredType superClass;
/*     */     TypeElement tE;
/* 622 */     switch (element.getKind()) {
/*     */       case INTERFACE:
/* 624 */         this.hasWebMethods = false;
/* 625 */         for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
/* 626 */           if (!isLegalMethod(method, element))
/* 627 */             return false;
/*     */         }
/* 629 */         for (TypeMirror superInterface : element.getInterfaces()) {
/* 630 */           if (!methodsAreLegal((TypeElement)((DeclaredType)superInterface).asElement()))
/* 631 */             return false;
/*     */         }
/* 633 */         return true;
/*     */
/*     */       case CLASS:
/* 636 */         this.hasWebMethods = hasWebMethods(element);
/* 637 */         for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
/* 638 */           if (!method.getModifiers().contains(Modifier.PUBLIC))
/*     */             continue;
/* 640 */           if (!isLegalMethod(method, element))
/* 641 */             return false;
/*     */         }
/* 643 */         superClass = (DeclaredType)element.getSuperclass();
/*     */
/* 645 */         tE = (TypeElement)superClass.asElement();
/* 646 */         return (tE.getQualifiedName().toString().equals(Object.class.getName()) ||
/* 647 */           methodsAreLegal(tE));
/*     */     }
/*     */
/* 650 */     throw new IllegalArgumentException("Class or interface was expecting. But element: " + element);
/*     */   }
/*     */
/*     */
/*     */
/*     */   protected boolean isLegalMethod(ExecutableElement method, TypeElement typeElement) {
/* 656 */     WebMethod webMethod = method.<WebMethod>getAnnotation(WebMethod.class);
/*     */
/* 658 */     if (typeElement.getKind().equals(ElementKind.INTERFACE) && webMethod != null && webMethod.exclude()) {
/* 659 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_INVALID_SEI_ANNOTATION_ELEMENT_EXCLUDE("exclude=true", typeElement.getQualifiedName(), method.toString()), method);
/*     */     }
/* 661 */     if (this.hasWebMethods && webMethod == null) {
/* 662 */       return true;
/*     */     }
/* 664 */     if (webMethod != null && webMethod.exclude()) {
/* 665 */       return true;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 674 */     TypeMirror returnType = method.getReturnType();
/* 675 */     if (!isLegalType(returnType)) {
/* 676 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_METHOD_RETURN_TYPE_CANNOT_IMPLEMENT_REMOTE(typeElement.getQualifiedName(), method
/* 677 */             .getSimpleName(), returnType), method);
/*     */     }
/*     */
/* 680 */     boolean isOneWay = (method.getAnnotation(Oneway.class) != null);
/* 681 */     if (isOneWay && !isValidOneWayMethod(method, typeElement)) {
/* 682 */       return false;
/*     */     }
/* 684 */     SOAPBinding soapBinding = method.<SOAPBinding>getAnnotation(SOAPBinding.class);
/* 685 */     if (soapBinding != null &&
/* 686 */       soapBinding.style().equals(SOAPBinding.Style.RPC)) {
/* 687 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_RPC_SOAPBINDING_NOT_ALLOWED_ON_METHOD(typeElement.getQualifiedName(), method.toString()), method);
/*     */     }
/*     */
/*     */
/* 691 */     int paramIndex = 0;
/* 692 */     for (VariableElement parameter : method.getParameters()) {
/* 693 */       if (!isLegalParameter(parameter, method, typeElement, paramIndex++)) {
/* 694 */         return false;
/*     */       }
/*     */     }
/* 697 */     if (!isDocLitWrapped() && this.soapStyle.equals(SOAPStyle.DOCUMENT)) {
/* 698 */       VariableElement outParam = getOutParameter(method);
/* 699 */       int inParams = getModeParameterCount(method, WebParam.Mode.IN);
/* 700 */       int outParams = getModeParameterCount(method, WebParam.Mode.OUT);
/* 701 */       if (inParams != 1) {
/* 702 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_DOC_BARE_AND_NO_ONE_IN(typeElement.getQualifiedName(), method.toString()), method);
/*     */       }
/* 704 */       if (((Boolean)returnType.accept(NO_TYPE_VISITOR, null)).booleanValue()) {
/* 705 */         if (outParam == null && !isOneWay) {
/* 706 */           this.builder.processError(WebserviceapMessages.WEBSERVICEAP_DOC_BARE_NO_OUT(typeElement.getQualifiedName(), method.toString()), method);
/*     */         }
/* 708 */         if (outParams != 1 &&
/* 709 */           !isOneWay && outParams != 0) {
/* 710 */           this.builder.processError(WebserviceapMessages.WEBSERVICEAP_DOC_BARE_NO_RETURN_AND_NO_OUT(typeElement.getQualifiedName(), method.toString()), method);
/*     */         }
/*     */       }
/* 713 */       else if (outParams > 0) {
/* 714 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_DOC_BARE_RETURN_AND_OUT(typeElement.getQualifiedName(), method.toString()), outParam);
/*     */       }
/*     */     }
/*     */
/* 718 */     return true;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected boolean isLegalParameter(VariableElement param, ExecutableElement method, TypeElement typeElement, int paramIndex) {
/* 725 */     if (!isLegalType(param.asType())) {
/* 726 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_METHOD_PARAMETER_TYPES_CANNOT_IMPLEMENT_REMOTE(typeElement.getQualifiedName(), method
/* 727 */             .getSimpleName(), param
/* 728 */             .getSimpleName(), param
/* 729 */             .asType().toString()), param);
/* 730 */       return false;
/*     */     }
/*     */
/* 733 */     TypeMirror holderType = this.builder.getHolderValueType(param.asType());
/* 734 */     WebParam webParam = param.<WebParam>getAnnotation(WebParam.class);
/* 735 */     WebParam.Mode mode = null;
/* 736 */     if (webParam != null) {
/* 737 */       mode = webParam.mode();
/*     */     }
/* 739 */     if (holderType != null) {
/* 740 */       if (mode != null && mode == WebParam.Mode.IN)
/* 741 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_HOLDER_PARAMETERS_MUST_NOT_BE_IN_ONLY(typeElement.getQualifiedName(), method.toString(), Integer.valueOf(paramIndex)), param);
/* 742 */     } else if (mode != null && mode != WebParam.Mode.IN) {
/* 743 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_NON_IN_PARAMETERS_MUST_BE_HOLDER(typeElement.getQualifiedName(), method.toString(), Integer.valueOf(paramIndex)), param);
/*     */     }
/*     */
/* 746 */     return true;
/*     */   }
/*     */
/*     */   protected boolean isDocLitWrapped() {
/* 750 */     return (this.soapStyle.equals(SOAPStyle.DOCUMENT) && this.wrapped);
/*     */   }
/*     */
/*     */   private static final class NoTypeVisitor extends SimpleTypeVisitor6<Boolean, Void> {
/*     */     private NoTypeVisitor() {}
/*     */
/*     */     public Boolean visitNoType(NoType t, Void o) {
/* 757 */       return Boolean.valueOf(true);
/*     */     }
/*     */
/*     */
/*     */     protected Boolean defaultAction(TypeMirror e, Void aVoid) {
/* 762 */       return Boolean.valueOf(false);
/*     */     }
/*     */   }
/*     */
/*     */   protected boolean isValidOneWayMethod(ExecutableElement method, TypeElement typeElement) {
/* 767 */     boolean valid = true;
/* 768 */     if (!((Boolean)method.getReturnType().accept(NO_TYPE_VISITOR, null)).booleanValue()) {
/*     */
/* 770 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ONEWAY_OPERATION_CANNOT_HAVE_RETURN_TYPE(typeElement.getQualifiedName(), method.toString()), method);
/* 771 */       valid = false;
/*     */     }
/* 773 */     VariableElement outParam = getOutParameter(method);
/* 774 */     if (outParam != null) {
/* 775 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ONEWAY_AND_OUT(typeElement.getQualifiedName(), method.toString()), outParam);
/* 776 */       valid = false;
/*     */     }
/* 778 */     if (!isDocLitWrapped() && this.soapStyle.equals(SOAPStyle.DOCUMENT)) {
/* 779 */       int inCnt = getModeParameterCount(method, WebParam.Mode.IN);
/* 780 */       if (inCnt != 1) {
/* 781 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ONEWAY_AND_NOT_ONE_IN(typeElement.getQualifiedName(), method.toString()), method);
/* 782 */         valid = false;
/*     */       }
/*     */     }
/* 785 */     for (TypeMirror thrownType : method.getThrownTypes()) {
/* 786 */       TypeElement thrownElement = (TypeElement)((DeclaredType)thrownType).asElement();
/* 787 */       if (this.builder.isServiceException(thrownType)) {
/* 788 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_ONEWAY_OPERATION_CANNOT_DECLARE_EXCEPTIONS(typeElement
/* 789 */               .getQualifiedName(), method.toString(), thrownElement.getQualifiedName()), method);
/* 790 */         valid = false;
/*     */       }
/*     */     }
/* 793 */     return valid;
/*     */   }
/*     */
/*     */
/*     */   protected int getModeParameterCount(ExecutableElement method, WebParam.Mode mode) {
/* 798 */     int cnt = 0;
/* 799 */     for (VariableElement param : method.getParameters()) {
/* 800 */       WebParam webParam = param.<WebParam>getAnnotation(WebParam.class);
/* 801 */       if (webParam != null) {
/* 802 */         if (webParam.header())
/*     */           continue;
/* 804 */         if (isEquivalentModes(mode, webParam.mode()))
/* 805 */           cnt++;  continue;
/*     */       }
/* 807 */       if (isEquivalentModes(mode, WebParam.Mode.IN)) {
/* 808 */         cnt++;
/*     */       }
/*     */     }
/*     */
/* 812 */     return cnt;
/*     */   }
/*     */
/*     */   protected boolean isEquivalentModes(WebParam.Mode mode1, WebParam.Mode mode2) {
/* 816 */     if (mode1.equals(mode2))
/* 817 */       return true;
/* 818 */     assert mode1 == WebParam.Mode.IN || mode1 == WebParam.Mode.OUT;
/* 819 */     return ((mode1 == WebParam.Mode.IN && mode2 != WebParam.Mode.OUT) || (mode1 == WebParam.Mode.OUT && mode2 != WebParam.Mode.IN));
/*     */   }
/*     */
/*     */   protected boolean isHolder(VariableElement param) {
/* 823 */     return (this.builder.getHolderValueType(param.asType()) != null);
/*     */   }
/*     */
/*     */   protected boolean isLegalType(TypeMirror type) {
/* 827 */     if (type == null || !type.getKind().equals(TypeKind.DECLARED))
/* 828 */       return true;
/* 829 */     TypeElement tE = (TypeElement)((DeclaredType)type).asElement();
/* 830 */     if (tE == null)
/*     */     {
/* 832 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_COULD_NOT_FIND_TYPEDECL(type.toString(), Integer.valueOf(this.context.getRound())));
/*     */     }
/* 834 */     return !this.builder.isRemote(tE);
/*     */   }
/*     */
/*     */
/*     */   protected VariableElement getOutParameter(ExecutableElement method) {
/* 839 */     for (VariableElement param : method.getParameters()) {
/* 840 */       WebParam webParam = param.<WebParam>getAnnotation(WebParam.class);
/* 841 */       if (webParam != null && webParam.mode() != WebParam.Mode.IN) {
/* 842 */         return param;
/*     */       }
/*     */     }
/* 845 */     return null;
/*     */   }
/*     */   protected abstract void processWebService(WebService paramWebService, TypeElement paramTypeElement);
/*     */
/*     */   protected abstract void processMethod(ExecutableElement paramExecutableElement, WebMethod paramWebMethod);
/*     */
/*     */   protected static class MySoapBinding implements SOAPBinding { public Style style() {
/* 852 */       return Style.DOCUMENT;
/*     */     }
/*     */
/*     */
/*     */     public Use use() {
/* 857 */       return Use.LITERAL;
/*     */     }
/*     */
/*     */
/*     */     public ParameterStyle parameterStyle() {
/* 862 */       return ParameterStyle.WRAPPED;
/*     */     }
/*     */
/*     */
/*     */     public Class<? extends Annotation> annotationType() {
/* 867 */       return (Class)SOAPBinding.class;
/*     */     } }
/*     */
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\WebServiceVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
