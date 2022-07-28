/*     */ package com.sun.tools.javac.util;
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
/*     */ public class IntHashTable
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_SIZE = 64;
/*     */   protected Object[] objs;
/*     */   protected int[] ints;
/*     */   protected int mask;
/*     */   protected int num_bindings;
/*  43 */   private static final Object DELETED = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntHashTable() {
/*  51 */     this.objs = new Object[64];
/*  52 */     this.ints = new int[64];
/*  53 */     this.mask = 63;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntHashTable(int paramInt) {
/*  61 */     byte b = 4;
/*  62 */     while (paramInt > 1 << b) {
/*  63 */       b++;
/*     */     }
/*  65 */     paramInt = 1 << b;
/*  66 */     this.objs = new Object[paramInt];
/*  67 */     this.ints = new int[paramInt];
/*  68 */     this.mask = paramInt - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hash(Object paramObject) {
/*  78 */     return System.identityHashCode(paramObject);
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
/*     */   public int lookup(Object paramObject, int paramInt) {
/*  91 */     int i = paramInt ^ paramInt >>> 15;
/*  92 */     int j = paramInt ^ paramInt << 6 | 0x1;
/*  93 */     int k = -1; int m;
/*  94 */     for (m = i & this.mask;; m = m + j & this.mask) {
/*  95 */       Object object = this.objs[m];
/*  96 */       if (object == paramObject)
/*  97 */         return m; 
/*  98 */       if (object == null)
/*  99 */         return (k >= 0) ? k : m; 
/* 100 */       if (object == DELETED && k < 0) {
/* 101 */         k = m;
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
/*     */   public int lookup(Object paramObject) {
/* 113 */     return lookup(paramObject, hash(paramObject));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFromIndex(int paramInt) {
/* 124 */     Object object = this.objs[paramInt];
/* 125 */     return (object == null || object == DELETED) ? -1 : this.ints[paramInt];
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
/*     */   public int putAtIndex(Object paramObject, int paramInt1, int paramInt2) {
/* 139 */     Object object = this.objs[paramInt2];
/* 140 */     if (object == null || object == DELETED) {
/* 141 */       this.objs[paramInt2] = paramObject;
/* 142 */       this.ints[paramInt2] = paramInt1;
/* 143 */       if (object != DELETED)
/* 144 */         this.num_bindings++; 
/* 145 */       if (3 * this.num_bindings >= 2 * this.objs.length)
/* 146 */         rehash(); 
/* 147 */       return -1;
/*     */     } 
/* 149 */     int i = this.ints[paramInt2];
/* 150 */     this.ints[paramInt2] = paramInt1;
/* 151 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public int remove(Object paramObject) {
/* 156 */     int i = lookup(paramObject);
/* 157 */     Object object = this.objs[i];
/* 158 */     if (object == null || object == DELETED)
/* 159 */       return -1; 
/* 160 */     this.objs[i] = DELETED;
/* 161 */     return this.ints[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rehash() {
/* 170 */     Object[] arrayOfObject1 = this.objs;
/* 171 */     int[] arrayOfInt1 = this.ints;
/* 172 */     int i = arrayOfObject1.length;
/* 173 */     int j = i << 1;
/* 174 */     Object[] arrayOfObject2 = new Object[j];
/* 175 */     int[] arrayOfInt2 = new int[j];
/* 176 */     int k = j - 1;
/* 177 */     this.objs = arrayOfObject2;
/* 178 */     this.ints = arrayOfInt2;
/* 179 */     this.mask = k;
/* 180 */     this.num_bindings = 0;
/*     */     
/* 182 */     for (int m = arrayOfInt1.length; --m >= 0; ) {
/* 183 */       Object object = arrayOfObject1[m];
/* 184 */       if (object != null && object != DELETED) {
/* 185 */         putAtIndex(object, arrayOfInt1[m], lookup(object, hash(object)));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 193 */     for (int i = this.objs.length; --i >= 0;) {
/* 194 */       this.objs[i] = null;
/*     */     }
/* 196 */     this.num_bindings = 0;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\IntHashTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */