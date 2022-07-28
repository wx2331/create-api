/*    */ package com.sun.tools.internal.xjc.reader.dtd;
/*    */ 
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
/*    */ final class Occurence
/*    */   extends Term
/*    */ {
/*    */   final Term term;
/*    */   final boolean isOptional;
/*    */   final boolean isRepeated;
/*    */   
/*    */   Occurence(Term term, boolean optional, boolean repeated) {
/* 42 */     this.term = term;
/* 43 */     this.isOptional = optional;
/* 44 */     this.isRepeated = repeated;
/*    */   }
/*    */   
/*    */   static Term wrap(Term t, int occurence) {
/* 48 */     switch (occurence) {
/*    */       case 3:
/* 50 */         return t;
/*    */       case 1:
/* 52 */         return new Occurence(t, false, true);
/*    */       case 0:
/* 54 */         return new Occurence(t, true, true);
/*    */       case 2:
/* 56 */         return new Occurence(t, true, false);
/*    */     } 
/* 58 */     throw new IllegalArgumentException();
/*    */   }
/*    */ 
/*    */   
/*    */   void normalize(List<Block> r, boolean optional) {
/* 63 */     if (this.isRepeated) {
/* 64 */       Block b = new Block((this.isOptional || optional), true);
/* 65 */       addAllElements(b);
/* 66 */       r.add(b);
/*    */     } else {
/* 68 */       this.term.normalize(r, (optional || this.isOptional));
/*    */     } 
/*    */   }
/*    */   
/*    */   void addAllElements(Block b) {
/* 73 */     this.term.addAllElements(b);
/*    */   }
/*    */   
/*    */   boolean isOptional() {
/* 77 */     return (this.isOptional || this.term.isOptional());
/*    */   }
/*    */   
/*    */   boolean isRepeated() {
/* 81 */     return (this.isRepeated || this.term.isRepeated());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\Occurence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */