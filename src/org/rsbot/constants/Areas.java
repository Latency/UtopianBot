package org.rsbot.constants;

import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.RSArea;

@ScriptManifest(authors = "Latency", name = "Areas", version = 1.0, description = "Global static constants.")
public interface Areas {
  // ///////////////////////////////////////////////////////////////////////
  // Areas
  // ///////////////////////////////////////////////////////////////////////
  /**
   * The RSArea for misc locations.
   */
  public static final RSArea                                Area_GuildStairsTop                 = new RSArea(3015, 3336, 3024, 3342),
                                                            Area_GuildStairsBottom              = new RSArea(3017, 9736, 3023, 9742),
                                                            Area_Guild                          = new RSArea(3024, 9732, 3055, 9756),
                                                            Area_PhoenixGangEntrance            = new RSArea(3242, 3380, 3244, 3383),
                                                            Area_BlackArmGangEntrance           = new RSArea(3182, 3382, 3195, 3387),
                                                            Area_Fally                          = new RSArea(3005, 3326, 3042, 3371);

  // ///////////////////////////////////////////////////////////////////////
  // Banks
  // ///////////////////////////////////////////////////////////////////////
  /**
   * The RSArea for the bank found in the corresponding location.
   */
  public static final RSArea                                bankArea_WestVarrock                = new RSArea(3179, 3194, 3189, 3446),
                                                            bankArea_EastVarrock                = new RSArea(3250, 3416, 3257, 3423),
                                                            bankArea_CentralFalador             = new RSArea(3009, 3353, 3018, 3358),
                                                            bankArea_Edgeville                  = new RSArea(3091, 3488, 3098, 3499),
                                                            bankArea_Draynor                    = new RSArea(3088, 3240, 3097, 3246),
                                                            bankArea_Lumbridge                  = new RSArea(3207, 3215, 3210, 3222),
                                                            bankArea_AlKharid                   = new RSArea(3265, 3161, 3272, 3173),
                                                            bankArea_Arena                      = new RSArea(3380, 3267, 3385, 3273);
                                                            //bankArea_Canifis                  = new RSRSArea(),
                                                            //bankArea_BurghDeRott              = new RSRSArea(),
                                                            //bankArea_PortPhasmatys            = new RSRSArea(),
                                                            //bankArea_GeneralExchange          = new RSRSArea(3139, 3468, 3186, 3516).union(new RSRSArea(3187, 3476, 3189, 3516)).union(new RSRSArea(3197, 3506, 3197, 3507)).union(new RSRSArea(3196, 3505, 3196, 3508)).union(new RSRSArea(3195, 3504, 3195, 3509)).union(new RSRSArea(3194, 3502, 3194, 3510)).union(new RSRSArea(3193, 3501, 3193, 3511)).union(new RSRSArea(3192, 3500, 3192, 3512)).union(new RSRSArea(3191, 3499, 3191, 3513)).union(new RSRSArea(3190, 3498, 3190, 3514)).minus(new RSRSArea(3139, 3483, 3141, 3493)).minus(new RSRSArea(3139, 3513, 3143, 3516)).minus(new RSRSArea(3159, 3513, 3170, 3516)),
                                                            //bankArea_WestFalador              = new RSRSArea(2943, 3368, 2947, 3373).union(new RSRSArea(2948, 3368, 2949, 3368));
}