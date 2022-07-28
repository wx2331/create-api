/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Exported
/*    */ public class NativeMethodException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 3924951669039469992L;
/*    */   
/*    */   public NativeMethodException() {}
/*    */   
/*    */   public NativeMethodException(String paramString) {
/* 44 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\NativeMethodException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */