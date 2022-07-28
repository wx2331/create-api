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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HackJavaValue
/*    */   extends JavaValue
/*    */ {
/*    */   private String value;
/*    */   private int size;
/*    */   
/*    */   public HackJavaValue(String paramString, int paramInt) {
/* 53 */     this.value = paramString;
/* 54 */     this.size = paramInt;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 58 */     return this.value;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 62 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\HackJavaValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */