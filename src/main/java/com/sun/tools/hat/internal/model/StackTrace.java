/*    */ package com.sun.tools.hat.internal.model;
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
/*    */ public class StackTrace
/*    */ {
/*    */   private StackFrame[] frames;
/*    */   
/*    */   public StackTrace(StackFrame[] paramArrayOfStackFrame) {
/* 50 */     this.frames = paramArrayOfStackFrame;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StackTrace traceForDepth(int paramInt) {
/* 59 */     if (paramInt >= this.frames.length) {
/* 60 */       return this;
/*    */     }
/* 62 */     StackFrame[] arrayOfStackFrame = new StackFrame[paramInt];
/* 63 */     System.arraycopy(this.frames, 0, arrayOfStackFrame, 0, paramInt);
/* 64 */     return new StackTrace(arrayOfStackFrame);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resolve(Snapshot paramSnapshot) {
/* 69 */     for (byte b = 0; b < this.frames.length; b++) {
/* 70 */       this.frames[b].resolve(paramSnapshot);
/*    */     }
/*    */   }
/*    */   
/*    */   public StackFrame[] getFrames() {
/* 75 */     return this.frames;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\StackTrace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */