/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.codemodel.internal.JExpr;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.Model;
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
/*    */ public final class DualObjectFactoryGenerator
/*    */   extends ObjectFactoryGenerator
/*    */ {
/*    */   public final ObjectFactoryGenerator publicOFG;
/*    */   public final ObjectFactoryGenerator privateOFG;
/*    */   
/*    */   DualObjectFactoryGenerator(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 50 */     this.publicOFG = new PublicObjectFactoryGenerator(outline, model, targetPackage);
/* 51 */     this.privateOFG = new PrivateObjectFactoryGenerator(outline, model, targetPackage);
/*    */ 
/*    */     
/* 54 */     this.publicOFG.getObjectFactory().field(28, Void.class, "_useJAXBProperties", 
/* 55 */         JExpr._null());
/*    */   }
/*    */   
/*    */   void populate(CElementInfo ei) {
/* 59 */     this.publicOFG.populate(ei);
/* 60 */     this.privateOFG.populate(ei);
/*    */   }
/*    */   
/*    */   void populate(ClassOutlineImpl cc) {
/* 64 */     this.publicOFG.populate(cc);
/* 65 */     this.privateOFG.populate(cc);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JDefinedClass getObjectFactory() {
/* 72 */     return this.privateOFG.getObjectFactory();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\DualObjectFactoryGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */