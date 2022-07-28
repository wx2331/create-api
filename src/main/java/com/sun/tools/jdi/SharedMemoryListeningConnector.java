/*    */ package com.sun.tools.jdi;
/*    */
/*    */ import com.sun.jdi.connect.Connector;
/*    */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*    */ import com.sun.jdi.connect.Transport;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
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
/*    */ public class SharedMemoryListeningConnector
/*    */   extends GenericListeningConnector
/*    */ {
/*    */   static final String ARG_NAME = "name";
/*    */
/*    */   public SharedMemoryListeningConnector() {
/* 41 */     super(new SharedMemoryTransportService());
/*    */
/* 43 */     addStringArgument("name",
/*    */
/* 45 */         getString("memory_listening.name.label"),
/* 46 */         getString("memory_listening.name"), "", false);
/*    */
/*    */
/*    */
/* 50 */     this.transport = new Transport() {
/*    */         public String name() {
/* 52 */           return "dt_shmem";
/*    */         }
/*    */       };
/*    */   }
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */   public String startListening(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/* 64 */     String str = argument("name", paramMap).value();
/*    */
/*    */
/*    */
/* 68 */     if (str.length() == 0) {
/* 69 */       assert this.transportService instanceof SharedMemoryTransportService;
/* 70 */       SharedMemoryTransportService sharedMemoryTransportService = (SharedMemoryTransportService)this.transportService;
/* 71 */       str = sharedMemoryTransportService.defaultAddress();
/*    */     }
/*    */
/* 74 */     return startListening(str, paramMap);
/*    */   }
/*    */
/*    */   public String name() {
/* 78 */     return "com.sun.jdi.SharedMemoryListen";
/*    */   }
/*    */
/*    */   public String description() {
/* 82 */     return getString("memory_listening.description");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SharedMemoryListeningConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
