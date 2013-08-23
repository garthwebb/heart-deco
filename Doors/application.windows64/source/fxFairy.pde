class Fairy {
  public color col;   // Fairy color
  public int strip;   // Which strip is this fairy on

  private float pos;         // Where this fairy currently is
  private float speed;       // How fast this fairy is moving
  private int haloMaxSize;   // Max radius of the halo
  private float haloCurSize; // Current size of the halo
  private float haloSpeed;   // How fast the halo expands
  
  Fairy(int stripNum, int haloSize, int hue) {
    col = color(hue, 100, 100);
    speed = 0.2;// random(0.05,.5);
    pos = 0.0;
    strip = stripNum;
    haloMaxSize = haloSize;
    haloCurSize = 0.0;
    haloSpeed = random(0.5,1.5);
  }

  int getPos() {
    return int(pos); 
  }

  void move() {
     pos += speed;
     
     if (haloCurSize >= haloMaxSize) {
       haloCurSize = 0.0;
     }
     
     haloCurSize += haloSpeed;
  }

  int haloTrailPos() {
    return getPos() - int(haloCurSize);
  }
  
  int haloLeadPos() {
    return getPos() + int(haloCurSize);
  }

  void drawPixels(color[] ledVal, int len) {
    int max = int(2*haloCurSize+1);
    float brightAdj = 1 - (float)haloCurSize/haloMaxSize;
    color haloCol = color(hue(col), saturation(col), brightAdj*brightness(col));
    
    // Draw the fairy
    if (haloCurSize < 6.0) {
      if (int(random(3)) == 2) {
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

  boolean isGone(int len) {
    if (getPos() >= len) {
      return true; 
    } else {
      return false; 
    }
  }
}

