/*    */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.Property;
/*    */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import java.util.List;
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
/*    */ final class BeanMappingImpl
/*    */   extends AbstractMappingImpl<CClassInfo>
/*    */ {
/* 43 */   private final TypeAndAnnotationImpl taa = new TypeAndAnnotationImpl(this.parent.outline, (TypeUse)this.clazz);
/*    */   
/*    */   BeanMappingImpl(JAXBModelImpl parent, CClassInfo classInfo) {
/* 46 */     super(parent, classInfo);
/* 47 */     assert classInfo.isElement();
/*    */   }
/*    */   
/*    */   public TypeAndAnnotation getType() {
/* 51 */     return this.taa;
/*    */   }
/*    */   
/*    */   public final String getTypeClass() {
/* 55 */     return getClazz();
/*    */   }
/*    */   
/*    */   public List<Property> calcDrilldown() {
/* 59 */     if (!this.clazz.isOrdered())
/* 60 */       return null; 
/* 61 */     return buildDrilldown(this.clazz);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\impl\s2j\BeanMappingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */