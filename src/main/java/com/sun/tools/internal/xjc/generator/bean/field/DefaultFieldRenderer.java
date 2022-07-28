/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
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
/*     */ final class DefaultFieldRenderer
/*     */   implements FieldRenderer
/*     */ {
/*     */   private final FieldRendererFactory frf;
/*     */   private FieldRenderer defaultCollectionFieldRenderer;
/*     */   
/*     */   DefaultFieldRenderer(FieldRendererFactory frf) {
/*  55 */     this.frf = frf;
/*     */   }
/*     */   
/*     */   public DefaultFieldRenderer(FieldRendererFactory frf, FieldRenderer defaultCollectionFieldRenderer) {
/*  59 */     this.frf = frf;
/*  60 */     this.defaultCollectionFieldRenderer = defaultCollectionFieldRenderer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldOutline generate(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  67 */     return decideRenderer(outline, prop).generate(outline, prop);
/*     */   }
/*     */ 
/*     */   
/*     */   private FieldRenderer decideRenderer(ClassOutlineImpl outline, CPropertyInfo prop) {
/*  72 */     if (prop instanceof CReferencePropertyInfo) {
/*  73 */       CReferencePropertyInfo p = (CReferencePropertyInfo)prop;
/*  74 */       if (p.isDummy()) {
/*  75 */         return this.frf.getDummyList(outline.parent().getCodeModel().ref(ArrayList.class));
/*     */       }
/*  77 */       if (p.isContent() && p.isMixedExtendedCust()) {
/*  78 */         return this.frf.getContentList(outline.parent().getCodeModel().ref(ArrayList.class).narrow(Serializable.class));
/*     */       }
/*     */     } 
/*     */     
/*  82 */     if (!prop.isCollection()) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  87 */       if (prop.isUnboxable())
/*     */       {
/*  89 */         return this.frf.getRequiredUnboxed();
/*     */       }
/*     */       
/*  92 */       return this.frf.getSingle();
/*     */     } 
/*     */     
/*  95 */     if (this.defaultCollectionFieldRenderer == null) {
/*  96 */       return this.frf.getList(outline.parent().getCodeModel().ref(ArrayList.class));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     return this.defaultCollectionFieldRenderer;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\DefaultFieldRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */