/*     */ package com.sun.tools.jdeps;
/*     */
/*     */ import com.sun.tools.classfile.Dependency;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Analyzer
/*     */ {
/*     */   private final Type type;
/*     */   private final Filter filter;
/*     */
/*     */   public enum Type
/*     */   {
/*  50 */     SUMMARY,
/*  51 */     PACKAGE,
/*  52 */     CLASS,
/*  53 */     VERBOSE;
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
/*  66 */   private final Map<Archive, ArchiveDeps> results = new HashMap<>();
/*  67 */   private final Map<Dependency.Location, Archive> map = new HashMap<>();
/*  68 */   private final Archive NOT_FOUND = new Archive(
/*  69 */       JdepsTask.getMessage("artifact.not.found", new Object[0]));
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Analyzer(Type paramType, Filter paramFilter) {
/*  78 */     this.type = paramType;
/*  79 */     this.filter = paramFilter;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void run(List<Archive> paramList) {
/*  87 */     buildLocationArchiveMap(paramList);
/*     */
/*     */
/*  90 */     for (Archive archive : paramList) {
/*  91 */       ArchiveDeps archiveDeps = new ArchiveDeps(archive, this.type);
/*  92 */       archive.visitDependences(archiveDeps);
/*  93 */       this.results.put(archive, archiveDeps);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   private void buildLocationArchiveMap(List<Archive> paramList) {
/*  99 */     for (Archive archive : paramList) {
/* 100 */       for (Dependency.Location location : archive.getClasses()) {
/* 101 */         if (!this.map.containsKey(location)) {
/* 102 */           this.map.put(location, archive);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   public boolean hasDependences(Archive paramArchive) {
/* 111 */     if (this.results.containsKey(paramArchive)) {
/* 112 */       return (((ArchiveDeps)this.results.get(paramArchive)).dependencies().size() > 0);
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */
/*     */   public Set<String> dependences(Archive paramArchive) {
/* 118 */     ArchiveDeps archiveDeps = this.results.get(paramArchive);
/* 119 */     return archiveDeps.targetDependences();
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
/*     */   public void visitDependences(Archive paramArchive, Visitor paramVisitor, Type paramType) {
/* 137 */     if (paramType == Type.SUMMARY) {
/* 138 */       ArchiveDeps archiveDeps = this.results.get(paramArchive);
/* 139 */       TreeMap<Object, Object> treeMap = new TreeMap<>();
/* 140 */       for (Archive archive : archiveDeps.requires()) {
/* 141 */         treeMap.put(archive.getName(), archive);
/*     */       }
/* 143 */       for (Archive archive : treeMap.values()) {
/* 144 */         Profile profile = archiveDeps.getTargetProfile(archive);
/* 145 */         paramVisitor.visitDependence(paramArchive.getName(), paramArchive, (profile != null) ? profile
/* 146 */             .profileName() : archive.getName(), archive);
/*     */       }
/*     */     } else {
/* 149 */       ArchiveDeps archiveDeps = this.results.get(paramArchive);
/* 150 */       if (paramType != this.type) {
/*     */
/* 152 */         archiveDeps = new ArchiveDeps(paramArchive, paramType);
/* 153 */         paramArchive.visitDependences(archiveDeps);
/*     */       }
/* 155 */       TreeSet<Dep> treeSet = new TreeSet<>(archiveDeps.dependencies());
/* 156 */       for (Dep dep : treeSet) {
/* 157 */         paramVisitor.visitDependence(dep.origin(), dep.originArchive(), dep.target(), dep.targetArchive());
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   public void visitDependences(Archive paramArchive, Visitor paramVisitor) {
/* 163 */     visitDependences(paramArchive, paramVisitor, this.type);
/*     */   }
/*     */
/*     */   class ArchiveDeps
/*     */     implements Archive.Visitor
/*     */   {
/*     */     protected final Archive archive;
/*     */     protected final Set<Archive> requires;
/*     */     protected final Set<Dep> deps;
/*     */     protected final Type level;
/*     */     private Profile profile;
/*     */     private Dep curDep;
/*     */
/*     */     ArchiveDeps(Archive param1Archive, Type param1Type) {
/* 177 */       this.archive = param1Archive;
/* 178 */       this.deps = new HashSet<>();
/* 179 */       this.requires = new HashSet<>();
/* 180 */       this.level = param1Type;
/*     */     }
/*     */
/*     */     Set<Dep> dependencies() {
/* 184 */       return this.deps;
/*     */     }
/*     */
/*     */     Set<String> targetDependences() {
/* 188 */       HashSet<String> hashSet = new HashSet();
/* 189 */       for (Dep dep : this.deps) {
/* 190 */         hashSet.add(dep.target());
/*     */       }
/* 192 */       return hashSet;
/*     */     }
/*     */
/*     */     Set<Archive> requires() {
/* 196 */       return this.requires;
/*     */     }
/*     */
/*     */     Profile getTargetProfile(Archive param1Archive) {
/* 200 */       return PlatformClassPath.JDKArchive.isProfileArchive(param1Archive) ? this.profile : null;
/*     */     }
/*     */
/*     */     Archive findArchive(Dependency.Location param1Location) {
/* 204 */       Archive archive = this.archive.getClasses().contains(param1Location) ? this.archive : (Archive)Analyzer.this.map.get(param1Location);
/* 205 */       if (archive == null) {
/* 206 */         Analyzer.this.map.put(param1Location, archive = Analyzer.this.NOT_FOUND);
/*     */       }
/* 208 */       return archive;
/*     */     }
/*     */
/*     */
/*     */     private String getLocationName(Dependency.Location param1Location) {
/* 213 */       if (this.level == Type.CLASS || this.level == Type.VERBOSE) {
/* 214 */         return param1Location.getClassName();
/*     */       }
/* 216 */       String str = param1Location.getPackageName();
/* 217 */       return str.isEmpty() ? "<unnamed>" : str;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public void visit(Dependency.Location param1Location1, Dependency.Location param1Location2) {
/* 223 */       Archive archive = findArchive(param1Location2);
/* 224 */       if (Analyzer.this.filter.accepts(param1Location1, this.archive, param1Location2, archive)) {
/* 225 */         addDep(param1Location1, param1Location2);
/* 226 */         if (this.archive != archive && !this.requires.contains(archive)) {
/* 227 */           this.requires.add(archive);
/*     */         }
/*     */       }
/* 230 */       if (archive instanceof PlatformClassPath.JDKArchive) {
/* 231 */         Profile profile = Profile.getProfile(param1Location2.getPackageName());
/* 232 */         if (this.profile == null || (profile != null && profile.compareTo(this.profile) > 0)) {
/* 233 */           this.profile = profile;
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     protected Dep addDep(Dependency.Location param1Location1, Dependency.Location param1Location2) {
/* 240 */       String str1 = getLocationName(param1Location1);
/* 241 */       String str2 = getLocationName(param1Location2);
/* 242 */       Archive archive = findArchive(param1Location2);
/* 243 */       if (this.curDep != null && this.curDep
/* 244 */         .origin().equals(str1) && this.curDep
/* 245 */         .originArchive() == this.archive && this.curDep
/* 246 */         .target().equals(str2) && this.curDep
/* 247 */         .targetArchive() == archive) {
/* 248 */         return this.curDep;
/*     */       }
/*     */
/* 251 */       Dep dep = new Dep(str1, this.archive, str2, archive);
/* 252 */       if (this.deps.contains(dep)) {
/* 253 */         for (Dep dep1 : this.deps) {
/* 254 */           if (dep.equals(dep1)) {
/* 255 */             this.curDep = dep1;
/*     */           }
/*     */         }
/*     */       } else {
/* 259 */         this.deps.add(dep);
/* 260 */         this.curDep = dep;
/*     */       }
/* 262 */       return this.curDep;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   class Dep
/*     */     implements Comparable<Dep>
/*     */   {
/*     */     final String origin;
/*     */     final Archive originArchive;
/*     */     final String target;
/*     */     final Archive targetArchive;
/*     */
/*     */     Dep(String param1String1, Archive param1Archive1, String param1String2, Archive param1Archive2) {
/* 276 */       this.origin = param1String1;
/* 277 */       this.originArchive = param1Archive1;
/* 278 */       this.target = param1String2;
/* 279 */       this.targetArchive = param1Archive2;
/*     */     }
/*     */
/*     */     String origin() {
/* 283 */       return this.origin;
/*     */     }
/*     */
/*     */     Archive originArchive() {
/* 287 */       return this.originArchive;
/*     */     }
/*     */
/*     */     String target() {
/* 291 */       return this.target;
/*     */     }
/*     */
/*     */     Archive targetArchive() {
/* 295 */       return this.targetArchive;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 301 */       if (param1Object instanceof Dep) {
/* 302 */         Dep dep = (Dep)param1Object;
/* 303 */         return (this.origin.equals(dep.origin) && this.originArchive == dep.originArchive && this.target
/*     */
/* 305 */           .equals(dep.target) && this.targetArchive == dep.targetArchive);
/*     */       }
/*     */
/* 308 */       return false;
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 313 */       int i = 7;
/*     */
/*     */
/*     */
/* 317 */       i = 67 * i + Objects.hashCode(this.origin) + Objects.hashCode(this.originArchive) + Objects.hashCode(this.target) + Objects.hashCode(this.targetArchive);
/* 318 */       return i;
/*     */     }
/*     */
/*     */
/*     */     public int compareTo(Dep param1Dep) {
/* 323 */       if (this.origin.equals(param1Dep.origin)) {
/* 324 */         if (this.target.equals(param1Dep.target)) {
/* 325 */           if (this.originArchive == param1Dep.originArchive && this.targetArchive == param1Dep.targetArchive)
/*     */           {
/* 327 */             return 0; }
/* 328 */           if (this.originArchive == param1Dep.originArchive) {
/* 329 */             return this.targetArchive.getPathName().compareTo(param1Dep.targetArchive.getPathName());
/*     */           }
/* 331 */           return this.originArchive.getPathName().compareTo(param1Dep.originArchive.getPathName());
/*     */         }
/*     */
/* 334 */         return this.target.compareTo(param1Dep.target);
/*     */       }
/*     */
/* 337 */       return this.origin.compareTo(param1Dep.origin);
/*     */     }
/*     */   }
/*     */
/*     */   static interface Filter {
/*     */     boolean accepts(Dependency.Location param1Location1, Archive param1Archive1, Dependency.Location param1Location2, Archive param1Archive2);
/*     */   }
/*     */
/*     */   public static interface Visitor {
/*     */     void visitDependence(String param1String1, Archive param1Archive1, String param1String2, Archive param1Archive2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\Analyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
