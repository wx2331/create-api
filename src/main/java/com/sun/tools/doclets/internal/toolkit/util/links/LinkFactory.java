/*     */ package com.sun.tools.doclets.internal.toolkit.util.links;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LinkFactory
/*     */ {
/*     */   protected abstract Content newContent();
/*     */   
/*     */   public Content getLink(LinkInfo paramLinkInfo) {
/*  58 */     if (paramLinkInfo.type != null) {
/*  59 */       Type type = paramLinkInfo.type;
/*  60 */       Content content = newContent();
/*  61 */       if (type.isPrimitive())
/*     */       
/*  63 */       { content.addContent(type.typeName()); }
/*  64 */       else { if (type.asAnnotatedType() != null && type.dimension().length() == 0) {
/*  65 */           content.addContent(getTypeAnnotationLinks(paramLinkInfo));
/*  66 */           paramLinkInfo.type = type.asAnnotatedType().underlyingType();
/*  67 */           content.addContent(getLink(paramLinkInfo));
/*  68 */           return content;
/*  69 */         }  if (type.asWildcardType() != null) {
/*     */           
/*  71 */           paramLinkInfo.isTypeBound = true;
/*  72 */           content.addContent("?");
/*  73 */           WildcardType wildcardType = type.asWildcardType();
/*  74 */           Type[] arrayOfType1 = wildcardType.extendsBounds();
/*  75 */           for (byte b1 = 0; b1 < arrayOfType1.length; b1++) {
/*  76 */             content.addContent((b1 > 0) ? ", " : " extends ");
/*  77 */             setBoundsLinkInfo(paramLinkInfo, arrayOfType1[b1]);
/*  78 */             content.addContent(getLink(paramLinkInfo));
/*     */           } 
/*  80 */           Type[] arrayOfType2 = wildcardType.superBounds();
/*  81 */           for (byte b2 = 0; b2 < arrayOfType2.length; b2++) {
/*  82 */             content.addContent((b2 > 0) ? ", " : " super ");
/*  83 */             setBoundsLinkInfo(paramLinkInfo, arrayOfType2[b2]);
/*  84 */             content.addContent(getLink(paramLinkInfo));
/*     */           } 
/*  86 */         } else if (type.asTypeVariable() != null) {
/*  87 */           content.addContent(getTypeAnnotationLinks(paramLinkInfo));
/*  88 */           paramLinkInfo.isTypeBound = true;
/*     */           
/*  90 */           ProgramElementDoc programElementDoc = type.asTypeVariable().owner();
/*  91 */           if (!paramLinkInfo.excludeTypeParameterLinks && programElementDoc instanceof ClassDoc) {
/*     */             
/*  93 */             paramLinkInfo.classDoc = (ClassDoc)programElementDoc;
/*  94 */             Content content1 = newContent();
/*  95 */             content1.addContent(type.typeName());
/*  96 */             paramLinkInfo.label = content1;
/*  97 */             content.addContent(getClassLink(paramLinkInfo));
/*     */           } else {
/*     */             
/* 100 */             content.addContent(type.typeName());
/*     */           } 
/*     */           
/* 103 */           Type[] arrayOfType = type.asTypeVariable().bounds();
/* 104 */           if (!paramLinkInfo.excludeTypeBounds) {
/* 105 */             paramLinkInfo.excludeTypeBounds = true;
/* 106 */             for (byte b = 0; b < arrayOfType.length; b++) {
/* 107 */               content.addContent((b > 0) ? " & " : " extends ");
/* 108 */               setBoundsLinkInfo(paramLinkInfo, arrayOfType[b]);
/* 109 */               content.addContent(getLink(paramLinkInfo));
/*     */             } 
/*     */           } 
/* 112 */         } else if (type.asClassDoc() != null) {
/*     */           
/* 114 */           if (paramLinkInfo.isTypeBound && paramLinkInfo.excludeTypeBoundsLinks) {
/*     */ 
/*     */ 
/*     */             
/* 118 */             content.addContent(type.typeName());
/* 119 */             content.addContent(getTypeParameterLinks(paramLinkInfo));
/* 120 */             return content;
/*     */           } 
/* 122 */           paramLinkInfo.classDoc = type.asClassDoc();
/* 123 */           content = newContent();
/* 124 */           content.addContent(getClassLink(paramLinkInfo));
/* 125 */           if (paramLinkInfo.includeTypeAsSepLink) {
/* 126 */             content.addContent(getTypeParameterLinks(paramLinkInfo, false));
/*     */           }
/*     */         }  }
/*     */ 
/*     */       
/* 131 */       if (paramLinkInfo.isVarArg) {
/* 132 */         if (type.dimension().length() > 2)
/*     */         {
/*     */           
/* 135 */           content.addContent(type.dimension().substring(2));
/*     */         }
/* 137 */         content.addContent("...");
/*     */       } else {
/* 139 */         while (type != null && type.dimension().length() > 0) {
/* 140 */           if (type.asAnnotatedType() != null) {
/* 141 */             paramLinkInfo.type = type;
/* 142 */             content.addContent(" ");
/* 143 */             content.addContent(getTypeAnnotationLinks(paramLinkInfo));
/* 144 */             content.addContent("[]");
/* 145 */             type = type.asAnnotatedType().underlyingType().getElementType(); continue;
/*     */           } 
/* 147 */           content.addContent("[]");
/* 148 */           type = type.getElementType();
/*     */         } 
/*     */         
/* 151 */         paramLinkInfo.type = type;
/* 152 */         Content content1 = newContent();
/* 153 */         content1.addContent(getTypeAnnotationLinks(paramLinkInfo));
/* 154 */         content1.addContent(content);
/* 155 */         content = content1;
/*     */       } 
/* 157 */       return content;
/* 158 */     }  if (paramLinkInfo.classDoc != null) {
/*     */       
/* 160 */       Content content = newContent();
/* 161 */       content.addContent(getClassLink(paramLinkInfo));
/* 162 */       if (paramLinkInfo.includeTypeAsSepLink) {
/* 163 */         content.addContent(getTypeParameterLinks(paramLinkInfo, false));
/*     */       }
/* 165 */       return content;
/*     */     } 
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setBoundsLinkInfo(LinkInfo paramLinkInfo, Type paramType) {
/* 172 */     paramLinkInfo.classDoc = null;
/* 173 */     paramLinkInfo.label = null;
/* 174 */     paramLinkInfo.type = paramType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getClassLink(LinkInfo paramLinkInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getTypeParameterLink(LinkInfo paramLinkInfo, Type paramType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getTypeAnnotationLink(LinkInfo paramLinkInfo, AnnotationDesc paramAnnotationDesc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTypeParameterLinks(LinkInfo paramLinkInfo) {
/* 205 */     return getTypeParameterLinks(paramLinkInfo, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTypeParameterLinks(LinkInfo paramLinkInfo, boolean paramBoolean) {
/*     */     TypeVariable[] arrayOfTypeVariable;
/* 217 */     Content content = newContent();
/*     */     
/* 219 */     if (paramLinkInfo.executableMemberDoc != null) {
/* 220 */       arrayOfTypeVariable = paramLinkInfo.executableMemberDoc.typeParameters();
/* 221 */     } else if (paramLinkInfo.type != null && paramLinkInfo.type
/* 222 */       .asParameterizedType() != null) {
/* 223 */       Type[] arrayOfType = paramLinkInfo.type.asParameterizedType().typeArguments();
/* 224 */     } else if (paramLinkInfo.classDoc != null) {
/* 225 */       arrayOfTypeVariable = paramLinkInfo.classDoc.typeParameters();
/*     */     } else {
/*     */       
/* 228 */       return content;
/*     */     } 
/* 230 */     if (((paramLinkInfo.includeTypeInClassLinkLabel && paramBoolean) || (paramLinkInfo.includeTypeAsSepLink && !paramBoolean)) && arrayOfTypeVariable.length > 0) {
/*     */ 
/*     */ 
/*     */       
/* 234 */       content.addContent("<");
/* 235 */       for (byte b = 0; b < arrayOfTypeVariable.length; b++) {
/* 236 */         if (b > 0) {
/* 237 */           content.addContent(",");
/*     */         }
/* 239 */         content.addContent(getTypeParameterLink(paramLinkInfo, (Type)arrayOfTypeVariable[b]));
/*     */       } 
/* 241 */       content.addContent(">");
/*     */     } 
/* 243 */     return content;
/*     */   }
/*     */   
/*     */   public Content getTypeAnnotationLinks(LinkInfo paramLinkInfo) {
/* 247 */     Content content = newContent();
/* 248 */     if (paramLinkInfo.type.asAnnotatedType() == null)
/* 249 */       return content; 
/* 250 */     AnnotationDesc[] arrayOfAnnotationDesc = paramLinkInfo.type.asAnnotatedType().annotations();
/* 251 */     for (byte b = 0; b < arrayOfAnnotationDesc.length; b++) {
/* 252 */       if (b > 0) {
/* 253 */         content.addContent(" ");
/*     */       }
/* 255 */       content.addContent(getTypeAnnotationLink(paramLinkInfo, arrayOfAnnotationDesc[b]));
/*     */     } 
/*     */     
/* 258 */     content.addContent(" ");
/* 259 */     return content;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\links\LinkFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */