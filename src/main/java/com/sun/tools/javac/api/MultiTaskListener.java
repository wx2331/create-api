/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.util.TaskEvent;
/*     */ import com.sun.source.util.TaskListener;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ public class MultiTaskListener
/*     */   implements TaskListener
/*     */ {
/*  45 */   public static final Context.Key<MultiTaskListener> taskListenerKey = new Context.Key();
/*     */   TaskListener[] listeners;
/*     */   ClientCodeWrapper ccw;
/*     */   
/*     */   public static MultiTaskListener instance(Context paramContext) {
/*  50 */     MultiTaskListener multiTaskListener = (MultiTaskListener)paramContext.get(taskListenerKey);
/*  51 */     if (multiTaskListener == null)
/*  52 */       multiTaskListener = new MultiTaskListener(paramContext); 
/*  53 */     return multiTaskListener;
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
/*     */   protected MultiTaskListener(Context paramContext) {
/*  65 */     this.listeners = new TaskListener[0];
/*     */     paramContext.put(taskListenerKey, this);
/*     */     this.ccw = ClientCodeWrapper.instance(paramContext);
/*     */   }
/*     */   public Collection<TaskListener> getTaskListeners() {
/*  70 */     return Arrays.asList(this.listeners);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  74 */     return (this.listeners.length == 0);
/*     */   }
/*     */   
/*     */   public void add(TaskListener paramTaskListener) {
/*  78 */     for (TaskListener taskListener : this.listeners) {
/*  79 */       if (this.ccw.unwrap(taskListener) == paramTaskListener)
/*  80 */         throw new IllegalStateException(); 
/*     */     } 
/*  82 */     this.listeners = Arrays.<TaskListener>copyOf(this.listeners, this.listeners.length + 1);
/*  83 */     this.listeners[this.listeners.length - 1] = this.ccw.wrap(paramTaskListener);
/*     */   }
/*     */   
/*     */   public void remove(TaskListener paramTaskListener) {
/*  87 */     for (byte b = 0; b < this.listeners.length; b++) {
/*  88 */       if (this.ccw.unwrap(this.listeners[b]) == paramTaskListener) {
/*  89 */         TaskListener[] arrayOfTaskListener = new TaskListener[this.listeners.length - 1];
/*  90 */         System.arraycopy(this.listeners, 0, arrayOfTaskListener, 0, b);
/*  91 */         System.arraycopy(this.listeners, b + 1, arrayOfTaskListener, b, arrayOfTaskListener.length - b);
/*  92 */         this.listeners = arrayOfTaskListener;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void started(TaskEvent paramTaskEvent) {
/* 101 */     TaskListener[] arrayOfTaskListener = this.listeners;
/* 102 */     for (TaskListener taskListener : arrayOfTaskListener) {
/* 103 */       taskListener.started(paramTaskEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void finished(TaskEvent paramTaskEvent) {
/* 109 */     TaskListener[] arrayOfTaskListener = this.listeners;
/* 110 */     for (TaskListener taskListener : arrayOfTaskListener) {
/* 111 */       taskListener.finished(paramTaskEvent);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 116 */     return Arrays.toString((Object[])this.listeners);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\MultiTaskListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */