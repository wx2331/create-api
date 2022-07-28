/*    */ package sun.rmi.rmic;
/*    */ 
/*    */ import sun.tools.java.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Names
/*    */ {
/*    */   public static final Identifier stubFor(Identifier paramIdentifier) {
/* 44 */     return Identifier.lookup(paramIdentifier + "_Stub");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Identifier skeletonFor(Identifier paramIdentifier) {
/* 51 */     return Identifier.lookup(paramIdentifier + "_Skel");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Identifier mangleClass(Identifier paramIdentifier) {
/* 71 */     if (!paramIdentifier.isInner()) {
/* 72 */       return paramIdentifier;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 79 */     Identifier identifier = Identifier.lookup(paramIdentifier
/* 80 */         .getFlatName().toString()
/* 81 */         .replace('.', '$'));
/* 82 */     if (identifier.isInner()) {
/* 83 */       throw new Error("failed to mangle inner class name");
/*    */     }
/*    */     
/* 86 */     return Identifier.lookup(paramIdentifier.getQualifier(), identifier);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\Names.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */