/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.PropertyWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.WriterFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuilderFactory
/*     */ {
/*     */   private final Configuration configuration;
/*     */   private final WriterFactory writerFactory;
/*     */   private final AbstractBuilder.Context context;
/*     */   
/*     */   public BuilderFactory(Configuration paramConfiguration) {
/*  68 */     this.configuration = paramConfiguration;
/*  69 */     this.writerFactory = paramConfiguration.getWriterFactory();
/*     */     
/*  71 */     HashSet<String> hashSet = new HashSet();
/*  72 */     this
/*  73 */       .context = new AbstractBuilder.Context(paramConfiguration, hashSet, LayoutParser.getInstance(paramConfiguration));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getConstantsSummaryBuider() throws Exception {
/*  81 */     return ConstantsSummaryBuilder.getInstance(this.context, this.writerFactory
/*  82 */         .getConstantsSummaryWriter());
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
/*     */   public AbstractBuilder getPackageSummaryBuilder(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3) throws Exception {
/*  95 */     return PackageSummaryBuilder.getInstance(this.context, paramPackageDoc1, this.writerFactory
/*  96 */         .getPackageSummaryWriter(paramPackageDoc1, paramPackageDoc2, paramPackageDoc3));
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
/*     */   public AbstractBuilder getProfileSummaryBuilder(Profile paramProfile1, Profile paramProfile2, Profile paramProfile3) throws Exception {
/* 109 */     return ProfileSummaryBuilder.getInstance(this.context, paramProfile1, this.writerFactory
/* 110 */         .getProfileSummaryWriter(paramProfile1, paramProfile2, paramProfile3));
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
/*     */   public AbstractBuilder getProfilePackageSummaryBuilder(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, Profile paramProfile) throws Exception {
/* 124 */     return ProfilePackageSummaryBuilder.getInstance(this.context, paramPackageDoc1, this.writerFactory
/* 125 */         .getProfilePackageSummaryWriter(paramPackageDoc1, paramPackageDoc2, paramPackageDoc3, paramProfile), paramProfile);
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
/*     */   public AbstractBuilder getClassBuilder(ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, ClassDoc paramClassDoc3, ClassTree paramClassTree) throws Exception {
/* 142 */     return ClassBuilder.getInstance(this.context, paramClassDoc1, this.writerFactory
/* 143 */         .getClassWriter(paramClassDoc1, paramClassDoc2, paramClassDoc3, paramClassTree));
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
/*     */   public AbstractBuilder getAnnotationTypeBuilder(AnnotationTypeDoc paramAnnotationTypeDoc, Type paramType1, Type paramType2) throws Exception {
/* 160 */     return AnnotationTypeBuilder.getInstance(this.context, paramAnnotationTypeDoc, this.writerFactory
/* 161 */         .getAnnotationTypeWriter(paramAnnotationTypeDoc, paramType1, paramType2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getMethodBuilder(ClassWriter paramClassWriter) throws Exception {
/* 171 */     return MethodBuilder.getInstance(this.context, paramClassWriter
/* 172 */         .getClassDoc(), this.writerFactory
/* 173 */         .getMethodWriter(paramClassWriter));
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
/*     */   public AbstractBuilder getAnnotationTypeFieldsBuilder(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/* 186 */     return AnnotationTypeFieldBuilder.getInstance(this.context, (ClassDoc)paramAnnotationTypeWriter
/* 187 */         .getAnnotationTypeDoc(), this.writerFactory
/* 188 */         .getAnnotationTypeFieldWriter(paramAnnotationTypeWriter));
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
/*     */   public AbstractBuilder getAnnotationTypeOptionalMemberBuilder(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/* 202 */     return AnnotationTypeOptionalMemberBuilder.getInstance(this.context, (ClassDoc)paramAnnotationTypeWriter
/* 203 */         .getAnnotationTypeDoc(), this.writerFactory
/* 204 */         .getAnnotationTypeOptionalMemberWriter(paramAnnotationTypeWriter));
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
/*     */   public AbstractBuilder getAnnotationTypeRequiredMemberBuilder(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/* 218 */     return AnnotationTypeRequiredMemberBuilder.getInstance(this.context, (ClassDoc)paramAnnotationTypeWriter
/* 219 */         .getAnnotationTypeDoc(), this.writerFactory
/* 220 */         .getAnnotationTypeRequiredMemberWriter(paramAnnotationTypeWriter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getEnumConstantsBuilder(ClassWriter paramClassWriter) throws Exception {
/* 231 */     return EnumConstantBuilder.getInstance(this.context, paramClassWriter.getClassDoc(), this.writerFactory
/* 232 */         .getEnumConstantWriter(paramClassWriter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getFieldBuilder(ClassWriter paramClassWriter) throws Exception {
/* 242 */     return FieldBuilder.getInstance(this.context, paramClassWriter.getClassDoc(), this.writerFactory
/* 243 */         .getFieldWriter(paramClassWriter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getPropertyBuilder(ClassWriter paramClassWriter) throws Exception {
/* 253 */     PropertyWriter propertyWriter = this.writerFactory.getPropertyWriter(paramClassWriter);
/* 254 */     return PropertyBuilder.getInstance(this.context, paramClassWriter
/* 255 */         .getClassDoc(), propertyWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getConstructorBuilder(ClassWriter paramClassWriter) throws Exception {
/* 266 */     return ConstructorBuilder.getInstance(this.context, paramClassWriter
/* 267 */         .getClassDoc(), this.writerFactory
/* 268 */         .getConstructorWriter(paramClassWriter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getMemberSummaryBuilder(ClassWriter paramClassWriter) throws Exception {
/* 278 */     return MemberSummaryBuilder.getInstance(paramClassWriter, this.context);
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
/*     */   public AbstractBuilder getMemberSummaryBuilder(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/* 291 */     return MemberSummaryBuilder.getInstance(paramAnnotationTypeWriter, this.context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBuilder getSerializedFormBuilder() throws Exception {
/* 301 */     return SerializedFormBuilder.getInstance(this.context);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\BuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */