/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Comment
/*     */ {
/*  53 */   private final ListBuffer<Tag> tagList = new ListBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String text;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final DocEnv docenv;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Comment(final DocImpl holder, final String commentString) {
/*  69 */     this.docenv = holder.env;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     class CommentStringParser
/*     */     {
/*     */       void parseCommentStateMachine()
/*     */       {
/*  90 */         byte b1 = 2;
/*  91 */         boolean bool = true;
/*  92 */         String str = null;
/*  93 */         byte b2 = 0;
/*  94 */         int i = 0;
/*  95 */         byte b = -1;
/*  96 */         int j = commentString.length();
/*  97 */         for (byte b3 = 0; b3 < j; b3++) {
/*  98 */           char c = commentString.charAt(b3);
/*  99 */           boolean bool1 = Character.isWhitespace(c);
/* 100 */           switch (b1) {
/*     */             case 3:
/* 102 */               if (bool1) {
/* 103 */                 str = commentString.substring(b2, b3);
/* 104 */                 b1 = 2;
/*     */               } 
/*     */               break;
/*     */             case 2:
/* 108 */               if (bool1) {
/*     */                 break;
/*     */               }
/* 111 */               i = b3;
/* 112 */               b1 = 1;
/*     */             
/*     */             case 1:
/* 115 */               if (bool && c == '@') {
/* 116 */                 parseCommentComponent(str, i, b + 1);
/*     */                 
/* 118 */                 b2 = b3;
/* 119 */                 b1 = 3;
/*     */               } 
/*     */               break;
/*     */           } 
/* 123 */           if (c == '\n') {
/* 124 */             bool = true;
/* 125 */           } else if (!bool1) {
/* 126 */             b = b3;
/* 127 */             bool = false;
/*     */           } 
/*     */         } 
/*     */         
/* 131 */         switch (b1) {
/*     */           case 3:
/* 133 */             str = commentString.substring(b2, j);
/*     */           
/*     */           case 2:
/* 136 */             i = j;
/*     */           
/*     */           case 1:
/* 139 */             parseCommentComponent(str, i, b + 1);
/*     */             break;
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       void parseCommentComponent(String param1String, int param1Int1, int param1Int2) {
/* 149 */         String str = (param1Int2 <= param1Int1) ? "" : commentString.substring(param1Int1, param1Int2);
/* 150 */         if (param1String == null) {
/* 151 */           Comment.this.text = str;
/*     */         } else {
/*     */           TagImpl tagImpl;
/* 154 */           if (param1String.equals("@exception") || param1String.equals("@throws")) {
/* 155 */             warnIfEmpty(param1String, str);
/* 156 */             tagImpl = new ThrowsTagImpl(holder, param1String, str);
/* 157 */           } else if (param1String.equals("@param")) {
/* 158 */             warnIfEmpty(param1String, str);
/* 159 */             tagImpl = new ParamTagImpl(holder, param1String, str);
/* 160 */           } else if (param1String.equals("@see")) {
/* 161 */             warnIfEmpty(param1String, str);
/* 162 */             tagImpl = new SeeTagImpl(holder, param1String, str);
/* 163 */           } else if (param1String.equals("@serialField")) {
/* 164 */             warnIfEmpty(param1String, str);
/* 165 */             tagImpl = new SerialFieldTagImpl(holder, param1String, str);
/* 166 */           } else if (param1String.equals("@return")) {
/* 167 */             warnIfEmpty(param1String, str);
/* 168 */             tagImpl = new TagImpl(holder, param1String, str);
/* 169 */           } else if (param1String.equals("@author")) {
/* 170 */             warnIfEmpty(param1String, str);
/* 171 */             tagImpl = new TagImpl(holder, param1String, str);
/* 172 */           } else if (param1String.equals("@version")) {
/* 173 */             warnIfEmpty(param1String, str);
/* 174 */             tagImpl = new TagImpl(holder, param1String, str);
/*     */           } else {
/* 176 */             tagImpl = new TagImpl(holder, param1String, str);
/*     */           } 
/* 178 */           Comment.this.tagList.append(tagImpl);
/*     */         } 
/*     */       }
/*     */       
/*     */       void warnIfEmpty(String param1String1, String param1String2) {
/* 183 */         if (param1String2.length() == 0) {
/* 184 */           Comment.this.docenv.warning(holder, "tag.tag_has_no_arguments", param1String1);
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */     
/* 190 */     (new CommentStringParser()).parseCommentStateMachine();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String commentText() {
/* 197 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Tag[] tags() {
/* 204 */     return (Tag[])this.tagList.toArray((Object[])new Tag[this.tagList.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Tag[] tags(String paramString) {
/* 211 */     ListBuffer listBuffer = new ListBuffer();
/* 212 */     String str = paramString;
/* 213 */     if (str.charAt(0) != '@') {
/* 214 */       str = "@" + str;
/*     */     }
/* 216 */     for (Tag tag : this.tagList) {
/* 217 */       if (tag.kind().equals(str)) {
/* 218 */         listBuffer.append(tag);
/*     */       }
/*     */     } 
/* 221 */     return (Tag[])listBuffer.toArray((Object[])new Tag[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ThrowsTag[] throwsTags() {
/* 228 */     ListBuffer listBuffer = new ListBuffer();
/* 229 */     for (Tag tag : this.tagList) {
/* 230 */       if (tag instanceof ThrowsTag) {
/* 231 */         listBuffer.append(tag);
/*     */       }
/*     */     } 
/* 234 */     return (ThrowsTag[])listBuffer.toArray((Object[])new ThrowsTag[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ParamTag[] paramTags() {
/* 241 */     return paramTags(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ParamTag[] typeParamTags() {
/* 248 */     return paramTags(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParamTag[] paramTags(boolean paramBoolean) {
/* 257 */     ListBuffer listBuffer = new ListBuffer();
/* 258 */     for (Tag tag : this.tagList) {
/* 259 */       if (tag instanceof ParamTag) {
/* 260 */         ParamTag paramTag = (ParamTag)tag;
/* 261 */         if (paramBoolean == paramTag.isTypeParameter()) {
/* 262 */           listBuffer.append(paramTag);
/*     */         }
/*     */       } 
/*     */     } 
/* 266 */     return (ParamTag[])listBuffer.toArray((Object[])new ParamTag[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SeeTag[] seeTags() {
/* 273 */     ListBuffer listBuffer = new ListBuffer();
/* 274 */     for (Tag tag : this.tagList) {
/* 275 */       if (tag instanceof SeeTag) {
/* 276 */         listBuffer.append(tag);
/*     */       }
/*     */     } 
/* 279 */     return (SeeTag[])listBuffer.toArray((Object[])new SeeTag[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SerialFieldTag[] serialFieldTags() {
/* 286 */     ListBuffer listBuffer = new ListBuffer();
/* 287 */     for (Tag tag : this.tagList) {
/* 288 */       if (tag instanceof SerialFieldTag) {
/* 289 */         listBuffer.append(tag);
/*     */       }
/*     */     } 
/* 292 */     return (SerialFieldTag[])listBuffer.toArray((Object[])new SerialFieldTag[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Tag[] getInlineTags(DocImpl paramDocImpl, String paramString) {
/* 299 */     ListBuffer listBuffer = new ListBuffer();
/* 300 */     int i = 0, j = 0, k = paramString.length();
/* 301 */     boolean bool = false;
/* 302 */     DocEnv docEnv = paramDocImpl.env;
/*     */     
/* 304 */     if (k == 0) {
/* 305 */       return (Tag[])listBuffer.toArray((Object[])new Tag[listBuffer.length()]);
/*     */     }
/*     */     do {
/*     */       int m;
/* 309 */       if ((m = inlineTagFound(paramDocImpl, paramString, j)) == -1) {
/*     */         
/* 311 */         listBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 312 */               .substring(j)));
/*     */         break;
/*     */       } 
/* 315 */       bool = scanForPre(paramString, j, m, bool);
/* 316 */       int n = m;
/* 317 */       for (int i1 = m; i1 < paramString.length(); i1++) {
/* 318 */         char c = paramString.charAt(i1);
/* 319 */         if (Character.isWhitespace(c) || c == '}') {
/*     */           
/* 321 */           n = i1;
/*     */           break;
/*     */         } 
/*     */       } 
/* 325 */       String str = paramString.substring(m + 2, n);
/* 326 */       if (!bool || (!str.equals("code") && !str.equals("literal")))
/*     */       {
/* 328 */         while (Character.isWhitespace(paramString
/* 329 */             .charAt(n))) {
/* 330 */           if (paramString.length() <= n) {
/* 331 */             listBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 332 */                   .substring(j, n)));
/* 333 */             docEnv.warning(paramDocImpl, "tag.Improper_Use_Of_Link_Tag", paramString);
/*     */ 
/*     */             
/* 336 */             return (Tag[])listBuffer.toArray((Object[])new Tag[listBuffer.length()]);
/*     */           } 
/* 338 */           n++;
/*     */         } 
/*     */       }
/*     */       
/* 342 */       listBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 343 */             .substring(j, m)));
/* 344 */       j = n;
/* 345 */       if ((i = findInlineTagDelim(paramString, j)) == -1) {
/*     */ 
/*     */         
/* 348 */         listBuffer.append(new TagImpl(paramDocImpl, "Text", paramString
/* 349 */               .substring(j)));
/* 350 */         docEnv.warning(paramDocImpl, "tag.End_delimiter_missing_for_possible_SeeTag", paramString);
/*     */ 
/*     */         
/* 353 */         return (Tag[])listBuffer.toArray((Object[])new Tag[listBuffer.length()]);
/*     */       } 
/*     */       
/* 356 */       if (str.equals("see") || str
/* 357 */         .equals("link") || str
/* 358 */         .equals("linkplain")) {
/* 359 */         listBuffer.append(new SeeTagImpl(paramDocImpl, "@" + str, paramString
/* 360 */               .substring(j, i)));
/*     */       } else {
/* 362 */         listBuffer.append(new TagImpl(paramDocImpl, "@" + str, paramString
/* 363 */               .substring(j, i)));
/*     */       } 
/* 365 */       j = i + 1;
/*     */     
/*     */     }
/* 368 */     while (j != paramString.length());
/*     */ 
/*     */ 
/*     */     
/* 372 */     return (Tag[])listBuffer.toArray((Object[])new Tag[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */   
/* 376 */   private static final Pattern prePat = Pattern.compile("(?i)<(/?)pre>");
/*     */   
/*     */   private static boolean scanForPre(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 379 */     Matcher matcher = prePat.matcher(paramString).region(paramInt1, paramInt2);
/* 380 */     while (matcher.find()) {
/* 381 */       paramBoolean = matcher.group(1).isEmpty();
/*     */     }
/* 383 */     return paramBoolean;
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
/*     */   private static int findInlineTagDelim(String paramString, int paramInt) {
/*     */     int i;
/* 396 */     if ((i = paramString.indexOf("}", paramInt)) == -1)
/* 397 */       return -1;  int j;
/* 398 */     if ((j = paramString.indexOf("{", paramInt)) != -1 && j < i) {
/*     */ 
/*     */       
/* 401 */       int k = findInlineTagDelim(paramString, j + 1);
/* 402 */       return (k != -1) ? 
/* 403 */         findInlineTagDelim(paramString, k + 1) : -1;
/*     */     } 
/*     */     
/* 406 */     return i;
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
/*     */   private static int inlineTagFound(DocImpl paramDocImpl, String paramString, int paramInt) {
/* 419 */     DocEnv docEnv = paramDocImpl.env;
/* 420 */     int i = paramString.indexOf("{@", paramInt);
/* 421 */     if (paramInt == paramString.length() || i == -1)
/* 422 */       return -1; 
/* 423 */     if (paramString.indexOf('}', i) == -1) {
/*     */       
/* 425 */       docEnv.warning(paramDocImpl, "tag.Improper_Use_Of_Link_Tag", paramString
/* 426 */           .substring(i, paramString.length()));
/* 427 */       return -1;
/*     */     } 
/* 429 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Tag[] firstSentenceTags(DocImpl paramDocImpl, String paramString) {
/* 438 */     DocLocale docLocale = paramDocImpl.env.doclocale;
/* 439 */     return getInlineTags(paramDocImpl, docLocale
/* 440 */         .localeSpecificFirstSentence(paramDocImpl, paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 448 */     return this.text;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\Comment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */