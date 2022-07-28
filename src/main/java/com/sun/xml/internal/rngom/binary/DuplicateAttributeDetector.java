/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ import java.util.ArrayList;
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
/*    */ class DuplicateAttributeDetector
/*    */ {
/* 54 */   private List nameClasses = new ArrayList();
/* 55 */   private Alternative alternatives = null;
/*    */   
/*    */   private static class Alternative {
/*    */     private int startIndex;
/*    */     private int endIndex;
/*    */     private Alternative parent;
/*    */     
/*    */     private Alternative(int startIndex, Alternative parent) {
/* 63 */       this.startIndex = startIndex;
/* 64 */       this.endIndex = startIndex;
/* 65 */       this.parent = parent;
/*    */     }
/*    */   }
/*    */   
/*    */   boolean addAttribute(NameClass nc) {
/* 70 */     int lim = this.nameClasses.size();
/* 71 */     for (Alternative a = this.alternatives; a != null; a = a.parent) {
/* 72 */       for (int j = a.endIndex; j < lim; j++) {
/* 73 */         if (nc.hasOverlapWith(this.nameClasses.get(j)))
/* 74 */           return false; 
/* 75 */       }  lim = a.startIndex;
/*    */     } 
/* 77 */     for (int i = 0; i < lim; i++) {
/* 78 */       if (nc.hasOverlapWith(this.nameClasses.get(i)))
/* 79 */         return false; 
/* 80 */     }  this.nameClasses.add(nc);
/* 81 */     return true;
/*    */   }
/*    */   
/*    */   void startChoice() {
/* 85 */     this.alternatives = new Alternative(this.nameClasses.size(), this.alternatives);
/*    */   }
/*    */   
/*    */   void alternative() {
/* 89 */     this.alternatives.endIndex = this.nameClasses.size();
/*    */   }
/*    */   
/*    */   void endChoice() {
/* 93 */     this.alternatives = this.alternatives.parent;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\DuplicateAttributeDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */