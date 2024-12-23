import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleScreen extends JPanel {
    private JLabel battleLabel;
    private JButton endBattleButton;
    private Game game;

    public BattleScreen(Game game) {
        this.game = game;
        setLayout(new BorderLayout());

        battleLabel = new JLabel("A wild Pokémon appeared!", SwingConstants.CENTER);
        battleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(battleLabel, BorderLayout.CENTER);

        endBattleButton = new JButton("End Battle");
        endBattleButton.setFont(new Font("Serif", Font.BOLD, 20));
        endBattleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endBattle();
            }
        });
        add(endBattleButton, BorderLayout.SOUTH);
    }

    private void endBattle() {
        game.endBattle();
    }
}
