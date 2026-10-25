package modelo;

import java.math.BigDecimal;

// Los números del panel principal (issue #17)
public class Dashboard {
    private int sociosActivos;
    private int membresiasVencidas;
    private int vencenEstaSemana;
    private BigDecimal ingresosMes;

    public int getSociosActivos() { return sociosActivos; }
    public void setSociosActivos(int sociosActivos) { this.sociosActivos = sociosActivos; }

    public int getMembresiasVencidas() { return membresiasVencidas; }
    public void setMembresiasVencidas(int membresiasVencidas) { this.membresiasVencidas = membresiasVencidas; }

    public int getVencenEstaSemana() { return vencenEstaSemana; }
    public void setVencenEstaSemana(int vencenEstaSemana) { this.vencenEstaSemana = vencenEstaSemana; }

    public BigDecimal getIngresosMes() { return ingresosMes; }
    public void setIngresosMes(BigDecimal ingresosMes) { this.ingresosMes = ingresosMes; }

    @Override
    public String toString() {
        return "Socios activos: " + sociosActivos
                + " | Vencidos: " + membresiasVencidas
                + " | Vencen esta semana: " + vencenEstaSemana
                + " | Ingresos del mes: $" + ingresosMes;
    }
}
