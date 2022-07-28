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
/*    */ 
/*    */ 
/*    */ final class JAnnotationStringValue
/*    */   extends JAnnotationValue
/*    */ {
/*    */   private final JExpression value;
/*    */   
/*    */   JAnnotationStringValue(JExpression value) {
/* 44 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 48 */     f.g(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAnnotationStringValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */