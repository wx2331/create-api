/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*    */ import com.sun.tools.hat.internal.model.JavaThing;
/*    */ import java.util.Enumeration;
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
/*    */ public class FinalizerObjectsQuery
/*    */   extends QueryHandler
/*    */ {
/*    */   public void run() {
/* 40 */     Enumeration<JavaHeapObject> enumeration = this.snapshot.getFinalizerObjects();
/* 41 */     startHtml("Objects pending finalization");
/*    */     
/* 43 */     this.out.println("<a href='/finalizerSummary/'>Finalizer summary</a>");
/*    */     
/* 45 */     this.out.println("<h1>Objects pending finalization</h1>");
/*    */     
/* 47 */     while (enumeration.hasMoreElements()) {
/* 48 */       printThing((JavaThing)enumeration.nextElement());
/* 49 */       this.out.println("<br>");
/*    */     } 
/*    */     
/* 52 */     endHtml();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\FinalizerObjectsQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */