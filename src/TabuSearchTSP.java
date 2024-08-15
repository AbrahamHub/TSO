import java.util.Arrays;
import java.util.Scanner;

public class TabuSearchTSP {
    private static final int V = 7; // Número de nodos
    private static final int M = Integer.MAX_VALUE; // Valor para representar distancias infinitas
    private static final String[] NODOS = {"A", "B", "C", "F", "R", "T", "S"}; // Letras correspondientes a los nodos
    private static final int TABU_LIST_SIZE = 10; // Tamaño de la lista tabú
    private static final int MAX_ITERATIONS = 5000; // Incrementar el número máximo de iteraciones

    private int[][] graph; // Matriz de adyacencia

    // Constructor para inicializar la matriz de adyacencia
    public TabuSearchTSP(int[][] graph) {
        this.graph = graph;
    }

    // Función para calcular la distancia total de un camino
    private int calculateTotalDistance(int[] path) {
        int totalDistance = 0;
        for (int i = 0; i < V; i++) {
            int distance = graph[path[i]][path[(i + 1) % V]];
            if (distance == M) {
                return Integer.MAX_VALUE; // Retornar distancia máxima si se encuentra un valor infinito
            }
            totalDistance += distance;
        }
        return totalDistance;
    }

    // Función para generar una solución inicial usando un enfoque heurístico
    private int[] generateInitialSolution(int startNode) {
        int[] path = new int[V];
        boolean[] visited = new boolean[V];
        int currentIndex = 0;

        path[currentIndex++] = startNode;
        visited[startNode] = true;

        for (int i = 1; i < V; i++) {
            int nextNode = -1;
            int minDistance = Integer.MAX_VALUE;

            for (int j = 0; j < V; j++) {
                if (!visited[j] && graph[path[currentIndex - 1]][j] < minDistance) {
                    nextNode = j;
                    minDistance = graph[path[currentIndex - 1]][j];
                }
            }

            if (nextNode == -1) {
                // Si no se encuentra un próximo nodo válido, salimos del bucle
                break;
            }

            path[currentIndex++] = nextNode;
            visited[nextNode] = true;
        }

        // En caso de que haya nodos no visitados, agregamos el nodo de inicio a la ruta
        if (currentIndex < V) {
            for (int i = 0; i < V; i++) {
                if (!visited[i]) {
                    path[currentIndex++] = i;
                }
            }
        }

        // Cerrar el ciclo
        int[] initialPath = new int[V + 1];
        System.arraycopy(path, 0, initialPath, 0, V);
        initialPath[V] = startNode;
        return initialPath;
    }

    // Función para aplicar una operación de intercambio 2-opt
    private int[] apply2OptMove(int[] path, int i, int j) {
        int[] newPath = Arrays.copyOf(path, path.length);
        while (i < j) {
            int temp = newPath[i];
            newPath[i] = newPath[j];
            newPath[j] = temp;
            i++;
            j--;
        }
        return newPath;
    }

    // Función para encontrar la mejor solución vecina
    private int[] findBestNeighbor(int[] currentPath, boolean[][] tabuList) {
        int[] bestNeighbor = Arrays.copyOf(currentPath, currentPath.length);
        int bestDistance = Integer.MAX_VALUE;

        for (int i = 1; i < V - 1; i++) {
            for (int j = i + 1; j < V; j++) {
                if (j - i == 1) continue; // Skip adjacent edges

                int[] neighbor = apply2OptMove(currentPath, i, j);
                int neighborDistance = calculateTotalDistance(neighbor);

                // Validar que la distancia no sea infinita y que no esté en la lista tabú
                if (neighborDistance < bestDistance && neighborDistance != Integer.MAX_VALUE && !tabuList[i][j]) {
                    bestNeighbor = neighbor;
                    bestDistance = neighborDistance;
                }
            }
        }
        return bestNeighbor;
    }

    // Función para actualizar la lista tabú
    private void updateTabuList(boolean[][] tabuList, int[] path) {
        // Inicializar la lista tabú con valores false
        for (int i = 0; i < V; i++) {
            Arrays.fill(tabuList[i], false);
        }

        // Marcar los movimientos actuales como tabú
        for (int i = 0; i < V; i++) {
            int current = path[i];
            int next = path[(i + 1) % V];
            if (current != next && graph[current][next] != M) { // Asegurarse de que no se está marcando un nodo consigo mismo y que la distancia es válida
                tabuList[current][next] = true;
                tabuList[next][current] = true; // También marcar el movimiento inverso como tabú
            }
        }
    }

    // Función para ejecutar la búsqueda tabú
    public void tabuSearch(int startNode) {
        int[] currentPath = generateInitialSolution(startNode);
        int[] bestPath = Arrays.copyOf(currentPath, currentPath.length);
        int bestDistance = calculateTotalDistance(bestPath);
        int currentDistance = bestDistance;

        boolean[][] tabuList = new boolean[V][V];

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            int[] newPath = findBestNeighbor(currentPath, tabuList);
            currentDistance = calculateTotalDistance(newPath);

            if (currentDistance < bestDistance && currentDistance != Integer.MAX_VALUE) {
                bestPath = Arrays.copyOf(newPath, newPath.length);
                bestDistance = currentDistance;
            }

            currentPath = Arrays.copyOf(newPath, newPath.length);
            updateTabuList(tabuList, currentPath); // Actualiza la lista tabú con la ruta actual

            System.out.println("Iteración " + iteration + ": Distancia actual = " + currentDistance);
        }

        // Imprimimos la ruta óptima y la distancia total
        System.out.println("Ruta óptima: ");
        for (int i = 0; i < V; i++) {
            System.out.print(NODOS[bestPath[i]] + " -> ");
        }
        System.out.println(NODOS[bestPath[0]]); // Cierra el ciclo
        System.out.println("Distancia total mínima: " + bestDistance);
    }

    public static void main(String[] args) {
        // Matriz de adyacencia (A=0, B=1, C=2, F=3, R=4, T=5, S=6)
        int[][] graph = {
                { 0, 9, 8, 12, 25, M, M },  // A
                { 8, 0, M, M, M, 21, 6 },    // B
                { 10, M, 0, 9, 10, 14, 6 }, // C
                { 15, M, 8, 0, 7, 16, 18 },   // F
                { 27, M, 11, 5, 0, 8, M },  // R
                { M, 23, 13, 14, 8, 0, 4 },  // T
                { M, 8, 7, 15, M, 8, 0 }    // S
        };

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nodo de partida (0-6): ");
        int startNode = scanner.nextInt();
        scanner.close();

        // Verificar que el nodo de inicio esté dentro del rango válido
        if (startNode < 0 || startNode >= V) {
            System.out.println("Nodo de inicio inválido. Debe estar en el rango de 0 a " + (V - 1));
            return;
        }

        TabuSearchTSP tsp = new TabuSearchTSP(graph);
        tsp.tabuSearch(startNode);
    }
}
