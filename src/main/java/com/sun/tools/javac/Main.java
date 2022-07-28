/*    */ package com.sun.tools.javac;
/*    */ 
/*    */ import com.sun.tools.javac.main.Main;
/*    */ import java.io.PrintWriter;
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
/*    */ 
/*    */ 
/*    */ @Exported
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] paramArrayOfString) throws Exception {
/* 42 */     System.exit(compile(paramArrayOfString));
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
/*    */   public static int compile(String[] paramArrayOfString) {
/* 54 */     Main main = new Main("javac");
/*    */     
/* 56 */     return (main.compile(paramArrayOfString)).exitCode;
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
/*    */   public static int compile(String[] paramArrayOfString, PrintWriter paramPrintWriter) {
/* 72 */     Main main = new Main("javac", paramPrintWriter);
/*    */     
/* 74 */     return (main.compile(paramArrayOfString)).exitCode;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */