/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlAttr;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTreeWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   protected final ClassTree classtree;
/*     */   private static final String LI_CIRCLE = "circle";
/*     */   
/*     */   protected AbstractTreeWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, ClassTree paramClassTree) throws IOException {
/*  71 */     super(paramConfigurationImpl, paramDocPath);
/*  72 */     this.classtree = paramClassTree;
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
/*     */   protected void addLevelInfo(ClassDoc paramClassDoc, List<ClassDoc> paramList, boolean paramBoolean, Content paramContent) {
/*  87 */     int i = paramList.size();
/*  88 */     if (i > 0) {
/*  89 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/*  90 */       for (byte b = 0; b < i; b++) {
/*  91 */         ClassDoc classDoc = paramList.get(b);
/*  92 */         HtmlTree htmlTree1 = new HtmlTree(HtmlTag.LI);
/*  93 */         htmlTree1.addAttr(HtmlAttr.TYPE, "circle");
/*  94 */         addPartialInfo(classDoc, (Content)htmlTree1);
/*  95 */         addExtendsImplements(paramClassDoc, classDoc, (Content)htmlTree1);
/*  96 */         addLevelInfo(classDoc, this.classtree.subs(classDoc, paramBoolean), paramBoolean, (Content)htmlTree1);
/*     */         
/*  98 */         htmlTree.addContent((Content)htmlTree1);
/*     */       } 
/* 100 */       paramContent.addContent((Content)htmlTree);
/*     */     } 
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
/*     */   protected void addTree(List<ClassDoc> paramList, String paramString, Content paramContent) {
/* 114 */     if (paramList.size() > 0) {
/* 115 */       ClassDoc classDoc = paramList.get(0);
/* 116 */       Content content = getResource(paramString);
/* 117 */       paramContent.addContent((Content)HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, content));
/*     */       
/* 119 */       addLevelInfo(!classDoc.isInterface() ? classDoc : null, paramList, 
/* 120 */           (paramList == this.classtree.baseEnums()), paramContent);
/*     */     } 
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
/*     */   protected void addExtendsImplements(ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, Content paramContent) {
/* 134 */     ClassDoc[] arrayOfClassDoc = paramClassDoc2.interfaces();
/* 135 */     if (arrayOfClassDoc.length > (paramClassDoc2.isInterface() ? 1 : 0)) {
/* 136 */       Arrays.sort((Object[])arrayOfClassDoc);
/* 137 */       byte b1 = 0;
/* 138 */       for (byte b2 = 0; b2 < arrayOfClassDoc.length; b2++) {
/* 139 */         if (paramClassDoc1 != arrayOfClassDoc[b2] && (
/* 140 */           arrayOfClassDoc[b2].isPublic() || 
/* 141 */           Util.isLinkable(arrayOfClassDoc[b2], this.configuration))) {
/*     */ 
/*     */           
/* 144 */           if (!b1) {
/* 145 */             if (paramClassDoc2.isInterface()) {
/* 146 */               paramContent.addContent(" (");
/* 147 */               paramContent.addContent(getResource("doclet.also"));
/* 148 */               paramContent.addContent(" extends ");
/*     */             } else {
/* 150 */               paramContent.addContent(" (implements ");
/*     */             } 
/*     */           } else {
/* 153 */             paramContent.addContent(", ");
/*     */           } 
/* 155 */           addPreQualifiedClassLink(LinkInfoImpl.Kind.TREE, arrayOfClassDoc[b2], paramContent);
/*     */           
/* 157 */           b1++;
/*     */         } 
/*     */       } 
/* 160 */       if (b1 > 0) {
/* 161 */         paramContent.addContent(")");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPartialInfo(ClassDoc paramClassDoc, Content paramContent) {
/* 173 */     addPreQualifiedStrongClassLink(LinkInfoImpl.Kind.TREE, paramClassDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkTree() {
/* 182 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.treeLabel);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AbstractTreeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */