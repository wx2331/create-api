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
/*    */ public class DInterleavePattern
/*    */   extends DContainerPattern
/*    */ {
/*    */   public boolean isNullable() {
/* 53 */     for (DPattern p = firstChild(); p != null; p = p.next) {
/* 54 */       if (!p.isNullable())
/* 55 */         return false; 
/* 56 */     }  return true;
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 59 */     return visitor.onInterleave(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DInterleavePattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */