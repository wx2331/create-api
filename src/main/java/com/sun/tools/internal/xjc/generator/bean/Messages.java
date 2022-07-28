/*    */ package com.sun.tools.internal.xjc.generator.bean;
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
/*    */ enum Messages
/*    */ {
/*    */   private static final ResourceBundle rb;
/* 36 */   METHOD_COLLISION,
/* 37 */   ERR_UNUSABLE_NAME,
/* 38 */   ERR_KEYNAME_COLLISION,
/* 39 */   ERR_NAME_COLLISION,
/* 40 */   ILLEGAL_CONSTRUCTOR_PARAM,
/* 41 */   OBJECT_FACTORY_CONFLICT,
/* 42 */   OBJECT_FACTORY_CONFLICT_RELATED;
/*    */   
/*    */   static {
/* 45 */     rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */   }
/*    */   public String toString() {
/* 48 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 52 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */