/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TagletWriter
/*     */ {
/*     */   protected final boolean isFirstSentence;
/*     */   
/*     */   protected TagletWriter(boolean paramBoolean) {
/*  52 */     this.isFirstSentence = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Content getOutputInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content codeTagOutput(Tag paramTag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getDocRootOutput();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content deprecatedTagOutput(Doc paramDoc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content literalTagOutput(Tag paramTag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract MessageRetriever getMsgRetriever();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getParamHeader(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content paramTagOutput(ParamTag paramParamTag, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content propertyTagOutput(Tag paramTag, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content returnTagOutput(Tag paramTag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content seeTagOutput(Doc paramDoc, SeeTag[] paramArrayOfSeeTag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content simpleTagOutput(Tag[] paramArrayOfTag, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content simpleTagOutput(Tag paramTag, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getThrowsHeader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content throwsTagOutput(ThrowsTag paramThrowsTag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content throwsTagOutput(Type paramType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content valueTagOutput(FieldDoc paramFieldDoc, String paramString, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void genTagOuput(TagletManager paramTagletManager, Doc paramDoc, Taglet[] paramArrayOfTaglet, TagletWriter paramTagletWriter, Content paramContent) {
/* 204 */     paramTagletManager.checkTags(paramDoc, paramDoc.tags(), false);
/* 205 */     paramTagletManager.checkTags(paramDoc, paramDoc.inlineTags(), true);
/* 206 */     Content content = null;
/* 207 */     for (byte b = 0; b < paramArrayOfTaglet.length; b++) {
/* 208 */       content = null;
/* 209 */       if (!(paramDoc instanceof com.sun.javadoc.ClassDoc) || !(paramArrayOfTaglet[b] instanceof ParamTaglet))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 214 */         if (!(paramArrayOfTaglet[b] instanceof DeprecatedTaglet)) {
/*     */ 
/*     */           
/*     */           try {
/*     */ 
/*     */             
/* 220 */             content = paramArrayOfTaglet[b].getTagletOutput(paramDoc, paramTagletWriter);
/* 221 */           } catch (IllegalArgumentException illegalArgumentException) {
/*     */ 
/*     */             
/* 224 */             Tag[] arrayOfTag = paramDoc.tags(paramArrayOfTaglet[b].getName());
/* 225 */             if (arrayOfTag.length > 0) {
/* 226 */               content = paramArrayOfTaglet[b].getTagletOutput(arrayOfTag[0], paramTagletWriter);
/*     */             }
/*     */           } 
/* 229 */           if (content != null) {
/* 230 */             paramTagletManager.seenCustomTag(paramArrayOfTaglet[b].getName());
/* 231 */             paramContent.addContent(content);
/*     */           } 
/*     */         } 
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
/*     */   public static Content getInlineTagOuput(TagletManager paramTagletManager, Tag paramTag1, Tag paramTag2, TagletWriter paramTagletWriter) {
/* 247 */     Taglet[] arrayOfTaglet = paramTagletManager.getInlineCustomTaglets();
/*     */     
/* 249 */     for (byte b = 0; b < arrayOfTaglet.length; b++) {
/* 250 */       if (("@" + arrayOfTaglet[b].getName()).equals(paramTag2.name())) {
/*     */ 
/*     */         
/* 253 */         paramTagletManager.seenCustomTag(arrayOfTaglet[b].getName());
/* 254 */         return arrayOfTaglet[b].getTagletOutput((paramTag1 != null && arrayOfTaglet[b]
/*     */             
/* 256 */             .getName().equals("inheritDoc")) ? paramTag1 : paramTag2, paramTagletWriter);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 261 */     return null;
/*     */   }
/*     */   
/*     */   public abstract Content commentTagsToOutput(Tag paramTag, Tag[] paramArrayOfTag);
/*     */   
/*     */   public abstract Content commentTagsToOutput(Doc paramDoc, Tag[] paramArrayOfTag);
/*     */   
/*     */   public abstract Content commentTagsToOutput(Tag paramTag, Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean);
/*     */   
/*     */   public abstract Configuration configuration();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\TagletWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */