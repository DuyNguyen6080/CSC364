import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Control extends JPanel{
    public JComboBox<Integer> gridOption;
    public JButton start;
    public JButton end;
    public JButton reset;
    private Vector<Integer> gridsOption = new Vector<>();
    private Blackboard blackboard;
    private ActionListener resetListener;

    public Control(Blackboard blackboard) {
        this.blackboard = blackboard;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(400, 50));

        start = new JButton("Start");
        end = new JButton("End");
        reset = new JButton("Reset");

        int limit = 1000;
        for (int i = 10 ; i <= limit; i*=10 ) {
            gridsOption.add(i);
        }
        gridOption = new JComboBox<>(gridsOption);

        // Add action listener to combo box to re-render blackboard
        gridOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer selectedValue = (Integer) gridOption.getSelectedItem();
                blackboard.reset(selectedValue);
            }
        });

        add(start);
        add(end);
        add(reset);
        add(gridOption);
    }
    // This method allows Main to register itself
    public void addListener(ActionListener listener) {
        gridOption.addActionListener(listener);
    }

    public void addStartListener(ActionListener listener) {
        start.addActionListener(listener);
    }

    public void addResetListener(ActionListener listener) {
        reset.addActionListener(listener);
    }

    public void addEndListener(ActionListener listener) {
        end.addActionListener(listener);
    }
}