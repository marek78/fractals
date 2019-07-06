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
    float centerCx, float centerCy,
    float zoomX, float zoomY,
    int maxIterations
    )
{
    unsigned int ix = get_global_id(0);
    unsigned int iy = get_global_id(1);

    float cx = centerCx - zoomX / 2.0f + ((float)ix / (width - 1)) * zoomX;
    float cy = centerCy - zoomY / 2.0f + ((float)iy / (height - 1)) * zoomY;

    float magnitudeSquared = 0;
    float x = 0;
    float y = 0;
    int iteration = 0;

    float xt;
    float yt;

    float xSquared;
    float ySquared;

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
