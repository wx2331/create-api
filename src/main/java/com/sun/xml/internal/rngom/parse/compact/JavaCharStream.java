/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaCharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   
/*     */   static final int hexval(char c) throws IOException {
/*  62 */     switch (c) {
/*     */       
/*     */       case '0':
/*  65 */         return 0;
/*     */       case '1':
/*  67 */         return 1;
/*     */       case '2':
/*  69 */         return 2;
/*     */       case '3':
/*  71 */         return 3;
/*     */       case '4':
/*  73 */         return 4;
/*     */       case '5':
/*  75 */         return 5;
/*     */       case '6':
/*  77 */         return 6;
/*     */       case '7':
/*  79 */         return 7;
/*     */       case '8':
/*  81 */         return 8;
/*     */       case '9':
/*  83 */         return 9;
/*     */       
/*     */       case 'A':
/*     */       case 'a':
/*  87 */         return 10;
/*     */       case 'B':
/*     */       case 'b':
/*  90 */         return 11;
/*     */       case 'C':
/*     */       case 'c':
/*  93 */         return 12;
/*     */       case 'D':
/*     */       case 'd':
/*  96 */         return 13;
/*     */       case 'E':
/*     */       case 'e':
/*  99 */         return 14;
/*     */       case 'F':
/*     */       case 'f':
/* 102 */         return 15;
/*     */     } 
/*     */     
/* 105 */     throw new IOException();
/*     */   }
/*     */ 
/*     */   
/* 109 */   public int bufpos = -1;
/*     */   
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*     */   protected int[] bufline;
/*     */   protected int[] bufcolumn;
/* 116 */   protected int column = 0;
/* 117 */   protected int line = 1;
/*     */   
/*     */   protected boolean prevCharIsCR = false;
/*     */   
/*     */   protected boolean prevCharIsLF = false;
/*     */   
/*     */   protected Reader inputStream;
/*     */   protected char[] nextCharBuf;
/*     */   protected char[] buffer;
/* 126 */   protected int maxNextCharInd = 0;
/* 127 */   protected int nextCharInd = -1;
/* 128 */   protected int inBuf = 0;
/* 129 */   protected int tabSize = 8;
/*     */   
/* 131 */   protected void setTabSize(int i) { this.tabSize = i; } protected int getTabSize(int i) {
/* 132 */     return this.tabSize;
/*     */   }
/*     */   
/*     */   protected void ExpandBuff(boolean wrapAround) {
/* 136 */     char[] newbuffer = new char[this.bufsize + 2048];
/* 137 */     int[] newbufline = new int[this.bufsize + 2048];
/* 138 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */ 
/*     */     
/*     */     try {
/* 142 */       if (wrapAround)
/*     */       {
/* 144 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 145 */         System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
/* 146 */         this.buffer = newbuffer;
/*     */         
/* 148 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 149 */         System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/* 150 */         this.bufline = newbufline;
/*     */         
/* 152 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 153 */         System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/* 154 */         this.bufcolumn = newbufcolumn;
/*     */         
/* 156 */         this.bufpos += this.bufsize - this.tokenBegin;
/*     */       }
/*     */       else
/*     */       {
/* 160 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 161 */         this.buffer = newbuffer;
/*     */         
/* 163 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 164 */         this.bufline = newbufline;
/*     */         
/* 166 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 167 */         this.bufcolumn = newbufcolumn;
/*     */         
/* 169 */         this.bufpos -= this.tokenBegin;
/*     */       }
/*     */     
/* 172 */     } catch (Throwable t) {
/*     */       
/* 174 */       throw new Error(t.getMessage());
/*     */     } 
/*     */     
/* 177 */     this.available = this.bufsize += 2048;
/* 178 */     this.tokenBegin = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void FillBuff() throws IOException {
/* 184 */     if (this.maxNextCharInd == 4096)
/* 185 */       this.maxNextCharInd = this.nextCharInd = 0; 
/*     */     try {
/*     */       int i;
/* 188 */       if ((i = this.inputStream.read(this.nextCharBuf, this.maxNextCharInd, 4096 - this.maxNextCharInd)) == -1) {
/*     */ 
/*     */         
/* 191 */         this.inputStream.close();
/* 192 */         throw new IOException();
/*     */       } 
/*     */       
/* 195 */       this.maxNextCharInd += i;
/*     */       
/*     */       return;
/* 198 */     } catch (IOException e) {
/* 199 */       if (this.bufpos != 0) {
/*     */         
/* 201 */         this.bufpos--;
/* 202 */         backup(0);
/*     */       }
/*     */       else {
/*     */         
/* 206 */         this.bufline[this.bufpos] = this.line;
/* 207 */         this.bufcolumn[this.bufpos] = this.column;
/*     */       } 
/* 209 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected char ReadByte() throws IOException {
/* 215 */     if (++this.nextCharInd >= this.maxNextCharInd) {
/* 216 */       FillBuff();
/*     */     }
/* 218 */     return this.nextCharBuf[this.nextCharInd];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char BeginToken() throws IOException {
/* 224 */     if (this.inBuf > 0) {
/*     */       
/* 226 */       this.inBuf--;
/*     */       
/* 228 */       if (++this.bufpos == this.bufsize) {
/* 229 */         this.bufpos = 0;
/*     */       }
/* 231 */       this.tokenBegin = this.bufpos;
/* 232 */       return this.buffer[this.bufpos];
/*     */     } 
/*     */     
/* 235 */     this.tokenBegin = 0;
/* 236 */     this.bufpos = -1;
/*     */     
/* 238 */     return readChar();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void AdjustBuffSize() {
/* 243 */     if (this.available == this.bufsize) {
/*     */       
/* 245 */       if (this.tokenBegin > 2048) {
/*     */         
/* 247 */         this.bufpos = 0;
/* 248 */         this.available = this.tokenBegin;
/*     */       } else {
/*     */         
/* 251 */         ExpandBuff(false);
/*     */       } 
/* 253 */     } else if (this.available > this.tokenBegin) {
/* 254 */       this.available = this.bufsize;
/* 255 */     } else if (this.tokenBegin - this.available < 2048) {
/* 256 */       ExpandBuff(true);
/*     */     } else {
/* 258 */       this.available = this.tokenBegin;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void UpdateLineColumn(char c) {
/* 263 */     this.column++;
/*     */     
/* 265 */     if (this.prevCharIsLF) {
/*     */       
/* 267 */       this.prevCharIsLF = false;
/* 268 */       this.line += this.column = 1;
/*     */     }
/* 270 */     else if (this.prevCharIsCR) {
/*     */       
/* 272 */       this.prevCharIsCR = false;
/* 273 */       if (c == '\n') {
/*     */         
/* 275 */         this.prevCharIsLF = true;
/*     */       } else {
/*     */         
/* 278 */         this.line += this.column = 1;
/*     */       } 
/*     */     } 
/* 281 */     switch (c) {
/*     */       
/*     */       case '\r':
/* 284 */         this.prevCharIsCR = true;
/*     */         break;
/*     */       case '\n':
/* 287 */         this.prevCharIsLF = true;
/*     */         break;
/*     */       case '\t':
/* 290 */         this.column--;
/* 291 */         this.column += this.tabSize - this.column % this.tabSize;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 297 */     this.bufline[this.bufpos] = this.line;
/* 298 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char readChar() throws IOException {
/* 304 */     if (this.inBuf > 0) {
/*     */       
/* 306 */       this.inBuf--;
/*     */       
/* 308 */       if (++this.bufpos == this.bufsize) {
/* 309 */         this.bufpos = 0;
/*     */       }
/* 311 */       return this.buffer[this.bufpos];
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 316 */     if (++this.bufpos == this.available) {
/* 317 */       AdjustBuffSize();
/*     */     }
/* 319 */     char c = ReadByte(); if ((c = ReadByte()) == '\\') {
/*     */       
/* 321 */       UpdateLineColumn(c);
/*     */       
/* 323 */       int backSlashCnt = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 327 */         if (++this.bufpos == this.available) {
/* 328 */           AdjustBuffSize();
/*     */         }
/*     */         
/*     */         try {
/* 332 */           this.buffer[this.bufpos] = c = ReadByte(); if ((c = ReadByte()) != '\\') {
/*     */             
/* 334 */             UpdateLineColumn(c);
/*     */             
/* 336 */             if (c == 'u' && (backSlashCnt & 0x1) == 1) {
/*     */               
/* 338 */               if (--this.bufpos < 0) {
/* 339 */                 this.bufpos = this.bufsize - 1;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } else {
/* 344 */               backup(backSlashCnt);
/* 345 */               return '\\';
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */             
/* 357 */             UpdateLineColumn(c);
/* 358 */             backSlashCnt++;
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } catch (IOException e) {
/*     */           if (backSlashCnt > 1) {
/*     */             backup(backSlashCnt - 1);
/*     */           }
/*     */ 
/*     */           
/*     */           return '\\';
/*     */         } 
/*     */         
/*     */         try {
/*     */           break;
/* 374 */         } catch (IOException e) {
/*     */           
/* 376 */           throw new Error("Invalid escape character at line " + this.line + " column " + this.column + ".");
/*     */         } 
/*     */       }  while ((c = ReadByte()) == 'u')
/*     */         this.column++;  this.buffer[this.bufpos] = c = (char)(hexval(c) << 12 | hexval(ReadByte()) << 8 | hexval(ReadByte()) << 4 | hexval(ReadByte())); this.column += 4;
/* 380 */       if (backSlashCnt == 1) {
/* 381 */         return c;
/*     */       }
/*     */       
/* 384 */       backup(backSlashCnt - 1);
/* 385 */       return '\\';
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 390 */     UpdateLineColumn(c);
/* 391 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getColumn() {
/* 401 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getLine() {
/* 410 */     return this.bufline[this.bufpos];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndColumn() {
/* 415 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndLine() {
/* 420 */     return this.bufline[this.bufpos];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBeginColumn() {
/* 425 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBeginLine() {
/* 430 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void backup(int amount) {
/* 436 */     this.inBuf += amount;
/* 437 */     if ((this.bufpos -= amount) < 0) {
/* 438 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 445 */     this.inputStream = dstream;
/* 446 */     this.line = startline;
/* 447 */     this.column = startcolumn - 1;
/*     */     
/* 449 */     this.available = this.bufsize = buffersize;
/* 450 */     this.buffer = new char[buffersize];
/* 451 */     this.bufline = new int[buffersize];
/* 452 */     this.bufcolumn = new int[buffersize];
/* 453 */     this.nextCharBuf = new char[4096];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(Reader dstream, int startline, int startcolumn) {
/* 460 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(Reader dstream) {
/* 466 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 472 */     this.inputStream = dstream;
/* 473 */     this.line = startline;
/* 474 */     this.column = startcolumn - 1;
/*     */     
/* 476 */     if (this.buffer == null || buffersize != this.buffer.length) {
/*     */       
/* 478 */       this.available = this.bufsize = buffersize;
/* 479 */       this.buffer = new char[buffersize];
/* 480 */       this.bufline = new int[buffersize];
/* 481 */       this.bufcolumn = new int[buffersize];
/* 482 */       this.nextCharBuf = new char[4096];
/*     */     } 
/* 484 */     this.prevCharIsLF = this.prevCharIsCR = false;
/* 485 */     this.tokenBegin = this.inBuf = this.maxNextCharInd = 0;
/* 486 */     this.nextCharInd = this.bufpos = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn) {
/* 493 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream) {
/* 499 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize) throws UnsupportedEncodingException {
/* 505 */     this((encoding == null) ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 512 */     this(new InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(InputStream dstream, String encoding, int startline, int startcolumn) throws UnsupportedEncodingException {
/* 519 */     this(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(InputStream dstream, int startline, int startcolumn) {
/* 526 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(InputStream dstream, String encoding) throws UnsupportedEncodingException {
/* 532 */     this(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCharStream(InputStream dstream) {
/* 538 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize) throws UnsupportedEncodingException {
/* 545 */     ReInit((encoding == null) ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 552 */     ReInit(new InputStreamReader(dstream), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn) throws UnsupportedEncodingException {
/* 558 */     ReInit(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn) {
/* 564 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding) throws UnsupportedEncodingException {
/* 569 */     ReInit(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream) {
/* 575 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String GetImage() {
/* 581 */     if (this.bufpos >= this.tokenBegin) {
/* 582 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 584 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] GetSuffix(int len) {
/* 591 */     char[] ret = new char[len];
/*     */     
/* 593 */     if (this.bufpos + 1 >= len) {
/* 594 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     } else {
/*     */       
/* 597 */       System.arraycopy(this.buffer, this.bufsize - len - this.bufpos - 1, ret, 0, len - this.bufpos - 1);
/*     */       
/* 599 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     } 
/*     */     
/* 602 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void Done() {
/* 608 */     this.nextCharBuf = null;
/* 609 */     this.buffer = null;
/* 610 */     this.bufline = null;
/* 611 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustBeginLineColumn(int newLine, int newCol) {
/* 619 */     int len, start = this.tokenBegin;
/*     */ 
/*     */     
/* 622 */     if (this.bufpos >= this.tokenBegin) {
/*     */       
/* 624 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     }
/*     */     else {
/*     */       
/* 628 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     } 
/*     */     
/* 631 */     int i = 0, j = 0, k = 0;
/* 632 */     int nextColDiff = 0, columnDiff = 0;
/*     */     
/* 634 */     while (i < len && this.bufline[j = start % this.bufsize] == this.bufline[k = ++start % this.bufsize]) {
/*     */       
/* 636 */       this.bufline[j] = newLine;
/* 637 */       nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 638 */       this.bufcolumn[j] = newCol + columnDiff;
/* 639 */       columnDiff = nextColDiff;
/* 640 */       i++;
/*     */     } 
/*     */     
/* 643 */     if (i < len) {
/*     */       
/* 645 */       this.bufline[j] = newLine++;
/* 646 */       this.bufcolumn[j] = newCol + columnDiff;
/*     */       
/* 648 */       while (i++ < len) {
/*     */         
/* 650 */         if (this.bufline[j = start % this.bufsize] != this.bufline[++start % this.bufsize]) {
/* 651 */           this.bufline[j] = newLine++; continue;
/*     */         } 
/* 653 */         this.bufline[j] = newLine;
/*     */       } 
/*     */     } 
/*     */     
/* 657 */     this.line = this.bufline[j];
/* 658 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\JavaCharStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */