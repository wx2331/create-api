/*     */ package com.sun.tools.internal.ws.processor.generator;
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAnnotationArrayMember;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCommentPart;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.ws.api.TJavaGeneratorExtension;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;
/*     */ import com.sun.tools.internal.ws.processor.model.AsyncOperation;
/*     */ import com.sun.tools.internal.ws.processor.model.Block;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Message;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.model.Operation;
/*     */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.Request;
/*     */ import com.sun.tools.internal.ws.processor.model.Response;
/*     */ import com.sun.tools.internal.ws.processor.model.Service;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaMethod;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*     */ import com.sun.tools.internal.ws.resources.GeneratorMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.Options;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Operation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.jws.Oneway;
/*     */ import javax.jws.WebMethod;
/*     */ import javax.jws.WebParam;
/*     */ import javax.jws.WebResult;
/*     */ import javax.jws.WebService;
/*     */ import javax.jws.soap.SOAPBinding;
/*     */ import javax.xml.bind.annotation.XmlSeeAlso;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Holder;
/*     */ import javax.xml.ws.RequestWrapper;
/*     */ import javax.xml.ws.ResponseWrapper;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public class SeiGenerator extends GeneratorBase {
/*     */   private TJavaGeneratorExtension extension;
/*     */   private List<TJavaGeneratorExtension> extensionHandlers;
/*     */   
/*     */   public static void generate(Model model, WsimportOptions options, ErrorReceiver receiver, TJavaGeneratorExtension... extensions) {
/*  60 */     SeiGenerator seiGenerator = new SeiGenerator();
/*  61 */     seiGenerator.init(model, options, receiver, extensions);
/*  62 */     seiGenerator.doGeneration();
/*     */   }
/*     */   
/*     */   public void init(Model model, WsimportOptions options, ErrorReceiver receiver, TJavaGeneratorExtension... extensions) {
/*  66 */     init(model, options, receiver);
/*  67 */     this.extensionHandlers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (options.target.isLaterThan(Options.Target.V2_2)) {
/*  73 */       register(new W3CAddressingJavaGeneratorExtension());
/*     */     }
/*     */     
/*  76 */     for (TJavaGeneratorExtension j : extensions) {
/*  77 */       register(j);
/*     */     }
/*     */     
/*  80 */     this.extension = new JavaGeneratorExtensionFacade(this.extensionHandlers.<TJavaGeneratorExtension>toArray(new TJavaGeneratorExtension[this.extensionHandlers.size()]));
/*     */   }
/*     */   private void write(Port port) {
/*     */     JDefinedClass cls;
/*  84 */     JavaInterface intf = port.getJavaInterface();
/*  85 */     String className = Names.customJavaTypeClassName(intf);
/*     */     
/*  87 */     if (this.donotOverride && GeneratorUtil.classExists((Options)this.options, className)) {
/*  88 */       log("Class " + className + " exists. Not overriding.");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  95 */       cls = getClass(className, ClassType.INTERFACE);
/*  96 */     } catch (JClassAlreadyExistsException e) {
/*     */       
/*  98 */       QName portTypeName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/*     */       
/* 100 */       Locator loc = null;
/* 101 */       if (portTypeName != null) {
/* 102 */         PortType pt = (PortType)port.portTypes.get(portTypeName);
/* 103 */         if (pt != null) {
/* 104 */           loc = pt.getLocator();
/*     */         }
/*     */       } 
/* 107 */       this.receiver.error(loc, GeneratorMessages.GENERATOR_SEI_CLASS_ALREADY_EXIST(intf.getName(), portTypeName));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 112 */     if (!cls.methods().isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 117 */     JDocComment comment = cls.javadoc();
/*     */     
/* 119 */     String ptDoc = intf.getJavaDoc();
/* 120 */     if (ptDoc != null) {
/* 121 */       comment.add(ptDoc);
/* 122 */       comment.add("\n\n");
/*     */     } 
/*     */     
/* 125 */     for (String doc : getJAXWSClassComment()) {
/* 126 */       comment.add(doc);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 131 */     JAnnotationUse webServiceAnn = cls.annotate(this.cm.ref(WebService.class));
/* 132 */     writeWebServiceAnnotation(port, webServiceAnn);
/*     */ 
/*     */     
/* 135 */     writeHandlerConfig(Names.customJavaTypeClassName(port.getJavaInterface()), cls, this.options);
/*     */ 
/*     */     
/* 138 */     writeSOAPBinding(port, cls);
/*     */ 
/*     */     
/* 141 */     if (this.options.target.isLaterThan(Options.Target.V2_1)) {
/* 142 */       writeXmlSeeAlso(cls);
/*     */     }
/*     */     
/* 145 */     for (Operation operation : port.getOperations()) {
/* 146 */       JMethod m; JDocComment methodDoc; JavaMethod method = operation.getJavaMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       String methodJavaDoc = operation.getJavaDoc();
/* 152 */       if (method.getReturnType().getName().equals("void")) {
/* 153 */         m = cls.method(1, void.class, method.getName());
/* 154 */         methodDoc = m.javadoc();
/*     */       } else {
/* 156 */         JAXBTypeAndAnnotation retType = method.getReturnType().getType();
/* 157 */         m = cls.method(1, retType.getType(), method.getName());
/* 158 */         retType.annotate((JAnnotatable)m);
/* 159 */         methodDoc = m.javadoc();
/* 160 */         JCommentPart ret = methodDoc.addReturn();
/* 161 */         ret.add("returns " + retType.getName());
/*     */       } 
/* 163 */       if (methodJavaDoc != null) {
/* 164 */         methodDoc.add(methodJavaDoc);
/*     */       }
/*     */       
/* 167 */       writeWebMethod(operation, m);
/* 168 */       JClass holder = this.cm.ref(Holder.class);
/* 169 */       for (JavaParameter parameter : method.getParametersList()) {
/*     */         JVar var;
/* 171 */         JAXBTypeAndAnnotation paramType = parameter.getType().getType();
/* 172 */         if (parameter.isHolder()) {
/* 173 */           var = m.param((JType)holder.narrow(paramType.getType().boxify()), parameter.getName());
/*     */         } else {
/* 175 */           var = m.param(paramType.getType(), parameter.getName());
/*     */         } 
/*     */ 
/*     */         
/* 179 */         paramType.annotate((JAnnotatable)var);
/* 180 */         methodDoc.addParam(var);
/* 181 */         JAnnotationUse paramAnn = var.annotate(this.cm.ref(WebParam.class));
/* 182 */         writeWebParam(operation, parameter, paramAnn);
/*     */       } 
/* 184 */       Operation wsdlOp = operation.getWSDLPortTypeOperation();
/* 185 */       for (Fault fault : operation.getFaultsSet()) {
/* 186 */         m._throws(fault.getExceptionClass());
/* 187 */         methodDoc.addThrows(fault.getExceptionClass());
/* 188 */         wsdlOp.putFault(fault.getWsdlFaultName(), fault.getExceptionClass());
/*     */       } 
/*     */ 
/*     */       
/* 192 */       this.extension.writeMethodAnnotations((TWSDLOperation)wsdlOp, m);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeXmlSeeAlso(JDefinedClass cls) {
/* 197 */     if (this.model.getJAXBModel().getS2JJAXBModel() != null) {
/* 198 */       List<JClass> objectFactories = this.model.getJAXBModel().getS2JJAXBModel().getAllObjectFactories();
/*     */ 
/*     */       
/* 201 */       if (objectFactories.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 205 */       JAnnotationUse xmlSeeAlso = cls.annotate(this.cm.ref(XmlSeeAlso.class));
/* 206 */       JAnnotationArrayMember paramArray = xmlSeeAlso.paramArray("value");
/* 207 */       for (JClass of : objectFactories) {
/* 208 */         paramArray = paramArray.param((JType)of);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeWebMethod(Operation operation, JMethod m) {
/* 215 */     Response response = operation.getResponse();
/* 216 */     JAnnotationUse webMethodAnn = m.annotate(this.cm.ref(WebMethod.class));
/*     */ 
/*     */     
/* 219 */     String operationName = (operation instanceof AsyncOperation) ? ((AsyncOperation)operation).getNormalOperation().getName().getLocalPart() : operation.getName().getLocalPart();
/*     */     
/* 221 */     if (!m.name().equals(operationName)) {
/* 222 */       webMethodAnn.param("operationName", operationName);
/*     */     }
/*     */     
/* 225 */     if (operation.getSOAPAction() != null && operation.getSOAPAction().length() > 0) {
/* 226 */       webMethodAnn.param("action", operation.getSOAPAction());
/*     */     }
/*     */     
/* 229 */     if (operation.getResponse() == null) {
/* 230 */       m.annotate(Oneway.class);
/* 231 */     } else if (!operation.getJavaMethod().getReturnType().getName().equals("void") && operation
/* 232 */       .getResponse().getParametersList().size() > 0) {
/*     */       
/* 234 */       String resultName = null;
/* 235 */       String nsURI = null;
/* 236 */       if (operation.getResponse().getBodyBlocks().hasNext()) {
/* 237 */         Block block = operation.getResponse().getBodyBlocks().next();
/* 238 */         resultName = block.getName().getLocalPart();
/* 239 */         if (this.isDocStyle || block.getLocation() == 2) {
/* 240 */           nsURI = block.getName().getNamespaceURI();
/*     */         }
/*     */       } 
/*     */       
/* 244 */       for (Parameter parameter : operation.getResponse().getParametersList()) {
/* 245 */         if (parameter.getParameterIndex() == -1) {
/* 246 */           if (operation.isWrapped() || !this.isDocStyle) {
/* 247 */             if (parameter.getBlock().getLocation() == 2) {
/* 248 */               resultName = parameter.getBlock().getName().getLocalPart();
/*     */             } else {
/* 250 */               resultName = parameter.getName();
/*     */             } 
/* 252 */             if (this.isDocStyle || parameter.getBlock().getLocation() == 2) {
/* 253 */               nsURI = parameter.getType().getName().getNamespaceURI();
/*     */             }
/* 255 */           } else if (this.isDocStyle) {
/* 256 */             JAXBType t = (JAXBType)parameter.getType();
/* 257 */             resultName = t.getName().getLocalPart();
/* 258 */             nsURI = t.getName().getNamespaceURI();
/*     */           } 
/* 260 */           if (!(operation instanceof AsyncOperation)) {
/* 261 */             JAnnotationUse wr = null;
/*     */             
/* 263 */             if (!resultName.equals("return")) {
/* 264 */               wr = m.annotate(WebResult.class);
/* 265 */               wr.param("name", resultName);
/*     */             } 
/* 267 */             if (nsURI != null || (this.isDocStyle && operation.isWrapped())) {
/* 268 */               if (wr == null) {
/* 269 */                 wr = m.annotate(WebResult.class);
/*     */               }
/* 271 */               wr.param("targetNamespace", nsURI);
/*     */             } 
/*     */             
/* 274 */             if (!this.isDocStyle || !operation.isWrapped() || parameter
/* 275 */               .getBlock().getLocation() == 2) {
/* 276 */               if (wr == null) {
/* 277 */                 wr = m.annotate(WebResult.class);
/*     */               }
/* 279 */               wr.param("partName", parameter.getName());
/*     */             } 
/* 281 */             if (parameter.getBlock().getLocation() == 2) {
/* 282 */               if (wr == null) {
/* 283 */                 wr = m.annotate(WebResult.class);
/*     */               }
/* 285 */               wr.param("header", true);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 294 */     if (!this.sameParamStyle && 
/* 295 */       !operation.isWrapped()) {
/* 296 */       JAnnotationUse sb = m.annotate(SOAPBinding.class);
/* 297 */       sb.param("parameterStyle", SOAPBinding.ParameterStyle.BARE);
/*     */     } 
/*     */ 
/*     */     
/* 301 */     if (operation.isWrapped() && operation.getStyle().equals(SOAPStyle.DOCUMENT)) {
/* 302 */       Block reqBlock = operation.getRequest().getBodyBlocks().next();
/* 303 */       JAnnotationUse reqW = m.annotate(RequestWrapper.class);
/* 304 */       reqW.param("localName", reqBlock.getName().getLocalPart());
/* 305 */       reqW.param("targetNamespace", reqBlock.getName().getNamespaceURI());
/* 306 */       reqW.param("className", reqBlock.getType().getJavaType().getName());
/*     */       
/* 308 */       if (response != null) {
/* 309 */         JAnnotationUse resW = m.annotate(ResponseWrapper.class);
/* 310 */         Block resBlock = response.getBodyBlocks().next();
/* 311 */         resW.param("localName", resBlock.getName().getLocalPart());
/* 312 */         resW.param("targetNamespace", resBlock.getName().getNamespaceURI());
/* 313 */         resW.param("className", resBlock.getType().getJavaType().getName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMessageParam(Parameter param, Message message) {
/* 319 */     Block block = param.getBlock();
/*     */     
/* 321 */     return ((message.getBodyBlockCount() > 0 && block.equals(message.getBodyBlocks().next())) || (message
/* 322 */       .getHeaderBlockCount() > 0 && block
/* 323 */       .equals(message.getHeaderBlocks().next())));
/*     */   }
/*     */   
/*     */   private boolean isHeaderParam(Parameter param, Message message) {
/* 327 */     if (message.getHeaderBlockCount() == 0) {
/* 328 */       return false;
/*     */     }
/*     */     
/* 331 */     for (Block headerBlock : message.getHeaderBlocksMap().values()) {
/* 332 */       if (param.getBlock().equals(headerBlock)) {
/* 333 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 337 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isAttachmentParam(Parameter param, Message message) {
/* 341 */     if (message.getAttachmentBlockCount() == 0) {
/* 342 */       return false;
/*     */     }
/*     */     
/* 345 */     for (Block attBlock : message.getAttachmentBlocksMap().values()) {
/* 346 */       if (param.getBlock().equals(attBlock)) {
/* 347 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 351 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isUnboundParam(Parameter param, Message message) {
/* 355 */     if (message.getUnboundBlocksCount() == 0) {
/* 356 */       return false;
/*     */     }
/*     */     
/* 359 */     for (Block unboundBlock : message.getUnboundBlocksMap().values()) {
/* 360 */       if (param.getBlock().equals(unboundBlock)) {
/* 361 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 365 */     return false;
/*     */   }
/*     */   private void writeWebParam(Operation operation, JavaParameter javaParameter, JAnnotationUse paramAnno) {
/*     */     String name;
/* 369 */     Parameter param = javaParameter.getParameter();
/* 370 */     Request req = operation.getRequest();
/* 371 */     Response res = operation.getResponse();
/*     */ 
/*     */     
/* 374 */     boolean header = (isHeaderParam(param, (Message)req) || (res != null && isHeaderParam(param, (Message)res)));
/*     */ 
/*     */     
/* 377 */     boolean isWrapped = operation.isWrapped();
/*     */     
/* 379 */     if (param.getBlock().getLocation() == 2 || (this.isDocStyle && !isWrapped)) {
/* 380 */       name = param.getBlock().getName().getLocalPart();
/*     */     } else {
/* 382 */       name = param.getName();
/*     */     } 
/*     */     
/* 385 */     paramAnno.param("name", name);
/*     */     
/* 387 */     String ns = null;
/*     */     
/* 389 */     if (this.isDocStyle) {
/* 390 */       ns = param.getBlock().getName().getNamespaceURI();
/* 391 */       if (isWrapped) {
/* 392 */         ns = param.getType().getName().getNamespaceURI();
/*     */       }
/* 394 */     } else if (header) {
/* 395 */       ns = param.getBlock().getName().getNamespaceURI();
/*     */     } 
/*     */     
/* 398 */     if (ns != null || (this.isDocStyle && isWrapped)) {
/* 399 */       paramAnno.param("targetNamespace", ns);
/*     */     }
/*     */     
/* 402 */     if (header) {
/* 403 */       paramAnno.param("header", true);
/*     */     }
/*     */     
/* 406 */     if (param.isINOUT()) {
/* 407 */       paramAnno.param("mode", WebParam.Mode.INOUT);
/* 408 */     } else if (res != null && (isMessageParam(param, (Message)res) || isHeaderParam(param, (Message)res) || isAttachmentParam(param, (Message)res) || 
/* 409 */       isUnboundParam(param, (Message)res) || param.isOUT())) {
/* 410 */       paramAnno.param("mode", WebParam.Mode.OUT);
/*     */     } 
/*     */ 
/*     */     
/* 414 */     if (!this.isDocStyle || !isWrapped || header)
/* 415 */       paramAnno.param("partName", javaParameter.getParameter().getName()); 
/*     */   }
/*     */   
/*     */   private boolean isDocStyle = true;
/*     */   private boolean sameParamStyle = true;
/*     */   
/*     */   private void writeSOAPBinding(Port port, JDefinedClass cls) {
/* 422 */     JAnnotationUse soapBindingAnn = null;
/* 423 */     this.isDocStyle = (port.getStyle() == null || port.getStyle().equals(SOAPStyle.DOCUMENT));
/* 424 */     if (!this.isDocStyle) {
/* 425 */       soapBindingAnn = cls.annotate(SOAPBinding.class);
/* 426 */       soapBindingAnn.param("style", SOAPBinding.Style.RPC);
/* 427 */       port.setWrapped(true);
/*     */     } 
/* 429 */     if (this.isDocStyle) {
/* 430 */       boolean first = true;
/* 431 */       boolean isWrapper = true;
/* 432 */       for (Operation operation : port.getOperations()) {
/* 433 */         if (first) {
/* 434 */           isWrapper = operation.isWrapped();
/* 435 */           first = false;
/*     */           continue;
/*     */         } 
/* 438 */         this.sameParamStyle = (isWrapper == operation.isWrapped());
/* 439 */         if (!this.sameParamStyle) {
/*     */           break;
/*     */         }
/*     */       } 
/* 443 */       if (this.sameParamStyle) {
/* 444 */         port.setWrapped(isWrapper);
/*     */       }
/*     */     } 
/* 447 */     if (this.sameParamStyle && !port.isWrapped()) {
/* 448 */       if (soapBindingAnn == null) {
/* 449 */         soapBindingAnn = cls.annotate(SOAPBinding.class);
/*     */       }
/* 451 */       soapBindingAnn.param("parameterStyle", SOAPBinding.ParameterStyle.BARE);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeWebServiceAnnotation(Port port, JAnnotationUse wsa) {
/* 456 */     QName name = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/* 457 */     wsa.param("name", name.getLocalPart());
/* 458 */     wsa.param("targetNamespace", name.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Model model) throws Exception {
/* 463 */     for (Service s : model.getServices()) {
/* 464 */       s.accept(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Service service) throws Exception {
/* 470 */     String jd = this.model.getJavaDoc();
/* 471 */     if (jd != null) {
/* 472 */       JPackage pkg = this.cm._package(this.options.defaultPackage);
/* 473 */       pkg.javadoc().add(jd);
/*     */     } 
/*     */     
/* 476 */     for (Port p : service.getPorts()) {
/* 477 */       visitPort(service, p);
/*     */     }
/*     */   }
/*     */   
/*     */   private void visitPort(Service service, Port port) {
/* 482 */     if (port.isProvider()) {
/*     */       return;
/*     */     }
/* 485 */     write(port);
/*     */   }
/*     */   
/*     */   private void register(TJavaGeneratorExtension h) {
/* 489 */     this.extensionHandlers.add(h);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\SeiGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */