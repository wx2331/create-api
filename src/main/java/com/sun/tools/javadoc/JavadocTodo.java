/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.tools.javac.comp.AttrContext;
/*    */ import com.sun.tools.javac.comp.Env;
/*    */ import com.sun.tools.javac.comp.Todo;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavadocTodo
/*    */   extends Todo
/*    */ {
/*    */   public static void preRegister(Context paramContext) {
/* 44 */     paramContext.put(todoKey, new Context.Factory<Todo>() {
/*    */           public Todo make(Context param1Context) {
/* 46 */             return new JavadocTodo(param1Context);
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   protected JavadocTodo(Context paramContext) {
/* 52 */     super(paramContext);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void append(Env<AttrContext> paramEnv) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean offer(Env<AttrContext> paramEnv) {
/* 62 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\JavadocTodo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */