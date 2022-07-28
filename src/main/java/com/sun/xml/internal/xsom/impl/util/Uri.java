/*     */ package com.sun.xml.internal.xsom.impl.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
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
/*     */ public class Uri
/*     */ {
/*  43 */   private static String utf8 = "UTF-8";
/*     */   
/*     */   public static boolean isValid(String s) {
/*  46 */     return (isValidPercent(s) && isValidFragment(s) && isValidScheme(s));
/*     */   }
/*     */   
/*     */   private static final String HEX_DIGITS = "0123456789abcdef";
/*     */   
/*     */   public static String escapeDisallowedChars(String s) {
/*  52 */     StringBuffer buf = null;
/*  53 */     int len = s.length();
/*  54 */     int done = 0; while (true) {
/*     */       byte[] bytes;
/*  56 */       int i = done;
/*     */       while (true) {
/*  58 */         if (i == len) {
/*  59 */           if (done == 0)
/*  60 */             return s; 
/*     */           break;
/*     */         } 
/*  63 */         if (isExcluded(s.charAt(i)))
/*     */           break; 
/*  65 */         i++;
/*     */       } 
/*  67 */       if (buf == null)
/*  68 */         buf = new StringBuffer(); 
/*  69 */       if (i > done) {
/*  70 */         buf.append(s.substring(done, i));
/*  71 */         done = i;
/*     */       } 
/*  73 */       if (i == len)
/*     */         break; 
/*  75 */       for (; ++i < len && isExcluded(s.charAt(i)); i++);
/*     */       
/*  77 */       String tem = s.substring(done, i);
/*     */       
/*     */       try {
/*  80 */         bytes = tem.getBytes(utf8);
/*     */       }
/*  82 */       catch (UnsupportedEncodingException e) {
/*  83 */         utf8 = "UTF8";
/*     */         try {
/*  85 */           bytes = tem.getBytes(utf8);
/*     */         }
/*  87 */         catch (UnsupportedEncodingException e2) {
/*     */           
/*  89 */           return s;
/*     */         } 
/*     */       } 
/*  92 */       for (int j = 0; j < bytes.length; j++) {
/*  93 */         buf.append('%');
/*  94 */         buf.append("0123456789abcdef".charAt((bytes[j] & 0xFF) >> 4));
/*  95 */         buf.append("0123456789abcdef".charAt(bytes[j] & 0xF));
/*     */       } 
/*  97 */       done = i;
/*     */     } 
/*  99 */     return buf.toString();
/*     */   }
/*     */   
/* 102 */   private static String excluded = "<>\"{}|\\^`";
/*     */   
/*     */   private static boolean isExcluded(char c) {
/* 105 */     return (c <= ' ' || c >= '' || excluded.indexOf(c) >= 0);
/*     */   }
/*     */   
/*     */   private static boolean isAlpha(char c) {
/* 109 */     return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'));
/*     */   }
/*     */   
/*     */   private static boolean isHexDigit(char c) {
/* 113 */     return (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F') || isDigit(c));
/*     */   }
/*     */   
/*     */   private static boolean isDigit(char c) {
/* 117 */     return ('0' <= c && c <= '9');
/*     */   }
/*     */   
/*     */   private static boolean isSchemeChar(char c) {
/* 121 */     return (isAlpha(c) || isDigit(c) || c == '+' || c == '-' || c == '.');
/*     */   }
/*     */   
/*     */   private static boolean isValidPercent(String s) {
/* 125 */     int len = s.length();
/* 126 */     for (int i = 0; i < len; i++) {
/* 127 */       if (s.charAt(i) == '%') {
/* 128 */         if (i + 2 >= len)
/* 129 */           return false; 
/* 130 */         if (!isHexDigit(s.charAt(i + 1)) || 
/* 131 */           !isHexDigit(s.charAt(i + 2)))
/* 132 */           return false; 
/*     */       } 
/* 134 */     }  return true;
/*     */   }
/*     */   
/*     */   private static boolean isValidFragment(String s) {
/* 138 */     int i = s.indexOf('#');
/* 139 */     return (i < 0 || s.indexOf('#', i + 1) < 0);
/*     */   }
/*     */   
/*     */   private static boolean isValidScheme(String s) {
/* 143 */     if (!isAbsolute(s))
/* 144 */       return true; 
/* 145 */     int i = s.indexOf(':');
/* 146 */     if (i == 0 || i + 1 == s
/* 147 */       .length() || 
/* 148 */       !isAlpha(s.charAt(0)))
/* 149 */       return false; 
/* 150 */     while (--i > 0) {
/* 151 */       if (!isSchemeChar(s.charAt(i)))
/* 152 */         return false; 
/* 153 */     }  return true;
/*     */   }
/*     */   
/*     */   public static String resolve(String baseUri, String uriReference) throws IOException {
/* 157 */     if (isAbsolute(uriReference)) {
/* 158 */       return uriReference;
/*     */     }
/* 160 */     if (baseUri == null) {
/* 161 */       throw new IOException("Unable to resolve relative URI " + uriReference + " without a base URI");
/*     */     }
/* 163 */     if (!isAbsolute(baseUri)) {
/* 164 */       throw new IOException("Unable to resolve relative URI " + uriReference + " because base URI is not absolute: " + baseUri);
/*     */     }
/* 166 */     return (new URL(new URL(baseUri), uriReference)).toString();
/*     */   }
/*     */   
/*     */   public static boolean hasFragmentId(String uri) {
/* 170 */     return (uri.indexOf('#') >= 0);
/*     */   }
/*     */   
/*     */   public static boolean isAbsolute(String uri) {
/* 174 */     int i = uri.indexOf(':');
/* 175 */     if (i < 0)
/* 176 */       return false; 
/* 177 */     while (--i >= 0) {
/* 178 */       switch (uri.charAt(i)) {
/*     */         case '#':
/*     */         case '/':
/*     */         case '?':
/* 182 */           return false;
/*     */       } 
/*     */     } 
/* 185 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\imp\\util\Uri.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */