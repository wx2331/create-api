package com.sun.javadoc;

public interface SeeTag extends Tag {
  String label();
  
  PackageDoc referencedPackage();
  
  String referencedClassName();
  
  ClassDoc referencedClass();
  
  String referencedMemberName();
  
  MemberDoc referencedMember();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\SeeTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */