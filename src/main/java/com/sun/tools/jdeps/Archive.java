/*     */ package com.sun.tools.jdeps;
/*     */ 
/*     */ import com.sun.tools.classfile.Dependency;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Archive
/*     */ {
/*     */   private final Path path;
/*     */   private final String filename;
/*     */   private final ClassFileReader reader;
/*     */   
/*     */   public static Archive getInstance(Path paramPath) throws IOException {
/*  41 */     return new Archive(paramPath, ClassFileReader.newInstance(paramPath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected Map<Dependency.Location, Set<Dependency.Location>> deps = new ConcurrentHashMap<>();
/*     */   
/*     */   protected Archive(String paramString) {
/*  50 */     this.path = null;
/*  51 */     this.filename = paramString;
/*  52 */     this.reader = null;
/*     */   }
/*     */   
/*     */   protected Archive(Path paramPath, ClassFileReader paramClassFileReader) {
/*  56 */     this.path = paramPath;
/*  57 */     this.filename = this.path.getFileName().toString();
/*  58 */     this.reader = paramClassFileReader;
/*     */   }
/*     */   
/*     */   public ClassFileReader reader() {
/*  62 */     return this.reader;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  66 */     return this.filename;
/*     */   }
/*     */   
/*     */   public void addClass(Dependency.Location paramLocation) {
/*  70 */     Set<Dependency.Location> set = this.deps.get(paramLocation);
/*  71 */     if (set == null) {
/*  72 */       set = new HashSet();
/*  73 */       this.deps.put(paramLocation, set);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addClass(Dependency.Location paramLocation1, Dependency.Location paramLocation2) {
/*  78 */     Set<Dependency.Location> set = this.deps.get(paramLocation1);
/*  79 */     if (set == null) {
/*  80 */       set = new HashSet();
/*  81 */       this.deps.put(paramLocation1, set);
/*     */     } 
/*  83 */     set.add(paramLocation2);
/*     */   }
/*     */   
/*     */   public Set<Dependency.Location> getClasses() {
/*  87 */     return this.deps.keySet();
/*     */   }
/*     */   
/*     */   public void visitDependences(Visitor paramVisitor) {
/*  91 */     for (Map.Entry<Dependency.Location, Set<Dependency.Location>> entry : this.deps.entrySet()) {
/*  92 */       for (Dependency.Location location : entry.getValue()) {
/*  93 */         paramVisitor.visit((Dependency.Location)entry.getKey(), location);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  99 */     return getClasses().isEmpty();
/*     */   }
/*     */   
/*     */   public String getPathName() {
/* 103 */     return (this.path != null) ? this.path.toString() : this.filename;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 107 */     return this.filename;
/*     */   }
/*     */   
/*     */   static interface Visitor {
/*     */     void visit(Dependency.Location param1Location1, Dependency.Location param1Location2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\Archive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */