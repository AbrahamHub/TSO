import java.util.HashMap;
import java.util.Map;

public class DimensionalBPR {

    class Pair {
        private int peso;
        private int beneficio;
    
        public Pair(int peso, int beneficio){
            this.peso = peso;
            this.beneficio = beneficio;
        }
        public int getPeso(){
            return peso;
        }
        public int getBeneficio(){
            return beneficio;
        }
    }
    public static void main(String[] args) {
        
        DimensionalBPR bp = new DimensionalBPR();

        Map<Integer, Pair> objetos = new HashMap<>();

        objetos.put(1, bp.new Pair(30, 100));
        objetos.put(2, bp.new Pair(10, 70));
        objetos.put(3, bp.new Pair(8, 70));
        objetos.put(4, bp.new Pair(4, 100));
        objetos.put(5, bp.new Pair(7, 40));
        objetos.put(6, bp.new Pair(15, 100));
        objetos.put(7, bp.new Pair(7, 10));
        objetos.put(8, bp.new Pair(20, 50));
        objetos.put(9, bp.new Pair(25, 15));


    }
}
