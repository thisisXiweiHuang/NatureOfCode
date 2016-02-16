var Attractor = function(mass) {
  this.position = createVector(width- width /3 , height - height / 3);
  this.mass = 40;
  this.G = 5;
  this.velocity = createVector(0, 0);
  this.acceleration = createVector(0, 0);
  this.dragOffset = createVector(0, 0);
  this.dragging = false;
  this.rollover = false;
  //for noise walking
  this.nOff=createVector(random(width),random(height));

  //create a force to animate attractor's movement;
  this.applysomeForce = function() {
    this.xOff = this.xOff+ .1;
    var n = noise(this.xoff) * width;
    var someForce = createVector(n,height);
    this.acceleration.add(p5.Vector.div(someForce, this.mass));
  };

  this.attractionForce = function(s) {
    //the direction of attraction
    var force = p5.Vector.sub(this.position, s.position);
    var distance = force.mag();
    distance = constrain(distance, 30, 50);
    force.normalize();
    var strength = 12 * (this.G * this.mass * s.mass) / (distance * distance);
    force.mult(strength);
    return force;
  };

  this.update = function() {
    this.position.x = map(noise(this.nOff.x),0,1,0,width);
    this.position.y=  map(noise(this.nOff.y),0,1,0,height);
    this.nOff.add(0.005,0.005,0);
  };

  this.display = function() {
    noStroke();
    fill(255,0,0,90);
    ellipse(this.position.x, this.position.y, this.mass / 4, this.mass / 4);
  };

  this.bounce = function() {
    if (this.position.y > height || this.position.y < 0) {
      this.velocity.mult(-1);
    }
    if (this.position.x > width || this.position.x < 0) {
      this.velocity.mult(-1);
    }
  }

  this.handleHover = function(mx, my) {
    var d = dist(mx, my, this.position.x, this.position.y);
    if (d < this.mass) {
      this.rollover = true;
      cursor(HAND);
    } else {
      this.rollover = false;
    }
  };

  this.handlePress = function(mx, my) {
    var d = dist(mx, my, this.position.x, this.position.y);
    if (d < this.mass) {
      this.dragging = true;
      this.dragOffset.x = this.position.x + mx;
      this.dragOffset.y = this.position.y + my;
    }
  };

  this.handleDrag = function(mx, my) {
    if (this.dragging) {
      this.position.x = mx;
      this.position.y = my;
    }
  };

  this.stopDragging = function(mx, my) {
    this.dragging = false;
  };
};
