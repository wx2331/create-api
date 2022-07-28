/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.nio.CharBuffer;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiagnosticSource
/*     */ {
/*  50 */   public static final DiagnosticSource NO_SOURCE = new DiagnosticSource()
/*     */     {
/*     */       protected boolean findLine(int param1Int) {
/*  53 */         return false;
/*     */       }
/*     */     };
/*     */   protected JavaFileObject fileObject; protected EndPosTable endPosTable;
/*     */   public DiagnosticSource(JavaFileObject paramJavaFileObject, AbstractLog paramAbstractLog) {
/*  58 */     this.fileObject = paramJavaFileObject;
/*  59 */     this.log = paramAbstractLog;
/*     */   }
/*     */   protected SoftReference<char[]> refBuf; protected char[] buf; protected int bufLen; protected int lineStart;
/*     */   protected int line;
/*     */   protected AbstractLog log;
/*     */   
/*     */   private DiagnosticSource() {}
/*     */   
/*     */   public JavaFileObject getFile() {
/*  68 */     return this.fileObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber(int paramInt) {
/*     */     try {
/*  77 */       if (findLine(paramInt)) {
/*  78 */         return this.line;
/*     */       }
/*  80 */       return 0;
/*     */     } finally {
/*  82 */       this.buf = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnNumber(int paramInt, boolean paramBoolean) {
/*     */     try {
/*  92 */       if (findLine(paramInt)) {
/*  93 */         int i = 0; int j;
/*  94 */         for (j = this.lineStart; j < paramInt; j++) {
/*  95 */           if (j >= this.bufLen) {
/*  96 */             return 0;
/*     */           }
/*  98 */           if (this.buf[j] == '\t' && paramBoolean) {
/*  99 */             i = i / 8 * 8 + 8;
/*     */           } else {
/* 101 */             i++;
/*     */           } 
/*     */         } 
/* 104 */         j = i + 1; return j;
/*     */       } 
/* 106 */       return 0;
/*     */     } finally {
/* 108 */       this.buf = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLine(int paramInt) {
/*     */     try {
/* 116 */       if (!findLine(paramInt)) {
/* 117 */         return null;
/*     */       }
/* 119 */       int i = this.lineStart;
/* 120 */       while (i < this.bufLen && this.buf[i] != '\r' && this.buf[i] != '\n')
/* 121 */         i++; 
/* 122 */       if (i - this.lineStart == 0)
/* 123 */         return null; 
/* 124 */       return new String(this.buf, this.lineStart, i - this.lineStart);
/*     */     } finally {
/* 126 */       this.buf = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public EndPosTable getEndPosTable() {
/* 131 */     return this.endPosTable;
/*     */   }
/*     */   
/*     */   public void setEndPosTable(EndPosTable paramEndPosTable) {
/* 135 */     if (this.endPosTable != null && this.endPosTable != paramEndPosTable)
/* 136 */       throw new IllegalStateException("endPosTable already set"); 
/* 137 */     this.endPosTable = paramEndPosTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean findLine(int paramInt) {
/* 144 */     if (paramInt == -1) {
/* 145 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 149 */       if (this.buf == null && this.refBuf != null) {
/* 150 */         this.buf = this.refBuf.get();
/*     */       }
/* 152 */       if (this.buf == null) {
/* 153 */         this.buf = initBuf(this.fileObject);
/* 154 */         this.lineStart = 0;
/* 155 */         this.line = 1;
/* 156 */       } else if (this.lineStart > paramInt) {
/* 157 */         this.lineStart = 0;
/* 158 */         this.line = 1;
/*     */       } 
/*     */       
/* 161 */       int i = this.lineStart;
/* 162 */       while (i < this.bufLen && i < paramInt) {
/* 163 */         switch (this.buf[i++]) {
/*     */           case '\r':
/* 165 */             if (i < this.bufLen && this.buf[i] == '\n') i++; 
/* 166 */             this.line++;
/* 167 */             this.lineStart = i;
/*     */           
/*     */           case '\n':
/* 170 */             this.line++;
/* 171 */             this.lineStart = i;
/*     */         } 
/*     */       
/*     */       } 
/* 175 */       return (i <= this.bufLen);
/* 176 */     } catch (IOException iOException) {
/* 177 */       this.log.directError("source.unavailable", new Object[0]);
/* 178 */       this.buf = new char[0];
/* 179 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected char[] initBuf(JavaFileObject paramJavaFileObject) throws IOException {
/*     */     char[] arrayOfChar;
/* 185 */     CharSequence charSequence = paramJavaFileObject.getCharContent(true);
/* 186 */     if (charSequence instanceof CharBuffer) {
/* 187 */       CharBuffer charBuffer = (CharBuffer)charSequence;
/* 188 */       arrayOfChar = JavacFileManager.toArray(charBuffer);
/* 189 */       this.bufLen = charBuffer.limit();
/*     */     } else {
/* 191 */       arrayOfChar = charSequence.toString().toCharArray();
/* 192 */       this.bufLen = arrayOfChar.length;
/*     */     } 
/* 194 */     this.refBuf = (SoftReference)new SoftReference<>(arrayOfChar);
/* 195 */     return arrayOfChar;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\DiagnosticSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */