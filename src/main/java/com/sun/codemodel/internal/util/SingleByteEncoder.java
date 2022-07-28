/*     */ package com.sun.codemodel.internal.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import sun.nio.cs.Surrogate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class SingleByteEncoder
/*     */   extends CharsetEncoder
/*     */ {
/*     */   private final short[] index1;
/*     */   private final String index2;
/*     */   private final int mask1;
/*     */   private final int mask2;
/*     */   private final int shift;
/*  51 */   private final Surrogate.Parser sgp = new Surrogate.Parser();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleByteEncoder(Charset cs, short[] index1, String index2, int mask1, int mask2, int shift) {
/*  57 */     super(cs, 1.0F, 1.0F);
/*  58 */     this.index1 = index1;
/*  59 */     this.index2 = index2;
/*  60 */     this.mask1 = mask1;
/*  61 */     this.mask2 = mask2;
/*  62 */     this.shift = shift;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canEncode(char c) {
/*  67 */     char testEncode = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */     
/*  69 */     if (testEncode == '\000') {
/*  70 */       return false;
/*     */     }
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   private CoderResult encodeArrayLoop(CharBuffer src, ByteBuffer dst) {
/*  76 */     char[] sa = src.array();
/*  77 */     int sp = src.arrayOffset() + src.position();
/*  78 */     int sl = src.arrayOffset() + src.limit();
/*  79 */     sp = (sp <= sl) ? sp : sl;
/*  80 */     byte[] da = dst.array();
/*  81 */     int dp = dst.arrayOffset() + dst.position();
/*  82 */     int dl = dst.arrayOffset() + dst.limit();
/*  83 */     dp = (dp <= dl) ? dp : dl;
/*     */     
/*     */     try {
/*  86 */       while (sp < sl) {
/*  87 */         char c = sa[sp];
/*  88 */         if (Surrogate.is(c)) {
/*  89 */           if (this.sgp.parse(c, sa, sp, sl) < 0)
/*  90 */             return this.sgp.error(); 
/*  91 */           return this.sgp.unmappableResult();
/*     */         } 
/*  93 */         if (c >= '￾')
/*  94 */           return CoderResult.unmappableForLength(1); 
/*  95 */         if (dl - dp < 1) {
/*  96 */           return CoderResult.OVERFLOW;
/*     */         }
/*  98 */         char e = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 103 */         if (e == '\000' && c != '\000') {
/* 104 */           return CoderResult.unmappableForLength(1);
/*     */         }
/* 106 */         sp++;
/* 107 */         da[dp++] = (byte)e;
/*     */       } 
/* 109 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 111 */       src.position(sp - src.arrayOffset());
/* 112 */       dst.position(dp - dst.arrayOffset());
/*     */     } 
/*     */   }
/*     */   
/*     */   private CoderResult encodeBufferLoop(CharBuffer src, ByteBuffer dst) {
/* 117 */     int mark = src.position();
/*     */     try {
/* 119 */       while (src.hasRemaining()) {
/* 120 */         char c = src.get();
/* 121 */         if (Surrogate.is(c)) {
/* 122 */           if (this.sgp.parse(c, src) < 0)
/* 123 */             return this.sgp.error(); 
/* 124 */           return this.sgp.unmappableResult();
/*     */         } 
/* 126 */         if (c >= '￾')
/* 127 */           return CoderResult.unmappableForLength(1); 
/* 128 */         if (!dst.hasRemaining()) {
/* 129 */           return CoderResult.OVERFLOW;
/*     */         }
/* 131 */         char e = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 136 */         if (e == '\000' && c != '\000') {
/* 137 */           return CoderResult.unmappableForLength(1);
/*     */         }
/* 139 */         mark++;
/* 140 */         dst.put((byte)e);
/*     */       } 
/* 142 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 144 */       src.position(mark);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected CoderResult encodeLoop(CharBuffer src, ByteBuffer dst) {
/* 149 */     if (src.hasArray() && dst.hasArray()) {
/* 150 */       return encodeArrayLoop(src, dst);
/*     */     }
/* 152 */     return encodeBufferLoop(src, dst);
/*     */   }
/*     */   
/*     */   public byte encode(char inputChar) {
/* 156 */     return (byte)this.index2.charAt(this.index1[(inputChar & this.mask1) >> this.shift] + (inputChar & this.mask2));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\interna\\util\SingleByteEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */