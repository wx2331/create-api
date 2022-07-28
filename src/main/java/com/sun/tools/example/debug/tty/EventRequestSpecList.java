/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.event.ClassPrepareEvent;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ class EventRequestSpecList
/*     */ {
/*     */   private static final int statusResolved = 1;
/*     */   private static final int statusUnresolved = 2;
/*     */   private static final int statusError = 3;
/*  51 */   private List<EventRequestSpec> eventRequestSpecs = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean resolve(ClassPrepareEvent paramClassPrepareEvent) {
/*  62 */     boolean bool = false;
/*  63 */     synchronized (this.eventRequestSpecs) {
/*  64 */       for (EventRequestSpec eventRequestSpec : this.eventRequestSpecs) {
/*  65 */         if (!eventRequestSpec.isResolved()) {
/*     */           try {
/*  67 */             EventRequest eventRequest = eventRequestSpec.resolve(paramClassPrepareEvent);
/*  68 */             if (eventRequest != null) {
/*  69 */               MessageOutput.println("Set deferred", eventRequestSpec.toString());
/*     */             }
/*  71 */           } catch (Exception exception) {
/*  72 */             MessageOutput.println("Unable to set deferred", new Object[] { eventRequestSpec
/*  73 */                   .toString(), eventRequestSpec
/*  74 */                   .errorMessageFor(exception) });
/*  75 */             bool = true;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*  80 */     return !bool;
/*     */   }
/*     */   
/*     */   void resolveAll() {
/*  84 */     for (EventRequestSpec eventRequestSpec : this.eventRequestSpecs) {
/*     */       try {
/*  86 */         EventRequest eventRequest = eventRequestSpec.resolveEagerly();
/*  87 */         if (eventRequest != null) {
/*  88 */           MessageOutput.println("Set deferred", eventRequestSpec.toString());
/*     */         }
/*  90 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean addEagerlyResolve(EventRequestSpec paramEventRequestSpec) {
/*     */     try {
/*  97 */       this.eventRequestSpecs.add(paramEventRequestSpec);
/*  98 */       EventRequest eventRequest = paramEventRequestSpec.resolveEagerly();
/*  99 */       if (eventRequest != null) {
/* 100 */         MessageOutput.println("Set", paramEventRequestSpec.toString());
/*     */       }
/* 102 */       return true;
/* 103 */     } catch (Exception exception) {
/* 104 */       MessageOutput.println("Unable to set", new Object[] { paramEventRequestSpec
/* 105 */             .toString(), paramEventRequestSpec
/* 106 */             .errorMessageFor(exception) });
/* 107 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   BreakpointSpec createBreakpoint(String paramString, int paramInt) throws ClassNotFoundException {
/* 113 */     PatternReferenceTypeSpec patternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString);
/*     */     
/* 115 */     return new BreakpointSpec(patternReferenceTypeSpec, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BreakpointSpec createBreakpoint(String paramString1, String paramString2, List<String> paramList) throws MalformedMemberNameException, ClassNotFoundException {
/* 123 */     PatternReferenceTypeSpec patternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString1);
/*     */     
/* 125 */     return new BreakpointSpec(patternReferenceTypeSpec, paramString2, paramList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventRequestSpec createExceptionCatch(String paramString, boolean paramBoolean1, boolean paramBoolean2) throws ClassNotFoundException {
/* 132 */     PatternReferenceTypeSpec patternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString);
/*     */     
/* 134 */     return new ExceptionSpec(patternReferenceTypeSpec, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   WatchpointSpec createAccessWatchpoint(String paramString1, String paramString2) throws MalformedMemberNameException, ClassNotFoundException {
/* 141 */     PatternReferenceTypeSpec patternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString1);
/*     */     
/* 143 */     return new AccessWatchpointSpec(patternReferenceTypeSpec, paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   WatchpointSpec createModificationWatchpoint(String paramString1, String paramString2) throws MalformedMemberNameException, ClassNotFoundException {
/* 150 */     PatternReferenceTypeSpec patternReferenceTypeSpec = new PatternReferenceTypeSpec(paramString1);
/*     */     
/* 152 */     return new ModificationWatchpointSpec(patternReferenceTypeSpec, paramString2);
/*     */   }
/*     */   
/*     */   boolean delete(EventRequestSpec paramEventRequestSpec) {
/* 156 */     synchronized (this.eventRequestSpecs) {
/* 157 */       int i = this.eventRequestSpecs.indexOf(paramEventRequestSpec);
/* 158 */       if (i != -1) {
/* 159 */         EventRequestSpec eventRequestSpec = this.eventRequestSpecs.get(i);
/* 160 */         eventRequestSpec.remove();
/* 161 */         this.eventRequestSpecs.remove(i);
/* 162 */         return true;
/*     */       } 
/* 164 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   List<EventRequestSpec> eventRequestSpecs() {
/* 171 */     synchronized (this.eventRequestSpecs) {
/* 172 */       return new ArrayList<>(this.eventRequestSpecs);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\EventRequestSpecList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */