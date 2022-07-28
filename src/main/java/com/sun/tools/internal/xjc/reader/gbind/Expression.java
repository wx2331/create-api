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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Expression
/*    */ {
/* 55 */   public static final Expression EPSILON = new Expression() {
/*    */       ElementSet lastSet() {
/* 57 */         return ElementSet.EMPTY_SET;
/*    */       }
/*    */       
/*    */       boolean isNullable() {
/* 61 */         return true;
/*    */       }
/*    */ 
/*    */       
/*    */       void buildDAG(ElementSet incoming) {}
/*    */ 
/*    */       
/*    */       public String toString() {
/* 69 */         return "-";
/*    */       }
/*    */     };
/*    */   
/*    */   abstract ElementSet lastSet();
/*    */   
/*    */   abstract boolean isNullable();
/*    */   
/*    */   abstract void buildDAG(ElementSet paramElementSet);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\gbind\Expression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */