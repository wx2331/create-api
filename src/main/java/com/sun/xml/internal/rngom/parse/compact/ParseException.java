/*     */ package com.sun.xml.internal.rngom.parse.compact;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParseException
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public String[] tokenImage;
/*     */   protected String eol;
/*     */   
/*     */   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
/*  79 */     super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     this.eol = System.getProperty("line.separator", "\n"); this.currentToken = currentTokenVal; this.expectedTokenSequences = expectedTokenSequencesVal; this.tokenImage = tokenImageVal; } public ParseException() { this.eol = System.getProperty("line.separator", "\n"); } public ParseException(String message) { super(message); this.eol = System.getProperty("line.separator", "\n"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String add_escapes(String str) {
/* 187 */     StringBuffer retval = new StringBuffer();
/*     */     
/* 189 */     for (int i = 0; i < str.length(); i++) {
/* 190 */       char ch; switch (str.charAt(i)) {
/*     */         case '\000':
/*     */           break;
/*     */         
/*     */         case '\b':
/* 195 */           retval.append("\\b");
/*     */           break;
/*     */         case '\t':
/* 198 */           retval.append("\\t");
/*     */           break;
/*     */         case '\n':
/* 201 */           retval.append("\\n");
/*     */           break;
/*     */         case '\f':
/* 204 */           retval.append("\\f");
/*     */           break;
/*     */         case '\r':
/* 207 */           retval.append("\\r");
/*     */           break;
/*     */         case '"':
/* 210 */           retval.append("\\\"");
/*     */           break;
/*     */         case '\'':
/* 213 */           retval.append("\\'");
/*     */           break;
/*     */         case '\\':
/* 216 */           retval.append("\\\\");
/*     */           break;
/*     */         default:
/* 219 */           if ((ch = str.charAt(i)) < ' ' || ch > '~') {
/* 220 */             String s = "0000" + Integer.toString(ch, 16);
/* 221 */             retval.append("\\u" + s.substring(s.length() - 4, s.length())); break;
/*     */           } 
/* 223 */           retval.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 228 */     return retval.toString();
/*     */   }
/*     */   
/*     */   private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage) {
/*     */     String eol = System.getProperty("line.separator", "\n");
/*     */     StringBuffer expected = new StringBuffer();
/*     */     int maxSize = 0;
/*     */     for (int i = 0; i < expectedTokenSequences.length; i++) {
/*     */       if (maxSize < (expectedTokenSequences[i]).length)
/*     */         maxSize = (expectedTokenSequences[i]).length; 
/*     */       for (int k = 0; k < (expectedTokenSequences[i]).length; k++)
/*     */         expected.append(tokenImage[expectedTokenSequences[i][k]]).append(' '); 
/*     */       if (expectedTokenSequences[i][(expectedTokenSequences[i]).length - 1] != 0)
/*     */         expected.append("..."); 
/*     */       expected.append(eol).append("    ");
/*     */     } 
/*     */     String retval = "Encountered \"";
/*     */     Token tok = currentToken.next;
/*     */     for (int j = 0; j < maxSize; j++) {
/*     */       if (j != 0)
/*     */         retval = retval + " "; 
/*     */       if (tok.kind == 0) {
/*     */         retval = retval + tokenImage[0];
/*     */         break;
/*     */       } 
/*     */       retval = retval + " " + tokenImage[tok.kind];
/*     */       retval = retval + " \"";
/*     */       retval = retval + add_escapes(tok.image);
/*     */       retval = retval + " \"";
/*     */       tok = tok.next;
/*     */     } 
/*     */     retval = retval + "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn;
/*     */     retval = retval + "." + eol;
/*     */     if (expectedTokenSequences.length == 1) {
/*     */       retval = retval + "Was expecting:" + eol + "    ";
/*     */     } else {
/*     */       retval = retval + "Was expecting one of:" + eol + "    ";
/*     */     } 
/*     */     retval = retval + expected.toString();
/*     */     return retval;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */