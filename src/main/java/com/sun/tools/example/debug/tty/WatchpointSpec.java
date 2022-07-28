/*    */ package com.sun.tools.example.debug.tty;
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
/*    */ abstract class WatchpointSpec
/*    */   extends EventRequestSpec
/*    */ {
/*    */   final String fieldId;
/*    */   
/*    */   WatchpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, String paramString) throws MalformedMemberNameException {
/* 42 */     super(paramReferenceTypeSpec);
/* 43 */     this.fieldId = paramString;
/* 44 */     if (!isJavaIdentifier(paramString)) {
/* 45 */       throw new MalformedMemberNameException(paramString);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     return this.refSpec.hashCode() + this.fieldId.hashCode() + 
/* 52 */       getClass().hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 57 */     if (paramObject instanceof WatchpointSpec) {
/* 58 */       WatchpointSpec watchpointSpec = (WatchpointSpec)paramObject;
/*    */       
/* 60 */       return (this.fieldId.equals(watchpointSpec.fieldId) && this.refSpec
/* 61 */         .equals(watchpointSpec.refSpec) && 
/* 62 */         getClass().equals(watchpointSpec.getClass()));
/*    */     } 
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   String errorMessageFor(Exception paramException) {
/* 70 */     if (paramException instanceof NoSuchFieldException) {
/* 71 */       return MessageOutput.format("No field in", new Object[] { this.fieldId, this.refSpec
/* 72 */             .toString() });
/*    */     }
/* 74 */     return super.errorMessageFor(paramException);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\WatchpointSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */