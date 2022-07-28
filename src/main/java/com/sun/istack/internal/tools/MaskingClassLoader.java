/*    */ package com.sun.istack.internal.tools;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class MaskingClassLoader
/*    */   extends ClassLoader
/*    */ {
/*    */   private final String[] masks;
/*    */   
/*    */   public MaskingClassLoader(String... masks) {
/* 44 */     this.masks = masks;
/*    */   }
/*    */   
/*    */   public MaskingClassLoader(Collection<String> masks) {
/* 48 */     this(masks.<String>toArray(new String[masks.size()]));
/*    */   }
/*    */   
/*    */   public MaskingClassLoader(ClassLoader parent, String... masks) {
/* 52 */     super(parent);
/* 53 */     this.masks = masks;
/*    */   }
/*    */   
/*    */   public MaskingClassLoader(ClassLoader parent, Collection<String> masks) {
/* 57 */     this(parent, masks.<String>toArray(new String[masks.size()]));
/*    */   }
/*    */ 
/*    */   
/*    */   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/* 62 */     for (String mask : this.masks) {
/* 63 */       if (name.startsWith(mask)) {
/* 64 */         throw new ClassNotFoundException();
/*    */       }
/*    */     } 
/* 67 */     return super.loadClass(name, resolve);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\istack\internal\tools\MaskingClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */