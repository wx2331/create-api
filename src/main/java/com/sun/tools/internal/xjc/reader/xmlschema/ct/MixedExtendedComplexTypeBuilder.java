/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClass;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSAttContainer;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
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
/*    */ final class MixedExtendedComplexTypeBuilder
/*    */   extends AbstractExtendedComplexTypeBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 45 */     if (!this.bgmBuilder.isGenerateMixedExtensions()) return false;
/*    */     
/* 47 */     XSType bt = ct.getBaseType();
/* 48 */     if (bt.isComplexType() && bt
/* 49 */       .asComplexType().isMixed() && ct
/* 50 */       .isMixed() && ct
/* 51 */       .getDerivationMethod() == 1 && ct
/* 52 */       .getContentType().asParticle() != null && ct
/* 53 */       .getExplicitContent().asEmpty() == null)
/*    */     {
/* 55 */       return true;
/*    */     }
/*    */     
/* 58 */     return false;
/*    */   }
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 62 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/*    */     
/* 65 */     CClass baseClass = this.selector.bindToType(baseType, (XSComponent)ct, true);
/* 66 */     assert baseClass != null;
/*    */     
/* 68 */     if (!checkIfExtensionSafe(baseType, ct)) {
/*    */       
/* 70 */       this.errorReceiver.error(ct.getLocator(), Messages.ERR_NO_FURTHER_EXTENSION
/* 71 */           .format(new Object[] {
/* 72 */               baseType.getName(), ct.getName()
/*    */             }));
/*    */       
/*    */       return;
/*    */     } 
/* 77 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/* 78 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_EXTENSION);
/*    */     
/* 80 */     BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/*    */ 
/*    */     
/* 83 */     RawTypeSet ts = RawTypeSetBuilder.build(ct.getContentType().asParticle(), false);
/* 84 */     CReferencePropertyInfo cReferencePropertyInfo = prop.createDummyExtendedMixedReferenceProperty("contentOverrideFor" + ct.getName(), (XSComponent)ct, ts);
/*    */     
/* 86 */     this.selector.getCurrentBean().addProperty((CPropertyInfo)cReferencePropertyInfo);
/*    */ 
/*    */     
/* 89 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\MixedExtendedComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */