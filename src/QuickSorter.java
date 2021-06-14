/***********************************
* AUTHOR:     Affan Khan
* CREATE DATE:    2021-06-09
* PURPOSE:    This Class extends from JPanel to create a JPanel that
    holds a list of randomly generated bars, and uses the quick Sort
    algorithm to sort through them.
***********************************/
package src;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class QuickSorter extends JPanel{
    //variables for bar graphics
    private final static int BAR_WIDTH = 30;
    private final static int BAR_HEIGHT = 400;
    private int[]list;
    //JPanel variable object to create/display and return
    private static JPanel mainPanel;

    //initializes the list of QuickSorter object
    private QuickSorter(int[] list){
        this.list = list;
    }
    //sets the list to this QuickSorter object's list
    private void setItems(int[] list){
        this.list = list;
        repaint();
    }
    //sorts the QuickSorter object
    private void sort(){
        new SortWorker(list).execute();
    }

    /*
    * Paint component that creates the graphical bars
    */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        //creates the bar for each element
        for(int i = 0; i < list.length; i++){
            int x = i * BAR_WIDTH;
            int y = getHeight() - list[i];

            //makes the color red
            g.setColor( Color.RED );
            g.fillRect(x, y, BAR_WIDTH, list[i]);
            g.setColor( Color.BLUE );
            g.drawString("" + list[i], x, y);
        }
    }

    /*
    * Returns the Dimensions of the rectangle bars used for QuickSort graph
    */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(list.length * BAR_WIDTH, BAR_HEIGHT + 20);
    }

    /*
    * Class that sorts the bars using the QuickSort algorithm
    */
    private class SortWorker extends SwingWorker<Void, int[]>{
        private int[] list;

        public SortWorker(int[] unsortedItems){
            list = Arrays.copyOf(unsortedItems, unsortedItems.length);
        }

        //QuickSort algorithm

        //simple swap function
        public void swap(int[]list, int i, int j){
            int temp = list[i];
            list[i] = list[j];
            list[j] = temp;

            //repaint(); to show graphics to user
            publish( Arrays.copyOf(list, list.length) );
            //sleep to slow down graphics
            try { Thread.sleep(100); } catch (Exception e) {}
        }

        /*
        * Function that takes last element as pivot
        * and places all smaller element values to the
        * left of pivot, and greater elements to the 
        * right of the pivot
        */
        public int partition(int[] list, int low, int high){
            //pivot
            int pivot = list[high];

            //index of smaller element, to the right of the pivot
            int i = (low - 1);

            for(int j = low; j <= (high - 1); j++){
                //if current element is smaller than the pivot
                if(list[j] < pivot){
                    i++;
                    swap(list, i, j);
                }
            }

            swap(list, i + 1, high);

            return (i + 1);
        }

        /*
        * Main QuickSort algorithm function
        */
        public void QuickSort(int[] list, int low, int high){
            if(low < high){
                //pi is partition index
                int pi = partition(list, low, high);

                //recursivly calls quicksort algorithm
                QuickSort(list, low, pi - 1);
                QuickSort(list, pi + 1, high);
            }
        }

        @Override
        protected Void doInBackground(){
            int n = list.length;

            QuickSort(list, 0, n - 1);

            Toolkit tk = Toolkit.getDefaultToolkit();
            tk.beep();
            return null;
        }

        @Override
        protected void process(List<int[]> processList){
            int[] list = processList.get(processList.size() - 1);
            setItems( list );
        }

        @Override
        protected void done() {}
    }

    //method that generates random numbers and fills an integer array
    private static int[]generateListNumbers(){
        //variables initialized
        int[] list = new int[20];

        /*creates random object and generates number from 0 to 
        BAR_HEIGHT for each element*/
        Random random = new Random();
        for(int i = 0; i < list.length; i++){
            list[i] = random.nextInt(QuickSorter.BAR_HEIGHT);
        }

        return list;
    }

    /*
    * method that will run the entire Quick Sorting algorithm.
    * From creating the objects and panels and buttons, to
    * calling all functions to sort, generate, order and return
    * a panel with a visual Quick Sorting algorithm.
    */
    public static JPanel runQuickSort(){
        //creates the object QuickSort, initializes the list, and fills it with randomly generated numbers
        QuickSorter quickSort = new QuickSorter(QuickSorter.generateListNumbers());

        //adds title to the top of the panel
        JLabel title = new JLabel("Quick Sort");
        
        //makes and adds two buttons, generate and sort
        JButton generate = new JButton("Generate Data");
        generate.addActionListener((e)->quickSort.setItems(QuickSorter.generateListNumbers()));
        JButton sort = new JButton("Sort Data");
        sort.addActionListener((e) -> quickSort.sort());

        //adds the buttons to the panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(generate);
        bottomPanel.add(sort);
        
        //creates the mainPanel for the quickSort graphics
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(quickSort, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(title, BorderLayout.NORTH);

        return mainPanel;
    }
}
