/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CValuePropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*    */ import com.sun.xml.internal.xsom.XSAttContainer;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import com.sun.xml.internal.xsom.XSDeclaration;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
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
/*    */ final class STDerivedComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 47 */     return ct.getBaseType().isSimpleType();
/*    */   }
/*    */   
/*    */   public void build(XSComplexType ct) {
/* 51 */     assert ct.getDerivationMethod() == 1;
/*    */ 
/*    */     
/* 54 */     XSSimpleType baseType = ct.getBaseType().asSimpleType();
/*    */ 
/*    */     
/* 57 */     this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */     
/* 59 */     this.simpleTypeBuilder.refererStack.push(ct);
/* 60 */     TypeUse use = this.simpleTypeBuilder.build(baseType);
/* 61 */     this.simpleTypeBuilder.refererStack.pop();
/*    */     
/* 63 */     BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/* 64 */     CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Value", false, (XSComponent)baseType, use, BGMBuilder.getName((XSDeclaration)baseType));
/* 65 */     this.selector.getCurrentBean().addProperty((CPropertyInfo)cValuePropertyInfo);
/*    */ 
/*    */     
/* 68 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\STDerivedComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */