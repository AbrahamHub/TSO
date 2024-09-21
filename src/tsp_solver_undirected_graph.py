import math
from itertools import combinations

class TspDynamicProgrammingIterative:
    def __init__(self, distance, start=0):
        self.N = len(distance)
        if self.N <= 2:
            raise ValueError("N <= 2 not yet supported.")
        if self.N != len(distance[0]):
            raise ValueError("Matrix must be square (n x n).")
        if start < 0 or start >= self.N:
            raise ValueError("Invalid start node.")
        if self.N > 32:
            raise ValueError("Matrix too large! A matrix that size requires too much computation.")

        self.start = start
        self.distance = distance
        self.tour = []
        self.minTourCost = float('inf')
        self.ranSolver = False

    def get_tour(self):
        if not self.ranSolver:
            self.solve()
        return self.tour

    def get_tour_cost(self):
        if not self.ranSolver:
            self.solve()
        return self.minTourCost

    def solve(self):
        if self.ranSolver:
            return

        END_STATE = (1 << self.N) - 1
        memo = [[None] * (1 << self.N) for _ in range(self.N)]

        # Add all outgoing edges from the starting node to memo table
        for end in range(self.N):
            if end == self.start:
                continue
            memo[end][(1 << self.start) | (1 << end)] = self.distance[self.start][end]

        # Iterate subsets and solve
        for r in range(3, self.N + 1):
            for subset in self.combinations(r, self.N):
                if self.not_in(self.start, subset):
                    continue
                for next in range(self.N):
                    if next == self.start or self.not_in(next, subset):
                        continue
                    subset_without_next = subset ^ (1 << next)
                    min_dist = float('inf')
                    for end in range(self.N):
                        if end == self.start or end == next or self.not_in(end, subset):
                            continue
                        new_distance = memo[end][subset_without_next] + self.distance[end][next]
                        if new_distance < min_dist:
                            min_dist = new_distance
                    memo[next][subset] = min_dist

        # Connect tour back to starting node and minimize cost
        for i in range(self.N):
            if i == self.start:
                continue
            tour_cost = memo[i][END_STATE] + self.distance[i][self.start]
            if tour_cost < self.minTourCost:
                self.minTourCost = tour_cost

        # Reconstruct TSP path from memo table
        lastIndex = self.start
        state = END_STATE
        self.tour.append(self.start)

        for i in range(1, self.N):
            bestIndex = -1
            bestDist = float('inf')
            for j in range(self.N):
                if j == self.start or self.not_in(j, state):
                    continue
                newDist = memo[j][state] + self.distance[j][lastIndex]
                if newDist < bestDist:
                    bestIndex = j
                    bestDist = newDist
            self.tour.append(bestIndex)
            state ^= (1 << bestIndex)
            lastIndex = bestIndex

        self.tour.append(self.start)
        self.tour.reverse()

        self.ranSolver = True

    def not_in(self, elem, subset):
        return (1 << elem) & subset == 0

    def combinations(self, r, n):
        return [sum(1 << i for i in comb) for comb in combinations(range(n), r)]

def numbers_to_letters(number_list):
    # Crear una lista de letras del alfabeto (mayúsculas)
    alphabet = [chr(i) for i in range(ord('A'), ord('Z') + 1)]
    
    # Convertir cada número a su letra correspondiente
    letters_list = [alphabet[num] for num in number_list if 0 <= num < len(alphabet)]
    
    return letters_list

if __name__ == "__main__":

    # Define a large distance to represent no direct connection
    INF = 10000

    # Create adjacency matrix 
    distance_matrix = [ # (This graph was presented in the first TSO class)
        [INF,   3,   5,   2, INF, INF, INF,  10],
        [  3, INF,   5,   8,   4, INF,   6,   6],
        [  5,   5, INF, INF,   1,   7,   9, INF],
        [  2,   8, INF, INF,  12, INF, INF,  14],
        [INF,   4,   1,  12, INF, INF,  15, INF],
        [INF, INF,   7, INF, INF, INF, INF,   9],
        [INF,   6,   9, INF,  15, INF, INF,   3],
        [ 10,   6, INF,  14, INF,   9,   3, INF]
    ]

    start_node = 0
    solver = TspDynamicProgrammingIterative(distance_matrix, start_node)

    # Prints: [0, 3, 2, 4, 1, 5, 0]
    print("Tour:", solver.get_tour())

    # Prints: []
    print("Tour(Letters):", numbers_to_letters(solver.get_tour()))

    # Print: 42.0
    print("Tour cost:", solver.get_tour_cost())


