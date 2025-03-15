package com.hatman.novelista.EditorUtils
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.hatman.novelista.R
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.delay

class CallbackRichEditor(context: Context, attributeSet: AttributeSet?, defstyle: Int) : RichEditor(context, attributeSet, defstyle) {
    public enum class CommandType(val value:String, val id:Int, var state: Boolean){
        BOLD("bold", R.id.action_bold, false),
        ITALIC("italic", R.id.action_italic, false),
        STRIKETHROUGH("strikeThrough", R.id.action_strikethrough, false),
        UNDERLINE("underline", R.id.action_underline, false),
        H1("formatBlock", R.id.action_heading1, false),
        H2("formatBlock", R.id.action_heading2, false),
        H3("formatBlock", R.id.action_heading3, false),
        H4("formatBlock", R.id.action_heading4, false),
        H5("formatBlock", R.id.action_heading5, false),
        H6("formatBlock", R.id.action_heading6, false),
        UNORDEREDLIST("insertUnorderedList", R.id.action_insert_bullets, false),
        ORDEREDLIST("insertOrderedList", R.id.action_insert_numbers, false),
    }
    init {
        RoutineHandler.newCoroutine {
            monitorChecked()
        }
    }
    data class FormatBlock(var headerType:CommandType?)
    private val monitored : List<CommandType> = listOf(
        CommandType.BOLD,
        CommandType.ITALIC,
        CommandType.STRIKETHROUGH,
        CommandType.UNDERLINE,
        CommandType.UNORDEREDLIST,
        CommandType.ORDEREDLIST,
    )
    private val currentFormatBlock=FormatBlock(null)

    /* TODO: MAKE A THREAD THAT CHECKS ONTEXTCHANGELISTENER WITH COROUTINES
    *   AND UPDATES ANY SWITCHES*/
    constructor(context: Context): this(context, null, android.R.attr.webViewStyle) {}
    constructor(context: Context, attributeSet: AttributeSet): this(context, attributeSet, android.R.attr.webViewStyle){}
    private fun isOn(idx: Int): Unit{

        val script="javascript:(function(){return document.queryCommandState('${monitored[idx].value}');})();"
        //EXAMPLE: "document.queryCommandState('bold')"
        evaluateJavascript(script) { res ->
            monitored[idx].state= (res=="true")
        }
    }


    // TODO: PUT THIS FUNCTION IN A SECOND THREAD WITH COROUTINES and make it better
    private fun checkMonitored(){
        for (ct:CommandType in monitored){
            if (!ct.state){
                val img=(parent as View).findViewById<ImageSwitch>(ct.id)
                if (!img.isOff()){
                    img.colorSwitch()
                }
            }
        }

    }
private fun setParagraph(){
    evaluateJavascript("javascript:document.execCommand('formatBlock', false, '<p>');", null)
}
    public fun swapFormatBlock(newHeader:CommandType){
        val currentHeader=currentFormatBlock.headerType
//        val newImg=(parent as View).findViewById<ImageSwitch>(newHeader.id)
        when(currentHeader){
            newHeader -> {
                setParagraph()
                //newImg.colorSwitch()
                currentFormatBlock.headerType=null
            };
            null -> {
                currentFormatBlock.headerType=newHeader
                //newImg.colorSwitch()
            };
            else ->{
                //val img=(parent as View).findViewById<ImageSwitch>(currentHeader.id)
                //img.colorSwitch()
                currentFormatBlock.headerType=newHeader
                //newImg.colorSwitch()
            };
        }
    }
    public fun addMonitored(ct:CommandType){
            ct.state=!ct.state
            (parent as View).findViewById<ImageSwitch>(ct.id).colorSwitch()
    }
    public fun createObservationRoutine(){
            monitor()
        RoutineHandler.newCoroutine {
            checkMonitored()
        }

    }

    private fun monitor(){
        for (idx:Int in monitored.indices){
            isOn(idx)
        }

    }
//    override fun performClick(): Boolean {
//        return super.performClick()
//    }

    public fun resetFormattingOptions(){
        for (ct:CommandType in monitored){
            if (ct.state){
                when (ct){
                    CommandType.BOLD ->{setBold()}
                    CommandType.ITALIC->{setItalic()}
                    CommandType.STRIKETHROUGH->{setStrikeThrough()}
                    CommandType.UNDERLINE->{setUnderline()}
                    CommandType.UNORDEREDLIST->{setBullets()}
                    CommandType.ORDEREDLIST->{setNumbers()}
                    else -> {}
                }
                (parent as View).findViewById<ImageSwitch>(ct.id).colorSwitch()
            }
        }
    }

    private suspend fun monitorChecked(){
        delay(1000)
        while (true){
            for (ct:CommandType in monitored){
                val img=(parent as View).findViewById<ImageSwitch>(ct.id)
                if (!(ct.state xor img.isOff())){
                    img.colorSwitch()
                }
            }

            delay(100)
        }
    }
}