package frame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Tag {
	int savdidNow=0,sav=0;
	String tmp[] = new String[50];
	
	ArrayList<String> kwds = new ArrayList<>();
	int me = Main.tgl.size();
	static int count = 0, now = 0, mouseOn;
	String name = Main.diaryta.getText().split("\n")[0], text="";
	JLabel namespace = new JLabel(name), prel = new JLabel(),backl = new JLabel();
	JPanel bg = new JPanel();
	boolean saved = true;
	URL url;
	
	public Tag() {
		prel.setOpaque(false); 
		bg.setOpaque(true); bg.setBackground(Color.white);
		backl.setBackground(new Color(208,223,239));
		if(name.equals("")) namespace=new JLabel("new-"+Integer.toString(++count));
		namespace.setOpaque(false); namespace.setBorder(null);
		url=Tag.class.getResource("/images/saved.png");
		Main.read(prel, url);
		
		
		namespace.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Main.tgl.get(now).text = Main.diaryta.getText();
				Main.tgl.get(now).bg.setBackground(new Color(208,223,239));
				now = me;
				Main.tgl.get(now).bg.setBackground(Color.white);
				Main.diaryta.setText(text);
			}
			public void mouseEntered(MouseEvent e) {
				mouseOn = me;
				bg.setBackground(Color.white);
				url=Tag.class.getResource("/images/beforecross.png");
				Main.read(backl, url);
			}
			public void mouseExited(MouseEvent e) {
				if(now != me) bg.setBackground(new Color(208,223,239));	
				backl.setIcon(null);
			}
			public void mouseReleased(MouseEvent e) {
				if(mouseOn != me) {
					if(mouseOn < me) {
						Main.ipn.add(Main.tgl.get(me).bg,mouseOn);
						Main.tgl.add(mouseOn,Main.tgl.get(me));
						Main.tgl.remove(++me);
						for (int i=mouseOn+1;i<me;i++) Main.tgl.get(i).me=i;
						me = mouseOn;
					}
					if(mouseOn > me) {
						Main.ipn.add(Main.tgl.get(me).bg,mouseOn-1);
						Main.tgl.add(mouseOn,Main.tgl.get(me));
						Main.tgl.remove(me);
						for (int i=me; i<mouseOn; i++) {
							Main.tgl.get(i).me=i;
						}
						me = mouseOn-1;
					}
			}}});
		bg.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Main.tgl.get(now).text = Main.diaryta.getText();
				Main.tgl.get(now).bg.setBackground(new Color(208,223,239));
				now = me;
				Main.tgl.get(now).bg.setBackground(Color.white);
				Main.diaryta.setText(text);
			}
			public void mouseEntered(MouseEvent e) {
				mouseOn = me;
				bg.setBackground(Color.white);
				url=Tag.class.getResource("/images/beforecross.png");
				Main.read(backl, url);
			}
			public void mouseExited(MouseEvent e) {
				if(now != me) bg.setBackground(new Color(208,223,239));
				backl.setIcon(null);
			}
			public void mouseReleased(MouseEvent e) {
				if(mouseOn != me) {
					if(mouseOn < me) {
						Main.ipn.add(Main.tgl.get(me).bg,mouseOn);
						Main.tgl.add(mouseOn,Main.tgl.get(me));
						Main.tgl.remove(++me);
						for (int i=mouseOn+1;i<me;i++) Main.tgl.get(i).me=i;
						me = mouseOn;
					}
					if(mouseOn > me) {
						Main.ipn.add(Main.tgl.get(me).bg,mouseOn-1);
						Main.tgl.add(mouseOn,Main.tgl.get(me));
						Main.tgl.remove(me);
						for (int i=me; i<mouseOn; i++) {
							Main.tgl.get(i).me=i;
						}
						me = mouseOn-1;
					}
			}}});
		backl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(Main.tgl.size() > 1) {
					if(!saved) {
						int ret = 2;
						String[] options ={"儲存","不要儲存","取消"};
						ret=JOptionPane.showOptionDialog(Main.mf, "尚未存檔，是否要儲存?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,ret);
						if(ret==0) Main.save();
						if(ret!=2) {
							if(me == now) {
								if(now == Main.tgl.size()-1) {
									now--;
									Main.tgl.get(now).bg.setBackground(Color.white);
									Main.diaryta.setText(Main.tgl.get(now).text);
								}
								else {
									Main.tgl.get(now+1).bg.setBackground(Color.white);
									Main.diaryta.setText(Main.tgl.get(now+1).text);
								}
							}
							if(me < now) now--;
							Main.ipn.remove(bg);
							int panelCount = Main.tgl.size();
							for(int i=me+1;i<panelCount;i++) Main.tgl.get(i).me--;
							Main.tgl.remove(me);
						}
					}
					else {
						if(me == now) {
							if(now == Main.tgl.size()-1) {
								now--;
								Main.tgl.get(now).bg.setBackground(Color.white);
								Main.diaryta.setText(Main.tgl.get(now).text);
							}
							else {
								Main.tgl.get(now+1).bg.setBackground(Color.white);
								Main.diaryta.setText(Main.tgl.get(now+1).text);
							}
						}
						if(me < now) now--;
						Main.ipn.remove(bg);
						int panelCount = Main.tgl.size();
						for(int i=me+1;i<panelCount;i++) Main.tgl.get(i).me--;
						Main.tgl.remove(me);
					}
				}
				Main.ipn.updateUI();
			}
			public void mouseEntered(MouseEvent e) {
				mouseOn = me;
				bg.setBackground(Color.white);
				url=Tag.class.getResource("/images/aftercross.png");
				Main.read(backl, url);
			}
			public void mouseExited(MouseEvent e) {
				if(now != me) bg.setBackground(new Color(208,223,239));
				backl.setIcon(null);
			}});
		
		GroupLayout bggp = new GroupLayout(bg);
		bggp.setVerticalGroup(
				bggp.createParallelGroup(Alignment.CENTER)
				.addComponent(prel)
				.addComponent(namespace)
				.addComponent(backl)
				);
		bggp.setHorizontalGroup(
					bggp.createSequentialGroup()
							.addComponent(prel,23,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
							.addComponent(namespace,0,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(backl,12,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
							.addContainerGap());
		bg.setLayout(bggp);
		Main.ipn.add(bg);
		if (Main.tgl.size() > 0) Main.tgl.get(now).bg.setBackground(new Color(208,223,239));
		now = me;
		bg.setBackground(Color.white);
		Main.ipn.updateUI();
	}
	public void Show() {
		Main.tgl.get(now).text = Main.diaryta.getText();
		Main.tgl.get(now).bg.setBackground(new Color(208,223,239));
		now = me;
		Main.tgl.get(now).bg.setBackground(Color.white);
		Main.diaryta.setText(text);
	}
	public void Close() {
		if(Main.tgl.size() > 1) {
			if(!saved) {
				int ret = 2;
				String[] options ={"儲存","不要儲存","取消"};
				ret=JOptionPane.showOptionDialog(Main.mf, "尚未存檔，是否要儲存?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,ret);
				if(ret==0) Main.save();
				if(ret!=2) {
					if(me == now) {
						if(now == Main.tgl.size()-1) {
							now--;
							Main.tgl.get(now).bg.setBackground(Color.white);
							Main.diaryta.setText(Main.tgl.get(now).text);
						}
						else {
							Main.tgl.get(now+1).bg.setBackground(Color.white);
							Main.diaryta.setText(Main.tgl.get(now+1).text);
						}
					}
					if(me < now) now--;
					Main.ipn.remove(bg);
					int panelCount = Main.tgl.size();
					for(int i=me+1;i<panelCount;i++) Main.tgl.get(i).me--;
					Main.tgl.remove(me);
				}
			}
			else {
				if(me == now) {
					if(now == Main.tgl.size()-1) {
						now--;
						Main.tgl.get(now).bg.setBackground(Color.white);
						Main.diaryta.setText(Main.tgl.get(now).text);
					}
					else {
						Main.tgl.get(now+1).bg.setBackground(Color.white);
						Main.diaryta.setText(Main.tgl.get(now+1).text);
					}
				}
				if(me < now) now--;
				Main.ipn.remove(bg);
				int panelCount = Main.tgl.size();
				for(int i=me+1;i<panelCount;i++) Main.tgl.get(i).me--;
				Main.tgl.remove(me);
			}
		}
		Main.ipn.updateUI();
	}
	public void unsave() {
		saved = false;
		url=Tag.class.getResource("/images/warning.png");
		Main.read(prel, url);
	}

	public void save() {
		saved = true;
		url=Tag.class.getResource("/images/saved.png");
		Main.read(prel, url);
	}
}