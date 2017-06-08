package demo.vehicle.transform;

public class YQuaternion {

	public YVector3 quat;
	
	public YQuaternion(double x,double y,double z,double w){
		setValue(x, y, z, w);
	}
	public YQuaternion(YVector3 axis,double angle){
		try {
			setRotation(axis, angle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public YQuaternion(double yaw,double pitch,double roll,boolean isZYX){
		if(isZYX){
			setEulerZYX(yaw,pitch,roll);
		}
		else{
			setEuler(yaw,pitch,roll);
		}
	}
	/**@brief Set the quaternion using Euler angles
	   * @param yaw Angle around Y
	   * @param pitch Angle around X
	   * @param roll Angle around Z */
	private void setEuler(double yaw, double pitch, double roll) {
		double halfYaw = yaw*0.5;  
		double halfPitch = pitch*0.5;  
		double halfRoll = roll*0.5;  
		double cosYaw = Math.cos(halfYaw);
		double sinYaw = Math.sin(halfYaw);
		double cosPitch = Math.cos(halfPitch);
		double sinPitch = Math.sin(halfPitch);
		double cosRoll = Math.cos(halfRoll);
		double sinRoll = Math.sin(halfRoll);
		setValue(cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw,
			cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw,
			sinRoll * cosPitch * cosYaw - cosRoll * sinPitch * sinYaw,
			cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw);
	}

	private void setValue(double x, double y, double z, double w) {
		quat=new YVector3(x, y, z);
		quat.w=w;
	}
	/**@brief Set the quaternion using euler angles 
	   * @param yaw Angle around Z
	   * @param pitch Angle around Y
	   * @param roll Angle around X */
	private void setEulerZYX(double yaw, double pitch, double roll) {
		double halfYaw = yaw*0.5;  
		double halfPitch = pitch*0.5;  
		double halfRoll = roll*0.5;  
		double cosYaw = Math.cos(halfYaw);
		double sinYaw = Math.sin(halfYaw);
		double cosPitch = Math.cos(halfPitch);
		double sinPitch = Math.sin(halfPitch);
		double cosRoll = Math.cos(halfRoll);
		double sinRoll = Math.sin(halfRoll);
		setValue(sinRoll * cosPitch * cosYaw - cosRoll * sinPitch * sinYaw, //x
                         cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw, //y
                         cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw, //z
                         cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw); //formerly yzx
	}

	/**
	 * @brief Set the rotation using axis angle notation
	 * @param axis The axis around which to rotate
	 * @param angle The magnitude of the rotation in Radians
	 */
	public void setRotation(YVector3 axis,double angle) throws Exception{
		double d=axis.length();
		if(d==0){
			throw new Exception("axis不能为零向量");
		}
		double s=Math.sin(angle*0.5)/d;
		setValue(axis.x*s, axis.y*s, axis.z*s, axis.w*s);
//		quat= YVector3.multiply(axis, s);
//		quat.w=axis.w*s;
	}
	
	public void multiply(double s){
		this.quat.multiply(s);
	}
	public void multiply(YQuaternion q){
		setValue(
	            quat.w * q.quat.x + quat.x * q.quat.w + quat.y * q.quat.z - quat.z * q.quat.y,
	            quat.w * q.quat.y + quat.y * q.quat.w + quat.z * q.quat.x - quat.x * q.quat.z,
	            quat.w * q.quat.z + quat.z * q.quat.w + quat.x * q.quat.y - quat.y * q.quat.x,
	            quat.w * q.quat.w - quat.x * q.quat.x - quat.y * q.quat.y - quat.z * q.quat.z);
	}
	public double dot(YQuaternion q){
		return quat.dot(q.quat);
	}
	
	
}
