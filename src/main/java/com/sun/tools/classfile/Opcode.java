/*     */ package com.sun.tools.classfile;
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
/*     */ public enum Opcode
/*     */ {
/*  43 */   NOP(0),
/*  44 */   ACONST_NULL(1),
/*  45 */   ICONST_M1(2),
/*  46 */   ICONST_0(3),
/*  47 */   ICONST_1(4),
/*  48 */   ICONST_2(5),
/*  49 */   ICONST_3(6),
/*  50 */   ICONST_4(7),
/*  51 */   ICONST_5(8),
/*  52 */   LCONST_0(9),
/*  53 */   LCONST_1(10),
/*  54 */   FCONST_0(11),
/*  55 */   FCONST_1(12),
/*  56 */   FCONST_2(13),
/*  57 */   DCONST_0(14),
/*  58 */   DCONST_1(15),
/*  59 */   BIPUSH(16, Instruction.Kind.BYTE),
/*  60 */   SIPUSH(17, Instruction.Kind.SHORT),
/*  61 */   LDC(18, Instruction.Kind.CPREF),
/*  62 */   LDC_W(19, Instruction.Kind.CPREF_W),
/*  63 */   LDC2_W(20, Instruction.Kind.CPREF_W),
/*  64 */   ILOAD(21, Instruction.Kind.LOCAL),
/*  65 */   LLOAD(22, Instruction.Kind.LOCAL),
/*  66 */   FLOAD(23, Instruction.Kind.LOCAL),
/*  67 */   DLOAD(24, Instruction.Kind.LOCAL),
/*  68 */   ALOAD(25, Instruction.Kind.LOCAL),
/*  69 */   ILOAD_0(26),
/*  70 */   ILOAD_1(27),
/*  71 */   ILOAD_2(28),
/*  72 */   ILOAD_3(29),
/*  73 */   LLOAD_0(30),
/*  74 */   LLOAD_1(31),
/*  75 */   LLOAD_2(32),
/*  76 */   LLOAD_3(33),
/*  77 */   FLOAD_0(34),
/*  78 */   FLOAD_1(35),
/*  79 */   FLOAD_2(36),
/*  80 */   FLOAD_3(37),
/*  81 */   DLOAD_0(38),
/*  82 */   DLOAD_1(39),
/*  83 */   DLOAD_2(40),
/*  84 */   DLOAD_3(41),
/*  85 */   ALOAD_0(42),
/*  86 */   ALOAD_1(43),
/*  87 */   ALOAD_2(44),
/*  88 */   ALOAD_3(45),
/*  89 */   IALOAD(46),
/*  90 */   LALOAD(47),
/*  91 */   FALOAD(48),
/*  92 */   DALOAD(49),
/*  93 */   AALOAD(50),
/*  94 */   BALOAD(51),
/*  95 */   CALOAD(52),
/*  96 */   SALOAD(53),
/*  97 */   ISTORE(54, Instruction.Kind.LOCAL),
/*  98 */   LSTORE(55, Instruction.Kind.LOCAL),
/*  99 */   FSTORE(56, Instruction.Kind.LOCAL),
/* 100 */   DSTORE(57, Instruction.Kind.LOCAL),
/* 101 */   ASTORE(58, Instruction.Kind.LOCAL),
/* 102 */   ISTORE_0(59),
/* 103 */   ISTORE_1(60),
/* 104 */   ISTORE_2(61),
/* 105 */   ISTORE_3(62),
/* 106 */   LSTORE_0(63),
/* 107 */   LSTORE_1(64),
/* 108 */   LSTORE_2(65),
/* 109 */   LSTORE_3(66),
/* 110 */   FSTORE_0(67),
/* 111 */   FSTORE_1(68),
/* 112 */   FSTORE_2(69),
/* 113 */   FSTORE_3(70),
/* 114 */   DSTORE_0(71),
/* 115 */   DSTORE_1(72),
/* 116 */   DSTORE_2(73),
/* 117 */   DSTORE_3(74),
/* 118 */   ASTORE_0(75),
/* 119 */   ASTORE_1(76),
/* 120 */   ASTORE_2(77),
/* 121 */   ASTORE_3(78),
/* 122 */   IASTORE(79),
/* 123 */   LASTORE(80),
/* 124 */   FASTORE(81),
/* 125 */   DASTORE(82),
/* 126 */   AASTORE(83),
/* 127 */   BASTORE(84),
/* 128 */   CASTORE(85),
/* 129 */   SASTORE(86),
/* 130 */   POP(87),
/* 131 */   POP2(88),
/* 132 */   DUP(89),
/* 133 */   DUP_X1(90),
/* 134 */   DUP_X2(91),
/* 135 */   DUP2(92),
/* 136 */   DUP2_X1(93),
/* 137 */   DUP2_X2(94),
/* 138 */   SWAP(95),
/* 139 */   IADD(96),
/* 140 */   LADD(97),
/* 141 */   FADD(98),
/* 142 */   DADD(99),
/* 143 */   ISUB(100),
/* 144 */   LSUB(101),
/* 145 */   FSUB(102),
/* 146 */   DSUB(103),
/* 147 */   IMUL(104),
/* 148 */   LMUL(105),
/* 149 */   FMUL(106),
/* 150 */   DMUL(107),
/* 151 */   IDIV(108),
/* 152 */   LDIV(109),
/* 153 */   FDIV(110),
/* 154 */   DDIV(111),
/* 155 */   IREM(112),
/* 156 */   LREM(113),
/* 157 */   FREM(114),
/* 158 */   DREM(115),
/* 159 */   INEG(116),
/* 160 */   LNEG(117),
/* 161 */   FNEG(118),
/* 162 */   DNEG(119),
/* 163 */   ISHL(120),
/* 164 */   LSHL(121),
/* 165 */   ISHR(122),
/* 166 */   LSHR(123),
/* 167 */   IUSHR(124),
/* 168 */   LUSHR(125),
/* 169 */   IAND(126),
/* 170 */   LAND(127),
/* 171 */   IOR(128),
/* 172 */   LOR(129),
/* 173 */   IXOR(130),
/* 174 */   LXOR(131),
/* 175 */   IINC(132, Instruction.Kind.LOCAL_BYTE),
/* 176 */   I2L(133),
/* 177 */   I2F(134),
/* 178 */   I2D(135),
/* 179 */   L2I(136),
/* 180 */   L2F(137),
/* 181 */   L2D(138),
/* 182 */   F2I(139),
/* 183 */   F2L(140),
/* 184 */   F2D(141),
/* 185 */   D2I(142),
/* 186 */   D2L(143),
/* 187 */   D2F(144),
/* 188 */   I2B(145),
/* 189 */   I2C(146),
/* 190 */   I2S(147),
/* 191 */   LCMP(148),
/* 192 */   FCMPL(149),
/* 193 */   FCMPG(150),
/* 194 */   DCMPL(151),
/* 195 */   DCMPG(152),
/* 196 */   IFEQ(153, Instruction.Kind.BRANCH),
/* 197 */   IFNE(154, Instruction.Kind.BRANCH),
/* 198 */   IFLT(155, Instruction.Kind.BRANCH),
/* 199 */   IFGE(156, Instruction.Kind.BRANCH),
/* 200 */   IFGT(157, Instruction.Kind.BRANCH),
/* 201 */   IFLE(158, Instruction.Kind.BRANCH),
/* 202 */   IF_ICMPEQ(159, Instruction.Kind.BRANCH),
/* 203 */   IF_ICMPNE(160, Instruction.Kind.BRANCH),
/* 204 */   IF_ICMPLT(161, Instruction.Kind.BRANCH),
/* 205 */   IF_ICMPGE(162, Instruction.Kind.BRANCH),
/* 206 */   IF_ICMPGT(163, Instruction.Kind.BRANCH),
/* 207 */   IF_ICMPLE(164, Instruction.Kind.BRANCH),
/* 208 */   IF_ACMPEQ(165, Instruction.Kind.BRANCH),
/* 209 */   IF_ACMPNE(166, Instruction.Kind.BRANCH),
/* 210 */   GOTO(167, Instruction.Kind.BRANCH),
/* 211 */   JSR(168, Instruction.Kind.BRANCH),
/* 212 */   RET(169, Instruction.Kind.LOCAL),
/* 213 */   TABLESWITCH(170, Instruction.Kind.DYNAMIC),
/* 214 */   LOOKUPSWITCH(171, Instruction.Kind.DYNAMIC),
/* 215 */   IRETURN(172),
/* 216 */   LRETURN(173),
/* 217 */   FRETURN(174),
/* 218 */   DRETURN(175),
/* 219 */   ARETURN(176),
/* 220 */   RETURN(177),
/* 221 */   GETSTATIC(178, Instruction.Kind.CPREF_W),
/* 222 */   PUTSTATIC(179, Instruction.Kind.CPREF_W),
/* 223 */   GETFIELD(180, Instruction.Kind.CPREF_W),
/* 224 */   PUTFIELD(181, Instruction.Kind.CPREF_W),
/* 225 */   INVOKEVIRTUAL(182, Instruction.Kind.CPREF_W),
/* 226 */   INVOKESPECIAL(183, Instruction.Kind.CPREF_W),
/* 227 */   INVOKESTATIC(184, Instruction.Kind.CPREF_W),
/* 228 */   INVOKEINTERFACE(185, Instruction.Kind.CPREF_W_UBYTE_ZERO),
/* 229 */   INVOKEDYNAMIC(186, Instruction.Kind.CPREF_W_UBYTE_ZERO),
/* 230 */   NEW(187, Instruction.Kind.CPREF_W),
/* 231 */   NEWARRAY(188, Instruction.Kind.ATYPE),
/* 232 */   ANEWARRAY(189, Instruction.Kind.CPREF_W),
/* 233 */   ARRAYLENGTH(190),
/* 234 */   ATHROW(191),
/* 235 */   CHECKCAST(192, Instruction.Kind.CPREF_W),
/* 236 */   INSTANCEOF(193, Instruction.Kind.CPREF_W),
/* 237 */   MONITORENTER(194),
/* 238 */   MONITOREXIT(195),
/*     */   
/* 240 */   MULTIANEWARRAY(197, Instruction.Kind.CPREF_W_UBYTE),
/* 241 */   IFNULL(198, Instruction.Kind.BRANCH),
/* 242 */   IFNONNULL(199, Instruction.Kind.BRANCH),
/* 243 */   GOTO_W(200, Instruction.Kind.BRANCH_W),
/* 244 */   JSR_W(201, Instruction.Kind.BRANCH_W),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   ILOAD_W(50197, Instruction.Kind.WIDE_LOCAL),
/* 250 */   LLOAD_W(50198, Instruction.Kind.WIDE_LOCAL),
/* 251 */   FLOAD_W(50199, Instruction.Kind.WIDE_LOCAL),
/* 252 */   DLOAD_W(50200, Instruction.Kind.WIDE_LOCAL),
/* 253 */   ALOAD_W(50201, Instruction.Kind.WIDE_LOCAL),
/* 254 */   ISTORE_W(50230, Instruction.Kind.WIDE_LOCAL),
/* 255 */   LSTORE_W(50231, Instruction.Kind.WIDE_LOCAL),
/* 256 */   FSTORE_W(50232, Instruction.Kind.WIDE_LOCAL),
/* 257 */   DSTORE_W(50233, Instruction.Kind.WIDE_LOCAL),
/* 258 */   ASTORE_W(50234, Instruction.Kind.WIDE_LOCAL),
/* 259 */   IINC_W(50308, Instruction.Kind.WIDE_LOCAL_SHORT),
/* 260 */   RET_W(50345, Instruction.Kind.WIDE_LOCAL),
/*     */ 
/*     */   
/* 263 */   LOAD_UBYTE(Set.PICOJAVA, 65024),
/* 264 */   LOAD_BYTE(Set.PICOJAVA, 65025),
/* 265 */   LOAD_CHAR(Set.PICOJAVA, 65026),
/* 266 */   LOAD_SHORT(Set.PICOJAVA, 65027),
/* 267 */   LOAD_WORD(Set.PICOJAVA, 65028),
/* 268 */   RET_FROM_SUB(Set.PICOJAVA, 65029),
/* 269 */   LOAD_CHAR_OE(Set.PICOJAVA, 65034),
/* 270 */   LOAD_SHORT_OE(Set.PICOJAVA, 65035),
/* 271 */   LOAD_WORD_OE(Set.PICOJAVA, 65036),
/* 272 */   NCLOAD_UBYTE(Set.PICOJAVA, 65040),
/* 273 */   NCLOAD_BYTE(Set.PICOJAVA, 65041),
/* 274 */   NCLOAD_CHAR(Set.PICOJAVA, 65042),
/* 275 */   NCLOAD_SHORT(Set.PICOJAVA, 65043),
/* 276 */   NCLOAD_WORD(Set.PICOJAVA, 65044),
/* 277 */   NCLOAD_CHAR_OE(Set.PICOJAVA, 65050),
/* 278 */   NCLOAD_SHORT_OE(Set.PICOJAVA, 65051),
/* 279 */   NCLOAD_WORD_OE(Set.PICOJAVA, 65052),
/* 280 */   CACHE_FLUSH(Set.PICOJAVA, 65054),
/* 281 */   STORE_BYTE(Set.PICOJAVA, 65056),
/* 282 */   STORE_SHORT(Set.PICOJAVA, 65058),
/* 283 */   STORE_WORD(Set.PICOJAVA, 65060),
/* 284 */   STORE_SHORT_OE(Set.PICOJAVA, 65066),
/* 285 */   STORE_WORD_OE(Set.PICOJAVA, 65068),
/* 286 */   NCSTORE_BYTE(Set.PICOJAVA, 65072),
/* 287 */   NCSTORE_SHORT(Set.PICOJAVA, 65074),
/* 288 */   NCSTORE_WORD(Set.PICOJAVA, 65076),
/* 289 */   NCSTORE_SHORT_OE(Set.PICOJAVA, 65082),
/* 290 */   NCSTORE_WORD_OE(Set.PICOJAVA, 65084),
/* 291 */   ZERO_LINE(Set.PICOJAVA, 65086),
/* 292 */   ENTER_SYNC_METHOD(Set.PICOJAVA, 65087),
/*     */ 
/*     */   
/* 295 */   PRIV_LOAD_UBYTE(Set.PICOJAVA, 65280),
/* 296 */   PRIV_LOAD_BYTE(Set.PICOJAVA, 65281),
/* 297 */   PRIV_LOAD_CHAR(Set.PICOJAVA, 65282),
/* 298 */   PRIV_LOAD_SHORT(Set.PICOJAVA, 65283),
/* 299 */   PRIV_LOAD_WORD(Set.PICOJAVA, 65284),
/* 300 */   PRIV_RET_FROM_TRAP(Set.PICOJAVA, 65285),
/* 301 */   PRIV_READ_DCACHE_TAG(Set.PICOJAVA, 65286),
/* 302 */   PRIV_READ_DCACHE_DATA(Set.PICOJAVA, 65287),
/* 303 */   PRIV_LOAD_CHAR_OE(Set.PICOJAVA, 65290),
/* 304 */   PRIV_LOAD_SHORT_OE(Set.PICOJAVA, 65291),
/* 305 */   PRIV_LOAD_WORD_OE(Set.PICOJAVA, 65292),
/* 306 */   PRIV_READ_ICACHE_TAG(Set.PICOJAVA, 65294),
/* 307 */   PRIV_READ_ICACHE_DATA(Set.PICOJAVA, 65295),
/* 308 */   PRIV_NCLOAD_UBYTE(Set.PICOJAVA, 65296),
/* 309 */   PRIV_NCLOAD_BYTE(Set.PICOJAVA, 65297),
/* 310 */   PRIV_NCLOAD_CHAR(Set.PICOJAVA, 65298),
/* 311 */   PRIV_NCLOAD_SHORT(Set.PICOJAVA, 65299),
/* 312 */   PRIV_NCLOAD_WORD(Set.PICOJAVA, 65300),
/* 313 */   PRIV_POWERDOWN(Set.PICOJAVA, 65302),
/* 314 */   PRIV_READ_SCACHE_DATA(Set.PICOJAVA, 65303),
/* 315 */   PRIV_NCLOAD_CHAR_OE(Set.PICOJAVA, 65306),
/* 316 */   PRIV_NCLOAD_SHORT_OE(Set.PICOJAVA, 65307),
/* 317 */   PRIV_NCLOAD_WORD_OE(Set.PICOJAVA, 65308),
/* 318 */   PRIV_CACHE_FLUSH(Set.PICOJAVA, 65310),
/* 319 */   PRIV_CACHE_INDEX_FLUSH(Set.PICOJAVA, 65311),
/* 320 */   PRIV_STORE_BYTE(Set.PICOJAVA, 65312),
/* 321 */   PRIV_STORE_SHORT(Set.PICOJAVA, 65314),
/* 322 */   PRIV_STORE_WORD(Set.PICOJAVA, 65316),
/* 323 */   PRIV_WRITE_DCACHE_TAG(Set.PICOJAVA, 65318),
/* 324 */   PRIV_WRITE_DCACHE_DATA(Set.PICOJAVA, 65319),
/* 325 */   PRIV_STORE_SHORT_OE(Set.PICOJAVA, 65322),
/* 326 */   PRIV_STORE_WORD_OE(Set.PICOJAVA, 65324),
/* 327 */   PRIV_WRITE_ICACHE_TAG(Set.PICOJAVA, 65326),
/* 328 */   PRIV_WRITE_ICACHE_DATA(Set.PICOJAVA, 65327),
/* 329 */   PRIV_NCSTORE_BYTE(Set.PICOJAVA, 65328),
/* 330 */   PRIV_NCSTORE_SHORT(Set.PICOJAVA, 65330),
/* 331 */   PRIV_NCSTORE_WORD(Set.PICOJAVA, 65332),
/* 332 */   PRIV_RESET(Set.PICOJAVA, 65334),
/* 333 */   PRIV_WRITE_SCACHE_DATA(Set.PICOJAVA, 65335),
/* 334 */   PRIV_NCSTORE_SHORT_OE(Set.PICOJAVA, 65338),
/* 335 */   PRIV_NCSTORE_WORD_OE(Set.PICOJAVA, 65340),
/* 336 */   PRIV_ZERO_LINE(Set.PICOJAVA, 65342),
/* 337 */   PRIV_READ_REG_0(Set.PICOJAVA, 65344),
/* 338 */   PRIV_READ_REG_1(Set.PICOJAVA, 65345),
/* 339 */   PRIV_READ_REG_2(Set.PICOJAVA, 65346),
/* 340 */   PRIV_READ_REG_3(Set.PICOJAVA, 65347),
/* 341 */   PRIV_READ_REG_4(Set.PICOJAVA, 65348),
/* 342 */   PRIV_READ_REG_5(Set.PICOJAVA, 65349),
/* 343 */   PRIV_READ_REG_6(Set.PICOJAVA, 65350),
/* 344 */   PRIV_READ_REG_7(Set.PICOJAVA, 65351),
/* 345 */   PRIV_READ_REG_8(Set.PICOJAVA, 65352),
/* 346 */   PRIV_READ_REG_9(Set.PICOJAVA, 65353),
/* 347 */   PRIV_READ_REG_10(Set.PICOJAVA, 65354),
/* 348 */   PRIV_READ_REG_11(Set.PICOJAVA, 65355),
/* 349 */   PRIV_READ_REG_12(Set.PICOJAVA, 65356),
/* 350 */   PRIV_READ_REG_13(Set.PICOJAVA, 65357),
/* 351 */   PRIV_READ_REG_14(Set.PICOJAVA, 65358),
/* 352 */   PRIV_READ_REG_15(Set.PICOJAVA, 65359),
/* 353 */   PRIV_READ_REG_16(Set.PICOJAVA, 65360),
/* 354 */   PRIV_READ_REG_17(Set.PICOJAVA, 65361),
/* 355 */   PRIV_READ_REG_18(Set.PICOJAVA, 65362),
/* 356 */   PRIV_READ_REG_19(Set.PICOJAVA, 65363),
/* 357 */   PRIV_READ_REG_20(Set.PICOJAVA, 65364),
/* 358 */   PRIV_READ_REG_21(Set.PICOJAVA, 65365),
/* 359 */   PRIV_READ_REG_22(Set.PICOJAVA, 65366),
/* 360 */   PRIV_READ_REG_23(Set.PICOJAVA, 65367),
/* 361 */   PRIV_READ_REG_24(Set.PICOJAVA, 65368),
/* 362 */   PRIV_READ_REG_25(Set.PICOJAVA, 65369),
/* 363 */   PRIV_READ_REG_26(Set.PICOJAVA, 65370),
/* 364 */   PRIV_READ_REG_27(Set.PICOJAVA, 65371),
/* 365 */   PRIV_READ_REG_28(Set.PICOJAVA, 65372),
/* 366 */   PRIV_READ_REG_29(Set.PICOJAVA, 65373),
/* 367 */   PRIV_READ_REG_30(Set.PICOJAVA, 65374),
/* 368 */   PRIV_READ_REG_31(Set.PICOJAVA, 65375),
/* 369 */   PRIV_WRITE_REG_0(Set.PICOJAVA, 65376),
/* 370 */   PRIV_WRITE_REG_1(Set.PICOJAVA, 65377),
/* 371 */   PRIV_WRITE_REG_2(Set.PICOJAVA, 65378),
/* 372 */   PRIV_WRITE_REG_3(Set.PICOJAVA, 65379),
/* 373 */   PRIV_WRITE_REG_4(Set.PICOJAVA, 65380),
/* 374 */   PRIV_WRITE_REG_5(Set.PICOJAVA, 65381),
/* 375 */   PRIV_WRITE_REG_6(Set.PICOJAVA, 65382),
/* 376 */   PRIV_WRITE_REG_7(Set.PICOJAVA, 65383),
/* 377 */   PRIV_WRITE_REG_8(Set.PICOJAVA, 65384),
/* 378 */   PRIV_WRITE_REG_9(Set.PICOJAVA, 65385),
/* 379 */   PRIV_WRITE_REG_10(Set.PICOJAVA, 65386),
/* 380 */   PRIV_WRITE_REG_11(Set.PICOJAVA, 65387),
/* 381 */   PRIV_WRITE_REG_12(Set.PICOJAVA, 65388),
/* 382 */   PRIV_WRITE_REG_13(Set.PICOJAVA, 65389),
/* 383 */   PRIV_WRITE_REG_14(Set.PICOJAVA, 65390),
/* 384 */   PRIV_WRITE_REG_15(Set.PICOJAVA, 65391),
/* 385 */   PRIV_WRITE_REG_16(Set.PICOJAVA, 65392),
/* 386 */   PRIV_WRITE_REG_17(Set.PICOJAVA, 65393),
/* 387 */   PRIV_WRITE_REG_18(Set.PICOJAVA, 65394),
/* 388 */   PRIV_WRITE_REG_19(Set.PICOJAVA, 65395),
/* 389 */   PRIV_WRITE_REG_20(Set.PICOJAVA, 65396),
/* 390 */   PRIV_WRITE_REG_21(Set.PICOJAVA, 65397),
/* 391 */   PRIV_WRITE_REG_22(Set.PICOJAVA, 65398),
/* 392 */   PRIV_WRITE_REG_23(Set.PICOJAVA, 65399),
/* 393 */   PRIV_WRITE_REG_24(Set.PICOJAVA, 65400),
/* 394 */   PRIV_WRITE_REG_25(Set.PICOJAVA, 65401),
/* 395 */   PRIV_WRITE_REG_26(Set.PICOJAVA, 65402),
/* 396 */   PRIV_WRITE_REG_27(Set.PICOJAVA, 65403),
/* 397 */   PRIV_WRITE_REG_28(Set.PICOJAVA, 65404),
/* 398 */   PRIV_WRITE_REG_29(Set.PICOJAVA, 65405),
/* 399 */   PRIV_WRITE_REG_30(Set.PICOJAVA, 65406),
/* 400 */   PRIV_WRITE_REG_31(Set.PICOJAVA, 65407);
/*     */   
/*     */   public final Set set;
/*     */   
/*     */   public final int opcode;
/*     */   public final Instruction.Kind kind;
/*     */   private static final Opcode[] stdOpcodes;
/*     */   private static final Opcode[] wideOpcodes;
/*     */   private static final Opcode[] nonPrivOpcodes;
/*     */   private static final Opcode[] privOpcodes;
/*     */   public static final int WIDE = 196;
/*     */   public static final int NONPRIV = 254;
/*     */   public static final int PRIV = 255;
/*     */   
/*     */   Opcode(Set paramSet, int paramInt1, Instruction.Kind paramKind) {
/* 415 */     this.set = paramSet;
/* 416 */     this.opcode = paramInt1;
/* 417 */     this.kind = paramKind;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Opcode get(int paramInt) {
/* 426 */     return stdOpcodes[paramInt];
/*     */   }
/*     */ 
/*     */   
/*     */   public static Opcode get(int paramInt1, int paramInt2) {
/* 431 */     Opcode[] arrayOfOpcode = getOpcodeBlock(paramInt1);
/* 432 */     return (arrayOfOpcode == null) ? null : arrayOfOpcode[paramInt2];
/*     */   }
/*     */   
/*     */   private static Opcode[] getOpcodeBlock(int paramInt) {
/* 436 */     switch (paramInt) {
/*     */       case 0:
/* 438 */         return stdOpcodes;
/*     */       case 196:
/* 440 */         return wideOpcodes;
/*     */       case 254:
/* 442 */         return nonPrivOpcodes;
/*     */       case 255:
/* 444 */         return privOpcodes;
/*     */     } 
/* 446 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 451 */     stdOpcodes = new Opcode[256];
/* 452 */     wideOpcodes = new Opcode[256];
/* 453 */     nonPrivOpcodes = new Opcode[256];
/* 454 */     privOpcodes = new Opcode[256];
/*     */     
/* 456 */     for (Opcode opcode : values()) {
/* 457 */       getOpcodeBlock(opcode.opcode >> 8)[opcode.opcode & 0xFF] = opcode;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Set
/*     */   {
/* 469 */     STANDARD,
/*     */     
/* 471 */     PICOJAVA;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Opcode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */