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
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ProcessAttachingConnector
/*     */   extends ConnectorImpl
/*     */   implements AttachingConnector
/*     */ {
/*     */   static final String ARG_PID = "pid";
/*     */   static final String ARG_TIMEOUT = "timeout";
/*     */   VirtualMachine vm;
/*     */   Transport transport;
/*     */
/*     */   public ProcessAttachingConnector() {
/*  58 */     addStringArgument("pid",
/*     */
/*  60 */         getString("process_attaching.pid.label"),
/*  61 */         getString("process_attaching.pid"), "", true);
/*     */
/*     */
/*     */
/*  65 */     addIntegerArgument("timeout",
/*     */
/*  67 */         getString("generic_attaching.timeout.label"),
/*  68 */         getString("generic_attaching.timeout"), "", false, 0, 2147483647);
/*     */
/*     */
/*     */
/*     */
/*  73 */     this.transport = new Transport() {
/*     */         public String name() {
/*  75 */           return "local";
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public VirtualMachine attach(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/*  87 */     String str1 = argument("pid", paramMap).value();
/*  88 */     String str2 = argument("timeout", paramMap).value();
/*  89 */     int i = 0;
/*  90 */     if (str2.length() > 0) {
/*  91 */       i = Integer.decode(str2).intValue();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*  97 */     String str3 = null;
/*  98 */     VirtualMachine virtualMachine = null;
/*     */     try {
/* 100 */       virtualMachine = VirtualMachine.attach(str1);
/* 101 */       Properties properties = virtualMachine.getAgentProperties();
/* 102 */       str3 = properties.getProperty("sun.jdwp.listenerAddress");
/* 103 */     } catch (Exception exception) {
/* 104 */       throw new IOException(exception.getMessage());
/*     */     } finally {
/* 106 */       if (virtualMachine != null) virtualMachine.detach();
/*     */
/*     */     }
/*     */
/*     */
/* 111 */     if (str3 == null) {
/* 112 */       throw new IOException("Not a debuggee, or not listening for debugger to attach");
/*     */     }
/* 114 */     int j = str3.indexOf(':');
/* 115 */     if (j < 1) {
/* 116 */       throw new IOException("Unable to determine transport endpoint");
/*     */     }
/*     */
/*     */
/*     */
/* 121 */     String str4 = str3.substring(0, j);
/* 122 */     str3 = str3.substring(j + 1, str3.length());
/*     */
/* 124 */     TransportService transportService = null;
/* 125 */     if (str4.equals("dt_socket")) {
/* 126 */       transportService = new SocketTransportService();
/*     */     }
/* 128 */     else if (str4.equals("dt_shmem")) {
/*     */       try {
/* 130 */         Class<?> clazz = Class.forName("com.sun.tools.jdi.SharedMemoryTransportService");
/* 131 */         transportService = (TransportService)clazz.newInstance();
/* 132 */       } catch (Exception exception) {}
/*     */     }
/*     */
/* 135 */     if (transportService == null) {
/* 136 */       throw new IOException("Transport " + str4 + " not recognized");
/*     */     }
/*     */
/*     */
/*     */
/* 141 */     Connection connection = transportService.attach(str3, i, 0L);
/* 142 */     return Bootstrap.virtualMachineManager().createVirtualMachine(connection);
/*     */   }
/*     */
/*     */   public String name() {
/* 146 */     return "com.sun.jdi.ProcessAttach";
/*     */   }
/*     */
/*     */   public String description() {
/* 150 */     return getString("process_attaching.description");
/*     */   }
/*     */
/*     */   public Transport transport() {
/* 154 */     if (this.transport == null) {
/* 155 */       return new Transport() {
/*     */           public String name() {
/* 157 */             return "local";
/*     */           }
/*     */         };
/*     */     }
/* 161 */     return this.transport;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ProcessAttachingConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
