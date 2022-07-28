/*    */ package com.sun.tools.internal.ws.wsdl.document.soap;
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
/*    */ public class SOAPBinding
/*    */   extends ExtensionImpl
/*    */ {
/*    */   private String _transport;
/*    */   private SOAPStyle _style;
/*    */   
/*    */   public SOAPBinding(Locator locator) {
/* 41 */     super(locator);
/* 42 */     this._style = SOAPStyle.DOCUMENT;
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 46 */     return SOAPConstants.QNAME_BINDING;
/*    */   }
/*    */   
/*    */   public String getTransport() {
/* 50 */     return this._transport;
/*    */   }
/*    */   
/*    */   public void setTransport(String s) {
/* 54 */     this._transport = s;
/*    */   }
/*    */   
/*    */   public SOAPStyle getStyle() {
/* 58 */     return this._style;
/*    */   }
/*    */   
/*    */   public void setStyle(SOAPStyle s) {
/* 62 */     this._style = s;
/*    */   }
/*    */   
/*    */   public boolean isDocument() {
/* 66 */     return (this._style == SOAPStyle.DOCUMENT);
/*    */   }
/*    */   
/*    */   public boolean isRPC() {
/* 70 */     return (this._style == SOAPStyle.RPC);
/*    */   }
/*    */   
/*    */   public void validateThis() {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\soap\SOAPBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */