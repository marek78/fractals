![](src/main/resources/icon.png?raw=true)

fractals
========

java/swing project which renders Mandelbrot fractal <br>
for calculation boost uses OpenCL device if available <br>

Features:
 - scrolling and zooming in Mandelbrot fractal
 - choosing between various color themes
 - adjusting fractal calculation iteretions
 - saving fractal as .png file
 - OpenCL support

### Building:
#### with eclipse
<pre>
Import project to the eclipse:
File -> Import... -> Existing Projects into Workspace
Browse to the downloaded sources, click OK and finish.
</pre>

#### with maven
<pre>
mvn clean package
</pre>

### Running:
<pre>
java -jar target/fractals*
</pre>

### Some snapshots:
![](snapshots/fractal_20190706_223110.jpg?raw=true)
=
![](snapshots/fractal_20190706_223115.jpg?raw=true)
=
![](snapshots/fractal_20190706_223205.jpg?raw=true)
=
![](snapshots/fractal_20190706_223329.jpg?raw=true)
=
