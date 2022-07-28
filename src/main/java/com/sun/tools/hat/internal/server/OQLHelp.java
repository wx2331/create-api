/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class OQLHelp
/*    */   extends QueryHandler
/*    */ {
/*    */   public void run() {
/* 49 */     InputStream inputStream = getClass().getResourceAsStream("/com/sun/tools/hat/resources/oqlhelp.html");
/* 50 */     int i = -1;
/*    */     try {
/* 52 */       inputStream = new BufferedInputStream(inputStream);
/* 53 */       while ((i = inputStream.read()) != -1) {
/* 54 */         this.out.print((char)i);
/*    */       }
/* 56 */     } catch (Exception exception) {
/* 57 */       printException(exception);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\OQLHelp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */