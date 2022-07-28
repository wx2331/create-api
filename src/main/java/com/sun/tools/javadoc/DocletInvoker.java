/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.LanguageVersion;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.javac.file.Locations;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import javax.tools.DocumentationTool;
/*     */ import javax.tools.JavaFileManager;
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
/*     */ public class DocletInvoker
/*     */ {
/*     */   private final Class<?> docletClass;
/*     */   private final String docletClassName;
/*     */   private final ClassLoader appClassLoader;
/*     */   private final Messager messager;
/*     */   private final boolean apiMode;
/*     */   
/*     */   private static class DocletInvokeException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private DocletInvokeException() {}
/*     */   }
/*     */   
/*     */   private String appendPath(String paramString1, String paramString2) {
/*  76 */     if (paramString1 == null || paramString1.length() == 0)
/*  77 */       return (paramString2 == null) ? "." : paramString2; 
/*  78 */     if (paramString2 == null || paramString2.length() == 0) {
/*  79 */       return paramString1;
/*     */     }
/*  81 */     return paramString1 + File.pathSeparator + paramString2;
/*     */   }
/*     */ 
/*     */   
/*     */   public DocletInvoker(Messager paramMessager, Class<?> paramClass, boolean paramBoolean) {
/*  86 */     this.messager = paramMessager;
/*  87 */     this.docletClass = paramClass;
/*  88 */     this.docletClassName = paramClass.getName();
/*  89 */     this.appClassLoader = null;
/*  90 */     this.apiMode = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocletInvoker(Messager paramMessager, JavaFileManager paramJavaFileManager, String paramString1, String paramString2, ClassLoader paramClassLoader, boolean paramBoolean) {
/*  97 */     this.messager = paramMessager;
/*  98 */     this.docletClassName = paramString1;
/*  99 */     this.apiMode = paramBoolean;
/*     */     
/* 101 */     if (paramJavaFileManager != null && paramJavaFileManager.hasLocation(DocumentationTool.Location.DOCLET_PATH)) {
/* 102 */       this.appClassLoader = paramJavaFileManager.getClassLoader(DocumentationTool.Location.DOCLET_PATH);
/*     */     } else {
/*     */       
/* 105 */       String str = null;
/*     */ 
/*     */       
/* 108 */       str = appendPath(System.getProperty("env.class.path"), str);
/* 109 */       str = appendPath(System.getProperty("java.class.path"), str);
/* 110 */       str = appendPath(paramString2, str);
/* 111 */       URL[] arrayOfURL = Locations.pathToURLs(str);
/* 112 */       if (paramClassLoader == null) {
/* 113 */         this.appClassLoader = new URLClassLoader(arrayOfURL, getDelegationClassLoader(paramString1));
/*     */       } else {
/* 115 */         this.appClassLoader = new URLClassLoader(arrayOfURL, paramClassLoader);
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     Class<?> clazz = null;
/*     */     try {
/* 121 */       clazz = this.appClassLoader.loadClass(paramString1);
/* 122 */     } catch (ClassNotFoundException classNotFoundException) {
/* 123 */       paramMessager.error(Messager.NOPOS, "main.doclet_class_not_found", new Object[] { paramString1 });
/* 124 */       paramMessager.exit();
/*     */     } 
/* 126 */     this.docletClass = clazz;
/*     */   }
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
/*     */   private ClassLoader getDelegationClassLoader(String paramString) {
/* 146 */     ClassLoader classLoader1 = Thread.currentThread().getContextClassLoader();
/* 147 */     ClassLoader classLoader2 = ClassLoader.getSystemClassLoader();
/* 148 */     if (classLoader2 == null)
/* 149 */       return classLoader1; 
/* 150 */     if (classLoader1 == null) {
/* 151 */       return classLoader2;
/*     */     }
/*     */     
/*     */     try {
/* 155 */       classLoader2.loadClass(paramString);
/*     */       try {
/* 157 */         classLoader1.loadClass(paramString);
/* 158 */       } catch (ClassNotFoundException classNotFoundException) {
/* 159 */         return classLoader2;
/*     */       } 
/* 161 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 166 */       if (getClass() == classLoader2.loadClass(getClass().getName())) {
/*     */         try {
/* 168 */           if (getClass() != classLoader1.loadClass(getClass().getName()))
/* 169 */             return classLoader2; 
/* 170 */         } catch (ClassNotFoundException classNotFoundException) {
/* 171 */           return classLoader2;
/*     */         } 
/*     */       }
/* 174 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/* 177 */     return classLoader1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean start(RootDoc paramRootDoc) {
/*     */     Object object;
/* 185 */     String str = "start";
/* 186 */     Class[] arrayOfClass = { RootDoc.class };
/* 187 */     Object[] arrayOfObject = { paramRootDoc };
/*     */     try {
/* 189 */       object = invoke(str, null, arrayOfClass, arrayOfObject);
/* 190 */     } catch (DocletInvokeException docletInvokeException) {
/* 191 */       return false;
/*     */     } 
/* 193 */     if (object instanceof Boolean) {
/* 194 */       return ((Boolean)object).booleanValue();
/*     */     }
/* 196 */     this.messager.error(Messager.NOPOS, "main.must_return_boolean", new Object[] { this.docletClassName, str });
/*     */     
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int optionLength(String paramString) {
/*     */     Object object;
/* 209 */     String str = "optionLength";
/* 210 */     Class[] arrayOfClass = { String.class };
/* 211 */     Object[] arrayOfObject = { paramString };
/*     */     try {
/* 213 */       object = invoke(str, new Integer(0), arrayOfClass, arrayOfObject);
/* 214 */     } catch (DocletInvokeException docletInvokeException) {
/* 215 */       return -1;
/*     */     } 
/* 217 */     if (object instanceof Integer) {
/* 218 */       return ((Integer)object).intValue();
/*     */     }
/* 220 */     this.messager.error(Messager.NOPOS, "main.must_return_int", new Object[] { this.docletClassName, str });
/*     */     
/* 222 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validOptions(List<String[]> paramList) {
/*     */     Object object;
/* 232 */     String[][] arrayOfString = (String[][])paramList.toArray((Object[])new String[paramList.length()][]);
/* 233 */     String str = "validOptions";
/* 234 */     Messager messager = this.messager;
/* 235 */     Class[] arrayOfClass = { String[][].class, DocErrorReporter.class };
/* 236 */     Object[] arrayOfObject = { arrayOfString, messager };
/*     */     try {
/* 238 */       object = invoke(str, Boolean.TRUE, arrayOfClass, arrayOfObject);
/* 239 */     } catch (DocletInvokeException docletInvokeException) {
/* 240 */       return false;
/*     */     } 
/* 242 */     if (object instanceof Boolean) {
/* 243 */       return ((Boolean)object).booleanValue();
/*     */     }
/* 245 */     this.messager.error(Messager.NOPOS, "main.must_return_boolean", new Object[] { this.docletClassName, str });
/*     */     
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LanguageVersion languageVersion() {
/*     */     try {
/*     */       Object object;
/* 258 */       String str = "languageVersion";
/* 259 */       Class[] arrayOfClass = new Class[0];
/* 260 */       Object[] arrayOfObject = new Object[0];
/*     */       try {
/* 262 */         object = invoke(str, LanguageVersion.JAVA_1_1, arrayOfClass, arrayOfObject);
/* 263 */       } catch (DocletInvokeException docletInvokeException) {
/* 264 */         return LanguageVersion.JAVA_1_1;
/*     */       } 
/* 266 */       if (object instanceof LanguageVersion) {
/* 267 */         return (LanguageVersion)object;
/*     */       }
/* 269 */       this.messager.error(Messager.NOPOS, "main.must_return_languageversion", new Object[] { this.docletClassName, str });
/*     */       
/* 271 */       return LanguageVersion.JAVA_1_1;
/*     */     }
/* 273 */     catch (NoClassDefFoundError noClassDefFoundError) {
/* 274 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object invoke(String paramString, Object paramObject, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject) throws DocletInvokeException {
/*     */     Method method;
/*     */     try {
/* 286 */       method = this.docletClass.getMethod(paramString, paramArrayOfClass);
/* 287 */     } catch (NoSuchMethodException noSuchMethodException) {
/* 288 */       if (paramObject == null) {
/* 289 */         this.messager.error(Messager.NOPOS, "main.doclet_method_not_found", new Object[] { this.docletClassName, paramString });
/*     */         
/* 291 */         throw new DocletInvokeException();
/*     */       } 
/* 293 */       return paramObject;
/*     */     }
/* 295 */     catch (SecurityException securityException) {
/* 296 */       this.messager.error(Messager.NOPOS, "main.doclet_method_not_accessible", new Object[] { this.docletClassName, paramString });
/*     */       
/* 298 */       throw new DocletInvokeException();
/*     */     } 
/* 300 */     if (!Modifier.isStatic(method.getModifiers())) {
/* 301 */       this.messager.error(Messager.NOPOS, "main.doclet_method_must_be_static", new Object[] { this.docletClassName, paramString });
/*     */       
/* 303 */       throw new DocletInvokeException();
/*     */     } 
/*     */     
/* 306 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 308 */       if (this.appClassLoader != null)
/* 309 */         Thread.currentThread().setContextClassLoader(this.appClassLoader); 
/* 310 */       return method.invoke(null, paramArrayOfObject);
/* 311 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 312 */       this.messager.error(Messager.NOPOS, "main.internal_error_exception_thrown", new Object[] { this.docletClassName, paramString, illegalArgumentException
/* 313 */             .toString() });
/* 314 */       throw new DocletInvokeException();
/* 315 */     } catch (IllegalAccessException illegalAccessException) {
/* 316 */       this.messager.error(Messager.NOPOS, "main.doclet_method_not_accessible", new Object[] { this.docletClassName, paramString });
/*     */       
/* 318 */       throw new DocletInvokeException();
/* 319 */     } catch (NullPointerException nullPointerException) {
/* 320 */       this.messager.error(Messager.NOPOS, "main.internal_error_exception_thrown", new Object[] { this.docletClassName, paramString, nullPointerException
/* 321 */             .toString() });
/* 322 */       throw new DocletInvokeException();
/* 323 */     } catch (InvocationTargetException invocationTargetException) {
/* 324 */       Throwable throwable = invocationTargetException.getTargetException();
/* 325 */       if (this.apiMode)
/* 326 */         throw new ClientCodeException(throwable); 
/* 327 */       if (throwable instanceof OutOfMemoryError) {
/* 328 */         this.messager.error(Messager.NOPOS, "main.out.of.memory", new Object[0]);
/*     */       } else {
/* 330 */         this.messager.error(Messager.NOPOS, "main.exception_thrown", new Object[] { this.docletClassName, paramString, invocationTargetException
/* 331 */               .toString() });
/* 332 */         invocationTargetException.getTargetException().printStackTrace();
/*     */       } 
/* 334 */       throw new DocletInvokeException();
/*     */     } finally {
/* 336 */       Thread.currentThread().setContextClassLoader(classLoader);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\DocletInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */