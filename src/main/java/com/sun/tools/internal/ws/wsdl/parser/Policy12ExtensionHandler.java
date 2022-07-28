/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensionHandler;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Policy12ExtensionHandler
/*     */   extends TWSDLExtensionHandler
/*     */ {
/*     */   public String getNamespaceURI() {
/*  49 */     return NamespaceVersion.v1_2.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlePortTypeExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  54 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleDefinitionsExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  59 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleBindingExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  64 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleOperationExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  69 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleInputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  74 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleOutputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  79 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleFaultExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  84 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleServiceExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  89 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  94 */     return handleExtension(context, parent, e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handleExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 102 */     return (XmlUtil.matchesTagNS(e, NamespaceVersion.v1_2.asQName(XmlToken.Policy)) || 
/* 103 */       XmlUtil.matchesTagNS(e, NamespaceVersion.v1_2.asQName(XmlToken.PolicyReference)) || 
/* 104 */       XmlUtil.matchesTagNS(e, NamespaceVersion.v1_2.asQName(XmlToken.UsingPolicy)));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\Policy12ExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */