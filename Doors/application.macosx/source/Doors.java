import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import com.heroicrobot.dropbit.registry.*; 
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel; 
import com.heroicrobot.dropbit.devices.pixelpusher.Strip; 
import java.util.*; 
import processing.core.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Doors extends PApplet {

//
// Code to run PixelPusher lights on the Heart Phoenix
// 







Screen screen;
char curMode = 'f';
char alt;

public void setup() {
//  frameRate(1);
  screen = new Screen();
}

public void keyPressed() {
  // Make the + key 'case insensitive'
  if (key == '=') {
    key = '+';
  }  

  if ((key != curMode) && ((key == 'f') || (key == 'y') || (key == 'c') || (key == 'i'))) {
    screen.resetLEDs();
    screen.clearPane(curMode);
    curMode = key;
  }
  if ((key != alt) && ((key == '+') || (key == '-') || (key == '0'))) {
    alt = key;
  }
}

public void draw() {

  switch (curMode) {
    case 'f': fxFlame(); break;
    case 'y': fxFairy(); break;
    case 'c': fxColumns(); break;
    case 'i': fxInferno(); break;
    default:
      //fxSweep();
      //fxBarSweep();
      break;
  }

  screen.paint(curMode);
}

// Effect Sweep
// A single light sweeps back and forth
int[] fxSweepPos = { 0, 20, 0, 20 };
int[] fxSweepDir = { 1, 1, 1, 1 };

public void fxSweep() {
  for (int strip=0; strip<4; strip++) {
    int pos = fxSweepPos[strip];
    int dir = fxSweepDir[strip];

    // Blank out the previous LED
    screen.ledVal[strip][pos] = color(0);

    // Set the direction for each lights movement
    pos += dir;
    fxSweepPos[strip] = pos;
    if ( pos >= screen.stripLen[strip]-1 ) {
      fxSweepDir[strip] = -1;
    } else if ( pos == 0 ) {
      fxSweepDir[strip] = 1;
    }

    // Set the new value
    screen.ledVal[strip][pos] = color(60, 100, 100);
  }
}

int fxBarSweepPos = 0;
int fxBarSweepDir = 1;
int fxBarSweepSpeed = 2;
int fxBarSweepColor = 0;

public void fxBarSweep() {
  int[] ledPair = screen.horzLineMap[fxBarSweepPos];

  // Clear out the last position
  for (int strip=0; strip<4; strip += 2) {
    screen.ledVal[strip][ledPair[0]] = color(0);
    screen.ledVal[strip+1][ledPair[1]] = color(0);
  }
  
  fxBarSweepPos += fxBarSweepDir;
  
  if (fxBarSweepPos >=334) {
    fxBarSweepPos = 334;
    fxBarSweepDir = -1 * fxBarSweepSpeed;
  } else if (fxBarSweepPos <= 0) {
    fxBarSweepPos = 0;
    fxBarSweepDir = 1 * fxBarSweepSpeed;
  }
  ledPair = screen.horzLineMap[fxBarSweepPos];

  fxBarSweepColor++;
  if (fxBarSweepColor > 100) {
    fxBarSweepColor = 0;
  }

  // Set the new position
  for (int strip=0; strip<4; strip += 2) {
    screen.ledVal[strip][ledPair[0]] = color(fxBarSweepColor, 100, 100);
    screen.ledVal[strip+1][ledPair[1]] = color(fxBarSweepColor, 100, 100);
  }
}

ArrayList<Mote> flame = new ArrayList<Mote>();
int maxMotes = 200; // Upper limit on number of motes to show
int maxCluster = 5; // Upper limit on number of motes to spawn at once

public void fxFlame() {
  if (alt == '+') {
    maxMotes += 20;
    if (maxMotes > 1000) {
      maxMotes = 1000; 
    }
    alt = '.';
  }
  if (alt == '-') {
    maxMotes -= 20;
    if (maxMotes < 10) {
      maxMotes = 10; 
    }
    alt = '.';
  }
  if (alt == '0') {
    maxMotes = 200;
    alt = '.';
  }
  
  // Add some new motes if we can
  if (flame.size() < maxMotes) {
    int spark = PApplet.parseInt(random(maxCluster));

    for (int i=0; i<spark; i++) {
      int stripNum = PApplet.parseInt(random(4));
      flame.add(new Mote(stripNum));
    }
  }

  // Clear out all the current values
  screen.resetLEDs();
  //screen.decayColor(40, 1);

  for (int i=0; i<flame.size(); i++) {
    Mote mote = flame.get(i);
    screen.ledVal[mote.strip][mote.pos] = mote.col;
    mote.move();
    if (mote.isDead()) {
      flame.remove(mote); 
    }
  }
}

ArrayList<Fairy> pinhead = new ArrayList<Fairy>();
int maxStripFairies = 2;
int[] fairiesOnStrip = {0, 0, 0, 0};
int waitTimer = 0;
int curColor = 0;

public void fxFairy() {
  if (alt == '+') {
    maxStripFairies++; 
    if (maxStripFairies > 20) {
       maxStripFairies = 20;
    }
    alt = '.';
  }
  if (alt == '-') {
    maxStripFairies--;
    if (maxStripFairies < 1) {
      maxStripFairies = 1;
    }
    alt = '.';
  }
  if (alt == '0') {
    maxStripFairies = 2; 
    alt = '.';
  }
  
  if (waitTimer <= 0) {
    int strip = PApplet.parseInt(random(4));
    int wildCard = PApplet.parseInt(random(100));
    if ((wildCard == 2) || fairiesOnStrip[strip] < maxStripFairies) {
      pinhead.add(new Fairy(strip, 60, curColor));
      fairiesOnStrip[strip]++;
      curColor = (curColor+PApplet.parseInt(random(10)))%360;
      waitTimer = PApplet.parseInt(random(150, 250));
    }
  } else {
    waitTimer--; 
  }
  
  // Clear out current values
  screen.resetLEDs();
  //screen.decayColor(20, 1);

  for (int n=0; n<pinhead.size(); n++) {
    Fairy fairy = pinhead.get(n);

    fairy.drawPixels(screen.ledVal[fairy.strip], screen.stripLen[fairy.strip]);

    fairy.move();
    if (fairy.isGone(screen.stripLen[fairy.strip])) {
      pinhead.remove(fairy);
      fairiesOnStrip[fairy.strip]--;
    }
  }
}

ArrayList<Column> colSet = new ArrayList<Column>();
int maxColumns = 7;
int fxColumnsWaitTimer = 0;
int fxColumnsCurColor = 0;
public void fxColumns() {
  if (alt == '+') {
    maxColumns++;
    if (maxColumns > 10) {
      maxColumns = 10;
    }
    alt = '.';
  }
  if (alt == '-') {
    maxColumns--;
    if (maxColumns < 1) {
      maxColumns = 1;
    }
    alt = '.';
  }
  if (alt == '0') {
    maxColumns = 2; 
    alt = '.';
  }
  
  if (fxColumnsWaitTimer <= 0) {
    if (colSet.size() < maxColumns) {
      colSet.add(new Column(fxColumnsCurColor));
      fxColumnsCurColor = (fxColumnsCurColor+PApplet.parseInt(random(10)))%360;
      fxColumnsWaitTimer = PApplet.parseInt(random(20, 80));
    }
  } else {
    fxColumnsWaitTimer--; 
  }
  
  // Clear out current values
  screen.decayColor(2);

  for (int n=0; n<colSet.size(); n++) {
    Column col = colSet.get(n);

    col.move();

    if (col.isGone(screen.horzSteps)) {
      colSet.remove(col);
    } else {
      screen.setHorzLeft(col.getPos(), col.col);
      screen.setHorzRight(col.getPos(), col.col);
    }
  }
}

ArrayList<Inferno> fire = new ArrayList<Inferno>();
int maxFlames = 1;
int fxInfernoWaitTimer = 0;
int fxInfernoCurColor = 20;
public void fxInferno() {
  if (alt == '+') {
    maxFlames++;
    if (maxFlames > 10) {
      maxFlames = 10;
    }
    alt = '.';
  }
  if (alt == '-') {
    maxFlames--;
    if (maxFlames < 1) {
      maxFlames = 1;
    }
    alt = '.';
  }
  if (alt == '0') {
    maxFlames = 1; 
    alt = '.';
  }
  
  if (fxInfernoWaitTimer <= 0) {
    if (fire.size() < maxFlames) {
      fire.add(new Inferno(random(40.0f, 60.0f), fxInfernoCurColor));
      fxInfernoCurColor = fxInfernoCurColor+PApplet.parseInt(random(20));
      if ((fxInfernoCurColor > 60) && (fxInfernoCurColor < 220)) {
        fxInfernoCurColor = 220;
      }
      if (fxInfernoCurColor > 260) {
        fxInfernoCurColor = 0; 
      }
      fxInfernoWaitTimer = PApplet.parseInt(random(20, 80));
    }
  } else {
    fxInfernoWaitTimer--; 
  }
  
  // Clear out current values
  //screen.resetLEDs();
  screen.decayToRed(0.25f, 1);

  for (int n=0; n<fire.size(); n++) {
    Inferno inf = fire.get(n);

    inf.move();

    if (inf.isGone(screen.vertSteps)) {
      fire.remove(inf);
    } else {
      screen.setVertLeft(screen.vertSteps - inf.getPos() - 1, inf.col);
      screen.setVertRight(screen.vertSteps - inf.getPos() - 1, inf.col);
    }
  }
}



int fxLinesCurPos = 0;
public void fxLines() {
  // Clear out current values
  screen.resetLEDs();
  
  screen.setVertLeft(fxLinesCurPos, color(60, 100, 100));
  screen.setVertRight(fxLinesCurPos, color(120, 100, 100));
  
  fxLinesCurPos++;
  if (fxLinesCurPos >= screen.vertSteps) {
    fxLinesCurPos = 0;
  } 
}
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
  
  // Map the pixels that fall one above the other horizontally; 270 pixel mappings
  private int[][] horzLineMap = { {0, 0},{1, 1},{2, 2},{3, 3},{3, 4},{4, 5},{5, 7},{6, 8},{7, 9},{7, 10},{8, 11},{9, 12},{10, 13},{11, 14},{12, 15},{12, 16},{13, 17},{14, 19},{15, 20},{16, 21},{16, 22},{17, 23},{18, 24},{19, 25},{20, 26},{21, 27},{21, 28},{22, 29},{23, 31},{24, 32},{25, 33},{25, 34},{26, 35},{27, 36},{28, 37},{29, 38},{30, 39},{30, 40},{31, 41},{32, 43},{33, 44},{34, 45},{34, 46},{35, 47},{36, 48},{37, 49},{38, 50},{39, 51},{39, 52},{40, 53},{41, 55},{42, 56},{43, 57},{43, 58},{44, 59},{45, 60},{46, 61},{47, 62},{48, 63},{48, 64},{49, 66},{50, 67},{51, 68},{52, 69},{52, 70},{53, 71},{54, 72},{55, 73},{56, 74},{57, 75},{57, 76},{58, 78},{59, 79},{60, 80},{61, 81},{62, 82},{62, 83},{63, 84},{64, 85},{65, 86},{66, 87},{66, 88},{67, 90},{68, 91},{69, 92},{70, 93},{71, 94},{71, 95},{72, 96},{73, 97},{74, 98},{75, 99},{75, 100},{76, 102},{77, 103},{78, 104},{79, 105},{80, 106},{80, 107},{81, 108},{82, 109},{83, 110},{84, 111},{84, 112},{85, 114},{86, 115},{87, 116},{88, 117},{89, 118},{89, 119},{90, 120},{91, 121},{92, 122},{93, 123},{93, 125},{94, 126},{95, 127},{96, 128},{97, 129},{98, 130},{98, 131},{99, 132},{100, 133},{101, 134},{102, 135},{102, 137},{103, 138},{104, 139},{105, 140},{106, 141},{107, 142},{107, 143},{108, 144},{109, 145},{110, 146},{111, 147},{111, 149},{112, 150},{113, 151},{114, 152},{115, 153},{116, 154},{116, 155},{117, 156},{118, 157},{119, 158},{120, 159},{120, 160},{121, 161},{122, 161},{123, 162},{124, 163},{125, 163},{125, 164},{126, 165},{127, 165},{128, 166},{129, 167},{129, 167},{130, 168},{131, 169},{132, 169},{133, 170},{134, 171},{134, 171},{135, 172},{136, 173},{137, 173},{138, 174},{138, 175},{139, 175},{140, 176},{141, 177},{142, 177},{143, 178},{143, 179},{144, 179},{145, 180},{146, 181},{147, 181},{147, 182},{148, 183},{149, 183},{150, 184},{151, 185},{152, 185},{152, 186},{153, 187},{154, 187},{155, 188},{156, 189},{156, 189},{157, 190},{158, 191},{159, 191},{160, 192},{161, 193},{161, 193},{162, 194},{163, 195},{164, 195},{165, 196},{166, 197},{166, 197},{167, 198},{168, 199},{169, 199},{170, 200},{170, 201},{171, 201},{172, 202},{173, 203},{174, 203},{175, 204},{175, 205},{176, 205},{177, 206},{178, 207},{179, 207},{179, 208},{180, 209},{181, 209},{182, 210},{183, 211},{184, 211},{184, 212},{185, 213},{186, 213},{187, 214},{188, 215},{188, 215},{189, 216},{190, 217},{191, 217},{192, 218},{193, 219},{193, 219},{194, 220},{195, 221},{196, 221},{197, 222},{197, 223},{198, 223},{199, 224},{200, 225},{201, 225},{202, 226},{202, 227},{203, 227},{204, 228},{205, 229},{206, 229},{206, 230},{207, 231},{208, 231},{209, 232},{210, 233},{211, 233},{211, 234},{212, 235},{213, 235},{214, 236},{215, 237},{215, 237},{216, 238},{217, 239},{218, 239},{219, 239},{220, 239},{220, 239} };

  // Number of pixel mappings in the horzLineMap
  public int horzSteps = 270;

  // Map the pixels that fall side by side each other vertically; 192 pixel mappings
  private int[][] vertLineMap = { {0, 0},{2, 1},{3, 2},{4, 3},{5, 4},{6, 5},{7, 6},{9, 6},{10, 7},{11, 8},{12, 9},{13, 10},{14, 11},{15, 11},{17, 12},{18, 13},{19, 14},{20, 15},{21, 16},{22, 16},{23, 17},{25, 18},{26, 19},{27, 20},{28, 21},{29, 22},{30, 22},{31, 23},{33, 24},{34, 25},{35, 26},{36, 27},{37, 27},{38, 28},{40, 29},{41, 30},{42, 31},{43, 32},{44, 32},{45, 33},{46, 34},{48, 35},{49, 36},{50, 37},{51, 38},{52, 38},{53, 39},{54, 40},{56, 41},{57, 42},{58, 43},{59, 43},{60, 44},{61, 45},{62, 46},{64, 47},{65, 48},{66, 48},{67, 49},{68, 50},{69, 51},{71, 52},{72, 53},{73, 54},{74, 54},{75, 55},{76, 56},{77, 57},{79, 58},{80, 59},{81, 59},{82, 60},{83, 61},{84, 62},{85, 63},{87, 64},{88, 64},{89, 65},{90, 66},{91, 67},{92, 68},{93, 69},{95, 69},{96, 70},{97, 71},{98, 72},{99, 73},{100, 74},{102, 75},{103, 75},{104, 76},{105, 77},{106, 78},{107, 79},{108, 80},{110, 80},{111, 81},{112, 82},{113, 83},{114, 84},{115, 85},{116, 85},{118, 86},{119, 87},{120, 88},{121, 89},{122, 90},{123, 91},{124, 91},{126, 92},{127, 93},{128, 94},{129, 95},{130, 96},{131, 96},{133, 97},{134, 98},{135, 99},{136, 100},{137, 101},{138, 101},{139, 102},{141, 103},{142, 104},{143, 105},{144, 106},{145, 107},{146, 107},{147, 108},{149, 109},{150, 110},{151, 111},{152, 112},{153, 112},{154, 113},{155, 114},{157, 115},{158, 116},{159, 117},{160, 117},{161, 118},{162, 119},{164, 120},{165, 121},{166, 122},{167, 123},{168, 123},{169, 124},{170, 125},{172, 126},{173, 127},{174, 128},{175, 128},{176, 129},{177, 130},{178, 131},{180, 132},{181, 133},{182, 133},{183, 134},{184, 135},{185, 136},{186, 137},{188, 138},{189, 138},{190, 139},{191, 140},{192, 141},{193, 142},{195, 143},{196, 144},{197, 144},{198, 145},{199, 146},{200, 147},{201, 148},{203, 149},{204, 149},{205, 150},{206, 151},{207, 152},{208, 153},{209, 154},{211, 154},{212, 155},{213, 156},{214, 157},{215, 158},{216, 159},{217, 239},{219, 239},{220, 239} };

  // Number of pixel mappings in the vertLineMap
  public int vertSteps = 192;

  // Color value of each LED
  public int[][] ledVal = new int[4][240];

  private int canvasWidth = 1100;
  private int canvasHeight = 400;
  
  private DeviceRegistry registry;
  
  Screen() {
    size(canvasWidth, canvasHeight);
      
    background(0);
    colorMode(HSB, 360, 100, 100);
  
    registry = new DeviceRegistry();
    registry.setExtraDelay(0);
    registry.startPushing();
  }
  
  public void setHorzLeft(int pos, int col) {
    int[] pair = horzLineMap[pos];
    ledVal[0][pair[0]] = col;
    ledVal[1][pair[1]] = col;
  }
  
  public void setHorzRight(int pos, int col) {
    int[] pair = horzLineMap[pos];
    ledVal[2][pair[0]] = col;
    ledVal[3][pair[1]] = col;
  }
  
  public void setVertLeft(int pos, int col) {
    int[] pair = vertLineMap[pos];
    ledVal[0][pair[0]] = col;
    ledVal[1][pair[1]] = col;
    
    if (pos >= vertSteps-1) {
      for (int x=159; x<stripLen[1]; x++) {
        ledVal[1][x] = col; 
      }
    }
  }
  
  public void setVertRight(int pos, int col) {
    int[] pair = vertLineMap[pos];
    ledVal[2][pair[0]] = col;
    ledVal[3][pair[1]] = col;
    
    if (pos >= vertSteps-1) {
      for (int x=159; x<stripLen[1]; x++) {
        ledVal[3][x] = col; 
      }
    }
  }
  
  public void paint(char curMode) {
    clearPane(curMode);

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

  public void resetLEDs() {
    for (int strip=0; strip<4; strip++) {
      for (int led=0; led<240; led++) {
        ledVal[strip][led] = color(0);
      } 
    }
  }
  
  public void decayColor(int decay) {
    decayColor(decay, 5);
  }
  
  public void decayColor(int decay, int colorShift) {
    for (int strip=0; strip<4; strip++) {
      for (int led=0; led<240; led++) {
        int curCol = ledVal[strip][led];
        float bright = brightness(curCol);
        if (bright > 0) {
          float hue = (hue(curCol)+colorShift)%360;
          bright -= decay;
          if (bright < 0) {
            bright = 0; 
          }
          
          ledVal[strip][led] = color(hue, saturation(curCol), bright);
        } else {
          ledVal[strip][led] = color(0);
        }
      } 
    }
  }
  
  public void decayToRed(float decay, float colorShift) {
    for (int strip=0; strip<4; strip++) {
      for (int led=0; led<240; led++) {
        int curCol = ledVal[strip][led];
        float bright = brightness(curCol);
        if (bright > 0) {
          float hue;
          if ((hue(curCol) > 0) && (hue(curCol) <= 60)) {
            hue = (hue(curCol)-colorShift);
          } else if ((hue(curCol) >= 220) && (hue(curCol) < 360)) {
            hue = (hue(curCol)+colorShift);
          } else {
            hue = 0; 
          }
          bright -= decay;
          if (bright < 0) {
            bright = 0; 
          }
          
          ledVal[strip][led] = color(hue, saturation(curCol), bright);
        } else {
          ledVal[strip][led] = color(0);
        }
      } 
    }
  }

  public void clearPane(char curKey) {
    fill(0);
    rect(0, 0, width, height);

    fill(0, 0, 100);
    text("Key Mappings\n", 480, 200);
    if (curKey == 'f') { fill(100, 100, 100); } else { fill(100, 100, 50); }
    text("(f) fire effect\n", 480, 220);
    if (curKey == 'y') { fill(100, 100, 100); } else { fill(100, 100, 50); }
    text("(y) fairy effect\n", 480, 240);
    if (curKey == 'c') { fill(100, 100, 100); } else { fill(100, 100, 50); }
    text("(c) columns effect\n", 480, 260);
    if (curKey == 'i') { fill(100, 100, 100); } else { fill(100, 100, 50); }
    text("(i) inferno effect\n", 480, 280);
    fill(40, 100, 80);
    text("(+) increase\n", 480, 320);
    text("(-) decrease\n", 480, 340);
    text("(0) reset to default\n", 480, 360);

    stroke(0,0,50);
    fill(0,0,0);
    triangle(545, 10, 253, 387, 13, 387);
    triangle(555, 10, 846, 387, 1086, 387);
    

  
    stroke(0, 0, 0);
  }
}
class Column {
  public int col;
 
  private float pos;
  private float speed;
 
  Column(int c) {
    col = color(c, 100, 100);
    pos = 0.0f;
    speed = 0.75f;
  }
  
  public int getPos() {
    return PApplet.parseInt(pos); 
  }
  
  public void move() {
    pos += speed;
  }

  public boolean isGone(int len) {
    if (getPos() >= len) {
      return true; 
    } else {
      return false; 
    }
  }
}
class Fairy {
  public int col;   // Fairy color
  public int strip;   // Which strip is this fairy on

  private float pos;         // Where this fairy currently is
  private float speed;       // How fast this fairy is moving
  private int haloMaxSize;   // Max radius of the halo
  private float haloCurSize; // Current size of the halo
  private float haloSpeed;   // How fast the halo expands
  
  Fairy(int stripNum, int haloSize, int hue) {
    col = color(hue, 100, 100);
    speed = 0.2f;// random(0.05,.5);
    pos = 0.0f;
    strip = stripNum;
    haloMaxSize = haloSize;
    haloCurSize = 0.0f;
    haloSpeed = random(0.5f,1.5f);
  }

  public int getPos() {
    return PApplet.parseInt(pos); 
  }

  public void move() {
     pos += speed;
     
     if (haloCurSize >= haloMaxSize) {
       haloCurSize = 0.0f;
     }
     
     haloCurSize += haloSpeed;
  }

  public int haloTrailPos() {
    return getPos() - PApplet.parseInt(haloCurSize);
  }
  
  public int haloLeadPos() {
    return getPos() + PApplet.parseInt(haloCurSize);
  }

  public void drawPixels(int[] ledVal, int len) {
    int max = PApplet.parseInt(2*haloCurSize+1);
    float brightAdj = 1 - (float)haloCurSize/haloMaxSize;
    int haloCol = color(hue(col), saturation(col), brightAdj*brightness(col));
    
    // Draw the fairy
    if (haloCurSize < 6.0f) {
      if (PApplet.parseInt(random(3)) == 2) {
         ledVal[getPos()] = color(0, 0, 100);
      } else {
        ledVal[getPos()] = col;
      }
    } else {
      ledVal[getPos()] = col;
    }
    
    // Draw the halo unless its out of bounds
    if (haloTrailPos() >= 0) {
      ledVal[haloTrailPos()] = haloCol;
    }
    if (haloLeadPos() < len) {
      ledVal[haloLeadPos()] = haloCol;
    }
  }

  public boolean isGone(int len) {
    if (getPos() >= len) {
      return true; 
    } else {
      return false; 
    }
  }
}

class Mote {
  public int col; // The color of this mote
  public int speed; // How fast this mote is moving
  public int pos;   // Where this mote currently is
  public int life;  // How long this mote will last until its dead
  public int strip; // Which strip is this mote on
  
  Mote(int stripNum) {
    int hueRange = PApplet.parseInt(random(100));
    int hue;
    if (hueRange > 90) {
      hue = PApplet.parseInt(random(40, 60));
    } else if (hueRange > 80) {
      hue = PApplet.parseInt(random(20, 40));
    } else {
      hue = PApplet.parseInt(random(0, 20));
    }
    
    // Get a random color in the red/orange/yellow area with higher saturation and value numbers
    col = color(hue, PApplet.parseInt(random(90,100)), PApplet.parseInt(random(70,100)));
    speed = PApplet.parseInt(random(1,4));

    // The longest this should last is the number of steps needed to get to
    // the end of the strip    
    life = PApplet.parseInt(random(200/speed));

    pos = 0;
    strip = stripNum;
  }
  
  public void move() {
     pos += speed;
     life--;

     // If we're out of life, turn black
     if (life <= 0) {
       life = 0;
       col = color(0); 
     }
  }
  
  public boolean isDead() {
    return life == 0; 
  }
};
class Inferno {
  public float speed;
  public int col;

  private float pos;
  private int time;
  private float decay;
  
  Inferno(float sp, int c) {
    speed = sp;
    col = color(c, 100, 100);
    time = millis();
    decay = 0.25f;
  }

  public int getPos() {
    return PApplet.parseInt(pos); 
  }

  public void move() {
    float t = (millis() - time)/1000.0f;
    pos = (speed*t) - (.5f*9.8f*sq(t));
    
    // If acceleration starts winning out, decay the flame
    if ((speed-(9.8f*t)) < 0) {
      float b = brightness(col);
      if (b <= 0) {
        b = 0;
      } else {
        col = color(hue(col), saturation(col), b - decay); 
      }
    }
  }

  public boolean isGone(int len) {
    if ((getPos() < 0) || (getPos() >= len)) {
      return true; 
    } else {
      return false; 
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Doors" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
