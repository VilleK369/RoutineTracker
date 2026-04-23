import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskTrackerFrame extends JFrame implements LanguageChangeListener {
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
    private JLabel helloLabel;
    private JLabel progressTextLabel;

    private LocalizationManager lang;
    

    public TaskTrackerFrame(String userName) {

        
        lang = LocalizationManager.getInstance();
        lang.addListener(this);

        this.userName = userName;
        setTitle(lang.getString("app.title"));
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        helloLabel = new JLabel(lang.getString("app.welcome") + " " + userName + "!");
        topPanel.add(helloLabel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        
        JButton statsButton = new JButton(lang.getString("button.view_stats"));
        statsButton.addActionListener(e -> new StatisticsWindow());
        
        switchModeButton = new JButton(lang.getString("mode.predefined"));
        switchModeButton.addActionListener(e -> toggleMode());

        JButton addTaskButton = new JButton(lang.getString("button.add_task"));
        addTaskButton.addActionListener(e -> showAddTaskDialog());

        JRadioButton darkBtn = new JRadioButton(lang.getString("button.dark_mode"));
        JRadioButton lightBtn = new JRadioButton(lang.getString("button.light_mode"));
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
        levelLabel = new JLabel(lang.getString("app.level") + ": " + currentLevel);
        levelLabel.setHorizontalAlignment(JLabel.CENTER);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        
        progressTextLabel = new JLabel(lang.getString("app.progress"));


        progressPanel.add(levelLabel, BorderLayout.NORTH);
        progressPanel.add(progressTextLabel, BorderLayout.CENTER);
        progressPanel.add(progressBar, BorderLayout.SOUTH);

        // Tasks
        tasksPanel = new JPanel(new GridBagLayout());
        taskStatusLabel = new JLabel(lang.getString("app.complete_tasks"));

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
        switchModeButton.setText(usePredefinedTasks ? lang.getString("mode.predefined") : lang.getString("mode.custom"));
        taskStatusLabel.setText(usePredefinedTasks ? lang.getString("mode.predefined_status") : lang.getString("mode.custom_status"));
        currentLevel = 1;
        taskProgress = 0;
        progressBar.setValue(0);
        levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);
        createTasksForCurrentLevel();
    }

    @Override
    public void onLanguageChanged() {

    setTitle(lang.getString("app.title"));

    helloLabel.setText(lang.getString("app.welcome") + " " + userName + "!");
    levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);
    progressTextLabel.setText(lang.getString("app.progress"));

    
    if (usePredefinedTasks) {
        switchModeButton.setText(lang.getString("mode.predefined"));
    } else {
        switchModeButton.setText(lang.getString("mode.custom"));
    }

    taskStatusLabel.setText(lang.getString("app.complete_tasks"));

    createTasksForCurrentLevel();
}

    private void createTasksForCurrentLevel() {
        tasksPanel.removeAll();


        TitledBorder predefinedBorder = BorderFactory.createTitledBorder(lang.getString("tasks.predefined_title") + currentLevel);
        TitledBorder customBorder = BorderFactory.createTitledBorder(lang.getString("tasks.custom_title") + currentLevel);

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
                JLabel emptyLabel = new JLabel(lang.getString("empty.custom_tasks"), JLabel.CENTER);
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
            JLabel noTasksLabel = new JLabel(lang.getString("empty.no_tasks"), JLabel.CENTER);
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

              
                taskLabels[i] = new JLabel(currentTasks.get(taskNumber) + " - " + lang.getString("task.pending"));

                if (!usePredefinedTasks) {
                    taskLabels[i].setForeground(new Color(0, 128, 0)); 
                }

                tasksPanel.add(taskLabels[i], constraints);

               
                constraints.gridx = 1;
                constraints.weightx = 0.3;

                taskButtons[i] = new JButton(lang.getString("task.start") + " " + currentTasks.get(taskNumber));

               
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

        if (taskLabel != null && !taskLabel.getText().contains(lang.getString("task.completed"))) {
            
          
            taskProgress = taskProgress + (100 / tasksPerLevel);
            progressBar.setValue(taskProgress);

           
            taskLabel.setText(taskSource.get(taskNumber) + "- " + lang.getString("task.completed"));
            taskButtons[taskIndex].setEnabled(false);

          
            taskStatusLabel.setText(lang.getString("app.task_completed") + " " +taskProgress + "%");

         
            LocalDate today = LocalDate.now();

          
            int currentCount = AppData.dailyTaskCount.getOrDefault(today, 0);
            AppData.dailyTaskCount.put(today, currentCount + 1);

          
            if (!AppData.dailyCompletedTasks.containsKey(today)) {
                AppData.dailyCompletedTasks.put(today, new java.util.ArrayList<>());
            }

            String taskType;
            if (usePredefinedTasks) {
                taskType = "[" + lang.getString("task.predefined_prefix") + "] ";
            } else {
                 taskType = "[" + lang.getString("task.custom_prefix") + "] ";
            }

            AppData.dailyCompletedTasks.get(today).add(taskType + taskSource.get(taskNumber));

          
            if (taskProgress >= 100) {
                levelUp();
            }
        }
    }

    private void levelUp() {
      
        currentLevel++;
        levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);

      
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

            taskStatusLabel.setText(lang.getString("app.level_up") + " " +currentLevel + " " + lang.getString("app.new_tasks"));

           
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
            JFrame addTaskFrame = new JFrame(lang.getString("dialog.add_task_title"));
            addTaskFrame.setSize(400, 200);
            addTaskFrame.setLocationRelativeTo(null);
            addTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel(lang.getString("dialog.task_name")), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            JTextField taskNameField = new JTextField(20);
            panel.add(taskNameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weightx = 0;
            JButton saveTaskButton = new JButton(lang.getString("dialog.save_task"));
            saveTaskButton.addActionListener(e -> {
                String taskName = taskNameField.getText().trim();
                if (!taskName.isEmpty()) {
                    AppData.customTasks.add(taskName);
                    JOptionPane.showMessageDialog(addTaskFrame, lang.getString("dialog.task_name") + taskName + lang.getString("dialog.task_added"), "Success", JOptionPane.INFORMATION_MESSAGE);
                    addTaskFrame.dispose();

                    currentLevel = 1;
                    taskProgress = 0;
                    progressBar.setValue(0);
                    levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);
                    createTasksForCurrentLevel();
                } else {
                    JOptionPane.showMessageDialog(addTaskFrame, lang.getString("dialog.empty_task"), lang.getString("error.message"), JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(saveTaskButton, gbc);

            gbc.gridy = 2;
            JButton cancelButton = new JButton(lang.getString("dialog.cancel"));
            cancelButton.addActionListener(e -> addTaskFrame.dispose());
            panel.add(cancelButton, gbc);

            addTaskFrame.add(panel);
            applyTheme(panel, isDarkMode);
            addTaskFrame.setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(this, lang.getString("dialog.wrong_mode"), lang.getString("error.message_wrong_mode"), JOptionPane.ERROR_MESSAGE);
        }
    }

private static void applyTheme(Container container, boolean darkMode) {
   
    Color backGround;
    Color foreGround;
    Color buttonBackGround;
    Color fieldBackGround; 

    if (darkMode) {
        backGround = new Color(45, 45, 45);
        foreGround = Color.WHITE;
        buttonBackGround = new Color(70, 70, 70);
        fieldBackGround = new Color(30, 30, 30); 
    } else {
        backGround = Color.WHITE;
        foreGround = Color.BLACK;
        buttonBackGround = new Color(220, 220, 220);
        fieldBackGround = Color.WHITE;
    }

    container.setBackground(backGround);

  
    for (Component comp : container.getComponents()) {
      
        comp.setForeground(foreGround);

        if (comp instanceof JPanel) {
           
            comp.setBackground(backGround);
            applyTheme((Container) comp, darkMode);
        } else if (comp instanceof JButton) {
          
            comp.setBackground(buttonBackGround);
        } else if (comp instanceof JTextField) {
           
            comp.setBackground(fieldBackGround);
            ((JTextField) comp).setCaretColor(foreGround);
        } else if (comp instanceof JRadioButton) {
           
            comp.setBackground(backGround);
            ((JRadioButton) comp).setOpaque(true);
        } else if (comp instanceof JProgressBar) {
         
            comp.setBackground(backGround);
            comp.setForeground(Color.GREEN);
        }
    }
}
}
