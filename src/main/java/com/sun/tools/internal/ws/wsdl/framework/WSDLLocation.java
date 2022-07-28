/*    */ package com.sun.tools.internal.ws.wsdl.framework;
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
/*    */ public class WSDLLocation
/*    */ {
/*    */   private LocationContext[] contexts;
/*    */   private int idPos;
/*    */   private LocationContext currentContext;
/*    */   
/*    */   WSDLLocation() {
/* 37 */     reset();
/*    */   }
/*    */   
/*    */   public void push() {
/* 41 */     int max = this.contexts.length;
/* 42 */     this.idPos++;
/* 43 */     if (this.idPos >= max) {
/* 44 */       LocationContext[] newContexts = new LocationContext[max * 2];
/* 45 */       System.arraycopy(this.contexts, 0, newContexts, 0, max);
/* 46 */       this.contexts = newContexts;
/*    */     } 
/* 48 */     this.currentContext = this.contexts[this.idPos];
/* 49 */     if (this.currentContext == null) {
/* 50 */       this.contexts[this.idPos] = this.currentContext = new LocationContext();
/*    */     }
/*    */   }
/*    */   
/*    */   public void pop() {
/* 55 */     this.idPos--;
/* 56 */     if (this.idPos >= 0) {
/* 57 */       this.currentContext = this.contexts[this.idPos];
/*    */     }
/*    */   }
/*    */   
/*    */   public final void reset() {
/* 62 */     this.contexts = new LocationContext[32];
/* 63 */     this.idPos = 0;
/* 64 */     this.contexts[this.idPos] = this.currentContext = new LocationContext();
/*    */   }
/*    */   
/*    */   public String getLocation() {
/* 68 */     return this.currentContext.getLocation();
/*    */   }
/*    */   
/*    */   public void setLocation(String loc) {
/* 72 */     this.currentContext.setLocation(loc);
/*    */   }
/*    */   
/*    */   private static class LocationContext
/*    */   {
/*    */     private String location;
/*    */     
/*    */     private LocationContext() {}
/*    */     
/*    */     void setLocation(String loc) {
/* 82 */       this.location = loc;
/*    */     }
/*    */     
/*    */     String getLocation() {
/* 86 */       return this.location;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\WSDLLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */