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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Sequence
/*    */   extends Expression
/*    */ {
/*    */   private final Expression lhs;
/*    */   private final Expression rhs;
/*    */   private final boolean isNullable;
/*    */   private ElementSet lastSet;
/*    */   
/*    */   public Sequence(Expression lhs, Expression rhs) {
/* 55 */     this.lhs = lhs;
/* 56 */     this.rhs = rhs;
/* 57 */     this.isNullable = (lhs.isNullable() && rhs.isNullable());
/*    */   }
/*    */   
/*    */   ElementSet lastSet() {
/* 61 */     if (this.lastSet == null)
/* 62 */       if (this.rhs.isNullable()) {
/* 63 */         this.lastSet = ElementSets.union(this.lhs.lastSet(), this.rhs.lastSet());
/*    */       } else {
/* 65 */         this.lastSet = this.rhs.lastSet();
/*    */       }  
/* 67 */     return this.lastSet;
/*    */   }
/*    */   
/*    */   boolean isNullable() {
/* 71 */     return this.isNullable;
/*    */   }
/*    */   
/*    */   void buildDAG(ElementSet incoming) {
/* 75 */     this.lhs.buildDAG(incoming);
/* 76 */     if (this.lhs.isNullable()) {
/* 77 */       this.rhs.buildDAG(ElementSets.union(incoming, this.lhs.lastSet()));
/*    */     } else {
/* 79 */       this.rhs.buildDAG(this.lhs.lastSet());
/*    */     } 
/*    */   }
/*    */   public String toString() {
/* 83 */     return '(' + this.lhs.toString() + ',' + this.rhs.toString() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\gbind\Sequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */