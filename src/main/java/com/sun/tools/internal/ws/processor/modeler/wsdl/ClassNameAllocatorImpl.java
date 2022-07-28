/*    */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.util.ClassNameCollector;
/*    */ import com.sun.tools.internal.xjc.api.ClassNameAllocator;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class ClassNameAllocatorImpl
/*    */   implements ClassNameAllocator
/*    */ {
/*    */   private static final String TYPE_SUFFIX = "_Type";
/*    */   private ClassNameCollector classNameCollector;
/*    */   private Set<String> jaxbClasses;
/*    */   
/*    */   public ClassNameAllocatorImpl(ClassNameCollector classNameCollector) {
/* 41 */     this.classNameCollector = classNameCollector;
/* 42 */     this.jaxbClasses = new HashSet<>();
/*    */   }
/*    */   
/*    */   public String assignClassName(String packageName, String className) {
/* 46 */     if (packageName == null || className == null)
/*    */     {
/* 48 */       return className;
/*    */     }
/*    */ 
/*    */     
/* 52 */     if (packageName.equals("") || className.equals("")) {
/* 53 */       return className;
/*    */     }
/* 55 */     String fullClassName = packageName + "." + className;
/*    */ 
/*    */     
/* 58 */     Set<String> seiClassNames = this.classNameCollector.getSeiClassNames();
/* 59 */     if (seiClassNames != null && seiClassNames.contains(fullClassName)) {
/* 60 */       className = className + "_Type";
/*    */     }
/*    */     
/* 63 */     this.jaxbClasses.add(packageName + "." + className);
/* 64 */     return className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<String> getJaxbGeneratedClasses() {
/* 72 */     return this.jaxbClasses;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\wsdl\ClassNameAllocatorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */