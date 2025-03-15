package com.hatman.novelista.EditorUtils
import android.R
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable

open class ImageSwitch(context: Context, attributeSet: AttributeSet?, defStyle:Int) : androidx.appcompat.widget.AppCompatImageButton(context, attributeSet, defStyle) {
   private var switch=0;
    private var attributes: TypedArray = context.obtainStyledAttributes(
        attributeSet, com.hatman.novelista.R.styleable.ImageSwitch
    )
    private val colorList= listOf(
        resources.getColor(com.hatman.novelista.R.color.button_list, context.theme),
        attributes.getColor(com.hatman.novelista.R.styleable.ImageSwitch_onSwitchColor, defStyle)
    )
    public fun colorSwitch(){
        val mTransition = TransitionDrawable(
            arrayOf(
                ColorDrawable(colorList[switch]),
            ColorDrawable(colorList[(switch+1)%2])
            )
        )
        this.background=mTransition
        mTransition.startTransition(200)
        switch=(switch+1)%2
    }
    public fun colorOff(){
        val mTransition = TransitionDrawable(
            arrayOf(
                ColorDrawable(colorList[1]),
                ColorDrawable(colorList[0])
            )
        )
        this.background=mTransition
        mTransition.startTransition(200)
        switch=(switch+1)%2
    }
    public fun isOff() : Boolean{
        return switch == 0
    }
    constructor(context: Context) : this(context, null, R.attr.imageButtonStyle) {
    }
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, R.attr.imageButtonStyle) {
    }

}