/*     */ package com.sun.tools.doclint;
/*     */
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.tools.Diagnostic;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Messages
/*     */ {
/*     */   private final Options options;
/*     */   private final Stats stats;
/*     */   ResourceBundle bundle;
/*     */   Env env;
/*     */
/*     */   public enum Group
/*     */   {
/*  65 */     ACCESSIBILITY,
/*  66 */     HTML,
/*  67 */     MISSING,
/*  68 */     SYNTAX,
/*  69 */     REFERENCE;
/*     */
/*  71 */     String optName() { return StringUtils.toLowerCase(name()); } String notOptName() {
/*  72 */       return "-" + optName();
/*     */     }
/*     */     static boolean accepts(String param1String) {
/*  75 */       for (Group group : values()) {
/*  76 */         if (param1String.equals(group.optName())) return true;
/*  77 */       }  return false;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Messages(Env paramEnv) {
/*  88 */     this.env = paramEnv;
/*  89 */     String str = getClass().getPackage().getName() + ".resources.doclint";
/*  90 */     this.bundle = ResourceBundle.getBundle(str, Locale.ENGLISH);
/*     */
/*  92 */     this.stats = new Stats(this.bundle);
/*  93 */     this.options = new Options(this.stats);
/*     */   }
/*     */
/*     */   void error(Group paramGroup, DocTree paramDocTree, String paramString, Object... paramVarArgs) {
/*  97 */     report(paramGroup, Diagnostic.Kind.ERROR, paramDocTree, paramString, paramVarArgs);
/*     */   }
/*     */
/*     */   void warning(Group paramGroup, DocTree paramDocTree, String paramString, Object... paramVarArgs) {
/* 101 */     report(paramGroup, Diagnostic.Kind.WARNING, paramDocTree, paramString, paramVarArgs);
/*     */   }
/*     */
/*     */   void setOptions(String paramString) {
/* 105 */     this.options.setOptions(paramString);
/*     */   }
/*     */
/*     */   void setStatsEnabled(boolean paramBoolean) {
/* 109 */     this.stats.setEnabled(paramBoolean);
/*     */   }
/*     */
/*     */   void reportStats(PrintWriter paramPrintWriter) {
/* 113 */     this.stats.report(paramPrintWriter);
/*     */   }
/*     */
/*     */   protected void report(Group paramGroup, Diagnostic.Kind paramKind, DocTree paramDocTree, String paramString, Object... paramVarArgs) {
/* 117 */     if (this.options.isEnabled(paramGroup, this.env.currAccess)) {
/* 118 */       String str = (paramString == null) ? (String)paramVarArgs[0] : localize(paramString, paramVarArgs);
/* 119 */       this.env.trees.printMessage(paramKind, str, paramDocTree, this.env.currDocComment, this.env.currPath
/* 120 */           .getCompilationUnit());
/*     */
/* 122 */       this.stats.record(paramGroup, paramKind, paramString);
/*     */     }
/*     */   }
/*     */
/*     */   protected void report(Group paramGroup, Diagnostic.Kind paramKind, Tree paramTree, String paramString, Object... paramVarArgs) {
/* 127 */     if (this.options.isEnabled(paramGroup, this.env.currAccess)) {
/* 128 */       String str = localize(paramString, paramVarArgs);
/* 129 */       this.env.trees.printMessage(paramKind, str, paramTree, this.env.currPath.getCompilationUnit());
/*     */
/* 131 */       this.stats.record(paramGroup, paramKind, paramString);
/*     */     }
/*     */   }
/*     */
/*     */   String localize(String paramString, Object... paramVarArgs) {
/* 136 */     String str = this.bundle.getString(paramString);
/* 137 */     if (str == null) {
/* 138 */       StringBuilder stringBuilder = new StringBuilder();
/* 139 */       stringBuilder.append("message file broken: code=").append(paramString);
/* 140 */       if (paramVarArgs.length > 0) {
/* 141 */         stringBuilder.append(" arguments={0}");
/* 142 */         for (byte b = 1; b < paramVarArgs.length; b++) {
/* 143 */           stringBuilder.append(", {").append(b).append("}");
/*     */         }
/*     */       }
/* 146 */       str = stringBuilder.toString();
/*     */     }
/* 148 */     return MessageFormat.format(str, paramVarArgs);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static class Options
/*     */   {
/* 157 */     Map<String, Env.AccessKind> map = new HashMap<>(); private final Stats stats;
/*     */     private static final String ALL = "all";
/*     */
/*     */     static boolean isValidOptions(String param1String) {
/* 161 */       for (String str : param1String.split(",")) {
/* 162 */         if (!isValidOption(StringUtils.toLowerCase(str.trim())))
/* 163 */           return false;
/*     */       }
/* 165 */       return true;
/*     */     }
/*     */
/*     */     private static boolean isValidOption(String param1String) {
/* 169 */       if (param1String.equals("none") || param1String.equals("stats")) {
/* 170 */         return true;
/*     */       }
/* 172 */       boolean bool = param1String.startsWith("-") ? true : false;
/* 173 */       int i = param1String.indexOf("/");
/* 174 */       String str = param1String.substring(bool, (i != -1) ? i : param1String.length());
/* 175 */       return (((!bool && str.equals("all")) || Group.accepts(str)) && (i == -1 ||
/* 176 */         Env.AccessKind.accepts(param1String.substring(i + 1))));
/*     */     }
/*     */
/*     */     Options(Stats param1Stats) {
/* 180 */       this.stats = param1Stats;
/*     */     }
/*     */
/*     */
/*     */     boolean isEnabled(Group param1Group, Env.AccessKind param1AccessKind) {
/* 185 */       if (this.map.isEmpty()) {
/* 186 */         this.map.put("all", Env.AccessKind.PROTECTED);
/*     */       }
/* 188 */       Env.AccessKind accessKind = this.map.get(param1Group.optName());
/* 189 */       if (accessKind != null && param1AccessKind.compareTo(accessKind) >= 0) {
/* 190 */         return true;
/*     */       }
/* 192 */       accessKind = this.map.get("all");
/* 193 */       if (accessKind != null && param1AccessKind.compareTo(accessKind) >= 0) {
/* 194 */         accessKind = this.map.get(param1Group.notOptName());
/* 195 */         if (accessKind == null || param1AccessKind.compareTo(accessKind) > 0) {
/* 196 */           return true;
/*     */         }
/*     */       }
/* 199 */       return false;
/*     */     }
/*     */
/*     */     void setOptions(String param1String) {
/* 203 */       if (param1String == null) {
/* 204 */         setOption("all", Env.AccessKind.PRIVATE);
/*     */       } else {
/* 206 */         for (String str : param1String.split(","))
/* 207 */           setOption(StringUtils.toLowerCase(str.trim()));
/*     */       }
/*     */     }
/*     */
/*     */     private void setOption(String param1String) throws IllegalArgumentException {
/* 212 */       if (param1String.equals("stats")) {
/* 213 */         this.stats.setEnabled(true);
/*     */
/*     */         return;
/*     */       }
/* 217 */       int i = param1String.indexOf("/");
/* 218 */       if (i > 0) {
/* 219 */         Env.AccessKind accessKind = Env.AccessKind.valueOf(StringUtils.toUpperCase(param1String.substring(i + 1)));
/* 220 */         setOption(param1String.substring(0, i), accessKind);
/*     */       } else {
/* 222 */         setOption(param1String, null);
/*     */       }
/*     */     }
/*     */
/*     */     private void setOption(String param1String, Env.AccessKind param1AccessKind) {
/* 227 */       this.map.put(param1String, (param1AccessKind != null) ? param1AccessKind : (
/* 228 */           param1String.startsWith("-") ? Env.AccessKind.PUBLIC : Env.AccessKind.PRIVATE));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   static class Stats
/*     */   {
/*     */     public static final String OPT = "stats";
/*     */
/*     */
/*     */     public static final String NO_CODE = "";
/*     */
/*     */
/*     */     final ResourceBundle bundle;
/*     */
/*     */     int[] groupCounts;
/*     */
/*     */     int[] dkindCounts;
/*     */
/*     */     Map<String, Integer> codeCounts;
/*     */
/*     */
/*     */     Stats(ResourceBundle param1ResourceBundle) {
/* 252 */       this.bundle = param1ResourceBundle;
/*     */     }
/*     */
/*     */     void setEnabled(boolean param1Boolean) {
/* 256 */       if (param1Boolean) {
/* 257 */         this.groupCounts = new int[(Group.values()).length];
/* 258 */         this.dkindCounts = new int[(Diagnostic.Kind.values()).length];
/* 259 */         this.codeCounts = new HashMap<>();
/*     */       } else {
/* 261 */         this.groupCounts = null;
/* 262 */         this.dkindCounts = null;
/* 263 */         this.codeCounts = null;
/*     */       }
/*     */     }
/*     */
/*     */     void record(Group param1Group, Diagnostic.Kind param1Kind, String param1String) {
/* 268 */       if (this.codeCounts == null) {
/*     */         return;
/*     */       }
/* 271 */       this.groupCounts[param1Group.ordinal()] = this.groupCounts[param1Group.ordinal()] + 1;
/* 272 */       this.dkindCounts[param1Kind.ordinal()] = this.dkindCounts[param1Kind.ordinal()] + 1;
/* 273 */       if (param1String == null) {
/* 274 */         param1String = "";
/*     */       }
/* 276 */       Integer integer = this.codeCounts.get(param1String);
/* 277 */       this.codeCounts.put(param1String, Integer.valueOf((integer == null) ? 1 : (integer.intValue() + 1)));
/*     */     }
/*     */
/*     */     void report(PrintWriter param1PrintWriter) {
/* 281 */       if (this.codeCounts == null) {
/*     */         return;
/*     */       }
/* 284 */       param1PrintWriter.println("By group...");
/* 285 */       Table table1 = new Table();
/* 286 */       for (Group group : Group.values()) {
/* 287 */         table1.put(group.optName(), this.groupCounts[group.ordinal()]);
/*     */       }
/* 289 */       table1.print(param1PrintWriter);
/* 290 */       param1PrintWriter.println();
/* 291 */       param1PrintWriter.println("By diagnostic kind...");
/* 292 */       Table table2 = new Table();
/* 293 */       for (Diagnostic.Kind kind : Diagnostic.Kind.values()) {
/* 294 */         table2.put(StringUtils.toLowerCase(kind.toString()), this.dkindCounts[kind.ordinal()]);
/*     */       }
/* 296 */       table2.print(param1PrintWriter);
/* 297 */       param1PrintWriter.println();
/* 298 */       param1PrintWriter.println("By message kind...");
/* 299 */       Table table3 = new Table();
/* 300 */       for (Map.Entry<String, Integer> entry : this.codeCounts.entrySet()) {
/* 301 */         String str2, str1 = (String)entry.getKey();
/*     */
/*     */         try {
/* 304 */           str2 = str1.equals("") ? "OTHER" : this.bundle.getString(str1);
/* 305 */         } catch (MissingResourceException missingResourceException) {
/* 306 */           str2 = str1;
/*     */         }
/* 308 */         table3.put(str2, ((Integer)entry.getValue()).intValue());
/*     */       }
/* 310 */       table3.print(param1PrintWriter);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     private static class Table
/*     */     {
/* 318 */       private static final Comparator<Integer> DECREASING = new Comparator<Integer>()
/*     */         {
/*     */           public int compare(Integer param3Integer1, Integer param3Integer2) {
/* 321 */             return param3Integer2.compareTo(param3Integer1);
/*     */           }
/*     */         };
/* 324 */       private final TreeMap<Integer, Set<String>> map = new TreeMap<>(DECREASING);
/*     */
/*     */       void put(String param2String, int param2Int) {
/* 327 */         if (param2Int == 0) {
/*     */           return;
/*     */         }
/* 330 */         Set<String> set = this.map.get(Integer.valueOf(param2Int));
/* 331 */         if (set == null) {
/* 332 */           this.map.put(Integer.valueOf(param2Int), set = new TreeSet<>());
/*     */         }
/* 334 */         set.add(param2String);
/*     */       }
/*     */
/*     */       void print(PrintWriter param2PrintWriter) {
/* 338 */         for (Map.Entry<Integer, Set<String>> entry : this.map.entrySet()) {
/* 339 */           int i = ((Integer)entry.getKey()).intValue();
/* 340 */           Set set = (Set)entry.getValue();
/* 341 */           for (String str : set) {
/* 342 */             param2PrintWriter.println(String.format("%6d: %s", new Object[] { Integer.valueOf(i), str }));
/*     */           }
/*     */         }
/*     */       }
/*     */
/*     */       private Table() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclint\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
