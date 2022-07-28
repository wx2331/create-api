/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeFieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeOptionalMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeRequiredMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstantsSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstructorWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.EnumConstantWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.FieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.MethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfilePackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfileSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.PropertyWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.WriterFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WriterFactoryImpl
/*     */   implements WriterFactory
/*     */ {
/*     */   private final ConfigurationImpl configuration;
/*     */   
/*     */   public WriterFactoryImpl(ConfigurationImpl paramConfigurationImpl) {
/*  51 */     this.configuration = paramConfigurationImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantsSummaryWriter getConstantsSummaryWriter() throws Exception {
/*  58 */     return new ConstantsSummaryWriterImpl(this.configuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageSummaryWriter getPackageSummaryWriter(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3) throws Exception {
/*  66 */     return new PackageWriterImpl(this.configuration, paramPackageDoc1, paramPackageDoc2, paramPackageDoc3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProfileSummaryWriter getProfileSummaryWriter(Profile paramProfile1, Profile paramProfile2, Profile paramProfile3) throws Exception {
/*  75 */     return new ProfileWriterImpl(this.configuration, paramProfile1, paramProfile2, paramProfile3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProfilePackageSummaryWriter getProfilePackageSummaryWriter(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, Profile paramProfile) throws Exception {
/*  84 */     return new ProfilePackageWriterImpl(this.configuration, paramPackageDoc1, paramPackageDoc2, paramPackageDoc3, paramProfile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassWriter getClassWriter(ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, ClassDoc paramClassDoc3, ClassTree paramClassTree) throws IOException {
/*  93 */     return new ClassWriterImpl(this.configuration, paramClassDoc1, paramClassDoc2, paramClassDoc3, paramClassTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeWriter getAnnotationTypeWriter(AnnotationTypeDoc paramAnnotationTypeDoc, Type paramType1, Type paramType2) throws Exception {
/* 103 */     return new AnnotationTypeWriterImpl(this.configuration, paramAnnotationTypeDoc, paramType1, paramType2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeFieldWriter getAnnotationTypeFieldWriter(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/* 112 */     return new AnnotationTypeFieldWriterImpl((SubWriterHolderWriter)paramAnnotationTypeWriter, paramAnnotationTypeWriter
/*     */         
/* 114 */         .getAnnotationTypeDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeOptionalMemberWriter getAnnotationTypeOptionalMemberWriter(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/* 123 */     return new AnnotationTypeOptionalMemberWriterImpl((SubWriterHolderWriter)paramAnnotationTypeWriter, paramAnnotationTypeWriter
/*     */         
/* 125 */         .getAnnotationTypeDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeRequiredMemberWriter getAnnotationTypeRequiredMemberWriter(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/* 133 */     return new AnnotationTypeRequiredMemberWriterImpl((SubWriterHolderWriter)paramAnnotationTypeWriter, paramAnnotationTypeWriter
/*     */         
/* 135 */         .getAnnotationTypeDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumConstantWriterImpl getEnumConstantWriter(ClassWriter paramClassWriter) throws Exception {
/* 143 */     return new EnumConstantWriterImpl((SubWriterHolderWriter)paramClassWriter, paramClassWriter
/* 144 */         .getClassDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldWriterImpl getFieldWriter(ClassWriter paramClassWriter) throws Exception {
/* 152 */     return new FieldWriterImpl((SubWriterHolderWriter)paramClassWriter, paramClassWriter
/* 153 */         .getClassDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyWriterImpl getPropertyWriter(ClassWriter paramClassWriter) throws Exception {
/* 161 */     return new PropertyWriterImpl((SubWriterHolderWriter)paramClassWriter, paramClassWriter
/* 162 */         .getClassDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodWriterImpl getMethodWriter(ClassWriter paramClassWriter) throws Exception {
/* 170 */     return new MethodWriterImpl((SubWriterHolderWriter)paramClassWriter, paramClassWriter
/* 171 */         .getClassDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorWriterImpl getConstructorWriter(ClassWriter paramClassWriter) throws Exception {
/* 179 */     return new ConstructorWriterImpl((SubWriterHolderWriter)paramClassWriter, paramClassWriter
/* 180 */         .getClassDoc());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberSummaryWriter getMemberSummaryWriter(ClassWriter paramClassWriter, int paramInt) throws Exception {
/* 189 */     switch (paramInt) {
/*     */       case 3:
/* 191 */         return getConstructorWriter(paramClassWriter);
/*     */       case 1:
/* 193 */         return getEnumConstantWriter(paramClassWriter);
/*     */       case 2:
/* 195 */         return getFieldWriter(paramClassWriter);
/*     */       case 8:
/* 197 */         return getPropertyWriter(paramClassWriter);
/*     */       case 0:
/* 199 */         return new NestedClassWriterImpl((SubWriterHolderWriter)paramClassWriter, paramClassWriter
/* 200 */             .getClassDoc());
/*     */       case 4:
/* 202 */         return getMethodWriter(paramClassWriter);
/*     */     } 
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberSummaryWriter getMemberSummaryWriter(AnnotationTypeWriter paramAnnotationTypeWriter, int paramInt) throws Exception {
/* 214 */     switch (paramInt) {
/*     */       case 5:
/* 216 */         return (AnnotationTypeFieldWriterImpl)
/* 217 */           getAnnotationTypeFieldWriter(paramAnnotationTypeWriter);
/*     */       case 6:
/* 219 */         return (AnnotationTypeOptionalMemberWriterImpl)
/* 220 */           getAnnotationTypeOptionalMemberWriter(paramAnnotationTypeWriter);
/*     */       case 7:
/* 222 */         return (AnnotationTypeRequiredMemberWriterImpl)
/* 223 */           getAnnotationTypeRequiredMemberWriter(paramAnnotationTypeWriter);
/*     */     } 
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializedFormWriter getSerializedFormWriter() throws Exception {
/* 233 */     return new SerializedFormWriterImpl(this.configuration);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\WriterFactoryImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */