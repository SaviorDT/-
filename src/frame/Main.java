package frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

class mka extends KeyAdapter{
	public void keyPressed(KeyEvent e) {
		if(e.getModifiers()==2)
			switch (e.getKeyCode()){
			case 83:
				Main.save();
				break;
			case 79:
				Main.open();
				break;
			case 78:
				Main.opennew();
				break;
			case 90:
				Main.undo();
				break;
			case 89:
				Main.redo();
				break;
			case 70:
			case 71:
				Main.srnow = 0; Main.srfilenow = 0;
				Main.searfm.setVisible(true);
				Main.searfd.requestFocus();
				break;
			case 72:
				JOptionPane.showMessageDialog(Main.mf,"敬請期待更新...",null,JOptionPane.PLAIN_MESSAGE);
				break;
			case 80:
				Main.encrypt=true;
				Main.save();
				break;
			case 87:
				Main.tgl.get(Tag.now).Close();
				break;
			}
	}
}

class savedid extends Thread{
		static boolean renew=false,fdo=true,flag=true;
		
		public void run() {
			while(flag) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("savedid.sleep:Wrong!");
				}
				if(!Main.diaryta.getText().equals(Main.tgl.get(Tag.now).tmp[Main.tgl.get(Tag.now).savdidNow])) {
					Main.tgl.get(Tag.now).unsave();
					Main.tgl.get(Tag.now).tmp[++Main.tgl.get(Tag.now).sav]=Main.diaryta.getText();
					if(Main.tgl.get(Tag.now).sav>=50) Main.tgl.get(Tag.now).sav=0;
					Main.tgl.get(Tag.now).savdidNow=Main.tgl.get(Tag.now).sav-1;
					if(Main.tgl.get(Tag.now).savdidNow==-1) Main.tgl.get(Tag.now).savdidNow=49;
					fdo=true;
					try {
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						System.out.println("savedid.sleep:Wrong!");
					}
				}
			}
		}
}

public class Main {
	public static JFrame mf = new JFrame();
	static JTextArea diaryta = new JTextArea();
	static JTextField mburlt = new JTextField("D:/Savior_TD");
	
	static JPanel ipn = new JPanel();
	static ArrayList<Tag> tgl = new ArrayList<>();
	
	static int itemNow = 0;
	
	static void read(JButton jb, URL url) {
		ImageIcon icon = new ImageIcon(url);
		jb.setIcon(icon);
	}
	static void read(JLabel jb, URL url) {
		ImageIcon icon = new ImageIcon(url);
		jb.setIcon(icon);
	}
//-------------------------------------------------------------------------------------
	static boolean encrypt = false;
	
	static void opennew() {
			tgl.get(Tag.now).text = diaryta.getText();
			diaryta.setText("");
			savedid.renew=true;
			tgl.add(new Tag());
	}
	static void open() {
		JFileChooser jfc = new JFileChooser(mburlt.getText());
		jfc.setMultiSelectionEnabled(true);
		jfc.showOpenDialog(null);
		File[] files = jfc.getSelectedFiles();
		int size = files.length;
		String str = "";
		FileReader fr = null;
		for(int i=0;i<size;i++) {
			str = files[i].getName().substring(0,files[i].getName().lastIndexOf("."));
			try {
				fr = new FileReader(mburlt.getText()+"/"+str+".sdt");
			} catch (FileNotFoundException e) {
				System.out.println("Main.open.FileReader:Wrong!");
			}
			BufferedReader br = new BufferedReader(fr);String pwdd = null;
			String opentext = null;
			try {
				opentext = br.readLine();
			} catch (IOException e1) {
				System.out.println("Main.decrypt.br.readLine:Wrong!");
			}
			if(opentext.equals("%已加密%")) {
				pwdd = JOptionPane.showInputDialog(mf,"請輸入密碼",str,JOptionPane.PLAIN_MESSAGE);
				try {
					opentext=br.readLine();
				} catch (IOException e) {
					System.out.println("open.br.read:wrong!");
				}}
			else pwdd= "abc123ㄅㄆㄇ";
			if(pwdd!=null) {
				char[] tomin = pwdd.toCharArray();
				Queue<Integer> charque= new LinkedList<>();
				int now=0,j=0;
				while(now<opentext.length()-1) {
					charque.add(Integer.parseInt(opentext.substring(now+1,now+(int)opentext.charAt(now)-47))-tomin[j++]);
					now+=opentext.charAt(now)-47;
					if(j>=pwdd.length()) j=0;
				}
				String text = "";
				while(!charque.isEmpty()) {
					int hi=charque.poll();
					text+=(char)hi;
				}
				tgl.get(Tag.now).text = diaryta.getText();
				diaryta.setText(text);
				tgl.add(new Tag());
			}
		}
	}
	static void open(File file) {
		FileReader fr = null;
			try {
				fr = new FileReader(file);
			} catch (FileNotFoundException e) {
				System.out.println("Main.open.FileReader:Wrong!");
			}
			BufferedReader br = new BufferedReader(fr);String pwdd = null;
			String opentext = null;
			try {
				opentext = br.readLine();
			} catch (IOException e1) {
				System.out.println("Main.decrypt.br.readLine:Wrong!");
			}
			if(opentext.equals("%已加密%")) {
				pwdd = JOptionPane.showInputDialog(mf,"請輸入密碼",file.getName(),JOptionPane.PLAIN_MESSAGE);
				try {
					opentext=br.readLine();
				} catch (IOException e) {
					System.out.println("open.br.read:wrong!");
				}}
			else pwdd= "abc123ㄅㄆㄇ";
			if(pwdd!=null) {
				char[] tomin = pwdd.toCharArray();
				Queue<Integer> charque= new LinkedList<>();
				int now=0,j=0;
				while(now<opentext.length()-1) {
					charque.add(Integer.parseInt(opentext.substring(now+1,now+(int)opentext.charAt(now)-47))-tomin[j++]);
					now+=opentext.charAt(now)-47;
					if(j>=pwdd.length()) j=0;
				}
				String text = "";
				while(!charque.isEmpty()) {
					int hi=charque.poll();
					text+=(char)hi;
				}
				tgl.get(Tag.now).text = diaryta.getText();
				diaryta.setText(text);
				tgl.add(new Tag());
			}
	}
	static void save() {
		String hihi = diaryta.getText();
		if(hihi.length()<1) JOptionPane.showMessageDialog(mf, "沒有字的檔案不用儲存啦~");
		if(hihi.charAt(0)=='2'&&hihi.length()>5) hihi = String.valueOf((Integer.parseInt(hihi.substring(0,4))-1911))+hihi.substring(4);
		
		File ThisFile = new File(mburlt.getText());
		String[] ThisFile_dirs = ThisFile.list();
		Pattern save_file_p = Pattern.compile("[^0-9]");
		Matcher save_file_m = null;
		try {
			if(ThisFile_dirs.length>0) {
				save_file_m = save_file_p.matcher(ThisFile_dirs[ThisFile_dirs.length-1]);
			}
		}
		catch(Exception e) {}
		int save_file_date = 0;
		int tosave_date = 2147483647;
		try {save_file_date = Integer.parseInt(save_file_m.replaceAll(""));}
		catch(Exception e) {}
		Matcher tosave_m = save_file_p.matcher(hihi.split("\n")[0]);
		try {tosave_date = Integer.parseInt(tosave_m.replaceAll(""));}
		catch(Exception e) {}
		if(tosave_date<=save_file_date) {
			String[] option = {"仍要儲存","取消除存"};
			if(JOptionPane.showOptionDialog(mf, "此檔案較檔案庫的最新日期舊，請確認是否有錯誤", "系統訊息", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, option, "test")!=0) return;
		}
		String pwd = null;
		if(encrypt) {
			pwd = JOptionPane.showInputDialog(mf,"請輸入密碼","加密",JOptionPane.PLAIN_MESSAGE);
		}
		else {
			pwd = "abc123ㄅㄆㄇ";
		}
		if(pwd!=null) {
			char[] toadd = pwd.toCharArray();
			char[] text = hihi.toCharArray();
			int now=0,len=toadd.length;
			for(int i=0;i<text.length;i++) {
				text[i]+=toadd[now++];
				if(now>=len) now=0;
			}
			String sav=hihi.split("\n")[0].replace('/','.');	
			int textlen=text.length;
			int[] last = new int[textlen];
			for(int i=0;i<textlen;i++) {
				int ilen = intlen(text[i]);
				last[i]=text[i]+ilen*deg(ilen);
			}
			String tosave="";
			if(encrypt)
			tosave="%已加密%\n";
			for(int i=0;i<last.length;i++) {
				tosave+=String.valueOf(last[i]);
			}
			tosave = savekeyword(tosave, hihi);
			FileWriter fw = null;
			try {
				fw = new FileWriter(mburlt.getText()+"/"+sav+".sdt");
				fw.write(tosave);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				File dir = new File(mburlt.getText());
				dir.mkdirs();
				try {
					fw = new FileWriter(mburlt.getText()+"/"+sav+".sdt");
					fw.write(tosave);
					fw.flush();
					fw.close();
				} catch (IOException e1) {
					System.out.println("Main.encsave.FileWriter:Wrong");
				}
			}
			encrypt = false;
			
			if(!diaryta.getText().equals(tgl.get(Tag.now).tmp[tgl.get(Tag.now).savdidNow])) {
				tgl.get(Tag.now).unsave();
				tgl.get(Tag.now).tmp[++tgl.get(Tag.now).sav]=diaryta.getText();
				if(tgl.get(Tag.now).sav>=50) tgl.get(Tag.now).sav=0;
				tgl.get(Tag.now).savdidNow=tgl.get(Tag.now).sav-1;
				if(tgl.get(Tag.now).savdidNow==-1) tgl.get(Tag.now).savdidNow=49;
				savedid.fdo=true;
			}
			tgl.get(Tag.now).save();
		}
	}
	static void save(String hihi) {
		if(hihi.length()<1) JOptionPane.showMessageDialog(mf, "沒有字的檔案不用儲存啦~");
		if(hihi.charAt(0)=='2') hihi = String.valueOf((Integer.parseInt(hihi.substring(0,4))-1911))+hihi.substring(4);
		
		File ThisFile = new File(mburlt.getText());
		String[] ThisFile_dirs = ThisFile.list();
		Pattern save_file_p = Pattern.compile("[^0-9]");
		Matcher save_file_m = save_file_p.matcher(ThisFile_dirs[ThisFile_dirs.length-1]);
		int save_file_date = 0;
		int tosave_date = 2147483647;
		try {save_file_date = Integer.parseInt(save_file_m.replaceAll(""));}
		catch(NumberFormatException e) {}
		Matcher tosave_m = save_file_p.matcher(hihi.split("\n")[0]);
		try {tosave_date = Integer.parseInt(tosave_m.replaceAll(""));}
		catch(NumberFormatException e) {}
		if(tosave_date<=save_file_date) {
			String[] option = {"仍要儲存","取消除存"};
			if(JOptionPane.showOptionDialog(mf, "此檔案較檔案庫的最新日期舊，請確認是否有錯誤", "系統訊息", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, option, "test")!=0) return;
		}
		String pwd = "abc123ㄅㄆㄇ";
		char[] toadd = pwd.toCharArray();
		char[] text = hihi.toCharArray();
		int now = 0, len = toadd.length;
		for (int i = 0; i < text.length; i++) {
			text[i] += toadd[now++];
			if (now >= len)
				now = 0;
		}
		String sav = hihi.split("\n")[0].replace('/', '.');
		int textlen = text.length;
		int[] last = new int[textlen];
		for (int i = 0; i < textlen; i++) {
			int ilen = intlen(text[i]);
			last[i] = text[i] + ilen * deg(ilen);
		}
		String tosave = "";
		for (int i = 0; i < last.length; i++) {
			tosave += String.valueOf(last[i]);
		}
		tosave = savekeyword(tosave, hihi);
		FileWriter fw = null;
		try {
			fw = new FileWriter(mburlt.getText()+"/" + sav + ".sdt");
			fw.write(tosave);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			File dir = new File(mburlt.getText());
			dir.mkdirs();
			try {
				fw = new FileWriter(mburlt.getText()+"/" + sav + ".sdt");
				fw.write(tosave);
				fw.flush();
				fw.close();
			} catch (IOException e1) {
				System.out.println("Main.encsave.FileWriter:Wrong");
			}
		}
	}
	static String savekeyword(String tosave, String old) {
		if(old.contains("\n#:")) {
			String kwd = old.split("\n#:")[old.split("\n#:").length-1];
			char[] kwds = kwd.toCharArray();
			String toret = "\n00038364000377490002338300012289"; //關鍵字、
			for(int i=0;i<kwds.length;i++) {
				String tmp = String.valueOf((int)kwds[i]);
				while(tmp.length()<8) tmp = "0"+tmp;
				toret += tmp;
			}
			tosave += toret + "00012289";
		}
		return tosave;
	}
	static int intlen(int in) {
		int lent=0;
		while(in>0) {
			lent++;
			in/=10;
		}
		return lent;
	}
	static int deg(int in) {
		int out =1;
		for(int i=0;i<in;i++) {
			out*=10;
		}
		return out;
	}
//----------------------------------------------------------------------------------------
	static void undo() {
		if(Main.tgl.get(Tag.now).savdidNow!=Main.tgl.get(Tag.now).sav+1) {
			Main.tgl.get(Tag.now).savdidNow--;
			if(savedid.fdo) {
				Main.tgl.get(Tag.now).tmp[Main.tgl.get(Tag.now).sav++]=diaryta.getText();
				if(Main.tgl.get(Tag.now).sav>=50) Main.tgl.get(Tag.now).sav=0;
				savedid.fdo=false;
			}
			if(Main.tgl.get(Tag.now).savdidNow<0) Main.tgl.get(Tag.now).savdidNow=49;
			Main.tgl.get(Tag.now).sav=Main.tgl.get(Tag.now).savdidNow+1;
			if(Main.tgl.get(Tag.now).sav>=50) Main.tgl.get(Tag.now).sav=0;
			diaryta.setText(Main.tgl.get(Tag.now).tmp[Main.tgl.get(Tag.now).savdidNow]);
		}
	}
	static void redo() {
		if(Main.tgl.get(Tag.now).savdidNow!=Main.tgl.get(Tag.now).sav) {
			Main.tgl.get(Tag.now).savdidNow++;
			if(Main.tgl.get(Tag.now).savdidNow<0) Main.tgl.get(Tag.now).savdidNow=49;
			Main.tgl.get(Tag.now).sav=Main.tgl.get(Tag.now).savdidNow+1;
			if(Main.tgl.get(Tag.now).sav>=50) Main.tgl.get(Tag.now).sav=0;
			if(savedid.fdo) {
				Main.tgl.get(Tag.now).tmp[Main.tgl.get(Tag.now).sav++]=diaryta.getText();
				if(Main.tgl.get(Tag.now).sav>=50) Main.tgl.get(Tag.now).sav=0;
				savedid.fdo=false;
			}
			diaryta.setText(Main.tgl.get(Tag.now).tmp[Main.tgl.get(Tag.now).savdidNow]);
		}
	}
//-------------------------------------------------------------------------------------
	
		static JFrame searfm = new JFrame("尋找/取代");
		static JTextField searfd = new JTextField(),findfd = new JTextField();
		static JCheckBox srkwd = new JCheckBox("只搜尋關鍵字"), srthis = new JCheckBox("搜尋本工作"), srall = new JCheckBox("尋找所有工作"), srselect = new JCheckBox("尋找特定檔案");
		static File[] srfiles;
		static boolean srnums[],srselnums[];
		static int srnow,srfilenow;
		static boolean searchfor() {
			String tmp = searfd.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\");
			if(diaryta.getText().indexOf(tmp,diaryta.getSelectionEnd())==-1) {
				try {
					if(srnow<srnums.length || srfilenow<srfiles.length) srNextPage();
					else {
						return false;
					}
				}
				catch(NullPointerException e) {
					return false;
				}
					
			}
			diaryta.setCaretPosition(diaryta.getText().indexOf(tmp,diaryta.getSelectionEnd()));
			diaryta.setSelectionEnd(diaryta.getCaretPosition()+tmp.length());
			return true;
		}
		static boolean searchformark(String tmp) {
			if(diaryta.getText().indexOf(tmp,diaryta.getSelectionEnd())==-1) return false;
			diaryta.setCaretPosition(diaryta.getText().indexOf(tmp,diaryta.getSelectionEnd()));
			diaryta.setSelectionEnd(diaryta.getCaretPosition()+tmp.length());
			return true;
		}
		static void replacefor() {
			int pos = diaryta.getCaretPosition();
			String replacement = findfd.getText(),old=diaryta.getText();
			String newtext = old.substring(0,diaryta.getSelectionStart())+replacement+old.substring(diaryta.getSelectionEnd());
			diaryta.setText(newtext);
			diaryta.setCaretPosition(pos);
			tgl.get(Tag.now).unsave();
			}
		static void srNextPage() {
			int next = srnext();
			if(srkwd.isSelected()) {
				if(next != -1) {
					String text = tgl.get(next).text;
					if(text.indexOf(searfd.getText(),text.lastIndexOf("\n#:")) != -1) {
						int ind = text.indexOf(searfd.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\"),text.lastIndexOf("\n#:"));
						if((text.charAt(ind-1) == '、'||text.charAt(ind-1) == ':')&&(text.charAt(ind+searfd.getText().length())=='、'||text.charAt(ind+searfd.getText().length())=='\n')) {
							tgl.get(next).Show();
							diaryta.setCaretPosition(text.lastIndexOf("\n#:"));
							searchfor();
						}
					}
				}
				else {
					String text = srNextFilekwd();
					if(text.indexOf("、"+searfd.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\")+"、",text.lastIndexOf("\n#:")) != -1) {
						open(srfiles[srfilenow-1]);
						diaryta.setCaretPosition(text.lastIndexOf("\n#:"));
						searchfor();
					}
				}
			}
			else {
				if(next != -1) {
					String text = tgl.get(next).text;
					if(text.indexOf(searfd.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\")) != -1) {
						tgl.get(next).Show();
						diaryta.setCaretPosition(0);
						searchfor();
					}
				}
				else {
					String text = srNextFile();
					if(text.indexOf(searfd.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\")) != -1) {
						open(srfiles[srfilenow-1]);
						searchfor();
					}
				}
			}
		}
		static void srsetfiles() {
			srnums = new boolean[tgl.size()];
			if(srthis.isSelected()) srnums[Tag.now]=true;
			if(srall.isSelected()) Arrays.fill(srnums, true);
			if(srselect.isSelected()) {
				for(int i=0;i<srselnums.length;i ++) if(srselnums[i]) srnums[i] = true;
			}
		}
		static int srnext() {
			boolean tru = true;
			while(tru) {
				if(srnow<srnums.length)	if(srnums[srnow++]) return srnow-1;
			}
			return -1;
		}
		static String srNextFile() {
			FileReader fr = null;
			try {
				fr = new FileReader(srfiles[srfilenow++]);
			} catch (FileNotFoundException e2) {
				System.out.println("srNextFile.FileReader:wrong!");
			}
			BufferedReader br = new BufferedReader(fr);
			String pwdd = null;
			String opentext = null;
			try {
				opentext = br.readLine();
			} catch (IOException e1) {
				System.out.println("Main.srNextFile.br.readLine:Wrong!");
			}
			if (opentext.equals("%已加密%")) {
				pwdd = JOptionPane.showInputDialog(mf, "請輸入密碼", srfiles[srfilenow-1].getName(), JOptionPane.PLAIN_MESSAGE);
				try {
					opentext = br.readLine();
				} catch (IOException e) {
					System.out.println("open.br.read:wrong!");
				}
			} else
				pwdd = "abc123ㄅㄆㄇ";
			if (pwdd != null) {
				char[] tomin = pwdd.toCharArray();
				Queue<Integer> charque = new LinkedList<>();
				int now = 0, j = 0;
				while (now < opentext.length() - 1) {
					charque.add(Integer.parseInt(opentext.substring(now + 1, now + (int) opentext.charAt(now) - 47))
							- tomin[j++]);
					now += opentext.charAt(now) - 47;
					if (j >= pwdd.length())
						j = 0;
				}
				String text = "";
				while (!charque.isEmpty()) {
					int hi = charque.poll();
					text += (char) hi;
				}
				return text;
			}
			return "";
		}
		static String srNextFilekwd() {
			FileReader fr = null;
			try {
				fr = new FileReader(srfiles[srfilenow++]);
			} catch (FileNotFoundException e2) {
				System.out.println("srNextFile.FileReader:wrong!");
			}
			BufferedReader br = new BufferedReader(fr);
			String pwdd = null;
			String opentext = null;
			try {
				opentext = br.readLine();
			} catch (IOException e1) {
				System.out.println("Main.srNextFile.br.readLine:Wrong!");
			}
			if (opentext.equals("%已加密%")) {
				pwdd = JOptionPane.showInputDialog(mf, "請輸入密碼", srfiles[srfilenow-1].getName(), JOptionPane.PLAIN_MESSAGE);
				try {
					opentext = br.readLine();
				} catch (IOException e) {
					System.out.println("open.br.read:wrong!");
				}
			} else
				pwdd = "abc123ㄅㄆㄇ";
			try {
				opentext = br.readLine();
			} catch (IOException e) {
				System.out.println("open.br.read:wrong!");
			}
			if (pwdd != null) {
				Queue<Integer> charque = new LinkedList<>();
				int now = 0;
				while (now < opentext.length() - 1) {
					charque.add(Integer.parseInt(opentext.substring(now, now + 8)));
					now += 8;
				}
				String text = "";
				while (!charque.isEmpty()) {
					int hi = charque.poll();
					text += (char) hi;
				}
				return text;
			}
			return "";
		}
//----------------------------------------------------------------------------------------
	public static void main(String[] args) {
		UIManager.LookAndFeelInfo[] lafinfo = UIManager.getInstalledLookAndFeels();
		String laf = lafinfo[3].getClassName();
		try {
			UIManager.setLookAndFeel(laf);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.out.println("Main,LookAndFeel:wrong");
		}
		
		mf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mf.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.45),(int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.35));
		mf.setSize(200,150);
		mf.setLayout(null);
		mf.getContentPane().setBackground(SystemColor.info);
		mf.addKeyListener(new mka());

		JFrame savefm = new JFrame();
		JPanel savepan = new JPanel();
		JButton saveYes = new JButton("確認"), saveCancel = new JButton("取消"),saveAll = new JButton("全部選擇"),saveAlln = new JButton("全不選擇");
		mf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				GroupLayout savefg = new GroupLayout(savefm.getContentPane()), savepg = new GroupLayout(savepan);
				ArrayList<JCheckBox> checkal = new ArrayList<>();
				boolean saved = true;
				int size = tgl.size();
				for(int i=0;i<size;i++) if(!tgl.get(i).saved) saved = false;
				if(saved==false) {
					int checknum[] = new int[tgl.size()];
					int checkednow = 0;
					for (int i = 0; i < tgl.size(); i++) {
						if (!tgl.get(i).saved) {
							checkal.add(new JCheckBox(tgl.get(i).namespace.getText()));
							checknum[checkednow++] = i;
						}
					}
					savefm.setLocationRelativeTo(mf);
					savefm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					savepan.setBorder(BorderFactory.createTitledBorder("選擇要儲存的日記"));
					ParallelGroup savepgp = savepg.createParallelGroup();
					SequentialGroup savepgs = savepg.createSequentialGroup();
					for (int i = 0; i < checkal.size(); i++) {
						savepgp.addComponent(checkal.get(i));
						savepgs.addComponent(checkal.get(i));
					}
					savepg.setVerticalGroup(savepgs);
					savepg.setHorizontalGroup(savepgp);
					savefg.setHorizontalGroup(savefg.createSequentialGroup()
							.addContainerGap()
							.addGroup(savefg.createParallelGroup(Alignment.CENTER)
								.addComponent(savepan,0,75,10000)
								.addGroup(savefg.createSequentialGroup()
										.addGap(0,10,10000)
										.addGroup(savefg.createParallelGroup()
												.addComponent(saveAll)
												.addComponent(saveAlln))
										.addGap(15,15,15))
								.addGroup(savefg.createSequentialGroup()
										.addContainerGap()
										.addComponent(saveYes)
										.addGap(0,10,10000)
										.addComponent(saveCancel)
										.addContainerGap()))
							.addContainerGap());
					savefg.setVerticalGroup(savefg.createSequentialGroup()
							.addContainerGap()
							.addComponent(savepan)
							.addGap(7)
							.addComponent(saveAll)
							.addComponent(saveAlln)
							.addGap(10)
							.addGroup(savefg.createParallelGroup()
									.addComponent(saveYes)
									.addComponent(saveCancel))
							.addContainerGap());
					savefm.setLayout(savefg);
					saveYes.requestFocus();
					savefm.setSize(170,checkal.size()*23+160);
					savepan.setLayout(savepg);
					savefm.setResizable(false);
					savefm.setVisible(true);

					saveYes.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							tgl.get(Tag.now).text = diaryta.getText();
							for (int i = 0; i < checkal.size(); i++)
								if (checkal.get(i).isSelected()) save(tgl.get(checknum[i]).text);
							System.exit(0);
						}});
					saveCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							savefm.dispose();
						}});
					saveAll.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int size = checkal.size();
							for(int i=0;i<size;i++) checkal.get(i).setSelected(true);
						}});
					saveAlln.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int size = checkal.size();
							for(int i=0;i<size;i++) checkal.get(i).setSelected(false);
						}});
				}else System.exit(0);}});
		
		
//		------------------------------------------------------
//		JPanel jpf = new JPanel();
//		jpf.setOpaque(false);
//		jpf.setBounds(0,0,500,500);
//		jpf.setLayout(null);
//
//		JTextArea pta = new JTextArea("載入中");
//		JTextArea pta_vanish = new JTextArea();
//		pta.setBounds(55,40,97,22);
//		pta.setOpaque(false);
//		pta.setBackground(new Color(240,240,240));
//		Font ptaf = new Font("新細明體",Font.BOLD,18);
//		pta.setFont(ptaf);
//		
//		jpf.add(pta_vanish);
//		jpf.add(pta);
//		mf.add(jpf);
		mf.setVisible(true);
//		{
//			do {
//				String pwds = pta.getText() + '.';
//				
//				if(pwds.substring(pwds.indexOf("載")).contains("載入了")) {
//					break;
//				}
//				
//				int pwl = pwds.length();
//				int pcp = pta.getCaretPosition();
//				
//				if(pwds.substring((pwl-6)>0?pwl-6:0).contentEquals("......")) {
//					pwds = pwds.substring(0,pwl-5);
//					pwl-=5;
//				}
//				
//				pta.setText(pwds);
//				pta.setCaretPosition(pcp<=pwl?pcp:pwl);
//				
//				
//				try {
//					TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e) {
//					System.out.println("Main.pth.TimeUnit1 bug");
//				}
//				
//			} while(true);
//			
//			for(int i=0;i<30;i++) {
//				Toolkit.getDefaultToolkit().beep();
//				try {
//					TimeUnit.MILLISECONDS.sleep(69);
//				} catch (InterruptedException e) {
//					System.out.println("Main.pth.TimeUnit2 bug");
//				}
//			}
//			pta.setLocation(40,40);
//			pta.setText("程式已崩潰");
//			
//			while(true) {
//				if(pta.getText().equals("程式已啟動")) {
//					mf.remove(jpf);
//					break;
//				}
//				try {
//					TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e) {
//					System.out.println("Main.pth.TimeUnit3 bug");
//				}
//			}
//		}
//		------------------------------------------------------
		Container mfc = mf.getContentPane();
		GroupLayout mgl = new GroupLayout(mfc);
		mf.getContentPane().setLayout(mgl);
		
		URL url = Main.class.getResource("/images/search.png");
		
		JTextField search = new JTextField();
		Highlighter searchhl = diaryta.getHighlighter();
		HighlightPainter searchhp = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
		search.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10) {
				diaryta.setCaretPosition(0);
				while(searchformark(search.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\"))) {
					try {
						searchhl.addHighlight(diaryta.getSelectionStart(), diaryta.getSelectionEnd(), searchhp);
					} catch (BadLocationException e1) {
						System.out.println("Main.main.search.highlight:Wrong!");
					}
				}
				}
			}
		});
		JButton searchb = new JButton();
		searchb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				diaryta.setCaretPosition(0);
				while(searchformark(search.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\")))
					try {
						searchhl.addHighlight(diaryta.getSelectionStart(), diaryta.getSelectionEnd(), searchhp);
					} catch (BadLocationException e1) {
						System.out.println("Main.main.search.highlight:Wrong!");
					}
			}});
		read(searchb, url);
		search.setText("search...");
		search.setForeground(Color.GRAY);
		search.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
				if(search.getText().equals("search...")&&search.getForeground()==Color.GRAY) {
					search.setText("");
					search.setForeground(Color.BLACK);	
				}
			}
			public void focusLost(FocusEvent e) {
				if(search.getText()==null) {
					search.setForeground(Color.GRAY);
					search.setText("search...");
				}
				}});
		
		JScrollPane diary = new JScrollPane(diaryta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		diary.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
		diary.setOpaque(false);
		diary.getViewport().setOpaque(false);
		diaryta.setOpaque(true);
		diaryta.setBackground(new Color(255,255,255));
		Font diaryf = new Font("標楷體",Font.PLAIN,18);
		diaryta.setFont(diaryf);
		diaryta.setLineWrap(true);
		Thread tath = new savedid();
		diaryta.addKeyListener(new mka());
		diaryta.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if((e.getModifiers()==2&&e.getKeyCode()==86)||!(e.getModifiers()==2)) tgl.get(Tag.now).saved = false;
			}});
		diaryta.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent v) {
				searchhl.removeAllHighlights();
			}});
		
		JMenuBar mb1 = new JMenuBar();
		
		JMenu jm1 = new JMenu("檔案(F)");
		JMenu jm2 = new JMenu("編輯(E)");
		JMenu jm3 = new JMenu("說明(H)");
		
		JMenuItem j1i1 = new JMenuItem("開新檔案　　　　ctrl+N");
		JMenuItem j1i2 = new JMenuItem("開啟舊檔　　　　ctrl+O");
		JMenuItem j1i3 = new JMenuItem("儲存檔案　　　　ctrl+S");
		j1i1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				opennew();
			}});
		j1i2.addActionListener(new ActionListener( ) {
			public void actionPerformed(ActionEvent ev) {
				open();
			}});
		j1i3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				save();
			}});
		
		JMenuItem j2i1 = new JMenuItem("復原　　　　ctrl+Z");
		j2i1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				undo();
			}});
		JMenuItem j2i2 = new JMenuItem("重做　　　　ctrl+Y");
		j2i2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				redo();
			}});
		JMenuItem j2i3 = new JMenuItem("尋找　　　　ctrl+F");
		j2i3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				srnow = 0; srfilenow = 0;
				searfm.setVisible(true);
				searfd.requestFocus();
			}});
		JMenuItem j2i4 = new JMenuItem("取代　　　　ctrl+G");
		j2i4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				srnow = 0; srfilenow = 0;
				searfm.setVisible(true);
				searfd.requestFocus();
			}});
		JMenuItem j2i5 = new JMenuItem("日曆　　　　ctrl+H");
		j2i5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mf,"敬請期待更新...",null,JOptionPane.PLAIN_MESSAGE);
			}});
		JMenuItem j2i6 = new JMenuItem("加密　　　　ctrl+P");
		j2i6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				encrypt=true;
				save();
			}});
		JFrame description = new JFrame("說明頁面");
		JMenuItem j3i1 = new JMenuItem("點此開啟說明頁面");
		j3i1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				description.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				description.setSize(300,300);
				description.setLocationRelativeTo(mf);
				JTextArea desjt = new JTextArea();
				desjt.setEditable(false);
				desjt.setLineWrap(true);
				desjt.setText("欸...基本上這個程式設計的很簡單，應該不需要解釋啦~\n\n主要有三個東西要講一講\n第一是右上角輸入以後按enter或按旁邊的按鈕可以標記。\n第二是ctrl+w可以關閉當前工作\n第三是關鍵字功能，在最後一行以｢#:｣開頭並將關鍵字以、分開。\n關鍵字範例:(記得要和本文空一行)\n#:關鍵字1、關鍵字2、關鍵字3");
				description.add(desjt);
				description.setVisible(true);
			}});
		
		mb1.add(jm1);
		mb1.add(jm2);
		mb1.add(jm3).add(j3i1);
		
		jm1.add(j1i1);
		jm1.add(j1i2);
		jm1.add(j1i3);
		jm2.add(j2i1);
		jm2.add(j2i2);
		jm2.addSeparator();
		jm2.add(j2i3);
		jm2.add(j2i4);
		jm2.add(j2i5);
		jm2.addSeparator();
		jm2.add(j2i6);

		JLabel mbl = new JLabel();
		url = Main.class.getResource("/images/MenuBar.jpg");
		read(mbl, url);
		JLabel mburll = new JLabel("存檔路徑：");
		mburll.setBounds(170,0,100,23);
		mf.add(mburll);
		mburlt.setBounds(230,0,150,23);
		mburlt.addKeyListener(new mka());
		mf.add(mburlt);
//---------------------------------------------------------------------------------------------
		JPanel ipnbs = new JPanel();
		ipn.setBackground(new Color(208,223,239)); ipnbs.setBackground(new Color(208,223,239));
		ipn.setOpaque(true); ipnbs.setOpaque(true);
		ipnbs.setLayout(null);
		JLabel ipnbr = new JLabel(), ipnbl = new JLabel();
		url = Main.class.getResource("/images/right.png");
		read(ipnbr,url);
		url = Main.class.getResource("/images/left.png");
		read(ipnbl,url);
		ipnbr.setBounds(0,1,11,11); ipnbl.setBounds(0,14,11,11);
		
		ipnbr.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(++itemNow >= tgl.size()) itemNow = tgl.size()-1;
				ipn.remove(tgl.get(itemNow-1).bg);
				ipn.updateUI();
			}});
		ipnbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(itemNow <= 0) itemNow = 1;
				ipn.add(tgl.get(--itemNow).bg,0);
				ipn.updateUI();
			}});
		ipnbs.add(ipnbr); ipnbs.add(ipnbl);
		
		
		FlowLayout ipnl = new FlowLayout();
		ipn.setLayout(ipnl);
		ipnl.setAlignment(0);
		ipnl.setHgap(0);
		ipnl.setVgap(0);
//---------------------------------------------------------------------------------------------
		mgl.setHorizontalGroup(
				mgl.createParallelGroup()
				.addGroup(mgl.createSequentialGroup()
						.addComponent(mb1)
						.addContainerGap(0,10000)
						.addComponent(search, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
						.addComponent(searchb,23,23,23)
						.addContainerGap())
				.addGroup(mgl.createSequentialGroup()
						.addComponent(mbl,0,mf.getWidth(),10000))
				.addGroup(mgl.createSequentialGroup()
						.addComponent(ipn,0,mf.getWidth(),10000)
						.addComponent(ipnbs,16,16,16)
						)
				.addComponent(diary,0,mf.getWidth(),10000)
				);
		
		mgl.setVerticalGroup(
				mgl.createSequentialGroup()
				.addGroup(
						mgl.createParallelGroup()
						.addComponent(mb1,23,23,23)
						.addGroup(
								mgl.createSequentialGroup()
								.addGap(1)
								.addComponent(search,19,19,19))
						.addComponent(searchb,21,21,21)
						.addComponent(mbl,23,23,23))
				.addGroup(mgl.createParallelGroup()
						.addComponent(ipn,25,25,25)
						.addComponent(ipnbs,25,25,25))
				.addComponent(diary,0,mf.getWidth(),10000)
				);

		tath.start();
		Main.mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		
		
//------------------------------------------------------------------------------
		{
			JLabel srls = new JLabel("尋找..."), srlf = new JLabel("取代...");
			JButton srnextb = new JButton("找下一個"), srnextpageb = new JButton("找下一篇"), srreplaceb = new JButton("　取代　"),srmarkb = new JButton("全部標記"), srrpallb = new JButton("全部取代"), srselworkb = new JButton("選擇工作..."), srselfileb = new JButton("選擇檔案...");		
			
			
			searfm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			searfm.setSize(250,240);
			searfm.setResizable(false);
			searfm.setLocationRelativeTo(mf);
			searfm.setLayout(null);
			searfd.setFont(diaryf); findfd.setFont(diaryf);
			searfd.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					searfd.setCaretPosition(0);
					searfd.setSelectionEnd(searfd.getText().length());
				}});
			
			findfd.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					findfd.setCaretPosition(0);
					findfd.setSelectionEnd(findfd.getText().length());
				}});
			srnextb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!searchfor()) JOptionPane.showMessageDialog(searfm, "搜尋已結束");
				}});
			srnextpageb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					srNextPage();
				}});
			srreplaceb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					replacefor();
					searchfor();
				}});
			srmarkb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					diaryta.setCaretPosition(0);
					mf.requestFocus();
					diaryta.requestFocus();
					try {
						while(!searfd.getText().equals("") &&(srnow<srnums.length || srfilenow<srfiles.length)) srNextPage();
					}
					catch(NullPointerException e2) {}
					while(searchformark(searfd.getText().replace("\\\\", "hwad89d uv9\niq2oe8").replace("\\n", "\n").replace("hwad89d uv9\niq2oe8", "\\\\")))
						try {
							searchhl.addHighlight(diaryta.getSelectionStart(), diaryta.getSelectionEnd(), searchhp);
						} catch (BadLocationException e1) {
							System.out.println("Main.main.search.highlight:Wrong!");
						}
//					diaryta.setCaretPosition(0);
//					diaryta.setSelectionEnd(0);
					}});
			srrpallb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					diaryta.setCaretPosition(0);
					while(searchfor() && !searfd.getText().equals("")) replacefor();
				}});
			JFrame searselfm = new JFrame();
			JPanel searselpan = new JPanel();
			JButton searselYes = new JButton("確認"), searselCancel = new JButton("取消"),searselAll = new JButton("全部選擇"),searselAlln = new JButton("全不選擇");
			srselworkb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox[] srselcheckal = new JCheckBox[tgl.size()];
					GroupLayout searselfg = new GroupLayout(searselfm.getContentPane()), searselpg = new GroupLayout(searselpan);
					for (int i = 0; i < tgl.size(); i++) {
							srselcheckal[i] = new JCheckBox(tgl.get(i).namespace.getText());
					}
					searselfm.setLocationRelativeTo(searfm);
					searselfm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					searselpan.setBorder(BorderFactory.createTitledBorder("選擇要執行的工作"));
					ParallelGroup searselpgp = searselpg.createParallelGroup();
					SequentialGroup searselpgs = searselpg.createSequentialGroup();
					for (int i = 0; i < srselcheckal.length; i++) {
						searselpgp.addComponent(srselcheckal[i]);
						searselpgs.addComponent(srselcheckal[i]);
					}
					searselpg.setVerticalGroup(searselpgs);
					searselpg.setHorizontalGroup(searselpgp);
					searselfg.setHorizontalGroup(searselfg.createSequentialGroup()
							.addContainerGap()
							.addGroup(searselfg.createParallelGroup(Alignment.CENTER)
								.addComponent(searselpan,0,75,10000)
								.addGroup(searselfg.createSequentialGroup()
										.addGap(0,10,10000)
										.addGroup(searselfg.createParallelGroup()
												.addComponent(searselAll)
												.addComponent(searselAlln))
										.addGap(15,15,15))
								.addGroup(searselfg.createSequentialGroup()
										.addContainerGap()
										.addComponent(searselYes)
										.addGap(0,10,10000)
										.addComponent(searselCancel)
										.addContainerGap()))
							.addContainerGap());
					searselfg.setVerticalGroup(searselfg.createSequentialGroup()
							.addContainerGap()
							.addComponent(searselpan)
							.addGap(7)
							.addComponent(searselAll)
							.addComponent(searselAlln)
							.addGap(10)
							.addGroup(searselfg.createParallelGroup()
									.addComponent(searselYes)
									.addComponent(searselCancel))
							.addContainerGap());
					searselfm.setLayout(searselfg);
					searselYes.requestFocus();
					searselfm.setSize(170,srselcheckal.length*23+160);
					searselpan.setLayout(searselpg);
					searselfm.setResizable(false);
					searselfm.setAlwaysOnTop(true);
					searselfm.setVisible(true);

					searselYes.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							srnow = 0;
							for (int i = 0; i < srselcheckal.length; i++)
								if (srselcheckal[i].isSelected()) srnums[i] = true;
						}});
					searselCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							searselfm.dispose();
						}});
					searselAll.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int size = srselcheckal.length;
							for(int i=0;i<size;i++) srselcheckal[i].setSelected(true);
						}});
					searselAlln.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int size = srselcheckal.length;
							for(int i=0;i<size;i++) srselcheckal[i].setSelected(false);
						}});
				}});
			srselfileb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser(mburlt.getText());
					jfc.setMultiSelectionEnabled(true);
					jfc.showOpenDialog(null);
					srfiles = jfc.getSelectedFiles();
					srfilenow = 0;
				}});
			
			srthis.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(srnow>Tag.now && srthis.isSelected()) srnow = Tag.now;
				}});
			srall.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(srall.isSelected()) srnow = 0;
				}});
			
			srls.setBounds(10,10,46,15); searfm.add(srls);
			srlf.setBounds(10,60,46,15); searfm.add(srlf);
			searfd.setBounds(10,30,120,20); searfm.add(searfd);
			findfd.setBounds(10,80,120,20); searfm.add(findfd);
			srkwd.setBounds(10,105,120,20); searfm.add(srkwd);
			srthis.setBounds(10,140,120,20);srthis.setSelected(true); searfm.add(srthis);
			srall.setBounds(10,160,120,20); searfm.add(srall);
			srselect.setBounds(10,180,120,20); searfm.add(srselect);
			srnextb.setBounds(150,10,86,25); searfm.add(srnextb);
			srnextpageb.setBounds(150,35,86,25); searfm.add(srnextpageb);
			srreplaceb.setBounds(150,60,86,25); searfm.add(srreplaceb);
			srmarkb.setBounds(150,85,86,25); searfm.add(srmarkb);
			srrpallb.setBounds(150,110,86,25); searfm.add(srrpallb);
			srselworkb.setBounds(136,145,100,25); searfm.add(srselworkb);
			srselfileb.setBounds(136,175,100,25); searfm.add(srselfileb);
			searfm.setAlwaysOnTop(true);
		}
//---------------------------------------------------------------------------------------
		tgl.add(new Tag());
		diaryta.requestFocus();
		
		Thread debug = new Debug();
		debug.start();
	}
}