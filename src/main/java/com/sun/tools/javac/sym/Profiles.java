/*     */ package com.sun.tools.javac.sym;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Profiles
/*     */ {
/*     */   public static void main(String[] paramArrayOfString) throws IOException {
/*  55 */     Profiles profiles = read(new File(paramArrayOfString[0]));
/*  56 */     if (paramArrayOfString.length >= 2) {
/*  57 */       TreeMap<Object, Object> treeMap = new TreeMap<>();
/*  58 */       for (byte b1 = 1; b1 <= 4; b1++) {
/*  59 */         treeMap.put(Integer.valueOf(b1), new TreeSet());
/*     */       }
/*  61 */       File file = new File(paramArrayOfString[1]);
/*  62 */       for (String str : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
/*  63 */         if (str.endsWith(".class")) {
/*  64 */           String str1 = str.substring(0, str.length() - 6);
/*  65 */           int i = profiles.getProfile(str1);
/*  66 */           for (int j = i; j <= 4; j++) {
/*  67 */             ((Set<String>)treeMap.get(Integer.valueOf(j))).add(str1);
/*     */           }
/*     */         } 
/*     */       } 
/*  71 */       for (byte b2 = 1; b2 <= 4; b2++) {
/*  72 */         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(b2 + ".txt"));
/*     */       }
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
/*     */   public static Profiles read(File paramFile) throws IOException {
/*  86 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*     */     try {
/*  88 */       Properties properties = new Properties();
/*  89 */       properties.load(bufferedInputStream);
/*  90 */       if (properties.containsKey("java/lang/Object")) {
/*  91 */         return new SimpleProfiles(properties);
/*     */       }
/*  93 */       return new MakefileProfiles(properties);
/*     */     } finally {
/*  95 */       bufferedInputStream.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract int getProfileCount();
/*     */   
/*     */   public abstract int getProfile(String paramString);
/*     */   
/*     */   public abstract Set<String> getPackages(int paramInt);
/*     */   
/*     */   private static class MakefileProfiles
/*     */     extends Profiles {
/*     */     static class Package {
/*     */       final Package parent;
/*     */       final String name;
/* 110 */       Map<String, Package> subpackages = new TreeMap<>();
/*     */       
/*     */       int profile;
/* 113 */       Map<String, Integer> includedTypes = new TreeMap<>();
/* 114 */       Map<String, Integer> excludedTypes = new TreeMap<>();
/*     */       
/*     */       Package(Package param2Package, String param2String) {
/* 117 */         this.parent = param2Package;
/* 118 */         this.name = param2String;
/*     */       }
/*     */       
/*     */       int getProfile() {
/* 122 */         return (this.parent == null) ? this.profile : Math.max(this.parent.getProfile(), this.profile);
/*     */       }
/*     */       
/*     */       int getProfile(String param2String) {
/*     */         Integer integer;
/* 127 */         if ((integer = this.includedTypes.get(param2String)) != null)
/* 128 */           return integer.intValue(); 
/* 129 */         if ((integer = this.includedTypes.get("*")) != null)
/* 130 */           return integer.intValue(); 
/* 131 */         if ((integer = this.excludedTypes.get(param2String)) != null)
/* 132 */           return integer.intValue() + 1; 
/* 133 */         if ((integer = this.excludedTypes.get("*")) != null)
/* 134 */           return integer.intValue() + 1; 
/* 135 */         return getProfile();
/*     */       }
/*     */       
/*     */       String getName() {
/* 139 */         return (this.parent == null) ? this.name : (this.parent.getName() + "/" + this.name);
/*     */       }
/*     */       
/*     */       void getPackages(int param2Int, Set<String> param2Set) {
/* 143 */         int i = getProfile();
/* 144 */         if (i != 0 && param2Int >= i)
/* 145 */           param2Set.add(getName()); 
/* 146 */         for (Package package_ : this.subpackages.values()) {
/* 147 */           package_.getPackages(param2Int, param2Set);
/*     */         }
/*     */       }
/*     */     }
/* 151 */     final Map<String, Package> packages = new TreeMap<>();
/*     */     
/* 153 */     final int maxProfile = 4;
/*     */ 
/*     */     
/*     */     MakefileProfiles(Properties param1Properties) {
/* 157 */       boolean bool = false;
/* 158 */       for (byte b = 1; b <= 4; b++) {
/* 159 */         String str1 = (b < 4) ? ("PROFILE_" + b) : "FULL_JRE";
/* 160 */         String str2 = param1Properties.getProperty(str1 + "_RTJAR_INCLUDE_PACKAGES");
/* 161 */         if (str2 == null)
/*     */           break; 
/* 163 */         for (String str : str2.substring(1).trim().split("\\s+")) {
/* 164 */           if (str.endsWith("/"))
/* 165 */             str = str.substring(0, str.length() - 1); 
/* 166 */           if (!bool && str.equals("java/lang"))
/* 167 */             bool = true; 
/* 168 */           includePackage(b, str);
/*     */         } 
/* 170 */         String str3 = param1Properties.getProperty(str1 + "_RTJAR_INCLUDE_TYPES");
/* 171 */         if (str3 != null)
/* 172 */           for (String str : str3.replace("$$", "$").split("\\s+")) {
/* 173 */             if (str.endsWith(".class")) {
/* 174 */               includeType(b, str.substring(0, str.length() - 6));
/*     */             }
/*     */           }  
/* 177 */         String str4 = param1Properties.getProperty(str1 + "_RTJAR_EXCLUDE_TYPES");
/* 178 */         if (str4 != null) {
/* 179 */           for (String str : str4.replace("$$", "$").split("\\s+")) {
/* 180 */             if (str.endsWith(".class")) {
/* 181 */               excludeType(b, str.substring(0, str.length() - 6));
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 192 */       if (bool) {
/* 193 */         includePackage(1, "javax/crypto");
/*     */       }
/*     */     }
/*     */     
/*     */     public int getProfileCount() {
/* 198 */       return 4;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getProfile(String param1String) {
/* 203 */       int i = param1String.lastIndexOf("/");
/* 204 */       String str1 = param1String.substring(0, i);
/* 205 */       String str2 = param1String.substring(i + 1);
/*     */       
/* 207 */       Package package_ = getPackage(str1);
/* 208 */       return package_.getProfile(str2);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<String> getPackages(int param1Int) {
/* 213 */       TreeSet<String> treeSet = new TreeSet();
/* 214 */       for (Package package_ : this.packages.values())
/* 215 */         package_.getPackages(param1Int, treeSet); 
/* 216 */       return treeSet;
/*     */     }
/*     */ 
/*     */     
/*     */     private void includePackage(int param1Int, String param1String) {
/* 221 */       Package package_ = getPackage(param1String);
/* 222 */       Assert.check((package_.profile == 0));
/* 223 */       package_.profile = param1Int;
/*     */     }
/*     */ 
/*     */     
/*     */     private void includeType(int param1Int, String param1String) {
/* 228 */       int i = param1String.lastIndexOf("/");
/* 229 */       String str1 = param1String.substring(0, i);
/* 230 */       String str2 = param1String.substring(i + 1);
/*     */       
/* 232 */       Package package_ = getPackage(str1);
/* 233 */       Assert.check(!package_.includedTypes.containsKey(str2));
/* 234 */       package_.includedTypes.put(str2, Integer.valueOf(param1Int));
/*     */     }
/*     */ 
/*     */     
/*     */     private void excludeType(int param1Int, String param1String) {
/* 239 */       int i = param1String.lastIndexOf("/");
/* 240 */       String str1 = param1String.substring(0, i);
/* 241 */       String str2 = param1String.substring(i + 1);
/*     */       
/* 243 */       Package package_ = getPackage(str1);
/* 244 */       Assert.check(!package_.excludedTypes.containsKey(str2));
/* 245 */       package_.excludedTypes.put(str2, Integer.valueOf(param1Int)); } private Package getPackage(String param1String) {
/*     */       Package package_1;
/*     */       Map<String, Package> map;
/*     */       String str;
/* 249 */       int i = param1String.lastIndexOf("/");
/*     */ 
/*     */ 
/*     */       
/* 253 */       if (i == -1) {
/* 254 */         package_1 = null;
/* 255 */         map = this.packages;
/* 256 */         str = param1String;
/*     */       } else {
/* 258 */         package_1 = getPackage(param1String.substring(0, i));
/* 259 */         map = package_1.subpackages;
/* 260 */         str = param1String.substring(i + 1);
/*     */       } 
/*     */       
/* 263 */       Package package_2 = map.get(str);
/* 264 */       if (package_2 == null) {
/* 265 */         map.put(str, package_2 = new Package(package_1, str));
/*     */       }
/* 267 */       return package_2;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SimpleProfiles extends Profiles {
/*     */     private final Map<String, Integer> map;
/*     */     private final int profileCount;
/*     */     
/*     */     SimpleProfiles(Properties param1Properties) {
/* 276 */       int i = 0;
/* 277 */       this.map = new HashMap<>();
/* 278 */       for (Map.Entry<Object, Object> entry : param1Properties.entrySet()) {
/* 279 */         String str = (String)entry.getKey();
/* 280 */         int j = Integer.valueOf((String)entry.getValue()).intValue();
/* 281 */         this.map.put(str, Integer.valueOf(j));
/* 282 */         i = Math.max(i, j);
/*     */       } 
/* 284 */       this.profileCount = i;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getProfileCount() {
/* 289 */       return this.profileCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getProfile(String param1String) {
/* 294 */       return ((Integer)this.map.get(param1String)).intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<String> getPackages(int param1Int) {
/* 299 */       TreeSet<String> treeSet = new TreeSet();
/* 300 */       for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
/* 301 */         String str = (String)entry.getKey();
/* 302 */         int i = ((Integer)entry.getValue()).intValue();
/* 303 */         int j = str.lastIndexOf("/");
/* 304 */         if (j > 0 && param1Int >= i)
/* 305 */           treeSet.add(str); 
/*     */       } 
/* 307 */       return treeSet;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\sym\Profiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */