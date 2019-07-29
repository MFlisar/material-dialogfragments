package com.michaelflisar.dialogs.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.michaelflisar.dialogs.classes.BaseDialogSetup
import com.michaelflisar.dialogs.helper.BaseDialogFragmentHandler
import com.michaelflisar.dialogs.interfaces.DialogFragment

abstract class BaseBottomDialogFragment<T : BaseDialogSetup> : DialogFragment<T>(), BaseDialogFragmentHandler.IBaseBottomDialog {

    // -----------------------------
    // forward functions to handler
    // -----------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dlg = handler.onCreateDialog(savedInstanceState)
        onDialogCreated(dlg)
        return dlg
    }

    protected open fun onDialogCreated(dialog: Dialog) {

    }

    override fun onDestroy() {
        handler.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        handler.onSaveInstanceState(outState)
    }

    // -----------------------------
    // abstract functions
    // -----------------------------

    abstract override fun onHandleCreateBottomDialog(savedInstanceState: Bundle?): View
}