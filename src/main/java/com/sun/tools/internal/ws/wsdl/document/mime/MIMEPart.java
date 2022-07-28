/*    */ package com.sun.tools.internal.ws.wsdl.document.mime;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
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
/*    */ public class MIMEPart
/*    */   extends ExtensionImpl
/*    */   implements TWSDLExtensible
/*    */ {
/*    */   private String _name;
/*    */   private ExtensibilityHelper _helper;
/*    */   
/*    */   public MIMEPart(Locator locator) {
/* 45 */     super(locator);
/* 46 */     this._helper = new ExtensibilityHelper();
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 50 */     return MIMEConstants.QNAME_PART;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 54 */     return this._name;
/*    */   }
/*    */   
/*    */   public void setName(String s) {
/* 58 */     this._name = s;
/*    */   }
/*    */   
/*    */   public String getNameValue() {
/* 62 */     return getName();
/*    */   }
/*    */   
/*    */   public String getNamespaceURI() {
/* 66 */     return getParent().getNamespaceURI();
/*    */   }
/*    */   
/*    */   public QName getWSDLElementName() {
/* 70 */     return getElementName();
/*    */   }
/*    */   
/*    */   public void addExtension(TWSDLExtension e) {
/* 74 */     this._helper.addExtension(e);
/*    */   }
/*    */   
/*    */   public Iterable<TWSDLExtension> extensions() {
/* 78 */     return this._helper.extensions();
/*    */   }
/*    */   
/*    */   public void withAllSubEntitiesDo(EntityAction action) {
/* 82 */     this._helper.withAllSubEntitiesDo(action);
/*    */   }
/*    */   
/*    */   public void validateThis() {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\mime\MIMEPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */