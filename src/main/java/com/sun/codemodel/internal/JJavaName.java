/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class JJavaName
/*     */ {
/*     */   public static boolean isJavaIdentifier(String s) {
/*  42 */     if (s.length() == 0) return false; 
/*  43 */     if (reservedKeywords.contains(s)) return false;
/*     */     
/*  45 */     if (!Character.isJavaIdentifierStart(s.charAt(0))) return false;
/*     */     
/*  47 */     for (int i = 1; i < s.length(); i++) {
/*  48 */       if (!Character.isJavaIdentifierPart(s.charAt(i)))
/*  49 */         return false; 
/*     */     } 
/*  51 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullyQualifiedClassName(String s) {
/*  58 */     return isJavaPackageName(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJavaPackageName(String s) {
/*  65 */     while (s.length() != 0) {
/*  66 */       int idx = s.indexOf('.');
/*  67 */       if (idx == -1) idx = s.length(); 
/*  68 */       if (!isJavaIdentifier(s.substring(0, idx))) {
/*  69 */         return false;
/*     */       }
/*  71 */       s = s.substring(idx);
/*  72 */       if (s.length() != 0) s = s.substring(1); 
/*     */     } 
/*  74 */     return true;
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
/*     */   public static String getPluralForm(String word) {
/*     */     int j;
/*  99 */     boolean allUpper = true;
/*     */ 
/*     */ 
/*     */     
/* 103 */     for (int i = 0; i < word.length(); i++) {
/* 104 */       char ch = word.charAt(i);
/* 105 */       if (ch >= '') {
/* 106 */         return word;
/*     */       }
/*     */       
/* 109 */       j = allUpper & (!Character.isLowerCase(ch) ? 1 : 0);
/*     */     } 
/*     */     
/* 112 */     for (Entry e : TABLE) {
/* 113 */       String r = e.apply(word);
/* 114 */       if (r != null) {
/* 115 */         if (j != 0) r = r.toUpperCase(); 
/* 116 */         return r;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 121 */     return word;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 126 */   private static HashSet<String> reservedKeywords = new HashSet<>();
/*     */   private static final Entry[] TABLE;
/*     */   
/*     */   static {
/* 130 */     String[] words = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null", "assert", "enum" };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     for (String w : words) {
/* 192 */       reservedKeywords.add(w);
/*     */     }
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
/* 220 */     String[] source = { "(.*)child", "$1children", "(.+)fe", "$1ves", "(.*)mouse", "$1mise", "(.+)f", "$1ves", "(.+)ch", "$1ches", "(.+)sh", "$1shes", "(.*)tooth", "$1teeth", "(.+)um", "$1a", "(.+)an", "$1en", "(.+)ato", "$1atoes", "(.*)basis", "$1bases", "(.*)axis", "$1axes", "(.+)is", "$1ises", "(.+)ss", "$1sses", "(.+)us", "$1uses", "(.+)s", "$1s", "(.*)foot", "$1feet", "(.+)ix", "$1ixes", "(.+)ex", "$1ices", "(.+)nx", "$1nxes", "(.+)x", "$1xes", "(.+)y", "$1ies", "(.+)", "$1s" };
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
/* 246 */     TABLE = new Entry[source.length / 2];
/*     */     
/* 248 */     for (int i = 0; i < source.length; i += 2)
/* 249 */       TABLE[i / 2] = new Entry(source[i], source[i + 1]); 
/*     */   }
/*     */   
/*     */   private static class Entry {
/*     */     private final Pattern pattern;
/*     */     private final String replacement;
/*     */     
/*     */     public Entry(String pattern, String replacement) {
/*     */       this.pattern = Pattern.compile(pattern, 2);
/*     */       this.replacement = replacement;
/*     */     }
/*     */     
/*     */     String apply(String word) {
/*     */       Matcher m = this.pattern.matcher(word);
/*     */       if (m.matches()) {
/*     */         StringBuffer buf = new StringBuffer();
/*     */         m.appendReplacement(buf, this.replacement);
/*     */         return buf.toString();
/*     */       } 
/*     */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JJavaName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */