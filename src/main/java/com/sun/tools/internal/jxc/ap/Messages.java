/*    */ package com.sun.tools.internal.jxc.ap;
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
/*    */ enum Messages
/*    */ {
/* 38 */   NON_EXISTENT_FILE,
/* 39 */   UNRECOGNIZED_PARAMETER,
/* 40 */   OPERAND_MISSING;
/*    */
/*    */   private static final ResourceBundle rb;
/*    */   static {
/* 43 */     rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */   }
/*    */   public String toString() {
/* 46 */     return format(new Object[0]);
/*    */   }
/*    */
/*    */   public String format(Object... args) {
/* 50 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\ap\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
