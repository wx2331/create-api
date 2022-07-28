/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClass;
/*     */ import com.sun.tools.internal.xjc.model.CDefaultValue;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
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
/*     */ public class BindPurple
/*     */   extends ColorBinder
/*     */ {
/*     */   public void attGroupDecl(XSAttGroupDecl xsAttGroupDecl) {
/*  53 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl xsAttributeDecl) {
/*  58 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/*  65 */     boolean hasFixedValue = (use.getFixedValue() != null);
/*  66 */     BIProperty pc = BIProperty.getCustomization((XSComponent)use);
/*     */ 
/*     */     
/*  69 */     boolean toConstant = (pc.isConstantProperty() && hasFixedValue);
/*  70 */     TypeUse attType = bindAttDecl(use.getDecl());
/*     */     
/*  72 */     CAttributePropertyInfo cAttributePropertyInfo = pc.createAttributeProperty(use, attType);
/*     */     
/*  74 */     if (toConstant) {
/*  75 */       ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(attType, use.getFixedValue());
/*  76 */       ((CPropertyInfo)cAttributePropertyInfo).realization = this.builder.fieldRendererFactory.getConst(((CPropertyInfo)cAttributePropertyInfo).realization);
/*     */     }
/*  78 */     else if (!attType.isCollection() && (((CPropertyInfo)cAttributePropertyInfo).baseType == null || !((CPropertyInfo)cAttributePropertyInfo).baseType.isPrimitive())) {
/*     */ 
/*     */ 
/*     */       
/*  82 */       if (use.getDefaultValue() != null) {
/*     */ 
/*     */ 
/*     */         
/*  86 */         ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(attType, use.getDefaultValue());
/*     */       }
/*  88 */       else if (use.getFixedValue() != null) {
/*  89 */         ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(attType, use.getFixedValue());
/*     */       } 
/*  91 */     } else if (((CPropertyInfo)cAttributePropertyInfo).baseType != null && ((CPropertyInfo)cAttributePropertyInfo).baseType.isPrimitive()) {
/*  92 */       ErrorReporter errorReporter = (ErrorReporter)Ring.get(ErrorReporter.class);
/*     */       
/*  94 */       errorReporter.warning(cAttributePropertyInfo.getLocator(), "WARN_DEFAULT_VALUE_PRIMITIVE_TYPE", new Object[] { ((CPropertyInfo)cAttributePropertyInfo).baseType.name() });
/*     */     } 
/*     */     
/*  97 */     getCurrentBean().addProperty((CPropertyInfo)cAttributePropertyInfo);
/*     */   }
/*     */   
/*     */   private TypeUse bindAttDecl(XSAttributeDecl decl) {
/* 101 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/* 102 */     stb.refererStack.push(decl);
/*     */     try {
/* 104 */       return stb.build(decl.getType());
/*     */     } finally {
/* 106 */       stb.refererStack.pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/* 112 */     CClass ctBean = this.selector.bindToType(ct, null, false);
/* 113 */     if (getCurrentBean() != ctBean)
/*     */     {
/*     */       
/* 116 */       getCurrentBean().setBaseClass(ctBean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard xsWildcard) {
/* 123 */     getCurrentBean().hasAttributeWildcard(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/* 128 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroup(XSModelGroup xsModelGroup) {
/* 133 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void elementDecl(XSElementDecl xsElementDecl) {
/* 138 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType type) {
/* 142 */     createSimpleTypeProperty(type, "Value");
/*     */   }
/*     */ 
/*     */   
/*     */   public void particle(XSParticle xsParticle) {
/* 147 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void empty(XSContentType ct) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\BindPurple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */