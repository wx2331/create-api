/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InheritDocTaglet
/*     */   extends BaseInlineTaglet
/*     */ {
/*     */   public static final String INHERIT_DOC_INLINE_TAG = "{@inheritDoc}";
/*     */   
/*     */   public boolean inField() {
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inConstructor() {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inOverview() {
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inPackage() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inType() {
/* 104 */     return true;
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
/*     */   private Content retrieveInheritedDocumentation(TagletWriter paramTagletWriter, ProgramElementDoc paramProgramElementDoc, Tag paramTag, boolean paramBoolean) {
/* 120 */     Content content = paramTagletWriter.getOutputInstance();
/*     */     
/* 122 */     Configuration configuration = paramTagletWriter.configuration();
/*     */     
/* 124 */     Taglet taglet = (paramTag == null) ? null : configuration.tagletManager.getTaglet(paramTag.name());
/* 125 */     if (taglet != null && !(taglet instanceof InheritableTaglet)) {
/*     */ 
/*     */ 
/*     */       
/* 129 */       String str = paramProgramElementDoc.name() + ((paramProgramElementDoc instanceof ExecutableMemberDoc) ? ((ExecutableMemberDoc)paramProgramElementDoc).flatSignature() : "");
/*     */ 
/*     */       
/* 132 */       configuration.message.warning(paramProgramElementDoc.position(), "doclet.noInheritedDoc", new Object[] { str });
/*     */     } 
/*     */ 
/*     */     
/* 136 */     DocFinder.Output output = DocFinder.search(new DocFinder.Input(paramProgramElementDoc, (InheritableTaglet)taglet, paramTag, paramBoolean, true));
/*     */ 
/*     */     
/* 139 */     if (output.isValidInheritDocTag) {
/* 140 */       if (output.inlineTags.length > 0) {
/* 141 */         content = paramTagletWriter.commentTagsToOutput(output.holderTag, output.holder, output.inlineTags, paramBoolean);
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 147 */       String str = paramProgramElementDoc.name() + ((paramProgramElementDoc instanceof ExecutableMemberDoc) ? ((ExecutableMemberDoc)paramProgramElementDoc).flatSignature() : "");
/*     */       
/* 149 */       configuration.message.warning(paramProgramElementDoc.position(), "doclet.noInheritedDoc", new Object[] { str });
/*     */     } 
/*     */     
/* 152 */     return content;
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
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter) {
/* 164 */     if (!(paramTag.holder() instanceof ProgramElementDoc)) {
/* 165 */       return paramTagletWriter.getOutputInstance();
/*     */     }
/* 167 */     return paramTag.name().equals("@inheritDoc") ? 
/* 168 */       retrieveInheritedDocumentation(paramTagletWriter, (ProgramElementDoc)paramTag.holder(), null, paramTagletWriter.isFirstSentence) : 
/* 169 */       retrieveInheritedDocumentation(paramTagletWriter, (ProgramElementDoc)paramTag.holder(), paramTag, paramTagletWriter.isFirstSentence);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\InheritDocTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */