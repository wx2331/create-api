/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.Transport;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class SocketListeningConnector
/*     */   extends GenericListeningConnector
/*     */ {
/*     */   static final String ARG_PORT = "port";
/*     */   static final String ARG_LOCALADDR = "localAddress";
/*     */
/*     */   public SocketListeningConnector() {
/*  43 */     super(new SocketTransportService());
/*     */
/*  45 */     addIntegerArgument("port",
/*     */
/*  47 */         getString("socket_listening.port.label"),
/*  48 */         getString("socket_listening.port"), "", false, 0, 2147483647);
/*     */
/*     */
/*     */
/*     */
/*  53 */     addStringArgument("localAddress",
/*     */
/*  55 */         getString("socket_listening.localaddr.label"),
/*  56 */         getString("socket_listening.localaddr"), "", false);
/*     */
/*     */
/*     */
/*  60 */     this.transport = new Transport() {
/*     */         public String name() {
/*  62 */           return "dt_socket";
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String startListening(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/*  72 */     String str1 = argument("port", paramMap).value();
/*  73 */     String str2 = argument("localAddress", paramMap).value();
/*     */
/*     */
/*  76 */     if (str1.length() == 0) {
/*  77 */       str1 = "0";
/*     */     }
/*     */
/*  80 */     if (str2.length() > 0) {
/*  81 */       str2 = str2 + ":" + str1;
/*     */     } else {
/*  83 */       str2 = str1;
/*     */     }
/*     */
/*  86 */     return startListening(str2, paramMap);
/*     */   }
/*     */
/*     */   public String name() {
/*  90 */     return "com.sun.jdi.SocketListen";
/*     */   }
/*     */
/*     */   public String description() {
/*  94 */     return getString("socket_listening.description");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected void updateArgumentMapIfRequired(Map<String, ? extends Argument> paramMap, TransportService.ListenKey paramListenKey) {
/* 101 */     if (isWildcardPort(paramMap)) {
/* 102 */       String[] arrayOfString = paramListenKey.address().split(":");
/* 103 */       if (arrayOfString.length > 1) {
/* 104 */         ((Argument)paramMap.get("port")).setValue(arrayOfString[1]);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private boolean isWildcardPort(Map<String, ? extends Argument> paramMap) {
/* 110 */     String str = ((Argument)paramMap.get("port")).value();
/* 111 */     return (str.isEmpty() || Integer.valueOf(str).intValue() == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SocketListeningConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
