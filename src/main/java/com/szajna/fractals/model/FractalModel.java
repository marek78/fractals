package com.szajna.fractals.model;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_MEM_WRITE_ONLY;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clCreateCommandQueueWithProperties;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetDeviceInfo;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clSetKernelArg;

import java.io.IOException;
import java.io.InputStream;

import org.jocl.CL;
import org.jocl.CLException;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import com.szajna.util.Filesystem;
import com.szajna.util.Log;


public class FractalModel {
    
    private static final String LOG_TAG = FractalModel.class.getSimpleName();

    public static final int ITERATIONS_COUNT_MIN = 100;
    public static final int ITERATIONS_COUNT_MAX = 1200;
    public static final int ITERATIONS_COUNT_DEFAULT = 300;
    
    private final static int THREADS_COUNT = 8;
    private final static double MAX_BETRAG_QUADRAT = 16.0;
    
    private int[][] fractalData;
    private int[] fractalData2;

    private int width = 0;
    private int height = 0;
    private int iterationsCount;

    private double centerCx;
    private double centerCy;
    private double zoom;
    
    private boolean openClSupported;
    private boolean openClDoubleSupport;
    
    private boolean openClEnabled;


    /** 
     * The OpenCL context
     */
    private cl_context context;

    /**
     * The OpenCL command queue
     */
    private cl_command_queue commandQueue;

    /**
     * The OpenCL kernel which will actually compute the Mandelbrot
     * set and store the pixel data in a CL memory object
     */
    private cl_kernel kernel;

    /**
     * The OpenCL memory object which stores the pixel data
     */
    private cl_mem pixelMem;
    
    /**
     * Constructs fractal model object.
     * @param width - fractal width.
     * @param height - fractal height.
     */
    public FractalModel() {

        setToDefaults();
        setSize(0, 0);
        this.iterationsCount = ITERATIONS_COUNT_DEFAULT;

        try {
            this.openClSupported = openClTryInit();
            this.openClEnabled = this.openClSupported;  // enable if supported

        } catch (UnsatisfiedLinkError e) {

            Log.e(LOG_TAG, "Could not load native OpenCL library");
            Log.i(LOG_TAG, "To install it on Ubuntu try:");
            Log.i(LOG_TAG, " sudo apt update");
            Log.i(LOG_TAG, " sudo apt install ocl-icd-opencl-dev");
            this.openClSupported = false;
            this.openClEnabled = false;
        }
    }
    
    /**
     * Creates fractal taking parameters from another model.
     * @param width - fractal width in pixels.
     * @param height - fractal height in pixels.
     * @param other - other model, must be != null.
     */
    public FractalModel(int width, int height, FractalModel other) {
        
        this.width = other.width;
        this.height= other.height;
        this.iterationsCount = other.iterationsCount;
        this.centerCx = other.centerCx;
        this.centerCy = other.centerCy;
        this.zoom = other.zoom;
        this.openClSupported = false; 
        this.openClDoubleSupport = false;
        this.openClEnabled = false;
        setSize(width, height);
    }
    
    /**
     * Sets zoomX, zoomY, minCx, minCy and iterationsCount to default values.
     */
    public void setToDefaults() {
        
        centerCx = -0.50f;
        centerCy =  0.00f;
        zoom = 4.0f;
    }
    
    /**
     * Initialize OpenCL: Create the context, the command queue and the kernel.
     * \return true if initialized with success, otherwise return false.
     */
    private boolean openClTryInit() {
        
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // enable exceptions
        CL.setExceptionsEnabled(true);

        try {
            // Obtain the number of platforms
            int numPlatformsArray[] = new int[1];
            clGetPlatformIDs(0, null, numPlatformsArray);
            
            int numPlatforms = numPlatformsArray[0];
            if (numPlatforms < 1) {
                Log.d(LOG_TAG, "OpenCL - no platforms");
                return false;
            }

            // Obtain a platform ID
            cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
            clGetPlatformIDs(platforms.length, platforms, null);
            cl_platform_id platform = platforms[platformIndex];

            // Initialize the context properties
            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
            
            // Obtain the number of devices for the platform
            int numDevicesArray[] = new int[1];
            clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);

            int numDevices = numDevicesArray[0];
            if (numDevices < 1) {
                Log.d(LOG_TAG, "OpenCL - no devices");
                return false;
            }
            
            // Obtain a device ID 
            cl_device_id devices[] = new cl_device_id[numDevices];
            clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
            cl_device_id device = devices[deviceIndex];
           
            // check double support
            openClDoubleSupport = openClCheckDoubleSupport(device);
            Log.d(LOG_TAG, "OpenCL device support for double: " + (openClDoubleSupport ? "yes" : "no"));

            // Create a context for the selected device
            context = clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
            
            // Create a command-queue for the selected device
            commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);

            // Program Setup
            String programResource;
            if (openClDoubleSupport) {
                programResource = "kernels/mandelbrot_double.cl";
            } else {
                programResource = "kernels/mandelbrot.cl";
            }
            
            InputStream sourceIs = getClass().getClassLoader().getResourceAsStream(programResource);
            if (sourceIs == null) {

                Log.d(LOG_TAG, "OpenCL error loading program: " + programResource);
                return false;
            }
            
            try {
                
                String source = Filesystem.readFile(sourceIs);

                // Create the program
                cl_program cpProgram = clCreateProgramWithSource(context, 1, new String[]{ source }, null, null);

                // Build the program
                clBuildProgram(cpProgram, 0, null, "-cl-mad-enable", null, null);

                // Create the kernel
                kernel = clCreateKernel(cpProgram, "computeMandelbrot", null);

            } catch (IOException e) {
                
                Log.d(LOG_TAG, "OpenCL error loading program: " + programResource + ", error: " + e.getMessage());
                return false;
            }
            
        } catch (CLException e) {
            Log.d(LOG_TAG, "OpenCL error: " + e.getMessage() + ", OpenCL status: " + e.getStatus());
            return false;
            
        } catch (Exception e) {
            Log.d(LOG_TAG, "OpenCL error: " + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if device supports double.
     * @param device - openCL device
     * @return true if double supported on that device, otherwise false.
     */
    private boolean openClCheckDoubleSupport(cl_device_id device) {
        
        boolean doubleSupported;
        int val[] = new int[1];
        
        clGetDeviceInfo(device, CL.CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE, Sizeof.cl_uint, Pointer.to(val), null);
        int preferredVectorWidthDouble = val[0];
        
        clGetDeviceInfo(device, CL.CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE, Sizeof.cl_uint, Pointer.to(val), null);
        int nativeVectorWidthDouble = val[0];
        
        if (preferredVectorWidthDouble == 0 || nativeVectorWidthDouble == 0) {
            doubleSupported = false;
        } else {
            doubleSupported = true;
        }

        return doubleSupported;
    }
    
    /**
     * Calculates fractal using OpenCL.
     * Execute the kernel function and read the resulting data.
     */
    private void openClCalculate()
    {
        // Set work size and execute the kernel
        long globalWorkSize[] = new long[2];
        globalWorkSize[0] = width;
        globalWorkSize[1] = height;

        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(pixelMem));
        clSetKernelArg(kernel, 1, Sizeof.cl_uint, Pointer.to(new int[]{width}));
        clSetKernelArg(kernel, 2, Sizeof.cl_uint, Pointer.to(new int[]{height}));
        
        if (openClDoubleSupport) {
            clSetKernelArg(kernel, 3, Sizeof.cl_double, Pointer.to(new double[]{ centerCx }));
            clSetKernelArg(kernel, 4, Sizeof.cl_double, Pointer.to(new double[]{ centerCy }));
            clSetKernelArg(kernel, 5, Sizeof.cl_double, Pointer.to(new double[]{ getZoomX() }));
            clSetKernelArg(kernel, 6, Sizeof.cl_double, Pointer.to(new double[]{ getZoomY() }));
        } else {
            clSetKernelArg(kernel, 3, Sizeof.cl_float, Pointer.to(new float[]{ (float)centerCx }));
            clSetKernelArg(kernel, 4, Sizeof.cl_float, Pointer.to(new float[]{ (float)centerCy }));
            clSetKernelArg(kernel, 5, Sizeof.cl_float, Pointer.to(new float[]{ (float)getZoomX() }));
            clSetKernelArg(kernel, 6, Sizeof.cl_float, Pointer.to(new float[]{ (float)getZoomY() }));
        }
        clSetKernelArg(kernel, 7, Sizeof.cl_int, Pointer.to(new int[]{ iterationsCount }));
        
        clEnqueueNDRangeKernel(commandQueue, kernel, 2, null, globalWorkSize, null, 0, null, null);
        clEnqueueReadBuffer(commandQueue, pixelMem, CL_TRUE, 0, 
            Sizeof.cl_int * width * height, Pointer.to(fractalData2), 0, null, null);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fractalData[x][y] = fractalData2[y * width + x];
            }
        }
    }
    
    /**
     * Calculates fractal.
     */
    public void calculate() {

        long startTimestamp = System.currentTimeMillis();
        boolean useOpenCl = (openClSupported && openClEnabled);

        if (useOpenCl) {
            openClCalculate();
        } else {
            // single thread implementation
            // calculate(0, width);

            // multi-thread calculation
            Thread [] threads = new Thread[THREADS_COUNT];
            for (int i = 0; i < threads.length; ++i) {
                
                int startX = i * width / THREADS_COUNT;
                int endX = (i + 1) * width / THREADS_COUNT;
                
                threads[i] = new Thread(new FractalWorker(this, startX, endX));
                threads[i].start();
            }

            try {
                for (int i = 0; i < threads.length; ++i)
                    threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long calcTime = System.currentTimeMillis() - startTimestamp;
        Log.v(LOG_TAG, "OpenCL: " + useOpenCl + ", calc.time: " + calcTime);
    }
    
    /**
     * Calculates fractal - useful for dividing job on more threads.
     * @param startX
     * @param endX
     */
    public void calculate(int startX, int endX) { 

        double cx;
        double cy;

        double zoomX = getZoomX();
        double zoomY = getZoomY();
        
        for (int x = startX; x < endX; ++x) {
            
            cx = centerCx - zoomX / 2.0 + ((double)x / (width - 1)) * zoomX;
            for (int y = 0; y < height; ++y) {
                
                cy = centerCy - zoomY / 2.0 + ((double)y / (height - 1)) * zoomY;
                fractalData[x][y] = pointIteration(cx, cy, MAX_BETRAG_QUADRAT, iterationsCount);
            }
        }

    }

    /**
     * Gets fractal data.
     * @return fractal data.
     */
    public int[][] getFractalData() {
        return fractalData;
    }

    public void setSize(int width, int height) {
        
        if (width < 1)
            width = 0;
        if (height < 1)
            height = 0;
        
        fractalData = new int[width][height];
        fractalData2 = new int[width * height];
        
        if (openClSupported) {
            // create the memory object which will be filled with the pixel data
            pixelMem = clCreateBuffer(context, CL_MEM_WRITE_ONLY, width * height * Sizeof.cl_uint, null, null);
        } else {
            pixelMem = null;
        }
        
        this.width = width;
        this.height = height;
    }

    public void setIterationsCount(int iterationsCount) {
        this.iterationsCount = iterationsCount;
    }
    public void setCenterCx(double centerCx) {
        this.centerCx = centerCx;
    }
    public void setCenterCy(double centerCy) {
        this.centerCy = centerCy;
    }
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getIterationsCount() {
        return iterationsCount;
    }
    public double getCenterCx() {
        return centerCx;
    }
    public double getCenterCy() {
        return centerCy;
    }
    public double getZoom() {
        return zoom;
    }
    public double getZoomX() {
        
        double aspect = width / (double)height;
        return aspect > 1.0 ? zoom * aspect : zoom;
    }
    public double getZoomY() {

        double aspect = width / (double)height;
        return aspect > 1.0 ? zoom : zoom * 1.0 / aspect;
    }
    
    public boolean isOpenClSupported() {
        return openClSupported;
    }
    
    public boolean isOpenClEnabled() {
        return openClEnabled;
    }
    
    public void setOpenClEnabled(boolean openClEnabled) {
        this.openClEnabled = openClEnabled;
    }

    private int pointIteration(double cx, double cy, double maxBetragQuadrat, int maxIterations) {
        
        double betragQuadrat = 0;
        double x = 0;
        double y = 0;
        int iter = 0;
        
        double xt;
        double yt;
        
        double xQuadrat;
        double yQuadrat;
        
        while (betragQuadrat <= maxBetragQuadrat && iter < maxIterations) {
            
            xQuadrat = x * x;
            yQuadrat = y * y;
            
            xt = xQuadrat - yQuadrat + cx;
            yt = 2 * x * y + cy;
            x = xt;
            y = yt;
            
            betragQuadrat = xQuadrat + yQuadrat;
            ++iter;
        }
        //return iter - (float)(Math.log(Math.log(betragQuadrat) / Math.log(4)) / Math.log(2));
        return iter;
    }
}

