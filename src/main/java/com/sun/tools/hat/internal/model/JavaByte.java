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
/*    */ public class JavaByte
/*    */   extends JavaValue
/*    */ {
/*    */   byte value;
/*    */   
/*    */   public JavaByte(byte paramByte) {
/* 47 */     this.value = paramByte;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 51 */     return "0x" + Integer.toString(this.value & 0xFF, 16);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaByte.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */