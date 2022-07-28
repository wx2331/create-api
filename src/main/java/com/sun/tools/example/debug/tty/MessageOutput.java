/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageOutput
/*     */ {
/*     */   static ResourceBundle textResources;
/*     */   private static MessageFormat messageFormat;
/*     */   
/*     */   static void fatalError(String paramString) {
/*  63 */     System.err.println();
/*  64 */     System.err.println(format("Fatal error"));
/*  65 */     System.err.println(format(paramString));
/*  66 */     Env.shutdown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String format(String paramString) {
/*  73 */     return textResources.getString(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String format(String paramString1, String paramString2) {
/*  81 */     return format(paramString1, new Object[] { paramString2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static synchronized String format(String paramString, Object[] paramArrayOfObject) {
/*  88 */     if (messageFormat == null) {
/*  89 */       messageFormat = new MessageFormat(textResources.getString(paramString));
/*     */     } else {
/*  91 */       messageFormat.applyPattern(textResources.getString(paramString));
/*     */     } 
/*  93 */     return messageFormat.format(paramArrayOfObject);
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
/*     */   static void printDirectln(String paramString) {
/* 108 */     System.out.println(paramString);
/*     */   }
/*     */   static void printDirect(String paramString) {
/* 111 */     System.out.print(paramString);
/*     */   }
/*     */   static void printDirect(char paramChar) {
/* 114 */     System.out.print(paramChar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void println() {
/* 122 */     System.out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void print(String paramString) {
/* 129 */     System.out.print(format(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void println(String paramString) {
/* 135 */     System.out.println(format(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void print(String paramString1, String paramString2) {
/* 144 */     System.out.print(format(paramString1, paramString2));
/*     */   }
/*     */   static void println(String paramString1, String paramString2) {
/* 147 */     System.out.println(format(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void println(String paramString, Object[] paramArrayOfObject) {
/* 155 */     System.out.println(format(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void lnprint(String paramString) {
/* 162 */     System.out.println();
/* 163 */     System.out.print(textResources.getString(paramString));
/*     */   }
/*     */   
/*     */   static void lnprint(String paramString1, String paramString2) {
/* 167 */     System.out.println();
/* 168 */     System.out.print(format(paramString1, paramString2));
/*     */   }
/*     */   
/*     */   static void lnprint(String paramString, Object[] paramArrayOfObject) {
/* 172 */     System.out.println();
/* 173 */     System.out.print(format(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void printException(String paramString, Exception paramException) {
/* 180 */     if (paramString != null) {
/*     */       try {
/* 182 */         println(paramString);
/* 183 */       } catch (MissingResourceException missingResourceException) {
/* 184 */         printDirectln(paramString);
/*     */       } 
/*     */     }
/* 187 */     System.out.flush();
/* 188 */     paramException.printStackTrace();
/*     */   }
/*     */   
/*     */   static void printPrompt() {
/* 192 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/* 193 */     if (threadInfo == null) {
/* 194 */       System.out
/* 195 */         .print(format("jdb prompt with no current thread"));
/*     */     } else {
/* 197 */       System.out
/* 198 */         .print(format("jdb prompt thread name and current stack frame", new Object[] {
/*     */               
/* 200 */               threadInfo.getThread().name(), new Integer(threadInfo
/* 201 */                 .getCurrentFrameIndex() + 1) }));
/*     */     } 
/* 203 */     System.out.flush();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\MessageOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */