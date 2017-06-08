package demo.vehicle.transform;

public class YVector3 {

	public double x;
		
	public double y;
	
	public double z;
	
	public double w;

	
	public YVector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 0;
	}
	public YVector3(YVector3 other){
		this.x=other.x;
		this.y=other.y;
		this.z=other.z;
	}
	public static YVector3 copy(YVector3 v){
		return new YVector3(v.x,v.y,v.z);
	}
	
	public double getAxis(int i){
		switch(i){
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
		default:
			return x;
		}
	}
	public void add(YVector3 v){
		this.x+=v.x;
		this.y+=v.y;
		this.z+=v.z;
	}
	public void minus(YVector3 v){
		this.x-=v.x;
		this.y-=v.y;
		this.z-=v.z;
	}
	
	public void multiply(double scale){
		this.x*=scale;
		this.y*=scale;
		this.z*=scale;
	}
	
	public void divide(double scale){
		if(scale==0){
			return ;
		}
		double s=1d/scale;
		multiply(s);
	}
	
	public double dot(YVector3 v){
		return this.x*v.x+this.y*v.y+this.z*v.z;
	}
	
	public double length2(){
		return dot(this);
	}
	
	public double length(){
		return Math.sqrt(length2());
	}
	
	public double norm(){
		return length();
	}
	
	public double safeNorm(){
		double d=length2();
		if(d>1.192092896e-07d){
			return Math.sqrt(d);
		}
		return 0d;
	}
	/**
	 * 与v的欧氏距离的平方,不改变向量自身
	 * @param v
	 * @return
	 */
	public double distance2(YVector3 v){
		YVector3 copyV=copy(this);
		copyV.minus(v);
		return copyV.length2();
	}
	/**
	 * 与v的欧氏距离,不改变向量自身
	 * @param v
	 * @return
	 */
	public double distance(YVector3 v){
		return Math.sqrt(distance2(v));
	}
	
	public void normalize(){
		this.divide(this.length());
	}
	
	public YVector3 normalized(){
		YVector3 v = YVector3.copy(this);
		v.normalize();
		return v;
	}
	/**
	 * rotate angle around wAxis
	 * @param wAxis wAxis must be a unit lenght vector(单位向量)
	 * @param angle
	 * @return
	 */
	public YVector3 rotate(YVector3 wAxis,double angle){
		YVector3 o=YVector3.multiply(wAxis, wAxis.dot(this));
		YVector3 _x=YVector3.minus(this, o);
		YVector3 _y=wAxis.cross(this);
		return (YVector3.add(YVector3.add(o, YVector3.multiply(_x, Math.cos(angle))),YVector3.multiply(_y, Math.sin(angle))));
	}
	
	public static YVector3 multiply(YVector3 wAxis, double scale) {
		return new YVector3(wAxis.x*scale,wAxis.y*scale,wAxis.z*scale);
	}
	public double angle(YVector3 v){
		double s=Math.sqrt(this.length2()*v.length2());
		if(s==0){
			return 0;
		}
		return Math.acos(dot(v)/s);
	}
	
	public YVector3 absolute(){
		return new YVector3(Math.abs(this.x),Math.abs(this.y),Math.abs(this.z));
	}
	/**
	 * 与向量v的叉积
	 * @param v
	 * @return
	 */
	public YVector3 cross(YVector3 v){
		return new YVector3(this.y*v.z-this.z*v.y,
							this.z*v.x-this.x*v.z,
							this.x*v.y-this.y*v.x);
	}
	
	public double triple(YVector3 v1,YVector3 v2){
		return this.x*(v1.y*v2.z-v1.z*v2.y)+
				this.y*(v1.z*v2.x-v1.x*v2.z)+
				this.z*(v1.x*v2.y-v1.y*v2.x);
	}
	
	public int minAxis(){
		//TODO
		return 0;
	}
	public int maxAxis(){
		//TODO
		return 0;
	}
	public int furthestAxis(){
		//TODO
		return 0;
	}
	public int closestAxis(){
		//TODO
		return 0;
	}
	
	public void setInterpolate3(YVector3 v0,YVector3 v1,double rt){
		double s=1d-rt;
		this.x=s*v0.x+rt*v1.x;
		this.y=s*v0.y+rt*v1.y;
		this.z=s*v0.z+rt*v1.z;
	}
	
	public YVector3 lerp(YVector3 v,double t){
		return new YVector3(this.x+(v.x-this.x)*t, 
				this.y+(v.y-this.y)*t, 
				this.z+(v.z-this.z)*t);
	}
	
	public void multiply(YVector3 v){
		this.x*=v.x;
		this.y*=v.y;
		this.z*=v.z;
	}
	
	public void setValue(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
		this.w=0;
	}
	/**
	 * 设置三个斜对称矩阵
	 * @param v0
	 * @param v1
	 * @param v2
	 */
	public void setSkewSymmetricMatrix(YVector3 v0,YVector3 v1,YVector3 v2){
		v0.setValue(0, -z, y);
		v1.setValue(z, 0, -x);
		v2.setValue(-y, x, 0);
	}
	
	public void setZero(){
		setValue(0, 0, 0);
	}
	
	public boolean isZero(){
		return this.x==0&&this.y==0&&this.z==0;
	}
	
	public YVector3 dot3(YVector3 v0,YVector3 v1,YVector3 v2){
		return new YVector3(this.dot(v0), this.dot(v1), this.dot(v2));
	}
	/**
	 * v0+v1
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static YVector3 add(YVector3 v0,YVector3 v1){
		YVector3 v=YVector3.copy(v0);
		v.add(v1);
		return v;
	}
	/**
	 * v0-v1
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static YVector3 minus(YVector3 v0,YVector3 v1){
		YVector3 v=YVector3.copy(v0);
		v.minus(v1);
		return v;
	}
	public static YVector3 multiply(YVector3 v0,YVector3 v1){
		YVector3 v=YVector3.copy(v0);
		v.multiply(v1);
		return v;
	}
	
	public static YVector3 divide(YVector3 v0,YVector3 v1){
		return new YVector3(v0.x/v1.x, v0.y/v1.y, v0.z/v1.z);
	}
	public String toString(){
		return "x:"+x+" y:"+y+" z:"+z;
	}
	public static void main(String[] args) {
		YVector3 vo=new YVector3(0, 0, 2);
		YVector3 v=new YVector3(2, 0, 0);
		
		System.out.println(v.rotate(vo.normalized(), Math.PI/2).toString());
	}
}
