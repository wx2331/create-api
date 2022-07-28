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
/*    */ public class HTTPBinding
/*    */   extends ExtensionImpl
/*    */ {
/*    */   private String _verb;
/*    */   
/*    */   public HTTPBinding(Locator locator) {
/* 41 */     super(locator);
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 45 */     return HTTPConstants.QNAME_BINDING;
/*    */   }
/*    */   
/*    */   public String getVerb() {
/* 49 */     return this._verb;
/*    */   }
/*    */   
/*    */   public void setVerb(String s) {
/* 53 */     this._verb = s;
/*    */   }
/*    */   
/*    */   public void validateThis() {
/* 57 */     if (this._verb == null)
/* 58 */       failValidation("validation.missingRequiredAttribute", "verb"); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\http\HTTPBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */