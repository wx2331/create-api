/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   public static void main(String... paramVarArgs) {
/*  54 */     System.exit(execute(paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(String... paramVarArgs) {
/*  63 */     Start start = new Start();
/*  64 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(ClassLoader paramClassLoader, String... paramVarArgs) {
/*  78 */     Start start = new Start(paramClassLoader);
/*  79 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(String paramString, String... paramVarArgs) {
/*  89 */     Start start = new Start(paramString);
/*  90 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(String paramString, ClassLoader paramClassLoader, String... paramVarArgs) {
/* 105 */     Start start = new Start(paramString, paramClassLoader);
/* 106 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(String paramString1, String paramString2, String... paramVarArgs) {
/* 119 */     Start start = new Start(paramString1, paramString2);
/* 120 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(String paramString1, String paramString2, ClassLoader paramClassLoader, String... paramVarArgs) {
/* 139 */     Start start = new Start(paramString1, paramString2, paramClassLoader);
/* 140 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2, String... paramVarArgs) {
/* 159 */     Start start = new Start(paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3, paramString2);
/*     */ 
/*     */     
/* 162 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int execute(String paramString1, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3, String paramString2, ClassLoader paramClassLoader, String... paramVarArgs) {
/* 187 */     Start start = new Start(paramString1, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3, paramString2, paramClassLoader);
/*     */ 
/*     */ 
/*     */     
/* 191 */     return start.begin(paramVarArgs);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */