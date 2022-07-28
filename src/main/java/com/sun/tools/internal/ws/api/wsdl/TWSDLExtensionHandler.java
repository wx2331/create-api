/*     */ package com.sun.tools.internal.ws.api.wsdl;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.document.WSDLConstants;
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
/*     */ 
/*     */ 
/*     */ public abstract class TWSDLExtensionHandler
/*     */ {
/*     */   public String getNamespaceURI() {
/*  45 */     return null;
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
/*     */   public boolean doHandleExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  58 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_DEFINITIONS))
/*  59 */       return handleDefinitionsExtension(context, parent, e); 
/*  60 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_TYPES))
/*  61 */       return handleTypesExtension(context, parent, e); 
/*  62 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_PORT_TYPE))
/*  63 */       return handlePortTypeExtension(context, parent, e); 
/*  64 */     if (parent
/*  65 */       .getWSDLElementName().equals(WSDLConstants.QNAME_BINDING))
/*  66 */       return handleBindingExtension(context, parent, e); 
/*  67 */     if (parent
/*  68 */       .getWSDLElementName().equals(WSDLConstants.QNAME_OPERATION))
/*  69 */       return handleOperationExtension(context, parent, e); 
/*  70 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_INPUT))
/*  71 */       return handleInputExtension(context, parent, e); 
/*  72 */     if (parent
/*  73 */       .getWSDLElementName().equals(WSDLConstants.QNAME_OUTPUT))
/*  74 */       return handleOutputExtension(context, parent, e); 
/*  75 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_FAULT))
/*  76 */       return handleFaultExtension(context, parent, e); 
/*  77 */     if (parent
/*  78 */       .getWSDLElementName().equals(WSDLConstants.QNAME_SERVICE))
/*  79 */       return handleServiceExtension(context, parent, e); 
/*  80 */     if (parent.getWSDLElementName().equals(WSDLConstants.QNAME_PORT)) {
/*  81 */       return handlePortExtension(context, parent, e);
/*     */     }
/*  83 */     return false;
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
/*     */   public boolean handlePortTypeExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  97 */     return false;
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
/*     */   public boolean handleDefinitionsExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 110 */     return false;
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
/*     */   public boolean handleTypesExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 123 */     return false;
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
/*     */   public boolean handleBindingExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 136 */     return false;
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
/*     */   public boolean handleOperationExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 149 */     return false;
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
/*     */   public boolean handleInputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 162 */     return false;
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
/*     */   public boolean handleOutputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 175 */     return false;
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
/*     */   public boolean handleFaultExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 188 */     return false;
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
/*     */   public boolean handleServiceExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 201 */     return false;
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
/*     */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 214 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\api\wsdl\TWSDLExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */