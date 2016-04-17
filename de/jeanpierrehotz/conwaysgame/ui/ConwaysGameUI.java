package de.jeanpierrehotz.conwaysgame.ui;

import de.jeanpierrehotz.conwaysgame.control.ConwaysGameControl;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Admin on 16.04.2016.
 */
public class ConwaysGameUI extends Frame{

    private ConwaysGameControl control;

    private Label captionLabel;
    private Label messageLabel;

    private Label aktualisierungsZeitLabel;
    private Label aktualisierungsZeitAnzeigeLabel;
    private Scrollbar aktualisierungsZeitScrollbar;
    private AdjustmentListener aktualisierungsZeitAnzeigeListener = e -> {
        aktualisierungsZeitAnzeigeLabel.setText(aktualisierungsZeitScrollbar.getValue() + "ms");
    };
    public int getAktualisierungsZeit(){
        return aktualisierungsZeitScrollbar.getValue();
    }

    private Label spaltenLabel;
    private Choice spaltenChoice;
    public int getSpalten(){
        return Integer.parseInt(spaltenChoice.getSelectedItem());
    }

    private Label zeilenLabel;
    private Choice zeilenChoice;
    public int getZeilen(){
        return Integer.parseInt(zeilenChoice.getSelectedItem());
    }

    private Checkbox infiniteCheckbox;
    public boolean shouldBeInfinite(){
        return infiniteCheckbox.getState();
    }

    private Checkbox gridLinesCheckbox;
    public boolean shouldShowGridLines(){
        return gridLinesCheckbox.getState();
    }

    private Button startGameBtn;
    private ActionListener startGameListener = e -> {
        requestFocus();
        control.init();
    };

    private Button resumeGameBtn;
    private ActionListener resumeGameListener = e -> {
        requestFocus();
        control.resumeGame();
    };

    private Button saveAndResumeBtn;
    private ActionListener saveAndResumeListener = e -> {
        requestFocus();
        control.saveAndResumeGame();
    };

    private Checkbox kopierWeltCheckbox;
    public boolean shouldBeKopierWelt(){
        return kopierWeltCheckbox.getState();
    }

    private Checkbox modifizierteRegelnCheckbox;
    public boolean shouldBeModifizierteRegeln(){
        return modifizierteRegelnCheckbox.getState();
    }
    private Label modifizierteRegelnUeberlebenLabel;
    private TextField modifizierteRegelnUeberlebenTF;
    public String getModifizierteRegelnUeberlebenText(){
        return modifizierteRegelnUeberlebenTF.getText();
    }
    private Label modifizierteRegelnGeburtLabel;
    private TextField modifizierteRegelnGeburtTF;
    public String getModifizierteRegelnGeburtText(){
        return modifizierteRegelnGeburtTF.getText();
    }

    private Checkbox modifizierteZufallsRateCheckbox;
    public boolean shouldBeMdifizierteZufallsRate(){
        return modifizierteZufallsRateCheckbox.getState();
    }
    private Label modifizierteZufallsRateLabel;
    private Label modifizierteZufallsRateAnzeigeLabel;
    private Scrollbar modifizierteZufallsRateScrollbar;
    private AdjustmentListener modifizierteZufallsRateListener = e -> {
        modifizierteZufallsRateAnzeigeLabel.setText(((double) modifizierteZufallsRateScrollbar.getValue() / 10d) + "%");
    };
    public double getModifizierteZufallsRate(){
        return 1d - ((double) modifizierteZufallsRateScrollbar.getValue() / 1000d);
    }

    private void fireEditingEvent(MouseEvent mouseEvent){
        if((leftMousePressed ^ rightMousePressed) && control.isInGame()){
//                System.out.println("asjdflasjda");
            if(ctrlPressed){
//                    if(leftMousePressed){
////                      alle auf 1
//                        control.setAllTo(true);
//                    }else{
////                      alle auf 0
//
//                    }
//                    System.out.println("all " + leftMousePressed);
                control.setAllTo(leftMousePressed);
            }else{
//                    if(leftMousePressed){
////                      an pos auf 1
//                    }else{
////                      an pos auf 0
//                    }
//                    System.out.println("one " + leftMousePressed);
                control.setFieldTo(mouseEvent.getX(), mouseEvent.getY(), leftMousePressed);
            }
        }
    }

    private KeyListener steuerungsKeyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent keyEvent) {}
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if(control.isInGame()) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_P:
                        control.togglePause();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        control.abortGame();
                        break;
                    case KeyEvent.VK_CONTROL:
                        ctrlPressed = true;
                        break;
//                    case KeyEvent.VK_R:
//                        if(!rec) {
//                            filectr++;
//                            pictctr = 0;
//                            new File("C://Users//Admin//Desktop//CGOL//" + filectr).mkdir();
//                        }
//                        rec = !rec;
//                        break;
                }
            }
        }
        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if(keyEvent.getKeyCode() == KeyEvent.VK_CONTROL){
                ctrlPressed = false;
            }/*else if(keyEvent.getKeyCode() == KeyEvent.VK_R){
                rec = false;
            }*/
        }
    };

//    private boolean rec;
//    private int filectr;
//    private int pictctr;

    private boolean ctrlPressed;
    private boolean leftMousePressed;
    private boolean rightMousePressed;

    private MouseListener steuerungsMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if(mouseEvent.getButton() == MouseEvent.BUTTON1){
                leftMousePressed = true;
                fireEditingEvent(mouseEvent);
//                System.out.println("1");
            }else if(mouseEvent.getButton() == MouseEvent.BUTTON3){
//                System.out.println("3");
                rightMousePressed = true;
                fireEditingEvent(mouseEvent);
            }
        }
        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            if(mouseEvent.getButton() == MouseEvent.BUTTON1){
                leftMousePressed = false;
            }else if(mouseEvent.getButton() == MouseEvent.BUTTON3){
                rightMousePressed = false;
            }
        }
    };
    private MouseMotionListener steuerungsMouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            fireEditingEvent(mouseEvent);
        }
        @Override
        public void mouseMoved(MouseEvent mouseEvent) {}
    };

    private WindowListener steuerungsWindowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            if(control.isInGame()){
                control.abortGame();
            }else {
                control.disposeThread();
                System.exit(0);
            }
        }
    };

    public void setResumable(boolean res){
        resumeGameBtn.setEnabled(res);
        saveAndResumeBtn.setEnabled(res);
    }

    public ConwaysGameUI(ConwaysGameControl c){
        super("Conways Game of life - Jean-Pierre Hotz");
        this.control = c;

        addWindowListener(steuerungsWindowListener);
        addKeyListener(steuerungsKeyListener);
        addMouseListener(steuerungsMouseListener);
        addMouseMotionListener(steuerungsMouseMotionListener);

        setLayout(null);

//        for(int i = 1; new File("C://Users//Admin//Desktop//CGOL//" + i).exists(); i++){
//            filectr = i;
//        }

        /*
        UI erstellen:
         */

        captionLabel = new Label("Conways Game of Life");
        captionLabel.setAlignment(Label.CENTER);
        captionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 48));
        add(captionLabel);

        messageLabel = new Label("Dr\u00FCcken sie \"Spielen\", um das Spiel zu starten!");
        messageLabel.setAlignment(Label.CENTER);
        add(messageLabel);

        aktualisierungsZeitLabel = new Label("Aktualisierungszeit: ");
        add(aktualisierungsZeitLabel);

        aktualisierungsZeitScrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 10, 1, 10, 301);
        aktualisierungsZeitScrollbar.addAdjustmentListener(aktualisierungsZeitAnzeigeListener);
        add(aktualisierungsZeitScrollbar);

        aktualisierungsZeitAnzeigeLabel = new Label("10ms");
        add(aktualisierungsZeitAnzeigeLabel);

        modifizierteZufallsRateCheckbox = new Checkbox("Eigene Zufallsrate benutzen", false);
        add(modifizierteZufallsRateCheckbox);

        modifizierteZufallsRateLabel = new Label("Eigene Zufallsrate: ");
        add(modifizierteZufallsRateLabel);

        modifizierteZufallsRateAnzeigeLabel = new Label("25.0%");
        add(modifizierteZufallsRateAnzeigeLabel);

        modifizierteZufallsRateScrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 250, 1, 0, 1001);
        modifizierteZufallsRateScrollbar.addAdjustmentListener(modifizierteZufallsRateListener);
        add(modifizierteZufallsRateScrollbar);

        spaltenLabel = new Label("Spalten:");
        add(spaltenLabel);

        spaltenChoice = new Choice();
        for(int i = 16; i <= 800; i++)
            spaltenChoice.add("" + i);
        add(spaltenChoice);

        zeilenLabel = new Label("Zeilen: ");
        add(zeilenLabel);

        zeilenChoice = new Choice();
        for(int i = 8; i <= 400; i++)
            zeilenChoice.add("" + i);
        add(zeilenChoice);

        infiniteCheckbox = new Checkbox("Soll das Spielfeld unendlich sein?", true);
        add(infiniteCheckbox);

        gridLinesCheckbox = new Checkbox("Sollen die Pixelbegrenzungen gezeigt werden?", true);
        add(gridLinesCheckbox);

        startGameBtn = new Button("Spielen");
        startGameBtn.addActionListener(startGameListener);
        add(startGameBtn);

        resumeGameBtn = new Button("Spiel wieder aufnehmen");
        resumeGameBtn.addActionListener(resumeGameListener);
        add(resumeGameBtn);

        saveAndResumeBtn = new Button("Speichern und wiederaufnehmen");
        saveAndResumeBtn.addActionListener(saveAndResumeListener);
        add(saveAndResumeBtn);

        kopierWeltCheckbox = new Checkbox("Soll die Welt als \"Kopierwelt\" erstellt werden? (-> lebende und tote Zellen ben\u00F6tigen 1, 3, 5 oder 7 lebende Nachbarn um zu leben) (h\u00F6chste Priorit\u00E4t)", false);
        add(kopierWeltCheckbox);

        modifizierteRegelnCheckbox = new Checkbox("Sollen eigene Regeln genutzt werden? (Zahlen 0-8 (Anzahl an lebenden Nachbarn zum leben) mit Kommata (\",\") abgetrennt)", false);
        add(modifizierteRegelnCheckbox);

        modifizierteRegelnUeberlebenLabel = new Label("\u00DCberleben (bereits lebende): ");
        add(modifizierteRegelnUeberlebenLabel);

        modifizierteRegelnUeberlebenTF = new TextField();
        add(modifizierteRegelnUeberlebenTF);

        modifizierteRegelnGeburtLabel = new Label("Geburt (noch tote): ");
        add(modifizierteRegelnGeburtLabel);

        modifizierteRegelnGeburtTF = new TextField();
        add(modifizierteRegelnGeburtTF);

        /*
        Fenster anzeigen, und Komponenten auslegen:
         */

        setResizable(false);
        setSize(1600, 900);
        setVisible(true);

        centerComponents();

        setResumable(false);
    }

    public void showGame(){
        captionLabel.setVisible(false);
        messageLabel.setVisible(false);
        aktualisierungsZeitLabel.setVisible(false);
        aktualisierungsZeitScrollbar.setVisible(false);
        aktualisierungsZeitAnzeigeLabel.setVisible(false);

        modifizierteZufallsRateCheckbox.setVisible(false);
        modifizierteZufallsRateLabel.setVisible(false);
        modifizierteZufallsRateAnzeigeLabel.setVisible(false);
        modifizierteZufallsRateScrollbar.setVisible(false);

        spaltenLabel.setVisible(false);
        spaltenChoice.setVisible(false);
        zeilenLabel.setVisible(false);
        zeilenChoice.setVisible(false);
        infiniteCheckbox.setVisible(false);
        gridLinesCheckbox.setVisible(false);
        startGameBtn.setVisible(false);
        resumeGameBtn.setVisible(false);
        saveAndResumeBtn.setVisible(false);
        kopierWeltCheckbox.setVisible(false);

        modifizierteRegelnCheckbox.setVisible(false);
        modifizierteRegelnUeberlebenLabel.setVisible(false);
        modifizierteRegelnUeberlebenTF.setVisible(false);
        modifizierteRegelnGeburtLabel.setVisible(false);
        modifizierteRegelnGeburtTF.setVisible(false);
    }

    public void showUI(String msg){
        captionLabel.setVisible(true);
        messageLabel.setVisible(true);
        aktualisierungsZeitLabel.setVisible(true);
        aktualisierungsZeitScrollbar.setVisible(true);
        aktualisierungsZeitAnzeigeLabel.setVisible(true);

        modifizierteZufallsRateCheckbox.setVisible(true);
        modifizierteZufallsRateLabel.setVisible(true);
        modifizierteZufallsRateAnzeigeLabel.setVisible(true);
        modifizierteZufallsRateScrollbar.setVisible(true);

        spaltenLabel.setVisible(true);
        spaltenChoice.setVisible(true);
        zeilenLabel.setVisible(true);
        zeilenChoice.setVisible(true);
        infiniteCheckbox.setVisible(true);
        gridLinesCheckbox.setVisible(true);
        startGameBtn.setVisible(true);
        resumeGameBtn.setVisible(true);
        saveAndResumeBtn.setVisible(true);
        kopierWeltCheckbox.setVisible(true);

        modifizierteRegelnCheckbox.setVisible(true);
        modifizierteRegelnUeberlebenLabel.setVisible(true);
        modifizierteRegelnUeberlebenTF.setVisible(true);
        modifizierteRegelnGeburtLabel.setVisible(true);
        modifizierteRegelnGeburtTF.setVisible(true);

        messageLabel.setText(msg);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if(control.isInGame())
            control.drawGame(g);

//        g.fillRect(50, 120, 330, 450);

//        ConwaysGame game = new ConwaysGame(getWidth(), getHeight(), 80, 40);
//        game.drawGame(false, getWidth(), getHeight(), g);
//        game.aktualisiereFeld(false);
//        game.drawGame(true, getWidth(), getHeight(), g);
    }

    private void centerComponents(){
        captionLabel                        .setBounds(0, 50, getWidth(), 60);
        messageLabel                        .setBounds(0, 110, getWidth(), 20);
        aktualisierungsZeitLabel            .setBounds((getWidth() / 2) - 200, 140, 120, 20);
        aktualisierungsZeitScrollbar        .setBounds((getWidth() / 2) - 60, 140, 260, 20);
        aktualisierungsZeitAnzeigeLabel     .setBounds((getWidth() / 2) + 210, 140, 120, 20);

        modifizierteZufallsRateCheckbox     .setBounds((getWidth() / 2) - 260, 170, 800, 20);
        modifizierteZufallsRateLabel        .setBounds((getWidth() / 2) - 200, 200, 120, 20);
        modifizierteZufallsRateScrollbar    .setBounds((getWidth() / 2) - 60, 200, 260, 20);
        modifizierteZufallsRateAnzeigeLabel .setBounds((getWidth() / 2) + 210, 200, 120, 20);

        spaltenLabel                        .setBounds((getWidth() / 2) - 255, 230, 120, 20);
        spaltenChoice                       .setBounds((getWidth() / 2) - 125, 230, 120, 20);
        zeilenLabel                         .setBounds((getWidth() / 2) + 5, 230, 120, 20);
        zeilenChoice                        .setBounds((getWidth() / 2) + 125, 230, 120, 20);
        infiniteCheckbox                    .setBounds((getWidth() / 2) - 260, 260, 800, 20);
        gridLinesCheckbox                   .setBounds((getWidth() / 2) - 260, 290, 800, 20);
        startGameBtn                        .setBounds((getWidth() / 2) - 120, 330, 240, 60);
        resumeGameBtn                       .setBounds((getWidth() / 2) - 80, 400, 160, 30);
        saveAndResumeBtn                    .setBounds((getWidth() / 2) - 100, 440, 200, 30);
        kopierWeltCheckbox                  .setBounds(20, getHeight() - 120, 1000, 20);
        modifizierteRegelnCheckbox          .setBounds(20, getHeight() - 90, 1000, 20);
        modifizierteRegelnUeberlebenLabel   .setBounds(40, getHeight() - 60, 200, 20);
        modifizierteRegelnUeberlebenTF      .setBounds(250, getHeight() - 60, 240, 20);
        modifizierteRegelnGeburtLabel       .setBounds(40, getHeight() - 30, 200, 20);
        modifizierteRegelnGeburtTF          .setBounds(250, getHeight() - 30, 240, 20);
    }

    /**
     * Dieses Image-Objekt wird für das Double-Buffern benutzt.<br>
     * Das bedeutet, dass man zuerst auf dieses Bild zeichnen lässt, ohne es dem
     * User anzuzeigen, und gibt dann dieses Image-Objekt auf einmal aus.<br>
     * Dadurch verhindert man Flackern, das durch die einzeln (zeitlich versetzt)
     * ausgeführten Befehle verursacht wird, bei schnellen Aktualisierungsraten.
     */
    private Image dbImage;
    /**
     * Mit diesem Graphics-Objekt zeichnen wir auf das Image-Objekt,
     * das dem Double-Buffern dient.<br>
     * Dadurch dient dieses Objekt ebenfalls ausschließlich dem Double-Buffern
     */
    private Graphics dbg;
    /**
     * Diese Methode dient dem Double-Buffern der Ausgabe
     */
    @Override
    public void update(Graphics g){
//      Falls das Image-Objekt zum Double-Buffern noch nicht erzeugt wurde
        if(dbImage == null){
//          Erzeugen wir eins mit den Abmessungen des Frames
            dbImage = createImage(getWidth(), getHeight());
//          Und weisen dem Graphics-Objekt die Graphics des Image-Objekts zu
            dbg = dbImage.getGraphics();
        }
//      Dann löschen wir das Image in der Hintergrundfarbe
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, getWidth(), getHeight());

//      Und malen dann (auf dem Image-Objekt) in der Vordergrundfarbe
        dbg.setColor(getForeground());
        paint(dbg);

//      Schlussendlich geben wir das Image-Objekt auf den Koordinaten (0|0) aus
        g.drawImage(dbImage, 0, 0, this);

//        /*
//        SAVE IMAGE
//         */
//        if(rec){
//
//            BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
//            Graphics bla = img.getGraphics();
//            bla.setColor(Color.BLACK);
//            paint(bla);
//
//            try {
//                ImageIO.write(img, "PNG", new File("C://Users//Admin//Desktop//CGOL//" + filectr + "//" + pictctr++ + ".png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
