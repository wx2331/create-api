/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClass;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.internal.xsom.XSAttContainer;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MixedComplexTypeBuilder
/*     */   extends CTBuilder
/*     */ {
/*     */   public boolean isApplicable(XSComplexType ct) {
/*  46 */     XSType bt = ct.getBaseType();
/*  47 */     if (bt == this.schemas.getAnyType() && ct.isMixed()) {
/*  48 */       return true;
/*     */     }
/*     */     
/*  51 */     if (bt.isComplexType() && 
/*  52 */       !bt.asComplexType().isMixed() && ct
/*  53 */       .isMixed() && ct
/*  54 */       .getDerivationMethod() == 1) {
/*  55 */       if (!this.bgmBuilder.isGenerateMixedExtensions() && ct.getContentType().asParticle() == null) {
/*  56 */         return false;
/*     */       }
/*  58 */       return true;
/*     */     } 
/*     */     
/*  61 */     return false;
/*     */   }
/*     */   public void build(XSComplexType ct) {
/*     */     CReferencePropertyInfo cReferencePropertyInfo;
/*  65 */     XSContentType contentType = ct.getContentType();
/*     */     
/*  67 */     boolean generateMixedExtensions = this.bgmBuilder.isGenerateMixedExtensions();
/*  68 */     if (generateMixedExtensions && (
/*  69 */       ct.getBaseType() != this.schemas.getAnyType() || !ct.isMixed())) {
/*  70 */       XSComplexType baseType = ct.getBaseType().asComplexType();
/*     */       
/*  72 */       CClass baseClass = this.selector.bindToType(baseType, (XSComponent)ct, true);
/*  73 */       this.selector.getCurrentBean().setBaseClass(baseClass);
/*     */     } 
/*     */ 
/*     */     
/*  77 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_CONTENT);
/*  78 */     BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/*     */ 
/*     */ 
/*     */     
/*  82 */     if (generateMixedExtensions) {
/*  83 */       List<XSComplexType> cType = ct.getSubtypes();
/*  84 */       boolean isSubtyped = (cType != null && cType.size() > 0);
/*     */       
/*  86 */       if (contentType.asEmpty() != null) {
/*  87 */         if (isSubtyped) {
/*  88 */           cReferencePropertyInfo = prop.createContentExtendedMixedReferenceProperty("Content", (XSComponent)ct, null);
/*     */         } else {
/*  90 */           CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Content", false, (XSComponent)ct, (TypeUse)CBuiltinLeafInfo.STRING, null);
/*     */         } 
/*  92 */       } else if (contentType.asParticle() == null) {
/*  93 */         cReferencePropertyInfo = prop.createContentExtendedMixedReferenceProperty("Content", (XSComponent)ct, null);
/*     */       } else {
/*  95 */         RawTypeSet ts = RawTypeSetBuilder.build(contentType.asParticle(), false);
/*  96 */         cReferencePropertyInfo = prop.createContentExtendedMixedReferenceProperty("Content", (XSComponent)ct, ts);
/*     */       }
/*     */     
/*     */     }
/* 100 */     else if (contentType.asEmpty() != null) {
/* 101 */       CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Content", false, (XSComponent)ct, (TypeUse)CBuiltinLeafInfo.STRING, null);
/*     */     } else {
/* 103 */       RawTypeSet ts = RawTypeSetBuilder.build(contentType.asParticle(), false);
/* 104 */       cReferencePropertyInfo = prop.createReferenceProperty("Content", false, (XSComponent)ct, ts, true, false, true, false);
/*     */     } 
/*     */ 
/*     */     
/* 108 */     this.selector.getCurrentBean().addProperty((CPropertyInfo)cReferencePropertyInfo);
/*     */ 
/*     */     
/* 111 */     this.green.attContainer((XSAttContainer)ct);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\MixedComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */