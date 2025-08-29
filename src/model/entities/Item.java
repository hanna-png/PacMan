package model.entities;

import model.enums.ItemType;

public class Item {
   private int row;
   private int col;
   private ItemType type;

   public Item(int row, int col, ItemType type) {
       this.row = row;
       this.col = col;
       this.type = type;
   }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

}
