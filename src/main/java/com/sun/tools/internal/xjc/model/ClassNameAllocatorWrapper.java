/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.tools.internal.xjc.api.ClassNameAllocator;
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
/*    */ final class ClassNameAllocatorWrapper
/*    */   implements ClassNameAllocator
/*    */ {
/*    */   private final ClassNameAllocator core;
/*    */   
/*    */   ClassNameAllocatorWrapper(ClassNameAllocator core) {
/* 40 */     if (core == null)
/* 41 */       core = new ClassNameAllocator() {
/*    */           public String assignClassName(String packageName, String className) {
/* 43 */             return className;
/*    */           }
/*    */         }; 
/* 46 */     this.core = core;
/*    */   }
/*    */   
/*    */   public String assignClassName(String packageName, String className) {
/* 50 */     return this.core.assignClassName(packageName, className);
/*    */   }
/*    */   
/*    */   public String assignClassName(JPackage pkg, String className) {
/* 54 */     return this.core.assignClassName(pkg.name(), className);
/*    */   }
/*    */   
/*    */   public String assignClassName(CClassInfoParent parent, String className) {
/* 58 */     if (parent instanceof CClassInfoParent.Package) {
/* 59 */       CClassInfoParent.Package p = (CClassInfoParent.Package)parent;
/* 60 */       return assignClassName(p.pkg, className);
/*    */     } 
/*    */     
/* 63 */     return className;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\ClassNameAllocatorWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */