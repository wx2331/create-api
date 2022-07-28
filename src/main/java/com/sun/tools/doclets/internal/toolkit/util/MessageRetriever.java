/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.text.MessageFormat;
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
/*     */ public class MessageRetriever
/*     */ {
/*     */   private final Configuration configuration;
/*     */   private final String resourcelocation;
/*     */   private ResourceBundle messageRB;
/*     */   
/*     */   public MessageRetriever(ResourceBundle paramResourceBundle) {
/*  68 */     this.configuration = null;
/*  69 */     this.messageRB = paramResourceBundle;
/*  70 */     this.resourcelocation = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageRetriever(Configuration paramConfiguration, String paramString) {
/*  81 */     this.configuration = paramConfiguration;
/*  82 */     this.resourcelocation = paramString;
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
/*     */   public String getText(String paramString, Object... paramVarArgs) throws MissingResourceException {
/*  94 */     if (this.messageRB == null) {
/*     */       try {
/*  96 */         this.messageRB = ResourceBundle.getBundle(this.resourcelocation);
/*  97 */       } catch (MissingResourceException missingResourceException) {
/*  98 */         throw new Error("Fatal: Resource (" + this.resourcelocation + ") for javadoc doclets is missing.");
/*     */       } 
/*     */     }
/*     */     
/* 102 */     String str = this.messageRB.getString(paramString);
/* 103 */     return MessageFormat.format(str, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printError(SourcePosition paramSourcePosition, String paramString) {
/* 113 */     this.configuration.root.printError(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printError(String paramString) {
/* 122 */     this.configuration.root.printError(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printWarning(SourcePosition paramSourcePosition, String paramString) {
/* 132 */     this.configuration.root.printWarning(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printWarning(String paramString) {
/* 141 */     this.configuration.root.printWarning(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printNotice(SourcePosition paramSourcePosition, String paramString) {
/* 151 */     this.configuration.root.printNotice(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printNotice(String paramString) {
/* 160 */     this.configuration.root.printNotice(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(SourcePosition paramSourcePosition, String paramString, Object... paramVarArgs) {
/* 171 */     printError(paramSourcePosition, getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString, Object... paramVarArgs) {
/* 181 */     printError(getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(SourcePosition paramSourcePosition, String paramString, Object... paramVarArgs) {
/* 192 */     if (this.configuration.showMessage(paramSourcePosition, paramString)) {
/* 193 */       printWarning(paramSourcePosition, getText(paramString, paramVarArgs));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String paramString, Object... paramVarArgs) {
/* 203 */     printWarning(getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notice(SourcePosition paramSourcePosition, String paramString, Object... paramVarArgs) {
/* 214 */     printNotice(paramSourcePosition, getText(paramString, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notice(String paramString, Object... paramVarArgs) {
/* 224 */     printNotice(getText(paramString, paramVarArgs));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\MessageRetriever.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */