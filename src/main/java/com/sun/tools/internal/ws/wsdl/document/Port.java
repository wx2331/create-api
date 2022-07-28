/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Defining;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityReferenceAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.GlobalEntity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Kind;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
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
/*     */ public class Port
/*     */   extends GlobalEntity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private ExtensibilityHelper _helper;
/*     */   private Documentation _documentation;
/*     */   private Service _service;
/*     */   private QName _binding;
/*     */   private TWSDLExtensible parent;
/*     */   
/*     */   public Port(Defining defining, Locator locator, ErrorReceiver errReceiver) {
/*  46 */     super(defining, locator, errReceiver);
/*  47 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public Service getService() {
/*  51 */     return this._service;
/*     */   }
/*     */   
/*     */   public void setService(Service s) {
/*  55 */     this._service = s;
/*     */   }
/*     */   
/*     */   public QName getBinding() {
/*  59 */     return this._binding;
/*     */   }
/*     */   
/*     */   public void setBinding(QName n) {
/*  63 */     this._binding = n;
/*     */   }
/*     */   
/*     */   public Binding resolveBinding(AbstractDocument document) {
/*     */     try {
/*  68 */       return (Binding)document.find(Kinds.BINDING, this._binding);
/*  69 */     } catch (NoSuchEntityException e) {
/*  70 */       this.errorReceiver.error(getLocator(), WsdlMessages.ENTITY_NOT_FOUND_BINDING(this._binding, new QName(getNamespaceURI(), getName())));
/*  71 */       throw new AbortException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Kind getKind() {
/*  76 */     return Kinds.PORT;
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/*  80 */     return getName();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/*  84 */     return getDefining().getTargetNamespaceURI();
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/*  88 */     return WSDLConstants.QNAME_PORT;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  92 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  96 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/* 100 */     super.withAllQNamesDo(action);
/*     */     
/* 102 */     if (this._binding != null) {
/* 103 */       action.perform(this._binding);
/*     */     }
/*     */   }
/*     */   
/*     */   public void withAllEntityReferencesDo(EntityReferenceAction action) {
/* 108 */     super.withAllEntityReferencesDo(action);
/* 109 */     if (this._binding != null) {
/* 110 */       action.perform(Kinds.BINDING, this._binding);
/*     */     }
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 115 */     visitor.preVisit(this);
/* 116 */     this._helper.accept(visitor);
/* 117 */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/* 121 */     if (getName() == null) {
/* 122 */       failValidation("validation.missingRequiredAttribute", "name");
/*     */     }
/* 124 */     if (this._binding == null) {
/* 125 */       failValidation("validation.missingRequiredAttribute", "binding");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/* 130 */     this._helper.addExtension(e);
/*     */   }
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/* 134 */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 138 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 142 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/* 146 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getElementName() {
/* 155 */     return getWSDLElementName();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Port.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */