/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JavaLazyReadObject
/*     */   extends JavaHeapObject
/*     */ {
/*     */   private final long offset;
/*     */   
/*     */   protected JavaLazyReadObject(long paramLong) {
/*  47 */     this.offset = paramLong;
/*     */   }
/*     */   
/*     */   public final int getSize() {
/*  51 */     return getValueLength() + getClazz().getMinimumObjectSize();
/*     */   }
/*     */   
/*     */   protected final long getOffset() {
/*  55 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final int getValueLength() {
/*     */     try {
/*  61 */       return readValueLength();
/*  62 */     } catch (IOException iOException) {
/*  63 */       System.err.println("lazy read failed at offset " + this.offset);
/*  64 */       iOException.printStackTrace();
/*  65 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final byte[] getValue() {
/*     */     try {
/*  72 */       return readValue();
/*  73 */     } catch (IOException iOException) {
/*  74 */       System.err.println("lazy read failed at offset " + this.offset);
/*  75 */       iOException.printStackTrace();
/*  76 */       return Snapshot.EMPTY_BYTE_ARRAY;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getId() {
/*     */     try {
/*  83 */       ReadBuffer readBuffer = getClazz().getReadBuffer();
/*  84 */       int i = getClazz().getIdentifierSize();
/*  85 */       if (i == 4) {
/*  86 */         return readBuffer.getInt(this.offset) & Snapshot.SMALL_ID_MASK;
/*     */       }
/*  88 */       return readBuffer.getLong(this.offset);
/*     */     }
/*  90 */     catch (IOException iOException) {
/*  91 */       System.err.println("lazy read failed at offset " + this.offset);
/*  92 */       iOException.printStackTrace();
/*  93 */       return -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract int readValueLength() throws IOException;
/*     */   
/*     */   protected abstract byte[] readValue() throws IOException;
/*     */   
/*     */   protected static Number makeId(long paramLong) {
/* 102 */     if ((paramLong & (Snapshot.SMALL_ID_MASK ^ 0xFFFFFFFFFFFFFFFFL)) == 0L) {
/* 103 */       return new Integer((int)paramLong);
/*     */     }
/* 105 */     return new Long(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static long getIdValue(Number paramNumber) {
/* 111 */     long l = paramNumber.longValue();
/* 112 */     if (paramNumber instanceof Integer) {
/* 113 */       l &= Snapshot.SMALL_ID_MASK;
/*     */     }
/* 115 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final long objectIdAt(int paramInt, byte[] paramArrayOfbyte) {
/* 120 */     int i = getClazz().getIdentifierSize();
/* 121 */     if (i == 4) {
/* 122 */       return intAt(paramInt, paramArrayOfbyte) & Snapshot.SMALL_ID_MASK;
/*     */     }
/* 124 */     return longAt(paramInt, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static byte byteAt(int paramInt, byte[] paramArrayOfbyte) {
/* 130 */     return paramArrayOfbyte[paramInt];
/*     */   }
/*     */   
/*     */   protected static boolean booleanAt(int paramInt, byte[] paramArrayOfbyte) {
/* 134 */     return !((paramArrayOfbyte[paramInt] & 0xFF) == 0);
/*     */   }
/*     */   
/*     */   protected static char charAt(int paramInt, byte[] paramArrayOfbyte) {
/* 138 */     int i = paramArrayOfbyte[paramInt++] & 0xFF;
/* 139 */     int j = paramArrayOfbyte[paramInt++] & 0xFF;
/* 140 */     return (char)((i << 8) + j);
/*     */   }
/*     */   
/*     */   protected static short shortAt(int paramInt, byte[] paramArrayOfbyte) {
/* 144 */     int i = paramArrayOfbyte[paramInt++] & 0xFF;
/* 145 */     int j = paramArrayOfbyte[paramInt++] & 0xFF;
/* 146 */     return (short)((i << 8) + j);
/*     */   }
/*     */   
/*     */   protected static int intAt(int paramInt, byte[] paramArrayOfbyte) {
/* 150 */     int i = paramArrayOfbyte[paramInt++] & 0xFF;
/* 151 */     int j = paramArrayOfbyte[paramInt++] & 0xFF;
/* 152 */     int k = paramArrayOfbyte[paramInt++] & 0xFF;
/* 153 */     int m = paramArrayOfbyte[paramInt++] & 0xFF;
/* 154 */     return (i << 24) + (j << 16) + (k << 8) + m;
/*     */   }
/*     */   
/*     */   protected static long longAt(int paramInt, byte[] paramArrayOfbyte) {
/* 158 */     long l = 0L;
/* 159 */     for (byte b = 0; b < 8; b++) {
/* 160 */       l <<= 8L;
/* 161 */       int i = paramArrayOfbyte[paramInt++] & 0xFF;
/* 162 */       l |= i;
/*     */     } 
/* 164 */     return l;
/*     */   }
/*     */   
/*     */   protected static float floatAt(int paramInt, byte[] paramArrayOfbyte) {
/* 168 */     int i = intAt(paramInt, paramArrayOfbyte);
/* 169 */     return Float.intBitsToFloat(i);
/*     */   }
/*     */   
/*     */   protected static double doubleAt(int paramInt, byte[] paramArrayOfbyte) {
/* 173 */     long l = longAt(paramInt, paramArrayOfbyte);
/* 174 */     return Double.longBitsToDouble(l);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaLazyReadObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */