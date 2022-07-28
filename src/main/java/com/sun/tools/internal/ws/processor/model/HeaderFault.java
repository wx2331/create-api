/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*    */ import javax.xml.namespace.QName;
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
/*    */ public class HeaderFault
/*    */   extends Fault
/*    */ {
/*    */   private QName _message;
/*    */   private String _part;
/*    */   
/*    */   public HeaderFault(Entity entity) {
/* 35 */     super(entity);
/*    */   }
/*    */   
/*    */   public HeaderFault(String name, Entity entity) {
/* 39 */     super(name, entity);
/*    */   }
/*    */   
/*    */   public QName getMessage() {
/* 43 */     return this._message;
/*    */   }
/*    */   
/*    */   public void setMessage(QName message) {
/* 47 */     this._message = message;
/*    */   }
/*    */   
/*    */   public String getPart() {
/* 51 */     return this._part;
/*    */   }
/*    */   
/*    */   public void setPart(String part) {
/* 55 */     this._part = part;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\HeaderFault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */