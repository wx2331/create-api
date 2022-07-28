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
/*    */ abstract class Term
/*    */ {
/* 47 */   static final Term EMPTY = new Term()
/*    */     {
/*    */       void normalize(List<Block> r, boolean optional) {}
/*    */ 
/*    */       
/*    */       void addAllElements(Block b) {}
/*    */       
/*    */       boolean isOptional() {
/* 55 */         return false;
/*    */       }
/*    */       
/*    */       boolean isRepeated() {
/* 59 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   abstract void normalize(List<Block> paramList, boolean paramBoolean);
/*    */   
/*    */   abstract void addAllElements(Block paramBlock);
/*    */   
/*    */   abstract boolean isOptional();
/*    */   
/*    */   abstract boolean isRepeated();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\Term.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */