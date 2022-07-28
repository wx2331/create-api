/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ThreadReference;
/*    */ import java.util.EventObject;
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
/*    */ class ThreadAction
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 5690763191100515283L;
/*    */   static final int THREAD_RESUMABLE = 2;
/*    */   int id;
/*    */   
/*    */   ThreadAction(ThreadReference paramThreadReference, int paramInt) {
/* 44 */     super(paramThreadReference);
/* 45 */     this.id = paramInt;
/*    */   }
/*    */   ThreadReference thread() {
/* 48 */     return (ThreadReference)getSource();
/*    */   }
/*    */   int id() {
/* 51 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ThreadAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */