/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public final class StringCutter
/*    */ {
/*    */   private final String original;
/*    */   private String s;
/*    */   private boolean ignoreWhitespace;
/*    */   
/*    */   public StringCutter(String s, boolean ignoreWhitespace) {
/* 43 */     this.s = this.original = s;
/* 44 */     this.ignoreWhitespace = ignoreWhitespace;
/*    */   }
/*    */   
/*    */   public void skip(String regexp) throws ParseException {
/* 48 */     next(regexp);
/*    */   }
/*    */   
/*    */   public String next(String regexp) throws ParseException {
/* 52 */     trim();
/* 53 */     Pattern p = Pattern.compile(regexp);
/* 54 */     Matcher m = p.matcher(this.s);
/* 55 */     if (m.lookingAt()) {
/* 56 */       String r = m.group();
/* 57 */       this.s = this.s.substring(r.length());
/* 58 */       trim();
/* 59 */       return r;
/*    */     } 
/* 61 */     throw error();
/*    */   }
/*    */   
/*    */   private ParseException error() {
/* 65 */     return new ParseException(this.original, this.original.length() - this.s.length());
/*    */   }
/*    */   
/*    */   public String until(String regexp) throws ParseException {
/* 69 */     Pattern p = Pattern.compile(regexp);
/* 70 */     Matcher m = p.matcher(this.s);
/* 71 */     if (m.find()) {
/* 72 */       String str = this.s.substring(0, m.start());
/* 73 */       this.s = this.s.substring(m.start());
/* 74 */       if (this.ignoreWhitespace)
/* 75 */         str = str.trim(); 
/* 76 */       return str;
/*    */     } 
/*    */     
/* 79 */     String r = this.s;
/* 80 */     this.s = "";
/* 81 */     return r;
/*    */   }
/*    */ 
/*    */   
/*    */   public char peek() {
/* 86 */     return this.s.charAt(0);
/*    */   }
/*    */   
/*    */   private void trim() {
/* 90 */     if (this.ignoreWhitespace)
/* 91 */       this.s = this.s.trim(); 
/*    */   }
/*    */   
/*    */   public int length() {
/* 95 */     return this.s.length();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\StringCutter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */