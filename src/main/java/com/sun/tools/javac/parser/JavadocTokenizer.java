/*     */ package com.sun.tools.javac.parser;
/*     */
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import java.nio.CharBuffer;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class JavadocTokenizer
/*     */   extends JavaTokenizer
/*     */ {
/*     */   protected JavadocTokenizer(ScannerFactory paramScannerFactory, CharBuffer paramCharBuffer) {
/*  52 */     super(paramScannerFactory, paramCharBuffer);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected JavadocTokenizer(ScannerFactory paramScannerFactory, char[] paramArrayOfchar, int paramInt) {
/*  59 */     super(paramScannerFactory, paramArrayOfchar, paramInt);
/*     */   }
/*     */
/*     */
/*     */   protected Tokens.Comment processComment(int paramInt1, int paramInt2, Tokens.Comment.CommentStyle paramCommentStyle) {
/*  64 */     char[] arrayOfChar = this.reader.getRawCharacters(paramInt1, paramInt2);
/*  65 */     return new JavadocComment(new DocReader(this.fac, arrayOfChar, arrayOfChar.length, paramInt1), paramCommentStyle);
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
/*     */   static class DocReader
/*     */     extends UnicodeReader
/*     */   {
/*     */     int col;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     int startPos;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 103 */     int[] pbuf = new int[128];
/*     */
/*     */
/*     */
/*     */
/* 108 */     int pp = 0;
/*     */
/*     */     DocReader(ScannerFactory param1ScannerFactory, char[] param1ArrayOfchar, int param1Int1, int param1Int2) {
/* 111 */       super(param1ScannerFactory, param1ArrayOfchar, param1Int1);
/* 112 */       this.startPos = param1Int2;
/*     */     }
/*     */
/*     */
/*     */     protected void convertUnicode() {
/* 117 */       if (this.ch == '\\' && this.unicodeConversionBp != this.bp) {
/* 118 */         this.bp++; this.ch = this.buf[this.bp]; this.col++;
/* 119 */         if (this.ch == 'u') {
/*     */           while (true) {
/* 121 */             this.bp++; this.ch = this.buf[this.bp]; this.col++;
/* 122 */             if (this.ch != 'u') {
/* 123 */               int i = this.bp + 3;
/* 124 */               if (i < this.buflen) {
/* 125 */                 int j = digit(this.bp, 16);
/* 126 */                 int k = j;
/* 127 */                 while (this.bp < i && j >= 0) {
/* 128 */                   this.bp++; this.ch = this.buf[this.bp]; this.col++;
/* 129 */                   j = digit(this.bp, 16);
/* 130 */                   k = (k << 4) + j;
/*     */                 }
/* 132 */                 if (j >= 0) {
/* 133 */                   this.ch = (char)k;
/* 134 */                   this.unicodeConversionBp = this.bp; return;
/*     */                 }
/*     */               }  break;
/*     */             }
/*     */           }
/*     */         } else {
/* 140 */           this.bp--;
/* 141 */           this.ch = '\\';
/* 142 */           this.col--;
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     protected void scanCommentChar() {
/* 149 */       scanChar();
/* 150 */       if (this.ch == '\\') {
/* 151 */         if (peekChar() == '\\' && !isUnicode()) {
/* 152 */           putChar(this.ch, false);
/* 153 */           this.bp++; this.col++;
/*     */         } else {
/* 155 */           convertUnicode();
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     protected void scanChar() {
/* 162 */       this.bp++;
/* 163 */       this.ch = this.buf[this.bp];
/* 164 */       switch (this.ch) {
/*     */         case '\r':
/* 166 */           this.col = 0;
/*     */           return;
/*     */         case '\n':
/* 169 */           if (this.bp == 0 || this.buf[this.bp - 1] != '\r') {
/* 170 */             this.col = 0;
/*     */           }
/*     */           return;
/*     */         case '\t':
/* 174 */           this.col = this.col / 8 * 8 + 8;
/*     */           return;
/*     */         case '\\':
/* 177 */           this.col++;
/* 178 */           convertUnicode();
/*     */           return;
/*     */       }
/* 181 */       this.col++;
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
/*     */     public void putChar(char param1Char, boolean param1Boolean) {
/* 194 */       if (this.pp == 0 || this.sp - this.pbuf[this.pp - 2] != this.startPos + this.bp - this.pbuf[this.pp - 1]) {
/*     */
/* 196 */         if (this.pp + 1 >= this.pbuf.length) {
/* 197 */           int[] arrayOfInt = new int[this.pbuf.length * 2];
/* 198 */           System.arraycopy(this.pbuf, 0, arrayOfInt, 0, this.pbuf.length);
/* 199 */           this.pbuf = arrayOfInt;
/*     */         }
/* 201 */         this.pbuf[this.pp] = this.sp;
/* 202 */         this.pbuf[this.pp + 1] = this.startPos + this.bp;
/* 203 */         this.pp += 2;
/*     */       }
/* 205 */       super.putChar(param1Char, param1Boolean);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   protected static class JavadocComment
/*     */     extends BasicComment<DocReader>
/*     */   {
/* 214 */     private String docComment = null;
/* 215 */     private int[] docPosns = null;
/*     */
/*     */     JavadocComment(DocReader param1DocReader, CommentStyle param1CommentStyle) {
/* 218 */       super(param1DocReader, param1CommentStyle);
/*     */     }
/*     */
/*     */
/*     */     public String getText() {
/* 223 */       if (!this.scanned && this.cs == CommentStyle.JAVADOC) {
/* 224 */         scanDocComment();
/*     */       }
/* 226 */       return this.docComment;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public int getSourcePos(int param1Int) {
/* 237 */       if (param1Int == -1)
/* 238 */         return -1;
/* 239 */       if (param1Int < 0 || param1Int > this.docComment.length())
/* 240 */         throw new StringIndexOutOfBoundsException(String.valueOf(param1Int));
/* 241 */       if (this.docPosns == null)
/* 242 */         return -1;
/* 243 */       int i = 0;
/* 244 */       int j = this.docPosns.length;
/* 245 */       while (i < j - 2) {
/*     */
/* 247 */         int k = (i + j) / 4 * 2;
/* 248 */         if (this.docPosns[k] < param1Int) {
/* 249 */           i = k; continue;
/* 250 */         }  if (this.docPosns[k] == param1Int) {
/* 251 */           return this.docPosns[k + 1];
/*     */         }
/* 253 */         j = k;
/*     */       }
/* 255 */       return this.docPosns[i + 1] + param1Int - this.docPosns[i];
/*     */     }
/*     */
/*     */
/*     */
/*     */     protected void scanDocComment() {
/*     */       try {
/* 262 */         boolean bool = true;
/*     */
/*     */
/* 265 */         this.comment_reader.scanCommentChar();
/*     */
/* 267 */         this.comment_reader.scanCommentChar();
/*     */
/*     */
/* 270 */         while (this.comment_reader.bp < this.comment_reader.buflen && this.comment_reader.ch == '*') {
/* 271 */           this.comment_reader.scanCommentChar();
/*     */         }
/*     */
/* 274 */         if (this.comment_reader.bp < this.comment_reader.buflen && this.comment_reader.ch == '/') {
/* 275 */           this.docComment = "";
/*     */
/*     */           return;
/*     */         }
/*     */
/* 280 */         if (this.comment_reader.bp < this.comment_reader.buflen) {
/* 281 */           if (this.comment_reader.ch == '\n') {
/* 282 */             this.comment_reader.scanCommentChar();
/* 283 */             bool = false;
/* 284 */           } else if (this.comment_reader.ch == '\r') {
/* 285 */             this.comment_reader.scanCommentChar();
/* 286 */             if (this.comment_reader.ch == '\n') {
/* 287 */               this.comment_reader.scanCommentChar();
/* 288 */               bool = false;
/*     */             }
/*     */           }
/*     */         }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 299 */         label88: while (this.comment_reader.bp < this.comment_reader.buflen) {
/* 300 */           int i = this.comment_reader.bp;
/* 301 */           char c = this.comment_reader.ch;
/*     */
/*     */
/*     */
/*     */
/* 306 */           while (this.comment_reader.bp < this.comment_reader.buflen) {
/* 307 */             switch (this.comment_reader.ch) {
/*     */               case ' ':
/* 309 */                 this.comment_reader.scanCommentChar();
/*     */
/*     */               case '\t':
/* 312 */                 this.comment_reader.col = (this.comment_reader.col - 1) / 8 * 8 + 8;
/* 313 */                 this.comment_reader.scanCommentChar();
/*     */
/*     */               case '\f':
/* 316 */                 this.comment_reader.col = 0;
/* 317 */                 this.comment_reader.scanCommentChar();
/*     */             }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */           }
/* 342 */           if (this.comment_reader.ch == '*') {
/*     */
/*     */             do {
/* 345 */               this.comment_reader.scanCommentChar();
/* 346 */             } while (this.comment_reader.ch == '*');
/*     */
/*     */
/* 349 */             if (this.comment_reader.ch == '/')
/*     */             {
/*     */               break;
/*     */             }
/*     */           }
/* 354 */           else if (!bool) {
/*     */
/*     */
/* 357 */             this.comment_reader.bp = i;
/* 358 */             this.comment_reader.ch = c;
/*     */           }
/*     */
/*     */
/*     */
/* 363 */           while (this.comment_reader.bp < this.comment_reader.buflen) {
/* 364 */             switch (this.comment_reader.ch) {
/*     */
/*     */
/*     */               case '*':
/* 368 */                 this.comment_reader.scanCommentChar();
/* 369 */                 if (this.comment_reader.ch == '/') {
/*     */                   break label88;
/*     */                 }
/*     */
/*     */
/*     */
/*     */
/* 376 */                 this.comment_reader.putChar('*', false);
/*     */                 continue;
/*     */               case '\t':
/*     */               case ' ':
/* 380 */                 this.comment_reader.putChar(this.comment_reader.ch, false);
/* 381 */                 this.comment_reader.scanCommentChar();
/*     */                 continue;
/*     */               case '\f':
/* 384 */                 this.comment_reader.scanCommentChar();
/*     */                 break;
/*     */               case '\r':
/* 387 */                 this.comment_reader.scanCommentChar();
/* 388 */                 if (this.comment_reader.ch != '\n') {
/*     */
/* 390 */                   this.comment_reader.putChar('\n', false);
/*     */                   break;
/*     */                 }
/*     */
/*     */
/*     */
/*     */
/*     */               case '\n':
/* 398 */                 this.comment_reader.putChar(this.comment_reader.ch, false);
/* 399 */                 this.comment_reader.scanCommentChar();
/*     */                 break;
/*     */             }
/*     */
/* 403 */             this.comment_reader.putChar(this.comment_reader.ch, false);
/* 404 */             this.comment_reader.scanCommentChar();
/*     */           }
/*     */
/* 407 */           bool = false;
/*     */         }
/*     */
/* 410 */         if (this.comment_reader.sp > 0) {
/* 411 */           int i = this.comment_reader.sp - 1;
/*     */
/* 413 */           while (i > -1) {
/* 414 */             switch (this.comment_reader.sbuf[i]) {
/*     */               case '*':
/* 416 */                 i--;
/*     */             }
/*     */
/*     */
/*     */
/*     */           }
/* 422 */           this.comment_reader.sp = i + 1;
/*     */
/*     */
/* 425 */           this.docComment = this.comment_reader.chars();
/* 426 */           this.docPosns = new int[this.comment_reader.pp];
/* 427 */           System.arraycopy(this.comment_reader.pbuf, 0, this.docPosns, 0, this.docPosns.length);
/*     */         } else {
/* 429 */           this.docComment = "";
/*     */         }
/*     */       } finally {
/* 432 */         this.scanned = true;
/* 433 */         this.comment_reader = null;
/* 434 */         if (this.docComment != null && this.docComment
/* 435 */           .matches("(?sm).*^\\s*@deprecated( |$).*")) {
/* 436 */           this.deprecatedFlag = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Position.LineMap getLineMap() {
/* 444 */     char[] arrayOfChar = this.reader.getRawCharacters();
/* 445 */     return Position.makeLineMap(arrayOfChar, arrayOfChar.length, true);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\JavadocTokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
