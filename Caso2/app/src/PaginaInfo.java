package app.src;

class PaginaInfo {
    private volatile boolean modificada;
    private volatile boolean accedida;
    
    public PaginaInfo(boolean modificada, boolean accedida) {
        this.modificada = modificada;
        this.accedida = accedida;
    }
    
    public synchronized void acceder(boolean esEscritura) {
        accedida = true;
        if (esEscritura) modificada = true;
    }
    
    public synchronized void resetAccedida() {
        accedida = false;
    }
    
    public boolean isModificada() {
        return modificada;
    }
    
    public boolean isAccedida() {
        return accedida;
    }
}