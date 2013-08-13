//
// Code to run PixelPusher lights on the Heart Phoenix
// 

import com.heroicrobot.dropbit.registry.*;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import java.util.*;
import processing.core.*;

Screen screen;

void setup() {
//  frameRate(1);
  screen = new Screen();
}

void draw() {
  //fxSweep();
  //fxBarSweep();
  //fxFlame();
  fxFairy();
  screen.paint();
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
  int[] ledPair = screen.horzOrder[fxBarSweepPos];

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
  ledPair = screen.horzOrder[fxBarSweepPos];

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
  // Add some new motes if we can
  if (flame.size() < maxMotes) {
    int spark = int(random(maxCluster));
//println("Flame.size() == " + flame.size() + " : sparking " + spark);
    for (int i=0; i<spark; i++) {
      int stripNum = int(random(4));
      flame.add(new Mote(stripNum));
    }
  }

  // Clear out all the current values
  screen.resetLEDs();

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
int maxFairies = 1;

void fxFairy() {
  if (pinhead.size() < maxFairies) {
    int stripNum = int(random(4));
    pinhead.add(new Fairy(stripNum, 20)); 
  }
  
  // Clear out current values
  screen.resetLEDs();

  for (int n=0; n<pinhead.size(); n++) {
    Fairy fairy = pinhead.get(n);

    IntList pixels = fairy.getPixels();
//    print("Fairy out: ");
    for (int p=fairy.startPixel(); p<fairy.startPixel()+pixels.size(); p++) {
//      print(p+":"+brightness(pixels.get(p-fairy.startPixel())) + " / ");
      // Ignore pixels that are out of bounds
      if ((p >= 0) && (p < screen.stripLen[fairy.strip])) {
        screen.ledVal[fairy.strip][p] = pixels.get(p-fairy.startPixel());
      }
    }
//    println();

    fairy.move();
    if (fairy.isGone(screen.stripLen[fairy.strip])) {
      pinhead.remove(fairy); 
    }
  }
}
