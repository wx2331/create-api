/*     */ package sun.jvmstat.perfdata.monitor;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AliasFileParser
/*     */ {
/*     */   private static final String ALIAS = "alias";
/*     */   private static final boolean DEBUG = false;
/*     */   private URL inputfile;
/*     */   private StreamTokenizer st;
/*     */   private Token currentToken;
/*     */   
/*     */   AliasFileParser(URL paramURL) {
/*  53 */     this.inputfile = paramURL;
/*     */   }
/*     */   
/*     */   private class Token
/*     */   {
/*     */     public String sval;
/*     */     public int ttype;
/*     */     
/*     */     public Token(int param1Int, String param1String) {
/*  62 */       this.ttype = param1Int;
/*  63 */       this.sval = param1String;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logln(String paramString) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextToken() throws IOException {
/*  77 */     this.st.nextToken();
/*  78 */     this.currentToken = new Token(this.st.ttype, this.st.sval);
/*     */     
/*  80 */     logln("Read token: type = " + this.currentToken.ttype + " string = " + this.currentToken.sval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void match(int paramInt, String paramString) throws IOException, SyntaxException {
/*  91 */     if (this.currentToken.ttype == paramInt && this.currentToken.sval
/*  92 */       .compareTo(paramString) == 0) {
/*  93 */       logln("matched type: " + paramInt + " and token = " + this.currentToken.sval);
/*     */       
/*  95 */       nextToken();
/*     */     } else {
/*  97 */       throw new SyntaxException(this.st.lineno());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void match(int paramInt) throws IOException, SyntaxException {
/* 107 */     if (this.currentToken.ttype == paramInt) {
/* 108 */       logln("matched type: " + paramInt + ", token = " + this.currentToken.sval);
/* 109 */       nextToken();
/*     */     } else {
/* 111 */       throw new SyntaxException(this.st.lineno());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void match(String paramString) throws IOException, SyntaxException {
/* 116 */     match(-3, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(Map<String, ArrayList<String>> paramMap) throws SyntaxException, IOException {
/* 124 */     if (this.inputfile == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 129 */     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputfile.openStream()));
/* 130 */     this.st = new StreamTokenizer(bufferedReader);
/*     */ 
/*     */     
/* 133 */     this.st.slashSlashComments(true);
/* 134 */     this.st.slashStarComments(true);
/* 135 */     this.st.wordChars(95, 95);
/*     */     
/* 137 */     nextToken();
/*     */     
/* 139 */     while (this.currentToken.ttype != -1) {
/*     */       
/* 141 */       if (this.currentToken.ttype != -3 || this.currentToken.sval
/* 142 */         .compareTo("alias") != 0) {
/* 143 */         nextToken();
/*     */         
/*     */         continue;
/*     */       } 
/* 147 */       match("alias");
/* 148 */       String str = this.currentToken.sval;
/* 149 */       match(-3);
/*     */       
/* 151 */       ArrayList<String> arrayList = new ArrayList();
/*     */       
/*     */       do {
/* 154 */         arrayList.add(this.currentToken.sval);
/* 155 */         match(-3);
/*     */       }
/* 157 */       while (this.currentToken.ttype != -1 && this.currentToken.sval
/* 158 */         .compareTo("alias") != 0);
/*     */       
/* 160 */       logln("adding map entry for " + str + " values = " + arrayList);
/*     */       
/* 162 */       paramMap.put(str, arrayList);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\AliasFileParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */