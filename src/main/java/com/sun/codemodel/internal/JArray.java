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
/*    */ public final class JArray
/*    */   extends JExpressionImpl
/*    */ {
/*    */   private final JType type;
/*    */   private final JExpression size;
/* 39 */   private List<JExpression> exprs = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JArray add(JExpression e) {
/* 45 */     if (this.exprs == null)
/* 46 */       this.exprs = new ArrayList<>(); 
/* 47 */     this.exprs.add(e);
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   JArray(JType type, JExpression size) {
/* 52 */     this.type = type;
/* 53 */     this.size = size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(JFormatter f) {
/* 60 */     int arrayCount = 0;
/* 61 */     JType t = this.type;
/*    */     
/* 63 */     while (t.isArray()) {
/* 64 */       t = t.elementType();
/* 65 */       arrayCount++;
/*    */     } 
/*    */     
/* 68 */     f.p("new").g(t).p('[');
/* 69 */     if (this.size != null)
/* 70 */       f.g(this.size); 
/* 71 */     f.p(']');
/*    */     
/* 73 */     for (int i = 0; i < arrayCount; i++) {
/* 74 */       f.p("[]");
/*    */     }
/* 76 */     if (this.size == null || this.exprs != null)
/* 77 */       f.p('{'); 
/* 78 */     if (this.exprs != null) {
/* 79 */       f.g((Collection)this.exprs);
/*    */     } else {
/* 81 */       f.p(' ');
/*    */     } 
/* 83 */     if (this.size == null || this.exprs != null)
/* 84 */       f.p('}'); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */