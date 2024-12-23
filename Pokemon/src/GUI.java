import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Random;

public class GUI implements ActionListener {

    private JLabel label = new JLabel("Choose a move...");
    private JFrame frame;
    private JPanel panel;
    private JPanel imagePanel;
    private JButton button, button2, button3, button4, healButton, runButton;
    private JLabel healthLabel1, healthLabel2;
    private JLabel statusLabel1, statusLabel2;
    private JProgressBar healthBar1, healthBar2;
    private JTextArea liveFeed;
    private Pokemon pokemon; // Player's Pokemon
    private Pokemon pokemon2; // Opponent's Pokemon
    private ImageIcon pk1Image;
    private ImageIcon pk2Image;
    private ImageIcon backgroundImage;
    private BattleSimulator battle;
    private boolean playerUsedHeal = false;
    private boolean opponentUsedHeal = false;
    private boolean playerProtectedLastTurn = false;
    private boolean opponentProtectedLastTurn = false;
    private boolean playerProtectedThisTurn = false;
    private boolean opponentProtectedThisTurn = false;
    private Game game;
    private String battleResult = "";

    public GUI(Pokemon pokemon1, Pokemon pokemon2, BattleSimulator battle, Game game) {
        this.pokemon = pokemon1;
        this.pokemon2 = pokemon2;
        this.battle = battle;
        this.game = game;

        frame = new JFrame();
        panel = new JPanel();
        imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        pk1Image = new ImageIcon(new ImageIcon(getClass().getResource(pokemon1.sprite)).getImage().getScaledInstance(278, 278, Image.SCALE_SMOOTH));
        pk2Image = new ImageIcon(new ImageIcon(getClass().getResource(pokemon2.sprite)).getImage().getScaledInstance(278, 278, Image.SCALE_SMOOTH));

        backgroundImage = new ImageIcon(getClass().getResource("PBackground.png"));

        JLabel displayImage1 = new JLabel(pk1Image);
        JLabel displayImage2 = new JLabel(pk2Image);

        displayImage1.setPreferredSize(new Dimension(278, 278));
        displayImage2.setPreferredSize(new Dimension(278, 278)); 

        healthLabel1 = new JLabel(pokemon1.name + " HP: ");
        healthBar1 = new JProgressBar(0, 200);
        healthBar1.setValue(200);
        statusLabel1 = new JLabel();

        healthLabel2 = new JLabel(pokemon2.name + " HP: ");
        healthBar2 = new JProgressBar(0, 200);
        healthBar2.setValue(200);
        statusLabel2 = new JLabel();

        button = new JButton(pokemon.moves[0].moveName);
        button2 = new JButton(pokemon.moves[1].moveName);
        button3 = new JButton(pokemon.moves[2].moveName);
        button4 = new JButton(pokemon.moves[3].moveName);
        healButton = new JButton("Heal");
        runButton = new JButton("Run");

        button.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        healButton.addActionListener(this);
        runButton.addActionListener(this);

        panel.setLayout(new GridLayout(7, 1, 5, 5));
        panel.setOpaque(false);

        panel.add(button);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(healButton);
        panel.add(runButton);
        panel.add(label);

        imagePanel.setLayout(null);
        displayImage1.setBounds(200, 460, 278, 278); 
        displayImage2.setBounds(800, 380, 278, 278); 

        healthLabel1.setBounds(200, 440, 150, 20);
        healthBar1.setBounds(350, 440, 200, 20);
        statusLabel1.setBounds(560, 440, 50, 20);

        healthLabel2.setBounds(800, 360, 150, 20);
        healthBar2.setBounds(950, 360, 200, 20);
        statusLabel2.setBounds(1160, 360, 50, 20);

        panel.setBounds(1150, 600, 300, 300);
        imagePanel.add(displayImage1);
        imagePanel.add(displayImage2);
        imagePanel.add(healthLabel1);
        imagePanel.add(healthBar1);
        imagePanel.add(statusLabel1);
        imagePanel.add(healthLabel2);
        imagePanel.add(healthBar2);
        imagePanel.add(statusLabel2);
        imagePanel.add(panel);

        liveFeed = new JTextArea();
        liveFeed.setBounds(100, 800, 910, 150); 
        liveFeed.setEditable(false);
        liveFeed.setLineWrap(true);
        liveFeed.setWrapStyleWord(true);
        liveFeed.setBackground(Color.LIGHT_GRAY);
        imagePanel.add(liveFeed);

        frame.add(imagePanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Pokemon Battle Simulation");
        frame.setSize(new Dimension(1500, 1000));
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (pokemon.hp <= 0 || pokemon2.hp <= 0) {
            return;
        }

        disableButtons();

        JButton pressedButton = (JButton) e.getSource();
        Move playerMove = null;

        if (pressedButton == healButton) {
            if (!playerUsedHeal) {
                healPokemon(pokemon);
                playerUsedHeal = true;
                liveFeed.append(pokemon.name + " used a heal item! Restored to full HP and cured status conditions.\n");
                Timer timer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        executeEnemyMove();
                        endTurn();
                    }
                });
                timer.setRepeats(false);
                timer.start();
                return;
            }
        } else if (pressedButton == runButton) {
            frame.dispose();
            game.endBattle(this);
            return;
        } else {
            for (Move move : pokemon.moves) {
                if (pressedButton.getText().equals(move.moveName)) {
                    if (move.pp <= 0) {
                        label.setText("No PP left for " + move.moveName + "!");
                        enableButtons();
                        return;
                    } else {
                        move.pp--;
                        playerMove = move;
                        break;
                    }
                }
            }
        }

        if (playerMove != null) {
            executePlayerMove(playerMove);
            if (pokemon2.hp > 0) {
                Timer timer = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        executeEnemyMove();
                        endTurn();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                battleResult = "You win!";
                showResultDialog();
            }
        } else {
            endTurn();
        }

        if (pokemon.hp <= 0 || pokemon2.hp <= 0) {
            battleResult = "You lose!";
            showResultDialog();
        }
    }

    private void executePlayerMove(Move move) {
        refreshLiveFeed();
        Random rand = new Random();
        if (move.moveName.equals("Protect")) {
            if (playerProtectedLastTurn) {
                liveFeed.append(pokemon.name + " used Protect, but it failed!\n");
                return;
            }
            playerProtectedThisTurn = true;
            liveFeed.append(pokemon.name + " used Protect!\n");
            return;
        }

        if (rand.nextDouble() <= move.acc) {
            double effectiveness = BattleSimulator.getEffectiveness(move.moveType, pokemon2.type);
            int damage = (int) (move.dmg * effectiveness);
            pokemon2.hp -= damage;
            pokemon2.hp = Math.max(pokemon2.hp, 0);
            healthBar2.setValue(pokemon2.hp);
            liveFeed.append(pokemon.name + " used " + move.moveName + "! It's " + effectiveness + "x effective! Opponent's " + pokemon2.name + " took " + damage + " damage.\n");
            applyStatusEffects(move, pokemon2, rand);
            updateStatusLabels();
            if (pokemon2.hp <= 0) {
                liveFeed.append("Opponent's " + pokemon2.name + " fainted! You win!\n");
            }
        } else {
            liveFeed.append(pokemon.name + " used " + move.moveName + " but it missed!\n");
        }
    }

    private void executeEnemyMove() {
        Random rand = new Random();
        Move enemyMove = pokemon2.moves[rand.nextInt(pokemon2.moves.length)];
        if (enemyMove.moveName.equals("Protect")) {
            if (opponentProtectedLastTurn) {
                liveFeed.append("Opponent's " + pokemon2.name + " used Protect, but it failed!\n");
                return;
            }
            opponentProtectedThisTurn = true;
            liveFeed.append("Opponent's " + pokemon2.name + " used Protect!\n");
        } else {
            if (rand.nextDouble() <= enemyMove.acc) {
                if (playerProtectedThisTurn) {
                    liveFeed.append("Opponent's " + pokemon2.name + " used " + enemyMove.moveName + ", but it was protected!\n");
                    return;
                }

                double effectiveness = BattleSimulator.getEffectiveness(enemyMove.moveType, pokemon.type);
                int damage = (int) (enemyMove.dmg * effectiveness);
                pokemon.hp -= damage;
                pokemon.hp = Math.max(pokemon.hp, 0);
                healthBar1.setValue(pokemon.hp);
                liveFeed.append("Opponent's " + pokemon2.name + " used " + enemyMove.moveName + "! It's " + effectiveness + "x effective! " + pokemon.name + " took " + damage + " damage.\n");
                applyStatusEffects(enemyMove, pokemon, rand);
                updateStatusLabels();
                if (pokemon.hp <= 0) {
                    liveFeed.append(pokemon.name + " fainted! You lose!\n");
                }
            } else {
                liveFeed.append("Opponent's " + pokemon2.name + " used " + enemyMove.moveName + " but it missed!\n");
            }
        }
    }

    private void applyStatusEffects(Move move, Pokemon target, Random rand) {
        if (move.burn > 0 && rand.nextInt(100) < move.burn) {
            target.isBurned = true;
            liveFeed.append(target.name + " was burned!\n");
        }
        if (move.par > 0 && rand.nextInt(100) < move.par) {
            target.isParalyzed = true;
            liveFeed.append(target.name + " was paralyzed!\n");
        }
        if (move.frz > 0 && rand.nextInt(100) < move.frz) {
            target.isFrozen = true;
            target.frozenTurns = rand.nextInt(3) + 1;
            liveFeed.append(target.name + " was frozen!\n");
        }
        if (move.slp > 0 && rand.nextInt(100) < move.slp) {
            target.isAsleep = true;
            target.sleepTurns = rand.nextInt(3) + 1;
            liveFeed.append(target.name + " fell asleep!\n");
        }
    }

    private void updateStatusLabels() {
        statusLabel1.setText(getStatusText(pokemon));
        statusLabel2.setText(getStatusText(pokemon2));
    }

    private String getStatusText(Pokemon p) {
        StringBuilder status = new StringBuilder();
        if (p.isBurned) status.append("B ");
        if (p.isParalyzed) status.append("P ");
        if (p.isFrozen) status.append("F ");
        if (p.isAsleep) status.append("S ");
        return status.toString().trim();
    }

    private void healPokemon(Pokemon p) {
        p.hp = p.maxHp;
        p.isBurned = false;
        p.isParalyzed = false;
        p.isFrozen = false;
        p.isAsleep = false;
        if (p == pokemon) {
            healthBar1.setValue(p.hp);
        } else {
            healthBar2.setValue(p.hp);
        }
        updateStatusLabels();
    }

    private void applyBurnDamage() {
        if (pokemon.isBurned) {
            int burnDamage = (int) (pokemon.maxHp * 0.06);
            pokemon.hp = Math.max(pokemon.hp - burnDamage, 0);
            healthBar1.setValue(pokemon.hp);
            liveFeed.append(pokemon.name + " is hurt by its burn and took " + burnDamage + " damage.\n");
        }
        if (pokemon2.isBurned) {
            int burnDamage = (int) (pokemon2.maxHp * 0.06);
            pokemon2.hp = Math.max(pokemon2.hp - burnDamage, 0);
            healthBar2.setValue(pokemon2.hp);
            liveFeed.append("Opponent's " + pokemon2.name + " is hurt by its burn and took " + burnDamage + " damage.\n");
        }
    }

    private void disableButtons() {
        button.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        healButton.setEnabled(false);
        runButton.setEnabled(false);
    }

    private void enableButtons() {
        button.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        healButton.setEnabled(true);
        runButton.setEnabled(true);
    }

    private void endTurn() {
        applyBurnDamage();
        playerProtectedLastTurn = playerProtectedThisTurn;
        opponentProtectedLastTurn = opponentProtectedThisTurn;
        playerProtectedThisTurn = false;
        opponentProtectedThisTurn = false;
        enableButtons();
    }

    private void refreshLiveFeed() {
        liveFeed.setText("");
    }

    public JFrame getFrame() {
        return frame;
    }

    public String getBattleResult() {
        return battleResult;
    }

    private void showResultDialog() {
        JDialog resultDialog = new JDialog(frame, "Battle Result", true);
        resultDialog.setLayout(new FlowLayout());
        JLabel resultLabel = new JLabel(battleResult);
        JButton finishButton = new JButton("Finish");

        finishButton.addActionListener(e -> {
            resultDialog.dispose();
            System.exit(0);
        });

        resultDialog.add(resultLabel);
        resultDialog.add(finishButton);
        resultDialog.setSize(300, 150);
        resultDialog.setLocationRelativeTo(frame);
        resultDialog.setVisible(true);
    }

    public static void main(String[] args) {
        BattleSimulator battle = new BattleSimulator();

        Move[] moves1 = {
            new Move("Tackle", "Normal", 35, 40, 1.0, 0, 0, 0, 0, 0),
            new Move("Rock Smash", "Fighting", 15, 75, 0.9, 0, 0, 0, 0, 0),
            new Move("Water Gun", "Water", 25, 40, 1.0, 0, 0, 0, 0, 0),
            new Move("Surf", "Water", 15, 90, 1.0, 0, 0, 0, 0, 0)
        };

        Move[] moves2 = {
            new Move("Thunderbolt", "Electric", 15, 90, 1.0, 0, 0, 10, 0, 0),
            new Move("Tackle", "Normal", 30, 40, 0.9, 0, 0, 0, 0, 0),
            new Move("Thunder", "Electric", 5, 110, 0.7, 0, 0, 10, 0, 0),
            new Move("Quick Attack", "Normal", 30, 40, 1.0, 1, 0, 0, 0, 0)
        };

        Move[] moves3 = {
            new Move("Scratch", "Normal", 35, 40, 1.0, 0, 0, 0, 0, 0),
            new Move("Ember", "Fire", 25, 40, 1.0, 0, 10, 0, 0, 0),
            new Move("Flamethrower", "Fire", 15, 90, 1.0, 0, 10, 0, 0, 0),
            new Move("Protect", "Normal", 10, 0, 1.0, 4, 0, 0, 0, 0)
        };

        Pokemon[] playerPokemons = {
            new Pokemon("Squirtle", "Water", 200, 200, moves1, "SquirtleSprite.png", 50),
            new Pokemon("Pikachu", "Electric", 200, 200, moves2, "PikachuSprite.png", 75),
            new Pokemon("Charmander", "Fire", 200, 200, moves3, "CharmanderSprite.png", 60)
        };

        Pokemon[] opponentPokemons = {
            new Pokemon("Squirtle", "Water", 200, 200, moves1, "SquirtleFrontSprite.png", 50),
            new Pokemon("Pikachu", "Electric", 200, 200, moves2, "PikachuFrontSprite.png", 75),
            new Pokemon("Charmander", "Fire", 200, 200, moves3, "CharmanderFrontSprite.png", 60)
        };

        Random rand = new Random();
        Pokemon playerPokemon = playerPokemons[rand.nextInt(playerPokemons.length)];
        Pokemon opponentPokemon = opponentPokemons[rand.nextInt(opponentPokemons.length)];

        new GUI(playerPokemon, opponentPokemon, battle, null);
    }
}
