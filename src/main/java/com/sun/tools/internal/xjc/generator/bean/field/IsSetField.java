/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
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
/*     */ public class IsSetField
/*     */   extends AbstractField
/*     */ {
/*     */   private final FieldOutline core;
/*     */   private final boolean generateUnSetMethod;
/*     */   private final boolean generateIsSetMethod;
/*     */   
/*     */   protected IsSetField(ClassOutlineImpl outline, CPropertyInfo prop, FieldOutline core, boolean unsetMethod, boolean issetMethod) {
/*  55 */     super(outline, prop);
/*  56 */     this.core = core;
/*  57 */     this.generateIsSetMethod = issetMethod;
/*  58 */     this.generateUnSetMethod = unsetMethod;
/*     */     
/*  60 */     generate(outline, prop);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void generate(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  66 */     MethodWriter writer = outline.createMethodWriter();
/*     */     
/*  68 */     JCodeModel codeModel = outline.parent().getCodeModel();
/*     */     
/*  70 */     FieldAccessor acc = this.core.create(JExpr._this());
/*     */     
/*  72 */     if (this.generateIsSetMethod) {
/*     */       
/*  74 */       JExpression hasSetValue = acc.hasSetValue();
/*  75 */       if (hasSetValue == null)
/*     */       {
/*     */         
/*  78 */         throw new UnsupportedOperationException();
/*     */       }
/*  80 */       writer.declareMethod((JType)codeModel.BOOLEAN, "isSet" + this.prop.getName(true))
/*  81 */         .body()._return(hasSetValue);
/*     */     } 
/*     */     
/*  84 */     if (this.generateUnSetMethod)
/*     */     {
/*  86 */       acc.unsetValues(writer
/*  87 */           .declareMethod((JType)codeModel.VOID, "unset" + this.prop.getName(true)).body());
/*     */     }
/*     */   }
/*     */   
/*     */   public JType getRawType() {
/*  92 */     return this.core.getRawType();
/*     */   }
/*     */   
/*     */   public FieldAccessor create(JExpression targetObject) {
/*  96 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   private class Accessor
/*     */     extends AbstractField.Accessor {
/*     */     private final FieldAccessor core;
/*     */     
/*     */     Accessor(JExpression $target) {
/* 104 */       super($target);
/* 105 */       this.core = IsSetField.this.core.create($target);
/*     */     }
/*     */ 
/*     */     
/*     */     public void unsetValues(JBlock body) {
/* 110 */       this.core.unsetValues(body);
/*     */     }
/*     */     public JExpression hasSetValue() {
/* 113 */       return this.core.hasSetValue();
/*     */     }
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 116 */       this.core.toRawValue(block, $var);
/*     */     }
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 120 */       this.core.fromRawValue(block, uniqueName, $var);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\IsSetField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */