/*    */ package com.sun.tools.javap;
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
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] paramArrayOfString) {
/* 45 */     JavapTask javapTask = new JavapTask();
/* 46 */     int i = javapTask.run(paramArrayOfString);
/* 47 */     System.exit(i);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int run(String[] paramArrayOfString, PrintWriter paramPrintWriter) {
/* 57 */     JavapTask javapTask = new JavapTask();
/* 58 */     javapTask.setLog(paramPrintWriter);
/* 59 */     return javapTask.run(paramArrayOfString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */