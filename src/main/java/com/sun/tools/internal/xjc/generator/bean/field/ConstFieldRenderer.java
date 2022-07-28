/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
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
/*    */ final class ConstFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private final FieldRenderer fallback;
/*    */   
/*    */   protected ConstFieldRenderer(FieldRenderer fallback) {
/* 47 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public FieldOutline generate(ClassOutlineImpl outline, CPropertyInfo prop) {
/* 51 */     if (prop.defaultValue.compute((Outline)outline.parent()) == null) {
/* 52 */       return this.fallback.generate(outline, prop);
/*    */     }
/* 54 */     return new ConstField(outline, prop);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\ConstFieldRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */