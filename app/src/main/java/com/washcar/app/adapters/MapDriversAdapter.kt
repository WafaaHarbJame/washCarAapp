package com.washcar.app.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.washcar.app.R
import com.washcar.app.apiHandlers.DataFetcherCallBack
import com.washcar.app.models.DriverModel

class MapDriversAdapter(
    private val activity: Activity,
    objectsList: MutableList<DriverModel>?,
    callBack: DataFetcherCallBack
) :
    RecyclerView.Adapter<MapDriversAdapter.ProductsHolder?>() {

    var view: View? = null
    var objectsModelList: MutableList<DriverModel>? = objectsList
    val dataFetcherCallBack = callBack


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MapDriversAdapter.ProductsHolder {

        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.row_map_drivers, viewGroup, false)
        return ProductsHolder(view)
    }


    override fun onBindViewHolder(holder: MapDriversAdapter.ProductsHolder, position: Int) {

        if (objectsModelList != null) {

            val driverModel: DriverModel = objectsModelList!![position]
            holder.bind(driverModel)
        }

    }

    override fun getItemCount(): Int {
        if (objectsModelList != null)
            return objectsModelList!!.size
        else
            return 5
    }

    inner class ProductsHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val addressTxt: TextView = itemView!!.findViewById(R.id.addressTxt)
        val driverNameTxt: TextView = itemView!!.findViewById(R.id.driverNameTxt)

        fun bind(allDriverModel: DriverModel) {

            addressTxt.text = allDriverModel.address
            driverNameTxt.text = allDriverModel.fullName
        }


        init {
            itemView?.setOnClickListener {

                dataFetcherCallBack.Result(adapterPosition, "", true);

            }
        }
    }

    val adapter: MapDriversAdapter
        get() = this

}