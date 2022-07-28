/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ public abstract class DContainerPattern
/*     */   extends DPattern
/*     */   implements Iterable<DPattern>
/*     */ {
/*     */   private DPattern head;
/*     */   private DPattern tail;
/*     */   
/*     */   public DPattern firstChild() {
/*  61 */     return this.head;
/*     */   }
/*     */   
/*     */   public DPattern lastChild() {
/*  65 */     return this.tail;
/*     */   }
/*     */   
/*     */   public int countChildren() {
/*  69 */     int i = 0;
/*  70 */     for (DPattern p = firstChild(); p != null; p = p.next)
/*  71 */       i++; 
/*  72 */     return i;
/*     */   }
/*     */   
/*     */   public Iterator<DPattern> iterator() {
/*  76 */     return new Iterator<DPattern>() {
/*  77 */         DPattern next = DContainerPattern.this.head;
/*     */         public boolean hasNext() {
/*  79 */           return (this.next != null);
/*     */         }
/*     */         
/*     */         public DPattern next() {
/*  83 */           DPattern r = this.next;
/*  84 */           this.next = this.next.next;
/*  85 */           return r;
/*     */         }
/*     */         
/*     */         public void remove() {
/*  89 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   void add(DPattern child) {
/*  95 */     if (this.tail == null) {
/*  96 */       child.prev = child.next = null;
/*  97 */       this.head = this.tail = child;
/*     */     } else {
/*  99 */       child.prev = this.tail;
/* 100 */       this.tail.next = child;
/* 101 */       child.next = null;
/* 102 */       this.tail = child;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DContainerPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */