package a.team.works.u22.hal.u22teama;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.content.DialogInterface;
        import android.os.Bundle;

public class DonationGratitudeDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle SavedInstanceState){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("寄付完了");
        dialogBuilder.setMessage("寄付が完了しました");

        dialogBuilder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return dialogBuilder.create();
    }
}
