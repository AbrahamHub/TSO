
import java.util.HashMap;
import java.util.Map;


public class BinaryBP {

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


    public Pair evaluarConfiguracion(String binario, Map<Integer, Pair> objetos, int capacidad) {
        int beneficioTotal = 0;
        int pesoTotal = 0;
        

        for (int i = 0; i < binario.length(); i++) {
            if (binario.charAt(i) == '1') { 
                beneficioTotal += objetos.get(i+1).getBeneficio();
                pesoTotal += objetos.get(i+1).getPeso();
                //System.out.println(i+1);
                
            }
            if (pesoTotal > capacidad) {
                break;
            }
        }

        if (pesoTotal <= capacidad) {
            System.out.println("Configuracion: " + binario + " -> Beneficio: " + beneficioTotal + ", Peso: " + pesoTotal);
            return new Pair(pesoTotal, beneficioTotal);
        } else {
            return new Pair(0, 0);
        }
    }
    public static void main(String[] args) {

        int n = 9;
        int capacidad = 60;
        int mejorBeneficio = 0;
        int mejorPeso = capacidad *2;
        String mejorBinario = "";

        BinaryBP bp = new BinaryBP();

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

        int totalCombinaciones = (int) Math.pow(2, n);

        for (int i = 0; i < totalCombinaciones; i++) {
            
            String binario = String.format("%09d", Integer.parseInt(Integer.toBinaryString(i)));

            Pair resultado = bp.evaluarConfiguracion(binario, objetos, capacidad);

            if (resultado.getBeneficio() > mejorBeneficio || 
                (resultado.getBeneficio() == mejorBeneficio && resultado.getPeso() < mejorPeso)) {
                mejorBeneficio = resultado.getBeneficio();
                mejorPeso = resultado.getPeso();
                mejorBinario = binario;
            }
        }

        System.out.println("La configuración óptima es: " + mejorBinario + " -> Beneficio: " + mejorBeneficio + ", Peso: " + mejorPeso);

    }

    
}
