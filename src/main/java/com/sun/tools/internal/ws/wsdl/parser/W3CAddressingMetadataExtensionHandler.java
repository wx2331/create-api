/*    */ package com.sun.tools.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*    */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*    */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import com.sun.tools.internal.ws.wsdl.document.Fault;
/*    */ import com.sun.tools.internal.ws.wsdl.document.Input;
/*    */ import com.sun.tools.internal.ws.wsdl.document.Output;
/*    */ import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
/*    */ import java.util.Map;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
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
/*    */ 
/*    */ public class W3CAddressingMetadataExtensionHandler
/*    */   extends AbstractExtensionHandler
/*    */ {
/*    */   private ErrorReceiver errReceiver;
/*    */   
/*    */   public W3CAddressingMetadataExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap, ErrorReceiver errReceiver) {
/* 55 */     super(extensionHandlerMap);
/* 56 */     this.errReceiver = errReceiver;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespaceURI() {
/* 61 */     return "http://www.w3.org/2007/05/addressing/metadata";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean handleInputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 66 */     String actionValue = XmlUtil.getAttributeNSOrNull(e, W3CAddressingMetadataConstants.WSAM_ACTION_QNAME);
/* 67 */     if (actionValue == null || actionValue.equals("")) {
/* 68 */       return warnEmptyAction(parent, context.getLocation(e));
/*    */     }
/* 70 */     ((Input)parent).setAction(actionValue);
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean handleOutputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 76 */     String actionValue = XmlUtil.getAttributeNSOrNull(e, W3CAddressingMetadataConstants.WSAM_ACTION_QNAME);
/* 77 */     if (actionValue == null || actionValue.equals("")) {
/* 78 */       return warnEmptyAction(parent, context.getLocation(e));
/*    */     }
/* 80 */     ((Output)parent).setAction(actionValue);
/* 81 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean handleFaultExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 86 */     String actionValue = XmlUtil.getAttributeNSOrNull(e, W3CAddressingMetadataConstants.WSAM_ACTION_QNAME);
/* 87 */     if (actionValue == null || actionValue.equals("")) {
/* 88 */       this.errReceiver.warning(context.getLocation(e), WsdlMessages.WARNING_FAULT_EMPTY_ACTION(parent.getNameValue(), parent.getWSDLElementName().getLocalPart(), parent.getParent().getNameValue()));
/* 89 */       return false;
/*    */     } 
/* 91 */     ((Fault)parent).setAction(actionValue);
/* 92 */     return true;
/*    */   }
/*    */   
/*    */   private boolean warnEmptyAction(TWSDLExtensible parent, Locator pos) {
/* 96 */     this.errReceiver.warning(pos, WsdlMessages.WARNING_INPUT_OUTPUT_EMPTY_ACTION(parent.getWSDLElementName().getLocalPart(), parent.getParent().getNameValue()));
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\W3CAddressingMetadataExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */