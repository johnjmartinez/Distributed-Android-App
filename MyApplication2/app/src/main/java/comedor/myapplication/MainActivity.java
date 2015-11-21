package comedor.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private static Integer[] CLK = null;
    private static Integer MY_PORT;
    private Thread LISTENER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NOT SURE IF NEEDED
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void display(View view) {
        EditText fail = (EditText) findViewById(R.id.editText6); //should be Text or popup
        EditText name = (EditText) findViewById(R.id.editText2);
        EditText password = (EditText) findViewById(R.id.editText4);

        String user = name.getText().toString();
        String pwd = password.getText().toString();

        if (pwd.equals("rosales") && user.equals("999")) {
            //TODO -- Add check so that user is within size of CLK[] ARR
            MY_PORT = Integer.parseInt(user); //same as ID

            //Expecting initResponse=CLK+MSG, MSG=OK or ACK
            // i.e. "[1, 0 , 2, 1, 2]!!ACK"
            //TODO -- Add check if MSG contains ERROR (retry?) or ANS doesnot have enough fields
            String answer = ServerReq.out(MY_PORT, CLK, "INIT");
            String[] fields = answer.split("!!");
            String MSG = fields[1];

            //http://stackoverflow.com/a/7646415/4570161
            String[] vector = fields[0].replaceAll("\\[", "").replaceAll("\\]", "").split(",");
            CLK = new Integer[vector.length];
            for (int i = 0; i < vector.length; i++) {
                try {
                    CLK[i] = Integer.parseInt(vector[i]);
                }
                catch (NumberFormatException nfe) {}
            }

            //Start listening for any incoming MSGs from either server or peers
            //TODO -- gotta save thread pointer somehow to access incoming MSGs?
            new Thread(new ListenerThread(MY_PORT)).start();

        }
        else {
            fail.setVisibility(View.VISIBLE);
        }
    }
}






