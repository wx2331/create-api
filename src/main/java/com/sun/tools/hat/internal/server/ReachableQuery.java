/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*    */ import com.sun.tools.hat.internal.model.JavaThing;
/*    */ import com.sun.tools.hat.internal.model.ReachableObjects;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ReachableQuery
/*    */   extends QueryHandler
/*    */ {
/*    */   public void run() {
/* 51 */     startHtml("Objects Reachable From " + this.query);
/* 52 */     long l1 = parseHex(this.query);
/* 53 */     JavaHeapObject javaHeapObject = this.snapshot.findThing(l1);
/*    */     
/* 55 */     ReachableObjects reachableObjects = new ReachableObjects(javaHeapObject, this.snapshot.getReachableExcludes());
/*    */     
/* 57 */     long l2 = reachableObjects.getTotalSize();
/* 58 */     JavaThing[] arrayOfJavaThing = reachableObjects.getReachables();
/* 59 */     long l3 = arrayOfJavaThing.length;
/*    */     
/* 61 */     this.out.print("<strong>");
/* 62 */     printThing((JavaThing)javaHeapObject);
/* 63 */     this.out.println("</strong><br>");
/* 64 */     this.out.println("<br>");
/* 65 */     for (byte b = 0; b < arrayOfJavaThing.length; b++) {
/* 66 */       printThing(arrayOfJavaThing[b]);
/* 67 */       this.out.println("<br>");
/*    */     } 
/*    */     
/* 70 */     printFields(reachableObjects.getUsedFields(), "Data Members Followed");
/* 71 */     printFields(reachableObjects.getExcludedFields(), "Excluded Data Members");
/* 72 */     this.out.println("<h2>Total of " + l3 + " instances occupying " + l2 + " bytes.</h2>");
/*    */     
/* 74 */     endHtml();
/*    */   }
/*    */   
/*    */   private void printFields(String[] paramArrayOfString, String paramString) {
/* 78 */     if (paramArrayOfString.length == 0) {
/*    */       return;
/*    */     }
/* 81 */     this.out.print("<h3>");
/* 82 */     print(paramString);
/* 83 */     this.out.println("</h3>");
/*    */     
/* 85 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/* 86 */       print(paramArrayOfString[b]);
/* 87 */       this.out.println("<br>");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\ReachableQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */