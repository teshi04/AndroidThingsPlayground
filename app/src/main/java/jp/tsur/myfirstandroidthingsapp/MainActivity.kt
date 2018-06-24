package jp.tsur.myfirstandroidthingsapp

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.google.android.things.contrib.driver.bmx280.Bmx280
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio
import kotlin.concurrent.thread


class MainActivity : Activity() {

    private val buttonA: Button by lazy { RainbowHat.openButtonA() }
    private val buttonB: Button by lazy { RainbowHat.openButtonB() }
    private val buttonC: Button by lazy { RainbowHat.openButtonC() }

    private val ledRed: Gpio by lazy { RainbowHat.openLedRed() }
    private val ledBlue: Gpio  by lazy { RainbowHat.openLedBlue() }
    private val ledGreen: Gpio by lazy { RainbowHat.openLedGreen() }

    private val buzzer: Speaker by lazy { RainbowHat.openPiezo() }

    private val sensor: Bmx280 by lazy {
        RainbowHat.openSensor().apply {
            temperatureOversampling = Bmx280.OVERSAMPLING_1X
        }
    }

    private val segment: AlphanumericDisplay by lazy {
        RainbowHat.openDisplay().apply {
            setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
            setEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonA.setOnButtonEventListener { _, pressed ->
            ledRed.value = pressed
            if (pressed) {
                buzzer.play(523.23) // ド
            } else {
                buzzer.stop()
            }
        }

        buttonB.setOnButtonEventListener { _, pressed ->
            ledGreen.value = pressed
            if (pressed) {
                buzzer.play(587.34) // レ
            } else {
                buzzer.stop()
            }
        }

        buttonC.setOnButtonEventListener { _, pressed ->
            ledBlue.value = pressed
            if (pressed) {
                buzzer.play(659.25) // ミ
            } else {
                buzzer.stop()
            }
        }

        findViewById<TextView>(R.id.temperature).text = "温度:${sensor.readTemperature()}"

        findViewById<android.widget.Button>(R.id.laco).setOnClickListener {
            startLacolaco()
        }
    }

    override fun onStop() {
        super.onStop()
        ledRed.close()
        ledBlue.close()
        ledGreen.close()
        buttonA.close()
        buttonB.close()
        buttonC.close()
        buzzer.close()
        segment.close()
        sensor.close()
    }

    private fun startLacolaco() {
        val text = "    LACOLACOLACO-w    "
        thread {
            for (index in 0..text.length - 4) {
                val substring = text.substring(index, index + 4)
                segment.display(substring)
                Thread.sleep(300L)
            }
        }
    }
}
