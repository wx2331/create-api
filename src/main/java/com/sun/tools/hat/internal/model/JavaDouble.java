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
/*    */ public class JavaDouble
/*    */   extends JavaValue
/*    */ {
/*    */   double value;
/*    */   
/*    */   public JavaDouble(double paramDouble) {
/* 47 */     this.value = paramDouble;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 51 */     return Double.toString(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */