/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
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
/*     */ public class BindingInput
/*     */   extends Entity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private ExtensibilityHelper _helper;
/*     */   private Documentation _documentation;
/*     */   private String _name;
/*     */   private TWSDLExtensible parent;
/*     */   
/*     */   public BindingInput(Locator locator) {
/*  45 */     super(locator);
/*  46 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  50 */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  54 */     this._name = name;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  58 */     return WSDLConstants.QNAME_INPUT;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  62 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  66 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/*  70 */     return getName();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/*  74 */     return getParent().getNamespaceURI();
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/*  78 */     return getElementName();
/*     */   }
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/*  82 */     this._helper.addExtension(e);
/*     */   }
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/*  86 */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/*  90 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*  94 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/*  98 */     visitor.preVisit(this);
/*  99 */     this._helper.accept(visitor);
/* 100 */     visitor.postVisit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateThis() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 111 */     this.parent = parent;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\BindingInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */