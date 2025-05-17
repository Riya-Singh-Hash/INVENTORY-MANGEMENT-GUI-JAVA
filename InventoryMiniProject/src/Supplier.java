public class Supplier {
    private int supplierId;
    private String supplierName;
    private float rating;
    private char region;
    private boolean active;

    public Supplier(int supplierId, String supplierName, float rating, char region, boolean active) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.rating = rating;
        this.region = region;
        this.active = active;
    }

    public int getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public float getRating() { return rating; }
    public char getRegion() { return region; }
    public boolean isActive() { return active; }
}
