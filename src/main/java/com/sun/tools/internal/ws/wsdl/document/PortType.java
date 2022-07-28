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
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class PortType
/*     */   extends GlobalEntity
/*     */   implements TWSDLExtensible
/*     */ {
/*     */   private TWSDLExtensible parent;
/*     */   private Documentation _documentation;
/*     */   private List _operations;
/*     */   private Set _operationKeys;
/*     */   private ExtensibilityHelper _helper;
/*     */   
/*     */   public PortType(Defining defining, Locator locator, ErrorReceiver errReceiver) {
/*  45 */     super(defining, locator, errReceiver);
/*  46 */     this._operations = new ArrayList();
/*  47 */     this._operationKeys = new HashSet();
/*  48 */     this._helper = new ExtensibilityHelper();
/*     */   }
/*     */   
/*     */   public void add(Operation operation) {
/*  52 */     String key = operation.getUniqueKey();
/*  53 */     if (this._operationKeys.contains(key))
/*  54 */       throw new ValidationException("validation.ambiguousName", new Object[] { operation
/*     */             
/*  56 */             .getName() }); 
/*  57 */     this._operationKeys.add(key);
/*  58 */     this._operations.add(operation);
/*     */   }
/*     */   
/*     */   public Iterator operations() {
/*  62 */     return this._operations.iterator();
/*     */   }
/*     */   
/*     */   public Set getOperationsNamed(String s) {
/*  66 */     Set<Operation> result = new HashSet();
/*  67 */     for (Iterator<Operation> iter = this._operations.iterator(); iter.hasNext(); ) {
/*  68 */       Operation operation = iter.next();
/*  69 */       if (operation.getName().equals(s)) {
/*  70 */         result.add(operation);
/*     */       }
/*     */     } 
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   public Kind getKind() {
/*  77 */     return Kinds.PORT_TYPE;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  81 */     return WSDLConstants.QNAME_PORT_TYPE;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  85 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  89 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*  93 */     super.withAllSubEntitiesDo(action);
/*     */     
/*  95 */     for (Iterator<Entity> iter = this._operations.iterator(); iter.hasNext();) {
/*  96 */       action.perform(iter.next());
/*     */     }
/*  98 */     this._helper.withAllSubEntitiesDo(action);
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 102 */     visitor.preVisit(this);
/* 103 */     this._helper.accept(visitor);
/* 104 */     for (Iterator<Operation> iter = this._operations.iterator(); iter.hasNext();) {
/* 105 */       ((Operation)iter.next()).accept(visitor);
/*     */     }
/* 107 */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/* 111 */     if (getName() == null) {
/* 112 */       failValidation("validation.missingRequiredAttribute", "name");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getNameValue() {
/* 117 */     return getName();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/* 121 */     return getDefining().getTargetNamespaceURI();
/*     */   }
/*     */   
/*     */   public QName getWSDLElementName() {
/* 125 */     return getElementName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(TWSDLExtension e) {
/* 132 */     this._helper.addExtension(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<TWSDLExtension> extensions() {
/* 140 */     return this._helper.extensions();
/*     */   }
/*     */   
/*     */   public TWSDLExtensible getParent() {
/* 144 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(TWSDLExtensible parent) {
/* 148 */     this.parent = parent;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\PortType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */