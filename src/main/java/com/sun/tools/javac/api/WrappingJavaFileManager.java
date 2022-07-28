/*     */ package com.sun.tools.javac.api;
/*     */
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.ForwardingJavaFileManager;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
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
/*     */ public class WrappingJavaFileManager<M extends JavaFileManager>
/*     */   extends ForwardingJavaFileManager<M>
/*     */ {
/*     */   protected WrappingJavaFileManager(M paramM) {
/*  63 */     super(paramM);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected FileObject wrap(FileObject paramFileObject) {
/*  73 */     return paramFileObject;
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
/*     */   protected JavaFileObject wrap(JavaFileObject paramJavaFileObject) {
/*  85 */     return (JavaFileObject)wrap(paramJavaFileObject);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected FileObject unwrap(FileObject paramFileObject) {
/*  95 */     return paramFileObject;
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
/*     */   protected JavaFileObject unwrap(JavaFileObject paramJavaFileObject) {
/* 107 */     return (JavaFileObject)unwrap(paramJavaFileObject);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected Iterable<JavaFileObject> wrap(Iterable<JavaFileObject> paramIterable) {
/* 118 */     ArrayList<JavaFileObject> arrayList = new ArrayList();
/* 119 */     for (JavaFileObject javaFileObject : paramIterable)
/* 120 */       arrayList.add(wrap(javaFileObject));
/* 121 */     return Collections.unmodifiableList(arrayList);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected URI unwrap(URI paramURI) {
/* 131 */     return paramURI;
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
/*     */   public Iterable<JavaFileObject> list(Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean) throws IOException {
/* 143 */     return wrap(super.list(paramLocation, paramString, paramSet, paramBoolean));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String inferBinaryName(Location paramLocation, JavaFileObject paramJavaFileObject) {
/* 150 */     return super.inferBinaryName(paramLocation, unwrap(paramJavaFileObject));
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
/*     */   public JavaFileObject getJavaFileForInput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind) throws IOException {
/* 163 */     return wrap(super.getJavaFileForInput(paramLocation, paramString, paramKind));
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
/*     */   public JavaFileObject getJavaFileForOutput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject) throws IOException {
/* 177 */     return wrap(super.getJavaFileForOutput(paramLocation, paramString, paramKind, unwrap(paramFileObject)));
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
/*     */   public FileObject getFileForInput(Location paramLocation, String paramString1, String paramString2) throws IOException {
/* 189 */     return wrap(super.getFileForInput(paramLocation, paramString1, paramString2));
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
/*     */   public FileObject getFileForOutput(Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject) throws IOException {
/* 202 */     return wrap(super.getFileForOutput(paramLocation, paramString1, paramString2,
/*     */
/*     */
/* 205 */           unwrap(paramFileObject)));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\WrappingJavaFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
