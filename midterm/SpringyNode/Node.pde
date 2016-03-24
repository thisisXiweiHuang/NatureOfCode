class Node extends VerletParticle {
  Vec3D pos;
  AttractionBehavior behavior;
  float t, ut;
  VerletSpring spring;
 
  Node (Vec3D loc) {
    super(loc);
    physics.addParticle(this);
    t= random(0, 200);
    ut = random(0, 0.01);//update
    //AttractionBehavior behavior = new AttractionBehavior(this, 20, -0.01);//repel
    //Vec3D pos = new Vec3D(width/2, height/2,-200);
    pos = new Vec3D (this.x, this.y, this.z);
  }

  void run(float d) {
    render();
    updateSpring(d);
  }

  void render() {
    fill(255);
    stroke(200);
    strokeWeight(.2);
    sphereDetail(8);
    pushMatrix();
    //point(this.x, this.y, this.z);
    translate(this.x, this.y, this.z);
    sphere(1);
    popMatrix();
  }

  void createSpring(Node birthPlace, float distBetween) {
    spring = new VerletSpring(this, birthPlace, distBetween, 0.002);
    physics.addSpring(spring);
  }

  void updateSpring(float d) {
  spring.setRestLength(map(noise(t), 0, 1, d-d/2, d+d/4));
  t += ut;
  }
}