/*    */ package com.sun.tools.hat.internal.model;
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
/*    */ public class ReferenceChain
/*    */ {
/*    */   JavaHeapObject obj;
/*    */   ReferenceChain next;
/*    */   
/*    */   public ReferenceChain(JavaHeapObject paramJavaHeapObject, ReferenceChain paramReferenceChain) {
/* 47 */     this.obj = paramJavaHeapObject;
/* 48 */     this.next = paramReferenceChain;
/*    */   }
/*    */   
/*    */   public JavaHeapObject getObj() {
/* 52 */     return this.obj;
/*    */   }
/*    */   
/*    */   public ReferenceChain getNext() {
/* 56 */     return this.next;
/*    */   }
/*    */   
/*    */   public int getDepth() {
/* 60 */     byte b = 1;
/* 61 */     ReferenceChain referenceChain = this.next;
/* 62 */     while (referenceChain != null) {
/* 63 */       b++;
/* 64 */       referenceChain = referenceChain.next;
/*    */     } 
/* 66 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\ReferenceChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */