/*    */ package com.sun.tools.internal.jxc;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class SchemaGeneratorFacade
/*    */ {
/*    */   public static void main(String[] args) throws Throwable {
/*    */     try {
/* 39 */       ClassLoader cl = SecureLoader.getClassClassLoader(SchemaGeneratorFacade.class);
/* 40 */       if (cl == null) cl = SecureLoader.getSystemClassLoader();
/*    */       
/* 42 */       Class<?> driver = cl.loadClass("com.sun.tools.internal.jxc.SchemaGenerator");
/* 43 */       Method mainMethod = driver.getDeclaredMethod("main", new Class[] { String[].class });
/*    */       try {
/* 45 */         mainMethod.invoke(null, new Object[] { args });
/* 46 */       } catch (IllegalAccessException e) {
/* 47 */         throw e;
/* 48 */       } catch (InvocationTargetException e) {
/* 49 */         if (e.getTargetException() != null)
/* 50 */           throw e.getTargetException(); 
/*    */       } 
/* 52 */     } catch (UnsupportedClassVersionError e) {
/* 53 */       System.err.println("schemagen requires JDK 6.0 or later. Please download it from http://www.oracle.com/technetwork/java/javase/downloads");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\SchemaGeneratorFacade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */