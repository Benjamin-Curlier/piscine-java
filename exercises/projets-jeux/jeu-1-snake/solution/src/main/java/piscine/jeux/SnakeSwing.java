package piscine.jeux;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * FOURNI — ne pas modifier. Rendu jouable de votre {@link JeuSnake}.
 *
 * <p>Une fois {@code avancer()} et {@code changerDirection(...)} implémentés, lancez
 * <pre>java piscine.jeux.SnakeSwing</pre> et jouez avec les flèches du clavier.</p>
 */
public final class SnakeSwing extends JPanel {

    private static final int CASE = 26;
    private static final int COLS = 24;
    private static final int LIGNES = 18;
    private static final int PERIODE_MS = 110;

    private final transient JeuSnake jeu;
    private final transient Random rng = new Random();

    private SnakeSwing() {
        jeu = new JeuSnake(COLS, LIGNES, new Position(LIGNES / 2, COLS / 2),
                Direction.DROITE, this::pommeAleatoire);
        setPreferredSize(new Dimension(COLS * CASE, LIGNES * CASE));
        setBackground(new Color(18, 18, 24));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> jeu.changerDirection(Direction.HAUT);
                    case KeyEvent.VK_DOWN -> jeu.changerDirection(Direction.BAS);
                    case KeyEvent.VK_LEFT -> jeu.changerDirection(Direction.GAUCHE);
                    case KeyEvent.VK_RIGHT -> jeu.changerDirection(Direction.DROITE);
                    default -> { /* touche ignorée */ }
                }
            }
        });
        new Timer(PERIODE_MS, e -> {
            jeu.avancer();
            repaint();
        }).start();
    }

    private Position pommeAleatoire() {
        return new Position(rng.nextInt(LIGNES), rng.nextInt(COLS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Position pomme = jeu.pomme();
        g2.setColor(new Color(225, 70, 80));
        g2.fillRoundRect(pomme.colonne() * CASE + 4, pomme.ligne() * CASE + 4, CASE - 8, CASE - 8, 10, 10);

        List<Position> corps = jeu.corps();
        for (int i = 0; i < corps.size(); i++) {
            Position p = corps.get(i);
            g2.setColor(i == 0 ? new Color(130, 235, 150) : new Color(70, 175, 100));
            g2.fillRoundRect(p.colonne() * CASE + 1, p.ligne() * CASE + 1, CASE - 2, CASE - 2, 7, 7);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("Score : " + jeu.score(), 10, 20);

        if (jeu.estTermine()) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 44));
            String t = "GAME OVER";
            int w = g2.getFontMetrics().stringWidth(t);
            g2.drawString(t, (getWidth() - w) / 2, getHeight() / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Snake — Piscine Java");
            SnakeSwing panneau = new SnakeSwing();
            f.add(panneau);
            f.pack();
            f.setResizable(false);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            panneau.requestFocusInWindow();
        });
    }
}
