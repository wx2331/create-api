/*    */ package sun.tools.javac;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ final class ErrorMessage
/*    */ {
/*    */   long where;
/*    */   String message;
/*    */   ErrorMessage next;
/*    */   
/*    */   ErrorMessage(long paramLong, String paramString) {
/* 46 */     this.where = paramLong;
/* 47 */     this.message = paramString;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\javac\ErrorMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */