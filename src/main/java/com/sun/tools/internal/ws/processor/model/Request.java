/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import com.sun.tools.internal.ws.wsdl.document.Message;
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
/*    */ public class Request
/*    */   extends Message
/*    */ {
/*    */   public Request(Message entity, ErrorReceiver receiver) {
/* 38 */     super(entity, receiver);
/*    */   }
/*    */   
/*    */   public void accept(ModelVisitor visitor) throws Exception {
/* 42 */     visitor.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */