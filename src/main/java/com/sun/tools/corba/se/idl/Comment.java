/*     */ package com.sun.tools.corba.se.idl;
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
/*     */ public class Comment
/*     */ {
/*     */   static final int UNKNOWN = -1;
/*     */   static final int JAVA_DOC = 0;
/*     */   static final int C_BLOCK = 1;
/*     */   static final int CPP_LINE = 2;
/*  53 */   private static String _eol = System.getProperty("line.separator");
/*     */   
/*  55 */   private String _text = new String("");
/*  56 */   private int _style = -1;
/*     */   Comment() {
/*  58 */     this._text = new String(""); this._style = -1;
/*     */   } Comment(String paramString) {
/*  60 */     this._text = paramString; this._style = style(this._text);
/*     */   }
/*     */   public void text(String paramString) {
/*  63 */     this._text = paramString; this._style = style(this._text);
/*     */   }
/*     */   public String text() {
/*  66 */     return this._text;
/*     */   }
/*     */ 
/*     */   
/*     */   private int style(String paramString) {
/*  71 */     if (paramString == null)
/*  72 */       return -1; 
/*  73 */     if (paramString.startsWith("/**") && paramString.endsWith("*/"))
/*  74 */       return 0; 
/*  75 */     if (paramString.startsWith("/*") && paramString.endsWith("*/"))
/*  76 */       return 1; 
/*  77 */     if (paramString.startsWith("//")) {
/*  78 */       return 2;
/*     */     }
/*  80 */     return -1;
/*     */   }
/*     */   
/*     */   public void write() {
/*  84 */     System.out.println(this._text);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(String paramString, PrintWriter paramPrintWriter) {
/*  89 */     if (this._text == null || paramPrintWriter == null)
/*     */       return; 
/*  91 */     if (paramString == null)
/*  92 */       paramString = new String(""); 
/*  93 */     switch (this._style) {
/*     */ 
/*     */       
/*     */       case 0:
/*  97 */         print(paramString, paramPrintWriter);
/*     */         break;
/*     */       
/*     */       case 1:
/* 101 */         print(paramString, paramPrintWriter);
/*     */         break;
/*     */       
/*     */       case 2:
/* 105 */         print(paramString, paramPrintWriter);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void print(String paramString, PrintWriter paramPrintWriter) {
/* 117 */     String str1 = this._text.trim() + _eol;
/* 118 */     String str2 = null;
/*     */     
/* 120 */     int i = 0;
/* 121 */     int j = str1.indexOf(_eol);
/* 122 */     int k = str1.length() - 1;
/*     */     
/* 124 */     paramPrintWriter.println();
/* 125 */     while (i < k) {
/*     */       
/* 127 */       str2 = str1.substring(i, j);
/* 128 */       paramPrintWriter.println(paramString + str2);
/* 129 */       i = j + _eol.length();
/* 130 */       j = i + str1.substring(i).indexOf(_eol);
/*     */     } 
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
/*     */   private void printJavaDoc(String paramString, PrintWriter paramPrintWriter) {
/* 144 */     String str1 = this._text.substring(3, this._text.length() - 2).trim() + _eol;
/* 145 */     String str2 = null;
/*     */     
/* 147 */     int i = 0;
/* 148 */     int j = str1.indexOf(_eol);
/* 149 */     int k = str1.length() - 1;
/*     */     
/* 151 */     paramPrintWriter.println(_eol + paramString + "/**");
/* 152 */     while (i < k) {
/*     */       
/* 154 */       str2 = str1.substring(i, j).trim();
/* 155 */       if (str2.startsWith("*")) {
/*     */         
/* 157 */         paramPrintWriter.println(paramString + " * " + str2.substring(1, str2.length()).trim());
/*     */       } else {
/* 159 */         paramPrintWriter.println(paramString + " * " + str2);
/* 160 */       }  i = j + _eol.length();
/* 161 */       j = i + str1.substring(i).indexOf(_eol);
/*     */     } 
/* 163 */     paramPrintWriter.println(paramString + " */");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printCBlock(String paramString, PrintWriter paramPrintWriter) {
/* 170 */     String str1 = this._text.substring(2, this._text.length() - 2).trim() + _eol;
/* 171 */     String str2 = null;
/*     */     
/* 173 */     int i = 0;
/* 174 */     int j = str1.indexOf(_eol);
/* 175 */     int k = str1.length() - 1;
/*     */     
/* 177 */     paramPrintWriter.println(paramString + "/*");
/* 178 */     while (i < k) {
/*     */       
/* 180 */       str2 = str1.substring(i, j).trim();
/* 181 */       if (str2.startsWith("*")) {
/*     */         
/* 183 */         paramPrintWriter.println(paramString + " * " + str2.substring(1, str2.length()).trim());
/*     */       } else {
/* 185 */         paramPrintWriter.println(paramString + " * " + str2);
/* 186 */       }  i = j + _eol.length();
/* 187 */       j = i + str1.substring(i).indexOf(_eol);
/*     */     } 
/* 189 */     paramPrintWriter.println(paramString + " */");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void printCppLine(String paramString, PrintWriter paramPrintWriter) {
/* 195 */     paramPrintWriter.println(paramString + "//");
/*     */     
/* 197 */     paramPrintWriter.println(paramString + "// " + this._text.substring(2).trim());
/* 198 */     paramPrintWriter.println(paramString + "//");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\Comment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */