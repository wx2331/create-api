/*      */ package com.sun.tools.doclets.formats.html;
/*      */ 
/*      */ import com.sun.javadoc.AnnotationDesc;
/*      */ import com.sun.javadoc.AnnotationTypeDoc;
/*      */ import com.sun.javadoc.AnnotationValue;
/*      */ import com.sun.javadoc.ClassDoc;
/*      */ import com.sun.javadoc.Doc;
/*      */ import com.sun.javadoc.ExecutableMemberDoc;
/*      */ import com.sun.javadoc.FieldDoc;
/*      */ import com.sun.javadoc.MemberDoc;
/*      */ import com.sun.javadoc.MethodDoc;
/*      */ import com.sun.javadoc.PackageDoc;
/*      */ import com.sun.javadoc.Parameter;
/*      */ import com.sun.javadoc.ProgramElementDoc;
/*      */ import com.sun.javadoc.SeeTag;
/*      */ import com.sun.javadoc.Tag;
/*      */ import com.sun.javadoc.Type;
/*      */ import com.sun.tools.doclets.formats.html.markup.Comment;
/*      */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*      */ import com.sun.tools.doclets.formats.html.markup.DocType;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlAttr;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlDocWriter;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlDocument;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*      */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*      */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*      */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*      */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*      */ import com.sun.tools.doclets.internal.toolkit.Content;
/*      */ import com.sun.tools.doclets.internal.toolkit.taglets.DocRootTaglet;
/*      */ import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.ImplementedMethods;
/*      */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*      */ import com.sun.tools.javac.util.StringUtils;
/*      */ import java.io.IOException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HtmlDocletWriter
/*      */   extends HtmlDocWriter
/*      */ {
/*      */   public final DocPath pathToRoot;
/*      */   public final DocPath path;
/*      */   public final DocPath filename;
/*      */   public final ConfigurationImpl configuration;
/*      */   protected boolean printedAnnotationHeading = false;
/*      */   protected boolean printedAnnotationFieldHeading = false;
/*      */   private boolean isAnnotationDocumented = false;
/*      */   private boolean isContainerDocumented = false;
/*      */   
/*      */   public HtmlDocletWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  112 */     super(paramConfigurationImpl, paramDocPath);
/*  113 */     this.configuration = paramConfigurationImpl;
/*  114 */     this.path = paramDocPath;
/*  115 */     this.pathToRoot = paramDocPath.parent().invert();
/*  116 */     this.filename = paramDocPath.basename();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replaceDocRootDir(String paramString) {
/*  140 */     int i = paramString.indexOf("{@");
/*  141 */     if (i < 0) {
/*  142 */       return paramString;
/*      */     }
/*  144 */     Matcher matcher = docrootPattern.matcher(paramString);
/*  145 */     if (!matcher.find()) {
/*  146 */       return paramString;
/*      */     }
/*  148 */     StringBuilder stringBuilder = new StringBuilder();
/*  149 */     int j = 0;
/*      */     while (true) {
/*  151 */       int k = matcher.start();
/*      */       
/*  153 */       stringBuilder.append(paramString.substring(j, k));
/*  154 */       j = matcher.end();
/*  155 */       if (this.configuration.docrootparent.length() > 0 && paramString.startsWith("/..", j)) {
/*      */         
/*  157 */         stringBuilder.append(this.configuration.docrootparent);
/*  158 */         j += 3;
/*      */       } else {
/*      */         
/*  161 */         stringBuilder.append(this.pathToRoot.isEmpty() ? "." : this.pathToRoot.getPath());
/*      */       } 
/*      */       
/*  164 */       if (j < paramString.length() && paramString.charAt(j) != '/') {
/*  165 */         stringBuilder.append('/');
/*      */       }
/*  167 */       if (!matcher.find()) {
/*  168 */         stringBuilder.append(paramString.substring(j));
/*  169 */         return stringBuilder.toString();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*  174 */   private static final Pattern docrootPattern = Pattern.compile(Pattern.quote("{@docroot}"), 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getAllClassesLinkScript(String paramString) {
/*  183 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.SCRIPT);
/*  184 */     htmlTree.addAttr(HtmlAttr.TYPE, "text/javascript");
/*  185 */     String str = "<!--" + DocletConstants.NL + "  allClassesLink = document.getElementById(\"" + paramString + "\");" + DocletConstants.NL + "  if(window==top) {" + DocletConstants.NL + "    allClassesLink.style.display = \"block\";" + DocletConstants.NL + "  }" + DocletConstants.NL + "  else {" + DocletConstants.NL + "    allClassesLink.style.display = \"none\";" + DocletConstants.NL + "  }" + DocletConstants.NL + "  //-->" + DocletConstants.NL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  194 */     RawHtml rawHtml = new RawHtml(str);
/*  195 */     htmlTree.addContent((Content)rawHtml);
/*  196 */     return (Content)HtmlTree.DIV((Content)htmlTree);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addMethodInfo(MethodDoc paramMethodDoc, Content paramContent) {
/*  207 */     ClassDoc[] arrayOfClassDoc = paramMethodDoc.containingClass().interfaces();
/*  208 */     MethodDoc methodDoc = paramMethodDoc.overriddenMethod();
/*      */ 
/*      */ 
/*      */     
/*  212 */     if ((arrayOfClassDoc.length > 0 && ((new ImplementedMethods(paramMethodDoc, this.configuration))
/*  213 */       .build()).length > 0) || methodDoc != null) {
/*      */       
/*  215 */       MethodWriterImpl.addImplementsInfo(this, paramMethodDoc, paramContent);
/*  216 */       if (methodDoc != null) {
/*  217 */         MethodWriterImpl.addOverridden(this, paramMethodDoc
/*  218 */             .overriddenType(), methodDoc, paramContent);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addTagsInfo(Doc paramDoc, Content paramContent) {
/*  230 */     if (this.configuration.nocomment) {
/*      */       return;
/*      */     }
/*  233 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DL);
/*  234 */     if (paramDoc instanceof MethodDoc) {
/*  235 */       addMethodInfo((MethodDoc)paramDoc, (Content)htmlTree);
/*      */     }
/*  237 */     ContentBuilder contentBuilder = new ContentBuilder();
/*  238 */     TagletWriter.genTagOuput(this.configuration.tagletManager, paramDoc, this.configuration.tagletManager
/*  239 */         .getCustomTaglets(paramDoc), 
/*  240 */         getTagletWriterInstance(false), (Content)contentBuilder);
/*  241 */     htmlTree.addContent((Content)contentBuilder);
/*  242 */     paramContent.addContent((Content)htmlTree);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasSerializationOverviewTags(FieldDoc paramFieldDoc) {
/*  253 */     ContentBuilder contentBuilder = new ContentBuilder();
/*  254 */     TagletWriter.genTagOuput(this.configuration.tagletManager, (Doc)paramFieldDoc, this.configuration.tagletManager
/*  255 */         .getCustomTaglets((Doc)paramFieldDoc), 
/*  256 */         getTagletWriterInstance(false), (Content)contentBuilder);
/*  257 */     return !contentBuilder.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TagletWriter getTagletWriterInstance(boolean paramBoolean) {
/*  266 */     return new TagletWriterImpl(this, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getTargetPackageLink(PackageDoc paramPackageDoc, String paramString, Content paramContent) {
/*  279 */     return getHyperLink(pathString(paramPackageDoc, DocPaths.PACKAGE_SUMMARY), paramContent, "", paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getTargetProfilePackageLink(PackageDoc paramPackageDoc, String paramString1, Content paramContent, String paramString2) {
/*  293 */     return getHyperLink(pathString(paramPackageDoc, DocPaths.profilePackageSummary(paramString2)), paramContent, "", paramString1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getTargetProfileLink(String paramString1, Content paramContent, String paramString2) {
/*  307 */     return getHyperLink(this.pathToRoot.resolve(
/*  308 */           DocPaths.profileSummary(paramString2)), paramContent, "", paramString1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTypeNameForProfile(ClassDoc paramClassDoc) {
/*  319 */     StringBuilder stringBuilder = new StringBuilder(paramClassDoc.containingPackage().name().replace(".", "/"));
/*  320 */     stringBuilder.append("/")
/*  321 */       .append(paramClassDoc.name().replace(".", "$"));
/*  322 */     return stringBuilder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTypeInProfile(ClassDoc paramClassDoc, int paramInt) {
/*  333 */     return (this.configuration.profiles.getProfile(getTypeNameForProfile(paramClassDoc)) <= paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent, int paramInt) {
/*  339 */     if (paramArrayOfClassDoc.length > 0) {
/*  340 */       Arrays.sort((Object[])paramArrayOfClassDoc);
/*  341 */       Content content = getTableCaption((Content)new RawHtml(paramString1));
/*  342 */       HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.typeSummary, 0, 3, 0, paramString2, content);
/*      */       
/*  344 */       htmlTree1.addContent(getSummaryTableHeader(paramArrayOfString, "col"));
/*  345 */       HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/*  346 */       for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/*  347 */         if (isTypeInProfile(paramArrayOfClassDoc[b], paramInt))
/*      */         {
/*      */           
/*  350 */           if (Util.isCoreClass(paramArrayOfClassDoc[b]) && this.configuration
/*  351 */             .isGeneratedDoc(paramArrayOfClassDoc[b])) {
/*      */ 
/*      */             
/*  354 */             Content content1 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE, paramArrayOfClassDoc[b]));
/*      */             
/*  356 */             HtmlTree htmlTree3 = HtmlTree.TD(HtmlStyle.colFirst, content1);
/*  357 */             HtmlTree htmlTree4 = HtmlTree.TR((Content)htmlTree3);
/*  358 */             if (b % 2 == 0) {
/*  359 */               htmlTree4.addStyle(HtmlStyle.altColor);
/*      */             } else {
/*  361 */               htmlTree4.addStyle(HtmlStyle.rowColor);
/*  362 */             }  HtmlTree htmlTree5 = new HtmlTree(HtmlTag.TD);
/*  363 */             htmlTree5.addStyle(HtmlStyle.colLast);
/*  364 */             if (Util.isDeprecated((Doc)paramArrayOfClassDoc[b])) {
/*  365 */               htmlTree5.addContent(this.deprecatedLabel);
/*  366 */               if ((paramArrayOfClassDoc[b].tags("deprecated")).length > 0) {
/*  367 */                 addSummaryDeprecatedComment((Doc)paramArrayOfClassDoc[b], paramArrayOfClassDoc[b]
/*  368 */                     .tags("deprecated")[0], (Content)htmlTree5);
/*      */               }
/*      */             } else {
/*      */               
/*  372 */               addSummaryComment((Doc)paramArrayOfClassDoc[b], (Content)htmlTree5);
/*  373 */             }  htmlTree4.addContent((Content)htmlTree5);
/*  374 */             htmlTree2.addContent((Content)htmlTree4);
/*      */           }  } 
/*  376 */       }  htmlTree1.addContent((Content)htmlTree2);
/*  377 */       paramContent.addContent((Content)htmlTree1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printHtmlDocument(String[] paramArrayOfString, boolean paramBoolean, Content paramContent) throws IOException {
/*  393 */     DocType docType = DocType.TRANSITIONAL;
/*  394 */     Comment comment = new Comment(this.configuration.getText("doclet.New_Page"));
/*  395 */     HtmlTree htmlTree1 = new HtmlTree(HtmlTag.HEAD);
/*  396 */     htmlTree1.addContent((Content)getGeneratedBy(!this.configuration.notimestamp));
/*  397 */     if (this.configuration.charset.length() > 0) {
/*  398 */       HtmlTree htmlTree = HtmlTree.META("Content-Type", "text/html", this.configuration.charset);
/*      */       
/*  400 */       htmlTree1.addContent((Content)htmlTree);
/*      */     } 
/*  402 */     htmlTree1.addContent((Content)getTitle());
/*  403 */     if (!this.configuration.notimestamp) {
/*  404 */       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
/*  405 */       HtmlTree htmlTree = HtmlTree.META("date", simpleDateFormat.format(new Date()));
/*  406 */       htmlTree1.addContent((Content)htmlTree);
/*      */     } 
/*  408 */     if (paramArrayOfString != null) {
/*  409 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  410 */         HtmlTree htmlTree = HtmlTree.META("keywords", paramArrayOfString[b]);
/*  411 */         htmlTree1.addContent((Content)htmlTree);
/*      */       } 
/*      */     }
/*  414 */     htmlTree1.addContent((Content)getStyleSheetProperties());
/*  415 */     htmlTree1.addContent((Content)getScriptProperties());
/*  416 */     HtmlTree htmlTree2 = HtmlTree.HTML(this.configuration.getLocale().getLanguage(), (Content)htmlTree1, paramContent);
/*      */     
/*  418 */     HtmlDocument htmlDocument = new HtmlDocument((Content)docType, (Content)comment, (Content)htmlTree2);
/*      */     
/*  420 */     write((Content)htmlDocument);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getWindowTitle(String paramString) {
/*  430 */     if (this.configuration.windowtitle.length() > 0) {
/*  431 */       paramString = paramString + " (" + this.configuration.windowtitle + ")";
/*      */     }
/*  433 */     return paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getUserHeaderFooter(boolean paramBoolean) {
/*      */     String str;
/*  444 */     if (paramBoolean) {
/*  445 */       str = replaceDocRootDir(this.configuration.header);
/*      */     }
/*  447 */     else if (this.configuration.footer.length() != 0) {
/*  448 */       str = replaceDocRootDir(this.configuration.footer);
/*      */     } else {
/*  450 */       str = replaceDocRootDir(this.configuration.header);
/*      */     } 
/*      */     
/*  453 */     return (Content)new RawHtml(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTop(Content paramContent) {
/*  463 */     RawHtml rawHtml = new RawHtml(replaceDocRootDir(this.configuration.top));
/*  464 */     paramContent.addContent((Content)rawHtml);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBottom(Content paramContent) {
/*  473 */     RawHtml rawHtml = new RawHtml(replaceDocRootDir(this.configuration.bottom));
/*  474 */     HtmlTree htmlTree1 = HtmlTree.SMALL((Content)rawHtml);
/*  475 */     HtmlTree htmlTree2 = HtmlTree.P(HtmlStyle.legalCopy, (Content)htmlTree1);
/*  476 */     paramContent.addContent((Content)htmlTree2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addNavLinks(boolean paramBoolean, Content paramContent) {
/*  486 */     if (!this.configuration.nonavbar) {
/*  487 */       String str = "allclasses_";
/*  488 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.DIV);
/*  489 */       Content content = this.configuration.getResource("doclet.Skip_navigation_links");
/*  490 */       if (paramBoolean) {
/*  491 */         paramContent.addContent(HtmlConstants.START_OF_TOP_NAVBAR);
/*  492 */         htmlTree1.addStyle(HtmlStyle.topNav);
/*  493 */         str = str + "navbar_top";
/*  494 */         Content content1 = getMarkerAnchor(SectionName.NAVBAR_TOP);
/*      */         
/*  496 */         htmlTree1.addContent(content1);
/*  497 */         HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.skipNav, getHyperLink(
/*  498 */               getDocLink(SectionName.SKIP_NAVBAR_TOP), content, content
/*  499 */               .toString(), ""));
/*  500 */         htmlTree1.addContent((Content)htmlTree);
/*      */       } else {
/*  502 */         paramContent.addContent(HtmlConstants.START_OF_BOTTOM_NAVBAR);
/*  503 */         htmlTree1.addStyle(HtmlStyle.bottomNav);
/*  504 */         str = str + "navbar_bottom";
/*  505 */         Content content1 = getMarkerAnchor(SectionName.NAVBAR_BOTTOM);
/*  506 */         htmlTree1.addContent(content1);
/*  507 */         HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.skipNav, getHyperLink(
/*  508 */               getDocLink(SectionName.SKIP_NAVBAR_BOTTOM), content, content
/*  509 */               .toString(), ""));
/*  510 */         htmlTree1.addContent((Content)htmlTree);
/*      */       } 
/*  512 */       if (paramBoolean) {
/*  513 */         htmlTree1.addContent(getMarkerAnchor(SectionName.NAVBAR_TOP_FIRSTROW));
/*      */       } else {
/*  515 */         htmlTree1.addContent(getMarkerAnchor(SectionName.NAVBAR_BOTTOM_FIRSTROW));
/*      */       } 
/*  517 */       HtmlTree htmlTree2 = new HtmlTree(HtmlTag.UL);
/*  518 */       htmlTree2.addStyle(HtmlStyle.navList);
/*  519 */       htmlTree2.addAttr(HtmlAttr.TITLE, this.configuration
/*  520 */           .getText("doclet.Navigation"));
/*  521 */       if (this.configuration.createoverview) {
/*  522 */         htmlTree2.addContent(getNavLinkContents());
/*      */       }
/*  524 */       if (this.configuration.packages.length == 1) {
/*  525 */         htmlTree2.addContent(getNavLinkPackage(this.configuration.packages[0]));
/*  526 */       } else if (this.configuration.packages.length > 1) {
/*  527 */         htmlTree2.addContent(getNavLinkPackage());
/*      */       } 
/*  529 */       htmlTree2.addContent(getNavLinkClass());
/*  530 */       if (this.configuration.classuse) {
/*  531 */         htmlTree2.addContent(getNavLinkClassUse());
/*      */       }
/*  533 */       if (this.configuration.createtree) {
/*  534 */         htmlTree2.addContent(getNavLinkTree());
/*      */       }
/*  536 */       if (!this.configuration.nodeprecated && !this.configuration.nodeprecatedlist)
/*      */       {
/*  538 */         htmlTree2.addContent(getNavLinkDeprecated());
/*      */       }
/*  540 */       if (this.configuration.createindex) {
/*  541 */         htmlTree2.addContent(getNavLinkIndex());
/*      */       }
/*  543 */       if (!this.configuration.nohelp) {
/*  544 */         htmlTree2.addContent(getNavLinkHelp());
/*      */       }
/*  546 */       htmlTree1.addContent((Content)htmlTree2);
/*  547 */       HtmlTree htmlTree3 = HtmlTree.DIV(HtmlStyle.aboutLanguage, getUserHeaderFooter(paramBoolean));
/*  548 */       htmlTree1.addContent((Content)htmlTree3);
/*  549 */       paramContent.addContent((Content)htmlTree1);
/*  550 */       HtmlTree htmlTree4 = HtmlTree.UL(HtmlStyle.navList, getNavLinkPrevious());
/*  551 */       htmlTree4.addContent(getNavLinkNext());
/*  552 */       HtmlTree htmlTree5 = HtmlTree.DIV(HtmlStyle.subNav, (Content)htmlTree4);
/*  553 */       HtmlTree htmlTree6 = HtmlTree.UL(HtmlStyle.navList, getNavShowLists());
/*  554 */       htmlTree6.addContent(getNavHideLists(this.filename));
/*  555 */       htmlTree5.addContent((Content)htmlTree6);
/*  556 */       HtmlTree htmlTree7 = HtmlTree.UL(HtmlStyle.navList, getNavLinkClassIndex());
/*  557 */       htmlTree7.addAttr(HtmlAttr.ID, str.toString());
/*  558 */       htmlTree5.addContent((Content)htmlTree7);
/*  559 */       htmlTree5.addContent(getAllClassesLinkScript(str.toString()));
/*  560 */       addSummaryDetailLinks((Content)htmlTree5);
/*  561 */       if (paramBoolean) {
/*  562 */         htmlTree5.addContent(getMarkerAnchor(SectionName.SKIP_NAVBAR_TOP));
/*  563 */         paramContent.addContent((Content)htmlTree5);
/*  564 */         paramContent.addContent(HtmlConstants.END_OF_TOP_NAVBAR);
/*      */       } else {
/*  566 */         htmlTree5.addContent(getMarkerAnchor(SectionName.SKIP_NAVBAR_BOTTOM));
/*  567 */         paramContent.addContent((Content)htmlTree5);
/*  568 */         paramContent.addContent(HtmlConstants.END_OF_BOTTOM_NAVBAR);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkNext() {
/*  580 */     return getNavLinkNext((DocPath)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkPrevious() {
/*  590 */     return getNavLinkPrevious((DocPath)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addSummaryDetailLinks(Content paramContent) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkContents() {
/*  605 */     Content content = getHyperLink(this.pathToRoot.resolve(DocPaths.OVERVIEW_SUMMARY), this.overviewLabel, "", "");
/*      */     
/*  607 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkPackage(PackageDoc paramPackageDoc) {
/*  618 */     Content content = getPackageLink(paramPackageDoc, this.packageLabel);
/*      */     
/*  620 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkPackage() {
/*  630 */     return (Content)HtmlTree.LI(this.packageLabel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkClassUse() {
/*  640 */     return (Content)HtmlTree.LI(this.useLabel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getNavLinkPrevious(DocPath paramDocPath) {
/*      */     HtmlTree htmlTree;
/*  652 */     if (paramDocPath != null) {
/*  653 */       htmlTree = HtmlTree.LI(getHyperLink(paramDocPath, this.prevLabel, "", ""));
/*      */     } else {
/*      */       
/*  656 */       htmlTree = HtmlTree.LI(this.prevLabel);
/*  657 */     }  return (Content)htmlTree;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getNavLinkNext(DocPath paramDocPath) {
/*      */     HtmlTree htmlTree;
/*  669 */     if (paramDocPath != null) {
/*  670 */       htmlTree = HtmlTree.LI(getHyperLink(paramDocPath, this.nextLabel, "", ""));
/*      */     } else {
/*      */       
/*  673 */       htmlTree = HtmlTree.LI(this.nextLabel);
/*  674 */     }  return (Content)htmlTree;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavShowLists(DocPath paramDocPath) {
/*  684 */     DocLink docLink = new DocLink(paramDocPath, this.path.getPath(), null);
/*  685 */     Content content = getHyperLink(docLink, this.framesLabel, "", "_top");
/*  686 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavShowLists() {
/*  696 */     return getNavShowLists(this.pathToRoot.resolve(DocPaths.INDEX));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavHideLists(DocPath paramDocPath) {
/*  706 */     Content content = getHyperLink(paramDocPath, this.noframesLabel, "", "_top");
/*  707 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkTree() {
/*      */     Content content;
/*  721 */     PackageDoc[] arrayOfPackageDoc = this.configuration.root.specifiedPackages();
/*  722 */     if (arrayOfPackageDoc.length == 1 && (this.configuration.root.specifiedClasses()).length == 0) {
/*  723 */       content = getHyperLink(pathString(arrayOfPackageDoc[0], DocPaths.PACKAGE_TREE), this.treeLabel, "", "");
/*      */     }
/*      */     else {
/*      */       
/*  727 */       content = getHyperLink(this.pathToRoot.resolve(DocPaths.OVERVIEW_TREE), this.treeLabel, "", "");
/*      */     } 
/*      */     
/*  730 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkMainTree(String paramString) {
/*  741 */     Content content = getHyperLink(this.pathToRoot.resolve(DocPaths.OVERVIEW_TREE), (Content)new StringContent(paramString));
/*      */     
/*  743 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkClass() {
/*  753 */     return (Content)HtmlTree.LI(this.classLabel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkDeprecated() {
/*  763 */     Content content = getHyperLink(this.pathToRoot.resolve(DocPaths.DEPRECATED_LIST), this.deprecatedLabel, "", "");
/*      */     
/*  765 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkClassIndex() {
/*  777 */     Content content = getHyperLink(this.pathToRoot.resolve(DocPaths.ALLCLASSES_NOFRAME), this.allclassesLabel, "", "");
/*      */ 
/*      */     
/*  780 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkIndex() {
/*  790 */     Content content = getHyperLink(this.pathToRoot.resolve(this.configuration.splitindex ? DocPaths.INDEX_FILES
/*      */           
/*  792 */           .resolve(DocPaths.indexN(1)) : DocPaths.INDEX_ALL), this.indexLabel, "", "");
/*      */ 
/*      */     
/*  795 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Content getNavLinkHelp() {
/*      */     DocPath docPath;
/*  807 */     String str = this.configuration.helpfile;
/*      */     
/*  809 */     if (str.isEmpty()) {
/*  810 */       docPath = DocPaths.HELP_DOC;
/*      */     } else {
/*  812 */       DocFile docFile = DocFile.createFileForInput(this.configuration, str);
/*  813 */       docPath = DocPath.create(docFile.getName());
/*      */     } 
/*  815 */     Content content = getHyperLink(this.pathToRoot.resolve(docPath), this.helpLabel, "", "");
/*      */     
/*  817 */     return (Content)HtmlTree.LI(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getSummaryTableHeader(String[] paramArrayOfString, String paramString) {
/*  829 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.TR);
/*  830 */     int i = paramArrayOfString.length;
/*      */     
/*  832 */     if (i == 1) {
/*  833 */       StringContent stringContent = new StringContent(paramArrayOfString[0]);
/*  834 */       htmlTree.addContent((Content)HtmlTree.TH(HtmlStyle.colOne, paramString, (Content)stringContent));
/*  835 */       return (Content)htmlTree;
/*      */     } 
/*  837 */     for (byte b = 0; b < i; b++) {
/*  838 */       StringContent stringContent = new StringContent(paramArrayOfString[b]);
/*  839 */       if (b == 0) {
/*  840 */         htmlTree.addContent((Content)HtmlTree.TH(HtmlStyle.colFirst, paramString, (Content)stringContent));
/*  841 */       } else if (b == i - 1) {
/*  842 */         htmlTree.addContent((Content)HtmlTree.TH(HtmlStyle.colLast, paramString, (Content)stringContent));
/*      */       } else {
/*  844 */         htmlTree.addContent((Content)HtmlTree.TH(paramString, (Content)stringContent));
/*      */       } 
/*  846 */     }  return (Content)htmlTree;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getTableCaption(Content paramContent) {
/*  856 */     HtmlTree htmlTree1 = HtmlTree.SPAN(paramContent);
/*  857 */     Content content = getSpace();
/*  858 */     HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.tabEnd, content);
/*  859 */     HtmlTree htmlTree3 = HtmlTree.CAPTION((Content)htmlTree1);
/*  860 */     htmlTree3.addContent((Content)htmlTree2);
/*  861 */     return (Content)htmlTree3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getMarkerAnchor(String paramString) {
/*  871 */     return getMarkerAnchor(getName(paramString), (Content)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getMarkerAnchor(SectionName paramSectionName) {
/*  881 */     return getMarkerAnchor(paramSectionName.getName(), (Content)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getMarkerAnchor(SectionName paramSectionName, String paramString) {
/*  892 */     return getMarkerAnchor(paramSectionName.getName() + getName(paramString), (Content)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getMarkerAnchor(String paramString, Content paramContent) {
/*      */     Comment comment;
/*  903 */     if (paramContent == null)
/*  904 */       comment = new Comment(" "); 
/*  905 */     return (Content)HtmlTree.A_NAME(paramString, (Content)comment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getPackageName(PackageDoc paramPackageDoc) {
/*  916 */     return (paramPackageDoc == null || paramPackageDoc.name().length() == 0) ? this.defaultPackageLabel : 
/*      */       
/*  918 */       getPackageLabel(paramPackageDoc.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getPackageLabel(String paramString) {
/*  928 */     return (Content)new StringContent(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addPackageDeprecatedAPI(List<Doc> paramList, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent) {
/*  942 */     if (paramList.size() > 0) {
/*  943 */       HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.deprecatedSummary, 0, 3, 0, paramString2, 
/*  944 */           getTableCaption(this.configuration.getResource(paramString1)));
/*  945 */       htmlTree1.addContent(getSummaryTableHeader(paramArrayOfString, "col"));
/*  946 */       HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/*  947 */       for (byte b = 0; b < paramList.size(); b++) {
/*  948 */         PackageDoc packageDoc = (PackageDoc)paramList.get(b);
/*  949 */         HtmlTree htmlTree5 = HtmlTree.TD(HtmlStyle.colOne, 
/*  950 */             getPackageLink(packageDoc, getPackageName(packageDoc)));
/*  951 */         if ((packageDoc.tags("deprecated")).length > 0) {
/*  952 */           addInlineDeprecatedComment((Doc)packageDoc, packageDoc.tags("deprecated")[0], (Content)htmlTree5);
/*      */         }
/*  954 */         HtmlTree htmlTree6 = HtmlTree.TR((Content)htmlTree5);
/*  955 */         if (b % 2 == 0) {
/*  956 */           htmlTree6.addStyle(HtmlStyle.altColor);
/*      */         } else {
/*  958 */           htmlTree6.addStyle(HtmlStyle.rowColor);
/*      */         } 
/*  960 */         htmlTree2.addContent((Content)htmlTree6);
/*      */       } 
/*  962 */       htmlTree1.addContent((Content)htmlTree2);
/*  963 */       HtmlTree htmlTree3 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/*  964 */       HtmlTree htmlTree4 = HtmlTree.UL(HtmlStyle.blockList, (Content)htmlTree3);
/*  965 */       paramContent.addContent((Content)htmlTree4);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DocPath pathString(ClassDoc paramClassDoc, DocPath paramDocPath) {
/*  976 */     return pathString(paramClassDoc.containingPackage(), paramDocPath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DocPath pathString(PackageDoc paramPackageDoc, DocPath paramDocPath) {
/*  989 */     return this.pathToRoot.resolve(DocPath.forPackage(paramPackageDoc).resolve(paramDocPath));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getPackageLink(PackageDoc paramPackageDoc, String paramString) {
/* 1000 */     return getPackageLink(paramPackageDoc, (Content)new StringContent(paramString));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getPackageLink(PackageDoc paramPackageDoc, Content paramContent) {
/* 1011 */     boolean bool = (paramPackageDoc != null && paramPackageDoc.isIncluded()) ? true : false;
/* 1012 */     if (!bool) {
/* 1013 */       PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 1014 */       for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/* 1015 */         if (arrayOfPackageDoc[b].equals(paramPackageDoc)) {
/* 1016 */           bool = true;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1021 */     if (bool || paramPackageDoc == null) {
/* 1022 */       return getHyperLink(pathString(paramPackageDoc, DocPaths.PACKAGE_SUMMARY), paramContent);
/*      */     }
/*      */     
/* 1025 */     DocLink docLink = getCrossPackageLink(Util.getPackageName(paramPackageDoc));
/* 1026 */     if (docLink != null) {
/* 1027 */       return getHyperLink(docLink, paramContent);
/*      */     }
/* 1029 */     return paramContent;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Content italicsClassName(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 1035 */     StringContent stringContent = new StringContent(paramBoolean ? paramClassDoc.qualifiedName() : paramClassDoc.name());
/* 1036 */     return paramClassDoc.isInterface() ? (Content)HtmlTree.SPAN(HtmlStyle.interfaceName, (Content)stringContent) : (Content)stringContent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSrcLink(ProgramElementDoc paramProgramElementDoc, Content paramContent1, Content paramContent2) {
/* 1047 */     if (paramProgramElementDoc == null) {
/*      */       return;
/*      */     }
/* 1050 */     ClassDoc classDoc = paramProgramElementDoc.containingClass();
/* 1051 */     if (classDoc == null)
/*      */     {
/* 1053 */       classDoc = (ClassDoc)paramProgramElementDoc;
/*      */     }
/*      */ 
/*      */     
/* 1057 */     DocPath docPath = this.pathToRoot.resolve(DocPaths.SOURCE_OUTPUT).resolve(DocPath.forClass(classDoc));
/* 1058 */     Content content = getHyperLink(docPath.fragment(SourceToHTMLConverter.getAnchorName((Doc)paramProgramElementDoc)), paramContent1, "", "");
/* 1059 */     paramContent2.addContent(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getLink(LinkInfoImpl paramLinkInfoImpl) {
/* 1070 */     LinkFactoryImpl linkFactoryImpl = new LinkFactoryImpl(this);
/* 1071 */     return linkFactoryImpl.getLink(paramLinkInfoImpl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getTypeParameterLinks(LinkInfoImpl paramLinkInfoImpl) {
/* 1081 */     LinkFactoryImpl linkFactoryImpl = new LinkFactoryImpl(this);
/* 1082 */     return linkFactoryImpl.getTypeParameterLinks(paramLinkInfoImpl, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getCrossClassLink(String paramString1, String paramString2, Content paramContent, boolean paramBoolean1, String paramString3, boolean paramBoolean2) {
/* 1102 */     String str1 = "";
/* 1103 */     String str2 = (paramString1 == null) ? "" : paramString1;
/*      */     int i;
/* 1105 */     while ((i = str2.lastIndexOf('.')) != -1) {
/*      */       HtmlTree htmlTree;
/* 1107 */       str1 = str2.substring(i + 1, str2.length()) + ((str1.length() > 0) ? ("." + str1) : "");
/* 1108 */       StringContent stringContent = new StringContent(str1);
/* 1109 */       if (paramBoolean2)
/* 1110 */         htmlTree = HtmlTree.CODE((Content)stringContent); 
/* 1111 */       str2 = str2.substring(0, i);
/* 1112 */       if (getCrossPackageLink(str2) != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1118 */         DocLink docLink = this.configuration.extern.getExternalLink(str2, this.pathToRoot, str1 + ".html", paramString2);
/*      */         
/* 1120 */         return getHyperLink(docLink, (paramContent == null || paramContent
/* 1121 */             .isEmpty()) ? (Content)htmlTree : paramContent, paramBoolean1, paramString3, this.configuration
/*      */             
/* 1123 */             .getText("doclet.Href_Class_Or_Interface_Title", str2), "");
/*      */       } 
/*      */     } 
/*      */     
/* 1127 */     return null;
/*      */   }
/*      */   
/*      */   public boolean isClassLinkable(ClassDoc paramClassDoc) {
/* 1131 */     if (paramClassDoc.isIncluded()) {
/* 1132 */       return this.configuration.isGeneratedDoc(paramClassDoc);
/*      */     }
/* 1134 */     return this.configuration.extern.isExternal((ProgramElementDoc)paramClassDoc);
/*      */   }
/*      */   
/*      */   public DocLink getCrossPackageLink(String paramString) {
/* 1138 */     return this.configuration.extern.getExternalLink(paramString, this.pathToRoot, DocPaths.PACKAGE_SUMMARY
/* 1139 */         .getPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc) {
/* 1150 */     return getLink((new LinkInfoImpl(this.configuration, paramKind, paramClassDoc))
/* 1151 */         .label(this.configuration.getClassName(paramClassDoc)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPreQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, Content paramContent) {
/* 1162 */     addPreQualifiedClassLink(paramKind, paramClassDoc, false, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getPreQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, boolean paramBoolean) {
/* 1176 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 1177 */     PackageDoc packageDoc = paramClassDoc.containingPackage();
/* 1178 */     if (packageDoc != null && !this.configuration.shouldExcludeQualifier(packageDoc.name())) {
/* 1179 */       contentBuilder.addContent(getPkgName(paramClassDoc));
/*      */     }
/* 1181 */     contentBuilder.addContent(getLink((new LinkInfoImpl(this.configuration, paramKind, paramClassDoc))
/* 1182 */           .label(paramClassDoc.name()).strong(paramBoolean)));
/* 1183 */     return (Content)contentBuilder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPreQualifiedClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, boolean paramBoolean, Content paramContent) {
/* 1198 */     PackageDoc packageDoc = paramClassDoc.containingPackage();
/* 1199 */     if (packageDoc != null && !this.configuration.shouldExcludeQualifier(packageDoc.name())) {
/* 1200 */       paramContent.addContent(getPkgName(paramClassDoc));
/*      */     }
/* 1202 */     paramContent.addContent(getLink((new LinkInfoImpl(this.configuration, paramKind, paramClassDoc))
/* 1203 */           .label(paramClassDoc.name()).strong(paramBoolean)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPreQualifiedStrongClassLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, Content paramContent) {
/* 1215 */     addPreQualifiedClassLink(paramKind, paramClassDoc, true, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, MemberDoc paramMemberDoc, String paramString) {
/* 1227 */     return getDocLink(paramKind, paramMemberDoc.containingClass(), paramMemberDoc, (Content)new StringContent(paramString));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, MemberDoc paramMemberDoc, String paramString, boolean paramBoolean) {
/* 1242 */     return getDocLink(paramKind, paramMemberDoc.containingClass(), paramMemberDoc, paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, String paramString, boolean paramBoolean) {
/* 1259 */     return getDocLink(paramKind, paramClassDoc, paramMemberDoc, paramString, paramBoolean, false);
/*      */   }
/*      */   
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, Content paramContent, boolean paramBoolean) {
/* 1263 */     return getDocLink(paramKind, paramClassDoc, paramMemberDoc, paramContent, paramBoolean, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/* 1281 */     return getDocLink(paramKind, paramClassDoc, paramMemberDoc, (Content)new StringContent(check(paramString)), paramBoolean1, paramBoolean2);
/*      */   }
/*      */   
/*      */   String check(String paramString) {
/* 1285 */     if (paramString.matches(".*[&<>].*")) throw new IllegalArgumentException(paramString); 
/* 1286 */     return paramString;
/*      */   }
/*      */ 
/*      */   
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, Content paramContent, boolean paramBoolean1, boolean paramBoolean2) {
/* 1291 */     if (!paramMemberDoc.isIncluded() && 
/* 1292 */       !Util.isLinkable(paramClassDoc, this.configuration))
/* 1293 */       return paramContent; 
/* 1294 */     if (paramMemberDoc instanceof ExecutableMemberDoc) {
/* 1295 */       ExecutableMemberDoc executableMemberDoc = (ExecutableMemberDoc)paramMemberDoc;
/* 1296 */       return getLink((new LinkInfoImpl(this.configuration, paramKind, paramClassDoc))
/* 1297 */           .label(paramContent).where(getName(getAnchor(executableMemberDoc, paramBoolean2))).strong(paramBoolean1));
/* 1298 */     }  if (paramMemberDoc instanceof MemberDoc) {
/* 1299 */       return getLink((new LinkInfoImpl(this.configuration, paramKind, paramClassDoc))
/* 1300 */           .label(paramContent).where(getName(paramMemberDoc.name())).strong(paramBoolean1));
/*      */     }
/* 1302 */     return paramContent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content getDocLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, MemberDoc paramMemberDoc, Content paramContent) {
/* 1319 */     if (!paramMemberDoc.isIncluded() && 
/* 1320 */       !Util.isLinkable(paramClassDoc, this.configuration))
/* 1321 */       return paramContent; 
/* 1322 */     if (paramMemberDoc instanceof ExecutableMemberDoc) {
/* 1323 */       ExecutableMemberDoc executableMemberDoc = (ExecutableMemberDoc)paramMemberDoc;
/* 1324 */       return getLink((new LinkInfoImpl(this.configuration, paramKind, paramClassDoc))
/* 1325 */           .label(paramContent).where(getName(getAnchor(executableMemberDoc))));
/* 1326 */     }  if (paramMemberDoc instanceof MemberDoc) {
/* 1327 */       return getLink((new LinkInfoImpl(this.configuration, paramKind, paramClassDoc))
/* 1328 */           .label(paramContent).where(getName(paramMemberDoc.name())));
/*      */     }
/* 1330 */     return paramContent;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAnchor(ExecutableMemberDoc paramExecutableMemberDoc) {
/* 1335 */     return getAnchor(paramExecutableMemberDoc, false);
/*      */   }
/*      */   
/*      */   public String getAnchor(ExecutableMemberDoc paramExecutableMemberDoc, boolean paramBoolean) {
/* 1339 */     if (paramBoolean) {
/* 1340 */       return paramExecutableMemberDoc.name();
/*      */     }
/* 1342 */     StringBuilder stringBuilder1 = new StringBuilder(paramExecutableMemberDoc.signature());
/* 1343 */     StringBuilder stringBuilder2 = new StringBuilder();
/* 1344 */     byte b1 = 0;
/* 1345 */     for (byte b2 = 0; b2 < stringBuilder1.length(); b2++) {
/* 1346 */       char c = stringBuilder1.charAt(b2);
/* 1347 */       if (c == '<') {
/* 1348 */         b1++;
/* 1349 */       } else if (c == '>') {
/* 1350 */         b1--;
/* 1351 */       } else if (b1 == 0) {
/* 1352 */         stringBuilder2.append(c);
/*      */       } 
/*      */     } 
/* 1355 */     return paramExecutableMemberDoc.name() + stringBuilder2.toString();
/*      */   }
/*      */   
/*      */   public Content seeTagToContent(SeeTag paramSeeTag) {
/* 1359 */     String str1 = paramSeeTag.name();
/* 1360 */     if (!str1.startsWith("@link") && !str1.equals("@see")) {
/* 1361 */       return (Content)new ContentBuilder();
/*      */     }
/*      */     
/* 1364 */     String str2 = replaceDocRootDir(Util.normalizeNewlines(paramSeeTag.text()));
/*      */ 
/*      */     
/* 1367 */     if (str2.startsWith("<") || str2.startsWith("\"")) {
/* 1368 */       return (Content)new RawHtml(str2);
/*      */     }
/*      */     
/* 1371 */     boolean bool = str1.equalsIgnoreCase("@linkplain");
/* 1372 */     Content content1 = plainOrCode(bool, (Content)new RawHtml(paramSeeTag.label()));
/*      */ 
/*      */     
/* 1375 */     Content content2 = plainOrCode(bool, (Content)new RawHtml(str2));
/*      */     
/* 1377 */     ClassDoc classDoc1 = paramSeeTag.referencedClass();
/* 1378 */     String str3 = paramSeeTag.referencedClassName();
/* 1379 */     MemberDoc memberDoc = paramSeeTag.referencedMember();
/* 1380 */     String str4 = paramSeeTag.referencedMemberName();
/*      */     
/* 1382 */     if (classDoc1 == null) {
/*      */       
/* 1384 */       PackageDoc packageDoc = paramSeeTag.referencedPackage();
/* 1385 */       if (packageDoc != null && packageDoc.isIncluded()) {
/*      */         
/* 1387 */         if (content1.isEmpty())
/* 1388 */           content1 = plainOrCode(bool, (Content)new StringContent(packageDoc.name())); 
/* 1389 */         return getPackageLink(packageDoc, content1);
/*      */       } 
/*      */ 
/*      */       
/* 1393 */       DocLink docLink = getCrossPackageLink(str3);
/* 1394 */       if (docLink != null)
/*      */       {
/* 1396 */         return getHyperLink(docLink, 
/* 1397 */             content1.isEmpty() ? content2 : content1); }  Content content;
/* 1398 */       if ((content = getCrossClassLink(str3, str4, content1, false, "", !bool)) != null)
/*      */       {
/*      */         
/* 1401 */         return content;
/*      */       }
/*      */       
/* 1404 */       this.configuration.getDocletSpecificMsg().warning(paramSeeTag.position(), "doclet.see.class_or_package_not_found", new Object[] { str1, str2 });
/*      */       
/* 1406 */       return content1.isEmpty() ? content2 : content1;
/*      */     } 
/*      */     
/* 1409 */     if (str4 == null) {
/*      */       
/* 1411 */       if (content1.isEmpty()) {
/* 1412 */         content1 = plainOrCode(bool, (Content)new StringContent(classDoc1.name()));
/*      */       }
/* 1414 */       return getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.DEFAULT, classDoc1))
/* 1415 */           .label(content1));
/* 1416 */     }  if (memberDoc == null)
/*      */     {
/*      */       
/* 1419 */       return content1.isEmpty() ? content2 : content1;
/*      */     }
/*      */ 
/*      */     
/* 1423 */     ClassDoc classDoc2 = memberDoc.containingClass();
/* 1424 */     if (paramSeeTag.text().trim().startsWith("#") && 
/* 1425 */       !classDoc2.isPublic() && 
/* 1426 */       !Util.isLinkable(classDoc2, this.configuration))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 1431 */       if (this instanceof ClassWriterImpl) {
/* 1432 */         classDoc2 = ((ClassWriterImpl)this).getClassDoc();
/* 1433 */       } else if (!classDoc2.isPublic()) {
/* 1434 */         this.configuration.getDocletSpecificMsg().warning(paramSeeTag
/* 1435 */             .position(), "doclet.see.class_or_package_not_accessible", new Object[] { str1, classDoc2
/* 1436 */               .qualifiedName() });
/*      */       } else {
/* 1438 */         this.configuration.getDocletSpecificMsg().warning(paramSeeTag
/* 1439 */             .position(), "doclet.see.class_or_package_not_found", new Object[] { str1, str2 });
/*      */       } 
/*      */     }
/*      */     
/* 1443 */     if (this.configuration.currentcd != classDoc2) {
/* 1444 */       str4 = classDoc2.name() + "." + str4;
/*      */     }
/* 1446 */     if (memberDoc instanceof ExecutableMemberDoc && 
/* 1447 */       str4.indexOf('(') < 0) {
/* 1448 */       str4 = str4 + ((ExecutableMemberDoc)memberDoc).signature();
/*      */     }
/*      */ 
/*      */     
/* 1452 */     content2 = plainOrCode(bool, (Content)new StringContent(str4));
/*      */     
/* 1454 */     return getDocLink(LinkInfoImpl.Kind.SEE_TAG, classDoc2, memberDoc, 
/* 1455 */         content1.isEmpty() ? content2 : content1, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private Content plainOrCode(boolean paramBoolean, Content paramContent) {
/* 1460 */     return (paramBoolean || paramContent.isEmpty()) ? paramContent : (Content)HtmlTree.CODE(paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInlineComment(Doc paramDoc, Tag paramTag, Content paramContent) {
/* 1471 */     addCommentTags(paramDoc, paramTag, paramTag.inlineTags(), false, false, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInlineDeprecatedComment(Doc paramDoc, Tag paramTag, Content paramContent) {
/* 1482 */     addCommentTags(paramDoc, paramTag.inlineTags(), true, false, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSummaryComment(Doc paramDoc, Content paramContent) {
/* 1492 */     addSummaryComment(paramDoc, paramDoc.firstSentenceTags(), paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSummaryComment(Doc paramDoc, Tag[] paramArrayOfTag, Content paramContent) {
/* 1503 */     addCommentTags(paramDoc, paramArrayOfTag, false, true, paramContent);
/*      */   }
/*      */   
/*      */   public void addSummaryDeprecatedComment(Doc paramDoc, Tag paramTag, Content paramContent) {
/* 1507 */     addCommentTags(paramDoc, paramTag.firstSentenceTags(), true, true, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInlineComment(Doc paramDoc, Content paramContent) {
/* 1517 */     addCommentTags(paramDoc, paramDoc.inlineTags(), false, false, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCommentTags(Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean1, boolean paramBoolean2, Content paramContent) {
/* 1531 */     addCommentTags(paramDoc, (Tag)null, paramArrayOfTag, paramBoolean1, paramBoolean2, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCommentTags(Doc paramDoc, Tag paramTag, Tag[] paramArrayOfTag, boolean paramBoolean1, boolean paramBoolean2, Content paramContent) {
/* 1546 */     if (this.configuration.nocomment) {
/*      */       return;
/*      */     }
/*      */     
/* 1550 */     Content content = commentTagsToContent((Tag)null, paramDoc, paramArrayOfTag, paramBoolean2);
/* 1551 */     if (paramBoolean1) {
/* 1552 */       HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.deprecationComment, content);
/* 1553 */       HtmlTree htmlTree1 = HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree2);
/* 1554 */       paramContent.addContent((Content)htmlTree1);
/*      */     } else {
/*      */       
/* 1557 */       HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.block, content);
/* 1558 */       paramContent.addContent((Content)htmlTree);
/*      */     } 
/* 1560 */     if (paramArrayOfTag.length == 0) {
/* 1561 */       paramContent.addContent(getSpace());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Content commentTagsToContent(Tag paramTag, Doc paramDoc, Tag[] paramArrayOfTag, boolean paramBoolean) {
/* 1579 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 1580 */     boolean bool = false;
/*      */     
/* 1582 */     this.configuration.tagletManager.checkTags(paramDoc, paramArrayOfTag, true);
/* 1583 */     for (byte b = 0; b < paramArrayOfTag.length; b++) {
/* 1584 */       Tag tag = paramArrayOfTag[b];
/* 1585 */       String str = tag.name();
/* 1586 */       if (tag instanceof SeeTag) {
/* 1587 */         contentBuilder.addContent(seeTagToContent((SeeTag)tag));
/* 1588 */       } else if (!str.equals("Text")) {
/* 1589 */         Content content; boolean bool1 = contentBuilder.isEmpty();
/*      */         
/* 1591 */         if (this.configuration.docrootparent.length() > 0 && tag
/* 1592 */           .name().equals("@docRoot") && paramArrayOfTag[b + 1]
/* 1593 */           .text().startsWith("/..")) {
/*      */ 
/*      */           
/* 1596 */           bool = true;
/*      */           
/* 1598 */           StringContent stringContent = new StringContent(this.configuration.docrootparent);
/*      */         } else {
/* 1600 */           content = TagletWriter.getInlineTagOuput(this.configuration.tagletManager, paramTag, tag, 
/*      */               
/* 1602 */               getTagletWriterInstance(paramBoolean));
/*      */         } 
/* 1604 */         if (content != null)
/* 1605 */           contentBuilder.addContent(content); 
/* 1606 */         if (bool1 && paramBoolean && tag.name().equals("@inheritDoc") && !contentBuilder.isEmpty()) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1612 */         String str1 = tag.text();
/*      */         
/* 1614 */         if (bool) {
/* 1615 */           str1 = str1.replaceFirst("/..", "");
/* 1616 */           bool = false;
/*      */         } 
/*      */ 
/*      */         
/* 1620 */         str1 = redirectRelativeLinks(tag.holder(), str1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1627 */         str1 = replaceDocRootDir(str1);
/* 1628 */         if (paramBoolean) {
/* 1629 */           str1 = removeNonInlineHtmlTags(str1);
/*      */         }
/* 1631 */         str1 = Util.replaceTabs(this.configuration, str1);
/* 1632 */         str1 = Util.normalizeNewlines(str1);
/* 1633 */         contentBuilder.addContent((Content)new RawHtml(str1));
/*      */       } 
/*      */     } 
/* 1636 */     return (Content)contentBuilder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean shouldNotRedirectRelativeLinks() {
/* 1645 */     return (this instanceof com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter || this instanceof com.sun.tools.doclets.internal.toolkit.ClassWriter || this instanceof com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String redirectRelativeLinks(Doc paramDoc, String paramString) {
/*      */     DocPath docPath;
/* 1674 */     if (paramDoc == null || shouldNotRedirectRelativeLinks()) {
/* 1675 */       return paramString;
/*      */     }
/*      */ 
/*      */     
/* 1679 */     if (paramDoc instanceof ClassDoc) {
/* 1680 */       docPath = DocPath.forPackage(((ClassDoc)paramDoc).containingPackage());
/* 1681 */     } else if (paramDoc instanceof MemberDoc) {
/* 1682 */       docPath = DocPath.forPackage(((MemberDoc)paramDoc).containingPackage());
/* 1683 */     } else if (paramDoc instanceof PackageDoc) {
/* 1684 */       docPath = DocPath.forPackage((PackageDoc)paramDoc);
/*      */     } else {
/* 1686 */       return paramString;
/*      */     } 
/*      */ 
/*      */     
/* 1690 */     int i = StringUtils.indexOfIgnoreCase(paramString, "<a");
/* 1691 */     if (i >= 0) {
/* 1692 */       StringBuilder stringBuilder = new StringBuilder(paramString);
/*      */       
/* 1694 */       while (i >= 0) {
/* 1695 */         if (stringBuilder.length() > i + 2 && !Character.isWhitespace(stringBuilder.charAt(i + 2))) {
/* 1696 */           i = StringUtils.indexOfIgnoreCase(stringBuilder.toString(), "<a", i + 1);
/*      */           
/*      */           continue;
/*      */         } 
/* 1700 */         i = stringBuilder.indexOf("=", i) + 1;
/* 1701 */         int j = stringBuilder.indexOf(">", i + 1);
/* 1702 */         if (i == 0) {
/*      */           
/* 1704 */           this.configuration.root.printWarning(paramDoc
/* 1705 */               .position(), this.configuration
/* 1706 */               .getText("doclet.malformed_html_link_tag", paramString));
/*      */           break;
/*      */         } 
/* 1709 */         if (j == -1) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/* 1714 */         if (stringBuilder.substring(i, j).indexOf("\"") != -1) {
/* 1715 */           i = stringBuilder.indexOf("\"", i) + 1;
/* 1716 */           j = stringBuilder.indexOf("\"", i + 1);
/* 1717 */           if (i == 0 || j == -1) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */         
/* 1722 */         String str1 = stringBuilder.substring(i, j);
/* 1723 */         String str2 = StringUtils.toLowerCase(str1);
/* 1724 */         if (!str2.startsWith("mailto:") && 
/* 1725 */           !str2.startsWith("http:") && 
/* 1726 */           !str2.startsWith("https:") && 
/* 1727 */           !str2.startsWith("file:")) {
/*      */           
/* 1729 */           str1 = "{@" + (new DocRootTaglet()).getName() + "}/" + docPath.resolve(str1).getPath();
/* 1730 */           stringBuilder.replace(i, j, str1);
/*      */         } 
/* 1732 */         i = StringUtils.indexOfIgnoreCase(stringBuilder.toString(), "<a", i + 1);
/*      */       } 
/* 1734 */       return stringBuilder.toString();
/*      */     } 
/* 1736 */     return paramString;
/*      */   }
/*      */   
/* 1739 */   static final Set<String> blockTags = new HashSet<>();
/*      */   static {
/* 1741 */     for (HtmlTag htmlTag : HtmlTag.values()) {
/* 1742 */       if (htmlTag.blockType == HtmlTag.BlockType.BLOCK)
/* 1743 */         blockTags.add(htmlTag.value); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static String removeNonInlineHtmlTags(String paramString) {
/* 1748 */     int i = paramString.length();
/*      */     
/* 1750 */     int j = 0;
/* 1751 */     int k = paramString.indexOf('<');
/* 1752 */     if (k < 0) {
/* 1753 */       return paramString;
/*      */     }
/*      */     
/* 1756 */     StringBuilder stringBuilder = new StringBuilder();
/* 1757 */     label28: while (k != -1) {
/* 1758 */       int m = k + 1;
/* 1759 */       if (m == i)
/*      */         break; 
/* 1761 */       char c = paramString.charAt(m);
/* 1762 */       if (c == '/') {
/* 1763 */         if (++m == i)
/*      */           break; 
/* 1765 */         c = paramString.charAt(m);
/*      */       } 
/* 1767 */       int n = m;
/* 1768 */       while (isHtmlTagLetterOrDigit(c)) {
/* 1769 */         if (++m == i)
/*      */           break label28; 
/* 1771 */         c = paramString.charAt(m);
/*      */       } 
/* 1773 */       if (c == '>' && blockTags.contains(StringUtils.toLowerCase(paramString.substring(n, m)))) {
/* 1774 */         stringBuilder.append(paramString, j, k);
/* 1775 */         j = m + 1;
/*      */       } 
/* 1777 */       k = paramString.indexOf('<', m);
/*      */     } 
/* 1779 */     stringBuilder.append(paramString.substring(j));
/*      */     
/* 1781 */     return stringBuilder.toString();
/*      */   }
/*      */   
/*      */   private static boolean isHtmlTagLetterOrDigit(char paramChar) {
/* 1785 */     return (('a' <= paramChar && paramChar <= 'z') || ('A' <= paramChar && paramChar <= 'Z') || ('1' <= paramChar && paramChar <= '6'));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HtmlTree getStyleSheetProperties() {
/*      */     DocPath docPath;
/* 1796 */     String str = this.configuration.stylesheetfile;
/*      */     
/* 1798 */     if (str.isEmpty()) {
/* 1799 */       docPath = DocPaths.STYLESHEET;
/*      */     } else {
/* 1801 */       DocFile docFile = DocFile.createFileForInput(this.configuration, str);
/* 1802 */       docPath = DocPath.create(docFile.getName());
/*      */     } 
/* 1804 */     return HtmlTree.LINK("stylesheet", "text/css", this.pathToRoot
/* 1805 */         .resolve(docPath).getPath(), "Style");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HtmlTree getScriptProperties() {
/* 1816 */     return HtmlTree.SCRIPT("text/javascript", this.pathToRoot
/* 1817 */         .resolve(DocPaths.JAVASCRIPT).getPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCoreClass(ClassDoc paramClassDoc) {
/* 1827 */     return (paramClassDoc.containingClass() == null || paramClassDoc.isStatic());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnnotationInfo(PackageDoc paramPackageDoc, Content paramContent) {
/* 1838 */     addAnnotationInfo((Doc)paramPackageDoc, paramPackageDoc.annotations(), paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addReceiverAnnotationInfo(ExecutableMemberDoc paramExecutableMemberDoc, AnnotationDesc[] paramArrayOfAnnotationDesc, Content paramContent) {
/* 1851 */     addAnnotationInfo(0, (Doc)paramExecutableMemberDoc, paramArrayOfAnnotationDesc, false, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnnotationInfo(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 1861 */     addAnnotationInfo((Doc)paramProgramElementDoc, paramProgramElementDoc.annotations(), paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addAnnotationInfo(int paramInt, Doc paramDoc, Parameter paramParameter, Content paramContent) {
/* 1874 */     return addAnnotationInfo(paramInt, paramDoc, paramParameter.annotations(), false, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addAnnotationInfo(Doc paramDoc, AnnotationDesc[] paramArrayOfAnnotationDesc, Content paramContent) {
/* 1887 */     addAnnotationInfo(0, paramDoc, paramArrayOfAnnotationDesc, true, paramContent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addAnnotationInfo(int paramInt, Doc paramDoc, AnnotationDesc[] paramArrayOfAnnotationDesc, boolean paramBoolean, Content paramContent) {
/* 1901 */     List<Content> list = getAnnotations(paramInt, paramArrayOfAnnotationDesc, paramBoolean);
/* 1902 */     String str = "";
/* 1903 */     if (list.isEmpty()) {
/* 1904 */       return false;
/*      */     }
/* 1906 */     for (Content content : list) {
/* 1907 */       paramContent.addContent(str);
/* 1908 */       paramContent.addContent(content);
/* 1909 */       str = " ";
/*      */     } 
/* 1911 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Content> getAnnotations(int paramInt, AnnotationDesc[] paramArrayOfAnnotationDesc, boolean paramBoolean) {
/* 1925 */     return getAnnotations(paramInt, paramArrayOfAnnotationDesc, paramBoolean, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Content> getAnnotations(int paramInt, AnnotationDesc[] paramArrayOfAnnotationDesc, boolean paramBoolean1, boolean paramBoolean2) {
/* 1945 */     ArrayList<ContentBuilder> arrayList = new ArrayList();
/*      */     
/* 1947 */     for (byte b = 0; b < paramArrayOfAnnotationDesc.length; b++) {
/* 1948 */       AnnotationTypeDoc annotationTypeDoc = paramArrayOfAnnotationDesc[b].annotationType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1954 */       if (Util.isDocumentedAnnotation(annotationTypeDoc) || this.isAnnotationDocumented || this.isContainerDocumented) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1963 */         ContentBuilder contentBuilder = new ContentBuilder();
/* 1964 */         this.isAnnotationDocumented = false;
/* 1965 */         LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.ANNOTATION, (ClassDoc)annotationTypeDoc);
/*      */         
/* 1967 */         AnnotationDesc.ElementValuePair[] arrayOfElementValuePair = paramArrayOfAnnotationDesc[b].elementValues();
/*      */         
/* 1969 */         if (paramArrayOfAnnotationDesc[b].isSynthesized()) {
/* 1970 */           for (byte b1 = 0; b1 < arrayOfElementValuePair.length; b1++) {
/* 1971 */             AnnotationValue annotationValue = arrayOfElementValuePair[b1].value();
/* 1972 */             ArrayList<AnnotationValue> arrayList1 = new ArrayList();
/* 1973 */             if (annotationValue.value() instanceof AnnotationValue[]) {
/*      */               
/* 1975 */               AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])annotationValue.value();
/* 1976 */               arrayList1.addAll(Arrays.asList(arrayOfAnnotationValue));
/*      */             } else {
/* 1978 */               arrayList1.add(annotationValue);
/*      */             } 
/* 1980 */             String str = "";
/* 1981 */             for (AnnotationValue annotationValue1 : arrayList1) {
/* 1982 */               contentBuilder.addContent(str);
/* 1983 */               contentBuilder.addContent(annotationValueToContent(annotationValue1));
/* 1984 */               str = " ";
/*      */             }
/*      */           
/*      */           } 
/* 1988 */         } else if (isAnnotationArray(arrayOfElementValuePair)) {
/*      */ 
/*      */ 
/*      */           
/* 1992 */           if (arrayOfElementValuePair.length == 1 && this.isAnnotationDocumented)
/*      */           {
/* 1994 */             AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])arrayOfElementValuePair[0].value().value();
/* 1995 */             ArrayList arrayList1 = new ArrayList();
/* 1996 */             arrayList1.addAll(Arrays.asList(arrayOfAnnotationValue));
/* 1997 */             String str = "";
/* 1998 */             for (AnnotationValue annotationValue : arrayList1) {
/* 1999 */               contentBuilder.addContent(str);
/* 2000 */               contentBuilder.addContent(annotationValueToContent(annotationValue));
/* 2001 */               str = " ";
/*      */             }
/*      */           
/*      */           }
/*      */           else
/*      */           {
/* 2007 */             addAnnotations(annotationTypeDoc, linkInfoImpl, contentBuilder, arrayOfElementValuePair, paramInt, false);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 2012 */           addAnnotations(annotationTypeDoc, linkInfoImpl, contentBuilder, arrayOfElementValuePair, paramInt, paramBoolean1);
/*      */         } 
/*      */         
/* 2015 */         contentBuilder.addContent(paramBoolean1 ? DocletConstants.NL : "");
/* 2016 */         arrayList.add(contentBuilder);
/*      */       } 
/* 2018 */     }  return (List)arrayList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addAnnotations(AnnotationTypeDoc paramAnnotationTypeDoc, LinkInfoImpl paramLinkInfoImpl, ContentBuilder paramContentBuilder, AnnotationDesc.ElementValuePair[] paramArrayOfElementValuePair, int paramInt, boolean paramBoolean) {
/* 2034 */     paramLinkInfoImpl.label = (Content)new StringContent("@" + paramAnnotationTypeDoc.name());
/* 2035 */     paramContentBuilder.addContent(getLink(paramLinkInfoImpl));
/* 2036 */     if (paramArrayOfElementValuePair.length > 0) {
/* 2037 */       paramContentBuilder.addContent("(");
/* 2038 */       for (byte b = 0; b < paramArrayOfElementValuePair.length; b++) {
/* 2039 */         if (b > 0) {
/* 2040 */           paramContentBuilder.addContent(",");
/* 2041 */           if (paramBoolean) {
/* 2042 */             paramContentBuilder.addContent(DocletConstants.NL);
/* 2043 */             int i = paramAnnotationTypeDoc.name().length() + 2;
/* 2044 */             for (byte b1 = 0; b1 < i + paramInt; b1++) {
/* 2045 */               paramContentBuilder.addContent(" ");
/*      */             }
/*      */           } 
/*      */         } 
/* 2049 */         paramContentBuilder.addContent(getDocLink(LinkInfoImpl.Kind.ANNOTATION, (MemberDoc)paramArrayOfElementValuePair[b]
/* 2050 */               .element(), paramArrayOfElementValuePair[b].element().name(), false));
/* 2051 */         paramContentBuilder.addContent("=");
/* 2052 */         AnnotationValue annotationValue = paramArrayOfElementValuePair[b].value();
/* 2053 */         ArrayList<AnnotationValue> arrayList = new ArrayList();
/* 2054 */         if (annotationValue.value() instanceof AnnotationValue[]) {
/*      */           
/* 2056 */           AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])annotationValue.value();
/* 2057 */           arrayList.addAll(Arrays.asList(arrayOfAnnotationValue));
/*      */         } else {
/* 2059 */           arrayList.add(annotationValue);
/*      */         } 
/* 2061 */         paramContentBuilder.addContent((arrayList.size() == 1) ? "" : "{");
/* 2062 */         String str = "";
/* 2063 */         for (AnnotationValue annotationValue1 : arrayList) {
/* 2064 */           paramContentBuilder.addContent(str);
/* 2065 */           paramContentBuilder.addContent(annotationValueToContent(annotationValue1));
/* 2066 */           str = ",";
/*      */         } 
/* 2068 */         paramContentBuilder.addContent((arrayList.size() == 1) ? "" : "}");
/* 2069 */         this.isContainerDocumented = false;
/*      */       } 
/* 2071 */       paramContentBuilder.addContent(")");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isAnnotationArray(AnnotationDesc.ElementValuePair[] paramArrayOfElementValuePair) {
/* 2085 */     for (byte b = 0; b < paramArrayOfElementValuePair.length; b++) {
/* 2086 */       AnnotationValue annotationValue = paramArrayOfElementValuePair[b].value();
/* 2087 */       if (annotationValue.value() instanceof AnnotationValue[]) {
/*      */         
/* 2089 */         AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])annotationValue.value();
/* 2090 */         if (arrayOfAnnotationValue.length > 1 && 
/* 2091 */           arrayOfAnnotationValue[0].value() instanceof AnnotationDesc) {
/*      */           
/* 2093 */           AnnotationTypeDoc annotationTypeDoc = ((AnnotationDesc)arrayOfAnnotationValue[0].value()).annotationType();
/* 2094 */           this.isContainerDocumented = true;
/* 2095 */           if (Util.isDocumentedAnnotation(annotationTypeDoc)) {
/* 2096 */             this.isAnnotationDocumented = true;
/*      */           }
/* 2098 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2103 */     return false;
/*      */   }
/*      */   
/*      */   private Content annotationValueToContent(AnnotationValue paramAnnotationValue) {
/* 2107 */     if (paramAnnotationValue.value() instanceof Type) {
/* 2108 */       Type type = (Type)paramAnnotationValue.value();
/* 2109 */       if (type.asClassDoc() != null) {
/* 2110 */         LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.ANNOTATION, type);
/*      */         
/* 2112 */         linkInfoImpl
/*      */           
/* 2114 */           .label = (Content)new StringContent((type.asClassDoc().isIncluded() ? type.typeName() : type.qualifiedTypeName()) + type.dimension() + ".class");
/* 2115 */         return getLink(linkInfoImpl);
/*      */       } 
/* 2117 */       return (Content)new StringContent(type.typeName() + type.dimension() + ".class");
/*      */     } 
/* 2119 */     if (paramAnnotationValue.value() instanceof AnnotationDesc) {
/* 2120 */       List<Content> list = getAnnotations(0, new AnnotationDesc[] { (AnnotationDesc)paramAnnotationValue
/* 2121 */             .value() }, false);
/*      */       
/* 2123 */       ContentBuilder contentBuilder = new ContentBuilder();
/* 2124 */       for (Content content : list) {
/* 2125 */         contentBuilder.addContent(content);
/*      */       }
/* 2127 */       return (Content)contentBuilder;
/* 2128 */     }  if (paramAnnotationValue.value() instanceof MemberDoc) {
/* 2129 */       return getDocLink(LinkInfoImpl.Kind.ANNOTATION, (MemberDoc)paramAnnotationValue
/* 2130 */           .value(), ((MemberDoc)paramAnnotationValue
/* 2131 */           .value()).name(), false);
/*      */     }
/* 2133 */     return (Content)new StringContent(paramAnnotationValue.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Configuration configuration() {
/* 2143 */     return this.configuration;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\HtmlDocletWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */