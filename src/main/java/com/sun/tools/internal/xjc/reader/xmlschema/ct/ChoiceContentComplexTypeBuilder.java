/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAttContainer;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import java.util.Collections;
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
/*    */ 
/*    */ final class ChoiceContentComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 44 */     if (!this.bgmBuilder.getGlobalBinding().isChoiceContentPropertyEnabled()) {
/* 45 */       return false;
/*    */     }
/* 47 */     if (ct.getBaseType() != this.schemas.getAnyType())
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 53 */       return false;
/*    */     }
/* 55 */     XSParticle p = ct.getContentType().asParticle();
/* 56 */     if (p == null) {
/* 57 */       return false;
/*    */     }
/* 59 */     XSModelGroup mg = getTopLevelModelGroup(p);
/*    */     
/* 61 */     if (mg.getCompositor() != XSModelGroup.CHOICE) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (p.isRepeated()) {
/* 65 */       return false;
/*    */     }
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private XSModelGroup getTopLevelModelGroup(XSParticle p) {
/* 73 */     XSModelGroup mg = p.getTerm().asModelGroup();
/* 74 */     if (p.getTerm().isModelGroupDecl())
/* 75 */       mg = p.getTerm().asModelGroupDecl().getModelGroup(); 
/* 76 */     return mg;
/*    */   }
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 80 */     XSParticle p = ct.getContentType().asParticle();
/*    */     
/* 82 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */     
/* 84 */     this.bgmBuilder.getParticleBinder().build(p, Collections.singleton(p));
/*    */     
/* 86 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\ChoiceContentComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */