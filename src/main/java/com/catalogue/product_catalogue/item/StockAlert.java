package com.catalogue.product_catalogue.item;

public final class StockAlert {

    private final String css;
    private final String icon;
    private final String message;

    public StockAlert(String css, String icon, String message) {
        this.css = css;
        this.icon = icon;
        this.message = message;
    }

    /** Alert to show after an item is saved, or null when nothing needs saying. */
    public static StockAlert forChange(StockStatus before, StockStatus after) {
        if (after != StockStatus.IN_STOCK) {
            return new StockAlert(after.getCss(), after.getIcon(), after.getAlertMessage());
        }
        if (before != null && before != StockStatus.IN_STOCK) {
            return new StockAlert("green", "\u2714", "Stock replenished");
        }
        return null;
    }

    public String getCss() { return css; }
    public String getIcon() { return icon; }
    public String getMessage() { return message; }
}