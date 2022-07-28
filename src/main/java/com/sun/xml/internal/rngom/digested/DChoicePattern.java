/*    */ package com.sun.xml.internal.rngom.digested;
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
/*    */ public class DChoicePattern
/*    */   extends DContainerPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 55 */     for (DPattern p = firstChild(); p != null; p = p.next) {
/* 56 */       if (p.isNullable())
/* 57 */         return true; 
/* 58 */     }  return false;
/*    */   }
/*    */   public <V> V accept(DPatternVisitor<V> visitor) {
/* 61 */     return visitor.onChoice(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DChoicePattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */