class Fairy {
  public color col;   // Fairy color
  public int strip;   // Which strip is this fairy on

  private float pos;     // Where this fairy currently is
  private float speed;   // How fast this fairy is moving
  private int haloMaxSize; // Max radius of the halo
  private int haloCurSize; // Current size of the halo
  private int haloDir;  // Whether the halo is expanding (1) or contracting (-1)
  
  Fairy(int stripNum, int haloSize) {
    col = color(245, 100, 100);
    speed = random(0.05,.5);
    pos = 0.0;
    strip = stripNum;
    haloMaxSize = haloSize;
    haloCurSize = 0;
    haloDir = 1;
    
    println("-- NEW: stripNum="+stripNum+", haloSize="+haloSize+" speed="+speed);
  }

  int getPos() {
    return int(pos); 
  }

  void move() {
     pos += speed;
     
     if (haloCurSize >= haloMaxSize) {
       haloDir = -1; 
     } else if (haloCurSize <= 0) {
       haloDir = 1; 
     }
     
     haloCurSize += haloDir;
     
//     println("MOVE: pos="+pos+", haloCurSize="+haloCurSize);
  }
  
  int startPixel() {
     return getPos() - haloCurSize;
  }

  IntList getPixels() {
    int max = 2*haloCurSize+1;
    IntList halo = new IntList();
    float brightAdj = 1 - (float)haloCurSize/haloMaxSize;

    for (int x=0; x < max; x++) {
      if ((x == 0) || (x == max-1)) {
        halo.append( color(hue(col), saturation(col), brightAdj*brightness(col)) );
      } else if (x == int(max/2)) {
        halo.append(col);    
      } else {
        halo.append(color(0)); 
      }
    }
    
    return halo;
  }
  
  boolean isGone(int len) {
    if (getPos()-haloCurSize > len) {
      return true; 
    } else {
      return false; 
    }
  }
}

