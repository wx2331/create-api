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
/*    */ @Exported
/*    */ public class ObjectCollectedException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -1928428056197269588L;
/*    */   
/*    */   public ObjectCollectedException() {}
/*    */   
/*    */   public ObjectCollectedException(String paramString) {
/* 43 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ObjectCollectedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */