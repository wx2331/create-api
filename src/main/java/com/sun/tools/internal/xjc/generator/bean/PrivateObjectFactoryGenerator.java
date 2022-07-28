/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import com.sun.codemodel.internal.fmt.JPropertyFile;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.runtime.JAXBContextFactory;
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
/*    */ final class PrivateObjectFactoryGenerator
/*    */   extends ObjectFactoryGeneratorImpl
/*    */ {
/*    */   public PrivateObjectFactoryGenerator(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 49 */     super(outline, model, targetPackage.subPackage("impl"));
/*    */     
/* 51 */     JPackage implPkg = targetPackage.subPackage("impl");
/*    */ 
/*    */     
/* 54 */     JClass factory = outline.generateStaticClass(JAXBContextFactory.class, implPkg);
/*    */ 
/*    */     
/* 57 */     JPropertyFile jaxbProperties = new JPropertyFile("jaxb.properties");
/* 58 */     targetPackage.addResourceFile((JResourceFile)jaxbProperties);
/* 59 */     jaxbProperties.add("javax.xml.bind.context.factory", factory
/*    */         
/* 61 */         .fullName());
/*    */   }
/*    */   
/*    */   void populate(CElementInfo ei) {
/* 65 */     populate(ei, Aspect.IMPLEMENTATION, Aspect.IMPLEMENTATION);
/*    */   }
/*    */   
/*    */   void populate(ClassOutlineImpl cc) {
/* 69 */     populate(cc, cc.implRef);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\PrivateObjectFactoryGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */