/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
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
/*    */ public enum Messages
/*    */ {
/*    */   private static final ResourceBundle rb;
/* 36 */   DEFAULT_GETTER_JAVADOC,
/* 37 */   DEFAULT_SETTER_JAVADOC;
/*    */   
/*    */   static {
/* 40 */     rb = ResourceBundle.getBundle(Messages.class.getName().substring(0, Messages.class.getName().lastIndexOf('.')) + ".MessageBundle");
/*    */   }
/*    */   public String toString() {
/* 43 */     return format(new Object[0]);
/*    */   }
/*    */   
/*    */   public String format(Object... args) {
/* 47 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */