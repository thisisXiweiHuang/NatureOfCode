var boids = [];
var attractor;

function setup() {
  createCanvas(window.innerWidth, window.innerHeight);
  boids.length = 800;
  for (var i = 0; i < boids.length; i++) {
    boids[i] = new Boid();
  }
  attractor = new Attractor();
}

function draw() {
  background(0,180);

  //attractor
  attractor.display();
  attractor.applysomeForce();
  attractor.update();
  attractor.bounce();

  //Boid
  for (var i = 0; i < boids.length; i++) {
    for (var j = 0; j < boids.length; j++) {
       if (i !== j) {
         var multiAttraction = boids[j].multiAttraction(boids[i]);
        boids[i].applyForce(multiAttraction);
     }
     }
    
    
    //multi attraction for snowFalkes;
    //var multiAttraction = boids[i].multiAttraction(boids[j]);
    //     boids[i].applyForce(multiAttraction);
    
    
    // atrrating force from the attractor
    var attraction = attractor.attractionForce(boids[i]);
    //resistance(dragforce)
    var c = 0.03; //constant c
    var normal = 10; //speed*speed(made up this number);
    var resistanceMag = c * normal;
    var resistance = boids[i].velocity.copy();
    //change the direction;
    resistance.mult * -1;
    resistance.normalize();
    resistance.mult(resistanceMag);
    //attractor's animation

    //for snowFlake movement
    //boids[i].applyGravity();
    //boids[i].applyWind();
    //boids[i].applyForce(resistance);
   boids[i].applyForce(attraction);
    boids[i].update();
    boids[i].render();
    //boids[i].bounce();

  }
}

function mouseMoved() {
  attractor.handleHover(mouseX, mouseY);
}

function mousePressed() {
  attractor.handlePress(mouseX, mouseY);
}

function mouseDragged() {
  attractor.handleHover(mouseX, mouseY);
  attractor.handleDrag(mouseX, mouseY);
}

function mouseReleased() {
  attractor.stopDragging();
}

function keyPressed() {
  if (keyCode == 32) {
    boids.push(new Boid());
  }
}
