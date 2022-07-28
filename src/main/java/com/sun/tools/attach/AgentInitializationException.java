/*    */ package com.sun.tools.attach;
/*    */ 
/*    */ import jdk.Exported;
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
/*    */ @Exported
/*    */ public class AgentInitializationException
/*    */   extends Exception
/*    */ {
/*    */   static final long serialVersionUID = -1508756333332806353L;
/*    */   private int returnValue;
/*    */   
/*    */   public AgentInitializationException() {
/* 57 */     this.returnValue = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AgentInitializationException(String paramString) {
/* 67 */     super(paramString);
/* 68 */     this.returnValue = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AgentInitializationException(String paramString, int paramInt) {
/* 80 */     super(paramString);
/* 81 */     this.returnValue = paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int returnValue() {
/* 92 */     return this.returnValue;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\attach\AgentInitializationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */