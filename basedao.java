public class BaseDao {
	private static final String DRIVER="com.mysql.jdbc.Driver"; 
	private static final String URL="jdbc:mysql:///exam_1?characterEncoding=utf8";
	private static final String USER="root";
	private static final String PWD="accp";
	
	static{
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL, USER, PWD);
		
	}
	
	private PreparedStatement createStatement(Connection conn ,String sql,Object...args) throws SQLException{
		PreparedStatement ps=conn.prepareStatement(sql);
		if(args!=null){
			for (int i = 0; i < args.length; i++) {
				ps.setObject((i+1),args[i]);
			}
		}
		
		return ps;
	}
	
	
	private void release(Connection conn,PreparedStatement ps,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	
	//增删改
	public Integer update(String sql,Object...args){
		Connection conn=null;
		PreparedStatement ps=null;
		Integer result=null;
		
		try{
			conn=getConnection();
			ps=createStatement(conn, sql, args);
			
			result=ps.executeUpdate();
			
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			release(conn, ps, null);
		}
		
		
		return result;
		
		
	}
	
	public <E> List<E> queryAll(RowMapper<E> rowMapper,String sql,Object...args){
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<E> list=new ArrayList<E>();
		
		try{
			conn=getConnection();
			ps=createStatement(conn, sql, args);
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				list.add(rowMapper.rowMapper(rs));
			}
			
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			release(conn, ps, rs);
		}
		
		
		return list;
	}
	
	public <E>E querySingle(RowMapper<E> rowMapper,String sql,Object...args){
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		
		try{
			conn=getConnection();
			ps=createStatement(conn, sql, args);
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				return rowMapper.rowMapper(rs);
			}
			
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			release(conn, ps, rs);
		}
		
		
		return null;
	}
	
	
}
