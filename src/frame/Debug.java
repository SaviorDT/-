package frame;

import java.util.concurrent.TimeUnit;

public class Debug extends Thread {
	public void run() {
		boolean a = true;
		String text = null;
		while(a) {
			text = Main.diaryta.getText();
			if(!text.equals(null)) {
				if(text.indexOf("show")!=-1 && text.length()>4) {
					try{
						int ind = text.indexOf("show");
						Main.diaryta.setText(text.substring(0,ind+4));
						Main.tgl.get((int)text.charAt(ind+4)-48).Show();
					}
					catch(Exception e) {}
					
				}
			}
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {}
		}
	}
}
