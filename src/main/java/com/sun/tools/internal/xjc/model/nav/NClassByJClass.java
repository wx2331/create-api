/*    */ package com.sun.tools.internal.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
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
/*    */ class NClassByJClass
/*    */   implements NClass
/*    */ {
/*    */   final JClass clazz;
/*    */   
/*    */   NClassByJClass(JClass clazz) {
/* 39 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public JClass toType(Outline o, Aspect aspect) {
/* 43 */     return this.clazz;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 47 */     return this.clazz.isAbstract();
/*    */   }
/*    */   
/*    */   public boolean isBoxedType() {
/* 51 */     return (this.clazz.getPrimitiveType() != null);
/*    */   }
/*    */   
/*    */   public String fullName() {
/* 55 */     return this.clazz.fullName();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\nav\NClassByJClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */