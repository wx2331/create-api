/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.Arrays;
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
/*     */ public class ThreadGroupReferenceImpl
/*     */   extends ObjectReferenceImpl
/*     */   implements ThreadGroupReference, VMListener
/*     */ {
/*     */   String name;
/*     */   ThreadGroupReference parent;
/*     */   boolean triedParent;
/*     */   
/*     */   private static class Cache
/*     */     extends ObjectReferenceImpl.Cache
/*     */   {
/*     */     private Cache() {}
/*     */     
/*  41 */     JDWP.ThreadGroupReference.Children kids = null;
/*     */   }
/*     */   
/*     */   protected ObjectReferenceImpl.Cache newCache() {
/*  45 */     return new Cache();
/*     */   }
/*     */   
/*     */   ThreadGroupReferenceImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  49 */     super(paramVirtualMachine, paramLong);
/*  50 */     this.vm.state().addListener(this);
/*     */   }
/*     */   
/*     */   protected String description() {
/*  54 */     return "ThreadGroupReference " + uniqueID();
/*     */   }
/*     */   
/*     */   public String name() {
/*  58 */     if (this.name == null) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/*  63 */         this
/*  64 */           .name = (JDWP.ThreadGroupReference.Name.process(this.vm, this)).groupName;
/*  65 */       } catch (JDWPException jDWPException) {
/*  66 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     }
/*  69 */     return this.name;
/*     */   }
/*     */   
/*     */   public ThreadGroupReference parent() {
/*  73 */     if (!this.triedParent) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/*  78 */         this
/*  79 */           .parent = (JDWP.ThreadGroupReference.Parent.process(this.vm, this)).parentGroup;
/*  80 */         this.triedParent = true;
/*  81 */       } catch (JDWPException jDWPException) {
/*  82 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     }
/*  85 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void suspend() {
/*  89 */     for (ThreadReference threadReference : threads()) {
/*  90 */       threadReference.suspend();
/*     */     }
/*     */     
/*  93 */     for (ThreadGroupReference threadGroupReference : threadGroups()) {
/*  94 */       threadGroupReference.suspend();
/*     */     }
/*     */   }
/*     */   
/*     */   public void resume() {
/*  99 */     for (ThreadReference threadReference : threads()) {
/* 100 */       threadReference.resume();
/*     */     }
/*     */     
/* 103 */     for (ThreadGroupReference threadGroupReference : threadGroups()) {
/* 104 */       threadGroupReference.resume();
/*     */     }
/*     */   }
/*     */   
/*     */   private JDWP.ThreadGroupReference.Children kids() {
/* 109 */     JDWP.ThreadGroupReference.Children children = null;
/*     */     try {
/* 111 */       Cache cache = (Cache)getCache();
/*     */       
/* 113 */       if (cache != null) {
/* 114 */         children = cache.kids;
/*     */       }
/* 116 */       if (children == null) {
/*     */         
/* 118 */         children = JDWP.ThreadGroupReference.Children.process(this.vm, this);
/* 119 */         if (cache != null) {
/* 120 */           cache.kids = children;
/* 121 */           if ((this.vm.traceFlags & 0x10) != 0) {
/* 122 */             this.vm.printTrace(description() + " temporarily caching children ");
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 127 */     } catch (JDWPException jDWPException) {
/* 128 */       throw jDWPException.toJDIException();
/*     */     } 
/* 130 */     return children;
/*     */   }
/*     */   
/*     */   public List<ThreadReference> threads() {
/* 134 */     return Arrays.asList((ThreadReference[])(kids()).childThreads);
/*     */   }
/*     */   
/*     */   public List<ThreadGroupReference> threadGroups() {
/* 138 */     return Arrays.asList((ThreadGroupReference[])(kids()).childGroups);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 142 */     return "instance of " + referenceType().name() + "(name='" + 
/* 143 */       name() + "', id=" + uniqueID() + ")";
/*     */   }
/*     */   
/*     */   byte typeValueKey() {
/* 147 */     return 103;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ThreadGroupReferenceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */