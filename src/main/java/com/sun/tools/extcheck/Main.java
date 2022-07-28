/*    */ package com.sun.tools.extcheck;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Main
/*    */ {
/*    */   public static final String INSUFFICIENT = "Insufficient number of arguments";
/*    */   public static final String MISSING = "Missing <jar file> argument";
/*    */   public static final String DOES_NOT_EXIST = "Jarfile does not exist: ";
/*    */   public static final String EXTRA = "Extra command line argument: ";
/*    */   
/*    */   public static void main(String[] paramArrayOfString) {
/*    */     try {
/* 48 */       realMain(paramArrayOfString);
/* 49 */     } catch (Exception exception) {
/* 50 */       System.err.println(exception.getMessage());
/* 51 */       System.exit(-1);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void realMain(String[] paramArrayOfString) throws Exception {
/* 56 */     if (paramArrayOfString.length < 1) {
/* 57 */       usage("Insufficient number of arguments");
/*    */     }
/* 59 */     byte b = 0;
/* 60 */     boolean bool = false;
/* 61 */     if (paramArrayOfString[b].equals("-verbose")) {
/* 62 */       bool = true;
/* 63 */       b++;
/* 64 */       if (b >= paramArrayOfString.length) {
/* 65 */         usage("Missing <jar file> argument");
/*    */       }
/*    */     } 
/* 68 */     String str = paramArrayOfString[b];
/* 69 */     b++;
/* 70 */     File file = new File(str);
/* 71 */     if (!file.exists()) {
/* 72 */       usage("Jarfile does not exist: " + str);
/*    */     }
/* 74 */     if (b < paramArrayOfString.length) {
/* 75 */       usage("Extra command line argument: " + paramArrayOfString[b]);
/*    */     }
/* 77 */     ExtCheck extCheck = ExtCheck.create(file, bool);
/* 78 */     boolean bool1 = extCheck.checkInstalledAgainstTarget();
/* 79 */     if (bool1) {
/* 80 */       System.exit(0);
/*    */     } else {
/* 82 */       System.exit(1);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void usage(String paramString) throws Exception {
/* 87 */     throw new Exception(paramString + "\nUsage: extcheck [-verbose] <jar file>");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\extcheck\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */