int stepSize = 15;
int dir = 6;//or 4 for 90 degree
float x,y;
float prevX, prevY;
 
void setup() {
  size(512,512);
  background(200);
  stroke(255);
  fill(0,8);
  smooth();
  frameRate(15);
  x = width/2;//starts from the center
  prevX = x;
  y = height/2;//starts from the center
  prevY = y;
}
 
void draw() {
  strokeWeight(0.1);
  //rect(0,0,width,height);//fading (change bk color to black if fade)
  float theta = TWO_PI/dir*floor(random(dir));
  x += cos(theta)*stepSize;
  y += sin(theta)*stepSize;
  if (x < 0 || x > width) {
    x = prevX;
    y = prevY;
  }
  if (y < 0 || y > height) {
    x = prevX;
    y = prevY;
  }
  strokeWeight(5);
  stroke(0,70);
  point(x,y);
  strokeWeight(1);
  line(x,y,prevX,prevY);
  prevX = x;
  prevY = y;
  
  //saveGif();
}


void saveGif() {
  if (frameCount < 240) {
    saveFrame("gif/image-####.gif");
    println("saving image");
  }
}