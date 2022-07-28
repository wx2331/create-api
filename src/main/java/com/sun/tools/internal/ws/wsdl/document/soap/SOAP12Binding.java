/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*    */ 
/*    */ import javax.xml.namespace.QName;
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
/*    */ public class SOAP12Binding
/*    */   extends SOAPBinding
/*    */ {
/*    */   public SOAP12Binding(Locator locator) {
/* 34 */     super(locator);
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 38 */     return SOAP12Constants.QNAME_BINDING;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\soap\SOAP12Binding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */