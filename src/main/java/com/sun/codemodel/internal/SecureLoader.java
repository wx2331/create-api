/*    */ package com.sun.codemodel.internal;
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
/*    */ class SecureLoader
/*    */ {
/*    */   static ClassLoader getContextClassLoader() {
/* 37 */     if (System.getSecurityManager() == null) {
/* 38 */       return Thread.currentThread().getContextClassLoader();
/*    */     }
/* 40 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*    */         {
/*    */           public Object run() {
/* 43 */             return Thread.currentThread().getContextClassLoader();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   static ClassLoader getClassClassLoader(final Class c) {
/* 50 */     if (System.getSecurityManager() == null) {
/* 51 */       return c.getClassLoader();
/*    */     }
/* 53 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*    */         {
/*    */           public Object run() {
/* 56 */             return c.getClassLoader();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   static ClassLoader getSystemClassLoader() {
/* 63 */     if (System.getSecurityManager() == null) {
/* 64 */       return ClassLoader.getSystemClassLoader();
/*    */     }
/* 66 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*    */         {
/*    */           public Object run() {
/* 69 */             return ClassLoader.getSystemClassLoader();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   static void setContextClassLoader(final ClassLoader cl) {
/* 76 */     if (System.getSecurityManager() == null) {
/* 77 */       Thread.currentThread().setContextClassLoader(cl);
/*    */     } else {
/* 79 */       AccessController.doPrivileged(new PrivilegedAction()
/*    */           {
/*    */             public Object run() {
/* 82 */               Thread.currentThread().setContextClassLoader(cl);
/* 83 */               return null;
/*    */             }
/*    */           });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\SecureLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */