var snowFlakes = [];
var attractor;


function setup() {
  createCanvas(window.innerWidth, window.innerHeight);
  snowFlakes.length = 100;
  for (var i = 0; i < snowFlakes.length; i++) {
    snowFlakes[i] = new snowFlake();
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

  //snowFlake
  for (var i = 0; i < snowFlakes.length; i++) {
    // atrrating force from the attractor
    var attraction = attractor.attractionForce(snowFlakes[i]);
    //resistance(dragforce)
    var c = 0.03; //constant c
    var normal = 10; //speed*speed(made up this number);
    var resistanceMag = c * normal;
    var resistance = snowFlakes[i].velocity.copy();
    //change the direction;
    resistance.mult * -1;
    resistance.normalize();
    resistance.mult(resistanceMag);
    //attractor's animation

    //for snowFlake movement
    if (keyPressed){
      if (keyCode === 32){
        snowFlakes[i].applyWind();
      }
    }
    //snowFlakes[i].applyGravity();
    //snowFlakes[i].applyWind();
    //snowFlakes[i].applyForce(resistance);
    snowFlakes[i].applyForce(attraction);
    snowFlakes[i].update();
    snowFlakes[i].render();
    //snowFlakes[i].bounce();
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
    snowFlakes.push(new snowFlake());
  }
}