package Features;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ideaapp.R;

public class DialogBox extends AppCompatActivity {


    DialogBox () {}

    public void showSuccesDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_succes_dialog,(ConstraintLayout) findViewById(R.id.LayoutDialogContainer));

        builder.setView(view);
        ((Button) view.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.ok));
        ((ImageView) view .findViewById(R.id.imageIcon)).setImageResource(R.drawable.done);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    public  void showErrorDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_error_dialog,(ConstraintLayout) findViewById(R.id.LayoutDialogContainer));

        builder.setView(view);

        ((Button) view.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.ok));
        ((ImageView) view .findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.ups));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.title));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    public void showWarningDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_warning_dialog,(ConstraintLayout) findViewById(R.id.LayoutDialogContainer));

        builder.setView(view);
        ((Button) view.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.yes));
        ((Button) view.findViewById(R.id.buttonAction)).setText(getResources().getString(R.string.no));
        ((ImageView) view .findViewById(R.id.imageIcon)).setImageResource(R.drawable.warning);
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.sure));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.title));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

    }
}
