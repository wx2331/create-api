/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaHeapObject;
/*     */ import com.sun.tools.hat.internal.model.JavaThing;
/*     */ import com.sun.tools.hat.internal.model.ReferenceChain;
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
/*     */ class RootsQuery
/*     */   extends QueryHandler
/*     */ {
/*     */   private boolean includeWeak;
/*     */   
/*     */   public RootsQuery(boolean paramBoolean) {
/*  52 */     this.includeWeak = paramBoolean;
/*     */   }
/*     */   
/*     */   public void run() {
/*  56 */     long l = parseHex(this.query);
/*  57 */     JavaHeapObject javaHeapObject = this.snapshot.findThing(l);
/*  58 */     if (javaHeapObject == null) {
/*  59 */       startHtml("Object not found for rootset");
/*  60 */       error("object not found");
/*  61 */       endHtml();
/*     */       return;
/*     */     } 
/*  64 */     if (this.includeWeak) {
/*  65 */       startHtml("Rootset references to " + javaHeapObject + " (includes weak refs)");
/*     */     } else {
/*     */       
/*  68 */       startHtml("Rootset references to " + javaHeapObject + " (excludes weak refs)");
/*     */     } 
/*     */     
/*  71 */     this.out.flush();
/*     */ 
/*     */     
/*  74 */     ReferenceChain[] arrayOfReferenceChain = this.snapshot.rootsetReferencesTo(javaHeapObject, this.includeWeak);
/*  75 */     ArraySorter.sort((Object[])arrayOfReferenceChain, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/*  77 */             ReferenceChain referenceChain1 = (ReferenceChain)param1Object1;
/*  78 */             ReferenceChain referenceChain2 = (ReferenceChain)param1Object2;
/*  79 */             Root root1 = referenceChain1.getObj().getRoot();
/*  80 */             Root root2 = referenceChain2.getObj().getRoot();
/*  81 */             int i = root1.getType() - root2.getType();
/*  82 */             if (i != 0) {
/*  83 */               return -i;
/*     */             }
/*  85 */             return referenceChain1.getDepth() - referenceChain2.getDepth();
/*     */           }
/*     */         });
/*     */     
/*  89 */     this.out.print("<h1>References to ");
/*  90 */     printThing((JavaThing)javaHeapObject);
/*  91 */     this.out.println("</h1>");
/*  92 */     int i = 0;
/*  93 */     for (byte b = 0; b < arrayOfReferenceChain.length; b++) {
/*  94 */       ReferenceChain referenceChain = arrayOfReferenceChain[b];
/*  95 */       Root root = referenceChain.getObj().getRoot();
/*  96 */       if (root.getType() != i) {
/*  97 */         i = root.getType();
/*  98 */         this.out.print("<h2>");
/*  99 */         print(root.getTypeName() + " References");
/* 100 */         this.out.println("</h2>");
/*     */       } 
/* 102 */       this.out.print("<h3>");
/* 103 */       printRoot(root);
/* 104 */       if (root.getReferer() != null) {
/* 105 */         this.out.print("<small> (from ");
/* 106 */         printThingAnchorTag(root.getReferer().getId());
/* 107 */         print(root.getReferer().toString());
/* 108 */         this.out.print(")</a></small>");
/*     */       } 
/*     */       
/* 111 */       this.out.print(" :</h3>");
/* 112 */       while (referenceChain != null) {
/* 113 */         ReferenceChain referenceChain1 = referenceChain.getNext();
/* 114 */         JavaHeapObject javaHeapObject1 = referenceChain.getObj();
/* 115 */         print("--> ");
/* 116 */         printThing((JavaThing)javaHeapObject1);
/* 117 */         if (referenceChain1 != null) {
/* 118 */           print(" (" + javaHeapObject1
/* 119 */               .describeReferenceTo((JavaThing)referenceChain1.getObj(), this.snapshot) + ":)");
/*     */         }
/*     */         
/* 122 */         this.out.println("<br>");
/* 123 */         referenceChain = referenceChain1;
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     this.out.println("<h2>Other queries</h2>");
/*     */     
/* 129 */     if (this.includeWeak) {
/* 130 */       printAnchorStart();
/* 131 */       this.out.print("roots/");
/* 132 */       printHex(l);
/* 133 */       this.out.print("\">");
/* 134 */       this.out.println("Exclude weak refs</a><br>");
/* 135 */       endHtml();
/*     */     } 
/*     */     
/* 138 */     if (!this.includeWeak) {
/* 139 */       printAnchorStart();
/* 140 */       this.out.print("allRoots/");
/* 141 */       printHex(l);
/* 142 */       this.out.print("\">");
/* 143 */       this.out.println("Include weak refs</a><br>");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\RootsQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */