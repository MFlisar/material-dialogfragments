package com.michaelflisar.dialogs.helper;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.michaelflisar.dialogs.classes.SendResultType;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.ExtendedFragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Created by flisar on 15.11.2016.
 */

public class BaseDialogFragmentHandler<T extends ExtendedFragment> {
    private SendResultType customSendResultType = null;
    private T mParent;
    public BaseDialogFragmentHandler(T parent) {
        mParent = parent;
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mParent instanceof IBaseBottomDialog) {
            BottomSheetDialog dlg = new BottomSheetDialog(mParent.getContext(), mParent.getTheme());
            // always show the sheet expanded!
            if (((IBaseBottomDialog) mParent).alwaysShowExpanded()) {
                dlg.setOnShowListener(dialog -> {
                    BottomSheetDialog d = (BottomSheetDialog) dialog;
                    View bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
                    behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                    behaviour.setPeekHeight(bottomSheet.getHeight());
                });
            }
            View v = ((IBaseBottomDialog) mParent).onHandleCreateBottomDialog(savedInstanceState);
            dlg.setContentView(v);
            onViewReady(dlg, v);
            return dlg;
        } else {
            Dialog dlg = ((IBaseDialog) mParent).onHandleCreateDialog(savedInstanceState);
            onViewReady(dlg, null);
            return dlg;
        }
    }

    public void onDestroy() {
        mParent = null;
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public final void onViewReady(Dialog dlg, View view) {
        if (mParent instanceof IDialogReadyListener) {
            ((IDialogReadyListener) mParent).onDialogReady(dlg, view);
        }
        if (mParent.getActivity() instanceof IDialogReadyListener) {
            ((IDialogReadyListener) mParent.getActivity()).onDialogReady(dlg, view);
        }
    }

    // -----------------------------
    // functions
    // -----------------------------

    public SendResultType getCustomSendResultType() {
        return customSendResultType;
    }

    public void show(FragmentActivity activity, DialogFragment fragment, SendResultType sendResultType) {
        show(activity, fragment.getClass().getName(), sendResultType);
    }

    public void show(FragmentActivity activity, String tag, SendResultType sendResultType) {
        customSendResultType = sendResultType;
        mParent.show(activity.getSupportFragmentManager(), tag);
    }

    public void show(Fragment parent, String tag, SendResultType sendResultType) {
        customSendResultType = sendResultType;
        mParent.show(parent.getChildFragmentManager(), tag);
    }

    public void showAllowingStateLoss(FragmentActivity activity, DialogFragment fragment) {
        showAllowingStateLoss(activity, fragment.getClass().getName());
    }

    public void showAllowingStateLoss(FragmentActivity activity, String tag) {
        mParent.showAllowingStateLoss(activity.getSupportFragmentManager(), tag);
    }

    // -----------------------------
    // Interface
    // -----------------------------

    public interface IBaseDialog{
        Dialog onHandleCreateDialog(Bundle savedInstanceState);
    }

    public interface IBaseBottomDialog {
        View onHandleCreateBottomDialog(Bundle savedInstanceState);

        boolean alwaysShowExpanded();
    }

    public interface IDialogReadyListener {
        void onDialogReady(Dialog dialog, View view);
    }
}
