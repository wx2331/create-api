/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ public class Response
/*    */   extends Message
/*    */ {
/*    */   private Map _faultBlocks;
/*    */   
/*    */   public Response(Message entity, ErrorReceiver receiver) {
/* 42 */     super(entity, receiver);
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
/* 73 */     this._faultBlocks = new HashMap<>();
/*    */   }
/*    */   
/*    */   public void addFaultBlock(Block b) {
/*    */     if (this._faultBlocks.containsKey(b.getName()))
/*    */       throw new ModelException("model.uniqueness", new Object[0]); 
/*    */     this._faultBlocks.put(b.getName(), b);
/*    */   }
/*    */   
/*    */   public Iterator getFaultBlocks() {
/*    */     return this._faultBlocks.values().iterator();
/*    */   }
/*    */   
/*    */   public int getFaultBlockCount() {
/*    */     return this._faultBlocks.size();
/*    */   }
/*    */   
/*    */   public Map getFaultBlocksMap() {
/*    */     return this._faultBlocks;
/*    */   }
/*    */   
/*    */   public void setFaultBlocksMap(Map m) {
/*    */     this._faultBlocks = m;
/*    */   }
/*    */   
/*    */   public void accept(ModelVisitor visitor) throws Exception {
/*    */     visitor.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */