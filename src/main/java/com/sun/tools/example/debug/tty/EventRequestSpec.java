/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.request.ClassPrepareRequest;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.ExceptionRequest;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class EventRequestSpec
/*     */ {
/*     */   final ReferenceTypeSpec refSpec;
/*  48 */   int suspendPolicy = 2;
/*     */   
/*  50 */   EventRequest resolved = null;
/*  51 */   ClassPrepareRequest prepareRequest = null;
/*     */   
/*     */   EventRequestSpec(ReferenceTypeSpec paramReferenceTypeSpec) {
/*  54 */     this.refSpec = paramReferenceTypeSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract EventRequest resolveEventRequest(ReferenceType paramReferenceType) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized EventRequest resolve(ClassPrepareEvent paramClassPrepareEvent) throws Exception {
/*  69 */     if (this.resolved == null && this.prepareRequest != null && this.prepareRequest
/*     */       
/*  71 */       .equals(paramClassPrepareEvent.request())) {
/*     */       
/*  73 */       this.resolved = resolveEventRequest(paramClassPrepareEvent.referenceType());
/*  74 */       this.prepareRequest.disable();
/*  75 */       Env.vm().eventRequestManager().deleteEventRequest((EventRequest)this.prepareRequest);
/*  76 */       this.prepareRequest = null;
/*     */       
/*  78 */       if (this.refSpec instanceof PatternReferenceTypeSpec) {
/*  79 */         PatternReferenceTypeSpec patternReferenceTypeSpec = (PatternReferenceTypeSpec)this.refSpec;
/*  80 */         if (!patternReferenceTypeSpec.isUnique()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  88 */           this.resolved = null;
/*  89 */           this.prepareRequest = this.refSpec.createPrepareRequest();
/*  90 */           this.prepareRequest.enable();
/*     */         } 
/*     */       } 
/*     */     } 
/*  94 */     return this.resolved;
/*     */   }
/*     */   
/*     */   synchronized void remove() {
/*  98 */     if (isResolved()) {
/*  99 */       Env.vm().eventRequestManager().deleteEventRequest(resolved());
/*     */     }
/* 101 */     if (this.refSpec instanceof PatternReferenceTypeSpec) {
/* 102 */       PatternReferenceTypeSpec patternReferenceTypeSpec = (PatternReferenceTypeSpec)this.refSpec;
/* 103 */       if (!patternReferenceTypeSpec.isUnique()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 110 */         ArrayList<ExceptionRequest> arrayList = new ArrayList();
/*     */         
/* 112 */         for (ExceptionRequest exceptionRequest : Env.vm().eventRequestManager().exceptionRequests()) {
/* 113 */           if (patternReferenceTypeSpec.matches(exceptionRequest.exception())) {
/* 114 */             arrayList.add(exceptionRequest);
/*     */           }
/*     */         } 
/* 117 */         Env.vm().eventRequestManager().deleteEventRequests(arrayList);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private EventRequest resolveAgainstPreparedClasses() throws Exception {
/* 123 */     for (ReferenceType referenceType : Env.vm().allClasses()) {
/* 124 */       if (referenceType.isPrepared() && this.refSpec.matches(referenceType)) {
/* 125 */         this.resolved = resolveEventRequest(referenceType);
/*     */       }
/*     */     } 
/* 128 */     return this.resolved;
/*     */   }
/*     */   
/*     */   synchronized EventRequest resolveEagerly() throws Exception {
/*     */     try {
/* 133 */       if (this.resolved == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 138 */         this.prepareRequest = this.refSpec.createPrepareRequest();
/* 139 */         this.prepareRequest.enable();
/*     */ 
/*     */         
/* 142 */         resolveAgainstPreparedClasses();
/* 143 */         if (this.resolved != null) {
/* 144 */           this.prepareRequest.disable();
/* 145 */           Env.vm().eventRequestManager().deleteEventRequest((EventRequest)this.prepareRequest);
/* 146 */           this.prepareRequest = null;
/*     */         } 
/*     */       } 
/* 149 */       if (this.refSpec instanceof PatternReferenceTypeSpec) {
/* 150 */         PatternReferenceTypeSpec patternReferenceTypeSpec = (PatternReferenceTypeSpec)this.refSpec;
/* 151 */         if (!patternReferenceTypeSpec.isUnique()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 159 */           this.resolved = null;
/* 160 */           if (this.prepareRequest == null) {
/* 161 */             this.prepareRequest = this.refSpec.createPrepareRequest();
/* 162 */             this.prepareRequest.enable();
/*     */           } 
/*     */         } 
/*     */       } 
/* 166 */     } catch (VMNotConnectedException vMNotConnectedException) {}
/*     */ 
/*     */ 
/*     */     
/* 170 */     return this.resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventRequest resolved() {
/* 178 */     return this.resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isResolved() {
/* 185 */     return (this.resolved != null);
/*     */   }
/*     */   
/*     */   protected boolean isJavaIdentifier(String paramString) {
/* 189 */     if (paramString.length() == 0) {
/* 190 */       return false;
/*     */     }
/*     */     
/* 193 */     int i = paramString.codePointAt(0);
/* 194 */     if (!Character.isJavaIdentifierStart(i)) {
/* 195 */       return false;
/*     */     }
/*     */     
/* 198 */     for (int j = Character.charCount(i); j < paramString.length(); j += Character.charCount(i)) {
/* 199 */       i = paramString.codePointAt(j);
/* 200 */       if (!Character.isJavaIdentifierPart(i)) {
/* 201 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   String errorMessageFor(Exception paramException) {
/* 209 */     if (paramException instanceof IllegalArgumentException)
/* 210 */       return MessageOutput.format("Invalid command syntax"); 
/* 211 */     if (paramException instanceof RuntimeException)
/*     */     {
/* 213 */       throw (RuntimeException)paramException;
/*     */     }
/* 215 */     return MessageOutput.format("Internal error; unable to set", this.refSpec
/* 216 */         .toString());
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\EventRequestSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */