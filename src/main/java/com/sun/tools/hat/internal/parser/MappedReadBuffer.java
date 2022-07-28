/*     */ package com.sun.tools.hat.internal.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MappedReadBuffer
/*     */   implements ReadBuffer
/*     */ {
/*     */   private MappedByteBuffer buf;
/*     */   
/*     */   MappedReadBuffer(MappedByteBuffer paramMappedByteBuffer) {
/*  49 */     this.buf = paramMappedByteBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   static ReadBuffer create(RandomAccessFile paramRandomAccessFile) throws IOException {
/*  54 */     FileChannel fileChannel = paramRandomAccessFile.getChannel();
/*  55 */     long l = fileChannel.size();
/*     */ 
/*     */     
/*  58 */     if (canUseFileMap() && l <= 2147483647L) {
/*     */       
/*     */       try {
/*  61 */         MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, l);
/*  62 */         fileChannel.close();
/*  63 */         return new MappedReadBuffer(mappedByteBuffer);
/*  64 */       } catch (IOException iOException) {
/*  65 */         iOException.printStackTrace();
/*  66 */         System.err.println("File mapping failed, will use direct read");
/*     */       } 
/*     */     }
/*     */     
/*  70 */     return new FileReadBuffer(paramRandomAccessFile);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canUseFileMap() {
/*  76 */     String str = System.getProperty("jhat.disableFileMap");
/*  77 */     return (str == null || str.equals("false"));
/*     */   }
/*     */   
/*     */   private void seek(long paramLong) throws IOException {
/*  81 */     assert paramLong <= 2147483647L : "position overflow";
/*  82 */     this.buf.position((int)paramLong);
/*     */   }
/*     */   
/*     */   public synchronized void get(long paramLong, byte[] paramArrayOfbyte) throws IOException {
/*  86 */     seek(paramLong);
/*  87 */     this.buf.get(paramArrayOfbyte);
/*     */   }
/*     */   
/*     */   public synchronized char getChar(long paramLong) throws IOException {
/*  91 */     seek(paramLong);
/*  92 */     return this.buf.getChar();
/*     */   }
/*     */   
/*     */   public synchronized byte getByte(long paramLong) throws IOException {
/*  96 */     seek(paramLong);
/*  97 */     return this.buf.get();
/*     */   }
/*     */   
/*     */   public synchronized short getShort(long paramLong) throws IOException {
/* 101 */     seek(paramLong);
/* 102 */     return this.buf.getShort();
/*     */   }
/*     */   
/*     */   public synchronized int getInt(long paramLong) throws IOException {
/* 106 */     seek(paramLong);
/* 107 */     return this.buf.getInt();
/*     */   }
/*     */   
/*     */   public synchronized long getLong(long paramLong) throws IOException {
/* 111 */     seek(paramLong);
/* 112 */     return this.buf.getLong();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\parser\MappedReadBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */