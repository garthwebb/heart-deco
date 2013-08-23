class Mote {
  public color col; // The color of this mote
  public int speed; // How fast this mote is moving
  public int pos;   // Where this mote currently is
  public int life;  // How long this mote will last until its dead
  public int strip; // Which strip is this mote on
  
  Mote(int stripNum) {
    int hueRange = int(random(100));
    int hue;
    if (hueRange > 90) {
      hue = int(random(40, 60));
    } else if (hueRange > 80) {
      hue = int(random(20, 40));
    } else {
      hue = int(random(0, 20));
    }
    
    // Get a random color in the red/orange/yellow area with higher saturation and value numbers
    col = color(hue, int(random(90,100)), int(random(70,100)));
    speed = int(random(1,4));

    // The longest this should last is the number of steps needed to get to
    // the end of the strip    
    life = int(random(200/speed));

    pos = 0;
    strip = stripNum;
  }
  
  void move() {
     pos += speed;
     life--;

     // If we're out of life, turn black
     if (life <= 0) {
       life = 0;
       col = color(0); 
     }
  }
  
  boolean isDead() {
    return life == 0; 
  }
};
