/***********************************
* AUTHOR:     Affan Khan
* CREATE DATE:    2021-06-09
* PURPOSE:    This Class extends from JPanel to create a JPanel that
    holds a list of randomly generated bars, and uses the selection Sort
    algorithm to sort through them.
***********************************/
package src;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class SelectionSorter extends JPanel{
    //variables for bar graphics
    private final static int BAR_WIDTH = 30;
    private final static int BAR_HEIGHT = 400;
    private int[]list;
    //JPanel variable object to create/display and return
    private static JPanel mainPanel;

    //initializes the list of SelectionSorter object
    private SelectionSorter(int[] list){
        this.list = list;
    }
    //sets the list to this SelectionSorter object's list
    private void setItems(int[] list){
        this.list = list;
        repaint();
    }
    //sorts the SelectionSorter object
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
    * Returns the Dimensions of the rectangle bars used for SelectionSort graph
    */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(list.length * BAR_WIDTH, BAR_HEIGHT + 20);
    }

    /*
    * Class that sorts the bars using the Selection algorithm
    */
    private class SortWorker extends SwingWorker<Void, int[]>{
        private int[] list;

        public SortWorker(int[] unsortedItems){
            list = Arrays.copyOf(unsortedItems, unsortedItems.length);
        }

        //SelectionSort algorithm
        @Override
        protected Void doInBackground(){
            int n = list.length;

            //one by one goes through unsorted subarray
            for(int i = 0; i < (n - 1); i++){
                //get min element in unsorted array
                int min = i;
                for(int j = i+1; j < n; j++){
                    if(list[j] < list[min]){
                        min = j;
                    }  
                }

                //swaps min element with 1st element
                int temp = list[min];
                list[min] = list[i];
                list[i] = temp;

                //repaint(); to show graphics to user
                publish( Arrays.copyOf(list, list.length) );
                //sleep to slow down graphics
                try { Thread.sleep(100); } catch (Exception e) {}
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

    //method that generates random numbers and fills an int array
    private static int[]generateListNumbers(){
        //variables initialized
        int[] list = new int[20];

        /*creates random object and generates number from 0 to 
        BAR_HEIGHT for each element*/
        Random random = new Random();
        for(int i = 0; i < list.length; i++){
            list[i] = random.nextInt(SelectionSorter.BAR_HEIGHT);
        }

        return list;
    }

    /*
    * method that will run the entire Selection Sorting algorithm.
    * From creating the objects and panels and buttons, to
    * calling all functions to sort, generate, order and return
    * a panel with a visual Selection Sorting algorithm.
    */
    public static JPanel runSelectionSort(){
        //creates the object SlecetionSort, initializes the list, and fills it with randomly generated numbers
        SelectionSorter selectionSort = new SelectionSorter(SelectionSorter.generateListNumbers());

        //adds the type of sorting algorithm to the top of the panel
        JLabel title = new JLabel("Selection Sort");
        
        //makes and adds two buttons, generate and sort
        JButton generate = new JButton("Generate Data");
        generate.addActionListener((e)->selectionSort.setItems(SelectionSorter.generateListNumbers()));
        JButton sort = new JButton("Sort Data");
        sort.addActionListener((e) -> selectionSort.sort());

        //adds the buttons to the panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(generate);
        bottomPanel.add(sort);
        
        //creates the mainPanel for the selectionSort graphics
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(selectionSort, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(title, BorderLayout.NORTH);

        return mainPanel;
    }
}
