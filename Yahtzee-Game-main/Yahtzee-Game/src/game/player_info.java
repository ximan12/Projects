package game;


import javax.swing.JTextArea;

public class player_info implements java.io.Serializable{
	
	private String name;
	private int turnNum=1;
	private int rollNum=0; 	
	private boolean[] able_flag = new boolean[13];
	
	private int acesTxt=-1,twosTxt=-1,threesTxt=-1,foursTxt=-1,fivesTxt=-1,sixsTxt=-1;
	private int threekTxt=-1,fourkTxt=-1,fullTxt=-1,smallTxt=-1,largeTxt=-1,yhzTxt=-1,yhzBnsTxt=-1,chanceTxt=-1;
	
	public void restart() {
		this.turnNum=1;
		this.rollNum=0;
		for(int i=0;i<13;i++) {
			able_flag[i] = true;
		}
		this.acesTxt = -1;
		this.twosTxt = -1;
		this.threesTxt =-1;
		this.foursTxt = -1;
		this.fivesTxt = -1;
		this.sixsTxt =-1;
		this.threekTxt = -1;
		this.fourkTxt = -1;
		this.fullTxt = -1;
		this.smallTxt = -1;
		this.largeTxt = -1;
		this.yhzTxt = -1;
		this.yhzBnsTxt = -1;
		this.chanceTxt = -1;
		
	}
	
	public boolean[] getableFlag() {
		return this.able_flag;
	}

	public int getRollNum() {
		return this.rollNum;
	}
	
	public int getTurnNum() {
		return this.turnNum;
	}
	
	
	
	public String getName() {
		return this.name;
	}
	
	public int getacesTxt() {
		return this.acesTxt;
	}
	public int gettwosTxt() {
		return this.twosTxt;
	}
	public int getthreesTxt() {
		return this.threesTxt;
	}
	public int getfoursTxt() {
		return this.foursTxt;
	}
	public int getfivesTxt() {
		return this.fivesTxt;
	}
	public int getsixsTxt() {
		return this.sixsTxt;
	}
	public int getthreekTxt() {
		return this.threekTxt;
	}
	public int getfourkTxt() {
		return this.fourkTxt;
	}
	public int getfullTxt() {
		return this.fullTxt;
	}
	public int getsmallTxt() {
		return this.smallTxt;
	}
	public int getlargeTxt() {
		return this.largeTxt;
	}
	public int getyhzTxt() {
		return this.yhzTxt;
	}
	public int getyhzbnsTxt() {
		return this.yhzBnsTxt;
	}
	public int getchanceTxt() {
		return this.chanceTxt;
	}
//input
	public void setName(String n) {
		this.name = n;
	}
	public void setrollNum(int n) {
		this.rollNum = n;
	}
	public void setturnNum(int n) {
		this.rollNum = n;
	}
	
	public void setacesTxt(int n) {
		this.acesTxt = n;
		
	}
	public void settwosTxt(int n) {
		this.twosTxt = n;
		
	}
	public void setthreesTxt(int n) {
		this.threesTxt = n;
		
	}
	public void setfoursTxt(int n) {
		this.foursTxt = n;
		
	}
	public void setfivesTxt(int n) {
		this.fivesTxt = n;
		
	}
	public void setsixsTxt(int n) {
		this.sixsTxt = n;
		
	}
	public void setthreekTxt(int n) {
		this.threekTxt = n;
		
	}
	public void setfourkTxt(int n) {
		this.fourkTxt = n;
		
	}
	public void setfullTxt(int n) {
		this.fullTxt = n;
		
	}
	public void setsmallTxt(int n) {
		this.smallTxt = n;
		
	}
	public void setlargeTxt(int n) {
		this.largeTxt = n;
		
	}
	public void setyhzBns(int n) {
		this.yhzBnsTxt = n;
	}

	public void setyhzTxt(int n) {
		this.yhzTxt = n;
	}
	public void setchanceTxt(int n) {
		this.chanceTxt = n;
		
	}
	public void setableflag(int i,boolean j) {
		this.able_flag[i]=j;
	}
	
	
	
}
