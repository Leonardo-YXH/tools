package demo.vehicle.transform;

public class YTransform {

	/**
	 * rotation & scale
	 */
	private YMatrix3x3 m_basis;
	/**
	 * translation
	 */
	private YVector3 m_origin;
	
	
	public YTransform(){
		this.m_basis=new YMatrix3x3(1, 0, 0, 0, 1, 0, 0, 0, 1);
		this.m_origin=new YVector3(0,0,0);
	}
	public YTransform(YQuaternion q,YVector3 position) {
		this.m_basis=new YMatrix3x3(q);
		this.m_origin=position;
	}
	
	public YTransform(YTransform other){
		this.m_basis=new YMatrix3x3(other.m_basis);
		this.m_origin=new YVector3(other.m_origin);
	}
	
	public void multiply(YTransform t){
		this.m_basis.multiply(t.m_basis);
		this.m_origin.add(t.m_origin.dot3(this.m_basis.m_el[0], this.m_basis.m_el[1], this.m_basis.m_el[2]));
	}
	
	public void multiply(YVector3 v){
		this.m_origin.add(v.dot3(this.m_basis.m_el[0], this.m_basis.m_el[1], this.m_basis.m_el[2]));
	}
	public void multiply(YQuaternion q){
		YQuaternion qtmp=this.m_basis.getRotation();
		qtmp.quat.multiply(q.quat);
		try {
			this.m_basis.setRotation(new YQuaternion(qtmp.quat.x, qtmp.quat.y, qtmp.quat.z, qtmp.quat.w));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public YMatrix3x3 getM_basis() {
		return m_basis;
	}

	public void setM_basis(YMatrix3x3 m_basis) {
		this.m_basis = new YMatrix3x3(m_basis);
	}

	public YVector3 getM_origin() {
		return m_origin;
	}

	public void setM_origin(YVector3 m_origin) {
		this.m_origin = new YVector3(m_origin);
	}
	public YQuaternion getRotation(){
		return this.m_basis.getRotation();
	}
}
