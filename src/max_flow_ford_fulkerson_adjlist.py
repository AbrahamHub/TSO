# Función para realizar DFS y encontrar un camino desde la fuente hasta el sumidero
def dfs(graph, residual_graph, source, sink, parent):
    visited = set()  # Conjunto para nodos visitados
    stack = [source]  # Pila para recorrer el grafo

    while stack:
        u = stack.pop()

        # Recorremos las aristas desde el nodo u
        for v, capacity in residual_graph[u]:
            # Si el nodo v no ha sido visitado y hay capacidad disponible
            if v not in visited and capacity > 0:
                parent[v] = u  # Guardamos el nodo predecesor
                visited.add(v)

                # Si llegamos al nodo sumidero, hemos encontrado un camino
                if v == sink:
                    return True

                stack.append(v)
    
    # No se encontró un camino hasta el nodo sumidero
    return False

# Función para ejecutar el algoritmo de Ford-Fulkerson
def ford_fulkerson(graph, source, sink):
    # Creamos el grafo residual inicializando las capacidades de las aristas
    residual_graph = {u: list(edges) for u, edges in graph.items()}

    # El flujo máximo inicial es 0
    max_flow = 0

    # Este diccionario almacenará el camino aumentante (el predecesor de cada nodo)
    parent = {}

    # Mientras haya un camino aumentante desde la fuente hasta el sumidero
    while dfs(graph, residual_graph, source, sink, parent):
        # Encontramos la capacidad mínima a lo largo del camino aumentante
        path_flow = float('Inf')
        v = sink

        while v != source:
            u = parent[v]
            # Buscar la capacidad de u -> v en el grafo residual
            for dest, capacity in residual_graph[u]:
                if dest == v:
                    path_flow = min(path_flow, capacity)
                    break
            v = u
        
        # Actualizamos las capacidades residuales (flujo hacia adelante y hacia atrás)
        v = sink
        while v != source:
            u = parent[v]

            # Reducimos la capacidad de u -> v en el grafo residual
            for i, (dest, capacity) in enumerate(residual_graph[u]):
                if dest == v:
                    residual_graph[u][i] = (dest, capacity - path_flow)

            # Aumentamos la capacidad de v -> u (flujo inverso)
            for i, (dest, capacity) in enumerate(residual_graph[v]):
                if dest == u:
                    residual_graph[v][i] = (dest, capacity + path_flow)
                    break
            else:
                # Si no existe, creamos una nueva arista de flujo inverso
                residual_graph[v].append((u, path_flow))

            v = u

        # Sumamos el flujo de este camino aumentante al flujo total
        max_flow += path_flow

    return max_flow

# Definición del grafo usando lista de adyacencia
graph = {
    1: [(2, 13), (3, 10), (4, 10)], # Nodo Inicial (s)
    2: [(5, 24)],
    3: [(2, 5), (4, 15), (7, 7)],
    4: [(7, 15)],
    5: [(8, 9), (6, 1)],
    6: [(7, 6), (8, 13)],
    7: [(8, 16)],
    8: [] # Nodo destino (t)
}

# Nodo fuente (source) es 0, y el sumidero (sink) es 5
source = 1
sink = 8

# Ejecutamos el algoritmo de Ford-Fulkerson
max_flow = ford_fulkerson(graph, source, sink)
print(f"El flujo máximo desde el nodo {source} al nodo {sink} es: {max_flow}")
