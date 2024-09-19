import numpy as np
import matplotlib.pyplot as plt
import networkx as nx
import random

def generar_nodos(n):
    return np.random.rand(n, 2) * 100

def calcular_distancia(nodo1, nodo2):
    return np.linalg.norm(nodo1 - nodo2)

def crear_matriz_distancias(nodos):
    n = len(nodos)
    matriz = np.zeros((n, n))
    for i in range(n):
        for j in range(n):
            matriz[i, j] = calcular_distancia(nodos[i], nodos[j])
    return matriz

def geni_insertion(nodos, matriz_distancias):
    recorrido = [0]
    while len(recorrido) < len(nodos):
        candidato = random.choice([i for i in range(len(nodos)) if i not in recorrido])
        mejor_pos = None
        mejor_aumento = float('inf')
        for i in range(len(recorrido)):
            j = (i + 1) % len(recorrido)
            aumento = (matriz_distancias[recorrido[i], candidato] +
                       matriz_distancias[candidato, recorrido[j]] -
                       matriz_distancias[recorrido[i], recorrido[j]])
            if aumento < mejor_aumento:
                mejor_aumento = aumento
                mejor_pos = i + 1
        recorrido.insert(mejor_pos, candidato)
    return recorrido

def dibujar_ruta(nodos, ruta):
    G = nx.Graph()
    for i in range(len(ruta)):
        G.add_edge(ruta[i], ruta[(i + 1) % len(ruta)])

    pos = {i: nodos[i] for i in range(len(nodos))}

    plt.figure(figsize=(8, 8))
    nx.draw(G, pos, with_labels=True, node_color='lightblue', edge_color='gray', node_size=500, font_size=8)
    plt.show()

n_nodos = 100

nodos = generar_nodos(n_nodos)
matriz_distancias = crear_matriz_distancias(nodos)

ruta = geni_insertion(nodos, matriz_distancias)

dibujar_ruta(nodos, ruta)
