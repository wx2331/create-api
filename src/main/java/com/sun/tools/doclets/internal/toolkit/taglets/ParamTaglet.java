/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ public class ParamTaglet
/*     */   extends BaseTaglet
/*     */   implements InheritableTaglet
/*     */ {
/*     */   private static Map<String, String> getRankMap(Object[] paramArrayOfObject) {
/*  63 */     if (paramArrayOfObject == null) {
/*  64 */       return null;
/*     */     }
/*  66 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  67 */     for (byte b = 0; b < paramArrayOfObject.length; b++) {
/*     */ 
/*     */       
/*  70 */       String str = (paramArrayOfObject[b] instanceof Parameter) ? ((Parameter)paramArrayOfObject[b]).name() : ((TypeVariable)paramArrayOfObject[b]).typeName();
/*  71 */       hashMap.put(str, String.valueOf(b));
/*     */     } 
/*  73 */     return (Map)hashMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput) {
/*  80 */     if (paramInput.tagId == null) {
/*  81 */       paramInput.isTypeVariableParamTag = ((ParamTag)paramInput.tag).isTypeParameter();
/*     */ 
/*     */       
/*  84 */       Object[] arrayOfObject = paramInput.isTypeVariableParamTag ? (Object[])((MethodDoc)paramInput.tag.holder()).typeParameters() : (Object[])((MethodDoc)paramInput.tag.holder()).parameters();
/*  85 */       String str = ((ParamTag)paramInput.tag).parameterName();
/*     */       byte b1;
/*  87 */       for (b1 = 0; b1 < arrayOfObject.length; b1++) {
/*     */ 
/*     */         
/*  90 */         String str1 = (arrayOfObject[b1] instanceof Parameter) ? ((Parameter)arrayOfObject[b1]).name() : ((TypeVariable)arrayOfObject[b1]).typeName();
/*  91 */         if (str1.equals(str)) {
/*  92 */           paramInput.tagId = String.valueOf(b1);
/*     */           break;
/*     */         } 
/*     */       } 
/*  96 */       if (b1 == arrayOfObject.length) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     ParamTag[] arrayOfParamTag = paramInput.isTypeVariableParamTag ? ((MethodDoc)paramInput.element).typeParamTags() : ((MethodDoc)paramInput.element).paramTags();
/* 106 */     Map<String, String> map = getRankMap(paramInput.isTypeVariableParamTag ? (Object[])((MethodDoc)paramInput.element)
/* 107 */         .typeParameters() : (Object[])((MethodDoc)paramInput.element)
/* 108 */         .parameters());
/* 109 */     for (byte b = 0; b < arrayOfParamTag.length; b++) {
/* 110 */       if (map.containsKey(arrayOfParamTag[b].parameterName()) && ((String)map
/* 111 */         .get(arrayOfParamTag[b].parameterName())).equals(paramInput.tagId)) {
/* 112 */         paramOutput.holder = (Doc)paramInput.element;
/* 113 */         paramOutput.holderTag = (Tag)arrayOfParamTag[b];
/* 114 */         paramOutput
/* 115 */           .inlineTags = paramInput.isFirstSentence ? arrayOfParamTag[b].firstSentenceTags() : arrayOfParamTag[b].inlineTags();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inField() {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inMethod() {
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inOverview() {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inPackage() {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inType() {
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInlineTag() {
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter) {
/* 170 */     if (paramDoc instanceof ExecutableMemberDoc) {
/* 171 */       ExecutableMemberDoc executableMemberDoc = (ExecutableMemberDoc)paramDoc;
/* 172 */       Content content = getTagletOutput(false, (Doc)executableMemberDoc, paramTagletWriter, (Object[])executableMemberDoc
/* 173 */           .typeParameters(), executableMemberDoc.typeParamTags());
/* 174 */       content.addContent(getTagletOutput(true, (Doc)executableMemberDoc, paramTagletWriter, (Object[])executableMemberDoc
/* 175 */             .parameters(), executableMemberDoc.paramTags()));
/* 176 */       return content;
/*     */     } 
/* 178 */     ClassDoc classDoc = (ClassDoc)paramDoc;
/* 179 */     return getTagletOutput(false, (Doc)classDoc, paramTagletWriter, (Object[])classDoc
/* 180 */         .typeParameters(), classDoc.typeParamTags());
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
/*     */   private Content getTagletOutput(boolean paramBoolean, Doc paramDoc, TagletWriter paramTagletWriter, Object[] paramArrayOfObject, ParamTag[] paramArrayOfParamTag) {
/* 197 */     Content content = paramTagletWriter.getOutputInstance();
/* 198 */     HashSet<String> hashSet = new HashSet();
/* 199 */     if (paramArrayOfParamTag.length > 0) {
/* 200 */       content.addContent(
/* 201 */           processParamTags(paramBoolean, paramArrayOfParamTag, 
/* 202 */             getRankMap(paramArrayOfObject), paramTagletWriter, hashSet));
/*     */     }
/*     */     
/* 205 */     if (hashSet.size() != paramArrayOfObject.length)
/*     */     {
/*     */       
/* 208 */       content.addContent(getInheritedTagletOutput(paramBoolean, paramDoc, paramTagletWriter, paramArrayOfObject, hashSet));
/*     */     }
/*     */     
/* 211 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content getInheritedTagletOutput(boolean paramBoolean, Doc paramDoc, TagletWriter paramTagletWriter, Object[] paramArrayOfObject, Set<String> paramSet) {
/* 221 */     Content content = paramTagletWriter.getOutputInstance();
/* 222 */     if (!paramSet.contains(null) && paramDoc instanceof MethodDoc)
/*     */     {
/* 224 */       for (byte b = 0; b < paramArrayOfObject.length; b++) {
/* 225 */         if (!paramSet.contains(String.valueOf(b))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 231 */           DocFinder.Output output = DocFinder.search(new DocFinder.Input((ProgramElementDoc)paramDoc, this, 
/* 232 */                 String.valueOf(b), !paramBoolean));
/* 233 */           if (output.inlineTags != null && output.inlineTags.length > 0)
/*     */           {
/* 235 */             content.addContent(
/* 236 */                 processParamTag(paramBoolean, paramTagletWriter, (ParamTag)output.holderTag, paramBoolean ? ((Parameter)paramArrayOfObject[b])
/*     */ 
/*     */                   
/* 239 */                   .name() : ((TypeVariable)paramArrayOfObject[b])
/* 240 */                   .typeName(), 
/* 241 */                   (paramSet.size() == 0)));
/*     */           }
/* 243 */           paramSet.add(String.valueOf(b));
/*     */         } 
/*     */       }  } 
/* 246 */     return content;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content processParamTags(boolean paramBoolean, ParamTag[] paramArrayOfParamTag, Map<String, String> paramMap, TagletWriter paramTagletWriter, Set<String> paramSet) {
/* 270 */     Content content = paramTagletWriter.getOutputInstance();
/* 271 */     if (paramArrayOfParamTag.length > 0) {
/* 272 */       for (byte b = 0; b < paramArrayOfParamTag.length; b++) {
/* 273 */         ParamTag paramTag = paramArrayOfParamTag[b];
/*     */         
/* 275 */         String str1 = paramBoolean ? paramTag.parameterName() : ("<" + paramTag.parameterName() + ">");
/* 276 */         if (!paramMap.containsKey(paramTag.parameterName())) {
/* 277 */           paramTagletWriter.getMsgRetriever().warning(paramTag.position(), paramBoolean ? "doclet.Parameters_warn" : "doclet.Type_Parameters_warn", new Object[] { str1 });
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 283 */         String str2 = paramMap.get(paramTag.parameterName());
/* 284 */         if (str2 != null && paramSet.contains(str2)) {
/* 285 */           paramTagletWriter.getMsgRetriever().warning(paramTag.position(), paramBoolean ? "doclet.Parameters_dup_warn" : "doclet.Type_Parameters_dup_warn", new Object[] { str1 });
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 291 */         content.addContent(processParamTag(paramBoolean, paramTagletWriter, paramTag, paramTag
/* 292 */               .parameterName(), (paramSet.size() == 0)));
/* 293 */         paramSet.add(str2);
/*     */       } 
/*     */     }
/* 296 */     return content;
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
/*     */   
/*     */   private Content processParamTag(boolean paramBoolean1, TagletWriter paramTagletWriter, ParamTag paramParamTag, String paramString, boolean paramBoolean2) {
/* 314 */     Content content = paramTagletWriter.getOutputInstance();
/* 315 */     String str = paramTagletWriter.configuration().getText(paramBoolean1 ? "doclet.Parameters" : "doclet.TypeParameters");
/*     */     
/* 317 */     if (paramBoolean2) {
/* 318 */       content.addContent(paramTagletWriter.getParamHeader(str));
/*     */     }
/* 320 */     content.addContent(paramTagletWriter.paramTagOutput(paramParamTag, paramString));
/*     */     
/* 322 */     return content;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\ParamTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */