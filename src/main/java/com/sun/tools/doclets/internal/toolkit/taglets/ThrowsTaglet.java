/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
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
/*     */ public class ThrowsTaglet
/*     */   extends BaseExecutableMemberTaglet
/*     */   implements InheritableTaglet
/*     */ {
/*     */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput) {
/*     */     ClassDoc classDoc;
/*  57 */     if (paramInput.tagId == null) {
/*  58 */       ThrowsTag throwsTag = (ThrowsTag)paramInput.tag;
/*  59 */       classDoc = throwsTag.exception();
/*  60 */       paramInput
/*     */         
/*  62 */         .tagId = (classDoc == null) ? throwsTag.exceptionName() : throwsTag.exception().qualifiedName();
/*     */     } else {
/*  64 */       classDoc = paramInput.element.containingClass().findClass(paramInput.tagId);
/*     */     } 
/*     */     
/*  67 */     ThrowsTag[] arrayOfThrowsTag = ((MethodDoc)paramInput.element).throwsTags();
/*  68 */     for (byte b = 0; b < arrayOfThrowsTag.length; b++) {
/*  69 */       if (paramInput.tagId.equals(arrayOfThrowsTag[b].exceptionName()) || (arrayOfThrowsTag[b]
/*  70 */         .exception() != null && paramInput.tagId
/*  71 */         .equals(arrayOfThrowsTag[b].exception().qualifiedName()))) {
/*  72 */         paramOutput.holder = (Doc)paramInput.element;
/*  73 */         paramOutput.holderTag = (Tag)arrayOfThrowsTag[b];
/*  74 */         paramOutput
/*  75 */           .inlineTags = paramInput.isFirstSentence ? arrayOfThrowsTag[b].firstSentenceTags() : arrayOfThrowsTag[b].inlineTags();
/*  76 */         paramOutput.tagList.add(arrayOfThrowsTag[b]);
/*  77 */       } else if (classDoc != null && arrayOfThrowsTag[b].exception() != null && arrayOfThrowsTag[b]
/*  78 */         .exception().subclassOf(classDoc)) {
/*  79 */         paramOutput.tagList.add(arrayOfThrowsTag[b]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content linkToUndocumentedDeclaredExceptions(Type[] paramArrayOfType, Set<String> paramSet, TagletWriter paramTagletWriter) {
/*  90 */     Content content = paramTagletWriter.getOutputInstance();
/*     */     
/*  92 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/*  93 */       if (paramArrayOfType[b].asClassDoc() != null && 
/*  94 */         !paramSet.contains(paramArrayOfType[b]
/*  95 */           .asClassDoc().name()) && 
/*  96 */         !paramSet.contains(paramArrayOfType[b]
/*  97 */           .asClassDoc().qualifiedName())) {
/*  98 */         if (paramSet.size() == 0) {
/*  99 */           content.addContent(paramTagletWriter.getThrowsHeader());
/*     */         }
/* 101 */         content.addContent(paramTagletWriter.throwsTagOutput(paramArrayOfType[b]));
/* 102 */         paramSet.add(paramArrayOfType[b].asClassDoc().name());
/*     */       } 
/*     */     } 
/* 105 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content inheritThrowsDocumentation(Doc paramDoc, Type[] paramArrayOfType, Set<String> paramSet, TagletWriter paramTagletWriter) {
/* 115 */     Content content = paramTagletWriter.getOutputInstance();
/* 116 */     if (paramDoc instanceof MethodDoc) {
/* 117 */       LinkedHashSet linkedHashSet = new LinkedHashSet();
/* 118 */       for (byte b = 0; b < paramArrayOfType.length; b++) {
/*     */         
/* 120 */         DocFinder.Output output = DocFinder.search(new DocFinder.Input((ProgramElementDoc)paramDoc, this, paramArrayOfType[b]
/* 121 */               .typeName()));
/* 122 */         if (output.tagList.size() == 0) {
/* 123 */           output = DocFinder.search(new DocFinder.Input((ProgramElementDoc)paramDoc, this, paramArrayOfType[b]
/*     */                 
/* 125 */                 .qualifiedTypeName()));
/*     */         }
/* 127 */         linkedHashSet.addAll(output.tagList);
/*     */       } 
/* 129 */       content.addContent(throwsTagsOutput((ThrowsTag[])linkedHashSet
/* 130 */             .toArray((Object[])new ThrowsTag[0]), paramTagletWriter, paramSet, false));
/*     */     } 
/*     */     
/* 133 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter) {
/* 140 */     ExecutableMemberDoc executableMemberDoc = (ExecutableMemberDoc)paramDoc;
/* 141 */     ThrowsTag[] arrayOfThrowsTag = executableMemberDoc.throwsTags();
/* 142 */     Content content = paramTagletWriter.getOutputInstance();
/* 143 */     HashSet<String> hashSet = new HashSet();
/* 144 */     if (arrayOfThrowsTag.length > 0) {
/* 145 */       content.addContent(throwsTagsOutput(executableMemberDoc
/* 146 */             .throwsTags(), paramTagletWriter, hashSet, true));
/*     */     }
/* 148 */     content.addContent(inheritThrowsDocumentation(paramDoc, executableMemberDoc
/* 149 */           .thrownExceptionTypes(), hashSet, paramTagletWriter));
/* 150 */     content.addContent(linkToUndocumentedDeclaredExceptions(executableMemberDoc
/* 151 */           .thrownExceptionTypes(), hashSet, paramTagletWriter));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content throwsTagsOutput(ThrowsTag[] paramArrayOfThrowsTag, TagletWriter paramTagletWriter, Set<String> paramSet, boolean paramBoolean) {
/* 168 */     Content content = paramTagletWriter.getOutputInstance();
/* 169 */     if (paramArrayOfThrowsTag.length > 0)
/* 170 */       for (byte b = 0; b < paramArrayOfThrowsTag.length; b++) {
/* 171 */         ThrowsTag throwsTag = paramArrayOfThrowsTag[b];
/* 172 */         ClassDoc classDoc = throwsTag.exception();
/* 173 */         if (paramBoolean || (!paramSet.contains(throwsTag.exceptionName()) && (classDoc == null || 
/* 174 */           !paramSet.contains(classDoc.qualifiedName())))) {
/*     */ 
/*     */           
/* 177 */           if (paramSet.size() == 0) {
/* 178 */             content.addContent(paramTagletWriter.getThrowsHeader());
/*     */           }
/* 180 */           content.addContent(paramTagletWriter.throwsTagOutput(throwsTag));
/* 181 */           paramSet.add((classDoc != null) ? classDoc
/* 182 */               .qualifiedName() : throwsTag.exceptionName());
/*     */         } 
/*     */       }  
/* 185 */     return content;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\ThrowsTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */