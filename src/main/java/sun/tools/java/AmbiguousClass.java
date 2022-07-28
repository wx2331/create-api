/*    */ package sun.tools.java;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AmbiguousClass
/*    */   extends ClassNotFound
/*    */ {
/*    */   public Identifier name1;
/*    */   public Identifier name2;
/*    */   
/*    */   public AmbiguousClass(Identifier paramIdentifier1, Identifier paramIdentifier2) {
/* 49 */     super(paramIdentifier1.getName());
/* 50 */     this.name1 = paramIdentifier1;
/* 51 */     this.name2 = paramIdentifier2;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\AmbiguousClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */