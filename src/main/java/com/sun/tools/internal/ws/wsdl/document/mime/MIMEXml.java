/*    */ package com.sun.tools.internal.ws.wsdl.document.mime;
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
/*    */ public class MIMEXml
/*    */   extends ExtensionImpl
/*    */ {
/*    */   private String _part;
/*    */   
/*    */   public MIMEXml(Locator locator) {
/* 41 */     super(locator);
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 45 */     return MIMEConstants.QNAME_MIME_XML;
/*    */   }
/*    */   
/*    */   public String getPart() {
/* 49 */     return this._part;
/*    */   }
/*    */   
/*    */   public void setPart(String s) {
/* 53 */     this._part = s;
/*    */   }
/*    */   
/*    */   public void validateThis() {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\mime\MIMEXml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */