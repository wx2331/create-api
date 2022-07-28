/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
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
/*    */ final class GWildcardElement
/*    */   extends GElement
/*    */ {
/*    */   private boolean strict = true;
/*    */   
/*    */   public String toString() {
/* 46 */     return "#any";
/*    */   }
/*    */   
/*    */   String getPropertyNameSeed() {
/* 50 */     return "any";
/*    */   }
/*    */   
/*    */   public void merge(XSWildcard wc) {
/* 54 */     switch (wc.getMode()) {
/*    */       case 1:
/*    */       case 3:
/* 57 */         this.strict = false;
/*    */         break;
/*    */     } 
/*    */   }
/*    */   public boolean isStrict() {
/* 62 */     return this.strict;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\GWildcardElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */