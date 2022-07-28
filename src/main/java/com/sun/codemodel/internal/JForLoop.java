/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ public class JForLoop
/*    */   implements JStatement
/*    */ {
/* 38 */   private List<Object> inits = new ArrayList();
/* 39 */   private JExpression test = null;
/* 40 */   private List<JExpression> updates = new ArrayList<>();
/* 41 */   private JBlock body = null;
/*    */   
/*    */   public JVar init(int mods, JType type, String var, JExpression e) {
/* 44 */     JVar v = new JVar(JMods.forVar(mods), type, var, e);
/* 45 */     this.inits.add(v);
/* 46 */     return v;
/*    */   }
/*    */   
/*    */   public JVar init(JType type, String var, JExpression e) {
/* 50 */     return init(0, type, var, e);
/*    */   }
/*    */   
/*    */   public void init(JVar v, JExpression e) {
/* 54 */     this.inits.add(JExpr.assign(v, e));
/*    */   }
/*    */   
/*    */   public void test(JExpression e) {
/* 58 */     this.test = e;
/*    */   }
/*    */   
/*    */   public void update(JExpression e) {
/* 62 */     this.updates.add(e);
/*    */   }
/*    */   
/*    */   public JBlock body() {
/* 66 */     if (this.body == null) this.body = new JBlock(); 
/* 67 */     return this.body;
/*    */   }
/*    */   
/*    */   public void state(JFormatter f) {
/* 71 */     f.p("for (");
/* 72 */     boolean first = true;
/* 73 */     for (Object o : this.inits) {
/* 74 */       if (!first) f.p(','); 
/* 75 */       if (o instanceof JVar) {
/* 76 */         f.b((JVar)o);
/*    */       } else {
/* 78 */         f.g((JExpression)o);
/* 79 */       }  first = false;
/*    */     } 
/* 81 */     f.p(';').g(this.test).p(';').g((Collection)this.updates).p(')');
/* 82 */     if (this.body != null) {
/* 83 */       f.g(this.body).nl();
/*    */     } else {
/* 85 */       f.p(';').nl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JForLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */