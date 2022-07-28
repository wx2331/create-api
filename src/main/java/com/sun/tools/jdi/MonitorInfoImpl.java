/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.InternalException;
/*    */ import com.sun.jdi.InvalidStackFrameException;
/*    */ import com.sun.jdi.MonitorInfo;
/*    */ import com.sun.jdi.ObjectReference;
/*    */ import com.sun.jdi.ThreadReference;
/*    */ import com.sun.jdi.VirtualMachine;
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
/*    */ public class MonitorInfoImpl
/*    */   extends MirrorImpl
/*    */   implements MonitorInfo, ThreadListener
/*    */ {
/*    */   private boolean isValid = true;
/*    */   ObjectReference monitor;
/*    */   ThreadReference thread;
/*    */   int stack_depth;
/*    */   
/*    */   MonitorInfoImpl(VirtualMachine paramVirtualMachine, ObjectReference paramObjectReference, ThreadReferenceImpl paramThreadReferenceImpl, int paramInt) {
/* 44 */     super(paramVirtualMachine);
/* 45 */     this.monitor = paramObjectReference;
/* 46 */     this.thread = paramThreadReferenceImpl;
/* 47 */     this.stack_depth = paramInt;
/* 48 */     paramThreadReferenceImpl.addListener(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean threadResumable(ThreadAction paramThreadAction) {
/* 58 */     synchronized (this.vm.state()) {
/* 59 */       if (this.isValid) {
/* 60 */         this.isValid = false;
/* 61 */         return false;
/*    */       } 
/* 63 */       throw new InternalException("Invalid stack frame thread listener");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void validateMonitorInfo() {
/* 70 */     if (!this.isValid) {
/* 71 */       throw new InvalidStackFrameException("Thread has been resumed");
/*    */     }
/*    */   }
/*    */   
/*    */   public ObjectReference monitor() {
/* 76 */     validateMonitorInfo();
/* 77 */     return this.monitor;
/*    */   }
/*    */   
/*    */   public int stackDepth() {
/* 81 */     validateMonitorInfo();
/* 82 */     return this.stack_depth;
/*    */   }
/*    */   
/*    */   public ThreadReference thread() {
/* 86 */     validateMonitorInfo();
/* 87 */     return this.thread;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\MonitorInfoImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */