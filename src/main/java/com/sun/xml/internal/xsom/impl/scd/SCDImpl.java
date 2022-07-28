/*    */ package com.sun.xml.internal.xsom.impl.scd;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.SCD;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SCDImpl
/*    */   extends SCD
/*    */ {
/*    */   private final Step[] steps;
/*    */   private final String text;
/*    */   
/*    */   public SCDImpl(String text, Step[] steps) {
/* 50 */     this.text = text;
/* 51 */     this.steps = steps;
/*    */   }
/*    */   
/*    */   public Iterator<XSComponent> select(Iterator<? extends XSComponent> contextNode) {
/* 55 */     Iterator<? extends XSComponent> iterator = contextNode;
/*    */     
/* 57 */     int len = this.steps.length;
/* 58 */     for (int i = 0; i < len; i++) {
/* 59 */       if (i != 0 && i != len - 1 && !(this.steps[i - 1]).axis.isModelGroup() && (this.steps[i]).axis.isModelGroup())
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 65 */         iterator = new Iterators.Unique<>(new Iterators.Map<XSComponent, XSComponent>(iterator)
/*    */             {
/*    */               protected Iterator<XSComponent> apply(XSComponent u) {
/* 68 */                 return new Iterators.Union<>(
/* 69 */                     Iterators.singleton(u), Axis.INTERMEDIATE_SKIP
/* 70 */                     .iterator(u));
/*    */               }
/*    */             });
/*    */       }
/*    */       
/* 75 */       iterator = this.steps[i].evaluate((Iterator)iterator);
/*    */     } 
/*    */     
/* 78 */     return (Iterator)iterator;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 82 */     return this.text;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\SCDImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */