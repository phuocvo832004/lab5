

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class registerDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	Connection con = null;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					registerDialog frame = new registerDialog();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public registerDialog() {
		
		String url = "jdbc:oracle:thin:@localhost:1521:orcllab5";
		String user = "c##lab5";
		String password = "123456";
		String driver = "oracle.jdbc.driver.OracleDriver";
		try {
		    Class.forName(driver);
		    con = DriverManager.getConnection(url, user, password);
		    System.out.println("Connected to the database");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		setTitle("Register Dialog");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 456, 381);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 422, 245);
		contentPane.add(panel);
		panel.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(164, 10, 213, 41);
		panel.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(10, 10, 117, 41);
		panel.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPassword.setBounds(10, 82, 117, 41);
		panel.add(lblPassword);
		
		JLabel lblConfirm = new JLabel("Confirm");
		lblConfirm.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblConfirm.setBounds(10, 158, 117, 41);
		panel.add(lblConfirm);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(165, 81, 212, 42);
		panel.add(txtPassword);
		
		txtConfirm = new JPasswordField();
		txtConfirm.setBounds(165, 160, 212, 42);
		panel.add(txtConfirm);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 265, 422, 69);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnSignin = new JButton("Sign in");
		btnSignin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText();
				String password=String.valueOf(txtPassword.getPassword());
				if(username.isEmpty() || password.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ thông tin đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            txtUsername.requestFocus();
		            return;
				}else {
					try {
						String checkSql = "SELECT USERNAME, PASSWORD FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
		                PreparedStatement checkStatement = con.prepareStatement(checkSql);
		                checkStatement.setString(1, username);
		                checkStatement.setString(2, password);
		                
		                ResultSet resultSet = checkStatement.executeQuery();
			            if (resultSet.next()) {
			                JOptionPane.showMessageDialog(null, "Đăng nhập thành công", "", JOptionPane.ERROR_MESSAGE);
			                dispose();
			                ItemManagement itemManagement = new ItemManagement();
			                itemManagement.setLocationRelativeTo(null);
							itemManagement.setVisible(true);
			            }else {
			                JOptionPane.showMessageDialog(null, "Đăng nhập thất bại, kiểm tra lại username hoặc password!", "", JOptionPane.ERROR_MESSAGE);
			                return;
			            }
					} catch (SQLException ex) {
						ex.printStackTrace();
			            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
		btnSignin.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSignin.setBounds(0, 10, 134, 49);
		panel_1.add(btnSignin);
		
		JButton btnSignup = new JButton("Sign up");
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText();
				String password= String.valueOf(txtPassword.getPassword());
				String confirm = String.valueOf(txtConfirm.getPassword());
				if(username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ thông tin đăng ký.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            txtUsername.requestFocus();
		            return;
				}else {
					if(!confirm.equals(password)) {
			            JOptionPane.showMessageDialog(null, "Password confirm phải giống với password!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			            txtConfirm.requestFocus();
			            return;
					}else {
						try {
							String checkSql = "SELECT USERNAME FROM USERS WHERE USERNAME = ?";
				            PreparedStatement checkStatement = con.prepareStatement(checkSql);
				            checkStatement.setString(1, username);

				            ResultSet resultSet = checkStatement.executeQuery();
				            if (resultSet.next()) {
				                JOptionPane.showMessageDialog(null, "Username đã tồn tại, vui lòng chọn username khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
				                txtUsername.requestFocus();
				            } else {
				                String insertSql = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?)";
				                PreparedStatement insertStatement = con.prepareStatement(insertSql);
				                insertStatement.setString(1, username);
				                insertStatement.setString(2, password);

				                int rowsInserted = insertStatement.executeUpdate();
				                if (rowsInserted > 0) {
				                    JOptionPane.showMessageDialog(null, "Đăng ký thành công!", "", JOptionPane.INFORMATION_MESSAGE);
				                    dispose();
				                    ItemManagement itemManagement = new ItemManagement();
				                    itemManagement.setLocationRelativeTo(null);
				                    itemManagement.setVisible(true);
				                } else {
				                    JOptionPane.showMessageDialog(null, "Đăng ký thất bại, vui lòng thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
				                }
				            }
						} catch (SQLException ex) {
							ex.printStackTrace();
				            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
						}
					}

				}
			}
		});
		btnSignup.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSignup.setBounds(144, 10, 134, 49);
		panel_1.add(btnSignup);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(288, 10, 134, 49);
		panel_1.add(btnCancel);
	}
}
