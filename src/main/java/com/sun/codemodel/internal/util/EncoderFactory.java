/*    */ package com.sun.codemodel.internal.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
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
/*    */ public class EncoderFactory
/*    */ {
/*    */   public static CharsetEncoder createEncoder(String encodin) {
/* 46 */     Charset cs = Charset.forName(System.getProperty("file.encoding"));
/* 47 */     CharsetEncoder encoder = cs.newEncoder();
/*    */     
/* 49 */     if (cs.getClass().getName().equals("sun.nio.cs.MS1252")) {
/*    */       
/*    */       try {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 58 */         Class<? extends CharsetEncoder> ms1252encoder = (Class)Class.forName("com.sun.codemodel.internal.util.MS1252Encoder");
/* 59 */         Constructor<? extends CharsetEncoder> c = ms1252encoder.getConstructor(new Class[] { Charset.class });
/*    */ 
/*    */         
/* 62 */         return c.newInstance(new Object[] { cs });
/* 63 */       } catch (Throwable t) {
/*    */ 
/*    */ 
/*    */         
/* 67 */         return encoder;
/*    */       } 
/*    */     }
/*    */     
/* 71 */     return encoder;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\interna\\util\EncoderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */