/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
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
/*     */ public class Fault
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
/*     */   public Fault(Locator locator) {
/*  43 */     super(locator);
/*  44 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  48 */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  52 */     this._name = name;
/*     */   }
/*     */   
/*     */   public QName getMessage() {
/*  56 */     return this._message;
/*     */   }
/*     */   
/*     */   public void setMessage(QName n) {
/*  60 */     this._message = n;
/*     */   }
/*     */   
/*     */   public Message resolveMessage(AbstractDocument document) {
/*  64 */     return (Message)document.find(Kinds.MESSAGE, this._message);
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getElementName() {
/*  69 */     return WSDLConstants.QNAME_FAULT;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  73 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  77 */     this._documentation = d;
/*     */   }
/*     */ 
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/*  82 */     if (this._message != null) {
/*  83 */       action.perform(this._message);
/*     */     }
/*     */   }
/*     */ 
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
/*     */   
/*     */   public void validateThis() {
/* 102 */     if (this._name == null) {
/* 103 */       failValidation("validation.missingRequiredAttribute", "name");
/*     */     }
/* 105 */     if (this._message == null) {
/* 106 */       failValidation("validation.missingRequiredAttribute", "message");
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
/*     */   public String getNameValue() {
/* 118 */     return getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/* 123 */     return (this.parent == null) ? null : this.parent.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getWSDLElementName() {
/* 128 */     return getElementName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/* 136 */     this._helper.addExtension(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/* 145 */     return this._helper.extensions();
/*     */   }
/*     */ 
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 150 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 155 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAction() {
/* 161 */     return this._action;
/*     */   }
/*     */   
/*     */   public void setAction(String _action) {
/* 165 */     this._action = _action;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Fault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */