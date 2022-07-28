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
/*    */ public class MIMEContent
/*    */   extends ExtensionImpl
/*    */ {
/*    */   private String _part;
/*    */   private String _type;
/*    */   
/*    */   public MIMEContent(Locator locator) {
/* 41 */     super(locator);
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 45 */     return MIMEConstants.QNAME_CONTENT;
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
/*    */   public String getType() {
/* 57 */     return this._type;
/*    */   }
/*    */   
/*    */   public void setType(String s) {
/* 61 */     this._type = s;
/*    */   }
/*    */   
/*    */   public void validateThis() {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\mime\MIMEContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */