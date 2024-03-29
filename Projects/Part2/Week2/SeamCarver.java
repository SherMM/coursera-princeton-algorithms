import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

private static final double INFINITY = Double.POSITIVE_INFINITY;
private int[][] picColors; // for storing RGB ints
private int w; // width of picture
private int h; // height of picture
private double[][] matrix; // enegry matrix

// create a seam carver object based on the given picture
public SeamCarver(Picture picture) {
        if (picture == null) {
                throw new NullPointerException("Picture can't be null");
        }
        // don't mutate original picture
        Picture seamPic = new Picture(picture);
        this.w = seamPic.width();
        this.h = seamPic.height();

        // initialize picColors matrix
        this.picColors = new int[h][w];
        for (int i = 0; i < this.height(); i++) {
                for (int j = 0; j < this.width(); j++) {
                        this.picColors[i][j] = seamPic.get(j, i).getRGB();
                }
        }

        // initialize energy matrix
        this.matrix = new double[h][w];
        for (int i = 0; i < this.height(); i++) {
                for (int j = 0; j < this.width(); j++) {
                        this.matrix[i][j] = 0.0;
                }
        }

        // create initial energy matrix
        for (int row = 0; row < this.height(); row++) {
                for (int col = 0; col < this.width(); col++) {
                        this.matrix[row][col] = this.energy(col, row);
                }
        }
}

// current picture
public Picture picture() {
        Picture pic = new Picture(this.width(), this.height());
        // add colors to picture
        for (int row = 0; row < this.height(); row++) {
                for (int col = 0; col < this.width(); col++) {
                        // color from color matrix
                        Color color = new Color(this.picColors[row][col]);
                        pic.set(col, row, color);
                }
        }

        return pic;
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
        if (!inValidRange(x, 0, this.width()-1) || !inValidRange(y, 0, this.height()-1)) {
                throw new IndexOutOfBoundsException("X or Y value out of range");
        }
        if (isOnBorder(x, y, this.height(), this.width())) {
                return 1000.0;
        }

        double xgrad = this.xGradient(x, y);
        double ygrad = this.yGradient(x, y);

        return Math.sqrt(xgrad + ygrad);
}

private static boolean inValidRange(int val, int low, int high) {
        return (val >= low) && (val <= high);
}

private double xGradient(int x, int y) {
        // get values at x+1, y pixel
        Color colorHi = new Color(picColors[y][x+1]);
        int rh = colorHi.getRed();
        int gh = colorHi.getGreen();
        int bh = colorHi.getBlue();

        // get values at x-1, y pixel
        Color colorLo = new Color(picColors[y][x-1]);
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
        Color colorHi = new Color(picColors[y+1][x]);
        int rh = colorHi.getRed();
        int gh = colorHi.getGreen();
        int bh = colorHi.getBlue();

        // get values at x-1, y pixel
        Color colorLo = new Color(picColors[y-1][x]);
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

// sequence of indices for horizontal seam
public int[] findHorizontalSeam() {
        this.transpose();
        int[] seam = this.findVerticalSeam();
        this.transpose();
        return seam;
}

private void transpose() {

        // initialize energy matrix
        double[][] tempe = new double[this.width()][this.height()];
        for (int i = 0; i < this.width(); i++) {
                for (int j = 0; j < this.height(); j++) {
                        tempe[i][j] = this.matrix[j][i];
                }
        }

        // initialize picColors temp matrix
        int[][] tempc = new int[this.width()][this.height()];
        for (int i = 0; i < this.width(); i++) {
                for (int j = 0; j < this.height(); j++) {
                        tempc[i][j] = this.picColors[j][i];
                }
        }

        // swap values for height and width
        int holder = this.height();
        this.setHeight(this.width());
        this.setWidth(holder);
        this.matrix = tempe;
        this.picColors = tempc;
}

// sequence of indices for vertical seam
public int[] findVerticalSeam() {
        // initialize distances matrix
        double[] distances = new double[this.height()*this.width()];
        for (int i = 0; i < this.height(); i++) {
                for (int j = 0; j < this.width(); j++) {
                        // all first row pixels are set to 100 energy
                        int index = convert2Dto1D(i, j, this.width());
                        if (i == 0) {
                                distances[index] = 1000.0;
                        } else {
                                distances[index] = INFINITY;
                        }
                }
        }

        // initialize parents matrix
        int[] parents = new int[this.height()*this.width()];
        for (int i = 0; i < this.height(); i++) {
                for (int j = 0; j < this.width(); j++) {
                        int index = convert2Dto1D(i, j, this.width());
                        // set all to column index, initially
                        parents[index] = j;
                }
        }

        // iterate through pixels (nodes) in picture
        for (int i = 0; i < this.height()-1; i++) {
                for (int j = 0; j < this.width(); j++) {
                        // relax adjacent edges of pixels
                        for (int k : adj(j, this.width())) {
                                // previous pixel index
                                int pindex = convert2Dto1D(i, j, this.width());
                                // current pixel index
                                int cindex = convert2Dto1D(i+1, k, this.width());

                                double prevDist = distances[pindex];
                                double currDist = distances[cindex];
                                double currEnergy = this.matrix[i+1][k];
                                if (prevDist != INFINITY && (currDist > prevDist + currEnergy)) {
                                        distances[cindex] = prevDist + currEnergy;
                                        parents[cindex] = j;
                                }
                        }
                }
        }

        // setup array to store array, which stores column indexes
        int[] seam = new int[this.height()];

        // first find column of lowest energy path
        int currCol = findMinPathColumn(this.height()-1, this.width(), distances);
        // reconstruct shortest energy path from bottom row to top row
        int currRow = this.height()-1;

        while (currRow >= 0) {
                seam[currRow] = currCol;
                int index = convert2Dto1D(currRow, currCol, this.width());
                currCol = parents[index];
                currRow -= 1;
        }

        return seam;
}

private static int convert2Dto1D(int i, int j, int cols) {
        return i * cols + j; // cols is number of columns
}

private static int findMinPathColumn(int row, int width, double[] dists) {
        int col = 0;
        int index = convert2Dto1D(row, col, width);
        double lowest = dists[index];
        for (int j = 1; j < width; j++) {
                int idx = convert2Dto1D(row, j, width);
                if (dists[idx] < lowest) {
                        lowest = dists[idx];
                        col = j;
                }
        }
        return col;
}

// returns adjacent pixels
private static int[] adj(int j, int width) {
        // returns list of column indexes (x-indexes are assumed)
        int[] adjPix;
        // when width is 1, only return lower adjacent pixel
        if (width == 1) {
                adjPix = new int[1];
                adjPix[0] = j;
        } else if (j == 0) {
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
        if (seam == null) {
                throw new NullPointerException("Seam can't be null");
        }

        if (this.height() <= 1) {
                throw new IllegalArgumentException("Picture not tall enough");
        }

        if (seam.length != this.width() || !isValidSeam(seam, 0, this.height()-1)) {
                throw new IllegalArgumentException("Invalid Seam");
        }

        this.transpose();
        this.removeVerticalSeam(seam);
        this.transpose();
}

// remove vertical seam from current picture
public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
                throw new NullPointerException("Seam can't be null");
        }

        if (this.width() <= 1) {
                throw new IllegalArgumentException("Picture not wide enough");
        }

        if (seam.length != this.height() || !isValidSeam(seam, 0, this.width()-1)) {
                throw new IllegalArgumentException("Invalid Seam");
        }


        // shift rows to overwrite removed index
        for (int row = 0; row < this.height(); row++) {
                // seam is array of column indexes for each row
                int seamCol = seam[row];
                // number of elements to shift
                int shift = this.width() - seamCol - 1;
                // shift in matrix of RGB int values
                System.arraycopy(this.picColors[row], seamCol+1, this.picColors[row], seamCol, shift);
                // shift in pixel energy value matrix
                System.arraycopy(this.matrix[row], seamCol+1, this.matrix[row], seamCol, shift);
        }

        // update width
        this.setWidth(this.width()-1);

        // recalculate energies around removed seam
        for (int row = 0; row < this.height(); row++) {
                int seamCol = seam[row];
                if (seamCol == 0) {
                        // removed pixel was on picture's left edge
                        this.matrix[row][seamCol] = this.energy(seamCol, row);
                } else if (seamCol == this.width()) {
                        // removed pixel was on picture's right edge
                        this.matrix[row][seamCol-1] = this.energy(seamCol-1, row);
                } else {
                        // removed pixel was somewere in middle of row of pixels
                        //handle left pixel
                        this.matrix[row][seamCol-1] = this.energy(seamCol-1, row);
                        this.matrix[row][seamCol] = this.energy(seamCol, row);
                }
        }
}

private static boolean isValidSeam(int[] seam, int low, int high) {
        // check that all values are in correct range
        for (int i = 0; i < seam.length; i++) {
                if (!inValidRange(seam[i], low, high)) {
                        return false;
                }
        }
        // return false if two consecture entries differ by more than one
        for (int i = 0; i <= seam.length-2; i++) {
                int val1 = seam[i];
                int val2 = seam[i+1];
                if (Math.abs(val1-val2) > 1) {
                        return false;
                }
        }
        return true;
}

public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        /*
           SeamCarver sc = new SeamCarver(picture);
           int[] seam = sc.findHorizontalSeam();
           StdOut.println(Arrays.toString(seam));
           sc.removeHorizontalSeam(seam);
         */

        SeamCarver sc = new SeamCarver(picture);
        int[] invalidSeam = { -1 };
        sc.removeHorizontalSeam(invalidSeam);
}
}
