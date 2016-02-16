 var sun;
 var earth;

 var moon = {
   position: 0,
   velocity: 0,
   acceleration: 0
 }

 var e;

 function setup() {
   createCanvas(800, 600);
   e = new earth();
 }

 function draw() {
   e.orbit();
   e.moonorbit();
   e.display();
 }

 function earth() {
   this.position = createVector(320, 200);
   this.velocity = createVector();
   this.acceleration = createVector();
   this.orbit = function() {
     sun = createVector(mouseX, mouseY);
     this.acceleration = p5.Vector.sub(sun, this.position);
     this.acceleration.setMag(noise(6));
     this.velocity.add(this.acceleration);
     this.velocity.limit(10);
     this.position.add(this.velocity);
   };

   this.moonorbit = function() {
     moon.position = createVector(320, 200);
     moon.velocity = createVector();
     moon.acceleration = createVector();
     moon.velocity = p5.Vector.sub(moon.position, this.position);
     moon.acceleration.setMag(noise(25));
     moon.velocity.add(moon.acceleration * -1);
     //moonvelocity.limit(30);
     moon.position.add(moon.velocity);
   };

   this.display = function() {
     background(255);
     //earth
     fill(150);
     ellipse(this.position.x, this.position.y, 20, 20);
     //sun
     fill(255, 150, 0);
     ellipse(sun.x, sun.y, 50, 50);
     //moon
     fill(0);
     ellipse(moon.position.x, moon.position.y, 10, 10)
     line(this.position.x, this.position.y, sun.x, sun.y);
     line(this.position.x, this.position.y, moon.position.x, moon.position.y);
   };
 }