/*     */ package com.sun.xml.internal.rngom.binary;
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
/*     */ final class PatternInterner
/*     */ {
/*     */   private static final int INIT_SIZE = 256;
/*     */   private static final float LOAD_FACTOR = 0.3F;
/*     */   private Pattern[] table;
/*     */   private int used;
/*     */   private int usedLimit;
/*     */   
/*     */   PatternInterner() {
/*  56 */     this.table = null;
/*  57 */     this.used = 0;
/*  58 */     this.usedLimit = 0;
/*     */   }
/*     */   
/*     */   PatternInterner(PatternInterner parent) {
/*  62 */     this.table = parent.table;
/*  63 */     if (this.table != null)
/*  64 */       this.table = (Pattern[])this.table.clone(); 
/*  65 */     this.used = parent.used;
/*  66 */     this.usedLimit = parent.usedLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Pattern intern(Pattern p) {
/*     */     int i;
/*  73 */     if (this.table == null) {
/*  74 */       this.table = new Pattern[256];
/*  75 */       this.usedLimit = 76;
/*  76 */       i = firstIndex(p);
/*     */     } else {
/*  78 */       for (i = firstIndex(p); this.table[i] != null; i = nextIndex(i)) {
/*  79 */         if (p.samePattern(this.table[i]))
/*  80 */           return this.table[i]; 
/*     */       } 
/*     */     } 
/*  83 */     if (this.used >= this.usedLimit) {
/*     */       
/*  85 */       Pattern[] oldTable = this.table;
/*  86 */       this.table = new Pattern[this.table.length << 1];
/*  87 */       for (int j = oldTable.length; j > 0; ) {
/*  88 */         j--;
/*  89 */         if (oldTable[j] != null) {
/*     */           
/*  91 */           int k = firstIndex(oldTable[j]);
/*  92 */           while (this.table[k] != null)
/*  93 */             k = nextIndex(k); 
/*  94 */           this.table[k] = oldTable[j];
/*     */         } 
/*     */       } 
/*  97 */       for (i = firstIndex(p); this.table[i] != null; i = nextIndex(i));
/*  98 */       this.usedLimit = (int)(this.table.length * 0.3F);
/*     */     } 
/* 100 */     this.used++;
/* 101 */     this.table[i] = p;
/* 102 */     return p;
/*     */   }
/*     */   
/*     */   private int firstIndex(Pattern p) {
/* 106 */     return p.patternHashCode() & this.table.length - 1;
/*     */   }
/*     */   
/*     */   private int nextIndex(int i) {
/* 110 */     return (i == 0) ? (this.table.length - 1) : (i - 1);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\PatternInterner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */