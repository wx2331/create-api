/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteBuffer
/*     */ {
/*     */   public byte[] elems;
/*     */   public int length;
/*     */   
/*     */   public ByteBuffer() {
/*  52 */     this(64);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer(int paramInt) {
/*  59 */     this.elems = new byte[paramInt];
/*  60 */     this.length = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendByte(int paramInt) {
/*  66 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length);
/*  67 */     this.elems[this.length++] = (byte)paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  74 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length + paramInt2);
/*  75 */     System.arraycopy(paramArrayOfbyte, paramInt1, this.elems, this.length, paramInt2);
/*  76 */     this.length += paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendBytes(byte[] paramArrayOfbyte) {
/*  82 */     appendBytes(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendChar(int paramInt) {
/*  88 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length + 1);
/*  89 */     this.elems[this.length] = (byte)(paramInt >> 8 & 0xFF);
/*  90 */     this.elems[this.length + 1] = (byte)(paramInt & 0xFF);
/*  91 */     this.length += 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendInt(int paramInt) {
/*  97 */     this.elems = ArrayUtils.ensureCapacity(this.elems, this.length + 3);
/*  98 */     this.elems[this.length] = (byte)(paramInt >> 24 & 0xFF);
/*  99 */     this.elems[this.length + 1] = (byte)(paramInt >> 16 & 0xFF);
/* 100 */     this.elems[this.length + 2] = (byte)(paramInt >> 8 & 0xFF);
/* 101 */     this.elems[this.length + 3] = (byte)(paramInt & 0xFF);
/* 102 */     this.length += 4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendLong(long paramLong) {
/* 108 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8);
/* 109 */     DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
/*     */     try {
/* 111 */       dataOutputStream.writeLong(paramLong);
/* 112 */       appendBytes(byteArrayOutputStream.toByteArray(), 0, 8);
/* 113 */     } catch (IOException iOException) {
/* 114 */       throw new AssertionError("write");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendFloat(float paramFloat) {
/* 121 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4);
/* 122 */     DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
/*     */     try {
/* 124 */       dataOutputStream.writeFloat(paramFloat);
/* 125 */       appendBytes(byteArrayOutputStream.toByteArray(), 0, 4);
/* 126 */     } catch (IOException iOException) {
/* 127 */       throw new AssertionError("write");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendDouble(double paramDouble) {
/* 134 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8);
/* 135 */     DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
/*     */     try {
/* 137 */       dataOutputStream.writeDouble(paramDouble);
/* 138 */       appendBytes(byteArrayOutputStream.toByteArray(), 0, 8);
/* 139 */     } catch (IOException iOException) {
/* 140 */       throw new AssertionError("write");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendName(Name paramName) {
/* 147 */     appendBytes(paramName.getByteArray(), paramName.getByteOffset(), paramName.getByteLength());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 153 */     this.length = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Name toName(Names paramNames) {
/* 159 */     return paramNames.fromUtf(this.elems, 0, this.length);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\ByteBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */