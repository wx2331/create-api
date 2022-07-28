/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IsSetFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private final FieldRenderer core;
/*    */   private final boolean generateUnSetMethod;
/*    */   private final boolean generateIsSetMethod;
/*    */   
/*    */   public IsSetFieldRenderer(FieldRenderer core, boolean generateUnSetMethod, boolean generateIsSetMethod) {
/* 53 */     this.core = core;
/* 54 */     this.generateUnSetMethod = generateUnSetMethod;
/* 55 */     this.generateIsSetMethod = generateIsSetMethod;
/*    */   }
/*    */   
/*    */   public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop) {
/* 59 */     return new IsSetField(context, prop, this.core
/* 60 */         .generate(context, prop), this.generateUnSetMethod, this.generateIsSetMethod);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\IsSetFieldRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */