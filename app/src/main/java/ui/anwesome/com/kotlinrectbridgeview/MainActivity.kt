package ui.anwesome.com.kotlinrectbridgeview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.rectbridgeview.RectBridgeView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RectBridgeView.create(this)
    }
}
