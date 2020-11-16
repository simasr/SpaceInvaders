import java.awt.*;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class SpaceInvaders {
    static JFrame gameWindow = new JFrame();
    static JLabel shooterLabel = new JLabel();
    static boolean isLeftDown = false;
    static boolean isRightDown = false;
    static ArrayList<JLabel> missiles = new ArrayList<JLabel>();
    static ArrayList<JLabel> enemies = new ArrayList<JLabel>();
    static ArrayList<JLabel> enemiesMissiles = new ArrayList<JLabel>();
    static BufferedImage missileSprite;
    static BufferedImage enemyMissileSprite;
    static BufferedImage shooterSprite;
    static BufferedImage enemySprite;

    public static void main(String[] args) {
        // Setting up game window
        gameWindow.setTitle("Space Invaders by Simonas Riska");
        gameWindow.setSize(800,600);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setLayout(null);
        gameWindow.getContentPane().setBackground(Color.BLACK);
        gameWindow.setResizable(false);
        try {
            // Setting up shooter sprite
            shooterSprite = ImageIO.read(new File("sprites/shooter_spriteFinal.png"));
            shooterLabel = new JLabel(new ImageIcon(shooterSprite));
            shooterLabel.setSize(50,50);
            shooterLabel.setLocation(375,500);

            // Setting up bullet sprite
            missileSprite = ImageIO.read(new File("sprites/missile_spriteFinal.png"));

            enemyMissileSprite = ImageIO.read(new File("sprites/enemy_missileFinal.png")); // Test

            // Setting up enemy sprite
            enemySprite = ImageIO.read(new File("sprites/enemy_spriteFinal.png"));
            
        } catch (Exception e) {
            System.out.println("Error! Image not found!");
        }
        // Adding the player into the game
        gameWindow.add(shooterLabel);
        gameWindow.addKeyListener(new Listener());
        gameWindow.setVisible(true);

        // Adding enemies into the game
        for (int i = 0; i < 6; i++) {
            JLabel enemy = new JLabel(new ImageIcon(enemySprite));
            enemy.setSize(50,50);
            enemy.setLocation(40 + (90 * i), 25);
            gameWindow.add(enemy);
            enemy.setVisible(true);
            enemies.add(enemy);
        }

        int enemyDirection = 1;

        // Enemy shooting missiles
        int delay = 3000;
        ActionListener enemyMissile = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JLabel enemyMissile = new JLabel(new ImageIcon(enemyMissileSprite));
                int randomEnemyIndex = (int) (Math.random() * ((enemies.size())));
                enemyMissile.setSize(20,29);
                enemyMissile.setLocation(enemies.get(randomEnemyIndex).getLocation().x + 15, enemies.get(randomEnemyIndex).getLocation().y+30);
                gameWindow.add(enemyMissile);
                // enemyMissile.setVisible(true);
                enemiesMissiles.add(enemyMissile);
            }
        };
        Timer timer = new Timer(delay, enemyMissile);
        timer.start();


        while (true){
            // Enemy shooting
            ArrayList<JLabel> enemyMissilesOutOfBounds = new ArrayList<JLabel>();
            for (int i = 0; i < enemiesMissiles.size(); i++) {
                int missileLocationX = enemiesMissiles.get(i).getLocation().x;
                int missileLocationY = enemiesMissiles.get(i).getLocation().y;
                int playerLocationX = shooterLabel.getLocation().x;
                int playerLocationY = shooterLabel.getLocation().y;
                if (missileLocationX >= playerLocationX && missileLocationX <= playerLocationX + 50 && missileLocationY >= playerLocationY && missileLocationY <= playerLocationY + 50) {
                    gameWindow.setVisible(false);
                    gameWindow.dispose();
                    return;
                }
                if (missileLocationY > 519) {
                    enemyMissilesOutOfBounds.add(enemiesMissiles.get(i));
                }
                enemiesMissiles.get(i).setLocation(missileLocationX, missileLocationY + 5);
                enemiesMissiles.get(i).setVisible(true);
            }


            int currentX = shooterLabel.getLocation().x;
            int currentY = shooterLabel.getLocation().y;
            if (isLeftDown) {
                if (currentX - 3 > 0){
                    shooterLabel.setLocation(currentX-3,currentY);
                    shooterLabel.setVisible(true);
                }
            }
            if (isRightDown) {
                if (currentX + 3 < gameWindow.getWidth() - 60) {
                    shooterLabel.setLocation(currentX + 3, currentY);
                    shooterLabel.setVisible(true);
                }
            }

            // Player shooting
            ArrayList<JLabel> missilesOutOfBounds = new ArrayList<JLabel>();
            for (int i = 0; i < missiles.size(); i++){
                int missileLocationX = missiles.get(i).getLocation().x;
                int missileLocationY = missiles.get(i).getLocation().y;
                if (missileLocationY < -14) {
                    missilesOutOfBounds.add(missiles.get(i));
                }
                missiles.get(i).setLocation(missileLocationX, missileLocationY-10);
                missiles.get(i).setVisible(true);
            }

            if (enemyDirection == 1 && enemies.get(enemies.size()-1).getLocation().x > 730) {
                for (int j = 0; j < enemies.size(); j++) {
                    int enemyLocationX = enemies.get(j).getLocation().x;
                    int enemyLocationY = enemies.get(j).getLocation().y;
                    enemies.get(j).setLocation(enemyLocationX,enemyLocationY + 20);
                }
                enemyDirection = -1;
            } else if (enemyDirection == -1 && enemies.get(0).getLocation().x < 10) {
                for (int j = 0; j < enemies.size(); j++) {
                    int enemyLocationX = enemies.get(j).getLocation().x;
                    int enemyLocationY = enemies.get(j).getLocation().y;
                    enemies.get(j).setLocation(enemyLocationX,enemyLocationY + 20);
                }
                enemyDirection = 1;
            } else if (enemyDirection == 1) {
                for (int j = 0; j < enemies.size(); j++) {
                    int enemyLocationX = enemies.get(j).getLocation().x;
                    int enemyLocationY = enemies.get(j).getLocation().y;
                    enemies.get(j).setLocation(enemyLocationX+1,enemyLocationY);
                }
            } else if (enemyDirection == -1) {
                for (int j = 0; j < enemies.size(); j++) {
                    int enemyLocationX = enemies.get(j).getLocation().x;
                    int enemyLocationY = enemies.get(j).getLocation().y;
                    enemies.get(j).setLocation(enemyLocationX - 1, enemyLocationY);
                }
            }

            // Priesai nebejudes jeigu pasieks Y koordinates kur yra zaidejas
            for (int i = 0; i < enemies.size(); i++){
                int enemyLocationX = enemies.get(i).getLocation().x;
                int enemyLocationY = enemies.get(i).getLocation().y;
                if (enemyLocationY >= currentY-50){
                    gameWindow.setVisible(false);
                    gameWindow.dispose();
                    return;
                }

            }




            ArrayList<JLabel> enemiesDefeated = new ArrayList<JLabel>();
            for (int u = 0; u < missiles.size(); u++) {
                int bulletLocationX = missiles.get(u).getLocation().x + 2;
                int bulletLocationY = missiles.get(u).getLocation().y + 2;
                for (int q = 0; q < enemies.size(); q++) {
                    int enemyLocationX = enemies.get(q).getLocation().x;
                    int enemyLocationY = enemies.get(q).getLocation().y;
                    if (bulletLocationX >= enemyLocationX && bulletLocationX <= enemyLocationX + 50 && bulletLocationY >= enemyLocationY && bulletLocationY <= enemyLocationY+50) {
                        missiles.get(u).setVisible(false);
                        missilesOutOfBounds.add(missiles.get(u));
                        enemies.get(q).setVisible(false);
                        enemiesDefeated.add(enemies.get(q));
                    }
                }
            }

            missiles.removeAll(enemyMissilesOutOfBounds);
            missiles.removeAll(missilesOutOfBounds);
            enemies.removeAll(enemiesDefeated);
            if (enemies.size() == 0) {
                gameWindow.setVisible(false);
                gameWindow.dispose();
                return;
            }



            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
            gameWindow.repaint();
        }
    }

    static class Listener implements KeyListener{

        public void keyTyped(KeyEvent e) {

        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT){
                isLeftDown = true;
            } else if (key == KeyEvent.VK_RIGHT){
                isRightDown = true;
            } else if (key == KeyEvent.VK_SPACE){
                int currentX = shooterLabel.getLocation().x;
                int currentY = shooterLabel.getLocation().y;

                JLabel missile = new JLabel(new ImageIcon(missileSprite));
                missile.setSize(10,24);
                missile.setLocation(currentX+20, currentY);
                gameWindow.add(missile);
                missiles.add(missile);
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT){
                isLeftDown = false;
            } else if (key == KeyEvent.VK_RIGHT){
                isRightDown = false;
            }
        }
    }

}