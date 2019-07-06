/*!
* OpenCL kernel for computing the mandelbrot set.
*
* output:               A buffer with width * height elements,
*                       storing calculated interations count for each point.
* width, height:        width and height of the buffer.
* centerCx, centerCy:   center of rectangle in which the mandelbrot set will be computed.
* zoomX, zoomY:         zoom of rectangle in which the mandelbrot set will be computed.
* maxIterations:        maximum number of iterations.
*/
__kernel void computeMandelbrot(
    __global uint *output,
    int width, int height,
    double centerCx, double centerCy,
    double zoomX, double zoomY,
    int maxIterations
    )
{
    unsigned int ix = get_global_id(0);
    unsigned int iy = get_global_id(1);

    double cx = centerCx - zoomX / 2.0f + ((double)ix / (width - 1)) * zoomX;
    double cy = centerCy - zoomY / 2.0f + ((double)iy / (height - 1)) * zoomY;

    double magnitudeSquared = 0;
    double x = 0;
    double y = 0;
    int iteration = 0;

    double xt;
    double yt;

    double xSquared;
    double ySquared;

    while (magnitudeSquared <= 16 && iteration < maxIterations)
    {
        xSquared = x * x;
        ySquared = y * y;

        xt = xSquared - ySquared + cx;
        yt = 2 * x * y + cy;
        x = xt;
        y = yt;

        magnitudeSquared = xSquared + ySquared;
        ++iteration;
    }

    output[iy * width + ix] = iteration;
}
