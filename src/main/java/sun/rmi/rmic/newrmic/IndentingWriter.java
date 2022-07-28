/*     */ package sun.rmi.rmic.newrmic;
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
/*     */ public class IndentingWriter
/*     */   extends BufferedWriter
/*     */ {
/*     */   private final int indentStep;
/*     */   private final int tabSize;
/*     */   private boolean beginningOfLine = true;
/*  57 */   private int currentIndent = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndentingWriter(Writer paramWriter) {
/*  64 */     this(paramWriter, 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndentingWriter(Writer paramWriter, int paramInt) {
/*  72 */     this(paramWriter, paramInt, 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndentingWriter(Writer paramWriter, int paramInt1, int paramInt2) {
/*  80 */     super(paramWriter);
/*  81 */     if (paramInt1 < 0) {
/*  82 */       throw new IllegalArgumentException("negative indent step");
/*     */     }
/*  84 */     if (paramInt2 < 0) {
/*  85 */       throw new IllegalArgumentException("negative tab size");
/*     */     }
/*  87 */     this.indentStep = paramInt1;
/*  88 */     this.tabSize = paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int paramInt) throws IOException {
/*  95 */     checkWrite();
/*  96 */     super.write(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
/* 103 */     if (paramInt2 > 0) {
/* 104 */       checkWrite();
/*     */     }
/* 106 */     super.write(paramArrayOfchar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String paramString, int paramInt1, int paramInt2) throws IOException {
/* 113 */     if (paramInt2 > 0) {
/* 114 */       checkWrite();
/*     */     }
/* 116 */     super.write(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void newLine() throws IOException {
/* 124 */     super.newLine();
/* 125 */     this.beginningOfLine = true;
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
/* 137 */     if (this.beginningOfLine) {
/* 138 */       this.beginningOfLine = false;
/* 139 */       int i = this.currentIndent;
/* 140 */       while (i >= this.tabSize) {
/* 141 */         super.write(9);
/* 142 */         i -= this.tabSize;
/*     */       } 
/* 144 */       while (i > 0) {
/* 145 */         super.write(32);
/* 146 */         i--;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void indentIn() {
/* 155 */     this.currentIndent += this.indentStep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void indentOut() {
/* 162 */     this.currentIndent -= this.indentStep;
/* 163 */     if (this.currentIndent < 0) {
/* 164 */       this.currentIndent = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pI() {
/* 171 */     indentIn();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pO() {
/* 178 */     indentOut();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void p(String paramString) throws IOException {
/* 185 */     write(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pln() throws IOException {
/* 192 */     newLine();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pln(String paramString) throws IOException {
/* 199 */     p(paramString);
/* 200 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void plnI(String paramString) throws IOException {
/* 207 */     p(paramString);
/* 208 */     pln();
/* 209 */     pI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pO(String paramString) throws IOException {
/* 216 */     pO();
/* 217 */     p(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOln(String paramString) throws IOException {
/* 224 */     pO(paramString);
/* 225 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOlnI(String paramString) throws IOException {
/* 235 */     pO(paramString);
/* 236 */     pln();
/* 237 */     pI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void p(Object paramObject) throws IOException {
/* 244 */     write(paramObject.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pln(Object paramObject) throws IOException {
/* 251 */     p(paramObject.toString());
/* 252 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void plnI(Object paramObject) throws IOException {
/* 259 */     p(paramObject.toString());
/* 260 */     pln();
/* 261 */     pI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pO(Object paramObject) throws IOException {
/* 268 */     pO();
/* 269 */     p(paramObject.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOln(Object paramObject) throws IOException {
/* 276 */     pO(paramObject.toString());
/* 277 */     pln();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pOlnI(Object paramObject) throws IOException {
/* 287 */     pO(paramObject.toString());
/* 288 */     pln();
/* 289 */     pI();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\IndentingWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */