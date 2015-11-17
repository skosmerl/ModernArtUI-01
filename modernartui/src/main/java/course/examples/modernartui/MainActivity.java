package course.examples.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    SeekBar mySeekBar;
    ViewGroup myGridLayout;
    private static final String TAG = "ModernArtUI";
    float[] hsvColorHueStart;
    int myGridChildcount;
    // Identifier for each type of Dialog
    private static final int ALERTTAG = 0, PROGRESSTAG = 1;
    private DialogFragment mDialog;
    static private final String URL = "http://www.moma.org/collection/works/79300";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySeekBar = (SeekBar) findViewById(R.id.colorSeekBar);
        mySeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        myGridLayout =  (ViewGroup) findViewById(R.id.gridLayout);

        myGridChildcount = myGridLayout.getChildCount();

        float[] hsvColor = {0, 1, 1};

        hsvColorHueStart = new float[15];
        for (int i=0; i < myGridChildcount; i++){
            //Log.i(TAG, "loop i="+i);
            View v = myGridLayout.getChildAt(i);
            Drawable drawable = v.getBackground();
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            int intColor = colorDrawable.getColor();
            Color.colorToHSV(intColor, hsvColor);
            //Log.i(TAG, "loop start color hsv hue= " + hsvColor[0]);
            hsvColorHueStart[i]=(float) hsvColor[0];//remember only hue component onCreate
            //Log.i(TAG, "loop hsvColorHueStart[i]= " + hsvColorHueStart[i]);
            //Log.i(TAG, "loop end i="+i);
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showDialogFragment(ALERTTAG);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        // initialize color changer seekbar and set change listener
        private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {


                float[] hsvColor = {0, 1, 1};
                // generate only hue component in range [0, 360),
                // leaving saturation and brightness maximum possible
                float newHSVColor = 360f * progress / 100;//maxProgress;

                  for (int i=0; i < myGridChildcount; i++){
                    View v = myGridLayout.getChildAt(i);
                    Drawable drawable = v.getBackground();
                    ColorDrawable colorDrawable = (ColorDrawable) drawable;
                    int intColor = colorDrawable.getColor();
                    Log.i(TAG, "loop: " + intColor);
                    if (intColor != -1 ) {
                    Color.colorToHSV(intColor, hsvColor);//retain only 1st component!
//                    Log.i(TAG, "loop hsvColor[0]: " + hsvColor[0]);
                    hsvColor[1]=1;
                    hsvColor[2]=1;
                     if (newHSVColor + hsvColorHueStart[i] > 360 ) {
                            hsvColor[0] = newHSVColor + hsvColorHueStart[i] - 360;
                        }
                     else
                        {
                            hsvColor[0] = newHSVColor + hsvColorHueStart[i];
                        }

                     v.setBackgroundColor(Color.HSVToColor(hsvColor));
                    }
                }

            }
        };

    // Show desired Dialog
    void showDialogFragment(int dialogID) {
         // Create a new AlertDialogFragment
         mDialog = AlertDialogFragment.newInstance();
         // Show AlertDialogFragment
         mDialog.show(getFragmentManager(), "Alert");

    }


    // Class that creates the AlertDialog
    public static class AlertDialogFragment extends DialogFragment {
//    public class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {
//        public AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("inspired by the works of artists Piet Mondrian and Ben Nicholson")

                    .setMessage("Click below to learn more!")

                    // User can dismiss dialog by hitting back button
                    .setCancelable(true)

                    // Set up No Button
                    .setNegativeButton("Not now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    //((MainActivity) getActivity()).continueShutdown(false);
                                }
                            })

                    // Set up Yes Button
                    .setPositiveButton("Visit MOMA",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    // Start a Browser Activity to view a web page or its URL
                                    Log.i(TAG, "Entered startImplicitActivation()");

                                    Intent baseIntent = new Intent(Intent.ACTION_VIEW, 	Uri.parse(URL));

                                    Intent chooserIntent = null;
                                    chooserIntent = Intent.createChooser(baseIntent , "Choose browser of your choice");

                                    Log.i(TAG,"Chooser Intent Action:" + chooserIntent.getAction());

                                    startActivity(chooserIntent);

                                }
                            }).create();
        }
    }


}
