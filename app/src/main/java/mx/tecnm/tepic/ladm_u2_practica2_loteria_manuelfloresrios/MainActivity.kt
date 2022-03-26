package mx.tecnm.tepic.ladm_u2_practica2_loteria_manuelfloresrios

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*
import mx.tecnm.tepic.ladm_u2_practica2_loteria_manuelfloresrios.databinding.ActivityMainBinding
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val lote=Array<Loteria>(54,{ Loteria(0,0) })
    val cartas= arrayOf<Int>(R.drawable.carta1,R.drawable.carta2,R.drawable.carta3,R.drawable.carta4
            ,R.drawable.carta5,R.drawable.carta6,R.drawable.carta7,R.drawable.carta8,R.drawable.carta9,
        R.drawable.carta10,R.drawable.carta11,R.drawable.carta12,R.drawable.carta13,R.drawable.carta14,
        R.drawable.carta15,R.drawable.carta16,R.drawable.carta17,R.drawable.carta18,R.drawable.carta19,
        R.drawable.carta20,R.drawable.carta21,R.drawable.carta22,R.drawable.carta23,R.drawable.carta24,
        R.drawable.carta25,R.drawable.carta26,R.drawable.carta27,R.drawable.carta28,R.drawable.carta29,
        R.drawable.carta30,R.drawable.carta31,R.drawable.carta32,R.drawable.carta33,R.drawable.carta34,
        R.drawable.carta35,R.drawable.carta36,R.drawable.carta37,R.drawable.carta38,R.drawable.carta39,
        R.drawable.carta40,R.drawable.carta41,R.drawable.carta42,R.drawable.carta43,R.drawable.carta44,
        R.drawable.carta45,R.drawable.carta46,R.drawable.carta47,R.drawable.carta48,R.drawable.carta49,
        R.drawable.carta50,R.drawable.carta51,R.drawable.carta52,R.drawable.carta53,R.drawable.carta54)

    val audios= arrayOf<Int>(R.raw.audio1,R.raw.audio2,R.raw.audio3,R.raw.audio4,R.raw.audio5,R.raw.audio6,
        R.raw.audio7,R.raw.audio8,R.raw.audio9,R.raw.audio10,R.raw.audio11,R.raw.audio12,R.raw.audio13,
        R.raw.audio14,R.raw.audio15,R.raw.audio16,R.raw.audio17,R.raw.audio18,R.raw.audio19,R.raw.audio20,
        R.raw.audio21,R.raw.audio22,R.raw.audio23,R.raw.audio24,R.raw.audio25,R.raw.audio26,R.raw.audio27,
        R.raw.audio28,R.raw.audio29,R.raw.audio30,R.raw.audio31,R.raw.audio32,R.raw.audio33,R.raw.audio34,
        R.raw.audio35,R.raw.audio36,R.raw.audio37,R.raw.audio38,R.raw.audio39,R.raw.audio40,R.raw.audio41,
        R.raw.audio42,R.raw.audio43,R.raw.audio44,R.raw.audio45,R.raw.audio46,R.raw.audio47,R.raw.audio48,
        R.raw.audio49,R.raw.audio50,R.raw.audio51,R.raw.audio52,R.raw.audio53,R.raw.audio54)
    var conta=0
    var ac=this
    var indi=false
    var indi2=true
    val barajeo=ArrayList<Int>()
    var hilo=HiloComprobar(this,indi,indi2)  //quitar porsi
    var audio:MediaPlayer?=null


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        llenar()
        binding.Pausar.isEnabled=false
        binding.Comprobar.isEnabled=false

        binding.Iniciar.setOnClickListener {
            binding.Iniciar.isEnabled=false
            binding.Pausar.isEnabled=true
            binding.Comprobar.isEnabled=false
            val scope= CoroutineScope(Job() +Dispatchers.Main)
            val lanzar=scope.launch(EmptyCoroutineContext,CoroutineStart.LAZY){
                while (conta<=lote.size-1){
                    if(!indi==true) {
                        delay(400L)
                        try{
                            audio = MediaPlayer.create(ac, lote[barajeo[conta]].aud)
                        }
                        catch (e:Exception){}
                        runOnUiThread {
                            binding.image.setImageResource(lote[barajeo[conta++]].ima)
                            try {
                                audio?.start()
                            }catch (e:Exception){}

                        }
                    }else{
                        indi2=false
                        audio?.release()
                    }
                    delay(4000L)
                }
                audio?.release()
                conta=0
                hilo.pausar()
                indi=false
                indi2=true
                binding.Iniciar.isEnabled=true
                binding.Pausar.isEnabled=false
                binding.Comprobar.isEnabled=false
            }
            if(indi2) {
                conta=0
                barajeo.clear()
                IniciaBarajeo()
                lanzar.start()
            }else{
                conta=0
                lanzar.cancel()
                indi=false
                barajeo.clear()
                IniciaBarajeo()
                lanzar.start()
            }
        }

        binding.Pausar.setOnClickListener {
            indi=true
            binding.Iniciar.isEnabled=false
            binding.Pausar.isEnabled=false
            binding.Comprobar.isEnabled=true
            hilo.despausar()
        }

        binding.Comprobar.setOnClickListener {
            try {
                hilo.start()
            }catch (e:Exception){
            }
            indi=hilo.indi
            indi2=hilo.indi2
        }
    }
    fun llenar(){
        (0..lote.size-1).forEach {
        lote[it].ima=cartas[it]
        lote[it].aud=audios[it]
        }
    }
    fun IniciaBarajeo(){
        (0..cartas.size-1).forEach {
            barajeo.add(baraje(barajeo))
        }
    }
    fun baraje(a:ArrayList<Int>):Int{
        var num= Random.nextInt(cartas.size)
        if(!a.contains(num)){
            return  num
        }
        else{
            return baraje(a)
        }
    }
}