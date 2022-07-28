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
/*    */ public class HTTPOperation
/*    */   extends ExtensionImpl
/*    */ {
/*    */   private String _location;
/*    */   
/*    */   public HTTPOperation(Locator locator) {
/* 41 */     super(locator);
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 45 */     return HTTPConstants.QNAME_OPERATION;
/*    */   }
/*    */   
/*    */   public String getLocation() {
/* 49 */     return this._location;
/*    */   }
/*    */   
/*    */   public void setLocation(String s) {
/* 53 */     this._location = s;
/*    */   }
/*    */   
/*    */   public void validateThis() {
/* 57 */     if (this._location == null)
/* 58 */       failValidation("validation.missingRequiredAttribute", "location"); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\http\HTTPOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */