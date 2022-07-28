/*     */ package com.sun.xml.internal.dtdparser;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MessageCatalog
/*     */ {
/*     */   private String bundleName;
/*     */   private Hashtable cache;
/*     */   
/*     */   protected MessageCatalog(Class packageMember) {
/* 152 */     this(packageMember, "Messages");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MessageCatalog(Class packageMember, String bundle) {
/* 433 */     this.cache = new Hashtable<>(5);
/*     */     this.bundleName = packageMember.getName();
/*     */     int index = this.bundleName.lastIndexOf('.');
/*     */     if (index == -1) {
/*     */       this.bundleName = "";
/*     */     } else {
/*     */       this.bundleName = this.bundleName.substring(0, index) + ".";
/*     */     } 
/*     */     this.bundleName += "resources." + bundle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(Locale locale, String messageId) {
/*     */     ResourceBundle bundle;
/*     */     if (locale == null) {
/*     */       locale = Locale.getDefault();
/*     */     }
/*     */     try {
/*     */       bundle = ResourceBundle.getBundle(this.bundleName, locale);
/*     */     } catch (MissingResourceException e) {
/*     */       bundle = ResourceBundle.getBundle(this.bundleName, Locale.ENGLISH);
/*     */     } 
/*     */     return bundle.getString(messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocaleSupported(String localeName) {
/* 462 */     Boolean value = (Boolean)this.cache.get(localeName);
/*     */     
/* 464 */     if (value != null) {
/* 465 */       return value.booleanValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     ClassLoader loader = null;
/*     */     
/*     */     while (true) {
/* 475 */       String name = this.bundleName + "_" + localeName;
/*     */ 
/*     */       
/*     */       try {
/* 479 */         Class.forName(name);
/* 480 */         this.cache.put(localeName, Boolean.TRUE);
/* 481 */         return true;
/* 482 */       } catch (Exception exception) {
/*     */         InputStream in;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 488 */         if (loader == null) {
/* 489 */           loader = getClass().getClassLoader();
/*     */         }
/* 491 */         name = name.replace('.', '/');
/* 492 */         name = name + ".properties";
/* 493 */         if (loader == null) {
/* 494 */           in = ClassLoader.getSystemResourceAsStream(name);
/*     */         } else {
/* 496 */           in = loader.getResourceAsStream(name);
/* 497 */         }  if (in != null) {
/* 498 */           this.cache.put(localeName, Boolean.TRUE);
/* 499 */           return true;
/*     */         } 
/*     */         
/* 502 */         int index = localeName.indexOf('_');
/*     */         
/* 504 */         if (index > 0) {
/* 505 */           localeName = localeName.substring(0, index);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 513 */     this.cache.put(localeName, Boolean.FALSE);
/* 514 */     return false;
/*     */   }
/*     */   
/*     */   public String getMessage(Locale locale, String messageId, Object[] parameters) {
/*     */     ResourceBundle bundle;
/*     */     if (parameters == null)
/*     */       return getMessage(locale, messageId); 
/*     */     for (int i = 0; i < parameters.length; i++) {
/*     */       if (!(parameters[i] instanceof String) && !(parameters[i] instanceof Number) && !(parameters[i] instanceof java.util.Date))
/*     */         if (parameters[i] == null) {
/*     */           parameters[i] = "(null)";
/*     */         } else {
/*     */           parameters[i] = parameters[i].toString();
/*     */         }  
/*     */     } 
/*     */     if (locale == null)
/*     */       locale = Locale.getDefault(); 
/*     */     try {
/*     */       bundle = ResourceBundle.getBundle(this.bundleName, locale);
/*     */     } catch (MissingResourceException e) {
/*     */       bundle = ResourceBundle.getBundle(this.bundleName, Locale.ENGLISH);
/*     */     } 
/*     */     MessageFormat format = new MessageFormat(bundle.getString(messageId));
/*     */     format.setLocale(locale);
/*     */     StringBuffer result = new StringBuffer();
/*     */     result = format.format(parameters, result, new FieldPosition(0));
/*     */     return result.toString();
/*     */   }
/*     */   
/*     */   public Locale chooseLocale(String[] languages) {
/*     */     if ((languages = canonicalize(languages)) != null)
/*     */       for (int i = 0; i < languages.length; i++) {
/*     */         if (isLocaleSupported(languages[i]))
/*     */           return getLocale(languages[i]); 
/*     */       }  
/*     */     return null;
/*     */   }
/*     */   
/*     */   private String[] canonicalize(String[] languages) {
/*     */     boolean didClone = false;
/*     */     int trimCount = 0;
/*     */     if (languages == null)
/*     */       return languages; 
/*     */     for (int i = 0; i < languages.length; i++) {
/*     */       String lang = languages[i];
/*     */       int len = lang.length();
/*     */       if (len != 2 && len != 5) {
/*     */         if (!didClone) {
/*     */           languages = (String[])languages.clone();
/*     */           didClone = true;
/*     */         } 
/*     */         languages[i] = null;
/*     */         trimCount++;
/*     */       } else if (len == 2) {
/*     */         lang = lang.toLowerCase();
/*     */         if (lang != languages[i]) {
/*     */           if (!didClone) {
/*     */             languages = (String[])languages.clone();
/*     */             didClone = true;
/*     */           } 
/*     */           languages[i] = lang;
/*     */         } 
/*     */       } else {
/*     */         char[] buf = new char[5];
/*     */         buf[0] = Character.toLowerCase(lang.charAt(0));
/*     */         buf[1] = Character.toLowerCase(lang.charAt(1));
/*     */         buf[2] = '_';
/*     */         buf[3] = Character.toUpperCase(lang.charAt(3));
/*     */         buf[4] = Character.toUpperCase(lang.charAt(4));
/*     */         if (!didClone) {
/*     */           languages = (String[])languages.clone();
/*     */           didClone = true;
/*     */         } 
/*     */         languages[i] = new String(buf);
/*     */       } 
/*     */     } 
/*     */     if (trimCount != 0) {
/*     */       String[] temp = new String[languages.length - trimCount];
/*     */       for (int j = 0; j < temp.length; j++) {
/*     */         while (languages[j + trimCount] == null)
/*     */           trimCount++; 
/*     */         temp[j] = languages[j + trimCount];
/*     */       } 
/*     */       languages = temp;
/*     */     } 
/*     */     return languages;
/*     */   }
/*     */   
/*     */   private Locale getLocale(String localeName) {
/*     */     String language, country;
/*     */     int index = localeName.indexOf('_');
/*     */     if (index == -1) {
/*     */       if (localeName.equals("de"))
/*     */         return Locale.GERMAN; 
/*     */       if (localeName.equals("en"))
/*     */         return Locale.ENGLISH; 
/*     */       if (localeName.equals("fr"))
/*     */         return Locale.FRENCH; 
/*     */       if (localeName.equals("it"))
/*     */         return Locale.ITALIAN; 
/*     */       if (localeName.equals("ja"))
/*     */         return Locale.JAPANESE; 
/*     */       if (localeName.equals("ko"))
/*     */         return Locale.KOREAN; 
/*     */       if (localeName.equals("zh"))
/*     */         return Locale.CHINESE; 
/*     */       language = localeName;
/*     */       country = "";
/*     */     } else {
/*     */       if (localeName.equals("zh_CN"))
/*     */         return Locale.SIMPLIFIED_CHINESE; 
/*     */       if (localeName.equals("zh_TW"))
/*     */         return Locale.TRADITIONAL_CHINESE; 
/*     */       language = localeName.substring(0, index);
/*     */       country = localeName.substring(index + 1);
/*     */     } 
/*     */     return new Locale(language, country);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\MessageCatalog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */