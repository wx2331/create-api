/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.ListeningConnector;
/*     */ import com.sun.jdi.connect.Transport;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class GenericListeningConnector
/*     */   extends ConnectorImpl
/*     */   implements ListeningConnector
/*     */ {
/*     */   static final String ARG_ADDRESS = "address";
/*     */   static final String ARG_TIMEOUT = "timeout";
/*     */   Map<Map<String, ? extends Argument>, TransportService.ListenKey> listenMap;
/*     */   TransportService transportService;
/*     */   Transport transport;
/*     */
/*     */   private GenericListeningConnector(TransportService paramTransportService, boolean paramBoolean) {
/*  60 */     this.transportService = paramTransportService;
/*  61 */     this.transport = new Transport() {
/*     */         public String name() {
/*  63 */           return GenericListeningConnector.this.transportService.name();
/*     */         }
/*     */       };
/*     */
/*  67 */     if (paramBoolean) {
/*  68 */       addStringArgument("address",
/*     */
/*  70 */           getString("generic_listening.address.label"),
/*  71 */           getString("generic_listening.address"), "", false);
/*     */     }
/*     */
/*     */
/*     */
/*  76 */     addIntegerArgument("timeout",
/*     */
/*  78 */         getString("generic_listening.timeout.label"),
/*  79 */         getString("generic_listening.timeout"), "", false, 0, 2147483647);
/*     */
/*     */
/*     */
/*     */
/*  84 */     this.listenMap = new HashMap<>(10);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected GenericListeningConnector(TransportService paramTransportService) {
/*  93 */     this(paramTransportService, false);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static GenericListeningConnector create(TransportService paramTransportService) {
/* 101 */     return new GenericListeningConnector(paramTransportService, true);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String startListening(String paramString, Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/* 107 */     TransportService.ListenKey listenKey = this.listenMap.get(paramMap);
/* 108 */     if (listenKey != null) {
/* 109 */       throw new IllegalConnectorArgumentsException("Already listening", new ArrayList(paramMap
/* 110 */             .keySet()));
/*     */     }
/* 112 */     listenKey = this.transportService.startListening(paramString);
/* 113 */     updateArgumentMapIfRequired(paramMap, listenKey);
/* 114 */     this.listenMap.put(paramMap, listenKey);
/* 115 */     return listenKey.address();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String startListening(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/* 122 */     String str = argument("address", paramMap).value();
/* 123 */     return startListening(str, paramMap);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void stopListening(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/* 129 */     TransportService.ListenKey listenKey = this.listenMap.get(paramMap);
/* 130 */     if (listenKey == null) {
/* 131 */       throw new IllegalConnectorArgumentsException("Not listening", new ArrayList(paramMap
/* 132 */             .keySet()));
/*     */     }
/* 134 */     this.transportService.stopListening(listenKey);
/* 135 */     this.listenMap.remove(paramMap);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public VirtualMachine accept(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/*     */     Connection connection;
/* 142 */     String str = argument("timeout", paramMap).value();
/* 143 */     int i = 0;
/* 144 */     if (str.length() > 0) {
/* 145 */       i = Integer.decode(str).intValue();
/*     */     }
/*     */
/* 148 */     TransportService.ListenKey listenKey = this.listenMap.get(paramMap);
/*     */
/* 150 */     if (listenKey != null) {
/* 151 */       connection = this.transportService.accept(listenKey, i, 0L);
/*     */
/*     */
/*     */     }
/*     */     else {
/*     */
/*     */
/* 158 */       startListening(paramMap);
/* 159 */       listenKey = this.listenMap.get(paramMap);
/* 160 */       assert listenKey != null;
/* 161 */       connection = this.transportService.accept(listenKey, i, 0L);
/* 162 */       stopListening(paramMap);
/*     */     }
/* 164 */     return Bootstrap.virtualMachineManager().createVirtualMachine(connection);
/*     */   }
/*     */
/*     */   public boolean supportsMultipleConnections() {
/* 168 */     return this.transportService.capabilities().supportsMultipleConnections();
/*     */   }
/*     */
/*     */   public String name() {
/* 172 */     return this.transport.name() + "Listen";
/*     */   }
/*     */
/*     */   public String description() {
/* 176 */     return this.transportService.description();
/*     */   }
/*     */
/*     */   public Transport transport() {
/* 180 */     return this.transport;
/*     */   }
/*     */
/*     */   protected void updateArgumentMapIfRequired(Map<String, ? extends Argument> paramMap, TransportService.ListenKey paramListenKey) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\GenericListeningConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
