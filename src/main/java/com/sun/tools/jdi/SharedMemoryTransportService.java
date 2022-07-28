/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.IOException;
/*     */ import java.util.ResourceBundle;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ class SharedMemoryTransportService
/*     */   extends TransportService
/*     */ {
/*  37 */   private ResourceBundle messages = null;
/*     */
/*     */
/*     */   static class SharedMemoryListenKey
/*     */     extends ListenKey
/*     */   {
/*     */     long id;
/*     */     String name;
/*     */
/*     */     SharedMemoryListenKey(long param1Long, String param1String) {
/*  47 */       this.id = param1Long;
/*  48 */       this.name = param1String;
/*     */     }
/*     */
/*     */     long id() {
/*  52 */       return this.id;
/*     */     }
/*     */
/*     */     void setId(long param1Long) {
/*  56 */       this.id = param1Long;
/*     */     }
/*     */
/*     */     public String address() {
/*  60 */       return this.name;
/*     */     }
/*     */
/*     */     public String toString() {
/*  64 */       return address();
/*     */     }
/*     */   }
/*     */
/*     */   SharedMemoryTransportService() {
/*  69 */     System.loadLibrary("dt_shmem");
/*  70 */     initialize();
/*     */   }
/*     */
/*     */   public String name() {
/*  74 */     return "SharedMemory";
/*     */   }
/*     */
/*     */   public String defaultAddress() {
/*  78 */     return "javadebug";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String description() {
/*  85 */     synchronized (this) {
/*  86 */       if (this.messages == null) {
/*  87 */         this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */       }
/*     */     }
/*  90 */     return this.messages.getString("memory_transportservice.description");
/*     */   }
/*     */
/*     */   public Capabilities capabilities() {
/*  94 */     return new SharedMemoryTransportServiceCapabilities();
/*     */   }
/*     */
/*     */
/*     */   private native void initialize();
/*     */
/*     */   private native long startListening0(String paramString) throws IOException;
/*     */
/*     */   private native long attach0(String paramString, long paramLong) throws IOException;
/*     */
/*     */   public Connection attach(String paramString, long paramLong1, long paramLong2) throws IOException {
/* 105 */     if (paramString == null) {
/* 106 */       throw new NullPointerException("address is null");
/*     */     }
/* 108 */     long l = attach0(paramString, paramLong1);
/* 109 */     SharedMemoryConnection sharedMemoryConnection = new SharedMemoryConnection(l);
/* 110 */     sharedMemoryConnection.handshake(paramLong2);
/* 111 */     return sharedMemoryConnection;
/*     */   } private native void stopListening0(long paramLong) throws IOException; private native long accept0(long paramLong1, long paramLong2) throws IOException;
/*     */   private native String name(long paramLong) throws IOException;
/*     */   public ListenKey startListening(String paramString) throws IOException {
/* 115 */     if (paramString == null || paramString.length() == 0) {
/* 116 */       paramString = defaultAddress();
/*     */     }
/* 118 */     long l = startListening0(paramString);
/* 119 */     return new SharedMemoryListenKey(l, name(l));
/*     */   }
/*     */
/*     */   public ListenKey startListening() throws IOException {
/* 123 */     return startListening(null);
/*     */   }
/*     */   public void stopListening(ListenKey paramListenKey) throws IOException {
/*     */     long l;
/* 127 */     if (!(paramListenKey instanceof SharedMemoryListenKey)) {
/* 128 */       throw new IllegalArgumentException("Invalid listener");
/*     */     }
/*     */
/*     */
/* 132 */     SharedMemoryListenKey sharedMemoryListenKey = (SharedMemoryListenKey)paramListenKey;
/* 133 */     synchronized (sharedMemoryListenKey) {
/* 134 */       l = sharedMemoryListenKey.id();
/* 135 */       if (l == 0L) {
/* 136 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
/*     */
/*     */
/* 140 */       sharedMemoryListenKey.setId(0L);
/*     */     }
/* 142 */     stopListening0(l);
/*     */   }
/*     */   public Connection accept(ListenKey paramListenKey, long paramLong1, long paramLong2) throws IOException {
/*     */     long l1;
/* 146 */     if (!(paramListenKey instanceof SharedMemoryListenKey)) {
/* 147 */       throw new IllegalArgumentException("Invalid listener");
/*     */     }
/*     */
/*     */
/* 151 */     SharedMemoryListenKey sharedMemoryListenKey = (SharedMemoryListenKey)paramListenKey;
/* 152 */     synchronized (sharedMemoryListenKey) {
/* 153 */       l1 = sharedMemoryListenKey.id();
/* 154 */       if (l1 == 0L) {
/* 155 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 164 */     long l2 = accept0(l1, paramLong1);
/* 165 */     SharedMemoryConnection sharedMemoryConnection = new SharedMemoryConnection(l2);
/* 166 */     sharedMemoryConnection.handshake(paramLong2);
/* 167 */     return sharedMemoryConnection;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SharedMemoryTransportService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
