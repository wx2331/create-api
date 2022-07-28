/*     */ package com.sun.tools.hat.internal.server;
/*     */
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class FinalizerSummaryQuery
/*     */   extends QueryHandler
/*     */ {
/*     */   public void run() {
/*  40 */     Enumeration enumeration = this.snapshot.getFinalizerObjects();
/*  41 */     startHtml("Finalizer Summary");
/*     */
/*  43 */     this.out.println("<p align='center'>");
/*  44 */     this.out.println("<b><a href='/'>All Classes (excluding platform)</a></b>");
/*  45 */     this.out.println("</p>");
/*     */
/*  47 */     printFinalizerSummary(enumeration);
/*  48 */     endHtml();
/*     */   }
/*     */   private static class HistogramElement { private JavaClass clazz;
/*     */
/*     */     public HistogramElement(JavaClass param1JavaClass) {
/*  53 */       this.clazz = param1JavaClass;
/*     */     }
/*     */     private long count;
/*     */     public void updateCount() {
/*  57 */       this.count++;
/*     */     }
/*     */
/*     */     public int compare(HistogramElement param1HistogramElement) {
/*  61 */       long l = param1HistogramElement.count - this.count;
/*  62 */       return (l == 0L) ? 0 : ((l > 0L) ? 1 : -1);
/*     */     }
/*     */
/*     */     public JavaClass getClazz() {
/*  66 */       return this.clazz;
/*     */     }
/*     */
/*     */     public long getCount() {
/*  70 */       return this.count;
/*     */     } }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void printFinalizerSummary(Enumeration<JavaHeapObject> paramEnumeration) {
/*  78 */     byte b1 = 0;
/*  79 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */
/*  81 */     while (paramEnumeration.hasMoreElements()) {
/*  82 */       JavaHeapObject javaHeapObject = paramEnumeration.nextElement();
/*  83 */       b1++;
/*  84 */       JavaClass javaClass = javaHeapObject.getClazz();
/*  85 */       if (!hashMap.containsKey(javaClass)) {
/*  86 */         hashMap.put(javaClass, new HistogramElement(javaClass));
/*     */       }
/*  88 */       HistogramElement histogramElement = (HistogramElement)hashMap.get(javaClass);
/*  89 */       histogramElement.updateCount();
/*     */     }
/*     */
/*  92 */     this.out.println("<p align='center'>");
/*  93 */     this.out.println("<b>");
/*  94 */     this.out.println("Total ");
/*  95 */     if (b1 != 0) {
/*  96 */       this.out.print("<a href='/finalizerObjects/'>instances</a>");
/*     */     } else {
/*  98 */       this.out.print("instances");
/*     */     }
/* 100 */     this.out.println(" pending finalization: ");
/* 101 */     this.out.print(b1);
/* 102 */     this.out.println("</b></p><hr>");
/*     */
/* 104 */     if (b1 == 0) {
/*     */       return;
/*     */     }
/*     */
/*     */
/* 109 */     HistogramElement[] arrayOfHistogramElement = new HistogramElement[hashMap.size()];
/* 110 */     hashMap.values().toArray((Object[])arrayOfHistogramElement);
/* 111 */     Arrays.sort(arrayOfHistogramElement, new Comparator<HistogramElement>() {
/*     */           public int compare(HistogramElement param1HistogramElement1, HistogramElement param1HistogramElement2) {
/* 113 */             return param1HistogramElement1.compare(param1HistogramElement2);
/*     */           }
/*     */         });
/*     */
/* 117 */     this.out.println("<table border=1 align=center>");
/* 118 */     this.out.println("<tr><th>Count</th><th>Class</th></tr>");
/* 119 */     for (byte b2 = 0; b2 < arrayOfHistogramElement.length; b2++) {
/* 120 */       this.out.println("<tr><td>");
/* 121 */       this.out.println(arrayOfHistogramElement[b2].getCount());
/* 122 */       this.out.println("</td><td>");
/* 123 */       printClass(arrayOfHistogramElement[b2].getClazz());
/* 124 */       this.out.println("</td><tr>");
/*     */     }
/* 126 */     this.out.println("</table>");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\FinalizerSummaryQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
