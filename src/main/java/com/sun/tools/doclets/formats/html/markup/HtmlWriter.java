/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MethodTypes;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.util.Map;
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
/*     */ public class HtmlWriter
/*     */ {
/*     */   protected String winTitle;
/*     */   protected Configuration configuration;
/*     */   protected boolean memberDetailsListPrinted;
/*     */   protected final String[] profileTableHeader;
/*     */   protected final String[] packageTableHeader;
/*     */   protected final String useTableSummary;
/*     */   protected final String modifierTypeHeader;
/*     */   public final Content overviewLabel;
/*     */   public final Content defaultPackageLabel;
/*     */   public final Content packageLabel;
/*     */   public final Content profileLabel;
/*     */   public final Content useLabel;
/*     */   public final Content prevLabel;
/*     */   public final Content nextLabel;
/*     */   public final Content prevclassLabel;
/*     */   public final Content nextclassLabel;
/*     */   public final Content summaryLabel;
/*     */   public final Content detailLabel;
/*     */   public final Content framesLabel;
/*     */   public final Content noframesLabel;
/*     */   public final Content treeLabel;
/*     */   public final Content classLabel;
/*     */   public final Content deprecatedLabel;
/*     */   public final Content deprecatedPhrase;
/*     */   public final Content allclassesLabel;
/*     */   public final Content allpackagesLabel;
/*     */   public final Content allprofilesLabel;
/*     */   public final Content indexLabel;
/*     */   public final Content helpLabel;
/*     */   public final Content seeLabel;
/*     */   public final Content descriptionLabel;
/*     */   public final Content prevpackageLabel;
/*     */   public final Content nextpackageLabel;
/*     */   public final Content prevprofileLabel;
/*     */   public final Content nextprofileLabel;
/*     */   public final Content packagesLabel;
/*     */   public final Content profilesLabel;
/*     */   public final Content methodDetailsLabel;
/*     */   public final Content annotationTypeDetailsLabel;
/*     */   public final Content fieldDetailsLabel;
/*     */   public final Content propertyDetailsLabel;
/*     */   public final Content constructorDetailsLabel;
/*     */   public final Content enumConstantsDetailsLabel;
/*     */   public final Content specifiedByLabel;
/*     */   public final Content overridesLabel;
/*     */   public final Content descfrmClassLabel;
/*     */   public final Content descfrmInterfaceLabel;
/*     */   private final DocFile file;
/*     */   private Writer writer;
/*     */   private Content script;
/*     */   
/*     */   public HtmlWriter(Configuration paramConfiguration, DocPath paramDocPath) throws IOException, UnsupportedEncodingException {
/* 185 */     this.file = DocFile.createFileForOutput(paramConfiguration, paramDocPath);
/* 186 */     this.configuration = paramConfiguration;
/* 187 */     this.memberDetailsListPrinted = false;
/* 188 */     this
/*     */       
/* 190 */       .profileTableHeader = new String[] { paramConfiguration.getText("doclet.Profile"), paramConfiguration.getText("doclet.Description") };
/*     */     
/* 192 */     this
/*     */       
/* 194 */       .packageTableHeader = new String[] { paramConfiguration.getText("doclet.Package"), paramConfiguration.getText("doclet.Description") };
/*     */     
/* 196 */     this.useTableSummary = paramConfiguration.getText("doclet.Use_Table_Summary", paramConfiguration
/* 197 */         .getText("doclet.packages"));
/* 198 */     this.modifierTypeHeader = paramConfiguration.getText("doclet.0_and_1", paramConfiguration
/* 199 */         .getText("doclet.Modifier"), paramConfiguration
/* 200 */         .getText("doclet.Type"));
/* 201 */     this.overviewLabel = getResource("doclet.Overview");
/* 202 */     this.defaultPackageLabel = new StringContent("<Unnamed>");
/* 203 */     this.packageLabel = getResource("doclet.Package");
/* 204 */     this.profileLabel = getResource("doclet.Profile");
/* 205 */     this.useLabel = getResource("doclet.navClassUse");
/* 206 */     this.prevLabel = getResource("doclet.Prev");
/* 207 */     this.nextLabel = getResource("doclet.Next");
/* 208 */     this.prevclassLabel = getNonBreakResource("doclet.Prev_Class");
/* 209 */     this.nextclassLabel = getNonBreakResource("doclet.Next_Class");
/* 210 */     this.summaryLabel = getResource("doclet.Summary");
/* 211 */     this.detailLabel = getResource("doclet.Detail");
/* 212 */     this.framesLabel = getResource("doclet.Frames");
/* 213 */     this.noframesLabel = getNonBreakResource("doclet.No_Frames");
/* 214 */     this.treeLabel = getResource("doclet.Tree");
/* 215 */     this.classLabel = getResource("doclet.Class");
/* 216 */     this.deprecatedLabel = getResource("doclet.navDeprecated");
/* 217 */     this.deprecatedPhrase = getResource("doclet.Deprecated");
/* 218 */     this.allclassesLabel = getNonBreakResource("doclet.All_Classes");
/* 219 */     this.allpackagesLabel = getNonBreakResource("doclet.All_Packages");
/* 220 */     this.allprofilesLabel = getNonBreakResource("doclet.All_Profiles");
/* 221 */     this.indexLabel = getResource("doclet.Index");
/* 222 */     this.helpLabel = getResource("doclet.Help");
/* 223 */     this.seeLabel = getResource("doclet.See");
/* 224 */     this.descriptionLabel = getResource("doclet.Description");
/* 225 */     this.prevpackageLabel = getNonBreakResource("doclet.Prev_Package");
/* 226 */     this.nextpackageLabel = getNonBreakResource("doclet.Next_Package");
/* 227 */     this.prevprofileLabel = getNonBreakResource("doclet.Prev_Profile");
/* 228 */     this.nextprofileLabel = getNonBreakResource("doclet.Next_Profile");
/* 229 */     this.packagesLabel = getResource("doclet.Packages");
/* 230 */     this.profilesLabel = getResource("doclet.Profiles");
/* 231 */     this.methodDetailsLabel = getResource("doclet.Method_Detail");
/* 232 */     this.annotationTypeDetailsLabel = getResource("doclet.Annotation_Type_Member_Detail");
/* 233 */     this.fieldDetailsLabel = getResource("doclet.Field_Detail");
/* 234 */     this.propertyDetailsLabel = getResource("doclet.Property_Detail");
/* 235 */     this.constructorDetailsLabel = getResource("doclet.Constructor_Detail");
/* 236 */     this.enumConstantsDetailsLabel = getResource("doclet.Enum_Constant_Detail");
/* 237 */     this.specifiedByLabel = getResource("doclet.Specified_By");
/* 238 */     this.overridesLabel = getResource("doclet.Overrides");
/* 239 */     this.descfrmClassLabel = getResource("doclet.Description_From_Class");
/* 240 */     this.descfrmInterfaceLabel = getResource("doclet.Description_From_Interface");
/*     */   }
/*     */   
/*     */   public void write(Content paramContent) throws IOException {
/* 244 */     this.writer = this.file.openWriter();
/* 245 */     paramContent.write(this.writer, true);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 249 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getResource(String paramString) {
/* 259 */     return this.configuration.getResource(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNonBreakResource(String paramString) {
/* 270 */     String str = this.configuration.getText(paramString);
/* 271 */     Content content = this.configuration.newContent();
/* 272 */     int i = 0;
/*     */     int j;
/* 274 */     while ((j = str.indexOf(" ", i)) != -1) {
/* 275 */       content.addContent(str.substring(i, j));
/* 276 */       content.addContent(RawHtml.nbsp);
/* 277 */       i = j + 1;
/*     */     } 
/* 279 */     content.addContent(str.substring(i));
/* 280 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getResource(String paramString, Object paramObject) {
/* 291 */     return this.configuration.getResource(paramString, paramObject);
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
/*     */   public Content getResource(String paramString, Object paramObject1, Object paramObject2) {
/* 303 */     return this.configuration.getResource(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HtmlTree getWinTitleScript() {
/* 312 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.SCRIPT);
/* 313 */     if (this.winTitle != null && this.winTitle.length() > 0) {
/* 314 */       htmlTree.addAttr(HtmlAttr.TYPE, "text/javascript");
/*     */ 
/*     */ 
/*     */       
/* 318 */       String str = "<!--" + DocletConstants.NL + "    try {" + DocletConstants.NL + "        if (location.href.indexOf('is-external=true') == -1) {" + DocletConstants.NL + "            parent.document.title=\"" + escapeJavaScriptChars(this.winTitle) + "\";" + DocletConstants.NL + "        }" + DocletConstants.NL + "    }" + DocletConstants.NL + "    catch(err) {" + DocletConstants.NL + "    }" + DocletConstants.NL + "//-->" + DocletConstants.NL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 324 */       RawHtml rawHtml = new RawHtml(str);
/* 325 */       htmlTree.addContent(rawHtml);
/*     */     } 
/* 327 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String escapeJavaScriptChars(String paramString) {
/* 337 */     StringBuilder stringBuilder = new StringBuilder();
/* 338 */     for (byte b = 0; b < paramString.length(); b++) {
/* 339 */       char c = paramString.charAt(b);
/* 340 */       switch (c) {
/*     */         case '\b':
/* 342 */           stringBuilder.append("\\b");
/*     */           break;
/*     */         case '\t':
/* 345 */           stringBuilder.append("\\t");
/*     */           break;
/*     */         case '\n':
/* 348 */           stringBuilder.append("\\n");
/*     */           break;
/*     */         case '\f':
/* 351 */           stringBuilder.append("\\f");
/*     */           break;
/*     */         case '\r':
/* 354 */           stringBuilder.append("\\r");
/*     */           break;
/*     */         case '"':
/* 357 */           stringBuilder.append("\\\"");
/*     */           break;
/*     */         case '\'':
/* 360 */           stringBuilder.append("\\'");
/*     */           break;
/*     */         case '\\':
/* 363 */           stringBuilder.append("\\\\");
/*     */           break;
/*     */         default:
/* 366 */           if (c < ' ' || c >= '') {
/* 367 */             stringBuilder.append(String.format("\\u%04X", new Object[] { Integer.valueOf(c) })); break;
/*     */           } 
/* 369 */           stringBuilder.append(c);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 374 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getFramesetJavaScript() {
/* 383 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.SCRIPT);
/* 384 */     htmlTree.addAttr(HtmlAttr.TYPE, "text/javascript");
/* 385 */     String str = DocletConstants.NL + "    tmpTargetPage = \"\" + window.location.search;" + DocletConstants.NL + "    if (tmpTargetPage != \"\" && tmpTargetPage != \"undefined\")" + DocletConstants.NL + "        tmpTargetPage = tmpTargetPage.substring(1);" + DocletConstants.NL + "    if (tmpTargetPage.indexOf(\":\") != -1 || (tmpTargetPage != \"\" && !validURL(tmpTargetPage)))" + DocletConstants.NL + "        tmpTargetPage = \"undefined\";" + DocletConstants.NL + "    targetPage = tmpTargetPage;" + DocletConstants.NL + "    function validURL(url) {" + DocletConstants.NL + "        try {" + DocletConstants.NL + "            url = decodeURIComponent(url);" + DocletConstants.NL + "        }" + DocletConstants.NL + "        catch (error) {" + DocletConstants.NL + "            return false;" + DocletConstants.NL + "        }" + DocletConstants.NL + "        var pos = url.indexOf(\".html\");" + DocletConstants.NL + "        if (pos == -1 || pos != url.length - 5)" + DocletConstants.NL + "            return false;" + DocletConstants.NL + "        var allowNumber = false;" + DocletConstants.NL + "        var allowSep = false;" + DocletConstants.NL + "        var seenDot = false;" + DocletConstants.NL + "        for (var i = 0; i < url.length - 5; i++) {" + DocletConstants.NL + "            var ch = url.charAt(i);" + DocletConstants.NL + "            if ('a' <= ch && ch <= 'z' ||" + DocletConstants.NL + "                    'A' <= ch && ch <= 'Z' ||" + DocletConstants.NL + "                    ch == '$' ||" + DocletConstants.NL + "                    ch == '_' ||" + DocletConstants.NL + "                    ch.charCodeAt(0) > 127) {" + DocletConstants.NL + "                allowNumber = true;" + DocletConstants.NL + "                allowSep = true;" + DocletConstants.NL + "            } else if ('0' <= ch && ch <= '9'" + DocletConstants.NL + "                    || ch == '-') {" + DocletConstants.NL + "                if (!allowNumber)" + DocletConstants.NL + "                     return false;" + DocletConstants.NL + "            } else if (ch == '/' || ch == '.') {" + DocletConstants.NL + "                if (!allowSep)" + DocletConstants.NL + "                    return false;" + DocletConstants.NL + "                allowNumber = false;" + DocletConstants.NL + "                allowSep = false;" + DocletConstants.NL + "                if (ch == '.')" + DocletConstants.NL + "                     seenDot = true;" + DocletConstants.NL + "                if (ch == '/' && seenDot)" + DocletConstants.NL + "                     return false;" + DocletConstants.NL + "            } else {" + DocletConstants.NL + "                return false;" + DocletConstants.NL + "            }" + DocletConstants.NL + "        }" + DocletConstants.NL + "        return true;" + DocletConstants.NL + "    }" + DocletConstants.NL + "    function loadFrames() {" + DocletConstants.NL + "        if (targetPage != \"\" && targetPage != \"undefined\")" + DocletConstants.NL + "             top.classFrame.location = top.targetPage;" + DocletConstants.NL + "    }" + DocletConstants.NL;
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
/* 437 */     RawHtml rawHtml = new RawHtml(str);
/* 438 */     htmlTree.addContent(rawHtml);
/* 439 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlTree getBody(boolean paramBoolean, String paramString) {
/* 450 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.BODY);
/*     */     
/* 452 */     this.winTitle = paramString;
/*     */ 
/*     */     
/* 455 */     if (paramBoolean) {
/* 456 */       this.script = getWinTitleScript();
/* 457 */       htmlTree.addContent(this.script);
/* 458 */       HtmlTree htmlTree1 = HtmlTree.NOSCRIPT(
/* 459 */           HtmlTree.DIV(getResource("doclet.No_Script_Message")));
/* 460 */       htmlTree.addContent(htmlTree1);
/*     */     } 
/* 462 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateMethodTypesScript(Map<String, Integer> paramMap, Set<MethodTypes> paramSet) {
/* 473 */     String str = "";
/* 474 */     StringBuilder stringBuilder = new StringBuilder("var methods = {");
/* 475 */     for (Map.Entry<String, Integer> entry : paramMap.entrySet()) {
/* 476 */       stringBuilder.append(str);
/* 477 */       str = ",";
/* 478 */       stringBuilder.append("\"")
/* 479 */         .append((String)entry.getKey())
/* 480 */         .append("\":")
/* 481 */         .append(entry.getValue());
/*     */     } 
/* 483 */     stringBuilder.append("};").append(DocletConstants.NL);
/* 484 */     str = "";
/* 485 */     stringBuilder.append("var tabs = {");
/* 486 */     for (MethodTypes methodTypes : paramSet) {
/* 487 */       stringBuilder.append(str);
/* 488 */       str = ",";
/* 489 */       stringBuilder.append(methodTypes.value())
/* 490 */         .append(":")
/* 491 */         .append("[")
/* 492 */         .append("\"")
/* 493 */         .append(methodTypes.tabId())
/* 494 */         .append("\"")
/* 495 */         .append(str)
/* 496 */         .append("\"")
/* 497 */         .append(this.configuration.getText(methodTypes.resourceKey()))
/* 498 */         .append("\"]");
/*     */     } 
/* 500 */     stringBuilder.append("};")
/* 501 */       .append(DocletConstants.NL);
/* 502 */     addStyles(HtmlStyle.altColor, stringBuilder);
/* 503 */     addStyles(HtmlStyle.rowColor, stringBuilder);
/* 504 */     addStyles(HtmlStyle.tableTab, stringBuilder);
/* 505 */     addStyles(HtmlStyle.activeTableTab, stringBuilder);
/* 506 */     this.script.addContent(new RawHtml(stringBuilder.toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStyles(HtmlStyle paramHtmlStyle, StringBuilder paramStringBuilder) {
/* 516 */     paramStringBuilder.append("var ").append(paramHtmlStyle).append(" = \"").append(paramHtmlStyle)
/* 517 */       .append("\";").append(DocletConstants.NL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlTree getTitle() {
/* 526 */     return HtmlTree.TITLE(new StringContent(this.winTitle));
/*     */   }
/*     */ 
/*     */   
/*     */   public String codeText(String paramString) {
/* 531 */     return "<code>" + paramString + "</code>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSpace() {
/* 538 */     return RawHtml.nbsp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getModifierTypeHeader() {
/* 545 */     return this.modifierTypeHeader;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\HtmlWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */