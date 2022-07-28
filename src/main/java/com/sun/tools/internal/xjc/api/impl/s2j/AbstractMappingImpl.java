/*     */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.api.Mapping;
/*     */ import com.sun.tools.internal.xjc.api.Property;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractMappingImpl<InfoT extends CElement>
/*     */   implements Mapping
/*     */ {
/*     */   protected final JAXBModelImpl parent;
/*     */   protected final InfoT clazz;
/*  67 */   private List<Property> drilldown = null;
/*     */   private boolean drilldownComputed = false;
/*     */   
/*     */   protected AbstractMappingImpl(JAXBModelImpl parent, InfoT clazz) {
/*  71 */     this.parent = parent;
/*  72 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public final QName getElement() {
/*  76 */     return this.clazz.getElementName();
/*     */   }
/*     */   
/*     */   public final String getClazz() {
/*  80 */     return ((NType)this.clazz.getType()).fullName();
/*     */   }
/*     */   
/*     */   public final List<? extends Property> getWrapperStyleDrilldown() {
/*  84 */     if (!this.drilldownComputed) {
/*  85 */       this.drilldownComputed = true;
/*  86 */       this.drilldown = calcDrilldown();
/*     */     } 
/*  88 */     return this.drilldown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract List<Property> calcDrilldown();
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Property> buildDrilldown(CClassInfo typeBean) {
/*     */     List<Property> result;
/* 100 */     if (containingChoice(typeBean)) {
/* 101 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 106 */     CClassInfo bc = typeBean.getBaseClass();
/* 107 */     if (bc != null) {
/* 108 */       result = buildDrilldown(bc);
/* 109 */       if (result == null) {
/* 110 */         return null;
/*     */       }
/*     */     } else {
/* 113 */       result = new ArrayList<>();
/*     */     } 
/*     */     
/* 116 */     for (CPropertyInfo p : typeBean.getProperties()) {
/* 117 */       if (p instanceof CElementPropertyInfo) {
/* 118 */         CElementPropertyInfo ep = (CElementPropertyInfo)p;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 124 */         List<? extends CTypeRef> ref = ep.getTypes();
/* 125 */         if (ref.size() != 1) {
/* 126 */           return null;
/*     */         }
/*     */         
/* 129 */         result.add(createPropertyImpl((CPropertyInfo)ep, ((CTypeRef)ref.get(0)).getTagName())); continue;
/* 130 */       }  if (p instanceof com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo) {
/* 131 */         ElementAdapter fr; CReferencePropertyInfo rp = (CReferencePropertyInfo)p;
/*     */         
/* 133 */         Collection<CElement> elements = rp.getElements();
/* 134 */         if (elements.size() != 1) {
/* 135 */           return null;
/*     */         }
/*     */         
/* 138 */         CElement ref = elements.iterator().next();
/* 139 */         if (ref instanceof com.sun.xml.internal.bind.v2.model.core.ClassInfo) {
/* 140 */           result.add(createPropertyImpl((CPropertyInfo)rp, ref.getElementName())); continue;
/*     */         } 
/* 142 */         CElementInfo eref = (CElementInfo)ref;
/* 143 */         if (!eref.getSubstitutionMembers().isEmpty()) {
/* 144 */           return null;
/*     */         }
/*     */ 
/*     */         
/* 148 */         if (rp.isCollection()) {
/* 149 */           fr = new ElementCollectionAdapter(this.parent.outline.getField((CPropertyInfo)rp), eref);
/*     */         } else {
/* 151 */           fr = new ElementSingleAdapter(this.parent.outline.getField((CPropertyInfo)rp), eref);
/*     */         } 
/*     */         
/* 154 */         result.add(new PropertyImpl(this, fr, eref
/* 155 */               .getElementName()));
/*     */         
/*     */         continue;
/*     */       } 
/* 159 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 164 */     return result;
/*     */   }
/*     */   
/*     */   private boolean containingChoice(CClassInfo typeBean) {
/* 168 */     XSComponent component = typeBean.getSchemaComponent();
/* 169 */     if (component instanceof XSComplexType) {
/* 170 */       XSContentType contentType = ((XSComplexType)component).getContentType();
/* 171 */       XSParticle particle = contentType.asParticle();
/* 172 */       if (particle != null) {
/* 173 */         XSTerm term = particle.getTerm();
/* 174 */         XSModelGroup modelGroup = term.asModelGroup();
/* 175 */         if (modelGroup != null) {
/* 176 */           return (modelGroup.getCompositor() == XSModelGroup.Compositor.CHOICE);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   private Property createPropertyImpl(CPropertyInfo p, QName tagName) {
/* 185 */     return new PropertyImpl(this, this.parent.outline
/* 186 */         .getField(p), tagName);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\impl\s2j\AbstractMappingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */