package com.hatman.novelista.EditorUtils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.hatman.novelista.R

class EditorSetup(var layout: LinearLayout) {
    var mEditor: CallbackRichEditor
    val richText: String
        get() = mEditor.html

    private fun findViewById(id: Int): View {
        return layout.findViewById(id)
    }

    init {
        val mode = Resources.getSystem().configuration.uiMode
        mEditor = findViewById(R.id.editor) as CallbackRichEditor
        mEditor.setEditorHeight(200)
        mEditor.setEditorFontSize(22)
        //boolean isDark=(mode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        setColors()
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10)
        mEditor.setOnTextChangeListener{ s: String? ->
            RoutineHandler.newCoroutineMain {
                if (s!=null) {
                    val formatted = s.replace(Regex("(?s)<[^>]*>(\\s*<[^>]*>)*"), "").trim()
                    if (formatted == "") {
                        val isHeader=s.replace(Regex("<h([1-6])>.*?</h([1-6])>"), "").trim()==""
                        if (!isHeader){
                            mEditor.resetFormattingOptions()
                        }


                    }
                }
                mEditor.createObservationRoutine()
            }

        }
//        mEditor.setOnKeyListener{_, _, _->
//            runBlocking {
//                mEditor.createObservationRoutine()
//            }
//            return@setOnKeyListener true
//        }
//        mEditor.setOnTouchListener { v, event ->
//            runBlocking {
//                v.performClick()
//                mEditor.requestFocus()
//                openKeyboard()
//                mEditor.createObservationRoutine()
//            }
//            return@setOnTouchListener true
//        }
        mEditor.setInputEnabled(true);
        setup()
    }
private fun openKeyboard(){
    val imm:InputMethodManager= layout.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(mEditor, 0)
}
    private fun setColors() {
        val theme = layout.context.theme
        val resources = layout.resources
        val foregroundColor = resources.getColor(R.color.editor_foreground, theme)
        val backgroundColor = resources.getColor(R.color.editor_background, theme)
        mEditor.setEditorBackgroundColor(backgroundColor)
        mEditor.setEditorFontColor(foregroundColor)
    }
public fun getText(): String{
    return mEditor.html
}
    //   private void isDarkTheme(boolean isDark){
    //        if (isDark){
    //            mEditor.setEditorBackgroundColor(Color.BLACK);
    //            mEditor.setBackgroundColor(Color.BLACK);
    //            mEditor.setEditorFontColor(Color.WHITE);
    //        }
    //        else{
    //            mEditor.setEditorFontColor(Color.BLACK);
    //        }
    //   }
    /* FIXME: 11/13/24 THE GLITCH HAPPENS WHEN ACTIVATING A SWITCH WITH TEXT ON
    SCREEN BEFORE DELETING A CHARACTER WITHOUT DOING ANYTHING
    FIX BY MAKING THE DELETE KEY TRIGGER THE COLOR SWITCH WHEN NEEDED
*/
    constructor(lay: LinearLayout, html: String) : this(lay) {
        mEditor.html = html
    }

    private fun setup() {
        findViewById(R.id.action_undo).setOnClickListener { mEditor.undo() }

        findViewById(R.id.action_redo).setOnClickListener { mEditor.redo() }

        findViewById(R.id.action_bold).setOnClickListener {
            mEditor.setBold()
                mEditor.addMonitored(CallbackRichEditor.CommandType.BOLD)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_italic).setOnClickListener {
            mEditor.setItalic()
            mEditor.addMonitored(CallbackRichEditor.CommandType.ITALIC)

            //((ImageSwitch)v).colorSwitch();
        }

        //       findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               mEditor.setSubscript();
//               ((ImageSwitch)v).colorSwitch();
//           }
//       });
//
//       findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               mEditor.setSuperscript();
//               ((ImageSwitch)v).colorSwitch();
//           }
//       });
        findViewById(R.id.action_strikethrough).setOnClickListener {
            mEditor.setStrikeThrough()
                mEditor.addMonitored(CallbackRichEditor.CommandType.STRIKETHROUGH)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_underline).setOnClickListener {
            mEditor.setUnderline()
                mEditor.addMonitored(CallbackRichEditor.CommandType.UNDERLINE)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_heading1).setOnClickListener {
            mEditor.setHeading(1)
            mEditor.swapFormatBlock(CallbackRichEditor.CommandType.H1)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_heading2).setOnClickListener {
            mEditor.setHeading(2)
            mEditor.swapFormatBlock(CallbackRichEditor.CommandType.H2)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_heading3).setOnClickListener {
            mEditor.setHeading(3)
            mEditor.swapFormatBlock(CallbackRichEditor.CommandType.H3)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_heading4).setOnClickListener {
            mEditor.setHeading(4)
            mEditor.swapFormatBlock(CallbackRichEditor.CommandType.H4)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_heading5).setOnClickListener {
            mEditor.setHeading(5)
            mEditor.swapFormatBlock(CallbackRichEditor.CommandType.H5)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_heading6).setOnClickListener {
            mEditor.setHeading(6)
            mEditor.swapFormatBlock(CallbackRichEditor.CommandType.H6)
            //((ImageSwitch)v).colorSwitch();
        }


        findViewById(R.id.action_indent).setOnClickListener { mEditor.setIndent() }

        findViewById(R.id.action_outdent).setOnClickListener { mEditor.setOutdent() }

        findViewById(R.id.action_align_left).setOnClickListener { mEditor.setAlignLeft() }

        findViewById(R.id.action_align_center).setOnClickListener { mEditor.setAlignCenter() }

        findViewById(R.id.action_align_right).setOnClickListener { mEditor.setAlignRight() }


        findViewById(R.id.action_insert_bullets).setOnClickListener {
            mEditor.setBullets()
                mEditor.addMonitored(CallbackRichEditor.CommandType.UNORDEREDLIST)
            //((ImageSwitch)v).colorSwitch();
        }

        findViewById(R.id.action_insert_numbers).setOnClickListener {
            mEditor.setNumbers()
                mEditor.addMonitored(CallbackRichEditor.CommandType.ORDEREDLIST)
            //((ImageSwitch)v).colorSwitch();
        }
    }
}