/*     */ package com.sun.tools.example.debug.expr;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ASCII_UCodeESC_CharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   
/*     */   static final int hexval(char paramChar) throws IOException {
/*  48 */     switch (paramChar) {
/*     */       
/*     */       case '0':
/*  51 */         return 0;
/*     */       case '1':
/*  53 */         return 1;
/*     */       case '2':
/*  55 */         return 2;
/*     */       case '3':
/*  57 */         return 3;
/*     */       case '4':
/*  59 */         return 4;
/*     */       case '5':
/*  61 */         return 5;
/*     */       case '6':
/*  63 */         return 6;
/*     */       case '7':
/*  65 */         return 7;
/*     */       case '8':
/*  67 */         return 8;
/*     */       case '9':
/*  69 */         return 9;
/*     */       
/*     */       case 'A':
/*     */       case 'a':
/*  73 */         return 10;
/*     */       case 'B':
/*     */       case 'b':
/*  76 */         return 11;
/*     */       case 'C':
/*     */       case 'c':
/*  79 */         return 12;
/*     */       case 'D':
/*     */       case 'd':
/*  82 */         return 13;
/*     */       case 'E':
/*     */       case 'e':
/*  85 */         return 14;
/*     */       case 'F':
/*     */       case 'f':
/*  88 */         return 15;
/*     */     } 
/*     */     
/*  91 */     throw new IOException();
/*     */   }
/*     */   
/*  94 */   public int bufpos = -1;
/*     */   
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*     */   private int[] bufline;
/*     */   private int[] bufcolumn;
/* 101 */   private int column = 0;
/* 102 */   private int line = 1;
/*     */   
/*     */   private InputStream inputStream;
/*     */   
/*     */   private boolean prevCharIsCR = false;
/*     */   
/*     */   private boolean prevCharIsLF = false;
/*     */   private byte[] nextCharBuf;
/*     */   private char[] buffer;
/* 111 */   private int maxNextCharInd = 0;
/* 112 */   private int nextCharInd = -1;
/* 113 */   private int inBuf = 0;
/*     */ 
/*     */   
/*     */   private final void ExpandBuff(boolean paramBoolean) {
/* 117 */     char[] arrayOfChar = new char[this.bufsize + 2048];
/* 118 */     int[] arrayOfInt1 = new int[this.bufsize + 2048];
/* 119 */     int[] arrayOfInt2 = new int[this.bufsize + 2048];
/*     */ 
/*     */     
/*     */     try {
/* 123 */       if (paramBoolean)
/*     */       {
/* 125 */         System.arraycopy(this.buffer, this.tokenBegin, arrayOfChar, 0, this.bufsize - this.tokenBegin);
/* 126 */         System.arraycopy(this.buffer, 0, arrayOfChar, this.bufsize - this.tokenBegin, this.bufpos);
/*     */         
/* 128 */         this.buffer = arrayOfChar;
/*     */         
/* 130 */         System.arraycopy(this.bufline, this.tokenBegin, arrayOfInt1, 0, this.bufsize - this.tokenBegin);
/* 131 */         System.arraycopy(this.bufline, 0, arrayOfInt1, this.bufsize - this.tokenBegin, this.bufpos);
/* 132 */         this.bufline = arrayOfInt1;
/*     */         
/* 134 */         System.arraycopy(this.bufcolumn, this.tokenBegin, arrayOfInt2, 0, this.bufsize - this.tokenBegin);
/* 135 */         System.arraycopy(this.bufcolumn, 0, arrayOfInt2, this.bufsize - this.tokenBegin, this.bufpos);
/* 136 */         this.bufcolumn = arrayOfInt2;
/*     */         
/* 138 */         this.bufpos += this.bufsize - this.tokenBegin;
/*     */       }
/*     */       else
/*     */       {
/* 142 */         System.arraycopy(this.buffer, this.tokenBegin, arrayOfChar, 0, this.bufsize - this.tokenBegin);
/* 143 */         this.buffer = arrayOfChar;
/*     */         
/* 145 */         System.arraycopy(this.bufline, this.tokenBegin, arrayOfInt1, 0, this.bufsize - this.tokenBegin);
/* 146 */         this.bufline = arrayOfInt1;
/*     */         
/* 148 */         System.arraycopy(this.bufcolumn, this.tokenBegin, arrayOfInt2, 0, this.bufsize - this.tokenBegin);
/* 149 */         this.bufcolumn = arrayOfInt2;
/*     */         
/* 151 */         this.bufpos -= this.tokenBegin;
/*     */       }
/*     */     
/* 154 */     } catch (Throwable throwable) {
/*     */       
/* 156 */       throw new Error(throwable.getMessage());
/*     */     } 
/*     */     
/* 159 */     this.available = this.bufsize += 2048;
/* 160 */     this.tokenBegin = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void FillBuff() throws IOException {
/* 166 */     if (this.maxNextCharInd == 4096)
/* 167 */       this.maxNextCharInd = this.nextCharInd = 0; 
/*     */     try {
/*     */       int i;
/* 170 */       if ((i = this.inputStream.read(this.nextCharBuf, this.maxNextCharInd, 4096 - this.maxNextCharInd)) == -1) {
/*     */ 
/*     */         
/* 173 */         this.inputStream.close();
/* 174 */         throw new IOException();
/*     */       } 
/*     */       
/* 177 */       this.maxNextCharInd += i;
/*     */       
/*     */       return;
/* 180 */     } catch (IOException iOException) {
/* 181 */       if (this.bufpos != 0) {
/*     */         
/* 183 */         this.bufpos--;
/* 184 */         backup(0);
/*     */       }
/*     */       else {
/*     */         
/* 188 */         this.bufline[this.bufpos] = this.line;
/* 189 */         this.bufcolumn[this.bufpos] = this.column;
/*     */       } 
/* 191 */       throw iOException;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final byte ReadByte() throws IOException {
/* 197 */     if (++this.nextCharInd >= this.maxNextCharInd) {
/* 198 */       FillBuff();
/*     */     }
/* 200 */     return this.nextCharBuf[this.nextCharInd];
/*     */   }
/*     */ 
/*     */   
/*     */   public final char BeginToken() throws IOException {
/* 205 */     if (this.inBuf > 0) {
/*     */       
/* 207 */       this.inBuf--;
/* 208 */       return this.buffer[this.tokenBegin = (this.bufpos == this.bufsize - 1) ? (this.bufpos = 0) : ++this.bufpos];
/*     */     } 
/*     */ 
/*     */     
/* 212 */     this.tokenBegin = 0;
/* 213 */     this.bufpos = -1;
/*     */     
/* 215 */     return readChar();
/*     */   }
/*     */ 
/*     */   
/*     */   private final void AdjustBuffSize() {
/* 220 */     if (this.available == this.bufsize) {
/*     */       
/* 222 */       if (this.tokenBegin > 2048) {
/*     */         
/* 224 */         this.bufpos = 0;
/* 225 */         this.available = this.tokenBegin;
/*     */       } else {
/*     */         
/* 228 */         ExpandBuff(false);
/*     */       } 
/* 230 */     } else if (this.available > this.tokenBegin) {
/* 231 */       this.available = this.bufsize;
/* 232 */     } else if (this.tokenBegin - this.available < 2048) {
/* 233 */       ExpandBuff(true);
/*     */     } else {
/* 235 */       this.available = this.tokenBegin;
/*     */     } 
/*     */   }
/*     */   
/*     */   private final void UpdateLineColumn(char paramChar) {
/* 240 */     this.column++;
/*     */     
/* 242 */     if (this.prevCharIsLF) {
/*     */       
/* 244 */       this.prevCharIsLF = false;
/* 245 */       this.line += this.column = 1;
/*     */     }
/* 247 */     else if (this.prevCharIsCR) {
/*     */       
/* 249 */       this.prevCharIsCR = false;
/* 250 */       if (paramChar == '\n') {
/*     */         
/* 252 */         this.prevCharIsLF = true;
/*     */       } else {
/*     */         
/* 255 */         this.line += this.column = 1;
/*     */       } 
/*     */     } 
/* 258 */     switch (paramChar) {
/*     */       
/*     */       case '\r':
/* 261 */         this.prevCharIsCR = true;
/*     */         break;
/*     */       case '\n':
/* 264 */         this.prevCharIsLF = true;
/*     */         break;
/*     */       case '\t':
/* 267 */         this.column--;
/* 268 */         this.column += 8 - (this.column & 0x7);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 274 */     this.bufline[this.bufpos] = this.line;
/* 275 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */ 
/*     */   
/*     */   public final char readChar() throws IOException {
/* 280 */     if (this.inBuf > 0) {
/*     */       
/* 282 */       this.inBuf--;
/* 283 */       return this.buffer[(this.bufpos == this.bufsize - 1) ? (this.bufpos = 0) : ++this.bufpos];
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 288 */     if (++this.bufpos == this.available) {
/* 289 */       AdjustBuffSize();
/*     */     }
/* 291 */     char c = (char)(0xFF & ReadByte()); if ((c = (char)(0xFF & ReadByte())) == '\\') {
/*     */       
/* 293 */       UpdateLineColumn(c);
/*     */       
/* 295 */       byte b = 1;
/*     */ 
/*     */       
/*     */       label48: while (true) {
/* 299 */         if (++this.bufpos == this.available) {
/* 300 */           AdjustBuffSize();
/*     */         }
/*     */         
/*     */         try {
/* 304 */           this.buffer[this.bufpos] = c = (char)(0xFF & ReadByte()); if ((c = (char)(0xFF & ReadByte())) != '\\')
/*     */           {
/* 306 */             UpdateLineColumn(c);
/*     */             
/* 308 */             if (c == 'u' && (b & 0x1) == 1) {
/*     */               
/* 310 */               if (--this.bufpos < 0) {
/* 311 */                 this.bufpos = this.bufsize - 1;
/*     */                 break label48;
/*     */               } 
/*     */               break;
/*     */             } 
/* 316 */             backup(b);
/* 317 */             return '\\';
/*     */           }
/*     */         
/* 320 */         } catch (IOException iOException) {
/*     */           
/* 322 */           if (b > 1) {
/* 323 */             backup(b);
/*     */           }
/* 325 */           return '\\';
/*     */         } 
/*     */         
/* 328 */         UpdateLineColumn(c);
/* 329 */         b++;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 335 */         while ((c = (char)(0xFF & ReadByte())) == 'u') {
/* 336 */           this.column++;
/*     */         }
/* 338 */         this.buffer[this.bufpos] = 
/*     */ 
/*     */           
/* 341 */           c = (char)(hexval(c) << 12 | hexval((char)(0xFF & ReadByte())) << 8 | hexval((char)(0xFF & ReadByte())) << 4 | hexval((char)(0xFF & ReadByte())));
/*     */         
/* 343 */         this.column += 4;
/*     */       }
/* 345 */       catch (IOException iOException) {
/*     */         
/* 347 */         throw new Error("Invalid escape character at line " + this.line + " column " + this.column + ".");
/*     */       } 
/*     */ 
/*     */       
/* 351 */       if (b == 1) {
/* 352 */         return c;
/*     */       }
/*     */       
/* 355 */       backup(b - 1);
/* 356 */       return '\\';
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 361 */     UpdateLineColumn(c);
/* 362 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final int getColumn() {
/* 372 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final int getLine() {
/* 381 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndColumn() {
/* 385 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndLine() {
/* 389 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getBeginColumn() {
/* 393 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public final int getBeginLine() {
/* 397 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   
/*     */   public final void backup(int paramInt) {
/* 402 */     this.inBuf += paramInt;
/* 403 */     if ((this.bufpos -= paramInt) < 0) {
/* 404 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ASCII_UCodeESC_CharStream(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3) {
/* 410 */     this.inputStream = paramInputStream;
/* 411 */     this.line = paramInt1;
/* 412 */     this.column = paramInt2 - 1;
/*     */     
/* 414 */     this.available = this.bufsize = paramInt3;
/* 415 */     this.buffer = new char[paramInt3];
/* 416 */     this.bufline = new int[paramInt3];
/* 417 */     this.bufcolumn = new int[paramInt3];
/* 418 */     this.nextCharBuf = new byte[4096];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASCII_UCodeESC_CharStream(InputStream paramInputStream, int paramInt1, int paramInt2) {
/* 424 */     this(paramInputStream, paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3) {
/* 429 */     this.inputStream = paramInputStream;
/* 430 */     this.line = paramInt1;
/* 431 */     this.column = paramInt2 - 1;
/*     */     
/* 433 */     if (this.buffer == null || paramInt3 != this.buffer.length) {
/*     */       
/* 435 */       this.available = this.bufsize = paramInt3;
/* 436 */       this.buffer = new char[paramInt3];
/* 437 */       this.bufline = new int[paramInt3];
/* 438 */       this.bufcolumn = new int[paramInt3];
/* 439 */       this.nextCharBuf = new byte[4096];
/*     */     } 
/* 441 */     this.prevCharIsLF = this.prevCharIsCR = false;
/* 442 */     this.tokenBegin = this.inBuf = this.maxNextCharInd = 0;
/* 443 */     this.nextCharInd = this.bufpos = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream paramInputStream, int paramInt1, int paramInt2) {
/* 449 */     ReInit(paramInputStream, paramInt1, paramInt2, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String GetImage() {
/* 454 */     if (this.bufpos >= this.tokenBegin) {
/* 455 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 457 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final char[] GetSuffix(int paramInt) {
/* 463 */     char[] arrayOfChar = new char[paramInt];
/*     */     
/* 465 */     if (this.bufpos + 1 >= paramInt) {
/* 466 */       System.arraycopy(this.buffer, this.bufpos - paramInt + 1, arrayOfChar, 0, paramInt);
/*     */     } else {
/*     */       
/* 469 */       System.arraycopy(this.buffer, this.bufsize - paramInt - this.bufpos - 1, arrayOfChar, 0, paramInt - this.bufpos - 1);
/*     */       
/* 471 */       System.arraycopy(this.buffer, 0, arrayOfChar, paramInt - this.bufpos - 1, this.bufpos + 1);
/*     */     } 
/*     */     
/* 474 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   
/*     */   public void Done() {
/* 479 */     this.nextCharBuf = null;
/* 480 */     this.buffer = null;
/* 481 */     this.bufline = null;
/* 482 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustBeginLineColumn(int paramInt1, int paramInt2) {
/* 490 */     int j, i = this.tokenBegin;
/*     */ 
/*     */     
/* 493 */     if (this.bufpos >= this.tokenBegin) {
/*     */       
/* 495 */       j = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     }
/*     */     else {
/*     */       
/* 499 */       j = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     } 
/*     */     
/* 502 */     byte b = 0; int k = 0, m = 0;
/* 503 */     int n = 0, i1 = 0;
/*     */     
/* 505 */     while (b < j && this.bufline[k = i % this.bufsize] == this.bufline[m = ++i % this.bufsize]) {
/*     */ 
/*     */       
/* 508 */       this.bufline[k] = paramInt1;
/* 509 */       n = i1 + this.bufcolumn[m] - this.bufcolumn[k];
/* 510 */       this.bufcolumn[k] = paramInt2 + i1;
/* 511 */       i1 = n;
/* 512 */       b++;
/*     */     } 
/*     */     
/* 515 */     if (b < j) {
/*     */       
/* 517 */       this.bufline[k] = paramInt1++;
/* 518 */       this.bufcolumn[k] = paramInt2 + i1;
/*     */       
/* 520 */       while (b++ < j) {
/*     */         
/* 522 */         if (this.bufline[k = i % this.bufsize] != this.bufline[++i % this.bufsize]) {
/* 523 */           this.bufline[k] = paramInt1++; continue;
/*     */         } 
/* 525 */         this.bufline[k] = paramInt1;
/*     */       } 
/*     */     } 
/*     */     
/* 529 */     this.line = this.bufline[k];
/* 530 */     this.column = this.bufcolumn[k];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\expr\ASCII_UCodeESC_CharStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */