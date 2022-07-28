/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ public class NameClassWalker
/*    */   implements NameClassVisitor<Void>
/*    */ {
/*    */   public Void visitChoice(NameClass nc1, NameClass nc2) {
/* 56 */     nc1.accept(this);
/* 57 */     return nc2.<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void visitNsName(String ns) {
/* 61 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitNsNameExcept(String ns, NameClass nc) {
/* 65 */     return nc.<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void visitAnyName() {
/* 69 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitAnyNameExcept(NameClass nc) {
/* 73 */     return nc.<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void visitName(QName name) {
/* 77 */     return null;
/*    */   }
/*    */   
/*    */   public Void visitNull() {
/* 81 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\NameClassWalker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */