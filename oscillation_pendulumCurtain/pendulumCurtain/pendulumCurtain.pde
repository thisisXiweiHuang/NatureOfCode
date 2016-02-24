Pendulum[] pendulums;//one array list named pendulums have a bunch of Pendulum;

void setup() {
  size(800, 1000);
  smooth();
  pendulums = new Pendulum[150];
  for (int i=0; i<pendulums.length; i++) {
    pendulums[i] = new Pendulum(new PVector (map(i,0,pendulums.length,10,width-10), 0), (i+50)*2, 1);
  }
}

void draw() {
  background(200);
  for (Pendulum pendulums_ : pendulums) {
    pendulums_.update();
    pendulums_.render();
    pendulums_.gravity();
 
  if (mousePressed){
    float wind = .002;
    pendulums_.applyForce(wind);
  }
}
}

class Pendulum {
  PVector origin;
  PVector bob;
  float bobMass;
  float radius;
  float angle;
  float aVelocity;
  float aAcceleration;
  float friction =0.99;
  float G=1;
  
  Pendulum(PVector origin_,float radius_, float bobMass_) {
    bobMass = bobMass_;
    radius = radius_;
    angle= PI/4;
    aVelocity=0.0;
    aAcceleration=0.0;
    origin = origin_;  
    bob = new PVector(origin.x, radius);
  }

  void update() {
    bob.x = origin.x+sin(angle)*radius;
    bob.y= origin.y+cos(angle)*radius;
    line(bob.x, bob.y, origin.x, origin.y);
    ellipse(bob.x, bob.y, bobMass, bobMass);
  }

  void render () {
    aVelocity += aAcceleration;
    aVelocity *= friction;
    angle += aVelocity;
    aAcceleration *=0;//always reture to o value each frame;
  }


  void gravity() {
    float gravity = (-1*G/bob.y)*sin(angle);
    applyForce(gravity);
  }

  void applyForce(float f) {
    aAcceleration += f;
  }
}