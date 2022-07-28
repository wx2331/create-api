/*      */ package com.sun.tools.example.debug.expr;
/*      */ 
/*      */ import com.sun.jdi.AbsentInformationException;
/*      */ import com.sun.jdi.ArrayReference;
/*      */ import com.sun.jdi.ArrayType;
/*      */ import com.sun.jdi.ClassNotLoadedException;
/*      */ import com.sun.jdi.ClassType;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.IncompatibleThreadStateException;
/*      */ import com.sun.jdi.InterfaceType;
/*      */ import com.sun.jdi.InvalidTypeException;
/*      */ import com.sun.jdi.InvocationException;
/*      */ import com.sun.jdi.LocalVariable;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.PrimitiveValue;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.StackFrame;
/*      */ import com.sun.jdi.StringReference;
/*      */ import com.sun.jdi.ThreadReference;
/*      */ import com.sun.jdi.Type;
/*      */ import com.sun.jdi.Value;
/*      */ import com.sun.jdi.VirtualMachine;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ abstract class LValue
/*      */ {
/*      */   protected Value jdiValue;
/*      */   static final int STATIC = 0;
/*      */   static final int INSTANCE = 1;
/*      */   
/*      */   void setValue(Value paramValue) throws ParseException {
/*      */     try {
/*   73 */       setValue0(paramValue);
/*   74 */     } catch (InvalidTypeException invalidTypeException) {
/*   75 */       throw new ParseException("Attempt to set value of incorrect type" + invalidTypeException);
/*      */     
/*      */     }
/*   78 */     catch (ClassNotLoadedException classNotLoadedException) {
/*   79 */       throw new ParseException("Attempt to set value before " + classNotLoadedException
/*   80 */           .className() + " was loaded" + classNotLoadedException);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void setValue(LValue paramLValue) throws ParseException {
/*   86 */     setValue(paramLValue.interiorGetValue());
/*      */   }
/*      */ 
/*      */   
/*      */   LValue memberLValue(ExpressionParser.GetFrame paramGetFrame, String paramString) throws ParseException {
/*      */     try {
/*   92 */       return memberLValue(paramString, paramGetFrame.get().thread());
/*   93 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*   94 */       throw new ParseException("Thread not suspended");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   LValue memberLValue(String paramString, ThreadReference paramThreadReference) throws ParseException {
/*  100 */     Value value = interiorGetValue();
/*  101 */     if (value instanceof ArrayReference && "length"
/*  102 */       .equals(paramString)) {
/*  103 */       return new LValueArrayLength((ArrayReference)value);
/*      */     }
/*  105 */     return new LValueInstanceMember(value, paramString, paramThreadReference);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Value getMassagedValue(ExpressionParser.GetFrame paramGetFrame) throws ParseException {
/*  111 */     Value value = interiorGetValue();
/*      */ 
/*      */ 
/*      */     
/*  115 */     if (value instanceof ObjectReference && !(value instanceof StringReference) && !(value instanceof ArrayReference)) {
/*      */       StackFrame stackFrame;
/*      */ 
/*      */       
/*      */       try {
/*  120 */         stackFrame = paramGetFrame.get();
/*  121 */       } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  122 */         throw new ParseException("Thread not suspended");
/*      */       } 
/*      */       
/*  125 */       ThreadReference threadReference = stackFrame.thread();
/*  126 */       LValue lValue = memberLValue("toString", threadReference);
/*  127 */       lValue.invokeWith(new ArrayList<>());
/*  128 */       return lValue.interiorGetValue();
/*      */     } 
/*  130 */     return value;
/*      */   }
/*      */   
/*      */   Value interiorGetValue() throws ParseException {
/*      */     Value value;
/*      */     try {
/*  136 */       value = getValue();
/*  137 */     } catch (InvocationException invocationException) {
/*  138 */       throw new ParseException("Unable to complete expression. Exception " + invocationException
/*  139 */           .exception() + " thrown");
/*  140 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  141 */       throw new ParseException("Unable to complete expression. Thread not suspended for method invoke");
/*      */     }
/*  143 */     catch (InvalidTypeException invalidTypeException) {
/*  144 */       throw new ParseException("Unable to complete expression. Method argument type mismatch");
/*      */     }
/*  146 */     catch (ClassNotLoadedException classNotLoadedException) {
/*  147 */       throw new ParseException("Unable to complete expression. Method argument type " + classNotLoadedException
/*  148 */           .className() + " not yet loaded");
/*      */     } 
/*      */     
/*  151 */     return value;
/*      */   }
/*      */   LValue arrayElementLValue(LValue paramLValue) throws ParseException {
/*      */     int i;
/*  155 */     Value value = paramLValue.interiorGetValue();
/*      */     
/*  157 */     if (value instanceof com.sun.jdi.IntegerValue || value instanceof com.sun.jdi.ShortValue || value instanceof com.sun.jdi.ByteValue || value instanceof com.sun.jdi.CharValue) {
/*      */ 
/*      */ 
/*      */       
/*  161 */       i = ((PrimitiveValue)value).intValue();
/*      */     } else {
/*  163 */       throw new ParseException("Array index must be a integer type");
/*      */     } 
/*  165 */     return new LValueArrayElement(interiorGetValue(), i);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*      */     try {
/*  171 */       return interiorGetValue().toString();
/*  172 */     } catch (ParseException parseException) {
/*  173 */       return "<Parse Exception>";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Field fieldByName(ReferenceType paramReferenceType, String paramString, int paramInt) {
/*  187 */     Field field = paramReferenceType.fieldByName(paramString);
/*  188 */     if (field != null) {
/*  189 */       boolean bool = field.isStatic();
/*  190 */       if ((paramInt == 0 && !bool) || (paramInt == 1 && bool))
/*      */       {
/*  192 */         field = null;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  201 */     return field;
/*      */   }
/*      */ 
/*      */   
/*      */   static List<Method> methodsByName(ReferenceType paramReferenceType, String paramString, int paramInt) {
/*  206 */     List<Method> list = paramReferenceType.methodsByName(paramString);
/*  207 */     Iterator<Method> iterator = list.iterator();
/*  208 */     while (iterator.hasNext()) {
/*  209 */       Method method = iterator.next();
/*  210 */       boolean bool = method.isStatic();
/*  211 */       if ((paramInt == 0 && !bool) || (paramInt == 1 && bool))
/*      */       {
/*  213 */         iterator.remove();
/*      */       }
/*      */     } 
/*  216 */     return list;
/*      */   }
/*      */   
/*  219 */   static List<String> primitiveTypeNames = new ArrayList<>();
/*      */   static {
/*  221 */     primitiveTypeNames.add("boolean");
/*  222 */     primitiveTypeNames.add("byte");
/*  223 */     primitiveTypeNames.add("char");
/*  224 */     primitiveTypeNames.add("short");
/*  225 */     primitiveTypeNames.add("int");
/*  226 */     primitiveTypeNames.add("long");
/*  227 */     primitiveTypeNames.add("float");
/*  228 */     primitiveTypeNames.add("double");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final int SAME = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   static final int ASSIGNABLE = 1;
/*      */ 
/*      */   
/*      */   static final int DIFFERENT = 2;
/*      */ 
/*      */ 
/*      */   
/*      */   static int argumentsMatch(List<Type> paramList, List<Value> paramList1) {
/*  246 */     if (paramList.size() != paramList1.size()) {
/*  247 */       return 2;
/*      */     }
/*      */     
/*  250 */     Iterator<Type> iterator = paramList.iterator();
/*  251 */     Iterator<Value> iterator1 = paramList1.iterator();
/*  252 */     boolean bool = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  257 */     while (iterator.hasNext()) {
/*  258 */       Type type = iterator.next();
/*  259 */       Value value = iterator1.next();
/*  260 */       if (value == null)
/*      */       {
/*  262 */         if (primitiveTypeNames.contains(type.name())) {
/*  263 */           return 2;
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*  268 */       if (!value.type().equals(type)) {
/*  269 */         if (isAssignableTo(value.type(), type)) {
/*  270 */           bool = true; continue;
/*      */         } 
/*  272 */         return 2;
/*      */       } 
/*      */     } 
/*      */     
/*  276 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isComponentAssignable(Type paramType1, Type paramType2) {
/*  284 */     if (paramType1 instanceof com.sun.jdi.PrimitiveType)
/*      */     {
/*      */       
/*  287 */       return paramType1.equals(paramType2);
/*      */     }
/*  289 */     if (paramType2 instanceof com.sun.jdi.PrimitiveType) {
/*  290 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  294 */     return isAssignableTo(paramType1, paramType2);
/*      */   }
/*      */   
/*      */   static boolean isArrayAssignableTo(ArrayType paramArrayType, Type paramType) {
/*  298 */     if (paramType instanceof ArrayType) {
/*      */       try {
/*  300 */         Type type = ((ArrayType)paramType).componentType();
/*  301 */         return isComponentAssignable(paramArrayType.componentType(), type);
/*  302 */       } catch (ClassNotLoadedException classNotLoadedException) {
/*      */ 
/*      */         
/*  305 */         return false;
/*      */       } 
/*      */     }
/*  308 */     if (paramType instanceof InterfaceType)
/*      */     {
/*  310 */       return paramType.name().equals("java.lang.Cloneable");
/*      */     }
/*      */     
/*  313 */     return paramType.name().equals("java.lang.Object");
/*      */   }
/*      */   static boolean isAssignableTo(Type paramType1, Type paramType2) {
/*      */     List list;
/*  317 */     if (paramType1.equals(paramType2)) {
/*  318 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  322 */     if (paramType1 instanceof com.sun.jdi.BooleanType) {
/*  323 */       if (paramType2 instanceof com.sun.jdi.BooleanType) {
/*  324 */         return true;
/*      */       }
/*  326 */       return false;
/*      */     } 
/*  328 */     if (paramType2 instanceof com.sun.jdi.BooleanType) {
/*  329 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  333 */     if (paramType1 instanceof com.sun.jdi.PrimitiveType) {
/*  334 */       if (paramType2 instanceof com.sun.jdi.PrimitiveType) {
/*  335 */         return true;
/*      */       }
/*  337 */       return false;
/*      */     } 
/*  339 */     if (paramType2 instanceof com.sun.jdi.PrimitiveType) {
/*  340 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  344 */     if (paramType1 instanceof ArrayType) {
/*  345 */       return isArrayAssignableTo((ArrayType)paramType1, paramType2);
/*      */     }
/*      */     
/*  348 */     if (paramType1 instanceof ClassType) {
/*  349 */       ClassType classType = ((ClassType)paramType1).superclass();
/*  350 */       if (classType != null && isAssignableTo((Type)classType, paramType2)) {
/*  351 */         return true;
/*      */       }
/*  353 */       list = ((ClassType)paramType1).interfaces();
/*      */     } else {
/*      */       
/*  356 */       list = ((InterfaceType)paramType1).superinterfaces();
/*      */     } 
/*  358 */     for (InterfaceType interfaceType : list) {
/*  359 */       if (isAssignableTo((Type)interfaceType, paramType2)) {
/*  360 */         return true;
/*      */       }
/*      */     } 
/*  363 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Method resolveOverload(List<Method> paramList, List<Value> paramList1) throws ParseException {
/*  374 */     if (paramList.size() == 1) {
/*  375 */       return paramList.get(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  387 */     Method method = null;
/*  388 */     byte b = 0;
/*  389 */     for (Method method1 : paramList) {
/*      */       List<Type> list;
/*      */       try {
/*  392 */         list = method1.argumentTypes();
/*  393 */       } catch (ClassNotLoadedException classNotLoadedException) {
/*      */         continue;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  399 */       int i = argumentsMatch(list, paramList1);
/*  400 */       if (i == 0) {
/*  401 */         return method1;
/*      */       }
/*  403 */       if (i == 2) {
/*      */         continue;
/*      */       }
/*      */       
/*  407 */       method = method1;
/*  408 */       b++;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  414 */     if (method != null) {
/*  415 */       if (b == 1) {
/*  416 */         return method;
/*      */       }
/*  418 */       throw new ParseException("Arguments match multiple methods");
/*      */     } 
/*  420 */     throw new ParseException("Arguments match no method");
/*      */   }
/*      */   
/*      */   private static class LValueLocal extends LValue {
/*      */     final StackFrame frame;
/*      */     final LocalVariable var;
/*      */     
/*      */     LValueLocal(StackFrame param1StackFrame, LocalVariable param1LocalVariable) {
/*  428 */       this.frame = param1StackFrame;
/*  429 */       this.var = param1LocalVariable;
/*      */     }
/*      */ 
/*      */     
/*      */     Value getValue() {
/*  434 */       if (this.jdiValue == null) {
/*  435 */         this.jdiValue = this.frame.getValue(this.var);
/*      */       }
/*  437 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue0(Value param1Value) throws InvalidTypeException, ClassNotLoadedException {
/*  443 */       this.frame.setValue(this.var, param1Value);
/*  444 */       this.jdiValue = param1Value;
/*      */     }
/*      */ 
/*      */     
/*      */     void invokeWith(List<Value> param1List) throws ParseException {
/*  449 */       throw new ParseException(this.var.name() + " is not a method");
/*      */     }
/*      */   }
/*      */   
/*      */   private static class LValueInstanceMember extends LValue {
/*      */     final ObjectReference obj;
/*      */     final ThreadReference thread;
/*      */     final Field matchingField;
/*      */     final List<Method> overloads;
/*  458 */     Method matchingMethod = null;
/*  459 */     List<Value> methodArguments = null;
/*      */ 
/*      */ 
/*      */     
/*      */     LValueInstanceMember(Value param1Value, String param1String, ThreadReference param1ThreadReference) throws ParseException {
/*  464 */       if (!(param1Value instanceof ObjectReference)) {
/*  465 */         throw new ParseException("Cannot access field of primitive type: " + param1Value);
/*      */       }
/*      */       
/*  468 */       this.obj = (ObjectReference)param1Value;
/*  469 */       this.thread = param1ThreadReference;
/*  470 */       ReferenceType referenceType = this.obj.referenceType();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  475 */       this.matchingField = LValue.fieldByName(referenceType, param1String, 1);
/*      */       
/*  477 */       this.overloads = LValue.methodsByName(referenceType, param1String, 1);
/*      */       
/*  479 */       if (this.matchingField == null && this.overloads.size() == 0) {
/*  480 */         throw new ParseException("No instance field or method with the name " + param1String + " in " + referenceType
/*  481 */             .name());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Value getValue() throws InvocationException, InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, ParseException {
/*  489 */       if (this.jdiValue != null) {
/*  490 */         return this.jdiValue;
/*      */       }
/*  492 */       if (this.matchingMethod == null) {
/*  493 */         if (this.matchingField == null) {
/*  494 */           throw new ParseException("No such field in " + this.obj.referenceType().name());
/*      */         }
/*  496 */         return this.jdiValue = this.obj.getValue(this.matchingField);
/*      */       } 
/*  498 */       return this.jdiValue = this.obj.invokeMethod(this.thread, this.matchingMethod, this.methodArguments, 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue0(Value param1Value) throws ParseException, InvalidTypeException, ClassNotLoadedException {
/*  506 */       if (this.matchingMethod != null) {
/*  507 */         throw new ParseException("Cannot assign to a method invocation");
/*      */       }
/*  509 */       this.obj.setValue(this.matchingField, param1Value);
/*  510 */       this.jdiValue = param1Value;
/*      */     }
/*      */ 
/*      */     
/*      */     void invokeWith(List<Value> param1List) throws ParseException {
/*  515 */       if (this.matchingMethod != null) {
/*  516 */         throw new ParseException("Invalid consecutive invocations");
/*      */       }
/*  518 */       this.methodArguments = param1List;
/*  519 */       this.matchingMethod = LValue.resolveOverload(this.overloads, param1List);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class LValueStaticMember extends LValue {
/*      */     final ReferenceType refType;
/*      */     final ThreadReference thread;
/*      */     final Field matchingField;
/*      */     final List<Method> overloads;
/*  528 */     Method matchingMethod = null;
/*  529 */     List<Value> methodArguments = null;
/*      */ 
/*      */ 
/*      */     
/*      */     LValueStaticMember(ReferenceType param1ReferenceType, String param1String, ThreadReference param1ThreadReference) throws ParseException {
/*  534 */       this.refType = param1ReferenceType;
/*  535 */       this.thread = param1ThreadReference;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  540 */       this.matchingField = LValue.fieldByName(param1ReferenceType, param1String, 0);
/*      */       
/*  542 */       this.overloads = LValue.methodsByName(param1ReferenceType, param1String, 0);
/*      */       
/*  544 */       if (this.matchingField == null && this.overloads.size() == 0) {
/*  545 */         throw new ParseException("No static field or method with the name " + param1String + " in " + param1ReferenceType
/*  546 */             .name());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Value getValue() throws InvocationException, InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, ParseException {
/*  554 */       if (this.jdiValue != null) {
/*  555 */         return this.jdiValue;
/*      */       }
/*  557 */       if (this.matchingMethod == null)
/*  558 */         return this.jdiValue = this.refType.getValue(this.matchingField); 
/*  559 */       if (this.refType instanceof ClassType) {
/*  560 */         ClassType classType = (ClassType)this.refType;
/*  561 */         return this.jdiValue = classType.invokeMethod(this.thread, this.matchingMethod, this.methodArguments, 0);
/*  562 */       }  if (this.refType instanceof InterfaceType) {
/*  563 */         InterfaceType interfaceType = (InterfaceType)this.refType;
/*  564 */         return this.jdiValue = interfaceType.invokeMethod(this.thread, this.matchingMethod, this.methodArguments, 0);
/*      */       } 
/*  566 */       throw new InvalidTypeException("Cannot invoke static method on " + this.refType
/*  567 */           .name());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue0(Value param1Value) throws ParseException, InvalidTypeException, ClassNotLoadedException {
/*  575 */       if (this.matchingMethod != null) {
/*  576 */         throw new ParseException("Cannot assign to a method invocation");
/*      */       }
/*  578 */       if (!(this.refType instanceof ClassType)) {
/*  579 */         throw new ParseException("Cannot set interface field: " + this.refType);
/*      */       }
/*      */       
/*  582 */       ((ClassType)this.refType).setValue(this.matchingField, param1Value);
/*  583 */       this.jdiValue = param1Value;
/*      */     }
/*      */ 
/*      */     
/*      */     void invokeWith(List<Value> param1List) throws ParseException {
/*  588 */       if (this.matchingMethod != null) {
/*  589 */         throw new ParseException("Invalid consecutive invocations");
/*      */       }
/*  591 */       this.methodArguments = param1List;
/*  592 */       this.matchingMethod = LValue.resolveOverload(this.overloads, param1List);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class LValueArrayLength
/*      */     extends LValue
/*      */   {
/*      */     final ArrayReference arrayRef;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     LValueArrayLength(ArrayReference param1ArrayReference) {
/*  611 */       this.arrayRef = param1ArrayReference;
/*      */     }
/*      */ 
/*      */     
/*      */     Value getValue() {
/*  616 */       if (this.jdiValue == null) {
/*  617 */         this.jdiValue = (Value)this.arrayRef.virtualMachine().mirrorOf(this.arrayRef.length());
/*      */       }
/*  619 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */     
/*      */     void setValue0(Value param1Value) throws ParseException {
/*  624 */       throw new ParseException("Cannot set constant: " + param1Value);
/*      */     }
/*      */ 
/*      */     
/*      */     void invokeWith(List<Value> param1List) throws ParseException {
/*  629 */       throw new ParseException("Array element is not a method");
/*      */     }
/*      */   }
/*      */   
/*      */   private static class LValueArrayElement extends LValue {
/*      */     final ArrayReference array;
/*      */     final int index;
/*      */     
/*      */     LValueArrayElement(Value param1Value, int param1Int) throws ParseException {
/*  638 */       if (!(param1Value instanceof ArrayReference)) {
/*  639 */         throw new ParseException("Must be array type: " + param1Value);
/*      */       }
/*      */       
/*  642 */       this.array = (ArrayReference)param1Value;
/*  643 */       this.index = param1Int;
/*      */     }
/*      */ 
/*      */     
/*      */     Value getValue() {
/*  648 */       if (this.jdiValue == null) {
/*  649 */         this.jdiValue = this.array.getValue(this.index);
/*      */       }
/*  651 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue0(Value param1Value) throws InvalidTypeException, ClassNotLoadedException {
/*  657 */       this.array.setValue(this.index, param1Value);
/*  658 */       this.jdiValue = param1Value;
/*      */     }
/*      */ 
/*      */     
/*      */     void invokeWith(List<Value> param1List) throws ParseException {
/*  663 */       throw new ParseException("Array element is not a method");
/*      */     }
/*      */   }
/*      */   
/*      */   private static class LValueConstant extends LValue {
/*      */     final Value value;
/*      */     
/*      */     LValueConstant(Value param1Value) {
/*  671 */       this.value = param1Value;
/*      */     }
/*      */ 
/*      */     
/*      */     Value getValue() {
/*  676 */       if (this.jdiValue == null) {
/*  677 */         this.jdiValue = this.value;
/*      */       }
/*  679 */       return this.jdiValue;
/*      */     }
/*      */ 
/*      */     
/*      */     void setValue0(Value param1Value) throws ParseException {
/*  684 */       throw new ParseException("Cannot set constant: " + this.value);
/*      */     }
/*      */ 
/*      */     
/*      */     void invokeWith(List<Value> param1List) throws ParseException {
/*  689 */       throw new ParseException("Constant is not a method");
/*      */     }
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, boolean paramBoolean) {
/*  694 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramBoolean));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, byte paramByte) {
/*  698 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramByte));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, char paramChar) {
/*  702 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramChar));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, short paramShort) {
/*  706 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramShort));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, int paramInt) {
/*  710 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramInt));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, long paramLong) {
/*  714 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramLong));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, float paramFloat) {
/*  718 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramFloat));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, double paramDouble) {
/*  722 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramDouble));
/*      */   }
/*      */   
/*      */   static LValue make(VirtualMachine paramVirtualMachine, String paramString) throws ParseException {
/*  726 */     return new LValueConstant((Value)paramVirtualMachine.mirrorOf(paramString));
/*      */   }
/*      */   
/*      */   static LValue makeBoolean(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  730 */     return make(paramVirtualMachine, (paramToken.image.charAt(0) == 't'));
/*      */   }
/*      */   
/*      */   static LValue makeCharacter(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  734 */     return make(paramVirtualMachine, paramToken.image.charAt(1));
/*      */   }
/*      */   
/*      */   static LValue makeFloat(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  738 */     return make(paramVirtualMachine, Float.valueOf(paramToken.image).floatValue());
/*      */   }
/*      */   
/*      */   static LValue makeDouble(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  742 */     return make(paramVirtualMachine, Double.valueOf(paramToken.image).doubleValue());
/*      */   }
/*      */   
/*      */   static LValue makeInteger(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  746 */     return make(paramVirtualMachine, Integer.parseInt(paramToken.image));
/*      */   }
/*      */   
/*      */   static LValue makeShort(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  750 */     return make(paramVirtualMachine, Short.parseShort(paramToken.image));
/*      */   }
/*      */   
/*      */   static LValue makeLong(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  754 */     return make(paramVirtualMachine, Long.parseLong(paramToken.image));
/*      */   }
/*      */   
/*      */   static LValue makeByte(VirtualMachine paramVirtualMachine, Token paramToken) {
/*  758 */     return make(paramVirtualMachine, Byte.parseByte(paramToken.image));
/*      */   }
/*      */ 
/*      */   
/*      */   static LValue makeString(VirtualMachine paramVirtualMachine, Token paramToken) throws ParseException {
/*  763 */     int i = paramToken.image.length();
/*  764 */     return make(paramVirtualMachine, paramToken.image.substring(1, i - 1));
/*      */   }
/*      */ 
/*      */   
/*      */   static LValue makeNull(VirtualMachine paramVirtualMachine, Token paramToken) throws ParseException {
/*  769 */     return new LValueConstant(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static LValue makeThisObject(VirtualMachine paramVirtualMachine, ExpressionParser.GetFrame paramGetFrame, Token paramToken) throws ParseException {
/*  775 */     if (paramGetFrame == null) {
/*  776 */       throw new ParseException("No current thread");
/*      */     }
/*      */     try {
/*  779 */       StackFrame stackFrame = paramGetFrame.get();
/*  780 */       ObjectReference objectReference = stackFrame.thisObject();
/*      */       
/*  782 */       if (objectReference == null) {
/*  783 */         throw new ParseException("No 'this'.  In native or static method");
/*      */       }
/*      */       
/*  786 */       return new LValueConstant((Value)objectReference);
/*      */     }
/*  788 */     catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  789 */       throw new ParseException("Thread not suspended");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static LValue makeNewObject(VirtualMachine paramVirtualMachine, ExpressionParser.GetFrame paramGetFrame, String paramString, List<Value> paramList) throws ParseException {
/*      */     ObjectReference objectReference;
/*  797 */     List<ReferenceType> list = paramVirtualMachine.classesByName(paramString);
/*  798 */     if (list.size() == 0) {
/*  799 */       throw new ParseException("No class named: " + paramString);
/*      */     }
/*      */     
/*  802 */     if (list.size() > 1) {
/*  803 */       throw new ParseException("More than one class named: " + paramString);
/*      */     }
/*      */     
/*  806 */     ReferenceType referenceType = list.get(0);
/*      */ 
/*      */     
/*  809 */     if (!(referenceType instanceof ClassType)) {
/*  810 */       throw new ParseException("Cannot create instance of interface " + paramString);
/*      */     }
/*      */ 
/*      */     
/*  814 */     ClassType classType = (ClassType)referenceType;
/*  815 */     ArrayList<Method> arrayList = new ArrayList(classType.methods());
/*  816 */     Iterator<Method> iterator = arrayList.iterator();
/*  817 */     while (iterator.hasNext()) {
/*  818 */       Method method1 = iterator.next();
/*  819 */       if (!method1.isConstructor()) {
/*  820 */         iterator.remove();
/*      */       }
/*      */     } 
/*  823 */     Method method = resolveOverload(arrayList, paramList);
/*      */ 
/*      */     
/*      */     try {
/*  827 */       ThreadReference threadReference = paramGetFrame.get().thread();
/*  828 */       objectReference = classType.newInstance(threadReference, method, paramList, 0);
/*  829 */     } catch (InvocationException invocationException) {
/*  830 */       throw new ParseException("Exception in " + paramString + " constructor: " + invocationException
/*  831 */           .exception().referenceType().name());
/*  832 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  833 */       throw new ParseException("Thread not suspended");
/*  834 */     } catch (Exception exception) {
/*      */ 
/*      */ 
/*      */       
/*  838 */       throw new ParseException("Unable to create " + paramString + " instance");
/*      */     } 
/*  840 */     return new LValueConstant((Value)objectReference);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static LValue nFields(LValue paramLValue, StringTokenizer paramStringTokenizer, ThreadReference paramThreadReference) throws ParseException {
/*  847 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  848 */       return paramLValue;
/*      */     }
/*  850 */     return nFields(paramLValue.memberLValue(paramStringTokenizer.nextToken(), paramThreadReference), paramStringTokenizer, paramThreadReference);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static LValue makeName(VirtualMachine paramVirtualMachine, ExpressionParser.GetFrame paramGetFrame, String paramString) throws ParseException {
/*  857 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, ".");
/*  858 */     String str = stringTokenizer.nextToken();
/*      */     
/*  860 */     if (paramGetFrame != null) {
/*      */       try {
/*  862 */         LocalVariable localVariable; StackFrame stackFrame = paramGetFrame.get();
/*  863 */         ThreadReference threadReference = stackFrame.thread();
/*      */         
/*      */         try {
/*  866 */           localVariable = stackFrame.visibleVariableByName(str);
/*  867 */         } catch (AbsentInformationException absentInformationException) {
/*  868 */           localVariable = null;
/*      */         } 
/*  870 */         if (localVariable != null) {
/*  871 */           return nFields(new LValueLocal(stackFrame, localVariable), stringTokenizer, threadReference);
/*      */         }
/*  873 */         ObjectReference objectReference = stackFrame.thisObject();
/*  874 */         if (objectReference != null) {
/*      */           LValue lValue;
/*  876 */           LValueConstant lValueConstant = new LValueConstant((Value)objectReference);
/*      */           
/*      */           try {
/*  879 */             lValue = lValueConstant.memberLValue(str, threadReference);
/*  880 */           } catch (ParseException parseException) {
/*  881 */             lValue = null;
/*      */           } 
/*  883 */           if (lValue != null) {
/*  884 */             return nFields(lValue, stringTokenizer, threadReference);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/*  889 */         while (stringTokenizer.hasMoreTokens()) {
/*  890 */           List<ReferenceType> list = paramVirtualMachine.classesByName(str);
/*  891 */           if (list.size() > 0) {
/*  892 */             if (list.size() > 1) {
/*  893 */               throw new ParseException("More than one class named: " + str);
/*      */             }
/*      */             
/*  896 */             ReferenceType referenceType = list.get(0);
/*      */             
/*  898 */             LValueStaticMember lValueStaticMember = new LValueStaticMember(referenceType, stringTokenizer.nextToken(), threadReference);
/*  899 */             return nFields(lValueStaticMember, stringTokenizer, threadReference);
/*      */           } 
/*      */           
/*  902 */           str = str + '.' + stringTokenizer.nextToken();
/*      */         } 
/*  904 */       } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  905 */         throw new ParseException("Thread not suspended");
/*      */       } 
/*      */     }
/*  908 */     throw new ParseException("Name unknown: " + paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   static String stringValue(LValue paramLValue, ExpressionParser.GetFrame paramGetFrame) throws ParseException {
/*  913 */     Value value = paramLValue.getMassagedValue(paramGetFrame);
/*  914 */     if (value == null) {
/*  915 */       return "null";
/*      */     }
/*  917 */     if (value instanceof StringReference) {
/*  918 */       return ((StringReference)value).value();
/*      */     }
/*  920 */     return value.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   static LValue booleanOperation(VirtualMachine paramVirtualMachine, Token paramToken, LValue paramLValue1, LValue paramLValue2) throws ParseException {
/*      */     boolean bool;
/*  926 */     String str = paramToken.image;
/*  927 */     Value value1 = paramLValue1.interiorGetValue();
/*  928 */     Value value2 = paramLValue2.interiorGetValue();
/*  929 */     if (!(value1 instanceof PrimitiveValue) || !(value2 instanceof PrimitiveValue)) {
/*      */       
/*  931 */       if (str.equals("=="))
/*  932 */         return make(paramVirtualMachine, value1.equals(value2)); 
/*  933 */       if (str.equals("!=")) {
/*  934 */         return make(paramVirtualMachine, !value1.equals(value2));
/*      */       }
/*  936 */       throw new ParseException("Operands or '" + str + "' must be primitive");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  941 */     double d1 = ((PrimitiveValue)value1).doubleValue();
/*  942 */     double d2 = ((PrimitiveValue)value2).doubleValue();
/*      */     
/*  944 */     if (str.equals("<")) {
/*  945 */       bool = (d1 < d2) ? true : false;
/*  946 */     } else if (str.equals(">")) {
/*  947 */       bool = (d1 > d2) ? true : false;
/*  948 */     } else if (str.equals("<=")) {
/*  949 */       bool = (d1 <= d2) ? true : false;
/*  950 */     } else if (str.equals(">=")) {
/*  951 */       bool = (d1 >= d2) ? true : false;
/*  952 */     } else if (str.equals("==")) {
/*  953 */       bool = (d1 == d2) ? true : false;
/*  954 */     } else if (str.equals("!=")) {
/*  955 */       bool = (d1 != d2) ? true : false;
/*      */     } else {
/*  957 */       throw new ParseException("Unknown operation: " + str);
/*      */     } 
/*  959 */     return make(paramVirtualMachine, bool);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static LValue operation(VirtualMachine paramVirtualMachine, Token paramToken, LValue paramLValue1, LValue paramLValue2, ExpressionParser.GetFrame paramGetFrame) throws ParseException {
/*      */     int k;
/*  966 */     String str = paramToken.image;
/*  967 */     Value value1 = paramLValue1.interiorGetValue();
/*  968 */     Value value2 = paramLValue2.interiorGetValue();
/*  969 */     if (value1 instanceof StringReference || value2 instanceof StringReference)
/*      */     {
/*  971 */       if (str.equals("+"))
/*      */       {
/*      */         
/*  974 */         return make(paramVirtualMachine, stringValue(paramLValue1, paramGetFrame) + 
/*  975 */             stringValue(paramLValue2, paramGetFrame));
/*      */       }
/*      */     }
/*  978 */     if (value1 instanceof ObjectReference || value2 instanceof ObjectReference) {
/*      */       
/*  980 */       if (str.equals("=="))
/*  981 */         return make(paramVirtualMachine, value1.equals(value2)); 
/*  982 */       if (str.equals("!=")) {
/*  983 */         return make(paramVirtualMachine, !value1.equals(value2));
/*      */       }
/*  985 */       throw new ParseException("Invalid operation '" + str + "' on an Object");
/*      */     } 
/*      */ 
/*      */     
/*  989 */     if (value1 instanceof com.sun.jdi.BooleanValue || value2 instanceof com.sun.jdi.BooleanValue)
/*      */     {
/*  991 */       throw new ParseException("Invalid operation '" + str + "' on a Boolean");
/*      */     }
/*      */ 
/*      */     
/*  995 */     PrimitiveValue primitiveValue1 = (PrimitiveValue)value1;
/*  996 */     PrimitiveValue primitiveValue2 = (PrimitiveValue)value2;
/*  997 */     if (primitiveValue1 instanceof com.sun.jdi.DoubleValue || primitiveValue2 instanceof com.sun.jdi.DoubleValue) {
/*      */       
/*  999 */       double d3, d1 = primitiveValue1.doubleValue();
/* 1000 */       double d2 = primitiveValue2.doubleValue();
/*      */       
/* 1002 */       if (str.equals("+")) {
/* 1003 */         d3 = d1 + d2;
/* 1004 */       } else if (str.equals("-")) {
/* 1005 */         d3 = d1 - d2;
/* 1006 */       } else if (str.equals("*")) {
/* 1007 */         d3 = d1 * d2;
/* 1008 */       } else if (str.equals("/")) {
/* 1009 */         d3 = d1 / d2;
/*      */       } else {
/* 1011 */         throw new ParseException("Unknown operation: " + str);
/*      */       } 
/* 1013 */       return make(paramVirtualMachine, d3);
/*      */     } 
/* 1015 */     if (primitiveValue1 instanceof com.sun.jdi.FloatValue || primitiveValue2 instanceof com.sun.jdi.FloatValue) {
/*      */       
/* 1017 */       float f3, f1 = primitiveValue1.floatValue();
/* 1018 */       float f2 = primitiveValue2.floatValue();
/*      */       
/* 1020 */       if (str.equals("+")) {
/* 1021 */         f3 = f1 + f2;
/* 1022 */       } else if (str.equals("-")) {
/* 1023 */         f3 = f1 - f2;
/* 1024 */       } else if (str.equals("*")) {
/* 1025 */         f3 = f1 * f2;
/* 1026 */       } else if (str.equals("/")) {
/* 1027 */         f3 = f1 / f2;
/*      */       } else {
/* 1029 */         throw new ParseException("Unknown operation: " + str);
/*      */       } 
/* 1031 */       return make(paramVirtualMachine, f3);
/*      */     } 
/* 1033 */     if (primitiveValue1 instanceof com.sun.jdi.LongValue || primitiveValue2 instanceof com.sun.jdi.LongValue) {
/*      */       
/* 1035 */       long l3, l1 = primitiveValue1.longValue();
/* 1036 */       long l2 = primitiveValue2.longValue();
/*      */       
/* 1038 */       if (str.equals("+")) {
/* 1039 */         l3 = l1 + l2;
/* 1040 */       } else if (str.equals("-")) {
/* 1041 */         l3 = l1 - l2;
/* 1042 */       } else if (str.equals("*")) {
/* 1043 */         l3 = l1 * l2;
/* 1044 */       } else if (str.equals("/")) {
/* 1045 */         l3 = l1 / l2;
/*      */       } else {
/* 1047 */         throw new ParseException("Unknown operation: " + str);
/*      */       } 
/* 1049 */       return make(paramVirtualMachine, l3);
/*      */     } 
/* 1051 */     int i = primitiveValue1.intValue();
/* 1052 */     int j = primitiveValue2.intValue();
/*      */     
/* 1054 */     if (str.equals("+")) {
/* 1055 */       k = i + j;
/* 1056 */     } else if (str.equals("-")) {
/* 1057 */       k = i - j;
/* 1058 */     } else if (str.equals("*")) {
/* 1059 */       k = i * j;
/* 1060 */     } else if (str.equals("/")) {
/* 1061 */       k = i / j;
/*      */     } else {
/* 1063 */       throw new ParseException("Unknown operation: " + str);
/*      */     } 
/* 1065 */     return make(paramVirtualMachine, k);
/*      */   }
/*      */   
/*      */   abstract Value getValue() throws InvocationException, IncompatibleThreadStateException, InvalidTypeException, ClassNotLoadedException, ParseException;
/*      */   
/*      */   abstract void setValue0(Value paramValue) throws ParseException, InvalidTypeException, ClassNotLoadedException;
/*      */   
/*      */   abstract void invokeWith(List<Value> paramList) throws ParseException;
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\expr\LValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */