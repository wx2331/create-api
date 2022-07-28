/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldRendererFactory
/*    */ {
/*    */   public FieldRenderer getDefault() {
/* 54 */     return this.DEFAULT;
/*    */   }
/*    */   public FieldRenderer getArray() {
/* 57 */     return ARRAY;
/*    */   }
/*    */   public FieldRenderer getRequiredUnboxed() {
/* 60 */     return REQUIRED_UNBOXED;
/*    */   }
/*    */   public FieldRenderer getSingle() {
/* 63 */     return SINGLE;
/*    */   }
/*    */   public FieldRenderer getSinglePrimitiveAccess() {
/* 66 */     return SINGLE_PRIMITIVE_ACCESS;
/*    */   }
/*    */   public FieldRenderer getList(JClass coreList) {
/* 69 */     return new UntypedListFieldRenderer(coreList);
/*    */   }
/*    */   public FieldRenderer getContentList(JClass coreList) {
/* 72 */     return new UntypedListFieldRenderer(coreList, false, true);
/*    */   }
/*    */   public FieldRenderer getDummyList(JClass coreList) {
/* 75 */     return new UntypedListFieldRenderer(coreList, true, false);
/*    */   }
/*    */   public FieldRenderer getConst(FieldRenderer fallback) {
/* 78 */     return new ConstFieldRenderer(fallback);
/*    */   }
/*    */   
/* 81 */   private final FieldRenderer DEFAULT = new DefaultFieldRenderer(this);
/*    */ 
/*    */   
/* 84 */   private static final FieldRenderer ARRAY = new GenericFieldRenderer(ArrayField.class);
/*    */ 
/*    */   
/* 87 */   private static final FieldRenderer REQUIRED_UNBOXED = new GenericFieldRenderer(UnboxedField.class);
/*    */ 
/*    */   
/* 90 */   private static final FieldRenderer SINGLE = new GenericFieldRenderer(SingleField.class);
/*    */ 
/*    */   
/* 93 */   private static final FieldRenderer SINGLE_PRIMITIVE_ACCESS = new GenericFieldRenderer(SinglePrimitiveAccessField.class);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\FieldRendererFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */