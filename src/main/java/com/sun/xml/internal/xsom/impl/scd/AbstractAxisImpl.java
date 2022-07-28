/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import java.util.Iterator;
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
/*     */ abstract class AbstractAxisImpl<T extends XSComponent>
/*     */   implements Axis<T>, XSFunction<Iterator<T>>
/*     */ {
/*     */   protected final Iterator<T> singleton(T t) {
/*  65 */     return Iterators.singleton(t);
/*     */   }
/*     */   
/*     */   protected final Iterator<T> union(T... items) {
/*  69 */     return new Iterators.Array<>(items);
/*     */   }
/*     */   
/*     */   protected final Iterator<T> union(Iterator<? extends T> first, Iterator<? extends T> second) {
/*  73 */     return new Iterators.Union<>(first, second);
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator(XSComponent contextNode) {
/*  77 */     return (Iterator<T>)contextNode.apply(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  84 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator(Iterator<? extends XSComponent> contextNodes) {
/*  91 */     return new Iterators.Map<T, XSComponent>(contextNodes) {
/*     */         protected Iterator<? extends T> apply(XSComponent u) {
/*  93 */           return AbstractAxisImpl.this.iterator(u);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean isModelGroup() {
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public Iterator<T> annotation(XSAnnotation ann) {
/* 103 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> attGroupDecl(XSAttGroupDecl decl) {
/* 107 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> attributeDecl(XSAttributeDecl decl) {
/* 111 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> attributeUse(XSAttributeUse use) {
/* 115 */     return empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> complexType(XSComplexType type) {
/* 120 */     XSParticle p = type.getContentType().asParticle();
/* 121 */     if (p != null) {
/* 122 */       return particle(p);
/*     */     }
/* 124 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> schema(XSSchema schema) {
/* 128 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> facet(XSFacet facet) {
/* 132 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> notation(XSNotation notation) {
/* 136 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> identityConstraint(XSIdentityConstraint decl) {
/* 140 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> xpath(XSXPath xpath) {
/* 144 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> simpleType(XSSimpleType simpleType) {
/* 148 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> particle(XSParticle particle) {
/* 152 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> empty(XSContentType empty) {
/* 156 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> wildcard(XSWildcard wc) {
/* 160 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> modelGroupDecl(XSModelGroupDecl decl) {
/* 164 */     return empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> modelGroup(XSModelGroup group) {
/* 169 */     return new Iterators.Map<T, XSParticle>(group.iterator()) {
/*     */         protected Iterator<? extends T> apply(XSParticle p) {
/* 171 */           return AbstractAxisImpl.this.particle(p);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<T> elementDecl(XSElementDecl decl) {
/* 177 */     return empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Iterator<T> empty() {
/* 184 */     return Iterators.empty();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\AbstractAxisImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */