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
/*    */ public class JCatchBlock
/*    */   implements JGenerable
/*    */ {
/*    */   JClass exception;
/* 36 */   private JVar var = null;
/* 37 */   private JBlock body = new JBlock();
/*    */   
/*    */   JCatchBlock(JClass exception) {
/* 40 */     this.exception = exception;
/*    */   }
/*    */   
/*    */   public JVar param(String name) {
/* 44 */     if (this.var != null) throw new IllegalStateException(); 
/* 45 */     this.var = new JVar(JMods.forVar(0), this.exception, name, null);
/* 46 */     return this.var;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 50 */     return this.body;
/*    */   }
/*    */   
/*    */   public void generate(JFormatter f) {
/* 54 */     if (this.var == null) {
/* 55 */       this.var = new JVar(JMods.forVar(0), this.exception, "_x", null);
/*    */     }
/* 57 */     f.p("catch (").b(this.var).p(')').g(this.body);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JCatchBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */