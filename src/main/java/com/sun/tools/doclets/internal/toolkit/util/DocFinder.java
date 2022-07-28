/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.InheritableTaglet;
/*     */ import java.util.ArrayList;
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
/*     */ public class DocFinder
/*     */ {
/*     */   public static class Input
/*     */   {
/*     */     public ProgramElementDoc element;
/*  58 */     public InheritableTaglet taglet = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     public String tagId = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     public Tag tag = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isFirstSentence = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isInheritDocTag = false;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isTypeVariableParamTag = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Input(ProgramElementDoc param1ProgramElementDoc, InheritableTaglet param1InheritableTaglet, Tag param1Tag, boolean param1Boolean1, boolean param1Boolean2) {
/*  89 */       this(param1ProgramElementDoc);
/*  90 */       this.taglet = param1InheritableTaglet;
/*  91 */       this.tag = param1Tag;
/*  92 */       this.isFirstSentence = param1Boolean1;
/*  93 */       this.isInheritDocTag = param1Boolean2;
/*     */     }
/*     */     
/*     */     public Input(ProgramElementDoc param1ProgramElementDoc, InheritableTaglet param1InheritableTaglet, String param1String) {
/*  97 */       this(param1ProgramElementDoc);
/*  98 */       this.taglet = param1InheritableTaglet;
/*  99 */       this.tagId = param1String;
/*     */     }
/*     */ 
/*     */     
/*     */     public Input(ProgramElementDoc param1ProgramElementDoc, InheritableTaglet param1InheritableTaglet, String param1String, boolean param1Boolean) {
/* 104 */       this(param1ProgramElementDoc);
/* 105 */       this.taglet = param1InheritableTaglet;
/* 106 */       this.tagId = param1String;
/* 107 */       this.isTypeVariableParamTag = param1Boolean;
/*     */     }
/*     */     
/*     */     public Input(ProgramElementDoc param1ProgramElementDoc, InheritableTaglet param1InheritableTaglet) {
/* 111 */       this(param1ProgramElementDoc);
/* 112 */       this.taglet = param1InheritableTaglet;
/*     */     }
/*     */     
/*     */     public Input(ProgramElementDoc param1ProgramElementDoc) {
/* 116 */       if (param1ProgramElementDoc == null)
/* 117 */         throw new NullPointerException(); 
/* 118 */       this.element = param1ProgramElementDoc;
/*     */     }
/*     */     
/*     */     public Input(ProgramElementDoc param1ProgramElementDoc, boolean param1Boolean) {
/* 122 */       this(param1ProgramElementDoc);
/* 123 */       this.isFirstSentence = param1Boolean;
/*     */     }
/*     */     
/*     */     public Input copy() {
/* 127 */       Input input = new Input(this.element);
/* 128 */       input.taglet = this.taglet;
/* 129 */       input.tagId = this.tagId;
/* 130 */       input.tag = this.tag;
/* 131 */       input.isFirstSentence = this.isFirstSentence;
/* 132 */       input.isInheritDocTag = this.isInheritDocTag;
/* 133 */       input.isTypeVariableParamTag = this.isTypeVariableParamTag;
/* 134 */       if (input.element == null)
/* 135 */         throw new NullPointerException(); 
/* 136 */       return input;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Output
/*     */   {
/*     */     public Tag holderTag;
/*     */ 
/*     */     
/*     */     public Doc holder;
/*     */ 
/*     */     
/*     */     public Tag[] inlineTags;
/*     */ 
/*     */     
/*     */     public boolean isValidInheritDocTag;
/*     */     
/*     */     public List<Tag> tagList;
/*     */ 
/*     */     
/*     */     public Output() {
/* 159 */       this.inlineTags = new Tag[0];
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 164 */       this.isValidInheritDocTag = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 174 */       this.tagList = new ArrayList<>();
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
/*     */   public static Output search(Input paramInput) {
/* 188 */     Output output = new Output();
/* 189 */     if (!paramInput.isInheritDocTag)
/*     */     {
/*     */       
/* 192 */       if (paramInput.taglet == null) {
/*     */         
/* 194 */         output
/*     */           
/* 196 */           .inlineTags = paramInput.isFirstSentence ? paramInput.element.firstSentenceTags() : paramInput.element.inlineTags();
/* 197 */         output.holder = (Doc)paramInput.element;
/*     */       } else {
/* 199 */         paramInput.taglet.inherit(paramInput, output);
/*     */       } 
/*     */     }
/* 202 */     if (output.inlineTags != null && output.inlineTags.length > 0) {
/* 203 */       return output;
/*     */     }
/* 205 */     output.isValidInheritDocTag = false;
/* 206 */     Input input = paramInput.copy();
/* 207 */     input.isInheritDocTag = false;
/* 208 */     if (paramInput.element instanceof MethodDoc) {
/* 209 */       MethodDoc methodDoc = ((MethodDoc)paramInput.element).overriddenMethod();
/* 210 */       if (methodDoc != null) {
/* 211 */         input.element = (ProgramElementDoc)methodDoc;
/* 212 */         output = search(input);
/* 213 */         output.isValidInheritDocTag = true;
/* 214 */         if (output.inlineTags.length > 0) {
/* 215 */           return output;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 222 */       MethodDoc[] arrayOfMethodDoc = (new ImplementedMethods((MethodDoc)paramInput.element, null)).build(false);
/* 223 */       for (byte b = 0; b < arrayOfMethodDoc.length; b++) {
/* 224 */         input.element = (ProgramElementDoc)arrayOfMethodDoc[b];
/* 225 */         output = search(input);
/* 226 */         output.isValidInheritDocTag = true;
/* 227 */         if (output.inlineTags.length > 0) {
/* 228 */           return output;
/*     */         }
/*     */       } 
/* 231 */     } else if (paramInput.element instanceof ClassDoc) {
/* 232 */       ClassDoc classDoc = ((ClassDoc)paramInput.element).superclass();
/* 233 */       if (classDoc != null) {
/* 234 */         input.element = (ProgramElementDoc)classDoc;
/* 235 */         output = search(input);
/* 236 */         output.isValidInheritDocTag = true;
/* 237 */         if (output.inlineTags.length > 0) {
/* 238 */           return output;
/*     */         }
/*     */       } 
/*     */     } 
/* 242 */     return output;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\DocFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */