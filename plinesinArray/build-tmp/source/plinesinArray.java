import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class plinesinArray extends PApplet {

Point [] points= new Point [100];
int interval = 10;
float noff=0;

public void setup() {


background(0);

noff += 1;

for (int i=0; i < points.length; i+=interval){ 
		 points[i] = new Point(300*noff,250*noff);
		 	}
frameRate(30);//speed of rendering,how many frames happens in a second;
}

public void draw(){
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

aVelocity =0.01f;
scaleValue = 0.0f;
iteration = 500;
}

public void update (){
scaleValue += aVelocity; //angle change is the celocity change over time;
}

public void render(){
  if(frameRate < iteration){
float x1= sin(angel1.x*scaleValue+0.5f)*locationA.x;
float y1 = cos(angel1.y*scaleValue)*locationA.y;
float x2 = sin(angel2.x*scaleValue+0.5f)* locationB.x;
float y2 = cos(angel2.y*scaleValue)* locationB.y;
translate(width/8, height/8);
stroke(130);
strokeWeight(0.1f);
line(x1,y1,x2,y2);
}
}
}
  public void settings() { 
size(800,800); 
smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "plinesinArray" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
