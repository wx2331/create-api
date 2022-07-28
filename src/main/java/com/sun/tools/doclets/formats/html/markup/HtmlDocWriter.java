/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.tools.doclets.formats.html.SectionName;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocLink;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HtmlDocWriter
/*     */   extends HtmlWriter
/*     */ {
/*     */   public static final String CONTENT_TYPE = "text/html";
/*     */   
/*     */   public HtmlDocWriter(Configuration paramConfiguration, DocPath paramDocPath) throws IOException {
/*  67 */     super(paramConfiguration, paramDocPath);
/*  68 */     paramConfiguration.message.notice("doclet.Generating_0", new Object[] {
/*  69 */           DocFile.createFileForOutput(paramConfiguration, paramDocPath).getPath()
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Configuration configuration();
/*     */ 
/*     */   
/*     */   public Content getHyperLink(DocPath paramDocPath, String paramString) {
/*  78 */     return getHyperLink(paramDocPath, new StringContent(paramString), false, "", "", "");
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
/*     */   public Content getHyperLink(String paramString, Content paramContent) {
/*  91 */     return getHyperLink(getDocLink(paramString), paramContent, "", "");
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
/*     */   public Content getHyperLink(SectionName paramSectionName, Content paramContent) {
/* 103 */     return getHyperLink(getDocLink(paramSectionName), paramContent, "", "");
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
/*     */   public Content getHyperLink(SectionName paramSectionName, String paramString, Content paramContent) {
/* 118 */     return getHyperLink(getDocLink(paramSectionName, paramString), paramContent, "", "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocLink getDocLink(String paramString) {
/* 128 */     return DocLink.fragment(getName(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocLink getDocLink(SectionName paramSectionName) {
/* 138 */     return DocLink.fragment(paramSectionName.getName());
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
/*     */   public DocLink getDocLink(SectionName paramSectionName, String paramString) {
/* 151 */     return DocLink.fragment(paramSectionName.getName() + getName(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName(String paramString) {
/* 161 */     StringBuilder stringBuilder = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     for (byte b = 0; b < paramString.length(); b++) {
/* 174 */       char c = paramString.charAt(b);
/* 175 */       switch (c) {
/*     */         case '(':
/*     */         case ')':
/*     */         case ',':
/*     */         case '<':
/*     */         case '>':
/* 181 */           stringBuilder.append('-');
/*     */           break;
/*     */         case ' ':
/*     */         case '[':
/*     */           break;
/*     */         case ']':
/* 187 */           stringBuilder.append(":A");
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case '$':
/* 194 */           if (b == 0)
/* 195 */             stringBuilder.append("Z:Z"); 
/* 196 */           stringBuilder.append(":D");
/*     */           break;
/*     */ 
/*     */         
/*     */         case '_':
/* 201 */           if (b == 0)
/* 202 */             stringBuilder.append("Z:Z"); 
/* 203 */           stringBuilder.append(c);
/*     */           break;
/*     */         default:
/* 206 */           stringBuilder.append(c); break;
/*     */       } 
/*     */     } 
/* 209 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getHyperLink(DocPath paramDocPath, Content paramContent) {
/* 220 */     return getHyperLink(paramDocPath, paramContent, "", "");
/*     */   }
/*     */   
/*     */   public Content getHyperLink(DocLink paramDocLink, Content paramContent) {
/* 224 */     return getHyperLink(paramDocLink, paramContent, "", "");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getHyperLink(DocPath paramDocPath, Content paramContent, boolean paramBoolean, String paramString1, String paramString2, String paramString3) {
/* 230 */     return getHyperLink(new DocLink(paramDocPath), paramContent, paramBoolean, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getHyperLink(DocLink paramDocLink, Content paramContent, boolean paramBoolean, String paramString1, String paramString2, String paramString3) {
/* 237 */     Content content = paramContent;
/* 238 */     if (paramBoolean) {
/* 239 */       content = HtmlTree.SPAN(HtmlStyle.typeNameLink, content);
/*     */     }
/* 241 */     if (paramString1 != null && paramString1.length() != 0) {
/* 242 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.FONT, new Content[] { content });
/* 243 */       htmlTree1.addAttr(HtmlAttr.CLASS, paramString1);
/* 244 */       content = htmlTree1;
/*     */     } 
/* 246 */     HtmlTree htmlTree = HtmlTree.A(paramDocLink.toString(), content);
/* 247 */     if (paramString2 != null && paramString2.length() != 0) {
/* 248 */       htmlTree.addAttr(HtmlAttr.TITLE, paramString2);
/*     */     }
/* 250 */     if (paramString3 != null && paramString3.length() != 0) {
/* 251 */       htmlTree.addAttr(HtmlAttr.TARGET, paramString3);
/*     */     }
/* 253 */     return htmlTree;
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
/*     */   public Content getHyperLink(DocPath paramDocPath, Content paramContent, String paramString1, String paramString2) {
/* 267 */     return getHyperLink(new DocLink(paramDocPath), paramContent, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Content getHyperLink(DocLink paramDocLink, Content paramContent, String paramString1, String paramString2) {
/* 272 */     HtmlTree htmlTree = HtmlTree.A(paramDocLink.toString(), paramContent);
/* 273 */     if (paramString1 != null && paramString1.length() != 0) {
/* 274 */       htmlTree.addAttr(HtmlAttr.TITLE, paramString1);
/*     */     }
/* 276 */     if (paramString2 != null && paramString2.length() != 0) {
/* 277 */       htmlTree.addAttr(HtmlAttr.TARGET, paramString2);
/*     */     }
/* 279 */     return htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPkgName(ClassDoc paramClassDoc) {
/* 288 */     String str = paramClassDoc.containingPackage().name();
/* 289 */     if (str.length() > 0) {
/* 290 */       str = str + ".";
/* 291 */       return str;
/*     */     } 
/* 293 */     return "";
/*     */   }
/*     */   
/*     */   public boolean getMemberDetailsListPrinted() {
/* 297 */     return this.memberDetailsListPrinted;
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
/*     */   public void printFramesetDocument(String paramString, boolean paramBoolean, Content paramContent) throws IOException {
/* 310 */     DocType docType = DocType.FRAMESET;
/* 311 */     Comment comment = new Comment(this.configuration.getText("doclet.New_Page"));
/* 312 */     HtmlTree htmlTree1 = new HtmlTree(HtmlTag.HEAD);
/* 313 */     htmlTree1.addContent(getGeneratedBy(!paramBoolean));
/* 314 */     if (this.configuration.charset.length() > 0) {
/* 315 */       HtmlTree htmlTree = HtmlTree.META("Content-Type", "text/html", this.configuration.charset);
/*     */       
/* 317 */       htmlTree1.addContent(htmlTree);
/*     */     } 
/* 319 */     HtmlTree htmlTree2 = HtmlTree.TITLE(new StringContent(paramString));
/* 320 */     htmlTree1.addContent(htmlTree2);
/* 321 */     htmlTree1.addContent(getFramesetJavaScript());
/* 322 */     HtmlTree htmlTree3 = HtmlTree.HTML(this.configuration.getLocale().getLanguage(), htmlTree1, paramContent);
/*     */     
/* 324 */     HtmlDocument htmlDocument = new HtmlDocument(docType, comment, htmlTree3);
/*     */     
/* 326 */     write(htmlDocument);
/*     */   }
/*     */   
/*     */   protected Comment getGeneratedBy(boolean paramBoolean) {
/* 330 */     String str = "Generated by javadoc";
/* 331 */     if (paramBoolean) {
/* 332 */       GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getDefault());
/* 333 */       Date date = gregorianCalendar.getTime();
/* 334 */       str = str + " (" + this.configuration.getDocletSpecificBuildDate() + ") on " + date;
/*     */     } 
/* 336 */     return new Comment(str);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\HtmlDocWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */