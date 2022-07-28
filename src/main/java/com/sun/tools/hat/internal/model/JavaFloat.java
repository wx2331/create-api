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
/*    */ public class JavaFloat
/*    */   extends JavaValue
/*    */ {
/*    */   float value;
/*    */   
/*    */   public JavaFloat(float paramFloat) {
/* 47 */     this.value = paramFloat;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 51 */     return Float.toString(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaFloat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */