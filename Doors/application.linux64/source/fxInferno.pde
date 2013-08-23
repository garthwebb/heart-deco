class Inferno {
  public float speed;
  public color col;

  private float pos;
  private int time;
  private float decay;
  
  Inferno(float sp, color c) {
    speed = sp;
    col = color(c, 100, 100);
    time = millis();
    decay = 0.25;
  }

  int getPos() {
    return int(pos); 
  }

  void move() {
    float t = (millis() - time)/1000.0;
    pos = (speed*t) - (.5*9.8*sq(t));
    
    // If acceleration starts winning out, decay the flame
    if ((speed-(9.8*t)) < 0) {
      float b = brightness(col);
      if (b <= 0) {
        b = 0;
      } else {
        col = color(hue(col), saturation(col), b - decay); 
      }
    }
  }

  boolean isGone(int len) {
    if ((getPos() < 0) || (getPos() >= len)) {
      return true; 
    } else {
      return false; 
    }
  }
}
