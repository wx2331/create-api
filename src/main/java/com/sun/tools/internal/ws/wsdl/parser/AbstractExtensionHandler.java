/*    */ package com.sun.tools.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensionHandler;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*    */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEConstants;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractExtensionHandler
/*    */   extends TWSDLExtensionHandler
/*    */ {
/*    */   private final Map<String, AbstractExtensionHandler> extensionHandlers;
/*    */   private final Map<String, AbstractExtensionHandler> unmodExtenHandlers;
/*    */   
/*    */   public AbstractExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap) {
/* 47 */     this.extensionHandlers = extensionHandlerMap;
/* 48 */     this.unmodExtenHandlers = Collections.unmodifiableMap(this.extensionHandlers);
/*    */   }
/*    */   
/*    */   public Map<String, AbstractExtensionHandler> getExtensionHandlers() {
/* 52 */     return this.unmodExtenHandlers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean doHandleExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 65 */     if (parent.getWSDLElementName().equals(MIMEConstants.QNAME_PART)) {
/* 66 */       return handleMIMEPartExtension(context, parent, e);
/*    */     }
/* 68 */     return super.doHandleExtension(context, parent, e);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean handleMIMEPartExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 82 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\AbstractExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */