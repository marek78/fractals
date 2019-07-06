fractals
========
<pre>
java/swing project which renders Mandelbrot fractal
for calculation boost uses OpenCL device if available
</pre>

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
