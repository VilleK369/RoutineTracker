import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class StatisticsWindow extends JFrame {
    public StatisticsWindow() {
        setTitle("Daily Statistics");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Daily Task Statistics", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarGraph(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(550, 300);
            }
        };

        graphPanel.setBackground(Color.CYAN);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

       
        int totalCompletedTasks = AppData.dailyCompletedTasks.values().stream().mapToInt(List::size).sum();
        int activeDays = AppData.dailyTaskCount.size();

        JLabel statisticLabel = new JLabel(String.format("Total Tasks Completed: %d | Active Days: %d", totalCompletedTasks, activeDays), JLabel.LEFT);
        statisticLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.addActionListener(e -> {
            AppData.dailyTaskCount.clear();
            AppData.dailyCompletedTasks.clear();
            graphPanel.repaint();
            statisticLabel.setText("Total Tasks Completed: 0 | Active Days: 0");
        });

        mainPanel.add(graphPanel, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(statisticLabel, BorderLayout.NORTH);
        controlPanel.add(clearHistoryButton, BorderLayout.SOUTH);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void drawBarGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int width = 550;
        int height = 250;
        int padding = 50;
        int graphHeight = height - 2 * padding;
        int graphWidth = width - 2 * padding;

        // Piirretään akselit
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, padding, padding, height - padding); // Y-akseli
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-akseli

        if (AppData.dailyTaskCount.isEmpty()) {
            g2d.drawString("No Data Available", width / 2 - 50, height / 2);
            return;
        }

     
        List<LocalDate> dates = new ArrayList<>(AppData.dailyTaskCount.keySet());
        dates.sort(LocalDate::compareTo);

       
        int maxTasks = AppData.dailyTaskCount.values().stream().max(Integer::compare).orElse(1);
        if (maxTasks == 0) maxTasks = 1;

    
        int barWidth = (graphWidth - 20) / dates.size() - 10;
        if (barWidth > 50) barWidth = 50;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            int taskCount = AppData.dailyTaskCount.get(date);

            int x = padding + 20 + i * (barWidth + 15);
            int barHeight = (int) ((double) taskCount / maxTasks * (graphHeight - 30));
            int y = height - padding - barHeight;

         
            g2d.setColor(new Color(100, 149, 237)); // Cornflower Blue
            g2d.fillRect(x, y, barWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, barWidth, barHeight);

         
            g2d.drawString(String.valueOf(taskCount), x + barWidth / 2 - 5, y - 5);

            g2d.drawString(date.format(dateFormatter), x - 5, height - padding + 15);

           
            g2d.drawString(date.format(dayFormatter), x - 5, height - padding + 30);
        }

      
        for (int i = 0; i <= maxTasks; i++) {
            int y = height - padding - (i * (graphHeight - 30) / maxTasks);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(i), padding - 25, y + 5);
            g2d.drawLine(padding - 5, y, padding, y); 
        }

        g2d.drawString("Tasks Completed", 10, 50);
    }
}
