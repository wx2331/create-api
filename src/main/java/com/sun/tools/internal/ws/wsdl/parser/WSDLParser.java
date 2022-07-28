/*      */ package com.sun.tools.internal.ws.wsdl.parser;
/*      */ 
/*      */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*      */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensionHandler;
/*      */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*      */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*      */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*      */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*      */ import com.sun.tools.internal.ws.wscompile.ErrorReceiverFilter;
/*      */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Binding;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingFault;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingInput;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingOperation;
/*      */ import com.sun.tools.internal.ws.wsdl.document.BindingOutput;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Definitions;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Documentation;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Fault;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Import;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Input;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*      */ import com.sun.tools.internal.ws.wsdl.document.MessagePart;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Operation;
/*      */ import com.sun.tools.internal.ws.wsdl.document.OperationStyle;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Output;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Port;
/*      */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*      */ import com.sun.tools.internal.ws.wsdl.document.Service;
/*      */ import com.sun.tools.internal.ws.wsdl.document.WSDLConstants;
/*      */ import com.sun.tools.internal.ws.wsdl.document.WSDLDocument;
/*      */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaConstants;
/*      */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaKinds;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.Defining;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.ParserListener;
/*      */ import com.sun.tools.internal.ws.wsdl.framework.TWSDLParserContextImpl;
/*      */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
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
/*      */ 
/*      */ 
/*      */ public class WSDLParser
/*      */ {
/*      */   private final ErrorReceiverFilter errReceiver;
/*      */   private WsimportOptions options;
/*      */   private final Map extensionHandlers;
/*      */   private MetadataFinder forest;
/*      */   private ArrayList<ParserListener> listeners;
/*      */   
/*      */   public WSDLParser(WsimportOptions options, ErrorReceiverFilter errReceiver, MetadataFinder forest) {
/*   97 */     this.extensionHandlers = new HashMap<>();
/*   98 */     this.options = options;
/*   99 */     this.errReceiver = errReceiver;
/*  100 */     if (forest == null) {
/*  101 */       forest = new MetadataFinder(new WSDLInternalizationLogic(), options, (ErrorReceiver)errReceiver);
/*  102 */       forest.parseWSDL();
/*  103 */       if (forest.isMexMetadata) {
/*  104 */         errReceiver.reset();
/*      */       }
/*      */     } 
/*  107 */     this.forest = forest;
/*      */     
/*  109 */     register(new SOAPExtensionHandler(this.extensionHandlers));
/*  110 */     register(new HTTPExtensionHandler(this.extensionHandlers));
/*  111 */     register(new MIMEExtensionHandler(this.extensionHandlers));
/*  112 */     register(new JAXWSBindingExtensionHandler(this.extensionHandlers));
/*  113 */     register(new SOAP12ExtensionHandler(this.extensionHandlers));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  118 */     register(new W3CAddressingExtensionHandler(this.extensionHandlers, (ErrorReceiver)errReceiver));
/*  119 */     register(new W3CAddressingMetadataExtensionHandler(this.extensionHandlers, (ErrorReceiver)errReceiver));
/*  120 */     register(new Policy12ExtensionHandler());
/*  121 */     register(new Policy15ExtensionHandler());
/*  122 */     for (TWSDLExtensionHandler te : ServiceFinder.<TWSDLExtensionHandler>find(TWSDLExtensionHandler.class)) {
/*  123 */       register(te);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   WSDLParser(WsimportOptions options, ErrorReceiverFilter errReceiver) {
/*  130 */     this(options, errReceiver, null);
/*      */   }
/*      */   private void register(TWSDLExtensionHandler h) {
/*  133 */     this.extensionHandlers.put(h.getNamespaceURI(), h);
/*      */   }
/*      */   
/*      */   public void addParserListener(ParserListener l) {
/*  137 */     if (this.listeners == null) {
/*  138 */       this.listeners = new ArrayList<>();
/*      */     }
/*  140 */     this.listeners.add(l);
/*      */   }
/*      */ 
/*      */   
/*      */   public WSDLDocument parse() throws SAXException, IOException {
/*  145 */     for (InputSource value : this.options.getWSDLBindings()) {
/*  146 */       this.errReceiver.pollAbort();
/*  147 */       Document root = this.forest.parse(value, false);
/*  148 */       if (root != null) {
/*  149 */         Element binding = root.getDocumentElement();
/*  150 */         if (!Internalizer.fixNull(binding.getNamespaceURI()).equals("http://java.sun.com/xml/ns/jaxws") || 
/*  151 */           !binding.getLocalName().equals("bindings")) {
/*  152 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(binding), WsdlMessages.PARSER_NOT_A_BINDING_FILE(binding
/*  153 */                 .getNamespaceURI(), binding
/*  154 */                 .getLocalName()));
/*      */         }
/*      */         else {
/*      */           
/*  158 */           NodeList nl = binding.getElementsByTagNameNS("http://java.sun.com/xml/ns/javaee", "handler-chains");
/*      */           
/*  160 */           for (int i = 0; i < nl.getLength(); i++)
/*  161 */             this.options.addHandlerChainConfiguration((Element)nl.item(i)); 
/*      */         } 
/*      */       } 
/*      */     } 
/*  165 */     return buildWSDLDocument();
/*      */   }
/*      */   
/*      */   public MetadataFinder getDOMForest() {
/*  169 */     return this.forest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private WSDLDocument buildWSDLDocument() {
/*  178 */     String location = this.forest.getRootWSDL();
/*      */ 
/*      */     
/*  181 */     if (location == null) {
/*  182 */       return null;
/*      */     }
/*  184 */     Document root = this.forest.get(location);
/*      */     
/*  186 */     if (root == null) {
/*  187 */       return null;
/*      */     }
/*  189 */     WSDLDocument document = new WSDLDocument(this.forest, (ErrorReceiver)this.errReceiver);
/*  190 */     document.setSystemId(location);
/*  191 */     TWSDLParserContextImpl context = new TWSDLParserContextImpl(this.forest, (AbstractDocument)document, this.listeners, (ErrorReceiver)this.errReceiver);
/*      */     
/*  193 */     Definitions definitions = parseDefinitions(context, root);
/*  194 */     document.setDefinitions(definitions);
/*  195 */     return document;
/*      */   }
/*      */   
/*      */   private Definitions parseDefinitions(TWSDLParserContextImpl context, Document root) {
/*  199 */     context.pushWSDLLocation();
/*  200 */     context.setWSDLLocation(context.getDocument().getSystemId());
/*      */     
/*  202 */     (new Internalizer(this.forest, this.options, (ErrorReceiver)this.errReceiver)).transform();
/*      */     
/*  204 */     Definitions definitions = parseDefinitionsNoImport(context, root);
/*  205 */     if (definitions == null) {
/*  206 */       Locator locator = this.forest.locatorTable.getStartLocation(root.getDocumentElement());
/*  207 */       this.errReceiver.error(locator, WsdlMessages.PARSING_NOT_AWSDL(locator.getSystemId()));
/*      */     } 
/*      */     
/*  210 */     processImports(context);
/*  211 */     context.popWSDLLocation();
/*  212 */     return definitions;
/*      */   }
/*      */   
/*      */   private void processImports(TWSDLParserContextImpl context) {
/*  216 */     for (String location : this.forest.getExternalReferences()) {
/*  217 */       if (!context.getDocument().isImportedDocument(location)) {
/*  218 */         Document doc = this.forest.get(location);
/*  219 */         if (doc == null)
/*      */           continue; 
/*  221 */         Definitions importedDefinitions = parseDefinitionsNoImport(context, doc);
/*  222 */         if (importedDefinitions == null)
/*      */           continue; 
/*  224 */         context.getDocument().addImportedEntity((Entity)importedDefinitions);
/*  225 */         context.getDocument().addImportedDocument(location);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Definitions parseDefinitionsNoImport(TWSDLParserContextImpl context, Document doc) {
/*  233 */     Element e = doc.getDocumentElement();
/*      */     
/*  235 */     if (e.getNamespaceURI() == null || !e.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/") || !e.getLocalName().equals("definitions")) {
/*  236 */       return null;
/*      */     }
/*  238 */     context.push();
/*  239 */     context.registerNamespaces(e);
/*      */     
/*  241 */     Definitions definitions = new Definitions(context.getDocument(), this.forest.locatorTable.getStartLocation(e));
/*  242 */     String name = XmlUtil.getAttributeOrNull(e, "name");
/*  243 */     definitions.setName(name);
/*      */ 
/*      */     
/*  246 */     String targetNamespaceURI = XmlUtil.getAttributeOrNull(e, "targetNamespace");
/*      */     
/*  248 */     definitions.setTargetNamespaceURI(targetNamespaceURI);
/*      */     
/*  250 */     boolean gotDocumentation = false;
/*  251 */     boolean gotTypes = false;
/*      */     
/*  253 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  254 */       Element e2 = Util.nextElement(iter);
/*  255 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*  258 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  259 */         if (gotDocumentation) {
/*  260 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e2), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*  261 */           return null;
/*      */         } 
/*  263 */         gotDocumentation = true;
/*  264 */         if (definitions.getDocumentation() == null)
/*  265 */           definitions.setDocumentation(getDocumentationFor(e2));  continue;
/*  266 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_TYPES)) {
/*  267 */         if (gotTypes && !this.options.isExtensionMode()) {
/*  268 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e2), WsdlMessages.PARSING_ONLY_ONE_TYPES_ALLOWED("definitions"));
/*  269 */           return null;
/*      */         } 
/*  271 */         gotTypes = true;
/*      */ 
/*      */         
/*  274 */         if (!this.options.isExtensionMode())
/*  275 */           validateSchemaImports(e2);  continue;
/*  276 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_MESSAGE)) {
/*  277 */         Message message = parseMessage(context, definitions, e2);
/*  278 */         definitions.add(message); continue;
/*      */       } 
/*  280 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_PORT_TYPE)) {
/*  281 */         PortType portType = parsePortType(context, definitions, e2);
/*  282 */         definitions.add(portType); continue;
/*  283 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_BINDING)) {
/*  284 */         Binding binding = parseBinding(context, definitions, e2);
/*  285 */         definitions.add(binding); continue;
/*  286 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_SERVICE)) {
/*  287 */         Service service = parseService(context, definitions, e2);
/*  288 */         definitions.add(service); continue;
/*  289 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_IMPORT)) {
/*  290 */         definitions.add(parseImport(context, definitions, e2)); continue;
/*  291 */       }  if (XmlUtil.matchesTagNS(e2, SchemaConstants.QNAME_IMPORT)) {
/*  292 */         this.errReceiver.warning(this.forest.locatorTable.getStartLocation(e2), WsdlMessages.WARNING_WSI_R_2003());
/*      */         continue;
/*      */       } 
/*  295 */       checkNotWsdlElement(e2);
/*  296 */       if (!handleExtension(context, (TWSDLExtensible)definitions, e2)) {
/*  297 */         checkNotWsdlRequired(e2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  302 */     context.pop();
/*  303 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_DEFINITIONS, (Entity)definitions);
/*      */ 
/*      */     
/*  306 */     return definitions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Message parseMessage(TWSDLParserContextImpl context, Definitions definitions, Element e) {
/*  313 */     context.push();
/*  314 */     context.registerNamespaces(e);
/*  315 */     Message message = new Message((Defining)definitions, this.forest.locatorTable.getStartLocation(e), (ErrorReceiver)this.errReceiver);
/*  316 */     String name = Util.getRequiredAttribute(e, "name");
/*  317 */     message.setName(name);
/*      */     
/*  319 */     boolean gotDocumentation = false;
/*      */     
/*  321 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  322 */       Element e2 = Util.nextElement(iter);
/*  323 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*  326 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  327 */         if (gotDocumentation) {
/*  328 */           Util.fail("parsing.onlyOneDocumentationAllowed", e
/*      */               
/*  330 */               .getLocalName());
/*      */         }
/*  332 */         gotDocumentation = true;
/*  333 */         message.setDocumentation(getDocumentationFor(e2)); continue;
/*  334 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_PART)) {
/*  335 */         MessagePart part = parseMessagePart(context, e2);
/*  336 */         message.add(part);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  347 */     context.pop();
/*  348 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_MESSAGE, (Entity)message);
/*  349 */     return message;
/*      */   }
/*      */   
/*      */   private MessagePart parseMessagePart(TWSDLParserContextImpl context, Element e) {
/*  353 */     context.push();
/*  354 */     context.registerNamespaces(e);
/*  355 */     MessagePart part = new MessagePart(this.forest.locatorTable.getStartLocation(e));
/*  356 */     String partName = Util.getRequiredAttribute(e, "name");
/*  357 */     part.setName(partName);
/*      */ 
/*      */     
/*  360 */     String elementAttr = XmlUtil.getAttributeOrNull(e, "element");
/*  361 */     String typeAttr = XmlUtil.getAttributeOrNull(e, "type");
/*      */     
/*  363 */     if (elementAttr != null) {
/*  364 */       if (typeAttr != null) {
/*  365 */         this.errReceiver.error(context.getLocation(e), WsdlMessages.PARSING_ONLY_ONE_OF_ELEMENT_OR_TYPE_REQUIRED(partName));
/*      */       }
/*      */ 
/*      */       
/*  369 */       part.setDescriptor(context.translateQualifiedName(context.getLocation(e), elementAttr));
/*  370 */       part.setDescriptorKind(SchemaKinds.XSD_ELEMENT);
/*  371 */     } else if (typeAttr != null) {
/*  372 */       part.setDescriptor(context.translateQualifiedName(context.getLocation(e), typeAttr));
/*  373 */       part.setDescriptorKind(SchemaKinds.XSD_TYPE);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  378 */       this.errReceiver.warning(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ELEMENT_OR_TYPE_REQUIRED(partName));
/*      */     } 
/*      */     
/*  381 */     context.pop();
/*  382 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_PART, (Entity)part);
/*  383 */     return part;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PortType parsePortType(TWSDLParserContextImpl context, Definitions definitions, Element e) {
/*  390 */     context.push();
/*  391 */     context.registerNamespaces(e);
/*  392 */     PortType portType = new PortType((Defining)definitions, this.forest.locatorTable.getStartLocation(e), (ErrorReceiver)this.errReceiver);
/*  393 */     String name = Util.getRequiredAttribute(e, "name");
/*  394 */     portType.setName(name);
/*      */     
/*  396 */     boolean gotDocumentation = false;
/*      */     
/*  398 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  399 */       Element e2 = Util.nextElement(iter);
/*  400 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*  403 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  404 */         if (gotDocumentation) {
/*  405 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */         }
/*  407 */         gotDocumentation = true;
/*  408 */         if (portType.getDocumentation() == null)
/*  409 */           portType.setDocumentation(getDocumentationFor(e2));  continue;
/*      */       } 
/*  411 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_OPERATION)) {
/*  412 */         Operation op = parsePortTypeOperation(context, e2);
/*  413 */         op.setParent((TWSDLExtensible)portType);
/*  414 */         portType.add(op);
/*      */         continue;
/*      */       } 
/*  417 */       checkNotWsdlElement(e2);
/*  418 */       if (!handleExtension(context, (TWSDLExtensible)portType, e2)) {
/*  419 */         checkNotWsdlRequired(e2);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  429 */     context.pop();
/*  430 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_PORT_TYPE, (Entity)portType);
/*  431 */     return portType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Operation parsePortTypeOperation(TWSDLParserContextImpl context, Element e) {
/*  437 */     context.push();
/*  438 */     context.registerNamespaces(e);
/*      */     
/*  440 */     Operation operation = new Operation(this.forest.locatorTable.getStartLocation(e));
/*  441 */     String name = Util.getRequiredAttribute(e, "name");
/*  442 */     operation.setName(name);
/*      */     
/*  444 */     String parameterOrderAttr = XmlUtil.getAttributeOrNull(e, "parameterOrder");
/*  445 */     operation.setParameterOrder(parameterOrderAttr);
/*      */     
/*  447 */     boolean gotDocumentation = false;
/*      */     
/*  449 */     boolean gotInput = false;
/*  450 */     boolean gotOutput = false;
/*  451 */     boolean gotFault = false;
/*  452 */     boolean inputBeforeOutput = false;
/*      */     
/*  454 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  455 */       Element e2 = Util.nextElement(iter);
/*  456 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*  459 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  460 */         if (gotDocumentation) {
/*  461 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e2), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e2.getLocalName()));
/*      */         }
/*  463 */         gotDocumentation = true;
/*  464 */         if (operation.getDocumentation() == null)
/*  465 */           operation.setDocumentation(getDocumentationFor(e2));  continue;
/*  466 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_INPUT)) {
/*  467 */         if (gotInput) {
/*  468 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_TOO_MANY_ELEMENTS("input", "operation", name));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  473 */         context.push();
/*  474 */         context.registerNamespaces(e2);
/*  475 */         Input input = new Input(this.forest.locatorTable.getStartLocation(e2), (ErrorReceiver)this.errReceiver);
/*  476 */         input.setParent((TWSDLExtensible)operation);
/*      */         
/*  478 */         String messageAttr = Util.getRequiredAttribute(e2, "message");
/*  479 */         input.setMessage(context.translateQualifiedName(context.getLocation(e2), messageAttr));
/*      */         
/*  481 */         String nameAttr = XmlUtil.getAttributeOrNull(e2, "name");
/*  482 */         input.setName(nameAttr);
/*  483 */         operation.setInput(input);
/*  484 */         gotInput = true;
/*  485 */         if (gotOutput) {
/*  486 */           inputBeforeOutput = false;
/*      */         }
/*      */ 
/*      */         
/*  490 */         Iterator<Attr> iter2 = XmlUtil.getAllAttributes(e2);
/*  491 */         while (iter2.hasNext()) {
/*      */           
/*  493 */           Attr e3 = iter2.next();
/*  494 */           if (e3.getLocalName().equals("message") || e3
/*  495 */             .getLocalName().equals("name")) {
/*      */             continue;
/*      */           }
/*      */           
/*  499 */           checkNotWsdlAttribute(e3);
/*  500 */           handleExtension(context, (TWSDLExtensible)input, e3, e2);
/*      */         } 
/*      */ 
/*      */         
/*  504 */         boolean gotDocumentation2 = false;
/*  505 */         Iterator iterator = XmlUtil.getAllChildren(e2);
/*  506 */         while (iterator.hasNext()) {
/*      */           
/*  508 */           Element e3 = Util.nextElement(iterator);
/*  509 */           if (e3 == null) {
/*      */             break;
/*      */           }
/*      */           
/*  513 */           if (XmlUtil.matchesTagNS(e3, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  514 */             if (gotDocumentation2) {
/*  515 */               this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */             }
/*  517 */             gotDocumentation2 = true;
/*  518 */             input.setDocumentation(getDocumentationFor(e3)); continue;
/*      */           } 
/*  520 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e3), WsdlMessages.PARSING_INVALID_ELEMENT(e3.getTagName(), e3
/*  521 */                 .getNamespaceURI()));
/*      */         } 
/*      */         
/*  524 */         context.pop(); continue;
/*  525 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_OUTPUT)) {
/*  526 */         if (gotOutput) {
/*  527 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_TOO_MANY_ELEMENTS("input", "operation", name));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  532 */         context.push();
/*  533 */         context.registerNamespaces(e2);
/*  534 */         Output output = new Output(this.forest.locatorTable.getStartLocation(e2), (ErrorReceiver)this.errReceiver);
/*  535 */         output.setParent((TWSDLExtensible)operation);
/*      */         
/*  537 */         String messageAttr = Util.getRequiredAttribute(e2, "message");
/*  538 */         output.setMessage(context.translateQualifiedName(context.getLocation(e2), messageAttr));
/*      */         
/*  540 */         String nameAttr = XmlUtil.getAttributeOrNull(e2, "name");
/*  541 */         output.setName(nameAttr);
/*  542 */         operation.setOutput(output);
/*  543 */         gotOutput = true;
/*  544 */         if (gotInput) {
/*  545 */           inputBeforeOutput = true;
/*      */         }
/*      */ 
/*      */         
/*  549 */         Iterator<Attr> iter2 = XmlUtil.getAllAttributes(e2);
/*  550 */         while (iter2.hasNext()) {
/*      */           
/*  552 */           Attr e3 = iter2.next();
/*  553 */           if (e3.getLocalName().equals("message") || e3
/*  554 */             .getLocalName().equals("name")) {
/*      */             continue;
/*      */           }
/*      */           
/*  558 */           checkNotWsdlAttribute(e3);
/*  559 */           handleExtension(context, (TWSDLExtensible)output, e3, e2);
/*      */         } 
/*      */ 
/*      */         
/*  563 */         boolean gotDocumentation2 = false;
/*  564 */         Iterator iterator = XmlUtil.getAllChildren(e2);
/*  565 */         while (iterator.hasNext()) {
/*      */           
/*  567 */           Element e3 = Util.nextElement(iterator);
/*  568 */           if (e3 == null) {
/*      */             break;
/*      */           }
/*      */           
/*  572 */           if (XmlUtil.matchesTagNS(e3, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  573 */             if (gotDocumentation2) {
/*  574 */               this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */             }
/*  576 */             gotDocumentation2 = true;
/*  577 */             output.setDocumentation(getDocumentationFor(e3)); continue;
/*      */           } 
/*  579 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e3), WsdlMessages.PARSING_INVALID_ELEMENT(e3.getTagName(), e3
/*  580 */                 .getNamespaceURI()));
/*      */         } 
/*      */         
/*  583 */         context.pop(); continue;
/*  584 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_FAULT)) {
/*  585 */         context.push();
/*  586 */         context.registerNamespaces(e2);
/*  587 */         Fault fault = new Fault(this.forest.locatorTable.getStartLocation(e2));
/*  588 */         fault.setParent((TWSDLExtensible)operation);
/*      */         
/*  590 */         String messageAttr = Util.getRequiredAttribute(e2, "message");
/*  591 */         fault.setMessage(context.translateQualifiedName(context.getLocation(e2), messageAttr));
/*      */         
/*  593 */         String nameAttr = XmlUtil.getAttributeOrNull(e2, "name");
/*  594 */         fault.setName(nameAttr);
/*  595 */         operation.addFault(fault);
/*  596 */         gotFault = true;
/*      */ 
/*      */         
/*  599 */         Iterator<Attr> iter2 = XmlUtil.getAllAttributes(e2);
/*  600 */         while (iter2.hasNext()) {
/*      */           
/*  602 */           Attr e3 = iter2.next();
/*  603 */           if (e3.getLocalName().equals("message") || e3
/*  604 */             .getLocalName().equals("name")) {
/*      */             continue;
/*      */           }
/*      */           
/*  608 */           checkNotWsdlAttribute(e3);
/*  609 */           handleExtension(context, (TWSDLExtensible)fault, e3, e2);
/*      */         } 
/*      */ 
/*      */         
/*  613 */         boolean gotDocumentation2 = false;
/*  614 */         Iterator iterator = XmlUtil.getAllChildren(e2);
/*  615 */         while (iterator.hasNext()) {
/*      */           
/*  617 */           Element e3 = Util.nextElement(iterator);
/*  618 */           if (e3 == null) {
/*      */             break;
/*      */           }
/*      */           
/*  622 */           if (XmlUtil.matchesTagNS(e3, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  623 */             if (gotDocumentation2) {
/*  624 */               this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */             }
/*  626 */             gotDocumentation2 = true;
/*  627 */             if (fault.getDocumentation() == null)
/*  628 */               fault.setDocumentation(getDocumentationFor(e3)); 
/*      */             continue;
/*      */           } 
/*  631 */           checkNotWsdlElement(e3);
/*  632 */           if (!handleExtension(context, (TWSDLExtensible)fault, e3)) {
/*  633 */             checkNotWsdlRequired(e3);
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  642 */         context.pop();
/*      */         continue;
/*      */       } 
/*  645 */       checkNotWsdlElement(e2);
/*  646 */       if (!handleExtension(context, (TWSDLExtensible)operation, e2)) {
/*  647 */         checkNotWsdlRequired(e2);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  657 */     if (gotInput && !gotOutput && !gotFault) {
/*  658 */       operation.setStyle(OperationStyle.ONE_WAY);
/*  659 */     } else if (gotInput && gotOutput && inputBeforeOutput) {
/*  660 */       operation.setStyle(OperationStyle.REQUEST_RESPONSE);
/*  661 */     } else if (gotInput && gotOutput && !inputBeforeOutput) {
/*  662 */       operation.setStyle(OperationStyle.SOLICIT_RESPONSE);
/*  663 */     } else if (gotOutput && !gotInput && !gotFault) {
/*  664 */       operation.setStyle(OperationStyle.NOTIFICATION);
/*      */     } else {
/*  666 */       this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_INVALID_OPERATION_STYLE(name));
/*      */     } 
/*      */     
/*  669 */     context.pop();
/*  670 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_OPERATION, (Entity)operation);
/*  671 */     return operation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Binding parseBinding(TWSDLParserContextImpl context, Definitions definitions, Element e) {
/*  678 */     context.push();
/*  679 */     context.registerNamespaces(e);
/*  680 */     Binding binding = new Binding((Defining)definitions, this.forest.locatorTable.getStartLocation(e), (ErrorReceiver)this.errReceiver);
/*  681 */     String name = Util.getRequiredAttribute(e, "name");
/*  682 */     binding.setName(name);
/*  683 */     String typeAttr = Util.getRequiredAttribute(e, "type");
/*  684 */     binding.setPortType(context.translateQualifiedName(context.getLocation(e), typeAttr));
/*      */     
/*  686 */     boolean gotDocumentation = false;
/*      */     
/*  688 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  689 */       Element e2 = Util.nextElement(iter);
/*  690 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*  693 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  694 */         if (gotDocumentation) {
/*  695 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */         }
/*  697 */         gotDocumentation = true;
/*  698 */         binding.setDocumentation(getDocumentationFor(e2)); continue;
/*      */       } 
/*  700 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_OPERATION)) {
/*  701 */         BindingOperation op = parseBindingOperation(context, e2);
/*  702 */         binding.add(op);
/*      */         continue;
/*      */       } 
/*  705 */       checkNotWsdlElement(e2);
/*  706 */       if (!handleExtension(context, (TWSDLExtensible)binding, e2)) {
/*  707 */         checkNotWsdlRequired(e2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  712 */     context.pop();
/*  713 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_BINDING, (Entity)binding);
/*  714 */     return binding;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private BindingOperation parseBindingOperation(TWSDLParserContextImpl context, Element e) {
/*  720 */     context.push();
/*  721 */     context.registerNamespaces(e);
/*  722 */     BindingOperation operation = new BindingOperation(this.forest.locatorTable.getStartLocation(e));
/*  723 */     String name = Util.getRequiredAttribute(e, "name");
/*  724 */     operation.setName(name);
/*      */     
/*  726 */     boolean gotDocumentation = false;
/*      */     
/*  728 */     boolean gotInput = false;
/*  729 */     boolean gotOutput = false;
/*  730 */     boolean gotFault = false;
/*  731 */     boolean inputBeforeOutput = false;
/*      */     
/*  733 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  734 */       Element e2 = Util.nextElement(iter);
/*  735 */       if (e2 == null)
/*      */         break; 
/*  737 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  738 */         if (gotDocumentation) {
/*  739 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */         }
/*  741 */         gotDocumentation = true;
/*  742 */         operation.setDocumentation(getDocumentationFor(e2)); continue;
/*  743 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_INPUT)) {
/*  744 */         if (gotInput) {
/*  745 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_TOO_MANY_ELEMENTS("input", "operation", name));
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  751 */         context.push();
/*  752 */         context.registerNamespaces(e2);
/*  753 */         BindingInput input = new BindingInput(this.forest.locatorTable.getStartLocation(e2));
/*      */         
/*  755 */         String nameAttr = XmlUtil.getAttributeOrNull(e2, "name");
/*  756 */         input.setName(nameAttr);
/*  757 */         operation.setInput(input);
/*  758 */         gotInput = true;
/*  759 */         if (gotOutput) {
/*  760 */           inputBeforeOutput = false;
/*      */         }
/*      */ 
/*      */         
/*  764 */         boolean gotDocumentation2 = false;
/*  765 */         Iterator iter2 = XmlUtil.getAllChildren(e2);
/*  766 */         while (iter2.hasNext()) {
/*      */           
/*  768 */           Element e3 = Util.nextElement(iter2);
/*  769 */           if (e3 == null) {
/*      */             break;
/*      */           }
/*      */           
/*  773 */           if (XmlUtil.matchesTagNS(e3, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  774 */             if (gotDocumentation2) {
/*  775 */               this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */             }
/*  777 */             gotDocumentation2 = true;
/*  778 */             input.setDocumentation(getDocumentationFor(e3));
/*      */             continue;
/*      */           } 
/*  781 */           checkNotWsdlElement(e3);
/*  782 */           if (!handleExtension(context, (TWSDLExtensible)input, e3)) {
/*  783 */             checkNotWsdlRequired(e3);
/*      */           }
/*      */         } 
/*      */         
/*  787 */         context.pop(); continue;
/*  788 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_OUTPUT)) {
/*  789 */         if (gotOutput) {
/*  790 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_TOO_MANY_ELEMENTS("input", "operation", name));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  795 */         context.push();
/*  796 */         context.registerNamespaces(e2);
/*  797 */         BindingOutput output = new BindingOutput(this.forest.locatorTable.getStartLocation(e2));
/*      */         
/*  799 */         String nameAttr = XmlUtil.getAttributeOrNull(e2, "name");
/*  800 */         output.setName(nameAttr);
/*  801 */         operation.setOutput(output);
/*  802 */         gotOutput = true;
/*  803 */         if (gotInput) {
/*  804 */           inputBeforeOutput = true;
/*      */         }
/*      */ 
/*      */         
/*  808 */         boolean gotDocumentation2 = false;
/*  809 */         Iterator iter2 = XmlUtil.getAllChildren(e2);
/*  810 */         while (iter2.hasNext()) {
/*      */ 
/*      */           
/*  813 */           Element e3 = Util.nextElement(iter2);
/*  814 */           if (e3 == null) {
/*      */             break;
/*      */           }
/*      */           
/*  818 */           if (XmlUtil.matchesTagNS(e3, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  819 */             if (gotDocumentation2) {
/*  820 */               this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */             }
/*  822 */             gotDocumentation2 = true;
/*  823 */             output.setDocumentation(getDocumentationFor(e3));
/*      */             continue;
/*      */           } 
/*  826 */           checkNotWsdlElement(e3);
/*  827 */           if (!handleExtension(context, (TWSDLExtensible)output, e3)) {
/*  828 */             checkNotWsdlRequired(e3);
/*      */           }
/*      */         } 
/*      */         
/*  832 */         context.pop(); continue;
/*  833 */       }  if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_FAULT)) {
/*  834 */         context.push();
/*  835 */         context.registerNamespaces(e2);
/*  836 */         BindingFault fault = new BindingFault(this.forest.locatorTable.getStartLocation(e2));
/*      */         
/*  838 */         String nameAttr = Util.getRequiredAttribute(e2, "name");
/*  839 */         fault.setName(nameAttr);
/*  840 */         operation.addFault(fault);
/*  841 */         gotFault = true;
/*      */ 
/*      */         
/*  844 */         boolean gotDocumentation2 = false;
/*  845 */         Iterator iter2 = XmlUtil.getAllChildren(e2);
/*  846 */         while (iter2.hasNext()) {
/*      */           
/*  848 */           Element e3 = Util.nextElement(iter2);
/*  849 */           if (e3 == null) {
/*      */             break;
/*      */           }
/*      */           
/*  853 */           if (XmlUtil.matchesTagNS(e3, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  854 */             if (gotDocumentation2) {
/*  855 */               this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */             }
/*  857 */             gotDocumentation2 = true;
/*  858 */             if (fault.getDocumentation() == null)
/*  859 */               fault.setDocumentation(getDocumentationFor(e3)); 
/*      */             continue;
/*      */           } 
/*  862 */           checkNotWsdlElement(e3);
/*  863 */           if (!handleExtension(context, (TWSDLExtensible)fault, e3)) {
/*  864 */             checkNotWsdlRequired(e3);
/*      */           }
/*      */         } 
/*      */         
/*  868 */         context.pop();
/*      */         continue;
/*      */       } 
/*  871 */       checkNotWsdlElement(e2);
/*  872 */       if (!handleExtension(context, (TWSDLExtensible)operation, e2)) {
/*  873 */         checkNotWsdlRequired(e2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  878 */     if (gotInput && !gotOutput && !gotFault) {
/*  879 */       operation.setStyle(OperationStyle.ONE_WAY);
/*  880 */     } else if (gotInput && gotOutput && inputBeforeOutput) {
/*  881 */       operation.setStyle(OperationStyle.REQUEST_RESPONSE);
/*  882 */     } else if (gotInput && gotOutput && !inputBeforeOutput) {
/*  883 */       operation.setStyle(OperationStyle.SOLICIT_RESPONSE);
/*  884 */     } else if (gotOutput && !gotInput && !gotFault) {
/*  885 */       operation.setStyle(OperationStyle.NOTIFICATION);
/*      */     } else {
/*  887 */       this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_INVALID_OPERATION_STYLE(name));
/*      */     } 
/*      */     
/*  890 */     context.pop();
/*  891 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_OPERATION, (Entity)operation);
/*  892 */     return operation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Import parseImport(TWSDLParserContextImpl context, Definitions definitions, Element e) {
/*  899 */     context.push();
/*  900 */     context.registerNamespaces(e);
/*  901 */     Import anImport = new Import(this.forest.locatorTable.getStartLocation(e));
/*      */     
/*  903 */     String namespace = Util.getRequiredAttribute(e, "namespace");
/*  904 */     anImport.setNamespace(namespace);
/*  905 */     String location = Util.getRequiredAttribute(e, "location");
/*  906 */     anImport.setLocation(location);
/*      */ 
/*      */     
/*  909 */     boolean gotDocumentation = false;
/*      */     
/*  911 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  912 */       Element e2 = Util.nextElement(iter);
/*  913 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*  916 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  917 */         if (gotDocumentation) {
/*  918 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */         }
/*  920 */         gotDocumentation = true;
/*  921 */         anImport.setDocumentation(getDocumentationFor(e2)); continue;
/*      */       } 
/*  923 */       this.errReceiver.error(this.forest.locatorTable.getStartLocation(e2), WsdlMessages.PARSING_INVALID_ELEMENT(e2.getTagName(), e2
/*  924 */             .getNamespaceURI()));
/*      */     } 
/*      */     
/*  927 */     context.pop();
/*  928 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_IMPORT, (Entity)anImport);
/*  929 */     return anImport;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Service parseService(TWSDLParserContextImpl context, Definitions definitions, Element e) {
/*  936 */     context.push();
/*  937 */     context.registerNamespaces(e);
/*  938 */     Service service = new Service((Defining)definitions, this.forest.locatorTable.getStartLocation(e), (ErrorReceiver)this.errReceiver);
/*  939 */     String name = Util.getRequiredAttribute(e, "name");
/*  940 */     service.setName(name);
/*      */     
/*  942 */     boolean gotDocumentation = false;
/*      */     
/*  944 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  945 */       Element e2 = Util.nextElement(iter);
/*  946 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*  949 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  950 */         if (gotDocumentation) {
/*  951 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */         }
/*  953 */         gotDocumentation = true;
/*  954 */         if (service.getDocumentation() == null)
/*  955 */           service.setDocumentation(getDocumentationFor(e2));  continue;
/*      */       } 
/*  957 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_PORT)) {
/*  958 */         Port port = parsePort(context, definitions, e2);
/*  959 */         service.add(port);
/*      */         continue;
/*      */       } 
/*  962 */       checkNotWsdlElement(e2);
/*  963 */       if (!handleExtension(context, (TWSDLExtensible)service, e2)) {
/*  964 */         checkNotWsdlRequired(e2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  969 */     context.pop();
/*  970 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_SERVICE, (Entity)service);
/*  971 */     return service;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Port parsePort(TWSDLParserContextImpl context, Definitions definitions, Element e) {
/*  978 */     context.push();
/*  979 */     context.registerNamespaces(e);
/*      */     
/*  981 */     Port port = new Port((Defining)definitions, this.forest.locatorTable.getStartLocation(e), (ErrorReceiver)this.errReceiver);
/*  982 */     String name = Util.getRequiredAttribute(e, "name");
/*  983 */     port.setName(name);
/*      */ 
/*      */     
/*  986 */     String bindingAttr = Util.getRequiredAttribute(e, "binding");
/*  987 */     port.setBinding(context.translateQualifiedName(context.getLocation(e), bindingAttr));
/*      */     
/*  989 */     boolean gotDocumentation = false;
/*      */     
/*  991 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/*  992 */       Element e2 = Util.nextElement(iter);
/*  993 */       if (e2 == null) {
/*      */         break;
/*      */       }
/*      */       
/*  997 */       if (XmlUtil.matchesTagNS(e2, WSDLConstants.QNAME_DOCUMENTATION)) {
/*  998 */         if (gotDocumentation) {
/*  999 */           this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_ONLY_ONE_DOCUMENTATION_ALLOWED(e.getLocalName()));
/*      */         }
/* 1001 */         gotDocumentation = true;
/* 1002 */         if (port.getDocumentation() == null) {
/* 1003 */           port.setDocumentation(getDocumentationFor(e2));
/*      */         }
/*      */         continue;
/*      */       } 
/* 1007 */       checkNotWsdlElement(e2);
/* 1008 */       if (!handleExtension(context, (TWSDLExtensible)port, e2)) {
/* 1009 */         checkNotWsdlRequired(e2);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1014 */     context.pop();
/* 1015 */     context.fireDoneParsingEntity(WSDLConstants.QNAME_PORT, (Entity)port);
/* 1016 */     return port;
/*      */   }
/*      */   
/*      */   private void validateSchemaImports(Element typesElement) {
/* 1020 */     for (Iterator iter = XmlUtil.getAllChildren(typesElement); iter.hasNext(); ) {
/* 1021 */       Element e = Util.nextElement(iter);
/* 1022 */       if (e == null) {
/*      */         break;
/*      */       }
/* 1025 */       if (XmlUtil.matchesTagNS(e, SchemaConstants.QNAME_IMPORT)) {
/* 1026 */         this.errReceiver.warning(this.forest.locatorTable.getStartLocation(e), WsdlMessages.WARNING_WSI_R_2003()); continue;
/*      */       } 
/* 1028 */       checkNotWsdlElement(e);
/*      */     } 
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
/*      */   private boolean handleExtension(TWSDLParserContextImpl context, TWSDLExtensible entity, Element e) {
/* 1043 */     TWSDLExtensionHandler h = (TWSDLExtensionHandler)this.extensionHandlers.get(e.getNamespaceURI());
/* 1044 */     if (h == null) {
/* 1045 */       context.fireIgnoringExtension(e, (Entity)entity);
/* 1046 */       this.errReceiver.warning(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_UNKNOWN_EXTENSIBILITY_ELEMENT_OR_ATTRIBUTE(e.getLocalName(), e.getNamespaceURI()));
/* 1047 */       return false;
/*      */     } 
/* 1049 */     return h.doHandleExtension((TWSDLParserContext)context, entity, e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handleExtension(TWSDLParserContextImpl context, TWSDLExtensible entity, Node n, Element e) {
/* 1059 */     TWSDLExtensionHandler h = (TWSDLExtensionHandler)this.extensionHandlers.get(n.getNamespaceURI());
/* 1060 */     if (h == null) {
/* 1061 */       context.fireIgnoringExtension(e, (Entity)entity);
/* 1062 */       this.errReceiver.warning(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_UNKNOWN_EXTENSIBILITY_ELEMENT_OR_ATTRIBUTE(n.getLocalName(), n.getNamespaceURI()));
/* 1063 */       return false;
/*      */     } 
/* 1065 */     return h.doHandleExtension((TWSDLParserContext)context, entity, e);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkNotWsdlElement(Element e) {
/* 1071 */     if (e.getNamespaceURI() != null && e.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/")) {
/* 1072 */       this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_INVALID_WSDL_ELEMENT(e.getTagName()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkNotWsdlAttribute(Attr a) {
/* 1078 */     if ("http://schemas.xmlsoap.org/wsdl/".equals(a.getNamespaceURI())) {
/* 1079 */       this.errReceiver.error(this.forest.locatorTable.getStartLocation(a.getOwnerElement()), WsdlMessages.PARSING_INVALID_WSDL_ELEMENT(a.getLocalName()));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkNotWsdlRequired(Element e) {
/* 1086 */     String required = XmlUtil.getAttributeNSOrNull(e, "required", "http://schemas.xmlsoap.org/wsdl/");
/*      */ 
/*      */ 
/*      */     
/* 1090 */     if (required != null && required.equals("true") && !this.options.isExtensionMode()) {
/* 1091 */       this.errReceiver.error(this.forest.locatorTable.getStartLocation(e), WsdlMessages.PARSING_REQUIRED_EXTENSIBILITY_ELEMENT(e.getTagName(), e
/* 1092 */             .getNamespaceURI()));
/*      */     }
/*      */   }
/*      */   
/*      */   private Documentation getDocumentationFor(Element e) {
/* 1097 */     String s = XmlUtil.getTextForNode(e);
/* 1098 */     if (s == null) {
/* 1099 */       return null;
/*      */     }
/* 1101 */     return new Documentation(s);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\WSDLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */