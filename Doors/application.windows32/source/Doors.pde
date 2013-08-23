//
// Code to run PixelPusher lights on the Heart Phoenix
// 

import com.heroicrobot.dropbit.registry.*;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import java.util.*;
import processing.core.*;

Screen screen;
char curMode = 'f';
char alt;

void setup() {
//  frameRate(1);
  screen = new Screen();
}

void keyPressed() {
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

void draw() {

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

void fxSweep() {
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

void fxBarSweep() {
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

void fxFlame() {
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
    int spark = int(random(maxCluster));

    for (int i=0; i<spark; i++) {
      int stripNum = int(random(4));
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

void fxFairy() {
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
    int strip = int(random(4));
    int wildCard = int(random(100));
    if ((wildCard == 2) || fairiesOnStrip[strip] < maxStripFairies) {
      pinhead.add(new Fairy(strip, 60, curColor));
      fairiesOnStrip[strip]++;
      curColor = (curColor+int(random(10)))%360;
      waitTimer = int(random(150, 250));
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
void fxColumns() {
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
      fxColumnsCurColor = (fxColumnsCurColor+int(random(10)))%360;
      fxColumnsWaitTimer = int(random(20, 80));
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
void fxInferno() {
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
      fire.add(new Inferno(random(40.0, 60.0), fxInfernoCurColor));
      fxInfernoCurColor = fxInfernoCurColor+int(random(20));
      if ((fxInfernoCurColor > 60) && (fxInfernoCurColor < 220)) {
        fxInfernoCurColor = 220;
      }
      if (fxInfernoCurColor > 260) {
        fxInfernoCurColor = 0; 
      }
      fxInfernoWaitTimer = int(random(20, 80));
    }
  } else {
    fxInfernoWaitTimer--; 
  }
  
  // Clear out current values
  //screen.resetLEDs();
  screen.decayToRed(0.25, 1);

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
void fxLines() {
  // Clear out current values
  screen.resetLEDs();
  
  screen.setVertLeft(fxLinesCurPos, color(60, 100, 100));
  screen.setVertRight(fxLinesCurPos, color(120, 100, 100));
  
  fxLinesCurPos++;
  if (fxLinesCurPos >= screen.vertSteps) {
    fxLinesCurPos = 0;
  } 
}
