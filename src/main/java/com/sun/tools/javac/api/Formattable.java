/*    */ package com.sun.tools.javac.api;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ 
/*    */ 
/*    */ public interface Formattable
/*    */ {
/*    */   String toString(Locale paramLocale, Messages paramMessages);
/*    */   
/*    */   String getKind();
/*    */   
/*    */   public static class LocalizedString
/*    */     implements Formattable
/*    */   {
/*    */     String key;
/*    */     
/*    */     public LocalizedString(String param1String) {
/* 62 */       this.key = param1String;
/*    */     }
/*    */     
/*    */     public String toString(Locale param1Locale, Messages param1Messages) {
/* 66 */       return param1Messages.getLocalizedString(param1Locale, this.key, new Object[0]);
/*    */     }
/*    */     public String getKind() {
/* 69 */       return "LocalizedString";
/*    */     }
/*    */     
/*    */     public String toString() {
/* 73 */       return this.key;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\Formattable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */