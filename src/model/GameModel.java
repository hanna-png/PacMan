package model;

import model.entities.Character;
import model.entities.Ghost;
import model.entities.Item;
import model.entities.PacMan;
import model.enums.Cell;
import model.enums.Direction;
import model.enums.GhostColor;
import model.enums.ItemType;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameModel implements MazeReader {
    private int width;
    private int height;
    private final String mazeFile = "resources/game/board/maze100x100.txt";
    private Cell[][] board;
    private volatile int dotCount;
    private PacMan pacman;
    private List<Ghost> ghosts;
    private volatile boolean ghostsFreezed;
    private volatile int score;
    private volatile int lives;
    private volatile int time;
    private volatile boolean end = false;
    private volatile boolean returnedToMenu = false;
    private List<Item> items;
    private Random rand = new Random();

    public GameModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = createBoard(width, height);
        items = new ArrayList<>();
        lives = 3;
        putPacmanOnBoard();
        putGhostsOnBoard();
        removeUnreachable(board, pacman.getCurrentR(), pacman.getCurrentC());
        recalculateDots();
        startEndChecker();
        startTimer();
        startLiveChecker();


    }

    private void recalculateDots() {
        dotCount = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] == Cell.DOT) {
                    dotCount++;
                }
            }
        }
        System.out.println("Number of dots: " + dotCount);
    }


    public void startEndChecker() {
        Thread thread = new Thread(() -> {
            while (!end) {
                synchronized (GameModel.this) {
                    if (lives <= 0 || dotCount <= 0) {
                        end = true;
                        break;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void returnToMenu(){
        returnedToMenu = true;
        lives = 0 ;
    }


    private void removeUnreachable(Cell[][] board, int startRow, int startCol) {
        int tall = board.length;
        int wide = board[0].length;
        boolean madeChange;

        do {
            boolean[][] seen = new boolean[tall][wide];
            Queue<Point> traveler = new ArrayDeque<>();
            if (board[startRow][startCol] != Cell.WALL) {
                seen[startRow][startCol] = true;
                traveler.add(new Point(startRow, startCol));
            }

            int[] drx = { 1, -1, 0, 0 };
            int[] dry = { 0, 0, 1, -1 };
            while (!traveler.isEmpty()) {
                Point spot = traveler.poll();
                int r0 = spot.x, c0 = spot.y;
                for (int d = 0; d < 4; d++) {
                    int nr = r0 + drx[d], nc = c0 + dry[d];
                    if (nr < 0 || nr >= tall || nc < 0 || nc >= wide) {
                        continue;
                    }
                    if (seen[nr][nc]) {
                        continue;
                    }
                    if (board[nr][nc] == Cell.WALL) {
                        continue;
                    }
                    seen[nr][nc] = true;
                    traveler.add(new Point(nr, nc));
                }
            }

            madeChange = false;
            List<Point> holes = new ArrayList<>();

            for (int r1 = 0; r1 < tall; r1++) {
                for (int c1 = 0; c1 < wide; c1++) {
                    if (board[r1][c1] != Cell.WALL) {
                        continue;
                    }

                    boolean nextToSeen = false;
                    boolean nextToUnseenNonWall = false;
                    for (int d = 0; d < 4; d++) {
                        int nr = r1 + drx[d], nc = c1 + dry[d];
                        if (nr < 0 || nr >= tall || nc < 0 || nc >= wide) {
                            continue;
                        }
                        if (seen[nr][nc]) {
                            nextToSeen = true;
                        } else {
                            if (board[nr][nc] != Cell.WALL) {
                                nextToUnseenNonWall = true;
                            }
                        }
                    }
                    if (nextToSeen && nextToUnseenNonWall) {
                        holes.add(new Point(r1, c1));
                    }
                }
            }

            if (!holes.isEmpty()) {
                madeChange = true;
                for (Point hole : holes) {
                    board[hole.x][hole.y] = Cell.DOT;
                }
            }
        } while (madeChange);
    }


    public void startTimer() {
        Thread thread = new Thread(() -> {
            while (!isEnd()) {
                time++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }


    public void startLiveChecker() {
        Thread thread = new Thread(() -> {
            while (true) {
                synchronized (GameModel.this) {
                    if (end) {
                        break;
                    }
                }


                boolean ghostEncountered = false;
                synchronized (GameModel.this) {
                    for (Ghost g : ghosts) {
                        if (pacman.getCurrentC() == g.getCurrentC() &&
                                pacman.getCurrentR() == g.getCurrentR()) {
                            ghostEncountered = true;
                            break;
                        }
                    }

                    if (ghostEncountered) {
                        lives--;

                        pacman.setCurrentC(pacman.getSpawnC());
                        pacman.setCurrentR(pacman.getSpawnR());
                    }
                }

            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    public synchronized void freeze() {
        Thread thread = new Thread(() -> {
            ghostsFreezed = true;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ghostsFreezed = false;
        });
        thread.setDaemon(true);
        thread.start();
    }

    public synchronized void speedPacman() {
        Thread thread = new Thread(() -> {
            pacman.setSpeed(1);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pacman.setSpeed(5);
        });
        thread.setDaemon(true);
        thread.start();
    }

    public synchronized void slowGhosts() {
        Thread thread = new Thread(() -> {


            for (var g : getGhosts()) {

                g.setSpeed(15);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (var g : getGhosts()) {

                g.setSpeed(5);
            }

        });
        thread.setDaemon(true);
        thread.start();
    }

    public synchronized boolean isEnd() {
        return end;
    }

    public synchronized void incrementScore(int num) {
        score += num;
    }

    public synchronized void incrementLives(int num) {
        lives += num;
    }


    public Cell[][] createBoard(int width, int height) {
        String[] fullMaze = readMazeFromFile(mazeFile);
        Cell[][] board = new Cell[height][width];

        int rowStart = (100 - height) / 2;
        int colStart = (100 - width) / 2;

        for (int i = 0; i < height; i++) {
            String mazeRow = fullMaze[rowStart + i];
            for (int j = 0; j < width; j++) {
                char ch = mazeRow.charAt(colStart + j);
                Cell cell = switch (ch) {
                    case 'w' -> Cell.WALL;
                    case '.' -> Cell.DOT;
                    default -> Cell.EMPTY;
                };

                if (i == 0 || j == 0 || i == height - 1 || j == width - 1) {
                    cell = Cell.WALL;
                }

                board[i][j] = cell;


            }
        }

        return board;
    }

    public void putPacmanOnBoard() {

        pacman = new PacMan();
        pacman.setSpawnDirection(Direction.RIGHT);
        pacman.setCurrentDirection(Direction.RIGHT);
        pacman.setCurrentFrame(1);
        pacman.setSpeed(4);

        int spawnR = 1;
        int spawnC = 1;
        do {
            spawnC++;
        } while (board[spawnR][spawnC] == Cell.WALL);

        pacman.setSpawnR(spawnR);
        pacman.setSpawnC(spawnC);
        pacman.setCurrentR(spawnR);
        pacman.setCurrentC(spawnC);

    }

    public void putGhostsOnBoard() {
        ghosts = new ArrayList<>();
        ghosts.add(new Ghost(GhostColor.PINK));
        ghosts.add(new Ghost(GhostColor.BLUE));
        ghosts.add(new Ghost(GhostColor.RED));
        ghosts.add(new Ghost(GhostColor.ORANGE));

        int middleR = height / 2;
        int middleC = width / 2;
        List<Point> spawnPoints = calculateSpawnPointsForGhosts(middleR, middleC);


        for (int i = 0; i < ghosts.size(); i++) {

            Point p = spawnPoints.get(i);
            ghosts.get(i).setCurrentR(p.x);
            ghosts.get(i).setCurrentC(p.y);
            ghosts.get(i).setCurrentDirection(Direction.values()[i]);
            ghosts.get(i).setCurrentFrame(1);
            ghosts.get(i).setSpeed(4);

        }


    }

    private List<Point> calculateSpawnPointsForGhosts(int middleR, int middleC) {
        List<Point> spawnPoints = new ArrayList<>();
        int rad = 0;

        while (spawnPoints.size() < ghosts.size()) {
            for (int dr = -rad; dr <= rad && spawnPoints.size() < ghosts.size(); dr++) {
                for (int dc = -rad; dc <= rad && spawnPoints.size() < ghosts.size(); dc++) {
                    if (Math.max(Math.abs(dr), Math.abs(dc)) != rad) {
                        continue;
                    }
                    int r = middleR + dr;
                    int c = middleC + dc;
                    if (r < 0 || r >= height || c < 0 || c >= width) {
                        continue;
                    }
                    if (board[r][c] == Cell.WALL) {
                        continue;
                    }
                    Point p = new Point(r, c);
                    if (!spawnPoints.contains(p)) {
                        spawnPoints.add(p);
                    }
                }
            }
            rad++;
        }
        return spawnPoints;
    }

    public synchronized void movePacman(Direction dir) {
        int prevR = pacman.getCurrentR();
        int prevC = pacman.getCurrentC();
        int newR = pacman.getCurrentR() + dir.directionRow;
        int newC = pacman.getCurrentC() + dir.directionColumn;

        if (newR >= 0 && newR < board.length
                && newC >= 0 && newC < board[0].length) {


            if (board[newR][newC] != Cell.WALL) {
                if (board[prevR][prevC] == Cell.DOT) {
                    board[prevR][prevC] = Cell.EMPTY;
                    dotCount--;
                    System.out.println("Number of dots: " + dotCount);
                    score++;
                }
                pacman.setCurrentR(newR);
                pacman.setCurrentC(newC);
            }
        }

        pacman.setCurrentDirection(dir);
    }

    public synchronized void moveGhost(Ghost ghost) {
        int oldR = ghost.getCurrentR();
        int oldC = ghost.getCurrentC();
        int newR = oldR + ghost.getCurrentDirection().directionRow;
        int newC = oldC + ghost.getCurrentDirection().directionColumn;
        if (newR >= 0 && newR < board.length
                && newC >= 0 && newC < board[0].length) {

            Direction opDir = null;
            switch (ghost.getCurrentDirection()) {
                case UP -> opDir = Direction.DOWN;
                case DOWN -> opDir = Direction.UP;
                case LEFT -> opDir = Direction.RIGHT;
                case RIGHT -> opDir = Direction.LEFT;
            }

            List<Direction> possibleDir = new ArrayList<>();
            for (Direction checkDir : Direction.values()) {
                int nxtRow = ghost.getCurrentR() + checkDir.directionRow;
                int nxtCol = ghost.getCurrentC() + checkDir.directionColumn;
                if (nxtRow >= 0 && nxtRow < board.length
                        && nxtCol >= 0 && nxtCol < board[0].length
                        && board[nxtRow][nxtCol] != Cell.WALL) {
                    boolean anotherGhostIsOnTheCell = false;
                    for (Ghost other : ghosts) {
                        if (other != ghost
                                && other.getCurrentR() == nxtRow
                                && other.getCurrentC() == nxtCol) {
                            anotherGhostIsOnTheCell = true;
                            break;
                        }
                    }
                    if (!anotherGhostIsOnTheCell) {
                        possibleDir.add(checkDir);
                    }
                }
            }

            if (possibleDir.size() > 1) {
                possibleDir.remove(opDir);
            }

            if (!possibleDir.isEmpty()) {
                Direction randomDir = possibleDir.get(
                        rand.nextInt(possibleDir.size())
                );
                int finalRow = ghost.getCurrentR() + randomDir.directionRow;
                int finalCol = ghost.getCurrentC() + randomDir.directionColumn;
                ghost.setCurrentDirection(randomDir);
                ghost.setCurrentR(finalRow);
                ghost.setCurrentC(finalCol);
            }
        }
    }

    public synchronized void generateItem(int row, int column) {
        for (Item existing : items) {
            if (existing.getRow() == row && existing.getCol() == column) {
                return;
            }
        }
        for (Ghost g : ghosts) {
            if (g.getCurrentR() == row && g.getCurrentC() == column) {
                return;
            }
        }
        if (rand.nextInt(4) == 1) {
            ItemType type = ItemType.values()[rand.nextInt(5)];
            items.add(new Item(row, column, type));
        }

    }


    public String[] readMazeFromFile(String fileName) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            System.err.println("Unable to read file " + e.getMessage());
            return null;
        }
        return lines.toArray(new String[0]);
    }


    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public synchronized List<Ghost> getGhosts() {
        return ghosts;
    }

    public synchronized void setGhosts(List<Ghost> ghosts) {
        this.ghosts = ghosts;
    }

    public synchronized Character getPacman() {
        return pacman;
    }

    public synchronized void setPacman(PacMan pacman) {
        this.pacman = pacman;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public boolean isGhostsFreezed() {
        return ghostsFreezed;
    }

    public void setGhostsFreezed(boolean ghostsFreezed) {
        this.ghostsFreezed = ghostsFreezed;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isReturnedToMenu() {
        return returnedToMenu;
    }

    public synchronized void setReturnedToMenu(boolean returnedToMenu) {
        this.returnedToMenu = returnedToMenu;
    }
}

