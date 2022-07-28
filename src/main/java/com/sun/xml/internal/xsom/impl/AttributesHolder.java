/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.impl.scd.Iterators;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class AttributesHolder
/*     */   extends DeclarationImpl
/*     */ {
/*     */   protected final Map<UName, AttributeUseImpl> attributes;
/*     */   protected final Set<UName> prohibitedAtts;
/*     */   protected final Set<Ref.AttGroup> attGroups;
/*     */   
/*     */   public void addAttributeUse(UName name, AttributeUseImpl a) {
/*     */     this.attributes.put(name, a);
/*     */   }
/*     */   
/*     */   public void addProhibitedAttribute(UName name) {
/*     */     this.prohibitedAtts.add(name);
/*     */   }
/*     */   
/*     */   protected AttributesHolder(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous) {
/*  51 */     super(_parent, _annon, loc, _fa, _parent.getTargetNamespace(), _name, _anonymous);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     this.attributes = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     this.prohibitedAtts = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     this.attGroups = new HashSet<>();
/*     */   } public Collection<XSAttributeUse> getAttributeUses() { List<XSAttributeUse> v = new ArrayList<>(); v.addAll(this.attributes.values()); for (XSAttGroupDecl agd : getAttGroups())
/* 106 */       v.addAll(agd.getAttributeUses());  return v; } public void addAttGroup(Ref.AttGroup a) { this.attGroups.add(a); }
/*     */   public Iterator<XSAttributeUse> iterateAttributeUses() {
/*     */     return getAttributeUses().iterator();
/*     */   } public XSAttributeUse getDeclaredAttributeUse(String nsURI, String localName) {
/*     */     return this.attributes.get(new UName(nsURI, localName));
/* 111 */   } public Iterator<XSAttGroupDecl> iterateAttGroups() { return (Iterator)new Iterators.Adapter<XSAttGroupDecl, Ref.AttGroup>(this.attGroups.iterator()) {
/*     */         protected XSAttGroupDecl filter(Ref.AttGroup u) {
/* 113 */           return u.get(); }
/*     */       }; } public Iterator<AttributeUseImpl> iterateDeclaredAttributeUses() {
/*     */     return this.attributes.values().iterator();
/*     */   } public Collection<AttributeUseImpl> getDeclaredAttributeUses() {
/*     */     return this.attributes.values();
/*     */   } public Set<XSAttGroupDecl> getAttGroups() {
/* 119 */     return new AbstractSet<XSAttGroupDecl>() {
/*     */         public Iterator<XSAttGroupDecl> iterator() {
/* 121 */           return AttributesHolder.this.iterateAttGroups();
/*     */         }
/*     */         
/*     */         public int size() {
/* 125 */           return AttributesHolder.this.attGroups.size();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public abstract void setWildcard(WildcardImpl paramWildcardImpl);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\AttributesHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */