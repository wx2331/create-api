/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkInfoImpl
/*     */   extends LinkInfo
/*     */ {
/*     */   public final ConfigurationImpl configuration;
/*     */   
/*     */   public enum Kind
/*     */   {
/*  45 */     DEFAULT,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     ALL_CLASSES_FRAME,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     CLASS,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     MEMBER,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     CLASS_USE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     INDEX,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     CONSTANT_SUMMARY,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     SERIALIZED_FORM,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     SERIAL_MEMBER,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     PACKAGE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     SEE_TAG,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     VALUE_TAG,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     TREE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     PACKAGE_FRAME,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     CLASS_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     CLASS_SIGNATURE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     RETURN_TYPE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     SUMMARY_RETURN_TYPE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     EXECUTABLE_MEMBER_PARAM,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     SUPER_INTERFACES,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     IMPLEMENTED_INTERFACES,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     IMPLEMENTED_CLASSES,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     SUBINTERFACES,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     SUBCLASSES,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     CLASS_SIGNATURE_PARENT_NAME,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     METHOD_DOC_COPY,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     METHOD_SPECIFIED_BY,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     METHOD_OVERRIDES,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     ANNOTATION,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     FIELD_DOC_COPY,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     CLASS_TREE_PARENT,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     MEMBER_TYPE_PARAMS,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     CLASS_USE_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     PROPERTY_DOC_COPY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public Kind context = Kind.DEFAULT;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   public String where = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public String styleName = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   public String target = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl(ConfigurationImpl paramConfigurationImpl, Kind paramKind, ExecutableMemberDoc paramExecutableMemberDoc) {
/* 245 */     this.configuration = paramConfigurationImpl;
/* 246 */     this.executableMemberDoc = paramExecutableMemberDoc;
/* 247 */     setContext(paramKind);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content newContent() {
/* 254 */     return (Content)new ContentBuilder();
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
/*     */   public LinkInfoImpl(ConfigurationImpl paramConfigurationImpl, Kind paramKind, ClassDoc paramClassDoc) {
/* 266 */     this.configuration = paramConfigurationImpl;
/* 267 */     this.classDoc = paramClassDoc;
/* 268 */     setContext(paramKind);
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
/*     */   public LinkInfoImpl(ConfigurationImpl paramConfigurationImpl, Kind paramKind, Type paramType) {
/* 280 */     this.configuration = paramConfigurationImpl;
/* 281 */     this.type = paramType;
/* 282 */     setContext(paramKind);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl label(String paramString) {
/* 291 */     this.label = (Content)new StringContent(paramString);
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl label(Content paramContent) {
/* 299 */     this.label = paramContent;
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl strong(boolean paramBoolean) {
/* 307 */     this.isStrong = paramBoolean;
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl styleName(String paramString) {
/* 316 */     this.styleName = paramString;
/* 317 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl target(String paramString) {
/* 325 */     this.target = paramString;
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl varargs(boolean paramBoolean) {
/* 333 */     this.isVarArg = paramBoolean;
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkInfoImpl where(String paramString) {
/* 341 */     this.where = paramString;
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Kind getContext() {
/* 349 */     return this.context;
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
/*     */   public final void setContext(Kind paramKind) {
/* 362 */     switch (paramKind) {
/*     */       case ALL_CLASSES_FRAME:
/*     */       case PACKAGE_FRAME:
/*     */       case IMPLEMENTED_CLASSES:
/*     */       case SUBCLASSES:
/*     */       case METHOD_DOC_COPY:
/*     */       case FIELD_DOC_COPY:
/*     */       case PROPERTY_DOC_COPY:
/*     */       case CLASS_USE_HEADER:
/* 371 */         this.includeTypeInClassLinkLabel = false;
/*     */         break;
/*     */       
/*     */       case ANNOTATION:
/* 375 */         this.excludeTypeParameterLinks = true;
/* 376 */         this.excludeTypeBounds = true;
/*     */         break;
/*     */       
/*     */       case IMPLEMENTED_INTERFACES:
/*     */       case SUPER_INTERFACES:
/*     */       case SUBINTERFACES:
/*     */       case CLASS_TREE_PARENT:
/*     */       case TREE:
/*     */       case CLASS_SIGNATURE_PARENT_NAME:
/* 385 */         this.excludeTypeParameterLinks = true;
/* 386 */         this.excludeTypeBounds = true;
/* 387 */         this.includeTypeInClassLinkLabel = false;
/* 388 */         this.includeTypeAsSepLink = true;
/*     */         break;
/*     */       
/*     */       case PACKAGE:
/*     */       case CLASS_USE:
/*     */       case CLASS_HEADER:
/*     */       case CLASS_SIGNATURE:
/* 395 */         this.excludeTypeParameterLinks = true;
/* 396 */         this.includeTypeAsSepLink = true;
/* 397 */         this.includeTypeInClassLinkLabel = false;
/*     */         break;
/*     */       
/*     */       case MEMBER_TYPE_PARAMS:
/* 401 */         this.includeTypeAsSepLink = true;
/* 402 */         this.includeTypeInClassLinkLabel = false;
/*     */         break;
/*     */       
/*     */       case RETURN_TYPE:
/*     */       case SUMMARY_RETURN_TYPE:
/* 407 */         this.excludeTypeBounds = true;
/*     */         break;
/*     */       case EXECUTABLE_MEMBER_PARAM:
/* 410 */         this.excludeTypeBounds = true;
/*     */         break;
/*     */     } 
/* 413 */     this.context = paramKind;
/* 414 */     if (this.type != null && this.type
/* 415 */       .asTypeVariable() != null && this.type
/* 416 */       .asTypeVariable().owner() instanceof ExecutableMemberDoc) {
/* 417 */       this.excludeTypeParameterLinks = true;
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
/*     */   public boolean isLinkable() {
/* 429 */     return Util.isLinkable(this.classDoc, this.configuration);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\LinkInfoImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */