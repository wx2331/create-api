/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.connect.spi.ClosedConnectionException;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SocketConnection
/*     */   extends Connection
/*     */ {
/*     */   private Socket socket;
/*     */   private boolean closed = false;
/*     */   private OutputStream socketOutput;
/*     */   private InputStream socketInput;
/* 373 */   private Object receiveLock = new Object();
/* 374 */   private Object sendLock = new Object();
/* 375 */   private Object closeLock = new Object();
/*     */   
/*     */   SocketConnection(Socket paramSocket) throws IOException {
/* 378 */     this.socket = paramSocket;
/* 379 */     paramSocket.setTcpNoDelay(true);
/* 380 */     this.socketInput = paramSocket.getInputStream();
/* 381 */     this.socketOutput = paramSocket.getOutputStream();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 385 */     synchronized (this.closeLock) {
/* 386 */       if (this.closed) {
/*     */         return;
/*     */       }
/* 389 */       this.socketOutput.close();
/* 390 */       this.socketInput.close();
/* 391 */       this.socket.close();
/* 392 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 397 */     synchronized (this.closeLock) {
/* 398 */       return !this.closed;
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] readPacket() throws IOException {
/* 403 */     if (!isOpen()) {
/* 404 */       throw new ClosedConnectionException("connection is closed");
/*     */     }
/* 406 */     synchronized (this.receiveLock) {
/*     */       int i, j, k, m;
/*     */ 
/*     */       
/*     */       try {
/* 411 */         i = this.socketInput.read();
/* 412 */         j = this.socketInput.read();
/* 413 */         k = this.socketInput.read();
/* 414 */         m = this.socketInput.read();
/* 415 */       } catch (IOException iOException) {
/* 416 */         if (!isOpen()) {
/* 417 */           throw new ClosedConnectionException("connection is closed");
/*     */         }
/* 419 */         throw iOException;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 424 */       if (i < 0) {
/* 425 */         return new byte[0];
/*     */       }
/*     */       
/* 428 */       if (j < 0 || k < 0 || m < 0) {
/* 429 */         throw new IOException("protocol error - premature EOF");
/*     */       }
/*     */       
/* 432 */       int n = i << 24 | j << 16 | k << 8 | m << 0;
/*     */       
/* 434 */       if (n < 0) {
/* 435 */         throw new IOException("protocol error - invalid length");
/*     */       }
/*     */       
/* 438 */       byte[] arrayOfByte = new byte[n];
/* 439 */       arrayOfByte[0] = (byte)i;
/* 440 */       arrayOfByte[1] = (byte)j;
/* 441 */       arrayOfByte[2] = (byte)k;
/* 442 */       arrayOfByte[3] = (byte)m;
/*     */       
/* 444 */       int i1 = 4;
/* 445 */       n -= i1;
/*     */       
/* 447 */       while (n > 0) {
/*     */         int i2;
/*     */         try {
/* 450 */           i2 = this.socketInput.read(arrayOfByte, i1, n);
/* 451 */         } catch (IOException iOException) {
/* 452 */           if (!isOpen()) {
/* 453 */             throw new ClosedConnectionException("connection is closed");
/*     */           }
/* 455 */           throw iOException;
/*     */         } 
/*     */         
/* 458 */         if (i2 < 0) {
/* 459 */           throw new IOException("protocol error - premature EOF");
/*     */         }
/* 461 */         n -= i2;
/* 462 */         i1 += i2;
/*     */       } 
/*     */       
/* 465 */       return arrayOfByte;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writePacket(byte[] paramArrayOfbyte) throws IOException {
/* 470 */     if (!isOpen()) {
/* 471 */       throw new ClosedConnectionException("connection is closed");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 477 */     if (paramArrayOfbyte.length < 11) {
/* 478 */       throw new IllegalArgumentException("packet is insufficient size");
/*     */     }
/* 480 */     int i = paramArrayOfbyte[0] & 0xFF;
/* 481 */     int j = paramArrayOfbyte[1] & 0xFF;
/* 482 */     int k = paramArrayOfbyte[2] & 0xFF;
/* 483 */     int m = paramArrayOfbyte[3] & 0xFF;
/* 484 */     int n = i << 24 | j << 16 | k << 8 | m << 0;
/* 485 */     if (n < 11) {
/* 486 */       throw new IllegalArgumentException("packet is insufficient size");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 492 */     if (n > paramArrayOfbyte.length) {
/* 493 */       throw new IllegalArgumentException("length mis-match");
/*     */     }
/*     */     
/* 496 */     synchronized (this.sendLock) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 502 */         this.socketOutput.write(paramArrayOfbyte, 0, n);
/* 503 */       } catch (IOException iOException) {
/* 504 */         if (!isOpen()) {
/* 505 */           throw new ClosedConnectionException("connection is closed");
/*     */         }
/* 507 */         throw iOException;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SocketConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */