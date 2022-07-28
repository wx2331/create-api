/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.connect.TransportTimeoutException;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
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
/*     */ public class SocketTransportService
/*     */   extends TransportService
/*     */ {
/*  42 */   private ResourceBundle messages = null;
/*     */
/*     */
/*     */   static class SocketListenKey
/*     */     extends ListenKey
/*     */   {
/*     */     ServerSocket ss;
/*     */
/*     */
/*     */     SocketListenKey(ServerSocket param1ServerSocket) {
/*  52 */       this.ss = param1ServerSocket;
/*     */     }
/*     */
/*     */     ServerSocket socket() {
/*  56 */       return this.ss;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public String address() {
/*     */       String str1;
/*  64 */       InetAddress inetAddress = this.ss.getInetAddress();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  72 */       if (inetAddress.isAnyLocalAddress()) {
/*     */         try {
/*  74 */           inetAddress = InetAddress.getLocalHost();
/*  75 */         } catch (UnknownHostException unknownHostException) {
/*  76 */           byte[] arrayOfByte = { Byte.MAX_VALUE, 0, 0, 1 };
/*     */           try {
/*  78 */             inetAddress = InetAddress.getByAddress("127.0.0.1", arrayOfByte);
/*  79 */           } catch (UnknownHostException unknownHostException1) {
/*  80 */             throw new InternalError("unable to get local hostname");
/*     */           }
/*     */         }
/*     */       }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  92 */       String str2 = inetAddress.getHostName();
/*  93 */       String str3 = inetAddress.getHostAddress();
/*  94 */       if (str2.equals(str3)) {
/*  95 */         if (inetAddress instanceof java.net.Inet6Address) {
/*  96 */           str1 = "[" + str3 + "]";
/*     */         } else {
/*  98 */           str1 = str3;
/*     */         }
/*     */       } else {
/* 101 */         str1 = str2;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 108 */       return str1 + ":" + this.ss.getLocalPort();
/*     */     }
/*     */
/*     */     public String toString() {
/* 112 */       return address();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   void handshake(Socket paramSocket, long paramLong) throws IOException {
/* 120 */     paramSocket.setSoTimeout((int)paramLong);
/*     */
/* 122 */     byte[] arrayOfByte1 = "JDWP-Handshake".getBytes("UTF-8");
/* 123 */     paramSocket.getOutputStream().write(arrayOfByte1);
/*     */
/* 125 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
/* 126 */     int i = 0;
/* 127 */     while (i < arrayOfByte1.length) {
/*     */       int j;
/*     */       try {
/* 130 */         j = paramSocket.getInputStream().read(arrayOfByte2, i, arrayOfByte1.length - i);
/* 131 */       } catch (SocketTimeoutException socketTimeoutException) {
/* 132 */         throw new IOException("handshake timeout");
/*     */       }
/* 134 */       if (j < 0) {
/* 135 */         paramSocket.close();
/* 136 */         throw new IOException("handshake failed - connection prematurally closed");
/*     */       }
/* 138 */       i += j;
/*     */     }
/* 140 */     for (byte b = 0; b < arrayOfByte1.length; b++) {
/* 141 */       if (arrayOfByte2[b] != arrayOfByte1[b]) {
/* 142 */         throw new IOException("handshake failed - unrecognized message from target VM");
/*     */       }
/*     */     }
/*     */
/*     */
/* 147 */     paramSocket.setSoTimeout(0);
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
/*     */
/*     */   public String name() {
/* 160 */     return "Socket";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String description() {
/* 167 */     synchronized (this) {
/* 168 */       if (this.messages == null) {
/* 169 */         this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */       }
/*     */     }
/* 172 */     return this.messages.getString("socket_transportservice.description");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public Capabilities capabilities() {
/* 179 */     return new SocketTransportServiceCapabilities();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Connection attach(String paramString, long paramLong1, long paramLong2) throws IOException {
/*     */     String str1, str2;
/*     */     int j;
/* 190 */     if (paramString == null) {
/* 191 */       throw new NullPointerException("address is null");
/*     */     }
/* 193 */     if (paramLong1 < 0L || paramLong2 < 0L) {
/* 194 */       throw new IllegalArgumentException("timeout is negative");
/*     */     }
/*     */
/* 197 */     int i = paramString.indexOf(':');
/*     */
/*     */
/* 200 */     if (i < 0) {
/* 201 */       str1 = InetAddress.getLocalHost().getHostName();
/* 202 */       str2 = paramString;
/*     */     } else {
/* 204 */       str1 = paramString.substring(0, i);
/* 205 */       str2 = paramString.substring(i + 1);
/*     */     }
/*     */
/*     */
/*     */     try {
/* 210 */       j = Integer.decode(str2).intValue();
/* 211 */     } catch (NumberFormatException numberFormatException) {
/* 212 */       throw new IllegalArgumentException("unable to parse port number in address");
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 219 */     InetSocketAddress inetSocketAddress = new InetSocketAddress(str1, j);
/* 220 */     Socket socket = new Socket();
/*     */     try {
/* 222 */       socket.connect(inetSocketAddress, (int)paramLong1);
/* 223 */     } catch (SocketTimeoutException socketTimeoutException) {
/*     */       try {
/* 225 */         socket.close();
/* 226 */       } catch (IOException iOException) {}
/* 227 */       throw new TransportTimeoutException("timed out trying to establish connection");
/*     */     }
/*     */
/*     */
/*     */     try {
/* 232 */       handshake(socket, paramLong2);
/* 233 */     } catch (IOException iOException) {
/*     */       try {
/* 235 */         socket.close();
/* 236 */       } catch (IOException iOException1) {}
/* 237 */       throw iOException;
/*     */     }
/*     */
/* 240 */     return new SocketConnection(socket);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   ListenKey startListening(String paramString, int paramInt) throws IOException {
/*     */     InetSocketAddress inetSocketAddress;
/* 249 */     if (paramString == null) {
/* 250 */       inetSocketAddress = new InetSocketAddress(paramInt);
/*     */     } else {
/* 252 */       inetSocketAddress = new InetSocketAddress(paramString, paramInt);
/*     */     }
/* 254 */     ServerSocket serverSocket = new ServerSocket();
/* 255 */     serverSocket.bind(inetSocketAddress);
/* 256 */     return new SocketListenKey(serverSocket);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public ListenKey startListening(String paramString) throws IOException {
/*     */     int j;
/* 264 */     if (paramString == null || paramString.length() == 0) {
/* 265 */       paramString = "0";
/*     */     }
/*     */
/* 268 */     int i = paramString.indexOf(':');
/* 269 */     String str = null;
/* 270 */     if (i >= 0) {
/* 271 */       str = paramString.substring(0, i);
/* 272 */       paramString = paramString.substring(i + 1);
/*     */     }
/*     */
/*     */
/*     */     try {
/* 277 */       j = Integer.decode(paramString).intValue();
/* 278 */     } catch (NumberFormatException numberFormatException) {
/* 279 */       throw new IllegalArgumentException("unable to parse port number in address");
/*     */     }
/*     */
/*     */
/* 283 */     return startListening(str, j);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public ListenKey startListening() throws IOException {
/* 290 */     return startListening(null, 0);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void stopListening(ListenKey paramListenKey) throws IOException {
/* 297 */     if (!(paramListenKey instanceof SocketListenKey)) {
/* 298 */       throw new IllegalArgumentException("Invalid listener");
/*     */     }
/*     */
/* 301 */     synchronized (paramListenKey) {
/* 302 */       ServerSocket serverSocket = ((SocketListenKey)paramListenKey).socket();
/*     */
/*     */
/*     */
/* 306 */       if (serverSocket.isClosed()) {
/* 307 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
/* 309 */       serverSocket.close();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Connection accept(ListenKey paramListenKey, long paramLong1, long paramLong2) throws IOException {
/*     */     ServerSocket serverSocket;
/*     */     Socket socket;
/* 317 */     if (paramLong1 < 0L || paramLong2 < 0L) {
/* 318 */       throw new IllegalArgumentException("timeout is negative");
/*     */     }
/* 320 */     if (!(paramListenKey instanceof SocketListenKey)) {
/* 321 */       throw new IllegalArgumentException("Invalid listener");
/*     */     }
/*     */
/*     */
/*     */
/*     */
/* 327 */     synchronized (paramListenKey) {
/* 328 */       serverSocket = ((SocketListenKey)paramListenKey).socket();
/* 329 */       if (serverSocket.isClosed()) {
/* 330 */         throw new IllegalArgumentException("Invalid listener");
/*     */       }
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
/* 345 */     serverSocket.setSoTimeout((int)paramLong1);
/*     */
/*     */     try {
/* 348 */       socket = serverSocket.accept();
/* 349 */     } catch (SocketTimeoutException socketTimeoutException) {
/* 350 */       throw new TransportTimeoutException("timeout waiting for connection");
/*     */     }
/*     */
/*     */
/* 354 */     handshake(socket, paramLong2);
/*     */
/* 356 */     return new SocketConnection(socket);
/*     */   }
/*     */
/*     */   public String toString() {
/* 360 */     return name();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SocketTransportService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
