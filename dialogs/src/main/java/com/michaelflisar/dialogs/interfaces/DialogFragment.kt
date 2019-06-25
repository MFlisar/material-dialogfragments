package com.michaelflisar.dialogs.interfaces

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.ExtendedFragment
import androidx.fragment.app.FragmentActivity
import com.michaelflisar.dialogs.DialogSetup
import com.michaelflisar.dialogs.classes.BaseDialogSetup
import com.michaelflisar.dialogs.enums.SendResultType
import com.michaelflisar.dialogs.events.BaseDialogEvent
import com.michaelflisar.dialogs.events.DialogCancelledEvent
import com.michaelflisar.dialogs.helper.BaseDialogFragmentHandler
import com.michaelflisar.dialogs.utils.DialogUtil

open class DialogFragment : ExtendedFragment() {

    companion object {
        const val ARG_SETUP = "setup"
    }

    protected val handler: BaseDialogFragmentHandler<*> = BaseDialogFragmentHandler(this)
    private val internalSetup: BaseDialogSetup by lazy {
        arguments!!.getParcelable<BaseDialogSetup>(ARG_SETUP)!!
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : BaseDialogSetup> getSetup(): T = internalSetup as T

    fun setSetupArgs(setup: BaseDialogSetup) {
        val args = arguments ?: Bundle()
        args.putParcelable(ARG_SETUP, setup)
        arguments = args
    }

    fun show(activity: FragmentActivity, customSendResultType: SendResultType? = DialogSetup.DEFAULT_SEND_RESULT_TYPE, tag: String = this::class.java.name) {
        handler.show(activity, tag, customSendResultType)
    }

    fun showAllowingStateLoss(activity: FragmentActivity) {
        handler.showAllowingStateLoss(activity, this)
    }

    override fun onCancel(dialog: DialogInterface?) {
        if (internalSetup.sendCancelEvent) {
            sendEvent(DialogCancelledEvent(internalSetup))
        }
        super.onCancel(dialog)
    }

    // -----------------------------
    // Result
    // -----------------------------

    protected fun <X : BaseDialogEvent> sendEvent(event: X) {
        // send result to any custom handler
        DialogSetup.sendResult(event)
        // send result the default way
        DialogUtil.trySendResult(event, this, handler.customSendResultType
                ?: DialogSetup.DEFAULT_SEND_RESULT_TYPE)

        onEventSend(event)
    }

    protected open fun <X : BaseDialogEvent> onEventSend(event: X) {

    }

}