package agh.lab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.JFrame;

public class GUI {
    private JFrame frame;
    private JButton startButton;
    private JButton pauseButton;
    private JButton restartButton;
    private JButton saveButton;
    private JButton animalStatsButton;
    private JTextArea textAreaSimulation;
    private JTextArea textAreaStatistics;
    private JTextArea textAreaAnimalStats;
    private JTextField textFieldCoordX;
    private JTextField textFieldCoordY;
    private Timer timer;
    private SimulationEngine simulationEngine;
    private final Map<String, Object> parameters;
    private final boolean firstSimulation;
    private Animal followedAnimal;
    private final int mapWidth;
    private final int mapHeight;

    public GUI(Map<String, Object> parameters, boolean firstSimulation) {
        this.parameters = parameters;
        this.simulationEngine = new SimulationEngine(parameters);
        this.firstSimulation = firstSimulation;
        this.mapWidth = (int) parameters.get("width");
        this.mapHeight = (int) parameters.get("height");
    }

    public void startSimulation() {
        simulationGUI();
    }

    private void simulationGUI() {
        this.frame = new JFrame("Evolution Simulator");
        int frameWidth = Math.max(mapWidth * 21, 575);
        int frameHeight = Math.max(mapHeight * 25 + 350, 915);
        this.frame.setSize(frameWidth, frameHeight);
        this.frame.setLayout(null);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (this.firstSimulation) {
            setLocationToLeft(this.frame);
        } else {
            setLocationNextTo(this.frame, frameWidth);
        }

        this.startButton = new JButton("Start");
        this.startButton.setBounds(50, 25, 75, 25);
        this.startButton.addActionListener(e -> timer.start());
        this.frame.add(startButton);

        this.pauseButton = new JButton("Stop");
        this.pauseButton.setBounds(150, 25, 75, 25);
        this.pauseButton.addActionListener(e -> timer.stop());
        this.frame.add(pauseButton);

        this.restartButton = new JButton("Restart");
        this.restartButton.setBounds(250, 25, 100, 25);
        this.restartButton.addActionListener(e -> {
            timer.stop();
            simulationEngine = new SimulationEngine(parameters);
            textAreaStatistics.setText(simulationEngine.getStats());
            frame.add(textAreaStatistics);
            textAreaSimulation.setText(simulationEngine.toString());
            frame.add(textAreaSimulation);
            textAreaAnimalStats.setText("");
            followedAnimal = null;
            frame.repaint();

        });
        this.frame.add(restartButton);

        this.saveButton = new JButton("Save");
        this.saveButton.setBounds(375, 25, 75, 25);
        this.saveButton.addActionListener(e -> simulationEngine.writeStats(firstSimulation));
        this.frame.add(saveButton);

        this.textAreaStatistics = new JTextArea("");
        textAreaStatistics.setBounds(50, 75, 400, 125);
        textAreaStatistics.setText(simulationEngine.getStats());
        textAreaStatistics.setEditable(false);
        this.frame.add(textAreaStatistics);

        this.textFieldCoordX = new JTextField("X");
        this.textFieldCoordX.setBounds(50, 225, 100, 25);
        this.frame.add(textFieldCoordX);

        this.textFieldCoordY = new JTextField("Y");
        this.textFieldCoordY.setBounds(175, 225, 100, 25);
        this.frame.add(textFieldCoordY);

        this.textAreaAnimalStats = new JTextArea("");
        textAreaAnimalStats.setBounds(50, 275, 400, 150);
        textAreaAnimalStats.setEditable(false);
        this.frame.add(textAreaAnimalStats);

        this.animalStatsButton = new JButton("Follow animal");
        this.animalStatsButton.setBounds(300, 225, 150, 25);
        this.animalStatsButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(textFieldCoordX.getText());
                int y = Integer.parseInt(textFieldCoordY.getText());
                if(followedAnimal != null){
                    followedAnimal.setFollowing(false);
                }
                followedAnimal = simulationEngine.getStrongestAnimalAt(x, y);
                if(followedAnimal != null){
                    followedAnimal.setFollowing(true);
                }
                updateAnimalStatsTextArea();
                if (x > mapWidth || y > mapHeight) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                textAreaAnimalStats.setText("Couldn't parse that input.\nPlease write coordinates that are integer in bounds of map.");
                frame.add(textAreaAnimalStats);
                frame.repaint();
            }
        });
        this.frame.add(animalStatsButton);

        int textAreaSimulationWidth = 120 + mapWidth * 14;
        int textAreaSimulationHeight = 120 + mapHeight * 14;
        this.textAreaSimulation = new JTextArea("");
        textAreaSimulation.setBounds(50, 450, textAreaSimulationWidth, textAreaSimulationHeight);
        textAreaSimulation.setText(simulationEngine.toString());
        textAreaSimulation.setEditable(false);
        this.frame.add(textAreaSimulation);
        frame.repaint();
        timer = new Timer(150, timerListener);
    }

    private final ActionListener timerListener = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            simulationEngine.dayPassed();
            textAreaStatistics.setText(simulationEngine.getStats());
            frame.add(textAreaStatistics);
            textAreaSimulation.setText(simulationEngine.toString());
            frame.add(textAreaSimulation);
            if (followedAnimal != null) {
                updateAnimalStatsTextArea();
            }
            frame.repaint();
        }
    };

    private void updateAnimalStatsTextArea() {
        if (followedAnimal != null) {
            textAreaAnimalStats.setText(followedAnimal.getStats());
        } else {
            textAreaAnimalStats.setText("There is no animal on this tile");
        }
        frame.add(textAreaAnimalStats);
        frame.repaint();
    }

    private Rectangle getMaxWindowBounds(JFrame frame) {
        GraphicsConfiguration config = frame.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        return bounds;
    }

    private void setLocationToLeft(JFrame frame) {
        frame.setLocation(getMaxWindowBounds(frame).x, frame.getY());
    }

    private void setLocationNextTo(JFrame frame, int width) {
        frame.setLocation(frame.getX() + width, getMaxWindowBounds(frame).y);
    }

}

