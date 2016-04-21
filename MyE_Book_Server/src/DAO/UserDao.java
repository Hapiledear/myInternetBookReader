package DAO;

import java.sql.*;

import JDBC.DatabaseConnection;
import VO.User;

public class UserDao {
	private DatabaseConnection dbc;
	private Connection conn;
	
	 public UserDao(){
		// TODO Auto-generated constructor stub
		
	}
	/**
	 * 登陆的数据库操作
	 * @param name 数据字段
	 * @param pass 数据字段
	 * @return user对象，user.id为0则表示没有该用户
	 * @throws Exception
	 */
	public User login(String name,String pass) throws Exception{
		dbc=new DatabaseConnection();
		conn=dbc.getConnection();
		String sql="select * from tb_user where name=? and password=?";
		PreparedStatement pstm=conn.prepareStatement(sql);
		pstm.setString(1, name);
		pstm.setString(2, pass);
		ResultSet rs=pstm.executeQuery();
		if(rs.next()){
			User u=new User();
			u.setUserid(rs.getInt("id"));
			u.setName(rs.getString("name"));
			u.setPassword(rs.getString("password"));
			u.setState(rs.getInt("state"));
			conn.commit();
			pstm.close();
			conn.close();
			return u;
		}else{
		    User u=new User();
			u.setUserid(0);
			return u;
		}
	}

	/**
	 * 注册的数据库操作
	 * @param name 数据库字段
	 * @param pass 数据库字段
	 * @return true 操作成功，false 操作失败
	 * @throws Exception
	 */
	public boolean register(String name, String pass) throws Exception {
		// TODO Auto-generated method stub
		dbc=new DatabaseConnection();
		conn=dbc.getConnection();
		
		String sql="insert into tb_user(name,password,state) values(?,?,?)";
		PreparedStatement pstm=conn.prepareStatement(sql);
		pstm.setString(1, name);
		pstm.setString(2, pass);
		pstm.setInt(3, 1);
		if(pstm.executeUpdate()==1){
			  pstm.close();
			  conn.close();
			  return true;
			}else{
				return false;
			}

	}
	/**
	 * 检查是否重复
	 * @param name
	 *  @return true用户名已经存在，
	 */
	public boolean hasUser(String name) {
	
			try {
				dbc=new DatabaseConnection();
				conn=dbc.getConnection();
				String sql="select * from tb_user where name=?";
				PreparedStatement pstm;
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, name);
				ResultSet rs=pstm.executeQuery();
				if(rs.next()){
					return true;
				}else{
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		
	}
	
/*	public boolean mailregister(String name, String pass) throws Exception {
		// TODO Auto-generated method stub
		dbc=new DatabaseConnection();
		conn=dbc.getConnection();
		
		String sql="insert into tb_user(name,password,state,email,acidCode) values(?,?,?,?,?)";
		PreparedStatement pstm=conn.prepareStatement(sql);
		pstm.setString(1, name);
		pstm.setString(2, pass);
		pstm.setInt(3, 0);
		pstm.setString(4, name);
		pstm.setString(5, "acidCode");
		if(pstm.executeUpdate()==1){
			  pstm.close();
			  conn.close();
			  sendMail(name);
			  return true;
			}else{
				return false;
			}
		
		
	}*/
	/**
	 * 发送邮件
	 * @param name
	 */
/*	private void sendMail(String name) {
		// TODO Auto-generated method stub
		String url="请点击下面的连接进行激活\n"+"http://192.168.1.102:8080/ServerforAndroid/login.do?flag=3&name="+name+"&acidCode=acidCode";
		MailSend.send(name, url);
	}*/
	public boolean updateState(String name, String acidCode) throws Exception {
		// TODO Auto-generated method stub
		
		dbc=new DatabaseConnection();
		conn=dbc.getConnection();
		String sql="update tb_user set  state=1 where name=? and acidCode=?";
		PreparedStatement pstm=conn.prepareStatement(sql);
		pstm.setString(1, name);
		pstm.setString(2, acidCode);
		if(pstm.executeUpdate()==1){
			  pstm.close();
			  conn.close();
			  return true;
			}else{
				return false;
			}
	}
}
