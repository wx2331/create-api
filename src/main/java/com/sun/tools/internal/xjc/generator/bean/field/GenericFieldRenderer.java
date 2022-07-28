/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
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
/*    */ public final class GenericFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private Constructor constructor;
/*    */   
/*    */   public GenericFieldRenderer(Class fieldClass) {
/*    */     try {
/* 46 */       this.constructor = fieldClass.getDeclaredConstructor(new Class[] { ClassOutlineImpl.class, CPropertyInfo.class });
/* 47 */     } catch (NoSuchMethodException e) {
/* 48 */       throw new NoSuchMethodError(e.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop) {
/*    */     try {
/* 54 */       return this.constructor.newInstance(new Object[] { context, prop });
/* 55 */     } catch (InstantiationException e) {
/* 56 */       throw new InstantiationError(e.getMessage());
/* 57 */     } catch (IllegalAccessException e) {
/* 58 */       throw new IllegalAccessError(e.getMessage());
/* 59 */     } catch (InvocationTargetException e) {
/* 60 */       Throwable t = e.getTargetException();
/* 61 */       if (t instanceof RuntimeException)
/* 62 */         throw (RuntimeException)t; 
/* 63 */       if (t instanceof Error) {
/* 64 */         throw (Error)t;
/*    */       }
/*    */       
/* 67 */       throw new AssertionError(t);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\GenericFieldRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */