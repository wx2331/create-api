/*    */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.api.Property;
/*    */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*    */ import com.sun.tools.internal.xjc.model.CAdapter;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.model.CNonElement;
/*    */ import com.sun.tools.internal.xjc.model.CTypeInfo;
/*    */ import com.sun.tools.internal.xjc.model.TypeUse;
/*    */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
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
/*    */ final class ElementMappingImpl
/*    */   extends AbstractMappingImpl<CElementInfo>
/*    */ {
/*    */   private final TypeAndAnnotation taa;
/*    */   
/*    */   protected ElementMappingImpl(JAXBModelImpl parent, CElementInfo elementInfo) {
/* 48 */     super(parent, elementInfo);
/*    */     TypeUse typeUse;
/* 50 */     CNonElement cNonElement = this.clazz.getContentType();
/* 51 */     if (this.clazz.getProperty().isCollection())
/* 52 */       typeUse = TypeUseFactory.makeCollection((TypeUse)cNonElement); 
/* 53 */     CAdapter a = this.clazz.getProperty().getAdapter();
/* 54 */     if (a != null)
/* 55 */       typeUse = TypeUseFactory.adapt(typeUse, a); 
/* 56 */     this.taa = new TypeAndAnnotationImpl(parent.outline, typeUse);
/*    */   }
/*    */   
/*    */   public TypeAndAnnotation getType() {
/* 60 */     return this.taa;
/*    */   }
/*    */   
/*    */   public final List<Property> calcDrilldown() {
/* 64 */     CElementPropertyInfo p = this.clazz.getProperty();
/*    */     
/* 66 */     if (p.getAdapter() != null) {
/* 67 */       return null;
/*    */     }
/* 69 */     if (p.isCollection())
/*    */     {
/* 71 */       return null;
/*    */     }
/* 73 */     CTypeInfo typeClass = p.ref().get(0);
/*    */     
/* 75 */     if (!(typeClass instanceof CClassInfo))
/*    */     {
/* 77 */       return null;
/*    */     }
/* 79 */     CClassInfo ci = (CClassInfo)typeClass;
/*    */ 
/*    */     
/* 82 */     if (ci.isAbstract()) {
/* 83 */       return null;
/*    */     }
/*    */     
/* 86 */     if (!ci.isOrdered()) {
/* 87 */       return null;
/*    */     }
/* 89 */     return buildDrilldown(ci);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\impl\s2j\ElementMappingImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */