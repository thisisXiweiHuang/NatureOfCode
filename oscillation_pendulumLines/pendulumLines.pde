void setup() {
size(800,1000);
background(0);
smooth();
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
float velocity = 0.01;//angle is the change of velocity over time;
int iteration = 1000;//iteration is the number of loops;

void draw() {

	translate(width/2, height/4);
	if(frameRate< iteration){

	float x1= sin(angelx1*c)*ampx1;
	float y1=cos(angely1*c)*ampy1;

	float x2 =sin(angelx2*c)*ampx2;
	float y2 = cos(angely2*c)*ampy2;

	c += velocity;//so angel changes over time depends on velocity adding up;

stroke(130);
strokeWeight(0.1);
line(x1,x2,y1,y2);

// noFill();
// strokeWeight(0.09);
// ellipse(x1,y2,5,5);
// ellipse(x2,y2,10,10);

}
}