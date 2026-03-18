import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class App implements ActionListener {
        private static  JLabel userLabel;
        private static  JTextField userText;
        private static  JLabel passwordLabel;
        private static  JPasswordField passwordText;
        private static  JButton button; 
        private static  JLabel success;
        private static JProgressBar progressBar;
        private static int taskProgress = 0;
        private static JPanel tasksPanel;
        private static JLabel [] taskLabels;
        private static int currentLevel = 1;
        private static JLabel levelLabel;
        private static JLabel taskStatusLabel;
        private static JButton[] taskButtons;
        private static List<String> taskNames;
        private static int tasksPerLevel=4;
        private static boolean usePredefinedTasks = true;
        private static List<String> customTasks = new ArrayList<>();
        private static JFrame addTaskFrame;
        private static JButton addTaskButton;
        private static JTextField taskNameField;
        private static JButton saveTaskButton;
        private static JButton switchModeButton;
        private static JRadioButton darkModeSwitchButton;
        private static JRadioButton lightModeSwitchButton;

        private static Map<LocalDate, Integer> dailyTaskCount = new HashMap<>();
        private static Map<LocalDate, List<String>> dailyCompletedTasks = new HashMap<>();
        private static JButton statsButton;
        private static LocalDate currentDate;
        
    public static void main(String[] args) throws Exception {
       
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setSize(350,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        frame.add(panel);

        panel.setLayout(null);

        userLabel = new JLabel("User Name");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);

        userText.setBounds(100,20,165,25);
        panel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10,50, 80, 25);

        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);


        button = new JButton("Login");
        button.setBounds(10,80,80,25);
        button.addActionListener(new App());


        success = new JLabel("");
        success.setBounds(10,110,300,25);

        panel.add(success);

        
        

        panel.add(button);

        frame.setVisible(true);


        initializeAllTasks();
        


    }

    private static void initializeAllTasks(){

        taskNames = new ArrayList<>();

        taskNames.add("Task 1: Clean Kitchen");
        taskNames.add("Task 2: Vacuum The House");
        taskNames.add("Task 3: Cooking");
        taskNames.add("Task 4: Take the dog out");
        taskNames.add("Task 5: Do Programming 4 homework");
        taskNames.add("Task 6: Take trash Out");
        taskNames.add("Task 7: Go to Gym");
        taskNames.add("Task 8: Do Project work");
        taskNames.add("Task 9: Clean Dust");
        taskNames.add("Task 10: Clean the bathroom");
        taskNames.add("Task 11: Dishes");
        taskNames.add("Task 12: Pyykit");
        taskNames.add("Task 13: Go walking");
        taskNames.add("Task 14: Make dinner");
        taskNames.add("Task 15: Call relatives");
        taskNames.add("Task 15: Brush Teeth");




    }



    private static void createProgressFrame(){

        JFrame progressFrame = new JFrame("Task Progress Tracker");
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));

        JPanel topPanel = new JPanel(new BorderLayout());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy");

        String formattedDate = currentDate.format(formatter);
        JLabel dateLabel = new JLabel("📅 " + formattedDate);
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        topPanel.add(dateLabel, BorderLayout.EAST);

        statsButton = new JButton("View Statistics");
        statsButton.addActionListener(e->createStatisticsWindow());

        switchModeButton = new JButton("Mode: Predefined Tasks");
        switchModeButton.addActionListener(e->{usePredefinedTasks= !usePredefinedTasks;
            if(usePredefinedTasks){
                switchModeButton.setText("Mode: Predefined Tasks");
                taskStatusLabel.setText("Using Predefined Tasks");
            }else{
                switchModeButton.setText("Mode: Custom Tasks");
                taskStatusLabel.setText("Using Custom Tasks - Add Task to create your own");
            }
            currentLevel = 1;
            taskProgress = 0;
            progressBar.setValue(0);
            createTasksForCurrentLevel();
        });

        addTaskButton= new JButton("+ Add Task");
        addTaskButton.addActionListener(e->showAddTaskDialog());

        ButtonGroup group = new ButtonGroup();

        darkModeSwitchButton = new JRadioButton("Dark Mode");
        lightModeSwitchButton = new JRadioButton("Light Mode");

        group.add(darkModeSwitchButton);
        group.add(lightModeSwitchButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.add(statsButton);
        buttonPanel.add(switchModeButton);
        buttonPanel.add(addTaskButton);
        buttonPanel.add(darkModeSwitchButton);
        buttonPanel.add(lightModeSwitchButton);
        topPanel.add(buttonPanel, BorderLayout.WEST);
        
        
        
        

        JLabel helloLabel = new JLabel("Welcome To Routine Tracker " + userText.getText() +"!");
        topPanel.add(helloLabel, BorderLayout.NORTH);

        JPanel progressPanel = new JPanel(new BorderLayout());
        JLabel progressLabel = new JLabel("Overall Progress ");

        JPanel levelProgressPanel = new JPanel(new BorderLayout());

        levelLabel = new JLabel("Level: " + currentLevel);
        levelLabel.setHorizontalAlignment(JLabel.CENTER);

        progressBar = new JProgressBar(0,100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        levelProgressPanel.add(levelLabel, BorderLayout.NORTH);
        levelProgressPanel.add(progressLabel, BorderLayout.CENTER);

        progressPanel.add(levelProgressPanel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        tasksPanel = new JPanel(new GridLayout(0,1,5,5));

        tasksPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tasks"));

        taskStatusLabel = new JLabel("Complete Tasks to Increase progress ");

            createTasksForCurrentLevel();
        
        darkModeSwitchButton.addActionListener(e->{
            applyTheme(mainPanel, true);
            mainPanel.repaint();
        });

        lightModeSwitchButton.addActionListener(e->{
            applyTheme(mainPanel, false);
            mainPanel.repaint();
        });


           

            JPanel centerPanel = new JPanel(new BorderLayout());

            centerPanel.add(progressPanel,BorderLayout.NORTH);
            centerPanel.add(tasksPanel, BorderLayout.CENTER);
            centerPanel.add(taskStatusLabel, BorderLayout.SOUTH);

            mainPanel.add(topPanel,BorderLayout.NORTH);
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));


            progressFrame.add(mainPanel);
            progressFrame.setSize(800,500);
            progressFrame.setLocationRelativeTo(null);
            progressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            progressFrame.setVisible(true);

            
        

        
    }

    private static void applyTheme(Container container, boolean darkMode){
        Color backGround;
        Color foreGround;
        Color buttonBackGround;

        if(darkMode){
            backGround = new Color(45,45,45);
            foreGround = Color.WHITE;
            buttonBackGround = new Color(70,70,70);
        }else{
            backGround=Color.white;
            foreGround=Color.BLACK;
            buttonBackGround=new Color(220,220,220);
        }

        for(Component comp : container.getComponents()){
            if(comp instanceof  JPanel){
                comp.setBackground(backGround);
                applyTheme((Container) comp, darkMode);
            }else if(comp instanceof JLabel){
                comp.setForeground(foreGround);
                comp.setBackground(backGround);
            }else if(comp instanceof JButton){
                comp.setBackground(buttonBackGround);
                comp.setForeground(foreGround);
            }else if(comp instanceof JProgressBar){
                comp.setBackground(backGround);
                comp.setForeground(Color.GREEN);
            }
        }
        container.setBackground(backGround);
    }

    private static void showAddTaskDialog(){
        if(!usePredefinedTasks){
            addTaskFrame = new JFrame("Add Custom Task");
            addTaskFrame.setSize(400,200);
            addTaskFrame.setLocationRelativeTo(null);
            addTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Task Name:"), gbc);

            gbc.gridx=1;
            gbc.gridy=0;
            gbc.weightx=1.0;
            taskNameField= new JTextField(20);
            panel.add(taskNameField,gbc);

            gbc.gridx=0;
            gbc.gridy=1;
            gbc.gridwidth=2;
            gbc.weightx=0;
            saveTaskButton=new JButton("Save Task");
            saveTaskButton.addActionListener(e->saveCustomTask());
            panel.add(saveTaskButton, gbc);

            gbc.gridy=2;
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e->addTaskFrame.dispose());
            panel.add(cancelButton,gbc);

            addTaskFrame.add(panel);
            addTaskFrame.setVisible(true);
            
        }else{
            JOptionPane.showMessageDialog(null, "Please Switch to Custom Tasks-mode first!","Wrong Mode",JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void saveCustomTask(){
        String taskName = taskNameField.getText().trim();

        if(!taskName.isEmpty()){
            customTasks.add(taskName);
            taskNameField.setText("");
            JOptionPane.showMessageDialog(addTaskFrame, "Task " + taskName + " Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            addTaskFrame.dispose();

            currentLevel = 1;
            taskProgress =0;
            progressBar.setValue(0);


            createTasksForCurrentLevel();

            if(levelLabel!=null){
                levelLabel.setText("Level: "+currentLevel);
            }
        }else{
            JOptionPane.showMessageDialog(addTaskFrame, "Please Enter Task Name to continue!" +"Error"+JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createTasksForCurrentLevel(){

        tasksPanel.removeAll();
        
        if(usePredefinedTasks){
            tasksPanel.setBorder(BorderFactory.createTitledBorder("Tasks For Today - Level "+ currentLevel));
        }else{
            tasksPanel.setBorder(BorderFactory.createTitledBorder("Custom Tasks - Level " +currentLevel));
        }

        tasksPanel.setLayout(new GridBagLayout());
        GridBagConstraints constrains = new GridBagConstraints();
        constrains.fill = GridBagConstraints.HORIZONTAL;
        constrains.weightx = 1.0;
        constrains.insets = new Insets(2, 2, 2, 2);

        List<String> currentTasks;
        if(usePredefinedTasks){
            currentTasks=taskNames;
        }else{
            currentTasks=customTasks;

            if(customTasks.isEmpty()){
                constrains.gridy=0;
                constrains.gridx=0;
                constrains.gridwidth=2;
                JLabel emptyLabel = new JLabel("No Custom Tasks Yet. Please Click 'Add Task' to create Tasks", JLabel.CENTER);
                emptyLabel.setForeground(Color.gray);
                tasksPanel.add(emptyLabel,constrains);
                tasksPanel.revalidate();
                tasksPanel.repaint();
                return;
            }
        }


        int startTaskIndex = (currentLevel-1)*tasksPerLevel;

        if(startTaskIndex>=currentTasks.size()){
            constrains.gridy=0;
            constrains.gridx=0;
            constrains.gridwidth=2;
            JLabel noTasksLabel = new JLabel("No more Tasks Available. Add More Tasks", JLabel.CENTER);
            noTasksLabel.setForeground(Color.RED);
            tasksPanel.add(noTasksLabel,constrains);
            tasksPanel.revalidate();
            tasksPanel.repaint();
            return;
        }
        taskButtons = new JButton[tasksPerLevel];
        taskLabels = new JLabel[tasksPerLevel];

        for(int i=0; i<tasksPerLevel;i++){
            int taskNumber = startTaskIndex+i;

            if(taskNumber < currentTasks.size()){
                constrains.gridwidth =1;
                constrains.gridy = i;
                constrains.gridx = 0;
                constrains.weightx = 0.7;

                //JPanel taskRow = new JPanel(new BorderLayout(5,5));

                taskLabels[i] = new JLabel(currentTasks.get(taskNumber) + " - PENDING");

                if(!usePredefinedTasks){
                    taskLabels[i].setForeground(Color.GREEN);
                }

                tasksPanel.add(taskLabels[i], constrains);

                constrains.gridx=1;
                constrains.weightx = 0.3;

                taskButtons[i] = new JButton("Start " + currentTasks.get(taskNumber));

                int finalI = i;
                int finalTaskNumber = taskNumber;

                taskButtons[i].addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        completeTask(finalI, finalTaskNumber, taskLabels[finalI],currentTasks);
                    }
                });

                //taskRow.add(taskLabels[i], BorderLayout.CENTER);
                //taskRow.add(taskButtons[i], BorderLayout.EAST);
                tasksPanel.add(taskButtons[i],constrains);
            }
        }

        tasksPanel.revalidate();
        tasksPanel.repaint();

    }



    private static void completeTask(int taskIndex, int taskNumber, JLabel taskLabel, List<String> taskSource){

            if(taskLabel!=null && !taskLabel.getText().contains("Completed")){
                taskProgress=taskProgress+(100/tasksPerLevel);
                progressBar.setValue(taskProgress);

                taskLabel.setText(taskSource.get(taskNumber)+ "- Completed ");

                taskButtons[taskIndex].setEnabled(false);

                taskStatusLabel.setText("Task Completed! Progress " + taskProgress + "%");

                LocalDate today =LocalDate.now();

                Integer currentCount;

                if(dailyTaskCount.containsKey(today)){
                    currentCount = dailyTaskCount.get(today);
                }else{
                    currentCount=0;
                }

                dailyTaskCount.put(today, currentCount+1);

                if(!dailyCompletedTasks.containsKey(today)){
                    dailyCompletedTasks.put(today, new ArrayList<>());
                }
                String taskType;

                if(usePredefinedTasks){
                    taskType="[Predefined] ";
                }else{
                    taskType="[Custom] ";
                }

                dailyCompletedTasks.get(today).add(taskType + taskSource.get(taskNumber));

                if(taskProgress>=100){
                 levelUp();
                }
            }
    }


    private static void levelUp(){
        currentLevel++;
        levelLabel.setText("Level: "+currentLevel);

        taskProgress=0;
        progressBar.setValue(0);

        List<String> currentTasks;

        if(usePredefinedTasks){
            currentTasks=taskNames;
        }else{
            currentTasks=customTasks;
        }


        int nextLevelStart  = (currentLevel-1)*tasksPerLevel;

        if(nextLevelStart < currentTasks.size()){
            createTasksForCurrentLevel();

            taskStatusLabel.setText("Level Up! Now At Level "+currentLevel+" - New tasks For the current level");

            for(JButton button : taskButtons){
                if(button != null){
                    button.setEnabled(true);
                }
            }
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        
        String user = userText.getText();
        String password = passwordText.getText();
        

        if (user.equals("Ville") && password.equals("12345")) {
            
            JFrame succesFrame = new JFrame("Login Successful");
            JPanel successPanel = new JPanel();

            succesFrame.setSize(350,200);
            succesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            succesFrame.add(successPanel);

            JLabel successMessage = new JLabel("Login Successful!");
            successMessage.setBounds(100,25,150,25);

            successPanel.add(successMessage);

            JButton closeButton = new JButton("OK");

            closeButton.setBounds(100,70,80,25);
            closeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    succesFrame.dispose();

                    currentLevel=1;
                    taskProgress=0;

                    createProgressFrame();
                }
                
            });

            successPanel.setLayout(null);
            successPanel.add(closeButton);
            succesFrame.setVisible(true);

            passwordText.setText("");

            button.setEnabled(false);
        }else{
            success.setText("Invalid Username or Password");
        }

        
    }


    private static void createStatisticsWindow(){

        JFrame statisticsFrame = new JFrame("Daily Statistics");
        statisticsFrame.setSize(600,550);
        statisticsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statisticsFrame.setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        JLabel titleLabel = new JLabel("Daily Task Statistics", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel,BorderLayout.NORTH);

        JPanel graphPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                drawBarGraph(g);
            }

            @Override
            public Dimension getPreferredSize(){
                return new Dimension(550,300);
            }
        };

        graphPanel.setBackground(Color.CYAN);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel graphContainer = new JPanel(new BorderLayout());
        graphContainer.add(graphPanel, BorderLayout.CENTER);

        int totalCompletedTasks = 0;
        for(List<String> tasks : dailyCompletedTasks.values()){
            totalCompletedTasks+=tasks.size();
        }

        int activeDays = dailyTaskCount.size();

        JLabel statisticLabel = new JLabel(String.format("Total Tasks Completed: %d | Active Days: %d",totalCompletedTasks, activeDays), JLabel.LEFT);
        statisticLabel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        graphContainer.add(statisticLabel, BorderLayout.SOUTH);

        JPanel controlPanel = new JPanel();

        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.addActionListener(e -> {
            dailyTaskCount.clear();
            dailyCompletedTasks.clear();
            graphPanel.repaint();
            statisticLabel.setText("Total Tasks Completed: 0 | Active Days: 0");
        });

        



        controlPanel.add(clearHistoryButton);

        mainPanel.add(graphContainer, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        statisticsFrame.add(mainPanel);
        statisticsFrame.setVisible(true);



    }


    private static void drawBarGraph(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        int width = 550;
        int height = 250;
        int padding = 50;
        int graphHeight = height - 2*padding;
        int graphWidth = width - 2*padding;

        g2d.drawLine(padding, padding, padding, height-padding);
        g2d.drawLine(padding, height-padding, width-padding, height-padding);

        if(dailyTaskCount.isEmpty()){
            g2d.drawString("No Data Available", width/2-50, height/2);
            return;
        }

        List<LocalDate> dates = new ArrayList<>(dailyTaskCount.keySet());
        dates.sort((d1, d2) -> d1.compareTo(d2));

        int maxTasks = dailyTaskCount.values().stream().max(Integer::compare).orElse(1);

        if(maxTasks == 0){
            maxTasks =1;
        }

        int barWidth = (graphWidth-20)/dates.size()-10;

        if(barWidth>50){
            barWidth=50;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for(int i = 0; i<dates.size(); i++){
            LocalDate date = dates.get(i);
            int taskCount = dailyTaskCount.get(date);

            int x = padding+20+i*(barWidth+15);
            int barHeight = (int) ((double) taskCount / maxTasks * (graphHeight-30));

            int y = height-padding-barHeight;

            g2d.setColor(new Color(100,149,237));
            g2d.fillRect(x, y, barWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, barWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(taskCount), x+barWidth/2-5, y-5);

            
            g2d.drawString(date.format(formatter), x-5, height-padding+15);

            DateTimeFormatter daysOfWeekFormatter = DateTimeFormatter.ofPattern("EEE");
            g2d.drawString(date.format(daysOfWeekFormatter), x-5, height-padding+30);
        }

        for(int i = 0; i<=maxTasks;i++){

            int y = height-padding-(i*(graphHeight-30)/maxTasks);

            g2d.drawString(String.valueOf(i), padding-25, y+5);
            g2d.drawLine(padding-5,y,padding,y);

            
        }
        g2d.drawString("Tasks Completed", 10,50);
    }
}
