/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.SCD;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSVariety;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Iterators;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaSetImpl
/*     */   implements XSSchemaSet
/*     */ {
/*  73 */   private final Map<String, XSSchema> schemas = new HashMap<>();
/*  74 */   private final Vector<XSSchema> schemas2 = new Vector<>();
/*  75 */   private final List<XSSchema> readonlySchemaList = Collections.unmodifiableList(this.schemas2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaImpl createSchema(String targetNamespace, Locator location) {
/*  82 */     SchemaImpl obj = (SchemaImpl)this.schemas.get(targetNamespace);
/*  83 */     if (obj == null) {
/*  84 */       obj = new SchemaImpl(this, location, targetNamespace);
/*  85 */       this.schemas.put(targetNamespace, obj);
/*  86 */       this.schemas2.add(obj);
/*     */     } 
/*  88 */     return obj;
/*     */   }
/*     */   
/*     */   public int getSchemaSize() {
/*  92 */     return this.schemas.size();
/*     */   }
/*     */   public XSSchema getSchema(String targetNamespace) {
/*  95 */     return this.schemas.get(targetNamespace);
/*     */   }
/*     */   public XSSchema getSchema(int idx) {
/*  98 */     return this.schemas2.get(idx);
/*     */   }
/*     */   public Iterator<XSSchema> iterateSchema() {
/* 101 */     return this.schemas2.iterator();
/*     */   }
/*     */   
/*     */   public final Collection<XSSchema> getSchemas() {
/* 105 */     return this.readonlySchemaList;
/*     */   }
/*     */   
/*     */   public XSType getType(String ns, String localName) {
/* 109 */     XSSchema schema = getSchema(ns);
/* 110 */     if (schema == null) return null;
/*     */     
/* 112 */     return schema.getType(localName);
/*     */   }
/*     */   
/*     */   public XSSimpleType getSimpleType(String ns, String localName) {
/* 116 */     XSSchema schema = getSchema(ns);
/* 117 */     if (schema == null) return null;
/*     */     
/* 119 */     return schema.getSimpleType(localName);
/*     */   }
/*     */   
/*     */   public XSElementDecl getElementDecl(String ns, String localName) {
/* 123 */     XSSchema schema = getSchema(ns);
/* 124 */     if (schema == null) return null;
/*     */     
/* 126 */     return schema.getElementDecl(localName);
/*     */   }
/*     */   
/*     */   public XSAttributeDecl getAttributeDecl(String ns, String localName) {
/* 130 */     XSSchema schema = getSchema(ns);
/* 131 */     if (schema == null) return null;
/*     */     
/* 133 */     return schema.getAttributeDecl(localName);
/*     */   }
/*     */   
/*     */   public XSModelGroupDecl getModelGroupDecl(String ns, String localName) {
/* 137 */     XSSchema schema = getSchema(ns);
/* 138 */     if (schema == null) return null;
/*     */     
/* 140 */     return schema.getModelGroupDecl(localName);
/*     */   }
/*     */   
/*     */   public XSAttGroupDecl getAttGroupDecl(String ns, String localName) {
/* 144 */     XSSchema schema = getSchema(ns);
/* 145 */     if (schema == null) return null;
/*     */     
/* 147 */     return schema.getAttGroupDecl(localName);
/*     */   }
/*     */   
/*     */   public XSComplexType getComplexType(String ns, String localName) {
/* 151 */     XSSchema schema = getSchema(ns);
/* 152 */     if (schema == null) return null;
/*     */     
/* 154 */     return schema.getComplexType(localName);
/*     */   }
/*     */   
/*     */   public XSIdentityConstraint getIdentityConstraint(String ns, String localName) {
/* 158 */     XSSchema schema = getSchema(ns);
/* 159 */     if (schema == null) return null;
/*     */     
/* 161 */     return schema.getIdentityConstraint(localName);
/*     */   }
/*     */   
/*     */   public Iterator<XSElementDecl> iterateElementDecls() {
/* 165 */     return (Iterator)new Iterators.Map<XSElementDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSElementDecl> apply(XSSchema u) {
/* 167 */           return u.iterateElementDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<XSType> iterateTypes() {
/* 173 */     return (Iterator)new Iterators.Map<XSType, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSType> apply(XSSchema u) {
/* 175 */           return u.iterateTypes();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<XSAttributeDecl> iterateAttributeDecls() {
/* 181 */     return (Iterator)new Iterators.Map<XSAttributeDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSAttributeDecl> apply(XSSchema u) {
/* 183 */           return u.iterateAttributeDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSAttGroupDecl> iterateAttGroupDecls() {
/* 188 */     return (Iterator)new Iterators.Map<XSAttGroupDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSAttGroupDecl> apply(XSSchema u) {
/* 190 */           return u.iterateAttGroupDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSModelGroupDecl> iterateModelGroupDecls() {
/* 195 */     return (Iterator)new Iterators.Map<XSModelGroupDecl, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSModelGroupDecl> apply(XSSchema u) {
/* 197 */           return u.iterateModelGroupDecls();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSSimpleType> iterateSimpleTypes() {
/* 202 */     return (Iterator)new Iterators.Map<XSSimpleType, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSSimpleType> apply(XSSchema u) {
/* 204 */           return u.iterateSimpleTypes();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSComplexType> iterateComplexTypes() {
/* 209 */     return (Iterator)new Iterators.Map<XSComplexType, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSComplexType> apply(XSSchema u) {
/* 211 */           return u.iterateComplexTypes();
/*     */         }
/*     */       };
/*     */   }
/*     */   public Iterator<XSNotation> iterateNotations() {
/* 216 */     return (Iterator)new Iterators.Map<XSNotation, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSNotation> apply(XSSchema u) {
/* 218 */           return u.iterateNotations();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<XSIdentityConstraint> iterateIdentityConstraints() {
/* 224 */     return (Iterator)new Iterators.Map<XSIdentityConstraint, XSSchema>(iterateSchema()) {
/*     */         protected Iterator<XSIdentityConstraint> apply(XSSchema u) {
/* 226 */           return u.getIdentityConstraints().values().iterator();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Collection<XSComponent> select(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 233 */       return SCD.create(scd, nsContext).select(this);
/* 234 */     } catch (ParseException e) {
/* 235 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public XSComponent selectSingle(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 241 */       return SCD.create(scd, nsContext).selectSingle(this);
/* 242 */     } catch (ParseException e) {
/* 243 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 248 */   public final EmptyImpl empty = new EmptyImpl(); public XSContentType getEmpty() {
/* 249 */     return this.empty;
/*     */   } public XSSimpleType getAnySimpleType() {
/* 251 */     return (XSSimpleType)this.anySimpleType;
/* 252 */   } public final AnySimpleType anySimpleType = new AnySimpleType();
/*     */   
/*     */   private class AnySimpleType
/*     */     extends DeclarationImpl implements XSRestrictionSimpleType, Ref.SimpleType {
/*     */     AnySimpleType() {
/* 257 */       super((SchemaDocumentImpl)null, (AnnotationImpl)null, (Locator)null, (ForeignAttributesImpl)null, "http://www.w3.org/2001/XMLSchema", "anySimpleType", false);
/*     */     }
/*     */     public SchemaImpl getOwnerSchema() {
/* 260 */       return SchemaSetImpl.this.createSchema("http://www.w3.org/2001/XMLSchema", null);
/*     */     }
/* 262 */     public XSSimpleType asSimpleType() { return (XSSimpleType)this; } public XSComplexType asComplexType() {
/* 263 */       return null;
/*     */     }
/*     */     public boolean isDerivedFrom(XSType t) {
/* 266 */       return (t == this || t == SchemaSetImpl.this.anyType);
/*     */     }
/*     */     
/* 269 */     public boolean isSimpleType() { return true; }
/* 270 */     public boolean isComplexType() { return false; }
/* 271 */     public XSContentType asEmpty() { return null; }
/* 272 */     public XSParticle asParticle() { return null; }
/* 273 */     public XSType getBaseType() { return (XSType)SchemaSetImpl.this.anyType; }
/* 274 */     public XSSimpleType getSimpleBaseType() { return null; }
/* 275 */     public int getDerivationMethod() { return 2; }
/* 276 */     public Iterator<XSFacet> iterateDeclaredFacets() { return Iterators.empty(); }
/* 277 */     public Collection<? extends XSFacet> getDeclaredFacets() { return Collections.EMPTY_LIST; }
/* 278 */     public void visit(XSSimpleTypeVisitor visitor) { visitor.restrictionSimpleType(this); }
/* 279 */     public void visit(XSContentTypeVisitor visitor) { visitor.simpleType((XSSimpleType)this); }
/* 280 */     public void visit(XSVisitor visitor) { visitor.simpleType((XSSimpleType)this); }
/* 281 */     public <T> T apply(XSSimpleTypeFunction<T> f) { return (T)f.restrictionSimpleType(this); }
/* 282 */     public <T> T apply(XSContentTypeFunction<T> f) { return (T)f.simpleType((XSSimpleType)this); }
/* 283 */     public <T> T apply(XSFunction<T> f) { return (T)f.simpleType((XSSimpleType)this); }
/* 284 */     public XSVariety getVariety() { return XSVariety.ATOMIC; }
/* 285 */     public XSSimpleType getPrimitiveType() { return (XSSimpleType)this; }
/* 286 */     public boolean isPrimitive() { return true; }
/* 287 */     public XSListSimpleType getBaseListType() { return null; }
/* 288 */     public XSUnionSimpleType getBaseUnionType() { return null; }
/* 289 */     public XSFacet getFacet(String name) { return null; }
/* 290 */     public List<XSFacet> getFacets(String name) { return Collections.EMPTY_LIST; }
/* 291 */     public XSFacet getDeclaredFacet(String name) { return null; } public List<XSFacet> getDeclaredFacets(String name) {
/* 292 */       return Collections.EMPTY_LIST;
/*     */     }
/* 294 */     public boolean isRestriction() { return true; }
/* 295 */     public boolean isList() { return false; }
/* 296 */     public boolean isUnion() { return false; }
/* 297 */     public boolean isFinal(XSVariety v) { return false; }
/* 298 */     public XSRestrictionSimpleType asRestriction() { return this; }
/* 299 */     public XSListSimpleType asList() { return null; }
/* 300 */     public XSUnionSimpleType asUnion() { return null; }
/* 301 */     public XSSimpleType getType() { return (XSSimpleType)this; }
/* 302 */     public XSSimpleType getRedefinedBy() { return null; } public int getRedefinedCount() {
/* 303 */       return 0;
/*     */     }
/*     */     public XSType[] listSubstitutables() {
/* 306 */       return Util.listSubstitutables((XSType)this);
/*     */     } }
/*     */   
/*     */   public XSComplexType getAnyType() {
/* 310 */     return this.anyType;
/* 311 */   } public final AnyType anyType = new AnyType();
/*     */   private class AnyType extends DeclarationImpl implements XSComplexType, Ref.Type { private final WildcardImpl anyWildcard; private final XSContentType contentType;
/*     */     
/* 314 */     AnyType() { super((SchemaDocumentImpl)null, (AnnotationImpl)null, (Locator)null, (ForeignAttributesImpl)null, "http://www.w3.org/2001/XMLSchema", "anyType", false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 360 */       this.anyWildcard = new WildcardImpl.Any(null, null, null, null, 3);
/* 361 */       this.contentType = new ParticleImpl(null, null, new ModelGroupImpl(null, null, null, null, XSModelGroup.SEQUENCE, new ParticleImpl[] { new ParticleImpl(null, null, this.anyWildcard, null, -1, 0) }), null, 1, 1); }
/*     */     public SchemaImpl getOwnerSchema() { return SchemaSetImpl.this.createSchema("http://www.w3.org/2001/XMLSchema", null); }
/*     */     public boolean isAbstract() { return false; }
/*     */     public XSWildcard getAttributeWildcard() { return this.anyWildcard; }
/*     */     public XSAttributeUse getAttributeUse(String nsURI, String localName) { return null; }
/*     */     public Iterator<XSAttributeUse> iterateAttributeUses() { return Iterators.empty(); }
/*     */     public XSAttributeUse getDeclaredAttributeUse(String nsURI, String localName) { return null; }
/*     */     public Iterator<XSAttributeUse> iterateDeclaredAttributeUses() { return Iterators.empty(); }
/* 369 */     public Iterator<XSAttGroupDecl> iterateAttGroups() { return Iterators.empty(); } public Collection<XSAttributeUse> getAttributeUses() { return Collections.EMPTY_LIST; } public Collection<? extends XSAttributeUse> getDeclaredAttributeUses() { return Collections.EMPTY_LIST; } public Collection<? extends XSAttGroupDecl> getAttGroups() { return Collections.EMPTY_LIST; } public boolean isFinal(int i) { return false; } public boolean isSubstitutionProhibited(int i) { return false; } public boolean isMixed() { return true; } public XSContentType getContentType() { return this.contentType; } public List<XSComplexType> getSubtypes() { ArrayList<XSComplexType> subtypeList = new ArrayList();
/* 370 */       Iterator<XSComplexType> cTypes = getRoot().iterateComplexTypes();
/* 371 */       while (cTypes.hasNext()) {
/* 372 */         XSComplexType cType = cTypes.next();
/* 373 */         XSType base = cType.getBaseType();
/* 374 */         if (base != null && base.equals(this)) {
/* 375 */           subtypeList.add(cType);
/*     */         }
/*     */       } 
/* 378 */       return subtypeList; }
/*     */     public XSContentType getExplicitContent() { return null; }
/*     */     public XSType getBaseType() { return (XSType)this; }
/*     */     public XSSimpleType asSimpleType() { return null; }
/* 382 */     public XSComplexType asComplexType() { return this; } public boolean isDerivedFrom(XSType t) { return (t == this); } public boolean isSimpleType() { return false; } public boolean isComplexType() { return true; } public XSContentType asEmpty() { return null; } public int getDerivationMethod() { return 2; } public XSElementDecl getScope() { return null; } public void visit(XSVisitor visitor) { visitor.complexType(this); } public <T> T apply(XSFunction<T> f) { return (T)f.complexType(this); } public XSType getType() { return (XSType)this; } public XSComplexType getRedefinedBy() { return null; } public int getRedefinedCount() { return 0; } public XSType[] listSubstitutables() { return Util.listSubstitutables((XSType)this); } public List<XSElementDecl> getElementDecls() { ArrayList<XSElementDecl> declList = new ArrayList();
/* 383 */       XSSchemaSet schemaSet = getRoot();
/* 384 */       for (XSSchema sch : schemaSet.getSchemas()) {
/* 385 */         for (XSElementDecl decl : sch.getElementDecls().values()) {
/* 386 */           if (decl.getType().equals(this)) {
/* 387 */             declList.add(decl);
/*     */           }
/*     */         } 
/*     */       } 
/* 391 */       return declList; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\SchemaSetImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */