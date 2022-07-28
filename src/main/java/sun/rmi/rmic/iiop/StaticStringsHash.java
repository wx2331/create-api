/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StaticStringsHash
/*     */ {
/*     */   public String[] strings;
/*     */   public int[] keys;
/*     */   public int[][] buckets;
/*     */   public String method;
/*     */   private int length;
/*     */   private int[] tempKeys;
/*     */   private int[] bucketSizes;
/*     */   private int bucketCount;
/*     */   private int maxDepth;
/*     */   private int minStringLength;
/*     */   private int keyKind;
/*     */   private int charAt;
/*     */   private static final int LENGTH = 0;
/*     */   private static final int CHAR_AT = 1;
/*     */   private static final int HASH_CODE = 2;
/*     */   private static final int CHAR_AT_MAX_LINES = 50;
/*     */   private static final int CHAR_AT_MAX_CHARS = 1000;
/*     */   
/*     */   public int getKey(String paramString) {
/*  81 */     switch (this.keyKind) { case 0:
/*  82 */         return paramString.length();
/*  83 */       case 1: return paramString.charAt(this.charAt);
/*  84 */       case 2: return paramString.hashCode(); }
/*     */     
/*  86 */     throw new Error("Bad keyKind");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaticStringsHash(String[] paramArrayOfString) {
/*     */     boolean bool2;
/*     */     this.strings = null;
/*     */     this.keys = null;
/*     */     this.buckets = (int[][])null;
/*     */     this.method = null;
/* 273 */     this.minStringLength = Integer.MAX_VALUE; this.strings = paramArrayOfString; this.length = paramArrayOfString.length; this.tempKeys = new int[this.length]; this.bucketSizes = new int[this.length]; setMinStringLength(); int i = getKeys(0); int j = -1; boolean bool1 = false; if (i > 1) { bool2 = this.minStringLength; if (this.length > 50 && this.length * bool2 > 1000)
/*     */         bool2 = this.length / 1000;  this.charAt = 0; int m; for (m = 0; m < bool2; m++) { int n = getKeys(1); if (n < i) { i = n; j = m; if (i == 1)
/*     */             break;  }
/*     */          this.charAt++; }
/*     */        this.charAt = j; if (i > 1) { m = getKeys(2); if (m < i - 3)
/*     */           bool1 = true;  }
/*     */        if (!bool1)
/*     */         if (j >= 0) { getKeys(1); }
/*     */         else { getKeys(0); }
/*     */           }
/*     */      this.keys = new int[this.bucketCount]; System.arraycopy(this.tempKeys, 0, this.keys, 0, this.bucketCount); do { bool2 = false; for (byte b1 = 0; b1 < this.bucketCount - 1; b1++) { if (this.keys[b1] > this.keys[b1 + 1]) { int m = this.keys[b1]; this.keys[b1] = this.keys[b1 + 1]; this.keys[b1 + 1] = m; m = this.bucketSizes[b1]; this.bucketSizes[b1] = this.bucketSizes[b1 + 1]; this.bucketSizes[b1 + 1] = m; bool2 = true; }
/*     */          }
/*     */        }
/*     */     while (bool2 == true); int k = findUnusedKey(); this.buckets = new int[this.bucketCount][]; byte b; for (b = 0; b < this.bucketCount; b++) { this.buckets[b] = new int[this.bucketSizes[b]]; for (byte b1 = 0; b1 < this.bucketSizes[b]; b1++)
/*     */         this.buckets[b][b1] = k;  }
/*     */      for (b = 0; b < paramArrayOfString.length; b++) { int m = getKey(paramArrayOfString[b]); for (byte b1 = 0; b1 < this.bucketCount; b1++) {
/*     */         if (this.keys[b1] == m) {
/*     */           byte b2 = 0; while (this.buckets[b1][b2] != k)
/*     */             b2++;  this.buckets[b1][b2] = b; break;
/*     */         } 
/*     */       }  }
/* 294 */      } private void resetKeys(int paramInt) { this.keyKind = paramInt;
/* 295 */     switch (paramInt) { case 0:
/* 296 */         this.method = "length()"; break;
/* 297 */       case 1: this.method = "charAt(" + this.charAt + ")"; break;
/* 298 */       case 2: this.method = "hashCode()"; break; }
/*     */     
/* 300 */     this.maxDepth = 1;
/* 301 */     this.bucketCount = 0;
/* 302 */     for (byte b = 0; b < this.length; b++)
/* 303 */     { this.tempKeys[b] = 0;
/* 304 */       this.bucketSizes[b] = 0; }  }
/*     */   public static void main(String[] paramArrayOfString) { StaticStringsHash staticStringsHash = new StaticStringsHash(paramArrayOfString); System.out.println(); System.out.println("    public boolean contains(String key) {"); System.out.println("        switch (key." + staticStringsHash.method + ") {"); for (byte b = 0; b < staticStringsHash.buckets.length; b++) { System.out.println("            case " + staticStringsHash.keys[b] + ": "); for (byte b1 = 0; b1 < (staticStringsHash.buckets[b]).length; b1++) { if (b1 > 0) { System.out.print("                } else "); }
/*     */         else { System.out.print("                "); }
/*     */          System.out.println("if (key.equals(\"" + staticStringsHash.strings[staticStringsHash.buckets[b][b1]] + "\")) {"); System.out.println("                    return true;"); }
/*     */        System.out.println("                }"); }
/* 309 */      System.out.println("        }"); System.out.println("        return false;"); System.out.println("    }"); } private void setMinStringLength() { for (byte b = 0; b < this.length; b++) {
/* 310 */       if (this.strings[b].length() < this.minStringLength) {
/* 311 */         this.minStringLength = this.strings[b].length();
/*     */       }
/*     */     }  }
/*     */ 
/*     */   
/*     */   private int findUnusedKey() {
/* 317 */     byte b = 0;
/* 318 */     int i = this.keys.length;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 326 */       boolean bool = false;
/* 327 */       for (byte b1 = 0; b1 < i; b1++) {
/* 328 */         if (this.keys[b1] == b) {
/* 329 */           bool = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 333 */       if (bool) {
/* 334 */         b--;
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 339 */     return b;
/*     */   }
/*     */   
/*     */   private int getKeys(int paramInt) {
/* 343 */     resetKeys(paramInt);
/* 344 */     for (byte b = 0; b < this.strings.length; b++) {
/* 345 */       addKey(getKey(this.strings[b]));
/*     */     }
/* 347 */     return this.maxDepth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addKey(int paramInt) {
/* 354 */     boolean bool = true;
/* 355 */     for (byte b = 0; b < this.bucketCount; b++) {
/* 356 */       if (this.tempKeys[b] == paramInt) {
/* 357 */         bool = false;
/* 358 */         this.bucketSizes[b] = this.bucketSizes[b] + 1;
/* 359 */         if (this.bucketSizes[b] > this.maxDepth) {
/* 360 */           this.maxDepth = this.bucketSizes[b];
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 366 */     if (bool) {
/* 367 */       this.tempKeys[this.bucketCount] = paramInt;
/* 368 */       this.bucketSizes[this.bucketCount] = 1;
/* 369 */       this.bucketCount++;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\StaticStringsHash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */