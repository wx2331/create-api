/*     */ package com.sun.tools.javac.parser;
/*     */ 
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Scanner
/*     */   implements Lexer
/*     */ {
/*     */   private Tokens tokens;
/*     */   private Tokens.Token token;
/*     */   private Tokens.Token prevToken;
/*  59 */   private List<Tokens.Token> savedTokens = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JavaTokenizer tokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Scanner(ScannerFactory paramScannerFactory, CharBuffer paramCharBuffer) {
/*  74 */     this(paramScannerFactory, new JavaTokenizer(paramScannerFactory, paramCharBuffer));
/*     */   }
/*     */   
/*     */   protected Scanner(ScannerFactory paramScannerFactory, char[] paramArrayOfchar, int paramInt) {
/*  78 */     this(paramScannerFactory, new JavaTokenizer(paramScannerFactory, paramArrayOfchar, paramInt));
/*     */   }
/*     */   
/*     */   protected Scanner(ScannerFactory paramScannerFactory, JavaTokenizer paramJavaTokenizer) {
/*  82 */     this.tokenizer = paramJavaTokenizer;
/*  83 */     this.tokens = paramScannerFactory.tokens;
/*  84 */     this.token = this.prevToken = Tokens.DUMMY;
/*     */   }
/*     */   
/*     */   public Tokens.Token token() {
/*  88 */     return token(0);
/*     */   }
/*     */   
/*     */   public Tokens.Token token(int paramInt) {
/*  92 */     if (paramInt == 0) {
/*  93 */       return this.token;
/*     */     }
/*  95 */     ensureLookahead(paramInt);
/*  96 */     return this.savedTokens.get(paramInt - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void ensureLookahead(int paramInt) {
/* 101 */     for (int i = this.savedTokens.size(); i < paramInt; i++) {
/* 102 */       this.savedTokens.add(this.tokenizer.readToken());
/*     */     }
/*     */   }
/*     */   
/*     */   public Tokens.Token prevToken() {
/* 107 */     return this.prevToken;
/*     */   }
/*     */   
/*     */   public void nextToken() {
/* 111 */     this.prevToken = this.token;
/* 112 */     if (!this.savedTokens.isEmpty()) {
/* 113 */       this.token = this.savedTokens.remove(0);
/*     */     } else {
/* 115 */       this.token = this.tokenizer.readToken();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tokens.Token split() {
/* 120 */     Tokens.Token[] arrayOfToken = this.token.split(this.tokens);
/* 121 */     this.prevToken = arrayOfToken[0];
/* 122 */     this.token = arrayOfToken[1];
/* 123 */     return this.token;
/*     */   }
/*     */   
/*     */   public Position.LineMap getLineMap() {
/* 127 */     return this.tokenizer.getLineMap();
/*     */   }
/*     */   
/*     */   public int errPos() {
/* 131 */     return this.tokenizer.errPos();
/*     */   }
/*     */   
/*     */   public void errPos(int paramInt) {
/* 135 */     this.tokenizer.errPos(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\parser\Scanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */