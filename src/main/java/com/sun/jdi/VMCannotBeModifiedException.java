/*    */ package com.sun.jdi;
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
/*    */ @Exported
/*    */ public class VMCannotBeModifiedException
/*    */   extends UnsupportedOperationException
/*    */ {
/*    */   private static final long serialVersionUID = -4063879815130164009L;
/*    */   
/*    */   public VMCannotBeModifiedException() {}
/*    */   
/*    */   public VMCannotBeModifiedException(String paramString) {
/* 43 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\VMCannotBeModifiedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */