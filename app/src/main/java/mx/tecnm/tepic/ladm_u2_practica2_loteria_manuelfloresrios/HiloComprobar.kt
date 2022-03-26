package mx.tecnm.tepic.ladm_u2_practica2_loteria_manuelfloresrios
class HiloComprobar(m:MainActivity,indi:Boolean,indi2:Boolean):Thread() {
    val m=m
    var indi=indi
    var indi2=indi2
    private var ejecutar=true
    private  var pausar=false


    override fun run() {
        super.run()
        while (ejecutar) {
            if (!pausar) {
                m.runOnUiThread() {
                    indi = false
                    indi2 = true
                }
            }
            sleep(10L)
        }
    }
    fun pausar(){
        pausar=true
    }
    fun despausar(){
        pausar=false
    }
}