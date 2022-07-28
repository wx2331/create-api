/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.Root;
/*    */ import com.sun.tools.hat.internal.model.StackTrace;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class RootStackQuery
/*    */   extends QueryHandler
/*    */ {
/*    */   public void run() {
/* 51 */     int i = (int)parseHex(this.query);
/* 52 */     Root root = this.snapshot.getRootAt(i);
/* 53 */     if (root == null) {
/* 54 */       error("Root at " + i + " not found");
/*    */       return;
/*    */     } 
/* 57 */     StackTrace stackTrace = root.getStackTrace();
/* 58 */     if (stackTrace == null || (stackTrace.getFrames()).length == 0) {
/* 59 */       error("No stack trace for " + root.getDescription());
/*    */       return;
/*    */     } 
/* 62 */     startHtml("Stack Trace for " + root.getDescription());
/* 63 */     this.out.println("<p>");
/* 64 */     printStackTrace(stackTrace);
/* 65 */     this.out.println("</p>");
/* 66 */     endHtml();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\RootStackQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */