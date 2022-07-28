/*    */ package com.sun.tools.internal.jxc;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
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
/*    */ enum Messages
/*    */ {
/* 38 */   UNEXPECTED_NGCC_TOKEN,
/* 39 */   BASEDIR_DOESNT_EXIST,
/* 40 */   USAGE,
/* 41 */   FULLVERSION,
/* 42 */   VERSION;
/*    */   
/*    */   static {
/* 45 */     rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */   }
/*    */   private static final ResourceBundle rb;
/*    */   public String toString() {
/* 49 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 53 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */