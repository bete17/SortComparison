package cs106;

import static org.apache.commons.lang3.ArrayUtils.*;

public class BasicSorter implements Sorter {
    @Override
    public void insertionSort(String[] data, int fi, int n) {
        for (var i = fi + 1; i < fi + n; i++) {
            var trueIndx = i;//prev index
            var currentStr = data[i]; //string to swap
            while (trueIndx != fi && data[trueIndx - 1].compareTo(currentStr) > 0) {
                trueIndx--; //traverse through the sorted section
            }
            if (trueIndx != i) {
                // Shift elements one position to the right
                System.arraycopy(data, trueIndx, data, trueIndx + 1, i - trueIndx);
                // Place the currentStr in its correct position
                data[trueIndx] = currentStr;
            }
        }
    }

    @Override
    public void quickSort(String[] data, int fi, int n) {
        if (n <= 1) return;

        if (n <= 63) {
            insertionSort(data, fi, n);
        }
        int pivotIndx = partition(data, fi, n);
        int leftsum = pivotIndx - fi;
        int rightSum = n - leftsum - 1;
        quickSort(data, fi, leftsum);
        quickSort(data, pivotIndx + 1, rightSum);

    }

    @Override
    public int partition(String[] data, int fi, int n) {
        int leftIndx = fi;
        int rightIndx = leftIndx + n - 1;
        int center = (leftIndx + rightIndx) / 2;
        if (data[center].compareTo(data[leftIndx]) < 0) {//if left is bigger than mid
            swap(data, leftIndx, center);
        }
        if (data[leftIndx].compareTo(data[rightIndx]) > 0) {//if left is bigger than right
            swap(data, leftIndx, rightIndx);
        }
        if (data[center].compareTo(data[rightIndx]) > 0) {//if center is bigger than right
            swap(data, center, rightIndx);
        }

        String pivot = data[center];
        swap(data, center, rightIndx);

        int tbi = fi;
        int tsi = rightIndx;

        while (true) {
            while (tbi < tsi && data[tbi].compareTo(pivot) <= 0) {
                tbi++;
            }
            while (tsi > fi && data[tsi].compareTo(pivot) >= 0) {
                tsi--;
            }
            if (tbi >= tsi) {
                break;
            }
            swap(data, tbi, tsi);
        }
        swap(data, tbi, rightIndx);
        return tbi;

    }

    @Override
    public void mergeSort(String[] data, int fi, int n) {
        if (n <= 63) {
            insertionSort(data, fi, n);
            return;
        }
        int center = fi + n / 2;
        mergeSort(data, fi, center - fi);
        mergeSort(data, center, fi - center + n);
        merge(data, fi, center - fi, fi + n - center);
    }

    @Override
    public void merge(String[] data, int fi, int nl, int nr) {
        String[] temp = new String[nl + nr];
        int leftIndx = fi;
        int rightindx = fi + nl;
        var indx = 0;
        while (leftIndx < fi + nl && rightindx < fi + nl + nr) {
            if (data[rightindx].compareTo(data[leftIndx]) >= 0) {
                temp[indx++] = data[leftIndx++];
            } else {
                temp[indx++] = data[rightindx++];
            }
        }
        while (leftIndx < fi + nl) {
            temp[indx++] = data[leftIndx++];
        }
        while (rightindx < fi + nl + nr) {
            temp[indx++] = data[rightindx++];
        }
        System.arraycopy(temp, 0, data, fi, temp.length);
    }

    @Override
    public void heapSort(String[] data) {

    }

    @Override
    public void heapify(String[] data) {

    }
}
