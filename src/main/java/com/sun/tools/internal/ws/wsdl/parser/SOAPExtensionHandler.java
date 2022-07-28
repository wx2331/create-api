/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPAddress;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBody;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPConstants;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPFault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPHeader;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPHeaderFault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPStyle;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.TWSDLParserContextImpl;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
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
/*     */ public class SOAPExtensionHandler
/*     */   extends AbstractExtensionHandler
/*     */ {
/*     */   public SOAPExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap) {
/*  48 */     super(extensionHandlerMap);
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/*  52 */     return "http://schemas.xmlsoap.org/wsdl/soap/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleDefinitionsExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  59 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/*  61 */         .getTagName(), e
/*  62 */         .getNamespaceURI());
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleTypesExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  70 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/*  72 */         .getTagName(), e
/*  73 */         .getNamespaceURI());
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   protected SOAPBinding getSOAPBinding(Locator location) {
/*  78 */     return new SOAPBinding(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleBindingExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  85 */     if (XmlUtil.matchesTagNS(e, getBindingQName())) {
/*  86 */       context.push();
/*  87 */       context.registerNamespaces(e);
/*     */       
/*  89 */       SOAPBinding binding = getSOAPBinding(context.getLocation(e));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       String transport = Util.getRequiredAttribute(e, "transport");
/*  95 */       binding.setTransport(transport);
/*     */       
/*  97 */       String style = XmlUtil.getAttributeOrNull(e, "style");
/*  98 */       if (style != null) {
/*  99 */         if (style.equals("rpc")) {
/* 100 */           binding.setStyle(SOAPStyle.RPC);
/* 101 */         } else if (style.equals("document")) {
/* 102 */           binding.setStyle(SOAPStyle.DOCUMENT);
/*     */         } else {
/* 104 */           Util.fail("parsing.invalidAttributeValue", "style", style);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 110 */       parent.addExtension((TWSDLExtension)binding);
/* 111 */       context.pop();
/*     */       
/* 113 */       return true;
/*     */     } 
/* 115 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 117 */         .getTagName(), e
/* 118 */         .getNamespaceURI());
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleOperationExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 127 */     if (XmlUtil.matchesTagNS(e, getOperationQName())) {
/* 128 */       context.push();
/* 129 */       context.registerNamespaces(e);
/*     */       
/* 131 */       SOAPOperation operation = new SOAPOperation(context.getLocation(e));
/*     */ 
/*     */       
/* 134 */       String soapAction = XmlUtil.getAttributeOrNull(e, "soapAction");
/* 135 */       if (soapAction != null) {
/* 136 */         operation.setSOAPAction(soapAction);
/*     */       }
/*     */       
/* 139 */       String style = XmlUtil.getAttributeOrNull(e, "style");
/* 140 */       if (style != null) {
/* 141 */         if (style.equals("rpc")) {
/* 142 */           operation.setStyle(SOAPStyle.RPC);
/* 143 */         } else if (style.equals("document")) {
/* 144 */           operation.setStyle(SOAPStyle.DOCUMENT);
/*     */         } else {
/* 146 */           Util.fail("parsing.invalidAttributeValue", "style", style);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 152 */       parent.addExtension((TWSDLExtension)operation);
/* 153 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 157 */       return true;
/*     */     } 
/* 159 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 161 */         .getTagName(), e
/* 162 */         .getNamespaceURI());
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleInputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 171 */     return handleInputOutputExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleOutputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 177 */     return handleInputOutputExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleMIMEPartExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 185 */     return handleInputOutputExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleInputOutputExtension(TWSDLParserContext contextif, TWSDLExtensible parent, Element e) {
/* 192 */     TWSDLParserContextImpl context = (TWSDLParserContextImpl)contextif;
/* 193 */     if (XmlUtil.matchesTagNS(e, getBodyQName())) {
/* 194 */       context.push();
/* 195 */       context.registerNamespaces(e);
/*     */       
/* 197 */       SOAPBody body = new SOAPBody(context.getLocation(e));
/*     */       
/* 199 */       String use = XmlUtil.getAttributeOrNull(e, "use");
/* 200 */       if (use != null) {
/* 201 */         if (use.equals("literal")) {
/* 202 */           body.setUse(SOAPUse.LITERAL);
/* 203 */         } else if (use.equals("encoded")) {
/* 204 */           body.setUse(SOAPUse.ENCODED);
/*     */         } else {
/* 206 */           Util.fail("parsing.invalidAttributeValue", "use", use);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 214 */       String namespace = XmlUtil.getAttributeOrNull(e, "namespace");
/* 215 */       if (namespace != null) {
/* 216 */         body.setNamespace(namespace);
/*     */       }
/*     */ 
/*     */       
/* 220 */       String encodingStyle = XmlUtil.getAttributeOrNull(e, "encodingStyle");
/* 221 */       if (encodingStyle != null) {
/* 222 */         body.setEncodingStyle(encodingStyle);
/*     */       }
/*     */       
/* 225 */       String parts = XmlUtil.getAttributeOrNull(e, "parts");
/* 226 */       if (parts != null) {
/* 227 */         body.setParts(parts);
/*     */       }
/*     */       
/* 230 */       parent.addExtension((TWSDLExtension)body);
/* 231 */       context.pop();
/*     */       
/* 233 */       return true;
/* 234 */     }  if (XmlUtil.matchesTagNS(e, getHeaderQName())) {
/* 235 */       return handleHeaderElement(parent, e, context);
/*     */     }
/* 237 */     Util.fail("parsing.invalidExtensionElement", e.getTagName(), e.getNamespaceURI());
/* 238 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean handleHeaderElement(TWSDLExtensible parent, Element e, TWSDLParserContextImpl context) {
/* 243 */     context.push();
/* 244 */     context.registerNamespaces(e);
/*     */     
/* 246 */     SOAPHeader header = new SOAPHeader(context.getLocation(e));
/*     */     
/* 248 */     String use = XmlUtil.getAttributeOrNull(e, "use");
/* 249 */     if (use != null) {
/* 250 */       if (use.equals("literal")) {
/* 251 */         header.setUse(SOAPUse.LITERAL);
/* 252 */       } else if (use.equals("encoded")) {
/* 253 */         header.setUse(SOAPUse.ENCODED);
/*     */       } else {
/* 255 */         Util.fail("parsing.invalidAttributeValue", "use", use);
/*     */       } 
/*     */     }
/*     */     
/* 259 */     String namespace = XmlUtil.getAttributeOrNull(e, "namespace");
/* 260 */     if (namespace != null) {
/* 261 */       header.setNamespace(namespace);
/*     */     }
/*     */     
/* 264 */     String encodingStyle = XmlUtil.getAttributeOrNull(e, "encodingStyle");
/* 265 */     if (encodingStyle != null) {
/* 266 */       header.setEncodingStyle(encodingStyle);
/*     */     }
/*     */     
/* 269 */     String part = XmlUtil.getAttributeOrNull(e, "part");
/* 270 */     if (part != null) {
/* 271 */       header.setPart(part);
/*     */     }
/*     */     
/* 274 */     String messageAttr = XmlUtil.getAttributeOrNull(e, "message");
/* 275 */     if (messageAttr != null) {
/* 276 */       header.setMessage(context.translateQualifiedName(context.getLocation(e), messageAttr));
/*     */     }
/*     */     
/* 279 */     for (Iterator iter = XmlUtil.getAllChildren(e); iter.hasNext(); ) {
/* 280 */       Element e2 = Util.nextElement(iter);
/* 281 */       if (e2 == null) {
/*     */         break;
/*     */       }
/* 284 */       if (XmlUtil.matchesTagNS(e2, getHeaderfaultQName())) {
/* 285 */         handleHeaderFaultElement(e, context, header, use, e2); continue;
/*     */       } 
/* 287 */       Util.fail("parsing.invalidElement", e2.getTagName(), e2.getNamespaceURI());
/*     */     } 
/*     */ 
/*     */     
/* 291 */     parent.addExtension((TWSDLExtension)header);
/* 292 */     context.pop();
/* 293 */     context.fireDoneParsingEntity(getHeaderQName(), (Entity)header);
/* 294 */     return true;
/*     */   }
/*     */   
/*     */   private void handleHeaderFaultElement(Element e, TWSDLParserContextImpl context, SOAPHeader header, String use, Element e2) {
/* 298 */     context.push();
/* 299 */     context.registerNamespaces(e);
/*     */     
/* 301 */     SOAPHeaderFault headerfault = new SOAPHeaderFault(context.getLocation(e));
/*     */     
/* 303 */     String use2 = XmlUtil.getAttributeOrNull(e2, "use");
/* 304 */     if (use2 != null) {
/* 305 */       if (use2.equals("literal")) {
/* 306 */         headerfault.setUse(SOAPUse.LITERAL);
/* 307 */       } else if (use.equals("encoded")) {
/* 308 */         headerfault.setUse(SOAPUse.ENCODED);
/*     */       } else {
/* 310 */         Util.fail("parsing.invalidAttributeValue", "use", use2);
/*     */       } 
/*     */     }
/*     */     
/* 314 */     String namespace2 = XmlUtil.getAttributeOrNull(e2, "namespace");
/* 315 */     if (namespace2 != null) {
/* 316 */       headerfault.setNamespace(namespace2);
/*     */     }
/*     */     
/* 319 */     String encodingStyle2 = XmlUtil.getAttributeOrNull(e2, "encodingStyle");
/* 320 */     if (encodingStyle2 != null) {
/* 321 */       headerfault.setEncodingStyle(encodingStyle2);
/*     */     }
/*     */     
/* 324 */     String part2 = XmlUtil.getAttributeOrNull(e2, "part");
/* 325 */     if (part2 != null) {
/* 326 */       headerfault.setPart(part2);
/*     */     }
/*     */     
/* 329 */     String messageAttr2 = XmlUtil.getAttributeOrNull(e2, "message");
/* 330 */     if (messageAttr2 != null) {
/* 331 */       headerfault.setMessage(context
/* 332 */           .translateQualifiedName(context.getLocation(e2), messageAttr2));
/*     */     }
/*     */     
/* 335 */     header.add(headerfault);
/* 336 */     context.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleFaultExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 343 */     if (XmlUtil.matchesTagNS(e, getFaultQName())) {
/* 344 */       context.push();
/* 345 */       context.registerNamespaces(e);
/*     */       
/* 347 */       SOAPFault fault = new SOAPFault(context.getLocation(e));
/*     */       
/* 349 */       String name = XmlUtil.getAttributeOrNull(e, "name");
/* 350 */       if (name != null) {
/* 351 */         fault.setName(name);
/*     */       }
/*     */       
/* 354 */       String use = XmlUtil.getAttributeOrNull(e, "use");
/* 355 */       if (use != null) {
/* 356 */         if (use.equals("literal")) {
/* 357 */           fault.setUse(SOAPUse.LITERAL);
/* 358 */         } else if (use.equals("encoded")) {
/* 359 */           fault.setUse(SOAPUse.ENCODED);
/*     */         } else {
/* 361 */           Util.fail("parsing.invalidAttributeValue", "use", use);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 369 */       String namespace = XmlUtil.getAttributeOrNull(e, "namespace");
/* 370 */       if (namespace != null) {
/* 371 */         fault.setNamespace(namespace);
/*     */       }
/*     */ 
/*     */       
/* 375 */       String encodingStyle = XmlUtil.getAttributeOrNull(e, "encodingStyle");
/* 376 */       if (encodingStyle != null) {
/* 377 */         fault.setEncodingStyle(encodingStyle);
/*     */       }
/*     */       
/* 380 */       parent.addExtension((TWSDLExtension)fault);
/* 381 */       context.pop();
/*     */       
/* 383 */       return true;
/* 384 */     }  if (XmlUtil.matchesTagNS(e, getHeaderQName()))
/*     */     {
/*     */       
/* 387 */       return handleHeaderElement(parent, e, (TWSDLParserContextImpl)context);
/*     */     }
/* 389 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 391 */         .getTagName(), e
/* 392 */         .getNamespaceURI());
/* 393 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleServiceExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 401 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 403 */         .getTagName(), e
/* 404 */         .getNamespaceURI());
/* 405 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 413 */     if (XmlUtil.matchesTagNS(e, getAddressQName())) {
/* 414 */       context.push();
/* 415 */       context.registerNamespaces(e);
/*     */       
/* 417 */       SOAPAddress address = new SOAPAddress(context.getLocation(e));
/*     */ 
/*     */       
/* 420 */       String location = Util.getRequiredAttribute(e, "location");
/* 421 */       address.setLocation(location);
/*     */       
/* 423 */       parent.addExtension((TWSDLExtension)address);
/* 424 */       context.pop();
/*     */       
/* 426 */       return true;
/*     */     } 
/* 428 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 430 */         .getTagName(), e
/* 431 */         .getNamespaceURI());
/* 432 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlePortTypeExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 437 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 439 */         .getTagName(), e
/* 440 */         .getNamespaceURI());
/* 441 */     return false;
/*     */   }
/*     */   
/*     */   protected QName getBodyQName() {
/* 445 */     return SOAPConstants.QNAME_BODY;
/*     */   }
/*     */   
/*     */   protected QName getHeaderQName() {
/* 449 */     return SOAPConstants.QNAME_HEADER;
/*     */   }
/*     */   
/*     */   protected QName getHeaderfaultQName() {
/* 453 */     return SOAPConstants.QNAME_HEADERFAULT;
/*     */   }
/*     */   
/*     */   protected QName getOperationQName() {
/* 457 */     return SOAPConstants.QNAME_OPERATION;
/*     */   }
/*     */   
/*     */   protected QName getFaultQName() {
/* 461 */     return SOAPConstants.QNAME_FAULT;
/*     */   }
/*     */   
/*     */   protected QName getAddressQName() {
/* 465 */     return SOAPConstants.QNAME_ADDRESS;
/*     */   }
/*     */   
/*     */   protected QName getBindingQName() {
/* 469 */     return SOAPConstants.QNAME_BINDING;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\SOAPExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */