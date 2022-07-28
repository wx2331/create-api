/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.SeeTag;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.FatalError;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.CollationKey;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.tools.FileObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DocImpl
/*     */   implements Doc, Comparable<Object>
/*     */ {
/*     */   protected final DocEnv env;
/*     */   protected TreePath treePath;
/*     */   private Comment comment;
/*  81 */   private CollationKey collationkey = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String documentation;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Tag[] firstSentence;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Tag[] inlineTags;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DocImpl(DocEnv paramDocEnv, TreePath paramTreePath) {
/* 102 */     this.treePath = paramTreePath;
/* 103 */     this.documentation = getCommentText(paramTreePath);
/* 104 */     this.env = paramDocEnv;
/*     */   }
/*     */   
/*     */   private static String getCommentText(TreePath paramTreePath) {
/* 108 */     if (paramTreePath == null) {
/* 109 */       return null;
/*     */     }
/* 111 */     JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit();
/* 112 */     JCTree jCTree = (JCTree)paramTreePath.getLeaf();
/* 113 */     return jCCompilationUnit.docComments.getCommentText(jCTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String documentation() {
/* 121 */     if (this.documentation == null) this.documentation = ""; 
/* 122 */     return this.documentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Comment comment() {
/* 129 */     if (this.comment == null) {
/* 130 */       String str = documentation();
/* 131 */       if (this.env.javaScriptScanner != null) {
/* 132 */         this.env.javaScriptScanner.parse(str, new JavaScriptScanner.Reporter()
/*     */             {
/*     */               public void report() {
/* 135 */                 DocImpl.this.env.error(DocImpl.this, "javadoc.JavaScript_in_comment");
/* 136 */                 throw new FatalError();
/*     */               }
/*     */             });
/*     */       }
/* 140 */       if (this.env.doclint != null && this.treePath != null && str
/*     */         
/* 142 */         .equals(getCommentText(this.treePath))) {
/* 143 */         this.env.doclint.scan(this.treePath);
/*     */       }
/* 145 */       this.comment = new Comment(this, str);
/*     */     } 
/* 147 */     return this.comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String commentText() {
/* 155 */     return comment().commentText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag[] tags() {
/* 164 */     return comment().tags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag[] tags(String paramString) {
/* 175 */     return comment().tags(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SeeTag[] seeTags() {
/* 184 */     return comment().seeTags();
/*     */   }
/*     */   
/*     */   public Tag[] inlineTags() {
/* 188 */     if (this.inlineTags == null) {
/* 189 */       this.inlineTags = Comment.getInlineTags(this, commentText());
/*     */     }
/* 191 */     return this.inlineTags;
/*     */   }
/*     */   
/*     */   public Tag[] firstSentenceTags() {
/* 195 */     if (this.firstSentence == null) {
/*     */       
/* 197 */       inlineTags();
/*     */       try {
/* 199 */         this.env.setSilent(true);
/* 200 */         this.firstSentence = Comment.firstSentenceTags(this, commentText());
/*     */       } finally {
/* 202 */         this.env.setSilent(false);
/*     */       } 
/*     */     } 
/* 205 */     return this.firstSentence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String readHTMLDocumentation(InputStream paramInputStream, FileObject paramFileObject) throws IOException {
/* 212 */     byte[] arrayOfByte = new byte[paramInputStream.available()];
/*     */     try {
/* 214 */       DataInputStream dataInputStream = new DataInputStream(paramInputStream);
/* 215 */       dataInputStream.readFully(arrayOfByte);
/*     */     } finally {
/* 217 */       paramInputStream.close();
/*     */     } 
/* 219 */     String str1 = this.env.getEncoding();
/* 220 */     String str2 = (str1 != null) ? new String(arrayOfByte, str1) : new String(arrayOfByte);
/*     */ 
/*     */     
/* 223 */     Pattern pattern = Pattern.compile("(?is).*<body\\b[^>]*>(.*)</body\\b.*");
/* 224 */     Matcher matcher = pattern.matcher(str2);
/* 225 */     if (matcher.matches()) {
/* 226 */       return matcher.group(1);
/*     */     }
/* 228 */     String str3 = str2.matches("(?is).*<body\\b.*") ? "javadoc.End_body_missing_from_html_file" : "javadoc.Body_missing_from_html_file";
/*     */ 
/*     */     
/* 231 */     this.env.error(SourcePositionImpl.make(paramFileObject, -1, null), str3);
/* 232 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRawCommentText() {
/* 242 */     return documentation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRawCommentText(String paramString) {
/* 251 */     this.treePath = null;
/* 252 */     this.documentation = paramString;
/* 253 */     this.comment = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setTreePath(TreePath paramTreePath) {
/* 260 */     this.treePath = paramTreePath;
/* 261 */     this.documentation = getCommentText(paramTreePath);
/* 262 */     this.comment = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CollationKey key() {
/* 269 */     if (this.collationkey == null) {
/* 270 */       this.collationkey = generateKey();
/*     */     }
/* 272 */     return this.collationkey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CollationKey generateKey() {
/* 281 */     String str = name();
/*     */     
/* 283 */     return this.env.doclocale.collator.getCollationKey(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 291 */     return qualifiedName();
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
/*     */   public abstract String name();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String qualifiedName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Object paramObject) {
/* 323 */     return key().compareTo(((DocImpl)paramObject).key());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isField() {
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnumConstant() {
/* 341 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 350 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMethod() {
/* 361 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnnotationTypeElement() {
/* 371 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 381 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isException() {
/* 390 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isError() {
/* 399 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnum() {
/* 408 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnnotationType() {
/* 417 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOrdinaryClass() {
/* 428 */     return false;
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
/*     */   public boolean isClass() {
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isIncluded();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourcePosition position() {
/* 452 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\DocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */