/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.xjc.util.DOMUtils;
/*     */ import com.sun.xml.internal.bind.v2.util.EditDistance;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import com.sun.xml.internal.ws.util.JAXWSUtils;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Internalizer
/*     */ {
/*  63 */   private final XPath xpath = ((XPathFactory)xpf.get()).newXPath();
/*     */   private final DOMForest forest;
/*     */   private final ErrorReceiver errorReceiver;
/*     */   
/*     */   public Internalizer(DOMForest forest, WsimportOptions options, ErrorReceiver errorReceiver) {
/*  68 */     this.forest = forest;
/*  69 */     this.errorReceiver = errorReceiver;
/*     */   }
/*     */   
/*     */   public void transform() {
/*  73 */     for (Element jaxwsBinding : this.forest.outerMostBindings) {
/*  74 */       internalize(jaxwsBinding, jaxwsBinding);
/*     */     }
/*     */   }
/*     */   
/*  78 */   private static final ContextClassloaderLocal<XPathFactory> xpf = new ContextClassloaderLocal<XPathFactory>()
/*     */     {
/*     */       protected XPathFactory initialValue() throws Exception {
/*  81 */         return XPathFactory.newInstance();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate(Element bindings) {
/*  88 */     NamedNodeMap atts = bindings.getAttributes();
/*  89 */     for (int i = 0; i < atts.getLength(); i++) {
/*  90 */       Attr a = (Attr)atts.item(i);
/*  91 */       if (a.getNamespaceURI() == null)
/*     */       {
/*     */         
/*  94 */         if (!a.getLocalName().equals("node"))
/*     */         {
/*     */           
/*  97 */           if (a.getLocalName().equals("wsdlLocation"));
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void internalize(Element bindings, Node inheritedTarget) {
/* 107 */     Node target = inheritedTarget;
/*     */     
/* 109 */     validate(bindings);
/*     */ 
/*     */     
/* 112 */     if (isTopLevelBinding(bindings)) {
/*     */       String wsdlLocation;
/* 114 */       if (bindings.getAttributeNode("wsdlLocation") != null) {
/* 115 */         wsdlLocation = bindings.getAttribute("wsdlLocation");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 122 */           wsdlLocation = (new URL(new URL(this.forest.getSystemId(bindings.getOwnerDocument())), wsdlLocation)).toExternalForm();
/* 123 */         } catch (MalformedURLException e) {
/* 124 */           wsdlLocation = JAXWSUtils.absolutize(JAXWSUtils.getFileOrURLName(wsdlLocation));
/*     */         } 
/*     */       } else {
/*     */         
/* 128 */         wsdlLocation = this.forest.getFirstRootDocument();
/*     */       } 
/* 130 */       target = this.forest.get(wsdlLocation);
/*     */       
/* 132 */       if (target == null) {
/* 133 */         reportError(bindings, WsdlMessages.INTERNALIZER_INCORRECT_SCHEMA_REFERENCE(wsdlLocation, EditDistance.findNearest(wsdlLocation, this.forest.listSystemIDs())));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 141 */     Element element = DOMUtil.getFirstElementChild(target);
/* 142 */     if (element != null && element.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/") && element.getLocalName().equals("definitions")) {
/*     */       
/* 144 */       Element type = DOMUtils.getFirstChildElement(element, "http://schemas.xmlsoap.org/wsdl/", "types");
/* 145 */       if (type != null) {
/* 146 */         for (Element schemaElement : DOMUtils.getChildElements(type, "http://www.w3.org/2001/XMLSchema", "schema")) {
/* 147 */           if (!schemaElement.hasAttributeNS("http://www.w3.org/2000/xmlns/", "jaxb")) {
/* 148 */             schemaElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:jaxb", "http://java.sun.com/xml/ns/jaxb");
/*     */           }
/*     */ 
/*     */           
/* 152 */           if (!schemaElement.hasAttributeNS("http://java.sun.com/xml/ns/jaxb", "version")) {
/* 153 */             schemaElement.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "jaxb:version", "2.0");
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 160 */     NodeList targetNodes = null;
/* 161 */     boolean hasNode = true;
/* 162 */     boolean isToplevelBinding = isTopLevelBinding(bindings);
/* 163 */     if ((isJAXWSBindings(bindings) || isJAXBBindings(bindings)) && bindings.getAttributeNode("node") != null) {
/* 164 */       targetNodes = evaluateXPathMultiNode(bindings, target, bindings.getAttribute("node"), new NamespaceContextImpl(bindings));
/*     */     }
/* 166 */     else if (isJAXWSBindings(bindings) && bindings.getAttributeNode("node") == null && !isToplevelBinding) {
/* 167 */       hasNode = false;
/*     */     }
/* 169 */     else if (isGlobalBinding(bindings) && !isWSDLDefinition(target) && isTopLevelBinding(bindings.getParentNode())) {
/* 170 */       targetNodes = getWSDLDefintionNode(bindings, target);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 175 */     if (targetNodes == null && hasNode && !isToplevelBinding) {
/*     */       return;
/*     */     }
/*     */     
/* 179 */     if (hasNode && 
/* 180 */       targetNodes != null) {
/* 181 */       for (int i = 0; i < targetNodes.getLength(); i++) {
/* 182 */         insertBinding(bindings, targetNodes.item(i));
/*     */         
/* 184 */         Element[] children = getChildElements(bindings);
/* 185 */         for (Element child : children) {
/* 186 */           if ("bindings".equals(child.getLocalName())) {
/* 187 */             internalize(child, targetNodes.item(i));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 193 */     if (targetNodes == null) {
/*     */       
/* 195 */       Element[] children = getChildElements(bindings);
/*     */       
/* 197 */       for (Element child : children) {
/* 198 */         internalize(child, target);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void insertBinding(@NotNull Element bindings, @NotNull Node target) {
/* 207 */     if ("bindings".equals(bindings.getLocalName())) {
/* 208 */       Element[] children = DOMUtils.getChildElements(bindings);
/* 209 */       for (Element item : children) {
/* 210 */         if (!"bindings".equals(item.getLocalName()))
/*     */         {
/*     */           
/* 213 */           moveUnder(item, (Element)target);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 218 */       moveUnder(bindings, (Element)target);
/*     */     } 
/*     */   }
/*     */   
/*     */   private NodeList getWSDLDefintionNode(Node bindings, Node target) {
/* 223 */     return evaluateXPathMultiNode(bindings, target, "wsdl:definitions", new NamespaceContext()
/*     */         {
/*     */           public String getNamespaceURI(String prefix)
/*     */           {
/* 227 */             return "http://schemas.xmlsoap.org/wsdl/";
/*     */           }
/*     */ 
/*     */           
/*     */           public String getPrefix(String nsURI) {
/* 232 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator getPrefixes(String namespaceURI) {
/* 237 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private boolean isWSDLDefinition(Node target) {
/* 243 */     if (target == null) {
/* 244 */       return false;
/*     */     }
/* 246 */     String localName = target.getLocalName();
/* 247 */     String nsURI = target.getNamespaceURI();
/* 248 */     return (fixNull(localName).equals("definitions") && fixNull(nsURI).equals("http://schemas.xmlsoap.org/wsdl/"));
/*     */   }
/*     */   
/*     */   private boolean isTopLevelBinding(Node node) {
/* 252 */     return (node.getOwnerDocument().getDocumentElement() == node);
/*     */   }
/*     */   
/*     */   private boolean isJAXWSBindings(Node bindings) {
/* 256 */     return (bindings.getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxws") && bindings.getLocalName().equals("bindings"));
/*     */   }
/*     */   
/*     */   private boolean isJAXBBindings(Node bindings) {
/* 260 */     return (bindings.getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb") && bindings.getLocalName().equals("bindings"));
/*     */   }
/*     */   
/*     */   private boolean isGlobalBinding(Node bindings) {
/* 264 */     if (bindings.getNamespaceURI() == null) {
/* 265 */       this.errorReceiver.warning(this.forest.locatorTable.getStartLocation((Element)bindings), WsdlMessages.INVALID_CUSTOMIZATION_NAMESPACE(bindings.getLocalName()));
/* 266 */       return false;
/*     */     } 
/* 268 */     return (bindings.getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxws") && (bindings
/* 269 */       .getLocalName().equals("package") || bindings
/* 270 */       .getLocalName().equals("enableAsyncMapping") || bindings
/* 271 */       .getLocalName().equals("enableAdditionalSOAPHeaderMapping") || bindings
/* 272 */       .getLocalName().equals("enableWrapperStyle") || bindings
/* 273 */       .getLocalName().equals("enableMIMEContent")));
/*     */   }
/*     */   
/*     */   private static Element[] getChildElements(Element parent) {
/* 277 */     ArrayList<Element> a = new ArrayList<>();
/* 278 */     NodeList children = parent.getChildNodes();
/* 279 */     for (int i = 0; i < children.getLength(); i++) {
/* 280 */       Node item = children.item(i);
/* 281 */       if (item instanceof Element)
/*     */       {
/*     */         
/* 284 */         if ("http://java.sun.com/xml/ns/jaxws".equals(item.getNamespaceURI()) || "http://java.sun.com/xml/ns/jaxb"
/* 285 */           .equals(item.getNamespaceURI()))
/* 286 */           a.add((Element)item); 
/*     */       }
/*     */     } 
/* 289 */     return a.<Element>toArray(new Element[a.size()]);
/*     */   }
/*     */   
/*     */   private NodeList evaluateXPathMultiNode(Node bindings, Node target, String expression, NamespaceContext namespaceContext) {
/*     */     NodeList nlst;
/*     */     try {
/* 295 */       this.xpath.setNamespaceContext(namespaceContext);
/* 296 */       nlst = (NodeList)this.xpath.evaluate(expression, target, XPathConstants.NODESET);
/* 297 */     } catch (XPathExpressionException e) {
/* 298 */       reportError((Element)bindings, WsdlMessages.INTERNALIZER_X_PATH_EVALUATION_ERROR(e.getMessage()), e);
/* 299 */       return null;
/*     */     } 
/*     */     
/* 302 */     if (nlst.getLength() == 0) {
/* 303 */       reportError((Element)bindings, WsdlMessages.INTERNALIZER_X_PATH_EVALUATES_TO_NO_TARGET(expression));
/* 304 */       return null;
/*     */     } 
/*     */     
/* 307 */     return nlst;
/*     */   }
/*     */   
/*     */   private boolean isJAXBBindingElement(Element e) {
/* 311 */     return fixNull(e.getNamespaceURI()).equals("http://java.sun.com/xml/ns/jaxb");
/*     */   }
/*     */   
/*     */   private boolean isJAXWSBindingElement(Element e) {
/* 315 */     return fixNull(e.getNamespaceURI()).equals("http://java.sun.com/xml/ns/jaxws");
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
/*     */   private void moveUnder(Element decl, Element target) {
/* 329 */     if (isJAXBBindingElement(decl)) {
/*     */       
/* 331 */       if (!target.hasAttributeNS("http://www.w3.org/2000/xmlns/", "jaxb")) {
/* 332 */         target.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:jaxb", "http://java.sun.com/xml/ns/jaxb");
/*     */       }
/*     */ 
/*     */       
/* 336 */       if (!target.hasAttributeNS("http://java.sun.com/xml/ns/jaxb", "version")) {
/* 337 */         target.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "jaxb:version", "2.0");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 348 */       if (target.getLocalName().equals("schema") && target.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema") && !target.hasAttributeNS("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes")) {
/* 349 */         target.setAttributeNS("http://java.sun.com/xml/ns/jaxb", "jaxb:extensionBindingPrefixes", "xjc");
/* 350 */         target.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xjc", "http://java.sun.com/xml/ns/jaxb/xjc");
/*     */       } 
/*     */ 
/*     */       
/* 354 */       target = refineSchemaTarget(target);
/* 355 */       copyInscopeNSAttributes(decl);
/* 356 */     } else if (isJAXWSBindingElement(decl)) {
/*     */       
/* 358 */       if (!target.hasAttributeNS("http://www.w3.org/2000/xmlns/", "JAXWS")) {
/* 359 */         target.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:JAXWS", "http://java.sun.com/xml/ns/jaxws");
/*     */       }
/*     */ 
/*     */       
/* 363 */       target = refineWSDLTarget(target);
/* 364 */       copyInscopeNSAttributes(decl);
/*     */     } else {
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 370 */     if (target.getOwnerDocument() != decl.getOwnerDocument())
/*     */     {
/* 372 */       decl = (Element)target.getOwnerDocument().importNode(decl, true);
/*     */     }
/*     */ 
/*     */     
/* 376 */     target.appendChild(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyInscopeNSAttributes(Element e) {
/* 385 */     Element p = e;
/* 386 */     Set<String> inscopes = new HashSet<>();
/*     */     while (true) {
/* 388 */       NamedNodeMap atts = p.getAttributes();
/* 389 */       for (int i = 0; i < atts.getLength(); i++) {
/* 390 */         Attr a = (Attr)atts.item(i);
/* 391 */         if ("http://www.w3.org/2000/xmlns/".equals(a.getNamespaceURI())) {
/*     */           String prefix;
/* 393 */           if (a.getName().indexOf(':') == -1) {
/* 394 */             prefix = "";
/*     */           } else {
/* 396 */             prefix = a.getLocalName();
/*     */           } 
/*     */           
/* 399 */           if (inscopes.add(prefix) && p != e)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 405 */             e.setAttributeNodeNS((Attr)a.cloneNode(true));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 410 */       if (p.getParentNode() instanceof org.w3c.dom.Document) {
/*     */         break;
/*     */       }
/*     */       
/* 414 */       p = (Element)p.getParentNode();
/*     */     } 
/*     */     
/* 417 */     if (!inscopes.contains(""))
/*     */     {
/*     */ 
/*     */       
/* 421 */       e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Element refineSchemaTarget(Element target) {
/* 427 */     Element annotation = DOMUtils.getFirstChildElement(target, "http://www.w3.org/2001/XMLSchema", "annotation");
/* 428 */     if (annotation == null)
/*     */     {
/* 430 */       annotation = insertXMLSchemaElement(target, "annotation");
/*     */     }
/*     */ 
/*     */     
/* 434 */     Element appinfo = DOMUtils.getFirstChildElement(annotation, "http://www.w3.org/2001/XMLSchema", "appinfo");
/* 435 */     if (appinfo == null)
/*     */     {
/* 437 */       appinfo = insertXMLSchemaElement(annotation, "appinfo");
/*     */     }
/*     */     
/* 440 */     return appinfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public Element refineWSDLTarget(Element target) {
/* 445 */     Element JAXWSBindings = DOMUtils.getFirstChildElement(target, "http://java.sun.com/xml/ns/jaxws", "bindings");
/* 446 */     if (JAXWSBindings == null)
/*     */     {
/* 448 */       JAXWSBindings = insertJAXWSBindingsElement(target, "bindings");
/*     */     }
/* 450 */     return JAXWSBindings;
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
/*     */   private Element insertXMLSchemaElement(Element parent, String localName) {
/* 462 */     String qname = parent.getTagName();
/* 463 */     int idx = qname.indexOf(':');
/* 464 */     if (idx == -1) {
/* 465 */       qname = localName;
/*     */     } else {
/* 467 */       qname = qname.substring(0, idx + 1) + localName;
/*     */     } 
/*     */     
/* 470 */     Element child = parent.getOwnerDocument().createElementNS("http://www.w3.org/2001/XMLSchema", qname);
/*     */     
/* 472 */     NodeList children = parent.getChildNodes();
/*     */     
/* 474 */     if (children.getLength() == 0) {
/* 475 */       parent.appendChild(child);
/*     */     } else {
/* 477 */       parent.insertBefore(child, children.item(0));
/*     */     } 
/*     */     
/* 480 */     return child;
/*     */   }
/*     */   
/*     */   private Element insertJAXWSBindingsElement(Element parent, String localName) {
/* 484 */     String qname = "JAXWS:" + localName;
/*     */     
/* 486 */     Element child = parent.getOwnerDocument().createElementNS("http://java.sun.com/xml/ns/jaxws", qname);
/*     */     
/* 488 */     NodeList children = parent.getChildNodes();
/*     */     
/* 490 */     if (children.getLength() == 0) {
/* 491 */       parent.appendChild(child);
/*     */     } else {
/* 493 */       parent.insertBefore(child, children.item(0));
/*     */     } 
/*     */     
/* 496 */     return child;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   static String fixNull(@Nullable String s) {
/* 501 */     if (s == null) {
/* 502 */       return "";
/*     */     }
/* 504 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg) {
/* 509 */     reportError(errorSource, formattedMsg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg, Exception nestedException) {
/* 516 */     SAXParseException e = new SAXParseException2(formattedMsg, this.forest.locatorTable.getStartLocation(errorSource), nestedException);
/*     */     
/* 518 */     this.errorReceiver.error(e);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\Internalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */