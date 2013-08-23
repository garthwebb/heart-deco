class Column {
  public color col;
 
  private float pos;
  private float speed;
 
  Column(color c) {
    col = color(c, 100, 100);
    pos = 0.0;
    speed = 0.75;
  }
  
  int getPos() {
    return int(pos); 
  }
  
  void move() {
    pos += speed;
  }

  boolean isGone(int len) {
    if (getPos() >= len) {
      return true; 
    } else {
      return false; 
    }
  }
}
