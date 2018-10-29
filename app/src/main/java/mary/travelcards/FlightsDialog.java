package mary.travelcards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;

import static mary.travelcards.CardsAdapter.VARIANTS_KEY;


public class FlightsDialog extends DialogFragment implements OnClickListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Resources res = getActivity().getResources();
        Bundle bundle = getArguments();
        String[] variantsStrings = bundle.getStringArray(VARIANTS_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(res.getString(R.string.flightVariants)).setPositiveButton(res.getString(R.string.choose), this)
                .setNeutralButton(res.getString(R.string.cancel), this)
                .setSingleChoiceItems(variantsStrings, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        //Do something
                    }
                });
        return builder.create();
    }

    public void onClick(DialogInterface dialog, int item) {
        switch (item) {
            case Dialog.BUTTON_POSITIVE:
                //Do something
                break;
            case Dialog.BUTTON_NEUTRAL:
                //Do something
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}