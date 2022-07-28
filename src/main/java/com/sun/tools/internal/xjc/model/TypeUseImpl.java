/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JStringLiteral;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*     */ final class TypeUseImpl
/*     */   implements TypeUse
/*     */ {
/*     */   private final CNonElement coreType;
/*     */   private final boolean collection;
/*     */   private final CAdapter adapter;
/*     */   private final ID id;
/*     */   private final MimeType expectedMimeType;
/*     */   
/*     */   public TypeUseImpl(CNonElement itemType, boolean collection, ID id, MimeType expectedMimeType, CAdapter adapter) {
/*  54 */     this.coreType = itemType;
/*  55 */     this.collection = collection;
/*  56 */     this.id = id;
/*  57 */     this.expectedMimeType = expectedMimeType;
/*  58 */     this.adapter = adapter;
/*     */   }
/*     */   
/*     */   public boolean isCollection() {
/*  62 */     return this.collection;
/*     */   }
/*     */   
/*     */   public CNonElement getInfo() {
/*  66 */     return this.coreType;
/*     */   }
/*     */   
/*     */   public CAdapter getAdapterUse() {
/*  70 */     return this.adapter;
/*     */   }
/*     */   
/*     */   public ID idUse() {
/*  74 */     return this.id;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/*  78 */     return this.expectedMimeType;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  82 */     if (this == o) return true; 
/*  83 */     if (!(o instanceof TypeUseImpl)) return false;
/*     */     
/*  85 */     TypeUseImpl that = (TypeUseImpl)o;
/*     */     
/*  87 */     if (this.collection != that.collection) return false; 
/*  88 */     if (this.id != that.id) return false; 
/*  89 */     if ((this.adapter != null) ? !this.adapter.equals(that.adapter) : (that.adapter != null)) return false; 
/*  90 */     if ((this.coreType != null) ? !this.coreType.equals(that.coreType) : (that.coreType != null)) return false;
/*     */     
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     int result = (this.coreType != null) ? this.coreType.hashCode() : 0;
/*  98 */     result = 29 * result + (this.collection ? 1 : 0);
/*  99 */     result = 29 * result + ((this.adapter != null) ? this.adapter.hashCode() : 0);
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression createConstant(Outline outline, XmlString lexical) {
/* 105 */     if (isCollection()) return null;
/*     */     
/* 107 */     if (this.adapter == null) return this.coreType.createConstant(outline, lexical);
/*     */ 
/*     */     
/* 110 */     JExpression cons = this.coreType.createConstant(outline, lexical);
/* 111 */     Class<? extends XmlAdapter> atype = this.adapter.getAdapterIfKnown();
/*     */ 
/*     */     
/* 114 */     if (cons instanceof JStringLiteral && atype != null) {
/* 115 */       JStringLiteral scons = (JStringLiteral)cons;
/* 116 */       XmlAdapter a = ClassFactory.<XmlAdapter>create((Class)atype);
/*     */       try {
/* 118 */         Object value = a.unmarshal(scons.str);
/* 119 */         if (value instanceof String) {
/* 120 */           return JExpr.lit((String)value);
/*     */         }
/* 122 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 127 */     return (JExpression)JExpr._new(this.adapter.getAdapterClass(outline)).invoke("unmarshal").arg(cons);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\TypeUseImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */