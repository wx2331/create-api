/*     */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValueTaglet
/*     */   extends BaseInlineTaglet
/*     */ {
/*     */   public boolean inMethod() {
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inConstructor() {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inOverview() {
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inPackage() {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inType() {
/* 104 */     return true;
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
/*     */   private FieldDoc getFieldDoc(Configuration paramConfiguration, Tag paramTag, String paramString) {
/* 123 */     if (paramString == null || paramString.length() == 0) {
/*     */       
/* 125 */       if (paramTag.holder() instanceof FieldDoc) {
/* 126 */         return (FieldDoc)paramTag.holder();
/*     */       }
/*     */ 
/*     */       
/* 130 */       return null;
/*     */     } 
/*     */     
/* 133 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, "#");
/* 134 */     String str = null;
/* 135 */     ClassDoc classDoc = null;
/* 136 */     if (stringTokenizer.countTokens() == 1) {
/*     */       
/* 138 */       Doc doc = paramTag.holder();
/* 139 */       if (doc instanceof MemberDoc) {
/* 140 */         classDoc = ((MemberDoc)doc).containingClass();
/* 141 */       } else if (doc instanceof ClassDoc) {
/* 142 */         classDoc = (ClassDoc)doc;
/*     */       } 
/* 144 */       str = stringTokenizer.nextToken();
/*     */     } else {
/*     */       
/* 147 */       classDoc = paramConfiguration.root.classNamed(stringTokenizer.nextToken());
/* 148 */       str = stringTokenizer.nextToken();
/*     */     } 
/* 150 */     if (classDoc == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     FieldDoc[] arrayOfFieldDoc = classDoc.fields();
/* 154 */     for (byte b = 0; b < arrayOfFieldDoc.length; b++) {
/* 155 */       if (arrayOfFieldDoc[b].name().equals(str)) {
/* 156 */         return arrayOfFieldDoc[b];
/*     */       }
/*     */     } 
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter) {
/* 166 */     FieldDoc fieldDoc = getFieldDoc(paramTagletWriter
/* 167 */         .configuration(), paramTag, paramTag.text());
/* 168 */     if (fieldDoc == null)
/* 169 */     { if (paramTag.text().isEmpty()) {
/*     */         
/* 171 */         paramTagletWriter.getMsgRetriever().warning(paramTag.holder().position(), "doclet.value_tag_invalid_use", new Object[0]);
/*     */       }
/*     */       else {
/*     */         
/* 175 */         paramTagletWriter.getMsgRetriever().warning(paramTag.holder().position(), "doclet.value_tag_invalid_reference", new Object[] { paramTag
/* 176 */               .text() });
/*     */       }  }
/* 178 */     else { if (fieldDoc.constantValue() != null) {
/* 179 */         return paramTagletWriter.valueTagOutput(fieldDoc, fieldDoc
/* 180 */             .constantValueExpression(), 
/* 181 */             !fieldDoc.equals(paramTag.holder()));
/*     */       }
/*     */       
/* 184 */       paramTagletWriter.getMsgRetriever().warning(paramTag.holder().position(), "doclet.value_tag_invalid_constant", new Object[] { fieldDoc
/* 185 */             .name() }); }
/*     */     
/* 187 */     return paramTagletWriter.getOutputInstance();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\ValueTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */