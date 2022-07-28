/*    */ package com.sun.tools.javac.main;
/*    */ 
/*    */ import com.sun.tools.javac.util.ListBuffer;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.StreamTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandLine
/*    */ {
/*    */   public static String[] parse(String[] paramArrayOfString) throws IOException {
/* 57 */     ListBuffer<String> listBuffer = new ListBuffer();
/* 58 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 59 */       String str = paramArrayOfString[b];
/* 60 */       if (str.length() > 1 && str.charAt(0) == '@') {
/* 61 */         str = str.substring(1);
/* 62 */         if (str.charAt(0) == '@') {
/* 63 */           listBuffer.append(str);
/*    */         } else {
/* 65 */           loadCmdFile(str, listBuffer);
/*    */         } 
/*    */       } else {
/* 68 */         listBuffer.append(str);
/*    */       } 
/*    */     } 
/* 71 */     return (String[])listBuffer.toList().toArray((Object[])new String[listBuffer.length()]);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static void loadCmdFile(String paramString, ListBuffer<String> paramListBuffer) throws IOException {
/* 77 */     BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
/* 78 */     StreamTokenizer streamTokenizer = new StreamTokenizer(bufferedReader);
/* 79 */     streamTokenizer.resetSyntax();
/* 80 */     streamTokenizer.wordChars(32, 255);
/* 81 */     streamTokenizer.whitespaceChars(0, 32);
/* 82 */     streamTokenizer.commentChar(35);
/* 83 */     streamTokenizer.quoteChar(34);
/* 84 */     streamTokenizer.quoteChar(39);
/* 85 */     while (streamTokenizer.nextToken() != -1) {
/* 86 */       paramListBuffer.append(streamTokenizer.sval);
/*    */     }
/* 88 */     bufferedReader.close();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\main\CommandLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */