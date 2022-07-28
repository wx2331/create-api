/*    */ package com.sun.tools.example.debug.tty;
/*    */ 
/*    */ import com.sun.jdi.Field;
/*    */ import com.sun.jdi.ReferenceType;
/*    */ import com.sun.jdi.request.EventRequest;
/*    */ import com.sun.jdi.request.EventRequestManager;
/*    */ import com.sun.jdi.request.ModificationWatchpointRequest;
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
/*    */ class ModificationWatchpointSpec
/*    */   extends WatchpointSpec
/*    */ {
/*    */   ModificationWatchpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, String paramString) throws MalformedMemberNameException {
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
/* 55 */     ModificationWatchpointRequest modificationWatchpointRequest = eventRequestManager.createModificationWatchpointRequest(field);
/* 56 */     modificationWatchpointRequest.setSuspendPolicy(this.suspendPolicy);
/* 57 */     modificationWatchpointRequest.enable();
/* 58 */     return (EventRequest)modificationWatchpointRequest;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return MessageOutput.format("watch modification of", new Object[] { this.refSpec
/* 64 */           .toString(), this.fieldId });
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\ModificationWatchpointSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */