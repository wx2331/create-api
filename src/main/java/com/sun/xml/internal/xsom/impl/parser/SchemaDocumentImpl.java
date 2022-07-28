/*     */ package com.sun.xml.internal.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.parser.SchemaDocument;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public final class SchemaDocumentImpl
/*     */   implements SchemaDocument
/*     */ {
/*     */   private final SchemaImpl schema;
/*     */   private final String schemaDocumentURI;
/*  52 */   final Set<SchemaDocumentImpl> references = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   final Set<SchemaDocumentImpl> referers = new HashSet<>();
/*     */   
/*     */   protected SchemaDocumentImpl(SchemaImpl schema, String _schemaDocumentURI) {
/*  60 */     this.schema = schema;
/*  61 */     this.schemaDocumentURI = _schemaDocumentURI;
/*     */   }
/*     */   
/*     */   public String getSystemId() {
/*  65 */     return this.schemaDocumentURI;
/*     */   }
/*     */   
/*     */   public String getTargetNamespace() {
/*  69 */     return this.schema.getTargetNamespace();
/*     */   }
/*     */   
/*     */   public SchemaImpl getSchema() {
/*  73 */     return this.schema;
/*     */   }
/*     */   
/*     */   public Set<SchemaDocument> getReferencedDocuments() {
/*  77 */     return Collections.unmodifiableSet((Set)this.references);
/*     */   }
/*     */   
/*     */   public Set<SchemaDocument> getIncludedDocuments() {
/*  81 */     return getImportedDocuments(getTargetNamespace());
/*     */   }
/*     */   
/*     */   public Set<SchemaDocument> getImportedDocuments(String targetNamespace) {
/*  85 */     if (targetNamespace == null)
/*  86 */       throw new IllegalArgumentException(); 
/*  87 */     Set<SchemaDocument> r = new HashSet<>();
/*  88 */     for (SchemaDocumentImpl doc : this.references) {
/*  89 */       if (doc.getTargetNamespace().equals(targetNamespace))
/*  90 */         r.add(doc); 
/*     */     } 
/*  92 */     return Collections.unmodifiableSet(r);
/*     */   }
/*     */   
/*     */   public boolean includes(SchemaDocument doc) {
/*  96 */     if (!this.references.contains(doc))
/*  97 */       return false; 
/*  98 */     return (doc.getSchema() == this.schema);
/*     */   }
/*     */   
/*     */   public boolean imports(SchemaDocument doc) {
/* 102 */     if (!this.references.contains(doc))
/* 103 */       return false; 
/* 104 */     return (doc.getSchema() != this.schema);
/*     */   }
/*     */   
/*     */   public Set<SchemaDocument> getReferers() {
/* 108 */     return Collections.unmodifiableSet((Set)this.referers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 113 */     SchemaDocumentImpl rhs = (SchemaDocumentImpl)o;
/*     */     
/* 115 */     if (this.schemaDocumentURI == null || rhs.schemaDocumentURI == null)
/* 116 */       return (this == rhs); 
/* 117 */     if (!this.schemaDocumentURI.equals(rhs.schemaDocumentURI))
/* 118 */       return false; 
/* 119 */     return (this.schema == rhs.schema);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 124 */     if (this.schemaDocumentURI == null)
/* 125 */       return super.hashCode(); 
/* 126 */     if (this.schema == null) {
/* 127 */       return this.schemaDocumentURI.hashCode();
/*     */     }
/* 129 */     return this.schemaDocumentURI.hashCode() ^ this.schema.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\SchemaDocumentImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */