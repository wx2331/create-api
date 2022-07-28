/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.AbstractJavaHeapObjectVisitor;
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObjectVisitor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class RefsByTypeQuery
/*     */   extends QueryHandler
/*     */ {
/*     */   public void run() {
/*  44 */     JavaClass javaClass = this.snapshot.findClass(this.query);
/*  45 */     if (javaClass == null) {
/*  46 */       error("class not found: " + this.query);
/*     */     } else {
/*  48 */       HashMap<Object, Object> hashMap1 = new HashMap<>();
/*  49 */       final HashMap<Object, Object> refereesStat = new HashMap<>();
/*  50 */       Enumeration<JavaHeapObject> enumeration = javaClass.getInstances(false);
/*  51 */       while (enumeration.hasMoreElements()) {
/*  52 */         JavaHeapObject javaHeapObject = enumeration.nextElement();
/*  53 */         if (javaHeapObject.getId() == -1L) {
/*     */           continue;
/*     */         }
/*  56 */         Enumeration<JavaHeapObject> enumeration1 = javaHeapObject.getReferers();
/*  57 */         while (enumeration1.hasMoreElements()) {
/*  58 */           JavaHeapObject javaHeapObject1 = enumeration1.nextElement();
/*  59 */           JavaClass javaClass1 = javaHeapObject1.getClazz();
/*  60 */           if (javaClass1 == null) {
/*  61 */             System.out.println("null class for " + javaHeapObject1);
/*     */             continue;
/*     */           } 
/*  64 */           Long long_ = (Long)hashMap1.get(javaClass1);
/*  65 */           if (long_ == null) {
/*  66 */             long_ = new Long(1L);
/*     */           } else {
/*  68 */             long_ = new Long(long_.longValue() + 1L);
/*     */           } 
/*  70 */           hashMap1.put(javaClass1, long_);
/*     */         } 
/*  72 */         javaHeapObject.visitReferencedObjects((JavaHeapObjectVisitor)new AbstractJavaHeapObjectVisitor()
/*     */             {
/*     */               public void visit(JavaHeapObject param1JavaHeapObject) {
/*  75 */                 JavaClass javaClass = param1JavaHeapObject.getClazz();
/*  76 */                 Long long_ = (Long)refereesStat.get(javaClass);
/*  77 */                 if (long_ == null) {
/*  78 */                   long_ = new Long(1L);
/*     */                 } else {
/*  80 */                   long_ = new Long(long_.longValue() + 1L);
/*     */                 } 
/*  82 */                 refereesStat.put(javaClass, long_);
/*     */               }
/*     */             });
/*     */       } 
/*     */ 
/*     */       
/*  88 */       startHtml("References by Type");
/*  89 */       this.out.println("<p align='center'>");
/*  90 */       printClass(javaClass);
/*  91 */       if (javaClass.getId() != -1L) {
/*  92 */         println("[" + javaClass.getIdString() + "]");
/*     */       }
/*  94 */       this.out.println("</p>");
/*     */       
/*  96 */       if (hashMap1.size() != 0) {
/*  97 */         this.out.println("<h3 align='center'>Referrers by Type</h3>");
/*  98 */         print((Map)hashMap1);
/*     */       } 
/*     */       
/* 101 */       if (hashMap2.size() != 0) {
/* 102 */         this.out.println("<h3 align='center'>Referees by Type</h3>");
/* 103 */         print((Map)hashMap2);
/*     */       } 
/*     */       
/* 106 */       endHtml();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void print(final Map<JavaClass, Long> map) {
/* 111 */     this.out.println("<table border='1' align='center'>");
/* 112 */     Set<JavaClass> set = map.keySet();
/* 113 */     JavaClass[] arrayOfJavaClass = new JavaClass[set.size()];
/* 114 */     set.toArray(arrayOfJavaClass);
/* 115 */     Arrays.sort(arrayOfJavaClass, new Comparator<JavaClass>() {
/*     */           public int compare(JavaClass param1JavaClass1, JavaClass param1JavaClass2) {
/* 117 */             Long long_1 = (Long)map.get(param1JavaClass1);
/* 118 */             Long long_2 = (Long)map.get(param1JavaClass2);
/* 119 */             return long_2.compareTo(long_1);
/*     */           }
/*     */         });
/*     */     
/* 123 */     this.out.println("<tr><th>Class</th><th>Count</th></tr>");
/* 124 */     for (byte b = 0; b < arrayOfJavaClass.length; b++) {
/* 125 */       JavaClass javaClass = arrayOfJavaClass[b];
/* 126 */       this.out.println("<tr><td>");
/* 127 */       this.out.print("<a href='/refsByType/");
/* 128 */       print(javaClass.getIdString());
/* 129 */       this.out.print("'>");
/* 130 */       print(javaClass.getName());
/* 131 */       this.out.println("</a>");
/* 132 */       this.out.println("</td><td>");
/* 133 */       this.out.println(map.get(javaClass));
/* 134 */       this.out.println("</td></tr>");
/*     */     } 
/* 136 */     this.out.println("</table>");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\RefsByTypeQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */