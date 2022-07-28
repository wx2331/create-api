/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ class VMState
/*     */ {
/*     */   private final VirtualMachineImpl vm;
/*  37 */   private final List<WeakReference<VMListener>> listeners = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean notifyingListeners = false;
/*     */ 
/*     */ 
/*     */   
/*  45 */   private final Set<Integer> pendingResumeCommands = Collections.synchronizedSet(new HashSet<>());
/*     */   
/*     */   private static class Cache
/*     */   {
/*  49 */     List<ThreadGroupReference> groups = null; private Cache() {}
/*  50 */     List<ThreadReference> threads = null;
/*     */   }
/*     */   
/*  53 */   private Cache cache = null;
/*  54 */   private static final Cache markerCache = new Cache();
/*     */   
/*     */   private void disableCache() {
/*  57 */     synchronized (this) {
/*  58 */       this.cache = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enableCache() {
/*  63 */     synchronized (this) {
/*  64 */       this.cache = markerCache;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Cache getCache() {
/*  69 */     synchronized (this) {
/*  70 */       if (this.cache == markerCache) {
/*  71 */         this.cache = new Cache();
/*     */       }
/*  73 */       return this.cache;
/*     */     } 
/*     */   }
/*     */   
/*     */   VMState(VirtualMachineImpl paramVirtualMachineImpl) {
/*  78 */     this.vm = paramVirtualMachineImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSuspended() {
/*  86 */     return (this.cache != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void notifyCommandComplete(int paramInt) {
/*  94 */     this.pendingResumeCommands.remove(Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   synchronized void freeze() {
/*  98 */     if (this.cache == null && this.pendingResumeCommands.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       processVMAction(new VMAction((VirtualMachine)this.vm, 1));
/* 105 */       enableCache();
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized PacketStream thawCommand(CommandSender paramCommandSender) {
/* 110 */     PacketStream packetStream = paramCommandSender.send();
/* 111 */     this.pendingResumeCommands.add(Integer.valueOf(packetStream.id()));
/* 112 */     thaw();
/* 113 */     return packetStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void thaw() {
/* 120 */     thaw(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void thaw(ThreadReference paramThreadReference) {
/* 129 */     if (this.cache != null) {
/* 130 */       if ((this.vm.traceFlags & 0x10) != 0) {
/* 131 */         this.vm.printTrace("Clearing VM suspended cache");
/*     */       }
/* 133 */       disableCache();
/*     */     } 
/* 135 */     processVMAction(new VMAction((VirtualMachine)this.vm, paramThreadReference, 2));
/*     */   }
/*     */   
/*     */   private synchronized void processVMAction(VMAction paramVMAction) {
/* 139 */     if (!this.notifyingListeners) {
/*     */       
/* 141 */       this.notifyingListeners = true;
/*     */       
/* 143 */       Iterator<WeakReference<VMListener>> iterator = this.listeners.iterator();
/* 144 */       while (iterator.hasNext()) {
/* 145 */         WeakReference<VMListener> weakReference = iterator.next();
/* 146 */         VMListener vMListener = weakReference.get();
/* 147 */         if (vMListener != null) {
/* 148 */           boolean bool = true;
/* 149 */           switch (paramVMAction.id()) {
/*     */             case 1:
/* 151 */               bool = vMListener.vmSuspended(paramVMAction);
/*     */               break;
/*     */             case 2:
/* 154 */               bool = vMListener.vmNotSuspended(paramVMAction);
/*     */               break;
/*     */           } 
/* 157 */           if (!bool) {
/* 158 */             iterator.remove();
/*     */           }
/*     */           continue;
/*     */         } 
/* 162 */         iterator.remove();
/*     */       } 
/*     */ 
/*     */       
/* 166 */       this.notifyingListeners = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized void addListener(VMListener paramVMListener) {
/* 171 */     this.listeners.add(new WeakReference<>(paramVMListener));
/*     */   }
/*     */   
/*     */   synchronized boolean hasListener(VMListener paramVMListener) {
/* 175 */     return this.listeners.contains(paramVMListener);
/*     */   }
/*     */   
/*     */   synchronized void removeListener(VMListener paramVMListener) {
/* 179 */     Iterator<WeakReference<VMListener>> iterator = this.listeners.iterator();
/* 180 */     while (iterator.hasNext()) {
/* 181 */       WeakReference weakReference = iterator.next();
/* 182 */       if (paramVMListener.equals(weakReference.get())) {
/* 183 */         iterator.remove();
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   List<ThreadReference> allThreads() {
/* 190 */     List<ThreadReference> list = null;
/*     */     try {
/* 192 */       Cache cache = getCache();
/*     */       
/* 194 */       if (cache != null)
/*     */       {
/* 196 */         list = cache.threads;
/*     */       }
/* 198 */       if (list == null) {
/* 199 */         list = Arrays.asList(
/* 200 */             (ThreadReference[])(JDWP.VirtualMachine.AllThreads.process(this.vm)).threads);
/* 201 */         if (cache != null) {
/* 202 */           cache.threads = list;
/* 203 */           if ((this.vm.traceFlags & 0x10) != 0) {
/* 204 */             this.vm.printTrace("Caching all threads (count = " + list
/* 205 */                 .size() + ") while VM suspended");
/*     */           }
/*     */         } 
/*     */       } 
/* 209 */     } catch (JDWPException jDWPException) {
/* 210 */       throw jDWPException.toJDIException();
/*     */     } 
/* 212 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   List<ThreadGroupReference> topLevelThreadGroups() {
/* 217 */     List<ThreadGroupReference> list = null;
/*     */     try {
/* 219 */       Cache cache = getCache();
/*     */       
/* 221 */       if (cache != null) {
/* 222 */         list = cache.groups;
/*     */       }
/* 224 */       if (list == null) {
/* 225 */         list = Arrays.asList(
/*     */             
/* 227 */             (ThreadGroupReference[])(JDWP.VirtualMachine.TopLevelThreadGroups.process(this.vm)).groups);
/* 228 */         if (cache != null) {
/* 229 */           cache.groups = list;
/* 230 */           if ((this.vm.traceFlags & 0x10) != 0) {
/* 231 */             this.vm.printTrace("Caching top level thread groups (count = " + list
/*     */                 
/* 233 */                 .size() + ") while VM suspended");
/*     */           }
/*     */         } 
/*     */       } 
/* 237 */     } catch (JDWPException jDWPException) {
/* 238 */       throw jDWPException.toJDIException();
/*     */     } 
/* 240 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\VMState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */