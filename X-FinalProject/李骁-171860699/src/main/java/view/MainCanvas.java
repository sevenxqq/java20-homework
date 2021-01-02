package view;

import java.util.ArrayList;

import creature.Creature;
import runway.Runway;
import runway.RunwayField;
import card.CardField;
import card.Card;
import card.CreatureCard;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import net.GameClient;
import net.GameServer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.Node;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.scene.input.MouseEvent;

public class MainCanvas extends Canvas {

    public static AnchorPane root; //父面板

    public static RunwayField runwayField; //跑道区

    public static CardField cardField; //卡牌区

    private boolean isRunning = true; //游戏正在运行

    //private Button controller; //按Q退出的控制器

    public static ExecutorService exec; //线程池

    public static Hero myHero; //我方小小英雄

    public static Hero enemyHero; //敌方小小英雄

    public static GameServer server = null;

    public static GameClient client = null;

    private Button btnSetServer;

    private Button btnSetClient;

    private Thread thread = new Thread(new Runnable(){ //画图线程
    
        @Override
        public void run() {
            while (!Thread.interrupted() && isRunning) {
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        draw();
                        update();
                    }
                });
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    //e.printStackTrace();
                }
            }
        }
    });

    public MainCanvas(AnchorPane root, double width, double height) {
        super(width, height);
        this.root = root; //父面板

        //初始化小小英雄
        myHero = new Hero(true);
        enemyHero = new Hero(false);
        myHero.setPosition(0, 10);
        enemyHero.setPosition(700, 10);
        
        //初始化跑道
        runwayField = new RunwayField();

        //线程池
        exec = Executors.newCachedThreadPool();
        //启动本线程
        //exec.execute(thread);

        //初始化卡牌区
        cardField = new CardField(root);
        cardField.removeAllCards();
        cardField.fillCards();

        //初始化启动服务器按钮
        btnSetServer = new Button("设置并启动服务器");
        btnSetServer.setLayoutX(40);
        btnSetServer.setLayoutY(550);
        btnSetServer.setPrefWidth(100);
        btnSetServer.setPrefHeight(40);
        //点击启动服务器
        btnSetServer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                server = new GameServer(28080);
                server.start();
            }
        });
        addToPane(btnSetServer);

        //初始化启动客户端按钮
        btnSetClient = new Button("设置并启动客户端");
        btnSetClient.setLayoutX(200);
        btnSetClient.setLayoutY(550);
        btnSetClient.setPrefWidth(100);
        btnSetClient.setPrefHeight(40);
        //点击启动服务器
        btnSetClient.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client = new GameClient("127.0.0.1", 28080);
                client.start();
            }
        });
        addToPane(btnSetClient);
    }

    public void draw() {

    }

    public void update() {

    }

    public static void addToPane(Node n) {
        root.getChildren().add(n);
    }

    public static void removeFromPane(Node n) {
        root.getChildren().remove(n);
    } 

    public static void iWin() {

    }

    public static void enemyWin() {

    }

    public static void close() {
        System.out.println("窗口关闭");
        exec.shutdown();
        runwayField.getRunways().forEach(runway -> runway.removeAllCreatures()); // 关闭计时器线程
        if (server != null) {
            server.close();
        }
        if (client != null) {
            client.close();
        }
        System.out.println("关完了");
    }

}