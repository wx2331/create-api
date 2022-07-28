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
/*     */ public class Input
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
/*     */   public Input(Locator locator, ErrorReceiver errReceiver) {
/*  46 */     super(locator);
/*  47 */     this.errorReceiver = errReceiver;
/*  48 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  52 */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  56 */     this._name = name;
/*     */   }
/*     */   
/*     */   public QName getMessage() {
/*  60 */     return this._message;
/*     */   }
/*     */   
/*     */   public void setMessage(QName n) {
/*  64 */     this._message = n;
/*     */   }
/*     */   
/*     */   public Message resolveMessage(AbstractDocument document) {
/*  68 */     return (Message)document.find(Kinds.MESSAGE, this._message);
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  72 */     return WSDLConstants.QNAME_INPUT;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  76 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  80 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/*  84 */     if (this._message != null) {
/*  85 */       action.perform(this._message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void withAllEntityReferencesDo(EntityReferenceAction action) {
/*  90 */     super.withAllEntityReferencesDo(action);
/*  91 */     if (this._message != null) {
/*  92 */       action.perform(Kinds.MESSAGE, this._message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/*  97 */     visitor.preVisit(this);
/*  98 */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/* 102 */     if (this._message == null) {
/* 103 */       this.errorReceiver.error(getLocator(), WsdlMessages.VALIDATION_MISSING_REQUIRED_ATTRIBUTE("name", "wsdl:message"));
/* 104 */       throw new AbortException();
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
/* 116 */     this._helper.addExtension(e);
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/* 120 */     return getElementName();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 124 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 128 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/* 132 */     return getElementName().getNamespaceURI();
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   public Iterable<? extends TWSDLExtension> extensions() {
/* 140 */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public String getAction() {
/* 144 */     return this._action;
/*     */   }
/*     */   
/*     */   public void setAction(String _action) {
/* 148 */     this._action = _action;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Input.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */