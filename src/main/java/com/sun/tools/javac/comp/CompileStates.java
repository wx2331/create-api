/*    */ package com.sun.tools.javac.comp;
/*    */ 
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompileStates
/*    */   extends HashMap<Env<AttrContext>, CompileStates.CompileState>
/*    */ {
/* 42 */   protected static final Context.Key<CompileStates> compileStatesKey = new Context.Key();
/*    */   private static final long serialVersionUID = 1812267524140424433L;
/*    */   protected Context context;
/*    */   
/*    */   public static CompileStates instance(Context paramContext) {
/* 47 */     CompileStates compileStates = (CompileStates)paramContext.get(compileStatesKey);
/* 48 */     if (compileStates == null) {
/* 49 */       compileStates = new CompileStates(paramContext);
/*    */     }
/* 51 */     return compileStates;
/*    */   }
/*    */   
/*    */   public enum CompileState {
/*    */     private final int value;
/* 56 */     INIT(0),
/* 57 */     PARSE(1),
/* 58 */     ENTER(2),
/* 59 */     PROCESS(3),
/* 60 */     ATTR(4),
/* 61 */     FLOW(5),
/* 62 */     TRANSTYPES(6),
/* 63 */     UNLAMBDA(7),
/* 64 */     LOWER(8),
/* 65 */     GENERATE(9);
/*    */     
/*    */     CompileState(int param1Int1) {
/* 68 */       this.value = param1Int1;
/*    */     }
/*    */     public boolean isAfter(CompileState param1CompileState) {
/* 71 */       return (this.value > param1CompileState.value);
/*    */     }
/*    */     public static CompileState max(CompileState param1CompileState1, CompileState param1CompileState2) {
/* 74 */       return (param1CompileState1.value > param1CompileState2.value) ? param1CompileState1 : param1CompileState2;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompileStates(Context paramContext) {
/* 84 */     this.context = paramContext;
/* 85 */     paramContext.put(compileStatesKey, this);
/*    */   }
/*    */   
/*    */   public boolean isDone(Env<AttrContext> paramEnv, CompileState paramCompileState) {
/* 89 */     CompileState compileState = get(paramEnv);
/* 90 */     return (compileState != null && !paramCompileState.isAfter(compileState));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\CompileStates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */