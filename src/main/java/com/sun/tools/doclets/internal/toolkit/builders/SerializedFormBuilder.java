/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class SerializedFormBuilder
/*     */   extends AbstractBuilder
/*     */ {
/*     */   public static final String NAME = "SerializedForm";
/*     */   private SerializedFormWriter writer;
/*     */   private SerializedFormWriter.SerialFieldWriter fieldWriter;
/*     */   private SerializedFormWriter.SerialMethodWriter methodWriter;
/*     */   private static final String SERIAL_VERSION_UID_HEADER = "serialVersionUID:";
/*     */   private PackageDoc currentPackage;
/*     */   private ClassDoc currentClass;
/*     */   protected MemberDoc currentMember;
/*     */   private Content contentTree;
/*     */
/*     */   private SerializedFormBuilder(Context paramContext) {
/* 103 */     super(paramContext);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static SerializedFormBuilder getInstance(Context paramContext) {
/* 111 */     return new SerializedFormBuilder(paramContext);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void build() throws IOException {
/* 118 */     if (!serialClassFoundToDocument(this.configuration.root.classes())) {
/*     */       return;
/*     */     }
/*     */
/*     */     try {
/* 123 */       this.writer = this.configuration.getWriterFactory().getSerializedFormWriter();
/* 124 */       if (this.writer == null) {
/*     */         return;
/*     */       }
/*     */     }
/* 128 */     catch (Exception exception) {
/* 129 */       throw new DocletAbortException(exception);
/*     */     }
/* 131 */     build(this.layoutParser.parseXML("SerializedForm"), this.contentTree);
/* 132 */     this.writer.close();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 139 */     return "SerializedForm";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSerializedForm(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 149 */     paramContent = this.writer.getHeader(this.configuration.getText("doclet.Serialized_Form"));
/*     */
/* 151 */     buildChildren(paramXMLNode, paramContent);
/* 152 */     this.writer.addFooter(paramContent);
/* 153 */     this.writer.printDocument(paramContent);
/* 154 */     this.writer.close();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSerializedFormSummaries(XMLNode paramXMLNode, Content paramContent) {
/* 164 */     Content content = this.writer.getSerializedSummariesHeader();
/* 165 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 166 */     for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/* 167 */       this.currentPackage = arrayOfPackageDoc[b];
/* 168 */       buildChildren(paramXMLNode, content);
/*     */     }
/* 170 */     paramContent.addContent(this.writer.getSerializedContent(content));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageSerializedForm(XMLNode paramXMLNode, Content paramContent) {
/* 181 */     Content content = this.writer.getPackageSerializedHeader();
/* 182 */     String str = this.currentPackage.name();
/* 183 */     ClassDoc[] arrayOfClassDoc = this.currentPackage.allClasses(false);
/* 184 */     if (arrayOfClassDoc == null || arrayOfClassDoc.length == 0) {
/*     */       return;
/*     */     }
/* 187 */     if (!serialInclude((Doc)this.currentPackage)) {
/*     */       return;
/*     */     }
/* 190 */     if (!serialClassFoundToDocument(arrayOfClassDoc)) {
/*     */       return;
/*     */     }
/* 193 */     buildChildren(paramXMLNode, content);
/* 194 */     paramContent.addContent(content);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageHeader(XMLNode paramXMLNode, Content paramContent) {
/* 204 */     paramContent.addContent(this.writer.getPackageHeader(
/* 205 */           Util.getPackageName(this.currentPackage)));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildClassSerializedForm(XMLNode paramXMLNode, Content paramContent) {
/* 215 */     Content content = this.writer.getClassSerializedHeader();
/* 216 */     ClassDoc[] arrayOfClassDoc = this.currentPackage.allClasses(false);
/* 217 */     Arrays.sort((Object[])arrayOfClassDoc);
/* 218 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 219 */       this.currentClass = arrayOfClassDoc[b];
/* 220 */       this.fieldWriter = this.writer.getSerialFieldWriter(this.currentClass);
/* 221 */       this.methodWriter = this.writer.getSerialMethodWriter(this.currentClass);
/* 222 */       if (this.currentClass.isClass() && this.currentClass.isSerializable() &&
/* 223 */         serialClassInclude(this.currentClass)) {
/*     */
/*     */
/* 226 */         Content content1 = this.writer.getClassHeader(this.currentClass);
/* 227 */         buildChildren(paramXMLNode, content1);
/* 228 */         content.addContent(content1);
/*     */       }
/*     */     }
/* 231 */     paramContent.addContent(content);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSerialUIDInfo(XMLNode paramXMLNode, Content paramContent) {
/* 241 */     Content content = this.writer.getSerialUIDInfoHeader();
/* 242 */     FieldDoc[] arrayOfFieldDoc = this.currentClass.fields(false);
/* 243 */     for (byte b = 0; b < arrayOfFieldDoc.length; b++) {
/* 244 */       if (arrayOfFieldDoc[b].name().equals("serialVersionUID") && arrayOfFieldDoc[b]
/* 245 */         .constantValueExpression() != null) {
/* 246 */         this.writer.addSerialUIDInfo("serialVersionUID:", arrayOfFieldDoc[b]
/* 247 */             .constantValueExpression(), content);
/*     */         break;
/*     */       }
/*     */     }
/* 251 */     paramContent.addContent(content);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildClassContent(XMLNode paramXMLNode, Content paramContent) {
/* 261 */     Content content = this.writer.getClassContentHeader();
/* 262 */     buildChildren(paramXMLNode, content);
/* 263 */     paramContent.addContent(content);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSerializableMethods(XMLNode paramXMLNode, Content paramContent) {
/* 274 */     Content content = this.methodWriter.getSerializableMethodsHeader();
/* 275 */     MethodDoc[] arrayOfMethodDoc = this.currentClass.serializationMethods();
/* 276 */     int i = arrayOfMethodDoc.length;
/* 277 */     if (i > 0) {
/* 278 */       for (byte b = 0; b < i; b++) {
/* 279 */         this.currentMember = (MemberDoc)arrayOfMethodDoc[b];
/* 280 */         Content content1 = this.methodWriter.getMethodsContentHeader((b == i - 1));
/*     */
/* 282 */         buildChildren(paramXMLNode, content1);
/* 283 */         content.addContent(content1);
/*     */       }
/*     */     }
/* 286 */     if ((this.currentClass.serializationMethods()).length > 0) {
/* 287 */       paramContent.addContent(this.methodWriter.getSerializableMethods(this.configuration
/* 288 */             .getText("doclet.Serialized_Form_methods"), content));
/*     */
/* 290 */       if (this.currentClass.isSerializable() && !this.currentClass.isExternalizable() && (
/* 291 */         this.currentClass.serializationMethods()).length == 0) {
/* 292 */         Content content1 = this.methodWriter.getNoCustomizationMsg(this.configuration
/* 293 */             .getText("doclet.Serializable_no_customization"));
/*     */
/* 295 */         paramContent.addContent(this.methodWriter.getSerializableMethods(this.configuration
/* 296 */               .getText("doclet.Serialized_Form_methods"), content1));
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
/*     */
/*     */
/*     */   public void buildMethodSubHeader(XMLNode paramXMLNode, Content paramContent) {
/* 310 */     this.methodWriter.addMemberHeader((MethodDoc)this.currentMember, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildDeprecatedMethodInfo(XMLNode paramXMLNode, Content paramContent) {
/* 320 */     this.methodWriter.addDeprecatedMemberInfo((MethodDoc)this.currentMember, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildMethodInfo(XMLNode paramXMLNode, Content paramContent) {
/* 330 */     if (this.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 333 */     buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildMethodDescription(XMLNode paramXMLNode, Content paramContent) {
/* 343 */     this.methodWriter.addMemberDescription((MethodDoc)this.currentMember, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildMethodTags(XMLNode paramXMLNode, Content paramContent) {
/* 353 */     this.methodWriter.addMemberTags((MethodDoc)this.currentMember, paramContent);
/* 354 */     MethodDoc methodDoc = (MethodDoc)this.currentMember;
/* 355 */     if (methodDoc.name().compareTo("writeExternal") == 0 && (methodDoc
/* 356 */       .tags("serialData")).length == 0 &&
/* 357 */       this.configuration.serialwarn) {
/* 358 */       this.configuration.getDocletSpecificMsg().warning(this.currentMember
/* 359 */           .position(), "doclet.MissingSerialDataTag", new Object[] { methodDoc
/* 360 */             .containingClass().qualifiedName(), methodDoc.name() });
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
/*     */   public void buildFieldHeader(XMLNode paramXMLNode, Content paramContent) {
/* 372 */     if ((this.currentClass.serializableFields()).length > 0) {
/* 373 */       buildFieldSerializationOverview(this.currentClass, paramContent);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildFieldSerializationOverview(ClassDoc paramClassDoc, Content paramContent) {
/* 384 */     if (paramClassDoc.definesSerializableFields()) {
/* 385 */       FieldDoc fieldDoc = paramClassDoc.serializableFields()[0];
/*     */
/*     */
/* 388 */       if (this.fieldWriter.shouldPrintOverview(fieldDoc)) {
/* 389 */         Content content1 = this.fieldWriter.getSerializableFieldsHeader();
/* 390 */         Content content2 = this.fieldWriter.getFieldsContentHeader(true);
/* 391 */         this.fieldWriter.addMemberDeprecatedInfo(fieldDoc, content2);
/*     */
/* 393 */         if (!this.configuration.nocomment) {
/* 394 */           this.fieldWriter.addMemberDescription(fieldDoc, content2);
/*     */
/* 396 */           this.fieldWriter.addMemberTags(fieldDoc, content2);
/*     */         }
/*     */
/* 399 */         content1.addContent(content2);
/* 400 */         paramContent.addContent(this.fieldWriter.getSerializableFields(this.configuration
/* 401 */               .getText("doclet.Serialized_Form_class"), content1));
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
/*     */
/*     */   public void buildSerializableFields(XMLNode paramXMLNode, Content paramContent) {
/* 414 */     FieldDoc[] arrayOfFieldDoc = this.currentClass.serializableFields();
/* 415 */     int i = arrayOfFieldDoc.length;
/* 416 */     if (i > 0) {
/* 417 */       Content content = this.fieldWriter.getSerializableFieldsHeader();
/* 418 */       for (byte b = 0; b < i; b++) {
/* 419 */         this.currentMember = (MemberDoc)arrayOfFieldDoc[b];
/* 420 */         if (!this.currentClass.definesSerializableFields()) {
/* 421 */           Content content1 = this.fieldWriter.getFieldsContentHeader((b == i - 1));
/*     */
/* 423 */           buildChildren(paramXMLNode, content1);
/* 424 */           content.addContent(content1);
/*     */         } else {
/*     */
/* 427 */           buildSerialFieldTagsInfo(content);
/*     */         }
/*     */       }
/* 430 */       paramContent.addContent(this.fieldWriter.getSerializableFields(this.configuration
/* 431 */             .getText("doclet.Serialized_Form_fields"), content));
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
/*     */   public void buildFieldSubHeader(XMLNode paramXMLNode, Content paramContent) {
/* 443 */     if (!this.currentClass.definesSerializableFields()) {
/* 444 */       FieldDoc fieldDoc = (FieldDoc)this.currentMember;
/* 445 */       this.fieldWriter.addMemberHeader(fieldDoc.type().asClassDoc(), fieldDoc
/* 446 */           .type().typeName(), fieldDoc.type().dimension(), fieldDoc.name(), paramContent);
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
/*     */   public void buildFieldDeprecationInfo(XMLNode paramXMLNode, Content paramContent) {
/* 458 */     if (!this.currentClass.definesSerializableFields()) {
/* 459 */       FieldDoc fieldDoc = (FieldDoc)this.currentMember;
/* 460 */       this.fieldWriter.addMemberDeprecatedInfo(fieldDoc, paramContent);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSerialFieldTagsInfo(Content paramContent) {
/* 470 */     if (this.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 473 */     FieldDoc fieldDoc = (FieldDoc)this.currentMember;
/*     */
/*     */
/*     */
/*     */
/* 478 */     SerialFieldTag[] arrayOfSerialFieldTag = fieldDoc.serialFieldTags();
/* 479 */     Arrays.sort((Object[])arrayOfSerialFieldTag);
/* 480 */     int i = arrayOfSerialFieldTag.length;
/* 481 */     for (byte b = 0; b < i; b++) {
/* 482 */       if (arrayOfSerialFieldTag[b].fieldName() != null && arrayOfSerialFieldTag[b].fieldType() != null) {
/*     */
/* 484 */         Content content = this.fieldWriter.getFieldsContentHeader((b == i - 1));
/*     */
/* 486 */         this.fieldWriter.addMemberHeader(arrayOfSerialFieldTag[b].fieldTypeDoc(), arrayOfSerialFieldTag[b]
/* 487 */             .fieldType(), "", arrayOfSerialFieldTag[b].fieldName(), content);
/* 488 */         this.fieldWriter.addMemberDescription(arrayOfSerialFieldTag[b], content);
/* 489 */         paramContent.addContent(content);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildFieldInfo(XMLNode paramXMLNode, Content paramContent) {
/* 500 */     if (this.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 503 */     FieldDoc fieldDoc = (FieldDoc)this.currentMember;
/* 504 */     ClassDoc classDoc = fieldDoc.containingClass();
/*     */
/* 506 */     if ((fieldDoc.tags("serial")).length == 0 && !fieldDoc.isSynthetic() && this.configuration.serialwarn)
/*     */     {
/* 508 */       this.configuration.message.warning(fieldDoc.position(), "doclet.MissingSerialTag", new Object[] { classDoc
/* 509 */             .qualifiedName(), fieldDoc
/* 510 */             .name() });
/*     */     }
/* 512 */     this.fieldWriter.addMemberDescription(fieldDoc, paramContent);
/* 513 */     this.fieldWriter.addMemberTags(fieldDoc, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static boolean serialInclude(Doc paramDoc) {
/* 523 */     if (paramDoc == null) {
/* 524 */       return false;
/*     */     }
/* 526 */     return paramDoc.isClass() ?
/* 527 */       serialClassInclude((ClassDoc)paramDoc) :
/* 528 */       serialDocInclude(paramDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static boolean serialClassInclude(ClassDoc paramClassDoc) {
/* 538 */     if (paramClassDoc.isEnum()) {
/* 539 */       return false;
/*     */     }
/*     */     try {
/* 542 */       paramClassDoc.superclassType();
/* 543 */     } catch (NullPointerException nullPointerException) {
/*     */
/* 545 */       return false;
/*     */     }
/* 547 */     if (paramClassDoc.isSerializable()) {
/* 548 */       if ((paramClassDoc.tags("serial")).length > 0)
/* 549 */         return serialDocInclude((Doc)paramClassDoc);
/* 550 */       if (paramClassDoc.isPublic() || paramClassDoc.isProtected()) {
/* 551 */         return true;
/*     */       }
/* 553 */       return false;
/*     */     }
/*     */
/* 556 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static boolean serialDocInclude(Doc paramDoc) {
/* 566 */     if (paramDoc.isEnum()) {
/* 567 */       return false;
/*     */     }
/* 569 */     Tag[] arrayOfTag = paramDoc.tags("serial");
/* 570 */     if (arrayOfTag.length > 0) {
/* 571 */       String str = StringUtils.toLowerCase(arrayOfTag[0].text());
/* 572 */       if (str.indexOf("exclude") >= 0)
/* 573 */         return false;
/* 574 */       if (str.indexOf("include") >= 0) {
/* 575 */         return true;
/*     */       }
/*     */     }
/* 578 */     return true;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean serialClassFoundToDocument(ClassDoc[] paramArrayOfClassDoc) {
/* 588 */     for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 589 */       if (serialClassInclude(paramArrayOfClassDoc[b])) {
/* 590 */         return true;
/*     */       }
/*     */     }
/* 593 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\SerializedFormBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
