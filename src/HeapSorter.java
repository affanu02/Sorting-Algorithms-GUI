/***********************************
* AUTHOR:     Affan Khan
* CREATE DATE:    2021-06-09
* PURPOSE:    This Class extends from JPanel to create a JPanel that
    holds a list of randomly generated bars, and uses the heap Sort
    algorithm to sort through them.
***********************************/
package src;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.*;


public class HeapSorter extends JPanel {
    //variables for bar graphics
    private final static int BAR_WIDTH = 30;
    private final static int BAR_HEIGHT = 400;
    private int[]list;
    //JPanel variable object to create/display and return
    private static JPanel mainPanel;

    //initializes the list of HeapSorter object
    private HeapSorter(int[] list){
        this.list = list;
    }
    //sets the list to this HeapSorter object's list
    private void setItems(int[] list){
        this.list = list;
        repaint();
    }
    //sorts the HeapSorter object
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
    * Returns the Dimensions of the rectangle bars used for HeapSort graph
    */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(list.length * BAR_WIDTH, BAR_HEIGHT + 20);
    }

    /*
    * Class that sorts the bars using the HeapSort algorithm
    */
    private class SortWorker extends SwingWorker<Void, int[]>{
        private int[] list;

        public SortWorker(int[] unsortedItems){
            list = Arrays.copyOf(unsortedItems, unsortedItems.length);
        }

        //HeapSort algorithm
        //implementation of heap sort
        public void heapify(int list[], int n, int i){
            int largest = i;//get largest as root
            int l = 2 * i + 1;//left = 2*i + 1
            int r = 2 * i + 2;//right = 2*i + 2

            //if left child is larger than root, replace it
            if(l < n && list[l] >  list[largest]){
                largest = l;
            }

            //if right child is larger than root, replace it
            if(r < n && list[r] > list[largest]){
                largest = r;
            } 

            //if largest is not root swap with element i
            if(largest != i){
                int temp = list[i];
                list[i] = list[largest];
                list[largest] = temp;

                //repaint(); to show graphics to user
                publish( Arrays.copyOf(list, list.length) );
                //sleep to slow down graphics
                try { Thread.sleep(100); } catch (Exception e) {}

                //recursivly call this method
                heapify(list, n, largest);
            }
        }

        //method to heapify a subtree
        @Override
        protected Void doInBackground(){
            int n = list.length;

            //build heap
            for(int i = (n/2 - 1); i >= 0; i--){
                heapify(list, n, i);
            }

            //now extract element from heap
            for(int i = n - 1; i > 0; i--){
                //move current root to the end
                int temp = list[0];
                list[0] = list[i];
                list[i] = temp;

                //repaint(); to show graphics to user
                publish( Arrays.copyOf(list, list.length) );
                //sleep to slow down graphics
                try { Thread.sleep(100); } catch (Exception e) {}

                //call max heapify on the reduced heap
                heapify(list, i, 0);
            }
            
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
            list[i] = random.nextInt(HeapSorter.BAR_HEIGHT);
        }

        return list;
    }

    /*
    * method that will run the entire Heap Sorting algorithm.
    * From creating the objects and panels and buttons, to
    * calling all functions to sort, generate, order and return
    * a panel with a visual Heap Sorting algorithm.
    */
    public static JPanel runHeapSort(){
        //creates the object HeapSort, initializes the list, and fills it with randomly generated numbers
        HeapSorter heapSort = new HeapSorter(HeapSorter.generateListNumbers());

        //adds title to the top of the panel
        JLabel title = new JLabel("Heap Sort");
        
        //makes and adds two buttons, generate and sort
        JButton generate = new JButton("Generate Data");
        generate.addActionListener((e)->heapSort.setItems(HeapSorter.generateListNumbers()));
        JButton sort = new JButton("Sort Data");
        sort.addActionListener((e) -> heapSort.sort());

        //adds the buttons to the panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(generate);
        bottomPanel.add(sort);
        
        //creates the mainPanel for the heapSort graphics
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(heapSort, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(title, BorderLayout.NORTH);

        return mainPanel;
    }
}
