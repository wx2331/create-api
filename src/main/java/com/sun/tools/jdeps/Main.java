/*    */ package com.sun.tools.jdeps;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String... paramVarArgs) throws Exception {
/* 47 */     JdepsTask jdepsTask = new JdepsTask();
/* 48 */     int i = jdepsTask.run(paramVarArgs);
/* 49 */     System.exit(i);
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
/*    */   public static int run(String[] paramArrayOfString, PrintWriter paramPrintWriter) {
/* 61 */     JdepsTask jdepsTask = new JdepsTask();
/* 62 */     jdepsTask.setLog(paramPrintWriter);
/* 63 */     return jdepsTask.run(paramArrayOfString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */