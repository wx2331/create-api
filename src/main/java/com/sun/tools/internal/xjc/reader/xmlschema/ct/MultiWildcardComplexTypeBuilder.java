/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.RawTypeSetBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSAttContainer;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
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
/*    */ final class MultiWildcardComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 45 */     if (!this.bgmBuilder.model.options.contentForWildcard) {
/* 46 */       return false;
/*    */     }
/* 48 */     XSType bt = ct.getBaseType();
/* 49 */     if (bt == this.schemas.getAnyType() && ct.getContentType() != null) {
/* 50 */       XSParticle part = ct.getContentType().asParticle();
/* 51 */       if (part != null && part.getTerm().isModelGroup()) {
/* 52 */         XSParticle[] parts = part.getTerm().asModelGroup().getChildren();
/* 53 */         int wildcardCount = 0;
/* 54 */         int i = 0;
/* 55 */         while (i < parts.length && wildcardCount <= 1) {
/* 56 */           if (parts[i].getTerm().isWildcard()) {
/* 57 */             wildcardCount++;
/*    */           }
/* 59 */           i++;
/*    */         } 
/* 61 */         return (wildcardCount > 1);
/*    */       } 
/*    */     } 
/* 64 */     return false;
/*    */   }
/*    */   public void build(XSComplexType ct) {
/*    */     CReferencePropertyInfo cReferencePropertyInfo;
/* 68 */     XSContentType contentType = ct.getContentType();
/*    */     
/* 70 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.FALLBACK_CONTENT);
/* 71 */     BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/*    */ 
/*    */ 
/*    */     
/* 75 */     if (contentType.asEmpty() != null) {
/* 76 */       CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Content", false, (XSComponent)ct, (TypeUse)CBuiltinLeafInfo.STRING, null);
/*    */     } else {
/* 78 */       RawTypeSet ts = RawTypeSetBuilder.build(contentType.asParticle(), false);
/* 79 */       cReferencePropertyInfo = prop.createReferenceProperty("Content", false, (XSComponent)ct, ts, true, false, true, false);
/*    */     } 
/*    */     
/* 82 */     this.selector.getCurrentBean().addProperty((CPropertyInfo)cReferencePropertyInfo);
/*    */ 
/*    */     
/* 85 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\MultiWildcardComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */