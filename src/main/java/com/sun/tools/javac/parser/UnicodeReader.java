/*     */ package com.sun.tools.javac.parser;
/*     */ 
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.util.ArrayUtils;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeReader
/*     */ {
/*     */   protected char[] buf;
/*     */   protected int bp;
/*     */   protected final int buflen;
/*     */   protected char ch;
/*  64 */   protected int unicodeConversionBp = -1;
/*     */ 
/*     */   
/*     */   protected Log log;
/*     */   
/*     */   protected Names names;
/*     */   
/*  71 */   protected char[] sbuf = new char[128];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int sp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UnicodeReader(ScannerFactory paramScannerFactory, CharBuffer paramCharBuffer) {
/*  85 */     this(paramScannerFactory, JavacFileManager.toArray(paramCharBuffer), paramCharBuffer.limit());
/*     */   }
/*     */   
/*     */   protected UnicodeReader(ScannerFactory paramScannerFactory, char[] paramArrayOfchar, int paramInt) {
/*  89 */     this.log = paramScannerFactory.log;
/*  90 */     this.names = paramScannerFactory.names;
/*  91 */     if (paramInt == paramArrayOfchar.length) {
/*  92 */       if (paramArrayOfchar.length > 0 && Character.isWhitespace(paramArrayOfchar[paramArrayOfchar.length - 1])) {
/*  93 */         paramInt--;
/*     */       } else {
/*  95 */         paramArrayOfchar = Arrays.copyOf(paramArrayOfchar, paramInt + 1);
/*     */       } 
/*     */     }
/*  98 */     this.buf = paramArrayOfchar;
/*  99 */     this.buflen = paramInt;
/* 100 */     this.buf[this.buflen] = '\032';
/* 101 */     this.bp = -1;
/* 102 */     scanChar();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void scanChar() {
/* 108 */     if (this.bp < this.buflen) {
/* 109 */       this.ch = this.buf[++this.bp];
/* 110 */       if (this.ch == '\\') {
/* 111 */         convertUnicode();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void scanCommentChar() {
/* 119 */     scanChar();
/* 120 */     if (this.ch == '\\') {
/* 121 */       if (peekChar() == '\\' && !isUnicode()) {
/* 122 */         skipChar();
/*     */       } else {
/* 124 */         convertUnicode();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void putChar(char paramChar, boolean paramBoolean) {
/* 132 */     this.sbuf = ArrayUtils.ensureCapacity(this.sbuf, this.sp);
/* 133 */     this.sbuf[this.sp++] = paramChar;
/* 134 */     if (paramBoolean)
/* 135 */       scanChar(); 
/*     */   }
/*     */   
/*     */   protected void putChar(char paramChar) {
/* 139 */     putChar(paramChar, false);
/*     */   }
/*     */   
/*     */   protected void putChar(boolean paramBoolean) {
/* 143 */     putChar(this.ch, paramBoolean);
/*     */   }
/*     */   
/*     */   Name name() {
/* 147 */     return this.names.fromChars(this.sbuf, 0, this.sp);
/*     */   }
/*     */   
/*     */   String chars() {
/* 151 */     return new String(this.sbuf, 0, this.sp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void convertUnicode() {
/* 158 */     if (this.ch == '\\' && this.unicodeConversionBp != this.bp) {
/* 159 */       this.bp++; this.ch = this.buf[this.bp];
/* 160 */       if (this.ch == 'u')
/*     */       { while (true) {
/* 162 */           this.bp++; this.ch = this.buf[this.bp];
/* 163 */           if (this.ch != 'u')
/* 164 */           { int i = this.bp + 3;
/* 165 */             if (i < this.buflen) {
/* 166 */               int j = digit(this.bp, 16);
/* 167 */               int k = j;
/* 168 */               while (this.bp < i && j >= 0) {
/* 169 */                 this.bp++; this.ch = this.buf[this.bp];
/* 170 */                 j = digit(this.bp, 16);
/* 171 */                 k = (k << 4) + j;
/*     */               } 
/* 173 */               if (j >= 0) {
/* 174 */                 this.ch = (char)k;
/* 175 */                 this.unicodeConversionBp = this.bp;
/*     */                 return;
/*     */               } 
/*     */             } 
/* 179 */             this.log.error(this.bp, "illegal.unicode.esc", new Object[0]); break; } 
/*     */         }  }
/* 181 */       else { this.bp--;
/* 182 */         this.ch = '\\'; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   static final boolean surrogatesSupported = surrogatesSupported();
/*     */   private static boolean surrogatesSupported() {
/*     */     try {
/* 192 */       Character.isHighSurrogate('a');
/* 193 */       return true;
/* 194 */     } catch (NoSuchMethodError noSuchMethodError) {
/* 195 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char scanSurrogates() {
/* 205 */     if (surrogatesSupported && Character.isHighSurrogate(this.ch)) {
/* 206 */       char c = this.ch;
/*     */       
/* 208 */       scanChar();
/*     */       
/* 210 */       if (Character.isLowSurrogate(this.ch)) {
/* 211 */         return c;
/*     */       }
/*     */       
/* 214 */       this.ch = c;
/*     */     } 
/*     */     
/* 217 */     return Character.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int digit(int paramInt1, int paramInt2) {
/* 224 */     char c = this.ch;
/* 225 */     int i = Character.digit(c, paramInt2);
/* 226 */     if (i >= 0 && c > '') {
/* 227 */       this.log.error(paramInt1 + 1, "illegal.nonascii.digit", new Object[0]);
/* 228 */       this.ch = "0123456789abcdef".charAt(i);
/*     */     } 
/* 230 */     return i;
/*     */   }
/*     */   
/*     */   protected boolean isUnicode() {
/* 234 */     return (this.unicodeConversionBp == this.bp);
/*     */   }
/*     */   
/*     */   protected void skipChar() {
/* 238 */     this.bp++;
/*     */   }
/*     */   
/*     */   protected char peekChar() {
/* 242 */     return this.buf[this.bp + 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] getRawCharacters() {
/* 250 */     char[] arrayOfChar = new char[this.buflen];
/* 251 */     System.arraycopy(this.buf, 0, arrayOfChar, 0, this.buflen);
/* 252 */     return arrayOfChar;
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
/*     */   public char[] getRawCharacters(int paramInt1, int paramInt2) {
/* 270 */     int i = paramInt2 - paramInt1;
/* 271 */     char[] arrayOfChar = new char[i];
/* 272 */     System.arraycopy(this.buf, paramInt1, arrayOfChar, 0, i);
/* 273 */     return arrayOfChar;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\UnicodeReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */