/*     */ package com.sun.tools.internal.xjc.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.MimeType;
/*     */ import javax.activation.MimeTypeParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeTypeRange
/*     */ {
/*     */   public final String majorType;
/*     */   public final String subType;
/*  45 */   public final Map<String, String> parameters = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float q;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MimeTypeRange> parseRanges(String s) throws ParseException {
/*  61 */     StringCutter cutter = new StringCutter(s, true);
/*  62 */     List<MimeTypeRange> r = new ArrayList<>();
/*  63 */     while (cutter.length() > 0) {
/*  64 */       r.add(new MimeTypeRange(cutter));
/*     */     }
/*  66 */     return r;
/*     */   }
/*     */   
/*     */   public MimeTypeRange(String s) throws ParseException {
/*  70 */     this(new StringCutter(s, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MimeTypeRange create(String s) {
/*     */     try {
/*  78 */       return new MimeTypeRange(s);
/*  79 */     } catch (ParseException e) {
/*     */       
/*  81 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MimeTypeRange(StringCutter cutter) throws ParseException {
/*  90 */     this.majorType = cutter.until("/");
/*  91 */     cutter.next("/");
/*  92 */     this.subType = cutter.until("[;,]");
/*     */     
/*  94 */     float q = 1.0F;
/*     */     
/*  96 */     while (cutter.length() > 0) {
/*  97 */       String value, sep = cutter.next("[;,]");
/*  98 */       if (sep.equals(",")) {
/*     */         break;
/*     */       }
/* 101 */       String key = cutter.until("=");
/* 102 */       cutter.next("=");
/*     */       
/* 104 */       char ch = cutter.peek();
/* 105 */       if (ch == '"') {
/*     */         
/* 107 */         cutter.next("\"");
/* 108 */         value = cutter.until("\"");
/* 109 */         cutter.next("\"");
/*     */       } else {
/* 111 */         value = cutter.until("[;,]");
/*     */       } 
/*     */       
/* 114 */       if (key.equals("q")) {
/* 115 */         q = Float.parseFloat(value); continue;
/*     */       } 
/* 117 */       this.parameters.put(key, value);
/*     */     } 
/*     */ 
/*     */     
/* 121 */     this.q = q;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType toMimeType() throws MimeTypeParseException {
/* 127 */     return new MimeType(toString());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 131 */     StringBuilder sb = new StringBuilder(this.majorType + '/' + this.subType);
/* 132 */     if (this.q != 1.0F) {
/* 133 */       sb.append("; q=").append(this.q);
/*     */     }
/* 135 */     for (Map.Entry<String, String> p : this.parameters.entrySet())
/*     */     {
/* 137 */       sb.append("; ").append(p.getKey()).append('=').append(p.getValue());
/*     */     }
/* 139 */     return sb.toString();
/*     */   }
/*     */   
/* 142 */   public static final MimeTypeRange ALL = create("*/*");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MimeTypeRange merge(Collection<MimeTypeRange> types) {
/* 148 */     if (types.size() == 0) throw new IllegalArgumentException(); 
/* 149 */     if (types.size() == 1) return types.iterator().next();
/*     */     
/* 151 */     String majorType = null;
/* 152 */     for (MimeTypeRange mt : types) {
/* 153 */       if (majorType == null) majorType = mt.majorType; 
/* 154 */       if (!majorType.equals(mt.majorType)) {
/* 155 */         return ALL;
/*     */       }
/*     */     } 
/* 158 */     return create(majorType + "/*");
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws ParseException {
/* 162 */     for (MimeTypeRange m : parseRanges(args[0]))
/* 163 */       System.out.println(m.toString()); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\MimeTypeRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */