/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityReferenceAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityReferenceValidator;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Kind;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public class WSDLDocument
/*     */   extends AbstractDocument
/*     */ {
/*     */   private Definitions _definitions;
/*     */   
/*     */   public WSDLDocument(MetadataFinder forest, ErrorReceiver errReceiver) {
/*  44 */     super(forest, errReceiver);
/*     */   }
/*     */   
/*     */   public Definitions getDefinitions() {
/*  48 */     return this._definitions;
/*     */   }
/*     */   
/*     */   public void setDefinitions(Definitions d) {
/*  52 */     this._definitions = d;
/*     */   }
/*     */ 
/*     */   
/*     */   public QName[] getAllServiceQNames() {
/*  57 */     ArrayList<QName> serviceQNames = new ArrayList();
/*     */     
/*  59 */     for (Iterator<Service> iter = getDefinitions().services(); iter.hasNext(); ) {
/*  60 */       Service next = iter.next();
/*  61 */       String targetNamespace = next.getDefining().getTargetNamespaceURI();
/*  62 */       String localName = next.getName();
/*  63 */       QName serviceQName = new QName(targetNamespace, localName);
/*  64 */       serviceQNames.add(serviceQName);
/*     */     } 
/*  66 */     return serviceQNames.<QName>toArray(new QName[serviceQNames.size()]);
/*     */   }
/*     */   
/*     */   public QName[] getAllPortQNames() {
/*  70 */     ArrayList<QName> portQNames = new ArrayList();
/*     */     
/*  72 */     for (Iterator<Service> iter = getDefinitions().services(); iter.hasNext(); ) {
/*  73 */       Service next = iter.next();
/*     */       
/*  75 */       for (Iterator<Port> piter = next.ports(); piter.hasNext(); ) {
/*     */         
/*  77 */         Port pnext = piter.next();
/*     */         
/*  79 */         String targetNamespace = pnext.getDefining().getTargetNamespaceURI();
/*  80 */         String localName = pnext.getName();
/*  81 */         QName portQName = new QName(targetNamespace, localName);
/*  82 */         portQNames.add(portQName);
/*     */       } 
/*     */     } 
/*  85 */     return portQNames.<QName>toArray(new QName[portQNames.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public QName[] getPortQNames(String serviceNameLocalPart) {
/*  90 */     ArrayList<QName> portQNames = new ArrayList();
/*     */     
/*  92 */     for (Iterator<Service> iter = getDefinitions().services(); iter.hasNext(); ) {
/*  93 */       Service next = iter.next();
/*  94 */       if (next.getName().equals(serviceNameLocalPart)) {
/*  95 */         for (Iterator<Port> piter = next.ports(); piter.hasNext(); ) {
/*  96 */           Port pnext = piter.next();
/*     */           
/*  98 */           String targetNamespace = pnext.getDefining().getTargetNamespaceURI();
/*  99 */           String localName = pnext.getName();
/* 100 */           QName portQName = new QName(targetNamespace, localName);
/* 101 */           portQNames.add(portQName);
/*     */         } 
/*     */       }
/*     */     } 
/* 105 */     return portQNames.<QName>toArray(new QName[portQNames.size()]);
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 109 */     this._definitions.accept(visitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(EntityReferenceValidator validator) {
/* 114 */     GloballyValidatingAction action = new GloballyValidatingAction(this, validator);
/*     */     
/* 116 */     withAllSubEntitiesDo(action);
/* 117 */     if (action.getException() != null) {
/* 118 */       throw action.getException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity getRoot() {
/* 124 */     return this._definitions;
/*     */   }
/*     */   
/*     */   private static class GloballyValidatingAction implements EntityAction, EntityReferenceAction {
/*     */     private ValidationException _exception;
/*     */     private AbstractDocument _document;
/*     */     private EntityReferenceValidator _validator;
/*     */     
/*     */     public GloballyValidatingAction(AbstractDocument document, EntityReferenceValidator validator) {
/* 133 */       this._document = document;
/* 134 */       this._validator = validator;
/*     */     }
/*     */ 
/*     */     
/*     */     public void perform(Entity entity) {
/*     */       try {
/* 140 */         entity.validateThis();
/* 141 */         entity.withAllEntityReferencesDo(this);
/* 142 */         entity.withAllSubEntitiesDo(this);
/* 143 */       } catch (ValidationException e) {
/* 144 */         if (this._exception == null) {
/* 145 */           this._exception = e;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void perform(Kind kind, QName name) {
/*     */       try {
/* 153 */         this._document.find(kind, name);
/* 154 */       } catch (NoSuchEntityException e) {
/*     */         
/* 156 */         if (this._exception == null && (
/* 157 */           this._validator == null || 
/* 158 */           !this._validator.isValid(kind, name))) {
/* 159 */           this._exception = (ValidationException)e;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ValidationException getException() {
/* 166 */       return this._exception;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\WSDLDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */