package de.jeanpierrehotz.conwaysgame.ui;

import de.jeanpierrehotz.conwaysgame.control.ConwaysGameControl;

import java.awt.*;
import java.awt.event.*;

/**
 * Diese Klasse bildet die grafische Oberfläche zu dem "Conways Game of life"
 */
public class ConwaysGameUI extends Frame{
    /**
     * Dieses ConwaysGameControl-Objekt bildet die Steuerung für jedes in diesem Programm ausgeführte Spiel
     */
    private ConwaysGameControl control;

    /* Alle primitiven Variablen */

    /**
     * Diese Variable zeigt an, ob die Taste "Strg" gedrückt ist.
     * Falls Strg und eine Maustaste in einem Spiel gleichzeitig gedrückt wird, so werden alle
     * Felder auf einen Wert gesetzt (abhängig von der Maustaste).
     */
    private boolean ctrlPressed;
    /**
     * Diese Variable zeigt an, dass die linke Maustaste gedrückt ist.
     * Falls diese Maustaste in einem Spiel gedrückt ist, so wird das Feld (oder alle Felder, falls
     * "Strg" auch gedrückt ist) auf 1 / true / lebendig gesetzt.
     */
    private boolean leftMousePressed;
    /**
     * Diese Variabl zeigt an, dass die rechte Maustaste gedrückt ist.
     * Falls diese Maustaste in einem Spiel gedrückt ist, so wird das Feld (oder alle Felder, falls
     * "Strg" auch gedrückt ist) auf 0 / false / tot gesetzt.
     */
    private boolean rightMousePressed;
//    TODO: Für Aufnahme zuständig.
//    /**
//     * Diese Variable zeigt an, dass derzeitig alle Frames aufgenommen (gespeichert) werden sollen
//     */
//    private boolean rec;
//    /**
//     * Diese Variable zeigt an, wie viele Dateien bisher gespeichert wurden
//     */
//    private int filectr;
//    /**
//     * Diese Variablen zeigt an, wie viele Bilder bisher in der derzeitigen Aufnahme gespeichert wurden
//     */
//    private int pictctr;

    /* Alle UI-Objekte */

    private Label captionLabel;
    private Label messageLabel;
    private Label aktualisierungsZeitLabel;
    private Label aktualisierungsZeitAnzeigeLabel;
    private Scrollbar aktualisierungsZeitScrollbar;
    private Label spaltenLabel;
    private Choice spaltenChoice;
    private Label zeilenLabel;
    private Choice zeilenChoice;
    private Checkbox infiniteCheckbox;
    private Checkbox gridLinesCheckbox;
    private Button startGameBtn;
    private Button resumeGameBtn;
    private Button saveAndResumeBtn;
    private Checkbox kopierWeltCheckbox;
    private Checkbox modifizierteRegelnCheckbox;
    private Label modifizierteRegelnUeberlebenLabel;
    private TextField modifizierteRegelnUeberlebenTF;
    private Label modifizierteRegelnGeburtLabel;
    private TextField modifizierteRegelnGeburtTF;
    private Checkbox modifizierteZufallsRateCheckbox;
    private Label modifizierteZufallsRateLabel;
    private Label modifizierteZufallsRateAnzeigeLabel;
    private Scrollbar modifizierteZufallsRateScrollbar;

    /* Alle Listener für die UI-Komponenten */

    /**
     * Dieser Adjustmentlistener ist dafür zuständig jederzeit dem User die eingestellte Aktualisierungszeit anzuzeigen
     */
    private AdjustmentListener aktualisierungsZeitAnzeigeListener = e -> {
        aktualisierungsZeitAnzeigeLabel.setText(aktualisierungsZeitScrollbar.getValue() + "ms");
    };
    /**
     * Dieser ActionListener ist dafür zuständig das Spiel zu starten
     */
    private ActionListener startGameListener = e -> {
        requestFocus();
        control.init();
    };
    /**
     * Dieser ActionListener ist dafür zuständig das Spiel wieder aufzunehmen
     */
    private ActionListener resumeGameListener = e -> {
        requestFocus();
        control.resumeGame();
    };
    /**
     * Dieser ActionListener ist dafür zuständig die Einstellungen einzustellen, und das Spiel danach wieder aufzunehmen
     */
    private ActionListener saveAndResumeListener = e -> {
        requestFocus();
        control.saveAndResumeGame();
    };
    /**
     * Dieser Adjustmentlistener ist dafür zuständig dem User jederzeit die eingestellte Zufallsrate anzuzeigen
     */
    private AdjustmentListener modifizierteZufallsRateListener = e -> {
        modifizierteZufallsRateAnzeigeLabel.setText(((double) modifizierteZufallsRateScrollbar.getValue() / 10d) + "%");
    };
    /**
     * Dieser KeyListener ist dafür zuständig in dem Spiel verschiedene Aktionen vorzunehmen
     * (bspw. "P" pausiert die Simulation, oder nimmt diese wieder auf)
     */
    private KeyListener steuerungsKeyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent keyEvent) {}
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if(control.isInGame()) {
                switch (keyEvent.getKeyCode()) {
//                  Mit "P" wird die Simulation pausiert oder wieder aufgenommen
                    case KeyEvent.VK_P:
                        control.togglePause();
                        break;
//                  Mit Escape wird die Simulation beendet
                    case KeyEvent.VK_ESCAPE:
                        control.abortGame();
                        break;
//                  Bei CTRL / Strg wird gespeichert, dass CTRL / Strg gedrückt wurde
                    case KeyEvent.VK_CONTROL:
                        ctrlPressed = true;
                        break;
//                    TODO: Für Aufnahme zuständig.
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
//          Falls CTRL / Strg losgelassen wird, so wird auf jeden Fall gespeichert, dass CTRL / Strg losgelassen wurde
            if(keyEvent.getKeyCode() == KeyEvent.VK_CONTROL){
                ctrlPressed = false;
            }
            /*
            else if(keyEvent.getKeyCode() == KeyEvent.VK_R){
                rec = false;
            }
            */
        }
    };
    /**
     * Dieser MouseListener ist dafür zuständig, die Zustände für leftMousePressed und rightMousePressed aktuell zu
     * halten, und beim ersten Druck ein Event für das Verändern des Statuses eines Felds zu feuern
     */
    private MouseListener steuerungsMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if(mouseEvent.getButton() == MouseEvent.BUTTON1){
                leftMousePressed = true;
                fireEditingEvent(mouseEvent);
            }else if(mouseEvent.getButton() == MouseEvent.BUTTON3){
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
    /**
     * Dieser Listener ist dafür zuständig ein Event für das Verändern des Statuses eines Felds zu feuern, sobald
     * die Maus mit mind. einer gedrückten Taste bewegt wurde.
     */
    private MouseMotionListener steuerungsMouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            fireEditingEvent(mouseEvent);
        }
        @Override
        public void mouseMoved(MouseEvent mouseEvent) {}
    };
    /**
     * Dieser Listener ist dafür zuständig das Spiel oder das Programm zu beenden, sobald der
     * User dies wünscht.
     */
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


    /**
     * Dieser Konstruktor erstellt eine UI für ein Conways Game of life, die die gegebene
     * ConwaysGameControl als Steuerung benutzt.
     * Diese UI wird als Fenster angezeigt.
     * @param c     Die von der UI genutzte Steuerung
     */
    public ConwaysGameUI(ConwaysGameControl c){
        super("Conways Game of life - Jean-Pierre Hotz");

        this.control = c;

        /*
        Listener hinzufügen:
         */

        addWindowListener(steuerungsWindowListener);
        addKeyListener(steuerungsKeyListener);
        addMouseListener(steuerungsMouseListener);
        addMouseMotionListener(steuerungsMouseMotionListener);

//        TODO: Für Aufnahme zuständig.
//        Wir suchen, wie viele Aufnahmen in dem Ordner bisher gemacht wurden, und diese werden gespeichert
//        for(int i = 1; new File("C://Users//Admin//Desktop//CGOL//" + i).exists(); i++){
//            filectr = i;
//        }

        /*
        UI erstellen:
         */

        setLayout(null);

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


    /**
     * Diese Methode gibt ihnen die derzeitig eingestellte Aktualisierungszeit
     * @return  aktuell eingestellte Aktualisierungszeit
     */
    public int getAktualisierungsZeit(){
        return aktualisierungsZeitScrollbar.getValue();
    }

    /**
     * Diese Methode gibt ihnen die derzeitig eingestellte Anzahl an Spalten
     * @return  aktuell eingestellte Anzahl an Spalten
     */
    public int getSpalten(){
        return Integer.parseInt(spaltenChoice.getSelectedItem());
    }

    /**
     * Diese Methode gibt ihnen die derzeitig eingestellte Anzahl an Zeilen
     * @return  aktuell eingestellte Anzahl an Zeilen
     */
    public int getZeilen(){
        return Integer.parseInt(zeilenChoice.getSelectedItem());
    }

    /**
     * Diese Methode gibt ihnen an, ob das Spielfeld "unendlich" (-> Enden sind verbunden) sein soll
     * @return  ob das Spielfeld unendlich sein soll.
     */
    public boolean shouldBeInfinite(){
        return infiniteCheckbox.getState();
    }

    /**
     * Diese Methode gibt ihnen an, ob die Grenzlinien gezeichnet werden sollen
     * @return  ob die Grenzlinien gezeichnet werden sollen
     */
    public boolean shouldShowGridLines(){
        return gridLinesCheckbox.getState();
    }

    /**
     * Diese Methode gibt ihnen an, ob die erstellte Welt eine "Kopierwelt" sein soll, und damit
     * Zellen mit 1, 3, 5 oder 7 lebenden Nachbarn lebendig macht.
     * @return  ob eine neue Welt ein Kopierwelt sein soll
     */
    public boolean shouldBeKopierWelt(){
        return kopierWeltCheckbox.getState();
    }

    /**
     * TODO:
     * @return
     */
    public boolean shouldBeModifizierteRegeln(){
        return modifizierteRegelnCheckbox.getState();
    }
    public String getModifizierteRegelnUeberlebenText(){
        return modifizierteRegelnUeberlebenTF.getText();
    }
    public String getModifizierteRegelnGeburtText(){
        return modifizierteRegelnGeburtTF.getText();
    }
    public boolean shouldBeMdifizierteZufallsRate(){
        return modifizierteZufallsRateCheckbox.getState();
    }
    public double getModifizierteZufallsRate(){
        return 1d - ((double) modifizierteZufallsRateScrollbar.getValue() / 1000d);
    }

    public void setResumable(boolean res){
        resumeGameBtn.setEnabled(res);
        saveAndResumeBtn.setEnabled(res);
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
     * Diese Methode zeichnet (falls man sich in einem Spiel befindet) das Spiel,
     * und zeigt dieses an.
     * @param g     Das Graphics-Objekt, mit dem gezeichnet wird
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (control.isInGame())
            control.drawGame(g);
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

//      /*
//      Speichere einen Frame, falls aufgenommen wird.
//      TODO: Entweder eine Möglichkeit den Ordner auszuwählen, und verhindern, dass ohne definierten Ordner aufgenommen wird
//      TODO: oder den Code, der für die Aufnahme zuständig ist (mit TODO: gekennzeichnet) löschen!
//       */
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
