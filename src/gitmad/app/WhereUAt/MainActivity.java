package gitmad.app.WhereUAt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	
	public final static String EXTRA_MESSAGE = "gitmad.app.WhereUAt";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View findMeButton = findViewById(R.id.find_me_button);
        findMeButton.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View v) {
		Intent i = new Intent(this, ResultActivity.class); //private intent
		EditText editText = (EditText)findViewById(R.id.editText1);
		String message = editText.getText().toString();
		i.putExtra(EXTRA_MESSAGE, message);
		startActivity(i);
		Log.d("Demo", "Find me button pressed");		
	}
}