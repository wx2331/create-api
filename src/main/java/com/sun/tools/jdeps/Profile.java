/*     */ package com.sun.tools.jdeps;
/*     */ 
/*     */ import com.sun.tools.classfile.Annotation;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.RuntimeAnnotations_attribute;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ enum Profile
/*     */ {
/*  46 */   COMPACT1("compact1", 1),
/*  47 */   COMPACT2("compact2", 2),
/*  48 */   COMPACT3("compact3", 3),
/*  49 */   FULL_JRE("Full JRE", 4);
/*     */   
/*     */   final String name;
/*     */   final int profile;
/*     */   final Set<String> packages;
/*     */   final Set<String> proprietaryPkgs;
/*     */   
/*     */   Profile(String paramString1, int paramInt1) {
/*  57 */     this.name = paramString1;
/*  58 */     this.profile = paramInt1;
/*  59 */     this.packages = new HashSet<>();
/*  60 */     this.proprietaryPkgs = new HashSet<>();
/*     */   }
/*     */   
/*     */   public String profileName() {
/*  64 */     return this.name;
/*     */   }
/*     */   
/*     */   public static int getProfileCount() {
/*  68 */     return PackageToProfile.map.values().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Profile getProfile(String paramString) {
/*  76 */     Profile profile = PackageToProfile.map.get(paramString);
/*  77 */     return (profile != null && profile.packages.contains(paramString)) ? profile : null;
/*     */   }
/*     */   
/*     */   static class PackageToProfile
/*     */   {
/*  82 */     static String[] JAVAX_CRYPTO_PKGS = new String[] { "javax.crypto", "javax.crypto.interfaces", "javax.crypto.spec" };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     static Map<String, Profile> map = initProfiles();
/*     */     private static Map<String, Profile> initProfiles() {
/*     */       try {
/*  90 */         String str = System.getProperty("jdeps.profiles");
/*  91 */         if (str != null) {
/*     */           
/*  93 */           initProfilesFromProperties(str);
/*     */         } else {
/*  95 */           Path path1 = Paths.get(System.getProperty("java.home"), new String[0]);
/*  96 */           if (path1.endsWith("jre")) {
/*  97 */             path1 = path1.getParent();
/*     */           }
/*  99 */           Path path2 = path1.resolve("lib").resolve("ct.sym");
/* 100 */           if (Files.exists(path2, new java.nio.file.LinkOption[0])) {
/*     */             
/* 102 */             try (JarFile null = new JarFile(path2.toFile())) {
/* 103 */               ClassFileReader classFileReader = ClassFileReader.newInstance(path2, jarFile);
/* 104 */               for (ClassFile classFile : classFileReader.getClassFiles()) {
/* 105 */                 findProfile(classFile);
/*     */               }
/*     */             } 
/*     */ 
/*     */             
/* 110 */             Collections.addAll(Profile.COMPACT1.packages, JAVAX_CRYPTO_PKGS);
/*     */           } 
/*     */         } 
/* 113 */       } catch (IOException|ConstantPoolException iOException) {
/* 114 */         throw new Error(iOException);
/*     */       } 
/* 116 */       HashMap<Object, Object> hashMap = new HashMap<>();
/* 117 */       for (Profile profile : Profile.values()) {
/* 118 */         for (String str : profile.packages) {
/* 119 */           if (!hashMap.containsKey(str))
/*     */           {
/* 121 */             hashMap.put(str, profile);
/*     */           }
/*     */         } 
/* 124 */         for (String str : profile.proprietaryPkgs) {
/* 125 */           if (!hashMap.containsKey(str)) {
/* 126 */             hashMap.put(str, profile);
/*     */           }
/*     */         } 
/*     */       } 
/* 130 */       return (Map)hashMap;
/*     */     }
/*     */     private static final String PROFILE_ANNOTATION = "Ljdk/Profile+Annotation;";
/*     */     private static final String PROPRIETARY_ANNOTATION = "Lsun/Proprietary+Annotation;";
/*     */     
/*     */     private static Profile findProfile(ClassFile param1ClassFile) throws ConstantPoolException {
/* 136 */       RuntimeAnnotations_attribute runtimeAnnotations_attribute = (RuntimeAnnotations_attribute)param1ClassFile.attributes.get("RuntimeInvisibleAnnotations");
/* 137 */       int i = 0;
/* 138 */       boolean bool = false;
/* 139 */       if (runtimeAnnotations_attribute != null) {
/* 140 */         for (byte b = 0; b < runtimeAnnotations_attribute.annotations.length; b++) {
/* 141 */           Annotation annotation = runtimeAnnotations_attribute.annotations[b];
/* 142 */           String str1 = param1ClassFile.constant_pool.getUTF8Value(annotation.type_index);
/* 143 */           if ("Ljdk/Profile+Annotation;".equals(str1)) {
/* 144 */             byte b1 = 0; if (b1 < annotation.num_element_value_pairs) {
/* 145 */               Annotation.element_value_pair element_value_pair = annotation.element_value_pairs[b1];
/* 146 */               Annotation.Primitive_element_value primitive_element_value = (Annotation.Primitive_element_value)element_value_pair.value;
/*     */               
/* 148 */               ConstantPool.CONSTANT_Integer_info cONSTANT_Integer_info = (ConstantPool.CONSTANT_Integer_info)param1ClassFile.constant_pool.get(primitive_element_value.const_value_index);
/* 149 */               i = cONSTANT_Integer_info.value;
/*     */             }
/*     */           
/* 152 */           } else if ("Lsun/Proprietary+Annotation;".equals(str1)) {
/* 153 */             bool = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 158 */       Profile profile = null;
/* 159 */       switch (i) {
/*     */         case 1:
/* 161 */           profile = Profile.COMPACT1; break;
/*     */         case 2:
/* 163 */           profile = Profile.COMPACT2; break;
/*     */         case 3:
/* 165 */           profile = Profile.COMPACT3; break;
/*     */         case 4:
/* 167 */           profile = Profile.FULL_JRE;
/*     */           break;
/*     */         
/*     */         default:
/* 171 */           return null;
/*     */       } 
/*     */       
/* 174 */       String str = param1ClassFile.getName();
/* 175 */       int j = str.lastIndexOf('/');
/* 176 */       str = (j > 0) ? str.substring(0, j).replace('/', '.') : "";
/* 177 */       if (bool) {
/* 178 */         profile.proprietaryPkgs.add(str);
/*     */       } else {
/* 180 */         profile.packages.add(str);
/*     */       } 
/* 182 */       return profile;
/*     */     }
/*     */     
/*     */     private static void initProfilesFromProperties(String param1String) throws IOException {
/* 186 */       Properties properties = new Properties();
/* 187 */       try (FileReader null = new FileReader(param1String)) {
/* 188 */         properties.load(fileReader);
/*     */       } 
/* 190 */       for (Profile profile : Profile.values()) {
/* 191 */         int i = profile.profile;
/* 192 */         String str1 = properties.getProperty("profile." + i + ".name");
/* 193 */         if (str1 == null) {
/* 194 */           throw new RuntimeException(str1 + " missing in " + param1String);
/*     */         }
/* 196 */         String str2 = properties.getProperty("profile." + i + ".packages");
/* 197 */         String[] arrayOfString = str2.split("\\s+");
/* 198 */         for (String str : arrayOfString) {
/* 199 */           if (!str.isEmpty()) {
/* 200 */             profile.packages.add(str);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 208 */     if (paramArrayOfString.length == 0) {
/* 209 */       if (getProfileCount() == 0) {
/* 210 */         System.err.println("No profile is present in this JDK");
/*     */       }
/* 212 */       for (Profile profile : values()) {
/* 213 */         String str = profile.name;
/* 214 */         TreeSet<String> treeSet = new TreeSet<>(profile.packages);
/* 215 */         for (String str1 : treeSet) {
/*     */ 
/*     */           
/* 218 */           if (PackageToProfile.map.get(str1) == profile) {
/* 219 */             System.out.format("%2d: %-10s  %s%n", new Object[] { Integer.valueOf(profile.profile), str, str1 });
/* 220 */             str = ""; continue;
/*     */           } 
/* 222 */           System.err.format("Split package: %s in %s and %s %n", new Object[] { str1, ((Profile)PackageToProfile.map
/* 223 */                 .get(str1)).name, profile.name });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 228 */     for (String str : paramArrayOfString) {
/* 229 */       System.out.format("%s in %s%n", new Object[] { str, getProfile(str) });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\Profile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */