/*     */ package com.sun.tools.jdeps;
/*     */
/*     */ import com.sun.tools.classfile.Annotation;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Dependencies;
/*     */ import com.sun.tools.classfile.RuntimeAnnotations_attribute;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ class PlatformClassPath
/*     */ {
/*  49 */   private static final List<String> NON_PLATFORM_JARFILES = Arrays.asList(new String[] { "alt-rt.jar", "ant-javafx.jar", "javafx-mx.jar" });
/*  50 */   private static final List<Archive> javaHomeArchives = init();
/*     */
/*     */   static List<Archive> getArchives() {
/*  53 */     return javaHomeArchives;
/*     */   }
/*     */
/*     */   private static List<Archive> init() {
/*  57 */     ArrayList<Archive> arrayList = new ArrayList();
/*  58 */     Path path = Paths.get(System.getProperty("java.home"), new String[0]);
/*     */     try {
/*  60 */       if (path.endsWith("jre")) {
/*     */
/*  62 */         arrayList.addAll(addJarFiles(path.resolve("lib")));
/*  63 */         if (path.getParent() != null) {
/*     */
/*  65 */           Path path1 = path.getParent().resolve("lib");
/*  66 */           if (Files.exists(path1, new java.nio.file.LinkOption[0])) {
/*  67 */             arrayList.addAll(addJarFiles(path1));
/*     */           }
/*     */         }
/*  70 */       } else if (Files.exists(path.resolve("lib"), new java.nio.file.LinkOption[0])) {
/*     */
/*  72 */         Path path1 = path.resolve("classes");
/*  73 */         if (Files.isDirectory(path1, new java.nio.file.LinkOption[0]))
/*     */         {
/*  75 */           arrayList.add(new JDKArchive(path1));
/*     */         }
/*     */
/*  78 */         arrayList.addAll(addJarFiles(path.resolve("lib")));
/*     */       } else {
/*  80 */         throw new RuntimeException("\"" + path + "\" not a JDK home");
/*     */       }
/*  82 */       return arrayList;
/*  83 */     } catch (IOException iOException) {
/*  84 */       throw new Error(iOException);
/*     */     }
/*     */   }
/*     */
/*     */   private static List<Archive> addJarFiles(final Path root) throws IOException {
/*  89 */     final ArrayList<Archive> result = new ArrayList();
/*  90 */     final Path ext = root.resolve("ext");
/*  91 */     Files.walkFileTree(root, new SimpleFileVisitor<Path>()
/*     */         {
/*     */
/*     */           public FileVisitResult preVisitDirectory(Path param1Path, BasicFileAttributes param1BasicFileAttributes) throws IOException
/*     */           {
/*  96 */             if (param1Path.equals(root) || param1Path.equals(ext)) {
/*  97 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */
/* 100 */             return FileVisitResult.SKIP_SUBTREE;
/*     */           }
/*     */
/*     */
/*     */
/*     */
/*     */           public FileVisitResult visitFile(Path param1Path, BasicFileAttributes param1BasicFileAttributes) throws IOException {
/* 107 */             String str = param1Path.getFileName().toString();
/* 108 */             if (str.endsWith(".jar"))
/*     */             {
/*     */
/* 111 */               result.add(PlatformClassPath.NON_PLATFORM_JARFILES.contains(str) ?
/* 112 */                   Archive.getInstance(param1Path) : new JDKArchive(param1Path));
/*     */             }
/*     */
/* 115 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         });
/* 118 */     return arrayList;
/*     */   }
/*     */
/*     */
/*     */
/*     */   static class JDKArchive
/*     */     extends Archive
/*     */   {
/* 126 */     private static List<String> PROFILE_JARS = Arrays.asList(new String[] { "rt.jar", "jce.jar" });
/*     */
/* 128 */     private static List<String> EXPORTED_PACKAGES = Arrays.asList(new String[] { "javax.jnlp", "org.w3c.dom.css", "org.w3c.dom.html", "org.w3c.dom.stylesheets", "org.w3c.dom.xpath" });
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public static boolean isProfileArchive(Archive param1Archive) {
/* 136 */       if (param1Archive instanceof JDKArchive) {
/* 137 */         return PROFILE_JARS.contains(param1Archive.getName());
/*     */       }
/* 139 */       return false;
/*     */     }
/*     */
/* 142 */     private final Map<String, Boolean> exportedPackages = new HashMap<>();
/* 143 */     private final Map<String, Boolean> exportedTypes = new HashMap<>();
/*     */     JDKArchive(Path param1Path) throws IOException {
/* 145 */       super(param1Path, ClassFileReader.newInstance(param1Path));
/*     */     }
/*     */
/*     */
/*     */     private static final String JDK_EXPORTED_ANNOTATION = "Ljdk/Exported;";
/*     */
/*     */     public boolean isExported(String param1String) {
/* 152 */       int i = param1String.lastIndexOf('.');
/* 153 */       String str = (i > 0) ? param1String.substring(0, i) : "";
/*     */
/* 155 */       boolean bool = isExportedPackage(str);
/* 156 */       if (this.exportedTypes.containsKey(param1String)) {
/* 157 */         return ((Boolean)this.exportedTypes.get(param1String)).booleanValue();
/*     */       }
/* 159 */       return bool;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isExportedPackage(String param1String) {
/* 166 */       if (Profile.getProfile(param1String) != null) {
/* 167 */         return true;
/*     */       }
/*     */
/* 170 */       if (EXPORTED_PACKAGES.contains(param1String) || param1String.startsWith("javafx.")) {
/* 171 */         return true;
/*     */       }
/* 173 */       return this.exportedPackages.containsKey(param1String) ? ((Boolean)this.exportedPackages.get(param1String)).booleanValue() : false;
/*     */     }
/*     */
/*     */
/*     */
/*     */     private Boolean isJdkExported(ClassFile param1ClassFile) throws ConstantPoolException {
/* 179 */       RuntimeAnnotations_attribute runtimeAnnotations_attribute = (RuntimeAnnotations_attribute)param1ClassFile.attributes.get("RuntimeVisibleAnnotations");
/* 180 */       if (runtimeAnnotations_attribute != null) {
/* 181 */         for (byte b = 0; b < runtimeAnnotations_attribute.annotations.length; b++) {
/* 182 */           Annotation annotation = runtimeAnnotations_attribute.annotations[b];
/* 183 */           String str = param1ClassFile.constant_pool.getUTF8Value(annotation.type_index);
/* 184 */           if ("Ljdk/Exported;".equals(str)) {
/* 185 */             boolean bool = true;
/* 186 */             for (byte b1 = 0; b1 < annotation.num_element_value_pairs; b1++) {
/* 187 */               Annotation.element_value_pair element_value_pair = annotation.element_value_pairs[b1];
/* 188 */               Annotation.Primitive_element_value primitive_element_value = (Annotation.Primitive_element_value)element_value_pair.value;
/*     */
/* 190 */               ConstantPool.CONSTANT_Integer_info cONSTANT_Integer_info = (ConstantPool.CONSTANT_Integer_info)param1ClassFile.constant_pool.get(primitive_element_value.const_value_index);
/* 191 */               bool = (cONSTANT_Integer_info.value != 0) ? true : false;
/*     */             }
/* 193 */             return Boolean.valueOf(bool);
/*     */           }
/*     */         }
/*     */       }
/* 197 */       return null;
/*     */     }
/*     */
/*     */     void processJdkExported(ClassFile param1ClassFile) throws IOException {
/*     */       try {
/* 202 */         String str1 = param1ClassFile.getName();
/* 203 */         String str2 = str1.substring(0, str1.lastIndexOf('/')).replace('/', '.');
/*     */
/* 205 */         Boolean bool = isJdkExported(param1ClassFile);
/* 206 */         if (bool != null) {
/* 207 */           this.exportedTypes.put(str1.replace('/', '.'), bool);
/*     */         }
/* 209 */         if (!this.exportedPackages.containsKey(str2)) {
/*     */
/* 211 */           Boolean bool1 = null;
/* 212 */           ClassFile classFile = reader().getClassFile(str1.substring(0, str1.lastIndexOf('/') + 1) + "package-info");
/* 213 */           if (classFile != null) {
/* 214 */             bool1 = isJdkExported(classFile);
/*     */           }
/* 216 */           if (bool1 != null) {
/* 217 */             this.exportedPackages.put(str2, bool1);
/*     */           }
/*     */         }
/* 220 */       } catch (ConstantPoolException constantPoolException) {
/* 221 */         throw new Dependencies.ClassFileError(constantPoolException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\PlatformClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
