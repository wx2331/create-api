/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
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
/*    */ final class PublicObjectFactoryGenerator
/*    */   extends ObjectFactoryGeneratorImpl
/*    */ {
/*    */   public PublicObjectFactoryGenerator(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 40 */     super(outline, model, targetPackage);
/*    */   }
/*    */   
/*    */   void populate(CElementInfo ei) {
/* 44 */     populate(ei, Aspect.IMPLEMENTATION, Aspect.EXPOSED);
/*    */   }
/*    */   
/*    */   void populate(ClassOutlineImpl cc) {
/* 48 */     populate(cc, (JClass)cc.ref);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\PublicObjectFactoryGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */