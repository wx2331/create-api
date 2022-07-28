/*    */ package com.sun.xml.internal.xsom;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface XSModelGroup
/*    */   extends XSComponent, XSTerm, Iterable<XSParticle>
/*    */ {
/*    */   public enum Compositor
/*    */   {
/* 41 */     ALL("all"), CHOICE("choice"), SEQUENCE("sequence"); private final String value;
/*    */     
/*    */     Compositor(String _value) {
/* 44 */       this.value = _value;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 55 */       return this.value;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static final Compositor ALL = Compositor.ALL;
/*    */ 
/*    */ 
/*    */   
/* 65 */   public static final Compositor SEQUENCE = Compositor.SEQUENCE;
/*    */ 
/*    */ 
/*    */   
/* 69 */   public static final Compositor CHOICE = Compositor.CHOICE;
/*    */   
/*    */   Compositor getCompositor();
/*    */   
/*    */   XSParticle getChild(int paramInt);
/*    */   
/*    */   int getSize();
/*    */   
/*    */   XSParticle[] getChildren();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSModelGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */