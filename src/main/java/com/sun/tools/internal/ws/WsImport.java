/*    */ package com.sun.tools.internal.ws;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.WsimportTool;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WsImport
/*    */ {
/*    */   public static void main(String[] args) throws Throwable {
/* 42 */     System.exit(Invoker.invoke("com.sun.tools.internal.ws.wscompile.WsimportTool", args));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int doMain(String[] args) throws Throwable {
/* 59 */     return (new WsimportTool(System.out)).run(args) ? 0 : 1;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\WsImport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */