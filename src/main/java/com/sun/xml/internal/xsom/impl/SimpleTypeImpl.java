/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSVariety;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SimpleTypeImpl
/*     */   extends DeclarationImpl
/*     */   implements XSSimpleType, ContentTypeImpl, Ref.SimpleType
/*     */ {
/*     */   private Ref.SimpleType baseType;
/*     */   private short redefiningCount;
/*     */   private SimpleTypeImpl redefinedBy;
/*     */   private final Set<XSVariety> finalSet;
/*     */   
/*     */   SimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _baseType) {
/*  59 */     super(_parent, _annon, _loc, _fa, _parent.getTargetNamespace(), _name, _anonymous);
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
/*  80 */     this.redefiningCount = 0;
/*     */     
/*  82 */     this.redefinedBy = null;
/*     */     this.baseType = _baseType;
/*     */     this.finalSet = finalSet;
/*  85 */   } public XSType[] listSubstitutables() { return Util.listSubstitutables((XSType)this); } public XSSimpleType getRedefinedBy() { return this.redefinedBy; }
/*     */   public void redefine(SimpleTypeImpl st) { this.baseType = st;
/*     */     st.redefinedBy = this;
/*     */     this.redefiningCount = (short)(st.redefiningCount + 1); } public int getRedefinedCount() {
/*  89 */     int i = 0;
/*  90 */     for (SimpleTypeImpl st = this.redefinedBy; st != null; st = st.redefinedBy)
/*  91 */       i++; 
/*  92 */     return i;
/*     */   }
/*     */   
/*  95 */   public XSType getBaseType() { return (XSType)this.baseType.getType(); }
/*  96 */   public XSSimpleType getSimpleBaseType() { return this.baseType.getType(); } public boolean isPrimitive() {
/*  97 */     return false;
/*     */   }
/*     */   public XSListSimpleType getBaseListType() {
/* 100 */     return getSimpleBaseType().getBaseListType();
/*     */   }
/*     */   
/*     */   public XSUnionSimpleType getBaseUnionType() {
/* 104 */     return getSimpleBaseType().getBaseUnionType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal(XSVariety v) {
/* 110 */     return this.finalSet.contains(v);
/*     */   }
/*     */   
/*     */   public final int getDerivationMethod() {
/* 114 */     return 2;
/*     */   }
/*     */   
/* 117 */   public final XSSimpleType asSimpleType() { return this; } public final XSComplexType asComplexType() {
/* 118 */     return null;
/*     */   }
/*     */   public boolean isDerivedFrom(XSType t) {
/* 121 */     SimpleTypeImpl simpleTypeImpl = this;
/*     */     while (true) {
/* 123 */       if (t == simpleTypeImpl)
/* 124 */         return true; 
/* 125 */       XSType s = simpleTypeImpl.getBaseType();
/* 126 */       if (s == simpleTypeImpl)
/* 127 */         return false; 
/* 128 */       XSType xSType1 = s;
/*     */     } 
/*     */   }
/*     */   
/* 132 */   public final boolean isSimpleType() { return true; }
/* 133 */   public final boolean isComplexType() { return false; }
/* 134 */   public final XSParticle asParticle() { return null; } public final XSContentType asEmpty() {
/* 135 */     return null;
/*     */   }
/*     */   
/* 138 */   public boolean isRestriction() { return false; }
/* 139 */   public boolean isList() { return false; }
/* 140 */   public boolean isUnion() { return false; }
/* 141 */   public XSRestrictionSimpleType asRestriction() { return null; }
/* 142 */   public XSListSimpleType asList() { return null; } public XSUnionSimpleType asUnion() {
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void visit(XSVisitor visitor) {
/* 149 */     visitor.simpleType(this);
/*     */   }
/*     */   public final void visit(XSContentTypeVisitor visitor) {
/* 152 */     visitor.simpleType(this);
/*     */   }
/*     */   public final Object apply(XSFunction function) {
/* 155 */     return function.simpleType(this);
/*     */   }
/*     */   public final Object apply(XSContentTypeFunction function) {
/* 158 */     return function.simpleType(this);
/*     */   }
/*     */   
/*     */   public XSContentType getContentType() {
/* 162 */     return this;
/*     */   } public XSSimpleType getType() {
/* 164 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\SimpleTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */