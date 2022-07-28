/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CClass;
/*    */ import com.sun.xml.internal.xsom.XSAttContainer;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
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
/*    */ final class ExtendedComplexTypeBuilder
/*    */   extends AbstractExtendedComplexTypeBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 44 */     XSType baseType = ct.getBaseType();
/* 45 */     return (baseType != this.schemas.getAnyType() && baseType
/* 46 */       .isComplexType() && ct
/* 47 */       .getDerivationMethod() == 1);
/*    */   }
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 51 */     XSComplexType baseType = ct.getBaseType().asComplexType();
/*    */ 
/*    */     
/* 54 */     CClass baseClass = this.selector.bindToType(baseType, (XSComponent)ct, true);
/* 55 */     assert baseClass != null;
/*    */     
/* 57 */     this.selector.getCurrentBean().setBaseClass(baseClass);
/*    */ 
/*    */     
/* 60 */     ComplexTypeBindingMode baseTypeFlag = this.builder.getBindingMode(baseType);
/*    */     
/* 62 */     XSContentType explicitContent = ct.getExplicitContent();
/*    */     
/* 64 */     if (!checkIfExtensionSafe(baseType, ct)) {
/*    */       
/* 66 */       this.errorReceiver.error(ct.getLocator(), Messages.ERR_NO_FURTHER_EXTENSION
/* 67 */           .format(new Object[] {
/* 68 */               baseType.getName(), ct.getName()
/*    */             }));
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 74 */     if (explicitContent != null && explicitContent.asParticle() != null) {
/* 75 */       if (baseTypeFlag == ComplexTypeBindingMode.NORMAL) {
/*    */         
/* 77 */         this.builder.recordBindingMode(ct, 
/* 78 */             this.bgmBuilder.getParticleBinder().checkFallback(explicitContent.asParticle()) ? ComplexTypeBindingMode.FALLBACK_REST : ComplexTypeBindingMode.NORMAL);
/*    */ 
/*    */ 
/*    */         
/* 82 */         this.bgmBuilder.getParticleBinder().build(explicitContent.asParticle());
/*    */       
/*    */       }
/*    */       else {
/*    */         
/* 87 */         this.builder.recordBindingMode(ct, baseTypeFlag);
/*    */       } 
/*    */     } else {
/*    */       
/* 91 */       this.builder.recordBindingMode(ct, baseTypeFlag);
/*    */     } 
/*    */ 
/*    */     
/* 95 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\ExtendedComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */