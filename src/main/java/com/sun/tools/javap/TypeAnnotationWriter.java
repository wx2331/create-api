/*     */ package com.sun.tools.javap;
/*     */
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.Method;
/*     */ import com.sun.tools.classfile.RuntimeTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.TypeAnnotation;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class TypeAnnotationWriter
/*     */   extends InstructionDetailWriter
/*     */ {
/*     */   private AnnotationWriter annotationWriter;
/*     */   private ClassWriter classWriter;
/*     */   private Map<Integer, List<Note>> pcMap;
/*     */
/*     */   public enum NoteKind
/*     */   {
/*  51 */     VISIBLE, INVISIBLE; }
/*     */
/*     */   public static class Note { Note(NoteKind param1NoteKind, TypeAnnotation param1TypeAnnotation) {
/*  54 */       this.kind = param1NoteKind;
/*  55 */       this.anno = param1TypeAnnotation;
/*     */     }
/*     */
/*     */     public final NoteKind kind;
/*     */     public final TypeAnnotation anno; }
/*     */
/*     */   static TypeAnnotationWriter instance(Context paramContext) {
/*  62 */     TypeAnnotationWriter typeAnnotationWriter = paramContext.<TypeAnnotationWriter>get(TypeAnnotationWriter.class);
/*  63 */     if (typeAnnotationWriter == null)
/*  64 */       typeAnnotationWriter = new TypeAnnotationWriter(paramContext);
/*  65 */     return typeAnnotationWriter;
/*     */   }
/*     */
/*     */   protected TypeAnnotationWriter(Context paramContext) {
/*  69 */     super(paramContext);
/*  70 */     paramContext.put(TypeAnnotationWriter.class, this);
/*  71 */     this.annotationWriter = AnnotationWriter.instance(paramContext);
/*  72 */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  76 */     Method method = this.classWriter.getMethod();
/*  77 */     this.pcMap = new HashMap<>();
/*  78 */     check(NoteKind.VISIBLE, (RuntimeTypeAnnotations_attribute)method.attributes.get("RuntimeVisibleTypeAnnotations"));
/*  79 */     check(NoteKind.INVISIBLE, (RuntimeTypeAnnotations_attribute)method.attributes.get("RuntimeInvisibleTypeAnnotations"));
/*     */   }
/*     */
/*     */   private void check(NoteKind paramNoteKind, RuntimeTypeAnnotations_attribute paramRuntimeTypeAnnotations_attribute) {
/*  83 */     if (paramRuntimeTypeAnnotations_attribute == null) {
/*     */       return;
/*     */     }
/*  86 */     for (TypeAnnotation typeAnnotation : paramRuntimeTypeAnnotations_attribute.annotations) {
/*  87 */       TypeAnnotation.Position position = typeAnnotation.position;
/*  88 */       Note note = null;
/*  89 */       if (position.offset != -1)
/*  90 */         addNote(position.offset, note = new Note(paramNoteKind, typeAnnotation));
/*  91 */       if (position.lvarOffset != null) {
/*  92 */         for (byte b = 0; b < position.lvarOffset.length; b++) {
/*  93 */           if (note == null)
/*  94 */             note = new Note(paramNoteKind, typeAnnotation);
/*  95 */           addNote(position.lvarOffset[b], note);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private void addNote(int paramInt, Note paramNote) {
/* 102 */     List<Note> list = this.pcMap.get(Integer.valueOf(paramInt));
/* 103 */     if (list == null)
/* 104 */       this.pcMap.put(Integer.valueOf(paramInt), list = new ArrayList<>());
/* 105 */     list.add(paramNote);
/*     */   }
/*     */
/*     */
/*     */   void writeDetails(Instruction paramInstruction) {
/* 110 */     String str = space(2);
/* 111 */     int i = paramInstruction.getPC();
/* 112 */     List list = this.pcMap.get(Integer.valueOf(i));
/* 113 */     if (list != null)
/* 114 */       for (Note note : list) {
/* 115 */         print(str);
/* 116 */         print("@");
/* 117 */         this.annotationWriter.write(note.anno, false, true);
/* 118 */         print(", ");
/* 119 */         println(StringUtils.toLowerCase(note.kind.toString()));
/*     */       }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\TypeAnnotationWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
