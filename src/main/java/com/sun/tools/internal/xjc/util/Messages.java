/*    */ package com.sun.tools.internal.xjc.util;
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
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_CLASSNAME_COLLISION = "CodeModelClassFactory.ClassNameCollision";
/*    */   static final String ERR_CLASSNAME_COLLISION_SOURCE = "CodeModelClassFactory.ClassNameCollision.Source";
/*    */   static final String ERR_INVALID_CLASSNAME = "ERR_INVALID_CLASSNAME";
/*    */   static final String ERR_CASE_SENSITIVITY_COLLISION = "CodeModelClassFactory.CaseSensitivityCollision";
/*    */   static final String ERR_CHAMELEON_SCHEMA_GONE_WILD = "ERR_CHAMELEON_SCHEMA_GONE_WILD";
/*    */   
/*    */   static String format(String property, Object... args) {
/* 38 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 39 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */