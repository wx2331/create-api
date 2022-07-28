/*    */ package com.sun.jdi.connect;
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
/*    */ @Exported
/*    */ public class VMStartException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 6408644824640801020L;
/*    */   Process process;
/*    */   
/*    */   public VMStartException(Process paramProcess) {
/* 45 */     this.process = paramProcess;
/*    */   }
/*    */ 
/*    */   
/*    */   public VMStartException(String paramString, Process paramProcess) {
/* 50 */     super(paramString);
/* 51 */     this.process = paramProcess;
/*    */   }
/*    */   
/*    */   public Process process() {
/* 55 */     return this.process;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\VMStartException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */