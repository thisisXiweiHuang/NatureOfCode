var Boid = function(mass) {
  this.position = createVector(random(-10, width), random(-10, 10));
  this.mass = random(1, 2);
  this.velocity = createVector(0, 0);
  this.acceleration = createVector(0, 0);
  this.radius = this.mass * 2;
  this.G=1;

  this.applyGravity = function() {
    var gravity = createVector(0, 0.01 * this.mass);
    this.acceleration.add(p5.Vector.div(gravity, this.mass));
  };

  this.applyWind = function() {
    var wind = createVector(random(-0.3, 0.3), random(-0.2, 0.2));
    this.acceleration.add(p5.Vector.div(wind, this.mass));
  };

  this.applyForce = function(force) {
    this.acceleration.add(p5.Vector.div(force, this.mass));
  };

  this.update = function() {
    this.position.add(this.velocity);
    this.velocity.add(this.acceleration);
    this.acceleration.mult(0);
  };

  this.bounce = function() {
    if (this.position.y > height) {
      this.position.y = height;
      this.velocity.mult(-1)
        // }else if(this.position.y < 0) {
        // this.position.y =height;
        // this.velocity.mult(-0.8);
    }
    // if (this.position.x > width){
    //   this.position.x = width;
    //   this.velocity.mult(-0.8);
    // }else if(this.position.x < 0) {
    //   this.position.y =width;
    //   this.velocity.mult(-0.8);
    // }
  };

  //for multi attraction

  this.multiAttraction = function(b) {
    var multiAttraction = p5.Vector.sub(this.position, b.position);
    var distance = multiAttraction.mag();
    distance = constrain(distance, 5, 25);
    multiAttraction.normalize();
    var strengh = (this.G * this.mass * b.mass) / (distance * distance);
    multiAttraction.mult(strengh);
    return multiAttraction;
  };

  this.render = function() {
    noStroke();
    fill(255);
    ellipse(this.position.x, this.position.y, this.radius, this.radius);
  };
}
