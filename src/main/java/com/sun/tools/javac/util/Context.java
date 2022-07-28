/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Context
/*     */ {
/*     */   public static interface Factory<T>
/*     */   {
/*     */     T make(Context param1Context);
/*     */   }
/*     */   
/*     */   public static class Key<T> {}
/*     */   
/* 122 */   private Map<Key<?>, Object> ht = new HashMap<>(); private Map<Key<?>, Factory<?>> ft;
/*     */   private Map<Class<?>, Key<?>> kt;
/*     */   
/*     */   public <T> void put(Key<T> paramKey, Factory<T> paramFactory) {
/* 126 */     checkState(this.ht);
/* 127 */     Object object = this.ht.put(paramKey, paramFactory);
/* 128 */     if (object != null)
/* 129 */       throw new AssertionError("duplicate context value"); 
/* 130 */     checkState(this.ft);
/* 131 */     this.ft.put(paramKey, paramFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void put(Key<T> paramKey, T paramT) {
/* 136 */     if (paramT instanceof Factory)
/* 137 */       throw new AssertionError("T extends Context.Factory"); 
/* 138 */     checkState(this.ht);
/* 139 */     Object object = this.ht.put(paramKey, paramT);
/* 140 */     if (object != null && !(object instanceof Factory) && object != paramT && paramT != null) {
/* 141 */       throw new AssertionError("duplicate context value");
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> T get(Key<T> paramKey) {
/* 146 */     checkState(this.ht);
/* 147 */     Object object = this.ht.get(paramKey);
/* 148 */     if (object instanceof Factory) {
/* 149 */       Factory factory = (Factory)object;
/* 150 */       object = factory.make(this);
/* 151 */       if (object instanceof Factory)
/* 152 */         throw new AssertionError("T extends Context.Factory"); 
/* 153 */       Assert.check((this.ht.get(paramKey) == object));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     return uncheckedCast(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Context() {
/* 169 */     this.ft = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     this.kt = new HashMap<>(); } public Context(Context paramContext) { this.ft = new HashMap<>(); this.kt = new HashMap<>();
/*     */     this.kt.putAll(paramContext.kt);
/*     */     this.ft.putAll(paramContext.ft);
/* 183 */     this.ht.putAll(paramContext.ft); } private <T> Key<T> key(Class<T> paramClass) { checkState(this.kt);
/* 184 */     Key<?> key = uncheckedCast(this.kt.get(paramClass));
/* 185 */     if (key == null) {
/* 186 */       key = new Key();
/* 187 */       this.kt.put(paramClass, key);
/*     */     } 
/* 189 */     return (Key)key; }
/*     */ 
/*     */   
/*     */   public <T> T get(Class<T> paramClass) {
/* 193 */     return get(key(paramClass));
/*     */   }
/*     */   
/*     */   public <T> void put(Class<T> paramClass, T paramT) {
/* 197 */     put(key(paramClass), paramT);
/*     */   }
/*     */   public <T> void put(Class<T> paramClass, Factory<T> paramFactory) {
/* 200 */     put(key(paramClass), paramFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T uncheckedCast(Object paramObject) {
/* 209 */     return (T)paramObject;
/*     */   }
/*     */   
/*     */   public void dump() {
/* 213 */     for (Object object : this.ht.values())
/* 214 */       System.err.println((object == null) ? null : object.getClass()); 
/*     */   }
/*     */   
/*     */   public void clear() {
/* 218 */     this.ht = null;
/* 219 */     this.kt = null;
/* 220 */     this.ft = null;
/*     */   }
/*     */   
/*     */   private static void checkState(Map<?, ?> paramMap) {
/* 224 */     if (paramMap == null)
/* 225 */       throw new IllegalStateException(); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */