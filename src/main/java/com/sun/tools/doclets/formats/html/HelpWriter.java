/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HelpWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   public HelpWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  53 */     super(paramConfigurationImpl, paramDocPath);
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl) {
/*  65 */     DocPath docPath = DocPath.empty;
/*     */     try {
/*  67 */       docPath = DocPaths.HELP_DOC;
/*  68 */       HelpWriter helpWriter = new HelpWriter(paramConfigurationImpl, docPath);
/*  69 */       helpWriter.generateHelpFile();
/*  70 */       helpWriter.close();
/*  71 */     } catch (IOException iOException) {
/*  72 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/*  74 */             .toString(), docPath });
/*  75 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHelpFile() throws IOException {
/*  83 */     String str = this.configuration.getText("doclet.Window_Help_title");
/*  84 */     HtmlTree htmlTree = getBody(true, getWindowTitle(str));
/*  85 */     addTop((Content)htmlTree);
/*  86 */     addNavLinks(true, (Content)htmlTree);
/*  87 */     addHelpFileContents((Content)htmlTree);
/*  88 */     addNavLinks(false, (Content)htmlTree);
/*  89 */     addBottom((Content)htmlTree);
/*  90 */     printHtmlDocument((String[])null, true, (Content)htmlTree);
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
/*     */   protected void addHelpFileContents(Content paramContent) {
/* 102 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, false, HtmlStyle.title, 
/* 103 */         getResource("doclet.Help_line_1"));
/* 104 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 105 */     HtmlTree htmlTree3 = HtmlTree.DIV(HtmlStyle.subTitle, 
/* 106 */         getResource("doclet.Help_line_2"));
/* 107 */     htmlTree2.addContent((Content)htmlTree3);
/* 108 */     paramContent.addContent((Content)htmlTree2);
/* 109 */     HtmlTree htmlTree4 = new HtmlTree(HtmlTag.UL);
/* 110 */     htmlTree4.addStyle(HtmlStyle.blockList);
/* 111 */     if (this.configuration.createoverview) {
/* 112 */       HtmlTree htmlTree41 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 113 */           getResource("doclet.Overview"));
/* 114 */       HtmlTree htmlTree42 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree41);
/* 115 */       Content content = getResource("doclet.Help_line_3", 
/* 116 */           getHyperLink(DocPaths.OVERVIEW_SUMMARY, this.configuration
/* 117 */             .getText("doclet.Overview")));
/* 118 */       HtmlTree htmlTree43 = HtmlTree.P(content);
/* 119 */       htmlTree42.addContent((Content)htmlTree43);
/* 120 */       htmlTree4.addContent((Content)htmlTree42);
/*     */     } 
/* 122 */     HtmlTree htmlTree5 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 123 */         getResource("doclet.Package"));
/* 124 */     HtmlTree htmlTree6 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree5);
/* 125 */     Content content1 = getResource("doclet.Help_line_4");
/* 126 */     HtmlTree htmlTree7 = HtmlTree.P(content1);
/* 127 */     htmlTree6.addContent((Content)htmlTree7);
/* 128 */     HtmlTree htmlTree8 = new HtmlTree(HtmlTag.UL);
/* 129 */     htmlTree8.addContent((Content)HtmlTree.LI(
/* 130 */           getResource("doclet.Interfaces_Italic")));
/* 131 */     htmlTree8.addContent((Content)HtmlTree.LI(
/* 132 */           getResource("doclet.Classes")));
/* 133 */     htmlTree8.addContent((Content)HtmlTree.LI(
/* 134 */           getResource("doclet.Enums")));
/* 135 */     htmlTree8.addContent((Content)HtmlTree.LI(
/* 136 */           getResource("doclet.Exceptions")));
/* 137 */     htmlTree8.addContent((Content)HtmlTree.LI(
/* 138 */           getResource("doclet.Errors")));
/* 139 */     htmlTree8.addContent((Content)HtmlTree.LI(
/* 140 */           getResource("doclet.AnnotationTypes")));
/* 141 */     htmlTree6.addContent((Content)htmlTree8);
/* 142 */     htmlTree4.addContent((Content)htmlTree6);
/* 143 */     HtmlTree htmlTree9 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 144 */         getResource("doclet.Help_line_5"));
/* 145 */     HtmlTree htmlTree10 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree9);
/* 146 */     Content content2 = getResource("doclet.Help_line_6");
/* 147 */     HtmlTree htmlTree11 = HtmlTree.P(content2);
/* 148 */     htmlTree10.addContent((Content)htmlTree11);
/* 149 */     HtmlTree htmlTree12 = new HtmlTree(HtmlTag.UL);
/* 150 */     htmlTree12.addContent((Content)HtmlTree.LI(
/* 151 */           getResource("doclet.Help_line_7")));
/* 152 */     htmlTree12.addContent((Content)HtmlTree.LI(
/* 153 */           getResource("doclet.Help_line_8")));
/* 154 */     htmlTree12.addContent((Content)HtmlTree.LI(
/* 155 */           getResource("doclet.Help_line_9")));
/* 156 */     htmlTree12.addContent((Content)HtmlTree.LI(
/* 157 */           getResource("doclet.Help_line_10")));
/* 158 */     htmlTree12.addContent((Content)HtmlTree.LI(
/* 159 */           getResource("doclet.Help_line_11")));
/* 160 */     htmlTree12.addContent((Content)HtmlTree.LI(
/* 161 */           getResource("doclet.Help_line_12")));
/* 162 */     htmlTree10.addContent((Content)htmlTree12);
/* 163 */     HtmlTree htmlTree13 = new HtmlTree(HtmlTag.UL);
/* 164 */     htmlTree13.addContent((Content)HtmlTree.LI(
/* 165 */           getResource("doclet.Nested_Class_Summary")));
/* 166 */     htmlTree13.addContent((Content)HtmlTree.LI(
/* 167 */           getResource("doclet.Field_Summary")));
/* 168 */     htmlTree13.addContent((Content)HtmlTree.LI(
/* 169 */           getResource("doclet.Constructor_Summary")));
/* 170 */     htmlTree13.addContent((Content)HtmlTree.LI(
/* 171 */           getResource("doclet.Method_Summary")));
/* 172 */     htmlTree10.addContent((Content)htmlTree13);
/* 173 */     HtmlTree htmlTree14 = new HtmlTree(HtmlTag.UL);
/* 174 */     htmlTree14.addContent((Content)HtmlTree.LI(
/* 175 */           getResource("doclet.Field_Detail")));
/* 176 */     htmlTree14.addContent((Content)HtmlTree.LI(
/* 177 */           getResource("doclet.Constructor_Detail")));
/* 178 */     htmlTree14.addContent((Content)HtmlTree.LI(
/* 179 */           getResource("doclet.Method_Detail")));
/* 180 */     htmlTree10.addContent((Content)htmlTree14);
/* 181 */     Content content3 = getResource("doclet.Help_line_13");
/* 182 */     HtmlTree htmlTree15 = HtmlTree.P(content3);
/* 183 */     htmlTree10.addContent((Content)htmlTree15);
/* 184 */     htmlTree4.addContent((Content)htmlTree10);
/*     */     
/* 186 */     HtmlTree htmlTree16 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 187 */         getResource("doclet.AnnotationType"));
/* 188 */     HtmlTree htmlTree17 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree16);
/* 189 */     Content content4 = getResource("doclet.Help_annotation_type_line_1");
/* 190 */     HtmlTree htmlTree18 = HtmlTree.P(content4);
/* 191 */     htmlTree17.addContent((Content)htmlTree18);
/* 192 */     HtmlTree htmlTree19 = new HtmlTree(HtmlTag.UL);
/* 193 */     htmlTree19.addContent((Content)HtmlTree.LI(
/* 194 */           getResource("doclet.Help_annotation_type_line_2")));
/* 195 */     htmlTree19.addContent((Content)HtmlTree.LI(
/* 196 */           getResource("doclet.Help_annotation_type_line_3")));
/* 197 */     htmlTree19.addContent((Content)HtmlTree.LI(
/* 198 */           getResource("doclet.Annotation_Type_Required_Member_Summary")));
/* 199 */     htmlTree19.addContent((Content)HtmlTree.LI(
/* 200 */           getResource("doclet.Annotation_Type_Optional_Member_Summary")));
/* 201 */     htmlTree19.addContent((Content)HtmlTree.LI(
/* 202 */           getResource("doclet.Annotation_Type_Member_Detail")));
/* 203 */     htmlTree17.addContent((Content)htmlTree19);
/* 204 */     htmlTree4.addContent((Content)htmlTree17);
/*     */     
/* 206 */     HtmlTree htmlTree20 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 207 */         getResource("doclet.Enum"));
/* 208 */     HtmlTree htmlTree21 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree20);
/* 209 */     Content content5 = getResource("doclet.Help_enum_line_1");
/* 210 */     HtmlTree htmlTree22 = HtmlTree.P(content5);
/* 211 */     htmlTree21.addContent((Content)htmlTree22);
/* 212 */     HtmlTree htmlTree23 = new HtmlTree(HtmlTag.UL);
/* 213 */     htmlTree23.addContent((Content)HtmlTree.LI(
/* 214 */           getResource("doclet.Help_enum_line_2")));
/* 215 */     htmlTree23.addContent((Content)HtmlTree.LI(
/* 216 */           getResource("doclet.Help_enum_line_3")));
/* 217 */     htmlTree23.addContent((Content)HtmlTree.LI(
/* 218 */           getResource("doclet.Enum_Constant_Summary")));
/* 219 */     htmlTree23.addContent((Content)HtmlTree.LI(
/* 220 */           getResource("doclet.Enum_Constant_Detail")));
/* 221 */     htmlTree21.addContent((Content)htmlTree23);
/* 222 */     htmlTree4.addContent((Content)htmlTree21);
/* 223 */     if (this.configuration.classuse) {
/* 224 */       HtmlTree htmlTree41 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 225 */           getResource("doclet.Help_line_14"));
/* 226 */       HtmlTree htmlTree42 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree41);
/* 227 */       Content content = getResource("doclet.Help_line_15");
/* 228 */       HtmlTree htmlTree43 = HtmlTree.P(content);
/* 229 */       htmlTree42.addContent((Content)htmlTree43);
/* 230 */       htmlTree4.addContent((Content)htmlTree42);
/*     */     } 
/* 232 */     if (this.configuration.createtree) {
/* 233 */       HtmlTree htmlTree41 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 234 */           getResource("doclet.Help_line_16"));
/* 235 */       HtmlTree htmlTree42 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree41);
/* 236 */       Content content = getResource("doclet.Help_line_17_with_tree_link", 
/* 237 */           getHyperLink(DocPaths.OVERVIEW_TREE, this.configuration
/* 238 */             .getText("doclet.Class_Hierarchy")), 
/* 239 */           HtmlTree.CODE((Content)new StringContent("java.lang.Object")));
/* 240 */       HtmlTree htmlTree43 = HtmlTree.P(content);
/* 241 */       htmlTree42.addContent((Content)htmlTree43);
/* 242 */       HtmlTree htmlTree44 = new HtmlTree(HtmlTag.UL);
/* 243 */       htmlTree44.addContent((Content)HtmlTree.LI(
/* 244 */             getResource("doclet.Help_line_18")));
/* 245 */       htmlTree44.addContent((Content)HtmlTree.LI(
/* 246 */             getResource("doclet.Help_line_19")));
/* 247 */       htmlTree42.addContent((Content)htmlTree44);
/* 248 */       htmlTree4.addContent((Content)htmlTree42);
/*     */     } 
/* 250 */     if (!this.configuration.nodeprecatedlist && !this.configuration.nodeprecated) {
/*     */       
/* 252 */       HtmlTree htmlTree41 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 253 */           getResource("doclet.Deprecated_API"));
/* 254 */       HtmlTree htmlTree42 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree41);
/* 255 */       Content content = getResource("doclet.Help_line_20_with_deprecated_api_link", 
/* 256 */           getHyperLink(DocPaths.DEPRECATED_LIST, this.configuration
/* 257 */             .getText("doclet.Deprecated_API")));
/* 258 */       HtmlTree htmlTree43 = HtmlTree.P(content);
/* 259 */       htmlTree42.addContent((Content)htmlTree43);
/* 260 */       htmlTree4.addContent((Content)htmlTree42);
/*     */     } 
/* 262 */     if (this.configuration.createindex) {
/*     */       Content content11;
/* 264 */       if (this.configuration.splitindex) {
/* 265 */         content11 = getHyperLink(DocPaths.INDEX_FILES.resolve(DocPaths.indexN(1)), this.configuration
/* 266 */             .getText("doclet.Index"));
/*     */       } else {
/* 268 */         content11 = getHyperLink(DocPaths.INDEX_ALL, this.configuration
/* 269 */             .getText("doclet.Index"));
/*     */       } 
/* 271 */       HtmlTree htmlTree41 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 272 */           getResource("doclet.Help_line_21"));
/* 273 */       HtmlTree htmlTree42 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree41);
/* 274 */       Content content12 = getResource("doclet.Help_line_22", content11);
/* 275 */       HtmlTree htmlTree43 = HtmlTree.P(content12);
/* 276 */       htmlTree42.addContent((Content)htmlTree43);
/* 277 */       htmlTree4.addContent((Content)htmlTree42);
/*     */     } 
/* 279 */     HtmlTree htmlTree24 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 280 */         getResource("doclet.Help_line_23"));
/* 281 */     HtmlTree htmlTree25 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree24);
/* 282 */     Content content6 = getResource("doclet.Help_line_24");
/* 283 */     HtmlTree htmlTree26 = HtmlTree.P(content6);
/* 284 */     htmlTree25.addContent((Content)htmlTree26);
/* 285 */     htmlTree4.addContent((Content)htmlTree25);
/* 286 */     HtmlTree htmlTree27 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 287 */         getResource("doclet.Help_line_25"));
/* 288 */     HtmlTree htmlTree28 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree27);
/* 289 */     Content content7 = getResource("doclet.Help_line_26");
/* 290 */     HtmlTree htmlTree29 = HtmlTree.P(content7);
/* 291 */     htmlTree28.addContent((Content)htmlTree29);
/* 292 */     htmlTree4.addContent((Content)htmlTree28);
/* 293 */     HtmlTree htmlTree30 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 294 */         getResource("doclet.All_Classes"));
/* 295 */     HtmlTree htmlTree31 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree30);
/* 296 */     Content content8 = getResource("doclet.Help_line_27", 
/* 297 */         getHyperLink(DocPaths.ALLCLASSES_NOFRAME, this.configuration
/* 298 */           .getText("doclet.All_Classes")));
/* 299 */     HtmlTree htmlTree32 = HtmlTree.P(content8);
/* 300 */     htmlTree31.addContent((Content)htmlTree32);
/* 301 */     htmlTree4.addContent((Content)htmlTree31);
/* 302 */     HtmlTree htmlTree33 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 303 */         getResource("doclet.Serialized_Form"));
/* 304 */     HtmlTree htmlTree34 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree33);
/* 305 */     Content content9 = getResource("doclet.Help_line_28");
/* 306 */     HtmlTree htmlTree35 = HtmlTree.P(content9);
/* 307 */     htmlTree34.addContent((Content)htmlTree35);
/* 308 */     htmlTree4.addContent((Content)htmlTree34);
/* 309 */     HtmlTree htmlTree36 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 310 */         getResource("doclet.Constants_Summary"));
/* 311 */     HtmlTree htmlTree37 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree36);
/* 312 */     Content content10 = getResource("doclet.Help_line_29", 
/* 313 */         getHyperLink(DocPaths.CONSTANT_VALUES, this.configuration
/* 314 */           .getText("doclet.Constants_Summary")));
/* 315 */     HtmlTree htmlTree38 = HtmlTree.P(content10);
/* 316 */     htmlTree37.addContent((Content)htmlTree38);
/* 317 */     htmlTree4.addContent((Content)htmlTree37);
/* 318 */     HtmlTree htmlTree39 = HtmlTree.DIV(HtmlStyle.contentContainer, (Content)htmlTree4);
/* 319 */     HtmlTree htmlTree40 = HtmlTree.SPAN(HtmlStyle.emphasizedPhrase, getResource("doclet.Help_line_30"));
/* 320 */     htmlTree39.addContent((Content)htmlTree40);
/* 321 */     paramContent.addContent((Content)htmlTree39);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkHelp() {
/* 331 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.helpLabel);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\HelpWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */