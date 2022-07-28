/*     */ package com.sun.codemodel.internal.fmt;
/*     */
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JResourceFile;
/*     */ import com.sun.codemodel.internal.JTypeVar;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.text.ParseException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public final class JStaticJavaFile
/*     */   extends JResourceFile
/*     */ {
/*     */   private final JPackage pkg;
/*     */   private final String className;
/*     */   private final URL source;
/*     */   private final JStaticClass clazz;
/*     */   private final LineFilter filter;
/*     */
/*     */   public JStaticJavaFile(JPackage _pkg, String className, String _resourceName) {
/*  77 */     this(_pkg, className,
/*  78 */         SecureLoader.getClassClassLoader(JStaticJavaFile.class).getResource(_resourceName), null);
/*     */   }
/*     */
/*     */   public JStaticJavaFile(JPackage _pkg, String _className, URL _source, LineFilter _filter) {
/*  82 */     super(_className + ".java");
/*  83 */     if (_source == null) throw new NullPointerException();
/*  84 */     this.pkg = _pkg;
/*  85 */     this.clazz = new JStaticClass();
/*  86 */     this.className = _className;
/*  87 */     this.source = _source;
/*  88 */     this.filter = _filter;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public final JClass getJClass() {
/*  95 */     return this.clazz;
/*     */   }
/*     */
/*     */   protected boolean isResource() {
/*  99 */     return false;
/*     */   }
/*     */
/*     */   protected void build(OutputStream os) throws IOException {
/* 103 */     InputStream is = this.source.openStream();
/*     */
/* 105 */     BufferedReader r = new BufferedReader(new InputStreamReader(is));
/* 106 */     PrintWriter w = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
/* 107 */     LineFilter filter = createLineFilter();
/* 108 */     int lineNumber = 1;
/*     */
/*     */     try {
/*     */       String line;
/* 112 */       while ((line = r.readLine()) != null) {
/* 113 */         line = filter.process(line);
/* 114 */         if (line != null)
/* 115 */           w.println(line);
/* 116 */         lineNumber++;
/*     */       }
/* 118 */     } catch (ParseException e) {
/* 119 */       throw new IOException("unable to process " + this.source + " line:" + lineNumber + "\n" + e.getMessage());
/*     */     }
/*     */
/* 122 */     w.close();
/* 123 */     r.close();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private LineFilter createLineFilter() {
/* 134 */     LineFilter f = new LineFilter() {
/*     */         public String process(String line) {
/* 136 */           if (!line.startsWith("package ")) return line;
/*     */
/*     */
/* 139 */           if (JStaticJavaFile.this.pkg.isUnnamed()) {
/* 140 */             return null;
/*     */           }
/* 142 */           return "package " + JStaticJavaFile.this.pkg.name() + ";";
/*     */         }
/*     */       };
/* 145 */     if (this.filter != null) {
/* 146 */       return new ChainFilter(this.filter, f);
/*     */     }
/* 148 */     return f;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static final class ChainFilter
/*     */     implements LineFilter
/*     */   {
/*     */     private final LineFilter first;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final LineFilter second;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public ChainFilter(LineFilter first, LineFilter second) {
/* 179 */       this.first = first;
/* 180 */       this.second = second;
/*     */     }
/*     */     public String process(String line) throws ParseException {
/* 183 */       line = this.first.process(line);
/* 184 */       if (line == null) return null;
/* 185 */       return this.second.process(line);
/*     */     }
/*     */   }
/*     */
/*     */   private class JStaticClass
/*     */     extends JClass
/*     */   {
/*     */     private final JTypeVar[] typeParams;
/*     */
/*     */     JStaticClass() {
/* 195 */       super(JStaticJavaFile.this.pkg.owner());
/*     */
/* 197 */       this.typeParams = new JTypeVar[0];
/*     */     }
/*     */
/*     */     public String name() {
/* 201 */       return JStaticJavaFile.this.className;
/*     */     }
/*     */
/*     */     public String fullName() {
/* 205 */       if (JStaticJavaFile.this.pkg.isUnnamed()) {
/* 206 */         return JStaticJavaFile.this.className;
/*     */       }
/* 208 */       return JStaticJavaFile.this.pkg.name() + '.' + JStaticJavaFile.this.className;
/*     */     }
/*     */
/*     */     public JPackage _package() {
/* 212 */       return JStaticJavaFile.this.pkg;
/*     */     }
/*     */
/*     */     public JClass _extends() {
/* 216 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */     public Iterator<JClass> _implements() {
/* 220 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */     public boolean isInterface() {
/* 224 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */     public boolean isAbstract() {
/* 228 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */     public JTypeVar[] typeParams() {
/* 232 */       return this.typeParams;
/*     */     }
/*     */
/*     */     protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 236 */       return this;
/*     */     }
/*     */   }
/*     */
/*     */   public static interface LineFilter {
/*     */     String process(String param1String) throws ParseException;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\fmt\JStaticJavaFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
