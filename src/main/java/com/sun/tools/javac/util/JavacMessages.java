/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavacMessages
/*     */   implements Messages
/*     */ {
/*  47 */   public static final Context.Key<JavacMessages> messagesKey = new Context.Key<>();
/*     */   
/*     */   private Map<Locale, SoftReference<List<ResourceBundle>>> bundleCache;
/*     */   
/*     */   public static JavacMessages instance(Context paramContext) {
/*  52 */     JavacMessages javacMessages = paramContext.<JavacMessages>get(messagesKey);
/*  53 */     if (javacMessages == null)
/*  54 */       javacMessages = new JavacMessages(paramContext); 
/*  55 */     return javacMessages;
/*     */   }
/*     */   
/*     */   private List<String> bundleNames;
/*     */   private Locale currentLocale;
/*     */   private List<ResourceBundle> currentBundles;
/*     */   private static final String defaultBundleName = "com.sun.tools.javac.resources.compiler";
/*     */   private static ResourceBundle defaultBundle;
/*     */   private static JavacMessages defaultMessages;
/*     */   
/*     */   public Locale getCurrentLocale() {
/*  66 */     return this.currentLocale;
/*     */   }
/*     */   
/*     */   public void setCurrentLocale(Locale paramLocale) {
/*  70 */     if (paramLocale == null) {
/*  71 */       paramLocale = Locale.getDefault();
/*     */     }
/*  73 */     this.currentBundles = getBundles(paramLocale);
/*  74 */     this.currentLocale = paramLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JavacMessages(Context paramContext) {
/*  80 */     this("com.sun.tools.javac.resources.compiler", paramContext.<Locale>get(Locale.class));
/*  81 */     paramContext.put(messagesKey, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavacMessages(String paramString) throws MissingResourceException {
/*  88 */     this(paramString, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavacMessages(String paramString, Locale paramLocale) throws MissingResourceException {
/*  95 */     this.bundleNames = List.nil();
/*  96 */     this.bundleCache = new HashMap<>();
/*  97 */     add(paramString);
/*  98 */     setCurrentLocale(paramLocale);
/*     */   }
/*     */   
/*     */   public JavacMessages() throws MissingResourceException {
/* 102 */     this("com.sun.tools.javac.resources.compiler");
/*     */   }
/*     */   
/*     */   public void add(String paramString) throws MissingResourceException {
/* 106 */     this.bundleNames = this.bundleNames.prepend(paramString);
/* 107 */     if (!this.bundleCache.isEmpty())
/* 108 */       this.bundleCache.clear(); 
/* 109 */     this.currentBundles = null;
/*     */   }
/*     */   
/*     */   public List<ResourceBundle> getBundles(Locale paramLocale) {
/* 113 */     if (paramLocale == this.currentLocale && this.currentBundles != null)
/* 114 */       return this.currentBundles; 
/* 115 */     SoftReference<List> softReference = (SoftReference)this.bundleCache.get(paramLocale);
/* 116 */     List<?> list = (softReference == null) ? null : softReference.get();
/* 117 */     if (list == null) {
/* 118 */       list = List.nil();
/* 119 */       for (String str : this.bundleNames) {
/*     */         try {
/* 121 */           ResourceBundle resourceBundle = ResourceBundle.getBundle(str, paramLocale);
/* 122 */           list = list.prepend(resourceBundle);
/* 123 */         } catch (MissingResourceException missingResourceException) {
/* 124 */           throw new InternalError("Cannot find javac resource bundle for locale " + paramLocale);
/*     */         } 
/*     */       } 
/* 127 */       this.bundleCache.put(paramLocale, (SoftReference)new SoftReference<>(list));
/*     */     } 
/* 129 */     return (List)list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocalizedString(String paramString, Object... paramVarArgs) {
/* 135 */     return getLocalizedString(this.currentLocale, paramString, paramVarArgs);
/*     */   }
/*     */   
/*     */   public String getLocalizedString(Locale paramLocale, String paramString, Object... paramVarArgs) {
/* 139 */     if (paramLocale == null)
/* 140 */       paramLocale = getCurrentLocale(); 
/* 141 */     return getLocalizedString(getBundles(paramLocale), paramString, paramVarArgs);
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
/*     */   static String getDefaultLocalizedString(String paramString, Object... paramVarArgs) {
/* 161 */     return getLocalizedString(List.of(getDefaultBundle()), paramString, paramVarArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   static JavacMessages getDefaultMessages() {
/* 167 */     if (defaultMessages == null)
/* 168 */       defaultMessages = new JavacMessages("com.sun.tools.javac.resources.compiler"); 
/* 169 */     return defaultMessages;
/*     */   }
/*     */   
/*     */   public static ResourceBundle getDefaultBundle() {
/*     */     try {
/* 174 */       if (defaultBundle == null)
/* 175 */         defaultBundle = ResourceBundle.getBundle("com.sun.tools.javac.resources.compiler"); 
/* 176 */       return defaultBundle;
/*     */     }
/* 178 */     catch (MissingResourceException missingResourceException) {
/* 179 */       throw new Error("Fatal: Resource for compiler is missing", missingResourceException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getLocalizedString(List<ResourceBundle> paramList, String paramString, Object... paramVarArgs) {
/* 186 */     String str = null;
/* 187 */     for (List<ResourceBundle> list = paramList; list.nonEmpty() && str == null; list = list.tail) {
/* 188 */       ResourceBundle resourceBundle = (ResourceBundle)list.head;
/*     */       try {
/* 190 */         str = resourceBundle.getString(paramString);
/*     */       }
/* 192 */       catch (MissingResourceException missingResourceException) {}
/*     */     } 
/*     */ 
/*     */     
/* 196 */     if (str == null) {
/* 197 */       str = "compiler message file broken: key=" + paramString + " arguments={0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}";
/*     */     }
/*     */     
/* 200 */     return MessageFormat.format(str, paramVarArgs);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\JavacMessages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */