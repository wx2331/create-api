/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClass;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RestrictedComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 48 */     XSType baseType = ct.getBaseType();
/* 49 */     return (baseType != this.schemas.getAnyType() && baseType
/* 50 */       .isComplexType() && ct
/* 51 */       .getDerivationMethod() == 2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 56 */     if (this.bgmBuilder.getGlobalBinding().isRestrictionFreshType()) {
/*    */       
/* 58 */       (new FreshComplexTypeBuilder()).build(ct);
/*    */       
/*    */       return;
/*    */     } 
/* 62 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/*    */     
/* 65 */     CClass baseClass = this.selector.bindToType(baseType, (XSComponent)ct, true);
/* 66 */     assert baseClass != null;
/*    */     
/* 68 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/*    */     
/* 70 */     if (this.bgmBuilder.isGenerateMixedExtensions()) {
/*    */ 
/*    */       
/* 73 */       boolean forceFallbackInExtension = (baseType.isMixed() && ct.isMixed() && ct.getExplicitContent() != null && this.bgmBuilder.inExtensionMode);
/*    */       
/* 75 */       if (forceFallbackInExtension) {
/* 76 */         this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */         
/* 78 */         BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/*    */ 
/*    */         
/* 81 */         XSParticle particle = ct.getContentType().asParticle();
/* 82 */         if (particle != null) {
/* 83 */           RawTypeSet ts = RawTypeSetBuilder.build(particle, false);
/* 84 */           CReferencePropertyInfo cReferencePropertyInfo = prop.createDummyExtendedMixedReferenceProperty("Content", (XSComponent)ct, ts);
/* 85 */           this.selector.getCurrentBean().addProperty((CPropertyInfo)cReferencePropertyInfo);
/*    */         } 
/*    */       } else {
/*    */         
/* 89 */         this.builder.recordBindingMode(ct, this.builder.getBindingMode(baseType));
/*    */       } 
/*    */     } else {
/* 92 */       this.builder.recordBindingMode(ct, this.builder.getBindingMode(baseType));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\RestrictedComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */