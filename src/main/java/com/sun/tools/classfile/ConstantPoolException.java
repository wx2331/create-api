/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConstantPoolException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -2324397349644754565L;
/*    */   public final int index;
/*    */   
/*    */   ConstantPoolException(int paramInt) {
/* 38 */     this.index = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\ConstantPoolException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */