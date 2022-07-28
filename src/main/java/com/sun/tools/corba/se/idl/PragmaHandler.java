/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PragmaHandler
/*     */ {
/*     */   public abstract boolean process(String paramString1, String paramString2) throws IOException;
/*     */   
/*     */   void init(Preprocessor paramPreprocessor) {
/*  49 */     this.preprocessor = paramPreprocessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String currentToken() {
/*  57 */     return this.preprocessor.currentToken();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SymtabEntry getEntryForName(String paramString) {
/*  66 */     return this.preprocessor.getEntryForName(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getStringToEOL() throws IOException {
/*  74 */     return this.preprocessor.getStringToEOL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUntil(char paramChar) throws IOException {
/*  85 */     return this.preprocessor.getUntil(paramChar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String nextToken() throws IOException {
/*  91 */     return this.preprocessor.nextToken();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SymtabEntry scopedName() throws IOException {
/* 100 */     return this.preprocessor.scopedName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void skipToEOL() throws IOException {
/* 106 */     this.preprocessor.skipToEOL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String skipUntil(char paramChar) throws IOException {
/* 113 */     return this.preprocessor.skipUntil(paramChar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseException(String paramString) {
/* 120 */     this.preprocessor.parseException(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openScope(SymtabEntry paramSymtabEntry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeScope(SymtabEntry paramSymtabEntry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   private Preprocessor preprocessor = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\PragmaHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */