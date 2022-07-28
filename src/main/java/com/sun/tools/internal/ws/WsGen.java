/*    */ package com.sun.tools.internal.ws;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.WsgenTool;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WsGen
/*    */ {
/*    */   public static void main(String[] args) throws Throwable {
/* 42 */     System.exit(Invoker.invoke("com.sun.tools.internal.ws.wscompile.WsgenTool", args));
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
/* 59 */     return (new WsgenTool(System.out)).run(args) ? 0 : 1;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\WsGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */