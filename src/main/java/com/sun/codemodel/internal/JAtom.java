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
/*    */ final class JAtom
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private final String what;
/*    */   
/*    */   JAtom(String what) {
/* 37 */     this.what = what;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 41 */     f.p(this.what);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAtom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */