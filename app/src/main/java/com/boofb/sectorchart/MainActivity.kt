package com.boofb.sectorchart

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private val COLORS = intArrayOf(
        Color.parseColor("#FFD778"), Color.parseColor("#FEA3C2"),
        Color.parseColor("#66DAD9"), Color.parseColor("#9092FF"),
        Color.parseColor("#A0E491"), Color.parseColor("#6BD9AC"),
        Color.parseColor("#75A2FF"), Color.parseColor("#6BD9AC")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.btn_create)
            .setOnClickListener {
                createData()
            }
    }

    private fun createData(): Unit {
        val dataList = ArrayList<SectorData>()

        for (i in COLORS.indices) {
            val nextInt = Random().nextInt(COLORS.size)
            dataList.add(
                SectorData("饮食$i", 100.0*nextInt, COLORS[i]),
            )
        }

        findViewById<SectorChartView>(R.id.sv)
            .setViewData(dataList)
    }
}