/*     */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.api.Mapping;
/*     */ import com.sun.tools.internal.xjc.api.S2JJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.outline.PackageOutline;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ final class JAXBModelImpl
/*     */   implements S2JJAXBModel
/*     */ {
/*     */   final Outline outline;
/*     */   private final Model model;
/*  65 */   private final Map<QName, Mapping> byXmlName = new HashMap<>();
/*     */   
/*     */   JAXBModelImpl(Outline outline) {
/*  68 */     this.model = outline.getModel();
/*  69 */     this.outline = outline;
/*     */     
/*  71 */     for (CClassInfo ci : this.model.beans().values()) {
/*  72 */       if (!ci.isElement())
/*     */         continue; 
/*  74 */       this.byXmlName.put(ci.getElementName(), new BeanMappingImpl(this, ci));
/*     */     } 
/*  76 */     for (CElementInfo ei : this.model.getElementMappings(null).values()) {
/*  77 */       this.byXmlName.put(ei.getElementName(), new ElementMappingImpl(this, ei));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public JCodeModel generateCode(Plugin[] extensions, ErrorListener errorListener) {
/*  83 */     return this.outline.getCodeModel();
/*     */   }
/*     */   
/*     */   public List<JClass> getAllObjectFactories() {
/*  87 */     List<JClass> r = new ArrayList<>();
/*  88 */     for (PackageOutline pkg : this.outline.getAllPackageContexts()) {
/*  89 */       r.add(pkg.objectFactory());
/*     */     }
/*  91 */     return r;
/*     */   }
/*     */   
/*     */   public final Mapping get(QName elementName) {
/*  95 */     return this.byXmlName.get(elementName);
/*     */   }
/*     */   
/*     */   public final Collection<? extends Mapping> getMappings() {
/*  99 */     return this.byXmlName.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeAndAnnotation getJavaType(QName xmlTypeName) {
/* 104 */     TypeUse use = (TypeUse)this.model.typeUses().get(xmlTypeName);
/* 105 */     if (use == null) return null;
/*     */     
/* 107 */     return new TypeAndAnnotationImpl(this.outline, use);
/*     */   }
/*     */   
/*     */   public final List<String> getClassList() {
/* 111 */     List<String> classList = new ArrayList<>();
/*     */ 
/*     */     
/* 114 */     for (PackageOutline p : this.outline.getAllPackageContexts())
/* 115 */       classList.add(p.objectFactory().fullName()); 
/* 116 */     return classList;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\impl\s2j\JAXBModelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */