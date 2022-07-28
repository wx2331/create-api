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
/*    */ final class JBreak
/*    */   implements JStatement
/*    */ {
/*    */   private final JLabel label;
/*    */   
/*    */   JBreak(JLabel _label) {
/* 43 */     this.label = _label;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 47 */     if (this.label == null) {
/* 48 */       f.p("break;").nl();
/*    */     } else {
/* 50 */       f.p("break").p(this.label.label).p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JBreak.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */