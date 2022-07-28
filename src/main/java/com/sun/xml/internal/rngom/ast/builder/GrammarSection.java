/*    */ package com.sun.xml.internal.rngom.ast.builder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GrammarSection<P extends com.sun.xml.internal.rngom.ast.om.ParsedPattern, E extends com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation, L extends com.sun.xml.internal.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
/*    */ {
/*    */   public static final class Combine
/*    */   {
/*    */     private final String name;
/*    */     
/*    */     private Combine(String name) {
/* 67 */       this.name = name;
/*    */     }
/*    */     public final String toString() {
/* 70 */       return this.name;
/*    */     }
/*    */   }
/*    */   
/* 74 */   public static final Combine COMBINE_CHOICE = new Combine("choice");
/* 75 */   public static final Combine COMBINE_INTERLEAVE = new Combine("interleave");
/*    */   public static final String START = "\000#start\000";
/*    */   
/*    */   void define(String paramString, Combine paramCombine, P paramP, L paramL, A paramA) throws BuildException;
/*    */   
/*    */   void topLevelAnnotation(E paramE) throws BuildException;
/*    */   
/*    */   void topLevelComment(CL paramCL) throws BuildException;
/*    */   
/*    */   Div<P, E, L, A, CL> makeDiv();
/*    */   
/*    */   Include<P, E, L, A, CL> makeInclude();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\ast\builder\GrammarSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */