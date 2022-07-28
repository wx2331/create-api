/*    */ package com.sun.tools.javah;
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
/* 45 */     JavahTask javahTask = new JavahTask();
/* 46 */     int i = javahTask.run(paramArrayOfString);
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
/* 57 */     JavahTask javahTask = new JavahTask();
/* 58 */     javahTask.setLog(paramPrintWriter);
/* 59 */     return javahTask.run(paramArrayOfString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */