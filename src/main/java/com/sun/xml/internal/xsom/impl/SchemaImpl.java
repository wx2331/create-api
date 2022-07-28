/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.ForeignAttributes;
/*     */ import com.sun.xml.internal.xsom.SCD;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.parser.SchemaDocument;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class SchemaImpl
/*     */   implements XSSchema
/*     */ {
/*     */   protected final SchemaSetImpl parent;
/*     */   private final String targetNamespace;
/*     */   private XSAnnotation annotation;
/*     */   private final Locator locator;
/*     */   
/*     */   public SchemaImpl(SchemaSetImpl _parent, Locator loc, String tns) {
/*  60 */     if (tns == null)
/*     */     {
/*  62 */       throw new IllegalArgumentException(); } 
/*  63 */     this.targetNamespace = tns;
/*  64 */     this.parent = _parent;
/*  65 */     this.locator = loc;
/*     */   }
/*     */   
/*     */   public SchemaDocument getSourceDocument() {
/*  69 */     return null;
/*     */   }
/*     */   
/*     */   public SchemaSetImpl getRoot() {
/*  73 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetNamespace() {
/*  80 */     return this.targetNamespace;
/*     */   }
/*     */   
/*     */   public XSSchema getOwnerSchema() {
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnnotation(XSAnnotation a) {
/*  89 */     this.annotation = a;
/*     */   }
/*     */   public XSAnnotation getAnnotation() {
/*  92 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public XSAnnotation getAnnotation(boolean createIfNotExist) {
/*  96 */     if (createIfNotExist && this.annotation == null)
/*  97 */       this.annotation = new AnnotationImpl(); 
/*  98 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locator getLocator() {
/* 109 */     return this.locator;
/*     */   }
/*     */ 
/*     */   
/* 113 */   private final Map<String, XSAttributeDecl> atts = new HashMap<>();
/* 114 */   private final Map<String, XSAttributeDecl> attsView = Collections.unmodifiableMap(this.atts);
/*     */   public void addAttributeDecl(XSAttributeDecl newDecl) {
/* 116 */     this.atts.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public Map<String, XSAttributeDecl> getAttributeDecls() {
/* 119 */     return this.attsView;
/*     */   }
/*     */   public XSAttributeDecl getAttributeDecl(String name) {
/* 122 */     return this.atts.get(name);
/*     */   }
/*     */   public Iterator<XSAttributeDecl> iterateAttributeDecls() {
/* 125 */     return this.atts.values().iterator();
/*     */   }
/*     */   
/* 128 */   private final Map<String, XSElementDecl> elems = new HashMap<>();
/* 129 */   private final Map<String, XSElementDecl> elemsView = Collections.unmodifiableMap(this.elems);
/*     */   public void addElementDecl(XSElementDecl newDecl) {
/* 131 */     this.elems.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public Map<String, XSElementDecl> getElementDecls() {
/* 134 */     return this.elemsView;
/*     */   }
/*     */   public XSElementDecl getElementDecl(String name) {
/* 137 */     return this.elems.get(name);
/*     */   }
/*     */   public Iterator<XSElementDecl> iterateElementDecls() {
/* 140 */     return this.elems.values().iterator();
/*     */   }
/*     */   
/* 143 */   private final Map<String, XSAttGroupDecl> attGroups = new HashMap<>();
/* 144 */   private final Map<String, XSAttGroupDecl> attGroupsView = Collections.unmodifiableMap(this.attGroups);
/*     */   public void addAttGroupDecl(XSAttGroupDecl newDecl, boolean overwrite) {
/* 146 */     if (overwrite || !this.attGroups.containsKey(newDecl.getName()))
/* 147 */       this.attGroups.put(newDecl.getName(), newDecl); 
/*     */   }
/*     */   public Map<String, XSAttGroupDecl> getAttGroupDecls() {
/* 150 */     return this.attGroupsView;
/*     */   }
/*     */   public XSAttGroupDecl getAttGroupDecl(String name) {
/* 153 */     return this.attGroups.get(name);
/*     */   }
/*     */   public Iterator<XSAttGroupDecl> iterateAttGroupDecls() {
/* 156 */     return this.attGroups.values().iterator();
/*     */   }
/*     */ 
/*     */   
/* 160 */   private final Map<String, XSNotation> notations = new HashMap<>();
/* 161 */   private final Map<String, XSNotation> notationsView = Collections.unmodifiableMap(this.notations);
/*     */   public void addNotation(XSNotation newDecl) {
/* 163 */     this.notations.put(newDecl.getName(), newDecl);
/*     */   }
/*     */   public Map<String, XSNotation> getNotations() {
/* 166 */     return this.notationsView;
/*     */   }
/*     */   public XSNotation getNotation(String name) {
/* 169 */     return this.notations.get(name);
/*     */   }
/*     */   public Iterator<XSNotation> iterateNotations() {
/* 172 */     return this.notations.values().iterator();
/*     */   }
/*     */   
/* 175 */   private final Map<String, XSModelGroupDecl> modelGroups = new HashMap<>();
/* 176 */   private final Map<String, XSModelGroupDecl> modelGroupsView = Collections.unmodifiableMap(this.modelGroups);
/*     */   public void addModelGroupDecl(XSModelGroupDecl newDecl, boolean overwrite) {
/* 178 */     if (overwrite || !this.modelGroups.containsKey(newDecl.getName()))
/* 179 */       this.modelGroups.put(newDecl.getName(), newDecl); 
/*     */   }
/*     */   public Map<String, XSModelGroupDecl> getModelGroupDecls() {
/* 182 */     return this.modelGroupsView;
/*     */   }
/*     */   public XSModelGroupDecl getModelGroupDecl(String name) {
/* 185 */     return this.modelGroups.get(name);
/*     */   }
/*     */   public Iterator<XSModelGroupDecl> iterateModelGroupDecls() {
/* 188 */     return this.modelGroups.values().iterator();
/*     */   }
/*     */ 
/*     */   
/* 192 */   private final Map<String, XSIdentityConstraint> idConstraints = new HashMap<>();
/* 193 */   private final Map<String, XSIdentityConstraint> idConstraintsView = Collections.unmodifiableMap(this.idConstraints);
/*     */   
/*     */   protected void addIdentityConstraint(IdentityConstraintImpl c) {
/* 196 */     this.idConstraints.put(c.getName(), c);
/*     */   }
/*     */   
/*     */   public Map<String, XSIdentityConstraint> getIdentityConstraints() {
/* 200 */     return this.idConstraintsView;
/*     */   }
/*     */   
/*     */   public XSIdentityConstraint getIdentityConstraint(String localName) {
/* 204 */     return this.idConstraints.get(localName);
/*     */   }
/*     */   
/* 207 */   private final Map<String, XSType> allTypes = new HashMap<>();
/* 208 */   private final Map<String, XSType> allTypesView = Collections.unmodifiableMap(this.allTypes);
/*     */   
/* 210 */   private final Map<String, XSSimpleType> simpleTypes = new HashMap<>();
/* 211 */   private final Map<String, XSSimpleType> simpleTypesView = Collections.unmodifiableMap(this.simpleTypes);
/*     */   public void addSimpleType(XSSimpleType newDecl, boolean overwrite) {
/* 213 */     if (overwrite || !this.simpleTypes.containsKey(newDecl.getName())) {
/* 214 */       this.simpleTypes.put(newDecl.getName(), newDecl);
/* 215 */       this.allTypes.put(newDecl.getName(), newDecl);
/*     */     } 
/*     */   }
/*     */   public Map<String, XSSimpleType> getSimpleTypes() {
/* 219 */     return this.simpleTypesView;
/*     */   }
/*     */   public XSSimpleType getSimpleType(String name) {
/* 222 */     return this.simpleTypes.get(name);
/*     */   }
/*     */   public Iterator<XSSimpleType> iterateSimpleTypes() {
/* 225 */     return this.simpleTypes.values().iterator();
/*     */   }
/*     */   
/* 228 */   private final Map<String, XSComplexType> complexTypes = new HashMap<>();
/* 229 */   private final Map<String, XSComplexType> complexTypesView = Collections.unmodifiableMap(this.complexTypes);
/*     */   public void addComplexType(XSComplexType newDecl, boolean overwrite) {
/* 231 */     if (overwrite || !this.complexTypes.containsKey(newDecl.getName())) {
/* 232 */       this.complexTypes.put(newDecl.getName(), newDecl);
/* 233 */       this.allTypes.put(newDecl.getName(), newDecl);
/*     */     } 
/*     */   }
/*     */   public Map<String, XSComplexType> getComplexTypes() {
/* 237 */     return this.complexTypesView;
/*     */   }
/*     */   public XSComplexType getComplexType(String name) {
/* 240 */     return this.complexTypes.get(name);
/*     */   }
/*     */   public Iterator<XSComplexType> iterateComplexTypes() {
/* 243 */     return this.complexTypes.values().iterator();
/*     */   }
/*     */   
/*     */   public Map<String, XSType> getTypes() {
/* 247 */     return this.allTypesView;
/*     */   }
/*     */   public XSType getType(String name) {
/* 250 */     return this.allTypes.get(name);
/*     */   }
/*     */   public Iterator<XSType> iterateTypes() {
/* 253 */     return this.allTypes.values().iterator();
/*     */   }
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/* 257 */     visitor.schema(this);
/*     */   }
/*     */   
/*     */   public Object apply(XSFunction function) {
/* 261 */     return function.schema(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 267 */   private List<ForeignAttributes> foreignAttributes = null;
/* 268 */   private List<ForeignAttributes> readOnlyForeignAttributes = null;
/*     */   
/*     */   public void addForeignAttributes(ForeignAttributesImpl fa) {
/* 271 */     if (this.foreignAttributes == null)
/* 272 */       this.foreignAttributes = new ArrayList<>(); 
/* 273 */     this.foreignAttributes.add(fa);
/*     */   }
/*     */   
/*     */   public List<ForeignAttributes> getForeignAttributes() {
/* 277 */     if (this.readOnlyForeignAttributes == null)
/* 278 */       if (this.foreignAttributes == null) {
/* 279 */         this.readOnlyForeignAttributes = Collections.EMPTY_LIST;
/*     */       } else {
/* 281 */         this.readOnlyForeignAttributes = Collections.unmodifiableList(this.foreignAttributes);
/*     */       }  
/* 283 */     return this.readOnlyForeignAttributes;
/*     */   }
/*     */   
/*     */   public String getForeignAttribute(String nsUri, String localName) {
/* 287 */     for (ForeignAttributes fa : getForeignAttributes()) {
/* 288 */       String v = fa.getValue(nsUri, localName);
/* 289 */       if (v != null) return v; 
/*     */     } 
/* 291 */     return null;
/*     */   }
/*     */   
/*     */   public Collection<XSComponent> select(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 296 */       return SCD.create(scd, nsContext).select((XSComponent)this);
/* 297 */     } catch (ParseException e) {
/* 298 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public XSComponent selectSingle(String scd, NamespaceContext nsContext) {
/*     */     try {
/* 304 */       return SCD.create(scd, nsContext).selectSingle((XSComponent)this);
/* 305 */     } catch (ParseException e) {
/* 306 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\SchemaImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */