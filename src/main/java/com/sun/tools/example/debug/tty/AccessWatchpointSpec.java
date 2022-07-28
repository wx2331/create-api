/*    */ package com.sun.tools.example.debug.tty;
/*    */ 
/*    */ import com.sun.jdi.Field;
/*    */ import com.sun.jdi.ReferenceType;
/*    */ import com.sun.jdi.request.AccessWatchpointRequest;
/*    */ import com.sun.jdi.request.EventRequest;
/*    */ import com.sun.jdi.request.EventRequestManager;
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
/*    */ class AccessWatchpointSpec
/*    */   extends WatchpointSpec
/*    */ {
/*    */   AccessWatchpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, String paramString) throws MalformedMemberNameException {
/* 44 */     super(paramReferenceTypeSpec, paramString);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   EventRequest resolveEventRequest(ReferenceType paramReferenceType) throws NoSuchFieldException {
/* 53 */     Field field = paramReferenceType.fieldByName(this.fieldId);
/* 54 */     EventRequestManager eventRequestManager = paramReferenceType.virtualMachine().eventRequestManager();
/* 55 */     AccessWatchpointRequest accessWatchpointRequest = eventRequestManager.createAccessWatchpointRequest(field);
/* 56 */     accessWatchpointRequest.setSuspendPolicy(this.suspendPolicy);
/* 57 */     accessWatchpointRequest.enable();
/* 58 */     return (EventRequest)accessWatchpointRequest;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return MessageOutput.format("watch accesses of", new Object[] { this.refSpec
/* 64 */           .toString(), this.fieldId });
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\AccessWatchpointSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */