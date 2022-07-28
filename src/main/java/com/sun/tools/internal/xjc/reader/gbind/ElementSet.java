/*    */ package com.sun.tools.internal.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
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
/*    */ interface ElementSet
/*    */   extends Iterable<Element>
/*    */ {
/* 42 */   public static final ElementSet EMPTY_SET = new ElementSet()
/*    */     {
/*    */       public void addNext(Element element) {}
/*    */ 
/*    */       
/*    */       public boolean contains(ElementSet element) {
/* 48 */         return (this == element);
/*    */       }
/*    */       
/*    */       public Iterator<Element> iterator() {
/* 52 */         return Collections.<Element>emptySet().iterator();
/*    */       }
/*    */     };
/*    */   
/*    */   void addNext(Element paramElement);
/*    */   
/*    */   boolean contains(ElementSet paramElementSet);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\gbind\ElementSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */