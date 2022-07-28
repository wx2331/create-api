/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class JTryBlock
/*    */   implements JStatement
/*    */ {
/* 38 */   private JBlock body = new JBlock();
/* 39 */   private List<JCatchBlock> catches = new ArrayList<>();
/* 40 */   private JBlock _finally = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JBlock body() {
/* 46 */     return this.body;
/*    */   }
/*    */   
/*    */   public JCatchBlock _catch(JClass exception) {
/* 50 */     JCatchBlock cb = new JCatchBlock(exception);
/* 51 */     this.catches.add(cb);
/* 52 */     return cb;
/*    */   }
/*    */   
/*    */   public JBlock _finally() {
/* 56 */     if (this._finally == null) this._finally = new JBlock(); 
/* 57 */     return this._finally;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 61 */     f.p("try").g(this.body);
/* 62 */     for (JCatchBlock cb : this.catches)
/* 63 */       f.g(cb); 
/* 64 */     if (this._finally != null)
/* 65 */       f.p("finally").g(this._finally); 
/* 66 */     f.nl();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JTryBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */