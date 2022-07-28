/*    */ package com.sun.tools.internal.ws.wsdl.document.http;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HTTPUrlReplacement
/*    */   extends ExtensionImpl
/*    */ {
/*    */   public HTTPUrlReplacement(Locator locator) {
/* 41 */     super(locator);
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 45 */     return HTTPConstants.QNAME_URL_REPLACEMENT;
/*    */   }
/*    */   
/*    */   public void validateThis() {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\http\HTTPUrlReplacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */