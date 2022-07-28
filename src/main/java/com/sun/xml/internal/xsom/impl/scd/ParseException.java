/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
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
/*     */ public class ParseException
/*     */   extends Exception
/*     */ {
/*     */   protected boolean specialConstructor;
/*     */   public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public List<String> tokenImage;
/*     */   protected String eol;
/*     */   
/*     */   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, List<String> tokenImageVal) {
/*  59 */     super("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     this.eol = System.getProperty("line.separator", "\n"); this.specialConstructor = true; this.currentToken = currentTokenVal; this.expectedTokenSequences = expectedTokenSequencesVal; this.tokenImage = tokenImageVal; } public ParseException() { this.eol = System.getProperty("line.separator", "\n"); this.specialConstructor = false; } public ParseException(String message) { super(message); this.eol = System.getProperty("line.separator", "\n");
/*     */     this.specialConstructor = false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String add_escapes(String str) {
/* 175 */     StringBuffer retval = new StringBuffer();
/*     */     
/* 177 */     for (int i = 0; i < str.length(); i++) {
/* 178 */       char ch; switch (str.charAt(i)) {
/*     */         case '\000':
/*     */           break;
/*     */         
/*     */         case '\b':
/* 183 */           retval.append("\\b");
/*     */           break;
/*     */         case '\t':
/* 186 */           retval.append("\\t");
/*     */           break;
/*     */         case '\n':
/* 189 */           retval.append("\\n");
/*     */           break;
/*     */         case '\f':
/* 192 */           retval.append("\\f");
/*     */           break;
/*     */         case '\r':
/* 195 */           retval.append("\\r");
/*     */           break;
/*     */         case '"':
/* 198 */           retval.append("\\\"");
/*     */           break;
/*     */         case '\'':
/* 201 */           retval.append("\\'");
/*     */           break;
/*     */         case '\\':
/* 204 */           retval.append("\\\\");
/*     */           break;
/*     */         default:
/* 207 */           if ((ch = str.charAt(i)) < ' ' || ch > '~') {
/* 208 */             String s = "0000" + Integer.toString(ch, 16);
/* 209 */             retval.append("\\u" + s.substring(s.length() - 4, s.length())); break;
/*     */           } 
/* 211 */           retval.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 216 */     return retval.toString();
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*     */     if (!this.specialConstructor)
/*     */       return super.getMessage(); 
/*     */     StringBuffer expected = new StringBuffer();
/*     */     int maxSize = 0;
/*     */     for (int i = 0; i < this.expectedTokenSequences.length; i++) {
/*     */       if (maxSize < (this.expectedTokenSequences[i]).length)
/*     */         maxSize = (this.expectedTokenSequences[i]).length; 
/*     */       for (int k = 0; k < (this.expectedTokenSequences[i]).length; k++)
/*     */         expected.append(this.tokenImage.get(this.expectedTokenSequences[i][k])).append(" "); 
/*     */       if (this.expectedTokenSequences[i][(this.expectedTokenSequences[i]).length - 1] != 0)
/*     */         expected.append("..."); 
/*     */       expected.append(this.eol).append("    ");
/*     */     } 
/*     */     String retval = "Encountered \"";
/*     */     Token tok = this.currentToken.next;
/*     */     for (int j = 0; j < maxSize; j++) {
/*     */       if (j != 0)
/*     */         retval = retval + " "; 
/*     */       if (tok.kind == 0) {
/*     */         retval = retval + (String)this.tokenImage.get(0);
/*     */         break;
/*     */       } 
/*     */       retval = retval + add_escapes(tok.image);
/*     */       tok = tok.next;
/*     */     } 
/*     */     retval = retval + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn;
/*     */     retval = retval + "." + this.eol;
/*     */     if (this.expectedTokenSequences.length == 1) {
/*     */       retval = retval + "Was expecting:" + this.eol + "    ";
/*     */     } else {
/*     */       retval = retval + "Was expecting one of:" + this.eol + "    ";
/*     */     } 
/*     */     retval = retval + expected.toString();
/*     */     return retval;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */