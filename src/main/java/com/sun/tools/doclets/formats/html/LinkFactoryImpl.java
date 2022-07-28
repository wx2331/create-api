/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkInfo;
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
/*     */ public class LinkFactoryImpl
/*     */   extends LinkFactory
/*     */ {
/*     */   private HtmlDocletWriter m_writer;
/*     */   
/*     */   public LinkFactoryImpl(HtmlDocletWriter paramHtmlDocletWriter) {
/*  54 */     this.m_writer = paramHtmlDocletWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content newContent() {
/*  61 */     return (Content)new ContentBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getClassLink(LinkInfo paramLinkInfo) {
/*  68 */     LinkInfoImpl linkInfoImpl = (LinkInfoImpl)paramLinkInfo;
/*  69 */     boolean bool = (paramLinkInfo.label == null || paramLinkInfo.label.isEmpty()) ? true : false;
/*  70 */     ClassDoc classDoc = linkInfoImpl.classDoc;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     String str = (linkInfoImpl.where == null || linkInfoImpl.where.length() == 0) ? getClassToolTip(classDoc, (linkInfoImpl.type != null && 
/*     */         
/*  77 */         !classDoc.qualifiedTypeName().equals(linkInfoImpl.type.qualifiedTypeName()))) : "";
/*     */     
/*  79 */     Content content = linkInfoImpl.getClassLinkLabel(this.m_writer.configuration);
/*  80 */     ConfigurationImpl configurationImpl = this.m_writer.configuration;
/*  81 */     ContentBuilder contentBuilder = new ContentBuilder();
/*  82 */     if (classDoc.isIncluded()) {
/*  83 */       if (configurationImpl.isGeneratedDoc(classDoc)) {
/*  84 */         DocPath docPath = getPath(linkInfoImpl);
/*  85 */         if (paramLinkInfo.linkToSelf || 
/*  86 */           !DocPath.forName(classDoc).equals(this.m_writer.filename)) {
/*  87 */           contentBuilder.addContent(this.m_writer.getHyperLink(docPath
/*  88 */                 .fragment(linkInfoImpl.where), content, linkInfoImpl.isStrong, linkInfoImpl.styleName, str, linkInfoImpl.target));
/*     */ 
/*     */ 
/*     */           
/*  92 */           if (bool && !linkInfoImpl.excludeTypeParameterLinks) {
/*  93 */             contentBuilder.addContent(getTypeParameterLinks(paramLinkInfo));
/*     */           }
/*  95 */           return (Content)contentBuilder;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  99 */       Content content1 = this.m_writer.getCrossClassLink(classDoc
/* 100 */           .qualifiedName(), linkInfoImpl.where, content, linkInfoImpl.isStrong, linkInfoImpl.styleName, true);
/*     */ 
/*     */       
/* 103 */       if (content1 != null) {
/* 104 */         contentBuilder.addContent(content1);
/* 105 */         if (bool && !linkInfoImpl.excludeTypeParameterLinks) {
/* 106 */           contentBuilder.addContent(getTypeParameterLinks(paramLinkInfo));
/*     */         }
/* 108 */         return (Content)contentBuilder;
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     contentBuilder.addContent(content);
/* 113 */     if (bool && !linkInfoImpl.excludeTypeParameterLinks) {
/* 114 */       contentBuilder.addContent(getTypeParameterLinks(paramLinkInfo));
/*     */     }
/* 116 */     return (Content)contentBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getTypeParameterLink(LinkInfo paramLinkInfo, Type paramType) {
/* 125 */     LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.m_writer.configuration, ((LinkInfoImpl)paramLinkInfo).getContext(), paramType);
/* 126 */     linkInfoImpl.excludeTypeBounds = paramLinkInfo.excludeTypeBounds;
/* 127 */     linkInfoImpl.excludeTypeParameterLinks = paramLinkInfo.excludeTypeParameterLinks;
/* 128 */     linkInfoImpl.linkToSelf = paramLinkInfo.linkToSelf;
/* 129 */     linkInfoImpl.isJava5DeclarationLocation = false;
/* 130 */     return getLink(linkInfoImpl);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Content getTypeAnnotationLink(LinkInfo paramLinkInfo, AnnotationDesc paramAnnotationDesc) {
/* 135 */     throw new RuntimeException("Not implemented yet!");
/*     */   }
/*     */   public Content getTypeAnnotationLinks(LinkInfo paramLinkInfo) {
/*     */     AnnotationDesc[] arrayOfAnnotationDesc;
/* 139 */     ContentBuilder contentBuilder = new ContentBuilder();
/*     */     
/* 141 */     if (paramLinkInfo.type instanceof com.sun.javadoc.AnnotatedType) {
/* 142 */       arrayOfAnnotationDesc = paramLinkInfo.type.asAnnotatedType().annotations();
/* 143 */     } else if (paramLinkInfo.type instanceof com.sun.javadoc.TypeVariable) {
/* 144 */       arrayOfAnnotationDesc = paramLinkInfo.type.asTypeVariable().annotations();
/*     */     } else {
/* 146 */       return (Content)contentBuilder;
/*     */     } 
/*     */     
/* 149 */     if (arrayOfAnnotationDesc.length == 0) {
/* 150 */       return (Content)contentBuilder;
/*     */     }
/* 152 */     List<Content> list = this.m_writer.getAnnotations(0, arrayOfAnnotationDesc, false, paramLinkInfo.isJava5DeclarationLocation);
/*     */     
/* 154 */     boolean bool = true;
/* 155 */     for (Content content : list) {
/* 156 */       if (!bool) {
/* 157 */         contentBuilder.addContent(" ");
/*     */       }
/* 159 */       contentBuilder.addContent(content);
/* 160 */       bool = false;
/*     */     } 
/* 162 */     if (!list.isEmpty()) {
/* 163 */       contentBuilder.addContent(" ");
/*     */     }
/*     */     
/* 166 */     return (Content)contentBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getClassToolTip(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 176 */     ConfigurationImpl configurationImpl = this.m_writer.configuration;
/* 177 */     if (paramBoolean)
/* 178 */       return configurationImpl.getText("doclet.Href_Type_Param_Title", paramClassDoc
/* 179 */           .name()); 
/* 180 */     if (paramClassDoc.isInterface())
/* 181 */       return configurationImpl.getText("doclet.Href_Interface_Title", 
/* 182 */           Util.getPackageName(paramClassDoc.containingPackage())); 
/* 183 */     if (paramClassDoc.isAnnotationType())
/* 184 */       return configurationImpl.getText("doclet.Href_Annotation_Title", 
/* 185 */           Util.getPackageName(paramClassDoc.containingPackage())); 
/* 186 */     if (paramClassDoc.isEnum()) {
/* 187 */       return configurationImpl.getText("doclet.Href_Enum_Title", 
/* 188 */           Util.getPackageName(paramClassDoc.containingPackage()));
/*     */     }
/* 190 */     return configurationImpl.getText("doclet.Href_Class_Title", 
/* 191 */         Util.getPackageName(paramClassDoc.containingPackage()));
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
/*     */   private DocPath getPath(LinkInfoImpl paramLinkInfoImpl) {
/* 204 */     if (paramLinkInfoImpl.context == LinkInfoImpl.Kind.PACKAGE_FRAME)
/*     */     {
/*     */       
/* 207 */       return DocPath.forName(paramLinkInfoImpl.classDoc);
/*     */     }
/* 209 */     return this.m_writer.pathToRoot.resolve(DocPath.forClass(paramLinkInfoImpl.classDoc));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\LinkFactoryImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */