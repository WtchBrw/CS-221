package GridMonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

public class GridMonitor implements GridMonitorInterface {
    private double[][] baseGrid;
    private double[][] sumGrid;
    private double[][] averageGrid;
    private double[][] deltaGrid;
    private boolean[][] dangerGrid;

    /**
     * @author
     * 
     * @param fname is the file that contains grid values
     * @throws FileNotFoundException thrown if the file does not exist
     */
    @SuppressWarnings("resource")
	public GridMonitor(String fname) throws FileNotFoundException {
        if (fname == null || fname.isEmpty()) {
        	throw new FileNotFoundException();
        } else {
        	int dimX = -1;
        	int dimY = -1;
	        File inputFile = new File(Paths.get("").toAbsolutePath().toString(), fname);
	        Scanner inputScanner = new Scanner(inputFile);
	        
	        while (inputScanner.hasNext()) {
	            String next = inputScanner.next();
	            if (dimY == -1) {
	                try {
	                    dimY = Integer.parseInt(next);
	                } catch (NumberFormatException err) {
	                    err.printStackTrace();
	                    return;
	                }
	            } else if (dimX == -1) {
	                try {
	                    dimX = Integer.parseInt(next);
	                    break;
	                } catch (NumberFormatException err) {
	                    err.printStackTrace();
	                    return;
	                }
	            }
	        }
	
	        baseGrid = new double[dimY][dimX];
	        sumGrid = new double[dimY][dimX];
	        averageGrid = new double[dimY][dimX];
	        deltaGrid = new double[dimY][dimX];
	        dangerGrid = new boolean[dimY][dimX];
	
	        yloop: for (int y = 0; y < dimY; y++) {
	            for (int x = 0; x < dimX; x++) {
	                if (!inputScanner.hasNext()) break yloop;
	                try {
	                    baseGrid[y][x] = Double.parseDouble(inputScanner.next());
	                } catch (NumberFormatException err) {
	                    err.printStackTrace();
	                    return;
	                }
	            }
	        }
	
	        inputScanner.close();
	        for (int y = 0; y < dimY; y++) {
	            for (int x = 0; x < dimX; x++) {
	                double above, below, left, right, sum;
	                
	                if (y == 0) above = baseGrid[y][x];
	                else above = baseGrid[y-1][x];
	                if (y == dimY-1) below = baseGrid[y][x];
	                else below = baseGrid[y+1][x];
	                
	                if (x == 0) left = baseGrid[y][x];
	                else left = baseGrid[y][x-1];
	                if (x == dimX-1) right = baseGrid[y][x];
	                else right = baseGrid[y][x+1];
	
	                sum = above + below + left + right;
	                sumGrid[y][x] = sum;
	                averageGrid[y][x] = sum/4;
	                deltaGrid[y][x] = Math.abs(sum/8);
	                dangerGrid[y][x] =
	                        baseGrid[y][x] < averageGrid[y][x] - deltaGrid[y][x] ||
	                                baseGrid[y][x] > averageGrid[y][x] + deltaGrid[y][x];
	            }
	        }
        }
    }

    @Override
    public double[][] getBaseGrid() {
        //return a copy of baseGrid
        return copy2DArray(baseGrid);
    }

    @Override
    public double[][] getSurroundingSumGrid() {
        //return a copy of sumGrid
        return copy2DArray(sumGrid);
    }

    @Override
    public double[][] getSurroundingAvgGrid() {
        //return a copy of averageGrid
        return copy2DArray(averageGrid);
    }

    @Override
    public double[][] getDeltaGrid() {
        //return a copy of deltaGrid
        return copy2DArray(deltaGrid);
    }

    @Override
    public boolean[][] getDangerGrid() {
        //return a copy of dangerGrid
        return copy2DArray(dangerGrid);
    }

    /**
     * create a backup of the array
     */
    private double[][] copy2DArray(double[][] toCopy) {
        double[][] toReturn = new double[toCopy.length][toCopy[0].length];

        for (int y = 0; y < toCopy.length; y++) {
            System.arraycopy(toCopy[y], 0, toReturn[y], 0, toCopy[y].length);
        }

        return toReturn;
    }

    /**
     * creates a copy of the array
     */
    private boolean[][] copy2DArray(boolean[][] toCopy) {
        boolean[][] toReturn = new boolean[toCopy.length][toCopy[0].length];

        for (int y = 0; y < toCopy.length; y++) {
            System.arraycopy(toCopy[y], 0, toReturn[y], 0, toCopy[y].length);
        }

        return toReturn;
    }

    /**
     * returns a string representation of the grid
     */
    @SuppressWarnings("unused")
	@Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        return getBaseGrid().toString();
    }
}
