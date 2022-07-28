/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import com.sun.jdi.ThreadReference;
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
/*     */ class ThreadInfo
/*     */ {
/*  48 */   private static List<ThreadInfo> threads = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*     */   private static boolean gotInitialThreads = false;
/*  51 */   private static ThreadInfo current = null;
/*  52 */   private static ThreadGroupReference group = null;
/*     */   
/*     */   private final ThreadReference thread;
/*  55 */   private int currentFrameIndex = 0;
/*     */   
/*     */   private ThreadInfo(ThreadReference paramThreadReference) {
/*  58 */     this.thread = paramThreadReference;
/*  59 */     if (paramThreadReference == null) {
/*  60 */       MessageOutput.fatalError("Internal error: null ThreadInfo created");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void initThreads() {
/*  65 */     if (!gotInitialThreads) {
/*  66 */       for (ThreadReference threadReference : Env.vm().allThreads()) {
/*  67 */         threads.add(new ThreadInfo(threadReference));
/*     */       }
/*  69 */       gotInitialThreads = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   static void addThread(ThreadReference paramThreadReference) {
/*  74 */     synchronized (threads) {
/*  75 */       initThreads();
/*  76 */       ThreadInfo threadInfo = new ThreadInfo(paramThreadReference);
/*     */ 
/*     */ 
/*     */       
/*  80 */       if (getThreadInfo(paramThreadReference) == null) {
/*  81 */         threads.add(threadInfo);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   static void removeThread(ThreadReference paramThreadReference) {
/*  87 */     if (paramThreadReference.equals(current)) {
/*     */       String str;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  95 */         str = "\"" + paramThreadReference.name() + "\"";
/*  96 */       } catch (Exception exception) {
/*  97 */         str = "";
/*     */       } 
/*     */       
/* 100 */       setCurrentThread(null);
/*     */       
/* 102 */       MessageOutput.println();
/* 103 */       MessageOutput.println("Current thread died. Execution continuing...", str);
/*     */     } 
/*     */     
/* 106 */     threads.remove(getThreadInfo(paramThreadReference));
/*     */   }
/*     */   
/*     */   static List<ThreadInfo> threads() {
/* 110 */     synchronized (threads) {
/* 111 */       initThreads();
/*     */       
/* 113 */       return new ArrayList<>(threads);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void invalidateAll() {
/* 118 */     current = null;
/* 119 */     group = null;
/* 120 */     synchronized (threads) {
/* 121 */       for (ThreadInfo threadInfo : threads()) {
/* 122 */         threadInfo.invalidate();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   static void setThreadGroup(ThreadGroupReference paramThreadGroupReference) {
/* 128 */     group = paramThreadGroupReference;
/*     */   }
/*     */   
/*     */   static void setCurrentThread(ThreadReference paramThreadReference) {
/* 132 */     if (paramThreadReference == null) {
/* 133 */       setCurrentThreadInfo(null);
/*     */     } else {
/* 135 */       ThreadInfo threadInfo = getThreadInfo(paramThreadReference);
/* 136 */       setCurrentThreadInfo(threadInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void setCurrentThreadInfo(ThreadInfo paramThreadInfo) {
/* 141 */     current = paramThreadInfo;
/* 142 */     if (current != null) {
/* 143 */       current.invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ThreadInfo getCurrentThreadInfo() {
/* 153 */     return current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ThreadReference getThread() {
/* 162 */     return this.thread;
/*     */   }
/*     */   
/*     */   static ThreadGroupReference group() {
/* 166 */     if (group == null)
/*     */     {
/*     */       
/* 169 */       setThreadGroup(Env.vm().topLevelThreadGroups().get(0));
/*     */     }
/* 171 */     return group;
/*     */   }
/*     */   
/*     */   static ThreadInfo getThreadInfo(long paramLong) {
/* 175 */     ThreadInfo threadInfo = null;
/*     */     
/* 177 */     synchronized (threads) {
/* 178 */       for (ThreadInfo threadInfo1 : threads()) {
/* 179 */         if (threadInfo1.thread.uniqueID() == paramLong) {
/* 180 */           threadInfo = threadInfo1;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 185 */     return threadInfo;
/*     */   }
/*     */   
/*     */   static ThreadInfo getThreadInfo(ThreadReference paramThreadReference) {
/* 189 */     return getThreadInfo(paramThreadReference.uniqueID());
/*     */   }
/*     */   
/*     */   static ThreadInfo getThreadInfo(String paramString) {
/* 193 */     ThreadInfo threadInfo = null;
/* 194 */     if (paramString.startsWith("t@")) {
/* 195 */       paramString = paramString.substring(2);
/*     */     }
/*     */     try {
/* 198 */       long l = Long.decode(paramString).longValue();
/* 199 */       threadInfo = getThreadInfo(l);
/* 200 */     } catch (NumberFormatException numberFormatException) {
/* 201 */       threadInfo = null;
/*     */     } 
/* 203 */     return threadInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<StackFrame> getStack() throws IncompatibleThreadStateException {
/* 212 */     return this.thread.frames();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StackFrame getCurrentFrame() throws IncompatibleThreadStateException {
/* 221 */     if (this.thread.frameCount() == 0) {
/* 222 */       return null;
/*     */     }
/* 224 */     return this.thread.frame(this.currentFrameIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void invalidate() {
/* 231 */     this.currentFrameIndex = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void assureSuspended() throws IncompatibleThreadStateException {
/* 236 */     if (!this.thread.isSuspended()) {
/* 237 */       throw new IncompatibleThreadStateException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getCurrentFrameIndex() {
/* 248 */     return this.currentFrameIndex;
/*     */   }
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
/*     */   void setCurrentFrameIndex(int paramInt) throws IncompatibleThreadStateException {
/* 262 */     assureSuspended();
/* 263 */     if (paramInt < 0 || paramInt >= this.thread.frameCount()) {
/* 264 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 266 */     this.currentFrameIndex = paramInt;
/*     */   }
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
/*     */   void up(int paramInt) throws IncompatibleThreadStateException {
/* 280 */     setCurrentFrameIndex(this.currentFrameIndex + paramInt);
/*     */   }
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
/*     */   void down(int paramInt) throws IncompatibleThreadStateException {
/* 293 */     setCurrentFrameIndex(this.currentFrameIndex - paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\ThreadInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */