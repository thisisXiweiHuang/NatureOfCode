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

public class pendulumLines extends PApplet {

public void setup() {

background(0);

frameRate(100);//new to this, but rememeber to have this frameRate(); how many frames in a second;
}


float ampx1= 200;
float ampx2= 100;

float angelx1=5;
float angelx2=2;

float ampy1= 150;
float ampy2=180;

float angely1 =2;
float angely2=5;

float c=0;
float velocity = 0.01f;//angle is the change of velocity over time;
int iteration = 1000;//iteration is the number of loops;

public void draw() {

	translate(width/2, height/4);
	if(frameRate< iteration){

	float x1= sin(angelx1*c)*ampx1;
	float y1=cos(angely1*c)*ampy1;

	float x2 =sin(angelx2*c)*ampx2;
	float y2 = cos(angely2*c)*ampy2;

	c += velocity;//so angel changes over time depends on velocity adding up;

stroke(130);
strokeWeight(0.1f);
line(x1,x2,y1,y2);

// noFill();
// strokeWeight(0.09);
// ellipse(x1,y2,5,5);
// ellipse(x2,y2,10,10);

}
}
  public void settings() { 
size(800,1000); 
smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "pendulumLines" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
