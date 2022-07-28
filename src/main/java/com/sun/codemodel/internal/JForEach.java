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
/*    */ public final class JForEach
/*    */   implements JStatement
/*    */ {
/*    */   private final JType type;
/*    */   private final String var;
/* 39 */   private JBlock body = null;
/*    */   
/*    */   private final JExpression collection;
/*    */   private final JVar loopVar;
/*    */   
/*    */   public JForEach(JType vartype, String variable, JExpression collection) {
/* 45 */     this.type = vartype;
/* 46 */     this.var = variable;
/* 47 */     this.collection = collection;
/* 48 */     this.loopVar = new JVar(JMods.forVar(0), this.type, this.var, collection);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JVar var() {
/* 56 */     return this.loopVar;
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 60 */     if (this.body == null)
/* 61 */       this.body = new JBlock(); 
/* 62 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 66 */     f.p("for (");
/* 67 */     f.g(this.type).id(this.var).p(": ").g(this.collection);
/* 68 */     f.p(')');
/* 69 */     if (this.body != null) {
/* 70 */       f.g(this.body).nl();
/*    */     } else {
/* 72 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JForEach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */