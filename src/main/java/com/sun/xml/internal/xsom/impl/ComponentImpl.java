/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.SCD;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.parser.SchemaDocument;
/*     */ import com.sun.xml.internal.xsom.util.ComponentNameFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
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
/*     */ public abstract class ComponentImpl
/*     */   implements XSComponent
/*     */ {
/*     */   protected final SchemaDocumentImpl ownerDocument;
/*     */   private AnnotationImpl annotation;
/*     */   private final Locator locator;
/*     */   private Object foreignAttributes;
/*     */   
/*     */   protected ComponentImpl(SchemaDocumentImpl _owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa) {
/*  47 */     this.ownerDocument = _owner;
/*  48 */     this.annotation = _annon;
/*  49 */     this.locator = _loc;
/*  50 */     this.foreignAttributes = fa;
/*     */   }
/*     */ 
/*     */   
/*     */   public SchemaImpl getOwnerSchema() {
/*  55 */     if (this.ownerDocument == null) {
/*  56 */       return null;
/*     */     }
/*  58 */     return this.ownerDocument.getSchema();
/*     */   }
/*     */   
/*     */   public XSSchemaSet getRoot() {
/*  62 */     if (this.ownerDocument == null) {
/*  63 */       return null;
/*     */     }
/*  65 */     return getOwnerSchema().getRoot();
/*     */   }
/*     */   
/*     */   public SchemaDocument getSourceDocument() {
/*  69 */     return (SchemaDocument)this.ownerDocument;
/*     */   }
/*     */   
/*     */   public final XSAnnotation getAnnotation() {
/*  73 */     return this.annotation;
/*     */   }
/*     */   public XSAnnotation getAnnotation(boolean createIfNotExist) {
/*  76 */     if (createIfNotExist && this.annotation == null) {
/*  77 */       this.annotation = new AnnotationImpl();
/*     */     }
/*  79 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public final Locator getLocator() {
/*  83 */     return this.locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ForeignAttributesImpl> getForeignAttributes() {
/*  94 */     Object t = this.foreignAttributes;
/*     */     
/*  96 */     if (t == null) {
/*  97 */       return Collections.EMPTY_LIST;
/*     */     }
/*  99 */     if (t instanceof List) {
/* 100 */       return (List<ForeignAttributesImpl>)t;
/*     */     }
/* 102 */     t = this.foreignAttributes = convertToList((ForeignAttributesImpl)t);
/* 103 */     return (List<ForeignAttributesImpl>)t;
/*     */   }
/*     */   
/*     */   public String getForeignAttribute(String nsUri, String localName) {
/* 107 */     for (ForeignAttributesImpl fa : getForeignAttributes()) {
/* 108 */       String v = fa.getValue(nsUri, localName);
/* 109 */       if (v != null) return v; 
/*     */     } 
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   private List<ForeignAttributesImpl> convertToList(ForeignAttributesImpl fa) {
/* 115 */     List<ForeignAttributesImpl> lst = new ArrayList<>();
/* 116 */     while (fa != null) {
/* 117 */       lst.add(fa);
/* 118 */       fa = fa.next;
/*     */     } 
/* 120 */     return Collections.unmodifiableList(lst);
/*     */   }
/*     */   
/*     */   public Collection<XSComponent> select(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 125 */       return SCD.create(scd, nsContext).select(this);
/* 126 */     } catch (ParseException e) {
/* 127 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public XSComponent selectSingle(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 133 */       return SCD.create(scd, nsContext).selectSingle(this);
/* 134 */     } catch (ParseException e) {
/* 135 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     return (String)apply((XSFunction)new ComponentNameFunction());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\ComponentImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */