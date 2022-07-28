/*    */ package com.sun.tools.internal.ws.wsdl.document;
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
/*    */ public class Documentation
/*    */ {
/*    */   private String content;
/*    */   
/*    */   public Documentation(String s) {
/* 36 */     this.content = s;
/*    */   }
/*    */   
/*    */   public String getContent() {
/* 40 */     return this.content;
/*    */   }
/*    */   
/*    */   public void setContent(String s) {
/* 44 */     this.content = s;
/*    */   }
/*    */   
/*    */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 48 */     visitor.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Documentation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */