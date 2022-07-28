/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaBoolean
/*    */   extends JavaValue
/*    */ {
/*    */   boolean value;
/*    */   
/*    */   public JavaBoolean(boolean paramBoolean) {
/* 47 */     this.value = paramBoolean;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 51 */     return "" + this.value;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaBoolean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */