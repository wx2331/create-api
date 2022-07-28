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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenMgrError
/*     */   extends Error
/*     */ {
/*     */   private static final long serialVersionUID = -6236440836177601522L;
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */   
/*     */   protected static final String addEscapes(String paramString) {
/*  77 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/*  79 */     for (byte b = 0; b < paramString.length(); b++) {
/*  80 */       char c; switch (paramString.charAt(b)) {
/*     */         case '\000':
/*     */           break;
/*     */         
/*     */         case '\b':
/*  85 */           stringBuffer.append("\\b");
/*     */           break;
/*     */         case '\t':
/*  88 */           stringBuffer.append("\\t");
/*     */           break;
/*     */         case '\n':
/*  91 */           stringBuffer.append("\\n");
/*     */           break;
/*     */         case '\f':
/*  94 */           stringBuffer.append("\\f");
/*     */           break;
/*     */         case '\r':
/*  97 */           stringBuffer.append("\\r");
/*     */           break;
/*     */         case '"':
/* 100 */           stringBuffer.append("\\\"");
/*     */           break;
/*     */         case '\'':
/* 103 */           stringBuffer.append("\\'");
/*     */           break;
/*     */         case '\\':
/* 106 */           stringBuffer.append("\\\\");
/*     */           break;
/*     */         default:
/* 109 */           if ((c = paramString.charAt(b)) < ' ' || c > '~') {
/* 110 */             String str = "0000" + Integer.toString(c, 16);
/* 111 */             stringBuffer.append("\\u" + str.substring(str.length() - 4, str.length())); break;
/*     */           } 
/* 113 */           stringBuffer.append(c);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 118 */     return stringBuffer.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LexicalError(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, String paramString, char paramChar) {
/* 134 */     return "Lexical error at line " + paramInt2 + ", column " + paramInt3 + ".  Encountered: " + (paramBoolean ? "<EOF> " : ("\"" + 
/*     */ 
/*     */       
/* 137 */       addEscapes(String.valueOf(paramChar)) + "\"" + " (" + paramChar + "), ")) + "after : \"" + 
/* 138 */       addEscapes(paramString) + "\"";
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
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 152 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrError() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrError(String paramString, int paramInt) {
/* 163 */     super(paramString);
/* 164 */     this.errorCode = paramInt;
/*     */   }
/*     */   
/*     */   public TokenMgrError(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, String paramString, char paramChar, int paramInt4) {
/* 168 */     this(LexicalError(paramBoolean, paramInt1, paramInt2, paramInt3, paramString, paramChar), paramInt4);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\expr\TokenMgrError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */