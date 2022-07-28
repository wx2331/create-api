/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Context
/*    */ {
/* 41 */   Map<Class<?>, Object> map = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T get(Class<T> paramClass) {
/* 46 */     return (T)this.map.get(paramClass);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T put(Class<T> paramClass, T paramT) {
/* 51 */     return (T)this.map.put(paramClass, paramT);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */