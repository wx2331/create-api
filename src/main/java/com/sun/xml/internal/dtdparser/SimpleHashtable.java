/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ import java.util.Enumeration;
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
/*     */ final class SimpleHashtable
/*     */   implements Enumeration
/*     */ {
/*     */   private Entry[] table;
/*  68 */   private Entry current = null;
/*  69 */   private int currentBucket = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private int count;
/*     */ 
/*     */   
/*     */   private int threshold;
/*     */ 
/*     */   
/*     */   private static final float loadFactor = 0.75F;
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleHashtable(int initialCapacity) {
/*  84 */     if (initialCapacity < 0) {
/*  85 */       throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
/*     */     }
/*  87 */     if (initialCapacity == 0)
/*  88 */       initialCapacity = 1; 
/*  89 */     this.table = new Entry[initialCapacity];
/*  90 */     this.threshold = (int)(initialCapacity * 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleHashtable() {
/*  97 */     this(11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 103 */     this.count = 0;
/* 104 */     this.currentBucket = 0;
/* 105 */     this.current = null;
/* 106 */     for (int i = 0; i < this.table.length; i++) {
/* 107 */       this.table[i] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 116 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration keys() {
/* 126 */     this.currentBucket = 0;
/* 127 */     this.current = null;
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMoreElements() {
/* 136 */     if (this.current != null)
/* 137 */       return true; 
/* 138 */     while (this.currentBucket < this.table.length) {
/* 139 */       this.current = this.table[this.currentBucket++];
/* 140 */       if (this.current != null)
/* 141 */         return true; 
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object nextElement() {
/* 153 */     if (this.current == null)
/* 154 */       throw new IllegalStateException(); 
/* 155 */     Object retval = this.current.key;
/* 156 */     this.current = this.current.next;
/* 157 */     return retval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String key) {
/* 165 */     Entry[] tab = this.table;
/* 166 */     int hash = key.hashCode();
/* 167 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 168 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 169 */       if (e.hash == hash && e.key == key)
/* 170 */         return e.value; 
/*     */     } 
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNonInterned(String key) {
/* 180 */     Entry[] tab = this.table;
/* 181 */     int hash = key.hashCode();
/* 182 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 183 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 184 */       if (e.hash == hash && e.key.equals(key))
/* 185 */         return e.value; 
/*     */     } 
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rehash() {
/* 198 */     int oldCapacity = this.table.length;
/* 199 */     Entry[] oldMap = this.table;
/*     */     
/* 201 */     int newCapacity = oldCapacity * 2 + 1;
/* 202 */     Entry[] newMap = new Entry[newCapacity];
/*     */     
/* 204 */     this.threshold = (int)(newCapacity * 0.75F);
/* 205 */     this.table = newMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 214 */     for (int i = oldCapacity; i-- > 0;) {
/* 215 */       for (Entry old = oldMap[i]; old != null; ) {
/* 216 */         Entry e = old;
/* 217 */         old = old.next;
/*     */         
/* 219 */         int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
/* 220 */         e.next = newMap[index];
/* 221 */         newMap[index] = e;
/*     */       } 
/*     */     } 
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
/*     */   public Object put(Object key, Object value) {
/* 236 */     if (value == null) {
/* 237 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */     
/* 241 */     Entry[] tab = this.table;
/* 242 */     int hash = key.hashCode();
/* 243 */     int index = (hash & Integer.MAX_VALUE) % tab.length; Entry e;
/* 244 */     for (e = tab[index]; e != null; e = e.next) {
/*     */       
/* 246 */       if (e.hash == hash && e.key == key) {
/* 247 */         Object old = e.value;
/* 248 */         e.value = value;
/* 249 */         return old;
/*     */       } 
/*     */     } 
/*     */     
/* 253 */     if (this.count >= this.threshold) {
/*     */       
/* 255 */       rehash();
/*     */       
/* 257 */       tab = this.table;
/* 258 */       index = (hash & Integer.MAX_VALUE) % tab.length;
/*     */     } 
/*     */ 
/*     */     
/* 262 */     e = new Entry(hash, key, value, tab[index]);
/* 263 */     tab[index] = e;
/* 264 */     this.count++;
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Entry
/*     */   {
/*     */     int hash;
/*     */     
/*     */     Object key;
/*     */     
/*     */     Object value;
/*     */     Entry next;
/*     */     
/*     */     protected Entry(int hash, Object key, Object value, Entry next) {
/* 279 */       this.hash = hash;
/* 280 */       this.key = key;
/* 281 */       this.value = value;
/* 282 */       this.next = next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\SimpleHashtable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */