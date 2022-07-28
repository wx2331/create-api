/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.util.Enumeration;
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
/*     */ class InstancesCountQuery
/*     */   extends QueryHandler
/*     */ {
/*     */   private boolean excludePlatform;
/*     */   
/*     */   public InstancesCountQuery(boolean paramBoolean) {
/*  52 */     this.excludePlatform = paramBoolean;
/*     */   }
/*     */   
/*     */   public void run() {
/*  56 */     if (this.excludePlatform) {
/*  57 */       startHtml("Instance Counts for All Classes (excluding platform)");
/*     */     } else {
/*  59 */       startHtml("Instance Counts for All Classes (including platform)");
/*     */     } 
/*     */     
/*  62 */     JavaClass[] arrayOfJavaClass = this.snapshot.getClassesArray();
/*  63 */     if (this.excludePlatform) {
/*  64 */       byte b1 = 0;
/*  65 */       for (byte b2 = 0; b2 < arrayOfJavaClass.length; b2++) {
/*  66 */         if (!PlatformClasses.isPlatformClass(arrayOfJavaClass[b2])) {
/*  67 */           arrayOfJavaClass[b1++] = arrayOfJavaClass[b2];
/*     */         }
/*     */       } 
/*  70 */       JavaClass[] arrayOfJavaClass1 = new JavaClass[b1];
/*  71 */       System.arraycopy(arrayOfJavaClass, 0, arrayOfJavaClass1, 0, arrayOfJavaClass1.length);
/*  72 */       arrayOfJavaClass = arrayOfJavaClass1;
/*     */     } 
/*  74 */     ArraySorter.sort((Object[])arrayOfJavaClass, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/*  76 */             JavaClass javaClass1 = (JavaClass)param1Object1;
/*  77 */             JavaClass javaClass2 = (JavaClass)param1Object2;
/*     */             
/*  79 */             int i = javaClass1.getInstancesCount(false) - javaClass2.getInstancesCount(false);
/*  80 */             if (i != 0) {
/*  81 */               return -i;
/*     */             }
/*  83 */             String str1 = javaClass1.getName();
/*  84 */             String str2 = javaClass2.getName();
/*  85 */             if (str1.startsWith("[") != str2.startsWith("[")) {
/*     */               
/*  87 */               if (str1.startsWith("[")) {
/*  88 */                 return 1;
/*     */               }
/*  90 */               return -1;
/*     */             } 
/*     */             
/*  93 */             return str1.compareTo(str2);
/*     */           }
/*     */         });
/*     */     
/*  97 */     Object object = null;
/*  98 */     long l1 = 0L;
/*  99 */     long l2 = 0L;
/* 100 */     for (byte b = 0; b < arrayOfJavaClass.length; b++) {
/* 101 */       JavaClass javaClass = arrayOfJavaClass[b];
/* 102 */       int i = javaClass.getInstancesCount(false);
/* 103 */       print("" + i);
/* 104 */       printAnchorStart();
/* 105 */       print("instances/" + encodeForURL(arrayOfJavaClass[b]));
/* 106 */       this.out.print("\"> ");
/* 107 */       if (i == 1) {
/* 108 */         print("instance");
/*     */       } else {
/* 110 */         print("instances");
/*     */       } 
/* 112 */       this.out.print("</a> ");
/* 113 */       if (this.snapshot.getHasNewSet()) {
/* 114 */         Enumeration<JavaHeapObject> enumeration = javaClass.getInstances(false);
/* 115 */         byte b1 = 0;
/* 116 */         while (enumeration.hasMoreElements()) {
/* 117 */           JavaHeapObject javaHeapObject = enumeration.nextElement();
/* 118 */           if (javaHeapObject.isNew()) {
/* 119 */             b1++;
/*     */           }
/*     */         } 
/* 122 */         print("(");
/* 123 */         printAnchorStart();
/* 124 */         print("newInstances/" + encodeForURL(arrayOfJavaClass[b]));
/* 125 */         this.out.print("\">");
/* 126 */         print("" + b1 + " new");
/* 127 */         this.out.print("</a>) ");
/*     */       } 
/* 129 */       print("of ");
/* 130 */       printClass(arrayOfJavaClass[b]);
/* 131 */       this.out.println("<br>");
/* 132 */       l2 += i;
/* 133 */       l1 += arrayOfJavaClass[b].getTotalInstanceSize();
/*     */     } 
/* 135 */     this.out.println("<h2>Total of " + l2 + " instances occupying " + l1 + " bytes.</h2>");
/*     */     
/* 137 */     this.out.println("<h2>Other Queries</h2>");
/* 138 */     this.out.println("<ul>");
/*     */     
/* 140 */     this.out.print("<li>");
/* 141 */     printAnchorStart();
/* 142 */     if (!this.excludePlatform) {
/* 143 */       this.out.print("showInstanceCounts/\">");
/* 144 */       print("Show instance counts for all classes (excluding platform)");
/*     */     } else {
/* 146 */       this.out.print("showInstanceCounts/includePlatform/\">");
/* 147 */       print("Show instance counts for all classes (including platform)");
/*     */     } 
/* 149 */     this.out.println("</a>");
/*     */     
/* 151 */     this.out.print("<li>");
/* 152 */     printAnchorStart();
/* 153 */     this.out.print("allClassesWithPlatform/\">");
/* 154 */     print("Show All Classes (including platform)");
/* 155 */     this.out.println("</a>");
/*     */     
/* 157 */     this.out.print("<li>");
/* 158 */     printAnchorStart();
/* 159 */     this.out.print("\">");
/* 160 */     print("Show All Classes (excluding platform)");
/* 161 */     this.out.println("</a>");
/*     */     
/* 163 */     this.out.println("</ul>");
/*     */     
/* 165 */     endHtml();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\InstancesCountQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */