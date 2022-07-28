/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletManager;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HtmlSerialMethodWriter
/*     */   extends MethodWriterImpl
/*     */   implements SerializedFormWriter.SerialMethodWriter
/*     */ {
/*     */   public HtmlSerialMethodWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  50 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSerializableMethodsHeader() {
/*  59 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/*  60 */     htmlTree.addStyle(HtmlStyle.blockList);
/*  61 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMethodsContentHeader(boolean paramBoolean) {
/*  71 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/*  72 */     if (paramBoolean) {
/*  73 */       htmlTree.addStyle(HtmlStyle.blockListLast);
/*     */     } else {
/*  75 */       htmlTree.addStyle(HtmlStyle.blockList);
/*  76 */     }  return (Content)htmlTree;
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
/*     */   public Content getSerializableMethods(String paramString, Content paramContent) {
/*  88 */     StringContent stringContent = new StringContent(paramString);
/*  89 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.SERIALIZED_MEMBER_HEADING, (Content)stringContent);
/*     */     
/*  91 */     HtmlTree htmlTree2 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/*  92 */     htmlTree2.addContent(paramContent);
/*  93 */     return (Content)htmlTree2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNoCustomizationMsg(String paramString) {
/* 103 */     return (Content)new StringContent(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberHeader(MethodDoc paramMethodDoc, Content paramContent) {
/* 114 */     paramContent.addContent(getHead((MemberDoc)paramMethodDoc));
/* 115 */     paramContent.addContent(getSignature(paramMethodDoc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecatedMemberInfo(MethodDoc paramMethodDoc, Content paramContent) {
/* 125 */     addDeprecatedInfo((ProgramElementDoc)paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberDescription(MethodDoc paramMethodDoc, Content paramContent) {
/* 135 */     addComment((ProgramElementDoc)paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberTags(MethodDoc paramMethodDoc, Content paramContent) {
/* 145 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 146 */     TagletManager tagletManager = this.configuration.tagletManager;
/*     */     
/* 148 */     TagletWriter.genTagOuput(tagletManager, (Doc)paramMethodDoc, tagletManager
/* 149 */         .getSerializedFormTaglets(), this.writer
/* 150 */         .getTagletWriterInstance(false), (Content)contentBuilder);
/* 151 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DL);
/* 152 */     htmlTree.addContent((Content)contentBuilder);
/* 153 */     paramContent.addContent((Content)htmlTree);
/* 154 */     MethodDoc methodDoc = paramMethodDoc;
/* 155 */     if (methodDoc.name().compareTo("writeExternal") == 0 && (methodDoc
/* 156 */       .tags("serialData")).length == 0)
/* 157 */       serialWarning(paramMethodDoc.position(), "doclet.MissingSerialDataTag", methodDoc
/* 158 */           .containingClass().qualifiedName(), methodDoc.name()); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\HtmlSerialMethodWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */