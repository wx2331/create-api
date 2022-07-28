/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import com.sun.jdi.request.ExceptionRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ExceptionSpec
/*     */   extends EventRequestSpec
/*     */ {
/*     */   private boolean notifyCaught;
/*     */   private boolean notifyUncaught;
/*     */   
/*     */   private ExceptionSpec(ReferenceTypeSpec paramReferenceTypeSpec) {
/*  45 */     this(paramReferenceTypeSpec, true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ExceptionSpec(ReferenceTypeSpec paramReferenceTypeSpec, boolean paramBoolean1, boolean paramBoolean2) {
/*  51 */     super(paramReferenceTypeSpec);
/*  52 */     this.notifyCaught = paramBoolean1;
/*  53 */     this.notifyUncaught = paramBoolean2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventRequest resolveEventRequest(ReferenceType paramReferenceType) {
/*  61 */     EventRequestManager eventRequestManager = paramReferenceType.virtualMachine().eventRequestManager();
/*  62 */     ExceptionRequest exceptionRequest = eventRequestManager.createExceptionRequest(paramReferenceType, this.notifyCaught, this.notifyUncaught);
/*     */ 
/*     */     
/*  65 */     exceptionRequest.enable();
/*  66 */     return (EventRequest)exceptionRequest;
/*     */   }
/*     */   
/*     */   public boolean notifyCaught() {
/*  70 */     return this.notifyCaught;
/*     */   }
/*     */   
/*     */   public boolean notifyUncaught() {
/*  74 */     return this.notifyUncaught;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  80 */     int i = 17;
/*  81 */     i = 37 * i + (notifyCaught() ? 0 : 1);
/*  82 */     i = 37 * i + (notifyUncaught() ? 0 : 1);
/*  83 */     i = 37 * i + this.refSpec.hashCode();
/*  84 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  89 */     if (paramObject instanceof ExceptionSpec) {
/*  90 */       ExceptionSpec exceptionSpec = (ExceptionSpec)paramObject;
/*     */       
/*  92 */       if (this.refSpec.equals(exceptionSpec.refSpec) && 
/*  93 */         notifyCaught() == exceptionSpec.notifyCaught() && 
/*  94 */         notifyUncaught() == exceptionSpec.notifyUncaught()) {
/*  95 */         return true;
/*     */       }
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     String str;
/* 104 */     if (this.notifyCaught && !this.notifyUncaught) {
/* 105 */       str = MessageOutput.format("exceptionSpec caught", this.refSpec
/* 106 */           .toString());
/* 107 */     } else if (this.notifyUncaught && !this.notifyCaught) {
/* 108 */       str = MessageOutput.format("exceptionSpec uncaught", this.refSpec
/* 109 */           .toString());
/*     */     } else {
/* 111 */       str = MessageOutput.format("exceptionSpec all", this.refSpec
/* 112 */           .toString());
/*     */     } 
/* 114 */     return str;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\ExceptionSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */