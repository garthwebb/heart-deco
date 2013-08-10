// Test user input controls
// 

import com.heroicrobot.dropbit.registry.*;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import java.util.*;
import processing.core.*;

DeviceRegistry registry;

int canvasWidth = 960;
int canvasHeight = 400;

void setup() {
  size(canvasWidth, canvasHeight);
  registry = new DeviceRegistry();
  background(0);
  colorMode(HSB, 100);

  registry.setExtraDelay(0);
  registry.startPushing();
}

void draw() {
  drawScreen();
  scrapePixels();
}

void drawScreen() {
  clear();
  
  noStroke();
  fill(color(mouseY/4, 100, 100));
  
  // Get the position of the mouse quantized to 4 pixel blocks
  int posX = mouseX/4;
  
  // Create a bar under the mouse
  rect(posX*4, 0, 4, canvasHeight);
}

void clear() {
  fill(0, 0, 0);
  rect(0, 0, width, height);
}

void scrapePixels() {
  List<Strip> allStrips = registry.getStrips();

  for (Strip strip : allStrips) {
    for (int screenX = 0; screenX < canvasWidth; screenX += 4) {
      color c = get(screenX, 0);
      strip.setPixel(c, screenX/4);
    }
  }
}
