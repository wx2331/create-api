/*     */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.ModelException;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaStructureType;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAXBStructuredType
/*     */   extends JAXBType
/*     */ {
/*     */   private List _elementMembers;
/*     */   private Map _elementMembersByName;
/*     */   private Set _subtypes;
/*     */   private JAXBStructuredType _parentType;
/*     */   
/*     */   public JAXBStructuredType(JAXBType jaxbType) {
/*  44 */     super(jaxbType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     this._elementMembers = new ArrayList();
/* 135 */     this._elementMembersByName = new HashMap<>();
/* 136 */     this._subtypes = null;
/* 137 */     this._parentType = null; } public JAXBStructuredType() { this._elementMembers = new ArrayList(); this._elementMembersByName = new HashMap<>(); this._subtypes = null; this._parentType = null; } public JAXBStructuredType(QName name, JavaStructureType javaType) { super(name, (JavaType)javaType); this._elementMembers = new ArrayList(); this._elementMembersByName = new HashMap<>(); this._subtypes = null; this._parentType = null; }
/*     */ 
/*     */   
/*     */   public JAXBStructuredType(QName name) {
/*     */     this(name, null);
/*     */   }
/*     */   
/*     */   public void add(JAXBElementMember m) {
/*     */     if (this._elementMembersByName.containsKey(m.getName()))
/*     */       throw new ModelException("model.uniqueness", new Object[0]); 
/*     */     this._elementMembers.add(m);
/*     */     if (m.getName() != null)
/*     */       this._elementMembersByName.put(m.getName().getLocalPart(), m); 
/*     */   }
/*     */   
/*     */   public Iterator getElementMembers() {
/*     */     return this._elementMembers.iterator();
/*     */   }
/*     */   
/*     */   public int getElementMembersCount() {
/*     */     return this._elementMembers.size();
/*     */   }
/*     */   
/*     */   public List getElementMembersList() {
/*     */     return this._elementMembers;
/*     */   }
/*     */   
/*     */   public void setElementMembersList(List l) {
/*     */     this._elementMembers = l;
/*     */   }
/*     */   
/*     */   public void addSubtype(JAXBStructuredType type) {
/*     */     if (this._subtypes == null)
/*     */       this._subtypes = new HashSet(); 
/*     */     this._subtypes.add(type);
/*     */     type.setParentType(this);
/*     */   }
/*     */   
/*     */   public Iterator getSubtypes() {
/*     */     if (this._subtypes != null)
/*     */       return this._subtypes.iterator(); 
/*     */     return null;
/*     */   }
/*     */   
/*     */   public boolean isUnwrapped() {
/*     */     return true;
/*     */   }
/*     */   
/*     */   public Set getSubtypesSet() {
/*     */     return this._subtypes;
/*     */   }
/*     */   
/*     */   public void setSubtypesSet(Set s) {
/*     */     this._subtypes = s;
/*     */   }
/*     */   
/*     */   public void setParentType(JAXBStructuredType parent) {
/*     */     if (this._parentType != null && parent != null && !this._parentType.equals(parent))
/*     */       throw new ModelException("model.parent.type.already.set", new Object[] { getName().toString(), this._parentType.getName().toString(), parent.getName().toString() }); 
/*     */     this._parentType = parent;
/*     */   }
/*     */   
/*     */   public JAXBStructuredType getParentType() {
/*     */     return this._parentType;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\jaxb\JAXBStructuredType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */