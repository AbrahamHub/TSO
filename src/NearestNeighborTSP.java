import java.util.Arrays;

public class NearestNeighborTSP {
    private static final int V = 7; // Número de nodos
    private static final int M = Integer.MAX_VALUE; // Valor para representar distancias infinitas
    private static final String[] NODOS = {"A", "B", "C", "F", "R", "T", "S"}; // Letras correspondientes a los nodos

    // Función para resolver el TSP usando la heurística del vecino más cercano
    void tsp(int[][] graph, int start) {
        boolean[] visited = new boolean[V]; // Array de nodos visitados
        int[] path = new int[V + 1]; // Para almacenar la ruta seguida, incluyendo regreso al inicio
        int totalDistance = 0; // Distancia total acumulada
        int currentNode = start; // Nodo actual

        Arrays.fill(visited, false);
        visited[start] = true;
        path[0] = start;

        // Recorremos el grafo visitando cada nodo solo una vez
        for (int i = 1; i < V; i++) {
            int nearestNode = -1;
            int shortestDistance = M;

            // Encontramos el vecino más cercano no visitado
            for (int j = 0; j < V; j++) {
                if (!visited[j] && graph[currentNode][j] != M && graph[currentNode][j] < shortestDistance) {
                    nearestNode = j;
                    shortestDistance = graph[currentNode][j];
                }
            }

            // Validamos si encontramos un nodo válido para visitar
            if (nearestNode == -1) {
                System.out.println("No se encontró un vecino no visitado. Ruta incompleta.");
                return;
            }

            // Visitamos el vecino más cercano
            visited[nearestNode] = true;
            path[i] = nearestNode;
            totalDistance += shortestDistance;
            currentNode = nearestNode;
        }

        // Volvemos al nodo inicial
        if (graph[currentNode][start] != M) {
            totalDistance += graph[currentNode][start];
            path[V] = start; // Cerramos el ciclo

            // Imprimimos la ruta seguida y la distancia total
            System.out.println("Ruta seguida: ");
            for (int i = 0; i <= V; i++) {
                System.out.print(NODOS[path[i]] + (i < V ? " -> " : ""));
            }
            System.out.println("\nDistancia total mínima: " + totalDistance);
        } else {
            System.out.println("No hay una ruta de regreso al nodo inicial. Ruta incompleta.");
        }
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

        NearestNeighborTSP tsp = new NearestNeighborTSP();
        tsp.tsp(graph, 0);  // Comenzamos el TSP desde el nodo A (índice 0)
    }
}
