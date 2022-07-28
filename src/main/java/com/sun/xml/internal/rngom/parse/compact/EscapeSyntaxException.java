/*    */ package com.sun.xml.internal.rngom.parse.compact;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class EscapeSyntaxException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final String key;
/*    */   private final int lineNumber;
/*    */   private final int columnNumber;
/*    */   
/*    */   EscapeSyntaxException(String key, int lineNumber, int columnNumber) {
/* 54 */     this.key = key;
/* 55 */     this.lineNumber = lineNumber;
/* 56 */     this.columnNumber = columnNumber;
/*    */   }
/*    */   
/*    */   String getKey() {
/* 60 */     return this.key;
/*    */   }
/*    */   
/*    */   int getLineNumber() {
/* 64 */     return this.lineNumber;
/*    */   }
/*    */   
/*    */   int getColumnNumber() {
/* 68 */     return this.columnNumber;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\compact\EscapeSyntaxException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */