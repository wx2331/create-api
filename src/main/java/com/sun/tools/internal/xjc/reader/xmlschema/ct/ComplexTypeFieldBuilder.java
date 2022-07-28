/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BindingComponent;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ComplexTypeFieldBuilder
/*     */   extends BindingComponent
/*     */ {
/*  51 */   private final CTBuilder[] complexTypeBuilders = new CTBuilder[] { new MultiWildcardComplexTypeBuilder(), new MixedExtendedComplexTypeBuilder(), new MixedComplexTypeBuilder(), new FreshComplexTypeBuilder(), new ExtendedComplexTypeBuilder(), new RestrictedComplexTypeBuilder(), new STDerivedComplexTypeBuilder() };
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
/*  62 */   private final Map<XSComplexType, ComplexTypeBindingMode> complexTypeBindingModes = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void build(XSComplexType type) {
/*  69 */     for (CTBuilder ctb : this.complexTypeBuilders) {
/*  70 */       if (ctb.isApplicable(type)) {
/*  71 */         ctb.build(type);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     assert false;
/*     */   }
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
/*     */   public void recordBindingMode(XSComplexType type, ComplexTypeBindingMode flag) {
/*  93 */     Object o = this.complexTypeBindingModes.put(type, flag);
/*  94 */     assert o == null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ComplexTypeBindingMode getBindingMode(XSComplexType type) {
/* 102 */     ComplexTypeBindingMode r = this.complexTypeBindingModes.get(type);
/* 103 */     assert r != null;
/* 104 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\ComplexTypeFieldBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */