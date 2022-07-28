/*     */ package com.sun.tools.internal.ws.wsdl.framework;
/*     */ 
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.DOMForest;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractDocument
/*     */ {
/*     */   protected final DOMForest forest;
/*     */   protected final ErrorReceiver errReceiver;
/*     */   private final Map kinds;
/*     */   private String _systemId;
/*     */   private final Set importedDocuments;
/*     */   private final List importedEntities;
/*     */   private final Set includedDocuments;
/*     */   private final List includedEntities;
/*     */   
/*     */   protected AbstractDocument(MetadataFinder forest, ErrorReceiver errReceiver) {
/*  48 */     this.forest = (DOMForest)forest;
/*  49 */     this.errReceiver = errReceiver;
/*  50 */     this.kinds = new HashMap<>();
/*  51 */     this.importedEntities = new ArrayList();
/*  52 */     this.importedDocuments = new HashSet();
/*  53 */     this.includedEntities = new ArrayList();
/*  54 */     this.includedDocuments = new HashSet();
/*     */   }
/*     */   
/*     */   public String getSystemId() {
/*  58 */     return this._systemId;
/*     */   }
/*     */   
/*     */   public void setSystemId(String s) {
/*  62 */     if (this._systemId != null && !this._systemId.equals(s))
/*     */     {
/*  64 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  67 */     this._systemId = s;
/*  68 */     if (s != null) {
/*  69 */       this.importedDocuments.add(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addIncludedDocument(String systemId) {
/*  74 */     this.includedDocuments.add(systemId);
/*     */   }
/*     */   
/*     */   public boolean isIncludedDocument(String systemId) {
/*  78 */     return this.includedDocuments.contains(systemId);
/*     */   }
/*     */   
/*     */   public void addIncludedEntity(Entity entity) {
/*  82 */     this.includedEntities.add(entity);
/*     */   }
/*     */   
/*     */   public void addImportedDocument(String systemId) {
/*  86 */     this.importedDocuments.add(systemId);
/*     */   }
/*     */   
/*     */   public boolean isImportedDocument(String systemId) {
/*  90 */     return this.importedDocuments.contains(systemId);
/*     */   }
/*     */   
/*     */   public void addImportedEntity(Entity entity) {
/*  94 */     this.importedEntities.add(entity);
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*  98 */     if (getRoot() != null) {
/*  99 */       action.perform(getRoot());
/*     */     }
/*     */     
/* 102 */     for (Iterator<Entity> iterator1 = this.importedEntities.iterator(); iterator1.hasNext();) {
/* 103 */       action.perform(iterator1.next());
/*     */     }
/*     */     
/* 106 */     for (Iterator<Entity> iter = this.includedEntities.iterator(); iter.hasNext();) {
/* 107 */       action.perform(iter.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public Map getMap(Kind k) {
/* 112 */     Map<Object, Object> m = (Map)this.kinds.get(k.getName());
/* 113 */     if (m == null) {
/* 114 */       m = new HashMap<>();
/* 115 */       this.kinds.put(k.getName(), m);
/*     */     } 
/* 117 */     return m;
/*     */   }
/*     */   
/*     */   public void define(GloballyKnown e) {
/* 121 */     Map<QName, GloballyKnown> map = getMap(e.getKind());
/* 122 */     if (e.getName() == null) {
/*     */       return;
/*     */     }
/* 125 */     QName name = new QName(e.getDefining().getTargetNamespaceURI(), e.getName());
/*     */     
/* 127 */     if (map.containsKey(name)) {
/* 128 */       this.errReceiver.error(e.getLocator(), WsdlMessages.ENTITY_DUPLICATE_WITH_TYPE(e.getElementName().getLocalPart(), e.getName()));
/* 129 */       throw new AbortException();
/*     */     } 
/* 131 */     map.put(name, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public GloballyKnown find(Kind k, QName name) {
/* 136 */     Map map = getMap(k);
/* 137 */     Object result = map.get(name);
/* 138 */     if (result == null) {
/* 139 */       this.errReceiver.error(null, WsdlMessages.ENTITY_NOT_FOUND_BY_Q_NAME(k.getName(), name, this._systemId));
/* 140 */       throw new AbortException();
/*     */     } 
/* 142 */     return (GloballyKnown)result;
/*     */   }
/*     */   
/*     */   public void validateLocally() {
/* 146 */     LocallyValidatingAction action = new LocallyValidatingAction();
/* 147 */     withAllSubEntitiesDo(action);
/* 148 */     if (action.getException() != null) {
/* 149 */       throw action.getException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void validate(EntityReferenceValidator paramEntityReferenceValidator);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Entity getRoot();
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LocallyValidatingAction
/*     */     implements EntityAction
/*     */   {
/*     */     private ValidationException _exception;
/*     */ 
/*     */     
/*     */     public void perform(Entity entity) {
/*     */       try {
/* 171 */         entity.validateThis();
/* 172 */         entity.withAllSubEntitiesDo(this);
/* 173 */       } catch (ValidationException e) {
/* 174 */         if (this._exception == null) {
/* 175 */           this._exception = e;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public ValidationException getException() {
/* 181 */       return this._exception;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\AbstractDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */