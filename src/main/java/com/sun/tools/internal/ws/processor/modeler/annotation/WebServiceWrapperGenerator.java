/*     */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*     */
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.CodeWriter;
/*     */ import com.sun.codemodel.internal.JAnnotationArrayMember;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JCommentPart;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.codemodel.internal.writer.ProgressCodeWriter;
/*     */ import com.sun.tools.internal.jxc.ap.InlineAnnotationReaderImpl;
/*     */ import com.sun.tools.internal.jxc.model.nav.ApNavigator;
/*     */ import com.sun.tools.internal.ws.ToolVersion;
/*     */ import com.sun.tools.internal.ws.processor.generator.GeneratorBase;
/*     */ import com.sun.tools.internal.ws.processor.generator.GeneratorConstants;
/*     */ import com.sun.tools.internal.ws.processor.generator.Names;
/*     */ import com.sun.tools.internal.ws.processor.modeler.ModelerException;
/*     */ import com.sun.tools.internal.ws.processor.util.DirectoryUtil;
/*     */ import com.sun.tools.internal.ws.resources.WebserviceapMessages;
/*     */ import com.sun.tools.internal.ws.util.ClassNameInfo;
/*     */ import com.sun.tools.internal.ws.wscompile.FilerCodeWriter;
/*     */ import com.sun.tools.internal.ws.wscompile.Options;
/*     */ import com.sun.tools.internal.ws.wscompile.WsgenOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.ws.model.AbstractWrapperBeanGenerator;
/*     */ import com.sun.xml.internal.ws.spi.db.BindingHelper;
/*     */ import com.sun.xml.internal.ws.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.jws.Oneway;
/*     */ import javax.jws.WebMethod;
/*     */ import javax.jws.WebService;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.MirroredTypeException;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.XmlMimeType;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.RequestWrapper;
/*     */ import javax.xml.ws.ResponseWrapper;
/*     */ import javax.xml.ws.WebFault;
/*     */ import javax.xml.ws.WebServiceException;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class WebServiceWrapperGenerator
/*     */   extends WebServiceVisitor
/*     */ {
/*     */   private Set<String> wrapperNames;
/*     */   private Set<String> processedExceptions;
/*     */   private JCodeModel cm;
/*     */   private final MakeSafeTypeVisitor makeSafeVisitor;
/* 113 */   private static final FieldFactory FIELD_FACTORY = new FieldFactory();
/*     */
/* 115 */   private final AbstractWrapperBeanGenerator ap_generator = new ApWrapperBeanGenerator((AnnotationReader<TypeMirror, TypeElement, ?, ExecutableElement>)InlineAnnotationReaderImpl.theInstance, (Navigator<TypeMirror, TypeElement, ?, ExecutableElement>)new ApNavigator(this.builder
/*     */
/* 117 */         .getProcessingEnvironment()), FIELD_FACTORY);
/*     */
/*     */
/*     */   private final class ApWrapperBeanGenerator
/*     */     extends AbstractWrapperBeanGenerator<TypeMirror, TypeElement, ExecutableElement, MemberInfo>
/*     */   {
/*     */     protected ApWrapperBeanGenerator(AnnotationReader<TypeMirror, TypeElement, ?, ExecutableElement> annReader, Navigator<TypeMirror, TypeElement, ?, ExecutableElement> nav, BeanMemberFactory<TypeMirror, MemberInfo> beanMemberFactory) {
/* 124 */       super(annReader, nav, beanMemberFactory);
/*     */     }
/*     */
/*     */
/*     */     protected TypeMirror getSafeType(TypeMirror type) {
/* 129 */       return WebServiceWrapperGenerator.this.getSafeType(type);
/*     */     }
/*     */
/*     */
/*     */     protected TypeMirror getHolderValueType(TypeMirror paramType) {
/* 134 */       return WebServiceWrapperGenerator.this.builder.getHolderValueType(paramType);
/*     */     }
/*     */
/*     */
/*     */     protected boolean isVoidType(TypeMirror type) {
/* 139 */       return (type != null && type.getKind().equals(TypeKind.VOID));
/*     */     }
/*     */   }
/*     */
/*     */   private static final class FieldFactory
/*     */     implements AbstractWrapperBeanGenerator.BeanMemberFactory<TypeMirror, MemberInfo>
/*     */   {
/*     */     private FieldFactory() {}
/*     */
/*     */     public MemberInfo createWrapperBeanMember(TypeMirror paramType, String paramName, List<Annotation> jaxb) {
/* 149 */       return new MemberInfo(paramType, paramName, jaxb);
/*     */     }
/*     */   }
/*     */
/*     */   public WebServiceWrapperGenerator(ModelBuilder builder, AnnotationProcessorContext context) {
/* 154 */     super(builder, context);
/* 155 */     this.makeSafeVisitor = new MakeSafeTypeVisitor(builder.getProcessingEnvironment());
/*     */   }
/*     */
/*     */
/*     */   protected void processWebService(WebService webService, TypeElement d) {
/* 160 */     this.cm = new JCodeModel();
/* 161 */     this.wrapperNames = new HashSet<>();
/* 162 */     this.processedExceptions = new HashSet<>();
/*     */   }
/*     */
/*     */
/*     */   protected void postProcessWebService(WebService webService, TypeElement d) {
/* 167 */     super.postProcessWebService(webService, d);
/* 168 */     doPostProcessWebService(webService, d);
/*     */   }
/*     */
/*     */
/*     */   protected void doPostProcessWebService(WebService webService, TypeElement d) {
/* 173 */     if (this.cm != null) {
/* 174 */       File sourceDir = this.builder.getSourceDir();
/* 175 */       assert sourceDir != null;
/* 176 */       WsgenOptions options = this.builder.getOptions(); try {
/*     */         ProgressCodeWriter progressCodeWriter;
/* 178 */         FilerCodeWriter filerCodeWriter = new FilerCodeWriter(sourceDir, (Options)options);
/* 179 */         if (options.verbose)
/* 180 */           progressCodeWriter = new ProgressCodeWriter((CodeWriter)filerCodeWriter, System.out);
/* 181 */         this.cm.build((CodeWriter)progressCodeWriter);
/* 182 */       } catch (IOException e) {
/* 183 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   protected void processMethod(ExecutableElement method, WebMethod webMethod) {
/* 190 */     this.builder.log("WrapperGen - method: " + method);
/* 191 */     this.builder.log("method.getDeclaringType(): " + method.asType());
/* 192 */     if (this.wrapped && this.soapStyle.equals(SOAPStyle.DOCUMENT)) {
/* 193 */       generateWrappers(method, webMethod);
/*     */     }
/* 195 */     generateExceptionBeans(method);
/*     */   }
/*     */
/*     */   private boolean generateExceptionBeans(ExecutableElement method) {
/* 199 */     String beanPackage = this.packageName + WebServiceConstants.PD_JAXWS_PACKAGE_PD.getValue();
/* 200 */     if (this.packageName.length() == 0)
/* 201 */       beanPackage = WebServiceConstants.JAXWS_PACKAGE_PD.getValue();
/* 202 */     boolean beanGenerated = false;
/* 203 */     for (TypeMirror thrownType : method.getThrownTypes()) {
/* 204 */       TypeElement typeDecl = (TypeElement)((DeclaredType)thrownType).asElement();
/* 205 */       if (typeDecl == null) {
/* 206 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_COULD_NOT_FIND_TYPEDECL(thrownType
/* 207 */               .toString(), Integer.valueOf(this.context.getRound())));
/* 208 */         return false;
/*     */       }
/* 210 */       boolean tmp = generateExceptionBean(typeDecl, beanPackage);
/* 211 */       beanGenerated = (beanGenerated || tmp);
/*     */     }
/* 213 */     return beanGenerated;
/*     */   }
/*     */
/*     */   private boolean duplicateName(String name) {
/* 217 */     for (String str : this.wrapperNames) {
/* 218 */       if (str.equalsIgnoreCase(name))
/* 219 */         return true;
/*     */     }
/* 221 */     this.wrapperNames.add(name);
/* 222 */     return false;
/*     */   }
/*     */
/*     */   private boolean generateWrappers(ExecutableElement method, WebMethod webMethod) {
/* 226 */     boolean isOneway = (method.getAnnotation(Oneway.class) != null);
/* 227 */     String beanPackage = this.packageName + WebServiceConstants.PD_JAXWS_PACKAGE_PD.getValue();
/* 228 */     if (this.packageName.length() == 0)
/* 229 */       beanPackage = WebServiceConstants.JAXWS_PACKAGE_PD.getValue();
/* 230 */     Name methodName = method.getSimpleName();
/* 231 */     String operationName = this.builder.getOperationName(methodName);
/*     */
/* 233 */     operationName = (webMethod != null && webMethod.operationName().length() > 0) ? webMethod.operationName() : operationName;
/* 234 */     String reqName = operationName;
/* 235 */     String resName = operationName + WebServiceConstants.RESPONSE.getValue();
/* 236 */     String reqNamespace = this.typeNamespace;
/* 237 */     String resNamespace = this.typeNamespace;
/*     */
/* 239 */     String requestClassName = beanPackage + StringUtils.capitalize(method.getSimpleName().toString());
/* 240 */     RequestWrapper reqWrapper = method.<RequestWrapper>getAnnotation(RequestWrapper.class);
/* 241 */     if (reqWrapper != null) {
/* 242 */       if (reqWrapper.className().length() > 0)
/* 243 */         requestClassName = reqWrapper.className();
/* 244 */       if (reqWrapper.localName().length() > 0)
/* 245 */         reqName = reqWrapper.localName();
/* 246 */       if (reqWrapper.targetNamespace().length() > 0)
/* 247 */         reqNamespace = reqWrapper.targetNamespace();
/*     */     }
/* 249 */     this.builder.log("requestWrapper: " + requestClassName);
/*     */
/*     */
/* 252 */     File file = new File(DirectoryUtil.getOutputDirectoryFor(requestClassName, this.builder.getSourceDir()), Names.stripQualifier(requestClassName) + GeneratorConstants.JAVA_SRC_SUFFIX.getValue());
/* 253 */     this.builder.getOptions().addGeneratedFile(file);
/*     */
/* 255 */     boolean canOverwriteRequest = this.builder.canOverWriteClass(requestClassName);
/* 256 */     if (!canOverwriteRequest) {
/* 257 */       this.builder.log("Class " + requestClassName + " exists. Not overwriting.");
/*     */     }
/* 259 */     if (duplicateName(requestClassName) && canOverwriteRequest) {
/* 260 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_METHOD_REQUEST_WRAPPER_BEAN_NAME_NOT_UNIQUE(this.typeElement
/* 261 */             .getQualifiedName(), method.toString()));
/*     */     }
/*     */
/* 264 */     String responseClassName = null;
/* 265 */     boolean canOverwriteResponse = canOverwriteRequest;
/* 266 */     if (!isOneway) {
/* 267 */       responseClassName = beanPackage + StringUtils.capitalize(method.getSimpleName().toString()) + WebServiceConstants.RESPONSE.getValue();
/* 268 */       ResponseWrapper resWrapper = method.<ResponseWrapper>getAnnotation(ResponseWrapper.class);
/* 269 */       if (resWrapper != null) {
/* 270 */         if (resWrapper.className().length() > 0)
/* 271 */           responseClassName = resWrapper.className();
/* 272 */         if (resWrapper.localName().length() > 0)
/* 273 */           resName = resWrapper.localName();
/* 274 */         if (resWrapper.targetNamespace().length() > 0)
/* 275 */           resNamespace = resWrapper.targetNamespace();
/*     */       }
/* 277 */       canOverwriteResponse = this.builder.canOverWriteClass(responseClassName);
/* 278 */       if (!canOverwriteResponse) {
/* 279 */         this.builder.log("Class " + responseClassName + " exists. Not overwriting.");
/*     */       }
/* 281 */       if (duplicateName(responseClassName) && canOverwriteResponse) {
/* 282 */         this.builder.processError(WebserviceapMessages.WEBSERVICEAP_METHOD_RESPONSE_WRAPPER_BEAN_NAME_NOT_UNIQUE(this.typeElement
/* 283 */               .getQualifiedName(), method.toString()));
/*     */       }
/*     */
/* 286 */       file = new File(DirectoryUtil.getOutputDirectoryFor(responseClassName, this.builder.getSourceDir()), Names.stripQualifier(responseClassName) + GeneratorConstants.JAVA_SRC_SUFFIX.getValue());
/* 287 */       this.builder.getOptions().addGeneratedFile(file);
/*     */     }
/*     */
/*     */
/* 291 */     WrapperInfo reqWrapperInfo = new WrapperInfo(requestClassName);
/*     */
/* 293 */     WrapperInfo resWrapperInfo = null;
/* 294 */     if (!isOneway) {
/* 295 */       resWrapperInfo = new WrapperInfo(responseClassName);
/*     */     }
/*     */
/* 298 */     this.seiContext.setReqWrapperOperation(method, reqWrapperInfo);
/* 299 */     if (!isOneway)
/* 300 */       this.seiContext.setResWrapperOperation(method, resWrapperInfo);
/*     */     try {
/* 302 */       if (!canOverwriteRequest && !canOverwriteResponse) {
/* 303 */         return false;
/*     */       }
/*     */
/* 306 */       JDefinedClass reqCls = null;
/* 307 */       if (canOverwriteRequest) {
/* 308 */         reqCls = getCMClass(requestClassName, ClassType.CLASS);
/*     */       }
/*     */
/* 311 */       JDefinedClass resCls = null;
/* 312 */       if (!isOneway && canOverwriteResponse) {
/* 313 */         resCls = getCMClass(responseClassName, ClassType.CLASS);
/*     */       }
/*     */
/*     */
/* 317 */       writeXmlElementDeclaration(reqCls, reqName, reqNamespace);
/* 318 */       writeXmlElementDeclaration(resCls, resName, resNamespace);
/*     */
/* 320 */       List<MemberInfo> reqMembers = this.ap_generator.collectRequestBeanMembers(method);
/* 321 */       List<MemberInfo> resMembers = this.ap_generator.collectResponseBeanMembers(method);
/*     */
/*     */
/* 324 */       writeXmlTypeDeclaration(reqCls, reqName, reqNamespace, reqMembers);
/* 325 */       writeXmlTypeDeclaration(resCls, resName, resNamespace, resMembers);
/*     */
/*     */
/* 328 */       writeMembers(reqCls, reqMembers);
/* 329 */       writeMembers(resCls, resMembers);
/*     */     }
/* 331 */     catch (Exception e) {
/* 332 */       throw new ModelerException("modeler.nestedGeneratorError", new Object[] { e });
/*     */     }
/* 334 */     return true;
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
/*     */   private TypeMirror getSafeType(TypeMirror type) {
/* 349 */     return this.makeSafeVisitor.visit(type, this.builder.getProcessingEnvironment().getTypeUtils());
/*     */   }
/*     */
/*     */   private JType getType(TypeMirror typeMirror) {
/* 353 */     String type = typeMirror.toString();
/*     */
/*     */     try {
/* 356 */       return this.cm.parseType(type);
/*     */     }
/* 358 */     catch (ClassNotFoundException e) {
/* 359 */       return (JType)this.cm.ref(type);
/*     */     }
/*     */   }
/*     */
/*     */   private void writeMembers(JDefinedClass cls, Collection<MemberInfo> members) {
/* 364 */     if (cls == null)
/*     */       return;
/* 366 */     for (MemberInfo memInfo : members) {
/* 367 */       JType type = getType(memInfo.getParamType());
/* 368 */       JFieldVar field = cls.field(4, type, memInfo.getParamName());
/* 369 */       annotateParameterWithJaxbAnnotations(memInfo, field);
/*     */     }
/* 371 */     for (MemberInfo memInfo : members) {
/* 372 */       writeMember(cls, memInfo.getParamType(), memInfo
/* 373 */           .getParamName());
/*     */     }
/*     */   }
/*     */
/*     */   private void annotateParameterWithJaxbAnnotations(MemberInfo memInfo, JFieldVar field) {
/* 378 */     List<Annotation> jaxbAnnotations = memInfo.getJaxbAnnotations();
/* 379 */     for (Annotation ann : jaxbAnnotations) {
/* 380 */       if (ann instanceof XmlMimeType) {
/* 381 */         JAnnotationUse jaxbAnn = field.annotate(XmlMimeType.class);
/* 382 */         jaxbAnn.param("value", ((XmlMimeType)ann).value()); continue;
/* 383 */       }  if (ann instanceof XmlJavaTypeAdapter) {
/* 384 */         JAnnotationUse jaxbAnn = field.annotate(XmlJavaTypeAdapter.class);
/* 385 */         XmlJavaTypeAdapter ja = (XmlJavaTypeAdapter)ann;
/*     */         try {
/* 387 */           ja.value();
/* 388 */           throw new AssertionError();
/* 389 */         } catch (MirroredTypeException e) {
/* 390 */           jaxbAnn.param("value", getType(e.getTypeMirror())); continue;
/*     */         }
/*     */       }
/* 393 */       if (ann instanceof XmlAttachmentRef) {
/* 394 */         field.annotate(XmlAttachmentRef.class); continue;
/* 395 */       }  if (ann instanceof XmlList) {
/* 396 */         field.annotate(XmlList.class); continue;
/* 397 */       }  if (ann instanceof XmlElement) {
/* 398 */         XmlElement elemAnn = (XmlElement)ann;
/* 399 */         JAnnotationUse jAnn = field.annotate(XmlElement.class);
/* 400 */         jAnn.param("name", elemAnn.name());
/* 401 */         jAnn.param("namespace", elemAnn.namespace());
/* 402 */         if (elemAnn.nillable()) {
/* 403 */           jAnn.param("nillable", true);
/*     */         }
/* 405 */         if (elemAnn.required())
/* 406 */           jAnn.param("required", true);
/*     */         continue;
/*     */       }
/* 409 */       throw new WebServiceException("SEI Parameter cannot have this JAXB annotation: " + ann);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   protected JDefinedClass getCMClass(String className, ClassType type) {
/*     */     JDefinedClass cls;
/*     */     try {
/* 417 */       cls = this.cm._class(className, type);
/* 418 */     } catch (JClassAlreadyExistsException e) {
/* 419 */       cls = this.cm._getClass(className);
/*     */     }
/* 421 */     return cls;
/*     */   }
/*     */
/*     */   private boolean generateExceptionBean(TypeElement thrownDecl, String beanPackage) {
/* 425 */     if (!this.builder.isServiceException(thrownDecl.asType())) {
/* 426 */       return false;
/*     */     }
/* 428 */     String exceptionName = ClassNameInfo.getName(thrownDecl.getQualifiedName().toString());
/* 429 */     if (this.processedExceptions.contains(exceptionName))
/* 430 */       return false;
/* 431 */     this.processedExceptions.add(exceptionName);
/* 432 */     WebFault webFault = thrownDecl.<WebFault>getAnnotation(WebFault.class);
/* 433 */     String className = beanPackage + exceptionName + WebServiceConstants.BEAN.getValue();
/*     */
/* 435 */     Collection<MemberInfo> members = this.ap_generator.collectExceptionBeanMembers(thrownDecl);
/* 436 */     boolean isWSDLException = isWSDLException(members, thrownDecl);
/* 437 */     String namespace = this.typeNamespace;
/* 438 */     String name = exceptionName;
/*     */
/* 440 */     if (isWSDLException) {
/* 441 */       TypeMirror beanType = getFaultInfoMember(members).getParamType();
/* 442 */       FaultInfo faultInfo1 = new FaultInfo(TypeMonikerFactory.getTypeMoniker(beanType), true);
/*     */
/* 444 */       namespace = (webFault.targetNamespace().length() > 0) ? webFault.targetNamespace() : namespace;
/*     */
/* 446 */       name = (webFault.name().length() > 0) ? webFault.name() : name;
/* 447 */       faultInfo1.setElementName(new QName(namespace, name));
/* 448 */       this.seiContext.addExceptionBeanEntry(thrownDecl.getQualifiedName(), faultInfo1, this.builder);
/* 449 */       return false;
/*     */     }
/* 451 */     if (webFault != null) {
/*     */
/* 453 */       namespace = (webFault.targetNamespace().length() > 0) ? webFault.targetNamespace() : namespace;
/*     */
/* 455 */       name = (webFault.name().length() > 0) ? webFault.name() : name;
/*     */
/* 457 */       className = (webFault.faultBean().length() > 0) ? webFault.faultBean() : className;
/*     */     }
/*     */
/* 460 */     JDefinedClass cls = getCMClass(className, ClassType.CLASS);
/* 461 */     FaultInfo faultInfo = new FaultInfo(className, false);
/*     */
/* 463 */     if (duplicateName(className)) {
/* 464 */       this.builder.processError(WebserviceapMessages.WEBSERVICEAP_METHOD_EXCEPTION_BEAN_NAME_NOT_UNIQUE(this.typeElement
/* 465 */             .getQualifiedName(), thrownDecl.getQualifiedName()));
/*     */     }
/*     */
/* 468 */     boolean canOverWriteBean = this.builder.canOverWriteClass(className);
/* 469 */     if (!canOverWriteBean) {
/* 470 */       this.builder.log("Class " + className + " exists. Not overwriting.");
/* 471 */       this.seiContext.addExceptionBeanEntry(thrownDecl.getQualifiedName(), faultInfo, this.builder);
/* 472 */       return false;
/*     */     }
/* 474 */     if (this.seiContext.getExceptionBeanName(thrownDecl.getQualifiedName()) != null) {
/* 475 */       return false;
/*     */     }
/*     */
/* 478 */     JDocComment comment = cls.javadoc();
/* 479 */     for (String doc : GeneratorBase.getJAXWSClassComment(ToolVersion.VERSION.MAJOR_VERSION)) {
/* 480 */       comment.add(doc);
/*     */     }
/*     */
/*     */
/* 484 */     writeXmlElementDeclaration(cls, name, namespace);
/*     */
/*     */
/*     */
/* 488 */     XmlType xmlType = thrownDecl.<XmlType>getAnnotation(XmlType.class);
/* 489 */     String xmlTypeName = (xmlType != null && !xmlType.name().equals("##default")) ? xmlType.name() : exceptionName;
/* 490 */     String xmlTypeNamespace = (xmlType != null && !xmlType.namespace().equals("##default")) ? xmlType.namespace() : this.typeNamespace;
/* 491 */     writeXmlTypeDeclaration(cls, xmlTypeName, xmlTypeNamespace, members);
/*     */
/* 493 */     writeMembers(cls, members);
/*     */
/* 495 */     this.seiContext.addExceptionBeanEntry(thrownDecl.getQualifiedName(), faultInfo, this.builder);
/* 496 */     return true;
/*     */   }
/*     */
/*     */   protected boolean isWSDLException(Collection<MemberInfo> members, TypeElement thrownDecl) {
/* 500 */     WebFault webFault = thrownDecl.<WebFault>getAnnotation(WebFault.class);
/* 501 */     return (webFault != null && members.size() == 2 && getFaultInfoMember(members) != null);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private MemberInfo getFaultInfoMember(Collection<MemberInfo> members) {
/* 509 */     for (MemberInfo member : members) {
/* 510 */       if (member.getParamName().equals(WebServiceConstants.FAULT_INFO.getValue())) {
/* 511 */         return member;
/*     */       }
/*     */     }
/* 514 */     return null;
/*     */   }
/*     */
/*     */
/*     */   private void writeXmlElementDeclaration(JDefinedClass cls, String elementName, String namespaceUri) {
/* 519 */     if (cls == null)
/*     */       return;
/* 521 */     JAnnotationUse xmlRootElementAnn = cls.annotate(XmlRootElement.class);
/* 522 */     xmlRootElementAnn.param("name", elementName);
/* 523 */     if (namespaceUri.length() > 0) {
/* 524 */       xmlRootElementAnn.param("namespace", namespaceUri);
/*     */     }
/* 526 */     JAnnotationUse xmlAccessorTypeAnn = cls.annotate(this.cm.ref(XmlAccessorType.class));
/* 527 */     xmlAccessorTypeAnn.param("value", XmlAccessType.FIELD);
/*     */   }
/*     */
/*     */
/*     */   private void writeXmlTypeDeclaration(JDefinedClass cls, String typeName, String namespaceUri, Collection<MemberInfo> members) {
/* 532 */     if (cls == null)
/*     */       return;
/* 534 */     JAnnotationUse xmlTypeAnn = cls.annotate(this.cm.ref(XmlType.class));
/* 535 */     xmlTypeAnn.param("name", typeName);
/* 536 */     xmlTypeAnn.param("namespace", namespaceUri);
/* 537 */     if (members.size() > 1) {
/* 538 */       JAnnotationArrayMember paramArray = xmlTypeAnn.paramArray("propOrder");
/* 539 */       for (MemberInfo memInfo : members) {
/* 540 */         paramArray.param(memInfo.getParamName());
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private void writeMember(JDefinedClass cls, TypeMirror paramType, String paramName) {
/* 548 */     if (cls == null) {
/*     */       return;
/*     */     }
/* 551 */     String accessorName = BindingHelper.mangleNameToPropertyName(paramName);
/* 552 */     String getterPrefix = paramType.toString().equals("boolean") ? "is" : "get";
/* 553 */     JType propType = getType(paramType);
/* 554 */     JMethod m = cls.method(1, propType, getterPrefix + accessorName);
/* 555 */     JDocComment methodDoc = m.javadoc();
/* 556 */     JCommentPart ret = methodDoc.addReturn();
/* 557 */     ret.add("returns " + propType.name());
/* 558 */     JBlock body = m.body();
/* 559 */     body._return((JExpression)JExpr._this().ref(paramName));
/*     */
/* 561 */     m = cls.method(1, (JType)this.cm.VOID, "set" + accessorName);
/* 562 */     JVar param = m.param(propType, paramName);
/* 563 */     methodDoc = m.javadoc();
/* 564 */     JCommentPart part = methodDoc.addParam(paramName);
/* 565 */     part.add("the value for the " + paramName + " property");
/* 566 */     body = m.body();
/* 567 */     body.assign((JAssignmentTarget)JExpr._this().ref(paramName), (JExpression)param);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\WebServiceWrapperGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
