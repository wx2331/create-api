/*    */ package com.sun.codemodel.internal;
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
/*    */ class JAnonymousClass
/*    */   extends JDefinedClass
/*    */ {
/*    */   private final JClass base;
/*    */   
/*    */   JAnonymousClass(JClass _base) {
/* 42 */     super(_base.owner(), 0, null);
/* 43 */     this.base = _base;
/*    */   }
/*    */ 
/*    */   
/*    */   public String fullName() {
/* 48 */     return this.base.fullName();
/*    */   }
/*    */ 
/*    */   
/*    */   public void generate(JFormatter f) {
/* 53 */     f.t(this.base);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAnonymousClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */