/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Options
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*  45 */   public static final Context.Key<Options> optionsKey = new Context.Key<>();
/*     */   
/*     */   private LinkedHashMap<String, String> values;
/*     */   
/*     */   private List<Runnable> listeners;
/*     */   
/*     */   public static Options instance(Context paramContext) {
/*  52 */     Options options = paramContext.<Options>get(optionsKey);
/*  53 */     if (options == null)
/*  54 */       options = new Options(paramContext); 
/*  55 */     return options;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Options(Context paramContext) {
/* 162 */     this.listeners = List.nil(); this.values = new LinkedHashMap<>(); paramContext.put(optionsKey, this);
/*     */   }
/*     */   public String get(String paramString) { return this.values.get(paramString); }
/* 165 */   public String get(Option paramOption) { return this.values.get(paramOption.text); } public boolean getBoolean(String paramString) { return getBoolean(paramString, false); } public boolean getBoolean(String paramString, boolean paramBoolean) { String str = get(paramString); return (str == null) ? paramBoolean : Boolean.parseBoolean(str); } public boolean isSet(String paramString) { return (this.values.get(paramString) != null); } public boolean isSet(Option paramOption) { return (this.values.get(paramOption.text) != null); } public boolean isSet(Option paramOption, String paramString) { return (this.values.get(paramOption.text + paramString) != null); } public boolean isUnset(String paramString) { return (this.values.get(paramString) == null); } public void addListener(Runnable paramRunnable) { this.listeners = this.listeners.prepend(paramRunnable); }
/*     */   public boolean isUnset(Option paramOption) { return (this.values.get(paramOption.text) == null); }
/*     */   public boolean isUnset(Option paramOption, String paramString) { return (this.values.get(paramOption.text + paramString) == null); }
/*     */   public void put(String paramString1, String paramString2) { this.values.put(paramString1, paramString2); }
/* 169 */   public void put(Option paramOption, String paramString) { this.values.put(paramOption.text, paramString); } public void putAll(Options paramOptions) { this.values.putAll(paramOptions.values); } public void remove(String paramString) { this.values.remove(paramString); } public Set<String> keySet() { return this.values.keySet(); } public int size() { return this.values.size(); } public void notifyListeners() { for (Runnable runnable : this.listeners) {
/* 170 */       runnable.run();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean lint(String paramString) {
/* 178 */     return (
/* 179 */       isSet(Option.XLINT_CUSTOM, paramString) || ((
/* 180 */       isSet(Option.XLINT) || isSet(Option.XLINT_CUSTOM, "all")) && 
/* 181 */       isUnset(Option.XLINT_CUSTOM, "-" + paramString)));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Options.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */