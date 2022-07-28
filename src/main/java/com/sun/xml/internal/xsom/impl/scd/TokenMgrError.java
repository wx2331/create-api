/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */   
/*     */   protected static final String addEscapes(String str) {
/*  66 */     StringBuffer retval = new StringBuffer();
/*     */     
/*  68 */     for (int i = 0; i < str.length(); i++) {
/*  69 */       char ch; switch (str.charAt(i)) {
/*     */         case '\000':
/*     */           break;
/*     */         
/*     */         case '\b':
/*  74 */           retval.append("\\b");
/*     */           break;
/*     */         case '\t':
/*  77 */           retval.append("\\t");
/*     */           break;
/*     */         case '\n':
/*  80 */           retval.append("\\n");
/*     */           break;
/*     */         case '\f':
/*  83 */           retval.append("\\f");
/*     */           break;
/*     */         case '\r':
/*  86 */           retval.append("\\r");
/*     */           break;
/*     */         case '"':
/*  89 */           retval.append("\\\"");
/*     */           break;
/*     */         case '\'':
/*  92 */           retval.append("\\'");
/*     */           break;
/*     */         case '\\':
/*  95 */           retval.append("\\\\");
/*     */           break;
/*     */         default:
/*  98 */           if ((ch = str.charAt(i)) < ' ' || ch > '~') {
/*  99 */             String s = "0000" + Integer.toString(ch, 16);
/* 100 */             retval.append("\\u" + s.substring(s.length() - 4, s.length())); break;
/*     */           } 
/* 102 */           retval.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 107 */     return retval.toString();
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
/*     */   protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar) {
/* 123 */     return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : ("\"" + 
/*     */ 
/*     */       
/* 126 */       addEscapes(String.valueOf(curChar)) + "\"" + " (" + curChar + "), ")) + "after : \"" + 
/* 127 */       addEscapes(errorAfter) + "\"";
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
/*     */   public String getMessage() {
/* 140 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrError() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrError(String message, int reason) {
/* 151 */     super(message);
/* 152 */     this.errorCode = reason;
/*     */   }
/*     */   
/*     */   public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
/* 156 */     this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\TokenMgrError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */