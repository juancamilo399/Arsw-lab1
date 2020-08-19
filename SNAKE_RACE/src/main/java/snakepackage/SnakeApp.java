package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private JButton play,pause;
    JLabel JLabelLongestSnake,JLabelWorstSnake;
    private static Board board;
    private AtomicInteger longestSnake;
    private AtomicInteger worstSnake;
    private AtomicInteger deaths;
    private EventNotifier notifier;

    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];


    public SnakeApp() {
        notifier = new EventNotifier(thread);
        worstSnake =  new AtomicInteger();
        deaths = new AtomicInteger();
        worstSnake.set(-1);
        longestSnake = new AtomicInteger();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        
        
        frame.add(board,BorderLayout.CENTER);
        
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        JLabelLongestSnake = new JLabel ("The snakes with the longests sizes are ");
        JLabelWorstSnake = new JLabel ("The worst snake is");
        play = new JButton("Jugar");
        pause = new JButton("Pausar");
        pause.setEnabled(true);
        play.setEnabled(false);
        actionsBPabel.add(play);
        actionsBPabel.add(pause);
        actionsBPabel.add(JLabelWorstSnake);
        frame.add(actionsBPabel,BorderLayout.SOUTH);
        frame.add(JLabelLongestSnake,BorderLayout.NORTH);
        prepareButtons();

    }

    private void actualizeData(){
        String field = new String();
        field = field + "The snakes with the longests sizes are ";
        for (int i = 0; i != MAX_THREADS; i++) {
            if(snakes[i].getBody().size() == longestSnake.get()){
                field = field + "Snake " + snakes[i].getIdt() + " with size = " + longestSnake.get() + " ";
            }
        }
        JLabelLongestSnake.setText(field);
        if(worstSnake.get()!=-1) {
            JLabelWorstSnake.setText("The worst snake is "+worstSnake.get());
        }
    }
    private void prepareButtons(){
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i != MAX_THREADS; i++) {
                    snakes[i].play();
                }
                pause.setEnabled(true);
                play.setEnabled(false);
            }
        });
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i != MAX_THREADS; i++) {
                    snakes[i].pause();
                }
                actualizeData();
                play.setEnabled(true);
                pause.setEnabled(false);
            }
        });
    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1 , longestSnake , worstSnake , notifier , deaths);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            thread[i].start();
        }

        frame.setVisible(true);

            
        /*while (true) {
            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                    x++;
                }
            }
            if (x == MAX_THREADS) {
                break;
            }
        }*/

        

    }

    public static SnakeApp getApp() {
        return app;
    }

}
