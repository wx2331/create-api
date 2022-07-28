/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DeprecatedAPIListBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeprecatedListWriter
/*     */   extends SubWriterHolderWriter
/*     */ {
/*  49 */   private static final String[] ANCHORS = new String[] { "package", "interface", "class", "enum", "exception", "error", "annotation.type", "field", "method", "constructor", "enum.constant", "annotation.type.member" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final String[] HEADING_KEYS = new String[] { "doclet.Deprecated_Packages", "doclet.Deprecated_Interfaces", "doclet.Deprecated_Classes", "doclet.Deprecated_Enums", "doclet.Deprecated_Exceptions", "doclet.Deprecated_Errors", "doclet.Deprecated_Annotation_Types", "doclet.Deprecated_Fields", "doclet.Deprecated_Methods", "doclet.Deprecated_Constructors", "doclet.Deprecated_Enum_Constants", "doclet.Deprecated_Annotation_Type_Members" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static final String[] SUMMARY_KEYS = new String[] { "doclet.deprecated_packages", "doclet.deprecated_interfaces", "doclet.deprecated_classes", "doclet.deprecated_enums", "doclet.deprecated_exceptions", "doclet.deprecated_errors", "doclet.deprecated_annotation_types", "doclet.deprecated_fields", "doclet.deprecated_methods", "doclet.deprecated_constructors", "doclet.deprecated_enum_constants", "doclet.deprecated_annotation_type_members" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final String[] HEADER_KEYS = new String[] { "doclet.Package", "doclet.Interface", "doclet.Class", "doclet.Enum", "doclet.Exceptions", "doclet.Errors", "doclet.AnnotationType", "doclet.Field", "doclet.Method", "doclet.Constructor", "doclet.Enum_Constant", "doclet.Annotation_Type_Member" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractMemberWriter[] writers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConfigurationImpl configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeprecatedListWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  99 */     super(paramConfigurationImpl, paramDocPath);
/* 100 */     this.configuration = paramConfigurationImpl;
/* 101 */     NestedClassWriterImpl nestedClassWriterImpl = new NestedClassWriterImpl(this);
/* 102 */     this.writers = new AbstractMemberWriter[] { nestedClassWriterImpl, nestedClassWriterImpl, nestedClassWriterImpl, nestedClassWriterImpl, nestedClassWriterImpl, nestedClassWriterImpl, new FieldWriterImpl(this), new MethodWriterImpl(this), new ConstructorWriterImpl(this), new EnumConstantWriterImpl(this), new AnnotationTypeOptionalMemberWriterImpl(this, null) };
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl) {
/* 119 */     DocPath docPath = DocPaths.DEPRECATED_LIST;
/*     */     try {
/* 121 */       DeprecatedListWriter deprecatedListWriter = new DeprecatedListWriter(paramConfigurationImpl, docPath);
/*     */       
/* 123 */       deprecatedListWriter.generateDeprecatedListFile(new DeprecatedAPIListBuilder(paramConfigurationImpl));
/*     */       
/* 125 */       deprecatedListWriter.close();
/* 126 */     } catch (IOException iOException) {
/* 127 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/* 129 */             .toString(), docPath });
/* 130 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateDeprecatedListFile(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder) throws IOException {
/* 141 */     Content content = getHeader();
/* 142 */     content.addContent(getContentsList(paramDeprecatedAPIListBuilder));
/*     */     
/* 144 */     String[] arrayOfString = new String[1];
/* 145 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 146 */     htmlTree.addStyle(HtmlStyle.contentContainer);
/* 147 */     for (byte b = 0; b < 12; b++) {
/* 148 */       if (paramDeprecatedAPIListBuilder.hasDocumentation(b)) {
/* 149 */         addAnchor(paramDeprecatedAPIListBuilder, b, (Content)htmlTree);
/*     */         
/* 151 */         String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 152 */             .getText(HEADING_KEYS[b]), this.configuration
/* 153 */             .getText(SUMMARY_KEYS[b]));
/* 154 */         arrayOfString[0] = this.configuration.getText("doclet.0_and_1", this.configuration
/* 155 */             .getText(HEADER_KEYS[b]), this.configuration
/* 156 */             .getText("doclet.Description"));
/*     */ 
/*     */         
/* 159 */         if (b == 0) {
/* 160 */           addPackageDeprecatedAPI(paramDeprecatedAPIListBuilder.getList(b), HEADING_KEYS[b], str, arrayOfString, (Content)htmlTree);
/*     */         } else {
/*     */           
/* 163 */           this.writers[b - 1].addDeprecatedAPI(paramDeprecatedAPIListBuilder.getList(b), HEADING_KEYS[b], str, arrayOfString, (Content)htmlTree);
/*     */         } 
/*     */       } 
/*     */     } 
/* 167 */     content.addContent((Content)htmlTree);
/* 168 */     addNavLinks(false, content);
/* 169 */     addBottom(content);
/* 170 */     printHtmlDocument((String[])null, true, content);
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
/*     */   private void addIndexLink(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder, int paramInt, Content paramContent) {
/* 182 */     if (paramDeprecatedAPIListBuilder.hasDocumentation(paramInt)) {
/* 183 */       HtmlTree htmlTree = HtmlTree.LI(getHyperLink(ANCHORS[paramInt], 
/* 184 */             getResource(HEADING_KEYS[paramInt])));
/* 185 */       paramContent.addContent((Content)htmlTree);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getContentsList(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder) {
/* 196 */     Content content1 = getResource("doclet.Deprecated_API");
/* 197 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, content1);
/*     */     
/* 199 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 200 */     Content content2 = getResource("doclet.Contents");
/* 201 */     htmlTree2.addContent((Content)HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, content2));
/*     */     
/* 203 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.UL);
/* 204 */     for (byte b = 0; b < 12; b++) {
/* 205 */       addIndexLink(paramDeprecatedAPIListBuilder, b, (Content)htmlTree3);
/*     */     }
/* 207 */     htmlTree2.addContent((Content)htmlTree3);
/* 208 */     return (Content)htmlTree2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAnchor(DeprecatedAPIListBuilder paramDeprecatedAPIListBuilder, int paramInt, Content paramContent) {
/* 219 */     if (paramDeprecatedAPIListBuilder.hasDocumentation(paramInt)) {
/* 220 */       paramContent.addContent(getMarkerAnchor(ANCHORS[paramInt]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getHeader() {
/* 230 */     String str = this.configuration.getText("doclet.Window_Deprecated_List");
/* 231 */     HtmlTree htmlTree = getBody(true, getWindowTitle(str));
/* 232 */     addTop((Content)htmlTree);
/* 233 */     addNavLinks(true, (Content)htmlTree);
/* 234 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkDeprecated() {
/* 243 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.deprecatedLabel);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\DeprecatedListWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */