/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractExecutableMemberWriter
/*     */   extends AbstractMemberWriter
/*     */ {
/*     */   public AbstractExecutableMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  49 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */   
/*     */   public AbstractExecutableMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  53 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTypeParameters(ExecutableMemberDoc paramExecutableMemberDoc, Content paramContent) {
/*  64 */     Content content = getTypeParameters(paramExecutableMemberDoc);
/*  65 */     if (!content.isEmpty()) {
/*  66 */       paramContent.addContent(content);
/*  67 */       paramContent.addContent(this.writer.getSpace());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getTypeParameters(ExecutableMemberDoc paramExecutableMemberDoc) {
/*  78 */     LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER_TYPE_PARAMS, paramExecutableMemberDoc);
/*     */     
/*  80 */     return this.writer.getTypeParameterLinks(linkInfoImpl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc) {
/*  87 */     ExecutableMemberDoc executableMemberDoc = (ExecutableMemberDoc)paramProgramElementDoc;
/*  88 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)executableMemberDoc, executableMemberDoc
/*  89 */         .qualifiedName() + executableMemberDoc.flatSignature());
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
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 102 */     ExecutableMemberDoc executableMemberDoc = (ExecutableMemberDoc)paramProgramElementDoc;
/* 103 */     String str = executableMemberDoc.name();
/* 104 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 105 */         .getDocLink(paramKind, paramClassDoc, (MemberDoc)executableMemberDoc, str, false));
/*     */     
/* 107 */     HtmlTree htmlTree2 = HtmlTree.CODE((Content)htmlTree1);
/* 108 */     addParameters(executableMemberDoc, false, (Content)htmlTree2, str.length() - 1);
/* 109 */     paramContent.addContent((Content)htmlTree2);
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
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 121 */     paramContent.addContent(this.writer
/* 122 */         .getDocLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/* 123 */           .name(), false));
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
/*     */   protected void addParam(ExecutableMemberDoc paramExecutableMemberDoc, Parameter paramParameter, boolean paramBoolean, Content paramContent) {
/* 136 */     if (paramParameter.type() != null) {
/* 137 */       Content content = this.writer.getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.EXECUTABLE_MEMBER_PARAM, paramParameter
/*     */             
/* 139 */             .type())).varargs(paramBoolean));
/* 140 */       paramContent.addContent(content);
/*     */     } 
/* 142 */     if (paramParameter.name().length() > 0) {
/* 143 */       paramContent.addContent(this.writer.getSpace());
/* 144 */       paramContent.addContent(paramParameter.name());
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
/*     */   protected void addReceiverAnnotations(ExecutableMemberDoc paramExecutableMemberDoc, Type paramType, AnnotationDesc[] paramArrayOfAnnotationDesc, Content paramContent) {
/* 158 */     this.writer.addReceiverAnnotationInfo(paramExecutableMemberDoc, paramArrayOfAnnotationDesc, paramContent);
/* 159 */     paramContent.addContent(this.writer.getSpace());
/* 160 */     paramContent.addContent(paramType.typeName());
/* 161 */     LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE, paramType);
/*     */     
/* 163 */     paramContent.addContent(this.writer.getTypeParameterLinks(linkInfoImpl));
/* 164 */     paramContent.addContent(this.writer.getSpace());
/* 165 */     paramContent.addContent("this");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addParameters(ExecutableMemberDoc paramExecutableMemberDoc, Content paramContent, int paramInt) {
/* 176 */     addParameters(paramExecutableMemberDoc, true, paramContent, paramInt);
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
/*     */   protected void addParameters(ExecutableMemberDoc paramExecutableMemberDoc, boolean paramBoolean, Content paramContent, int paramInt) {
/* 188 */     paramContent.addContent("(");
/* 189 */     String str1 = "";
/* 190 */     Parameter[] arrayOfParameter = paramExecutableMemberDoc.parameters();
/* 191 */     String str2 = makeSpace(paramInt + 1);
/* 192 */     Type type = paramExecutableMemberDoc.receiverType();
/* 193 */     if (paramBoolean && type instanceof com.sun.javadoc.AnnotatedType) {
/* 194 */       AnnotationDesc[] arrayOfAnnotationDesc = type.asAnnotatedType().annotations();
/* 195 */       if (arrayOfAnnotationDesc.length > 0) {
/* 196 */         addReceiverAnnotations(paramExecutableMemberDoc, type, arrayOfAnnotationDesc, paramContent);
/* 197 */         str1 = "," + DocletConstants.NL + str2;
/*     */       } 
/*     */     } 
/*     */     byte b;
/* 201 */     for (b = 0; b < arrayOfParameter.length; b++) {
/* 202 */       paramContent.addContent(str1);
/* 203 */       Parameter parameter = arrayOfParameter[b];
/* 204 */       if (!parameter.name().startsWith("this$")) {
/* 205 */         if (paramBoolean) {
/*     */           
/* 207 */           boolean bool = this.writer.addAnnotationInfo(str2.length(), (Doc)paramExecutableMemberDoc, parameter, paramContent);
/*     */           
/* 209 */           if (bool) {
/* 210 */             paramContent.addContent(DocletConstants.NL);
/* 211 */             paramContent.addContent(str2);
/*     */           } 
/*     */         } 
/* 214 */         addParam(paramExecutableMemberDoc, parameter, (b == arrayOfParameter.length - 1 && paramExecutableMemberDoc
/* 215 */             .isVarArgs()), paramContent);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 220 */     for (int i = b + 1; i < arrayOfParameter.length; i++) {
/* 221 */       paramContent.addContent(",");
/* 222 */       paramContent.addContent(DocletConstants.NL);
/* 223 */       paramContent.addContent(str2);
/* 224 */       if (paramBoolean) {
/*     */         
/* 226 */         boolean bool = this.writer.addAnnotationInfo(str2.length(), (Doc)paramExecutableMemberDoc, arrayOfParameter[i], paramContent);
/*     */         
/* 228 */         if (bool) {
/* 229 */           paramContent.addContent(DocletConstants.NL);
/* 230 */           paramContent.addContent(str2);
/*     */         } 
/*     */       } 
/* 233 */       addParam(paramExecutableMemberDoc, arrayOfParameter[i], (i == arrayOfParameter.length - 1 && paramExecutableMemberDoc.isVarArgs()), paramContent);
/*     */     } 
/*     */     
/* 236 */     paramContent.addContent(")");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addExceptions(ExecutableMemberDoc paramExecutableMemberDoc, Content paramContent, int paramInt) {
/* 246 */     Type[] arrayOfType = paramExecutableMemberDoc.thrownExceptionTypes();
/* 247 */     if (arrayOfType.length > 0) {
/* 248 */       LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramExecutableMemberDoc);
/*     */       
/* 250 */       String str = makeSpace(paramInt + 1 - 7);
/* 251 */       paramContent.addContent(DocletConstants.NL);
/* 252 */       paramContent.addContent(str);
/* 253 */       paramContent.addContent("throws ");
/* 254 */       str = makeSpace(paramInt + 1);
/* 255 */       Content content = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, arrayOfType[0]));
/*     */       
/* 257 */       paramContent.addContent(content);
/* 258 */       for (byte b = 1; b < arrayOfType.length; b++) {
/* 259 */         paramContent.addContent(",");
/* 260 */         paramContent.addContent(DocletConstants.NL);
/* 261 */         paramContent.addContent(str);
/* 262 */         Content content1 = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, arrayOfType[b]));
/*     */         
/* 264 */         paramContent.addContent(content1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassDoc implementsMethodInIntfac(MethodDoc paramMethodDoc, ClassDoc[] paramArrayOfClassDoc) {
/* 271 */     for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 272 */       MethodDoc[] arrayOfMethodDoc = paramArrayOfClassDoc[b].methods();
/* 273 */       if (arrayOfMethodDoc.length > 0) {
/* 274 */         for (byte b1 = 0; b1 < arrayOfMethodDoc.length; b1++) {
/* 275 */           if (arrayOfMethodDoc[b1].name().equals(paramMethodDoc.name()) && arrayOfMethodDoc[b1]
/* 276 */             .signature().equals(paramMethodDoc.signature())) {
/* 277 */             return paramArrayOfClassDoc[b];
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 282 */     return null;
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
/*     */   protected String getErasureAnchor(ExecutableMemberDoc paramExecutableMemberDoc) {
/* 294 */     StringBuilder stringBuilder = new StringBuilder(paramExecutableMemberDoc.name() + "(");
/* 295 */     Parameter[] arrayOfParameter = paramExecutableMemberDoc.parameters();
/* 296 */     boolean bool = false;
/* 297 */     for (byte b = 0; b < arrayOfParameter.length; b++) {
/* 298 */       if (b > 0) {
/* 299 */         stringBuilder.append(",");
/*     */       }
/* 301 */       Type type = arrayOfParameter[b].type();
/* 302 */       bool = (bool || type.asTypeVariable() != null) ? true : false;
/* 303 */       stringBuilder.append(type.isPrimitive() ? type
/* 304 */           .typeName() : type.asClassDoc().qualifiedName());
/* 305 */       stringBuilder.append(type.dimension());
/*     */     } 
/* 307 */     stringBuilder.append(")");
/* 308 */     return bool ? this.writer.getName(stringBuilder.toString()) : null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AbstractExecutableMemberWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */