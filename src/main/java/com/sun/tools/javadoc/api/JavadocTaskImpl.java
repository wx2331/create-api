/*    */ package com.sun.tools.javadoc.api;
/*    */ 
/*    */ import com.sun.tools.javac.util.ClientCodeException;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javadoc.Start;
/*    */ import java.util.Collections;
/*    */ import java.util.Locale;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import javax.tools.DocumentationTool;
/*    */ import javax.tools.JavaFileObject;
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
/*    */ public class JavadocTaskImpl
/*    */   implements DocumentationTool.DocumentationTask
/*    */ {
/* 48 */   private final AtomicBoolean used = new AtomicBoolean();
/*    */   
/*    */   private final Context context;
/*    */   
/*    */   private Class<?> docletClass;
/*    */   private Iterable<String> options;
/*    */   private Iterable<? extends JavaFileObject> fileObjects;
/*    */   private Locale locale;
/*    */   
/*    */   public JavadocTaskImpl(Context paramContext, Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1) {
/* 58 */     this.context = paramContext;
/* 59 */     this.docletClass = paramClass;
/*    */     
/* 61 */     this
/* 62 */       .options = (paramIterable == null) ? Collections.<String>emptySet() : nullCheck(paramIterable);
/* 63 */     this
/* 64 */       .fileObjects = (paramIterable1 == null) ? Collections.<JavaFileObject>emptySet() : nullCheck(paramIterable1);
/* 65 */     setLocale(Locale.getDefault());
/*    */   }
/*    */   
/*    */   public void setLocale(Locale paramLocale) {
/* 69 */     if (this.used.get())
/* 70 */       throw new IllegalStateException(); 
/* 71 */     this.locale = paramLocale;
/*    */   }
/*    */   
/*    */   public Boolean call() {
/* 75 */     if (!this.used.getAndSet(true)) {
/* 76 */       initContext();
/* 77 */       Start start = new Start(this.context);
/*    */       try {
/* 79 */         return Boolean.valueOf(start.begin(this.docletClass, this.options, this.fileObjects));
/* 80 */       } catch (ClientCodeException clientCodeException) {
/* 81 */         throw new RuntimeException(clientCodeException.getCause());
/*    */       } 
/*    */     } 
/* 84 */     throw new IllegalStateException("multiple calls to method 'call'");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void initContext() {
/* 90 */     this.context.put(Locale.class, this.locale);
/*    */   }
/*    */   
/*    */   private static <T> Iterable<T> nullCheck(Iterable<T> paramIterable) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokeinterface iterator : ()Ljava/util/Iterator;
/*    */     //   6: astore_1
/*    */     //   7: aload_1
/*    */     //   8: invokeinterface hasNext : ()Z
/*    */     //   13: ifeq -> 38
/*    */     //   16: aload_1
/*    */     //   17: invokeinterface next : ()Ljava/lang/Object;
/*    */     //   22: astore_2
/*    */     //   23: aload_2
/*    */     //   24: ifnonnull -> 35
/*    */     //   27: new java/lang/NullPointerException
/*    */     //   30: dup
/*    */     //   31: invokespecial <init> : ()V
/*    */     //   34: athrow
/*    */     //   35: goto -> 7
/*    */     //   38: aload_0
/*    */     //   39: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #94	-> 0
/*    */     //   #95	-> 23
/*    */     //   #96	-> 27
/*    */     //   #97	-> 35
/*    */     //   #98	-> 38
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\api\JavadocTaskImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */