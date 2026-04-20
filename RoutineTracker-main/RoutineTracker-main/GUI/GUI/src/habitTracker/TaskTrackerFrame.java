import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskTrackerFrame extends JFrame {
    private JProgressBar progressBar;
    private int taskProgress = 0;
    private int currentLevel = 1;
    private int tasksPerLevel = 4;
    private boolean usePredefinedTasks = true;
    private boolean isDarkMode = false;
    private String userName;

    private JPanel tasksPanel;
    private JLabel levelLabel;
    private JLabel taskStatusLabel;
    private JButton[] taskButtons;
    private JLabel[] taskLabels;
    private JButton switchModeButton;

    public TaskTrackerFrame(String userName) {
        this.userName = userName;
        setTitle("Task Progress Tracker");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel helloLabel = new JLabel("Welcome To Routine Tracker " + userName + "!");
        topPanel.add(helloLabel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        
        JButton statsButton = new JButton("View Statistics");
        statsButton.addActionListener(e -> new StatisticsWindow());
        
        switchModeButton = new JButton("Mode: Predefined Tasks");
        switchModeButton.addActionListener(e -> toggleMode());

        JButton addTaskButton = new JButton("+ Add Task");
        addTaskButton.addActionListener(e -> showAddTaskDialog());

        JRadioButton darkBtn = new JRadioButton("Dark Mode");
        JRadioButton lightBtn = new JRadioButton("Light Mode");
        ButtonGroup group = new ButtonGroup();
        group.add(darkBtn); group.add(lightBtn);

        buttonPanel.add(statsButton);
        buttonPanel.add(switchModeButton);
        buttonPanel.add(addTaskButton);
        buttonPanel.add(darkBtn);
        buttonPanel.add(lightBtn);
        topPanel.add(buttonPanel, BorderLayout.WEST);

        // Date
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy"));
        JLabel dateLabel = new JLabel("📅 " + formattedDate);
        topPanel.add(dateLabel, BorderLayout.EAST);

        // Progress
        JPanel progressPanel = new JPanel(new BorderLayout());
        levelLabel = new JLabel("Level: " + currentLevel);
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        
        progressPanel.add(levelLabel, BorderLayout.NORTH);
        progressPanel.add(new JLabel("Overall Progress "), BorderLayout.CENTER);
        progressPanel.add(progressBar, BorderLayout.SOUTH);

        // Tasks
        tasksPanel = new JPanel(new GridBagLayout());
        taskStatusLabel = new JLabel("Complete Tasks to Increase progress ");

        darkBtn.addActionListener(e -> { isDarkMode = true; applyTheme(mainPanel, true); });
        lightBtn.addActionListener(e -> { isDarkMode = false; applyTheme(mainPanel, false); });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(progressPanel, BorderLayout.NORTH);
        centerPanel.add(tasksPanel, BorderLayout.CENTER);
        centerPanel.add(taskStatusLabel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        createTasksForCurrentLevel();
        setVisible(true);
    }

    private void toggleMode() {
        usePredefinedTasks = !usePredefinedTasks;
        switchModeButton.setText(usePredefinedTasks ? "Mode: Predefined Tasks" : "Mode: Custom Tasks");
        taskStatusLabel.setText(usePredefinedTasks ? "Using Predefined Tasks" : "Using Custom Tasks - Add Task to create your own");
        currentLevel = 1;
        taskProgress = 0;
        progressBar.setValue(0);
        levelLabel.setText("Level: " + currentLevel);
        createTasksForCurrentLevel();
    }
private void createTasksForCurrentLevel() {
        tasksPanel.removeAll();

       
        TitledBorder predefinedBorder = BorderFactory.createTitledBorder("Tasks For Today - Level " + currentLevel);
        TitledBorder customBorder = BorderFactory.createTitledBorder("Custom Tasks - Level " + currentLevel);

        predefinedBorder.setTitleColor(Color.gray);
        customBorder.setTitleColor(Color.gray);

   
        if (usePredefinedTasks) {
            tasksPanel.setBorder(predefinedBorder);
        } else {
            tasksPanel.setBorder(customBorder);
        }

        tasksPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 2, 2, 2);

      
        List<String> currentTasks;
        if (usePredefinedTasks) {
            currentTasks = AppData.taskNames;
        } else {
            currentTasks = AppData.customTasks;

          
            if (currentTasks.isEmpty()) {
                constraints.gridy = 0;
                constraints.gridx = 0;
                constraints.gridwidth = 2;
                JLabel emptyLabel = new JLabel("No Custom Tasks Yet. Please Click 'Add Task' to create Tasks", JLabel.CENTER);
                emptyLabel.setForeground(Color.gray);
                tasksPanel.add(emptyLabel, constraints);
                tasksPanel.revalidate();
                tasksPanel.repaint();
                return;
            }
        }

   
        int startTaskIndex = (currentLevel - 1) * tasksPerLevel;

       
        if (startTaskIndex >= currentTasks.size()) {
            constraints.gridy = 0;
            constraints.gridx = 0;
            constraints.gridwidth = 2;
            JLabel noTasksLabel = new JLabel("No more Tasks Available. Add More Tasks", JLabel.CENTER);
            noTasksLabel.setForeground(Color.RED);
            tasksPanel.add(noTasksLabel, constraints);
            tasksPanel.revalidate();
            tasksPanel.repaint();
            return;
        }

      
        taskButtons = new JButton[tasksPerLevel];
        taskLabels = new JLabel[tasksPerLevel];

      
        for (int i = 0; i < tasksPerLevel; i++) {
            int taskNumber = startTaskIndex + i;

            if (taskNumber < currentTasks.size()) {
                constraints.gridwidth = 1;
                constraints.gridy = i;
                constraints.gridx = 0;
                constraints.weightx = 0.7;

              
                taskLabels[i] = new JLabel(currentTasks.get(taskNumber) + " - PENDING");

                if (!usePredefinedTasks) {
                    taskLabels[i].setForeground(new Color(0, 128, 0)); 
                }

                tasksPanel.add(taskLabels[i], constraints);

               
                constraints.gridx = 1;
                constraints.weightx = 0.3;

                taskButtons[i] = new JButton("Start " + currentTasks.get(taskNumber));

               
                int finalI = i;
                int finalTaskNumber = taskNumber;
                List<String> finalTaskSource = currentTasks;

                taskButtons[i].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        completeTask(finalI, finalTaskNumber, taskLabels[finalI], finalTaskSource);
                    }
                });

                tasksPanel.add(taskButtons[i], constraints);
            }
        }

        tasksPanel.revalidate();
        tasksPanel.repaint();

      
        applyTheme(tasksPanel, isDarkMode);
    }

 private void completeTask(int taskIndex, int taskNumber, JLabel taskLabel, List<String> taskSource) {

        if (taskLabel != null && !taskLabel.getText().contains("Completed")) {
            
          
            taskProgress = taskProgress + (100 / tasksPerLevel);
            progressBar.setValue(taskProgress);

           
            taskLabel.setText(taskSource.get(taskNumber) + "- Completed ");
            taskButtons[taskIndex].setEnabled(false);

          
            taskStatusLabel.setText("Task Completed! Progress " + taskProgress + "%");

         
            LocalDate today = LocalDate.now();

          
            int currentCount = AppData.dailyTaskCount.getOrDefault(today, 0);
            AppData.dailyTaskCount.put(today, currentCount + 1);

          
            if (!AppData.dailyCompletedTasks.containsKey(today)) {
                AppData.dailyCompletedTasks.put(today, new java.util.ArrayList<>());
            }

            String taskType;
            if (usePredefinedTasks) {
                taskType = "[Predefined] ";
            } else {
                taskType = "[Custom] ";
            }

            AppData.dailyCompletedTasks.get(today).add(taskType + taskSource.get(taskNumber));

          
            if (taskProgress >= 100) {
                levelUp();
            }
        }
    }

    private void levelUp() {
      
        currentLevel++;
        levelLabel.setText("Level: " + currentLevel);

      
        taskProgress = 0;
        progressBar.setValue(0);

       
        List<String> currentTasks;
        if (usePredefinedTasks) {
            currentTasks = AppData.taskNames;
        } else {
            currentTasks = AppData.customTasks;
        }

       
        int nextLevelStart = (currentLevel - 1) * tasksPerLevel;

       
        if (nextLevelStart < currentTasks.size()) {
           
            createTasksForCurrentLevel();

            taskStatusLabel.setText("Level Up! Now At Level " + currentLevel + " - New tasks available");

           
            if (taskButtons != null) {
                for (JButton button : taskButtons) {
                    if (button != null) {
                        button.setEnabled(true);
                    }
                }
            }
        }
    }

    private void showAddTaskDialog() {
        if (!usePredefinedTasks) {
            JFrame addTaskFrame = new JFrame("Add Custom Task");
            addTaskFrame.setSize(400, 200);
            addTaskFrame.setLocationRelativeTo(null);
            addTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Task Name:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            JTextField taskNameField = new JTextField(20);
            panel.add(taskNameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weightx = 0;
            JButton saveTaskButton = new JButton("Save Task");
            saveTaskButton.addActionListener(e -> {
                String taskName = taskNameField.getText().trim();
                if (!taskName.isEmpty()) {
                    AppData.customTasks.add(taskName);
                    JOptionPane.showMessageDialog(addTaskFrame, "Task " + taskName + " Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    addTaskFrame.dispose();

                    currentLevel = 1;
                    taskProgress = 0;
                    progressBar.setValue(0);
                    levelLabel.setText("Level: " + currentLevel);
                    createTasksForCurrentLevel();
                } else {
                    JOptionPane.showMessageDialog(addTaskFrame, "Please Enter Task Name to continue!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(saveTaskButton, gbc);

            gbc.gridy = 2;
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> addTaskFrame.dispose());
            panel.add(cancelButton, gbc);

            addTaskFrame.add(panel);
            applyTheme(panel, isDarkMode);
            addTaskFrame.setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(this, "Please Switch to Custom Tasks-mode first!", "Wrong Mode", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void applyTheme(Container container, boolean darkMode) {
        Color bg = darkMode ? new Color(45, 45, 45) : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        Color btnBg = darkMode ? new Color(70, 70, 70) : new Color(220, 220, 220);

        container.setBackground(bg);
        for (Component comp : container.getComponents()) {
            comp.setForeground(fg);
            if (comp instanceof JPanel) {
                applyTheme((Container) comp, darkMode);
            } else if (comp instanceof JButton) {
                comp.setBackground(btnBg);
            } else if (comp instanceof JProgressBar) {
                comp.setBackground(bg);
                comp.setForeground(Color.GREEN);
            }
        }
    }
}
