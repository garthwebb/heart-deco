class Screen {
  // Positions of the LEDs in each strip
  private int[][][] ledPos = {
    // 221 lights - Top strip, left triangle
    { {545, 10}, {542, 11}, {540, 13}, {537, 15}, {535, 16}, {532, 18}, {530, 20}, {527, 22}, {525, 23}, {523, 25}, {520, 27}, {518, 29}, {515, 30}, {513, 32}, {510, 34}, {508, 36}, {505, 37}, {503, 39}, {501, 41}, {498, 43}, {496, 44}, {493, 46}, {491, 48}, {488, 50}, {486, 51}, {483, 53}, {481, 55}, {479, 57}, {476, 58}, {474, 60}, {471, 62}, {469, 64}, {466, 65}, {464, 67}, {461, 69}, {459, 70}, {457, 72}, {454, 74}, {452, 76}, {449, 77}, {447, 79}, {444, 81}, {442, 83}, {439, 84}, {437, 86}, {435, 88}, {432, 90}, {430, 91}, {427, 93}, {425, 95}, {422, 97}, {420, 98}, {417, 100}, {415, 102}, {413, 104}, {410, 105}, {408, 107}, {405, 109}, {403, 111}, {400, 112}, {398, 114}, {396, 116}, {393, 118}, {391, 119}, {388, 121}, {386, 123}, {383, 124}, {381, 126}, {378, 128}, {376, 130}, {374, 131}, {371, 133}, {369, 135}, {366, 137}, {364, 138}, {361, 140}, {359, 142}, {356, 144}, {354, 145}, {352, 147}, {349, 149}, {347, 151}, {344, 152}, {342, 154}, {339, 156}, {337, 158}, {334, 159}, {332, 161}, {330, 163}, {327, 165}, {325, 166}, {322, 168}, {320, 170}, {317, 172}, {315, 173}, {312, 175}, {310, 177}, {308, 178}, {305, 180}, {303, 182}, {300, 184}, {298, 185}, {295, 187}, {293, 189}, {290, 191}, {288, 192}, {286, 194}, {283, 196}, {281, 198}, {278, 199}, {276, 201}, {273, 203}, {271, 205}, {269, 206}, {266, 208}, {264, 210}, {261, 212}, {259, 213}, {256, 215}, {254, 217}, {251, 219}, {249, 220}, {247, 222}, {244, 224}, {242, 226}, {239, 227}, {237, 229}, {234, 231}, {232, 232}, {229, 234}, {227, 236}, {225, 238}, {222, 239}, {220, 241}, {217, 243}, {215, 245}, {212, 246}, {210, 248}, {207, 250}, {205, 252}, {203, 253}, {200, 255}, {198, 257}, {195, 259}, {193, 260}, {190, 262}, {188, 264}, {185, 266}, {183, 267}, {181, 269}, {178, 271}, {176, 273}, {173, 274}, {171, 276}, {168, 278}, {166, 280}, {163, 281}, {161, 283}, {159, 285}, {156, 286}, {154, 288}, {151, 290}, {149, 292}, {146, 293}, {144, 295}, {142, 297}, {139, 299}, {137, 300}, {134, 302}, {132, 304}, {129, 306}, {127, 307}, {124, 309}, {122, 311}, {120, 313}, {117, 314}, {115, 316}, {112, 318}, {110, 320}, {107, 321}, {105, 323}, {102, 325}, {100, 327}, {98, 328}, {95, 330}, {93, 332}, {90, 334}, {88, 335}, {85, 337}, {83, 339}, {80, 341}, {78, 342}, {76, 344}, {73, 346}, {71, 347}, {68, 349}, {66, 351}, {63, 353}, {61, 354}, {58, 356}, {56, 358}, {54, 360}, {51, 361}, {49, 363}, {46, 365}, {44, 367}, {41, 368}, {39, 370}, {36, 372}, {34, 374}, {32, 375}, {29, 377}, {27, 379}, {24, 381}, {22, 382}, {19, 384}, {17, 386}, {15, 388}, {12, 389}, {10, 391}, {7, 393} },
    // 240 lights - Bottom strip, left triangle
    {
      // Section 1
      {545, 10}, {543, 12}, {541, 14}, {539, 17}, {537, 19}, {535, 21}, {534, 24}, {532, 26}, {530, 29}, {528, 31}, {526, 33}, {524, 36}, {523, 38}, {521, 40}, {519, 43}, {517, 45}, {515, 48}, {513, 50}, {512, 52}, {510, 55}, {508, 57}, {506, 59}, {504, 62}, {502, 64}, {501, 67}, {499, 69}, {497, 71}, {495, 74}, {493, 76}, {491, 78}, {490, 81}, {488, 83}, {486, 86}, {484, 88}, {482, 90}, {480, 93}, {479, 95}, {477, 97}, {475, 100}, {473, 102}, {471, 105}, {469, 107}, {468, 109}, {466, 112}, {464, 114}, {462, 116}, {460, 119}, {458, 121}, {457, 124}, {455, 126}, {453, 128}, {451, 131}, {449, 133}, {447, 135}, {446, 138}, {444, 140}, {442, 143}, {440, 145}, {438, 147}, {437, 150}, {435, 152}, {433, 154}, {431, 157}, {429, 159}, {427, 162}, {426, 164}, {424, 166}, {422, 169}, {420, 171}, {418, 174}, {416, 176}, {415, 178}, {413, 181}, {411, 183}, {409, 185}, {407, 188}, {405, 190}, {404, 193}, {402, 195}, {400, 197}, {398, 200}, {396, 202}, {394, 204}, {393, 207}, {391, 209}, {389, 212}, {387, 214}, {385, 216}, {383, 219}, {382, 221}, {380, 223}, {378, 226}, {376, 228}, {374, 231}, {372, 233}, {371, 235}, {369, 238}, {367, 240}, {365, 242}, {363, 245}, {361, 247}, {360, 250}, {358, 252}, {356, 254}, {354, 257}, {352, 259}, {350, 261}, {349, 264}, {347, 266}, {345, 269}, {343, 271}, {341, 273}, {339, 276}, {338, 278}, {336, 280}, {334, 283}, {332, 285}, {330, 288}, {329, 290}, {327, 292}, {325, 295}, {323, 297}, {321, 299}, {319, 302}, {318, 304}, {316, 307}, {314, 309}, {312, 311}, {310, 314}, {308, 316}, {307, 318}, {305, 321}, {303, 323}, {301, 326}, {299, 328}, {297, 330}, {296, 333}, {294, 335}, {292, 338}, {290, 340}, {288, 342}, {286, 345}, {285, 347}, {283, 349}, {281, 352}, {279, 354}, {277, 357}, {275, 359}, {274, 361}, {272, 364}, {270, 366}, {268, 368}, {266, 371}, {264, 373}, {263, 376}, {261, 378}, {259, 380}, {257, 383}, {255, 385},
      // Section 2
      {253, 387}, {250, 387}, {247, 387}, {244, 387}, {241, 387}, {238, 387}, {235, 387}, {232, 387}, {229, 387}, {226, 387}, {223, 387}, {220, 387}, {217, 387}, {214, 387}, {211, 387}, {208, 387}, {205, 387}, {202, 387}, {199, 387}, {196, 387}, {193, 387}, {190, 387}, {187, 387}, {184, 387}, {181, 387}, {178, 387}, {175, 387}, {172, 387}, {169, 387}, {166, 387}, {163, 387}, {160, 387}, {157, 387}, {154, 387}, {151, 387}, {148, 387}, {145, 387}, {142, 387}, {139, 387}, {136, 387}, {133, 387}, {130, 387}, {127, 387}, {124, 387}, {121, 387}, {118, 387}, {115, 387}, {112, 387}, {109, 387}, {106, 387}, {103, 387}, {100, 387}, {97, 387}, {94, 387}, {91, 387}, {88, 387}, {85, 387}, {82, 387}, {79, 387}, {76, 387}, {73, 387}, {70, 387}, {67, 387}, {64, 387}, {61, 387}, {58, 387}, {55, 387}, {52, 387}, {49, 387}, {46, 387}, {43, 387}, {40, 387}, {37, 387}, {34, 387}, {31, 387}, {28, 387}, {25, 387}, {22, 387}, {19, 387}, {16, 387}, {13, 387}
    },

    // 221 lights - Top strip, right triangle
    { {555, 10}, {557, 11}, {559, 13}, {562, 15}, {564, 16}, {567, 18}, {569, 20}, {572, 22}, {574, 23}, {576, 25}, {579, 27}, {581, 29}, {584, 30}, {586, 32}, {589, 34}, {591, 36}, {594, 37}, {596, 39}, {598, 41}, {601, 43}, {603, 44}, {606, 46}, {608, 48}, {611, 50}, {613, 51}, {616, 53}, {618, 55}, {620, 57}, {623, 58}, {625, 60}, {628, 62}, {630, 64}, {633, 65}, {635, 67}, {638, 69}, {640, 70}, {642, 72}, {645, 74}, {647, 76}, {650, 77}, {652, 79}, {655, 81}, {657, 83}, {660, 84}, {662, 86}, {664, 88}, {667, 90}, {669, 91}, {672, 93}, {674, 95}, {677, 97}, {679, 98}, {682, 100}, {684, 102}, {686, 104}, {689, 105}, {691, 107}, {694, 109}, {696, 111}, {699, 112}, {701, 114}, {703, 116}, {706, 118}, {708, 119}, {711, 121}, {713, 123}, {716, 124}, {718, 126}, {721, 128}, {723, 130}, {725, 131}, {728, 133}, {730, 135}, {733, 137}, {735, 138}, {738, 140}, {740, 142}, {743, 144}, {745, 145}, {747, 147}, {750, 149}, {752, 151}, {755, 152}, {757, 154}, {760, 156}, {762, 158}, {765, 159}, {767, 161}, {769, 163}, {772, 165}, {774, 166}, {777, 168}, {779, 170}, {782, 172}, {784, 173}, {787, 175}, {789, 177}, {791, 178}, {794, 180}, {796, 182}, {799, 184}, {801, 185}, {804, 187}, {806, 189}, {809, 191}, {811, 192}, {813, 194}, {816, 196}, {818, 198}, {821, 199}, {823, 201}, {826, 203}, {828, 205}, {830, 206}, {833, 208}, {835, 210}, {838, 212}, {840, 213}, {843, 215}, {845, 217}, {848, 219}, {850, 220}, {852, 222}, {855, 224}, {857, 226}, {860, 227}, {862, 229}, {865, 231}, {867, 232}, {870, 234}, {872, 236}, {874, 238}, {877, 239}, {879, 241}, {882, 243}, {884, 245}, {887, 246}, {889, 248}, {892, 250}, {894, 252}, {896, 253}, {899, 255}, {901, 257}, {904, 259}, {906, 260}, {909, 262}, {911, 264}, {914, 266}, {916, 267}, {918, 269}, {921, 271}, {923, 273}, {926, 274}, {928, 276}, {931, 278}, {933, 280}, {936, 281}, {938, 283}, {940, 285}, {943, 286}, {945, 288}, {948, 290}, {950, 292}, {953, 293}, {955, 295}, {957, 297}, {960, 299}, {962, 300}, {965, 302}, {967, 304}, {970, 306}, {972, 307}, {975, 309}, {977, 311}, {979, 313}, {982, 314}, {984, 316}, {987, 318}, {989, 320}, {992, 321}, {994, 323}, {997, 325}, {999, 327}, {1001, 328}, {1004, 330}, {1006, 332}, {1009, 334}, {1011, 335}, {1014, 337}, {1016, 339}, {1019, 341}, {1021, 342}, {1023, 344}, {1026, 346}, {1028, 347}, {1031, 349}, {1033, 351}, {1036, 353}, {1038, 354}, {1041, 356}, {1043, 358}, {1045, 360}, {1048, 361}, {1050, 363}, {1053, 365}, {1055, 367}, {1058, 368}, {1060, 370}, {1063, 372}, {1065, 374}, {1067, 375}, {1070, 377}, {1072, 379}, {1075, 381}, {1077, 382}, {1080, 384}, {1082, 386}, {1084, 388}, {1087, 389}, {1089, 391}, {1092, 393} },
    // 240 lights - Bottom strip, right triangle
    {
      // Section 1
      {555, 10}, {556, 12}, {558, 14}, {560, 17}, {562, 19}, {564, 21}, {565, 24}, {567, 26}, {569, 29}, {571, 31}, {573, 33}, {575, 36}, {576, 38}, {578, 40}, {580, 43}, {582, 45}, {584, 48}, {586, 50}, {587, 52}, {589, 55}, {591, 57}, {593, 59}, {595, 62}, {597, 64}, {598, 67}, {600, 69}, {602, 71}, {604, 74}, {606, 76}, {608, 78}, {609, 81}, {611, 83}, {613, 86}, {615, 88}, {617, 90}, {619, 93}, {620, 95}, {622, 97}, {624, 100}, {626, 102}, {628, 105}, {630, 107}, {631, 109}, {633, 112}, {635, 114}, {637, 116}, {639, 119}, {641, 121}, {642, 124}, {644, 126}, {646, 128}, {648, 131}, {650, 133}, {652, 135}, {653, 138}, {655, 140}, {657, 143}, {659, 145}, {661, 147}, {662, 150}, {664, 152}, {666, 154}, {668, 157}, {670, 159}, {672, 162}, {673, 164}, {675, 166}, {677, 169}, {679, 171}, {681, 174}, {683, 176}, {684, 178}, {686, 181}, {688, 183}, {690, 185}, {692, 188}, {694, 190}, {695, 193}, {697, 195}, {699, 197}, {701, 200}, {703, 202}, {705, 204}, {706, 207}, {708, 209}, {710, 212}, {712, 214}, {714, 216}, {716, 219}, {717, 221}, {719, 223}, {721, 226}, {723, 228}, {725, 231}, {727, 233}, {728, 235}, {730, 238}, {732, 240}, {734, 242}, {736, 245}, {738, 247}, {739, 250}, {741, 252}, {743, 254}, {745, 257}, {747, 259}, {749, 261}, {750, 264}, {752, 266}, {754, 269}, {756, 271}, {758, 273}, {760, 276}, {761, 278}, {763, 280}, {765, 283}, {767, 285}, {769, 288}, {770, 290}, {772, 292}, {774, 295}, {776, 297}, {778, 299}, {780, 302}, {781, 304}, {783, 307}, {785, 309}, {787, 311}, {789, 314}, {791, 316}, {792, 318}, {794, 321}, {796, 323}, {798, 326}, {800, 328}, {802, 330}, {803, 333}, {805, 335}, {807, 338}, {809, 340}, {811, 342}, {813, 345}, {814, 347}, {816, 349}, {818, 352}, {820, 354}, {822, 357}, {824, 359}, {825, 361}, {827, 364}, {829, 366}, {831, 368}, {833, 371}, {835, 373}, {836, 376}, {838, 378}, {840, 380}, {842, 383}, {844, 385},
      // Section 2
      {846, 387}, {849, 387}, {852, 387}, {855, 387}, {858, 387}, {861, 387}, {864, 387}, {867, 387}, {870, 387}, {873, 387}, {876, 387}, {879, 387}, {882, 387}, {885, 387}, {888, 387}, {891, 387}, {894, 387}, {897, 387}, {900, 387}, {903, 387}, {906, 387}, {909, 387}, {912, 387}, {915, 387}, {918, 387}, {921, 387}, {924, 387}, {927, 387}, {930, 387}, {933, 387}, {936, 387}, {939, 387}, {942, 387}, {945, 387}, {948, 387}, {951, 387}, {954, 387}, {957, 387}, {960, 387}, {963, 387}, {966, 387}, {969, 387}, {972, 387}, {975, 387}, {978, 387}, {981, 387}, {984, 387}, {987, 387}, {990, 387}, {993, 387}, {996, 387}, {999, 387}, {1002, 387}, {1005, 387}, {1008, 387}, {1011, 387}, {1014, 387}, {1017, 387}, {1020, 387}, {1023, 387}, {1026, 387}, {1029, 387}, {1032, 387}, {1035, 387}, {1038, 387}, {1041, 387}, {1044, 387}, {1047, 387}, {1050, 387}, {1053, 387}, {1056, 387}, {1059, 387}, {1062, 387}, {1065, 387}, {1068, 387}, {1071, 387}, {1074, 387}, {1077, 387}, {1080, 387}, {1083, 387}, {1086, 387}
    }
  };
 
  public int[] stripLen = { 221, 240, 221, 240 };

  // The order the pixels should be lit in order to form a horizontal "line" between the two strips
  public int[][] horzOrder = { };

  // Color value of each LED
  public color[][] ledVal = new color[4][240];

  private int canvasWidth = 1100;
  private int canvasHeight = 400;
  
  private DeviceRegistry registry;
  
  Screen() {
    size(canvasWidth, canvasHeight);
      
    background(0);
    colorMode(HSB, 100);
  
    registry = new DeviceRegistry();
    registry.setExtraDelay(0);
    registry.startPushing();
  }
  
  void paint() {
    clearPane();

    List<Strip> allStrips = registry.getStrips();
    int numStrips = allStrips.size();

    for (int strip=0; strip<4; strip++) {
      for (int led=0; led<stripLen[strip]; led++) {
        int[] pos = ledPos[strip][led];
        int c = ledVal[strip][led];

        if (strip < numStrips) {
          Strip curStrip = allStrips.get(strip);
          curStrip.setPixel(c, led);
        }

        if ( c != color(0) ) {
          fill(c);
          ellipse(pos[0], pos[1], 10, 10);
        }
      }
    }
  }

  void resetLEDs() {
    for (int strip=0; strip<4; strip++) {
      for (int led=0; led<240; led++) {
        ledVal[strip][led] = color(0);
      } 
    }
  }

  void clearPane() {
    fill(0);
    rect(0, 0, width, height);
  
    stroke(0,0,50);
    fill(0,0,0);
    triangle(545, 10, 253, 387, 13, 387);
    triangle(555, 10, 846, 387, 1086, 387);
  
    stroke(0, 0, 0);
  }
}
