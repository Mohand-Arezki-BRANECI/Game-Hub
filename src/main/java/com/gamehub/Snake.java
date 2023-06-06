package com.gamehub;


import javafx.animation.Animation;
        import javafx.animation.KeyFrame;
        import javafx.animation.Timeline;
        import javafx.application.Application;
        import java.awt.Point;
        import javafx.scene.Group;
        import javafx.scene.Scene;
        import javafx.scene.canvas.Canvas;
        import javafx.scene.canvas.GraphicsContext;
        import javafx.scene.image.Image;
        import javafx.scene.input.KeyCode;
        import javafx.scene.paint.Color;
        import javafx.scene.text.Font;
        import javafx.stage.Stage;
        import javafx.util.Duration;
        import java.io.IOException;
        import java.net.MalformedURLException;
        import java.nio.file.Path;
        import java.util.ArrayList;
        import java.util.List;

public class Snake extends Application {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int ROWS = 20;
    private static final int COLUMNS = 20;
    private static final int SQUARE_SIZE = 30;

    // Random List of images
    private static final String[] FOOD = {"orange.png", "apple.png", "watermelon.png", "tomato.png",
            "coconut.png", "cherry.png", "peach.png", "berry.png", "pomegranate.png"};

    // for movements
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    // GraphicsContext : it's used to draw calls to a Canvas using a buffer
    private GraphicsContext graphicsContext;
    // List of 2D Point that make the snake's body
    private final List<Point> snakeBody = new ArrayList<>();
    private Point snakeHead;
    private javafx.scene.image.Image foodIcon;
    private int foodX;
    private int foodY;
    private int score;
    private boolean gameOver;
    private int currentDirection;

    private final Color lightTileColor = javafx.scene.paint.Color.web("AAD751");
    private final Color darkTileColor = javafx.scene.paint.Color.web("A2D149");

    private final Color snakeColor = javafx.scene.paint.Color.web("4674E9");

    private void run(GraphicsContext graphicsContext) {
        if (gameOver) {
            graphicsContext.setFill(javafx.scene.paint.Color.RED);
            graphicsContext.setFont(new javafx.scene.text.Font(70));
            graphicsContext.fillText("Game Over", WIDTH / 4.5, HEIGHT / 2);
            return;
        }

        drawBackground(graphicsContext);
        drawFood(graphicsContext);
        drawSnake(graphicsContext);
        drawScore();
        eatFood();

        // This makes the snake's body follow it's head
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;

        }

        switch (currentDirection) {
            case RIGHT -> moveRight();
            case LEFT -> moveLeft();
            case UP -> moveUp();
            case DOWN -> moveDown();
        }

        gameOver();


    }


    private void drawBackground(GraphicsContext graphicsContext) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if ((row + col) % 2 == 0) {
                    graphicsContext.setFill(lightTileColor);
                } else {
                    graphicsContext.setFill(darkTileColor);
                }
                graphicsContext.fillRect(row * SQUARE_SIZE, col * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void generateFood() throws MalformedURLException {
        String defaultPath = "./src/main/java/com/gamehub/images/";

        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }
            String imagePath = defaultPath + FOOD[(int) (Math.random() * FOOD.length)];

            foodIcon = new Image(Path.of(imagePath).toUri().toURL().toString());

            break;
        }
    }

    private void drawFood(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(foodIcon, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawSnake(GraphicsContext graphicsContext) {
        graphicsContext.setFill(snakeColor);
        graphicsContext.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++) {
            graphicsContext.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }

    }

    private void moveRight() {
        snakeHead.x += 1;
    }

    private void moveLeft() {
        snakeHead.x -= 1;

    }

    private void moveUp() {
        snakeHead.y -= 1;

    }

    private void moveDown() {
        snakeHead.y += 1;

    }

    private void gameOver() {
        // When the snake touches the edges
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH || snakeHead.y * SQUARE_SIZE >= HEIGHT) {
            gameOver = true;
        }
        // When the snake's head touches it's body
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.y == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }

        }

    }

    private void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point());
            try {
                generateFood();
                score += 5;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void drawScore() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(20));
        graphicsContext.fillText("Score : " + score, 10, 28);

    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Snake");
        Group root = new Group();
        javafx.scene.canvas.Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, 10));
        }
        snakeHead = snakeBody.get(0);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), actionEvent -> run(graphicsContext)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        graphicsContext = canvas.getGraphicsContext2D();
        scene.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if (keyCode == KeyCode.RIGHT) {
                if (currentDirection != LEFT) {
                    currentDirection = RIGHT;
                }
            } else if (keyCode == KeyCode.LEFT) {
                if (currentDirection != RIGHT) {
                    currentDirection = LEFT;
                }
            } else if (keyCode == KeyCode.UP) {
                if (currentDirection != DOWN) {
                    currentDirection = UP;
                }
            } else if (keyCode == KeyCode.DOWN) {
                if (currentDirection != UP) {
                    currentDirection = DOWN;
                }
            }

        });

        generateFood();
        run(graphicsContext);
        stage.show();


    }


    public static void main(String[] args) {
        launch();
    }


}


