/*     */ package com.sun.tools.internal.jxc;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.gen.config.NGCCRuntime;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ public final class NGCCRuntimeEx
/*     */   extends NGCCRuntime
/*     */ {
/*     */   private final ErrorHandler errorHandler;
/*     */   
/*     */   public NGCCRuntimeEx(ErrorHandler errorHandler) {
/*  54 */     this.errorHandler = errorHandler;
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
/*     */   public File getBaseDir(String baseDir) throws SAXException {
/*  66 */     File dir = new File(baseDir);
/*  67 */     if (dir.exists()) {
/*  68 */       return dir;
/*     */     }
/*     */ 
/*     */     
/*  72 */     SAXParseException e = new SAXParseException(Messages.BASEDIR_DOESNT_EXIST.format(new Object[] { dir.getAbsolutePath() }, ), getLocator());
/*  73 */     this.errorHandler.error(e);
/*  74 */     throw e;
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
/*     */   public List<Pattern> getIncludePatterns(List<String> includeContent) {
/*  87 */     List<Pattern> includeRegexList = new ArrayList<>();
/*  88 */     for (String includes : includeContent) {
/*  89 */       String regex = convertToRegex(includes);
/*  90 */       Pattern pattern = Pattern.compile(regex);
/*  91 */       includeRegexList.add(pattern);
/*     */     } 
/*  93 */     return includeRegexList;
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
/*     */   public List getExcludePatterns(List<String> excludeContent) {
/* 106 */     List<Pattern> excludeRegexList = new ArrayList<>();
/* 107 */     for (String excludes : excludeContent) {
/* 108 */       String regex = convertToRegex(excludes);
/* 109 */       Pattern pattern = Pattern.compile(regex);
/* 110 */       excludeRegexList.add(pattern);
/*     */     } 
/* 112 */     return excludeRegexList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String convertToRegex(String pattern) {
/* 121 */     StringBuilder regex = new StringBuilder();
/* 122 */     char nc = ' ';
/* 123 */     if (pattern.length() > 0)
/*     */     {
/* 125 */       for (int i = 0; i < pattern.length(); i++) {
/* 126 */         char c = pattern.charAt(i);
/* 127 */         nc = ' ';
/* 128 */         if (i + 1 != pattern.length()) {
/* 129 */           nc = pattern.charAt(i + 1);
/*     */         }
/*     */         
/* 132 */         if (c == '.' && nc != '.') {
/* 133 */           regex.append('\\');
/* 134 */           regex.append('.');
/*     */         }
/* 136 */         else if (c != '.') {
/*     */ 
/*     */           
/* 139 */           if (c == '*' && nc == '*') {
/* 140 */             regex.append(".*");
/*     */             break;
/*     */           } 
/* 143 */           if (c == '*') {
/* 144 */             regex.append("[^\\.]+");
/*     */           
/*     */           }
/* 147 */           else if (c == '?') {
/* 148 */             regex.append("[^\\.]");
/*     */           } else {
/*     */             
/* 151 */             regex.append(c);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 156 */     return regex.toString();
/*     */   }
/*     */   
/*     */   protected void unexpectedX(String token) throws SAXException {
/* 160 */     this.errorHandler.error(new SAXParseException(Messages.UNEXPECTED_NGCC_TOKEN
/* 161 */           .format(new Object[] {
/* 162 */               token, Integer.valueOf(getLocator().getLineNumber()), Integer.valueOf(getLocator().getColumnNumber())
/* 163 */             }, ), getLocator()));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\NGCCRuntimeEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */