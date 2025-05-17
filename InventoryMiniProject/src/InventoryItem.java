public class InventoryItem {
    private int itemId;
    private String itemName;
    private float price;
    private char category;
    private boolean inStock;

    public InventoryItem(int itemId, String itemName, float price, char category, boolean inStock) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.inStock = inStock;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public float getPrice() {
        return price;
    }

    public char getCategory() {
        return category;
    }

    public boolean isInStock() {
        return inStock;
    }

    @Override
    public String toString() {
        return itemName;
    }
}
