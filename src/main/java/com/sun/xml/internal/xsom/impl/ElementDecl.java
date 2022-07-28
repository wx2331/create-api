/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import com.sun.xml.internal.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
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
/*     */ public class ElementDecl
/*     */   extends DeclarationImpl
/*     */   implements XSElementDecl, Ref.Term
/*     */ {
/*     */   private XmlString defaultValue;
/*     */   private XmlString fixedValue;
/*     */   private boolean nillable;
/*     */   private boolean _abstract;
/*     */   private Ref.Type type;
/*     */   private Ref.Element substHead;
/*     */   private int substDisallowed;
/*     */   private int substExcluded;
/*     */   private final List<XSIdentityConstraint> idConstraints;
/*     */   private Boolean form;
/*     */   private Set<XSElementDecl> substitutables;
/*     */   private Set<XSElementDecl> substitutablesView;
/*     */   
/*     */   public ElementDecl(PatcherManager reader, SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl fa, String _tns, String _name, boolean _anonymous, XmlString _defv, XmlString _fixedv, boolean _nillable, boolean _abstract, Boolean _form, Ref.Type _type, Ref.Element _substHead, int _substDisallowed, int _substExcluded, List<IdentityConstraintImpl> idConstraints) {
/*  62 */     super(owner, _annon, _loc, fa, _tns, _name, _anonymous);
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
/* 133 */     this.substitutables = null;
/*     */ 
/*     */     
/* 136 */     this.substitutablesView = null; this.defaultValue = _defv; this.fixedValue = _fixedv; this.nillable = _nillable; this._abstract = _abstract; this.form = _form; this.type = _type; this.substHead = _substHead; this.substDisallowed = _substDisallowed; this.substExcluded = _substExcluded; this.idConstraints = Collections.unmodifiableList((List)idConstraints); for (IdentityConstraintImpl idc : idConstraints) idc.setParent(this);  if (this.type == null) throw new IllegalArgumentException(); 
/*     */   }
/*     */   public XmlString getDefaultValue() { return this.defaultValue; }
/* 139 */   public XmlString getFixedValue() { return this.fixedValue; } public boolean isNillable() { return this.nillable; } public boolean isAbstract() { return this._abstract; } public XSType getType() { return this.type.getType(); } public Set<? extends XSElementDecl> getSubstitutables() { if (this.substitutables == null)
/*     */     {
/*     */       
/* 142 */       this.substitutables = this.substitutablesView = Collections.singleton(this);
/*     */     }
/* 144 */     return this.substitutablesView; }
/*     */   public XSElementDecl getSubstAffiliation() { if (this.substHead == null) return null;  return this.substHead.get(); }
/*     */   public boolean isSubstitutionDisallowed(int method) { return ((this.substDisallowed & method) != 0); }
/*     */   public boolean isSubstitutionExcluded(int method) { return ((this.substExcluded & method) != 0); }
/* 148 */   public List<XSIdentityConstraint> getIdentityConstraints() { return this.idConstraints; } public Boolean getForm() { return this.form; } public XSElementDecl[] listSubstitutables() { Set<? extends XSElementDecl> s = getSubstitutables(); return s.<XSElementDecl>toArray(new XSElementDecl[s.size()]); } protected void addSubstitutable(ElementDecl decl) { if (this.substitutables == null) {
/* 149 */       this.substitutables = new HashSet<>();
/* 150 */       this.substitutables.add(this);
/* 151 */       this.substitutablesView = Collections.unmodifiableSet(this.substitutables);
/*     */     } 
/* 153 */     this.substitutables.add(decl); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSubstitutabilityMap() {
/* 158 */     ElementDecl parent = this;
/* 159 */     XSType type = getType();
/*     */     
/* 161 */     boolean rused = false;
/* 162 */     boolean eused = false;
/*     */     
/* 164 */     while ((parent = (ElementDecl)parent.getSubstAffiliation()) != null) {
/*     */       
/* 166 */       if (parent.isSubstitutionDisallowed(4)) {
/*     */         continue;
/*     */       }
/* 169 */       boolean rd = parent.isSubstitutionDisallowed(2);
/* 170 */       boolean ed = parent.isSubstitutionDisallowed(1);
/*     */       
/* 172 */       if ((rd && rused) || (ed && eused))
/*     */         continue; 
/* 174 */       XSType parentType = parent.getType();
/* 175 */       while (type != parentType) {
/* 176 */         if (type.getDerivationMethod() == 2) { rused = true; }
/* 177 */         else { eused = true; }
/*     */         
/* 179 */         type = type.getBaseType();
/* 180 */         if (type == null) {
/*     */           break;
/*     */         }
/* 183 */         if (type.isComplexType()) {
/* 184 */           rd |= type.asComplexType().isSubstitutionProhibited(2);
/* 185 */           ed |= type.asComplexType().isSubstitutionProhibited(1);
/*     */         } 
/* 187 */         if (getRoot().getAnyType().equals(type))
/*     */           break; 
/*     */       } 
/* 190 */       if ((rd && rused) || (ed && eused)) {
/*     */         continue;
/*     */       }
/* 193 */       parent.addSubstitutable(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeSubstitutedBy(XSElementDecl e) {
/* 198 */     return getSubstitutables().contains(e);
/*     */   }
/*     */   
/* 201 */   public boolean isWildcard() { return false; }
/* 202 */   public boolean isModelGroupDecl() { return false; }
/* 203 */   public boolean isModelGroup() { return false; } public boolean isElementDecl() {
/* 204 */     return true;
/*     */   }
/* 206 */   public XSWildcard asWildcard() { return null; }
/* 207 */   public XSModelGroupDecl asModelGroupDecl() { return null; }
/* 208 */   public XSModelGroup asModelGroup() { return null; } public XSElementDecl asElementDecl() {
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/* 215 */     visitor.elementDecl(this);
/*     */   }
/*     */   public void visit(XSTermVisitor visitor) {
/* 218 */     visitor.elementDecl(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 221 */     return function.elementDecl(this);
/*     */   }
/*     */   
/*     */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 225 */     return (T)function.elementDecl(this, param);
/*     */   }
/*     */   
/*     */   public Object apply(XSFunction function) {
/* 229 */     return function.elementDecl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public XSTerm getTerm() {
/* 234 */     return (XSTerm)this;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\ElementDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */