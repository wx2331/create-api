/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import org.xml.sax.Locator;
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
/*    */ public abstract class GlobalEntity
/*    */   extends Entity
/*    */   implements GloballyKnown
/*    */ {
/*    */   private Defining _defining;
/*    */   private String _name;
/*    */   
/*    */   public GlobalEntity(Defining defining, Locator locator, ErrorReceiver errorReceiver) {
/* 39 */     super(locator);
/* 40 */     this._defining = defining;
/* 41 */     this.errorReceiver = errorReceiver;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 45 */     return this._name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 49 */     this._name = name;
/*    */   }
/*    */   
/*    */   public abstract Kind getKind();
/*    */   
/*    */   public Defining getDefining() {
/* 55 */     return this._defining;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\GlobalEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */