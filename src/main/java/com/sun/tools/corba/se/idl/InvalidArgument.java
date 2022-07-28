/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvalidArgument
/*    */   extends Exception
/*    */ {
/*    */   private String message;
/*    */   
/*    */   public InvalidArgument(String paramString) {
/* 61 */     this.message = null; this.message = Util.getMessage("InvalidArgument.1", paramString) + "\n\n" + Util.getMessage("usage"); } public InvalidArgument() { this.message = null;
/*    */     this.message = Util.getMessage("InvalidArgument.2") + "\n\n" + Util.getMessage("usage"); }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/*    */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\InvalidArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */