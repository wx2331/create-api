/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Group
/*     */ {
/*  64 */   private Map<String, String> regExpGroupMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private List<String> sortedRegExpList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private List<String> groupList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private Map<String, String> pkgNameGroupMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   private final Configuration configuration;
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MapKeyComparator
/*     */     implements Comparator<String>
/*     */   {
/*     */     private MapKeyComparator() {}
/*     */ 
/*     */     
/*     */     public int compare(String param1String1, String param1String2) {
/*  95 */       return param1String2.length() - param1String1.length();
/*     */     }
/*     */   }
/*     */   
/*     */   public Group(Configuration paramConfiguration) {
/* 100 */     this.configuration = paramConfiguration;
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
/*     */   public boolean checkPackageGroups(String paramString1, String paramString2) {
/* 119 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString2, ":");
/* 120 */     if (this.groupList.contains(paramString1)) {
/* 121 */       this.configuration.message.warning("doclet.Groupname_already_used", new Object[] { paramString1 });
/* 122 */       return false;
/*     */     } 
/* 124 */     this.groupList.add(paramString1);
/* 125 */     while (stringTokenizer.hasMoreTokens()) {
/* 126 */       String str = stringTokenizer.nextToken();
/* 127 */       if (str.length() == 0) {
/* 128 */         this.configuration.message.warning("doclet.Error_in_packagelist", new Object[] { paramString1, paramString2 });
/* 129 */         return false;
/*     */       } 
/* 131 */       if (str.endsWith("*")) {
/* 132 */         str = str.substring(0, str.length() - 1);
/* 133 */         if (foundGroupFormat(this.regExpGroupMap, str)) {
/* 134 */           return false;
/*     */         }
/* 136 */         this.regExpGroupMap.put(str, paramString1);
/* 137 */         this.sortedRegExpList.add(str); continue;
/*     */       } 
/* 139 */       if (foundGroupFormat(this.pkgNameGroupMap, str)) {
/* 140 */         return false;
/*     */       }
/* 142 */       this.pkgNameGroupMap.put(str, paramString1);
/*     */     } 
/*     */     
/* 145 */     Collections.sort(this.sortedRegExpList, new MapKeyComparator());
/* 146 */     return true;
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
/*     */   boolean foundGroupFormat(Map<String, ?> paramMap, String paramString) {
/* 158 */     if (paramMap.containsKey(paramString)) {
/* 159 */       this.configuration.message.error("doclet.Same_package_name_used", new Object[] { paramString });
/* 160 */       return true;
/*     */     } 
/* 162 */     return false;
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
/*     */   public Map<String, List<PackageDoc>> groupPackages(PackageDoc[] paramArrayOfPackageDoc) {
/* 179 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */     
/* 183 */     String str = (this.pkgNameGroupMap.isEmpty() && this.regExpGroupMap.isEmpty()) ? this.configuration.message.getText("doclet.Packages", new Object[0]) : this.configuration.message.getText("doclet.Other_Packages", new Object[0]);
/*     */     
/* 185 */     if (!this.groupList.contains(str)) {
/* 186 */       this.groupList.add(str);
/*     */     }
/* 188 */     for (byte b = 0; b < paramArrayOfPackageDoc.length; b++) {
/* 189 */       PackageDoc packageDoc = paramArrayOfPackageDoc[b];
/* 190 */       String str1 = packageDoc.name();
/* 191 */       String str2 = this.pkgNameGroupMap.get(str1);
/*     */ 
/*     */       
/* 194 */       if (str2 == null) {
/* 195 */         str2 = regExpGroupName(str1);
/*     */       }
/*     */ 
/*     */       
/* 199 */       if (str2 == null) {
/* 200 */         str2 = str;
/*     */       }
/* 202 */       getPkgList((Map)hashMap, str2).add(packageDoc);
/*     */     } 
/* 204 */     return (Map)hashMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String regExpGroupName(String paramString) {
/* 215 */     for (byte b = 0; b < this.sortedRegExpList.size(); b++) {
/* 216 */       String str = this.sortedRegExpList.get(b);
/* 217 */       if (paramString.startsWith(str)) {
/* 218 */         return this.regExpGroupMap.get(str);
/*     */       }
/*     */     } 
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<PackageDoc> getPkgList(Map<String, List<PackageDoc>> paramMap, String paramString) {
/* 232 */     List<PackageDoc> list = paramMap.get(paramString);
/* 233 */     if (list == null) {
/* 234 */       list = new ArrayList();
/* 235 */       paramMap.put(paramString, list);
/*     */     } 
/* 237 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getGroupList() {
/* 245 */     return this.groupList;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\Group.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */