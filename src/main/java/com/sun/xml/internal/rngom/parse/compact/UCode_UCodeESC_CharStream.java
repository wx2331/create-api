/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.util.Utf16;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UCode_UCodeESC_CharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   
/*     */   static final int hexval(char c) {
/*  65 */     switch (c) {
/*     */       case '0':
/*  67 */         return 0;
/*     */       case '1':
/*  69 */         return 1;
/*     */       case '2':
/*  71 */         return 2;
/*     */       case '3':
/*  73 */         return 3;
/*     */       case '4':
/*  75 */         return 4;
/*     */       case '5':
/*  77 */         return 5;
/*     */       case '6':
/*  79 */         return 6;
/*     */       case '7':
/*  81 */         return 7;
/*     */       case '8':
/*  83 */         return 8;
/*     */       case '9':
/*  85 */         return 9;
/*     */       
/*     */       case 'A':
/*     */       case 'a':
/*  89 */         return 10;
/*     */       case 'B':
/*     */       case 'b':
/*  92 */         return 11;
/*     */       case 'C':
/*     */       case 'c':
/*  95 */         return 12;
/*     */       case 'D':
/*     */       case 'd':
/*  98 */         return 13;
/*     */       case 'E':
/*     */       case 'e':
/* 101 */         return 14;
/*     */       case 'F':
/*     */       case 'f':
/* 104 */         return 15;
/*     */     } 
/* 106 */     return -1;
/*     */   }
/* 108 */   public int bufpos = -1;
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*     */   private int[] bufline;
/*     */   private int[] bufcolumn;
/* 114 */   private int column = 0;
/* 115 */   private int line = 1;
/*     */   private Reader inputStream;
/*     */   private boolean closed = false;
/*     */   private boolean prevCharIsLF = false;
/*     */   private char[] nextCharBuf;
/*     */   private char[] buffer;
/* 121 */   private int maxNextCharInd = 0;
/* 122 */   private int nextCharInd = -1;
/* 123 */   private int inBuf = 0; private static final char NEWLINE_MARKER = '\000'; private static final char BOM = '﻿';
/*     */   
/*     */   private final void ExpandBuff(boolean wrapAround) {
/* 126 */     char[] newbuffer = new char[this.bufsize + 2048];
/* 127 */     int[] newbufline = new int[this.bufsize + 2048];
/* 128 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */     
/* 130 */     if (wrapAround) {
/* 131 */       System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 132 */       System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
/*     */       
/* 134 */       this.buffer = newbuffer;
/*     */       
/* 136 */       System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 137 */       System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/* 138 */       this.bufline = newbufline;
/*     */       
/* 140 */       System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 141 */       System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/* 142 */       this.bufcolumn = newbufcolumn;
/*     */       
/* 144 */       this.bufpos += this.bufsize - this.tokenBegin;
/*     */     } else {
/* 146 */       System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 147 */       this.buffer = newbuffer;
/*     */       
/* 149 */       System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 150 */       this.bufline = newbufline;
/*     */       
/* 152 */       System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 153 */       this.bufcolumn = newbufcolumn;
/*     */       
/* 155 */       this.bufpos -= this.tokenBegin;
/*     */     } 
/*     */     
/* 158 */     this.available = this.bufsize += 2048;
/* 159 */     this.tokenBegin = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private final void FillBuff() throws EOFException {
/* 164 */     if (this.maxNextCharInd == 4096) {
/* 165 */       this.maxNextCharInd = this.nextCharInd = 0;
/*     */     }
/*     */     
/* 168 */     if (this.closed)
/* 169 */       throw new EOFException(); 
/*     */     try {
/*     */       int i;
/* 172 */       if ((i = this.inputStream.read(this.nextCharBuf, this.maxNextCharInd, 4096 - this.maxNextCharInd)) == -1) {
/* 173 */         this.closed = true;
/* 174 */         this.inputStream.close();
/* 175 */         throw new EOFException();
/*     */       } 
/* 177 */       this.maxNextCharInd += i;
/*     */     }
/* 179 */     catch (IOException e) {
/* 180 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private final char ReadChar() throws EOFException {
/* 185 */     if (++this.nextCharInd >= this.maxNextCharInd) {
/* 186 */       FillBuff();
/*     */     }
/*     */     
/* 189 */     return this.nextCharBuf[this.nextCharInd];
/*     */   }
/*     */   
/*     */   private final char PeekChar() throws EOFException {
/* 193 */     char c = ReadChar();
/* 194 */     this.nextCharInd--;
/* 195 */     return c;
/*     */   }
/*     */   
/*     */   public final char BeginToken() throws EOFException {
/* 199 */     if (this.inBuf > 0) {
/* 200 */       this.inBuf--;
/* 201 */       return this.buffer[this.tokenBegin = (this.bufpos == this.bufsize - 1) ? (this.bufpos = 0) : ++this.bufpos];
/*     */     } 
/*     */ 
/*     */     
/* 205 */     this.tokenBegin = 0;
/* 206 */     this.bufpos = -1;
/*     */     
/* 208 */     return readChar();
/*     */   }
/*     */   
/*     */   private final void AdjustBuffSize() {
/* 212 */     if (this.available == this.bufsize) {
/* 213 */       if (this.tokenBegin > 2048) {
/* 214 */         this.bufpos = 0;
/* 215 */         this.available = this.tokenBegin;
/*     */       } else {
/* 217 */         ExpandBuff(false);
/*     */       } 
/* 219 */     } else if (this.available > this.tokenBegin) {
/* 220 */       this.available = this.bufsize;
/* 221 */     } else if (this.tokenBegin - this.available < 2048) {
/* 222 */       ExpandBuff(true);
/*     */     } else {
/* 224 */       this.available = this.tokenBegin;
/*     */     } 
/*     */   }
/*     */   
/*     */   private final void UpdateLineColumn(char c) {
/* 229 */     this.column++;
/*     */     
/* 231 */     if (this.prevCharIsLF) {
/* 232 */       this.prevCharIsLF = false;
/* 233 */       this.line += this.column = 1;
/*     */     } 
/*     */     
/* 236 */     switch (c) {
/*     */       case '\000':
/* 238 */         this.prevCharIsLF = true;
/*     */         break;
/*     */       case '\t':
/* 241 */         this.column--;
/* 242 */         this.column += 8 - (this.column & 0x7);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 248 */     this.bufline[this.bufpos] = this.line;
/* 249 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */   
/*     */   public final char readChar() throws EOFException {
/*     */     char c;
/* 254 */     if (this.inBuf > 0) {
/* 255 */       this.inBuf--;
/* 256 */       return this.buffer[(this.bufpos == this.bufsize - 1) ? (this.bufpos = 0) : ++this.bufpos];
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 261 */       c = ReadChar();
/* 262 */       switch (c) {
/*     */         case '\r':
/* 264 */           c = Character.MIN_VALUE;
/*     */           try {
/* 266 */             if (PeekChar() == '\n') {
/* 267 */               ReadChar();
/*     */             }
/* 269 */           } catch (EOFException eOFException) {}
/*     */           break;
/*     */         
/*     */         case '\n':
/* 273 */           c = Character.MIN_VALUE;
/*     */           break;
/*     */         case '\t':
/*     */           break;
/*     */         default:
/* 278 */           if (c >= ' ') {
/* 279 */             if (Utf16.isSurrogate(c)) {
/* 280 */               if (Utf16.isSurrogate2(c)) {
/* 281 */                 throw new EscapeSyntaxException("illegal_surrogate_pair", this.line, this.column + 1);
/*     */               }
/* 283 */               if (++this.bufpos == this.available) {
/* 284 */                 AdjustBuffSize();
/*     */               }
/* 286 */               this.buffer[this.bufpos] = c;
/*     */               
/*     */               try {
/* 289 */                 c = ReadChar();
/* 290 */               } catch (EOFException e) {
/* 291 */                 throw new EscapeSyntaxException("illegal_surrogate_pair", this.line, this.column + 1);
/*     */               } 
/* 293 */               if (!Utf16.isSurrogate2(c)) {
/* 294 */                 throw new EscapeSyntaxException("illegal_surrogate_pair", this.line, this.column + 2);
/*     */               }
/*     */             } 
/*     */             break;
/*     */           } 
/*     */         
/*     */         case '￾':
/*     */         case '￿':
/* 302 */           throw new EscapeSyntaxException("illegal_char_code", this.line, this.column + 1);
/*     */       } 
/* 304 */     } catch (EOFException e) {
/* 305 */       if (this.bufpos == -1) {
/* 306 */         if (++this.bufpos == this.available) {
/* 307 */           AdjustBuffSize();
/*     */         }
/* 309 */         this.bufline[this.bufpos] = this.line;
/* 310 */         this.bufcolumn[this.bufpos] = this.column;
/*     */       } 
/* 312 */       throw e;
/*     */     } 
/* 314 */     if (++this.bufpos == this.available) {
/* 315 */       AdjustBuffSize();
/*     */     }
/* 317 */     this.buffer[this.bufpos] = c;
/* 318 */     UpdateLineColumn(c);
/*     */     try {
/* 320 */       if (c != '\\' || PeekChar() != 'x') {
/* 321 */         return c;
/*     */       }
/* 323 */     } catch (EOFException e) {
/* 324 */       return c;
/*     */     } 
/*     */     
/* 327 */     int xCnt = 1;
/*     */     while (true) {
/* 329 */       ReadChar();
/* 330 */       if (++this.bufpos == this.available) {
/* 331 */         AdjustBuffSize();
/*     */       }
/* 333 */       this.buffer[this.bufpos] = 'x';
/* 334 */       UpdateLineColumn('x');
/*     */       try {
/* 336 */         c = PeekChar();
/* 337 */       } catch (EOFException e) {
/* 338 */         backup(xCnt);
/* 339 */         return '\\';
/*     */       } 
/* 341 */       if (c == '{') {
/* 342 */         ReadChar();
/* 343 */         this.column++;
/*     */         
/* 345 */         this.bufpos -= xCnt;
/* 346 */         if (this.bufpos < 0) {
/* 347 */           this.bufpos += this.bufsize;
/*     */         }
/*     */         break;
/*     */       } 
/* 351 */       if (c != 'x') {
/* 352 */         backup(xCnt);
/* 353 */         return '\\';
/*     */       } 
/* 355 */       xCnt++;
/*     */     } 
/*     */     try {
/* 358 */       int scalarValue = hexval(ReadChar());
/* 359 */       this.column++;
/* 360 */       if (scalarValue < 0) {
/* 361 */         throw new EscapeSyntaxException("illegal_hex_digit", this.line, this.column);
/*     */       }
/* 363 */       while ((c = ReadChar()) != '}') {
/* 364 */         this.column++;
/* 365 */         int n = hexval(c);
/* 366 */         if (n < 0) {
/* 367 */           throw new EscapeSyntaxException("illegal_hex_digit", this.line, this.column);
/*     */         }
/* 369 */         scalarValue <<= 4;
/* 370 */         scalarValue |= n;
/* 371 */         if (scalarValue >= 1114112) {
/* 372 */           throw new EscapeSyntaxException("char_code_too_big", this.line, this.column);
/*     */         }
/*     */       } 
/* 375 */       this.column++;
/* 376 */       if (scalarValue <= 65535)
/* 377 */       { c = (char)scalarValue;
/* 378 */         switch (c)
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case '\t':
/*     */           case '\n':
/*     */           case '\r':
/* 392 */             this.buffer[this.bufpos] = c;
/* 393 */             return c;default: if (c >= ' ' && !Utf16.isSurrogate(c)) { this.buffer[this.bufpos] = c; return c; }  break;
/*     */           case '￾': case '￿':
/* 395 */             break; }  throw new EscapeSyntaxException("illegal_char_code_ref", this.line, this.column); }  c = Utf16.surrogate1(scalarValue);
/* 396 */       this.buffer[this.bufpos] = c;
/* 397 */       int bufpos1 = this.bufpos;
/* 398 */       if (++this.bufpos == this.bufsize) {
/* 399 */         this.bufpos = 0;
/*     */       }
/* 401 */       this.buffer[this.bufpos] = Utf16.surrogate2(scalarValue);
/* 402 */       this.bufline[this.bufpos] = this.bufline[bufpos1];
/* 403 */       this.bufcolumn[this.bufpos] = this.bufcolumn[bufpos1];
/* 404 */       backup(1);
/* 405 */       return c;
/* 406 */     } catch (EOFException e) {
/* 407 */       throw new EscapeSyntaxException("incomplete_escape", this.line, this.column);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getColumn() {
/* 415 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getLine() {
/* 422 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndColumn() {
/* 426 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndLine() {
/* 430 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getBeginColumn() {
/* 434 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public final int getBeginLine() {
/* 438 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   
/*     */   public final void backup(int amount) {
/* 443 */     this.inBuf += amount;
/* 444 */     if ((this.bufpos -= amount) < 0) {
/* 445 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public UCode_UCodeESC_CharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 451 */     this.inputStream = dstream;
/* 452 */     this.line = startline;
/* 453 */     this.column = startcolumn - 1;
/*     */     
/* 455 */     this.available = this.bufsize = buffersize;
/* 456 */     this.buffer = new char[buffersize];
/* 457 */     this.bufline = new int[buffersize];
/* 458 */     this.bufcolumn = new int[buffersize];
/* 459 */     this.nextCharBuf = new char[4096];
/* 460 */     skipBOM();
/*     */   }
/*     */ 
/*     */   
/*     */   public UCode_UCodeESC_CharStream(Reader dstream, int startline, int startcolumn) {
/* 465 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 470 */     this.inputStream = dstream;
/* 471 */     this.closed = false;
/* 472 */     this.line = startline;
/* 473 */     this.column = startcolumn - 1;
/*     */     
/* 475 */     if (this.buffer == null || buffersize != this.buffer.length) {
/* 476 */       this.available = this.bufsize = buffersize;
/* 477 */       this.buffer = new char[buffersize];
/* 478 */       this.bufline = new int[buffersize];
/* 479 */       this.bufcolumn = new int[buffersize];
/* 480 */       this.nextCharBuf = new char[4096];
/*     */     } 
/* 482 */     this.prevCharIsLF = false;
/* 483 */     this.tokenBegin = this.inBuf = this.maxNextCharInd = 0;
/* 484 */     this.nextCharInd = this.bufpos = -1;
/* 485 */     skipBOM();
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn) {
/* 490 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public UCode_UCodeESC_CharStream(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 495 */     this(new InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public UCode_UCodeESC_CharStream(InputStream dstream, int startline, int startcolumn) {
/* 500 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 505 */     ReInit(new InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn) {
/* 510 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   private void skipBOM() {
/*     */     try {
/* 516 */       if (PeekChar() == '﻿') {
/* 517 */         ReadChar();
/*     */       }
/* 519 */     } catch (EOFException eOFException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public final String GetImage() {
/* 524 */     if (this.bufpos >= this.tokenBegin) {
/* 525 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 527 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final char[] GetSuffix(int len) {
/* 533 */     char[] ret = new char[len];
/*     */     
/* 535 */     if (this.bufpos + 1 >= len) {
/* 536 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     } else {
/* 538 */       System.arraycopy(this.buffer, this.bufsize - len - this.bufpos - 1, ret, 0, len - this.bufpos - 1);
/*     */       
/* 540 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     } 
/*     */     
/* 543 */     return ret;
/*     */   }
/*     */   
/*     */   public void Done() {
/* 547 */     this.nextCharBuf = null;
/* 548 */     this.buffer = null;
/* 549 */     this.bufline = null;
/* 550 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustBeginLineColumn(int newLine, int newCol) {
/* 557 */     int len, start = this.tokenBegin;
/*     */ 
/*     */     
/* 560 */     if (this.bufpos >= this.tokenBegin) {
/* 561 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     } else {
/* 563 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     } 
/*     */     
/* 566 */     int i = 0, j = 0, k = 0;
/* 567 */     int columnDiff = 0;
/*     */     
/* 569 */     while (i < len && this.bufline[j = start % this.bufsize] == this.bufline[k = ++start % this.bufsize]) {
/*     */       
/* 571 */       this.bufline[j] = newLine;
/* 572 */       int nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 573 */       this.bufcolumn[j] = newCol + columnDiff;
/* 574 */       columnDiff = nextColDiff;
/* 575 */       i++;
/*     */     } 
/*     */     
/* 578 */     if (i < len) {
/* 579 */       this.bufline[j] = newLine++;
/* 580 */       this.bufcolumn[j] = newCol + columnDiff;
/*     */       
/* 582 */       while (i++ < len) {
/* 583 */         if (this.bufline[j = start % this.bufsize] != this.bufline[++start % this.bufsize]) {
/* 584 */           this.bufline[j] = newLine++; continue;
/*     */         } 
/* 586 */         this.bufline[j] = newLine;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 591 */     this.line = this.bufline[j];
/* 592 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\UCode_UCodeESC_CharStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */