/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
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
/*     */ public class Instruction
/*     */   implements Constants
/*     */ {
/*     */   long where;
/*     */   int pc;
/*     */   int opc;
/*     */   Object value;
/*     */   Instruction next;
/*     */   boolean flagCondInverted;
/*     */   boolean flagNoCovered = false;
/*     */   public static final double SWITCHRATIO;
/*     */   
/*     */   public Instruction(long paramLong, int paramInt, Object paramObject, boolean paramBoolean) {
/*  58 */     this.where = paramLong;
/*  59 */     this.opc = paramInt;
/*  60 */     this.value = paramObject;
/*  61 */     this.flagCondInverted = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Instruction(boolean paramBoolean, long paramLong, int paramInt, Object paramObject) {
/*  68 */     this.where = paramLong;
/*  69 */     this.opc = paramInt;
/*  70 */     this.value = paramObject;
/*  71 */     this.flagNoCovered = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Instruction(long paramLong, int paramInt, boolean paramBoolean) {
/*  78 */     this.where = paramLong;
/*  79 */     this.opc = paramInt;
/*  80 */     this.flagNoCovered = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Instruction(long paramLong, int paramInt, Object paramObject) {
/*  88 */     this.where = paramLong;
/*  89 */     this.opc = paramInt;
/*  90 */     this.value = paramObject;
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
/*     */   static {
/* 106 */     double d = 1.5D;
/* 107 */     String str = System.getProperty("javac.switchratio");
/* 108 */     if (str != null) {
/*     */       try {
/* 110 */         double d1 = Double.valueOf(str).doubleValue();
/* 111 */         if (!Double.isNaN(d1) && d1 >= 0.0D) {
/* 112 */           d = d1;
/*     */         }
/* 114 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/* 116 */     SWITCHRATIO = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOpcode() {
/* 123 */     return this.pc;
/*     */   }
/*     */   
/*     */   public Object getValue() {
/* 127 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(Object paramObject) {
/* 131 */     this.value = paramObject; } void optimize(Environment paramEnvironment) {
/*     */     Label label;
/*     */     SwitchData switchData;
/*     */     Enumeration<Integer> enumeration;
/*     */     long l1;
/*     */     long l2;
/*     */     long l3;
/*     */     long l4;
/* 139 */     switch (this.opc) { case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/* 144 */         if (this.value instanceof LocalVariable && !paramEnvironment.debug_vars()) {
/* 145 */           this.value = new Integer(((LocalVariable)this.value).slot);
/*     */         }
/*     */         break;
/*     */       
/*     */       case 167:
/* 150 */         label = (Label)this.value;
/* 151 */         this.value = label = label.getDestination();
/* 152 */         if (label == this.next) {
/*     */           
/* 154 */           this.opc = -2;
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
/*     */           break;
/*     */         } 
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
/* 199 */         if (label.next != null && paramEnvironment.opt())
/* 200 */           switch (label.next.opc) { case 172: case 173: case 174:
/*     */             case 175:
/*     */             case 176:
/*     */             case 177:
/* 204 */               this.opc = label.next.opc;
/* 205 */               this.value = label.next.value; break; }
/*     */             break;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 198:
/*     */       case 199:
/* 215 */         this.value = ((Label)this.value).getDestination();
/* 216 */         if (this.value == this.next) {
/*     */           
/* 218 */           this.opc = 87;
/*     */           break;
/*     */         } 
/* 221 */         if (this.next.opc == 167 && this.value == this.next.next)
/*     */         
/*     */         { 
/*     */           
/* 225 */           switch (this.opc) { case 153:
/* 226 */               this.opc = 154; break;
/* 227 */             case 154: this.opc = 153; break;
/* 228 */             case 155: this.opc = 156; break;
/* 229 */             case 158: this.opc = 157; break;
/* 230 */             case 157: this.opc = 158; break;
/* 231 */             case 156: this.opc = 155; break;
/* 232 */             case 198: this.opc = 199; break;
/* 233 */             case 199: this.opc = 198;
/*     */               break; }
/*     */           
/* 236 */           this.flagCondInverted = !this.flagCondInverted;
/*     */           
/* 238 */           this.value = this.next.value;
/* 239 */           this.next.opc = -2; }  break;
/*     */       case 159: case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/* 247 */         this.value = ((Label)this.value).getDestination();
/* 248 */         if (this.value == this.next) {
/*     */           
/* 250 */           this.opc = 88;
/*     */           break;
/*     */         } 
/* 253 */         if (this.next.opc == 167 && this.value == this.next.next) {
/*     */           
/* 255 */           switch (this.opc) { case 165:
/* 256 */               this.opc = 166; break;
/* 257 */             case 166: this.opc = 165; break;
/* 258 */             case 159: this.opc = 160; break;
/* 259 */             case 160: this.opc = 159; break;
/* 260 */             case 163: this.opc = 164; break;
/* 261 */             case 162: this.opc = 161; break;
/* 262 */             case 161: this.opc = 162; break;
/* 263 */             case 164: this.opc = 163;
/*     */               break; }
/*     */           
/* 266 */           this.flagCondInverted = !this.flagCondInverted;
/*     */           
/* 268 */           this.value = this.next.value;
/* 269 */           this.next.opc = -2;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 170:
/*     */       case 171:
/* 275 */         switchData = (SwitchData)this.value;
/* 276 */         switchData.defaultLabel = switchData.defaultLabel.getDestination();
/* 277 */         for (enumeration = switchData.tab.keys(); enumeration.hasMoreElements(); ) {
/* 278 */           Integer integer = enumeration.nextElement();
/* 279 */           Label label1 = switchData.tab.get(integer);
/* 280 */           switchData.tab.put(integer, label1.getDestination());
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 286 */         l1 = switchData.maxValue - switchData.minValue + 1L;
/* 287 */         l2 = switchData.tab.size();
/*     */         
/* 289 */         l3 = 4L + l1;
/* 290 */         l4 = 3L + 2L * l2;
/*     */         
/* 292 */         if (l3 <= l4 * SWITCHRATIO) {
/* 293 */           this.opc = 170; break;
/*     */         } 
/* 295 */         this.opc = 171;
/*     */         break; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void collect(ConstantPool paramConstantPool) {
/*     */     Enumeration<CatchData> enumeration;
/* 307 */     switch (this.opc) { case 54: case 55: case 56:
/*     */       case 57:
/*     */       case 58:
/* 310 */         if (this.value instanceof LocalVariable)
/* 311 */         { MemberDefinition memberDefinition = ((LocalVariable)this.value).field;
/* 312 */           paramConstantPool.put(memberDefinition.getName().toString());
/* 313 */           paramConstantPool.put(memberDefinition.getType().getTypeSignature()); }  return;
/*     */       case 178: case 179: case 180:
/*     */       case 181:
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/*     */       case 185:
/*     */       case 187:
/*     */       case 192:
/*     */       case 193:
/* 323 */         paramConstantPool.put(this.value);
/*     */         return;
/*     */       
/*     */       case 189:
/* 327 */         paramConstantPool.put(this.value);
/*     */         return;
/*     */       
/*     */       case 197:
/* 331 */         paramConstantPool.put(((ArrayData)this.value).type);
/*     */         return;
/*     */       
/*     */       case 18:
/*     */       case 19:
/* 336 */         if (this.value instanceof Integer) {
/* 337 */           int i = ((Integer)this.value).intValue();
/* 338 */           if (i >= -1 && i <= 5) {
/* 339 */             this.opc = 3 + i; return;
/*     */           } 
/* 341 */           if (i >= -128 && i < 128) {
/* 342 */             this.opc = 16; return;
/*     */           } 
/* 344 */           if (i >= -32768 && i < 32768) {
/* 345 */             this.opc = 17;
/*     */             return;
/*     */           } 
/* 348 */         } else if (this.value instanceof Float) {
/* 349 */           float f = ((Float)this.value).floatValue();
/* 350 */           if (f == 0.0F) {
/* 351 */             if (Float.floatToIntBits(f) == 0) {
/* 352 */               this.opc = 11; return;
/*     */             } 
/*     */           } else {
/* 355 */             if (f == 1.0F) {
/* 356 */               this.opc = 12; return;
/*     */             } 
/* 358 */             if (f == 2.0F) {
/* 359 */               this.opc = 13; return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 363 */         paramConstantPool.put(this.value);
/*     */         return;
/*     */       
/*     */       case 20:
/* 367 */         if (this.value instanceof Long) {
/* 368 */           long l = ((Long)this.value).longValue();
/* 369 */           if (l == 0L) {
/* 370 */             this.opc = 9; return;
/*     */           } 
/* 372 */           if (l == 1L) {
/* 373 */             this.opc = 10;
/*     */             return;
/*     */           } 
/* 376 */         } else if (this.value instanceof Double) {
/* 377 */           double d = ((Double)this.value).doubleValue();
/* 378 */           if (d == 0.0D) {
/* 379 */             if (Double.doubleToLongBits(d) == 0L) {
/* 380 */               this.opc = 14;
/*     */               return;
/*     */             } 
/* 383 */           } else if (d == 1.0D) {
/* 384 */             this.opc = 15;
/*     */             return;
/*     */           } 
/*     */         } 
/* 388 */         paramConstantPool.put(this.value);
/*     */         return;
/*     */       
/*     */       case -3:
/* 392 */         for (enumeration = ((TryData)this.value).catches.elements(); enumeration.hasMoreElements(); ) {
/* 393 */           CatchData catchData = enumeration.nextElement();
/* 394 */           if (catchData.getType() != null) {
/* 395 */             paramConstantPool.put(catchData.getType());
/*     */           }
/*     */         } 
/*     */         return;
/*     */       
/*     */       case 0:
/* 401 */         if (this.value != null && this.value instanceof sun.tools.java.ClassDeclaration) {
/* 402 */           paramConstantPool.put(this.value);
/*     */         }
/*     */         return; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int balance() {
/* 411 */     switch (this.opc) { case -3: case -2: case -1: case 0: case 47: case 49: case 95: case 116: case 117: case 118: case 119: case 132: case 134: case 138: case 139: case 143: case 145: case 146: case 147: case 167:
/*     */       case 168:
/*     */       case 169:
/*     */       case 177:
/*     */       case 188:
/*     */       case 189:
/*     */       case 190:
/*     */       case 192:
/*     */       case 193:
/*     */       case 200:
/*     */       case 201:
/* 422 */         return 0;
/*     */       case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 11: case 12: case 13: case 16: case 17: case 18: case 19: case 21: case 23:
/*     */       case 25:
/*     */       case 89:
/*     */       case 90:
/*     */       case 91:
/*     */       case 133:
/*     */       case 135:
/*     */       case 140:
/*     */       case 141:
/*     */       case 187:
/* 433 */         return 1;
/*     */       case 9: case 10: case 14: case 15: case 20: case 22:
/*     */       case 24:
/*     */       case 92:
/*     */       case 93:
/*     */       case 94:
/* 439 */         return 2;
/*     */       case 46: case 48: case 50: case 51: case 52: case 53: case 54: case 56: case 58: case 87: case 96: case 98: case 100: case 102: case 104: case 106: case 108: case 110: case 112: case 114: case 120: case 121: case 122: case 123: case 124: case 125: case 126: case 128: case 130: case 136: case 137: case 142: case 144: case 149:
/*     */       case 150:
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 170:
/*     */       case 171:
/*     */       case 172:
/*     */       case 174:
/*     */       case 176:
/*     */       case 191:
/*     */       case 194:
/*     */       case 195:
/*     */       case 198:
/*     */       case 199:
/* 458 */         return -1;
/*     */       case 55: case 57: case 88: case 97: case 99: case 101: case 103: case 105: case 107: case 109: case 111: case 113: case 115: case 127: case 129: case 131: case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 173:
/*     */       case 175:
/* 469 */         return -2;
/*     */       case 79: case 81: case 83: case 84: case 85: case 86:
/*     */       case 148:
/*     */       case 151:
/*     */       case 152:
/* 474 */         return -3;
/*     */       case 80:
/*     */       case 82:
/* 477 */         return -4;
/*     */       
/*     */       case 197:
/* 480 */         return 1 - ((ArrayData)this.value).nargs;
/*     */       
/*     */       case 180:
/* 483 */         return ((MemberDefinition)this.value).getType().stackSize() - 1;
/*     */       
/*     */       case 181:
/* 486 */         return -1 - ((MemberDefinition)this.value).getType().stackSize();
/*     */       
/*     */       case 178:
/* 489 */         return ((MemberDefinition)this.value).getType().stackSize();
/*     */       
/*     */       case 179:
/* 492 */         return -((MemberDefinition)this.value).getType().stackSize();
/*     */       
/*     */       case 182:
/*     */       case 183:
/*     */       case 185:
/* 497 */         return ((MemberDefinition)this.value).getType().getReturnType().stackSize() - ((MemberDefinition)this.value)
/* 498 */           .getType().stackSize() + 1;
/*     */       
/*     */       case 184:
/* 501 */         return ((MemberDefinition)this.value).getType().getReturnType().stackSize() - ((MemberDefinition)this.value)
/* 502 */           .getType().stackSize(); }
/*     */     
/* 504 */     throw new CompilerError("invalid opcode: " + toString());
/*     */   }
/*     */   
/*     */   int size(ConstantPool paramConstantPool) {
/*     */     int i;
/*     */     SwitchData switchData;
/*     */     int j;
/* 511 */     switch (this.opc) { case -3: case -2:
/*     */       case -1:
/* 513 */         return 0;
/*     */       case 16:
/*     */       case 188:
/* 516 */         return 2;
/*     */       case 17: case 153: case 154: case 155: case 156: case 157: case 158: case 159: case 160: case 161: case 162: case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 167:
/*     */       case 168:
/*     */       case 198:
/*     */       case 199:
/* 525 */         return 3;
/*     */       
/*     */       case 18:
/*     */       case 19:
/* 529 */         if (paramConstantPool.index(this.value) < 256) {
/* 530 */           this.opc = 18;
/* 531 */           return 2;
/*     */         } 
/* 533 */         this.opc = 19;
/* 534 */         return 3;
/*     */       case 21: case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/* 539 */         i = ((Number)this.value).intValue();
/* 540 */         if (i < 4) {
/* 541 */           if (i < 0) {
/* 542 */             throw new CompilerError("invalid slot: " + toString() + "\nThis error possibly resulted from poorly constructed class paths.");
/*     */           }
/*     */           
/* 545 */           this.opc = 26 + (this.opc - 21) * 4 + i;
/* 546 */           return 1;
/* 547 */         }  if (i <= 255) {
/* 548 */           return 2;
/*     */         }
/* 550 */         this.opc += 256;
/* 551 */         return 4;
/*     */ 
/*     */ 
/*     */       
/*     */       case 132:
/* 556 */         i = ((int[])this.value)[0];
/* 557 */         j = ((int[])this.value)[1];
/* 558 */         if (i < 0) {
/* 559 */           throw new CompilerError("invalid slot: " + toString());
/*     */         }
/* 561 */         if (i <= 255 && (byte)j == j) {
/* 562 */           return 3;
/*     */         }
/* 564 */         this.opc += 256;
/* 565 */         return 6;
/*     */       
/*     */       case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/* 572 */         i = (this.value instanceof Number) ? ((Number)this.value).intValue() : ((LocalVariable)this.value).slot;
/* 573 */         if (i < 4) {
/* 574 */           if (i < 0) {
/* 575 */             throw new CompilerError("invalid slot: " + toString());
/*     */           }
/* 577 */           this.opc = 59 + (this.opc - 54) * 4 + i;
/* 578 */           return 1;
/* 579 */         }  if (i <= 255) {
/* 580 */           return 2;
/*     */         }
/* 582 */         this.opc += 256;
/* 583 */         return 4;
/*     */ 
/*     */ 
/*     */       
/*     */       case 169:
/* 588 */         i = ((Number)this.value).intValue();
/* 589 */         if (i <= 255) {
/* 590 */           if (i < 0) {
/* 591 */             throw new CompilerError("invalid slot: " + toString());
/*     */           }
/* 593 */           return 2;
/*     */         } 
/* 595 */         this.opc += 256;
/* 596 */         return 4;
/*     */       case 20: case 178: case 179: case 180:
/*     */       case 181:
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/*     */       case 187:
/*     */       case 189:
/*     */       case 192:
/*     */       case 193:
/* 606 */         return 3;
/*     */       
/*     */       case 197:
/* 609 */         return 4;
/*     */       
/*     */       case 185:
/*     */       case 200:
/*     */       case 201:
/* 614 */         return 5;
/*     */       
/*     */       case 170:
/* 617 */         switchData = (SwitchData)this.value;
/* 618 */         j = 1;
/* 619 */         for (; (this.pc + j) % 4 != 0; j++);
/* 620 */         return j + 16 + (switchData.maxValue - switchData.minValue) * 4;
/*     */ 
/*     */       
/*     */       case 171:
/* 624 */         switchData = (SwitchData)this.value;
/* 625 */         j = 1;
/* 626 */         for (; (this.pc + j) % 4 != 0; j++);
/* 627 */         return j + 8 + switchData.tab.size() * 8;
/*     */ 
/*     */       
/*     */       case 0:
/* 631 */         if (this.value != null && !(this.value instanceof Integer)) {
/* 632 */           return 2;
/*     */         }
/* 634 */         return 1; }
/*     */ 
/*     */ 
/*     */     
/* 638 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   void write(DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/*     */     SwitchData switchData;
/*     */     int i;
/*     */     Enumeration<Integer> enumeration;
/* 646 */     switch (this.opc) { case -3: case -2: case -1: return;
/*     */       case 16: case 21: case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 169:
/*     */       case 188:
/* 653 */         paramDataOutputStream.writeByte(this.opc);
/* 654 */         paramDataOutputStream.writeByte(((Number)this.value).intValue());
/*     */       case 277: case 278:
/*     */       case 279:
/*     */       case 280:
/*     */       case 281:
/*     */       case 425:
/* 660 */         paramDataOutputStream.writeByte(196);
/* 661 */         paramDataOutputStream.writeByte(this.opc - 256);
/* 662 */         paramDataOutputStream.writeShort(((Number)this.value).intValue());
/*     */       case 54: case 55:
/*     */       case 56:
/*     */       case 57:
/*     */       case 58:
/* 667 */         paramDataOutputStream.writeByte(this.opc);
/* 668 */         paramDataOutputStream.writeByte((this.value instanceof Number) ? ((Number)this.value)
/* 669 */             .intValue() : ((LocalVariable)this.value).slot);
/*     */       case 310:
/*     */       case 311:
/*     */       case 312:
/*     */       case 313:
/*     */       case 314:
/* 675 */         paramDataOutputStream.writeByte(196);
/* 676 */         paramDataOutputStream.writeByte(this.opc - 256);
/* 677 */         paramDataOutputStream.writeShort((this.value instanceof Number) ? ((Number)this.value)
/* 678 */             .intValue() : ((LocalVariable)this.value).slot);
/*     */ 
/*     */       
/*     */       case 17:
/* 682 */         paramDataOutputStream.writeByte(this.opc);
/* 683 */         paramDataOutputStream.writeShort(((Number)this.value).intValue());
/*     */ 
/*     */       
/*     */       case 18:
/* 687 */         paramDataOutputStream.writeByte(this.opc);
/* 688 */         paramDataOutputStream.writeByte(paramConstantPool.index(this.value));
/*     */       case 19: case 20: case 178: case 179: case 180:
/*     */       case 181:
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/*     */       case 187:
/*     */       case 192:
/*     */       case 193:
/* 697 */         paramDataOutputStream.writeByte(this.opc);
/* 698 */         paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/*     */ 
/*     */       
/*     */       case 132:
/* 702 */         paramDataOutputStream.writeByte(this.opc);
/* 703 */         paramDataOutputStream.writeByte(((int[])this.value)[0]);
/* 704 */         paramDataOutputStream.writeByte(((int[])this.value)[1]);
/*     */ 
/*     */       
/*     */       case 388:
/* 708 */         paramDataOutputStream.writeByte(196);
/* 709 */         paramDataOutputStream.writeByte(this.opc - 256);
/* 710 */         paramDataOutputStream.writeShort(((int[])this.value)[0]);
/* 711 */         paramDataOutputStream.writeShort(((int[])this.value)[1]);
/*     */ 
/*     */       
/*     */       case 189:
/* 715 */         paramDataOutputStream.writeByte(this.opc);
/* 716 */         paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/*     */ 
/*     */       
/*     */       case 197:
/* 720 */         paramDataOutputStream.writeByte(this.opc);
/* 721 */         paramDataOutputStream.writeShort(paramConstantPool.index(((ArrayData)this.value).type));
/* 722 */         paramDataOutputStream.writeByte(((ArrayData)this.value).nargs);
/*     */ 
/*     */       
/*     */       case 185:
/* 726 */         paramDataOutputStream.writeByte(this.opc);
/* 727 */         paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/* 728 */         paramDataOutputStream.writeByte(((MemberDefinition)this.value).getType().stackSize() + 1);
/* 729 */         paramDataOutputStream.writeByte(0);
/*     */       case 153: case 154: case 155: case 156: case 157: case 158: case 159: case 160: case 161: case 162: case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 167:
/*     */       case 168:
/*     */       case 198:
/*     */       case 199:
/* 738 */         paramDataOutputStream.writeByte(this.opc);
/* 739 */         paramDataOutputStream.writeShort(((Instruction)this.value).pc - this.pc);
/*     */ 
/*     */       
/*     */       case 200:
/*     */       case 201:
/* 744 */         paramDataOutputStream.writeByte(this.opc);
/* 745 */         paramDataOutputStream.writeLong((((Instruction)this.value).pc - this.pc));
/*     */ 
/*     */       
/*     */       case 170:
/* 749 */         switchData = (SwitchData)this.value;
/* 750 */         paramDataOutputStream.writeByte(this.opc);
/* 751 */         for (i = 1; (this.pc + i) % 4 != 0; i++) {
/* 752 */           paramDataOutputStream.writeByte(0);
/*     */         }
/* 754 */         paramDataOutputStream.writeInt(switchData.defaultLabel.pc - this.pc);
/* 755 */         paramDataOutputStream.writeInt(switchData.minValue);
/* 756 */         paramDataOutputStream.writeInt(switchData.maxValue);
/* 757 */         for (i = switchData.minValue; i <= switchData.maxValue; i++) {
/* 758 */           Label label = switchData.get(i);
/* 759 */           int j = (label != null) ? label.pc : switchData.defaultLabel.pc;
/* 760 */           paramDataOutputStream.writeInt(j - this.pc);
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       case 171:
/* 766 */         switchData = (SwitchData)this.value;
/* 767 */         paramDataOutputStream.writeByte(this.opc);
/* 768 */         i = this.pc + 1;
/* 769 */         for (; i % 4 != 0; i++) {
/* 770 */           paramDataOutputStream.writeByte(0);
/*     */         }
/* 772 */         paramDataOutputStream.writeInt(switchData.defaultLabel.pc - this.pc);
/* 773 */         paramDataOutputStream.writeInt(switchData.tab.size());
/* 774 */         for (enumeration = switchData.sortedKeys(); enumeration.hasMoreElements(); ) {
/* 775 */           Integer integer = enumeration.nextElement();
/* 776 */           paramDataOutputStream.writeInt(integer.intValue());
/* 777 */           paramDataOutputStream.writeInt((switchData.get(integer)).pc - this.pc);
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 783 */         if (this.value != null) {
/* 784 */           if (this.value instanceof Integer) {
/* 785 */             paramDataOutputStream.writeByte(((Integer)this.value).intValue());
/*     */           } else {
/* 787 */             paramDataOutputStream.writeShort(paramConstantPool.index(this.value));
/*     */           } 
/*     */           return;
/*     */         } 
/*     */         break; }
/*     */     
/* 793 */     paramDataOutputStream.writeByte(this.opc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     int i, j;
/* 802 */     String str = (this.where >> 32L) + ":\t";
/* 803 */     switch (this.opc) {
/*     */       case -3:
/* 805 */         return str + "try " + ((TryData)this.value).getEndLabel().hashCode();
/*     */       
/*     */       case -2:
/* 808 */         return str + "dead";
/*     */       
/*     */       case 132:
/* 811 */         i = ((int[])this.value)[0];
/* 812 */         j = ((int[])this.value)[1];
/* 813 */         return str + opcNames[this.opc] + " " + i + ", " + j;
/*     */     } 
/*     */ 
/*     */     
/* 817 */     if (this.value != null) {
/* 818 */       if (this.value instanceof Label)
/* 819 */         return str + opcNames[this.opc] + " " + this.value.toString(); 
/* 820 */       if (this.value instanceof Instruction)
/* 821 */         return str + opcNames[this.opc] + " " + this.value.hashCode(); 
/* 822 */       if (this.value instanceof String) {
/* 823 */         return str + opcNames[this.opc] + " \"" + this.value + "\"";
/*     */       }
/* 825 */       return str + opcNames[this.opc] + " " + this.value;
/*     */     } 
/*     */     
/* 828 */     return str + opcNames[this.opc];
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\Instruction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */