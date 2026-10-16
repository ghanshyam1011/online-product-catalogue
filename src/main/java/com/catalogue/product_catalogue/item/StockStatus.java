package com.catalogue.product_catalogue.item;

public enum StockStatus {
    IN_STOCK("In stock", "green", "", ""),
    LOW_STOCK("Low stock", "amber", "\u26A0", "Your stock is getting low"),
    OUT_OF_STOCK("Out of stock", "red", "\u26D4", "Your stock is empty");

    private final String label;
    private final String css;
    private final String icon;
    private final String alertMessage;

    StockStatus(String label, String css, String icon, String alertMessage) {
        this.label = label;
        this.css = css;
        this.icon = icon;
        this.alertMessage = alertMessage;
    }

    public static StockStatus of(int quantity, int threshold) {
        if (quantity <= 0) {
            return OUT_OF_STOCK;
        }
        if (quantity <= threshold) {
            return LOW_STOCK;
        }
        return IN_STOCK;
    }

    public String getLabel() { return label; }
    public String getCss() { return css; }
    public String getIcon() { return icon; }
    public String getAlertMessage() { return alertMessage; }
}