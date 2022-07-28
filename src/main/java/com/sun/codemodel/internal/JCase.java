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
/*    */ public final class JCase
/*    */   implements JStatement
/*    */ {
/*    */   private JExpression label;
/* 41 */   private JBlock body = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isDefaultCase = false;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JCase(JExpression label) {
/* 52 */     this(label, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JCase(JExpression label, boolean isDefaultCase) {
/* 60 */     this.label = label;
/* 61 */     this.isDefaultCase = isDefaultCase;
/*    */   }
/*    */   
/*    */   public JExpression label() {
/* 65 */     return this.label;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 69 */     if (this.body == null) this.body = new JBlock(false, true); 
/* 70 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 74 */     f.i();
/* 75 */     if (!this.isDefaultCase) {
/* 76 */       f.p("case ").g(this.label).p(':').nl();
/*    */     } else {
/* 78 */       f.p("default:").nl();
/*    */     } 
/* 80 */     if (this.body != null)
/* 81 */       f.s(this.body); 
/* 82 */     f.o();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JCase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */