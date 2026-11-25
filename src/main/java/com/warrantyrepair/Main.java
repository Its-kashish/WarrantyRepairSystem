package com.warrantyrepair;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Main {
    static final Color PURPLE = new Color(91, 65, 145), DARK = new Color(53, 38, 91), LAV = new Color(235, 228, 250), CREAM = new Color(249, 247, 244), INK = new Color(45, 39, 57);
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
    static DefaultTableModel model(String... cols) { return new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } }; }
    static JTable table(DefaultTableModel m) { JTable t = new JTable(m); t.setRowHeight(26); t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12)); t.getTableHeader().setBackground(LAV); t.setFont(new Font("SansSerif", Font.PLAIN, 12)); return t; }
    static JPanel panel() { JPanel p = new JPanel(new BorderLayout(12, 12)); p.setBackground(CREAM); p.setBorder(new EmptyBorder(18,18,18,18)); return p; }
    static JPanel formPanel() { JPanel p = new JPanel(new GridBagLayout()); p.setBackground(Color.WHITE); p.setBorder(new CompoundBorder(new LineBorder(new Color(225,220,235)), new EmptyBorder(12,12,12,12))); return p; }
    static void addField(JPanel p, String label, Component field, int x, int y) { GridBagConstraints c = new GridBagConstraints(); c.gridx=x; c.gridy=y; c.insets=new Insets(6,6,6,6); c.anchor=GridBagConstraints.WEST; c.fill=GridBagConstraints.HORIZONTAL; c.weightx=1; p.add(new JLabel(label), c); c.gridx=x+1; p.add(field,c); }
    static JTextField field(int cols) { JTextField f=new JTextField(cols); f.setPreferredSize(new Dimension(170,28)); return f; }
    static JButton button(String text) { JButton b=new JButton(text); b.setFocusPainted(false); b.setBackground(PURPLE); b.setForeground(new Color(0x4E, 0x34, 0x2E)); b.setBorder(new EmptyBorder(8,14,8,14)); return b; }
    static void title(JPanel p, String text) { JLabel l=new JLabel(text); l.setFont(new Font("SansSerif", Font.BOLD, 22)); l.setForeground(INK); p.add(l,BorderLayout.NORTH); }
    static void refreshAll() { if (app != null) app.refreshPanels(); }
    static AppFrame app;

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> { try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored){} app = new AppFrame(); app.setVisible(true); }); }

    static class AppFrame extends JFrame {
        CardLayout cards = new CardLayout(); JPanel content = new JPanel(cards);
        Dashboard dash = new Dashboard(); Customers customers = new Customers(); Products products = new Products(); Warranties warranties = new Warranties(); Repairs repairs = new Repairs(); History history = new History();
        AppFrame() {
            setTitle("Warranty & Repair History Management System"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1120,760); setMinimumSize(new Dimension(900,620)); setLocationRelativeTo(null); getContentPane().setBackground(CREAM); setLayout(new BorderLayout());
            JPanel sidebar = new JPanel(); sidebar.setBackground(DARK); sidebar.setPreferredSize(new Dimension(210,0)); sidebar.setLayout(new BoxLayout(sidebar,BoxLayout.Y_AXIS)); sidebar.setBorder(new EmptyBorder(20,12,20,12));
            JLabel brand = new JLabel("  Warranty Desk"); brand.setForeground(Color.WHITE); brand.setFont(new Font("SansSerif",Font.BOLD,19)); brand.setBorder(new EmptyBorder(5,4,25,4)); sidebar.add(brand);
            addNav(sidebar,"Dashboard","dashboard"); addNav(sidebar,"Customers","customers"); addNav(sidebar,"Products","products"); addNav(sidebar,"Warranty","warranty"); addNav(sidebar,"Repair Management","repairs"); addNav(sidebar,"Repair History","history");
            content.add(dash,"dashboard"); content.add(customers,"customers"); content.add(products,"products"); content.add(warranties,"warranty"); content.add(repairs,"repairs"); content.add(history,"history");
            add(sidebar,BorderLayout.WEST); add(content,BorderLayout.CENTER); cards.show(content,"dashboard");
            addWindowListener(new WindowAdapter(){public void windowOpened(WindowEvent e){ refreshPanels(); try(Connection c=connect()){}catch(SQLException ex){ JOptionPane.showMessageDialog(AppFrame.this, "Unable to connect to MySQL database.\n\nError: "+ex.getMessage()+"\n\nPlease ensure:\n1. MySQL Server is running on localhost:3306\n2. database/schema.sql has been executed\n3. A .env file exists in this folder with valid DB_USER and DB_PASSWORD", "Database Connection Error", JOptionPane.WARNING_MESSAGE); } }});
        }
        void addNav(JPanel side,String text,String key) { JButton b=new JButton(text); b.setMaximumSize(new Dimension(190,44)); b.setAlignmentX(Component.LEFT_ALIGNMENT); b.setBackground(DARK); b.setForeground(new Color(0x4E, 0x34, 0x2E)); b.setBorder(new EmptyBorder(12,12,12,12)); b.setFocusPainted(false); b.setHorizontalAlignment(SwingConstants.LEFT); b.addActionListener(e->{cards.show(content,key); if(key.equals("dashboard"))dash.load(); if(key.equals("customers"))customers.load(); if(key.equals("products")){products.loadCustomers();products.load();} if(key.equals("warranty"))warranties.load(); if(key.equals("repairs")){repairs.loadProducts();repairs.load();} if(key.equals("history"))history.reset();}); side.add(b); }
        void refreshPanels(){ dash.load(); customers.load(); products.loadCustomers(); products.load(); warranties.load(); repairs.loadProducts(); repairs.load(); history.loadAll(); }
    }

    static class Dashboard extends JPanel {
        JPanel stats=new JPanel(new GridLayout(2,3,12,12)); DefaultTableModel tm=model("Repair ID","Product","Serial Number","Issue","Repair Date","Status"); JTable recent=table(tm);
        Dashboard(){ setLayout(new BorderLayout(12,12)); setBackground(CREAM); setBorder(new EmptyBorder(20,20,20,20)); JLabel h=new JLabel("Dashboard"); h.setFont(new Font("SansSerif",Font.BOLD,24)); add(h,BorderLayout.NORTH); stats.setOpaque(false); add(stats,BorderLayout.CENTER); JPanel bottom=new JPanel(new BorderLayout(8,8)); bottom.setBackground(CREAM); JLabel sub=new JLabel("Recent Repairs"); sub.setFont(new Font("SansSerif",Font.BOLD,17)); bottom.add(sub,BorderLayout.NORTH); bottom.add(new JScrollPane(recent),BorderLayout.CENTER); bottom.setPreferredSize(new Dimension(0,290)); add(bottom,BorderLayout.SOUTH); load(); }
        void card(String name,String value){ JPanel p=new JPanel(new BorderLayout(4,8)); p.setBackground(Color.WHITE); p.setBorder(new CompoundBorder(new LineBorder(new Color(225,220,235)),new EmptyBorder(14,14,14,14))); JLabel n=new JLabel(name); n.setForeground(new Color(110,100,120)); JLabel v=new JLabel(value); v.setFont(new Font("SansSerif",Font.BOLD,26)); v.setForeground(PURPLE); p.add(n,BorderLayout.NORTH); p.add(v,BorderLayout.CENTER); stats.add(p); }
        void load(){ stats.removeAll(); try(Connection c=connect(); Statement s=c.createStatement()){ card("Total Customers",scalar(s,"SELECT COUNT(*) FROM customers")); card("Total Products",scalar(s,"SELECT COUNT(*) FROM products")); card("Active Warranties",scalar(s,"SELECT COUNT(*) FROM products WHERE DATE_ADD(purchase_date, INTERVAL warranty_years YEAR) >= CURDATE()")); card("Expired Warranties",scalar(s,"SELECT COUNT(*) FROM products WHERE DATE_ADD(purchase_date, INTERVAL warranty_years YEAR) < CURDATE()")); card("Pending Repairs",scalar(s,"SELECT COUNT(*) FROM repairs WHERE status='PENDING'")); card("Completed Repairs",scalar(s,"SELECT COUNT(*) FROM repairs WHERE status='COMPLETED'")); tm.setRowCount(0); try(ResultSet r=s.executeQuery("SELECT r.repair_id,p.product_name,p.serial_number,r.issue_description,r.repair_date,r.status FROM repairs r JOIN products p ON r.product_id=p.product_id ORDER BY r.repair_date DESC LIMIT 8")){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getDate(5),r.getString(6)});} }catch(Exception ex){stats.removeAll(); card("Database","Not connected"); card("Setup","Check MySQL / .env"); tm.setRowCount(0);} stats.revalidate();stats.repaint(); }
        String scalar(Statement s,String q)throws SQLException{try(ResultSet r=s.executeQuery(q)){r.next();return r.getString(1);}}
    }

    static String strVal(Object o){ return o == null ? "" : o.toString(); }

    static class Customers extends JPanel {
        JTextField name=field(12),phone=field(12),email=field(12),address=field(12),search=field(12); DefaultTableModel tm=model("ID","Name","Phone","Email","Address"); JTable t=table(tm); int selected=-1;
        Customers(){ setLayout(new BorderLayout(10,10)); setBackground(CREAM); JLabel h=new JLabel("Customer Management");h.setFont(new Font("SansSerif",Font.BOLD,22));add(h,BorderLayout.NORTH); JPanel center=new JPanel(new BorderLayout(10,10));center.setOpaque(false); JPanel f=formPanel(); addField(f,"Name",name,0,0);addField(f,"Phone (10 digits)",phone,0,1);addField(f,"Email",email,0,2);addField(f,"Address",address,0,3); JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));actions.setOpaque(false); JButton save=button("Add Customer"),update=button("Update Selected"),del=button("Delete Selected"),clear=button("Clear"); actions.add(save);actions.add(update);actions.add(del);actions.add(clear); f.add(actions,grid(0,4,2)); center.add(f,BorderLayout.NORTH); JPanel list=new JPanel(new BorderLayout(6,6));list.setOpaque(false);JPanel sr=new JPanel(new FlowLayout(FlowLayout.LEFT));sr.setOpaque(false);sr.add(new JLabel("Search name/phone:"));sr.add(search);JButton sb=button("Search"),all=button("Show All");sr.add(sb);sr.add(all);list.add(sr,BorderLayout.NORTH);list.add(new JScrollPane(t),BorderLayout.CENTER);center.add(list,BorderLayout.CENTER);add(center,BorderLayout.CENTER);
        save.addActionListener(e->add()); update.addActionListener(e->update()); del.addActionListener(e->delete()); clear.addActionListener(e->clear()); sb.addActionListener(e->find()); search.addActionListener(e->find()); all.addActionListener(e->{search.setText("");load();}); t.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){int r=t.getSelectedRow();if(r>=0){selected=Integer.parseInt(tm.getValueAt(r,0).toString());name.setText(strVal(tm.getValueAt(r,1)));phone.setText(strVal(tm.getValueAt(r,2)));email.setText(strVal(tm.getValueAt(r,3)));address.setText(strVal(tm.getValueAt(r,4)));}}}); load(); }
        GridBagConstraints grid(int x,int y,int w){GridBagConstraints c=new GridBagConstraints();c.gridx=x;c.gridy=y;c.gridwidth=w;c.insets=new Insets(5,5,5,5);c.anchor=GridBagConstraints.WEST;return c;}
        boolean validateInput(){
            if(name.getText().trim().isEmpty()||!phone.getText().trim().matches("\\d{10}")){info(this,"Enter a customer name and a valid 10-digit phone number.");return false;}
            String em=email.getText().trim();
            if(!em.isEmpty()&&!em.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){info(this,"Enter a valid email address or leave it blank.");return false;}
            return true;
        }
        void add(){if(!validateInput())return;try(Connection c=connect();PreparedStatement p=c.prepareStatement("INSERT INTO customers(name,phone,email,address) VALUES(?,?,?,?)")){p.setString(1,name.getText().trim());p.setString(2,phone.getText().trim());p.setString(3,email.getText().trim());p.setString(4,address.getText().trim());p.executeUpdate();info(this,"Customer added.");clear();load();refreshAll();}catch(Exception ex){error(this,ex);}}
        void update(){if(selected<0){info(this,"Select a customer row first.");return;}if(!validateInput())return;try(Connection c=connect();PreparedStatement p=c.prepareStatement("UPDATE customers SET name=?,phone=?,email=?,address=? WHERE customer_id=?")){p.setString(1,name.getText().trim());p.setString(2,phone.getText().trim());p.setString(3,email.getText().trim());p.setString(4,address.getText().trim());p.setInt(5,selected);p.executeUpdate();info(this,"Customer updated.");clear();load();refreshAll();}catch(Exception ex){error(this,ex);}}
        void delete(){if(selected<0){info(this,"Select a customer row first.");return;}if(JOptionPane.showConfirmDialog(this,"Delete selected customer? Products linked to them may prevent deletion.","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;try(Connection c=connect();PreparedStatement p=c.prepareStatement("DELETE FROM customers WHERE customer_id=?")){p.setInt(1,selected);p.executeUpdate();info(this,"Customer deleted.");clear();load();refreshAll();}catch(SQLException ex){if("23000".equals(ex.getSQLState()))info(this,"Cannot delete this customer because products are registered under them.");else error(this,ex);}catch(Exception ex){error(this,ex);}}
        void clear(){name.setText("");phone.setText("");email.setText("");address.setText("");selected=-1;t.clearSelection();}
        void load(){tm.setRowCount(0);try(Connection c=connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT customer_id,name,phone,email,address FROM customers ORDER BY customer_id DESC")){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5)});}catch(Exception ex){if(isShowing())error(this,ex);}}
        void find(){tm.setRowCount(0);try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT customer_id,name,phone,email,address FROM customers WHERE name LIKE ? OR phone LIKE ? ORDER BY customer_id DESC")){String q="%"+search.getText().trim()+"%";p.setString(1,q);p.setString(2,q);try(ResultSet r=p.executeQuery()){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5)});}}catch(Exception ex){error(this,ex);}}
    }

    static class Products extends JPanel {
        JComboBox<CustomerItem> customer=new JComboBox<>(); JTextField pname=field(10),brand=field(10),serial=field(10),purchase=field(10),years=field(4),search=field(10); DefaultTableModel tm=model("ID","Customer","Product","Brand","Serial","Purchase Date","Warranty Years"); JTable t=table(tm); int selected=-1;
        Products(){setLayout(new BorderLayout(10,10));setBackground(CREAM);JLabel h=new JLabel("Product Management");h.setFont(new Font("SansSerif",Font.BOLD,22));add(h,BorderLayout.NORTH);JPanel center=new JPanel(new BorderLayout(10,10));center.setOpaque(false);JPanel f=formPanel();addField(f,"Customer",customer,0,0);addField(f,"Product Name",pname,0,1);addField(f,"Brand",brand,0,2);addField(f,"Serial Number",serial,0,3);addField(f,"Purchase Date (YYYY-MM-DD)",purchase,0,4);addField(f,"Warranty Years",years,0,5);JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));actions.setOpaque(false);JButton add=button("Register Product"),upd=button("Update Selected"),del=button("Delete Selected"),clr=button("Clear");actions.add(add);actions.add(upd);actions.add(del);actions.add(clr);f.add(actions,grid(0,6,2));center.add(f,BorderLayout.NORTH);JPanel list=new JPanel(new BorderLayout(6,6));list.setOpaque(false);JPanel sr=new JPanel(new FlowLayout(FlowLayout.LEFT));sr.setOpaque(false);sr.add(new JLabel("Search product/serial:"));sr.add(search);JButton sb=button("Search"),show=button("Show All");sr.add(sb);sr.add(show);list.add(sr,BorderLayout.NORTH);list.add(new JScrollPane(t),BorderLayout.CENTER);center.add(list,BorderLayout.CENTER);add(center,BorderLayout.CENTER);add.addActionListener(e->insert());upd.addActionListener(e->update());del.addActionListener(e->delete());clr.addActionListener(e->clear());sb.addActionListener(e->find());search.addActionListener(e->find());show.addActionListener(e->{search.setText("");load();});t.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){int r=t.getSelectedRow();if(r>=0){selected=Integer.parseInt(tm.getValueAt(r,0).toString());String customerName=strVal(tm.getValueAt(r,1));for(int i=0;i<customer.getItemCount();i++){if(customer.getItemAt(i).name.equals(customerName)){customer.setSelectedIndex(i);break;}}pname.setText(strVal(tm.getValueAt(r,2)));brand.setText(strVal(tm.getValueAt(r,3)));serial.setText(strVal(tm.getValueAt(r,4)));purchase.setText(strVal(tm.getValueAt(r,5)));years.setText(strVal(tm.getValueAt(r,6)));}}});load();loadCustomers();}
        GridBagConstraints grid(int x,int y,int w){GridBagConstraints c=new GridBagConstraints();c.gridx=x;c.gridy=y;c.gridwidth=w;c.insets=new Insets(5,5,5,5);c.anchor=GridBagConstraints.WEST;return c;}
        void loadCustomers(){CustomerItem prev=(CustomerItem)customer.getSelectedItem();customer.removeAllItems();try(Connection c=connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT customer_id,name FROM customers ORDER BY name")){while(r.next()){CustomerItem item=new CustomerItem(r.getInt(1),r.getString(2));customer.addItem(item);if(prev!=null&&prev.id==item.id)customer.setSelectedItem(item);}}catch(Exception ex){}}
        boolean validateProductInput(){
            CustomerItem ci=(CustomerItem)customer.getSelectedItem();
            if(ci==null||pname.getText().trim().isEmpty()||serial.getText().trim().isEmpty()){info(this,"Select a customer and enter product name and serial number.");return false;}
            try{
                LocalDate d=LocalDate.parse(purchase.getText().trim());
                int w=Integer.parseInt(years.getText().trim());
                if(d.isAfter(LocalDate.now())||w<=0){info(this,"Purchase date must not be future and warranty years must be positive.");return false;}
            }catch(Exception ex){info(this,"Enter a valid YYYY-MM-DD purchase date and a positive whole number for warranty years.");return false;}
            return true;
        }
        void insert(){if(!validateProductInput())return;CustomerItem ci=(CustomerItem)customer.getSelectedItem();try{LocalDate d=LocalDate.parse(purchase.getText().trim());int w=Integer.parseInt(years.getText().trim());try(Connection c=connect();PreparedStatement p=c.prepareStatement("INSERT INTO products(customer_id,product_name,brand,serial_number,purchase_date,warranty_years) VALUES(?,?,?,?,?,?)")){p.setInt(1,ci.id);p.setString(2,pname.getText().trim());p.setString(3,brand.getText().trim());p.setString(4,serial.getText().trim());p.setDate(5,Date.valueOf(d));p.setInt(6,w);p.executeUpdate();}info(this,"Product registered.");clear();load();refreshAll();}catch(SQLException ex){if("23000".equals(ex.getSQLState()))info(this,"A product with that serial number already exists.");else error(this,ex);}catch(Exception ex){error(this,ex);}}
        void update(){if(selected<0){info(this,"Select a product row first.");return;}if(!validateProductInput())return;CustomerItem ci=(CustomerItem)customer.getSelectedItem();try{LocalDate d=LocalDate.parse(purchase.getText().trim());int w=Integer.parseInt(years.getText().trim());try(Connection c=connect();PreparedStatement p=c.prepareStatement("UPDATE products SET customer_id=?,product_name=?,brand=?,serial_number=?,purchase_date=?,warranty_years=? WHERE product_id=?")){p.setInt(1,ci.id);p.setString(2,pname.getText().trim());p.setString(3,brand.getText().trim());p.setString(4,serial.getText().trim());p.setDate(5,Date.valueOf(d));p.setInt(6,w);p.setInt(7,selected);p.executeUpdate();}info(this,"Product updated.");clear();load();refreshAll();}catch(SQLException ex){if("23000".equals(ex.getSQLState()))info(this,"A product with that serial number already exists.");else error(this,ex);}catch(Exception ex){error(this,ex);}}
        void delete(){if(selected<0){info(this,"Select a product row first.");return;}if(JOptionPane.showConfirmDialog(this,"Delete selected product? Existing repairs may prevent deletion.","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;try(Connection c=connect();PreparedStatement p=c.prepareStatement("DELETE FROM products WHERE product_id=?")){p.setInt(1,selected);p.executeUpdate();info(this,"Product deleted.");clear();load();refreshAll();}catch(SQLException ex){if("23000".equals(ex.getSQLState()))info(this,"Cannot delete this product because repair records exist for it.");else error(this,ex);}catch(Exception ex){error(this,ex);}}
        void clear(){pname.setText("");brand.setText("");serial.setText("");purchase.setText("");years.setText("");selected=-1;t.clearSelection();}
        void load(){tm.setRowCount(0);try(Connection c=connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT p.product_id,c.name,p.product_name,p.brand,p.serial_number,p.purchase_date,p.warranty_years FROM products p JOIN customers c ON c.customer_id=p.customer_id ORDER BY p.product_id DESC")){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getDate(6),r.getInt(7)});}catch(Exception ex){}}
        void find(){tm.setRowCount(0);try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT p.product_id,c.name,p.product_name,p.brand,p.serial_number,p.purchase_date,p.warranty_years FROM products p JOIN customers c ON c.customer_id=p.customer_id WHERE p.product_name LIKE ? OR p.serial_number LIKE ? ORDER BY p.product_id DESC")){String q="%"+search.getText().trim()+"%";p.setString(1,q);p.setString(2,q);try(ResultSet r=p.executeQuery()){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getDate(6),r.getInt(7)});}}catch(Exception ex){error(this,ex);}}
    }
    static class CustomerItem { int id; String name; CustomerItem(int i,String n){id=i;name=n;} public String toString(){return id+" - "+name;} }

    static class Warranties extends JPanel {
        DefaultTableModel tm=model("Product ID","Customer","Product","Brand","Serial","Purchase Date","Expiry Date","Status"); JTable t=table(tm); JTextField search=field(14);
        Warranties(){setLayout(new BorderLayout(10,10));setBackground(CREAM);JLabel h=new JLabel("Warranty Tracking");h.setFont(new Font("SansSerif",Font.BOLD,22));JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));top.setOpaque(false);top.add(new JLabel("Search serial/product/customer:"));top.add(search);JButton s=button("Search"),all=button("Show All");top.add(s);top.add(all);JPanel north=new JPanel(new BorderLayout(4,4));north.setOpaque(false);north.add(h,BorderLayout.NORTH);north.add(top,BorderLayout.CENTER);add(north,BorderLayout.NORTH);add(new JScrollPane(t),BorderLayout.CENTER);s.addActionListener(e->load(search.getText().trim()));search.addActionListener(e->load(search.getText().trim()));all.addActionListener(e->{search.setText("");load("");});load("");}
        void load(){ load(search.getText().trim()); }
        void load(String q){tm.setRowCount(0);String sql="SELECT p.product_id,c.name,p.product_name,p.brand,p.serial_number,p.purchase_date,DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR) expiry,IF(CURDATE()<=DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR),'ACTIVE','EXPIRED') status FROM products p JOIN customers c ON c.customer_id=p.customer_id WHERE p.serial_number LIKE ? OR p.product_name LIKE ? OR c.name LIKE ? ORDER BY expiry";try(Connection c=connect();PreparedStatement p=c.prepareStatement(sql)){String like="%"+q+"%";p.setString(1,like);p.setString(2,like);p.setString(3,like);try(ResultSet r=p.executeQuery()){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getDate(6),r.getDate(7),r.getString(8)});}}catch(Exception ex){}}
    }

    static class Repairs extends JPanel {
        JComboBox<ProductItem> product=new JComboBox<>(); JTextField issue=field(12),date=field(10),cost=field(8),remarks=field(12),search=field(10); JComboBox<String> status=new JComboBox<>(new String[]{"PENDING","IN_PROGRESS","COMPLETED"}); DefaultTableModel tm=model("Repair ID","Product","Serial","Issue","Date","Cost","Status","Remarks"); JTable t=table(tm); int selected=-1;
        Repairs(){setLayout(new BorderLayout(10,10));setBackground(CREAM);JLabel h=new JLabel("Repair Management");h.setFont(new Font("SansSerif",Font.BOLD,22));add(h,BorderLayout.NORTH);JPanel center=new JPanel(new BorderLayout(10,10));center.setOpaque(false);JPanel f=formPanel();addField(f,"Product",product,0,0);addField(f,"Issue Description",issue,0,1);addField(f,"Repair Date (YYYY-MM-DD)",date,0,2);addField(f,"Repair Cost",cost,0,3);addField(f,"Status",status,0,4);addField(f,"Remarks",remarks,0,5);date.setText(LocalDate.now().toString());JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));actions.setOpaque(false);JButton add=button("Register Repair"),upd=button("Update Selected Status/Cost"),del=button("Delete Selected"),clr=button("Clear");actions.add(add);actions.add(upd);actions.add(del);actions.add(clr);GridBagConstraints gc=new GridBagConstraints();gc.gridx=0;gc.gridy=6;gc.gridwidth=2;gc.insets=new Insets(5,5,5,5);gc.anchor=GridBagConstraints.WEST;f.add(actions,gc);center.add(f,BorderLayout.NORTH);JPanel list=new JPanel(new BorderLayout(6,6));list.setOpaque(false);JPanel sr=new JPanel(new FlowLayout(FlowLayout.LEFT));sr.setOpaque(false);sr.add(new JLabel("Search serial:"));sr.add(search);JButton sb=button("Search"),show=button("Show All");sr.add(sb);sr.add(show);list.add(sr,BorderLayout.NORTH);list.add(new JScrollPane(t),BorderLayout.CENTER);center.add(list,BorderLayout.CENTER);add(center,BorderLayout.CENTER);add.addActionListener(e->insert());upd.addActionListener(e->update());del.addActionListener(e->delete());clr.addActionListener(e->clear());sb.addActionListener(e->find());search.addActionListener(e->find());show.addActionListener(e->{search.setText("");load();});t.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){int r=t.getSelectedRow();if(r>=0){selected=Integer.parseInt(tm.getValueAt(r,0).toString());String serialNo=strVal(tm.getValueAt(r,2));for(int i=0;i<product.getItemCount();i++){if(product.getItemAt(i).serial.equals(serialNo)){product.setSelectedIndex(i);break;}}issue.setText(strVal(tm.getValueAt(r,3)));date.setText(strVal(tm.getValueAt(r,4)));cost.setText(strVal(tm.getValueAt(r,5)));status.setSelectedItem(strVal(tm.getValueAt(r,6)));remarks.setText(strVal(tm.getValueAt(r,7)));}}});loadProducts();load();}
        void loadProducts(){ProductItem prev=(ProductItem)product.getSelectedItem();product.removeAllItems();try(Connection c=connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT product_id,product_name,serial_number FROM products ORDER BY product_name")){while(r.next()){ProductItem item=new ProductItem(r.getInt(1),r.getString(2),r.getString(3));product.addItem(item);if(prev!=null&&prev.id==item.id)product.setSelectedItem(item);}}catch(Exception ex){}}
        void insert(){ProductItem pi=(ProductItem)product.getSelectedItem();if(pi==null||issue.getText().trim().isEmpty()){info(this,"Select a product and enter the repair issue.");return;}try{LocalDate d=LocalDate.parse(date.getText().trim());double co=Double.parseDouble(cost.getText().trim().isEmpty()?"0":cost.getText().trim());if(co<0)throw new IllegalArgumentException("Cost cannot be negative.");try(Connection c=connect();PreparedStatement p=c.prepareStatement("INSERT INTO repairs(product_id,issue_description,repair_date,repair_cost,status,remarks) VALUES(?,?,?,?,?,?)")){p.setInt(1,pi.id);p.setString(2,issue.getText().trim());p.setDate(3,Date.valueOf(d));p.setDouble(4,co);p.setString(5,status.getSelectedItem().toString());p.setString(6,remarks.getText().trim());p.executeUpdate();}String warranty;try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT IF(CURDATE()<=DATE_ADD(purchase_date,INTERVAL warranty_years YEAR),'Under Warranty','Out of Warranty'),DATE_ADD(purchase_date,INTERVAL warranty_years YEAR) FROM products WHERE product_id=?")){p.setInt(1,pi.id);try(ResultSet r=p.executeQuery()){r.next();warranty=r.getString(1)+" (expiry: "+r.getDate(2)+")";}}info(this,"Repair registered. Warranty check: "+warranty);clear();load();refreshAll();}catch(Exception ex){error(this,ex);}}
        void update(){if(selected<0){info(this,"Select a repair row first.");return;}if(issue.getText().trim().isEmpty()){info(this,"Enter the repair issue description.");return;}try{LocalDate d=LocalDate.parse(date.getText().trim());double co=Double.parseDouble(cost.getText().trim().isEmpty()?"0":cost.getText().trim());if(co<0)throw new IllegalArgumentException("Cost cannot be negative.");try(Connection c=connect();PreparedStatement p=c.prepareStatement("UPDATE repairs SET issue_description=?,repair_date=?,status=?,repair_cost=?,remarks=? WHERE repair_id=?")){p.setString(1,issue.getText().trim());p.setDate(2,Date.valueOf(d));p.setString(3,status.getSelectedItem().toString());p.setDouble(4,co);p.setString(5,remarks.getText().trim());p.setInt(6,selected);p.executeUpdate();}info(this,"Repair updated.");clear();load();refreshAll();}catch(Exception ex){error(this,ex);}}
        void delete(){if(selected<0){info(this,"Select a repair row first.");return;}if(JOptionPane.showConfirmDialog(this,"Delete selected repair record?","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;try(Connection c=connect();PreparedStatement p=c.prepareStatement("DELETE FROM repairs WHERE repair_id=?")){p.setInt(1,selected);p.executeUpdate();info(this,"Repair deleted.");clear();load();refreshAll();}catch(Exception ex){error(this,ex);}}
        void clear(){issue.setText("");date.setText(LocalDate.now().toString());cost.setText("");remarks.setText("");status.setSelectedItem("PENDING");selected=-1;t.clearSelection();}
        void load(){tm.setRowCount(0);try(Connection c=connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT r.repair_id,p.product_name,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id ORDER BY r.repair_id DESC")){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getDate(5),r.getBigDecimal(6),r.getString(7),r.getString(8)});}catch(Exception ex){}}
        void find(){tm.setRowCount(0);try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT r.repair_id,p.product_name,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id WHERE p.serial_number LIKE ? ORDER BY r.repair_id DESC")){p.setString(1,"%"+search.getText().trim()+"%");try(ResultSet r=p.executeQuery()){while(r.next())tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getDate(5),r.getBigDecimal(6),r.getString(7),r.getString(8)});}}catch(Exception ex){error(this,ex);}}
    }
    static class ProductItem {int id;String name,serial;ProductItem(int i,String n,String s){id=i;name=n;serial=s;}public String toString(){return name+" ("+serial+")";}}

    static class History extends JPanel {
        JTextField serial=field(16); JLabel details=new JLabel("Enter a product serial number (or click a row below) to view its repair history."); JLabel summary=new JLabel(" "); DefaultTableModel tm=model("Repair ID","Serial","Issue","Date","Cost","Status","Remarks"); JTable t=table(tm);
        History(){
            setLayout(new BorderLayout(10,10));setBackground(CREAM);
            JLabel h=new JLabel("Repair History");h.setFont(new Font("SansSerif",Font.BOLD,22));
            JPanel sr=new JPanel(new FlowLayout(FlowLayout.LEFT));sr.setOpaque(false);
            sr.add(new JLabel("Product serial number:"));sr.add(serial);
            JButton s=button("Search"),all=button("Show All");sr.add(s);sr.add(all);
            JPanel info=new JPanel(new GridLayout(2,1,4,4));info.setBackground(Color.WHITE);info.setBorder(new EmptyBorder(12,12,12,12));
            details.setFont(new Font("SansSerif",Font.PLAIN,13));summary.setFont(new Font("SansSerif",Font.BOLD,14));
            info.add(details);info.add(summary);
            JPanel top=new JPanel(new BorderLayout(8,8));top.setOpaque(false);
            top.add(h,BorderLayout.NORTH);top.add(sr,BorderLayout.CENTER);top.add(info,BorderLayout.SOUTH);
            add(top,BorderLayout.NORTH);
            t.setFillsViewportHeight(true);
            JScrollPane scroll=new JScrollPane(t);scroll.setPreferredSize(new Dimension(800,420));
            add(scroll,BorderLayout.CENTER);
            s.addActionListener(e->find());
            serial.addActionListener(e->find());
            all.addActionListener(e->reset());
            t.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){int r=t.getSelectedRow();if(r>=0){String sn=String.valueOf(tm.getValueAt(r,1));if(!sn.isEmpty()){serial.setText(sn);find();}}}});
            loadAll();
        }
        void reset(){serial.setText("");details.setText("Showing all repair records. Enter a serial number or click a row to filter by product.");loadAll();}
        void loadAll(){
            tm.setRowCount(0);
            try(Connection c=connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT r.repair_id,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id ORDER BY r.repair_date DESC, r.repair_id DESC")){
                double total=0;int count=0;
                while(r.next()){double co=r.getDouble(5);total+=co;count++;tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getDate(4),r.getBigDecimal(5),r.getString(6),r.getString(7)});}
                summary.setText("All Repairs: "+count+" | Total Cost: Rs. "+String.format("%.2f",total));
            }catch(Exception ex){summary.setText("Unable to load repair history.");}
        }
        void find(){
            tm.setRowCount(0);
            String sn=serial.getText().trim();
            if(sn.isEmpty()){reset();return;}
            try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT p.product_name,p.brand,p.serial_number,c.name,p.purchase_date,DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR),IF(CURDATE()<=DATE_ADD(p.purchase_date,INTERVAL p.warranty_years YEAR),'ACTIVE','EXPIRED') FROM products p JOIN customers c ON c.customer_id=p.customer_id WHERE LOWER(p.serial_number)=LOWER(?)")){
                p.setString(1,sn);
                try(ResultSet r=p.executeQuery()){
                    if(!r.next()){details.setText("No product found with serial: "+sn);summary.setText("Repairs: 0 | Total Cost: Rs. 0.00 | Latest Status: None");return;}
                    sn=r.getString(3);serial.setText(sn);
                    details.setText("<html>Product: <b>"+r.getString(1)+"</b> | Brand: "+r.getString(2)+" | Serial: <b>"+sn+"</b> | Customer: "+r.getString(4)+" | Purchase: "+r.getDate(5)+" | Warranty Expiry: "+r.getDate(6)+" | Warranty: <b>"+r.getString(7)+"</b></html>");
                }
            }catch(Exception ex){error(this,ex);return;}
            try(Connection c=connect();PreparedStatement p=c.prepareStatement("SELECT r.repair_id,p.serial_number,r.issue_description,r.repair_date,r.repair_cost,r.status,r.remarks FROM repairs r JOIN products p ON p.product_id=r.product_id WHERE LOWER(p.serial_number)=LOWER(?) ORDER BY r.repair_date DESC, r.repair_id DESC")){
                p.setString(1,sn);
                double total=0;int count=0;String latest="None";
                try(ResultSet r=p.executeQuery()){
                    while(r.next()){double co=r.getDouble(5);total+=co;count++;if(count==1)latest=r.getString(6);tm.addRow(new Object[]{r.getInt(1),r.getString(2),r.getString(3),r.getDate(4),r.getBigDecimal(5),r.getString(6),r.getString(7)});}
                }
                if(count==0){summary.setText("No repair records exist for this product. | Total Cost: Rs. 0.00 | Latest Status: None");}
                else{summary.setText("Repairs: "+count+" | Total Cost: Rs. "+String.format("%.2f",total)+" | Latest Status: "+latest);}
            }catch(Exception ex){error(this,ex);}
        }
    }
}
