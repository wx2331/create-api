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
/*    */ public class DRefPattern
/*    */   extends DPattern
/*    */ {
/*    */   private final DDefine target;
/*    */   
/*    */   public DRefPattern(DDefine target) {
/* 55 */     this.target = target;
/*    */   }
/*    */   
/*    */   public boolean isNullable() {
/* 59 */     return this.target.isNullable();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DDefine getTarget() {
/* 66 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 73 */     return this.target.getName();
/*    */   }
/*    */   
/*    */   public Object accept(DPatternVisitor visitor) {
/* 77 */     return visitor.onRef(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DRefPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */