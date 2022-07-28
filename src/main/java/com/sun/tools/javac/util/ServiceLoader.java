/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.ServiceConfigurationError;
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
/*     */ public final class ServiceLoader<S>
/*     */   implements Iterable<S>
/*     */ {
/*     */   private static final String PREFIX = "META-INF/services/";
/*     */   private Class<S> service;
/*     */   private ClassLoader loader;
/*  73 */   private LinkedHashMap<String, S> providers = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LazyIterator lookupIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reload() {
/*  90 */     this.providers.clear();
/*  91 */     this.lookupIterator = new LazyIterator(this.service, this.loader);
/*     */   }
/*     */   
/*     */   private ServiceLoader(Class<S> paramClass, ClassLoader paramClassLoader) {
/*  95 */     this.service = Objects.<Class<S>>requireNonNull(paramClass, "Service interface cannot be null");
/*  96 */     this.loader = (paramClassLoader == null) ? ClassLoader.getSystemClassLoader() : paramClassLoader;
/*  97 */     reload();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void fail(Class<?> paramClass, String paramString, Throwable paramThrowable) throws ServiceConfigurationError {
/* 103 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void fail(Class<?> paramClass, String paramString) throws ServiceConfigurationError {
/* 110 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void fail(Class<?> paramClass, URL paramURL, int paramInt, String paramString) throws ServiceConfigurationError {
/* 116 */     fail(paramClass, paramURL + ":" + paramInt + ": " + paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int parseLine(Class<?> paramClass, URL paramURL, BufferedReader paramBufferedReader, int paramInt, List<String> paramList) throws IOException, ServiceConfigurationError {
/* 126 */     String str = paramBufferedReader.readLine();
/* 127 */     if (str == null) {
/* 128 */       return -1;
/*     */     }
/* 130 */     int i = str.indexOf('#');
/* 131 */     if (i >= 0) str = str.substring(0, i); 
/* 132 */     str = str.trim();
/* 133 */     int j = str.length();
/* 134 */     if (j != 0) {
/* 135 */       if (str.indexOf(' ') >= 0 || str.indexOf('\t') >= 0)
/* 136 */         fail(paramClass, paramURL, paramInt, "Illegal configuration-file syntax"); 
/* 137 */       int k = str.codePointAt(0);
/* 138 */       if (!Character.isJavaIdentifierStart(k))
/* 139 */         fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);  int m;
/* 140 */       for (m = Character.charCount(k); m < j; m += Character.charCount(k)) {
/* 141 */         k = str.codePointAt(m);
/* 142 */         if (!Character.isJavaIdentifierPart(k) && k != 46)
/* 143 */           fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str); 
/*     */       } 
/* 145 */       if (!this.providers.containsKey(str) && !paramList.contains(str))
/* 146 */         paramList.add(str); 
/*     */     } 
/* 148 */     return paramInt + 1;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private Iterator<String> parse(Class<?> paramClass, URL paramURL) throws ServiceConfigurationError {
/* 171 */     InputStream inputStream = null;
/* 172 */     BufferedReader bufferedReader = null;
/* 173 */     ArrayList<String> arrayList = new ArrayList();
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
/*     */     try {
/* 185 */       URLConnection uRLConnection = paramURL.openConnection();
/* 186 */       uRLConnection.setUseCaches(false);
/* 187 */       inputStream = uRLConnection.getInputStream();
/*     */       
/* 189 */       bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
/* 190 */       int i = 1;
/* 191 */       while ((i = parseLine(paramClass, paramURL, bufferedReader, i, arrayList)) >= 0);
/* 192 */     } catch (IOException iOException) {
/* 193 */       fail(paramClass, "Error reading configuration file", iOException);
/*     */     } finally {
/*     */       try {
/* 196 */         if (bufferedReader != null) bufferedReader.close(); 
/* 197 */         if (inputStream != null) inputStream.close(); 
/* 198 */       } catch (IOException iOException) {
/* 199 */         fail(paramClass, "Error closing configuration file", iOException);
/*     */       } 
/*     */     } 
/* 202 */     return arrayList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   private class LazyIterator
/*     */     implements Iterator<S>
/*     */   {
/*     */     Class<S> service;
/*     */     
/*     */     ClassLoader loader;
/*     */     
/* 213 */     Enumeration<URL> configs = null;
/* 214 */     Iterator<String> pending = null;
/* 215 */     String nextName = null;
/*     */     
/*     */     private LazyIterator(Class<S> param1Class, ClassLoader param1ClassLoader) {
/* 218 */       this.service = param1Class;
/* 219 */       this.loader = param1ClassLoader;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 223 */       if (this.nextName != null) {
/* 224 */         return true;
/*     */       }
/* 226 */       if (this.configs == null) {
/*     */         try {
/* 228 */           String str = "META-INF/services/" + this.service.getName();
/* 229 */           if (this.loader == null)
/* 230 */           { this.configs = ClassLoader.getSystemResources(str); }
/*     */           else
/* 232 */           { this.configs = this.loader.getResources(str); } 
/* 233 */         } catch (IOException iOException) {
/* 234 */           ServiceLoader.fail(this.service, "Error locating configuration files", iOException);
/*     */         } 
/*     */       }
/* 237 */       while (this.pending == null || !this.pending.hasNext()) {
/* 238 */         if (!this.configs.hasMoreElements()) {
/* 239 */           return false;
/*     */         }
/* 241 */         this.pending = ServiceLoader.this.parse(this.service, this.configs.nextElement());
/*     */       } 
/* 243 */       this.nextName = this.pending.next();
/* 244 */       return true;
/*     */     }
/*     */     
/*     */     public S next() {
/* 248 */       if (!hasNext()) {
/* 249 */         throw new NoSuchElementException();
/*     */       }
/* 251 */       String str = this.nextName;
/* 252 */       this.nextName = null;
/* 253 */       Class<?> clazz = null;
/*     */       try {
/* 255 */         clazz = Class.forName(str, false, this.loader);
/* 256 */       } catch (ClassNotFoundException classNotFoundException) {
/* 257 */         ServiceLoader.fail(this.service, "Provider " + str + " not found");
/*     */       } 
/*     */       
/* 260 */       if (!this.service.isAssignableFrom(clazz)) {
/* 261 */         ServiceLoader.fail(this.service, "Provider " + str + " not a subtype");
/*     */       }
/*     */       
/*     */       try {
/* 265 */         S s = this.service.cast(clazz.newInstance());
/* 266 */         ServiceLoader.this.providers.put(str, s);
/* 267 */         return s;
/* 268 */       } catch (Throwable throwable) {
/* 269 */         ServiceLoader.fail(this.service, "Provider " + str + " could not be instantiated: " + throwable, throwable);
/*     */ 
/*     */ 
/*     */         
/* 273 */         throw new Error();
/*     */       } 
/*     */     }
/*     */     public void remove() {
/* 277 */       throw new UnsupportedOperationException();
/*     */     }
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
/*     */   public Iterator<S> iterator() {
/* 323 */     return new Iterator<S>()
/*     */       {
/* 325 */         Iterator<Map.Entry<String, S>> knownProviders = ServiceLoader.this
/* 326 */           .providers.entrySet().iterator();
/*     */         
/*     */         public boolean hasNext() {
/* 329 */           if (this.knownProviders.hasNext())
/* 330 */             return true; 
/* 331 */           return ServiceLoader.this.lookupIterator.hasNext();
/*     */         }
/*     */         
/*     */         public S next() {
/* 335 */           if (this.knownProviders.hasNext())
/* 336 */             return (S)((Map.Entry)this.knownProviders.next()).getValue(); 
/* 337 */           return ServiceLoader.this.lookupIterator.next();
/*     */         }
/*     */         
/*     */         public void remove() {
/* 341 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
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
/*     */ 
/*     */   
/*     */   public static <S> ServiceLoader<S> load(Class<S> paramClass, ClassLoader paramClassLoader) {
/* 365 */     return new ServiceLoader<>(paramClass, paramClassLoader);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> ServiceLoader<S> load(Class<S> paramClass) {
/* 390 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 391 */     return load(paramClass, classLoader);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> ServiceLoader<S> loadInstalled(Class<S> paramClass) {
/* 419 */     ClassLoader classLoader1 = ClassLoader.getSystemClassLoader();
/* 420 */     ClassLoader classLoader2 = null;
/* 421 */     while (classLoader1 != null) {
/* 422 */       classLoader2 = classLoader1;
/* 423 */       classLoader1 = classLoader1.getParent();
/*     */     } 
/* 425 */     return load(paramClass, classLoader2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 434 */     return "java.util.ServiceLoader[" + this.service.getName() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\ServiceLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */