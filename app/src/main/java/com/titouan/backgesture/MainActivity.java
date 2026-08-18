package com.titouan.backgesture;
import android.app.Activity; import android.os.Bundle; import android.provider.Settings; import android.widget.*;
public class MainActivity extends Activity {
 SeekBar seek; TextView value;
 public void onCreate(Bundle b){super.onCreate(b); LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.VERTICAL);r.setPadding(32,32,32,32);
  TextView t=new TextView(this);t.setText("Back Gesture — zone haute exclue");t.setTextSize(22);r.addView(t); value=new TextView(this);value.setTextSize(18);r.addView(value);
  seek=new SeekBar(this);seek.setMax(50);r.addView(seek); Button a=new Button(this);a.setText("Appliquer et redémarrer SystemUI");r.addView(a);
  TextView i=new TextView(this);i.setText("0 % = aucun changement\n30 % = les 30 % supérieurs sont exclus\n50 % = moitié supérieure exclue\n\nLe hook agit uniquement dans SystemUI.");r.addView(i);
  int cur=0;try{cur=Settings.Secure.getInt(getContentResolver(),"gesture_back_exclude_top",0);}catch(Exception ignored){} seek.setProgress(Math.max(0,Math.min(50,cur)));update();
  seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar s,int p,boolean f){update();}public void onStartTrackingTouch(SeekBar s){}public void onStopTrackingTouch(SeekBar s){}});
  a.setOnClickListener(v->{int p=seek.getProgress();try{Process su=Runtime.getRuntime().exec(new String[]{"su"});java.io.DataOutputStream d=new java.io.DataOutputStream(su.getOutputStream());d.writeBytes("settings put secure gesture_back_exclude_top "+p+"\nkillall com.android.systemui\nexit\n");d.flush();su.waitFor();Toast.makeText(this,"Appliqué : "+p+" %",Toast.LENGTH_SHORT).show();}catch(Exception e){Toast.makeText(this,"Root requis : "+e,Toast.LENGTH_LONG).show();}}); setContentView(r); }
 void update(){value.setText(seek.getProgress()+" %");}
}
