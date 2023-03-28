package cl.forevision.wrapper.controllers;

/**
 * Created by root on 08-03-23.
 */

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import cl.forevision.wrapper.helpers.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import static cl.forevision.wrapper.model.ParameterEnum.*;
import static cl.forevision.wrapper.model.ParameterEnum.SCRAPPER_PATH;

public class MainController {

    @FXML
    private TextArea console;
    private PrintStream ps ;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField baseUrlToken;

    @FXML
    private TextField baseUrlConfig;

    @FXML
    private TextField scrapperPath;

    @FXML
    private Button startBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button fileChooserBtn;

    private static Logger logger;

    private static final String SQUARE_BUBBLE = "M24 1h-24v16.981h4v5.019l7-5.019h13z";

    private static boolean flag = true;

    Stage stage;

    static LogHelper fh;

    public MainController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() throws Exception {
        //tabPane = new TabPane();


        fh = LogHelper.getInstance();
        logger = Logger.getLogger(ParamsHelper.class.getName());
        logger.addHandler(fh);

        String baseUrlToken = System.getenv("BASE_URL_TOKEN");
        String baseUrlConfig = System.getenv("BASE_URL_CONFIG");

        ConfigHelper.getInstance().setParameter(BASE_URL_TOKEN.getParameter(), baseUrlToken);
        ConfigHelper.getInstance().setParameter(BASE_URL_CONFIG.getParameter(), baseUrlConfig);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Init Error");
        alert.setResizable(true);
        alert.setWidth(500.0);
        alert.setHeight(500.0);

        if (StringUtils.isEmpty(baseUrlToken)) {
            alert.setContentText("You have to provide environment variable BASE_URL_TOKEN with proper URL");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.exit(0);
                }
                System.exit(0);
            });
        }

        if (StringUtils.isEmpty(baseUrlConfig)) {
            alert.setContentText("You have to provide environment variable BASE_URL_CONFIG with proper URL");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.exit(0);
                }
                System.exit(0);
            });
        }

        try {
            if (!ConnectionHelper.getInstance().healthCheck(baseUrlToken)) {
                alert.setContentText("BASE_URL_TOKEN is not healthy, please contact Administrator.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.exit(0);
                    }
                    System.exit(0);
                });
            }
        }
        catch (IOException e) {
            alert.setContentText("Unable to hit BASE_URL_TOKEN, please contact Administrator");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.exit(0);
                }
                System.exit(0);
            });
        }

        try {
            if (!ConnectionHelper.getInstance().healthCheck(baseUrlToken)) {
                alert.setContentText("BASE_URL_CONFIG is not healthy, please contact Administrator.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.exit(0);
                    }
                    System.exit(0);
                });
            }
        }
        catch (IOException e) {
            alert.setContentText("Unable to hit BASE_URL_CONFIG, please contact Administrator");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.exit(0);
                }
                System.exit(0);
            });
        }

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tab = new Tab("launcher");
        tab.setId("launcher");
        TextArea textArea = new TextArea();
        tab.setContent(textArea);
        tabPane.getTabs().add(tab);
        //ps = new PrintStream(new Console(console));

        FileChooser fileChooser = new FileChooser();

        fileChooserBtn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                scrapperPath.setText(selectedFile.getAbsolutePath());
            }
        });

        startBtn.setOnMouseEntered(e -> startBtn.setStyle("-fx-background-color: #027bc7d6"));
        startBtn.setOnMouseExited(e -> startBtn.setStyle("-fx-background-color:  #027bc7"));

        startBtn.setOnAction(event -> {

            flag = true;

            if (username.getText().isEmpty()) {
                makeBubble(username, "Value is required");
                return;
            }
            if (password.getText().isEmpty()) {
                makeBubble(password, "Value is required");
                return;
            }
            if (scrapperPath.getText().isEmpty()) {
                makeBubble(scrapperPath, "Value is required");
                return;
            }
            ConfigHelper.getInstance().setParameter(USER_NAME.getParameter(), username.getText());
            ConfigHelper.getInstance().setParameter(PASSWORD.getParameter(), password.getText());
            ConfigHelper.getInstance().setParameter(SCRAPPER_PATH.getParameter(), scrapperPath.getText());

            try {
                if(!TokenHelper.getInstance().canLogin()) {
                    flag = false;
                    alert.setContentText("Incorrect Username or Password.");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                        }
                    });
                }
            }
            catch (IOException e) {
                alert.setContentText("Unable to login, please try again later");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }

            if (!flag) {
                return;
            }

            ConfigHelper.getInstance().setParameter(SCRAPPER_PATH.getParameter(), scrapperPath.getText());

            File f = new File(scrapperPath.getText());

            if (!f.exists()) {
                flag = false;
                alert.setContentText("Provided file does not exist");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }

            if (!flag) {
                return;
            }

            if (f.isDirectory()) {
                flag = false;
                alert.setContentText("Provided file is a directory");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }

            if (!flag) {
                return;
            }

            try {
                if (!detectJarFile(scrapperPath.getText())) {
                    flag = false;
                    alert.setContentText("Provided file is not a jar");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                        }
                    });
                }
            } catch (IOException e) {
                flag = false;
                alert.setContentText("Error verifying file. Please try again later");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }

            if (!flag) {
                return;
            }

            if (ProcessHelper.getInstance().isProcessRunning()) {
                flag = false;
                alert.setContentText("There is an active process. Please stop it before running a new one");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }

            if (!flag) {
                return;
            }

            logger.log(Level.INFO, "Starting Launcher process...");

            TokenHelper.getInstance().start();

            while(ConfigHelper.getInstance().getParameter(TOKEN.getParameter()) == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            logger.log(Level.INFO, "Loading parameters from ScrapperConfig...");

            try {
                ParamsHelper.getInstance().loadParameters();
            } catch (Exception e) {
                e.printStackTrace();
            }

            PollingHelper.getInstance().start();

            username.setDisable(true);
            password.setDisable(true);
            scrapperPath.setDisable(true);
            fileChooserBtn.setDisable(true);

            progressBar.setVisible(true);

        });


        stopBtn.setOnAction(event -> {

            boolean flag = true;

            if (!ProcessHelper.getInstance().isProcessRunning()) {
                flag = false;
                alert.setContentText("There is no active process. Nothing to stop!");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                    }
                });
            }

            if (!flag) {
                return;
            }

            logger.log(Level.INFO, "Stopping Launcher process...");

            try {
                ProcessHelper.getInstance().stopInstances();
            } catch (Exception e) {
                e.printStackTrace();
            }

            username.setDisable(false);
            password.setDisable(false);
            scrapperPath.setDisable(false);
            fileChooserBtn.setDisable(false);

            progressBar.setVisible(false);

        });

    }

    public void appendText(String id, String valueOf) {
        Platform.runLater(() -> {
            Tab tab = null;
            try {
                tab = tabPane.getTabs().stream()
                        .filter(t -> t.getId().equals(id))
                        .findFirst()
                        .orElseThrow(Exception::new);
            } catch (Exception e) {
                tab = addTab(id);
                logger.log(Level.SEVERE, e.getMessage());
                //e.printStackTrace();
            }
            TextArea textArea = (TextArea) tab.getContent();
            textArea.appendText(valueOf);
            textArea.appendText("\n");
        });
    }

    @FXML
    public Tab addTab(String name) {
        boolean tabExists = tabPane.getTabs().stream().anyMatch(t -> name.equals(t.getId()));
        if(!tabExists) {
            Tab tab = new Tab(name);
            tab.setId(name);
            tab.setContent(new TextArea());
            tabPane.getTabs().add(tab);
            return tab;
        }
        return tabPane.getTabs().stream().filter(t -> name.equals(t.getId())).findFirst().get();
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }


    private void makeBubble(TextField textField, String msg) {
        Image image = new Image(getClass().getResourceAsStream("/Images/error.png"), 15, 15, false, false);

        MenuItem item = new MenuItem(msg, new ImageView(image));
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(item);

        textField.setContextMenu(contextMenu);
        contextMenu.show(textField, Side.RIGHT, 0, 0);
    }

    public boolean detectJarFile(String file) throws IOException {
        // First check if this is a zip file.
        // Then do this.
        ZipFile zip = new ZipFile(file);
        boolean hasManifestEntry = zip.getEntry("META-INF/MANIFEST.MF") != null;
        zip.close();
        return hasManifestEntry;
    }



}
