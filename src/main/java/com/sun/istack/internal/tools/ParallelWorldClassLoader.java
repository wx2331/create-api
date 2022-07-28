/*     */ package com.sun.istack.internal.tools;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class ParallelWorldClassLoader
/*     */   extends ClassLoader
/*     */   implements Closeable
/*     */ {
/*     */   private final String prefix;
/*     */   private final Set<JarFile> jars;
/*     */   
/*     */   public ParallelWorldClassLoader(ClassLoader parent, String prefix) {
/*  98 */     super(parent);
/*  99 */     this.prefix = prefix;
/* 100 */     this.jars = Collections.synchronizedSet(new HashSet<>());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class findClass(String name) throws ClassNotFoundException {
/* 105 */     StringBuffer sb = new StringBuffer(name.length() + this.prefix.length() + 6);
/* 106 */     sb.append(this.prefix).append(name.replace('.', '/')).append(".class");
/*     */     
/* 108 */     URL u = getParent().getResource(sb.toString());
/* 109 */     if (u == null) {
/* 110 */       throw new ClassNotFoundException(name);
/*     */     }
/*     */     
/* 113 */     InputStream is = null;
/* 114 */     URLConnection con = null;
/*     */     
/*     */     try {
/* 117 */       con = u.openConnection();
/* 118 */       is = con.getInputStream();
/* 119 */     } catch (IOException ioe) {
/* 120 */       throw new ClassNotFoundException(name);
/*     */     } 
/*     */     
/* 123 */     if (is == null) {
/* 124 */       throw new ClassNotFoundException(name);
/*     */     }
/*     */     try {
/* 127 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 128 */       byte[] buf = new byte[1024];
/*     */       int len;
/* 130 */       while ((len = is.read(buf)) >= 0) {
/* 131 */         baos.write(buf, 0, len);
/*     */       }
/* 133 */       buf = baos.toByteArray();
/* 134 */       int packIndex = name.lastIndexOf('.');
/* 135 */       if (packIndex != -1) {
/* 136 */         String pkgname = name.substring(0, packIndex);
/*     */         
/* 138 */         Package pkg = getPackage(pkgname);
/* 139 */         if (pkg == null) {
/* 140 */           definePackage(pkgname, null, null, null, null, null, null, null);
/*     */         }
/*     */       } 
/* 143 */       return defineClass(name, buf, 0, buf.length);
/* 144 */     } catch (IOException e) {
/* 145 */       throw new ClassNotFoundException(name, e);
/*     */     } finally {
/*     */       try {
/* 148 */         if (con != null && con instanceof JarURLConnection) {
/* 149 */           this.jars.add(((JarURLConnection)con).getJarFile());
/*     */         }
/* 151 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 154 */       if (is != null) {
/*     */         try {
/* 156 */           is.close();
/* 157 */         } catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected URL findResource(String name) {
/* 166 */     URL u = getParent().getResource(this.prefix + name);
/* 167 */     if (u != null) {
/*     */       try {
/* 169 */         this.jars.add(new JarFile(new File(toJarUrl(u).toURI())));
/* 170 */       } catch (URISyntaxException ex) {
/* 171 */         Logger.getLogger(ParallelWorldClassLoader.class.getName()).log(Level.WARNING, (String)null, ex);
/* 172 */       } catch (IOException ex) {
/* 173 */         Logger.getLogger(ParallelWorldClassLoader.class.getName()).log(Level.WARNING, (String)null, ex);
/* 174 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */     
/* 178 */     return u;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Enumeration<URL> findResources(String name) throws IOException {
/* 183 */     Enumeration<URL> en = getParent().getResources(this.prefix + name);
/* 184 */     while (en.hasMoreElements()) {
/*     */       try {
/* 186 */         this.jars.add(new JarFile(new File(toJarUrl(en.nextElement()).toURI())));
/* 187 */       } catch (URISyntaxException ex) {
/*     */         
/* 189 */         Logger.getLogger(ParallelWorldClassLoader.class.getName()).log(Level.WARNING, (String)null, ex);
/* 190 */       } catch (IOException ex) {
/* 191 */         Logger.getLogger(ParallelWorldClassLoader.class.getName()).log(Level.WARNING, (String)null, ex);
/* 192 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */     } 
/*     */ 
/*     */     
/* 196 */     return en;
/*     */   }
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 200 */     for (JarFile jar : this.jars) {
/* 201 */       jar.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL toJarUrl(URL res) throws ClassNotFoundException, MalformedURLException {
/* 209 */     String url = res.toExternalForm();
/* 210 */     if (!url.startsWith("jar:"))
/* 211 */       throw new ClassNotFoundException("Loaded outside a jar " + url); 
/* 212 */     url = url.substring(4);
/* 213 */     url = url.substring(0, url.lastIndexOf('!'));
/* 214 */     url = url.replaceAll(" ", "%20");
/* 215 */     return new URL(url);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\istack\internal\tools\ParallelWorldClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */