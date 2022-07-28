/*    */ package com.sun.tools.internal.jxc.ap;
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
/*    */ public enum Const
/*    */ {
/* 43 */   CONFIG_FILE_OPTION("jaxb.config"),
/*    */   
/* 45 */   DEBUG_OPTION("jaxb.debug");
/*    */   
/*    */   private String value;
/*    */   
/*    */   Const(String value) {
/* 50 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 54 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\ap\Const.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */