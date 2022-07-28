/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.util.DOMUtils;
/*     */ import com.sun.xml.internal.bind.v2.util.EditDistance;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import com.sun.xml.internal.xsom.SCD;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
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
/*     */ class Internalizer
/*     */ {
/*     */   private static final String WSDL_NS = "http://schemas.xmlsoap.org/wsdl/";
/*     */   private final XPath xpath;
/*     */   private final DOMForest forest;
/*     */   private ErrorReceiver errorHandler;
/*     */   private boolean enableSCD;
/*     */   private static final String EXTENSION_PREFIXES = "extensionBindingPrefixes";
/*     */   
/*     */   static SCDBasedBindingSet transform(DOMForest forest, boolean enableSCD, boolean disableSecureProcessing) {
/*  92 */     return (new Internalizer(forest, enableSCD, disableSecureProcessing)).transform();
/*     */   }
/*     */ 
/*     */   
/*     */   private Internalizer(DOMForest forest, boolean enableSCD, boolean disableSecureProcessing) {
/*  97 */     this.errorHandler = forest.getErrorHandler();
/*  98 */     this.forest = forest;
/*  99 */     this.enableSCD = enableSCD;
/* 100 */     this.xpath = XmlFactory.createXPathFactory(disableSecureProcessing).newXPath();
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
/*     */   private SCDBasedBindingSet transform() {
/* 122 */     Map<Element, List<Node>> targetNodes = new HashMap<>();
/*     */     
/* 124 */     SCDBasedBindingSet scd = new SCDBasedBindingSet(this.forest);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     for (Element jaxbBindings : this.forest.outerMostBindings)
/*     */     {
/* 131 */       buildTargetNodeMap(jaxbBindings, jaxbBindings, null, targetNodes, scd);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     for (Element jaxbBindings : this.forest.outerMostBindings) {
/* 138 */       move(jaxbBindings, targetNodes);
/*     */     }
/*     */     
/* 141 */     return scd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate(Element bindings) {
/* 148 */     NamedNodeMap atts = bindings.getAttributes();
/* 149 */     for (int i = 0; i < atts.getLength(); i++) {
/* 150 */       Attr a = (Attr)atts.item(i);
/* 151 */       if (a.getNamespaceURI() == null)
/*     */       {
/* 153 */         if (!a.getLocalName().equals("node"))
/*     */         {
/* 155 */           if (!a.getLocalName().equals("schemaLocation"))
/*     */           {
/* 157 */             if (!a.getLocalName().equals("scd"))
/*     */             {
/*     */ 
/*     */               
/* 161 */               if (!a.getLocalName().equals("required"))
/*     */               {
/* 163 */                 if (a.getLocalName().equals("multiple"));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildTargetNodeMap(Element bindings, @NotNull Node inheritedTarget, @Nullable SCDBasedBindingSet.Target inheritedSCD, Map<Element, List<Node>> result, SCDBasedBindingSet scdResult) {
/* 188 */     Node target = inheritedTarget;
/* 189 */     ArrayList<Node> targetMultiple = null;
/*     */     
/* 191 */     validate(bindings);
/*     */     
/* 193 */     boolean required = true;
/* 194 */     boolean multiple = false;
/*     */     
/* 196 */     if (bindings.getAttribute("required") != null) {
/* 197 */       String requiredAttr = bindings.getAttribute("required");
/*     */       
/* 199 */       if (requiredAttr.equals("no") || requiredAttr.equals("false") || requiredAttr.equals("0")) {
/* 200 */         required = false;
/*     */       }
/*     */     } 
/* 203 */     if (bindings.getAttribute("multiple") != null) {
/* 204 */       String requiredAttr = bindings.getAttribute("multiple");
/*     */       
/* 206 */       if (requiredAttr.equals("yes") || requiredAttr.equals("true") || requiredAttr.equals("1")) {
/* 207 */         multiple = true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 212 */     if (bindings.getAttributeNode("schemaLocation") != null) {
/* 213 */       String schemaLocation = bindings.getAttribute("schemaLocation");
/*     */ 
/*     */       
/* 216 */       if (schemaLocation.equals("*")) {
/* 217 */         for (String systemId : this.forest.listSystemIDs()) {
/* 218 */           if (result.get(bindings) == null)
/* 219 */             result.put(bindings, new ArrayList<>()); 
/* 220 */           ((List<Element>)result.get(bindings)).add(this.forest.get(systemId).getDocumentElement());
/*     */           
/* 222 */           Element[] arrayOfElement = DOMUtils.getChildElements(bindings, "http://java.sun.com/xml/ns/jaxb", "bindings");
/* 223 */           for (Element value : arrayOfElement) {
/* 224 */             buildTargetNodeMap(value, this.forest.get(systemId).getDocumentElement(), inheritedSCD, result, scdResult);
/*     */           }
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 232 */       try { URL loc = new URL(new URL(this.forest.getSystemId(bindings.getOwnerDocument())), schemaLocation);
/*     */         
/* 234 */         schemaLocation = loc.toExternalForm();
/* 235 */         target = this.forest.get(schemaLocation);
/* 236 */         if (target == null && loc.getProtocol().startsWith("file")) {
/* 237 */           File f = new File(loc.getFile());
/* 238 */           schemaLocation = (new File(f.getCanonicalPath())).toURI().toString();
/*     */         }  }
/* 240 */       catch (MalformedURLException malformedURLException) {  }
/* 241 */       catch (IOException e)
/* 242 */       { Logger.getLogger(Internalizer.class.getName()).log(Level.FINEST, e.getLocalizedMessage()); }
/*     */ 
/*     */       
/* 245 */       target = this.forest.get(schemaLocation);
/* 246 */       if (target == null) {
/* 247 */         reportError(bindings, 
/* 248 */             Messages.format("Internalizer.IncorrectSchemaReference", new Object[] {
/*     */                 
/* 250 */                 schemaLocation, EditDistance.findNearest(schemaLocation, this.forest.listSystemIDs())
/*     */               }));
/*     */         
/*     */         return;
/*     */       } 
/* 255 */       target = ((Document)target).getDocumentElement();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 260 */     if (bindings.getAttributeNode("node") != null) {
/* 261 */       NodeList nlst; String nodeXPath = bindings.getAttribute("node");
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 266 */         this.xpath.setNamespaceContext(new NamespaceContextImpl(bindings));
/* 267 */         nlst = (NodeList)this.xpath.evaluate(nodeXPath, target, XPathConstants.NODESET);
/* 268 */       } catch (XPathExpressionException e) {
/* 269 */         if (required) {
/* 270 */           reportError(bindings, 
/* 271 */               Messages.format("Internalizer.XPathEvaluationError", new Object[] { e.getMessage() }), e);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 278 */       if (nlst.getLength() == 0) {
/* 279 */         if (required) {
/* 280 */           reportError(bindings, 
/* 281 */               Messages.format("Internalizer.XPathEvaluatesToNoTarget", new Object[] { nodeXPath }));
/*     */         }
/*     */         return;
/*     */       } 
/* 285 */       if (nlst.getLength() != 1) {
/* 286 */         if (!multiple) {
/* 287 */           reportError(bindings, 
/* 288 */               Messages.format("Internalizer.XPathEvaulatesToTooManyTargets", new Object[] { nodeXPath, Integer.valueOf(nlst.getLength()) }));
/*     */           
/*     */           return;
/*     */         } 
/* 292 */         if (targetMultiple == null) targetMultiple = new ArrayList<>(); 
/* 293 */         for (int i = 0; i < nlst.getLength(); i++) {
/* 294 */           targetMultiple.add(nlst.item(i));
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 300 */       if (!multiple || nlst.getLength() == 1) {
/* 301 */         Node rnode = nlst.item(0);
/* 302 */         if (!(rnode instanceof Element)) {
/* 303 */           reportError(bindings, 
/* 304 */               Messages.format("Internalizer.XPathEvaluatesToNonElement", new Object[] { nodeXPath }));
/*     */           
/*     */           return;
/*     */         } 
/* 308 */         if (!this.forest.logic.checkIfValidTargetNode(this.forest, bindings, (Element)rnode)) {
/* 309 */           reportError(bindings, 
/* 310 */               Messages.format("Internalizer.XPathEvaluatesToNonSchemaElement", new Object[] {
/* 311 */                   nodeXPath, rnode.getNodeName()
/*     */                 }));
/*     */           return;
/*     */         } 
/* 315 */         target = rnode;
/*     */       } else {
/* 317 */         for (Node rnode : targetMultiple) {
/* 318 */           if (!(rnode instanceof Element)) {
/* 319 */             reportError(bindings, 
/* 320 */                 Messages.format("Internalizer.XPathEvaluatesToNonElement", new Object[] { nodeXPath }));
/*     */             
/*     */             return;
/*     */           } 
/* 324 */           if (!this.forest.logic.checkIfValidTargetNode(this.forest, bindings, (Element)rnode)) {
/* 325 */             reportError(bindings, 
/* 326 */                 Messages.format("Internalizer.XPathEvaluatesToNonSchemaElement", new Object[] {
/* 327 */                     nodeXPath, rnode.getNodeName()
/*     */                   }));
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 335 */     if (bindings.getAttributeNode("scd") != null) {
/* 336 */       String scdPath = bindings.getAttribute("scd");
/* 337 */       if (!this.enableSCD) {
/*     */ 
/*     */         
/* 340 */         reportError(bindings, 
/* 341 */             Messages.format("SCD_NOT_ENABLED", new Object[0]));
/* 342 */         this.enableSCD = true;
/*     */       } 
/*     */       
/*     */       try {
/* 346 */         inheritedSCD = scdResult.createNewTarget(inheritedSCD, bindings, 
/* 347 */             SCD.create(scdPath, new NamespaceContextImpl(bindings)));
/* 348 */       } catch (ParseException e) {
/* 349 */         NodeList nlst; reportError(bindings, Messages.format("ERR_SCD_EVAL", new Object[] { nlst.getMessage() }), (Exception)nlst);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 355 */     if (inheritedSCD != null) {
/* 356 */       inheritedSCD.addBinidng(bindings);
/* 357 */     } else if (!multiple || targetMultiple == null) {
/* 358 */       if (result.get(bindings) == null)
/* 359 */         result.put(bindings, new ArrayList<>()); 
/* 360 */       ((List<Node>)result.get(bindings)).add(target);
/*     */     } else {
/* 362 */       for (Node rnode : targetMultiple) {
/* 363 */         if (result.get(bindings) == null) {
/* 364 */           result.put(bindings, new ArrayList<>());
/*     */         }
/* 366 */         ((List<Node>)result.get(bindings)).add(rnode);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 373 */     Element[] children = DOMUtils.getChildElements(bindings, "http://java.sun.com/xml/ns/jaxb", "bindings");
/* 374 */     for (Element value : children) {
/* 375 */       if (!multiple || targetMultiple == null) {
/* 376 */         buildTargetNodeMap(value, target, inheritedSCD, result, scdResult);
/*     */       } else {
/* 378 */         for (Node rnode : targetMultiple) {
/* 379 */           buildTargetNodeMap(value, rnode, inheritedSCD, result, scdResult);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void move(Element bindings, Map<Element, List<Node>> targetNodes) {
/* 388 */     List<Node> nodelist = targetNodes.get(bindings);
/*     */     
/* 390 */     if (nodelist == null) {
/*     */       return;
/*     */     }
/*     */     
/* 394 */     for (Node target : nodelist) {
/* 395 */       if (target == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 401 */       for (Element item : DOMUtils.getChildElements(bindings)) {
/* 402 */         String localName = item.getLocalName();
/*     */         
/* 404 */         if ("bindings".equals(localName)) {
/*     */           
/* 406 */           move(item, targetNodes);
/* 407 */         } else if ("globalBindings".equals(localName)) {
/*     */           
/* 409 */           Element root = this.forest.getOneDocument().getDocumentElement();
/* 410 */           if (root.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/")) {
/* 411 */             NodeList elements = root.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 412 */             if (elements == null || elements.getLength() < 1) {
/* 413 */               reportError(item, Messages.format("Internalizer.OrphanedCustomization", new Object[] { item.getNodeName() }));
/*     */               return;
/*     */             } 
/* 416 */             moveUnder(item, (Element)elements.item(0));
/*     */           } else {
/*     */             
/* 419 */             moveUnder(item, root);
/*     */           } 
/*     */         } else {
/* 422 */           if (!(target instanceof Element)) {
/* 423 */             reportError(item, 
/* 424 */                 Messages.format("Internalizer.ContextNodeIsNotElement", new Object[0]));
/*     */             
/*     */             return;
/*     */           } 
/* 428 */           if (!this.forest.logic.checkIfValidTargetNode(this.forest, item, (Element)target)) {
/* 429 */             reportError(item, 
/* 430 */                 Messages.format("Internalizer.OrphanedCustomization", new Object[] { item.getNodeName() }));
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 435 */           moveUnder(item, (Element)target);
/*     */         } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void moveUnder(Element decl, Element target) {
/* 452 */     Element realTarget = this.forest.logic.refineTarget(target);
/*     */     
/* 454 */     declExtensionNamespace(decl, target);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 459 */     Element p = decl;
/* 460 */     Set<String> inscopes = new HashSet<>();
/*     */     while (true) {
/* 462 */       NamedNodeMap atts = p.getAttributes();
/* 463 */       for (int i = 0; i < atts.getLength(); i++) {
/* 464 */         Attr a = (Attr)atts.item(i);
/* 465 */         if ("http://www.w3.org/2000/xmlns/".equals(a.getNamespaceURI())) {
/*     */           String prefix;
/* 467 */           if (a.getName().indexOf(':') == -1) { prefix = ""; }
/* 468 */           else { prefix = a.getLocalName(); }
/*     */           
/* 470 */           if (inscopes.add(prefix) && p != decl)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 476 */             decl.setAttributeNodeNS((Attr)a.cloneNode(true));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 481 */       if (p.getParentNode() instanceof Document) {
/*     */         break;
/*     */       }
/* 484 */       p = (Element)p.getParentNode();
/*     */     } 
/*     */     
/* 487 */     if (!inscopes.contains(""))
/*     */     {
/*     */ 
/*     */       
/* 491 */       decl.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 496 */     if (realTarget.getOwnerDocument() != decl.getOwnerDocument()) {
/*     */       
/* 498 */       Element original = decl;
/* 499 */       decl = (Element)realTarget.getOwnerDocument().importNode(decl, true);
/*     */ 
/*     */       
/* 502 */       copyLocators(original, decl);
/*     */     } 
/*     */     
/* 505 */     realTarget.appendChild(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void declExtensionNamespace(Element decl, Element target) {
/* 516 */     if (!"http://java.sun.com/xml/ns/jaxb".equals(decl.getNamespaceURI())) {
/* 517 */       declareExtensionNamespace(target, decl.getNamespaceURI());
/*     */     }
/* 519 */     NodeList lst = decl.getChildNodes();
/* 520 */     for (int i = 0; i < lst.getLength(); i++) {
/* 521 */       Node n = lst.item(i);
/* 522 */       if (n instanceof Element) {
/* 523 */         declExtensionNamespace((Element)n, target);
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
/*     */   
/*     */   private void declareExtensionNamespace(Element target, String nsUri) {
/* 537 */     Element root = target.getOwnerDocument().getDocumentElement();
/* 538 */     Attr att = root.getAttributeNodeNS("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes");
/* 539 */     if (att == null) {
/* 540 */       String jaxbPrefix = allocatePrefix(root, "http://java.sun.com/xml/ns/jaxb");
/*     */       
/* 542 */       att = target.getOwnerDocument().createAttributeNS("http://java.sun.com/xml/ns/jaxb", jaxbPrefix + ':' + "extensionBindingPrefixes");
/*     */       
/* 544 */       root.setAttributeNodeNS(att);
/*     */     } 
/*     */     
/* 547 */     String prefix = allocatePrefix(root, nsUri);
/* 548 */     if (att.getValue().indexOf(prefix) == -1)
/*     */     {
/* 550 */       att.setValue(att.getValue() + ' ' + prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String allocatePrefix(Element e, String nsUri) {
/*     */     String prefix;
/* 562 */     NamedNodeMap atts = e.getAttributes();
/* 563 */     for (int i = 0; i < atts.getLength(); i++) {
/* 564 */       Attr a = (Attr)atts.item(i);
/* 565 */       if ("http://www.w3.org/2000/xmlns/".equals(a.getNamespaceURI()) && 
/* 566 */         a.getName().indexOf(':') != -1)
/*     */       {
/* 568 */         if (a.getValue().equals(nsUri)) {
/* 569 */           return a.getLocalName();
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/*     */     while (true) {
/* 575 */       prefix = "p" + (int)(Math.random() * 1000000.0D) + '_';
/* 576 */       if (e.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", prefix) != null)
/*     */         continue;  break;
/*     */     } 
/* 579 */     e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, nsUri);
/* 580 */     return prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyLocators(Element src, Element dst) {
/* 589 */     this.forest.locatorTable.storeStartLocation(dst, this.forest.locatorTable
/* 590 */         .getStartLocation(src));
/* 591 */     this.forest.locatorTable.storeEndLocation(dst, this.forest.locatorTable
/* 592 */         .getEndLocation(src));
/*     */ 
/*     */     
/* 595 */     Element[] srcChilds = DOMUtils.getChildElements(src);
/* 596 */     Element[] dstChilds = DOMUtils.getChildElements(dst);
/*     */     
/* 598 */     for (int i = 0; i < srcChilds.length; i++) {
/* 599 */       copyLocators(srcChilds[i], dstChilds[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg) {
/* 604 */     reportError(errorSource, formattedMsg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg, Exception nestedException) {
/* 611 */     SAXParseException e = new SAXParseException2(formattedMsg, this.forest.locatorTable.getStartLocation(errorSource), nestedException);
/*     */     
/* 613 */     this.errorHandler.error(e);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\Internalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */