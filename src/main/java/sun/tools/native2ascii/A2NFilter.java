/*     */ package sun.tools.native2ascii;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
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
/*     */ class A2NFilter
/*     */   extends FilterReader
/*     */ {
/*  45 */   private char[] trailChars = null;
/*     */   
/*     */   public A2NFilter(Reader paramReader) {
/*  48 */     super(paramReader);
/*     */   }
/*     */   
/*     */   public int read(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
/*  52 */     int i = 0;
/*  53 */     byte b1 = 0;
/*     */     
/*  55 */     char[] arrayOfChar = new char[paramInt2];
/*  56 */     boolean bool1 = false;
/*  57 */     boolean bool2 = false;
/*     */ 
/*     */     
/*  60 */     if (this.trailChars != null) {
/*  61 */       for (byte b = 0; b < this.trailChars.length; b++)
/*  62 */         arrayOfChar[b] = this.trailChars[b]; 
/*  63 */       i = this.trailChars.length;
/*  64 */       this.trailChars = null;
/*     */     } 
/*     */     
/*  67 */     int j = this.in.read(arrayOfChar, i, paramInt2 - i);
/*  68 */     if (j < 0) {
/*  69 */       bool2 = true;
/*  70 */       if (i == 0)
/*  71 */         return -1; 
/*     */     } else {
/*  73 */       i += j;
/*     */     } 
/*     */     
/*  76 */     for (byte b2 = 0; b2 < i; ) {
/*  77 */       char c = arrayOfChar[b2++];
/*     */       
/*  79 */       if (c != '\\' || (bool2 && i <= 5)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  87 */         paramArrayOfchar[b1++] = c;
/*     */         
/*     */         continue;
/*     */       } 
/*  91 */       int k = i - b2;
/*  92 */       if (k < 5) {
/*     */ 
/*     */         
/*  95 */         this.trailChars = new char[1 + k];
/*  96 */         this.trailChars[0] = c;
/*  97 */         for (byte b = 0; b < k; b++) {
/*  98 */           this.trailChars[1 + b] = arrayOfChar[b2 + b];
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/* 103 */       c = arrayOfChar[b2++];
/* 104 */       if (c != 'u') {
/*     */         
/* 106 */         paramArrayOfchar[b1++] = '\\';
/* 107 */         paramArrayOfchar[b1++] = c;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 112 */       char c1 = Character.MIN_VALUE;
/* 113 */       boolean bool = true;
/*     */       try {
/* 115 */         c1 = (char)Integer.parseInt(new String(arrayOfChar, b2, 4), 16);
/* 116 */       } catch (NumberFormatException numberFormatException) {
/* 117 */         bool = false;
/*     */       } 
/* 119 */       if (bool && Main.canConvert(c1)) {
/*     */         
/* 121 */         paramArrayOfchar[b1++] = c1;
/* 122 */         b2 += 4;
/*     */         continue;
/*     */       } 
/* 125 */       paramArrayOfchar[b1++] = '\\';
/* 126 */       paramArrayOfchar[b1++] = 'u';
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     return b1;
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/* 136 */     char[] arrayOfChar = new char[1];
/*     */     
/* 138 */     if (read(arrayOfChar, 0, 1) == -1) {
/* 139 */       return -1;
/*     */     }
/* 141 */     return arrayOfChar[0];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\native2ascii\A2NFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */