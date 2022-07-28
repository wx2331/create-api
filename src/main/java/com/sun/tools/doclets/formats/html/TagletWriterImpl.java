/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.SerializedFormBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TagletWriterImpl
/*     */   extends TagletWriter
/*     */ {
/*     */   private final HtmlDocletWriter htmlWriter;
/*     */   private final ConfigurationImpl configuration;
/*     */   
/*     */   public TagletWriterImpl(HtmlDocletWriter paramHtmlDocletWriter, boolean paramBoolean) {
/*  58 */     super(paramBoolean);
/*  59 */     this.htmlWriter = paramHtmlDocletWriter;
/*  60 */     this.configuration = paramHtmlDocletWriter.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getOutputInstance() {
/*  67 */     return (Content)new ContentBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content codeTagOutput(Tag paramTag) {
/*  74 */     return (Content)HtmlTree.CODE((Content)new StringContent(Util.normalizeNewlines(paramTag.text())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getDocRootOutput() {
/*     */     String str;
/*  83 */     if (this.htmlWriter.pathToRoot.isEmpty()) {
/*  84 */       str = ".";
/*     */     } else {
/*  86 */       str = this.htmlWriter.pathToRoot.getPath();
/*  87 */     }  return (Content)new StringContent(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content deprecatedTagOutput(Doc paramDoc) {
/*  94 */     ContentBuilder contentBuilder = new ContentBuilder();
/*  95 */     Tag[] arrayOfTag = paramDoc.tags("deprecated");
/*  96 */     if (paramDoc instanceof ClassDoc) {
/*  97 */       if (Util.isDeprecated(paramDoc)) {
/*  98 */         contentBuilder.addContent((Content)HtmlTree.SPAN(HtmlStyle.deprecatedLabel, (Content)new StringContent(this.configuration
/*  99 */                 .getText("doclet.Deprecated"))));
/* 100 */         contentBuilder.addContent(RawHtml.nbsp);
/* 101 */         if (arrayOfTag.length > 0) {
/* 102 */           Tag[] arrayOfTag1 = arrayOfTag[0].inlineTags();
/* 103 */           if (arrayOfTag1.length > 0) {
/* 104 */             contentBuilder.addContent(commentTagsToOutput(null, paramDoc, arrayOfTag[0]
/* 105 */                   .inlineTags(), false));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 111 */       MemberDoc memberDoc = (MemberDoc)paramDoc;
/* 112 */       if (Util.isDeprecated(paramDoc)) {
/* 113 */         contentBuilder.addContent((Content)HtmlTree.SPAN(HtmlStyle.deprecatedLabel, (Content)new StringContent(this.configuration
/* 114 */                 .getText("doclet.Deprecated"))));
/* 115 */         contentBuilder.addContent(RawHtml.nbsp);
/* 116 */         if (arrayOfTag.length > 0) {
/* 117 */           Content content = commentTagsToOutput(null, paramDoc, arrayOfTag[0]
/* 118 */               .inlineTags(), false);
/* 119 */           if (!content.isEmpty()) {
/* 120 */             contentBuilder.addContent((Content)HtmlTree.SPAN(HtmlStyle.deprecationComment, content));
/*     */           }
/*     */         } 
/* 123 */       } else if (Util.isDeprecated((Doc)memberDoc.containingClass())) {
/* 124 */         contentBuilder.addContent((Content)HtmlTree.SPAN(HtmlStyle.deprecatedLabel, (Content)new StringContent(this.configuration
/* 125 */                 .getText("doclet.Deprecated"))));
/* 126 */         contentBuilder.addContent(RawHtml.nbsp);
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     return (Content)contentBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content literalTagOutput(Tag paramTag) {
/* 137 */     return (Content)new StringContent(Util.normalizeNewlines(paramTag.text()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageRetriever getMsgRetriever() {
/* 145 */     return this.configuration.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getParamHeader(String paramString) {
/* 152 */     return (Content)HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.paramLabel, (Content)new StringContent(paramString)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content paramTagOutput(ParamTag paramParamTag, String paramString) {
/* 161 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 162 */     contentBuilder.addContent((Content)HtmlTree.CODE((Content)new RawHtml(paramString)));
/* 163 */     contentBuilder.addContent(" - ");
/* 164 */     contentBuilder.addContent(this.htmlWriter.commentTagsToContent((Tag)paramParamTag, (Doc)null, paramParamTag.inlineTags(), false));
/* 165 */     return (Content)HtmlTree.DD((Content)contentBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content propertyTagOutput(Tag paramTag, String paramString) {
/* 173 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 174 */     contentBuilder.addContent((Content)new RawHtml(paramString));
/* 175 */     contentBuilder.addContent(" ");
/* 176 */     contentBuilder.addContent((Content)HtmlTree.CODE((Content)new RawHtml(paramTag.text())));
/* 177 */     contentBuilder.addContent(".");
/* 178 */     return (Content)HtmlTree.P((Content)contentBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content returnTagOutput(Tag paramTag) {
/* 186 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 187 */     contentBuilder.addContent((Content)HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.returnLabel, (Content)new StringContent(this.configuration
/* 188 */               .getText("doclet.Returns")))));
/* 189 */     contentBuilder.addContent((Content)HtmlTree.DD(this.htmlWriter.commentTagsToContent(paramTag, (Doc)null, paramTag
/* 190 */             .inlineTags(), false)));
/* 191 */     return (Content)contentBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content seeTagOutput(Doc paramDoc, SeeTag[] paramArrayOfSeeTag) {
/* 198 */     ContentBuilder contentBuilder1 = new ContentBuilder();
/* 199 */     if (paramArrayOfSeeTag.length > 0) {
/* 200 */       for (byte b = 0; b < paramArrayOfSeeTag.length; b++) {
/* 201 */         appendSeparatorIfNotEmpty(contentBuilder1);
/* 202 */         contentBuilder1.addContent(this.htmlWriter.seeTagToContent(paramArrayOfSeeTag[b]));
/*     */       } 
/*     */     }
/* 205 */     if (paramDoc.isField() && ((FieldDoc)paramDoc).constantValue() != null && this.htmlWriter instanceof ClassWriterImpl) {
/*     */ 
/*     */       
/* 208 */       appendSeparatorIfNotEmpty(contentBuilder1);
/*     */       
/* 210 */       DocPath docPath = this.htmlWriter.pathToRoot.resolve(DocPaths.CONSTANT_VALUES);
/*     */       
/* 212 */       String str = ((ClassWriterImpl)this.htmlWriter).getClassDoc().qualifiedName() + "." + ((FieldDoc)paramDoc).name();
/* 213 */       DocLink docLink = docPath.fragment(str);
/* 214 */       contentBuilder1.addContent(this.htmlWriter.getHyperLink(docLink, (Content)new StringContent(this.configuration
/* 215 */               .getText("doclet.Constants_Summary"))));
/*     */     } 
/* 217 */     if (paramDoc.isClass() && ((ClassDoc)paramDoc).isSerializable())
/*     */     {
/* 219 */       if (SerializedFormBuilder.serialInclude(paramDoc) && 
/* 220 */         SerializedFormBuilder.serialInclude((Doc)((ClassDoc)paramDoc).containingPackage())) {
/* 221 */         appendSeparatorIfNotEmpty(contentBuilder1);
/* 222 */         DocPath docPath = this.htmlWriter.pathToRoot.resolve(DocPaths.SERIALIZED_FORM);
/* 223 */         DocLink docLink = docPath.fragment(((ClassDoc)paramDoc).qualifiedName());
/* 224 */         contentBuilder1.addContent(this.htmlWriter.getHyperLink(docLink, (Content)new StringContent(this.configuration
/* 225 */                 .getText("doclet.Serialized_Form"))));
/*     */       } 
/*     */     }
/* 228 */     if (contentBuilder1.isEmpty()) {
/* 229 */       return (Content)contentBuilder1;
/*     */     }
/* 231 */     ContentBuilder contentBuilder2 = new ContentBuilder();
/* 232 */     contentBuilder2.addContent((Content)HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.seeLabel, (Content)new StringContent(this.configuration
/* 233 */               .getText("doclet.See_Also")))));
/* 234 */     contentBuilder2.addContent((Content)HtmlTree.DD((Content)contentBuilder1));
/* 235 */     return (Content)contentBuilder2;
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendSeparatorIfNotEmpty(ContentBuilder paramContentBuilder) {
/* 240 */     if (!paramContentBuilder.isEmpty()) {
/* 241 */       paramContentBuilder.addContent(", ");
/* 242 */       paramContentBuilder.addContent(DocletConstants.NL);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content simpleTagOutput(Tag[] paramArrayOfTag, String paramString) {
/* 250 */     ContentBuilder contentBuilder1 = new ContentBuilder();
/* 251 */     contentBuilder1.addContent((Content)HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.simpleTagLabel, (Content)new RawHtml(paramString))));
/* 252 */     ContentBuilder contentBuilder2 = new ContentBuilder();
/* 253 */     for (byte b = 0; b < paramArrayOfTag.length; b++) {
/* 254 */       if (b > 0) {
/* 255 */         contentBuilder2.addContent(", ");
/*     */       }
/* 257 */       contentBuilder2.addContent(this.htmlWriter.commentTagsToContent(paramArrayOfTag[b], (Doc)null, paramArrayOfTag[b]
/* 258 */             .inlineTags(), false));
/*     */     } 
/* 260 */     contentBuilder1.addContent((Content)HtmlTree.DD((Content)contentBuilder2));
/* 261 */     return (Content)contentBuilder1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content simpleTagOutput(Tag paramTag, String paramString) {
/* 268 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 269 */     contentBuilder.addContent((Content)HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.simpleTagLabel, (Content)new RawHtml(paramString))));
/* 270 */     Content content = this.htmlWriter.commentTagsToContent(paramTag, (Doc)null, paramTag
/* 271 */         .inlineTags(), false);
/* 272 */     contentBuilder.addContent((Content)HtmlTree.DD(content));
/* 273 */     return (Content)contentBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getThrowsHeader() {
/* 280 */     return (Content)HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.throwsLabel, (Content)new StringContent(this.configuration
/* 281 */             .getText("doclet.Throws"))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content throwsTagOutput(ThrowsTag paramThrowsTag) {
/* 289 */     ContentBuilder contentBuilder = new ContentBuilder();
/*     */ 
/*     */     
/* 292 */     Content content1 = (Content)((paramThrowsTag.exceptionType() == null) ? new RawHtml(paramThrowsTag.exceptionName()) : this.htmlWriter.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramThrowsTag
/* 293 */           .exceptionType())));
/* 294 */     contentBuilder.addContent((Content)HtmlTree.CODE(content1));
/* 295 */     Content content2 = this.htmlWriter.commentTagsToContent((Tag)paramThrowsTag, (Doc)null, paramThrowsTag
/* 296 */         .inlineTags(), false);
/* 297 */     if (content2 != null && !content2.isEmpty()) {
/* 298 */       contentBuilder.addContent(" - ");
/* 299 */       contentBuilder.addContent(content2);
/*     */     } 
/* 301 */     return (Content)HtmlTree.DD((Content)contentBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content throwsTagOutput(Type paramType) {
/* 309 */     return (Content)HtmlTree.DD((Content)HtmlTree.CODE(this.htmlWriter.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramType))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content valueTagOutput(FieldDoc paramFieldDoc, String paramString, boolean paramBoolean) {
/* 319 */     return paramBoolean ? this.htmlWriter
/* 320 */       .getDocLink(LinkInfoImpl.Kind.VALUE_TAG, (MemberDoc)paramFieldDoc, paramString, false) : (Content)new RawHtml(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content commentTagsToOutput(Tag paramTag, Tag[] paramArrayOfTag) {
/* 328 */     return commentTagsToOutput(paramTag, null, paramArrayOfTag, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content commentTagsToOutput(Doc paramDoc, Tag[] paramArrayOfTag) {
/* 335 */     return commentTagsToOutput(null, paramDoc, paramArrayOfTag, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content commentTagsToOutput(Tag paramTag, Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean) {
/* 343 */     return this.htmlWriter.commentTagsToContent(paramTag, paramDoc, paramArrayOfTag, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration configuration() {
/* 351 */     return this.configuration;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\TagletWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */