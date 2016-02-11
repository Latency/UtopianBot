package org.rsbot.constants;

import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.RSTile;


@ScriptManifest(authors = "Latency", name = "Tiles", version = 1.0, description = "Global static constants.")
public interface Tiles {

  /**
   * The RSTile for the bank found in the corresponding town.
   */
  public static final RSTile                                bankTile_Barbarian_Village          = new RSTile(3094, 3491),
                                                            bankTile_Rimmington                 = new RSTile(3047, 3236),
                                                            bankTile_AlKharid                   = new RSTile(3269, 3167),
                                                            bankTile_WestVarrock                = new RSTile(3182, 3436),
                                                            bankTile_Varrock                    = new RSTile(3254, 3420),
                                                            bankTile_MiningGuild                = new RSTile(3012, 3355),
                                                            bankTile_unknown1                   = new RSTile(3093, 3243),
                                                            bankTile_unknown2                   = new RSTile(3209, 3219, 2),
                                                            bankTile_unknown3                   = new RSTile(3270, 3167),
                                                            bankTile_unknown4                   = new RSTile(2946, 3369);

  /**
   * The list of all RSTiles for banks.
   */
  public static final RSTile[]                              bankTiles_All                       = { bankTile_Barbarian_Village,
                                                                                                    bankTile_Rimmington,
                                                                                                    bankTile_AlKharid,
                                                                                                    bankTile_WestVarrock,
                                                                                                    bankTile_Varrock,
                                                                                                    bankTile_MiningGuild,
                                                                                                    bankTile_unknown1,
                                                                                                    bankTile_unknown2,
                                                                                                    bankTile_unknown3,
                                                                                                    bankTile_unknown4
                                                                                                  };
  

  public static final int
                                              // not2Bank
                                              pickID[]          = { 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261 },
                                              autoPickIDs[]     = { 14664,  // random gift
                                                                    995,    // gold
                                                                    Rune.FIRE.getItemID(),
                                                                    Rune.WATER.getItemID(),
                                                                    Rune.AIR.getItemID(),
                                                                    Rune.EARTH.getItemID(),
                                                                    Rune.MIND.getItemID(),
                                                                    Rune.BODY.getItemID(),
                                                                    Rune.DEATH.getItemID(),
                                                                    Rune.CHAOS.getItemID(),
                                                                    Rune.LAW.getItemID(),
                                                                    Rune.COSMIC.getItemID(),
                                                                    Rune.ESSENCE.getItemID()
                                                                  },
                                              superheatIDs[]    = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261 },
                                              // for essence
                                              portalID          = 39831,
                                              auburyID          = 5913,
                                              xpPerEssence      = 5;

  public static final int                 BankerIDs_AlKharid[]  = {496, 497};

  public static final RSTile              AuburysShopDoor       = new RSTile(3253, 3399);
  
  /**
   * A list of valuable ore heat IDs corresponding to its associated variable name.
   */
  public static final int                     // not2Drop
                                              clayHeatIDs[]     = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 434 };

  public static final int                     // not2Drop
                                              copperHeatIDs[]   = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 436, 2349 },
                                              tinHeatIDs[]      = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 438, 2349 },
                                              ironHeatIDs[]     = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 440, 2351 },
                                              silverHeatIDs[]   = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 442, 2355 },
                                              coalHeatIDs[]     = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 453, 2353 },
                                              goldHeatIDs[]     = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 444, 2357 },
                                              mithrilHeatIDs[]  = { 561, 1387, 554, 1265, 1267, 1269, 1271, 1273, 1275, 14099, 14107, 15259, 15261, 447, 2359 };

  /**
   * A list of valuable object IDs corresponding to its associated variable name.
   */
  public static final int
                                              // Gem IDs
                                              GemIDs[]          = { 1621, 1619, 1623, 1617 },
                                              // Rock IDs
                                              Essence_IDs[]     = { 2491, 7936 /*perfect*/ },
                                              Clay_IDs[]        = { 11504, 11503, 11505, 9711, 9713, 15503, 15505 },
                                              Copper_IDs[]      = { 11937, 11938, 9710, 9708, 9709 },
                                              Tin_IDs[]         = { 11933, 11934, 11935, 11957, 11958, 11959, 9714, 9716 },
                                              Iron_IDs[]        = { 37309, 37307, 37308, 11955, 11956, 9718, 9717, 9719 },
                                              Silver_IDs[]      = { 37306, 37304, 37305, 11950, 11949, 11948 },
                                              Coal_IDs[]        = { 11930, 11932, 11931, 5771, 5772, 5770 },
                                              Gold_IDs[]        = { 37312, 37310, 9720, 9722 },
                                              Mithril_IDs[]     = { 5784, 5786, 5785 },
                                              StrangeRock_IDs[] = { 15532, 15533 },
                                              RailRoad_IDs[]    = { 495, 494, 493, 469, 467, 460, 455, 454, 448 },
                                              DepoBox_IDs[]     = { 36788 },
                                              Stair_IDs[]       = { 6226, 2113 },
                                              Door_IDs[]        = { 24381, 24379 };

  /**
   * The General Exchange object ID corresponding to ore type.
   */
  public static final int
                                              // GE Ore ID's
                                              GE_Essence_ID     = 1436,
                                              GE_Clay_ID        = 434,
                                              GE_Copper_ID      = 436,
                                              GE_Tin_ID         = 438,
                                              GE_Iron_ID        = 440,
                                              GE_Silver_ID      = 442,
                                              GE_Coal_ID        = 453,
                                              GE_Gold_ID        = 444,
                                              GE_Mithril_ID     = 447;

  /**
   * A list of object IDs for a set corresponding to ore type.
   */
  public static final int[][]   OreIDs                          = { Essence_IDs,
                                                                    Clay_IDs,
                                                                    Copper_IDs,
                                                                    Tin_IDs,
                                                                    Iron_IDs,
                                                                    Silver_IDs,
                                                                    Coal_IDs,
                                                                    Gold_IDs,
                                                                    Mithril_IDs
                                                                  };

  /**
   * A list of object IDs for the General Exchange corresponding to ore type.
   */
  public static final Integer[] GEOreIDs                        = { GE_Essence_ID,
                                                                    GE_Clay_ID,
                                                                    GE_Copper_ID,
                                                                    GE_Tin_ID,
                                                                    GE_Iron_ID,
                                                                    GE_Silver_ID,
                                                                    GE_Coal_ID,
                                                                    GE_Gold_ID,
                                                                    GE_Mithril_ID
                                                                  };

  // ///////////////////////////////////////////////////////////////////////
  // Paths
  // ///////////////////////////////////////////////////////////////////////
  public static final RSTile[]  pathTo_Guild                            = new RSTile[] {
                                                                          new RSTile(3046, 9753),
                                                                          new RSTile(3046, 9744),
                                                                          new RSTile(3040, 9738),
                                                                          new RSTile(3032, 9739),
                                                                          new RSTile(3020, 9740)
                                                                        },
                                // ////////////// ESSENCE ////////////////
                                pathTo_Essence_Barbarian_Village        = new RSTile[] {},
                                pathTo_Essence_Rimmington               = new RSTile[] {},
                                pathTo_Essence_AlKharid                 = new RSTile[] {},
                                pathTo_Essence_WestVarrock              = new RSTile[] {},
                                pathTo_Essence_Varrock                  = new RSTile[] {
                                                                          bankTile_Varrock,
                                                                          new RSTile(3258, 3428),
                                                                          new RSTile(3260, 3421),
                                                                          new RSTile(3257, 3408),
                                                                          new RSTile(3253, 3398)
                                                                        },
                                pathTo_Essence_MiningGuild              = new RSTile[] {},
                                // ////////////// CLAY //////////////////
                                pathTo_Clay_Barbarian_Village           = new RSTile[] {},
                                pathTo_Clay_Rimmington                  = new RSTile[] {
                                                                          new RSTile(3012, 3355),
                                                                          new RSTile(3007, 3344),
                                                                          new RSTile(3007, 3332),
                                                                          new RSTile(3006, 3321),
                                                                          new RSTile(2997, 3310),
                                                                          new RSTile(2986, 3304),
                                                                          new RSTile(2985, 3293),
                                                                          new RSTile(2986, 3282),
                                                                          new RSTile(2979, 3274),
                                                                          new RSTile(2974, 3263),
                                                                          new RSTile(2977, 3254),
                                                                          new RSTile(2978, 3242),
                                                                          new RSTile(2986, 3240),
                                                                          new RSTile(2986, 3240)
                                                                        },
                                pathTo_Clay_AlKharid                    = new RSTile[] {},
                                pathTo_Clay_WestVarrock                 = new RSTile[] {
                                                                          new RSTile(3182, 3436),
                                                                          new RSTile(3177, 3429),
                                                                          new RSTile(3171, 3420),
                                                                          new RSTile(3171, 3410),
                                                                          new RSTile(3171, 3402),
                                                                          new RSTile(3173, 3394),
                                                                          new RSTile(3176, 3387),
                                                                          new RSTile(3182, 3379),
                                                                          new RSTile(3184, 3373),
                                                                          new RSTile(3180, 3371)
                                                                        },
                                pathTo_Clay_Varrock                     = new RSTile[] {},
                                pathTo_Clay_MiningGuild                 = new RSTile[] {},
                                // ////////////// COPPER ////////////////
                                pathTo_Copper_Barbarian_Village         = new RSTile[] {},
                                pathTo_Copper_Rimmington                = new RSTile[] {
                                                                          new RSTile(3047, 3236),
                                                                          new RSTile(3040, 3236),
                                                                          new RSTile(3033, 3236),
                                                                          new RSTile(3027, 3240),
                                                                          new RSTile(3021, 3242),
                                                                          new RSTile(3015, 3242),
                                                                          new RSTile(3008, 3240),
                                                                          new RSTile(3001, 3238),
                                                                          new RSTile(2995, 3236),
                                                                          new RSTile(2989, 3232),
                                                                          new RSTile(2985, 3238),
                                                                          new RSTile(2980, 3243),
                                                                          new RSTile(2977, 3246)
                                                                        },
                                pathTo_Copper_AlKharid                  = new RSTile[] {
                                                                          new RSTile(3269, 3166),
                                                                          new RSTile(3275, 3170),
                                                                          new RSTile(3278, 3178),
                                                                          new RSTile(3283, 3185),
                                                                          new RSTile(3282, 3191),
                                                                          new RSTile(3282, 3199),
                                                                          new RSTile(3278, 3206),
                                                                          new RSTile(3278, 3212),
                                                                          new RSTile(3278, 3220),
                                                                          new RSTile(3279, 3226),
                                                                          new RSTile(3283, 3233),
                                                                          new RSTile(3287, 3240),
                                                                          new RSTile(3291, 3247),
                                                                          new RSTile(3293, 3255),
                                                                          new RSTile(3294, 3261),
                                                                          new RSTile(3295, 3268),
                                                                          new RSTile(3296, 3276),
                                                                          new RSTile(3298, 3283),
                                                                          new RSTile(3299, 3290),
                                                                          new RSTile(3298, 3297),
                                                                          new RSTile(3299, 3303),
                                                                          new RSTile(3300, 3310),
                                                                          new RSTile(3297, 3314)
                                                                        },
                                pathTo_Copper_WestVarrock               = new RSTile[] {},
                                pathTo_Copper_Varrock                   = new RSTile[] {},
                                pathTo_Copper_MiningGuild               = new RSTile[] {},
                                // ////////////// TIN ///////////////////
                                pathTo_Tin_Barbarian_Village            = new RSTile[] {
                                                                          new RSTile(3094, 3491),
                                                                          new RSTile(3088, 3488),
                                                                          new RSTile(3093, 3485),
                                                                          new RSTile(3100, 3483),
                                                                          new RSTile(3100, 3475),
                                                                          new RSTile(3100, 3466),
                                                                          new RSTile(3094, 3464),
                                                                          new RSTile(3087, 3462),
                                                                          new RSTile(3087, 3455),
                                                                          new RSTile(3087, 3448),
                                                                          new RSTile(3088, 3439),
                                                                          new RSTile(3089, 3432),
                                                                          new RSTile(3086, 3427),
                                                                          new RSTile(3083, 3423),
                                                                          new RSTile(3080, 3491)
                                                                        },
                                pathTo_Tin_Rimmington                   = new RSTile[] {
                                                                          new RSTile(3047, 3236),
                                                                          new RSTile(3040, 3236),
                                                                          new RSTile(3033, 3236),
                                                                          new RSTile(3027, 3240),
                                                                          new RSTile(3021, 3242),
                                                                          new RSTile(3015, 3242),
                                                                          new RSTile(3008, 3240),
                                                                          new RSTile(3001, 3238),
                                                                          new RSTile(2995, 3236),
                                                                          new RSTile(2989, 3232),
                                                                          new RSTile(2984, 3236)
                                                                        },
                                pathTo_Tin_AlKharid                     = new RSTile[] {
                                                                          new RSTile(3269, 3166),
                                                                          new RSTile(3275, 3170),
                                                                          new RSTile(3278, 3178),
                                                                          new RSTile(3283, 3185),
                                                                          new RSTile(3282, 3191),
                                                                          new RSTile(3282, 3199),
                                                                          new RSTile(3278, 3206),
                                                                          new RSTile(3278, 3212),
                                                                          new RSTile(3278, 3220),
                                                                          new RSTile(3279, 3226),
                                                                          new RSTile(3283, 3233),
                                                                          new RSTile(3287, 3240),
                                                                          new RSTile(3291, 3247),
                                                                          new RSTile(3293, 3255),
                                                                          new RSTile(3294, 3261),
                                                                          new RSTile(3295, 3268),
                                                                          new RSTile(3296, 3276),
                                                                          new RSTile(3298, 3283),
                                                                          new RSTile(3299, 3290),
                                                                          new RSTile(3298, 3297),
                                                                          new RSTile(3299, 3303),
                                                                          new RSTile(3300, 3310),
                                                                          new RSTile(3301, 3316)
                                                                        },
                                pathTo_Tin_WestVarrock                  = new RSTile[] {
                                                                          new RSTile(3182, 3436),
                                                                          new RSTile(3177, 3429),
                                                                          new RSTile(3171, 3420),
                                                                          new RSTile(3171, 3410),
                                                                          new RSTile(3171, 3402),
                                                                          new RSTile(3173, 3394),
                                                                          new RSTile(3176, 3387),
                                                                          new RSTile(3182, 3379),
                                                                          new RSTile(3182, 3376)
                                                                        },
                                pathTo_Tin_Varrock                      = new RSTile[] {},
                                pathTo_Tin_MiningGuild                  = new RSTile[] {},
                                // ////////////// IRON /////////////////
                                pathTo_Iron_Barbarian_Village           = new RSTile[] {},
                                pathTo_Iron_Rimmington                  = new RSTile[] {
                                                                          new RSTile(3047, 3236),
                                                                          new RSTile(3040, 3236),
                                                                          new RSTile(3033, 3236),
                                                                          new RSTile(3027, 3240),
                                                                          new RSTile(3021, 3242),
                                                                          new RSTile(3015, 3242),
                                                                          new RSTile(3008, 3240),
                                                                          new RSTile(3001, 3238),
                                                                          new RSTile(2995, 3236),
                                                                          new RSTile(2989, 3232),
                                                                          new RSTile(2982, 3235),
                                                                          new RSTile(2976, 3237),
                                                                          new RSTile(2970, 3240)
                                                                        },
                                pathTo_Iron_AlKharid                    = new RSTile[] {
                                                                          new RSTile(3269, 3166),
                                                                          new RSTile(3275, 3170),
                                                                          new RSTile(3278, 3178),
                                                                          new RSTile(3283, 3185),
                                                                          new RSTile(3282, 3191),
                                                                          new RSTile(3282, 3199),
                                                                          new RSTile(3278, 3206),
                                                                          new RSTile(3278, 3212),
                                                                          new RSTile(3278, 3220),
                                                                          new RSTile(3279, 3226),
                                                                          new RSTile(3283, 3233),
                                                                          new RSTile(3287, 3240),
                                                                          new RSTile(3291, 3247),
                                                                          new RSTile(3293, 3255),
                                                                          new RSTile(3294, 3261),
                                                                          new RSTile(3295, 3268),
                                                                          new RSTile(3296, 3276),
                                                                          new RSTile(3298, 3283),
                                                                          new RSTile(3299, 3290),
                                                                          new RSTile(3298, 3297),
                                                                          new RSTile(3299, 3303),
                                                                          new RSTile(3300, 3310),
                                                                          new RSTile(3298, 3311)
                                                                        },
                                pathTo_Iron_WestVarrock                 = new RSTile[] {
                                                                          new RSTile(3182, 3436),
                                                                          new RSTile(3177, 3429),
                                                                          new RSTile(3171, 3420),
                                                                          new RSTile(3171, 3410),
                                                                          new RSTile(3171, 3402),
                                                                          new RSTile(3173, 3394),
                                                                          new RSTile(3176, 3387),
                                                                          new RSTile(3182, 3379),
                                                                          new RSTile(3184, 3373),
                                                                          new RSTile(3175, 3367)
                                                                        },
                                pathTo_Iron_Varrock                     = new RSTile[] {},
                                pathTo_Iron_MiningGuild                 = new RSTile[] {},
                                // ////////////// SILVER ///////////////
                                pathTo_Silver_Barbarian_Village         = new RSTile[] {},
                                pathTo_Silver_Rimmington                = new RSTile[] {},
                                pathTo_Silver_AlKharid                  = new RSTile[] {
                                                                          new RSTile(3269, 3166),
                                                                          new RSTile(3275, 3170),
                                                                          new RSTile(3278, 3178),
                                                                          new RSTile(3283, 3185),
                                                                          new RSTile(3282, 3191),
                                                                          new RSTile(3282, 3199),
                                                                          new RSTile(3278, 3206),
                                                                          new RSTile(3278, 3212),
                                                                          new RSTile(3278, 3220),
                                                                          new RSTile(3279, 3226),
                                                                          new RSTile(3283, 3233),
                                                                          new RSTile(3287, 3240),
                                                                          new RSTile(3291, 3247),
                                                                          new RSTile(3293, 3255),
                                                                          new RSTile(3294, 3261),
                                                                          new RSTile(3295, 3268),
                                                                          new RSTile(3296, 3276),
                                                                          new RSTile(3298, 3283),
                                                                          new RSTile(3299, 3290),
                                                                          new RSTile(3298, 3297),
                                                                          new RSTile(3295, 3301)
                                                                        },
                                pathTo_Silver_WestVarrock               = new RSTile[] {
                                                                          new RSTile(3182, 3436),
                                                                          new RSTile(3177, 3429),
                                                                          new RSTile(3171, 3420),
                                                                          new RSTile(3171, 3410),
                                                                          new RSTile(3171, 3402),
                                                                          new RSTile(3173, 3394),
                                                                          new RSTile(3176, 3387),
                                                                          new RSTile(3182, 3379),
                                                                          new RSTile(3184, 3373),
                                                                          new RSTile(3177, 3368)
                                                                        },
                                pathTo_Silver_Varrock                   = new RSTile[] {},
                                pathTo_Silver_MiningGuild               = new RSTile[] {},
                                // ////////////// COAL /////////////////
                                pathTo_Coal_Barbarian_Village           = new RSTile[] {
                                                                          new RSTile(3094, 3491),
                                                                          new RSTile(3088, 3488),
                                                                          new RSTile(3093, 3485),
                                                                          new RSTile(3100, 3483),
                                                                          new RSTile(3100, 3475),
                                                                          new RSTile(3100, 3466),
                                                                          new RSTile(3094, 3464),
                                                                          new RSTile(3087, 3462),
                                                                          new RSTile(3087, 3455),
                                                                          new RSTile(3087, 3448),
                                                                          new RSTile(3088, 3439),
                                                                          new RSTile(3089, 3432),
                                                                          new RSTile(3086, 3427),
                                                                          new RSTile(3083, 3423),
                                                                          new RSTile(3083, 3422)
                                                                        },
                                pathTo_Coal_Rimmington                  = new RSTile[] {},
                                pathTo_Coal_AlKharid                    = new RSTile[] {
                                                                          new RSTile(3269, 3166),
                                                                          new RSTile(3275, 3170),
                                                                          new RSTile(3278, 3178),
                                                                          new RSTile(3283, 3185),
                                                                          new RSTile(3282, 3191),
                                                                          new RSTile(3282, 3199),
                                                                          new RSTile(3278, 3206),
                                                                          new RSTile(3278, 3212),
                                                                          new RSTile(3278, 3220),
                                                                          new RSTile(3279, 3226),
                                                                          new RSTile(3283, 3233),
                                                                          new RSTile(3287, 3240),
                                                                          new RSTile(3291, 3247),
                                                                          new RSTile(3293, 3255),
                                                                          new RSTile(3294, 3261),
                                                                          new RSTile(3295, 3268),
                                                                          new RSTile(3296, 3276),
                                                                          new RSTile(3298, 3283),
                                                                          new RSTile(3299, 3290),
                                                                          new RSTile(3298, 3297),
                                                                          new RSTile(3300, 3300)
                                                                        },
                                pathTo_Coal_WestVarrock                 = new RSTile[] {},
                                pathTo_Coal_Varrock                     = new RSTile[] {},
                                pathTo_Coal_MiningGuild                 = new RSTile[] {
                                                                          new RSTile(3013, 3355),
                                                                          new RSTile(3018, 3362),
                                                                          new RSTile(3024, 3358),
                                                                          new RSTile(3024, 3351),
                                                                          new RSTile(3031, 3348),
                                                                          new RSTile(3031, 3341),
                                                                          new RSTile(3027, 3336),
                                                                          new RSTile(3021, 3339)
                                                                        },
                                // ////////////// GOLD /////////////////
                                pathTo_Gold_Barbarian_Village           = new RSTile[] {},
                                pathTo_Gold_Rimmington                  = new RSTile[] {
                                                                          new RSTile(3047, 3236),
                                                                          new RSTile(3040, 3236),
                                                                          new RSTile(3033, 3236),
                                                                          new RSTile(3027, 3240),
                                                                          new RSTile(3021, 3242),
                                                                          new RSTile(3015, 3242),
                                                                          new RSTile(3008, 3240),
                                                                          new RSTile(3001, 3238),
                                                                          new RSTile(2995, 3236),
                                                                          new RSTile(2989, 3232),
                                                                          new RSTile(2981, 3232),
                                                                          new RSTile(2977, 3234)
                                                                        },
                                pathTo_Gold_AlKharid                    = new RSTile[] {
                                                                          new RSTile(3269, 3166),
                                                                          new RSTile(3275, 3170),
                                                                          new RSTile(3278, 3178),
                                                                          new RSTile(3283, 3185),
                                                                          new RSTile(3282, 3191),
                                                                          new RSTile(3282, 3199),
                                                                          new RSTile(3278, 3206),
                                                                          new RSTile(3278, 3212),
                                                                          new RSTile(3278, 3220),
                                                                          new RSTile(3279, 3226),
                                                                          new RSTile(3283, 3233),
                                                                          new RSTile(3287, 3240),
                                                                          new RSTile(3291, 3247),
                                                                          new RSTile(3293, 3255),
                                                                          new RSTile(3294, 3261),
                                                                          new RSTile(3295, 3268),
                                                                          new RSTile(3296, 3276),
                                                                          new RSTile(3298, 3283),
                                                                          new RSTile(3297, 3287)
                                                                        },
                                pathTo_Gold_WestVarrock                 = new RSTile[] {},
                                pathTo_Gold_Varrock                     = new RSTile[] {},
                                pathTo_Gold_MiningGuild                 = new RSTile[] {},
                                // ////////////// MITHRIL /////////////
                                pathTo_Mithril_Barbarian_Village        = new RSTile[] {},
                                pathTo_Mithril_Rimmington               = new RSTile[] {},
                                pathTo_Mithril_AlKharid                 = new RSTile[] {},
                                pathTo_Mithril_WestVarrock              = new RSTile[] {},
                                pathTo_Mithril_Varrock                  = new RSTile[] {},
                                pathTo_Mithril_MiningGuild              = new RSTile[] {
                                                                          new RSTile(3013, 3355),
                                                                          new RSTile(3018, 3362),
                                                                          new RSTile(3024, 3358),
                                                                          new RSTile(3024, 3351),
                                                                          new RSTile(3031, 3348),
                                                                          new RSTile(3031, 3341),
                                                                          new RSTile(3027, 3336),
                                                                          new RSTile(3021, 3339)
                                                                        },
                                pathTo_FireRuins_AlKharid               = new RSTile[] {
                                                                          new RSTile(3269, 3168),
                                                                          new RSTile(3280, 3181),
                                                                          new RSTile(3280, 3199),
                                                                          new RSTile(3278, 3214),
                                                                          new RSTile(3283, 3231),
                                                                          new RSTile(3296, 3243),
                                                                          new RSTile(3311, 3254)
                                                                        },
                                pathTo_FireRune_Altar                   = new RSTile[] {
                                                                          new RSTile(2577, 4846), new RSTile(2584, 4840)
                                                                        };

  /**
   * The list of RSTile paths based on ore material type.
   */
  public static final RSTile[][]pathTiles_Essence                       = { pathTo_Essence_Barbarian_Village,
                                                                            pathTo_Essence_Rimmington,
                                                                            pathTo_Essence_AlKharid,
                                                                            pathTo_Essence_WestVarrock,
                                                                            pathTo_Essence_Varrock,
                                                                            pathTo_Essence_MiningGuild
                                                                          },
                                pathTiles_Clay                          = { pathTo_Clay_Barbarian_Village,
                                                                            pathTo_Clay_Rimmington,
                                                                            pathTo_Clay_AlKharid,
                                                                            pathTo_Clay_WestVarrock,
                                                                            pathTo_Clay_Varrock,
                                                                            pathTo_Clay_MiningGuild
                                                                          },
                                pathTiles_Copper                        = { pathTo_Copper_Barbarian_Village,
                                                                            pathTo_Copper_Rimmington,
                                                                            pathTo_Copper_AlKharid,
                                                                            pathTo_Copper_WestVarrock,
                                                                            pathTo_Copper_Varrock,
                                                                            pathTo_Copper_MiningGuild
                                                                          },
                                pathTiles_Tin                           = { pathTo_Tin_Barbarian_Village,
                                                                            pathTo_Tin_Rimmington,
                                                                            pathTo_Tin_AlKharid,
                                                                            pathTo_Tin_WestVarrock,
                                                                            pathTo_Tin_Varrock,
                                                                            pathTo_Tin_MiningGuild
                                                                          },
                                pathTiles_Iron                          = { pathTo_Iron_Barbarian_Village,
                                                                            pathTo_Iron_Rimmington,
                                                                            pathTo_Iron_AlKharid,
                                                                            pathTo_Iron_WestVarrock,
                                                                            pathTo_Iron_Varrock,
                                                                            pathTo_Iron_MiningGuild
                                                                          },
                                pathTiles_Silver                        = { pathTo_Silver_Barbarian_Village,
                                                                            pathTo_Silver_Rimmington,
                                                                            pathTo_Silver_AlKharid,
                                                                            pathTo_Silver_WestVarrock,
                                                                            pathTo_Silver_Varrock,
                                                                            pathTo_Silver_MiningGuild
                                                                          },
                                pathTiles_Coal                          = { pathTo_Coal_Barbarian_Village,
                                                                            pathTo_Coal_Rimmington,
                                                                            pathTo_Coal_AlKharid,
                                                                            pathTo_Coal_WestVarrock,
                                                                            pathTo_Coal_Varrock,
                                                                            pathTo_Coal_MiningGuild
                                                                          },
                                pathTiles_Gold                          = { pathTo_Gold_Barbarian_Village,
                                                                            pathTo_Gold_Rimmington,
                                                                            pathTo_Gold_AlKharid,
                                                                            pathTo_Gold_WestVarrock,
                                                                            pathTo_Gold_Varrock,
                                                                            pathTo_Gold_MiningGuild
                                                                          },
                                pathTiles_Mithril                       = { pathTo_Mithril_Barbarian_Village,
                                                                            pathTo_Mithril_Rimmington,
                                                                            pathTo_Mithril_AlKharid,
                                                                            pathTo_Mithril_WestVarrock,
                                                                            pathTo_Mithril_Varrock,
                                                                            pathTo_Mithril_MiningGuild
                                                                          };

  /**
   * The list of all RSTile paths for ore materials.
   */
  public static final RSTile[][][]  pathTiles_All_Ore                   = { pathTiles_Essence,
                                                                            pathTiles_Clay,
                                                                            pathTiles_Copper,
                                                                            pathTiles_Tin,
                                                                            pathTiles_Iron,
                                                                            pathTiles_Silver,
                                                                            pathTiles_Coal,
                                                                            pathTiles_Gold,
                                                                            pathTiles_Mithril
                                                                          };

  /**
   * The list of RSTile paths based on location.
   */
  public static final RSTile[][]    pathTiles_Barbarian_Village         = { pathTo_Essence_Barbarian_Village,
                                                                            pathTo_Clay_Barbarian_Village,
                                                                            pathTo_Copper_Barbarian_Village,
                                                                            pathTo_Tin_Barbarian_Village,
                                                                            pathTo_Iron_Barbarian_Village,
                                                                            pathTo_Silver_Barbarian_Village,
                                                                            pathTo_Coal_Barbarian_Village,
                                                                            pathTo_Gold_Barbarian_Village,
                                                                            pathTo_Mithril_Barbarian_Village
                                                                          },
                                    pathTiles_Rimmington                = { pathTo_Essence_Rimmington,
                                                                            pathTo_Clay_Rimmington,
                                                                            pathTo_Copper_Rimmington,
                                                                            pathTo_Tin_Rimmington,
                                                                            pathTo_Iron_Rimmington,
                                                                            pathTo_Silver_Rimmington,
                                                                            pathTo_Coal_Rimmington,
                                                                            pathTo_Gold_Rimmington,
                                                                            pathTo_Mithril_Rimmington
                                                                          },
                                    pathTiles_AlKharid                  = { pathTo_Essence_AlKharid,
                                                                            pathTo_Clay_AlKharid,
                                                                            pathTo_Copper_AlKharid,
                                                                            pathTo_Tin_AlKharid,
                                                                            pathTo_Iron_AlKharid,
                                                                            pathTo_Silver_AlKharid,
                                                                            pathTo_Coal_AlKharid,
                                                                            pathTo_Gold_AlKharid,
                                                                            pathTo_Mithril_AlKharid
                                                                          },
                                    pathTiles_WestVarrock               = { pathTo_Essence_WestVarrock,
                                                                            pathTo_Clay_WestVarrock,
                                                                            pathTo_Copper_WestVarrock,
                                                                            pathTo_Tin_WestVarrock,
                                                                            pathTo_Iron_WestVarrock,
                                                                            pathTo_Silver_WestVarrock,
                                                                            pathTo_Coal_WestVarrock,
                                                                            pathTo_Gold_WestVarrock,
                                                                            pathTo_Mithril_WestVarrock
                                                                          },
                                    pathTiles_Varrock                   = { pathTo_Essence_Varrock,
                                                                            pathTo_Clay_Varrock,
                                                                            pathTo_Copper_Varrock,
                                                                            pathTo_Tin_Varrock,
                                                                            pathTo_Iron_Varrock,
                                                                            pathTo_Silver_Varrock,
                                                                            pathTo_Coal_Varrock,
                                                                            pathTo_Gold_Varrock,
                                                                            pathTo_Mithril_Varrock
                                                                          },
                                    pathTiles_MiningGuild               = { pathTo_Essence_MiningGuild,
                                                                            pathTo_Clay_MiningGuild,
                                                                            pathTo_Copper_MiningGuild,
                                                                            pathTo_Tin_MiningGuild,
                                                                            pathTo_Iron_MiningGuild,
                                                                            pathTo_Silver_MiningGuild,
                                                                            pathTo_Coal_MiningGuild,
                                                                            pathTo_Gold_MiningGuild,
                                                                            pathTo_Mithril_MiningGuild
                                                                          };

  /**
   * The list of all RSTile paths for all locations.
   */
  public static final RSTile[][][]  pathTiles_All_Location              = { pathTiles_Barbarian_Village,
                                                                            pathTiles_Rimmington,
                                                                            pathTiles_AlKharid,
                                                                            pathTiles_WestVarrock,
                                                                            pathTiles_Varrock,
                                                                            pathTiles_MiningGuild
                                                                          };

  // ///////////////////////////////////////////////////////////////////////
  // Mines
  // ///////////////////////////////////////////////////////////////////////
  /**
   * The list of RSTiles for the mines in the Guild.
   */
  public static final RSTile[]  Guild_mines         = new RSTile[] { new RSTile(3034, 9736),
                                                                     new RSTile(3029, 9738),
                                                                     new RSTile(3036, 9740),
                                                                     new RSTile(3047, 9736) };

  /**
   * The RSTile of the mine associated with the variable name.
   */
  // ////////////// ESSENCE ////////////////
  public static final RSTile    mine_Essence_Barbarian_Village  = null,
                                mine_Essence_Rimmington         = null,
                                mine_Essence_AlKharid           = null,
                                mine_Essence_WestVarrock        = null,
                                mine_Essence_Varrock            = null,
                                mine_Essence_MiningGuild        = null,
                                // ////////////// CLAY //////////////////
                                mine_Clay_Barbarian_Village     = null,
                                mine_Clay_Rimmington            = new RSTile(2986, 3240),
                                mine_Clay_AlKharid              = null,
                                mine_Clay_WestVarrock           = new RSTile(3180, 3371),
                                mine_Clay_Varrock               = null,
                                mine_Clay_MiningGuild           = null,
                                // ////////////// COPPER ////////////////
                                mine_Copper_Barbarian_Village   = null,
                                mine_Copper_Rimmington          = new RSTile(2977, 3246),
                                mine_Copper_AlKharid            = new RSTile(3297, 3314),
                                mine_Copper_WestVarrock         = null,
                                mine_Copper_Varrock             = null,
                                mine_Copper_MiningGuild         = null,
                                // ////////////// TIN ///////////////////
                                mine_Tin_Barbarian_Village      = new RSTile(3080, 3419),
                                mine_Tin_Rimmington             = new RSTile(2984, 3236),
                                mine_Tin_AlKharid               = new RSTile(3301, 3316),
                                mine_Tin_WestVarrock            = new RSTile(3182, 3377),
                                mine_Tin_Varrock                = null,
                                mine_Tin_MiningGuild            = null,
                                // ////////////// IRON /////////////////
                                mine_Iron_Barbarian_Village     = null,
                                mine_Iron_Rimmington            = new RSTile(2970, 3240),
                                mine_Iron_AlKharid              = new RSTile(3298, 3311),
                                mine_Iron_WestVarrock           = new RSTile(3175, 3367),
                                mine_Iron_Varrock               = null,
                                mine_Iron_MiningGuild           = null,
                                // ////////////// SILVER ///////////////
                                mine_Silver_Barbarian_Village   = null,
                                mine_Silver_Rimmington          = null,
                                mine_Silver_AlKharid            = new RSTile(3295, 3301),
                                mine_Silver_WestVarrock         = new RSTile(3176, 3368),
                                mine_Silver_Varrock             = null,
                                mine_Silver_MiningGuild         = null,
                                // ////////////// COAL /////////////////
                                mine_Coal_Barbarian_Village     = new RSTile(3083, 3422),
                                mine_Coal_Rimmington            = null,
                                mine_Coal_AlKharid              = new RSTile(3300, 3299),
                                mine_Coal_WestVarrock           = null,
                                mine_Coal_Varrock               = null,
                                mine_Coal_MiningGuild           = new RSTile(3037, 9738),
                                // ////////////// GOLD /////////////////
                                mine_Gold_Barbarian_Village     = null,
                                mine_Gold_Rimmington            = new RSTile(2977, 3234),
                                mine_Gold_AlKharid              = new RSTile(3297, 3287),
                                mine_Gold_WestVarrock           = null,
                                mine_Gold_Varrock               = null,
                                mine_Gold_MiningGuild           = null,
                                // ////////////// MITHRIL //////////////
                                mine_Mithril_Barbarian_Village  = null,
                                mine_Mithril_Rimmington         = null,
                                mine_Mithril_AlKharid           = null,
                                mine_Mithril_WestVarrock        = null,
                                mine_Mithril_Varrock            = null,
                                mine_Mithril_MiningGuild        = new RSTile(3050, 9737);

  /**
   * The list of RSTiles for the associated variable name ore.
   */
  public static final RSTile[]      mineTiles_Essence           = { mine_Essence_Barbarian_Village,
                                                                    mine_Essence_Rimmington,
                                                                    mine_Essence_AlKharid,
                                                                    mine_Essence_WestVarrock,
                                                                    mine_Essence_Varrock,
                                                                    mine_Essence_MiningGuild
                                                                  },
                                    mineTiles_Clay              = { mine_Clay_Barbarian_Village,
                                                                    mine_Clay_Rimmington,
                                                                    mine_Clay_AlKharid,
                                                                    mine_Clay_WestVarrock,
                                                                    mine_Clay_Varrock,
                                                                    mine_Clay_MiningGuild
                                                                  },
                                    mineTiles_Copper            = { mine_Copper_Barbarian_Village,
                                                                    mine_Copper_Rimmington,
                                                                    mine_Copper_AlKharid,
                                                                    mine_Copper_WestVarrock,
                                                                    mine_Copper_Varrock,
                                                                    mine_Copper_MiningGuild
                                                                  },
                                    mineTiles_Tin               = { mine_Tin_Barbarian_Village,
                                                                    mine_Tin_Rimmington,
                                                                    mine_Tin_AlKharid,
                                                                    mine_Tin_WestVarrock,
                                                                    mine_Tin_Varrock,
                                                                    mine_Tin_MiningGuild
                                                                  },
                                    mineTiles_Iron              = { mine_Iron_Barbarian_Village,
                                                                    mine_Iron_Rimmington,
                                                                    mine_Iron_AlKharid,
                                                                    mine_Iron_WestVarrock,
                                                                    mine_Iron_Varrock,
                                                                    mine_Iron_MiningGuild
                                                                  },
                                    mineTiles_Silver            = { mine_Silver_Barbarian_Village,
                                                                    mine_Silver_Rimmington,
                                                                    mine_Silver_AlKharid,
                                                                    mine_Silver_WestVarrock,
                                                                    mine_Silver_Varrock,
                                                                    mine_Silver_MiningGuild
                                                                  },
                                    mineTiles_Coal              = { mine_Coal_Barbarian_Village,
                                                                    mine_Coal_Rimmington,
                                                                    mine_Coal_AlKharid,
                                                                    mine_Coal_WestVarrock,
                                                                    mine_Coal_Varrock,
                                                                    mine_Coal_MiningGuild
                                                                  },
                                    mineTiles_Gold              = { mine_Gold_Barbarian_Village,
                                                                    mine_Gold_Rimmington,
                                                                    mine_Gold_AlKharid,
                                                                    mine_Gold_WestVarrock,
                                                                    mine_Gold_Varrock,
                                                                    mine_Gold_MiningGuild
                                                                  },
                                    mineTiles_Mithril           = { mine_Mithril_Barbarian_Village,
                                                                    mine_Mithril_Rimmington,
                                                                    mine_Mithril_AlKharid,
                                                                    mine_Mithril_WestVarrock,
                                                                    mine_Mithril_Varrock,
                                                                    mine_Mithril_MiningGuild
                                                                  };

  /**
   * The list of all mine RSTiles for all ore material.
   */
  public static final RSTile[][]    mineTiles_All               = { mineTiles_Essence,
                                                                    mineTiles_Clay,
                                                                    mineTiles_Copper,
                                                                    mineTiles_Tin,
                                                                    mineTiles_Iron,
                                                                    mineTiles_Silver,
                                                                    mineTiles_Coal,
                                                                    mineTiles_Gold,
                                                                    mineTiles_Mithril
                                                                  };
}