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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StackFrame
/*    */ {
/*    */   public static final int LINE_NUMBER_UNKNOWN = -1;
/*    */   public static final int LINE_NUMBER_COMPILED = -2;
/*    */   public static final int LINE_NUMBER_NATIVE = -3;
/*    */   private String methodName;
/*    */   private String methodSignature;
/*    */   private String className;
/*    */   private String sourceFileName;
/*    */   private int lineNumber;
/*    */   
/*    */   public StackFrame(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt) {
/* 63 */     this.methodName = paramString1;
/* 64 */     this.methodSignature = paramString2;
/* 65 */     this.className = paramString3;
/* 66 */     this.sourceFileName = paramString4;
/* 67 */     this.lineNumber = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public void resolve(Snapshot paramSnapshot) {}
/*    */   
/*    */   public String getMethodName() {
/* 74 */     return this.methodName;
/*    */   }
/*    */   
/*    */   public String getMethodSignature() {
/* 78 */     return this.methodSignature;
/*    */   }
/*    */   
/*    */   public String getClassName() {
/* 82 */     return this.className;
/*    */   }
/*    */   
/*    */   public String getSourceFileName() {
/* 86 */     return this.sourceFileName;
/*    */   }
/*    */   
/*    */   public String getLineNumber() {
/* 90 */     switch (this.lineNumber) {
/*    */       case -1:
/* 92 */         return "(unknown)";
/*    */       case -2:
/* 94 */         return "(compiled method)";
/*    */       case -3:
/* 96 */         return "(native method)";
/*    */     } 
/* 98 */     return Integer.toString(this.lineNumber, 10);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\StackFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */