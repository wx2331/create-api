/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Definitions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Documentation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Fault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Operation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Port;
/*     */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Service;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.CustomName;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBindingsConstants;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.Parameter;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAXWSBindingExtensionHandler
/*     */   extends AbstractExtensionHandler
/*     */ {
/*  57 */   private static final ContextClassloaderLocal<XPathFactory> xpf = new ContextClassloaderLocal<XPathFactory>()
/*     */     {
/*     */       protected XPathFactory initialValue() throws Exception {
/*  60 */         return XPathFactory.newInstance();
/*     */       }
/*     */     };
/*     */   
/*  64 */   private final XPath xpath = ((XPathFactory)xpf.get()).newXPath();
/*     */   
/*     */   public JAXWSBindingExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap) {
/*  67 */     super(extensionHandlerMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/*  75 */     return "http://java.sun.com/xml/ns/jaxws";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean parseGlobalJAXWSBindings(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  84 */     context.push();
/*  85 */     context.registerNamespaces(e);
/*     */     
/*  87 */     JAXWSBinding jaxwsBinding = getJAXWSExtension(parent);
/*  88 */     if (jaxwsBinding == null) {
/*  89 */       jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */     }
/*  91 */     String attr = XmlUtil.getAttributeOrNull(e, "wsdlLocation");
/*  92 */     if (attr != null) {
/*  93 */       jaxwsBinding.setWsdlLocation(attr);
/*     */     }
/*     */     
/*  96 */     attr = XmlUtil.getAttributeOrNull(e, "node");
/*  97 */     if (attr != null) {
/*  98 */       jaxwsBinding.setNode(attr);
/*     */     }
/*     */     
/* 101 */     attr = XmlUtil.getAttributeOrNull(e, "version");
/* 102 */     if (attr != null) {
/* 103 */       jaxwsBinding.setVersion(attr);
/*     */     }
/*     */     
/* 106 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 107 */       Element e2 = Util.nextElement(iter);
/* 108 */       if (e2 == null) {
/*     */         break;
/*     */       }
/*     */       
/* 112 */       if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.PACKAGE)) {
/* 113 */         parsePackage(context, jaxwsBinding, e2);
/* 114 */         if (jaxwsBinding.getJaxwsPackage() != null && jaxwsBinding.getJaxwsPackage().getJavaDoc() != null)
/* 115 */           ((Definitions)parent).setDocumentation(new Documentation(jaxwsBinding.getJaxwsPackage().getJavaDoc()));  continue;
/*     */       } 
/* 117 */       if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_WRAPPER_STYLE)) {
/* 118 */         parseWrapperStyle(context, jaxwsBinding, e2); continue;
/* 119 */       }  if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_ASYNC_MAPPING)) {
/* 120 */         parseAsynMapping(context, jaxwsBinding, e2);
/*     */         
/*     */         continue;
/*     */       } 
/* 124 */       if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_MIME_CONTENT)) {
/* 125 */         parseMimeContent(context, jaxwsBinding, e2); continue;
/*     */       } 
/* 127 */       Util.fail("parsing.invalidExtensionElement", e2
/*     */           
/* 129 */           .getTagName(), e2
/* 130 */           .getNamespaceURI());
/* 131 */       return false;
/*     */     } 
/*     */     
/* 134 */     parent.addExtension((TWSDLExtension)jaxwsBinding);
/* 135 */     context.pop();
/*     */ 
/*     */ 
/*     */     
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   private static JAXWSBinding getJAXWSExtension(TWSDLExtensible extensible) {
/* 143 */     for (TWSDLExtension extension : extensible.extensions()) {
/* 144 */       if (extension.getClass().equals(JAXWSBinding.class)) {
/* 145 */         return (JAXWSBinding)extension;
/*     */       }
/*     */     } 
/*     */     
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseProvider(TWSDLParserContext context, JAXWSBinding parent, Element e) {
/* 158 */     String val = e.getTextContent();
/* 159 */     if (val == null) {
/*     */       return;
/*     */     }
/* 162 */     if (val.equals("false") || val.equals("0")) {
/* 163 */       parent.setProvider(Boolean.FALSE);
/* 164 */     } else if (val.equals("true") || val.equals("1")) {
/* 165 */       parent.setProvider(Boolean.TRUE);
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
/*     */   private void parsePackage(TWSDLParserContext context, JAXWSBinding parent, Element e) {
/* 177 */     String packageName = XmlUtil.getAttributeOrNull(e, "name");
/* 178 */     JAXWSBinding binding = parent;
/* 179 */     binding.setJaxwsPackage(new CustomName(packageName, getJavaDoc(e)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseWrapperStyle(TWSDLParserContext context, JAXWSBinding parent, Element e) {
/* 189 */     String val = e.getTextContent();
/* 190 */     if (val == null) {
/*     */       return;
/*     */     }
/* 193 */     if (val.equals("false") || val.equals("0")) {
/* 194 */       parent.setEnableWrapperStyle(Boolean.FALSE);
/* 195 */     } else if (val.equals("true") || val.equals("1")) {
/* 196 */       parent.setEnableWrapperStyle(Boolean.TRUE);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseAsynMapping(TWSDLParserContext context, JAXWSBinding parent, Element e) {
/* 224 */     String val = e.getTextContent();
/* 225 */     if (val == null) {
/*     */       return;
/*     */     }
/* 228 */     if (val.equals("false") || val.equals("0")) {
/* 229 */       parent.setEnableAsyncMapping(Boolean.FALSE);
/* 230 */     } else if (val.equals("true") || val.equals("1")) {
/* 231 */       parent.setEnableAsyncMapping(Boolean.TRUE);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseMimeContent(TWSDLParserContext context, JAXWSBinding parent, Element e) {
/* 242 */     String val = e.getTextContent();
/* 243 */     if (val == null) {
/*     */       return;
/*     */     }
/* 246 */     if (val.equals("false") || val.equals("0")) {
/* 247 */       parent.setEnableMimeContentMapping(Boolean.FALSE);
/* 248 */     } else if (val.equals("true") || val.equals("1")) {
/* 249 */       parent.setEnableMimeContentMapping(Boolean.TRUE);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseMethod(TWSDLParserContext context, JAXWSBinding jaxwsBinding, Element e) {
/* 259 */     String methodName = XmlUtil.getAttributeOrNull(e, "name");
/* 260 */     String javaDoc = getJavaDoc(e);
/* 261 */     CustomName name = new CustomName(methodName, javaDoc);
/* 262 */     jaxwsBinding.setMethodName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseParameter(TWSDLParserContext context, JAXWSBinding jaxwsBinding, Element e) {
/* 271 */     String part = XmlUtil.getAttributeOrNull(e, "part");
/* 272 */     Element msgPartElm = evaluateXPathNode(e.getOwnerDocument(), part, new NamespaceContextImpl(e));
/* 273 */     Node msgElm = msgPartElm.getParentNode();
/*     */ 
/*     */     
/* 276 */     String partName = XmlUtil.getAttributeOrNull(msgPartElm, "name");
/* 277 */     String msgName = XmlUtil.getAttributeOrNull((Element)msgElm, "name");
/* 278 */     if (partName == null || msgName == null) {
/*     */       return;
/*     */     }
/*     */     
/* 282 */     String element = XmlUtil.getAttributeOrNull(e, "childElementName");
/* 283 */     String name = XmlUtil.getAttributeOrNull(e, "name");
/*     */     
/* 285 */     QName elementName = null;
/* 286 */     if (element != null) {
/* 287 */       String uri = e.lookupNamespaceURI(XmlUtil.getPrefix(element));
/* 288 */       elementName = (uri == null) ? null : new QName(uri, XmlUtil.getLocalPart(element));
/*     */     } 
/*     */     
/* 291 */     jaxwsBinding.addParameter(new Parameter(msgName, partName, elementName, name));
/*     */   }
/*     */   
/*     */   private Element evaluateXPathNode(Node target, String expression, NamespaceContext namespaceContext) {
/*     */     NodeList nlst;
/*     */     try {
/* 297 */       this.xpath.setNamespaceContext(namespaceContext);
/* 298 */       nlst = (NodeList)this.xpath.evaluate(expression, target, XPathConstants.NODESET);
/* 299 */     } catch (XPathExpressionException e) {
/* 300 */       Util.fail("internalizer.XPathEvaluationError", e.getMessage());
/* 301 */       return null;
/*     */     } 
/*     */     
/* 304 */     if (nlst.getLength() == 0) {
/* 305 */       Util.fail("internalizer.XPathEvaluatesToNoTarget", new Object[] { expression });
/* 306 */       return null;
/*     */     } 
/*     */     
/* 309 */     if (nlst.getLength() != 1) {
/* 310 */       Util.fail("internalizer.XPathEvaulatesToTooManyTargets", new Object[] { expression, Integer.valueOf(nlst.getLength()) });
/* 311 */       return null;
/*     */     } 
/*     */     
/* 314 */     Node rnode = nlst.item(0);
/* 315 */     if (!(rnode instanceof Element)) {
/* 316 */       Util.fail("internalizer.XPathEvaluatesToNonElement", new Object[] { expression });
/* 317 */       return null;
/*     */     } 
/* 319 */     return (Element)rnode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseClass(TWSDLParserContext context, JAXWSBinding jaxwsBinding, Element e) {
/* 328 */     String className = XmlUtil.getAttributeOrNull(e, "name");
/* 329 */     String javaDoc = getJavaDoc(e);
/* 330 */     jaxwsBinding.setClassName(new CustomName(className, javaDoc));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleDefinitionsExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 336 */     return parseGlobalJAXWSBindings(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlePortTypeExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 341 */     if (XmlUtil.matchesTagNS(e, JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 342 */       context.push();
/* 343 */       context.registerNamespaces(e);
/* 344 */       JAXWSBinding jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */       
/* 346 */       for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 347 */         Element e2 = Util.nextElement(iter);
/* 348 */         if (e2 == null) {
/*     */           break;
/*     */         }
/*     */         
/* 352 */         if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_WRAPPER_STYLE)) {
/* 353 */           parseWrapperStyle(context, jaxwsBinding, e2); continue;
/* 354 */         }  if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_ASYNC_MAPPING)) {
/* 355 */           parseAsynMapping(context, jaxwsBinding, e2); continue;
/* 356 */         }  if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.CLASS)) {
/* 357 */           parseClass(context, jaxwsBinding, e2);
/* 358 */           if (jaxwsBinding.getClassName() != null && jaxwsBinding.getClassName().getJavaDoc() != null && parent instanceof PortType)
/* 359 */             ((PortType)parent).setDocumentation(new Documentation(jaxwsBinding.getClassName().getJavaDoc())); 
/*     */           continue;
/*     */         } 
/* 362 */         Util.fail("parsing.invalidExtensionElement", e2
/*     */             
/* 364 */             .getTagName(), e2
/* 365 */             .getNamespaceURI());
/* 366 */         return false;
/*     */       } 
/*     */       
/* 369 */       parent.addExtension((TWSDLExtension)jaxwsBinding);
/* 370 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 374 */       return true;
/*     */     } 
/* 376 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 378 */         .getTagName(), e
/* 379 */         .getNamespaceURI());
/* 380 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleOperationExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 386 */     if (XmlUtil.matchesTagNS(e, JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 387 */       if (parent instanceof Operation)
/* 388 */         return handlePortTypeOperation(context, (Operation)parent, e); 
/* 389 */       if (parent instanceof BindingOperation) {
/* 390 */         return handleBindingOperation(context, (BindingOperation)parent, e);
/*     */       }
/*     */     } else {
/* 393 */       Util.fail("parsing.invalidExtensionElement", e
/*     */           
/* 395 */           .getTagName(), e
/* 396 */           .getNamespaceURI());
/* 397 */       return false;
/*     */     } 
/* 399 */     return false;
/*     */   }
/*     */   
/*     */   private boolean handleBindingOperation(TWSDLParserContext context, BindingOperation operation, Element e) {
/* 403 */     if (XmlUtil.matchesTagNS(e, JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 404 */       context.push();
/* 405 */       context.registerNamespaces(e);
/* 406 */       JAXWSBinding jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */       
/* 408 */       for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 409 */         Element e2 = Util.nextElement(iter);
/* 410 */         if (e2 == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 417 */         if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_MIME_CONTENT)) {
/* 418 */           parseMimeContent(context, jaxwsBinding, e2); continue;
/* 419 */         }  if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.PARAMETER)) {
/* 420 */           parseParameter(context, jaxwsBinding, e2); continue;
/*     */         } 
/* 422 */         Util.fail("parsing.invalidExtensionElement", e2
/*     */             
/* 424 */             .getTagName(), e2
/* 425 */             .getNamespaceURI());
/* 426 */         return false;
/*     */       } 
/*     */       
/* 429 */       operation.addExtension((TWSDLExtension)jaxwsBinding);
/* 430 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 434 */       return true;
/*     */     } 
/* 436 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 438 */         .getTagName(), e
/* 439 */         .getNamespaceURI());
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean handlePortTypeOperation(TWSDLParserContext context, Operation parent, Element e) {
/* 445 */     context.push();
/* 446 */     context.registerNamespaces(e);
/* 447 */     JAXWSBinding jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */     
/* 449 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 450 */       Element e2 = Util.nextElement(iter);
/* 451 */       if (e2 == null) {
/*     */         break;
/*     */       }
/*     */       
/* 455 */       if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_WRAPPER_STYLE)) {
/* 456 */         parseWrapperStyle(context, jaxwsBinding, e2); continue;
/* 457 */       }  if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_ASYNC_MAPPING)) {
/* 458 */         parseAsynMapping(context, jaxwsBinding, e2); continue;
/* 459 */       }  if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.METHOD)) {
/* 460 */         parseMethod(context, jaxwsBinding, e2);
/* 461 */         if (jaxwsBinding.getMethodName() != null && jaxwsBinding.getMethodName().getJavaDoc() != null)
/* 462 */           parent.setDocumentation(new Documentation(jaxwsBinding.getMethodName().getJavaDoc()));  continue;
/*     */       } 
/* 464 */       if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.PARAMETER)) {
/* 465 */         parseParameter(context, jaxwsBinding, e2); continue;
/*     */       } 
/* 467 */       Util.fail("parsing.invalidExtensionElement", e2
/*     */           
/* 469 */           .getTagName(), e2
/* 470 */           .getNamespaceURI());
/* 471 */       return false;
/*     */     } 
/*     */     
/* 474 */     parent.addExtension((TWSDLExtension)jaxwsBinding);
/* 475 */     context.pop();
/*     */ 
/*     */ 
/*     */     
/* 479 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleBindingExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 484 */     if (XmlUtil.matchesTagNS(e, JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 485 */       context.push();
/* 486 */       context.registerNamespaces(e);
/* 487 */       JAXWSBinding jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */       
/* 489 */       for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 490 */         Element e2 = Util.nextElement(iter);
/* 491 */         if (e2 == null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 498 */         if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.ENABLE_MIME_CONTENT)) {
/* 499 */           parseMimeContent(context, jaxwsBinding, e2); continue;
/*     */         } 
/* 501 */         Util.fail("parsing.invalidExtensionElement", e2
/*     */             
/* 503 */             .getTagName(), e2
/* 504 */             .getNamespaceURI());
/* 505 */         return false;
/*     */       } 
/*     */       
/* 508 */       parent.addExtension((TWSDLExtension)jaxwsBinding);
/* 509 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 513 */       return true;
/*     */     } 
/* 515 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 517 */         .getTagName(), e
/* 518 */         .getNamespaceURI());
/* 519 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleFaultExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 528 */     if (XmlUtil.matchesTagNS(e, JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 529 */       context.push();
/* 530 */       context.registerNamespaces(e);
/* 531 */       JAXWSBinding jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */       
/* 533 */       for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 534 */         Element e2 = Util.nextElement(iter);
/* 535 */         if (e2 == null) {
/*     */           break;
/*     */         }
/* 538 */         if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.CLASS)) {
/* 539 */           parseClass(context, jaxwsBinding, e2);
/* 540 */           if (jaxwsBinding.getClassName() != null && jaxwsBinding.getClassName().getJavaDoc() != null)
/* 541 */             ((Fault)parent).setDocumentation(new Documentation(jaxwsBinding.getClassName().getJavaDoc())); 
/*     */           continue;
/*     */         } 
/* 544 */         Util.fail("parsing.invalidExtensionElement", e2
/*     */             
/* 546 */             .getTagName(), e2
/* 547 */             .getNamespaceURI());
/* 548 */         return false;
/*     */       } 
/*     */       
/* 551 */       parent.addExtension((TWSDLExtension)jaxwsBinding);
/* 552 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 556 */       return true;
/*     */     } 
/* 558 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 560 */         .getTagName(), e
/* 561 */         .getNamespaceURI());
/* 562 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleServiceExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 568 */     if (XmlUtil.matchesTagNS(e, JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 569 */       context.push();
/* 570 */       context.registerNamespaces(e);
/* 571 */       JAXWSBinding jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */       
/* 573 */       for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 574 */         Element e2 = Util.nextElement(iter);
/* 575 */         if (e2 == null) {
/*     */           break;
/*     */         }
/* 578 */         if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.CLASS)) {
/* 579 */           parseClass(context, jaxwsBinding, e2);
/* 580 */           if (jaxwsBinding.getClassName() != null && jaxwsBinding.getClassName().getJavaDoc() != null)
/* 581 */             ((Service)parent).setDocumentation(new Documentation(jaxwsBinding.getClassName().getJavaDoc())); 
/*     */           continue;
/*     */         } 
/* 584 */         Util.fail("parsing.invalidExtensionElement", e2
/*     */             
/* 586 */             .getTagName(), e2
/* 587 */             .getNamespaceURI());
/* 588 */         return false;
/*     */       } 
/*     */       
/* 591 */       parent.addExtension((TWSDLExtension)jaxwsBinding);
/* 592 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 596 */       return true;
/*     */     } 
/* 598 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 600 */         .getTagName(), e
/* 601 */         .getNamespaceURI());
/* 602 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 608 */     if (XmlUtil.matchesTagNS(e, JAXWSBindingsConstants.JAXWS_BINDINGS)) {
/* 609 */       context.push();
/* 610 */       context.registerNamespaces(e);
/* 611 */       JAXWSBinding jaxwsBinding = new JAXWSBinding(context.getLocation(e));
/*     */       
/* 613 */       for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 614 */         Element e2 = Util.nextElement(iter);
/* 615 */         if (e2 == null) {
/*     */           break;
/*     */         }
/*     */         
/* 619 */         if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.PROVIDER)) {
/* 620 */           parseProvider(context, jaxwsBinding, e2); continue;
/* 621 */         }  if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.METHOD)) {
/* 622 */           parseMethod(context, jaxwsBinding, e2);
/* 623 */           if (jaxwsBinding.getMethodName() != null && jaxwsBinding.getMethodName().getJavaDoc() != null)
/* 624 */             ((Port)parent).setDocumentation(new Documentation(jaxwsBinding.getMethodName().getJavaDoc())); 
/*     */           continue;
/*     */         } 
/* 627 */         Util.fail("parsing.invalidExtensionElement", e2
/*     */             
/* 629 */             .getTagName(), e2
/* 630 */             .getNamespaceURI());
/* 631 */         return false;
/*     */       } 
/*     */       
/* 634 */       parent.addExtension((TWSDLExtension)jaxwsBinding);
/* 635 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 639 */       return true;
/*     */     } 
/* 641 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 643 */         .getTagName(), e
/* 644 */         .getNamespaceURI());
/* 645 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getJavaDoc(Element e) {
/* 650 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 651 */       Element e2 = Util.nextElement(iter);
/* 652 */       if (e2 == null) {
/*     */         break;
/*     */       }
/* 655 */       if (XmlUtil.matchesTagNS(e2, JAXWSBindingsConstants.JAVADOC)) {
/* 656 */         return XmlUtil.getTextForNode(e2);
/*     */       }
/*     */     } 
/* 659 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\JAXWSBindingExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */