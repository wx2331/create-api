/*    */ package com.sun.tools.internal.xjc.reader.dtd;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
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
/*    */ final class Block
/*    */ {
/*    */   final boolean isOptional;
/*    */   final boolean isRepeated;
/* 44 */   final Set<Element> elements = new LinkedHashSet<>();
/*    */   
/*    */   Block(boolean optional, boolean repeated) {
/* 47 */     this.isOptional = optional;
/* 48 */     this.isRepeated = repeated;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\Block.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */