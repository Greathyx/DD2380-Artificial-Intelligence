import java.util.ArrayList;

public class Main {
    private int INF = Integer.MAX_VALUE;
    private int x_N = 5;
    private int z_N = 4;
    private int[][] traversability = {
            {1, 1, 1, 1},
            {1, 0, 0, 1},
            {1, 1, 0, 1},
            {1, 0, 0, 1},
            {1, 1, 1, 1}
    };
    ArrayList<Node> nodes = new ArrayList<>();
    private int start_i = 3;
    private int start_j = 1;
    private int goal_i = 1;
    private int goal_j = 1;

    private int n = 1;
    private int[] dis;
    private int[][] c;
    private int[][] cc; //这个数组保证输出路径的时候不会往回走
    private boolean[] s;
    private int v;

    Main() {
        init();
    }

    private int[][] modify_traversability() {
        int[][] traversability_new = new int[x_N * 3][z_N * 3];

        for (int i = 0; i < x_N; i++) {
            for (int j = 0; j < z_N; j++) {
                if (traversability[i][j] == 1) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            traversability_new[i * 3 + k][j * 3 + l] = 1;
                        }
                    }
                } else {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            traversability_new[i * 3 + k][j * 3 + l] = 0;
                        }
                    }
                    if (i - 1 >= 0 && traversability[i - 1][j] == 1) {

                    }
                    if (i + 1 < x_N && traversability_new[i + 1][j] == 1) {

                    }
                    if (j - 1 >= 0 && traversability[i][j - 1] == 1) {

                    }
                    if (j + 1 < z_N && traversability[i][j + 1] == 1) {

                    }
                }
            }
        }

        return traversability_new;
    }

    private void init() {
        nodes.add(new Node(start_i, start_j, n));

        for (int i = 0; i < x_N; i++) {
            for (int j = 0; j < z_N; j++) {
                if (traversability[i][j] == 0) {
//                    System.out.print(i+" "+j);
//                    System.out.println();
                    if (i == start_i && j == start_j)
                        continue;
                    if (i == goal_i && j == goal_j)
                        continue;
                    n++;
                    nodes.add(new Node(i, j, n));
                }
            }
        }

        n += 1;
        nodes.add(new Node(goal_i, goal_j, n));

        for (Node node : nodes) {
            System.out.print(node.getName() + ": (" + node.getI() + ", " + node.getJ() + ")");
            System.out.println();
        }

        // 这里初始化变量
        dis = new int[n + 1];
        s = new boolean[n + 1];
        c = new int[n + 1][n + 1];
        cc = new int[n + 1][n + 1];
        v = 1;

        for (int i = 0; i < n + 1; i++) {
            dis[i] = INF;
            s[i] = false;
            for (int j = 0; j < n + 1; j++) {
                c[i][j] = INF;
                cc[i][j] = INF;
            }
        }

        for (int i = 0; i < x_N; i++) {
            for (int j = 0; j < z_N; j++) {
                if (traversability[i][j] == 0) {
                    int current_node = this.getNode(i, j);
                    if (j + 1 < z_N && traversability[i][j + 1] == 0) {
                        int next_node = this.getNode(i, j + 1);
//                        System.out.println("(" + current_node + ", " + next_node + ")");
                        this.setCandCC(current_node, next_node);
                    }
                    if (i + 1 < x_N && traversability[i + 1][j] == 0) {
                        int next_node = this.getNode(i + 1, j);
//                        System.out.println("(" + current_node + ", " + next_node + ")");
                        this.setCandCC(current_node, next_node);
                    }
                }
            }
        }

        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n + 1; j++) {
                System.out.print(c[i][j] + " ");
            }
            System.out.println();
        }

    }

    /**
     * 这个方法目的是根据i和j获得当前node的编号
     * 如果C#可以用字典，那就方便多啦，可以直接根据value获取key值就好
     * 我这写的相当烂，效率不高
     *
     * @param i int traversability矩阵的横坐标
     * @param j int traversability矩阵的纵坐标
     * @return int
     */
    private int getNode(int i, int j) {
        for (Node node : nodes) {
            if (node.getI() == i && node.getJ() == j)
                return node.getName();
        }
        return 0;
    }


    private void setCandCC(int i, int j) {
        c[i][j] = 1;
        c[j][i] = 1;
        cc[i][j] = 1;
        cc[j][i] = 1;
    }

    public void dijkstra() {
        for (int i = 1; i <= n; i++) {
            dis[i] = c[v][i];
        }
        dis[v] = 0;
        s[v] = true;
        // 在源点找到一个离他最近的点(并且没有被标记过），并且标记该点，然后使用
        for (int i = 2; i <= n; i++) {
            int temp = INF;
            int u = v;
            for (int j = 1; j <= n; j++) {
                if (!s[j] && dis[j] < temp) {
                    u = j;
                    temp = dis[j];
                }
            }
            s[u] = true;
            for (int j = 1; j <= n; j++) {
                if (!s[j] && c[u][j] < INF) {
                    int newdis = dis[u] + c[u][j];
                    if (newdis < dis[j]) { // 经过此k点到达j点的路径是否小于其他到达j点的路径
                        dis[j] = newdis;
                    }
                }
            }
        }

        System.out.println("The shortest distance: " + dis[n]);
    }

    public void printNode() {
        int e = n;
        int u;
        System.out.print(e); //先输出最后一个点
        // 现在开始输出路径；倒着输出
        while (true) {
            //从所有的点中找到与上个点相连的点
            for (u = 1; u <= n; u++) {
                // 如果这个点上个点相连，并且在最短路径上面
                // 那么输出这个点，并且在从这个点寻找路径上的下一个点
                if (dis[e] - dis[u] == cc[e][u] && cc[e][u] != INF) {
                    e = u;
                    System.out.print(" " + e);
                    break;
                }
            }
            if (e == 1)
                break;
        }
    }


    public static void main(String[] args) {
        Main main = new Main();
        main.dijkstra();
        main.printNode();
    }

}
