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
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSDeclaration;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSTerm;
/*    */ import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;
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
/*    */ final class FreshComplexTypeBuilder
/*    */   extends CTBuilder
/*    */ {
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 51 */     return (ct.getBaseType() == this.schemas.getAnyType() && 
/* 52 */       !ct.isMixed());
/*    */   }
/*    */   
/*    */   public void build(final XSComplexType ct) {
/* 56 */     XSContentType contentType = ct.getContentType();
/*    */     
/* 58 */     contentType.visit(new XSContentTypeVisitor() {
/*    */           public void simpleType(XSSimpleType st) {
/* 60 */             FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */             
/* 62 */             FreshComplexTypeBuilder.this.simpleTypeBuilder.refererStack.push(ct);
/* 63 */             TypeUse use = FreshComplexTypeBuilder.this.simpleTypeBuilder.build(st);
/* 64 */             FreshComplexTypeBuilder.this.simpleTypeBuilder.refererStack.pop();
/*    */             
/* 66 */             BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/* 67 */             CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Value", false, (XSComponent)ct, use, BGMBuilder.getName((XSDeclaration)st));
/* 68 */             FreshComplexTypeBuilder.this.selector.getCurrentBean().addProperty((CPropertyInfo)cValuePropertyInfo);
/*    */           }
/*    */ 
/*    */ 
/*    */           
/*    */           public void particle(XSParticle p) {
/* 74 */             FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, 
/* 75 */                 FreshComplexTypeBuilder.this.bgmBuilder.getParticleBinder().checkFallback(p) ? ComplexTypeBindingMode.FALLBACK_CONTENT : ComplexTypeBindingMode.NORMAL);
/*    */             
/* 77 */             FreshComplexTypeBuilder.this.bgmBuilder.getParticleBinder().build(p);
/*    */             
/* 79 */             XSTerm term = p.getTerm();
/* 80 */             if (term.isModelGroup() && term.asModelGroup().getCompositor() == XSModelGroup.ALL) {
/* 81 */               FreshComplexTypeBuilder.this.selector.getCurrentBean().setOrdered(false);
/*    */             }
/*    */           }
/*    */           
/*    */           public void empty(XSContentType e) {
/* 86 */             FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*    */           }
/*    */         });
/*    */ 
/*    */     
/* 91 */     this.green.attContainer((XSAttContainer)ct);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\FreshComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */