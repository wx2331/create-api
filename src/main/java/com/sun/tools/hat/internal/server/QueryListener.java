/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.oql.OQLEngine;
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
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
/*     */ public class QueryListener
/*     */   implements Runnable
/*     */ {
/*     */   private Snapshot snapshot;
/*     */   private OQLEngine engine;
/*     */   private int port;
/*     */   
/*     */   public QueryListener(int paramInt) {
/*  66 */     this.port = paramInt;
/*  67 */     this.snapshot = null;
/*  68 */     this.engine = null;
/*     */   }
/*     */   
/*     */   public void setModel(Snapshot paramSnapshot) {
/*  72 */     this.snapshot = paramSnapshot;
/*  73 */     if (OQLEngine.isOQLSupported()) {
/*  74 */       this.engine = new OQLEngine(paramSnapshot);
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/*  80 */       waitForRequests();
/*  81 */     } catch (IOException iOException) {
/*  82 */       iOException.printStackTrace();
/*  83 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void waitForRequests() throws IOException {
/*  88 */     ServerSocket serverSocket = new ServerSocket(this.port);
/*  89 */     Thread thread = null;
/*     */     while (true) {
/*  91 */       Socket socket = serverSocket.accept();
/*  92 */       Thread thread1 = new Thread(new HttpReader(socket, this.snapshot, this.engine));
/*  93 */       if (this.snapshot == null) {
/*  94 */         thread1.setPriority(6);
/*     */       } else {
/*  96 */         thread1.setPriority(4);
/*  97 */         if (thread != null) {
/*     */           try {
/*  99 */             thread.setPriority(3);
/* 100 */           } catch (Throwable throwable) {}
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 106 */       thread1.start();
/* 107 */       thread = thread1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\QueryListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */