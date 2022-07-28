/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.java.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Vset
/*     */   implements Constants
/*     */ {
/*     */   long vset;
/*     */   long uset;
/*     */   long[] x;
/*  58 */   static final long[] emptyX = new long[0];
/*  59 */   static final long[] fullX = new long[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int VBITS = 64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   static final Vset DEAD_END = new Vset(-1L, -1L, fullX);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset() {
/*  84 */     this.x = emptyX;
/*     */   }
/*     */   
/*     */   private Vset(long paramLong1, long paramLong2, long[] paramArrayOflong) {
/*  88 */     this.vset = paramLong1;
/*  89 */     this.uset = paramLong2;
/*  90 */     this.x = paramArrayOflong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset copy() {
/*  98 */     if (this == DEAD_END) {
/*  99 */       return this;
/*     */     }
/* 101 */     Vset vset = new Vset(this.vset, this.uset, this.x);
/* 102 */     if (this.x.length > 0) {
/* 103 */       vset.growX(this.x.length);
/*     */     }
/* 105 */     return vset;
/*     */   }
/*     */   
/*     */   private void growX(int paramInt) {
/* 109 */     long[] arrayOfLong1 = new long[paramInt];
/* 110 */     long[] arrayOfLong2 = this.x;
/* 111 */     for (byte b = 0; b < arrayOfLong2.length; b++) {
/* 112 */       arrayOfLong1[b] = arrayOfLong2[b];
/*     */     }
/* 114 */     this.x = arrayOfLong1;
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
/*     */   public boolean isDeadEnd() {
/* 128 */     return (this == DEAD_END);
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
/*     */   public boolean isReallyDeadEnd() {
/* 141 */     return (this.x == fullX);
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
/*     */   public Vset clearDeadEnd() {
/* 155 */     if (this == DEAD_END) {
/* 156 */       return new Vset(-1L, -1L, fullX);
/*     */     }
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean testVar(int paramInt) {
/* 165 */     long l = 1L << paramInt;
/* 166 */     if (paramInt >= 64) {
/* 167 */       int i = (paramInt / 64 - 1) * 2;
/* 168 */       if (i >= this.x.length) {
/* 169 */         return (this.x == fullX);
/*     */       }
/* 171 */       return ((this.x[i] & l) != 0L);
/*     */     } 
/* 173 */     return ((this.vset & l) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean testVarUnassigned(int paramInt) {
/* 183 */     long l = 1L << paramInt;
/* 184 */     if (paramInt >= 64) {
/*     */       
/* 186 */       int i = (paramInt / 64 - 1) * 2 + 1;
/* 187 */       if (i >= this.x.length) {
/* 188 */         return (this.x == fullX);
/*     */       }
/* 190 */       return ((this.x[i] & l) != 0L);
/*     */     } 
/* 192 */     return ((this.uset & l) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset addVar(int paramInt) {
/* 201 */     if (this.x == fullX) {
/* 202 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 207 */     long l = 1L << paramInt;
/* 208 */     if (paramInt >= 64) {
/* 209 */       int i = (paramInt / 64 - 1) * 2;
/* 210 */       if (i >= this.x.length) {
/* 211 */         growX(i + 1);
/*     */       }
/* 213 */       this.x[i] = this.x[i] | l;
/* 214 */       if (i + 1 < this.x.length) {
/* 215 */         this.x[i + 1] = this.x[i + 1] & (l ^ 0xFFFFFFFFFFFFFFFFL);
/*     */       }
/*     */     } else {
/* 218 */       this.vset |= l;
/* 219 */       this.uset &= l ^ 0xFFFFFFFFFFFFFFFFL;
/*     */     } 
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset addVarUnassigned(int paramInt) {
/* 229 */     if (this.x == fullX) {
/* 230 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 235 */     long l = 1L << paramInt;
/* 236 */     if (paramInt >= 64) {
/*     */       
/* 238 */       int i = (paramInt / 64 - 1) * 2 + 1;
/* 239 */       if (i >= this.x.length) {
/* 240 */         growX(i + 1);
/*     */       }
/* 242 */       this.x[i] = this.x[i] | l;
/* 243 */       this.x[i - 1] = this.x[i - 1] & (l ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     } else {
/* 245 */       this.uset |= l;
/* 246 */       this.vset &= l ^ 0xFFFFFFFFFFFFFFFFL;
/*     */     } 
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset clearVar(int paramInt) {
/* 257 */     if (this.x == fullX) {
/* 258 */       return this;
/*     */     }
/* 260 */     long l = 1L << paramInt;
/* 261 */     if (paramInt >= 64) {
/* 262 */       int i = (paramInt / 64 - 1) * 2;
/* 263 */       if (i >= this.x.length) {
/* 264 */         return this;
/*     */       }
/* 266 */       this.x[i] = this.x[i] & (l ^ 0xFFFFFFFFFFFFFFFFL);
/* 267 */       if (i + 1 < this.x.length) {
/* 268 */         this.x[i + 1] = this.x[i + 1] & (l ^ 0xFFFFFFFFFFFFFFFFL);
/*     */       }
/*     */     } else {
/* 271 */       this.vset &= l ^ 0xFFFFFFFFFFFFFFFFL;
/* 272 */       this.uset &= l ^ 0xFFFFFFFFFFFFFFFFL;
/*     */     } 
/* 274 */     return this;
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
/*     */   public Vset join(Vset paramVset) {
/* 288 */     if (this == DEAD_END) {
/* 289 */       return paramVset.copy();
/*     */     }
/* 291 */     if (paramVset == DEAD_END) {
/* 292 */       return this;
/*     */     }
/* 294 */     if (this.x == fullX) {
/* 295 */       return paramVset.copy();
/*     */     }
/* 297 */     if (paramVset.x == fullX) {
/* 298 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     this.vset &= paramVset.vset;
/* 305 */     this.uset &= paramVset.uset;
/*     */     
/* 307 */     if (paramVset.x == emptyX) {
/* 308 */       this.x = emptyX;
/*     */     } else {
/*     */       
/* 311 */       long[] arrayOfLong = paramVset.x;
/* 312 */       int i = this.x.length;
/* 313 */       int j = (arrayOfLong.length < i) ? arrayOfLong.length : i; int k;
/* 314 */       for (k = 0; k < j; k++) {
/* 315 */         this.x[k] = this.x[k] & arrayOfLong[k];
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 320 */       for (k = j; k < i; k++) {
/* 321 */         this.x[k] = 0L;
/*     */       }
/*     */     } 
/* 324 */     return this;
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
/*     */ 
/*     */   
/*     */   public Vset addDAandJoinDU(Vset paramVset) {
/* 340 */     if (this == DEAD_END) {
/* 341 */       return this;
/*     */     }
/* 343 */     if (paramVset == DEAD_END) {
/* 344 */       return paramVset;
/*     */     }
/* 346 */     if (this.x == fullX) {
/* 347 */       return this;
/*     */     }
/* 349 */     if (paramVset.x == fullX) {
/* 350 */       return paramVset.copy();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     this.vset |= paramVset.vset;
/* 357 */     this.uset = this.uset & paramVset.uset & (paramVset.vset ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     
/* 359 */     int i = this.x.length;
/* 360 */     long[] arrayOfLong = paramVset.x;
/* 361 */     int j = arrayOfLong.length;
/*     */     
/* 363 */     if (arrayOfLong != emptyX) {
/*     */       
/* 365 */       if (j > i) {
/* 366 */         growX(j);
/*     */       }
/* 368 */       byte b = 0;
/* 369 */       while (b < j) {
/* 370 */         this.x[b] = this.x[b] | arrayOfLong[b];
/* 371 */         b++;
/* 372 */         if (b == j)
/* 373 */           break;  this.x[b] = this.x[b] & arrayOfLong[b] & (arrayOfLong[b - 1] ^ 0xFFFFFFFFFFFFFFFFL);
/* 374 */         b++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     for (int k = j | 0x1; k < i; k += 2) {
/* 383 */       this.x[k] = 0L;
/*     */     }
/* 385 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vset firstDAandSecondDU(Vset paramVset1, Vset paramVset2) {
/* 404 */     if (paramVset1.x == fullX) {
/* 405 */       return paramVset1.copy();
/*     */     }
/*     */     
/* 408 */     long[] arrayOfLong1 = paramVset1.x;
/* 409 */     int i = arrayOfLong1.length;
/* 410 */     long[] arrayOfLong2 = paramVset2.x;
/* 411 */     int j = arrayOfLong2.length;
/* 412 */     int k = (i > j) ? i : j;
/* 413 */     long[] arrayOfLong3 = emptyX;
/*     */     
/* 415 */     if (k > 0) {
/* 416 */       arrayOfLong3 = new long[k]; byte b;
/* 417 */       for (b = 0; b < i; b += 2) {
/* 418 */         arrayOfLong3[b] = arrayOfLong1[b];
/*     */       }
/* 420 */       for (b = 1; b < j; b += 2) {
/* 421 */         arrayOfLong3[b] = arrayOfLong2[b];
/*     */       }
/*     */     } 
/*     */     
/* 425 */     return new Vset(paramVset1.vset, paramVset2.uset, arrayOfLong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset removeAdditionalVars(int paramInt) {
/* 436 */     if (this.x == fullX) {
/* 437 */       return this;
/*     */     }
/* 439 */     long l = 1L << paramInt;
/* 440 */     if (paramInt >= 64) {
/* 441 */       int i = (paramInt / 64 - 1) * 2;
/* 442 */       if (i < this.x.length) {
/* 443 */         this.x[i] = this.x[i] & l - 1L;
/* 444 */         if (++i < this.x.length) {
/* 445 */           this.x[i] = this.x[i] & l - 1L;
/*     */         }
/* 447 */         while (++i < this.x.length) {
/* 448 */           this.x[i] = 0L;
/*     */         }
/*     */       } 
/*     */     } else {
/* 452 */       if (this.x.length > 0) {
/* 453 */         this.x = emptyX;
/*     */       }
/* 455 */       this.vset &= l - 1L;
/* 456 */       this.uset &= l - 1L;
/*     */     } 
/* 458 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int varLimit() {
/*     */     byte b;
/* 468 */     for (int i = this.x.length / 2 * 2; i >= 0; i -= 2) {
/* 469 */       if (i != this.x.length) {
/* 470 */         long l1 = this.x[i];
/* 471 */         if (i + 1 < this.x.length) {
/* 472 */           l1 |= this.x[i + 1];
/*     */         }
/* 474 */         if (l1 != 0L) {
/* 475 */           b = (i / 2 + 1) * 64; continue label22;
/*     */         } 
/*     */       } 
/*     */     } 
/* 479 */     long l = this.vset;
/* 480 */     l |= this.uset;
/* 481 */     if (l != 0L) {
/* 482 */       b = 0;
/*     */     } else {
/*     */       
/* 485 */       return 0;
/*     */     } 
/*     */     
/* 488 */     label22: while (l != 0L) {
/* 489 */       b++;
/* 490 */       l >>>= 1L;
/*     */     } 
/* 492 */     return b;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 496 */     if (this == DEAD_END)
/* 497 */       return "{DEAD_END}"; 
/* 498 */     StringBuffer stringBuffer = new StringBuffer("{");
/* 499 */     int i = 64 * (1 + (this.x.length + 1) / 2);
/* 500 */     for (byte b = 0; b < i; b++) {
/* 501 */       if (!testVarUnassigned(b)) {
/* 502 */         if (stringBuffer.length() > 1) {
/* 503 */           stringBuffer.append(' ');
/*     */         }
/* 505 */         stringBuffer.append(b);
/* 506 */         if (!testVar(b)) {
/* 507 */           stringBuffer.append('?');
/*     */         }
/*     */       } 
/*     */     } 
/* 511 */     if (this.x == fullX) {
/* 512 */       stringBuffer.append("...DEAD_END");
/*     */     }
/* 514 */     stringBuffer.append('}');
/* 515 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\Vset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */