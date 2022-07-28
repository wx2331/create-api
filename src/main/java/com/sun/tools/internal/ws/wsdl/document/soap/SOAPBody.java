/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
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
/*    */ public class SOAPBody
/*    */   extends ExtensionImpl
/*    */ {
/*    */   private String _encodingStyle;
/*    */   private String _namespace;
/*    */   private String _parts;
/*    */   private SOAPUse _use;
/*    */   
/*    */   public SOAPBody(Locator locator) {
/* 42 */     super(locator);
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
/* 98 */     this._use = SOAPUse.LITERAL;
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/*    */     return SOAPConstants.QNAME_BODY;
/*    */   }
/*    */   
/*    */   public String getNamespace() {
/*    */     return this._namespace;
/*    */   }
/*    */   
/*    */   public void setNamespace(String s) {
/*    */     this._namespace = s;
/*    */   }
/*    */   
/*    */   public SOAPUse getUse() {
/*    */     return this._use;
/*    */   }
/*    */   
/*    */   public void setUse(SOAPUse u) {
/*    */     this._use = u;
/*    */   }
/*    */   
/*    */   public boolean isEncoded() {
/*    */     return (this._use == SOAPUse.ENCODED);
/*    */   }
/*    */   
/*    */   public boolean isLiteral() {
/*    */     return (this._use == SOAPUse.LITERAL);
/*    */   }
/*    */   
/*    */   public String getEncodingStyle() {
/*    */     return this._encodingStyle;
/*    */   }
/*    */   
/*    */   public void setEncodingStyle(String s) {
/*    */     this._encodingStyle = s;
/*    */   }
/*    */   
/*    */   public String getParts() {
/*    */     return this._parts;
/*    */   }
/*    */   
/*    */   public void setParts(String s) {
/*    */     this._parts = s;
/*    */   }
/*    */   
/*    */   public void validateThis() {
/*    */     if (this._use == SOAPUse.ENCODED)
/*    */       throw new ValidationException("validation.unsupportedUse.encoded", new Object[] { Integer.valueOf(getLocator().getLineNumber()), getLocator().getSystemId() }); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\soap\SOAPBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */