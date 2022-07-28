/*    */ package com.sun.tools.internal.xjc.reader.gbind;
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
/*    */ 
/*    */ 
/*    */ public final class Choice
/*    */   extends Expression
/*    */ {
/*    */   private final Expression lhs;
/*    */   private final Expression rhs;
/*    */   private final boolean isNullable;
/*    */   
/*    */   public Choice(Expression lhs, Expression rhs) {
/* 48 */     this.lhs = lhs;
/* 49 */     this.rhs = rhs;
/* 50 */     this.isNullable = (lhs.isNullable() || rhs.isNullable());
/*    */   }
/*    */   
/*    */   boolean isNullable() {
/* 54 */     return this.isNullable;
/*    */   }
/*    */   
/*    */   ElementSet lastSet() {
/* 58 */     return ElementSets.union(this.lhs.lastSet(), this.rhs.lastSet());
/*    */   }
/*    */   
/*    */   void buildDAG(ElementSet incoming) {
/* 62 */     this.lhs.buildDAG(incoming);
/* 63 */     this.rhs.buildDAG(incoming);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 67 */     return '(' + this.lhs.toString() + '|' + this.rhs.toString() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\gbind\Choice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */