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
/*    */ public class VMDisconnectedException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 2892975269768351637L;
/*    */   
/*    */   public VMDisconnectedException() {}
/*    */   
/*    */   public VMDisconnectedException(String paramString) {
/* 44 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\VMDisconnectedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */