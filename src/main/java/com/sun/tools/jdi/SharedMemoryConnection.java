/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.connect.spi.ClosedConnectionException;
/*     */ import com.sun.jdi.connect.spi.Connection;
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
/*     */ class SharedMemoryConnection
/*     */   extends Connection
/*     */ {
/*     */   private long id;
/*  36 */   private Object receiveLock = new Object();
/*  37 */   private Object sendLock = new Object();
/*  38 */   private Object closeLock = new Object();
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */   
/*     */   private native byte receiveByte0(long paramLong) throws IOException;
/*     */ 
/*     */   
/*     */   private native void sendByte0(long paramLong, byte paramByte) throws IOException;
/*     */   
/*     */   void handshake(long paramLong) throws IOException {
/*  49 */     byte[] arrayOfByte = "JDWP-Handshake".getBytes("UTF-8");
/*     */     byte b;
/*  51 */     for (b = 0; b < arrayOfByte.length; b++) {
/*  52 */       sendByte0(this.id, arrayOfByte[b]);
/*     */     }
/*  54 */     for (b = 0; b < arrayOfByte.length; b++) {
/*  55 */       byte b1 = receiveByte0(this.id);
/*  56 */       if (b1 != arrayOfByte[b])
/*  57 */         throw new IOException("handshake failed - unrecognized message from target VM"); 
/*     */     } 
/*     */   }
/*     */   private native void close0(long paramLong);
/*     */   private native byte[] receivePacket0(long paramLong) throws IOException;
/*     */   private native void sendPacket0(long paramLong, byte[] paramArrayOfbyte) throws IOException;
/*     */   SharedMemoryConnection(long paramLong) throws IOException {
/*  64 */     this.id = paramLong;
/*     */   }
/*     */   
/*     */   public void close() {
/*  68 */     synchronized (this.closeLock) {
/*  69 */       if (!this.closed) {
/*  70 */         close0(this.id);
/*  71 */         this.closed = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/*  77 */     synchronized (this.closeLock) {
/*  78 */       return !this.closed;
/*     */     } 
/*     */   }
/*     */   public byte[] readPacket() throws IOException {
/*     */     byte[] arrayOfByte;
/*  83 */     if (!isOpen()) {
/*  84 */       throw new ClosedConnectionException("Connection closed");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  89 */       synchronized (this.receiveLock) {
/*  90 */         arrayOfByte = receivePacket0(this.id);
/*     */       } 
/*  92 */     } catch (IOException iOException) {
/*  93 */       if (!isOpen()) {
/*  94 */         throw new ClosedConnectionException("Connection closed");
/*     */       }
/*  96 */       throw iOException;
/*     */     } 
/*     */     
/*  99 */     return arrayOfByte;
/*     */   }
/*     */   
/*     */   public void writePacket(byte[] paramArrayOfbyte) throws IOException {
/* 103 */     if (!isOpen()) {
/* 104 */       throw new ClosedConnectionException("Connection closed");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (paramArrayOfbyte.length < 11) {
/* 111 */       throw new IllegalArgumentException("packet is insufficient size");
/*     */     }
/* 113 */     int i = paramArrayOfbyte[0] & 0xFF;
/* 114 */     int j = paramArrayOfbyte[1] & 0xFF;
/* 115 */     int k = paramArrayOfbyte[2] & 0xFF;
/* 116 */     int m = paramArrayOfbyte[3] & 0xFF;
/* 117 */     int n = i << 24 | j << 16 | k << 8 | m << 0;
/* 118 */     if (n < 11) {
/* 119 */       throw new IllegalArgumentException("packet is insufficient size");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     if (n > paramArrayOfbyte.length) {
/* 126 */       throw new IllegalArgumentException("length mis-match");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 131 */       synchronized (this.sendLock) {
/* 132 */         sendPacket0(this.id, paramArrayOfbyte);
/*     */       } 
/* 134 */     } catch (IOException iOException) {
/* 135 */       if (!isOpen()) {
/* 136 */         throw new ClosedConnectionException("Connection closed");
/*     */       }
/* 138 */       throw iOException;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SharedMemoryConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */