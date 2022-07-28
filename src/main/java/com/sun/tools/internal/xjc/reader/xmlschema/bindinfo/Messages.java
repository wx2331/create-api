/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
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
/*    */ enum Messages
/*    */ {
/* 36 */   ERR_CANNOT_BE_BOUND_TO_SIMPLETYPE,
/* 37 */   ERR_UNDEFINED_SIMPLE_TYPE,
/* 38 */   ERR_ILLEGAL_FIXEDATTR;
/*    */ 
/*    */ 
/*    */   
/*    */   String format(Object... args) {
/* 43 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(name());
/* 44 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */