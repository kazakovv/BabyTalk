package com.example.victor.swipeviews;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Victor on 19/10/2014.
 */
public class MaleOrFemaleDialog extends DialogFragment {
    TextView mainMessage;
    //View fertilityCalandarIcon;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //vrazvam osnovnoto sabshtenie i ikonata za kalendara

        mainMessage = (TextView) getActivity().findViewById(R.id.mainMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_menu_title)
                .setItems(R.array.guy_or_girl_option, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch(which) {
                    case 0:
                        mainMessage.setText("Your partner's upcoming fertile days are");
                        Main.fertilityCalandarIcon.setVisible(false);

                        break;
                    case 1:
                        mainMessage.setText("Your upcoming fertile days are");
                        Main.fertilityCalandarIcon.setVisible(true);

                        break;
                }
            }
        });
        return builder.create();

    }
}
