/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Defining;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityReferenceAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.GlobalEntity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Kind;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.QNameAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class Binding
/*     */   extends GlobalEntity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private ExtensibilityHelper _helper;
/*     */   private Documentation _documentation;
/*     */   private QName _portType;
/*     */   private List _operations;
/*     */   private TWSDLExtensible parent;
/*     */   
/*     */   public Binding(Defining defining, Locator locator, ErrorReceiver receiver) {
/*  49 */     super(defining, locator, receiver);
/*  50 */     this._operations = new ArrayList();
/*  51 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public void add(BindingOperation operation) {
/*  55 */     this._operations.add(operation);
/*     */   }
/*     */   
/*     */   public Iterator operations() {
/*  59 */     return this._operations.iterator();
/*     */   }
/*     */   
/*     */   public QName getPortType() {
/*  63 */     return this._portType;
/*     */   }
/*     */   
/*     */   public void setPortType(QName n) {
/*  67 */     this._portType = n;
/*     */   }
/*     */   
/*     */   public PortType resolvePortType(AbstractDocument document) {
/*     */     try {
/*  72 */       return (PortType)document.find(Kinds.PORT_TYPE, this._portType);
/*  73 */     } catch (NoSuchEntityException e) {
/*  74 */       this.errorReceiver.error(getLocator(), WsdlMessages.ENTITY_NOT_FOUND_PORT_TYPE(this._portType, new QName(getNamespaceURI(), getName())));
/*  75 */       throw new AbortException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Kind getKind() {
/*  80 */     return Kinds.BINDING;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  84 */     return WSDLConstants.QNAME_BINDING;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  88 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  92 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*  96 */     for (Iterator<Entity> iter = this._operations.iterator(); iter.hasNext();) {
/*  97 */       action.perform(iter.next());
/*     */     }
/*  99 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/* 103 */     super.withAllQNamesDo(action);
/*     */     
/* 105 */     if (this._portType != null) {
/* 106 */       action.perform(this._portType);
/*     */     }
/*     */   }
/*     */   
/*     */   public void withAllEntityReferencesDo(EntityReferenceAction action) {
/* 111 */     super.withAllEntityReferencesDo(action);
/* 112 */     if (this._portType != null) {
/* 113 */       action.perform(Kinds.PORT_TYPE, this._portType);
/*     */     }
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 118 */     visitor.preVisit(this);
/*     */     
/* 120 */     this._helper.accept(visitor);
/* 121 */     for (Iterator<BindingOperation> iter = this._operations.iterator(); iter.hasNext();) {
/* 122 */       ((BindingOperation)iter.next()).accept(visitor);
/*     */     }
/* 124 */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/* 128 */     if (getName() == null) {
/* 129 */       failValidation("validation.missingRequiredAttribute", "name");
/*     */     }
/* 131 */     if (this._portType == null) {
/* 132 */       failValidation("validation.missingRequiredAttribute", "type");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/* 137 */     return getName();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/* 141 */     return getDefining().getTargetNamespaceURI();
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/* 145 */     return getElementName();
/*     */   }
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/* 149 */     this._helper.addExtension(e);
/*     */   }
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/* 153 */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 157 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 166 */     this.parent = parent;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Binding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */