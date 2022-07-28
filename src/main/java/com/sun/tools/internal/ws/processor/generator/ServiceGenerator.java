/*     */ package com.sun.tools.internal.ws.processor.generator;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JCatchBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCommentPart;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JTryBlock;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.Service;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.resources.GeneratorMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.Options;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*     */ import com.sun.xml.internal.ws.spi.db.BindingHelper;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebEndpoint;
/*     */ import javax.xml.ws.WebServiceClient;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class ServiceGenerator
/*     */   extends GeneratorBase
/*     */ {
/*     */   public static void generate(Model model, WsimportOptions options, ErrorReceiver receiver) {
/*  78 */     ServiceGenerator serviceGenerator = new ServiceGenerator(model, options, receiver);
/*  79 */     serviceGenerator.doGeneration();
/*     */   }
/*     */   
/*     */   private ServiceGenerator(Model model, WsimportOptions options, ErrorReceiver receiver) {
/*  83 */     init(model, options, receiver);
/*     */   }
/*     */   
/*     */   public void visit(Service service) {
/*     */     JDefinedClass cls;
/*  88 */     JavaInterface intf = service.getJavaInterface();
/*  89 */     String className = Names.customJavaTypeClassName(intf);
/*  90 */     if (this.donotOverride && GeneratorUtil.classExists((Options)this.options, className)) {
/*  91 */       log("Class " + className + " exists. Not overriding.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  97 */       cls = getClass(className, ClassType.CLASS);
/*  98 */     } catch (JClassAlreadyExistsException e) {
/*  99 */       this.receiver.error(service.getLocator(), GeneratorMessages.GENERATOR_SERVICE_CLASS_ALREADY_EXIST(className, service.getName()));
/*     */       
/*     */       return;
/*     */     } 
/* 103 */     cls._extends(Service.class);
/* 104 */     String serviceFieldName = BindingHelper.mangleNameToClassName(service.getName().getLocalPart()).toUpperCase(Locale.ENGLISH);
/* 105 */     String wsdlLocationName = serviceFieldName + "_WSDL_LOCATION";
/* 106 */     JFieldVar urlField = cls.field(28, URL.class, wsdlLocationName);
/*     */     
/* 108 */     JFieldVar exField = cls.field(28, WebServiceException.class, serviceFieldName + "_EXCEPTION");
/*     */ 
/*     */     
/* 111 */     String serviceName = serviceFieldName + "_QNAME";
/* 112 */     cls.field(28, QName.class, serviceName, 
/* 113 */         (JExpression)JExpr._new(this.cm.ref(QName.class)).arg(service.getName().getNamespaceURI()).arg(service.getName().getLocalPart()));
/*     */     
/* 115 */     JClass qNameCls = this.cm.ref(QName.class);
/*     */     
/* 117 */     JInvocation inv = JExpr._new(qNameCls);
/* 118 */     inv.arg("namespace");
/* 119 */     inv.arg("localpart");
/*     */     
/* 121 */     if (this.options.useBaseResourceAndURLToLoadWSDL) {
/* 122 */       writeClassLoaderBaseResourceWSDLLocation(className, cls, urlField, exField);
/* 123 */     } else if (this.wsdlLocation.startsWith("http://") || this.wsdlLocation.startsWith("https://") || this.wsdlLocation.startsWith("file:/")) {
/* 124 */       writeAbsWSDLLocation(cls, urlField, exField);
/* 125 */     } else if (this.wsdlLocation.startsWith("META-INF/")) {
/* 126 */       writeClassLoaderResourceWSDLLocation(className, cls, urlField, exField);
/*     */     } else {
/* 128 */       writeResourceWSDLLocation(className, cls, urlField, exField);
/*     */     } 
/*     */ 
/*     */     
/* 132 */     JDocComment comment = cls.javadoc();
/*     */     
/* 134 */     if (service.getJavaDoc() != null) {
/* 135 */       comment.add(service.getJavaDoc());
/* 136 */       comment.add("\n\n");
/*     */     } 
/*     */     
/* 139 */     for (String doc : getJAXWSClassComment()) {
/* 140 */       comment.add(doc);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 145 */     JMethod constructor1 = cls.constructor(1);
/* 146 */     String constructor1Str = String.format("super(__getWsdlLocation(), %s);", new Object[] { serviceName });
/* 147 */     constructor1.body().directStatement(constructor1Str);
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (this.options.target.isLaterThan(Options.Target.V2_2)) {
/* 152 */       JMethod constructor2 = cls.constructor(1);
/* 153 */       constructor2.varParam(WebServiceFeature.class, "features");
/* 154 */       String constructor2Str = String.format("super(__getWsdlLocation(), %s, features);", new Object[] { serviceName });
/* 155 */       constructor2.body().directStatement(constructor2Str);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 160 */     if (this.options.target.isLaterThan(Options.Target.V2_2)) {
/* 161 */       JMethod constructor3 = cls.constructor(1);
/* 162 */       constructor3.param(URL.class, "wsdlLocation");
/* 163 */       String constructor3Str = String.format("super(wsdlLocation, %s);", new Object[] { serviceName });
/* 164 */       constructor3.body().directStatement(constructor3Str);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 169 */     if (this.options.target.isLaterThan(Options.Target.V2_2)) {
/* 170 */       JMethod constructor4 = cls.constructor(1);
/* 171 */       constructor4.param(URL.class, "wsdlLocation");
/* 172 */       constructor4.varParam(WebServiceFeature.class, "features");
/* 173 */       String constructor4Str = String.format("super(wsdlLocation, %s, features);", new Object[] { serviceName });
/* 174 */       constructor4.body().directStatement(constructor4Str);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 179 */     JMethod constructor5 = cls.constructor(1);
/* 180 */     constructor5.param(URL.class, "wsdlLocation");
/* 181 */     constructor5.param(QName.class, "serviceName");
/* 182 */     constructor5.body().directStatement("super(wsdlLocation, serviceName);");
/*     */ 
/*     */ 
/*     */     
/* 186 */     if (this.options.target.isLaterThan(Options.Target.V2_2)) {
/* 187 */       JMethod constructor6 = cls.constructor(1);
/* 188 */       constructor6.param(URL.class, "wsdlLocation");
/* 189 */       constructor6.param(QName.class, "serviceName");
/* 190 */       constructor6.varParam(WebServiceFeature.class, "features");
/* 191 */       constructor6.body().directStatement("super(wsdlLocation, serviceName, features);");
/*     */     } 
/*     */ 
/*     */     
/* 195 */     JAnnotationUse webServiceClientAnn = cls.annotate(this.cm.ref(WebServiceClient.class));
/* 196 */     writeWebServiceClientAnnotation(service, webServiceClientAnn);
/*     */ 
/*     */     
/* 199 */     for (GeneratorExtension f : ServiceFinder.<GeneratorExtension>find(GeneratorExtension.class)) {
/* 200 */       f.writeWebServiceClientAnnotation(this.options, this.cm, cls);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 205 */     writeHandlerConfig(Names.customJavaTypeClassName(service.getJavaInterface()), cls, this.options);
/*     */     
/* 207 */     for (Port port : service.getPorts()) {
/* 208 */       JDefinedClass jDefinedClass; if (port.isProvider()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 215 */         jDefinedClass = getClass(port.getJavaInterface().getName(), ClassType.INTERFACE);
/* 216 */       } catch (JClassAlreadyExistsException e) {
/*     */         
/* 218 */         QName portTypeName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/*     */         
/* 220 */         Locator loc = null;
/* 221 */         if (portTypeName != null) {
/* 222 */           PortType pt = (PortType)port.portTypes.get(portTypeName);
/* 223 */           if (pt != null) {
/* 224 */             loc = pt.getLocator();
/*     */           }
/*     */         } 
/* 227 */         this.receiver.error(loc, GeneratorMessages.GENERATOR_SEI_CLASS_ALREADY_EXIST(port.getJavaInterface().getName(), portTypeName));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 232 */       writeDefaultGetPort(port, (JType)jDefinedClass, cls);
/*     */ 
/*     */       
/* 235 */       if (this.options.target.isLaterThan(Options.Target.V2_1)) {
/* 236 */         writeGetPort(port, (JType)jDefinedClass, cls);
/*     */       }
/*     */     } 
/*     */     
/* 240 */     writeGetWsdlLocation((JType)this.cm.ref(URL.class), cls, urlField, exField);
/*     */   }
/*     */   
/*     */   private void writeGetPort(Port port, JType retType, JDefinedClass cls) {
/* 244 */     JMethod m = cls.method(1, retType, port.getPortGetter());
/* 245 */     JDocComment methodDoc = m.javadoc();
/* 246 */     if (port.getJavaDoc() != null) {
/* 247 */       methodDoc.add(port.getJavaDoc());
/*     */     }
/* 249 */     JCommentPart ret = methodDoc.addReturn();
/* 250 */     JCommentPart paramDoc = methodDoc.addParam("features");
/* 251 */     paramDoc.append("A list of ");
/* 252 */     paramDoc.append("{@link " + WebServiceFeature.class.getName() + "}");
/* 253 */     paramDoc.append("to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.");
/* 254 */     ret.add("returns " + retType.name());
/* 255 */     m.varParam(WebServiceFeature.class, "features");
/* 256 */     JBlock body = m.body();
/* 257 */     StringBuilder statement = new StringBuilder("return ");
/* 258 */     statement.append("super.getPort(new QName(\"").append(port.getName().getNamespaceURI()).append("\", \"").append(port.getName().getLocalPart()).append("\"), ");
/* 259 */     statement.append(retType.name());
/* 260 */     statement.append(".class, features);");
/* 261 */     body.directStatement(statement.toString());
/* 262 */     writeWebEndpoint(port, m);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeAbsWSDLLocation(JDefinedClass cls, JFieldVar urlField, JFieldVar exField) {
/* 283 */     JBlock staticBlock = cls.init();
/* 284 */     JVar urlVar = staticBlock.decl((JType)this.cm.ref(URL.class), "url", JExpr._null());
/* 285 */     JVar exVar = staticBlock.decl((JType)this.cm.ref(WebServiceException.class), "e", JExpr._null());
/*     */     
/* 287 */     JTryBlock tryBlock = staticBlock._try();
/* 288 */     tryBlock.body().assign((JAssignmentTarget)urlVar, (JExpression)JExpr._new(this.cm.ref(URL.class)).arg(this.wsdlLocation));
/* 289 */     JCatchBlock catchBlock = tryBlock._catch(this.cm.ref(MalformedURLException.class));
/* 290 */     catchBlock.param("ex");
/* 291 */     catchBlock.body().assign((JAssignmentTarget)exVar, (JExpression)JExpr._new(this.cm.ref(WebServiceException.class)).arg((JExpression)JExpr.ref("ex")));
/*     */     
/* 293 */     staticBlock.assign((JAssignmentTarget)urlField, (JExpression)urlVar);
/* 294 */     staticBlock.assign((JAssignmentTarget)exField, (JExpression)exVar);
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
/*     */   
/*     */   private void writeResourceWSDLLocation(String className, JDefinedClass cls, JFieldVar urlField, JFieldVar exField) {
/* 311 */     JBlock staticBlock = cls.init();
/* 312 */     staticBlock.assign((JAssignmentTarget)urlField, (JExpression)JExpr.dotclass(this.cm.ref(className)).invoke("getResource").arg(this.wsdlLocation));
/* 313 */     JVar exVar = staticBlock.decl((JType)this.cm.ref(WebServiceException.class), "e", JExpr._null());
/* 314 */     JConditional ifBlock = staticBlock._if(urlField.eq(JExpr._null()));
/* 315 */     ifBlock._then().assign((JAssignmentTarget)exVar, (JExpression)JExpr._new(this.cm.ref(WebServiceException.class)).arg("Cannot find " + 
/* 316 */           JExpr.quotify('\'', this.wsdlLocation) + " wsdl. Place the resource correctly in the classpath."));
/* 317 */     staticBlock.assign((JAssignmentTarget)exField, (JExpression)exVar);
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
/*     */   
/*     */   private void writeClassLoaderResourceWSDLLocation(String className, JDefinedClass cls, JFieldVar urlField, JFieldVar exField) {
/* 334 */     JBlock staticBlock = cls.init();
/* 335 */     staticBlock.assign((JAssignmentTarget)urlField, (JExpression)JExpr.dotclass(this.cm.ref(className)).invoke("getClassLoader").invoke("getResource").arg(this.wsdlLocation));
/* 336 */     JVar exVar = staticBlock.decl((JType)this.cm.ref(WebServiceException.class), "e", JExpr._null());
/* 337 */     JConditional ifBlock = staticBlock._if(urlField.eq(JExpr._null()));
/* 338 */     ifBlock._then().assign((JAssignmentTarget)exVar, (JExpression)JExpr._new(this.cm.ref(WebServiceException.class)).arg("Cannot find " + 
/* 339 */           JExpr.quotify('\'', this.wsdlLocation) + " wsdl. Place the resource correctly in the classpath."));
/* 340 */     staticBlock.assign((JAssignmentTarget)exField, (JExpression)exVar);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeClassLoaderBaseResourceWSDLLocation(String className, JDefinedClass cls, JFieldVar urlField, JFieldVar exField) {
/* 360 */     JBlock staticBlock = cls.init();
/* 361 */     JVar exVar = staticBlock.decl((JType)this.cm.ref(WebServiceException.class), "e", JExpr._null());
/* 362 */     JVar urlVar = staticBlock.decl((JType)this.cm.ref(URL.class), "url", JExpr._null());
/* 363 */     JTryBlock tryBlock = staticBlock._try();
/* 364 */     tryBlock.body().assign((JAssignmentTarget)urlVar, (JExpression)JExpr._new(this.cm.ref(URL.class)).arg((JExpression)JExpr.dotclass(this.cm.ref(className)).invoke("getResource").arg(".")).arg(this.wsdlLocation));
/* 365 */     JCatchBlock catchBlock = tryBlock._catch(this.cm.ref(MalformedURLException.class));
/* 366 */     JVar murlVar = catchBlock.param("murl");
/* 367 */     catchBlock.body().assign((JAssignmentTarget)exVar, (JExpression)JExpr._new(this.cm.ref(WebServiceException.class)).arg((JExpression)murlVar));
/* 368 */     staticBlock.assign((JAssignmentTarget)urlField, (JExpression)urlVar);
/* 369 */     staticBlock.assign((JAssignmentTarget)exField, (JExpression)exVar);
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
/*     */   
/*     */   private void writeGetWsdlLocation(JType retType, JDefinedClass cls, JFieldVar urlField, JFieldVar exField) {
/* 386 */     JMethod m = cls.method(20, retType, "__getWsdlLocation");
/* 387 */     JConditional ifBlock = m.body()._if(exField.ne(JExpr._null()));
/* 388 */     ifBlock._then()._throw((JExpression)exField);
/* 389 */     m.body()._return((JExpression)urlField);
/*     */   }
/*     */   
/*     */   private void writeDefaultGetPort(Port port, JType retType, JDefinedClass cls) {
/* 393 */     String portGetter = port.getPortGetter();
/* 394 */     JMethod m = cls.method(1, retType, portGetter);
/* 395 */     JDocComment methodDoc = m.javadoc();
/* 396 */     if (port.getJavaDoc() != null) {
/* 397 */       methodDoc.add(port.getJavaDoc());
/*     */     }
/* 399 */     JCommentPart ret = methodDoc.addReturn();
/* 400 */     ret.add("returns " + retType.name());
/* 401 */     JBlock body = m.body();
/* 402 */     StringBuilder statement = new StringBuilder("return ");
/* 403 */     statement.append("super.getPort(new QName(\"").append(port.getName().getNamespaceURI()).append("\", \"").append(port.getName().getLocalPart()).append("\"), ");
/* 404 */     statement.append(retType.name());
/* 405 */     statement.append(".class);");
/* 406 */     body.directStatement(statement.toString());
/* 407 */     writeWebEndpoint(port, m);
/*     */   }
/*     */   
/*     */   private void writeWebServiceClientAnnotation(Service service, JAnnotationUse wsa) {
/* 411 */     String serviceName = service.getName().getLocalPart();
/* 412 */     String serviceNS = service.getName().getNamespaceURI();
/* 413 */     wsa.param("name", serviceName);
/* 414 */     wsa.param("targetNamespace", serviceNS);
/* 415 */     wsa.param("wsdlLocation", this.wsdlLocation);
/*     */   }
/*     */   
/*     */   private void writeWebEndpoint(Port port, JMethod m) {
/* 419 */     JAnnotationUse webEndpointAnn = m.annotate(this.cm.ref(WebEndpoint.class));
/* 420 */     webEndpointAnn.param("name", port.getName().getLocalPart());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\ServiceGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */