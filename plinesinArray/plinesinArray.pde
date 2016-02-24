Point [] points= new Point [100];
int interval = 10;
float noff=0;

void setup() {

size(800,800);
background(0);
smooth();
noff += 1;

for (int i=0; i < points.length; i+=interval){ 
		 points[i] = new Point(300*noff,250*noff);
		 	}
frameRate(30);//speed of rendering,how many frames happens in a second;
}

void draw(){
for (int i=0; i < points.length; i+=interval){ 	
    points[i].update(); 
    points[i].render();
 
}
}

class Point{

PVector locationA;
PVector locationB;
PVector pVelocity;//point velocity//

PVector angel1;
PVector angel2;

float aVelocity;//angle velocity//
float scaleValue;//scaling up angle change//
int iteration;//frameCount//


Point(float x, float y){
locationA = new PVector(x, y);
pVelocity = new PVector(-20,50);
locationB = locationA.sub(pVelocity);

angel1 = new PVector(3,6);
angel2 = new PVector(6,3);

aVelocity =0.01;
scaleValue = 0.0;
iteration = 500;
}

void update (){
scaleValue += aVelocity; //angle change is the celocity change over time;
}

void render(){
  if(frameRate < iteration){
float x1= sin(angel1.x*scaleValue+0.5)*locationA.x;
float y1 = cos(angel1.y*scaleValue)*locationA.y;
float x2 = sin(angel2.x*scaleValue+0.5)* locationB.x;
float y2 = cos(angel2.y*scaleValue)* locationB.y;
translate(width/8, height/8);
stroke(130);
strokeWeight(0.1);
line(x1,y1,x2,y2);
}
}
}
