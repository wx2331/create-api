/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class SecureLoader
/*    */ {
/*    */   static ClassLoader getContextClassLoader() {
/* 39 */     if (System.getSecurityManager() == null) {
/* 40 */       return Thread.currentThread().getContextClassLoader();
/*    */     }
/* 42 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*    */         {
/*    */           public Object run() {
/* 45 */             return Thread.currentThread().getContextClassLoader();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   static ClassLoader getClassClassLoader(final Class c) {
/* 52 */     if (System.getSecurityManager() == null) {
/* 53 */       return c.getClassLoader();
/*    */     }
/* 55 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*    */         {
/*    */           public Object run() {
/* 58 */             return c.getClassLoader();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   static ClassLoader getSystemClassLoader() {
/* 65 */     if (System.getSecurityManager() == null) {
/* 66 */       return ClassLoader.getSystemClassLoader();
/*    */     }
/* 68 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*    */         {
/*    */           public Object run() {
/* 71 */             return ClassLoader.getSystemClassLoader();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   static void setContextClassLoader(final ClassLoader cl) {
/* 78 */     if (System.getSecurityManager() == null) {
/* 79 */       Thread.currentThread().setContextClassLoader(cl);
/*    */     } else {
/* 81 */       AccessController.doPrivileged(new PrivilegedAction()
/*    */           {
/*    */             public Object run() {
/* 84 */               Thread.currentThread().setContextClassLoader(cl);
/* 85 */               return null;
/*    */             }
/*    */           });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\fmt\SecureLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */