/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JPrimitiveType;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
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
/*     */ final class ConstField
/*     */   extends AbstractField
/*     */ {
/*     */   private final JFieldVar $ref;
/*     */   
/*     */   ConstField(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  59 */     super(outline, prop);
/*     */ 
/*     */     
/*  62 */     assert !prop.isCollection();
/*     */     
/*  64 */     JPrimitiveType ptype = this.implType.boxify().getPrimitiveType();
/*     */ 
/*     */     
/*  67 */     JExpression defaultValue = null;
/*  68 */     if (prop.defaultValue != null) {
/*  69 */       defaultValue = prop.defaultValue.compute((Outline)outline.parent());
/*     */     }
/*  71 */     this.$ref = outline.ref.field(25, (ptype != null) ? (JType)ptype : this.implType, prop
/*  72 */         .getName(true), defaultValue);
/*  73 */     this.$ref.javadoc().append(prop.javadoc);
/*     */     
/*  75 */     annotate((JAnnotatable)this.$ref);
/*     */   }
/*     */ 
/*     */   
/*     */   public JType getRawType() {
/*  80 */     return this.exposedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldAccessor create(JExpression target) {
/*  85 */     return new Accessor(target);
/*     */   }
/*     */   
/*     */   private class Accessor
/*     */     extends AbstractField.Accessor {
/*     */     Accessor(JExpression $target) {
/*  91 */       super($target);
/*     */     }
/*     */ 
/*     */     
/*     */     public void unsetValues(JBlock body) {}
/*     */     
/*     */     public JExpression hasSetValue() {
/*  98 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 103 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 107 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\ConstField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */