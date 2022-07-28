/*     */ package org.relaxng.datatype.helpers;
/*     */
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ import org.relaxng.datatype.DatatypeLibrary;
/*     */ import org.relaxng.datatype.DatatypeLibraryFactory;
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
/*     */ public class DatatypeLibraryLoader
/*     */   implements DatatypeLibraryFactory
/*     */ {
/*  57 */   private final Service service = new Service(DatatypeLibraryFactory.class);
/*     */
/*     */   public DatatypeLibrary createDatatypeLibrary(String uri) {
/*  60 */     Enumeration<DatatypeLibraryFactory> e = this.service.getProviders();
/*  61 */     while (e.hasMoreElements()) {
/*     */
/*  63 */       DatatypeLibraryFactory factory = e.nextElement();
/*  64 */       DatatypeLibrary library = factory.createDatatypeLibrary(uri);
/*  65 */       if (library != null)
/*  66 */         return library;
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */
/*     */   private static class Service {
/*     */     private final Class serviceClass;
/*     */     private final Enumeration configFiles;
/*  74 */     private Enumeration classNames = null; private Loader loader; private static final int START = 0;
/*  75 */     private final Vector providers = new Vector();
/*     */     private static final int IN_NAME = 1;
/*     */     private static final int IN_COMMENT = 2;
/*     */
/*  79 */     private class ProviderEnumeration implements Enumeration { private int nextIndex = 0;
/*     */
/*     */       public boolean hasMoreElements() {
/*  82 */         return (this.nextIndex < Service.this.providers.size() || Service.this.moreProviders());
/*     */       }
/*     */
/*     */       public Object nextElement() {
/*     */         try {
/*  87 */           return Service.this.providers.elementAt(this.nextIndex++);
/*     */         }
/*  89 */         catch (ArrayIndexOutOfBoundsException e) {
/*  90 */           throw new NoSuchElementException();
/*     */         }
/*     */       }
/*     */       private ProviderEnumeration() {} }
/*     */
/*     */     private static class Singleton implements Enumeration { private Object obj;
/*     */
/*     */       private Singleton(Object obj) {
/*  98 */         this.obj = obj;
/*     */       }
/*     */
/*     */       public boolean hasMoreElements() {
/* 102 */         return (this.obj != null);
/*     */       }
/*     */
/*     */       public Object nextElement() {
/* 106 */         if (this.obj == null)
/* 107 */           throw new NoSuchElementException();
/* 108 */         Object tem = this.obj;
/* 109 */         this.obj = null;
/* 110 */         return tem;
/*     */       } }
/*     */
/*     */     private static class Loader { private Loader() {}
/*     */
/*     */       Enumeration getResources(String resName) {
/*     */         URL url;
/* 117 */         ClassLoader cl = Loader.class.getClassLoader();
/*     */
/* 119 */         if (cl == null) {
/* 120 */           url = ClassLoader.getSystemResource(resName);
/*     */         } else {
/* 122 */           url = cl.getResource(resName);
/* 123 */         }  return new Singleton(url);
/*     */       }
/*     */
/*     */       Class loadClass(String name) throws ClassNotFoundException {
/* 127 */         return Class.forName(name);
/*     */       } }
/*     */
/*     */
/*     */     private static class Loader2
/*     */       extends Loader {
/*     */       private ClassLoader cl;
/*     */
/*     */       Loader2() {
/* 136 */         this.cl = Loader2.class.getClassLoader();
/*     */
/*     */
/*     */
/*     */
/* 141 */         ClassLoader clt = Thread.currentThread().getContextClassLoader();
/* 142 */         for (ClassLoader tem = clt; tem != null; tem = tem.getParent()) {
/* 143 */           if (tem == this.cl) {
/* 144 */             this.cl = clt;
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       Enumeration getResources(String resName) {
/*     */         try {
/* 151 */           return this.cl.getResources(resName);
/*     */         }
/* 153 */         catch (IOException e) {
/* 154 */           return new Singleton(null);
/*     */         }
/*     */       }
/*     */
/*     */       Class loadClass(String name) throws ClassNotFoundException {
/* 159 */         return Class.forName(name, true, this.cl);
/*     */       }
/*     */     }
/*     */
/*     */     public Service(Class cls) {
/*     */       try {
/* 165 */         this.loader = new Loader2();
/*     */       }
/* 167 */       catch (NoSuchMethodError e) {
/* 168 */         this.loader = new Loader();
/*     */       }
/* 170 */       this.serviceClass = cls;
/* 171 */       String resName = "META-INF/services/" + this.serviceClass.getName();
/* 172 */       this.configFiles = this.loader.getResources(resName);
/*     */     }
/*     */
/*     */     public Enumeration getProviders() {
/* 176 */       return new ProviderEnumeration();
/*     */     }
/*     */
/*     */     private synchronized boolean moreProviders() {
/*     */       while (true) {
/* 181 */         while (this.classNames == null) {
/* 182 */           if (!this.configFiles.hasMoreElements())
/* 183 */             return false;
/* 184 */           this.classNames = parseConfigFile(this.configFiles.nextElement());
/*     */         }
/* 186 */         while (this.classNames.hasMoreElements()) {
/* 187 */           String className = this.classNames.nextElement();
/*     */
/* 189 */           try { Class cls = this.loader.loadClass(className);
/* 190 */             Object obj = cls.newInstance();
/* 191 */             if (this.serviceClass.isInstance(obj)) {
/* 192 */               this.providers.addElement(obj);
/* 193 */               return true;
/*     */             }
/*     */              }
/* 196 */           catch (ClassNotFoundException classNotFoundException) {  }
/* 197 */           catch (InstantiationException instantiationException) {  }
/* 198 */           catch (IllegalAccessException illegalAccessException) {  }
/* 199 */           catch (LinkageError linkageError) {}
/*     */         }
/* 201 */         this.classNames = null;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private static Enumeration parseConfigFile(URL url) {
/*     */       try {
/* 211 */         InputStream in = url.openStream();
/*     */
/*     */         try {
/* 214 */           r = new InputStreamReader(in, "UTF-8");
/*     */         }
/* 216 */         catch (UnsupportedEncodingException e) {
/* 217 */           r = new InputStreamReader(in, "UTF8");
/*     */         }
/* 219 */         Reader r = new BufferedReader(r);
/* 220 */         Vector<String> tokens = new Vector();
/* 221 */         StringBuffer tokenBuf = new StringBuffer();
/* 222 */         int state = 0;
/*     */         while (true) {
/* 224 */           int n = r.read();
/* 225 */           if (n < 0)
/*     */             break;
/* 227 */           char c = (char)n;
/* 228 */           switch (c) {
/*     */             case '\n':
/*     */             case '\r':
/* 231 */               state = 0;
/*     */               break;
/*     */             case '\t':
/*     */             case ' ':
/*     */               break;
/*     */             case '#':
/* 237 */               state = 2;
/*     */               break;
/*     */             default:
/* 240 */               if (state != 2) {
/* 241 */                 state = 1;
/* 242 */                 tokenBuf.append(c);
/*     */               }
/*     */               break;
/*     */           }
/* 246 */           if (tokenBuf.length() != 0 && state != 1) {
/* 247 */             tokens.addElement(tokenBuf.toString());
/* 248 */             tokenBuf.setLength(0);
/*     */           }
/*     */         }
/* 251 */         if (tokenBuf.length() != 0)
/* 252 */           tokens.addElement(tokenBuf.toString());
/* 253 */         return tokens.elements();
/*     */       }
/* 255 */       catch (IOException e) {
/* 256 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\helpers\DatatypeLibraryLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
