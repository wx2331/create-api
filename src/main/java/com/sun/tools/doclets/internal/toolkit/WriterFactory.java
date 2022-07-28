package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Type;
import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
import com.sun.tools.javac.jvm.Profile;

public interface WriterFactory {
  ConstantsSummaryWriter getConstantsSummaryWriter() throws Exception;
  
  PackageSummaryWriter getPackageSummaryWriter(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3) throws Exception;
  
  ProfileSummaryWriter getProfileSummaryWriter(Profile paramProfile1, Profile paramProfile2, Profile paramProfile3) throws Exception;
  
  ProfilePackageSummaryWriter getProfilePackageSummaryWriter(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, Profile paramProfile) throws Exception;
  
  ClassWriter getClassWriter(ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, ClassDoc paramClassDoc3, ClassTree paramClassTree) throws Exception;
  
  AnnotationTypeWriter getAnnotationTypeWriter(AnnotationTypeDoc paramAnnotationTypeDoc, Type paramType1, Type paramType2) throws Exception;
  
  MethodWriter getMethodWriter(ClassWriter paramClassWriter) throws Exception;
  
  AnnotationTypeFieldWriter getAnnotationTypeFieldWriter(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception;
  
  AnnotationTypeOptionalMemberWriter getAnnotationTypeOptionalMemberWriter(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception;
  
  AnnotationTypeRequiredMemberWriter getAnnotationTypeRequiredMemberWriter(AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception;
  
  EnumConstantWriter getEnumConstantWriter(ClassWriter paramClassWriter) throws Exception;
  
  FieldWriter getFieldWriter(ClassWriter paramClassWriter) throws Exception;
  
  PropertyWriter getPropertyWriter(ClassWriter paramClassWriter) throws Exception;
  
  ConstructorWriter getConstructorWriter(ClassWriter paramClassWriter) throws Exception;
  
  MemberSummaryWriter getMemberSummaryWriter(ClassWriter paramClassWriter, int paramInt) throws Exception;
  
  MemberSummaryWriter getMemberSummaryWriter(AnnotationTypeWriter paramAnnotationTypeWriter, int paramInt) throws Exception;
  
  SerializedFormWriter getSerializedFormWriter() throws Exception;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\WriterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */