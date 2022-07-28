/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HtmlSerialFieldWriter
/*     */   extends FieldWriterImpl
/*     */   implements SerializedFormWriter.SerialFieldWriter
/*     */ {
/*  50 */   ProgramElementDoc[] members = null;
/*     */ 
/*     */   
/*     */   public HtmlSerialFieldWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  54 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */   
/*     */   public List<FieldDoc> members(ClassDoc paramClassDoc) {
/*  58 */     return Arrays.asList(paramClassDoc.serializableFields());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSerializableFieldsHeader() {
/*  67 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/*  68 */     htmlTree.addStyle(HtmlStyle.blockList);
/*  69 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getFieldsContentHeader(boolean paramBoolean) {
/*  79 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/*  80 */     if (paramBoolean) {
/*  81 */       htmlTree.addStyle(HtmlStyle.blockListLast);
/*     */     } else {
/*  83 */       htmlTree.addStyle(HtmlStyle.blockList);
/*  84 */     }  return (Content)htmlTree;
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
/*     */   public Content getSerializableFields(String paramString, Content paramContent) {
/*  96 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/*  97 */     htmlTree.addStyle(HtmlStyle.blockList);
/*  98 */     if (paramContent.isValid()) {
/*  99 */       StringContent stringContent = new StringContent(paramString);
/* 100 */       HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.SERIALIZED_MEMBER_HEADING, (Content)stringContent);
/*     */       
/* 102 */       htmlTree.addContent((Content)htmlTree1);
/* 103 */       htmlTree.addContent(paramContent);
/*     */     } 
/* 105 */     return (Content)htmlTree;
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
/*     */   public void addMemberHeader(ClassDoc paramClassDoc, String paramString1, String paramString2, String paramString3, Content paramContent) {
/* 119 */     RawHtml rawHtml = new RawHtml(paramString3);
/* 120 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.MEMBER_HEADING, (Content)rawHtml);
/* 121 */     paramContent.addContent((Content)htmlTree1);
/* 122 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.PRE);
/* 123 */     if (paramClassDoc == null) {
/* 124 */       htmlTree2.addContent(paramString1);
/*     */     } else {
/* 126 */       Content content = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SERIAL_MEMBER, paramClassDoc));
/*     */       
/* 128 */       htmlTree2.addContent(content);
/*     */     } 
/* 130 */     htmlTree2.addContent(paramString2 + " ");
/* 131 */     htmlTree2.addContent(paramString3);
/* 132 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberDeprecatedInfo(FieldDoc paramFieldDoc, Content paramContent) {
/* 142 */     addDeprecatedInfo((ProgramElementDoc)paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberDescription(FieldDoc paramFieldDoc, Content paramContent) {
/* 152 */     if ((paramFieldDoc.inlineTags()).length > 0) {
/* 153 */       this.writer.addInlineComment((Doc)paramFieldDoc, paramContent);
/*     */     }
/* 155 */     Tag[] arrayOfTag = paramFieldDoc.tags("serial");
/* 156 */     if (arrayOfTag.length > 0) {
/* 157 */       this.writer.addInlineComment((Doc)paramFieldDoc, arrayOfTag[0], paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberDescription(SerialFieldTag paramSerialFieldTag, Content paramContent) {
/* 168 */     String str = paramSerialFieldTag.description().trim();
/* 169 */     if (!str.isEmpty()) {
/* 170 */       RawHtml rawHtml = new RawHtml(str);
/* 171 */       HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.block, (Content)rawHtml);
/* 172 */       paramContent.addContent((Content)htmlTree);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberTags(FieldDoc paramFieldDoc, Content paramContent) {
/* 183 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 184 */     TagletWriter.genTagOuput(this.configuration.tagletManager, (Doc)paramFieldDoc, this.configuration.tagletManager
/* 185 */         .getCustomTaglets((Doc)paramFieldDoc), this.writer
/* 186 */         .getTagletWriterInstance(false), (Content)contentBuilder);
/* 187 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DL);
/* 188 */     htmlTree.addContent((Content)contentBuilder);
/* 189 */     paramContent.addContent((Content)htmlTree);
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
/*     */   public boolean shouldPrintOverview(FieldDoc paramFieldDoc) {
/* 201 */     if (!this.configuration.nocomment && (
/* 202 */       !paramFieldDoc.commentText().isEmpty() || this.writer
/* 203 */       .hasSerializationOverviewTags(paramFieldDoc))) {
/* 204 */       return true;
/*     */     }
/* 206 */     if ((paramFieldDoc.tags("deprecated")).length > 0)
/* 207 */       return true; 
/* 208 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\HtmlSerialFieldWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */