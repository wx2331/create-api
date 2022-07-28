/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Types
/*     */   extends Entity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private TWSDLExtensible parent;
/*     */   private ExtensibilityHelper _helper;
/*     */   private Documentation _documentation;
/*     */   
/*     */   public Types(Locator locator) {
/*  46 */     super(locator);
/*  47 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getElementName() {
/*  52 */     return WSDLConstants.QNAME_TYPES;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  56 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  60 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/*  64 */     visitor.preVisit(this);
/*  65 */     this._helper.accept(visitor);
/*  66 */     visitor.postVisit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateThis() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNameValue() {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/*  83 */     return (this.parent == null) ? null : this.parent.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getWSDLElementName() {
/*  88 */     return getElementName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/*  93 */     this._helper.addExtension(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/*  98 */     return this._helper.extensions();
/*     */   }
/*     */ 
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 103 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 107 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/* 112 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */   
/*     */   public void accept(ExtensionVisitor visitor) throws Exception {
/* 116 */     this._helper.accept(visitor);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Types.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */