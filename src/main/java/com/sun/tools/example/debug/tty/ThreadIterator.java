/*    */ package com.sun.tools.example.debug.tty;
/*    */ 
/*    */ import com.sun.jdi.ThreadGroupReference;
/*    */ import com.sun.jdi.ThreadReference;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ class ThreadIterator
/*    */   implements Iterator<ThreadReference>
/*    */ {
/* 43 */   Iterator<ThreadReference> it = null;
/*    */   ThreadGroupIterator tgi;
/*    */   
/*    */   ThreadIterator(ThreadGroupReference paramThreadGroupReference) {
/* 47 */     this.tgi = new ThreadGroupIterator(paramThreadGroupReference);
/*    */   }
/*    */   
/*    */   ThreadIterator(List<ThreadGroupReference> paramList) {
/* 51 */     this.tgi = new ThreadGroupIterator(paramList);
/*    */   }
/*    */   
/*    */   ThreadIterator() {
/* 55 */     this.tgi = new ThreadGroupIterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 60 */     while (this.it == null || !this.it.hasNext()) {
/* 61 */       if (!this.tgi.hasNext()) {
/* 62 */         return false;
/*    */       }
/* 64 */       this.it = this.tgi.nextThreadGroup().threads().iterator();
/*    */     } 
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ThreadReference next() {
/* 71 */     return this.it.next();
/*    */   }
/*    */   
/*    */   public ThreadReference nextThread() {
/* 75 */     return next();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 80 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\ThreadIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */