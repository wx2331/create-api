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
/*    */ public class JStringLiteral
/*    */   extends JExpressionImpl
/*    */ {
/*    */   public final String str;
/*    */   
/*    */   JStringLiteral(String what) {
/* 40 */     this.str = what;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(JFormatter f) {
/* 46 */     f.p(JExpr.quotify('"', this.str));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JStringLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */