/*    */ package com.sun.tools.internal.ws.util;
/*    */ 
/*    */ import com.sun.tools.internal.ws.spi.WSToolsObjectFactory;
/*    */ import com.sun.tools.internal.ws.wscompile.WsgenTool;
/*    */ import com.sun.tools.internal.ws.wscompile.WsimportTool;
/*    */ import com.sun.xml.internal.ws.api.server.Container;
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WSToolsObjectFactoryImpl
/*    */   extends WSToolsObjectFactory
/*    */ {
/*    */   public boolean wsimport(OutputStream logStream, Container container, String[] args) {
/* 44 */     WsimportTool tool = new WsimportTool(logStream, container);
/* 45 */     return tool.run(args);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean wsgen(OutputStream logStream, Container container, String[] args) {
/* 50 */     WsgenTool tool = new WsgenTool(logStream, container);
/* 51 */     return tool.run(args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\w\\util\WSToolsObjectFactoryImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */