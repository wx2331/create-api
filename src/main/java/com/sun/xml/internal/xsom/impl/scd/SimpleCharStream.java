/*     */ package com.sun.xml.internal.xsom.impl.scd;
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
/*     */ public class SimpleCharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*  40 */   public int bufpos = -1;
/*     */   
/*     */   protected int[] bufline;
/*     */   protected int[] bufcolumn;
/*  44 */   protected int column = 0;
/*  45 */   protected int line = 1;
/*     */   
/*     */   protected boolean prevCharIsCR = false;
/*     */   
/*     */   protected boolean prevCharIsLF = false;
/*     */   
/*     */   protected Reader inputStream;
/*     */   protected char[] buffer;
/*  53 */   protected int maxNextCharInd = 0;
/*  54 */   protected int inBuf = 0;
/*  55 */   protected int tabSize = 8;
/*     */   
/*  57 */   protected void setTabSize(int i) { this.tabSize = i; } protected int getTabSize(int i) {
/*  58 */     return this.tabSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ExpandBuff(boolean wrapAround) {
/*  63 */     char[] newbuffer = new char[this.bufsize + 2048];
/*  64 */     int[] newbufline = new int[this.bufsize + 2048];
/*  65 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */ 
/*     */     
/*     */     try {
/*  69 */       if (wrapAround)
/*     */       {
/*  71 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  72 */         System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
/*     */         
/*  74 */         this.buffer = newbuffer;
/*     */         
/*  76 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  77 */         System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/*  78 */         this.bufline = newbufline;
/*     */         
/*  80 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/*  81 */         System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/*  82 */         this.bufcolumn = newbufcolumn;
/*     */         
/*  84 */         this.maxNextCharInd = this.bufpos += this.bufsize - this.tokenBegin;
/*     */       }
/*     */       else
/*     */       {
/*  88 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  89 */         this.buffer = newbuffer;
/*     */         
/*  91 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  92 */         this.bufline = newbufline;
/*     */         
/*  94 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/*  95 */         this.bufcolumn = newbufcolumn;
/*     */         
/*  97 */         this.maxNextCharInd = this.bufpos -= this.tokenBegin;
/*     */       }
/*     */     
/* 100 */     } catch (Throwable t) {
/*     */       
/* 102 */       throw new Error(t.getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 106 */     this.bufsize += 2048;
/* 107 */     this.available = this.bufsize;
/* 108 */     this.tokenBegin = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void FillBuff() throws IOException {
/* 113 */     if (this.maxNextCharInd == this.available)
/*     */     {
/* 115 */       if (this.available == this.bufsize) {
/*     */         
/* 117 */         if (this.tokenBegin > 2048) {
/*     */           
/* 119 */           this.bufpos = this.maxNextCharInd = 0;
/* 120 */           this.available = this.tokenBegin;
/*     */         }
/* 122 */         else if (this.tokenBegin < 0) {
/* 123 */           this.bufpos = this.maxNextCharInd = 0;
/*     */         } else {
/* 125 */           ExpandBuff(false);
/*     */         } 
/* 127 */       } else if (this.available > this.tokenBegin) {
/* 128 */         this.available = this.bufsize;
/* 129 */       } else if (this.tokenBegin - this.available < 2048) {
/* 130 */         ExpandBuff(true);
/*     */       } else {
/* 132 */         this.available = this.tokenBegin;
/*     */       } 
/*     */     }
/*     */     try {
/*     */       int i;
/* 137 */       if ((i = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd)) == -1) {
/*     */ 
/*     */         
/* 140 */         this.inputStream.close();
/* 141 */         throw new IOException();
/*     */       } 
/*     */       
/* 144 */       this.maxNextCharInd += i;
/*     */       
/*     */       return;
/* 147 */     } catch (IOException e) {
/* 148 */       this.bufpos--;
/* 149 */       backup(0);
/* 150 */       if (this.tokenBegin == -1)
/* 151 */         this.tokenBegin = this.bufpos; 
/* 152 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public char BeginToken() throws IOException {
/* 158 */     this.tokenBegin = -1;
/* 159 */     char c = readChar();
/* 160 */     this.tokenBegin = this.bufpos;
/*     */     
/* 162 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void UpdateLineColumn(char c) {
/* 167 */     this.column++;
/*     */     
/* 169 */     if (this.prevCharIsLF) {
/*     */       
/* 171 */       this.prevCharIsLF = false;
/* 172 */       this.line += this.column = 1;
/*     */     }
/* 174 */     else if (this.prevCharIsCR) {
/*     */       
/* 176 */       this.prevCharIsCR = false;
/* 177 */       if (c == '\n') {
/*     */         
/* 179 */         this.prevCharIsLF = true;
/*     */       } else {
/*     */         
/* 182 */         this.line += this.column = 1;
/*     */       } 
/*     */     } 
/* 185 */     switch (c) {
/*     */       
/*     */       case '\r':
/* 188 */         this.prevCharIsCR = true;
/*     */         break;
/*     */       case '\n':
/* 191 */         this.prevCharIsLF = true;
/*     */         break;
/*     */       case '\t':
/* 194 */         this.column--;
/* 195 */         this.column += this.tabSize - this.column % this.tabSize;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 201 */     this.bufline[this.bufpos] = this.line;
/* 202 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */ 
/*     */   
/*     */   public char readChar() throws IOException {
/* 207 */     if (this.inBuf > 0) {
/*     */       
/* 209 */       this.inBuf--;
/*     */       
/* 211 */       if (++this.bufpos == this.bufsize) {
/* 212 */         this.bufpos = 0;
/*     */       }
/* 214 */       return this.buffer[this.bufpos];
/*     */     } 
/*     */     
/* 217 */     if (++this.bufpos >= this.maxNextCharInd) {
/* 218 */       FillBuff();
/*     */     }
/* 220 */     char c = this.buffer[this.bufpos];
/*     */     
/* 222 */     UpdateLineColumn(c);
/* 223 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 232 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 241 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getEndColumn() {
/* 245 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getEndLine() {
/* 249 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getBeginColumn() {
/* 253 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public int getBeginLine() {
/* 257 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   
/*     */   public void backup(int amount) {
/* 262 */     this.inBuf += amount;
/* 263 */     if ((this.bufpos -= amount) < 0) {
/* 264 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 270 */     this.inputStream = dstream;
/* 271 */     this.line = startline;
/* 272 */     this.column = startcolumn - 1;
/*     */     
/* 274 */     this.available = this.bufsize = buffersize;
/* 275 */     this.buffer = new char[buffersize];
/* 276 */     this.bufline = new int[buffersize];
/* 277 */     this.bufcolumn = new int[buffersize];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn) {
/* 283 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream) {
/* 288 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 293 */     this.inputStream = dstream;
/* 294 */     this.line = startline;
/* 295 */     this.column = startcolumn - 1;
/*     */     
/* 297 */     if (this.buffer == null || buffersize != this.buffer.length) {
/*     */       
/* 299 */       this.available = this.bufsize = buffersize;
/* 300 */       this.buffer = new char[buffersize];
/* 301 */       this.bufline = new int[buffersize];
/* 302 */       this.bufcolumn = new int[buffersize];
/*     */     } 
/* 304 */     this.prevCharIsLF = this.prevCharIsCR = false;
/* 305 */     this.tokenBegin = this.inBuf = this.maxNextCharInd = 0;
/* 306 */     this.bufpos = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn) {
/* 312 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream) {
/* 317 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize) throws UnsupportedEncodingException {
/* 322 */     this((encoding == null) ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 328 */     this(new InputStreamReader(dstream), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, String encoding, int startline, int startcolumn) throws UnsupportedEncodingException {
/* 334 */     this(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, int startline, int startcolumn) {
/* 340 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, String encoding) throws UnsupportedEncodingException {
/* 345 */     this(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream) {
/* 350 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn, int buffersize) throws UnsupportedEncodingException {
/* 356 */     ReInit((encoding == null) ? new InputStreamReader(dstream) : new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 362 */     ReInit(new InputStreamReader(dstream), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding) throws UnsupportedEncodingException {
/* 367 */     ReInit(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream) {
/* 372 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, String encoding, int startline, int startcolumn) throws UnsupportedEncodingException {
/* 377 */     ReInit(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn) {
/* 382 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public String GetImage() {
/* 386 */     if (this.bufpos >= this.tokenBegin) {
/* 387 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 389 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] GetSuffix(int len) {
/* 395 */     char[] ret = new char[len];
/*     */     
/* 397 */     if (this.bufpos + 1 >= len) {
/* 398 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     } else {
/*     */       
/* 401 */       System.arraycopy(this.buffer, this.bufsize - len - this.bufpos - 1, ret, 0, len - this.bufpos - 1);
/*     */       
/* 403 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     } 
/*     */     
/* 406 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void Done() {
/* 411 */     this.buffer = null;
/* 412 */     this.bufline = null;
/* 413 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustBeginLineColumn(int newLine, int newCol) {
/* 421 */     int len, start = this.tokenBegin;
/*     */ 
/*     */     
/* 424 */     if (this.bufpos >= this.tokenBegin) {
/*     */       
/* 426 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     }
/*     */     else {
/*     */       
/* 430 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     } 
/*     */     
/* 433 */     int i = 0, j = 0, k = 0;
/* 434 */     int nextColDiff = 0, columnDiff = 0;
/*     */     
/* 436 */     while (i < len && this.bufline[j = start % this.bufsize] == this.bufline[k = ++start % this.bufsize]) {
/*     */ 
/*     */       
/* 439 */       this.bufline[j] = newLine;
/* 440 */       nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 441 */       this.bufcolumn[j] = newCol + columnDiff;
/* 442 */       columnDiff = nextColDiff;
/* 443 */       i++;
/*     */     } 
/*     */     
/* 446 */     if (i < len) {
/*     */       
/* 448 */       this.bufline[j] = newLine++;
/* 449 */       this.bufcolumn[j] = newCol + columnDiff;
/*     */       
/* 451 */       while (i++ < len) {
/*     */         
/* 453 */         if (this.bufline[j = start % this.bufsize] != this.bufline[++start % this.bufsize]) {
/* 454 */           this.bufline[j] = newLine++; continue;
/*     */         } 
/* 456 */         this.bufline[j] = newLine;
/*     */       } 
/*     */     } 
/*     */     
/* 460 */     this.line = this.bufline[j];
/* 461 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\SimpleCharStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */