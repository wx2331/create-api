/*    */ package com.sun.tools.hat.internal.parser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.RandomAccessFile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FileReadBuffer
/*    */   implements ReadBuffer
/*    */ {
/*    */   private RandomAccessFile file;
/*    */   
/*    */   FileReadBuffer(RandomAccessFile paramRandomAccessFile) {
/* 48 */     this.file = paramRandomAccessFile;
/*    */   }
/*    */   
/*    */   private void seek(long paramLong) throws IOException {
/* 52 */     this.file.getChannel().position(paramLong);
/*    */   }
/*    */   
/*    */   public synchronized void get(long paramLong, byte[] paramArrayOfbyte) throws IOException {
/* 56 */     seek(paramLong);
/* 57 */     this.file.read(paramArrayOfbyte);
/*    */   }
/*    */   
/*    */   public synchronized char getChar(long paramLong) throws IOException {
/* 61 */     seek(paramLong);
/* 62 */     return this.file.readChar();
/*    */   }
/*    */   
/*    */   public synchronized byte getByte(long paramLong) throws IOException {
/* 66 */     seek(paramLong);
/* 67 */     return (byte)this.file.read();
/*    */   }
/*    */   
/*    */   public synchronized short getShort(long paramLong) throws IOException {
/* 71 */     seek(paramLong);
/* 72 */     return this.file.readShort();
/*    */   }
/*    */   
/*    */   public synchronized int getInt(long paramLong) throws IOException {
/* 76 */     seek(paramLong);
/* 77 */     return this.file.readInt();
/*    */   }
/*    */   
/*    */   public synchronized long getLong(long paramLong) throws IOException {
/* 81 */     seek(paramLong);
/* 82 */     return this.file.readLong();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\parser\FileReadBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */