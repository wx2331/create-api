/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.tools.javac.code.Symbol;
/*    */ import com.sun.tools.javac.jvm.ClassReader;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import java.util.EnumSet;
/*    */ import javax.tools.JavaFileObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavadocClassReader
/*    */   extends ClassReader
/*    */ {
/*    */   private DocEnv docenv;
/*    */   
/*    */   public static JavadocClassReader instance0(Context paramContext) {
/* 47 */     ClassReader classReader = (ClassReader)paramContext.get(classReaderKey);
/* 48 */     if (classReader == null)
/* 49 */       classReader = new JavadocClassReader(paramContext); 
/* 50 */     return (JavadocClassReader)classReader;
/*    */   }
/*    */   
/*    */   public static void preRegister(Context paramContext) {
/* 54 */     paramContext.put(classReaderKey, new Context.Factory<ClassReader>() {
/*    */           public ClassReader make(Context param1Context) {
/* 56 */             return new JavadocClassReader(param1Context);
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 62 */   private EnumSet<JavaFileObject.Kind> all = EnumSet.of(JavaFileObject.Kind.CLASS, JavaFileObject.Kind.SOURCE, JavaFileObject.Kind.HTML);
/*    */ 
/*    */   
/* 65 */   private EnumSet<JavaFileObject.Kind> noSource = EnumSet.of(JavaFileObject.Kind.CLASS, JavaFileObject.Kind.HTML);
/*    */ 
/*    */   
/*    */   public JavadocClassReader(Context paramContext) {
/* 69 */     super(paramContext, true);
/* 70 */     this.docenv = DocEnv.instance(paramContext);
/* 71 */     this.preferSource = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected EnumSet<JavaFileObject.Kind> getPackageFileKinds() {
/* 79 */     return this.docenv.docClasses ? this.noSource : this.all;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void extraFileActions(Symbol.PackageSymbol paramPackageSymbol, JavaFileObject paramJavaFileObject) {
/* 87 */     if (paramJavaFileObject.isNameCompatible("package", JavaFileObject.Kind.HTML))
/* 88 */       this.docenv.getPackageDoc(paramPackageSymbol).setDocPath(paramJavaFileObject); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\JavadocClassReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */