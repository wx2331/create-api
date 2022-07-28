/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.AttachingConnector;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.Transport;
/*     */ import com.sun.jdi.connect.spi.Connection;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class GenericAttachingConnector
/*     */   extends ConnectorImpl
/*     */   implements AttachingConnector
/*     */ {
/*     */   static final String ARG_ADDRESS = "address";
/*     */   static final String ARG_TIMEOUT = "timeout";
/*     */   TransportService transportService;
/*     */   Transport transport;
/*     */
/*     */   private GenericAttachingConnector(TransportService paramTransportService, boolean paramBoolean) {
/*  61 */     this.transportService = paramTransportService;
/*  62 */     this.transport = new Transport()
/*     */       {
/*     */         public String name() {
/*  65 */           return GenericAttachingConnector.this.transportService.name();
/*     */         }
/*     */       };
/*     */
/*  69 */     if (paramBoolean) {
/*  70 */       addStringArgument("address",
/*     */
/*  72 */           getString("generic_attaching.address.label"),
/*  73 */           getString("generic_attaching.address"), "", true);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*  79 */     addIntegerArgument("timeout",
/*     */
/*  81 */         getString("generic_attaching.timeout.label"),
/*  82 */         getString("generic_attaching.timeout"), "", false, 0, 2147483647);
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
/*     */   protected GenericAttachingConnector(TransportService paramTransportService) {
/*  94 */     this(paramTransportService, false);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static GenericAttachingConnector create(TransportService paramTransportService) {
/* 102 */     return new GenericAttachingConnector(paramTransportService, true);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public VirtualMachine attach(String paramString, Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/* 111 */     String str = argument("timeout", paramMap).value();
/* 112 */     int i = 0;
/* 113 */     if (str.length() > 0) {
/* 114 */       i = Integer.decode(str).intValue();
/*     */     }
/* 116 */     Connection connection = this.transportService.attach(paramString, i, 0L);
/* 117 */     return Bootstrap.virtualMachineManager().createVirtualMachine(connection);
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
/*     */   public VirtualMachine attach(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/* 129 */     String str = argument("address", paramMap).value();
/* 130 */     return attach(str, paramMap);
/*     */   }
/*     */
/*     */   public String name() {
/* 134 */     return this.transport.name() + "Attach";
/*     */   }
/*     */
/*     */   public String description() {
/* 138 */     return this.transportService.description();
/*     */   }
/*     */
/*     */   public Transport transport() {
/* 142 */     return this.transport;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\GenericAttachingConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
