# PPMTool
A Java library for reading and transforming PPM images.

Inspired by /r/dailyprogrammer Challenge #248, A Measure of Edginess: https://www.reddit.com/r/dailyprogrammer/comments/3zqiiq/20160106_challenge_248_intermediate_a_measure_of/

```
usage: com.ppm.cli.PPMTool
 -c,--max_color <arg>        Maximum color value used when outputting the
                             PPM. Min=0, Max=65536, Default=255
 -d,--display                Display output using Javafx
 -e,--edge_detection <arg>   Perform edge detection. Can optionally be
                             provded the algorithm to use. Supported
                             algorithms: [SOBEL]. Default algorithm: SOBEL
 -g,--greyscale              Transform the PPM image to greyscale.
 -h,--help                   Display usage information
 -i,--stdin                  Read PPM from stdin
 -if,--in_file <arg>         Read PPM from file
 -o,--stdout                 Write as PPM to stdout
 -of,--out_file <arg>        Write as PPM to file
```

# Transformations

## GreyScale

Converts the PPM image to greyscale using the following color factors:

* Red: 0.2126
* Green: 0.7152
* Blue: 0.0722

Example:
```java -jar PPM-standalone-cli-1.0.jar -if potatoes.ppm -g -d```

TODO

## Edge Detection

Performs edge detection on the image using the sobel operator.
>
The Sobel operator focuses on finding edges based on the "brightness" of the image, requiring each pixel in the image to have a "brightness" value. In other words, it requires a grayscale, not color image.
>
PPMTool will perform the edge detection for you using the sobel operator approach, but it is recommended you first apply the greyscale transformation to achieve the maximum results.

Example:
```java -jar PPM-standalone-cli-1.0.jar -if potatoes.ppm -g -e -d```

TODO
