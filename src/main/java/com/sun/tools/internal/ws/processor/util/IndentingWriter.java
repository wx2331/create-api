/*     */ package com.sun.tools.internal.ws.processor.util;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.generator.GeneratorException;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.text.MessageFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndentingWriter
/*     */   extends BufferedWriter
/*     */ {
/*     */   private boolean beginningOfLine = true;
/*  44 */   private int currentIndent = 0;
/*  45 */   private int indentStep = 4;
/*     */   
/*     */   public IndentingWriter(Writer out) {
/*  48 */     super(out);
/*     */   }
/*     */   
/*     */   public IndentingWriter(Writer out, int step) {
/*  52 */     this(out);
/*     */     
/*  54 */     if (this.indentStep < 0) {
/*  55 */       throw new IllegalArgumentException("negative indent step");
/*     */     }
/*  57 */     this.indentStep = step;
/*     */   }
/*     */   
/*     */   public void write(int c) throws IOException {
/*  61 */     checkWrite();
/*  62 */     super.write(c);
/*     */   }
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*  66 */     if (len > 0) {
/*  67 */       checkWrite();
/*     */     }
/*  69 */     super.write(cbuf, off, len);
/*     */   }
/*     */   
/*     */   public void write(String s, int off, int len) throws IOException {
/*  73 */     if (len > 0) {
/*  74 */       checkWrite();
/*     */     }
/*  76 */     super.write(s, off, len);
/*     */   }
/*     */   
/*     */   public void newLine() throws IOException {
/*  80 */     super.newLine();
/*  81 */     this.beginningOfLine = true;
/*     */   }
/*     */   
/*     */   protected void checkWrite() throws IOException {
/*  85 */     if (this.beginningOfLine) {
/*  86 */       this.beginningOfLine = false;
/*  87 */       int i = this.currentIndent;
/*  88 */       while (i > 0) {
/*  89 */         super.write(32);
/*  90 */         i--;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void indentIn() {
/*  96 */     this.currentIndent += this.indentStep;
/*     */   }
/*     */   
/*     */   protected void indentOut() {
/* 100 */     this.currentIndent -= this.indentStep;
/* 101 */     if (this.currentIndent < 0) {
/* 102 */       this.currentIndent = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public void pI() {
/* 107 */     indentIn();
/*     */   }
/*     */   
/*     */   public void pO() {
/* 111 */     indentOut();
/*     */   }
/*     */   
/*     */   public void pI(int levels) {
/* 115 */     for (int i = 0; i < levels; i++) {
/* 116 */       indentIn();
/*     */     }
/*     */   }
/*     */   
/*     */   public void pO(int levels) {
/* 121 */     for (int i = 0; i < levels; i++) {
/* 122 */       indentOut();
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
/*     */ 
/*     */   
/*     */   public void p(String s) throws IOException {
/* 138 */     boolean canEncode = true;
/*     */ 
/*     */     
/*     */     try {
/* 142 */       if (!canEncode(s)) {
/* 143 */         canEncode = false;
/*     */       }
/* 145 */     } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (!canEncode) {
/* 152 */       throw new GeneratorException("generator.indentingwriter.charset.cantencode", new Object[] { s });
/*     */     }
/*     */     
/* 155 */     write(s);
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
/*     */   protected boolean canEncode(String s) {
/* 169 */     CharsetEncoder encoder = Charset.forName(System.getProperty("file.encoding")).newEncoder();
/* 170 */     char[] chars = s.toCharArray();
/* 171 */     for (int i = 0; i < chars.length; i++) {
/* 172 */       if (!encoder.canEncode(chars[i])) {
/* 173 */         return false;
/*     */       }
/*     */     } 
/* 176 */     return true;
/*     */   }
/*     */   
/*     */   public void p(String s1, String s2) throws IOException {
/* 180 */     p(s1);
/* 181 */     p(s2);
/*     */   }
/*     */   
/*     */   public void p(String s1, String s2, String s3) throws IOException {
/* 185 */     p(s1);
/* 186 */     p(s2);
/* 187 */     p(s3);
/*     */   }
/*     */   
/*     */   public void p(String s1, String s2, String s3, String s4) throws IOException {
/* 191 */     p(s1);
/* 192 */     p(s2);
/* 193 */     p(s3);
/* 194 */     p(s4);
/*     */   }
/*     */   
/*     */   public void p(String s1, String s2, String s3, String s4, String s5) throws IOException {
/* 198 */     p(s1);
/* 199 */     p(s2);
/* 200 */     p(s3);
/* 201 */     p(s4);
/* 202 */     p(s5);
/*     */   }
/*     */   
/*     */   public void pln() throws IOException {
/* 206 */     newLine();
/*     */   }
/*     */   
/*     */   public void pln(String s) throws IOException {
/* 210 */     p(s);
/* 211 */     pln();
/*     */   }
/*     */   
/*     */   public void pln(String s1, String s2) throws IOException {
/* 215 */     p(s1, s2);
/* 216 */     pln();
/*     */   }
/*     */   
/*     */   public void pln(String s1, String s2, String s3) throws IOException {
/* 220 */     p(s1, s2, s3);
/* 221 */     pln();
/*     */   }
/*     */   
/*     */   public void pln(String s1, String s2, String s3, String s4) throws IOException {
/* 225 */     p(s1, s2, s3, s4);
/* 226 */     pln();
/*     */   }
/*     */   
/*     */   public void pln(String s1, String s2, String s3, String s4, String s5) throws IOException {
/* 230 */     p(s1, s2, s3, s4, s5);
/* 231 */     pln();
/*     */   }
/*     */   
/*     */   public void plnI(String s) throws IOException {
/* 235 */     p(s);
/* 236 */     pln();
/* 237 */     pI();
/*     */   }
/*     */   
/*     */   public void pO(String s) throws IOException {
/* 241 */     pO();
/* 242 */     p(s);
/*     */   }
/*     */   
/*     */   public void pOln(String s) throws IOException {
/* 246 */     pO(s);
/* 247 */     pln();
/*     */   }
/*     */   
/*     */   public void pOlnI(String s) throws IOException {
/* 251 */     pO(s);
/* 252 */     pln();
/* 253 */     pI();
/*     */   }
/*     */   
/*     */   public void p(Object o) throws IOException {
/* 257 */     write(o.toString());
/*     */   }
/*     */   
/*     */   public void pln(Object o) throws IOException {
/* 261 */     p(o.toString());
/* 262 */     pln();
/*     */   }
/*     */   
/*     */   public void plnI(Object o) throws IOException {
/* 266 */     p(o.toString());
/* 267 */     pln();
/* 268 */     pI();
/*     */   }
/*     */   
/*     */   public void pO(Object o) throws IOException {
/* 272 */     pO();
/* 273 */     p(o.toString());
/*     */   }
/*     */   
/*     */   public void pOln(Object o) throws IOException {
/* 277 */     pO(o.toString());
/* 278 */     pln();
/*     */   }
/*     */   
/*     */   public void pOlnI(Object o) throws IOException {
/* 282 */     pO(o.toString());
/* 283 */     pln();
/* 284 */     pI();
/*     */   }
/*     */   
/*     */   public void pM(String s) throws IOException {
/* 288 */     int i = 0;
/* 289 */     while (i < s.length()) {
/* 290 */       int j = s.indexOf('\n', i);
/* 291 */       if (j == -1) {
/* 292 */         p(s.substring(i));
/*     */         break;
/*     */       } 
/* 295 */       pln(s.substring(i, j));
/* 296 */       i = j + 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void pMln(String s) throws IOException {
/* 302 */     pM(s);
/* 303 */     pln();
/*     */   }
/*     */   
/*     */   public void pMlnI(String s) throws IOException {
/* 307 */     pM(s);
/* 308 */     pln();
/* 309 */     pI();
/*     */   }
/*     */   
/*     */   public void pMO(String s) throws IOException {
/* 313 */     pO();
/* 314 */     pM(s);
/*     */   }
/*     */   
/*     */   public void pMOln(String s) throws IOException {
/* 318 */     pMO(s);
/* 319 */     pln();
/*     */   }
/*     */   
/*     */   public void pF(String pattern, Object[] arguments) throws IOException {
/* 323 */     pM(MessageFormat.format(pattern, arguments));
/*     */   }
/*     */   
/*     */   public void pFln(String pattern, Object[] arguments) throws IOException {
/* 327 */     pF(pattern, arguments);
/* 328 */     pln();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processo\\util\IndentingWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */