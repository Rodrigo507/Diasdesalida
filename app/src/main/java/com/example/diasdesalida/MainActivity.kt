package com.example.diasdesalida


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var option : Spinner
    lateinit var r:String

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        option = findViewById(R.id.sexo_spinner) as Spinner
       //result = findViewById(R.id.resultado) as TextView
        val options = arrayOf("M", "F")
        option.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,options)
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //result.text = "Please Select an Option"
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //result.text = options.get(position)
                r= options.get(position).toLowerCase()
            }
        }


        botonverificar.setOnClickListener {
           val cedular: String = cedula.text.toString()
            val ultimonumero:Int =cedular[cedular.lastIndex].toString().toInt()
                hidenMyKeyboard()

            Log.d("TAG","Dia de salida es 1 para mujeres 2 hombres ${diasalida(r)}")
            //print("SEXO SELECTED"+r)

            when(tiposexo(r)){
                true-> when(diasalida(r)){
                    1-> resultado.text="HORA EN LA QUE PUEDES SALIR Y REGRESAR  \n"+ horradesalida(ultimonumero)
                    0->resultado.text= """HOY NO SALEN LAS MUJERES
                                        |#QUEDATEENTUCASA
                                 """.trimMargin()
                }
                false->when(diasalida(r)){
                    2-> resultado.text="HORA EN LA QUE PUEDES SALIR Y REGRESAR  \n"+ horradesalida(ultimonumero)
                    0->resultado.text= """HOY NO SALEN LOS HOMBRES
                                        |#QUEDATEENTUCASA
                                 """.trimMargin()
                }
            }
        }
    }
//-----------------------------------------------------------------------
    //Funcion que oculta el teclado
    fun hidenMyKeyboard(): Unit {
        val view = this.currentFocus
        if (view!=null){
            val hideMe= getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken,0)
        }
        //else
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    //------------------------------------------------------------------------------
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }


}
fun tiposexo(va: String): Boolean {
    return va=="f"
}
//-----------------------------------------------
//Funcion retornara un string con la hora de salida y de regreso
fun horradesalida(numero : Int): String {
    return when(numero){
        7 -> "6:30 am - 8:30 am "
        8 -> "7:30 am - 8:30 am "
        9 -> "8:30 am - 10:30 am "
        0 -> "9:30 am - 11:30 am "
        1 -> "12:30 pm - 2:30 pm "
        2 -> "1:30 pm - 3:30 pm "
        3 -> "2:30 pm - 4:30 pm "
        4 -> "3:30 pm - 5:30 pm "
        5 -> "4:30 pm - 6:30 pm "
        6 -> "5:30 pm - 7:30 pm "

        else -> "ERROR HORA DE SALIDA"
    }
}
//----------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
fun diasalida(sexo: String): Int {
    val current = LocalDateTime.now()
    val formatterd = DateTimeFormatter.ofPattern("eeee")
    var formatted3 = current.format(formatterd).toLowerCase()
    val diasmujerres = arrayOf<String>("lunes","monday", "miércoles","wednesday", "viernes","friday")
    val diashombres = arrayOf<String>("martes","tuesday", "jueves","thursay")
    //formatted3="Thursday"
    Log.d("TAG","Dia de de hoy "+formatted3)
    var resultado: Int

    if(sexo=="f"){
        resultado= when(formatted3){
            in diasmujerres -> 1
            else ->0
        }
    }else{
        resultado= when(formatted3){
            in diashombres ->2
            else -> 0
        }
    }
    return resultado
}