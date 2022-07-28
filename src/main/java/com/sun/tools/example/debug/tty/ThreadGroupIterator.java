/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.ThreadGroupReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
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
/*     */ class ThreadGroupIterator
/*     */   implements Iterator<ThreadGroupReference>
/*     */ {
/*  48 */   private final Stack<Iterator<ThreadGroupReference>> stack = new Stack<>();
/*     */   
/*     */   ThreadGroupIterator(List<ThreadGroupReference> paramList) {
/*  51 */     push(paramList);
/*     */   }
/*     */   
/*     */   ThreadGroupIterator(ThreadGroupReference paramThreadGroupReference) {
/*  55 */     ArrayList<ThreadGroupReference> arrayList = new ArrayList();
/*  56 */     arrayList.add(paramThreadGroupReference);
/*  57 */     push(arrayList);
/*     */   }
/*     */   
/*     */   ThreadGroupIterator() {
/*  61 */     this(Env.vm().topLevelThreadGroups());
/*     */   }
/*     */   
/*     */   private Iterator<ThreadGroupReference> top() {
/*  65 */     return this.stack.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void push(List<ThreadGroupReference> paramList) {
/*  75 */     this.stack.push(paramList.iterator());
/*  76 */     while (!this.stack.isEmpty() && !top().hasNext()) {
/*  77 */       this.stack.pop();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  83 */     return !this.stack.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadGroupReference next() {
/*  88 */     return nextThreadGroup();
/*     */   }
/*     */   
/*     */   public ThreadGroupReference nextThreadGroup() {
/*  92 */     ThreadGroupReference threadGroupReference = top().next();
/*  93 */     push(threadGroupReference.threadGroups());
/*  94 */     return threadGroupReference;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  99 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   static ThreadGroupReference find(String paramString) {
/* 103 */     ThreadGroupIterator threadGroupIterator = new ThreadGroupIterator();
/* 104 */     while (threadGroupIterator.hasNext()) {
/* 105 */       ThreadGroupReference threadGroupReference = threadGroupIterator.nextThreadGroup();
/* 106 */       if (threadGroupReference.name().equals(paramString)) {
/* 107 */         return threadGroupReference;
/*     */       }
/*     */     } 
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\ThreadGroupIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */