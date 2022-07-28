/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  56 */   private int currentIndent = 0;
/*     */ 
/*     */   
/*  59 */   private int indentStep = 4;
/*     */ 
/*     */   
/*  62 */   private int tabSize = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndentingWriter(Writer paramWriter) {
/*  69 */     super(paramWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndentingWriter(Writer paramWriter, int paramInt) {
/*  77 */     this(paramWriter);
/*     */     
/*  79 */     if (this.indentStep < 0) {
/*  80 */       throw new IllegalArgumentException("negative indent step");
/*     */     }
/*  82 */     this.indentStep = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndentingWriter(Writer paramWriter, int paramInt1, int paramInt2) {
/*  90 */     this(paramWriter);
/*     */     
/*  92 */     if (this.indentStep < 0) {
/*  93 */       throw new IllegalArgumentException("negative indent step");
/*     */     }
/*  95 */     this.indentStep = paramInt1;
/*  96 */     this.tabSize = paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int paramInt) throws IOException {
/* 103 */     checkWrite();
/* 104 */     super.write(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
/* 111 */     if (paramInt2 > 0) {
/* 112 */       checkWrite();
/*     */     }
/* 114 */     super.write(paramArrayOfchar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String paramString, int paramInt1, int paramInt2) throws IOException {
/* 121 */     if (paramInt2 > 0) {
/* 122 */       checkWrite();
/*     */     }
/* 124 */     super.write(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void newLine() throws IOException {
/* 132 */     super.newLine();
/* 133 */     this.beginningOfLine = true;
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
/*     */   protected void checkWrite() throws IOException {
/* 145 */     if (this.beginningOfLine) {
/* 146 */       this.beginningOfLine = false;
/* 147 */       int i = this.currentIndent;
/* 148 */       while (i >= this.tabSize) {
/* 149 */         super.write(9);
/* 150 */         i -= this.tabSize;
/*     */       } 
/* 152 */       while (i > 0) {
/* 153 */         super.write(32);
/* 154 */         i--;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void indentIn() {
/* 163 */     this.currentIndent += this.indentStep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void indentOut() {
/* 170 */     this.currentIndent -= this.indentStep;
/* 171 */     if (this.currentIndent < 0) {
/* 172 */       this.currentIndent = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pI() {
/* 179 */     indentIn();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pO() {
/* 186 */     indentOut();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void p(String paramString) throws IOException {
/* 193 */     write(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pln() throws IOException {
/* 200 */     newLine();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pln(String paramString) throws IOException {
/* 207 */     p(paramString);
/* 208 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void plnI(String paramString) throws IOException {
/* 215 */     p(paramString);
/* 216 */     pln();
/* 217 */     pI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pO(String paramString) throws IOException {
/* 224 */     pO();
/* 225 */     p(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOln(String paramString) throws IOException {
/* 232 */     pO(paramString);
/* 233 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOlnI(String paramString) throws IOException {
/* 243 */     pO(paramString);
/* 244 */     pln();
/* 245 */     pI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void p(Object paramObject) throws IOException {
/* 252 */     write(paramObject.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pln(Object paramObject) throws IOException {
/* 258 */     p(paramObject.toString());
/* 259 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void plnI(Object paramObject) throws IOException {
/* 266 */     p(paramObject.toString());
/* 267 */     pln();
/* 268 */     pI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pO(Object paramObject) throws IOException {
/* 275 */     pO();
/* 276 */     p(paramObject.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOln(Object paramObject) throws IOException {
/* 283 */     pO(paramObject.toString());
/* 284 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOlnI(Object paramObject) throws IOException {
/* 294 */     pO(paramObject.toString());
/* 295 */     pln();
/* 296 */     pI();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\IndentingWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */