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
/*    */ public abstract class DUnaryPattern
/*    */   extends DPattern
/*    */ {
/*    */   private DPattern child;
/*    */   
/*    */   public DPattern getChild() {
/* 55 */     return this.child;
/*    */   }
/*    */   
/*    */   public void setChild(DPattern child) {
/* 59 */     this.child = child;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DUnaryPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */