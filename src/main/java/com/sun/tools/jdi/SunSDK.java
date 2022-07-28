/*    */ package com.sun.tools.jdi;
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
/*    */ class SunSDK
/*    */ {
/*    */   static String home() {
/* 40 */     File file1 = new File(System.getProperty("java.home"));
/* 41 */     File file2 = new File(file1.getParent());
/*    */ 
/*    */     
/* 44 */     String str = "bin" + File.separator + System.mapLibraryName("jdwp");
/* 45 */     File file3 = new File(file2, str);
/* 46 */     return file3.exists() ? file2.getAbsolutePath() : null;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SunSDK.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */