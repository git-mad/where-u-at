package gitmad.app.WhereUAt;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity implements OnClickListener {
	private String result = "not set";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		Log.d("Demo", "Atlanta, GA : the location");
		TextView resultView = (TextView) this.findViewById(R.id.result_value_label);

		View returnButton = findViewById(R.id.back_to_menu_button);
		returnButton.setOnClickListener(this);
		
		final Geocoder geocoder = new Geocoder(this);
		
		Intent i = getIntent();
		String location = i.getStringExtra(MainActivity.EXTRA_MESSAGE);
		try
		{
			List<Address> addresses = geocoder.getFromLocationName(location, 1);
			if(addresses != null && !addresses.isEmpty())
			{
				Address address = addresses.get(0);
				String message = "";
				if(address.getLocality() != null)
				{
					message = String.format("%s", address.getLocality());
				}
				else
				{
					message = "Unable to locate";
				}
				resultView.setText(message);
			}
			else
			{
				resultView.setText("Unable to locate");
			}
		}
		catch (IOException e)
		{
			Log.d("Demo", "IOException");
		}
	}
	
	@Override
	public void onClick(View arg0) {
		this.finish();		
	}

}
