/*     */ package com.sun.codemodel.internal.util;
/*     */ 
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Surrogate
/*     */ {
/*     */   public static final char MIN_HIGH = '?';
/*     */   public static final char MAX_HIGH = '?';
/*     */   public static final char MIN_LOW = '?';
/*     */   public static final char MAX_LOW = '?';
/*     */   public static final char MIN = '?';
/*     */   public static final char MAX = '?';
/*     */   public static final int UCS4_MIN = 65536;
/*     */   public static final int UCS4_MAX = 1114111;
/*     */   
/*     */   public static boolean isHigh(int c) {
/*  61 */     return (55296 <= c && c <= 56319);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLow(int c) {
/*  68 */     return (56320 <= c && c <= 57343);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean is(int c) {
/*  75 */     return (55296 <= c && c <= 57343);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean neededFor(int uc) {
/*  83 */     return (uc >= 65536 && uc <= 1114111);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char high(int uc) {
/*  90 */     return (char)(0xD800 | uc - 65536 >> 10 & 0x3FF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char low(int uc) {
/*  97 */     return (char)(0xDC00 | uc - 65536 & 0x3FF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toUCS4(char c, char d) {
/* 104 */     return ((c & 0x3FF) << 10 | d & 0x3FF) + 65536;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Parser
/*     */   {
/*     */     private int character;
/*     */ 
/*     */ 
/*     */     
/* 116 */     private CoderResult error = CoderResult.UNDERFLOW;
/*     */ 
/*     */     
/*     */     private boolean isPair;
/*     */ 
/*     */     
/*     */     public int character() {
/* 123 */       return this.character;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isPair() {
/* 131 */       return this.isPair;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int increment() {
/* 139 */       return this.isPair ? 2 : 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CoderResult error() {
/* 147 */       return this.error;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CoderResult unmappableResult() {
/* 155 */       return CoderResult.unmappableForLength(this.isPair ? 2 : 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int parse(char c, CharBuffer in) {
/* 172 */       if (Surrogate.isHigh(c)) {
/* 173 */         if (!in.hasRemaining()) {
/* 174 */           this.error = CoderResult.UNDERFLOW;
/* 175 */           return -1;
/*     */         } 
/* 177 */         char d = in.get();
/* 178 */         if (Surrogate.isLow(d)) {
/* 179 */           this.character = Surrogate.toUCS4(c, d);
/* 180 */           this.isPair = true;
/* 181 */           this.error = null;
/* 182 */           return this.character;
/*     */         } 
/* 184 */         this.error = CoderResult.malformedForLength(1);
/* 185 */         return -1;
/*     */       } 
/* 187 */       if (Surrogate.isLow(c)) {
/* 188 */         this.error = CoderResult.malformedForLength(1);
/* 189 */         return -1;
/*     */       } 
/* 191 */       this.character = c;
/* 192 */       this.isPair = false;
/* 193 */       this.error = null;
/* 194 */       return this.character;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int parse(char c, char[] ia, int ip, int il) {
/* 213 */       if (Surrogate.isHigh(c)) {
/* 214 */         if (il - ip < 2) {
/* 215 */           this.error = CoderResult.UNDERFLOW;
/* 216 */           return -1;
/*     */         } 
/* 218 */         char d = ia[ip + 1];
/* 219 */         if (Surrogate.isLow(d)) {
/* 220 */           this.character = Surrogate.toUCS4(c, d);
/* 221 */           this.isPair = true;
/* 222 */           this.error = null;
/* 223 */           return this.character;
/*     */         } 
/* 225 */         this.error = CoderResult.malformedForLength(1);
/* 226 */         return -1;
/*     */       } 
/* 228 */       if (Surrogate.isLow(c)) {
/* 229 */         this.error = CoderResult.malformedForLength(1);
/* 230 */         return -1;
/*     */       } 
/* 232 */       this.character = c;
/* 233 */       this.isPair = false;
/* 234 */       this.error = null;
/* 235 */       return this.character;
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
/*     */   public static class Generator
/*     */   {
/* 249 */     private CoderResult error = CoderResult.OVERFLOW;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CoderResult error() {
/* 256 */       return this.error;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int generate(int uc, int len, CharBuffer dst) {
/* 274 */       if (uc <= 65535) {
/* 275 */         if (Surrogate.is(uc)) {
/* 276 */           this.error = CoderResult.malformedForLength(len);
/* 277 */           return -1;
/*     */         } 
/* 279 */         if (dst.remaining() < 1) {
/* 280 */           this.error = CoderResult.OVERFLOW;
/* 281 */           return -1;
/*     */         } 
/* 283 */         dst.put((char)uc);
/* 284 */         this.error = null;
/* 285 */         return 1;
/*     */       } 
/* 287 */       if (uc < 65536) {
/* 288 */         this.error = CoderResult.malformedForLength(len);
/* 289 */         return -1;
/*     */       } 
/* 291 */       if (uc <= 1114111) {
/* 292 */         if (dst.remaining() < 2) {
/* 293 */           this.error = CoderResult.OVERFLOW;
/* 294 */           return -1;
/*     */         } 
/* 296 */         dst.put(Surrogate.high(uc));
/* 297 */         dst.put(Surrogate.low(uc));
/* 298 */         this.error = null;
/* 299 */         return 2;
/*     */       } 
/* 301 */       this.error = CoderResult.unmappableForLength(len);
/* 302 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int generate(int uc, int len, char[] da, int dp, int dl) {
/* 322 */       if (uc <= 65535) {
/* 323 */         if (Surrogate.is(uc)) {
/* 324 */           this.error = CoderResult.malformedForLength(len);
/* 325 */           return -1;
/*     */         } 
/* 327 */         if (dl - dp < 1) {
/* 328 */           this.error = CoderResult.OVERFLOW;
/* 329 */           return -1;
/*     */         } 
/* 331 */         da[dp] = (char)uc;
/* 332 */         this.error = null;
/* 333 */         return 1;
/*     */       } 
/* 335 */       if (uc < 65536) {
/* 336 */         this.error = CoderResult.malformedForLength(len);
/* 337 */         return -1;
/*     */       } 
/* 339 */       if (uc <= 1114111) {
/* 340 */         if (dl - dp < 2) {
/* 341 */           this.error = CoderResult.OVERFLOW;
/* 342 */           return -1;
/*     */         } 
/* 344 */         da[dp] = Surrogate.high(uc);
/* 345 */         da[dp + 1] = Surrogate.low(uc);
/* 346 */         this.error = null;
/* 347 */         return 2;
/*     */       } 
/* 349 */       this.error = CoderResult.unmappableForLength(len);
/* 350 */       return -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\interna\\util\Surrogate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */