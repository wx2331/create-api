/*    */ package com.sun.tools.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*    */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class W3CAddressingExtensionHandler
/*    */   extends AbstractExtensionHandler
/*    */ {
/*    */   public W3CAddressingExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap) {
/* 48 */     this(extensionHandlerMap, null);
/*    */   }
/*    */   
/*    */   public W3CAddressingExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap, ErrorReceiver errReceiver) {
/* 52 */     super(extensionHandlerMap);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespaceURI() {
/* 57 */     return AddressingVersion.W3C.wsdlNsUri;
/*    */   }
/*    */ 
/*    */   
/*    */   protected QName getWSDLExtensionQName() {
/* 62 */     return AddressingVersion.W3C.wsdlExtensionTag;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean handleBindingExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 67 */     if (XmlUtil.matchesTagNS(e, getWSDLExtensionQName()))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 78 */       return true;
/*    */     }
/* 80 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 85 */     return handleBindingExtension(context, parent, e);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\W3CAddressingExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */