/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Defining;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.GlobalEntity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Kind;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Service
/*     */   extends GlobalEntity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private ExtensibilityHelper _helper;
/*     */   private Documentation _documentation;
/*     */   private List<Port> _ports;
/*     */   
/*     */   public Service(Defining defining, Locator locator, ErrorReceiver errReceiver) {
/*  47 */     super(defining, locator, errReceiver);
/*  48 */     this._ports = new ArrayList<>();
/*  49 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public void add(Port port) {
/*  53 */     port.setService(this);
/*  54 */     this._ports.add(port);
/*     */   }
/*     */   
/*     */   public Iterator<Port> ports() {
/*  58 */     return this._ports.iterator();
/*     */   }
/*     */   
/*     */   public Kind getKind() {
/*  62 */     return Kinds.SERVICE;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  66 */     return WSDLConstants.QNAME_SERVICE;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  70 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  74 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*  78 */     for (Iterator<Port> iter = this._ports.iterator(); iter.hasNext();) {
/*  79 */       action.perform((Entity)iter.next());
/*     */     }
/*  81 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/*  85 */     visitor.preVisit(this);
/*  86 */     for (Iterator<Port> iter = this._ports.iterator(); iter.hasNext();) {
/*  87 */       ((Port)iter.next()).accept(visitor);
/*     */     }
/*  89 */     this._helper.accept(visitor);
/*  90 */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/*  94 */     if (getName() == null) {
/*  95 */       failValidation("validation.missingRequiredAttribute", "name");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/* 100 */     return getName();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/* 104 */     return getDefining().getTargetNamespaceURI();
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/* 108 */     return getElementName();
/*     */   }
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/* 112 */     this._helper.addExtension(e);
/*     */   }
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/* 116 */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 120 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */