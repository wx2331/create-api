/*    */ package com.sun.tools.doclets.formats.html.markup;
/*    */ 
/*    */ import com.sun.tools.javac.util.StringUtils;
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
/*    */ public enum HtmlAttr
/*    */ {
/* 41 */   ALT,
/* 42 */   BORDER,
/* 43 */   CELLPADDING,
/* 44 */   CELLSPACING,
/* 45 */   CLASS,
/* 46 */   CLEAR,
/* 47 */   COLS,
/* 48 */   CONTENT,
/* 49 */   HREF,
/* 50 */   HTTP_EQUIV("http-equiv"),
/* 51 */   ID,
/* 52 */   LANG,
/* 53 */   NAME,
/* 54 */   ONLOAD,
/* 55 */   REL,
/* 56 */   ROWS,
/* 57 */   SCOPE,
/* 58 */   SCROLLING,
/* 59 */   SRC,
/* 60 */   SUMMARY,
/* 61 */   TARGET,
/* 62 */   TITLE,
/* 63 */   TYPE,
/* 64 */   WIDTH;
/*    */   
/*    */   private final String value;
/*    */   
/*    */   HtmlAttr() {
/* 69 */     this.value = StringUtils.toLowerCase(name());
/*    */   }
/*    */   
/*    */   HtmlAttr(String paramString1) {
/* 73 */     this.value = paramString1;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 77 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\HtmlAttr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */