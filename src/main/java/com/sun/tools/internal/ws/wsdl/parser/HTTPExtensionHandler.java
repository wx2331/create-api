/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.tools.internal.ws.wsdl.document.http.HTTPAddress;
/*     */ import com.sun.tools.internal.ws.wsdl.document.http.HTTPBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.http.HTTPConstants;
/*     */ import com.sun.tools.internal.ws.wsdl.document.http.HTTPOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.http.HTTPUrlEncoded;
/*     */ import com.sun.tools.internal.ws.wsdl.document.http.HTTPUrlReplacement;
/*     */ import java.util.Map;
/*     */ import org.w3c.dom.Element;
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
/*     */ public class HTTPExtensionHandler
/*     */   extends AbstractExtensionHandler
/*     */ {
/*     */   public HTTPExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap) {
/*  45 */     super(extensionHandlerMap);
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/*  49 */     return "http://schemas.xmlsoap.org/wsdl/http/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleDefinitionsExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  56 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/*  58 */         .getTagName(), e
/*  59 */         .getNamespaceURI());
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleTypesExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  67 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/*  69 */         .getTagName(), e
/*  70 */         .getNamespaceURI());
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleBindingExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  78 */     if (XmlUtil.matchesTagNS(e, HTTPConstants.QNAME_BINDING)) {
/*  79 */       context.push();
/*  80 */       context.registerNamespaces(e);
/*     */       
/*  82 */       HTTPBinding binding = new HTTPBinding(context.getLocation(e));
/*     */       
/*  84 */       String verb = Util.getRequiredAttribute(e, "verb");
/*  85 */       binding.setVerb(verb);
/*     */       
/*  87 */       parent.addExtension((TWSDLExtension)binding);
/*  88 */       context.pop();
/*     */       
/*  90 */       return true;
/*     */     } 
/*  92 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/*  94 */         .getTagName(), e
/*  95 */         .getNamespaceURI());
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleOperationExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 104 */     if (XmlUtil.matchesTagNS(e, HTTPConstants.QNAME_OPERATION)) {
/* 105 */       context.push();
/* 106 */       context.registerNamespaces(e);
/*     */       
/* 108 */       HTTPOperation operation = new HTTPOperation(context.getLocation(e));
/*     */ 
/*     */       
/* 111 */       String location = Util.getRequiredAttribute(e, "location");
/* 112 */       operation.setLocation(location);
/*     */       
/* 114 */       parent.addExtension((TWSDLExtension)operation);
/* 115 */       context.pop();
/*     */ 
/*     */ 
/*     */       
/* 119 */       return true;
/*     */     } 
/* 121 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 123 */         .getTagName(), e
/* 124 */         .getNamespaceURI());
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleInputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 133 */     if (XmlUtil.matchesTagNS(e, HTTPConstants.QNAME_URL_ENCODED)) {
/* 134 */       parent.addExtension((TWSDLExtension)new HTTPUrlEncoded(context.getLocation(e)));
/* 135 */       return true;
/*     */     } 
/* 137 */     if (XmlUtil.matchesTagNS(e, HTTPConstants.QNAME_URL_REPLACEMENT)) {
/* 138 */       parent.addExtension((TWSDLExtension)new HTTPUrlReplacement(context.getLocation(e)));
/* 139 */       return true;
/*     */     } 
/* 141 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 143 */         .getTagName(), e
/* 144 */         .getNamespaceURI());
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleOutputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 153 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 155 */         .getTagName(), e
/* 156 */         .getNamespaceURI());
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleFaultExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 164 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 166 */         .getTagName(), e
/* 167 */         .getNamespaceURI());
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleServiceExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 175 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 177 */         .getTagName(), e
/* 178 */         .getNamespaceURI());
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 186 */     if (XmlUtil.matchesTagNS(e, HTTPConstants.QNAME_ADDRESS)) {
/* 187 */       context.push();
/* 188 */       context.registerNamespaces(e);
/*     */       
/* 190 */       HTTPAddress address = new HTTPAddress(context.getLocation(e));
/*     */ 
/*     */       
/* 193 */       String location = Util.getRequiredAttribute(e, "location");
/* 194 */       address.setLocation(location);
/*     */       
/* 196 */       parent.addExtension((TWSDLExtension)address);
/* 197 */       context.pop();
/*     */       
/* 199 */       return true;
/*     */     } 
/* 201 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 203 */         .getTagName(), e
/* 204 */         .getNamespaceURI());
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlePortTypeExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 210 */     Util.fail("parsing.invalidExtensionElement", e
/*     */         
/* 212 */         .getTagName(), e
/* 213 */         .getNamespaceURI());
/* 214 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\HTTPExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */