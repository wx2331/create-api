/*     */ package com.sun.tools.example.debug.expr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   private static final long serialVersionUID = 7978489144303647901L;
/*     */   protected boolean specialConstructor;
/*     */   public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public String[] tokenImage;
/*     */   protected String eol;
/*     */   
/*     */   public ParseException(Token paramToken, int[][] paramArrayOfint, String[] paramArrayOfString) {
/*  68 */     super("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     this.eol = System.getProperty("line.separator", "\n"); this.specialConstructor = true; this.currentToken = paramToken; this.expectedTokenSequences = paramArrayOfint; this.tokenImage = paramArrayOfString; } public ParseException() { this.eol = System.getProperty("line.separator", "\n"); this.specialConstructor = false; } public ParseException(String paramString) { super(paramString); this.eol = System.getProperty("line.separator", "\n");
/*     */     this.specialConstructor = false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String add_escapes(String paramString) {
/* 186 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/* 188 */     for (byte b = 0; b < paramString.length(); b++) {
/* 189 */       char c; switch (paramString.charAt(b)) {
/*     */         case '\000':
/*     */           break;
/*     */         
/*     */         case '\b':
/* 194 */           stringBuffer.append("\\b");
/*     */           break;
/*     */         case '\t':
/* 197 */           stringBuffer.append("\\t");
/*     */           break;
/*     */         case '\n':
/* 200 */           stringBuffer.append("\\n");
/*     */           break;
/*     */         case '\f':
/* 203 */           stringBuffer.append("\\f");
/*     */           break;
/*     */         case '\r':
/* 206 */           stringBuffer.append("\\r");
/*     */           break;
/*     */         case '"':
/* 209 */           stringBuffer.append("\\\"");
/*     */           break;
/*     */         case '\'':
/* 212 */           stringBuffer.append("\\'");
/*     */           break;
/*     */         case '\\':
/* 215 */           stringBuffer.append("\\\\");
/*     */           break;
/*     */         default:
/* 218 */           if ((c = paramString.charAt(b)) < ' ' || c > '~') {
/* 219 */             String str = "0000" + Integer.toString(c, 16);
/* 220 */             stringBuffer.append("\\u" + str.substring(str.length() - 4, str.length())); break;
/*     */           } 
/* 222 */           stringBuffer.append(c);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 227 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*     */     if (!this.specialConstructor)
/*     */       return super.getMessage(); 
/*     */     String str1 = "";
/*     */     int i = 0;
/*     */     for (int[] arrayOfInt : this.expectedTokenSequences) {
/*     */       if (i < arrayOfInt.length)
/*     */         i = arrayOfInt.length; 
/*     */       for (byte b1 = 0; b1 < arrayOfInt.length; b1++)
/*     */         str1 = str1 + this.tokenImage[arrayOfInt[b1]] + " "; 
/*     */       if (arrayOfInt[arrayOfInt.length - 1] != 0)
/*     */         str1 = str1 + "..."; 
/*     */       str1 = str1 + this.eol + "    ";
/*     */     } 
/*     */     String str2 = "Encountered \"";
/*     */     Token token = this.currentToken.next;
/*     */     for (byte b = 0; b < i; b++) {
/*     */       if (b != 0)
/*     */         str2 = str2 + " "; 
/*     */       if (token.kind == 0) {
/*     */         str2 = str2 + this.tokenImage[0];
/*     */         break;
/*     */       } 
/*     */       str2 = str2 + add_escapes(token.image);
/*     */       token = token.next;
/*     */     } 
/*     */     str2 = str2 + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn + "." + this.eol;
/*     */     if (this.expectedTokenSequences.length == 1) {
/*     */       str2 = str2 + "Was expecting:" + this.eol + "    ";
/*     */     } else {
/*     */       str2 = str2 + "Was expecting one of:" + this.eol + "    ";
/*     */     } 
/*     */     str2 = str2 + str1;
/*     */     return str2;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\expr\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */