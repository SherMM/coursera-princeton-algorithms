import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class SeamCarver {

private Picture seamPic;
private int w; // width of picture
private int h; // height of picture
private double[][] matrix; // enegry matrix

private static final double INFINITY = Double.POSITIVE_INFINITY;
private double[][] distances; // for storing shortest path distances

// create a seam carver object based on the given picture
public SeamCarver(Picture picture) {
        // don't mutate original picture
        seamPic = new Picture(picture);
        w = seamPic.width();
        h = seamPic.height();

        // initialize energy matrix
        matrix = new double[h][w];
        for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                        matrix[i][j] = 0.0;
                }
        }

        // setup distance matrix
        // will be initialized later
        distances = new double[h][w];
}

// current picture
public Picture picture() {
        return seamPic;
}

// width of current picture
public int width() {
        return this.w;
}

private void setWidth(int width) {
        this.w = width;
}

// height of current picture
public int height() {
        return this.h;
}

private void setHeight(int height) {
        this.h = height;
}

// energy of pixel at column x and row y
public double energy(int x, int y) {
        if (isOnBorder(x, y, this.height(), this.width())) {
                return 1000.0;
        }

        double xgrad = this.xGradient(x, y);
        double ygrad = this.yGradient(x, y);

        return Math.sqrt(xgrad + ygrad);
}

private double xGradient(int x, int y) {
        // get values at x+1, y pixel
        Color colorHi = seamPic.get(x+1, y);
        int rh = colorHi.getRed();
        int gh = colorHi.getGreen();
        int bh = colorHi.getBlue();

        // get values at x-1, y pixel
        Color colorLo = seamPic.get(x-1, y);
        int rl = colorLo.getRed();
        int gl = colorLo.getGreen();
        int bl = colorLo.getBlue();

        // get differences, squared
        double rsq = Math.pow((double) rh - rl, 2.0);
        double gsq = Math.pow((double) gh - gl, 2.0);
        double bsq = Math.pow((double) bh - bl, 2.0);

        double sqSum = rsq + gsq + bsq;

        return sqSum;
}

private double yGradient(int x, int y) {
        // get values at x+1, y pixel
        Color colorHi = seamPic.get(x, y+1);
        int rh = colorHi.getRed();
        int gh = colorHi.getGreen();
        int bh = colorHi.getBlue();

        // get values at x-1, y pixel
        Color colorLo = seamPic.get(x, y-1);
        int rl = colorLo.getRed();
        int gl = colorLo.getGreen();
        int bl = colorLo.getBlue();

        // get differences, squared
        double rsq = Math.pow((double) rh - rl, 2);
        double gsq = Math.pow((double) gh - gl, 2);
        double bsq = Math.pow((double) bh - bl, 2);

        double sqSum = rsq + gsq + bsq;

        return sqSum;
}

private static boolean isOnBorder(int x, int y, int height, int width) {
        if (x == 0 || y == 0 || x == width-1 || y == height-1) {
                return true;
        }
        return false;
}

/*
   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {

   }

 */
// sequence of indices for vertical seam
public int[] findVerticalSeam() {
        // initialize distances matrix
        for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                        // all first row pixels are set to 100 energy
                        if (i == 0) {
                                this.distances[i][j] = 1000.0;
                        } else {
                                this.distances[i][j] = INFINITY;
                        }
                }
        }

        // create energy matrix
        for (int row = 0; row < this.height(); row++) {
                for (int col = 0; col < this.width(); col++) {
                        this.matrix[row][col] = this.energy(col, row);
                }
        }

        // iterate through pixels (nodes) in picture
        for (int i = 0; i < this.height()-1; i++) {
                for (int j = 0; j < this.width(); j++) {
                        // relax adjacent edges of pixels
                        for (int k : adj(j, this.width())) {
                                double prevDist = this.distances[i][j];
                                double currDist = this.distances[i+1][k];
                                double currEnergy = this.matrix[i+1][k];
                                if (prevDist != INFINITY && (currDist > prevDist + currEnergy)) {
                                        this.distances[i+1][k] = prevDist + currEnergy;
                                }
                        }
                }
        }

        // debug distances
        /*
           for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                        // all first row pixels are set to 100 energy
                        StdOut.printf("%9.0f ", this.distances[i][j]);
                }
                StdOut.println();
           }
         */

        // setup array to store array, which stores column indexes
        int[] seam = new int[this.height()];

        // reconstruct shortest energy path from bottom row to top row
        // first find column of lowest energy path
        int minCol = findMinPathColumn(this.height()-1, this.distances[this.height()-1]);


        return seam;
}

private static int findMinPathColumn(int row, double[] lastRow) {
        int col = 0;
        double lowest = lastRow[col];
        for (int j = 1; j < lastRow.length; j++) {
                if (lastRow[j] < lowest) {
                        lowest = lastRow[j];
                        col = j;
                }
        }
        return col;
}

// returns adjacent pixels
private static int[] adj(int j, int width) {
        // returns list of column indexes (x-indexes are assumed)
        int[] adjPix;
        if (j == 0) {
                // if at left edge of picture, only 2 adjacent pixels
                adjPix = new int[2];
                adjPix[0] = j;
                adjPix[1] = j+1;
        } else if (j == width-1) {
                // if at right edge of picture, only 2 adjacent pixels
                adjPix = new int[2];
                adjPix[0] = j-1;
                adjPix[1] = j;
        } else {
                // all other pixels have three adjacent, lower pixel
                adjPix = new int[3];
                adjPix[0] = j-1;
                adjPix[1] = j;
                adjPix[2] = j+1;
        }
        return adjPix;
}


// remove horizontal seam from current picture
public void removeHorizontalSeam(int[] seam) {

}

// remove vertical seam from current picture
public void removeVerticalSeam(int[] seam) {

}

public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);
        sc.findVerticalSeam();
}
}
