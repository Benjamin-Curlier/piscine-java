package piscine.jeux;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * FOURNI — ne pas modifier. Rendu jouable de votre {@link JeuBomberman}.
 *
 * <p>Flèches = se déplacer, <strong>Espace</strong> = poser une bombe. Lancez avec
 * <pre>java piscine.jeux.BombermanSwing</pre> une fois {@code deplacer} et {@code tick} écrits.</p>
 */
public final class BombermanSwing extends JPanel {

    private static final int CASE = 40;
    private static final int COLS = 13;
    private static final int LIGNES = 11;
    private static final int PERIODE_MS = 140;

    private final transient JeuBomberman jeu;

    private BombermanSwing() {
        jeu = new JeuBomberman(plateauInitial(), new Position(1, 1));
        setPreferredSize(new Dimension(COLS * CASE, LIGNES * CASE));
        setBackground(new Color(30, 30, 36));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> jeu.deplacer(Direction.HAUT);
                    case KeyEvent.VK_DOWN -> jeu.deplacer(Direction.BAS);
                    case KeyEvent.VK_LEFT -> jeu.deplacer(Direction.GAUCHE);
                    case KeyEvent.VK_RIGHT -> jeu.deplacer(Direction.DROITE);
                    case KeyEvent.VK_SPACE -> jeu.poserBombe(3, 2);
                    default -> { /* ignorée */ }
                }
                repaint();
            }
        });
        new Timer(PERIODE_MS, e -> {
            jeu.tick();
            repaint();
        }).start();
    }

    private static Plateau plateauInitial() {
        String[] carte = new String[LIGNES];
        for (int l = 0; l < LIGNES; l++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < COLS; c++) {
                boolean bord = l == 0 || l == LIGNES - 1 || c == 0 || c == COLS - 1;
                boolean pilier = l % 2 == 0 && c % 2 == 0;
                boolean depart = (l <= 2 && c <= 2);
                if (bord || pilier) {
                    sb.append('#');
                } else if (!depart && (l + c) % 3 == 0) {
                    sb.append('o');
                } else {
                    sb.append('.');
                }
            }
            carte[l] = sb.toString();
        }
        return Plateau.depuis(carte);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Plateau p = jeu.plateau();
        for (int l = 0; l < p.hauteur(); l++) {
            for (int c = 0; c < p.largeur(); c++) {
                Case cs = p.caseEn(new Position(l, c));
                g2.setColor(switch (cs) {
                    case MUR -> new Color(90, 90, 102);
                    case CAISSE -> new Color(150, 100, 60);
                    default -> new Color(45, 45, 54);
                });
                g2.fillRect(c * CASE, l * CASE, CASE - 1, CASE - 1);
            }
        }
        for (Bombe b : jeu.bombes()) {
            g2.setColor(Color.BLACK);
            g2.fillOval(b.position().colonne() * CASE + 8, b.position().ligne() * CASE + 8, CASE - 16, CASE - 16);
            g2.setColor(Color.ORANGE);
            g2.drawString(String.valueOf(b.compteur()),
                b.position().colonne() * CASE + CASE / 2 - 3, b.position().ligne() * CASE + CASE / 2 + 4);
        }
        Position j = jeu.positionJoueur();
        g2.setColor(jeu.joueurVivant() ? new Color(120, 200, 255) : Color.DARK_GRAY);
        g2.fillRoundRect(j.colonne() * CASE + 6, j.ligne() * CASE + 6, CASE - 12, CASE - 12, 10, 10);
        if (!jeu.joueurVivant()) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 40));
            g2.drawString("GAME OVER", getWidth() / 2 - 120, getHeight() / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Bomberman — Piscine Java");
            BombermanSwing panneau = new BombermanSwing();
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
