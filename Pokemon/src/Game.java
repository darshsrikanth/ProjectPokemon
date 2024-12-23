import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Game extends JFrame implements KeyListener {
    private Landscape landscape;
    private Player player;
    private JPanel gamePanel;
    private JLabel[][] grid;
    private Random random = new Random();

    private ImageIcon fieldIcon;
    private ImageIcon grassIcon;
    private ImageIcon spriteUpIcon;
    private ImageIcon spriteDownIcon;
    private ImageIcon spriteLeftIcon;
    private ImageIcon spriteRightIcon;

    private Clip backgroundMusicClip;
    private Clip battleMusicClip;

    private Pokemon[] playerPokemons;
    private Pokemon[] opponentPokemons;

    private boolean isBattleInProgress = false; // Flag to track if a battle is in progress

    public Game(String filePath) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        landscape = new Landscape(filePath);
        int startRow = 0, startCol = 0;

        for (int i = 0; i < landscape.getRows(); i++) {
            for (int j = 0; j < landscape.getCols(); j++) {
                if (landscape.getTile(i, j) == 'P') {
                    startRow = i;
                    startCol = j;
                    landscape.setTile(i, j, '.'); 
                    break;
                }
            }
        }
        player = new Player(startRow, startCol);

        fieldIcon = loadImageIcon("Field.png", 100, 100);
        grassIcon = loadImageIcon("Grass.png", 100, 100);
        spriteUpIcon = loadImageIcon("SpriteUp.png", 100, 100);
        spriteDownIcon = loadImageIcon("SpriteDown.png", 100, 100);
        spriteLeftIcon = loadImageIcon("SpriteLeft.png", 100, 100);
        spriteRightIcon = loadImageIcon("SpriteRight.png", 100, 100);

        setTitle("Pokemon Adventure");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gamePanel = new JPanel(new GridLayout(landscape.getRows(), landscape.getCols()));
        grid = new JLabel[landscape.getRows()][landscape.getCols()];

        for (int i = 0; i < landscape.getRows(); i++) {
            for (int j = 0; j < landscape.getCols(); j++) {
                grid[i][j] = new JLabel();
                grid[i][j].setOpaque(true);  // Make sure the label is opaque to display the background
                grid[i][j].setHorizontalAlignment(JLabel.CENTER);
                grid[i][j].setVerticalAlignment(JLabel.CENTER);
                grid[i][j].setPreferredSize(new Dimension(100, 100));
                gamePanel.add(grid[i][j]);
            }
        }
        add(gamePanel, BorderLayout.CENTER);
        addKeyListener(this);
        setFocusable(true);
        displayLandscape();

        // Load and play background music
        playBackgroundMusic("MainMusic.wav");

        // Initialize the Pokémon pools
        initializePokemonPools();
    }

    private void initializePokemonPools() {
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

        playerPokemons = new Pokemon[]{
            new Pokemon("Squirtle", "Water", 200, 200, moves1, "SquirtleSprite.png", 50),
            new Pokemon("Pikachu", "Electric", 200, 200, moves2, "PikachuSprite.png", 75),
            new Pokemon("Charmander", "Fire", 200, 200, moves3, "CharmanderSprite.png", 60)
        };

        opponentPokemons = new Pokemon[]{
            new Pokemon("Squirtle", "Water", 200, 200, moves1, "SquirtleFrontSprite.png", 50),
            new Pokemon("Pikachu", "Electric", 200, 200, moves2, "PikachuFrontSprite.png", 75),
            new Pokemon("Charmander", "Fire", 200, 200, moves3, "CharmanderFrontSprite.png", 60)
        };
    }

    private ImageIcon loadImageIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void displayLandscape() {
        for (int i = 0; i < landscape.getRows(); i++) {
            for (int j = 0; j < landscape.getCols(); j++) {
                char tile = landscape.getTile(i, j);
                if (tile == '#') {
                    grid[i][j].setBackground(Color.GRAY);
                    grid[i][j].setIcon(null);
                } else if (tile == '.') {
                    grid[i][j].setIcon(fieldIcon);
                } else if (tile == 'G') {
                    grid[i][j].setIcon(grassIcon);
                }
            }
        }

        ImageIcon playerIcon = player.getDirection().equals("up") ? spriteUpIcon : player.getDirection().equals("down") ? spriteDownIcon :
                              player.getDirection().equals("left") ? spriteLeftIcon : spriteRightIcon;

        JLabel playerLabel = grid[player.getRow()][player.getCol()];
        playerLabel.setIcon(overlayIcons(fieldIcon, playerIcon)); 
    }

    private ImageIcon overlayIcons(ImageIcon backgroundIcon, ImageIcon overlayIcon) {
        if (backgroundIcon == null || overlayIcon == null) {
            return null;
        }

        Image background = backgroundIcon.getImage();
        Image overlay = overlayIcon.getImage();

        int width = Math.max(background.getWidth(null), overlay.getWidth(null));
        int height = Math.max(background.getHeight(null), overlay.getHeight(null));

        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combined.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.drawImage(overlay, 0, 0, null);
        return new ImageIcon(combined);
    }

    private void playBackgroundMusic(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        stopBackgroundMusic();
        URL musicURL = getClass().getClassLoader().getResource(filepath);
        if (musicURL != null) {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicURL);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioInputStream);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Couldn't find music file: " + filepath);
        }
    }

    private void playBattleMusic(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        stopBackgroundMusic();
        URL musicURL = getClass().getClassLoader().getResource(filepath);
        if (musicURL != null) {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicURL);
            battleMusicClip = AudioSystem.getClip();
            battleMusicClip.open(audioInputStream);
            battleMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Couldn't find music file: " + filepath);
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }

    private void stopBattleMusic() {
        if (battleMusicClip != null && battleMusicClip.isRunning()) {
            battleMusicClip.stop();
            battleMusicClip.close();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char direction = ' ';

        switch (keyCode) {
            case KeyEvent.VK_W: direction = 'W'; break;
            case KeyEvent.VK_S: direction = 'S'; break;
            case KeyEvent.VK_A: direction = 'A'; break;
            case KeyEvent.VK_D: direction = 'D'; break;
            case KeyEvent.VK_Q: System.exit(0); break;
        }

        if (direction != ' ' && !isBattleInProgress) {  
            player.move(direction, landscape);
            if (landscape.getTile(player.getRow(), player.getCol()) == 'G') {
                if (random.nextInt(100) < 20) { // 20% chance to initiate a battle
                    try {
                        playBattleMusic("BattleMusic.wav");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    initiateBattle();
                }
            }
            displayLandscape();
        }
    }

    private void initiateBattle() {
        isBattleInProgress = true; 
        setGamePanelEnabled(false);

        Pokemon playerPokemon = playerPokemons[random.nextInt(playerPokemons.length)];
        Pokemon wildPokemon = opponentPokemons[random.nextInt(opponentPokemons.length)];

        BattleSimulator battle = new BattleSimulator();
        GUI battleGUI = new GUI(playerPokemon, wildPokemon, battle, this);

        battleGUI.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                endBattle(battleGUI);
            }
        });

        revalidate();
        repaint();
    }

    public void endBattle(GUI battleGUI) {
        stopBattleMusic();
        JDialog resultDialog = new JDialog(this, "Battle Result", true);
        resultDialog.setLayout(new FlowLayout());
        JLabel resultLabel = new JLabel(battleGUI.getBattleResult());
        JButton finishButton = new JButton("Finish");

        finishButton.addActionListener(e -> {
            resultDialog.dispose();
            isBattleInProgress = false; 
            playBackgroundMusicOnReturn();
            System.exit(0);  // Close the game when Finish is clicked
        });

        resultDialog.addWindowListener(new WindowAdapter() {  // Handle window close (X) button
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);  // Close the game when the window is closed
            }
        });

        resultDialog.add(resultLabel);
        resultDialog.add(finishButton);
        resultDialog.setSize(300, 150);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setVisible(true);
    }

    private void playBackgroundMusicOnReturn() {
        try {
            playBackgroundMusic("MainMusic.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setGamePanelEnabled(boolean enabled) {
        for (Component comp : gamePanel.getComponents()) {
            comp.setEnabled(enabled);
        }
        gamePanel.setBackground(enabled ? null : Color.GRAY);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        try {
            Game game = new Game("landscape.txt");
            game.setVisible(true);
        } catch (FileNotFoundException e) {
            System.err.println("Landscape file not found.");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
