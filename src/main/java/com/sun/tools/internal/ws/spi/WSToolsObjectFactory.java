/*    */ package com.sun.tools.internal.ws.spi;
/*    */ 
/*    */ import com.sun.tools.internal.ws.util.WSToolsObjectFactoryImpl;
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
/*    */ 
/*    */ public abstract class WSToolsObjectFactory
/*    */ {
/* 41 */   private static final WSToolsObjectFactory factory = (WSToolsObjectFactory)new WSToolsObjectFactoryImpl();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static WSToolsObjectFactory newInstance() {
/* 48 */     return factory;
/*    */   }
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
/*    */   public abstract boolean wsimport(OutputStream paramOutputStream, Container paramContainer, String[] paramArrayOfString);
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
/*    */   public boolean wsimport(OutputStream logStream, String[] args) {
/* 73 */     return wsimport(logStream, Container.NONE, args);
/*    */   }
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
/*    */   public abstract boolean wsgen(OutputStream paramOutputStream, Container paramContainer, String[] paramArrayOfString);
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
/*    */   public boolean wsgen(OutputStream logStream, String[] args) {
/* 97 */     return wsgen(logStream, Container.NONE, args);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\spi\WSToolsObjectFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */