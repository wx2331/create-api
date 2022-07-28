/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityReferenceAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.QNameAction;
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
/*     */ public class Output
/*     */   extends Entity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private Documentation _documentation;
/*     */   private String _name;
/*     */   private QName _message;
/*     */   private String _action;
/*     */   private ExtensibilityHelper _helper;
/*     */   private TWSDLExtensible parent;
/*     */   
/*     */   public Output(Locator locator, ErrorReceiver errReceiver) {
/*  46 */     super(locator);
/*  47 */     this.errorReceiver = errReceiver;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  51 */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  55 */     this._name = name;
/*     */   }
/*     */   
/*     */   public QName getMessage() {
/*  59 */     return this._message;
/*     */   }
/*     */   
/*     */   public void setMessage(QName n) {
/*  63 */     this._message = n;
/*     */   }
/*     */   
/*     */   public Message resolveMessage(AbstractDocument document) {
/*  67 */     return (Message)document.find(Kinds.MESSAGE, this._message);
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  71 */     return WSDLConstants.QNAME_OUTPUT;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  75 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  79 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/*  83 */     if (this._message != null) {
/*  84 */       action.perform(this._message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void withAllEntityReferencesDo(EntityReferenceAction action) {
/*  89 */     super.withAllEntityReferencesDo(action);
/*  90 */     if (this._message != null) {
/*  91 */       action.perform(Kinds.MESSAGE, this._message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/*  96 */     visitor.preVisit(this);
/*  97 */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/* 101 */     if (this._message == null) {
/* 102 */       this.errorReceiver.error(getLocator(), WsdlMessages.VALIDATION_MISSING_REQUIRED_ATTRIBUTE("name", "wsdl:message"));
/* 103 */       throw new AbortException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/* 115 */     this._helper.addExtension(e);
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/* 119 */     return getElementName();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 123 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 127 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/* 131 */     return getElementName().getNamespaceURI();
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/* 135 */     return null;
/*     */   }
/*     */   
/*     */   public Iterable<? extends TWSDLExtension> extensions() {
/* 139 */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public String getAction() {
/* 143 */     return this._action;
/*     */   }
/*     */   
/*     */   public void setAction(String _action) {
/* 147 */     this._action = _action;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Output.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */