/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSVariety;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ public class RestrictionSimpleTypeImpl
/*     */   extends SimpleTypeImpl
/*     */   implements XSRestrictionSimpleType
/*     */ {
/*     */   private final List<XSFacet> facets;
/*     */   
/*     */   public RestrictionSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _baseType) {
/*  50 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, _baseType);
/*     */ 
/*     */ 
/*     */     
/*  54 */     this.facets = new ArrayList<>();
/*     */   } public void addFacet(XSFacet facet) {
/*  56 */     this.facets.add(facet);
/*     */   }
/*     */   public Iterator<XSFacet> iterateDeclaredFacets() {
/*  59 */     return this.facets.iterator();
/*     */   }
/*     */   
/*     */   public Collection<? extends XSFacet> getDeclaredFacets() {
/*  63 */     return this.facets;
/*     */   }
/*     */   
/*     */   public XSFacet getDeclaredFacet(String name) {
/*  67 */     int len = this.facets.size();
/*  68 */     for (int i = 0; i < len; i++) {
/*  69 */       XSFacet f = this.facets.get(i);
/*  70 */       if (f.getName().equals(name))
/*  71 */         return f; 
/*     */     } 
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   public List<XSFacet> getDeclaredFacets(String name) {
/*  77 */     List<XSFacet> r = new ArrayList<>();
/*  78 */     for (XSFacet f : this.facets) {
/*  79 */       if (f.getName().equals(name))
/*  80 */         r.add(f); 
/*  81 */     }  return r;
/*     */   }
/*     */   
/*     */   public XSFacet getFacet(String name) {
/*  85 */     XSFacet f = getDeclaredFacet(name);
/*  86 */     if (f != null) return f;
/*     */ 
/*     */     
/*  89 */     return getSimpleBaseType().getFacet(name);
/*     */   }
/*     */   
/*     */   public List<XSFacet> getFacets(String name) {
/*  93 */     List<XSFacet> f = getDeclaredFacets(name);
/*  94 */     if (!f.isEmpty()) return f;
/*     */ 
/*     */     
/*  97 */     return getSimpleBaseType().getFacets(name);
/*     */   }
/*     */   public XSVariety getVariety() {
/* 100 */     return getSimpleBaseType().getVariety();
/*     */   }
/*     */   public XSSimpleType getPrimitiveType() {
/* 103 */     if (isPrimitive()) return this; 
/* 104 */     return getSimpleBaseType().getPrimitiveType();
/*     */   }
/*     */   
/*     */   public boolean isPrimitive() {
/* 108 */     return (getSimpleBaseType() == (getOwnerSchema().getRoot()).anySimpleType);
/*     */   }
/*     */   
/*     */   public void visit(XSSimpleTypeVisitor visitor) {
/* 112 */     visitor.restrictionSimpleType(this);
/*     */   }
/*     */   public Object apply(XSSimpleTypeFunction function) {
/* 115 */     return function.restrictionSimpleType(this);
/*     */   }
/*     */   
/* 118 */   public boolean isRestriction() { return true; } public XSRestrictionSimpleType asRestriction() {
/* 119 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\RestrictionSimpleTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */