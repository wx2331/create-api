/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.model.Root;
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class AllRootsQuery
/*     */   extends QueryHandler
/*     */ {
/*     */   public void run() {
/*  53 */     startHtml("All Members of the Rootset");
/*     */     
/*  55 */     Root[] arrayOfRoot = this.snapshot.getRootsArray();
/*  56 */     ArraySorter.sort((Object[])arrayOfRoot, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/*  58 */             Root root1 = (Root)param1Object1;
/*  59 */             Root root2 = (Root)param1Object2;
/*  60 */             int i = root1.getType() - root2.getType();
/*  61 */             if (i != 0) {
/*  62 */               return -i;
/*     */             }
/*  64 */             return root1.getDescription().compareTo(root2.getDescription());
/*     */           }
/*     */         });
/*     */     
/*  68 */     int i = 0;
/*     */     
/*  70 */     for (byte b = 0; b < arrayOfRoot.length; b++) {
/*  71 */       Root root = arrayOfRoot[b];
/*     */       
/*  73 */       if (root.getType() != i) {
/*  74 */         i = root.getType();
/*  75 */         this.out.print("<h2>");
/*  76 */         print(root.getTypeName() + " References");
/*  77 */         this.out.println("</h2>");
/*     */       } 
/*     */       
/*  80 */       printRoot(root);
/*  81 */       if (root.getReferer() != null) {
/*  82 */         this.out.print("<small> (from ");
/*  83 */         printThingAnchorTag(root.getReferer().getId());
/*  84 */         print(root.getReferer().toString());
/*  85 */         this.out.print(")</a></small>");
/*     */       } 
/*  87 */       this.out.print(" :<br>");
/*     */       
/*  89 */       JavaHeapObject javaHeapObject = this.snapshot.findThing(root.getId());
/*  90 */       if (javaHeapObject != null) {
/*  91 */         print("--> ");
/*  92 */         printThing((JavaThing)javaHeapObject);
/*  93 */         this.out.println("<br>");
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     this.out.println("<h2>Other Queries</h2>");
/*  98 */     this.out.println("<ul>");
/*  99 */     this.out.println("<li>");
/* 100 */     printAnchorStart();
/* 101 */     this.out.print("\">");
/* 102 */     print("Show All Classes");
/* 103 */     this.out.println("</a>");
/* 104 */     this.out.println("</ul>");
/*     */     
/* 106 */     endHtml();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\AllRootsQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */