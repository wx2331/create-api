/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Iterators
/*     */ {
/*     */   static abstract class ReadOnly<T>
/*     */     implements Iterator<T>
/*     */   {
/*     */     public final void remove() {
/*  42 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  47 */   private static final Iterator EMPTY = Collections.EMPTY_LIST.iterator();
/*     */   
/*     */   public static <T> Iterator<T> empty() {
/*  50 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static <T> Iterator<T> singleton(T value) {
/*  54 */     return new Singleton<>(value);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Singleton<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private T next;
/*     */     
/*     */     Singleton(T next) {
/*  64 */       this.next = next;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/*  68 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public T next() {
/*  72 */       T r = this.next;
/*  73 */       this.next = null;
/*  74 */       return r;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Adapter<T, U>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends U> core;
/*     */     
/*     */     public Adapter(Iterator<? extends U> core) {
/*  85 */       this.core = core;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/*  89 */       return this.core.hasNext();
/*     */     }
/*     */     
/*     */     public T next() {
/*  93 */       return filter(this.core.next());
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract T filter(U param1U);
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Map<T, U>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends U> core;
/*     */     
/*     */     private Iterator<? extends T> current;
/*     */     
/*     */     protected Map(Iterator<? extends U> core) {
/* 109 */       this.core = core;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 113 */       while (this.current == null || !this.current.hasNext()) {
/* 114 */         if (!this.core.hasNext())
/* 115 */           return false; 
/* 116 */         this.current = apply(this.core.next());
/*     */       } 
/* 118 */       return true;
/*     */     }
/*     */     
/*     */     public T next() {
/* 122 */       return this.current.next();
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract Iterator<? extends T> apply(U param1U);
/*     */   }
/*     */   
/*     */   public static abstract class Filter<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends T> core;
/*     */     private T next;
/*     */     
/*     */     protected Filter(Iterator<? extends T> core) {
/* 136 */       this.core = core;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract boolean matches(T param1T);
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 145 */       while (this.core.hasNext() && this.next == null) {
/* 146 */         this.next = this.core.next();
/* 147 */         if (!matches(this.next)) {
/* 148 */           this.next = null;
/*     */         }
/*     */       } 
/* 151 */       return (this.next != null);
/*     */     }
/*     */     
/*     */     public T next() {
/* 155 */       if (this.next == null) throw new NoSuchElementException(); 
/* 156 */       T r = this.next;
/* 157 */       this.next = null;
/* 158 */       return r;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Unique<T>
/*     */     extends Filter<T>
/*     */   {
/* 166 */     private Set<T> values = new HashSet<>();
/*     */     public Unique(Iterator<? extends T> core) {
/* 168 */       super(core);
/*     */     }
/*     */     
/*     */     protected boolean matches(T value) {
/* 172 */       return this.values.add(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Union<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final Iterator<? extends T> first;
/*     */     private final Iterator<? extends T> second;
/*     */     
/*     */     public Union(Iterator<? extends T> first, Iterator<? extends T> second) {
/* 183 */       this.first = first;
/* 184 */       this.second = second;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 188 */       return (this.first.hasNext() || this.second.hasNext());
/*     */     }
/*     */     
/*     */     public T next() {
/* 192 */       if (this.first.hasNext()) return this.first.next(); 
/* 193 */       return this.second.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Array<T>
/*     */     extends ReadOnly<T>
/*     */   {
/*     */     private final T[] items;
/* 202 */     private int index = 0;
/*     */     public Array(T[] items) {
/* 204 */       this.items = items;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 208 */       return (this.index < this.items.length);
/*     */     }
/*     */     
/*     */     public T next() {
/* 212 */       return this.items[this.index++];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\Iterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */