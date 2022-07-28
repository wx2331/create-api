/*    */ package sun.tools.tree;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CheckContext
/*    */   extends Context
/*    */ {
/* 37 */   public Vset vsBreak = Vset.DEAD_END;
/* 38 */   public Vset vsContinue = Vset.DEAD_END;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Vset vsTryExit = Vset.DEAD_END;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   CheckContext(Context paramContext, Statement paramStatement) {
/* 50 */     super(paramContext, paramStatement);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\CheckContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */