/*    */ package com.sun.jdi.request;
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
/*    */ public class DuplicateRequestException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -3719784920313411060L;
/*    */   
/*    */   public DuplicateRequestException() {}
/*    */   
/*    */   public DuplicateRequestException(String paramString) {
/* 43 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\DuplicateRequestException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */