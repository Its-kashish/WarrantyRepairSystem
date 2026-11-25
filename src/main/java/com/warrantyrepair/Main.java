package com.warrantyrepair;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

public class Main {
    // Lively Light Lavender & Pastel Palette
    static final Color BG_MAIN = new Color(0xF3, 0xF1, 0xFF);          // #F3F1FF Light lavender main background
    static final Color CARD_BG = new Color(0xFF, 0xFF, 0xFF);          // #FFFFFF Crisp white cards
    static final Color SIDEBAR_BG = new Color(0xE7, 0xE2, 0xFF);       // #E7E2FF Soft lavender sidebar
    static final Color SIDEBAR_HOVER = new Color(0xD8, 0xCF, 0xFF);
    static final Color SIDEBAR_BORDER = new Color(0xCF, 0xC5, 0xF7);
    static final Color SIDEBAR_TEXT = new Color(0x2B, 0x22, 0x57);

    static final Color PURPLE = new Color(0x75, 0x63, 0xD9);           // #7563D9 Primary purple button
    static final Color PURPLE_HOVER = new Color(0x5E, 0x4B, 0xC7);
    static final Color PURPLE_BORDER = new Color(0x62, 0x50, 0xC8);

    static final Color SEC_BG = new Color(0xDC, 0xE8, 0xFF);           // #DCE8FF Soft blue secondary button
    static final Color SEC_HOVER = new Color(0xC5, 0xD9, 0xFF);
    static final Color SEC_BORDER = new Color(0xB4, 0xCC, 0xFA);
    static final Color SEC_TEXT = new Color(0x1E, 0x33, 0x62);

    static final Color DANGER_BG = new Color(0xFF, 0xE3, 0xE8);        // Soft rose delete button
    static final Color DANGER_HOVER = new Color(0xFF, 0xCC, 0xD5);
    static final Color DANGER_BORDER = new Color(0xF4, 0xB4, 0xC0);
    static final Color DANGER_TEXT = new Color(0xA3, 0x20, 0x38);

    static final Color TBL_HEADER_BG = new Color(0xD8, 0xD0, 0xFF);    // #D8D0FF Lavender-purple table header
    static final Color TBL_HEADER_FG = new Color(0x23, 0x1B, 0x4D);
    static final Color TBL_ROW_ALT = new Color(0xF0, 0xF4, 0xFF);      // #F0F4FF Very light blue/lavender row
    static final Color TBL_SELECT = new Color(0xE3, 0xDC, 0xFF);       // #E3DCFF Soft purple selected row
    static final Color TBL_GRID = new Color(0xE6, 0xE2, 0xF8);

    static final Color BORDER_SUBTLE = new Color(0xDD, 0xD8, 0xEE);    // Subtle lavender-gray border
    static final Color INPUT_BG = new Color(0xFA, 0xF9, 0xFF);
    static final Color INK = new Color(0x22, 0x1D, 0x3C);
    static final Color TEXT_MUTED = new Color(0x62, 0x5B, 0x86);

    // Dashboard & Accent Pastel Cards (Soft Blue, Lavender, Mint, Peach)
    static final Color STAT_BLUE_BG = new Color(0xEA, 0xF2, 0xFF);
    static final Color STAT_BLUE_BORDER = new Color(0xBD, 0xD5, 0xFF);
    static final Color STAT_BLUE_ACCENT = new Color(0x2D, 0x5E, 0xCC);
    static final Color STAT_BLUE_PILL = new Color(0xD9, 0xE7, 0xFF);

    static final Color STAT_LAV_BG = new Color(0xEF, 0xEA, 0xFF);
    static final Color STAT_LAV_BORDER = new Color(0xCD, 0xBF, 0xFF);
    static final Color STAT_LAV_ACCENT = new Color(0x63, 0x48, 0xCC);
    static final Color STAT_LAV_PILL = new Color(0xE0, 0xD7, 0xFF);

    static final Color STAT_MINT_BG = new Color(0xE6, 0xF8, 0xEF);
    static final Color STAT_MINT_BORDER = new Color(0xB2, 0xE6, 0xCA);
    static final Color STAT_MINT_ACCENT = new Color(0x18, 0x7E, 0x52);
    static final Color STAT_MINT_PILL = new Color(0xCE, 0xF0, 0xDF);

    static final Color STAT_PEACH_BG = new Color(0xFF, 0xF0, 0xE4);
    static final Color STAT_PEACH_BORDER = new Color(0xFF, 0xCE, 0xAA);
    static final Color STAT_PEACH_ACCENT = new Color(0xC9, 0x5B, 0x1B);
    static final Color STAT_PEACH_PILL = new Color(0xFF, 0xDF, 0xC9);

    // Status Badges
    static final Color STATUS_GREEN_BG = new Color(0xDD, 0xF6, 0xE8);
    static final Color STATUS_GREEN_FG = new Color(0x17, 0x74, 0x46);
    static final Color STATUS_RED_BG = new Color(0xFF, 0xE2, 0xE6);
    static final Color STATUS_RED_FG = new Color(0xB3, 0x22, 0x35);
    static final Color STATUS_AMBER_BG = new Color(0xFF, 0xEF, 0xD6);
    static final Color STATUS_AMBER_FG = new Color(0xA6, 0x57, 0x10);
    static final Color STATUS_BLUE_BG = new Color(0xE0, 0xEA, 0xFF);
    static final Color STATUS_BLUE_FG = new Color(0x29, 0x4F, 0xB8);

    // Compatibility aliases
    static final Color DARK = SIDEBAR_BG;
    static final Color LAV = TBL_HEADER_BG;
    static final Color CREAM = BG_MAIN;

    static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 12);

    static final Map<String, String> DOT_ENV = loadDotEnv();
    static final String DB_URL = config("DB_URL", "WARRANTY_DB_URL", "jdbc:mysql://localhost:3306/warranty_repair_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    static final String DB_USER = config("DB_USER", "WARRANTY_DB_USER", "root");
    static final String DB_PASS = config("DB_PASSWORD", "WARRANTY_DB_PASSWORD", "");

    static Map<String, String> loadDotEnv() {
        Map<String, String> map = new HashMap<>();
        Path projectEnv = null;
        try {
            Path loc = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (loc.getParent() != null) projectEnv = loc.getParent().resolve(".env");
        } catch (Exception ignored) {}
        for (Path p : new Path[]{ Paths.get(".env"), Paths.get("..", ".env"), projectEnv }) {
            if (p != null && Files.isRegularFile(p)) {
                try {
                    for (String raw : Files.readAllLines(p, StandardCharsets.UTF_8)) {
                        String line = raw.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        int eq = line.indexOf('=');
                        if (eq <= 0) continue;
                        String k = line.substring(0, eq).trim();
                        String v = line.substring(eq + 1).trim();
                        if (v.length() >= 2 && ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'")))) {
                            v = v.substring(1, v.length() - 1);
                        }
                        map.put(k, v);
                    }
                    break;
                } catch (IOException ignored) {}
            }
        }
        return map;
    }

    static String config(String key, String altKey, String fallback) {
        if (DOT_ENV.containsKey(key)) return DOT_ENV.get(key);
        if (DOT_ENV.containsKey(altKey)) return DOT_ENV.get(altKey);
        String env = System.getenv(key);
        if (env != null) return env;
        String altEnv = System.getenv(altKey);
        return altEnv != null ? altEnv : fallback;
    }

    static Connection connect() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException e) { throw new SQLException("MySQL JDBC driver not found on classpath.", e); }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    static void info(Component c, String s) { JOptionPane.showMessageDialog(c, s, "Warranty & Repair System", JOptionPane.INFORMATION_MESSAGE); }
    static void error(Component c, Exception e) { JOptionPane.showMessageDialog(c, "Something went wrong:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }

    // Smooth rounded container panel
    static class RoundedPanel extends JPanel {
        private final int radius;
        private Color borderColor;
        private Color topAccent;

        RoundedPanel(int radius, Color bg, Color borderColor) {
            this(radius, bg, borderColor, null);
        }

        RoundedPanel(int radius, Color bg, Color borderColor, Color topAccent) {
            this.radius = radius;
            this.borderColor = borderColor;
            this.topAccent = topAccent;
            setBackground(bg);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, radius, radius);
            if (topAccent != null) {
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, radius, radius));
                g2.setColor(topAccent);
                g2.fillRect(0, 0, w + 1, 5);
                g2.setClip(null);
            }
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, w, h, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static DefaultTableModel model(String... cols) {
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    static JTable table(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(38);
        t.setFont(FONT_BODY);
        t.setForeground(INK);
        t.setBackground(CARD_BG);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setSelectionBackground(TBL_SELECT);
        t.setSelectionForeground(INK);
        t.setGridColor(TBL_GRID);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setFillsViewportHeight(true);

        JTableHeader header = t.getTableHeader();
        header.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 44));
        header.setBackground(TBL_HEADER_BG);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                l.setBackground(TBL_HEADER_BG);
                l.setForeground(TBL_HEADER_FG);
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                l.setOpaque(true);
                l.setHorizontalAlignment(SwingConstants.LEFT);
                l.setBorder(new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xBE, 0xB2, 0xF6)),
                        new EmptyBorder(0, 14, 0, 14)));
                return l;
            }
        });

        t.setDefaultRenderer(Object.class, alignedCellRenderer(SwingConstants.LEFT));
        return t;
    }

    static DefaultTableCellRenderer alignedCellRenderer(int align) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                l.setFont(FONT_BODY);
                l.setHorizontalAlignment(align);
                l.setOpaque(true);
                l.setBorder(new EmptyBorder(8, 14, 8, 14));
                if (sel) {
                    l.setBackground(TBL_SELECT);
                    l.setForeground(INK);
                } else {
                    l.setBackground(row % 2 == 0 ? CARD_BG : TBL_ROW_ALT);
                    l.setForeground(INK);
                }
                return l;
            }
        };
    }

    // Pill-style status renderer centered on top of the row's alternating/selected background
    static TableCellRenderer statusRenderer() {
        return (tbl, val, sel, foc, row, col) -> {
            Color rowBg = sel ? TBL_SELECT : (row % 2 == 0 ? CARD_BG : TBL_ROW_ALT);
            JPanel cell = new JPanel(new GridBagLayout());
            cell.setBackground(rowBg);
            cell.setOpaque(true);

            String text = val == null ? "" : val.toString().trim().toUpperCase();
            Color bg = STAT_LAV_BG, fg = INK, br = BORDER_SUBTLE;
            switch (text) {
                case "ACTIVE":
                case "COMPLETED":
                    bg = STATUS_GREEN_BG; fg = STATUS_GREEN_FG; br = STAT_MINT_BORDER;
                    break;
                case "EXPIRED":
                    bg = STATUS_RED_BG; fg = STATUS_RED_FG; br = DANGER_BORDER;
                    break;
                case "PENDING":
                    bg = STATUS_AMBER_BG; fg = STATUS_AMBER_FG; br = STAT_PEACH_BORDER;
                    break;
                case "IN_PROGRESS":
                    bg = STATUS_BLUE_BG; fg = STATUS_BLUE_FG; br = STAT_BLUE_BORDER;
                    break;
                default:
                    break;
            }

            RoundedPanel pill = new RoundedPanel(12, bg, br);
            pill.setLayout(new BorderLayout());
            pill.setBorder(new EmptyBorder(3, 10, 3, 10));
            JLabel lbl = new JLabel(text);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(fg);
            pill.add(lbl, BorderLayout.CENTER);
            cell.add(pill);
            return cell;
        };
    }

    static void setColWidths(JTable t, int... widths) {
        for (int i = 0; i < widths.length && i < t.getColumnModel().getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    static JScrollPane scrollTable(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new LineBorder(BORDER_SUBTLE, 1));
        sp.getViewport().setBackground(CARD_BG);
        sp.getVerticalScrollBar().setUI(lavenderScrollBarUI());
        sp.getHorizontalScrollBar().setUI(lavenderScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        JPanel corner = new JPanel();
        corner.setBackground(TBL_HEADER_BG);
        sp.setCorner(JScrollPane.UPPER_RIGHT_CORNER, corner);
        return sp;
    }

    static javax.swing.plaf.basic.BasicScrollBarUI lavenderScrollBarUI() {
        return new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0xC8, 0xBE, 0xF5);
                this.trackColor = BG_MAIN;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return zeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return zeroButton(); }
            private JButton zeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
        };
    }

    static JPanel panel() {
        JPanel p = new JPanel(new BorderLayout(18, 18));
        p.setBackground(BG_MAIN);
        p.setBorder(new EmptyBorder(24, 30, 24, 30));
        return p;
    }

    static JPanel cardPanel() {
        RoundedPanel p = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE);
        p.setBorder(new EmptyBorder(20, 22, 20, 22));
        return p;
    }

    static JPanel formCard(String sectionTitle, Color accentColor, JPanel fieldsGrid, JPanel actionsBar) {
        RoundedPanel card = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE, accentColor);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(new EmptyBorder(20, 24, 18, 24));

        JLabel titleLbl = new JLabel(sectionTitle);
        titleLbl.setFont(FONT_SECTION);
        titleLbl.setForeground(new Color(0x36, 0x2A, 0x74));
        titleLbl.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xEB, 0xE7, 0xFA)),
                new EmptyBorder(0, 0, 10, 0)));

        fieldsGrid.setOpaque(false);
        actionsBar.setOpaque(false);
        actionsBar.setBorder(new EmptyBorder(8, 0, 0, 0));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(fieldsGrid, BorderLayout.CENTER);
        card.add(actionsBar, BorderLayout.SOUTH);
        return card;
    }

    static JPanel formPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        return p;
    }

    static JPanel pageHeader(String heading, String subtitle, Color accentColor) {
        JPanel wrap = new JPanel(new BorderLayout(14, 0));
        wrap.setBackground(BG_MAIN);

        RoundedPanel bar = new RoundedPanel(6, accentColor, accentColor);
        bar.setPreferredSize(new Dimension(6, 46));
        wrap.add(bar, BorderLayout.WEST);

        JPanel h = new JPanel();
        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.setBackground(BG_MAIN);
        JLabel l = new JLabel(heading);
        l.setFont(FONT_TITLE);
        l.setForeground(INK);
        JLabel s = new JLabel(subtitle);
        s.setFont(FONT_SUBTITLE);
        s.setForeground(TEXT_MUTED);
        h.add(l);
        h.add(Box.createVerticalStrut(3));
        h.add(s);

        wrap.add(h, BorderLayout.CENTER);
        return wrap;
    }

    static JLabel statusPill(String text, Color bg, Color fg, Color border) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setForeground(fg);
        lbl.setBorder(new CompoundBorder(new LineBorder(border, 1), new EmptyBorder(7, 14, 7, 14)));
        return lbl;
    }

    // Aligned 2-column form field helper with uniform input sizing and trailing spacer
    static void addField(JPanel p, String label, Component field, int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = new Insets(8, 4, 8, 12);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(INK);
        lbl.setPreferredSize(new Dimension(175, 24));
        p.add(lbl, c);

        c.gridx = x + 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(8, 0, 8, 28);
        p.add(field, c);

        if (x == 2 && y == 0) {
            GridBagConstraints glue = new GridBagConstraints();
            glue.gridx = 4;
            glue.gridy = 0;
            glue.weightx = 1.0;
            glue.fill = GridBagConstraints.HORIZONTAL;
            p.add(Box.createHorizontalGlue(), glue);
        }
    }

    static JTextField field(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(FONT_BODY);
        f.setForeground(INK);
        f.setBackground(INPUT_BG);
        Dimension d = new Dimension(230, 36);
        f.setPreferredSize(d);
        f.setMinimumSize(new Dimension(160, 36));
        Border idleBorder = new CompoundBorder(new LineBorder(BORDER_SUBTLE, 1), new EmptyBorder(6, 12, 6, 12));
        Border focusBorder = new CompoundBorder(new LineBorder(PURPLE, 2), new EmptyBorder(5, 11, 5, 11));
        f.setBorder(idleBorder);
        f.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { f.setBorder(focusBorder); }
            @Override
            public void focusLost(FocusEvent e) { f.setBorder(idleBorder); }
        });
        return f;
    }

    static JTextField searchField() {
        JTextField f = field(16);
        f.setPreferredSize(new Dimension(210, 36));
        return f;
    }

    static void styleCombo(JComboBox<?> cb) {
        cb.setFont(FONT_BODY);
        cb.setForeground(INK);
        cb.setBackground(INPUT_BG);
        cb.setPreferredSize(new Dimension(230, 36));
        cb.setBorder(new LineBorder(BORDER_SUBTLE, 1));
        cb.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton("v");
                arrow.setFont(new Font("Segoe UI", Font.BOLD, 10));
                arrow.setForeground(TBL_HEADER_FG);
                arrow.setBackground(STAT_LAV_BG);
                arrow.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_SUBTLE));
                arrow.setFocusPainted(false);
                arrow.setOpaque(true);
                arrow.setContentAreaFilled(true);
                return arrow;
            }
        });
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setFont(FONT_BODY);
                l.setBorder(new EmptyBorder(6, 12, 6, 12));
                if (isSelected) {
                    l.setBackground(TBL_SELECT);
                    l.setForeground(INK);
                } else {
                    l.setBackground(CARD_BG);
                    l.setForeground(INK);
                }
                return l;
            }
        });
    }

    static class RoundedButton extends JButton {
        private final int radius;
        private Color borderColor;

        RoundedButton(String text, int radius, Color bg, Color fg, Color borderColor) {
            super(text);
            this.radius = radius;
            this.borderColor = borderColor;
            setBackground(bg);
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        void setBorderColor(Color c) {
            this.borderColor = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, radius, radius);
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, w, h, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static JButton button(String text) {
        return styledButton(text, PURPLE, PURPLE_HOVER, Color.WHITE, PURPLE_BORDER);
    }

    static JButton secondaryButton(String text) {
        return styledButton(text, SEC_BG, SEC_HOVER, SEC_TEXT, SEC_BORDER);
    }

    static JButton dangerButton(String text) {
        return styledButton(text, DANGER_BG, DANGER_HOVER, DANGER_TEXT, DANGER_BORDER);
    }

    static JButton styledButton(String text, Color bg, Color hover, Color fg, Color borderCol) {
        RoundedButton b = new RoundedButton(text, 10, bg, fg, borderCol);
        b.setFont(FONT_BTN);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        Dimension pref = b.getPreferredSize();
        b.setPreferredSize(new Dimension(Math.max(pref.width, 108), 36));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    static void title(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_TITLE);
        l.setForeground(INK);
        p.add(l, BorderLayout.NORTH);
    }

    static void refreshAll() { if (app != null) app.refreshPanels(); }
    static AppFrame app;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("Button.gradient", null);
                UIManager.put("OptionPane.background", CARD_BG);
                UIManager.put("OptionPane.messageForeground", INK);
                UIManager.put("OptionPane.messageFont", FONT_BODY);
                UIManager.put("OptionPane.buttonFont", FONT_BTN);
                UIManager.put("Button.background", SEC_BG);
                UIManager.put("Button.foreground", SEC_TEXT);
                UIManager.put("ComboBox.background", INPUT_BG);
                UIManager.put("ComboBox.selectionBackground", TBL_SELECT);
                UIManager.put("ComboBox.selectionForeground", INK);
            } catch (Exception ignored) {}
            app = new AppFrame();
            app.setVisible(true);
            app.toFront();
        });
    }

    static class AppFrame extends JFrame {
        CardLayout cards = new CardLayout();
        JPanel content = new JPanel(cards);
        Map<String, JButton> navButtons = new LinkedHashMap<>();
        String activeNav = "dashboard";

        Dashboard dash = new Dashboard();
        Customers customers = new Customers();
        Products products = new Products();
        Warranties warranties = new Warranties();
        Repairs repairs = new Repairs();
        History history = new History();

        AppFrame() {
            setTitle("Warranty & Repair History Management System");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(1280, 820);
            setMinimumSize(new Dimension(1080, 720));
            setLocationRelativeTo(null);
            getContentPane().setBackground(BG_MAIN);
            setLayout(new BorderLayout());

            JPanel sidebar = new JPanel(new BorderLayout());
            sidebar.setBackground(SIDEBAR_BG);
            sidebar.setPreferredSize(new Dimension(252, 0));
            sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SIDEBAR_BORDER));

            JPanel brandBox = new JPanel(new BorderLayout(12, 0));
            brandBox.setBackground(SIDEBAR_BG);
            brandBox.setBorder(new EmptyBorder(24, 18, 22, 18));

            RoundedPanel logoBadge = new RoundedPanel(12, PURPLE, PURPLE_BORDER);
            logoBadge.setLayout(new GridBagLayout());
            logoBadge.setPreferredSize(new Dimension(40, 40));
            JLabel logoText = new JLabel("WD");
            logoText.setFont(new Font("Segoe UI", Font.BOLD, 15));
            logoText.setForeground(Color.WHITE);
            logoBadge.add(logoText);

            JPanel brandText = new JPanel();
            brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));
            brandText.setBackground(SIDEBAR_BG);
            JLabel brand = new JLabel("Warranty Desk");
            brand.setForeground(new Color(0x2A, 0x1F, 0x64));
            brand.setFont(new Font("Segoe UI", Font.BOLD, 19));
            JLabel brandSub = new JLabel("Service & Repair Hub");
            brandSub.setForeground(new Color(0x63, 0x56, 0xA3));
            brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            brandText.add(brand);
            brandText.add(Box.createVerticalStrut(2));
            brandText.add(brandSub);

            brandBox.add(logoBadge, BorderLayout.WEST);
            brandBox.add(brandText, BorderLayout.CENTER);
            sidebar.add(brandBox, BorderLayout.NORTH);

            JPanel navWrap = new JPanel(new BorderLayout(0, 8));
            navWrap.setBackground(SIDEBAR_BG);
            navWrap.setBorder(new EmptyBorder(4, 14, 14, 14));

            JLabel menuLbl = new JLabel("NAVIGATION");
            menuLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            menuLbl.setForeground(new Color(0x6D, 0x60, 0xAA));
            menuLbl.setBorder(new EmptyBorder(0, 10, 4, 10));
            navWrap.add(menuLbl, BorderLayout.NORTH);

            JPanel navList = new JPanel();
            navList.setBackground(SIDEBAR_BG);
            navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));

            addNav(navList, "Dashboard", "dashboard");
            addNav(navList, "Customers", "customers");
            addNav(navList, "Products", "products");
            addNav(navList, "Warranty", "warranty");
            addNav(navList, "Repair Management", "repairs");
            addNav(navList, "Repair History", "history");
            navWrap.add(navList, BorderLayout.CENTER);
            sidebar.add(navWrap, BorderLayout.CENTER);

            JPanel sideFooter = new JPanel(new BorderLayout());
            sideFooter.setBackground(SIDEBAR_BG);
            sideFooter.setBorder(new EmptyBorder(14, 18, 20, 18));
            RoundedPanel envPill = new RoundedPanel(12, new Color(0xDC, 0xD4, 0xFF), SIDEBAR_BORDER);
            envPill.setLayout(new BorderLayout());
            envPill.setBorder(new EmptyBorder(8, 12, 8, 12));
            JLabel footLbl = new JLabel("MySQL 8  •  Java Swing");
            footLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            footLbl.setForeground(new Color(0x4B, 0x3D, 0x8F));
            envPill.add(footLbl, BorderLayout.CENTER);
            sideFooter.add(envPill, BorderLayout.CENTER);
            sidebar.add(sideFooter, BorderLayout.SOUTH);

            content.setBackground(BG_MAIN);
            content.add(dash, "dashboard");
            content.add(customers, "customers");
            content.add(products, "products");
            content.add(warranties, "warranty");
            content.add(repairs, "repairs");
            content.add(history, "history");

            add(sidebar, BorderLayout.WEST);
            add(content, BorderLayout.CENTER);
            cards.show(content, "dashboard");
            updateNavSelection("dashboard");

            addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    refreshPanels();
                    try (Connection c = connect()) {
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                                AppFrame.this,
                                "Unable to connect to MySQL database.\n\nError: " + ex.getMessage()
                                + "\n\nPlease ensure:\n1. MySQL Server is running on localhost:3306\n2. database/schema.sql has been executed\n3. A .env file exists in this folder with valid DB_USER and DB_PASSWORD",
                                "Database Connection Error",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }

        void addNav(JPanel side, String text, String key) {
            RoundedButton b = new RoundedButton(text, 10, SIDEBAR_BG, SIDEBAR_TEXT, null);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            b.setPreferredSize(new Dimension(220, 44));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setBorder(new EmptyBorder(10, 14, 10, 14));
            b.setHorizontalAlignment(SwingConstants.LEFT);

            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!key.equals(activeNav)) b.setBackground(SIDEBAR_HOVER);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!key.equals(activeNav)) b.setBackground(SIDEBAR_BG);
                }
            });

            b.addActionListener(e -> {
                cards.show(content, key);
                updateNavSelection(key);
                if (key.equals("dashboard")) dash.load();
                if (key.equals("customers")) customers.load();
                if (key.equals("products")) { products.loadCustomers(); products.load(); }
                if (key.equals("warranty")) warranties.load();
                if (key.equals("repairs")) { repairs.loadProducts(); repairs.load(); }
                if (key.equals("history")) history.reset();
            });
            navButtons.put(key, b);
            side.add(b);
            side.add(Box.createVerticalStrut(6));
        }

        void updateNavSelection(String key) {
            activeNav = key;
            for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
                RoundedButton btn = (RoundedButton) entry.getValue();
                if (entry.getKey().equals(key)) {
                    btn.setBackground(PURPLE);
                    btn.setForeground(Color.WHITE);
                    btn.setBorderColor(PURPLE_BORDER);
                } else {
                    btn.setBackground(SIDEBAR_BG);
                    btn.setForeground(SIDEBAR_TEXT);
                    btn.setBorderColor(null);
                }
            }
        }

        void refreshPanels() {
            dash.load();
            customers.load();
            products.loadCustomers();
            products.load();
            warranties.load();
            repairs.loadProducts();
            repairs.load();
            history.loadAll();
        }
    }

    static class Dashboard extends JPanel {
        JPanel stats = new JPanel(new GridLayout(1, 4, 18, 18));
        DefaultTableModel tm = model("Repair ID", "Product", "Serial Number", "Issue", "Repair Date", "Status");
        JTable recent = table(tm);
        JLabel connBadge = statusPill("Checking DB...", STAT_LAV_BG, STAT_LAV_ACCENT, STAT_LAV_BORDER);

        Dashboard() {
            setLayout(new BorderLayout(0, 22));
            setBackground(BG_MAIN);
            setBorder(new EmptyBorder(26, 32, 26, 32));

            JPanel topBar = new JPanel(new BorderLayout(16, 0));
            topBar.setBackground(BG_MAIN);
            topBar.add(pageHeader("Dashboard", "Overview of customers, registered products, warranty coverage, and repairs", PURPLE), BorderLayout.WEST);

            JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
            rightControls.setBackground(BG_MAIN);
            JButton btnRefresh = secondaryButton("Refresh Data");
            btnRefresh.addActionListener(e -> load());
            rightControls.add(connBadge);
            rightControls.add(btnRefresh);
            topBar.add(rightControls, BorderLayout.EAST);
            add(topBar, BorderLayout.NORTH);

            JPanel body = new JPanel(new BorderLayout(0, 24));
            body.setBackground(BG_MAIN);

            stats.setOpaque(false);
            stats.setPreferredSize(new Dimension(0, 162));
            body.add(stats, BorderLayout.NORTH);

            RoundedPanel recentCard = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE, PURPLE);
            recentCard.setLayout(new BorderLayout(0, 14));
            recentCard.setBorder(new EmptyBorder(20, 24, 20, 24));

            JPanel recentHeader = new JPanel(new BorderLayout());
            recentHeader.setOpaque(false);
            JLabel sub = new JLabel("Recent Repairs");
            sub.setFont(FONT_SECTION);
            sub.setForeground(new Color(0x36, 0x2A, 0x74));
            JLabel subHint = statusPill("Latest 8 Tickets", STAT_LAV_BG, STAT_LAV_ACCENT, STAT_LAV_BORDER);
            subHint.setBorder(new CompoundBorder(new LineBorder(STAT_LAV_BORDER, 1), new EmptyBorder(4, 12, 4, 12)));
            recentHeader.add(sub, BorderLayout.WEST);
            recentHeader.add(subHint, BorderLayout.EAST);

            recentCard.add(recentHeader, BorderLayout.NORTH);
            setColWidths(recent, 85, 165, 145, 290, 120, 125);
            recent.getColumnModel().getColumn(0).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            recent.getColumnModel().getColumn(4).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            recent.getColumnModel().getColumn(5).setCellRenderer(statusRenderer());
            recentCard.add(scrollTable(recent), BorderLayout.CENTER);

            body.add(recentCard, BorderLayout.CENTER);
            add(body, BorderLayout.CENTER);
            load();
        }

        void card(String name, String value, String tag, Color bg, Color borderCol, Color accent, Color pillBg, String detail) {
            RoundedPanel p = new RoundedPanel(16, bg, borderCol, accent);
            p.setLayout(new BorderLayout(0, 8));
            p.setBorder(new EmptyBorder(18, 20, 16, 20));

            JPanel topRow = new JPanel(new BorderLayout(8, 0));
            topRow.setOpaque(false);
            JLabel n = new JLabel(name);
            n.setFont(new Font("Segoe UI", Font.BOLD, 13));
            n.setForeground(INK);

            RoundedPanel iconPill = new RoundedPanel(10, pillBg, borderCol);
            iconPill.setLayout(new GridBagLayout());
            iconPill.setPreferredSize(new Dimension(34, 26));
            JLabel tagLbl = new JLabel(tag);
            tagLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            tagLbl.setForeground(accent);
            iconPill.add(tagLbl);

            topRow.add(n, BorderLayout.WEST);
            topRow.add(iconPill, BorderLayout.EAST);

            JLabel v = new JLabel(value);
            v.setFont(new Font("Segoe UI", Font.BOLD, 36));
            v.setForeground(accent);

            JLabel cap = new JLabel(detail);
            cap.setFont(new Font("Segoe UI", Font.BOLD, 11));
            cap.setForeground(TEXT_MUTED);

            p.add(topRow, BorderLayout.NORTH);
            p.add(v, BorderLayout.CENTER);
            p.add(cap, BorderLayout.SOUTH);
            stats.add(p);
        }

        void load() {
            stats.removeAll();
            try (Connection c = connect(); Statement s = c.createStatement()) {
                String totalCustomers = scalar(s, "SELECT COUNT(*) FROM customers");
                String totalProducts = scalar(s, "SELECT COUNT(*) FROM products");
                String activeWarranties = scalar(s, "SELECT COUNT(*) FROM products WHERE DATE_ADD(purchase_date, INTERVAL warranty_years YEAR) >= CURDATE()");
                String expiredWarranties = scalar(s, "SELECT COUNT(*) FROM products WHERE DATE_ADD(purchase_date, INTERVAL warranty_years YEAR) < CURDATE()");
                String totalRepairs = scalar(s, "SELECT COUNT(*) FROM repairs");
                String pendingRepairs = scalar(s, "SELECT COUNT(*) FROM repairs WHERE status='PENDING'");
                String completedRepairs = scalar(s, "SELECT COUNT(*) FROM repairs WHERE status='COMPLETED'");

                card("Customers", totalCustomers, "CU", STAT_BLUE_BG, STAT_BLUE_BORDER, STAT_BLUE_ACCENT, STAT_BLUE_PILL, "Registered profiles");
                card("Products", totalProducts, "PR", STAT_LAV_BG, STAT_LAV_BORDER, STAT_LAV_ACCENT, STAT_LAV_PILL, "Active: " + activeWarranties + "  |  Expired: " + expiredWarranties);
                card("Total Repairs", totalRepairs, "RP", STAT_MINT_BG, STAT_MINT_BORDER, STAT_MINT_ACCENT, STAT_MINT_PILL, "Completed: " + completedRepairs);
                card("Pending Repairs", pendingRepairs, "PD", STAT_PEACH_BG, STAT_PEACH_BORDER, STAT_PEACH_ACCENT, STAT_PEACH_PILL, "Awaiting service");

                connBadge.setText("MySQL Connected");
                connBadge.setBackground(STATUS_GREEN_BG);
                connBadge.setForeground(STATUS_GREEN_FG);
                connBadge.setBorder(new CompoundBorder(new LineBorder(STAT_MINT_BORDER, 1), new EmptyBorder(7, 14, 7, 14)));

                tm.setRowCount(0);
                try (ResultSet r = s.executeQuery("SELECT r.repair_id,p.product_name,p.serial_number,r.issue_description,r.repair_date,r.status FROM repairs r JOIN products p ON r.product_id=p.product_id ORDER BY r.repair_date DESC LIMIT 8")) {
                    while (r.next()) tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getDate(5), r.getString(6)});
                }
            } catch (Exception ex) {
                stats.removeAll();
                card("Database", "Offline", "DB", STAT_PEACH_BG, STAT_PEACH_BORDER, STATUS_RED_FG, STAT_PEACH_PILL, "Not connected");
                card("Setup", ".env", "CF", STAT_LAV_BG, STAT_LAV_BORDER, STAT_LAV_ACCENT, STAT_LAV_PILL, "Check credentials");
                card("Schema", "SQL", "SC", STAT_BLUE_BG, STAT_BLUE_BORDER, STAT_BLUE_ACCENT, STAT_BLUE_PILL, "Run database/schema.sql");
                card("Action", "Retry", "RT", STAT_MINT_BG, STAT_MINT_BORDER, TEXT_MUTED, STAT_MINT_PILL, "Click Refresh Data");

                connBadge.setText("MySQL Disconnected");
                connBadge.setBackground(STATUS_RED_BG);
                connBadge.setForeground(STATUS_RED_FG);
                connBadge.setBorder(new CompoundBorder(new LineBorder(DANGER_BORDER, 1), new EmptyBorder(7, 14, 7, 14)));
                tm.setRowCount(0);
            }
            stats.revalidate();
            stats.repaint();
        }

        String scalar(Statement s, String q) throws SQLException {
            try (ResultSet r = s.executeQuery(q)) {
                r.next();
                return r.getString(1);
            }
        }
    }

    static String strVal(Object o) { return o == null ? "" : o.toString(); }

    static class Customers extends JPanel {
        JTextField name = field(16), phone = field(16), email = field(16), address = field(16), search = searchField();
        DefaultTableModel tm = model("ID", "Name", "Phone", "Email", "Address");
        JTable t = table(tm);
        int selected = -1;

        Customers() {
            setLayout(new BorderLayout(0, 22));
            setBackground(BG_MAIN);
            setBorder(new EmptyBorder(26, 32, 26, 32));
            add(pageHeader("Customer Management", "Register, search, update, and manage service center customer profiles", STAT_BLUE_ACCENT), BorderLayout.NORTH);

            JPanel center = new JPanel(new BorderLayout(0, 22));
            center.setOpaque(false);

            JPanel f = formPanel();
            addField(f, "Name *", name, 0, 0);
            addField(f, "Phone (10 digits) *", phone, 2, 0);
            addField(f, "Email (Optional)", email, 0, 1);
            addField(f, "Address", address, 2, 1);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JButton save = button("Add Customer");
            JButton update = secondaryButton("Update Selected");
            JButton del = dangerButton("Delete Selected");
            JButton clear = secondaryButton("Clear");
            actions.add(save);
            actions.add(update);
            actions.add(del);
            actions.add(clear);
            center.add(formCard("Customer Details", STAT_BLUE_ACCENT, f, actions), BorderLayout.NORTH);

            RoundedPanel list = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE, PURPLE);
            list.setLayout(new BorderLayout(0, 14));
            list.setBorder(new EmptyBorder(20, 24, 20, 24));

            JPanel sr = new JPanel(new BorderLayout(12, 0));
            sr.setOpaque(false);
            JLabel tblLbl = new JLabel("Registered Customers");
            tblLbl.setFont(FONT_SECTION);
            tblLbl.setForeground(new Color(0x36, 0x2A, 0x74));
            sr.add(tblLbl, BorderLayout.WEST);

            JPanel srRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            srRight.setOpaque(false);
            JLabel srLbl = new JLabel("Search name/phone:");
            srLbl.setFont(FONT_LABEL);
            srLbl.setForeground(TEXT_MUTED);
            srRight.add(srLbl);
            srRight.add(search);
            JButton sb = button("Search"), all = secondaryButton("Show All");
            srRight.add(sb);
            srRight.add(all);
            sr.add(srRight, BorderLayout.EAST);

            list.add(sr, BorderLayout.NORTH);
            setColWidths(t, 70, 185, 135, 220, 270);
            t.getColumnModel().getColumn(0).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            list.add(scrollTable(t), BorderLayout.CENTER);
            center.add(list, BorderLayout.CENTER);
            add(center, BorderLayout.CENTER);

            save.addActionListener(e -> add());
            update.addActionListener(e -> update());
            del.addActionListener(e -> delete());
            clear.addActionListener(e -> clear());
            sb.addActionListener(e -> find());
            search.addActionListener(e -> find());
            all.addActionListener(e -> { search.setText(""); load(); });
            t.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int r = t.getSelectedRow();
                    if (r >= 0) {
                        selected = Integer.parseInt(tm.getValueAt(r, 0).toString());
                        name.setText(strVal(tm.getValueAt(r, 1)));
                        phone.setText(strVal(tm.getValueAt(r, 2)));
                        email.setText(strVal(tm.getValueAt(r, 3)));
                        address.setText(strVal(tm.getValueAt(r, 4)));
                    }
                }
            });
            load();
        }

        boolean validateInput() {
            if (name.getText().trim().isEmpty() || !phone.getText().trim().matches("\\d{10}")) {
                info(this, "Enter a customer name and a valid 10-digit phone number.");
                return false;
            }
            String em = email.getText().trim();
            if (!em.isEmpty() && !em.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                info(this, "Enter a valid email address or leave it blank.");
                return false;
            }
            return true;
        }

        void add() {
            if (!validateInput()) return;
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("INSERT INTO customers(name,phone,email,address) VALUES(?,?,?,?)")) {
                p.setString(1, name.getText().trim());
                p.setString(2, phone.getText().trim());
                p.setString(3, email.getText().trim());
                p.setString(4, address.getText().trim());
                p.executeUpdate();
                info(this, "Customer added.");
                clear();
                load();
                refreshAll();
            } catch (Exception ex) { error(this, ex); }
        }

        void update() {
            if (selected < 0) { info(this, "Select a customer row first."); return; }
            if (!validateInput()) return;
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("UPDATE customers SET name=?,phone=?,email=?,address=? WHERE customer_id=?")) {
                p.setString(1, name.getText().trim());
                p.setString(2, phone.getText().trim());
                p.setString(3, email.getText().trim());
                p.setString(4, address.getText().trim());
                p.setInt(5, selected);
                p.executeUpdate();
                info(this, "Customer updated.");
                clear();
                load();
                refreshAll();
            } catch (Exception ex) { error(this, ex); }
        }

        void delete() {
            if (selected < 0) { info(this, "Select a customer row first."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete selected customer? Products linked to them may prevent deletion.", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("DELETE FROM customers WHERE customer_id=?")) {
                p.setInt(1, selected);
                p.executeUpdate();
                info(this, "Customer deleted.");
                clear();
                load();
                refreshAll();
            } catch (SQLException ex) {
                if ("23000".equals(ex.getSQLState())) info(this, "Cannot delete this customer because products are registered under them.");
                else error(this, ex);
            } catch (Exception ex) { error(this, ex); }
        }

        void clear() { name.setText(""); phone.setText(""); email.setText(""); address.setText(""); selected = -1; t.clearSelection(); }

        void load() {
            tm.setRowCount(0);
            try (Connection c = connect(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT customer_id,name,phone,email,address FROM customers ORDER BY customer_id DESC")) {
                while (r.next()) tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5)});
            } catch (Exception ex) { if (isShowing()) error(this, ex); }
        }

        void find() {
            tm.setRowCount(0);
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT customer_id,name,phone,email,address FROM customers WHERE name LIKE ? OR phone LIKE ? ORDER BY customer_id DESC")) {
                String q = "%" + search.getText().trim() + "%";
                p.setString(1, q);
                p.setString(2, q);
                try (ResultSet r = p.executeQuery()) {
                    while (r.next()) tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5)});
                }
            } catch (Exception ex) { error(this, ex); }
        }
    }

    static class Products extends JPanel {
        JComboBox<CustomerItem> customer = new JComboBox<>();
        JTextField pname = field(16), brand = field(16), serial = field(16), purchase = field(16), years = field(16), search = searchField();
        DefaultTableModel tm = model("ID", "Customer", "Product", "Brand", "Serial", "Purchase Date", "Warranty Years");
        JTable t = table(tm);
        int selected = -1;

        Products() {
            setLayout(new BorderLayout(0, 22));
            setBackground(BG_MAIN);
            setBorder(new EmptyBorder(26, 32, 26, 32));
            add(pageHeader("Product Management", "Register products to customers with unique serial numbers and warranty terms", STAT_LAV_ACCENT), BorderLayout.NORTH);

            styleCombo(customer);

            JPanel center = new JPanel(new BorderLayout(0, 22));
            center.setOpaque(false);

            JPanel f = formPanel();
            addField(f, "Customer *", customer, 0, 0);
            addField(f, "Product Name *", pname, 2, 0);
            addField(f, "Brand", brand, 0, 1);
            addField(f, "Serial Number *", serial, 2, 1);
            addField(f, "Purchase Date (YYYY-MM-DD) *", purchase, 0, 2);
            addField(f, "Warranty Years *", years, 2, 2);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JButton add = button("Register Product");
            JButton upd = secondaryButton("Update Selected");
            JButton del = dangerButton("Delete Selected");
            JButton clr = secondaryButton("Clear");
            actions.add(add);
            actions.add(upd);
            actions.add(del);
            actions.add(clr);
            center.add(formCard("Product Registration", STAT_LAV_ACCENT, f, actions), BorderLayout.NORTH);

            RoundedPanel list = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE, PURPLE);
            list.setLayout(new BorderLayout(0, 14));
            list.setBorder(new EmptyBorder(20, 24, 20, 24));

            JPanel sr = new JPanel(new BorderLayout(12, 0));
            sr.setOpaque(false);
            JLabel tblLbl = new JLabel("Registered Products");
            tblLbl.setFont(FONT_SECTION);
            tblLbl.setForeground(new Color(0x36, 0x2A, 0x74));
            sr.add(tblLbl, BorderLayout.WEST);

            JPanel srRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            srRight.setOpaque(false);
            JLabel srLbl = new JLabel("Search product/serial:");
            srLbl.setFont(FONT_LABEL);
            srLbl.setForeground(TEXT_MUTED);
            srRight.add(srLbl);
            srRight.add(search);
            JButton sb = button("Search"), show = secondaryButton("Show All");
            srRight.add(sb);
            srRight.add(show);
            sr.add(srRight, BorderLayout.EAST);

            list.add(sr, BorderLayout.NORTH);
            setColWidths(t, 65, 155, 180, 110, 145, 120, 115);
            t.getColumnModel().getColumn(0).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(5).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(6).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            list.add(scrollTable(t), BorderLayout.CENTER);
            center.add(list, BorderLayout.CENTER);
            add(center, BorderLayout.CENTER);

            add.addActionListener(e -> insert());
            upd.addActionListener(e -> update());
            del.addActionListener(e -> delete());
            clr.addActionListener(e -> clear());
            sb.addActionListener(e -> find());
            search.addActionListener(e -> find());
            show.addActionListener(e -> { search.setText(""); load(); });
            t.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int r = t.getSelectedRow();
                    if (r >= 0) {
                        selected = Integer.parseInt(tm.getValueAt(r, 0).toString());
                        String customerName = strVal(tm.getValueAt(r, 1));
                        for (int i = 0; i < customer.getItemCount(); i++) {
                            if (customer.getItemAt(i).name.equals(customerName)) {
                                customer.setSelectedIndex(i);
                                break;
                            }
                        }
                        pname.setText(strVal(tm.getValueAt(r, 2)));
                        brand.setText(strVal(tm.getValueAt(r, 3)));
                        serial.setText(strVal(tm.getValueAt(r, 4)));
                        purchase.setText(strVal(tm.getValueAt(r, 5)));
                        years.setText(strVal(tm.getValueAt(r, 6)));
                    }
                }
            });
            load();
            loadCustomers();
        }

        void loadCustomers() {
            CustomerItem prev = (CustomerItem) customer.getSelectedItem();
            customer.removeAllItems();
            try (Connection c = connect(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT customer_id,name FROM customers ORDER BY name")) {
                while (r.next()) {
                    CustomerItem item = new CustomerItem(r.getInt(1), r.getString(2));
                    customer.addItem(item);
                    if (prev != null && prev.id == item.id) customer.setSelectedItem(item);
                }
            } catch (Exception ex) {}
        }

        boolean validateProductInput() {
            CustomerItem ci = (CustomerItem) customer.getSelectedItem();
            if (ci == null || pname.getText().trim().isEmpty() || serial.getText().trim().isEmpty()) {
                info(this, "Select a customer and enter product name and serial number.");
                return false;
            }
            try {
                LocalDate d = LocalDate.parse(purchase.getText().trim());
                int w = Integer.parseInt(years.getText().trim());
                if (d.isAfter(LocalDate.now()) || w <= 0) {
                    info(this, "Purchase date must not be future and warranty years must be positive.");
                    return false;
                }
            } catch (Exception ex) {
                info(this, "Enter a valid YYYY-MM-DD purchase date and a positive whole number for warranty years.");
                return false;
            }
            return true;
        }

        void insert() {
            if (!validateProductInput()) return;
            CustomerItem ci = (CustomerItem) customer.getSelectedItem();
            try {
                LocalDate d = LocalDate.parse(purchase.getText().trim());
                int w = Integer.parseInt(years.getText().trim());
                try (Connection c = connect(); PreparedStatement p = c.prepareStatement("INSERT INTO products(customer_id,product_name,brand,serial_number,purchase_date,warranty_years) VALUES(?,?,?,?,?,?)")) {
                    p.setInt(1, ci.id);
                    p.setString(2, pname.getText().trim());
                    p.setString(3, brand.getText().trim());
                    p.setString(4, serial.getText().trim());
                    p.setDate(5, Date.valueOf(d));
                    p.setInt(6, w);
                    p.executeUpdate();
                }
                info(this, "Product registered.");
                clear();
                load();
                refreshAll();
            } catch (SQLException ex) {
                if ("23000".equals(ex.getSQLState())) info(this, "A product with that serial number already exists.");
                else error(this, ex);
            } catch (Exception ex) { error(this, ex); }
        }

        void update() {
            if (selected < 0) { info(this, "Select a product row first."); return; }
            if (!validateProductInput()) return;
            CustomerItem ci = (CustomerItem) customer.getSelectedItem();
            try {
                LocalDate d = LocalDate.parse(purchase.getText().trim());
                int w = Integer.parseInt(years.getText().trim());
                try (Connection c = connect(); PreparedStatement p = c.prepareStatement("UPDATE products SET customer_id=?,product_name=?,brand=?,serial_number=?,purchase_date=?,warranty_years=? WHERE product_id=?")) {
                    p.setInt(1, ci.id);
                    p.setString(2, pname.getText().trim());
                    p.setString(3, brand.getText().trim());
                    p.setString(4, serial.getText().trim());
                    p.setDate(5, Date.valueOf(d));
                    p.setInt(6, w);
                    p.setInt(7, selected);
                    p.executeUpdate();
                }
                info(this, "Product updated.");
                clear();
                load();
                refreshAll();
            } catch (SQLException ex) {
                if ("23000".equals(ex.getSQLState())) info(this, "A product with that serial number already exists.");
                else error(this, ex);
            } catch (Exception ex) { error(this, ex); }
        }

        void delete() {
            if (selected < 0) { info(this, "Select a product row first."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete selected product? Existing repairs may prevent deletion.", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("DELETE FROM products WHERE product_id=?")) {
                p.setInt(1, selected);
                p.executeUpdate();
                info(this, "Product deleted.");
                clear();
                load();
                refreshAll();
            } catch (SQLException ex) {
                if ("23000".equals(ex.getSQLState())) info(this, "Cannot delete this product because repair records exist for it.");
                else error(this, ex);
            } catch (Exception ex) { error(this, ex); }
        }

        void clear() { pname.setText(""); brand.setText(""); serial.setText(""); purchase.setText(""); years.setText(""); selected = -1; t.clearSelection(); }

        void load() {
            tm.setRowCount(0);
            try (Connection c = connect(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT p.product_id,c.name,p.product_name,p.brand,p.serial_number,p.purchase_date,p.warranty_years FROM products p JOIN customers c ON c.customer_id=p.customer_id ORDER BY p.product_id DESC")) {
                while (r.next()) tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getDate(6), r.getInt(7)});
            } catch (Exception ex) {}
        }

        void find() {
            tm.setRowCount(0);
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT p.product_id,c.name,p.product_name,p.brand,p.serial_number,p.purchase_date,p.warranty_years FROM products p JOIN customers c ON c.customer_id=p.customer_id WHERE p.product_name LIKE ? OR p.serial_number LIKE ? ORDER BY p.product_id DESC")) {
                String q = "%" + search.getText().trim() + "%";
                p.setString(1, q);
                p.setString(2, q);
                try (ResultSet r = p.executeQuery()) {
                    while (r.next()) tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getDate(6), r.getInt(7)});
                }
            } catch (Exception ex) { error(this, ex); }
        }
    }

    static class CustomerItem {
        int id; String name;
        CustomerItem(int i, String n) { id = i; name = n; }
        public String toString() { return id + " - " + name; }
    }

    static class Warranties extends JPanel {
        DefaultTableModel tm = model("Product ID", "Customer", "Product", "Brand", "Serial", "Purchase Date", "Expiry Date", "Status");
        JTable t = table(tm);
        JTextField search = searchField();
        JLabel badgeShowing = statusPill("Total: 0", STAT_LAV_BG, STAT_LAV_ACCENT, STAT_LAV_BORDER);
        JLabel badgeActive = statusPill("Active: 0", STAT_MINT_BG, STAT_MINT_ACCENT, STAT_MINT_BORDER);
        JLabel badgeExpired = statusPill("Expired: 0", STAT_PEACH_BG, STAT_PEACH_ACCENT, STAT_PEACH_BORDER);

        Warranties() {
            setLayout(new BorderLayout(0, 22));
            setBackground(BG_MAIN);
            setBorder(new EmptyBorder(26, 32, 26, 32));

            JPanel headerRow = new JPanel(new BorderLayout(16, 0));
            headerRow.setBackground(BG_MAIN);
            headerRow.add(pageHeader("Warranty Tracking", "Automatically calculated warranty dates and ACTIVE / EXPIRED status", STAT_MINT_ACCENT), BorderLayout.WEST);

            JPanel pills = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
            pills.setBackground(BG_MAIN);
            pills.add(badgeShowing);
            pills.add(badgeActive);
            pills.add(badgeExpired);
            headerRow.add(pills, BorderLayout.EAST);
            add(headerRow, BorderLayout.NORTH);

            RoundedPanel card = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE, STAT_MINT_ACCENT);
            card.setLayout(new BorderLayout(0, 14));
            card.setBorder(new EmptyBorder(20, 24, 20, 24));

            JPanel top = new JPanel(new BorderLayout(12, 0));
            top.setOpaque(false);
            JLabel tblLbl = new JLabel("Product Warranties");
            tblLbl.setFont(FONT_SECTION);
            tblLbl.setForeground(new Color(0x36, 0x2A, 0x74));
            top.add(tblLbl, BorderLayout.WEST);

            JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            topRight.setOpaque(false);
            JLabel srLbl = new JLabel("Search serial/product/customer:");
            srLbl.setFont(FONT_LABEL);
            srLbl.setForeground(TEXT_MUTED);
            topRight.add(srLbl);
            topRight.add(search);
            JButton s = button("Search"), all = secondaryButton("Show All");
            topRight.add(s);
            topRight.add(all);
            top.add(topRight, BorderLayout.EAST);

            card.add(top, BorderLayout.NORTH);
            setColWidths(t, 80, 145, 165, 105, 135, 120, 120, 115);
            t.getColumnModel().getColumn(0).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(5).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(6).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(7).setCellRenderer(statusRenderer());
            card.add(scrollTable(t), BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);

            s.addActionListener(e -> load(search.getText().trim()));
            search.addActionListener(e -> load(search.getText().trim()));
            all.addActionListener(e -> { search.setText(""); load(""); });
            load("");
        }

        void load() { load(search.getText().trim()); }

        void load(String q) {
            tm.setRowCount(0);
            int total = 0, active = 0, expired = 0;
            String sql = "SELECT p.product_id,c.name,p.product_name,p.brand,p.serial_number,p.purchase_date,DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR) expiry,IF(CURDATE()<=DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR),'ACTIVE','EXPIRED') status FROM products p JOIN customers c ON c.customer_id=p.customer_id WHERE p.serial_number LIKE ? OR p.product_name LIKE ? OR c.name LIKE ? ORDER BY expiry";
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
                String like = "%" + q + "%";
                p.setString(1, like);
                p.setString(2, like);
                p.setString(3, like);
                try (ResultSet r = p.executeQuery()) {
                    while (r.next()) {
                        String st = r.getString(8);
                        total++;
                        if ("ACTIVE".equalsIgnoreCase(st)) active++;
                        else expired++;
                        tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getDate(6), r.getDate(7), st});
                    }
                }
            } catch (Exception ex) {}
            badgeShowing.setText("Total: " + total);
            badgeActive.setText("Active: " + active);
            badgeExpired.setText("Expired: " + expired);
        }
    }

    static class Repairs extends JPanel {
        JComboBox<ProductItem> product = new JComboBox<>();
        JTextField issue = field(16), date = field(16), cost = field(16), remarks = field(16), search = searchField();
        JComboBox<String> status = new JComboBox<>(new String[]{"PENDING", "IN_PROGRESS", "COMPLETED"});
        DefaultTableModel tm = model("Repair ID", "Product", "Serial", "Issue", "Date", "Cost", "Status", "Remarks");
        JTable t = table(tm);
        int selected = -1;

        Repairs() {
            setLayout(new BorderLayout(0, 22));
            setBackground(BG_MAIN);
            setBorder(new EmptyBorder(26, 32, 26, 32));
            add(pageHeader("Repair Management", "Register service requests, check warranty coverage, and update repair status and costs", STAT_PEACH_ACCENT), BorderLayout.NORTH);

            styleCombo(product);
            styleCombo(status);

            JPanel center = new JPanel(new BorderLayout(0, 22));
            center.setOpaque(false);

            JPanel f = formPanel();
            addField(f, "Product *", product, 0, 0);
            addField(f, "Issue Description *", issue, 2, 0);
            addField(f, "Repair Date (YYYY-MM-DD) *", date, 0, 1);
            addField(f, "Repair Cost", cost, 2, 1);
            addField(f, "Status *", status, 0, 2);
            addField(f, "Remarks", remarks, 2, 2);
            date.setText(LocalDate.now().toString());

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            JButton add = button("Register Repair");
            JButton upd = secondaryButton("Update Selected Status/Cost");
            JButton del = dangerButton("Delete Selected");
            JButton clr = secondaryButton("Clear");
            actions.add(add);
            actions.add(upd);
            actions.add(del);
            actions.add(clr);
            center.add(formCard("Repair Ticket Details", STAT_PEACH_ACCENT, f, actions), BorderLayout.NORTH);

            RoundedPanel list = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE, PURPLE);
            list.setLayout(new BorderLayout(0, 14));
            list.setBorder(new EmptyBorder(20, 24, 20, 24));

            JPanel sr = new JPanel(new BorderLayout(12, 0));
            sr.setOpaque(false);
            JLabel tblLbl = new JLabel("Repair Requests");
            tblLbl.setFont(FONT_SECTION);
            tblLbl.setForeground(new Color(0x36, 0x2A, 0x74));
            sr.add(tblLbl, BorderLayout.WEST);

            JPanel srRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            srRight.setOpaque(false);
            JLabel srLbl = new JLabel("Search serial:");
            srLbl.setFont(FONT_LABEL);
            srLbl.setForeground(TEXT_MUTED);
            srRight.add(srLbl);
            srRight.add(search);
            JButton sb = button("Search"), show = secondaryButton("Show All");
            srRight.add(sb);
            srRight.add(show);
            sr.add(srRight, BorderLayout.EAST);

            list.add(sr, BorderLayout.NORTH);
            setColWidths(t, 80, 140, 125, 210, 110, 90, 120, 190);
            t.getColumnModel().getColumn(0).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(4).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(5).setCellRenderer(alignedCellRenderer(SwingConstants.RIGHT));
            t.getColumnModel().getColumn(6).setCellRenderer(statusRenderer());
            list.add(scrollTable(t), BorderLayout.CENTER);
            center.add(list, BorderLayout.CENTER);
            add(center, BorderLayout.CENTER);

            add.addActionListener(e -> insert());
            upd.addActionListener(e -> update());
            del.addActionListener(e -> delete());
            clr.addActionListener(e -> clear());
            sb.addActionListener(e -> find());
            search.addActionListener(e -> find());
            show.addActionListener(e -> { search.setText(""); load(); });
            t.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int r = t.getSelectedRow();
                    if (r >= 0) {
                        selected = Integer.parseInt(tm.getValueAt(r, 0).toString());
                        String serialNo = strVal(tm.getValueAt(r, 2));
                        for (int i = 0; i < product.getItemCount(); i++) {
                            if (product.getItemAt(i).serial.equals(serialNo)) {
                                product.setSelectedIndex(i);
                                break;
                            }
                        }
                        issue.setText(strVal(tm.getValueAt(r, 3)));
                        date.setText(strVal(tm.getValueAt(r, 4)));
                        cost.setText(strVal(tm.getValueAt(r, 5)));
                        status.setSelectedItem(strVal(tm.getValueAt(r, 6)));
                        remarks.setText(strVal(tm.getValueAt(r, 7)));
                    }
                }
            });
            loadProducts();
            load();
        }

        void loadProducts() {
            ProductItem prev = (ProductItem) product.getSelectedItem();
            product.removeAllItems();
            try (Connection c = connect(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT product_id,product_name,serial_number FROM products ORDER BY product_name")) {
                while (r.next()) {
                    ProductItem item = new ProductItem(r.getInt(1), r.getString(2), r.getString(3));
                    product.addItem(item);
                    if (prev != null && prev.id == item.id) product.setSelectedItem(item);
                }
            } catch (Exception ex) {}
        }

        void insert() {
            ProductItem pi = (ProductItem) product.getSelectedItem();
            if (pi == null || issue.getText().trim().isEmpty()) { info(this, "Select a product and enter the repair issue."); return; }
            try {
                LocalDate d = LocalDate.parse(date.getText().trim());
                double co = Double.parseDouble(cost.getText().trim().isEmpty() ? "0" : cost.getText().trim());
                if (co < 0) throw new IllegalArgumentException("Cost cannot be negative.");
                try (Connection c = connect(); PreparedStatement p = c.prepareStatement("INSERT INTO repairs(product_id,issue_description,repair_date,repair_cost,status,remarks) VALUES(?,?,?,?,?,?)")) {
                    p.setInt(1, pi.id);
                    p.setString(2, issue.getText().trim());
                    p.setDate(3, Date.valueOf(d));
                    p.setDouble(4, co);
                    p.setString(5, status.getSelectedItem().toString());
                    p.setString(6, remarks.getText().trim());
                    p.executeUpdate();
                }
                String warranty;
                try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT IF(CURDATE()<=DATE_ADD(purchase_date,INTERVAL warranty_years YEAR),'Under Warranty','Out of Warranty'),DATE_ADD(purchase_date,INTERVAL warranty_years YEAR) FROM products WHERE product_id=?")) {
                    p.setInt(1, pi.id);
                    try (ResultSet r = p.executeQuery()) {
                        r.next();
                        warranty = r.getString(1) + " (expiry: " + r.getDate(2) + ")";
                    }
                }
                info(this, "Repair registered. Warranty check: " + warranty);
                clear();
                load();
                refreshAll();
            } catch (Exception ex) { error(this, ex); }
        }

        void update() {
            if (selected < 0) { info(this, "Select a repair row first."); return; }
            if (issue.getText().trim().isEmpty()) { info(this, "Enter the repair issue description."); return; }
            try {
                LocalDate d = LocalDate.parse(date.getText().trim());
                double co = Double.parseDouble(cost.getText().trim().isEmpty() ? "0" : cost.getText().trim());
                if (co < 0) throw new IllegalArgumentException("Cost cannot be negative.");
                try (Connection c = connect(); PreparedStatement p = c.prepareStatement("UPDATE repairs SET issue_description=?,repair_date=?,status=?,repair_cost=?,remarks=? WHERE repair_id=?")) {
                    p.setString(1, issue.getText().trim());
                    p.setDate(2, Date.valueOf(d));
                    p.setString(3, status.getSelectedItem().toString());
                    p.setDouble(4, co);
                    p.setString(5, remarks.getText().trim());
                    p.setInt(6, selected);
                    p.executeUpdate();
                }
                info(this, "Repair updated.");
                clear();
                load();
                refreshAll();
            } catch (Exception ex) { error(this, ex); }
        }

        void delete() {
            if (selected < 0) { info(this, "Select a repair row first."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete selected repair record?", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("DELETE FROM repairs WHERE repair_id=?")) {
                p.setInt(1, selected);
                p.executeUpdate();
                info(this, "Repair deleted.");
                clear();
                load();
                refreshAll();
            } catch (Exception ex) { error(this, ex); }
        }

        void clear() { issue.setText(""); date.setText(LocalDate.now().toString()); cost.setText(""); remarks.setText(""); status.setSelectedItem("PENDING"); selected = -1; t.clearSelection(); }

        void load() {
            tm.setRowCount(0);
            try (Connection c = connect(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT r.repair_id,p.product_name,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id ORDER BY r.repair_id DESC")) {
                while (r.next()) tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getDate(5), r.getBigDecimal(6), r.getString(7), r.getString(8)});
            } catch (Exception ex) {}
        }

        void find() {
            tm.setRowCount(0);
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT r.repair_id,p.product_name,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id WHERE p.serial_number LIKE ? ORDER BY r.repair_id DESC")) {
                p.setString(1, "%" + search.getText().trim() + "%");
                try (ResultSet r = p.executeQuery()) {
                    while (r.next()) tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getDate(5), r.getBigDecimal(6), r.getString(7), r.getString(8)});
                }
            } catch (Exception ex) { error(this, ex); }
        }
    }

    static class ProductItem {
        int id; String name, serial;
        ProductItem(int i, String n, String s) { id = i; name = n; serial = s; }
        public String toString() { return name + " (" + serial + ")"; }
    }

    static class History extends JPanel {
        JTextField serial = searchField();
        JLabel details = new JLabel("Enter a product serial number (or click a row below) to view its repair history.");
        JLabel summary = new JLabel(" ");
        DefaultTableModel tm = model("Repair ID", "Serial", "Issue", "Date", "Cost", "Status", "Remarks");
        JTable t = table(tm);

        History() {
            setLayout(new BorderLayout(0, 22));
            setBackground(BG_MAIN);
            setBorder(new EmptyBorder(26, 32, 26, 32));

            JPanel top = new JPanel(new BorderLayout(0, 18));
            top.setOpaque(false);
            top.add(pageHeader("Repair History", "Search by product serial number to inspect warranty status and complete service log", PURPLE), BorderLayout.NORTH);

            JPanel searchAndInfo = new JPanel(new BorderLayout(0, 16));
            searchAndInfo.setOpaque(false);

            RoundedPanel srCard = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE);
            srCard.setLayout(new BorderLayout(12, 0));
            srCard.setBorder(new EmptyBorder(16, 22, 16, 22));

            JPanel srLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            srLeft.setOpaque(false);
            JLabel srLbl = new JLabel("Product serial number:");
            srLbl.setFont(FONT_LABEL);
            srLbl.setForeground(INK);
            srLeft.add(srLbl);
            srLeft.add(serial);
            JButton s = button("Search"), all = secondaryButton("Show All");
            srLeft.add(s);
            srLeft.add(all);

            JLabel srHint = new JLabel("Click any row below to inspect that product");
            srHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            srHint.setForeground(TEXT_MUTED);
            srCard.add(srLeft, BorderLayout.WEST);
            srCard.add(srHint, BorderLayout.EAST);

            RoundedPanel info = new RoundedPanel(16, STAT_BLUE_BG, STAT_BLUE_BORDER, STAT_BLUE_ACCENT);
            info.setLayout(new GridLayout(2, 1, 6, 8));
            info.setBorder(new EmptyBorder(16, 22, 16, 22));
            details.setFont(FONT_BODY);
            details.setForeground(INK);
            summary.setFont(new Font("Segoe UI", Font.BOLD, 14));
            summary.setForeground(STAT_LAV_ACCENT);
            info.add(details);
            info.add(summary);

            searchAndInfo.add(srCard, BorderLayout.NORTH);
            searchAndInfo.add(info, BorderLayout.CENTER);
            top.add(searchAndInfo, BorderLayout.CENTER);
            add(top, BorderLayout.NORTH);

            RoundedPanel tableCard = new RoundedPanel(16, CARD_BG, BORDER_SUBTLE, PURPLE);
            tableCard.setLayout(new BorderLayout(0, 14));
            tableCard.setBorder(new EmptyBorder(20, 24, 20, 24));
            JLabel tblLbl = new JLabel("Service Records");
            tblLbl.setFont(FONT_SECTION);
            tblLbl.setForeground(new Color(0x36, 0x2A, 0x74));
            tableCard.add(tblLbl, BorderLayout.NORTH);

            setColWidths(t, 85, 130, 230, 110, 95, 125, 210);
            t.getColumnModel().getColumn(0).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(3).setCellRenderer(alignedCellRenderer(SwingConstants.CENTER));
            t.getColumnModel().getColumn(4).setCellRenderer(alignedCellRenderer(SwingConstants.RIGHT));
            t.getColumnModel().getColumn(5).setCellRenderer(statusRenderer());
            tableCard.add(scrollTable(t), BorderLayout.CENTER);
            add(tableCard, BorderLayout.CENTER);

            s.addActionListener(e -> find());
            serial.addActionListener(e -> find());
            all.addActionListener(e -> reset());
            t.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int r = t.getSelectedRow();
                    if (r >= 0) {
                        String sn = String.valueOf(tm.getValueAt(r, 1));
                        if (!sn.isEmpty()) {
                            serial.setText(sn);
                            find();
                        }
                    }
                }
            });
            loadAll();
        }

        void reset() {
            serial.setText("");
            details.setText("Showing all repair records. Enter a serial number or click a row to filter by product.");
            loadAll();
        }

        void loadAll() {
            tm.setRowCount(0);
            try (Connection c = connect(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT r.repair_id,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id ORDER BY r.repair_date DESC, r.repair_id DESC")) {
                double total = 0;
                int count = 0;
                while (r.next()) {
                    double co = r.getDouble(5);
                    total += co;
                    count++;
                    tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getDate(4), r.getBigDecimal(5), r.getString(6), r.getString(7)});
                }
                summary.setText("All Repairs: " + count + "   |   Total Cost: Rs. " + String.format("%.2f", total));
            } catch (Exception ex) { summary.setText("Unable to load repair history."); }
        }

        void find() {
            tm.setRowCount(0);
            String sn = serial.getText().trim();
            if (sn.isEmpty()) { reset(); return; }
            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT p.product_name,p.brand,p.serial_number,c.name,p.purchase_date,DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR),IF(CURDATE()<=DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR),'ACTIVE','EXPIRED') FROM products p JOIN customers c ON c.customer_id=p.customer_id WHERE LOWER(p.serial_number)=LOWER(?)")) {
                p.setString(1, sn);
                try (ResultSet r = p.executeQuery()) {
                    if (!r.next()) {
                        details.setText("No product found with serial: " + sn);
                        summary.setText("Repairs: 0   |   Total Cost: Rs. 0.00   |   Latest Status: None");
                        return;
                    }
                    sn = r.getString(3);
                    serial.setText(sn);
                    details.setText("<html>Product: <b>" + r.getString(1) + "</b> &nbsp;|&nbsp; Brand: " + r.getString(2) + " &nbsp;|&nbsp; Serial: <b>" + sn + "</b> &nbsp;|&nbsp; Customer: " + r.getString(4) + " &nbsp;|&nbsp; Purchase: " + r.getDate(5) + " &nbsp;|&nbsp; Warranty Expiry: " + r.getDate(6) + " &nbsp;|&nbsp; Warranty: <b>" + r.getString(7) + "</b></html>");
                }
            } catch (Exception ex) { error(this, ex); return; }

            try (Connection c = connect(); PreparedStatement p = c.prepareStatement("SELECT r.repair_id,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id WHERE LOWER(p.serial_number)=LOWER(?) ORDER BY r.repair_date DESC, r.repair_id DESC")) {
                p.setString(1, sn);
                double total = 0;
                int count = 0;
                String latest = "None";
                try (ResultSet r = p.executeQuery()) {
                    while (r.next()) {
                        double co = r.getDouble(5);
                        total += co;
                        count++;
                        if (count == 1) latest = r.getString(6);
                        tm.addRow(new Object[]{r.getInt(1), r.getString(2), r.getString(3), r.getDate(4), r.getBigDecimal(5), r.getString(6), r.getString(7)});
                    }
                }
                if (count == 0) {
                    summary.setText("No repair records exist for this product.   |   Total Cost: Rs. 0.00   |   Latest Status: None");
                } else {
                    summary.setText("Repairs: " + count + "   |   Total Cost: Rs. " + String.format("%.2f", total) + "   |   Latest Status: " + latest);
                }
            } catch (Exception ex) { error(this, ex); }
        }
    }
}
