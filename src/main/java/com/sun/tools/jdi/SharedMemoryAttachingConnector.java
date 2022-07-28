/*    */ package com.sun.tools.jdi;
/*    */
/*    */ import com.sun.jdi.VirtualMachine;
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
/*    */ public class SharedMemoryAttachingConnector
/*    */   extends GenericAttachingConnector
/*    */ {
/*    */   static final String ARG_NAME = "name";
/*    */
/*    */   public SharedMemoryAttachingConnector() {
/* 42 */     super(new SharedMemoryTransportService());
/*    */
/* 44 */     addStringArgument("name",
/*    */
/* 46 */         getString("memory_attaching.name.label"),
/* 47 */         getString("memory_attaching.name"), "", true);
/*    */
/*    */
/*    */
/* 51 */     this.transport = new Transport() {
/*    */         public String name() {
/* 53 */           return "dt_shmem";
/*    */         }
/*    */       };
/*    */   }
/*    */
/*    */
/*    */
/*    */
/*    */   public VirtualMachine attach(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException {
/* 62 */     String str = argument("name", paramMap).value();
/* 63 */     return attach(str, paramMap);
/*    */   }
/*    */
/*    */   public String name() {
/* 67 */     return "com.sun.jdi.SharedMemoryAttach";
/*    */   }
/*    */
/*    */   public String description() {
/* 71 */     return getString("memory_attaching.description");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SharedMemoryAttachingConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
