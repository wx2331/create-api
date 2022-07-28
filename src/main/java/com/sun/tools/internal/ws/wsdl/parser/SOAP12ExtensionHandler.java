/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAP12Binding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAP12Constants;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBinding;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SOAP12ExtensionHandler
/*     */   extends SOAPExtensionHandler
/*     */ {
/*     */   public SOAP12ExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap) {
/*  39 */     super(extensionHandlerMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/*  47 */     return "http://schemas.xmlsoap.org/wsdl/soap12/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getAddressQName() {
/*  55 */     return SOAP12Constants.QNAME_ADDRESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getBindingQName() {
/*  63 */     return SOAP12Constants.QNAME_BINDING;
/*     */   }
/*     */   
/*     */   protected SOAPBinding getSOAPBinding(Locator location) {
/*  67 */     return (SOAPBinding)new SOAP12Binding(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getBodyQName() {
/*  75 */     return SOAP12Constants.QNAME_BODY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getFaultQName() {
/*  83 */     return SOAP12Constants.QNAME_FAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getHeaderfaultQName() {
/*  91 */     return SOAP12Constants.QNAME_HEADERFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getHeaderQName() {
/*  99 */     return SOAP12Constants.QNAME_HEADER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName getOperationQName() {
/* 107 */     return SOAP12Constants.QNAME_OPERATION;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\SOAP12ExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */