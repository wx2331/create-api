/*    */ package com.sun.tools.javac.comp;
/*    */ 
/*    */ import com.sun.tools.javac.code.Symbol;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import java.util.Collection;
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
/*    */ class TypeEnvs
/*    */ {
/*    */   private static final long serialVersionUID = 571524752489954631L;
/* 44 */   protected static final Context.Key<TypeEnvs> typeEnvsKey = new Context.Key(); private HashMap<Symbol.TypeSymbol, Env<AttrContext>> map;
/*    */   public static TypeEnvs instance(Context paramContext) {
/* 46 */     TypeEnvs typeEnvs = (TypeEnvs)paramContext.get(typeEnvsKey);
/* 47 */     if (typeEnvs == null)
/* 48 */       typeEnvs = new TypeEnvs(paramContext); 
/* 49 */     return typeEnvs;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeEnvs(Context paramContext) {
/* 54 */     this.map = new HashMap<>();
/* 55 */     paramContext.put(typeEnvsKey, this);
/*    */   }
/*    */   
/* 58 */   Env<AttrContext> get(Symbol.TypeSymbol paramTypeSymbol) { return this.map.get(paramTypeSymbol); }
/* 59 */   Env<AttrContext> put(Symbol.TypeSymbol paramTypeSymbol, Env<AttrContext> paramEnv) { return this.map.put(paramTypeSymbol, paramEnv); }
/* 60 */   Env<AttrContext> remove(Symbol.TypeSymbol paramTypeSymbol) { return this.map.remove(paramTypeSymbol); }
/* 61 */   Collection<Env<AttrContext>> values() { return this.map.values(); } void clear() {
/* 62 */     this.map.clear();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\TypeEnvs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */