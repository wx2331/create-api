/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstructorWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstructorWriterImpl
/*     */   extends AbstractExecutableMemberWriter
/*     */   implements ConstructorWriter, MemberSummaryWriter
/*     */ {
/*     */   private boolean foundNonPubConstructor = false;
/*     */   
/*     */   public ConstructorWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  61 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*  62 */     VisibleMemberMap visibleMemberMap = new VisibleMemberMap(paramClassDoc, 3, this.configuration);
/*     */     
/*  64 */     ArrayList<ProgramElementDoc> arrayList = new ArrayList(visibleMemberMap.getMembersFor(paramClassDoc));
/*  65 */     for (byte b = 0; b < arrayList.size(); b++) {
/*  66 */       if (((ProgramElementDoc)arrayList.get(b)).isProtected() || ((ProgramElementDoc)arrayList
/*  67 */         .get(b)).isPrivate()) {
/*  68 */         setFoundNonPubConstructor(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  79 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  87 */     paramContent.addContent(HtmlConstants.START_OF_CONSTRUCTOR_SUMMARY);
/*  88 */     Content content = this.writer.getMemberTreeHeader();
/*  89 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  90 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getConstructorDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  98 */     paramContent.addContent(HtmlConstants.START_OF_CONSTRUCTOR_DETAILS);
/*  99 */     Content content = this.writer.getMemberTreeHeader();
/* 100 */     content.addContent(this.writer.getMarkerAnchor(SectionName.CONSTRUCTOR_DETAIL));
/*     */     
/* 102 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.constructorDetailsLabel);
/*     */     
/* 104 */     content.addContent((Content)htmlTree);
/* 105 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getConstructorDocTreeHeader(ConstructorDoc paramConstructorDoc, Content paramContent) {
/*     */     String str;
/* 114 */     if ((str = getErasureAnchor((ExecutableMemberDoc)paramConstructorDoc)) != null) {
/* 115 */       paramContent.addContent(this.writer.getMarkerAnchor(str));
/*     */     }
/* 117 */     paramContent.addContent(this.writer
/* 118 */         .getMarkerAnchor(this.writer.getAnchor((ExecutableMemberDoc)paramConstructorDoc)));
/* 119 */     Content content = this.writer.getMemberTreeHeader();
/* 120 */     HtmlTree htmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/* 121 */     htmlTree.addContent(paramConstructorDoc.name());
/* 122 */     content.addContent((Content)htmlTree);
/* 123 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSignature(ConstructorDoc paramConstructorDoc) {
/* 130 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 131 */     this.writer.addAnnotationInfo((ProgramElementDoc)paramConstructorDoc, (Content)htmlTree);
/* 132 */     addModifiers((MemberDoc)paramConstructorDoc, (Content)htmlTree);
/* 133 */     if (this.configuration.linksource) {
/* 134 */       StringContent stringContent = new StringContent(paramConstructorDoc.name());
/* 135 */       this.writer.addSrcLink((ProgramElementDoc)paramConstructorDoc, (Content)stringContent, (Content)htmlTree);
/*     */     } else {
/* 137 */       addName(paramConstructorDoc.name(), (Content)htmlTree);
/*     */     } 
/* 139 */     int i = htmlTree.charCount();
/* 140 */     addParameters((ExecutableMemberDoc)paramConstructorDoc, (Content)htmlTree, i);
/* 141 */     addExceptions((ExecutableMemberDoc)paramConstructorDoc, (Content)htmlTree, i);
/* 142 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSummaryColumnStyle(HtmlTree paramHtmlTree) {
/* 150 */     if (this.foundNonPubConstructor) {
/* 151 */       paramHtmlTree.addStyle(HtmlStyle.colLast);
/*     */     } else {
/* 153 */       paramHtmlTree.addStyle(HtmlStyle.colOne);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecated(ConstructorDoc paramConstructorDoc, Content paramContent) {
/* 160 */     addDeprecatedInfo((ProgramElementDoc)paramConstructorDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComments(ConstructorDoc paramConstructorDoc, Content paramContent) {
/* 167 */     addComment((ProgramElementDoc)paramConstructorDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTags(ConstructorDoc paramConstructorDoc, Content paramContent) {
/* 174 */     this.writer.addTagsInfo((Doc)paramConstructorDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getConstructorDetails(Content paramContent) {
/* 181 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getConstructorDoc(Content paramContent, boolean paramBoolean) {
/* 189 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 196 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFoundNonPubConstructor(boolean paramBoolean) {
/* 205 */     this.foundNonPubConstructor = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/* 212 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 213 */         .getResource("doclet.Constructor_Summary"));
/* 214 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/* 221 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 222 */         .getText("doclet.Constructor_Summary"), this.configuration
/* 223 */         .getText("doclet.constructors"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 230 */     return this.configuration.getResource("doclet.Constructors");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/*     */     String[] arrayOfString;
/* 238 */     if (this.foundNonPubConstructor) {
/*     */ 
/*     */       
/* 241 */       arrayOfString = new String[] { this.configuration.getText("doclet.Modifier"), this.configuration.getText("doclet.0_and_1", this.configuration
/* 242 */             .getText("doclet.Constructor"), this.configuration
/* 243 */             .getText("doclet.Description")) };
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 248 */       arrayOfString = new String[] { this.configuration.getText("doclet.0_and_1", this.configuration
/* 249 */             .getText("doclet.Constructor"), this.configuration
/* 250 */             .getText("doclet.Description")) };
/*     */     } 
/*     */     
/* 253 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 260 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.CONSTRUCTOR_SUMMARY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMemberKind() {
/* 277 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 284 */     if (paramBoolean) {
/* 285 */       return this.writer.getHyperLink(SectionName.CONSTRUCTOR_SUMMARY, this.writer
/* 286 */           .getResource("doclet.navConstructor"));
/*     */     }
/* 288 */     return this.writer.getResource("doclet.navConstructor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent) {
/* 296 */     if (paramBoolean) {
/* 297 */       paramContent.addContent(this.writer.getHyperLink(SectionName.CONSTRUCTOR_DETAIL, this.writer
/*     */             
/* 299 */             .getResource("doclet.navConstructor")));
/*     */     } else {
/* 301 */       paramContent.addContent(this.writer.getResource("doclet.navConstructor"));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 309 */     if (this.foundNonPubConstructor) {
/* 310 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.CODE);
/* 311 */       if (paramProgramElementDoc.isProtected()) {
/* 312 */         htmlTree.addContent("protected ");
/* 313 */       } else if (paramProgramElementDoc.isPrivate()) {
/* 314 */         htmlTree.addContent("private ");
/* 315 */       } else if (paramProgramElementDoc.isPublic()) {
/* 316 */         htmlTree.addContent(this.writer.getSpace());
/*     */       } else {
/* 318 */         htmlTree.addContent(this.configuration
/* 319 */             .getText("doclet.Package_private"));
/*     */       } 
/* 321 */       paramContent.addContent((Content)htmlTree);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ConstructorWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */