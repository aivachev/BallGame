import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by dysha_000 on 02.08.2017.
 */
public class Dijkstra {
    public static Integer[][] battlefield;
    static int INF = Integer.MAX_VALUE / 2; // "Бесконечность"
    int vNum; // количество вершин
    int end; //нужная вершина
    static Integer[][] graph; // матрица смежности

    int[] prev = new int [vNum]; // массив предков

    /* Алгоритм Дейкстры*/
    void dijkstra(int start) {
        boolean[] used = new boolean[vNum]; // массив пометок
        int[] dist = new int[vNum]; // массив расстояния.

        fill(prev, -1);
        fill(dist, INF); // устанаавливаем расстояние до всех вершин INF
        dist[start] = 0; // для начальной вершины положим 0

        for (; ; ) {
            int v = -1;
            for (int nv = 0; nv < vNum; nv++) // перебираем вершины
                if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv])) // выбираем самую близкую непомеченную вершину
                    v = nv;
            if (v == -1) break; // ближайшая вершина не найдена
            used[v] = true; //
            for (int nv = 0; nv < vNum; nv++)
                if (!used[nv] && graph[v][nv] < INF) 
                {
                    dist[nv] = min(dist[nv], dist[v] + graph[v][nv]); // улучшаем оценку расстояния
                    prev[nv] = v; // помечаем предка
                }
        }

        /* Восстановление пути*/
        Stack stack = new Stack();
        for (int v = end; v != -1; v = prev[v]) {
            stack.push(v);
        }
        int[] sp = new int[stack.size()];
        for (int i = 0; i < sp.length; i++)
            sp[i] = (Integer) stack.pop() + 1;
    }

    private int min(int i, int i1) {
        if (i < i1) return i;
        else return i1;
    }

    private static void fill(int[] prev, int i) {
        for (int prev1 : prev) {
            prev1 = i;
        }
    }

    public static void main(String[] args) {
        String c;
        Integer countCell; //размерность доски
        Integer countBall; //количество шаров и дырок
        //List<Ball> balls = new ArrayList<>();
        //List<Hole> holes = new ArrayList<>();
        //List<Wall> walls = new ArrayList<>();
        boolean ready = false;


        /*battlefield = new Integer[][]{ // матрица смежности графа. 1 - стена. 0 - свободная клетка
                  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16
                1{0,1,0,0,1,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
                2{1,0,0,0,0,1,0,0,0,0, 0, 0, 0, 0, 0, 0},
                3{0,0,0,0,0,0,1,0,0,0, 0, 0, 0, 0, 0, 0},
                4{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
                5{1,0,0,0,0,0,0,0,-,0, 0, 0, 0, 0, 0, 0},
                6{0,1,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
                7{0,0,1,0,0,0,0,-,0,0, 0, 0, 0, 0, 0, 0},
                8{0,0,0,0,0,0,-,0,0,0, 0, 0, 0, 0, 0, 0},
                9{0,0,0,0,-,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
               10{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
               11{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
               12{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
               13{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
               14{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
               15{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0},
               16{0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0}};*/

        try(BufferedReader in = new BufferedReader(new FileReader("example/ex1.txt")))
        {
            c = in.readLine();
            String strArr[] = c.split(" ");
            Integer intArr[] = new Integer[strArr.length];
            for (int i =0; i < strArr.length; i++)
                intArr[i] = Integer.parseInt(strArr[i]);

            countCell = intArr[0];
            countBall = intArr[1];

            graph = new Integer[countCell*countCell][countCell*countCell];
            fillINF(countCell,battlefield, INF); // устанавливаем расстояние до всех вершин INF
            //заполнение матрицы смежности
            for (int i = 1; i < countCell*countCell; i++) {
                //balls.add(new Ball(intArr[i*2], intArr[i*2+1]));
                if (i - 1 > 0)
                    battlefield[i-1][i] = battlefield[i][i - 1] = 1;
                if (i - countCell > 0)
                    battlefield[i-countCell][i] = battlefield[i][i-countCell]= 1;
                if (i + 1 < countCell)
                    battlefield[i][i + 1] = battlefield[i + 1][i] = 1;
                if (i + countCell < countCell)
                    battlefield[i][i + countCell] = battlefield[i + countCell][i] = 1;
            }

            printMatrix(15, 15);
            //for (int i = countBall*2 + 1; i < countBall; i++) {
            //holes.add(new Hole(intArr[i*2], intArr[i*2+1]));
            //}

            //setTargetPosition(holes.size()); // размещаем лунки
            //setStartPosition(balls.size()); // размещаем шарики
            //ready = true; // можем начинать искать путь
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void fillINF(Integer g, Integer[][] battlefield, int inf) {
        for (int i = 0; i < g*g; i++) {
            for (int j = 0; j < g*g; j++) {
                battlefield[i][j] = inf;
            }
        }
    }

    public static void printMatrix(int x, int y){
        String arr = "";
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                arr += battlefield[i][j] + ",";
            }
            arr += "\n";
        }
        System.out.println(arr);
    }
}
