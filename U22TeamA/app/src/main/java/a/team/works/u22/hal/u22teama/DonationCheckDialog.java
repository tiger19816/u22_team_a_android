package a.team.works.u22.hal.u22teama;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DonationCheckDialog extends DialogFragment{

    public Dialog onCreateDialog(Bundle SavedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        String donationMoney = getArguments().getString("donationMoney");
        dialogBuilder.setTitle("寄付金額確認");

        dialogBuilder.setMessage("下記の金額を寄付します。\nよろしいですか？\n\n"+donationMoney+"  円");

        final TextView tvCheck = new TextView(getActivity());
        dialogBuilder.setView(tvCheck);

        dialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogBuilder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DonationActivity donationActivity = (DonationActivity) getActivity();
                donationActivity.donationSend();

            }
        });
        return dialogBuilder.create();
    }
}