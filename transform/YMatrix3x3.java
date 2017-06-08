package demo.vehicle.transform;

public class YMatrix3x3 {

	public YVector3[] m_el;
	
	public YMatrix3x3(double xx,double xy,double xz,
			double yx,double yy,double yz,
			double zx,double zy,double zz){
		this.m_el=new YVector3[3];
		for(int i=0;i<this.m_el.length;i++){
			this.m_el[i]=new YVector3(0, 0, 0);
		}
		setValue(xx, xy, xz, yx, yy, yz, zx, zy, zz);
	}
	public YMatrix3x3(YVector3 r0,YVector3 r1,YVector3 r2){
		this.m_el=new YVector3[3];
		m_el[0]=YVector3.copy(r0);
		m_el[1]=YVector3.copy(r1);
		m_el[2]=YVector3.copy(r2);
	}
	public YMatrix3x3(YMatrix3x3 other){
		this.m_el=new YVector3[3];
		m_el[0]=YVector3.copy(other.m_el[0]);
		m_el[1]=YVector3.copy(other.m_el[1]);
		m_el[2]=YVector3.copy(other.m_el[2]);
	}
	public YMatrix3x3(YQuaternion q){
		this.m_el=new YVector3[3];
		for(int i=0;i<this.m_el.length;i++){
			this.m_el[i]=new YVector3(0, 0, 0);
		}
		try {
			setRotation(q);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static YMatrix3x3 copy(YMatrix3x3 other){
		YMatrix3x3 m=new YMatrix3x3(other);
		return m;
	}
	public void setValue(double xx,double xy,double xz,
			double yx,double yy,double yz,
			double zx,double zy,double zz){
		this.m_el[0].setValue(xx, xy, xz);
		this.m_el[1].setValue(yx, yy, yz);
		this.m_el[2].setValue(zx, zy, zz);
	}
	
	public YVector3 getColumn(int i){
		return new YVector3(m_el[0].getAxis(i), m_el[1].getAxis(i), m_el[2].getAxis(i));
	}
	
	public YVector3 getRow(int i){
		return YVector3.copy(m_el[i]);
	}
	
	public void add(YMatrix3x3 m){
		m_el[0].add(m.m_el[0]);
		m_el[1].add(m.m_el[1]);
		m_el[2].add(m.m_el[2]);
	}
	public static YMatrix3x3 add(YMatrix3x3 m0,YMatrix3x3 m1){
		YMatrix3x3 o=new YMatrix3x3(m0);
		o.add(m1);
		return o;
	}
	public void minus(YMatrix3x3 m){
		m_el[0].minus(m.m_el[0]);
		m_el[1].minus(m.m_el[1]);
		m_el[2].minus(m.m_el[2]);
	}
	public static YMatrix3x3 minus(YMatrix3x3 m0,YMatrix3x3 m1){
		YMatrix3x3 o=new YMatrix3x3(m0);
		o.minus(m1);
		return o;
	}
	public void multiply(YMatrix3x3 m){
		setValue(m.tdotx(m_el[0]), m.tdoty(m_el[0]), m.tdotz(m_el[0]),
				m.tdotx(m_el[1]), m.tdoty(m_el[1]), m.tdotz(m_el[1]),
				m.tdotx(m_el[2]), m.tdoty(m_el[2]), m.tdotz(m_el[2]));
		
		
	}
	public static YMatrix3x3 multiply(YMatrix3x3 m0,YMatrix3x3 m1){
		YMatrix3x3 o=new YMatrix3x3(m0);
		o.multiply(m1);
		return o;
	}
	public YVector3 rightMultiply(YVector3 v){
		return new YVector3(m_el[0].dot(v), m_el[1].dot(v), m_el[2].dot(v));
	}
	
	public YVector3 leftMultiply(YVector3 v){
		return new YVector3(this.tdotx(v), this.tdoty(v), this.tdotz(v));
	}
	public void multiply(double k){
		this.m_el[0].multiply(k);
		this.m_el[1].multiply(k);
		this.m_el[2].multiply(k);
	}
	public void setRotation(YQuaternion q) throws Exception{
		double d=q.quat.length2();
		if(d==0){
			throw new Exception("不能为零向量");
		}
		double s=2.0/d;
		double xs = q.quat.x * s,   ys = q.quat.y * s,   zs = q.quat.z * s;
		double wx = q.quat.w * xs,  wy = q.quat.w * ys,  wz = q.quat.w * zs;
		double xx = q.quat.x * xs,  xy = q.quat.x * ys,  xz = q.quat.x * zs;
		double yy = q.quat.y * ys,  yz = q.quat.y * zs,  zz = q.quat.z * zs;
		setValue(
            1.0 - (yy + zz), xy - wz, xz + wy,
			xy + wz, 1.0 - (xx + zz), yz - wx,
			xz - wy, yz + wx, 1.0 - (xx + yy));
	}
	/** @brief Set the matrix from euler angles using YPR around YXZ respectively
	*  @param yaw Yaw about Y axis
	*  @param pitch Pitch about X axis
	*  @param roll Roll about Z axis 
	*/
	public void setEulerYPR(double yaw,double pitch,double roll){
		setEulerZYX(roll,pitch,yaw);
	}
	/** @brief Set the matrix from euler angles YPR around ZYX axes
	* @param eulerX Roll about X axis
	* @param eulerY Pitch around Y axis
	* @param eulerZ Yaw aboud Z axis
	* 
	* These angles are used to produce a rotation matrix. The euler
	* angles are applied in ZYX order. I.e a vector is first rotated 
	* about X then Y and then Z
	**/
	public void setEulerZYX(double eulerX, double eulerY, double eulerZ) {
		double ci =Math.cos(eulerX); 
		double cj =Math.cos(eulerY); 
		double ch =Math.cos(eulerZ); 
		double si =Math.sin(eulerX); 
		double sj =Math.sin(eulerY); 
		double sh =Math.sin(eulerZ); 
		double cc = ci * ch; 
		double cs = ci * sh; 
		double sc = si * ch; 
		double ss = si * sh;

		setValue(cj * ch, sj * sc - cs, sj * cc + ss,
			cj * sh, sj * ss + cc, sj * cs - sc, 
			-sj,      cj * si,      cj * ci);
	}
	/**
	 * 设置为单位矩阵
	 */
	public void setIdentity(){
		setValue(1d, 0, 0,
				0, 1d, 0, 
				0, 0, 1d);
	}
	/**
	 * 将matrix3x3转成quaternion
	 * @return
	 */
	public YQuaternion getRotation(){
		double trace=m_el[0].x+m_el[1].y+m_el[2].z;
		double[] temp=new double[4];
		if (trace > 0) 
		{
			double s =Math.sqrt(trace + 1.0);
			temp[3]=(s * 0.5);
			s = 0.5 / s;

			temp[0]=((m_el[2].y - m_el[1].z) * s);
			temp[1]=((m_el[0].z - m_el[2].x) * s);
			temp[2]=((m_el[1].x - m_el[0].y) * s);
		} 
		else 
		{
			int i = m_el[0].x < m_el[1].y ? 
				(m_el[1].y < m_el[2].z ? 2 : 1) :
				(m_el[0].x < m_el[2].z ? 2 : 0); 
			int j = (i + 1) % 3;  
			int k = (i + 2) % 3;

			double s = Math.sqrt(m_el[i].getAxis(i) - m_el[j].getAxis(j) - m_el[k].getAxis(k) + 1.0);
			temp[i] = s * 0.5;
			s = 0.5 / s;

			temp[3] = (m_el[k].getAxis(j) - m_el[j].getAxis(k)) * s;
			temp[j] = (m_el[j].getAxis(i) + m_el[i].getAxis(j)) * s;
			temp[k] = (m_el[k].getAxis(i) + m_el[i].getAxis(k)) * s;
		}
		return new YQuaternion(temp[0], temp[1], temp[2], temp[3]);
	}
	/**
	 * 将matrix3x3转成yaw,pitch,roll
	 * @brief Get the matrix represented as euler angles around YXZ, roundtrip with setEulerYPR
	* <br> yaw Yaw around Y axis
	* <br> pitch Pitch around X axis
	* <br>roll around Z axis
	 * @return x:yaw,y:pitch,z:roll
	 */
	public YVector3 getEulerYPR(){
		// first use the normal calculus
		double yaw = Math.atan2(m_el[1].x, m_el[0].x);
		double pitch = Math.asin(-m_el[2].x);
		double roll = Math.atan2(m_el[2].y, m_el[2].z);

		// on pitch = +/-HalfPI
		if (Math.abs(pitch)==Math.PI)
		{
			if (yaw>0)
				yaw-=Math.PI;
			else
				yaw+=Math.PI;

			if (roll>0)
				roll-=Math.PI;
			else
				roll+=Math.PI;
		}
		return new YVector3(yaw, pitch, roll);
	}
	/**@brief Get the matrix represented as euler angles around ZYX
	* <br> yaw Yaw around X axis
	* <br> pitch Pitch around Y axis
	* <br> roll around X axis 
	* <br> solution_number Which solution of two possible solutions ( 1 or 2) are possible values
	* @param solution_number
	* @return x:yaw,y:pitch,z:roll
	**/	
	public YVector3 getEulerZYX(int solution_number){
		YVector3 euler_out=new YVector3(0, 0, 0);
		YVector3 euler_out2=new YVector3(0, 0, 0); //second solution
		//get the pointer to the raw data

		// Check that pitch is not at a singularity
		if (Math.abs(m_el[2].x) >= 1)
		{
			euler_out.x = 0;
			euler_out2.x = 0;

			// From difference of angles formula
			double delta = Math.atan2(m_el[0].x,m_el[0].z);
			if (m_el[2].x > 0)  //gimbal locked up
			{
				euler_out.y = Math.PI / 2.0;
				euler_out2.y = Math.PI / 2.0;
				euler_out.z = euler_out.y + delta;
				euler_out2.z = euler_out.y + delta;
			}
			else // gimbal locked down
			{
				euler_out.y = -Math.PI / 2.0;
				euler_out2.y = -Math.PI / 2.0;
				euler_out.z = -euler_out.y + delta;
				euler_out2.z = -euler_out.y + delta;
			}
		}
		else
		{
			euler_out.y = - Math.asin(m_el[2].x);
			euler_out2.y = Math.PI - euler_out.y;

			euler_out.z = Math.atan2(m_el[2].y/Math.cos(euler_out.y), 
				m_el[2].z/Math.cos(euler_out.y));
			euler_out2.z = Math.atan2(m_el[2].y/Math.cos(euler_out2.y), 
				m_el[2].z/Math.cos(euler_out2.y));

			euler_out.x = Math.atan2(m_el[1].x/Math.cos(euler_out.y), 
				m_el[0].x/Math.cos(euler_out.y));
			euler_out2.x = Math.atan2(m_el[1].x/Math.cos(euler_out2.y), 
				m_el[0].x/Math.cos(euler_out2.y));
		}

		if (solution_number == 1)
		{ 
//			yaw = euler_out.yaw; 
//			pitch = euler_out.pitch;
//			roll = euler_out.roll;
			return euler_out;
		}
		else
		{ 
//			yaw = euler_out2.yaw; 
//			pitch = euler_out2.pitch;
//			roll = euler_out2.roll;
			return euler_out2;
		}
	}
	
	public YMatrix3x3 scaled(YVector3 s){
		return new YMatrix3x3(m_el[0].x * s.x, m_el[0].y * s.y, m_el[0].z * s.z,
				m_el[1].x * s.x, m_el[1].y * s.y, m_el[1].z * s.z,
				m_el[2].x * s.x, m_el[2].y * s.y, m_el[2].z * s.z);
	}
	/**
	 * Solve A * x = b, where b is a column vector. This is more efficient
	 *<br> than computing the inverse in one-shot cases.
	 *<br>Solve33 is from Box2d, thanks to Erin Catto,
	 * @param b
	 * @return x
	 */
	public YVector3 solve33(YVector3 b){
		YVector3 col1 = getColumn(0);
		YVector3 col2 = getColumn(1);
		YVector3 col3 = getColumn(2);
		
		double det = col1.dot(col2.cross(col3));
		if (Math.abs(det)>1.192092896e-07F)
		{
			det = 1.0 / det;
		}
		
		double x = det * b.dot(col2.cross(col3));//btDot(b, btCross(col2, col3));
		double y = det * col1.dot(b.cross(col3));//btDot(col1, btCross(b, col3));
		double z = det * col1.dot(col2.cross(b));//btDot(col1, btCross(col2, b));
		return new YVector3(x, y, z);
	}
	
	public double tdotx(YVector3 v){
		return m_el[0].x * v.x + m_el[1].x * v.y + m_el[2].x * v.z;
	}
	public double tdoty(YVector3 v){
		return m_el[0].y * v.x + m_el[1].y * v.y + m_el[2].y * v.z;
	}
	public double tdotz(YVector3 v){
		return m_el[0].z * v.x + m_el[1].z * v.y + m_el[2].z * v.z;
	}
	public double cofac(int r1,int c1,int r2,int c2){
		return m_el[r1].getAxis(c1)*m_el[r2].getAxis(c2)-m_el[r1].getAxis(c2)*m_el[r2].getAxis(c1);
	}
	
	public double determinant(){
		return m_el[0].triple(m_el[1], m_el[2]);
	}
	
	public YMatrix3x3 absolute(){
		YMatrix3x3 o=new YMatrix3x3(m_el[0].absolute(), m_el[1].absolute(), m_el[2].absolute());
		return o;
	}
	
	public YMatrix3x3 transpose(){
		return new YMatrix3x3(m_el[0].x, m_el[1].x, m_el[2].x,
                			  m_el[0].y, m_el[1].y, m_el[2].y,
                			  m_el[0].z, m_el[1].z, m_el[2].z);
	}
	/**
	 * 伴随矩阵
	 * @return
	 */
	public YMatrix3x3 adjoint(){
		return new YMatrix3x3(cofac(1, 1, 2, 2), cofac(0, 2, 2, 1), cofac(0, 1, 1, 2),
				cofac(1, 2, 2, 0), cofac(0, 0, 2, 2), cofac(0, 2, 1, 0),
				cofac(1, 0, 2, 1), cofac(0, 1, 2, 0), cofac(0, 0, 1, 1));
	}
	
	public YMatrix3x3 inverse() throws Exception{
		YVector3 co=new YVector3(cofac(1, 1, 2, 2), cofac(1, 2, 2, 0), cofac(1, 0, 2, 1));
		double det = this.m_el[0].dot(co);
		//btFullAssert(det != btScalar(0.0));
		if(det == 0.0){
			throw new Exception("矩阵不可逆") ;
		}
		double s = 1.0 / det;
		return new YMatrix3x3(co.x * s, cofac(0, 2, 2, 1) * s, cofac(0, 1, 1, 2) * s,
			co.y * s, cofac(0, 0, 2, 2) * s, cofac(0, 2, 1, 0) * s,
			co.z * s, cofac(0, 1, 2, 0) * s, cofac(0, 0, 1, 1) * s);
	}
	
	
	public static void main(String[] args) {
		YMatrix3x3 m=new YMatrix3x3(0, 0, 0, 0, 0, 0, 0, 0, 0);
		System.out.println(m);
	}
}
