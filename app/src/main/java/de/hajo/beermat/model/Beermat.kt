package de.hajo.beermat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author hansjoerg.keser
 * @since 23.11.18
 */
@Entity
data class Beermat(
    @ColumnInfo(name = "amount")
    var amount: Int = 0,

    @ColumnInfo(name = "price")
    var price: Int = 0,

    @ColumnInfo(name = "total_price")
    var totalPrice: Int = 0
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}