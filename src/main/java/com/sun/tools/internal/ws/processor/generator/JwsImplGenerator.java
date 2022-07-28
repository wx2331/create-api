/*     */ package com.sun.tools.internal.ws.processor.generator;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCommentPart;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.model.Operation;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.Service;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaMethod;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.Options;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Binding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Definitions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Operation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAP12Binding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBinding;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.BindingType;
/*     */ import javax.xml.ws.Holder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JwsImplGenerator
/*     */   extends GeneratorBase
/*     */ {
/*  66 */   private static final Map<String, String> TRANSLATION_MAP = new HashMap<>(1);
/*     */ 
/*     */   
/*     */   static {
/*  70 */     TRANSLATION_MAP.put("http://schemas.xmlsoap.org/soap/http", "http://schemas.xmlsoap.org/wsdl/soap/http");
/*     */   }
/*     */ 
/*     */   
/*  74 */   private final List<String> implFiles = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> generate(Model model, WsimportOptions options, ErrorReceiver receiver) {
/*  81 */     if (options.implDestDir == null) {
/*  82 */       return null;
/*     */     }
/*  84 */     JwsImplGenerator jwsImplGenerator = new JwsImplGenerator();
/*  85 */     jwsImplGenerator.init(model, options, receiver);
/*  86 */     jwsImplGenerator.doGeneration();
/*     */     
/*  88 */     if (jwsImplGenerator.implFiles.isEmpty()) {
/*  89 */       StringBuilder msg = new StringBuilder();
/*  90 */       if (options.implServiceName != null)
/*  91 */         msg.append("serviceName=[").append(options.implServiceName).append("] "); 
/*  92 */       if (options.implPortName != null) {
/*  93 */         msg.append("portName=[").append(options.implPortName).append("] ");
/*     */       }
/*  95 */       if (msg.length() > 0) {
/*  96 */         msg.append(", Not found in wsdl file.\n");
/*     */       }
/*  98 */       msg.append("No impl files generated!");
/*  99 */       receiver.warning(null, msg.toString());
/*     */     } 
/*     */     
/* 102 */     return jwsImplGenerator.implFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean moveToImplDestDir(List<String> gImplFiles, WsimportOptions options, ErrorReceiver receiver) {
/* 110 */     if (options.implDestDir == null || gImplFiles == null || gImplFiles
/* 111 */       .isEmpty()) {
/* 112 */       return true;
/*     */     }
/* 114 */     List<ImplFile> generatedImplFiles = ImplFile.toImplFiles(gImplFiles);
/*     */     
/*     */     try {
/* 117 */       File implDestDir = makePackageDir(options);
/*     */ 
/*     */ 
/*     */       
/* 121 */       for (ImplFile implF : generatedImplFiles) {
/* 122 */         File movedF = findFile(options, implF.qualifiedName);
/* 123 */         if (movedF == null) {
/*     */           
/* 125 */           receiver.warning(null, "Class " + implF.qualifiedName + " is not generated. Not moving.");
/*     */           
/* 127 */           return false;
/*     */         } 
/*     */         
/* 130 */         File f = new File(implDestDir, implF.name);
/* 131 */         if (!movedF.equals(f)) {
/*     */           
/* 133 */           if (f.exists())
/*     */           {
/* 135 */             if (!f.delete()) {
/* 136 */               receiver.error("Class " + implF.qualifiedName + " has existed in destImplDir, and it can not be written!", null);
/*     */             }
/*     */           }
/*     */ 
/*     */           
/* 141 */           if (!movedF.renameTo(f))
/*     */           {
/* 143 */             throw new Exception();
/*     */           }
/*     */         } 
/*     */       } 
/* 147 */     } catch (Exception e) {
/* 148 */       receiver.error("Moving WebService Impl files failed!", e);
/* 149 */       return false;
/*     */     } 
/* 151 */     return true;
/*     */   }
/*     */   
/*     */   private JwsImplGenerator() {
/* 155 */     this.donotOverride = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Service service) {
/* 160 */     QName serviceName = service.getName();
/*     */     
/* 162 */     if (this.options.implServiceName != null && 
/* 163 */       !equalsNSOptional(this.options.implServiceName, serviceName)) {
/*     */       return;
/*     */     }
/* 166 */     for (Port port : service.getPorts()) {
/* 167 */       if (port.isProvider()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 173 */       QName portName = port.getName();
/*     */ 
/*     */       
/* 176 */       if (this.options.implPortName != null && 
/* 177 */         !equalsNSOptional(this.options.implPortName, portName)) {
/*     */         continue;
/*     */       }
/*     */       
/* 181 */       String simpleClassName = serviceName.getLocalPart() + "_" + portName.getLocalPart() + "Impl";
/* 182 */       String className = makePackageQualified(simpleClassName);
/* 183 */       this.implFiles.add(className);
/*     */       
/* 185 */       if (this.donotOverride && GeneratorUtil.classExists((Options)this.options, className)) {
/* 186 */         log("Class " + className + " exists. Not overriding.");
/*     */         
/*     */         return;
/*     */       } 
/* 190 */       JDefinedClass cls = null;
/*     */       try {
/* 192 */         cls = getClass(className, ClassType.CLASS);
/* 193 */       } catch (JClassAlreadyExistsException e) {
/* 194 */         log("Class " + className + " generates failed. JClassAlreadyExistsException[" + className + "].");
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 201 */       JavaInterface portIntf = port.getJavaInterface();
/* 202 */       String portClassName = Names.customJavaTypeClassName(portIntf);
/* 203 */       JDefinedClass portCls = null;
/*     */       try {
/* 205 */         portCls = getClass(portClassName, ClassType.INTERFACE);
/* 206 */       } catch (JClassAlreadyExistsException e) {
/* 207 */         log("Class " + className + " generates failed. JClassAlreadyExistsException[" + portClassName + "].");
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 212 */       cls._implements((JClass)portCls);
/*     */ 
/*     */       
/* 215 */       cls.constructor(1);
/*     */ 
/*     */       
/* 218 */       JDocComment comment = cls.javadoc();
/*     */       
/* 220 */       if (service.getJavaDoc() != null) {
/* 221 */         comment.add(service.getJavaDoc());
/* 222 */         comment.add("\n\n");
/*     */       } 
/*     */       
/* 225 */       for (String doc : getJAXWSClassComment()) {
/* 226 */         comment.add(doc);
/*     */       }
/*     */ 
/*     */       
/* 230 */       JAnnotationUse webServiceAnn = cls.annotate(this.cm.ref(WebService.class));
/* 231 */       writeWebServiceAnnotation(service, port, webServiceAnn);
/*     */ 
/*     */       
/* 234 */       JAnnotationUse bindingTypeAnn = cls.annotate(this.cm.ref(BindingType.class));
/* 235 */       writeBindingTypeAnnotation(port, bindingTypeAnn);
/*     */ 
/*     */       
/* 238 */       for (GeneratorExtension f : ServiceFinder.<GeneratorExtension>find(GeneratorExtension.class)) {
/* 239 */         f.writeWebServiceAnnotation(this.model, this.cm, cls, port);
/*     */       }
/*     */ 
/*     */       
/* 243 */       for (Operation operation : port.getOperations()) {
/* 244 */         JMethod m; JDocComment methodDoc; JavaMethod method = operation.getJavaMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 249 */         String methodJavaDoc = operation.getJavaDoc();
/* 250 */         if (method.getReturnType().getName().equals("void")) {
/* 251 */           m = cls.method(1, void.class, method.getName());
/* 252 */           methodDoc = m.javadoc();
/*     */         } else {
/* 254 */           JAXBTypeAndAnnotation retType = method.getReturnType().getType();
/* 255 */           m = cls.method(1, retType.getType(), method.getName());
/* 256 */           retType.annotate((JAnnotatable)m);
/* 257 */           methodDoc = m.javadoc();
/* 258 */           JCommentPart ret = methodDoc.addReturn();
/* 259 */           ret.add("returns " + retType.getName());
/*     */         } 
/*     */         
/* 262 */         if (methodJavaDoc != null) {
/* 263 */           methodDoc.add(methodJavaDoc);
/*     */         }
/* 265 */         JClass holder = this.cm.ref(Holder.class);
/* 266 */         for (JavaParameter parameter : method.getParametersList()) {
/*     */           JVar var;
/* 268 */           JAXBTypeAndAnnotation paramType = parameter.getType().getType();
/* 269 */           if (parameter.isHolder()) {
/* 270 */             var = m.param((JType)holder.narrow(paramType.getType().boxify()), parameter
/* 271 */                 .getName());
/*     */           } else {
/* 273 */             var = m.param(paramType.getType(), parameter.getName());
/*     */           } 
/* 275 */           methodDoc.addParam(var);
/*     */         } 
/*     */ 
/*     */         
/* 279 */         Operation wsdlOp = operation.getWSDLPortTypeOperation();
/* 280 */         for (Fault fault : operation.getFaultsSet()) {
/* 281 */           m._throws(fault.getExceptionClass());
/* 282 */           methodDoc.addThrows(fault.getExceptionClass());
/* 283 */           wsdlOp.putFault(fault.getWsdlFaultName(), fault.getExceptionClass());
/*     */         } 
/* 285 */         m.body().block().directStatement("//replace with your impl here");
/* 286 */         m.body().block().directStatement(
/* 287 */             getReturnString(method.getReturnType().getName()));
/*     */       } 
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
/*     */   private String getReturnString(String type) {
/* 300 */     String nullReturnStr = "return null;";
/*     */     
/* 302 */     if (type.indexOf('.') > -1 || type.indexOf('[') > -1) {
/* 303 */       return "return null;";
/*     */     }
/*     */ 
/*     */     
/* 307 */     if (type.equals("void")) {
/* 308 */       return "return;";
/*     */     }
/* 310 */     if (type.equals("boolean")) {
/* 311 */       return "return false;";
/*     */     }
/* 313 */     if (type.equals("int") || type.equals("byte") || type.equals("short") || type
/* 314 */       .equals("long") || type.equals("double") || type.equals("float")) {
/* 315 */       return "return 0;";
/*     */     }
/* 317 */     if (type.equals("char")) {
/* 318 */       return "return '0';";
/*     */     }
/*     */     
/* 321 */     return "return null;";
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
/*     */   private void writeWebServiceAnnotation(Service service, Port port, JAnnotationUse webServiceAnn) {
/* 333 */     webServiceAnn.param("portName", port.getName().getLocalPart());
/* 334 */     webServiceAnn.param("serviceName", service.getName().getLocalPart());
/* 335 */     webServiceAnn.param("targetNamespace", service.getName().getNamespaceURI());
/* 336 */     webServiceAnn.param("wsdlLocation", this.wsdlLocation);
/* 337 */     webServiceAnn.param("endpointInterface", port.getJavaInterface().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   private String transToValidJavaIdentifier(String s) {
/* 342 */     if (s == null) {
/* 343 */       return null;
/*     */     }
/* 345 */     int len = s.length();
/* 346 */     StringBuilder retSB = new StringBuilder();
/* 347 */     if (len == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
/* 348 */       retSB.append("J");
/*     */     } else {
/* 350 */       retSB.append(s.charAt(0));
/*     */     } 
/*     */     
/* 353 */     for (int i = 1; i < len; i++) {
/* 354 */       if (Character.isJavaIdentifierPart(s.charAt(i)))
/*     */       {
/*     */         
/* 357 */         retSB.append(s.charAt(i));
/*     */       }
/*     */     } 
/* 360 */     return retSB.toString();
/*     */   }
/*     */   
/*     */   private String makePackageQualified(String s) {
/* 364 */     s = transToValidJavaIdentifier(s);
/* 365 */     if (this.options.defaultPackage != null && !this.options.defaultPackage.equals("")) {
/* 366 */       return this.options.defaultPackage + "." + s;
/*     */     }
/* 368 */     return s;
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
/*     */   private void writeBindingTypeAnnotation(Port port, JAnnotationUse bindingTypeAnn) {
/* 382 */     QName bName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLBindingName");
/* 383 */     if (bName == null) {
/*     */       return;
/*     */     }
/* 386 */     String v = getBindingType(bName);
/*     */ 
/*     */     
/* 389 */     if (v != null)
/*     */     {
/* 391 */       bindingTypeAnn.param("value", v);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private String resolveBindingValue(TWSDLExtension wsdlext) {
/* 397 */     if (wsdlext.getClass().equals(SOAPBinding.class)) {
/* 398 */       SOAPBinding sb = (SOAPBinding)wsdlext;
/* 399 */       if ("http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true".equals(sb.getTransport())) {
/* 400 */         return "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true";
/*     */       }
/* 402 */       for (GeneratorExtension f : ServiceFinder.<GeneratorExtension>find(GeneratorExtension.class)) {
/* 403 */         String bindingValue = f.getBindingValue(sb.getTransport(), SOAPVersion.SOAP_11);
/* 404 */         if (bindingValue != null) {
/* 405 */           return bindingValue;
/*     */         }
/*     */       } 
/* 408 */       return "http://schemas.xmlsoap.org/wsdl/soap/http";
/*     */     } 
/*     */     
/* 411 */     if (wsdlext.getClass().equals(SOAP12Binding.class)) {
/* 412 */       SOAP12Binding sb = (SOAP12Binding)wsdlext;
/* 413 */       if ("http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true".equals(sb.getTransport())) {
/* 414 */         return "http://www.w3.org/2003/05/soap/bindings/HTTP/?mtom=true";
/*     */       }
/* 416 */       for (GeneratorExtension f : ServiceFinder.<GeneratorExtension>find(GeneratorExtension.class)) {
/* 417 */         String bindingValue = f.getBindingValue(sb.getTransport(), SOAPVersion.SOAP_12);
/* 418 */         if (bindingValue != null) {
/* 419 */           return bindingValue;
/*     */         }
/*     */       } 
/* 422 */       return "http://www.w3.org/2003/05/soap/bindings/HTTP/";
/*     */     } 
/*     */     
/* 425 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getBindingType(QName bName) {
/* 430 */     String value = null;
/*     */     
/* 432 */     if (this.model.getEntity() instanceof Definitions) {
/* 433 */       Definitions definitions = (Definitions)this.model.getEntity();
/* 434 */       if (definitions != null) {
/* 435 */         Iterator<Binding> bindings = definitions.bindings();
/* 436 */         if (bindings != null) {
/* 437 */           while (bindings.hasNext()) {
/* 438 */             Binding binding = bindings.next();
/* 439 */             if (bName.getLocalPart().equals(binding.getName()) && bName
/* 440 */               .getNamespaceURI().equals(binding.getNamespaceURI())) {
/*     */               
/* 442 */               List<TWSDLExtension> bindextends = (List<TWSDLExtension>)binding.extensions();
/* 443 */               for (TWSDLExtension wsdlext : bindextends) {
/* 444 */                 value = resolveBindingValue(wsdlext);
/* 445 */                 if (value != null) {
/*     */                   break;
/*     */                 }
/*     */               } 
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 456 */     if (value == null && 
/* 457 */       this.model.getEntity() instanceof Definitions) {
/* 458 */       Definitions definitions = (Definitions)this.model.getEntity();
/* 459 */       Binding b = (Binding)definitions.resolveBindings().get(bName);
/* 460 */       if (b != null) {
/*     */         
/* 462 */         List<TWSDLExtension> bindextends = (List<TWSDLExtension>)b.extensions();
/* 463 */         for (TWSDLExtension wsdlext : bindextends) {
/* 464 */           value = resolveBindingValue(wsdlext);
/* 465 */           if (value != null) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 472 */     return value;
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
/*     */   static final class ImplFile
/*     */   {
/*     */     public String qualifiedName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ImplFile(String qualifiedClassName) {
/* 504 */       this.qualifiedName = qualifiedClassName + ".java";
/*     */       
/* 506 */       String simpleClassName = qualifiedClassName;
/* 507 */       int i = qualifiedClassName.lastIndexOf(".");
/* 508 */       if (i != -1) {
/* 509 */         simpleClassName = qualifiedClassName.substring(i + 1);
/*     */       }
/* 511 */       this.name = simpleClassName + ".java";
/*     */     }
/*     */     
/*     */     public static List<ImplFile> toImplFiles(List<String> qualifiedClassNames) {
/* 515 */       List<ImplFile> ret = new ArrayList<>();
/*     */       
/* 517 */       for (String qualifiedClassName : qualifiedClassNames) {
/* 518 */         ret.add(new ImplFile(qualifiedClassName));
/*     */       }
/* 520 */       return ret;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File makePackageDir(WsimportOptions options) {
/* 529 */     File ret = null;
/* 530 */     if (options.defaultPackage != null && !options.defaultPackage.equals("")) {
/* 531 */       String subDir = options.defaultPackage.replace('.', '/');
/* 532 */       ret = new File(options.implDestDir, subDir);
/*     */     } else {
/* 534 */       ret = options.implDestDir;
/*     */     } 
/*     */     
/* 537 */     boolean created = ret.mkdirs();
/* 538 */     if (options.verbose && !created) {
/* 539 */       System.out.println(MessageFormat.format("Directory not created: {0}", new Object[] { ret }));
/*     */     }
/* 541 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getQualifiedFileName(String canonicalBaseDir, File f) throws IOException {
/* 546 */     String fp = f.getCanonicalPath();
/* 547 */     if (fp == null)
/* 548 */       return null; 
/* 549 */     fp = fp.replace(canonicalBaseDir, "");
/* 550 */     fp = fp.replace('\\', '.');
/* 551 */     fp = fp.replace('/', '.');
/* 552 */     if (fp.startsWith(".")) {
/* 553 */       fp = fp.substring(1);
/*     */     }
/* 555 */     return fp;
/*     */   }
/*     */ 
/*     */   
/*     */   private static File findFile(WsimportOptions options, String qualifiedFileName) throws IOException {
/* 560 */     String baseDir = options.sourceDir.getCanonicalPath();
/* 561 */     String fp = null;
/* 562 */     for (File f : options.getGeneratedFiles()) {
/* 563 */       fp = getQualifiedFileName(baseDir, f);
/* 564 */       if (qualifiedFileName.equals(fp)) {
/* 565 */         return f;
/*     */       }
/*     */     } 
/* 568 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean equalsNSOptional(String strQName, QName checkQN) {
/* 572 */     if (strQName == null)
/* 573 */       return false; 
/* 574 */     strQName = strQName.trim();
/* 575 */     QName reqQN = QName.valueOf(strQName);
/*     */     
/* 577 */     if (reqQN.getNamespaceURI() == null || reqQN.getNamespaceURI().equals("")) {
/* 578 */       return reqQN.getLocalPart().equals(checkQN.getLocalPart());
/*     */     }
/* 580 */     return reqQN.equals(checkQN);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\JwsImplGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */