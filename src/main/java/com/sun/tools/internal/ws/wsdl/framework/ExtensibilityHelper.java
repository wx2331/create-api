/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public class ExtensibilityHelper
/*    */ {
/*    */   private List<TWSDLExtension> _extensions;
/*    */   
/*    */   public void addExtension(TWSDLExtension e) {
/* 45 */     if (this._extensions == null) {
/* 46 */       this._extensions = new ArrayList<>();
/*    */     }
/* 48 */     this._extensions.add(e);
/*    */   }
/*    */   
/*    */   public Iterable<TWSDLExtension> extensions() {
/* 52 */     if (this._extensions == null) {
/* 53 */       return new ArrayList<>();
/*    */     }
/* 55 */     return this._extensions;
/*    */   }
/*    */ 
/*    */   
/*    */   public void withAllSubEntitiesDo(EntityAction action) {
/* 60 */     if (this._extensions != null) {
/* 61 */       for (Iterator<TWSDLExtension> iter = this._extensions.iterator(); iter.hasNext();) {
/* 62 */         action.perform((Entity)iter.next());
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void accept(ExtensionVisitor visitor) throws Exception {
/* 68 */     if (this._extensions != null)
/* 69 */       for (Iterator<TWSDLExtension> iter = this._extensions.iterator(); iter.hasNext();)
/* 70 */         ((ExtensionImpl)iter.next()).accept(visitor);  
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\ExtensibilityHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */