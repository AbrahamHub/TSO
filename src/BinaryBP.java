import java.util.HashMap;
import java.util.Map;

public class BinaryBP {

    class Triple {
        private int peso;
        private int volumen;
        private int beneficio;

        public Triple(int peso, int volumen, int beneficio) {
            this.peso = peso;
            this.volumen = volumen;
            this.beneficio = beneficio;
        }

        public int getPeso() {
            return peso;
        }

        public int getVolumen() {
            return volumen;
        }

        public int getBeneficio() {
            return beneficio;
        }
    }

    public Triple evaluarConfiguracion(String binario, Map<Integer, Triple> objetos, int capacidadPeso, int capacidadVolumen) {
        int beneficioTotal = 0;
        int pesoTotal = 0;
        int volumenTotal = 0;

        for (int i = 0; i < binario.length(); i++) {
            if (binario.charAt(i) == '1') { 
                beneficioTotal += objetos.get(i + 1).getBeneficio();
                volumenTotal += objetos.get(i + 1).getVolumen();
                pesoTotal += objetos.get(i + 1).getPeso();
            }
            if (pesoTotal > capacidadPeso || volumenTotal > capacidadVolumen) {
                return new Triple(0, 0, 0); // Retornar una configuración inválida
            }
        }

        System.out.println("Configuracion: " + binario + " -> Beneficio: " + beneficioTotal + ", Peso: " + pesoTotal + ", volumen: " + volumenTotal);
        return new Triple(pesoTotal, volumenTotal, beneficioTotal);
    }

    public static void main(String[] args) {
        int n = 6;
        int capacidadPeso = 600;
        int capacidadVolumen = 250;
        int mejorBeneficio = 0;
        int mejorPeso = capacidadPeso * 2;
        int mejorVolumen = capacidadVolumen * 2;
        String mejorBinario = "";

        BinaryBP bp = new BinaryBP();
        Map<Integer, Triple> objetos = new HashMap<>();

        objetos.put(1, bp.new Triple(240, 60, 144));
        objetos.put(2, bp.new Triple(200, 80, 160));
        objetos.put(3, bp.new Triple(250, 80, 160));
        objetos.put(4, bp.new Triple(180, 90, 162));
        objetos.put(5, bp.new Triple(235, 50, 118));
        objetos.put(6, bp.new Triple(190, 80, 152));

        int totalCombinaciones = (int) Math.pow(2, n);

        for (int i = 0; i < totalCombinaciones; i++) {
            String binario = String.format("%06d", Integer.parseInt(Integer.toBinaryString(i)));
            Triple resultado = bp.evaluarConfiguracion(binario, objetos, capacidadPeso, capacidadVolumen);

            if (resultado != null && (resultado.getBeneficio() > mejorBeneficio || 
                (resultado.getBeneficio() == mejorBeneficio && resultado.getPeso() < mejorPeso) || 
                (resultado.getBeneficio() == mejorBeneficio && resultado.getVolumen() < mejorVolumen))) {
                mejorBeneficio = resultado.getBeneficio();
                mejorVolumen = resultado.getVolumen();
                mejorPeso = resultado.getPeso();
                mejorBinario = binario;
            }
        }

        System.out.println("La configuración óptima es: " + mejorBinario + " -> Beneficio: " + mejorBeneficio + ", Peso: " + mejorPeso + ", Volumen: " + mejorVolumen);
    }
}
