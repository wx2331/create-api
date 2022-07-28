/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaClass;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class InstancesQuery
/*    */   extends QueryHandler
/*    */ {
/*    */   private boolean includeSubclasses;
/*    */   private boolean newObjects;
/*    */   
/*    */   public InstancesQuery(boolean paramBoolean) {
/* 50 */     this.includeSubclasses = paramBoolean;
/*    */   }
/*    */   
/*    */   public InstancesQuery(boolean paramBoolean1, boolean paramBoolean2) {
/* 54 */     this.includeSubclasses = paramBoolean1;
/* 55 */     this.newObjects = paramBoolean2;
/*    */   }
/*    */   public void run() {
/*    */     String str;
/* 59 */     JavaClass javaClass = this.snapshot.findClass(this.query);
/*    */     
/* 61 */     if (this.newObjects) {
/* 62 */       str = "New instances of ";
/*    */     } else {
/* 64 */       str = "Instances of ";
/* 65 */     }  if (this.includeSubclasses) {
/* 66 */       startHtml(str + this.query + " (including subclasses)");
/*    */     } else {
/* 68 */       startHtml(str + this.query);
/*    */     } 
/* 70 */     if (javaClass == null) {
/* 71 */       error("Class not found");
/*    */     } else {
/* 73 */       this.out.print("<strong>");
/* 74 */       printClass(javaClass);
/* 75 */       this.out.print("</strong><br><br>");
/* 76 */       Enumeration<JavaHeapObject> enumeration = javaClass.getInstances(this.includeSubclasses);
/* 77 */       long l1 = 0L;
/* 78 */       long l2 = 0L;
/* 79 */       while (enumeration.hasMoreElements()) {
/* 80 */         JavaHeapObject javaHeapObject = enumeration.nextElement();
/* 81 */         if (this.newObjects && !javaHeapObject.isNew())
/*    */           continue; 
/* 83 */         printThing((JavaThing)javaHeapObject);
/* 84 */         this.out.println("<br>");
/* 85 */         l1 += javaHeapObject.getSize();
/* 86 */         l2++;
/*    */       } 
/* 88 */       this.out.println("<h2>Total of " + l2 + " instances occupying " + l1 + " bytes.</h2>");
/*    */     } 
/* 90 */     endHtml();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\InstancesQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */