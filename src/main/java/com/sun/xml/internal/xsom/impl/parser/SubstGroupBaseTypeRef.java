/*    */ package com.sun.xml.internal.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ import com.sun.xml.internal.xsom.impl.Ref;
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
/*    */ public class SubstGroupBaseTypeRef
/*    */   implements Ref.Type
/*    */ {
/*    */   private final Ref.Element e;
/*    */   
/*    */   public SubstGroupBaseTypeRef(Ref.Element _e) {
/* 41 */     this.e = _e;
/*    */   }
/*    */   
/*    */   public XSType getType() {
/* 45 */     return this.e.get().getType();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\SubstGroupBaseTypeRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */