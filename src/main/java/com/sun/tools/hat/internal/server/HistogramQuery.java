/*    */ package com.sun.tools.hat.internal.server;
/*    */ 
/*    */ import com.sun.tools.hat.internal.model.JavaClass;
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
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
/*    */ public class HistogramQuery
/*    */   extends QueryHandler
/*    */ {
/*    */   public void run() {
/*    */     Comparator<JavaClass> comparator;
/* 45 */     JavaClass[] arrayOfJavaClass = this.snapshot.getClassesArray();
/*    */     
/* 47 */     if (this.query.equals("count")) {
/* 48 */       comparator = new Comparator<JavaClass>()
/*    */         {
/*    */           public int compare(JavaClass param1JavaClass1, JavaClass param1JavaClass2) {
/* 51 */             long l = (param1JavaClass2.getInstancesCount(false) - param1JavaClass1.getInstancesCount(false));
/* 52 */             return (l == 0L) ? 0 : ((l < 0L) ? -1 : 1);
/*    */           }
/*    */         };
/* 55 */     } else if (this.query.equals("class")) {
/* 56 */       comparator = new Comparator<JavaClass>() {
/*    */           public int compare(JavaClass param1JavaClass1, JavaClass param1JavaClass2) {
/* 58 */             return param1JavaClass1.getName().compareTo(param1JavaClass2.getName());
/*    */           }
/*    */         };
/*    */     } else {
/*    */       
/* 63 */       comparator = new Comparator<JavaClass>()
/*    */         {
/*    */           public int compare(JavaClass param1JavaClass1, JavaClass param1JavaClass2) {
/* 66 */             long l = param1JavaClass2.getTotalInstanceSize() - param1JavaClass1.getTotalInstanceSize();
/* 67 */             return (l == 0L) ? 0 : ((l < 0L) ? -1 : 1);
/*    */           }
/*    */         };
/*    */     } 
/* 71 */     Arrays.sort(arrayOfJavaClass, comparator);
/*    */     
/* 73 */     startHtml("Heap Histogram");
/*    */     
/* 75 */     this.out.println("<p align='center'>");
/* 76 */     this.out.println("<b><a href='/'>All Classes (excluding platform)</a></b>");
/* 77 */     this.out.println("</p>");
/*    */     
/* 79 */     this.out.println("<table align=center border=1>");
/* 80 */     this.out.println("<tr><th><a href='/histo/class'>Class</a></th>");
/* 81 */     this.out.println("<th><a href='/histo/count'>Instance Count</a></th>");
/* 82 */     this.out.println("<th><a href='/histo/size'>Total Size</a></th></tr>");
/* 83 */     for (byte b = 0; b < arrayOfJavaClass.length; b++) {
/* 84 */       JavaClass javaClass = arrayOfJavaClass[b];
/* 85 */       this.out.println("<tr><td>");
/* 86 */       printClass(javaClass);
/* 87 */       this.out.println("</td>");
/* 88 */       this.out.println("<td>");
/* 89 */       this.out.println(javaClass.getInstancesCount(false));
/* 90 */       this.out.println("</td>");
/* 91 */       this.out.println("<td>");
/* 92 */       this.out.println(javaClass.getTotalInstanceSize());
/* 93 */       this.out.println("</td></tr>");
/*    */     } 
/* 95 */     this.out.println("</table>");
/*    */     
/* 97 */     endHtml();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\HistogramQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */